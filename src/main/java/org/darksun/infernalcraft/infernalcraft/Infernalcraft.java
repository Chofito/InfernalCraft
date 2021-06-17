package org.darksun.infernalcraft.infernalcraft;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.darksun.infernalcraft.infernalcraft.events.HostileMobsEvents;
import org.darksun.infernalcraft.infernalcraft.events.PlayerEvents;

import java.util.List;

public final class Infernalcraft extends JavaPlugin {
    FileConfiguration config = getConfig();
    private BossBar nightBar;
    private boolean running = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        setupListeners();
        setupConfig();

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if (Bukkit.getWorld("world").getTime() < 13000 && running) {
                    nightBar.removeAll();
                    running = false;
                } else if (Bukkit.getWorld("world").getTime() >= 13000 && !running) {
                    nightBar = Bukkit.createBossBar("Luna de Sangre",
                            BarColor.RED,
                            BarStyle.SEGMENTED_12
                    );
                    nightBar.setProgress(0.0);

                    List<Player> players = Bukkit.getWorld("world").getPlayers();
                    for (Player player : players)
                    {
                        nightBar.addPlayer(player);
                    }

                    running = true;
                }
            }
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void setupListeners() {
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getServer().getPluginManager().registerEvents(new HostileMobsEvents(config), this);
    }

    public void setupConfig() {
        Configurations.SetupDefaultSettings(config);
        saveConfig();
    }
}
