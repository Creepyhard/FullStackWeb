package br.com.back.end.DTO;

import java.math.BigDecimal;
import java.sql.Date;

public record UserPaymentDTO (
        Long id,
        BigDecimal balance,
        BigDecimal value,
        BigDecimal blockedBalance,
        String destinationCC,
        String originCC,
        Date dateTransfer
) {
}
