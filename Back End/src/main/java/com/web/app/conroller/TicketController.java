package com.web.app.conroller;

import java.time.LocalDateTime;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.app.entity.Message;
import com.web.app.entity.Product;
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
@RequestMapping(value = "/api/ticket")
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

	/*
	 * =============================================================================
	 * ================== Guest Part *
	 * =============================================================================
	 * ===================
	 */
	/* Get All Tickets of Existing Guest */
	@GetMapping(value = "/all")
	public ResponseEntity<?> allGuestTickets(@RequestParam("authorization") String token) {
		try {

			String username = this.jwtUtils.getUserNameFromJwtToken(token);
			User guest = this.userService.findUserByUsername(username);
			List<Ticket> tickets = this.ticketService.allTickets().stream()
					.filter(ticket -> ticket.getGuest().getId().equals(guest.getId())).collect(Collectors.toList());
			return ResponseEntity.ok(tickets);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: This ticket is null !!!"));
		}
	}

	/* Add a new ticket by existing guest */
	@PostMapping(value = "/add", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addNewTicket(@RequestBody TicketRequest ticketRequest,
			@RequestHeader("Authorization") String token) {
		try {

			if (ticketRequest == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: This ticket is null !!!"));
			}

			// Get Data from ticket request
			Product product = this.productService.getProductById(ticketRequest.getProductId());
			String username = this.jwtUtils.getUserNameFromJwtToken(token);
			User user = this.userService.findUserByUsername(username);
			// Create a new ticket
			Ticket ticket = new Ticket();
			ticket.setState(false); // Unassign by default
			ticket.setStatus(true); // Open by default
			ticket.setProduct(product);
			ticket.setDescription(ticketRequest.getDescription());
			ticket.setOpenDate(ticketRequest.getOpenDate());
			ticket.setGuest(user);
			this.ticketService.addTicket(ticket);

			// Create a new message
			if (ticketRequest.getMessage() != null || ticketRequest.getMessage() != "") {
				Message message = new Message();
				message.setCreatedOn(ticketRequest.getOpenDate());
				message.setSender(ticket.getGuest());
				message.setTicket(ticket);
				message.setContent(ticketRequest.getMessage());
				this.messageService.addNewMessage(message);
			}
			return ResponseEntity.ok(new MessageResponse("Ticket Added successfully!"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Registered failed, try again."));
		}
	}

	@GetMapping(value = "/get")
	public ResponseEntity<TicketResponse> getTicket(@RequestParam("authorization") String token,
			@RequestParam("ticketId") Long ticketId) {
		try {
			User user = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			TicketResponse ticketResp = new TicketResponse();
			if (user != null) {
				Ticket ticket = this.ticketService.getTicketyId(ticketId);
				ticketResp.setId(ticket.getId());
				ticketResp.setStatus((ticket.getStatus() ? "Open" : "Close"));
				ticketResp.setOpenDate(ticket.getOpenDate());
				ticketResp.setDescription(ticket.getDescription());
				String productName = this.productService.getProductById(ticket.getProduct().getId()) == null
						? "Was deleted"
						: ticket.getProduct().getName().toString();
				ticketResp.setProductName(productName);
				List<Message> messages = this.messageService.getMessagesByTicket(ticket);
				ticketResp.setMessages(messages);
			}
			return new ResponseEntity<TicketResponse>(ticketResp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<TicketResponse>(HttpStatus.BAD_REQUEST);
		}
	}

	/* Delete Ticket */
	@DeleteMapping(value = "/delete")
	public ResponseEntity<?> deleteTicket(@RequestParam("authorization") String token,
			@RequestParam("ticketId") Long ticketId) {
		try {
			User user = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			//System.out.println("====>Delete " + user.getEmail());
			if (user != null) {
				System.out.println("==========> message: 1");
				List<Message> messages = this.messageService.getAllMessages().stream()
						                     .filter(msg -> msg.getTicket().getId().equals(ticketId))
						                     .collect(Collectors.toList());
				System.out.println("==========> message: 2");
				for(Message message : messages) {
					System.out.println("==========> message:==> "+message.getId());
				}
				System.out.println("==========> message: 3");
				if(messages != null){
					for(Message message : messages) {
						this.messageService.deleteMessage(message.getId());
					}
				}
				System.out.println("==========> message: 4");
				this.ticketService.delelteTicket(ticketId);
			}
			return ResponseEntity.ok(new MessageResponse("Delete Ticket Successfully."));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Delete Operation Faild, Try Again!"));
		}
	}

	/* Send Message from Guest or Technician */
	@GetMapping(value = "/sendMessage")
	public ResponseEntity<?> sendMessage(@RequestParam("token") String token, @RequestParam("ticketId") Long ticketId,
			@RequestParam("message") String content) {
		try {
			if (content != null || content != "") {
				User sender = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
				Ticket ticket = this.ticketService.getTicketyId(ticketId);
				LocalDateTime today = LocalDateTime.now();
				Message message = new Message(today, content, sender, ticket);
				this.messageService.addNewMessage(message);
			}

			return ResponseEntity.ok(new MessageResponse("Sent Message Successfully."));
		} catch (Exception e) {
			System.out.println("======>" + e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("SomeThing went Wrong Operation Failed!"));
		}
	}

	/*
	 * =============================================================================
	 * ================== Technician Part *
	 * =============================================================================
	 * ===================
	 */

	/* Getting No Assign tickets */
	@GetMapping(value = "/unassignTickets")
	public ResponseEntity<?> getUnAssignTickets(@RequestHeader("Authorization") String token) {
		try {
			User tech = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			List<Ticket> unAssignTickets = null;
			if (tech != null) {
				unAssignTickets = this.ticketService.allTickets().stream().filter(ticket -> !ticket.getState())
						.collect(Collectors.toList());
			}
			return ResponseEntity.ok(unAssignTickets);
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Empty List. There is no ticket of type unassign."));
		}
	}

	/* Getting Assign tickets */
	@GetMapping(value = "/assignTickets")
	public ResponseEntity<?> getAssignTickets(@RequestHeader("Authorization") String token) {
		try {
			User tech = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			for (Ticket ticket : this.ticketService.allTicketsOfTech(tech)) {
				System.out.println("======> Assign Tickets :" + ticket.getId());
			}
			return ResponseEntity.ok(this.ticketService.allTicketsOfTech(tech));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Empty List. There is no ticket of type ansign."));
		}
	}

	/* Assign Or Unassign ticket to Technician */
	@GetMapping(value = "/assign/{id}")
	public ResponseEntity<?> assignTicket(@RequestParam("token") String token,
			@PathVariable("id") Long ticketId) {
		try {
			User tech = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			Ticket ticket = this.ticketService.getTicketyId(ticketId);
			if (tech != null && tech.getRole().getName().toString().equalsIgnoreCase("Technician")) {
				if (!ticket.getState()) {
					ticket.setTechnician(tech);
					ticket.setState(true);
				} else if(ticket.getState()) {
					ticket.setTechnician(null);
					ticket.setState(false);
					
				}
				this.ticketService.addTicket(ticket);
			}
			return ResponseEntity.ok(ticket);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Operation Failed !"));
		}
	}

	/* Edit Status Of Assign Ticket */
	@GetMapping(value = "/changeStatus")
	public ResponseEntity<Ticket> editAssignTicket(@RequestHeader("Authorization") String token,
			@RequestParam("ticketId") Long ticketId, @RequestParam("ticketStatus") String status) {
		try {
			User tech = this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token));
			Boolean ticketStatus = status.toLowerCase().equals("open") ? true : false;
			Ticket ticket = this.ticketService.allTicketsOfTech(tech).stream()
					.filter(tick -> tick.getId().equals(ticketId)).findAny().get();
			ticket.setStatus(ticketStatus);
			this.ticketService.addTicket(ticket);
			return new ResponseEntity<Ticket>(ticket, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Ticket>(HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * =============================================================================
	 * ================== Administrator Part *
	 * =============================================================================
	 * ===================
	 */

	/* Getting All Tickets With Exceptions */

	@GetMapping(value = "allTickets")
	public ResponseEntity<List<Ticket>> getAllTickets(@RequestHeader("Authorization") String token) {
		try {
			List<Ticket> tickets = null;
			if (this.userService.findUserByUsername(this.jwtUtils.getUserNameFromJwtToken(token)) != null) {
				tickets = this.ticketService.allTickets();
			}
			return new ResponseEntity<List<Ticket>>(tickets, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<Ticket>>(HttpStatus.BAD_REQUEST);
		}

	}

}