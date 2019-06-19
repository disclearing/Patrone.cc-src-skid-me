package cc.patrone.practice.kit;

import zone.potion.storage.flatfile.Config;
import zone.potion.utils.item.ItemBuilder;
import zone.potion.utils.message.CC;
import cc.patrone.practice.PracticePlugin;
import cc.patrone.practice.arena.Arena;
import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public enum Kit implements IKit {
	NODEBUFF("NoDebuff", new ItemBuilder(Material.POTION).durability(16421).flags(ItemFlag.HIDE_POTION_EFFECTS).build(), false, true),
	DEBUFF("Debuff", new ItemBuilder(Material.POTION).durability(16388).flags(ItemFlag.HIDE_POTION_EFFECTS).build(), false, true),
	GAPPLE("Gapple", new ItemBuilder(Material.GOLDEN_APPLE).durability(1).build(), false, false),
	COMBO("Combo", new ItemBuilder(Material.RAW_FISH).durability(3).build(), false, false) {
		@Override
		public void applyAttributes(Player player) {
			player.setMaximumNoDamageTicks(3);
		}
	},
	SOUP("Soup", Material.MUSHROOM_SOUP, false, true),
	AXE("Axe", new ItemBuilder(Material.IRON_AXE).flags(ItemFlag.HIDE_ATTRIBUTES).build(), false, true),
	ARCHER("Archer", Material.BOW, false, true),
	CLASSIC("Classic", Material.FISHING_ROD, false, true),
	SUMO("Sumo", Material.LEASH, false, false);

	@Getter
	private final String name;
	private final ItemStack icon;
	@Getter
	private final ItemStack[] armor;
	@Getter
	private final ItemStack[] contents;
	@Getter
	private final ItemStack[] editorContents;
	@Getter
	private final boolean buildingEnabled;
	@Getter
	private final boolean ranked;
	@Getter
	private final List<Arena> availableArenas = new ArrayList<>();

	Kit(String name, ItemStack icon, boolean buildingEnabled, boolean ranked) {
		this.name = name;

		Config config = new Config(PracticePlugin.getInstance(), "kits");

		List<String> arenas = config.getStringList(name + ".arenas");

		for (String arena : arenas) {
			if (PracticePlugin.getInstance().getArenaManager().getArena(arena) != null) {
				availableArenas.add(PracticePlugin.getInstance().getArenaManager().getArena(arena));
			}
		}

		this.icon = Objects.firstNonNull(ItemBuilder.from(icon).name(CC.ACCENT + name).build(), config.getItem(name + ".icon"));
		this.armor = Objects.firstNonNull(config.getItemArray(name + ".armor"), new ItemStack[4]);
		this.contents = Objects.firstNonNull(config.getItemArray(name + ".contents"), new ItemStack[36]);
		this.editorContents = Objects.firstNonNull(config.getItemArray(name + ".editor-contents"), new ItemStack[54]);
		this.buildingEnabled = buildingEnabled;
		this.ranked = ranked;
	}

	Kit(String name, Material iconType, boolean buildingEnabled, boolean ranked) {
		this(name, new ItemStack(iconType), buildingEnabled, ranked);
	}

	public static Kit getByName(String kitName) {
		for (Kit kit : values()) {
			if (kit.name().equalsIgnoreCase(kitName)) {
				return kit;
			}
		}

		return null;
	}

	public void save() {
		Config config = new Config(PracticePlugin.getInstance(), "kits");
		config.set(name + ".arenas", availableArenas.stream().map(Arena::getName).collect(Collectors.toList()));
		config.save();
	}

	public ItemStack getIcon() {
		return icon.clone();
	}

	@Override
	public void apply(Player player, boolean sendMessage) {
		player.setHealth(player.getMaxHealth());
		player.getInventory().setArmorContents(armor);
		player.getInventory().setContents(contents);
		player.updateInventory();

		if (sendMessage) {
			player.sendMessage(CC.PRIMARY + "You were given the default " + CC.SECONDARY + getName() + CC.PRIMARY + " kit.");
		}
	}
}
