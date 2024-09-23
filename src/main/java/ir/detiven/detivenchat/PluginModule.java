package ir.detiven.detivenchat;

import org.bukkit.plugin.Plugin;

public abstract class PluginModule {
    public final Plugin plugin;
    private final String name;

    public PluginModule(Plugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract void load();

    public abstract void shutdown();
}
