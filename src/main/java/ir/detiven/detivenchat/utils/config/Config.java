package ir.detiven.detivenchat.utils.config;

import ir.detiven.detivenchat.DetivenChat;
import ir.detiven.detivenchat.modules.connection.objects.ConnectionAction;
import ir.detiven.detivenchat.modules.connection.objects.ConnectionObject;
import ir.detiven.detivenchat.utils.Helper;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PUBLIC)
public class Config {

    private Boolean settingLogger;
    private Integer settingTimer;
    private String settingServer;
    private String settingLicense;

    private String languageReload;
    private final List<String> languageHelp = new ArrayList<>();

    private List<ConnectionObject> connectionObjects;

    private String chatFormat;

    private String mentionSymbol;
    private String mentionColor;
    private String mentionEndColor;
    private Boolean mentionChatMessage;
    private Boolean mentionActionBar;
    private Boolean mentionTitle;
    private String[] mentionTimer;
    private Boolean mentionSound;
    private String mentionLanguageSoundName;
    private String mentionLanguageChatMessage;
    private String mentionLanguageActionBar;
    private String mentionLanguageTitle;
    private String mentionLanguageSubTitle;

    private Boolean antiSpamChat;
    private Boolean antiSpamCommand;
    private Integer antiSpamCooldown;
    private String antiSpamMessageCooldown;
    private String antiSpamBypassPermission;

    private Boolean antiSwearCensor;
    private Boolean antiSwearRemoveSpammerChar;
    private Boolean antiSwearFontReplacer;
    private String antiSwearCensorChar;
    private String antiSwearMessageStaff;
    private String antiSwearMessagePlayer;
    private String antiSwearStaffPermission;
    private String antiSwearBypassPermission;
    private List<String> antiSwearReplaceChar = new ArrayList<>();
    private List<String> antiSwearReplaceAir = new ArrayList<>();
    private List<String> antiSwearNotFilter = new ArrayList<>();
    private List<String> antiSwearFilter = new ArrayList<>();

    public void init() {
        FileConfiguration config = DetivenChat.getInstance().getConfig();

        this.settingServer = config.getString("Server");
        this.settingLicense = config.getString("License");
        this.settingLogger = config.getBoolean("Logger");
        this.settingTimer = config.getInt("Timer") * 20 * 60;

        this.languageReload = Helper.applyColor(config.getString("Language.Reload"));
        config.getStringList("Language.Help").forEach((message) -> this.languageHelp.add(Helper.applyColor(message)));

        ConfigurationSection connectionKey = config.getConfigurationSection("Module.ConnectionMessage.Messages");
        this.connectionObjects = new ArrayList<>();
        for (String key : connectionKey.getKeys(false)) {
            String permission = config.getString(("Module.ConnectionMessage.Messages." + key + ".Permission"));
            String message = config.getString(("Module.ConnectionMessage.Messages." + key + ".Message"));
            String action = config.getString(("Module.ConnectionMessage.Messages." + key + ".Action"));
            ConnectionObject object = new ConnectionObject(
                    ConnectionAction.valueOf(action.toUpperCase()),
                    permission,
                    message);

            connectionObjects.add(object);
        }

        this.chatFormat = config.getString("Module.ChatFormat.Format");

        this.mentionSymbol = config.getString("Module.Mention.Symbol");
        this.mentionColor = config.getString("Module.Mention.Color");
        this.mentionEndColor = config.getString("Module.Mention.EndColor");
        this.mentionChatMessage = config.getBoolean("Module.Mention.SendChatMessage");
        this.mentionActionBar = config.getBoolean("Module.Mention.SendActionBar");
        this.mentionTitle = config.getBoolean("Module.Mention.SendTitle");
        this.mentionTimer = config.getString("Module.Mention.TitleTimer").split(",");
        this.mentionSound = config.getBoolean("Module.Mention.SendSound");
        this.mentionLanguageSoundName = config.getString("Module.Mention.SoundName");
        this.mentionLanguageChatMessage = Helper.applyColor(config.getString("Module.Mention.MessageChat"));
        this.mentionLanguageActionBar = Helper.applyColor(config.getString("Module.Mention.MessageActionBar"));
        this.mentionLanguageTitle = Helper.applyColor(config.getString("Module.Mention.MessageTitle"));
        this.mentionLanguageSubTitle = Helper.applyColor(config.getString("Module.Mention.MessageSubTitle"));

        this.antiSpamChat = config.getBoolean("Module.AntiSpam.Chat");
        this.antiSpamCommand = config.getBoolean("Module.AntiSpam.Command");
        this.antiSpamCooldown = config.getInt("Module.AntiSpam.Cooldown");
        this.antiSpamMessageCooldown = Helper.applyColor(config.getString("Module.AntiSpam.MessageCooldown"));
        this.antiSpamBypassPermission = Helper.applyColor(config.getString("Module.AntiSpam.BypassPermission"));

        this.antiSwearCensor = config.getBoolean("Module.AntiSwear.Censor");
        this.antiSwearRemoveSpammerChar = config.getBoolean("Module.AntiSwear.RemoveSpammerChar");
        this.antiSwearFontReplacer = config.getBoolean("Module.AntiSwear.FontReplacer");
        this.antiSwearCensorChar = config.getString("Module.AntiSwear.CensorChar");
        this.antiSwearMessageStaff = Helper.applyColor(config.getString("Module.AntiSwear.StaffMessage"));
        this.antiSwearMessagePlayer = Helper.applyColor(config.getString("Module.AntiSwear.PlayerMessage"));
        this.antiSwearStaffPermission = config.getString("Module.AntiSwear.StaffPermission");
        this.antiSwearBypassPermission = config.getString("Module.AntiSwear.BypassPermission");
        this.antiSwearReplaceChar = config.getStringList("Module.AntiSwear.ReplaceChar");
        this.antiSwearReplaceAir = config.getStringList("Module.AntiSwear.ReplaceAir");
        this.antiSwearNotFilter = config.getStringList("Module.AntiSwear.NotFilter");
        this.antiSwearFilter = config.getStringList("Module.AntiSwear.Filter");
    }

    public void reload() {
        this.antiSwearReplaceAir.clear();
        this.antiSwearNotFilter.clear();
        this.connectionObjects.clear();
        this.antiSwearFilter.clear();
        this.languageHelp.clear();
        init();
    }

    public String support(Player player, String message) {
        if (DetivenChat.getInstance().isInstalledPlaceholderAPI()) {
            return PlaceholderAPI.setPlaceholders(player, message);
        }
        return message;
    }
}
