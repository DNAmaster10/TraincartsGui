package com.dnamaster10.traincartsticketshop.util;

import com.dnamaster10.traincartsticketshop.objects.buttons.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.dnamaster10.traincartsticketshop.objects.buttons.DataKeys.*;

public class ButtonUtils {
    //A utility class for buttons
    public static String getButtonType(ItemStack button) {
        //Returns the button type from a given item
        //First check if item is a button
        if (!button.hasItemMeta()) {
            return null;
        }
        ItemMeta meta = button.getItemMeta();
        assert meta != null;
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

        if (!dataContainer.has(BUTTON_TYPE, PersistentDataType.STRING)) {
            return null;
        }

        return dataContainer.get(BUTTON_TYPE, PersistentDataType.STRING);
    }
    public static Button getNewButton(String buttonType, ItemStack item) {
        //Returns a new button object from a button type and an item
        //If any of the item keys are invalid, null will be returned
        if (!item.hasItemMeta()) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        String displayName = meta.getDisplayName();

        switch (buttonType) {
            //TODO maybe move each of these into their own method?
            case "ticket" -> {
                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                if (!dataContainer.has(TC_TICKET_NAME, PersistentDataType.STRING)) {
                    return null;
                }
                String tcTicketName = dataContainer.get(TC_TICKET_NAME, PersistentDataType.STRING);
                if (tcTicketName == null) {
                    return null;
                }
                String purchaseMessage = null;
                if (dataContainer.has(PURCHASE_MESSAGE, PersistentDataType.STRING)) purchaseMessage = dataContainer.get(PURCHASE_MESSAGE, PersistentDataType.STRING);
                if (purchaseMessage == null || purchaseMessage.isBlank()) purchaseMessage = "";
                return new Ticket(tcTicketName, displayName, purchaseMessage);
            }
            case "link" -> {
                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                if (!dataContainer.has(DEST_GUI_ID, PersistentDataType.INTEGER)) {
                    return null;
                }
                Integer linkedGuiId = dataContainer.get(DEST_GUI_ID, PersistentDataType.INTEGER);
                if (linkedGuiId == null) {
                    return null;
                }
                Integer linkedGuiPage = dataContainer.get(DEST_GUI_PAGE, PersistentDataType.INTEGER);
                if (linkedGuiPage == null) {
                    linkedGuiPage = 0;
                }
                return new Link(linkedGuiId, linkedGuiPage, displayName);
            }
            default -> {
                //Item is a simple button, handle as such
                //Check if the simple button is a head button or an item button
                Material material = item.getType();
                if (material == Material.PLAYER_HEAD) {
                    //Item is a payer head button, create new button for that
                    //First get the head type
                    HeadData.HeadType type = HeadData.getHeadTypeFromItem(item);
                    if (type == null) {
                        return null;
                    }
                    return new SimpleHeadButton(buttonType, type, displayName);
                }
                else {
                    //Item is a regular item button, create new button for that
                    return new SimpleItemButton(buttonType, material, displayName);
                }
            }
        }
    }

}
