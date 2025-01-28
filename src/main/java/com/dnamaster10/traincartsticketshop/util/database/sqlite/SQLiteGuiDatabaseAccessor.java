package com.dnamaster10.traincartsticketshop.util.database.sqlite;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces.GuiDatabaseAccessor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteGuiDatabaseAccessor extends SQLiteDatabaseAccessor implements GuiDatabaseAccessor {
    @Override
    public List<GuiDatabaseObject> getGuisFromDatabase() throws QueryException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id,name,display_name,owner_uuid FROM guis");
            ResultSet result = statement.executeQuery();
            List<GuiDatabaseObject> guis = new ArrayList<>();
            while (result.next()) {
                GuiDatabaseObject gui = new GuiDatabaseObject(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("display_name"),
                        result.getString("owner_uuid")
                );
                guis.add(gui);
            }
            return guis;
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public int getHighestPageNumber(int guiId) throws QueryException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement;
            int maxPage = 0;

            statement = connection.prepareStatement("SELECT MAX(page) FROM tickets WHERE gui_id=?");
            statement.setInt(1, guiId);
            ResultSet ticketsResult = statement.executeQuery();
            if (ticketsResult.next()) maxPage = ticketsResult.getInt(1);

            statement = connection.prepareStatement("SELECT MAX(page) FROM links WHERE gui_id=?");
            statement.setInt(1, guiId);
            ResultSet linksResult = statement.executeQuery();
            int linksPage = 0;
            if (linksResult.next()) linksPage = linksResult.getInt(1);

            if (linksPage > maxPage) maxPage = linksPage;

            return maxPage;
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public void updateGuiName(int guiId, String newName) throws ModificationException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE guis SET name=? WHERE id=?");
            statement.setString(1, newName);
            statement.setInt(2, guiId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ModificationException(e);
        }
    }

    @Override
    public void updateGuiDisplayName(int guiId, String colouredDisplayName, String rawDisplayName) throws ModificationException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE guis SET display_name=?, raw_display_name=? WHERE id=?");
            statement.setString(1, colouredDisplayName);
            statement.setString(2, rawDisplayName);
            statement.setInt(3, guiId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ModificationException(e);
        }
    }

    @Override
    public void updateGuiOwner(int guiId, String uuid) throws ModificationException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE guis SET owner_uuid=? WHERE id=?");
            statement.setString(1, uuid);
            statement.setInt(2, guiId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ModificationException(e);
        }
    }

    @Override
    public Integer addGui(String name, String colouredDisplayName, String rawDisplayName, String ownerUuid) throws ModificationException {
        Integer guiId = null;
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO guis (name, display_name, raw_display_name, owner_uuid) VALUES (?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, colouredDisplayName);
            statement.setString(3, rawDisplayName);
            statement.setString(4, ownerUuid);
            statement.executeUpdate();

            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) guiId = result.getInt(1);

        } catch (SQLException e) {
            throw new ModificationException(e);
        }
        return guiId;
    }

    @Override
    public void insertPage(int guiId, int currentPage) throws ModificationException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement;

            statement = connection.prepareStatement("""
                    UPDATE tickets SET page = page + 1 WHERE gui_id = ? AND page >= ?
                    """);
            statement.setInt(1, guiId);
            statement.setInt(2, currentPage);
            statement.executeUpdate();

            statement = connection.prepareStatement("""
                    UPDATE links SET page = page + 1 WHERE gui_id = ? AND page >= ?
                    """);
            statement.setInt(1, guiId);
            statement.setInt(2, currentPage);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new ModificationException(e);
        }
    }

    @Override
    public void deleteGui(int guiId) throws ModificationException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM guis WHERE id=?");
            statement.setInt(1, guiId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ModificationException(e);
        }
    }

    @Override
    public void deletePage(int guiId, int page) throws ModificationException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement;

            statement = connection.prepareStatement("DELETE FROM tickets WHERE gui_id=? AND page=?");
            statement.setInt(1, guiId);
            statement.setInt(2, page);
            statement.executeUpdate();

            statement = connection.prepareStatement("DELETE FROM links WHERE gui_id=? AND page=?");
            statement.setInt(1, guiId);
            statement.setInt(2, page);
            statement.executeUpdate();

            statement = connection.prepareStatement("UPDATE tickets SET page = page - 1 WHERE gui_id=? AND page > ?");
            statement.setInt(1, guiId);
            statement.setInt(2, page);
            statement.executeUpdate();

            statement = connection.prepareStatement("UPDATE links SET page = page - 1 WHERE gui_id=? AND page > ?");
            statement.setInt(1, guiId);
            statement.setInt(2, page);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ModificationException(e);
        }
    }
}
