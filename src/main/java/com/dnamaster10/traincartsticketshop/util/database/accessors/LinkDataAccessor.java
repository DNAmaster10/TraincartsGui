package com.dnamaster10.traincartsticketshop.util.database.accessors;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.LinkDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import com.dnamaster10.traincartsticketshop.util.database.DatabaseAccessorFactory;
import com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces.LinksDatabaseAccessor;

import java.util.List;

public class LinkDataAccessor extends DataAccessor {
    LinksDatabaseAccessor linksDatabaseAccessor = DatabaseAccessorFactory.getLinksDatabaseAccessor();

    /**
     * Gets an array of links within a specific page within a Gui.
     *
     * @param guiId The Gui ID
     * @param page The page number
     * @return An array of links within the specified Gui page
     * @throws QueryException Thrown if an error occurs accessing the database
     */
    public LinkDatabaseObject[] getLinks(int guiId, int page) throws QueryException {
        return linksDatabaseAccessor.getLinksByGuiId(guiId, page);
    }

    /**
     * Searches links by their display name within a specified Gui.
     * Note that this will return the maximum number of elements which can be held within a page.
     *
     * @param guiId The Gui ID
     * @param offset The number of elements to "skip over" when performing the search
     * @param searchTerm The search term to search
     * @return An array of links
     * @throws QueryException Thrown if an error occurs accessing the database
     */
    public LinkDatabaseObject[] searchLinks(int guiId, int offset, String searchTerm) throws QueryException {
        return linksDatabaseAccessor.searchLinks(guiId, offset, searchTerm);
    }

    /**
     * Returns the total number of links within the Gui.
     *
     * @param guiId The Gui ID
     * @return The number of tickets within the Gui
     * @throws QueryException Thrown if an error occurs accessing the database
     */
    public int getTotalLinks(int guiId) throws QueryException {
        return linksDatabaseAccessor.getTotalLinks(guiId);
    }

    /**
     * Returns the total number of links, within a Gui, whose display name matches the search term.
     *
     * @param guiId The Gui ID
     * @param searchTerm The search term to search
     * @return The number of links
     * @throws QueryException Thrown if an error occurs accessing the database
     */
    public int getTotalLinkSearchResults(int guiId, String searchTerm) throws QueryException {
        return linksDatabaseAccessor.getTotalLinkSearchResults(guiId, searchTerm);
    }

    /**
     * Saves links to the database.
     *
     * @param guiId The Gui ID
     * @param page The page number to use
     * @param links The list of links to save
     * @throws ModificationException Thrown if an error occurs modifying the database
     */
    public void saveLinkPage(int guiId, int page, List<LinkDatabaseObject> links) throws ModificationException {
        linksDatabaseAccessor.saveLinkPage(guiId, page, links);
    }
}
