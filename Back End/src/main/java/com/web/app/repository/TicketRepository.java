package com.web.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.app.entity.Ticket;
import com.web.app.entity.User;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	List<Ticket> findAllTicketByGuest(User guest);
	
	List<Ticket> findAllTicketByTechnician(User Technician);
}