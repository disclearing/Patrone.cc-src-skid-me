package zone.potion.commands.impl;

import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import zone.potion.CorePlugin;
import zone.potion.commands.BaseCommand;
import zone.potion.utils.StringUtil;

import java.util.Map;


public class DispatchCommand extends BaseCommand {

    private CorePlugin plugin;

    public DispatchCommand(CorePlugin plugin) {
        super("dispatch", "spike.super");
        setUsage("/dispatch <dispatched command>");

        this.plugin = plugin;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(getUsage());
            return;
        }
        String dispatchedCommand = StringUtil.buildString(args, 0);
        Map<String, Object> map = Maps.newHashMap();
        map.put("server", plugin.getServerName());
        map.put("command", dispatchedCommand);
        map.put("sender", sender.getName());

        plugin.getRedisMessenger().send("dispatch-command", map);
        sender.sendMessage(ChatColor.RED + "Command dispatched.");
    }

}
