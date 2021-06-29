package io.chofito.proyectox.configuration;

import de.leonhard.storage.Json;

public class GlobalConfiguration {
    public static void setupDefaultConfig(Json config) {
        config.setDefault("enableBloodMoonModule", true);
        config.setDefault("enableHardCoreMobs", true);
        config.setDefault("enableDeathTrainModule", false);
        config.setDefault("hostileMobsHealthMutiplier", 1.5);
        config.setDefault("hostileMobsXpMultiplier", 1.25);
        config.setDefault("screamingGoatSpawnChance", 0.2f);
        config.setDefault("chargedCreeperSpawnChance", 0.2f);
        config.setDefault("disableSleep", false);
        config.setDefault("thunderOnSleepAttempt", false);
        config.setDefault("explosionOnSleepAttempt", false);
        config.setDefault("hostileMobsNightMultiplier", 1.25);
        config.setDefault("spawnIllusionerOnRaid", true);
        config.setDefault("spawnDungeonsMobsOnRaid", true);
        config.setDefault("witherBossReinforcements", true);
    }
}
