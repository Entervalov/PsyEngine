package org.entervalov.psyEngine.message;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MessageManager {
    private final JavaPlugin plugin;
    private final Map<String, String> messages = new HashMap<>();

    public MessageManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadMessages() {
        File messagesDir = new File(plugin.getDataFolder(), "messages");
        if (!messagesDir.exists()) messagesDir.mkdirs();

        saveResourceIfNotExists("messages/messages_ru.yml");
        saveResourceIfNotExists("messages/messages_en.yml");

        File defaultFile = new File(messagesDir, "messages_en.yml");
        if (defaultFile.exists()) {
            loadFromFile(defaultFile);
        }

        String lang = plugin.getConfig().getString("general.language", "ru");
        String userFilename = "messages_" + lang + ".yml";
        File userFile = new File(messagesDir, userFilename);

        if (!lang.equalsIgnoreCase("en") && userFile.exists()) {
            plugin.getLogger().info("Loading custom language: " + userFilename);
            loadFromFile(userFile);
        } else if (!userFile.exists() && !lang.equalsIgnoreCase("en")) {
            plugin.getLogger().warning("Custom language file " + userFilename + " not found! Using English defaults.");
        }

        plugin.getLogger().info("✓ Messages system initialized (" + messages.size() + " keys)");
    }

    private void saveResourceIfNotExists(String path) {
        if (plugin.getResource(path) != null) {
            File target = new File(plugin.getDataFolder(), path);
            if (!target.exists()) {
                plugin.saveResource(path, false);
            }
        }
    }

    private void loadFromFile(File file) {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        for (String key : yaml.getKeys(true)) {
            if (!yaml.isConfigurationSection(key)) {
                String msg = yaml.getString(key);
                if (msg != null) {

                    messages.put(key, msg.replace("&", "§"));
                }
            }
        }
    }

    public String getMessage(String key) {
        return messages.getOrDefault(key, "§c[Missing: " + key + "]");
    }

    public String getMessage(String key, Object... args) {
        String msg = getMessage(key);
        for (int i = 0; i < args.length; i++) {
            msg = msg.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return msg;
    }
}