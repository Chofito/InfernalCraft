package io.chofito.proyectox.configuration;

import de.leonhard.storage.Json;

import java.util.Arrays;

public class DefaultItemsConfiguration {
    public static void setupDefaultItems(Json itemsConfig) {
        // Espada Rey Arruinado
        itemsConfig.setDefault("EspadaReyArruinado.item", "DIAMOND_SWORD");
        itemsConfig.setDefault("EspadaReyArruinado.displayName", "Espada del Rey Arruinado");
        itemsConfig.setDefault("EspadaReyArruinado.lore", "Una espada antigua de un rey que lo perdio todo...");
        itemsConfig.setDefault("EspadaReyArruinado.enchantments", Arrays.asList("DAMAGE_ALL:10"));
        itemsConfig.setDefault("EspadaReyArruinado.unbreakable", true);
        // Espada Demoniaca
        itemsConfig.setDefault("RobaAlmas.item", "NETHERITE_SWORD");
        itemsConfig.setDefault("RobaAlmas.displayName", "Roba Almas");
        itemsConfig.setDefault("RobaAlmas.lore", "Forjada por un antiguo demonio...");
        itemsConfig.setDefault("RobaAlmas.enchantments", Arrays.asList("DAMAGE_ALL:15"));
        itemsConfig.setDefault("RobaAlmas.unbreakable", true);
    }
}
