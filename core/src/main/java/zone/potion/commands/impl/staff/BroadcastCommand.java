package zone.potion.commands.impl.staff;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import zone.potion.CorePlugin;
import zone.potion.commands.BaseCommand;
import zone.potion.utils.StringUtil;
import zone.potion.utils.message.CC;

public class BroadcastCommand extends BaseCommand {
    private final CorePlugin plugin;

    public BroadcastCommand(CorePlugin plugin) {
        super("broadcast", "spike.admin");
        this.plugin = plugin;
        setAliases("bc");
        setUsage(CC.RED + "Usage: /broadcast <message> [-god]");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(usageMessage);
            return;
        }

        String message = CC.D_RED + "[Alert] " + CC.WHITE
                + ChatColor.translateAlternateColorCodes('&', StringUtil.buildString(args, 0)).trim();

        if (message.endsWith(" -god")) {
            message = message.substring(12, message.length() - 5).trim();
        }

        plugin.getServer().broadcastMessage(message);
    }
}
