package ir.detiven.detivenchat.task;

import ir.detiven.detivenchat.utils.log.Logger;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveDataTask extends BukkitRunnable {
    public static boolean isRun = false;

    @Override
    public void run() {
        if (!isRun) {
            return;
        }
        Logger.save();
    }
}
