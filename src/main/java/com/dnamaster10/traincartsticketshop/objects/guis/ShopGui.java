package com.dnamaster10.traincartsticketshop.objects.guis;

import com.dnamaster10.traincartsticketshop.objects.buttons.SimpleItemButton;
import com.dnamaster10.traincartsticketshop.objects.guis.interfaces.ClickHandler;
import com.dnamaster10.traincartsticketshop.objects.guis.interfaces.Pageable;
import com.dnamaster10.traincartsticketshop.util.GuiUtils;
import com.dnamaster10.traincartsticketshop.util.Session;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.accessors.LinkDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.accessors.TicketDataAccessor;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;
import static com.dnamaster10.traincartsticketshop.util.ButtonUtils.getButtonType;
import static com.dnamaster10.traincartsticketshop.util.GuiUtils.linkGui;

/**
 * A Gui which is used for browsing a ticket shop.
 */
public class ShopGui extends Gui implements InventoryHolder, ClickHandler, Pageable {
    private final int guiId;
    private final PageManager pageManager;
    private int maxPage;
    private final Player player;
    private String displayName;
    private Inventory inventory;

    /**
     * @param player The player who will be opening the Gui
     * @param guiId The ID of the Gui to open
     * @param pageNumber The page number to open the Shop Gui at
     */
    public ShopGui(Player player, int guiId, int pageNumber) {
        this.player = player;
        this.guiId = guiId;
        this.pageManager = new PageManager(pageNumber);

        getPlugin().getGuiManager().getSession(player).addGui(this);

        GuiDataAccessor guiDataAccessor = new GuiDataAccessor();
        try {
            displayName = guiDataAccessor.getDisplayName(guiId);
            maxPage = guiDataAccessor.getHighestPageNumber(guiId);
        } catch (QueryException e) {
            getPlugin().handleSqlException(player, e);
            Bukkit.getScheduler().runTask(getPlugin(), player::closeInventory);
            return;
        }
        pageManager.addPage(pageNumber, getNewPage(pageNumber));
        inventory = pageManager.getPage(pageNumber).getAsInventory(this);
    }

    /**
     * Defaults to the first page of the Shop Gui.
     *
     * @param player The player who will be opening the Gui
     * @param guiId The ID of the Gui to open
     */
    public ShopGui(Player player, int guiId) {
        this(player, guiId, 0);
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
        if (pageNumber < maxPage) page.addNextPageButton();
        if (pageNumber > 0) page.addPrevPageButton();
        if (getPlugin().getGuiManager().getSession(player).checkBack()) page.addBackButton();

        SimpleItemButton searchButton = new SimpleItemButton("search", Material.SPYGLASS, "Search this gui");
        page.addButton(49, searchButton);

        return page;
    }

    @Override
    public void open() {
        Bukkit.getScheduler().runTask(getPlugin(), () -> player.openInventory(inventory));
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        String buttonType = getButtonType(clickedItem);
        if (buttonType == null) return;

        switch (buttonType) {
            case "ticket" -> {
                GuiUtils.handleTicketItemPurchase(clickedItem, player);
                Bukkit.getScheduler().runTask(getPlugin(), player::closeInventory);
            }
            case "prev_page" -> prevPage();
            case "next_page" -> nextPage();
            case "back" -> {
                Session session = getPlugin().getGuiManager().getSession(player);
                if (!session.checkBack()) return;
                session.back();
            }
            case "search" -> {
                SearchSelectGui searchSelectGui = new SearchSelectGui(player, guiId);
                searchSelectGui.open();
            }
            case "link" -> linkGui(clickedItem, player);
        }
    }

    @Override
    public void nextPage() {
        int currentPageNumber = pageManager.getCurrentPageNumber();
        if (currentPageNumber >= maxPage) return;
        setPageNumber(currentPageNumber + 1);
    }

    @Override
    public void prevPage() {
        int currentPageNumber = pageManager.getCurrentPageNumber();
        if (currentPageNumber <= 0) return;
        setPageNumber(currentPageNumber - 1);
    }

    private void setPageNumber(int pageNumber) {
        pageManager.setCurrentPageNumber(pageNumber);
        if (!pageManager.hasPage(pageNumber)) {
            Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
                Page newPage = getNewPage(pageNumber);
                if (newPage == null) return;
                pageManager.addPage(pageNumber, newPage);
                inventory = newPage.getAsInventory(this);
                open();
            });
            return;
        }
        inventory = pageManager.getPage(pageNumber).getAsInventory(this);
        open();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
