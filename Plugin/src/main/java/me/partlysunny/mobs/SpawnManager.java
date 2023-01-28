package me.partlysunny.mobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class SpawnManager implements Listener {

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        Toughener.toughen(event.getEntity());
    }

}
