package zone.potion.managers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.player.CoreProfile;
import zone.potion.utils.message.CC;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class StaffManager {
    @Getter
    private final Set<CoreProfile> cachedStaff = new HashSet<>();
    private final CorePlugin plugin;

    public void addCachedStaff(CoreProfile profile) {
        cachedStaff.add(profile);
    }

    public boolean isInStaffCache(CoreProfile profile) {
        return cachedStaff.contains(profile);
    }

    public void removeCachedStaff(CoreProfile profile) {
        cachedStaff.remove(profile);
    }

    public void messageStaff(String displayName, String msg, String server) {
        String formattedMsg = CC.SECONDARY + "[Staff] " + ChatColor.DARK_AQUA + "[" + server + "] " + CC.SECONDARY + displayName + CC.R + ": " + msg;
        messageStaff(formattedMsg);
    }

    public void messageStaff(String msg) {
        for (CoreProfile profile : cachedStaff) {
            Player loopPlayer = plugin.getServer().getPlayer(profile.getId());

            if (loopPlayer != null && loopPlayer.isOnline()) {
                loopPlayer.sendMessage(msg);
            }
        }
        plugin.getServer().getConsoleSender().sendMessage(msg);
    }

    public void messageStaffWithPrefix(String msg, String server) {
        msg = CC.SECONDARY + "[Staff] " + CC.D_AQUA + "[" + server + "] " + CC.R + msg;

        for (CoreProfile profile : cachedStaff) {
            Player loopPlayer = plugin.getServer().getPlayer(profile.getId());

            if (loopPlayer != null && loopPlayer.isOnline()) {
                loopPlayer.sendMessage(msg);
            }
        }
        plugin.getServer().getConsoleSender().sendMessage(msg);
    }

    public void hideVanishedStaffFromPlayer(Player player) {
        if (!plugin.getProfileManager().getProfile(player).hasStaff()) {
            for (CoreProfile profile : cachedStaff) {
                if (profile.isVanished()) {
                    Player loopPlayer = plugin.getServer().getPlayer(profile.getId());

                    if (loopPlayer != null && loopPlayer.isOnline()) {
                        player.hidePlayer(loopPlayer);
                    }
                }
            }
        }
    }
}
