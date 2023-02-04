package me.partlysunny.mobs.tougheners;

import me.partlysunny.mobs.IMobToughener;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Mob;

public class NameToughener implements IMobToughener {

    @Override
    public String id() {
        return "NAME";
    }

    @Override
    public void toughen(Mob mob, ConfigurationSection config) {
        if (config.isString("value")) {
            mob.setCustomName(config.getString("value"));
            mob.setCustomNameVisible(true);
        }
    }
}
