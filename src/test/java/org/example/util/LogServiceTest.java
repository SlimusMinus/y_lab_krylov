package org.example.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.dataTest.Logs.*;

class LogServiceTest {

    private LogService logService = new LogService("log/y_lab.log");

    LogServiceTest() throws IOException {
    }

    @Test
    void filterByDate() {
        List<LogEntry> dateFilteredLogs = logService.filterByDate(startDate, endDate);
        assertThat(dateFilteredLogs.size()).isEqualTo(SIZE_LOG_FILTERED_DATE);
    }
}