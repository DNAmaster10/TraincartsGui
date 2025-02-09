package com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.TicketDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;

import java.util.List;

public interface TicketsDatabaseAccessor {
    TicketDatabaseObject[] getTickets(int guiId, int page) throws QueryException;
    TicketDatabaseObject[] searchTickets(int guiId, int offset, String searchTerm) throws QueryException;
    int getTotalTickets(int guiId) throws QueryException;
    int getTotalTicketSearchResults(int guiId, String searchTerm) throws QueryException;
    void saveTicketPage(int guiId, int page, List<TicketDatabaseObject> tickets) throws ModificationException;
}