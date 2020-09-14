package com.solitardj9.timelineService.application.timelienManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.solitardj9.timelineService.application.timelineManager.service.TimelineManager;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineConflictFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineResourceNotFound;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TimelienManagerTest {
	
	@Autowired
	TimelineManager timelineManager;

    @Test
    public void testAll() throws Exception {
    	//
    	String timelineName = "";
		String value1 = "{\"key1\":\"value1\", \"key2\":\"value2\"}";
		String value2 = "{\"key1\":\"value3\", \"key2\":\"value4\"}";
		String value3 = "{\"key3\":\"value5\", \"key4\":\"value6\"}";
		
		Integer testCount = 10;
		Integer valueCount = 10;
		
		List<Long> timelineKeys = new ArrayList<>();
		for (int j = 0 ; j < valueCount ; j++) {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			timelineKeys.add(timestamp.getTime());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("//----------------------------------------------------------");
		System.out.println("put value");
		for (int i = 0 ; i < testCount ; i++) {
			timelineName = "timeline" + i;
			try {
				System.out.println("addTimeline : " + timelineManager.addTimeline(timelineName));
			} catch (ExceptionTimelineConflictFailure e) {
				e.printStackTrace();
			}
			
			for (Long timelienKey : timelineKeys) {
				try {
					System.out.println("put : " + timelineManager.put(timelineName, timelienKey, value1));
				} catch (ExceptionTimelineResourceNotFound e) {
					e.printStackTrace();
				}
			}
		}
		
		for (int i = 0 ; i < testCount ; i++) {
			timelineName = "timeline" + i;
			try {
				System.out.println(timelineManager.getTimeline(timelineName).toString());
			} catch (ExceptionTimelineResourceNotFound e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("//----------------------------------------------------------");
		System.out.println("put new value");
		for (int i = 0 ; i < testCount ; i++) {
			timelineName = "timeline" + i;
			
			for (Long timelienKey : timelineKeys) {
				try {
					System.out.println("put : " + timelineManager.put(timelineName, timelienKey, value2));
				} catch (ExceptionTimelineResourceNotFound e) {
					e.printStackTrace();
				}
			}
		}
		
		for (int i = 0 ; i < testCount ; i++) {
			timelineName = "timeline" + i;
			try {
				System.out.println(timelineManager.getTimeline(timelineName).toString());
			} catch (ExceptionTimelineResourceNotFound e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("//----------------------------------------------------------");
		System.out.println("update new value");
		for (int i = 0 ; i < testCount ; i++) {
			timelineName = "timeline" + i;
			
			for (Long timelienKey : timelineKeys) {
				try {
					System.out.println("update : " + timelineManager.update(timelineName, timelienKey, value3));
				} catch (ExceptionTimelineResourceNotFound e) {
					e.printStackTrace();
				}
			}
		}
		
		for (int i = 0 ; i < testCount ; i++) {
			timelineName = "timeline" + i;
			try {
				System.out.println(timelineManager.getTimeline(timelineName).toString());
			} catch (ExceptionTimelineResourceNotFound e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("//----------------------------------------------------------");
		System.out.println("remove value");
		Long removeKey = timelineKeys.get(3);
		for (int i = 0 ; i < testCount ; i++) {
			timelineName = "timeline" + i;
			try {
				System.out.println("remove : " + timelineManager.remove(timelineName, removeKey));
			} catch (ExceptionTimelineResourceNotFound e) {
				e.printStackTrace();
			}
		}
		
		for (int i = 0 ; i < testCount ; i++) {
			timelineName = "timeline" + i;
			try {
				System.out.println(timelineManager.getTimeline(timelineName).toString());
			} catch (ExceptionTimelineResourceNotFound e) {
				e.printStackTrace();
			}
		}
    }
}