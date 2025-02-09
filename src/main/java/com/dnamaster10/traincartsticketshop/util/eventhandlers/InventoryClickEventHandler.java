package com.dnamaster10.traincartsticketshop.util.eventhandlers;

import com.dnamaster10.traincartsticketshop.objects.guis.EditGui;
import com.dnamaster10.traincartsticketshop.objects.guis.interfaces.ClickHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickEventHandler implements Listener {
    @EventHandler(ignoreCancelled = true)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!(event.getClickedInventory().getHolder() instanceof ClickHandler c)) return;
        if (!(c instanceof EditGui)) event.setCancelled(true);
        c.handleClick(event);
    }
}
