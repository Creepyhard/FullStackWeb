package br.com.back.end.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.back.end.DTO.UserAccountDTO;
import br.com.back.end.DTO.UserPaymentDTO;
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

	public List<UserAccountDTO> getAllUsers() {
		return userRepository.findAll().stream().map(userMapper::convertUserToAccountDTO).collect(Collectors.toList());
	}
	public UserAccountDTO addUserService(UserAccountDTO userDTO) {
		User user = new User.UserBuilder()
						.createUser(userDTO.name(),userDTO.email(),userDTO.password())
						.numberCC(userDTO.getNumberAccount())
						.digitNumberCC("1")
						.nickname(userDTO.getRandomNickName(userDTO.name()))
						.password(userDTO.getPasswordEncrypt(userDTO.password()))
						.build();
		return userMapper.convertUserToAccountDTO(userRepository.save(user));
	}
	
	public UserAccountDTO findIdService(@PathVariable long id) {
		return userRepository.findById(id).map(userMapper::convertUserToAccountDTO)
				.orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id));
	}
	
	public ResponseEntity<Object> deleteUserService(@PathVariable long id) {
		return userRepository.findById(id).map(record -> {
			userRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id));
	}
	
	public ResponseEntity<UserAccountDTO> attUserService(Long id, UserAccountDTO userDTODetails){
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id + " Confirm the entered data!!"));
		BCrypt.Result result;
		result = BCrypt.verifyer().verify(userDTODetails.oldPassword().toCharArray(), user.getPassword());
		if (result.verified) {
			user = new User.UserBuilder().attUser(user)
						.name(userDTODetails.name())
						.password(userDTODetails.getPasswordEncrypt(userDTODetails.password()))
						.email(user.getEmail())
						.build();
			User attUser = userRepository.save(user);
			return ResponseEntity.ok(userMapper.convertUserToAccountDTO(attUser));
		} else {
			return null;
		}
	}

	public UserAccountDTO loginUserService(@RequestBody UserAccountDTO userAccountDTO) throws UserNotFoundException {
		String emailT = userAccountDTO.email();
		String passwordT = userAccountDTO.password();
		User userObject = new User();
		BCrypt.Result result = null;
		if(emailT != null && passwordT != null) {
			userObject = userRepository.findEmailPasswordsTest(emailT);
			result = BCrypt.verifyer().verify(passwordT.toCharArray(), userObject.getPassword());
		}
        if (!result.verified || !userObject.getEmail().equalsIgnoreCase(emailT)) {
            throw new UserNotFoundException();
        } else {
            return userMapper.convertUserToAccountDTO(userObject);
        }
    }

	public StatusTransaction schedulePaymentService(UserPaymentDTO transaction) {
		User destionationUser = new User();
		User userOrigin = new User();
		BigDecimal value = transaction.value();
		BigDecimal totalTax = null;
		destionationUser = userRepository.returnAccount(transaction.destinationCC());
		userOrigin = userRepository.returnAccount(transaction.originCC());
		if (destionationUser == null) {
			throw new ResourceNotFoundException("The target account could not be found");
		}
		int qtdDays = userOrigin.getDiffDays(String.valueOf(transaction.dateTransfer()));
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
				ht.setDateTransfer(transaction.dateTransfer());
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
