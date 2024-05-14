package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.user.RoleReponsitory;
import com.shopme.common.entity.Role;

// Test RoleReponsitory CRUD

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
class RoleReponsitoryTest {

	// Khai bao bien va depen
	@Autowired
	private RoleReponsitory repo;

	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin", "manage everthing");
		Role savedRole = repo.save(roleAdmin);
		assertThat(savedRole.getId() != null && savedRole.getId() > 0);
	}

	@Test
	public void testCreateFirstRoles() {
		Role roleSalesperson = new Role("Salesperson",
				"manage product price, " + "customers, shipping, orders and sales report");

		Role roleEditor = new Role("Editor", "manage categories, " + "brands, products, articles and menus");

		Role roleShipper = new Role("Shipper", "View products, view orders");

		Role roleAssistant = new Role("Assistant", "manage questions and reviews");

		repo.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
	}
}