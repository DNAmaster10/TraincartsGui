package com.dnamaster10.traincartsticketshop.util.database.sqlite;

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

public class SQLitePlayerAccessor extends SQLiteDatabaseAccessor implements PlayerDatabaseAccessor {
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
            PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO players (username, uuid, last_join) VALUES (?, ?, ?);");
            statement.setString(1, username);
            statement.setString(2, uuid);
            statement.setLong(3, System.currentTimeMillis());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ModificationException(e);
        }
    }
}
