package com.dnamaster10.traincartsticketshop.util;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class GuiManager {
    //Holds all current sessions bound to a specific player
    private final HashMap<Player, Session> sessions = new HashMap<>();
    //A hashmap containing all guis which are currently being edited
    private final HashMap<Integer, Player> openEditGuis = new HashMap<>();

    /**
     * Returns the Session currently tied to the given player.
     *
     * @param player The player whose session should be returned
     * @return The player's session
     */
    public Session getSession(Player player) {
        return sessions.get(player);
    }

    /**
     * Opens a new session for the specified player.
     *
     * @param player The player to open a new session for
     */
    public void openNewSession(Player player) {
        //Creates a new session, removes a player's old session, and returns the new session
        Session newSession = new Session(player);
        sessions.put(player, newSession);
    }

    /**
     * Closes the specified player's session.
     *
     * @param player The player whose session should be closed
     */
    public void closeSession(Player player) {
        sessions.remove(player);
        openEditGuis.values().remove(player);
    }

    /**
     * Registers an edit Gui. Used for ensuring that only one player can edit any given Gui at a time.
     *
     * @param guiId The ID of the Gui
     * @param player The player who is editing the Gui
     */
    public void addEditGui(int guiId, Player player) {
        openEditGuis.put(guiId, player);
    }

    /**
     * Unregisters an edit Gui. Should be called when a player closes an edit Gui to allow other players to edit the Gui.
     *
     * @param guiId The ID of the Gui
     */
    public void removeEditGui(int guiId) {
        openEditGuis.remove(guiId);
    }

    /**
     * Gets the player currently editing the given Gui.
     *
     * @param guiId The ID of the Gui
     * @return The player who is currently editing the given Gui. Returns null if no player is editing that Gui
     */
    public Player getGuiEditor(int guiId) {
        //Returns the player who is editing a gui
        return openEditGuis.get(guiId);
    }

    /**
     * Handles a player leave event. Closes any open sessions for the player, and unregisters any Guis which the player has open.
     *
     * @param player The player who has disconnected
     */
    public void handlePlayerLeave(Player player) {
        Session session = getSession(player);
        if (session == null) return;
        closeSession(player);
    }
}
