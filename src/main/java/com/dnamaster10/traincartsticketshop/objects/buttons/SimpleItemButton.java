package com.dnamaster10.traincartsticketshop.objects.buttons;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static com.dnamaster10.traincartsticketshop.objects.buttons.DataKeys.BUTTON_TYPE;

/**
 * A button which onl needs to hold basic information.
 * Used for buttons such as the search gui button, which does not need to store any additional data.
 * @see com.dnamaster10.traincartsticketshop.objects.buttons.DataKeys
 */
public class SimpleItemButton extends Button {
    //For simple buttons using an item
    private final String buttonType;
    private final Material material;
    private final String displayText;

    @Override
    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        meta.setDisplayName(this.displayText);
        meta.getPersistentDataContainer().set(BUTTON_TYPE, PersistentDataType.STRING, this.buttonType);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * @param buttonType The button type
     * @param material The material to use
     * @param displayText The coloured display text for the ItemStack
     */
    public SimpleItemButton(String buttonType, Material material, String displayText) {
        this.buttonType = buttonType;
        this.material = material;
        this.displayText = displayText;
    }
}
