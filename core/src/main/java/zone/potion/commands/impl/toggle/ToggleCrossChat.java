package zone.potion.commands.impl.toggle;

import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.commands.PlayerCommand;
import zone.potion.player.CoreProfile;
import zone.potion.utils.message.CC;

public class ToggleCrossChat extends PlayerCommand {
    private final CorePlugin plugin;

    public ToggleCrossChat(CorePlugin plugin) {
        super("togglecrosschat");
        this.plugin = plugin;
        setAliases("tcchat", "tcc");
    }

    @Override
    public void execute(Player player, String[] args) {
        CoreProfile profile = plugin.getProfileManager().getProfile(player);
        boolean enabled = !profile.isCrossChat();

        profile.setCrossChat(enabled);
        player.sendMessage(enabled ? CC.GREEN + "Cross chat enabled." : CC.RED + "Cross chat disabled.");
    }
}
