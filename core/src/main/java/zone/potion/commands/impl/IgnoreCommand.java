package zone.potion.commands.impl;

import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.commands.PlayerCommand;
import zone.potion.player.CoreProfile;
import zone.potion.utils.message.CC;
import zone.potion.utils.message.Messages;

public class IgnoreCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public IgnoreCommand(CorePlugin plugin) {
        super("ignore");
        this.plugin = plugin;
        setAliases("unignore");
        setUsage(CC.RED + "Usage: /ignore <player>");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(usageMessage);
            return;
        }

        Player target = plugin.getServer().getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Messages.PLAYER_NOT_FOUND);
            return;
        }

        if (target.getName().equals(player.getName())) {
            player.sendMessage(CC.RED + "You can't ignore yourself!");
            return;
        }

        CoreProfile targetProfile = plugin.getProfileManager().getProfile(target);

        if (targetProfile.hasStaff()) {
            player.sendMessage(CC.RED + "You can't ignore a staff member. If this staff member is harrassing you " +
                    "or engaging in other abusive manners, please report this or contact a higher staff member.");
            return;
        }

        CoreProfile profile = plugin.getProfileManager().getProfile(player);

        if (profile.hasPlayerIgnored(target.getUniqueId())) {
            profile.unignore(target.getUniqueId());
            player.sendMessage(CC.GREEN + "No longer ignoring " + target.getName() + ".");
        } else {
            profile.ignore(target.getUniqueId());
            player.sendMessage(CC.GREEN + "Now ignoring " + target.getName() + ".");
        }
    }
}
