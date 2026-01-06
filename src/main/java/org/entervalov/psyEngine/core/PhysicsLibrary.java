package org.entervalov.psyEngine.core;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.entervalov.psyEngine.api.PhysicsProperties;
import org.entervalov.psyEngine.config.PhysicsConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.entervalov.psyEngine.material.MaterialsConfig;
import org.entervalov.psyEngine.message.MessageManager;
import org.entervalov.psyEngine.utils.PhysicsLogger;

import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class PhysicsLibrary {
    private static PhysicsLibrary instance;
    private final JavaPlugin plugin;

    private final PhysicsConfig config;
    private final PhysicsRegistry registry;
    private final DebugManager debugManager;
    private final MessageManager messageManager;

    private final Map<UUID, PhysicsEntity> entities = new ConcurrentHashMap<>();
    private final PhysicsTask task;
    private PhysicsLogger customLogger;

    public PhysicsLibrary(JavaPlugin plugin) {
        instance = this;
        this.plugin = plugin;

        this.config = new PhysicsConfig(plugin);
        this.registry = new PhysicsRegistry();
        MaterialsConfig materialsConfig = new MaterialsConfig(plugin, registry);
        this.debugManager = new DebugManager();
        this.messageManager = new MessageManager(plugin);

        long startTime = System.currentTimeMillis();

        config.loadConfig();
        PhysicsLogger customLogger = new PhysicsLogger(plugin, config.getLogLevel());
        materialsConfig.load();
        messageManager.loadMessages();

        task = new PhysicsTask(this);
        task.start();

        long loadTime = System.currentTimeMillis() - startTime;
        plugin.getLogger().info("✓ PhysicsLibrary initialized for " + loadTime + "ms");
    }

    public void logToFile(String msg) {
        if (customLogger != null) {
            customLogger.log(msg);
        }
    }

    public static PhysicsLibrary getInstance() {
        return instance;
    }

    public PhysicsEntity spawnPhysicsBlock(Location loc, Material material) {
        if (entities.size() >= config.getMaxActiveEntities()) {
            return null;
        }

        FallingBlock fb = Objects.requireNonNull(loc.getWorld()).spawnFallingBlock(loc, material.createBlockData());
        fb.setDropItem(false);
        fb.setGravity(false);
        fb.setHurtEntities(false);

        PhysicsProperties props = registry.get(material);
        PhysicsEntity entity = new PhysicsEntity(fb, props, config);
        entities.put(fb.getUniqueId(), entity);

        return entity;
    }

    public PhysicsEntity spawnCustomEntity(Entity entity, PhysicsProperties customProps) {
        if (entities.size() >= config.getMaxActiveEntities()) {
            return null;
        }

        PhysicsEntity physEntity = new PhysicsEntity(entity, customProps, config);
        entities.put(entity.getUniqueId(), physEntity);
        return physEntity;
    }

    public void removePhysicsEntity(UUID uuid) {
        PhysicsEntity removed = entities.remove(uuid);
        if (removed != null) removed.kill();
    }

    public void disable() {
        if (task != null) task.cancel();

        if (customLogger != null) customLogger.close();

        entities.values().forEach(PhysicsEntity::kill);
        entities.clear();
        plugin.getLogger().info(messageManager.getMessage("shutdown.disabled"));
    }

    public JavaPlugin getPlugin() { return plugin; }
    public PhysicsConfig getConfig() { return config; }
    public DebugManager getDebugManager() { return debugManager; }
    public PhysicsRegistry getRegistry() { return registry; }
    public MessageManager getMessageManager() { return messageManager; }
    public Map<UUID, PhysicsEntity> getEntities() { return entities; }
    public Collection<PhysicsEntity> getActiveEntities() { return entities.values(); }
    public int getActiveEntityCount() { return entities.size(); }
}
}
