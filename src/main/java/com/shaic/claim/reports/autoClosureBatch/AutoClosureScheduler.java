

package com.shaic.claim.reports.autoClosureBatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.CloseIgnoringInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.Intimation;
import com.shaic.domain.PremiaIntimationTable;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.paclaim.printbulkreminder.SearchPAPrintRemainderBulkService;
import com.shaic.reimbursement.reminderBulkSearch.SearchGenerateRemainderBulkService;
import com.shaic.starfax.simulation.PremiaPullService;
import com.shaic.starfax.simulation.StarFaxFVRServiceRevised;
import com.shaic.starfax.simulation.StarFaxServiceRevised;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@Singleton
@Startup()
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class AutoClosureScheduler
{
	private static final Logger log = LoggerFactory.getLogger(AutoClosureScheduler.class);
	public static String dataDir = System.getProperty("jboss.server.data.dir");
  // Here we inject a reference to the timerService
	 @Resource
	 private TimerService timerService;
	 
	 private static File dataFile;

	static {
		
	}

		@PostConstruct
	    private void init() {
			log.info("**************TIMER SERVICE INITTTTTTTTTTTTTTTTTTTT***********************");

	    }
		

		
		  @Timeout	
		  @AccessTimeout(value = 20, unit = TimeUnit.MINUTES) 
		  public void scheduledTimeout(Timer timer) {
			  try {
				  if(timer.getInfo() != null) {
					  if(SHAConstants.AUTO_CLOSURE_BATCH.equals(timer.getInfo())) {
						  log.info("###############Auto closure started ###################");
						  
						  //submitAutoCloseClaim();
						  
						} 
				  }
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
			  
		  }
 
		public static Properties readConnectionPropertyFile()
		{
			Properties prop = new Properties();
			InputStream input = null;
			
			try {
				
				input = new FileInputStream(dataDir + "/" + "connection.properties");
				prop.load(input);
				return prop;
		  
			} catch (IOException io) {
				io.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}


		public void cancelTimer(String timerInfo) {
			if(timerService != null) {
				for (Timer timer: timerService.getTimers()) {
			        if (true) {
			            System.out.println("Canceling Timer: info: " + timer.getInfo());
			            timer.cancel();
			        }
			    }   
			} else {
				log.info("******************TIMER SERVICE IS NULL****************");
			}
			
		}


		public void createTimer(String timerInfo,File file) {
			//if(timerService == null) {
				dataFile = file;
				log.info("Creating new Timer: " + timerInfo);
			    TimerConfig timerConf = new TimerConfig();
			    timerConf.setInfo(timerInfo);
			    Date currentDate = new Date();
			    timerService.createSingleActionTimer(currentDate, timerConf);
			/*} else {
				log.info("******************TIMER SERVICE IS NULL****************");
			}*/
			 	
		}
		
		
		public void submitAutoCloseClaim(File file){
			
			if(file != null){
			
			FileInputStream fis = null;
			try {
				
				fis = new FileInputStream(file);
				org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fis);
				Sheet sheetAt = workbook.getSheetAt(0);
				
				Iterator<Row> rowIterator = sheetAt.iterator();
				
				
				while (rowIterator.hasNext()){
					try{
					Row row = rowIterator.next();
					
					if(row.getRowNum() != 0){
						log.info("Auto closure running....");
					}

					
					}catch(Exception e){
						Notification.show("Error", "" + "Please upload excel with Valid format", Type.ERROR_MESSAGE);
						break;
					} 
				}
				cancelTimer(SHAConstants.AUTO_CLOSURE_BATCH);

			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(fis != null){
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		}


}

