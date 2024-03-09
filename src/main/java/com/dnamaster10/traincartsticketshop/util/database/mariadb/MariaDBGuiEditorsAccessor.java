package com.dnamaster10.traincartsticketshop.util.database.mariadb;

import com.dnamaster10.traincartsticketshop.util.database.accessorinterfaces.GuiEditorsAccessor;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiEditorsDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MariaDBGuiEditorsAccessor extends MariaDBDatabaseAccessor implements GuiEditorsAccessor {

    public boolean checkGuiEditorByUuid(int guiId, String uuid) {
        //Returns true if uuid appears in gui edit list for the given gui id
        return getGuiEditorsCache().checkGuiEditorByUuid(guiId, uuid);
    }

    public HashMap<Integer, GuiEditorsDatabaseObject> getAllGuiEditorsFromDatabase() throws QueryException {
        try (Connection connection = getConnection()) {
            //Get list of editor UUIDs
            PreparedStatement statement = connection.prepareStatement("SELECT gui_id,editor_uuid FROM guieditors");
            ResultSet result = statement.executeQuery();
            HashMap<Integer, GuiEditorsDatabaseObject> guiEditors = new HashMap<>();
            while (result.next()) {
                int guiId = result.getInt("gui_id");
                String uuid = result.getString("editor_uuid");
                if (!guiEditors.containsKey(guiId)) {
                    guiEditors.put(guiId, new GuiEditorsDatabaseObject(guiId, new ArrayList<>()));
                }
                //Add the editor
                guiEditors.get(guiId).editorUuids().add(uuid);
            }
            return guiEditors;
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    public List<String> getEditorUsernames(int guiId, int startIndex, int limit) throws QueryException {
        //Returns a list of username of players who are a registered editor of a gui
        return getGuiEditorsCache().getEditorUsernamesForGui(guiId);
    }

    public void addGuiEditor(int guiId, String uuid) throws ModificationException {
        //Adds a player as an editor for the given gui
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement( """
                    INSERT INTO guieditors (gui_id, editor_uuid)
                    VALUES (?, ?)
                    ON DUPLICATE KEY UPDATE
                        gui_id=VALUES(gui_id),
                        editor_uuid=VALUES(editor_uuid)
                    """);
            statement.setInt(1, guiId);
            statement.setString(2, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ModificationException(e);
        }
        getGuiEditorsCache().addGuiEditor(uuid, guiId);
    }

    public void removeGuiEditor(int guiId, String uuid) throws ModificationException {
        //Removes an editor from a gui
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM guieditors WHERE gui_id=? AND editor_uuid=?");
            statement.setInt(1, guiId);
            statement.setString(2, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ModificationException(e);
        }
        getGuiEditorsCache().removeGuiEditor(uuid, guiId);
    }

}
