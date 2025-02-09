package com.dnamaster10.traincartsticketshop.commands.commandhandlers.gui;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.AsyncCommandHandler;
import com.dnamaster10.traincartsticketshop.objects.guis.ConfirmGuiDeleteGui;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;

/**
 * The command handler for the /tshop gui delete command handler.
 */
public class DeleteGuiCommandHandler extends AsyncCommandHandler {
    //Command example: /traincartsticketshop gui delete <gui ID>
    private GuiDataAccessor guiAccessor;
    private GuiDatabaseObject gui;
    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //Check permissions if player
        if (sender instanceof Player p) {
            if (!p.hasPermission("traincartsticketshop.gui.delete") && !p.hasPermission("traincartsticketshop.admin.gui.delete")) {
                returnInsufficientPermissionsError(sender);
                return false;
            }
        }

        //Check syntax
        if (args.length > 3) {
            returnInvalidSubCommandError(sender, args[3]);
            return false;
        }
        if (args.length < 3) {
            returnMissingArgumentsError(sender, "/tshop gui delete <gui ID>");
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
        guiAccessor = new GuiDataAccessor();

        //Check gui exists
        if (!guiAccessor.checkGuiByName(args[2])) {
            returnGuiNotFoundError(sender, args[2]);
            return false;
        }
        //Get the gui
        gui = guiAccessor.getGuiByName(args[2]);

        //If sender is player, check they are owner
        if (sender instanceof Player p) {
            if (!p.hasPermission("traincartsticketshop.admin.gui.delete")) {
                if (!gui.ownerUuid().equalsIgnoreCase(p.getUniqueId().toString())) {
                    returnError(p, "You must own the gui in order to delete it");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) throws ModificationException {
        if (sender instanceof Player player) {
            getPlugin().getGuiManager().openNewSession(player);
            ConfirmGuiDeleteGui newGui = new ConfirmGuiDeleteGui(player, gui.id());
            newGui.open();
            return;
        }
        //If sender isn't a player, we don't need to bother with a confirm action gui.
        //Delete the gui.
        guiAccessor.deleteGui(gui.id());
        sender.sendMessage(ChatColor.GREEN + "Gui \"" + args[2] + "\" was deleted");
    }
}
