package org.darksun.infernalcraft.infernalcraft.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.darksun.infernalcraft.infernalcraft.utils.GlobalHelpers;

public class PlayerEvents implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.broadcastMessage("Asi que " + event.getPlayer().getName() + " ha regresado... Preparate para seguir sufriendo! " + Bukkit.getWorld("world").getTime());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeathWithTotem(EntityResurrectEvent event) {
        if (GlobalHelpers.getChance(0.5)) {
            event.setCancelled(true);
        }
    }
}
