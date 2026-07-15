package dev.chudziudgi.ohmc.drop.paper.feature.crafting;

import dev.chudziudgi.ohmc.drop.paper.feature.crafting.configuration.CraftingConfiguration;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.configuration.CraftingInventoryConfiguration;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.configuration.CraftingMessages;
import dev.chudziudgi.ohmc.drop.paper.multification.CraftingMultification;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class CraftingService {

    private final CraftingConfiguration configuration;
    private final CraftingInventoryConfiguration inventoryConfiguration;
    private final CraftingMessages messages;
    private final CraftingMultification multification;
    private final Plugin plugin;

    public CraftingService(
            CraftingConfiguration configuration,
            CraftingInventoryConfiguration inventoryConfiguration,
            CraftingMessages messages,
            CraftingMultification multification,
            Plugin plugin
    ) {
        this.configuration = configuration;
        this.inventoryConfiguration = inventoryConfiguration;
        this.messages = messages;
        this.multification = multification;
        this.plugin = plugin;
        this.registerRecipes();
    }

    public void addRecipe(CustomRecipe recipe) {
        this.configuration.recipes.add(recipe);
        this.configuration.save();
        this.registerRecipes();
    }

    public boolean removeRecipe(String name) {
        boolean removed = this.configuration.recipes.removeIf(r -> r.name().equalsIgnoreCase(name));
        if (removed) {
            this.configuration.save();
            this.registerRecipes();
        }
        return removed;
    }

    public CustomRecipe findRecipe(String name) {
        return this.configuration.recipes.stream()
                .filter(r -> r.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public CraftingMultification getMultification() {
        return this.multification;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public boolean hasIngredients(Player player, CustomRecipe recipe) {
        PlayerInventory inventory = player.getInventory();
        Map<ItemStack, Integer> requiredItems = new HashMap<>();

        for (ItemStack ingredient : recipe.ingredients().values()) {
            if (ingredient == null) {
                continue;
            }

            boolean found = false;
            for (Map.Entry<ItemStack, Integer> entry : requiredItems.entrySet()) {
                if (entry.getKey().isSimilar(ingredient)) {
                    entry.setValue(entry.getValue() + ingredient.getAmount());
                    found = true;
                    break;
                }
            }

            if (!found) {
                requiredItems.put(ingredient.clone(), ingredient.getAmount());
            }
        }

        for (Map.Entry<ItemStack, Integer> entry : requiredItems.entrySet()) {
            ItemStack item = entry.getKey();
            int required = entry.getValue();
            int found = 0;

            for (ItemStack invItem : inventory.getContents()) {
                if (invItem != null && invItem.isSimilar(item)) {
                    found += invItem.getAmount();
                }
            }

            if (found < required) {
                return false;
            }
        }

        return true;
    }

    public boolean craft(Player player, CustomRecipe recipe) {
        if (!this.hasIngredients(player, recipe)) {
            this.multification.create()
                    .viewer(player)
                    .notice(messages -> messages.missingIngredients)
                    .send();
            return false;
        }

        PlayerInventory inventory = player.getInventory();
        if (inventory.firstEmpty() == -1) {

            boolean hasSpace = false;
            for (ItemStack item : inventory.getContents()) {
                if (item != null && item.isSimilar(recipe.result())) {
                    if (item.getAmount() + recipe.result().getAmount() <= item.getMaxStackSize()) {
                        hasSpace = true;
                        break;
                    }
                }
            }

            if (!hasSpace) {
                this.multification.create()
                        .viewer(player)
                        .notice(messages -> messages.noSpace)
                        .send();
                return false;
            }
        }

        Map<ItemStack, Integer> requiredItems = new HashMap<>();
        for (ItemStack ingredient : recipe.ingredients().values()) {
            if (ingredient == null) {
                continue;
            }

            boolean found = false;
            for (Map.Entry<ItemStack, Integer> entry : requiredItems.entrySet()) {
                if (entry.getKey().isSimilar(ingredient)) {
                    entry.setValue(entry.getValue() + ingredient.getAmount());
                    found = true;
                    break;
                }
            }

            if (!found) {
                requiredItems.put(ingredient.clone(), ingredient.getAmount());
            }
        }

        for (Map.Entry<ItemStack, Integer> entry : requiredItems.entrySet()) {
            ItemStack item = entry.getKey();
            int toRemove = entry.getValue();

            ItemStack[] contents = inventory.getContents();
            for (int i = 0; i < contents.length; i++) {
                ItemStack invItem = contents[i];
                if (invItem != null && invItem.isSimilar(item)) {
                    if (invItem.getAmount() > toRemove) {
                        invItem.setAmount(invItem.getAmount() - toRemove);
                        inventory.setItem(i, invItem);
                        toRemove = 0;
                        break;
                    } else {
                        toRemove -= invItem.getAmount();
                        inventory.setItem(i, null);
                        if (toRemove <= 0) {
                            break;
                        }
                    }
                }
            }
        }
        inventory.addItem(recipe.result().clone());

        this.multification.create()
                .viewer(player)
                .notice(messages -> messages.craftingSuccess)
                .send();

        return true;
    }

    public CraftingConfiguration getConfiguration() {
        return configuration;
    }

    public CraftingInventoryConfiguration getInventoryConfiguration() {
        return inventoryConfiguration;
    }

    public CraftingMessages getMessages() {
        return messages;
    }


    public void registerRecipes() {

        for (int i = 0; i < this.configuration.recipes.size(); i++) {
            CustomRecipe recipe = this.configuration.recipes.get(i);

            try {
                NamespacedKey key = new NamespacedKey(plugin, "custom_recipe_" + i);

                Bukkit.removeRecipe(key);

                ShapedRecipe shapedRecipe = new ShapedRecipe(key, recipe.result().clone());

                shapedRecipe.shape("ABC", "DEF", "GHI");

                char[] slots = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
                boolean hasIngredients = false;

                for (int slot = 0; slot < 9; slot++) {
                    ItemStack ingredient = recipe.ingredients().get(slot);

                    if (ingredient != null && ingredient.getType() != org.bukkit.Material.AIR) {
                        shapedRecipe.setIngredient(slots[slot], ingredient.getType());
                        hasIngredients = true;
                    } else {
                        shapedRecipe.setIngredient(slots[slot], org.bukkit.Material.AIR);
                    }
                }

                if (hasIngredients) {
                    Bukkit.addRecipe(shapedRecipe);
                }

            } catch (Exception e) {
                plugin.getLogger().warning("Nie można zarejestrować receptury: " + recipe.name() + " - " + e.getMessage());
            }
        }
    }
}
