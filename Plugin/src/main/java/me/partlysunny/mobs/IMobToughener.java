package me.partlysunny.mobs;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Mob;

public interface IMobToughener {

    String id();

    void toughen(Mob mob, ConfigurationSection config);

}
