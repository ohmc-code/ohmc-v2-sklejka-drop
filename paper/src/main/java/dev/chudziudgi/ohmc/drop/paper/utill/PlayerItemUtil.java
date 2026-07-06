package dev.chudziudgi.ohmc.drop.paper.utill;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public final class PlayerItemUtil {

    private PlayerItemUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean giveItem(Player player, ItemStack... itemStacks) {
        boolean droppedAny = false;

        for (ItemStack itemStack : itemStacks) {
            Map<Integer, ItemStack> notStored = player.getInventory().addItem(itemStack);

            if (!notStored.isEmpty()) {
                for (ItemStack excess : notStored.values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), excess);
                    droppedAny = true;
                }
            }
        }

        return droppedAny;
    }

    public static boolean removeItem(Player player, ItemStack... itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            if (!player.getInventory().containsAtLeast(itemStack, itemStack.getAmount())) {
                return false;
            }
        }

        for (ItemStack itemStack : itemStacks) {
            player.getInventory().removeItem(itemStack);
        }

        return true;
    }
}
