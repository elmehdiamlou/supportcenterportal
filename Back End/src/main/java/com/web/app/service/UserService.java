package com.web.app.service;

import java.util.List;

import com.web.app.entity.User;

public interface UserService {

	List<User> allUsers();
	
	User getUserById(Long userId);

	User findUserByUsername(String userName);
	
	User addUser(User user);
	
	void deleteUser(Long userId);
	
	User getUserByEmail(String email);
	
	User ignoreNullCopy(User src, User target);
}
