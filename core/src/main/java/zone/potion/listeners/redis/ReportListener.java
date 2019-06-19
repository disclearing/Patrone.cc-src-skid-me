package zone.potion.listeners.redis;

import lombok.RequiredArgsConstructor;
import zone.potion.CorePlugin;
import zone.potion.redis.annotation.RedisHandler;
import zone.potion.utils.message.CC;

import java.util.Map;

@RequiredArgsConstructor
public class ReportListener {

    private final CorePlugin plugin;

    @RedisHandler("report")
    public void onReport(Map<String, Object> map) {
        String player = (String) map.get("reporter");
        String target = (String) map.get("reported");
        String report = (String) map.get("reason");
        String server = (String) map.get("server");
        plugin.getStaffManager().messageStaff("");
        plugin.getStaffManager().messageStaff(CC.RED + "(Report) " + CC.D_AQUA + "[" + server + "] " + CC.SECONDARY + player + CC.PRIMARY
                + " reported " + CC.SECONDARY + target + CC.PRIMARY + " for " + CC.SECONDARY + report + CC.PRIMARY + ".");
        plugin.getStaffManager().messageStaff("");
    }

}
