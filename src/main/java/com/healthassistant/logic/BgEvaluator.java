package com.healthassistant.logic;

import java.util.LinkedHashMap;
import java.util.Map;

public final class BgEvaluator {

    private BgEvaluator() {}

    public static final String PERIOD_FASTING = "空腹";
    public static final String PERIOD_POST_MEAL = "餐后";
    public static final String LEVEL_NORMAL = "正常";
    public static final String LEVEL_HIGH = "偏高";
    public static final String LEVEL_DIABETES = "高血糖";
    public static final String LEVEL_LOW = "低血糖";

    public static Map<String, Object> evaluate(double value, String period) {
        if (value <= 0) {
            throw new IllegalArgumentException("血糖值不能为负数或零");
        }
        if (value > 35) {
            throw new IllegalArgumentException("血糖值超出合理范围");
        }
        if (!PERIOD_FASTING.equals(period) && !PERIOD_POST_MEAL.equals(period)) {
            throw new IllegalArgumentException("测量时段必须为\"空腹\"或\"餐后\"");
        }

        double[] normalRange = PERIOD_FASTING.equals(period)
                ? new double[]{3.9, 6.1}
                : new double[]{3.9, 7.8};
        double[] highRange = PERIOD_FASTING.equals(period)
                ? new double[]{6.1, 7.0}
                : new double[]{7.8, 11.1};

        String level;
        String suggestion;

        if (value < normalRange[0]) {
            level = LEVEL_LOW;
            suggestion = "血糖偏低，请及时补充糖分，如症状持续请就医。";
        } else if (value <= normalRange[1]) {
            level = LEVEL_NORMAL;
            suggestion = "血糖正常，请保持健康饮食习惯。";
        } else if (value <= highRange[1]) {
            level = LEVEL_HIGH;
            suggestion = "血糖偏高，建议控制糖分摄入，增加粗粮和蔬菜摄入，定期监测。";
        } else {
            level = LEVEL_DIABETES;
            suggestion = "血糖过高，请及时就医检查，遵医嘱调整饮食和药物。";
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("value", value);
        result.put("period", period);
        result.put("level", level);
        result.put("suggestion", suggestion);
        return result;
    }
}
