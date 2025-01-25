package com.tnecnivvv.api.config.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MySqlConfiguration {

    private MySQL mysql;
    private Hikari hikari;
    private Jedis jedis;

    public MySQL getMysql() {
        return mysql;
    }

    public Hikari getHikari() {
        return hikari;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public static class MySQL {

        @JsonProperty("user")
        private String user;

        @JsonProperty("password")
        private String password;

        @JsonProperty("host")
        private String host;

        @JsonProperty("port")
        private int port;

        @JsonProperty("database")
        private String database;

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String getDatabase() {
            return database;
        }
    }

    public static class Hikari {

        @JsonProperty("caching")
        private Caching caching;

        @JsonProperty("pooling")
        private Pooling pooling;

        @JsonProperty("detection")
        private Detection detection;

        public Caching getCaching() {
            return caching;
        }

        public Pooling getPooling() {
            return pooling;
        }

        public Detection getDetection() {
            return detection;
        }

        public static class Caching {

            @JsonProperty("enableCaching")
            private boolean enableCaching;

            @JsonProperty("cacheSize")
            private int cacheSize;

            @JsonProperty("sqlLimit")
            private int sqlLimit;

            public boolean isEnableCaching() {
                return enableCaching;
            }

            public int getCacheSize() {
                return cacheSize;
            }

            public int getSqlLimit() {
                return sqlLimit;
            }
        }

        public static class Pooling {

            @JsonProperty("maxConnections")
            private int maxConnections;

            @JsonProperty("maxLifetime")
            private int maxLifetime;

            @JsonProperty("minIdleConnections")
            private int minIdleConnections;

            @JsonProperty("idleTimeout")
            private int idleTimeout;

            @JsonProperty("connectionTimeout")
            private int connectionTimeout;

            public int getMaxConnections() {
                return maxConnections;
            }

            public int getMaxLifetime() {
                return maxLifetime;
            }

            public int getMinIdleConnections() {
                return minIdleConnections;
            }

            public int getIdleTimeout() {
                return idleTimeout;
            }

            public int getConnectionTimeout() {
                return connectionTimeout;
            }
        }

        public static class Detection {

            @JsonProperty("threshold")
            private int threshold;

            public int getThreshold() {
                return threshold;
            }
        }
    }

    public static class Jedis {

        @JsonProperty("use-redis")
        private boolean useRedis;

        @JsonProperty("host")
        private String host;

        @JsonProperty("port")
        private int port;

        @JsonProperty("timeout")
        private int timeout;

        @JsonProperty("password")
        private String password;

        public boolean isUseRedis() {
            return useRedis;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public int getTimeout() {
            return timeout;
        }

        public String getPassword() {
            return password;
        }

        @JsonProperty("connection")
        private Connection connection;

        public Connection getConnection() {
            return connection;
        }

        public static class Connection {

            @JsonProperty("maxConnections")
            private int maxConnections;

            @JsonProperty("maxIdleConnections")
            private int maxIdleConnections;

            @JsonProperty("minIdleConnections")
            private int minIdleConnections;

            @JsonProperty("block-when-exhausted")
            private boolean blockWhenExhausted;

            @JsonProperty("test-on-borrow")
            private boolean testOnBorrow;

            @JsonProperty("test-on-return")
            private boolean testOnReturn;

            @JsonProperty("test-while-idle")
            private boolean testWhileIdle;

            @JsonProperty("max-wait")
            private int maxWait;

            public int getMaxConnections() {
                return maxConnections;
            }

            public int getMaxIdleConnections() {
                return maxIdleConnections;
            }

            public int getMinIdleConnections() {
                return minIdleConnections;
            }

            public boolean isBlockWhenExhausted() {
                return blockWhenExhausted;
            }

            public boolean isTestOnBorrow() {
                return testOnBorrow;
            }

            public boolean isTestOnReturn() {
                return testOnReturn;
            }

            public boolean isTestWhileIdle() {
                return testWhileIdle;
            }

            public int getMaxWait() {
                return maxWait;
            }
        }
    }
}