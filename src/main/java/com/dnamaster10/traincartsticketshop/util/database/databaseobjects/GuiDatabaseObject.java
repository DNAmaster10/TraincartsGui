package com.dnamaster10.traincartsticketshop.util.database.databaseobjects;

/**
 * Holds information pertaining to a specific Gui.
 */
public class GuiDatabaseObject {
    public GuiDatabaseObject(int id, String name, String displayName, String ownerUuid) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.ownerUuid = ownerUuid;
    }

    private final int id;
    private String name;
    private String displayName;
    private String ownerUuid;

    public int id() {return this.id;}
    public String name() {return this.name;}
    public String displayName() {return this.displayName;}
    public String ownerUuid() {return this.ownerUuid;}

    public void setName(String name) {this.name = name;}
    public void setDisplayName(String displayName) {this.displayName = displayName;}
    public void setOwnerUuid(String ownerUuid) {this.ownerUuid = ownerUuid;}
}
