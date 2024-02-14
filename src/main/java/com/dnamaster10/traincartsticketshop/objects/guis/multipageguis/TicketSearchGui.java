package com.dnamaster10.traincartsticketshop.objects.guis.multipageguis;

import com.dnamaster10.traincartsticketshop.objects.buttons.Button;
import com.dnamaster10.traincartsticketshop.objects.guis.PageBuilder;
import com.dnamaster10.traincartsticketshop.util.Traincarts;
import com.dnamaster10.traincartsticketshop.util.database.GuiAccessor;
import com.dnamaster10.traincartsticketshop.util.database.TicketAccessor;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.TicketDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.DQLException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.dnamaster10.traincartsticketshop.objects.buttons.Buttons.getButtonType;
import static com.dnamaster10.traincartsticketshop.objects.buttons.DataKeys.TC_TICKET_NAME;

public class TicketSearchGui extends SearchGui {
    @Override
    protected Button[] generateNewPage() throws DQLException {
        PageBuilder pageBuilder = new PageBuilder();
        TicketAccessor ticketAccessor = new TicketAccessor();

        TicketDatabaseObject[] ticketDatabaseObjects = ticketAccessor.searchTickets(getSearchGuiId(), getPageNumber() * 45, getSearchTerm());
        pageBuilder.addTickets(ticketDatabaseObjects);

        if (ticketAccessor.getTotalTicketSearchResults(getSearchGuiId(), getSearchTerm()) > (getPageNumber() + 1) * 45) {
            pageBuilder.addNextPageButton();
        }
        if (getPageNumber() > 0) {
            pageBuilder.addPrevPageButton();
        }

        return pageBuilder.getPage();
    }
    @Override
    public void handleClick(InventoryClickEvent event, ItemStack clickedItem) {
        String buttonType = getButtonType(clickedItem);
        if (buttonType == null) {
            return;
        }
        getPlayer().setItemOnCursor(null);
        switch (buttonType) {
            case "ticket" -> handleTicketClick(clickedItem);
            case "prev_page" -> prevPage();
            case "next_page" -> nextPage();
        }
    }
    public void handleTicketClick(ItemStack ticket) {
        //Get ticket data
        ItemMeta meta = ticket.getItemMeta();
        if (meta == null) {
            return;
        }
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        if (!dataContainer.has(TC_TICKET_NAME, PersistentDataType.STRING)) {
            return;
        }
        String tcName = dataContainer.get(TC_TICKET_NAME, PersistentDataType.STRING);

        //Check that the tc ticket exists
        if (!Traincarts.checkTicket(tcName)) {
            return;
        }

        //Give ticket to player
        Traincarts.giveTicketItem(tcName, getPlayer());
        removeCursorItemAndClose();
    }
    public TicketSearchGui(int searchGuiId, String searchTerm, int page, Player p) throws DQLException {
        //Must be run async
        setSearchGuiId(searchGuiId);
        setSearchTerm(searchTerm);
        setPageNumber(page);
        setPlayer(p);

        //Get gui display name
        GuiAccessor guiAccessor = new GuiAccessor();
        setDisplayName("Searching: " + guiAccessor.getColouredDisplayNameById(searchGuiId));
    }
}