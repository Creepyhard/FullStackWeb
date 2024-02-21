package br.com.back.end.DTO.mapper;

import br.com.back.end.DTO.UserDTO;
import br.com.back.end.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO convertUserToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getBalance());
    }
}
