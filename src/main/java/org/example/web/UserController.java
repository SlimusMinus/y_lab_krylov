package org.example.web;

import org.example.dto.UserDTO;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<User> users = service.getAll();
        return ResponseEntity.ok(service.getAllDTO(users));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<UserDTO>> getAllFiltered(@RequestParam(value = "name-filter", required = false) String nameFilter,
                                                        @RequestParam(value = "params", required = false) String params) {
        List<User> users = service.getFilteredUsers(nameFilter, params);
        return ResponseEntity.ok(service.getAllDTO(users));
    }

    @GetMapping("/sort")
    public ResponseEntity<List<UserDTO>> getAllSorted(@RequestParam(value = "params", required = false) String params) {
        List<User> users = service.getSortedUsers(params);
        return ResponseEntity.ok(service.getAllDTO(users));
    }

    @PutMapping
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDTO, @RequestParam(value = "id", required = false) int id) {
        if (service.isCarValidation(userDTO, id)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
