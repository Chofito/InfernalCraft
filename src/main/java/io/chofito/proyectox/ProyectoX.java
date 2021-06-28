package io.chofito.proyectox;

import de.leonhard.storage.Json;
import io.chofito.proyectox.configuration.DefaultMobsConfiguration;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.plugin.ap.Plugin;
import me.lucko.helper.plugin.ap.PluginDependency;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import io.chofito.proyectox.bloodmoon.BloodMoon;
import io.chofito.proyectox.events.HostileMobsEvents;
import io.chofito.proyectox.events.PlayerEvents;
import org.bukkit.util.EulerAngle;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.Arrays;

@Plugin(
        apiVersion = "1.17",
        name = "ProyectoX",
        version = "0.1",
        description = "A mod with tweaks to improve minecraft difficulty over time",
        load = PluginLoadOrder.POSTWORLD,
        authors = {"Chofito"},
        depends = {@PluginDependency("helper")}
)
public final class ProyectoX extends JavaPlugin {
    private static ProyectoX instance;
    private Json infernalCraftConfig;
    private Json bloodMoonConfig;
    private Json mobsConfig;
    private Json itemsConfig;
    private Json deathTrainConfig;
    private BukkitScheduler scheduler;
    private BloodMoon bloodMoon;
    private World world;

    public static ProyectoX getInstance() {
        return instance;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        infernalCraftConfig = new Json("infernalcraft", "plugins/Infernalcraft");
        bloodMoonConfig = new Json("bloodmoon", "plugins/Infernalcraft");
        mobsConfig = new Json("custom_mobs", "plugins/Infernalcraft");
        itemsConfig = new Json("custom_items", "plugins/Infernalcraft");
        deathTrainConfig = new Json("deathtrain", "plugins/Infernalcraft");

        scheduler = getServer().getScheduler();
        world = Bukkit.getWorld("world");
        setupDefaultSettings();
        setupListeners();

        Events.subscribe(PlayerJoinEvent.class)
                .handler(event -> {
                    ItemStack ruinedSword = ItemStackBuilder.of(Material.DIAMOND_SWORD)
                            .amount(1)
                            .name("Ruined King Sword")
                            .breakable(false)
                            .enchant(Enchantment.DAMAGE_ALL, 10)
                            .showAttributes()
                            .lore("An ancient sword from a Ruined King...")
                            .build();

                    Schedulers.sync().runRepeating(task -> {
                        world.getPlayers().stream()
                                .forEach(player -> {
                                    Arrays.stream(player.getInventory().getContents()).forEach(itemStack -> {
                                        if (itemStack.getItemMeta().getDisplayName() == "Espada Demoniaca") {
                                            new ParticleBuilder(ParticleEffect.SOUL, player.getLocation())
                                                    .setOffsetY(1f)
                                                    .setOffsetX(0.5f)
                                                    .setOffsetZ(0.5f)
                                                    .setAmount(8)
                                                    .display();
                                        }
                                    });
                                });
                    }, 0, 10);
                });

        if (infernalCraftConfig.getOrSetDefault("enableBloodMoonModule", true)) {
            setupBloodMoon();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void setupListeners() {
        PlayerEvents.setupOnPlayerJoinEvents();
        PlayerEvents.setupOnPlayerWithTotemDeathEvents();
        PlayerEvents.setupOnPlayerDeathEvent();
        HostileMobsEvents.setupCustomZombieSpawn();
        HostileMobsEvents.setupCustomCreeperSpawn(infernalCraftConfig);
        HostileMobsEvents.setupCustomSkeletonSpawn();
        HostileMobsEvents.onCreeperDeathRemovePotionEffect();
        HostileMobsEvents.setupShulkerExplosion();
    }

    public void setupBloodMoon() {
        bloodMoon = new BloodMoon(world, bloodMoonConfig);
        bloodMoon.setupBloodMoonEvents();
    }

    public void setupDefaultSettings() {
        DefaultMobsConfiguration.setupDefaultMobs(this.mobsConfig);
    }

    public BukkitScheduler getScheduler() {
        return scheduler;
    }

    public Json getInfernalCraftConfig() {
        return infernalCraftConfig;
    }

    public Json getBloodMoonConfig() {
        return bloodMoonConfig;
    }

    public Json getMobsConfig() {
        return mobsConfig;
    }

    public Json getItemsConfig() {
        return itemsConfig;
    }

    public Json getDeathTrainConfig() {
        return deathTrainConfig;
    }

    public BloodMoon getBloodMoon() {
        return bloodMoon;
    }
}
