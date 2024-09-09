package ru.bereshs.hhworksearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableFeignClients
@SpringBootApplication
public class
HhWorkSearchApplication {
	public static void main(String[] args) {
		SpringApplication.run(HhWorkSearchApplication.class, args);
	}

}
