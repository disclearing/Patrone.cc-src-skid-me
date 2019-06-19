package zone.potion.listeners;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import zone.potion.CorePlugin;
import zone.potion.player.CoreProfile;
import zone.potion.storage.database.MongoStorage;
import zone.potion.utils.message.CC;
import zone.potion.utils.message.Messages;
import zone.potion.utils.time.TimeUtil;
import zone.potion.utils.timer.Timer;

import java.util.Iterator;
import java.util.Map;

@RequiredArgsConstructor
public class PlayerListener implements Listener {
    private static final String[] DISALLOWED_PERMISSIONS = {
            "bukkit.command.version", "bukkit.command.plugins", "bukkit.command.help", "bukkit.command.tps",
            "minecraft.command.tell", "minecraft.command.me", "minecraft.command.help"
    };
    private final CorePlugin plugin;

    private boolean isNotBanned(Document document, AsyncPlayerPreLoginEvent event) {
        if (document != null && document.getBoolean("banned") != null && document.getBoolean("banned")) {
            long expiry = document.getLong("ban_expiry");
            long difference = expiry - System.currentTimeMillis();

            if (expiry == -1L || difference > 0) {
                String formattedDifference = TimeUtil.formatTimeMillis(difference);
                String kickMessage = expiry == -1L ? Messages.BANNED_PERMANENTLY : String.format(Messages.BANNED_TEMPORARILY, formattedDifference);

                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, kickMessage);
                return false;
            }
        }

