package zone.potion.commands.impl.staff;

import org.bukkit.command.CommandSender;
import zone.potion.CorePlugin;
import zone.potion.commands.BaseCommand;
import zone.potion.utils.message.CC;

public class SlowChatCommand extends BaseCommand {
    private final CorePlugin plugin;

    public SlowChatCommand(CorePlugin plugin) {
        super("slowchat", "spike.staff");
        this.plugin = plugin;
        setUsage(CC.RED + "Usage: /slowchat <seconds|disable>");
    }

    private static Integer getInt(String arg) {
        try {
            int i = Integer.parseInt(arg);

            if (i < 4 || i > 60) {
                return null;
            }

            return i;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(usageMessage);
            return;
        }

        String arg = args[0];

        switch (arg.toLowerCase()) {
            case "off":
            case "toggle":
            case "disable":
                int slowChatTime = plugin.getServerSettings().getSlowChatTime();

                if (slowChatTime == -1) {
                    sender.sendMessage(CC.RED + "Slow chat is already disabled!");
                } else {
                    plugin.getServerSettings().setSlowChatTime(-1);
                    plugin.getServer().broadcastMessage(CC.RED + "Slow chat has been disabled by " + sender.getName() + ".");
                }
                break;
            default:
                Integer time = getInt(arg);

                if (time == null) {
                    sender.sendMessage(CC.RED + "You must enter a valid time between 4 and 60 seconds.");
                } else {
                    plugin.getServerSettings().setSlowChatTime(time);
                    plugin.getServer().broadcastMessage(CC.YELLOW + "Slow chat has been enabled and set to " + time
                            + " seconds by " + sender.getName() + ".");
                }
                break;
        }
    }
}
