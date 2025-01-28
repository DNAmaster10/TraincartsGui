package com.dnamaster10.traincartsticketshop.commands.commandhandlers.gui;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.AsyncCommandHandler;
import com.dnamaster10.traincartsticketshop.objects.guis.ShopGui;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;

/**
 * The command handler for the /shop gui open command.
 */
public class OpenGuiCommandHandler extends AsyncCommandHandler {
    private Player player;
    private GuiDataAccessor guiAccessor;

    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //Check sender is player
        if (!(sender instanceof Player)) {
            returnOnlyPlayersExecuteError(sender);
            return false;
        }
        player = (Player) sender;

        //Check permissions
        if (!player.hasPermission("traincartsticketshop.gui.open")) {
            returnInsufficientPermissionsError(sender);
            return false;
        }

        //Check syntax
        if (args.length < 3) {
            returnMissingArgumentsError(sender, "/tshop gui open <gui ID>");
            return false;
        }
        if (args.length > 3) {
            returnInvalidSubCommandError(sender, args[3]);
            return false;
        }
        if (!checkGuiNameSyntax(args[2])) {
            returnGuiNotFoundError(sender, args[2]);
            return false;
        }

        return true;
    }

    @Override
    protected boolean checkAsync(CommandSender sender, String[] args) {
        //Example command: /traincartsticketshop gui open <gui_name>
        guiAccessor = new GuiDataAccessor();

        //Check the gui exists
        if (!guiAccessor.checkGuiByName(args[2])) {
            returnGuiNotFoundError(player, args[2]);
            return false;
        }
        return true;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        //Create a new gui session
        getPlugin().getGuiManager().openNewSession(player);

        //Create a new gui
        int guiId = guiAccessor.getGuiIdByName(args[2]);
        ShopGui gui = new ShopGui(player, guiId);

        //Open the new gui
        gui.open();
    }
}
