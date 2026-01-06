package org.entervalov.psyEngine.test;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.entervalov.psyEngine.core.PhysicsLibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhysicsTabCompleter implements TabCompleter {

    private final PhysicsLibrary physics;
    private final List<String> subCommands = List.of(
            "tower", "cannon", "fireball", "raft", "landslide",
            "stats", "info", "clear", "push", "debug",
            "glass", "reload", "test"
    );

    public PhysicsTabCompleter(PhysicsLibrary physics) {
        this.physics = physics;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            List<String> availableCommands = new ArrayList<>();

            for (String cmd : subCommands) {
                if (physics.getConfig().isCommandEnabled(cmd)) {
                    availableCommands.add(cmd);
                }
            }

            StringUtil.copyPartialMatches(args[0], availableCommands, completions);

            Collections.sort(completions);
            return completions;
        }

        return Collections.emptyList();
    }
}