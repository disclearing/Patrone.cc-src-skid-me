package net.minecraft.server;

import java.io.IOException;

public class PacketPlayOutBlockChange implements Packet<PacketListenerPlayOut> {
    public IBlockData block;
    private BlockPosition a;

    public PacketPlayOutBlockChange() {
    }

    public PacketPlayOutBlockChange(World var1, BlockPosition var2) {
        this.a = var2;
        this.block = var1.getType(var2);
    }

    public void a(PacketDataSerializer var1) throws IOException {
        this.a = var1.c();
        this.block = (IBlockData) Block.d.a(var1.e());
    }

    public void b(PacketDataSerializer var1) throws IOException {
        var1.a(this.a);
        var1.b(Block.d.b(this.block));
    }

    public void a(PacketListenerPlayOut var1) {
        var1.a(this);
    }

    public BlockPosition getPosition() {
        return a;
    }
}
