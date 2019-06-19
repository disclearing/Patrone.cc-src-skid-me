package cc.patrone.hub.task;

import zone.potion.inventory.menu.Menu;
import cc.patrone.hub.Hub;
import cc.patrone.hub.menu.ServerMenu;

public class InventoryTask implements Runnable {

    private Hub plugin;
    private Menu serverMenu;

    public InventoryTask(Hub plugin) {
        this.plugin = plugin;
        this.serverMenu = plugin.getCore().getMenuManager().getMenu(ServerMenu.class);
    }

    @Override
    public void run() {
        plugin.getServer().getOnlinePlayers().stream()
                .filter(player -> !player.getOpenInventory().getTitle().equals(serverMenu.getInventory().getTitle()))
                .forEach(player -> serverMenu.open(player));
    }
}
