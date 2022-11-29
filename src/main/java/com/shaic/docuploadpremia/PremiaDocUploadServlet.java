 /**
 * 
 */
package com.shaic.docuploadpremia;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author ntv.vijayar
 *
 */
public class PremiaDocUploadServlet extends HttpServlet {
	
	 protected void doGet(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException {
		 
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
	            throw new IOException(e.getMessage());
		 }
	 }

}
