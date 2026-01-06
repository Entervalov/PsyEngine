package org.entervalov.psyEngine.event;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.entervalov.psyEngine.core.PhysicsEntity;

public class PhysicsCollideEvent extends PhysicsEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Block hitBlock;
    private double impactForce;
    private boolean cancelled;

    public PhysicsCollideEvent(PhysicsEntity entity, Block hitBlock, double impactForce) {
        super(entity);
        this.hitBlock = hitBlock;
        this.impactForce = impactForce;
    }

    public Block getHitBlock() {
        return hitBlock;
    }

    public double getImpactForce() {
        return impactForce;
    }

    public void setImpactForce(double force) {
        this.impactForce = force;
    }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancel) { this.cancelled = cancel; }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
