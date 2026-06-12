package com.healthassistant.logic;

import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseAdvisorTest {

    @Test
    void testSedentaryYoungElderly() {
        Map<String, String> plan = ExerciseAdvisor.getExerciseAdvice(65, ExerciseAdvisor.FREQ_SEDENTARY);
        assertEquals("散步", plan.get("type"));
        assertTrue(plan.get("duration").contains("分钟"));
    }

    @Test
    void testOccasionalYoungElderly() {
        Map<String, String> plan = ExerciseAdvisor.getExerciseAdvice(65, ExerciseAdvisor.FREQ_OCCASIONAL);
        assertTrue(plan.get("type").contains("快走"));
        assertTrue(plan.get("type").contains("太极拳"));
    }

    @Test
    void testOccasionalOldElderly() {
        Map<String, String> plan = ExerciseAdvisor.getExerciseAdvice(75, ExerciseAdvisor.FREQ_OCCASIONAL);
        assertTrue(plan.get("type").contains("散步"));
        assertTrue(plan.get("type").contains("拉伸"));
    }

    @Test
    void testFrequentYoungElderly() {
        Map<String, String> plan = ExerciseAdvisor.getExerciseAdvice(60, ExerciseAdvisor.FREQ_FREQUENT);
        assertTrue(plan.get("type").contains("游泳"));
        assertEquals("每周 5 次", plan.get("frequency"));
    }

    @Test
    void testFrequentOldElderly() {
        Map<String, String> plan = ExerciseAdvisor.getExerciseAdvice(80, ExerciseAdvisor.FREQ_FREQUENT);
        assertTrue(plan.get("type").contains("太极拳"));
        assertEquals("每周 4-5 次", plan.get("frequency"));
    }

    @Test
    void testAgeBoundary70Sedentary() {
        Map<String, String> planBelow = ExerciseAdvisor.getExerciseAdvice(69, ExerciseAdvisor.FREQ_SEDENTARY);
        Map<String, String> planAbove = ExerciseAdvisor.getExerciseAdvice(70, ExerciseAdvisor.FREQ_SEDENTARY);
        assertEquals(planBelow.get("type"), planAbove.get("type"));
    }

    @Test
    void testAgeBoundary70Occasional() {
        Map<String, String> planBelow = ExerciseAdvisor.getExerciseAdvice(69, ExerciseAdvisor.FREQ_OCCASIONAL);
        Map<String, String> planAbove = ExerciseAdvisor.getExerciseAdvice(70, ExerciseAdvisor.FREQ_OCCASIONAL);
        assertNotEquals(planBelow.get("type"), planAbove.get("type"));
    }

    @Test
    void testAgeBoundary70Frequent() {
        Map<String, String> planBelow = ExerciseAdvisor.getExerciseAdvice(69, ExerciseAdvisor.FREQ_FREQUENT);
        Map<String, String> planAbove = ExerciseAdvisor.getExerciseAdvice(70, ExerciseAdvisor.FREQ_FREQUENT);
        assertNotEquals(planBelow.get("type"), planAbove.get("type"));
    }

    @Test
    void testResultContainsAllKeys() {
        Map<String, String> plan = ExerciseAdvisor.getExerciseAdvice(65, ExerciseAdvisor.FREQ_OCCASIONAL);
        assertTrue(plan.containsKey("type"));
        assertTrue(plan.containsKey("duration"));
        assertTrue(plan.containsKey("frequency"));
        assertTrue(plan.containsKey("note"));
    }

    @Test
    void testNegativeAgeRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                ExerciseAdvisor.getExerciseAdvice(-1, ExerciseAdvisor.FREQ_OCCASIONAL));
    }

    @Test
    void testZeroAgeRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                ExerciseAdvisor.getExerciseAdvice(0, ExerciseAdvisor.FREQ_OCCASIONAL));
    }

    @Test
    void testUnreasonableAgeRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                ExerciseAdvisor.getExerciseAdvice(150, ExerciseAdvisor.FREQ_OCCASIONAL));
    }

    @Test
    void testInvalidFrequencyRaises() {
        assertThrows(IllegalArgumentException.class, () ->
                ExerciseAdvisor.getExerciseAdvice(65, "每天"));
    }

    @Test
    void testFrequencyLabel() {
        assertEquals("几乎不运动", ExerciseAdvisor.getFrequencyLabel(ExerciseAdvisor.FREQ_SEDENTARY));
        assertEquals("偶尔运动", ExerciseAdvisor.getFrequencyLabel(ExerciseAdvisor.FREQ_OCCASIONAL));
        assertEquals("经常运动", ExerciseAdvisor.getFrequencyLabel(ExerciseAdvisor.FREQ_FREQUENT));
        assertEquals("未知", ExerciseAdvisor.getFrequencyLabel("未知"));
    }
}
