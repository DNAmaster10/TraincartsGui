package com.dnamaster10.traincartsticketshop.commands.tabcompleters.gui;

import com.dnamaster10.traincartsticketshop.commands.tabcompleters.ArgumentCompleter;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiDatabaseObject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * The argument completer for the /tshop gui delete command.
 */
public class GuiDeleteTabCompleter extends ArgumentCompleter {
    //Example command: /tshop gui delete <gui name>
    private boolean checkPermissions(Player player) {
        return player.hasPermission("traincartsticketshop.gui.delete") || player.hasPermission("traincartsticketshop.admin.gui.delete");
    }
    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        if (sender instanceof Player p && !checkPermissions(p)) return new ArrayList<>();
        if (args.length > 3) return getNextArgumentCompletions(sender, args);

        GuiDataAccessor guiAccessor = new GuiDataAccessor();
        if (!(sender instanceof Player p) || p.hasPermission("traincartsticketshop.admin.gui.delete")) {
            //Return all gui names
            return guiAccessor.getPartialNameMatches(args[2]);
        }

        //Return guis owned by this player
        List<GuiDatabaseObject> guis = guiAccessor.getGuisOwnedBy(p.getUniqueId().toString());
        List<String> guiNames = guis.stream().map(GuiDatabaseObject::name).toList();
        return StringUtil.copyPartialMatches(args[2], guiNames, new ArrayList<>());
    }

    @Override
    protected List<String> getNextArgumentCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
