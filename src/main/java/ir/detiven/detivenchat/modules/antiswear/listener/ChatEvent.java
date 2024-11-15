package ir.detiven.detivenchat.modules.antiswear.listener;

import ir.detiven.detivenchat.DetivenChat;
import ir.detiven.detivenchat.api.event.PlayerSwearEvent;
import ir.detiven.detivenchat.modules.antiswear.AntiSwearModule;
import ir.detiven.detivenchat.modules.antiswear.objects.SwearObject;
import ir.detiven.detivenchat.utils.Helper;
import ir.detiven.detivenchat.utils.config.Config;
import ir.detiven.detivenchat.utils.log.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    private final AntiSwearModule module = AntiSwearModule.getInstance();

    private final DetivenChat plugin = DetivenChat.getInstance();

    private final Config config = plugin.getPluginConfig();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (event.isCancelled())
            return;
        if (player.hasPermission(config.getAntiSwearBypassPermission()))
            return;

        SwearObject object = plugin.getApi().isSwear(
                message,
                config.getAntiSwearFontReplacer(),
                config.getAntiSwearRemoveSpammerChar(),
                !config.getAntiSwearReplaceChar().isEmpty(),
                !config.getAntiSwearReplaceAir().isEmpty());

        if (object.isSwear()) {
            PlayerSwearEvent swearEvent = new PlayerSwearEvent(player, message, object.getWord());
            Bukkit.getPluginManager().callEvent(swearEvent);

            if (swearEvent.isCancelled()) {
                return;
            }

            if (config.getAntiSwearCensor()) {
                int count = object.getWord().length();
                StringBuilder builder = new StringBuilder();

                for (int i = 1; i < count; i++) {
                    builder.append(config.getAntiSwearCensorChar());
                }

                message = message.replace(object.getWord(), builder.toString());
                event.setMessage(message);
            } else {
                reportToPlayer(player, object.getWord());
                event.setCancelled(true);
            }
            reportToStaff(player, object.getWord());
        }
    }

    private void reportToPlayer(Player player, String swear) {
        if (config.getAntiSwearMessagePlayer().isEmpty())
            return;
        String message = config.getAntiSwearMessagePlayer().replace("%player%", player.getName()).replace(
                "%filtered%",
                swear);
        player.sendMessage(message);
    }

    private void reportToStaff(Player player, String swear) {
        String message = config.getAntiSwearMessageStaff().replace("%player%", player.getName()).replace(
                "%filtered%",
                swear);

        if (config.getAntiSwearMessagePlayer().isEmpty())
            return;
        for (Player staff : DetivenChat.getEveryone) {
            if (!staff.hasPermission(config.getAntiSwearStaffPermission()))
                continue;
            if (staff.equals(player))
                continue;

            staff.sendMessage(message);
        }
        Logger.add(module.getName(), message);
        DetivenChat.getInstance().logger(Helper.applyColor(message));
    }
}