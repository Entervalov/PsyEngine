package org.entervalov.psyEngine.test;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.entervalov.psyEngine.api.PhysicsProperties;
import org.entervalov.psyEngine.core.PhysicsLibrary;

public class PhysicsListener implements Listener {

    private final PhysicsLibrary physics;

    public PhysicsListener(PhysicsLibrary physics) {
        this.physics = physics;
    }

    @EventHandler
    public void onArrowShoot(ProjectileLaunchEvent event) {

        if (event.getEntity() instanceof Arrow arrow && arrow.getShooter() instanceof Player) {

            PhysicsProperties arrowProps = new PhysicsProperties()
                    .setMass(0.5f)
                    .setDrag(0.01f)
                    .setBounciness(0.3f);

            physics.spawnCustomEntity(arrow, arrowProps);
        }
    }
}