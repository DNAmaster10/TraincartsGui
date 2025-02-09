package com.dnamaster10.traincartsticketshop.commands.tabcompleters.gui;

import com.dnamaster10.traincartsticketshop.commands.tabcompleters.ArgumentCompleter;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The argument completer used for the /tshop gui info command
 */
public class GuiInfoTabCompleter extends ArgumentCompleter {
    //Example command: /tshop gui info <gui name>
    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        if (args.length > 3) return getNextArgumentCompletions(sender, args);
        if (sender instanceof Player p && !p.hasPermission("traincartsticketshop.gui.info")) return new ArrayList<>();

        GuiDataAccessor guiAccessor = new GuiDataAccessor();
        return guiAccessor.getPartialNameMatches(args[2]);
    }

    @Override
    protected List<String> getNextArgumentCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
