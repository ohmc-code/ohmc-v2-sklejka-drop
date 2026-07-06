package dev.chudziudgi.ohmc.drop.paper.feature.drop.listener;

import dev.chudziudgi.ohmc.drop.paper.feature.drop.DropService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DropConnectionListener implements Listener {

    private final DropService service;

    public DropConnectionListener(DropService service) {
        this.service = service;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.service.loadPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.service.unloadPlayer(event.getPlayer().getUniqueId());
    }
}
