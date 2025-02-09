package com.dnamaster10.traincartsticketshop.util.database.accessors;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import com.dnamaster10.traincartsticketshop.util.database.DatabaseAccessorFactory;
import com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces.GuiDatabaseAccessor;

import java.util.List;

public class GuiDataAccessor extends DataAccessor {
    private final GuiDatabaseAccessor guiDatabaseAccessor = DatabaseAccessorFactory.getGuiDatabaseAccessor();

    /**
     * Checks whether the Gui exists.
     *
     * @param name The name of the Gui to check for
     * @return True if the Gui exists
     */
    public boolean checkGuiByName(String name) {
        return getGuiCache().checkGuiByName(name);
    }

    /**
     * Checks whether the Gui exists.
     *
     * @param id The ID of the Gui to check for
     * @return True if the Gui exists
     */
    public boolean checkGuiById(int id) {
        return getGuiCache().checkGuiById(id);
    }

    /**
     * Checks whether the specified player owns the specified Gui
     *
     * @param guiId The ID of the Gui to check
     * @param ownerUuid The UUID of the player
     * @return True if the player owns the Gui
     */
    public boolean checkGuiOwnerByUuid(int guiId, String ownerUuid) {
        return getGuiCache().checkGuiOwnerByUuid(guiId, ownerUuid);
    }

    /**
     * Returns true if the specified player has adequate permissions to edit the Gui
     * @param guiId The ID of the Gui to check
     * @param uuid The UUID of the player
     * @return True if the player can edit the Gui
     */
    public boolean playerCanEdit(int guiId, String uuid) {
        return getGuiCache().checkGuiOwnerByUuid(guiId, uuid) || getGuiEditorsCache().checkGuiEditorByUuid(guiId, uuid);
    }

    /**
     * Gets a list of all Guis.
     *
     * @return A list of GuiDatabaseObjects
     * @throws QueryException Thrown if an error occurs accessing the database
     */
    public List<GuiDatabaseObject> getGuisFromDatabase() throws QueryException {
        return guiDatabaseAccessor.getGuisFromDatabase();
    }

    /**
     * Gets a Gui's ID from its name.
     *
     * @param name The name of the Gui
     * @return The Gui's ID
     */
    public Integer getGuiIdByName(String name) {
        return getGuiCache().getGuiIdByName(name);
    }

    /**
     * Gets a Gui's name from its ID.
     *
     * @param id The ID of the Gui
     * @return The name of the Gui
     */
    public String getGuiNameById(int id) {
        return getGuiCache().getGuiNameById(id);
    }

    /**
     * Returns the highest page number for the given Gui.
     * Note that the database stores pages starting at page 0, so a Gui with only 1 page will return 0 as the highest page number.
     *
     * @param guiId The ID for the Gui
     * @return The highest page number
     * @throws QueryException Thrown if an error occurs accessing the database
     */
    public int getHighestPageNumber(int guiId) throws QueryException {
        return guiDatabaseAccessor.getHighestPageNumber(guiId);
    }

    /**
     * Gets the Gui's coloured display name.
     *
     * @param guiId Gui ID
     * @return The coloured display name
     */
    public String getDisplayName(int guiId) {
        return getGuiCache().getDisplayNameById(guiId);
    }

    /**
     * Gets the username of the owner of the specified Gui.
     *
     * @param guiId Gui ID
     * @return The username of the owner
     */
    public String getOwnerUsername(int guiId) {
        String uuid = getGuiCache().getGuiById(guiId).ownerUuid();
        return getPlayerCache().getPlayerByUuid(uuid).username();
    }

    /**
     * Gets the UUID of the owner of the specified Gui.
     *
     * @param guiId Gui ID
     * @return The UUID of the owner
     */
    public String getOwnerUuid(int guiId) {
        return getGuiCache().getGuiById(guiId).ownerUuid();
    }

    /**
     * Gets a list of Gui names which at least partially match the input string.
     *
     * @param argument Partial match String
     * @return A list of Gui names
     */
    public List<String> getPartialNameMatches(String argument) {
        return getGuiCache().getPartialNameMatches(argument);
    }

    /**
     * Gets a list of Guis owned by the specified player.
     *
     * @param uuid The UUID of the player
     * @return A list of GuiDatabaseObjects which the player owns
     */
    public List<GuiDatabaseObject> getGuisOwnedBy(String uuid) {
        return getGuiCache().getGuisOwnedBy(uuid);
    }

