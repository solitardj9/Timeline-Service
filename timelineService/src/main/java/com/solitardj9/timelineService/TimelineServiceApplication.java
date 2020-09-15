package com.solitardj9.timelineService;

import java.util.TreeMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class TimelineServiceApplication {

	public static void main(String[] args) {
		//
		ConfigurableApplicationContext context = SpringApplication.run(TimelineServiceApplication.class, args);
		
		// TODO : API 조회 시 없으면 다른 노드에서도 조회하도록 기능을 확장 개선할 것
		// 그렇다면 조회도 TimelineSyncManager 통해서 가져가도록 해봐야 겠다.....
		
		
		
		
	}
}