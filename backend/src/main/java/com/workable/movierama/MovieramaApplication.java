package com.workable.movierama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableCaching
public class MovieramaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieramaApplication.class, args);
	}

}
