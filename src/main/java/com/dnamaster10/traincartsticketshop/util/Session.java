package com.dnamaster10.traincartsticketshop.util;

import com.dnamaster10.traincartsticketshop.objects.guis.Gui;
import org.bukkit.entity.Player;

import java.util.Stack;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;

public class Session {
    //Holds information about a player's current gui session
    //Indicates the maximum amount of guis a player can have stored in their session stack
    private static final int maxGuis;
    private final Player owner;
    private final Stack<Gui> guis = new Stack<>();

    static {
        maxGuis = getPlugin().getConfig().getInt("MaxStoredGuisPerPlayer");
    }

    public Session(Player owner) {
        this.owner = owner;
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }
    }

    /**
     * Adds a gui to the player's Gui Stack.
     *
     * @param gui The Gui to be added to the stack
     */
    public void addGui(Gui gui) {
        guis.push(gui);
        if (guis.size() > maxGuis) {
            guis.remove(0);
        }
    }

    /**
     * Closes the current open Gui, and opens the previous Gui on the Gui Stack.
     */
    public void back() {
        if (guis.size() <= 1) {
            owner.closeInventory();
            return;
        }
        guis.pop();
        Gui previousGui = guis.peek();
        previousGui.open();
    }

    /**
     * Checks whether going back is possible.
     *
     * @return True if it's possible to go back
     */
    public boolean checkBack() {
        return guis.size() > 1;
    }
}
