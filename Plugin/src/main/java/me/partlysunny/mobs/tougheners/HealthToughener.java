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
        AttributeInstance healthAttribute = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute != null) {
            AttributeModifier.Operation operation;
            try {
                operation = AttributeModifier.Operation.valueOf(Util.getOrDefault(config, "operation", "MULTIPLY_SCALAR_1"));
            } catch (IllegalArgumentException e) {
                ConsoleLogger.error("Configuration for %s on %s was invalid! Operation must be one of the following: %s".formatted(id(), mob.getType(), Arrays.stream(AttributeModifier.Operation.values()).map(Enum::toString).collect(Collectors.toList())));
                return;
            }
            Double value = Util.getOrDefault(config, "value", 0.0);
            healthAttribute.addModifier(new AttributeModifier(id() + "_TOUGHEN", value, operation));
            mob.setHealth(healthAttribute.getValue());
        }
    }
}
