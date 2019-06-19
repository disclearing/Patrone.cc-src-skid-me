package zone.potion.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import zone.potion.CorePlugin;
import zone.potion.player.CoreProfile;
import zone.potion.utils.message.CC;

@RequiredArgsConstructor
public class HelpCommandListener implements Listener {

    /**
     * Commands we don't want players to be able to run(and or, display the help message for).
     */
    private static final String[] DISALLOWED_COMMANDS = {
            "?",
            "help",
            "version",
            "ver",
            "icanhasbukkit"
    };
    private final CorePlugin plugin;

    @EventHandler
    public void onHelpCommand(PlayerCommandPreprocessEvent event) {
        CoreProfile profile = plugin.getProfileManager().getProfile(event.getPlayer());
        boolean staff = profile.hasStaff();
        for (String command : DISALLOWED_COMMANDS) {
            if (event.getMessage().startsWith("/" + command) && !staff) {
                event.getPlayer().sendMessage(CC.RED + "If you need help, require assistance by using /helpop.");
            }
        }
    }

}
