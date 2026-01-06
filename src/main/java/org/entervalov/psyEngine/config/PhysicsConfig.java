package org.entervalov.psyEngine.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class PhysicsConfig {
    private final JavaPlugin plugin;

    private boolean commandsEnabled;
    private boolean commandTowerEnabled;
    private boolean commandCannonEnabled;
    private boolean commandFireballEnabled;
    private boolean commandGlassEnabled;
    private boolean commandRaftEnabled;
    private boolean commandLandslideEnabled;
    private boolean commandInfoEnabled;
    private boolean commandPushEnabled;
    private boolean commandClearEnabled;
    private boolean commandDebugEnabled;
    private boolean commandReloadEnabled;
    private boolean commandTestEnabled;
    private boolean commandStatsEnabled;
    private boolean requireOp;
    private boolean usePermissions;
    private String permissionPrefix;

    private double gravity;
    private double maxVelocity;
    private double minVelocity;

    private double buoyancyMultiplier;
    private double dragInWaterMultiplier;
    private double splashVelocityThreshold;
    private boolean splashParticles;
    private boolean splashSound;

    private boolean sleepMode;
    private double sleepThreshold;
    private int sleepDelay;
    private boolean solidifyOnSleep;

    private int maxActiveEntities;
    private long updateInterval;
    private int unloadDistance;
    private boolean warnOnLimit;
    private boolean autoCleanup;

    private boolean particlesEnabled;
    private boolean collisionSpark;
    private boolean waterSplash;
    private boolean thermalEffects;
    private boolean sleepIndicator;
    private boolean soundsEnabled;
    private boolean collisionSound;
    private boolean waterSplashSound;
    private boolean thermalSound;

    private String logLevel;
    private boolean logEntitySpawns;
    private boolean logCollisions;
    private boolean logPerformance;

    private double collisionDamageMultiplier;
    private double thermalConductivity;
    private boolean thermalEnabled;
    private boolean allowBlockBreaking;
    private boolean allowBlockPlacement;
    private boolean allowEntityDamage;
    private boolean checkOtherPlugins;
    private boolean safeMode;

    public PhysicsConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        try {
            if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
            plugin.reloadConfig();
            FileConfiguration config = plugin.getConfig();

            commandsEnabled = config.getBoolean("commands.enabled");

            commandTowerEnabled = config.getBoolean("commands.available.tower");
            commandCannonEnabled = config.getBoolean("commands.available.cannon");
            commandFireballEnabled = config.getBoolean("commands.available.fireball");
            commandGlassEnabled = config.getBoolean("commands.available.glass");
            commandRaftEnabled = config.getBoolean("commands.available.raft");
            commandLandslideEnabled = config.getBoolean("commands.available.landslide");
            commandInfoEnabled = config.getBoolean("commands.available.info");
            commandPushEnabled = config.getBoolean("commands.available.push");
            commandClearEnabled = config.getBoolean("commands.available.clear");
            commandDebugEnabled = config.getBoolean("commands.available.debug");
            commandReloadEnabled = config.getBoolean("commands.available.reload");
            commandTestEnabled = config.getBoolean("commands.available.test");
            commandStatsEnabled = config.getBoolean("commands.available.stats");

            requireOp = config.getBoolean("commands.require-op");
            usePermissions = config.getBoolean("commands.use-permissions");
            permissionPrefix = config.getString("commands.permission-prefix");

            gravity = config.getDouble("physics.gravity");
            maxVelocity = config.getDouble("physics.max-velocity");
            minVelocity = config.getDouble("physics.min-velocity");

            buoyancyMultiplier = config.getDouble("water.buoyancy-multiplier");
            dragInWaterMultiplier = config.getDouble("water.drag-in-water-multiplier");
            splashVelocityThreshold = config.getDouble("water.splash-velocity-threshold");
            splashParticles = config.getBoolean("water.splash-particles");
            splashSound = config.getBoolean("water.splash-sound");

            sleepMode = config.getBoolean("optimization.sleep-mode");
            sleepThreshold = config.getDouble("optimization.sleep-threshold");
            sleepDelay = config.getInt("optimization.sleep-delay");
            solidifyOnSleep = config.getBoolean("optimization.solidify-on-sleep");

            maxActiveEntities = config.getInt("performance.max-active-entities");
            updateInterval = config.getLong("performance.update-interval");
            unloadDistance = config.getInt("performance.unload-distance");
            warnOnLimit = config.getBoolean("performance.warn-on-limit");
            autoCleanup = config.getBoolean("performance.auto-cleanup");

            particlesEnabled = config.getBoolean("effects.particles.enabled");
            collisionSpark = config.getBoolean("effects.particles.collision-spark");
            waterSplash = config.getBoolean("effects.particles.water-splash");
            thermalEffects = config.getBoolean("effects.particles.thermal-effects");
            sleepIndicator = config.getBoolean("effects.particles.sleep-indicator");
            soundsEnabled = config.getBoolean("effects.sounds.enabled");
            collisionSound = config.getBoolean("effects.sounds.collision-sound");
            waterSplashSound = config.getBoolean("effects.sounds.water-splash-sound");
            thermalSound = config.getBoolean("effects.sounds.thermal-sound");

            logLevel = config.getString("logging.level");
            logEntitySpawns = config.getBoolean("logging.log-entity-spawns");
            logCollisions = config.getBoolean("logging.log-collisions");
            logPerformance = config.getBoolean("logging.log-performance");

            collisionDamageMultiplier = config.getDouble("advanced.collision-damage-multiplier");
            thermalConductivity = config.getDouble("advanced.thermal-conductivity");
            thermalEnabled = config.getBoolean("advanced.thermal-enabled");
            allowBlockBreaking = config.getBoolean("advanced.allow-block-breaking");
            allowBlockPlacement = config.getBoolean("advanced.allow-block-placement");
            allowEntityDamage = config.getBoolean("advanced.allow-entity-damage");
            checkOtherPlugins = config.getBoolean("advanced.check-other-plugins");
            safeMode = config.getBoolean("advanced.safe-mode");

            plugin.getLogger().info("§a✓ The configu is loaded!");
            logConfigStatus();
        } catch (Exception e) {
            plugin.getLogger().warning("Config error: " + e.getMessage());
        }
    }

    private void logConfigStatus() {
        if (commandsEnabled) {
            plugin.getLogger().info("[Commands] ENABLED - The built-in commands are activated (only for test!)");
        } else {
            plugin.getLogger().info("[Commands] DISABLED - The built-in commands are disabled used as a library");
        }

        if (safeMode) {
            plugin.getLogger().info("⚠️ Safe mode IS ACTIVE");
        }
    }


    public boolean isCommandsEnabled() { return commandsEnabled; }
    public boolean isCommandEnabled(String cmd) {
        return switch (cmd.toLowerCase()) {
            case "tower" -> commandTowerEnabled;
            case "cannon" -> commandCannonEnabled;
            case "fireball" -> commandFireballEnabled;
            case "glass" -> commandGlassEnabled;
            case "raft" -> commandRaftEnabled;
            case "landslide" -> commandLandslideEnabled;
            case "info" -> commandInfoEnabled;
            case "push" -> commandPushEnabled;
            case "clear" -> commandClearEnabled;
            case "debug" -> commandDebugEnabled;
            case "reload" -> commandReloadEnabled;
            case "test" -> commandTestEnabled;
            case "stats" -> commandStatsEnabled;
            default -> false;
        };
    }
    public boolean isRequireOp() { return requireOp; }
    public boolean isUsePermissions() { return usePermissions; }
    public String getPermissionPrefix() { return permissionPrefix; }

    public double getGravity() { return gravity; }
    public double getMaxVelocity() { return maxVelocity; }
    public double getMinVelocity() { return minVelocity; }

    public double getBuoyancyMultiplier() { return buoyancyMultiplier; }
    public double getDragInWaterMultiplier() { return dragInWaterMultiplier; }
    public double getSplashVelocityThreshold() { return splashVelocityThreshold; }
    public boolean isSplashParticles() { return splashParticles && particlesEnabled; }
    public boolean isSplashSound() { return splashSound && soundsEnabled; }

    public boolean isSleepMode() { return sleepMode; }
    public double getSleepThreshold() { return sleepThreshold; }
    public int getSleepDelay() { return sleepDelay; }
    public boolean isSolidifyOnSleep() { return solidifyOnSleep && allowBlockPlacement; }

    public int getMaxActiveEntities() { return maxActiveEntities; }
    public long getUpdateInterval() { return updateInterval; }
    public int getUnloadDistance() { return unloadDistance; }
    public boolean isWarnOnLimit() { return warnOnLimit; }
    public boolean isAutoCleanup() { return autoCleanup; }

    public boolean isParticlesEnabled() { return particlesEnabled; }
    public boolean isCollisionSpark() { return collisionSpark && particlesEnabled; }
    public boolean isWaterSplash() { return waterSplash && particlesEnabled; }
    public boolean isThermalEffects() { return thermalEffects && particlesEnabled; }
    public boolean isSleepIndicator() { return sleepIndicator && particlesEnabled; }
    public boolean isSoundsEnabled() { return soundsEnabled; }
    public boolean isCollisionSound() { return collisionSound && soundsEnabled; }
    public boolean isWaterSplashSound() { return waterSplashSound && soundsEnabled; }
    public boolean isThermalSound() { return thermalSound && soundsEnabled; }

    public String getLogLevel() { return logLevel; }
    public boolean isLogEntitySpawns() { return logEntitySpawns; }
    public boolean isLogCollisions() { return logCollisions; }
    public boolean isLogPerformance() { return logPerformance; }

    public double getCollisionDamageMultiplier() { return collisionDamageMultiplier; }
    public double getThermalConductivity() { return thermalConductivity; }
    public boolean isThermalEnabled() { return thermalEnabled; }
    public boolean isAllowBlockBreaking() { return allowBlockBreaking; }
    public boolean isAllowBlockPlacement() { return allowBlockPlacement; }
    public boolean isAllowEntityDamage() { return allowEntityDamage; }
    public boolean isCheckOtherPlugins() { return checkOtherPlugins; }
    public boolean isSafeMode() { return safeMode; }
}