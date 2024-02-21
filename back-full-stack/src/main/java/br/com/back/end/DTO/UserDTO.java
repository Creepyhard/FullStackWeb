package br.com.back.end.DTO;

import java.math.BigDecimal;
import java.sql.Date;

public record UserDTO (Long id,
                       String name,
                       String email,
                       BigDecimal balance
){

}
