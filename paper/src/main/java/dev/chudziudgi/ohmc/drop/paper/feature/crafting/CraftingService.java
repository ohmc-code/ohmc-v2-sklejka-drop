package dev.chudziudgi.ohmc.drop.paper.feature.crafting;

import dev.chudziudgi.ohmc.drop.paper.feature.crafting.config.CraftingConfig;
import dev.chudziudgi.ohmc.drop.paper.reload.Reloadable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CraftingService implements Reloadable {

    private final CraftingConfig configuration;
    private final Plugin plugin;
    private final Logger logger;

    private final List<NamespacedKey> registeredKeys = new ArrayList<>();

    public CraftingService(CraftingConfig configuration, Plugin plugin, Logger logger) {
        this.configuration = configuration;
        this.plugin = plugin;
        this.logger = logger;
    }

    public void registerRecipes() {
        int index = 0;
        for (CraftingConfig.CraftingRecipe recipe : this.configuration.recipes) {
            NamespacedKey key = new NamespacedKey(this.plugin, "custom_recipe_" + index++);

            if (!recipe.enabled) {
                continue;
            }
            if (recipe.result == null || recipe.result.getType() == Material.AIR) {
                this.logger.warning("Pomijam recepturę '" + recipe.name() + "' - brak przedmiotu wynikowego (result).");
                continue;
            }
            if (recipe.shape == null || recipe.shape.isEmpty()) {
                this.logger.warning("Pomijam recepturę '" + recipe.name() + "' - brak kształtu (shape).");
                continue;
            }

            try {
                ShapedRecipe shapedRecipe = new ShapedRecipe(key, recipe.result.clone());
                shapedRecipe.shape(recipe.shape.toArray(new String[0]));

                Set<Character> usedSymbols = recipe.shape.stream()
                        .flatMap(row -> row.chars().mapToObj(c -> (char) c))
                        .filter(c -> c != ' ')
                        .collect(Collectors.toSet());

                boolean missingIngredient = false;
                for (Character symbol : usedSymbols) {
                    Material material = recipe.ingredients.get(String.valueOf(symbol));
                    if (material == null) {
                        this.logger.warning("Pomijam recepturę '" + recipe.name() + "' - brak składnika dla symbolu '" + symbol + "'.");
                        missingIngredient = true;
                        break;
                    }
                    shapedRecipe.setIngredient(symbol, material);
                }

                if (missingIngredient) {
                    continue;
                }

                Bukkit.removeRecipe(key);
                Bukkit.addRecipe(shapedRecipe);
                this.registeredKeys.add(key);
            }
            catch (Exception exception) {
                this.logger.warning("Nie można zarejestrować receptury '" + recipe.name() + "': " + exception.getMessage());
            }
        }

        Bukkit.getServer().updateRecipes();
    }

    public void unregisterRecipes() {
        for (NamespacedKey key : this.registeredKeys) {
            Bukkit.removeRecipe(key);
        }
        this.registeredKeys.clear();
    }

    @Override
    public void reload() {
        this.unregisterRecipes();
        this.registerRecipes();
    }
}
