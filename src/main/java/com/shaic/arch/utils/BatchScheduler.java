package com.shaic.arch.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.mapper.AcknowledgeDocumentReceivedMapper;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.GalaxyIntimationTable;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredDto;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.OPPremiaIntimationTable;
import com.shaic.domain.Policy;
import com.shaic.domain.PreauthService;
import com.shaic.domain.PremiaEndorsementTable;
import com.shaic.domain.PremiaIntimationTable;
import com.shaic.domain.Product;
import com.shaic.domain.bancs.BaNCSIntimationTable;
import com.shaic.domain.bancs.BaNCSPullService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.preauth.StarfaxProvisionHistory;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.InsuredMapper;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.domain.PolicyMapper;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.paclaim.printbulkreminder.SearchPAPrintRemainderBulkService;
import com.shaic.reimbursement.reminderBulkSearch.SearchGenerateRemainderBulkService;
import com.shaic.starfax.simulation.PremiaPullService;
import com.shaic.starfax.simulation.StarFaxFVRServiceRevised;
import com.shaic.starfax.simulation.StarFaxServiceRevised;

@Singleton
@Startup()
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class BatchScheduler
{
	private final Logger log = LoggerFactory.getLogger(BatchScheduler.class);
	public static String dataDir = System.getProperty("jboss.server.data.dir");
  // Here we inject a reference to the timerService
	 @Resource
	 private TimerService timerService;
	 
	 @EJB
	 private PremiaPullService premiaPullService;
  
	 @EJB
	 private StarFaxServiceRevised starFaxRevisedService;
	 
	 @EJB
	 private StarFaxFVRServiceRevised starFaxFVRRevisedService;
	
	 @EJB
	 private SearchGenerateRemainderBulkService generateBulkReminderService;
	 
	 @EJB
	 private SearchPAPrintRemainderBulkService generatePABulkRemidnerService;
	 
	 @EJB
	 private ReimbursementService reimbursementService;
	 
	 @EJB
	 private MasterService masterService;
	 
	 @EJB
	 private PreauthService preauthService;
	 
	 @EJB
	 private IntimationService intimationService;
	 
	 @EJB
	 private OMPIntimationService ompIntimationService;
	 
	public static String SHOULD_BATCH_RUN = null;
	
	public static String BATCH_SIZE = null;
	
	public static String SHOULD_SCHEDULE_START = null;
	
	@EJB
	private BaNCSPullService bancsPullService;
	
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	
	static {
		Properties prop = readConnectionPropertyFile();
		SHOULD_BATCH_RUN = prop.getProperty("should_batch_run");
		BATCH_SIZE = prop.getProperty("batch_size");
	}

		@PostConstruct
	    private void init() {
			log.info("**************TIMER SERVICE INITTTTTTTTTTTTTTTTTTTT***********************");
			if(SHOULD_BATCH_RUN != null && SHOULD_BATCH_RUN.equalsIgnoreCase("yes")) {

				TimerConfig timeConfigForPremiaPull = new TimerConfig(SHAConstants.PREMIA_PULL, false);
				timeConfigForPremiaPull.setInfo(SHAConstants.PREMIA_PULL);
				ScheduleExpression exp = new ScheduleExpression();
				exp.hour("*").minute("*").second("*/15");
			    timerService.createCalendarTimer(exp, timeConfigForPremiaPull);
			    
			    TimerConfig timeConfigForOPPremiaPull = new TimerConfig(SHAConstants.OP_PREMIA_PULL, false);
			    timeConfigForOPPremiaPull.setInfo(SHAConstants.OP_PREMIA_PULL);
				ScheduleExpression expOP = new ScheduleExpression();
				expOP.hour("*").minute("*").second("*/15");
			    timerService.createCalendarTimer(expOP, timeConfigForOPPremiaPull);
			    
			    TimerConfig timeConfigForOPPremiaPullCashless = new TimerConfig(SHAConstants.OP_PREMIA_PULL_CASHLESS, false);
			    timeConfigForOPPremiaPullCashless.setInfo(SHAConstants.OP_PREMIA_PULL_CASHLESS);
				ScheduleExpression expOPCashelss = new ScheduleExpression();
				expOPCashelss.hour("*").minute("*").second("*/15");
			    timerService.createCalendarTimer(expOPCashelss, timeConfigForOPPremiaPullCashless);
//			    
			    TimerConfig timeConfigForPremiaPullGMC = new TimerConfig(SHAConstants.PREMIA_PULL_GMC_NETORK, false);
			    timeConfigForPremiaPullGMC.setInfo(SHAConstants.PREMIA_PULL_GMC_NETORK);
				ScheduleExpression expGmc = new ScheduleExpression();
				expGmc.hour("*").minute("*").second("*/15");
			    timerService.createCalendarTimer(expGmc, timeConfigForPremiaPullGMC);
//			    
			    TimerConfig timeConfigForPremiaPullForNonNetwork = new TimerConfig(SHAConstants.PREMIA_PULL_NON_NETWORK, false);
			    timeConfigForPremiaPullForNonNetwork.setInfo(SHAConstants.PREMIA_PULL_NON_NETWORK);
				ScheduleExpression expNonNetwork = new ScheduleExpression();
				expNonNetwork.hour("*").minute("*").second("*/30");
			    timerService.createCalendarTimer(expNonNetwork, timeConfigForPremiaPullForNonNetwork);
////			        
			    TimerConfig timeConfigForDocUpload = new TimerConfig(SHAConstants.DOC_UPLOAD, false); 
			    timeConfigForDocUpload.setInfo(SHAConstants.DOC_UPLOAD);
		        ScheduleExpression exp1 = new ScheduleExpression();
		        exp1.hour("*").minute("*").second("*/30");
		        //Timer createCalendarTimer = timerService.createCalendarTimer(exp1, timeConfigForDocUpload);
		        timerService.createCalendarTimer(exp1, timeConfigForDocUpload);
////////		        
////////		        
		        TimerConfig timeConfigForDocUploadFVR = new TimerConfig(SHAConstants.DOC_UPLOAD_FVR, false); 
		        timeConfigForDocUploadFVR.setInfo(SHAConstants.DOC_UPLOAD_FVR);
		        ScheduleExpression exp2 = new ScheduleExpression();
		        exp2.hour("*").minute("*/1");
		        timerService.createCalendarTimer(exp2, timeConfigForDocUploadFVR);
//		        
		        TimerConfig timeConfigForDocUploadFVRError = new TimerConfig(SHAConstants.DOC_UPLOAD_FVR_ERROR, false); 
		        timeConfigForDocUploadFVRError.setInfo(SHAConstants.DOC_UPLOAD_FVR_ERROR);
		        ScheduleExpression exp2Error = new ScheduleExpression();
		        exp2Error.hour("*").minute("*/1");
		        timerService.createCalendarTimer(exp2Error, timeConfigForDocUploadFVRError);
//////		        
		        TimerConfig timeConfigForDocUploadWithNoClaim = new TimerConfig(SHAConstants.DOC_UPLOAD_WITH_NO_CLAIM, false); 
		        timeConfigForDocUploadWithNoClaim.setInfo(SHAConstants.DOC_UPLOAD_WITH_NO_CLAIM);
		        ScheduleExpression exp3 = new ScheduleExpression();
		        exp3.hour("*").minute("*/1");
		        timerService.createCalendarTimer(exp3, timeConfigForDocUploadWithNoClaim);
//////		        
		        TimerConfig timeConfigForXRayBatch = new TimerConfig(SHAConstants.X_REPORT_BATCH, false); 
		        timeConfigForXRayBatch.setInfo(SHAConstants.X_REPORT_BATCH);
		        ScheduleExpression exp4 = new ScheduleExpression();
		        exp4.hour("*").minute("*/1");
		        timerService.createCalendarTimer(exp4, timeConfigForXRayBatch);
////////		        
		        TimerConfig timeConfigForUniquePremiumBatch = new TimerConfig(SHAConstants.UNIQUE_PREMIUM_BATCH, false); 
		        timeConfigForUniquePremiumBatch.setInfo(SHAConstants.UNIQUE_PREMIUM_BATCH);
		        ScheduleExpression exp5 = new ScheduleExpression();
		        exp5.hour("*").minute("*/1");
		        timerService.createCalendarTimer(exp5, timeConfigForUniquePremiumBatch);
////		        
//////		        TimerConfig reminderPreauthApprovedBatch = new TimerConfig(SHAConstants.PREAUTH_REMINDER, false); 
//////		        reminderPreauthApprovedBatch.setInfo(SHAConstants.PREAUTH_REMINDER);
//////		        ScheduleExpression exp6 = new ScheduleExpression();
//////		        exp6.hour("*").minute("*/1");
//////		        timerService.createCalendarTimer(exp6, reminderPreauthApprovedBatch);
//////		        
//////		        TimerConfig reminderEnhancementApprovedBatch = new TimerConfig(SHAConstants.ENHANCEMENT_REMINDER, false); 
//////		        reminderEnhancementApprovedBatch.setInfo(SHAConstants.ENHANCEMENT_REMINDER);
//////		        ScheduleExpression exp7 = new ScheduleExpression();
//////		        exp7.hour("*").minute("*/1");
//////		        timerService.createCalendarTimer(exp7, reminderEnhancementApprovedBatch);
//////		        
//		       
////		        
			    TimerConfig timeConfigForDocUploadDesc = new TimerConfig(SHAConstants.DOC_UPLOAD_DESCENDING, false); 
			    timeConfigForDocUploadDesc.setInfo(SHAConstants.DOC_UPLOAD_DESCENDING);
		        ScheduleExpression exp10 = new ScheduleExpression();
		        exp10.hour("*").minute("*").second("*/30");
		        //Timer createCalendarTimer = timerService.createCalendarTimer(exp1, timeConfigForDocUpload);
		        timerService.createCalendarTimer(exp10, timeConfigForDocUploadDesc);
//				
		        Calendar cal3 = Calendar.getInstance();
		        cal3.set(Calendar.HOUR_OF_DAY,06);
		        cal3.set(Calendar.MINUTE,0);
		        cal3.set(Calendar.SECOND,0);
		        cal3.set(Calendar.MILLISECOND,0);
		        Date currentDate3 = cal3.getTime();
		        
				TimerConfig timeConfigForICR = new TimerConfig(SHAConstants.ICR_CALCULATION_RATIO, false);
		        timeConfigForICR.setInfo(SHAConstants.ICR_CALCULATION_RATIO);
/*				ScheduleExpression expICR = new ScheduleExpression();
				expICR.hour("6").minute("*");*/
			    timerService.createSingleActionTimer(currentDate3, timeConfigForICR);
		        
		        Calendar cal = Calendar.getInstance();
		        cal.set(Calendar.HOUR_OF_DAY,07);
		        cal.set(Calendar.MINUTE,0);
		        cal.set(Calendar.SECOND,0);
		        cal.set(Calendar.MILLISECOND,0);
		        Date currentDate = cal.getTime();

			    
			    TimerConfig reminderWithdrawBatch = new TimerConfig(SHAConstants.PA_BULK_REMINDER, false); 
		        reminderWithdrawBatch.setInfo(SHAConstants.PA_BULK_REMINDER);
		       /* ScheduleExpression exp8 = new ScheduleExpression();
		        exp8.hour("6").minute("*");*/
		        //timerService.createCalendarTimer(exp8, reminderWithdrawBatch);
		        timerService.createSingleActionTimer(currentDate, reminderWithdrawBatch);
////////				
		        Calendar cal1 = Calendar.getInstance();
		        cal1.set(Calendar.HOUR_OF_DAY,07);
		        cal1.set(Calendar.MINUTE,0);
		        cal1.set(Calendar.SECOND,0);
		        cal1.set(Calendar.MILLISECOND,0);
		        Date currentDate1 = cal1.getTime();
		        
		        TimerConfig autoReminderLetterBatch = new TimerConfig(SHAConstants.AUTO_GEN_BULK_REMINDER_BATCH, false); 
		        autoReminderLetterBatch.setInfo(SHAConstants.AUTO_GEN_BULK_REMINDER_BATCH);
//		        ScheduleExpression exp9 = new ScheduleExpression();
//		        exp9.hour("6").minute("*");
		        timerService.createSingleActionTimer(currentDate1, autoReminderLetterBatch);
		        //timerService.createCalendarTimer(exp9, autoReminderLetterBatch);
//		        
		        TimerConfig updateProvisonAmtTimer = new TimerConfig(SHAConstants.UPDATE_PROVISON_AMOUNT_STARFAX_DOC, false); 
		        updateProvisonAmtTimer.setInfo(SHAConstants.UPDATE_PROVISON_AMOUNT_STARFAX_DOC);
		        ScheduleExpression updateExp = new ScheduleExpression();
		        updateExp.hour("*").minute("*").second("*/30");
		        //Timer createCalendarTimer = timerService.createCalendarTimer(exp1, timeConfigForDocUpload);
		        timerService.createCalendarTimer(updateExp, updateProvisonAmtTimer);
		        
		        TimerConfig endorsementTimer = new TimerConfig(SHAConstants.POLICY_ENDORSEMENT_BATCH, false); 
		        endorsementTimer.setInfo(SHAConstants.POLICY_ENDORSEMENT_BATCH);
		        ScheduleExpression schedule = new ScheduleExpression();
		        schedule.hour("*").minute("*/1");
		        //Timer createCalendarTimer = timerService.createCalendarTimer(exp1, timeConfigForDocUpload);
		        
		        timerService.createCalendarTimer(schedule, endorsementTimer);

		        TimerConfig timerSTP = new TimerConfig(SHAConstants.STP_PREAUTH_PROCESS_BATCH, false); 
		        timerSTP.setInfo(SHAConstants.STP_PREAUTH_PROCESS_BATCH);
		        ScheduleExpression timerSchedule = new ScheduleExpression();
		        timerSchedule.hour("*").minute("*/3");
		        timerService.createCalendarTimer(timerSchedule, timerSTP);
		        
		        TimerConfig endorsementGMCTimer = new TimerConfig(SHAConstants.POLICY_ENDORSEMENT_BATCH_GMC, false); 
		        endorsementGMCTimer.setInfo(SHAConstants.POLICY_ENDORSEMENT_BATCH_GMC);
		        ScheduleExpression scheduleGMC = new ScheduleExpression();
		        scheduleGMC.hour("*").minute("*/1");
		        
		        timerService.createCalendarTimer(scheduleGMC, endorsementGMCTimer);
		        
		        TimerConfig endorsementGPATimer = new TimerConfig(SHAConstants.POLICY_ENDORSEMENT_BATCH_GPA, false); 
		        endorsementGPATimer.setInfo(SHAConstants.POLICY_ENDORSEMENT_BATCH_GPA);
		        ScheduleExpression scheduleGPA = new ScheduleExpression();
		        scheduleGPA.hour("*").minute("*/1");
		        
		        timerService.createCalendarTimer(scheduleGPA, endorsementGPATimer);
		        
		        TimerConfig ompPaymentTimer = new TimerConfig(SHAConstants.OMP_PAYMENT_LETTER_BATCH, false); 
		        ompPaymentTimer.setInfo(SHAConstants.OMP_PAYMENT_LETTER_BATCH);
		        ScheduleExpression scheduleOMP = new ScheduleExpression();
		        scheduleOMP.hour("*").minute("*/59");
		        
		        timerService.createCalendarTimer(scheduleOMP, ompPaymentTimer);
		        
		        
		        TimerConfig timeConfigForBaNCSPull = new TimerConfig(SHAConstants.BANCS_PULL, false);
		        timeConfigForBaNCSPull.setInfo(SHAConstants.BANCS_PULL);
				ScheduleExpression expBaNCS = new ScheduleExpression();
				expBaNCS.hour("*").minute("*").second("*/15");
			    timerService.createCalendarTimer(expBaNCS, timeConfigForBaNCSPull);
			    
			    TimerConfig timeConfigForBaNCSPullForNonNetwork = new TimerConfig(SHAConstants.BANCS_PULL_NON_NETWORK, false);
			    timeConfigForBaNCSPullForNonNetwork.setInfo(SHAConstants.BANCS_PULL_NON_NETWORK);
				ScheduleExpression expNonNetworkBaNCS = new ScheduleExpression();
				expNonNetworkBaNCS.hour("*").minute("*").second("*/30");
			    timerService.createCalendarTimer(expNonNetworkBaNCS, timeConfigForBaNCSPullForNonNetwork);

			    TimerConfig endorsementGMCDescTimer = new TimerConfig(SHAConstants.POLICY_ENDORSEMENT_BATCH_GMC_DESC, false); 
		        endorsementGMCDescTimer.setInfo(SHAConstants.POLICY_ENDORSEMENT_BATCH_GMC_DESC);
		        ScheduleExpression scheduleGMCDesc = new ScheduleExpression();
		        scheduleGMCDesc.hour("*").minute("*/1");
		        timerService.createCalendarTimer(scheduleGMCDesc, endorsementGMCDescTimer);
		        
		      //Added For Accident Trauma care Group product		
				TimerConfig timeConfigForPAPremiaPull = new TimerConfig(SHAConstants.PREMIA_PULL_ACCIDENT_PA, false);
				timeConfigForPAPremiaPull.setInfo(SHAConstants.PREMIA_PULL_ACCIDENT_PA);
				ScheduleExpression schedulePA = new ScheduleExpression();
				schedulePA.hour("*").minute("*/59");
			    timerService.createCalendarTimer(schedulePA, timeConfigForPAPremiaPull);
			    
			    //Added For document Ack automatic creation batch only for cashless claim, if ack not received more than 7days		
				TimerConfig timeConfigForAckDocumentCreationBatch = new TimerConfig(SHAConstants.ACK_DOCUEMENT_CREATION_BATCH, false);
				timeConfigForAckDocumentCreationBatch.setInfo(SHAConstants.ACK_DOCUEMENT_CREATION_BATCH);
				ScheduleExpression scheduleAckBatch = new ScheduleExpression();
				scheduleAckBatch.hour("*").minute("*/59");
			    timerService.createCalendarTimer(scheduleAckBatch, timeConfigForAckDocumentCreationBatch);
			    
			    TimerConfig timeConfigForMA = new TimerConfig(SHAConstants.ACK_DOCUMENT_CREATION_BY_FALG, false);
			    timeConfigForMA.setInfo(SHAConstants.ACK_DOCUMENT_CREATION_BY_FALG);
				ScheduleExpression scheduleMA = new ScheduleExpression();
				scheduleMA.hour("*").minute("*/1");
			    timerService.createCalendarTimer(scheduleMA, timeConfigForMA);
			    
			    TimerConfig timeConfigForGalaxyPull = new TimerConfig(SHAConstants.GALAXY_PULL, false);
			    timeConfigForGalaxyPull.setInfo(SHAConstants.GALAXY_PULL);
				ScheduleExpression galExp = new ScheduleExpression();
				galExp.hour("*").minute("*").second("*/15");
			    timerService.createCalendarTimer(galExp, timeConfigForGalaxyPull);
			    
			    TimerConfig timeConfigForGalaxyPullGMC = new TimerConfig(SHAConstants.GALAXY_PULL_GMC_NETORK, false);
			    timeConfigForGalaxyPullGMC.setInfo(SHAConstants.GALAXY_PULL_GMC_NETORK);
				ScheduleExpression galExpGmc = new ScheduleExpression();
				galExpGmc.hour("*").minute("*").second("*/15");
			    timerService.createCalendarTimer(galExpGmc, timeConfigForGalaxyPullGMC);
//			    
			    TimerConfig timeConfigForGalaxyPullForNonNetwork = new TimerConfig(SHAConstants.GALAXY_PULL_NON_NETWORK, false);
			    timeConfigForGalaxyPullForNonNetwork.setInfo(SHAConstants.GALAXY_PULL_NON_NETWORK);
				ScheduleExpression galExpNonNetwork = new ScheduleExpression();
				galExpNonNetwork.hour("*").minute("*").second("*/30");
			    timerService.createCalendarTimer(galExpNonNetwork, timeConfigForGalaxyPullForNonNetwork);
			    
			  //Added For Accident Trauma care Group product		
				TimerConfig timeConfigForPAGalaxyPull = new TimerConfig(SHAConstants.GALAXY_PULL_ACCIDENT_PA, false);
				timeConfigForPAGalaxyPull.setInfo(SHAConstants.GALAXY_PULL_ACCIDENT_PA);
				ScheduleExpression galSchedulePA = new ScheduleExpression();
				galSchedulePA.hour("*").minute("*/59");
			    timerService.createCalendarTimer(galSchedulePA, timeConfigForPAGalaxyPull);
			    
			    TimerConfig timeConfigForPremiaPullNonNetworkError = new TimerConfig(SHAConstants.PREMIA_PULL_NON_NETWORK_ERROR, false);
			    timeConfigForPremiaPullNonNetworkError.setInfo(SHAConstants.PREMIA_PULL_NON_NETWORK_ERROR);
				ScheduleExpression expNonNetworkError = new ScheduleExpression();
				expNonNetworkError.hour("*").minute("*/59");
			    timerService.createCalendarTimer(expNonNetworkError, timeConfigForPremiaPullNonNetworkError);
			    
			    
			    TimerConfig timeConfigForGlxPullNonNetworkError = new TimerConfig(SHAConstants.GLX_PULL_NON_NETWORK_ERROR, false);
			    timeConfigForGlxPullNonNetworkError.setInfo(SHAConstants.GLX_PULL_NON_NETWORK_ERROR);
				ScheduleExpression expGlxNonNetworkError = new ScheduleExpression();
				expGlxNonNetworkError.hour("*").minute("*/59");
			    timerService.createCalendarTimer(expNonNetworkError, timeConfigForGlxPullNonNetworkError);
			    
//			    TimerConfig timeConfigForInstallmentPremiumBatch = new TimerConfig(SHAConstants.INSTALLMENT_PREMIUM_BATCH, false); 
//		        timeConfigForUniquePremiumBatch.setInfo(SHAConstants.INSTALLMENT_PREMIUM_BATCH);
//		        ScheduleExpression instamentBth = new ScheduleExpression();
//		        exp5.hour("*").minute("*/1");
//		        timerService.createCalendarTimer(exp5, timeConfigForUniquePremiumBatch);
			}
	    }
		

		
		  @Timeout	
		  @AccessTimeout(value = 20, unit = TimeUnit.MINUTES) 
		  public void scheduledTimeout(Timer timer) {
			  try {
				  if(timer.getInfo() != null) {
					  if(SHAConstants.PREMIA_PULL.equals(timer.getInfo())) {
						  log.info("###############STARTED PREMIA PULL FOR EVERY 30 SECONDS FOR NON GMC NETWORK ###################");
						  doPremiaIntimationPull();
					  } else if(SHAConstants.OP_PREMIA_PULL.equals(timer.getInfo())) {
						  log.info("###############STARTED PREMIA PULL FOR EVERY 30 SECONDS FOR OP REIMBURSEMENT ###################");
						  doPremiaIntimationPullForOP();
					  }else if(SHAConstants.OP_PREMIA_PULL_CASHLESS.equals(timer.getInfo())) {
						  log.info("###############STARTED PREMIA PULL FOR EVERY 30 SECONDS FOR OP CASHLESS ###################");
						  doPremiaIntimationPullForOPCashless();
					  } else if(SHAConstants.PREMIA_PULL_GMC_NETORK.equals(timer.getInfo())) {
						  log.info("###############STARTED PREMIA PULL FOR EVERY 30 SECONDS FOR GMC NETWORK ###################");
						  doPremiaIntimationPullForGmc();
					  } else  if(SHAConstants.PREMIA_PULL_NON_NETWORK.equals(timer.getInfo())) {
						  log.info("###############STARTED PREMIA PULL FOR EVERY 30 SECONDS FOR NON NETWORK###################");
						  doPremiaIntimationPullForNonNetwork(false);
					  } else if(SHAConstants.DOC_UPLOAD.equals(timer.getInfo())) {
						  log.info("############### STARTED DOC UPLOAD WITHOUT FVR FOR EVERY 30 seconds ###################");
						  doPremiaPullForDocUploadWithoutFVR();
					  } else if(SHAConstants.DOC_UPLOAD_FVR.equals(timer.getInfo())) {
						  log.info("################### STARTED DOC UPLOAD WITH FVR FOR EVERY 1 MINUTES ################");
						  doPremiaPullForDocUploadFVR();
						  
					  }else if(SHAConstants.DOC_UPLOAD_FVR_ERROR.equals(timer.getInfo())) {
						  log.info("################### STARTED DOC UPLOAD WITH FVR FOR EVERY 1 MINUTES (FVR ERROR BATCH) ################");
						  doPremiaPullForDocUploadFVRError();
						  
					  } else if(SHAConstants.DOC_UPLOAD_WITH_NO_CLAIM.equals(timer.getInfo())) {
						  log.info("################### STARTED DOC UPLOAD FOR NO CLAIM CASE FOR EVERY 1 MINUTE ################");
						  doPremiaPullForDocNoClaim();
					  }
					  else if(SHAConstants.X_REPORT_BATCH.equals(timer.getInfo())) {
						  log.info("################### STARTED X RAY REPORT FOR EVERY 3 MINUTE ################");
						  saveXRAYRecordsInGALAXY();
					  } else if(SHAConstants.UNIQUE_PREMIUM_BATCH.equals(timer.getInfo())) {
						  log.info("################### STARTED UNIQUE PREMIUM BATCH FOR EVERY 2 MINUTES ################");
						  callPremiaWSForUniquePremium();
					  }
//					  else if(SHAConstants.PREAUTH_REMINDER.equals(timer.getInfo())) {
//						  log.info("################### STARTED PREAUTH REMINDER BATCH FOR EVERY 1 MINUTES ################");
//						  callPreauthReminderProcess();
//					  } else if(SHAConstants.ENHANCEMENT_REMINDER.equals(timer.getInfo())) {
//						  log.info("################### STARTED PREAUTH ENHANCEMENT REMINDER BATCH FOR EVERY 1 MINUTES ################");
//						  callPreauthReminderProcessForEnhancement();
//					  } 
//					  else if(SHAConstants.WITHDRAW_REMINDER.equals(timer.getInfo())) {
//						  log.info("################### STARTED WITHDRAW REMINDER BATCH FOR EVERY 1 MINUTES ################");
//						  callReminderProcessForWithdraw();
//					  } 
					  
					  /***
					   * This block is for Auto Generation of Bulk Reminder Letters
					   * 
					   */
					  else if(SHAConstants.AUTO_GEN_BULK_REMINDER_BATCH.equals(timer.getInfo())) {
						  log.info("################### STARTED AUTO GEN. BULK REMINDER BATCH FOR EVERY 24 HOURS ################");
						  callAutoGenerateBulkReminderLetter();
					  } 
					  else if(SHAConstants.PA_BULK_REMINDER.equals(timer.getInfo())) {
						  log.info("################### STARTED AUTO GEN. BULK REMINDER BATCH FOR EVERY 24 HOURS ################");
						  callAutoGenerateBulkReminderLetterForPA();
					  } else if(SHAConstants.DOC_UPLOAD_DESCENDING.equals(timer.getInfo())) {
						  log.info("############### STARTED DOC UPLOAD WITHOUT FVR FOR EVERY 30 MINUTES (DESCENDING) ###################");
						  doPremiaPullForDocUploadWithoutFVRForDescending();
					  } else if(SHAConstants.ICR_CALCULATION_RATIO.equals(timer.getInfo())) {
						  log.info("################### STARTED ICR CACULATION RATIO FOR EVERY 24 HOURS ################");
						  calculateICR();
					  } else if(SHAConstants.UPDATE_PROVISON_AMOUNT_STARFAX_DOC.equals(timer.getInfo())) {
						  log.info("################### STARTED PROVISION UPDATE TO PREMIA FOR EVERY 30 SECS ################");
						  updateProvisionToPremiaForStarFax();
					  } else if(SHAConstants.POLICY_ENDORSEMENT_BATCH.equals(timer.getInfo())) {
						  doPremiaEndorsementDetails();
					  } else if(SHAConstants.POLICY_ENDORSEMENT_BATCH_GMC.equals(timer.getInfo())) {
						  log.info("################### STARTED GMC ENDORSEMENT ASC ################");
						  doPremiaEndorsementDetailsForGMC(false);
					  } else if(SHAConstants.POLICY_ENDORSEMENT_BATCH_GPA.equals(timer.getInfo())) {
						  doPremiaEndorsementDetailsForGPA();
					  } else if(SHAConstants.STP_PREAUTH_PROCESS_BATCH.equals(timer.getInfo())) {
						  log.info("################### STARTED STP PROCESS ################");
						  processSTPProcess();
					  } else if(SHAConstants.OMP_PAYMENT_LETTER_BATCH.equals(timer.getInfo())) {
						  log.info("################### STARTED Omp Payment Letter Generation PROCESS ################");
						  ompPaymentLetterGenerationProcess();

						  }
					  else if(SHAConstants.BANCS_PULL.equals(timer.getInfo())) {
						  log.info("###############STARTED BANCS PULL FOR EVERY 30 SECONDS FOR NON GMC NETWORK ###################");
						  doBaNCSIntimationPull();
					  }
					  else  if(SHAConstants.BANCS_PULL_NON_NETWORK.equals(timer.getInfo())) {
						  log.info("###############STARTED BANCS PULL FOR EVERY 30 SECONDS FOR NON GMC NON-NETWORK ###################");
						  doBaNCSIntimationPullForNonNetwork();
					  }
					  else if(SHAConstants.POLICY_ENDORSEMENT_BATCH_GMC_DESC.equals(timer.getInfo())) {
						  log.info("################### STARTED GMC ENDORSEMENT DESC ################");
						  doPremiaEndorsementDetailsForGMC(true);
					  }
					//Added For Accident Trauma care Group product		
					  else if(SHAConstants.PREMIA_PULL_ACCIDENT_PA.equals(timer.getInfo())) {
						  log.info("###############STARTED PREMIA PULL FOR EVERY 30 SECONDS FOR ACCIDENT PA ACC-PRD-008###################");
						  doPremiaIntimationPullForPA();
					  }
					  //Added For document Ack automatic creation batch only for cashless claim, if ack not received more than 7days		
					  else if(SHAConstants.ACK_DOCUEMENT_CREATION_BATCH.equals(timer.getInfo())) {
						  log.info("###############STARTED ACK INTIMATION PULL FOR EVERY 1HOUR FOR AUTOMATIC ACK CREATION###################");
						  ackDocumentCreationBatch();
					  }
					  else if(SHAConstants.ACK_DOCUMENT_CREATION_BY_FALG.equals(timer.getInfo())) {
						  log.info("################### STARTED AUTO DOCUMENT CREATION FOR EVERY 5 MINUTES ################");
						  doCreateNewAckDocForTopupIntimation();
					  }else if(SHAConstants.GALAXY_PULL.equals(timer.getInfo())) {
						  log.info("###############STARTED GALAXY PULL FOR EVERY 30 SECONDS FOR NON GMC NETWORK ###################");
						  doGalaxyIntimationPull();
					  } else if(SHAConstants.GALAXY_PULL_GMC_NETORK.equals(timer.getInfo())) {
						  log.info("###############STARTED GALAXY PULL FOR EVERY 30 SECONDS FOR GMC NETWORK ###################");
						  doGalaxyIntimationPullForGmc();
					  } else  if(SHAConstants.GALAXY_PULL_NON_NETWORK.equals(timer.getInfo())) {
						  log.info("###############STARTED GALAXY PULL FOR EVERY 30 SECONDS FOR NON NETWORK###################");
						  doGalaxyIntimationPullForNonNetwork(false);
					  }//Added For Accident Trauma care Group product		
					  else if(SHAConstants.GALAXY_PULL_ACCIDENT_PA.equals(timer.getInfo())) {
						  log.info("###############STARTED GALAXY PULL FOR EVERY 30 SECONDS FOR ACCIDENT PA ACC-PRD-008###################");
						  doGalaxyIntimationPullForPA();
					  }
					  else if(SHAConstants.PREMIA_PULL_NON_NETWORK_ERROR.equals(timer.getInfo())) {
						  log.info("###############STARTED PREMIA PULL FOR EVERY 1 Hr FOR NON NETWORK ERROR###################");
						  doPremiaIntimationPullForNonNetwork(true);
					  }
					  
					  else if(SHAConstants.GLX_PULL_NON_NETWORK_ERROR.equals(timer.getInfo())) {
						  log.info("###############STARTED GLX PULL FOR EVERY 1 Hr FOR NON NETWORK ERROR###################");
						  doGalaxyIntimationPullForNonNetwork(true);
					  }
//					  else if(SHAConstants.INSTALLMENT_PREMIUM_BATCH.equals(timer.getInfo())) {
//						  log.info("################### STARTED INSTALLMENT PREMIUM BATCH FOR EVERY 2 MINUTES ################");
//						  callPremiaWSForInstallmentPremium();
//					  }
					  
					  
					  }

			  } catch (Exception e) {
				  e.printStackTrace();
			  }
			  
		  }
  
		  
		//CR2019034
			
			public void ompPaymentLetterGenerationProcess(){
				
				ompIntimationService.ompPaymentLetterGenerationProcess();
			}
			
			public void doPremiaIntimationPull() {
				List<PremiaIntimationTable> processPremiaIntimationData = premiaPullService.processPremiaIntimationData("20");
				if(processPremiaIntimationData != null && !processPremiaIntimationData.isEmpty()) {
					
					for (PremiaIntimationTable premiaIntimationTable : processPremiaIntimationData) {
						log.info("@@@@@@@@@ INTIMATION PULL STARTING TIME FOR  NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
						try {
							if(!premiaPullService.isIntimationExist(premiaIntimationTable.getGiIntimationNo())) {
								if((null != premiaIntimationTable.getGiHospCode() && !premiaIntimationTable.getGiHospCode().isEmpty()) && premiaPullService.isPolicyAllowed(premiaIntimationTable.getGiPolNo())) {
									String policyServiceType = "OTHERS";
									Boolean isIntegratedPolicy = true;
									if(premiaIntimationTable.getProductCode() != null){
										Product productByProductCode = premiaPullService.getProductByProductCode(premiaIntimationTable.getProductCode());
										if(productByProductCode != null && productByProductCode.getProductService() != null){
											policyServiceType = productByProductCode.getProductService();
										}
									}
									PremPolicyDetails policyDetails = null;
									
									log.info("@@@@@@@@@PREMIA WEB SERVICE CALL STARTING TIME FOR  NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									
									
									if(policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
										policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(premiaIntimationTable
												.getGiPolNo(), premiaIntimationTable.getGiInsuredName());
										isIntegratedPolicy =premiaPullService. populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getGiInsuredName(),false);
										
									}else if(policyServiceType.equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)){
										policyDetails = premiaPullService.fetchGpaPolicyDetailsFromPremia(premiaIntimationTable
												.getGiPolNo(), premiaIntimationTable.getGiInsuredName());
										policyDetails.setRiskSysId(premiaIntimationTable.getGiInsuredName());
										isIntegratedPolicy = premiaPullService.populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getGiInsuredName(),false);
									}else if(policyServiceType.equalsIgnoreCase(SHAConstants.JET_PRIVILLAGE_POL_SERVICE)){
										policyDetails = premiaPullService.fetchJetPrivillagePolicyDetailsFromPremia(premiaIntimationTable
												.getGiPolNo());
										isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getGiInsuredName(),false);
									}else{
										policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(premiaIntimationTable
												.getGiPolNo());
										isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getGiInsuredName(),false);
									}
									log.info("@@@@@@@@@ PREMIA WEB SERVICE CALL ENDING TIME FOR  NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									
									if(isIntegratedPolicy) {
										
										log.info("@@@@@@@@@ INSERT INTIMATION STARTING TIME FOR  NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
//										Intimation intimation = premiaPullService.insertIntimationDataRevised(premiaIntimationTable);
										Intimation intimation = premiaPullService.insertIntimationDataRevised(premiaIntimationTable,policyDetails.getLob());
										
										log.info("@@@@@@@@@ INSERT INTIMATION ENDING TIME FOR  NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
										
										if(null != intimation ){
											log.info("***** SUCCESSFULLY INITMATION CREATED FOR  NON GMC NETWORK ********************-----> " + intimation.getIntimationId());
										}
										else {
											log.info("***************** Failed to Create INITMATION for Premia Intimation ********************-----> "+ premiaIntimationTable.getGiIntimationNo());
											premiaPullService.updatePremiaIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
										}
										if(null != intimation && null != intimation.getHospital() && null != intimation.getCpuCode()) {
											log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME  NON GMC  @@@@@@@@@@-----> "+ System.currentTimeMillis());
											premiaPullService.processSavedIntimationRevised(intimation, premiaIntimationTable);
											log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME  NON GMC @@@@@@@@@@-----> "+ System.currentTimeMillis());
										} 
									} else {
										log.info("#^#^^#^#^#THIS PRODUCT IS NOT INTREGRATED IN GALAXY----S-- OR PRODUCT CODE IS WRONG PLEASE CHECK THE POLCIY NUMBER OR POLICY NOT SAVED WHILE SAVING THE POLICY DATA---> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode() + " Policy No---> " + premiaIntimationTable.getGiPolNo() );
									}
								} else {
									log.info("#*#*#*#**#*#*#*#*#*#*#* THIS POLICY IS NOT ALLOWED IN GALAXY----BASED ON INTIMATION IMPORT VALIDATION VALUES *#*#*#*#*#*#*#*#*#-----> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode());
									premiaPullService.updatePremiaIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
								}
							} else {
								log.info("%*&%*%&%*%&%*%&%*(%*% THIS INTIMATION IS ALREADY EXISTING PLEASE CHECK #*@&(#&@(@*@(*@*(@ --->" + premiaIntimationTable.getGiIntimationNo());
								premiaPullService.updatePremiaIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
						
						log.info("@@@@@@@@@ INTIMATION PULL ENDING TIME @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
					}
					
					
				}
			}
			
			
			
			/**
			 * Added for x ray batch.
			 * */
			public void saveXRAYRecordsInGALAXY()
			{
				premiaPullService.processXRayRecord("20");
			}
		  
			
		public Boolean doPremiaPullForDocUploadWithoutFVR() {
			try {
				List<DocUploadToPremia> fetchRecFromPremiaDocUploadTblWithoutFVR = starFaxRevisedService.fetchRecFromPremiaDocUploadTblWithoutFVR(BATCH_SIZE);
				starFaxRevisedService.startProcessRevised(fetchRecFromPremiaDocUploadTblWithoutFVR);
			} catch(Exception e) {
				log.error("&&&&&&&&&&&&&& ERROR OCCURED WHILE DOC UPLOAD WITHOUT FVR -----> " + e.getMessage()  );
				return true;
			}
			return true;
		}
		
		
		public Boolean doPremiaPullForDocUploadFVR() {
			try {
				List<DocUploadToPremia> fetchRecFromPremiaDocUploadTblWithFVR = starFaxFVRRevisedService.fetchRecFromPremiaDocUploadTblWithFVR("20");
				starFaxFVRRevisedService.startProcessForFVRRevised(fetchRecFromPremiaDocUploadTblWithFVR);
			} catch(Exception e) {
				log.error("&&&&&&&&&&&&&& ERROR OCCURED WHILE DOC UPLOAD FOR FVR -----> " + e.getMessage()  );
				return true;
			}
			return true;
		}
		
		public Boolean doPremiaPullForDocUploadFVRError() {
			try {
				List<DocUploadToPremia> fetchRecFromPremiaDocUploadTblWithFVR = starFaxFVRRevisedService.fetchRecFromPremiaDocUploadTblWithFVRError("20");
				starFaxFVRRevisedService.startProcessForFVRRevised(fetchRecFromPremiaDocUploadTblWithFVR);
			} catch(Exception e) {
				log.error("&&&&&&&&&&&&&& ERROR OCCURED WHILE DOC UPLOAD FOR FVR (FVR ERROR BATCH) -----> " + e.getMessage()  );
				return true;
			}
			return true;
		}
		
		public Boolean doPremiaPullForDocNoClaim() {
			try {
				List<DocUploadToPremia> fetchRecFromPremiaDocUploadTblWithNoClaim= starFaxFVRRevisedService.fetchRecFromPremiaDocUploadTblWithNoClaim("20");
				starFaxRevisedService.startProcessRevised(fetchRecFromPremiaDocUploadTblWithNoClaim);
			} catch(Exception e) {
				log.error("&&&&&&&&&&&&&& ERROR OCCURED WHILE DOC UPLOAD FOR NO CLAIM -----> " + e.getMessage()  );
				return true;
			}
			return true;
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


		public void createTimer(String timerInfo) {
			if(timerService != null) {
				log.info("Creating new Timer: " + timerInfo);
			    TimerConfig timerConf = new TimerConfig();
			    timerConf.setInfo(timerInfo);
			    timerService.createIntervalTimer(1, 1000, timerConf); //just an example
			} else {
				log.info("******************TIMER SERVICE IS NULL****************");
			}
			 	
		}
		
		public void callPremiaWSForUniquePremium() {
			premiaPullService.processPremiaWSForUniquePremium();
		}	
//		
//		public void callPreauthReminderProcess(){
//			premiaPullService.reminderLetterGeneratedForPreauthApproved();
//		}
		
//		public void callPreauthReminderProcessForEnhancement(){
//			premiaPullService.reminderLetterGeneratedForEhancementApproved();;
//		}
//		
//		public void callReminderProcessForWithdraw(){
//			premiaPullService.reminderLetterGeneratedForWithdrawApproved();
//		}
		public void callAutoGenerateBulkReminderLetter(){
			
			generateBulkReminderService.searchNGenerateBulkReminderLetter(masterService);
			generateBulkReminderService.searchNGeneratePANCardReminderLetter();
			reimbursementService.submitCloseClaim();
			
		}
		
		public void callAutoGenerateBulkReminderLetterForPA(){
			
			generatePABulkRemidnerService.searchNGeneratePAReminderLetterBulk(masterService);
			reimbursementService.submitCloseClaim();
		}	
		
		public Boolean doPremiaPullForDocUploadWithoutFVRForDescending() {
			try {
				List<DocUploadToPremia> fetchRecFromPremiaDocUploadTblWithoutFVR = starFaxRevisedService.fetchRecFromPremiaDocUploadTblWithoutFVRDescending(BATCH_SIZE);
				starFaxRevisedService.startProcessRevised(fetchRecFromPremiaDocUploadTblWithoutFVR);
			} catch(Exception e) {
				log.error("&&&&&&&&&&&&&& ERROR OCCURED WHILE DOC UPLOAD WITHOUT FVR @@@@@ Descending.-----> " + e.getMessage()  );
				return true;
			}
			return true;
		}
		
		public void calculateICR(){
			if(!premiaPullService.isICRBatchProcessed()){
				DBCalculationService dbCalculationService = new DBCalculationService();
				dbCalculationService.callInsertPolicyForGMC();
				log.info("@@@@@@@@@ ICR BATCH POLIY DATA INSERTED SUCESSFULLY@@@@@@@@@@-----> ");
				premiaPullService.updateGrossPremiumAmtForPolicyNumber();
				log.info("@@@@@@@@@ ICR BATCH GROSS PREMIUM AMOUNT UPDATED SUCESSFULLY@@@@@@@@@@-----> ");
				//added for ICR batch to DB procedure call
				premiaPullService.insertICRbatchData();
//			dbCalculationService.calculateGmcClaimRatio();
				log.info("@@@@@@@@@ ICR BATCH CALCULATION ENDED SUCESSFULLY@@@@@@@@@@-----> ");
			}
		}
		
		public void doPremiaIntimationPullForNonNetwork(Boolean isError) {
			List<PremiaIntimationTable> processPremiaIntimationData = null; 
					
			if(isError){
				processPremiaIntimationData = premiaPullService.processPremiaIntimationForNonNetworkError("20");
			}
			else{
				processPremiaIntimationData = premiaPullService.processPremiaIntimationDataForNonNetwork("20");
			}
			if(processPremiaIntimationData != null && !processPremiaIntimationData.isEmpty()) {
				
				for (PremiaIntimationTable premiaIntimationTable : processPremiaIntimationData) {
					log.info("@@@@@@@@@ INTIMATION PULL STARTING TIME FOR NON NETWORK@@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
					try {
						if(!premiaPullService.isIntimationExist(premiaIntimationTable.getGiIntimationNo())) {
							if((null != premiaIntimationTable.getGiHospCode() && !premiaIntimationTable.getGiHospCode().isEmpty()) && premiaPullService.isPolicyAllowed(premiaIntimationTable.getGiPolNo())) {
								String policyServiceType = "OTHERS";
								Boolean isIntegratedPolicy = true;
								if(premiaIntimationTable.getProductCode() != null){
									Product productByProductCode = premiaPullService.getProductByProductCode(premiaIntimationTable.getProductCode());
									if(productByProductCode != null && productByProductCode.getProductService() != null){
										policyServiceType = productByProductCode.getProductService();
									}
								}
								PremPolicyDetails policyDetails = null;
								
								log.info("@@@@@@@@@PREMIA WEB SERVICE CALL STARTING TIME FOR NON NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								
								if(policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo(), premiaIntimationTable.getGiInsuredName());
									isIntegratedPolicy =premiaPullService. populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getGiInsuredName(),false);
									
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGpaPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo(), premiaIntimationTable.getGiInsuredName());
									policyDetails.setRiskSysId(premiaIntimationTable.getGiInsuredName());
									isIntegratedPolicy = premiaPullService.populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getGiInsuredName(),false);
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.JET_PRIVILLAGE_POL_SERVICE)){
									policyDetails = premiaPullService.fetchJetPrivillagePolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getGiInsuredName(),false);
								}else{
									policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getGiInsuredName(),false);
								}
								log.info("@@@@@@@@@ PREMIA WEB SERVICE CALL ENDING TIME FOR NON NETWORK@@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								if(isIntegratedPolicy) {
									
									log.info("@@@@@@@@@ INSERT INTIMATION STARTING TIME FOR NON NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
//									Intimation intimation = premiaPullService.insertIntimationDataRevised(premiaIntimationTable);
									Intimation intimation = premiaPullService.insertIntimationDataRevised(premiaIntimationTable,policyDetails.getLob());
									
									log.info("@@@@@@@@@ INSERT INTIMATION ENDING TIME FOR NON NETWORK@@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									
									if(null != intimation ){
										log.info("***** SUCCESSFULLY INITMATION CREATED FOR NON NETWORK ********************-----> " + intimation.getIntimationId());
									}
									else{
										log.info("***************** Failed to Create INITMATION for Premia Intimation ********************-----> "+ premiaIntimationTable.getGiIntimationNo());
										premiaPullService.updatePremiaIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
									}
									if(null != intimation && null != intimation.getHospital() && null != intimation.getCpuCode()) {
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME FOR NON NETWORK@@@@@@@@@@-----> "+ System.currentTimeMillis());
										premiaPullService.processSavedIntimationRevised(intimation, premiaIntimationTable);
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME FOR NON NETWORK@@@@@@@@@@-----> "+ System.currentTimeMillis());
									} 
								} else {
									log.info("#^#^^#^#^#THIS PRODUCT IS NOT INTREGRATED IN GALAXY----S-- OR PRODUCT CODE IS WRONG PLEASE CHECK THE POLCIY NUMBER OR POLICY NOT SAVED WHILE SAVING THE POLICY DATA---> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode() + " Policy No---> " + premiaIntimationTable.getGiPolNo() );
								}
							} else {
								log.info("#*#*#*#**#*#*#*#*#*#*#* THIS POLICY IS NOT ALLOWED IN GALAXY----BASED ON INTIMATION IMPORT VALIDATION VALUES *#*#*#*#*#*#*#*#*#-----> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode());
								premiaPullService.updatePremiaIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
							}
						} else {
							log.info("%*&%*%&%*%&%*%&%*(%*% THIS INTIMATION IS ALREADY EXISTING PLEASE CHECK #*@&(#&@(@*@(*@*(@ --->" + premiaIntimationTable.getGiIntimationNo());
							premiaPullService.updatePremiaIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					log.info("@@@@@@@@@ INTIMATION PULL ENDING TIME FOR NON NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
				}
				
				
			}
		}
		
		public void doPremiaIntimationPullForGmc() {
			List<PremiaIntimationTable> processPremiaIntimationData = premiaPullService.processPremiaIntimationDataForGmc("20");
			if(processPremiaIntimationData != null && !processPremiaIntimationData.isEmpty()) {
				
				for (PremiaIntimationTable premiaIntimationTable : processPremiaIntimationData) {
					log.info("@@@@@@@@@ INTIMATION PULL STARTING TIME FOR GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
					try {
						if(!premiaPullService.isIntimationExist(premiaIntimationTable.getGiIntimationNo())) {
							if((null != premiaIntimationTable.getGiHospCode() && !premiaIntimationTable.getGiHospCode().isEmpty()) && premiaPullService.isPolicyAllowed(premiaIntimationTable.getGiPolNo())) {
								String policyServiceType = "OTHERS";
								Boolean isIntegratedPolicy = true;
								if(premiaIntimationTable.getProductCode() != null){
									Product productByProductCode = premiaPullService.getProductByProductCode(premiaIntimationTable.getProductCode());
									if(productByProductCode != null && productByProductCode.getProductService() != null){
										policyServiceType = productByProductCode.getProductService();
									}
								}
								PremPolicyDetails policyDetails = null;
								
								log.info("@@@@@@@@@PREMIA WEB SERVICE CALL STARTING TIME FOR GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								
								if(policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
									
									log.info("@@@@@@@@@ POLICY WEB SERVICE CALL STARTING TIME FOR GMC @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									
									policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo(), premiaIntimationTable.getGiInsuredName());
									isIntegratedPolicy =premiaPullService. populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getGiInsuredName(),false);
									
									log.info("@@@@@@@@@POLICY WEB SERVICE CALL ENDING TIME FOR GMC @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGpaPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo(), premiaIntimationTable.getGiInsuredName());
									policyDetails.setRiskSysId(premiaIntimationTable.getGiInsuredName());
									isIntegratedPolicy = premiaPullService.populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getGiInsuredName(),false);
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.JET_PRIVILLAGE_POL_SERVICE)){
									policyDetails = premiaPullService.fetchJetPrivillagePolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getGiInsuredName(),false);
								}else{
									policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getGiInsuredName(),false);
								}
								log.info("@@@@@@@@@ PREMIA WEB SERVICE CALL ENDING TIME FOR GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								if(isIntegratedPolicy) {
									
									log.info("@@@@@@@@@ INSERT INTIMATION STARTING TIME FOR GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
//									Intimation intimation = premiaPullService.insertIntimationDataRevised(premiaIntimationTable);
									Intimation intimation = premiaPullService.insertIntimationDataRevised(premiaIntimationTable,policyDetails.getLob());
									
									log.info("@@@@@@@@@ INSERT INTIMATION ENDING TIME FOR GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									
									if(null != intimation ){
										log.info("***** SUCCESSFULLY INITMATION CREATED FOR GMC NETWORK ********************-----> " + intimation.getIntimationId());
									}
									else{
										log.info("***************** Failed to Create INITMATION for Premia Intimation ********************-----> "+ premiaIntimationTable.getGiIntimationNo());
										premiaPullService.updatePremiaIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
									}
									if(null != intimation && null != intimation.getHospital() && null != intimation.getCpuCode()) {
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME FOR GMC @@@@@@@@@@-----> "+ System.currentTimeMillis());
										premiaPullService.processSavedIntimationRevised(intimation, premiaIntimationTable);
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME FOR GMC @@@@@@@@@@-----> "+ System.currentTimeMillis());
									} 
								} else {
									log.info("#^#^^#^#^#THIS PRODUCT IS NOT INTREGRATED IN GALAXY----S-- OR PRODUCT CODE IS WRONG PLEASE CHECK THE POLCIY NUMBER OR POLICY NOT SAVED WHILE SAVING THE POLICY DATA  FOR GMC ---> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode() + " Policy No---> " + premiaIntimationTable.getGiPolNo() );
								}
							} else {
								log.info("#*#*#*#**#*#*#*#*#*#*#* THIS POLICY IS NOT ALLOWED IN GALAXY----BASED ON INTIMATION IMPORT VALIDATION VALUES  FOR GMC *#*#*#*#*#*#*#*#*#-----> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode());
								premiaPullService.updatePremiaIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
							}
						} else {
							log.info("%*&%*%&%*%&%*%&%*(%*% THIS INTIMATION IS ALREADY EXISTING PLEASE CHECK  FOR GMC  #*@&(#&@(@*@(*@*(@ --->" + premiaIntimationTable.getGiIntimationNo());
							premiaPullService.updatePremiaIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					log.info("@@@@@@@@@ INTIMATION PULL ENDING TIME FOR GMC @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
				}
				
				
			}
		}
		
		public void updateProvisionToPremiaForStarFax(){
			List<StarfaxProvisionHistory> provisionHistory = starFaxRevisedService.fetchProvisonUpdateDetailsForStarfax(BATCH_SIZE);
			for (StarfaxProvisionHistory starfaxProvisionHistory : provisionHistory) {
				log.info("STARFAX PROVISION UPDATE : INTIMATION_NUMBER ---->"+starfaxProvisionHistory.getIntimationNumber() +" ------> PROVISION_AMT :" +starfaxProvisionHistory.getCurrentProvisonAmt());
				log.info("STARFAX PROVISION UPDATE : STARTING TIME ---->"+System.currentTimeMillis());
				starFaxRevisedService.updateProvisionToPremiaForStarfax(starfaxProvisionHistory);
				log.info("STARFAX PROVISION UPDATE : ENDING TIME ---->"+System.currentTimeMillis());
			}
			
		}

		
		public void doPremiaEndorsementDetails() {
			List<PremiaEndorsementTable> processPremiaIntimationData = premiaPullService.fetchRecFromPremiaEndorsementTableDTO("20");
			if(processPremiaIntimationData != null && !processPremiaIntimationData.isEmpty()) {

				log.info("@@@@@@@@@PREMIA WEB SERVICE CALL FOR ENDORSEMENT BATCH @@@@@@@@@@-----> "+ System.currentTimeMillis());
				for (PremiaEndorsementTable premiaIntimationTable : processPremiaIntimationData) {
					try {
							if((null != premiaIntimationTable.getProductCode() || !("").equalsIgnoreCase(premiaIntimationTable.getProductCode())) && premiaPullService.isPolicyAllowed(premiaIntimationTable.getProductCode())) {
								String policyServiceType = "OTHERS";
								Boolean isIntegratedPolicy = true;
								if(premiaIntimationTable.getProductCode() != null){
									Product productByProductCode = premiaPullService.getProductByProductCode(premiaIntimationTable.getProductCode());
									if(productByProductCode != null && productByProductCode.getProductService() != null){
										policyServiceType = productByProductCode.getProductService();
									}
								}
								PremPolicyDetails policyDetails = null;
								
								log.info("@@@@@@@@@PREMIA WEB SERVICE CALL STARTING TIME FOR ENDORSEMENT BATCH @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getPolicyNumber()+"-----> " +premiaIntimationTable.getRiskId()+"-----> "+ System.currentTimeMillis());
								
								
								if(policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(premiaIntimationTable
											.getPolicyNumber(), premiaIntimationTable.getRiskId());
									isIntegratedPolicy =premiaPullService. populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getRiskId(),true);
									
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGpaPolicyDetailsFromPremia(premiaIntimationTable
											.getPolicyNumber(), premiaIntimationTable.getRiskId());
									policyDetails.setRiskSysId(premiaIntimationTable.getRiskId());
									isIntegratedPolicy = premiaPullService.populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getRiskId(),true);
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.JET_PRIVILLAGE_POL_SERVICE)){
									policyDetails = premiaPullService.fetchJetPrivillagePolicyDetailsFromPremia(premiaIntimationTable
											.getPolicyNumber());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getRiskId(),true);
								}else{
									policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(premiaIntimationTable
											.getPolicyNumber());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getRiskId(),true);
								}
								
								if(isIntegratedPolicy) {
									premiaPullService.updatePremiaEndorsementTable(premiaIntimationTable, "Y");
								} else {
									premiaPullService.updatePremiaEndorsementTable(premiaIntimationTable, "N");
								}
							} else {
								premiaPullService.updatePremiaEndorsementTable(premiaIntimationTable, "N");
							}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
				}
				
				
			}
		}
		
		public void processSTPProcess(){
			intimationService.getDBTaskForSTPProcess(SHAConstants.STP_CURRENT_QUEUE);
		}
		
		public void doPremiaEndorsementDetailsForGMC(Boolean isDesc){

			List<PremiaEndorsementTable> processPremiaIntimationData = null;
			if(isDesc && (premiaPullService.fetchCountPremiaEndorsementTableForGMC() > 10000L)){
				log.info("@@@@@@@@@PREMIA WEB SERVICE CALL FOR ENDORSEMENT BATCH DESC@@@@@@@@@@-----> "+ System.currentTimeMillis());
				processPremiaIntimationData = premiaPullService.fetchRecFromPremiaEndorsementTableForGMCDesc("20");
			}
			if(!isDesc){
				log.info("@@@@@@@@@PREMIA WEB SERVICE CALL FOR ENDORSEMENT BATCH ASC@@@@@@@@@@-----> "+ System.currentTimeMillis());
				processPremiaIntimationData = premiaPullService.fetchRecFromPremiaEndorsementTableForGMC("20");
			}
			if(processPremiaIntimationData != null && !processPremiaIntimationData.isEmpty()) {

				log.info("@@@@@@@@@PREMIA WEB SERVICE CALL FOR ENDORSEMENT BATCH @@@@@@@@@@-----> "+ System.currentTimeMillis());
				for (PremiaEndorsementTable premiaIntimationTable : processPremiaIntimationData) {
					try {
							if((null != premiaIntimationTable.getProductCode() || !("").equalsIgnoreCase(premiaIntimationTable.getProductCode())) && premiaPullService.isPolicyAllowed(premiaIntimationTable.getProductCode())) {
								String policyServiceType = "OTHERS";
								Boolean isIntegratedPolicy = true;
								if(premiaIntimationTable.getProductCode() != null){
									Product productByProductCode = premiaPullService.getProductByProductCode(premiaIntimationTable.getProductCode());
									if(productByProductCode != null && productByProductCode.getProductService() != null){
										policyServiceType = productByProductCode.getProductService();
									}
								}
								PremPolicyDetails policyDetails = null;
								
								log.info("@@@@@@@@@PREMIA WEB SERVICE CALL STARTING TIME FOR ENDORSEMENT BATCH @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getPolicyNumber()+"-----> " +premiaIntimationTable.getRiskId()+"-----> "+ System.currentTimeMillis());
								
								
								if(policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(premiaIntimationTable
											.getPolicyNumber(), premiaIntimationTable.getRiskId());
									isIntegratedPolicy =premiaPullService. populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getRiskId(),true);
									
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGpaPolicyDetailsFromPremia(premiaIntimationTable
											.getPolicyNumber(), premiaIntimationTable.getRiskId());
									policyDetails.setRiskSysId(premiaIntimationTable.getRiskId());
									isIntegratedPolicy = premiaPullService.populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getRiskId(),true);
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.JET_PRIVILLAGE_POL_SERVICE)){
									policyDetails = premiaPullService.fetchJetPrivillagePolicyDetailsFromPremia(premiaIntimationTable
											.getPolicyNumber());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getRiskId(),true);
								}else{
									policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(premiaIntimationTable
											.getPolicyNumber());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getRiskId(),true);
								}
								
								if(isIntegratedPolicy) {
									premiaPullService.updatePremiaEndorsementTable(premiaIntimationTable, "Y");
								} else {
									premiaPullService.updatePremiaEndorsementTable(premiaIntimationTable, "N");
								}
							} else {
								premiaPullService.updatePremiaEndorsementTable(premiaIntimationTable, "N");
							}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
				}
				
				
			}
		
		}
		
		public void doPremiaEndorsementDetailsForGPA(){

			List<PremiaEndorsementTable> processPremiaIntimationData = premiaPullService.fetchRecFromPremiaEndorsementTableForGPA("20");
			if(processPremiaIntimationData != null && !processPremiaIntimationData.isEmpty()) {

				log.info("@@@@@@@@@PREMIA WEB SERVICE CALL FOR ENDORSEMENT BATCH @@@@@@@@@@-----> "+ System.currentTimeMillis());
				for (PremiaEndorsementTable premiaIntimationTable : processPremiaIntimationData) {
					try {
							if((null != premiaIntimationTable.getProductCode() || !("").equalsIgnoreCase(premiaIntimationTable.getProductCode())) && premiaPullService.isPolicyAllowed(premiaIntimationTable.getProductCode())) {
								String policyServiceType = "OTHERS";
								Boolean isIntegratedPolicy = true;
								if(premiaIntimationTable.getProductCode() != null){
									Product productByProductCode = premiaPullService.getProductByProductCode(premiaIntimationTable.getProductCode());
									if(productByProductCode != null && productByProductCode.getProductService() != null){
										policyServiceType = productByProductCode.getProductService();
									}
								}
								PremPolicyDetails policyDetails = null;
								
								log.info("@@@@@@@@@PREMIA WEB SERVICE CALL STARTING TIME FOR ENDORSEMENT BATCH @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getPolicyNumber()+"-----> " +premiaIntimationTable.getRiskId()+"-----> "+ System.currentTimeMillis());
								
								
								if(policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(premiaIntimationTable
											.getPolicyNumber(), premiaIntimationTable.getRiskId());
									isIntegratedPolicy =premiaPullService. populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getRiskId(),true);
									
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGpaPolicyDetailsFromPremia(premiaIntimationTable
											.getPolicyNumber(), premiaIntimationTable.getRiskId());
									policyDetails.setRiskSysId(premiaIntimationTable.getRiskId());
									isIntegratedPolicy = premiaPullService.populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getRiskId(),true);
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.JET_PRIVILLAGE_POL_SERVICE)){
									policyDetails = premiaPullService.fetchJetPrivillagePolicyDetailsFromPremia(premiaIntimationTable
											.getPolicyNumber());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getRiskId(),true);
								}else{
									policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(premiaIntimationTable
											.getPolicyNumber());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getRiskId(),true);
								}
								
								if(isIntegratedPolicy) {
									premiaPullService.updatePremiaEndorsementTable(premiaIntimationTable, "Y");
								} else {
									premiaPullService.updatePremiaEndorsementTable(premiaIntimationTable, "N");
								}
							} else {
								premiaPullService.updatePremiaEndorsementTable(premiaIntimationTable, "N");
							}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
				}
				
				
			}
		
			
		}
		
		public void doBaNCSIntimationPull() {
			List<BaNCSIntimationTable> processBaNCSIntimationData = bancsPullService.processBaNCSIntimationData("20");
			if(processBaNCSIntimationData != null && !processBaNCSIntimationData.isEmpty()) {
				
				for (BaNCSIntimationTable baNCSIntimationTable : processBaNCSIntimationData) {
					log.info("@@@@@@@@@ INTIMATION PULL STARTING TIME FOR  NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +baNCSIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
					try {
						if(!premiaPullService.isIntimationExist(baNCSIntimationTable.getGiIntimationNo())) {
							if((null != baNCSIntimationTable.getGiHospCode() || !("").equalsIgnoreCase(baNCSIntimationTable.getGiHospCode())) && premiaPullService.isPolicyAllowed(baNCSIntimationTable.getGiPolNo())) {
								String policyServiceType = "OTHERS";
								Boolean isIntegratedPolicy = true;
								if(baNCSIntimationTable.getProductCode() != null){
									Product productByProductCode = premiaPullService.getProductBySource(baNCSIntimationTable.getProductCode());
									if(productByProductCode != null && productByProductCode.getProductService() != null){
										policyServiceType = productByProductCode.getProductService();
									}
								}
								PremPolicyDetails policyDetails = null;
								
								log.info("@@@@@@@@@BANCS WEB SERVICE CALL STARTING TIME FOR  NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +baNCSIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								
								if(policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(baNCSIntimationTable
											.getGiPolNo(), baNCSIntimationTable.getGiInsuredName());
									isIntegratedPolicy =premiaPullService. populateGMCandGPAPolicy(policyDetails, baNCSIntimationTable.getGiInsuredName(),false);
									
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGpaPolicyDetailsFromPremia(baNCSIntimationTable
											.getGiPolNo(), baNCSIntimationTable.getGiInsuredName());
									policyDetails.setRiskSysId(baNCSIntimationTable.getGiInsuredName());
									isIntegratedPolicy = premiaPullService.populateGMCandGPAPolicy(policyDetails, baNCSIntimationTable.getGiInsuredName(),false);
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.JET_PRIVILLAGE_POL_SERVICE)){
									policyDetails = premiaPullService.fetchJetPrivillagePolicyDetailsFromPremia(baNCSIntimationTable
											.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,baNCSIntimationTable.getGiInsuredName(),false);
								}else{
									policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(baNCSIntimationTable
											.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,baNCSIntimationTable.getGiInsuredName(),false);
								}
								log.info("@@@@@@@@@ BANCS WEB SERVICE CALL ENDING TIME FOR  NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +baNCSIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								if(isIntegratedPolicy) {
									
									log.info("@@@@@@@@@ INSERT INTIMATION STARTING TIME FOR  NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +baNCSIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
//									Intimation intimation = premiaPullService.insertIntimationDataRevised(premiaIntimationTable);
									Intimation intimation = premiaPullService.insertIntimationDataRevisedBaNCS(baNCSIntimationTable,policyDetails.getLob());
									
									log.info("@@@@@@@@@ INSERT INTIMATION ENDING TIME FOR  NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +baNCSIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									
									if(null != intimation ){
										log.info("***** SUCCESSFULLY INITMATION CREATED FOR  NON GMC NETWORK ********************-----> " + intimation.getIntimationId());
									}
									else {
										log.info("***************** Failed to Create INITMATION for Premia Intimation ********************-----> "+ baNCSIntimationTable.getGiIntimationNo());
										premiaPullService.updateBaNCSIntimationTable(baNCSIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
									}
									if(null != intimation && null != intimation.getHospital() && null != intimation.getCpuCode()) {
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME  NON GMC  @@@@@@@@@@-----> "+ System.currentTimeMillis());
										premiaPullService.processSavedIntimationRevisedBaNCS(intimation, baNCSIntimationTable);
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME  NON GMC @@@@@@@@@@-----> "+ System.currentTimeMillis());
									} 
								} else {
									log.info("#^#^^#^#^#THIS PRODUCT IS NOT INTREGRATED IN GALAXY----S-- OR PRODUCT CODE IS WRONG PLEASE CHECK THE POLCIY NUMBER OR POLICY NOT SAVED WHILE SAVING THE POLICY DATA---> " + baNCSIntimationTable.getGiIntimationNo() + "  HOSP CODE" + baNCSIntimationTable.getGiHospCode() + " Policy No---> " + baNCSIntimationTable.getGiPolNo() );
								}
							} else {
								log.info("#*#*#*#**#*#*#*#*#*#*#* THIS POLICY IS NOT ALLOWED IN GALAXY----BASED ON INTIMATION IMPORT VALIDATION VALUES *#*#*#*#*#*#*#*#*#-----> " + baNCSIntimationTable.getGiIntimationNo() + "  HOSP CODE" + baNCSIntimationTable.getGiHospCode());
							}
						} else {
							log.info("%*&%*%&%*%&%*%&%*(%*% THIS INTIMATION IS ALREADY EXISTING PLEASE CHECK #*@&(#&@(@*@(*@*(@ --->" + baNCSIntimationTable.getGiIntimationNo());
							premiaPullService.updateBaNCSIntimationTable(baNCSIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					log.info("@@@@@@@@@ INTIMATION PULL ENDING TIME @@@@@@@@@@-----> "+"-----> " +baNCSIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
				}
				
				
			}
		}
		
		public void doBaNCSIntimationPullForNonNetwork() {
			List<BaNCSIntimationTable> processBaNCSIntimationData = bancsPullService.processBaNCSIntimationDataForNonNetwork("20");
			if(processBaNCSIntimationData != null && !processBaNCSIntimationData.isEmpty()) {
				
				for (BaNCSIntimationTable bancsIntimationTable : processBaNCSIntimationData) {
					log.info("@@@@@@@@@ INTIMATION PULL STARTING TIME FOR NON NETWORK@@@@@@@@@@-----> "+"-----> " +bancsIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
					try {
						if(!premiaPullService.isIntimationExist(bancsIntimationTable.getGiIntimationNo())) {
							if((null != bancsIntimationTable.getGiHospCode() || !("").equalsIgnoreCase(bancsIntimationTable.getGiHospCode())) && premiaPullService.isPolicyAllowed(bancsIntimationTable.getGiPolNo())) {
								String policyServiceType = "OTHERS";
								Boolean isIntegratedPolicy = true;
								if(bancsIntimationTable.getProductCode() != null){
									Product productByProductCode = premiaPullService.getProductBySource(bancsIntimationTable.getProductCode());
									if(productByProductCode != null && productByProductCode.getProductService() != null){
										policyServiceType = productByProductCode.getProductService();
									}
								}
								PremPolicyDetails policyDetails = null;
								
								log.info("@@@@@@@@@BANCS WEB SERVICE CALL STARTING TIME FOR NON NETWORK @@@@@@@@@@-----> "+"-----> " +bancsIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								
								if(policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(bancsIntimationTable
											.getGiPolNo(), bancsIntimationTable.getGiInsuredName());
									isIntegratedPolicy =premiaPullService. populateGMCandGPAPolicy(policyDetails, bancsIntimationTable.getGiInsuredName(),false);
									
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGpaPolicyDetailsFromPremia(bancsIntimationTable
											.getGiPolNo(), bancsIntimationTable.getGiInsuredName());
									policyDetails.setRiskSysId(bancsIntimationTable.getGiInsuredName());
									isIntegratedPolicy = premiaPullService.populateGMCandGPAPolicy(policyDetails, bancsIntimationTable.getGiInsuredName(),false);
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.JET_PRIVILLAGE_POL_SERVICE)){
									policyDetails = premiaPullService.fetchJetPrivillagePolicyDetailsFromPremia(bancsIntimationTable
											.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,bancsIntimationTable.getGiInsuredName(),false);
								}else{
									policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(bancsIntimationTable
											.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,bancsIntimationTable.getGiInsuredName(),false);
								}
								log.info("@@@@@@@@@ BANCS WEB SERVICE CALL ENDING TIME FOR NON NETWORK@@@@@@@@@@-----> "+"-----> " +bancsIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								if(isIntegratedPolicy) {
									
									log.info("@@@@@@@@@ INSERT INTIMATION STARTING TIME FOR NON NETWORK @@@@@@@@@@-----> "+"-----> " +bancsIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
//									Intimation intimation = premiaPullService.insertIntimationDataRevised(premiaIntimationTable);
									Intimation intimation = premiaPullService.insertIntimationDataRevisedBaNCS(bancsIntimationTable,policyDetails.getLob());
									
									log.info("@@@@@@@@@ INSERT INTIMATION ENDING TIME FOR NON NETWORK@@@@@@@@@@-----> "+"-----> " +bancsIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									
									if(null != intimation ){
										log.info("***** SUCCESSFULLY INITMATION CREATED FOR NON NETWORK ********************-----> " + intimation.getIntimationId());
									}
									else{
										log.info("***************** Failed to Create INITMATION for BANCS Intimation ********************-----> "+ bancsIntimationTable.getGiIntimationNo());
									}
									if(null != intimation && null != intimation.getHospital() && null != intimation.getCpuCode()) {
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME FOR NON NETWORK@@@@@@@@@@-----> "+ System.currentTimeMillis());
										premiaPullService.processSavedIntimationRevisedBaNCS(intimation, bancsIntimationTable);
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME FOR NON NETWORK@@@@@@@@@@-----> "+ System.currentTimeMillis());
									} 
								} else {
									log.info("#^#^^#^#^#THIS PRODUCT IS NOT INTREGRATED IN GALAXY----S-- OR PRODUCT CODE IS WRONG PLEASE CHECK THE POLCIY NUMBER OR POLICY NOT SAVED WHILE SAVING THE POLICY DATA---> " + bancsIntimationTable.getGiIntimationNo() + "  HOSP CODE" + bancsIntimationTable.getGiHospCode() + " Policy No---> " + bancsIntimationTable.getGiPolNo() );
								}
							} else {
								log.info("#*#*#*#**#*#*#*#*#*#*#* THIS POLICY IS NOT ALLOWED IN GALAXY----BASED ON INTIMATION IMPORT VALIDATION VALUES *#*#*#*#*#*#*#*#*#-----> " + bancsIntimationTable.getGiIntimationNo() + "  HOSP CODE" + bancsIntimationTable.getGiHospCode());
							}
						} else {
							log.info("%*&%*%&%*%&%*%&%*(%*% THIS INTIMATION IS ALREADY EXISTING PLEASE CHECK #*@&(#&@(@*@(*@*(@ --->" + bancsIntimationTable.getGiIntimationNo());
							premiaPullService.updateBaNCSIntimationTable(bancsIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					log.info("@@@@@@@@@ INTIMATION PULL ENDING TIME FOR NON NETWORK @@@@@@@@@@-----> "+"-----> " +bancsIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
				}
				
				
			}
		}
		
		//Added For Accident Trauma care Group product		
				public void doPremiaIntimationPullForPA() {
					List<PremiaIntimationTable> processPremiaIntimationData = premiaPullService.processPremiaIntimationDataForPA("20");
					if(processPremiaIntimationData != null && !processPremiaIntimationData.isEmpty()) {				
						for (PremiaIntimationTable premiaIntimationTable : processPremiaIntimationData) {
							log.info("@@@@@@@@@ INTIMATION PULL STARTING TIME FOR ACCIDENT PA @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
							try {
								if(!premiaPullService.isIntimationExist(premiaIntimationTable.getGiIntimationNo())) {
									if((null != premiaIntimationTable.getGiHospCode() && !premiaIntimationTable.getGiHospCode().isEmpty()) && premiaPullService.isPolicyAllowed(premiaIntimationTable.getGiPolNo())) {
										String policyServiceType = "OTHERS";
										Boolean isIntegratedPolicy = true;
										if(premiaIntimationTable.getProductCode() != null){
											Product productByProductCode = premiaPullService.getProductByProductCode(premiaIntimationTable.getProductCode());
											if(productByProductCode != null && productByProductCode.getProductService() != null){
												policyServiceType = productByProductCode.getProductService();
											}
										}
										PremPolicyDetails policyDetails = null;
										
										log.info("@@@@@@@@@PREMIA WEB SERVICE CALL STARTING TIME FOR ACCIDENT PA @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
										
										
										if(policyServiceType.equalsIgnoreCase(SHAConstants.OTHER_POL_SERIVICE)){
											policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(premiaIntimationTable.getGiPolNo());
											isIntegratedPolicy = premiaPullService.populatePolicyFromPAPolicy(policyDetails,premiaIntimationTable.getGiInsuredName(),false);
										}
										log.info("@@@@@@@@@ PREMIA WEB SERVICE CALL ENDING TIME FOR ACCIDENT PA @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
										
										if(isIntegratedPolicy) {
											
											log.info("@@@@@@@@@ INSERT INTIMATION STARTING TIME FOR ACCIDENT PA @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
											Intimation intimation = premiaPullService.insertIntimationDataRevised(premiaIntimationTable,policyDetails.getLob());
											log.info("@@@@@@@@@ INSERT INTIMATION ENDING TIME FOR ACCIDENT PA  @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
											
											if(null != intimation ){
												log.info("***** SUCCESSFULLY INITMATION CREATED FOR ACCIDENT PA  ********************-----> " + intimation.getIntimationId());
											}
											else {
												log.info("***************** Failed to Create INITMATION for Premia Intimation ********************-----> "+ premiaIntimationTable.getGiIntimationNo());
												premiaPullService.updatePremiaIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
											}
											if(null != intimation && null != intimation.getHospital() && null != intimation.getCpuCode()) {
												log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME ACCIDENT PA  @@@@@@@@@@-----> "+ System.currentTimeMillis());
												premiaPullService.processSavedIntimationRevised(intimation, premiaIntimationTable);
												log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME ACCIDENT PA  @@@@@@@@@@-----> "+ System.currentTimeMillis());
											} 
										} else {
											log.info("#^#^^#^#^#THIS PRODUCT IS NOT INTREGRATED IN GALAXY----S-- OR PRODUCT CODE IS WRONG PLEASE CHECK THE POLCIY NUMBER OR POLICY NOT SAVED WHILE SAVING THE POLICY DATA---> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode() + " Policy No---> " + premiaIntimationTable.getGiPolNo() );
										}
									} else {
										log.info("#*#*#*#**#*#*#*#*#*#*#* THIS POLICY IS NOT ALLOWED IN GALAXY----BASED ON INTIMATION IMPORT VALIDATION VALUES *#*#*#*#*#*#*#*#*#-----> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode());
										premiaPullService.updatePremiaIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
									}
								} else {
									log.info("%*&%*%&%*%&%*%&%*(%*% THIS INTIMATION IS ALREADY EXISTING PLEASE CHECK #*@&(#&@(@*@(*@*(@ --->" + premiaIntimationTable.getGiIntimationNo());
									premiaPullService.updatePremiaIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
								}
							} catch(Exception e) {
								e.printStackTrace();
							}
							
							log.info("@@@@@@@@@ INTIMATION PULL ENDING TIME @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
						}
						
						
					}
				}

		public void doPremiaIntimationPullForOP() {
			List<OPPremiaIntimationTable> processOPPremiaIntimationData = premiaPullService.processOPPremiaIntimationData("20");
			if(processOPPremiaIntimationData != null && !processOPPremiaIntimationData.isEmpty()) {
				
				for (OPPremiaIntimationTable oppremiaIntimationTable : processOPPremiaIntimationData) {
					log.info("@@@@@@@@@ INTIMATION PULL STARTING TIME FOR OP INTIMATION @@@@@@@@@@-----> "+"-----> " +oppremiaIntimationTable.getIntimationNumber()+"-----> "+ System.currentTimeMillis());
					try {
						if(!premiaPullService.isOPIntimationExist(oppremiaIntimationTable.getIntimationNumber())) {
							if(premiaPullService.isPolicyAllowed(oppremiaIntimationTable.getPolicyNumber())) {
								String policyServiceType = "OTHERS";
								Boolean isIntegratedPolicy = true;
								if(oppremiaIntimationTable.getProdCode() != null){
									Product productByProductCode = premiaPullService.getProductByProductCode(oppremiaIntimationTable.getProdCode());
									if(productByProductCode != null && productByProductCode.getProductService() != null){
										policyServiceType = productByProductCode.getProductService();
									}
								}
								PremPolicyDetails policyDetails = null;
								
								log.info("@@@@@@@@@PREMIA WEB SERVICE CALL STARTING TIME FOR  OP INTIMATION @@@@@@@@@@-----> "+"-----> " +oppremiaIntimationTable.getIntimationNumber()+"-----> "+ System.currentTimeMillis());
								
									policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(oppremiaIntimationTable
											.getPolicyNumber());
									isIntegratedPolicy = premiaPullService.populateOPPolicyFromTmpPolicy(policyDetails);
//								}
								log.info("@@@@@@@@@ PREMIA WEB SERVICE CALL ENDING TIME FOR  OP INTIMATION @@@@@@@@@@-----> "+"-----> " +oppremiaIntimationTable.getIntimationNumber()+"-----> "+ System.currentTimeMillis());
								
								if(isIntegratedPolicy) {
									
									log.info("@@@@@@@@@ INSERT INTIMATION STARTING TIME FOR OP INTIMATION @@@@@@@@@@-----> "+"-----> " +oppremiaIntimationTable.getIntimationNumber()+"-----> "+ System.currentTimeMillis());
//									Intimation intimation = premiaPullService.insertIntimationDataRevised(premiaIntimationTable);
									OPIntimation opintimation = premiaPullService.insertOPIntimationData(oppremiaIntimationTable,policyDetails.getLob());
									
									log.info("@@@@@@@@@ INSERT INTIMATION ENDING TIME FOR  OP INTIMATION @@@@@@@@@@-----> "+"-----> " +oppremiaIntimationTable.getIntimationNumber()+"-----> "+ System.currentTimeMillis());
									
									if(null != opintimation ){
										log.info("***** SUCCESSFULLY INITMATION CREATED FOR  OP INTIMATION ********************-----> " + opintimation.getIntimationId());
									}
									else {
										log.info("***************** Failed to Create INITMATION for Premia OPIntimation ********************-----> "+ oppremiaIntimationTable.getIntimationNumber());
										premiaPullService.updateOPPremiaIntimationTable(oppremiaIntimationTable,SHAConstants.FAILURE_FLAG, SHAConstants.N_FLAG);
									}
									if(null != opintimation) {
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME OP INTIMATION @@@@@@@@@@-----> "+ System.currentTimeMillis());
										premiaPullService.processSavedOPIntimationRevised(opintimation, oppremiaIntimationTable);
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME  OP INTIMATION @@@@@@@@@@-----> "+ System.currentTimeMillis());
									} 
								} else {
//									log.info("#^#^^#^#^#THIS PRODUCT IS NOT INTREGRATED IN GALAXY----S-- OR PRODUCT CODE IS WRONG PLEASE CHECK THE POLCIY NUMBER OR POLICY NOT SAVED WHILE SAVING THE POLICY DATA---> " + oppremiaIntimationTable.getGiIntimationNo() + " Policy No---> " + premiaIntimationTable.getGiPolNo() );
								}
							} else {
								log.info("#*#*#*#**#*#*#*#*#*#*#* THIS POLICY IS NOT ALLOWED IN GALAXY----BASED ON INTIMATION IMPORT VALIDATION VALUES *#*#*#*#*#*#*#*#*#-----> " + oppremiaIntimationTable.getIntimationNumber());
							}
						} else {
							log.info("%*&%*%&%*%&%*%&%*(%*% THIS INTIMATION IS ALREADY EXISTING PLEASE CHECK #*@&(#&@(@*@(*@*(@ --->" + oppremiaIntimationTable.getIntimationNumber());
							premiaPullService.updateOPPremiaIntimationTable(oppremiaIntimationTable,SHAConstants.SUCCESS_FLAG, SHAConstants.YES_FLAG);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					log.info("@@@@@@@@@ INTIMATION PULL ENDING TIME @@@@@@@@@@-----> "+"-----> " +oppremiaIntimationTable.getIntimationNumber()+"-----> "+ System.currentTimeMillis());
				}
				
				
			}
		}
		
		public void ackDocumentCreationBatch(){

			ackDocReceivedService.acknowledgementDocumentCreationBatch();
		}

		public void doCreateNewAckDocForTopupIntimation(){
			try {
				List<DocAcknowledgement>  docAcknowledgementList = premiaPullService.loadAcknowledgementDocForTopup();
				String templateName = "AckReceipt";
				ReportDto reportDto = null;
				DocumentGenerator docGen = null;
				if( docAcknowledgementList != null && !docAcknowledgementList.isEmpty() ){
					log.info("@@@@@@@@@ DOC ACKNOWLEDGEMENT CREATION START @@@@@@@@@@-----> "+"-----> " +docAcknowledgementList.size()+"-----> "+ System.currentTimeMillis());
					
					for( DocAcknowledgement docAcknowledgement : docAcknowledgementList )
					{
						ReceiptOfDocumentsDTO receiptOfDocumentsDTO = null;
						Claim claim = docAcknowledgement.getClaim();
						if(claim != null & claim.getClaimId() != null)
						{
							log.info("claim.getClaimId() : "+claim.getClaimId());
							receiptOfDocumentsDTO = new ReceiptOfDocumentsDTO();
							DocumentDetailsDTO documentDetailsDTO = new DocumentDetailsDTO();
							documentDetailsDTO.setDocumentsReceivedFrom(new SelectValue(1L,"insured"));
							documentDetailsDTO.setDocumentCheckList(premiaPullService.getDocumentList(docAcknowledgement.getKey()));
							receiptOfDocumentsDTO.setDocumentDetails(documentDetailsDTO);
							
							ClaimMapper claimMapper = ClaimMapper.getInstance();
							ClaimDto claimDto = claimMapper.getClaimDto(claim);
							NewIntimationDto newIntimationDto = NewIntimationMapper.getInstance()
									.getNewIntimationDto(claim.getIntimation());
							log.info("newIntimationDto : "+newIntimationDto.getInsuredPatientName());
							
							claimDto.setNewIntimationDto(newIntimationDto);
							
							/*newIntimationDto.getInsuredPatient().getInsuredName();*/
							
							Long policyKey = claim.getIntimation().getPolicy().getKey();
							
							Policy policy = premiaPullService.getPolicyByPolicyKey(policyKey);

							/*PolicyMapper policyMapper = new PolicyMapper();
							PolicyDto policyDto = policyMapper.getPolicyDto(policy);
							
							Policy policy2 = policyMapper.getPolicy(policyDto);*/
							
							claimDto.getNewIntimationDto().setPolicy(policy);
							
							Insured insured = claim.getIntimation().getInsured();
							/*InsuredMapper insuredMapper = new InsuredMapper();
							InsuredDto insuredDto = insuredMapper.getInsuredDto(insured);
							
							Insured newinsured = insuredMapper.getInsured(insuredDto);*/
							claimDto.getNewIntimationDto().setInsuredPatient(insured);

							log.info("getIntimation().getPolicy().getKey() :  "+claim.getIntimation().getPolicy().getKey());
							receiptOfDocumentsDTO.getDocumentDetails().setAcknowledgementNumber(docAcknowledgement.getAcknowledgeNumber());
							receiptOfDocumentsDTO.setClaimDTO(claimDto);
							receiptOfDocumentsDTO.setStrUserName("System");
							/*receiptOfDocumentsDTO.getDocumentDetails().setAcknowledgementNumber(docAcknowledgement.getAcknowledgeNumber());
							Intimation intimation = claim.getIntimation();
							log.info("intimation ---- >>> "+intimation.getIntimationId());
							NewIntimationMapper intimationMapper = NewIntimationMapper.getInstance();
							receiptOfDocumentsDTO.setNewIntimationDTO( intimationMapper.getNewIntimationDto(intimation));
							
							NewIntimationDto newIntimationDTO = receiptOfDocumentsDTO.getNewIntimationDTO();
							
							Insured insuredPatient = newIntimationDTO.getInsuredPatient();
							log.info("insuredPatient.getInsuredName() : "+insuredPatient.getInsuredName());
							
							receiptOfDocumentsDTO.getNewIntimationDTO().setInsuredPatient(insuredPatient);
							
							LegalHeirDTO legalHeirDTO = new LegalHeirDTO();
							legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
							receiptOfDocumentsDTO.getPreauthDTO().setLegalHeirDto(legalHeirDTO);
							
							receiptOfDocumentsDTO.setStrUserName("sh022077");*/
							/*receiptOfDocumentsDTO.setStrPassword("Star@123");*/
							
							/*RRCDTO rrcDTO = new RRCDTO();
							rrcDTO.setClaimDto(claimDto);
							rrcDTO.setNewIntimationDTO(newIntimationDTO);
							rrcDTO.setStrUserName("sh022077");
							receiptOfDocumentsDTO.setRrcDTO(rrcDTO);*/
							/*newIntimationDTO.setInsuredDeceasedFlag(dBCalculationService.getInsuredPatientStatus(newIntimationDTO.getPolicy().getKey(), newIntimationDTO.getInsuredPatient().getKey()));
							receiptOfDocumentsDTO.setIsAlreadyHospitalizationExist(reimbursementService.isPreviousHospAcknowledgment(claim.getKey()));*/
							
							reportDto = new ReportDto();
							reportDto.setBeanList(Arrays.asList(receiptOfDocumentsDTO));
							reportDto.setClaimId(claimDto.getClaimId());
							docGen = new DocumentGenerator();
							
							String filePath = docGen.generatePdfDocument(templateName, reportDto);
							
							if( !filePath.isEmpty() )
							{
								receiptOfDocumentsDTO.setDocFilePath(filePath);
								receiptOfDocumentsDTO.setDocType(SHAConstants.ACK_RECEIPT);
								
								if(null != receiptOfDocumentsDTO.getDocFilePath() && !("").equalsIgnoreCase(receiptOfDocumentsDTO.getDocFilePath()))
								{
									docAcknowledgement.setClaim(claim);
									reimbursementService.generateDocForTopup( docAcknowledgement , receiptOfDocumentsDTO );
								}
								docAcknowledgement.setAckLetterGenFlag("Y");
								docAcknowledgement.setAckLetterGenDate(new Timestamp(System.currentTimeMillis()));

								if( docAcknowledgement != null )
								{
									premiaPullService.updateAcknowledgementLetterGenFlag(docAcknowledgement);	
								}
							}
						}
						else
						{
							log.info("claim.getClaimId() is null ");
						}
					}
					
					/*List<ReceiptOfDocumentsDTO> rodDTOList = null;
					ReceiptOfDocumentsDTO bean = null;
					ReportDto reportDto = null;
					DocumentGenerator docGen = null;
					
					AcknowledgeDocumentReceivedMapper receivedMapper = AcknowledgeDocumentReceivedMapper.getInstance();
					
					rodDTOList = AcknowledgeDocumentReceivedMapper.getReceiptOfDocumentsDTOs(docAcknowledgementList);
					for(ReceiptOfDocumentsDTO receiptOfDocumentsDTO : rodDTOList){
						DocAcknowledgement docAcknowledgement = receivedMapper.getDocAckRecDetails(receiptOfDocumentsDTO);
						Claim claim = preauthService.searchByClaimKey(receiptOfDocumentsDTO.getClaimDTO().getKey());

						DocAcknowledgement docAcknowledgement2 = reimbursementService.getAcknowledgementByClaimKey(claim.getKey());
						

						
						
						receiptOfDocumentsDTO.getAcknowledgementNumber();
						reportDto = new ReportDto();
						reportDto.setBeanList(Arrays.asList(receiptOfDocumentsDTO));
						docGen = new DocumentGenerator();
						String filePath = docGen.generatePdfDocument(templateName, reportDto);

						receiptOfDocumentsDTO.setDocFilePath(filePath);
						receiptOfDocumentsDTO.setDocType(SHAConstants.ACK_RECEIPT);

						if(null != receiptOfDocumentsDTO.getDocFilePath() && !("").equalsIgnoreCase(receiptOfDocumentsDTO.getDocFilePath()))
						{
							docAcknowledgement.setClaim(claim);
							reimbursementService.generateDocForTopup( docAcknowledgement , receiptOfDocumentsDTO );
						}

						log.info("@@@@@@@@@ DOC ACKNOWLEDGEMENT CREATION END@@@@@@@@@@-----> "+"-----> "+ System.currentTimeMillis());
					}*/
				}
				else
					log.info("docAcknowledgementList  flag is empty : "+docAcknowledgementList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void doGalaxyIntimationPull() {
			List<GalaxyIntimationTable> processPremiaIntimationData = premiaPullService.processGalaxyIntimationData("20");
			if(processPremiaIntimationData != null && !processPremiaIntimationData.isEmpty()) {
				
				for (GalaxyIntimationTable premiaIntimationTable : processPremiaIntimationData) {
					log.info("@@@@@@@@@GLX INTIMATION PULL STARTING TIME FOR  NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
					try {
						if(!premiaPullService.isIntimationExist(premiaIntimationTable.getGiIntimationNo())) {
							if((null != premiaIntimationTable.getGiHospCode() && !premiaIntimationTable.getGiHospCode().isEmpty()) && premiaPullService.isPolicyAllowed(premiaIntimationTable.getGiPolNo())) {
								String policyServiceType = "OTHERS";
								Boolean isIntegratedPolicy = true;
								if(premiaIntimationTable.getProductCode() != null){
									Product productByProductCode = premiaPullService.getProductByProductCode(premiaIntimationTable.getProductCode());
									if(productByProductCode != null && productByProductCode.getProductService() != null){
										policyServiceType = productByProductCode.getProductService();
									}
								}
								PremPolicyDetails policyDetails = null;
								
								log.info("@@@@@@@@@PREMIA WEB SERVICE CALL STARTING TIME FOR GLX NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								
								if(policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo(), premiaIntimationTable.getGiInsuredName());
									isIntegratedPolicy =premiaPullService. populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getGiInsuredName(),false);
									
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGpaPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo(), premiaIntimationTable.getGiInsuredName());
									policyDetails.setRiskSysId(premiaIntimationTable.getGiInsuredName());
									isIntegratedPolicy = premiaPullService.populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getGiInsuredName(),false);
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.JET_PRIVILLAGE_POL_SERVICE)){
									policyDetails = premiaPullService.fetchJetPrivillagePolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getGiInsuredName(),false);
								}else{
									policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getGiInsuredName(),false);
								}
								log.info("@@@@@@@@@ PREMIA WEB SERVICE CALL ENDING TIME FOR GLX NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								if(isIntegratedPolicy) {
									
									log.info("@@@@@@@@@ INSERT GLX INTIMATION STARTING TIME FOR  NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
//									Intimation intimation = premiaPullService.insertIntimationDataRevised(premiaIntimationTable);
									Intimation intimation = premiaPullService.insertGalaxyIntimationDataRevised(premiaIntimationTable,policyDetails.getLob());
									
									log.info("@@@@@@@@@ INSERT GLX INTIMATION ENDING TIME FOR  NON GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									
									if(null != intimation ){
										log.info("***** SUCCESSFULLY GLX INITMATION CREATED FOR  NON GMC NETWORK ********************-----> " + intimation.getIntimationId());
									}
									else {
										log.info("***************** Failed to Create INITMATION for Galaxy Intimation ********************-----> "+ premiaIntimationTable.getGiIntimationNo());
										premiaPullService.updateGalaxyIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
									}
									if(null != intimation && null != intimation.getHospital() && null != intimation.getCpuCode()) {
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME GLX NON GMC  @@@@@@@@@@-----> "+ System.currentTimeMillis());
										premiaPullService.processSavedIntimationRevisedGalaxy(intimation, premiaIntimationTable);
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME GLX NON GMC @@@@@@@@@@-----> "+ System.currentTimeMillis());
									} 
								} else {
									log.info("#^#^^#^#^#THIS PRODUCT IS NOT INTREGRATED IN GALAXY----S-- OR PRODUCT CODE IS WRONG PLEASE CHECK THE POLCIY NUMBER OR POLICY NOT SAVED WHILE SAVING THE POLICY DATA---> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode() + " Policy No---> " + premiaIntimationTable.getGiPolNo() );
								}
							} else {
								log.info("#*#*#*#**#*#*#*#*#*#*#* THIS POLICY IS NOT ALLOWED IN GALAXY----BASED ON GLX INTIMATION IMPORT VALIDATION VALUES *#*#*#*#*#*#*#*#*#-----> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode());
								premiaPullService.updateGalaxyIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
							}
						} else {
							log.info("%*&%*%&%*%&%*%&%*(%*% THIS GLX INTIMATION IS ALREADY EXISTING PLEASE CHECK #*@&(#&@(@*@(*@*(@ --->" + premiaIntimationTable.getGiIntimationNo());
							premiaPullService.updateGalaxyIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					log.info("@@@@@@@@@GLX INTIMATION PULL ENDING TIME @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
				}
				
				
			}
		}
		
		public void doGalaxyIntimationPullForGmc() {
			List<GalaxyIntimationTable> processPremiaIntimationData = premiaPullService.processGalaxyIntimationDataForGmc("20");
			if(processPremiaIntimationData != null && !processPremiaIntimationData.isEmpty()) {
				
				for (GalaxyIntimationTable premiaIntimationTable : processPremiaIntimationData) {
					log.info("@@@@@@@@@GLX INTIMATION PULL STARTING TIME FOR GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
					try {
						if(!premiaPullService.isIntimationExist(premiaIntimationTable.getGiIntimationNo())) {
							if((null != premiaIntimationTable.getGiHospCode() && !premiaIntimationTable.getGiHospCode().isEmpty()) && premiaPullService.isPolicyAllowed(premiaIntimationTable.getGiPolNo())) {
								String policyServiceType = "OTHERS";
								Boolean isIntegratedPolicy = true;
								if(premiaIntimationTable.getProductCode() != null){
									Product productByProductCode = premiaPullService.getProductByProductCode(premiaIntimationTable.getProductCode());
									if(productByProductCode != null && productByProductCode.getProductService() != null){
										policyServiceType = productByProductCode.getProductService();
									}
								}
								PremPolicyDetails policyDetails = null;
								
								log.info("@@@@@@@@@PREMIA WEB SERVICE CALL STARTING TIME FOR GLX GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								
								if(policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
									
									log.info("@@@@@@@@@ POLICY WEB SERVICE CALL STARTING TIME FOR GLX GMC @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									
									policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo(), premiaIntimationTable.getGiInsuredName());
									isIntegratedPolicy =premiaPullService. populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getGiInsuredName(),false);
									
									log.info("@@@@@@@@@POLICY WEB SERVICE CALL ENDING TIME FOR GLX GMC @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGpaPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo(), premiaIntimationTable.getGiInsuredName());
									policyDetails.setRiskSysId(premiaIntimationTable.getGiInsuredName());
									isIntegratedPolicy = premiaPullService.populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getGiInsuredName(),false);
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.JET_PRIVILLAGE_POL_SERVICE)){
									policyDetails = premiaPullService.fetchJetPrivillagePolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getGiInsuredName(),false);
								}else{
									policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getGiInsuredName(),false);
								}
								log.info("@@@@@@@@@ PREMIA WEB SERVICE CALL ENDING TIME FOR GLX GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								if(isIntegratedPolicy) {
									
									log.info("@@@@@@@@@ INSERT GLX INTIMATION STARTING TIME FOR GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
//									Intimation intimation = premiaPullService.insertIntimationDataRevised(premiaIntimationTable);
									Intimation intimation = premiaPullService.insertGalaxyIntimationDataRevised(premiaIntimationTable,policyDetails.getLob());
									
									log.info("@@@@@@@@@ INSERT GLX INTIMATION ENDING TIME FOR GMC NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									
									if(null != intimation ){
										log.info("***** SUCCESSFULLY GLX INITMATION CREATED FOR GMC NETWORK ********************-----> " + intimation.getIntimationId());
									}
									else{
										log.info("***************** Failed to Create GLX INITMATION for Premia Intimation ********************-----> "+ premiaIntimationTable.getGiIntimationNo());
										premiaPullService.updateGalaxyIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
									}
									if(null != intimation && null != intimation.getHospital() && null != intimation.getCpuCode()) {
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME FOR GLX GMC @@@@@@@@@@-----> "+ System.currentTimeMillis());
										premiaPullService.processSavedIntimationRevisedGalaxy(intimation, premiaIntimationTable);
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME FOR GLX  GMC @@@@@@@@@@-----> "+ System.currentTimeMillis());
									} 
								} else {
									log.info("#^#^^#^#^#THIS PRODUCT IS NOT INTREGRATED IN GALAXY----S-- OR PRODUCT CODE IS WRONG PLEASE CHECK THE POLCIY NUMBER OR POLICY NOT SAVED WHILE SAVING THE POLICY DATA  FOR GMC ---> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode() + " Policy No---> " + premiaIntimationTable.getGiPolNo() );
								}
							} else {
								log.info("#*#*#*#**#*#*#*#*#*#*#* THIS POLICY IS NOT ALLOWED IN GALAXY----BASED ON GLX INTIMATION IMPORT VALIDATION VALUES  FOR GMC *#*#*#*#*#*#*#*#*#-----> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode());
								premiaPullService.updateGalaxyIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
							}
						} else {
							log.info("%*&%*%&%*%&%*%&%*(%*% THIS GLX INTIMATION IS ALREADY EXISTING PLEASE CHECK  FOR GMC  #*@&(#&@(@*@(*@*(@ --->" + premiaIntimationTable.getGiIntimationNo());
							premiaPullService.updateGalaxyIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					log.info("@@@@@@@@@GLX INTIMATION PULL ENDING TIME FOR GMC @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
				}
				
				
			}
		}
		
		public void doGalaxyIntimationPullForNonNetwork(Boolean isError) {
			
			List<GalaxyIntimationTable> processPremiaIntimationData = null; 
			
			if(isError){
				processPremiaIntimationData = premiaPullService.processGlxIntimationForNonNetworkError("20");
			}
			else{
				processPremiaIntimationData = premiaPullService.processGalaxyIntimationDataForNonNetwork("20");
			}
			
			if(processPremiaIntimationData != null && !processPremiaIntimationData.isEmpty()) {
				
				for (GalaxyIntimationTable premiaIntimationTable : processPremiaIntimationData) {
					log.info("@@@@@@@@@GLX INTIMATION PULL STARTING TIME FOR NON NETWORK@@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
					try {
						if(!premiaPullService.isIntimationExist(premiaIntimationTable.getGiIntimationNo())) {
							if((null != premiaIntimationTable.getGiHospCode() && !premiaIntimationTable.getGiHospCode().isEmpty()) && premiaPullService.isPolicyAllowed(premiaIntimationTable.getGiPolNo())) {
								String policyServiceType = "OTHERS";
								Boolean isIntegratedPolicy = true;
								if(premiaIntimationTable.getProductCode() != null){
									Product productByProductCode = premiaPullService.getProductByProductCode(premiaIntimationTable.getProductCode());
									if(productByProductCode != null && productByProductCode.getProductService() != null){
										policyServiceType = productByProductCode.getProductService();
									}
								}
								PremPolicyDetails policyDetails = null;
								
								log.info("@@@@@@@@@PREMIA WEB SERVICE CALL STARTING TIME FOR GLX NON NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								
								if(policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo(), premiaIntimationTable.getGiInsuredName());
									isIntegratedPolicy =premiaPullService. populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getGiInsuredName(),false);
									
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchGpaPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo(), premiaIntimationTable.getGiInsuredName());
									policyDetails.setRiskSysId(premiaIntimationTable.getGiInsuredName());
									isIntegratedPolicy = premiaPullService.populateGMCandGPAPolicy(policyDetails, premiaIntimationTable.getGiInsuredName(),false);
								}else if(policyServiceType.equalsIgnoreCase(SHAConstants.JET_PRIVILLAGE_POL_SERVICE)){
									policyDetails = premiaPullService.fetchJetPrivillagePolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getGiInsuredName(),false);
								}else{
									policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(premiaIntimationTable
											.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails,premiaIntimationTable.getGiInsuredName(),false);
								}
								log.info("@@@@@@@@@ PREMIA WEB SERVICE CALL ENDING TIME FOR GLX NON NETWORK@@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								if(isIntegratedPolicy) {
									
									log.info("@@@@@@@@@ INSERT GLX INTIMATION STARTING TIME FOR NON NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
//									Intimation intimation = premiaPullService.insertIntimationDataRevised(premiaIntimationTable);
									Intimation intimation = premiaPullService.insertGalaxyIntimationDataRevised(premiaIntimationTable,policyDetails.getLob());
									
									log.info("@@@@@@@@@ INSERT GLX INTIMATION ENDING TIME FOR NON NETWORK@@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									
									if(null != intimation ){
										log.info("***** SUCCESSFULLY GLX INITMATION CREATED FOR NON NETWORK ********************-----> " + intimation.getIntimationId());
									}
									else{
										log.info("***************** Failed to Create GLX INITMATION for Premia Intimation ********************-----> "+ premiaIntimationTable.getGiIntimationNo());
										premiaPullService.updateGalaxyIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
									}
									if(null != intimation && null != intimation.getHospital() && null != intimation.getCpuCode()) {
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME FOR GLX NON NETWORK@@@@@@@@@@-----> "+ System.currentTimeMillis());
										premiaPullService.processSavedIntimationRevisedGalaxy(intimation, premiaIntimationTable);
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME FOR GLX NON NETWORK@@@@@@@@@@-----> "+ System.currentTimeMillis());
									} 
								} else {
									log.info("#^#^^#^#^#THIS PRODUCT IS NOT INTREGRATED IN GALAXY----S-- OR PRODUCT CODE IS WRONG PLEASE CHECK THE POLCIY NUMBER OR POLICY NOT SAVED WHILE SAVING THE POLICY DATA---> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode() + " Policy No---> " + premiaIntimationTable.getGiPolNo() );
								}
							} else {
								log.info("#*#*#*#**#*#*#*#*#*#*#* THIS POLICY IS NOT ALLOWED IN GALAXY----BASED ON GLX INTIMATION IMPORT VALIDATION VALUES *#*#*#*#*#*#*#*#*#-----> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode());
								premiaPullService.updateGalaxyIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
							}
						} else {
							log.info("%*&%*%&%*%&%*%&%*(%*% THIS GLX INTIMATION IS ALREADY EXISTING PLEASE CHECK #*@&(#&@(@*@(*@*(@ --->" + premiaIntimationTable.getGiIntimationNo());
							premiaPullService.updateGalaxyIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					log.info("@@@@@@@@@GLX INTIMATION PULL ENDING TIME FOR NON NETWORK @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
				}
				
				
			}
		}
		
		//Added For Accident Trauma care Group product		
		public void doGalaxyIntimationPullForPA() {
			List<GalaxyIntimationTable> processPremiaIntimationData = premiaPullService.processGalaxyIntimationDataForPA("20");
			if(processPremiaIntimationData != null && !processPremiaIntimationData.isEmpty()) {				
				for (GalaxyIntimationTable premiaIntimationTable : processPremiaIntimationData) {
					log.info("@@@@@@@@@GALAXY INTIMATION PULL STARTING TIME FOR ACCIDENT PA @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
					try {
						if(!premiaPullService.isIntimationExist(premiaIntimationTable.getGiIntimationNo())) {
							if((null != premiaIntimationTable.getGiHospCode() && !premiaIntimationTable.getGiHospCode().isEmpty()) && premiaPullService.isPolicyAllowed(premiaIntimationTable.getGiPolNo())) {
								String policyServiceType = "OTHERS";
								Boolean isIntegratedPolicy = true;
								if(premiaIntimationTable.getProductCode() != null){
									Product productByProductCode = premiaPullService.getProductByProductCode(premiaIntimationTable.getProductCode());
									if(productByProductCode != null && productByProductCode.getProductService() != null){
										policyServiceType = productByProductCode.getProductService();
									}
								}
								PremPolicyDetails policyDetails = null;
								
								log.info("@@@@@@@@@PREMIA WEB SERVICE CALL STARTING TIME FOR GLX ACCIDENT PA @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								
								if(policyServiceType.equalsIgnoreCase(SHAConstants.OTHER_POL_SERIVICE)){
									policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(premiaIntimationTable.getGiPolNo());
									isIntegratedPolicy = premiaPullService.populatePolicyFromPAPolicy(policyDetails,premiaIntimationTable.getGiInsuredName(),false);
								}
								log.info("@@@@@@@@@ PREMIA WEB SERVICE CALL ENDING TIME FOR GLX ACCIDENT PA @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
								
								if(isIntegratedPolicy) {
									
									log.info("@@@@@@@@@ INSERT GLX INTIMATION STARTING TIME FOR ACCIDENT PA @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									Intimation intimation = premiaPullService.insertGalaxyIntimationDataRevised(premiaIntimationTable,policyDetails.getLob());
									log.info("@@@@@@@@@ INSERT GLX INTIMATION ENDING TIME FOR ACCIDENT PA  @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
									
									if(null != intimation ){
										log.info("***** SUCCESSFULLY GLX INITMATION CREATED FOR ACCIDENT PA  ********************-----> " + intimation.getIntimationId());
									}
									else {
										log.info("***************** Failed to Create INITMATION for Galaxy Intimation ********************-----> "+ premiaIntimationTable.getGiIntimationNo());
										premiaPullService.updateGalaxyIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
									}
									if(null != intimation && null != intimation.getHospital() && null != intimation.getCpuCode()) {
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME GLX ACCIDENT PA  @@@@@@@@@@-----> "+ System.currentTimeMillis());
										premiaPullService.processSavedIntimationRevisedGalaxy(intimation, premiaIntimationTable);
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME GLX ACCIDENT PA  @@@@@@@@@@-----> "+ System.currentTimeMillis());
									} 
								} else {
									log.info("#^#^^#^#^#THIS PRODUCT IS NOT INTREGRATED IN GALAXY----S-- OR PRODUCT CODE IS WRONG PLEASE CHECK THE POLCIY NUMBER OR POLICY NOT SAVED WHILE SAVING THE POLICY DATA---> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode() + " Policy No---> " + premiaIntimationTable.getGiPolNo() );
								}
							} else {
								log.info("#*#*#*#**#*#*#*#*#*#*#* THIS POLICY IS NOT ALLOWED IN GALAXY----BASED ON GLX INTIMATION IMPORT VALIDATION VALUES *#*#*#*#*#*#*#*#*#-----> " + premiaIntimationTable.getGiIntimationNo() + "  HOSP CODE" + premiaIntimationTable.getGiHospCode());
								premiaPullService.updateGalaxyIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_ERROR_STATUS);
							}
						} else {
							log.info("%*&%*%&%*%&%*%&%*(%*% THIS GLX INTIMATION IS ALREADY EXISTING PLEASE CHECK #*@&(#&@(@*@(*@*(@ --->" + premiaIntimationTable.getGiIntimationNo());
							premiaPullService.updateGalaxyIntimationTable(premiaIntimationTable, SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					log.info("@@@@@@@@@GLX INTIMATION PULL ENDING TIME @@@@@@@@@@-----> "+"-----> " +premiaIntimationTable.getGiIntimationNo()+"-----> "+ System.currentTimeMillis());
				}
				
				
			}
		}
		
		public void doPremiaIntimationPullForOPCashless() {
			List<OPPremiaIntimationTable> processOPPremiaIntimationData = premiaPullService.processOPPremiaIntimationDataForCashless("20");
			if(processOPPremiaIntimationData != null && !processOPPremiaIntimationData.isEmpty()) {
				
				for (OPPremiaIntimationTable oppremiaIntimationTable : processOPPremiaIntimationData) {
					log.info("@@@@@@@@@ INTIMATION PULL STARTING TIME FOR OP CASHLESS INTIMATION @@@@@@@@@@-----> "+"-----> " +oppremiaIntimationTable.getIntimationNumber()+"-----> "+ System.currentTimeMillis());
					try {
						if(!premiaPullService.isOPIntimationExist(oppremiaIntimationTable.getIntimationNumber())) {
							if(premiaPullService.isPolicyAllowed(oppremiaIntimationTable.getPolicyNumber())) {
								String policyServiceType = "OTHERS";
								Boolean isIntegratedPolicy = true;
								if(oppremiaIntimationTable.getProdCode() != null){
									Product productByProductCode = premiaPullService.getProductByProductCode(oppremiaIntimationTable.getProdCode());
									if(productByProductCode != null && productByProductCode.getProductService() != null){
										policyServiceType = productByProductCode.getProductService();
									}
								}
								PremPolicyDetails policyDetails = null;
								
								log.info("@@@@@@@@@PREMIA WEB SERVICE CALL STARTING TIME FOR  OP CASHLESS INTIMATION @@@@@@@@@@-----> "+"-----> " +oppremiaIntimationTable.getIntimationNumber()+"-----> "+ System.currentTimeMillis());
								
									policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(oppremiaIntimationTable
											.getPolicyNumber());
									isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails, null, false);
//								}
								log.info("@@@@@@@@@ PREMIA WEB SERVICE CALL ENDING TIME FOR  OP CASHLESS INTIMATION @@@@@@@@@@-----> "+"-----> " +oppremiaIntimationTable.getIntimationNumber()+"-----> "+ System.currentTimeMillis());
								
								if(isIntegratedPolicy) {
									
									log.info("@@@@@@@@@ INSERT INTIMATION STARTING TIME FOR OP CASHLESS INTIMATION @@@@@@@@@@-----> "+"-----> " +oppremiaIntimationTable.getIntimationNumber()+"-----> "+ System.currentTimeMillis());
//									Intimation intimation = premiaPullService.insertIntimationDataRevised(premiaIntimationTable);
									OPIntimation opintimation = premiaPullService.insertOPIntimationData(oppremiaIntimationTable,policyDetails.getLob());
									
									log.info("@@@@@@@@@ INSERT INTIMATION ENDING TIME FOR  OP CASHLESS INTIMATION @@@@@@@@@@-----> "+"-----> " +oppremiaIntimationTable.getIntimationNumber()+"-----> "+ System.currentTimeMillis());
									
									if(null != opintimation ){
										log.info("***** SUCCESSFULLY INITMATION CREATED FOR  OP CASHLESS INTIMATION ********************-----> " + opintimation.getIntimationId());
									}
									else {
										log.info("***************** Failed to Create INITMATION for Premia OPIntimation CASHLESS ********************-----> "+ oppremiaIntimationTable.getIntimationNumber());
										premiaPullService.updateOPPremiaIntimationTable(oppremiaIntimationTable,SHAConstants.FAILURE_FLAG, SHAConstants.N_FLAG);
									}
									if(null != opintimation) {
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL STARTING TIME OP CASHLESS INTIMATION @@@@@@@@@@-----> "+ System.currentTimeMillis());
										premiaPullService.processSavedOPIntimationRevisedForCashless(opintimation, oppremiaIntimationTable);
										log.info("@@@@@@@@@ SUBMIT PROCEDURE CALL ENDING TIME  OP CASHLESS INTIMATION @@@@@@@@@@-----> "+ System.currentTimeMillis());
									} 
								} else {
//									log.info("#^#^^#^#^#THIS PRODUCT IS NOT INTREGRATED IN GALAXY----S-- OR PRODUCT CODE IS WRONG PLEASE CHECK THE POLCIY NUMBER OR POLICY NOT SAVED WHILE SAVING THE POLICY DATA---> " + oppremiaIntimationTable.getGiIntimationNo() + " Policy No---> " + premiaIntimationTable.getGiPolNo() );
								}
							} else {
								log.info("#*#*#*#**#*#*#*#*#*#*#* THIS POLICY IS NOT ALLOWED IN GALAXY----BASED ON INTIMATION IMPORT VALIDATION VALUES *#*#*#*#*#*#*#*#*#-----> " + oppremiaIntimationTable.getIntimationNumber());
							}
						} else {
							log.info("%*&%*%&%*%&%*%&%*(%*% THIS INTIMATION IS ALREADY EXISTING PLEASE CHECK #*@&(#&@(@*@(*@*(@ --->" + oppremiaIntimationTable.getIntimationNumber());
							premiaPullService.updateOPPremiaIntimationTable(oppremiaIntimationTable,SHAConstants.SUCCESS_FLAG, SHAConstants.YES_FLAG);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					log.info("@@@@@@@@@ INTIMATION PULL ENDING TIME @@@@@@@@@@-----> "+"-----> " +oppremiaIntimationTable.getIntimationNumber()+"-----> "+ System.currentTimeMillis());
				}
				
				
			}
		}
		public void callPremiaWSForInstallmentPremium() {
			premiaPullService.adjustPolicyInstallmentProcess();
		}
}
