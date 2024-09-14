package me.zivush.punishmentsystemz;

import org.bukkit.plugin.java.JavaPlugin;

public class PunishmentSystemZ extends JavaPlugin {
    private static PunishmentSystemZ instance;
    private DatabaseManager databaseManager;
    private PunishmentManager punishmentManager;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Utils.setPlugin(this);


        this.databaseManager = new DatabaseManager(this);
        this.punishmentManager = new PunishmentManager(this);
        this.commandHandler = new CommandHandler(this);

        getServer().getPluginManager().registerEvents(new EventListener(this), this);

        getCommand("ban").setExecutor(commandHandler);
        getCommand("unban").setExecutor(commandHandler);
        getCommand("kick").setExecutor(commandHandler);
        getCommand("mute").setExecutor(commandHandler);
        getCommand("unmute").setExecutor(commandHandler);
        getCommand("blacklist").setExecutor(commandHandler);
        getCommand("banip").setExecutor(commandHandler);
        getCommand("unbanip").setExecutor(commandHandler);
        getCommand("history").setExecutor(commandHandler);

        getLogger().info("PunishmentSystemZ has been enabled!");
    }

    @Override
    public void onDisable() {
        databaseManager.saveData();
        getLogger().info("PunishmentSystemZ has been disabled!");
    }

    public static PunishmentSystemZ getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }
}