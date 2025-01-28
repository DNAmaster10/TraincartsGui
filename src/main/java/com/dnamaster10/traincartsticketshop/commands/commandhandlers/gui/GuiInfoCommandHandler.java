package com.dnamaster10.traincartsticketshop.commands.commandhandlers.gui;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.AsyncCommandHandler;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiEditorsDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.accessors.LinkDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.accessors.TicketDataAccessor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.*;

import java.util.List;

/**
 * The command handler for the /tshop gui info command.
 */
public class GuiInfoCommandHandler extends AsyncCommandHandler {
    //Example command: /tshop gui info <gui ID>

    GuiDataAccessor guiAccessor;
    GuiDatabaseObject gui;
    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //Check permissions
        if (sender instanceof Player p) {
            if (!p.hasPermission("traincartsticketshop.gui.info")) {
                returnInsufficientPermissionsError(p);
                return false;
            }
        }

        //Check syntax
        if (args.length < 3) {
            returnMissingArgumentsError(sender, "/tshop gui info <gui ID>");
            return false;
        }
        if (args.length > 3) {
            returnInvalidSubCommandError(sender, args[3]);
            return false;
        }
        if (!checkGuiNameSyntax(args[2])) {
            returnGuiNotFoundError(sender, args[2]);
            return false;
        }

        return true;
    }

    @Override
    protected boolean checkAsync(CommandSender sender, String[] args) {
        guiAccessor = new GuiDataAccessor();

        //Check gui exists
        if (!guiAccessor.checkGuiByName(args[2])) {
            returnGuiNotFoundError(sender, args[2]);
            return false;
        }
        gui = guiAccessor.getGuiByName(args[2]);

        return true;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) throws QueryException {
        //Fetch info
        int totalPages = guiAccessor.getHighestPageNumber(gui.id()) + 1;

        TicketDataAccessor ticketAccessor = new TicketDataAccessor();
        LinkDataAccessor linkAccessor = new LinkDataAccessor();
        GuiEditorsDataAccessor guiEditorsDataAccessor = new GuiEditorsDataAccessor();

        List<String> guiEditors = guiEditorsDataAccessor.getEditorUsernames(gui.id());
        int totalTickets = ticketAccessor.getTotalTickets(gui.id());
        int totalLinks = linkAccessor.getTotalLinks(gui.id());

        //Send info to player
        TextComponent line;
        line = new TextComponent(ChatColor.AQUA + "Info for gui \"" + gui.name() + "\":");
        sender.spigot().sendMessage(line);

        line = new TextComponent(ChatColor.WHITE + "| Owner: " + guiAccessor.getOwnerUsername(gui.id()));
        sender.spigot().sendMessage(line);
        line = new TextComponent(ChatColor.WHITE + "| Pages: " + totalPages);
        sender.spigot().sendMessage(line);
        line = new TextComponent(ChatColor.WHITE + "| Tickets: " + totalTickets);
        sender.spigot().sendMessage(line);
        line = new TextComponent(ChatColor.WHITE + "| Links: " + totalLinks);
        sender.spigot().sendMessage(line);
        line = new TextComponent(ChatColor.WHITE + "| Editors: " + String.join(",", guiEditors));
        sender.spigot().sendMessage(line);
    }
}
