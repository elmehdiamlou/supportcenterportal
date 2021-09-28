package com.web.app.conroller;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.app.entity.ERole;
import com.web.app.entity.Role;
import com.web.app.entity.Ticket;
import com.web.app.entity.User;
import com.web.app.payload.request.SignupRequest;
import com.web.app.payload.response.MessageResponse;
import com.web.app.repository.RoleRepository;
import com.web.app.repository.TicketRepository;
import com.web.app.repository.UserRepository;
import com.web.app.security.JwtUtils;
import com.web.app.service.TicketService;
import com.web.app.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value="/api/management")
public class userController {
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private PasswordEncoder passwordEncode;
		
	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private UserRepository userRepo;	
	
	@Autowired
	private TicketRepository ticketRepo;
	
	@Autowired
	private JwtUtils jwtUtils;

	@GetMapping(value="/role")
	public ResponseEntity<?> getUserRole(@RequestHeader("authorization") String token){
		try {
			String username = this.jwtUtils.getUserNameFromJwtToken(token);
			User user = this.userService.findUserByUsername(username);
			return ResponseEntity.ok(new MessageResponse(user.getRole().getName().toString()));
		} catch(Exception e) {
			throw e;
		}
	}
	
	@PostMapping(value="/adduser", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE,
		    produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> register(@RequestBody SignupRequest signupRequest,@RequestHeader("authorization") String token){
		String username = this.jwtUtils.getUserNameFromJwtToken(token);
		User userAdmin = this.userService.findUserByUsername(username);
		if(userAdmin.getRole().getName().toString() == "Admin")
		{
			if(this.userService.findUserByUsername(signupRequest.getUsername()) != null) {
				return ResponseEntity
					.badRequest()
					.body(new MessageResponse("User is already registered with this username!"));
			}
			if(( this.userService.getUserByEmail(signupRequest.getEmail()) != null)) {
				return ResponseEntity
					.badRequest()
					.body(new MessageResponse("User is already registered with this email !"));
			}
			User user = new User(signupRequest.getFirstname(), 
	             signupRequest.getLastname(), 
	             signupRequest.getUsername(), 
	             signupRequest.getEmail(),
	             signupRequest.getPhone(),
	             this.passwordEncode.encode(signupRequest.getPassword()));
			String strRole = signupRequest.getRole();
			Role role;
			if (strRole == null) {
				role = this.roleRepo.findByName(ERole.Guest);
			} else {
				switch (strRole) {
				case "Admin":
					role = this.roleRepo.findByName(ERole.Admin);
					break;
				case "Technician":
					role = this.roleRepo.findByName(ERole.Technician);
					break;
				default:
					role = this.roleRepo.findByName(ERole.Guest);
				}
			}
			user.setRole(role);
			this.userService.addUser(user);
			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
		} else {
			return ResponseEntity
				.badRequest()
				.body(new MessageResponse("You are unauthorized to add user!")); 
		}
	}
	
	@GetMapping(value="/all")
	public ResponseEntity<?> allUsers(@RequestHeader("authorization") String token){
		String username = this.jwtUtils.getUserNameFromJwtToken(token);
		User userAdmin = this.userService.findUserByUsername(username);
		if(userAdmin.getRole().getName().toString() == "Admin")
		{
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
		} else {
			return ResponseEntity
				.badRequest()
				.body(new MessageResponse("You are unauthorized to get all user!")); 
		}
	}
	
	@GetMapping(value="/edit/{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") Long Id, @RequestHeader("authorization") String token){
		String username = this.jwtUtils.getUserNameFromJwtToken(token);
		User userAdmin = this.userService.findUserByUsername(username);
		if(userAdmin.getRole().getName().toString() == "Admin")
		{
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
		} else {
			return ResponseEntity
				.badRequest()
				.body(new MessageResponse("You are unauthorized to edit user!")); 
		}
	}
	
	@PutMapping("/save")
	public ResponseEntity<?> saveUser(@RequestBody User user, @RequestHeader("authorization") String token){
		String username = this.jwtUtils.getUserNameFromJwtToken(token);
		User userAdmin = this.userService.findUserByUsername(username);
		if(userAdmin.getRole().getName().toString() == "Admin")
		{
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
		} else {
			return ResponseEntity
				.badRequest()
				.body(new MessageResponse("You are unauthorized to update user!")); 
		}
	}
	
	@DeleteMapping(value="/delete/{id}")
	public void deleteUser(@PathVariable("id") Long Id,@RequestHeader("authorization") String token){
		String username = this.jwtUtils.getUserNameFromJwtToken(token);
		User userAdmin = this.userService.findUserByUsername(username);
		if(userAdmin.getRole().getName().toString() == "Admin")
		{
			try {
				User user = this.userService.getUserById(Id);
				if(user.getRole().getName().toString() == "Guest") {
					List<Ticket> tickets = this.ticketService.allTickets().stream()
						.filter(t -> t.getGuest().getId().equals(user.getId()))
						.collect(Collectors.toList());
					for(Ticket ticket : tickets) {
						this.ticketService.delelteTicket(ticket.getId());
					}
				}
				if(user.getRole().getName().toString() == "Technician") {
					List<Ticket> tickets = this.ticketService.allTickets().stream()
						.filter(t -> t.getTechnician().getId().equals(user.getId()))
						.collect(Collectors.toList());
					for(Ticket ticket : tickets) {
						ticket.setTechnician(null);
						ticket.setStatus(true);
						this.ticketRepo.save(ticket);
					}
				}
			    userRepo.delete(user);
			}catch(NullPointerException e) {
				System.out.println("Not found");
			}
		}
	}
}
