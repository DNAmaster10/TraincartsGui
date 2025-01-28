package com.dnamaster10.traincartsticketshop.commands.commandhandlers.gui;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.AsyncCommandHandler;
import com.dnamaster10.traincartsticketshop.util.Utilities;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The command handler for the /tshop gui create command.
 */
public class CreateGuiCommandHandler extends AsyncCommandHandler {
    //Example command: /traincartsticketshop gui create <gui name> <optional name>

    //Used to store the display name since spaces can be entered here
    private String rawDisplayName;
    private String colouredDisplayName;
    private GuiDataAccessor guiAccessor;
    private Player player;

    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //Check sender is player and permissions
        if (!(sender instanceof Player)) {
            returnOnlyPlayersExecuteError(sender);
            return false;
        }
        player = (Player) sender;

        if (!player.hasPermission("traincartsticketshop.gui.create")) {
            returnInsufficientPermissionsError(player);
            return false;
        }

        //Check syntax
        if (args.length < 2) {
            returnMissingArgumentsError(player, "/tshop gui create <gui ID> <optional name>");
            return false;
        }
        if (args.length > 4) {
            returnInvalidSubCommandError(player, args[4]);
            return false;
        }

        if (args[2].length() > 20) {
            returnError(player, "Gui IDs cannot be more than 20 characters in length");
            return false;
        }

        if (args[2].isBlank()) {
            returnError(player, "Gui IDs must be at least 1 character in length");
            return false;
        }

        if (Utilities.checkSpecialCharacters(args[2])) {
            returnError(player, "Gui IDs can only contain characters Aa to Zz, numbers, underscores, and dashes");
            return false;
        }

        if (args.length > 3) colouredDisplayName = args[3];
        else colouredDisplayName = args[2];
        colouredDisplayName = ChatColor.translateAlternateColorCodes('&', colouredDisplayName);
        rawDisplayName = ChatColor.stripColor(colouredDisplayName);

        //Check display name
        if (rawDisplayName.length() > 25) {
            returnError(player, "Gui names cannot be longer than 25 characters in length");
            return false;
        }
        if (rawDisplayName.isBlank()) {
            returnError(player, "Gui names cannot be less than 1 character in length");
            return false;
        }
        if (colouredDisplayName.length() > 100) {
            returnError(player, "Too many colours used in display name");
            return false;
        }

        //If all checks have passed return true
        return true;
    }

    @Override
    protected boolean checkAsync(CommandSender sender, String[] args) {
        guiAccessor = new GuiDataAccessor();

        //Check gui doesn't already exist
        if (guiAccessor.checkGuiByName(args[2])) {
            returnError(player, "A gui with the ID \"" + args[2] + "\" already exists");
            return false;
        }
        return true;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) throws ModificationException {
        //Runs the command
        guiAccessor.addGui(args[2], colouredDisplayName, rawDisplayName, player.getUniqueId().toString());
        player.sendMessage(ChatColor.GREEN + "A gui with the ID \"" + args[2] + "\" was created");
    }
}
