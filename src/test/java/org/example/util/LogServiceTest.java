package org.example.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.dataTest.Logs.*;

/**
 * Тестовый класс для проверки функциональности {@code LogService}.
 * <p>
 * Данный класс содержит тесты, которые проверяют корректность фильтрации логов по дате
 * с использованием метода {@code filterByDate}.
 * </p>
 */
@DisplayName("Тестирования класса LogService на")
class LogServiceTest {

    private final LogService logService = new LogService("log/y_lab.log");

    LogServiceTest() throws IOException {
    }

    /**
     * Тест для метода {@code filterByDate}, проверяющий правильность фильтрации логов по дате.
     * <p>
     * Этот тест проверяет, что метод возвращает правильное количество записей лога,
     * соответствующих указанному диапазону дат.
     * </p>
     */
    @Test
    @DisplayName("фильтрацию логов по дате")
    void filterByDate() {
        List<LogEntry> dateFilteredLogs = logService.filterByDate(startDate, endDate);
        assertThat(dateFilteredLogs.size()).isEqualTo(SIZE_LOG_FILTERED_DATE);
    }
}