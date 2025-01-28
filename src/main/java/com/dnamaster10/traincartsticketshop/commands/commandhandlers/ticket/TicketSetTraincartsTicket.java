package com.dnamaster10.traincartsticketshop.commands.commandhandlers.ticket;

import com.dnamaster10.traincartsticketshop.commands.commandhandlers.SyncCommandHandler;
import com.dnamaster10.traincartsticketshop.util.Traincarts;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.dnamaster10.traincartsticketshop.util.ButtonUtils.getButtonType;
import static com.dnamaster10.traincartsticketshop.objects.buttons.DataKeys.TC_TICKET_NAME;

/**
 * The command handler for the /tshop ticket setTraincartsTicket command.
 */
public class TicketSetTraincartsTicket extends SyncCommandHandler {
    //Example command: /tshop ticket setTraincartsTicket <traincarts ticket>
    Player player;
    ItemStack ticket;
    @Override
    protected boolean checkSync(CommandSender sender, String[] args) {
        //Check permissions and that sender is player
        if (!(sender instanceof Player)) {
            returnOnlyPlayersExecuteError(sender);
            return false;
        }
        player = (Player) sender;

        if (!player.hasPermission("traincartsticketshop.ticket.settraincartsticket")) {
            returnInsufficientPermissionsError(player);
            return false;
        }

        //Check syntax
        if (args.length < 3) {
            returnMissingArgumentsError(player, "/tshop ticket setTraincartsTicket <tc ticket name>");
            return false;
        }
        if (args.length > 3) {
            returnInvalidSubCommandError(player, args[3]);
            return false;
        }

        //Check Traincarts ticket exists
        if (!Traincarts.checkTicket(args[2])) {
            returnError(player, "Traincarts ticket \"" + args[2] + "\" does not exist");
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
        dataContainer.set(TC_TICKET_NAME, PersistentDataType.STRING, args[2]);
        ticket.setItemMeta(meta);

        player.sendMessage(ChatColor.GREEN + "Traincarts ticket changed to \"" + args[2] + "\"");
    }
}
