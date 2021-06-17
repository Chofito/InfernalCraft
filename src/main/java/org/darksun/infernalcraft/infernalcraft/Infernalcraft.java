package org.darksun.infernalcraft.infernalcraft;

import org.bukkit.plugin.java.JavaPlugin;
import org.darksun.infernalcraft.infernalcraft.events.PlayerEvents;

public final class Infernalcraft extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
