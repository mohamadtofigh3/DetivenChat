package ir.detiven.detivenchat.modules.mention.listener;

import ir.detiven.detivenchat.DetivenChat;
import ir.detiven.detivenchat.modules.mention.MentionModule;
import ir.detiven.detivenchat.utils.Helper;
import ir.detiven.detivenchat.utils.config.Config;
import ir.detiven.detivenchat.utils.log.Logger;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatEvent implements Listener {
    private final Config config = DetivenChat.getInstance().config;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Player target = null;
        String message = event.getMessage();

        if (event.isCancelled())
            return;
        for (Player other : DetivenChat.getEveryone) {
            if (message.toLowerCase().contains(other.getName().toLowerCase())) {
                target = other;
                break;
            }
        }

        if (target != null) {
            if (Helper.isVanished(target)) {
                return;
            }

            if (player.getName().equals(target.getName())) {
                return;
            }

            message = message.replace(
                    target.getName(),
                    config.getMentionColor() + config.getMentionSymbol() + target.getName()
                            + config.getMentionEndColor());

            event.setMessage(message);
            Player finalTarget = target;
            String finalMessage = message;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (config.getMentionSound()) {
                        Sound sound = Sound.valueOf(config.getMentionLanguageSoundName());
                        finalTarget.playSound(finalTarget.getLocation(), sound, 1.0F, 1.0F);
                    }

                    if (config.getMentionChatMessage()) {
                        finalTarget.sendMessage(
                                config.getMentionLanguageChatMessage().replace("%player%", player.getName()));
                    }

                    if (config.getMentionActionBar()) {
                        finalTarget.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                                config.getMentionLanguageActionBar().replace("%player%", player.getName())));
                    }

                    if (config.getMentionTitle()) {
                        String title = config.getMentionLanguageTitle().replace("%player%", player.getName());
                        String subtitle = config.getMentionLanguageSubTitle().replace("%player%", player.getName());
                        int i = Integer.parseInt(config.getMentionTimer()[0]);
                        int j = Integer.parseInt(config.getMentionTimer()[1]);
                        int v = Integer.parseInt(config.getMentionTimer()[2]);
                        finalTarget.sendTitle(title, subtitle, i, j, v);
                    }
                    Logger.add(MentionModule.getInstance().getName(), finalMessage);
                }
            }.runTaskLater(DetivenChat.getInstance(), 1);
        }
    }
}
