package io.chofito.proyectox.events;

import io.chofito.proyectox.utils.GlobalHelpers;
import me.lucko.helper.Events;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Goat;
import org.bukkit.event.entity.EntitySpawnEvent;

public class NeutralMobsEvents {
    public NeutralMobsEvents() {
    }

    public void setupEvents() {
        summonScreamingGoats();
    }

    public void summonScreamingGoats() {
        Events.subscribe(EntitySpawnEvent.class)
                .filter(event -> event.getEntity().getType() == EntityType.GOAT)
                .handler(event -> {
                    ((Goat)event.getEntity()).setScreaming(GlobalHelpers.getChance(0.2));
                });
    }
}
