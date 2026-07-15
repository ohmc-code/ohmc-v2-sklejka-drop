package dev.chudziudgi.ohmc.drop.paper.feature.crafting.inventory.item;

import dev.chudziudgi.ohmc.drop.paper.feature.crafting.configuration.CraftingInventoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;

import static dev.chudziudgi.ohmc.drop.paper.DropPlugin.MINI_MESSAGE;

public class CraftingWallpaperItem extends AbstractItem {

    private final CraftingInventoryConfiguration.WallpaperConfig config;

    public CraftingWallpaperItem(
            CraftingInventoryConfiguration.WallpaperConfig config
    ) {
        this.config = config;
    }

    @Override
    public @NotNull ItemProvider getItemProvider(@NotNull Player viewer) {
        ItemStack item = new ItemStack(this.config.material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(MINI_MESSAGE.deserialize(this.config.name));
            item.setItemMeta(meta);
        }

        return new ItemBuilder(item);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
    }
}
