package com.web.app.payload.request;

import java.time.LocalDateTime;

public class TicketRequest {

	private LocalDateTime openDate;
	private String description;
	private Long productId;
	private String message;
	
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
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}