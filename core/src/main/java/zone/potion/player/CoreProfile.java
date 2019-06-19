package zone.potion.player;

import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;
import zone.potion.CorePlugin;
import zone.potion.storage.database.MongoRequest;
import zone.potion.utils.message.CC;
import zone.potion.utils.message.Messages;
import zone.potion.utils.time.TimeUtil;
import zone.potion.utils.timer.Timer;
import zone.potion.utils.timer.impl.DoubleTimer;
import zone.potion.utils.timer.impl.IntegerTimer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Setter
@Getter
public class CoreProfile {

    private final List<UUID> ignored = new ArrayList<>();
    private final List<String> knownAddresses = new ArrayList<>();
    private final String name;
    private final UUID id;
    private final Timer commandCooldownTimer = new DoubleTimer(1);
    private final Timer reportCooldownTimer = new IntegerTimer(TimeUnit.SECONDS, 60);

    private Timer chatCooldownTimer;
    private UUID converser;
    private String reportingPlayerName;

    private boolean playingSounds = true;
    private boolean messaging = true;
    private boolean crossChat = true;
    private boolean globalChatEnabled = true;
    private boolean inStaffChat;
    private boolean vanished;
    private boolean frozen = false;

    private long lastChatTime;
    private int[] lastLocation = new int[]{0, 0, 0};
    private int afkViolations = 0;


    @SuppressWarnings("unchecked")
    public CoreProfile(String name, UUID id, String address) {
        this.name = name;
        this.id = id;
        this.knownAddresses.add(address);

        CorePlugin.getInstance().getMongoStorage().getOrCreateDocument("players", id, (document, exists) -> {
            if (exists) {
                this.inStaffChat = document.getBoolean("staff_chat_enabled", inStaffChat);
                this.messaging = document.getBoolean("messaging_enabled", messaging);
                this.playingSounds = document.getBoolean("playing_sounds", playingSounds);
                this.crossChat = document.getBoolean("cross_chat", crossChat);

                List<UUID> ignored = (List<UUID>) document.get("ignored_ids");

                if (ignored != null) {
                    this.ignored.addAll(ignored);
                }

                List<String> knownAddresses = (List<String>) document.get("known_addresses");

                if (knownAddresses != null) {
                    for (String knownAddress : knownAddresses) {
                        if (knownAddress.equals(address)) {
                            continue;
                        }

                        this.knownAddresses.add(knownAddress);
                    }
                }
            }

            save(false);
        });

    }

    public void save(boolean async) {
        MongoRequest request = MongoRequest.newRequest("players", id)
                .put("name", name)
                .put("staff_chat_enabled", inStaffChat)
                .put("messaging_enabled", messaging)
                .put("cross_chat", crossChat)
                .put("playing_sounds", playingSounds)
                .put("ignored_ids", ignored)
                .put("known_addresses", knownAddresses);

        if (async) {
            CorePlugin.getInstance().getServer().getScheduler().runTaskAsynchronously(CorePlugin.getInstance(), request::run);
        } else {
            request.run();
        }
    }

    public int getLastLocationX() {
        return lastLocation[0];
    }

    public int getLastLocationY() {
        return lastLocation[1];
    }

    public int getLastLocationZ() {
        return lastLocation[2];
    }

    public void updateLastChatTime() {
        lastChatTime = System.currentTimeMillis();
    }

    public boolean hasStaff(){
        if(getPlayer() == null) return false;

        return getPlayer().hasPermission("spike.staff");
    }

    public void ignore(UUID id) {
        ignored.add(id);
    }

    public void unignore(UUID id) {
        ignored.remove(id);
    }

    public boolean hasPlayerIgnored(UUID id) {
        return ignored.contains(id);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(id);
    }

    public boolean freeze(CommandSender executor) {
        frozen = !frozen;
        if (frozen) {
            getPlayer().setWalkSpeed(0.0F);
            getPlayer().setFlySpeed(0.0F);
            getPlayer().setFoodLevel(0);
            getPlayer().setSprinting(false);
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200));
            Arrays.stream(Messages.FROZEN_MESSAGE).forEach(message -> Messages.sendCenteredMessage(getPlayer(), message));
            executor.sendMessage(CC.RED + "Player frozen.");
            return true;
        }
        getPlayer().setWalkSpeed(0.2f);
        getPlayer().setFlySpeed(0.0001f);
        getPlayer().setFoodLevel(20);
        getPlayer().setSprinting(true);
        getPlayer().removePotionEffect(PotionEffectType.JUMP);
        getPlayer().sendMessage(CC.SECONDARY + "You have been unfrozen.");
        executor.sendMessage(CC.RED + getPlayer().getName() + " has been unfrozen.");
        return false;
    }

    public String getPrimaryGroupPrefix() {
        User user = CorePlugin.getInstance().getLuckPermsApi().getUser(id);
        UserData userData = user.getCachedData();
        Contexts contexts = CorePlugin.getInstance().getLuckPermsApi().getContextForUser(user).get();

        MetaData metaData = userData.getMetaData(contexts);

        return metaData.getPrefix();
    }

}