package com.web.app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web.app.entity.User;
import com.web.app.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public List<User> allUsers() {
		return this.userRepo.findAll();
	}
	
	@Override
	public User addUser(User user) {
		return this.userRepo.save(user);
	}
	
	@Override
	public User getUserById(Long userId) {
		return this.userRepo.findById(userId).get();
	}

	@Override
	public void deleteUser(Long userId) {
		this.userRepo.deleteById(userId);
	}
	
	@Override
	public User getUserByEmail(String email) {
		return this.userRepo.findByEmail(email);
	}

	@Override
	public User findUserByUsername(String userName) {
		return this.userRepo.findByuserName(userName);
	}

	public String[] getNullPropertyNames (User source) {
	    final BeanWrapper src = new BeanWrapperImpl(source);
	    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

	    Set<String> emptyNames = new HashSet<String>();
	    for(java.beans.PropertyDescriptor pd : pds) {
	        Object srcValue = (Object) src.getPropertyValue(pd.getName());
	        if (srcValue == null) emptyNames.add(pd.getName());
	    }
	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}
	@Override
	// then use Spring BeanUtils to copy and ignore null using our function
	public User ignoreNullCopy(User src, User target) {
	    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	    return target;
	}

}
