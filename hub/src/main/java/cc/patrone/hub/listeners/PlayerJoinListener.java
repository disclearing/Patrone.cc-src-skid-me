package cc.patrone.hub.listeners;

import cc.patrone.hub.Hub;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerJoinListener implements Listener {

    private Hub plugin;

    public PlayerJoinListener(Hub plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().clear();
        event.getPlayer().teleport(new Location(plugin.getServer().getWorld("world"), 0, 1, 0, 0, 0));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(!event.getTo().equals(event.getFrom())) {
            player.teleport(event.getFrom());
        }
    }

}
