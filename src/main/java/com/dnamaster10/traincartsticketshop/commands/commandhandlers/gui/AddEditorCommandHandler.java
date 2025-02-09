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
 * The command handler for the /tshop gui addEditor command.
 */
public class AddEditorCommandHandler extends AsyncCommandHandler {
    //Command example: /tshop gui addEditor <gui ID> <player>
    private PlayerDatabaseObject player;
    private GuiEditorsDataAccessor editorsAccessor;
    private GuiDatabaseObject gui;
    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //If player check perms
        if (sender instanceof Player p) {
            if (!p.hasPermission("traincartsticketshop.gui.addeditor") && !p.hasPermission("traincartsticketshop.admin.gui.addeditor")) {
                returnInsufficientPermissionsError(sender);
                return false;
            }
        }

        //Check syntax
        if (args.length < 4) {
            returnMissingArgumentsError(sender, "/tshop gui addEditor <gui ID> <player>");
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

        //Get the gui ID and check that the gui exists
        if (!guiAccessor.checkGuiByName(args[2])) {
            returnGuiNotFoundError(sender, args[2]);
            return false;
        }
        gui = guiAccessor.getGuiByName(args[2]);

        //Check player is owner
        if (sender instanceof Player p && !p.hasPermission("traincartsticketshop.admin.gui.addeditor")) {
            if (!gui.ownerUuid().equalsIgnoreCase(p.getUniqueId().toString())) {
                returnError(sender, "You do not own that gui");
                return false;
            }
        }

        //Check the editor username is a valid username
        player = Players.getPlayerByUsername(args[3]);
        if (player == null) {
            returnError(sender, "No player with the username \"" + args[3] + "\" could be found");
            return false;
        }

        //Check that the new player isn't the same player as the owner
        if (gui.ownerUuid().equalsIgnoreCase(player.uuid())) {
            returnError(sender, "Player \"" + player.username() + "\" already owns that gui");
            return false;
        }

        editorsAccessor = new GuiEditorsDataAccessor();
        //Check that the player isn't already an editor
        if (editorsAccessor.checkGuiEditorByUuid(gui.id(), player.uuid())) {
            returnError(sender, "Player \"" + player.username() + "\" is already an editor of that gui");
            return false;
        }
        return true;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) throws ModificationException {
        editorsAccessor.addGuiEditor(gui.id(), player.uuid());
        sender.sendMessage(ChatColor.GREEN + "Player \"" + player.username() + "\" was registered as an editor for gui \"" + args[2] + "\"");
    }
}