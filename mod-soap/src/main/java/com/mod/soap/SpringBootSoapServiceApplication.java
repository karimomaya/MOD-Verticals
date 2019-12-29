package com.mod.soap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;
@EnableJpaRepositories("com.mod.soap.dao.repository")
@EnableJpaAuditing
@SpringBootApplication
public class SpringBootSoapServiceApplication {

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(SpringBootSoapServiceApplication.class);
		app.setDefaultProperties(Collections
				.singletonMap("server.port", "8083"));
		app.run(args);

//		SpringApplication.run(SpringBootSoapServiceApplication.class, args);

	}
}
