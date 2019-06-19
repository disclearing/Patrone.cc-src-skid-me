package zone.potion.task;


import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import zone.potion.CorePlugin;
import zone.potion.utils.message.CC;
import zone.potion.utils.time.TimeUtil;

public class ShutdownTask extends BukkitRunnable {

    private CorePlugin instance;
    private Integer shutdownSeconds;

    public ShutdownTask(CorePlugin instance, Integer shutdownSeconds){
        this.instance = instance;
        this.shutdownSeconds = shutdownSeconds;
    }

    @Override
    public void run() {
        if (shutdownSeconds == 0) {
            Bukkit.shutdown();
        } else if (shutdownSeconds % 60 == 0 || shutdownSeconds == 30 || shutdownSeconds == 10 || shutdownSeconds <= 5) {
            instance.getServer().getOnlinePlayers().forEach(player -> {
                player.sendMessage(" ");
                player.sendMessage(CC.color("&c[Alert] &7The server will restart in " + TimeUtil.formatTimeSeconds(shutdownSeconds) + "."));
                player.sendMessage(" ");
            });
        }

        shutdownSeconds--;
    }
}
