package io.chofito.proyectox.configuration;

import de.leonhard.storage.Json;

public class DefaultMobsConfiguration {
    public static void setupDefaultMobs(Json mobsConfig) {
        // Guerrero Zombie
        mobsConfig.setDefault("GuerreroZombie.entity", "ZOMBIE");
        mobsConfig.setDefault("GuerreroZombie.displayName", "Guerrero Zombie");
        mobsConfig.setDefault("GuerreroZombie.mainHand", "IRON_SWORD");
        mobsConfig.setDefault("GuerreroZombie.spawnChance", 0.75);
        mobsConfig.setDefault("GuerreroZombie.damageMultiplier", 1.25);
        mobsConfig.setDefault("GuerreroZombie.lifeMultiplier", 1.5);
        mobsConfig.setDefault("GuerreroZombie.chestplate", "LEATHER_CHESTPLATE");
        mobsConfig.setDefault("GuerreroZombie.leggings", "LEATHER_LEGGINGS");
        mobsConfig.setDefault("GuerreroZombie.helmet", "LEATHER_HELMET");
        mobsConfig.setDefault("GuerreroZombie.boots", "LEATHER_BOOTS");
        // Guerrero Oscuro
        mobsConfig.setDefault("GuerreroOscuro.entity", "WITHER_SKELETON");
        mobsConfig.setDefault("GuerreroOscuro.displayName", "Guerrero Oscuro");
        mobsConfig.setDefault("GuerreroOscuro.mainHand", "IRON_AXE");
        mobsConfig.setDefault("GuerreroOscuro.offHand", "TOTEM_OF_UNDYING");
        mobsConfig.setDefault("GuerreroOscuro.spawnChance", 0.1);
        mobsConfig.setDefault("GuerreroOscuro.damageMultiplier", 2);
        mobsConfig.setDefault("GuerreroOscuro.lifeMultiplier", 2.5);
        mobsConfig.setDefault("GuerreroOscuro.chestplate", "NETHERITE_CHESTPLATE");
        mobsConfig.setDefault("GuerreroOscuro.leggings", "NETHERITE_LEGGINGS");
    }
}
