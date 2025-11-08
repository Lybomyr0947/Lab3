
package io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FightIO {
    private static FightLog LAST_LOG = new FightLog();

    public static void setLastLog(FightLog log) { LAST_LOG = log; }
    public static List<String> getLastLog() { return LAST_LOG.getLines(); }

    public static String saveLastLogToFile(String name) {
        try {
            Files.createDirectories(Path.of("battles"));
            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "battles/" + name + "_" + stamp + ".txt";
            try (Writer w = new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8)) {
                for (String s : LAST_LOG.getLines()) {
                    w.write(s);
                    w.write(System.lineSeparator());
                }
            }
            return filename;
        } catch (IOException e) {
            System.out.println("Помилка запису: " + e.getMessage());
            return "";
        }
    }

    public static void replayFromFile(String name) {
        String filename = "battles/" + name + ".txt";
        Path path = Path.of(filename);
        if (!Files.exists(path)) {
            System.out.println("Файл не знайдено: " + filename);
            return;
        }
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            System.out.println("\n▶ ВІДТВОРЕННЯ БОЮ з файлу: " + filename);
            for (String s : lines) {
                System.out.println(s);
                try { Thread.sleep(120); } catch (InterruptedException ignored) {}
            }
        } catch (IOException e) {
            System.out.println("Помилка читання: " + e.getMessage());
        }
    }
}
