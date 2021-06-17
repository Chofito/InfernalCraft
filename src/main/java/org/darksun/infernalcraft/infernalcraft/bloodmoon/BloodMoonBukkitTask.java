package org.darksun.infernalcraft.infernalcraft.bloodmoon;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodMoonBukkitTask extends BukkitRunnable {
    private final JavaPlugin plugin;
    private BossBar nightBar;

    public BloodMoonBukkitTask(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getServer().broadcastMessage("Welcome to Bukkit! Remember to read the documentation!");
        if (Bukkit.getWorld("world").getTime() < 13000) {
            nightBar = Bukkit.createBossBar("Luna de Sangre",
                    BarColor.RED,
                    BarStyle.SEGMENTED_12,
                    BarFlag.CREATE_FOG,
                    BarFlag.DARKEN_SKY
            );
        } else if (Bukkit.getWorld("world").getTime() >= 13000) {
            nightBar.removeAll();
        }
    }
}
