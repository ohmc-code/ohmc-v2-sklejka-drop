package dev.chudziudgi.ohmc.drop.paper.feature.crafting;

import dev.chudziudgi.ohmc.drop.paper.feature.crafting.configuration.CraftingConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class CraftingController implements Listener {

    private final CraftingService craftingService;
    private final Plugin plugin;

    public CraftingController(CraftingService craftingService, Plugin plugin) {
        this.craftingService = craftingService;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        if (!(recipe instanceof ShapedRecipe shapedRecipe)) {
            return;
        }

        CustomRecipe customRecipe = findCustomRecipe(shapedRecipe);
        if (customRecipe == null) {
            return;
        }

        if (!(event.getView().getPlayer() instanceof Player player)) {
            return;
        }

        if (!hasEnoughIngredients(player, event.getInventory(), customRecipe)) {
            event.getInventory().setResult(null);
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        Recipe recipe = event.getRecipe();
        if (!(recipe instanceof ShapedRecipe shapedRecipe)) {
            return;
        }

        CustomRecipe customRecipe = findCustomRecipe(shapedRecipe);
        if (customRecipe == null) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        // Always cancel to handle amounts manually
        event.setCancelled(true);

        if (!hasEnoughIngredients(player, event.getInventory(), customRecipe)) {
            this.craftingService.getMultification().create()
                    .viewer(player)
                    .notice(messages -> messages.missingIngredients)
                    .send();
            return;
        }

        PlayerInventory inventory = player.getInventory();
        if (inventory.firstEmpty() == -1) {
            boolean hasSpace = false;
            for (ItemStack item : inventory.getContents()) {
                if (item != null && item.isSimilar(customRecipe.result())) {
                    if (item.getAmount() + customRecipe.result().getAmount() <= item.getMaxStackSize()) {
                        hasSpace = true;
                        break;
                    }
                }
            }
            if (!hasSpace) {
                this.craftingService.getMultification().create()
                        .viewer(player)
                        .notice(messages -> messages.noSpace)
                        .send();
                return;
            }
        }

        removeIngredients(player, event.getInventory(), customRecipe);
        inventory.addItem(customRecipe.result().clone());

        // Update cursor and inventory
        Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 1L);
    }

    private CustomRecipe findCustomRecipe(ShapedRecipe shapedRecipe) {
        NamespacedKey key = shapedRecipe.getKey();
        if (!key.getNamespace().equals(plugin.getName().toLowerCase(java.util.Locale.ROOT))) {
            return null;
        }

        if (!key.getKey().startsWith("custom_recipe_")) {
            return null;
        }

        String indexStr = key.getKey().replace("custom_recipe_", "");
        try {
            int index = Integer.parseInt(indexStr);
            CraftingConfiguration config = this.craftingService.getConfiguration();
            if (index >= 0 && index < config.recipes.size()) {
                return config.recipes.get(index);
            }
        } catch (NumberFormatException ignored) {
        }
        return null;
    }

    /**
     * Checks if the player has enough ingredients, counting both the crafting grid and player inventory.
     */
    private boolean hasEnoughIngredients(Player player, CraftingInventory craftingInventory, CustomRecipe recipe) {
        Map<ItemStack, Integer> requiredItems = buildRequiredItems(recipe);

        for (Map.Entry<ItemStack, Integer> entry : requiredItems.entrySet()) {
            ItemStack item = entry.getKey();
            int required = entry.getValue();
            int found = 0;

            // Count items in crafting grid (slots 1-9)
            for (int i = 1; i < craftingInventory.getSize(); i++) {
                ItemStack gridItem = craftingInventory.getItem(i);
                if (gridItem != null && gridItem.isSimilar(item)) {
                    found += gridItem.getAmount();
                }
            }

            // Count items in player inventory
            for (ItemStack invItem : player.getInventory().getContents()) {
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

    /**
     * Removes the correct amount of ingredients from the crafting grid first, then from the player inventory.
     */
    private void removeIngredients(Player player, CraftingInventory craftingInventory, CustomRecipe recipe) {
        Map<ItemStack, Integer> requiredItems = buildRequiredItems(recipe);

        for (Map.Entry<ItemStack, Integer> entry : requiredItems.entrySet()) {
            ItemStack item = entry.getKey();
            int toRemove = entry.getValue();

            // First, remove from crafting grid (slots 1-9)
            for (int i = 1; i < craftingInventory.getSize(); i++) {
                if (toRemove <= 0) break;
                ItemStack gridItem = craftingInventory.getItem(i);
                if (gridItem != null && gridItem.isSimilar(item)) {
                    if (gridItem.getAmount() > toRemove) {
                        gridItem.setAmount(gridItem.getAmount() - toRemove);
                        craftingInventory.setItem(i, gridItem);
                        toRemove = 0;
                    } else {
                        toRemove -= gridItem.getAmount();
                        craftingInventory.setItem(i, null);
                    }
                }
            }

            // Then, remove remaining from player inventory
            if (toRemove > 0) {
                PlayerInventory inventory = player.getInventory();
                ItemStack[] contents = inventory.getContents();
                for (int i = 0; i < contents.length; i++) {
                    if (toRemove <= 0) break;
                    ItemStack invItem = contents[i];
                    if (invItem != null && invItem.isSimilar(item)) {
                        if (invItem.getAmount() > toRemove) {
                            invItem.setAmount(invItem.getAmount() - toRemove);
                            inventory.setItem(i, invItem);
                            toRemove = 0;
                        } else {
                            toRemove -= invItem.getAmount();
                            inventory.setItem(i, null);
                        }
                    }
                }
            }
        }

        // Clear the result slot
        craftingInventory.setResult(null);
    }

    private Map<ItemStack, Integer> buildRequiredItems(CustomRecipe recipe) {
        Map<ItemStack, Integer> requiredItems = new HashMap<>();

        for (ItemStack ingredient : recipe.ingredients().values()) {
            if (ingredient == null || ingredient.getType() == Material.AIR) {
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
        return requiredItems;
    }
}
