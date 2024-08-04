package org.example.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Класс, предоставляющий функциональность для работы с записями лога.
 */
public class LogService {
    private List<LogEntry> logEntries;
    /**
     * Создает экземпляр {@link LogService} и загружает записи лога из указанного файла.
     *
     * @param logFilePath путь к файлу с записями лога.
     * @throws IOException если возникает ошибка при чтении файла.
     */
    public LogService(String logFilePath) throws IOException {
        logEntries = Files.lines(Paths.get(logFilePath))
                .map(LogEntry::parse)
                .collect(Collectors.toList());
    }
    /**
     * Фильтрует записи лога на основе предоставленного предиката.
     *
     * @param predicate условие фильтрации.
     * @return список записей лога, удовлетворяющих условию.
     */
    public List<LogEntry> filterLogs(Predicate<LogEntry> predicate) {
        return logEntries.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    /**
     * Фильтрует записи лога по диапазону дат.
     *
     * @param startDate начальная дата диапазона.
     * @param endDate   конечная дата диапазона.
     * @return список записей лога, находящихся в указанном диапазоне дат.
     */
    public List<LogEntry> filterByDate(LocalDateTime startDate, LocalDateTime endDate) {
        return filterLogs(entry -> !entry.getTimestamp().isBefore(startDate) && !entry.getTimestamp().isAfter(endDate));
    }
    /**
     * Фильтрует записи лога по имени пользователя.
     *
     * @param user имя пользователя.
     * @return список записей лога, связанных с указанным пользователем.
     */
    public List<LogEntry> filterByUser(String user) {
        return filterLogs(entry -> entry.getUser().equalsIgnoreCase(user));
    }
    /**
     * Фильтрует записи лога по действию.
     *
     * @param action действие.
     * @return список записей лога, связанных с указанным действием.
     */
    public List<LogEntry> filterByAction(String action) {
        return filterLogs(entry -> entry.getAction().equalsIgnoreCase(action));
    }
}
