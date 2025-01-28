package com.dnamaster10.traincartsticketshop.util;

import com.dnamaster10.traincartsticketshop.objects.guis.ShopGui;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import com.dnamaster10.traincartsticketshop.util.database.accessors.GuiDataAccessor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

import static com.dnamaster10.traincartsticketshop.TraincartsTicketShop.getPlugin;

public class SignHandler {

    /*
    * Sign Format Example:
    * Line 1: This is a gui!
    * Line 2: [tshop] <option page number>
    * Line 3: <gui name>
    * Line 4: Test123
    * */

    private static final String signIdentifier = getPlugin().getConfig().getString("SignIdentifier");

    boolean isGuiSign(SignSide side) {
        if (signIdentifier == null || signIdentifier.isBlank()) return false;
        return ChatColor.stripColor(side.getLine(1)).contains(signIdentifier);
    }

    int getPage(SignSide side) {
        //Defaults to page 0 if none is specified
        String[] args = ChatColor.stripColor(side.getLine(1)).split(" ");
        if (args.length > 1) {
            if (Utilities.isInt(args[1])) {
                //Page must be reduced by one to convert from a regular number to an index
                return Integer.parseInt(args[1]) - 1;
            }
        }
        return 0;
    }

    String getGuiName(SignSide side) {
        String nameLine = ChatColor.stripColor(side.getLine(2));
        if (nameLine.isBlank()) {
            return null;
        }
        return nameLine;
    }

    /**
     * Handles a sign click.
     *
     * @param event The PlayerInteractEvent to be handled
     * @return Returns true if the event should be cancelled
     */
    public boolean handleSignClickEvent(PlayerInteractEvent event) {
        // Clicked block here will always be a sign as this is checked from the PlayerInteractEvent handler
        if (event.getPlayer().isSneaking()) {
            return false;
        }

        //First check that the sign is a traincartsticketshop sign
        Sign sign = (Sign) Objects.requireNonNull(event.getClickedBlock()).getState();
        SignSide side = sign.getTargetSide(event.getPlayer());

        if (!isGuiSign(side)) return false;
        String guiName = getGuiName(side);

        //Handle sign click async from here. True will be returned beforehand to cancel the sign edit event.
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            try {
                GuiDataAccessor guiAccessor = new GuiDataAccessor();
                if (!guiAccessor.checkGuiByName(guiName)) {
                    event.getPlayer().sendMessage(ChatColor.RED + "No gui with name \"" + guiName + "\" exists");
                    return;
                }
                //Check the max page number. If the number on the sign is higher than the max pages in the gui, set the page to the highest possible page
                int page = getPage(side);
                int guiId = guiAccessor.getGuiIdByName(guiName);
                int maxPage = guiAccessor.getHighestPageNumber(guiId);
                if (page > maxPage) {
                    page = maxPage;
                }
                if (page < 0) {
                    page = 0;
                }

                Player player = event.getPlayer();

                //Create a new session
                getPlugin().getGuiManager().openNewSession(player);

                //Create the new gui
                ShopGui shopGui = new ShopGui(player, guiId, page);

                //Open the gui
                shopGui.open();

            } catch (QueryException e) {
                getPlugin().handleSqlException(event.getPlayer(), e);
            }
        });
        return  true;
    }
}
