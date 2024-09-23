package ir.detiven.detivenchat.modules.connection.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ir.detiven.detivenchat.DetivenChat;
import ir.detiven.detivenchat.modules.connection.objects.ConnectionAction;
import ir.detiven.detivenchat.modules.connection.objects.ConnectionObject;
import ir.detiven.detivenchat.utils.Helper;
import ir.detiven.detivenchat.utils.config.Config;

public class ConnectionEvent implements Listener {

    private final Config config = DetivenChat.getInstance().config;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ConnectionAction action;

        if (player.hasPlayedBefore()) {
            action = ConnectionAction.JOIN;
        } else {
            action = ConnectionAction.WELCOME;
        }

        String message = this.handleConnection(player, action);
        event.setJoinMessage(message);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String message = this.handleConnection(player, ConnectionAction.QUIT);

        event.setQuitMessage(message);
    }

    private String handleConnection(Player player, ConnectionAction action) {
        String result = "";

        for (ConnectionObject object : config.getConnectionObjects()) {
            String permission = object.getPermission();
            String message = object.getMessage();
            ConnectionAction objectAction = object.getAction();

            if (!objectAction.equals(action)) {
                continue;
            }
            if (permission.isEmpty() || player.hasPermission(object.getPermission())) {
                result = message;
                break;
            }
            continue;
        }

        result = result.replace("%player_name%", player.getName());
        result = config.support(player, result);
        result = Helper.applyColor(result);

        return result;
    }
}