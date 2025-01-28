package com.dnamaster10.traincartsticketshop.util.database;

import com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces.*;
import com.dnamaster10.traincartsticketshop.util.database.mariadb.*;
import com.dnamaster10.traincartsticketshop.util.database.sqlite.*;

import java.util.Objects;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;

/**
 * Used for deciding which methods should be called and how they should be called, depending on the database type being used, e.g. MariaDB vs SQLite.
 */
public class DatabaseAccessorFactory {
    enum DatabaseType {
        MARIA,
        SQLITE
    }
    private static final DatabaseType databaseType;

    static {
        if (getPlugin().getConfig().getString("database.type") == null) {
            databaseType = null;
            getPlugin().getLogger().severe("Please set a database type in the config");
            getPlugin().disable();
        } else {
            String dbType = Objects.requireNonNull(getPlugin().getConfig().getString("database.type")).toLowerCase();
            if (dbType.equals("mariadb")) {
                databaseType = DatabaseType.MARIA;
            } else if (dbType.equals("sqlite")) {
                databaseType = DatabaseType.SQLITE;
            } else {
                databaseType = null;
                getPlugin().getLogger().severe("Database type \"" + dbType + "\" is not supported");
                getPlugin().disable();
            }
        }
    }

    public static PlayerDatabaseAccessor getPlayerDatabaseAccessor() {
        switch (databaseType) {
            case MARIA -> {
                return new MariaPlayerAccessor();
            }
            case SQLITE -> {
                return new SQLitePlayerAccessor();
            }
        }
        return new SQLitePlayerAccessor();
    }

    public static GuiEditorsDatabaseAccessor getGuiEditorsDatabaseAccessor() {
        switch (databaseType) {
            case MARIA -> {
                return new MariaGuiEditorsAccessor();
            }
            case SQLITE -> {
                return new SQLiteGuiEditorsAccessor();
            }
        }
        return new SQLiteGuiEditorsAccessor();
    }

    public static GuiDatabaseAccessor getGuiDatabaseAccessor() {
        switch (databaseType) {
            case MARIA -> {
                return new MariaGuiDatabaseAccessor();
            }
            case SQLITE -> {
                return new SQLiteGuiDatabaseAccessor();
            }
        }
        return new SQLiteGuiDatabaseAccessor();
    }

    public static LinksDatabaseAccessor getLinksDatabaseAccessor() {
        switch (databaseType) {
            case MARIA -> {
                return new MariaLinksAccessor();
            }
            case SQLITE -> {
                return new SQLiteLinksAccessor();
            }
        }
        return new SQLiteLinksAccessor();
    }

    public static TicketsDatabaseAccessor getTicketDatabaseAccessor() {
        switch (databaseType) {
            case MARIA -> {
                return new MariaTicketAccessor();
            }
            case SQLITE -> {
                return new SQLiteTicketAccessor();
            }
        }
        return new SQLiteTicketAccessor();
    }

    public static DatabaseTableCreator getDatabaseTableCreator() {
        switch (databaseType) {
            case MARIA -> {
                return new MariaTableCreator();
            }
            case SQLITE -> {
                return new SQLiteTableCreator();
            }
        }
        return new SQLiteTableCreator();
    }
}
