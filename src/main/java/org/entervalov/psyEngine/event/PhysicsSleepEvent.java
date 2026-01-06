package org.entervalov.psyEngine.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.entervalov.psyEngine.core.PhysicsEntity;

public class PhysicsSleepEvent extends PhysicsEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    public PhysicsSleepEvent(PhysicsEntity entity) {
        super(entity);
    }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancel) { this.cancelled = cancel; }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
