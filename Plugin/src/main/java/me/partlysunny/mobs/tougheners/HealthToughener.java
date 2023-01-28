package me.partlysunny.mobs.tougheners;

import me.partlysunny.ConsoleLogger;
import me.partlysunny.mobs.IMobToughener;
import me.partlysunny.util.Util;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Mob;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class HealthToughener implements IMobToughener {
    @Override
    public String id() {
        return "HEALTH";
    }

    @Override
    public void toughen(Mob mob, ConfigurationSection config) {
        Optional<ConfigurationSection> healthSection = Util.getOptional(config, id(), ConfigurationSection.class);
        AttributeInstance healthAttribute = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute != null && healthSection.isPresent()) {
            ConfigurationSection healthConfig = healthSection.get();
            AttributeModifier.Operation operation;
            try {
                operation = AttributeModifier.Operation.valueOf(Util.getOrDefault(healthConfig, "operation", "MULTIPLY_SCALAR_1", String.class));
            } catch (IllegalArgumentException e) {
                ConsoleLogger.error("Configuration for %s on %s was invalid! Operation must be one of the following: %s".formatted(id(), mob.getType(), Arrays.stream(AttributeModifier.Operation.values()).map(Enum::toString).collect(Collectors.toList())));
                return;
            }
            Double value = Util.getOrDefault(healthConfig, "value", 0.0d, Double.class);
            healthAttribute.addModifier(new AttributeModifier(id() + "_TOUGHEN", value, operation));
        }

    }
}
