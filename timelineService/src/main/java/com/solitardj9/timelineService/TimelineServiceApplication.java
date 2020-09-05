package com.solitardj9.timelineService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.solitardj9.timelineService.application.mapManager.service.TimelineManager;

@SpringBootApplication
public class TimelineServiceApplication {

	public static void main(String[] args) {
		//
		ConfigurableApplicationContext context = SpringApplication.run(TimelineServiceApplication.class, args);
		
		TimelineManager timelineManager = ((TimelineManager)context.getBean("timelineManager"));
		
		String timelineName = "";
		String value = "{\"key1\":\"value1\", \"key2\":\"value2\"}";
		
		
		System.out.println("//----------------------------------------------------------");
		System.out.println("put value");
		for (int i = 0 ; i < 10 ; i++) {
			timelineName = "timeline" + i;
			timelineManager.addTimeline(timelineName);
			
			for (int j = 0 ; j < 10 ; j++) {
				
				timelineManager.put(timelineName, j, value);
			}
		}
		System.out.println("//----------------------------------------------------------");
		System.out.println("display value");
		for (int i = 0 ; i < 10 ; i++) {
			timelineName = "timeline" + i;
			System.out.println(timelineManager.getTimeline(timelineName).toString());
		}
		System.out.println("put new value");
		for (int i = 0 ; i < 10 ; i++) {
			timelineName = "timeline" + i;
			timelineManager.put(timelineName, 3, "new value");
		}
		System.out.println("display new value");
		for (int i = 0 ; i < 10 ; i++) {
			timelineName = "timeline" + i;
			System.out.println(timelineManager.getTimeline(timelineName).toString());
		}
		System.out.println("//----------------------------------------------------------");
		System.out.println("remove value");
		for (int i = 0 ; i < 10 ; i++) {
			timelineName = "timeline" + i;
			timelineManager.remove(timelineName, 7);
		}
		System.out.println("display removed value");
		for (int i = 0 ; i < 10 ; i++) {
			timelineName = "timeline" + i;
			System.out.println(timelineManager.getTimeline(timelineName).toString());
		}
	}
}
