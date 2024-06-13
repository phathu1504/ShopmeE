package com.shopme.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.category.CategoryReponsitory;
import com.shopme.common.entity.Category;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {
	private CategoryReponsitory repo;

	@Autowired
	public CategoryService(CategoryReponsitory repo) {
		this.repo = repo;
	}

	public List<Category> listAll() {
		return repo.findAll();
	}
}
