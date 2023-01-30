package me.partlysunny.mobs;

import me.partlysunny.ConsoleLogger;
import me.partlysunny.TougherMobsCore;
import me.partlysunny.mobs.tougheners.DamageToughener;
import me.partlysunny.mobs.tougheners.HealthToughener;
import me.partlysunny.mobs.tougheners.SpeedToughener;
import me.partlysunny.util.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;

import java.util.concurrent.atomic.AtomicInteger;

public enum Toughener {

    SPEED(new SpeedToughener()),
    DAMAGE(new DamageToughener()),
    HEALTH(new HealthToughener());

    public static final String TOUGHENER_KEY = "tougheners";

    private final IMobToughener toughener;

    Toughener(IMobToughener toughener) {
        this.toughener = toughener;
    }

    public static void toughen(Entity e) {
        ConfigurationSection toughenerSection = TougherMobsCore.config().getConfigurationSection(TOUGHENER_KEY);
        if (toughenerSection == null) {
            ConsoleLogger.error("config.yml does not contain \"" + TOUGHENER_KEY + "\" key! Delete config and reload or fix the config.yml");
            return;
        }
        ConfigurationSection entitySpawnConfig = toughenerSection.getConfigurationSection(e.getType().toString());
        //This means this entity has no tougheners in the config!
        if (entitySpawnConfig == null) return;
        //Must be a mob
        if (e instanceof Mob m) {
            for (Toughener toughener : values()) {
                ConfigurationSection toughenerConfig = null;
                int currentPriority = Integer.MAX_VALUE;
                for (String key : entitySpawnConfig.getKeys(false)) {
                    ConfigurationSection config = (ConfigurationSection) entitySpawnConfig.get(key);
                    assert config != null;
                    Integer priority = Util.getOrDefault(config, "priority", Integer.MAX_VALUE);
                    String type = Util.getOrError(config, "type");
                    if (type.equals(toughener.toughener.id()) && priority <= currentPriority) {
                        currentPriority = priority;
                        toughenerConfig = config;
                    }
                }
                toughener.toughener.toughen(m, toughenerConfig);
            }
        }
    }
}
