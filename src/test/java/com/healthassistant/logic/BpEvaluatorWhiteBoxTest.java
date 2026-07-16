package com.healthassistant.logic;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BpEvaluatorWhiteBoxTest {

    @Test
    void coversNormalPathAndStrictUpperBounds() {
        Map<String, Object> normal = BpEvaluator.evaluate(119.9, 79.9);

        assertEquals(BpEvaluator.LEVEL_NORMAL, normal.get("level"));
        assertEquals(119.9, normal.get("systolic"));
        assertEquals(79.9, normal.get("diastolic"));
        assertNotNull(normal.get("suggestion"));
    }

    @Test
    void coversHighPathWhenEitherNormalConditionIsFalse() {
        assertEquals(BpEvaluator.LEVEL_HIGH, BpEvaluator.evaluate(120, 79).get("level"));
        assertEquals(BpEvaluator.LEVEL_HIGH, BpEvaluator.evaluate(119, 80).get("level"));
        assertEquals(BpEvaluator.LEVEL_HIGH, BpEvaluator.evaluate(139.9, 89.9).get("level"));
    }

    @Test
    void coversHypertensionPathAtEitherThreshold() {
        assertEquals(BpEvaluator.LEVEL_HYPERTENSION, BpEvaluator.evaluate(140, 89).get("level"));
        assertEquals(BpEvaluator.LEVEL_HYPERTENSION, BpEvaluator.evaluate(139, 90).get("level"));
        assertEquals(BpEvaluator.LEVEL_HYPERTENSION, BpEvaluator.evaluate(160, 100).get("level"));
    }

    @Test
    void coversNonPositiveGuardConditionForBothOperands() {
        assertThrows(IllegalArgumentException.class, () -> BpEvaluator.evaluate(0, 80));
        assertThrows(IllegalArgumentException.class, () -> BpEvaluator.evaluate(120, 0));
        assertThrows(IllegalArgumentException.class, () -> BpEvaluator.evaluate(-1, -1));
    }

    @Test
    void coversExcessiveGuardConditionForBothOperands() {
        assertThrows(IllegalArgumentException.class, () -> BpEvaluator.evaluate(301, 80));
        assertThrows(IllegalArgumentException.class, () -> BpEvaluator.evaluate(120, 201));
        assertThrows(IllegalArgumentException.class, () -> BpEvaluator.evaluate(301, 201));
    }

    @Test
    void coversMaximumAcceptedValuesBeforeHypertensionResult() {
        Map<String, Object> result = BpEvaluator.evaluate(300, 200);

        assertEquals(BpEvaluator.LEVEL_HYPERTENSION, result.get("level"));
        assertEquals(300.0, result.get("systolic"));
        assertEquals(200.0, result.get("diastolic"));
    }
}
