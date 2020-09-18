package com.solitardj9.timelineService.utils.fileUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MapFileUtilTest {
	
    @Test
    public void test() throws Exception {
    	//
    	Map<String, ConcurrentNavigableMap<Long, String>> timelines = new ConcurrentHashMap<>();
    	
    	timelines.put("aaa", timelines.getOrDefault("aaa", new ConcurrentSkipListMap<Long, String>()));
    	timelines.get("aaa").put(1234L, "bbbb");
    	timelines.get("aaa").put(5678L, "cccc");
    	
    	timelines.put("bbb", timelines.getOrDefault("bbb", new ConcurrentSkipListMap<Long, String>()));
    	timelines.get("bbb").put(4321L, "dddd");
    	timelines.get("bbb").put(8765L, "eeee");
    	
    	MapFileUtil.writeTimelinespAsFile(timelines, "timelines");
    	
    	Path fileLocation = Paths.get("timelines").toAbsolutePath().normalize();
		Resource resource = new UrlResource(fileLocation.toUri());
		
		if(resource.exists()) {
			System.out.println("timelines found.");
		}
		else {
			System.out.println("timelines not found.");
		}
    	
    	Map<String, ConcurrentNavigableMap<Long, String>> otherTimelines = MapFileUtil.readTimelinesFromFile("timelines");
    	System.out.println("otherTimelines = " + otherTimelines.toString());
    	
    	System.out.println(MapFileUtil.deleteFile("timelines"));
    }
}