package zone.potion.commands.impl.toggle;

import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.commands.PlayerCommand;
import zone.potion.player.CoreProfile;
import zone.potion.utils.message.CC;

public class ToggleSoundsCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public ToggleSoundsCommand(CorePlugin plugin) {
        super("togglesounds");
        this.plugin = plugin;
        setAliases("sounds", "ts");
    }

    @Override
    public void execute(Player player, String[] args) {
        CoreProfile profile = plugin.getProfileManager().getProfile(player);
        boolean playingSounds = !profile.isPlayingSounds();

        profile.setPlayingSounds(playingSounds);
        player.sendMessage(playingSounds ? CC.GREEN + "Sounds enabled." : CC.RED + "Sounds disabled.");
    }
}
