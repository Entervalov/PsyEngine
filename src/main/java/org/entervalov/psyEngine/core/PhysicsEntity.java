package org.entervalov.psyEngine.core;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.entervalov.psyEngine.api.PhysicsProperties;
import org.entervalov.psyEngine.config.PhysicsConfig;

import org.bukkit.block.Block;
import org.entervalov.psyEngine.event.PhysicsCollideEvent;
import org.entervalov.psyEngine.event.PhysicsSleepEvent;
import org.entervalov.psyEngine.event.PhysicsWakeEvent;

import java.util.Objects;

@SuppressWarnings("unused")
public class PhysicsEntity {

    private final Entity entity;
    private final PhysicsProperties properties;
    private final PhysicsConfig config;

    private Vector velocity;
    private final Vector acceleration = new Vector();

    private boolean onGround = false;
    private boolean inWater = false;
    private boolean isDead = false;

    private boolean sleeping = false;
    private int sleepTimer = 0;

    private double currentTemperature = 20.0;

    private boolean damageOnImpact = false;
    private double impactDamageMultiplier = 1.0;

    public PhysicsEntity(Entity entity, PhysicsProperties properties, PhysicsConfig config) {
        this.entity = Objects.requireNonNull(entity, "entity");
        this.config = Objects.requireNonNull(config, "config");
        this.properties = Objects.requireNonNull(properties, "properties").clone();

        entity.getVelocity();
        this.velocity = entity.getVelocity().clone();
    }

    public void updatePhysics() {
        if (isDead || entity.isDead()) {
            kill();
            return;
        }

        if (sleeping) {
            entity.getVelocity();
            if (entity.getVelocity().lengthSquared() > 0.01) {
                velocity = entity.getVelocity().clone();
                wakeUp("EXTERNAL_VELOCITY");
            } else if (velocity.lengthSquared() > 0.01) {
                wakeUp("INTERNAL_VELOCITY");
            } else {
                entity.setVelocity(new Vector(0, 0, 0));

                if (config.isSolidifyOnSleep()) {
                    trySolidify();
                }
                return;
            }
        }

        acceleration.zero();
        Location loc = entity.getLocation();

        checkWater(loc);

        applyGravity();
        applyBuoyancy();
        applyDrag();
        applyFriction();
        applyThermodynamics(loc);

        if (isDead) return;

        handleCollisions(loc);

        velocity.add(acceleration);

        if (velocity.length() < config.getMinVelocity()) {
            velocity.zero();
        }

        if (velocity.length() > config.getMaxVelocity()) {
            velocity.normalize().multiply(config.getMaxVelocity());
        }

        entity.setVelocity(velocity);

        checkSleep();

        checkOutOfBounds(loc);
    }

    private void checkSleep() {
        if (!config.isSleepMode()) return;
        if (!onGround) {
            sleepTimer = 0;
            return;
        }

        double thr = config.getSleepThreshold();
        if (velocity.lengthSquared() <= (thr * thr)) {
            sleepTimer++;
            if (sleepTimer >= config.getSleepDelay()) {
                sleep();
            }
        } else {
            sleepTimer = 0;
        }
    }

    private void sleep() {
        PhysicsSleepEvent event = new PhysicsSleepEvent(this);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            sleepTimer = 0;
            return;
        }

        sleeping = true;
        velocity.zero();
        entity.setVelocity(new Vector(0, 0, 0));

