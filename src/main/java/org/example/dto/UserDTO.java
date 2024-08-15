package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.Order;
import org.example.model.Roles;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String login;
    private String password;
    private String name;
    private int age;
    private String city;
    private Set<Roles> role;
    private List<Order> orders;
}
