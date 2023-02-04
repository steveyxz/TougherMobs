package me.partlysunny.mobs.tougheners;

import me.partlysunny.mobs.IMobToughener;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

public class NameToughener implements IMobToughener {

    @Override
    public String id() {
        return "NAME";
    }

    @Override
    public void toughen(Mob mob, ConfigurationSection config) {
        if (config.isString("name")) {
            mob.setCustomName(config.getString("name"));
            mob.setCustomNameVisible(true);
        }
    }
}
