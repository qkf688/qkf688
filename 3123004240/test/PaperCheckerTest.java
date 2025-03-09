package com.paperchecker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaperCheckerTest {

    @Test
    public void testExactMatch() {
        String text = "今天是星期天，天气晴，今天晚上我要去看电影。";
        assertEquals(1.0, PaperChecker.calculateSimilarity(text, text));
    }

    @Test
    public void testPartialMatch() {
        String orig = "今天是星期天，天气晴，今天晚上我要去看电影。";
        String plag = "今天是周天，天气晴朗，我晚上要去看电影。";
        double similarity = PaperChecker.calculateSimilarity(orig, plag);
        assertTrue(similarity > 0.5 && similarity < 1.0);
    }

    @Test
    public void testCompletelyDifferent() {
        String orig = "ABCDEF";
        String plag = "UVWXYZ";
        assertEquals(0.0, PaperChecker.calculateSimilarity(orig, plag));
    }

    @Test
    public void testOneCharacterDifference() {
        String orig = "hello";
        String plag = "hella";
        double similarity = PaperChecker.calculateSimilarity(orig, plag);
        // Fixed assertion to allow similarity to be 0.8 (since it's exactly 0.8)
        assertTrue(similarity >= 0.8);
    }

    @Test
    public void testEmptyStrings() {
        assertEquals(1.0, PaperChecker.calculateSimilarity("", ""));
    }

    @Test
    public void testOriginalEmpty() {
        assertEquals(0.0, PaperChecker.calculateSimilarity("", "not empty"));
    }

    @Test
    public void testPlagiarizedEmpty() {
        assertEquals(0.0, PaperChecker.calculateSimilarity("not empty", ""));
    }

    @Test
    public void testWhitespaceIgnored() {
        String orig = "Hello World";
        String plag = "Hello   World";
        assertEquals(1.0, PaperChecker.calculateSimilarity(orig, plag));
    }

    @Test
    public void testCaseSensitivity() {
        String orig = "Hello World";
        String plag = "hello world";
        double similarity = PaperChecker.calculateSimilarity(orig, plag);
        assertTrue(similarity < 1.0);
    }

    @Test
    public void testLongText() {
        String orig = "这是一个非常长的文本，我们需要测试它的相似度计算是否能够正常工作。" +
                "长文本测试可以揭示程序在处理大量数据时的性能问题。";
        String plag = "这是一个很长的文本，我们需要测试它的查重算法是否能够准确工作。" +
                "长文本测试可以检查程序在处理大数据时的性能瓶颈。";
        double similarity = PaperChecker.calculateSimilarity(orig, plag);
        assertTrue(similarity > 0.5 && similarity < 1.0);
    }
}
