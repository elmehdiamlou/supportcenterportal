package com.web.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.app.entity.Ticket;
import com.web.app.entity.User;
import com.web.app.repository.TicketRepository;
import com.web.app.repository.UserRepository;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketRepository ticketRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	
	@Override
	public Ticket addTicket(Ticket ticket) {
		return this.ticketRepo.save(ticket);
	}

	@Override
	public void delelteTicket(Long ticketId) {
		this.ticketRepo.deleteById(ticketId);
	}

	@Override
	public Ticket assignTicketToTech(Long ticketId, Long techId) {
		Ticket existTicket = this.ticketRepo.findById(ticketId).get();
		User tech = this.userRepo.findById(techId).get();
		existTicket.setTechnician(tech);
		existTicket.setState(true);
		return this.ticketRepo.save(existTicket);
	}

	@Override
	public List<Ticket> allTickets() {
		return this.ticketRepo.findAll();
	}

	@Override
	public Ticket getTicketyId(Long ticketId) {
		return this.ticketRepo.findById(ticketId).get();
	}

	@Override
	public List<Ticket> allTicketsOfGuest(User guest) {
		return this.ticketRepo.findAllTicketByGuest(guest);
	}

	@Override
	public List<Ticket> allTicketsOfTech(User tech) {
		return this.ticketRepo.findAllTicketByTechnician(tech);
	}

}