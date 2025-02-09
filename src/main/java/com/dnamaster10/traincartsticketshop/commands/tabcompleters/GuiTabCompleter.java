package com.dnamaster10.traincartsticketshop.commands.tabcompleters;

import com.dnamaster10.traincartsticketshop.commands.tabcompleters.gui.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * The top level argument completer for all Gui related commands.
 */
public class GuiTabCompleter extends ArgumentCompleter {
    //Example command: /tshop gui <arg1>
    private static final List<String> ARGS1;
    static {
        ARGS1 = new ArrayList<>();
        ARGS1.add("addEditor");
        ARGS1.add("info");
        ARGS1.add("create");
        ARGS1.add("delete");
        ARGS1.add("edit");
        ARGS1.add("open");
        ARGS1.add("removeEditor");
        ARGS1.add("rename");
        ARGS1.add("searchLinks");
        ARGS1.add("searchTickets");
        ARGS1.add("setId");
        ARGS1.add("transfer");
    }

    private boolean checkPermission(Player p, String command) {
        return p.hasPermission("traincartsticketshop.gui." + command.toLowerCase()) || p.hasPermission("traincartsticketshop.admin.gui." + command.toLowerCase());
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        //Check that sub-command hasn't already been entered
        if (args.length > 2) {
            return getNextArgumentCompletions(sender, args);
        }
        //Return sub-command matches
        List<String> subCommands = StringUtil.copyPartialMatches(args[1], ARGS1, new ArrayList<>());

        //If sender isn't player, return
        if (!(sender instanceof Player)) {
            return subCommands;
        }

        //Else, check permissions for all sub-commands. Remove sub-command if player has no permission to use it
        subCommands.removeIf(s -> !checkPermission((Player) sender, s));
        return subCommands;
    }

    @Override
    protected List<String> getNextArgumentCompletions(CommandSender sender, String[] args) {
        switch (args[1].toLowerCase()) {
            case "addeditor" -> {
                return new GuiAddEditorTabCompleter().getCompletions(sender, args);
            }
            case "delete" -> {
                return new GuiDeleteTabCompleter().getCompletions(sender, args);
            }
            case "edit" -> {
                return new GuiEditTabCompleter().getCompletions(sender, args);
            }
            case "info" -> {
                return new GuiInfoTabCompleter().getCompletions(sender, args);
            }
            case "open" -> {
                return new GuiOpenTabCompleter().getCompletions(sender, args);
            }
            case "removeeditor" -> {
                return new GuiRemoveEditorTabCompleter().getCompletions(sender, args);
            }
            case "setid" -> {
                return new GuiSetIdCompleter().getCompletions(sender, args);
            }
            case "rename" -> {
                return new GuiRenameTabCompleter().getCompletions(sender, args);
            }
            case "transfer" -> {
                return new GuiTransferTabCompleter().getCompletions(sender, args);
            }
        }
        return new ArrayList<>();
    }
}
