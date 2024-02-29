package com.dnamaster10.traincartsticketshop.commands.commandhandlers.gui;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.AsyncCommandHandler;
import com.dnamaster10.traincartsticketshop.objects.guis.EditGui;
import com.dnamaster10.traincartsticketshop.util.Session;
import com.dnamaster10.traincartsticketshop.util.database.mariadb.MariaDBGuiAccessor;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;

public class EditGuiCommandHandler extends AsyncCommandHandler {
    private Player player;
    private Integer guiId;
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
            returnMissingArgumentsError(player, "/tshop gui edit <gui name>");
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
    protected boolean checkAsync(CommandSender sender, String[] args) throws QueryException {
        //Example command: /traincartsticketshop gui edit <gui_name>
        MariaDBGuiAccessor guiAccessor = new MariaDBGuiAccessor();

        //Get the guiID and check that it exists
        guiId = guiAccessor.getGuiIdByName(args[2]);
        if (guiId == null) {
            returnGuiNotFoundError(player, args[2]);
            return false;
        }

        //Check that player is owner or editor of gui
        if (!player.hasPermission("traincartsticketshop.admin.gui.edit")) {
            if (!guiAccessor.playerCanEdit(guiId, player.getUniqueId().toString())) {
                returnError(player, "You do not have permission to edit that gui. Request that the owner adds you as an editor before making any changes");
                return false;
            }
        }
        return true;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) throws QueryException {
        //Create the new gui
        EditGui gui = new EditGui(guiId, player);

        //Open a new gui session
        Session session = getPlugin().getGuiManager().getNewSession(player);

        //Register the gui
        session.addGui(gui);

        //Open the new gui
        gui.open();
    }
}
