package com.dnamaster10.traincartsticketshop.util.database.caches;

import com.dnamaster10.traincartsticketshop.util.database.accessors.PlayerDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.PlayerDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Holds caches of information relating to players. Should only be accessed via a PlayerDataAccessor
 * @see com.dnamaster10.traincartsticketshop.util.database.accessors.PlayerDataAccessor
 */
public class PlayerCache {
    final List<String> usernames = new CopyOnWriteArrayList<>();
    final ConcurrentHashMap<String, PlayerDatabaseObject> uuidPlayerMap = new ConcurrentHashMap<>();
    final ConcurrentHashMap<String, PlayerDatabaseObject> usernamePlayerMap = new ConcurrentHashMap<>();

    public void initialize() throws QueryException {
        PlayerDataAccessor playerAccessor = new PlayerDataAccessor();
        List<PlayerDatabaseObject> players = playerAccessor.getAllPlayersFromDatabase();
        for (PlayerDatabaseObject player : players) {
            usernames.add(player.username());
            uuidPlayerMap.put(player.uuid().toLowerCase(), player);
            usernamePlayerMap.put(player.username().toLowerCase(), player);
        }
    }

    public boolean checkPlayerByUsername(String username) {
        return usernamePlayerMap.containsKey(username.toLowerCase());
    }

    public PlayerDatabaseObject getPlayerByUsername(String username) {
        return usernamePlayerMap.get(username.toLowerCase());
    }
    public PlayerDatabaseObject getPlayerByUuid(String uuid) {
        return uuidPlayerMap.get(uuid);
    }
    public List<String> getPartialUsernameMatches(String inputString) {
        return StringUtil.copyPartialMatches(inputString, usernames, new ArrayList<>());
    }

    public void updatePlayer(String username, String uuid) {
        if (!usernames.contains(username)) {
            usernames.add(username);
        }
        PlayerDatabaseObject newPlayer = new PlayerDatabaseObject(
                username,
                uuid
        );
        uuidPlayerMap.put(uuid.toLowerCase(), newPlayer);
        usernamePlayerMap.put(username.toLowerCase(), newPlayer);
    }
}
