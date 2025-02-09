package com.dnamaster10.traincartsticketshop.objects.buttons;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;
import static com.dnamaster10.traincartsticketshop.objects.buttons.DataKeys.HEAD_TYPE;

/**
 * Holds information and methods used for custom head items, such as the texture sources.
 */
public class HeadData {
    /**
     * Links head enums to their corresponding texture
     */
    public enum HeadType {
        RED_CROSS("https://textures.minecraft.net/texture/beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7"),
        GRAY_BACK_ARROW("https://textures.minecraft.net/texture/1b701c1f05e319d6b28f61b28b66a7e2a846a510de322bdc96e94a2388b78469"),
        GREEN_PLUS("https://textures.minecraft.net/texture/5ff31431d64587ff6ef98c0675810681f8c13bf96f51d9cb07ed7852b2ffd1"),
        CHAT_ARROW_RIGHT("https://textures.minecraft.net/texture/8399e5da82ef7765fd5e472f3147ed118d981887730ea7bb80d7a1bed98d5ba"),
        CHAT_ARROW_LEFT("https://textures.minecraft.net/texture/76ebaa41d1d405eb6b60845bb9ac724af70e85eac8a96a5544b9e23ad6c96c62");
        public final String url;
        HeadType(String url) {
            this.url = url;
        }
    }

    private static final UUID RANDOM_UUID = UUID.fromString("68f92a5b-8980-4e0c-a479-89e41ce1ada6");
    private static PlayerProfile getProfile(HeadType type) {
        PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(type.url);
        } catch (MalformedURLException e) {
            getPlugin().getLogger().warning("An error occurred creating a player head within a GUI: " + e);
            return null;
        }
        textures.setSkin(urlObject);
        profile.setTextures(textures);
        return profile;
    }

    /**
     * Gets a new head ItemStack, and applies the corresponding textures to the head.
     *
     * @param type The head type to be made
     * @return The head
     */
    public static ItemStack getPlayerHeadItem(HeadType type) {
        PlayerProfile headProfile = getProfile(type);
        if (headProfile == null) {
            //If head creation failed, just return a potato
            return new ItemStack(Material.POTATO, 1);
        }

        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        assert meta != null;

        meta.setOwnerProfile(headProfile);
        meta.getPersistentDataContainer().set(HEAD_TYPE, PersistentDataType.STRING, type.toString());
        head.setItemMeta(meta);
        return head;
    }

    /**
     * Gets the HeadType from a head ItemStack.
     *
     * @param item The item stack of the item to get the head type from
     * @return The HeadType. Returns null if the ItemStack is not a valid HeadType
     */
    public static HeadType getHeadTypeFromItem(ItemStack item) {
        if (!(item.getItemMeta() instanceof SkullMeta meta)) {
            return null;
        }
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        String type = dataContainer.get(HEAD_TYPE, PersistentDataType.STRING);

        HeadType headType;
        try {
            headType = HeadType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return headType;
    }
}
