package br.com.back.end.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private UserRepository userRepository;
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	public User addUserService(@RequestBody User user) {
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
		
		user.setName(userDetails.getName());
		user.setCpf(userDetails.getCpf());
		user.setPassword(userDetails.getPassword());
		user.setOffice(userDetails.getOffice());
		user.setBirthDate(userDetails.getBirthDate());
		
		User attUser = userRepository.save(user);
		return ResponseEntity.ok(attUser);
	}
	
	public User loginUserService(@RequestBody User user) throws UserNotFoundException {
		String emailT = user.getEmail();
		String passwT = user.getPassword();
		User userObject = null;
		if(emailT != null && passwT != null) {
			userObject = userRepository.findEmailPasswordsUser(emailT, passwT);
		}
		if(userObject == null) {
			throw new UserNotFoundException();
		}
		return userObject;
	}
}
