package zone.potion.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import zone.potion.CorePlugin;
import zone.potion.inventory.menu.Menu;
import zone.potion.inventory.menu.action.Action;
import zone.potion.player.CoreProfile;

@RequiredArgsConstructor
public class InventoryListener implements Listener {
    private final CorePlugin plugin;

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (player.getGameMode() != GameMode.SURVIVAL || event.getClickedInventory() == null
                || event.getClickedInventory() == player.getInventory() || event.getCurrentItem() == null
                || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        Menu menu = plugin.getMenuManager().getMatchingMenu(event.getClickedInventory());

        if (menu != null) {
            Action action = menu.getAction(event.getSlot());

            if (action != null) {
                event.setCancelled(true);
                action.onClick(player);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        CoreProfile profile = plugin.getProfileManager().getProfile(player);

        if (profile != null && profile.getReportingPlayerName() != null) {
            profile.setReportingPlayerName(null);
        }
    }
}
