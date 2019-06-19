package zone.potion.toothless;

import com.google.common.base.Throwables;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ToothlessConfig {
    public static YamlConfiguration config;
    public static boolean nerfCrits;
    public static boolean practiceOptimizations;
    public static boolean tabHiddenPlayers;
    public static boolean disableMoveEvent;
    public static boolean proxyPipePingFix;

    public static double knockbackFriction;
    public static double knockbackHorizontal;
    public static double knockbackVertical;
    public static double knockbackVerticalLimit;
    public static double knockbackExtraHorizontal;
    public static double knockbackExtraVertical;

    private static Map<String, Command> commands;
    private static File CONFIG_FILE;

    public static void reload() {
        init(CONFIG_FILE);
    }

    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();

        try {
            config.load(CONFIG_FILE);
        } catch (IOException ignored) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load toothless.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }

        config.options().copyDefaults(true);

        commands = new HashMap<>();

        readConfig(ToothlessConfig.class, null);
    }

    public static void registerCommands() {
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            Bukkit.getServer().getCommandMap().register(entry.getKey(), "Toothless", entry.getValue());
        }
    }

    private static void readConfig(Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch (InvocationTargetException ex) {
                        throw Throwables.propagate(ex.getCause());
                    } catch (Exception ex) {
                        Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + method, ex);
                    }
                }
            }
        }

        try {
            config.save(CONFIG_FILE);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + CONFIG_FILE, ex);
        }
    }

    private static void set(String path, Object val) {
        config.set(path, val);
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private static <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return config.getList(path, config.getList(path));
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    private static void disableMoveEvent() {
        disableMoveEvent = getBoolean("settings.disable-move-event", false);
    }

    private static void tabHiddenPlayers() {
        tabHiddenPlayers = getBoolean("settings.tab-hidden-players", false);
    }

    private static void practiceOptimizations() {
        practiceOptimizations = getBoolean("settings.practice-optimizations", false);
    }

    private static void nerfCrits() {
        nerfCrits = getBoolean("settings.nerf-crits", false);
    }

    private static void proxyPipePingFix() {
        proxyPipePingFix = getBoolean("settings.proxy-pipe-ping-fix", false);
    }

    private static void knockbackFriction(){
        knockbackFriction = getDouble("settings.knockbackFriction", 2.0);
    }
    private static void knockbackHorizontal(){
        knockbackHorizontal = getDouble("settings.knockbackHorizontal", 0.35);
    }
    private static void knockbackVertical(){
        knockbackVertical = getDouble("settings.knockbackVertical", 0.35);
    }
    private static void knockbackVerticalLimit(){
        knockbackVerticalLimit = getDouble("settings.knockbackVerticalLimit", 0.4);
    }
    private static void knockbackExtraHorizontal(){
        knockbackExtraHorizontal = getDouble("settings.knockbackExtraHorizontal", 0.425);
    }
    private static void knockbackExtraVertical(){
        knockbackExtraVertical = getDouble("settings.knockbackExtraVertical", 0.085);
    }
}
