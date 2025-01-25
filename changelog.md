# 📜 Changelog

A detailed history of all versions, changes, updates, and improvements in the project.

---

## 🌟 Version 1.0.0-Beta - 24.01.2025

- 🚀 **Project Initiated**  
  The foundation of the project was established.

## 🛠️ ConfigurationAPI Introduced
- **Added the first API: `ConfigurationAPI`.**
  - Configurations are managed through `settings.yml`, with support for additional configuration files planned in future updates.
  - **Caching Mechanism:**
    - Frequently accessed configurations are cached in memory to minimize file I/O operations and enhance performance.
    - The cache can be cleared selectively for specific configurations or entirely when needed.

- 📂 **Library Integration**  
  The [jackson-dataformats-yaml](https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml) library is utilized for managing configuration files.
    - The initial configuration file is `settings.yml`.

⚠️ **Note**:  
The ConfigurationAPI is still in its early stages and may undergo significant changes or be removed in future updates.

---

## 🌟 Version 1.0.1-Beta - 25.01.2025

### 🔧 **ConfigurationAPI Expanded**
- **Added New Configurations:**
  - Expanded the configuration for an additional configuration type.
- **Method Renaming:**
  - The `saveConfigs` method has been renamed to `saveConfiguration`.

### 🛠️ **DatabaseAPI Introduced**
- **Initial Implementation:**
  - The `DatabaseAPI` has been added, serving as the base for managing database connections.
- **Integration Highlights:**
  - **HikariCP Connection Pool:**
    - Added to manage MySQL connections efficiently.
  - **Jedis Integration:**
    - Redis connection pool integrated for caching.

⚠️ **Note**:  
The `DatabaseAPI` is in its early stages and may be removed, expanded, or significantly altered in future updates.

### 🔄 **Updater & TextSerializer**
- **Updater:**
  - Automatically checks for updates every few minutes and notifies all online players with the appropriate permission.
  - Performs an update check during server startup to ensure the plugin is running the latest version.
- **TextSerializer:**
  - Added as a utility for future features, with more details to come.

---