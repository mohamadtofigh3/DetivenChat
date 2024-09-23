package ir.detiven.detivenchat.modules.connection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import ir.detiven.detivenchat.PluginModule;
import ir.detiven.detivenchat.modules.connection.listener.ConnectionEvent;
import lombok.Getter;

public class ConnectionModule extends PluginModule {

    public ConnectionModule(Plugin plugin) {
        super(plugin, "Connection");
    }

    private final List<Listener> listeners = new ArrayList<>();

    @Getter
    private static ConnectionModule instance;

    @Override
    public void load() {
        instance = this;
        listeners.add(new ConnectionEvent());

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