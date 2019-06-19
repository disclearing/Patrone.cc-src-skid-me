package zone.potion.listeners.redis;

import lombok.RequiredArgsConstructor;
import zone.potion.CorePlugin;
import zone.potion.redis.annotation.RedisHandler;
import zone.potion.utils.message.CC;

import java.util.Map;

@RequiredArgsConstructor
public class HelpopListener {

    private final CorePlugin plugin;

    @RedisHandler("help-op")
    public void onHelpOp(Map<String, Object> map) {
        String server = (String) map.get("server");
        String player = (String) map.get("player");
        String request = (String) map.get("request");
        plugin.getStaffManager().messageStaff(CC.RED + "\n(HelpOp) " + CC.D_AQUA + "[" + server + "] " + CC.SECONDARY + player
                + CC.PRIMARY + " requested assistance: " + CC.SECONDARY + request + CC.PRIMARY + ".\n ");
    }

}
