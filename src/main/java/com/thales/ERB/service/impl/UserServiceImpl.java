package com.thales.ERB.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.thales.ERB.ErbApplication;
import com.thales.ERB.entity.Employee;
import com.thales.ERB.entity.Referral;
import com.thales.ERB.entity.User;
import com.thales.ERB.repo.UserRepository;
import com.thales.ERB.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

	@Override
	public User login(String username, String password) {
		try {
			logger.debug("UserServiceImpl :: login  :: start");
			User user = userRepository.findByUsernameAndPassword(username, password);
			if (null != user) {
				logger.debug("UserServiceImpl :: login  :: end");
				return user;
			} else {
				logger.debug("UserServiceImpl :: login  :: end");
				return null;
			}
		} catch (Exception e) {
			logger.debug("UserServiceImpl :: login  :: error occurred" + e.toString());
			return null;
		}
	}
	
	@Override
	public List<User> getAllUsers() {
		try {
			logger.debug("UserServiceImpl ::getAllUsers  :: start");
			List<User> userList = userRepository.findAll();
			if(null != userList) {
				logger.debug("UserServiceImpl :: getAllUsers  :: end");
				return userList;
			}
			else {
				logger.debug("UserServiceImpl :: getAllUsers  :: end");
				return null;
			}
		} catch (Exception e) {
			logger.debug("UserServiceImpl ::getAllUsers  :: error occurred" + e.toString());
			return null;
		}
	}

	@Override
	public User getUserByName(String userName) {
		try {
			logger.debug("UserServiceImpl :: getUserById  :: start");
			User user = userRepository.findUserByName(userName);
			
			if(null != user) {
				logger.debug("UserServiceImpl :: getUserById  :: end");
				logger.debug("UserServiceImpl :: Userdetails  :: "+user);
				return user;
			}
			else {
				logger.debug("UserServiceImpl :: getUserById  :: end");
				return null;
			}
		} catch (Exception e) {
			logger.debug("UserServiceImpl :: getUserById  :: error occurred" + e.toString());
			return null;
		}
	}
	
	@Override
	public User saveUser(User adminUser) {
		try {
			logger.debug("UserServiceImpl :: saveUser  :: start");
//			Employee emp = employeeRepository.save(employee, user);
			//BCryptPasswordEncoder bcryptEncoder= new BCryptPasswordEncoder();
			User encyptedUser= new User();
			encyptedUser.setUsername(adminUser.getUsername());
			encyptedUser.setPassword(bcryptEncoder.encode(adminUser.getPassword()));
			User user = userRepository.save(encyptedUser);
			if(null != user) {
				logger.debug("UserServiceImpl :: saveUser  :: end");
				return user;
			}
			else {
				logger.debug("UserServiceImpl :: saveUser  :: end");
				return null;
			}
		} catch (Exception e) {
			logger.debug("UserServiceImpl :: saveUser  :: error occurred" + e.toString());
			return null;
		}
	}

	@Override
	public void updateUser(User existingUser)  throws Exception{
		try {
			logger.debug("UserServiceImpl :: updateUser  :: start");
//			Employee emp = employeeRepository.update(employee, user);
			User encryptedUser= new User();
			encryptedUser.setUsername(existingUser.getUsername());
			encryptedUser.setPassword(bcryptEncoder.encode(existingUser.getPassword()));
			String username = encryptedUser.getUsername();
			String password = encryptedUser.getPassword();
			userRepository.update(username, password);
		} catch (Exception e) {
			logger.debug("UserServiceImpl :: updateUser  :: error occurred" + e.toString());
			throw e;
		}
	}

	@Override
	public void deleteUserByName(String username) {
		try {
			logger.debug("UserServiceImpl :: deleteUserByName  :: start");
//			employeeRepository.deleteById(employeeId, user);
			userRepository.deleteByUsername(username);
			logger.debug("UserServiceImpl :: deleteUserByName  :: end");
		} catch (Exception e) {
			logger.debug("UserServiceImpl :: deleteUserByName  :: error occurred" + e.toString());
		}
	}

}
