package zone.potion.listeners.redis;

import lombok.RequiredArgsConstructor;
import zone.potion.CorePlugin;
import zone.potion.redis.annotation.RedisHandler;
import zone.potion.utils.message.CC;

import java.util.Map;

@RequiredArgsConstructor
public class FreezeListener {

    private final CorePlugin plugin;

    @RedisHandler("freeze-listener")
    public void onFreeze(Map<String, Object> map) {
        String sender = (String) map.get("sender");
        String target = (String) map.get("frozen");
        String server = (String) map.get("server");
        boolean frozen = Boolean.valueOf((String) map.get("isFrozen"));
        plugin.getStaffManager().messageStaffWithPrefix(CC.PRIMARY + sender + CC.SECONDARY + " has " + (frozen ? "frozen " : "unfrozen ") + target + ".", server);
    }
}
