package by.it.group310951.makarevich.lesson15;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.stream.Stream;


public class SourceScannerA {


    public static void main(String[] args) {
        // Определяем рабочий каталог
        Path srcDir = Path.of(System.getProperty("user.dir"));

        // Если текущая директория — не "src", добавляем подкаталог "src"
        if (!"src".equals(srcDir.getFileName().toString())) {
            srcDir = srcDir.resolve("src");
        }

        // Проверка, что каталог существует и это действительно директория
        if (!Files.isDirectory(srcDir)) {
            System.err.println("Исходная папка не найдена: " + srcDir);
            return;
        }

        // Рекурсивный обход всех файлов и вывод подходящих путей
        try (Stream<Path> paths = Files.walk(srcDir)) {
            paths
                    .filter(SourceScannerA::isValidJavaFile)   // фильтруем только нужные Java-файлы
                    .map(srcDir::relativize)                   // получаем относительный путь
                    .sorted()                                  // сортируем для стабильного вывода
                    .forEach(System.out::println);             // выводим
        } catch (IOException e) {
            System.err.println("Ошибка при обходе директории: " + e.getMessage());
        }
    }


    private static boolean isValidJavaFile(Path path) {
        // Проверяем расширение файла
        if (!path.toString().endsWith(".java")) return false;

        try {
            // Читаем содержимое файла в UTF-8
            String content = Files.readString(path, StandardCharsets.UTF_8);
            // Ищем аннотации, указывающие на тестовый файл
            return !(content.contains("@Test") || content.contains("org.junit.Test"));
        } catch (IOException e) {
            // Игнорируем файлы, которые не удалось прочитать
            return false;
        }
    }
}