package com.web.app.service;

import java.util.List;

import com.web.app.entity.Ticket;
import com.web.app.entity.User;

public interface TicketService {

	Ticket addTicket(Ticket ticket);
	
	void delelteTicket(Long ticketId);
	
	Ticket assignTicketToTech(Long ticketId, Long techId);
	
	Ticket getTicketyId(Long ticketId);
	
	List<Ticket> allTickets();
	
	List<Ticket> allTicketsOfGuest(User guest);
	
	List<Ticket> allTicketsOfTech(User tech);
}