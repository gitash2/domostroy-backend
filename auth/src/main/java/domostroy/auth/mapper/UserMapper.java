package domostroy.auth.mapper;

import domostroy.auth.users.model.User;
import domostroy.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTO user);
    UserDTO toDTO(User user);
}



