package zone.potion.listeners.redis;

import lombok.RequiredArgsConstructor;
import zone.potion.CorePlugin;
import zone.potion.redis.annotation.RedisHandler;
import zone.potion.utils.message.CC;

import java.util.Map;

@RequiredArgsConstructor
public class StaffJoinListener {

    private final CorePlugin plugin;

    @RedisHandler("staff-join")
    public void onJoin(Map<String, Object> message) {
        plugin.getStaffManager().messageStaffWithPrefix(message.get("sender") + CC.PRIMARY + " joined the server.", (String) message.get("server"));
    }

    @RedisHandler("staff-quit")
    public void onQuit(Map<String, Object> message) {
        plugin.getStaffManager().messageStaffWithPrefix(message.get("sender") + CC.PRIMARY + " left the server.", (String) message.get("server"));
    }

}
