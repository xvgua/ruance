package com.healthassistant.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReminderManagerTest {

    private ReminderManager mgr;

    @BeforeEach
    void setUp() {
        mgr = new ReminderManager();
    }

    @Test
    void testAddReminder() {
        Reminder r = mgr.add("阿司匹林", "100mg", 8, 0);
        assertEquals("阿司匹林", r.getName());
        assertEquals("100mg", r.getDosage());
        assertEquals(8, r.getHour());
        assertEquals(0, r.getMinute());
        assertEquals("08:00", r.getTimeStr());
    }

    @Test
    void testAddMultipleReminders() {
        mgr.add("药A", "1片", 8, 0);
        mgr.add("药B", "2片", 12, 30);
        mgr.add("药C", "3片", 18, 0);
        assertEquals(3, mgr.getAll().size());
    }

    @Test
    void testUniqueIds() {
        Reminder r1 = mgr.add("药A", "1片", 8, 0);
        Reminder r2 = mgr.add("药B", "2片", 12, 0);
        assertNotEquals(r1.getId(), r2.getId());
    }

    @Test
    void testRemoveExistingReminder() {
        Reminder r = mgr.add("药A", "1片", 8, 0);
        assertTrue(mgr.remove(r.getId()));
        assertEquals(0, mgr.getAll().size());
    }

    @Test
    void testRemoveNonExisting() {
        assertFalse(mgr.remove(999));
    }

    @Test
    void testGetAllReturnsUnmodifiable() {
        mgr.add("药A", "1片", 8, 0);
        List<Reminder> reminders = mgr.getAll();
        assertThrows(UnsupportedOperationException.class, () ->
                reminders.add(new Reminder(99, "test", "1片", 8, 0)));
    }

    @Test
    void testEmptyNameRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                mgr.add("", "1片", 8, 0));
    }

    @Test
    void testWhitespaceNameRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                mgr.add("   ", "1片", 8, 0));
    }

    @Test
    void testHourOutOfRangeLow() {
        assertThrows(IllegalArgumentException.class, () ->
                mgr.add("药A", "1片", -1, 0));
    }

    @Test
    void testHourOutOfRangeHigh() {
        assertThrows(IllegalArgumentException.class, () ->
                mgr.add("药A", "1片", 24, 0));
    }

    @Test
    void testMinuteOutOfRangeLow() {
        assertThrows(IllegalArgumentException.class, () ->
                mgr.add("药A", "1片", 8, -1));
    }

    @Test
    void testMinuteOutOfRangeHigh() {
        assertThrows(IllegalArgumentException.class, () ->
                mgr.add("药A", "1片", 8, 60));
    }

    @Test
    void testTimeStrFormat() {
        Reminder r = mgr.add("药A", "1片", 8, 5);
        assertEquals("08:05", r.getTimeStr());
        Reminder r2 = mgr.add("药B", "2片", 23, 59);
        assertEquals("23:59", r2.getTimeStr());
    }

    @Test
    void testCheckNowMatchesCurrentTime() {
        LocalTime now = LocalTime.now();
        Reminder r = mgr.add("药A", "1片", now.getHour(), now.getMinute());
        List<Reminder> due = mgr.checkNow();
        assertTrue(due.stream().anyMatch(d -> d.getId() == r.getId()));
    }

    @Test
    void testCheckNowNoMatch() {
        LocalTime now = LocalTime.now();
        int minute = (now.getMinute() + 1) % 60;
        int hour = minute > now.getMinute() ? now.getHour() : (now.getHour() + 1) % 24;
        mgr.add("药A", "1片", hour, minute);
        List<Reminder> due = mgr.checkNow();
        assertEquals(0, due.size());
    }

    @Test
    void testNameStripped() {
        Reminder r = mgr.add("  药A  ", "1片", 8, 0);
        assertEquals("药A", r.getName());
    }

    @Test
    void testNameTooLongRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                mgr.add("A".repeat(51), "1片", 8, 0));
    }

    @Test
    void testNameMaxLengthOk() {
        Reminder r = mgr.add("A".repeat(50), "1片", 8, 0);
        assertEquals(50, r.getName().length());
    }

    @Test
    void testDosageTooLongRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                mgr.add("药A", "B".repeat(31), 8, 0));
    }
}
