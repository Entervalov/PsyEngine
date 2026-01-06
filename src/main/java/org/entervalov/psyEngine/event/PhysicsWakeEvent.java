package org.entervalov.psyEngine.event;

import org.bukkit.event.HandlerList;
import org.entervalov.psyEngine.core.PhysicsEntity;

public class PhysicsWakeEvent extends PhysicsEvent {
    private static final HandlerList handlers = new HandlerList();
    private final String reason;

    public PhysicsWakeEvent(PhysicsEntity entity, String reason) {
        super(entity);
        this.reason = reason;
    }

    public String getReason() { return reason; }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
