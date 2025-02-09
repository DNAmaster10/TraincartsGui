package com.dnamaster10.traincartsticketshop.util.exceptions;

import java.sql.SQLException;

public class ModificationException extends SQLException {
    /**
     * Used for when an error occurs while modifying the Database, e.g. UPDATE or INSERT statements.
     * The plugin will usually disable itself when this error is thrown to prevent database damage.
     *
     * @param cause The SQLException
     */
    public ModificationException(SQLException cause) {
        super(cause);
    }
}
