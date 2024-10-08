package ru.bereshs.hhworksearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class
HhWorkSearchApplication {
	public static void main(String[] args) {
		SpringApplication.run(HhWorkSearchApplication.class, args);
	}

}
