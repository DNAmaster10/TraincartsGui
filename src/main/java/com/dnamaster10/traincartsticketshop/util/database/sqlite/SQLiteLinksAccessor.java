package com.dnamaster10.traincartsticketshop.util.database.sqlite;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.LinkDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces.LinksDatabaseAccessor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;

public class SQLiteLinksAccessor extends SQLiteDatabaseAccessor implements LinksDatabaseAccessor {
    @Override
    public LinkDatabaseObject[] getLinksByGuiId(int guiId, int page) throws QueryException {
        //Returns an array of links for a given gui ID and page number
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT slot, linked_gui_id, linked_gui_page, display_name, raw_display_name FROM links WHERE gui_id=? AND page=?");
            statement.setInt(1, guiId);
            statement.setInt(2, page);
            ResultSet result = statement.executeQuery();
            List<LinkDatabaseObject> linksList = new ArrayList<>();
            while (result.next()) {
                linksList.add(new LinkDatabaseObject(result.getInt("slot"), result.getInt("linked_gui_id"), result.getInt("linked_gui_page"), result.getString("display_name"), result.getString("raw_display_name")));
            }
            return linksList.toArray(LinkDatabaseObject[]::new);
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public LinkDatabaseObject[] searchLinks(int guiId, int offset, String searchTerm) throws QueryException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT linked_gui_id, linked_gui_page, display_name
                    FROM links
                    WHERE gui_id=? AND raw_display_name LIKE ?
                    ORDER BY raw_display_name LIMIT 45 OFFSET ?
                    """);
            statement.setInt(1, guiId);
            statement.setString(2, searchTerm + "%");
            statement.setInt(3, offset);
            ResultSet result = statement.executeQuery();
            List<LinkDatabaseObject> linkList = new ArrayList<>();
            int i = 0;
            while (result.next()) {
                linkList.add(new LinkDatabaseObject(i, result.getInt("linked_gui_id"), result.getInt("linked_gui_page"), result.getString("display_name"), null));
                i++;
            }
            return linkList.toArray(LinkDatabaseObject[]::new);
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public int getTotalLinks(int guiId) throws QueryException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM links WHERE gui_id=?");
            statement.setInt(1, guiId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public int getTotalLinkSearchResults(int guiId, String searchTerm) throws QueryException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM links WHERE gui_id=? AND raw_display_name LIKE ?");
            statement.setInt(1, guiId);
            statement.setString(2, searchTerm + "%");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public void saveLinkPage(int guiId, int page, List<LinkDatabaseObject> links) throws ModificationException {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            //Delete existing page
            PreparedStatement statement = connection.prepareStatement("DELETE FROM links WHERE gui_id=? AND page=?");
            statement.setInt(1, guiId);
            statement.setInt(2, page);
            statement.executeUpdate();

            if (!links.isEmpty()) {
                //Add new items
                statement = connection.prepareStatement("""
                        INSERT INTO links
                        (gui_id, page, slot, linked_gui_id, linked_gui_page, display_name, raw_display_name)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """);
                for (LinkDatabaseObject link : links) {
                    statement.setInt(1, guiId);
                    statement.setInt(2, page);
                    statement.setInt(3, link.slot());
                    statement.setInt(4, link.linkedGuiId());
                    statement.setInt(5, link.linkedGuiPage());
                    statement.setString(6, link.colouredDisplayName());
                    statement.setString(7, link.rawDisplayName());

                    statement.addBatch();
                }
                statement.executeBatch();
            }
            connection.commit();
        } catch (SQLException e) {
            try (Connection connection = getConnection()) {
                connection.rollback();
            } catch (SQLException re) {
                getPlugin().getLogger().severe("Rollback failed: " + re);
            }
            throw new ModificationException(e);
        }
    }
}
