package com.dnamaster10.traincartsticketshop.util.database.accessors;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.TicketDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import com.dnamaster10.traincartsticketshop.util.database.DatabaseAccessorFactory;
import com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces.TicketsDatabaseAccessor;

import java.util.List;

public class TicketDataAccessor extends DataAccessor {
    TicketsDatabaseAccessor ticketsDatabaseAccessor = DatabaseAccessorFactory.getTicketDatabaseAccessor();

    /**
     * Gets an array of tickets within a specific page within a Gui.
     *
     * @param guiId The Gui ID
     * @param page The page number
     * @return An array of tickets within the specified Gui
     * @throws QueryException Thrown if an error occurs accessing the database
     */
    public TicketDatabaseObject[] getTickets(int guiId, int page) throws QueryException {
        return ticketsDatabaseAccessor.getTickets(guiId, page);
    }

    /**
     * Searches tickets by their display name within a specified Gui.
     * Note that this will return the maximum number of elements which can be held within a page.
     *
     * @param guiId The Gui ID
     * @param offset The number of elements to "skip over" when performing the search
     * @param searchTerm The search term to search
     * @return An array of tickets
     * @throws QueryException Thrown if an error occurs accessing the database
     */
    public TicketDatabaseObject[] searchTickets(int guiId, int offset, String searchTerm) throws QueryException {
        return ticketsDatabaseAccessor.searchTickets(guiId, offset, searchTerm);
    }

    /**
     * Returns the total number of tickets within the Gui.
     *
     * @param guiId The Gui ID
     * @return The number of tickets within the Gui
     * @throws QueryException Thrown if an error occurs accessing the database
     */
    public int getTotalTickets(int guiId) throws QueryException {
        return ticketsDatabaseAccessor.getTotalTickets(guiId);
    }

    /**
     * Returns the total number of tickets, within a Gui, whose display name matches the search term.
     *
     * @param guiId The Gui ID
     * @param searchTerm The search term to search
     * @return The number of tickets
     * @throws QueryException Thrown if an error occurs accessing the database
     */
    public int getTotalTicketSearchResults(int guiId, String searchTerm) throws QueryException {
        return ticketsDatabaseAccessor.getTotalTicketSearchResults(guiId, searchTerm);
    }

    /**
     * Saves tickets to the database.
     *
     * @param guiId The Gui ID
     * @param page The page number to use
     * @param tickets The list of tickets to save
     * @throws ModificationException Thrown if an error occurs modifying the database
     */
    public void saveTicketPage(int guiId, int page, List<TicketDatabaseObject> tickets) throws ModificationException {
        ticketsDatabaseAccessor.saveTicketPage(guiId, page, tickets);
    }
}