package com.web.app.entity;

import java.io.Serializable;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="user_id", nullable = false, updatable = false)
	private Long id;
	private String firstName;
	private String lastName;
	private String userName;
	private String email;
	private String phone;
	private String password;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Role role ;
	
	/* constructors */
	public User() {
		super();
	}
	
	
	public User(String firstName, String lastName, String userName, String email, String phone, String password,
			Role role) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.role = role;
	}


	public User(String firstName, String lastName, String userName, String email, String phone, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.email = email;
		this.phone = phone;
		this.password = password;
	}

	/* Getters and Setters*/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}

	
	
}
