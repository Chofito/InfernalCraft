package io.chofito.proyectox.deathtrain;

import de.leonhard.storage.Json;

import java.util.TimerTask;

public class DeathTrainTimerTask extends TimerTask {
    Json bloodmoonConfig;

    public DeathTrainTimerTask(Json bloodmoonConfig) {
        this.bloodmoonConfig = bloodmoonConfig;
    }

    public void run() {
        int currentDay = bloodmoonConfig.getOrSetDefault("cache.currentDay", 0);
        bloodmoonConfig.set("cache.currentDay", currentDay + 1);
    }
}
