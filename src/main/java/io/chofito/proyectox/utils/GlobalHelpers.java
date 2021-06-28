package io.chofito.proyectox.utils;


import xyz.xenondevs.particle.ParticleEffect;

import java.util.Random;

public class GlobalHelpers {
    public static ParticleEffect getParticleEffectFromString(String particleEffectString) {
        return ParticleEffect.valueOf(particleEffectString);
    }

    public static int getRandomFromRange(int min, int max) {
        Random random = new Random();

        return getRandomFromRange(min, max, random);
    }

    public static int getRandomFromRange(int min, int max, Random provider) {
        if (max <= 0 || min <= 0) return 0;
        if (min == max) return max;
        if (min > max) {
            return provider.nextInt((min + 1) - max) + max;
        }

        return provider.nextInt((max + 1) - min) + min;
    }

    public static boolean getChance(double chance) {
        double random = (float) Math.random();
        return random < chance;
    }

    public static boolean getChance(double chance, int maxNumber) {
        double random = Math.random() * maxNumber;
        return random < chance;
    }
}
