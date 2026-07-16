package com.healthassistant.logic;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BgEvaluatorWhiteBoxTest {

    @Test
    void coversFastingPeriodRangeSelection() {
        assertEquals(BgEvaluator.LEVEL_LOW,
                BgEvaluator.evaluate(3.8, BgEvaluator.PERIOD_FASTING).get("level"));
        assertEquals(BgEvaluator.LEVEL_NORMAL,
                BgEvaluator.evaluate(3.9, BgEvaluator.PERIOD_FASTING).get("level"));
        assertEquals(BgEvaluator.LEVEL_NORMAL,
                BgEvaluator.evaluate(6.1, BgEvaluator.PERIOD_FASTING).get("level"));
        assertEquals(BgEvaluator.LEVEL_HIGH,
                BgEvaluator.evaluate(6.1001, BgEvaluator.PERIOD_FASTING).get("level"));
        assertEquals(BgEvaluator.LEVEL_HIGH,
                BgEvaluator.evaluate(7.0, BgEvaluator.PERIOD_FASTING).get("level"));
        assertEquals(BgEvaluator.LEVEL_DIABETES,
                BgEvaluator.evaluate(7.0001, BgEvaluator.PERIOD_FASTING).get("level"));
    }

    @Test
    void coversPostMealPeriodRangeSelection() {
        assertEquals(BgEvaluator.LEVEL_LOW,
                BgEvaluator.evaluate(3.8, BgEvaluator.PERIOD_POST_MEAL).get("level"));
        assertEquals(BgEvaluator.LEVEL_NORMAL,
                BgEvaluator.evaluate(3.9, BgEvaluator.PERIOD_POST_MEAL).get("level"));
        assertEquals(BgEvaluator.LEVEL_NORMAL,
                BgEvaluator.evaluate(7.8, BgEvaluator.PERIOD_POST_MEAL).get("level"));
        assertEquals(BgEvaluator.LEVEL_HIGH,
                BgEvaluator.evaluate(7.8001, BgEvaluator.PERIOD_POST_MEAL).get("level"));
        assertEquals(BgEvaluator.LEVEL_HIGH,
                BgEvaluator.evaluate(11.1, BgEvaluator.PERIOD_POST_MEAL).get("level"));
        assertEquals(BgEvaluator.LEVEL_DIABETES,
                BgEvaluator.evaluate(11.1001, BgEvaluator.PERIOD_POST_MEAL).get("level"));
    }

    @Test
    void coversDifferentOutcomesForSameValueInDifferentPeriods() {
        assertEquals(BgEvaluator.LEVEL_HIGH,
                BgEvaluator.evaluate(6.2, BgEvaluator.PERIOD_FASTING).get("level"));
        assertEquals(BgEvaluator.LEVEL_NORMAL,
                BgEvaluator.evaluate(6.2, BgEvaluator.PERIOD_POST_MEAL).get("level"));
    }

    @Test
    void coversInvalidValueGuardsBeforePeriodValidation() {
        assertThrows(IllegalArgumentException.class, () -> BgEvaluator.evaluate(0, BgEvaluator.PERIOD_FASTING));
        assertThrows(IllegalArgumentException.class, () -> BgEvaluator.evaluate(-0.1, BgEvaluator.PERIOD_POST_MEAL));
        assertThrows(IllegalArgumentException.class, () -> BgEvaluator.evaluate(35.1, BgEvaluator.PERIOD_FASTING));
    }

    @Test
    void coversInvalidPeriodGuardForNonMatchingAndNullPeriod() {
        assertThrows(IllegalArgumentException.class, () -> BgEvaluator.evaluate(5.0, "random"));
        assertThrows(IllegalArgumentException.class, () -> BgEvaluator.evaluate(5.0, null));
    }

    @Test
    void coversMaximumAcceptedValueBeforeDiabetesResult() {
        Map<String, Object> result = BgEvaluator.evaluate(35.0, BgEvaluator.PERIOD_POST_MEAL);

        assertEquals(BgEvaluator.LEVEL_DIABETES, result.get("level"));
        assertEquals(35.0, result.get("value"));
        assertEquals(BgEvaluator.PERIOD_POST_MEAL, result.get("period"));
        assertNotNull(result.get("suggestion"));
    }
}
