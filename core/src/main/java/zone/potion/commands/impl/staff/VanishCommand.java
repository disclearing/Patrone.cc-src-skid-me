package zone.potion.commands.impl.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.commands.BaseCommand;
import zone.potion.player.CoreProfile;
import zone.potion.utils.message.CC;

public class VanishCommand extends BaseCommand {
    private final CorePlugin plugin;

    public VanishCommand(CorePlugin plugin) {
        super("vanish", "spike.staff");
        this.plugin = plugin;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        CoreProfile profile = plugin.getProfileManager().getProfile(player);
        boolean vanished = !profile.isVanished();

        profile.setVanished(vanished);

        for (Player online : plugin.getServer().getOnlinePlayers()) {
            plugin.getStaffManager().hideVanishedStaffFromPlayer(online);
        }

        player.sendMessage(vanished ? CC.GREEN + "Poof, you vanished." : CC.RED + "You're visible again.");
    }
}
