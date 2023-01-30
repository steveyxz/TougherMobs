package me.partlysunny.mobs.tougheners;

import me.partlysunny.mobs.IMobToughener;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Mob;

public class GearToughener implements IMobToughener {
    @Override
    public String id() {
        return "GEAR";
    }

    @Override
    public void toughen(Mob mob, ConfigurationSection config) {
        //
    }
}
