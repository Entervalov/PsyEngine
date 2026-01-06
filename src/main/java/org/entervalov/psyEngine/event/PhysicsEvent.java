package org.entervalov.psyEngine.event;

import org.bukkit.event.Event;
import org.entervalov.psyEngine.core.PhysicsEntity;

public abstract class PhysicsEvent extends Event {
    protected final PhysicsEntity entity;

    public PhysicsEvent(PhysicsEntity entity) {
        this.entity = entity;
    }

    public PhysicsEntity getPhysicsEntity() {
        return entity;
    }
}