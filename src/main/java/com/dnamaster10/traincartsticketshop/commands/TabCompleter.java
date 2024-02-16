package com.dnamaster10.traincartsticketshop.commands;

import com.dnamaster10.traincartsticketshop.commands.tabcompleters.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    private static final List<String> ARGS0;
    static {
        ARGS0 = new ArrayList<>();
        ARGS0.add("gui");
        ARGS0.add("ticket");
        ARGS0.add("linker");
    }
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
                return new GuiTabCompleter().onTabComplete(commandSender, args);
            }
            case "ticket" -> {
                return new TicketTabCompleter().onTabComplete(commandSender, args);
            }
            case "linker" -> {
                return new LinkerTabCompleter().onTabComplete(commandSender, args);
            }
        }
        return null;
    }
}
