package com.mod.soap;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;
@EnableJpaRepositories("com.mod.soap.dao.repository")
@EnableJpaAuditing
@SpringBootApplication
public class SpringBootSoapServiceApplication {

	private static ApplicationContext applicationContext = null;

	public static void main(String[] args) {
		String mode = args != null && args.length > 0 ? args[0] : null;

		if (applicationContext != null && mode != null && "stop".equals(mode)) {
			System.exit(SpringApplication.exit(applicationContext, new ExitCodeGenerator() {
				@Override
				public int getExitCode() {
					return 0;
				}
			}));
		}else {
			SpringApplication app = new SpringApplication(SpringBootSoapServiceApplication.class);
			app.setDefaultProperties(Collections
					.singletonMap("server.port", "8082"));
			applicationContext = app.run(args);
		}


//		SpringApplication.run(SpringBootSoapServiceApplication.class, args);

	}
}
