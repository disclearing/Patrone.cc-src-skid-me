package zone.potion.commands.impl.toggle;

import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.commands.PlayerCommand;
import zone.potion.player.CoreProfile;
import zone.potion.utils.message.CC;

public class ToggleMessagesCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public ToggleMessagesCommand(CorePlugin plugin) {
        super("togglemessages");
        this.plugin = plugin;
        setAliases("tpm");
    }

    @Override
    public void execute(Player player, String[] args) {
        CoreProfile profile = plugin.getProfileManager().getProfile(player);
        boolean messaging = !profile.isMessaging();

        profile.setMessaging(messaging);
        player.sendMessage(messaging ? CC.GREEN + "Messages enabled." : CC.RED + "Messages disabled.");
    }
}
