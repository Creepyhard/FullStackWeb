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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
	@NotNull @NotBlank @Length(min = 3, max = 20)
	@Column(nullable = false)
	private String name;
	@Column
	private String nickname;
	@NotNull @NotBlank @Length(min = 12, max = 256)
	@Column(nullable = false, unique = true)
	private String email;
	@NotNull @NotBlank @Length(min = 8, max = 60)
	@Column(nullable = false)
	private String password;
	@Length(min = 11, max = 11)
	@Column(unique = true, length = 11)
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

	public User() {

	}

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

	private User(UserBuilder userBuilder) {
		this.id = userBuilder.id;
		this.name = userBuilder.name;
		this.email = userBuilder.email;
		this.password = userBuilder.password;
		this.nickname = userBuilder.nickname;
		this.cpf = userBuilder.cpf;
		this.numberCC = userBuilder.numberCC;
		this.digitNumberCC = userBuilder.digitNumberCC;
		this.balance = userBuilder.balance;
		this.blockedBalance = userBuilder.blockedBalance;
		this.birthDate = userBuilder.birthDate;
		this.userActive = userBuilder.userActive;

    }
	//Joshua Bloch’s
	public static class UserBuilder {
		private Long id;
		private String name;
		private String email;
		private String password;
		private String nickname;
		private String cpf;
		private String numberCC;
		private String digitNumberCC;
		private BigDecimal balance;
		private BigDecimal blockedBalance;
		private Date birthDate;
		private int userActive;
		private BigDecimal value;
		private Date dateTransfer;
		private String destinationAccount;
		private String oldPassword;

		public UserBuilder createUser(String name, String email, String password) {
			this.name = name;
			this.email = email;
			this.password = password;
			return this;
		}

		public UserBuilder attUser(User user) {
			this.id = user.id;
			this.name = user.name;
			this.email = user.email;
			this.password = user.password;
			this.nickname = user.nickname;
			this.cpf = user.cpf;
			this.numberCC = user.numberCC;
			this.digitNumberCC = user.digitNumberCC;
			this.balance = user.balance;
			this.blockedBalance = user.blockedBalance;
			this.birthDate = user.birthDate;
			this.userActive = user.userActive;
			return this;
		}
		public UserBuilder id(Long id) {
			this.id = id;
			return this;
		}
		public UserBuilder name(String name) {
			this.name = name;
			return this;
		}
		public UserBuilder email(String email) {
			this.email = email;
			return this;
		}
		public UserBuilder password(String password) {
			this.password = password;
			return this;
		}
		public UserBuilder nickname(String nickname) {
			this.nickname = nickname;
			return this;
		}
		public UserBuilder cpf(String cpf) {
			this.cpf = cpf;
			return this;
		}
		public UserBuilder numberCC(String numberCC) {
			this.numberCC = numberCC;
			return this;
		}
		public UserBuilder digitNumberCC(String digitNumberCC) {
			this.digitNumberCC = digitNumberCC;
			return this;
		}
		public UserBuilder balance(BigDecimal balance) {
			this.balance = balance;
			return this;
		}
		public UserBuilder blockedBalance(BigDecimal blockedBalance) {
			this.blockedBalance = blockedBalance;
			return this;
		}

		public UserBuilder birthDate(Date birthDate) {
			this.birthDate = birthDate;
			return this;
		}
		public UserBuilder userActive(int userActive) {
			this.userActive = userActive;
			return this;
		}

		public UserBuilder value(BigDecimal value) {
			this.value = value;
			return this;
		}
		public UserBuilder dateTransfer(Date dateTransfer) {
			this.dateTransfer = dateTransfer;
			return this;
		}public UserBuilder destinationAccount(String destinationAccount) {
			this.destinationAccount = destinationAccount;
			return this;
		}
		public UserBuilder oldPassword(String oldPassword) {
			this.oldPassword = oldPassword;
			return this;
		}

		public User build() {
			return new User(this);
		}
	}

}
