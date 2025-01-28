package com.dnamaster10.traincartsticketshop.objects.guis;

import com.dnamaster10.traincartsticketshop.objects.buttons.Button;
import com.dnamaster10.traincartsticketshop.objects.buttons.Link;
import com.dnamaster10.traincartsticketshop.objects.buttons.SimpleHeadButton;
import com.dnamaster10.traincartsticketshop.objects.buttons.Ticket;
import com.dnamaster10.traincartsticketshop.objects.guis.interfaces.ClickHandler;
import com.dnamaster10.traincartsticketshop.objects.guis.interfaces.CloseHandler;
import com.dnamaster10.traincartsticketshop.objects.guis.interfaces.DragHandler;
import com.dnamaster10.traincartsticketshop.objects.guis.interfaces.Pageable;
import com.dnamaster10.traincartsticketshop.util.ButtonUtils;
import com.dnamaster10.traincartsticketshop.util.Traincarts;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.accessors.LinkDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.accessors.TicketDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.LinkDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.database.databaseobjects.TicketDatabaseObject;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;
import static com.dnamaster10.traincartsticketshop.objects.buttons.HeadData.HeadType.GREEN_PLUS;
import static com.dnamaster10.traincartsticketshop.objects.buttons.HeadData.HeadType.RED_CROSS;
import static com.dnamaster10.traincartsticketshop.util.ButtonUtils.getButtonType;

/**
 * A Gui which allows items to be removed, added, etc.
 * Used as the basic editor for a ticket shop.
 */
public class EditGui extends Gui implements InventoryHolder, ClickHandler, DragHandler, Pageable, CloseHandler {
    private final Player player;
    private final int guiId;
    private final PageManager pageManager;
    private int maxPage;
    private String displayName;
    private Inventory inventory;

    //The variable "asyncTaskRunning" is used to ensure that no actions can be performed while the contents of the
    //inventory are being saved to the database.
    private boolean asyncTaskRunning = false;
    private boolean cancelSave = false;
    private boolean cancelSaveMessage = false;

    /**
     * @param player The player who will be editing the Gui
     * @param guiId The ID of the Gui which will be edited
     * @param pageNumber The page to open the EditGui at
     */
    public EditGui(Player player, int guiId, int pageNumber) {
        this.player = player;
        this.guiId = guiId;
        this.pageManager = new PageManager(pageNumber);

        getPlugin().getGuiManager().getSession(player).addGui(this);

        GuiDataAccessor guiDataAccessor = new GuiDataAccessor();
        if (!guiDataAccessor.checkGuiById(guiId)) {
            player.sendMessage(ChatColor.RED + "Gui has been moved or deleted");
            Bukkit.getScheduler().runTask(getPlugin(), player::closeInventory);
            return;
        }
        try {
            displayName = guiDataAccessor.getDisplayName(guiId);
            maxPage = guiDataAccessor.getHighestPageNumber(guiId);
        } catch (QueryException e) {
            getPlugin().handleSqlException(e);
            Bukkit.getScheduler().runTask(getPlugin(), player::closeInventory);
        }
        pageManager.addPage(pageNumber, getNewPage(pageNumber));
        inventory = pageManager.getPage(pageNumber).getAsInventory(this);
    }

    /**
     * @param player The player who will be editing the Gui
     * @param guiId The ID of the Gui which will be edited
     */
    public EditGui(Player player, int guiId) {
        this(player, guiId, 0);
    }

