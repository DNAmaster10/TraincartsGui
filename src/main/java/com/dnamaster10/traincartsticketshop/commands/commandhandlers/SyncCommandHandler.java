package com.dnamaster10.traincartsticketshop.commands.commandhandlers;

import com.dnamaster10.traincartsticketshop.util.Utilities;
import org.bukkit.command.CommandSender;

/**
 * An abstract command handler extended by command handlers which do not require asynchronous, such as commands which rename items.
 */
public abstract class SyncCommandHandler extends CommandHandler {
    /**
     * Executes the command synchronously
     *
     * @param sender The CommandSender
     * @param args The command arguments
     */
    protected abstract void execute(CommandSender sender, String[] args);

    @Override
    public void handle(CommandSender sender, String[] args) {
        String[] filteredArgs = Utilities.concatenateQuotedStrings(args);
        if (!checkSync(sender, filteredArgs)) {
            return;
        }
        execute(sender, filteredArgs);
    }
}
