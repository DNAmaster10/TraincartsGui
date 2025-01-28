package com.dnamaster10.traincartsticketshop.util.database.mariadb;

import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;

/**
 * Holds information about the MariaDB database, such as the host name and port.
 */
public class MariaDatabaseAccessor {
    private static final HikariDataSource dataSource;
    static {
        String url = getPlugin().getConfig().getString("database.host");
        String port = getPlugin().getConfig().getString("database.port");
        String name = getPlugin().getConfig().getString("database.database");

        String dbUrl = "jdbc:mariadb://" + url + ":" + port + "/" + name;
        String dbUsername = getPlugin().getConfig().getString("database.user");
        String dbPassword = getPlugin().getConfig().getString("database.password");

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);

        dataSource = new HikariDataSource(config);
    }

    /**
     * @return A new Connection object for the MariaDB database
     * @throws QueryException Thrown if an error occurs opening the connection
     */
    public Connection getConnection() throws QueryException {
        //Returns a new connection to the database
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }
}
