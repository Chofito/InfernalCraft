package io.chofito.proyectox.items;

import de.leonhard.storage.Json;
import io.chofito.proyectox.utils.ItemHelpers;
import me.lucko.helper.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemBuilder {
    public static ItemStack buildItemFromConfig(String key, Json configProvider) {
        if (key == null || configProvider == null) return null;

        Material itemMaterial = ItemHelpers.getMaterialFromString(configProvider.getOrSetDefault(key + ".item", "DIAMOND_SWORD"));
        String displayName = configProvider.getString( key + ".displayName");
        String lore = configProvider.getString( key + ".lore");
        boolean unbreakable = configProvider.getBoolean( key + ".displayName");
        int amount = configProvider.getOrDefault( key + ".amount", 1);
        List<String> enchantments = (List<String>) configProvider.getList(key + ".enchantments");

        ItemStackBuilder itemStackBuilder = ItemStackBuilder.of(itemMaterial);

        if (displayName != null) {
            itemStackBuilder.name(displayName);
        }

        if (lore != null) {
            itemStackBuilder.lore(lore);
        }

        for (String enchantment : enchantments) {
            String[] enchantmentProperties = enchantment.split(":");
            if (enchantmentProperties[0] != null) {
                Enchantment enchantmentType = ItemHelpers.getEnchantmentFromString(enchantmentProperties[0]);
                if (enchantmentType != null) {
                    itemStackBuilder.enchant(enchantmentType, enchantmentProperties.length > 1 ? Integer.parseInt(enchantmentProperties[1]) : 1);
                }
            }
        }

        itemStackBuilder.breakable(!unbreakable);
        itemStackBuilder.amount(amount);
        itemStackBuilder.showAttributes();

        return itemStackBuilder.build();
    }
}
