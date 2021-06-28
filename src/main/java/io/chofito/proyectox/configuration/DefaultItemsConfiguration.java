package io.chofito.proyectox.configuration;

import de.leonhard.storage.Json;

import java.util.Arrays;

public class DefaultItemsConfiguration {
    public static void setupDefaultItems(Json itemsConfig) {
        // Espada Rey Arruinado
        itemsConfig.setDefault("EspadaReyArruinado.item", "DIAMOND_SWORD");
        itemsConfig.setDefault("GuerreroZombie.displayName", "Espada del Rey Arruinado");
        itemsConfig.setDefault("GuerreroZombie.lore", "Una espada antigua de un rey que lo perdio todo...");
        itemsConfig.setDefault("GuerreroZombie.enchantments", Arrays.asList("DAMAGE_ALL:10"));
        itemsConfig.setDefault("GuerreroZombie.unbreakable", true);
        // Espada Demoniaca
        itemsConfig.setDefault("EspadaReyArruinado.item", "NETHERITE_SWORD");
        itemsConfig.setDefault("GuerreroZombie.displayName", "Espada Demoniaca");
        itemsConfig.setDefault("GuerreroZombie.lore", "Forjada en el corazon del reyno de los demonios");
        itemsConfig.setDefault("GuerreroZombie.enchantments", Arrays.asList("DAMAGE_ALL:15"));
        itemsConfig.setDefault("GuerreroZombie.unbreakable", true);
    }
}
