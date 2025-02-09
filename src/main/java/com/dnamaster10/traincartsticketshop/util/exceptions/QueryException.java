package com.dnamaster10.traincartsticketshop.util.exceptions;

import java.sql.SQLException;

public class QueryException extends SQLException {
    /**
     * Used for when an error occurs while accessing the Database, e.g. SELECT statements.
     *
     * @param cause The SQLException
     */
    public QueryException(SQLException cause) {
        super(cause);
    }
}
