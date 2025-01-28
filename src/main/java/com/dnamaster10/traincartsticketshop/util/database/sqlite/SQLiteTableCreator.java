package com.dnamaster10.traincartsticketshop.util.database.sqlite;

import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces.DatabaseTableCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLiteTableCreator extends SQLiteDatabaseAccessor implements DatabaseTableCreator {
    @Override
    public void createTables() throws ModificationException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement;

            //Players Table
            statement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS players (
                        uuid TEXT PRIMARY KEY NOT NULL,
                        username TEXT,
                        last_join INTEGER
                    )
                    """);
            statement.execute();

            //Guis Table
            statement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS guis (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        owner_uuid TEXT,
                        name TEXT UNIQUE,
                        display_name TEXT,
                        raw_display_name TEXT,
                        FOREIGN KEY (owner_uuid) REFERENCES players(uuid)
                    )
                    """);
            statement.execute();

            //Note that here we've had to remove the unique constraints on the following two tables.
            //This is because SQLite does not support ORDER BY within an UPDATE statement.
            //Tickets Table
            statement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS tickets (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        gui_id INTEGER,
                        page INTEGER,
                        slot INTEGER,
                        tc_name TEXT,
                        display_name TEXT,
                        raw_display_name TEXT,
                        purchase_message TEXT,
                        FOREIGN KEY (gui_id) REFERENCES guis(id)
                            ON DELETE CASCADE
                    )
                    """);
            statement.execute();
            statement = connection.prepareStatement("""
                    CREATE INDEX IF NOT EXISTS idx_tickets_gui_page_slot ON tickets (gui_id, page, slot)
                    """);
            statement.execute();

            //Links Table
            statement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS links (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        gui_id INTEGER,
                        page INTEGER,
                        slot INTEGER,
                        linked_gui_id INTEGER,
                        linked_gui_page INTEGER DEFAULT 0,
                        display_name TEXT,
                        raw_display_name TEXT,
                        FOREIGN KEY (gui_id) REFERENCES guis(id)
                            ON DELETE CASCADE
                    )
                    """);
            statement.execute();
            statement = connection.prepareStatement("""
                    CREATE INDEX IF NOT EXISTS idx_links_gui_page_slot ON links (gui_id, page, slot)
                    """);
            statement.execute();

            //GuiEditors Table
            statement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS guieditors (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        gui_id INTEGER NOT NULL,
                        editor_uuid TEXT NOT NULL,
                        FOREIGN KEY (editor_uuid) REFERENCES players(uuid)
                            ON DELETE CASCADE,
                        FOREIGN KEY (gui_id) REFERENCES guis(id)
                            ON DELETE CASCADE
                    )
                    """);
            statement.execute();


        } catch (SQLException e) {
            throw new ModificationException(e);
        }
    }
}
