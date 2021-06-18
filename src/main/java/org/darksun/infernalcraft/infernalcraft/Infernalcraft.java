package org.darksun.infernalcraft.infernalcraft;

import de.leonhard.storage.Json;
import me.lucko.helper.Events;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.darksun.infernalcraft.infernalcraft.events.HostileMobsEvents;
import org.darksun.infernalcraft.infernalcraft.events.PlayerEvents;

import java.util.List;

public final class Infernalcraft extends JavaPlugin {
    Infernalcraft instance;
    Json infernalCraftConfig;
    BukkitScheduler scheduler;
    private boolean running = false;
    private BossBar nightBar;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        infernalCraftConfig = new Json("infernalcraft", "plugins/Infernalcraft");
        scheduler = getServer().getScheduler();
        setupListeners();
        setupRepeatingTasks();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void setupListeners() {
        PlayerEvents.setupOnPlayerJoinEvents();
        PlayerEvents.setupOnPlayerWithTotemDeathEvents();
        HostileMobsEvents.setupCustomZombieSpawn();
        HostileMobsEvents.setupCustomCreeperSpawn(infernalCraftConfig);
        HostileMobsEvents.setupCustomSkeletonSpawn();
    }


    public void setupRepeatingTasks() {
        scheduler.scheduleSyncRepeatingTask(this, () -> {
            World world = Bukkit.getWorld("world");
            if (world != null && world.getTime() < 13000 && running) {
                nightBar.removeAll();
                running = false;
            } else if (world != null &&  world.getTime() >= 13000 && !running) {
                nightBar = Bukkit.createBossBar("Luna de Sangre",
                        BarColor.RED,
                        BarStyle.SEGMENTED_12
                );
                nightBar.setProgress(0.0);

                List<Player> players = world.getPlayers();

                for (Player player : players) {
                    nightBar.addPlayer(player);
                }

                running = true;
            }
        }, 0L, 20L);
    }
}