        if (config.isSolidifyOnSleep()) {
            trySolidify();
        }
    }

    public void wakeUp() {
        wakeUp("UNKNOWN");
    }

    public void wakeUp(String reason) {
        if (!sleeping) return;

        PhysicsWakeEvent event = new PhysicsWakeEvent(this, reason);
        Bukkit.getPluginManager().callEvent(event);

        sleeping = false;
        sleepTimer = 0;
    }

    private void trySolidify() {
        if (!(entity instanceof FallingBlock fb)) return;

        Location l = entity.getLocation();
        Block target = l.getBlock();

        if (!target.getType().isSolid()) {
            target.setBlockData(fb.getBlockData());
            kill();
            return;
        }

        Block above = target.getRelative(0, 1, 0);
        if (!above.getType().isSolid()) {
            above.setBlockData(fb.getBlockData());
            kill();
        }
    }

    private void checkWater(Location loc) {
        boolean wasInWater = inWater;
        Material type = loc.getBlock().getType();
        inWater = (type == Material.WATER);

        if (inWater && !wasInWater) {
            handleWaterEntry();
        }
    }

    private void handleWaterEntry() {
        double speed = velocity.length();
        if (speed <= config.getSplashVelocityThreshold()) return;

        velocity.multiply(0.5);

        if (config.isSplashSound()) {
            entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_GENERIC_SPLASH, 1.0f, 1.0f);
        }
        if (config.isSplashParticles()) {
            int count = Math.min((int) (speed * 20), 100);
            entity.getWorld().spawnParticle(Particle.WATER_SPLASH, entity.getLocation(), count, 0.5, 0.5, 0.5, 0.1);
        }
    }

    private void applyGravity() {
        double g = config.getGravity() * properties.getMass();
        acceleration.add(new Vector(0, -g, 0));
    }

    private void applyBuoyancy() {
        if (!inWater) return;

        double force = config.getGravity() * properties.getBuoyancy() * config.getBuoyancyMultiplier();
        acceleration.add(new Vector(0, force, 0));
    }

    private void applyDrag() {
        double speed = velocity.length();
        if (speed < 0.001) return;

        double dragCoeff = properties.getDrag();
        if (inWater) dragCoeff *= config.getDragInWaterMultiplier();

        Vector drag = velocity.clone().normalize().multiply(-dragCoeff * speed * speed);
        acceleration.add(drag);
    }

    private void applyFriction() {
        if (!onGround) return;

        Vector horizontal = velocity.clone();
        horizontal.setY(0);

        if (horizontal.lengthSquared() < 1.0E-6) return;

        Vector friction = horizontal.multiply(-properties.getFriction() * 0.2);
        acceleration.add(friction);
    }

    private void applyThermodynamics(Location loc) {
        if (!config.isThermalEnabled()) return;

        double ambient = 20.0;
        Block block = loc.getBlock();
        Material type = block.getType();

        if (type == Material.LAVA) ambient = 1200.0;
        else if (type == Material.FIRE || type == Material.SOUL_FIRE) ambient = 400.0;
        else if (type == Material.WATER) ambient = 15.0;
        else if (type == Material.ICE || type == Material.BLUE_ICE) ambient = -5.0;
        else if (type == Material.SNOW) ambient = -2.0;

        if (Objects.requireNonNull(loc.getWorld()).getEnvironment() == org.bukkit.World.Environment.NETHER) {
            ambient = Math.max(ambient, 100.0);
        }

        double k = properties.getThermalConductivity();
        double diff = ambient - currentTemperature;

        currentTemperature += diff * k * 0.1;

        if (config.isThermalEffects()) {
            if (currentTemperature > 100 && Math.random() < 0.1) {
                loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 1, 0, 0.1, 0, 0);
            }
        }

        if (currentTemperature > properties.getMeltingPoint()) {
            handleBehavior(properties.getBehaviorType());
        }
    }

    private void handleBehavior(String behavior) {
        if (behavior == null) behavior = "DEFAULT";
        if (isDead) return;
        
        if (config.isSafeMode()) {
            if (!behavior.equals("MELT")) return;
        }

        switch (behavior) {
            case "MELT" -> {
                if (config.isThermalEffects()) {
                    entity.getWorld().spawnParticle(Particle.DRIP_WATER, entity.getLocation(), 15);
                }
                if (entity instanceof FallingBlock fb && fb.getMaterial() == Material.ICE) {
                    entity.getLocation().getBlock().setType(Material.WATER);
                }
                kill();
            }
            case "BURN" -> {
                if (config.isSafeMode()) return;

                if (config.isThermalEffects()) {
                    entity.getWorld().spawnParticle(Particle.FLAME, entity.getLocation(), 20, 0.2, 0.2, 0.2, 0.05);
                    entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f);
                }
                if (config.isAllowBlockPlacement()) {
                    Block b = entity.getLocation().getBlock();
                    if (b.getType() == Material.AIR) b.setType(Material.FIRE);
                }
                kill();
            }
            case "EXPLODE" -> {
                if (config.isSafeMode()) return;
                
                entity.getWorld().createExplosion(
                        entity.getLocation(),
                        2.0f,
                        false,
                        config.isAllowBlockBreaking()
                );
                kill();
            }
        }
    }

    private void handleCollisions(Location loc) {
        if (velocity.lengthSquared() < 0.0001) {
            Block below = loc.getBlock().getRelative(0, -1, 0);
            onGround = below.getType().isSolid();
            return;
        }

        double rayDistance = velocity.length() + 0.1;

        org.bukkit.util.RayTraceResult result = Objects.requireNonNull(loc.getWorld()).rayTraceBlocks(
                loc,
                velocity.clone().normalize(),
                rayDistance,
                org.bukkit.FluidCollisionMode.NEVER,
                true
        );

        if (result == null || result.getHitBlock() == null) {
            onGround = false;
            return;
        }

        Block hitBlock = result.getHitBlock();
        org.bukkit.block.BlockFace face = result.getHitBlockFace();

        double impactForce = velocity.length() * properties.getMass();

        PhysicsCollideEvent event = new PhysicsCollideEvent(this, hitBlock, impactForce);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;

        impactForce = event.getImpactForce();

        if (properties.getBreakThreshold() > 0 && impactForce > properties.getBreakThreshold()) {
            if (config.isCollisionSpark()) {
                entity.getWorld().spawnParticle(Particle.BLOCK_CRACK, result.getHitPosition().toLocation(entity.getWorld()), 20, 0.2, 0.2, 0.2, hitBlock.getBlockData());
            }
            if (config.isCollisionSound()) {
                entity.getWorld().playSound(loc, Sound.BLOCK_STONE_BREAK, 1f, 1f);
            }
            kill();
            return;
        }

        if (face != null) {
            Vector normal = new Vector(face.getModX(), face.getModY(), face.getModZ());
            reflect(velocity, normal, properties.getBounciness());

            if (face == org.bukkit.block.BlockFace.UP) {
                onGround = true;

                velocity.setX(velocity.getX() * 0.8);
                velocity.setZ(velocity.getZ() * 0.8);
            } else {
                onGround = false;
            }
        }

        if (face != null) {
            Vector hitPos = result.getHitPosition();
            Vector correction = new Vector(face.getModX(), face.getModY(), face.getModZ()).multiply(0.3);
            Location newLoc = hitPos.add(correction).toLocation(entity.getWorld());

            newLoc.setYaw(loc.getYaw());
            newLoc.setPitch(loc.getPitch());

            entity.teleport(newLoc);
        }

        if (config.isCollisionSound() && impactForce > 0.5) {
            entity.getWorld().playSound(loc, Sound.BLOCK_STONE_HIT, (float)Math.min(impactForce/10.0, 2.0), 1.0f);
        }

        if (damageOnImpact && config.isAllowEntityDamage() && impactForce > 1.0) {
            damageNearbyEntities(loc, (float) (impactForce * impactDamageMultiplier));
        }

        wakeUp("COLLISION");
    }

    private void reflect(Vector v, Vector n, float bounciness) {

        double dot = v.dot(n);

        if (dot > 0) return;

        Vector change = n.clone().multiply(dot * (1.0 + bounciness));
        v.subtract(change);

        if (v.lengthSquared() < 0.001) v.zero();
    }

    private void damageNearbyEntities(Location loc, float damage) {
        if (loc.getWorld() == null) return;

        double capped = Math.min(damage, 20.0);
        for (Entity e : loc.getWorld().getNearbyEntities(loc, 1.5, 1.5, 1.5)) {
            if (e == entity) continue;
            if (e instanceof LivingEntity living) {
                living.damage(capped);
            }
        }
    }

    private void checkOutOfBounds(Location loc) {
        if (loc.getY() < -64) {
            kill();
        }
    }

    public void kill() {
        isDead = true;
        entity.remove();
    }

    public void applyProperties(PhysicsProperties newProps) {
        if (newProps == null) return;
        this.properties.copyFrom(newProps);
    }

    public void setVelocity(Vector v) {
        if (v == null) return;
        this.velocity = v.clone();
        wakeUp("SET_VELOCITY");
    }

    public Vector getVelocity() {
        return velocity.clone();
    }

    public void addVelocity(Vector v) {
        if (v == null) return;
        this.velocity.add(v);
        wakeUp("ADD_VELOCITY");
    }

    public Vector getAcceleration() {
        return acceleration.clone();
    }

    public boolean isSleeping() {
        return sleeping;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isInWater() {
        return inWater;
    }

    public boolean isDead() {
        return isDead;
    }

    public double getTemperature() {
        return currentTemperature;
    }

    public PhysicsProperties getProperties() {
        return properties;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setDamageOnImpact(boolean enabled) {
        this.damageOnImpact = enabled;
    }

    public void setImpactDamageMultiplier(double multiplier) {
        this.impactDamageMultiplier = multiplier;
    }
}
}
