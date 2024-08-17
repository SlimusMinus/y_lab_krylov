package org.example.mapper;

import org.example.dto.CarDTO;
import org.example.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);
    CarDTO getCar(Car car);
    Car getCarDTO(CarDTO carDTO);
}
