package zone.potion.listeners.redis;

import lombok.RequiredArgsConstructor;
import zone.potion.CorePlugin;
import zone.potion.redis.annotation.RedisHandler;
import zone.potion.utils.message.CC;

import java.util.Map;

@RequiredArgsConstructor
public class StaffChatListener {

    private final CorePlugin plugin;

    @RedisHandler("staff-chat")
    public void onStaffChatMessage(Map<String, Object> message) {
        String sender = (String) message.get("sender");
        if (sender.equalsIgnoreCase("CONSOLE")) {
            plugin.getStaffManager().messageStaff(CC.D_RED + sender, (String) message.get("message"), "GLOBAL");
            return;
        }
        plugin.getStaffManager().messageStaff((String) message.get("format"), (String) message.get("message"), (String) message.get("server"));
    }

}
