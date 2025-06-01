package org.example.enumtype;

public enum WorkspaceType {
    OPEN("Open Space"),
    PRIVATE("Private Office"),
    ROOM("Meeting Room");

    private final String displayName;

    WorkspaceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
