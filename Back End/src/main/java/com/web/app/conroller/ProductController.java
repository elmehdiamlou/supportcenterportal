package com.web.app.conroller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.app.entity.Product;
import com.web.app.entity.Ticket;
import com.web.app.entity.User;
import com.web.app.payload.response.MessageResponse;
import com.web.app.security.JwtUtils;
import com.web.app.service.ProductService;
import com.web.app.service.TicketService;
import com.web.app.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value="/api/product")
public class ProductController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;

	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@GetMapping(value="/all")
	public ResponseEntity<List<Product>> getAllProducts(@RequestHeader("Authorization") String token){
		try {
			User user = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			List<Product> products = (user != null?this.productService.allProducts():null);
			return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<List<Product>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value="/add")
	public ResponseEntity<Product> addNewProduct(@RequestHeader("Authorization") String token, @RequestBody Product product){
		try {
			User admin = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			if(admin != null && product != null) {
				this.productService.addProduct(product);
			}
			return new ResponseEntity<Product>(product, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value="/edit")
	public ResponseEntity<Product> editProduct(@RequestHeader("Authorization") String token, @RequestBody Product product){
		try {
			Product existingPrd = this.productService.getProductById(product.getId());
			if(existingPrd != null){
				existingPrd.setName(product.getName());
				existingPrd.setCategory(product.getCategory());
				existingPrd.setDescription(product.getDescription());
				this.productService.addProduct(existingPrd);
			}
			return new ResponseEntity<Product>(HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value="/get")
	public ResponseEntity<Product> getProduct(@RequestHeader("Authorization") String token, @RequestParam("productId") String id){
		try {
			User user = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			Product product = (user != null?this.productService.getProductById(Long.parseLong(id)):null);
			 return new ResponseEntity<Product>(product, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping(value="/delete")
	public ResponseEntity<?> deleteProduct(@RequestHeader("Authorization") String token, @RequestParam("productId") Long productId){
		try {
			if(this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token)) != null &&
			   this.productService.getProductById(productId) != null) 
			{
				List<Ticket> tickets = this.ticketService.allTickets().stream()
						                      .filter(ticket -> ticket.getProduct().getId().equals(productId))
						                      .collect(Collectors.toList());
				this.productService.deleteProduct(productId);
				for(Ticket ticket : tickets) {
					ticket.setProduct(null);
					this.ticketService.addTicket(ticket);
				}
			}
			return ResponseEntity.ok(new MessageResponse("Delete Product Successfully."));
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Operation Failed Try Again."));
		}
	}
}
