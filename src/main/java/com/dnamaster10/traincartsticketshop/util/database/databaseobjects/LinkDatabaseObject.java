package com.dnamaster10.traincartsticketshop.util.database.databaseobjects;

/**
 * Holds information pertaining to a specific link.
 *
 * @param slot The slot in the page where the Link can be found
 * @param linkedGuiId The Gui which this link links to
 * @param linkedGuiPage The page in the linked Gui which this link links to
 * @param colouredDisplayName The coloured item display name
 * @param rawDisplayName The stripped display name
 */
public record LinkDatabaseObject (
        int slot,
        int linkedGuiId,
        int linkedGuiPage,
        String colouredDisplayName,
        String rawDisplayName
) {}