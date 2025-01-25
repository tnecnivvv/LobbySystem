package com.tnecnivvv.api.mysql.redis;

import com.tnecnivvv.api.config.ConfigurationTypes;
import com.tnecnivvv.api.config.types.MySqlConfiguration;
import com.tnecnivvv.lobbySystem.dependencies.SimpleLogger;
import com.tnecnivvv.lobbySystem.plugin.ServiceLocator;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.logging.Level;

public class Redis {

    private JedisPool jedisPool;
    private final SimpleLogger simpleLogger;
    private final MySqlConfiguration.Jedis jedisConfiguration;

    public Redis() {
        simpleLogger = ServiceLocator.getInstance().getSimpleLogger();
        jedisConfiguration = ServiceLocator.getInstance().
                getConfigurationAPI().getConfiguration(
                        ConfigurationTypes.MYSQL,
                        MySqlConfiguration.class).
                getJedis();
    }

    /**
     * Initializes the Redis connection pool using configuration data.
     */
    public synchronized void initializeRedis() {
        if (jedisPool != null) {
            return;
        }

        try {
            if (jedisConfiguration == null) {
                throw new IllegalStateException("Jedis configuration is missing.");
            }

            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(jedisConfiguration.getConnection().getMaxConnections());
            jedisPoolConfig.setMaxIdle(jedisConfiguration.getConnection().getMaxIdleConnections());
            jedisPoolConfig.setMinIdle(jedisConfiguration.getConnection().getMinIdleConnections());

            jedisPool = new JedisPool(
                    jedisPoolConfig,
                    jedisConfiguration.getHost(),
                    jedisConfiguration.getPort(),
                    jedisConfiguration.getTimeout(),
                    jedisConfiguration.getPassword()
            );

            simpleLogger.log(Level.INFO, "Redis connection initialized.", null);
        } catch (Exception exception) {
            simpleLogger.log(Level.SEVERE, "Failed to initialize Redis connection.", exception);
        }
    }

    /**
     * Terminates the Redis connection pool and releases all resources.
     */
    public void terminateRedis() {
        if (jedisPool != null) {
            try {
                jedisPool.close();
                simpleLogger.log(Level.INFO, "Redis connection successfully closed.", null);
            } catch (Exception exception) {
                simpleLogger.log(Level.WARNING, "Failed to close Redis connection.", exception);
            }
        }
    }

    /**
     * Retrieves the Redis connection pool.
     *
     * @return the initialized {@link JedisPool}.
     * @throws IllegalStateException if the Redis connection pool has not been initialized.
     */
    public JedisPool getJedisPool() {
        if (jedisPool == null) {
            throw new IllegalStateException("Redis connection has not been initialized.");
        }
        return jedisPool;
    }
}