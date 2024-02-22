package br.com.back.end.DTO;

import java.math.BigDecimal;

public record UserDTO (Long id,
                       String name,
                       String email,
                       BigDecimal balance,
                       String password,
                       String oldPassword
){

}
