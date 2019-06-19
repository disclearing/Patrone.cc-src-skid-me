package zone.potion.listeners.redis;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import zone.potion.CorePlugin;
import zone.potion.player.CoreProfile;
import zone.potion.redis.annotation.RedisHandler;
import zone.potion.utils.message.CC;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class ChatListener {

    private final CorePlugin plugin;

    @RedisHandler("cross-chat")
    public void onChat(Map<String, Object> messageMap) {
        String format = (String) messageMap.get("format");
        String message = (String) messageMap.get("message");
        String region = (String) messageMap.get("region");
        UUID senderUUID = UUID.fromString((String) messageMap.get("uuid"));
        if (plugin.getConfig().getString("region").equalsIgnoreCase(region)) return;

        plugin.getServer().getOnlinePlayers().stream()
                .map(player -> plugin.getProfileManager().getProfile(player))
                .filter(player -> !player.hasPlayerIgnored(senderUUID))
                .filter(CoreProfile::isCrossChat)
                .filter(CoreProfile::isGlobalChatEnabled)
                .map(profile -> Bukkit.getPlayer(profile.getId()))
                .forEach(player -> player.sendMessage(CC.GRAY + "[" + region.toUpperCase() + "] " + CC.R + format + CC.R + ": " + message));
        //Bukkit.broadcastMessage(CC.GRAY + "[" + region.toUpperCase() + "] " + CC.R + format + CC.R + ": " + message);
    }

}
