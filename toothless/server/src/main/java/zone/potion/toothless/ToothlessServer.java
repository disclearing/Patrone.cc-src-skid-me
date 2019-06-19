package zone.potion.toothless;

import zone.potion.toothless.handler.MovementHandler;
import zone.potion.toothless.handler.PacketHandler;

import java.util.HashSet;
import java.util.Set;

public class ToothlessServer {
    private static final ToothlessServer INSTANCE = new ToothlessServer();

    private final Set<PacketHandler> packetHandlers = new HashSet<>();
    private final Set<MovementHandler> movementHandlers = new HashSet<>();

    private ToothlessServer() {
    }

    public static ToothlessServer getInstance() {
        return INSTANCE;
    }

    public void addPacketHandler(PacketHandler handler) {
        packetHandlers.add(handler);
    }

    public Set<PacketHandler> getPacketHandlers() {
        return packetHandlers;
    }

    public void addMovementHandler(MovementHandler handler) {
        movementHandlers.add(handler);
    }

    public Set<MovementHandler> getMovementHandlers() {
        return movementHandlers;
    }
}
