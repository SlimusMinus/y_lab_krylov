package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.Order;
import org.example.model.Roles;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "поле имя не должно быть пустым")
    private String name;
    @Min(value = 18, message = "минимальный возраст 18 лет")
    @Max(value = 120, message = "максимальный возраст 120 лет")
    private int age;
    @NotBlank(message = "поле город не должно быть пустым")
    private String city;
    private Set<Roles> role;
    private List<Order> orders;
}
