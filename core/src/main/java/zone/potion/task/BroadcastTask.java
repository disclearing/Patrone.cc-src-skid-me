package zone.potion.task;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitRunnable;
import zone.potion.CorePlugin;
import zone.potion.utils.message.CC;

import java.util.List;

@RequiredArgsConstructor
public class BroadcastTask extends BukkitRunnable {

    private CorePlugin instance;

    private int lastAnnouncement;

    public BroadcastTask(CorePlugin instance) {
        this.instance = instance;

        this.lastAnnouncement = 0;
    }

    @Override
    public void run() {

        List<String> announcements = instance.getConfig().getStringList("broadcasts.strings");

        int announcementIndex = lastAnnouncement;

        if (lastAnnouncement >= announcements.size()) {
            announcementIndex = 0;
            lastAnnouncement = 0;
        }

        String[] announcement = {
                CC.color(" "),
                CC.color(announcements.get(announcementIndex)),
                CC.color(" ")
        };

        Bukkit.getOnlinePlayers().forEach(player ->
            player.sendMessage(announcement)
        );

        lastAnnouncement++;

    }

}
