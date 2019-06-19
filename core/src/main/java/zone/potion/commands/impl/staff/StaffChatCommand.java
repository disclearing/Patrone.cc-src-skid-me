package zone.potion.commands.impl.staff;

import com.google.common.collect.Maps;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.commands.BaseCommand;
import zone.potion.player.CoreProfile;
import zone.potion.utils.StringUtil;
import zone.potion.utils.message.CC;

import java.util.Map;

public class StaffChatCommand extends BaseCommand {
    private final CorePlugin plugin;

    public StaffChatCommand(CorePlugin plugin) {
        super("staffchat", "spike.staff");
        this.plugin = plugin;
        setAliases("sc");
        setUsage("/staffchat [message]");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 0) {
            if (sender instanceof Player) {
                CoreProfile profile = plugin.getProfileManager().getProfile(((Player) sender));
                boolean inStaffChat = !profile.isInStaffChat();

                profile.setInStaffChat(inStaffChat);

                sender.sendMessage(inStaffChat ? CC.GREEN + "You are now in staff chat." : CC.RED + "You are no longer in staff chat.");
                return;
            }
            sender.sendMessage(CC.RED + getUsage());
            return;
        } else {
            String message = StringUtil.buildString(args, 0);

            Map<String, Object> redisMessage = Maps.newHashMap();
            redisMessage.put("message", message);
            redisMessage.put("sender", sender.getName());

            if (sender instanceof Player) {
                CoreProfile profile = plugin.getProfileManager().getProfile(((Player) sender));
                redisMessage.put("format", profile.getPrimaryGroupPrefix() + sender.getName());
            }

            redisMessage.put("server", plugin.getServerName());

            plugin.getRedisMessenger().send("staff-chat", redisMessage);
            //plugin.getStaffManager().messageStaff(profile.getChatFormat(), message, plugin.getServerName());
        }
    }
}