    /**
     * Gets a list of Guis which the specified player is able to edit.
     *
     * @param uuid The UUID of the player
     * @return A list of GuisDatabaseObjects which the player can edit
     */
    public List<GuiDatabaseObject> getGuisEditableBy(String uuid) {
        //Note that this includes both guis the player owns, and also guis the player is a registered editor of. The GuiEditorAccessor must be used to *only* get guis
        //which a player is a registered editor of.
        List<GuiDatabaseObject> ownedGuis = getGuisOwnedBy(uuid);
        List<GuiDatabaseObject> editorGuis = getGuiEditorsCache().getGuisEditableByEditor(uuid);
        ownedGuis.addAll(editorGuis);
        return ownedGuis;
    }

    /**
     * Gets a gui from a GuiID.
     *
     * @param guiId Gui ID
     * @return The GuiDatabaseObject for the Gui
     */
    public GuiDatabaseObject getGuiById(int guiId) {
        return getGuiCache().getGuiById(guiId);
    }

    /**
     * Gets a gui from a Gui name
     *
     * @param name Gui name
     * @return The GuiDatabaseObject for the Gui
     */
    public GuiDatabaseObject getGuiByName(String name) {
        return getGuiCache().getGuiByName(name);
    }

    /**
     * Updates the name of a Gui in the database.
     *
     * @param guiId The ID for the Gui
     * @param newName The new name for the Gui
     * @throws ModificationException Thrown if an error occurs modifying the database
     */
    public void updateGuiName(int guiId, String newName) throws ModificationException{
        guiDatabaseAccessor.updateGuiName(guiId, newName);
        getGuiCache().updateGuiName(guiId, newName);
    }

    /**
     * Updates the display name of a Gui in the database.
     *
     * @param guiId The ID for the Gui
     * @param colouredDisplayName The colour formatted display name
     * @param rawDisplayName The stripped display name
     * @throws ModificationException Thrown if an error occurs modifying the database
     */
    public void updateGuiDisplayName(int guiId, String colouredDisplayName, String rawDisplayName) throws ModificationException {
        guiDatabaseAccessor.updateGuiDisplayName(guiId, colouredDisplayName, rawDisplayName);
        getGuiCache().updateGuiDisplayName(guiId, colouredDisplayName);
    }

    /**
     * Updates the owner of a Gui in the database.
     *
     * @param guiId The ID for the Gui
     * @param uuid The UUID of the new owner
     * @throws ModificationException Thrown if an error occurs modifying the database
     */
    public void updateGuiOwner(int guiId, String uuid) throws ModificationException {
        guiDatabaseAccessor.updateGuiOwner(guiId, uuid);
        getGuiCache().updateGuiOwner(guiId, uuid);
    }

    /**
     * Adds a new Gui to the database.
     *
     * @param name The name of the new Gui
     * @param colouredDisplayName The colour formatted display name
     * @param rawDisplayName The stripped display name
     * @param ownerUuid The UUID of the owner
     * @throws ModificationException Thrown if an error occurs modifying the database
     */
    public void addGui(String name, String colouredDisplayName, String rawDisplayName, String ownerUuid) throws ModificationException {
        Integer guiId = guiDatabaseAccessor.addGui(name, colouredDisplayName, rawDisplayName, ownerUuid);
        if (guiId == null) return;
        getGuiCache().addGui(new GuiDatabaseObject(guiId, name, colouredDisplayName, ownerUuid));
    }

    /**
     * Inserts a page in the database for the given Gui.
     *
     * @param guiId The ID for the Gui
     * @param currentPage The page which should have a new page inserted before itself
     * @throws ModificationException Thrown if an error occurs modifying the database
     */
    public void insertPage(int guiId, int currentPage) throws ModificationException {
        guiDatabaseAccessor.insertPage(guiId, currentPage);
    }

    /**
     * Deletes a Gui from the database.
     *
     * @param guiId The ID for the Gui
     * @throws ModificationException Thrown if an error occurs modifying the database
     */
    public void deleteGui(int guiId) throws ModificationException {
        guiDatabaseAccessor.deleteGui(guiId);
        getGuiCache().deleteGuiById(guiId);
        getGuiEditorsCache().removeGui(guiId);
    }

    /**
     * Deletes a page within the specified Gui.
     *
     * @param guiId The ID for the Gui
     * @param page The number of the page to be deleted
     * @throws ModificationException Thrown if an error occurs modifying the database
     */
    public void deletePage(int guiId, int page) throws ModificationException {
        guiDatabaseAccessor.deletePage(guiId, page);
    }
}