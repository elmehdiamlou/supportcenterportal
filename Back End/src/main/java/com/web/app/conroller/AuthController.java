package com.web.app.conroller;




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
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncode;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	/* Login */
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
			
			String role = myUserDetails.getAuthorities().stream()
	                 .map(item -> item.getAuthority())
	                 .findFirst().get();
			
			return ResponseEntity.ok(new JwtResponse(jwt,myUserDetails.getUsername(), role));
			
			}catch(Exception e) {
				throw new Exception("Inavalid username or password");
			}
			
	}
	
	
	/* Register a new user */
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
		
		// Create new user's account
		User user = new User(signupRequest.getFirstname(), 
				             signupRequest.getLastname(), 
				             signupRequest.getUsername(), 
				             signupRequest.getEmail(),
				             signupRequest.getPhone(),
				             this.passwordEncode.encode(signupRequest.getPassword()));
		
		user.setRole(this.roleRepo.findByName(ERole.Guest));
		this.userService.addUser(user);
		
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
	
	
	
	
}