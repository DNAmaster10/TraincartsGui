package com.dnamaster10.traincartsticketshop.util.database.databaseobjects;

/**
 * Holds information pertaining to a specific ticket.
 *
 * @param slot The slot within the page in which the ticket can be found.
 * @param tcName The Traincarts ticket name
 * @param colouredDisplayName The coloured display name
 * @param rawDisplayName The stripped display name
 * @param purchaseMessage The message to be displayed when the ticket is purchased
 */
public record TicketDatabaseObject (
        int slot,
        String tcName,
        String colouredDisplayName,
        String rawDisplayName,
        String purchaseMessage
) {}
