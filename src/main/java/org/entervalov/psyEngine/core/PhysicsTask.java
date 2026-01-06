package org.entervalov.psyEngine.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class PhysicsTask {
    private final PhysicsLibrary library;
    private final JavaPlugin plugin;
    private BukkitTask task;

    public PhysicsTask(PhysicsLibrary library) {
        this.library = library;
        this.plugin = library.getPlugin();
    }

    public void start() {
        long updateInterval = library.getConfig().getUpdateInterval();

        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            int activeCount = 0;

            for (PhysicsEntity entity : library.getActiveEntities()) {
                try {
                    if (!entity.getEntity().isDead() && !entity.isDead()) {
                        entity.updatePhysics();
                        activeCount++;

                        if (library.getDebugManager().isDebugEnabled()) {
                            library.getDebugManager().visualize(entity, entity.getAcceleration());
                        }
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning(library.getMessageManager().getMessage("log.physics_error", e.getMessage()));
                }
            }

            if (activeCount > library.getConfig().getMaxActiveEntities()) {
                plugin.getLogger().warning(library.getMessageManager().getMessage("log.limit_warning", activeCount, library.getConfig().getMaxActiveEntities()));
            }

        }, 1L, updateInterval);
    }

    public void cancel() {
        if (task != null) {
            task.cancel();
        }
    }
}
