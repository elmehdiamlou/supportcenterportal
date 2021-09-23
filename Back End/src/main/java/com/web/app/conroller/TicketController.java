package com.web.app.conroller;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.app.entity.Message;
import com.web.app.entity.Product;
import com.web.app.entity.Role;
import com.web.app.entity.Ticket;
import com.web.app.entity.User;
import com.web.app.payload.request.TicketRequest;
import com.web.app.payload.response.MessageResponse;
import com.web.app.payload.response.TicketResponse;
import com.web.app.security.JwtUtils;
import com.web.app.service.MessageService;
import com.web.app.service.ProductService;
import com.web.app.service.TicketService;
import com.web.app.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value="/api/ticket")
public class TicketController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private JwtUtils jwtUtils;

	/* Guest */

	@GetMapping(value="/all")
	public ResponseEntity<?> allGuestTickets(@RequestHeader("Authorization") String token){
		try {
			try {
				String username = this.jwtUtils.getUserNameFromJwtToken(token);
				User guest = this.userService.findUserByUsername(username);
				List<Ticket> tickets = new ArrayList<>();
				for(Ticket ticket: this.ticketService.allTicketsOfGuest(guest)) {
					if(this.productService.getProductById(ticket.getProduct().getId()) == null) {
						ticket.setProduct(null);
						this.ticketService.addTicket(ticket);
						tickets.add(ticket);
					}else {
						tickets.add(ticket);
					}
				}
				return ResponseEntity.ok(tickets);
			}catch(NullPointerException e) {
				System.out.println("NullPointerException thrown!");
				return ResponseEntity.badRequest().body(new MessageResponse("Error: NullPointerException thrown!"));
			}
			
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: This ticket is null !!!"));
		}
	}
	
	@PostMapping(value="/add",consumes = MimeTypeUtils.APPLICATION_JSON_VALUE,
		    produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addNewTicket(@RequestBody TicketRequest ticketRequest, @RequestHeader("Authorization") String token){
		try {
			
			if(ticketRequest == null) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: This ticket is null !!!"));
			}
			
			//Get Data from ticket request	
			Product product = this.productService.getProductById(ticketRequest.getProductId());
			String username = this.jwtUtils.getUserNameFromJwtToken(token);
			User user = this.userService.findUserByUsername(username);
			//Create a new ticket
			Ticket ticket = new Ticket();
			ticket.setState(false);
			ticket.setStatus(true);
			ticket.setProduct(product);
			ticket.setDescription(ticketRequest.getDescription());
			ticket.setOpenDate(ticketRequest.getOpenDate());
			ticket.setGuest(user);
			this.ticketService.addTicket(ticket);
			
			// Create a new message
			if(ticketRequest.getMessage() != null) {
				Message message =  new Message();
				message.setCreatedOn(ticketRequest.getOpenDate());
				message.setSender(user);
				message.setTicket(ticket);
				message.setContent(ticketRequest.getMessage());
				this.messageService.addNewMessage(message);
			}
			return ResponseEntity.ok(new MessageResponse("Ticket Added successfully!"));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Registered failed, try again."));
		}
	}
	
	@GetMapping(value="/get")
	public ResponseEntity<TicketResponse> getTicket(@RequestHeader("Authorization") String token, @RequestParam("ticketId") String ticketId){
		try {
			Ticket ticket  = null;
			String userName = this.jwtUtils.getUserNameFromJwtToken(token);
			User user = this.userService.findUserByUsername(userName);
			Role role = user.getRoles().stream().findAny().get();
			
			switch(role.getName().toString()) {
			    case "Admin":{
				    ticket = this.ticketService.allTickets().stream()
			                    .filter(teck -> teck.getId().equals(Long.parseLong(ticketId)))
			                    .findAny().get();
				    
				    break;
			      }
			   case "Technician":{
			    	ticket = this.ticketService.allTicketsOfTech(user).stream()
			                     .filter(teck -> teck.getId().equals(Long.parseLong(ticketId)))
			                     .findAny().get();
			    	
				    break;
			      }
			   default:{
				    ticket = this.ticketService.allTicketsOfGuest(user).stream()
			                     .filter(teck -> teck.getId().equals(Long.parseLong(ticketId)))
			                     .findAny().get();
				 
				    break;
			      }
			}
			
			TicketResponse ticketResp = new TicketResponse();
			ticketResp.setId(ticket.getId());
			ticketResp.setStatus((ticket.getStatus()?"Open":"Close"));
			ticketResp.setOpenDate(ticket.getOpenDate());
			ticketResp.setDescription(ticket.getDescription());
			ticketResp.setProductName(ticket.getProduct().getName());
			List<Message> messages = this.messageService.getMessagesByTicket(ticket);
			ticketResp.setMessages(messages);
		
			return new ResponseEntity<TicketResponse>(ticketResp , HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<TicketResponse>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping(value="/delete")
	public ResponseEntity<?> deleteTicket(@RequestHeader("Authorization") String token, @RequestParam("ticketId") String ticketId){
		try {
			User user = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			Ticket ticket = this.ticketService.allTicketsOfGuest(user).stream()
					              .filter(tick -> tick.getId().equals(Long.parseLong(ticketId)))
					              .findAny().get();
			String msg = "";
			if(ticket != null) {
				this.ticketService.delelteTicket(ticket.getId());
				msg = "Delete Ticket Successfully.";
			}else {
				msg = "Delete Operation Faild, Try Again!";
			}
			return ResponseEntity.ok(new MessageResponse(msg));
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Delete Operation Faild, Try Again!"));
		}
	}
	
	@GetMapping(value="/sendMessage")
	public ResponseEntity<?> sendMessage(@RequestParam("token") String token, 
			                @RequestParam("ticketId") Long ticketId,
			                @RequestParam("message") String content) {
		try {
			User sender = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			System.out.println("=======> "+sender.getUserName());
			Ticket ticket = this.ticketService.getTicketyId(ticketId);
			LocalDateTime today = LocalDateTime.now();
			Message message = new Message(today, content, sender, ticket);
			this.messageService.addNewMessage(message);
			
			return ResponseEntity.ok(new MessageResponse("Sent Message Successfully."));
		}catch(Exception e) {
			System.out.println("======>" +e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("SomeThing went Wrong Operation Failed!"));
		}
	}
	
	/* Technician */

	@GetMapping(value="/unassignTickets")
	public ResponseEntity<?> getUnAssignTickets(@RequestHeader("Authorization") String token){
		try {
			User tech = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			List<Ticket> unAssignTickets = null ;
			if(tech != null) {
				unAssignTickets = this.ticketService.allTickets().stream()
	                       .filter(ticket -> !ticket.getState())
	                       .collect(Collectors.toList());
			}
			return ResponseEntity.ok(unAssignTickets);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Empty List. There is no ticket of type unassign."));
		}
	}
	
	@GetMapping(value="/assignTickets")
	public ResponseEntity<?> getAssignTickets(@RequestHeader("Authorization") String token){
		try {
			User tech = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			List<Ticket> assignTickets = this.ticketService.allTicketsOfTech(tech).stream()
	                       .collect(Collectors.toList());
			return ResponseEntity.ok(assignTickets);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Empty List. There is no ticket of type ansign."));
		}
	}
	
	@GetMapping(value="/assign")
	public ResponseEntity<?> assignTicket(@RequestHeader(value="Authorization") String token, @RequestParam(value="ticketId") Long ticketId){
		try {
			User tech = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			Ticket ticket = new Ticket();
			if(tech.getRoles().contains("Technician")) {
				ticket = this.ticketService.getTicketyId(ticketId);
				if(!ticket.getState()) {
					ticket.setTechnician(tech);
					ticket.setState(true);
					this.ticketService.addTicket(ticket);
				}else if(ticket.getState()) {
					ticket.setTechnician(null);
					ticket.setState(false);
					this.ticketService.addTicket(ticket);
				}
				
			}
			return ResponseEntity.ok(ticket);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Operation Failed !"));
		}
	}
	
	@GetMapping(value="/changeStatus")
	public ResponseEntity<Ticket> editAssignTicket(@RequestHeader("Authorization") String token,
			                                       @RequestParam("ticketId") String ticketId,
			                                       @RequestParam("ticketStatus") String status){
		try {
			User tech = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			Boolean ticketStatus = status.toLowerCase().equals("open")?true:false;
			Ticket ticket = this.ticketService.allTicketsOfTech(tech).stream()
					             .filter(tick -> tick.getId().equals(Long.parseLong(ticketId)))
					             .findAny().get(); 
			ticket.setStatus(ticketStatus);
			this.ticketService.addTicket(ticket);
			return new ResponseEntity<Ticket>(ticket, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<Ticket>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	/* Admin */
	
	@GetMapping(value="allTickets")
	public ResponseEntity<List<Ticket>> getAllTickets(@RequestHeader("Authorization") String token){
		try {
			List<Ticket> tickets = null;
			if(this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token)) != null) {
				tickets = this.ticketService.allTickets();
			}
			return new ResponseEntity<List<Ticket>>(tickets, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<List<Ticket>>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	
	
	
	
}