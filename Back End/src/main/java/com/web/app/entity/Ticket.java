package com.web.app.entity;

import java.io.Serializable;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tickets")
public class Ticket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ticket_id", nullable = false, updatable = false)
	private Long id;
	private Boolean status;
	private LocalDateTime openDate;
	private String description;
	private Boolean state;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User guest;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User technician;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Product product;
	
	/* Constructor */
	public Ticket() {
		super();
	}

	public Ticket(Boolean status, LocalDateTime openDate, String description, Boolean state, User guest,
			User technician, Product product) {
		super();
		this.status = status;
		this.openDate = openDate;
		this.description = description;
		this.state = state;
		this.guest = guest;
		this.technician = technician;
		this.product = product;
	}



	/* Getters and Setters */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	
	public LocalDateTime getOpenDate() {
		return openDate;
	}

	public void setOpenDate(LocalDateTime openDate) {
		this.openDate = openDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public User getGuest() {
		return guest;
	}

	public void setGuest(User guest) {
		this.guest = guest;
	}

	public User getTechnician() {
		return technician;
	}

	public void setTechnician(User technician) {
		this.technician = technician;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
	
}