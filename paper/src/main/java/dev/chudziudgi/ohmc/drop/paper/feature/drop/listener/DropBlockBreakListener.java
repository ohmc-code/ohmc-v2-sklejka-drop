package dev.chudziudgi.ohmc.drop.paper.feature.drop.listener;

import dev.chudziudgi.ohmc.drop.paper.feature.drop.DropService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class DropBlockBreakListener implements Listener {

    private final DropService service;

    public DropBlockBreakListener(DropService service) {
        this.service = service;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material block = event.getBlock().getType();

        if (this.service.disableAllOreDrops() && this.service.isOre(block)) {
            event.setDropItems(false);
            event.setExpToDrop(0);
            return;
        }

        if (!this.service.isSourceBlock(block)) {
            return;
        }

        if (this.service.removeNaturalDrops()) {
            event.setDropItems(false);
            event.setExpToDrop(0);
        }

        this.service.handleBreak(player, block);
    }
}
