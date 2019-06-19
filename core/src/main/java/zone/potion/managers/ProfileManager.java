package zone.potion.managers;

import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.player.CoreProfile;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager {
    private final Map<UUID, CoreProfile> profiles = new HashMap<>();

    public CoreProfile createProfile(String name, UUID id, String address) {
        CoreProfile profile = new CoreProfile(name, id, address);
        profiles.put(id, profile);
        return profile;
    }

    public CoreProfile getProfile(Player player) {
        return (profiles.containsKey(player.getUniqueId()) ? profiles.get(player.getUniqueId()) : createProfile(player.getDisplayName(), player.getUniqueId(), player.getAddress().getAddress().getHostAddress()));
    }

    public void removeProfile(UUID id) {
        profiles.remove(id);
    }

    public void saveProfiles() {
        for (CoreProfile profile : profiles.values()) {
            profile.save(false);
        }
    }
}
