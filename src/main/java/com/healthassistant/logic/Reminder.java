package com.healthassistant.logic;

import java.util.Objects;

public class Reminder {
    private final int id;
    private final String name;
    private final String dosage;
    private final int hour;
    private final int minute;
    private final String timeStr;

    public Reminder(int id, String name, String dosage, int hour, int minute) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.hour = hour;
        this.minute = minute;
        this.timeStr = String.format("%02d:%02d", hour, minute);
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getDosage() { return dosage; }

    public int getHour() { return hour; }

    public int getMinute() { return minute; }

    public String getTimeStr() { return timeStr; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reminder reminder)) return false;
        return id == reminder.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Reminder{id=%d, name='%s', dosage='%s', time='%s'}", id, name, dosage, timeStr);
    }
}
