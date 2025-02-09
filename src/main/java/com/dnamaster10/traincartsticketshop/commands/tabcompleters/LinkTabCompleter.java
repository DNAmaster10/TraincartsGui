package com.dnamaster10.traincartsticketshop.commands.tabcompleters;

import com.dnamaster10.traincartsticketshop.commands.tabcompleters.link.LinkCreateTabCompleter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * The top level argument completer for all link related commands.
 */
public class LinkTabCompleter extends ArgumentCompleter {
    private static final List<String> ARGS1;
    static {
        ARGS1 = new ArrayList<>();
        ARGS1.add("create");
        ARGS1.add("rename");
        ARGS1.add("setDestinationPage");
    }

    private boolean checkPermission(Player p, String command) {
        return p.hasPermission("traincartsticketshop.link." + command.toLowerCase());
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        //If sub-command has already been entered, return final args
        if (args.length > 2) {
            return getNextArgumentCompletions(sender, args);
        }
        //Get sub-command matches
        List<String> subCommands = StringUtil.copyPartialMatches(args[1], ARGS1, new ArrayList<>());

        //If sender isn't player, return
        if (!(sender instanceof Player)) {
            return subCommands;
        }

        //Else check permissions
        subCommands.removeIf(s -> !checkPermission((Player) sender, s));
        return subCommands;
    }

    @Override
    protected List<String> getNextArgumentCompletions(CommandSender sender, String[] args) {
        switch (args[1].toLowerCase()) {
            case "create" -> {
                return new LinkCreateTabCompleter().getCompletions(sender, args);
            }
        }
        return new ArrayList<>();
    }
}
