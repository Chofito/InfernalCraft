package io.chofito.proyectox.utils;

import org.bukkit.entity.EntityType;

public class EntityHelpers {
    public static EntityType getEntityTypeFromString(String stringEntity) {
        return EntityType.valueOf(stringEntity);
    }
}
