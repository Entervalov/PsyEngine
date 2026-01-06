package org.entervalov.psyEngine.test;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.entervalov.psyEngine.api.PhysicsProperties;
import org.entervalov.psyEngine.core.PhysicsEntity;
import org.entervalov.psyEngine.core.PhysicsLibrary;
import org.entervalov.psyEngine.message.MessageManager;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

public class PhysicsCommands implements CommandExecutor {

    private final PhysicsLibrary physics;
    private final MessageManager msg;

    public PhysicsCommands(PhysicsLibrary physics) {
        this.physics = physics;
        this.msg = physics.getMessageManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!physics.getConfig().isCommandsEnabled()) return true;

        if (!(sender instanceof Player player)) {
            sender.sendMessage(msg.getMessage("error.not_player"));
            return true;
        }

        if (physics.getConfig().isRequireOp() && !player.isOp()) {
            player.sendMessage(msg.getMessage("error.require_op"));
            return true;
        }

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (!physics.getConfig().isCommandEnabled(subCommand)) {
            player.sendMessage(msg.getMessage("error.command_disabled"));
            return true;
        }

        switch (subCommand) {
            case "tower" -> createTower(player);
            case "cannon" -> createCannon(player);
            case "fireball" -> launchFireball(player);
            case "raft" -> createRaft(player);
            case "landslide" -> createLandslide(player);
            case "stats" -> showStats(player);
            case "info" -> showEntityInfo(player);
            case "clear" -> clearEntities(player);
            case "push" -> pushNearby(player);
            case "debug" -> toggleDebug(player);
            case "glass" -> createGlass(player);
            case "reload" -> reloadPlugin(player);
            case "test" -> testPhysics(player);
            default -> showHelp(player);
        }

        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage(msg.getMessage("command.help.header"));
        player.sendMessage(msg.getMessage("command.help.title"));
        player.sendMessage(msg.getMessage("command.help.divider"));

        if (check("tower")) player.sendMessage(msg.getMessage("command.help.tower"));
        if (check("cannon")) player.sendMessage(msg.getMessage("command.help.cannon"));
        if (check("fireball")) player.sendMessage(msg.getMessage("command.help.fireball"));
        if (check("glass")) player.sendMessage(msg.getMessage("command.help.glass"));
        if (check("raft")) player.sendMessage(msg.getMessage("command.help.raft"));
        if (check("landslide")) player.sendMessage(msg.getMessage("command.help.landslide"));
        if (check("info")) player.sendMessage(msg.getMessage("command.help.info"));
        if (check("push")) player.sendMessage(msg.getMessage("command.help.push"));
        if (check("clear")) player.sendMessage(msg.getMessage("command.help.clear"));
        if (check("debug")) player.sendMessage(msg.getMessage("command.help.debug"));
        if (check("reload")) player.sendMessage(msg.getMessage("command.help.reload"));
        if (check("test")) player.sendMessage(msg.getMessage("command.help.test"));
        if (check("stats")) player.sendMessage(msg.getMessage("command.help.stats"));

