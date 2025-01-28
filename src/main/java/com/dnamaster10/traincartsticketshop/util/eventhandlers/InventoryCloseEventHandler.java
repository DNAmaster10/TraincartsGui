package com.dnamaster10.traincartsticketshop.util.eventhandlers;

import com.dnamaster10.traincartsticketshop.objects.guis.Gui;
import com.dnamaster10.traincartsticketshop.objects.guis.interfaces.CloseHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseEventHandler implements Listener {
    @EventHandler
    void onPlayerCloseInventory(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof Gui g)) return;
        if (g instanceof CloseHandler c) c.handleClose();
    }
}
