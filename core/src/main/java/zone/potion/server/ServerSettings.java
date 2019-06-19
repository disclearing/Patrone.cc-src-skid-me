package zone.potion.server;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import zone.potion.CorePlugin;
import zone.potion.storage.flatfile.Config;
import zone.potion.task.ShutdownTask;

@Getter
public class ServerSettings {
    private final Config coreConfig;
    @Setter
    private ShutdownTask shutdownTask;
    @Setter
    private int slowChatTime = -1;

    public ServerSettings(CorePlugin plugin) {
        this.coreConfig = new Config(plugin, "core");

        coreConfig.addDefaults(ImmutableMap.<String, Object>builder()
                .build());
        coreConfig.copyDefaults();
    }

    public void saveConfig() {
        coreConfig.save();
    }
}