        player.sendMessage(msg.getMessage("command.help.footer"));
    }

    private boolean check(String cmd) {
        return physics.getConfig().isCommandEnabled(cmd);
    }

    private void createTower(Player player) {
        Location base = player.getLocation().add(0, 5, 0);
        player.sendMessage(msg.getMessage("command.tower.create"));
        for (int y = 0; y < 5; y++) {
            physics.spawnPhysicsBlock(base.clone().add(0, y * 1.2, 0), Material.STONE_BRICKS);
        }
        player.sendMessage(msg.getMessage("command.tower.complete"));
    }

    private void createCannon(Player player) {
        Location barrelLoc = player.getLocation().add(2, 1, 0);
        Vector direction = player.getLocation().getDirection();
        barrelLoc.getBlock().setType(Material.IRON_BLOCK);

        PhysicsEntity cannonball = physics.spawnPhysicsBlock(
                barrelLoc.clone().add(direction.clone().normalize().multiply(2)),
                Material.IRON_BLOCK
        );

        if (cannonball != null) {

            PhysicsProperties props = PhysicsProperties.heavy()
                    .setBounciness(0.05f)
                    .setBehaviorType("EXPLODE");

            cannonball.applyProperties(props);
            cannonball.getProperties().copyFrom(props);

            cannonball.setVelocity(direction.normalize().multiply(3.0));
            cannonball.setDamageOnImpact(true);
            cannonball.setImpactDamageMultiplier(5.0);

            Objects.requireNonNull(barrelLoc.getWorld()).playSound(barrelLoc, Sound.ENTITY_GENERIC_EXPLODE, 1.5f, 0.8f);
            player.sendMessage(msg.getMessage("command.cannon.fire"));
        }
    }

    private void launchFireball(Player player) {
        Vector dir = player.getLocation().getDirection();
        PhysicsEntity fireball = physics.spawnPhysicsBlock(
                player.getLocation().add(0, 2, 0).add(dir.multiply(2)),
                Material.MAGMA_BLOCK
        );
        if (fireball != null) {
            fireball.setVelocity(dir.normalize().multiply(2.0));
            fireball.getProperties().setBehaviorType("BURN");
            player.sendMessage(msg.getMessage("command.fireball.launch"));
        }
    }

    private void createRaft(Player player) {
        Location loc = player.getLocation().add(0, 1, 0);
        for (int x = 0; x < 3; x++) {

            physics.spawnPhysicsBlock(loc.clone().add(x, 0, 0), Material.OAK_LOG);
        }
        player.sendMessage(msg.getMessage("command.raft.created"));
    }

    private void createLandslide(Player player) {
        Location center = player.getLocation().add(0, 5, 0);
        for (int x = -2; x <= 2; x++) {
            if (Math.random() < 0.5) {
                physics.spawnPhysicsBlock(center.clone().add(x, 0, 0), Material.DIRT);
            }
        }
        player.sendMessage(msg.getMessage("command.landslide.created"));
    }

    private void createGlass(Player player) {
        PhysicsEntity glass = physics.spawnPhysicsBlock(player.getLocation().add(0, 10, 0), Material.GLASS);
        if (glass != null) {
            player.sendMessage(msg.getMessage("command.glass.created"));
        }
    }

    private void showEntityInfo(Player player) {
        Optional<PhysicsEntity> nearest = physics.getEntities().values().stream()
                .min(Comparator.comparingDouble(e -> e.getEntity().getLocation().distanceSquared(player.getLocation())));

        if (nearest.isPresent()) {
            PhysicsEntity entity = nearest.get();
            PhysicsProperties props = entity.getProperties();

            player.sendMessage(msg.getMessage("info.header"));

            String matName = (entity.getEntity() instanceof org.bukkit.entity.FallingBlock fb)
                    ? fb.getMaterial().name()
                    : entity.getEntity().getType().name();

            player.sendMessage(msg.getMessage("info.material", matName));
            player.sendMessage(msg.getMessage("info.velocity", String.format("%.2f", entity.getVelocity().length())));

            String state = entity.isSleeping() ? msg.getMessage("info.sleeping") : msg.getMessage("info.active");
            player.sendMessage(msg.getMessage("info.state", state));

            player.sendMessage(msg.getMessage("info.mass", props.getMass()));
            player.sendMessage(msg.getMessage("info.behavior", props.getBehaviorType()));
            player.sendMessage(msg.getMessage("info.footer"));
        } else {
            player.sendMessage(msg.getMessage("info.not_found"));
        }
    }

    private void clearEntities(Player player) {
        int count = physics.getEntities().size();
        physics.getEntities().values().forEach(PhysicsEntity::kill);
        physics.getEntities().clear();
        player.sendMessage(msg.getMessage("command.clear.success", count));
    }

    private void pushNearby(Player player) {
        for (PhysicsEntity entity : physics.getEntities().values()) {
            if (entity.getEntity().getLocation().distance(player.getLocation()) < 5) {
                Vector push = player.getLocation().getDirection().multiply(2.0);
                entity.addVelocity(push);
                player.getWorld().playSound(entity.getEntity().getLocation(), Sound.ENTITY_ARROW_HIT, 1f, 1f);
                player.sendMessage(msg.getMessage("command.push.success"));
                return;
            }
        }
        player.sendMessage(msg.getMessage("command.push.none"));
    }

    private void toggleDebug(Player player) {
        physics.getDebugManager().toggleDebug(player);
    }

    private void showStats(Player player) {
        int total = physics.getEntities().size();
        long sleeping = physics.getEntities().values().stream().filter(PhysicsEntity::isSleeping).count();
        int max = physics.getConfig().getMaxActiveEntities();

        player.sendMessage(msg.getMessage("stats.header"));
        player.sendMessage(msg.getMessage("stats.active", total - sleeping));
        player.sendMessage(msg.getMessage("stats.sleeping", sleeping));
        player.sendMessage(msg.getMessage("stats.total", total));
        player.sendMessage(msg.getMessage("stats.limit", max));
        player.sendMessage(msg.getMessage("stats.footer"));
    }

    private void reloadPlugin(Player player) {
        if (!player.isOp()) {
            player.sendMessage(msg.getMessage("error.require_op"));
            return;
        }
        player.sendMessage(msg.getMessage("reload.start"));
        physics.getPlugin().onDisable();
        physics.getPlugin().onEnable();
        player.sendMessage(msg.getMessage("reload.complete"));
    }

    private void testPhysics(Player player) {
        Location center = player.getLocation().add(5, 10, 0);
        int count = 0;
        player.sendMessage(msg.getMessage("test.start"));

        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 10; z++) {
                if (physics.getEntities().size() < physics.getConfig().getMaxActiveEntities()) {
                    physics.spawnPhysicsBlock(center.clone().add(x * 1.5, 0, z * 1.5), Material.STONE);
                    count++;
                }
            }
        }
        player.sendMessage(msg.getMessage("test.complete", count));
    }
}