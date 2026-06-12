package com.healthassistant.logic;

import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SleepEvaluatorTest {

    @Test
    void testSufficientSleep() {
        Map<String, Object> result = SleepEvaluator.evaluate(22, 0, 6, 0, 4);
        assertEquals(SleepEvaluator.LEVEL_SUFFICIENT, result.get("level"));
    }

    @Test
    void testSufficientBoundaryDuration() {
        Map<String, Object> result = SleepEvaluator.evaluate(22, 0, 5, 0, 4);
        assertEquals(7, result.get("durationHours"));
        assertEquals(SleepEvaluator.LEVEL_SUFFICIENT, result.get("level"));
    }

    @Test
    void testSufficientBoundaryScore() {
        Map<String, Object> result = SleepEvaluator.evaluate(22, 0, 6, 0, 4);
        assertEquals(SleepEvaluator.LEVEL_SUFFICIENT, result.get("level"));
    }

    @Test
    void testNormalSleepDuration() {
        Map<String, Object> result = SleepEvaluator.evaluate(23, 0, 6, 0, 3);
        assertEquals(7, result.get("durationHours"));
        assertEquals(SleepEvaluator.LEVEL_NORMAL, result.get("level"));
    }

    @Test
    void testNormalBoundaryDuration() {
        Map<String, Object> result = SleepEvaluator.evaluate(23, 0, 6, 0, 3);
        assertEquals(7, result.get("durationHours"));
        assertEquals(SleepEvaluator.LEVEL_NORMAL, result.get("level"));
    }

    @Test
    void testInsufficientSleep() {
        Map<String, Object> result = SleepEvaluator.evaluate(0, 0, 5, 0, 2);
        assertEquals(5, result.get("durationHours"));
        assertEquals(SleepEvaluator.LEVEL_INSUFFICIENT, result.get("level"));
    }

    @Test
    void testOvernightSleep() {
        Map<String, Object> result = SleepEvaluator.evaluate(23, 30, 6, 30, 4);
        assertEquals(7, result.get("durationHours"));
        assertEquals(0, result.get("durationMinutes"));
    }

    @Test
    void testOvernightMidnight() {
        Map<String, Object> result = SleepEvaluator.evaluate(0, 0, 7, 0, 4);
        assertEquals(7, result.get("durationHours"));
    }

    @Test
    void testShortSleepLowScore() {
        Map<String, Object> result = SleepEvaluator.evaluate(1, 0, 5, 0, 2);
        assertEquals(SleepEvaluator.LEVEL_INSUFFICIENT, result.get("level"));
    }

    @Test
    void testResultContainsAllFields() {
        Map<String, Object> result = SleepEvaluator.evaluate(22, 0, 6, 0, 3);
        assertTrue(result.containsKey("sleepTime"));
        assertTrue(result.containsKey("wakeTime"));
        assertTrue(result.containsKey("durationHours"));
        assertTrue(result.containsKey("durationMinutes"));
        assertTrue(result.containsKey("selfScore"));
        assertTrue(result.containsKey("level"));
        assertTrue(result.containsKey("suggestion"));
    }

    @Test
    void testTimeFormat() {
        Map<String, Object> result = SleepEvaluator.evaluate(9, 5, 17, 15, 3);
        assertEquals("09:05", result.get("sleepTime"));
        assertEquals("17:15", result.get("wakeTime"));
    }

    @Test
    void testInvalidSleepHourRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                SleepEvaluator.evaluate(-1, 0, 6, 0, 3));
    }

    @Test
    void testInvalidSleepHourHigh() {
        assertThrows(IllegalArgumentException.class, () ->
                SleepEvaluator.evaluate(24, 0, 6, 0, 3));
    }

    @Test
    void testInvalidSleepMinuteRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                SleepEvaluator.evaluate(22, 60, 6, 0, 3));
    }

    @Test
    void testInvalidWakeHourRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                SleepEvaluator.evaluate(22, 0, 24, 0, 3));
    }

    @Test
    void testInvalidWakeMinuteRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                SleepEvaluator.evaluate(22, 0, 6, 60, 3));
    }

    @Test
    void testInvalidScoreLow() {
        assertThrows(IllegalArgumentException.class, () ->
                SleepEvaluator.evaluate(22, 0, 6, 0, 0));
    }

    @Test
    void testInvalidScoreHigh() {
        assertThrows(IllegalArgumentException.class, () ->
                SleepEvaluator.evaluate(22, 0, 6, 0, 6));
    }

    @Test
    void testSufficientRequiresGoodScore() {
        Map<String, Object> result = SleepEvaluator.evaluate(22, 0, 6, 0, 3);
        assertEquals(SleepEvaluator.LEVEL_NORMAL, result.get("level"));
    }

    @Test
    void testEquivalencePartitioning() {
        assertEquals(SleepEvaluator.LEVEL_SUFFICIENT,
                SleepEvaluator.evaluate(22, 30, 6, 30, 5).get("level"));
        assertEquals(SleepEvaluator.LEVEL_NORMAL,
                SleepEvaluator.evaluate(22, 30, 5, 30, 3).get("level"));
        assertEquals(SleepEvaluator.LEVEL_INSUFFICIENT,
                SleepEvaluator.evaluate(2, 0, 6, 0, 2).get("level"));
    }
}
