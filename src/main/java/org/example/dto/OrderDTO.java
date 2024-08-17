package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private static final int CURRENT_YEAR = 2024;
    @Positive(message = "userId должен быть положительным числом")
    private int userId;
    @Positive(message = "carId должен быть положительным числом")
    private int carId;
    @PastOrPresent(message = "Год должен быть не больше текущего года")
    private LocalDate date;
    @NotBlank(message = "поле статус не должно быть пустым")
    private String status;
}
