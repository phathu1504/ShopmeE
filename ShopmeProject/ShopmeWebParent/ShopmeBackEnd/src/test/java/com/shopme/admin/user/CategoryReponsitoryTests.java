package com.shopme.admin.user;

import com.shopme.admin.category.CategoryReponsitory;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.startsWith;

import java.util.List;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CategoryReponsitoryTests {

	private CategoryReponsitory categoryReponsitory;

	@Autowired
	public CategoryReponsitoryTests(CategoryReponsitory categoryReponsitory) {
		this.categoryReponsitory = categoryReponsitory;
	}

	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronics");
		Category savedCategory = categoryReponsitory.save(category);

		assertThat(savedCategory.getId() != null && savedCategory.getId() > 0);
	}

	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(4);
		Category desktops = new Category("Memory", parent);
		categoryReponsitory.saveAll(List.of(desktops));
	}

	@Test
	public void testGetCategory() {
		Category category = this.categoryReponsitory.findById(2).get();
		System.out.println(category.getName());

		Set<Category> children = category.getChildren();

		for (Category subCategory : children) {
			System.out.println(subCategory.getName());
		}

		assertThat(category.getId()).isGreaterThan(0);
	}

	@Test
	public void testPrintHierachicalCategories() {
		Iterable<Category> categories = categoryReponsitory.findAll();

		for (Category category : categories) {
			if (category.getParent() == null) {
				System.out.println(category.getName());

				Set<Category> children = category.getChildren();

				for (Category subCategory : children) {
					System.out.println("--" + subCategory.getName());
					this.printChildren(subCategory, 1);
				}
			}
		}
	}

	private void printChildren(Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		for (Category subCategory : children) {
			for (int i = 0; i < newSubLevel; i++) {
				System.out.println("--");
			}
			System.out.println(subCategory.getName());
			
			for (Category category : children) 	{
				System.out.println("--"+subCategory.getName());
			}
		}
	}
}
