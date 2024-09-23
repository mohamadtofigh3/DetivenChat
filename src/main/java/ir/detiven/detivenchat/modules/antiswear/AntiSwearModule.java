package ir.detiven.detivenchat.modules.antiswear;

import ir.detiven.detivenchat.PluginModule;
import ir.detiven.detivenchat.modules.antiswear.listener.ChatEvent;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class AntiSwearModule extends PluginModule {

    public AntiSwearModule(Plugin plugin) {
        super(plugin, "AntiSwear");
    }

    private final List<Listener> listeners = new ArrayList<>();

    @Getter
    private static AntiSwearModule instance;

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
