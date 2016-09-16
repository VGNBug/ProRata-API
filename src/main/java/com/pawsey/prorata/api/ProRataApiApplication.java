package com.pawsey.prorata.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class ProRataApiApplication {

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Welcome to the ProRata-API!";
	}

	public static void main(String[] args)
    {
		SpringApplication.run(ProRataApiApplication.class, args);
	}
}
