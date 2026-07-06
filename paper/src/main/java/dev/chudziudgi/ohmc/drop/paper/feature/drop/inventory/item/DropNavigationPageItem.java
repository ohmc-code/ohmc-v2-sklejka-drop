package dev.chudziudgi.ohmc.drop.paper.feature.drop.inventory.item;

import dev.chudziudgi.ohmc.drop.paper.feature.drop.config.DropInventoryConfiguration;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.BoundItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;

import static dev.chudziudgi.ohmc.drop.paper.DropPlugin.MINI_MESSAGE;

public class DropNavigationPageItem {

    public enum NavigationType {
        PREVIOUS,
        NEXT
    }

    private DropNavigationPageItem() {
    }

    public static BoundItem createNavigationItem(
            @NotNull DropInventoryConfiguration configuration,
            @NotNull NavigationType navigationType
    ) {
        boolean isPrevious = navigationType == NavigationType.PREVIOUS;
        DropInventoryConfiguration.CustomItem customItem = isPrevious
                ? configuration.previousPage
                : configuration.nextPage;

        return BoundItem.pagedBuilder()
                .setItemProvider((player, gui) -> {
                    if (isPrevious ? !hasPreviousPage(gui) : !hasNextPage(gui)) {
                        return createItemProvider(configuration.wallpaper, gui.getPage() + 1, gui.getPageCount());
                    }
                    return createItemProvider(customItem, gui.getPage() + 1, gui.getPageCount());
                })
                .addClickHandler((item, gui, click) -> {
                    if (isPrevious) {
                        if (hasPreviousPage(gui)) {
                            gui.setPage(gui.getPage() - 1);
                        }
                    } else {
                        if (hasNextPage(gui)) {
                            gui.setPage(gui.getPage() + 1);
                        }
                    }
                })
                .build();
    }

    private static boolean hasPreviousPage(PagedGui<?> gui) {
        return gui.getPage() > 0;
    }

    private static boolean hasNextPage(PagedGui<?> gui) {
        return gui.getPage() < gui.getPageCount() - 1;
    }

    private static ItemProvider createItemProvider(
            @NotNull DropInventoryConfiguration.CustomItem customItem,
            int current,
            int max
    ) {
        ItemBuilder itemBuilder = new ItemBuilder(customItem.material)
                .setName(MINI_MESSAGE.deserialize(replacePage(customItem.name, current, max)));

        if (customItem.itemModel != null && !customItem.itemModel.isEmpty()) {
            itemBuilder.setCustomModelData(customItem.itemModel.toArray(new String[0]));
        }

        customItem.lore.stream()
                .map(line -> MINI_MESSAGE.deserialize(replacePage(line, current, max)))
                .forEach(itemBuilder::addLoreLines);

        return itemBuilder;
    }

    private static String replacePage(String input, int current, int max) {
        return input
                .replace("{CURRENT}", String.valueOf(current))
                .replace("{MAX}", String.valueOf(max));
    }
}
