package dev.chudziudgi.ohmc.drop.paper.feature.crafting.inventory.item;

import dev.chudziudgi.ohmc.drop.paper.feature.crafting.CustomRecipe;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.CraftingService;
import dev.chudziudgi.ohmc.drop.paper.feature.crafting.configuration.CraftingInventoryConfiguration;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static dev.chudziudgi.ohmc.drop.paper.DropPlugin.MINI_MESSAGE;

public class CraftResultItem extends AbstractItem {

    private final CustomRecipe recipe;
    private final CraftingService craftingService;
    private final CraftingInventoryConfiguration.CraftingMenuConfig config;

    public CraftResultItem(
            CustomRecipe recipe,
            CraftingService craftingService,
            CraftingInventoryConfiguration.CraftingMenuConfig config
    ) {
        this.recipe = recipe;
        this.craftingService = craftingService;
        this.config = config;
    }

    @Override
    public @NotNull ItemProvider getItemProvider(@NotNull Player viewer) {
        ItemStack result = this.recipe.result().clone();
        ItemMeta meta = result.getItemMeta();

        if (meta != null) {
            List<Component> lore = new ArrayList<>();
            if (meta.hasLore()) {
                lore.addAll(Objects.requireNonNull(meta.lore()));
            }

            lore.addAll(this.config.resultSlot.stream()
                    .map(MINI_MESSAGE::deserialize)
                    .toList());

            meta.lore(lore);
            result.setItemMeta(meta);
        }

        return new ItemBuilder(result);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        if (this.craftingService.craft(player, this.recipe)) {
            player.closeInventory();
        }
    }
}
