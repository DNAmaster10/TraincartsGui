package com.dnamaster10.tcgui;

import com.dnamaster10.tcgui.util.Database;
import com.dnamaster10.tcgui.util.Players;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;


public final class TraincartsGui extends JavaPlugin implements Listener {
    public static TraincartsGui plugin;
    @Override
    public void onEnable() {
        //Get plugin
        plugin = this;
        plugin.getLogger().info("Staring TraincartsGui...");

        //Load config file
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Create tables in database
        try {
            Database.createTables();
        }
        catch (SQLException e) {
            //Disable plugin if failed
            plugin.reportSqlError("Failed to create tables in database: " + String.valueOf(e));
            plugin.disable();
        }

        plugin.getLogger().info("TraincartsGui has finished loading!");
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Commands.execute(sender, command, label, args);
        return true;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Update player in database
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Players.updatePlayer(event.getPlayer());
            } catch (SQLException e) {
                reportSqlError(e.toString());
            }
        });
    }
    public void reportSqlError(String error) {
        plugin.getLogger().severe("A database error occurred: " + error);
    }
    public void reportSqlError(Player p, String error) {
        p.sendMessage(ChatColor.RED + "An error occurred. Check server logs for more info");
        plugin.getLogger().severe("A database error occurred: " + error);
    }
    public void disable() {
        //Disables the plugin. For use when a severe error occurs
        plugin.getLogger().info("Disabling TraincartsGui...");
        plugin.getServer().getPluginManager().disablePlugin(this);
    }
}
