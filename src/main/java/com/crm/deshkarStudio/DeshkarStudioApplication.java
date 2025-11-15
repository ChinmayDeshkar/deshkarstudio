package com.crm.deshkarStudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DeshkarStudioApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeshkarStudioApplication.class, args);
	}

}
