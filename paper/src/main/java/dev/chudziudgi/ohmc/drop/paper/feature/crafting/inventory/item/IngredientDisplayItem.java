package dev.chudziudgi.ohmc.drop.paper.feature.crafting.inventory.item;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;

public class IngredientDisplayItem extends AbstractItem {

    private final ItemStack ingredient;

    public IngredientDisplayItem(ItemStack ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public @NotNull ItemProvider getItemProvider(@NotNull Player viewer) {
        if (this.ingredient == null) {
            return new ItemBuilder(new ItemStack(org.bukkit.Material.AIR));
        }
        return new ItemBuilder(this.ingredient.clone());
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {

    }
}
