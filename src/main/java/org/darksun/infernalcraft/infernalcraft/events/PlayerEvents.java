package org.darksun.infernalcraft.infernalcraft.events;

import me.lucko.helper.Events;
import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.darksun.infernalcraft.infernalcraft.utils.GlobalHelpers;

public class PlayerEvents {
    public static void setupOnPlayerWithTotemDeathEvents() {
        Events.subscribe(EntityResurrectEvent.class, EventPriority.MONITOR)
                .handler(e -> {
                    if (GlobalHelpers.getChance(0.5)) {
                        e.setCancelled(true);
                    }
                });
    }

    public static void setupOnPlayerJoinEvents() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(e -> Bukkit.broadcastMessage(
                        "Asi que " + e.getPlayer().getName() + " ha regresado... Preparate para seguir sufriendo! "
                ));
    }
}
