package zone.potion.task;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import zone.potion.CorePlugin;
import zone.potion.player.CoreProfile;

@RequiredArgsConstructor
public class AFKTask extends BukkitRunnable {

    private final CorePlugin plugin;

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            CoreProfile profile = plugin.getProfileManager().getProfile(player);

            if (profile.hasStaff()) continue;

            int x = player.getLocation().getBlockX();
            int y = player.getLocation().getBlockY();
            int z = player.getLocation().getBlockZ();

            if (profile.getLastLocationX() == x && profile.getLastLocationY() == y && profile.getLastLocationZ() == z) {
                profile.setAfkViolations(profile.getAfkViolations() + 1);
                if (profile.getAfkViolations() >= 5) {
                    player.kickPlayer(ChatColor.RED + "You have been kicked for being AFK for too long.");
                }
            } else {
                profile.setAfkViolations(0);
                profile.setLastLocation(new int[]{x, y, z});
            }
        }
    }
}
