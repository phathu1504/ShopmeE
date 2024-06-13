package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.export.UserCsvExport;
import com.shopme.admin.export.UserExcelExport;
import com.shopme.admin.export.UserPDFExport;
import com.shopme.admin.handler.UserNotFoundException;
import com.shopme.admin.service.UserService;
import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("users")
public class UserController {

	private UserService service;

	@Autowired
	public UserController(UserService service) {
		this.service = service;
	}

	@GetMapping({ "", "/" })
	public String listFirstPage(Model model) {
		return listByPage(1, model, "id", "asc", null);
	}

	@GetMapping("/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
		Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<User> listUsers = page.getContent();

		long startCount = (pageNum - 1) * UserService.User_Per_Page + 1;
		long endCount = startCount + UserService.User_Per_Page - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

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
	public String saveUser(@ModelAttribute(name = "user") User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User saveUser = service.save(user);
			String uploadDir = "user-photos/" + saveUser.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			if (user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}
			service.save(user);
		}

		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");

		return getRedirectURLtoAffectedUser(user);
	}

	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}

	@GetMapping("/edit/{id}")
	public String showEditUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
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

	@GetMapping("/delete/{id}")
	public String deletUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);

			redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully");

		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/users";
	}

	@GetMapping("/{id}/enabled/{status}")
	public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The user: ID " + id + " has been " + status;

		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/users";
	}

	@GetMapping("/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserCsvExport exporter = new UserCsvExport();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserExcelExport exporter = new UserExcelExport();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserPDFExport exporter = new UserPDFExport();
		exporter.export(listUsers, response);
	}
}
