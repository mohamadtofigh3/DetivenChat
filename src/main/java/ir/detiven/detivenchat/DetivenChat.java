package ir.detiven.detivenchat;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;

import ir.detiven.detivenchat.api.API;
import ir.detiven.detivenchat.api.DetivenChatAPI;
import ir.detiven.detivenchat.commands.MainCommand;
import ir.detiven.detivenchat.listener.JoinQuitEvent;
import ir.detiven.detivenchat.modules.antispam.AntiSpamModule;
import ir.detiven.detivenchat.modules.antiswear.AntiSwearModule;
import ir.detiven.detivenchat.modules.chatformat.ChatFormatModule;
import ir.detiven.detivenchat.modules.connection.ConnectionModule;
import ir.detiven.detivenchat.modules.mention.MentionModule;
import ir.detiven.detivenchat.task.SaveDataTask;
import ir.detiven.detivenchat.utils.Helper;
import ir.detiven.detivenchat.utils.config.Config;
import ir.detiven.detivenchat.utils.log.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
public final class DetivenChat extends JavaPlugin {

    @Getter
    private static DetivenChat instance = null;

    public static final Set<Player> getEveryone = new HashSet<>();

    private BukkitCommandManager manager = null;

    public boolean installedPlaceholderAPI = false;

    public List<PluginModule> modules = null;

    public Config pluginConfig = null;

    private boolean active = false;

    @Setter
    public API api = null;

    @Override
    public void onLoad() {
        if (instance == null) {
            instance = this;
            modules = new ArrayList<>();
            pluginConfig = new Config();
            saveDefaultConfig();
            getPluginConfig().init();
            loadModule();
        }
    }

    @Override
    public void onEnable() {
        handleLicense();
        if (isActive()) {
            logger("&8[&3License&8] &alicense is detected. enabling plugin..");
        } else {
            logger("&8[&3License&8] &clicense is invalid or expired!");
            return;
        }

        // set defualt object of api
        setApi(new DetivenChatAPI());

        init();

        logger("&8[&2Core&8] &aloading modules..");
        modules.forEach(this::registerModule);
        logger("&8[&2Core&8] &aloaded modules successfully");
    }

    @Override
    public void onDisable() {
        if (!isActive()) {
            modules.clear();
            return;
        }

        logger("&8[&4Core&8] &cunloading modules..");
        modules.forEach(this::unregisterModule);
        logger("&8[&4Core&8] &cunloaded modules successfully");
        HandlerList.unregisterAll(this);
        getEveryone.clear();
        modules.clear();
        Logger.save();
        instance = null;
    }

    private void loadModule() {
        modules.add(new AntiSpamModule(this));
        modules.add(new AntiSwearModule(this));
        modules.add(new ChatFormatModule(this));
        modules.add(new MentionModule(this));
        modules.add(new ConnectionModule(this));
    }

    private void runTask() {
        if (getPluginConfig().getSettingLogger()) {
            SaveDataTask.isRun = true;
            SaveDataTask task = new SaveDataTask();
            task.runTaskTimer(this, 0L, getPluginConfig().getSettingTimer());
        }
    }

    private void registerCommand() {
        BaseCommand main = new MainCommand();

        manager.registerCommand(main);
    }

    public void logger(String message) {
        getServer().getConsoleSender().sendMessage(Helper.applyColor("&8[&bDetivenChat&8] &7" + message));
    }

    /**
     * this plugin is open source.
     * Github: https://github.com/mohamadtofigh3/DetivenChat
     */
    private void handleLicense() {
        String link = "https://api.craft-tech.xyz/stv1/license.php?license=%s&plugin_name=DetivenChat&server_ip=";
        String server = getPluginConfig().getSettingServer();
        String token = getPluginConfig().getSettingLicense();
        link = String.format(link, token).concat(server);
        if (token.equals("please-enter-license")) {
            logger("&8[&3License&8] &cplease check 'getPluginConfig().yml', and enter your license");
            return;
        }
        if (server.equals("please-enter-ip")) {
            logger("&8[&3License&8] &cplease check 'getPluginConfig().yml', and enter your server ip");
            return;
        }
        if (!Helper.isInternetAvailable()) {
            logger("&8[&3License&8] &cplease check your internet connection!");
            return;
        }
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer content = new StringBuffer();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
            String response = content.toString();
            if (response.contains("\"status\":\"valid\"")) {
                active = true;
            } else if (response
                    .contains("{\"status\":\"invalid\",\"message\":\"License already in use on another server\"}")) {
                logger("&8[&3License&8] &clicense already in use on another server!");
            }
        } catch (Exception e) {
            logger("&8[&3License&8] &cservices is offline, please restart your server in next time!");
        }
        return;
    }

    private void init() {
        installedPlaceholderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        getEveryone.addAll(Bukkit.getOnlinePlayers()); // support pluginmanager
        getServer().getPluginManager().registerEvents(new JoinQuitEvent(), this);
        manager = new BukkitCommandManager(this);
        registerCommand();
        runTask();
    }

    public void registerModule(PluginModule module) {
        try {
            module.load();
            logger("&8[&2" + module.getName() + "&8] &ahas loaded");
        } catch (Exception e) {
            getLogger().severe("&8[&4" + module.getName() + "&8] &cfailed to load");
            e.printStackTrace();
        }
    }

    public void unregisterModule(PluginModule module) {
        try {
            module.shutdown();
            logger("&8[&4" + module.getName() + "&8] &chas unloaded");
        } catch (Exception e) {
            getLogger().severe("&8[&4" + module.getName() + "&8] &cfailed to unload");
            e.printStackTrace();
        }
    }
}