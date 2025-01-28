package com.dnamaster10.traincartsticketshop.util.database.accessors;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiEditorDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import com.dnamaster10.traincartsticketshop.util.database.DatabaseAccessorFactory;
import com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces.GuiEditorsDatabaseAccessor;

import java.util.List;

public class GuiEditorsDataAccessor extends DataAccessor {
    private final GuiEditorsDatabaseAccessor guiEditorsDatabaseAccessor = DatabaseAccessorFactory.getGuiEditorsDatabaseAccessor();

    /**
     * Checks whether a player is an editor of a Gui.
     *
     * @param guiId The Gui ID to check
     * @param uuid The UUID of the player
     * @return True if the player is an editor for that Gui
     */
    public boolean checkGuiEditorByUuid(int guiId, String uuid) {
        return getGuiEditorsCache().checkGuiEditorByUuid(guiId, uuid);
    }

    /**
     * Gets all Gui editors for all Guis.
     *
     * @return A List of all Gui editors
     * @throws QueryException Thrown if an error occurs accessing the database
     */
    public List<GuiEditorDatabaseObject> getAllGuiEditorsFromDatabase() throws QueryException {
        return guiEditorsDatabaseAccessor.getAllGuiEditorsFromDatabase();
    }

    /**
     * Gets the usernames for the players who are registered editors of the specified Gui.
     *
     * @param guiId The ID of the Gui
     * @return A list of usernames
     */
    public List<String> getEditorUsernames(int guiId) {
        return getGuiEditorsCache().getEditorUsernamesForGui(guiId);
    }

    /**
     * Gets a list of Guis which the specified player can edit.
     *
     * @param uuid The UUID of the player
     * @return A List of the Guis which the player can edit
     */
    public List<GuiDatabaseObject> getGuisEditableByEditor(String uuid) {
        //Note that this does not return guis the given player owns
        return getGuiEditorsCache().getGuisEditableByEditor(uuid);
    }

    /**
     * Registers a player as an editor of a Gui.
     *
     * @param guiId The Gui ID
     * @param uuid The UUID of the player editor
     * @throws ModificationException Thrown if an error occurs modifying the database
     */
    public void addGuiEditor(int guiId, String uuid) throws ModificationException {
        getGuiEditorsCache().addGuiEditor(uuid, guiId);
        guiEditorsDatabaseAccessor.addGuiEditor(guiId, uuid);
    }

    /**
     * Removes a player as an editor from a Gui.
     *
     * @param guiId The Gui ID
     * @param uuid The UUID of the player to remove
     * @throws ModificationException Thrown if an error occurs modifying the database
     */
    public void removeGuiEditor(int guiId, String uuid) throws ModificationException {
        getGuiEditorsCache().removeGuiEditor(uuid, guiId);
        guiEditorsDatabaseAccessor.removeGuiEditor(guiId, uuid);
    }
}
