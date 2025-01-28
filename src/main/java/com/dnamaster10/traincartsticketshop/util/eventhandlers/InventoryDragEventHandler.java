package com.dnamaster10.traincartsticketshop.util.eventhandlers;

import com.dnamaster10.traincartsticketshop.objects.guis.EditGui;
import com.dnamaster10.traincartsticketshop.objects.guis.Gui;
import com.dnamaster10.traincartsticketshop.objects.guis.interfaces.DragHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryDragEventHandler implements Listener {
    @EventHandler(ignoreCancelled = true)
    void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getInventory().getHolder() instanceof Gui g)) return;

        for (int slot : event.getRawSlots()) {
            if (slot < 54) {
                if (!(g instanceof DragHandler)) {
                    event.setCancelled(true);
                    return;
                } else if (g instanceof EditGui e) {
                    e.handleDrag(event);
                } else {
                    event.setCancelled(true);
                    ((DragHandler) g).handleDrag(event);
                }
            }
        }
    }
}
