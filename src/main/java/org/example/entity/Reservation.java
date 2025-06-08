package org.example.entity;

import java.util.Objects;

public class Reservation {
    private final String name;
    private final int spaceId;
    private final Interval interval;
    private int id;

    public Reservation(String name, int spaceId, Interval interval) {
        this.name = Objects.requireNonNull(name, "Reservation name cannot be null.");
        this.interval = Objects.requireNonNull(interval, "Interval cannot be null.");
        this.spaceId = spaceId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public int getSpaceId() {
        return spaceId;
    }
    public Interval getInterval() {
        return interval;
    }
    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", spaceId=" + spaceId +
                ", interval=" + interval +
                '}';
    }
}
