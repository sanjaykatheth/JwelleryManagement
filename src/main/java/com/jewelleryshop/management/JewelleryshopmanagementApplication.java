package com.jewelleryshop.management;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.jewelleryshop.management.model.Student;

@SpringBootApplication
public class JewelleryshopmanagementApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(JewelleryshopmanagementApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}

	@Bean
	public Student hello() {
		return new Student("sanjay","katheth");
	}
}
