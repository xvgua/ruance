package com.healthassistant.logic;

import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BgEvaluatorTest {

    @Test
    void testNormalFasting() {
        Map<String, Object> result = BgEvaluator.evaluate(5.0, BgEvaluator.PERIOD_FASTING);
        assertEquals(BgEvaluator.LEVEL_NORMAL, result.get("level"));
    }

    @Test
    void testNormalFastingBoundaryLow() {
        Map<String, Object> result = BgEvaluator.evaluate(3.9, BgEvaluator.PERIOD_FASTING);
        assertEquals(BgEvaluator.LEVEL_NORMAL, result.get("level"));
    }

    @Test
    void testNormalFastingBoundaryHigh() {
        Map<String, Object> result = BgEvaluator.evaluate(6.1, BgEvaluator.PERIOD_FASTING);
        assertEquals(BgEvaluator.LEVEL_NORMAL, result.get("level"));
    }

    @Test
    void testHighFasting() {
        Map<String, Object> result = BgEvaluator.evaluate(6.5, BgEvaluator.PERIOD_FASTING);
        assertEquals(BgEvaluator.LEVEL_HIGH, result.get("level"));
    }

    @Test
    void testHighFastingBoundaryHigh() {
        Map<String, Object> result = BgEvaluator.evaluate(7.0, BgEvaluator.PERIOD_FASTING);
        assertEquals(BgEvaluator.LEVEL_HIGH, result.get("level"));
    }

    @Test
    void testDiabetesFasting() {
        Map<String, Object> result = BgEvaluator.evaluate(7.5, BgEvaluator.PERIOD_FASTING);
        assertEquals(BgEvaluator.LEVEL_DIABETES, result.get("level"));
    }

    @Test
    void testNormalPostMeal() {
        Map<String, Object> result = BgEvaluator.evaluate(6.0, BgEvaluator.PERIOD_POST_MEAL);
        assertEquals(BgEvaluator.LEVEL_NORMAL, result.get("level"));
    }

    @Test
    void testNormalPostMealBoundary() {
        Map<String, Object> result = BgEvaluator.evaluate(7.8, BgEvaluator.PERIOD_POST_MEAL);
        assertEquals(BgEvaluator.LEVEL_NORMAL, result.get("level"));
    }

    @Test
    void testHighPostMeal() {
        Map<String, Object> result = BgEvaluator.evaluate(9.0, BgEvaluator.PERIOD_POST_MEAL);
        assertEquals(BgEvaluator.LEVEL_HIGH, result.get("level"));
    }

    @Test
    void testHighPostMealBoundaryHigh() {
        Map<String, Object> result = BgEvaluator.evaluate(11.1, BgEvaluator.PERIOD_POST_MEAL);
        assertEquals(BgEvaluator.LEVEL_HIGH, result.get("level"));
    }

    @Test
    void testDiabetesPostMeal() {
        Map<String, Object> result = BgEvaluator.evaluate(12.0, BgEvaluator.PERIOD_POST_MEAL);
        assertEquals(BgEvaluator.LEVEL_DIABETES, result.get("level"));
    }

    @Test
    void testInvalidPeriodRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                BgEvaluator.evaluate(5.0, "随机"));
    }

    @Test
    void testNegativeValueRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                BgEvaluator.evaluate(-0.5, BgEvaluator.PERIOD_FASTING));
    }

    @Test
    void testZeroValueRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                BgEvaluator.evaluate(0, BgEvaluator.PERIOD_FASTING));
    }

    @Test
    void testLowBloodSugarFasting() {
        Map<String, Object> result = BgEvaluator.evaluate(2.5, BgEvaluator.PERIOD_FASTING);
        assertEquals(BgEvaluator.LEVEL_LOW, result.get("level"));
    }

    @Test
    void testLowBloodSugarPostMeal() {
        Map<String, Object> result = BgEvaluator.evaluate(3.0, BgEvaluator.PERIOD_POST_MEAL);
        assertEquals(BgEvaluator.LEVEL_LOW, result.get("level"));
    }

    @Test
    void testExcessiveValueRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                BgEvaluator.evaluate(50, BgEvaluator.PERIOD_FASTING));
    }

    @Test
    void testResultContainsFields() {
        Map<String, Object> result = BgEvaluator.evaluate(5.5, BgEvaluator.PERIOD_FASTING);
        assertTrue(result.containsKey("value"));
        assertTrue(result.containsKey("period"));
        assertTrue(result.containsKey("level"));
        assertTrue(result.containsKey("suggestion"));
    }

    @Test
    void testFastingNormalRangeEquivalence() {
        for (double val : new double[]{4.0, 4.5, 5.0, 5.5, 6.0}) {
            Map<String, Object> result = BgEvaluator.evaluate(val, BgEvaluator.PERIOD_FASTING);
            assertEquals(BgEvaluator.LEVEL_NORMAL, result.get("level"),
                    "value=" + val);
        }
    }

    @Test
    void testPostMealNormalRangeEquivalence() {
        for (double val : new double[]{4.0, 5.0, 6.0, 7.0}) {
            Map<String, Object> result = BgEvaluator.evaluate(val, BgEvaluator.PERIOD_POST_MEAL);
            assertEquals(BgEvaluator.LEVEL_NORMAL, result.get("level"),
                    "value=" + val);
        }
    }
}
