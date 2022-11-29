//
//
//package com.shaic.arch.utils;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.nio.channels.Channels;
//import java.nio.channels.ReadableByteChannel;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Properties;
//import java.util.concurrent.TimeUnit;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import javax.ejb.AccessTimeout;
//import javax.ejb.ConcurrencyManagement;
//import javax.ejb.ConcurrencyManagementType;
//import javax.ejb.EJB;
//import javax.ejb.ScheduleExpression;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;
//import javax.ejb.Timeout;
//import javax.ejb.Timer;
//import javax.ejb.TimerConfig;
//import javax.ejb.TimerService;
//import javax.transaction.HeuristicMixedException;
//import javax.transaction.HeuristicRollbackException;
//import javax.transaction.NotSupportedException;
//import javax.transaction.RollbackException;
//import javax.transaction.SystemException;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.WorkbookFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.shaic.arch.SHAConstants;
//import com.shaic.arch.SHAFileUtils;
//import com.shaic.arch.fields.dto.SelectValue;
//import com.shaic.claim.policy.search.ui.PremPolicyDetails;
//import com.shaic.claim.policy.search.ui.PremPolicySchedule;
//import com.shaic.domain.Claim;
//import com.shaic.domain.DocUploadToPremia;
//import com.shaic.domain.Intimation;
//import com.shaic.domain.PremiaIntimationTable;
//import com.shaic.domain.Product;
//import com.shaic.domain.ReferenceTable;
//import com.shaic.domain.reimbursement.ReimbursementService;
//import com.shaic.ims.bpm.claim.BPMClientContext;
//import com.shaic.ims.bpm.claim.DBCalculationService;
//import com.shaic.domain.reimbursement.ReimbursementService;
//import com.shaic.paclaim.printbulkreminder.SearchPAPrintRemainderBulkService;
//import com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel.CloseClaimPageDTO;
//import com.shaic.reimbursement.reminderBulkSearch.SearchGenerateRemainderBulkService;
//import com.shaic.starfax.simulation.PremiaPullService;
//import com.shaic.starfax.simulation.StarFaxFVRServiceRevised;
//import com.shaic.starfax.simulation.StarFaxServiceRevised;
//
//@Singleton
//@Startup()
//@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
//public class ManualBatchScheduler
//{
//	private final Logger log = LoggerFactory.getLogger(ManualBatchScheduler.class);
//	public static String dataDir = System.getProperty("jboss.server.data.dir");
//  // Here we inject a reference to the timerService
//	 @Resource
//	 private TimerService timerService;
//	 
//	 @EJB
//	 private PremiaPullService premiaPullService;
//  
//	 @EJB
//	 private StarFaxServiceRevised starFaxRevisedService;
//	 
//	 @EJB
//	 private StarFaxFVRServiceRevised starFaxFVRRevisedService;
//	
//	 @EJB
//	 private SearchGenerateRemainderBulkService generateBulkReminderService;
//	 
//	 @EJB
//	 private SearchPAPrintRemainderBulkService generatePABulkRemidnerService;
//	 
//	 @EJB
//	 private ReimbursementService reimbursementService;
//	 
//	public static String SHOULD_BATCH_RUN = null;
//	
//	public static String BATCH_SIZE = null;
//	
//	public static String SHOULD_SCHEDULE_START = null;
//
//		@PostConstruct
//	    private void init() {
//			    log.info("**************TIMER SERVICE INITTTTTTTTTTTTTTTTTTTT***********************");
////				TimerConfig timeConfigForPremiaPull = new TimerConfig(SHAConstants.PORTABILITY_POLICY_BATCH, false);
////				timeConfigForPremiaPull.setInfo(SHAConstants.PORTABILITY_POLICY_BATCH);
////				ScheduleExpression exp = new ScheduleExpression();
////				exp.hour("1").minute("*");
////			    timerService.createCalendarTimer(exp, timeConfigForPremiaPull);
//			    
//			    TimerConfig insuredEmailId = new TimerConfig(SHAConstants.INSURED_EMAIL_ID_BATCH, false);
//			    insuredEmailId.setInfo(SHAConstants.INSURED_EMAIL_ID_BATCH);
//				ScheduleExpression exp1 = new ScheduleExpression();
//				exp1.hour("*").minute("*/5");
//			    timerService.createCalendarTimer(exp1, insuredEmailId);
//			    
//			    TimerConfig jioPreviousPolicy = new TimerConfig(SHAConstants.JIO_PREVIOUS_POLICY_BATCH, false);
//			    jioPreviousPolicy.setInfo(SHAConstants.JIO_PREVIOUS_POLICY_BATCH);
//				ScheduleExpression exp2 = new ScheduleExpression();
//				exp2.hour("*").minute("*/5");
//			    timerService.createCalendarTimer(exp2, jioPreviousPolicy);
//			    
//			    TimerConfig ocrTimer = new TimerConfig(SHAConstants.OCR_DOCUMENT_BATCH, false);
//			    ocrTimer.setInfo(SHAConstants.OCR_DOCUMENT_BATCH);
//				ScheduleExpression expOcr = new ScheduleExpression();
//				expOcr.hour("*").minute("*/5");
//			    timerService.createCalendarTimer(expOcr, ocrTimer);
//			    
////			    TimerConfig portedFlag = new TimerConfig(SHAConstants.POLICY_PORTED_FLAG_UPDATED, false);
////			    portedFlag.setInfo(SHAConstants.POLICY_PORTED_FLAG_UPDATED);
////				ScheduleExpression exp3 = new ScheduleExpression();
////				exp3.hour("*").minute("*/5");
////			    timerService.createCalendarTimer(exp3, portedFlag);
//			    
//	    }
//		
//		@Timeout	
//		@AccessTimeout(value = 20, unit = TimeUnit.MINUTES) 
//		public void scheduledTimeout(Timer timer) {
//			try {
//				if(timer.getInfo() != null) {
//						  
//					if(SHAConstants.PORTABILITY_POLICY_BATCH.equals(timer.getInfo())) {
//						log.info("###############STARTED PORTABILITY POLICY PULL FOR EVERY ONE HOUR ###################");
//						String url = System.getProperty("jboss.server.data.dir") + File.separator + SHAConstants.PORTABLITY_EXCEL;
//						File excel =  new File(url);
//						if(excel != null){
//							savePortabilityPolicy(excel);
//						}
//					}else if(SHAConstants.INSURED_EMAIL_ID_BATCH.equals(timer.getInfo())) {
//						log.info("###############STARTED INSURED EMAIL ID UPDATION FOR EVERY ONE HOUR ###################");
//						String url = System.getProperty("jboss.server.data.dir") + File.separator + SHAConstants.INSURED_EMAIL_ID_EXCEL;
//						File excel =  new File(url);
//						if(excel != null){
//							saveEmployeeIdAndInsuredEmail(excel);
//						}
//					}
//					else if(SHAConstants.JIO_PREVIOUS_POLICY_BATCH.equals(timer.getInfo())) {
//						log.info("###############STARTED INSURED EMAIL ID UPDATION FOR EVERY ONE HOUR ###################");
//						String url = System.getProperty("jboss.server.data.dir") + File.separator + SHAConstants.JIO_PREVIOUS_POLICY_EXCEL;
//						File excel =  new File(url);
//						if(excel != null){
//							saveJioPreviousPolicy(excel);
//						}
//					}else if(SHAConstants.OCR_DOCUMENT_BATCH.equals(timer.getInfo())) {
//						log.info("###############STARTED OCR DOCUMENT FOR EVERY ONE HOUR ###################");
//						String url = System.getProperty("jboss.server.data.dir") + File.separator + SHAConstants.OCR_DOCUMENT_BATCH;
//						File excel =  new File(url);
//						if(excel != null){
//							saveJioPreviousPolicy(excel);
//						}
//					}
//					else if(SHAConstants.POLICY_PORTED_FLAG_UPDATED.equals(timer.getInfo())) {
//						log.info("###############STARTED PORTED FLAG UPDATION FOR EVERY 5 MINS ###################");
//						String url = System.getProperty("jboss.server.data.dir") + File.separator + SHAConstants.POLICY_PORTED_UPDATED;
//						File excel =  new File(url);
//						if(excel != null){
//							savePolicyPortedFlag(excel);
//						}
//					}
//				}
//			} catch (Exception e) {
//					e.printStackTrace();
//			}
//			  
//		}
//		  
//		public void savePortabilityPolicy(File excel) {
//
//	        try {
//				FileInputStream fis = new FileInputStream(excel);
//				org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fis);
//				Sheet sheetAt = workbook.getSheetAt(0);
//				
//				log.info("############### PORTABILITY POLICY EXECUTION STARTED ###################");
//				
//				Iterator<Row> rowIterator = sheetAt.iterator();
//				
//				while (rowIterator.hasNext()){
//					try{
//					Row row = rowIterator.next();
//					
//					Cell cellPolicyNumber = row.getCell(0);
//					String policyNumber = cellPolicyNumber.getStringCellValue();
//					
//					log.info("!!!!!! POLICY NUMBER FOR PORTABILITY UPDATION START----------------->  " +policyNumber);
//					
//					PremPolicyDetails fetchPolicyDetailsfromPremia = premiaPullService.fetchPolicyDetailsFromPremia(policyNumber);
//					try {
//						premiaPullService.saveManualPrevPortabilityPolicy(fetchPolicyDetailsfromPremia);
//						
//				   log.info("!!!!!! POLICY NUMBER FOR PORTABILITY UPDATION END ----------------->  " +policyNumber);	
//					
//					} catch (SecurityException | IllegalStateException
//							| NotSupportedException | SystemException | RollbackException
//							| HeuristicMixedException | HeuristicRollbackException e) {
//						e.printStackTrace();
//					}
//					}catch(Exception e){
//					}
//				}
//				
//				log.info("############### PORTABILITY POLICY EXECUTION ENDED ###################");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				//e.printStackTrace();
//			}
//		}
//		
//		public void saveEmployeeIdAndInsuredEmail(File excel) {
//			
//	        try {
//				FileInputStream fis = new FileInputStream(excel);
//				org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fis);
//				Sheet sheetAt = workbook.getSheetAt(0);
//				
//				log.info("############### INSURED EMAIL EXECUTION STARTED ###################");
//				
//				Iterator<Row> rowIterator = sheetAt.iterator();
//				
//				while (rowIterator.hasNext()){
//					try{
//					Row row = rowIterator.next();
//					
//					Cell cellPolicyNumber = row.getCell(0);
//					
//					Cell cellRiskId = row.getCell(1);
//					
//					String policyNumber = cellPolicyNumber.getStringCellValue();
//					Double insuredNumer = cellRiskId.getNumericCellValue();
//					String riskID = String.valueOf(insuredNumer.intValue());
//					
//					log.info("!!!!!! Insured Number for Email Id updation---------------->  " +riskID);
//					
//					PremPolicyDetails fetchGmcPolicyDetailsFromPremia = premiaPullService.fetchGmcPolicyDetailsFromPremia(policyNumber, riskID);
//					premiaPullService.saveEffectiveFromDate(fetchGmcPolicyDetailsFromPremia);
//					
//					//log.info("!!!!!! POLICY NUMBER FOR EMAIL ID UPDATION END----------------->  " +policyNumber);
//
////					}catch(Exception e){
////						
////					}
//					
////					Iterator<Cell> cellIterator = row.cellIterator();
//					
////					while (cellIterator.hasNext()) {
////						Cell cell = cellIterator.next();
////						
////						String policyNo = cell.getStringCellValue();
////						count++;
////						try{
////						setPolicyData(policyNo);
////						System.out.print("Total count ********* "+count);
////						}catch(Exception e){
////							e.printStackTrace();
////						}
////					}
//					 
//					}catch(Exception e){
//						
//					}
//				}
//				
//				log.info("############### INSURED EMAIL EXECUTION ENDED ###################");
//				
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				//e.printStackTrace();
//			}
//			
//		}
//		
//		public void saveJioPreviousPolicy(File excel) {
//
//	        try {
//				FileInputStream fis = new FileInputStream(excel);
//				org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fis);
//				Sheet sheetAt = workbook.getSheetAt(0);
//				
//				Iterator<Row> rowIterator = sheetAt.iterator();
//				
//				log.info("############### SAVE PREVIOUS JIO INSURED EXECUTION STARTED ###################");
//				int count = 1;
//				while (rowIterator.hasNext()){
//					try{
//					Row row = rowIterator.next();
//					
//					Cell cellPolicyNumber = row.getCell(0);
//					String policyNumber = cellPolicyNumber.getStringCellValue();
//					
//					Cell cellRiskID = row.getCell(1);
//					Double insuredNumer = cellRiskID.getNumericCellValue();
//					String riskID = String.valueOf(insuredNumer.intValue());
//					
//					log.info("!!!!!! POLICY NUMBER FOR SAVE PREVIOUS JIO INSURED UPDATION START ----------------->  " +policyNumber);
//					
//					log.info("!!!!!! NUMBER OF POLICY COUNT ----------------->  " +count);
//					
//					log.info("!!!!!! Policy risk Id  ----------------->  " +riskID);
//					
//					try {
//						PremPolicyDetails policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(policyNumber, riskID);
//						Boolean fetchPolicyDetailsfromPremia = premiaPullService.saveJIOPreviousPolicy(policyDetails, riskID);
//						
//						log.info("!!!!!! POLICY NUMBER FOR SAVE PREVIOUS JIO INSURED UPDATION END----------------->  " +policyNumber);
//						
//						
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					}catch(Exception e){
//						e.printStackTrace();
//					}
//					count++;
//				}
//				
//				log.info("############### SAVE PREVIOUS JIO INSURED EXECUTION ENDED ###################");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		public void savePolicyPortedFlag(File excel) {
//			
//	        try {
//				FileInputStream fis = new FileInputStream(excel);
//				org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fis);
//				Sheet sheetAt = workbook.getSheetAt(0);
//				
//				log.info("############### POLICY PORTED  FLAG EXECUTION STARTED ###################");
//				
//				Iterator<Row> rowIterator = sheetAt.iterator();
//				
//				while (rowIterator.hasNext()){
//					try{
//					Row row = rowIterator.next();
//					
//					Cell cellPolicyNumber = row.getCell(0);
//					String policyNumber = cellPolicyNumber.getStringCellValue();
//					log.info("!!!!!! Policy Number ---------------->  " +policyNumber);
//					
//					PremPolicyDetails fetchGmcPolicyDetailsFromPremia = premiaPullService.fetchPolicyDetailsFromPremia(policyNumber);
//					if(fetchGmcPolicyDetailsFromPremia != null){
//						premiaPullService.savePortedPolicy(fetchGmcPolicyDetailsFromPremia);
//					}
//	
//					}catch(Exception e){
//						
//					}
//				}
//				
//				log.info("############### POLICY PORTED  FLAG EXECUTION ENDED ###################");
//				
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				//e.printStackTrace();
//			}
//			
//		}
//		
//		protected void saveClaimDocuments(File excel){
//			
//			//File excel =  new File ("C:/Users/yosuva.a/Desktop/document_files/updated_list/Claim File-Galaxy_1.xlsx");
//	        try {
//				FileInputStream fis = new FileInputStream(excel);
//				org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fis);
//				Sheet sheetAt = workbook.getSheetAt(0);
//				
//				Iterator<Row> rowIterator = sheetAt.iterator();
//				Path createDirectory = SHAFileUtils.createDirectory();
//				
//				while (rowIterator.hasNext()){
//					try{
//					Row row = rowIterator.next();
//					
//					if(row.getRowNum() >= 1){
//					Cell cellFileName = row.getCell(0);
//					String fileName = null;
//					if(cellFileName != null){
//						fileName = cellFileName.getStringCellValue();
//					}
//					Cell cellDocumentToken = row.getCell(1);
//					Double numericCellValue = cellDocumentToken.getNumericCellValue();
//					Integer tokenValue = 0;
//					if(numericCellValue != null){
//						tokenValue = numericCellValue.intValue();
//					}
//					String token = String.valueOf(tokenValue);
//					//Path createDirectory = SHAFileUtils.createDirectoryTemp(intimationNumber);
//					String fileUrl = SHAFileUtils.viewFileByToken(token);
//					SHAFileUtils.downloadUsingNIO(fileUrl, fileName, createDirectory);
//					}
//					 
//					}catch(Exception e){
//						e.printStackTrace();
//					}
//				}
//				
//				
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			
////			view.setUploadDocumentsDTO(uploadDocumentForCloseClaim);
//			
//		}
//		
//		public static void downloadUsingNIO(String fileUrl,String filename,Path directory) throws IOException {
//			 try{
////				 /*filename = "others.pdf";
////				 fileU*/rl = "http://www.alfs.starhealth.in:8080/alfresco/webdav/Galaxy/2016/01/14/14/73516_ID PROOF.PDF?ticket=TICKET_25b909e0e252de876561c8473ac5f4260b895a6b";
//
//			    String filePath = directory.toString();
//			    filePath = filePath+File.separator+filename;
//		        URL url = new URL(fileUrl);
//		        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
//		        FileOutputStream fos = new FileOutputStream(filePath);
//		        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
//		        fos.close();
//		        rbc.close();
//			 }catch(Exception e){
//				 e.printStackTrace();
//			 }
//		   }
//		
//}
//

