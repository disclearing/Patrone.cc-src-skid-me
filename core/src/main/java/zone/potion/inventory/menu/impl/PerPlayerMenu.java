package zone.potion.inventory.menu.impl;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import zone.potion.inventory.menu.Menu;

public abstract class PerPlayerMenu extends Menu {
    protected PerPlayerMenu(int rows, String name) {
        super(rows, name);
    }

    @Override
    public final void update() {
        for (HumanEntity entity : getInventory().getViewers()) {
            Player player = (Player) entity;
            updatePlayer(player);
            player.updateInventory();
        }
    }

    @Override
    public final void open(Player player) {
        super.open(player);
        updatePlayer(player);
    }

    public abstract void updatePlayer(Player player);
}
