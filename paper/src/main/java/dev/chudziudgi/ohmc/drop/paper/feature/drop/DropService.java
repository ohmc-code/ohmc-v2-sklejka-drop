package dev.chudziudgi.ohmc.drop.paper.feature.drop;

import dev.chudziudgi.ohmc.drop.paper.feature.drop.config.DropConfig;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.config.DropData;
import dev.chudziudgi.ohmc.drop.paper.feature.drop.database.DropRepository;
import dev.chudziudgi.ohmc.drop.paper.multification.LobbyMultification;
import dev.chudziudgi.ohmc.drop.paper.utill.PlayerItemUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class DropService {

    private final DropConfig config;
    private final DropRepository repository;
    private final LobbyMultification multification;

    private final Map<UUID, Set<Material>> disabledCache = new ConcurrentHashMap<>();

    public DropService(DropConfig config, DropRepository repository, LobbyMultification multification) {
        this.config = config;
        this.repository = repository;
        this.multification = multification;
    }

    public void loadPlayer(UUID playerUuid) {
        this.repository.getDisabled(playerUuid)
                .thenAccept(disabled -> this.disabledCache.put(playerUuid, disabled));
    }

    public void unloadPlayer(UUID playerUuid) {
        this.disabledCache.remove(playerUuid);
    }

    public List<DropData> drops() {
        return this.config.drops;
    }

    public boolean isSourceBlock(Material block) {
        return this.config.blocks.contains(block);
    }

    public boolean removeNaturalDrops() {
        return this.config.removeNaturalDrops;
    }

    public boolean isEnabled(UUID playerUuid, Material dropItem) {
        Set<Material> disabled = this.disabledCache.get(playerUuid);
        return disabled == null || !disabled.contains(dropItem);
    }

    public boolean toggle(UUID playerUuid, Material dropItem) {
        Set<Material> disabled = this.disabledCache.computeIfAbsent(playerUuid, key -> EnumSet.noneOf(Material.class));

        boolean nowEnabled;
        if (disabled.contains(dropItem)) {
            disabled.remove(dropItem);
            nowEnabled = true;
        } else {
            disabled.add(dropItem);
            nowEnabled = false;
        }

        Set<Material> snapshot = disabled.isEmpty() ? EnumSet.noneOf(Material.class) : EnumSet.copyOf(disabled);
        this.repository.setDisabled(playerUuid, snapshot);

        return nowEnabled;
    }

    public void handleBreak(Player player, Material block) {
        if (!this.isSourceBlock(block)) {
            return;
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        int fortuneLevel = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.FORTUNE);
        double fortuneBonus = fortuneLevel * this.config.fortuneBonusPerLevel;

        for (DropData data : this.config.drops) {
            if (data.dropItem == null) {
                continue;
            }
            if (!this.isEnabled(player.getUniqueId(), data.dropItem)) {
                continue;
            }

            double chance = Math.min(100.0, data.chance + fortuneBonus);
            if (random.nextDouble(100.0) >= chance) {
                continue;
            }

            int amount = randomBetween(random, data.minAmount, data.maxAmount);
            if (amount > 0) {
                PlayerItemUtil.giveItem(player, new ItemStack(data.dropItem, amount));

                this.multification.create()
                        .viewer(player)
                        .notice(messages -> messages.dropReceived)
                        .placeholder("{ITEM}", data.displayName())
                        .placeholder("{AMOUNT}", String.valueOf(amount))
                        .send();
            }

            int exp = randomBetween(random, data.minExp, data.maxExp);
            if (exp > 0) {
                player.giveExp(exp);
            }
        }
    }

    private static int randomBetween(ThreadLocalRandom random, int first, int second) {
        int min = Math.min(first, second);
        int max = Math.max(first, second);
        if (min >= max) {
            return min;
        }
        return random.nextInt(min, max + 1);
    }
}
