package org.entervalov.psyEngine.core;

import org.bukkit.Material;
import org.entervalov.psyEngine.api.PhysicsProperties;

import java.util.HashMap;
import java.util.Map;

public class PhysicsRegistry {

    private final Map<Material, PhysicsProperties> registry = new HashMap<>();
    private final PhysicsProperties defaultProperties = new PhysicsProperties();

    public PhysicsProperties get(Material material) {
        return registry.getOrDefault(material, defaultProperties);
    }

    public void register(Material material, PhysicsProperties properties) {
        registry.put(material, properties);
    }

    public void clear() {
        registry.clear();
    }
}