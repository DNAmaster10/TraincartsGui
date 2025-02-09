package com.dnamaster10.traincartsticketshop.objects.buttons;

import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.LinkDatabaseObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.dnamaster10.traincartsticketshop.objects.buttons.DataKeys.*;

/**
 * A link, used within some Guis to link to a different Gui.
 */
public class Link extends Button {
    private final String displayName;
    private final int linkedGuiId;
    private final int linkedGuiPage;

    /**
     * Gets the link as a LinkDatabaseObject
     *
     * @param slot The slot in which this link can be found
     * @return A LinkDatabaseObject whose values will reflect this link
     * @see com.dnamaster10.traincartsticketshop.util.database.databaseobjects.LinkDatabaseObject
     */
    public LinkDatabaseObject getAsDatabaseObject(int slot) {
        String rawDisplayName = ChatColor.stripColor(displayName);
        return new LinkDatabaseObject(slot, linkedGuiId, linkedGuiPage, displayName, rawDisplayName);
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(this.displayName);

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(BUTTON_TYPE, PersistentDataType.STRING, "link");
        dataContainer.set(DEST_GUI_ID, PersistentDataType.INTEGER, linkedGuiId);
        dataContainer.set(DEST_GUI_PAGE, PersistentDataType.INTEGER, linkedGuiPage);

        item.setItemMeta(meta);
        return item;
    }

    /**
     * @param linkedGuiId The Gui which this link links to
     * @param linkedGuiPage The page in the linked Gui which this link links to
     * @param displayName The colour formatted display name for this link
     */
    public Link(int linkedGuiId, int linkedGuiPage, String displayName) {
        this.displayName = displayName;
        this.linkedGuiId = linkedGuiId;
        this.linkedGuiPage = linkedGuiPage;
    }

    /**
     * Creates a link button from a LinkDatabaseObject
     *
     * @param link The LinkDatabaseObject to use
     * @see com.dnamaster10.traincartsticketshop.util.database.databaseobjects.LinkDatabaseObject
     */
    public Link(LinkDatabaseObject link) {
        displayName = link.colouredDisplayName();
        linkedGuiId = link.linkedGuiId();
        linkedGuiPage = link.linkedGuiPage();
    }
}
