package zone.potion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import zone.potion.commands.BaseCommand;
import zone.potion.commands.impl.*;
import zone.potion.commands.impl.staff.*;
import zone.potion.commands.impl.staff.punish.*;
import zone.potion.commands.impl.toggle.ToggleCrossChat;
import zone.potion.commands.impl.toggle.ToggleGlobalChat;
import zone.potion.commands.impl.toggle.ToggleMessagesCommand;
import zone.potion.commands.impl.toggle.ToggleSoundsCommand;
import zone.potion.listeners.HelpCommandListener;
import zone.potion.listeners.InventoryListener;
import zone.potion.listeners.MessageListener;
import zone.potion.listeners.PlayerListener;
import zone.potion.listeners.redis.*;
import zone.potion.managers.MenuManager;
import zone.potion.managers.PlayerManager;
import zone.potion.managers.ProfileManager;
import zone.potion.managers.StaffManager;
import zone.potion.redis.RedisMessenger;
import zone.potion.server.ServerSettings;
import zone.potion.server.filter.Filter;
import zone.potion.storage.database.MongoStorage;
import zone.potion.task.AFKTask;
import zone.potion.task.BroadcastTask;
import zone.potion.utils.message.CC;
import zone.potion.utils.structure.Cuboid;

import java.lang.reflect.Field;
import java.util.Arrays;

@Getter
public class CorePlugin extends JavaPlugin {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    @Getter
    private static CorePlugin instance;
    @Getter
    private static boolean isServerEnabled = false;
    private ServerSettings serverSettings;
    private Filter filter;
    private MongoStorage mongoStorage;

    private ProfileManager profileManager;
    private StaffManager staffManager;
    private PlayerManager playerManager;
    private MenuManager menuManager;
    private RedisMessenger redisMessenger;

    private BroadcastTask broadcastTask;

    private LuckPermsApi luckPermsApi;


    private static void registerSerializableClass(Class<?> clazz) {
        if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
            Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
            ConfigurationSerialization.registerClass(serializable);
        }
    }

    @Override
    public void onEnable() {

        instance = this;
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        saveDefaultConfig();

        registerSerializableClass(Cuboid.class);

        RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
        if (provider != null) {
            LuckPermsApi api = provider.getProvider();

            this.luckPermsApi = api;
        }

        serverSettings = new ServerSettings(this);
        filter = new Filter();

        redisMessenger = new RedisMessenger(
                this,
                getConfig().getString("redis.host"),
                getConfig().getInt("redis.port"),
                getConfig().getInt("redis.timeout"),
                getConfig().getString("redis.password")
        );

        redisMessenger.registerListeners(
                new StaffChatListener(this),
                new StaffJoinListener(this),
                new ChatListener(this),
                new HelpopListener(this),
                new ReportListener(this),
                new DispatchCommandListener(this),
                new FreezeListener(this)
        );

        redisMessenger.initialize();

        mongoStorage = new MongoStorage();

        profileManager = new ProfileManager();
        staffManager = new StaffManager(this);
        playerManager = new PlayerManager();
        menuManager = new MenuManager(this);

        registerCommands(
                new BroadcastCommand(this),
                new ClearChatCommand(this),
                new IgnoreCommand(this),
                new MessageCommand(this),
                new ReloadKBCommand(this),
                new ReplyCommand(this),
                new StaffChatCommand(this),
                new TeleportCommand(this),
                new ToggleMessagesCommand(this),
                new ToggleGlobalChat(this),
                new ToggleSoundsCommand(this),
                new ToggleCrossChat(this),
                new VanishCommand(this),
                new ReportCommand(this),
                new HelpOpCommand(this),
                new PingCommand(),
                new SlowChatCommand(this),
                new GameModeCommand(),
                new DispatchCommand(this),
                new FreezeCommand(this),
                new ShutdownCommand(this)
        );
        registerListeners(
                new PlayerListener(this),
                new MessageListener(this),
                new InventoryListener(this),
                new HelpCommandListener(this)
        );


        int broadcastTime = getConfig().getInt("broadcasts.delay");
        if (getConfig().getBoolean("broadcasts.enabled")) {
            this.broadcastTask = new BroadcastTask(this);
            broadcastTask.runTaskTimerAsynchronously(this, broadcastTime * 20L, broadcastTime * 20L);
        }

        getServer().getScheduler().runTaskTimer(this, new AFKTask(this), 0, 20 * 60);
        getServer().getScheduler().runTaskLater(this, () -> isServerEnabled = true, 20L);

    }

    public String getServerName() {
        return getConfig().getString("server-name");
    }

    @Override
    public void onDisable() {
        profileManager.saveProfiles();
        serverSettings.saveConfig();

        for (Player player : getServer().getOnlinePlayers()) {
            player.kickPlayer(CC.RED + "The server is restarting.");
        }
    }

    private void registerCommands(BaseCommand... commands) {
        try {
            final Field bukkitCommandMap = getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(getServer());

            Arrays.stream(commands).forEach(command -> {
                commandMap.register(getName(), command);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
