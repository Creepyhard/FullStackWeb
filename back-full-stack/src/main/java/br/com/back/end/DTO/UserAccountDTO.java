package br.com.back.end.DTO;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.math.BigDecimal;
import java.util.Random;

public record UserAccountDTO(Long id,
                             String name,
                             String nickname,
                             String email,
                             String password,
                             String oldPassword,
                             String numberCC,
                             String digitNumberCC,
                             BigDecimal balance
){

    public String getPasswordEncrypt(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public String getRandomNickName(String fullName) {
        String nameUser = "";
        nameUser = fullName.substring(0,fullName.indexOf(" ")) + String.valueOf(Math.abs(new Random().nextInt()));
        if(nameUser.length() > 20) {
            return nameUser.substring(0,20);
        } else {
            return nameUser;
        }
    }

    public String getNumberAccount() {
        //temporary method
        //take the country and region and generate a number for that region, e.g. "Brazil - São Paulo" = 24856 + 6 random numbers + digit account random

        String numberAccount = String.valueOf(Math.abs(new Random().nextInt())) + "00000000000";
        return numberAccount.substring(0, 11);
    }

}
