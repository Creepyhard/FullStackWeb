package br.com.back.end.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.back.end.DTO.UserDTO;
import br.com.back.end.DTO.mapper.UserMapper;
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

	private final UserMapper userMapper;
	public UserService(UserRepository userRepository, HistoryTransactionsRepository historyTransactionsRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.historyTransactionsRepository = historyTransactionsRepository;
		this.userMapper = userMapper;
	}

	public List<UserDTO> getAllUsers() {
		return userRepository.findAll().stream().map(userMapper::convertUserToDTO).collect(Collectors.toList());
	}
	public UserDTO addUserService(User user) {
		user.setNumberCC(user.getNumberAccount());
		user.setDigitNumberCC("1");
		user.setNickname(user.getRandomNickName(user.getName()));
		user.setPassword(user.getPasswordEncrypt(user.getPassword()));
		return userMapper.convertUserToDTO(userRepository.save(user));
	}
	
	public UserDTO findIdService(@PathVariable long id) {
		return userRepository.findById(id).map(userMapper::convertUserToDTO)
				.orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id));
	}
	
	public ResponseEntity<Object> deleteUserService(@PathVariable long id) {
		return userRepository.findById(id).map(record -> {
			userRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id));
	}
	
	public ResponseEntity<UserDTO> attUserService(Long id, UserDTO userDetails){
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id + " Confirm the entered data!!"));

		BCrypt.Result result;
		result = BCrypt.verifyer().verify(userDetails.oldPassword().toCharArray(), user.getPassword());
		User user1 = userMapper.convertDTOToUser(userDetails);

		if (result.verified) {
			user.setName(user1.getName());
			user.setPassword(user1.getPasswordEncrypt(user1.getPassword()));
			user.setEmail(user1.getEmail());
/*			user.setOffice(user1.getOffice());
			user.setBirthDate(user1.getBirthDate());*/
			User attUser = userRepository.save(user);
			return ResponseEntity.ok(userMapper.convertUserToDTO(attUser));
		} else {
			return null;
		}
	}

	public UserDTO loginUserService(@RequestBody User user) throws UserNotFoundException {
		String emailT = user.getEmail();
		String passwordT = user.getPassword();
		User userObject = new User();
		BCrypt.Result result = null;
		if(emailT != null && passwordT != null) {
			userObject = userRepository.findEmailPasswordsTest(emailT);
			result = BCrypt.verifyer().verify(passwordT.toCharArray(), userObject.getPassword());
		}
        if (!result.verified || !userObject.getEmail().equals(emailT)) {
            throw new UserNotFoundException();
        } else {
            return userMapper.convertUserToDTO(userObject);
        }
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
