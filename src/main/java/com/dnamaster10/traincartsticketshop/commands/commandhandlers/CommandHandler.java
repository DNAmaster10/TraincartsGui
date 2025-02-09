package com.dnamaster10.traincartsticketshop.commands.commandhandlers;

import com.dnamaster10.traincartsticketshop.util.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;

/**
 * An abstract command handler which all other command handlers extend from.
 * Contains some commonly used methods between all other command handlers.
 */
public abstract class CommandHandler {

    /**
     * Used by all command handlers to run synchronous validation checks, e.g. syntax validation.
     *
     * @param sender The CommandSender
     * @param args The command arguments
     * @return True if all validation checks passed
     */
    protected abstract boolean checkSync(CommandSender sender, String[] args);

    /**
     * The main handle method. Runs validation checks and executes the command in the correct order.
     *
     * @param sender The CommandSender
     * @param args The command arguments
     */
    public abstract void handle(CommandSender sender, String[] args);

    /**
     * Returns an error to the sender, applying the correct chat formatting automatically.
     *
     * @param sender The CommandSender
     * @param error The error message to send
     */
    protected void returnError(CommandSender sender, String error) {
        //Returns an error to sender
        if (sender instanceof Player p) {
            p.sendMessage(ChatColor.RED + error);
        }
        else if (sender instanceof ConsoleCommandSender) {
            getPlugin().getLogger().warning(error);
        }
    }

    /**
     * Returns an error message indicating that the Gui does not exist
     *
     * @param sender The CommandSender
     * @param guiName The name of the Gui which does not exist
     */
    protected void returnGuiNotFoundError(CommandSender sender, String guiName) {
        returnError(sender, "No gui with the ID \"" + guiName + "\" exists");
    }

    /**
     * Returns an error message indicating that the player is not holding the correct item.
     *
     * @param sender The CommandSender
     * @param correctItem The name of the item which the player should be holding instead
     */
    protected void returnWrongItemError(CommandSender sender, String correctItem) {
        sender.sendMessage(ChatColor.RED + "You must be holding a " + correctItem + " to perform that action");
    }

    /**
     * Returns an error message indicating that the command must be executed by a player.
     *
     * @param sender The CommandSender
     */
    protected void returnOnlyPlayersExecuteError(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Command must be executed by a player");
    }

    /**
     * Returns an error message indicating that the sender does not have sufficient permissions to run the command.
     *
     * @param sender The CommandSender
     */
    protected void returnInsufficientPermissionsError(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to perform that action");
    }

    /**
     * Returns an error message indicating that the entered command was missing arguments, as well as the correct syntax to use.
     *
     * @param sender The CommandSender
     * @param commandSyntax The correct command syntax to use
     */
    protected void returnMissingArgumentsError(CommandSender sender, String commandSyntax) {
        sender.sendMessage(ChatColor.RED + "Missing argument(s): " + commandSyntax);
    }

    /**
     * Returns an error message indicating that an entered argument was invalid.
     *
     * @param sender The CommandSender
     * @param subCommand The argument which was invalid
     */
    protected void returnInvalidSubCommandError(CommandSender sender, String subCommand) {
        sender.sendMessage(ChatColor.RED + "Invalid sub-command \"" + subCommand + "\"");
    }

    /**
     * Checks whether the input is a valid Gui name, e.g. does not contain special characters.
     *
     * @param input The input String
     * @return True if the syntax is correct
     * @see com.dnamaster10.traincartsticketshop.util.Utilities#checkSpecialCharacters(String);
     */
    protected boolean checkGuiNameSyntax(String input) {
        return !input.isEmpty() && input.length() < 20 && !Utilities.checkSpecialCharacters(input);
    }
}
