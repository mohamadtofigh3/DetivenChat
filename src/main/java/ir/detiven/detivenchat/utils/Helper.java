package ir.detiven.detivenchat.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import com.ibm.icu.text.Transliterator;

import ir.detiven.detivenchat.DetivenChat;

import java.net.Socket;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Helper {

    public static String applyColor(String str) {
        return ChatColor.translateAlternateColorCodes('&', str).trim();
    }

    public static boolean isVanished(Player player) {
        Iterator<MetadataValue> metadata = player.getMetadata("vanished").iterator();

        MetadataValue meta;
        do {
            if (!metadata.hasNext()) {
                return false;
            }

            meta = metadata.next();
        } while (!meta.asBoolean());

        return true;
    }

    public static String clearSpammedChar(String str) {
        if (str.isEmpty())
            return str;

        StringBuilder builder = new StringBuilder();
        char o = '-';

        for (Character c : str.toCharArray()) {
            if (o == c)
                continue;
            o = c;
            builder.append(c);
        }
        return builder.toString().trim();
    }

    public static boolean isInternetAvailable() {
        try {
            Socket socket = new Socket();
            boolean result;
            try {
                socket.connect(new InetSocketAddress("google.com", 80), 3000);
                result = true;
            } catch (Throwable t) {
                try {
                    socket.close();
                } catch (Throwable th) {
                    t.addSuppressed(th);
                }
                throw t;
            }
            socket.close();
            return result;
        } catch (IOException var0) {
            return false;
        }
    }

    public static String clearCharacter(String str, List<String> list) {
        for (String s : list) {
            str = str.replace(s, "");
        }
        return str.trim();
    }

    public static String fontBreaker(String font) {
        if (!DetivenChat.getInstance().config.getAntiSwearFontReplacer()) {
            return font;
        }
        Transliterator transliterator = Transliterator.getInstance("Any-Latin; Latin-ASCII");
        return transliterator.transliterate(font);
    }

    public static String lower(String str) {
        return str.toLowerCase(Locale.ENGLISH).trim();
    }

    public static String upper(String str) {
        return str.toUpperCase(Locale.ENGLISH).trim();
    }
}