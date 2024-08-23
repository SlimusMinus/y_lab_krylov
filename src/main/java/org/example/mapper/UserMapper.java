package org.example.mapper;

import org.example.dto.UserDTO;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Интерфейс для преобразования (маппинга) объектов типа {@link User} в объекты типа {@link UserDTO} и обратно.
 * <p>
 * Этот интерфейс используется для автоматического создания маппера с помощью библиотеки MapStruct.
 * </p>
 *
 * <p>
 * Аннотации и поля:
 * <ul>
 *     <li>{@code @Mapper} — аннотация, указывающая, что данный интерфейс является маппером для MapStruct.</li>
 *     <li>{@code INSTANCE} — статическое поле, содержащее экземпляр автоматически сгенерированного класса-мэппера.</li>
 * </ul>
 * </p>
 */
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User getUser(UserDTO userDTO);
    UserDTO getUserDTO(User user);

}
