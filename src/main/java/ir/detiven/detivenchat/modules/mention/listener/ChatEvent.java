package ir.detiven.detivenchat.modules.mention.listener;

import ir.detiven.detivenchat.DetivenChat;
import ir.detiven.detivenchat.api.event.PlayerMentionEvent;
import ir.detiven.detivenchat.modules.mention.MentionModule;
import ir.detiven.detivenchat.utils.Helper;
import ir.detiven.detivenchat.utils.config.Config;
import ir.detiven.detivenchat.utils.log.Logger;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatEvent implements Listener {

    private final DetivenChat plugin = DetivenChat.getInstance();

    private final Config config = plugin.getPluginConfig();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Player target = null;
        String message = event.getMessage();

        if (event.isCancelled())
            return;
        target = DetivenChat.getEveryone.stream()
                .filter(other -> event.getMessage().toLowerCase().contains(other.getName().toLowerCase()))
                .findFirst()
                .orElse(null);

        if (target != null) {
            if (Helper.isVanished(target)) {
                return;
            }

            if (player.getName().equals(target.getName())) {
                return;
            }

            PlayerMentionEvent mentionEvent = new PlayerMentionEvent(player, target);
            plugin.getServer().getPluginManager().callEvent(mentionEvent);

            if (mentionEvent.isCancelled()) {
                return;
            }

            message = message.replace(
                    target.getName(),
                    config.getMentionColor() + config.getMentionSymbol() + target.getName()
                            + config.getMentionEndColor());

            runAction(player, target, message);
            event.setMessage(message);

        }
    }

    private void runAction(Player player, Player target, String message) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (config.getMentionSound()) {
                    Sound sound = Sound.valueOf(config.getMentionLanguageSoundName());
                    target.playSound(target.getLocation(), sound, 1.0F, 1.0F);
                }

                if (config.getMentionChatMessage()) {
                    target.sendMessage(
                            config.getMentionLanguageChatMessage().replace("%player%", player.getName()));
                }

                if (config.getMentionActionBar()) {
                    target.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            config.getMentionLanguageActionBar().replace("%player%", player.getName())));
                }

                if (config.getMentionTitle()) {
                    String title = config.getMentionLanguageTitle().replace("%player%", player.getName());
                    String subtitle = config.getMentionLanguageSubTitle().replace("%player%", player.getName());
                    int i = Integer.parseInt(config.getMentionTimer()[0]);
                    int j = Integer.parseInt(config.getMentionTimer()[1]);
                    int v = Integer.parseInt(config.getMentionTimer()[2]);
                    target.sendTitle(title, subtitle, i, j, v);
                }
                Logger.add(MentionModule.getInstance().getName(), message);
            }
        }.runTask(plugin);
    }
}
