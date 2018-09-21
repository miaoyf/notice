package com.meow.notice.quartz;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class MainScheduler {
	
	public static Scheduler getScheduler() throws SchedulerException {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		return schedulerFactory.getScheduler();
	}

	public static void schedulerJob() throws SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(CheckNoticeJob.class).withIdentity("job1", "group1").build();

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group3")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(30).repeatForever()).build();
		Scheduler scheduler = getScheduler();

		scheduler.scheduleJob(jobDetail, trigger);

		scheduler.start();
	}
}
