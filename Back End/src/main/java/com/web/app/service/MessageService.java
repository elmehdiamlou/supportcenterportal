package com.web.app.service;

import java.util.List;

import com.web.app.entity.Message;
import com.web.app.entity.Ticket;

public interface MessageService {

	Message addNewMessage(Message message);
	
	List<Message> getMessagesByTicket(Ticket ticket);
}
