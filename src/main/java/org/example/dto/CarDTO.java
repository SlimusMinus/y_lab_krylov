package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {
    private static final int CURRENT_YEAR = 2024;
    @NotBlank(message = "поле бренд не должно быть пустым")
    private String brand;
    @NotBlank(message = "поле модель не должно быть пустым")
    private String model;
    @Max(value = CURRENT_YEAR, message = "год выпуска не должен быть больше текущего года")
    private int year;
    @Positive(message = "Цена должна быть положительным числом")
    private double price;
    @NotBlank(message = "поле состояние не должно быть пустым")
    private String condition;
}
