package com.dnamaster10.traincartsticketshop.commands.commandhandlers;

import com.dnamaster10.traincartsticketshop.util.Utilities;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;

/**
 * An abstract command handler extended by command handlers which require asynchronous, such as commands which access the database.
 */
public abstract class AsyncCommandHandler extends CommandHandler {
    /**
     * Runs async validation checks.
     *
     * @param sender The CommandSender
     * @param args The command arguments
     * @return Boolean indicating whether the checks passed
     * @throws QueryException Thrown when an error occurs accessing the database
     * @throws ModificationException Thrown when an error occurs modifying the database
     */
    protected abstract boolean checkAsync(CommandSender sender, String[] args) throws QueryException, ModificationException;

    /**
     * Executes the command async.
     *
     * @param sender The CommandSender
     * @param args The command arguments
     * @throws QueryException Thrown when an error occurs accessing the database
     * @throws ModificationException Thrown when an error occurs modifying the database
     */
    protected abstract void execute(CommandSender sender, String[] args) throws QueryException, ModificationException;

    @Override
    public void handle(CommandSender sender, String[] args) {
        String[] filteredArgs = Utilities.concatenateQuotedStrings(args);
        if (!checkSync(sender, filteredArgs)) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            try {
                if (!checkAsync(sender, filteredArgs)) {
                    return;
                }
                execute(sender, filteredArgs);
            } catch (QueryException | ModificationException e) {
                getPlugin().handleSqlException(sender, e);
            }
        });
    }
}
