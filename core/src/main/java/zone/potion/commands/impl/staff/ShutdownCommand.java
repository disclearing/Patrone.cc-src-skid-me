package zone.potion.commands.impl.staff;

import org.bukkit.command.CommandSender;
import zone.potion.CorePlugin;
import zone.potion.commands.BaseCommand;
import zone.potion.event.server.ServerShutdownCancelEvent;
import zone.potion.event.server.ServerShutdownScheduleEvent;
import zone.potion.task.ShutdownTask;
import zone.potion.utils.NumberUtil;
import zone.potion.utils.message.CC;

public class ShutdownCommand extends BaseCommand {
    private final CorePlugin plugin;

    public ShutdownCommand(CorePlugin plugin) {
        super("shutdown", "spike.super");
        this.plugin = plugin;
        setUsage(CC.RED + "Usage: /shutdown <seconds|cancel>");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(usageMessage);
            return;
        }

        String arg = args[0];

        if (arg.equals("cancel")) {
            ShutdownTask task = plugin.getServerSettings().getShutdownTask();

            if (task == null) {
                sender.sendMessage(CC.RED + "There is no shutdown in progress.");
            } else {
                plugin.getServer().getPluginManager().callEvent(new ServerShutdownCancelEvent());

                task.cancel();
                plugin.getServerSettings().setShutdownTask(null);
                plugin.getServer().broadcastMessage(CC.GREEN + "The shutdown in progress has been cancelled by " + sender.getName() + ".");
            }
            return;
        }

        Integer seconds = NumberUtil.getInteger(arg);

        if (seconds == null) {
            sender.sendMessage(usageMessage);
        } else {
            if (seconds >= 5 && seconds <= 300) {
                plugin.getServer().getPluginManager().callEvent(new ServerShutdownScheduleEvent());

                ShutdownTask task = new ShutdownTask(plugin, seconds);

                plugin.getServerSettings().setShutdownTask(task);
                task.runTaskTimer(plugin, 0L, 20L);
            } else {
                sender.sendMessage(CC.RED + "Please enter a time between 5 and 300 seconds.");
            }
        }
    }
}
