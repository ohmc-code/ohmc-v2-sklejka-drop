package dev.chudziudgi.ohmc.drop.paper.feature.crafting;

import dev.chudziudgi.ohmc.drop.paper.utill.ItemStackJsonUtil;
import eu.okaeri.configs.OkaeriConfig;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CustomRecipe extends OkaeriConfig {

    private String name;
    private HashMap<Integer, String> ingredients;
    private String result;

    public CustomRecipe() {
        this.name = "Unnamed";
        this.result = "";
        this.ingredients = new HashMap<>();
    }

    public CustomRecipe(
            String name,
            HashMap<Integer, String> ingredients,
            String result
    ) {
        this.name = name;
        this.ingredients = ingredients;
        this.result = result;
    }

    public CustomRecipe(String result) {
        this.name = "Unnamed";
        this.result = result;
        this.ingredients = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            this.ingredients.put(i, ItemStackJsonUtil.serializeItem(new ItemStack(Material.DIRT, 1)));
        }
    }

    public String name() {
        return this.name;
    }

    public HashMap<Integer, ItemStack> ingredients() {
        HashMap<Integer, ItemStack> deserialized = new HashMap<>();
        for (HashMap.Entry<Integer, String> entry : this.ingredients.entrySet()) {
            deserialized.put(entry.getKey(), ItemStackJsonUtil.deserializeItem(entry.getValue()));
        }
        return deserialized;
    }

    public HashMap<Integer, String> ingredientsRaw() {
        return this.ingredients;
    }

    public ItemStack result() {
        return ItemStackJsonUtil.deserializeItem(this.result);
    }

    public String resultJson() {
        return this.result;
    }

    public ItemStack getIcon() {
        return result();
    }
}
