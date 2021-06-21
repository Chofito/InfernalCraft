package io.chofito.proyectox.events;

import de.leonhard.storage.Config;
import de.leonhard.storage.Json;
import me.lucko.helper.Events;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import io.chofito.proyectox.utils.GlobalHelpers;

public class HostileMobsEvents {
    public static void setupCustomZombieSpawn() {
        Events.subscribe(CreatureSpawnEvent.class)
                .filter(event -> event.getEntity().getType() == EntityType.ZOMBIE)
                .filter(event -> event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL)
                .handler(event -> {
                    LivingEntity zombieEntity = event.getEntity();
                    ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);

                    zombieEntity.setCustomName("Zombie Mamadisimo");
                    zombieEntity.getEquipment().setChestplate(chestplate);
                });
    }

    public static void setupCustomCreeperSpawn(Json config) {
        Events.subscribe(CreatureSpawnEvent.class)
                .filter(event -> event.getEntity().getType() == EntityType.CREEPER)
                .filter(event -> event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL)
                .handler(event -> {
                    if (GlobalHelpers.getChance(config.getOrSetDefault("chargedCreeperSpawnChance", 0.5))) {
                        Creeper creeperEntity = (Creeper) event.getEntity();
                        creeperEntity.setPowered(true);
                        creeperEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 999999));
                    }
                });
    }

    public static void onCreeperDeathRemovePotionEffect() {
        Events.subscribe(EntityExplodeEvent.class)
                .filter(event -> event.getEntity().getType() == EntityType.CREEPER)
                .handler(event -> {
                    Creeper creeper = (Creeper) event.getEntity();
                    creeper.getActivePotionEffects().stream()
                            .map(PotionEffect::getType)
                            .forEach(creeper::removePotionEffect);
                });
    }

    public static void setupCustomSkeletonSpawn() {
        Events.subscribe(CreatureSpawnEvent.class)
                .filter(event -> event.getEntity().getType() == EntityType.SKELETON)
                .filter(event -> event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL)
                .handler(event -> {
                    Skeleton skeletonEntity = (Skeleton) event.getEntity();
                    ItemStack punchBow = new ItemStack(Material.BOW);
                    ItemMeta punchBowMeta = punchBow.getItemMeta();
                    punchBowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true);
                    punchBowMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
                    punchBow.setItemMeta(punchBowMeta);

                    skeletonEntity.getEquipment().setItemInMainHand(punchBow);
                    skeletonEntity.getEquipment().setItemInMainHandDropChance(1);
                });
    }

    public static void setupShulkerExplosion() {
        Events.subscribe(ProjectileHitEvent.class)
                .filter(e -> e.getEntity().getType() == EntityType.SHULKER_BULLET)
                .filter(e -> e.getHitEntity() != null)
                .handler(event -> {
                    LivingEntity entity = (LivingEntity) event.getHitEntity();

                    entity.getWorld().createExplosion(entity.getLocation(), 1F);
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 200, 1));
                });
    }
}
