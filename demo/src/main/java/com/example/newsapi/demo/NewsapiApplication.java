package com.example.newsapi.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class NewsapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsapiApplication.class, args);
	}

}
