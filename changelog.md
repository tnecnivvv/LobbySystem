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
