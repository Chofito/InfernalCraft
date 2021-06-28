package io.chofito.proyectox.items;

import de.leonhard.storage.Json;
import io.chofito.proyectox.utils.ItemHelpers;
import me.lucko.helper.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.List;

public class ItemBuilder {
    public static ItemStack buildItemFromConfig(String key, Json configProvider) {
        if (key == null || configProvider == null) return null;

        Material itemMaterial = ItemHelpers.getMaterialFromString(configProvider.getOrSetDefault(key + ".item", "DIAMOND_SWORD"));
        String displayName = configProvider.getString( key + ".displayName");
        String lore = configProvider.getString( key + ".lore");
        boolean unbreakable = configProvider.getBoolean( key + ".displayName");
        int amount = configProvider.getInt( key + ".amount");
        List<String> enchantments = (List<String>) configProvider.getList(key + ".enchantments");
        String onHandParticles = configProvider.getString(key + ".particles");

        ItemStackBuilder itemStackBuilder = ItemStackBuilder.of(itemMaterial);

        if (displayName != null) {
            itemStackBuilder.name(displayName);
        }

        if (lore != null) {
            itemStackBuilder.lore(lore);
        }

        itemStackBuilder.breakable(!unbreakable);
        itemStackBuilder.amount(amount);

        for (String enchantment : enchantments) {
            enchantment.split(":");
        }
    }
}
