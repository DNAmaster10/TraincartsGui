package com.dnamaster10.traincartsticketshop.objects.guis;

import com.dnamaster10.traincartsticketshop.objects.buttons.HeadData;
import com.dnamaster10.traincartsticketshop.objects.buttons.SimpleHeadButton;
import com.dnamaster10.traincartsticketshop.objects.guis.interfaces.ClickHandler;
import com.dnamaster10.traincartsticketshop.util.Session;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;
import static com.dnamaster10.traincartsticketshop.util.ButtonUtils.getButtonType;

/**
 * A Gui which prompts the player to confirm whether they want to delete a Gui.
 */
public class ConfirmGuiDeleteGui extends Gui implements InventoryHolder, ClickHandler {
    //Gui used for when someone wants to delete a gui.
    private final Player player;
    private final int deleteGuiId;
    private final Inventory inventory;

    /**
     * @param player The player who will use the Gui
     * @param guiToDeleteId The ID of the gui which should be deleted
     */
    public ConfirmGuiDeleteGui(Player player, int guiToDeleteId) {
        deleteGuiId = guiToDeleteId;
        this.player = player;

        getPlugin().getGuiManager().getSession(player).addGui(this);

        Page page = new Page();
        page.setDisplayName(ChatColor.RED + "Confirm Gui Deletion");
        if (getPlugin().getGuiManager().getSession(player).checkBack()) {
            page.addBackButton();
        }
        SimpleHeadButton deleteGuiButton = new SimpleHeadButton("confirm_action", HeadData.HeadType.RED_CROSS, "Delete Gui");
        page.addButton(22, deleteGuiButton);

        inventory = page.getAsInventory(this);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        String buttonType = getButtonType(clickedItem);
        if (buttonType == null) return;
        player.setItemOnCursor(null);

        switch (buttonType) {
            case "back" -> {
                Session session = getPlugin().getGuiManager().getSession(player);
                if (!session.checkBack()) return;
                session.back();
            }
            case "confirm_action" -> Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
                try {
                    GuiDataAccessor guiAccessor = new GuiDataAccessor();
                    guiAccessor.deleteGui(deleteGuiId);
                } catch (ModificationException e) {
                    getPlugin().handleSqlException(e);
                }
                player.sendMessage(ChatColor.GREEN + "Successfully deleted Gui");
                Bukkit.getScheduler().runTaskLater(getPlugin(), player::closeInventory, 1L);
            });
        }
    }

    @Override
    public void open() {
        Bukkit.getScheduler().runTask(getPlugin(), () -> player.openInventory(inventory));
    }
}
