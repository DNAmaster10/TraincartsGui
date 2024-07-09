package com.dnamaster10.traincartsticketshop.util.eventhandlers;

import com.dnamaster10.traincartsticketshop.objects.guis.Gui;
import com.dnamaster10.traincartsticketshop.objects.guis.GuiHolder;
import com.dnamaster10.traincartsticketshop.objects.guis.multipageguis.EditGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickEventHandler implements Listener {
    @EventHandler(ignoreCancelled = true)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!(event.getClickedInventory().getHolder() instanceof GuiHolder g)) return;
        Gui gui = g.getGui();
        if (!(gui instanceof EditGui)) event.setCancelled(true);
        gui.handleClick(event);
    }
}
