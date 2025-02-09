package com.dnamaster10.traincartsticketshop.commands.tabcompleters.gui;

import com.dnamaster10.traincartsticketshop.commands.tabcompleters.ArgumentCompleter;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiEditorsDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.GuiDatabaseObject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * The argument completer used for the /tshop gui removeEditor command.
 */
public class GuiRemoveEditorTabCompleter extends ArgumentCompleter {
    //Example command: /tshop gui removeEditor <gui name> <username>
    private boolean checkPermissions(Player player) {
        return player.hasPermission("traincartsticketshop.gui.removeeditor") || player.hasPermission("traincartsticketshop.admin.gui.removeeditor");
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        if (sender instanceof Player p && !checkPermissions(p)) return new ArrayList<>();
        if (args.length > 3) return getNextArgumentCompletions(sender, args);

        GuiDataAccessor guiAccessor = new GuiDataAccessor();
        if (!(sender instanceof Player player) || player.hasPermission("traincartsticketshop.admin.gui.removeeditor")) {
            return guiAccessor.getPartialNameMatches(args[2]);
        }

        //Get guis owned by this sender
        List<GuiDatabaseObject> ownedGuis = guiAccessor.getGuisOwnedBy(player.getUniqueId().toString());
        List<String> ownedGuiNames = ownedGuis.stream().map(GuiDatabaseObject::name).toList();
        return StringUtil.copyPartialMatches(args[2], ownedGuiNames, new ArrayList<>());
    }

    @Override
    protected List<String> getNextArgumentCompletions(CommandSender sender, String[] args) {
        GuiDataAccessor guiAccessor = new GuiDataAccessor();
        if (!guiAccessor.checkGuiByName(args[2])) return new ArrayList<>();
        int guiId = guiAccessor.getGuiIdByName(args[2]);
        if (sender instanceof Player player && !player.hasPermission("traincartsticketshop.admin.gui.removeeditor")) {
            if (!guiAccessor.checkGuiOwnerByUuid(guiId, player.getUniqueId().toString())) return new ArrayList<>();
        }

        GuiEditorsDataAccessor guiEditorsAccessor = new GuiEditorsDataAccessor();
        List<String> editors = guiEditorsAccessor.getEditorUsernames(guiId);
        return StringUtil.copyPartialMatches(args[3], editors, new ArrayList<>());
    }
}
