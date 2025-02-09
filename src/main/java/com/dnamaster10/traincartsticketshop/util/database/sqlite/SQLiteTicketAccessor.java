package com.dnamaster10.traincartsticketshop.util.database.sqlite;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.TicketDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces.TicketsDatabaseAccessor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;

public class SQLiteTicketAccessor extends SQLiteDatabaseAccessor implements TicketsDatabaseAccessor {

    @Override
    public TicketDatabaseObject[] getTickets(int guiId, int page) throws QueryException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT slot, tc_name, display_name, raw_display_name, purchase_message
                    FROM tickets
                    WHERE gui_id=? AND page=?
                    """);
            statement.setInt(1, guiId);
            statement.setInt(2, page);
            ResultSet result = statement.executeQuery();
            List<TicketDatabaseObject> ticketList = new ArrayList<>();
            while (result.next()) {
                ticketList.add(new TicketDatabaseObject(
                        result.getInt("slot"),
                        result.getString("tc_name"),
                        result.getString("display_name"),
                        result.getString("raw_display_name"),
                        result.getString("purchase_message")
                ));
            }
            return ticketList.toArray(TicketDatabaseObject[]::new);
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public TicketDatabaseObject[] searchTickets(int guiId, int offset, String searchTerm) throws QueryException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT tc_name, display_name, raw_display_name, purchase_message
                    FROM tickets WHERE gui_id=? AND raw_display_name LIKE ?
                    ORDER BY raw_display_name LIMIT 45 OFFSET ?
                    """);
            statement.setInt(1, guiId);
            statement.setString(2, searchTerm + "%");
            statement.setInt(3, offset);
            ResultSet result = statement.executeQuery();
            List<TicketDatabaseObject> ticketList = new ArrayList<>();
            int i = 0;
            while (result.next()) {
                ticketList.add(new TicketDatabaseObject(
                        i,
                        result.getString("tc_name"),
                        result.getString("display_name"),
                        result.getString("raw_display_name"),
                        result.getString("purchase_message")
                ));
                i++;
            }
            return ticketList.toArray(TicketDatabaseObject[]::new);
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public int getTotalTickets(int guiId) throws QueryException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM tickets WHERE gui_id=?");
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
    public int getTotalTicketSearchResults(int guiId, String searchTerm) throws QueryException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM tickets WHERE gui_id=? AND raw_display_name LIKE ?");
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
    public void saveTicketPage(int guiId, int page, List<TicketDatabaseObject> tickets) throws ModificationException {
        try (Connection connection = getConnection()) {
            //Disable autocommit for SQLite performance reasons
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement("DELETE FROM tickets WHERE gui_id=? AND page=?");
            statement.setInt(1, guiId);
            statement.setInt(2, page);
            statement.executeUpdate();

            if (!tickets.isEmpty()) {
                //Add new items
                statement = connection.prepareStatement("""
                        INSERT INTO tickets
                        (gui_id, page, slot, tc_name, display_name, raw_display_name, purchase_message)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """);
                for (TicketDatabaseObject ticket : tickets) {
                    statement.setInt(1, guiId);
                    statement.setInt(2, page);
                    statement.setInt(3, ticket.slot());
                    statement.setString(4, ticket.tcName());
                    statement.setString(5, ticket.colouredDisplayName());
                    statement.setString(6, ticket.rawDisplayName());
                    statement.setString(7, ticket.purchaseMessage());

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
