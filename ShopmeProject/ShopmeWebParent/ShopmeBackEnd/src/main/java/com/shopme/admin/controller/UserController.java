package com.shopme.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.service.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
@RequestMapping("users")
public class UserController {

	private UserService service;

	@Autowired
	public UserController(UserService service) {
		this.service = service;
	}

	@GetMapping({ "", "/" })
	public String listAll(Model model) {
		List<User> listUsers = this.service.listAll();

		model.addAttribute("listUsers", listUsers);

		return "User/users";
	}

	@GetMapping("/new")
	public String showNewUser(Model model) {

		List<Role> listRoles = service.listRole();
		model.addAttribute("listRoles", listRoles);

		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("titlePage", "Create New User");

		return "User/user_form_create";
	}

	@PostMapping("/save")
	public String saveUser(@ModelAttribute(name = "user") User user, RedirectAttributes redirectAttributes) {
		service.save(user);

		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");

		return "redirect:/users";
	}

	@GetMapping("/edit/{id}")
	public String showEditUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRole();

			model.addAttribute("listRoles", listRoles);
			model.addAttribute("user", user);
			model.addAttribute("titlePage", "Edit User (ID: " + id + ")");
			
			return "User/user_form_create";
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}
	}
}