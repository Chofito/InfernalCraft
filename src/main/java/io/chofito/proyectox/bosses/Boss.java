package io.chofito.proyectox.bosses;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class Boss {
    private LivingEntity bossEntity;
    private EntityType bossType;

    public EntityType getBossType() {
        return bossType;
    }

    public void setBossType(EntityType bossType) {
        this.bossType = bossType;
    }

    public Boss(LivingEntity entity) {
        bossEntity = entity;
        bossType = entity.getType();
    }

}
