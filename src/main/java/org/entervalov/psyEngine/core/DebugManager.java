package org.entervalov.psyEngine.core;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.entervalov.psyEngine.message.MessageManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DebugManager {
    private final Set<UUID> debugPlayers = new HashSet<>();
    private final Particle.DustOptions VELOCITY_COLOR = new Particle.DustOptions(Color.GREEN, 0.8f);
    private final Particle.DustOptions ACCEL_COLOR = new Particle.DustOptions(Color.RED, 0.8f);
    private final Particle.DustOptions SLEEPING_COLOR = new Particle.DustOptions(Color.YELLOW, 0.6f);

    public void toggleDebug(Player player) {
        MessageManager msg = PhysicsLibrary.getInstance().getMessageManager();

        if (debugPlayers.contains(player.getUniqueId())) {
            debugPlayers.remove(player.getUniqueId());
            player.sendMessage(msg.getMessage("command.debug.off"));
        } else {
            debugPlayers.add(player.getUniqueId());
            player.sendMessage(msg.getMessage("command.debug.on"));
            player.sendMessage(msg.getMessage("command.debug.info"));
        }
    }

    public boolean isDebugEnabled() {
        return !debugPlayers.isEmpty();
    }

    public void visualize(PhysicsEntity entity, Vector acceleration) {
        if (debugPlayers.isEmpty()) return;

        Location loc = entity.getEntity().getLocation().add(0, 0.5, 0);

        Vector vel = entity.getVelocity().clone().multiply(2);
        if (vel.length() > 0.1) {
            drawLine(loc.clone(), vel, VELOCITY_COLOR);
        }

        if (acceleration.lengthSquared() > 0.0001) {
            Vector acc = acceleration.clone().multiply(5);
            drawLine(loc.clone().add(0, 0.1, 0), acc, ACCEL_COLOR);
        }

        if (entity.isSleeping()) {
            for (UUID uuid : debugPlayers) {
                Player p = org.bukkit.Bukkit.getPlayer(uuid);
                if (p != null && p.getWorld().equals(loc.getWorld()) && p.getLocation().distance(loc) < 100) {
                    p.spawnParticle(Particle.REDSTONE, loc, 3, 0, 0, 0, 0.5, SLEEPING_COLOR);
                }
            }
        }
    }

    private void drawLine(Location start, Vector direction, Particle.DustOptions color) {
        if (direction.length() < 0.05) return;

        double length = direction.length();
        double step = 0.15;
        Vector dirNorm = direction.clone().normalize().multiply(step);

        for (double d = 0; d < length; d += step) {
            for (UUID uuid : debugPlayers) {
                Player p = org.bukkit.Bukkit.getPlayer(uuid);
                if (p != null && p.getWorld().equals(start.getWorld()) && p.getLocation().distance(start) < 100) {
                    p.spawnParticle(Particle.REDSTONE, start, 1, 0, 0, 0, 0, color);
                }
            }
            start.add(dirNorm);
        }
    }
}