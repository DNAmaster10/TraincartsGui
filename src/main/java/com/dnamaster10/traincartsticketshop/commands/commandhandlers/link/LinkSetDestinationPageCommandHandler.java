package com.dnamaster10.traincartsticketshop.commands.commandhandlers.link;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.SyncCommandHandler;
import com.dnamaster10.traincartsticketshop.util.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.dnamaster10.traincartsticketshop.util.ButtonUtils.getButtonType;
import static com.dnamaster10.traincartsticketshop.objects.buttons.DataKeys.DEST_GUI_PAGE;

/**
 * The command handler for the /tshop link setDestinationPage command.
 */
public class LinkSetDestinationPageCommandHandler extends SyncCommandHandler {
    //Example command: /traincartsticketshop link setDestinationPage <destination page>
    private Player player;
    private ItemStack link;
    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //Check perms and that sender is a player
        if (!(sender instanceof Player)) {
            returnOnlyPlayersExecuteError(sender);
            return false;
        }
        player = (Player) sender;

        if (!player.hasPermission("traincartsticketshop.link.setdestinationpage")) {
            returnInsufficientPermissionsError(player);
            return false;
        }

        //Check syntax
        if (args.length < 3) {
            returnMissingArgumentsError(player, "/tshop link setDestinationPage <destination page>");
            return false;
        }
        if (args.length > 3) {
            returnInvalidSubCommandError(player, args[3]);
            return false;
        }
        if (!Utilities.isInt(args[2]) || Integer.parseInt(args[2]) < 1) {
            returnError(player, "Page number must be a positive integer more than or equal to 1");
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
        //Get the link meta
        ItemMeta meta = link.getItemMeta();
        assert meta != null;

        //Add the keys and data
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(DEST_GUI_PAGE, PersistentDataType.INTEGER, Integer.parseInt(args[2]) - 1);
        link.setItemMeta(meta);

        player.sendMessage(ChatColor.GREEN + "Link page set");
    }
}
