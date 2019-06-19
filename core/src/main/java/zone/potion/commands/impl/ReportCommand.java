package zone.potion.commands.impl;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.commands.PlayerCommand;
import zone.potion.inventory.menu.impl.ReportMenu;
import zone.potion.player.CoreProfile;
import zone.potion.utils.StringUtil;
import zone.potion.utils.message.CC;
import zone.potion.utils.message.Messages;
import zone.potion.utils.timer.Timer;

import java.util.Map;

public class ReportCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public ReportCommand(CorePlugin plugin) {
        super("report");
        this.plugin = plugin;
        setUsage(CC.RED + "Usage: /report <player> <reason>");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(usageMessage);
            return;
        }

        Player target = plugin.getServer().getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Messages.PLAYER_NOT_FOUND);
            return;
        }

        if (player == target) {
            player.sendMessage(CC.RED + "You can't report yourself!");
            return;
        }

        CoreProfile targetProfile = plugin.getProfileManager().getProfile(target);

        if (targetProfile.hasStaff()) {
            player.sendMessage(CC.RED + "You can't report a staff member. If this staff member is harassing you or" +
                    " engaging in other abusive manners, please report this or contact a higher staff member.");
            return;
        }

        CoreProfile profile = plugin.getProfileManager().getProfile(player);

        Timer cooldownTimer = profile.getReportCooldownTimer();

        if (cooldownTimer.isActive()) {
            player.sendMessage(CC.RED + "You can't report a player for another " + cooldownTimer.formattedExpiration() + ".");
            return;
        }

        String report = StringUtil.buildString(args, 1);


        Map<String, Object> reportInformation = Maps.newHashMap();
        reportInformation.put("server", plugin.getServerName());
        reportInformation.put("reporter", player.getName());
        reportInformation.put("reported", target.getName());
        reportInformation.put("reason", report);
        plugin.getRedisMessenger().send("report", reportInformation);

        player.sendMessage(CC.GREEN + "Report sent for " + target.getDisplayName() + CC.GREEN + ": " + CC.R + report);

    }
}
