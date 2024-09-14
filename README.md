# PunishmentSystemZ

PunishmentSystemZ is a comprehensive punishment management system for Bukkit/Spigot Minecraft servers. It provides a range of punishment options including bans, mutes, kicks, blacklists, and IP bans.

## Features

- Multiple punishment types: ban, mute, kick, blacklist, and IP ban
- Temporary and permanent punishments
- Offline player punishments
- Customizable messages
- Punishment history tracking
- Easy-to-use commands
- YAML-based configuration and data storage

## Commands

- `/ban <player> <duration> <reason>` - Ban a player
- `/unban <player>` - Unban a player
- `/mute <player> <duration> <reason>` - Mute a player
- `/unmute <player>` - Unmute a player
- `/kick <player> <reason>` - Kick a player
- `/blacklist <player> <reason>` - Blacklist a player
- `/banip <ip/player> <duration> <reason>` - Ban an IP address
- `/unbanip <ip>` - Unban an IP address
- `/history <player>` - View a player's punishment history

## Permissions

- `punishmentsystemz.ban` - Allow banning players
- `punishmentsystemz.unban` - Allow unbanning players
- `punishmentsystemz.mute` - Allow muting players
- `punishmentsystemz.unmute` - Allow unmuting players
- `punishmentsystemz.kick` - Allow kicking players
- `punishmentsystemz.blacklist` - Allow blacklisting players
- `punishmentsystemz.banip` - Allow banning IP addresses
- `punishmentsystemz.unbanip` - Allow unbanning IP addresses
- `punishmentsystemz.history` - Allow viewing punishment history

## Installation

1. Download the PunishmentSystemZ.jar file
2. Place the jar file in your server's `plugins` folder
3. Restart your server or run `/reload confirm`
4. Edit the `config.yml` file in the `plugins/PunishmentSystemZ` folder to customize messages and settings
5. Use `/reload confirm` to apply changes or restart your server

## Configuration

The `config.yml` file allows you to customize various messages and time format labels. You can edit this file to change how messages are displayed to players and staff.
