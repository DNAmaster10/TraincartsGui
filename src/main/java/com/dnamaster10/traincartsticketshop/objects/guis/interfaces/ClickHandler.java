package com.dnamaster10.traincartsticketshop.objects.guis.interfaces;

import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Implemented by Guis which can be clicked.
 */
public interface ClickHandler {
    void handleClick(InventoryClickEvent event);
}
