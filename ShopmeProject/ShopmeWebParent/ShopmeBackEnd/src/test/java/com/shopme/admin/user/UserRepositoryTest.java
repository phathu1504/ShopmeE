package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
class UserRepositoryTest {
	@Autowired
	private UserRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateUseNewUserWithOneRole() {

		Role roleAdmin = entityManager.find(Role.class, 1);

		User userThuPHA = new User("phanthu152004@gmail.com", "01052004", "Thu", "Phan Hoang Anh");

		userThuPHA.addRole(roleAdmin);

		User savedUser = repo.save(userThuPHA);

		assertThat(savedUser.getId() != null && savedUser.getId() > 0);
	}

	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userRavi = new User("ravi2@gmail.com", "ravi2024", "ravi", "Kumar");

		Role roleEditor = new Role(3);

		Role roleAssistant = new Role(5);

		userRavi.addRole(roleAssistant);
		userRavi.addRole(roleEditor);

		User savedUser = repo.save(userRavi);

		assertThat(savedUser.getId() != null && savedUser.getId() > 0);
	}

	@Test
	public void testListAllUser() {
		Iterable<User> listUser = repo.findAll();
		listUser.forEach(user -> System.out.println(user));
	}

	@Test
	public void testGetUserById() {
		User userThu = repo.findById(1).get();
		System.out.println(userThu);
		assertThat(userThu.getId() > 0);
	}

	@Test
	public void testUpdateUserDetails() {
		User userThu = repo.findById(1).get();
		userThu.setEnabled(true);
		userThu.setEmail("phanthu@gmail.com");

		repo.save(userThu);
	}

	@Test
	public void testUpdateUserRoles() {
		User userRavi = repo.findById(3).get();

		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);

		userRavi.getRoles().remove(roleEditor);
		userRavi.addRole(roleSalesperson);

		repo.save(userRavi);
	}

	@Test
	public void testDeleteUser() {
		Integer userId = 1;
		repo.deleteById(userId);
	}

	@Test
	public void testGetUserByEmail() {
		String email = "user@gmail.com";

		User user = repo.getUserByEmail(email);

		assertThat(user).isNotNull();
	}
}