package me.partlysunny.mobs.tougheners;

import me.partlysunny.mobs.IMobToughener;
import me.partlysunny.util.Util;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class LootToughener implements IMobToughener {
    @Override
    public String id() {
        return "LOOT";
    }

    @Override
    public void toughen(Mob mob, ConfigurationSection config) {
        //Increase loot drops from the mob
        //Start by processing the loot info in config
        //Then change the drops of the mob to match the new loot
        ConfigurationSection loot = Util.getOrError(config, "loot");
        LootTable table = LootTable.fromConfig(loot);
        LootListener.mobLoot.put(mob, table);
    }

    public static final class LootListener implements Listener {

        public static final HashMap<Mob, LootTable> mobLoot = new HashMap<>();

        @EventHandler
        public void onMobDeath(EntityDeathEvent e) {
            if (e.getEntity() instanceof Mob m) {
                //Get the mob's loot table
                LootTable loot = mobLoot.get(m);
                if (loot == null) return;
                //Drop the loot
                loot.drop(m.getLocation());
            }
        }

    }

    @SuppressWarnings("ClassCanBeRecord")
    private static final class LootTable {
        private final LootEntry[] entries;

        public LootTable(LootEntry[] entries) {
            this.entries = entries;
        }

        public static LootTable fromConfig(ConfigurationSection config) {
            LootEntry[] entries = new LootEntry[config.getKeys(false).size()];
            int i = 0;
            for (String key : config.getKeys(false)) {
                ConfigurationSection entryDetails = Util.getOrError(config, key);
                ItemStack item = Util.loadItemFromConfig(Util.getOrError(entryDetails, "item"));
                int maxAmount = Util.getOrError(entryDetails, "maxAmount");
                int minAmount = Util.getOrError(entryDetails, "minAmount");
                double chance = Util.getOrError(entryDetails, "chance");
                entries[i] = new LootEntry(item, maxAmount, minAmount, chance);
                i++;
            }
            return new LootTable(entries);
        }

        public void drop(Location l) {
            for (LootEntry entry : entries) {
                if (Math.random() < entry.chance()) {
                    int amount = (int) (Math.random() * (entry.maxAmount() - entry.minAmount() + 1)) + entry.minAmount();
                    ItemStack item = entry.item().clone();
                    item.setAmount(amount);
                    assert l.getWorld() != null;
                    l.getWorld().dropItemNaturally(l, item);
                }
            }
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    private static final class LootEntry {
        private final ItemStack item;
        private final int maxAmount;
        private final int minAmount;
        private final double chance;

        public LootEntry(ItemStack item, int maxAmount, int minAmount, double chance) {
            this.item = item;
            this.maxAmount = maxAmount;
            this.minAmount = minAmount;
            this.chance = chance;
        }

        public ItemStack item() {
            return item;
        }

        public int maxAmount() {
            return maxAmount;
        }

        public int minAmount() {
            return minAmount;
        }

        public double chance() {
            return chance;
        }
    }


}
