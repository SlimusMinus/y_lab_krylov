package org.example.dataTest;

import java.time.LocalDateTime;

/**
 * Класс {@code Logs} содержит предопределённые значения, связанные с логами и их фильтрацией.
 * <p>
 * Этот класс используется для хранения диапазона дат и времени, в пределах которого ведётся фильтрация логов,
 * а также для хранения предопределённого размера фильтрованных логов.
 * </p>
 */
public class Logs {
    public static final LocalDateTime startDate = LocalDateTime.of(2024, 8, 3, 0, 0);
    public static final LocalDateTime endDate = LocalDateTime.of(2024, 8, 3, 12, 16);

    public static final int SIZE_LOG_FILTERED_DATE = 6;
}
