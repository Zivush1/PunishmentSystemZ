package me.zivush.punishmentsystemz;

import java.util.Date;
import java.util.UUID;

public class Punishment {
    private final PunishmentType type;
    private final UUID targetUUID;
    private final String reason;
    private final String duration;
    private final String punisher;
    private final Date date;

    public Punishment(PunishmentType type, UUID targetUUID, String reason, String duration, String punisher, Date date) {
        this.type = type;
        this.targetUUID = targetUUID;
        this.reason = reason;
        this.duration = duration;
        this.punisher = punisher;
        this.date = date;
    }

    public PunishmentType getType() {
        return type;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public String getReason() {
        return reason;
    }

    public String getDuration() {
        return duration;
    }

    public String getPunisher() {
        return punisher;
    }

    public Date getDate() {
        return date;
    }

    public boolean isExpired() {
        if (duration.equals("0")) {
            return false;
        }
        long durationMillis = Utils.parseDuration(duration);
        return System.currentTimeMillis() > (date.getTime() + durationMillis);
    }

    public Date getExpirationDate() {
        if (duration.equals("0")) {
            return null;
        }
        long durationMillis = Utils.parseDuration(duration);
        return new Date(date.getTime() + durationMillis);
    }

    public String serialize() {
        return type + "," + targetUUID + "," + reason + "," + duration + "," + punisher + "," + date.getTime();
    }

    public static Punishment deserialize(String data) {
        String[] parts = data.split(",");
        PunishmentType type = PunishmentType.valueOf(parts[0]);
        UUID targetUUID = UUID.fromString(parts[1]);
        String reason = parts[2];
        String duration = parts[3];
        String punisher = parts[4];
        Date date = new Date(Long.parseLong(parts[5]));
        return new Punishment(type, targetUUID, reason, duration, punisher, date);
    }
}

