package com.dnamaster10.traincartsticketshop.util;

import com.dnamaster10.traincartsticketshop.objects.guis.ShopGui;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;
import static com.dnamaster10.traincartsticketshop.objects.buttons.DataKeys.*;

public class GuiUtils {
    private static final String DEFAULT_PURCHASE_MESSAGE = getPlugin().getConfig().getString("PurchaseMessage");

    /**
     * Handles a ticket purchase.
     *
     * @param ticketItem The Ticket Shop Ticket ItemStack
     * @param player The player making the purchase
     */
    public static void handleTicketItemPurchase(ItemStack ticketItem, Player player) {
        ItemMeta meta = ticketItem.getItemMeta();
        if (meta == null) {
            player.sendMessage(ChatColor.RED + "Ticket info is broken, failed to purchase ticket");
            return;
        }
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        if (!dataContainer.has(TC_TICKET_NAME, PersistentDataType.STRING)) {
            player.sendMessage(ChatColor.RED + "Ticket info is broken, failed to purchase ticket");
            return;
        }
        String tcName = dataContainer.get(TC_TICKET_NAME, PersistentDataType.STRING);
        if (tcName == null || !Traincarts.checkTicket(tcName)) {
            player.sendMessage(ChatColor.RED + "Traincarts ticket \"" + tcName + "\" is invalid or has been deleted");
            return;
        }
        String purchaseMessage = null;
        if (dataContainer.has(PURCHASE_MESSAGE, PersistentDataType.STRING)) {
            purchaseMessage = dataContainer.get(PURCHASE_MESSAGE, PersistentDataType.STRING);
            if (purchaseMessage != null && purchaseMessage.isBlank()) purchaseMessage = null;
        }

        //Handle purchase
        Traincarts.giveTicketItem(tcName, player);
        player.sendMessage(ChatColor.GREEN + DEFAULT_PURCHASE_MESSAGE);
        if (purchaseMessage != null) {
            player.sendMessage("");
            player.sendMessage(ChatColor.AQUA + purchaseMessage);
            player.sendMessage("");
        }

    }

    /**
     * Handles a link between two Guis.
     *
     * @param linkItem The link ItemStack which is being handled
     * @param player The player who is being linked
     */
    public static void linkGui(ItemStack linkItem, Player player) {
        ItemMeta meta = linkItem.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        Integer linkedGuiId = dataContainer.get(DEST_GUI_ID, PersistentDataType.INTEGER);
        Integer linkedGuiPage = dataContainer.get(DEST_GUI_PAGE, PersistentDataType.INTEGER);

        if (linkedGuiId == null || linkedGuiPage == null) return;

        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            GuiDataAccessor guiDataAccessor = new GuiDataAccessor();
            if (!guiDataAccessor.checkGuiById(linkedGuiId)) return;
            int goalPage = linkedGuiPage;
            if (goalPage > 0) {
                try {
                    if (guiDataAccessor.getHighestPageNumber(linkedGuiId) < goalPage) {
                        goalPage = 0;
                    }
                } catch (QueryException e) {
                    getPlugin().handleSqlException(player, e);
                    return;
                }
            }
            ShopGui shopGui = new ShopGui(player, linkedGuiId, goalPage);
            shopGui.open();
        });
    }
}
