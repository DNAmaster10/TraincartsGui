package com.dnamaster10.traincartsticketshop.util.database.accessors;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.PlayerDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import com.dnamaster10.traincartsticketshop.util.database.DatabaseAccessorFactory;
import com.dnamaster10.traincartsticketshop.util.database.databaseaccessorinterfaces.PlayerDatabaseAccessor;

import java.util.List;

public class PlayerDataAccessor extends DataAccessor {
    private final PlayerDatabaseAccessor playerDatabaseAccessor = DatabaseAccessorFactory.getPlayerDatabaseAccessor();

    /**
     * Checks whether the specified username exists within the database.
     *
     * @param username The username to check for
     * @return True of the username exists
     */
    public boolean checkPlayerByUsername(String username) {
        return getPlayerCache().checkPlayerByUsername(username);
    }

    /**
     * Gets a list of all players from the database.
     *
     * @return A List of all players
     * @throws QueryException Thrown if an error occurs accessing the database
     */
    public List<PlayerDatabaseObject> getAllPlayersFromDatabase() throws QueryException {
        return playerDatabaseAccessor.getAllPlayers();
    }

    /**
     * Gets a player from their username from the database.
     *
     * @param username The player's username
     * @return The corresponding PlayerDatabaseObject for the player
     */
    public PlayerDatabaseObject getPlayerByUsername(String username) {
        return getPlayerCache().getPlayerByUsername(username);
    }

    /**
     * Gets a player from their UUID from the database.
     *
     * @param uuid The player's UUID
     * @return The player
     */
    public PlayerDatabaseObject getPlayerByUuid(String uuid) {
        return getPlayerCache().getPlayerByUuid(uuid);
    }

    /**
     * Returns a list of usernames which partially match the input string.
     *
     * @param inputString The string to search for
     * @return A list of usernames
     */
    public List<String> getPartialUsernameMatches(String inputString) {
        return getPlayerCache().getPartialUsernameMatches(inputString);
    }

    /**
     * Updates a player in the database.
     *
     * @param username The player's username
     * @param uuid The player's UUID
     * @throws ModificationException Thrown if an error occurs modifying the database
     */
    public void updatePlayer(String username, String uuid) throws ModificationException {
        getPlayerCache().updatePlayer(username, uuid);
        playerDatabaseAccessor.updatePlayer(username, uuid);
    }
}
