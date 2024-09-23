package ir.detiven.detivenchat.modules.mention;

import ir.detiven.detivenchat.PluginModule;
import ir.detiven.detivenchat.modules.mention.listener.ChatEvent;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class MentionModule extends PluginModule {

    public MentionModule(Plugin plugin) {
        super(plugin, "Mention");
    }

    private final List<Listener> listeners = new ArrayList<>();

    @Getter
    private static MentionModule instance;

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
