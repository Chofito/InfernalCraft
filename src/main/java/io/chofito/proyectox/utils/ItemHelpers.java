package io.chofito.proyectox.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class ItemHelpers {
    public static Material getMaterialFromString(String stringMaterial) {
        return Material.getMaterial(stringMaterial);
    }

    public static Enchantment getEnchantmentFromString(String enchantmentString) {
        return Enchantment.getByKey(NamespacedKey.minecraft(enchantmentString));
    }
}
