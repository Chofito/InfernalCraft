package io.chofito.infernalcraft.infernalcraft.bloodmoon;

import de.leonhard.storage.Config;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBedEnterEvent;

import java.util.List;

public class BloodMoon {
    private BossBar bloodMoonBar;
    private boolean isInProgress = false;
    // This is not the best name but disable day count for the rest of the day
    private boolean isCurrentOrNextDayChecked = false;
    private final World world;
    private Config bloodMoonConfig;
    private int originalWorldMobCap = 0;

    public BloodMoon(World bloodMoonWorld, Config config) {
        this.world = bloodMoonWorld;
        bloodMoonConfig = config;

        setupSomeDefaultSettings();

        Schedulers.sync().runRepeating(task -> {
            int dayInterval = bloodMoonConfig.getOrSetDefault("bloodMoonInterval", 7);
            int dayCounter = bloodMoonConfig.getOrSetDefault("bloodMoonCache.currentDay", 1);
            int previousDayInterval = bloodMoonConfig.getOrSetDefault("bloodMoonCache.previousDayInterval", dayInterval);

            if (dayCounter < 1) {
                bloodMoonConfig.set("bloodMoonCache.currentDay", 1);
            }

            if (previousDayInterval != dayInterval) {
                bloodMoonConfig.set("bloodMoonCache.currentDay", 1);
                bloodMoonConfig.set("bloodMoonCache.previousDayInterval", dayInterval);
            }

            if (world != null && world.getTime() < 13000 && isInProgress) {
                this.stopBloodMoon();
                isInProgress = false;
            } else if (world != null && world.getTime() >= 13000 && !isInProgress) {
                if (dayCounter % dayInterval == 0) {
                    this.startBloodMoon();
                    isInProgress = true;
                }
            }

            if (world != null && world.getTime() < 1000 && !isCurrentOrNextDayChecked) {
                bloodMoonConfig.set("bloodMoonCache.currentDay", dayCounter + 1);
                isCurrentOrNextDayChecked = true;
            } else if (world != null && world.getTime() >= 1000 && isCurrentOrNextDayChecked) {
                isCurrentOrNextDayChecked = false;
            }
        }, 0L, 200L);
    }

    public void startBloodMoon() {
        isInProgress = true;
        setupBloodMoonBar();
        increaseMobCap();
        setupHordes();
    }

    public void stopBloodMoon() {
        isInProgress = false;
        hideBloodMoonBar();
        resetMobCap();
    }

    private void increaseMobCap() {
        double configMonsterSpawnLimitMultiplier = bloodMoonConfig.getOrSetDefault("monsterSpawnLimitMultiplier", 2.0);
        originalWorldMobCap = world.getMonsterSpawnLimit();
        world.setMonsterSpawnLimit((int) configMonsterSpawnLimitMultiplier * originalWorldMobCap);
    }

    private void resetMobCap() {
        world.setMonsterSpawnLimit(originalWorldMobCap);
    }

    public void setupBloodMoonBar() {
        bloodMoonBar = Bukkit.createBossBar("Luna de Sangre",
                BarColor.RED,
                BarStyle.SEGMENTED_12,
                BarFlag.CREATE_FOG,
                BarFlag.DARKEN_SKY
        );

        bloodMoonBar.setProgress(0.0);
        Schedulers.sync().runRepeating(task -> {
            if (isInProgress) {
                updateNightBar();
            } else {
                task.stop();
            }
        }, 10L, 60L);

        List<Player> players = world.getPlayers();
        for (Player player : players) {
            bloodMoonBar.addPlayer(player);
            player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 60.0f, 1.5f);
        }
    }

    private void hideBloodMoonBar() {
        if (bloodMoonBar != null) bloodMoonBar.removeAll();
        bloodMoonBar = null;
    }

    private void updateNightBar() {
        if (bloodMoonBar != null) {
            long timeTotal = 12000;
            long currentTime = world.getTime();
            long timeLeft = 24000 - currentTime;

            double percent = (double) timeLeft / (double) timeTotal;

            if (percent >= 0.0 && percent <= 1.0f) bloodMoonBar.setProgress(1.0 - percent);
        }
    }

    public void setupPreventSleeping() {
        Events.subscribe(PlayerBedEnterEvent.class)
                .handler(event -> {
                    Player player = event.getPlayer();
                    boolean preventSleeping = bloodMoonConfig.getOrSetDefault("preventSleeping", true);
                    boolean onSleepThunderPlayer = bloodMoonConfig.getOrSetDefault("onSleepThunderPlayer", true);
                    boolean onSleepExplodePlayer = bloodMoonConfig.getOrSetDefault("onSleepExplodePlayer", true);

                    if (
                        player.getGameMode() != GameMode.CREATIVE
                        && player.getGameMode() != GameMode.SPECTATOR
                        && preventSleeping
                        && isInProgress
                    ) {

                        if (onSleepThunderPlayer) {
                            world.strikeLightning(player.getLocation());
                        }

                        if (onSleepExplodePlayer) {
                            world.createExplosion(player.getLocation(), 1.0F);
                        }

                        event.setCancelled(true);
                    }
                });
    }

    private void setupHordes() {
    }

    private void setupSomeDefaultSettings() {
        bloodMoonConfig.setDefault("maxMobsPerHorde", 8);
        bloodMoonConfig.setDefault("minMobsPerHorde", 4);
        bloodMoonConfig.setDefault("hordeSpawnDistance", 12);
        bloodMoonConfig.setDefault("naturalMobsDamageMultiplier", 2);
        bloodMoonConfig.setDefault("naturalMobsHealthMultiplier", 1.5);
        bloodMoonConfig.setDefault("hordeSpawnInterval", 800);
        bloodMoonConfig.setDefault("preventSleeping", true);
        bloodMoonConfig.setDefault("onSleepThunderPlayer", false);
        bloodMoonConfig.setDefault("onSleepExplodePlayer", false);
        bloodMoonConfig.setDefault("experienceMultiplier", true);
    }
}
