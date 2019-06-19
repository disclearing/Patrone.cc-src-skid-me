package zone.potion.utils.player;

import lombok.experimental.UtilityClass;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;

import java.util.function.Consumer;

@UtilityClass
public class PlayerUtil {
    public static void clearPlayer(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.setHealth(player.getMaxHealth());
        player.setMaximumNoDamageTicks(20);
        player.setFallDistance(0.0F);
        player.setFoodLevel(20);
        player.setSaturation(5.0F);
        player.setFireTicks(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setItemOnCursor(null);
        player.updateInventory();
    }

    private static void runPlayerAction(Player player, Consumer<Player> playerConsumer) {
        if (player != null && player.isOnline()) {
            playerConsumer.accept(player);
        }
    }
}
