package dev.chudziudgi.ohmc.drop.paper.feature.crafting.inventory;

import dev.chudziudgi.ohmc.drop.paper.feature.crafting.CraftingService;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.CustomRecipe;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.configuration.CraftingInventoryConfiguration;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.inventory.item.CraftingWallpaperItem;
import dev.chudziudgi.ohmc.drop.paper.utill.ItemStackJsonUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.Structure;
import xyz.xenondevs.invui.inventory.VirtualInventory;
import xyz.xenondevs.invui.window.Window;

import java.util.HashMap;

import static dev.chudziudgi.ohmc.drop.paper.DropPlugin.MINI_MESSAGE;

public class CraftingCreatorInventory {

    private final CraftingService craftingService;
    private final CraftingInventoryConfiguration configuration;

    public CraftingCreatorInventory(
            CraftingService craftingService,
            CraftingInventoryConfiguration configuration
    ) {
        this.craftingService = craftingService;
        this.configuration = configuration;
    }

    public void open(Player player, String recipeName) {
        VirtualInventory virtualInventory = new VirtualInventory(9);

        Structure structure = new Structure(
                "# # # # # # # # #",
                "# # # s s s # # #",
                "# # # s s s # # #",
                "# # # s s s # # #",
                "# # # # # # # # #"
        )
                .addIngredient('#', new CraftingWallpaperItem(this.configuration.wallpaper))
                .addIngredient('s', virtualInventory);

        Gui gui = Gui.builder()
                .setStructure(structure)
                .build();

        Window window = Window.builder()
                .setViewer(player)
                .setUpperGui(gui)
                .setTitle(MINI_MESSAGE.deserialize("<gradient:#ff6b6b:#ffa500>Kreator Craftingu: " + recipeName + "</gradient>"))
                .build();

        window.addCloseHandler((InventoryCloseEvent.Reason reason) -> handleClose(player, virtualInventory, recipeName));

        window.open();

        this.craftingService.getMultification().create()
                .viewer(player)
                .notice(messages -> messages.creatorOpened)
                .send();
    }

    private void handleClose(Player player, VirtualInventory virtualInventory, String recipeName) {
        ItemStack resultItem = player.getInventory().getItemInMainHand();

        if (resultItem.getType() == Material.AIR) {
            this.craftingService.getMultification().create()
                    .viewer(player)
                    .notice(messages -> messages.holdItemForResult)
                    .send();
            return;
        }

        HashMap<Integer, String> ingredients = new HashMap<>();
        boolean hasAnyIngredient = false;

        for (int i = 0; i < 9; i++) {
            ItemStack item = virtualInventory.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                ingredients.put(i, ItemStackJsonUtil.serializeItem(item.clone()));
                hasAnyIngredient = true;
            }
        }

        if (!hasAnyIngredient) {
            this.craftingService.getMultification().create()
                    .viewer(player)
                    .notice(messages -> messages.emptyGrid)
                    .send();
            return;
        }

        String resultJson = ItemStackJsonUtil.serializeItem(resultItem.clone());
        CustomRecipe recipe = new CustomRecipe(recipeName, ingredients, resultJson);

        this.craftingService.addRecipe(recipe);

        this.craftingService.getMultification().create()
                .viewer(player)
                .notice(messages -> messages.recipeCreated)
                .placeholder("{RECIPE_NAME}", recipeName)
                .send();
    }
}
