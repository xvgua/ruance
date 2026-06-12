package com.healthassistant.logic;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SleepEvaluator {

    private SleepEvaluator() {}

    public static final String LEVEL_SUFFICIENT = "充足";
    public static final String LEVEL_NORMAL = "一般";
    public static final String LEVEL_INSUFFICIENT = "不足";

    public static Map<String, Object> evaluate(int sleepHour, int sleepMinute,
                                                int wakeHour, int wakeMinute, int selfScore) {
        if (sleepHour < 0 || sleepHour > 23) {
            throw new IllegalArgumentException("入睡小时必须在 0-23 之间");
        }
        if (sleepMinute < 0 || sleepMinute > 59) {
            throw new IllegalArgumentException("入睡分钟必须在 0-59 之间");
        }
        if (wakeHour < 0 || wakeHour > 23) {
            throw new IllegalArgumentException("起床小时必须在 0-23 之间");
        }
        if (wakeMinute < 0 || wakeMinute > 59) {
            throw new IllegalArgumentException("起床分钟必须在 0-59 之间");
        }
        if (selfScore < 1 || selfScore > 5) {
            throw new IllegalArgumentException("睡眠质量自评必须在 1-5 之间");
        }

        int sleepTotal = sleepHour * 60 + sleepMinute;
        int wakeTotal = wakeHour * 60 + wakeMinute;

        int durationMinutes;
        if (sleepTotal < wakeTotal) {
            durationMinutes = wakeTotal - sleepTotal;
        } else {
            durationMinutes = (24 * 60 - sleepTotal) + wakeTotal;
        }

        int hours = durationMinutes / 60;
        int minutes = durationMinutes % 60;

        String level;
        String suggestion;

        if (durationMinutes >= 420 && selfScore >= 4) {
            level = LEVEL_SUFFICIENT;
            suggestion = "睡眠时间充足，质量良好，请保持当前作息规律。";
        } else if (durationMinutes >= 360) {
            level = LEVEL_NORMAL;
            suggestion = "睡眠时间基本达标，建议保持规律作息，睡前减少看手机。";
        } else {
            level = LEVEL_INSUFFICIENT;
            suggestion = "睡眠时间不足，建议提早上床，睡前放松心情，避免咖啡因和浓茶。";
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sleepTime", String.format("%02d:%02d", sleepHour, sleepMinute));
        result.put("wakeTime", String.format("%02d:%02d", wakeHour, wakeMinute));
        result.put("durationHours", hours);
        result.put("durationMinutes", minutes);
        result.put("selfScore", selfScore);
        result.put("level", level);
        result.put("suggestion", suggestion);
        return result;
    }
}
