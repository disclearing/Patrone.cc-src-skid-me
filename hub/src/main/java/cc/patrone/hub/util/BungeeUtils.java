package cc.patrone.hub.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Idriz Pelaj
 */

public class BungeeUtils {

    /**
     *
     * @param plugin The plugin instance needed, so we can send a plugin message.
     * @param player The player object, the player we'll transfer between servers.
     * @param server The server we want to transfer the player to. Must be the same as the one in the BungeeCord configuration file.
     */
    public static void connectToServer(JavaPlugin plugin, Player player, String server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(server);
        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

}
