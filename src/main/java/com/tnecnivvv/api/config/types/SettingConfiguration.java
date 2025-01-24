package com.tnecnivvv.api.config.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingConfiguration {

    private Updates updates;
    private Logging logging;

    public Updates getUpdates() {
        return updates;
    }

    public Logging getLogging() {
        return logging;
    }

    public static class Updates {

        @JsonProperty("notifications")
        private boolean notifications;

        @JsonProperty("check-on-startup")
        private boolean checkOnStartup;

        public boolean doNotifications() {
            return notifications;
        }

        public boolean doCheckOnStartup() {
            return checkOnStartup;
        }

        public void setNotifications(boolean notifications) {
            this.notifications = notifications;
        }

        public void setCheckOnStartup(boolean checkOnStartup) {
            this.checkOnStartup = checkOnStartup;
        }
    }

    public static class Logging {

        @JsonProperty("verbose-logging")
        private boolean verboseLogging;

        public boolean doVerboseLogging() {
            return verboseLogging;
        }

        public void setVerboseLogging(boolean verboseLogging) {
            this.verboseLogging = verboseLogging;
        }
    }
}