    @Override
    public void open() {
        Player editor = getPlugin().getGuiManager().getGuiEditor(guiId);
        if (editor != null && player.getUniqueId() != editor.getUniqueId()) {
            player.sendMessage(ChatColor.RED + "Someone else is already editing that gui");
            Bukkit.getScheduler().runTask(getPlugin(), player::closeInventory);
            return;
        } else if (editor == null) {
            //Register the editor
            getPlugin().getGuiManager().addEditGui(guiId, player);
        }

        if (!pageManager.hasPage(pageManager.getCurrentPageNumber())) {
            Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
                pageManager.addPage(pageManager.getCurrentPageNumber(), getNewPage(pageManager.getCurrentPageNumber()));
                inventory = pageManager.getPage(pageManager.getCurrentPageNumber()).getAsInventory(this);
                Bukkit.getScheduler().runTask(getPlugin(), () -> player.openInventory(inventory));
            });
            return;
        }
        inventory = pageManager.getPage(pageManager.getCurrentPageNumber()).getAsInventory(this);
        Bukkit.getScheduler().runTask(getPlugin(), () -> player.openInventory(inventory));
    }

    private Page getNewPage(int pageNumber) {
        Page page = new Page();
        page.setDisplayName(displayName + " (" + (pageNumber + 1) + "/" + (maxPage + 1) + ")");

        TicketDataAccessor ticketDataAccessor = new TicketDataAccessor();
        LinkDataAccessor linkDataAccessor = new LinkDataAccessor();
        try {
            page.addFromTicketDatabaseObjects(ticketDataAccessor.getTickets(guiId, pageNumber));
            page.addFromLinkDatabaseObjects(linkDataAccessor.getLinks(guiId, pageNumber));
        } catch (QueryException e) {
            getPlugin().handleSqlException(player, e);
            Bukkit.getScheduler().runTask(getPlugin(), player::closeInventory);
            return null;
        }
        if (pageNumber + 1 < getPlugin().getConfig().getInt("MaxPagesPerGui")) page.addNextPageButton();
        if (pageNumber > 0) page.addPrevPageButton();
        if (maxPage < getPlugin().getConfig().getInt("MaxPagesPerGui")) {
            SimpleHeadButton insertPageButton = new SimpleHeadButton("insert_page", GREEN_PLUS, "Insert Page");
            page.addButton(47, insertPageButton);
        }
        SimpleHeadButton deletePageButton = new SimpleHeadButton("delete_page", RED_CROSS, "Delete Page");
        page.addButton(48, deletePageButton);

        return page;
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        String buttonType = getButtonType(clickedItem);
        if (buttonType != null && !buttonType.equals("ticket") && !buttonType.equals("link")) {
            event.setCancelled(true);
            if (asyncTaskRunning) return;
            else asyncTaskRunning = true;
            Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
                switch (buttonType) {
                    case "next_page" -> nextPage();
                    case "prev_page" -> prevPage();
                    case "delete_page" -> deletePage();
                    case "insert_page" -> insertPage();
                }
                asyncTaskRunning = false;
            });
            return;
        }

        //Player has not clicked a button, handle as if they are placing into or removing item from Gui
        if (event.getRawSlot() >= 45 && event.getRawSlot() < 54) {
            event.setCancelled(true);
            return;
        }
        if (event.getCursor() == null || event.getCursor().getType() == Material.AIR) return;

        ItemStack addedItem = event.getCursor();
        String addedButtonType = ButtonUtils.getButtonType(addedItem);
        if (addedButtonType != null) {
            if (!addedButtonType.equals("link") && !addedButtonType.equals("ticket")) event.setCancelled(true);
            inventory = event.getView().getTopInventory();
            return;
        }

        if (!Traincarts.isTraincartsTicket(addedItem)) {
            event.setCancelled(true);
            return;
        }

        Ticket convertedTicket = Traincarts.getAsTicketShopTicket(addedItem);
        if (convertedTicket == null) {
            event.setCancelled(true);
            return;
        }
        event.getWhoClicked().setItemOnCursor(convertedTicket.getItemStack());
        inventory = event.getView().getTopInventory();
    }

    @Override
    public void handleDrag(InventoryDragEvent event) {
        Set<Integer> slots = event.getRawSlots();

        for (int slot : slots) {
            if (slot < 54 && slot >= 45) {
                event.setCancelled(true);
                return;
            }
        }

        if (!event.getNewItems().entrySet().iterator().hasNext()) {
            event.setCancelled(true);
            return;
        }

        Map.Entry<Integer, ItemStack> entry = event.getNewItems().entrySet().iterator().next();
        ItemStack addedItem = entry.getValue();
        String buttonType = getButtonType(addedItem);
        if (buttonType == null) {
            if (!Traincarts.isTraincartsTicket(addedItem)) {
                event.setCancelled(true);
                return;
            }
            //Item is a traincarts ticket, get ticket shop ticket from that.
            Ticket convertedTicket = Traincarts.getAsTicketShopTicket(addedItem);
            if (convertedTicket == null) {
                event.setCancelled(true);
                return;
            }
            final ItemStack convertedItem = convertedTicket.getItemStack();
            Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
                for (int slot : slots) {
                    if (slot < 45) {
                        event.getInventory().setItem(slot, convertedItem);
                    }
                }
            }, 1L);
        } else if (!buttonType.equals("link") && !buttonType.equals("ticket")) {
            event.setCancelled(true);
        }
        inventory = event.getView().getTopInventory();
    }

    private void deletePage() {
        savePage();
        cancelSave = true;
        cancelSaveMessage = true;
        pageManager.clearCache();
        ConfirmPageDeleteGui pageDeleteGui = new ConfirmPageDeleteGui(player, guiId, pageManager.getCurrentPageNumber());
        pageDeleteGui.open();
    }

    private void insertPage() {
        savePage();
        cancelSave = true;
        cancelSaveMessage = true;
        pageManager.clearCache();
        GuiDataAccessor guiDataAccessor = new GuiDataAccessor();
        try {
            guiDataAccessor.insertPage(guiId, pageManager.getCurrentPageNumber());
        } catch (ModificationException e) {
            getPlugin().handleSqlException(e);
            Bukkit.getScheduler().runTask(getPlugin(), player::closeInventory);
        }
        open();
    }

    private void savePage() {
        //TODO possible optimization to check if any changes were made before saving
        List<TicketDatabaseObject> tickets = new ArrayList<>();
        List<LinkDatabaseObject> links = new ArrayList<>();

        Page newPage = new Page();
        newPage.setDisplayName(displayName + " (" + (pageManager.getCurrentPageNumber() + 1) + "/" + (maxPage + 1) + ")");
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item == null) continue;
            String buttonType = getButtonType(item);
            if (buttonType == null) continue;
            Button button = ButtonUtils.getNewButton(buttonType, item);
            if (button instanceof Ticket t) tickets.add(t.getAsDatabaseObject(slot));
            if (button instanceof Link l) links.add(l.getAsDatabaseObject(slot));
            newPage.addButton(slot, button);
        }

        TicketDataAccessor ticketDataAccessor = new TicketDataAccessor();
        LinkDataAccessor linkDataAccessor = new LinkDataAccessor();

        try {
            ticketDataAccessor.saveTicketPage(guiId, pageManager.getCurrentPageNumber(), tickets);
            linkDataAccessor.saveLinkPage(guiId, pageManager.getCurrentPageNumber(), links);
        } catch (ModificationException e) {
            getPlugin().handleSqlException(e);
            Bukkit.getScheduler().runTask(getPlugin(), player::closeInventory);
        }

        pageManager.addPage(pageManager.getCurrentPageNumber(), newPage);
    }
    @Override
    public void handleClose() {
        if (cancelSave) {
            cancelSave = false;
            cancelSaveMessage = false;
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            savePage();
            getPlugin().getGuiManager().removeEditGui(guiId);
            if (!cancelSaveMessage) {
                player.sendMessage(ChatColor.GREEN + "Your changes have been saved!");
                cancelSaveMessage = false;
            }
        });
    }

    @Override
    public void nextPage() {
        if (pageManager.getCurrentPageNumber() + 1 >= getPlugin().getConfig().getInt("MaxPagesPerGui")) return;
        savePage();
        cancelSave = true;
        cancelSaveMessage = true;
        pageManager.setCurrentPageNumber(pageManager.getCurrentPageNumber() + 1);
        if (pageManager.getCurrentPageNumber() > maxPage) maxPage = pageManager.getCurrentPageNumber();
        open();
    }

    @Override
    public void prevPage() {
        if (pageManager.getCurrentPageNumber() - 1 < 0) return;
        savePage();
        cancelSave = true;
        cancelSaveMessage = true;
        pageManager.setCurrentPageNumber(pageManager.getCurrentPageNumber() - 1);
        open();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
