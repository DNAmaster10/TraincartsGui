package com.dnamaster10.traincartsticketshop.util.database.mariadb;

import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces.DatabaseTableCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MariaTableCreator extends MariaDatabaseAccessor implements DatabaseTableCreator {
    @Override
    public void createTables() throws ModificationException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement;
            statement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS players (
                        uuid varchar(50) PRIMARY KEY NOT NULL,
                        username varchar(20),
                        last_join bigint
                    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """);
            statement.execute();

            statement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS guis (
                        id int AUTO_INCREMENT PRIMARY KEY,
                        owner_uuid varchar(50),
                        name varchar(100) UNIQUE,
                        display_name varchar(100),
                        raw_display_name varchar(100),
                        FOREIGN KEY (owner_uuid) REFERENCES players(uuid)
                            ON DELETE SET NULL
                    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """);
            statement.execute();

            statement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS tickets (
                        id int AUTO_INCREMENT PRIMARY KEY,
                        gui_id int,
                        page int,
                        slot int,
                        tc_name varchar(100),
                        display_name varchar(100),
                        raw_display_name varchar(100),
                        UNIQUE KEY gui_page_slot_unique (gui_id, page, slot),
                        FOREIGN KEY (gui_id) REFERENCES guis(id)
                            ON DELETE CASCADE
                    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """);
            statement.execute();

            statement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS links (
                        id int AUTO_INCREMENT PRIMARY KEY,
                        gui_id int,
                        page int,
                        slot int,
                        linked_gui_id int,
                        linked_gui_page int DEFAULT 0,
                        display_name varchar(100),
                        raw_display_name varchar(100),
                        UNIQUE KEY gui_page_slot (gui_id, page, slot),
                        FOREIGN KEY (gui_id) REFERENCES guis(id)
                            ON DELETE CASCADE
                    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """);
            statement.execute();

            statement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS guieditors (
                        id int AUTO_INCREMENT PRIMARY KEY,
                        gui_id int NOT NULL,
                        editor_uuid varchar(50) NOT NULL,
                        FOREIGN KEY (editor_uuid) REFERENCES players(uuid)
                            ON DELETE CASCADE,
                        FOREIGN KEY (gui_id) REFERENCES guis(id)
                            ON DELETE CASCADE
                    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """);
            statement.execute();

            //Post plugin release updates
            statement = connection.prepareStatement("""
                    ALTER TABLE tickets ADD COLUMN IF NOT EXISTS purchase_message varchar(1000);
                    """);
            statement.execute();

        } catch (SQLException e) {
            throw new ModificationException(e);
        }
    }
}
