package com.solitardj9.timelineService.utils.fileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentNavigableMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapFileUtil {
	//
	private static final Logger logger = LoggerFactory.getLogger(MapFileUtil.class);
	
	public static void writeTimelinespAsFile(Map<String, ConcurrentNavigableMap<Long, String>> timelines, String fileName) {
		//
		ObjectOutputStream oos = null;
		try {
            oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(timelines);
            oos.close();
        } catch (IOException e) {
        	logger.error("[MapFileUtil].writeMapAsFile : error = " + e.getStackTrace());
        } finally {
        	if (oos != null) {
        		try {
					oos.close();
				} catch (IOException e) {
					logger.error("[MapFileUtil].writeMapAsFile : error = " + e.getStackTrace());
				}
        	}
        }
	}
	
	@SuppressWarnings({ "unchecked" })
	public static Map<String, ConcurrentNavigableMap<Long, String>> readTimelinesFromFile(String fileName) {
		//
		Map<String, ConcurrentNavigableMap<Long, String>> timelines = new HashMap<>();
		ObjectInputStream ois = null;
		try {
            ois = new ObjectInputStream(new FileInputStream(fileName));
            timelines = (Map<String, ConcurrentNavigableMap<Long, String>>) ois.readObject();
            ois.close();
        } catch (IOException e) {
        	logger.error("[MapFileUtil].readTimelinesFromFile : error = " + e.getStackTrace());
        	return timelines;
        } catch (ClassNotFoundException e) {
        	logger.error("[MapFileUtil].readTimelinesFromFile : error = " + e.getStackTrace());
        	return timelines;
        } finally {
        	if (ois != null) {
        		try {
        			ois.close();
				} catch (IOException e) {
					logger.error("[MapFileUtil].readTimelinesFromFile : error = " + e.getStackTrace());
				}
        	}
        }
		return timelines;
	}
	
	@SuppressWarnings({ "unchecked" })
	public static Map<String, ConcurrentNavigableMap<Long, String>> readTimelinesFromFile(File file) {
		//
		Map<String, ConcurrentNavigableMap<Long, String>> timelines = new HashMap<>();
		ObjectInputStream ois = null;
		try {
            ois = new ObjectInputStream(new FileInputStream(file));
            timelines = (Map<String, ConcurrentNavigableMap<Long, String>>) ois.readObject();
            ois.close();
        } catch (IOException e) {
        	logger.error("[MapFileUtil].readTimelinesFromFile : error = " + e.getStackTrace());
        	return timelines;
        } catch (ClassNotFoundException e) {
        	logger.error("[MapFileUtil].readTimelinesFromFile : error = " + e.getStackTrace());
        	return timelines;
        } finally {
        	if (ois != null) {
        		try {
        			ois.close();
				} catch (IOException e) {
					logger.error("[MapFileUtil].readTimelinesFromFile : error = " + e.getStackTrace());
				}
        	}
        }
		return timelines;
	}
	
	public static Boolean deleteFile(String fileName) {
		//
		File file = new File(fileName);
		if (file.exists()) {
			if (file.delete()) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static Boolean deleteFile(File file) {
		//
		if (file.exists()) {
			if (file.delete()) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
}