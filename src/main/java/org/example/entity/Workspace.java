package org.example.entity;

import org.example.enumtype.WorkspaceType;

import java.util.Objects;

public class Workspace {
    private int id;
    private String name;
    private WorkspaceType type;
    private double price;

    public Workspace(String name, WorkspaceType type, double price) {
        this.name = Objects.requireNonNull(name, "Workspace name cannot be null.");
        this.type = Objects.requireNonNull(type, "Workspace type cannot be null.");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (this.id != 0)
            throw new IllegalStateException("ID can only be set once.");
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Workspace name cannot be null.");
    }

    public WorkspaceType getType() {
        return type;
    }

    public void setType(WorkspaceType type) {
        this.type = Objects.requireNonNull(type, "Workspace type cannot be null.");
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    @Override
    public String toString() {
        return "Workspace{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", price=" + price +
                '}';
    }
}
