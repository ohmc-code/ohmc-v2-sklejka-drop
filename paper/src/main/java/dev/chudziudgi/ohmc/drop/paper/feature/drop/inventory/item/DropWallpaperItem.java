package dev.chudziudgi.ohmc.drop.paper.feature.drop.inventory.item;

import dev.chudziudgi.ohmc.drop.paper.feature.drop.config.DropInventoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;

import static dev.chudziudgi.ohmc.drop.paper.DropPlugin.MINI_MESSAGE;

public class DropWallpaperItem extends AbstractItem {

    private final DropInventoryConfiguration configuration;

    public DropWallpaperItem(DropInventoryConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public @NotNull ItemProvider getItemProvider(@NotNull Player viewer) {
        ItemBuilder itemBuilder = new ItemBuilder(this.configuration.wallpaper.material)
                .setName(MINI_MESSAGE.deserialize(this.configuration.wallpaper.name));

        if (this.configuration.wallpaper.itemModel != null && !this.configuration.wallpaper.itemModel.isEmpty()) {
            itemBuilder.setCustomModelData(this.configuration.wallpaper.itemModel.toArray(new String[0]));
        }

        this.configuration.wallpaper.lore.stream()
                .map(MINI_MESSAGE::deserialize)
                .forEach(itemBuilder::addLoreLines);

        return itemBuilder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
    }
}
