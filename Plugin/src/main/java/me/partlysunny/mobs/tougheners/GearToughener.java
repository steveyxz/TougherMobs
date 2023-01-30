package me.partlysunny.mobs.tougheners;

import me.partlysunny.mobs.IMobToughener;
import me.partlysunny.util.Util;
import me.partlysunny.util.classes.gear.MobGear;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;

public class GearToughener implements IMobToughener {
    @Override
    public String id() {
        return "GEAR";
    }

    @Override
    public void toughen(Mob mob, ConfigurationSection config) {
        ItemStack mainHand = Util.loadItemFromConfig(Util.getOrDefault(config, "mainHand", null));
        ItemStack offHand = Util.loadItemFromConfig(Util.getOrDefault(config, "offHand", null));
        ItemStack helm = Util.loadItemFromConfig(Util.getOrDefault(config, "helmet", null));
        ItemStack chestplate = Util.loadItemFromConfig(Util.getOrDefault(config, "chestplate", null));
        ItemStack leggings = Util.loadItemFromConfig(Util.getOrDefault(config, "leggings", null));
        ItemStack boots = Util.loadItemFromConfig(Util.getOrDefault(config, "boots", null));
        MobGear.Builder.builder().setMainHand(mainHand).setOffHand(offHand).setArmor(boots, leggings, chestplate, helm).build().equip(mob);
    }
}
