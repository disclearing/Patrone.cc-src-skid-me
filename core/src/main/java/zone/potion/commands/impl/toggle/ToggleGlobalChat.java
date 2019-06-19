package zone.potion.commands.impl.toggle;

import org.bukkit.entity.Player;
import zone.potion.CorePlugin;
import zone.potion.commands.PlayerCommand;
import zone.potion.player.CoreProfile;
import zone.potion.utils.message.CC;

public class ToggleGlobalChat extends PlayerCommand {
    private final CorePlugin plugin;

    public ToggleGlobalChat(CorePlugin plugin) {
        super("toggleglobalchat");
        this.plugin = plugin;
        setAliases("togglechat", "tgc");
    }

    @Override
    public void execute(Player player, String[] args) {
        CoreProfile profile = plugin.getProfileManager().getProfile(player);
        boolean enabled = !profile.isGlobalChatEnabled();

        profile.setGlobalChatEnabled(enabled);
        player.sendMessage(enabled ? CC.GREEN + "Global chat enabled." : CC.RED + "Global chat disabled.");
    }
}
