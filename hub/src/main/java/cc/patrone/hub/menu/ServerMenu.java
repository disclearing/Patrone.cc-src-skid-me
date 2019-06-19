package cc.patrone.hub.menu;

import zone.potion.inventory.menu.Menu;
import zone.potion.utils.item.ItemBuilder;
import zone.potion.utils.message.CC;
import cc.patrone.hub.Hub;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static cc.patrone.hub.util.BungeeUtils.connectToServer;

public class ServerMenu extends Menu {

    private Hub plugin;

    public ServerMenu(Hub plugin, String name) {
        super(1, name);
        this.plugin = plugin;
    }

    @Override
    public void setup() {

    }

    private ItemStack getServerItem(String proxy, boolean differentProxy, String proxyHost) {
        return
                differentProxy ? new ItemBuilder(Material.DIAMOND_SWORD)
                        .name(CC.D_GRAY + proxy.toUpperCase() + CC.PURPLE + " Practice")
                        .lore(CC.RED + "If you want to join the " + proxy.toUpperCase() + " proxy, you'll have to use this IP: " + proxyHost)
                        .build() :

                        new ItemBuilder(Material.DIAMOND_SWORD)
                                .name(CC.D_GRAY + proxy.toUpperCase() + CC.PURPLE + " Practice")
                                .lore(CC.GRAY + "Click to connect.")
                                .build();
    }

    @Override
    public void update() {

        switch (plugin.getConfig().getString("proxy")) {
            case "US":
                setActionableItem(3, getServerItem("US", false, "patrone.cc"), player -> connectToServer(plugin, player, "practice"));
                setActionableItem(4, getServerItem("EU", true, "eu.patrone.cc"), player -> {});
                break;
            case "EU":
                setActionableItem(3, getServerItem("EU", false, "eu.patrone.cc"), player -> connectToServer(plugin, player, "practice"));
                setActionableItem(4, getServerItem("US", true, "patrone.cc"), player -> {});
                break;
        }

        setActionableItem(8, new ItemBuilder(Material.INK_SACK).color(DyeColor.RED).name(CC.RED + "Leave Patrone").build(), player ->
                player.kickPlayer(CC.RED + "Thank you for checking out Patrone. Goodbye.")
        );
    }
}
