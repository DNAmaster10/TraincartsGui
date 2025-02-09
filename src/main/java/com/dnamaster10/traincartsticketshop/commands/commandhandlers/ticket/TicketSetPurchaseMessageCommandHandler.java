package com.dnamaster10.traincartsticketshop.commands.commandhandlers.ticket;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.SyncCommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.dnamaster10.traincartsticketshop.util.ButtonUtils.getButtonType;
import static com.dnamaster10.traincartsticketshop.objects.buttons.DataKeys.PURCHASE_MESSAGE;

/**
 * The command handler for the /tshop ticket setPurchaseMessage command.
 */
public class TicketSetPurchaseMessageCommandHandler extends SyncCommandHandler {
    //Example command: /tshop ticket setPurchaseMessage <message>
    private Player player;
    private String colouredDisplayText;
    private ItemStack ticket;

    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //Check that sender is a player
        if (!(sender instanceof Player)) {
            returnOnlyPlayersExecuteError(sender);
            return false;
        }
        player = (Player) sender;

        if (!player.hasPermission("traincartsticketshop.ticket.setpurchasemessage")) {
            returnInsufficientPermissionsError(player);
            return false;
        }

        //Check syntax
        if (args.length < 3) {
            returnMissingArgumentsError(sender, "/tshop ticket setPurchaseMessage <message>");
            return false;
        }
        if (args.length > 3) {
            returnInvalidSubCommandError(player, args[3]);
            return false;
        }

        colouredDisplayText = ChatColor.translateAlternateColorCodes('&', args[2]);

        if (colouredDisplayText.length() > 500) {
            returnError(player, "Purchase messages cannot be more than 500 characters in length");
            return false;
        }
        if (colouredDisplayText.isBlank()) {
            returnError(player, "Purchase messages cannot be blank");
            return false;
        }

        //Check that player is holding a ticket
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
        dataContainer.set(PURCHASE_MESSAGE, PersistentDataType.STRING, colouredDisplayText);
        ticket.setItemMeta(meta);
        player.sendMessage(ChatColor.GREEN + "Held ticket's purchase message was set successfully");
    }
}
