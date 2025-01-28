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
 * The argument completer used for the /tshop gui transfer command.
 */
public class GuiTransferTabCompleter extends ArgumentCompleter {
    //Example command: /tshop gui transfer <gui name> <player name>
    private boolean checkPermissions(Player player) {
        return player.hasPermission("traincartsticketshop.gui.transfer") || player.hasPermission("traincartsticketshop.admin.gui.transfer");
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        if (sender instanceof Player p && !checkPermissions(p)) return new ArrayList<>();
        if (args.length > 3) return getNextArgumentCompletions(sender, args);

        GuiDataAccessor guiAccessor = new GuiDataAccessor();
        if (!(sender instanceof Player player) || player.hasPermission("traincartsticketshop.admin.gui.transfer")) {
            return guiAccessor.getPartialNameMatches(args[2]);
        }

        //Return only guis this player owns
        List<GuiDatabaseObject> ownedGuis = guiAccessor.getGuisOwnedBy(player.getUniqueId().toString());
        List<String> ownedGuiNames = ownedGuis.stream().map(GuiDatabaseObject::name).toList();
        return StringUtil.copyPartialMatches(args[2], ownedGuiNames, new ArrayList<>());
    }

    @Override
    protected List<String> getNextArgumentCompletions(CommandSender sender, String[] args) {
        return null;
    }
}
