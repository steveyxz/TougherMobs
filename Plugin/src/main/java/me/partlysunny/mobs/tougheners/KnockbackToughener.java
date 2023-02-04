package me.partlysunny.mobs.tougheners;

import me.partlysunny.TougherMobsCore;
import me.partlysunny.mobs.IMobToughener;
import me.partlysunny.util.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class KnockbackToughener implements IMobToughener {
    @Override
    public String id() {
        return "KNOCKBACK";
    }

    @Override
    public void toughen(Mob mob, ConfigurationSection config) {
        //Set knockback the mob deals to players
        mob.setMetadata("knockback", new FixedMetadataValue(TougherMobsCore.instance(), Util.getOrError(config, "knockback")));
        mob.setMetadata("lift", new FixedMetadataValue(TougherMobsCore.instance(), Util.getOrDefault(config, "lift", 0.0f)));
    }

    public static final class KnockbackHandler implements Listener {

        @EventHandler
        public void onPlayerHit(EntityDamageByEntityEvent event) {
            if (event.getDamager() instanceof Mob mob) {
                if (mob.hasMetadata("knockback")) {
                    double knockback = mob.getMetadata("knockback").get(0).asDouble();
                    double lift = mob.getMetadata("lift").get(0).asDouble();
                    Entity victim = event.getEntity();
                    victim.getVelocity().add(new Vector(0, lift, 0));
                    //Calculate knockback in relation to mob's location
                    Vector knockbackVector = victim.getLocation().toVector().subtract(mob.getLocation().toVector()).normalize().multiply(knockback);
                    victim.getVelocity().add(knockbackVector);
                }
            }
        }

    }


}
