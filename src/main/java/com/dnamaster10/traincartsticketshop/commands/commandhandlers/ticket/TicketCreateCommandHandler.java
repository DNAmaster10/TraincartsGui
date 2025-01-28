package com.dnamaster10.traincartsticketshop.commands.commandhandlers.ticket;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.SyncCommandHandler;
import com.dnamaster10.traincartsticketshop.objects.buttons.Ticket;
import com.dnamaster10.traincartsticketshop.util.Traincarts;
import com.dnamaster10.traincartsticketshop.util.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The command handler for the /tshop ticket create comand.
 */
public class TicketCreateCommandHandler extends SyncCommandHandler {
    //Example command: /tshop ticket create <tc ticket name> <optional display name>
    private String colouredDisplayName;
    private String ticketName;
    private Player player;

    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //Check sender is player and permissions
        if (!(sender instanceof Player)) {
            returnOnlyPlayersExecuteError(sender);
            return false;
        }
        player = (Player) sender;

        if (!player.hasPermission("traincartsticketshop.ticket.create")) {
            returnInsufficientPermissionsError(player);
            return false;
        }

        //Filter quotes
        String[] filteredArguments = Utilities.concatenateQuotedStrings(args);

        //Check syntax
        if (filteredArguments.length < 3) {
            returnMissingArgumentsError(player, "/tshop ticket create <tc ticket name> <optional display name>");
            return false;
        }

        if (filteredArguments.length > 4) {
            returnError(player, "Too many arguments. Use double quotes to encapsulate spaces within ticket names and display names.");
            return false;
        }

        ticketName = filteredArguments[2];
        if (filteredArguments.length > 3) colouredDisplayName = filteredArguments[3];
        else colouredDisplayName = ticketName;

        colouredDisplayName = ChatColor.translateAlternateColorCodes('&', colouredDisplayName);
        String rawDisplayName = ChatColor.stripColor(colouredDisplayName);

        if (rawDisplayName.isBlank()) {
            returnError(player, "Ticket names cannot be less than 1 character in length");
            return false;
        }
        if (rawDisplayName.length() > 25) {
            returnError(player, "Ticket names cannot be more than 25 characters in length");
            return false;
        }
        if (colouredDisplayName.length() > 100) {
            returnError(player, "You've used too many colours in that display name");
            return false;
        }

        //Check traincarts ticket
        if (!Traincarts.checkTicket(ticketName)) {
            returnError(player, "No traincarts ticket with the name \"" + ticketName + "\" exists");
            return false;
        }

        //If all checks pass return true
        return true;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        //Create the ticket object
        Ticket ticket = new Ticket(ticketName, colouredDisplayName, "");

        //Give the ticket to the player
        ItemStack ticketItem = ticket.getItemStack();
        player.getInventory().addItem(ticketItem);

        player.sendMessage(ChatColor.GREEN + "Ticket created");
    }
}