        return true;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoginBeforeEnable(AsyncPlayerPreLoginEvent event) {
        if (!CorePlugin.isServerEnabled()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CC.RED + "The server is still starting up.");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (plugin.getPlayerManager().isNameOnline(event.getName()) || plugin.getPlayerManager().getOnlineByIp(event.getAddress()) > 3) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CC.RED + "You're already online!");
        } else if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            MongoStorage storage = plugin.getMongoStorage();

            boolean notBannedById = isNotBanned(storage.getDocument("punished_ids", event.getUniqueId()), event);
            boolean notBannedByIp = isNotBanned(storage.getDocument("punished_addresses", event.getAddress().getHostAddress()), event);

            if (notBannedById && notBannedByIp) {
                plugin.getProfileManager().createProfile(event.getName(), event.getUniqueId(), event.getAddress().getHostAddress());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        CoreProfile profile = plugin.getProfileManager().getProfile(player);

        if (profile == null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Messages.DATA_LOAD_FAIL);
            return;
        } else if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            plugin.getProfileManager().removeProfile(player.getUniqueId());
            return;
        }

        if (profile.hasStaff()) {
            plugin.getStaffManager().addCachedStaff(profile);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        plugin.getPlayerManager().addPlayer(player);

        CoreProfile profile = plugin.getProfileManager().getProfile(player);

        plugin.getStaffManager().hideVanishedStaffFromPlayer(player);

        if (profile.hasStaff()) {
            Map<String, Object> message = Maps.newHashMap();
            message.put("server", plugin.getServerName());
            message.put("sender", profile.getPrimaryGroupPrefix() + player.getName());

            plugin.getRedisMessenger().send("staff-join", message);
        }
        profile.setLastLocation(new int[]{player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()});
    }

    private void onDisconnect(Player player) {
        plugin.getPlayerManager().removePlayer(player);

        CoreProfile profile = plugin.getProfileManager().getProfile(player);

        // in case disconnect is somehow called twice
        if (profile == null) {
            return;
        }

        if (profile.hasStaff()) {
            plugin.getStaffManager().removeCachedStaff(profile);

            Map<String, Object> message = Maps.newHashMap();
            message.put("server", plugin.getServerName());
            message.put("sender", profile.getPrimaryGroupPrefix() + player.getName());

            plugin.getRedisMessenger().send("staff-quit", message);
            //plugin.getStaffManager().messageStaffWithPrefix(profile.getChatFormat() + CC.PRIMARY + " left the server.");
        }

        profile.save(true);
        plugin.getProfileManager().removeProfile(player.getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        event.setLeaveMessage(null);

        onDisconnect(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        onDisconnect(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        CoreProfile profile = plugin.getProfileManager().getProfile(player);
        String msg = event.getMessage();

        if (!profile.hasStaff()) {
            if (plugin.getServerSettings().getSlowChatTime() != -1) {
                long lastChatTime = profile.getLastChatTime();
                int slowChatTime = plugin.getServerSettings().getSlowChatTime();
                long sum = lastChatTime + (slowChatTime * 1000);

                if (lastChatTime != 0 && sum > System.currentTimeMillis()) {
                    event.setCancelled(true);
                    String diff = TimeUtil.formatTimeMillis(sum - System.currentTimeMillis());
                    player.sendMessage(CC.RED + "Slow chat is currently enabled. You can talk again in " + diff + ".");
                    return;
                }
            }

            Timer timer = profile.getChatCooldownTimer();

            if (timer.isActive()) {
                event.setCancelled(true);
                player.sendMessage(CC.RED + "You can't chat for another " + timer.formattedExpiration() + ".");
                return;
            }

        } else if (profile.isInStaffChat()) {
            event.setCancelled(true);

            Map<String, Object> message = Maps.newHashMap();

            message.put("server", plugin.getServerName());
            message.put("format", profile.getPrimaryGroupPrefix() + player.getName());
            message.put("message", event.getMessage());
            message.put("sender", event.getPlayer().getName());
            plugin.getRedisMessenger().send("staff-chat", message);
            //plugin.getStaffManager().messageStaff(profile.getChatFormat(), msg);
            return;
        }

        if (plugin.getFilter().isFiltered(msg)) {
            if (profile.hasStaff()) {
                player.sendMessage(CC.RED + "That would have been filtered.");
            } else {
                event.setCancelled(true);

                String formattedMessage = profile.getPrimaryGroupPrefix() + player.getName() + CC.R + ": " + msg;

                plugin.getStaffManager().messageStaff(CC.RED + "(Filtered) " + formattedMessage);
                player.sendMessage(formattedMessage);
                return;
            }
        }

        Iterator<Player> recipients = event.getRecipients().iterator();

        while (recipients.hasNext()) {
            Player recipient = recipients.next();
            CoreProfile recipientProfile = plugin.getProfileManager().getProfile(recipient);

            if (recipientProfile == null) {
                continue;
            }

            if (recipientProfile.hasPlayerIgnored(player.getUniqueId())
                    || (!recipientProfile.isGlobalChatEnabled() && (!profile.hasStaff() || recipientProfile.hasStaff()))) {
                recipients.remove();
            } else if (recipient != player) {
                String[] words = msg.split(" ");
                boolean found = false;

                StringBuilder newMessage = new StringBuilder();

                for (String word : words) {
                    if (recipient.getName().equalsIgnoreCase(word) && !found) {
                        newMessage.append(CC.PINK).append(CC.I).append(word).append(CC.R).append(" ");
                        found = true;
                    } else {
                        newMessage.append(word).append(" ");
                    }
                }

                if (!found) {
                    continue;
                }

                if (recipientProfile.isPlayingSounds()) {
                    recipient.playSound(recipient.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
                }

                String mentionMessage = profile.getPrimaryGroupPrefix() + player.getName()+ CC.R + ": " + newMessage.toString();

                recipient.sendMessage(mentionMessage);
                recipient.sendMessage(player.getDisplayName() + CC.PRIMARY + " mentioned you!");

                recipients.remove();
            }
        }

        Map<String, Object> map = Maps.newHashMap();
        map.put("message", event.getMessage());
        map.put("format", profile.getPrimaryGroupPrefix() + player.getName());
        map.put("region", plugin.getConfig().getString("region"));
        map.put("uuid", event.getPlayer().getUniqueId().toString());
        plugin.getRedisMessenger().send("cross-chat", map);
        event.setFormat(profile.getPrimaryGroupPrefix() + player.getName() + CC.R + ": %2$s");

        profile.updateLastChatTime();
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        CoreProfile profile = plugin.getProfileManager().getProfile(player);

        if (profile.hasStaff()) return;

        Timer timer = profile.getCommandCooldownTimer();

        if (timer.isActive()) {
            event.setCancelled(true);
            player.sendMessage(CC.RED + "You can't use commands for another " + timer.formattedExpiration() + ".");
        }

    }

    @EventHandler
    public void onFreezeDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            CoreProfile profile = plugin.getProfileManager().getProfile(player);
            if (e.getEntity() instanceof Player && plugin.getProfileManager().getProfile((Player)e.getEntity()).isFrozen() || profile.isFrozen())
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFreezeCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        CoreProfile profile = plugin.getProfileManager().getProfile(player);
        if (profile.isFrozen()) {
            player.sendMessage(CC.RED + "You cannot run commands while you are frozen.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFreezeInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        CoreProfile profile = plugin.getProfileManager().getProfile(player);
        if (profile.isFrozen()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFreezeSprint(PlayerToggleSprintEvent e) {
        Player player = e.getPlayer();
        CoreProfile profile = plugin.getProfileManager().getProfile(player);
        if (profile.isFrozen()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        CoreProfile profile = plugin.getProfileManager().getProfile(player);
        if (profile.isFrozen()) {
            e.setCancelled(true);
        }
    }


}
