package by.it.group310951.makarevich.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Map<String, String> fileToProcessedText = new TreeMap<>();

        try (Stream<Path> paths = Files.walk(Paths.get(src))) {
            List<Path> javaFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .collect(Collectors.toList());

            for (Path file : javaFiles) {
                String content = readFileSafely(file);
                if (content == null || content.contains("@Test") || content.contains("org.junit.Test"))
                    continue;

                String processed = preprocess(content);
                fileToProcessedText.put(file.toString(), processed);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Find similar texts using Levenshtein distance
        List<String> paths = new ArrayList<>(fileToProcessedText.keySet());
        Map<String, List<String>> copiesMap = new TreeMap<>();

        for (int i = 0; i < paths.size(); i++) {
            String path1 = paths.get(i);
            String text1 = fileToProcessedText.get(path1);

            for (int j = i + 1; j < paths.size(); j++) {
                String path2 = paths.get(j);
                String text2 = fileToProcessedText.get(path2);

                if (levenshtein(text1, text2) < 10) {
                    copiesMap.computeIfAbsent(path1, k -> new ArrayList<>()).add(path2);
                    copiesMap.computeIfAbsent(path2, k -> new ArrayList<>()).add(path1);
                }
            }
        }

        // Print results
        for (String path : copiesMap.keySet()) {
            List<String> duplicates = new ArrayList<>(new TreeSet<>(copiesMap.get(path)));
            if (duplicates.isEmpty()) continue;

            System.out.println(path);
            for (String copy : duplicates) {
                System.out.println(copy);
            }
        }
    }

    private static String readFileSafely(Path file) {
        try {
            byte[] bytes = Files.readAllBytes(file);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            // skip unreadable files
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String preprocess(String text) {
        StringBuilder sb = new StringBuilder();
        int len = text.length();
        boolean inLineComment = false;
        boolean inBlockComment = false;

        for (int i = 0; i < len; i++) {
            if (inLineComment && text.charAt(i) == '\n') {
                inLineComment = false;
                sb.append('\n');
                continue;
            }

            if (inBlockComment && text.charAt(i) == '*' && i + 1 < len && text.charAt(i + 1) == '/') {
                inBlockComment = false;
                i++; // skip '/'
                continue;
            }

            if (inLineComment || inBlockComment) continue;

            if (text.charAt(i) == '/' && i + 1 < len) {
                if (text.charAt(i + 1) == '/') {
                    inLineComment = true;
                    i++;
                    continue;
                } else if (text.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i++;
                    continue;
                }
            }

            sb.append(text.charAt(i));
        }

        String cleaned = sb.toString();
        cleaned = cleaned.replaceAll("(?m)^\\s*package\\s+.*?;", "");
        cleaned = cleaned.replaceAll("(?m)^\\s*import\\s+.*?;", "");
        cleaned = cleaned.replaceAll("[\\x00-\\x20&&[^\\x20]]", " "); // replace code <33 with space
        return cleaned.trim();
    }

    private static int levenshtein(String s1, String s2) {
        if (s1.equals(s2)) return 0;
        int len1 = s1.length(), len2 = s2.length();
        if (Math.abs(len1 - len2) >= 10) return 10; // early cutoff

        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];
        for (int j = 0; j <= len2; j++) prev[j] = j;

        for (int i = 1; i <= len1; i++) {
            curr[0] = i;
            for (int j = 1; j <= len2; j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(Math.min(
                                curr[j - 1] + 1,
                                prev[j] + 1),
                        prev[j - 1] + cost);
            }
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[len2];
    }
}