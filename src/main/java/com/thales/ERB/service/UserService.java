package com.thales.ERB.service;

import java.util.List;

import com.thales.ERB.entity.Employee;
import com.thales.ERB.entity.User;

public interface UserService {

	List<User> getAllUsers();
	
	User getUserByName(String userName);
	
	User saveUser(User adminUser);

	User login(String username, String password);

	void updateUser(User existingUser) throws Exception;

	void deleteUserByName(String username);

}
