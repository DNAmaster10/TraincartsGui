package com.dnamaster10.traincartsticketshop.commands.commandhandlers.link;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.AsyncCommandHandler;
import com.dnamaster10.traincartsticketshop.objects.buttons.Link;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The command for the /tshop link create command.
 */
public class LinkCreateCommandHandler extends AsyncCommandHandler {
    //Example command: /tshop link create <linked gui ID> <optional display name>
    private String colouredDisplayName;
    private Player player;
    private GuiDataAccessor guiAccessor;
    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //Check sender is player and permissions
        if (!(sender instanceof Player)) {
            returnOnlyPlayersExecuteError(sender);
            return false;
        }
        player = (Player) sender;

        if (!player.hasPermission("traincartsticketshop.link.create")) {
            returnInsufficientPermissionsError(player);
            return false;
        }
        //Check syntax
        if (args.length < 3) {
            returnMissingArgumentsError(player, "/tshop link create <linked gui ID> <optional display name>");
            return false;
        }
        if (!checkGuiNameSyntax(args[2])) {
            returnGuiNotFoundError(player, args[2]);
            return false;
        }

        if (args.length > 3) colouredDisplayName = args[3];
        else colouredDisplayName = args[2];
        colouredDisplayName = ChatColor.translateAlternateColorCodes('&', colouredDisplayName);
        String rawDisplayName = ChatColor.stripColor(colouredDisplayName);

        if (rawDisplayName.length() > 25) {
            returnError(player, "Link display names cannot be more than 25 characters in length");
            return false;
        }
        if (rawDisplayName.isBlank()) {
            returnError(player, "Link display names cannot be less than 1 character in length");
            return false;
        }
        if (colouredDisplayName.length() > 100) {
            returnError(player, "Too many colours used in display name");
            return false;
        }

        return true;
    }

    @Override
    protected boolean checkAsync(CommandSender sender, String[] args) {
        guiAccessor = new GuiDataAccessor();

        //Check that the gui exists
        if (!guiAccessor.checkGuiByName(args[2])) {
            returnGuiNotFoundError(sender, args[2]);
            return false;
        }

        return true;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        //Get gui ID
        int guiId = guiAccessor.getGuiIdByName(args[2]);

        //Create the link
        Link link = new Link(guiId, 0, colouredDisplayName);

        //Give the link to the player
        ItemStack item = link.getItemStack();
        player.getInventory().addItem(item);

        player.sendMessage(ChatColor.GREEN + "Link created");
    }
}
