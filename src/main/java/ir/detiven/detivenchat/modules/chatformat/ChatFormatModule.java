package ir.detiven.detivenchat.modules.chatformat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import ir.detiven.detivenchat.PluginModule;
import ir.detiven.detivenchat.modules.chatformat.listener.ChatEvent;
import lombok.Getter;

public class ChatFormatModule extends PluginModule {

    public ChatFormatModule(Plugin plugin) {
        super(plugin, "ChatFormat");
    }

    private final List<Listener> listeners = new ArrayList<>();

    @Getter
    private static ChatFormatModule instance;

    @Override
    public void load() {
        instance = this;
        listeners.add(new ChatEvent());

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
