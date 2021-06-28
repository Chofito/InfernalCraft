package io.chofito.proyectox.mobs;

import de.leonhard.storage.Json;
import io.chofito.proyectox.ProyectoX;
import io.chofito.proyectox.utils.EntityHelpers;
import io.chofito.proyectox.utils.ItemHelpers;
import me.lucko.helper.item.ItemStackBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
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

    public EntityBuilder setItemMainHand(ItemStack item) {
        if (item == null) return this;

        this.setItemMainHand(item, 0);

        return this;
    }

    public EntityBuilder setItemMainHand(ItemStack item, float dropChance) {
        if (item == null) return this;

        if (entity instanceof LivingEntity) {
            ((LivingEntity)(entity)).getEquipment().setItemInMainHand(item);
            ((LivingEntity)(entity)).getEquipment().setItemInMainHandDropChance(dropChance);
        }
        return this;
    }

    public EntityBuilder setItemOffHand(ItemStack item) {
        if (item == null) return this;

        this.setItemOffHand(item, 0);

        return this;
    }

    public EntityBuilder setItemOffHand(ItemStack item, float dropChance) {
        if (item == null) return this;

        if (entity instanceof LivingEntity) {
            ((LivingEntity)(entity)).getEquipment().setItemInOffHand(item);
            ((LivingEntity)(entity)).getEquipment().setItemInOffHandDropChance(dropChance);
        }
        return this;
    }

    public EntityBuilder setHelmet(ItemStack item) {
        if (item == null) return this;

        this.setHelmet(item, 0);

        return this;
    }

    public EntityBuilder setHelmet(ItemStack item, float dropChance) {
        if (item == null) return this;

        if (entity instanceof LivingEntity) {
            ((LivingEntity)(entity)).getEquipment().setHelmet(item);
            ((LivingEntity)(entity)).getEquipment().setHelmetDropChance(dropChance);
        }
        return this;
    }

    public EntityBuilder setChestplate(ItemStack item) {
        if (item == null) return this;

        this.setChestplate(item, 0);

        return this;
    }

    public EntityBuilder setChestplate(ItemStack item, float dropChance) {
        if (item == null) return this;

        if (entity instanceof LivingEntity) {
            ((LivingEntity)(entity)).getEquipment().setChestplate(item);
            ((LivingEntity)(entity)).getEquipment().setChestplateDropChance(dropChance);
        }
        return this;
    }

    public EntityBuilder setBoots(ItemStack item) {
        if (item == null) return this;

        this.setBoots(item, 0);

        return this;
    }

    public EntityBuilder setBoots(ItemStack item, float dropChance) {
        if (item == null) return this;

        if (entity instanceof LivingEntity) {
            ((LivingEntity)(entity)).getEquipment().setBoots(item);
            ((LivingEntity)(entity)).getEquipment().setBootsDropChance(dropChance);
        }
        return this;
    }

    public EntityBuilder setLeggings(ItemStack item) {
        if (item == null) return this;

        this.setLeggings(item, 0);

        return this;
    }

    public EntityBuilder setLeggings(ItemStack item, float dropChance) {
        if (item == null) return this;

        if (entity instanceof LivingEntity) {
            ((LivingEntity)(entity)).getEquipment().setLeggings(item);
            ((LivingEntity)(entity)).getEquipment().setLeggingsDropChance(dropChance);
        }
        return this;
    }

    public EntityBuilder setAttributeValue(Attribute attribute, double value) {
        if (attribute != null) {
            ((LivingEntity)(entity)).getAttribute(attribute).setBaseValue(value);
        }

        return this;
    }

    public double getAttributeValue(Attribute attribute) {
        if (attribute != null) {
            return ((LivingEntity)(entity)).getAttribute(attribute).getBaseValue();
        }

        return 0;
    }

    public EntityBuilder setMaxHealth(double value) {
        this.setAttributeValue(Attribute.GENERIC_MAX_HEALTH, value);
        ((LivingEntity)(entity)).setHealth(value);

        return this;
    }

    public EntityBuilder multiplyMaxHealth(double multiplier) {
        this.setMaxHealth(multiplier * this.getAttributeValue(Attribute.GENERIC_MAX_HEALTH));

        return this;
    }

    public static Entity buildEntityFromConfig(String key, Json configProvider, Location location) {
        if (key == null || configProvider == null || location == null) return null;

        String entityTypeString = configProvider.getOrSetDefault(key + ".entity", "ZOMBIE");
        EntityBuilder spawnedEntity = EntityBuilder.ofString(entityTypeString, location);
        Material mainHandItemMaterial = ItemHelpers
                .getMaterialFromString(configProvider.getString( key + ".mainHand"));
        Material offHandItemMaterial = ItemHelpers
                .getMaterialFromString(configProvider.getString(key + ".offHand"));
        Material helmetMaterial = ItemHelpers
                .getMaterialFromString(configProvider.getString(key + ".helmet"));
        Material chestplateMaterial = ItemHelpers
                .getMaterialFromString(configProvider.getString(key + ".chestplate"));
        Material leggingsMaterial = ItemHelpers
                .getMaterialFromString(configProvider.getString(key + ".leggings"));
        Material bootsMaterial = ItemHelpers
                .getMaterialFromString(configProvider.getString(key + ".boots"));
        String displayName = configProvider.getString(key + ".displayName");

        if (displayName != null) {
            spawnedEntity.setCustomName(displayName);
        }

        if (mainHandItemMaterial != null) {
            ItemStack mainHandItem = ItemStackBuilder
                    .of(mainHandItemMaterial)
                    .build();
            spawnedEntity.setItemMainHand(mainHandItem);
        }

        if (offHandItemMaterial != null) {
            ItemStack offHandItem = ItemStackBuilder
                    .of(offHandItemMaterial)
                    .build();
            spawnedEntity.setItemOffHand(offHandItem);
        }

        if (helmetMaterial != null) {
            ItemStack helmetItem = ItemStackBuilder
                    .of(helmetMaterial)
                    .build();
            spawnedEntity.setHelmet(helmetItem);
        }

        if (chestplateMaterial != null) {
            ItemStack chestplateItem = ItemStackBuilder
                    .of(chestplateMaterial)
                    .build();
            spawnedEntity.setChestplate(chestplateItem);
        }

        if (leggingsMaterial != null) {
            ItemStack leggingsItem = ItemStackBuilder
                    .of(leggingsMaterial)
                    .build();
            spawnedEntity.setLeggings(leggingsItem);
        }

        if (bootsMaterial != null) {
            ItemStack bootsItem = ItemStackBuilder
                    .of(bootsMaterial)
                    .build();
            spawnedEntity.setBoots(bootsItem);
        }

        return spawnedEntity.build();
    }

    public EntityType getType() {
        return this.entity.getType();
    }

    public Entity build() {
        return this.entity;
    }
}
