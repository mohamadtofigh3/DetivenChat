package ir.detiven.detivenchat.api;

import java.util.Objects;

import ir.detiven.detivenchat.DetivenChat;
import ir.detiven.detivenchat.modules.antiswear.objects.SwearObject;
import ir.detiven.detivenchat.utils.Helper;
import ir.detiven.detivenchat.utils.config.Config;

public class DetivenChatAPI implements API {

    private final DetivenChat plugin = DetivenChat.getInstance();

    private final Config config = plugin.getPluginConfig();

    @Override
    public SwearObject isSwear(String str) {
        return isSwear(str, false);
    }

    @Override
    public SwearObject isSwear(String str, boolean detectWithFont) {
        return isSwear(str, detectWithFont, false);
    }

    @Override
    public SwearObject isSwear(String str, boolean detectWithFont, boolean clearSpammedChar) {
        return isSwear(str, detectWithFont, clearSpammedChar, false);
    }

    @Override
    public SwearObject isSwear(String str, boolean detectWithFont, boolean clearSpammedChar, boolean replacer) {
        return isSwear(str, detectWithFont, clearSpammedChar, replacer, false);
    }

    @Override
    public SwearObject isSwear(String str, boolean detectWithFont, boolean clearSpammedChar, boolean replacer, boolean clearCharacter) {
        Objects.requireNonNull(str, "object str is null!");
        SwearObject object = new SwearObject(false, "");
        if (str.isEmpty()) {
            return object;
        }

        if (detectWithFont) {
            str = Helper.fontBreaker(str);
        }

        str = Helper.lower(str);
        if (replacer) {
            if (!config.getAntiSwearReplaceChar().isEmpty()) {
                for (String s : config.getAntiSwearReplaceChar()) {
                    try {
                        String[] list = s.split(":");

                        String a = list[0];
                        String b = list[1];
                        str = str.replaceAll(a, b);
                    } catch (Exception e) {
                        DetivenChat.getInstance().logger("ReplacerChar is bad format detected!");
                        e.printStackTrace();
                    }
                }
            }
        }

        if (clearSpammedChar) {
            str = Helper.clearSpammedChar(str);
        }

        if (clearCharacter) {
            str = Helper.clearCharacter(str, config.getAntiSwearReplaceAir());
        }

        String swear = "";
        boolean detected = false;
        for (String badWord : config.getAntiSwearFilter()) {
            if (detected)
                break;
            for (String word : config.getAntiSwearNotFilter()) {
                if (str.contains(Helper.lower(badWord)) && !str.contains(Helper.lower(word))) {
                    swear = badWord;
                    detected = true;
                    break;
                }
            }
        }

        if (detected && !swear.isEmpty()) {
            object = new SwearObject(true, swear);
        } 
        return object;
    }

}