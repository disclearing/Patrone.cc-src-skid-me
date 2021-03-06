package cc.patrone.practice.commands.management;

import zone.potion.commands.PlayerCommand;
import zone.potion.player.rank.Rank;
import zone.potion.storage.flatfile.Config;
import zone.potion.utils.message.CC;
import cc.patrone.practice.PracticePlugin;
import cc.patrone.practice.arena.Arena;
import cc.patrone.practice.kit.Kit;
import java.util.Set;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class KitCommand extends PlayerCommand {
	private final PracticePlugin plugin;
	private final Config config;

	public KitCommand(PracticePlugin plugin) {
		super("kit", Rank.ADMIN);
		this.plugin = plugin;
		this.config = new Config(plugin, "kits");
		setUsage(CC.RED + "Usage: /kit <subcommand> <args>");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (args.length < 2) {
			player.sendMessage(usageMessage);
			return;
		}

		String subCommand = args[0].toLowerCase();
		Kit kit = Kit.getByName(args[1]);

		if (kit == null) {
			player.sendMessage(CC.RED + "Kit doesn't exist!");
			return;
		}

		switch (subCommand) {
			case "setinv":
				if (player.getGameMode() == GameMode.CREATIVE) {
					player.sendMessage(CC.RED + "You can't set kit contents in creative mode!");
				} else {
					ItemStack[] armor = player.getInventory().getArmorContents().clone();
					ItemStack[] contents = player.getInventory().getContents().clone();

					config.set(kit.getName() + ".armor", armor);
					config.set(kit.getName() + ".contents", contents);
					config.save();

					player.sendMessage(CC.GREEN + "Successfully set kit contents for " + args[1] + ".");
				}
				break;
			case "setedititems":
				Block block = player.getTargetBlock((Set<Material>) null, 100);

				if (block == null || block.getType() != Material.CHEST) {
					player.sendMessage(CC.RED + "That's not a chest!");
				} else {
					BlockState state = block.getState();
					InventoryHolder holder = ((Chest) state).getInventory().getHolder();

					if (holder instanceof DoubleChest) {
						DoubleChest chest = (DoubleChest) holder;

						config.set(kit.getName() + ".editor-contents", chest.getInventory().getContents());
						config.save();

						player.sendMessage(CC.GREEN + "Successfully set editor contents for " + args[1] + ".");
					} else {
						player.sendMessage(CC.RED + "That's not a double chest!");
					}
				}
				break;
			case "getinv":
				player.getInventory().setContents(kit.getContents());
				player.getInventory().setArmorContents(kit.getArmor());
				player.updateInventory();
				player.sendMessage(CC.GREEN + "Successfully retrieved kit contents from " + args[1] + ".");
				break;
			case "icon":
				if (player.getItemInHand().getType() == Material.AIR) {
					player.sendMessage(CC.RED + "You must be holding an item to set the kit icon!");
				} else {
					config.set(kit.getName() + ".icon", player.getItemInHand());
					config.save();

					player.sendMessage(CC.GREEN + "Successfully set icon for kit " + args[1] + ".");
				}
				break;
			case "addarena": {
				if (args.length < 3) {
					player.sendMessage(CC.RED + "You must specify an arena.");
					return;
				}

				Arena arena = plugin.getArenaManager().getArena(args[2]);

				if (arena == null) {
					player.sendMessage(CC.RED + "That arena doesn't exist.");
					return;
				}

				if (kit.getAvailableArenas().contains(arena)) {
					player.sendMessage(CC.RED + "Arena already added");
				} else {
					kit.getAvailableArenas().add(arena);
					player.sendMessage(CC.GREEN + "Added arena");
				}
				break;
			}
			case "removearena":
				if (args.length < 3) {
					player.sendMessage(CC.RED + "You must specify an arena.");
					return;
				}

				Arena arena = plugin.getArenaManager().getArena(args[2]);

				if (arena == null) {
					player.sendMessage(CC.RED + "That arena doesn't exist.");
					return;
				}

				if (kit.getAvailableArenas().contains(arena)) {
					kit.getAvailableArenas().remove(arena);
					player.sendMessage(CC.GREEN + "Removed arena");
				} else {
					player.sendMessage(CC.RED + "Arena isn't added");
				}
				break;
			default:
				player.sendMessage(usageMessage);
				break;
		}

		plugin.getMenuManager().updateAllMenus();
	}
}
