package com.dnamaster10.traincartsticketshop;

import com.dnamaster10.traincartsticketshop.commands.CommandDispatcher;
import com.dnamaster10.traincartsticketshop.commands.MainTabCompleter;
import com.dnamaster10.traincartsticketshop.util.ConfigUtils;
import com.dnamaster10.traincartsticketshop.util.GuiManager;
import com.dnamaster10.traincartsticketshop.util.SignHandler;
import com.dnamaster10.traincartsticketshop.util.eventhandlers.*;
import com.dnamaster10.traincartsticketshop.util.exceptions.ModificationException;
import com.dnamaster10.traincartsticketshop.util.exceptions.QueryException;
import com.dnamaster10.traincartsticketshop.util.database.DatabaseAccessorFactory;
import com.dnamaster10.traincartsticketshop.util.database.accessors.DataAccessor;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;


public final class TraincartsTicketShop extends JavaPlugin implements Listener {
    public static TraincartsTicketShop plugin;

    private GuiManager guiManager;
    /**
     * @return The gui manager currently in use.
     * @see GuiManager
     */
    public GuiManager getGuiManager() {
        return this.guiManager;
    }

    private SignHandler signHandler;
    /**
     * @return The sign handler current in use.
     * @see SignHandler
     */
    public SignHandler getSignHandler() {
        return this.signHandler;
    }

    /**
     * @return This class instance
     */
    public static TraincartsTicketShop getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        //Get plugin
        plugin = this;
        plugin.getLogger().info("Starting TraincartsTicketShop...");

        //Load config file
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Update config stuff if needed
        try {
            ConfigUtils.migrateIfNeeded(getConfig(), this);
        } catch (IOException e) {
            getLogger().severe("Failed to update config values");
            e.printStackTrace();
        }

        //Create database tables
        try {
            DatabaseAccessorFactory.getDatabaseTableCreator().createTables();
        } catch (ModificationException e) {
            //Disable plugin if failed
            plugin.handleSqlException(e);
        }

        //Initialize database caches
        try {
            new DataAccessor().initializeCaches();
        } catch (QueryException e) {
            plugin.handleSqlException(e);
            getPlugin().disable();
        }

        //Register the "traincartsticketshop" command
        Objects.requireNonNull(getCommand("traincartsticketshop")).setExecutor(new CommandDispatcher());

        //Register tab completers
        Objects.requireNonNull(getCommand("traincartsticketshop")).setTabCompleter(new MainTabCompleter());

        //Register gui manager
        this.guiManager = new GuiManager();

        //Register the sign handler
        this.signHandler = new SignHandler();

        //Register listeners
        getServer().getPluginManager().registerEvents(new InventoryCloseEventHandler(), plugin);
        getServer().getPluginManager().registerEvents(new InventoryClickEventHandler(), plugin);
        getServer().getPluginManager().registerEvents(new InventoryDragEventHandler(), plugin);
        getServer().getPluginManager().registerEvents(new PlayerJoinEventHandler(), plugin);
        getServer().getPluginManager().registerEvents(new PlayerQuitEventHandler(), plugin);
        getServer().getPluginManager().registerEvents(new PlayerInteractEventHandler(), plugin);

        //Register BStats metrics class
        int pluginId = 23289;
        Metrics metrics = new Metrics(this, pluginId);

        plugin.getLogger().info("TraincartsTicketShop has finished loading!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Called to handle any database exceptions.
     *
     * @param e The SQLException
     * @see ModificationException
     * @see QueryException
     */
    public void handleSqlException(SQLException e) {
        //If the exception was only a query, report it. If it altered the database, disable the plugin too as a failsafe.
        plugin.getLogger().severe("A database error occurred: " + e);
        e.printStackTrace();
        if (e instanceof QueryException) {
            return;
        }
        if (plugin.getConfig().getBoolean("DisableOnDatabaseModificationException")) {
            disable();
        }
    }

    /**
     * Called to handle any database exceptions.
     * Will also return a short message to the sender indicating that an error occurred.
     *
     * @param sender The CommandSender
     * @param e The SQLException
     * @see ModificationException
     * @see QueryException
     */
    public void handleSqlException(CommandSender sender, SQLException e) {
        sender.sendMessage(ChatColor.RED + "A database error occurred. Check server logs for more info");
        handleSqlException(e);
    }

    /**
     * Disables the plugin. Note that EditGuis do not need to be saved here, as shutting down the server also triggers
     * the inventory close event, automatically saving all changes.
     */
    public void disable() {
        //Disables the plugin. For use when a severe error occurs
        plugin.getLogger().info("Disabling TraincartsTicketShop...");
        plugin.getServer().getPluginManager().disablePlugin(this);
    }
}
