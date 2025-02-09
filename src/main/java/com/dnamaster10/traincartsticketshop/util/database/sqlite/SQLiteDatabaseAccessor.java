package com.dnamaster10.traincartsticketshop.util.database.sqlite;

import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;

import java.sql.*;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;

/**
 * Holds information about the SQLite database, such as the file name.
 */
public class SQLiteDatabaseAccessor {
    private static final String url;

    static {
        String name = getPlugin().getConfig().getString("database.database");
        String path = getPlugin().getDataFolder() + "/" + name + ".db";
        url = "jdbc:sqlite:" + path;
    }

    public Connection getConnection() throws QueryException {
        Connection connection;
        try {
            connection = DriverManager.getConnection(url);

            //Set Pragma optimizations
            PreparedStatement statement;

            statement = connection.prepareStatement("PRAGMA journal_mode=WAL");
            statement.execute();
            statement.close();

            statement = connection.prepareStatement("PRAGMA synchronous = normal");
            statement.execute();
            statement.close();

            statement = connection.prepareStatement("PRAGMA temp_store = memory");
            statement.execute();
            statement.close();

            statement = connection.prepareStatement("PRAGMA mmap_size = 30000000000");
            statement.execute();
            statement.close();

            statement = connection.prepareStatement("PRAGMA foreign_keys = ON");
            statement.execute();
            statement.close();

            return connection;
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }
}
