package com.noctify.Main.Registration;

import com.noctify.Custom.ItemRegistry;
import com.noctify.Custom.FoodRegistry;
import com.noctify.Main.Exceptions.AutoRegisterException;
import org.bukkit.plugin.Plugin;
import java.util.Set;
import org.reflections.Reflections;

public class BehaviorAutoRegistrar {
    public static void registerAll(Plugin plugin, String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> itemBehaviors = reflections.getTypesAnnotatedWith(AutoRegister.class);

        for (Class<?> clazz : itemBehaviors) {
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                if (instance instanceof FXItemBehavior) {
                    ItemRegistry.registerItemClass(clazz);
                } else if (instance instanceof FXFoodBehavior) {
                    // Implement similar registration for food behaviors if needed
                }
            } catch (Exception e) {
                try {
                    throw new AutoRegisterException("Failed to auto-register: " + clazz.getName(), e);
                } catch (AutoRegisterException ex) {
                    plugin.getLogger().warning(ex.getMessage());
                }
            }
        }
    }
}