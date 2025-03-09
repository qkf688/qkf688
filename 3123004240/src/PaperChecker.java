package com.paperchecker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PaperChecker {

    // 计算Levenshtein距离
    public static int calculateLevenshteinDistance(String s1, String s2) {
        int lenS1 = s1.length(), lenS2 = s2.length();
        int[] prev = new int[lenS2 + 1];
        int[] curr = new int[lenS2 + 1];
        for (int j = 0; j <= lenS2; j++) prev[j] = j;
        for (int i = 1; i <= lenS1; i++) {
            curr[0] = i;
            for (int j = 1; j <= lenS2; j++) {
                curr[j] = Math.min(prev[j-1] + (s1.charAt(i-1) == s2.charAt(j-1) ? 0 : 1),
                        Math.min(prev[j] + 1, curr[j-1] + 1));
            }
            int[] temp = prev; prev = curr; curr = temp;
        }
        return prev[lenS2];
    }

    // 计算文本相似度
    public static double calculateSimilarity(String text1, String text2) {
        text1 = text1.trim().replaceAll("\\s+", " ");
        text2 = text2.trim().replaceAll("\\s+", " ");

        if (text1.isEmpty() && text2.isEmpty()) {
            return 1.0;
        }

        int levenshteinDistance = calculateLevenshteinDistance(text1, text2);
        int maxLength = Math.max(text1.length(), text2.length());

        if (maxLength == 0) {
            return 1.0;
        }

        double similarity = 1.0 - (double) levenshteinDistance / maxLength;
        return Double.parseDouble(String.format("%.2f", similarity));
    }

    // 从文件读取内容（优化版）
    public static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString().trim();
    }

    // 主方法：从命令行读取文件并输出相似度结果
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java -jar main.jar <original_file> <plagiarized_file> <output_file>");
            return;
        }

        String originalFile = args[0];
        String plagiarizedFile = args[1];
        String outputFile = args[2];

        try {
            String originalText = readFile(originalFile);
            String plagiarizedText = readFile(plagiarizedFile);
            double similarity = calculateSimilarity(originalText, plagiarizedText);

            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile))) {
                writer.write(String.format("Similarity: %.2f%%", similarity * 100));
            }

            System.out.println("Similarity: " + similarity);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}