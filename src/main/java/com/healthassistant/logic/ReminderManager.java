package com.healthassistant.logic;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ReminderManager {
    private final List<Reminder> reminders;
    private int nextId;

    public ReminderManager() {
        this.reminders = new ArrayList<>();
        this.nextId = 1;
    }

    public Reminder add(String name, String dosage, int hour, int minute) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("小时必须在 0-23 之间");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("分钟必须在 0-59 之间");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("药品名称不能为空");
        }
        if (name.trim().length() > 50) {
            throw new IllegalArgumentException("药品名称不能超过50个字符");
        }
        if (dosage != null && dosage.trim().length() > 30) {
            throw new IllegalArgumentException("剂量不能超过30个字符");
        }

        Reminder reminder = new Reminder(nextId, name.trim(),
                dosage != null ? dosage.trim() : "", hour, minute);
        reminders.add(reminder);
        nextId++;
        return reminder;
    }

    public boolean remove(int reminderId) {
        Iterator<Reminder> it = reminders.iterator();
        while (it.hasNext()) {
            if (it.next().getId() == reminderId) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public List<Reminder> getAll() {
        return Collections.unmodifiableList(reminders);
    }

    public List<Reminder> checkNow() {
        LocalTime now = LocalTime.now();
        int h = now.getHour();
        int m = now.getMinute();

        List<Reminder> due = new ArrayList<>();
        for (Reminder r : reminders) {
            if (r.getHour() == h && r.getMinute() == m) {
                due.add(r);
            }
        }
        return due;
    }
}
