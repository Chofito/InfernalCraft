package org.darksun.infernalcraft.infernalcraft;

import org.bukkit.configuration.file.FileConfiguration;

public class Configurations {
    public static void SetupDefaultSettings(FileConfiguration config) {
        config.addDefault("enableBloodMoon", true);
        config.addDefault("enableChargedCreepers", true);
        config.addDefault("chargedCreeperSpawnChance", 0.01);
        config.addDefault("enableInvisibleChargedCreeper", true);
        config.addDefault("invisibleChargedCreepersChance", 1);
        config.options().copyDefaults(true);
    }
}
