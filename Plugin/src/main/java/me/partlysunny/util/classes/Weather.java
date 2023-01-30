package me.partlysunny.util.classes;

import org.bukkit.World;

public enum Weather {

    THUNDER,
    STORM,
    CLEAR;

    public static Weather of(World w) {
        if (w.isClearWeather()) return CLEAR;
        else return w.isThundering() ? THUNDER : STORM;
    }

}
