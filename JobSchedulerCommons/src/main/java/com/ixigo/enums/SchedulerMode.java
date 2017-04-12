package com.ixigo.enums;

/**
 * Created by dixant on 12/04/17.
 */
public enum SchedulerMode {
    START("START"),
    SHUTDOWN("SHUTDOWN"),
    STANDBY("STANDBY"),
    NULL("NULL");

    private String value;

    SchedulerMode(String value) {
        this.value = value;
    }

    public static SchedulerMode forName(String val) {
        for (SchedulerMode schedulerMode : SchedulerMode.values()) {
            if (schedulerMode.value.equalsIgnoreCase(val)) {
                return schedulerMode;
            }
        }
        return NULL;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
