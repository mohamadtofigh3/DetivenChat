package ir.detiven.detivenchat.modules.antispam;

import ir.detiven.detivenchat.PluginModule;
import ir.detiven.detivenchat.modules.antispam.listener.ChatEvent;
import ir.detiven.detivenchat.modules.antispam.listener.CommandEvent;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class AntiSpamModule extends PluginModule {

    public AntiSpamModule(Plugin plugin) {
        super(plugin, "AntiSpam");
    }

    private final List<Listener> listeners = new ArrayList<>();

    @Getter
    private static AntiSpamModule instance;

    @Override
    public void load() {
        instance = this;
        listeners.add(new ChatEvent());
        listeners.add(new CommandEvent());

        listeners.forEach(this::registerListener);
    }

    @Override
    public void shutdown() {
        listeners.forEach(this::unregisterListener);
        listeners.clear();
    }

    private void registerListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    private void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }
}