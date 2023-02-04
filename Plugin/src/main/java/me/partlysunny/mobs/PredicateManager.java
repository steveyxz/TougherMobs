package me.partlysunny.mobs;

import me.partlysunny.ConsoleLogger;
import me.partlysunny.TougherMobsCore;
import me.partlysunny.util.classes.predicates.CheckerPredicate;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class PredicateManager {

    public static final String PREDICATE_KEY = "predicates";
    private static final Map<String, CheckerPredicate> predicates = new HashMap<>();

    public static void register(String id, CheckerPredicate predicate) {
        predicates.put(id, predicate);
    }

    public static CheckerPredicate get(String id) {
        return predicates.get(id);
    }

    public static void unregister(String id) {
        predicates.remove(id);
    }

    public static void load() {
        ConfigurationSection predicateSection = TougherMobsCore.config().getConfigurationSection(PREDICATE_KEY);
        if (predicateSection == null) {
            ConsoleLogger.error("Section %s not found in config.yml! Plugin will not work as expected!".formatted(PREDICATE_KEY));
            return;
        }
        predicateSection.getValues(false).forEach((key, value) -> register(key, new CheckerPredicate(value.toString())));
    }

}
