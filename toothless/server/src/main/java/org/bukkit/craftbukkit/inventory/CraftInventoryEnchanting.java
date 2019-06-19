package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.InventorySubcontainer;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryEnchanting extends CraftInventory implements EnchantingInventory {
    public CraftInventoryEnchanting(InventorySubcontainer inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getItem() {
        return getItem(0);
    }

    @Override
    public void setItem(ItemStack item) {
        setItem(0, item);
    }

    @Override
    public InventorySubcontainer getInventory() {
        return (InventorySubcontainer) inventory;
    }

    @Override
    public ItemStack getSecondary() {
        return getItem(1);
    }

    @Override
    public void setSecondary(ItemStack item) {
        setItem(1, item);
    }
}
