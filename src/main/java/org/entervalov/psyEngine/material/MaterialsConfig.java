package org.entervalov.psyEngine.material;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.entervalov.psyEngine.api.PhysicsProperties;
import org.entervalov.psyEngine.core.PhysicsRegistry;

import java.io.File;

public class MaterialsConfig {
    private final JavaPlugin plugin;
    private final PhysicsRegistry registry;

    public MaterialsConfig(JavaPlugin plugin, PhysicsRegistry registry) {
        this.plugin = plugin;
        this.registry = registry;
    }

    public void load() {
        File file = new File(plugin.getDataFolder(), "materials.yml");
        if (!file.exists()) {
            plugin.saveResource("materials.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        registry.clear();

        int count = 0;
        for (String key : config.getKeys(false)) {
            Material mat = Material.matchMaterial(key);
            if (mat == null) {
                plugin.getLogger().warning("Unknown material in materials.yml: " + key);
                continue;
            }

            ConfigurationSection sec = config.getConfigurationSection(key);
            if (sec == null) continue;

            PhysicsProperties props = new PhysicsProperties()
                    .setMass((float) sec.getDouble("mass", 1.0))
                    .setDrag((float) sec.getDouble("drag", 0.05))
                    .setFriction((float) sec.getDouble("friction", 0.6))
                    .setBounciness((float) sec.getDouble("bounciness", 0.2))
                    .setBuoyancy((float) sec.getDouble("buoyancy", 0.1))
                    .setBreakThreshold((float) sec.getDouble("break-threshold", 0.0))
                    .setThermalConductivity((float) sec.getDouble("thermal-conductivity", 0.3))
                    .setMeltingPoint((float) sec.getDouble("melting-point", 1000.0))
                    .setBehaviorType(sec.getString("behavior", "DEFAULT"));

            registry.register(mat, props);
            count++;
        }

        plugin.getLogger().info("✓ Uploaded materials: " + count);
    }
}