package br.com.back.end.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.back.end.model.transactions.HistoryTransactions;
import br.com.back.end.model.transactions.StatusTransaction;
import br.com.back.end.model.transactions.TaxTransfer;
import br.com.back.end.repository.HistoryTransactionsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.back.end.exception.ResourceNotFoundException;
import br.com.back.end.exception.UserNotFoundException;
import br.com.back.end.model.User;
import br.com.back.end.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	private final HistoryTransactionsRepository historyTransactionsRepository;

	public UserService(UserRepository userRepository, HistoryTransactionsRepository historyTransactionsRepository) {
		this.userRepository = userRepository;
		this.historyTransactionsRepository = historyTransactionsRepository;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	public User addUserService(User user) {
		user.setNumberCC(user.getNumberAccount());
		user.setDigitNumberCC("1");
		user.setNickname(user.getRandomNickName(user.getName()));
		user.setPassword(user.getPasswordEncrypt(user.getPassword()));
		return userRepository.save(user);
	}
	
	public ResponseEntity<User> findIdService(@PathVariable long id) {
		return userRepository.findById(id).map(record -> ResponseEntity.ok().body(record))
				.orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id));
	}
	
	public ResponseEntity<Object> deleteUserService(@PathVariable long id) {
		return userRepository.findById(id).map(record -> {
			userRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id));
	}
	
	public ResponseEntity<User> attUserService(Long id, User userDetails){
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id + " Confirm the entered data!!"));

		BCrypt.Result result = null;
		String passwordDB = userDetails.getPassword();
		String oldPassword = userDetails.getOldPassword();

		result = BCrypt.verifyer().verify(oldPassword.toCharArray(), passwordDB);

		if (result.verified) {
			user.setName(userDetails.getName());
			user.setPassword(userDetails.getPassword());
			user.setEmail(userDetails.getEmail());
			user.setOffice(userDetails.getOffice());
			user.setBirthDate(userDetails.getBirthDate());
			User attUser = userRepository.save(user);
			return ResponseEntity.ok(attUser);
		} else {
			return null;
		}
	}
	
	public int loginUserService(@RequestBody User user) throws UserNotFoundException {
		String emailT = user.getEmail();
		String passwordT = user.getPassword();
		int userObject = 0;
		if(emailT != null && passwordT != null) {
			userObject = userRepository.findEmailPasswordsUser(emailT, passwordT);
		}
		if(userObject == 0) {
			throw new UserNotFoundException();
		}
		return userObject;
	}

	public StatusTransaction schedulePaymentService(User transaction) {
		User destionationUser = new User();
		User userOrigin = new User();
		BigDecimal value = transaction.getValue();
		BigDecimal totalTax = null;
		destionationUser = userRepository.returnAccount(transaction.getDestinationAccount());
		userOrigin = userRepository.returnAccount(transaction.getNumberCC());
		if (destionationUser == null) {
			throw new ResourceNotFoundException("The target account could not be found");
		}
		int qtdDays = transaction.getDiffDays(String.valueOf(transaction.getDateTransfer()));
		BigDecimal balance = userOrigin.getBalance();
		TaxTransfer txt = new TaxTransfer();
		txt = userRepository.returnsRateReferentToDay(qtdDays);
		try {
			BigDecimal ratePercentage = txt.getRatePercentage().divide(new BigDecimal(100));
			BigDecimal fixValue = txt.getFixValue();
			totalTax = value.multiply(ratePercentage).add(fixValue).setScale(2, RoundingMode.HALF_EVEN);
			value = value.add(totalTax);
			if (balance.compareTo(value) >= 0) {
				userOrigin.setBalance(balance.subtract(value));
				userOrigin.setBlockedBalance(value);
				User updateUserOrigin = userRepository.save(userOrigin);
				ResponseEntity.ok(updateUserOrigin);
				HistoryTransactions ht = new HistoryTransactions();
				ht.setIdUserCO(userOrigin);
				ht.setIdUserCD(destionationUser);
				ht.setStatus(StatusTransaction.SCHEDULE);
				ht.setTax(totalTax);
				ht.setValue(value);
				ht.setDateSchedule(ht.getDateSchedule());
				ht.setDateTransfer(transaction.getDateTransfer());
				historyTransactionsRepository.save(ht);
			} else {
				throw new ResourceNotFoundException("Insufficient balance");
			}
		} catch (ResourceNotFoundException s){
			HistoryTransactions htError = new HistoryTransactions();
			htError.setIdUserCD(userOrigin);
			htError.setIdUserCO(destionationUser);
			htError.setStatus(StatusTransaction.DENIED);
			htError.setTax(totalTax);
			htError.setValue(value);
			htError.setDateSchedule(htError.getDateSchedule());
			htError.setDateTransfer(userOrigin.getDateTransfer());
			historyTransactionsRepository.save(htError);
			return StatusTransaction.DENIED;
		}
		return StatusTransaction.SCHEDULE;
	}
}
