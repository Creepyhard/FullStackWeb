package br.com.back.end.model;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private Timestamp inclusion;
	@Column
	private int userInclusion;
	@Column
	private Timestamp alteration;
	@Column
	private Timestamp userAlteration;
	@Column(nullable = false)
	private String name;
	@Column
	private String nickname;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(unique = true)
	private String cpf;
	@Column(unique = true)
	private String numberCC;
	@Column(length = 1)
	private String digitNumberCC;
	@Column
	private BigDecimal balance;
	@Column
	private BigDecimal blockedBalance;
	@Column(name = "birth_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
	private Date birthDate;
	@Column
	private int userActive;

	@Transient
	private BigDecimal value;
	@Transient
	private Date dateTransfer;
	@Transient
	private String destinationAccount;
	@Transient
	private String oldPassword;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_office")
	private Office office;

	public int getDiffDays(String dateTransfer) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(new java.util.Date());
		int maxDay = cal.getActualMaximum( Calendar.DAY_OF_MONTH );

		String year = dateTransfer.substring(0,4);
		String month = dateTransfer.substring(5,7);
		String day = dateTransfer.substring(8,10);

		LocalDate ld = LocalDate.now();
		int qtdDays = 0;
		if(ld.getMonthValue() != Integer.parseInt(month)) {
			int qtdMonth = Integer.parseInt(month) - ld.getMonthValue();
			for (int i = 1; i < qtdMonth; i++) {
				if(qtdMonth > i) {
					cal.add(Calendar.MONTH, i);
					int maxDay2 = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
					qtdDays += maxDay2;
				}
			}
			qtdDays += maxDay - ld.getDayOfMonth() + Integer.parseInt(day);
		} else {
			qtdDays = Integer.parseInt(day) + ld.getDayOfMonth();
		}
		return qtdDays;
	}

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
