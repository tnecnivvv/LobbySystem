## 🗓️ Version 1.0.3
### Plugin Updates
- [X] Refactored `GAMELEVEL` to `NETWORKLEVEL` and `GAMERANK` to `NETWORKRANK`.
- [X] Added **Player Data** handling in ConfigValues.
- [X] Secured all Manager classes by making them private to ensure single-instance usage.
- [X] Introduced a **DatabaseManager**.
- [X] Implemented a **PlayerLoginEvent** to automatically create player profiles in the database upon login.
- [X] Created **PlayerProfileTable** to handle both the table structure and individual player profiles.

## 🗓️ Version 1.0.2
### Plugin Updates
- [X] Implemented **DataSource** using HikariCP for connection pooling.
- [X] Introduced a **Cache System** (beta).
- [X] Added **DataProfiles** to manage all values and table names.

## 🗓️ Patch for Version 1.0.1   
### Plugin Updates
- [X] Moved **Updater** to `onLoad()`.
- [X] Added a config option to enable or disable the **Updater**.
- [X] Added **MOTD** (Message of the Day) to the configuration file.
- [X] Updated logging messages for config verification.
- [X] Renamed `helper` to `plugin` in `settings.yml`.

## 🗓️ Version 1.0.1
### Plugin Updates
- [X] Introduced **ConfigManager** for more structured configuration.
- [X] Updated **SystemManager** for config handling.
- [X] Tweaked **EntityDamageEvent** for better handling.
- [X] Performed general code clean-up.
- [X] Prepared the plugin for future updates.

## 🗓️ Version 1.0.0
### Plugin Updates
- [X] Launched early development.
- [X] Added **SystemManager** for core management.
- [X] Integrated basic event handling.