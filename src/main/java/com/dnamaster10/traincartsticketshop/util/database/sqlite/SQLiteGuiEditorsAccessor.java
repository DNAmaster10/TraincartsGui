package com.dnamaster10.traincartsticketshop.util.database.sqlite;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiEditorDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces.GuiEditorsDatabaseAccessor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteGuiEditorsAccessor extends SQLiteDatabaseAccessor implements GuiEditorsDatabaseAccessor {

    @Override
    public List<GuiEditorDatabaseObject> getAllGuiEditorsFromDatabase() throws QueryException {
        try (Connection connection = getConnection()) {
            //Get list of editor UUIDs
            PreparedStatement statement = connection.prepareStatement("SELECT gui_id,editor_uuid FROM guieditors");
            ResultSet result = statement.executeQuery();
            List<GuiEditorDatabaseObject> editors = new ArrayList<>();
            while (result.next()) {
                int guiId = result.getInt("gui_id");
                String uuid = result.getString("editor_uuid");
                editors.add(new GuiEditorDatabaseObject(guiId, uuid));
            }
            return editors;
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public void addGuiEditor(int guiId, String uuid) throws ModificationException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT OR IGNORE INTO guieditors (gui_id, editor_uuid)
                    VALUES (?, ?)
                    """);
            statement.setInt(1, guiId);
            statement.setString(2, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ModificationException(e);
        }
    }

    @Override
    public void removeGuiEditor(int guiId, String uuid) throws ModificationException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM guieditors WHERE gui_id=? AND editor_uuid=?");
            statement.setInt(1, guiId);
            statement.setString(2, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ModificationException(e);
        }
    }
}
