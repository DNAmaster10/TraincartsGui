package com.dnamaster10.traincartsticketshop.commands;

import com.dnamaster10.traincartsticketshop.commands.tabcompleters.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The main tab completer used for all tab completions. Determines which other tab completers should be called by evaluating current arguments.
 */
public class MainTabCompleter implements org.bukkit.command.TabCompleter {
    private static final List<String> ARGS0;
    static {
        ARGS0 = new ArrayList<>();
        ARGS0.add("gui");
        ARGS0.add("ticket");
        ARGS0.add("link");
    }

    /**
     * The main method which is called.
     *
     * @return The text to display for the tab completion
     */
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //Handle first sub-command
        if (args.length < 2) {
            return StringUtil.copyPartialMatches(args[0], ARGS0, new ArrayList<>());
        }

        //Select an appropriate tab completer for the given sub-command
        switch (args[0].toLowerCase()) {
            case "gui" -> {
                return new GuiTabCompleter().getCompletions(commandSender, args);
            }
            case "ticket" -> {
                return new TicketTabCompleter().getCompletions(commandSender, args);
            }
            case "link" -> {
                return new LinkTabCompleter().getCompletions(commandSender, args);
            }
        }
        return null;
    }
}
