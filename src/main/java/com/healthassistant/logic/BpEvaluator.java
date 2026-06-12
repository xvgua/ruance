package com.healthassistant.logic;

import java.util.LinkedHashMap;
import java.util.Map;

public final class BpEvaluator {

    private BpEvaluator() {}

    public static final String LEVEL_NORMAL = "正常";
    public static final String LEVEL_HIGH = "偏高";
    public static final String LEVEL_HYPERTENSION = "高血压";

    public static Map<String, Object> evaluate(double systolic, double diastolic) {
        if (systolic <= 0 || diastolic <= 0) {
            throw new IllegalArgumentException("血压值不能为负数或零");
        }
        if (systolic > 300 || diastolic > 200) {
            throw new IllegalArgumentException("血压值超出合理范围");
        }

        String level;
        String suggestion;

        if (systolic < 120 && diastolic < 80) {
            level = LEVEL_NORMAL;
            suggestion = "血压正常，请保持健康生活习惯。";
        } else if (systolic < 140 && diastolic < 90) {
            level = LEVEL_HIGH;
            suggestion = "血压偏高，建议减少盐分摄入，适当运动，定期监测。";
        } else {
            level = LEVEL_HYPERTENSION;
            suggestion = "血压过高，请及时就医咨询专业医生，遵医嘱服药。";
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("systolic", systolic);
        result.put("diastolic", diastolic);
        result.put("level", level);
        result.put("suggestion", suggestion);
        return result;
    }
}
