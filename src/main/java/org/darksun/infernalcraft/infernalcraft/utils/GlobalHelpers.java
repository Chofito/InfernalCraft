package org.darksun.infernalcraft.infernalcraft.utils;

public class GlobalHelpers {
    public static boolean getChance(double chance) {
        double random = (float) Math.random();
        return random < chance;
    }

    public static boolean getChance(double chance, int maxNumber) {
        double random = Math.random() * maxNumber;
        return random < chance;
    }
}
