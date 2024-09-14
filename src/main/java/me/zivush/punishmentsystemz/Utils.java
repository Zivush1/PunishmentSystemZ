package me.zivush.punishmentsystemz;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

public class Utils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Pattern IP_PATTERN = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    private static PunishmentSystemZ plugin;

    public static void setPlugin(PunishmentSystemZ pl) {
        plugin = pl;
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static UUID getUUID(String playerName) {
        return Bukkit.getOfflinePlayer(playerName).getUniqueId();
    }

    public static boolean isValidIP(String ip) {
        return IP_PATTERN.matcher(ip).matches();
    }

    public static long parseDuration(String duration) {
        long totalMillis = 0;
        String[] parts = duration.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        for (int i = 0; i < parts.length; i += 2) {
            int value = Integer.parseInt(parts[i]);
            String unit = parts[i + 1].toLowerCase();
            switch (unit) {
                case "s":
                    totalMillis += value * 1000L;
                    break;
                case "m":
                    totalMillis += value * 60 * 1000L;
                    break;
                case "h":
                    totalMillis += value * 60 * 60 * 1000L;
                    break;
                case "d":
                    totalMillis += value * 24 * 60 * 60 * 1000L;
                    break;
                case "w":
                    totalMillis += value * 7 * 24 * 60 * 60 * 1000L;
                    break;
                case "mo":
                    totalMillis += value * 30 * 24 * 60 * 60 * 1000L;
                    break;
                case "y":
                    totalMillis += value * 365 * 24 * 60 * 60 * 1000L;
                    break;
            }
        }
        return totalMillis;
    }

    public static String formatDuration(String duration) {
        if (duration.equals("0")) {
            return "Permanent";
        }
        return duration;
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static String formatTimeLeft(Date expirationDate) {
        if (expirationDate == null) {
            return "Permanent";
        }
        long timeLeft = expirationDate.getTime() - System.currentTimeMillis();
        if (timeLeft <= 0) {
            return "Expired";
        }
        return formatTime(timeLeft);
    }

    private static String formatTime(long millis) {
        FileConfiguration config = plugin.getConfig();
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long months = days / 30;
        long years = days / 365;

        StringBuilder sb = new StringBuilder();

        if (years > 0) {
            sb.append(years).append(" ").append(years == 1 ? config.getString("time-format.year") : config.getString("time-format.years")).append(", ");
            months %= 12;
        }
        if (months > 0) {
            sb.append(months).append(" ").append(months == 1 ? config.getString("time-format.month") : config.getString("time-format.months")).append(", ");
            weeks %= 4;
        }
        if (weeks > 0) {
            sb.append(weeks).append(" ").append(weeks == 1 ? config.getString("time-format.week") : config.getString("time-format.weeks")).append(", ");
            days %= 7;
        }
        if (days > 0) {
            sb.append(days).append(" ").append(days == 1 ? config.getString("time-format.day") : config.getString("time-format.days")).append(", ");
        }
        if (hours % 24 > 0) {
            sb.append(hours % 24).append(" ").append(hours % 24 == 1 ? config.getString("time-format.hour") : config.getString("time-format.hours")).append(", ");
        }
        if (minutes % 60 > 0) {
            sb.append(minutes % 60).append(" ").append(minutes % 60 == 1 ? config.getString("time-format.minute") : config.getString("time-format.minutes")).append(", ");
        }
        if (seconds % 60 > 0 || sb.length() == 0) {
            sb.append(seconds % 60).append(" ").append(seconds % 60 == 1 ? config.getString("time-format.second") : config.getString("time-format.seconds"));
        } else {
            sb.setLength(sb.length() - 2);
        }

        return sb.toString();
    }
}