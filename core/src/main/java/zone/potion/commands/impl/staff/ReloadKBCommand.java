package zone.potion.commands.impl.staff;

import org.bukkit.command.CommandSender;
import zone.potion.CorePlugin;
import zone.potion.commands.BaseCommand;
import zone.potion.toothless.ToothlessConfig;
import zone.potion.utils.message.CC;

public class ReloadKBCommand extends BaseCommand {
    private final CorePlugin plugin;

    public ReloadKBCommand(CorePlugin plugin) {
        super("reloadkb", "spike.super");
        this.plugin = plugin;
        setUsage(CC.RED + "Usage: /reloadkb");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        ToothlessConfig.reload();
        sender.sendMessage(CC.GREEN + "Reloaded knockback settings!");
    }
}
