package com.dnamaster10.traincartsticketshop.util;

import com.bergerkiller.bukkit.tc.tickets.Ticket;
import com.bergerkiller.bukkit.tc.tickets.TicketStore;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bergerkiller.bukkit.tc.tickets.TicketStore.*;

public class Traincarts {
    //Contains methods which interact or involve the traincarts plugin

    /**
     * Checks if the ticket is present in Traincarts.
     *
     * @param name Name of the ticket to check
     * @return True if the ticket was found
     */
    public static boolean checkTicket(String name) {
        return getTicket(name) != null;
    }

    /**
     * Checks whether an item is a Traincarts ticket.
     *
     * @param item Item to check
     * @return True if the item is a Traincarts ticket
     */
    public static boolean isTraincartsTicket(ItemStack item) {
        return isTicketItem(item);
    }

    /**
     * Converts a Traincarts ticket item into a Ticket Shop ticket item.
     *
     * @param traincartsTicketItem Traincarts ticket ItemStack
     * @return Ticket Shop Ticket ItemStack
     */
    public static com.dnamaster10.traincartsticketshop.objects.buttons.Ticket getAsTicketShopTicket(ItemStack traincartsTicketItem) {
        Ticket traincartsTicket = getTicketFromItem(traincartsTicketItem);
        if (traincartsTicket == null) return null;
        String ticketName = traincartsTicket.getName();
        return new com.dnamaster10.traincartsticketshop.objects.buttons.Ticket(ticketName, ticketName, null);
    }

    /**
     * Gives a Traincarts ticket to the specified player.
     *
     * @param tcName The name of the Traincarts ticket
     * @param player The player to give the ticket to
     */
    public static void giveTicketItem(String tcName, Player player) {
        Ticket ticket = getTicket(tcName);

        ItemStack item = ticket.createItem(player);
        player.getInventory().addItem(item);
    }

    /**
     * Gets a list of all Traincarts ticket names.
     *
     * @return A List of Strings of all Traincarts tickets
     */
    private static List<String> getTicketNames() {
        return TicketStore.getAll().stream()
                .map(Ticket::getName)
                .collect(Collectors.toList());
    }

    public static List<String> getPartialTicketNameCompletions(String inputString) {
        return StringUtil.copyPartialMatches(inputString, getTicketNames(), new ArrayList<>());
    }
}
