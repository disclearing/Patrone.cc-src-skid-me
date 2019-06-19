package zone.potion.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.potion.CorePlugin;
import zone.potion.event.player.PlayerMessageEvent;
import zone.potion.player.CoreProfile;
import zone.potion.utils.message.CC;

@RequiredArgsConstructor
public class MessageListener implements Listener {
    private final CorePlugin plugin;

    private static void sendMessage(CoreProfile sender, CoreProfile receiver, Player player, String msg) {
        receiver.setConverser(sender.getId());
        player.sendMessage(msg);
    }

    @EventHandler
    public void onMessage(PlayerMessageEvent event) {
        Player sender = event.getPlayer();
        CoreProfile senderProfile = plugin.getProfileManager().getProfile(sender);

        if (!senderProfile.isMessaging() && !senderProfile.hasStaff()) {
            sender.sendMessage(CC.RED + "You have messaging disabled.");
            return;
        }

        Player receiver = event.getReceiver();
        CoreProfile receiverProfile = plugin.getProfileManager().getProfile(receiver);

        if (senderProfile.hasStaff()) {
            // NO-OP
        } else if (!receiverProfile.isMessaging()) {
            sender.sendMessage(CC.RED + receiver.getName() + " has messaging disabled.");
            return;
        }

        String toMsg = CC.GRAY + "(To " + receiverProfile.getPrimaryGroupPrefix() + receiver.getName() + CC.GRAY + ") " + event.getMessage();
        String fromMsg = CC.GRAY + "(From " + senderProfile.getPrimaryGroupPrefix() + sender.getName()+ CC.GRAY + ") " + event.getMessage();

        sendMessage(senderProfile, receiverProfile, receiver, fromMsg);
        sendMessage(receiverProfile, senderProfile, sender, toMsg);

        if (receiverProfile.isPlayingSounds()) {
            receiver.playSound(receiver.getLocation(), Sound.NOTE_PLING, 1.0F, 2.0F);
        }
    }
}
