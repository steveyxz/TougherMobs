package me.partlysunny.mobs.tougheners;

import com.google.common.base.Preconditions;
import me.partlysunny.ConsoleLogger;
import me.partlysunny.mobs.IMobToughener;
import me.partlysunny.util.Util;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashMap;

import static me.partlysunny.mobs.tougheners.CreeperBombToughener.CreeperBombListener.specialCreepers;

public class CreeperBombToughener implements IMobToughener {
    @Override
    public String id() {
        return "BOMB";
    }

    @Override
    public void toughen(Mob mob, ConfigurationSection config) {
        if (mob instanceof Creeper creeper) {
            String explosionType = Util.getOrDefault(config, "explosionType", "default");
            Integer explosionRadius = Util.getOrDefault(config, "explosionRadius", creeper.getExplosionRadius());
            Integer maxFuseTicks = Util.getOrDefault(config, "maxFuseTicks", creeper.getMaxFuseTicks());
            creeper.setExplosionRadius(explosionRadius);
            creeper.setMaxFuseTicks(maxFuseTicks);
            if (explosionType.equals("special")) {
                Integer tntCount = Util.getOrDefault(config, "tntCount", 3);
                Double tntLaunchStrength = Util.getOrDefault(config, "tntLaunchStrength", 1.0);
                String particleType = Util.getOrDefault(config, "particleType", "");
                Integer particleCount = Util.getOrDefault(config, "particleCount", 10);
                Integer baseExplosionPower = Util.getOrDefault(config, "baseExplosionPower", 0);
                specialCreepers.put(creeper, new CreeperBombInfo(tntCount, tntLaunchStrength, Particle.valueOf(particleType), particleCount, baseExplosionPower));
            }
        } else {
            ConsoleLogger.error(id() + " can only be applied to type CREEPER");
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    private static final class CreeperBombInfo {
        private final Integer tntCount;
        private final Double tntLaunchStrength;
        private final Particle particleType;
        private final Integer particleCount;
        private final Integer baseExplosionPower;


        private CreeperBombInfo(Integer tntCount, Double tntLaunchStrength, Particle particleType, Integer particleCount, Integer baseExplosionPower) {
            this.tntCount = tntCount;
            this.tntLaunchStrength = tntLaunchStrength;
            this.particleType = particleType;
            this.particleCount = particleCount;
            this.baseExplosionPower = baseExplosionPower;
            Preconditions.checkArgument(baseExplosionPower >= 0);
            Preconditions.checkArgument(tntLaunchStrength > 0);
            Preconditions.checkArgument(particleType != null);
        }
    }

    public static final class CreeperBombListener implements Listener {

        public static final HashMap<Creeper, CreeperBombInfo> specialCreepers = new HashMap<>();

        @EventHandler
        public void onCreeperExplode(EntityExplodeEvent e) {
            if (e.getEntity() instanceof Creeper creeper && specialCreepers.containsKey(creeper)) {
                e.setCancelled(true);
                CreeperBombInfo info = specialCreepers.get(creeper);
                World world = creeper.getWorld();
                Location l = creeper.getLocation();
                //Base explosion
                if (info.baseExplosionPower > 0) {
                    //Do a base explosion of strength baseExplosionPower and emit particleCount particles of type particleType
                    world.createExplosion(l, info.baseExplosionPower);
                    world.spawnParticle(info.particleType, l, info.particleCount);
                    //Now emit explosion particles at that location
                    world.spawnParticle(Particle.EXPLOSION_LARGE, l, 1);
                }
                //TNT explosions
                for (int i = 0; i < info.tntCount; i++) {
                    //Launch a TNT entity in a random direction with a random strength
                    world.spawn(l, org.bukkit.entity.TNTPrimed.class, tnt -> tnt.setVelocity(Util.randomVector(info.tntLaunchStrength)));
                }

                //Remove the creeper from the list of special creepers
                specialCreepers.remove(creeper);

                //Remove the creeper from the world
                creeper.remove();

            }
        }

    }
}
