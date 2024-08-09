package org.example.model;

import lombok.*;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int orderId;
    private int userId;
    private int carId;
    private LocalDate date;
    private String status;

    public Order(int userId, int carId, LocalDate date, String status) {
        this.userId = userId;
        this.carId = carId;
        this.date = date;
        this.status = status;
    }
}
