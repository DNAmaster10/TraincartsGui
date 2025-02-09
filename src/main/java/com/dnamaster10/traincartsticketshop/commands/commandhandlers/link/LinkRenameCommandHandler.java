package com.dnamaster10.traincartsticketshop.commands.commandhandlers.link;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.SyncCommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.dnamaster10.traincartsticketshop.util.ButtonUtils.getButtonType;

/**
 * The command handler for the /tshop link setDisplayName command.
 */
public class LinkRenameCommandHandler extends SyncCommandHandler {
    //Example command: /tshop link setDisplayName <display name>

    private String colouredDisplayName;
    private ItemStack link;
    private Player player;
    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //Check that sender is a player
        if (!(sender instanceof Player)) {
            returnOnlyPlayersExecuteError(sender);
            return false;
        }
        player = (Player) sender;

        if (!player.hasPermission("traincartsticketshop.link.rename")) {
            returnInsufficientPermissionsError(player);
            return false;
        }

        //Check syntax
        if (args.length < 3) {
            returnMissingArgumentsError(player, "/tshop link rename <name>");
            return false;
        }
        if (args.length > 3) {
            returnInvalidSubCommandError(player, args[3]);
            return false;
        }

        colouredDisplayName = ChatColor.translateAlternateColorCodes('&', args[2]);
        String rawDisplayName = ChatColor.stripColor(colouredDisplayName);

        if (rawDisplayName.length() > 25) {
            returnError(player, "Link names cannot be more than 25 characters in length");
            return false;
        }
        if (rawDisplayName.isBlank()) {
            returnError(player, "Link names cannot be less than 1 character in length");
            return false;
        }
        if (colouredDisplayName.length() > 100) {
            returnError(player, "Too many colours!");
            return false;
        }

        //Check that player is holding a link
        link = player.getInventory().getItemInMainHand();
        String buttonType = getButtonType(link);
        if (buttonType == null || !buttonType.equals("link")) {
            returnWrongItemError(player, "link");
            return false;
        }
        return true;
    }
    @Override
    protected void execute(CommandSender sender, String[] args) {
        //Get the item meta
        ItemMeta meta = link.getItemMeta();
        assert meta != null;

        //Set the data
        meta.setDisplayName(colouredDisplayName);
        link.setItemMeta(meta);
        player.sendMessage(ChatColor.GREEN + "Held link was renamed to \"" + colouredDisplayName + "\"");
    }
}
