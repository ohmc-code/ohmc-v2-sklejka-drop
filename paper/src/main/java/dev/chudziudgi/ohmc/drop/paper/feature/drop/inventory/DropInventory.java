package dev.chudziudgi.ohmc.drop.paper.feature.drop.inventory;

import dev.chudziudgi.ohmc.drop.paper.feature.drop.DropService;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.config.DropInventoryConfiguration;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.inventory.item.DropCloseItem;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.inventory.item.DropNavigationPageItem;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.inventory.item.DropStoneToggleItem;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.inventory.item.DropToggleItem;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.inventory.item.DropWallpaperItem;
import dev.chudziudgi.ohmc.drop.paper.multification.LobbyMultification;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Markers;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.Structure;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

import java.util.List;
import java.util.stream.Collectors;

import static dev.chudziudgi.ohmc.drop.paper.DropPlugin.MINI_MESSAGE;

public class DropInventory {

    private final DropInventoryConfiguration configuration;
    private final DropService service;
    private final LobbyMultification multification;

    public DropInventory(DropInventoryConfiguration configuration, DropService service, LobbyMultification multification) {
        this.configuration = configuration;
        this.service = service;
        this.multification = multification;
    }

    public void open(Player player) {
        List<Material> stoneToggleItems = this.service.stoneToggleItems();
        List<Item> toggleItems = this.service.drops().stream()
                .filter(data -> data.dropItem != null)
                .filter(data -> !stoneToggleItems.contains(data.dropItem))
                .map(data -> new DropToggleItem(this.configuration, this.service, this.multification, data))
                .collect(Collectors.toList());

        Structure structure = new Structure(this.configuration.structure.toArray(new String[0]));
        structure.addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL);
        structure.addIngredient('<', DropNavigationPageItem.createNavigationItem(this.configuration, DropNavigationPageItem.NavigationType.PREVIOUS));
        structure.addIngredient('>', DropNavigationPageItem.createNavigationItem(this.configuration, DropNavigationPageItem.NavigationType.NEXT));
        structure.addIngredient('c', new DropCloseItem(this.configuration));
        structure.addIngredient('o', new DropStoneToggleItem(this.configuration, this.service, this.multification));
        structure.addIngredient('#', new DropWallpaperItem(this.configuration));

        PagedGui<@NotNull Item> gui = PagedGui.itemsBuilder()
                .setStructure(structure)
                .setContent(toggleItems)
                .build();

        Window.builder()
                .setViewer(player)
                .setUpperGui(gui)
                .setTitle(MINI_MESSAGE.deserialize(this.configuration.title))
                .build()
                .open();
    }
}
