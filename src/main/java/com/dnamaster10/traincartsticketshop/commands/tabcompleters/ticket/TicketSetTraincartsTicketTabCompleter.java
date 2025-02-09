package com.dnamaster10.traincartsticketshop.commands.tabcompleters.ticket;

import com.dnamaster10.traincartsticketshop.commands.tabcompleters.ArgumentCompleter;
import com.dnamaster10.traincartsticketshop.util.Traincarts;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The argument completer for the /tshop ticket setTraincartsTicket command.
 */
public class TicketSetTraincartsTicketTabCompleter extends ArgumentCompleter {
    //Example command: /tshop ticket setTraincartsTicket <tc ticket name>
    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        if (sender instanceof Player p && !p.hasPermission("traincartsticketshop.ticket.settraincartsticket")) return new ArrayList<>();
        if (args.length > 3) return getNextArgumentCompletions(sender, args);

        return Traincarts.getPartialTicketNameCompletions(args[2]);
    }

    @Override
    protected List<String> getNextArgumentCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
