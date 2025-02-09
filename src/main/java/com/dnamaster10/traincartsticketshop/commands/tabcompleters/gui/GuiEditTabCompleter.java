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
 * The argument completer used for the /tshop gui edit command.
 */
public class GuiEditTabCompleter extends ArgumentCompleter {
    //Example command: /tshop gui edit <gui name>
    private boolean checkPermissions(Player player) {
        return player.hasPermission("traincartsticketshop.gui.edit") || player.hasPermission("traincartsticketshop.admin.gui.edit");
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        if (sender instanceof Player p && !checkPermissions(p)) return new ArrayList<>();
        if (args.length > 3) return getNextArgumentCompletions(sender, args);

        GuiDataAccessor guiAccessor = new GuiDataAccessor();
        if (!(sender instanceof Player player) || player.hasPermission("traincartsticketshop.admin.gui.edit")) {
            //Get all guis
            return guiAccessor.getPartialNameMatches(args[2]);
        }

        //Get guis editable by this player
        List<GuiDatabaseObject> editableGuis = guiAccessor.getGuisEditableBy(player.getUniqueId().toString());
        List<String> editableGuiNames = editableGuis.stream().map(GuiDatabaseObject::name).toList();
        return StringUtil.copyPartialMatches(args[2], editableGuiNames, new ArrayList<>());
    }

    @Override
    protected List<String> getNextArgumentCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
