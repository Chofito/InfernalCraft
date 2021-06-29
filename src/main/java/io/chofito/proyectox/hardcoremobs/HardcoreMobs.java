package io.chofito.proyectox.hardcoremobs;

import de.leonhard.storage.Json;
import me.lucko.helper.Events;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class HardcoreMobs {
    private Json hardCodeMobsConfig;

    public HardcoreMobs(Json hardCodeMobsConfig) {
        this.hardCodeMobsConfig = hardCodeMobsConfig;
    }

    public void setupHardcoreMobs() {
        this.setupSpawnEvent();
    }

    public void setupSpawnEvent() {
        Events.subscribe(CreatureSpawnEvent.class)
                .filter(event -> event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL)
                .handler(event -> {

                });
    }
}
