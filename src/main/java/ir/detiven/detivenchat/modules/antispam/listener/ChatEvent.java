package ir.detiven.detivenchat.modules.antispam.listener;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import ir.detiven.detivenchat.DetivenChat;
import ir.detiven.detivenchat.utils.config.Config;

public class ChatEvent implements Listener {

    private final Config config = DetivenChat.getInstance().config;

    private final long cooldown = TimeUnit.SECONDS.toMillis(config.getAntiSpamCooldown());

    private final Cache<Player, Long> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(cooldown, TimeUnit.MILLISECONDS).build();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        long current = System.currentTimeMillis();
        Long time = cache.asMap().get(player);

        if (event.isCancelled())
            return;
        if (cooldown == 0)
            return;
        if (!config.getAntiSpamChat())
            return;
        if (player.hasPermission(config.getAntiSpamBypassPermission()))
            return;
        if (time == null) {
            cache.put(player, current + cooldown);
        } else if (time > current) {
            double seconds = (time - current) / 1000.0;
            DecimalFormat formatted = new DecimalFormat("#.##");

            player.sendMessage(config.getAntiSpamMessageCooldown().replace("%cooldown%",
                    formatted.format(seconds)));
            event.setCancelled(true);
        }
    }
}
