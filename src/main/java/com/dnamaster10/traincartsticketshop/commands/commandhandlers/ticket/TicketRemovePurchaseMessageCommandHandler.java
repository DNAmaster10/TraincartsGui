package com.dnamaster10.traincartsticketshop.commands.commandhandlers.ticket;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.SyncCommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import static com.dnamaster10.traincartsticketshop.objects.buttons.DataKeys.PURCHASE_MESSAGE;
import static com.dnamaster10.traincartsticketshop.util.ButtonUtils.getButtonType;

/**
 * The command handler for the /tshop ticket removePurchaseMessage command.
 */
public class TicketRemovePurchaseMessageCommandHandler extends SyncCommandHandler {
    private Player player;
    private ItemStack ticket;

    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            returnOnlyPlayersExecuteError(sender);
            return false;
        }
        player = (Player) sender;

        if (!player.hasPermission("traincartsticketshop.ticket.removepurchasemessage")) {
            returnInsufficientPermissionsError(player);
            return false;
        }

        //Check syntax
        if (args.length > 2) {
            returnInvalidSubCommandError(player, args[2]);
            return false;
        }

        //Check player is holding a ticket
        ticket = player.getInventory().getItemInMainHand();
        String buttonType = getButtonType(ticket);
        if (buttonType == null || !buttonType.equals("ticket")) {
            returnWrongItemError(player, "ticket");
            return false;
        }

        return true;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        ItemMeta meta = ticket.getItemMeta();
        assert meta != null;
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        if (!dataContainer.has(PURCHASE_MESSAGE)) {
            returnError(player, "The held ticket does not have a purchase message set");
            return;
        }
        dataContainer.remove(PURCHASE_MESSAGE);
        ticket.setItemMeta(meta);
        player.sendMessage(ChatColor.GREEN + "Held ticket's purchase message was removed");
    }
}
