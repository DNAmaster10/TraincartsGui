package com.dnamaster10.traincartsticketshop.objects.buttons;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.TicketDatabaseObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.dnamaster10.traincartsticketshop.objects.buttons.DataKeys.*;

/**
 * A ticket, used within some Guis. Can be used to purchase a Traincarts ticket.
 */
public class Ticket extends Button {
    private final String tcTicketName;
    private final String displayName;
    private final String purchaseMessage;

    /**
     * Gets the ticket as a TicketDatabaseObject
     *
     * @param slot The slot in which this ticket can be found
     * @return A TicketDatabaseObject whose values will reflect this ticket
     * @see com.dnamaster10.traincartsticketshop.util.database.databaseobjects.TicketDatabaseObject
     */
    public TicketDatabaseObject getAsDatabaseObject(int slot) {
        String rawDisplayName = ChatColor.stripColor(displayName);
        return new TicketDatabaseObject(slot, tcTicketName, displayName, rawDisplayName, purchaseMessage);
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(displayName);

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(BUTTON_TYPE, PersistentDataType.STRING, "ticket");
        dataContainer.set(TC_TICKET_NAME, PersistentDataType.STRING, this.tcTicketName);
        if (purchaseMessage != null) {
            dataContainer.set(PURCHASE_MESSAGE, PersistentDataType.STRING, this.purchaseMessage);
        }

        item.setItemMeta(meta);
        return item;
    }

    /**
     * @param tcName The Traincarts ticket name
     * @param displayName The colour formatted display name for this ticket
     * @param purchaseMessage The message to be displayed when the ticket is purchased
     */
    public Ticket(String tcName, String displayName, String purchaseMessage) {
        this.tcTicketName = tcName;
        this.displayName = displayName;
        this.purchaseMessage = purchaseMessage;
    }

    /**
     * Creates a ticket button from a TicketDatabaseObject
     *
     * @param ticket The TicketDatabaseObject
     * @see com.dnamaster10.traincartsticketshop.util.database.databaseobjects.TicketDatabaseObject
     */
    public Ticket(TicketDatabaseObject ticket) {
        tcTicketName = ticket.tcName();
        displayName = ticket.colouredDisplayName();
        purchaseMessage = ticket.purchaseMessage();
    }
}
