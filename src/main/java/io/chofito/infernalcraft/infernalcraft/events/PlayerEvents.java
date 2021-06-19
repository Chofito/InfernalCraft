package io.chofito.infernalcraft.infernalcraft.events;

import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.item.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import io.chofito.infernalcraft.infernalcraft.utils.GlobalHelpers;

public class PlayerEvents {
    public static void setupOnPlayerWithTotemDeathEvents() {
        Events.subscribe(EntityResurrectEvent.class, EventPriority.MONITOR)
                .handler(e -> {
                    if (GlobalHelpers.getChance(0.5)) {
                        e.setCancelled(true);
                    }
                });
    }

    public static void setupOnPlayerJoinEvents() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(e -> {
                    ItemStack ruinedSword = ItemStackBuilder.of(Material.DIAMOND_SWORD)
                            .amount(1)
                            .name("Ruined King Sword")
                            .breakable(false)
                            .enchant(Enchantment.DAMAGE_ALL, 10)
                            .showAttributes()
                            .lore("An ancient sword from a Ruined King...")
                            .build();

                    Bukkit.broadcastMessage(
                            "Asi que " + e.getPlayer().getName() + " ha regresado... Preparate para seguir sufriendo! "
                    );

                    if (!e.getPlayer().getInventory().contains(ruinedSword)) {
                        e.getPlayer().getInventory().addItem(ruinedSword);
                    }
                });
    }

    public static void setupOnPlayerDeathEvent() {
        Events.subscribe(PlayerDeathEvent.class)
                .handler(event -> {
                    Player player = event.getEntity().getPlayer();
                    if (player != null) {
                        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, Float.MAX_VALUE, -0.1F);
                        Schedulers.sync().runLater(() -> player.playSound(player.getLocation(), Sound.ENTITY_SKELETON_HORSE_DEATH, Float.MAX_VALUE, 1F), 50);
                    }
                });
    }
}
