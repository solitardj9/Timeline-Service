package com.solitardj9.timelineService.systemInterface.schedulerInterface.service;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

@Service(value= "schedulerManager")
public class SchedulerManager {
	//
	private Scheduler scheduler = null;
		
	public SchedulerManager() {
		//
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            System.out.println("QuartzScheduler is Loaded");
		} catch (SchedulerException se) {
			System.out.println("QuartzScheduler Loading Error : " + se.toString());
        }
	}

	public Scheduler getScheduler() {
		return scheduler;
	}
}