package com.pawsey.prorata.api;

import com.pawsey.api.repository.BaseRepository;
import com.pawsey.api.service.BaseService;
import com.pawsey.api.service.impl.BaseServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;

@SpringBootApplication
@EntityScan("com.pawsey.prorata.model")
public class ProRataApiApplication {

	@Bean
	public BaseService service() {
		return new BaseServiceImpl() {
			@Override
			protected void persistCollections(Object entity, Object response) {

			}

			@Override
			protected Object initializeCollections(Object user) {
				return null;
			}

			@Override
			protected boolean entityIsNull(Object entity) {
				return false;
			}
		};
	}

	@Bean
	public BaseRepository repository() {
		return new BaseRepository() {
			@Override
			public Object save(Object entity) {
				return null;
			}

			@Override
			public Iterable save(Iterable entities) {
				return null;
			}

			@Override
			public Object findOne(Serializable serializable) {
				return null;
			}

			@Override
			public boolean exists(Serializable serializable) {
				return false;
			}

			@Override
			public Iterable findAll() {
				return null;
			}

			@Override
			public Iterable findAll(Iterable iterable) {
				return null;
			}

			@Override
			public long count() {
				return 0;
			}

			@Override
			public void delete(Serializable serializable) {

			}

			@Override
			public void delete(Object entity) {

			}

			@Override
			public void delete(Iterable entities) {

			}

			@Override
			public void deleteAll() {

			}
		};
	}

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
