package org.dixantmittal.entity;

/**
 * Created by dixant on 04/04/17.
 */
public enum Status {
    SUCCESS("SUCCESS"), FAILURE("FAILURE");

    private String value;

    Status(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
