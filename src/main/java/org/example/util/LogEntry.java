package org.example.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Класс, представляющий запись лога.
 */
@Data
@AllArgsConstructor
public class LogEntry {
    private LocalDateTime timestamp;
    private String level;
    private String user;
    private String action;
    private String message;
    /**
     * Разбирает строку лога и создает объект {@link LogEntry}.
     *
     * @param logLine строка лога, содержащая метку времени, уровень, пользователя, действие и сообщение, разделенные пробелами.
     *                Пример строки лога: "2024-08-03 12:08:40,969 INFO  org.example.repository.AuthServiceImpl.getAll:18 - Get all cars"
     * @return новый объект {@link LogEntry}, созданный из строки лога.
     * @throws DateTimeParseException если метка времени не может быть разобрана.
     */
    public static LogEntry parse(String logLine) {
        String[] parts = logLine.split(" ", 6);

        // Проверка на корректный формат (ожидаемое количество частей и правильный формат временной метки)
        if (parts.length < 6) {
            System.out.println("Skipping invalid log line: " + logLine);
            return null;  // Можно вернуть null, если строка не соответствует формату
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
            LocalDateTime timestamp = LocalDateTime.parse(parts[0] + " " + parts[1], formatter);
            String level = parts[2];
            String user = parts[3];
            String action = parts[4];
            String message = parts[5];

            return new LogEntry(timestamp, level, user, action, message);
        } catch (DateTimeParseException e) {
            System.out.println("Error parsing log line: " + logLine);
            return null;  // Или обработайте ошибку по-другому
        }
    }
}
