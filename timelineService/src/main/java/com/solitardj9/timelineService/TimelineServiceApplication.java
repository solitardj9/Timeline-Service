package com.solitardj9.timelineService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TimelineServiceApplication {

	public static void main(String[] args) {
		//
		ConfigurableApplicationContext context = SpringApplication.run(TimelineServiceApplication.class, args);
		
	}
}