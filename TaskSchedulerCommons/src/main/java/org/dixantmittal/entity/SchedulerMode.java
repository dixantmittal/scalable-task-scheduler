package org.dixantmittal.entity;

/**
 * Created by dixant on 12/04/17.
 */
public enum SchedulerMode {
    START("START"),
    SHUTDOWN("SHUTDOWN"),
    STANDBY("STANDBY");

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
        return null;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
