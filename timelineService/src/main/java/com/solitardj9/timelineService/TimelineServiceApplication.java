package com.solitardj9.timelineService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TimelineServiceApplication {

	public static void main(String[] args) {
		//
		ConfigurableApplicationContext context = SpringApplication.run(TimelineServiceApplication.class, args);
	
		
		// TODO ; 
		// 1. add file download to http proxy manager --> 임시 폴더에 생성딤.....이거 해결해야 할듯
		// 2. add restore status and function to service instances manager
		// 3. add timeline restore manager <-- spring event --> service instances manager
		// 4. modify replication manager to avoid conflict with timeline restore manager
	}
}