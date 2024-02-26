package br.com.back.end.controller;

import java.util.List;

import br.com.back.end.DTO.UserAccountDTO;
import br.com.back.end.DTO.UserPaymentDTO;
import br.com.back.end.model.transactions.StatusTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.back.end.model.User;
import br.com.back.end.service.UserService;

@CrossOrigin(origins="http://localhost:4200")  
@RestController
@RequestMapping(value = "/users")
public class UserController {
		
	@Autowired
	private UserService userService;

	@GetMapping
	public List<UserAccountDTO> getUsers() {
		return userService.getAllUsers();
	}

	@GetMapping(path = {"/{id}"})
	public UserAccountDTO getUserId(@PathVariable long id) {
		return userService.findIdService(id);
	}
	
	@PostMapping
	public UserAccountDTO addUser(@RequestBody UserAccountDTO user) {
		return userService.addUserService(user);
	}
	
	@DeleteMapping(path = { "/{id}" })
	public ResponseEntity<Object> deleteUser(@PathVariable long id) {
		return userService.deleteUserService(id);
	}
	
	@PutMapping({"/{id}"})
	public ResponseEntity<UserAccountDTO> attUser(@PathVariable Long id, @RequestBody UserAccountDTO userDetails){
		return userService.attUserService(id, userDetails);
	}
	
	@PostMapping(path = {"/{schedulePayment}"})
	private StatusTransaction schedulePayment(@RequestBody UserPaymentDTO user) {
		return userService.schedulePaymentService(user);
	}

	@PostMapping("/login")
	private UserAccountDTO loginUserRemote(@RequestBody UserAccountDTO user){
		return userService.loginUserService(user);
	}
}
