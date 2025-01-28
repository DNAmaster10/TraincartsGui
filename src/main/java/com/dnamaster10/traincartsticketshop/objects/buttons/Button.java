package com.dnamaster10.traincartsticketshop.objects.buttons;

import org.bukkit.inventory.ItemStack;

/**
 * An abstract class extended by all other button classes.
 */
public abstract class Button {
    /**
     * Gets the Button's ItemStack.
     *
     * @return An ItemStack
     */
    public abstract ItemStack getItemStack();
}
