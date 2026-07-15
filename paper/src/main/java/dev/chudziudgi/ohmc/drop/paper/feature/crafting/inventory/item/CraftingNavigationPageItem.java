package dev.chudziudgi.ohmc.drop.paper.feature.crafting.inventory.item;

import dev.chudziudgi.ohmc.drop.paper.feature.crafting.configuration.CraftingInventoryConfiguration;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.BoundItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;

import static dev.chudziudgi.ohmc.drop.paper.DropPlugin.MINI_MESSAGE;

public class CraftingNavigationPageItem {

    public static BoundItem createNavigationItem(
            @NotNull CraftingInventoryConfiguration configuration,
            @NotNull CraftingNavigationPageItem.NavigationType navigationType
    ) {
        CraftingInventoryConfiguration.CustomItem customItem;
        boolean isPrevious = navigationType == CraftingNavigationPageItem.NavigationType.PREVIOUS;

        if (isPrevious) {
            customItem = configuration.navigation.previousPage;
        } else {
            customItem = configuration.navigation.nextPage;
        }

        CraftingInventoryConfiguration.CustomItem disabledItem = isPrevious
                ? configuration.navigation.previousPageDisabled
                : configuration.navigation.nextPageDisabled;

        var builder = BoundItem.pagedBuilder()
                .setItemProvider((player, gui) -> {
                    boolean has;
                    if (isPrevious) {
                        has = gui.getPage() > 0;
                    } else {
                        has = gui.getPage() < Math.max(0, gui.getPageCount() - 1);
                    }

                    if (has) return createItemProvider(customItem);
                    if (!configuration.navigation.hideWhenImpossible) return createItemProvider(disabledItem);
                    return ItemProvider.EMPTY;
                });

        return builder
                .addClickHandler((item, gui, click) -> {
                    if (isPrevious) {
                        gui.setPage(gui.getPage() - 1);
                    } else {
                        gui.setPage(gui.getPage() + 1);
                    }
                })
                .build();
    }

    private static ItemProvider createItemProvider(@NotNull CraftingInventoryConfiguration.CustomItem customItem) {
        ItemBuilder itemBuilder = new ItemBuilder(customItem.material)
                .setName(MINI_MESSAGE.deserialize(customItem.name));

        customItem.lore.stream()
                .map(MINI_MESSAGE::deserialize)
                .forEach(itemBuilder::addLoreLines);

        return itemBuilder;
    }

    public enum NavigationType {
        PREVIOUS,
        NEXT
    }
}
