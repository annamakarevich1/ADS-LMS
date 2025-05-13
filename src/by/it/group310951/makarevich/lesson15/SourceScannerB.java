package by.it.group310951.makarevich.lesson15;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;


public class SourceScannerB {


    public static void main(String[] args) {
        Path base = resolveSrcPath();
        if (base == null) {
            System.err.println("Исходная папка не найдена.");
            return;
        }

        try (Stream<Path> files = Files.walk(base)) {
            files
                    .filter(SourceScannerB::isJavaSource)
                    .filter(SourceScannerB::notATestFile)
                    .map(base::relativize)
                    .sorted(Comparator.naturalOrder())
                    .forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Ошибка при обходе директории: " + e.getMessage());
        }
    }


    private static boolean isJavaSource(Path path) {
        return path.toString().endsWith(".java");
    }


    private static boolean notATestFile(Path file) {
        try {
            String text = Files.readString(file, StandardCharsets.UTF_8);
            return !(text.contains("@Test") || text.contains("org.junit.Test"));
        } catch (IOException e) {
            // Проблемы при чтении файла — просто исключаем его
            return false;
        }
    }

    private static Path resolveSrcPath() {
        Path current = Path.of(System.getProperty("user.dir"));
        Path srcPath = current.getFileName().toString().equals("src") ? current : current.resolve("src");
        return Files.isDirectory(srcPath) ? srcPath : null;
    }
}