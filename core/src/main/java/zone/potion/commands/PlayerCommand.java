package zone.potion.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zone.potion.utils.message.CC;

public abstract class PlayerCommand extends BaseCommand {
    protected PlayerCommand(String name, String permission) {
        super(name, permission);
    }

    protected PlayerCommand(String name) {
        super(name, "spike.player");
    }

    @Override
    protected final void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            execute((Player) sender, args);
        } else {
            sender.sendMessage(CC.RED + "Only players can perform this command.");
        }
    }

    public abstract void execute(Player player, String[] args);
}
