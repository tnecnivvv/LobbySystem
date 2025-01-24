package com.tnecnivvv.lobbySystem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Registry {

    private volatile static Registry instance;
    private final Map<Class<?>, Object> registryCache = new ConcurrentHashMap<>();

    private Registry() {}

    /**
     * Singleton Instance
     *
     * @return {@link Registry} instance
     */
    public static Registry getInstance() {
        if (instance == null) {
            synchronized (Registry.class) {
                if (instance == null) {
                    instance = new Registry();
                }
            }
        }
        return instance;
    }

    /**
     * Registers an object in the registry, associating it with its class type
     *
     * @param <T>   The type of the object being registered
     * @param clazz The class type to associate with the object
     * @param obj   The object to register
     * @throws IllegalArgumentException If the provided class or object is null
     */
    public <T> void registerClass(Class<T> clazz, T obj) {
        if (clazz == null || obj == null) {
            throw new IllegalArgumentException("Class type and object must not be null.");
        }
        registryCache.put(clazz, obj);
    }

    /**
     * Resolves and retrieves an object from the registry by its class type
     *
     * @param <T>   The type of the object to retrieve
     * @param clazz The class type of the object to retrieve
     * @return The object associated with the class type, or null if not found
     * @throws IllegalArgumentException If the provided class is null
     */
    public <T> T resolveClass(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class type must not be null.");
        }
        return clazz.cast(registryCache.get(clazz));
    }

    /**
     * Clears all entries from the registry
     */
    public void clearCache() {
        registryCache.clear();
    }
}