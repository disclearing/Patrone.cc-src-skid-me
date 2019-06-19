package zone.potion.commands.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.commands.BaseCommand;
import zone.potion.player.CoreProfile;
import zone.potion.utils.message.CC;

import java.util.Collections;

public class ClearChatCommand extends BaseCommand {
    private static final String BLANK_MESSAGE = String.join("", Collections.nCopies(150, "§8 §8 §1 §3 §3 §7 §8 §r\n"));
    private final CorePlugin plugin;

    public ClearChatCommand(CorePlugin plugin) {
        super("clearchat", "spike.staff");
        this.plugin = plugin;
        setAliases("cc");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            CoreProfile profile = plugin.getProfileManager().getProfile(player);

            if (!profile.hasStaff()) {
                player.sendMessage(BLANK_MESSAGE);
            }
        }

        plugin.getServer().broadcastMessage(CC.GREEN + "The chat was cleared by " + sender.getName() + ".");
        sender.sendMessage(CC.YELLOW + "Don't worry, staff can still see cleared messages.");
    }
}
