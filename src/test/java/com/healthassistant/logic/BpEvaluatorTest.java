package com.healthassistant.logic;

import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BpEvaluatorTest {

    @Test
    void testNormalBp() {
        Map<String, Object> result = BpEvaluator.evaluate(110, 70);
        assertEquals(BpEvaluator.LEVEL_NORMAL, result.get("level"));
        assertEquals(110.0, result.get("systolic"));
        assertEquals(70.0, result.get("diastolic"));
    }

    @Test
    void testNormalBoundarySystolic() {
        Map<String, Object> result = BpEvaluator.evaluate(119, 79);
        assertEquals(BpEvaluator.LEVEL_NORMAL, result.get("level"));
    }

    @Test
    void testHighNormalSystolic() {
        Map<String, Object> result = BpEvaluator.evaluate(130, 80);
        assertEquals(BpEvaluator.LEVEL_HIGH, result.get("level"));
    }

    @Test
    void testHighNormalDiastolic() {
        Map<String, Object> result = BpEvaluator.evaluate(115, 85);
        assertEquals(BpEvaluator.LEVEL_HIGH, result.get("level"));
    }

    @Test
    void testHighNormalBoundarySystolic() {
        Map<String, Object> result = BpEvaluator.evaluate(139, 89);
        assertEquals(BpEvaluator.LEVEL_HIGH, result.get("level"));
    }

    @Test
    void testHypertensionSystolic() {
        Map<String, Object> result = BpEvaluator.evaluate(140, 85);
        assertEquals(BpEvaluator.LEVEL_HYPERTENSION, result.get("level"));
    }

    @Test
    void testHypertensionDiastolic() {
        Map<String, Object> result = BpEvaluator.evaluate(130, 90);
        assertEquals(BpEvaluator.LEVEL_HYPERTENSION, result.get("level"));
    }

    @Test
    void testHypertensionBoth() {
        Map<String, Object> result = BpEvaluator.evaluate(160, 100);
        assertEquals(BpEvaluator.LEVEL_HYPERTENSION, result.get("level"));
    }

    @Test
    void testResultContainsSuggestion() {
        Map<String, Object> result = BpEvaluator.evaluate(120, 80);
        assertTrue(result.containsKey("suggestion"));
        assertFalse(((String) result.get("suggestion")).isEmpty());
    }

    @Test
    void testNegativeSystolicRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                BpEvaluator.evaluate(-1, 80));
    }

    @Test
    void testNegativeDiastolicRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                BpEvaluator.evaluate(120, -1));
    }

    @Test
    void testZeroBp() {
        assertThrows(IllegalArgumentException.class, () ->
                BpEvaluator.evaluate(0, 0));
    }

    @Test
    void testExcessiveSystolic() {
        assertThrows(IllegalArgumentException.class, () ->
                BpEvaluator.evaluate(400, 80));
    }

    @Test
    void testExcessiveDiastolic() {
        assertThrows(IllegalArgumentException.class, () ->
                BpEvaluator.evaluate(120, 300));
    }

    @Test
    void testDifferentCombinationsSameLevel() {
        assertEquals(BpEvaluator.LEVEL_NORMAL, BpEvaluator.evaluate(110, 70).get("level"));
        assertEquals(BpEvaluator.LEVEL_HIGH, BpEvaluator.evaluate(125, 82).get("level"));
        assertEquals(BpEvaluator.LEVEL_HYPERTENSION, BpEvaluator.evaluate(150, 95).get("level"));
    }
}
