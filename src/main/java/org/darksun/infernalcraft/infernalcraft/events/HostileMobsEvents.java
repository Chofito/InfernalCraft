package org.darksun.infernalcraft.infernalcraft.events;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.darksun.infernalcraft.infernalcraft.utils.GlobalHelpers;

public class HostileMobsEvents implements Listener {
    public FileConfiguration configuration;

    public HostileMobsEvents(FileConfiguration config) {
        this.configuration = config;
    }

    @EventHandler
    public void onCreatureSpawn(EntitySpawnEvent event) {
        switch (event.getEntity().getType()) {
            case ZOMBIE:
                LivingEntity zombieEntity = (LivingEntity) event.getEntity();
                ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);

                zombieEntity.setCustomName("Zombie Mamadisimo");
                zombieEntity.getEquipment().setChestplate(chestplate);
                break;
            case CREEPER:
                if (GlobalHelpers.getChance(configuration.getDouble("chargedCreeperSpawnChance"))) {
                    Creeper creeperEntity = (Creeper) event.getEntity();
                    creeperEntity.setPowered(true);
                    creeperEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 999999));
                }
                break;
            case SKELETON:
                Skeleton skeletonEntity = (Skeleton) event.getEntity();
                ItemStack punchBow = new ItemStack(Material.BOW);
                ItemMeta punchBowMeta = punchBow.getItemMeta();
                punchBowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true);
                punchBowMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
                punchBow.setItemMeta(punchBowMeta);

                skeletonEntity.getEquipment().setItemInMainHand(punchBow);
                skeletonEntity.getEquipment().setItemInMainHandDropChance(1);
                break;
        }
    }
}
