package io.chofito.proyectox;

import de.leonhard.storage.Json;
import me.lucko.helper.Events;
import me.lucko.helper.plugin.ap.Plugin;
import me.lucko.helper.plugin.ap.PluginDependency;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import io.chofito.proyectox.bloodmoon.BloodMoon;
import io.chofito.proyectox.events.HostileMobsEvents;
import io.chofito.proyectox.events.PlayerEvents;

@Plugin(
        name = "ProyectoX",
        version = "0.1",
        description = "A mod with tweaks to improve minecraft difficulty over time",
        load = PluginLoadOrder.POSTWORLD,
        authors = {"Chofito"},
        depends = {@PluginDependency("helper")}
)
public final class ProyectoX extends JavaPlugin {
    private static ProyectoX instance;
    Json infernalCraftConfig;
    Json bloodMoonConfig;
    BukkitScheduler scheduler;
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
        scheduler = getServer().getScheduler();
        world = Bukkit.getWorld("world");
        setupListeners();

        Events.subscribe(PlayerJoinEvent.class)
                .handler(event -> event.getPlayer().setGravity(true));

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

    public BukkitScheduler getScheduler() {
        return scheduler;
    }
}
