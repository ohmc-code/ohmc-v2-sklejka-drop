package dev.chudziudgi.ohmc.drop.paper.feature.drop.inventory.item;

import dev.chudziudgi.ohmc.drop.paper.feature.drop.DropService;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.config.DropInventoryConfiguration;
import dev.chudziudgi.ohmc.drop.paper.multification.LobbyMultification;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;

import static dev.chudziudgi.ohmc.drop.paper.DropPlugin.MINI_MESSAGE;

public class DropStoneToggleItem extends AbstractItem {

    private final DropInventoryConfiguration configuration;
    private final DropService service;
    private final LobbyMultification multification;

    public DropStoneToggleItem(DropInventoryConfiguration configuration, DropService service,
                               LobbyMultification multification) {
        this.configuration = configuration;
        this.service = service;
        this.multification = multification;
    }

    @Override
    public @NotNull ItemProvider getItemProvider(@NotNull Player viewer) {
        boolean enabled = this.service.isStoneDropEnabled(viewer.getUniqueId());
        DropInventoryConfiguration.CustomItem button = enabled
                ? this.configuration.stoneToggleOn
                : this.configuration.stoneToggleOff;

        ItemBuilder itemBuilder = new ItemBuilder(button.material)
                .setName(MINI_MESSAGE.deserialize(button.name));

        if (button.itemModel != null && !button.itemModel.isEmpty()) {
            itemBuilder.setCustomModelData(button.itemModel.toArray(new String[0]));
        }

        button.lore.stream()
                .map(MINI_MESSAGE::deserialize)
                .forEach(itemBuilder::addLoreLines);

        return itemBuilder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        boolean nowEnabled = this.service.toggleStoneDrop(player.getUniqueId());

        this.multification.create()
                .viewer(player)
                .notice(messages -> nowEnabled ? messages.dropEnabled : messages.dropDisabled)
                .placeholder("{ITEM}", this.configuration.stoneToggleDisplayName)
                .send();

        this.notifyWindows();
    }
}
