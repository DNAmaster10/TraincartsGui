package com.dnamaster10.traincartsticketshop.objects.buttons;

import org.bukkit.NamespacedKey;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;

/**
 * Holds commonly used keys for button ItemStacks.
 * Used to determine if an ItemStack is a button, and also determine which type of button it is.
 */
public class DataKeys {
    public static final NamespacedKey BUTTON_TYPE = new NamespacedKey(getPlugin(), "button_type");
    public static final NamespacedKey DEST_GUI_ID = new NamespacedKey(getPlugin(), "dest_gui_id");
    public static final NamespacedKey DEST_GUI_PAGE = new NamespacedKey(getPlugin(), "dest_gui_page");
    public static final NamespacedKey TC_TICKET_NAME = new NamespacedKey(getPlugin(), "tc_ticket_name");
    public static final NamespacedKey HEAD_TYPE = new NamespacedKey(getPlugin(), "tc_ticket_name");
    public static final NamespacedKey PURCHASE_MESSAGE = new NamespacedKey(getPlugin(), "purchase_message");
}
