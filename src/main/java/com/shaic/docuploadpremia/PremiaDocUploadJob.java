
package com.shaic.docuploadpremia;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author ntv.vijayar
 *
 */
public class PremiaDocUploadJob implements Job {
	
	//RM-STUB
	/*@EJB
	private StarFaxSimulatorService starFaxStimulatorService;*/
	//@EJB
	//private PremiaDocUploadService premiaDocUploadService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		//premiaDocUploadService.processPremiaDocUploadReq(starFaxStimulatorService);
	}
	
	

}
