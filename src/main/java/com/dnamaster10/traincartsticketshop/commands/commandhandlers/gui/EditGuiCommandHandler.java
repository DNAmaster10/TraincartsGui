package com.dnamaster10.traincartsticketshop.commands.commandhandlers.gui;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.AsyncCommandHandler;
import com.dnamaster10.traincartsticketshop.objects.guis.EditGui;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;

/**
 * The command handler for the /tshop gui edit command.
 */
public class EditGuiCommandHandler extends AsyncCommandHandler {
    //Example command: /tshop gui edit <gui ID>
    private Player player;
    private GuiDatabaseObject gui;
    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //Check sender is player and permissions
        if (!(sender instanceof Player)) {
            returnOnlyPlayersExecuteError(sender);
            return false;
        }
        player = (Player) sender;

        if (!player.hasPermission("traincartsticketshop.gui.edit") && !player.hasPermission("traincartsticketshop.admin.gui.edit")) {
            returnInsufficientPermissionsError(player);
            return false;
        }

        //Check syntax
        if (args.length < 3)  {
            returnMissingArgumentsError(player, "/tshop gui edit <gui ID>");
            return false;
        }
        if (args.length > 3) {
            returnInvalidSubCommandError(player, args[3]);
            return false;
        }
        if (!checkGuiNameSyntax(args[2])) {
            returnGuiNotFoundError(player, args[2]);
            return false;
        }

        return true;
    }

    @Override
    protected boolean checkAsync(CommandSender sender, String[] args) {
        //Example command: /traincartsticketshop gui edit <gui_name>
        GuiDataAccessor guiAccessor = new GuiDataAccessor();

        //Get the guiID and check that it exists
        if (!guiAccessor.checkGuiByName(args[2])) {
            returnGuiNotFoundError(player, args[2]);
            return false;
        }
        gui = guiAccessor.getGuiByName(args[2]);

        //Check that player is owner or editor of gui
        if (!player.hasPermission("traincartsticketshop.admin.gui.edit")) {
            if (!guiAccessor.playerCanEdit(gui.id(), player.getUniqueId().toString())) {
                returnError(player, "You do not have permission to edit that gui. Request that the owner adds you as an editor before making any changes");
                return false;
            }
        }
        return true;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        //Open a new gui session
        getPlugin().getGuiManager().openNewSession(player);

        //Create the new gui
        EditGui editGui = new EditGui(player, gui.id());

        //Open the new gui
        editGui.open();
    }
}
