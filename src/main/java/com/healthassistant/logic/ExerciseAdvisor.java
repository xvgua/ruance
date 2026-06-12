package com.healthassistant.logic;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ExerciseAdvisor {

    private ExerciseAdvisor() {}

    public static final String FREQ_SEDENTARY = "几乎不";
    public static final String FREQ_OCCASIONAL = "偶尔";
    public static final String FREQ_FREQUENT = "经常";

    private static final Map<String, String> FREQUENCY_LABELS = Map.of(
            FREQ_SEDENTARY, "几乎不运动",
            FREQ_OCCASIONAL, "偶尔运动",
            FREQ_FREQUENT, "经常运动"
    );

    public static Map<String, String> getExerciseAdvice(int age, String frequency) {
        if (age <= 0) {
            throw new IllegalArgumentException("年龄不能为负数");
        }
        if (age > 120) {
            throw new IllegalArgumentException("请输入合理年龄");
        }
        if (!FREQ_SEDENTARY.equals(frequency)
                && !FREQ_OCCASIONAL.equals(frequency)
                && !FREQ_FREQUENT.equals(frequency)) {
            throw new IllegalArgumentException("运动频率参数无效");
        }

        Map<String, String> plan = new LinkedHashMap<>();

        switch (frequency) {
            case FREQ_SEDENTARY:
                plan.put("type", "散步");
                plan.put("duration", "15-20 分钟/次");
                plan.put("frequency", "每周 3 次");
                plan.put("note", "从短时间低强度开始，逐步增加运动量，注意热身。");
                break;
            case FREQ_OCCASIONAL:
                if (age < 70) {
                    plan.put("type", "快走或太极拳");
                    plan.put("duration", "25-30 分钟/次");
                    plan.put("frequency", "每周 4 次");
                    plan.put("note", "保持中等强度，心率稍加快即可，避免剧烈运动。");
                } else {
                    plan.put("type", "散步、拉伸运动");
                    plan.put("duration", "20-25 分钟/次");
                    plan.put("frequency", "每周 3-4 次");
                    plan.put("note", "注意关节保护，避免剧烈运动，注意补充水分。");
                }
                break;
            case FREQ_FREQUENT:
                if (age < 70) {
                    plan.put("type", "快走、游泳、太极拳");
                    plan.put("duration", "30-40 分钟/次");
                    plan.put("frequency", "每周 5 次");
                    plan.put("note", "保持规律运动，注意运动前后拉伸，避免过度劳累。");
                } else {
                    plan.put("type", "散步、太极拳、健身操");
                    plan.put("duration", "25-30 分钟/次");
                    plan.put("frequency", "每周 4-5 次");
                    plan.put("note", "根据身体状况调整强度，感到不适时立即休息。");
                }
                break;
        }

        return plan;
    }

    public static String getFrequencyLabel(String freqKey) {
        return FREQUENCY_LABELS.getOrDefault(freqKey, freqKey);
    }
}
