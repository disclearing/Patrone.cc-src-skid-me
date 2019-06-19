package net.minecraft.server;

// CraftBukkit start

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

import java.util.List;
// CraftBukkit end

public class InventoryHorseChest extends InventorySubcontainer {

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private EntityHorse horse;
    private int maxStack = MAX_STACK;
    public InventoryHorseChest(String s, int i) {
        super(s, false, i);
    }

    public InventoryHorseChest(String s, int i, EntityHorse horse) {
        super(s, false, i, (org.bukkit.craftbukkit.entity.CraftHorse) horse.getBukkitEntity());
        this.horse = horse;
    }

    @Override
    public ItemStack[] getContents() {
        return this.items;
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return transaction;
    }

    @Override
    public org.bukkit.inventory.InventoryHolder getOwner() {
        return (org.bukkit.entity.Horse) this.horse.getBukkitEntity();
    }

    @Override
    public int getMaxStackSize() {
        return maxStack;
    }

    @Override
    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end
}
