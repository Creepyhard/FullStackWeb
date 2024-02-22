package br.com.back.end.DTO.mapper;

import br.com.back.end.DTO.UserDTO;
import br.com.back.end.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO convertUserToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getBalance(), user.getPassword(), user.getOldPassword());
    }

    public User convertDTOToUser(UserDTO userDto) {
        return new User.UserBuilder()
                .id(userDto.id())
                .name(userDto.name())
                .balance(userDto.balance())
                .email(userDto.email())
                .password(userDto.password())
                .build();
    }
}
