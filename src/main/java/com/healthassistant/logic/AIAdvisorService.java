package com.healthassistant.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class AIAdvisorService {

    private AIAdvisorService() {}

    public static final String RESPONSE_NO_MATCH = "抱歉，我暂时无法回答您的问题。\n请尝试询问关于血压、血糖、睡眠、运动、用药或饮食营养方面的健康知识。";

    private static final List<KnowledgeEntry> KNOWLEDGE_BASE = buildKnowledgeBase();

    public record KnowledgeEntry(String id, String category, List<String> keywords, String answer) {}

    public static List<KnowledgeEntry> getKnowledgeBase() {
        return new ArrayList<>(KNOWLEDGE_BASE);
    }

    public static String answer(String question) {
        if (question == null || question.trim().isEmpty()) {
            return RESPONSE_NO_MATCH;
        }
        String normalized = question.trim();
        List<String> inputKeywords = extractKeywords(normalized);

        KnowledgeEntry best = null;
        int bestScore = 0;
        for (KnowledgeEntry entry : KNOWLEDGE_BASE) {
            int score = calculateMatchScore(inputKeywords, entry.keywords());
            if (score > bestScore) {
                bestScore = score;
                best = entry;
            }
        }

        if (best != null && bestScore >= 1) {
            return best.answer();
        }
        return RESPONSE_NO_MATCH;
    }

    public static List<String> extractKeywords(String text) {
        List<String> keywords = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) {
            return keywords;
        }

        String cleaned = text.trim()
                .replace("？", " ")
                .replace("，", " ")
                .replace(",", " ")
                .replace("。", " ")
                .replace(".", " ")
                .replace("！", " ")
                .replace("!", " ")
                .replace("、", " ")
                .replace("；", " ")
                .replace(";", " ")
                .replace("：", " ")
                .replace(":", " ");

        for (String part : cleaned.split("[\\s]+")) {
            String kw = part.trim();
            if (!kw.isEmpty() && kw.length() >= 2) {
                keywords.add(kw);
            }
        }
        return keywords;
    }

    public static int calculateMatchScore(List<String> inputKeywords, List<String> entryKeywords) {
        if (inputKeywords.isEmpty() || entryKeywords.isEmpty()) {
            return 0;
        }
        int score = 0;
        for (String ik : inputKeywords) {
            for (String ek : entryKeywords) {
                if (ik.equals(ek)) {
                    score += 5;
                } else if (ik.contains(ek)) {
                    score += 1 + ek.length();
                } else if (ek.contains(ik)) {
                    score += 1 + ik.length();
                }
            }
        }
        return score;
    }

    private static List<KnowledgeEntry> buildKnowledgeBase() {
        List<KnowledgeEntry> base = new ArrayList<>();

        base.add(new KnowledgeEntry("bp1", "血压",
                Arrays.asList("血压", "高血压", "血压高", "正常血压", "血压标准"),
                "【正常血压标准】\n根据中国高血压防治指南：\n• 正常血压：收缩压 < 120 mmHg 且 舒张压 < 80 mmHg\n• 正常高值（偏高）：收缩压 120-139 mmHg 或舒张压 80-89 mmHg\n• 高血压：收缩压 ≥ 140 mmHg 或舒张压 ≥ 90 mmHg\n\n建议定期监测血压，保持健康生活方式。"
        ));

        base.add(new KnowledgeEntry("bp2", "血压",
                Arrays.asList("低血压", "血压低", "血压偏低"),
                "【关于低血压】\n收缩压低于 90 mmHg 或舒张压低于 60 mmHg 可视为低血压。\n常见症状：头晕、乏力、眼前发黑。\n建议：起身时放慢动作，适当增加盐分摄入（需遵医嘱），多喝水，避免长时间站立。"
        ));

        base.add(new KnowledgeEntry("bp3", "血压",
                Arrays.asList("降血压", "降压", "控制血压", "降压方法"),
                "【控制血压的建议】\n1. 低盐饮食：每日盐摄入不超过 5 克\n2. 适量运动：每周至少 150 分钟中等强度运动\n3. 控制体重：保持 BMI 在 18.5-24 之间\n4. 戒烟限酒\n5. 保持良好心态，避免情绪激动\n6. 遵医嘱服药，不可擅自停药或换药"
        ));

        base.add(new KnowledgeEntry("bg1", "血糖",
                Arrays.asList("血糖", "正常血糖", "血糖标准", "空腹血糖", "餐后血糖"),
                "【血糖参考标准】\n• 空腹血糖正常值：3.9 - 6.1 mmol/L\n• 餐后 2 小时血糖正常值：3.9 - 7.8 mmol/L\n• 空腹血糖 ≥ 7.0 mmol/L 或餐后 ≥ 11.1 mmol/L 需警惕糖尿病\n• 血糖 < 3.9 mmol/L 为低血糖\n\n建议定期体检，关注血糖变化。"
        ));

        base.add(new KnowledgeEntry("bg2", "血糖",
                Arrays.asList("低血糖", "血糖低", "头晕", "出汗", "心慌"),
                "【低血糖注意事项】\n低血糖时可能出现：心慌、出汗、手抖、头晕、乏力等症状。\n应急处理：立即吃含糖食物（糖果、果汁、饼干），15 分钟后若未缓解再进食。\n预防：规律进餐，避免空腹运动，随身携带含糖零食。"
        ));

        base.add(new KnowledgeEntry("bg3", "血糖",
                Arrays.asList("糖尿病", "控制血糖", "降血糖", "饮食控制"),
                "【糖尿病日常管理】\n1. 合理饮食：控制总热量，少食多餐，优先选择低 GI 食物\n2. 规律运动：饭后 1 小时散步 30 分钟\n3. 监测血糖：按医嘱定期测量并记录\n4. 遵医嘱用药：口服药或胰岛素不可随意停用\n5. 定期复查：每 3-6 个月查糖化血红蛋白"
        ));

        base.add(new KnowledgeEntry("sleep1", "睡眠",
                Arrays.asList("睡眠", "失眠", "睡不着", "睡眠质量", "睡眠时间"),
                "【老年人睡眠建议】\n• 建议每日睡眠 7-8 小时\n• 保持固定作息，定时上床、定时起床\n• 睡前 1 小时远离手机和电视\n• 卧室保持安静、黑暗和适宜温度\n• 睡前可温水泡脚，听轻音乐放松\n• 避免睡前喝浓茶、咖啡"
        ));

        base.add(new KnowledgeEntry("sleep2", "睡眠",
                Arrays.asList("午睡", "午休", "白天睡觉", "打盹"),
                "【关于午睡】\n适量午睡有益健康，但需要注意：\n• 午睡时间控制在 20-30 分钟为宜\n• 不宜超过 1 小时，否则可能影响夜间睡眠\n• 午睡时间建议在下午 1-3 点之间\n• 尽量不要在傍晚或晚饭后打盹"
        ));

        base.add(new KnowledgeEntry("ex1", "运动",
                Arrays.asList("运动", "锻炼", "健身", "运动建议", "运动频率"),
                "【老年人运动建议】\n• 推荐运动：快走、太极拳、广场舞、游泳、骑自行车\n• 运动频率：每周 3-5 次，每次 30-60 分钟\n• 运动强度：以微微出汗、能正常说话为宜\n• 运动前充分热身，运动后做拉伸\n• 避免剧烈运动和对抗性运动\n• 有心脑血管疾病者请先咨询医生"
        ));

        base.add(new KnowledgeEntry("ex2", "运动",
                Arrays.asList("散步", "走路", "步行", "快走"),
                "【散步的好处】\n散步是最适合老年人的运动之一：\n• 每天快走 30 分钟有助于控制血压、血糖和体重\n• 增强心肺功能，改善血液循环\n• 促进肠胃蠕动，预防便秘\n• 改善情绪，缓解压力和焦虑\n• 建议选择平坦路面，穿舒适防滑的鞋子"
        ));

        base.add(new KnowledgeEntry("med1", "用药",
                Arrays.asList("用药", "吃药", "服药", "药物", "药品", "按时吃药"),
                "【安全用药提醒】\n1. 遵医嘱服药，不随意增减剂量\n2. 按时服药，可设置闹钟或用本 App 的用药提醒功能\n3. 不随意混用多种药物，注意药物相互作用\n4. 服药期间不饮酒\n5. 定期清理过期药品\n6. 就医时告知医生所有正在服用的药物"
        ));

        base.add(new KnowledgeEntry("med2", "用药",
                Arrays.asList("忘记吃药", "漏服", "忘记服药", "补服"),
                "【忘记服药怎么办】\n• 如果刚过服药时间不久，可以立即补服\n• 如果距离下次服药时间很近，则跳过本次，下次正常服用\n• 千万不要一次服用双倍剂量！\n• 不确定时请咨询医生或药师\n• 建议使用提醒工具避免漏服"
        ));

        base.add(new KnowledgeEntry("diet1", "饮食",
                Arrays.asList("饮食", "营养", "吃什么", "膳食", "健康饮食"),
                "【老年人健康饮食建议】\n1. 食物多样化，谷类为主\n2. 多吃蔬菜水果，每天至少 5 种\n3. 适量优质蛋白：鱼、蛋、瘦肉、豆制品\n4. 控盐控油：每天盐 < 5g，油 < 25g\n5. 每天饮水 1500-2000ml\n6. 少食多餐，细嚼慢咽"
        ));

        base.add(new KnowledgeEntry("diet2", "饮食",
                Arrays.asList("补钙", "钙", "骨质疏松", "补钙食物", "骨头"),
                "【老年人补钙建议】\n• 推荐每日钙摄入量：1000-1200 mg\n• 补钙食物：牛奶、酸奶、豆制品、虾皮、芝麻酱、绿叶蔬菜\n• 适当晒太阳促进维生素 D 合成，帮助钙吸收\n• 必要时在医生指导下服用钙片和维生素 D 补充剂\n• 注意防止跌倒，预防骨折"
        ));

        base.add(new KnowledgeEntry("general1", "综合",
                Arrays.asList("健康", "养生", "长寿", "保健"),
                "【老年人养生保健要点】\n1. 定期体检：每年至少一次全面体检\n2. 良好心态：保持乐观，多与人交流\n3. 适度运动：每天保持活动量\n4. 规律作息：早睡早起，不熬夜\n5. 健康饮食：均衡营养，清淡为主\n6. 安全用药：遵医嘱，定期复查\n7. 戒烟限酒：远离不良生活习惯"
        ));

        base.add(new KnowledgeEntry("general2", "综合",
                Arrays.asList("体检", "检查", "体检项目", "定期检查"),
                "【老年人常规体检项目】\n• 血压测量：每次就医都应测量\n• 血糖和血脂：每年至少一次\n• 心电图：检查心脏功能\n• 肝功能、肾功能检查\n• 骨密度检查：预防骨质疏松\n• 眼底检查：筛查青光眼、白内障\n• 肿瘤标志物筛查：遵医嘱选择"
        ));

        base.add(new KnowledgeEntry("general3", "综合",
                Arrays.asList("喝水", "饮水", "喝水时间", "补水"),
                "【科学饮水指南】\n• 每天饮水量：1500-2000ml（约 7-8 杯）\n• 早起一杯温水：补充夜间流失水分\n• 饭前半小时喝水：有助消化\n• 少量多次：不要等口渴再喝\n• 睡前少量饮水，避免起夜过多\n• 有心肾功能问题者，饮水遵医嘱"
        ));

        base.add(new KnowledgeEntry("general4", "综合",
                Arrays.asList("头晕", "眩晕", "头昏"),
                "【头晕可能的原因】\n头晕是老年人常见症状，可能原因包括：\n1. 血压波动（高血压或低血压）\n2. 低血糖\n3. 贫血\n4. 颈椎病\n5. 耳石症\n6. 脑供血不足\n\n如果频繁头晕，请及时就医检查，不要自行判断用药。"
        ));

        return base;
    }
}
