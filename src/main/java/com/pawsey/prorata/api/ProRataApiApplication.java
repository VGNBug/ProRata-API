package com.pawsey.prorata.api;

import com.pawsey.api.GenericApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;

@SpringBootApplication
@EntityScan("com.pawsey.prorata.model")
public class ProRataApiApplication extends GenericApplication {

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
