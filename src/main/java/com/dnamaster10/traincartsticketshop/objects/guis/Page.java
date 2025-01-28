package com.dnamaster10.traincartsticketshop.objects.guis;

import com.dnamaster10.traincartsticketshop.objects.buttons.Button;
import com.dnamaster10.traincartsticketshop.objects.buttons.Link;
import com.dnamaster10.traincartsticketshop.objects.buttons.SimpleHeadButton;
import com.dnamaster10.traincartsticketshop.objects.buttons.Ticket;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.LinkDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.TicketDatabaseObject;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import static com.dnamaster10.traincartsticketshop.objects.buttons.HeadData.HeadType.*;

/**
 * Holds the contents of a page within a Gui
 *
 * @see com.dnamaster10.traincartsticketshop.objects.guis.interfaces.Pageable
 */
public class Page {
    //Holds the contents of an inventory
    private final Button[] page = new Button[54];
    private String displayName = "Inventory";

    /**
     * Gets the contents of this page
     *
     * @return An array of Buttons
     * @see com.dnamaster10.traincartsticketshop.objects.buttons.Button
     */
    public Button[] getPage() {
        return page;
    }

    /**
     * Sets the display name to be used when the page is opened in an Inventory.
     *
     * @param displayName The colour formatted display name
     * @see Inventory
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    //TODO possibly use inventory.setStorageContents(ItemStack[] items);

    /**
     * Gets this page as an Inventory to be displayed to the player.
     *
     * @param inventoryHolder The Gui which will "own" this inventory
     * @return An inventory whose contents will match this page
     * @see Inventory
     * @see InventoryHolder
     */
    public Inventory getAsInventory(InventoryHolder inventoryHolder) {
        Inventory inventory = Bukkit.createInventory(inventoryHolder, 54, displayName);
        for (int slot = 0; slot < 54; slot++) {
            if (page[slot] != null) {
                inventory.setItem(slot, page[slot].getItemStack());
            }
        }
        return inventory;
    }

    /**
     * Adds a button to the page at the specified slot.
     *
     * @param slot The slot to add the button at
     * @param button The button to be added
     * @see Button
     */
    public void addButton(int slot, Button button) {
        page[slot] = button;
    }

    /**
     * Adds a back button to the page at a predetermined slot.
     *
     * @see SimpleHeadButton
     */
    public void addBackButton() {
        SimpleHeadButton button = new SimpleHeadButton("back", GRAY_BACK_ARROW, "Back");
        page[45] = button;
    }

    /**
     * Adds a next page button to the page at a predetermined slot.
     *
     * @see SimpleHeadButton
     */
    public void addNextPageButton() {
        SimpleHeadButton button = new SimpleHeadButton("next_page", CHAT_ARROW_RIGHT, "Next Page");
        page[53] = button;
    }

    /**
     * Adds a prev page button to the page at a predetermined slot.
     *
     * @see SimpleHeadButton
     */
    public void addPrevPageButton() {
        SimpleHeadButton button = new SimpleHeadButton("prev_page", CHAT_ARROW_LEFT, "Prev Page");
        page[52] = button;
    }

    /**
     * Automatically generates buttons from an array of TicketDatabaseObjects, and adds them to the page at their corresponding slot.
     *
     * @param tickets An array of TicketDatabaseObjects
     * @see TicketDatabaseObject
     */
    public void addFromTicketDatabaseObjects(TicketDatabaseObject[] tickets) {
        for (TicketDatabaseObject ticketDatabaseObject : tickets) {
            if (ticketDatabaseObject == null) continue;
            Ticket ticket = new Ticket(ticketDatabaseObject);
            page[ticketDatabaseObject.slot()] = ticket;
        }
    }

    /**
     * Automatically generates buttons from an array of LinkDatabaseObjects, and adds them to the page at their corresponding slot.
     *
     * @param links An array of LinkDatabaseObjects
     * @see LinkDatabaseObject
     */
    public void addFromLinkDatabaseObjects(LinkDatabaseObject[] links) {
        for (LinkDatabaseObject linkDatabaseObject : links) {
            if (linkDatabaseObject == null) continue;
            Link link = new Link(linkDatabaseObject);
            page[linkDatabaseObject.slot()] = link;
        }
    }
}
