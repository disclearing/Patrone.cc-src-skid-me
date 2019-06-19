package zone.potion.managers;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import zone.potion.CorePlugin;

import java.net.InetAddress;
import java.util.*;

public class PlayerManager {
    private final Map<InetAddress, Integer> onlinePerIp = new HashMap<>();
    private final List<String> onlineNames = new ArrayList<>();

    private final Map<UUID, BukkitTask> announcementTasks = new HashMap<>();

    public void addPlayer(Player player) {
        onlineNames.add(player.getName());

        InetAddress address = player.getAddress().getAddress();
        int count = onlinePerIp.getOrDefault(address, 0) + 1;

        onlinePerIp.put(address, count);
    }

    public void removePlayer(Player player) {
        onlineNames.remove(player.getName());

        InetAddress address = player.getAddress().getAddress();
        int count = onlinePerIp.getOrDefault(address, 0) - 1;

        if (count == 0) {
            onlinePerIp.remove(address);
        } else {
            onlinePerIp.put(address, count);
        }

        BukkitTask announcementTask = announcementTasks.get(player.getUniqueId());
        if (announcementTask != null) {
            announcementTask.cancel();
        }
        announcementTasks.remove(player.getUniqueId());
    }

    public int getOnlineByIp(InetAddress address) {
        return onlinePerIp.getOrDefault(address, 0);
    }

    public boolean isNameOnline(String name) {
        return onlineNames.contains(name);
    }
}
