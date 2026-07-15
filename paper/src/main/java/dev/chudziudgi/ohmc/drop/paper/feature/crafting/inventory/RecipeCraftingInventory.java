package dev.chudziudgi.ohmc.drop.paper.feature.crafting.inventory;

import dev.chudziudgi.ohmc.drop.paper.feature.crafting.CraftingService;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.CustomRecipe;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.configuration.CraftingInventoryConfiguration;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.inventory.item.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.Markers;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.Structure;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static dev.chudziudgi.ohmc.drop.paper.DropPlugin.MINI_MESSAGE;

public class RecipeCraftingInventory {

    private final CraftingService craftingService;
    private final CraftingInventoryConfiguration configuration;

    public RecipeCraftingInventory(
            CraftingService craftingService,
            CraftingInventoryConfiguration configuration
    ) {
        this.craftingService = craftingService;
        this.configuration = configuration;
    }

    public void open(Player player) {
        CraftingInventoryConfiguration config = this.craftingService.getInventoryConfiguration();

        List<Item> recipeItems = new ArrayList<>();
        for (CustomRecipe recipe : this.craftingService.getConfiguration().recipes) {
            recipeItems.add(new RecipeItem(recipe, this.configuration.recipeButtonConfig, this));
        }

        Structure structure = new Structure(config.structure.toArray(new String[0]))
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', new CraftingWallpaperItem(config.wallpaper))
                .addIngredient('c', new CraftingCloseItem(config.navigation.close))
                .addIngredient('<', CraftingNavigationPageItem.createNavigationItem(this.configuration, CraftingNavigationPageItem.NavigationType.PREVIOUS))
                .addIngredient('>', CraftingNavigationPageItem.createNavigationItem(this.configuration, CraftingNavigationPageItem.NavigationType.NEXT));


        PagedGui<@NotNull Item> gui = PagedGui.itemsBuilder()
                .setStructure(structure)
                .setContent(recipeItems)
                .build();

        Window window = Window.builder()
                .setViewer(player)
                .setUpperGui(gui)
                .setTitle(MINI_MESSAGE.deserialize(config.title))
                .build();

        window.open();
    }

    public void openRecipe(Player player, CustomRecipe recipe) {
        CraftingInventoryConfiguration config = this.craftingService.getInventoryConfiguration();
        CraftingInventoryConfiguration.CraftingMenuConfig menuConfig = config.craftingMenu;

        Structure structure = new Structure(menuConfig.structure.toArray(new String[0]))
                .addIngredient('#', new CraftingWallpaperItem(menuConfig.wallpaper))
                .addIngredient('b', new BackToListItem(this.configuration.navigation.back, this))
                .addIngredient('r', new CraftResultItem(recipe, this.craftingService, this.configuration.craftingMenu));

        final HashMap<Integer, ItemStack> ingredientMap = recipe.ingredients();
        final int[] ingredientIndex = {0};
        structure.addIngredient('i', () -> {
            int slot = ingredientIndex[0]++;
            ItemStack ingredient = ingredientMap.get(slot);
            return new IngredientDisplayItem(ingredient);
        });

        Gui gui = Gui.builder()
                .setStructure(structure)
                .build();

        String title = menuConfig.title.replace("{RECIPE_NAME}", recipe.name());
        Window window = Window.builder()
                .setViewer(player)
                .setUpperGui(gui)
                .setTitle(MINI_MESSAGE.deserialize(title))
                .build();

        window.open();
    }
}
