package com.dnamaster10.traincartsticketshop.commands.commandhandlers.gui;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.AsyncCommandHandler;
import com.dnamaster10.traincartsticketshop.util.Players;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiEditorsDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.PlayerDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The command handler for the /tshop gui removeEditor command
 */
public class RemoveEditorCommandHandler extends AsyncCommandHandler {
    //Example command: /tshop editor removeEditor <gui ID> <player name>
    private PlayerDatabaseObject editorDatabaseObject;
    private GuiEditorsDataAccessor editorsAccessor;
    private GuiDatabaseObject gui;
    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //Check sender perms
        if (sender instanceof Player p) {
            if (!p.hasPermission("traincartsticketshop.gui.removeEditor") && !p.hasPermission("traincartsticketshop.admin.gui.removeEditor")) {
                returnInsufficientPermissionsError(sender);
                return false;
            }
        }

        //Check syntax
        if (args.length < 4) {
            returnMissingArgumentsError(sender, "/tshop gui removeEditor <gui ID> <username>");
            return false;
        }
        if (args.length > 4) {
            returnInvalidSubCommandError(sender, args[4]);
            return false;
        }
        if (!checkGuiNameSyntax(args[2])) {
            returnGuiNotFoundError(sender, args[2]);
            return false;
        }
        return true;
    }

    @Override
    protected boolean checkAsync(CommandSender sender, String[] args) throws ModificationException {
        GuiDataAccessor guiAccessor = new GuiDataAccessor();

        //Get the guiID and check that the gui exists
        if (!guiAccessor.checkGuiByName(args[2])) {
            returnGuiNotFoundError(sender, args[2]);
            return false;
        }
        gui = guiAccessor.getGuiByName(args[2]);

        //If player, check that they own the gui
        if (sender instanceof Player p) {
            if (!p.hasPermission("traincartsticketshop.admin.gui.removeEditor")) {
                if (!gui.ownerUuid().equalsIgnoreCase(p.getUniqueId().toString())) {
                    returnError(sender, "You do not own that gui");
                    return false;
                }
            }
        }

       //Check that editor is a valid username and that they are a registered editor of the gui
        editorDatabaseObject = Players.getPlayerByUsername(args[3]);
        if (editorDatabaseObject == null) {
            returnError(sender, "No player with the username \"" + args[3] + "\" could be found");
            return false;
        }

        editorsAccessor = new GuiEditorsDataAccessor();
        //Check that the editor exists in the editors table
        if (!editorsAccessor.checkGuiEditorByUuid(gui.id(), editorDatabaseObject.uuid())) {
            returnError(sender, "Player \"" + editorDatabaseObject.username() + "\" is not a registered editor for gui \"" + args[2] + "\"");
            return false;
        }
        return true;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) throws ModificationException {
        //Remove the editor
        editorsAccessor.removeGuiEditor(gui.id(), editorDatabaseObject.uuid());
        sender.sendMessage(ChatColor.GREEN + "Player \"" + editorDatabaseObject.username() + "\" is no longer an editor of gui \"" + args[2] + "\"");
    }
}