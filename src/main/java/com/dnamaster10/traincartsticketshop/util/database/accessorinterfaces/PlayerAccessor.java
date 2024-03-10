package com.dnamaster10.traincartsticketshop.util.database.accessorinterfaces;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.PlayerDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;

import java.util.List;

public interface PlayerAccessor {
    boolean checkPlayerByUsername(String username);

    List<PlayerDatabaseObject> getAllPlayersFromDatabase() throws QueryException;
    PlayerDatabaseObject getPlayerByUsername(String username);
    PlayerDatabaseObject getPlayerByUuid(String uuid);
    List<String> getPartialUsernameMatches(String inputString);

    void updatePlayer(String name, String uuid) throws ModificationException;
}
