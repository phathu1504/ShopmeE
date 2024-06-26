package com.shopme.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({ "com.shopme.common.entity", "com.shopme.admin.user", "com.shopme.admin.controller",
		"com.shopme.admin.restcontroller", "com.shopme.admin.security", "com.shopme.admin.service" })
public class ShopmeBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmeBackEndApplication.class, args);
	}

}
