package cc.patrone.hub;

import zone.potion.CorePlugin;
import cc.patrone.hub.listeners.PlayerJoinListener;
import cc.patrone.hub.menu.ServerMenu;
import cc.patrone.hub.task.InventoryTask;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hub extends JavaPlugin {

    private CorePlugin core;
    private PluginManager pluginManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.pluginManager = getServer().getPluginManager();
        //Fixed indentation. Should be wrapped in curly braces.
        if (!this.setupCore()) {
            getLogger().info("Disabling plugin. Couldn't find core.");
            pluginManager.disablePlugin(this);
        }

        core.getMenuManager().registerMenus(new ServerMenu(this, ChatColor.DARK_PURPLE + "Server Selector"));

        getServer().getScheduler().runTaskTimer(this, new InventoryTask(this), 0, 20);

        pluginManager.registerEvents(new PlayerJoinListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Initializes the core if found.
     * @return whether or not the core was found.
     */
    private boolean setupCore() {
        if (pluginManager.getPlugin("pCore") == null)
            return false;
        core = CorePlugin.getInstance();
        return true;
    }

    public CorePlugin getCore() {
        return core;
    }
}
