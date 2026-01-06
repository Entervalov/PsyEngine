package org.entervalov.psyEngine.utils;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class PhysicsLogger {
    private final Logger logger;
    private FileHandler fileHandler;

    public PhysicsLogger(JavaPlugin plugin, String fileName) {
        this.logger = Logger.getLogger("PhysicsEngine");

        try {
            File logFile = new File(plugin.getDataFolder(), fileName);
            if (!logFile.exists()) {
                logFile.getParentFile().mkdirs();
                logFile.createNewFile();
            }

            fileHandler = new FileHandler(logFile.getAbsolutePath(), true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);

        } catch (IOException e) {
            plugin.getLogger().severe("Could not create log file! " + e.getMessage());
        }
    }

    public void log(String message) {
        if (fileHandler != null) {
            logger.info(message);
        }
    }

    public void close() {
        if (fileHandler != null) {
            fileHandler.close();
        }
    }
}