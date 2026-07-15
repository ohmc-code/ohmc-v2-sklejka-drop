package dev.chudziudgi.ohmc.drop.paper.feature.crafting.inventory.item;

import dev.chudziudgi.ohmc.drop.paper.feature.crafting.configuration.CraftingInventoryConfiguration;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.inventory.RecipeCraftingInventory;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;

import java.util.List;
import java.util.stream.Collectors;

import static dev.chudziudgi.ohmc.drop.paper.DropPlugin.MINI_MESSAGE;

public class BackToListItem extends AbstractItem {

    private final CraftingInventoryConfiguration.CustomItem config;
    private final RecipeCraftingInventory recipeListInventory;

    public BackToListItem(
            CraftingInventoryConfiguration.CustomItem customItem,
            RecipeCraftingInventory recipeListInventory
    ) {
        this.config = customItem;
        this.recipeListInventory = recipeListInventory;
    }


    @Override
    public @NotNull ItemProvider getItemProvider(@NotNull Player viewer) {
        ItemStack item = new ItemStack(this.config.material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(MINI_MESSAGE.deserialize(this.config.name));

            List<Component> lore = this.config.lore.stream()
                    .map(MINI_MESSAGE::deserialize)
                    .collect(Collectors.toList());
            meta.lore(lore);

            item.setItemMeta(meta);
        }

        return new ItemBuilder(item);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, Player player, @NotNull Click click) {
        player.closeInventory();
        this.recipeListInventory.open(player);
    }
}
