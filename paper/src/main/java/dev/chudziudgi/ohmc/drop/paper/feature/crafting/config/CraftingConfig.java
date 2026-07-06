package dev.chudziudgi.ohmc.drop.paper.feature.crafting.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CraftingConfig extends OkaeriConfig {

    @Comment({
            "# Własne receptury które automatycznie wchodzą do zwykłego craftingu w grze.",
            "# enabled     - czy receptura jest aktywna",
            "# shape       - kształt 3x3 (użyj liter jako kluczy, spacja = puste pole)",
            "# ingredients - przypisanie: litera z 'shape' -> materiał",
            "# result      - przedmiot który powstanie (pełny ItemStack, można edytować ilość/nazwę/enchanty)"
    })
    public List<CraftingRecipe> recipes = new ArrayList<>(List.of(
            new CraftingRecipe(
                    "saddle",
                    List.of(
                            "LLL",
                            "I I",
                            "   "
                    ),
                    new LinkedHashMap<>(Map.of(
                            "L", Material.LEATHER,
                            "I", Material.IRON_INGOT
                    )),
                    new ItemStack(Material.SADDLE, 1)
            )
    ));

    public static class CraftingRecipe extends OkaeriConfig {
        @Comment("# Unikalna nazwa (używana jako klucz receptury w grze)")
        public String name = "";
        public boolean enabled = true;
        public List<String> shape = new ArrayList<>();
        public Map<String, Material> ingredients = new LinkedHashMap<>();
        public ItemStack result;

        public CraftingRecipe() {
        }

        public CraftingRecipe(String name, List<String> shape, Map<String, Material> ingredients, ItemStack result) {
            this.name = name;
            this.shape = new ArrayList<>(shape);
            this.ingredients = new LinkedHashMap<>(ingredients);
            this.result = result;
        }

        public String name() {
            return this.name;
        }
    }
}
