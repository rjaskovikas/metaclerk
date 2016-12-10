package org.rola.metaclerk.model;

public abstract class DbObject {

    private String ownerName;
    private String name;
    private boolean wasFound;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWasFound() {
        return wasFound;
    }

    public void setWasFound(boolean wasFound) {
        this.wasFound = wasFound;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public abstract String getObjectType();

    @SuppressWarnings("SameReturnValue")
    public static String getStaticTypeName() {
        return "DbObject";
    }

    @Override
    public String toString() {
        return "DbObject{" +
                "ownerName='" + ownerName + '\'' +
                ", name='" + name + '\'' +
                ", wasFound=" + wasFound +
                '}';
    }
}
