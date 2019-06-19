package zone.potion.listeners.redis;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.player.CoreProfile;
import zone.potion.redis.annotation.RedisHandler;
import zone.potion.utils.message.CC;

import java.util.Map;

@RequiredArgsConstructor
public class DispatchCommandListener {

    private final CorePlugin plugin;

    @RedisHandler("dispatch-command")
    public void onDispatch(Map<String, Object> map) {
        String command = (String) map.get("command");
        String server = (String) map.get("server");
        String sender = (String) map.get("sender");
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                CoreProfile profile = plugin.getProfileManager().getProfile(player);
                if (profile.hasStaff())
                    player.sendMessage(CC.D_AQUA + "[" + server + "] " + CC.RED + sender + CC.SECONDARY + " dispatched command " + CC.PRIMARY + "\"/" + command + "\"");
            }
            Bukkit.getConsoleSender().sendMessage(CC.D_AQUA + "[" + server + "] " + CC.RED + sender + CC.SECONDARY + " dispatched command " + CC.PRIMARY + "\"/" + command + "\"");
        });
    }

}
