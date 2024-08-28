package org.example.web;

import org.example.dto.CarDTO;
import org.example.mapper.CarMapper;
import org.example.model.Car;
import org.example.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/cars", produces = MediaType.APPLICATION_JSON_VALUE)
public class CarController {
    private final CarService service;
    public CarController(CarService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CarDTO>> getAll() {
        List<Car> cars = service.getAll();
        return ResponseEntity.ok(service.getAllDTO(cars));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CarDTO>> getAllAfterFilter(@RequestParam(value = "name-filter", required = false) String nameFilter,
                                                          @RequestParam(value = "params", required = false) String params) {
        List<Car> cars = service.getFilteredCars(nameFilter, params);
        return ResponseEntity.ok(service.getAllDTO(cars));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getById(@PathVariable int id) {
        Car carById = service.getById(id);
        return ResponseEntity.ok(CarMapper.INSTANCE.getCarDTO(carById));
    }

    @PostMapping
    public ResponseEntity<Car> create(@RequestBody CarDTO carDTO) {
        if (service.isCarValidation(carDTO, 0)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<Car> update(@RequestBody CarDTO carDTO, @RequestParam(value = "id", required = false) int id) {
        if (service.isCarValidation(carDTO, id)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<Void> delete(@RequestParam(value = "id", required = false) int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
