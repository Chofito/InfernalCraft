package org.darksun.infernalcraft.infernalcraft.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvents implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.broadcastMessage("Asi que" + event.getPlayer().getName() + "ha regresado... Preparate para seguir sufriendo!");
    }
}