package zone.potion.commands.impl.staff.punish;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.commands.BaseCommand;
import zone.potion.player.CoreProfile;
import zone.potion.utils.message.CC;

import java.util.Map;

public class FreezeCommand extends BaseCommand {

    private CorePlugin plugin;

    public FreezeCommand(CorePlugin plugin) {
        super("freeze", "spike.staff");
        this.plugin = plugin;
        setUsage("/freeze <player>");
        setAliases("ice", "frz");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.RED + getUsage());
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
            CoreProfile targetProfile = plugin.getProfileManager().getProfile(target);
            if (targetProfile.hasStaff()) {
                sender.sendMessage(ChatColor.RED + "If you believe a staff member is cheating, please alert higher staff.");
                return;
            }
            String server = plugin.getServerName();
            String targetName = target.getName();
            String senderName = sender.getName();

            Map<String, Object> map = Maps.newHashMap();
            map.put("server", server);
            map.put("frozen", targetName);
            map.put("sender", senderName);

            if (targetProfile.freeze(sender)) {
                map.put("isFrozen", true);
                plugin.getRedisMessenger().send("freeze-listener", map);
                return;
            }
            map.put("isFrozen", false);
            plugin.getRedisMessenger().send("freeze-listener", map);
            return;
        }
        sender.sendMessage(CC.RED + "That player is offline or does not exist.");
    }
}
