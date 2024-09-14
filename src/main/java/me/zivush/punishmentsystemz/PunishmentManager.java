package me.zivush.punishmentsystemz;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public class PunishmentManager {
    private final PunishmentSystemZ plugin;

    public PunishmentManager(PunishmentSystemZ plugin) {
        this.plugin = plugin;
    }

    public void ban(UUID targetUUID, String reason, String duration, String punisher) {
        Punishment punishment = new Punishment(PunishmentType.BAN, targetUUID, reason, duration, punisher, new Date());
        plugin.getDatabaseManager().addPunishment(targetUUID, punishment);
        Player target = Bukkit.getPlayer(targetUUID);
        if (target != null && target.isOnline()) {
            target.kickPlayer(formatBanMessage(punishment));
        }
    }

    public void unban(UUID targetUUID) {
        plugin.getDatabaseManager().removePunishment(targetUUID, PunishmentType.BAN);
    }

    public void mute(UUID targetUUID, String reason, String duration, String punisher) {
        Punishment punishment = new Punishment(PunishmentType.MUTE, targetUUID, reason, duration, punisher, new Date());
        plugin.getDatabaseManager().addPunishment(targetUUID, punishment);
    }

    public void unmute(UUID targetUUID) {
        plugin.getDatabaseManager().removePunishment(targetUUID, PunishmentType.MUTE);
    }

    public void kick(Player target, String reason, String punisher) {
        Punishment punishment = new Punishment(PunishmentType.KICK, target.getUniqueId(), reason, "0", punisher, new Date());
        plugin.getDatabaseManager().addPunishment(target.getUniqueId(), punishment);
        target.kickPlayer(formatKickMessage(punishment));
    }

    public void blacklist(UUID targetUUID, String reason, String punisher) {
        Punishment punishment = new Punishment(PunishmentType.BLACKLIST, targetUUID, reason, "0", punisher, new Date());
        plugin.getDatabaseManager().addPunishment(targetUUID, punishment);
        Player target = Bukkit.getPlayer(targetUUID);
        if (target != null && target.isOnline()) {
            target.kickPlayer(formatBlacklistMessage(punishment));
        }
    }

    public void banIP(String ip, String reason, String duration, String punisher) {
        Punishment punishment = new Punishment(PunishmentType.BANIP, null, reason, duration, punisher, new Date());
        plugin.getDatabaseManager().addIPBan(ip, punishment);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getAddress().getAddress().getHostAddress().equals(ip)) {
                player.kickPlayer(formatBanIPMessage(punishment));
            }
        }
    }

    public void unbanIP(String ip) {
        plugin.getDatabaseManager().removeIPBan(ip);
    }

    public boolean isBanned(UUID uuid) {
        return plugin.getDatabaseManager().hasPunishment(uuid, PunishmentType.BAN);
    }

    public boolean isMuted(UUID uuid) {
        return plugin.getDatabaseManager().hasPunishment(uuid, PunishmentType.MUTE);
    }

    public boolean isBlacklisted(UUID uuid) {
        return plugin.getDatabaseManager().hasPunishment(uuid, PunishmentType.BLACKLIST);
    }

    public boolean isIPBanned(String ip) {
        return plugin.getDatabaseManager().isIPBanned(ip);
    }

    public String formatBanMessage(Punishment punishment) {
        return Utils.color(plugin.getConfig().getString("messages.ban-message")
                .replace("{reason}", punishment.getReason())
                .replace("{time_left}", Utils.formatTimeLeft(punishment.getExpirationDate()))
                .replace("{punisher}", punishment.getPunisher()));
    }

    public String formatMuteMessage(Punishment punishment) {
        return Utils.color(plugin.getConfig().getString("messages.mute-message")
                .replace("{reason}", punishment.getReason())
                .replace("{time_left}", Utils.formatTimeLeft(punishment.getExpirationDate()))
                .replace("{punisher}", punishment.getPunisher()));
    }

    public String formatKickMessage(Punishment punishment) {
        return Utils.color(plugin.getConfig().getString("messages.kick-message")
                .replace("{reason}", punishment.getReason())
                .replace("{punisher}", punishment.getPunisher()));
    }

    public String formatBlacklistMessage(Punishment punishment) {
        return Utils.color(plugin.getConfig().getString("messages.blacklist-message")
                .replace("{reason}", punishment.getReason())
                .replace("{punisher}", punishment.getPunisher()));
    }

    public String formatBanIPMessage(Punishment punishment) {
        return Utils.color(plugin.getConfig().getString("messages.banip-message")
                .replace("{reason}", punishment.getReason())
                .replace("{time_left}", Utils.formatTimeLeft(punishment.getExpirationDate()))
                .replace("{punisher}", punishment.getPunisher()));
    }
}