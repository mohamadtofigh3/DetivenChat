package ir.detiven.detivenchat.modules.antiswear.listener;

import ir.detiven.detivenchat.DetivenChat;
import ir.detiven.detivenchat.modules.antiswear.AntiSwearModule;
import ir.detiven.detivenchat.utils.Helper;
import ir.detiven.detivenchat.utils.config.Config;
import ir.detiven.detivenchat.utils.log.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {
    private final Config config = DetivenChat.getInstance().config;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String fake = Helper.fontBreaker(message);

        if (event.isCancelled())
            return;
        if (player.hasPermission(config.getAntiSwearBypassPermission()))
            return;
        fake = format(fake);

        String swear = "";
        boolean detected = false;
        for (String badWord : config.getAntiSwearFilter()) {
            if (detected)
                break;
            for (String word : config.getAntiSwearNotFilter()) {
                if (fake.contains(Helper.lower(badWord)) && !fake.contains(Helper.lower(word))) {
                    swear = badWord;
                    detected = true;
                    break;
                }
            }
        }

        if (detected && !swear.isEmpty()) {
            if (config.getAntiSwearCensor()) {
                int count = swear.length();
                StringBuilder builder = new StringBuilder();

                for (int i = 1; i < count; i++) {
                    builder.append(config.getAntiSwearCensorChar());
                }

                message = message.replace(swear, builder.toString());
                event.setMessage(message);
            } else {
                reportToPlayer(player, swear);
                event.setCancelled(true);
            }
            reportToStaff(player, swear);
        }
    }

    private String format(String fake) {
        if (!config.getAntiSwearReplaceChar().isEmpty()) {
            for (String s : config.getAntiSwearReplaceChar()) {
                String[] list = s.split(":");
                try {
                    String a = list[0];
                    String b = list[1];
                    fake = fake.replaceAll(a, b);
                } catch (IndexOutOfBoundsException ignored) {
                    ;
                }
            }
        }
        fake = Helper.clearCharacter(fake, config.getAntiSwearReplaceAir());
        if (config.getAntiSwearRemoveSpammerChar()) {
            fake = Helper.clearSpammedChar(fake);
        }
        return Helper.lower(fake);
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
        Logger.add(AntiSwearModule.getInstance().getName(), message);
        DetivenChat.getInstance().logger(Helper.applyColor(message));
    }
}