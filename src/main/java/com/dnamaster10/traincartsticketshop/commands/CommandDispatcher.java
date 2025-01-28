package com.dnamaster10.traincartsticketshop.commands;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.gui.*;
import com.dnamaster10.traincartsticketshop.commands.commandhandlers.link.LinkCreateCommandHandler;
import com.dnamaster10.traincartsticketshop.commands.commandhandlers.link.LinkRenameCommandHandler;
import com.dnamaster10.traincartsticketshop.commands.commandhandlers.link.LinkSetDestinationPageCommandHandler;
import com.dnamaster10.traincartsticketshop.commands.commandhandlers.ticket.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Contains the top-level method used to send all commands.
 */
public class CommandDispatcher implements CommandExecutor {
    private void returnError(CommandSender sender, String error) {
        sender.sendMessage(ChatColor.RED + error);
    }
    private void returnInvalidSubCommandError(CommandSender sender, String argument) {
        returnError(sender, "Invalid sub-command \"" + argument + "\"");
    }

    /**
     * The method used to send all commands. Checks arguments to determine which CommandHandler should be used.
     *
     * @param sender The CommandSender
     * @param command The command
     * @param label The label
     * @param args The command arguments
     * @return True
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //Check that a sub-command was provided
        if (args.length < 2) {
            returnError(sender, "Please enter a valid sub-command");
            return true;
        }

        //Decide how to handle command
        switch (args[0].toLowerCase()) {
            case "gui" -> {
                switch (args[1].toLowerCase()) {
                    case "addeditor" -> new AddEditorCommandHandler().handle(sender, args);
                    case "info" -> new GuiInfoCommandHandler().handle(sender, args);
                    case "create" -> new CreateGuiCommandHandler().handle(sender, args);
                    case "delete" -> new DeleteGuiCommandHandler().handle(sender, args);
                    case "edit" -> new EditGuiCommandHandler().handle(sender, args);
                    case "open" -> new OpenGuiCommandHandler().handle(sender, args);
                    case "removeeditor" -> new RemoveEditorCommandHandler().handle(sender, args);
                    case "setid" -> new SetGuiIdCommandHandler().handle(sender, args);
                    case "rename" -> new RenameGuiCommandHandler().handle(sender, args);
                    case "searchlinks" -> new SearchLinksCommandHandler().handle(sender, args);
                    case "searchtickets" -> new SearchTicketsCommandHandler().handle(sender, args);
                    case "transfer" -> new TransferGuiCommandHandler().handle(sender, args);

                    default -> returnInvalidSubCommandError(sender, args[1]);
                }
            }
            case "ticket" -> {
                switch (args[1].toLowerCase()) {
                    case "create" -> new TicketCreateCommandHandler().handle(sender, args);
                    case "removepurchasemessage" -> new TicketRemovePurchaseMessageCommandHandler().handle(sender, args);
                    case "rename" -> new TicketRenameCommandHandler().handle(sender, args);
                    case "settraincartsticket" -> new TicketSetTraincartsTicket().handle(sender, args);
                    case "setpurchasemessage" -> new TicketSetPurchaseMessageCommandHandler().handle(sender, args);

                    default -> returnInvalidSubCommandError(sender, args[1]);
                }
            }
            case "link" -> {
                switch (args[1].toLowerCase()) {
                    case "create" -> new LinkCreateCommandHandler().handle(sender, args);
                    case "rename" -> new LinkRenameCommandHandler().handle(sender, args);
                    case "setdestinationpage" -> new LinkSetDestinationPageCommandHandler().handle(sender, args);

                    default -> returnInvalidSubCommandError(sender, args[1]);
                }
            }
            default -> returnInvalidSubCommandError(sender, args[1]);
        }
        return true;
    }
}
