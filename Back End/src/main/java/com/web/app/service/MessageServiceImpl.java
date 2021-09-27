package com.web.app.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.app.entity.Message;
import com.web.app.entity.Ticket;
import com.web.app.repository.MessageRepository;


@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageRepository messageRepo;

	
	
	@Override
	public Message addNewMessage(Message message) {
	      return this.messageRepo.save(message);
	}

	@Override
	public List<Message> getMessagesByTicket(Ticket ticket) {
		List<Message> messages = this.messageRepo.findAll().stream()
				                  .filter( msg -> ( msg.getTicket().getId().equals(ticket.getId())) )
				                  .sorted(Comparator.comparing(Message::getCreatedOn))
				                  .collect(Collectors.toList());
		return messages;
	}

	@Override
	public void deleteMessage(Long messageId) {
		this.messageRepo.deleteById(messageId);
	}

	@Override
	public List<Message> getAllMessages() {
		return this.messageRepo.findAll();
	}

}