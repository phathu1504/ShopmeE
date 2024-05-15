package com.shopme.admin.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.user.RoleReponsitory;
import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {

	private UserRepository userRepo;
	private RoleReponsitory roleRepo;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepo, RoleReponsitory roleRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.passwordEncoder = passwordEncoder;
	}

	public List<User> listAll() {
		return (List<User>) userRepo.findAll();
	}

	public List<Role> listRole() {
		return (List<Role>) roleRepo.findAll();
	}

	public void save(User user) {
		encodePassword(user);
		userRepo.save(user);
	}

	private void encodePassword(User user) {
		String encodedPassword = this.passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	public boolean isEmailUnique(String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		
		return userByEmail == null;
	}

	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user with ID: " + id);
		}

	}
}
