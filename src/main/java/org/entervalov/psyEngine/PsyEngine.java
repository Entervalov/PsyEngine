package org.entervalov.psyEngine;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.entervalov.psyEngine.core.PhysicsLibrary;
import org.entervalov.psyEngine.message.MessageManager;
import org.entervalov.psyEngine.test.PhysicsCommands;
import org.entervalov.psyEngine.test.PhysicsListener;
import org.entervalov.psyEngine.test.PhysicsTabCompleter;

public class PsyEngine extends JavaPlugin {

    private PhysicsLibrary physics;
    private MessageManager msg;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        physics = new PhysicsLibrary(this);
        msg = physics.getMessageManager();

        if (PhysicsLibrary.getInstance() == null) {
            getLogger().severe("❌ Initialization error: PhysicsEngine instance is null!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new PhysicsListener(physics), this);

        if (physics.getConfig().isCommandsEnabled()) {
            PluginCommand cmd = getCommand("physics");

            if (cmd != null) {
                cmd.setExecutor(new PhysicsCommands(physics));
                cmd.setTabCompleter(new PhysicsTabCompleter(physics));
                getLogger().info(msg.getMessage("startup.commands_enabled"));
            } else {
                // Полезный лог, если забыл добавить в plugin.yml
                getLogger().warning("⚠️ Command 'psy' not found in plugin.yml! Commands will not work.");
            }
        } else {
            getLogger().info(msg.getMessage("startup.commands_disabled"));
        }

        getLogger().info(msg.getMessage("startup.enabled"));
    }

    @Override
    public void onDisable() {
        if (physics != null) {
            physics.disable();
            getLogger().info(msg.getMessage("shutdown.disabled"));
        }
    }
}