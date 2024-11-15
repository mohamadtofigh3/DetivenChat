package ir.detiven.detivenchat.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;

@Getter
public class PlayerMentionEvent extends Event implements Cancellable {
    
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    private final Player target;

    private boolean cancelled = false;
    
    public PlayerMentionEvent(Player player, Player target) {
        this.player = player;
        this.target = target;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
    
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}