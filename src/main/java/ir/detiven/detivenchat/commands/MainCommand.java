package ir.detiven.detivenchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import ir.detiven.detivenchat.DetivenChat;
import ir.detiven.detivenchat.task.SaveDataTask;
import ir.detiven.detivenchat.utils.config.Config;
import ir.detiven.detivenchat.utils.log.Logger;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("detivenchat")
@CommandPermission("detivenchat.commands")
public class MainCommand extends BaseCommand {

    private final DetivenChat plugin = DetivenChat.getInstance();

    private final Config config = plugin.getPluginConfig();

    @Subcommand("reload")
    @CommandPermission("detivenchat.command.reload")
    public void reload(CommandSender sender) {
        long time = reload();
        String message = config.getLanguageReload();

        if (message.contains("%s")) {
            message = String.format(message, time);
        }

        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            Location location = player.getLocation();
            Particle particle = Particle.CLOUD;
            Sound sound = Sound.ENTITY_BAT_DEATH;

            player.playSound(location, sound, 1.0F, 1.0F);
            player.spawnParticle(particle, location, 10);
            player.sendMessage(message);
        } else
            sender.sendMessage(message);
    }

    private long reload() {
        long startTime = System.currentTimeMillis();

        Logger.save();
        config.reload();
        if (config.getSettingLogger()) {
            if (!SaveDataTask.isRun) {
                SaveDataTask.isRun = true;
                SaveDataTask task = new SaveDataTask();
                task.runTaskTimer(plugin, 0L, config.getSettingTimer());
            }
        } else {
            if (SaveDataTask.isRun) {
                SaveDataTask.isRun = false;
                SaveDataTask task = new SaveDataTask();
                task.cancel();
            }
        }
        plugin.modules.forEach((plugin::unregisterModule));
        plugin.modules.forEach((plugin::registerModule));

        return Math.abs(startTime - System.currentTimeMillis());
    }

    @Subcommand("help")
    @CommandPermission("detivenchat.command.help")
    public void help(CommandSender sender) {
        config.getLanguageHelp().forEach(sender::sendMessage);
    }
}
