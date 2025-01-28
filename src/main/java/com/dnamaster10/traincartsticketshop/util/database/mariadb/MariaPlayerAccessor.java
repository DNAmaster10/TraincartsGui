package com.dnamaster10.traincartsticketshop.util.database.mariadb;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.PlayerDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces.PlayerDatabaseAccessor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MariaPlayerAccessor extends MariaDatabaseAccessor implements PlayerDatabaseAccessor {
    @Override
    public List<PlayerDatabaseObject> getAllPlayers() throws QueryException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT username,uuid FROM players");
            ResultSet result = statement.executeQuery();
            List<PlayerDatabaseObject> players = new ArrayList<>();
            while (result.next()) {
                players.add(new PlayerDatabaseObject(result.getString("username"), result.getString("uuid")));
            }
            return players;
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public void updatePlayer(String username, String uuid) throws ModificationException {
        try (Connection connection = getConnection()) {
            /*
            * Updates or inserts a player into the players table.
            * The last_join column is used in the event that a player changes their username.
            * When selecting UUID from username, if there are duplicate usernames, will favour the most recent
            * time in this column.
            * */

            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO players (username, uuid, last_join)
                    VALUES (?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                        username=VALUES(username),
                        last_join=VALUES(last_join)
                    """);
            statement.setString(1, username);
            statement.setString(2, uuid);
            statement.setLong(3, System.currentTimeMillis());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ModificationException(e);
        }
    }
}
