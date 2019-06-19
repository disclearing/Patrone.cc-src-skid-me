package zone.potion.commands.impl;

import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.commands.PlayerCommand;
import zone.potion.event.player.PlayerMessageEvent;
import zone.potion.player.CoreProfile;
import zone.potion.utils.StringUtil;
import zone.potion.utils.message.CC;

public class ReplyCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public ReplyCommand(CorePlugin plugin) {
        super("reply");
        this.plugin = plugin;
        setAliases("r");
        setUsage(CC.RED + "Usage: /reply <message>");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(usageMessage);
            return;
        }

        CoreProfile profile = plugin.getProfileManager().getProfile(player);

        Player target = plugin.getServer().getPlayer(profile.getConverser());

        if (target == null) {
            player.sendMessage(CC.RED + "You are not in a conversation.");
            return;
        }

        CoreProfile targetProfile = plugin.getProfileManager().getProfile(target);

        if (targetProfile.hasPlayerIgnored(player.getUniqueId())) {
            player.sendMessage(CC.RED + "That player is ignoring you!");
            return;
        }

        plugin.getServer().getPluginManager().callEvent(new PlayerMessageEvent(player, target, StringUtil.buildString(args, 0)));
    }
}
