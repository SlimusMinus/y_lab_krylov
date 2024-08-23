package org.example.web;

import org.example.dto.OrderDTO;
import org.example.mapper.OrderMapper;
import org.example.model.Car;
import org.example.model.Order;
import org.example.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAll() {
        List<Order> list = service.getAll();
        return ResponseEntity.ok(service.getAllDTO(list));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<OrderDTO>> getAllAfterFilter(@RequestParam(value = "name-filter", required = false) String nameFilter,
                                                            @RequestParam(value = "params", required = false) String params) {
        List<Order> orders = service.getFilteredOrder(nameFilter, params);
        return ResponseEntity.ok(service.getAllDTO(orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getById(@PathVariable int id) {
        Order orderById = service.getById(id);
        return ResponseEntity.ok(OrderMapper.INSTANCE.getOdderDTO(orderById));
    }

    @PostMapping
    public ResponseEntity<Car> create(@RequestBody OrderDTO orderDTO) {
        if (service.isOrderValidation(orderDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/canceled")
    public ResponseEntity<Order> canceled(@RequestParam(value = "id", required = false) int id) {
        service.canceled(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/change-status")
    public ResponseEntity<Order> changeStatus(@RequestParam(value = "id", required = false) int id,
                                              @RequestParam(value = "status", required = false) String status) {
        service.changeStatus(id, status);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
