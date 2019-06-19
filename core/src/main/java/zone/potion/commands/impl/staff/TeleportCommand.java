package zone.potion.commands.impl.staff;

import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.commands.PlayerCommand;
import zone.potion.player.CoreProfile;
import zone.potion.utils.message.CC;
import zone.potion.utils.message.Messages;

public class TeleportCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public TeleportCommand(CorePlugin plugin) {
        super("tp", "spike.staff");
        this.plugin = plugin;
        setAliases("teleport");
        setUsage(CC.RED + "Usage: /teleport <player> [player]");
    }

    private static boolean isOffline(Player checker, Player target) {
        if (target == null) {
            checker.sendMessage(Messages.PLAYER_NOT_FOUND);
            return true;
        }

        return false;
    }

    private void teleport(Player to, Player from) {
        to.teleport(from);
        to.sendMessage(CC.GREEN + "You have been teleported to " + from.getName() + ".");

        CoreProfile fromProfile = plugin.getProfileManager().getProfile(from);

        if (fromProfile.hasStaff()) {
            from.sendMessage(CC.GREEN + to.getName() + " has been teleported to you.");
        }
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(usageMessage);
            return;
        }

        Player target = plugin.getServer().getPlayer(args[0]);

        if (isOffline(player, target)) {
            return;
        }

        if (args.length < 2) {
            teleport(player, target);
        } else {
            Player target2 = plugin.getServer().getPlayer(args[1]);

            if (isOffline(player, target2)) {
                return;
            }

            teleport(target, target2);

            player.sendMessage(CC.GREEN + "Teleported " + target.getName() + " to " + target2.getName() + ".");
        }
    }
}
