package io.chofito.proyectox.bloodmoon;

import de.leonhard.storage.Json;
import io.chofito.proyectox.mobs.EntityBuilder;
import io.chofito.proyectox.random.ObjectWeighted;
import io.chofito.proyectox.utils.GlobalHelpers;
import io.chofito.proyectox.utils.ItemHelpers;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.random.RandomSelector;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class BloodMoon {
    private boolean isInProgress = false;
    private int originalWorldMobCap = 0;
    // This is not the best name but disable day count for the rest of the day
    private boolean isCurrentOrNextDayChecked = false;
    private BossBar bloodMoonBar;
    private Random random = new Random();
    private final World world;
    private final Json bloodMoonConfig;

    public BloodMoon(World bloodMoonWorld, Json config) {
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
            addBloodMoonBarToPlayer(player);
            player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 60.0f, 1.5f);
        }
    }

    private void addBloodMoonBarToPlayer(Player player) {
        if (isInProgress && bloodMoonBar != null && !bloodMoonBar.getPlayers().contains(player))
            bloodMoonBar.addPlayer(player);
    }

    private void removeBloodMoonBarToPlayer(Player player) {
        if (isInProgress && bloodMoonBar != null) bloodMoonBar.removePlayer(player);
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

    public void setupBloodMoonEvents() {
        setupPreventSleeping();
        setupPlayerJoinEvent();
        setupPlayerTeleportEvent();
        setupPlayerRespawnEvent();
        setupPlayerChangeWorld();
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

    public void handleBloodMoonPlayerWorldChange(World from, World to, Player player) {
        if (world != to && from != world) return;

        if (from != to) {
            if (to == world && isInProgress) {
                addBloodMoonBarToPlayer(player);
            }

            if (from == world && isInProgress) {
                removeBloodMoonBarToPlayer(player);
            }
        }
    }

    public void setupPlayerJoinEvent() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(event -> {
                    Player player = event.getPlayer();

                    if (player.getWorld() == world && isInProgress) {
                        bloodMoonBar.addPlayer(player);
                    }
                });
    }

    public void setupPlayerTeleportEvent() {
        Events.subscribe(PlayerTeleportEvent.class)
                .handler(event -> {
                    World to = event.getTo().getWorld();
                    World from = event.getFrom().getWorld();

                    handleBloodMoonPlayerWorldChange(from, to, event.getPlayer());
                });
    }

    public void setupPlayerRespawnEvent() {
        Events.subscribe(PlayerRespawnEvent.class)
                .handler(event -> {
                    World from = event.getPlayer().getWorld();
                    World to = event.getRespawnLocation().getWorld();

                    handleBloodMoonPlayerWorldChange(from, to, event.getPlayer());
                });
    }

    public void setupPlayerChangeWorld() {
        Events.subscribe(PlayerChangedWorldEvent.class)
                .handler(event -> {
                    World from = event.getFrom();
                    World to = event.getPlayer().getWorld();

                    handleBloodMoonPlayerWorldChange(from, to, event.getPlayer());
                });
    }

    private void setupHordes() {
        int hordeInterval = bloodMoonConfig.getOrSetDefault("hordeInterval", 500);

        Schedulers.sync().runRepeating(task -> {
            if (!isInProgress) task.close();

            List<Player> players = world.getPlayers();
            List<String> hordeEnemiesNames = (List<String>) bloodMoonConfig.getList("mobsToSpawn.hordeEnemies");
            List<ObjectWeighted> hordeEnemiesWeighted = new ArrayList<>();

            for (String enemyName : hordeEnemiesNames) {
                double spawnChance = bloodMoonConfig.getOrSetDefault("mobsToSpawn." + enemyName.replaceAll("\\s+","") + ".spawnChance", 0.25);
                hordeEnemiesWeighted.add(new ObjectWeighted(enemyName, spawnChance));
            }

            RandomSelector<ObjectWeighted> entitySelector = RandomSelector.weighted(hordeEnemiesWeighted);

            if (players.size() <= 0) return;

            int selectedIndex = random.nextInt(players.size());
            Player player = players.get(selectedIndex);
            int hordeMaxDistance = bloodMoonConfig.getInt("hordeMaxDistance");
            int hordeMinDistance = bloodMoonConfig.getInt("hordeMinDistance");
            int minMobs = bloodMoonConfig.getInt("minMobsPerHorde");
            int maxMobs = bloodMoonConfig.getInt("maxMobsPerHorde");
            int totalMobs = GlobalHelpers.getRandomFromRange(minMobs, maxMobs, random);

            for (int i = 0; i <= totalMobs; i++) {
                Location newMobLocation = player.getLocation().clone();
                String entityName = entitySelector.pick(random).getName();
                String nameJsonSelector = entityName.replaceAll("\\s+","");
                String entityTypeString = bloodMoonConfig.getOrSetDefault("mobsToSpawn." + nameJsonSelector + ".entity", "ZOMBIE");
                double angle = random.nextDouble() * 360;
                double radius = GlobalHelpers.getRandomFromRange(hordeMinDistance, hordeMaxDistance, random);
                newMobLocation.add(Math.cos(angle) * radius, 0, Math.sin(angle) * radius);

                newMobLocation.setY(world.getHighestBlockYAt(newMobLocation) + 1);

                EntityBuilder entityToSpawn = EntityBuilder
                        .ofString(entityTypeString, newMobLocation)
                        .setCustomName(entityName);

                if (entityToSpawn.getType() != EntityType.SPIDER) {
                    Material mainHandItemMaterial = ItemHelpers
                            .getMaterialFromString(bloodMoonConfig.getString("mobsToSpawn." + nameJsonSelector + ".mainHand"));
                    Material offHandItemMaterial = ItemHelpers
                            .getMaterialFromString(bloodMoonConfig.getString("mobsToSpawn." + nameJsonSelector + ".offHand"));
                    Material helmetMaterial = ItemHelpers
                            .getMaterialFromString(bloodMoonConfig.getString("mobsToSpawn." + nameJsonSelector + ".helmet"));
                    Material chestplateMaterial = ItemHelpers
                            .getMaterialFromString(bloodMoonConfig.getString("mobsToSpawn." + nameJsonSelector + ".chestplate"));
                    Material leggingsMaterial = ItemHelpers
                            .getMaterialFromString(bloodMoonConfig.getString("mobsToSpawn." + nameJsonSelector + ".leggings"));
                    Material bootsMaterial = ItemHelpers
                            .getMaterialFromString(bloodMoonConfig.getString("mobsToSpawn." + nameJsonSelector + ".boots"));

                    if (mainHandItemMaterial != null) {
                        ItemStack mainHandItem = ItemStackBuilder
                                .of(mainHandItemMaterial)
                                .build();
                        entityToSpawn.setItemMainHand(mainHandItem);
                    }

                    if (offHandItemMaterial != null) {
                        ItemStack offHandItem = ItemStackBuilder
                                .of(offHandItemMaterial)
                                .build();
                        entityToSpawn.setItemOffHand(offHandItem);
                    }

                    if (helmetMaterial != null) {
                        ItemStack helmetItem = ItemStackBuilder
                                .of(helmetMaterial)
                                .build();
                        entityToSpawn.setHelmet(helmetItem);
                    }

                    if (chestplateMaterial != null) {
                        ItemStack chestplateItem = ItemStackBuilder
                                .of(chestplateMaterial)
                                .build();
                        entityToSpawn.setChestplate(chestplateItem);
                    }

                    if (leggingsMaterial != null) {
                        ItemStack leggingsItem = ItemStackBuilder
                                .of(mainHandItemMaterial)
                                .build();
                        entityToSpawn.setItemMainHand(leggingsItem);
                    }

                    if (bootsMaterial != null) {
                        ItemStack bootsItem = ItemStackBuilder
                                .of(bootsMaterial)
                                .build();
                        entityToSpawn.setItemOffHand(bootsItem);
                    }
                }
            }

            world.strikeLightningEffect(player.getLocation());
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 200,1));

        }, 0, hordeInterval);
    }

    private void setupSomeDefaultSettings() {
        bloodMoonConfig.setDefault("hordeInterval", 500);
        bloodMoonConfig.setDefault("maxMobsPerHorde", 24);
        bloodMoonConfig.setDefault("minMobsPerHorde", 10);
        bloodMoonConfig.setDefault("hordeMaxDistance", 16);
        bloodMoonConfig.setDefault("hordeMinDistance", 8);
        bloodMoonConfig.setDefault("naturalMobsDamageMultiplier", 2);
        bloodMoonConfig.setDefault("naturalMobsHealthMultiplier", 1.5);
        bloodMoonConfig.setDefault("hordeSpawnInterval", 800);
        bloodMoonConfig.setDefault("preventSleeping", true);
        bloodMoonConfig.setDefault("onSleepThunderPlayer", false);
        bloodMoonConfig.setDefault("onSleepExplodePlayer", false);
        bloodMoonConfig.setDefault("experienceMultiplier", true);
        bloodMoonConfig.setDefault("mobsToSpawn.hordeEnemies", Arrays.asList("Guerrero Zombie", "Guerrero Oscuro"));
        // Guerrero Zombie
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroZombie.entity", "ZOMBIE");
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroZombie.mainHand", "IRON_SWORD");
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroZombie.spawnChance", 0.75);
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroZombie.damageMultiplier", 1.25);
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroZombie.lifeMultiplier", 1.5);
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroZombie.chestplate", "LEATHER_CHESTPLATE");
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroZombie.leggings", "LEATHER_LEGGINGS");
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroZombie.helmet", "LEATHER_HELMET");
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroZombie.boots", "LEATHER_BOOTS");
        // Guerrero Oscuro
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroOscuro.entity", "WITHER_SKELETON");
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroOscuro.mainHand", "IRON_AXE");
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroOscuro.offHand", "TOTEM_OF_UNDYING");
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroOscuro.spawnChance", 0.1);
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroOscuro.damageMultiplier", 2);
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroOscuro.lifeMultiplier", 2.5);
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroOscuro.chestplate", "NETHERITE_CHESTPLATE");
        bloodMoonConfig.setDefault("mobsToSpawn.GuerreroOscuro.leggings", "NETHERITE_LEGGINGS");
    }
}
