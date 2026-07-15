package dev.chudziudgi.ohmc.drop.paper.feature.crafting.inventory.item;

import dev.chudziudgi.ohmc.drop.paper.feature.crafting.CustomRecipe;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.configuration.CraftingInventoryConfiguration;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.inventory.RecipeCraftingInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;

import static dev.chudziudgi.ohmc.drop.paper.DropPlugin.MINI_MESSAGE;

public class RecipeItem extends AbstractItem {

    private final CustomRecipe recipe;
    private final CraftingInventoryConfiguration.RecipeButtonConfig recipeButtonConfig;
    private final RecipeCraftingInventory recipeCraftingInventory;

    public RecipeItem(
            CustomRecipe recipe,
            CraftingInventoryConfiguration.RecipeButtonConfig recipeButtonConfig,
            RecipeCraftingInventory recipeCraftingInventory
    ) {
        this.recipe = recipe;
        this.recipeButtonConfig = recipeButtonConfig;
        this.recipeCraftingInventory = recipeCraftingInventory;
    }

    @Override
    public @NotNull ItemProvider getItemProvider(@NotNull Player viewer) {
        ItemBuilder itemBuilder = new ItemBuilder(this.recipe.getIcon())
                .setName(MINI_MESSAGE.deserialize(this.recipeButtonConfig.name.replace("{name}", this.recipe.name())));

        this.recipeButtonConfig.lore.stream()
                .map(line -> MINI_MESSAGE.deserialize(line.replace("{name}", this.recipe.name())))
                .forEach(itemBuilder::addLoreLines);

        return itemBuilder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        player.closeInventory();
        this.recipeCraftingInventory.openRecipe(player, this.recipe);
    }
}
