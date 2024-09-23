package ir.detiven.detivenchat.utils.log;

import ir.detiven.detivenchat.DetivenChat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Logger {

    private final static Map<String, List<String>> log = new HashMap<>();

    public static void add(String module, String message) {
        List<String> list = log.get(module);

        if (list == null)
            list = new ArrayList<>();

        message = format(module, message);
        list.add(message);
        log.put(module, list);
    }

    private static String format(String module, String message) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H/m/s");
        String date = now.format(dateFormatter);
        String time = now.format(timeFormatter);
        StringBuilder builder = new StringBuilder("[").append(date)
                .append("] [").append(time)
                .append("] [").append(module)
                .append("] ").append(message);

        return builder.toString();
    }

    public static void clear(String module) {
        List<String> list = log.get(module);

        if (list == null)
            return;

        list.clear();
    }

    public static void clear() {
        if (log.isEmpty())
            return;
        log.clear();
    }

    public static void save() {
        for (Map.Entry<String, List<String>> entry : log.entrySet()) {
            List<String> value = entry.getValue();

            if (value.isEmpty())
                continue;
            value.forEach(Logger::logToFile);
        }
        DetivenChat.getInstance().logger("saved log success.");
        clear();
    }

    public static void save(String module) {
        List<String> list = log.get(module);

        if (list == null || list.isEmpty())
            return;

        list.forEach(Logger::logToFile);
        DetivenChat.getInstance().logger("saved log success.");
        clear(module);
    }

    private static void logToFile(String message) {
        try {
            File dataFolder = DetivenChat.getInstance().getDataFolder();
            dataFolder.mkdir();
            File saveTo = new File(dataFolder, "log.txt");
            if (!saveTo.exists()) {
                saveTo.createNewFile();
            }

            FileWriter fw = new FileWriter(saveTo, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(message);
            pw.flush();
            pw.close();
        } catch (IOException ignored) {
        }
    }
}