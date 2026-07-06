package dev.chudziudgi.ohmc.drop.paper.feature.drop.inventory.item;

import dev.chudziudgi.ohmc.drop.paper.feature.drop.DropService;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.config.DropData;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.config.DropInventoryConfiguration;
import dev.chudziudgi.ohmc.drop.paper.multification.LobbyMultification;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;

import java.text.DecimalFormat;

import static dev.chudziudgi.ohmc.drop.paper.DropPlugin.MINI_MESSAGE;

public class DropToggleItem extends AbstractItem {

    private static final DecimalFormat CHANCE_FORMAT = new DecimalFormat("0.##");

    private final DropInventoryConfiguration configuration;
    private final DropService service;
    private final LobbyMultification multification;
    private final DropData data;

    public DropToggleItem(DropInventoryConfiguration configuration, DropService service,
                          LobbyMultification multification, DropData data) {
        this.configuration = configuration;
        this.service = service;
        this.multification = multification;
        this.data = data;
    }

    @Override
    public @NotNull ItemProvider getItemProvider(@NotNull Player viewer) {
        boolean enabled = this.service.isEnabled(viewer.getUniqueId(), this.data.dropItem);
        DropInventoryConfiguration.ToggleItem toggle = enabled
                ? this.configuration.dropEnabled
                : this.configuration.dropDisabled;

        ItemBuilder itemBuilder = new ItemBuilder(this.data.dropItem)
                .setName(MINI_MESSAGE.deserialize(this.replace(toggle.name)));

        if (toggle.itemModel != null && !toggle.itemModel.isEmpty()) {
            itemBuilder.setCustomModelData(toggle.itemModel.toArray(new String[0]));
        }

        toggle.lore.stream()
                .map(line -> MINI_MESSAGE.deserialize(this.replace(line)))
                .forEach(itemBuilder::addLoreLines);

        return itemBuilder;
    }

    private String replace(String input) {
        return input
                .replace("{ITEM}", this.data.displayName())
                .replace("{CHANCE}", CHANCE_FORMAT.format(this.data.chance))
                .replace("{MIN}", String.valueOf(Math.min(this.data.minAmount, this.data.maxAmount)))
                .replace("{MAX}", String.valueOf(Math.max(this.data.minAmount, this.data.maxAmount)));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        boolean nowEnabled = this.service.toggle(player.getUniqueId(), this.data.dropItem);

        this.multification.create()
                .viewer(player)
                .notice(messages -> nowEnabled ? messages.dropEnabled : messages.dropDisabled)
                .placeholder("{ITEM}", this.data.displayName())
                .send();

        this.notifyWindows();
    }
}
