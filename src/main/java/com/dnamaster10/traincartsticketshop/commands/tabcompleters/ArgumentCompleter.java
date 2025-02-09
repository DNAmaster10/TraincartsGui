package com.dnamaster10.traincartsticketshop.commands.tabcompleters;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * An abstract class extended by other ArgumentCompleters to check and return the next predicted argument.
 */
public abstract class ArgumentCompleter {
    /**
     * Returns the possible tab completion for this specific argument.
     *
     * @param sender The CommandSender
     * @param args The current command arguments
     * @return The possible tab completions
     */
    public abstract List<String> getCompletions(CommandSender sender, String[] args);

    /**
     * Calls the next argument completer if this argument has already been entered
     *
     * @param sender The CommandSender
     * @param args The current command arguments
     * @return The possible tab completions for the next argument
     */
    protected abstract List<String> getNextArgumentCompletions(CommandSender sender, String[] args);
}
