package zone.potion.commands.impl;

import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.commands.PlayerCommand;
import zone.potion.event.player.PlayerMessageEvent;
import zone.potion.player.CoreProfile;
import zone.potion.utils.StringUtil;
import zone.potion.utils.message.CC;
import zone.potion.utils.message.Messages;

public class MessageCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public MessageCommand(CorePlugin plugin) {
        super("message");
        this.plugin = plugin;
        setAliases("msg", "m", "whisper", "w", "tell", "t");
        setUsage(CC.RED + "Usage: /message <player> <message>");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(usageMessage);
            return;
        }

        CoreProfile profile = plugin.getProfileManager().getProfile(player);

        Player target = plugin.getServer().getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Messages.PLAYER_NOT_FOUND);
            return;
        }

        if (target.isRecording()) {
            player.sendMessage(CC.RED + "That player is in recording mode and can't see your messages!");
            return;
        }

        CoreProfile targetProfile = plugin.getProfileManager().getProfile(target);

        if (targetProfile.hasPlayerIgnored(player.getUniqueId())) {
            player.sendMessage(CC.RED + "That player is ignoring you!");
            return;
        }

        plugin.getServer().getPluginManager().callEvent(new PlayerMessageEvent(player, target, StringUtil.buildString(args, 1)));
    }
}
