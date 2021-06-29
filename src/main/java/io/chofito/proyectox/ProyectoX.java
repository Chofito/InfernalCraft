package io.chofito.proyectox;

import de.leonhard.storage.Json;
import io.chofito.proyectox.configuration.DefaultItemsConfiguration;
import io.chofito.proyectox.configuration.DefaultMobsConfiguration;
import io.chofito.proyectox.configuration.GlobalConfiguration;
import io.chofito.proyectox.deathtrain.DeathTrain;
import io.chofito.proyectox.events.NeutralMobsEvents;
import io.chofito.proyectox.hardcoremobs.HardcoreMobs;
import me.lucko.helper.plugin.ap.Plugin;
import me.lucko.helper.plugin.ap.PluginDependency;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import io.chofito.proyectox.bloodmoon.BloodMoon;
import io.chofito.proyectox.events.HostileMobsEvents;
import io.chofito.proyectox.events.PlayerEvents;

import java.util.Random;

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
    private Json deathTrainConfig;
    private Json hardcoreMobsConfig;
    private Json mobsConfig;
    private Json itemsConfig;
    private BukkitScheduler scheduler;
    private BloodMoon bloodMoon;
    private DeathTrain deathTrain;
    private HardcoreMobs hardcoreMobs;
    private World world;
    private Random random;
    private HostileMobsEvents hostileMobsEvents;
    private PlayerEvents playerEvents;
    private NeutralMobsEvents neutralMobsEvents;

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
        this.infernalCraftConfig = new Json("infernalcraft", "plugins/Infernalcraft");
        this.bloodMoonConfig = new Json("bloodmoon", "plugins/Infernalcraft");
        this.deathTrainConfig = new Json("deathtrain", "plugins/Infernalcraft");
        this.hardcoreMobsConfig = new Json("hardcoremobs", "plugins/Infernalcraft");
        this.mobsConfig = new Json("custom_mobs", "plugins/Infernalcraft");
        this.itemsConfig = new Json("custom_items", "plugins/Infernalcraft");

        this.hostileMobsEvents = new HostileMobsEvents(this.infernalCraftConfig);
        this.playerEvents = new PlayerEvents(this.itemsConfig);
        this.neutralMobsEvents = new NeutralMobsEvents();

        this.scheduler = getServer().getScheduler();
        this.world = Bukkit.getWorld("world");
        setupDefaultSettings();
        setupListeners();

        if (this.infernalCraftConfig.getBoolean("enableBloodMoonModule")) {
            setupBloodMoon();
        }

        if (this.infernalCraftConfig.getBoolean("enableDeathTrainModule")) {
            setupDeathTrain();
        }

        if (this.infernalCraftConfig.getBoolean("enableHardCoreMobs")) {
            setupHardcoreMobs();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void setupListeners() {
        this.playerEvents.setupEvents();
        this.hostileMobsEvents.setupEvents();
        this.neutralMobsEvents.setupEvents();
    }

    public void setupBloodMoon() {
        this.bloodMoon = new BloodMoon(this.world, this.bloodMoonConfig, this.mobsConfig);
        this.bloodMoon.setupBloodMoonEvents();
    }

    private void setupDeathTrain() {
        this.deathTrain = new DeathTrain(this.deathTrainConfig);
    }

    private void setupHardcoreMobs() {
        this.hardcoreMobs = new HardcoreMobs(this.hardcoreMobsConfig);
        this.hardcoreMobs.setupHardcoreMobs();
    }

    public void setupDefaultSettings() {
        DefaultMobsConfiguration.setupDefaultMobs(this.mobsConfig);
        DefaultItemsConfiguration.setupDefaultItems(this.itemsConfig);
        GlobalConfiguration.setupDefaultConfig(this.infernalCraftConfig);
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
