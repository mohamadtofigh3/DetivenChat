package ir.detiven.detivenchat.modules.chatformat.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ir.detiven.detivenchat.DetivenChat;
import ir.detiven.detivenchat.utils.Helper;
import ir.detiven.detivenchat.utils.config.Config;

public class ChatEvent implements Listener {

    private final Config config = DetivenChat.getInstance().config;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String format = config.getChatFormat();

        if (event.isCancelled()) {
            return;
        }
        if (format == null) {
            return;
        }

        format = format.replace("%player_name%", player.getName());
        format = format.replace("%message%", message);
        format = config.support(player, format);
        format = Helper.applyColor(format);

        event.setFormat(format);
    }

}
