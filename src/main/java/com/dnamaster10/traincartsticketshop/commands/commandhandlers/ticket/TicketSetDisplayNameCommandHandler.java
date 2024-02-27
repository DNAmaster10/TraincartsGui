package com.dnamaster10.traincartsticketshop.commands.commandhandlers.ticket;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.SyncCommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.StringJoiner;

import static com.dnamaster10.traincartsticketshop.util.ButtonUtils.getButtonType;

public class TicketSetDisplayNameCommandHandler extends SyncCommandHandler {
    //Example command: /tshop ticket rename <new_name>
    private String colouredDisplayName;
    private Player player;
    private ItemStack ticket;
    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //Check that sender is a player
        if (!(sender instanceof Player)) {
            returnOnlyPlayersExecuteError(sender);
            return false;
        }
        player = (Player) sender;

        if (!player.hasPermission("traincartsticketshop.ticket.setdisplayname")) {
            returnInsufficientPermissionsError(player);
            return false;
        }

        //Check syntax
        if (args.length < 3) {
            returnError(player, "Please enter a new name for the ticket");
            return false;
        }

        //Build display name
        StringJoiner stringJoiner = new StringJoiner(" ");
        for (int i = 2; i < args.length; i++) {
            stringJoiner.add(args[i]);
        }
        colouredDisplayName = ChatColor.translateAlternateColorCodes('&', stringJoiner.toString());
        String rawDisplayName = ChatColor.stripColor(colouredDisplayName);

        if (rawDisplayName.length() > 25) {
            returnError(player, "Ticket names cannot be more than 25 characters in length");
            return false;
        }
        if (rawDisplayName.isBlank()) {
            returnError(player, "Ticket names cannot be less than 1 character in length");
            return false;
        }
        if (colouredDisplayName.length() > 100) {
            returnError(player, "Too many colours!");
            return false;
        }

        //Now check that the player is holding a ticket
        ticket = player.getInventory().getItemInMainHand();
        String buttonType = getButtonType(ticket);
        if (buttonType == null || !buttonType.equals("ticket")) {
            returnWrongItemError(player, "ticket");
            return false;
        }

        return true;
    }
    @Override
    protected void execute(CommandSender sender, String[] args) {
        //Set new display name
        ItemMeta meta = ticket.getItemMeta();
        assert meta != null;
        meta.setDisplayName(colouredDisplayName);
        ticket.setItemMeta(meta);
        player.sendMessage(ChatColor.GREEN + "Held ticket was renamed to \"" + colouredDisplayName + "\"");
    }
}
