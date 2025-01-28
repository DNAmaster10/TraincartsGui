package com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces;

import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;

public interface DatabaseTableCreator {
    /**
     * Creates, configures, and updates tables within the database.
     */
    void createTables() throws ModificationException;
}
