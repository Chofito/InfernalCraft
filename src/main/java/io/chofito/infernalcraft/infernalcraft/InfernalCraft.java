package io.chofito.infernalcraft.infernalcraft;

import de.leonhard.storage.Config;
import de.leonhard.storage.internal.settings.ConfigSettings;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import io.chofito.infernalcraft.infernalcraft.bloodmoon.BloodMoon;
import io.chofito.infernalcraft.infernalcraft.events.HostileMobsEvents;
import io.chofito.infernalcraft.infernalcraft.events.PlayerEvents;

public final class InfernalCraft extends JavaPlugin {
    private static InfernalCraft instance;
    Config infernalCraftConfig;
    Config bloodMoonConfig;
    BukkitScheduler scheduler;
    private BossBar nightBar;
    private BloodMoon bloodMoon;
    private World world;

    public static InfernalCraft getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        infernalCraftConfig = new Config("infernalcraft", "plugins/Infernalcraft");
        bloodMoonConfig = new Config("bloodmoon", "plugins/Infernalcraft");
        infernalCraftConfig.setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);
        bloodMoonConfig.setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);
        scheduler = getServer().getScheduler();
        world = Bukkit.getWorld("world");
        bloodMoon = new BloodMoon(world, bloodMoonConfig);
        setupListeners();
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
        bloodMoon.setupPreventSleeping();
    }

    public BukkitScheduler getScheduler() {
        return scheduler;
    }
}
