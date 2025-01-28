package com.dnamaster10.traincartsticketshop.util.database.databaseobjects;

/**
 * Holds information pertaining to a specific player.
 *
 * @param username The player's username
 * @param uuid The player's UUID
 */
public record PlayerDatabaseObject (
    String username,
    String uuid
) {}
