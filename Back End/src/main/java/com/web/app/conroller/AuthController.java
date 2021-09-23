package com.web.app.conroller;

import java.util.stream.Collectors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.app.entity.ERole;
import com.web.app.entity.Role;
import com.web.app.entity.User;
import com.web.app.payload.request.LoginRequest;
import com.web.app.payload.request.SignupRequest;
import com.web.app.payload.response.JwtResponse;
import com.web.app.payload.response.MessageResponse;
import com.web.app.repository.RoleRepository;
import com.web.app.security.JwtUtils;
import com.web.app.security.MyUserDetails;
import com.web.app.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value="/api/auth")
public class AuthController {

	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncode;
	
	@Autowired
	private RoleRepository roleRepo;
	
	 @Autowired
	 private AuthenticationManager authenticationManager;
	
	@PostMapping(value="/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception{
		
			try {
			
			Authentication auth = 
			        authenticationManager.authenticate(
			            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
			        );

			SecurityContextHolder.getContext().setAuthentication(auth);
			
			String jwt = this.jwtUtils.generateJwtToken(auth);
			
			MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
			
			List<String> roles = myUserDetails.getAuthorities().stream()
					                 .map(item -> item.getAuthority())
					                 .collect(Collectors.toList());
			
			return ResponseEntity.ok(new JwtResponse(jwt,myUserDetails.getUsername() ,roles));
			
			}catch(Exception e) {
				throw new Exception("Inavalid username or password");
			}
			
	}
	
	@PostMapping(value="/register", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE,
		    produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> register(@RequestBody SignupRequest signupRequest){
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
		Set<Role> roles = new HashSet<>();
		
		if(strRole == null) {
			Role guestRole = roleRepo.findByName(ERole.Guest)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(guestRole);
		}else {
				switch(strRole) {
				   case "Admin" :
					   Role adminRole = roleRepo.findByName(ERole.Admin)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found-1-."));
					   roles.add(adminRole);
					   break;
					   
				   case "Technician" :
					   Role techRole = roleRepo.findByName(ERole.Technician)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found-2-."));
					   roles.add(techRole);
					   break;
					   
				   default:
					   Role guestRole = roleRepo.findByName(ERole.Guest)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found-3-."));
					   roles.add(guestRole);   
				}
			
		}
		
		user.setRoles(roles);
		this.userService.addUser(user);
		
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
	
	
	
	
}