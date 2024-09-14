package me.zivush.punishmentsystemz;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DatabaseManager {
    private final PunishmentSystemZ plugin;
    private final File databaseFile;
    private FileConfiguration database;

    public DatabaseManager(PunishmentSystemZ plugin) {
        this.plugin = plugin;
        this.databaseFile = new File(plugin.getDataFolder(), "database.yml");
        loadData();
    }

    private void loadData() {
        if (!databaseFile.exists()) {
            plugin.saveResource("database.yml", false);
        }
        database = YamlConfiguration.loadConfiguration(databaseFile);
    }

    public void saveData() {
        try {
            database.save(databaseFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save database.yml!");
            e.printStackTrace();
        }
    }

    public void addPunishment(UUID uuid, Punishment punishment) {
        String path = uuid.toString() + ".active." + punishment.getType().name().toLowerCase();
        List<String> punishments = database.getStringList(path);
        punishments.add(punishment.serialize());
        database.set(path, punishments);
        saveData();
    }

    public void removePunishment(UUID uuid, PunishmentType type) {
        String activePath = uuid.toString() + ".active." + type.name().toLowerCase();
        List<String> activePunishments = database.getStringList(activePath);
        if (!activePunishments.isEmpty()) {
            String inactivePath = uuid.toString() + ".inactive";
            List<String> inactivePunishments = database.getStringList(inactivePath);
            inactivePunishments.addAll(activePunishments);
            database.set(inactivePath, inactivePunishments);
            database.set(activePath, null);
            saveData();
        }
    }

    public boolean hasPunishment(UUID uuid, PunishmentType type) {
        String path = uuid.toString() + ".active." + type.name().toLowerCase();
        List<String> punishments = database.getStringList(path);
        return !punishments.isEmpty();
    }

    public Punishment getActivePunishment(UUID uuid, PunishmentType type) {
        String path = uuid.toString() + ".active." + type.name().toLowerCase();
        List<String> punishments = database.getStringList(path);
        if (!punishments.isEmpty()) {
            return Punishment.deserialize(punishments.get(punishments.size() - 1));
        }
        return null;
    }

    public List<Punishment> getPunishmentHistory(UUID uuid) {
        List<Punishment> history = new ArrayList<>();
        String activePath = uuid.toString() + ".active";
        String inactivePath = uuid.toString() + ".inactive";

        for (String type : database.getConfigurationSection(activePath).getKeys(false)) {
            List<String> punishments = database.getStringList(activePath + "." + type);
            for (String punishmentData : punishments) {
                history.add(Punishment.deserialize(punishmentData));
            }
        }

        List<String> inactivePunishments = database.getStringList(inactivePath);
        for (String punishmentData : inactivePunishments) {
            history.add(Punishment.deserialize(punishmentData));
        }

        history.sort(Comparator.comparing(Punishment::getDate).reversed());
        return history;
    }

    public void addIPBan(String ip, Punishment punishment) {
        String path = "ip_bans." + ip;
        database.set(path, punishment.serialize());
        saveData();
    }

    public void removeIPBan(String ip) {
        String path = "ip_bans." + ip;
        database.set(path, null);
        saveData();
    }

    public boolean isIPBanned(String ip) {
        return database.contains("ip_bans." + ip);
    }

    public Punishment getIPBan(String ip) {
        String path = "ip_bans." + ip;
        String punishmentData = database.getString(path);
        return punishmentData != null ? Punishment.deserialize(punishmentData) : null;
    }
}