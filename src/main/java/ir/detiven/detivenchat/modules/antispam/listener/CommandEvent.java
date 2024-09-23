package ir.detiven.detivenchat.modules.antispam.listener;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import ir.detiven.detivenchat.DetivenChat;
import ir.detiven.detivenchat.utils.config.Config;

public class CommandEvent implements Listener {

    private final Config config = DetivenChat.getInstance().config;

    private final long cooldown = config.getAntiSpamCooldown();

    private final Cache<Player, Long> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(cooldown, TimeUnit.MILLISECONDS).build();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        long current = System.currentTimeMillis();
        Long time = cache.asMap().get(player);

        if (event.isCancelled())
            return;
        if (cooldown == 0)
            return;
        if (!config.getAntiSpamCommand())
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