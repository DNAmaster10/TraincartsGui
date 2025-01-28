package com.dnamaster10.traincartsticketshop.objects.guis.interfaces;

import org.bukkit.event.inventory.InventoryDragEvent;

/**
 * Implemented by Guis which can be dragged over by the cursor.
 */
public interface DragHandler {
    void handleDrag(InventoryDragEvent event);
}
