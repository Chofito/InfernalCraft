package io.chofito.proyectox.events;

import de.leonhard.storage.Json;
import io.chofito.proyectox.items.ItemBuilder;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import io.chofito.proyectox.utils.GlobalHelpers;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class PlayerEvents {
    private Json itemConfig;

    public PlayerEvents(Json itemConfig) {
        this.itemConfig = itemConfig;
    }

    public void setupEvents() {
        this.setupOnPlayerDeathEvent();
        this.setupOnPlayerJoinEvents();
        this.setupOnPlayerWithTotemDeathEvents();
    }

    public void setupOnPlayerWithTotemDeathEvents() {
        Events.subscribe(EntityResurrectEvent.class, EventPriority.MONITOR)
                .handler(event -> {
                    if (GlobalHelpers.getChance(0.5) && event.getEntity() instanceof Player) {
                        event.setCancelled(true);
                    }
                });
    }

    public void setupOnPlayerJoinEvents() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(event -> {
                    Bukkit.broadcastMessage(
                            "Asi que " + event.getPlayer().getName() + " ha regresado... Preparate para seguir sufriendo! "
                    );

                    Schedulers.sync().runRepeating(task -> {
                        if (!event.getPlayer().isOnline()) {
                            task.close();
                        }

                        new ParticleBuilder(ParticleEffect.SOUL, event.getPlayer().getLocation().add(0, 1, 0))
                                .setOffsetY(1f)
                                .setAmount(10)
                                .setSpeed(0.1f)
                                .display();
                    }, 0, 10);

                    ItemStack souldSword = ItemBuilder.buildItemFromConfig("RobaAlmas", itemConfig);

                    if (!event.getPlayer().getInventory().contains(souldSword)) {
                        event.getPlayer().getInventory().addItem(souldSword);
                    }
                });
    }

    public void setupOnPlayerDeathEvent() {
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
