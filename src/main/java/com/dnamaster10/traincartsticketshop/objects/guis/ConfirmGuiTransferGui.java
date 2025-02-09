package com.dnamaster10.traincartsticketshop.objects.guis;

import com.dnamaster10.traincartsticketshop.objects.buttons.HeadData;
import com.dnamaster10.traincartsticketshop.objects.buttons.SimpleHeadButton;
import com.dnamaster10.traincartsticketshop.objects.guis.interfaces.ClickHandler;
import com.dnamaster10.traincartsticketshop.util.Session;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import com.dnamaster10.traincartsticketshop.util.database.accessors.PlayerDataAccessor;
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

public class ConfirmGuiTransferGui extends Gui implements InventoryHolder, ClickHandler {

    private final Player player;
    private final int guiId;
    private final String destPlayerUuid;
    private final Inventory inventory;

    public ConfirmGuiTransferGui(Player player, int guiId, String destPlayerUuid) {
        this.guiId = guiId;
        this.player = player;
        this.destPlayerUuid = destPlayerUuid;

        getPlugin().getGuiManager().getSession(player).addGui(this);

        Page page = new Page();
        page.setDisplayName(ChatColor.RED + "Confirm Gui Transfer");
        if (getPlugin().getGuiManager().getSession(player).checkBack()) {
            page.addBackButton();
        }

        PlayerDataAccessor playerDataAccessor = new PlayerDataAccessor();
        String username = playerDataAccessor.getPlayerByUuid(destPlayerUuid).username();

        SimpleHeadButton transferGuiButton = new SimpleHeadButton("confirm_action", HeadData.HeadType.CHAT_ARROW_RIGHT, "Transfer to " + username);
        page.addButton(22, transferGuiButton);

        inventory = page.getAsInventory(this);
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
                String username = "";
                String guiName = "";
                try {
                    GuiDataAccessor guiDataAccessor = new GuiDataAccessor();
                    guiDataAccessor.updateGuiOwner(guiId, destPlayerUuid);
                    guiName = guiDataAccessor.getGuiNameById(guiId);

                    PlayerDataAccessor playerDataAccessor = new PlayerDataAccessor();
                    username = playerDataAccessor.getPlayerByUuid(destPlayerUuid).username();
                } catch (ModificationException e) {
                    getPlugin().handleSqlException(e);
                }
                player.sendMessage(ChatColor.RED + "Successfully transferred gui \"" + guiName + "\" to player " + username);
            });
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void open() {
        Bukkit.getScheduler().runTask(getPlugin(), () -> player.openInventory(inventory));
    }
}
