package me.zivush.punishmentsystemz;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class EventListener implements Listener {
    private final PunishmentSystemZ plugin;

    public EventListener(PunishmentSystemZ plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        String ip = event.getAddress().getHostAddress();
        if (plugin.getPunishmentManager().isIPBanned(ip)) {
            Punishment punishment = plugin.getDatabaseManager().getIPBan(ip);
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, plugin.getPunishmentManager().formatBanIPMessage(punishment));
            return;
        }

        if (plugin.getPunishmentManager().isBlacklisted(event.getPlayer().getUniqueId())) {
            Punishment punishment = plugin.getDatabaseManager().getActivePunishment(event.getPlayer().getUniqueId(), PunishmentType.BLACKLIST);
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, plugin.getPunishmentManager().formatBlacklistMessage(punishment));
            return;
        }

        if (plugin.getPunishmentManager().isBanned(event.getPlayer().getUniqueId())) {
            Punishment punishment = plugin.getDatabaseManager().getActivePunishment(event.getPlayer().getUniqueId(), PunishmentType.BAN);
            if (punishment.isExpired()) {
                plugin.getPunishmentManager().unban(event.getPlayer().getUniqueId());
            } else {
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, plugin.getPunishmentManager().formatBanMessage(punishment));
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (plugin.getPunishmentManager().isMuted(event.getPlayer().getUniqueId())) {
            Punishment punishment = plugin.getDatabaseManager().getActivePunishment(event.getPlayer().getUniqueId(), PunishmentType.MUTE);
            if (punishment.isExpired()) {
                plugin.getPunishmentManager().unmute(event.getPlayer().getUniqueId());
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage(plugin.getPunishmentManager().formatMuteMessage(punishment));
            }
        }
    }
}

