package zone.potion.managers;

import org.bukkit.inventory.Inventory;
import zone.potion.CorePlugin;
import zone.potion.inventory.menu.Menu;
import zone.potion.inventory.menu.impl.ReportMenu;

import java.util.HashMap;
import java.util.Map;

public class MenuManager {
    private final Map<Class<? extends Menu>, Menu> menus = new HashMap<>();

    public MenuManager(CorePlugin plugin) {
        registerMenus(
                new ReportMenu(plugin)
        );
    }

    public Menu getMenu(Class<? extends Menu> clazz) {
        return menus.get(clazz);
    }

    public Menu getMatchingMenu(Inventory other) {
        for (Menu menu : menus.values()) {
            if (menu.getInventory().equals(other)) {
                return menu;
            }
        }

        return null;
    }

    public void registerMenus(Menu... menus) {
        for (Menu menu : menus) {
            menu.setup();
            menu.update();
            this.menus.put(menu.getClass(), menu);
        }
    }
}