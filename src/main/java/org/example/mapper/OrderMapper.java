package org.example.mapper;

import org.example.dto.OrderDTO;
import org.example.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    OrderDTO getOdderDTO(Order order);
    Order getOrder(OrderDTO orderDTO);
}
