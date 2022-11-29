/**
 * 
 */
package com.shaic.docuploadpremia;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author ntv.vijayar
 *
 */
public class PremDocUploadTest {

	
	public static void main(String args[]) throws SchedulerException
	{
		try
		{
		JobDetail job = JobBuilder.newJob(PremiaDocUploadJob.class).withIdentity("Premia Doc Upload job", "group1").build();
 
    	//configure the scheduler time
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("dummyTriggerName", "group1")
				.withSchedule(
					SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(5).repeatForever())
				.build();
 
    	//schedule it
    	Scheduler scheduler = new StdSchedulerFactory().getScheduler();
    	scheduler.start();
    	scheduler.scheduleJob(job, trigger);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
