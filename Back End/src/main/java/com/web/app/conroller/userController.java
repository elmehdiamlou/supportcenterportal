package com.web.app.conroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.app.entity.User;
import com.web.app.payload.response.MessageResponse;
import com.web.app.repository.UserRepository;
import com.web.app.service.UserService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value="/api/management")
public class userController {

		
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncode;
		
	@Autowired
	private UserRepository userRepo;	
	
	@GetMapping(value="/all")
	public ResponseEntity<?> allUsers(){
		try {
			try {
				List<User> users = this.userService.allUsers();
				return ResponseEntity.ok(users);
			}catch(NullPointerException e) {
				System.out.println("Null");
				return ResponseEntity.badRequest().body(new MessageResponse("Null"));
			}
			
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Null"));
		}
	}
	
	@GetMapping(value="/edit/{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") Long Id){
		try {
			try {
				User user = this.userService.getUserById(Id);
				return ResponseEntity.ok(user);
			}catch(NullPointerException e) {
				System.out.println("Null");
				return ResponseEntity.badRequest().body(new MessageResponse("Null"));
			}
			
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Null"));
		}
	}
	
	@PutMapping("/save")
	public ResponseEntity<?> saveUser(@RequestBody User user){
		User olduser = this.userService.getUserById(user.getId());
		if(user.getUserName().equals(olduser.getUserName()) == false  && this.userService.findUserByUsername(user.getUserName()) != null) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("User is already registered with this username!"));
		}
		if(user.getEmail().equals(olduser.getEmail()) == false  && this.userService.getUserByEmail(user.getEmail()) != null) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("User is already registered with this email!"));
		}
		if(user.getPassword() == "" || user.getPassword() == null)
		{	
			user = userService.ignoreNullCopy(user, olduser);
			this.userRepo.save(user);
			return ResponseEntity.ok(new MessageResponse("User edited successfully!"));
		}
		else {
			user = userService.ignoreNullCopy(user, olduser);
			user.setPassword(this.passwordEncode.encode(user.getPassword()));
			this.userRepo.save(user);
			return ResponseEntity.ok(new MessageResponse("User edited successfully!"));
		}
	}
	
	@DeleteMapping(value="/delete/{id}")
	public void deleteUser(@PathVariable("id") Long Id){
		try {
			User user = this.userService.getUserById(Id);
		    userRepo.delete(user);
		}catch(NullPointerException e) {
			System.out.println("Not found");
		}
	}

	@PostMapping(value="/adduser", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE,
		    produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> register(@RequestBody User user){
		if(this.userService.findUserByUsername(user.getUserName()) != null) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("User is already registered with this username!"));
		}
		if(( this.userService.getUserByEmail(user.getEmail()) != null)) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("User is already registered with this email!"));
		}
		user.setPassword(this.passwordEncode.encode(user.getPassword()));
		this.userService.addUser(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
}
