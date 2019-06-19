package zone.potion.commands.impl;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.commands.PlayerCommand;
import zone.potion.player.CoreProfile;
import zone.potion.utils.StringUtil;
import zone.potion.utils.message.CC;
import zone.potion.utils.timer.Timer;

import java.util.Map;

public class HelpOpCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public HelpOpCommand(CorePlugin plugin) {
        super("helpop");
        this.plugin = plugin;
        setUsage(CC.RED + "/helpop <help message>");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(usageMessage);
            return;
        }

        CoreProfile profile = plugin.getProfileManager().getProfile(player);
        Timer cooldownTimer = profile.getReportCooldownTimer();

        if (cooldownTimer.isActive()) {
            player.sendMessage(CC.RED + "You can't request assistance for another " + cooldownTimer.formattedExpiration() + ".");
            return;
        }

        String request = StringUtil.buildString(args, 0);

        Map<String, Object> requestMap = Maps.newHashMap();
        requestMap.put("server", plugin.getServerName());
        requestMap.put("player", player.getName());
        requestMap.put("request", request);

        plugin.getRedisMessenger().send("help-op", requestMap);
        player.sendMessage(CC.GREEN + "Request sent: " + CC.R + request);
    }
}
