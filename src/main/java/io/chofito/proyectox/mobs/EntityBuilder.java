package io.chofito.proyectox.mobs;

import io.chofito.proyectox.ProyectoX;
import io.chofito.proyectox.utils.EntityHelpers;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class EntityBuilder {
    private Entity entity;

    public EntityBuilder(EntityType entityType, Location location) {
        this.entity = ProyectoX.getInstance().getWorld().spawnEntity(location, entityType);
    }

    public static EntityBuilder of(EntityType entityType, Location location) {
        return new EntityBuilder(entityType, location);
    }

    public static EntityBuilder ofString(String entityType, Location location) {
        return new EntityBuilder(getEntityFromString(entityType), location);
    }

    public static EntityType getEntityFromString(String entityString) {
        return EntityHelpers.getEntityTypeFromString(entityString);
    }

    public EntityBuilder setCustomName(String customName) {
        this.entity.setCustomName(customName);
        return this;
    }

    public EntityBuilder setMaxHealth(double newMaxHealth) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)(entity)).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHealth);
            ((LivingEntity)(entity)).setHealth(newMaxHealth);
        }

        return this;
    }

    public EntityBuilder setItemMainHand(ItemStack item) {
        this.setItemMainHand(item, 0);

        return this;
    }

    public EntityBuilder setItemMainHand(ItemStack item, float dropChance) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)(entity)).getEquipment().setItemInMainHand(item);
            ((LivingEntity)(entity)).getEquipment().setItemInMainHandDropChance(dropChance);
        }
        return this;
    }

    public EntityBuilder setItemOffHand(ItemStack item) {
        this.setItemMainHand(item, 0);

        return this;
    }

    public EntityBuilder setItemOffHand(ItemStack item, float dropChance) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)(entity)).getEquipment().setItemInOffHand(item);
            ((LivingEntity)(entity)).getEquipment().setItemInOffHandDropChance(dropChance);
        }
        return this;
    }

    public EntityType getType() {
        return this.entity.getType();
    }

    public Entity build() {
        return this.entity;
    }
}
