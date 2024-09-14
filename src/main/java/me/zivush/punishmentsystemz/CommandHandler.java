package me.zivush.punishmentsystemz;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class CommandHandler implements CommandExecutor {
    private final PunishmentSystemZ plugin;

    public CommandHandler(PunishmentSystemZ plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("punishmentsystemz." + command.getName())) {
            sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }

        switch (command.getName().toLowerCase()) {
            case "ban":
                return handleBan(sender, args);
            case "unban":
                return handleUnban(sender, args);
            case "kick":
                return handleKick(sender, args);
            case "mute":
                return handleMute(sender, args);
            case "unmute":
                return handleUnmute(sender, args);
            case "blacklist":
                return handleBlacklist(sender, args);
            case "banip":
                return handleBanIP(sender, args);
            case "unbanip":
                return handleUnbanIP(sender, args);
            case "history":
                return handleHistory(sender, args);
            default:
                return false;
        }
    }

    private boolean handleBan(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(Utils.color("&cUsage: /ban <player> <duration> <reason>"));
            return true;
        }

        String playerName = args[0];
        String duration = args[1];
        String reason = String.join(" ", args).substring(playerName.length() + duration.length() + 2);

        UUID targetUUID = Utils.getUUID(playerName);
        if (targetUUID == null) {
            sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.player-not-found")));
            return true;
        }

        plugin.getPunishmentManager().ban(targetUUID, reason, duration, sender.getName());
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.punishment-success")));
        return true;
    }

    private boolean handleUnban(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(Utils.color("&cUsage: /unban <player>"));
            return true;
        }

        String playerName = args[0];
        UUID targetUUID = Utils.getUUID(playerName);
        if (targetUUID == null) {
            sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.player-not-found")));
            return true;
        }

        plugin.getPunishmentManager().unban(targetUUID);
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.punishment-removed")));
        return true;
    }

    private boolean handleKick(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Utils.color("&cUsage: /kick <player> <reason>"));
            return true;
        }

        String playerName = args[0];
        String reason = String.join(" ", args).substring(playerName.length() + 1);

        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.player-not-found")));
            return true;
        }

        plugin.getPunishmentManager().kick(target, reason, sender.getName());
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.punishment-success")));
        return true;
    }

    private boolean handleMute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(Utils.color("&cUsage: /mute <player> <duration> <reason>"));
            return true;
        }

        String playerName = args[0];
        String duration = args[1];
        String reason = String.join(" ", args).substring(playerName.length() + duration.length() + 2);

        UUID targetUUID = Utils.getUUID(playerName);
        if (targetUUID == null) {
            sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.player-not-found")));
            return true;
        }

        plugin.getPunishmentManager().mute(targetUUID, reason, duration, sender.getName());
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.punishment-success")));
        return true;
    }

    private boolean handleUnmute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(Utils.color("&cUsage: /unmute <player>"));
            return true;
        }

        String playerName = args[0];
        UUID targetUUID = Utils.getUUID(playerName);
        if (targetUUID == null) {
            sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.player-not-found")));
            return true;
        }

        plugin.getPunishmentManager().unmute(targetUUID);
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.punishment-removed")));
        return true;
    }

    private boolean handleBlacklist(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Utils.color("&cUsage: /blacklist <player> <reason>"));
            return true;
        }

        String playerName = args[0];
        String reason = String.join(" ", args).substring(playerName.length() + 1);

        UUID targetUUID = Utils.getUUID(playerName);
        if (targetUUID == null) {
            sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.player-not-found")));
            return true;
        }

        plugin.getPunishmentManager().blacklist(targetUUID, reason, sender.getName());
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.punishment-success")));
        return true;
    }

    private boolean handleBanIP(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(Utils.color("&cUsage: /banip <ip/player> <duration> <reason>"));
            return true;
        }

        String ipOrPlayer = args[0];
        String duration = args[1];
        String reason = String.join(" ", args).substring(ipOrPlayer.length() + duration.length() + 2);

        String ip;
        if (Utils.isValidIP(ipOrPlayer)) {
            ip = ipOrPlayer;
        } else {
            Player target = Bukkit.getPlayer(ipOrPlayer);
            if (target == null) {
                sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.player-not-found")));
                return true;
            }
            ip = target.getAddress().getAddress().getHostAddress();
        }

        plugin.getPunishmentManager().banIP(ip, reason, duration, sender.getName());
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.punishment-success")));
        return true;
    }

    private boolean handleUnbanIP(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(Utils.color("&cUsage: /unbanip <ip>"));
            return true;
        }

        String ip = args[0];
        if (!Utils.isValidIP(ip)) {
            sender.sendMessage(Utils.color("&cInvalid IP address format!"));
            return true;
        }

        plugin.getPunishmentManager().unbanIP(ip);
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.punishment-removed")));
        return true;
    }

    private boolean handleHistory(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(Utils.color("&cUsage: /history <player>"));
            return true;
        }

        String playerName = args[0];
        UUID targetUUID = Utils.getUUID(playerName);
        if (targetUUID == null) {
            sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.player-not-found")));
            return true;
        }

        List<Punishment> history = plugin.getDatabaseManager().getPunishmentHistory(targetUUID);
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.history-header").replace("{player}", playerName)));
        for (Punishment punishment : history) {
            sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.history-entry")
                    .replace("{date}", Utils.formatDate(punishment.getDate()))
                    .replace("{action}", punishment.getType().toString())
                    .replace("{duration}", Utils.formatDuration(punishment.getDuration()))
                    .replace("{reason}", punishment.getReason())
                    .replace("{punisher}", punishment.getPunisher())));
        }
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.history-footer")));
        return true;
    }
}

