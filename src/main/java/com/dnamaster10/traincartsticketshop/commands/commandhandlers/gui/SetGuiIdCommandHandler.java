package com.dnamaster10.traincartsticketshop.commands.commandhandlers.gui;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.AsyncCommandHandler;
import com.dnamaster10.traincartsticketshop.util.Utilities;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The command handler for the /tshop gui setId command.
 */
public class SetGuiIdCommandHandler extends AsyncCommandHandler {
    //Example command: /traincartsticketshop gui setId <old ID> <new ID>
    private GuiDataAccessor guiAccessor;
    private GuiDatabaseObject gui;

    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //Check syntax
        if (args.length < 4) {
            returnMissingArgumentsError(sender, "/tshop gui setId <old ID> <new ID>");
            return false;
        }
        if (args.length > 4) {
            returnInvalidSubCommandError(sender, args[4]);
            return false;
        }
        if (args[3].length() > 20) {
            returnError(sender, "Gui IDs cannot be more than 20 characters in length");
            return false;
        }
        if (args[3].isBlank()) {
            returnError(sender, "Gui IDs cannot be less than 3 characters in length");
            return false;
        }
        //Check syntax of old gui name to save on database calls
        if (!checkGuiNameSyntax(args[2])) {
            returnGuiNotFoundError(sender, args[2]);
            return false;
        }
        if (Utilities.checkSpecialCharacters(args[3])) {
            returnError(sender, "Gui IDs can only contain letters Aa - Zz, numbers, underscores and dashes");
            return false;
        }

        //Check permissions
        if (sender instanceof Player p) {
            if (!p.hasPermission("traincartsticketshop.gui.setid") && !p.hasPermission("traincartsticketshop.admin.gui.setid")) {
                returnInsufficientPermissionsError(sender);
                return false;
            }
        }

        //If all checks have passed, return true
        return true;
    }

    @Override
    protected boolean checkAsync(CommandSender sender, String[] args) {
        guiAccessor = new GuiDataAccessor();

        //Get the gui ID and check that it exists
        if (!guiAccessor.checkGuiByName(args[2])) {
            returnGuiNotFoundError(sender, args[2]);
            return false;
        }
        gui = guiAccessor.getGuiByName(args[2]);

        //If sender is player, check that player is an editor of that gui if they don't have admin perms
        if (sender instanceof Player p) {
            if (!p.hasPermission("traincartsticketshop.admin.gui.setid")) {
                if (!gui.ownerUuid().equalsIgnoreCase(p.getUniqueId().toString())) {
                    returnError(sender, "You must be the owner of a Gui to change its ID.");
                    return false;
                }
            }
        }

        //Check that the new gui name doesn't already exist
        if (guiAccessor.checkGuiByName(args[3])) {
            returnError(sender, "A gui with the ID \"" + args[3] + "\" already exists");
            return false;
        }
        return true;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) throws ModificationException {
        guiAccessor.updateGuiName(gui.id(), args[3]);
        sender.sendMessage(ChatColor.GREEN + "Gui \"" + args[2] + "\"'s ID was successfully changed to \"" + args[3] + "\"");
    }
}
