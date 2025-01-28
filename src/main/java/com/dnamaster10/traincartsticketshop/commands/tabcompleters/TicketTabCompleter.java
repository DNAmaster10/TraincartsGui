package com.dnamaster10.traincartsticketshop.commands.tabcompleters;

import com.dnamaster10.traincartsticketshop.commands.tabcompleters.ticket.TicketCreateTabCompleter;
import com.dnamaster10.traincartsticketshop.commands.tabcompleters.ticket.TicketSetTraincartsTicketTabCompleter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * The top level argument completer for all ticket related commands.
 */
public class TicketTabCompleter extends ArgumentCompleter {
    private static final List<String> ARGS1;
    static {
        ARGS1 = new ArrayList<>();
        ARGS1.add("create");
        ARGS1.add("removePurchaseMessage");
        ARGS1.add("rename");
        ARGS1.add("setTraincartsTicket");
        ARGS1.add("setPurchaseMessage");
    }

    private boolean checkPermission(Player p, String command) {
        //Returns boolean indicating whether a player has permission
        //to run the specific sub-command
        return p.hasPermission("traincartsticketshop.ticket." + command.toLowerCase());
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        //Check that sub-command hasn't already been entered
        if (args.length > 2) {
            return getNextArgumentCompletions(sender, args);
        }

        List<String> subCommands = StringUtil.copyPartialMatches(args[1].toLowerCase(), ARGS1, new ArrayList<>());

        //Return sub-command matches
        if (!(sender instanceof Player)) {
            return subCommands;
        }

        //Check permissions
        subCommands.removeIf(s -> !checkPermission((Player) sender, s));
        return subCommands;
    }

    @Override
    protected List<String> getNextArgumentCompletions(CommandSender sender, String[] args) {
        switch (args[1].toLowerCase()) {
            case "create" -> {
                return new TicketCreateTabCompleter().getCompletions(sender, args);
            }
            case "settraincartsticket" -> {
                return new TicketSetTraincartsTicketTabCompleter().getCompletions(sender, args);
            }
            case "rename", "removepurchasemessage", "setpurchasemessage" -> {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }
}
