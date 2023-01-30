package me.partlysunny.mobs;

import me.partlysunny.ConsoleLogger;
import me.partlysunny.TougherMobsCore;
import me.partlysunny.mobs.tougheners.DamageToughener;
import me.partlysunny.mobs.tougheners.HealthToughener;
import me.partlysunny.mobs.tougheners.SpeedToughener;
import me.partlysunny.util.Util;
import me.partlysunny.util.classes.Weather;
import me.partlysunny.util.classes.builders.HashMapBuilder;
import me.partlysunny.util.classes.predicates.CheckerPredicate;
import me.partlysunny.util.classes.predicates.PredicateContext;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;

import java.util.Optional;

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
                    Optional<String> predicate = Util.getOptional(entitySpawnConfig, "predicate");
                    if (predicate.isPresent()) {
                        CheckerPredicate ref = PredicateManager.get(predicate.get());
                        if (!ref.process(getWorldContext(m))) {
                            continue;
                        }
                    }
                    Integer priority = Util.getOrDefault(config, "priority", Integer.MAX_VALUE);
                    String type = Util.getOrError(config, "type");
                    if (type.equals(toughener.toughener.id()) && priority <= currentPriority) {
                        currentPriority = priority;
                        toughenerConfig = config;
                    }
                }
                if (toughenerConfig != null) toughener.toughener.toughen(m, toughenerConfig);
            }
        }
    }

    private static PredicateContext getWorldContext(Mob m) {
        World w = m.getWorld();
        Location location = m.getLocation();
        return new PredicateContext(
                new HashMapBuilder<String, String>()
                        .put("time", String.valueOf(w.getTime()))
                        .put("weather", String.valueOf(Weather.of(w)))
                        .put("locX", String.valueOf(location.getX()))
                        .put("locY", String.valueOf(location.getY()))
                        .put("locZ", String.valueOf(location.getZ()))
                        .put("light", String.valueOf(w.getBlockAt(location).getLightLevel()))
                        .put("skyLight", String.valueOf(w.getBlockAt(location).getLightFromSky()))
                        .put("blockLight", String.valueOf(w.getBlockAt(location).getLightFromBlocks()))
                        .build()
        );
    }
}
