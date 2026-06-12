package com.healthassistant.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AIAdvisorServiceTest {

    @Test
    void testGetKnowledgeBaseNotEmpty() {
        List<AIAdvisorService.KnowledgeEntry> base = AIAdvisorService.getKnowledgeBase();
        assertNotNull(base);
        assertFalse(base.isEmpty());
    }

    @Test
    void testGetKnowledgeBaseReturnsCopy() {
        List<AIAdvisorService.KnowledgeEntry> base1 = AIAdvisorService.getKnowledgeBase();
        List<AIAdvisorService.KnowledgeEntry> base2 = AIAdvisorService.getKnowledgeBase();
        assertNotSame(base1, base2);
        assertEquals(base1.size(), base2.size());
    }

    @Test
    void testExtractKeywordsNull() {
        List<String> result = AIAdvisorService.extractKeywords(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testExtractKeywordsEmpty() {
        List<String> result = AIAdvisorService.extractKeywords("");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testExtractKeywordsBlank() {
        List<String> result = AIAdvisorService.extractKeywords("   ");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testExtractKeywordsSimpleWords() {
        List<String> result = AIAdvisorService.extractKeywords("血压 血糖 睡眠");
        assertEquals(3, result.size());
        assertTrue(result.contains("血压"));
        assertTrue(result.contains("血糖"));
        assertTrue(result.contains("睡眠"));
    }

    @Test
    void testExtractKeywordsShortWordsFiltered() {
        List<String> result = AIAdvisorService.extractKeywords("我 的 血压 高 了");
        assertEquals(1, result.size());
        assertTrue(result.contains("血压"));
    }

    @Test
    void testExtractKeywordsWithPunctuation() {
        List<String> result = AIAdvisorService.extractKeywords("血压多少算正常？应该怎么控制？");
        assertEquals(2, result.size());
        assertTrue(result.contains("血压多少算正常"));
        assertTrue(result.contains("应该怎么控制"));
    }

    @Test
    void testExtractKeywordsComma() {
        List<String> result = AIAdvisorService.extractKeywords("高血压，怎么办");
        assertEquals(2, result.size());
        assertTrue(result.contains("高血压"));
        assertTrue(result.contains("怎么办"));
    }

    @Test
    void testExtractKeywordsPeriod() {
        List<String> result = AIAdvisorService.extractKeywords("血压高。头晕。怎么办");
        assertEquals(3, result.size());
        assertTrue(result.contains("血压高"));
    }

    @Test
    void testExtractKeywordsExclamation() {
        List<String> result = AIAdvisorService.extractKeywords("忘记吃药了！怎么办！");
        assertEquals(2, result.size());
        assertTrue(result.contains("忘记吃药了"));
        assertTrue(result.contains("怎么办"));
    }

    @Test
    void testExtractKeywordsSemicolon() {
        List<String> result = AIAdvisorService.extractKeywords("血压高；头晕；怎么办");
        assertEquals(3, result.size());
    }

    @Test
    void testCalculateMatchScoreBothEmpty() {
        assertEquals(0, AIAdvisorService.calculateMatchScore(
                Collections.emptyList(), Collections.emptyList()));
    }

    @Test
    void testCalculateMatchScoreInputEmpty() {
        assertEquals(0, AIAdvisorService.calculateMatchScore(
                Collections.emptyList(), List.of("血压", "血糖")));
    }

    @Test
    void testCalculateMatchScoreEntryEmpty() {
        assertEquals(0, AIAdvisorService.calculateMatchScore(
                List.of("血压", "血糖"), Collections.emptyList()));
    }

    @Test
    void testCalculateMatchScoreExactMatch() {
        int score = AIAdvisorService.calculateMatchScore(
                List.of("血压"), List.of("血压"));
        assertEquals(5, score);
    }

    @Test
    void testCalculateMatchScoreExactMatchMultiple() {
        int score = AIAdvisorService.calculateMatchScore(
                List.of("血压", "血糖"), List.of("血压", "血糖", "运动"));
        assertEquals(10, score);
    }

    @Test
    void testCalculateMatchScorePartialMatch() {
        int score = AIAdvisorService.calculateMatchScore(
                List.of("高血压"), List.of("血压"));
        assertEquals(3, score);
    }

    @Test
    void testCalculateMatchScoreNoMatch() {
        int score = AIAdvisorService.calculateMatchScore(
                List.of("血压"), List.of("运动", "睡眠"));
        assertEquals(0, score);
    }

    @Test
    void testCalculateMatchScoreMixed() {
        int score = AIAdvisorService.calculateMatchScore(
                List.of("血压", "血糖"), List.of("血压", "运动"));
        assertEquals(5, score);
    }

    @Test
    void testAnswerNull() {
        String result = AIAdvisorService.answer(null);
        assertEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
    }

    @Test
    void testAnswerEmpty() {
        String result = AIAdvisorService.answer("");
        assertEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
    }

    @Test
    void testAnswerBlank() {
        String result = AIAdvisorService.answer("   ");
        assertEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
    }

    @Test
    void testAnswerBloodPressureNormal() {
        String result = AIAdvisorService.answer("血压多少算正常");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("120"));
        assertTrue(result.contains("80"));
    }

    @Test
    void testAnswerHypertension() {
        String result = AIAdvisorService.answer("高血压怎么办");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("收缩压") || result.contains("血压"));
    }

    @Test
    void testAnswerLowBloodPressure() {
        String result = AIAdvisorService.answer("低血压怎么办");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("低血压"));
    }

    @Test
    void testAnswerBloodGlucoseNormal() {
        String result = AIAdvisorService.answer("血糖正常值是多少");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("mmol/L"));
    }

    @Test
    void testAnswerLowBloodGlucose() {
        String result = AIAdvisorService.answer("低血糖的时候心慌怎么办");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("低血糖") || result.contains("血糖"));
    }

    @Test
    void testAnswerDiabetes() {
        String result = AIAdvisorService.answer("糖尿病怎么控制");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("血糖"));
    }

    @Test
    void testAnswerSleep() {
        String result = AIAdvisorService.answer("晚上睡不着怎么办");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("睡眠"));
    }

    @Test
    void testAnswerNap() {
        String result = AIAdvisorService.answer("中午午睡多久合适");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("午睡"));
    }

    @Test
    void testAnswerExercise() {
        String result = AIAdvisorService.answer("老年人适合什么运动");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("运动"));
    }

    @Test
    void testAnswerWalking() {
        String result = AIAdvisorService.answer("每天散步有什么好处");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("散步"));
    }

    @Test
    void testAnswerMedication() {
        String result = AIAdvisorService.answer("按时吃药要注意什么");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("药"));
    }

    @Test
    void testAnswerMissDose() {
        String result = AIAdvisorService.answer("忘记吃药了要补服吗");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("服药"));
    }

    @Test
    void testAnswerDietHealth() {
        String result = AIAdvisorService.answer("老年人健康饮食要注意什么");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("饮食"));
    }

    @Test
    void testAnswerCalcium() {
        String result = AIAdvisorService.answer("怎么补钙比较好");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("钙"));
    }

    @Test
    void testAnswerGeneralHealth() {
        String result = AIAdvisorService.answer("怎么养生保健");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
        assertTrue(result.contains("体检") || result.contains("健康"));
    }

    @Test
    void testAnswerNoMatch() {
        String result = AIAdvisorService.answer("今天天气怎么样");
        assertEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
    }

    @Test
    void testAnswerIrrelevant() {
        String result = AIAdvisorService.answer("股票涨了多少");
        assertEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
    }

    @Test
    void testAnswerBoundarySingleChar() {
        String result = AIAdvisorService.answer("血压");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
    }

    @Test
    void testExtractKeywordsAllShort() {
        List<String> result = AIAdvisorService.extractKeywords("我 的 很 好 不 大");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testExtractKeywordsMixedLength() {
        List<String> result = AIAdvisorService.extractKeywords("我 血压 高 了 怎么办");
        assertEquals(2, result.size());
        assertTrue(result.contains("血压"));
        assertTrue(result.contains("怎么办"));
    }

    @Test
    void testExtractKeywordsEnglishChars() {
        List<String> result = AIAdvisorService.extractKeywords("BMI 怎么 计算");
        assertEquals(3, result.size());
        assertTrue(result.contains("BMI"));
        assertTrue(result.contains("怎么"));
        assertTrue(result.contains("计算"));
    }

    @Test
    void testCalculateMatchScoreLongerContainsShorter() {
        int score = AIAdvisorService.calculateMatchScore(
                List.of("高血压", "怎么办"), List.of("血压", "健康"));
        assertEquals(3, score);
    }

    @Test
    void testCalculateMatchScoreShorterContainsLonger() {
        int score = AIAdvisorService.calculateMatchScore(
                List.of("血压"), List.of("高血压"));
        assertEquals(3, score);
    }

    @Test
    void testAnswerFullQuestion() {
        String result = AIAdvisorService.answer("血压多少算正常？我今年65岁了");
        assertNotEquals(AIAdvisorService.RESPONSE_NO_MATCH, result);
    }

    @Test
    void testKnowledgeBaseContainsAllCategories() {
        List<AIAdvisorService.KnowledgeEntry> base = AIAdvisorService.getKnowledgeBase();
        boolean hasBP = false, hasBG = false, hasSleep = false;
        boolean hasExercise = false, hasMed = false, hasDiet = false;
        for (var entry : base) {
            switch (entry.category()) {
                case "血压" -> hasBP = true;
                case "血糖" -> hasBG = true;
                case "睡眠" -> hasSleep = true;
                case "运动" -> hasExercise = true;
                case "用药" -> hasMed = true;
                case "饮食" -> hasDiet = true;
            }
        }
        assertTrue(hasBP, "Knowledge base should contain blood pressure entries");
        assertTrue(hasBG, "Knowledge base should contain blood glucose entries");
        assertTrue(hasSleep, "Knowledge base should contain sleep entries");
        assertTrue(hasExercise, "Knowledge base should contain exercise entries");
        assertTrue(hasMed, "Knowledge base should contain medication entries");
        assertTrue(hasDiet, "Knowledge base should contain diet entries");
    }

    @Test
    void testKnowledgeEntryFieldsNotNull() {
        for (var entry : AIAdvisorService.getKnowledgeBase()) {
            assertNotNull(entry.id(), "Entry id should not be null");
            assertNotNull(entry.category(), "Entry category should not be null");
            assertNotNull(entry.keywords(), "Entry keywords should not be null");
            assertNotNull(entry.answer(), "Entry answer should not be null");
            assertFalse(entry.id().isEmpty());
            assertFalse(entry.category().isEmpty());
            assertFalse(entry.keywords().isEmpty());
            assertFalse(entry.answer().isEmpty());
        }
    }
}
