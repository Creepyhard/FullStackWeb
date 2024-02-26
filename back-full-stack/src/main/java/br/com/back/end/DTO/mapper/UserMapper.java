package br.com.back.end.DTO.mapper;

import br.com.back.end.DTO.UserAccountDTO;
import br.com.back.end.DTO.UserPaymentDTO;
import br.com.back.end.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserAccountDTO convertUserToAccountDTO(User user) {
        return new UserAccountDTO(user.getId(), user.getName(),  user.getNickname(), user.getEmail(),
                user.getPassword(), user.getOldPassword(), user.getNumberCC(), user.getDigitNumberCC(), user.getBalance());
    }

    public User convertAccountDTOToUser(UserAccountDTO userAccountDto) {
        return new User.UserBuilder()
                .id(userAccountDto.id())
                .name(userAccountDto.name())
                .nickname(userAccountDto.nickname())
                .email(userAccountDto.email())
                .password(userAccountDto.password())
                .build();
    }

    public UserPaymentDTO convertUserToPaymentDTO(User user) {
        return new UserPaymentDTO(user.getId(), user.getBalance(), user.getValue(), user.getBlockedBalance(),
                user.getDestinationAccount(), user.getNumberCC(), user.getDateTransfer());
    }

    public User convertPaymentDTOToUser(UserPaymentDTO userPaymentDTO) {
        return new User.UserBuilder()
                .id(userPaymentDTO.id())
                .balance(userPaymentDTO.balance())
                .blockedBalance(userPaymentDTO.blockedBalance())
                .destinationAccount(userPaymentDTO.destinationCC())
                .numberCC(userPaymentDTO.originCC())
                .build();
    }
}
