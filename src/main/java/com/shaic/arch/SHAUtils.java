package com.shaic.arch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.inject.Instance;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.branchmanagerfeedback.BranchManagerFeedbackTableDTO;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.coinsurance.view.CoInsuranceDetailView;
import com.shaic.claim.coinsurance.view.CoInsuranceTableDTO;
import com.shaic.claim.common.NursingChargesMatchingDTO;
import com.shaic.claim.common.RoomRentMatchingDTO;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryTableDTO;
import com.shaic.claim.lumen.CommonSearchFormData;
import com.shaic.claim.outpatient.registerclaim.wizard.DiagnosisDetailsOPTableDTO;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.claim.policy.search.ui.PremBonusDetails;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.dto.ConsolidatedAmountDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.HopitalizationCalulationDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.OtherInsHospSettlementDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.OtherInsPostHospSettlementDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.OtherInsPreHospSettlementDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.PostHopitalizationDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.PreHopitalizationDetailsDTO;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.EsclateClaimToRawPage;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitalisation;
import com.shaic.domain.Hospitals;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.LumenRequest;
import com.shaic.domain.MasBillCategory;
import com.shaic.domain.MasBillClassification;
import com.shaic.domain.MasBillDetailsType;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OPClaim;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ProviderWiseScoring;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReportLog;
import com.shaic.domain.ZUASendQueryTable;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.BillItemMapping;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.feedback.managerfeedback.ManagerFeedBackPolicyTableDto;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.cashless.listenertables.PAAmountConsideredTable;
import com.shaic.reimbursement.fraudidentification.FraudIdentificationTableDTO;
import com.shaic.reimbursement.topup_policy_master.search.TopUpPolicyMasterTableDTO;
import com.shaic.restservices.bancs.consumerDetails.BancsConsumerDetailService;
import com.sun.jersey.api.client.WebResource.Builder;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.zybnet.autocomplete.server.AutocompleteField;

import oracle.sql.DATE;

public class SHAUtils {
	

	 	
//	private final Logger logger = LoggerFactory.getLogger(SHAUtils.class);
	
	private Map<Integer, Long> seniorCitizenViewMap;

	private boolean isPackageAvailable = true;

	private String packageFlg = "N";

	public static Map<Long, String> statusMap = getStatusMap();
	
	public static Map<Long,String> baseCellMap = getBaseCellMap();
	
	public static Map<Long, Integer> baseCellSno = getBaseCellSno();
	
	public static Map<Long, Long> rejectStatusMap = getRejectStatusMap();
	
	public static Map<Long, Long> cashlessTableMapping = getRejectStatusMap();
	
	public final static String DEFAULT_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public final static String DEFAULT_DATE_WITHOUT_TIME_FORMAT = "dd/MM/yyyy";
	
	public static BeanItemContainer<SelectValue> dianosisListValue = null;
	
	public static Map<Long, Long> fvrInitateMAStatusMap = getInitiateFVRStatusMap();
	
	private static DecimalFormat df = new DecimalFormat("0.00");
	
	public static BeanItemContainer<SelectValue> employeeListValue = null;

	public static Map<Long,String> legalbaseCellMap = getLegalBaseCellMap();

	public static Map<Long, Integer> legalbaseCellSno = getLegalBaseCellSno();
	
	
	
	public static Map<Long, String> getStatusMap() { 
		
		 Map<Long, String> statusMap =new HashMap<Long, String>();
		
		statusMap.put(16l, "Query");
		statusMap.put(17l, "Suggest Rejection");
		statusMap.put(15L, "Waived Rejection");
		statusMap.put(18l, "Send For Processing");
		statusMap.put(60l, "Refer to Coordinator");
		statusMap.put(19l, "Query Reply Received");
		statusMap.put(20l, "Suggest Rejection");
		statusMap.put(21l, "Send For Processing");
		statusMap.put(61l, "Refer To Coordinator");
		statusMap.put(22l, "Approve");
		statusMap.put(23l, "Reject");
		statusMap.put(175l, "Cashless Reopened");
		statusMap.put(24l, "Query");
		statusMap.put(25l, "Query Reply Received");
		statusMap.put(26l, "Denial of cashless");
		statusMap.put(27l, "Esclation");
		statusMap.put(28l, "Refer to coordinator");
		statusMap.put(29l, "Coordinator Reply Received");
		statusMap.put(30l, "Approve");
		statusMap.put(31l, "Reject");
		statusMap.put(32l, "Query Reply Received");
		statusMap.put(33l, "Query Reply Received");
		statusMap.put(34l, "Denial of cashless");
		statusMap.put(35l, "Esclation");
		statusMap.put(36l, "Refer to coordinator");
		statusMap.put(37l, "Coordinator Reply Received");
		statusMap.put(66l, "Query Reply Received");
		statusMap.put(65l, "Query Reply Received");
		statusMap.put(62l, "Specialist Reply Received");
		statusMap.put(68l, "Investigation Disapproved");
		statusMap.put(50l, "FVR Reply Received");
		statusMap.put(51l, "FVR Assigned");
		statusMap.put(52l, "Skip Fvr");
		statusMap.put(70l, "Document Submitted");
		statusMap.put(71l, "ROD Completed");
		statusMap.put(72l, "Bill Entry Completed");
		statusMap.put(73l, "Suggest Query");
		statusMap.put(74l, "Rejected");
		statusMap.put(75l, "Approved");
		statusMap.put(76l, "Refer To Coordinator");
		statusMap.put(77l, "Coordinator Reply");
		statusMap.put(78l, "Approved");
		statusMap.put(79l, "FVR Initiated");
		statusMap.put(80l, "Investigation Initiated");
		statusMap.put(81l, "Refer To Coordinator");
		statusMap.put(82l, "Coordinator Reply");
		statusMap.put(83l, "Specialist");
		statusMap.put(84l, "Specialist Reply");
		statusMap.put(85l, "Escalation Reply");
		statusMap.put(86l, "Escalation");
		statusMap.put(87l, "Send To Reply");
		statusMap.put(91l, "Reject Query");
		statusMap.put(96l, "Disapproved Rejection");
		statusMap.put(99l, "Coordinator Reply");
		statusMap.put(100l, "Refer To Medical Approver");
		statusMap.put(101l, "Send To Financial Approver");
		statusMap.put(102l, "Benefits Approved");
		statusMap.put(103l, "Approved");
		statusMap.put(104l, "Refer To Medical Approver");
		statusMap.put(105l, "Refer To Billing");
		statusMap.put(106l, "Initiate FVR");
		statusMap.put(107l, "Initiate Investigation");
		statusMap.put(108l, "Refer To Coordinator");
		statusMap.put(109l, "Coordinator Reply Received");
		statusMap.put(110l, "Refer To Specialist");
		statusMap.put(111l, "Specialist Reply Received");
		statusMap.put(112l, "FA-Initiate Query");
		statusMap.put(115l, "Reject Query");
		statusMap.put(121l, "FA - Approve Reject");
		statusMap.put(122l, "FA - Benefits Approved");
		statusMap.put(139l, "Coordinator Reply Received");
		statusMap.put(202L, "Draft Investigation letter");
		statusMap.put(56L, "Investigator Assigned");
		statusMap.put(88L, "Query Initiated");
		statusMap.put(89L, "Query Drafted");
		statusMap.put(90L, "Query Redrafted");
		statusMap.put(92L, "Query Approved");
		statusMap.put(162L, "Query Reply Received");
		statusMap.put(203L, "Investigation Initiated");
		
		statusMap.put(ReferenceTable.ZONAL_REVIEW_REOPENED, "Reopened");
		statusMap.put(ReferenceTable.CLAIM_REQUEST_REOPENED, "Reopened");
		statusMap.put(ReferenceTable.BILLING_REOPENED, "Reopened");
		statusMap.put(ReferenceTable.FINANCIAL_REOPENED, "Reopened");
		statusMap.put(ReferenceTable.CREATE_ROD_REOPENED,  "Reopened");
		statusMap.put(ReferenceTable.BILL_ENTRY_REOPENED,  "Reopened");
		
		statusMap.put(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS, "Downsize Preauth");
		statusMap.put(ReferenceTable.STANDALONE_WITHDRAW_STATUS, "Withdraw Preauth");
		statusMap.put(ReferenceTable.DOWNSIZE_APPROVED_STATUS, "Downsize Preauth");
		statusMap.put(ReferenceTable.DOWNSIZE_ESCALATION_STATUS, "Downsize Preauth");
		statusMap.put(ReferenceTable.WITHDRAW_APPROVED_STATUS, "Withdraw Preauth");
		statusMap.put(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED,"Investigation Completed");
		statusMap.put(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT, "Withdraw and Reject");
		statusMap.put(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS, "Withdraw and Reject");
		//added for withdrawn Post Process
		statusMap.put(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS, "Withdraw Preauth Post Process");
		statusMap.put(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER, "Billing Completed");
		statusMap.put(ReferenceTable.PREAUTH_REFER_TO_FLP_STATUS,"Refer to FLP");
		statusMap.put(ReferenceTable.ENHANCEMENT_REFER_TO_FLP_STATUS,"Refer to FLE");

		statusMap.put(ReferenceTable.FVR_REPLY_RECEIVED, "FVR Reply Received");
		statusMap.put(ReferenceTable.INITITATE_FVR, "FVR Initiated");
		statusMap.put(ReferenceTable.INITIATE_INVESTIGATION_APPROVED, "Investigation Approved");
		statusMap.put(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS, "Reply to FA");
		
		
		return statusMap;
	}
	
	public static Map<Long, String> getReferedStatusMap(){
		Map<Long, String> statusMap = new WeakHashMap<Long, String>();
		statusMap.put(ReferenceTable.ZMR_REFER_TO_BILL_ENTRY, SHAConstants.REF_FROM_ZMR);
		statusMap.put(ReferenceTable.CLAIM_REQUEST_REFER_TO_BILL_ENTRY, SHAConstants.REF_FROM_MA);
		statusMap.put(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY, SHAConstants.REF_FROM_BILLING);
		statusMap.put(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY, SHAConstants.REF_FROM_FA);
		
		return statusMap; 
	}	
	
	public static String getReferedFrom(Long statusKey){
		Map<Long, String> statusMap = getReferedStatusMap();
		String status = "";
		if(statusKey != null){
			status = statusMap.get(statusKey) != null ? statusMap.get(statusKey) : "";
		}
		return status;
	}
	
	public static Map<String,Object> getConvertStatusMap(){
		
		 Map<String,Object> conversionStatusKeyMap = new HashMap<String,Object>();
		
		 List<Long> withDrawkeyList = new ArrayList<Long>();
		 List<Long> denialList = new ArrayList<Long>();
		 List<Long> queryList = new ArrayList<Long>();
		 List<Long> ackList = new ArrayList<Long>();
		 withDrawkeyList.add(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS);
		 withDrawkeyList.add(ReferenceTable.STANDALONE_WITHDRAW_STATUS);
		 withDrawkeyList.add(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT);
		 withDrawkeyList.add(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS);
		 
		 queryList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS);
		 queryList.add(ReferenceTable.PREAUTH_QUERY_STATUS);
		 
		 denialList.add(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS);
		 denialList.add(ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS);
		 
		 ackList.add(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
		 
		 Long preauthNotRcvd = ReferenceTable.INTIMATION_REGISTERED_STATUS;
		 List<Long> preauthNotRcvdList = new ArrayList<Long>();
		 preauthNotRcvdList.add(preauthNotRcvd);
		 		 
		 conversionStatusKeyMap.put(SHAConstants.PRE_AUTH_NOT_RECEIVED, preauthNotRcvdList);
		 conversionStatusKeyMap.put(SHAConstants.WITHDRAW, withDrawkeyList);
		 conversionStatusKeyMap.put(SHAConstants.QUERY,queryList);
		 conversionStatusKeyMap.put(SHAConstants.CASHLESS_DENIAL,denialList);
		 
		 
		 List<Long> allKeyList = new ArrayList<Long>();
		 allKeyList.addAll(preauthNotRcvdList);
		 allKeyList.addAll(queryList);
		 allKeyList.addAll(withDrawkeyList);
		 allKeyList.addAll(ackList);
		 conversionStatusKeyMap.put(SHAConstants.ALL,allKeyList);
		 
		 return conversionStatusKeyMap;
	}
	
	public static Map<Long,Long> getConversionReasonId(){
		
//		Map<String,Long> conversionReasonIdMap = new HashMap<String, Long>();
//		conversionReasonIdMap.put(SHAConstants.PRE_AUTH_NOT_RECEIVED, ReferenceTable.PREAUTH_NOT_RECEIVED);
//		conversionReasonIdMap.put(SHAConstants.WITHDRAW, ReferenceTable.POST_WITHDRAW);
//		conversionReasonIdMap.put(SHAConstants.QUERY,ReferenceTable.POST_CASHLESS_QUERY);
//		conversionReasonIdMap.put(SHAConstants.CASHLESS_DENIAL ,ReferenceTable.POST_DENIAL);
		
		
    	Map<Long,Long> conversionReasonIdMap = new HashMap<Long,Long>();
    	
    	conversionReasonIdMap.put(ReferenceTable.PREAUTH_QUERY_STATUS, ReferenceTable.POST_CASHLESS_QUERY);
    	conversionReasonIdMap.put(ReferenceTable.INTIMATION_REGISTERED_STATUS, ReferenceTable.PREAUTH_NOT_RECEIVED);
    	conversionReasonIdMap.put(ReferenceTable.STANDALONE_WITHDRAW_STATUS, ReferenceTable.POST_WITHDRAW);
    	conversionReasonIdMap.put(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS, ReferenceTable.POST_WITHDRAW);
    	conversionReasonIdMap.put(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS, ReferenceTable.POST_DENIAL);
    	conversionReasonIdMap.put(ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS, ReferenceTable.POST_DENIAL);		
		
		return conversionReasonIdMap;
		
	}
	
	public static Map<Long, String> getBaseCellMap() {
		
		
		 Map<Long,String> baseCellMap = new HashMap<Long, String>();
		
		 baseCellMap.put(8l, "Room Rent & Nursing charges");
		 baseCellMap.put(9l, "ICU Charges");
		 baseCellMap.put(10l, "OT Charges");
		 baseCellMap.put(11l, "Professional Fees (Surgeon, Anastheist, Consultation charges etc)");
		 baseCellMap.put(12l, "Investigation & Diagnostics");
		 baseCellMap.put(13l, "Medicines");
		 baseCellMap.put(23l, "Consumables");
		 baseCellMap.put(14l, "Implant/Stunt/Valve/Pacemaker/Etc");
		 baseCellMap.put(15l, "Ambulance Fees");
		 baseCellMap.put(16l, "a) ANH Package");
		 baseCellMap.put(17l, "b) Composite Package");
		 baseCellMap.put(18l, "c) Other Package");
		 baseCellMap.put(19l, "Others");
		 baseCellMap.put(20l, "Taxes and Other Cess");
		 baseCellMap.put(21l, "Discount in Hospital Bill");
		 baseCellMap.put(22l, "ICCU");
		 
		 return baseCellMap;
	}
		 
	public static Map<Long, Integer> getBaseCellSno() {
		
		 Map<Long,Integer> baseCellSno = new HashMap<Long, Integer>();
		 baseCellSno.put(8l,  1);
		 baseCellSno.put(9l,  2);
		 baseCellSno.put(10l, 3);
		 baseCellSno.put(11l, 4);
		 baseCellSno.put(12l, 5);
		 baseCellSno.put(13l, 6);
		 baseCellSno.put(14l, 7);
		 baseCellSno.put(15l, 8);
		 baseCellSno.put(16l, 9);
		 baseCellSno.put(17l, 10);
		 baseCellSno.put(18l, 11);
		 baseCellSno.put(19l, 12);
		 baseCellSno.put(20l, 13);
		 baseCellSno.put(21l, 14);
		 
		 return baseCellSno;
		 
	}
	
	public static Map<Long, Long> getRejectStatusMap() {
		Map<Long, Long> rejectStatusMap = new HashMap<Long, Long>();
		rejectStatusMap.put(93l, 93l);
		rejectStatusMap.put(117l, 117l);
		rejectStatusMap.put(94l, 94l);
		rejectStatusMap.put(95l, 95l);
		rejectStatusMap.put(96l, 96l);
		rejectStatusMap.put(97l, 97l);
		rejectStatusMap.put(118l, 118l);
		rejectStatusMap.put(119l, 119l);
		rejectStatusMap.put(120l, 120l);
		rejectStatusMap.put(121l, 121l);
		return rejectStatusMap;
	}
	
	public static String getPreviousApprovedAmt(List<DiagnosisDetailsTableDTO> diagnonsisDTO, List<ProcedureDTO> procedureDTO, ResidualAmountDTO residualDTO ) {
		Double totalAmt = 0d;
		for (DiagnosisDetailsTableDTO diagDTO : diagnonsisDTO) {
			totalAmt += diagDTO.getAmountConsideredAmount() != null ? diagDTO.getAmountConsideredAmount() : 0d;
		}
		for (ProcedureDTO procedureDTO2 : procedureDTO) {
			totalAmt += procedureDTO2.getAmountConsideredAmount() != null ? procedureDTO2.getAmountConsideredAmount() : 0d;
		}
		totalAmt += residualDTO.getAmountConsideredAmount() != null ? residualDTO.getAmountConsideredAmount() : 0d;
		return String.valueOf(totalAmt.intValue());
	}
	
	
	public static String getPreviousApprovedAmtForAmbulance(List<DiagnosisDetailsTableDTO> diagnonsisDTO, List<ProcedureDTO> procedureDTO) {
		Double totalAmt = 0d;
		for (DiagnosisDetailsTableDTO diagDTO : diagnonsisDTO) {
			totalAmt += diagDTO.getAmbulanceCharge() != null ? diagDTO.getAmbulanceCharge() : 0d;
		}
		for (ProcedureDTO procedureDTO2 : procedureDTO) {
			totalAmt += procedureDTO2.getAmbulanceCharge() != null ? procedureDTO2.getAmbulanceCharge() : 0d;
		}
//		totalAmt += residualDTO.getAmountConsideredAmount() != null ? residualDTO.getAmountConsideredAmount() : 0d;
		
		return String.valueOf(totalAmt.intValue());
	}
	
	public static String getAmountForAmbulance(List<DiagnosisProcedureTableDTO> diagnonsisDTO) {
		Double totalAmt = 0d;
		for (DiagnosisProcedureTableDTO diagDTO : diagnonsisDTO) {
			totalAmt += diagDTO.getAmbulanceCharge() != null ? diagDTO.getAmbulanceCharge() : 0d;
		}
//		totalAmt += residualDTO.getAmountConsideredAmount() != null ? residualDTO.getAmountConsideredAmount() : 0d;
		
		return String.valueOf(totalAmt.intValue());
	}
	
	

	public static String getInputTokenValue(Object objects) {
		Set tokens = (Set) objects;
		Object[] tokens1 = tokens.toArray();
		return "" + tokens1[tokens1.length - 1];
	}

	/**
	 * This method retreives hospital information from VW_HOSPITALS.
	 * 
	 * @param EntityManager
	 *            -- entityManager instance for creating criteriaBuilder
	 *            instance.
	 * @param List
	 *            <Long> -- List of Hospital type id.
	 * 
	 * */
	
	public static Date getFromDate(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String dateInString =  date + "  00:00:00";
		Date resultdate = null;
		try {
			resultdate = sdf.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		System.out.println(date); //Tue Aug 31 10:20:56 SGT 1982
		return resultdate;
	}
	
	public static Date getToDate(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String dateInString = date + "  24:59:59";
		Date resultdate = null;
		
		try {
			resultdate = sdf.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(date); //Tue Aug 31 10:20:56 SGT 1982
		return resultdate;
	}
	
	public static List getHospitalInformation(EntityManager entityManager,
			List<Long> hospitalTypeIdList) {
		List<Hospitals> hospitalInfoList = new ArrayList<Hospitals>();
		if (null != hospitalTypeIdList && 0 != hospitalTypeIdList.size()) {
			final CriteriaBuilder hospitalBuilder = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<Hospitals> hospitalCriteriaQuery = hospitalBuilder
					.createQuery(Hospitals.class);
			Root<Hospitals> searchRootForHospitalInfo = hospitalCriteriaQuery
					.from(Hospitals.class);
			hospitalCriteriaQuery.where(searchRootForHospitalInfo.<Long> get(
					"key").in(hospitalTypeIdList));
			final TypedQuery<Hospitals> hospitalInfoQuery = entityManager
					.createQuery(hospitalCriteriaQuery);
			hospitalInfoList = hospitalInfoQuery.getResultList();
		}
		return hospitalInfoList;

	}
	
	 public static String removeLastChar(String str) {
	        return str.substring(0,str.length()-1);
	    }

	public static List getByPreauthKey(EntityManager entityManager,
			List<Long> preauthKeyList) {
		List<PedValidation> diagnosisList = new ArrayList<PedValidation>();
		if (null != preauthKeyList && 0 != preauthKeyList.size()) {
			final CriteriaBuilder hospitalBuilder = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<PedValidation> hospitalCriteriaQuery = hospitalBuilder
					.createQuery(PedValidation.class);
			Root<PedValidation> searchRootForHospitalInfo = hospitalCriteriaQuery
					.from(PedValidation.class);
			hospitalCriteriaQuery.where(searchRootForHospitalInfo.<Long> get("transactionKey")
					.in(preauthKeyList));
			final TypedQuery<PedValidation> hospitalInfoQuery = entityManager
					.createQuery(hospitalCriteriaQuery);
			diagnosisList = hospitalInfoQuery.getResultList();
		}
		return diagnosisList;
	}

	public static List getByDiagonsisKey(EntityManager entityManager,
			List<Long> preauthKeyList) {
		List<Diagnosis> diagnosisList = new ArrayList<Diagnosis>();
		if (null != preauthKeyList && 0 != preauthKeyList.size()) {
			final CriteriaBuilder hospitalBuilder = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<Diagnosis> hospitalCriteriaQuery = hospitalBuilder
					.createQuery(Diagnosis.class);
			Root<Diagnosis> searchRootForHospitalInfo = hospitalCriteriaQuery
					.from(Diagnosis.class);
			hospitalCriteriaQuery.where(searchRootForHospitalInfo.<Long> get(
					"key").in(preauthKeyList));
			final TypedQuery<Diagnosis> hospitalInfoQuery = entityManager
					.createQuery(hospitalCriteriaQuery);
			diagnosisList = hospitalInfoQuery.getResultList();
		}
		return diagnosisList;
	}

	/**
	 * This method retreives intimation information from IMS_CLS_INTIMATION .
	 * 
	 * @param strScreenName
	 *            -- Name of search screen name.
	 * @param EntityManager
	 *            -- entityManager instance for creating criteriaBuilder
	 *            instance.
	 * @param List
	 *            <Long> -- List of intimation keys .
	 * */
	public static List getIntimationInformation(String strScreenName,
			EntityManager entityManager, List<Long> keyList) {
		List infoList = new ArrayList();

		if (null != keyList && 0 != keyList.size()) {

			if ((SHAConstants.PED_SEARCH_SCREEN).equals(strScreenName)) {
				final CriteriaBuilder IntimationBuilder = entityManager
						.getCriteriaBuilder();
				final CriteriaQuery<OldInitiatePedEndorsement> criteriaQuery = IntimationBuilder
						.createQuery(OldInitiatePedEndorsement.class);
				Root<OldInitiatePedEndorsement> searchRootForIntimation = criteriaQuery
						.from(OldInitiatePedEndorsement.class);
				criteriaQuery.where(searchRootForIntimation.<Long> get("key")
						.in(keyList));
				
				criteriaQuery.orderBy(IntimationBuilder.asc(searchRootForIntimation.get("key")));
				
				final TypedQuery<OldInitiatePedEndorsement> pedInfoQuery = entityManager
						.createQuery(criteriaQuery);

				infoList = pedInfoQuery.getResultList();
			}

			else if ((SHAConstants.PREAUTH_SEARCH_SCREEN).equals(strScreenName)) {
				final CriteriaBuilder IntimationBuilder = entityManager
						.getCriteriaBuilder();
				final CriteriaQuery<Preauth> criteriaQuery = IntimationBuilder
						.createQuery(Preauth.class);
				Root<Preauth> searchRootForIntimation = criteriaQuery
						.from(Preauth.class);
				criteriaQuery.where(searchRootForIntimation.<Long> get("key")
						.in(keyList));

				final TypedQuery<Preauth> intimateInfoQuery = entityManager
						.createQuery(criteriaQuery);

				infoList = intimateInfoQuery.getResultList();
			}

			else if ((SHAConstants.SEARCH_CLAIM).equals(strScreenName)) {

				final CriteriaBuilder IntimationBuilder = entityManager
						.getCriteriaBuilder();
				final CriteriaQuery<Claim> criteriaQuery = IntimationBuilder
						.createQuery(Claim.class);
				Root<Claim> searchRootForIntimation = criteriaQuery
						.from(Claim.class);
				criteriaQuery.where(searchRootForIntimation.<Long> get("key")
						.in(keyList));

				final TypedQuery<Claim> claimInfoQuery = entityManager
						.createQuery(criteriaQuery);
				infoList = claimInfoQuery.getResultList();
			}
			/* For intimation */
			else if ((SHAConstants.INTIMATION_SEARCH).equals(strScreenName)) {

				final CriteriaBuilder IntimationBuilder = entityManager
						.getCriteriaBuilder();
				final CriteriaQuery<Intimation> criteriaQuery = IntimationBuilder
						.createQuery(Intimation.class);
				Root<Intimation> searchRootForIntimation = criteriaQuery
						.from(Intimation.class);
				criteriaQuery.where(searchRootForIntimation.<Long> get("key")
						.in(keyList));

				final TypedQuery<Intimation> claimInfoQuery = entityManager
						.createQuery(criteriaQuery);
				infoList = claimInfoQuery.getResultList();
			}
		}
		return infoList;

	}

	/**
	 * This method retreives intimation information from IMS_CLS_PED_VALIDATION
	 * .
	 * 
	 * @param strScreenName
	 *            -- Name of search screen name.
	 * @param EntityManager
	 *            -- entityManager instance for creating criteriaBuilder
	 *            instance.
	 * @param List
	 *            <Long> -- List of intimation keys .
	 * 
	 *            For time being , only with draw pre auth and downsize pre auth
	 *            is using diagonis information. But some other screen also
	 *            requires the same, then they have to form a separate else if
	 *            block with screen name which would come from service. This is
	 *            because the JPA class involved will vary from screen to
	 *            screen.
	 * */
	public static Map getDiagnosisInformation(String strScreenName,
			EntityManager entityManager, List<Long> preAuthList) {
		List pedValidationList = new ArrayList();
		List diagnosisList = new ArrayList();
		Map finalDiagRemarksMap = new WeakHashMap();
		BidiMap diagnosisMap = new DualHashBidiMap();

		if ((SHAConstants.CASHLESS_PREAUTH_WITHDRAW).equals(strScreenName)) {
			final CriteriaBuilder pedValidationBuilder = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<PedValidation> criteriaQuery = pedValidationBuilder
					.createQuery(PedValidation.class);
			Root<PedValidation> searchRootForPedValidation = criteriaQuery
					.from(PedValidation.class);

			criteriaQuery.where(searchRootForPedValidation.<Preauth> get(
					"preauth").in(preAuthList));

			final TypedQuery<PedValidation> pedValInfoQuery = entityManager
					.createQuery(criteriaQuery);

			pedValidationList = pedValInfoQuery.getResultList();

			for (Long preAuthKey : preAuthList) {
				List<Long> diagnosisIdList = new ArrayList();
				for (Object searchWithDrawCashless : pedValidationList) {
					PedValidation objPedVal = (PedValidation) searchWithDrawCashless;
					if (preAuthKey == objPedVal.getTransactionKey()) {
						Long lDiagnosisId = objPedVal.getDiagnosisId();
						diagnosisIdList.add(lDiagnosisId);
					}
				}
				diagnosisMap.put(preAuthKey, diagnosisIdList);
			}

			// Code for fetching diagnosis information with diagnosis id list.
			final CriteriaBuilder diagonsisBuilder = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<Diagnosis> criteriaDiagQuery = diagonsisBuilder
					.createQuery(Diagnosis.class);
			Root<Diagnosis> searchRootForDiag = criteriaDiagQuery
					.from(Diagnosis.class);
			for (int i = 0; i < preAuthList.size(); i++) {
				List diagIdList = (List) diagnosisMap.get(preAuthList.get(i));

				if (null != diagIdList && 0 != diagIdList.size()) {
					criteriaDiagQuery.where(searchRootForDiag.<Long> get("key")
							.in(diagIdList));
					final TypedQuery<Diagnosis> diagnosisInfoQuery = entityManager
							.createQuery(criteriaDiagQuery);

					diagnosisList = diagnosisInfoQuery.getResultList();
					StringBuilder strBuilder = new StringBuilder();
					for (Object objDiag : diagnosisList) {
						Diagnosis diagnosis = (Diagnosis) objDiag;
						strBuilder = strBuilder.append(diagnosis.getValue())
								.append(",");
					}

					Long lPreAuthKey = (Long) diagnosisMap.getKey(diagIdList);
					finalDiagRemarksMap.put(lPreAuthKey, strBuilder.toString());
				}
			}
		}
		return finalDiagRemarksMap;
	}
	
	
	

	public static long getDaysBetweenDate(Date star, Date end) {
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		
		if(star != null){
			calendar1.setTime(star);
		}
		if(end != null){
			calendar2.setTime(end);
		}
		long milliseconds1 = calendar1.getTimeInMillis();
		long milliseconds2 = calendar2.getTimeInMillis();
		long diff = milliseconds2 - milliseconds1;
		long diffSeconds = diff / 1000;
		long diffMinutes = diff / (60 * 1000);
		long diffHours = diff / (60 * 60 * 1000);
		long diffDays = diff / (24 * 60 * 60 * 1000);

		return diffDays;

	}

	/*public static UnsignedInteger getDaysDifference(Date star, Date end) {
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		
		if(star != null){
			calendar1.setTime(star);
		}
		if(end != null){
			calendar2.setTime(end);
		}
		Long milliseconds1 = calendar1.getTimeInMillis();
		Long milliseconds2 = calendar2.getTimeInMillis();
		UnsignedInteger diff = milliseconds2.intValue()- milliseconds1.intValue();
		//long diffSeconds = diff / 1000;
	//	long diffMinutes = diff / (60 * 1000);
		//long diffHours = diff / (60 * 60 * 1000);
		long UnsignedInteger = diff / (24 * 60 * 60 * 1000);

		return diffDays;

	}*/
	public static final boolean isValidFloat(String strNum) {
		try {
			if (strNum != null && !strNum.equals("") && !strNum.equals("NaN") && !strNum.equals("NA")) {
				Float.parseFloat(strNum);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}
	
	
	public static final boolean isValidInteger(String strNum) {
		try {
			if (strNum != null && !strNum.equals("") && !strNum.equals("NaN") && !strNum.equals("NA")) {
				String replaceAll = strNum.replaceAll(",", "");
				if(strNum.contains("."))
				{
					String replacedStr = replaceAll.replaceAll(".", "");
					Integer.valueOf(replacedStr);
				}
				else
				{
				Integer.valueOf(replaceAll);
				}
				//Integer.valueOf(replacedStr);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
//			e.printStackTrace();
			return false;
		}

	}
	
	public static final boolean isValidLong(String strNum) {
		try {
			if (strNum != null && !strNum.equals("") && !strNum.equals("NaN") && !strNum.equals("NA")) {
				Long.valueOf(strNum);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}

	public static final boolean isValidDouble(String strNum) {
		try {
			if (strNum != null && !strNum.equals("") && !strNum.equals("NaN") && !strNum.equals("NA")) {
				Double.valueOf(strNum);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
//			e.printStackTrace();
			return false;
		}

	}

	public static final Integer getIntegerFromString(String strNum) {
		if (isValidInteger(strNum)) {
			return new Float(strNum).intValue();
		} else {
			return new Float("0.0").intValue();
		}
	}
	
	public static final Integer getFloatFromString(String strNum) {
		if (isValidFloat(strNum)) {
			return new Float(strNum).intValue();
		} else {
			return new Float("0.0").intValue();
		}
	}
	
	
	public static final Float convertFloatToString(String strNum) {
		if (isValidFloat(strNum)) {
			return new Float(strNum);
		} else {
			return new Float("0.0");
		}
	}
	
	public static final Integer getIntegerFromStringWithComma(String strNum){
		String number = (strNum != null && strNum.length() > 0) ? strNum.replaceAll(",","") : "0";
		Integer num = 0;
		if(isValidInteger(number)) {
			 num = Integer.parseInt(number);
			 
		}
		return num;
	}

	public static final Integer getDoubleFromString(String strNum) {
		if (isValidDouble(strNum)) {
			return new Double(strNum).intValue();
		} else {
			return new Double("0.0").intValue();
		}
	}
	
	public static final Double getDoubleFromStringWithComma(String strNum){
		String number = (strNum != null && strNum.length() > 0) ? strNum.replaceAll(",","") : "0";
		Double num = 0d;
		if(isValidDouble(number)) {
			 num = new Double(number);
			 
		}
		return num;
	}
	
	public static final Long getLongFromString(String strNum) {
		
		if(isValidLong(strNum)){
			try{
				return new Long(strNum).longValue();
			}
			catch(Exception e){
	//			e.printStackTrace();
			}
		}
		return 0l;
		
	}
	
	public static final Double getDoubleValueFromString(String strNum) {
		if (isValidDouble(strNum)) {
			return new Double(strNum);
		} else {
			return new Double("0.0");
		}
	}
	
	public static String parseDate(Date datechange) {
		String formattedDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATEFORMAT);
			formattedDate = sdf.format(datechange);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return formattedDate;
	}
	
	public static String parseDate(Date datechange,String DATE_WITHOUT_TIME_FORMAT) {
		
		String formattedDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_WITHOUT_TIME_FORMAT);
			formattedDate = sdf.format(datechange);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return formattedDate;
	}
	

	/**
	 * Method to retreive the previous claims alone.
	 * */
//	public static List<PreviousClaimsTableDTO> getPreviousClaims(
//			List<Claim> claimList, String strClaimNumber, PEDValidationService pedValidationService, MasterService masterService) {
//		List<Claim> objClaimList = new ArrayList<Claim>();
//		List<PreviousClaimsTableDTO> previousClaimList;
//		
//		for (Claim objClaim : claimList) {
//			if (!(strClaimNumber.equalsIgnoreCase(objClaim.getClaimId()))) {
//				objClaimList.add(objClaim);
//			}
//		}
//		previousClaimList = new PreviousClaimMapper()
//				.getPreviousClaimDTOList(objClaimList);
//		
//		for (PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimList) {
//			
//			
//		}
//		try{
//			if (objClaimList != null && previousClaimList != null && !objClaimList.isEmpty() && !previousClaimList.isEmpty()) {
//				for (int i = 0; i < objClaimList.size(); i++) {
//					if(previousClaimList.get(i).getAdmissionDate() != null){
//                         previousClaimList.get(i).setAdmissionDate(formatDate(objClaimList.get(i).getIntimation().getAdmissionDate()));
//					}
//				}
//		}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		
//		
//		if(!previousClaimList.isEmpty()) {
//			for (PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimList) {
//				String diagnosisStr = " ";
//				List<PedValidation> pedValidationsList = pedValidationService.getDiagnosis(previousClaimsTableDTO.getIntimationKey());
//			    List<Long> keyList = new ArrayList<Long>();
//				if (!pedValidationsList.isEmpty()) {
//					for (PedValidation pedValidation : pedValidationsList) {
//						
////						map.put(pedValidation.getDiagnosisId(), masterService.getDiagnosis(pedValidation
////										.getDiagnosisId()));
//						if(! keyList.contains(pedValidation.getDiagnosisId())){
//						diagnosisStr = (diagnosisStr == " " ? ""
//								: diagnosisStr + ", ")
//								+ masterService.getDiagnosis(pedValidation
//										.getDiagnosisId());
//						keyList.add(pedValidation.getDiagnosisId());
//						}
//						
//					}
//					
//				}
//				previousClaimsTableDTO.setDiagnosis(diagnosisStr);
//			}
//			
//		}
//		
//		return previousClaimList;
//	}
	
	
	
	public Long getClaimedAmountForROD(EntityManager entityManager,Long claimKey){
		
		Double claimedAmount = 0d;
		
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey").setParameter(
						"claimKey", claimKey);
		List<Reimbursement> rodList = query.getResultList();
		
		for (Reimbursement reimbursement : rodList) {
			if(reimbursement.getDocAcknowLedgement() != null){
				DocAcknowledgement docAcknowledgement = reimbursement.getDocAcknowLedgement();
				
					claimedAmount += docAcknowledgement.getHospitalizationClaimedAmount() != null ? docAcknowledgement.getHospitalizationClaimedAmount() : 0d;
					claimedAmount += docAcknowledgement.getPreHospitalizationClaimedAmount() != null ? docAcknowledgement.getPreHospitalizationClaimedAmount() : 0d;
					claimedAmount += docAcknowledgement.getPostHospitalizationClaimedAmount() != null ? docAcknowledgement.getPostHospitalizationClaimedAmount() : 0d;
			}
		}
		
		Long amount =claimedAmount.longValue();
		
		return amount;
		
	}
	
	
	

	/**
	 * Based on the status id , the correponding type value will be returned.
	 * This is used in pre medical pre auth. pre auth, premedical enhancement,
	 * pre auth enhancement screens.
	 * */

	public static String getTypeBasedOnStatusId(Long statusId) {
		return statusMap.get(statusId);

	}

	/**
	 * The below method validates the date which is entered by the user in the
	 * date fields.
	 * */


	public static String getIndianFormattedNumber(Number value) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale(
				"en", "IN"));
		return formatter.format(value);
	}


	public static Date formatTimestamp(String date){
		Date strDate = null;
		if (date != null) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.S");
		try {
			strDate = dateFormat.parse(date);
		} catch (ParseException e) {		
			e.printStackTrace();
			return null;
		}
		}
		return strDate;
	}
	
	public static String formatDateForSearch(String date){
		Date strDate = null;
		if (date != null) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"E MMM dd hh:mm:ss zzz yyyy");
		SimpleDateFormat correctFormat = new SimpleDateFormat(
				"dd/MM/yyyy");
		try {
			strDate = dateFormat.parse(date);
			String format = correctFormat.format(strDate);
			return format;
		} catch (ParseException e) {		
 			e.printStackTrace();
			return null;
		}
			
		}
		return null;
	}
	
	
	
	public static Date formatDateWithoutTime(String date){
		Date strDate = null;
		if (date != null) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd-MM-yyyy");
		try {
			strDate = dateFormat.parse(date);
		} catch (ParseException e) {		
			e.printStackTrace();
			return null;
		}
		}
		return strDate;
	}
	
	public static Date formatTime(String date){
		Date date1 = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
				date1 = formatter.parse(date);
				System.out.println(date);
				
		 
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		
		return date1;
	}
	
	
	public static Date formatIntimationDate(String date)
	{
		Date date1 = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		try {
			
				date1 = formatter.parse(date);
				System.out.println(date);
				
		 
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		
		return date1;
	
	}
	
	public static String formatIntimationDateForEdit(Date date)
	{
		String date1 = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			
				date1 = formatter.format(date);
				Date d = formatter.parse(date1);
				System.out.println(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return date1;
	}
	
	
	public static String formatIntimationDateValue(Date date)
	{
		
		String strDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		try {
			
			strDate = formatter.format(date);
			
			//strDate = formatter.format(new Date(DateFormat.getInstance().format(date)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		return strDate;
	}
	
	public static Date formatTimeFromString(String date){
		Date date1 = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
				date1 = formatter.parse(date);
				System.out.println(date);
				
		 
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		
		return date1;
		
		
	}

	public static String getAge(Date dateofBirth) {
		Calendar dob = Calendar.getInstance();
		String insuredAge = "0";
		if (dateofBirth != null) {
			dob.setTime(dateofBirth);
			Calendar today = Calendar.getInstance();
			int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
			insuredAge = String.valueOf(age);
		}
		return insuredAge;
	}
	
	public static String getAge(Date dateofBirth, Date admissionDate) {
		Calendar dob = Calendar.getInstance();
		String insuredAge = "0";
		if (dateofBirth != null) {
			dob.setTime(dateofBirth);
			Calendar today = Calendar.getInstance();
			today.setTime(admissionDate);
			int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
			insuredAge = String.valueOf(age);
		}
		return insuredAge;
	}
	
	public static Date formatPremiaDate(String dateStr) {
		 SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
   	  try {
   		  	Date parseDate = formatter.parse(dateStr);
   		  	return parseDate;
//			return new SimpleDateFormat("dd/MM/yyyy").format(parseDate);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
   	  return null;
	}
	
	public static Date formatDateForStarfax(String dateStr)
	{
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	   	  try {
	   		  	Date parseDate = formatter.parse(dateStr);
	   		  	return parseDate;
//				return new SimpleDateFormat("dd/MM/yyyy").format(parseDate);
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
	   	  return null;
	}
	
	
	public static String formatDateAndTime(String dateStr){
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		if(dateStr != null){
	   	  try {
	   		  	Date parseDate = formatter.parse(dateStr);
//	   		  	return parseDate;
				return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(parseDate);
	   	
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
		}
	   	  return null;
		
		
	}
	
public static String getDateWithoutTime(Date date){
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		if(date != null){
	   	 
	   		  //	Date parseDate = formatter.parse(dateStr);
//	   		  	return parseDate;
				return new SimpleDateFormat("dd/MM/yyyy").format(date);	   	
			
		}
	   	  return null;
		
		
	}
	
	public static String formateDateForHistory(Date createdDate){
		
		if(createdDate != null){
			return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(createdDate);
			}
		   	  return null;
	}
	
	public static String formateDateForOMPHistory(Date createdDate){
		
		if(createdDate != null){
			return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(createdDate);
			}
		   	  return null;
	}
	
	
	public static String formatDateToFindDifference(Date createDate)
	{
		if(createDate != null){
			return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(createDate);
		}
		   	  return null;
	}

	
	public static String getTimeFromDate(Date createDate){
		
		if(createDate != null){
			return new SimpleDateFormat("HH:mm:ss").format(createDate);
		}
		   	  return null;
	}
	
	
	public static String formatPremiaDateAsString(Date dateStr) {
		 SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
  	  try {
  		  	String parseDate = formatter.format(dateStr);
  		  	return parseDate;
//			return new SimpleDateFormat("dd/MM/yyyy").format(parseDate);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
  	  return null;
	}
	
	public static String formatDateForBatchNo(Date dateStr)
	{
		 SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	  	  try {
	  		  	String parseDate = formatter.format(dateStr);
	  		  	return parseDate;
//				return new SimpleDateFormat("dd/MM/yyyy").format(parseDate);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	  	  return null;
	}
	
	
	/***
	 * @author yosuva.a
	 * @param birthDate
	 * @return
	 */
	
	 public static Integer calculateAge(Date birthDate){
		 try{
		      int years = 0;
		      int months = 0;
		      int days = 0;
		      //create calendar object for birth day
		      Calendar birthDay = Calendar.getInstance();
		      birthDay.setTimeInMillis(birthDate.getTime());
		      //create calendar object for current day
		      long currentTime = System.currentTimeMillis();
		      Calendar now = Calendar.getInstance();
		      now.setTimeInMillis(currentTime);
		      //Get difference between years
		      years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
		      int currMonth = now.get(Calendar.MONTH) + 1;
		      int birthMonth = birthDay.get(Calendar.MONTH) + 1;
		      //Get difference between months
		      months = currMonth - birthMonth;
		      //if month difference is in negative then reduce years by one and calculate the number of months.
		      if (months < 0)
		      {
		         years--;
		         months = 12 - birthMonth + currMonth;
		         if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
		            months--;
		      } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
		      {
		         years--;
		         months = 11;
		      }
		      //Calculate the days
		      if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
		         days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
		      else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
		      {
		         int today = now.get(Calendar.DAY_OF_MONTH);
		         now.add(Calendar.MONTH, -1);
		         days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
		      } else
		      {
		         days = 0;
		         if (months == 12)
		         {
		            years++;
		            months = 0;
		         }
		      }
		      return years;
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return 0;
	   }
	 
	   public static String getRodAgeing(Date fromDate){
		   
			 try{
			      int years = 0;
			      int months = 0;
			      int days = 0;
			      int hours = 0;
			      
			      Date currentDate = new Date();
			     
			      
			      String rodAgeing = "";
			      //create calendar object for birth day
			      Calendar birthDay = Calendar.getInstance();
			      birthDay.setTimeInMillis(fromDate.getTime());
			      //create calendar object for current day
			      long currentTime = System.currentTimeMillis();
			      
			      
			     
			      Calendar now = Calendar.getInstance();
			      now.setTimeInMillis(currentTime);
			      //Get difference between years
			      years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
			      int currMonth = now.get(Calendar.MONTH) + 1;
			      int birthMonth = birthDay.get(Calendar.MONTH) + 1;
			      //Get difference between months
			      months = currMonth - birthMonth;
			      //if month difference is in negative then reduce years by one and calculate the number of months.
			      if (months < 0)
			      {
			         years--;
			         months = 12 - birthMonth + currMonth;
			         if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
			            months--;
			      } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
			      {
			         years--;
			         months = 11;
			      }
			      //Calculate the days
			      if (now.get(Calendar.DATE) >= birthDay.get(Calendar.DATE)){
			    	  days =  (int) ((currentDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
			      	  hours = fromDate.getHours();
			          hours -= 9;
			      }
			         
			      else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
			      {
			         int today = now.get(Calendar.DAY_OF_MONTH);
			         now.add(Calendar.MONTH, -1);
			         days =  (int) ((currentDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
//			         hours = now.get(Calendar.HOUR_OF_DAY);
			         hours = fromDate.getHours();
			         hours -= 9;
			      } else
			      {
			         days = 0;
			         if (months == 12)
			         {
			            years++;
			            months = 0;
			         }
			      }
			      
			      if(months >0){
//			    	  rodAgeing += months+" Months ";
			      }
			      if(days > 1){
			    	  rodAgeing += days+" Days ";
			      }
			      
//			      if(hours> 0){
//			    	  rodAgeing += hours+" Hours";
//			      }
			      
			      if(days <= 1){
			    	  rodAgeing = "1 Day";
			      }
			      
			      return rodAgeing;
			 }catch(Exception e){
				 e.printStackTrace();
			 }
			 return "";
		
	   }
	
	   
		/***
		 * @author 
		 * @param birthDate
		 * @return
		 * 
		 * This method will calculate insured age , based on policy start date
		 * and date of birth , which are retreived from premia.
		 * 
		 * This is used only for insured age calculation.
		 * 
		 */
		
		 public static Integer calculateInsuredAge(Date policyStartDate , Date birthDate){
			 try{
			      int years = 0;
			      int months = 0;
			      int days = 0;
			      //create calendar object for birth day
			      Calendar birthDay = Calendar.getInstance();
			      birthDay.setTimeInMillis(birthDate.getTime());
			      //create calendar object for current day
			      long currentTime = policyStartDate.getTime();
			      //long currentTime = System.currentTimeMillis();
			      Calendar now = Calendar.getInstance();
			      now.setTimeInMillis(currentTime);
			      //Get difference between years
			      years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
			      int currMonth = now.get(Calendar.MONTH) + 1;
			      int birthMonth = birthDay.get(Calendar.MONTH) + 1;
			      //Get difference between months
			      
			      /**
			       * This needs to be checked.
			       * */
			      if(/*currMonth > birthMonth || */currMonth < birthMonth)
			      {
			    	  years--;
			      }
			      else if (birthMonth == currMonth)
			      {
			    	  int day1 = now.get(Calendar.DAY_OF_MONTH);
				       int day2 = birthDay.get(Calendar.DAY_OF_MONTH);
				       if (day1 < day2) {
				    	   years--;
					        }
			      }
			     
			   //   months = currMonth - birthMonth;
			      
			    
			      
			
			    	  
			    		  
			      
			      //if month difference is in negative then reduce years by one and calculate the number of months.
			      /*if (months < 0)
			      {
			        // years--;
			         months = 12 - birthMonth + currMonth;
			         if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
			            months--;
			      } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
			      {
			         //years--;
			         months = 11;
			      }
			      else if(months > 0)
			      {
			    	  years--;
			      }*/
			      //Calculate the days
			     /* if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
			         days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
			      else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
			      {
			         int today = now.get(Calendar.DAY_OF_MONTH);
			         now.add(Calendar.MONTH, -1);
			         days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
			      } else
			      {
			         days = 0;
			         if (months == 12)
			         {
			            years++;
			            months = 0;
			         }
			      }*/
			      return years;
			 }catch(Exception e){
				 e.printStackTrace();
			 }
			 return 0;
		   }
	 
	 
	/**
	 * Method which will validate date of death.
	 * This date should be greater than or equal to 
	 * admission date and should be less than or equal
	 * to current date.
	 * 
	 * */
	
	 public static int getDiffYears(Date first, Date last) {
		    Calendar a = getCalendar(first);
		    Calendar b = getCalendar(last);
		    int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
		    if (a.get(Calendar.DAY_OF_YEAR) > b.get(Calendar.DAY_OF_YEAR)) {
		        diff--;
		    }
		    return diff;
	}
	 
	 public static Calendar getCalendar(Date date) {
		    Calendar cal = Calendar.getInstance(Locale.US);
		    cal.setTime(date);
		    return cal;
	}
	 
	public static void resetComponent(AbstractLayout mainLayout) {
		if(mainLayout != null) {
			Iterator<Component> componentIterator = mainLayout.iterator();
			while (componentIterator.hasNext()) {
				Component searchScrnComponent = componentIterator.next();
				if(searchScrnComponent instanceof ComboBox){
					continue;
				}
				if (searchScrnComponent instanceof HorizontalLayout) {
					HorizontalLayout horizontalLayout = (HorizontalLayout) searchScrnComponent;
					Iterator<Component> searchScrnCompIter = horizontalLayout
							.iterator();
					while (searchScrnCompIter.hasNext()) {
						Component verticalLayoutComp = searchScrnCompIter.next();
						if (verticalLayoutComp instanceof Button) {
							continue;
						}
						if (verticalLayoutComp instanceof Label) {
							continue;
						}
						if (verticalLayoutComp instanceof TextField) {
							TextField field = (TextField) verticalLayoutComp;
							field.setValue("");
							continue;
						}
						if (verticalLayoutComp instanceof AutocompleteField) {
							AutocompleteField<?> field = (AutocompleteField<?>) verticalLayoutComp;
							field.setValue("");
							continue;
						}
						FormLayout fLayout = (FormLayout) verticalLayoutComp;
						Iterator<Component> formComIter = fLayout.iterator();

						while (formComIter.hasNext()) {
							Component indivdualComp = formComIter.next();
							if (indivdualComp != null) {
								if (indivdualComp instanceof Label) {
									continue;
								}

								if (indivdualComp instanceof TextField) {
									TextField field = (TextField) indivdualComp;
									field.setValue("");
								}
								if(indivdualComp instanceof ComboBox){
									ComboBox field = (ComboBox) indivdualComp;
									String id = field.getId();
									if(id != null && (id.equalsIgnoreCase("notAllowedReset"))){
										continue;
									}else{
										field.setValue(null);
									}
									
								}
								if(indivdualComp instanceof DateField){
									DateField field = (DateField) indivdualComp;
									field.setValue(null);
								}
								if (verticalLayoutComp instanceof AutocompleteField) {
									AutocompleteField<?> field = (AutocompleteField<?>) verticalLayoutComp;
									field.setValue("");
									continue;
								}
							}
						}
					}
				}
				else 	if (searchScrnComponent instanceof AbsoluteLayout) {
					AbsoluteLayout horizontalLayout = (AbsoluteLayout) searchScrnComponent;
					Iterator<Component> searchScrnCompIter = horizontalLayout
							.iterator();
					while (searchScrnCompIter.hasNext()) {
						Component verticalLayoutComp = searchScrnCompIter.next();
						if (verticalLayoutComp instanceof Button) {
							continue;
						}
						if (verticalLayoutComp instanceof Label) {
							continue;
						}
						if (verticalLayoutComp instanceof TextField) {
							TextField field = (TextField) verticalLayoutComp;
							field.setValue("");
							continue;
						}
						if(verticalLayoutComp instanceof ComboBox){
							ComboBox field = (ComboBox) verticalLayoutComp;
							String id = field.getId();
							if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
								continue;
							}else{
								field.setValue(null);
							}
						}
						
						if(verticalLayoutComp instanceof HorizontalLayout)
						{
						HorizontalLayout hLayout = (HorizontalLayout) verticalLayoutComp;
						Iterator<Component> hLayoutComp = hLayout.iterator();
						while (hLayoutComp.hasNext()) {
							Component indivdualComp = hLayoutComp.next();
							if (indivdualComp != null) {
								if (indivdualComp instanceof Label) {
									continue;
								}

								if (indivdualComp instanceof TextField) {
									TextField field = (TextField) indivdualComp;
									field.setValue("");
								}
								if(indivdualComp instanceof ComboBox){
									ComboBox field = (ComboBox) indivdualComp;
									String id = field.getId();
									if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
										continue;
									}else{
										field.setValue(null);
									}
								}
								if(indivdualComp instanceof DateField){
									DateField field = (DateField) indivdualComp;
									field.setValue(null);
								}
								if(indivdualComp instanceof CheckBox)
								{
									CheckBox field = (CheckBox) indivdualComp;
									field.setValue(null);
								}
								
								if(indivdualComp instanceof HorizontalLayout)
								{
									HorizontalLayout hLayout2 = (HorizontalLayout)indivdualComp;
									Iterator<Component> horizontalCompIter = hLayout2.iterator();
									while (horizontalCompIter.hasNext()) {
										Component indivdualComp1 = horizontalCompIter.next();
										if (indivdualComp1 != null) {
											if (indivdualComp1 instanceof Label) {
												continue;
											}

											if (indivdualComp1 instanceof TextField) {
												TextField field = (TextField) indivdualComp1;
												field.setValue("");
											}
											if(indivdualComp1 instanceof ComboBox){
												ComboBox field = (ComboBox) indivdualComp1;
												String id = field.getId();
												if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
													continue;
												}else{
													field.setValue(null);
												}
											}
											if(indivdualComp1 instanceof DateField){
												DateField field = (DateField) indivdualComp1;
												field.setValue(null);
											}
											if(indivdualComp1 instanceof CheckBox)
											{
												CheckBox field = (CheckBox) indivdualComp1;
												field.setValue(null);
											}
											if(indivdualComp1 instanceof FormLayout)
											{
												FormLayout fLayout = (FormLayout) indivdualComp;
												Iterator<Component> formComIter = fLayout.iterator();	
												while (formComIter.hasNext()) {
													Component indivdualComp11 = formComIter.next();
													if (indivdualComp1 != null) {
														if (indivdualComp11 instanceof Label) {
															continue;
														}

														if (indivdualComp11 instanceof TextField) {
															TextField field = (TextField) indivdualComp11;
															field.setValue("");
														}
														if(indivdualComp11 instanceof ComboBox){
															ComboBox field = (ComboBox) indivdualComp11;
															String id = field.getId();
															if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
																continue;
															}else{
																field.setValue(null);
															}
														}
														if(indivdualComp11 instanceof DateField){
															DateField field = (DateField) indivdualComp11;
															field.setValue(null);
														}
														if(indivdualComp1 instanceof CheckBox)
														{
															CheckBox field = (CheckBox) indivdualComp1;
															field.setValue(null);
														}
														if(indivdualComp instanceof HorizontalLayout)
														{
															HorizontalLayout hLayout3 = (HorizontalLayout)indivdualComp;
															Iterator<Component> horizontalCompIter1 = hLayout2.iterator();
															while (horizontalCompIter1.hasNext()) {
																Component indivdualComp2 = horizontalCompIter1.next();
																if (indivdualComp2 != null) {
																	if (indivdualComp2 instanceof Label) {
																		continue;
																	}

																	if (indivdualComp2 instanceof TextField) {
																		TextField field = (TextField) indivdualComp2;
																		field.setValue("");
																	}
																	if(indivdualComp2 instanceof ComboBox){
																		ComboBox field = (ComboBox) indivdualComp2;
																		String id = field.getId();
																		if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
																			continue;
																		}else{
																			field.setValue(null);
																		}
																	}
																	if(indivdualComp2 instanceof DateField){
																		DateField field = (DateField) indivdualComp2;
																		field.setValue(null);
																	}
																	if(indivdualComp2 instanceof CheckBox)
																	{
																		CheckBox field = (CheckBox) indivdualComp2;
																		field.setValue(null);
																	}
													}
												}
											}
														else if(indivdualComp instanceof VerticalLayout)
														{
															VerticalLayout hLayout3 = (VerticalLayout)indivdualComp;
															Iterator<Component> horizontalCompIter1 = hLayout3.iterator();
															while (horizontalCompIter1.hasNext()) {
																Component indivdualComp2 = horizontalCompIter1.next();
																if (indivdualComp2 != null) {
																	if (indivdualComp2 instanceof Label) {
																		continue;
																	}

																	if (indivdualComp2 instanceof TextField) {
																		TextField field = (TextField) indivdualComp2;
																		field.setValue("");
																	}
																	if(indivdualComp2 instanceof ComboBox){
																		ComboBox field = (ComboBox) indivdualComp2;
																		String id = field.getId();
																		if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
																			continue;
																		}else{
																			field.setValue(null);
																		}
																	}
																	if(indivdualComp2 instanceof DateField){
																		DateField field = (DateField) indivdualComp2;
																		field.setValue(null);
																	}
																	if(indivdualComp2 instanceof CheckBox)
																	{
																		CheckBox field = (CheckBox) indivdualComp2;
																		field.setValue(null);
																	}
													}
												}
											}
										}
									}
									
								}
							}
								if(indivdualComp instanceof FormLayout)
								{
									FormLayout fLayout = (FormLayout) indivdualComp;
									Iterator<Component> formComIter = fLayout.iterator();	
									while (formComIter.hasNext()) {
										Component indivdualComp11 = formComIter.next();
										if (indivdualComp11 != null) {
											if (indivdualComp11 instanceof Label) {
												continue;
											}

											if (indivdualComp11 instanceof TextField) {
												TextField field = (TextField) indivdualComp11;
												field.setValue("");
											}
											if(indivdualComp11 instanceof ComboBox){
												ComboBox field = (ComboBox) indivdualComp11;
												String id = field.getId();
												if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
													continue;
												}else{
													field.setValue(null);
												}
											}
											if(indivdualComp11 instanceof DateField){
												DateField field = (DateField) indivdualComp11;
												field.setValue(null);
											}
											if(indivdualComp11 instanceof CheckBox)
											{
												CheckBox field = (CheckBox) indivdualComp11;
												field.setValue(null);
											}
										}
									}
								}
									
								
					}
							
								
								}
								
								if(indivdualComp instanceof FormLayout)
								{
									FormLayout fLayout = (FormLayout) indivdualComp;
									Iterator<Component> formComIter = fLayout.iterator();	
									while (formComIter.hasNext()) {
										Component indivdualComp11 = formComIter.next();
										if (indivdualComp11 != null) {
											if (indivdualComp11 instanceof Label) {
												continue;
											}

											if (indivdualComp11 instanceof TextField) {
												TextField field = (TextField) indivdualComp11;
												field.setValue("");
											}
											if(indivdualComp11 instanceof ComboBox){
												ComboBox field = (ComboBox) indivdualComp11;
												String id = field.getId();
												if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
													continue;
												}else{
													field.setValue(null);
												}
											}
											if(indivdualComp11 instanceof DateField){
												DateField field = (DateField) indivdualComp11;
												field.setValue(null);
											}
											if(indivdualComp11 instanceof CheckBox)
											{
												CheckBox field = (CheckBox) indivdualComp11;
												field.setValue(null);
											}											
											if (indivdualComp11 instanceof AutocompleteField) {
												AutocompleteField<?> field = (AutocompleteField<?>) indivdualComp11;
												field.setValue(null);
												field.setText(null);
												continue;
											}
										}
									}
								}
					}}
						}
						else if(verticalLayoutComp instanceof VerticalLayout)
						{

							VerticalLayout hLayout = (VerticalLayout) verticalLayoutComp;
							Iterator<Component> hLayoutComp = hLayout.iterator();
							while (hLayoutComp.hasNext()) {
								Component indivdualComp = hLayoutComp.next();
								if (indivdualComp != null) {
									if (indivdualComp instanceof Label) {
										continue;
									}

									if (indivdualComp instanceof TextField) {
										TextField field = (TextField) indivdualComp;
										field.setValue("");
									}
									if(indivdualComp instanceof ComboBox){
										ComboBox field = (ComboBox) indivdualComp;
										String id = field.getId();
										if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
											continue;
										}else{
											field.setValue(null);
										}
									}
									if(indivdualComp instanceof DateField){
										DateField field = (DateField) indivdualComp;
										field.setValue(null);
									}
									if(indivdualComp instanceof CheckBox)
									{
										CheckBox field = (CheckBox) indivdualComp;
										field.setValue(null);
									}
									
									if(indivdualComp instanceof HorizontalLayout)
									{
										HorizontalLayout hLayout2 = (HorizontalLayout)indivdualComp;
										Iterator<Component> horizontalCompIter = hLayout2.iterator();
										while (horizontalCompIter.hasNext()) {
											Component indivdualComp1 = horizontalCompIter.next();
											if (indivdualComp1 != null) {
												if (indivdualComp1 instanceof Label) {
													continue;
												}

												if (indivdualComp1 instanceof TextField) {
													TextField field = (TextField) indivdualComp1;
													field.setValue("");
												}
												if(indivdualComp1 instanceof ComboBox){
													ComboBox field = (ComboBox) indivdualComp1;
													String id = field.getId();
													if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
														continue;
													}else{
														field.setValue(null);
													}
												}
												if(indivdualComp1 instanceof DateField){
													DateField field = (DateField) indivdualComp1;
													field.setValue(null);
												}
												if(indivdualComp1 instanceof CheckBox)
												{
													CheckBox field = (CheckBox) indivdualComp1;
													field.setValue(null);
												}
												if(indivdualComp1 instanceof FormLayout)
												{
													FormLayout fLayout = (FormLayout) indivdualComp1;
													Iterator<Component> formComIter = fLayout.iterator();	
													while (formComIter.hasNext()) {
														Component indivdualComp11 = formComIter.next();
														if (indivdualComp1 != null) {
															if (indivdualComp11 instanceof Label) {
																continue;
															}

															if (indivdualComp11 instanceof TextField) {
																TextField field = (TextField) indivdualComp11;
																field.setValue("");
															}
															if(indivdualComp11 instanceof ComboBox){
																ComboBox field = (ComboBox) indivdualComp11;
																String id = field.getId();
																if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
																	continue;
																}else{
																	field.setValue(null);
																}
															}
															if(indivdualComp11 instanceof DateField){
																DateField field = (DateField) indivdualComp11;
																field.setValue(null);
															}
															if(indivdualComp1 instanceof CheckBox)
															{
																CheckBox field = (CheckBox) indivdualComp1;
																field.setValue(null);
															}
															if(indivdualComp instanceof HorizontalLayout)
															{
																HorizontalLayout hLayout3 = (HorizontalLayout)indivdualComp;
																Iterator<Component> horizontalCompIter1 = hLayout2.iterator();
																while (horizontalCompIter1.hasNext()) {
																	Component indivdualComp2 = horizontalCompIter1.next();
																	if (indivdualComp2 != null) {
																		if (indivdualComp2 instanceof Label) {
																			continue;
																		}

																		if (indivdualComp2 instanceof TextField) {
																			TextField field = (TextField) indivdualComp2;
																			field.setValue("");
																		}
																		if(indivdualComp2 instanceof ComboBox){
																			ComboBox field = (ComboBox) indivdualComp2;
																			String id = field.getId();
																			if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
																				continue;
																			}else{
																				field.setValue(null);
																			}
																		}
																		if(indivdualComp2 instanceof DateField){
																			DateField field = (DateField) indivdualComp2;
																			field.setValue(null);
																		}
																		if(indivdualComp2 instanceof CheckBox)
																		{
																			CheckBox field = (CheckBox) indivdualComp2;
																			field.setValue(null);
																		}
														}
													}
												}
															else if(indivdualComp instanceof VerticalLayout)
															{
																VerticalLayout hLayout3 = (VerticalLayout)indivdualComp;
																Iterator<Component> horizontalCompIter1 = hLayout3.iterator();
																while (horizontalCompIter1.hasNext()) {
																	Component indivdualComp2 = horizontalCompIter1.next();
																	if (indivdualComp2 != null) {
																		if (indivdualComp2 instanceof Label) {
																			continue;
																		}

																		if (indivdualComp2 instanceof TextField) {
																			TextField field = (TextField) indivdualComp2;
																			field.setValue("");
																		}
																		if(indivdualComp2 instanceof ComboBox){
																			ComboBox field = (ComboBox) indivdualComp2;
																			String id = field.getId();
																			if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
																				continue;
																			}else{
																				field.setValue(null);
																			}
																		}
																		if(indivdualComp2 instanceof DateField){
																			DateField field = (DateField) indivdualComp2;
																			field.setValue(null);
																		}
																		if(indivdualComp2 instanceof CheckBox)
																		{
																			CheckBox field = (CheckBox) indivdualComp2;
																			field.setValue(null);
																		}
														}
													}
												}
											}
										}
										
									}
								}
									if(indivdualComp instanceof FormLayout)
									{
										FormLayout fLayout = (FormLayout) indivdualComp;
										Iterator<Component> formComIter = fLayout.iterator();	
										while (formComIter.hasNext()) {
											Component indivdualComp11 = formComIter.next();
											if (indivdualComp11 != null) {
												if (indivdualComp11 instanceof Label) {
													continue;
												}

												if (indivdualComp11 instanceof TextField) {
													TextField field = (TextField) indivdualComp11;
													field.setValue("");
												}
												if(indivdualComp11 instanceof ComboBox){
													ComboBox field = (ComboBox) indivdualComp11;
													String id = field.getId();
													if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
														continue;
													}else{
														field.setValue(null);
													}
												}
												if(indivdualComp11 instanceof DateField){
													DateField field = (DateField) indivdualComp11;
													field.setValue(null);
												}
												if(indivdualComp11 instanceof CheckBox)
												{
													CheckBox field = (CheckBox) indivdualComp11;
													field.setValue(null);
												}
											}
										}
									}
										
									
						}
								
									
									}
									
									if(indivdualComp instanceof FormLayout)
									{
										FormLayout fLayout = (FormLayout) indivdualComp;
										Iterator<Component> formComIter = fLayout.iterator();	
										while (formComIter.hasNext()) {
											Component indivdualComp11 = formComIter.next();
											if (indivdualComp11 != null) {
												if (indivdualComp11 instanceof Label) {
													continue;
												}

												if (indivdualComp11 instanceof TextField) {
													TextField field = (TextField) indivdualComp11;
													field.setValue("");
												}
												if(indivdualComp11 instanceof ComboBox){
													ComboBox field = (ComboBox) indivdualComp11;
													String id = field.getId();
													if(id != null && id.equalsIgnoreCase(SHAConstants.COMBOBOX_NOT_RESET)){
														continue;
													}else{
														field.setValue(null);
													}
												}
												if(indivdualComp11 instanceof DateField){
													DateField field = (DateField) indivdualComp11;
													field.setValue(null);
												}
												if(indivdualComp11 instanceof CheckBox)
												{
													CheckBox field = (CheckBox) indivdualComp11;
													field.setValue(null);
												}
											}
										}
									}
						}}
							
						}
					}
				}
			}
		}
		
	}

		
	
	
	


	/**
	 * The below method validates the date which is entered by the user in the
	 * date fields.
	 * */

	public static boolean validateDate(Date date) {
		try {
			System.out.println("--the date----" + date);
			// if(null != date)
			// {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"dd/MM/yyyy");
			String formatedDate = dateFormat.format(date);
			dateFormat.parse(formatedDate);
			return true;
			// }
			/*
			 * else { //return false; }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public static String formatDate(Date date) {
		String strDate = "";
	try {
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			strDate = dateFormat.format(date);
		}
		} catch(Exception e) {
			
		}
		
		return strDate;
	}
	
	public static String formatDateForOP(Date date) {
		String strDate = "";
		try {
			if (date != null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-YY");
				strDate = dateFormat.format(date);
			}
		} catch(Exception e) {
			
		}
		
		return strDate;
	}


	public static boolean isEmpty(String value) {
		return ((value == null || value.equals("")) ? true : false);
	}


	public static String formatDateTime(Date date) {
		String strDate = "";
		if (date != null) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss aaa");
		strDate = dateFormat.format(date);
		}
		return strDate;
	}

	
	/**
	 * Method which will validate date of death.
	 * This date should be greater than or equal to 
	 * admission date and should be less than or equal
	 * to current date.
	 * 
	 * */
	
	public static boolean validateDeathDate(Date deathDate , Date admissionDate)
	{
		Date currentSystemDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String deathDate1=sdf.format(deathDate);
		String currentSystemDate1 = sdf.format(currentSystemDate);
		if((deathDate.equals(admissionDate) || deathDate.after(admissionDate))
				/*&& (deathDate1.equals(currentSystemDate) || deathDate.before(currentSystemDate))*/)
		{
			return true;
		}	
		else
		{
			return false;
		}
	}
	
	private void loadMapForStarSeniorCitizen()
	{
		if(null == seniorCitizenViewMap || seniorCitizenViewMap.isEmpty())
		{
			seniorCitizenViewMap = new WeakHashMap<Integer , Long>();
			seniorCitizenViewMap.put(1, ReferenceTable.ROOM_RENT_NURSING_CHARGES);
			seniorCitizenViewMap.put(2, ReferenceTable.ICU_CHARGES);
			if(isPackageAvailable)
			{
				seniorCitizenViewMap.put(3, ReferenceTable.PROFESSIONAL_CHARGES);
				seniorCitizenViewMap.put(4, ReferenceTable.OT_CHARGES);
				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("N").equalsIgnoreCase(this.packageFlg))
				{
					seniorCitizenViewMap.put(10,ReferenceTable.PROCEDURES);
					//seniorCitizenViewMap.put(13,ReferenceTable.PACKAGE_CHARGES);
					seniorCitizenViewMap.put(11,ReferenceTable.ANH_PACKAGES);
					seniorCitizenViewMap.put(12,ReferenceTable.COMPOSITE_PACKAGES );
					seniorCitizenViewMap.put(13,ReferenceTable.OTHER_PACKAGES);
					//seniorCitizenViewMap.put(17,ReferenceTable.MISC_CHARGES);
					seniorCitizenViewMap.put(14,ReferenceTable.MISC_WITHIN_HOSPITAL);
					seniorCitizenViewMap.put(15,ReferenceTable.MISC_OUTSIDE_HOSPITAL);
					seniorCitizenViewMap.put(16,ReferenceTable.OTHERS);
					seniorCitizenViewMap.put(17,ReferenceTable.AMBULANCE_FEES);
					seniorCitizenViewMap.put(18,ReferenceTable.HOSPITAL_DISCOUNT);
					seniorCitizenViewMap.put(19,ReferenceTable.DEDUCTIONS);
					seniorCitizenViewMap.put(20,ReferenceTable.NETWORK_HOSPITAL_DISCOUNT);
				}
				else
				{
					//seniorCitizenViewMap.put(10,ReferenceTable.PROCEDURES);
					//seniorCitizenViewMap.put(13,ReferenceTable.PACKAGE_CHARGES);
					seniorCitizenViewMap.put(10,ReferenceTable.ANH_PACKAGES);
					seniorCitizenViewMap.put(11,ReferenceTable.COMPOSITE_PACKAGES );
					seniorCitizenViewMap.put(12,ReferenceTable.OTHER_PACKAGES);
					//seniorCitizenViewMap.put(17,ReferenceTable.MISC_CHARGES);
					seniorCitizenViewMap.put(13,ReferenceTable.MISC_WITHIN_HOSPITAL);
					seniorCitizenViewMap.put(14,ReferenceTable.MISC_OUTSIDE_HOSPITAL);
					seniorCitizenViewMap.put(15,ReferenceTable.OTHERS);
					seniorCitizenViewMap.put(16,ReferenceTable.AMBULANCE_FEES);
					seniorCitizenViewMap.put(17,ReferenceTable.HOSPITAL_DISCOUNT);
					seniorCitizenViewMap.put(18,ReferenceTable.DEDUCTIONS);
					seniorCitizenViewMap.put(19,ReferenceTable.NETWORK_HOSPITAL_DISCOUNT);
				}
				

			}
			else
			{
				seniorCitizenViewMap.put(3, ReferenceTable.OT_CHARGES);
				seniorCitizenViewMap.put(4, ReferenceTable.PROFESSIONAL_CHARGES);
				seniorCitizenViewMap.put(10,ReferenceTable.AMBULANCE_FEES);
				//seniorCitizenViewMap.put(13,ReferenceTable.PACKAGE_CHARGES);
				seniorCitizenViewMap.put(11,ReferenceTable.ANH_PACKAGES);
				seniorCitizenViewMap.put(12,ReferenceTable.COMPOSITE_PACKAGES );
				seniorCitizenViewMap.put(13,ReferenceTable.OTHER_PACKAGES);
				seniorCitizenViewMap.put(14,ReferenceTable.PROCEDURES);
				//seniorCitizenViewMap.put(18,ReferenceTable.MISC_CHARGES);
				seniorCitizenViewMap.put(15,ReferenceTable.MISC_WITHIN_HOSPITAL);
				seniorCitizenViewMap.put(16,ReferenceTable.MISC_OUTSIDE_HOSPITAL);
				seniorCitizenViewMap.put(17,ReferenceTable.OTHERS);
				seniorCitizenViewMap.put(18,ReferenceTable.HOSPITAL_DISCOUNT);
				seniorCitizenViewMap.put(19,ReferenceTable.DEDUCTIONS);	
				seniorCitizenViewMap.put(20,ReferenceTable.NETWORK_HOSPITAL_DISCOUNT);
			}
			//seniorCitizenViewMap.put(5,ReferenceTable.INVESTIGATION_DIAG);
			seniorCitizenViewMap.put(5,ReferenceTable.INVESTIGATION_DIAG_WITHIN_HOSPITAL);
			seniorCitizenViewMap.put(6,ReferenceTable.INVESTIGATION_DIAG_OUTSIDE_HOSPITAL);
			//seniorCitizenViewMap.put(8,ReferenceTable.MEDICINES_CONSUMABLES);
			seniorCitizenViewMap.put(7,ReferenceTable.MEDICINES);
			seniorCitizenViewMap.put(8,ReferenceTable.CONSUMABLES);
			seniorCitizenViewMap.put(9,ReferenceTable.IMPLANT_STUNT_VALVE);
			
		}
	}
	
	private List<Hospitalisation> getListOfHospDetailsForBillType(List<Hospitalisation> hospList , Long billKey)
	{
		List<Hospitalisation> hospitalisationList = null;
		if(null != hospList && !hospList.isEmpty())
		{
			hospitalisationList = new ArrayList<Hospitalisation>();
			for (Hospitalisation hospitalisation : hospList) {
				if(billKey.equals(hospitalisation.getBillTypeNumber()))
				{
					hospitalisationList.add(hospitalisation);
				}
				
			}
		}
		return hospitalisationList;
	}
	
	public static boolean validateDischargeDate(Date dischargeDate , Date admissionDate)
	{
		Boolean isValidDate = false;
		if(admissionDate == null){
			return true;
		}
		if((dischargeDate.equals(admissionDate) || dischargeDate.after(admissionDate)))
		{
			isValidDate = true;
		}	
		return isValidDate;
	}
	
	/**
	 * Amount Number to words
	 * 
	 */
			//method to parse the double digit number  
		public static StringBuffer getTens(int amount)
		{
		  StringBuffer sb = new StringBuffer("");   
		  if(amount!=0)
		  {
		     if(amount<100 && amount>19 && amount%10!=0)    //Process Tens
		     {     
		      sb.append(convert(amount/10*10));
		      sb.append(convert(amount%10));
		     }
		     else
		     {
		         sb.append(convert(amount));
		     }      
		   }
		   else
		   {
		   }    
		  return sb;
		}
		  //Recursive method to parse the given number 
		    public static StringBuffer getNumberInWords(int amount,int length,StringBuffer amountInWords)
		    {
		      if(amount != 0)
		      {
//		         // Process single digit
//		          if(amount < 20 )      
//		          {
//		           amountInWords.append(convert(amount));
//		          }    
//		          else
//		          {
//		          }    
//		        
//		       //Process Tens
//		          if(length == 2 && amount > 19)   
//		          {
//		            amountInWords.append(convert(amount/10*10));
//		            return amountInWords;
//		          }    
//		          else
//		          {
//		          }
//		     
//		         // Process Hundreds
//		            if(length==3)
//		            {
//		             amountInWords.append(getTens(amount/(int)Math.pow(10, 2)));
//		             amountInWords.append(convert((int)Math.pow(10, 2)));
//		             amountInWords.append(" and");  
//		            }
//		            else
//		            {
//		            }
		      // Process single digit
		         if(length == 1)      
		         {
		          amountInWords.append(convert(amount));
		          return amountInWords;
		         }    
		         else
		         {
		         }    
		         
		           // Process Tens and Hundreds
		         if(length>1 && length<4)
		         {
		             if(amount/(int)Math.pow(10, 2) > 0)  //Check for hundreds
		             {
		              amountInWords.append(getTens(amount/(int)Math.pow(10, 2)));
		              amountInWords.append(convert((int)Math.pow(10, 2)));
		             }
		             else
		             {
		             }
		             if(length==2)   //Check for Tens
		             {
		               amountInWords.append(getTens(amount));
		               return amountInWords;
		             }    
		             else
		             {
		             }
		             if(amount%(int)Math.pow(10, 2)!=0)
		             {    
		              amountInWords.append(" and");  
		             }
		             else
		             {
		             }     
		         }
		         else
		         {
		         }
		     
		            // Process Ten thousands and Thousands
		          if(length>3 && length <6)
		          {
		             if(amount/(int)Math.pow(10, 3)>0)   // Check for thousands
		             {
		               amountInWords.append(getTens(amount/(int)Math.pow(10, 3)));
		             }
		              else
		              {
		              }
		              amountInWords.append(convert((int)Math.pow(10, 3)));
		           }
		           else
		           {  
		           }
		          
		            // Process Ten Lakhs and Lakhs
		           if(length>5 && length<8)
		           {
		               if(amount/(int)Math.pow(10, 5)>0)   // Check for lakhs
		               {
		                 amountInWords.append(getTens(amount/(int)Math.pow(10, 5)));
		               }
		               else
		               {
		               }
		               amountInWords.append(convert((int)Math.pow(10, 5)));
		           }    
		           else
		           {
		           }
		            
		            // Process Ten Crores and Crores
		            if(length>7 && length<10)
		            {
		                if(amount/(int)Math.pow(10, 7)>0)  // Check for crores
		                {
		                  amountInWords.append(getTens(amount/(int)Math.pow(10, 7)));
		                }
		                else
		                {
		                }
		                amountInWords.append(convert((int)Math.pow(10, 7)));
		            }    
		            else
		            {
		            }
		            
		            // Get the next Weightage Amount
		        if(length%2 == 0 || length == 3 || length == 1 )
		        {    
		         amount%=(int)Math.pow(10, length-1);    //Reduce the amount to next weightage
		        }
		        else
		        {
		           if(length > 3 && length%2 != 0)
		           {
		               amount%=(int)Math.pow(10, length-2);   //Reduce the amount to next weightage
		           }
		           else
		           {
		           }    
		        }
		        length=Integer.toString(amount).length();
		        getNumberInWords(amount,length,amountInWords);  
		        }
		        else
		        {
		          }
		       return amountInWords;
		   }
		   
		   // Method to convert number to words
		    private static String convert(int amount)
		    {   
		     String amtWords="";
		             switch(amount)
		             {
		                 case 1 :      amtWords =" One";     break;
		                 case 2 :      amtWords =" Two";     break;
		                 case 3 :    amtWords =" Three";  	 break;
		                 case 4 :     amtWords =" Four";     break;
		                 case 5 :     amtWords =" Five";     break;
		                 case 6 :      amtWords =" Six";     break;
		                 case 7 :    amtWords =" Seven";   	 break;
		                 case 8 :    amtWords =" Eight";   	 break;
		                 case 9 :     amtWords =" Nine";     break;
		                 case 10 :     amtWords =" Ten";     break;
		                 case 11 :   amtWords=" Eleven";     break;
		                 case 12 :   amtWords=" Twelve";     break;
		                 case 13 : amtWords =" Thirteen";    break;
		                 case 14 : amtWords =" Fourteen";    break;
		                 case 15 :  amtWords =" Fifteen";    break;
		                 case 16 :  amtWords =" Sixteen";    break;
		                 case 17 :amtWords =" Seventeen";    break;
		                 case 18 : amtWords =" Eighteen";    break;
		                 case 19 : amtWords =" Nineteen";    break;
		                 case 20 :   amtWords =" Twenty";    break;
		                 case 30 :   amtWords =" Thirty";    break;
		                 case 40 :   amtWords =" Forty";     break;
		                 case 50 :    amtWords =" Fifty";    break;
		                 case 60 :    amtWords =" Sixty";    break;
		                 case 70 :  amtWords =" Seventy";    break;
		                 case 80 :   amtWords =" Eighty";    break;
		                 case 90 :   amtWords =" Ninety";    break;
		                 case 100 :  amtWords =" Hundred";   break;
		                 case 1000 : amtWords =" Thousand";  break;
		                 case 100000 :  amtWords =" Lakh";   break;
		                 case 10000000 : amtWords =" Crore"; break;
		                 default: amtWords="";
		             }
		      return amtWords;
		    }
		    
		    /**
		     * 
		     * @param amount type double
		     * @returns amount in words as string
		     */
		    public static String getParsedAmount(double amount)
		    {
		    	StringBuffer amountInWords = new StringBuffer();
		    	String amountToWords = "";
		    	if(amount > 0)
		    	{
		        int a_rupees;
		        int a_paise;
		        int a_length;
		          
		        String s="";
		        a_rupees = (int)(amount);
		        a_paise = Math.round((float)(amount - a_rupees)*100);  
		        a_length=Integer.toString(a_rupees).length();
		        if(a_rupees !=0)
		        {    
		          if(a_paise==100)
		          {
		            a_paise=0;
		            a_rupees++;
		          }
		          else
		          {
		          }
		         amountInWords=getNumberInWords(a_rupees,a_length,amountInWords);
		        }
		        else
		        {  
		        }
		         if(a_paise !=0)
		          {    
		           amountInWords.append(" and");
		           amountInWords.append(getTens(a_paise));
		           amountInWords.append(" Paise only");  
		          }
		          else
		          {
		        	  amountInWords.append(" Only");
		          } 
		      
		         amountToWords = amountInWords.toString();
		    
		    }
		    	else if(amount == 0){
		    		amountToWords = "Zero Only";
		    	}
		    
		    return amountToWords;	
		    	
		    }
//		    public static void main(String args[])
//		    {
//		      double amt=105000d;
//		      String ss;
////		     for(int i = 1; i < 10; i++)
////		     {  
////		         amt= Math.random()*Math.pow(10, i);
//		         int amount = (int)(amt);
//		         int paisa = Math.round((float)(amt - amount)*100);  
//		         ss=getParsedAmount(amt);
//		       System.out.println(amount+"."+paisa+": "+ss);
////		     }
//		    }
		

		    /*public static int getTokenNumber()
=======
	public static String getTokenNumber()
>>>>>>> 96f3dfdd9862576750f9ab40e5051a341ef2b0d6
		    {
		    	/*Random randomGenerator = new Random();
			    int tokenId = randomGenerator.nextInt(10000);
			    return tokenId;
			    }*/

		    
		    public static String getTokenNumber()
		    {
		    	Random randomGenerator = new Random();
			    int tokenId = randomGenerator.nextInt(10000);
				StringBuffer tokenBfr = new StringBuffer();
				Date date  = new Date();
				//tokenBfr.append(tokenId).append(new Timestamp(date.getTime()));
				tokenBfr.append(tokenId).append(date.getTime());
				System.out.println("--the token id --"+tokenId);
				System.out.println("---the time stamp----"+date.getTime());
				
				System.out.println("--the generated token----"+tokenBfr.toString());
				
				return String.valueOf(tokenBfr);
		    }
		    
		    
		    public static String getDateFormat(String date){
		    	
		    	String format = null;
		    	if(date != null){
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date convertedCurrentDate = sdf.parse(date);
					format = new SimpleDateFormat("dd/MM/yyyy").format(convertedCurrentDate);
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return format;
		    	}
		    	else
		    	{
		    		return null;
		    	}
		}
		    
		    private BillEntryDetailsDTO populateHospitalizationDetails(BillEntryDetailsDTO billEntryDetailsDTO1 , Hospitalisation hospitalisation)
			{
				
				billEntryDetailsDTO1.setNoOfDays(getValidDoubleFromString(String.valueOf(hospitalisation.getNoOfDays())));
				billEntryDetailsDTO1.setPerDayAmt(getValidDoubleFromString(String.valueOf(hospitalisation.getPerDayAmount())));
				billEntryDetailsDTO1.setItemValue(getValidDoubleFromString(String.valueOf(hospitalisation.getClaimedAmount())));
				billEntryDetailsDTO1.setNoOfDaysAllowed(getValidDoubleFromString(String.valueOf(hospitalisation.getPolicyNoOfDays())));
				billEntryDetailsDTO1.setPerDayAmtProductBased(getValidDoubleFromString(String.valueOf(hospitalisation.getPolicyPerDayAmount())));
				billEntryDetailsDTO1.setAmountAllowableAmount(getValidDoubleFromString(String.valueOf(hospitalisation.getTotalAmount())));
				billEntryDetailsDTO1.setNonPayableProductBased(getValidDoubleFromString(String.valueOf(hospitalisation.getNonPayableAmountProduct())));
				billEntryDetailsDTO1.setNonPayable(getValidDoubleFromString(String.valueOf(hospitalisation.getNonPayableAmt())));
				billEntryDetailsDTO1.setProportionateDeduction(getValidDoubleFromString(String.valueOf(hospitalisation.getProrateDeductionAmount())));
				billEntryDetailsDTO1.setReasonableDeduction(getValidDoubleFromString(String.valueOf(hospitalisation.getDeductibleAmount())));
				billEntryDetailsDTO1.setTotalDisallowances(getValidDoubleFromString(String.valueOf(hospitalisation.getPayableAmount())));
				billEntryDetailsDTO1.setNetPayableAmount(getValidDoubleFromString(String.valueOf(hospitalisation.getNetAmount())));
				billEntryDetailsDTO1.setDeductibleOrNonPayableReason(hospitalisation.getReason());
				return billEntryDetailsDTO1;
			}
		    
		    public static List<Long> getExecutiveSummaryStatusList(){
		    	
		    	List<Long> statusList = new ArrayList<Long>();
		    	
		    	statusList.add(ReferenceTable.CLAIM_REGISTRATION_STAGE);
		    	statusList.add(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS);
		    	statusList.add(ReferenceTable.PROCESS_REJECTED);
		    	statusList.add(ReferenceTable.PREMEDICAL_WAIVED_REJECTION);
		    	statusList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_REFER_TO_COORDINATOR_STATUS);
		    	statusList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS);
		    	statusList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS);
		    	statusList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_REFER_TO_COORDINATOR_STATUS);
		    	statusList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS);
		    	statusList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS);
		    	statusList.add(ReferenceTable.REFER_TO_FLP);
		    	statusList.add(ReferenceTable.CLEAR_CASHLESS_STATUS_FOR_PREAUTH);
		    	statusList.add(ReferenceTable.CLEAR_CASHLESS_STATUS_FOR_ENHANCEMENT);
		    	statusList.add(ReferenceTable.INITITATE_FVR);
		    	statusList.add(ReferenceTable.PREAUTH_REFER_TO_FLP_STATUS);
		    	statusList.add(ReferenceTable.PREAUTH_REFER_TO_COORDINATOR_STATUS);
		    	statusList.add(ReferenceTable.PREAUTH_APPROVE_STATUS);
		    	statusList.add(ReferenceTable.PREAUTH_REJECT_STATUS);
		    	statusList.add(ReferenceTable.PREAUTH_QUERY_STATUS);
		    	statusList.add(ReferenceTable.PREAUTH_ESCALATION_STATUS);
		    	statusList.add(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS);
		    	statusList.add(ReferenceTable.ENHANCEMENT_REFER_TO_COORDINATOR_STATUS);
		    	statusList.add(ReferenceTable.ENHANCEMENT_REFER_TO_FLP_STATUS);
		    	statusList.add(ReferenceTable.ENHANCEMENT_APPROVE_STATUS);
		    	statusList.add(ReferenceTable.ENHANCEMENT_REJECT_STATUS);
		    	statusList.add(ReferenceTable.ENHANCEMENT_QUERY_STATUS);		    	
		    	statusList.add(ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS);
		    	statusList.add(ReferenceTable.ENHANCEMENT_ESCALATION_STATUS);
		    	statusList.add(ReferenceTable.ENHANCEMENT_REFER_TO_COORDINATOR_STATUS);
		    	statusList.add(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS);
		    	statusList.add(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS);
    			statusList.add(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT);
    			statusList.add(ReferenceTable.ENHANCEMENT_DOWNSIZE_STATUS);
    			statusList.add(ReferenceTable.WITHDRAW_APPROVED_STATUS);
    			statusList.add(ReferenceTable.ENHANCEMENT_REFER_TO_FLP_STATUS);
    			statusList.add(ReferenceTable.DOWNSIZE_ESCALATION_STATUS);
    			statusList.add(ReferenceTable.STANDALONE_WITHDRAW_STATUS);
    			statusList.add(ReferenceTable.PED_INITIATE);
    			statusList.add(ReferenceTable.PED_REJECT);
    			statusList.add(ReferenceTable.PED_APPROVED);
    			statusList.add(ReferenceTable.SEND_TO_APPROVER);
    			statusList.add(ReferenceTable.ASSIGNFVR);    			
    			statusList.add(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS);
    			statusList.add(ReferenceTable.PED_REJECT);
    			statusList.add(ReferenceTable.SEND_TO_APPROVER);
    			statusList.add(ReferenceTable.PED_APPROVED);
    			statusList.add(ReferenceTable.ASSIGNFVR);
    			statusList.add(ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED);
    			statusList.add(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
    			statusList.add(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
    			statusList.add(ReferenceTable.CREATE_ROD_STATUS_KEY);
    			statusList.add(ReferenceTable.CREATE_ROD_CLOSED_STATUS);
    			statusList.add(ReferenceTable.BILL_ENTRY_STATUS_KEY);
    			statusList.add(ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS);
    			statusList.add(ReferenceTable.ZONAL_REVIEW_QUERY_STATUS);
    			statusList.add(ReferenceTable.ZONAL_REVIEW_REJECTION_STATUS);
    			statusList.add(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS);    			
    			statusList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_CLOSED_STATUS);
    			statusList.add(ReferenceTable.CLAIM_APPROVAL_REFER_TO_BILLING);
    			statusList.add(ReferenceTable.CLAIM_APPROVAL_REFER_TO_MEDICAL_APPROVER);
    			statusList.add(ReferenceTable.ZONAL_REVIEW_CANCEL_ROD);
    			statusList.add(ReferenceTable.ZONAL_REVIEW_REFER_TO_COORDINATOR_STATUS);
    			statusList.add(ReferenceTable.ZONAL_REVIEW_PROCESS_CLAIM_REQUEST_CLOSED_STATUS);
    			statusList.add(ReferenceTable.CLAIM_APPROVAL_REFER_TO_BILLING);
    			statusList.add(ReferenceTable.CLAIM_APPROVAL_REFER_TO_MEDICAL_APPROVER);
    			statusList.add(ReferenceTable.ZONAL_REVIEW_CANCEL_ROD);
    			statusList.add(ReferenceTable.ZONAL_REVIEW_REFER_TO_COORDINATOR_STATUS);
    			statusList.add(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS);
    			statusList.add(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
    			statusList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_DRAFT_QUERY_STATUS);
    			statusList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS);
    			statusList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS);
    			statusList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS);
    			statusList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS);
    			statusList.add(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
				statusList.add(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
    			statusList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);
    			statusList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_REDRAFT_REJECT_STATUS);
    			statusList.add(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
    			statusList.add(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS);
    			statusList.add(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
    			statusList.add(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);
    			statusList.add(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
    			statusList.add(ReferenceTable.BILLING_REFER_TO_COORDINATOR);
    			statusList.add(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER);
    			statusList.add(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY);
    			statusList.add(ReferenceTable.BILLING_BENEFITS_APPROVED);
    			statusList.add(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER);
    			statusList.add(ReferenceTable.BILLING_CLOSED_STATUS);
    			statusList.add(ReferenceTable.BILLING_CANCEL_ROD);
    			statusList.add(ReferenceTable.FINANCIAL_REJECT_STATUS);
    			statusList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DRAFT_REJECT_STATUS);
    			statusList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_REDRAFT_REJECT_STATUS);
    			statusList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS);
    			statusList.add(ReferenceTable.FINANCIAL_INITIATE_FIELD_REQUEST_STATUS);
    			statusList.add(ReferenceTable.FINANCIAL_REFER_TO_CLAIM_APPROVAL);
    			statusList.add(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER);
    			statusList.add(ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS);
    			statusList.add(ReferenceTable.FINANCIAL_REFER_TO_BILLING);
    			statusList.add(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST);
    			statusList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS);		
    			statusList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS);
    			statusList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS);
    			statusList.add(ReferenceTable.FINANCIAL_QUERY_STATUS);
    			statusList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DRAFT_QUERY_STATUS);
    			statusList.add(ReferenceTable.FINANCIAL_SETTLED);
    			statusList.add(ReferenceTable.FINANCIAL_CLOSED_STATUS);
    			statusList.add(ReferenceTable.FINANCIAL_CANCEL_ROD);
    			statusList.add(ReferenceTable.FINANCIAL_APPROVE_STATUS);
    			statusList.add(ReferenceTable.FINANCIAL_INITIATE_INVESTIGATION_STATUS);
    			statusList.add(ReferenceTable.INTIATE_RRC_REQUEST_STATUS);
    			statusList.add(ReferenceTable.RRC_REQUEST_PROCESS_STATUS);
    			statusList.add(ReferenceTable.RRC_REQUEST_HOLD_STATUS);
    			statusList.add(ReferenceTable.RRC_REQUEST_REVIEWED_STATUS);
    			statusList.add(ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS);
    			statusList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_CLOSED_STATUS);
    			statusList.add(ReferenceTable.ZONAL_REVIEW_PROCESS_CLAIM_REQUEST_CLOSED_STATUS);
    			statusList.add(ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS);
    			statusList.add(ReferenceTable.BILLING_CLOSED_STATUS);
    			statusList.add(ReferenceTable.CREATE_ROD_REOPENED);
    			statusList.add(ReferenceTable.BILLING_REOPENED);
    			statusList.add(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS);
    			statusList.add(ReferenceTable.SEND_FOR_PAYMENT_APPROVAL_STATUS);
    			statusList.add(ReferenceTable.LOT_CREATED_STATUS);
    			statusList.add(ReferenceTable.BATCH_CREATED_STATUS);
    			statusList.add(ReferenceTable.PAYMENT_NEW_STATUS);
    			statusList.add(ReferenceTable.PAYMENT_SETTLED);
    			statusList.add(ReferenceTable.PAYMENT_CANCELLED);
    			statusList.add(ReferenceTable.PAYMENT_RECONSIDERATION);
    			statusList.add(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS);
    			statusList.add(ReferenceTable.CLAIM_REOPENED_STATUS);
    			statusList.add(ReferenceTable.PREAUTH_REOPENED_STATUS);
    			statusList.add(ReferenceTable.PREAUTH_CLOSED_STATUS);
    			statusList.add(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY);
    			statusList.add(ReferenceTable.INITIATE_INVESTIGATION);
    			statusList.add(ReferenceTable.INITIATE_INVESTIGATION_APPROVED);    			
    			statusList.add(ReferenceTable.ASSIGN_INVESTIGATION);
    			statusList.add(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED);
    			statusList.add(ReferenceTable.DRAFT_INVESTIGATION);
    			statusList.add(ReferenceTable.ZONAL_REVIEW_INITATE_INV_STATUS);
    			statusList.add(ReferenceTable.ZMR_REFER_TO_BILL_ENTRY);
    			statusList.add(ReferenceTable.CLAIM_REQUEST_REFER_TO_BILL_ENTRY);
    			statusList.add(ReferenceTable.INVESTIGATION_GRADING);
    			//add for withdrawnPostProcess
    			statusList.add(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS);
		    	return statusList;
		    }
		    
		    
		    private Hospitalisation getHospDetailsForBillType(List<Hospitalisation> hospList , Long billKey)
			{
				Hospitalisation hospitalizationObj = null;
				if(null != hospList && !hospList.isEmpty())
				{
					for (Hospitalisation hospitalisation : hospList) {
						if(billKey.equals(hospitalisation.getBillTypeNumber()))
						{
							hospitalizationObj = hospitalisation;
							break;
						}
						
					}
				}
				return hospitalizationObj;
				
			}
		    
		    private Double getValidDoubleFromString(String value)
			{
				Double dValue = null;
				if(null != value && !("").equalsIgnoreCase(value))
				{
				 dValue = SHAUtils.getDoubleValueFromString(value);
				}
				return dValue;
			
			}
		    
		    private Double getDiffOfTwoNumber(Double number1 , Double number2)
			{
				Double diff = 0d;
				if(number1 > number2)
				{
					diff = number1 - number2;
				}
				else
				{
					diff = number2 - number1;
				}
				return diff;
			}
		    
			
			public List<BillEntryDetailsDTO> setHospitalizationTableValuesForStarSeniorCitizen(Long rodKey,  List<Hospitalisation> newHospitalizationList, CreateRODService billDetailsService )
			{
				 List<BillEntryDetailsDTO> totalList = new ArrayList<BillEntryDetailsDTO>();
				 List<MasBillDetailsType> billDetailsType = billDetailsService.getBillDetails(ReferenceTable.HOSPITALIZATION);
				 
				 //Reimbursement reimbursement  = billDetailsService.getReimbursementObjectByKey(rodKey);
				 List<RODDocumentSummary> rodDocSummary = billDetailsService.getRODSummaryDetailsByReimbursementKey(rodKey);
				 //List<Hospitalisation> hospitalizationList = billDetailsService.getHospitalisationListOrderByItemNumber(rodKey);
				 List<Hospitalisation> roomRentList = null;
				 List<Hospitalisation> nursingList = null;
				 List<Hospitalisation> icuRoomRentList = null;
				 List<Hospitalisation> icuNursingList = null;
				 
				/* Double totalItemValueInFooter = 0d;
				 Double totalProductPerDayAmtInFooter = 0d;
				 Double totalAllowableAmtInFooter = 0d;
				 Double totalNonPayablePdtBasedInFooter = 0d;
				 Double totalNonPayableManualInFooter = 0d;
				 Double totalProportionateDedInFooter = 0d;
				 Double totalReasonableDeductionInFooter = 0d;
				 Double totalDisallowancesInFooter = 0d;
				 Double totalNetPayableInFooter = 0d;*/
				 
				 Double packTotalItemValueInFooter = 0d;
				 Double packTotalAllowableAmtInFooter = 0d;
				 Double packTotalNonPayablePdtBasedInFooter = 0d;
				 Double packTotalNonPayableManualInFooter = 0d;
				 Double packTotalProportionateDedInFooter = 0d;
				 Double packTotalReasonableDeductionInFooter = 0d;
				 Double packTotalDisallowancesInFooter = 0d;
				 Double packTotalNetPayableInFooter = 0d;
				 
				 Double nonpackTotalItemValueInFooter = 0d;
				 Double nonpackTotalProductPerDayAmtInFooter = 0d;
				 Double nonpackTotalAllowableAmtInFooter = 0d;
				 Double nonpackTotalNonPayablePdtBasedInFooter = 0d;
				 
				//To start from herenonpackTotalNonPayablePdtBasedInFooter
				 Double nonpackTotalNonPayableManualInFooter = 0d;
				 Double nonpackTotalProportionateDedInFooter = 0d;
				 Double nonpackTotalReasonableDeductionInFooter = 0d;
				 Double nonpackTotalDisallowancesInFooter = 0d;
				 Double nonpackTotalNetPayableInFooter = 0d;
				 
				 
				 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
				 {
					 roomRentList = new ArrayList<Hospitalisation>();
					 nursingList = new ArrayList<Hospitalisation>();
					 icuRoomRentList = new ArrayList<Hospitalisation>();
					 icuNursingList = new ArrayList<Hospitalisation>();
					 
					 for (Hospitalisation hospitalisation : newHospitalizationList) {
						 if(ReferenceTable.ROOM_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
							 roomRentList.add(hospitalisation);
						 }
						 if(ReferenceTable.NURSING_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
							 nursingList.add(hospitalisation);
						 }
						 
						 if(ReferenceTable.ICU_ROOM_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
							 icuRoomRentList.add(hospitalisation);
						 }
						 if(ReferenceTable.ICU_NURSING_BILL_TYPE.equals(hospitalisation.getBillTypeNumber())){
							 icuNursingList.add(hospitalisation);
						 }
						 
						 
						 
						 //Need to create another list for ICU rooms and ICU nursing charges. 
						
					}
					 
				 }
				
				 if(null != billDetailsType && !billDetailsType.isEmpty())
				 {
					 //Applicable only for senior citizen red carpet pdt alone.
					 Double totalAmountIfPackAvailable = 0d;
					 Double totalNonPayableAmt = 0d;
					 Double totalProportionateDeduction = 0d;
					 Double totalReasonableDeductionAmt = 0d;
					 Double nonPayableProductBasedIfPackageIsAvailable = 0d;
					 Double totalDisallowancesIfPackageIsAvailable = 0d;
					
					 loadMapForStarSeniorCitizen();
					 int iMapSize = 0;
					 if(null != seniorCitizenViewMap || !seniorCitizenViewMap.isEmpty())
					 {
						iMapSize = seniorCitizenViewMap.size(); 
					 }
					// for (MasBillDetailsType masBillType : billDetailsType)
					 for(int i = 1; i<=iMapSize; i++)
					 {
						 Long billTypeKey = (Long)seniorCitizenViewMap.get(i);
						 //if((SHAConstants.ROOM_RENT_NURSING_CHARGES).equalsIgnoreCase(masBillType.getValue()))
						 if((ReferenceTable.ROOM_RENT_NURSING_CHARGES).equals(billTypeKey))
						 {
							 //Adding a blank row for room rent and nursing charges.
							 
							 
							 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
							 billEntryDetailsDTO.setItemNoForView(Double.parseDouble(String.valueOf(i)));
							 billEntryDetailsDTO.setItemName(SHAConstants.ROOM_RENT_NURSING_CHARGES);
							 totalList.add(billEntryDetailsDTO);
							 
							 //Adding fields for calculating total room rent.
							 Double totalClaimedAmt = 0d;
							 Double totalPdtPerDayAmt = 0d;
							 Double totalAllowableAmt = 0d;
							 Double totalNonPayable = 0d;
							 Double totalNonPayableManual = 0d;
							 Double totalReasonableDeduction = 0d;
							 Double totalDisallowances = 0d;
							 Double netPayableAmt = 0d;
							 Double proportionalDed = 0d;
							 
							 int iNo = 0;
							 if(null != rodDocSummary && !rodDocSummary.isEmpty())
							 {
								 for (RODDocumentSummary rodDocumentSummary : rodDocSummary) {
									List<RODBillDetails> rodBillDetails = billDetailsService.getBillEntryDetails(rodDocumentSummary.getKey());
									if(null != rodBillDetails && !rodBillDetails.isEmpty())
									{
										for (RODBillDetails rodBillDetails2 : rodBillDetails) {
											
											if(ReferenceTable.ROOM_CATEGORY_ID.equals(rodBillDetails2.getBillCategory().getKey()))
											{
												
												for (Hospitalisation hospitalisation : roomRentList) {
													BillEntryDetailsDTO billEntryDetailsDTO1 = new BillEntryDetailsDTO();
													if(null != hospitalisation.getItemNumber())
													{
														if(rodBillDetails2.getKey().equals(hospitalisation.getItemNumber()))
														{
															if(iNo != 0)
															{
																String value = "1."+ iNo;
																billEntryDetailsDTO1.setItemNoForView(Double.valueOf(value));
															}
															//billEntryDetailsDTO1.setItemNo(Long.valueOf(value));
															billEntryDetailsDTO1.setItemName("	a)Room Rent");
															billEntryDetailsDTO1 = populateHospitalizationDetails(billEntryDetailsDTO1,hospitalisation);
															 totalList.add(billEntryDetailsDTO);
															int iSlNo = 98;
															Double totalNursingCharges = 0d;
															Double totalNursingClaimedAmt = 0d;
															for(int j = 0 ; j<nursingList.size() ; j++)
															{
																Hospitalisation hospitalisationObj = nursingList.get(j);
																if(hospitalisation.getItemNumber().equals(hospitalisationObj.getItemNumber()))
																{	
																	BillEntryDetailsDTO billEntryDetailsDTO2 = new BillEntryDetailsDTO();
																	billEntryDetailsDTO2.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
																	billEntryDetailsDTO2.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
																	billEntryDetailsDTO2.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
																	billEntryDetailsDTO2.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
																	//To calculate the total nursing charges.
																	totalNursingCharges += billEntryDetailsDTO2.getPerDayAmt();
																	totalNursingClaimedAmt += billEntryDetailsDTO2.getItemValue();
																	totalList.add(billEntryDetailsDTO2);
																	iSlNo++;
																}
															}
															
															//Add code for sub total charges for each room rent and nursing charges.
															BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
															billEntryDetailsDTO3.setItemName("Sub Total (Sl no 1."+iNo+"(a) and 1."+iNo+"(b))");
															billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3,hospitalisation);
															billEntryDetailsDTO3.setNoOfDays(null);
															Double totalPerDayNursingCharges = billEntryDetailsDTO1.getPerDayAmt() + totalNursingCharges;
															billEntryDetailsDTO3.setPerDayAmt(totalPerDayNursingCharges);
															Double totalClaimedAmount = billEntryDetailsDTO1.getItemValue() + totalNursingClaimedAmt;
															billEntryDetailsDTO3.setItemValue(totalClaimedAmount);	
															totalList.add(billEntryDetailsDTO3);
															totalClaimedAmt += totalClaimedAmount;
															if(null != billEntryDetailsDTO1.getPerDayAmtProductBased())
															totalPdtPerDayAmt += billEntryDetailsDTO1.getPerDayAmtProductBased();
															if(null != billEntryDetailsDTO3.getAmountAllowableAmount())
															totalAllowableAmt += billEntryDetailsDTO3.getAmountAllowableAmount();
															if(null != billEntryDetailsDTO3.getNonPayableProductBased())
															totalNonPayable += billEntryDetailsDTO3.getNonPayableProductBased();
															if(null != billEntryDetailsDTO3.getNonPayable())
															totalNonPayableManual += billEntryDetailsDTO3.getNonPayable();
															if(null != billEntryDetailsDTO3.getReasonableDeduction())
															totalReasonableDeduction += billEntryDetailsDTO3.getReasonableDeduction();
															if(null != billEntryDetailsDTO3.getTotalDisallowances())
															totalDisallowances += billEntryDetailsDTO3.getTotalDisallowances();
															if(null != billEntryDetailsDTO3.getNetPayableAmount())
															netPayableAmt += billEntryDetailsDTO3.getNetPayableAmount();
															if(null != billEntryDetailsDTO3.getProportionateDeduction())
															{
																proportionalDed += billEntryDetailsDTO3.getProportionateDeduction();
															}
															iNo++;
															
														}
													}
													//Add the room rent and nursing charges, if the mapping is not available.
												}
											}
										}
										
									}
								}
							 }
							//Add code for calculating the total room rent.
								BillEntryDetailsDTO billEntryDetailsDTO4 = new BillEntryDetailsDTO();
								billEntryDetailsDTO4.setItemName("Total Room Rent");
								billEntryDetailsDTO4.setItemValue(totalClaimedAmt);
								billEntryDetailsDTO4.setPerDayAmtProductBased(totalPdtPerDayAmt);
								billEntryDetailsDTO4.setAmountAllowableAmount(totalAllowableAmt);
								billEntryDetailsDTO4.setNonPayableProductBased(totalNonPayable);
								billEntryDetailsDTO4.setNonPayable(totalNonPayableManual);
								billEntryDetailsDTO4.setProportionateDeduction(proportionalDed);
								billEntryDetailsDTO4.setReasonableDeduction(totalReasonableDeduction);
								billEntryDetailsDTO4.setTotalDisallowances(totalDisallowances);
								billEntryDetailsDTO4.setNetPayableAmount(netPayableAmt);
								
								packTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
								packTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
								packTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
								packTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
								packTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
								packTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
								packTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
								packTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();

								nonpackTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
								nonpackTotalProductPerDayAmtInFooter += billEntryDetailsDTO4.getPerDayAmtProductBased();
								
								nonpackTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
								nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
								nonpackTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
								nonpackTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
								nonpackTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
								nonpackTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
								nonpackTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
								totalList.add(billEntryDetailsDTO4);
								
						 }
						 //Once ICU room rent mapping is finalized, will implement the below block. 
						 else if((ReferenceTable.ICU_CHARGES).equals(billTypeKey))
						 {
							 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
							 billEntryDetailsDTO.setItemNoForView(Double.parseDouble(String.valueOf(i)));
							 billEntryDetailsDTO.setItemName(SHAConstants.ICU_CHARGES);
							 totalList.add(billEntryDetailsDTO);
							 
							 //Adding fields for calculating total room rent.
							 Double totalClaimedAmt = 0d;
							 Double totalAllowableAmt = 0d;
							 Double totalPdtPerDayAmt = 0d;
							 Double totalNonPayable = 0d;
							 Double totalNonPayableManual = 0d;
							 Double totalReasonableDeduction = 0d;
							 Double totalDisallowances = 0d;
							 Double netPayableAmt = 0d;
							 Double proportionalDed = 0d;
							 int iNo = 0;
							 if(null != rodDocSummary && !rodDocSummary.isEmpty())
							 {
								 for (RODDocumentSummary rodDocumentSummary : rodDocSummary) {
									List<RODBillDetails> rodBillDetails = billDetailsService.getBillEntryDetails(rodDocumentSummary.getKey());
									if(null != rodBillDetails && !rodBillDetails.isEmpty())
									{
										for (RODBillDetails rodBillDetails2 : rodBillDetails) {
											
											if(ReferenceTable.ICU_ROOM_CATEGORY_ID.equals(rodBillDetails2.getBillCategory().getKey()))
											{
												
												for (Hospitalisation hospitalisation : icuRoomRentList) {
													BillEntryDetailsDTO billEntryDetailsDTO1 = new BillEntryDetailsDTO();
													if(null != hospitalisation.getItemNumber())
													{
														if(rodBillDetails2.getKey().equals(hospitalisation.getItemNumber()))
														{
															String value = "1."+ iNo;
															billEntryDetailsDTO1.setItemNoForView(Double.parseDouble(value));
															billEntryDetailsDTO1.setItemName("	a)Room Rent");
															billEntryDetailsDTO1 = populateHospitalizationDetails(billEntryDetailsDTO1,hospitalisation);
															totalList.add(billEntryDetailsDTO1);
															int iSlNo = 98;
															Double totalNursingCharges = 0d;
															Double totalNursingClaimedAmt = 0d;
															for(int j = 0 ; j<icuNursingList.size() ; j++)
															{
																Hospitalisation hospitalisationObj = icuNursingList.get(j);
																if(hospitalisation.getItemNumber().equals(hospitalisationObj.getItemNumber()))
																{	
																	BillEntryDetailsDTO billEntryDetailsDTO2 = new BillEntryDetailsDTO();
																	billEntryDetailsDTO2.setItemName("	"+Character.toString((char)iSlNo) +")Nursing Charges");
																	billEntryDetailsDTO2.setNoOfDays(Double.parseDouble(String.valueOf(hospitalisationObj.getNoOfDays())));
																	billEntryDetailsDTO2.setPerDayAmt(Double.parseDouble(String.valueOf(hospitalisationObj.getPerDayAmount())));
																	billEntryDetailsDTO2.setItemValue(Double.parseDouble(String.valueOf(hospitalisationObj.getClaimedAmount())));
																	//To calculate the total nursing charges.
																	totalNursingCharges += billEntryDetailsDTO2.getPerDayAmt();
																	totalNursingClaimedAmt += billEntryDetailsDTO2.getItemValue();
																	totalList.add(billEntryDetailsDTO2);
																	iSlNo++;
																}
															}
															
															//Add code for sub total charges for each room rent and nursing charges.
															BillEntryDetailsDTO billEntryDetailsDTO3 = new BillEntryDetailsDTO();
															billEntryDetailsDTO3.setItemName("Sub Total (Sl no 1."+iNo+"(a) and 1."+iNo+"(b))");
															billEntryDetailsDTO3 = populateHospitalizationDetails(billEntryDetailsDTO3,hospitalisation);
															billEntryDetailsDTO3.setNoOfDays(null);
															Double totalPerDayNursingCharges = billEntryDetailsDTO1.getPerDayAmt() + totalNursingCharges;
															billEntryDetailsDTO3.setPerDayAmt(totalPerDayNursingCharges);
															Double totalClaimedAmount = billEntryDetailsDTO1.getItemValue() + totalNursingClaimedAmt;
															billEntryDetailsDTO3.setItemValue(totalClaimedAmount);	
															totalList.add(billEntryDetailsDTO3);
															totalClaimedAmt += totalClaimedAmount;
															if(null != billEntryDetailsDTO1.getPerDayAmtProductBased())
															totalPdtPerDayAmt += billEntryDetailsDTO1.getPerDayAmtProductBased();
															if(null != billEntryDetailsDTO3.getAmountAllowableAmount())
															totalAllowableAmt += billEntryDetailsDTO3.getAmountAllowableAmount();
															if(null != billEntryDetailsDTO3.getNonPayableProductBased())
															totalNonPayable += billEntryDetailsDTO3.getNonPayableProductBased();
															if(null != billEntryDetailsDTO3.getNonPayable())
															totalNonPayableManual += billEntryDetailsDTO3.getNonPayable();
															if(null != billEntryDetailsDTO3.getReasonableDeduction())
															totalReasonableDeduction += billEntryDetailsDTO3.getReasonableDeduction();
															if(null != billEntryDetailsDTO3.getTotalDisallowances())
															totalDisallowances += billEntryDetailsDTO3.getTotalDisallowances();
															if(null != billEntryDetailsDTO3.getNetPayableAmount())
															netPayableAmt += billEntryDetailsDTO3.getNetPayableAmount();
															if(null != billEntryDetailsDTO3.getProportionateDeduction())
															{
																proportionalDed += billEntryDetailsDTO3.getProportionateDeduction();
															}
															iNo++;
															
														}
													}
													//Add the room rent and nursing charges, if the mapping is not available.
												}
											}
										}
										
									}
								}
							 }
							//Add code for calculating the total room rent.
								BillEntryDetailsDTO billEntryDetailsDTO4 = new BillEntryDetailsDTO();
								billEntryDetailsDTO4.setItemName("Total Room Rent");
								billEntryDetailsDTO4.setItemValue(totalClaimedAmt);
								billEntryDetailsDTO4.setPerDayAmtProductBased(totalPdtPerDayAmt);
								billEntryDetailsDTO4.setAmountAllowableAmount(totalAllowableAmt);
								billEntryDetailsDTO4.setNonPayableProductBased(totalNonPayable);
								billEntryDetailsDTO4.setReasonableDeduction(totalReasonableDeduction);
								billEntryDetailsDTO4.setProportionateDeduction(proportionalDed);
								billEntryDetailsDTO4.setTotalDisallowances(totalDisallowances);
								billEntryDetailsDTO4.setNetPayableAmount(netPayableAmt);
								billEntryDetailsDTO4.setNonPayable(totalNonPayableManual);
								
								packTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
								packTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
								packTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
								packTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
								packTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
								packTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
								packTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
								packTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
								
								nonpackTotalItemValueInFooter += billEntryDetailsDTO4.getItemValue();
								nonpackTotalProductPerDayAmtInFooter += billEntryDetailsDTO4.getPerDayAmtProductBased();
								
								nonpackTotalAllowableAmtInFooter += billEntryDetailsDTO4.getAmountAllowableAmount();
								nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsDTO4.getNonPayableProductBased();
								nonpackTotalNonPayableManualInFooter += billEntryDetailsDTO4.getNonPayable();
								
								nonpackTotalProportionateDedInFooter += billEntryDetailsDTO4.getProportionateDeduction();
								nonpackTotalReasonableDeductionInFooter += billEntryDetailsDTO4.getReasonableDeduction();
								nonpackTotalDisallowancesInFooter += billEntryDetailsDTO4.getTotalDisallowances();
								nonpackTotalNetPayableInFooter += billEntryDetailsDTO4.getNetPayableAmount();
								
								totalList.add(billEntryDetailsDTO4);
						 }
						 else if((ReferenceTable.OT_CHARGES).equals(billTypeKey))
						 {
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			billEntryDetailsObj.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 			billEntryDetailsObj.setItemName(SHAConstants.OT_CHARGES);
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				if(null != billEntryDetailsObj.getItemValue() )
					 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				if(null != billEntryDetailsObj.getNonPayableProductBased())
					 					nonPayableProductBasedIfPackageIsAvailable += billEntryDetailsObj.getNonPayableProductBased();
					 				if(null != billEntryDetailsObj.getNonPayable() )
					 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
					 				if(null != billEntryDetailsObj.getProportionateDeduction() )
					 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
					 				if(null != billEntryDetailsObj.getReasonableDeduction())
					 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
					 		}
						 }
						 else if((ReferenceTable.PROFESSIONAL_CHARGES).equals(billTypeKey))
						 {
							 BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
					 		 billEntryDetailsObject.setItemNoForView(Double.parseDouble(String.valueOf(i)));
					 		 billEntryDetailsObject.setItemName(SHAConstants.PROFESSIONAL_CHARGES);
					 		totalList.add(billEntryDetailsObject);
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			//Added for senior citizen red carpet pdt.
					 			int iSlNo = 97;
				 				Double totalAmount = 0d;
				 				Double productLimit = 0d;
				 				Double allowableAmount = 0d;
				 				Double nonPayable = 0d;
				 				Double reasonableDeduction = 0d;
				 				Double proportionateDeduction = 0d;
				 			//	Double productPerDayAmt = 0d;
					 			List<Hospitalisation> hospObj = getListOfHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj && !hospObj.isEmpty())
					 			{
					 				for (Hospitalisation hospitalisation : hospObj) {
					 					BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 					//billEntryDetailsObj.setItemNoForView(Double.parseDouble(String.valueOf(i)));
							 			billEntryDetailsObj.setItemName("	"+Character.toString((char)iSlNo)+")"+SHAConstants.PROFESSIONAL_CHARGES);
							 			billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospitalisation);
							 			if(null != billEntryDetailsObj.getItemValue())
							 			{
							 				totalAmount += Math.round(billEntryDetailsObj.getItemValue());
							 				nonpackTotalItemValueInFooter += totalAmount;
							 			}
							 			if(null != billEntryDetailsObj.getPerDayAmtProductBased())
							 			{
							 				productLimit += Math.round(billEntryDetailsObj.getPerDayAmtProductBased());
							 				nonpackTotalProductPerDayAmtInFooter += productLimit;
							 			}
							 			if(null != billEntryDetailsObj.getAmountAllowableAmount())
							 			{
							 				allowableAmount += Math.round(billEntryDetailsObj.getAmountAllowableAmount());
							 				nonpackTotalAllowableAmtInFooter += allowableAmount;
							 			}
							 			if(null != billEntryDetailsObj.getNonPayableProductBased())
							 			{
							 				 nonPayableProductBasedIfPackageIsAvailable += billEntryDetailsObj.getNonPayableProductBased();
							 				nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsObj.getNonPayableProductBased();
							 			}
							 			if(null != billEntryDetailsObj.getNonPayable())
							 			{
							 				nonPayable += Math.round(billEntryDetailsObj.getNonPayable());
							 				nonpackTotalNonPayableManualInFooter += nonPayable;
							 			}
										if(null != billEntryDetailsObj.getProportionateDeduction())
										{
											proportionateDeduction += Math.round(billEntryDetailsObj.getProportionateDeduction());
											nonpackTotalProportionateDedInFooter += proportionateDeduction;
										}
							 			if(null != billEntryDetailsObj.getReasonableDeduction())
							 			{
							 				reasonableDeduction += Math.round(billEntryDetailsObj.getReasonableDeduction());
							 				nonpackTotalReasonableDeductionInFooter += reasonableDeduction;
							 			}
							 			if(null != billEntryDetailsObj.getTotalDisallowances())
							 			{
							 				nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
							 				totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
							 				
							 			}
							 			if(null != billEntryDetailsObj.getNetPayableAmount())
							 			{
							 				
							 				nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
							 			}
							 			
						 				totalList.add(billEntryDetailsObj);
						 				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
						 				{
						 					//totalAmountIfPackAvailable = totalAmount;
						 					totalAmountIfPackAvailable += totalAmount;
						 					totalNonPayableAmt += nonPayable;
						 					totalReasonableDeductionAmt += reasonableDeduction;
						 					totalProportionateDeduction += proportionateDeduction;
						 				}
						 				iSlNo++;
									}
					 				
					 			}
					 			if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("N").equalsIgnoreCase(this.packageFlg))
					 			{
						 			BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
						 			if( iSlNo == 97)
						 			{
						 				billEntryDetailsObj1.setItemName("Sub Total");
						 			}
						 			else
						 			{
						 				billEntryDetailsObj1.setItemName("Sub Total (Sl.No "+Character.toString((char)(iSlNo - (iSlNo-1)))+"to"+Character.toString((char)iSlNo));
						 			}
						 			billEntryDetailsObj1.setItemValue(totalAmount);
						 			//totalItemValueInFooter += billEntryDetailsObj1.getItemValue();
						 			billEntryDetailsObj1.setPerDayAmtProductBased(productLimit);
						 			billEntryDetailsObj1.setAmountAllowableAmount(allowableAmount);
						 			/*Double amount1 = 0d;
						 			amount1 = getDiffOfTwoNumber(totalAmount, productLimit);
						 			Double amount2 = 0d;
						 			amount2 = getDiffOfTwoNumber(nonPayable, reasonableDeduction); */
						 			Double nonPayablePdtBased = 0d;
						 			nonPayablePdtBased = getDiffOfTwoNumber(totalAmount, allowableAmount);
						 			billEntryDetailsObj1.setNonPayableProductBased(nonPayablePdtBased);
						 			billEntryDetailsObj1.setNonPayable(nonPayable);
						 			billEntryDetailsObj1.setProportionateDeduction(proportionateDeduction);
						 			Double totalDisAllowance = nonPayablePdtBased+reasonableDeduction+nonPayable;
						 			billEntryDetailsObj1.setTotalDisallowances(totalDisAllowance);
						 			Double amount1 = getDiffOfTwoNumber(totalAmount, nonPayable);
						 			Double amount2 = getDiffOfTwoNumber(amount1,proportionateDeduction);
						 			billEntryDetailsObj1.setNetPayableAmount(Math.min(allowableAmount, amount2));
						 			totalList.add(billEntryDetailsObj1);
					 			}	
					 		}
						 }
						 /*else if((ReferenceTable.INVESTIGATION_DIAG).equals(billTypeKey))
						 {
							 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
							 billEntryDetailList.setItemNoForView(Double.parseDouble(String.valueOf(i)));
							 billEntryDetailList.setItemName(SHAConstants.INVESTIGATION_DIAG);
							 viewHospitalizationObj.addBeanToList(billEntryDetailList);
						 }*/
						 else if((ReferenceTable.INVESTIGATION_DIAG_WITHIN_HOSPITAL).equals(billTypeKey))
						 {
							 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
							 billEntryDetailList.setItemNoForView(Double.parseDouble(String.valueOf(i)));
							 billEntryDetailList.setItemName(SHAConstants.INVESTIGATION_DIAG);
							 totalList.add(billEntryDetailList);
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			billEntryDetailsObj.setItemName("	a)"+ SHAConstants.INVESTIGATION_DIAG_WITHIN_HOSPITAL);
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				
					 				if(null != billEntryDetailsObj.getItemValue() )
					 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				if(null != billEntryDetailsObj.getNonPayable() )
					 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
					 				if(null != billEntryDetailsObj.getNonPayableProductBased())
					 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
					 				if(null != billEntryDetailsObj.getProportionateDeduction() )
					 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
					 				if(null != billEntryDetailsObj.getReasonableDeduction())
					 					totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
							}
					 	}
						else if((ReferenceTable.INVESTIGATION_DIAG_OUTSIDE_HOSPITAL).equals(billTypeKey))
						 {
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			billEntryDetailsObj.setItemName("	b)"+ SHAConstants.INVESTIGATION_DIAG_OUTSIDE_HOSPITAL);
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				
					 				if(null != billEntryDetailsObj.getItemValue() )
					 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				if(null != billEntryDetailsObj.getNonPayable() )
					 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
					 				if(null != billEntryDetailsObj.getProportionateDeduction() )
					 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
					 				if(null != billEntryDetailsObj.getReasonableDeduction())
					 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
					 				
					 				if(null != billEntryDetailsObj.getNonPayableProductBased())
					 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
					 				
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
							}
					 	}
						// }
						/* else if((ReferenceTable.MEDICINES_CONSUMABLES).equals(billTypeKey))
						 {
							 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
							 //billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
							 billEntryDetailList.setItemNoForView(6.0d);
							 billEntryDetailList.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
							 viewHospitalizationObj.addBeanToList(billEntryDetailList);
						 }*/
						 else if((ReferenceTable.MEDICINES).equals(billTypeKey))
						 {
								 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
								 //billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
								 billEntryDetailList.setItemNoForView(6.0d);
								 billEntryDetailList.setItemName(SHAConstants.MEDICINES_CONSUMABLES);
								 totalList.add(billEntryDetailList);
								 
							 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			billEntryDetailsObj.setItemName("	a)Medicines");
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				if(null != billEntryDetailsObj.getItemValue() )
					 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				if(null != billEntryDetailsObj.getNonPayable() )
					 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
					 				if(null != billEntryDetailsObj.getProportionateDeduction() )
					 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
					 				if(null != billEntryDetailsObj.getReasonableDeduction())
					 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
					 				if(null != billEntryDetailsObj.getNonPayableProductBased())
					 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
					 				
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
						 }
						 else if((ReferenceTable.CONSUMABLES).equals(billTypeKey))
						 {
							 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			billEntryDetailsObj.setItemName("	b)Consumbles");
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				if(null != billEntryDetailsObj.getItemValue() )
					 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				if(null != billEntryDetailsObj.getNonPayable() )
					 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
					 				if(null != billEntryDetailsObj.getProportionateDeduction() )
					 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
					 				if(null != billEntryDetailsObj.getReasonableDeduction())
					 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
					 				if(null != billEntryDetailsObj.getNonPayableProductBased())
					 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
					 				
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
						 }
						 else if((ReferenceTable.IMPLANT_STUNT_VALVE).equals(billTypeKey))
						 {
							 	BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			billEntryDetailsObj.setItemName("	c)ÃŒmplant/Stent/Valve/Pacemaker/Etc");
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				
					 				if(null != billEntryDetailsObj.getItemValue() )
					 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				if(null != billEntryDetailsObj.getNonPayable() )
					 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
					 				if(null != billEntryDetailsObj.getProportionateDeduction() )
					 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
					 				if(null != billEntryDetailsObj.getReasonableDeduction())
					 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
					 				
					 				if(null != billEntryDetailsObj.getNonPayableProductBased())
					 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
					 				
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				
					 				
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
						 }
						
						 /*else if((ReferenceTable.PACKAGE_CHARGES).equals(billTypeKey))
						 {

							 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
							// billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
							 billEntryDetailList.setItemNoForView(8.0d);
							 billEntryDetailList.setItemName(SHAConstants.PACKAGES_CHARGES);
							 viewHospitalizationObj.addBeanToList(billEntryDetailList);
						 }*/
						 else if((ReferenceTable.ANH_PACKAGES).equals(billTypeKey))
						 {
							 
							 BillEntryDetailsDTO billEntryDetailList = new BillEntryDetailsDTO();
								// billEntryDetailList.setItemNo(Long.parseLong(String.valueOf(i)));
							 billEntryDetailList.setItemNoForView(8.0d);
							 billEntryDetailList.setItemName(SHAConstants.PACKAGES_CHARGES);
							 totalList.add(billEntryDetailList);
							 
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			billEntryDetailsObj.setItemName("	a)ANH Package");
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				
					 				if(null != billEntryDetailsObj.getItemValue() )
					 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				if(null != billEntryDetailsObj.getNonPayable() )
					 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
					 				if(null != billEntryDetailsObj.getProportionateDeduction() )
					 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
					 				if(null != billEntryDetailsObj.getReasonableDeduction())
					 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
					 				
					 				if(null != billEntryDetailsObj.getNonPayableProductBased())
					 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
					 				
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
					 		}
						 }
						 else if((ReferenceTable.COMPOSITE_PACKAGES).equals(billTypeKey))
						 {
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			billEntryDetailsObj.setItemName("	b)Composite Package  Over ride  80% /100%");
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				
					 				if(null != billEntryDetailsObj.getItemValue() )
					 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				if(null != billEntryDetailsObj.getNonPayable() )
					 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
					 				if(null != billEntryDetailsObj.getProportionateDeduction() )
					 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
					 				if(null != billEntryDetailsObj.getReasonableDeduction())
					 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
					 				
					 				if(null != billEntryDetailsObj.getNonPayableProductBased())
					 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
					 				
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
					 		}
						 }
						 else if((ReferenceTable.OTHER_PACKAGES).equals(billTypeKey))
						 {
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			billEntryDetailsObj.setItemName("	c)Other packages");
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				
					 				if(null != billEntryDetailsObj.getItemValue() )
					 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				if(null != billEntryDetailsObj.getNonPayable() )
					 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
					 				if(null != billEntryDetailsObj.getProportionateDeduction() )
					 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
					 				if(null != billEntryDetailsObj.getReasonableDeduction())
					 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
					 				
					 				if(null != billEntryDetailsObj.getNonPayableProductBased())
					 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
					 				
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
					 		}
						 }
						 else if((ReferenceTable.PROCEDURES).equals(billTypeKey))
						 {
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
					 			billEntryDetailsObj.setItemNoForView(7.0d);
					 			billEntryDetailsObj.setItemName(SHAConstants.PROCEDURES);
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				if(null != billEntryDetailsObj.getItemValue())
					 				totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				if(null != billEntryDetailsObj.getNonPayable())
					 				totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
					 				if(null != billEntryDetailsObj.getReasonableDeduction())
					 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
					 				if(null != billEntryDetailsObj.getNonPayableProductBased())
					 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
					 				
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
					 		}
						 }
						/* else if((ReferenceTable.MISC_CHARGES).equals(billTypeKey))
						 {
							 //The below block needs to be checked later once the mapping is available.
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
					 			billEntryDetailsObj.setItemNoForView(9.0d);
					 			billEntryDetailsObj.setItemName(SHAConstants.MISC_CHARGES);
					 			viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, masBillType.getKey());
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
					 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				viewHospitalizationObj.addBeanToList(billEntryDetailsObj);
					 			}
					 		//}
						 }*/
						 else if((ReferenceTable.MISC_WITHIN_HOSPITAL).equals(billTypeKey))
						 {
							 BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
					 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
							 billEntryDetailsObject.setItemNoForView(9.0d);
							 billEntryDetailsObject.setItemName(SHAConstants.MISC_CHARGES);
							 totalList.add(billEntryDetailsObject);
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			billEntryDetailsObj.setItemName("	a) Miscellaneous within hospital");
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				
					 				if(null != billEntryDetailsObj.getItemValue() )
					 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				if(null != billEntryDetailsObj.getNonPayable() )
					 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
					 				if(null != billEntryDetailsObj.getProportionateDeduction() )
					 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
					 				if(null != billEntryDetailsObj.getReasonableDeduction())
					 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
					 				
					 				if(null != billEntryDetailsObj.getNonPayableProductBased())
					 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
					 				
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				
					 				/*totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				totalNonPayableAmt += billEntryDetailsObj.getNonPayable();*/
					 				 totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
					 		}
						 }
						 else if((ReferenceTable.MISC_OUTSIDE_HOSPITAL).equals(billTypeKey))
						 {
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			billEntryDetailsObj.setItemName("	b) Miscellaneous outside hospital");
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				
					 				if(null != billEntryDetailsObj.getItemValue() )
					 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				if(null != billEntryDetailsObj.getNonPayable() )
					 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
					 				if(null != billEntryDetailsObj.getProportionateDeduction() )
					 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
					 				if(null != billEntryDetailsObj.getReasonableDeduction())
					 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
					 				
					 				if(null != billEntryDetailsObj.getNonPayableProductBased())
					 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
					 				
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
					 				
					 				/*totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
					 				totalNonPayableAmt += billEntryDetailsObj.getNonPayable();*/
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
					 		}
						 }
						 else if((ReferenceTable.OTHERS).equals(billTypeKey))
						 {
							 if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
						 		{
						 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
						 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
						 			billEntryDetailsObj.setItemNoForView(10.0d);
						 			billEntryDetailsObj.setItemName(SHAConstants.OTHERS);
						 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
						 			if(null != hospObj)
						 			{
						 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
						 				
						 				if(null != billEntryDetailsObj.getItemValue() )
						 					totalAmountIfPackAvailable += billEntryDetailsObj.getItemValue();
						 				if(null != billEntryDetailsObj.getNonPayable() )
						 					totalNonPayableAmt += billEntryDetailsObj.getNonPayable();
						 				if(null != billEntryDetailsObj.getProportionateDeduction() )
						 					totalProportionateDeduction += billEntryDetailsObj.getProportionateDeduction();
						 				if(null != billEntryDetailsObj.getReasonableDeduction())
						 				totalReasonableDeductionAmt +=billEntryDetailsObj.getReasonableDeduction();
						 				
						 				if(null != billEntryDetailsObj.getNonPayableProductBased())
						 					nonPayableProductBasedIfPackageIsAvailable +=  billEntryDetailsObj.getNonPayableProductBased();
						 				
						 				if(null != billEntryDetailsObj.getTotalDisallowances())
						 					totalDisallowancesIfPackageIsAvailable += billEntryDetailsObj.getTotalDisallowances();
						 				
						 				totalList.add(billEntryDetailsObj);
						 			}
						 			else
						 			{
						 				totalList.add(billEntryDetailsObj);
						 			}
						 			//if(isPackageAvailable)
						 			//{
							 			BillEntryDetailsDTO billEntryDetailsObj1 = new BillEntryDetailsDTO();
							 			Double calculatedAmtIfPackIsAvailable = 0d;
							 			Reimbursement reimbursement = billDetailsService.getReimbursementObjectByKey(rodKey);
							 			DBCalculationService dbCalculationService = new DBCalculationService();
							 			Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
					 					reimbursement.getClaim().getIntimation().getInsured().getInsuredId().toString(), reimbursement.getClaim().getIntimation().getPolicy().getKey()
					 					,reimbursement.getClaim().getIntimation().getInsured().getLopFlag());
							 			
							 			
							 			if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("N").equalsIgnoreCase(this.packageFlg))
							 			{
							 				billEntryDetailsObj1.setItemName("Sub Total (Sl no 4,5, 6 & 7,8, 9 & 10)");
							 				billEntryDetailsObj1.setItemValue(totalAmountIfPackAvailable);
								 			
								 			Double productlimit = insuredSumInsured *(50f/100f);
								 			nonpackTotalProductPerDayAmtInFooter +=productlimit;
								 			billEntryDetailsObj1.setPerDayAmtProductBased(productlimit);
								 			billEntryDetailsObj1.setNonPayable(totalNonPayableAmt);
								 			billEntryDetailsObj1.setReasonableDeduction(totalReasonableDeductionAmt);
								 			
								 			Double amount1 = getDiffOfTwoNumber(totalAmountIfPackAvailable, totalNonPayableAmt);
								 			Double amount2 = getDiffOfTwoNumber(amount1,totalReasonableDeductionAmt);
								 			
								 			Double allowableAmt = Math.min(amount2, productlimit);
									 		billEntryDetailsObj1.setAmountAllowableAmount(allowableAmt);
									 		
									 		billEntryDetailsObj1.setNonPayableProductBased(getDiffOfTwoNumber(billEntryDetailsObj1.getItemValue() , billEntryDetailsObj1.getAmountAllowableAmount()));
									 		billEntryDetailsObj1.setProportionateDeduction(totalProportionateDeduction);
									 		Double proportionateDeduc = billEntryDetailsObj1.getProportionateDeduction();
							 				Double totalDisAllowances = billEntryDetailsObj1.getNonPayableProductBased() + billEntryDetailsObj1.getReasonableDeduction() + 
								 					billEntryDetailsObj1.getNonPayable() + proportionateDeduc;
								 			billEntryDetailsObj1.setTotalDisallowances(totalDisAllowances);
								 			Double amount3 = 0d;
								 			amount3 = getDiffOfTwoNumber(totalAmountIfPackAvailable,totalNonPayableAmt );
								 			Double amount4 = 0d;
								 			amount4 = getDiffOfTwoNumber(amount3,totalReasonableDeductionAmt );
								 			billEntryDetailsObj1.setNetPayableAmount(Math.min(billEntryDetailsObj1.getAmountAllowableAmount(), amount4));
								 			nonpackTotalItemValueInFooter += billEntryDetailsObj1.getItemValue();
								 			nonpackTotalAllowableAmtInFooter += billEntryDetailsObj1.getAmountAllowableAmount();
								 			nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsObj1.getNonPayableProductBased();
								 			nonpackTotalNonPayableManualInFooter += billEntryDetailsObj1.getNonPayable();
								 			nonpackTotalProportionateDedInFooter += billEntryDetailsObj1.getProportionateDeduction();
								 			nonpackTotalReasonableDeductionInFooter += billEntryDetailsObj1.getReasonableDeduction();
								 			nonpackTotalDisallowancesInFooter += billEntryDetailsObj1.getTotalDisallowances();
								 			nonpackTotalNetPayableInFooter += billEntryDetailsObj1.getNetPayableAmount();
							 			}
							 			else
							 			{
							 				billEntryDetailsObj1.setItemName("Sub Total (Sl no 3 to 10)");
							 				billEntryDetailsObj1.setItemValue(totalAmountIfPackAvailable);
							 				
								 			billEntryDetailsObj1.setNonPayableProductBased(nonPayableProductBasedIfPackageIsAvailable);
								 			billEntryDetailsObj1.setNonPayable(totalNonPayableAmt);
								 			billEntryDetailsObj1.setReasonableDeduction(totalReasonableDeductionAmt);
								 			billEntryDetailsObj1.setProportionateDeduction(totalProportionateDeduction);
								 			Double amount1 = 0d;
								 			amount1 = getDiffOfTwoNumber(totalAmountIfPackAvailable, nonPayableProductBasedIfPackageIsAvailable);
								 			Double amount2 = 0d;
								 			amount2 = getDiffOfTwoNumber(amount1, totalNonPayableAmt);
								 			Double amount3 = 0d;
								 			amount3 = getDiffOfTwoNumber(amount2 ,totalProportionateDeduction );
								 			calculatedAmtIfPackIsAvailable = getDiffOfTwoNumber(amount3, totalReasonableDeductionAmt);
								 			//billEntryDetailsObj1.setProportionateDeduction(null);
								 			billEntryDetailsObj1.setTotalDisallowances(totalDisallowancesIfPackageIsAvailable);
								 			
								 			if(null != billEntryDetailsObj1.getItemValue())
								 			packTotalItemValueInFooter += billEntryDetailsObj1.getItemValue();
								 			if(null != billEntryDetailsObj1.getNonPayableProductBased())
								 			packTotalNonPayablePdtBasedInFooter += billEntryDetailsObj1.getNonPayableProductBased();
								 			if(null != billEntryDetailsObj1.getNonPayable())
								 			packTotalNonPayableManualInFooter += billEntryDetailsObj1.getNonPayable(); 
								 			if(null != billEntryDetailsObj1.getProportionateDeduction())
								 			packTotalProportionateDedInFooter += billEntryDetailsObj1.getProportionateDeduction();
								 			if(null != billEntryDetailsObj1.getReasonableDeduction())
								 			packTotalReasonableDeductionInFooter += billEntryDetailsObj1.getReasonableDeduction();
								 			if(null != billEntryDetailsObj1.getTotalDisallowances())
								 			packTotalDisallowancesInFooter += billEntryDetailsObj1.getTotalDisallowances();
								 			if(null != billEntryDetailsObj1.getNetPayableAmount())
								 			packTotalNetPayableInFooter += billEntryDetailsObj1.getNetPayableAmount();
							 			}
							 			
							 			
							 		//	billEntryDetailsObj1.setNetPayableAmount(Math.min(allowableAmt, totalDisAllowances));
						 				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("N").equalsIgnoreCase(this.packageFlg))
						 					totalList.add(billEntryDetailsObj1);
							 			
							 			if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
							 			{
							 				BillEntryDetailsDTO billEntryDetailsObject = new BillEntryDetailsDTO();
							 				billEntryDetailsObject.setItemName(SHAConstants.PACKAGE_RESTRICTIONS);
							 				Double productLimit = insuredSumInsured*(75f/100f);
							 				billEntryDetailsObject.setPerDayAmtProductBased(productLimit);
							 				/*if(null != billEntryDetailsObject.getAmountAllowableAmount())
							 				totalAllowableAmtInFooter += billEntryDetailsObject.getAmountAllowableAmount();
							 				if(null != billEntryDetailsObject.getNonPayableProductBased())
							 				totalNonPayablePdtBasedInFooter += billEntryDetailsObject.getNonPayableProductBased();
							 				if(null != billEntryDetailsObject.getNonPayable())
											totalNonPayableManualInFooter += billEntryDetailsObject.getNonPayable();
							 				if(null != billEntryDetailsObject.getProportionateDeduction())
											totalProportionateDedInFooter += billEntryDetailsObject.getProportionateDeduction();
							 				if(null != billEntryDetailsObject.getReasonableDeduction())
											totalReasonableDeductionInFooter += billEntryDetailsObject.getReasonableDeduction();
							 				if(null != billEntryDetailsObject.getTotalDisallowances())
											totalDisallowancesInFooter += billEntryDetailsObject.getTotalDisallowances();
							 				if(null != billEntryDetailsObject.getNetPayableAmount())
							 				totalNetPayableInFooter += billEntryDetailsObject.getNetPayableAmount();*/
							 				
							 				if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
								 			{
								 				/*Double amountPdtBasedNonPayable = getDiffOfTwoNumber( billEntryDetailsObj1.getNonPayableProductBased(),proportionateDeduc);
								 				Double amount6 = getDiffOfTwoNumber(amount1, amountPdtBasedNonPayable);
								 				Double amount7 = getDiffOfTwoNumber(amount6, billEntryDetailsObj1.getReasonableDeduction());*/
								 				billEntryDetailsObj1.setAmountAllowableAmount(Math.min(billEntryDetailsObject.getPerDayAmtProductBased(), calculatedAmtIfPackIsAvailable));
								 				
								 				totalList.add(billEntryDetailsObj1);
								 				Double amountallowable = Math.min(productLimit, billEntryDetailsObj1.getAmountAllowableAmount());
								 				billEntryDetailsObject.setAmountAllowableAmount(amountallowable);
								 				if(null != billEntryDetailsObject.getAmountAllowableAmount())
								 				{
									 				packTotalAllowableAmtInFooter += billEntryDetailsObject.getAmountAllowableAmount();
								 				}
								 				billEntryDetailsObject.setNonPayableProductBased(getDiffOfTwoNumber(amountallowable,billEntryDetailsObj1.getAmountAllowableAmount()));
								 				if(null != billEntryDetailsObject.getNonPayableProductBased())
								 				{
								 					packTotalNonPayablePdtBasedInFooter +=  billEntryDetailsObject.getNonPayableProductBased();
								 				}
								 				billEntryDetailsObject.setNonPayable(totalNonPayableAmt);
								 				if(null != billEntryDetailsObject.getNonPayable())
								 				{
								 					packTotalNonPayableManualInFooter += billEntryDetailsObject.getNonPayable();
								 				}
								 				billEntryDetailsObject.setProportionateDeduction(totalProportionateDeduction);
								 				if(null != billEntryDetailsObject.getProportionateDeduction())
								 				{
								 					packTotalProportionateDedInFooter += billEntryDetailsObject.getProportionateDeduction();
								 				}
								 				billEntryDetailsObject.setReasonableDeduction(totalReasonableDeductionAmt);
								 				if(null != billEntryDetailsObject.getReasonableDeduction())
								 				{
								 					packTotalReasonableDeductionInFooter += billEntryDetailsObject.getReasonableDeduction();
								 				}
								 				
								 				billEntryDetailsObject.setTotalDisallowances(totalNonPayableAmt + totalProportionateDeduction + totalReasonableDeductionAmt+billEntryDetailsObject.getNonPayableProductBased());
								 				
								 				if(null != billEntryDetailsObject.getTotalDisallowances())
								 				{
								 					packTotalDisallowancesInFooter += billEntryDetailsObject.getTotalDisallowances();
								 				}
								 				billEntryDetailsObject.setNetPayableAmount(billEntryDetailsObject.getAmountAllowableAmount());
								 				if(null != billEntryDetailsObject.getNonPayableProductBased())
								 				{
								 					packTotalNetPayableInFooter += billEntryDetailsObject.getAmountAllowableAmount();
								 				}
								 				totalList.add(billEntryDetailsObject);
								 			}
							 				
							 				
							 				
							 				
							 				
							 			}
			
						 	}
						 }
						 else if((ReferenceTable.AMBULANCE_FEES).equals(billTypeKey))
						 {
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
					 			billEntryDetailsObj.setItemNoForView(11.0d);
					 			billEntryDetailsObj.setItemName(SHAConstants.AMBULANCE_FEES);
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				if(null != billEntryDetailsObj.getItemValue())
					 				{
					 					packTotalItemValueInFooter +=  billEntryDetailsObj.getItemValue();
					 					nonpackTotalItemValueInFooter += billEntryDetailsObj.getItemValue();
					 				}
					 				if(null != billEntryDetailsObj.getPerDayAmtProductBased())
					 				{
					 					nonpackTotalProductPerDayAmtInFooter += billEntryDetailsObj.getPerDayAmtProductBased();
					 				}
					 				if(null != billEntryDetailsObj.getAmountAllowableAmount())
					 				{
					 					packTotalAllowableAmtInFooter += billEntryDetailsObj.getAmountAllowableAmount();
					 					nonpackTotalAllowableAmtInFooter += billEntryDetailsObj.getAmountAllowableAmount();
					 				}
					 				if(null != billEntryDetailsObj.getNonPayableProductBased())
					 				{
					 					packTotalNonPayablePdtBasedInFooter += billEntryDetailsObj.getNonPayableProductBased();
					 					nonpackTotalNonPayablePdtBasedInFooter += billEntryDetailsObj.getNonPayableProductBased();
					 				}
					 				if(null != billEntryDetailsObj.getNonPayable())
					 				{
					 					packTotalNonPayableManualInFooter += billEntryDetailsObj.getNonPayable();
					 					nonpackTotalNonPayableManualInFooter += billEntryDetailsObj.getNonPayable();
					 				}
					 				if(null != billEntryDetailsObj.getProportionateDeduction())
					 				{
					 					packTotalProportionateDedInFooter += billEntryDetailsObj.getProportionateDeduction();
					 					nonpackTotalProportionateDedInFooter += billEntryDetailsObj.getProportionateDeduction();
					 				}
					 				if(null != billEntryDetailsObj.getReasonableDeduction())
					 				{
					 					packTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
					 					nonpackTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
					 				}
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 				{
					 					packTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
					 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
					 				}
					 				if(null != billEntryDetailsObj.getNetPayableAmount())
					 				{
					 					packTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
					 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
					 				}
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
					 		}
						 }
						 else if((ReferenceTable.HOSPITAL_DISCOUNT).equals(billTypeKey))
						 {
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
					 			billEntryDetailsObj.setItemNoForView(12.0d);
					 			billEntryDetailsObj.setItemName(SHAConstants.HOSPITAL_DISCOUNT);
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				if(null != billEntryDetailsObj.getItemValue())
					 				{
					 					packTotalItemValueInFooter -= billEntryDetailsObj.getItemValue();
					 					nonpackTotalItemValueInFooter -= billEntryDetailsObj.getItemValue();
					 				}
					 				if(null != billEntryDetailsObj.getPerDayAmtProductBased())
					 					nonpackTotalProductPerDayAmtInFooter -= billEntryDetailsObj.getPerDayAmtProductBased();
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 				{
					 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
					 				}
					 				if(null != billEntryDetailsObj.getNetPayableAmount())
					 				{
					 					packTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
					 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
					 				}
					 				
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
					 		}
						 }
						 else if((ReferenceTable.DEDUCTIONS).equals(billTypeKey))
						 {
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			//billEntryDetailsObj.setItemNo(Long.parseLong(String.valueOf(i)));
					 			billEntryDetailsObj.setItemNoForView(13.0d);
					 			billEntryDetailsObj.setItemName(SHAConstants.DEDUCTIONS);
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				if(null != billEntryDetailsObj.getReasonableDeduction())
					 				{
					 					packTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
					 					nonpackTotalReasonableDeductionInFooter += billEntryDetailsObj.getReasonableDeduction();
					 				}
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
					 				if(null != billEntryDetailsObj.getNetPayableAmount())
					 				{
					 					packTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
					 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
					 				}
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
					 		}
						 }
						 else if((ReferenceTable.NETWORK_HOSPITAL_DISCOUNT).equals(billTypeKey))
						 {
					 		if(null != newHospitalizationList && !newHospitalizationList.isEmpty())
					 		{
					 			BillEntryDetailsDTO billEntryDetailsObj = new BillEntryDetailsDTO();
					 			billEntryDetailsObj.setItemNoForView(12.0d);
					 			billEntryDetailsObj.setItemName(SHAConstants.NETWORK_HOSPITAL_DISCOUNT);
					 			Hospitalisation hospObj = getHospDetailsForBillType(newHospitalizationList, billTypeKey);
					 			if(null != hospObj)
					 			{
					 				billEntryDetailsObj = populateHospitalizationDetails(billEntryDetailsObj, hospObj);
					 				if(null != billEntryDetailsObj.getItemValue())
					 				{
					 					packTotalItemValueInFooter -= billEntryDetailsObj.getItemValue();
					 					nonpackTotalItemValueInFooter -= billEntryDetailsObj.getItemValue();
					 				}
					 				if(null != billEntryDetailsObj.getPerDayAmtProductBased())
					 					nonpackTotalProductPerDayAmtInFooter -= billEntryDetailsObj.getPerDayAmtProductBased();
					 				if(null != billEntryDetailsObj.getTotalDisallowances())
					 				{
					 					nonpackTotalDisallowancesInFooter += billEntryDetailsObj.getTotalDisallowances();
					 				}
					 				if(null != billEntryDetailsObj.getNetPayableAmount())
					 				{
					 					packTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
					 					nonpackTotalNetPayableInFooter += billEntryDetailsObj.getNetPayableAmount();
					 				}
					 				
					 				totalList.add(billEntryDetailsObj);
					 			}
					 			else
					 			{
					 				totalList.add(billEntryDetailsObj);
					 			}
					 		}
						 }
						 
					 }
					 
//					 if(null != this.packageFlg && !("").equalsIgnoreCase(this.packageFlg) && ("Y").equalsIgnoreCase(this.packageFlg))
//					 {
//						 viewHospitalizationObj.calculateTotalForSeniorCitizen(packTotalItemValueInFooter ,
//									0d , packTotalAllowableAmtInFooter ,
//									packTotalNonPayablePdtBasedInFooter ,
//									packTotalNonPayableManualInFooter ,
//									packTotalProportionateDedInFooter ,
//									packTotalReasonableDeductionInFooter ,
//									packTotalDisallowancesInFooter ,
//									packTotalNetPayableInFooter );	
//					 }
//					 else
//					 {
//						 viewHospitalizationObj.calculateTotalForSeniorCitizen(nonpackTotalItemValueInFooter ,
//									nonpackTotalProductPerDayAmtInFooter , nonpackTotalAllowableAmtInFooter ,
//									nonpackTotalNonPayablePdtBasedInFooter ,
//									nonpackTotalNonPayableManualInFooter ,
//									nonpackTotalProportionateDedInFooter ,
//									nonpackTotalReasonableDeductionInFooter ,
//									nonpackTotalDisallowancesInFooter ,
//									nonpackTotalNetPayableInFooter );	
//					 }
					
				 }
				 
				 return totalList;
			}
			
			
			public static Double getCorrectAmt(Double amt) {
				if(amt != null) {
					return amt;
				}
				return 0d;
			}
			
			
			public static Double getCorrectLongAmt(Long amt) {
				if(amt != null) {
					return amt.doubleValue();
				}
				return 0d;
			}
			
			public static Map<Long, String> getDocumentTypeList()  {
				Map<Long, String> mappedValues = new WeakHashMap<Long, String>();
				mappedValues.put(34L, "Duly Filled and Signed Claim Form");
				mappedValues.put(35L, "Diagnosis Report");
				mappedValues.put(36L, "Medical Prescription");
				mappedValues.put(37L, "Break-up Bill");
				mappedValues.put(38L, "Payment Receipt");
				return mappedValues;
			}
			
			public static boolean isDateOfIntimationWithPolicyRange(Date policyFrmDate , Date policyToDate, Date selectedDate){
				
				if((policyFrmDate.equals(selectedDate) || policyFrmDate.before(selectedDate)) 
					&& (policyToDate.equals(selectedDate) || policyToDate.after(selectedDate)))
				{
					return true;
				}	
				else
				{
					return false;
				}
			}
			
	public static boolean isOMPDateOfIntimationWithPolicyRange(Date policyFrmDate , Date policyToDate, Date selectedDate){
				
				if((policyFrmDate.equals(selectedDate) || policyFrmDate.before(selectedDate)) 
					&& (policyToDate.equals(selectedDate) || policyToDate.after(selectedDate)))
				{
					return true;
				}else if(policyFrmDate.compareTo(selectedDate) == 0){
					return true;
				}if(policyToDate.compareTo(selectedDate) == 0){
					return true;
				}
				else
				{
					return false;
				}
			}
			
			public static boolean checkAftStartDate(Date policyFrmDate, Date selectedDate) {
				Date addDays = addDays(policyFrmDate, 30);
				if(selectedDate.after(addDays)) 
				{
					return true;
				}	
				else
				{
					return false;
				}
			}
			
			public static Date addDays(Date d, int days) {
				Date date = new Date();
				date.setTime(d.getTime() +  days * 1000L * 60L * 60L * 24L);
		        return date;
		    }
			
			public static Date addYears(Date d, int years) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				cal.add(Calendar.YEAR, years);
				Date revisedDate = cal.getTime();
		        return revisedDate;
		    }
			
			public static boolean checkAfterYears(Date policyEndDate, int years) {
				Date addDays = addYears(policyEndDate, years);
				if(addDays.after(new Date())) {
					return true;
				}	
				else{
					return false;
				}
			}
			
			
			public static Boolean isValidEmail(String strEmail) {
				String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
				Pattern validEmailPattern = Pattern.compile(emailPattern);
				Matcher validMatcher = validEmailPattern.matcher(strEmail);
				return validMatcher.matches();
			}
			
			public static Long getDiffDays(Date fromDate, Date admissionDate){
				if(fromDate != null && admissionDate != null){
					long diff = Math.abs(admissionDate.getTime()
							- fromDate.getTime());
					long diffDays = diff / (24 * 60 * 60 * 1000);
					
					return diffDays;
				}
				return 0l;
				
			}
			
			public static Long getDiffDaysWithNegative(Date fromDate, Date admissionDate){
				if(fromDate != null && admissionDate != null){
					long diff = admissionDate.getTime()
							- fromDate.getTime();
					long diffDays = diff / (24 * 60 * 60 * 1000);
					
					return diffDays;
				}
				return 0l;
				
			}
			
		public static String getProvisionAmtInput(Claim claim, String hospitalName, String provisionAmt) {
			String input = "{\"ClaimType\": " + "\"" + claim.getClaimType().getValue() + "\", "
	                + "\"HospitalName\": " + "\"" + hospitalName + "\", "
	                + "\"HospitalType\": " + "\"" + claim.getIntimation().getHospitalType().getValue() + "\", "
	                + "\"RiskSysID\": " + "\"" + claim.getIntimation().getInsured().getInsuredId().toString() + "\", "
	                + "\"InsuredName\": " + "\"" + claim.getIntimation().getInsured().getInsuredName() + "\", "
	                + "\"IntimationNo\": " + "\"" + claim.getIntimation().getIntimationId() + "\", "
	                + "\"PolicyNo\": " + "\"" + claim.getIntimation().getPolicy().getPolicyNumber() + "\", "
	                + "\"ProvisionAmount\": " + "\"" + provisionAmt + "\" } ";
			
			return input;
		}
		
		
	  public static String getTruncateString(String word,Integer length){
		
		   if(word != null){
			
			   return word.substring(0, length);
			
		   }
		   
		   return null;
			
	}
	  
	  public static String getUserNameForDB(String userName){
		  if (userName != null
					&& userName.length() > 15) {
				String truncateString = getTruncateString(userName, 15);
				return truncateString;
			}
		  
		  return userName;
	  }
	  
	  public static String getTruncateWord(String word, Integer startIndex, Integer endIndex){
		  
		  if(word != null){
			  return word.substring(startIndex, endIndex);
		  }
		  return null;
	  }

		public static String getLockPolicyInput(Claim claim, String hospitalCode) {
			String input = "{\"PolicyNo\": " + "\"" + claim.getIntimation().getPolicy().getPolicyNumber() + "\", "
	                + "\"PolicyEndNo\": " + "\"" + claim.getIntimation().getPolicy().getEndorsementNumber() + "\", "
	                + "\"IntimationNo\": " + "\"" + claim.getIntimation().getIntimationId() + "\", "
	                + "\"ClaimNo\": " + "\"" + claim.getClaimId() + "\", "
	                + "\"HealthIDCard\": " + "\"" + claim.getIntimation().getInsured().getHealthCardNumber() + "\", "
	                + "\"RiskSysID\": " + "\"" + claim.getIntimation().getInsured().getInsuredId().toString() + "\", "
	                + "\"ReasonForAdmission\": " + "\"" + claim.getIntimation().getAdmissionReason() + "\", "
	                + "\"AdmissionDate\": " + "\"" + String.valueOf(formatDate(claim.getIntimation().getAdmissionDate()))   + "\", "
	                + "\"HospitalCode\": " + "\"" + hospitalCode + "\" } ";
			
			return input;
		}

			/**
			 * Method to convert gregorian calendar to util date.
			 * This is used when date is submitted to BPM services.
			 * **/

		public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(java.util.Date date) {
			if(date == null) {
			return null;
			} else {
				
				DatatypeFactory df = null;
				XMLGregorianCalendar xmlGregorianCal = null;
				try {
					df = DatatypeFactory.newInstance();
					GregorianCalendar gregorianCal = new GregorianCalendar();
					gregorianCal.setTimeInMillis(date.getTime());
					xmlGregorianCal = df.newXMLGregorianCalendar(gregorianCal);
				} catch (DatatypeConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return xmlGregorianCal;
		
			}
			}
		
		public static Boolean getProcedureStatus(ProcedureDTO procedureDTO) {

			Boolean isPaymentAvailable = true;
			if (procedureDTO.getConsiderForPaymentFlag() != null) {
				isPaymentAvailable = procedureDTO
						.getConsiderForPaymentFlag().toLowerCase()
						.equalsIgnoreCase("y") ? true : false;
			} else {
				isPaymentAvailable = false;
				if(procedureDTO.getNewProcedureFlag() != null && procedureDTO.getNewProcedureFlag().equals(1)) {
					isPaymentAvailable = true;
				}
			}
				
			if (isPaymentAvailable) {
				if (procedureDTO.getExclusionDetails() != null
						&& procedureDTO.getExclusionDetails()
								.getValue() != null
						&& !procedureDTO.getExclusionDetails()
								.getValue().toString().toLowerCase()
								.equalsIgnoreCase("not applicable")) {
					isPaymentAvailable = false;
				}
			}
				
			return isPaymentAvailable;
		
		}
		
		
		public static Boolean getDiagnosisStatus(DiagnosisDetailsTableDTO pedValidationDTO) {

			Boolean isPaymentAvailable = (pedValidationDTO
					.getConsiderForPaymentFlag() != null && pedValidationDTO
							.getConsiderForPaymentFlag().toLowerCase()
					.equalsIgnoreCase("y")) ? true : false;
			if (isPaymentAvailable) {
				List<PedDetailsTableDTO> pedList = pedValidationDTO
						.getPedList();
				if (!pedList.isEmpty()) {
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
                        
						List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO
								.getExclusionAllDetails();
						String paymentFlag = "y";
						if(exclusionAllDetails != null){
							for (ExclusionDetails exclusionDetails : exclusionAllDetails) {
								if (null != pedDetailsTableDTO
										.getExclusionDetails()
										&& exclusionDetails
												.getKey()
												.equals(pedDetailsTableDTO
														.getExclusionDetails()
														.getId())) {
									paymentFlag = exclusionDetails
											.getPaymentFlag();
								}
							}
						}

						if (paymentFlag.toLowerCase().equalsIgnoreCase(
								"n")) {
							isPaymentAvailable = false;
							break;
						}
					}
				}
			}
			return isPaymentAvailable;
		}
		
		public static Boolean isUserAvailable(String[] userList, String user){
			
			if(Arrays.asList(userList).contains(user)){
				return true;
			}
			return false;
		}
		
		public static WeakHashMap<String, Object> getEscalateValidation(String[] userList){
			
			WeakHashMap<String, Object>  userMap = new WeakHashMap<String, Object>();
			
		     userMap.put(SHAConstants.CMA6,isUserAvailable(userList, SHAConstants.CMA6));
		     userMap.put(SHAConstants.CMA5, isUserAvailable(userList, SHAConstants.CMA5));
			 userMap.put(SHAConstants.CMA4, isUserAvailable(userList, SHAConstants.CMA4));
			 userMap.put(SHAConstants.CMA3, isUserAvailable(userList, SHAConstants.CMA3));
			 userMap.put(SHAConstants.CMA2, isUserAvailable(userList, SHAConstants.CMA2));
			 userMap.put(SHAConstants.CMA1, isUserAvailable(userList, SHAConstants.CMA1));
			 
			 userMap.put(SHAConstants.RMA6,	isUserAvailable(userList, SHAConstants.RMA6));
		     userMap.put(SHAConstants.RMA5, isUserAvailable(userList, SHAConstants.RMA5));
			 userMap.put(SHAConstants.RMA4, isUserAvailable(userList, SHAConstants.RMA4));
			 userMap.put(SHAConstants.RMA3, isUserAvailable(userList, SHAConstants.RMA3));
			 userMap.put(SHAConstants.RMA2, isUserAvailable(userList, SHAConstants.RMA2));
			 userMap.put(SHAConstants.RMA1, isUserAvailable(userList, SHAConstants.RMA1));
			 
			 
			 return userMap;
		}
		
		
		
		public static Integer getEntitlementNoOfDays(List<UploadDocumentDTO> uploadDTO) {
			Integer entitlementDays = 0;
			for (UploadDocumentDTO uploadDocumentDTO : uploadDTO) {
				List<BillEntryDetailsDTO> billEntryDetailList = uploadDocumentDTO.getBillEntryDetailList();
				if(billEntryDetailList != null &&  !billEntryDetailList.isEmpty()) {
					for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailList) {
						if(billEntryDetailsDTO.getCategory() != null && billEntryDetailsDTO.getCategory().getId() != null && (billEntryDetailsDTO.getCategory().getId().equals(8l) || billEntryDetailsDTO.getCategory().getId().equals(10l))) {
							entitlementDays += (billEntryDetailsDTO.getNoOfDaysAllowed() != null ? billEntryDetailsDTO.getNoOfDaysAllowed().intValue() : 0) ;
						}
					}
				}
			}
			return entitlementDays;
		}
		
		public static WeakHashMap uploadFileToDMS(String filePath)
		{
			WeakHashMap uploadStatus = null;
			File file = new File(filePath);
			byte[] fileAsbyteArray;
			try {
				fileAsbyteArray = FileUtils.readFileToByteArray(file);
				if (null != fileAsbyteArray) {
					Path p = Paths.get(filePath);
					String fileName = p.getFileName().toString();
					
					uploadStatus = SHAFileUtils.sendGeneratedFileToDMS(fileName, fileAsbyteArray, file);
					//uploadStatus = SHAFileUtils.sendFileToDMSServer(fileName, fileAsbyteArray);
					Boolean flagUploadSuccess = new Boolean(""
							+ uploadStatus.get("status"));
					if (flagUploadSuccess.booleanValue()) {
						//String token = "" + uploadStatus.get("fileKey");
						uploadStatus.put("fileName",fileName);
						return uploadStatus;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				fileAsbyteArray = null;
			}
			return uploadStatus;
		}
		
		@SuppressWarnings("unchecked")
		public static Preauth getPreauthClaimKey(EntityManager entityManager,Long claimKey) {
			Query query = entityManager.createNamedQuery("Preauth.findByClaimKey");
			query.setParameter("claimkey", claimKey);
			List<Preauth> preauthList = (List<Preauth>) query.getResultList();
			
			if(preauthList != null && ! preauthList.isEmpty()){
				entityManager.refresh(preauthList.get(0));
				return preauthList.get(0);
			}
			return null;
		}

		public static String uploadGeneratedLetterToDMS(EntityManager entityManager,WeakHashMap dataMap)
		{
			WeakHashMap fileUploadMap = SHAUtils.uploadFileToDMS((String)dataMap.get("filePath"));
			if(null != fileUploadMap && !fileUploadMap.isEmpty())
			{
				String docToken = (String)fileUploadMap.get("fileKey");
				String fileName = (String)fileUploadMap.get("fileName");
				dataMap.put("fileKey", docToken);
				dataMap.put("fileName", fileName);
				dataMap.put("fileSize", (Long)fileUploadMap.get("fileSize"));
				return populateDocumentDetailsObject(entityManager , dataMap);
			}
			else
			{
				return null;
			}   
		}
		
		public static String populateDocumentDetailsObject(EntityManager entityManager,WeakHashMap fileUploadMap)
		{
			/*EntityTransaction tx = entityManager.getTransaction();
			tx.begin();*/
//			entityManager.getTransaction().begin();
			DocumentDetails docDetails = new DocumentDetails();
			String docToken = (String)fileUploadMap.get("fileKey");
			docDetails.setDocumentToken((null != docToken && !docToken.isEmpty())? Long.parseLong(docToken):null);
			docDetails.setIntimationNumber((String)fileUploadMap.get("intimationNumber"));
			docDetails.setClaimNumber((String)fileUploadMap.get("claimNumber"));
			docDetails.setCashlessNumber((String)fileUploadMap.get("cashlessNumber"));
			docDetails.setReimbursementNumber((String)fileUploadMap.get("reimbursementNumber"));
			docDetails.setDocumentType((String)fileUploadMap.get("docType"));
			docDetails.setDocumentSource((String)fileUploadMap.get("docSources"));
			docDetails.setSfFileSize((Long)fileUploadMap.get("fileSize"));
			docDetails.setSfFileName((String)fileUploadMap.get("fileName"));
			
			if(((String)fileUploadMap.get("docSources")) != null &&((String)fileUploadMap.get("docSources")).equalsIgnoreCase(SHAConstants.FINANCIAL_APPROVER)){
				docDetails.setDocVersion(String.valueOf(fileUploadMap.get("version")));
			}	
			
			docDetails.setCreatedBy((String)fileUploadMap.get("createdBy"));
			docDetails.setDocSubmittedDate((new Timestamp(System
					.currentTimeMillis())));
			docDetails.setDocAcknowledgementDate((new Timestamp(System
					.currentTimeMillis())));
			//tx.commit();
			entityManager.persist(docDetails);
			entityManager.flush();
			entityManager.clear();
//			entityManager.getTransaction().commit();
			System.out.println("Doc Details Transaction Completed...");
			return docToken;
		}
		
		public static DiagnosisDetailsTableDTO getDialysisDiagnosisDTO(List<DiagnosisDetailsTableDTO> dtoList) {
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : dtoList) {
				if(diagnosisDetailsTableDTO.getSublimitName() != null && diagnosisDetailsTableDTO.getSublimitName().getName() != null && diagnosisDetailsTableDTO.getSublimitName().getName().toLowerCase().toString().equalsIgnoreCase(SHAConstants.DIALYSIS) && diagnosisDetailsTableDTO.getConsiderForPayment() != null && diagnosisDetailsTableDTO.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)) {
					return diagnosisDetailsTableDTO;
				}
			}
			return null;
		}
		
		public static ProcedureDTO getDialysisProcedureDTO(List<ProcedureDTO> dtoList) {
			for (ProcedureDTO procedureDTO : dtoList) {
				if(procedureDTO.getSublimitName() != null && procedureDTO.getSublimitName().getName() != null && procedureDTO.getSublimitName().getName().toLowerCase().equalsIgnoreCase(SHAConstants.DIALYSIS) && procedureDTO.getConsiderForPayment() != null && procedureDTO.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)) {
					return procedureDTO;
				}
			}
			return null;
		}
		
		public static String getBedsInput(String bedsCount, String hospitalCode) {
			String input = "{\"hosCode\": " + "\"" + hospitalCode + "\", "
	                + "\"bedCount\": " + "\"" + bedsCount + "\" } ";
			return input;
		}
		
		public static BeanItemContainer<SelectValue> getSelectValueForPriority(){
			BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
			SelectValue selectValue = new SelectValue();
			selectValue.setId(1l);
			selectValue.setValue(SHAConstants.VIP_CUSTOMER);
			SelectValue selectValue2 = new SelectValue();
			selectValue2.setId(2l);
			selectValue2.setValue(SHAConstants.SENIOR_CITIZEN);
			SelectValue selectValue3 = new SelectValue();
			selectValue3.setId(3l);
			selectValue3.setValue(SHAConstants.ALL);
			SelectValue selectValue4 = new SelectValue();
			selectValue4.setId(4l);
			selectValue4.setValue(SHAConstants.NORMAL);
			container.addBean(selectValue3);
			container.addBean(selectValue4);	
			container.addBean(selectValue2);
			container.addBean(selectValue);
			
			container.sort(new Object[] {"value"}, new boolean[] {true});
			
			return container;
		}
		
		public static BeanItemContainer<SelectValue> getSelectValueForPriorityIRDA(){
			BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
			SelectValue selectValue2 = new SelectValue();
			selectValue2.setId(2l);
			selectValue2.setValue(SHAConstants.SENIOR_CITIZEN);
			SelectValue selectValue3 = new SelectValue();
			selectValue3.setId(3l);
			selectValue3.setValue(SHAConstants.ALL);
			SelectValue selectValue4 = new SelectValue();
			selectValue4.setId(4l);
			selectValue4.setValue(SHAConstants.NORMAL);
			container.addBean(selectValue3);
			container.addBean(selectValue4);	
			container.addBean(selectValue2);
			container.sort(new Object[] {"value"}, new boolean[] {true});
			
			return container;
		}
		
		public static BeanItemContainer<SelectValue> getSelectValueForPriorityNew(){
			BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
			
			SelectValue selectValue3 = new SelectValue();
			selectValue3.setId(1l);
			selectValue3.setValue(SHAConstants.ALL);
			SelectValue selectValue4 = new SelectValue();
			selectValue4.setId(2l);
			selectValue4.setValue(SHAConstants.NORMAL);
			SelectValue selectValue2 = new SelectValue();
			selectValue2.setId(3l);
			selectValue2.setValue(SHAConstants.CRM_FLAGGED);
			container.addBean(selectValue3);
			container.addBean(selectValue4);	
			container.addBean(selectValue2);
			container.sort(new Object[] {"value"}, new boolean[] {true});
			
			return container;
		}
		
		public static BeanItemContainer<SelectValue> getHospitalTypeContainer(){
			BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
			SelectValue selectValue = new SelectValue();
			selectValue.setId(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID);
			selectValue.setValue(SHAConstants.NETWORK_HOSPITAL_TYPE);
			SelectValue selectValue1 = new SelectValue();
			selectValue1.setId(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID);
			selectValue1.setValue(SHAConstants.NON_NETWORK_HOSPITAL_TYPE);
			container.addBean(selectValue);
			container.addBean(selectValue1);
			
			container.sort(new Object[] {"value"}, new boolean[] {true});
			
			return container;
		}
		public static BeanItemContainer<SelectValue> getSelectValueForType(){
			BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
			SelectValue selectValue = new SelectValue();
			selectValue.setId(1l);
			selectValue.setValue(SHAConstants.TYPE_FRESH);
			
			SelectValue selectValue2 = new SelectValue();
			selectValue2.setId(2l);
			selectValue2.setValue(SHAConstants.QUERY_REPLY);
			
			SelectValue selectValue3 = new SelectValue();
			selectValue3.setId(3l);
			selectValue3.setValue(SHAConstants.RECONSIDERATION);
			
			SelectValue selectValue4 = new SelectValue();
			selectValue4.setId(4l);
			selectValue4.setValue(SHAConstants.ALL);
			
			SelectValue selectValue5 = new SelectValue();
			selectValue5.setId(5l);
			selectValue5.setValue(SHAConstants.QUERY_REPLY_WITH_FINAL_BILL);
			
			container.addBean(selectValue4);	
			container.addBean(selectValue);
			container.addBean(selectValue2);
			container.addBean(selectValue3);
			container.addBean(selectValue5);
			
			container.sort(new Object[] {"value"}, new boolean[] {true});
			
			return container;
		}
		
		public static BeanItemContainer<SelectValue> getSelectValueForFilterType(){
			BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
			SelectValue selectValue = new SelectValue();
			selectValue.setId(1l);
			selectValue.setValue(SHAConstants.TYPE_FRESH);
			
			SelectValue selectValue4 = new SelectValue();
			selectValue4.setId(4l);
			selectValue4.setValue(SHAConstants.ALL);
			
			container.addBean(selectValue);
			container.addBean(selectValue4);	
			return container;
		}
		
		/**
		 * For claim type, the values are hardcoded. The reason is, since this isn't going to change
		 * , we had hardcoded. Else an db hit is required to load this value. Since this
		 * isn't going to change, we had opted to move with hardcoded value
		 * 
		 * */
	/*	public static BeanItemContainer<SelectValue> getSelectValueForClaimType(){
			BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
			SelectValue selectValue = new SelectValue();
			selectValue.setId(1l);
			selectValue.setValue(SHAConstants.TYPE_FRESH);
			
			SelectValue selectValue4 = new SelectValue();
			selectValue4.setId(4l);
			selectValue4.setValue(SHAConstants.ALL);
			
			container.addBean(selectValue);
			container.addBean(selectValue4);	
			return container;
		}*/

		/*public static void showBPMNProcess(String userName, String tokenNumber){
			
			
			Window popup = new com.vaadin.ui.Window();
			popup.setWidth("75%");
			popup.setHeight("90%");
			popup.setContent(showBPMNFlowImage(userName, tokenNumber));
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
			popup.addCloseListener(new Window.CloseListener() {
				*//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}*/
		
		/*public static Panel showBPMNFlowImage(String userName, String tokenNumber){
			
			Panel panel = new Panel();
			
			final Embedded imageViewer = new Embedded("Uploaded Image");
			
			final String url = System.getProperty("jboss.server.data.dir") + "/"
					+"flow_image.JPG";
			
			AuditImageService auditImageService = BPMClientContext.getAuditImageService(userName, "Star@123");
			
			 byte[] image = null;
			try {
				image = auditImageService.getImage(BPMClientContext.BPMN_TASK_USER, tokenNumber);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 InputStream result = new ByteArrayInputStream(image);
			 try {
				archiveDiagramToFile(result, url);
				
				StreamResource.StreamSource s = new StreamResource.StreamSource() {
					public FileInputStream getStream() {
						try {
							File f = new File(url);
							FileInputStream fis = new FileInputStream(f);
							return fis;
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
					}
				};
				
				imageViewer.setSource(new StreamResource(s, "SampleBill.jpg"));
			    imageViewer.setVisible(true);  
//			    imageViewer.setHeight("1000px");
//			    imageViewer.setWidth("1000px");
			    Panel imagePanel = new Panel();
			    imagePanel.setContent(imageViewer);
			    imagePanel.setWidth("100%");
			    panel.setContent(imagePanel);	
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return panel;

			
		}*/
		
		 private static void archiveDiagramToFile(InputStream istream,String url)
			        throws IOException
			    {
			        File outputFile = new File(url);
			        OutputStream out = new FileOutputStream(outputFile);

			        // Transfer bytes from in to out
			        byte[] buf = new byte[1024];
			        int len;
			        while ((len = istream.read(buf)) > 0)
			        {
			            out.write(buf, 0, len);
			        }
			        istream.close();
			        out.close();
			    }

		public static String formatGregorianDate(String dateStr)
		{
			
			DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
			Date date= null;
			Date date1 = null;
			String formatedDate = null;
			try {
				date = (Date)formatter.parse(dateStr);
				System.out.println(date);        

				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				formatedDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-"  + cal.get(Calendar.DATE) ;
				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
					
				date1 = formatter1.parse(formatedDate);
						System.out.println(date1);
						
				 
						//+ "-" + cal.get(Calendar.HOUR);
				//formatedDate = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +         cal.get(Calendar.YEAR);
				System.out.println("formatedDate : " + formatedDate);
			/*	Date d = new Date(formatedDate);
				System.out.println("---the date d"+d);*/
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return formatedDate;
		}
		
		public static void setActiveOrDeactiveClaim(String userName,String password,Integer taskNumber,VaadinSession session) {

			Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
			if(existingTaskNumber != null){
				//BPMClientContext.setActiveOrDeactiveClaim(userName,password, existingTaskNumber, SHAConstants.SYS_RELEASE);
			}	
			
			if(taskNumber != null){
			session.setAttribute(SHAConstants.TOKEN_ID, taskNumber);		
			//BPMClientContext.setActiveOrDeactiveClaim(userName,password, taskNumber, SHAConstants.SYS_ACQUIRE);
			
			}
		}
		
		public static void releaseHumanTask(String userName,String password,Integer taskNumber,VaadinSession session){

	 		if(taskNumber != null){
	 			//BPMClientContext.setActiveOrDeactiveClaim(userName,password, taskNumber, SHAConstants.SYS_RELEASE);
	 			session.setAttribute(SHAConstants.TOKEN_ID, null);
	 		}
		}
		
		public static Boolean isActiveHumanTask(String userName, String password,Integer taskNumber,VaadinSession session){
			
			Boolean acquired = false;
			/**
			 * The below code to be refractored.
			 * 
			 * */
			/*if(taskNumber != null){
	 			HTAcquireByUser acquireHumanTask = BPMClientContext.getAcquireHumanTask(userName,password);
	 			
	 			try {
					acquired = acquireHumanTask.isAcquired(userName, taskNumber);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 		}*/
			
			return acquired;
		}
		
		public static Map<String, String> getSuspiciousMap(String remarks){
			
			Map<String, String> suspiciousPopupMessage = new WeakHashMap<String, String>();
			suspiciousPopupMessage.put(remarks, "<b style = 'color: blue'>Suspicious Hospital </b>");
		
			return suspiciousPopupMessage;
			
		}
		
	    public static Map<String, String> getSuspiciousRemarks(String suspiciousType, String instruction ){
			
			Map<String, String> suspiciousPopupRemarks = new WeakHashMap<String, String>();
			suspiciousPopupRemarks.put(suspiciousType, instruction );
		    return suspiciousPopupRemarks;
			
		}
		
		public static void fillDetailsForUtilizationForDiag(Map<String, Object>  caluculationInputValues, DiagnosisProcedureTableDTO medicalDecisionDto ) {
			caluculationInputValues
			.put("restrictedSI",
					medicalDecisionDto
							.getDiagnosisDetailsDTO()
							.getSumInsuredRestriction() != null ? medicalDecisionDto
							.getDiagnosisDetailsDTO()
							.getSumInsuredRestriction().getId()
							: null);
			caluculationInputValues
			.put("restrictedSIAmount",
					medicalDecisionDto
							.getDiagnosisDetailsDTO()
							.getSumInsuredRestriction() != null ? medicalDecisionDto
							.getDiagnosisDetailsDTO()
							.getSumInsuredRestriction().getValue()
							: null);
	
			caluculationInputValues
					.put("sublimitId",
							medicalDecisionDto
									.getDiagnosisDetailsDTO()
									.getSublimitName() != null ? medicalDecisionDto
									.getDiagnosisDetailsDTO()
									.getSublimitName().getLimitId()
									: null);
			caluculationInputValues.put("diagOrProcId",
					medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName() == null ? 0l : medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName().getId());
			caluculationInputValues.put("diagnosisId",
					medicalDecisionDto.getDiagnosisDetailsDTO()
							.getDiagnosisName().getId().toString());
			caluculationInputValues.put("referenceFlag", "D");
		}
		
		public static void fillDetailsForUtilForProcedure(Map<String, Object>  caluculationInputValues, DiagnosisProcedureTableDTO medicalDecisionDto ) {
			caluculationInputValues.put("restrictedSI", null);
			caluculationInputValues.put("restrictedSIAmount", null);
			caluculationInputValues
					.put("sublimitId",
							medicalDecisionDto.getProcedureDTO()
									.getSublimitName() != null ? medicalDecisionDto
									.getProcedureDTO()
									.getSublimitName().getLimitId()
									: null);
			caluculationInputValues.put("diagOrProcId", medicalDecisionDto.getProcedureDTO().getProcedureName() == null ? 0l : (medicalDecisionDto.getProcedureDTO().getProcedureName().getId() == null ? 0l : medicalDecisionDto.getProcedureDTO().getProcedureName().getId()));
			caluculationInputValues.put("referenceFlag", "P");
		}
		
		
		public static void setPostHospitalizationDetailsToDTO(PreauthDTO bean, String otherInsurerValues) {
			if(bean.getPostHospitalizaionFlag()) {
					PostHopitalizationDetailsDTO postHospitalizationCalculationDTO = bean.getPostHospitalizationCalculationDTO();
					postHospitalizationCalculationDTO.setNetAmount(SHAUtils.getIntegerFromString(bean.getPostHospitalisationValue()) );
//					if(postHospitalizationCalculationDTO.getNetAmount() > 0) {
						postHospitalizationCalculationDTO.setOtherInsurerAmt(SHAUtils.getIntegerFromString(otherInsurerValues));
						Integer amountForPayment =  postHospitalizationCalculationDTO.getNetAmount() - postHospitalizationCalculationDTO.getOtherInsurerAmt();
						postHospitalizationCalculationDTO.setAmountConsideredForPayment(amountForPayment < 0 ? 0 : amountForPayment);
							if(bean.getPostHospAmt() < 0) {
								bean.setPostHospAmt(0);
							}
							if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null &&  ReferenceTable.getNoLimitForMaxPayableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
								bean.setPostHospclaimRestrictionAmount(postHospitalizationCalculationDTO.getAmountConsideredForPayment().doubleValue());
							}
							
							//GALAXYMAIN-12945 - ADDED FOR MEDI_CLASSIC_BASIC GOLD PLAN
							if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && bean.getNewIntimationDTO().getInsuredPatient() != null &&  ((ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY_98.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) ||  ReferenceTable.REVISED_POS_MEDICLASSIC_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) && (SHAConstants.POLICY_GOLD_PLAN).equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan())) {
								bean.setPostHospclaimRestrictionAmount(postHospitalizationCalculationDTO.getAmountConsideredForPayment().doubleValue());
							}
							
							postHospitalizationCalculationDTO.setMaxPayable(bean.getPostHospclaimRestrictionAmount() != null ? bean.getPostHospclaimRestrictionAmount().intValue() : 0);
							postHospitalizationCalculationDTO.setPreviousRodPostHospamt(bean.getPreviousPostHospAmount());
							Integer amtPayable  = postHospitalizationCalculationDTO.getMaxPayable();
							if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() == null || (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && !bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
//								amtPayable  = postHospitalizationCalculationDTO.get() - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
							}
							postHospitalizationCalculationDTO.setMaxPayable(amtPayable < 0 ? 0 :amtPayable);
							
							if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
								
								List<Double> productCopay = bean.getProductCopay();
								Double copayValue = 0d;
								if(productCopay != null && !productCopay.isEmpty()) {
									copayValue = productCopay.get(0);
									if(productCopay.size() >=2){
										copayValue = productCopay.get(1);
									}
								}
								copayValue = bean.getAmountConsCopayPercentage() != null ? bean.getAmountConsCopayPercentage().doubleValue() : 0d;
								postHospitalizationCalculationDTO.setCopayValue(copayValue);
								Double copayAmount = (copayValue / 100) * bean.getPostHospAmt();
								Long roundedCopay = Math.round(copayAmount);
								postHospitalizationCalculationDTO.setCopayAmt(roundedCopay.intValue());
								Long afterCopayAmt = (bean.getPostHospAmt().longValue()) - roundedCopay;
								
								Float value =  (((!bean.getPostHospPercentage().equals(0d) ? bean.getPostHospPercentage().floatValue() : 7f)/ 100f) * afterCopayAmt.floatValue());
								int roundedValue = Math.round(value);
								if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null
										&& (ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
												|| ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
									if(value != null){
										int postAllowedAmt = Math.min(roundedValue, bean.getPostHospclaimRestrictionAmount().intValue());
										roundedValue = postAllowedAmt;
									}
								}
								postHospitalizationCalculationDTO.setEligibleAmt(roundedValue);
								postHospitalizationCalculationDTO.setCopayAmt(0);
								
							
							} else {
								Float value =  (((!bean.getPostHospPercentage().equals(0d) ? bean.getPostHospPercentage().floatValue() : 7f)/ 100f) * bean.getPostHospAmt().floatValue());
								int roundedValue = Math.round(value);
								postHospitalizationCalculationDTO.setEligibleAmt(roundedValue);
								if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null &&  ReferenceTable.getNoLimitForMaxPayableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
									postHospitalizationCalculationDTO.setEligibleAmt(bean.getPostHospclaimRestrictionAmount() != null ? bean.getPostHospclaimRestrictionAmount().intValue() : 0);
								}
								
								//GALAXYMAIN-12945 - ADDED FOR MEDI_CLASSIC_BASIC GOLD PLAN
								if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && bean.getNewIntimationDTO().getInsuredPatient() != null &&  (ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY_98.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) && (SHAConstants.POLICY_GOLD_PLAN).equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan())) {
									postHospitalizationCalculationDTO.setEligibleAmt(bean.getPostHospclaimRestrictionAmount() != null ? bean.getPostHospclaimRestrictionAmount().intValue() : 0);
								}
								
								if(ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
									postHospitalizationCalculationDTO.setEligibleAmt(bean.getPostHospclaimRestrictionAmount() != null ? bean.getPostHospclaimRestrictionAmount().intValue() : 0);
								}
							}
							
							Integer minAmt = 0;
							
								minAmt = Math.min(postHospitalizationCalculationDTO.getEligibleAmt() < 0 ? 0 : postHospitalizationCalculationDTO.getEligibleAmt(), postHospitalizationCalculationDTO.getAmountConsideredForPayment() < 0 ? 0 : postHospitalizationCalculationDTO.getAmountConsideredForPayment());
							
							/** For this product we need to set maximum amount to B section, So that the below code is added**/
								
							if(ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
								 minAmt = Math.max(postHospitalizationCalculationDTO.getEligibleAmt() < 0 ? 0 : postHospitalizationCalculationDTO.getEligibleAmt(), postHospitalizationCalculationDTO.getAmountConsideredForPayment() < 0 ? 0 : postHospitalizationCalculationDTO.getAmountConsideredForPayment());
							}
							
							Integer minAmtWithMaxPayable = 0;
							
							minAmtWithMaxPayable = Math.min((bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_ANOTHER_POLICY) || ReferenceTable.getFHOProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) ? minAmt : (postHospitalizationCalculationDTO.getMaxPayable() < 0 ? 0: postHospitalizationCalculationDTO.getMaxPayable()), minAmt);
							
							if(ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
								
								minAmtWithMaxPayable = minAmt;
							}
							
							if(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) || 
									ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									|| ReferenceTable.STAR_GRP_COVID_PROD_KEY_INDI.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) || 
									ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){

								minAmtWithMaxPayable = minAmt;
							}
							
							if(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_KEY_IND.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) || 
									ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_KEY_FLT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){

								minAmtWithMaxPayable = minAmt;
							}
							
							postHospitalizationCalculationDTO.setAmountPayable(minAmtWithMaxPayable);
							
							if(!(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
								List<Double> productCopay = bean.getProductCopay();
								Double copayValue = 0d;
								if(productCopay != null && !productCopay.isEmpty()) {
									copayValue = productCopay.get(0);
									if(productCopay.size() >=2){
										copayValue = productCopay.get(1);
									}
								}
								copayValue = bean.getAmountConsCopayPercentage() != null ? bean.getAmountConsCopayPercentage().doubleValue() : 0d;
								postHospitalizationCalculationDTO.setCopayValue(copayValue);
								Double copayAmount = (copayValue / 100) * postHospitalizationCalculationDTO.getAmountPayable();
								Long roundedCopay = Math.round(copayAmount);
								
								postHospitalizationCalculationDTO.setCopayAmt(roundedCopay.intValue());
							}
							
							
							postHospitalizationCalculationDTO.setIsSIRestrictionAvail(bean.getIsSIRestrictionAvail());
							Integer payableAmt = postHospitalizationCalculationDTO.getAmountPayable() - postHospitalizationCalculationDTO.getCopayAmt();
							postHospitalizationCalculationDTO.setPayableAmt(payableAmt < 0 ? 0 : payableAmt);
							Integer excludingHospAmount = bean.getBalanceSIAftHosp() + bean.getHospAmountInBiling();
							excludingHospAmount = excludingHospAmount > 0 ? excludingHospAmount : 0;
							Integer currentHospAmount = 0;
							/**
							 * IMSSUPPOR-24150
							 */
							if(bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) &&
									! bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getHospitalisationFlag().equalsIgnoreCase("Y")
									&& ! bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y")){
								currentHospAmount = excludingHospAmount;
							}else{
								currentHospAmount = excludingHospAmount - ((bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) ? bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : bean.getHospitalizationCalculationDTO().getPayableToHospitalAftTDSAmt());
							}
							//currentHospAmount = (excludingHospAmount.equals(currentHospAmount) ? bean.getBalanceSIAftHosp() : currentHospAmount) - bean.getPreHospitalizationCalculationDTO().getPreviousRodPreviousPrehospAmt();
							if(bean.getPreHospitalizaionFlag()) {
//								currentHospAmount = currentHospAmount - bean.getPreHospitalizationCalculationDTO().getPayableToInsAmt();
								if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
									currentHospAmount = currentHospAmount - bean.getPreHospitalizationCalculationDTO().getBalanceToBePaid();
									} else {	
									currentHospAmount = currentHospAmount - bean.getPreHospitalizationCalculationDTO().getPayableToInsAmt();
									}
							}
							
							//IMSSUPPOR-24282
							if( bean.getPartialHospitalizaionFlag()  && (bean.getHospitalizationCalculationDTO().getPreauthAppAmt() != null && bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt() != null)){
								Integer preauthApprovedAmt = 0;
								if(bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt() > 0){
									preauthApprovedAmt = Math.min(bean.getHospitalizationCalculationDTO().getPreauthAppAmt(), bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt());
								}else{
									preauthApprovedAmt = bean.getHospitalizationCalculationDTO().getPreauthAppAmt();
								}
								currentHospAmount = currentHospAmount - preauthApprovedAmt;
								//IMSSUPPOR-26536
								if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
									//IMSSUPPOR-27414
									if(bean.getHospitalizationCalculationDTO().getAmountAlreadyPaid() != null && ((bean.getPostHospAmtAlreadyPaid() != null && bean.getPostHospAmtAlreadyPaid() > 0) || (bean.getPreviousPostHospAmount() != null && bean.getPreviousPostHospAmount() > 0))){
										currentHospAmount = currentHospAmount - bean.getHospitalizationCalculationDTO().getAmountAlreadyPaid();
									}
								}
							}
							
							currentHospAmount = currentHospAmount > 0 ? currentHospAmount : 0;
							
							
							postHospitalizationCalculationDTO.setAvaliableSumInsuredAftHosp(excludingHospAmount.equals(currentHospAmount) ? bean.getBalanceSIAftHosp() : currentHospAmount);
							postHospitalizationCalculationDTO.setRestrictedSIAftHosp(bean.getIsSIRestrictionAvail() ? bean.getSiRestrictionAmount() : 0);
							
							//IMSSUPPOR-26536 
							Integer totalAvailableSI = postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp()+ (bean.getPostHospAmtAlreadyPaid() != null ? bean.getPostHospAmtAlreadyPaid() : 0) +((bean.getPreviousPostHospAmount() != null && bean.getPreviousPostHospAmount() > 0) ? bean.getPreviousPostHospAmount() : 0);
							postHospitalizationCalculationDTO.setAvaliableSumInsuredAftHosp(totalAvailableSI);
							
							Integer min = Math.min(postHospitalizationCalculationDTO.getPayableAmt(), Math.min(postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp(), postHospitalizationCalculationDTO.getIsSIRestrictionAvail() ? (postHospitalizationCalculationDTO.getRestrictedSIAftHosp() > 0 ? postHospitalizationCalculationDTO.getRestrictedSIAftHosp() : 0 ) : postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp()));
							if(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue().equalsIgnoreCase(ReferenceTable.BARIATRIC_SECTION_CODE)) {
								postHospitalizationCalculationDTO.setAvaliableSublimitBalanceForBariatric(bean.getBariatricAvailableAmount().intValue());
								min = Math.min(min, postHospitalizationCalculationDTO.getAvaliableSublimitBalanceForBariatric());
							}
							postHospitalizationCalculationDTO.setNetPayable(min);
							
							postHospitalizationCalculationDTO.setClaimRestrictionAmt(0);  
//							if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
//								postHospitalizationCalculationDTO.setPayableToInsAmt(postHospitalizationCalculationDTO.getPayableAmt());
//							} 
							Integer eligibleAmt = postHospitalizationCalculationDTO.getNetPayable() - postHospitalizationCalculationDTO.getClaimRestrictionAmt();
							eligibleAmt = eligibleAmt - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
							postHospitalizationCalculationDTO.setRevisedEligibleAmount(eligibleAmt < 0 ? 0: eligibleAmt);
							//int amountPayableToIns = Math.min(postHospitalizationCalculationDTO.getRevisedEligibleAmount(), bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.FHO_NEW_PRODUCT) ?  postHospitalizationCalculationDTO.getRevisedEligibleAmount() : postHospitalizationCalculationDTO.getRevisedEligibleAmount());
							int amountPayableToIns = Math.min(postHospitalizationCalculationDTO.getRevisedEligibleAmount(), ReferenceTable.getFHOProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ?  postHospitalizationCalculationDTO.getRevisedEligibleAmount() : postHospitalizationCalculationDTO.getRevisedEligibleAmount());
							Integer payableToIns = amountPayableToIns; // postHospitalizationCalculationDTO.getRevisedEligibleAmount() - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
							postHospitalizationCalculationDTO.setPayableToInsAmt(payableToIns < 0 ? 0: payableToIns);
							postHospitalizationCalculationDTO.setBalancePremiumAmt(0);
							postHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(postHospitalizationCalculationDTO.getPayableToInsAmt() - postHospitalizationCalculationDTO.getBalancePremiumAmt());
							
							if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
								postHospitalizationCalculationDTO.setAmountAlreadyPaid(bean.getPostHospAmtAlreadyPaid() != null ? bean.getPostHospAmtAlreadyPaid() : 0);
								Integer deductedAmt = 0;
								if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() == null || (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && !bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
									deductedAmt = postHospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - postHospitalizationCalculationDTO.getAmountAlreadyPaid();
								} else {
									deductedAmt = postHospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt();
								}
//								Integer balanceAmt = hospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - (hospitalizationCalculationDTO.getAmountAlreadyPaid());
								postHospitalizationCalculationDTO.setBalanceToBePaid(deductedAmt < 0  ? 0 : deductedAmt);
							}
							
							if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
									ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
								bean.setIsPostHospApplicable(true);
							}
							
							if(!bean.getIsPostHospApplicable() || bean.getIsHospitalizationRejected()  || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP)) {
								postHospitalizationCalculationDTO.setPayableToInsAmt(0);
								postHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(0);
								postHospitalizationCalculationDTO.setBalanceToBePaid(0);
							}
//					}
			
				}
				
			
			
//			if(bean.getPostHospitalizaionFlag()) {
//				PostHopitalizationDetailsDTO postHospitalizationCalculationDTO = bean.getPostHospitalizationCalculationDTO();
//				postHospitalizationCalculationDTO.setNetAmount(SHAUtils.getIntegerFromString(bean.getPostHospitalisationValue()) );
////				if(postHospitalizationCalculationDTO.getNetAmount() > 0) {
//					postHospitalizationCalculationDTO.setOtherInsurerAmt(SHAUtils.getIntegerFromString(otherInsurerValues));
//					Integer amountForPayment =  postHospitalizationCalculationDTO.getNetAmount() - postHospitalizationCalculationDTO.getOtherInsurerAmt();
//					postHospitalizationCalculationDTO.setAmountConsideredForPayment(amountForPayment < 0 ? 0 : amountForPayment);
//						if(bean.getPostHospAmt() < 0) {
//							bean.setPostHospAmt(0);
//						}
//						if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.FHO_NEW_PRODUCT)) {
//							bean.setPostHospAmt(postHospitalizationCalculationDTO.getAmountConsideredForPayment());
//						}
//						Float value =  (((!bean.getPostHospPercentage().equals(0d) ? bean.getPostHospPercentage().floatValue() : 7f)/ 100f) * bean.getPostHospAmt().floatValue());
//						int roundedValue = Math.round(value);
//						postHospitalizationCalculationDTO.setEligibleAmt(roundedValue);
//						
//						Integer amt = roundedValue;
//						Integer min2 = Math.min(amt, postHospitalizationCalculationDTO.getAmountConsideredForPayment() < 0 ? 0 : postHospitalizationCalculationDTO.getAmountConsideredForPayment());
//						postHospitalizationCalculationDTO.setAmountPayable(min2);
//						
//						List<Double> productCopay = bean.getProductCopay();
//						Double copayValue = 0d;
//						if(productCopay != null && !productCopay.isEmpty()) {
//							copayValue = productCopay.get(0);
//							if(productCopay.size() >=2){
//								copayValue = productCopay.get(1);
//							}
//						}
//						copayValue = bean.getAmountConsCopayPercentage() != null ? bean.getAmountConsCopayPercentage().doubleValue() : 0d;
//						postHospitalizationCalculationDTO.setCopayValue(copayValue.intValue());
//						Double copayAmount = (copayValue / 100) * postHospitalizationCalculationDTO.getAmountPayable();
//						Long roundedCopay = Math.round(copayAmount);
//						
//						postHospitalizationCalculationDTO.setCopayAmt(roundedCopay.intValue());
//						postHospitalizationCalculationDTO.setIsSIRestrictionAvail(bean.getIsSIRestrictionAvail());
//						Integer payableAmt = postHospitalizationCalculationDTO.getAmountPayable() - postHospitalizationCalculationDTO.getCopayAmt();
//						postHospitalizationCalculationDTO.setPayableAmt(payableAmt < 0 ? 0 : payableAmt);
//						postHospitalizationCalculationDTO.setMaxPayable(bean.getPostHospclaimRestrictionAmount() != null ? bean.getPostHospclaimRestrictionAmount().intValue() : 0);
//						postHospitalizationCalculationDTO.setAvaliableSumInsuredAftHosp(bean.getBalanceSIAftHosp());
//						postHospitalizationCalculationDTO.setPreviousRodPostHospamt(bean.getPreviousPostHospAmount());
//						postHospitalizationCalculationDTO.setRestrictedSIAftHosp(bean.getIsSIRestrictionAvail() ? bean.getSiRestrictionAmount() : 0);
//						Integer amtPayable  = postHospitalizationCalculationDTO.getMaxPayable() - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
//						postHospitalizationCalculationDTO.setMaxPayable(amtPayable < 0 ? 0 :amtPayable);
//						int min3 = Math.min(postHospitalizationCalculationDTO.getPayableAmt(), bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.FHO_NEW_PRODUCT) ? postHospitalizationCalculationDTO.getPayableAmt() :  amtPayable < 0 ? 0 :amtPayable);
//						if(amtPayable < 0) {
//							min3 = 0;
//						}
//						
//						Integer min = Math.min(postHospitalizationCalculationDTO.getPayableAmt(), Math.min((bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_ANOTHER_POLICY) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.FHO_NEW_PRODUCT)) ? postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp()  : postHospitalizationCalculationDTO.getMaxPayable(), Math.min(postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp(),(postHospitalizationCalculationDTO.getIsSIRestrictionAvail() ? (postHospitalizationCalculationDTO.getRestrictedSIAftHosp() > 0 ? postHospitalizationCalculationDTO.getRestrictedSIAftHosp() : 0 ) : postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp()))));
//						postHospitalizationCalculationDTO.setNetPayable(min);
//						
//						postHospitalizationCalculationDTO.setClaimRestrictionAmt(0);  
////						if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
////							postHospitalizationCalculationDTO.setPayableToInsAmt(postHospitalizationCalculationDTO.getPayableAmt());
////						} 
//						Integer eligibleAmt = postHospitalizationCalculationDTO.getNetPayable() - postHospitalizationCalculationDTO.getClaimRestrictionAmt();
//						postHospitalizationCalculationDTO.setRevisedEligibleAmount(eligibleAmt < 0 ? 0: eligibleAmt);
//						int amountPayableToIns = Math.min(postHospitalizationCalculationDTO.getRevisedEligibleAmount(), bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.FHO_NEW_PRODUCT) ?  postHospitalizationCalculationDTO.getRevisedEligibleAmount() : amtPayable < 0 ? 0 :amtPayable);
//						Integer payableToIns = amountPayableToIns; // postHospitalizationCalculationDTO.getRevisedEligibleAmount() - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
//						postHospitalizationCalculationDTO.setPayableToInsAmt(payableToIns < 0 ? 0: payableToIns);
//						postHospitalizationCalculationDTO.setBalancePremiumAmt(0);
//						postHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(postHospitalizationCalculationDTO.getPayableToInsAmt() - postHospitalizationCalculationDTO.getBalancePremiumAmt());
//						
//						if(!bean.getIsPostHospApplicable() || bean.getIsHospitalizationRejected()  || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP)) {
//							postHospitalizationCalculationDTO.setPayableToInsAmt(0);
//							postHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(0);
//						}
////				}
//		
//			}
			
		}
		
		public static void setRevisedPostHospitalizationDetailsToDTO(PreauthDTO bean, String otherInsurerValues) {
			if(bean.getPostHospitalizaionFlag()) {
				PostHopitalizationDetailsDTO postHospitalizationCalculationDTO = bean.getPostHospitalizationCalculationDTO();
				postHospitalizationCalculationDTO.setNetAmount(SHAUtils.getIntegerFromString(bean.getPostHospitalisationValue()) );
//				if(postHospitalizationCalculationDTO.getNetAmount() > 0) {
					postHospitalizationCalculationDTO.setOtherInsurerAmt(SHAUtils.getIntegerFromString(otherInsurerValues));
					Integer amountForPayment =  postHospitalizationCalculationDTO.getNetAmount() - postHospitalizationCalculationDTO.getOtherInsurerAmt();
					postHospitalizationCalculationDTO.setAmountConsideredForPayment(amountForPayment < 0 ? 0 : amountForPayment);
						if(bean.getPostHospAmt() < 0) {
							bean.setPostHospAmt(0);
						}
						if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && ReferenceTable.getFHOProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
							bean.setPostHospAmt(postHospitalizationCalculationDTO.getAmountConsideredForPayment());
						}
						postHospitalizationCalculationDTO.setMaxPayable(bean.getPostHospclaimRestrictionAmount() != null ? bean.getPostHospclaimRestrictionAmount().intValue() : 0);
						postHospitalizationCalculationDTO.setPreviousRodPostHospamt(bean.getPreviousPostHospAmount());
						Integer amtPayable  = postHospitalizationCalculationDTO.getMaxPayable();
						if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() == null || (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && !bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
//							amtPayable  = postHospitalizationCalculationDTO.getMaxPayable() - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
						}
						postHospitalizationCalculationDTO.setMaxPayable(amtPayable < 0 ? 0 :amtPayable);
						
						if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
							
							List<Double> productCopay = bean.getProductCopay();
							Double copayValue = 0d;
							if(productCopay != null && !productCopay.isEmpty()) {
								copayValue = productCopay.get(0);
								if(productCopay.size() >=2){
									copayValue = productCopay.get(1);
								}
							}
							copayValue = bean.getAmountConsCopayPercentage() != null ? bean.getAmountConsCopayPercentage().doubleValue() : 0d;
							postHospitalizationCalculationDTO.setCopayValue(copayValue);
							Double copayAmount = (copayValue / 100) * bean.getPostHospAmt();
							Long roundedCopay = Math.round(copayAmount);
							postHospitalizationCalculationDTO.setCopayAmt(roundedCopay.intValue());
							Long afterCopayAmt = (bean.getPostHospAmt().longValue()) - roundedCopay;
							
							Float value =  (((!bean.getPostHospPercentage().equals(0d) ? bean.getPostHospPercentage().floatValue() : 7f)/ 100f) * afterCopayAmt.floatValue());
							int roundedValue = Math.round(value);
							postHospitalizationCalculationDTO.setEligibleAmt(roundedValue);
							
							postHospitalizationCalculationDTO.setCopayAmt(0);
							
						
						} else {
							Float value =  (((!bean.getPostHospPercentage().equals(0d) ? bean.getPostHospPercentage().floatValue() : 7f)/ 100f) * bean.getPostHospAmt().floatValue());
							int roundedValue = Math.round(value);
							postHospitalizationCalculationDTO.setEligibleAmt(roundedValue);
						}
						
						Integer minAmt = Math.min(postHospitalizationCalculationDTO.getEligibleAmt() < 0 ? 0 : postHospitalizationCalculationDTO.getEligibleAmt(), postHospitalizationCalculationDTO.getAmountConsideredForPayment() < 0 ? 0 : postHospitalizationCalculationDTO.getAmountConsideredForPayment());
						Integer minAmtWithMaxPayable = Math.min((bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_ANOTHER_POLICY) || ReferenceTable.getFHOProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) ? minAmt : (postHospitalizationCalculationDTO.getMaxPayable() < 0 ? 0: postHospitalizationCalculationDTO.getMaxPayable()), minAmt);
						postHospitalizationCalculationDTO.setAmountPayable(minAmtWithMaxPayable);
						
						if(!(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
							List<Double> productCopay = bean.getProductCopay();
							Double copayValue = 0d;
							if(productCopay != null && !productCopay.isEmpty()) {
								copayValue = productCopay.get(0);
								if(productCopay.size() >=2){
									copayValue = productCopay.get(1);
								}
							}
							copayValue = bean.getAmountConsCopayPercentage() != null ? bean.getAmountConsCopayPercentage().doubleValue() : 0d;
							postHospitalizationCalculationDTO.setCopayValue(copayValue);
							Double copayAmount = (copayValue / 100) * postHospitalizationCalculationDTO.getAmountPayable();
							Long roundedCopay = Math.round(copayAmount);
							
							postHospitalizationCalculationDTO.setCopayAmt(roundedCopay.intValue());
						}
						
						
						postHospitalizationCalculationDTO.setIsSIRestrictionAvail(bean.getIsSIRestrictionAvail());
						Integer payableAmt = postHospitalizationCalculationDTO.getAmountPayable() - postHospitalizationCalculationDTO.getCopayAmt();
						postHospitalizationCalculationDTO.setPayableAmt(payableAmt < 0 ? 0 : payableAmt);
						Integer excludingHospAmount = bean.getBalanceSIAftHosp() + bean.getHospAmountInBiling();
						excludingHospAmount = excludingHospAmount > 0 ? excludingHospAmount : 0;
						Integer currentHospAmount = excludingHospAmount - ((bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) ? bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : bean.getHospitalizationCalculationDTO().getPayableToHospitalAftTDSAmt());
						currentHospAmount = currentHospAmount > 0 ? currentHospAmount : 0;
						
						currentHospAmount = currentHospAmount - bean.getPostHospitalizationCalculationDTO().getPreviousRodPostHospamt();
						if(bean.getPostHospitalizaionFlag()) {
							currentHospAmount = currentHospAmount - bean.getPostHospitalizationCalculationDTO().getPayableToInsAmt();
						}
						
						if(bean.getHospitalizationCalculationDTO().getPreauthAppAmt() != null && bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt() != null){
							Integer preauthApprovedAmt = 0;
							if(bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt() > 0){
								preauthApprovedAmt = Math.min(bean.getHospitalizationCalculationDTO().getPreauthAppAmt(), bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt());
							}else{
								preauthApprovedAmt = bean.getHospitalizationCalculationDTO().getPreauthAppAmt();
							}
							currentHospAmount = currentHospAmount - preauthApprovedAmt;
						}
						
						currentHospAmount = currentHospAmount > 0 ? currentHospAmount : 0;
						
						postHospitalizationCalculationDTO.setAvaliableSumInsuredAftHosp(excludingHospAmount.equals(currentHospAmount) ? bean.getBalanceSIAftHosp() : currentHospAmount);
						postHospitalizationCalculationDTO.setRestrictedSIAftHosp(bean.getIsSIRestrictionAvail() ? bean.getSiRestrictionAmount() : 0);
						
						Integer min = Math.min(postHospitalizationCalculationDTO.getPayableAmt(), Math.min(postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp(), postHospitalizationCalculationDTO.getIsSIRestrictionAvail() ? (postHospitalizationCalculationDTO.getRestrictedSIAftHosp() > 0 ? postHospitalizationCalculationDTO.getRestrictedSIAftHosp() : 0 ) : postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp()));
						postHospitalizationCalculationDTO.setNetPayable(min);
						
						postHospitalizationCalculationDTO.setClaimRestrictionAmt(0);  
//						if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
//							postHospitalizationCalculationDTO.setPayableToInsAmt(postHospitalizationCalculationDTO.getPayableAmt());
//						} 
						Integer eligibleAmt = postHospitalizationCalculationDTO.getNetPayable() - postHospitalizationCalculationDTO.getClaimRestrictionAmt();
						eligibleAmt = eligibleAmt - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
						postHospitalizationCalculationDTO.setRevisedEligibleAmount(eligibleAmt < 0 ? 0: eligibleAmt);
						//int amountPayableToIns = Math.min(postHospitalizationCalculationDTO.getRevisedEligibleAmount(), bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.FHO_NEW_PRODUCT) ?  postHospitalizationCalculationDTO.getRevisedEligibleAmount() : postHospitalizationCalculationDTO.getRevisedEligibleAmount());
						int amountPayableToIns = Math.min(postHospitalizationCalculationDTO.getRevisedEligibleAmount(), ReferenceTable.getFHOProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ?  postHospitalizationCalculationDTO.getRevisedEligibleAmount() : postHospitalizationCalculationDTO.getRevisedEligibleAmount());
						Integer payableToIns = amountPayableToIns; // postHospitalizationCalculationDTO.getRevisedEligibleAmount() - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
						postHospitalizationCalculationDTO.setPayableToInsAmt(payableToIns < 0 ? 0: payableToIns);
						postHospitalizationCalculationDTO.setBalancePremiumAmt(0);
						postHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(postHospitalizationCalculationDTO.getPayableToInsAmt() - postHospitalizationCalculationDTO.getBalancePremiumAmt());
						
						if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
							postHospitalizationCalculationDTO.setAmountAlreadyPaid(bean.getPostHospAmtAlreadyPaid() != null ? bean.getPostHospAmtAlreadyPaid() : 0);
							Integer deductedAmt = 0;
							if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() == null || (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && !bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
								deductedAmt = postHospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - postHospitalizationCalculationDTO.getAmountAlreadyPaid();
							} else {
								deductedAmt = postHospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt();
							}
//							Integer balanceAmt = hospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - (hospitalizationCalculationDTO.getAmountAlreadyPaid());
							postHospitalizationCalculationDTO.setBalanceToBePaid(deductedAmt < 0  ? 0 : deductedAmt);
						}
						
						if(!bean.getIsPostHospApplicable() || bean.getIsHospitalizationRejected()  || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP)) {
							postHospitalizationCalculationDTO.setPayableToInsAmt(0);
							postHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(0);
							postHospitalizationCalculationDTO.setBalanceToBePaid(0);
						}
//				}
		
			}
			
		}
		
		public static void setPostHospitalizationDetailsToDTOForChange(PreauthDTO bean, String otherInsurerValues) {

			PostHopitalizationDetailsDTO postHospitalizationCalculationDTO = bean.getPostHospitalizationCalculationDTO();
			postHospitalizationCalculationDTO.setNetAmount(SHAUtils.getIntegerFromString(bean.getPostHospitalisationValue()) );
//			if(postHospitalizationCalculationDTO.getNetAmount() > 0) {
				postHospitalizationCalculationDTO.setOtherInsurerAmt(SHAUtils.getIntegerFromString(otherInsurerValues));
				Integer amountForPayment =  postHospitalizationCalculationDTO.getNetAmount() - postHospitalizationCalculationDTO.getOtherInsurerAmt();
				postHospitalizationCalculationDTO.setAmountConsideredForPayment(amountForPayment < 0 ? 0 : amountForPayment);
					if(bean.getPostHospAmt() < 0) {
						bean.setPostHospAmt(0);
					}
					if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && ReferenceTable.getNoLimitForMaxPayableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
						bean.setPostHospclaimRestrictionAmount(postHospitalizationCalculationDTO.getAmountConsideredForPayment().doubleValue());
					}
					postHospitalizationCalculationDTO.setMaxPayable(bean.getPostHospclaimRestrictionAmount() != null ? bean.getPostHospclaimRestrictionAmount().intValue() : 0);
					postHospitalizationCalculationDTO.setPreviousRodPostHospamt(bean.getPreviousPostHospAmount());
					Integer amtPayable  = postHospitalizationCalculationDTO.getMaxPayable();
					if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() == null || (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && !bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
//						amtPayable  = postHospitalizationCalculationDTO.getMaxPayable() - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
					}
					postHospitalizationCalculationDTO.setMaxPayable(amtPayable < 0 ? 0 :amtPayable);
					
					if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
						
						List<Double> productCopay = bean.getProductCopay();
						Double copayValue = 0d;
						if(productCopay != null && !productCopay.isEmpty()) {
							copayValue = productCopay.get(0);
							if(productCopay.size() >=2){
								copayValue = productCopay.get(1);
							}
						}
						copayValue = bean.getAmountConsCopayPercentage() != null ? bean.getAmountConsCopayPercentage().doubleValue() : 0d;
						postHospitalizationCalculationDTO.setCopayValue(copayValue);
						Double copayAmount = (copayValue / 100) * bean.getPostHospAmt();
						Long roundedCopay = Math.round(copayAmount);
						postHospitalizationCalculationDTO.setCopayAmt(roundedCopay.intValue());
						Long afterCopayAmt = (bean.getPostHospAmt().longValue()) - roundedCopay;
						
						Float value =  (((!bean.getPostHospPercentage().equals(0d) ? bean.getPostHospPercentage().floatValue() : 7f)/ 100f) * afterCopayAmt.floatValue());
						int roundedValue = Math.round(value);
						postHospitalizationCalculationDTO.setEligibleAmt(roundedValue);
						
						postHospitalizationCalculationDTO.setCopayAmt(0);
						
					
					} else {
						Float value =  (((!bean.getPostHospPercentage().equals(0d) ? bean.getPostHospPercentage().floatValue() : 7f)/ 100f) * bean.getPostHospAmt().floatValue());
						int roundedValue = Math.round(value);
						postHospitalizationCalculationDTO.setEligibleAmt(roundedValue);
						if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null &&  ReferenceTable.getNoLimitForMaxPayableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
							postHospitalizationCalculationDTO.setEligibleAmt(bean.getPostHospclaimRestrictionAmount() != null ? bean.getPostHospclaimRestrictionAmount().intValue() : 0);
						}
					}
					
					Integer minAmt = Math.min(postHospitalizationCalculationDTO.getEligibleAmt() < 0 ? 0 : postHospitalizationCalculationDTO.getEligibleAmt(), postHospitalizationCalculationDTO.getAmountConsideredForPayment() < 0 ? 0 : postHospitalizationCalculationDTO.getAmountConsideredForPayment());
					Integer minAmtWithMaxPayable = Math.min((bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_ANOTHER_POLICY) || ReferenceTable.getFHOProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) ? minAmt : (postHospitalizationCalculationDTO.getMaxPayable() < 0 ? 0: postHospitalizationCalculationDTO.getMaxPayable()), minAmt);
					postHospitalizationCalculationDTO.setAmountPayable(minAmtWithMaxPayable);
					
					if(!(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
						List<Double> productCopay = bean.getProductCopay();
						Double copayValue = 0d;
						if(productCopay != null && !productCopay.isEmpty()) {
							copayValue = productCopay.get(0);
							if(productCopay.size() >=2){
								copayValue = productCopay.get(1);
							}
						}
						copayValue = bean.getAmountConsCopayPercentage() != null ? bean.getAmountConsCopayPercentage().doubleValue() : 0d;
						postHospitalizationCalculationDTO.setCopayValue(copayValue);
						Double copayAmount = (copayValue / 100) * postHospitalizationCalculationDTO.getAmountPayable();
						Long roundedCopay = Math.round(copayAmount);
						
						postHospitalizationCalculationDTO.setCopayAmt(roundedCopay.intValue());
					}
					
					
					postHospitalizationCalculationDTO.setIsSIRestrictionAvail(bean.getIsSIRestrictionAvail());
					Integer payableAmt = postHospitalizationCalculationDTO.getAmountPayable() - postHospitalizationCalculationDTO.getCopayAmt();
					postHospitalizationCalculationDTO.setPayableAmt(payableAmt < 0 ? 0 : payableAmt);
					Integer excludingHospAmount = bean.getBalanceSIAftHosp() + bean.getHospAmountInBiling();
					excludingHospAmount = excludingHospAmount > 0 ? excludingHospAmount : 0;
					Integer currentHospAmount = excludingHospAmount - ((bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) ? bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : bean.getHospitalizationCalculationDTO().getPayableToHospitalAftTDSAmt());
					currentHospAmount = currentHospAmount > 0 ? currentHospAmount : 0;
					
					currentHospAmount = currentHospAmount - bean.getPostHospitalizationCalculationDTO().getPreviousRodPostHospamt();
					if(bean.getPostHospitalizaionFlag()) {
						currentHospAmount = currentHospAmount - bean.getPostHospitalizationCalculationDTO().getPayableToInsAmt();
					}
					
					/**
					 * Only first ROD is paritial hospitalization
					 */
					
					/*if(bean.getPartialHospitalizaionFlag()  && (bean.getHospitalizationCalculationDTO().getPreauthAppAmt() != null && bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt() != null)){
						Integer preauthApprovedAmt = 0;
						if(bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt() > 0){
							preauthApprovedAmt = Math.min(bean.getHospitalizationCalculationDTO().getPreauthAppAmt(), bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt());
						}else{
							preauthApprovedAmt = bean.getHospitalizationCalculationDTO().getPreauthAppAmt();
						}
						currentHospAmount = currentHospAmount - preauthApprovedAmt;
					}*/
					
					currentHospAmount = currentHospAmount > 0 ? currentHospAmount : 0;
					
					postHospitalizationCalculationDTO.setAvaliableSumInsuredAftHosp(excludingHospAmount.equals(currentHospAmount) ? bean.getBalanceSIAftHosp() : currentHospAmount);
					postHospitalizationCalculationDTO.setRestrictedSIAftHosp(bean.getIsSIRestrictionAvail() ? bean.getSiRestrictionAmount() : 0);
					
					Integer min = Math.min(postHospitalizationCalculationDTO.getPayableAmt(), Math.min(postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp(), postHospitalizationCalculationDTO.getIsSIRestrictionAvail() ? (postHospitalizationCalculationDTO.getRestrictedSIAftHosp() > 0 ? postHospitalizationCalculationDTO.getRestrictedSIAftHosp() : 0 ) : postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp()));
					if(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue().equalsIgnoreCase(ReferenceTable.BARIATRIC_SECTION_CODE)) {
						postHospitalizationCalculationDTO.setAvaliableSublimitBalanceForBariatric(bean.getBariatricAvailableAmount().intValue());
						min = Math.min(min, postHospitalizationCalculationDTO.getAvaliableSublimitBalanceForBariatric());
					}
					postHospitalizationCalculationDTO.setNetPayable(min);
					
					postHospitalizationCalculationDTO.setClaimRestrictionAmt(0);  
//					if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
//						postHospitalizationCalculationDTO.setPayableToInsAmt(postHospitalizationCalculationDTO.getPayableAmt());
//					} 
					Integer eligibleAmt = postHospitalizationCalculationDTO.getNetPayable() - postHospitalizationCalculationDTO.getClaimRestrictionAmt();
					eligibleAmt = eligibleAmt - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
					postHospitalizationCalculationDTO.setRevisedEligibleAmount(eligibleAmt < 0 ? 0: eligibleAmt);
					//int amountPayableToIns = Math.min(postHospitalizationCalculationDTO.getRevisedEligibleAmount(), bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.FHO_NEW_PRODUCT) ?  postHospitalizationCalculationDTO.getRevisedEligibleAmount() : postHospitalizationCalculationDTO.getRevisedEligibleAmount());
					int amountPayableToIns = Math.min(postHospitalizationCalculationDTO.getRevisedEligibleAmount(), ReferenceTable.getFHOProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ?  postHospitalizationCalculationDTO.getRevisedEligibleAmount() : postHospitalizationCalculationDTO.getRevisedEligibleAmount());
					Integer payableToIns = amountPayableToIns; // postHospitalizationCalculationDTO.getRevisedEligibleAmount() - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
					postHospitalizationCalculationDTO.setPayableToInsAmt(payableToIns < 0 ? 0: payableToIns);
					postHospitalizationCalculationDTO.setBalancePremiumAmt(0);
					postHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(postHospitalizationCalculationDTO.getPayableToInsAmt() - postHospitalizationCalculationDTO.getBalancePremiumAmt());
					
					if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
						postHospitalizationCalculationDTO.setAmountAlreadyPaid(bean.getPostHospAmtAlreadyPaid() != null ? bean.getPostHospAmtAlreadyPaid() : 0);
						Integer deductedAmt = 0;
						if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() == null || (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && !bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
							deductedAmt = postHospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - postHospitalizationCalculationDTO.getAmountAlreadyPaid();
						} else {
							deductedAmt = postHospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt();
						}
//						Integer balanceAmt = hospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - (hospitalizationCalculationDTO.getAmountAlreadyPaid());
						postHospitalizationCalculationDTO.setBalanceToBePaid(deductedAmt < 0  ? 0 : deductedAmt);
					}
					
					if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
						bean.setIsPostHospApplicable(true);
					}
					
					if(!bean.getIsPostHospApplicable() || bean.getIsHospitalizationRejected()  || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP)) {
						postHospitalizationCalculationDTO.setPayableToInsAmt(0);
						postHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(0);
						postHospitalizationCalculationDTO.setBalanceToBePaid(0);
					}
//			}
	
		
		
			
			
//				PostHopitalizationDetailsDTO postHospitalizationCalculationDTO = bean.getPostHospitalizationCalculationDTO();
//				postHospitalizationCalculationDTO.setNetAmount(SHAUtils.getIntegerFromString(bean.getPostHospitalisationValue()) );
////				if(postHospitalizationCalculationDTO.getNetAmount() > 0) {
//					postHospitalizationCalculationDTO.setOtherInsurerAmt(SHAUtils.getIntegerFromString(otherInsurerValues));
//					Integer amountForPayment =  postHospitalizationCalculationDTO.getNetAmount() - postHospitalizationCalculationDTO.getOtherInsurerAmt();
//					postHospitalizationCalculationDTO.setAmountConsideredForPayment(amountForPayment < 0 ? 0 : amountForPayment);
//						if(bean.getPostHospAmt() < 0) {
//							bean.setPostHospAmt(0);
//						}
//						if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.FHO_NEW_PRODUCT)) {
//							bean.setPostHospAmt(postHospitalizationCalculationDTO.getAmountConsideredForPayment());
//						}
//						Float value =  (((!bean.getPostHospPercentage().equals(0d) ? bean.getPostHospPercentage().floatValue() : 7f)/ 100f) * bean.getPostHospAmt().floatValue());
//						int roundedValue = Math.round(value);
//						postHospitalizationCalculationDTO.setEligibleAmt(roundedValue);
//						
//						Integer amt = roundedValue;
//						Integer min2 = Math.min(amt, postHospitalizationCalculationDTO.getAmountConsideredForPayment() < 0 ? 0 : postHospitalizationCalculationDTO.getAmountConsideredForPayment());
//						postHospitalizationCalculationDTO.setAmountPayable(min2);
//						
//						List<Double> productCopay = bean.getProductCopay();
//						Double copayValue = 0d;
//						if(productCopay != null && !productCopay.isEmpty()) {
//							copayValue = productCopay.get(0);
//							if(productCopay.size() >=2){
//								copayValue = productCopay.get(1);
//							}
//						}
//						copayValue = bean.getAmountConsCopayPercentage() != null ? bean.getAmountConsCopayPercentage().doubleValue() : 0d;
//						postHospitalizationCalculationDTO.setCopayValue(copayValue.intValue());
//						Double copayAmount = (copayValue / 100) * postHospitalizationCalculationDTO.getAmountPayable();
//						Long roundedCopay = Math.round(copayAmount);
//						
//						postHospitalizationCalculationDTO.setCopayAmt(roundedCopay.intValue());
//						postHospitalizationCalculationDTO.setIsSIRestrictionAvail(bean.getIsSIRestrictionAvail());
//						Integer payableAmt = postHospitalizationCalculationDTO.getAmountPayable() - postHospitalizationCalculationDTO.getCopayAmt();
//						postHospitalizationCalculationDTO.setPayableAmt(payableAmt < 0 ? 0 : payableAmt);
//						postHospitalizationCalculationDTO.setMaxPayable(bean.getPostHospclaimRestrictionAmount() != null ? bean.getPostHospclaimRestrictionAmount().intValue() : 0);
//						postHospitalizationCalculationDTO.setAvaliableSumInsuredAftHosp(bean.getBalanceSIAftHosp());
//						postHospitalizationCalculationDTO.setPreviousRodPostHospamt(bean.getPreviousPostHospAmount());
//						postHospitalizationCalculationDTO.setRestrictedSIAftHosp(bean.getIsSIRestrictionAvail() ? bean.getSiRestrictionAmount() : 0);
//						Integer amtPayable  = postHospitalizationCalculationDTO.getMaxPayable() - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
//						postHospitalizationCalculationDTO.setMaxPayable(amtPayable < 0 ? 0 :amtPayable);
//						int min3 = Math.min(postHospitalizationCalculationDTO.getPayableAmt(), bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.FHO_NEW_PRODUCT) ? postHospitalizationCalculationDTO.getPayableAmt() :  amtPayable < 0 ? 0 :amtPayable);
//						if(amtPayable < 0) {
//							min3 = 0;
//						}
//						
//						Integer min = Math.min(postHospitalizationCalculationDTO.getPayableAmt(), Math.min((bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_ANOTHER_POLICY) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.FHO_NEW_PRODUCT)) ? postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp()  : postHospitalizationCalculationDTO.getMaxPayable(), Math.min(postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp(),(postHospitalizationCalculationDTO.getIsSIRestrictionAvail() ? (postHospitalizationCalculationDTO.getRestrictedSIAftHosp() > 0 ? postHospitalizationCalculationDTO.getRestrictedSIAftHosp() : 0 ) : postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp()))));
//						postHospitalizationCalculationDTO.setNetPayable(min);
//						
//						postHospitalizationCalculationDTO.setClaimRestrictionAmt(0);  
////						if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
////							postHospitalizationCalculationDTO.setPayableToInsAmt(postHospitalizationCalculationDTO.getPayableAmt());
////						} 
//						Integer eligibleAmt = postHospitalizationCalculationDTO.getNetPayable() - postHospitalizationCalculationDTO.getClaimRestrictionAmt();
//						postHospitalizationCalculationDTO.setRevisedEligibleAmount(eligibleAmt < 0 ? 0: eligibleAmt);
//						int amountPayableToIns = Math.min(postHospitalizationCalculationDTO.getRevisedEligibleAmount(), bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.FHO_NEW_PRODUCT) ?  postHospitalizationCalculationDTO.getRevisedEligibleAmount() : amtPayable < 0 ? 0 :amtPayable);
//						Integer payableToIns = amountPayableToIns; // postHospitalizationCalculationDTO.getRevisedEligibleAmount() - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
//						postHospitalizationCalculationDTO.setPayableToInsAmt(payableToIns < 0 ? 0: payableToIns);
//						postHospitalizationCalculationDTO.setBalancePremiumAmt(0);
//						postHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(postHospitalizationCalculationDTO.getPayableToInsAmt() - postHospitalizationCalculationDTO.getBalancePremiumAmt());
//						
//						if(!bean.getIsPostHospApplicable() || bean.getIsHospitalizationRejected()  || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP)) {
//							postHospitalizationCalculationDTO.setPayableToInsAmt(0);
//							postHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(0);
//						}
////				}
			
		}
		
		public static void setPreHospitalizationDetailsToDTO(PreauthDTO bean, String otherInsurerValues) {
			if(bean.getPreHospitalizaionFlag()) {
				PreHopitalizationDetailsDTO pretHospitalizationCalculationDTO = bean.getPreHospitalizationCalculationDTO();
				
				Double min = Math.min(bean.getPreHospitalizaionFlag() ? SHAUtils.getDoubleFromString(bean.getPreHospitalisationValue())  : 0d  , SHAUtils.getDoubleFromString(bean.getPreHospitalisationValue()));
//				if(min > 0) {
					pretHospitalizationCalculationDTO.setAmountConsidered(min.intValue());
					pretHospitalizationCalculationDTO.setOtherInsurerAmt(SHAUtils.getIntegerFromStringWithComma(otherInsurerValues));
					Integer deductibleAmt = pretHospitalizationCalculationDTO.getAmountConsidered() - pretHospitalizationCalculationDTO.getOtherInsurerAmt();
					pretHospitalizationCalculationDTO.setAmountConsideredForPayment(deductibleAmt < 0 ? 0 : deductibleAmt);
					List<Double> productCopay = bean.getProductCopay();
					Double copayValue = 0d;
					if(productCopay != null && !productCopay.isEmpty()) {
						copayValue = productCopay.get(0);
						if(productCopay.size() >=2){
							copayValue = productCopay.get(1);
						}
					}
					copayValue = bean.getAmountConsCopayPercentage() != null ? bean.getAmountConsCopayPercentage().doubleValue() : 0d;
					pretHospitalizationCalculationDTO.setCopayValue(copayValue);
					Double copayAmount = (copayValue / 100d) * pretHospitalizationCalculationDTO.getAmountConsideredForPayment();
					Long roundedCopay = Math.round(copayAmount);
					
					pretHospitalizationCalculationDTO.setCopayAmt(roundedCopay.intValue());
					Integer calcAmt = pretHospitalizationCalculationDTO.getAmountConsideredForPayment() - pretHospitalizationCalculationDTO.getCopayAmt();
					pretHospitalizationCalculationDTO.setPayableAmt(calcAmt < 0 ? 0 : calcAmt);
					Integer existingHospAmount = bean.getBalanceSIAftHosp() + bean.getHospAmountInBiling();
					existingHospAmount = existingHospAmount > 0 ? existingHospAmount : 0;
					/**
					 * IMSSUPPOR-24150
					 */
					Integer currentHospAmount = 0;
					if(bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) &&
							! bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getHospitalisationFlag().equalsIgnoreCase("Y")
							&& ! bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y")){
						currentHospAmount = existingHospAmount;
					}else{
						currentHospAmount = existingHospAmount - ((bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) ? bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : bean.getHospitalizationCalculationDTO().getPayableToHospitalAftTDSAmt());
					}
					currentHospAmount = currentHospAmount > 0 ? currentHospAmount : 0;
					Integer balanceSIAftHospAndPostHosp = (existingHospAmount.equals(currentHospAmount) ? bean.getBalanceSIAftHosp() : currentHospAmount)/* - bean.getPostHospitalizationCalculationDTO().getPreviousRodPostHospamt()*/;
					//Integer balanceSIAftHospAndPostHosp = currentHospAmount - bean.getPostHospitalizationCalculationDTO().getPreviousRodPostHospamt();
					if(bean.getPostHospitalizaionFlag()) {
						//IMSSUPPOR-28250 - Added as like post hospitalization calculation - 20-Apr-2019 - Dinesh M(Scenario: reimbursement rod 1 settled with pre & post(max payable upto 5000), reconsideration rod pre not able to pay avail bsi amt since avail_suminsured_afr_hosp&post is deducting the -revised post payable amt)
						//balanceSIAftHospAndPostHosp = balanceSIAftHospAndPostHosp - bean.getPostHospitalizationCalculationDTO().getPayableToInsAmt();
						if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
							balanceSIAftHospAndPostHosp = balanceSIAftHospAndPostHosp - bean.getPostHospitalizationCalculationDTO().getBalanceToBePaid();
						} else {	
								balanceSIAftHospAndPostHosp = balanceSIAftHospAndPostHosp - bean.getPostHospitalizationCalculationDTO().getPayableToInsAmt();
						}
						
					}
					
					//IMSSUPPOR-24282								IMSSUPPOR-25174
						if(bean.getPartialHospitalizaionFlag() /*&& ! (existingHospAmount.equals(currentHospAmount))*/  && (bean.getHospitalizationCalculationDTO().getPreauthAppAmt() != null && bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt() != null)){
							Integer preauthApprovedAmt = 0;
							if(bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt() > 0){
								preauthApprovedAmt = Math.min(bean.getHospitalizationCalculationDTO().getPreauthAppAmt(), bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt());
							}else{
								preauthApprovedAmt = bean.getHospitalizationCalculationDTO().getPreauthAppAmt();
							}
							
							balanceSIAftHospAndPostHosp = balanceSIAftHospAndPostHosp -preauthApprovedAmt;
							//IMSSUPPOR-26536
							if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
								if(bean.getHospitalizationCalculationDTO().getAmountAlreadyPaid() != null){
									balanceSIAftHospAndPostHosp = balanceSIAftHospAndPostHosp - bean.getHospitalizationCalculationDTO().getAmountAlreadyPaid();
								}
							}
						}
					
					pretHospitalizationCalculationDTO.setAvailableSIAftHosp(balanceSIAftHospAndPostHosp < 0 ? 0 : balanceSIAftHospAndPostHosp);
					//IMSSUPPOR-26536 
					Integer totalAvailableSI = pretHospitalizationCalculationDTO.getAvailableSIAftHosp() + ((bean.getPreHospAmtAlreadyPaid() != null ? bean.getPreHospAmtAlreadyPaid() : 0));
					pretHospitalizationCalculationDTO.setAvailableSIAftHosp(totalAvailableSI);
					Integer siRestrictionAftHospAndPostHosp = 0;
					if(bean.getIsSIRestrictionAvail()) {
						siRestrictionAftHospAndPostHosp = bean.getSiRestrictionAmount() - bean.getPostHospitalizationCalculationDTO().getPreviousRodPostHospamt();
						if(bean.getPostHospitalizaionFlag()) {
							siRestrictionAftHospAndPostHosp = siRestrictionAftHospAndPostHosp - bean.getPostHospitalizationCalculationDTO().getPayableToInsAmt();
						}
					}
					pretHospitalizationCalculationDTO.setIsSIRestrictionAvail(bean.getIsSIRestrictionAvail());
					pretHospitalizationCalculationDTO.setRestrictedSIAftHosp(siRestrictionAftHospAndPostHosp < 0 ? 0 : siRestrictionAftHospAndPostHosp);
					int minAmount = Math.min(pretHospitalizationCalculationDTO.getPayableAmt(), Math.min(bean.getIsSIRestrictionAvail() ? (pretHospitalizationCalculationDTO.getRestrictedSIAftHosp() > 0 ? pretHospitalizationCalculationDTO.getRestrictedSIAftHosp() : 0) : pretHospitalizationCalculationDTO.getAvailableSIAftHosp(), pretHospitalizationCalculationDTO.getAvailableSIAftHosp()));
					if((bean.getClaimDTO().getClaimSectionCode().equalsIgnoreCase(ReferenceTable.BARIATRIC_SECTION_CODE)) || ( bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue() != null && bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue().equalsIgnoreCase(ReferenceTable.BARIATRIC_SECTION_CODE))) {
						List<DiagnosisProcedureTableDTO> medicalDecisionTableDTO = bean.getPreauthMedicalDecisionDetails().getMedicalDecisionTableDTO();
//						for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : medicalDecisionTableDTO) {
//							if(diagnosisProcedureTableDTO.getS)
//						}
						pretHospitalizationCalculationDTO.setAvaliableSublimitBalanceForBariatric(bean.getBariatricAvailableAmount().intValue());
						minAmount = Math.min(minAmount, pretHospitalizationCalculationDTO.getAvaliableSublimitBalanceForBariatric());
					}
					pretHospitalizationCalculationDTO.setNetPayable(minAmount);
					
					pretHospitalizationCalculationDTO.setBalancePremiumAmt(0);
					Integer calculatedAmt = pretHospitalizationCalculationDTO.getNetPayable() - pretHospitalizationCalculationDTO.getClaimRestrictionAmt();
					pretHospitalizationCalculationDTO.setRevisedEligibleAmount(calculatedAmt < 0 ? 0: calculatedAmt);
					pretHospitalizationCalculationDTO.setPreviousRodPreviousPrehospAmt(bean.getPreviousPreHospAmount());
					Integer payableToInsAmt = pretHospitalizationCalculationDTO.getRevisedEligibleAmount();
					pretHospitalizationCalculationDTO.setPayableToInsAmt(payableToInsAmt < 0 ? 0 : payableToInsAmt);
					Integer aftPremiumAmt = pretHospitalizationCalculationDTO.getPayableToInsAmt() - pretHospitalizationCalculationDTO.getBalancePremiumAmt();
					pretHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(aftPremiumAmt < 0 ? 0: aftPremiumAmt);
					if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
						pretHospitalizationCalculationDTO.setAmountAlreadyPaid(bean.getPreHospAmtAlreadyPaid() != null ? bean.getPreHospAmtAlreadyPaid() : 0);
						Integer deductedAmt = 0;
						if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() == null || (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && !bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
							deductedAmt = pretHospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - pretHospitalizationCalculationDTO.getAmountAlreadyPaid();
						} else {
							deductedAmt = pretHospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt();
						}
//						Integer balanceAmt = hospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - (hospitalizationCalculationDTO.getAmountAlreadyPaid());
						pretHospitalizationCalculationDTO.setBalanceToBePaid(deductedAmt < 0  ? 0 : deductedAmt);
					}
					
					if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
							ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
						bean.setIsPreHospApplicable(true);
					}
					
					if(!bean.getIsPreHospApplicable() || bean.getIsHospitalizationRejected() || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP)) {
						pretHospitalizationCalculationDTO.setPayableToInsAmt(0);
						pretHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(0);
						pretHospitalizationCalculationDTO.setBalanceToBePaid(0);
					}
					else if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
						if(bean.getIsPreHospApplicable() && (bean.getPreauthDataExtractionDetails().getCauseOfInjury() != null && (ReferenceTable.ROAD_TRAFFIC_ACCIDENT).equals(bean.getPreauthDataExtractionDetails().getCauseOfInjury().getId()) &&
								bean.getPreauthDataExtractionDetails().getHospitalisationDueTo() != null && (ReferenceTable.INJURY_MASTER_ID).equals(bean.getPreauthDataExtractionDetails().getHospitalisationDueTo().getId()))){
						pretHospitalizationCalculationDTO.setPayableToInsAmt(0);
						pretHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(0);
						pretHospitalizationCalculationDTO.setBalanceToBePaid(0);
						}
					}
//				}
			}
		}
		
		public static void setHospitalizationDetailsToDTOForBilling(PreauthDTO bean) {
			HopitalizationCalulationDetailsDTO hospitalizationCalculationDTO = bean.getHospitalizationCalculationDTO();
//			Integer amount = (bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0) + ((bean.getHospDiscountAmount() != null && bean.getHospitalizaionFlag()) ? bean.getHospDiscountAmount().intValue() : 0 );
			Integer amount = (bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0);
			hospitalizationCalculationDTO.setNetPayableAmt(amount);
			hospitalizationCalculationDTO.setPreauthAppAmt(0);
			String preauthApprAmt = "0";
			
			if(hospitalizationCalculationDTO.getNetPayableAmt() < 0) {
				hospitalizationCalculationDTO.setNetPayableAmt(0);
			}
			
//			if(!bean.getPreviousPreauthTableDTO().isEmpty()) {
//				for (PreviousPreAuthTableDTO preauth : bean.getPreviousPreauthTableDTO()) {
//					preauthApprAmt = preauth.getApprovedAmt();
//				}
//			}
//			if(SHAUtils.isValidDouble(preauthApprAmt)) {
//				Double valueOf = Double.valueOf(preauthApprAmt);
//				hospitalizationCalculationDTO.setPreauthAppAmt(valueOf.intValue());
//				hospitalizationCalculationDTO.setPreauthAppAmt(this.previousPreAuthDetailsTableObj.getTotalApprovedAmt() != null ? this.previousPreAuthDetailsTableObj.getTotalApprovedAmt().intValue() : 0);
//			}
			
//			if(bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
//				if((bean.getPartialHospitalizaionFlag()) &&  bean.getFAApprovedAmount() != null && bean.getFAApprovedAmount() != 0d && hospitalizationCalculationDTO.getPreauthAppAmt() > bean.getFAApprovedAmount()) {
//					hospitalizationCalculationDTO.setPreauthAppAmt(bean.getFAApprovedAmount().intValue());
//				}
//			}
			hospitalizationCalculationDTO.setHospitalDiscount(bean.getHospDiscountAmount() != null  ?bean.getHospDiscountAmount().intValue() : 0 );
			hospitalizationCalculationDTO.setPayableToInsAmt(0);
			hospitalizationCalculationDTO.setClaimRestrictionAmt((bean.getClaimRestrictionAmount() != null && bean.getClaimRestrictionAmount() > 0) ?  bean.getClaimRestrictionAmount().intValue() : 0);
			
			
			/*if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL)) {
				
				DBCalculationService dbCalculationService = new DBCalculationService();
				
				Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(bean.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), bean.getNewIntimationDTO().getPolicy().getKey());
				
				if(insuredSumInsured != null && insuredSumInsured > 0) {
					Float value =  ((50f/ 100f) * insuredSumInsured.floatValue());
					int roundedValue = Math.round(value);
					if(hospitalizationCalculationDTO.getNetPayableAmt() > roundedValue) {
						hospitalizationCalculationDTO.setClaimRestrictionAmt(roundedValue);
					} else {
						hospitalizationCalculationDTO.setClaimRestrictionAmt(0);
					}
				}
			}*/
			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				hospitalizationCalculationDTO.setPayableToInsAmt(hospitalizationCalculationDTO.getNetPayableAmt() - hospitalizationCalculationDTO.getClaimRestrictionAmt() - hospitalizationCalculationDTO.getPreauthAppAmt());
				if(hospitalizationCalculationDTO.getPayableToInsAmt() < 0) {
					hospitalizationCalculationDTO.setPayableToInsAmt(0);
				}
				hospitalizationCalculationDTO.setPayableToHospitalAmt(bean.getPayableToHospAmt() != null ? bean.getPayableToHospAmt().intValue() : 0);
				Integer afterDiscountAmt = hospitalizationCalculationDTO.getPayableToHospitalAmt() - hospitalizationCalculationDTO.getHospitalDiscount();
				hospitalizationCalculationDTO.setAfterHospitalDiscount(afterDiscountAmt < 0 ? 0 : afterDiscountAmt);
				hospitalizationCalculationDTO.setTdsAmt(0);
				hospitalizationCalculationDTO.setPayableToHospitalAftTDSAmt(afterDiscountAmt < 0 ? 0 : afterDiscountAmt);
				
				hospitalizationCalculationDTO.setBalancePremiumAmt(0);
				hospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(hospitalizationCalculationDTO.getPayableToInsAmt() - hospitalizationCalculationDTO.getBalancePremiumAmt());
				
				if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest() ) {
					hospitalizationCalculationDTO.setAmountAlreadyPaid(bean.getHospAmountAlreadyPaid() != null ? bean.getHospAmountAlreadyPaid() : 0);
					Integer balancedAmt = 0;
					if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() == null || (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && !bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
						balancedAmt = hospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - (hospitalizationCalculationDTO.getAmountAlreadyPaid());
					} else {
						balancedAmt = hospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt();
					}
//					Integer balanceAmt = hospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - (hospitalizationCalculationDTO.getAmountAlreadyPaid());
					hospitalizationCalculationDTO.setBalanceToBePaid(balancedAmt < 0  ? 0 : balancedAmt);
				}
				
				
			} else if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {
				hospitalizationCalculationDTO.setPayableToHospitalAmt((hospitalizationCalculationDTO.getPreauthAppAmt() != 0 ? Math.min(hospitalizationCalculationDTO.getPreauthAppAmt(), (hospitalizationCalculationDTO.getNetPayableAmt() - hospitalizationCalculationDTO.getClaimRestrictionAmt())) : (hospitalizationCalculationDTO.getNetPayableAmt() - hospitalizationCalculationDTO.getClaimRestrictionAmt())));
				if(hospitalizationCalculationDTO.getPayableToHospitalAmt() < 0) {
					hospitalizationCalculationDTO.setPayableToHospitalAmt(0);
				}
				hospitalizationCalculationDTO.setBalancePremiumAmt(0);
				hospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(0);
				
				hospitalizationCalculationDTO.setTdsAmt(0);
				Integer discountAmt = hospitalizationCalculationDTO.getHospitalDiscount() != null ? hospitalizationCalculationDTO.getHospitalDiscount() : 0;
				Integer aftDiscountAmt  = hospitalizationCalculationDTO.getPayableToHospitalAmt() - discountAmt;
				hospitalizationCalculationDTO.setPayableToHospitalAftTDSAmt(aftDiscountAmt);
//				hospitalizationCalculationDTO.setPayableToHospitalAftTDSAmt(hospitalizationCalculationDTO.getPayableToHospitalAmt());
				
				if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
					hospitalizationCalculationDTO.setAmountAlreadyPaid(bean.getHospAmountAlreadyPaid());
					Integer balanceAmt = 0;
					if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() == null || (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && !bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
						balanceAmt = hospitalizationCalculationDTO.getPayableToHospitalAftTDSAmt() - (hospitalizationCalculationDTO.getAmountAlreadyPaid());
					} else {
						balanceAmt = hospitalizationCalculationDTO.getPayableToHospitalAftTDSAmt();
					}
					
					hospitalizationCalculationDTO.setBalanceToBePaid(balanceAmt < 0  ? 0 : balanceAmt);
				}
				
			} else {
				System.out.println("Something went wrong .... Doc Received from not updated.. Please check "+ bean.getKey());
			}
			
			if(bean.getHospitalizaionFlag() && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
				hospitalizationCalculationDTO.setPreauthAppAmtBeforePremium(hospitalizationCalculationDTO.getPreauthAppAmt() + uniqueInstallmentAmount);
				hospitalizationCalculationDTO.setInstallmentAmount(uniqueInstallmentAmount);
			}
			
		}
		
		public static void setHospitalizationDetailsToDTOForFinancial(PreauthDTO bean) {
			HopitalizationCalulationDetailsDTO hospitalizationCalculationDTO = bean.getHospitalizationCalculationDTO();
//			Integer amount = (bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0) + ((bean.getHospDiscountAmount() != null && bean.getHospitalizaionFlag()) ? bean.getHospDiscountAmount().intValue() : 0 );
//			Integer amount = (bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0);
			Integer amount = 0;
			if(bean.getIsReverseAllocation()) {
				amount = (bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0) + ((bean.getHospDiscountAmount() != null && bean.getHospitalizaionFlag()) ? bean.getHospDiscountAmount().intValue() : 0 );
			} else {
				amount = (bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0);
			}
			hospitalizationCalculationDTO.setNetPayableAmt(amount);
			hospitalizationCalculationDTO.setPreauthAppAmt(0);
			String preauthApprAmt = "0";
			
			if(hospitalizationCalculationDTO.getNetPayableAmt() < 0) {
				hospitalizationCalculationDTO.setNetPayableAmt(0);
			}
			
//			if(!bean.getPreviousPreauthTableDTO().isEmpty()) {
//				for (PreviousPreAuthTableDTO preauth : bean.getPreviousPreauthTableDTO()) {
//					preauthApprAmt = preauth.getApprovedAmt();
//				}
//			}
//			if(SHAUtils.isValidDouble(preauthApprAmt)) {
//				Double valueOf = Double.valueOf(preauthApprAmt);
//				hospitalizationCalculationDTO.setPreauthAppAmt(valueOf.intValue());
//				hospitalizationCalculationDTO.setPreauthAppAmt(this.previousPreAuthDetailsTableObj.getTotalApprovedAmt() != null ? this.previousPreAuthDetailsTableObj.getTotalApprovedAmt().intValue() : 0);
//			}

//			if(bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
//				hospitalizationCalculationDTO.setPreauthAppAmt((this.previousPreAuthDetailsTableObj != null && this.previousPreAuthDetailsTableObj.getTotalApprovedAmt() != null) ? this.previousPreAuthDetailsTableObj.getTotalApprovedAmt().intValue() : 0);
//			}
//			
//			if(bean.getIsHospitalizationRepeat()) {
//				hospitalizationCalculationDTO.setPreauthAppAmt(0);
//			}
			
//			if(bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
//				if((bean.getPartialHospitalizaionFlag()) &&  bean.getFAApprovedAmount() != null && bean.getFAApprovedAmount() != 0d && hospitalizationCalculationDTO.getPreauthAppAmt() > bean.getFAApprovedAmount()) {
//					hospitalizationCalculationDTO.setPreauthAppAmt(bean.getFAApprovedAmount().intValue());
//				}
//			}
			hospitalizationCalculationDTO.setHospitalDiscount(bean.getHospDiscountAmount() != null  ?bean.getHospDiscountAmount().intValue() : 0 );
			hospitalizationCalculationDTO.setPayableToInsAmt(0);
			hospitalizationCalculationDTO.setClaimRestrictionAmt((bean.getClaimRestrictionAmount() != null && bean.getClaimRestrictionAmount() > 0) ?  bean.getClaimRestrictionAmount().intValue() : 0);
			
			/*if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL)) {
				
				DBCalculationService dbCalculationService = new DBCalculationService();
				
				Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(bean.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), bean.getNewIntimationDTO().getPolicy().getKey());
				
				if(insuredSumInsured != null && insuredSumInsured > 0) {
					Float value =  ((50f/ 100f) * insuredSumInsured.floatValue());
					int roundedValue = Math.round(value);
					if(hospitalizationCalculationDTO.getNetPayableAmt() > roundedValue) {
						hospitalizationCalculationDTO.setClaimRestrictionAmt(roundedValue);
					} else {
						hospitalizationCalculationDTO.setClaimRestrictionAmt(0);
					}
				}
			}*/
//			if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL)) {
//				if(hospitalizationCalculationDTO.getNetPayableAmt() > 0) {
//					Float value =  ((50f/ 100f) * hospitalizationCalculationDTO.getNetPayableAmt().floatValue());
//					int roundedValue = Math.round(value);
//					hospitalizationCalculationDTO.setClaimRestrictionAmt(roundedValue);
//				}
//			}
			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				hospitalizationCalculationDTO.setPayableToInsAmt(hospitalizationCalculationDTO.getNetPayableAmt() - hospitalizationCalculationDTO.getClaimRestrictionAmt() - hospitalizationCalculationDTO.getPreauthAppAmt());
				if(hospitalizationCalculationDTO.getPayableToInsAmt() < 0) {
					hospitalizationCalculationDTO.setPayableToInsAmt(0);
				}
				
				hospitalizationCalculationDTO.setPayableToHospitalAmt(bean.getPayableToHospAmt() != null ? bean.getPayableToHospAmt().intValue() : 0);
				Integer afterDiscountAmt = hospitalizationCalculationDTO.getPayableToHospitalAmt() - hospitalizationCalculationDTO.getHospitalDiscount();
				hospitalizationCalculationDTO.setAfterHospitalDiscount(afterDiscountAmt < 0 ? 0 : afterDiscountAmt);
				hospitalizationCalculationDTO.setTdsAmt(0);
				hospitalizationCalculationDTO.setPayableToHospitalAftTDSAmt(afterDiscountAmt < 0 ? 0 : afterDiscountAmt);
				
				hospitalizationCalculationDTO.setBalancePremiumAmt(0);
				
				Integer balanceAmt = hospitalizationCalculationDTO.getPayableToInsAmt() - hospitalizationCalculationDTO.getBalancePremiumAmt();
				hospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(balanceAmt < 0 ? 0 : balanceAmt);
				
				if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
					hospitalizationCalculationDTO.setAmountAlreadyPaid(bean.getHospAmountAlreadyPaid() != null ? bean.getHospAmountAlreadyPaid() : 0);
					Integer balancedAmt = 0;
					if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() == null || (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && !bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
						balancedAmt = hospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - (hospitalizationCalculationDTO.getAmountAlreadyPaid());
					} else {
						balancedAmt = hospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt();
					}
//					Integer balancedAmt = hospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - (hospitalizationCalculationDTO.getAmountAlreadyPaid());
					
//					Integer balancedAmt = hospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - hospitalizationCalculationDTO.getAmountAlreadyPaid();
					hospitalizationCalculationDTO.setBalanceToBePaid(balancedAmt < 0  ? 0 : balancedAmt);
				}
				
			} else if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {
				hospitalizationCalculationDTO.setPayableToHospitalAmt((hospitalizationCalculationDTO.getPreauthAppAmt() != 0 ? Math.min(hospitalizationCalculationDTO.getPreauthAppAmt(), (hospitalizationCalculationDTO.getNetPayableAmt() - hospitalizationCalculationDTO.getClaimRestrictionAmt())) : (hospitalizationCalculationDTO.getNetPayableAmt() - hospitalizationCalculationDTO.getClaimRestrictionAmt())));
				if(hospitalizationCalculationDTO.getPayableToHospitalAmt() < 0) {
					hospitalizationCalculationDTO.setPayableToHospitalAmt(0);
				}
				hospitalizationCalculationDTO.setBalancePremiumAmt(0);
				hospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(0);
				
				hospitalizationCalculationDTO.setTdsAmt(0);
				Integer discountAmt = hospitalizationCalculationDTO.getHospitalDiscount() != null ? hospitalizationCalculationDTO.getHospitalDiscount() : 0;
				Integer aftDiscountAmt  = hospitalizationCalculationDTO.getPayableToHospitalAmt() - discountAmt;
				hospitalizationCalculationDTO.setPayableToHospitalAftTDSAmt(aftDiscountAmt);
//				hospitalizationCalculationDTO.setPayableToHospitalAftTDSAmt(hospitalizationCalculationDTO.getPayableToHospitalAmt());
				
				if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
					hospitalizationCalculationDTO.setAmountAlreadyPaid(bean.getHospAmountAlreadyPaid());
					Integer balanceAmt = 0;
					if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() == null || (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && !bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
						balanceAmt = hospitalizationCalculationDTO.getPayableToHospitalAftTDSAmt() - (hospitalizationCalculationDTO.getAmountAlreadyPaid());
					} else {
						balanceAmt = hospitalizationCalculationDTO.getPayableToHospitalAftTDSAmt();
					}
					
					hospitalizationCalculationDTO.setBalanceToBePaid(balanceAmt < 0  ? 0 : balanceAmt);
				}
				
			} else {
				System.out.println("Something went wrong .... Doc Received from not updated.. Please check "+ bean.getKey());
			}

			if(bean.getHospitalizaionFlag() && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
				hospitalizationCalculationDTO.setPreauthAppAmtBeforePremium(hospitalizationCalculationDTO.getPreauthAppAmt() + uniqueInstallmentAmount);
				hospitalizationCalculationDTO.setInstallmentAmount(uniqueInstallmentAmount);
			}
			//cr2019184
			if(bean.getHospitalizaionFlag() && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) 
					&& bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)) {
				Integer uniqueInstallmentAmount = PremiaService.getInstance().getPolicyInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
				hospitalizationCalculationDTO.setPreauthAppAmtBeforePremium(hospitalizationCalculationDTO.getPreauthAppAmt() + uniqueInstallmentAmount);
				hospitalizationCalculationDTO.setInstallmentAmount(uniqueInstallmentAmount);
			}
		}
		
		
		public static void setPreHospitalizationDetailsToDTOForChange(PreauthDTO bean, String otherInsurerValues) {
				PreHopitalizationDetailsDTO pretHospitalizationCalculationDTO = bean.getPreHospitalizationCalculationDTO();
				
				Double min = Math.min(bean.getPreHospitalizaionFlag() ? SHAUtils.getDoubleFromString(bean.getPreHospitalisationValue())  : 0d  , SHAUtils.getDoubleFromString(bean.getPreHospitalisationValue()));
//				if(min > 0) {
					pretHospitalizationCalculationDTO.setAmountConsidered(min.intValue());
					pretHospitalizationCalculationDTO.setOtherInsurerAmt(SHAUtils.getIntegerFromStringWithComma(otherInsurerValues));
					Integer deductibleAmt = pretHospitalizationCalculationDTO.getAmountConsidered() - pretHospitalizationCalculationDTO.getOtherInsurerAmt();
					pretHospitalizationCalculationDTO.setAmountConsideredForPayment(deductibleAmt < 0 ? 0 : deductibleAmt);
					List<Double> productCopay = bean.getProductCopay();
					Double copayValue = 0d;
					if(productCopay != null && !productCopay.isEmpty()) {
						copayValue = productCopay.get(0);
						if(productCopay.size() >=2){
							copayValue = productCopay.get(1);
						}
					}
					copayValue = bean.getAmountConsCopayPercentage() != null ? bean.getAmountConsCopayPercentage().doubleValue() : 0d;
					pretHospitalizationCalculationDTO.setCopayValue(copayValue);
					Double copayAmount = (copayValue / 100d) * pretHospitalizationCalculationDTO.getAmountConsideredForPayment();
					Long roundedCopay = Math.round(copayAmount);
					
					pretHospitalizationCalculationDTO.setCopayAmt(roundedCopay.intValue());
					Integer calcAmt = pretHospitalizationCalculationDTO.getAmountConsideredForPayment() - pretHospitalizationCalculationDTO.getCopayAmt();
					pretHospitalizationCalculationDTO.setPayableAmt(calcAmt < 0 ? 0 : calcAmt);
					Integer balanceSIAftHospAndPostHosp = bean.getBalanceSIAftHosp() - bean.getPostHospitalizationCalculationDTO().getPreviousRodPostHospamt();
					if(bean.getPostHospitalizaionFlag()) {
						balanceSIAftHospAndPostHosp = balanceSIAftHospAndPostHosp - bean.getPostHospitalizationCalculationDTO().getPayableToInsAmt();
					}
					
					/*if(bean.getPartialHospitalizaionFlag()  && (bean.getHospitalizationCalculationDTO().getPreauthAppAmt() != null && bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt() != null)){
						Integer preauthApprovedAmt = 0;
						if(bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt() > 0){
							preauthApprovedAmt = Math.min(bean.getHospitalizationCalculationDTO().getPreauthAppAmt(), bean.getHospitalizationCalculationDTO().getPayableToHospitalAmt());
						}else{
							preauthApprovedAmt = bean.getHospitalizationCalculationDTO().getPreauthAppAmt();
						}
						balanceSIAftHospAndPostHosp = balanceSIAftHospAndPostHosp - preauthApprovedAmt;
					}*/
					
					pretHospitalizationCalculationDTO.setAvailableSIAftHosp(balanceSIAftHospAndPostHosp < 0 ? 0 : balanceSIAftHospAndPostHosp);
					Integer siRestrictionAftHospAndPostHosp = 0;
					if(bean.getIsSIRestrictionAvail()) {
						siRestrictionAftHospAndPostHosp = bean.getSiRestrictionAmount() - bean.getPostHospitalizationCalculationDTO().getPreviousRodPostHospamt();
						if(bean.getPostHospitalizaionFlag()) {
							siRestrictionAftHospAndPostHosp = siRestrictionAftHospAndPostHosp - bean.getPostHospitalizationCalculationDTO().getPayableToInsAmt();
						}
					}
					pretHospitalizationCalculationDTO.setIsSIRestrictionAvail(bean.getIsSIRestrictionAvail());
					pretHospitalizationCalculationDTO.setRestrictedSIAftHosp(siRestrictionAftHospAndPostHosp < 0 ? 0 : siRestrictionAftHospAndPostHosp);
					pretHospitalizationCalculationDTO.setNetPayable(Math.min(pretHospitalizationCalculationDTO.getPayableAmt(), Math.min(bean.getIsSIRestrictionAvail() ? (pretHospitalizationCalculationDTO.getRestrictedSIAftHosp() > 0 ? pretHospitalizationCalculationDTO.getRestrictedSIAftHosp() : 0) : pretHospitalizationCalculationDTO.getAvailableSIAftHosp(), pretHospitalizationCalculationDTO.getAvailableSIAftHosp())));
					
					pretHospitalizationCalculationDTO.setBalancePremiumAmt(0);
					Integer calculatedAmt = pretHospitalizationCalculationDTO.getNetPayable() - pretHospitalizationCalculationDTO.getClaimRestrictionAmt();
					pretHospitalizationCalculationDTO.setRevisedEligibleAmount(calculatedAmt < 0 ? 0: calculatedAmt);
					pretHospitalizationCalculationDTO.setPreviousRodPreviousPrehospAmt(bean.getPreviousPreHospAmount());
					Integer payableToInsAmt = pretHospitalizationCalculationDTO.getRevisedEligibleAmount();
					pretHospitalizationCalculationDTO.setPayableToInsAmt(payableToInsAmt < 0 ? 0 : payableToInsAmt);
					Integer aftPremiumAmt = pretHospitalizationCalculationDTO.getPayableToInsAmt() - pretHospitalizationCalculationDTO.getBalancePremiumAmt();
					pretHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(aftPremiumAmt < 0 ? 0: aftPremiumAmt);
					if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
						pretHospitalizationCalculationDTO.setAmountAlreadyPaid(bean.getPreHospAmtAlreadyPaid() != null ? bean.getPreHospAmtAlreadyPaid() : 0);
						Integer deductedAmt = 0;
						if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() == null || (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && !bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
							deductedAmt = pretHospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - pretHospitalizationCalculationDTO.getAmountAlreadyPaid();
						} else {
							deductedAmt = pretHospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt();
						}
//						Integer balanceAmt = hospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - (hospitalizationCalculationDTO.getAmountAlreadyPaid());
						pretHospitalizationCalculationDTO.setBalanceToBePaid(deductedAmt < 0  ? 0 : deductedAmt);
					}
					if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
						bean.setIsPreHospApplicable(true);
					}	
					if(!bean.getIsPreHospApplicable() || bean.getIsHospitalizationRejected() || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP)) {
						pretHospitalizationCalculationDTO.setPayableToInsAmt(0);
						pretHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(0);
						pretHospitalizationCalculationDTO.setBalanceToBePaid(0);
					}
//				}
		}

		public static Boolean isRoomRentAvailable(List<ClaimAmountDetails> claimedAmountDetailsList) {
			Boolean isRoomRentApplied = false;
			for (ClaimAmountDetails claimAmountDetails : claimedAmountDetailsList) {
				if((claimAmountDetails.getBenefitId().equals(9l) || claimAmountDetails.getBenefitId().equals(8l)) && claimAmountDetails.getPaybleAmount() != null && claimAmountDetails.getPaybleAmount() > 0f ) {
					isRoomRentApplied = true;
				}
			}
			return isRoomRentApplied;
		}
		
		public static Map<Long, Long> getCashlessTableMapping() {
			Map<Long, Long> cashlessTableMap = new WeakHashMap<Long, Long>();
			cashlessTableMap.put(8l, 8l);   // RoomRent
			cashlessTableMap.put(9l, 10l);  // ICU Room Rent
			cashlessTableMap.put(22l, 86l); //ICCU Room Rent
			cashlessTableMap.put(10l, 30l); // OT Charges
			cashlessTableMap.put(11l, 41l); // Profession Charges
			cashlessTableMap.put(12l, 26l); // Investigation & Diagnostics
			cashlessTableMap.put(13l, 27l); // a) Medicines 
			cashlessTableMap.put(14l, 24l); // c) Implant/Stunt/Valve/Pacemaker/Etc
			cashlessTableMap.put(15l, 12l); // ambulance
			cashlessTableMap.put(16l, 34l); // ANH Package
			cashlessTableMap.put(17l, 34l); // Composite Package
			cashlessTableMap.put(18l, 34l); // Other Package
			cashlessTableMap.put(19l, 46l); // Others
		    cashlessTableMap.put(20l, 78l); //Sales and Other cess

		    cashlessTableMap.put(21l, 23l); //Discount in hospital Bill
		    
		    cashlessTableMap.put(23l, 27l); // b) Consumables
		    
		    cashlessTableMap.put(26l, 153l); // Network Hospital Discount
		    
			return cashlessTableMap;
		}
		
		public static Map<Long, String> getReimbItemNameMapping() {
			Map<Long, String> cashlessTableMap = new WeakHashMap<Long, String>();
			cashlessTableMap.put(8l, "Room Rent");   // RoomRent
			cashlessTableMap.put(10l, "ICU Charges");  // ICU Room Rent
			cashlessTableMap.put(30l, "OT Charges"); // OT Charges
			cashlessTableMap.put(41l, "Professional fees"); // Profession Charges
			cashlessTableMap.put(26l, "Investiagation & Diagnoistics"); // Investigation & Diagnostics
			cashlessTableMap.put(27l, "Medicines and Consumables"); // a) Medicines and Consumables
			cashlessTableMap.put(24l, "Implant/Stunt/Valve/Pacemaker/Etc"); // b) Implant/Stunt/Valve/Pacemaker/Etc
			cashlessTableMap.put(12l, "Ambulance"); // ambulance
			cashlessTableMap.put(34l, "Package charges"); // ANH Package
			cashlessTableMap.put(34l, "Package charges"); // Composite Package
			cashlessTableMap.put(34l, "Package charges"); // Other Package
			cashlessTableMap.put(46l, "Others"); // Others
			cashlessTableMap.put(78l, "Sales and Others Cess");
			cashlessTableMap.put(86l, "ICCU Room Rent");
			cashlessTableMap.put(151l, "Others with Proportionate Deduction");
			cashlessTableMap.put(152l, "Others without Proportionate Deduction");
			
			cashlessTableMap.put(23l, "Discount in Hospital Bill"); //Discount in hospital Bill
			cashlessTableMap.put(153l, "Network Hospital Discount"); //Network Hospital Discount
			
			return cashlessTableMap;
		}
		
		private static Map<Long, Long> getMap(Long irdaLevel1, Long irdaLevel2) {
			Map<Long, Long> hashMap = new WeakHashMap<Long, Long>();
			hashMap.put(irdaLevel1, irdaLevel2);
			return hashMap;
		}
		
		public static Map<Long, Map<Long, Long>> getIRDALEVELMapping() {
			Map<Long, Map<Long, Long>> cashlessTableMap = new WeakHashMap<Long, Map<Long, Long>>();
			cashlessTableMap.put(8l, getMap(8l, 8l));   // RoomRent
			cashlessTableMap.put(10l, getMap(9l, 12l));  // ICU Room Rent
			cashlessTableMap.put(30l, getMap(10l, 17l)); // OT Charges
			cashlessTableMap.put(41l, getMap(12l, 23l)); // Profession Charges
			cashlessTableMap.put(26l, getMap(13l, 0l)); // Investigation & Diagnostics
			cashlessTableMap.put(27l, getMap(11l, 21l)); // a) Medicines and Consumables
			cashlessTableMap.put(24l, getMap(11l, 21l)); // b) Implant/Stunt/Valve/Pacemaker/Etc
			cashlessTableMap.put(12l, getMap(14l, 40l)); // ambulance
			cashlessTableMap.put(34l, getMap(16l, 50l)); // ANH Package
			cashlessTableMap.put(34l, getMap(16l, 0l)); // Composite Package
			cashlessTableMap.put(34l, getMap(16l, 0l)); // Other Package
			cashlessTableMap.put(46l, getMap(15l, 0l)); // Others
			cashlessTableMap.put(78l, getMap(15l, 0l)); // Sales and Others cess
			cashlessTableMap.put(153l,getMap(18l, 69l)); // Network Hospital discount
			cashlessTableMap.put(23l, getMap(18l, 69l)); // Discount in hospital Bill
			//R1006	cashlessTableMap.put(23l, getMap(0l, 0l)); // Discount in hospital Bill
			return cashlessTableMap;
		}

		public static Boolean isDirectToBilling(Preauth bean, List<ClaimAmountDetails> claimedAmountDetailsList ) {
			if((null == bean.getEnhancementType() || (bean.getEnhancementType() != null && bean.getEnhancementType().equalsIgnoreCase("I"))) && !isRoomRentAvailable(claimedAmountDetailsList)) {
				return true;
			}
			
			return false;
		}
		
		public static Boolean isDirectToFinancial(Preauth bean, List<ClaimAmountDetails> claimedAmountDetailsList ) {
			if((bean.getEnhancementType() != null && bean.getEnhancementType().equalsIgnoreCase("F")) || ((null == bean.getEnhancementType() || (bean.getEnhancementType() != null && bean.getEnhancementType().equalsIgnoreCase("I"))) && isRoomRentAvailable(claimedAmountDetailsList))) {
				return true;
			}
			
			return false;
		}
		
		
		public static Map<String, String> uploadDocumentManually(String filePath) {
			WeakHashMap fileUploadMap = SHAUtils.uploadFileToDMS(filePath);
			Map<String, String> fileDetailsMap = new WeakHashMap<String, String>();
			if(null != fileUploadMap && !fileUploadMap.isEmpty())
			{
				String docToken = (String)fileUploadMap.get("fileKey");
				String fileName = (String)fileUploadMap.get("fileName");
				fileDetailsMap.put(ReferenceTable.DOCUMENT_TOKEN, docToken);
				fileDetailsMap.put(ReferenceTable.FILE_NAME, fileName);
				System.out.println("--------Manually uploaded for bypass---- " + docToken);
			}
			
			return fileDetailsMap;
			
		}
		
		public static RODDocumentSummary getDummyDocSummary(Map<String, String> fileDetails) {
			RODDocumentSummary docSummary = new RODDocumentSummary();
			docSummary.setBillDate(new Timestamp(System.currentTimeMillis()));
			docSummary.setBillNumber("CS1");
			docSummary.setDeletedFlag("N");
			docSummary.setDocumentToken(fileDetails.get(ReferenceTable.DOCUMENT_TOKEN));
			docSummary.setFileName(fileDetails.get(ReferenceTable.FILE_NAME));
			MastersValue value = new MastersValue();
			value.setKey(ReferenceTable.CASHLESS_SETTLEMENT_BILL_KEY);
			docSummary.setFileType(value);
			
			docSummary.setNoOfItems(1l);
			
			return docSummary;
		}
		
		public static List<RODBillDetails> getDummyBillDetailsForByPass(List<ClaimAmountDetails> claimAmountDetailsList) {
			List<RODBillDetails> billDetailsList = new ArrayList<RODBillDetails>();
			Map<Long, Long> cashlessTableMappingKeys = getCashlessTableMapping();
			Map<Long, String> reimbItemNameMapping = getReimbItemNameMapping();
			Map<Long, Map<Long, Long>> irdalevelMapping = getIRDALEVELMapping();
			Long i = 1l;
		for (ClaimAmountDetails claimAmountDetails : claimAmountDetailsList) {
			if ((claimAmountDetails.getClaimedBillAmount() != null && claimAmountDetails
					.getClaimedBillAmount().intValue() > 0)
					|| (claimAmountDetails.getPaybleAmount() != null && claimAmountDetails
							.getPaybleAmount().intValue() > 0)
					|| (claimAmountDetails.getBenefitId() != null && claimAmountDetails
							.getBenefitId().equals(21L) && (claimAmountDetails.getClaimedBillAmount() != null && claimAmountDetails
									.getClaimedBillAmount().intValue() != 0))) {
				RODBillDetails billDetails = new RODBillDetails();
				MasBillCategory category = new MasBillCategory();
				category.setKey(cashlessTableMappingKeys.get(claimAmountDetails
						.getBenefitId()));
				//added for gmc prop deduction CR by noufel
				if(claimAmountDetails.getPreauth().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
						|| claimAmountDetails.getPreauth().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY)){
					if(claimAmountDetails.getBenefitId().equals(19l) && claimAmountDetails.getSequenceNumber() == null){
						category.setKey(151l);	
					}else if(claimAmountDetails.getBenefitId().equals(19l) && claimAmountDetails.getSequenceNumber() != null && 
							claimAmountDetails.getSequenceNumber().equals(2l)){
						category.setKey(152l);	
					}
				}
				billDetails.setBillCategory(category);

				MasBillClassification classification = new MasBillClassification();
				classification.setKey(ReferenceTable.HOSPITALIZATION);
				billDetails.setBillClassification(classification);

				billDetails.setItemName(reimbItemNameMapping
						.get(cashlessTableMappingKeys.get(claimAmountDetails
								.getBenefitId())));
				//added for gmc prop deduction CR by noufel
				if(claimAmountDetails.getPreauth().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
						|| claimAmountDetails.getPreauth().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY)){
					if(claimAmountDetails.getBenefitId().equals(19l) && claimAmountDetails.getSequenceNumber() == null){
						billDetails.setItemName("Others with Proportionate Deduction");	
					}else if(claimAmountDetails.getBenefitId().equals(19l) && claimAmountDetails.getSequenceNumber() != null && 
							claimAmountDetails.getSequenceNumber().equals(2l)){
						billDetails.setItemName("Others without Proportionate Deduction");
					}
				}
				billDetails.setDeletedFlag("N");
				billDetails.setItemNumber(i);
				billDetails.setNonPayableReason(claimAmountDetails
						.getNonPayableReason());
				if (claimAmountDetails.getTotalBillingDays() != null) {
					billDetails.setNoOfDaysBills(claimAmountDetails
							.getTotalBillingDays().doubleValue());
				}
				if (claimAmountDetails.getBillDayPayment() != null) {
					billDetails.setPerDayAmountBills(claimAmountDetails
							.getBillDayPayment().doubleValue());
				}

				if (claimAmountDetails.getTotalDaysForPolicy() != null) {
					billDetails.setNoOfDaysPolicy(claimAmountDetails
							.getTotalDaysForPolicy().doubleValue());
				}
				if (claimAmountDetails.getPolicyDayPayment() != null) {
					billDetails.setPerDayAmountPolicy(claimAmountDetails
							.getPolicyDayPayment().doubleValue());
				}
				// R1006
				if (claimAmountDetails.getBenefitId() != null
						&& claimAmountDetails.getBenefitId().equals(21L)) {
					billDetails
							.setClaimedAmountBills(claimAmountDetails
									.getClaimedBillAmount() != null ? ((claimAmountDetails
									.getClaimedBillAmount() < 0F) ? Math
									.abs(claimAmountDetails
											.getClaimedBillAmount()
											.doubleValue())
									: claimAmountDetails.getClaimedBillAmount()
											.doubleValue()) : 0d);
				} else {
					billDetails
							.setClaimedAmountBills(claimAmountDetails
									.getClaimedBillAmount() != null ? claimAmountDetails
									.getClaimedBillAmount().doubleValue() : 0d);
				}
				billDetails.setNonPayableAmount(claimAmountDetails
						.getDeductibleAmount() != null ? claimAmountDetails
						.getDeductibleAmount().doubleValue() : 0d);
				//code changed done by noufel for prop cr
				if(!(claimAmountDetails.getPreauth().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
						|| claimAmountDetails.getPreauth().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
				if (claimAmountDetails.getBenefitId() != null
						&& (claimAmountDetails.getBenefitId().equals(19l))) {
					Double amt = 0d;
					if (claimAmountDetails.getDeductibleAmount() != null) {
						amt += claimAmountDetails.getDeductibleAmount();
					}
					if (claimAmountDetails.getNonPayableAmount() != null) {
						amt += claimAmountDetails.getNonPayableAmount();
					}
					billDetails.setNonPayableAmount(amt);
				}
			}
				// R1006
				if (claimAmountDetails.getBenefitId() != null
						&& claimAmountDetails.getBenefitId().equals(21L)) {
					billDetails.setNetAmount(claimAmountDetails
							.getPaybleAmount() != null ? ((claimAmountDetails
							.getPaybleAmount() < 0F) ? Math
							.abs(claimAmountDetails.getPaybleAmount()
									.doubleValue()) : claimAmountDetails
							.getPaybleAmount().doubleValue()) : 0d);
				} else {
					billDetails.setNetAmount(claimAmountDetails
							.getPaybleAmount() != null ? claimAmountDetails
							.getPaybleAmount().doubleValue() : 0d);
				}
				Map<Long, Long> map = irdalevelMapping
						.get(cashlessTableMappingKeys.get(claimAmountDetails
								.getBenefitId()));
				if (map != null && !map.isEmpty()) {
					for (Map.Entry<Long, Long> entry : map.entrySet()) {
						billDetails.setIrdaLevel1Id(entry.getKey());
						if (!entry.getValue().equals(0l)) {
							billDetails.setIrdaLevel2Id(entry.getValue());
						}

					}
				}

				billDetailsList.add(billDetails);
			}

		}
			return billDetailsList;
		}
		
		public static PreauthDTO roomRentNursingMapping(PreauthDTO bean, Long roomRentCategoryId, Long nursingChargesCategoryId, Boolean isIcuMapping,Boolean isIccuMapping) {
			List<RoomRentMatchingDTO> dtoList = new ArrayList<RoomRentMatchingDTO>();
			List<NursingChargesMatchingDTO> nursingDTOList = new ArrayList<NursingChargesMatchingDTO>();
			List<UploadDocumentDTO> uploadDocumentDTOList = bean
					.getUploadDocumentDTO();
			if(roomRentCategoryId != null && roomRentCategoryId.equals(85l)){
				roomRentCategoryId = 86l;
			}
			if(nursingChargesCategoryId != null && nursingChargesCategoryId.equals(84l)){
				nursingChargesCategoryId = 85l;
			}
			List<SelectValue> listValues = new ArrayList<SelectValue>();
			Long i = 1l;
			if(isIcuMapping && ! isIccuMapping) {
				i = 100l;
			}else if(isIccuMapping){
				i = 200l;
			}
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocumentDTOList) {
				List<BillEntryDetailsDTO> billEntryDetailList = uploadDocumentDTO
						.getBillEntryDetailList();
				for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailList) {

					if (billEntryDetailsDTO.getCategory() != null
							&& billEntryDetailsDTO.getCategory().getId() != null
							&& billEntryDetailsDTO.getCategory().getId().equals(roomRentCategoryId)) {
						RoomRentMatchingDTO dto = new RoomRentMatchingDTO();
						dto.setIdentityId(i++);
						dto.setItemName(billEntryDetailsDTO.getCategory().getId().equals(10l) ? SHAConstants.ICU_ROOM_RENT_MAPPING : billEntryDetailsDTO.getCategory().getId().equals(86l) ? SHAConstants.ICCU_ROOM_RENT : SHAConstants.ROOM_RENT_MAPPING);
						dto.setId(billEntryDetailsDTO.getKey());
						dto.setBillNumber(billEntryDetailsDTO.getBillNo() != null ? billEntryDetailsDTO
								.getBillNo() : "");
						dto.setClaimedNoOfDays(billEntryDetailsDTO.getNoOfDays() != null ? billEntryDetailsDTO
								.getNoOfDays() : 0);
						dto.setAllowedNoOfDays(billEntryDetailsDTO
								.getNoOfDaysAllowed() != null ? billEntryDetailsDTO
								.getNoOfDaysAllowed() : 0);
						dto.setPerDayAmount(billEntryDetailsDTO.getPerDayAmt() != null ? billEntryDetailsDTO
								.getPerDayAmt() : 0);
						SelectValue value = new SelectValue();
						value.setId(dto.getId());
						value.setValue(dto.getIdentityId().toString());
						listValues.add(value);
						dtoList.add(dto);
					} else if (billEntryDetailsDTO.getCategory() != null
							&& billEntryDetailsDTO.getCategory().getValue() != null
							&& billEntryDetailsDTO.getCategory().getId().equals(nursingChargesCategoryId)) {
						NursingChargesMatchingDTO dto = new NursingChargesMatchingDTO();
						dto.setItemName(billEntryDetailsDTO.getCategory().getId().equals(11l) ? SHAConstants.ICU_ROOM_RENT_NURSING_MAPPING : billEntryDetailsDTO.getCategory().getId().equals(85l) ? SHAConstants.ICCU_NURSING_CHARGES : SHAConstants.ROOM_RENT_NURSING_MAPPING);
						dto.setKey(i++);
						dto.setId(billEntryDetailsDTO.getKey());
						dto.setBillNumber(billEntryDetailsDTO.getBillNo() != null ? billEntryDetailsDTO
								.getBillNo() : "");
						dto.setPerDayAmount(billEntryDetailsDTO.getPerDayAmt() != null ? billEntryDetailsDTO.getPerDayAmt() : 0);
						dto.setClaimedNoOfDays(billEntryDetailsDTO.getNoOfDays() != null ? billEntryDetailsDTO
								.getNoOfDays() : 0);
						dto.setAllocatedClaimedNoOfDays(0d);
						nursingDTOList.add(dto);
					}
				}
			}

			if (!dtoList.isEmpty() && !nursingDTOList.isEmpty()) {
				if (dtoList.size() == 1 && nursingDTOList.size() == 1) {
					nursingDTOList.get(0).setMapToRoomDays(
							dtoList.get(0).getAllowedNoOfDays());
					dtoList.get(0).setNursingChargesDTOList(nursingDTOList);
					if(!isIcuMapping && ! isIccuMapping) {
						bean.setRoomRentMappingDTOList(dtoList);
						bean.setIsOneMapping(true);
					} else if (isIcuMapping && ! isIccuMapping){
						bean.setIcuRoomRentMappingDTOList(dtoList);
						bean.setIsICUoneMapping(true);
					} else if (!isIcuMapping && isIccuMapping){
						bean.setIccuRoomRentMappingDTOList(dtoList);
						bean.setIsICCUoneMapping(true);
					}
				} else if (dtoList.size() > 1 || nursingDTOList.size() > 1) {
					Long j = 1l;
					for (RoomRentMatchingDTO roomRentDTO : dtoList) {
						
						List<NursingChargesMatchingDTO> nursingDTOLists = new ArrayList<NursingChargesMatchingDTO>();
						Long k = 1l;
						for (NursingChargesMatchingDTO nursingDTO : nursingDTOList) {
							nursingDTO.setListValues(listValues);
							nursingDTO.setMappingId(k);
							nursingDTOLists.add(getNursingDTO(roomRentDTO.getIdentityId() + j, nursingDTO, listValues));
							j++;
							k++;
						}
						
						roomRentDTO.setNursingChargesDTOList(nursingDTOLists);
					}
					if(!isIcuMapping && ! isIccuMapping) {
						bean.setRoomRentMappingDTOList(dtoList);
						bean.setIsOneMapping(true);
					} else if (isIcuMapping && ! isIccuMapping){
						bean.setIcuRoomRentMappingDTOList(dtoList);
						bean.setIsICUoneMapping(true);
					} else if (!isIcuMapping && isIccuMapping){
						bean.setIccuRoomRentMappingDTOList(dtoList);
						bean.setIsICCUoneMapping(true);
					}
					
				}
			} else if(!dtoList.isEmpty()){
				
				for (RoomRentMatchingDTO dto : dtoList) {
					
					dto.setNursingChargesDTOList(new ArrayList<NursingChargesMatchingDTO>());
				}
				if(!isIcuMapping && ! isIccuMapping) {
					bean.setRoomRentMappingDTOList(dtoList);
				} else if (isIcuMapping && ! isIccuMapping){
					bean.setIcuRoomRentMappingDTOList(dtoList);
				} else if (!isIcuMapping && isIccuMapping){
					bean.setIccuRoomRentMappingDTOList(dtoList);
				}
			} else if(dtoList.isEmpty() && !nursingDTOList.isEmpty()) {
				Long j = 1l;
				RoomRentMatchingDTO roomRentDTO = new RoomRentMatchingDTO();
				roomRentDTO.setIdentityId(i+1);
				List<NursingChargesMatchingDTO> nursingDTOLists = new ArrayList<NursingChargesMatchingDTO>();
				Long k = 1l;
				for (NursingChargesMatchingDTO nursingDTO : nursingDTOList) {
					nursingDTO.setListValues(listValues);
					nursingDTO.setMappingId(k);
					nursingDTOLists.add(getNursingDTO(roomRentDTO.getIdentityId() + j, nursingDTO, listValues));
					j++;
					k++;
				}
				roomRentDTO.setNursingChargesDTOList(nursingDTOLists);
				dtoList.add(roomRentDTO);
				if(!isIcuMapping && ! isIccuMapping) {
					bean.setRoomRentMappingDTOList(dtoList);
					bean.setIsOneMapping(true);
				} else if (isIcuMapping && ! isIccuMapping){
					bean.setIcuRoomRentMappingDTOList(dtoList);
					bean.setIsICUoneMapping(true);
				} else if (!isIcuMapping && isIccuMapping){
					bean.setIccuRoomRentMappingDTOList(dtoList);
					bean.setIsICCUoneMapping(true);
				}
			}
			return bean;

		}
		
		private static NursingChargesMatchingDTO getNursingDTO(Long key, NursingChargesMatchingDTO filledDto, List<SelectValue> listValues) {
			NursingChargesMatchingDTO dto = new NursingChargesMatchingDTO();
			dto.setItemName(filledDto.getItemName());
			dto.setKey(key);
			dto.setId(filledDto.getId());
			dto.setBillNumber(filledDto.getBillNumber());
			dto.setPerDayAmount(filledDto.getPerDayAmount());
			dto.setClaimedNoOfDays(filledDto.getClaimedNoOfDays());
			dto.setAllocatedClaimedNoOfDays(0d);
			dto.setMappingId(filledDto.getMappingId());
			dto.setListValues(listValues);
			return dto;
		}
		
		public static void fillMappingData(PreauthDTO bean, List<BillItemMapping> mappingData, Boolean isInvokeForOneToOne) {
			if((!bean.getIsOneMapping() && !isInvokeForOneToOne)) {
				SHAUtils.roomRentNursingMapping(bean, 8l, 9l, false,false);
				setMappingDays(mappingData,  bean.getRoomRentMappingDTOList());
			}
			
			if(bean.getIsOneMapping() && isInvokeForOneToOne) {
				SHAUtils.roomRentNursingMapping(bean, 8l, 9l, false,false);
				setMappingDays(mappingData,  bean.getRoomRentMappingDTOList());
			}
			
			if(!bean.getIsICUoneMapping() && !isInvokeForOneToOne) {
				SHAUtils.roomRentNursingMapping(bean, 10l, 11l, true,false);
				setMappingDays(mappingData, bean.getIcuRoomRentMappingDTOList());
			}
			
			if(bean.getIsICUoneMapping() && isInvokeForOneToOne) {
				SHAUtils.roomRentNursingMapping(bean, 10l, 11l, true,false);
				setMappingDays(mappingData, bean.getIcuRoomRentMappingDTOList());
			}
			
			if(!bean.getIsICCUoneMapping() && !isInvokeForOneToOne) {
				SHAUtils.roomRentNursingMapping(bean, 85l, 84l, false,true);
				setMappingDays(mappingData, bean.getIccuRoomRentMappingDTOList());
			}
			
			if(bean.getIsICCUoneMapping() && isInvokeForOneToOne) {
				SHAUtils.roomRentNursingMapping(bean, 85l, 84l, false,true);
				setMappingDays(mappingData, bean.getIccuRoomRentMappingDTOList());
			}
			
			
		}

		private static void setMappingDays(List<BillItemMapping> mappingData,
				List<RoomRentMatchingDTO> roomRentMappingDTOList) {
			for (RoomRentMatchingDTO roomRentMatchingDTO : roomRentMappingDTOList) {
				List<NursingChargesMatchingDTO> nursingChargesDTOList = roomRentMatchingDTO.getNursingChargesDTOList();
				for (NursingChargesMatchingDTO nursingChargesMatchingDTO : nursingChargesDTOList) {
					for (BillItemMapping mappedItem : mappingData) {
						if(roomRentMatchingDTO.getId() != null && nursingChargesMatchingDTO.getId() != null && mappedItem.getRoomIcuRentId() != null &&  mappedItem.getNursingId() != null && mappedItem.getRoomIcuRentId().equals(roomRentMatchingDTO.getId()) && mappedItem.getNursingId().equals(nursingChargesMatchingDTO.getId())) {
							nursingChargesMatchingDTO.setMapToRoomDays(mappedItem.getAllowedDays() != null ? mappedItem.getAllowedDays() : 0 );
						}
					}
				}
			}
		}
		
	/*	public static String getAciquireByUserId(Integer taskNumber){
			
			String user = null;
			
			HTAcquireBy acquireByUser = BPMClientContext.getAcquireByUser(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
			
			try {
				user = acquireByUser.getUser(BPMClientContext.BPMN_TASK_USER, taskNumber);
			} catch (Exception e) {
				
				e.printStackTrace();
			}

			return user;
		}*/
		
		public static void setCopayAmounts(PreauthDTO bean, AmountConsideredTable amountConsideredTable) {
			if(bean != null && amountConsideredTable != null) {
				bean.setAmountConsCopayPercentage(amountConsideredTable.getConsideredAmountCopayValue() != null ? amountConsideredTable.getConsideredAmountCopayValue().doubleValue() : 0l);
				bean.setBalanceSICopayPercentage(amountConsideredTable.getBalanceSICopayValue() != null ? amountConsideredTable.getBalanceSICopayValue().doubleValue() : 0l);
				bean.setAmountConsAftCopayAmt(amountConsideredTable.getConsideredAmountValue() != null ?amountConsideredTable.getConsideredAmountValue().doubleValue() : 0d );
				bean.setBalanceSIAftCopayAmt(amountConsideredTable.getBalanceSumInsuredAmt() != null ?amountConsideredTable.getBalanceSumInsuredAmt().doubleValue() : 0d );
			}
			
		}
		
		public static void setCopayAmounts(PreauthDTO bean, PAAmountConsideredTable amountConsideredTable) {
			if(bean != null && amountConsideredTable != null) {
				bean.setAmountConsCopayPercentage(amountConsideredTable.getConsideredAmountCopayValue() != null ? amountConsideredTable.getConsideredAmountCopayValue().doubleValue() : 0l);
				bean.setBalanceSICopayPercentage(amountConsideredTable.getBalanceSICopayValue() != null ? amountConsideredTable.getBalanceSICopayValue().doubleValue() : 0l);
				bean.setAmountConsAftCopayAmt(amountConsideredTable.getConsideredAmountValue() != null ?amountConsideredTable.getConsideredAmountValue().doubleValue() : 0d );
				bean.setBalanceSIAftCopayAmt(amountConsideredTable.getBalanceSumInsuredAmt() != null ?amountConsideredTable.getBalanceSumInsuredAmt().doubleValue() : 0d );
			}
			
		}
		
		public static Boolean isWithinRange(String claimedAmtFrom, String claimedAmtTo, String claimedAmt) {
			Double claimAmtFrom = SHAUtils.getDoubleValueFromString(claimedAmtFrom);
			Double claimAmtTo = SHAUtils.getDoubleValueFromString(claimedAmtTo);
			Double payloadClaimedAmt = SHAUtils.getDoubleValueFromString(claimedAmt);
			Boolean isAllowed = false;
			if(claimedAmt != null && claimedAmtFrom != null && claimedAmtTo != null && !claimedAmtFrom.isEmpty() && !claimedAmtTo.isEmpty() )  {
				if(!(claimAmtFrom > claimAmtTo || claimAmtTo < claimAmtFrom) && payloadClaimedAmt >= claimAmtFrom && payloadClaimedAmt <= claimAmtTo) {
					isAllowed = true;
				}
			} else if(claimedAmt != null && claimedAmtFrom != null && !claimedAmtFrom.isEmpty()) {
				if(payloadClaimedAmt >= claimAmtFrom) {
					isAllowed = true;
				}
				
			} else if(claimedAmt != null && claimedAmtTo != null && !claimedAmtTo.isEmpty()) {
				if(payloadClaimedAmt <= claimAmtTo) {
					isAllowed = true;
				}
			} else {
				isAllowed = true;
			}
			
			return isAllowed;
		}
		
		public static String convertBooleanToStringFlag(Boolean ischecked) {
			String value = "N";
			if(ischecked) {
				value = "Y";
			} 
			
			return value;
		}
		
		public static Boolean convertStringFlagToBoolean(String flag) {
			Boolean value = false;
			if(flag.equalsIgnoreCase("Y")) {
				value = true;
			} 
			
			return value;
		}
		
		public static BeanItemContainer<SelectValue> getSortOrder() {
			BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
			SelectValue selectValue = new SelectValue();
			selectValue.setId(1l);
			selectValue.setValue(SHAConstants.FIFO);
			
			SelectValue selectValue4 = new SelectValue();
			selectValue4.setId(2l);
			selectValue4.setValue(SHAConstants.LIFO);
			
			container.addBean(selectValue);
			container.addBean(selectValue4);	
			return container;
		}
		
		public static BeanItemContainer<String> getStringSortOrder() {
			BeanItemContainer<String> container = new BeanItemContainer<String>(String.class);
			String value = "FIFO";
			String value1= "LIFO";
			
			container.addBean(value);
			container.addBean(value1);	
			return container;
		}
		
		public static Double getHospOrPartialAppAmt(PreauthDTO dto, ReimbursementService reimbrusementService, Double balanceSI) {
			if(dto.getClaimDTO() != null && dto.getClaimDTO().getClaimType() != null && dto.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				if(dto.getHospitalizaionFlag() || dto.getPartialHospitalizaionFlag()) {
					Reimbursement hospitalizationOrPartialRODForBalanceSI = reimbrusementService.getHospitalizationOrPartialRODForBalanceSI(dto.getClaimDTO().getKey(), dto.getRodNumber());
					if(hospitalizationOrPartialRODForBalanceSI != null) {
						//IMSSUPPOR-36364 - Commented as the buffer logic changed - buffer utlized is updated instead of buffer allocated in reimbursement
						/*if(dto.getNewIntimationDTO() != null && dto.getNewIntimationDTO().getPolicy() != null && ReferenceTable.getGMCProductList().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey()) && hospitalizationOrPartialRODForBalanceSI.getCorporateUtilizedAmt() != null){
							balanceSI = balanceSI + (hospitalizationOrPartialRODForBalanceSI.getFinancialApprovedAmount() != null && (hospitalizationOrPartialRODForBalanceSI.getFinancialApprovedAmount() > hospitalizationOrPartialRODForBalanceSI.getCorporateUtilizedAmt()) ? (hospitalizationOrPartialRODForBalanceSI.getFinancialApprovedAmount() - hospitalizationOrPartialRODForBalanceSI.getCorporateUtilizedAmt()): 0d);	
						}else{*/
						balanceSI = balanceSI + (hospitalizationOrPartialRODForBalanceSI.getFinancialApprovedAmount() != null ? hospitalizationOrPartialRODForBalanceSI.getFinancialApprovedAmount(): 0d);
						//}
						//IMSSUPPOR-27009 - Adding FAappAmt for same rod versions
						if(hospitalizationOrPartialRODForBalanceSI.getVersion() != null && !hospitalizationOrPartialRODForBalanceSI.getVersion().equals(1)){
							List<Reimbursement> rodVersions = 	reimbrusementService.getReimbursementList(hospitalizationOrPartialRODForBalanceSI.getRodNumber());
							if(rodVersions != null && !rodVersions.isEmpty()){
								for (Reimbursement reimbursement : rodVersions) {
									if(!reimbursement.getKey().equals(hospitalizationOrPartialRODForBalanceSI.getKey()) && reimbursement.getStatus() != null && reimbursement.getStatus().getKey().equals(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS) && !ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) && !ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey()) && (reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().toLowerCase().equalsIgnoreCase("y")
											|| reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().toLowerCase().equalsIgnoreCase("y")) && !(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS) || reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS))){
										//IMSSUPPOR-36364 - Commented as the buffer logic changed - buffer utlized is updated instead of buffer allocated in reimbursement
										/*if(dto.getNewIntimationDTO() != null && dto.getNewIntimationDTO().getPolicy() != null && ReferenceTable.getGMCProductList().containsKey(dto.getNewIntimationDTO().getPolicy().getProduct().getKey()) && reimbursement.getCorporateUtilizedAmt() != null){
											balanceSI = balanceSI + ((reimbursement.getFinancialApprovedAmount() != null && (reimbursement.getFinancialApprovedAmount() > reimbursement.getCorporateUtilizedAmt())) ? (reimbursement.getFinancialApprovedAmount() - reimbursement.getCorporateUtilizedAmt()): 0d);
										}
										else{*/
											balanceSI = balanceSI + (reimbursement.getFinancialApprovedAmount() != null ? reimbursement.getFinancialApprovedAmount(): 0d);
										//}
									}
								}
							}
						}
					}
				}
			}
			return balanceSI;
		}
		
		public static Boolean isPEDAvailable(List<InsuredPedDetails> pedList, String pedCode) {
			if(pedList != null) {
				for (InsuredPedDetails insuredPedDetails : pedList) {
					if(pedCode != null && insuredPedDetails.getPedCode() != null && insuredPedDetails.getPedCode().equalsIgnoreCase(pedCode)) {
						return true;
					}
					break;
				}
			}
		
			return false;
		}
		
		public static InsuredPedDetails getAvailableInsuredPed(List<InsuredPedDetails> pedList, String pedCode) {
			if(pedList != null) {
				for (InsuredPedDetails insuredPedDetails : pedList) {
					if(pedCode != null && insuredPedDetails.getPedCode() != null && insuredPedDetails.getPedCode().equalsIgnoreCase(pedCode)) {
						return insuredPedDetails;
					}
					break;
				}
			}
			return null;
		}
		
		
		public static String getTimeBetweenTwoDays(Date fromDate, Date ToDate){
			
			long diff = fromDate.getTime() - fromDate.getTime();
			
			//long diffSeconds = diff / 1000 % 60;
			
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
			
			String time = String.valueOf(diffDays) + "Days" + String.valueOf(diffHours) + "Hours" + String.valueOf(diffMinutes) + " Minutes";
			
			return time;
		}
		
		
		public static Integer isAmbulanceAvailable(List<NoOfDaysCell> claimedDetails){
			
			for (NoOfDaysCell noOfDaysCell : claimedDetails) {
				if(noOfDaysCell.getBenefitId() != null && noOfDaysCell.getBenefitId().equals(15l)){
					if(noOfDaysCell.getPaybleAmount() != null && noOfDaysCell.getPaybleAmount() >0){
						return noOfDaysCell.getPaybleAmount();
					}
				}
			}
			
			return null;
		}
		public static void setOtherInsurerSettlementHospValues(PreauthDTO bean, String hospitalAmt, String amountAlreadyPaid) {
			OtherInsHospSettlementDetailsDTO otherInsHospSettlementCalcDTO = bean.getOtherInsHospSettlementCalcDTO();
			Integer netAmt = otherInsHospSettlementCalcDTO.getTotalClaimedAmt() - otherInsHospSettlementCalcDTO.getNonMedicalAmt();
			otherInsHospSettlementCalcDTO.setNetClaimedAmt(netAmt > 0 ? netAmt : 0);
			Integer balanceAmt = otherInsHospSettlementCalcDTO.getNetClaimedAmt() - otherInsHospSettlementCalcDTO.getTpaSettledAmt();
			otherInsHospSettlementCalcDTO.setBalanceAmt(balanceAmt > 0 ? balanceAmt : 0);
			otherInsHospSettlementCalcDTO.setHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(hospitalAmt));
			Integer minAmt = Math.min(SHAUtils.getIntegerFromStringWithComma(hospitalAmt), balanceAmt > 0 ?  balanceAmt : SHAUtils.getIntegerFromStringWithComma(hospitalAmt));
			otherInsHospSettlementCalcDTO.setPayableToIns(minAmt > 0 ? minAmt : 0);
			otherInsHospSettlementCalcDTO.setAmountAlreadyPaid(SHAUtils.getIntegerFromStringWithComma(amountAlreadyPaid));
			Integer payableAmt = otherInsHospSettlementCalcDTO.getPayableToIns() - otherInsHospSettlementCalcDTO.getAmountAlreadyPaid();
			otherInsHospSettlementCalcDTO.setPayableAmt(payableAmt > 0 ? payableAmt : 0);
		}
		
		public static void setOtherInsurerSettlementPreHospValues(PreauthDTO bean, String prehHospAmt, String amountAlreadyPaid) {
			OtherInsPreHospSettlementDetailsDTO otherInsPreHospSettlementCalcDTO = bean.getOtherInsPreHospSettlementCalcDTO();
			Integer netAmt = otherInsPreHospSettlementCalcDTO.getTotalClaimedAmt() - otherInsPreHospSettlementCalcDTO.getNonMedicalAmt();
			otherInsPreHospSettlementCalcDTO.setNetClaimedAmt(netAmt > 0 ? netAmt : 0);
			Integer balanceAmt = otherInsPreHospSettlementCalcDTO.getNetClaimedAmt() - otherInsPreHospSettlementCalcDTO.getTpaSettledAmt();
			otherInsPreHospSettlementCalcDTO.setBalanceAmt(balanceAmt > 0 ? balanceAmt : 0);
			otherInsPreHospSettlementCalcDTO.setHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(prehHospAmt));
			Integer minAmt = Math.min(SHAUtils.getIntegerFromStringWithComma(prehHospAmt), balanceAmt > 0 ?  balanceAmt : SHAUtils.getIntegerFromStringWithComma(prehHospAmt));
			otherInsPreHospSettlementCalcDTO.setPayableToIns(minAmt > 0 ? minAmt : 0);
			otherInsPreHospSettlementCalcDTO.setAmountAlreadyPaid(SHAUtils.getIntegerFromStringWithComma(amountAlreadyPaid));
			Integer payableAmt = otherInsPreHospSettlementCalcDTO.getPayableToIns() - otherInsPreHospSettlementCalcDTO.getAmountAlreadyPaid();
			otherInsPreHospSettlementCalcDTO.setPayableAmt(payableAmt > 0 ? payableAmt : 0);
		}

		public static void setOtherInsurerSettlementPostHospValues(PreauthDTO bean, String postHospAmt, String amountAlreadyPaid) {
			OtherInsPostHospSettlementDetailsDTO otherInsPostHospSettlementCalcDTO = bean.getOtherInsPostHospSettlementCalcDTO();
			Integer netAmt = otherInsPostHospSettlementCalcDTO.getTotalClaimedAmt() - otherInsPostHospSettlementCalcDTO.getNonMedicalAmt();
			otherInsPostHospSettlementCalcDTO.setNetClaimedAmt(netAmt > 0 ? netAmt : 0);
			Integer balanceAmt = otherInsPostHospSettlementCalcDTO.getNetClaimedAmt() - otherInsPostHospSettlementCalcDTO.getTpaSettledAmt();
			balanceAmt = balanceAmt > 0 ? balanceAmt : 0;
			otherInsPostHospSettlementCalcDTO.setBalanceAmt(balanceAmt > 0 ? balanceAmt : 0);
			otherInsPostHospSettlementCalcDTO.setHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(postHospAmt));
			Integer minAmt = Math.min(SHAUtils.getIntegerFromStringWithComma(postHospAmt), balanceAmt > 0 ?  balanceAmt : SHAUtils.getIntegerFromStringWithComma(postHospAmt));
			otherInsPostHospSettlementCalcDTO.setPayableToIns(minAmt > 0 ? minAmt : 0);
			otherInsPostHospSettlementCalcDTO.setAmountAlreadyPaid(SHAUtils.getIntegerFromStringWithComma(amountAlreadyPaid));
			Integer payableAmt = otherInsPostHospSettlementCalcDTO.getPayableToIns() - otherInsPostHospSettlementCalcDTO.getAmountAlreadyPaid();
			otherInsPostHospSettlementCalcDTO.setPayableAmt(payableAmt > 0 ? payableAmt : 0);
		}
		
		public static void resetOtherInsurerValues(PreauthDTO bean) {
			OtherInsHospSettlementDetailsDTO otherInsHospSettlementCalcDTO = bean.getOtherInsHospSettlementCalcDTO();
			otherInsHospSettlementCalcDTO.setBalanceAmt(0);
			otherInsHospSettlementCalcDTO.setNetClaimedAmt(0);
			otherInsHospSettlementCalcDTO.setNonMedicalAmt(0);
			otherInsHospSettlementCalcDTO.setPayableAmt(0);
			otherInsHospSettlementCalcDTO.setPayableToIns(0);
			otherInsHospSettlementCalcDTO.setTotalClaimedAmt(0);
			otherInsHospSettlementCalcDTO.setTpaSettledAmt(0);
			
			OtherInsPostHospSettlementDetailsDTO otherInsPostSettlementCalcDTO = bean.getOtherInsPostHospSettlementCalcDTO();
			otherInsPostSettlementCalcDTO.setBalanceAmt(0);
			otherInsPostSettlementCalcDTO.setNetClaimedAmt(0);
			otherInsPostSettlementCalcDTO.setNonMedicalAmt(0);
			otherInsPostSettlementCalcDTO.setPayableAmt(0);
			otherInsHospSettlementCalcDTO.setPayableToIns(0);
			otherInsPostSettlementCalcDTO.setTotalClaimedAmt(0);
			otherInsPostSettlementCalcDTO.setTpaSettledAmt(0);
			
			OtherInsPreHospSettlementDetailsDTO otherInsPreSettlementCalcDTO = bean.getOtherInsPreHospSettlementCalcDTO();
			otherInsPreSettlementCalcDTO.setBalanceAmt(0);
			otherInsPreSettlementCalcDTO.setNetClaimedAmt(0);
			otherInsPreSettlementCalcDTO.setNonMedicalAmt(0);
			otherInsPreSettlementCalcDTO.setPayableAmt(0);
			otherInsPreSettlementCalcDTO.setPayableToIns(0);
			otherInsPreSettlementCalcDTO.setTotalClaimedAmt(0);
			otherInsPreSettlementCalcDTO.setTpaSettledAmt(0);
		}
		
		public static void setConsolidatedAmtDTO(PreauthDTO bean, Boolean isFinancial) {
			ConsolidatedAmountDetailsDTO consolidatedAmtDTO = bean.getConsolidatedAmtDTO();
			if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
				if(bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag() || bean.getIsHospitalizationRepeat()) {
					consolidatedAmtDTO.setHospPayableAmt(bean.getOtherInsHospSettlementCalcDTO().getPayableAmt());
				}
				if(bean.getPreHospitalizaionFlag()) {
					consolidatedAmtDTO.setPreHospPayableAmt(bean.getOtherInsPreHospSettlementCalcDTO().getPayableAmt());
				}
				if(bean.getPostHospitalizaionFlag()) {
					consolidatedAmtDTO.setPostHospPayableAmt(bean.getOtherInsPostHospSettlementCalcDTO().getPayableAmt());
				}
				
				Integer addonBenefitAmt = 0;
				if(bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList() != null && !bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList().isEmpty()) {
					for (AddOnBenefitsDTO addOnBenefitsDTO : bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList()) {
						addonBenefitAmt += addOnBenefitsDTO.getPayableAmount() != null ? addOnBenefitsDTO.getPayableAmount() : 0;
					}
					
				}
				consolidatedAmtDTO.setAddonBenefitAmt(addonBenefitAmt);
				Integer totalAmt = consolidatedAmtDTO.getHospPayableAmt() + consolidatedAmtDTO.getPreHospPayableAmt() + consolidatedAmtDTO.getPostHospPayableAmt() + addonBenefitAmt;
				consolidatedAmtDTO.setAmountPayableToInsAftPremium(totalAmt > 0 ? totalAmt : 0);
				if((ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) ) {
					if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
						Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
						consolidatedAmtDTO.setPremiumAmt(uniqueInstallmentAmount);
						Integer deductedAmt = totalAmt - uniqueInstallmentAmount;
						consolidatedAmtDTO.setAmountPayableAfterPremium(deductedAmt > 0 ? deductedAmt : 0);
					}
				}else if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
					if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
						Integer uniqueInstallmentAmount = PremiaService.getInstance().getPolicyInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
						consolidatedAmtDTO.setPremiumAmt(uniqueInstallmentAmount);
						Integer deductedAmt = totalAmt - uniqueInstallmentAmount;
						consolidatedAmtDTO.setAmountPayableAfterPremium(deductedAmt > 0 ? deductedAmt : 0);
					}
				}
				
			} else {
				Boolean isReceivedFromIns = true;
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL) ) {
					isReceivedFromIns = false;
				}
				if(bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag() || bean.getIsHospitalizationRepeat()) {
					consolidatedAmtDTO.setHospPayableAmt(isReceivedFromIns ? bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() : bean.getHospitalizationCalculationDTO().getPayableToHospitalAftTDSAmt());
					if(isFinancial && bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
						Integer balanceToBePaid = bean.getHospitalizationCalculationDTO().getBalanceToBePaid() != null ? bean.getHospitalizationCalculationDTO().getBalanceToBePaid() > 0 ? bean.getHospitalizationCalculationDTO().getBalanceToBePaid() : 0:0;
						bean.getHospitalizationCalculationDTO().setBalanceToBePaid(balanceToBePaid);
						consolidatedAmtDTO.setHospPayableAmt(balanceToBePaid);
					}
				}
				if(bean.getPreHospitalizaionFlag()) {
					consolidatedAmtDTO.setPreHospPayableAmt(bean.getPreHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt());
					if(isFinancial && bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
						consolidatedAmtDTO.setPreHospPayableAmt(bean.getPreHospitalizationCalculationDTO().getBalanceToBePaid());
					}
				}
				if(bean.getPostHospitalizaionFlag()) {
					consolidatedAmtDTO.setPostHospPayableAmt(bean.getPostHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt());
					if(isFinancial && bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
						consolidatedAmtDTO.setPostHospPayableAmt(bean.getPostHospitalizationCalculationDTO().getBalanceToBePaid());
					}
				}
				
				Integer addonBenefitAmt = 0;
				if(bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList() != null && !bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList().isEmpty()) {
					for (AddOnBenefitsDTO addOnBenefitsDTO : bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList()) {
						addonBenefitAmt += addOnBenefitsDTO.getPayableAmount() != null ? addOnBenefitsDTO.getPayableAmount() : 0;
					}
					
				}
				consolidatedAmtDTO.setAddonBenefitAmt(addonBenefitAmt);
				Integer totalAmt = consolidatedAmtDTO.getHospPayableAmt() + consolidatedAmtDTO.getPreHospPayableAmt() + consolidatedAmtDTO.getPostHospPayableAmt() + addonBenefitAmt;
				consolidatedAmtDTO.setAmountPayableToInsAftPremium(totalAmt > 0 ? totalAmt : 0);
				if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
					if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
						Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
						consolidatedAmtDTO.setPremiumAmt(uniqueInstallmentAmount);
						Integer deductedAmt = totalAmt - uniqueInstallmentAmount;
						consolidatedAmtDTO.setAmountPayableAfterPremium(deductedAmt > 0 ? deductedAmt : 0);
					}
				}
			}
		}
		
		public static ReimbursementCalCulationDetails getCalcObjByClassification(List<ReimbursementCalCulationDetails> list, Long calssificationKey) {
			ReimbursementCalCulationDetails reimbursementCalCulationDetailsObj = null;
			if(list != null && !list.isEmpty()){
				for (ReimbursementCalCulationDetails reimbursementCalCulationDetails : list) {
					if(reimbursementCalCulationDetails.getBillClassificationId().equals(calssificationKey)) {
						reimbursementCalCulationDetailsObj = reimbursementCalCulationDetails;
						break;
					}
				}
			}
			return reimbursementCalCulationDetailsObj;
		}
		
		public static Integer findMinimumValuesForCommon(Integer netApprovedAmount, Integer sublimitAvailable, Integer selectedSIAmount) {
			
			Integer minimumAmt = netApprovedAmount;
			
		    if(sublimitAvailable >= 0){
		    	minimumAmt = Math.min(minimumAmt, sublimitAvailable);
		    }
		    if(selectedSIAmount >= 0){
		    	minimumAmt = Math.min(minimumAmt, selectedSIAmount);
		    }
		    
		    return minimumAmt;

		}
		
		public static Boolean isSublimitApplicable(DiagnosisProcedureTableDTO dto){
			
			if(dto.getDiagnosisDetailsDTO() != null && dto.getDiagnosisDetailsDTO().getSublimitApplicableFlag() != null 
					&& dto.getDiagnosisDetailsDTO().getSublimitApplicableFlag().equalsIgnoreCase("Y")){
				return true;
			}
			
			if(dto.getProcedureDTO() != null && dto.getProcedureDTO().getSublimitApplicableFlag() != null 
					&& dto.getProcedureDTO().getSublimitApplicableFlag().equalsIgnoreCase("Y")){
				return true;
			}
			
			return false;
			
		}
		
		public static Boolean isSIavailable(DiagnosisProcedureTableDTO dto){
			
			if(dto.getDiagnosisDetailsDTO() != null && dto.getDiagnosisDetailsDTO().getSumInsuredRestriction() != null){
				return true;
			}
			return false;
			
		}

		public static Double getClaimedAmount(Map<String, String> payableAmount) {
			Double hospAmt = SHAUtils.getDoubleFromStringWithComma(payableAmount.containsKey(SHAConstants.HOSP_CLAIMED) ? payableAmount.get(SHAConstants.HOSP_CLAIMED) : "0");
			Double preHospAmt = SHAUtils.getDoubleFromStringWithComma(payableAmount.containsKey(SHAConstants.PREHOSP_CLAIMED) ? payableAmount.get(SHAConstants.PREHOSP_CLAIMED) : "0");
			Double postHospAmt = SHAUtils.getDoubleFromStringWithComma(payableAmount.containsKey(SHAConstants.POSTHOSP_CLAIMED) ? payableAmount.get(SHAConstants.POSTHOSP_CLAIMED) : "0");
			Double sumAmount = hospAmt + preHospAmt + postHospAmt;
			
			return sumAmount;
}
		/*public static void setDefaultCopayValue(PreauthDTO bean) {
			Map<Long, String> coPayForSeniorCitizen = ReferenceTable.DEFAULT_CO_PAY_VALUES;
			if (coPayForSeniorCitizen.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				String string = coPayForSeniorCitizen.get(bean.getNewIntimationDTO().getPolicy().getProduct().getKey());			
				
				if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) &&
						(null !=bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() &&
						ReferenceTable.LUMPSUM_SECTION_CODE.equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue())))
				{
					bean.setIsDefaultCopay(false);
					bean.setDefaultCopayStr(null);
				}
				else
				{
					bean.setIsDefaultCopay(true);
					bean.setDefaultCopayStr(string);
				}
			}

		}*/
		
		/**
		 * GALAXYMAIN-13279
		 * 	
		 */
		public static Boolean isFAApprovedAndNotLotCreated(Reimbursement reimbursement, ClaimPayment claimPayment ) {
		Policy policyObj = null;
		Builder builder = null;
		PremBonusDetails policyDetails = null;

		if (reimbursement.getClaim().getIntimation().getPolicyNumber() != null) {
			DBCalculationService dbService = new DBCalculationService();
			policyObj = dbService.getPolicyObject(reimbursement.getClaim().getIntimation().getPolicyNumber());
			if (policyObj != null) {
				if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
					if (reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)
							&& (null != claimPayment && (claimPayment.getStatusId().getKey().equals(ReferenceTable.PAYMENT_LVL1_TYPE_CREATE_BATCH)))) {
					
						return true;
					}
				}	
				else {
					if (reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
						
						if (reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) && (claimPayment == null || (claimPayment != null && claimPayment.getLotNumber() == null))) {
							return true;
						}
					}	
				}
			}
		}	
		return false;
		}
		public static Double getPostHospClaimedAmount(PreauthDTO bean) {
			Double claimedAmt = 0d;
			List<UploadDocumentDTO> uploadDocumentDTO = bean.getUploadDocumentDTO();
			for (UploadDocumentDTO uploadDocumentDTO2 : uploadDocumentDTO) {
				List<BillEntryDetailsDTO> billEntryDetailList = uploadDocumentDTO2.getBillEntryDetailList();
				for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailList) {
					if(billEntryDetailsDTO.getClassification() != null && billEntryDetailsDTO.getClassification().getId() != null && billEntryDetailsDTO.getClassification().getId().equals(ReferenceTable.POST_HOSPITALIZATION)) {
						claimedAmt += billEntryDetailsDTO.getItemValue() != null ? billEntryDetailsDTO.getItemValue() : 0d;
					}
				}
			}
			return claimedAmt;
		}
		public static Boolean isHospitalDiscount(Hospitals hospitals){
			
			if(hospitals != null && hospitals.getHospitalDiscountFlag() != null && hospitals.getHospitalDiscountFlag().equalsIgnoreCase("Y")){
				return true;
			}

			return false;
		}

		
		public static Boolean isFAApprovedAndLotCreatedForOtherRODs(Reimbursement reimbursement, ClaimPayment claimPayment ) {
			if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)
					&& (claimPayment == null || (claimPayment != null && claimPayment.getLotNumber() != null))) {  	 
				return true;
			}
					
//			if(reimbursement.getDocAcknowLedgement().getReconsiderationRequest() != null && reimbursement.getDocAcknowLedgement().getReconsiderationRequest().equalsIgnoreCase("Y")) {
//				return false;
//			}
//			return true;

		return false;
		}


		public static Boolean isCopayAvailable(List <DiagnosisProcedureTableDTO> dto){
			Boolean isCopayAvail = false;
			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : dto) {
				if (diagnosisProcedureTableDTO.getCoPayPercentage() != null && diagnosisProcedureTableDTO.getCoPayPercentage().getValue() != null 
						&& getDoubleValueFromString(diagnosisProcedureTableDTO.getCoPayPercentage().getValue()) > 0 ) {
					isCopayAvail= true;
					break;
				}
			}
			return isCopayAvail;
			
		}
		
		public static Double isCopayAvailableForPEDValidation(List <PedDetailsTableDTO> dto){
			Double copay = 0d;
			List<Double> copayList = new ArrayList<Double>();
			for (PedDetailsTableDTO diagnosisProcedureTableDTO : dto) {
				if (diagnosisProcedureTableDTO.getCopay() != null && diagnosisProcedureTableDTO.getCopay().getValue() != null 
						&& getDoubleValueFromString(diagnosisProcedureTableDTO.getCopay().getValue()) > 0 ) {
					copayList.add(getDoubleFromStringWithComma(diagnosisProcedureTableDTO.getCopay().getValue()));
					
				}
			}
			if(! copayList.isEmpty()){
			  copay = Collections.max(copayList);
			}
			return copay;
			
		}
		
		public static Double isCopayAvailableForProcedureValidation(List <ProcedureDTO> dto){
			Double copay = 0d;
			List<Double> copayList = new ArrayList<Double>();
			for (ProcedureDTO diagnosisProcedureTableDTO : dto) {
				if (diagnosisProcedureTableDTO.getCopay() != null && diagnosisProcedureTableDTO.getCopay().getValue() != null 
						&& getDoubleValueFromString(diagnosisProcedureTableDTO.getCopay().getValue()) > 0 ) {
					copayList.add(getDoubleFromStringWithComma(diagnosisProcedureTableDTO.getCopay().getValue()));
					
				}
			}
			if(! copayList.isEmpty()){
			  copay = Collections.max(copayList);
			}
			return copay;
			
		}
		
		public static String getPrevioustAmountForDiagnosisProcedure(List<DiagnosisDetailsTableDTO> diagnonsisDTO, List<ProcedureDTO> procedureDTO, ResidualAmountDTO residualDTO ) {
			Double totalAmt = 0d;
			for (DiagnosisDetailsTableDTO diagDTO : diagnonsisDTO) {
				totalAmt += diagDTO.getNetApprovedAmount() != null ? diagDTO.getNetApprovedAmount() : 0d;
			}
			for (ProcedureDTO procedureDTO2 : procedureDTO) {
				totalAmt += procedureDTO2.getNetApprovedAmount() != null ? procedureDTO2.getNetApprovedAmount() : 0d;
			}
			totalAmt += residualDTO.getNetApprovedAmount() != null ? residualDTO.getNetApprovedAmount() : 0d;
			return String.valueOf(totalAmt.intValue());
		}
		

/*public static void main (String args[])
{
	SimpleDateFormat parseFormat = 
		    new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		Date date;
		try {
			date = parseFormat.parse("Sun Apr 24 00:00:00 IST 2016");
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			
			String result = format.format(date);
			Date date1 = getDateFormatDate(result);
			System.out.println("----"+date1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
}*/

	public static Date getDateFormatDate(String date){
		
		String format = null;
		Date convertedCurrentDate= null;
		if(date != null){
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			 convertedCurrentDate = sdf.parse(date);
			format = new SimpleDateFormat("dd/MM/yyyy").format(convertedCurrentDate);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertedCurrentDate;
		}
		else
		{
			return null;
		}
	}
	
    public static String changeDatetoString(Date currentDate)
    {
        String fdob = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try
        {
            fdob = sdf.format(currentDate).replace(" ", "T");
            fdob += fdob+"Z";
        }
        catch (Exception e)
        {
        }
        return fdob;
    }
	
	
	public static Boolean getBooleanValueForFlag(String flag){
		Boolean isSelected = false;
		if(flag != null && flag.equalsIgnoreCase(SHAConstants.YES_FLAG)) {
			isSelected = true;
		}
		return isSelected;
	}
	    
	    public static String getDurationFromTwoDate(Date startDate, Date endDate){
	    	try{

	    	Long duration  = endDate.getTime() - startDate.getTime();

	    	Long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
	    	Long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
	    	Long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
	    	
	    	return diffInHours.toString()+"Hours " + diffInMinutes.toString()+"Minutes " + diffInSeconds.toString()+"Seconds ";
	    	}catch(Exception e){
	    		
	    	}
	    	return "";
	    }
	    
	    public static Integer differenceInMonths(Date startDate, Date endDate) {
	        Calendar c1 = Calendar.getInstance();
	        c1.setTime(startDate);
	        Calendar c2 = Calendar.getInstance();
	        c2.setTime(endDate);
	        int diff = 0;
	        if (c2.after(c1)) {
	            while (c2.after(c1)) {
	                c1.add(Calendar.MONTH, 1);
	                if (c2.after(c1)) {
	                    diff++;
	                }
	            }
	        } else if (c2.before(c1)) {
	            while (c2.before(c1)) {
	                c1.add(Calendar.MONTH, -1);
	                if (c1.before(c2)) {
	                    diff--;
	                }
	            }
	        }
	        return diff;
	    }

	    public static void doFinalEnhancementCalculationForLetter(List<DiagnosisProcedureTableDTO> medicalDecisionTableList, Double highestCopay, PreauthDTO bean) {
	    	Double exceedSublimit = 0d;
	    	Double exceedSumInsrued = 0d;
	    	Double exceedPackage = 0d;
	    	Double netAmount = 0d;
	    	Double approvedAmount = 0d;
	    	Double sublimitAvailableAmt = 0d;
	    	//Boolean isPackageApplied = false;
	    	
	    	for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : medicalDecisionTableList) {
				if(diagnosisProcedureTableDTO.getAmountConsidered() != null && diagnosisProcedureTableDTO.getAmountConsidered() > 0) {
					if(diagnosisProcedureTableDTO.getPackageAmt() != null && !diagnosisProcedureTableDTO.getPackageAmt().equalsIgnoreCase("NA") && SHAUtils.isValidInteger(diagnosisProcedureTableDTO.getPackageAmt())) {
						//isPackageApplied = true;
						
//						exceedPackage += ((diagnosisProcedureTableDTO.getNetAmount() != null ? diagnosisProcedureTableDTO.getNetAmount().doubleValue() : 0d) - (diagnosisProcedureTableDTO.getReverseAllocatedAmt() != null ? diagnosisProcedureTableDTO.getReverseAllocatedAmt().doubleValue() : 0d));
						
						Double amt = (diagnosisProcedureTableDTO.getAmountConsidered().doubleValue()) - SHAUtils.getDoubleFromStringWithComma(diagnosisProcedureTableDTO.getPackageAmt());
						exceedPackage += (amt < 0 ? 0 : amt);
					}
					
					if(diagnosisProcedureTableDTO.getSubLimitAmount() != null && !diagnosisProcedureTableDTO.getSubLimitAmount().equalsIgnoreCase("NA") && SHAUtils.isValidInteger(diagnosisProcedureTableDTO.getSubLimitAmount())
							&& bean.getBalanceSI() > diagnosisProcedureTableDTO.getNetAmount().doubleValue() ) {
						if(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
							Double amt = (diagnosisProcedureTableDTO.getAmountConsidered().doubleValue()) - (diagnosisProcedureTableDTO.getSubLimitAvaliableAmt() != null ? diagnosisProcedureTableDTO.getSubLimitAvaliableAmt().doubleValue() : 0d);
							exceedSublimit += amt < 0 ? 0 : amt;
						} else {
							exceedSublimit += Double.valueOf(diagnosisProcedureTableDTO.getSubLimitAvaliableAmt()) < diagnosisProcedureTableDTO.getNetAmount().doubleValue() ? diagnosisProcedureTableDTO.getNetAmount().doubleValue() - Double.valueOf(diagnosisProcedureTableDTO.getSubLimitAvaliableAmt()) : 0d; 
//							exceedSublimit += ((diagnosisProcedureTableDTO.getNetAmount() != null ? diagnosisProcedureTableDTO.getNetAmount().doubleValue() : 0d) - (diagnosisProcedureTableDTO.getReverseAllocatedAmt() != null ? diagnosisProcedureTableDTO.getReverseAllocatedAmt().doubleValue() : 0d));
						}
						
					}
					
					if(diagnosisProcedureTableDTO.getRestrictionSI() != null && !diagnosisProcedureTableDTO.getRestrictionSI().equalsIgnoreCase("NA") && SHAUtils.isValidInteger(diagnosisProcedureTableDTO.getRestrictionSI()) && diagnosisProcedureTableDTO.getNetAmount().doubleValue() > Double.valueOf(diagnosisProcedureTableDTO.getRestrictionSI())) {
				
						if(diagnosisProcedureTableDTO.getSubLimitAvaliableAmt() == null || Double.valueOf(diagnosisProcedureTableDTO.getRestrictionSI()) < (diagnosisProcedureTableDTO.getSubLimitAvaliableAmt() != null && !diagnosisProcedureTableDTO.getSubLimitAmount().equalsIgnoreCase("NA") && SHAUtils.isValidInteger(diagnosisProcedureTableDTO.getSubLimitAmount()) ? diagnosisProcedureTableDTO.getSubLimitAvaliableAmt().doubleValue() : 0d)){
							exceedSumInsrued += bean.getBalanceSI() > Double.valueOf(diagnosisProcedureTableDTO.getRestrictionSI()) && Double.valueOf(diagnosisProcedureTableDTO.getRestrictionSI()) > diagnosisProcedureTableDTO.getNetAmount().doubleValue()  ? Double.valueOf(diagnosisProcedureTableDTO.getRestrictionSI()) - diagnosisProcedureTableDTO.getNetAmount().doubleValue() : 0d;
						}
						else{
							exceedSumInsrued = 0d;
						}
//						exceedSumInsrued += ((diagnosisProcedureTableDTO.getNetAmount() != null ? diagnosisProcedureTableDTO.getNetAmount().doubleValue() : 0d) - (diagnosisProcedureTableDTO.getReverseAllocatedAmt() != null ? diagnosisProcedureTableDTO.getReverseAllocatedAmt().doubleValue() : 0d));
					}
					
//					if(bean.getBalanceSI() == 0d || (diagnosisProcedureTableDTO.getNetAmount() != null && diagnosisProcedureTableDTO.getNetAmount().doubleValue() > bean.getBalanceSI() && (diagnosisProcedureTableDTO.getSubLimitAmount().equalsIgnoreCase("NA") || (!diagnosisProcedureTableDTO.getSubLimitAmount().equalsIgnoreCase("NA") && bean.getBalanceSI() <  (diagnosisProcedureTableDTO.getSubLimitAmount() != null ? diagnosisProcedureTableDTO.getSubLimitAvaliableAmt() != null ? Double.valueOf(diagnosisProcedureTableDTO.getSubLimitAvaliableAmt()) : 0d : 0d)))  
//							|| (bean.getBalanceSI() < (diagnosisProcedureTableDTO.getRestrictionSI() != null && !diagnosisProcedureTableDTO.getRestrictionSI().equalsIgnoreCase("NA") && SHAUtils.isValidInteger(diagnosisProcedureTableDTO.getRestrictionSI()) ? Double.valueOf(diagnosisProcedureTableDTO.getRestrictionSI()): 0d)))) {
					//IMSSUPPOR-31384
					if(bean.getBalanceSI() == 0d || (( diagnosisProcedureTableDTO.getNetAmount() != null && diagnosisProcedureTableDTO.getNetAmount().doubleValue() > bean.getBalanceSI()) || 
							(( diagnosisProcedureTableDTO.getNetAmount() != null && diagnosisProcedureTableDTO.getNetAmount().doubleValue() > bean.getBalanceSI()) && (!diagnosisProcedureTableDTO.getSubLimitAmount().equalsIgnoreCase("NA") && bean.getBalanceSI() <  (diagnosisProcedureTableDTO.getSubLimitAmount() != null ? diagnosisProcedureTableDTO.getSubLimitAvaliableAmt() != null ? Double.valueOf(diagnosisProcedureTableDTO.getSubLimitAvaliableAmt()) : 0d : 0d)))  
						 || (( diagnosisProcedureTableDTO.getNetAmount() != null && diagnosisProcedureTableDTO.getNetAmount().doubleValue() > bean.getBalanceSI()) && (bean.getBalanceSI() < (diagnosisProcedureTableDTO.getRestrictionSI() != null && !diagnosisProcedureTableDTO.getRestrictionSI().equalsIgnoreCase("NA") && SHAUtils.isValidInteger(diagnosisProcedureTableDTO.getRestrictionSI()) ? Double.valueOf(diagnosisProcedureTableDTO.getRestrictionSI()): 0d))))) {

						exceedSumInsrued +=  diagnosisProcedureTableDTO.getNetAmount().doubleValue() - bean.getBalanceSI();
					}
					
					if(diagnosisProcedureTableDTO.getCoPayPercentage() != null && Double.valueOf(diagnosisProcedureTableDTO.getCoPayPercentage().getValue()) > highestCopay ) {
						highestCopay = Double.valueOf(diagnosisProcedureTableDTO.getCoPayPercentage().getValue());
					}
					
					if(diagnosisProcedureTableDTO.getSubLimitAvaliableAmt() != null && diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null
							&& diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getConsiderForPaymentFlag() != null
							&& SHAConstants.YES_FLAG.equalsIgnoreCase(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getConsiderForPaymentFlag())){
						sublimitAvailableAmt += diagnosisProcedureTableDTO.getSubLimitAvaliableAmt();
					}
					
					netAmount += diagnosisProcedureTableDTO.getNetAmount() != null ? diagnosisProcedureTableDTO.getNetAmount().doubleValue() : 0d;
					approvedAmount += diagnosisProcedureTableDTO.getReverseAllocatedAmt() != null ? diagnosisProcedureTableDTO.getReverseAllocatedAmt().doubleValue() : 0d;
				}
			}
//	    	if(bean.getBalanceSI() != null && bean.getIsReverseAllocation() && approvedAmount > bean.getBalanceSI()  && exceedSumInsrued > bean.getBalanceSI()) {
//	    		Double derivedAmt = netAmount - bean.getBalanceSI();
//	    		exceedSumInsrued = (derivedAmt < 0 ? 0 : derivedAmt);
//	    	}
//	    	else if(bean.getBalanceSI() != null && approvedAmount > bean.getBalanceSI()  && exceedSumInsrued < bean.getBalanceSI() && netAmount > exceedSumInsrued){
//	    		Double derivedAmt = netAmount - exceedSumInsrued;
//	    		exceedSumInsrued = (derivedAmt < 0 ? 0 : derivedAmt);
//	    	}
//	    	else{
//	    		exceedSumInsrued = 0d;
//	    	}
	    	
	    	if(ReferenceTable.getRevisedSuperSurplusKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				
				exceedSumInsrued  =  bean.getBalanceSI().longValue() < getDoubleFromStringWithComma(bean.getAmountConsideredForClaimAlert()) ?  getDoubleFromStringWithComma(bean.getAmountConsideredForClaimAlert()) - bean.getBalanceSI() : 0d;
			}
	    	
	    	
	    	bean.setHighestCopay(highestCopay.doubleValue());
	    	bean.setExceedSublimitAmt(exceedSublimit);
	    	bean.setExceedSumInsAmt(exceedSumInsrued);
	    	bean.setExceedPackageAmt(exceedPackage);
	    	bean.setSublimitTotalAvailableAmt(sublimitAvailableAmt);
}
	    
	    public static void doFinalCopayCalculationForSCRCLetter(PreauthDTO bean) {
	    	List<DiagnosisProcedureTableDTO> medicalDecisionTableList = bean.getPreauthMedicalDecisionDetails().getMedicalDecisionTableDTO();
	    	Integer copay30percent = 0;
	    	Integer copay50percent = 0;
	    	Double afterCopayAmt = 0d;
	    	
	    	StringBuffer selectedCopay = new StringBuffer("");
	    	WeakHashMap<Integer, String> selectedCopayMap = new WeakHashMap<Integer, String>();
	    	
	    	for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : medicalDecisionTableList) {
				if(diagnosisProcedureTableDTO.getCoPayPercentage() != null ) {
					if(diagnosisProcedureTableDTO.getCoPayPercentage().getId() != null && !selectedCopayMap.containsKey(diagnosisProcedureTableDTO.getCoPayPercentage().getId().intValue())) {
							selectedCopayMap.put(diagnosisProcedureTableDTO.getCoPayPercentage().getId().intValue(), diagnosisProcedureTableDTO.getCoPayPercentage().getValue()+"%");
					}
					if(30 == diagnosisProcedureTableDTO.getCoPayPercentage().getId().intValue()){
							copay30percent += diagnosisProcedureTableDTO.getCoPayAmount();
					}
					if(50 == diagnosisProcedureTableDTO.getCoPayPercentage().getId().intValue()){
						copay50percent += diagnosisProcedureTableDTO.getCoPayAmount();
					}
				}
				
				if(diagnosisProcedureTableDTO.getNetAmount() != null && diagnosisProcedureTableDTO.getNetAmount().intValue() != 0){
					afterCopayAmt += Double.valueOf(diagnosisProcedureTableDTO.getNetAmount().doubleValue());
				}
			}
		
	    	if(!selectedCopayMap.isEmpty()){
	    		if(selectedCopayMap.containsKey(50) && selectedCopayMap.get(50) != null
    				&& selectedCopayMap.containsKey(30) && selectedCopayMap.get(30) != null){  	
	    			
//    				selectedCopay.append(selectedCopayMap.get(0).toString());
	    				selectedCopay.append(selectedCopayMap.get(30).toString());	    			
	    				selectedCopay.append("and " + selectedCopayMap.get(50).toString() +" as applicable");
	    		}
	    		else if(selectedCopayMap.containsKey(50) && selectedCopayMap.get(50) != null
	    				&& (selectedCopayMap.containsKey(0) || !selectedCopayMap.containsKey(0))
	    				&& (!selectedCopayMap.containsKey(30) || (selectedCopayMap.containsKey(30) && selectedCopayMap.get(30) == null))){
	    			selectedCopay.append(selectedCopayMap.get(50).toString());
	    		}
	    		else if(selectedCopayMap.containsKey(30) && selectedCopayMap.get(30) != null
	    				&& (selectedCopayMap.containsKey(0) || !selectedCopayMap.containsKey(0)) 
	    				&& (!selectedCopayMap.containsKey(50) || (selectedCopayMap.containsKey(50) && selectedCopayMap.get(50) == null))){
	    			selectedCopay.append(selectedCopayMap.get(30).toString());
	    		}
	    		else if(selectedCopayMap.containsKey(0) 
	    				&& (!selectedCopayMap.containsKey(50) || (selectedCopayMap.containsKey(50) && selectedCopayMap.get(50) == null))
	    				&& (!selectedCopayMap.containsKey(30) || (selectedCopayMap.containsKey(30) && selectedCopayMap.get(30) == null))){
	    			selectedCopay.append(selectedCopayMap.get(0).toString());
	    		}
	    	}
	    	
	    	bean.setScrcCopayPercentSelected(selectedCopay.toString());
	    	bean.setCopay30PercentAmt(copay30percent);
	    	bean.setCopay50PercentAmt(copay50percent);
	    	bean.setAmountConsidedAfterCoPay(afterCopayAmt);
	    	selectedCopayMap.clear();
	    	selectedCopayMap = null;
	    }
	    
	    public static int getDateDiffDays(Date fromDate, Date toDate)
	    {
	    	Calendar fromCal = Calendar.getInstance();
	    	Calendar toCal = Calendar.getInstance();
	    	
	    	fromCal.setTime(fromDate);
	    	toCal.setTime(toDate);
	    	
	    	int currentMonthdays = toCal.get(Calendar.DAY_OF_MONTH)-1;  // current Month Days
	    	
	    	/**
	    	 *   To Calculate balance Days of from_Date 
	    	 */
	    	
			int actualday = fromCal.get(Calendar.DAY_OF_MONTH);
			int intimaMonthdays = fromCal.getActualMaximum(fromCal.DAY_OF_MONTH);  
			int intimCompdays = intimaMonthdays - actualday + 1;
			
			
			// diff. between from Day and to_day
						
			int totalDays = toCal.get(Calendar.DAY_OF_MONTH) - fromCal.get(Calendar.DAY_OF_MONTH);
			
			
			if(fromCal.get(Calendar.MONTH) < toCal.get(Calendar.MONTH) || totalDays < 0)
			{
				totalDays = 0;
				for(int m = fromCal.get(Calendar.MONTH) ; m < toCal.get(Calendar.MONTH)-1 ; m++){
				   totalDays +=  fromCal.getActualMaximum(m+1);
				}
				
				totalDays += (intimCompdays + currentMonthdays);
			}
			else{
				totalDays = toCal.get(Calendar.DAY_OF_MONTH) - fromCal.get(Calendar.DAY_OF_MONTH);
			}
	    	
			
//			totalDays = toCal.get(toCal.DAY_OF_YEAR) - fromCal.get(fromCal.DAY_OF_YEAR);

			return totalDays;
	    	
	    	
//	    	final Long ONE_HOUR = 60 * 60 * 1000L;
//
//			Long totalintimDays = (toDate.getTime() - fromDate.getTime() + ONE_HOUR) / (ONE_HOUR * 24);
//			
//			return totalintimDays.intValue();
	    	
	    	
	    }
	    public static Map<Object,String> getConvertCategoryMap(){
	    	Map<Object,String> categoryMap = new HashMap<Object,String>();
	    	
	    	categoryMap.put(ReferenceTable.POST_CASHLESS_QUERY, SHAConstants.QUERY);
	    	categoryMap.put(ReferenceTable.PREAUTH_NOT_RECEIVED, SHAConstants.PREAUTH_NOT_RECEIVED);
	    	categoryMap.put(ReferenceTable.POST_WITHDRAW,SHAConstants.WITHDRAW_PRE_AUTH);
	    	categoryMap.put(ReferenceTable.POST_DENIAL,SHAConstants.CASHLESS_DENIAL);
//	    	categoryMap.put(ReferenceTable.PREAUTH_REJECT_STATUS, SHAConstants.PREAUTHREJECTED);
	    	
	    	return categoryMap;
	    }

	    
	    
	    public static String getBenefitsValue(String flag)
		{
			String benefit="";
			if(flag !=null)
			{
			if(flag.equalsIgnoreCase(SHAConstants.PPD))
				benefit = "Permanent Partial Disability";
			else if(flag.equalsIgnoreCase(SHAConstants.DEATH_FLAGS))
				benefit = "Death";
			else if(flag.equalsIgnoreCase(SHAConstants.PTD))
				benefit = "Permanent Total Disability";
			else if(flag.equalsIgnoreCase(SHAConstants.TTD))
				benefit = "Temporary Total Disability";
			else if(flag.equalsIgnoreCase(SHAConstants.HOSP))
				benefit = "Hospitalisation";
			else if(flag.equalsIgnoreCase(SHAConstants.PART))
				benefit = "Partial Hospitalisation";
			}
					
			return benefit;
		}
	    
	  /*  public static PayloadBOType getCashlessPayloadForPA(PayloadBOType payloadBO) {
	    	if(payloadBO == null) {
	    		payloadBO = new PayloadBOType();
	    	}
	    	ProductInfoType productInfo = payloadBO.getProductInfo();
	    	if(productInfo == null) {
	    		productInfo = new ProductInfoType();
	    	}
			productInfo.setLob(SHAConstants.PA_LOB);
			productInfo.setLobType(SHAConstants.PA_CASHLESS_LOB_TYPE);
			payloadBO.setProductInfo(productInfo);
	    	return payloadBO;
	    }
	    
	    public static PayloadBOType getCashlessPayloadForHealth(PayloadBOType payloadBO) {
	    	if(payloadBO == null) {
	    		payloadBO = new PayloadBOType();
	    	}
	    	ProductInfoType productInfo = payloadBO.getProductInfo();
	    	if(productInfo == null) {
	    		productInfo = new ProductInfoType();
	    	}
			productInfo.setLob(SHAConstants.HEALTH_LOB);
			productInfo.setLobType(SHAConstants.HEALTH_LOB_TYPE);
			payloadBO.setProductInfo(productInfo);
	    	return payloadBO;
	    }
	    
	    public static com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType getReimPayloadForPA(com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType payloadBO) {
	    	if(payloadBO == null) {
	    		payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
	    	}
	    	com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.productinfo.ProductInfoType productInfo = payloadBO.getProductInfo();
	    	if(productInfo == null) {
	    		productInfo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.productinfo.ProductInfoType();
	    	}
			productInfo.setLob(SHAConstants.PA_LOB);
			productInfo.setLobType(SHAConstants.PA_CASHLESS_LOB_TYPE);
			payloadBO.setProductInfo(productInfo);
	    	return payloadBO;
	    }
	    
	    public static com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType getReimPayloadForHealth(com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType payloadBO) {
	    	if(payloadBO == null) {
	    		payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
	    	}
	    	com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.productinfo.ProductInfoType productInfo = payloadBO.getProductInfo();
	    	if(productInfo == null) {
	    		productInfo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.productinfo.ProductInfoType();
	    	}
	    	
			productInfo.setLob(SHAConstants.HEALTH_LOB);
			productInfo.setLobType(SHAConstants.HEALTH_LOB_TYPE);
			payloadBO.setProductInfo(productInfo);
	    	return payloadBO;
	    }
	    
	    public static com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType getReimPayloadForDeathAcc(com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType payloadBO) {
	    	if(payloadBO == null) {
	    		payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
	    	}
	    	com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.productinfo.ProductInfoType productInfo = payloadBO.getProductInfo();
	    	if(productInfo == null) {
	    		productInfo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.productinfo.ProductInfoType();
	    	}
			productInfo.setLob(SHAConstants.PA_LOB);
			productInfo.setLobType(SHAConstants.PA_LOB_TYPE);
			payloadBO.setProductInfo(productInfo);
	    	return payloadBO;
	    }
*/

	    public static List<NoOfDaysCell> copyClaimeDetailsList(List<NoOfDaysCell> claimedAmountDetails){
	    	List<NoOfDaysCell> detailsForBenefitSheet = new ArrayList<NoOfDaysCell>();
	    	NoOfDaysCell dto = null;
	    	NoOfDaysCell medicineDto = new NoOfDaysCell();
	    	NoOfDaysCell consumableDto = new NoOfDaysCell();
	    	
	    	for (NoOfDaysCell noOfDaysCell : claimedAmountDetails) {
				dto = new NoOfDaysCell();
				dto.setKey(noOfDaysCell.getKey());
				dto.setPreauthKey(noOfDaysCell.getPreauthKey());
				dto.setStageId(noOfDaysCell.getStageId());
				dto.setStageName(noOfDaysCell.getStageName());
				dto.setSubStatusId(noOfDaysCell.getSubStatusId());
				dto.setBenefitId(noOfDaysCell.getBenefitId());
				dto.setTotalBillingDays(noOfDaysCell.getTotalBillingDays());
				dto.setBillingPerDayAmount(noOfDaysCell.getBillingPerDayAmount());
				dto.setClaimedBillAmount(noOfDaysCell.getClaimedBillAmount());
				dto.setDeductibleAmount(noOfDaysCell.getDeductibleAmount());
				dto.setNetAmount(noOfDaysCell.getNetAmount());
				dto.setTotalDaysForPolicy(noOfDaysCell.getTotalDaysForPolicy());
				dto.setPolicyMaxAmount(noOfDaysCell.getPolicyMaxAmount());
				dto.setPolicyPerDayPayment(noOfDaysCell.getPolicyPerDayPayment());
				dto.setPaybleAmount(noOfDaysCell.getPaybleAmount());
				dto.setNonPayableAmount(noOfDaysCell.getNonPayableAmount());
                dto.setRestrictTo(noOfDaysCell.getRestrictTo());
                dto.setRestrictToFlag(noOfDaysCell.getRestrictToFlag());
                dto.setOverridePackageDeduction(noOfDaysCell.getOverridePackageDeduction());
                dto.setOverridePackageDeductionFlag(noOfDaysCell.getOverridePackageDeductionFlag());
                dto.setConsiderPerDayAmt(noOfDaysCell.getConsiderPerDayAmt());
                dto.setNonPayableReason(noOfDaysCell.getNonPayableReason());
                dto.setDescription(noOfDaysCell.getDescription());
                dto.setSno(noOfDaysCell.getSno());
				
                if(noOfDaysCell.getDescription().equalsIgnoreCase("Medicines")){
                	medicineDto = dto;
                }
                else if (noOfDaysCell.getDescription().equalsIgnoreCase("consumables")){
                	consumableDto = dto;
                	continue;
                }
                detailsForBenefitSheet.add(dto);

			}
	    	
	    	for (NoOfDaysCell noOfDaysCell : detailsForBenefitSheet) {
				if(noOfDaysCell.getDescription().equalsIgnoreCase("Medicines")){
					noOfDaysCell.setClaimedBillAmount((noOfDaysCell.getClaimedBillAmount() != null ? noOfDaysCell.getClaimedBillAmount() : 0 ) 
														+ (consumableDto.getClaimedBillAmount() != null ? consumableDto.getClaimedBillAmount() : 0));
					noOfDaysCell.setDeductibleAmount((noOfDaysCell.getDeductibleAmount() != null ? noOfDaysCell.getDeductibleAmount() : 0)
														+ (consumableDto.getDeductibleAmount() != null ? consumableDto.getDeductibleAmount() : 0));
					noOfDaysCell.setNetAmount((noOfDaysCell.getNetAmount() != null ? noOfDaysCell.getNetAmount() : 0)
														+(consumableDto.getNetAmount() != null ? consumableDto.getNetAmount() : 0));
					noOfDaysCell.setTotalDaysForPolicy((noOfDaysCell.getTotalDaysForPolicy() != null ? noOfDaysCell.getTotalDaysForPolicy() : 0)
							                            +(consumableDto.getTotalDaysForPolicy() != null ? consumableDto.getTotalDaysForPolicy() : 0));
					noOfDaysCell.setPolicyMaxAmount((noOfDaysCell.getPolicyMaxAmount() != null ? noOfDaysCell.getPolicyMaxAmount() : 0)
														+(consumableDto.getPolicyMaxAmount() != null ? consumableDto.getPolicyMaxAmount() :0));
					noOfDaysCell.setPolicyPerDayPayment((noOfDaysCell.getPolicyPerDayPayment() != null ? noOfDaysCell.getPolicyPerDayPayment() :0)
														+(consumableDto.getPolicyPerDayPayment() != null ? consumableDto.getPolicyPerDayPayment() : 0));
					noOfDaysCell.setPaybleAmount((noOfDaysCell.getPaybleAmount() != null ? noOfDaysCell.getPaybleAmount() : 0)
														+(consumableDto.getPaybleAmount() != null ? consumableDto.getPaybleAmount() : 0));
					noOfDaysCell.setNonPayableAmount((noOfDaysCell.getNonPayableAmount() != null ? noOfDaysCell.getNonPayableAmount() : 0)
														+(consumableDto.getNonPayableAmount() != null ? consumableDto.getNonPayableAmount() : 0));
					noOfDaysCell.setConsiderPerDayAmt((noOfDaysCell.getConsiderPerDayAmt() != null ? noOfDaysCell.getConsiderPerDayAmt() : 0)
														+(consumableDto.getConsiderPerDayAmt() != null ? consumableDto.getConsiderPerDayAmt() : 0));
					noOfDaysCell.setNonPayableReason((noOfDaysCell.getNonPayableReason() != null ? noOfDaysCell.getNonPayableReason() : "")
														+(consumableDto.getNonPayableReason() != null ? consumableDto.getNonPayableReason() : ""));
					noOfDaysCell.setDescription("Medicines and Consumables");
					
				}
			}
	    	
	    	return detailsForBenefitSheet;
	    }
	    
	    public static Object[] getArrayListForDBCall(Claim claim,Hospitals hospital){
	    	Object[] resultArray = new Object[1];
	    	
	    	Object[] attributes = new Object[37];
	    	attributes[SHAConstants.INDEX_WORK_FLOW_KEY] = 0;
	    	attributes[SHAConstants.INDEX_INTIMATION_NO] = claim.getIntimation().getIntimationId();
	    	attributes[SHAConstants.INDEX_INTIMATION_KEY] = claim.getIntimation().getKey();
	    	attributes[SHAConstants.INDEX_INTIMATION_SOURCE] = claim.getIntimation().getIntimationSource().getValue();
	    	attributes[SHAConstants.INDEX_CPU_CODE] = claim.getIntimation().getCpuCode().getCpuCode();
	    	attributes[SHAConstants.INDEX_CLAIM_NUMBER] = claim.getClaimId();
	    	attributes[SHAConstants.INDEX_CLAIM_KEY] = claim.getKey();
	    	attributes[SHAConstants.INDEX_CLAIM_TYPE] = claim.getClaimType().getValue();
	    	attributes[SHAConstants.INDEX_BALANCE_SI_FLAG] = "Y";
	    	
				DATE admissionDate;
				try {
					admissionDate = new DATE(claim.getIntimation().getAdmissionDate().getTime());
					attributes[SHAConstants.INDEX_ADMISSION_DATE] = admissionDate;
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				
//			attributes[SHAConstants.INDEX_ADMISSION_DATE] = claim.getIntimation().getAdmissionDate();	    	
//	    	attributes[SHAConstants.INDEX_ADMISSION_DATE] = null;
				
				DATE createdDate;
				try {
					createdDate = new DATE(claim.getIntimation().getCreatedDate().getTime());
					attributes[SHAConstants.INDEX_INTIMATION_DATE] = createdDate;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				
//				attributes[SHAConstants.INDEX_INTIMATION_DATE] = ;		

	    	attributes[SHAConstants.INDEX_POLICY_KEY] = claim.getIntimation().getPolicy().getKey();
	    	attributes[SHAConstants.INDEX_POLICY_NUMBER] = claim.getIntimation().getPolicy().getPolicyNumber();
	    	attributes[SHAConstants.INDEX_PRODUCT_KEY] = claim.getIntimation().getPolicy().getProduct().getKey();
	    	attributes[SHAConstants.INDEX_PRODUCT_NAME] = claim.getIntimation().getPolicy().getProduct().getValue();
	    	attributes[SHAConstants.INDEX_LOB] = "HEALTH";
	    	attributes[SHAConstants.INDEX_LOB_TYPE] = "H";
	    	attributes[SHAConstants.INDEX_HOSPITAL_TYPE] = claim.getIntimation().getHospitalType().getValue();
	    	attributes[SHAConstants.INDEX_NETWORK_TYPE] = hospital.getNetworkHospitalType();
	    	attributes[SHAConstants.INDEX_HOSPITAL_KEY] = hospital.getKey();
	    	attributes[SHAConstants.INDEX_TREATMENT_TYPE] = "";
	    	attributes[SHAConstants.INDEX_SPECIALITY_NAME] = "";
	    	attributes[SHAConstants.INDEX_PRIORITY] = "";
	    	attributes[SHAConstants.INDEX_RECORD_TYPE] = "FRESH";
	    	attributes[SHAConstants.INDEX_STAGE_SOURCE] = "";
	    	attributes[SHAConstants.INDEX_CASHLESS_NO] = "";
	    	attributes[SHAConstants.INDEX_CASHLESS_KEY] = 0;
	    	attributes[SHAConstants.INDEX_CLAIMED_AMT] = claim.getClaimedAmount();
	    	attributes[SHAConstants.INDEX_ROLE_ID] = "";
	    	attributes[SHAConstants.INDEX_ESCALATE_ROLE_ID] = 0l;
	    	attributes[SHAConstants.INDEX_ESCALATE_USER_ID] = "";
	    	attributes[SHAConstants.INDEX_PROCESS_TYPE] = claim.getClaimType().getValue();
	    	attributes[SHAConstants.INDEX_RECONSIDERATION_FLAG] = "N";
//	    	attributes[SHAConstants.INDEX_REFERENCE_USER_ID] = "ADMINB";
	    	attributes[SHAConstants.INDEX_OUT_COME] = "REGNWFPA";
	    	DATE currentDate = new DATE();
	    	attributes[SHAConstants.INDEX_PROECSSED_DATE] = currentDate;
	    	attributes[SHAConstants.INDEX_USER_ID] = "ADMINB";
	    	resultArray[0] = attributes;

	    	return resultArray;
	    }
	    
	    
	    public static String getIdFromString(String InvestigatorId)
	    {
	    	if(null != InvestigatorId)
	    	{
	    		String arr[] = InvestigatorId.split("-");
	    		System.out.println("-------"+arr[1]);
	    	}
	    	return "";
	    }

	    

	    public static Map<String,Object> getLockObjFromCursor(ResultSet rs){
	    	
	    	Map <String,Object> objectMap = new WeakHashMap<String,Object>();
	    	
	    	try{
	    		objectMap.put(SHAConstants.WK_KEY, (Long)rs.getLong(SHAConstants.WK_KEY));
	    		objectMap.put(SHAConstants.INTIMATION_NO, (String) rs.getString(SHAConstants.INTIMATION_NO));
	    		objectMap.put(SHAConstants.CURRENT_Q, (String) rs.getString(SHAConstants.CURRENT_Q));
	    		objectMap.put(SHAConstants.LOCK_USER, rs.getString(SHAConstants.LOCK_USER));
	    		
	    	}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return objectMap;
	    }
	    
		public static Map <String,Object> getObjectFromCursorObj(ResultSet rs) {
			
			Map <String,Object> objectMap = new WeakHashMap<String,Object>();
			
			try {
				
				
				objectMap.put(SHAConstants.WK_KEY, (Long)rs.getLong(SHAConstants.WK_KEY));
				objectMap.put(SHAConstants.INTIMATION_NO, (String) rs.getString(SHAConstants.INTIMATION_NO));
				objectMap.put(SHAConstants.INTIMATION_KEY, (Long) rs.getLong(SHAConstants.INTIMATION_KEY));
				objectMap.put(SHAConstants.INT_SOURCE, (String) rs.getString(SHAConstants.INT_SOURCE));
				objectMap.put(SHAConstants.CPU_CODE, (String) rs.getString(SHAConstants.CPU_CODE));
				objectMap.put(SHAConstants.CLAIM_NUMBER, (String) rs.getString(SHAConstants.DB_CLAIM_NUMBER));
				objectMap.put(SHAConstants.DB_CLAIM_KEY, (Long) rs.getLong(SHAConstants.DB_CLAIM_KEY));
				objectMap.put(SHAConstants.CLAIM_TYPE, (String) rs.getString(SHAConstants.CLAIM_TYPE));
				objectMap.put(SHAConstants.BAL_SI_FLAG, (String) rs.getString(SHAConstants.BAL_SI_FLAG));
				objectMap.put(SHAConstants.ADMISSION_DATE, (String) rs.getString(SHAConstants.ADMISSION_DATE));
				objectMap.put(SHAConstants.INTIMATION_DATE, (String) rs.getString(SHAConstants.INTIMATION_DATE));
				objectMap.put(SHAConstants.POLICY_KEY, (Long) rs.getLong(SHAConstants.DB_POLICY_KEY));
				objectMap.put(SHAConstants.POLICY_NUMBER, (String) rs.getString(SHAConstants.POLICY_NUMBER));
				objectMap.put(SHAConstants.PRODUCT_KEY, (Long) rs.getLong(SHAConstants.PRODUCT_KEY));
				objectMap.put(SHAConstants.PRODUCT_NAME, (String) rs.getString(SHAConstants.PRODUCT_NAME));
				objectMap.put(SHAConstants.LOB, (String) rs.getString(SHAConstants.LOB));
				objectMap.put(SHAConstants.LOB_TYPE, (String) rs.getString(SHAConstants.LOB_TYPE));
				objectMap.put(SHAConstants.HOSPITAL_TYPE, (String) rs.getString(SHAConstants.HOSPITAL_TYPE));
				objectMap.put(SHAConstants.NETWORK_TYPE, (String) rs.getString(SHAConstants.NETWORK_TYPE));
				objectMap.put(SHAConstants.HOSPITAL_KEY, (Long) rs.getLong(SHAConstants.HOSPITAL_KEY));
				objectMap.put(SHAConstants.TREATEMENT_TYPE, (String) rs.getString(SHAConstants.TREATEMENT_TYPE));
				objectMap.put(SHAConstants.SPECIALITY_NAME, (String) rs.getString(SHAConstants.SPECIALITY_NAME));
				objectMap.put(SHAConstants.PRIORITY, (String) rs.getString(SHAConstants.PRIORITY));
				objectMap.put(SHAConstants.RECORD_TYPE, (String) rs.getString(SHAConstants.RECORD_TYPE));
				objectMap.put(SHAConstants.STAGE_SOURCE, (String) rs.getString(SHAConstants.STAGE_SOURCE));
				objectMap.put(SHAConstants.CASHLESS_NO, (String) rs.getString(SHAConstants.CASHLESS_NO));
				objectMap.put(SHAConstants.CASHLESS_KEY,(Long) rs.getLong(SHAConstants.CASHLESS_KEY));
				objectMap.put(SHAConstants.CLAIMED_AMOUNT, (String) rs.getString(SHAConstants.CLAIMED_AMOUNT));
				objectMap.put(SHAConstants.ROLE_ID,(String) rs.getString(SHAConstants.ROLE_ID));
				objectMap.put(SHAConstants.ESCALATE_ROLE_ID,(Long)rs.getLong(SHAConstants.ESCALATE_ROLE_ID));
				objectMap.put(SHAConstants.ESCALATE_USER_ID,(String) rs.getString(SHAConstants.ESCALATE_USER_ID));
				objectMap.put(SHAConstants.PROCESS_TYPE,(String)rs.getString(SHAConstants.PROCESS_TYPE));
				objectMap.put(SHAConstants.RECONSIDER_FLAG, (String) rs.getString(SHAConstants.RECONSIDER_FLAG));
				objectMap.put(SHAConstants.REFERENCE_USER_ID, (String) rs.getString(SHAConstants.REFERENCE_USER_ID));
				objectMap.put(SHAConstants.CURRENT_Q, (String) rs.getString(SHAConstants.CURRENT_Q));
				objectMap.put(SHAConstants.OUTCOME, (String) rs.getString(SHAConstants.OUTCOME));
				objectMap.put(SHAConstants.ALLOCATED_USER,(String) rs.getString(SHAConstants.ALLOCATED_USER));
				objectMap.put(SHAConstants.ALLOCATED_DATE, (String) rs.getString(SHAConstants.ALLOCATED_DATE));
				objectMap.put(SHAConstants.RRC_ELIGIBILITY_TYPE,(Long)rs.getLong(SHAConstants.RRC_ELIGIBILITY_TYPE));
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return objectMap;
			
		}
		
		public static Object[] setObjArrayForGetTask(Map<String, Object> map) {
			
			Object[] resultArray = new Object[1];
			
			Object[] inputArray = new Object[23];
			inputArray[0] = map.get(SHAConstants.INTIMATION_NO) != null ?  map.get(SHAConstants.INTIMATION_NO) : "";
			inputArray[1] = map.get(SHAConstants.INT_SOURCE) != null ?  map.get(SHAConstants.INT_SOURCE) : "";
			inputArray[2] = map.get(SHAConstants.CPU_CODE) != null ?  map.get(SHAConstants.CPU_CODE) : null;
			inputArray[3] = map.get(SHAConstants.CLAIM_NUMBER) != null ?  map.get(SHAConstants.CLAIM_NUMBER) : "";
			inputArray[4] = map.get(SHAConstants.CLAIM_TYPE) != null ?  map.get(SHAConstants.CLAIM_TYPE) : "";
			inputArray[5] = map.get(SHAConstants.ADMISSION_DATE) != null ?  map.get(SHAConstants.ADMISSION_DATE) : "";
			inputArray[6] = map.get(SHAConstants.INTIMATION_DATE) != null ?  map.get(SHAConstants.INTIMATION_DATE) : "";
			inputArray[7] = map.get(SHAConstants.POLICY_NUMBER) != null ?  map.get(SHAConstants.POLICY_NUMBER) : "";
			inputArray[8] = map.get(SHAConstants.PRODUCT_NAME) != null ?  map.get(SHAConstants.PRODUCT_NAME) : "";
			inputArray[9] = map.get(SHAConstants.LOB) != null ?  map.get(SHAConstants.LOB) : "";
			inputArray[10] = map.get(SHAConstants.LOB_TYPE) != null ?  map.get(SHAConstants.LOB_TYPE) : "";
			inputArray[11] = map.get(SHAConstants.HOSPITAL_TYPE) != null ?  map.get(SHAConstants.HOSPITAL_TYPE) : "";
			inputArray[12] = map.get(SHAConstants.NETWORK_TYPE) != null ?  map.get(SHAConstants.NETWORK_TYPE) : "";
			inputArray[13] = map.get(SHAConstants.TREATEMENT_TYPE) != null ?  map.get(SHAConstants.TREATEMENT_TYPE) : "";
			inputArray[14] = map.get(SHAConstants.SPECIALITY_NAME) != null ?  map.get(SHAConstants.SPECIALITY_NAME) : "";
			inputArray[15] = map.get(SHAConstants.PRIORITY) != null ?  map.get(SHAConstants.PRIORITY) : "";
			inputArray[16] = map.get(SHAConstants.RECORD_TYPE) != null ?  map.get(SHAConstants.RECORD_TYPE) : "";
			inputArray[17] = map.get(SHAConstants.STAGE_SOURCE) != null ?  map.get(SHAConstants.STAGE_SOURCE) : "";
			inputArray[18] = map.get(SHAConstants.CLM_FM_AMT) != null ?  map.get(SHAConstants.CLM_FM_AMT) : null;
			inputArray[19] = map.get(SHAConstants.CLM_TO_AMT) != null ?  map.get(SHAConstants.CLM_TO_AMT) :null;
			inputArray[20] = map.get(SHAConstants.PROCESS_TYPE) != null ?  map.get(SHAConstants.PROCESS_TYPE) : "";
			inputArray[21] = map.get(SHAConstants.CURRENT_Q) != null ?  map.get(SHAConstants.CURRENT_Q) : "WFPA";
			inputArray[22] = map.get(SHAConstants.USER_ID) != null ?  map.get(SHAConstants.USER_ID) : "ADMINB";
			
			resultArray[0]= inputArray;
			
			return resultArray;
			
		}
		
		   public static List<Object[]> getArrayListFromGettask(List<Map<String,Object>> list){
			   
			   List<Object[]> objList = new ArrayList<Object[]>() ;
			   for (Map<String, Object> map : list) {
				   
				   Object[] resultArray = new Object[1];
			    	
			    	Object[] attributes = new Object[85];
			    	attributes[SHAConstants.INDEX_WORK_FLOW_KEY] = map.get(SHAConstants.WK_KEY);
			    	attributes[SHAConstants.INDEX_INTIMATION_NO] = map.get(SHAConstants.INTIMATION_NO);
			    	attributes[SHAConstants.INDEX_INTIMATION_KEY] = map.get(SHAConstants.INTIMATION_KEY);
			    	attributes[SHAConstants.INDEX_INTIMATION_SOURCE] = map.get(SHAConstants.INT_SOURCE);
			    	attributes[SHAConstants.INDEX_CPU_CODE] =map.get(SHAConstants.CPU_CODE);
			    	attributes[SHAConstants.INDEX_CLAIM_NUMBER] = map.get(SHAConstants.DB_CLAIM_NUMBER);
			    	attributes[SHAConstants.INDEX_CLAIM_KEY] = map.get(SHAConstants.DB_CLAIM_KEY);
			    	attributes[SHAConstants.INDEX_CLAIM_TYPE] =map.get(SHAConstants.CLAIM_TYPE);
			    	attributes[SHAConstants.INDEX_BALANCE_SI_FLAG] =map.get(SHAConstants.BAL_SI_FLAG);
			    	attributes[SHAConstants.INDEX_ADMISSION_DATE] = map.get(SHAConstants.ADMISSION_DATE);
			    	attributes[SHAConstants.INDEX_INTIMATION_DATE] =map.get(SHAConstants.INTIMATION_DATE);
			    	attributes[SHAConstants.INDEX_POLICY_KEY] =map.get(SHAConstants.POLICY_KEY);
			    	attributes[SHAConstants.INDEX_POLICY_NUMBER] = map.get(SHAConstants.POLICY_NUMBER);
			    	attributes[SHAConstants.INDEX_PRODUCT_KEY] = map.get(SHAConstants.PRODUCT_KEY);
			    	attributes[SHAConstants.INDEX_PRODUCT_NAME] = map.get(SHAConstants.PRODUCT_NAME);
			    	attributes[SHAConstants.INDEX_LOB] =map.get(SHAConstants.LOB);
			    	attributes[SHAConstants.INDEX_LOB_TYPE] =map.get(SHAConstants.LOB_TYPE);
			    	attributes[SHAConstants.INDEX_HOSPITAL_TYPE] = map.get(SHAConstants.HOSPITAL_TYPE);
			    	attributes[SHAConstants.INDEX_NETWORK_TYPE] = map.get(SHAConstants.NETWORK_TYPE);
			    	attributes[SHAConstants.INDEX_HOSPITAL_KEY] = map.get(SHAConstants.HOSPITAL_KEY);
			    	attributes[SHAConstants.INDEX_TREATMENT_TYPE] = map.get(SHAConstants.TREATEMENT_TYPE);
			    	attributes[SHAConstants.INDEX_SPECIALITY_NAME] =map.get(SHAConstants.SPECIALITY_NAME);
			    	attributes[SHAConstants.INDEX_PRIORITY] = map.get(SHAConstants.PRIORITY);
			    	attributes[SHAConstants.INDEX_RECORD_TYPE] = map.get(SHAConstants.RECORD_TYPE);
			    	attributes[SHAConstants.INDEX_STAGE_SOURCE] = map.get(SHAConstants.STAGE_SOURCE);
			    	attributes[SHAConstants.INDEX_CASHLESS_NO] = map.get(SHAConstants.CASHLESS_NO);
			    	attributes[SHAConstants.INDEX_CASHLESS_KEY] =map.get(SHAConstants.CASHLESS_KEY);
			    	attributes[SHAConstants.INDEX_CLAIMED_AMT] =map.get(SHAConstants.CLAIMED_AMOUNT);
			    	attributes[SHAConstants.INDEX_ROLE_ID] = map.get(SHAConstants.ROLE_ID);
			    	attributes[SHAConstants.INDEX_ESCALATE_ROLE_ID] =map.get(SHAConstants.ESCALATE_ROLE_ID);
			    	attributes[SHAConstants.INDEX_ESCALATE_USER_ID] =map.get(SHAConstants.ESCALATE_USER_ID);
			    	attributes[SHAConstants.INDEX_PROCESS_TYPE] = map.get(SHAConstants.PROCESS_TYPE);
			    	attributes[SHAConstants.INDEX_RECONSIDERATION_FLAG] =map.get(SHAConstants.RECONSIDER_FLAG);
			    	attributes[SHAConstants.INDEX_REFERENCE_USER_ID] = map.get(SHAConstants.REFERENCE_USER_ID);
			    	attributes[SHAConstants.INDEX_OUT_COME] = map.get(SHAConstants.OUTCOME);
//			    	attributes[SHAConstants.INDEX_PROECSSED_DATE] = new DATE();
			    	attributes[SHAConstants.INDEX_PROECSSED_DATE] = map.get(SHAConstants.PROCESSED_DATE);
			    	attributes[SHAConstants.INDEX_USER_ID] = map.get(SHAConstants.USER_ID);
//			    	attributes[SHAConstants.INDEX_PROCESS] = map.get(SHAConstants.PAYLOAD_PROCESS) != null ? map.get(SHAConstants.PAYLOAD_PROCESS) : "";					
//					attributes[SHAConstants.INDEX_REGISTRATION_DATE] = map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) != null ? map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) : "";			    	
//			    	attributes[SHAConstants.INDEX_FVR_KEY] = map.get(SHAConstants.FVR_KEY) != null ? map.get(SHAConstants.FVR_KEY) : 0;
//			    	attributes[SHAConstants.INDEX_FVR_CPU_CODE] = map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE) != null ? map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE) : "";
//			    	attributes[SHAConstants.INDEX_FVR_NUMBER] = map.get(SHAConstants.PAYLOAD_FVR_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_FVR_NUMBER) : "";
//			    	attributes[SHAConstants.INDEX_ROD_KEY] = map.get(SHAConstants.PAYLOAD_ROD_KEY) != null ? map.get(SHAConstants.PAYLOAD_ROD_KEY) : "";
//			    	attributes[SHAConstants.INDEX_ROD_NUMBER] = map.get(SHAConstants.PAYLOAD_ROD_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_ROD_NUMBER) : "";
//			    	attributes[SHAConstants.INDEX_ROD_CREATED_DATE] = map.get(SHAConstants.PAYLOAD_ROD_CREATED_DATE) != null ? map.get(SHAConstants.PAYLOAD_ROD_CREATED_DATE) : null;
//			    	attributes[SHAConstants.INDEX_REIMB_REQ_BY] = map.get(SHAConstants.PAYLOAD_REIMB_REQ_BY) != null ? map.get(SHAConstants.PAYLOAD_REIMB_REQ_BY) : "";
//			    	attributes[SHAConstants.INDEX_ROD_REFER_BY] = map.get(SHAConstants.PAYLOAD_ROD_REFER_BY) != null ? map.get(SHAConstants.PAYLOAD_ROD_REFER_BY) : "";
//			    	attributes[SHAConstants.INDEX_ACK_KEY] = map.get(SHAConstants.PAYLOAD_ACK_KEY) != null ? map.get(SHAConstants.PAYLOAD_ACK_KEY) :  0;
//			    	attributes[SHAConstants.INDEX_ACK_NUMBER] = map.get(SHAConstants.PAYLOAD_ACK_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_ACK_NUMBER) : "";
//			    	attributes[SHAConstants.INDEX_PREAUTH_STATUS] = map.get(SHAConstants.PAYLOAD_PREAUTH_STATUS) != null ? map.get(SHAConstants.PAYLOAD_PREAUTH_STATUS) : "";
//			    	attributes[SHAConstants.INDEX_BILL_AVAILABLE] = map.get(SHAConstants.PAYLOAD_BILL_AVAILABLE) != null ? map.get(SHAConstants.PAYLOAD_BILL_AVAILABLE) : "";
//			    	attributes[SHAConstants.INDEX_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_HOSPITALIZATION) != null ? map.get(SHAConstants.PAYLOAD_HOSPITALIZATION) : "";
//			    	attributes[SHAConstants.INDEX_POST_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_POST_HOSPITALIZATION) != null ? map.get(SHAConstants.PAYLOAD_POST_HOSPITALIZATION) : "";
//			    	attributes[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION) != null ? map.get(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION) : "";
//			    	attributes[SHAConstants.INDEX_PRE_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION) != null ? map.get(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION) : "";
//			    	attributes[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = map.get(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT) != null ? map.get(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT) : "";
//			    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE) != null ? map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE) : "";
//			    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH) != null ? map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH) : "";
//			    	attributes[SHAConstants.INDEX_RRC_REQUEST_KEY] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY) != null ? map.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY) : 0;
//			    	attributes[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER) : "";
//			    	attributes[SHAConstants.INDEX_RRC_REQUEST_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) != null ? map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) : "";
//			    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE) != null ? map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE) : "";
//			    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY) != null ? map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY) : "";
//			    	attributes[SHAConstants.INDEX_PED_KEY] = map.get(SHAConstants.PAYLOAD_PED_KEY) != null ? map.get(SHAConstants.PAYLOAD_PED_KEY) : "";
//			    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ROLE] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE) != null ? map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE) : "";
//			    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ID] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ID) != null ? map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ID) : "";
//			    	attributes[SHAConstants.INDEX_PED_REFERRED_BY] = map.get(SHAConstants.PAYLOAD_PED_REFERRED_BY) != null ? map.get(SHAConstants.PAYLOAD_PED_REFERRED_BY) : "";
//			    	attributes[SHAConstants.INDEX_QUERY_KEY] = map.get(SHAConstants.PAYLOAD_QUERY_KEY) != null ? map.get(SHAConstants.PAYLOAD_QUERY_KEY) : "";
//			    	attributes[SHAConstants.INDEX_QUERY_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_QUERY_DISAPPROVED) != null ? map.get(SHAConstants.PAYLOAD_QUERY_DISAPPROVED) : "";
//			    	attributes[SHAConstants.INDEX_QUERY_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY) != null ? map.get(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY) : "";
//			    	attributes[SHAConstants.INDEX_INVESTIGATION_KEY]  = map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY) != null ? map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY) : "";
//			    	attributes[SHAConstants.INDEX_INV_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_INV_DISAPPROVED) != null ? map.get(SHAConstants.PAYLOAD_INV_DISAPPROVED) : "";
//			    	attributes[SHAConstants.INDEX_INV_REQUEST] = map.get(SHAConstants.PAYLOAD_INV_REQUEST) != null ? map.get(SHAConstants.PAYLOAD_INV_REQUEST) : "";
//			    	attributes[SHAConstants.INDEX_INV_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_INV_REQUESTED_BY) != null ? map.get(SHAConstants.PAYLOAD_INV_REQUESTED_BY) : "";
//			    	attributes[SHAConstants.INDEX_REMINDER_CATEGORY] = map.get(SHAConstants.PAYLOAD_REMINDER_CATEGORY) != null ? map.get(SHAConstants.PAYLOAD_REMINDER_CATEGORY) : "";
//			    	attributes[SHAConstants.INDEX_HEALTH_CARD_NUMBER] = map.get(SHAConstants.PAYLOAD_HEALTH_CARD_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_HEALTH_CARD_NUMBER) : "";
//			    	attributes[SHAConstants.INDEX_REMINDER_TYPE] = map.get(SHAConstants.PAYLOAD_REMINDER_TYPE) != null ? map.get(SHAConstants.PAYLOAD_REMINDER_TYPE) : "";
			    	
			    	attributes[SHAConstants.INDEX_PROCESS] = map.get(SHAConstants.PAYLOAD_PROCESS);					
					attributes[SHAConstants.INDEX_REGISTRATION_DATE] = map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) ;			    	
			    	attributes[SHAConstants.INDEX_FVR_KEY] = map.get(SHAConstants.FVR_KEY);
			    	attributes[SHAConstants.INDEX_FVR_CPU_CODE] = map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE);
			    	attributes[SHAConstants.INDEX_FVR_NUMBER] = map.get(SHAConstants.PAYLOAD_FVR_NUMBER) ;
			    	attributes[SHAConstants.INDEX_ROD_KEY] = map.get(SHAConstants.PAYLOAD_ROD_KEY) ;
			    	attributes[SHAConstants.INDEX_ROD_NUMBER] = map.get(SHAConstants.PAYLOAD_ROD_NUMBER) ;
			    	attributes[SHAConstants.INDEX_ROD_CREATED_DATE] = map.get(SHAConstants.PAYLOAD_ROD_CREATED_DATE);
			    	attributes[SHAConstants.INDEX_REIMB_REQ_BY] = map.get(SHAConstants.PAYLOAD_REIMB_REQ_BY) ;
			    	attributes[SHAConstants.INDEX_ROD_REFER_BY] = map.get(SHAConstants.PAYLOAD_ROD_REFER_BY);
			    	attributes[SHAConstants.INDEX_ACK_KEY] = map.get(SHAConstants.PAYLOAD_ACK_KEY);
			    	attributes[SHAConstants.INDEX_ACK_NUMBER] = map.get(SHAConstants.PAYLOAD_ACK_NUMBER);
			    	attributes[SHAConstants.INDEX_PREAUTH_STATUS] = map.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
			    	attributes[SHAConstants.INDEX_BILL_AVAILABLE] = map.get(SHAConstants.PAYLOAD_BILL_AVAILABLE) ;
			    	attributes[SHAConstants.INDEX_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_HOSPITALIZATION);
			    	attributes[SHAConstants.INDEX_POST_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_POST_HOSPITALIZATION);
			    	attributes[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION);
			    	attributes[SHAConstants.INDEX_PRE_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION);
			    	attributes[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = map.get(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT) ;
			    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE);
			    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH);
			    	attributes[SHAConstants.INDEX_RRC_REQUEST_KEY] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY);
			    	attributes[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER);
			    	attributes[SHAConstants.INDEX_RRC_REQUEST_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) ;
			    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE);
			    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY);
			    	attributes[SHAConstants.INDEX_PED_KEY] = map.get(SHAConstants.PAYLOAD_PED_KEY) ;
			    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ROLE] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE);
			    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ID] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ID) ;
			    	attributes[SHAConstants.INDEX_PED_REFERRED_BY] = map.get(SHAConstants.PAYLOAD_PED_REFERRED_BY);
			    	attributes[SHAConstants.INDEX_QUERY_KEY] = map.get(SHAConstants.PAYLOAD_QUERY_KEY) ;
			    	attributes[SHAConstants.INDEX_QUERY_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_QUERY_DISAPPROVED) ;
			    	attributes[SHAConstants.INDEX_QUERY_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY);
			    	attributes[SHAConstants.INDEX_INVESTIGATION_KEY]  = map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY);
			    	attributes[SHAConstants.INDEX_INV_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_INV_DISAPPROVED);
			    	attributes[SHAConstants.INDEX_INV_REQUEST] = map.get(SHAConstants.PAYLOAD_INV_REQUEST) ;
			    	attributes[SHAConstants.INDEX_INV_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_INV_REQUESTED_BY) ;
			    	attributes[SHAConstants.INDEX_REMINDER_CATEGORY] = map.get(SHAConstants.PAYLOAD_REMINDER_CATEGORY);
			    	attributes[SHAConstants.INDEX_HEALTH_CARD_NUMBER] = map.get(SHAConstants.PAYLOAD_HEALTH_CARD_NUMBER);
			    	attributes[SHAConstants.INDEX_REMINDER_TYPE] = map.get(SHAConstants.PAYLOAD_REMINDER_TYPE);
			    	attributes[SHAConstants.INDEX_PAYMENT_CANCELLATION] = map.get(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION);
			    	attributes[SHAConstants.INDEX_DOCUMENT_RECEVIED_FROM] = map.get(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM);
			    	attributes[SHAConstants.INDEX_ZONAL_BYPASS] = map.get(SHAConstants.PAYLOAD_ZONAL_BY_PASS);
			    	attributes[SHAConstants.INDEX_REMINDER_LATTER_FLAG] = map.get(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG);
			    	attributes[SHAConstants.INDEX_ALLOCATED_USER] = map.get(SHAConstants.PAYLOAD_ALLOCATED_USER);
			    	attributes[SHAConstants.INDEX_ALLOCATED_DATE] = map.get(SHAConstants.PAYLOAD_ALLOCATED_DATE);
			    	attributes[SHAConstants.INDEX_PED_INITIATED_DATE] = map.get(SHAConstants.PAYLOAD_PED_INITIATED_DATE);
			    	
			    	resultArray[0] = attributes;
			    	objList.add(resultArray);
			}
			return objList;
			   
		   }
		   
		   public static void setDBActiveOrDeactiveClaim(Long workFlowKey,VaadinSession session) {
			   
			   //Long  existingTaskNumber= (Long)session.getAttribute(SHAConstants.WK_KEY);
				/*if(existingTaskNumber != null){
					DBCalculationService dbCalculationService = new DBCalculationService();
					dbCalculationService.callUnlockProcedure(existingTaskNumber);
				}*/	
				
				if(workFlowKey != null){
					session.setAttribute(SHAConstants.WK_KEY, workFlowKey);		
				
				}
			}
		   
		   public static Object[] getObjArrayForSubmit(Map<String,Object> map){
			   
				   
				   Object[] resultArray = new Object[1];
			    	
			    	Object[] attributes = new Object[37];
			    	attributes[SHAConstants.INDEX_WORK_FLOW_KEY] = map.get(SHAConstants.WK_KEY);
			    	attributes[SHAConstants.INDEX_INTIMATION_NO] = map.get(SHAConstants.INTIMATION_NO);
			    	attributes[SHAConstants.INDEX_INTIMATION_KEY] = map.get(SHAConstants.INTIMATION_KEY);
			    	attributes[SHAConstants.INDEX_INTIMATION_SOURCE] = map.get(SHAConstants.INT_SOURCE);
			    	attributes[SHAConstants.INDEX_CPU_CODE] =map.get(SHAConstants.CPU_CODE);
			    	attributes[SHAConstants.INDEX_CLAIM_NUMBER] = map.get(SHAConstants.DB_CLAIM_NUMBER);
			    	attributes[SHAConstants.INDEX_CLAIM_KEY] = map.get(SHAConstants.DB_CLAIM_KEY);
			    	attributes[SHAConstants.INDEX_CLAIM_TYPE] =map.get(SHAConstants.CLAIM_TYPE);
			    	attributes[SHAConstants.INDEX_BALANCE_SI_FLAG] =map.get(SHAConstants.BAL_SI_FLAG);
			    	attributes[SHAConstants.INDEX_ADMISSION_DATE] = map.get(SHAConstants.ADMISSION_DATE);
			    	attributes[SHAConstants.INDEX_INTIMATION_DATE] =map.get(SHAConstants.INTIMATION_DATE);
			    	attributes[SHAConstants.INDEX_POLICY_KEY] =map.get(SHAConstants.POLICY_KEY);
			    	attributes[SHAConstants.INDEX_POLICY_NUMBER] = map.get(SHAConstants.POLICY_NUMBER);
			    	attributes[SHAConstants.INDEX_PRODUCT_KEY] = map.get(SHAConstants.PRODUCT_KEY);
			    	attributes[SHAConstants.INDEX_PRODUCT_NAME] = map.get(SHAConstants.PRODUCT_NAME);
			    	attributes[SHAConstants.INDEX_LOB] =map.get(SHAConstants.LOB);
			    	attributes[SHAConstants.INDEX_LOB_TYPE] =map.get(SHAConstants.LOB_TYPE);
			    	attributes[SHAConstants.INDEX_HOSPITAL_TYPE] = map.get(SHAConstants.HOSPITAL_TYPE);
			    	attributes[SHAConstants.INDEX_NETWORK_TYPE] = map.get(SHAConstants.NETWORK_TYPE);
			    	attributes[SHAConstants.INDEX_HOSPITAL_KEY] = map.get(SHAConstants.HOSPITAL_KEY);
			    	attributes[SHAConstants.INDEX_TREATMENT_TYPE] = map.get(SHAConstants.TREATEMENT_TYPE);
			    	attributes[SHAConstants.INDEX_SPECIALITY_NAME] =map.get(SHAConstants.SPECIALITY_NAME);
			    	attributes[SHAConstants.INDEX_PRIORITY] = map.get(SHAConstants.PRIORITY);
			    	attributes[SHAConstants.INDEX_RECORD_TYPE] = map.get(SHAConstants.RECORD_TYPE);
			    	attributes[SHAConstants.INDEX_STAGE_SOURCE] = map.get(SHAConstants.STAGE_SOURCE);
			    	attributes[SHAConstants.INDEX_CASHLESS_NO] = map.get(SHAConstants.CASHLESS_NO);
			    	attributes[SHAConstants.INDEX_CASHLESS_KEY] =map.get(SHAConstants.CASHLESS_KEY);
			    	attributes[SHAConstants.INDEX_CLAIMED_AMT] =map.get(SHAConstants.CLAIMED_AMOUNT);
			    	attributes[SHAConstants.INDEX_ROLE_ID] = map.get(SHAConstants.ROLE_ID);
			    	attributes[SHAConstants.INDEX_ESCALATE_ROLE_ID] =map.get(SHAConstants.ESCALATE_ROLE_ID);
			    	attributes[SHAConstants.INDEX_ESCALATE_USER_ID] =map.get(SHAConstants.ESCALATE_USER_ID);
			    	attributes[SHAConstants.INDEX_PROCESS_TYPE] = map.get(SHAConstants.PROCESS_TYPE);
			    	attributes[SHAConstants.INDEX_RECONSIDERATION_FLAG] =map.get(SHAConstants.RECONSIDER_FLAG);
			    	attributes[SHAConstants.INDEX_REFERENCE_USER_ID] = map.get(SHAConstants.REFERENCE_USER_ID);
			    	attributes[SHAConstants.INDEX_OUT_COME] = map.get(SHAConstants.OUTCOME);
			    	attributes[SHAConstants.INDEX_PROECSSED_DATE] = new DATE();
			    	attributes[SHAConstants.INDEX_USER_ID] = map.get(SHAConstants.USER_ID);
			    	resultArray[0] = attributes;
			
			return resultArray;
			   
		   }
		   public static Object[] getRevisedObjArrayForSubmit(Map<String,Object> map){			   
			   
			   Object[] resultArray = new Object[1];
		    	
		    	Object[] attributes = new Object[85];
		    	attributes[SHAConstants.INDEX_WORK_FLOW_KEY] = map.get(SHAConstants.WK_KEY);
		    	attributes[SHAConstants.INDEX_INTIMATION_NO] = map.get(SHAConstants.INTIMATION_NO);
		    	attributes[SHAConstants.INDEX_INTIMATION_KEY] = map.get(SHAConstants.INTIMATION_KEY);
		    	attributes[SHAConstants.INDEX_INTIMATION_SOURCE] = map.get(SHAConstants.INT_SOURCE);
		    	attributes[SHAConstants.INDEX_CPU_CODE] =map.get(SHAConstants.CPU_CODE);
		    	attributes[SHAConstants.INDEX_CLAIM_NUMBER] = map.get(SHAConstants.DB_CLAIM_NUMBER);
		    	attributes[SHAConstants.INDEX_CLAIM_KEY] = map.get(SHAConstants.DB_CLAIM_KEY);
		    	attributes[SHAConstants.INDEX_CLAIM_TYPE] =map.get(SHAConstants.CLAIM_TYPE);
		    	attributes[SHAConstants.INDEX_BALANCE_SI_FLAG] =map.get(SHAConstants.BAL_SI_FLAG);
		    	attributes[SHAConstants.INDEX_ADMISSION_DATE] = map.get(SHAConstants.ADMISSION_DATE);
		    	attributes[SHAConstants.INDEX_INTIMATION_DATE] =map.get(SHAConstants.INTIMATION_DATE);
		    	attributes[SHAConstants.INDEX_POLICY_KEY] =map.get(SHAConstants.POLICY_KEY);
		    	attributes[SHAConstants.INDEX_POLICY_NUMBER] = map.get(SHAConstants.POLICY_NUMBER);
		    	attributes[SHAConstants.INDEX_PRODUCT_KEY] = map.get(SHAConstants.PRODUCT_KEY);
		    	attributes[SHAConstants.INDEX_PRODUCT_NAME] = map.get(SHAConstants.PRODUCT_NAME);
		    	attributes[SHAConstants.INDEX_LOB] =map.get(SHAConstants.LOB);
		    	attributes[SHAConstants.INDEX_LOB_TYPE] =map.get(SHAConstants.LOB_TYPE);
		    	attributes[SHAConstants.INDEX_HOSPITAL_TYPE] = map.get(SHAConstants.HOSPITAL_TYPE);
		    	attributes[SHAConstants.INDEX_NETWORK_TYPE] = map.get(SHAConstants.NETWORK_TYPE);
		    	attributes[SHAConstants.INDEX_HOSPITAL_KEY] = map.get(SHAConstants.HOSPITAL_KEY);
		    	attributes[SHAConstants.INDEX_TREATMENT_TYPE] = map.get(SHAConstants.TREATEMENT_TYPE);
		    	attributes[SHAConstants.INDEX_SPECIALITY_NAME] =map.get(SHAConstants.SPECIALITY_NAME);
		    	attributes[SHAConstants.INDEX_PRIORITY] = map.get(SHAConstants.PRIORITY);
		    	attributes[SHAConstants.INDEX_RECORD_TYPE] = map.get(SHAConstants.RECORD_TYPE);
		    	attributes[SHAConstants.INDEX_STAGE_SOURCE] = map.get(SHAConstants.STAGE_SOURCE);
		    	attributes[SHAConstants.INDEX_CASHLESS_NO] = map.get(SHAConstants.CASHLESS_NO);
		    	attributes[SHAConstants.INDEX_CASHLESS_KEY] =map.get(SHAConstants.CASHLESS_KEY);
		    	attributes[SHAConstants.INDEX_CLAIMED_AMT] =map.get(SHAConstants.CLAIMED_AMOUNT);
		    	attributes[SHAConstants.INDEX_ROLE_ID] = map.get(SHAConstants.ROLE_ID);
		    	attributes[SHAConstants.INDEX_ESCALATE_ROLE_ID] =map.get(SHAConstants.ESCALATE_ROLE_ID);
		    	attributes[SHAConstants.INDEX_ESCALATE_USER_ID] =map.get(SHAConstants.ESCALATE_USER_ID);
		    	attributes[SHAConstants.INDEX_PROCESS_TYPE] = map.get(SHAConstants.PROCESS_TYPE);
		    	attributes[SHAConstants.INDEX_RECONSIDERATION_FLAG] =map.get(SHAConstants.RECONSIDER_FLAG);
		    	attributes[SHAConstants.INDEX_REFERENCE_USER_ID] = map.get(SHAConstants.REFERENCE_USER_ID);
		    	attributes[SHAConstants.INDEX_OUT_COME] = map.get(SHAConstants.OUTCOME);
		    	attributes[SHAConstants.INDEX_PROECSSED_DATE] = map.get(SHAConstants.PROCESSED_DATE);
		    	attributes[SHAConstants.INDEX_USER_ID] = map.get(SHAConstants.USER_ID);
	/*	    	attributes[SHAConstants.INDEX_PROCESS] = map.get(SHAConstants.PAYLOAD_PROCESS) != null ? map.get(SHAConstants.PAYLOAD_PROCESS) : "";					
				attributes[SHAConstants.INDEX_REGISTRATION_DATE] = map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) != null ? map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) : "";			    	
		    	attributes[SHAConstants.INDEX_FVR_KEY] = map.get(SHAConstants.FVR_KEY) != null ? map.get(SHAConstants.FVR_KEY) : 0;
		    	attributes[SHAConstants.INDEX_FVR_CPU_CODE] = map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE) != null ? map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE) : "";
		    	attributes[SHAConstants.INDEX_FVR_NUMBER] = map.get(SHAConstants.PAYLOAD_FVR_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_FVR_NUMBER) : "";
		    	attributes[SHAConstants.INDEX_ROD_KEY] = map.get(SHAConstants.PAYLOAD_ROD_KEY) != null ? map.get(SHAConstants.PAYLOAD_ROD_KEY) : "";
		    	attributes[SHAConstants.INDEX_ROD_NUMBER] = map.get(SHAConstants.PAYLOAD_ROD_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_ROD_NUMBER) : "";
		    	attributes[SHAConstants.INDEX_ROD_CREATED_DATE] = map.get(SHAConstants.PAYLOAD_ROD_CREATED_DATE) != null ? map.get(SHAConstants.PAYLOAD_ROD_CREATED_DATE) : null;
		    	attributes[SHAConstants.INDEX_REIMB_REQ_BY] = map.get(SHAConstants.PAYLOAD_REIMB_REQ_BY) != null ? map.get(SHAConstants.PAYLOAD_REIMB_REQ_BY) : "";
		    	attributes[SHAConstants.INDEX_ROD_REFER_BY] = map.get(SHAConstants.PAYLOAD_ROD_REFER_BY) != null ? map.get(SHAConstants.PAYLOAD_ROD_REFER_BY) : "";
		    	attributes[SHAConstants.INDEX_ACK_KEY] = map.get(SHAConstants.PAYLOAD_ACK_KEY) != null ? map.get(SHAConstants.PAYLOAD_ACK_KEY) :  0;
		    	attributes[SHAConstants.INDEX_ACK_NUMBER] = map.get(SHAConstants.PAYLOAD_ACK_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_ACK_NUMBER) : "";
		    	attributes[SHAConstants.INDEX_PREAUTH_STATUS] = map.get(SHAConstants.PAYLOAD_PREAUTH_STATUS) != null ? map.get(SHAConstants.PAYLOAD_PREAUTH_STATUS) : "";
		    	attributes[SHAConstants.INDEX_BILL_AVAILABLE] = map.get(SHAConstants.PAYLOAD_BILL_AVAILABLE) != null ? map.get(SHAConstants.PAYLOAD_BILL_AVAILABLE) : "";
		    	attributes[SHAConstants.INDEX_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_HOSPITALIZATION) != null ? map.get(SHAConstants.PAYLOAD_HOSPITALIZATION) : "";
		    	attributes[SHAConstants.INDEX_POST_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_POST_HOSPITALIZATION) != null ? map.get(SHAConstants.PAYLOAD_POST_HOSPITALIZATION) : "";
		    	attributes[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION) != null ? map.get(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION) : "";
		    	attributes[SHAConstants.INDEX_PRE_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION) != null ? map.get(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION) : "";
		    	attributes[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = map.get(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT) != null ? map.get(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT) : "";
		    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE) != null ? map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE) : "";
		    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH) != null ? map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH) : "";
		    	attributes[SHAConstants.INDEX_RRC_REQUEST_KEY] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY) != null ? map.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY) : 0;
		    	attributes[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER) : "";
		    	attributes[SHAConstants.INDEX_RRC_REQUEST_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) != null ? map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) : "";
		    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE) != null ? map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE) : "";
		    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY) != null ? map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY) : "";
		    	attributes[SHAConstants.INDEX_PED_KEY] = map.get(SHAConstants.PAYLOAD_PED_KEY) != null ? map.get(SHAConstants.PAYLOAD_PED_KEY) : "";
		    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ROLE] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE) != null ? map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE) : "";
		    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ID] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ID) != null ? map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ID) : "";
		    	attributes[SHAConstants.INDEX_PED_REFERRED_BY] = map.get(SHAConstants.PAYLOAD_PED_REFERRED_BY) != null ? map.get(SHAConstants.PAYLOAD_PED_REFERRED_BY) : "";
		    	attributes[SHAConstants.INDEX_QUERY_KEY] = map.get(SHAConstants.PAYLOAD_QUERY_KEY) != null ? map.get(SHAConstants.PAYLOAD_QUERY_KEY) : "";
		    	attributes[SHAConstants.INDEX_QUERY_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_QUERY_DISAPPROVED) != null ? map.get(SHAConstants.PAYLOAD_QUERY_DISAPPROVED) : "";
		    	attributes[SHAConstants.INDEX_QUERY_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY) != null ? map.get(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY) : "";
		    	attributes[SHAConstants.INDEX_INVESTIGATION_KEY]  = map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY) != null ? map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY) : "";
		    	attributes[SHAConstants.INDEX_INV_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_INV_DISAPPROVED) != null ? map.get(SHAConstants.PAYLOAD_INV_DISAPPROVED) : "";
		    	attributes[SHAConstants.INDEX_INV_REQUEST] = map.get(SHAConstants.PAYLOAD_INV_REQUEST) != null ? map.get(SHAConstants.PAYLOAD_INV_REQUEST) : "";
		    	attributes[SHAConstants.INDEX_INV_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_INV_REQUESTED_BY) != null ? map.get(SHAConstants.PAYLOAD_INV_REQUESTED_BY) : "";
		    	attributes[SHAConstants.INDEX_REMINDER_CATEGORY] = map.get(SHAConstants.PAYLOAD_REMINDER_CATEGORY) != null ? map.get(SHAConstants.PAYLOAD_REMINDER_CATEGORY) : "";
		    	attributes[SHAConstants.INDEX_HEALTH_CARD_NUMBER] = map.get(SHAConstants.PAYLOAD_HEALTH_CARD_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_HEALTH_CARD_NUMBER) : "";
		    	attributes[SHAConstants.INDEX_REMINDER_TYPE] = map.get(SHAConstants.PAYLOAD_REMINDER_TYPE) != null ? map.get(SHAConstants.PAYLOAD_REMINDER_TYPE) : "";*/
		    	
		    		attributes[SHAConstants.INDEX_PROCESS] = map.get(SHAConstants.PAYLOAD_PROCESS);					
					attributes[SHAConstants.INDEX_REGISTRATION_DATE] = map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) ;			    	
			    	attributes[SHAConstants.INDEX_FVR_KEY] = map.get(SHAConstants.FVR_KEY);
			    	attributes[SHAConstants.INDEX_FVR_CPU_CODE] = map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE);
			    	attributes[SHAConstants.INDEX_FVR_NUMBER] = map.get(SHAConstants.PAYLOAD_FVR_NUMBER) ;
			    	attributes[SHAConstants.INDEX_ROD_KEY] = map.get(SHAConstants.PAYLOAD_ROD_KEY) ;
			    	attributes[SHAConstants.INDEX_ROD_NUMBER] = map.get(SHAConstants.PAYLOAD_ROD_NUMBER) ;
			    	attributes[SHAConstants.INDEX_ROD_CREATED_DATE] = map.get(SHAConstants.PAYLOAD_ROD_CREATED_DATE);
			    	attributes[SHAConstants.INDEX_REIMB_REQ_BY] = map.get(SHAConstants.PAYLOAD_REIMB_REQ_BY) ;
			    	attributes[SHAConstants.INDEX_ROD_REFER_BY] = map.get(SHAConstants.PAYLOAD_ROD_REFER_BY);
			    	attributes[SHAConstants.INDEX_ACK_KEY] = map.get(SHAConstants.PAYLOAD_ACK_KEY);
			    	attributes[SHAConstants.INDEX_ACK_NUMBER] = map.get(SHAConstants.PAYLOAD_ACK_NUMBER);
			    	attributes[SHAConstants.INDEX_PREAUTH_STATUS] = map.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
			    	attributes[SHAConstants.INDEX_BILL_AVAILABLE] = map.get(SHAConstants.PAYLOAD_BILL_AVAILABLE) ;
			    	attributes[SHAConstants.INDEX_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_HOSPITALIZATION);
			    	attributes[SHAConstants.INDEX_POST_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_POST_HOSPITALIZATION);
			    	attributes[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION);
			    	attributes[SHAConstants.INDEX_PRE_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION);
			    	attributes[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = map.get(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT) ;
			    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE);
			    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH);
			    	attributes[SHAConstants.INDEX_RRC_REQUEST_KEY] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY);
			    	attributes[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER);
			    	attributes[SHAConstants.INDEX_RRC_REQUEST_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) ;
			    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE);
			    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY);
			    	attributes[SHAConstants.INDEX_PED_KEY] = map.get(SHAConstants.PAYLOAD_PED_KEY) ;
			    	attributes[SHAConstants.INDEX_PED_TYPE] = map.get(SHAConstants.PAYLOAD_PED_TYPE);
			    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ROLE] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE);
			    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ID] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ID) ;
			    	attributes[SHAConstants.INDEX_PED_REFERRED_BY] = map.get(SHAConstants.PAYLOAD_PED_REFERRED_BY);
			    	attributes[SHAConstants.INDEX_PREAUTH_STATUS] =  map.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
			    	attributes[SHAConstants.INDEX_QUERY_KEY] = map.get(SHAConstants.PAYLOAD_QUERY_KEY) ;
			    	attributes[SHAConstants.INDEX_QUERY_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_QUERY_DISAPPROVED) ;
			    	attributes[SHAConstants.INDEX_QUERY_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY);
			    	attributes[SHAConstants.INDEX_INVESTIGATION_KEY]  = map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY);
			    	attributes[SHAConstants.INDEX_INV_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_INV_DISAPPROVED);
			    	attributes[SHAConstants.INDEX_INV_REQUEST] = map.get(SHAConstants.PAYLOAD_INV_REQUEST) ;
			    	attributes[SHAConstants.INDEX_INV_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_INV_REQUESTED_BY) ;
			    	attributes[SHAConstants.INDEX_REMINDER_CATEGORY] = map.get(SHAConstants.PAYLOAD_REMINDER_CATEGORY);
			    	attributes[SHAConstants.INDEX_HEALTH_CARD_NUMBER] = map.get(SHAConstants.PAYLOAD_HEALTH_CARD_NUMBER);
			    	attributes[SHAConstants.INDEX_REMINDER_TYPE] = map.get(SHAConstants.PAYLOAD_REMINDER_TYPE);
			    	attributes[SHAConstants.INDEX_PAYMENT_CANCELLATION] = map.get(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION);
			    	attributes[SHAConstants.INDEX_DOCUMENT_RECEVIED_FROM] = map.get(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM);
			    	attributes[SHAConstants.INDEX_ZONAL_BYPASS] = map.get(SHAConstants.PAYLOAD_ZONAL_BY_PASS);
			    	attributes[SHAConstants.INDEX_REMINDER_LATTER_FLAG] = map.get(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG);
			    	attributes[SHAConstants.INDEX_ALLOCATED_USER] = map.get(SHAConstants.PAYLOAD_ALLOCATED_USER);
			    	attributes[SHAConstants.INDEX_ALLOCATED_DATE] = map.get(SHAConstants.PAYLOAD_ALLOCATED_DATE);
			    	attributes[SHAConstants.INDEX_PED_INITIATED_DATE] = map.get(SHAConstants.PAYLOAD_PED_INITIATED_DATE);
			    	
		    	resultArray[0] = attributes;
				
				return resultArray;
		   }
		   
		   public static Object[] getArrayListForManualRegDBCall(Intimation intimation,Hospitals hospital){
		    	Object[] resultArray = new Object[1];
		    	
		    	Object[] attributes = new Object[37];
		    	attributes[SHAConstants.INDEX_WORK_FLOW_KEY] = 0;
		    	attributes[SHAConstants.INDEX_INTIMATION_NO] = intimation.getIntimationId();
		    	attributes[SHAConstants.INDEX_INTIMATION_KEY] = intimation.getKey();
		    	attributes[SHAConstants.INDEX_INTIMATION_SOURCE] = intimation.getIntimationSource().getValue();
		    	attributes[SHAConstants.INDEX_CPU_CODE] =intimation.getCpuCode().getCpuCode();
		    	attributes[SHAConstants.INDEX_CLAIM_NUMBER] ="";
		    	attributes[SHAConstants.INDEX_CLAIM_KEY] =0;
		    	attributes[SHAConstants.INDEX_CLAIM_TYPE] = "";
		    	attributes[SHAConstants.INDEX_BALANCE_SI_FLAG] = "Y";
		    	
//					DATE admissionDate = new DATE(claim.getIntimation().getAdmissionDate().getTime());
//					attributes[SHAConstants.INDEX_ADMISSION_DATE] = claim.getIntimation().getAdmissionDate();
		    	attributes[SHAConstants.INDEX_ADMISSION_DATE] = intimation.getAdmissionDate();
//		    	attributes[SHAConstants.INDEX_ADMISSION_DATE] = null;
					
//					DATE createdDate = new DATE(claim.getIntimation().getCreatedDate().getTime());
					attributes[SHAConstants.INDEX_INTIMATION_DATE] = intimation.getCreatedDate();
//					attributes[SHAConstants.INDEX_INTIMATION_DATE] = null;
					
				

		    	attributes[SHAConstants.INDEX_POLICY_KEY] =intimation.getPolicy().getKey();
		    	attributes[SHAConstants.INDEX_POLICY_NUMBER] = intimation.getPolicy().getPolicyNumber();
		    	attributes[SHAConstants.INDEX_PRODUCT_KEY] = intimation.getPolicy().getProduct().getKey();
		    	attributes[SHAConstants.INDEX_PRODUCT_NAME] = intimation.getPolicy().getProduct().getValue();
		    	attributes[SHAConstants.INDEX_LOB] = "HEALTH";
		    	attributes[SHAConstants.INDEX_LOB_TYPE] = "H";
		    	attributes[SHAConstants.INDEX_HOSPITAL_TYPE] =intimation.getHospitalType().getValue();
		    	attributes[SHAConstants.INDEX_NETWORK_TYPE] = hospital.getNetworkHospitalType();
		    	attributes[SHAConstants.INDEX_HOSPITAL_KEY] = hospital.getKey();
		    	attributes[SHAConstants.INDEX_TREATMENT_TYPE] = "";
		    	attributes[SHAConstants.INDEX_SPECIALITY_NAME] = "";
		    	attributes[SHAConstants.INDEX_PRIORITY] = "";
		    	attributes[SHAConstants.INDEX_RECORD_TYPE] = "FRESH";
		    	attributes[SHAConstants.INDEX_STAGE_SOURCE] =  intimation.getStage().getStageName();
		    	attributes[SHAConstants.INDEX_CASHLESS_NO] = "";
		    	attributes[SHAConstants.INDEX_CASHLESS_KEY] = 0;
		    	attributes[SHAConstants.INDEX_CLAIMED_AMT] = null;
		    	attributes[SHAConstants.INDEX_ROLE_ID] = "";
		    	attributes[SHAConstants.INDEX_ESCALATE_ROLE_ID] = 0l;
		    	attributes[SHAConstants.INDEX_ESCALATE_USER_ID] = "";
		    	attributes[SHAConstants.INDEX_PROCESS_TYPE] = "";
		    	attributes[SHAConstants.INDEX_RECONSIDERATION_FLAG] = "N";
//		    	attributes[SHAConstants.INDEX_REFERENCE_USER_ID] = "ADMINB";
		    	attributes[SHAConstants.INDEX_OUT_COME] = "REGNWFPA";
		    	DATE currentDate = new DATE();
		    	attributes[SHAConstants.INDEX_PROECSSED_DATE] = currentDate;
		    	attributes[SHAConstants.INDEX_USER_ID] = "ADMINB";
		    	resultArray[0] = attributes;

		    	return resultArray;
		    }
		   
		   
		   public static Map<String,Object> getReallocationUserFromCursor(ResultSet rs){
		    	
		    	Map <String,Object> objectMap = new WeakHashMap<String,Object>();
		    	
		    	try{
		    		objectMap.put(SHAConstants.REALLOCATION_LOGIN_ID, (String)rs.getString(SHAConstants.REALLOCATION_LOGIN_ID));
		    		objectMap.put(SHAConstants.REALLOCATION_LOGIN_NAME, (String) rs.getString(SHAConstants.REALLOCATION_LOGIN_NAME));
		    		objectMap.put(SHAConstants.REALLOCATION_LOGIN_DATE_TIME, (String) rs.getString(SHAConstants.REALLOCATION_LOGIN_DATE_TIME));
		    		objectMap.put(SHAConstants.REALLOCATION_ACTIVE_STATUS, (Integer)rs.getInt(SHAConstants.REALLOCATION_ACTIVE_STATUS));
		    		objectMap.put(SHAConstants.REALLOCATION_TOTAL_ASSIGNED, (Integer)rs.getInt(SHAConstants.REALLOCATION_TOTAL_ASSIGNED));
		    		objectMap.put(SHAConstants.REALLOCATION_COMPLETED, (Integer)rs.getInt(SHAConstants.REALLOCATION_COMPLETED));
		    		objectMap.put(SHAConstants.REALLOCATION_PENDING, (Integer)rs.getInt(SHAConstants.REALLOCATION_PENDING));
		    		
		    	}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return objectMap;
		    }
	    
	    
		   public static Object[] setRevisedObjArrayForGetTask(Map<String, Object> map) {
				
				Object[] resultArray = new Object[1];
				
				Object[] inputArray = new Object[37];
				inputArray[0] = map.get(SHAConstants.INTIMATION_NO) != null ?  map.get(SHAConstants.INTIMATION_NO) : "";
				inputArray[1] = map.get(SHAConstants.INT_SOURCE) != null ?  map.get(SHAConstants.INT_SOURCE) : "";
				inputArray[2] = map.get(SHAConstants.CPU_CODE) != null ?  map.get(SHAConstants.CPU_CODE) : null;
				inputArray[3] = map.get(SHAConstants.CLAIM_NUMBER) != null ?  map.get(SHAConstants.CLAIM_NUMBER) : "";
				inputArray[4] = map.get(SHAConstants.CLAIM_TYPE) != null ?  map.get(SHAConstants.CLAIM_TYPE) : "";
				inputArray[5] = map.get(SHAConstants.ADMISSION_DATE) != null ?  map.get(SHAConstants.ADMISSION_DATE) : "";
				inputArray[6] = map.get(SHAConstants.INTIMATION_DATE) != null ?  map.get(SHAConstants.INTIMATION_DATE) : "";
				inputArray[7] = map.get(SHAConstants.POLICY_NUMBER) != null ?  map.get(SHAConstants.POLICY_NUMBER) : "";
				inputArray[8] = map.get(SHAConstants.PRODUCT_NAME) != null ?  map.get(SHAConstants.PRODUCT_NAME) : "";
				inputArray[9] = map.get(SHAConstants.LOB) != null ?  map.get(SHAConstants.LOB) : "";
				inputArray[10] = map.get(SHAConstants.LOB_TYPE) != null ?  map.get(SHAConstants.LOB_TYPE) : "";
				inputArray[11] = map.get(SHAConstants.HOSPITAL_TYPE) != null ?  map.get(SHAConstants.HOSPITAL_TYPE) : "";
				inputArray[12] = map.get(SHAConstants.NETWORK_TYPE) != null ?  map.get(SHAConstants.NETWORK_TYPE) : "";
				inputArray[13] = map.get(SHAConstants.TREATEMENT_TYPE) != null ?  map.get(SHAConstants.TREATEMENT_TYPE) : "";
				inputArray[14] = map.get(SHAConstants.SPECIALITY_NAME) != null ?  map.get(SHAConstants.SPECIALITY_NAME) : "";
				inputArray[15] = map.get(SHAConstants.PRIORITY) != null ?  map.get(SHAConstants.PRIORITY) : "";
				inputArray[16] = map.get(SHAConstants.RECORD_TYPE) != null ?  map.get(SHAConstants.RECORD_TYPE) : "";
				inputArray[17] = map.get(SHAConstants.STAGE_SOURCE) != null ?  map.get(SHAConstants.STAGE_SOURCE) : "";
				inputArray[18] = map.get(SHAConstants.CLM_FM_AMT) != null ?  map.get(SHAConstants.CLM_FM_AMT) : null;
				inputArray[19] = map.get(SHAConstants.CLM_TO_AMT) != null ?  map.get(SHAConstants.CLM_TO_AMT) :null;
				inputArray[20] = map.get(SHAConstants.PROCESS_TYPE) != null ?  map.get(SHAConstants.PROCESS_TYPE) : "";
				inputArray[21] = map.get(SHAConstants.CURRENT_Q) != null ?  map.get(SHAConstants.CURRENT_Q) : "WFPA";
				inputArray[22] = map.get(SHAConstants.USER_ID) != null ?  map.get(SHAConstants.USER_ID) : "ADMINB";
				inputArray[23] = map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) != null ? map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) : null;
				inputArray[24] = map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE) != null ? map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE) : null;
				inputArray[25] = map.get(SHAConstants.PAYLOAD_ROD_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_ROD_NUMBER) : "";
				inputArray[26] = map.get(SHAConstants.PAYLOAD_ACK_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_ACK_NUMBER) : "";
				inputArray[27] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER) : "";
				inputArray[28] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) != null ? map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) : "";
				inputArray[29] = map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY) != null ? map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY) : null;
				inputArray[30] = map.get(SHAConstants.PAYLOAD_QUERY_KEY) != null ? map.get(SHAConstants.PAYLOAD_QUERY_KEY) : null;
				inputArray[31] = map.get(SHAConstants.PAYLOAD_PED_KEY) != null ? map.get(SHAConstants.PAYLOAD_PED_KEY) :null;
				inputArray[32] = map.get(SHAConstants.PAYLOAD_PED_TYPE) != null ? map.get(SHAConstants.PAYLOAD_PED_TYPE) :"";
				inputArray[33] = map.get(SHAConstants.PAYLOAD_REFERENCE_USER_ID) != null ? map.get(SHAConstants.PAYLOAD_REFERENCE_USER_ID) : null;
				inputArray[34] = map.get(SHAConstants.PAYLOAD_BILLCLASS_HOSP) != null ? map.get(SHAConstants.PAYLOAD_BILLCLASS_HOSP) : null;
				inputArray[35] = map.get(SHAConstants.PAYLOAD_BILLCLASS_POSTHOSP) != null ? map.get(SHAConstants.PAYLOAD_BILLCLASS_POSTHOSP) : null;
				inputArray[36] = map.get(SHAConstants.PAYLOAD_BILLCLASS_PREHOSP) != null ? map.get(SHAConstants.PAYLOAD_BILLCLASS_PREHOSP) : null;
				//inputArray[37] = map.get(SHAConstants.CRM_PRIORITY) != null ? map.get(SHAConstants.CRM_PRIORITY) : null;
				resultArray[0]= inputArray;
				
				return resultArray;
				
			}

		   public static Object[] getRevisedArrayListForDBCall(Claim claim,Hospitals hospital){
		    	Object[] resultArray = new Object[1];
		    	
		    	Object[] attributes = new Object[85];
		    	attributes[SHAConstants.INDEX_WORK_FLOW_KEY] = 0;
		    	attributes[SHAConstants.INDEX_INTIMATION_NO] = claim.getIntimation().getIntimationId();
		    	attributes[SHAConstants.INDEX_INTIMATION_KEY] = claim.getIntimation().getKey();
		    	attributes[SHAConstants.INDEX_INTIMATION_SOURCE] = claim.getIntimation().getIntimationSource().getValue();
		    	attributes[SHAConstants.INDEX_CPU_CODE] = claim.getIntimation().getCpuCode().getCpuCode();
		    	attributes[SHAConstants.INDEX_CLAIM_NUMBER] = claim.getClaimId();
		    	attributes[SHAConstants.INDEX_CLAIM_KEY] = claim.getKey();
		    	attributes[SHAConstants.INDEX_CLAIM_TYPE] = claim.getClaimType().getValue();
		    	attributes[SHAConstants.INDEX_BALANCE_SI_FLAG] = "Y";
		    	
//					DATE admissionDate = new DATE(claim.getIntimation().getAdmissionDate().getTime());
//					attributes[SHAConstants.INDEX_ADMISSION_DATE] = claim.getIntimation().getAdmissionDate();
		    	
		    	attributes[SHAConstants.INDEX_ADMISSION_DATE] = claim.getIntimation().getAdmissionDate();
//		    	attributes[SHAConstants.INDEX_ADMISSION_DATE] = null;
					
//					DATE createdDate = new DATE(claim.getIntimation().getCreatedDate().getTime());
					attributes[SHAConstants.INDEX_INTIMATION_DATE] = claim.getIntimation().getCreatedDate();
//					attributes[SHAConstants.INDEX_INTIMATION_DATE] = null;
					
				

		    	attributes[SHAConstants.INDEX_POLICY_KEY] = claim.getIntimation().getPolicy().getKey();
		    	attributes[SHAConstants.INDEX_POLICY_NUMBER] = claim.getIntimation().getPolicy().getPolicyNumber();
		    	attributes[SHAConstants.INDEX_PRODUCT_KEY] = claim.getIntimation().getPolicy().getProduct().getKey();
		    	attributes[SHAConstants.INDEX_PRODUCT_NAME] = claim.getIntimation().getPolicy().getProduct().getValue();
		    	if((ReferenceTable.HEALTH_LOB_KEY).equals(claim.getIntimation().getPolicy().getLobId())){
		    		attributes[SHAConstants.INDEX_LOB] = SHAConstants.HEALTH_LOB;	
		    		attributes[SHAConstants.INDEX_LOB_TYPE] = "H";
		    	}
		    	else if((ReferenceTable.PA_LOB_KEY).equals(claim.getIntimation().getPolicy().getLobId()) || 
		    			(ReferenceTable.PACKAGE_MASTER_VALUE).equals(claim.getIntimation().getPolicy().getLobId())){
		    		attributes[SHAConstants.INDEX_LOB] = SHAConstants.PA_LOB;	
		    		attributes[SHAConstants.INDEX_LOB_TYPE] = "P";
		    	}	    	
		    	
		    	
		    	if((ReferenceTable.PACKAGE_MASTER_VALUE).equals(claim.getIntimation().getPolicy().getLobId())){
		    		
		    		attributes[SHAConstants.INDEX_LOB] = SHAConstants.PA_LOB;	
		    		attributes[SHAConstants.INDEX_LOB_TYPE] = "P";
		    		
		    		if(claim.getIntimation().getProcessClaimType().equalsIgnoreCase(SHAConstants.HEALTH_LOB_FLAG))
		    		{
		    			attributes[SHAConstants.INDEX_LOB] = SHAConstants.HEALTH_LOB;	
			    		attributes[SHAConstants.INDEX_LOB_TYPE] = SHAConstants.HEALTH_LOB_FLAG;
		    		}
		    		
		    	}
		    	
		    	if(null != claim.getIntimation().getInsured().getLopFlag() &&
		    			(SHAConstants.PA_LOB_TYPE).equals(claim.getIntimation().getInsured().getLopFlag())){
		    		
		    		attributes[SHAConstants.INDEX_LOB] = SHAConstants.PA_LOB;	
		    		attributes[SHAConstants.INDEX_LOB_TYPE] = SHAConstants.PA_LOB_TYPE;
		    	}
		    	
//		    	attributes[SHAConstants.INDEX_LOB_TYPE] = claim.getProcessClaimType();
		    	attributes[SHAConstants.INDEX_HOSPITAL_TYPE] = claim.getIntimation().getHospitalType().getValue();
		    	attributes[SHAConstants.INDEX_NETWORK_TYPE] = hospital.getNetworkHospitalType();
		    	attributes[SHAConstants.INDEX_HOSPITAL_KEY] = hospital.getKey();
		    	attributes[SHAConstants.INDEX_TREATMENT_TYPE] = "";
		    	attributes[SHAConstants.INDEX_SPECIALITY_NAME] = "";
		    	attributes[SHAConstants.INDEX_PRIORITY] = claim.getIsVipCustomer() != null ? (claim.getIsVipCustomer().intValue() == 1 ? SHAConstants.VIP_CUSTOMER  : SHAConstants.NORMAL) : "" ;
		    	if(claim.getIntimation().getInsured() != null && claim.getIntimation().getInsured().getInsuredAge() != null && claim.getIntimation().getInsured().getInsuredAge().intValue()>60){
		    		attributes[SHAConstants.INDEX_PRIORITY] = SHAConstants.SENIOR_CITIZEN;
		    	}
		    	//added for updating covid priority value by noufel
		    	if(claim != null && claim.getPriorityEvent() != null && !claim.getPriorityEvent().trim().isEmpty()){
		    		attributes[SHAConstants.INDEX_PRIORITY] = claim.getPriorityEvent() != null ? claim.getPriorityEvent() : SHAConstants.NORMAL ;
		    	}
		    	attributes[SHAConstants.INDEX_RECORD_TYPE] = "FRESH";
		    	attributes[SHAConstants.INDEX_STAGE_SOURCE] = claim.getStage().getStageName();
		    	attributes[SHAConstants.INDEX_CASHLESS_NO] = "";
		    	attributes[SHAConstants.INDEX_CASHLESS_KEY] = 0;
		    	attributes[SHAConstants.INDEX_CLAIMED_AMT] = claim.getClaimedAmount();
		    	attributes[SHAConstants.INDEX_ROLE_ID] = "";
		    	attributes[SHAConstants.INDEX_ESCALATE_ROLE_ID] = 0;
		    	attributes[SHAConstants.INDEX_ESCALATE_USER_ID] = "";
		    	attributes[SHAConstants.INDEX_PROCESS_TYPE] = claim.getClaimType().getValue();
		    	attributes[SHAConstants.INDEX_RECONSIDERATION_FLAG] = "N";
//		    	attributes[SHAConstants.INDEX_REFERENCE_USER_ID] = "ADMINB";
		    	attributes[SHAConstants.INDEX_OUT_COME] = "REGNWFPA";
		    	DATE currentDate = new DATE();
		    	attributes[SHAConstants.INDEX_PROECSSED_DATE] = currentDate;
		    	attributes[SHAConstants.INDEX_USER_ID] = "ADMINB";
		    	attributes[SHAConstants.INDEX_PROCESS] = "";
		    	try {
		    		if(claim.getCreatedDate() != null){
						currentDate = new DATE(claim.getCreatedDate());
						attributes[SHAConstants.INDEX_REGISTRATION_DATE] = currentDate;
		    		}
		    		else{
		    			attributes[SHAConstants.INDEX_REGISTRATION_DATE] = null;
		    		}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
							    	
		    	attributes[SHAConstants.INDEX_FVR_KEY] = 0;
		    	attributes[SHAConstants.INDEX_FVR_CPU_CODE] = "";
		    	attributes[SHAConstants.INDEX_FVR_NUMBER] = "";
		    	attributes[SHAConstants.INDEX_ROD_KEY] = 0;
		    	attributes[SHAConstants.INDEX_ROD_NUMBER] = "";
//		    	Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		    	attributes[SHAConstants.INDEX_ROD_CREATED_DATE] = null;
		    	attributes[SHAConstants.INDEX_REIMB_REQ_BY] = "";
		    	attributes[SHAConstants.INDEX_ROD_REFER_BY] = "";
		    	attributes[SHAConstants.INDEX_ACK_KEY] = 0;
		    	attributes[SHAConstants.INDEX_ACK_NUMBER] = "";
		    	attributes[SHAConstants.INDEX_PREAUTH_STATUS] = "";
		    	attributes[SHAConstants.INDEX_BILL_AVAILABLE] = "";
		    	attributes[SHAConstants.INDEX_HOSPITALIZATION] = "";
		    	attributes[SHAConstants.INDEX_POST_HOSPITALIZATION] = "";
		    	attributes[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = "";
		    	attributes[SHAConstants.INDEX_PRE_HOSPITALIZATION] = "";
		    	attributes[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = "";
		    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = "";
		    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = "";
		    	attributes[SHAConstants.INDEX_RRC_REQUEST_KEY] = 0;
		    	attributes[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = "";
		    	attributes[SHAConstants.INDEX_RRC_REQUEST_TYPE] = claim.getCrcFlag();
		    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY_TYPE] = 0;
		    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY] = 0;
		    	attributes[SHAConstants.INDEX_PED_KEY] = 0;
		    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ROLE] = "";
		    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ID] = 0;
		    	attributes[SHAConstants.INDEX_PED_REFERRED_BY] = "";
		    	attributes[SHAConstants.INDEX_QUERY_KEY] = 0;
		    	attributes[SHAConstants.INDEX_QUERY_DISAPPROVED] = "";
		    	attributes[SHAConstants.INDEX_QUERY_REQUESTED_BY] = "";
		    	attributes[SHAConstants.INDEX_INVESTIGATION_KEY] = 0;
		    	attributes[SHAConstants.INDEX_INV_DISAPPROVED] = "";
		    	attributes[SHAConstants.INDEX_INV_REQUEST] = "";
		    	attributes[SHAConstants.INDEX_INV_REQUESTED_BY] = "";
		    	attributes[SHAConstants.INDEX_REMINDER_CATEGORY] = "";
		    	attributes[SHAConstants.INDEX_HEALTH_CARD_NUMBER] = "";
		    	attributes[SHAConstants.INDEX_REMINDER_TYPE] = "";	 
		    	attributes[SHAConstants.INDEX_PED_TYPE] = "";
		    	attributes[SHAConstants.INDEX_PAYMENT_CANCELLATION] = "";
		    	attributes[SHAConstants.INDEX_DOCUMENT_RECEVIED_FROM] = "";
		    	attributes[SHAConstants.INDEX_ZONAL_BYPASS] = "";
		    	attributes[SHAConstants.INDEX_REMINDER_LATTER_FLAG] = "";
		    	attributes[SHAConstants.INDEX_ALLOCATED_USER] = "";
		    	attributes[SHAConstants.INDEX_ALLOCATED_DATE] = null;
		    	attributes[SHAConstants.INDEX_PED_INITIATED_DATE] = null;
		    	
		    	
		    	resultArray[0] = attributes;
		    	
		    	return resultArray;
		    }
		   
		    public static void main (String args[])
		    {
		    	SHAUtils.getIdFromString("INV-027");
		    		
		    		
		    }
		   
		   
		   public static Object[] getRevisedArrayListForManualRegistrationDBCall(Intimation intimation,Hospitals hospital){
		    	Object[] resultArray = new Object[1];
		    	
		    	Object[] attributes = new Object[85];
		    	attributes[SHAConstants.INDEX_WORK_FLOW_KEY] = 0;
		    	attributes[SHAConstants.INDEX_INTIMATION_NO] = intimation.getIntimationId();
		    	attributes[SHAConstants.INDEX_INTIMATION_KEY] = intimation.getKey();
		    	attributes[SHAConstants.INDEX_INTIMATION_SOURCE] = intimation.getIntimationSource().getValue();
		    	attributes[SHAConstants.INDEX_CPU_CODE] =intimation.getCpuCode().getCpuCode();
		    	attributes[SHAConstants.INDEX_CLAIM_NUMBER] ="";
		    	attributes[SHAConstants.INDEX_CLAIM_KEY] =0;
		    	attributes[SHAConstants.INDEX_CLAIM_TYPE] = "";
		    	attributes[SHAConstants.INDEX_BALANCE_SI_FLAG] = "Y";
		    	
//					DATE admissionDate = new DATE(claim.getIntimation().getAdmissionDate().getTime());
//					attributes[SHAConstants.INDEX_ADMISSION_DATE] = claim.getIntimation().getAdmissionDate();
		    	attributes[SHAConstants.INDEX_ADMISSION_DATE] = intimation.getAdmissionDate();
//		    	attributes[SHAConstants.INDEX_ADMISSION_DATE] = null;
					
//					DATE createdDate = new DATE(claim.getIntimation().getCreatedDate().getTime());
					attributes[SHAConstants.INDEX_INTIMATION_DATE] = intimation.getCreatedDate();
//					attributes[SHAConstants.INDEX_INTIMATION_DATE] = null;
					
				

		    	attributes[SHAConstants.INDEX_POLICY_KEY] =intimation.getPolicy().getKey();
		    	attributes[SHAConstants.INDEX_POLICY_NUMBER] = intimation.getPolicy().getPolicyNumber();
		    	attributes[SHAConstants.INDEX_PRODUCT_KEY] = intimation.getPolicy().getProduct().getKey();
		    	attributes[SHAConstants.INDEX_PRODUCT_NAME] = intimation.getPolicy().getProduct().getValue();
		    	attributes[SHAConstants.INDEX_LOB] = "HEALTH";
		    	attributes[SHAConstants.INDEX_LOB_TYPE] = "H";
		    	attributes[SHAConstants.INDEX_HOSPITAL_TYPE] =intimation.getHospitalType().getValue();
		    	attributes[SHAConstants.INDEX_NETWORK_TYPE] = hospital.getNetworkHospitalType();
		    	attributes[SHAConstants.INDEX_HOSPITAL_KEY] = hospital.getKey();
		    	attributes[SHAConstants.INDEX_TREATMENT_TYPE] = "";
		    	attributes[SHAConstants.INDEX_SPECIALITY_NAME] = "";
		    	attributes[SHAConstants.INDEX_PRIORITY] = "";
		    	attributes[SHAConstants.INDEX_RECORD_TYPE] = "FRESH";
		    	attributes[SHAConstants.INDEX_STAGE_SOURCE] =  intimation.getStage().getStageName();
		    	attributes[SHAConstants.INDEX_CASHLESS_NO] = "";
		    	attributes[SHAConstants.INDEX_CASHLESS_KEY] = 0;
		    	attributes[SHAConstants.INDEX_CLAIMED_AMT] = null;
		    	attributes[SHAConstants.INDEX_ROLE_ID] = "";
		    	attributes[SHAConstants.INDEX_ESCALATE_ROLE_ID] = 0l;
		    	attributes[SHAConstants.INDEX_ESCALATE_USER_ID] = "";
		    	attributes[SHAConstants.INDEX_PROCESS_TYPE] = "";
		    	attributes[SHAConstants.INDEX_RECONSIDERATION_FLAG] = "N";
//		    	attributes[SHAConstants.INDEX_REFERENCE_USER_ID] = "ADMINB";
		    	attributes[SHAConstants.INDEX_OUT_COME] = "REGNWFPA";
		    	DATE currentDate = new DATE();
		    	attributes[SHAConstants.INDEX_PROECSSED_DATE] = currentDate;
		    	attributes[SHAConstants.INDEX_USER_ID] = "ADMINB";
		    	
		    	attributes[SHAConstants.INDEX_PROCESS] = "";					
				attributes[SHAConstants.INDEX_REGISTRATION_DATE] = currentDate;			    	
		    	attributes[SHAConstants.INDEX_FVR_KEY] = 0;
		    	attributes[SHAConstants.INDEX_FVR_CPU_CODE] = "";
		    	attributes[SHAConstants.INDEX_FVR_NUMBER] = "";
		    	attributes[SHAConstants.INDEX_ROD_KEY] = 0;
		    	attributes[SHAConstants.INDEX_ROD_NUMBER] = "";
//		    	Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		    	attributes[SHAConstants.INDEX_ROD_CREATED_DATE] = null;
		    	attributes[SHAConstants.INDEX_REIMB_REQ_BY] = "";
		    	attributes[SHAConstants.INDEX_ROD_REFER_BY] = "";
		    	attributes[SHAConstants.INDEX_ACK_KEY] = 0;
		    	attributes[SHAConstants.INDEX_ACK_NUMBER] = "";
		    	attributes[SHAConstants.INDEX_PREAUTH_STATUS] = "";
		    	attributes[SHAConstants.INDEX_BILL_AVAILABLE] = "";
		    	attributes[SHAConstants.INDEX_HOSPITALIZATION] = "";
		    	attributes[SHAConstants.INDEX_POST_HOSPITALIZATION] = "";
		    	attributes[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = "";
		    	attributes[SHAConstants.INDEX_PRE_HOSPITALIZATION] = "";
		    	attributes[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = "";
		    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = "";
		    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = "";
		    	attributes[SHAConstants.INDEX_RRC_REQUEST_KEY] = 0;
		    	attributes[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = "";
		    	attributes[SHAConstants.INDEX_RRC_REQUEST_TYPE] = "";
		    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY_TYPE] = 0;
		    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY] = 0;
		    	attributes[SHAConstants.INDEX_PED_KEY] = 0;
		    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ROLE] = "";
		    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ID] = 0;
		    	attributes[SHAConstants.INDEX_PED_REFERRED_BY] = "";
		    	attributes[SHAConstants.INDEX_QUERY_KEY] = 0;
		    	attributes[SHAConstants.INDEX_QUERY_DISAPPROVED] = "";
		    	attributes[SHAConstants.INDEX_QUERY_REQUESTED_BY] = "";
		    	attributes[SHAConstants.INDEX_INVESTIGATION_KEY] = 0;
		    	attributes[SHAConstants.INDEX_INV_DISAPPROVED] = "";
		    	attributes[SHAConstants.INDEX_INV_REQUEST] = "";
		    	attributes[SHAConstants.INDEX_INV_REQUESTED_BY] = "";
		    	attributes[SHAConstants.INDEX_REMINDER_CATEGORY] = "";
		    	attributes[SHAConstants.INDEX_HEALTH_CARD_NUMBER] = "";
		    	attributes[SHAConstants.INDEX_REMINDER_TYPE] = "";	 
		    	attributes[SHAConstants.INDEX_PED_TYPE] = "";
		    	attributes[SHAConstants.INDEX_PAYMENT_CANCELLATION] = "";
		    	attributes[SHAConstants.INDEX_DOCUMENT_RECEVIED_FROM] = "";
		    	attributes[SHAConstants.INDEX_ZONAL_BYPASS] = "";
		    	attributes[SHAConstants.INDEX_REMINDER_LATTER_FLAG] = "";
		    	attributes[SHAConstants.INDEX_ALLOCATED_USER] = "";
		    	attributes[SHAConstants.INDEX_ALLOCATED_DATE] = null;
		    	attributes[SHAConstants.INDEX_PED_INITIATED_DATE] = null;
		    	
		    	resultArray[0] = attributes;

		    	return resultArray;
		    }

		   public static Map <String,Object> getRevisedObjectFromCursorObj(ResultSet rs) {
				
				Map <String,Object> objectMap = new WeakHashMap<String,Object>();
				
				try {
					
					
					objectMap.put(SHAConstants.WK_KEY, (Long)rs.getLong(SHAConstants.WK_KEY));
					objectMap.put(SHAConstants.INTIMATION_NO, (String) rs.getString(SHAConstants.INTIMATION_NO));
					objectMap.put(SHAConstants.INTIMATION_KEY, (Long) rs.getLong(SHAConstants.INTIMATION_KEY));
					objectMap.put(SHAConstants.INT_SOURCE, (String) rs.getString(SHAConstants.INT_SOURCE));
					objectMap.put(SHAConstants.CPU_CODE, (String) rs.getString(SHAConstants.CPU_CODE));
					objectMap.put(SHAConstants.CLAIM_NUMBER, (String) rs.getString(SHAConstants.DB_CLAIM_NUMBER));
					objectMap.put(SHAConstants.DB_CLAIM_KEY, (Long) rs.getLong(SHAConstants.DB_CLAIM_KEY));
					objectMap.put(SHAConstants.CLAIM_TYPE, (String) rs.getString(SHAConstants.CLAIM_TYPE));
					objectMap.put(SHAConstants.BAL_SI_FLAG, (String) rs.getString(SHAConstants.BAL_SI_FLAG));
					objectMap.put(SHAConstants.ADMISSION_DATE, (String) rs.getString(SHAConstants.ADMISSION_DATE));
					objectMap.put(SHAConstants.INTIMATION_DATE, (String) rs.getString(SHAConstants.INTIMATION_DATE));
					objectMap.put(SHAConstants.POLICY_KEY, (Long) rs.getLong(SHAConstants.DB_POLICY_KEY));
					objectMap.put(SHAConstants.POLICY_NUMBER, (String) rs.getString(SHAConstants.POLICY_NUMBER));
					objectMap.put(SHAConstants.PRODUCT_KEY, (Long) rs.getLong(SHAConstants.PRODUCT_KEY));
					objectMap.put(SHAConstants.PRODUCT_NAME, (String) rs.getString(SHAConstants.PRODUCT_NAME));
					objectMap.put(SHAConstants.LOB, (String) rs.getString(SHAConstants.LOB));
					objectMap.put(SHAConstants.LOB_TYPE, (String) rs.getString(SHAConstants.LOB_TYPE));
					objectMap.put(SHAConstants.HOSPITAL_TYPE, (String) rs.getString(SHAConstants.HOSPITAL_TYPE));
					objectMap.put(SHAConstants.NETWORK_TYPE, (String) rs.getString(SHAConstants.NETWORK_TYPE));
					objectMap.put(SHAConstants.HOSPITAL_KEY, (Long) rs.getLong(SHAConstants.HOSPITAL_KEY));
					objectMap.put(SHAConstants.TREATEMENT_TYPE, (String) rs.getString(SHAConstants.TREATEMENT_TYPE));
					objectMap.put(SHAConstants.SPECIALITY_NAME, (String) rs.getString(SHAConstants.SPECIALITY_NAME));
					objectMap.put(SHAConstants.PRIORITY, (String) rs.getString(SHAConstants.PRIORITY));
					objectMap.put(SHAConstants.RECORD_TYPE, (String) rs.getString(SHAConstants.RECORD_TYPE));
					objectMap.put(SHAConstants.STAGE_SOURCE, (String) rs.getString(SHAConstants.STAGE_SOURCE));
					objectMap.put(SHAConstants.CASHLESS_NO, (String) rs.getString(SHAConstants.CASHLESS_NO));
					objectMap.put(SHAConstants.CASHLESS_KEY,(Long) rs.getLong(SHAConstants.CASHLESS_KEY));
					objectMap.put(SHAConstants.CLAIMED_AMOUNT, (String) rs.getString(SHAConstants.CLAIMED_AMOUNT));
					objectMap.put(SHAConstants.ROLE_ID,(String) rs.getString(SHAConstants.ROLE_ID));
					objectMap.put(SHAConstants.ESCALATE_ROLE_ID,(Long)rs.getLong(SHAConstants.ESCALATE_ROLE_ID));
					objectMap.put(SHAConstants.ESCALATE_USER_ID,(String) rs.getString(SHAConstants.ESCALATE_USER_ID));
					objectMap.put(SHAConstants.PROCESS_TYPE,(String)rs.getString(SHAConstants.PROCESS_TYPE));
					objectMap.put(SHAConstants.RECONSIDER_FLAG, (String) rs.getString(SHAConstants.RECONSIDER_FLAG));
					objectMap.put(SHAConstants.REFERENCE_USER_ID, (String) rs.getString(SHAConstants.REFERENCE_USER_ID));
					objectMap.put(SHAConstants.CURRENT_Q, (String) rs.getString(SHAConstants.CURRENT_Q));
					objectMap.put(SHAConstants.OUTCOME, (String) rs.getString(SHAConstants.OUTCOME));
					objectMap.put(SHAConstants.PAYLOAD_PROCESS,(String) rs.getString(SHAConstants.PAYLOAD_PROCESS));
					objectMap.put(SHAConstants.PAYLOAD_REGISTRATION_DATE, (String) rs.getString(SHAConstants.PAYLOAD_REGISTRATION_DATE));
					objectMap.put(SHAConstants.PAYLOAD_FVR_CPU_CODE, (String) rs.getString(SHAConstants.PAYLOAD_FVR_CPU_CODE));
					objectMap.put(SHAConstants.FVR_KEY, (String) rs.getString(SHAConstants.FVR_KEY));
					objectMap.put(SHAConstants.PAYLOAD_FVR_NUMBER, (String) rs.getString(SHAConstants.PAYLOAD_FVR_NUMBER));
					objectMap.put(SHAConstants.PAYLOAD_ROD_KEY, (Long) rs.getLong(SHAConstants.PAYLOAD_ROD_KEY));
					objectMap.put(SHAConstants.PAYLOAD_ROD_NUMBER, (String) rs.getString(SHAConstants.PAYLOAD_ROD_NUMBER));
					objectMap.put(SHAConstants.PAYLOAD_ROD_CREATED_DATE, (String) rs.getString(SHAConstants.PAYLOAD_ROD_CREATED_DATE));
					objectMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, (String) rs.getString(SHAConstants.PAYLOAD_REIMB_REQ_BY));
					objectMap.put(SHAConstants.PAYLOAD_ROD_REFER_BY, (String) rs.getString(SHAConstants.PAYLOAD_ROD_REFER_BY));
					objectMap.put(SHAConstants.PAYLOAD_ACK_KEY, (Long) rs.getLong(SHAConstants.PAYLOAD_ACK_KEY));
					objectMap.put(SHAConstants.PAYLOAD_ACK_NUMBER, (String) rs.getString(SHAConstants.PAYLOAD_ACK_NUMBER));
					objectMap.put(SHAConstants.PAYLOAD_PREAUTH_STATUS, (String) rs.getString(SHAConstants.PAYLOAD_PREAUTH_STATUS));
					objectMap.put(SHAConstants.PAYLOAD_BILL_AVAILABLE, (String) rs.getString(SHAConstants.PAYLOAD_BILL_AVAILABLE));
					objectMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, (String) rs.getString(SHAConstants.PAYLOAD_HOSPITALIZATION));
					objectMap.put(SHAConstants.PAYLOAD_POST_HOSPITALIZATION, (String) rs.getString(SHAConstants.PAYLOAD_POST_HOSPITALIZATION));
					objectMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, (String) rs.getString(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION));
					objectMap.put(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION, (String) rs.getString(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION));
					objectMap.put(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT, (String) rs.getString(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT));
					objectMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE, (String) rs.getString(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE));
					objectMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH, (String) rs.getString(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH));
					objectMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_KEY, (String) rs.getString(SHAConstants.PAYLOAD_RRC_REQUEST_KEY));
					objectMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER, (String) rs.getString(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER));
					objectMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, (String) rs.getString(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE));
					objectMap.put(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE, (String) rs.getString(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE));
					objectMap.put(SHAConstants.PAYLOAD_RRC_ELIGIBILITY, (String) rs.getString(SHAConstants.PAYLOAD_RRC_ELIGIBILITY));
					objectMap.put(SHAConstants.PAYLOAD_PED_KEY, (Long) rs.getLong((SHAConstants.PAYLOAD_PED_KEY)));
					objectMap.put(SHAConstants.PAYLOAD_PED_TYPE, (String) rs.getString((SHAConstants.PAYLOAD_PED_TYPE)));
					objectMap.put(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE, (String) rs.getString(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE));
					objectMap.put(SHAConstants.PAYLOAD_PED_REQUESTOR_ID, (String) rs.getString(SHAConstants.PAYLOAD_PED_REQUESTOR_ID));
					objectMap.put(SHAConstants.PAYLOAD_PED_REFERRED_BY, (String) rs.getString(SHAConstants.PAYLOAD_PED_REFERRED_BY));
					objectMap.put(SHAConstants.PAYLOAD_QUERY_KEY, (Long) rs.getLong(SHAConstants.PAYLOAD_QUERY_KEY));
					objectMap.put(SHAConstants.PAYLOAD_QUERY_DISAPPROVED, (String) rs.getString(SHAConstants.PAYLOAD_QUERY_DISAPPROVED));
					objectMap.put(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY, (String) rs.getString(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY));
					objectMap.put(SHAConstants.PAYLOAD_INVESTIGATION_KEY, (Long) rs.getLong(SHAConstants.PAYLOAD_INVESTIGATION_KEY));
					objectMap.put(SHAConstants.PAYLOAD_INV_DISAPPROVED, (String) rs.getString(SHAConstants.PAYLOAD_INV_DISAPPROVED));
					objectMap.put(SHAConstants.PAYLOAD_INV_REQUEST, (String) rs.getString(SHAConstants.PAYLOAD_INV_REQUEST));
					objectMap.put(SHAConstants.PAYLOAD_INV_REQUESTED_BY, (String) rs.getString(SHAConstants.PAYLOAD_INV_REQUESTED_BY));
					objectMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION, (String) rs.getString(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION));
					objectMap.put(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM, (String) rs.getString(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM));
					objectMap.put(SHAConstants.PAYLOAD_ZONAL_BY_PASS, (String) rs.getString(SHAConstants.PAYLOAD_ZONAL_BY_PASS));
					objectMap.put(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG, (String) rs.getString(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG));
					objectMap.put(SHAConstants.PAYLOAD_REFERENCE_USER_ID, (String) rs.getString(SHAConstants.PAYLOAD_REFERENCE_USER_ID));
					objectMap.put(SHAConstants.PAYLOAD_BILLCLASS_HOSP, (String) rs.getString(SHAConstants.PAYLOAD_BILLCLASS_HOSP));
					objectMap.put(SHAConstants.PAYLOAD_BILLCLASS_POSTHOSP, (String) rs.getString(SHAConstants.PAYLOAD_BILLCLASS_POSTHOSP));
					objectMap.put(SHAConstants.PAYLOAD_BILLCLASS_PREHOSP, (String) rs.getString(SHAConstants.PAYLOAD_BILLCLASS_PREHOSP));
					if(rs.getString(SHAConstants.PAYLOAD_PED_INITIATED_DATE) != null){
						objectMap.put(SHAConstants.PAYLOAD_PED_INITIATED_DATE, (String) rs.getString(SHAConstants.PAYLOAD_PED_INITIATED_DATE));
					}
					/*if(rs.getString(SHAConstants.CPU_ADVISE_STATUS) != null){
						objectMap.put(SHAConstants.CPU_ADVISE_STATUS, (String) rs.getString(SHAConstants.CPU_ADVISE_STATUS));
					}*/
					
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return objectMap;
				
			}
		   public static Map <String,Object> getRevisedPayloadMap(Claim claim,Hospitals hospital) {
				
				Map <String,Object> objectMap = new WeakHashMap<String,Object>();

				try {
					
					
					objectMap.put(SHAConstants.WK_KEY, 0l);
					objectMap.put(SHAConstants.INTIMATION_NO, claim.getIntimation().getIntimationId());
					objectMap.put(SHAConstants.INTIMATION_KEY, claim.getIntimation().getKey());
					objectMap.put(SHAConstants.INT_SOURCE, claim.getIntimation().getIntimationSource().getValue());
					objectMap.put(SHAConstants.CPU_CODE, claim.getIntimation().getCpuCode().getCpuCode());
					objectMap.put(SHAConstants.CLAIM_NUMBER, claim.getClaimId());
					objectMap.put(SHAConstants.DB_CLAIM_KEY, claim.getKey());
					objectMap.put(SHAConstants.CLAIM_TYPE, claim.getClaimType().getValue());
					objectMap.put(SHAConstants.BAL_SI_FLAG, SHAConstants.YES_FLAG);
					objectMap.put(SHAConstants.ADMISSION_DATE, claim.getIntimation().getAdmissionDate());
					objectMap.put(SHAConstants.INTIMATION_DATE, claim.getIntimation().getCreatedDate());
					objectMap.put(SHAConstants.POLICY_KEY, claim.getIntimation().getPolicy().getKey());
					objectMap.put(SHAConstants.POLICY_NUMBER, claim.getIntimation().getPolicy().getPolicyNumber());
					objectMap.put(SHAConstants.PRODUCT_KEY, claim.getIntimation().getPolicy().getProduct().getKey());
					objectMap.put(SHAConstants.PRODUCT_NAME, claim.getIntimation().getPolicy().getProduct().getValue());
					if((ReferenceTable.HEALTH_LOB_KEY).equals(claim.getIntimation().getPolicy().getLobId())){
						objectMap.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);	
						objectMap.put(SHAConstants.LOB_TYPE, "H");
					}
			    	else if((ReferenceTable.PA_LOB_KEY).equals(claim.getIntimation().getPolicy().getLobId())){
			    		objectMap.put(SHAConstants.LOB,SHAConstants.PA_LOB);
			    		objectMap.put(SHAConstants.LOB_TYPE, "P");
			    	}
			    	objectMap.put(SHAConstants.HOSPITAL_TYPE, claim.getIntimation().getHospitalType().getValue());
					objectMap.put(SHAConstants.NETWORK_TYPE, hospital.getHospitalType().getValue());
					objectMap.put(SHAConstants.HOSPITAL_KEY, hospital.getKey());
					objectMap.put(SHAConstants.TREATEMENT_TYPE, (String) "");
					objectMap.put(SHAConstants.SPECIALITY_NAME, (String) "");
					objectMap.put(SHAConstants.PRIORITY, claim.getIsVipCustomer() != null ? (claim.getIsVipCustomer().intValue() == 1 ? SHAConstants.VIP_CUSTOMER  : SHAConstants.NORMAL) : "" );
					if(claim.getIntimation().getInsured() != null && claim.getIntimation().getInsured().getInsuredAge() != null && claim.getIntimation().getInsured().getInsuredAge().intValue()>60){
						objectMap.put(SHAConstants.PRIORITY, SHAConstants.SENIOR_CITIZEN);
			    	}
					objectMap.put(SHAConstants.RECORD_TYPE, "FRESH");
					objectMap.put(SHAConstants.STAGE_SOURCE, claim.getStage().getStageName());
					objectMap.put(SHAConstants.CASHLESS_NO, (String) "");
					objectMap.put(SHAConstants.CASHLESS_KEY,0l);
					objectMap.put(SHAConstants.CLAIMED_AMOUNT, claim.getClaimedAmount());
					objectMap.put(SHAConstants.ROLE_ID,"");
					objectMap.put(SHAConstants.ESCALATE_ROLE_ID,0l);
					objectMap.put(SHAConstants.ESCALATE_USER_ID,(String) "");
					objectMap.put(SHAConstants.PROCESS_TYPE,claim.getClaimType().getValue());
					objectMap.put(SHAConstants.RECONSIDER_FLAG, SHAConstants.N_FLAG);
					
					DATE currentDate = new DATE();
					objectMap.put(SHAConstants.PROCESSED_DATE, currentDate);
					objectMap.put(SHAConstants.USER_ID,claim.getCreatedBy());
					objectMap.put(SHAConstants.PAYLOAD_PROCESS,(String) "");

					try {
			    		if(claim.getCreatedDate() != null){
							currentDate = new DATE(claim.getCreatedDate());
							objectMap.put(SHAConstants.PAYLOAD_REGISTRATION_DATE, currentDate);
			    		}
			    		else{
			    			objectMap.put(SHAConstants.PAYLOAD_REGISTRATION_DATE, null);
			    		}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (Exception E) {
					// TODO Auto-generated catch block
					E.printStackTrace();
				}
				return objectMap;
				
		   }
		   
		   public static String getCurrentRemQueName(String curQName){
			   Map<String,String> nameMap = new HashMap<String,String>();
			   nameMap.put(SHAConstants.FIRST_REMINDER_LETTER_CURRENT_QUEUE, SHAConstants.FIRST_REMINDER);
			   nameMap.put(SHAConstants.SECOND_REMINDER_LETTER_CURRENT_QUEUE, SHAConstants.SECOND_REMINDER);
			   nameMap.put(SHAConstants.THIRD_REMINDER_LETTER_CURRENT_QUEUE, SHAConstants.CLOSE);
			   
			   return String.valueOf(nameMap.get(curQName));
		   }
		   
		   public static String getCurrentRemQueNameForPan(String curQName){
			   Map<String,String> nameMap = new HashMap<String,String>();
			   nameMap.put(SHAConstants.FIRST_REMINDER_LETTER_CURRENT_QUEUE, "PAN CARD REQUEST");
			   nameMap.put(SHAConstants.SECOND_REMINDER_LETTER_CURRENT_QUEUE, "PAN CARD - " + SHAConstants.FIRST_REMINDER);
			   nameMap.put(SHAConstants.THIRD_REMINDER_LETTER_CURRENT_QUEUE,  "PAN CARD - " + SHAConstants.SECOND_REMINDER);
			   
			   nameMap.put(SHAConstants.FIRST_REMINDER.toLowerCase(), "PAN CARD REQUEST");
			   nameMap.put(SHAConstants.SECOND_REMINDER.toLowerCase(), "PAN CARD - " + SHAConstants.FIRST_REMINDER);
			   nameMap.put(SHAConstants.CLOSE.toLowerCase(),  "PAN CARD - " + SHAConstants.SECOND_REMINDER);
			   
			   return String.valueOf(nameMap.get(curQName));
		   }
		   
		   public static String getOnlyStrings(String s) {
			   Pattern pattern = Pattern.compile("[^0-9a-zA-Z.]");
			   Matcher matcher = pattern.matcher(s);
			   String strNumber = matcher.replaceAll("");
			   return strNumber;
			 }
		   
		   public static Object[] setOMPObjArrayForGetTask(Map<String, Object> map) {
				
				Object[] resultArray = new Object[1];
				
				Object[] inputArray = new Object[40];
				inputArray[0] = map.get(SHAConstants.INTIMATION_NO) != null ?  map.get(SHAConstants.INTIMATION_NO) : "";
				inputArray[1] = map.get(SHAConstants.INT_SOURCE) != null ?  map.get(SHAConstants.INT_SOURCE) : "";
				inputArray[2] = map.get(SHAConstants.CPU_CODE) != null ?  map.get(SHAConstants.CPU_CODE) : null;
				inputArray[3] = map.get(SHAConstants.CLAIM_NUMBER) != null ?  map.get(SHAConstants.CLAIM_NUMBER) : "";
				inputArray[4] = map.get(SHAConstants.CLAIM_TYPE) != null ?  map.get(SHAConstants.CLAIM_TYPE) : "";
//				DATE currentDate = new DATE();
//				inputArray[SHAConstants.INDEX_ADMISSION_DATE] = currentDate;
				
				inputArray[5] = map.get(SHAConstants.ADMISSION_DATE) != null ?  map.get(SHAConstants.ADMISSION_DATE) : null;
				inputArray[6] = map.get(SHAConstants.INTIMATION_DATE) != null ?  map.get(SHAConstants.INTIMATION_DATE) : null;
				inputArray[7] = map.get(SHAConstants.POLICY_NUMBER) != null ?  map.get(SHAConstants.POLICY_NUMBER) : "";
				inputArray[8] = map.get(SHAConstants.PRODUCT_NAME) != null ?  map.get(SHAConstants.PRODUCT_NAME) : "";
				inputArray[9] = map.get(SHAConstants.LOB) != null ?  map.get(SHAConstants.LOB) : "";
				inputArray[10] = map.get(SHAConstants.LOB_TYPE) != null ?  map.get(SHAConstants.LOB_TYPE) : "";
				inputArray[11] = map.get(SHAConstants.HOSPITAL_TYPE) != null ?  map.get(SHAConstants.HOSPITAL_TYPE) : "";
				inputArray[12] = map.get(SHAConstants.NETWORK_TYPE) != null ?  map.get(SHAConstants.NETWORK_TYPE) : "";
				inputArray[13] = map.get(SHAConstants.TREATEMENT_TYPE) != null ?  map.get(SHAConstants.TREATEMENT_TYPE) : "";
				inputArray[14] = map.get(SHAConstants.SPECIALITY_NAME) != null ?  map.get(SHAConstants.SPECIALITY_NAME) : "";
				inputArray[15] = map.get(SHAConstants.PRIORITY) != null ?  map.get(SHAConstants.PRIORITY) : "";
				inputArray[16] = map.get(SHAConstants.RECORD_TYPE) != null ?  map.get(SHAConstants.RECORD_TYPE) : "";
				inputArray[17] = map.get(SHAConstants.STAGE_SOURCE) != null ?  map.get(SHAConstants.STAGE_SOURCE) : "";
				inputArray[18] = map.get(SHAConstants.CLM_FM_AMT) != null ?  map.get(SHAConstants.CLM_FM_AMT) : null;
				inputArray[19] = map.get(SHAConstants.CLM_TO_AMT) != null ?  map.get(SHAConstants.CLM_TO_AMT) :null;
				inputArray[20] = map.get(SHAConstants.PROCESS_TYPE) != null ?  map.get(SHAConstants.PROCESS_TYPE) : "";
				inputArray[21] = map.get(SHAConstants.CURRENT_Q) != null ?  map.get(SHAConstants.CURRENT_Q) : "WFPA";
				inputArray[22] = map.get(SHAConstants.USER_ID) != null ?  map.get(SHAConstants.USER_ID) : "ADMINB";
				
				inputArray[23] = map.get(SHAConstants.REGISTRATION_DATE) != null ?  map.get(SHAConstants.REGISTRATION_DATE) : null;
				inputArray[24] = map.get(SHAConstants.FVR_CPU_CODE) != null ?  map.get(SHAConstants.FVR_CPU_CODE) : null;
				inputArray[25] = map.get(SHAConstants.ROD_NUMBER) != null ?  map.get(SHAConstants.ROD_NUMBER) : "";
				inputArray[26] = map.get(SHAConstants.ACK_NUMBER) != null ?  map.get(SHAConstants.ACK_NUMBER) : "";
				inputArray[27] = map.get(SHAConstants.RRC_REQUEST_NUMBER) != null ?  map.get(SHAConstants.RRC_REQUEST_NUMBER) : "";
				inputArray[28] = map.get(SHAConstants.RRC_REQUEST_TYPE) != null ?  map.get(SHAConstants.RRC_REQUEST_TYPE) : "";
				inputArray[29] = map.get(SHAConstants.INVESTIGATION_KEY) != null ?  map.get(SHAConstants.INVESTIGATION_KEY) : null;
				inputArray[30] = map.get(SHAConstants.QUERY_KEY) != null ?  map.get(SHAConstants.QUERY_KEY) : null;
				inputArray[31] = map.get(SHAConstants.PED_KEY) != null ?  map.get(SHAConstants.PED_KEY) : null;
				inputArray[32] = map.get(SHAConstants.PED_TYPE) != null ?  map.get(SHAConstants.PED_TYPE) : "";
				inputArray[33] = map.get(SHAConstants.REFERENCE_USER_ID) != null ?  map.get(SHAConstants.REFERENCE_USER_ID) : null;
				inputArray[34] = map.get(SHAConstants.HOSPITALIZATION) != null ?  map.get(SHAConstants.HOSPITALIZATION) : null;
				inputArray[35] = map.get(SHAConstants.POST_HOSPITALIZATION) != null ?  map.get(SHAConstants.POST_HOSPITALIZATION) : null;
				inputArray[36] = map.get(SHAConstants.PRE_HOSPITALIZATION) != null ?  map.get(SHAConstants.PRE_HOSPITALIZATION) : null;
				inputArray[37] = map.get(SHAConstants.EVENT_CODE) != null ?  map.get(SHAConstants.EVENT_CODE) : "";	
				inputArray[38] = map.get(SHAConstants.LOSS_DATE) != null ?  map.get(SHAConstants.LOSS_DATE) : null;	
				inputArray[39] = map.get(SHAConstants.CLASSIFICATION) != null ?  map.get(SHAConstants.CLASSIFICATION) : "";	
				
				resultArray[0]= inputArray;
				
				return resultArray;
				
			}
		   
		   public static Object[] getArrayListForOMPIntimationSubmit(OMPIntimation intimation,Hospitals hospital){
		    	Object[] resultArray = new Object[1];
		    	
		    	Object[] attributes = new Object[89];
		    	/*System.out.println("Work flow key 0");
		    	System.out.println("1 :"+intimation.getIntimationId());
		    	System.out.println("2 :"+intimation.getKey());
		    	System.out.println("3 :"+new java.sql.Date(intimation.getAdmissionDate().getTime()));
		    	System.out.println("4 :"+new java.sql.Date(intimation.getCreatedDate().getTime()));
		    	System.out.println("5 :"+new java.sql.Date(intimation.getCreatedDate().getTime()));
		    	System.out.println("6 :"+intimation.getPolicy().getKey());
		    	System.out.println("7 :"+intimation.getPolicy().getPolicyNumber());
		    	System.out.println("8 :"+intimation.getStage().getStageName());	
		    	System.out.println("9 Processed Date :"+new java.sql.Date(Calendar.getInstance().getTime().getTime()));		 
		    	System.out.println("10 Event Code :"+intimation.getEvent().getEventCode());
		    	System.out.println("11 Aliment Loss :"+intimation.getAilmentLoss());*/
//		    	System.out.println("12 Loss Date :"+new java.sql.Date(intimation.getLossDateTime().getTime()));
		    	
		    	
		    	attributes[SHAConstants.INDEX_WORK_FLOW_KEY] = 0;
		    	attributes[SHAConstants.INDEX_INTIMATION_NO] = intimation.getIntimationId();
		    	attributes[SHAConstants.INDEX_INTIMATION_KEY] = intimation.getKey();
		    	attributes[SHAConstants.INDEX_INTIMATION_SOURCE] = null;//intimation.getIntimationSource().getValue();
		    	attributes[SHAConstants.INDEX_CPU_CODE] =null;//intimation.getCpuCode().getCpuCode();
		    	attributes[SHAConstants.INDEX_CLAIM_NUMBER] ="";
		    	attributes[SHAConstants.INDEX_CLAIM_KEY] =0;
		    	attributes[SHAConstants.INDEX_CLAIM_TYPE] = "";
		    	attributes[SHAConstants.INDEX_BALANCE_SI_FLAG] = "Y";
		    	if(intimation.getAdmissionDate()!=null){
		    		attributes[SHAConstants.INDEX_ADMISSION_DATE] = new java.sql.Date(intimation.getAdmissionDate().getTime());
		    	}
				attributes[SHAConstants.INDEX_INTIMATION_DATE] = new java.sql.Date(intimation.getCreatedDate().getTime());
		    	attributes[SHAConstants.INDEX_POLICY_KEY] =intimation.getPolicy().getKey();
		    	attributes[SHAConstants.INDEX_POLICY_NUMBER] = intimation.getPolicy().getPolicyNumber();
		    	attributes[SHAConstants.INDEX_PRODUCT_KEY] = null;//intimation.getPolicy().getProduct().getKey();
		    	attributes[SHAConstants.INDEX_PRODUCT_NAME] = null;//intimation.getPolicy().getProduct().getValue();
		    	attributes[SHAConstants.INDEX_LOB] = "HEALTH";
		    	attributes[SHAConstants.INDEX_LOB_TYPE] = "H";
		    	attributes[SHAConstants.INDEX_HOSPITAL_TYPE] =null;//intimation.getHospitalType().getValue();
		    	attributes[SHAConstants.INDEX_NETWORK_TYPE] = null;//hospital.getNetworkHospitalType();
		    	attributes[SHAConstants.INDEX_HOSPITAL_KEY] = null;//hospital.getKey();
		    	attributes[SHAConstants.INDEX_TREATMENT_TYPE] = "";
		    	attributes[SHAConstants.INDEX_SPECIALITY_NAME] = "";
		    	attributes[SHAConstants.INDEX_PRIORITY] = "";
		    	attributes[SHAConstants.INDEX_RECORD_TYPE] = "FRESH";
		    	attributes[SHAConstants.INDEX_STAGE_SOURCE] =  intimation.getStage().getStageName();
		    	attributes[SHAConstants.INDEX_CASHLESS_NO] = "";
		    	attributes[SHAConstants.INDEX_CASHLESS_KEY] = 0;
		    	attributes[SHAConstants.INDEX_CLAIMED_AMT] = null;
		    	attributes[SHAConstants.INDEX_ROLE_ID] = "";
		    	attributes[SHAConstants.INDEX_ESCALATE_ROLE_ID] = 0;
		    	attributes[SHAConstants.INDEX_ESCALATE_USER_ID] = "";
		    	attributes[SHAConstants.INDEX_PROCESS_TYPE] = "";
		    	attributes[SHAConstants.INDEX_RECONSIDERATION_FLAG] = "N";
		    	attributes[SHAConstants.INDEX_OUT_COME] = "";
		    	//DATE currentDate = new DATE();
		    	
		    	attributes[SHAConstants.INDEX_PROECSSED_DATE] = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		    	attributes[SHAConstants.INDEX_USER_ID] = "ADMINB";
		    	
		    	attributes[SHAConstants.INDEX_CLASSIFICATION] = null;
		    	
		    	attributes[SHAConstants.INDEX_SUB_CLASSIFICATION] = null;
		    	attributes[SHAConstants.INDEX_EVENT_CODE] = intimation.getEvent().getEventCode();
		    	attributes[SHAConstants.INDEX_AILMENT_LOSS] = intimation.getAilmentLoss();
		    	java.sql.Date lossDate = null;
				if(intimation.getLossDateTime() != null){
					lossDate = new java.sql.Date(intimation.getLossDateTime().getTime());
				}
		    	attributes[SHAConstants.INDEX_LOSS_DATE] = lossDate;
		    	resultArray[0] = attributes;
		    	return resultArray;
		    }
		
		   public static BeanItemContainer<SelectValue> getOMPSelectValueForStatus(){
				BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
				/*SelectValue all = new SelectValue();
				all.setValue(SHAConstants.ALL);
				
				SelectValue saved = new SelectValue();
				saved.setValue(ReferenceTable.INTIMATION_SAVE_STATUS_KEY);
				saved.setValue(SHAConstants.SAVE);*/
				
				SelectValue submitted = new SelectValue();
				submitted.setId(ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
				submitted.setValue(SHAConstants.OMP_SUBMITTED);
				
				//container.addBean(all);
				//container.addBean(saved);	
				container.addBean(submitted);
				
				return container;
			}
		
		   public static BeanItemContainer<SelectValue> getOMPSelectValueForType(){
				BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
				
				SelectValue searchIntimation = new SelectValue();
				searchIntimation.setValue(SHAConstants.SEARCH_INTIMATION);
				
				SelectValue createIntimation = new SelectValue();
				createIntimation.setValue(SHAConstants.CREATE_INTIMATION);

				container.addBean(createIntimation);
				container.addBean(searchIntimation);
				
				return container;
			}

		   public static Map <String,Object> getOMPObjectFromCursorObj(ResultSet rs) {
				
				Map <String,Object> objectMap = new WeakHashMap<String,Object>();
				
				try {
					
					
					objectMap.put(SHAConstants.WK_KEY, (Long)rs.getLong(SHAConstants.WK_KEY));
					objectMap.put(SHAConstants.INTIMATION_NO, (String) rs.getString(SHAConstants.INTIMATION_NO));
					objectMap.put(SHAConstants.INTIMATION_KEY, (Long) rs.getLong(SHAConstants.INTIMATION_KEY));
					objectMap.put(SHAConstants.INT_SOURCE, (String) rs.getString(SHAConstants.INT_SOURCE));
					objectMap.put(SHAConstants.CPU_CODE, (String) rs.getString(SHAConstants.CPU_CODE));
					objectMap.put(SHAConstants.CLAIM_NUMBER, (String) rs.getString(SHAConstants.DB_CLAIM_NUMBER));
					objectMap.put(SHAConstants.DB_CLAIM_KEY, (Long) rs.getLong(SHAConstants.DB_CLAIM_KEY));
					objectMap.put(SHAConstants.CLAIM_TYPE, (String) rs.getString(SHAConstants.CLAIM_TYPE));
					objectMap.put(SHAConstants.BAL_SI_FLAG, (String) rs.getString(SHAConstants.BAL_SI_FLAG));
					
					objectMap.put(SHAConstants.ADMISSION_DATE, (String) rs.getString(SHAConstants.ADMISSION_DATE));
					objectMap.put(SHAConstants.INTIMATION_DATE, (String) rs.getString(SHAConstants.INTIMATION_DATE));
					objectMap.put(SHAConstants.POLICY_KEY, (Long) rs.getLong(SHAConstants.DB_POLICY_KEY));
					objectMap.put(SHAConstants.POLICY_NUMBER, (String) rs.getString(SHAConstants.POLICY_NUMBER));
					objectMap.put(SHAConstants.PRODUCT_KEY, (Long) rs.getLong(SHAConstants.PRODUCT_KEY));
					objectMap.put(SHAConstants.PRODUCT_NAME, (String) rs.getString(SHAConstants.PRODUCT_NAME));
					objectMap.put(SHAConstants.LOB, (String) rs.getString(SHAConstants.LOB));
					objectMap.put(SHAConstants.LOB_TYPE, (String) rs.getString(SHAConstants.LOB_TYPE));
					objectMap.put(SHAConstants.HOSPITAL_TYPE, (String) rs.getString(SHAConstants.HOSPITAL_TYPE));
					objectMap.put(SHAConstants.NETWORK_TYPE, (String) rs.getString(SHAConstants.NETWORK_TYPE));
					objectMap.put(SHAConstants.HOSPITAL_KEY, (Long) rs.getLong(SHAConstants.HOSPITAL_KEY));
					objectMap.put(SHAConstants.TREATEMENT_TYPE, (String) rs.getString(SHAConstants.TREATEMENT_TYPE));
					objectMap.put(SHAConstants.SPECIALITY_NAME, (String) rs.getString(SHAConstants.SPECIALITY_NAME));
					objectMap.put(SHAConstants.PRIORITY, (String) rs.getString(SHAConstants.PRIORITY));
					objectMap.put(SHAConstants.RECORD_TYPE, (String) rs.getString(SHAConstants.RECORD_TYPE));
					objectMap.put(SHAConstants.STAGE_SOURCE, (String) rs.getString(SHAConstants.STAGE_SOURCE));
					objectMap.put(SHAConstants.CASHLESS_NO, (String) rs.getString(SHAConstants.CASHLESS_NO));
					objectMap.put(SHAConstants.CASHLESS_KEY,(Long) rs.getLong(SHAConstants.CASHLESS_KEY));
					objectMap.put(SHAConstants.CLAIMED_AMOUNT, (String) rs.getString(SHAConstants.CLAIMED_AMOUNT));
					objectMap.put(SHAConstants.ROLE_ID,(String) rs.getString(SHAConstants.ROLE_ID));
					objectMap.put(SHAConstants.ESCALATE_ROLE_ID,(Long)rs.getLong(SHAConstants.ESCALATE_ROLE_ID));
					objectMap.put(SHAConstants.ESCALATE_USER_ID,(String) rs.getString(SHAConstants.ESCALATE_USER_ID));
					objectMap.put(SHAConstants.PROCESS_TYPE,(String)rs.getString(SHAConstants.PROCESS_TYPE));
					objectMap.put(SHAConstants.RECONSIDER_FLAG, (String) rs.getString(SHAConstants.RECONSIDER_FLAG));
					objectMap.put(SHAConstants.REFERENCE_USER_ID, (String) rs.getString(SHAConstants.REFERENCE_USER_ID));
					objectMap.put(SHAConstants.CURRENT_Q, (String) rs.getString(SHAConstants.CURRENT_Q));
					objectMap.put(SHAConstants.OUTCOME, (String) rs.getString(SHAConstants.OUTCOME));
					objectMap.put(SHAConstants.ALLOCATED_USER,(String) rs.getString(SHAConstants.ALLOCATED_USER));
					objectMap.put(SHAConstants.ALLOCATED_DATE, (String) rs.getString(SHAConstants.ALLOCATED_DATE));
					
					objectMap.put(SHAConstants.PROCESS, (String) rs.getString(SHAConstants.PROCESS));
					objectMap.put(SHAConstants.REGISTRATION_DATE, (String) rs.getString(SHAConstants.REGISTRATION_DATE));
					objectMap.put(SHAConstants.FVR_KEY, (Long) rs.getLong(SHAConstants.FVR_KEY));
					objectMap.put(SHAConstants.FVR_CPU_CODE, (String) rs.getString(SHAConstants.FVR_CPU_CODE));
					objectMap.put(SHAConstants.FVR_NUMBER, (String) rs.getString(SHAConstants.FVR_NUMBER));
					objectMap.put(SHAConstants.ROD_KEY, (Long) rs.getLong(SHAConstants.ROD_KEY));
					objectMap.put(SHAConstants.PAYLOAD_ROD_NUMBER, (String) rs.getString(SHAConstants.PAYLOAD_ROD_NUMBER));
					objectMap.put(SHAConstants.ROD_CREATED_DATE, (String) rs.getString(SHAConstants.ROD_CREATED_DATE));
					objectMap.put(SHAConstants.REIMB_REQ_BY, (String) rs.getString(SHAConstants.REIMB_REQ_BY));
					objectMap.put(SHAConstants.ROD_REFER_BY, (String) rs.getString(SHAConstants.ROD_REFER_BY));
					objectMap.put(SHAConstants.ACK_KEY, (Long) rs.getLong(SHAConstants.ACK_KEY));
					objectMap.put(SHAConstants.ACK_NUMBER, (String) rs.getString(SHAConstants.ACK_NUMBER));
					objectMap.put(SHAConstants.PREAUTH_STATUS, (String) rs.getString(SHAConstants.PREAUTH_STATUS));
					objectMap.put(SHAConstants.BILL_AVAILABLE, (String) rs.getString(SHAConstants.BILL_AVAILABLE));
					objectMap.put(SHAConstants.HOSPITALIZATION, (String) rs.getString(SHAConstants.HOSPITALIZATION));
					objectMap.put(SHAConstants.PAYLOAD_POST_HOSPITALIZATION, (String) rs.getString(SHAConstants.PAYLOAD_POST_HOSPITALIZATION));
					objectMap.put(SHAConstants.PARTIAL_HOSPITALIZATION, (String) rs.getString(SHAConstants.PARTIAL_HOSPITALIZATION));
					objectMap.put(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION, (String) rs.getString(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION));
					objectMap.put(SHAConstants.LUMP_SUM_AMOUNT, (String) rs.getString(SHAConstants.LUMP_SUM_AMOUNT));
					objectMap.put(SHAConstants.ADDON_BENEFITS_PATIENT_CARE, (String) rs.getString(SHAConstants.ADDON_BENEFITS_PATIENT_CARE));
					objectMap.put(SHAConstants.ADDON_BENEFITS_HOSP_CASH, (String) rs.getString(SHAConstants.ADDON_BENEFITS_HOSP_CASH));
					objectMap.put(SHAConstants.RRC_REQUEST_KEY, (Long) rs.getLong(SHAConstants.RRC_REQUEST_KEY));
					objectMap.put(SHAConstants.RRC_REQUEST_NUMBER, (String) rs.getString(SHAConstants.RRC_REQUEST_NUMBER));
					objectMap.put(SHAConstants.RRC_REQUEST_TYPE, (String) rs.getString(SHAConstants.RRC_REQUEST_TYPE));
					objectMap.put(SHAConstants.RRC_ELIGIBILITY_TYPE, (String) rs.getString(SHAConstants.RRC_ELIGIBILITY_TYPE));
					objectMap.put(SHAConstants.RRC_ELIGIBILITY, (String) rs.getString(SHAConstants.RRC_ELIGIBILITY));
					objectMap.put(SHAConstants.PED_KEY, (Long) rs.getLong(SHAConstants.PED_KEY));
					objectMap.put(SHAConstants.PED_REQUESTOR_ROLE, (String) rs.getString(SHAConstants.PED_REQUESTOR_ROLE));
					objectMap.put(SHAConstants.PED_REQUESTOR_ID, (String) rs.getString(SHAConstants.PED_REQUESTOR_ID));
					objectMap.put(SHAConstants.PED_REFERRED_BY, (String) rs.getString(SHAConstants.PED_REFERRED_BY));
					objectMap.put(SHAConstants.QUERY_KEY, (Long) rs.getLong(SHAConstants.QUERY_KEY));
					objectMap.put(SHAConstants.QUERY_DISAPPROVED, (String) rs.getString(SHAConstants.QUERY_DISAPPROVED));
					objectMap.put(SHAConstants.QUERY_REQUESTED_BY, (String) rs.getString(SHAConstants.QUERY_REQUESTED_BY));
					objectMap.put(SHAConstants.INVESTIGATION_KEY, (Long) rs.getLong(SHAConstants.INVESTIGATION_KEY));
					objectMap.put(SHAConstants.INV_DISAPPROVED, (String) rs.getString(SHAConstants.INV_DISAPPROVED));
					objectMap.put(SHAConstants.INV_REQUEST, (String) rs.getString(SHAConstants.INV_REQUEST));
					objectMap.put(SHAConstants.INV_REQUESTED_BY, (String) rs.getString(SHAConstants.INV_REQUESTED_BY));
					objectMap.put(SHAConstants.PED_TYPE, (String) rs.getString(SHAConstants.PED_TYPE));
					objectMap.put(SHAConstants.PAYMENT_CANCELLATION, (String) rs.getString(SHAConstants.PAYMENT_CANCELLATION));
					objectMap.put(SHAConstants.DOCUMENT_RECEVIED_FROM, (String) rs.getString(SHAConstants.DOCUMENT_RECEVIED_FROM));
					objectMap.put(SHAConstants.ZONAL_BY_PASS, (String) rs.getString(SHAConstants.ZONAL_BY_PASS));
					objectMap.put(SHAConstants.REMINDER_LATTER_FLAG, (String) rs.getString(SHAConstants.REMINDER_LATTER_FLAG));
					objectMap.put(SHAConstants.EVENT_CODE, (String) rs.getString(SHAConstants.EVENT_CODE));
					objectMap.put(SHAConstants.LOSS_DATE, (String) rs.getString(SHAConstants.LOSS_DATE));
					objectMap.put(SHAConstants.CLASSIFICATION , (String) rs.getString(SHAConstants.CLASSIFICATION));
								
													
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return objectMap;
				
			}

		   public static Object[] getOMPRevisedObjArrayForSubmit(Map<String,Object> map){			   
			   
			   Object[] resultArray = new Object[1];
		    	
		    	Object[] attributes = new Object[89];
		    	attributes[SHAConstants.INDEX_WORK_FLOW_KEY] = map.get(SHAConstants.WK_KEY);
		    	attributes[SHAConstants.INDEX_INTIMATION_NO] = map.get(SHAConstants.INTIMATION_NO);
		    	attributes[SHAConstants.INDEX_INTIMATION_KEY] = map.get(SHAConstants.INTIMATION_KEY);
		    	attributes[SHAConstants.INDEX_INTIMATION_SOURCE] = map.get(SHAConstants.INT_SOURCE);
		    	attributes[SHAConstants.INDEX_CPU_CODE] =map.get(SHAConstants.CPU_CODE);
		    	attributes[SHAConstants.INDEX_CLAIM_NUMBER] = map.get(SHAConstants.DB_CLAIM_NUMBER);
		    	attributes[SHAConstants.INDEX_CLAIM_KEY] = map.get(SHAConstants.DB_CLAIM_KEY);
		    	attributes[SHAConstants.INDEX_CLAIM_TYPE] =map.get(SHAConstants.CLAIM_TYPE);
		    	attributes[SHAConstants.INDEX_BALANCE_SI_FLAG] =map.get(SHAConstants.BAL_SI_FLAG);
		    	attributes[SHAConstants.INDEX_ADMISSION_DATE] = map.get(SHAConstants.ADMISSION_DATE);
		    	attributes[SHAConstants.INDEX_INTIMATION_DATE] =map.get(SHAConstants.INTIMATION_DATE);
		    	attributes[SHAConstants.INDEX_POLICY_KEY] =map.get(SHAConstants.POLICY_KEY);
		    	attributes[SHAConstants.INDEX_POLICY_NUMBER] = map.get(SHAConstants.POLICY_NUMBER);
		    	attributes[SHAConstants.INDEX_PRODUCT_KEY] = map.get(SHAConstants.PRODUCT_KEY);
		    	attributes[SHAConstants.INDEX_PRODUCT_NAME] = map.get(SHAConstants.PRODUCT_NAME);
		    	attributes[SHAConstants.INDEX_LOB] =map.get(SHAConstants.LOB);
		    	attributes[SHAConstants.INDEX_LOB_TYPE] =map.get(SHAConstants.LOB_TYPE);
		    	attributes[SHAConstants.INDEX_HOSPITAL_TYPE] = map.get(SHAConstants.HOSPITAL_TYPE);
		    	attributes[SHAConstants.INDEX_NETWORK_TYPE] = map.get(SHAConstants.NETWORK_TYPE);
		    	attributes[SHAConstants.INDEX_HOSPITAL_KEY] = map.get(SHAConstants.HOSPITAL_KEY);
		    	attributes[SHAConstants.INDEX_TREATMENT_TYPE] = map.get(SHAConstants.TREATEMENT_TYPE);
		    	attributes[SHAConstants.INDEX_SPECIALITY_NAME] =map.get(SHAConstants.SPECIALITY_NAME);
		    	attributes[SHAConstants.INDEX_PRIORITY] = map.get(SHAConstants.PRIORITY);
		    	attributes[SHAConstants.INDEX_RECORD_TYPE] = map.get(SHAConstants.RECORD_TYPE);
		    	attributes[SHAConstants.INDEX_STAGE_SOURCE] = map.get(SHAConstants.STAGE_SOURCE);
		    	attributes[SHAConstants.INDEX_CASHLESS_NO] = map.get(SHAConstants.CASHLESS_NO);
		    	attributes[SHAConstants.INDEX_CASHLESS_KEY] =map.get(SHAConstants.CASHLESS_KEY);
		    	attributes[SHAConstants.INDEX_CLAIMED_AMT] =map.get(SHAConstants.CLAIMED_AMOUNT);
		    	attributes[SHAConstants.INDEX_ROLE_ID] = map.get(SHAConstants.ROLE_ID);
		    	attributes[SHAConstants.INDEX_ESCALATE_ROLE_ID] =map.get(SHAConstants.ESCALATE_ROLE_ID);
		    	attributes[SHAConstants.INDEX_ESCALATE_USER_ID] =map.get(SHAConstants.ESCALATE_USER_ID);
		    	attributes[SHAConstants.INDEX_PROCESS_TYPE] = map.get(SHAConstants.PROCESS_TYPE);
		    	attributes[SHAConstants.INDEX_RECONSIDERATION_FLAG] =map.get(SHAConstants.RECONSIDER_FLAG);
		    	attributes[SHAConstants.INDEX_REFERENCE_USER_ID] = map.get(SHAConstants.REFERENCE_USER_ID);
		    	attributes[SHAConstants.INDEX_OUT_COME] = map.get(SHAConstants.OUTCOME);
		    	attributes[SHAConstants.INDEX_PROECSSED_DATE] = map.get(SHAConstants.PROCESSED_DATE);
		    	attributes[SHAConstants.INDEX_USER_ID] = map.get(SHAConstants.USER_ID);
	
		    		attributes[SHAConstants.INDEX_PROCESS] = map.get(SHAConstants.PAYLOAD_PROCESS);					
					attributes[SHAConstants.INDEX_REGISTRATION_DATE] = map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) ;			    	
			    	attributes[SHAConstants.INDEX_FVR_KEY] = map.get(SHAConstants.FVR_KEY);
			    	attributes[SHAConstants.INDEX_FVR_CPU_CODE] = map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE);
			    	attributes[SHAConstants.INDEX_FVR_NUMBER] = map.get(SHAConstants.PAYLOAD_FVR_NUMBER) ;
			    	attributes[SHAConstants.INDEX_ROD_KEY] = map.get(SHAConstants.PAYLOAD_ROD_KEY) ;
			    	attributes[SHAConstants.INDEX_ROD_NUMBER] = map.get(SHAConstants.PAYLOAD_ROD_NUMBER) ;
			    	attributes[SHAConstants.INDEX_ROD_CREATED_DATE] = map.get(SHAConstants.PAYLOAD_ROD_CREATED_DATE);
			    	attributes[SHAConstants.INDEX_REIMB_REQ_BY] = map.get(SHAConstants.PAYLOAD_REIMB_REQ_BY) ;
			    	attributes[SHAConstants.INDEX_ROD_REFER_BY] = map.get(SHAConstants.PAYLOAD_ROD_REFER_BY);
			    	attributes[SHAConstants.INDEX_ACK_KEY] = map.get(SHAConstants.PAYLOAD_ACK_KEY);
			    	attributes[SHAConstants.INDEX_ACK_NUMBER] = map.get(SHAConstants.PAYLOAD_ACK_NUMBER);
			    	attributes[SHAConstants.INDEX_PREAUTH_STATUS] = map.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
			    	attributes[SHAConstants.INDEX_BILL_AVAILABLE] = map.get(SHAConstants.PAYLOAD_BILL_AVAILABLE) ;
			    	attributes[SHAConstants.INDEX_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_HOSPITALIZATION);
			    	attributes[SHAConstants.INDEX_POST_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_POST_HOSPITALIZATION);
			    	attributes[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION);
			    	attributes[SHAConstants.INDEX_PRE_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION);
			    	attributes[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = map.get(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT) ;
			    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE);
			    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH);
			    	attributes[SHAConstants.INDEX_RRC_REQUEST_KEY] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY);
			    	attributes[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER);
			    	attributes[SHAConstants.INDEX_RRC_REQUEST_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) ;
			    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE);
			    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY);
			    	attributes[SHAConstants.INDEX_PED_KEY] = map.get(SHAConstants.PAYLOAD_PED_KEY) ;
			    	attributes[SHAConstants.INDEX_PED_TYPE] = map.get(SHAConstants.PAYLOAD_PED_TYPE);
			    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ROLE] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE);
			    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ID] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ID) ;
			    	attributes[SHAConstants.INDEX_PED_REFERRED_BY] = map.get(SHAConstants.PAYLOAD_PED_REFERRED_BY);
			    	attributes[SHAConstants.INDEX_QUERY_KEY] = map.get(SHAConstants.PAYLOAD_QUERY_KEY) ;
			    	attributes[SHAConstants.INDEX_QUERY_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_QUERY_DISAPPROVED) ;
			    	attributes[SHAConstants.INDEX_QUERY_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY);
			    	attributes[SHAConstants.INDEX_INVESTIGATION_KEY]  = map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY);
			    	attributes[SHAConstants.INDEX_INV_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_INV_DISAPPROVED);
			    	attributes[SHAConstants.INDEX_INV_REQUEST] = map.get(SHAConstants.PAYLOAD_INV_REQUEST) ;
			    	attributes[SHAConstants.INDEX_INV_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_INV_REQUESTED_BY) ;
			    	attributes[SHAConstants.INDEX_REMINDER_CATEGORY] = map.get(SHAConstants.PAYLOAD_REMINDER_CATEGORY);
			    	attributes[SHAConstants.INDEX_HEALTH_CARD_NUMBER] = map.get(SHAConstants.PAYLOAD_HEALTH_CARD_NUMBER);
			    	attributes[SHAConstants.INDEX_REMINDER_TYPE] = map.get(SHAConstants.PAYLOAD_REMINDER_TYPE);
			    	attributes[SHAConstants.INDEX_PAYMENT_CANCELLATION] = map.get(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION);
			    	attributes[SHAConstants.INDEX_DOCUMENT_RECEVIED_FROM] = map.get(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM);
			    	attributes[SHAConstants.INDEX_ZONAL_BYPASS] = map.get(SHAConstants.PAYLOAD_ZONAL_BY_PASS);
			    	attributes[SHAConstants.INDEX_REMINDER_LATTER_FLAG] = map.get(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG);
			    	attributes[SHAConstants.INDEX_ALLOCATED_USER] = map.get(SHAConstants.PAYLOAD_ALLOCATED_USER);
			    	attributes[SHAConstants.INDEX_ALLOCATED_DATE] = map.get(SHAConstants.PAYLOAD_ALLOCATED_DATE);
			    	
			    	attributes[SHAConstants.INDEX_CLASSIFICATION] = map.get(SHAConstants.CLASSIFICATION);
			    	attributes[SHAConstants.INDEX_SUB_CLASSIFICATION] = map.get(SHAConstants.SUB_CLASSIFICATION);
			    	attributes[SHAConstants.INDEX_EVENT_CODE] = map.get(SHAConstants.EVENT_CODE);
			    	attributes[SHAConstants.INDEX_AILMENT_LOSS] = map.get(SHAConstants.AILMENT_LOSS);
			    	attributes[SHAConstants.INDEX_LOSS_DATE] = map.get(SHAConstants.LOSS_DATE);
			    	
		    	resultArray[0] = attributes;
				
				return resultArray;
		   }
		   
		   public static String getOMPProvisionAmtInput(OMPClaim claim, String hospitalName, String provisionAmt) {
				String input = "{\"ClaimType\": " + "\"" + claim.getClaimType().getValue() + "\", "
		                + "\"HospitalName\": " + "\"" + hospitalName + "\", "
		                + "\"HospitalType\": " + "\"" + claim.getIntimation().getHospitalType().getValue() + "\", "
		                + "\"RiskSysID\": " + "\"" + claim.getIntimation().getInsured().getInsuredId().toString() + "\", "
		                + "\"InsuredName\": " + "\"" + claim.getIntimation().getInsured().getInsuredName() + "\", "
		                + "\"IntimationNo\": " + "\"" + claim.getIntimation().getIntimationId() + "\", "
		                + "\"PolicyNo\": " + "\"" + claim.getIntimation().getPolicy().getPolicyNumber() + "\", "
		                + "\"ProvisionAmount\": " + "\"" + provisionAmt + "\" } ";
				
				return input;
			}


		   
		   public static Object[] getOMPRevisedArrayListForDBCall(OMPClaim claim){
		    	Object[] resultArray = new Object[1];
		    	Object[] attributes = new Object[89];
		    	attributes[SHAConstants.INDEX_WORK_FLOW_KEY] = 0;
		    	attributes[SHAConstants.INDEX_INTIMATION_NO] = claim.getIntimation().getIntimationId();
		    	attributes[SHAConstants.INDEX_INTIMATION_KEY] = claim.getIntimation().getKey();
		    	attributes[SHAConstants.INDEX_INTIMATION_SOURCE] = null;
		    	attributes[SHAConstants.INDEX_CPU_CODE] = null;
		    	attributes[SHAConstants.INDEX_CLAIM_NUMBER] = claim.getClaimId();
		    	attributes[SHAConstants.INDEX_CLAIM_KEY] = claim.getKey();
		    	attributes[SHAConstants.INDEX_CLAIM_TYPE] = claim.getClaimType().getValue();
		    	attributes[SHAConstants.INDEX_BALANCE_SI_FLAG] = "Y";
		    	
//					DATE admissionDate = new DATE(claim.getIntimation().getAdmissionDate().getTime());
//					attributes[SHAConstants.INDEX_ADMISSION_DATE] = claim.getIntimation().getAdmissionDate();
		    	
		    	attributes[SHAConstants.INDEX_ADMISSION_DATE] = claim.getIntimation().getAdmissionDate();
//		    	attributes[SHAConstants.INDEX_ADMISSION_DATE] = null;
					
//					DATE createdDate = new DATE(claim.getIntimation().getCreatedDate().getTime());
		    	
					attributes[SHAConstants.INDEX_INTIMATION_DATE] = new java.sql.Date(claim.getIntimation().getCreatedDate().getTime());
//					attributes[SHAConstants.INDEX_INTIMATION_DATE] = null;
					

		    	attributes[SHAConstants.INDEX_POLICY_KEY] = claim.getIntimation().getPolicy().getKey();
		    	attributes[SHAConstants.INDEX_POLICY_NUMBER] = claim.getIntimation().getPolicy().getPolicyNumber();
		    	attributes[SHAConstants.INDEX_PRODUCT_KEY] = claim.getIntimation().getPolicy().getProduct().getKey();
		    	attributes[SHAConstants.INDEX_PRODUCT_NAME] = claim.getIntimation().getPolicy().getProduct().getValue();
		    	if((ReferenceTable.HEALTH_LOB_KEY).equals(claim.getIntimation().getPolicy().getLobId())){
		    		attributes[SHAConstants.INDEX_LOB] = SHAConstants.HEALTH_LOB;	
		    	}
		    	else if((ReferenceTable.PA_LOB_KEY).equals(claim.getIntimation().getPolicy().getLobId())){
		    		attributes[SHAConstants.INDEX_LOB] = SHAConstants.PA_LOB;	
		    	}
		    	attributes[SHAConstants.INDEX_LOB_TYPE] = "H";
//		    	attributes[SHAConstants.INDEX_LOB_TYPE] = claim.getProcessClaimType();
		    	attributes[SHAConstants.INDEX_HOSPITAL_TYPE] = null;
		    	attributes[SHAConstants.INDEX_NETWORK_TYPE] = null;
		    	attributes[SHAConstants.INDEX_HOSPITAL_KEY] = null;
		    	attributes[SHAConstants.INDEX_TREATMENT_TYPE] = null;
		    	attributes[SHAConstants.INDEX_SPECIALITY_NAME] = null;
		    	attributes[SHAConstants.INDEX_PRIORITY] = claim.getIsVipCustomer() != null ? (claim.getIsVipCustomer().intValue() == 1 ? SHAConstants.VIP_CUSTOMER  : SHAConstants.NORMAL) : "" ;
		    	if(claim.getIntimation().getInsured() != null && claim.getIntimation().getInsured().getInsuredAge() != null && claim.getIntimation().getInsured().getInsuredAge().intValue()>60){
		    		attributes[SHAConstants.INDEX_PRIORITY] = SHAConstants.SENIOR_CITIZEN;
		    	}	
		    	
		    	attributes[SHAConstants.INDEX_RECORD_TYPE] = "FRESH";
		    	attributes[SHAConstants.INDEX_STAGE_SOURCE] = claim.getStage().getStageName();
		    	attributes[SHAConstants.INDEX_CASHLESS_NO] = null;
		    	attributes[SHAConstants.INDEX_CASHLESS_KEY] = 0;
		    	attributes[SHAConstants.INDEX_CLAIMED_AMT] = claim.getClaimedAmount();
		    	attributes[SHAConstants.INDEX_ROLE_ID] = null;
		    	attributes[SHAConstants.INDEX_ESCALATE_ROLE_ID] = 0;
		    	attributes[SHAConstants.INDEX_ESCALATE_USER_ID] = null;
		    	attributes[SHAConstants.INDEX_PROCESS_TYPE] = claim.getClaimType().getValue();
		    	attributes[SHAConstants.INDEX_RECONSIDERATION_FLAG] = "N";
//		    	attributes[SHAConstants.INDEX_REFERENCE_USER_ID] = "ADMINB";
		    	attributes[SHAConstants.INDEX_OUT_COME] = "REGNWFPA";
		    	DATE currentDate = new DATE();
		    	attributes[SHAConstants.INDEX_PROECSSED_DATE] = currentDate;
		    	attributes[SHAConstants.INDEX_USER_ID] = "ADMINB";
		    	attributes[SHAConstants.INDEX_PROCESS] = null;
		    	try {
		    		if(claim.getCreatedDate() != null){
						currentDate = new DATE(claim.getCreatedDate());
						attributes[SHAConstants.INDEX_REGISTRATION_DATE] = currentDate;
		    		}
		    		else{
		    			attributes[SHAConstants.INDEX_REGISTRATION_DATE] = null;
		    		}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
							    	
		    	attributes[SHAConstants.INDEX_FVR_KEY] = 0;
		    	attributes[SHAConstants.INDEX_FVR_CPU_CODE] = null;
		    	attributes[SHAConstants.INDEX_FVR_NUMBER] = null;
		    	attributes[SHAConstants.INDEX_ROD_KEY] = 0;
		    	attributes[SHAConstants.INDEX_ROD_NUMBER] = null;
//		    	Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		    	attributes[SHAConstants.INDEX_ROD_CREATED_DATE] = null;
		    	attributes[SHAConstants.INDEX_REIMB_REQ_BY] = null;
		    	attributes[SHAConstants.INDEX_ROD_REFER_BY] = null;
		    	attributes[SHAConstants.INDEX_ACK_KEY] = 0;
		    	attributes[SHAConstants.INDEX_ACK_NUMBER] = null;
		    	attributes[SHAConstants.INDEX_PREAUTH_STATUS] = null;
		    	attributes[SHAConstants.INDEX_BILL_AVAILABLE] = null;
		    	attributes[SHAConstants.INDEX_HOSPITALIZATION] = null;
		    	attributes[SHAConstants.INDEX_POST_HOSPITALIZATION] = null;
		    	attributes[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = null;
		    	attributes[SHAConstants.INDEX_PRE_HOSPITALIZATION] = null;
		    	attributes[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = null;
		    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = null;
		    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = null;
		    	attributes[SHAConstants.INDEX_RRC_REQUEST_KEY] = 0;
		    	attributes[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = null;
		    	attributes[SHAConstants.INDEX_RRC_REQUEST_TYPE] = null;
		    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY_TYPE] = 0;
		    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY] = 0;
		    	attributes[SHAConstants.INDEX_PED_KEY] = 0;
		    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ROLE] = null;
		    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ID] = 0;
		    	attributes[SHAConstants.INDEX_PED_REFERRED_BY] = null;
		    	attributes[SHAConstants.INDEX_QUERY_KEY] = 0;
		    	attributes[SHAConstants.INDEX_QUERY_DISAPPROVED] = null;
		    	attributes[SHAConstants.INDEX_QUERY_REQUESTED_BY] = null;
		    	attributes[SHAConstants.INDEX_INVESTIGATION_KEY] = 0;
		    	attributes[SHAConstants.INDEX_INV_DISAPPROVED] = null;
		    	attributes[SHAConstants.INDEX_INV_REQUEST] = null;
		    	attributes[SHAConstants.INDEX_INV_REQUESTED_BY] = null;
		    	attributes[SHAConstants.INDEX_REMINDER_CATEGORY] = null;
		    	attributes[SHAConstants.INDEX_HEALTH_CARD_NUMBER] = null;
		    	attributes[SHAConstants.INDEX_REMINDER_TYPE] = null;	 
		    	attributes[SHAConstants.INDEX_PED_TYPE] = null;
		    	attributes[SHAConstants.INDEX_PAYMENT_CANCELLATION] = null;
		    	attributes[SHAConstants.INDEX_DOCUMENT_RECEVIED_FROM] = null;
		    	attributes[SHAConstants.INDEX_ZONAL_BYPASS] = null;
		    	attributes[SHAConstants.INDEX_REMINDER_LATTER_FLAG] = null;
		    	attributes[SHAConstants.INDEX_ALLOCATED_USER] = null;
		    	attributes[SHAConstants.INDEX_ALLOCATED_DATE] = null;
		    	attributes[SHAConstants.INDEX_CLASSIFICATION] = null;
		    	
		    	attributes[SHAConstants.INDEX_SUB_CLASSIFICATION] = null;
		    	attributes[SHAConstants.INDEX_EVENT_CODE] = claim.getIntimation().getEvent().getEventCode();
		    	attributes[SHAConstants.INDEX_AILMENT_LOSS] = claim.getAilmentLoss();
		    	java.sql.Date lossDate = null;
				if(claim.getLossDateTime() != null){
					lossDate = new java.sql.Date(claim.getLossDateTime().getTime());
				}
		    	attributes[SHAConstants.INDEX_LOSS_DATE] = lossDate;
		    	
		    	
		    	resultArray[0] = attributes;

		    	return resultArray;
		    }
		   
			public static void setActiveOrDeactiveOMPClaim(Long wrkFlowKey,VaadinSession session) {
				DBCalculationService dbService = new DBCalculationService();
				if (wrkFlowKey != null) {
					dbService.callOMPUnlockProcedure(wrkFlowKey);
					session.setAttribute(SHAConstants.WK_KEY, null);
				}
			}
		   
		   public static String getAckAgeing(Date fromDate){
			   
				 try{
				      int years = 0;
				      int months = 0;
				      int days = 0;
				      int hours = 0;
				      
				      Date currentDate = new Date();
				     
				      
				      String rodAgeing = "";
				      //create calendar object for birth day
				      Calendar birthDay = Calendar.getInstance();
				      birthDay.setTimeInMillis(fromDate.getTime());
				      //create calendar object for current day
				      long currentTime = System.currentTimeMillis();
				      
				      
				     
				      Calendar now = Calendar.getInstance();
				      now.setTimeInMillis(currentTime);
				      //Get difference between years
				      years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
				      int currMonth = now.get(Calendar.MONTH) + 1;
				      int birthMonth = birthDay.get(Calendar.MONTH) + 1;
				      //Get difference between months
				      months = currMonth - birthMonth;
				      //if month difference is in negative then reduce years by one and calculate the number of months.
				      if (months < 0)
				      {
				         years--;
				         months = 12 - birthMonth + currMonth;
				         if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
				            months--;
				      } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
				      {
				         years--;
				         months = 11;
				      }
				      //Calculate the days
				      if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE)){
				    	  days =  (int) ((currentDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
				      	  hours = fromDate.getHours();
				          hours -= 9;
				      }
				         
				      else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
				      {
				         int today = now.get(Calendar.DAY_OF_MONTH);
				         now.add(Calendar.MONTH, -1);
				         days =  (int) ((currentDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
//				         hours = now.get(Calendar.HOUR_OF_DAY);
				         hours = fromDate.getHours();
				         hours -= 9;
				      } else
				      {
				         days = 0;
				         if (months == 12)
				         {
				            years++;
				            months = 0;
				         }
				      }
				      
				      if(months >0){
//				    	  rodAgeing += months+" Months ";
				      }
				      if(days > 0){
				    	  rodAgeing += days+" Days ";
				      }
				      
//				      if(hours> 0){
//				    	  rodAgeing += hours+" Hours";
//				      }
				      
				      if(days <= 0){
				    	  rodAgeing = "1 Day";
				      }
				      
				      return rodAgeing;
				 }catch(Exception e){
					 e.printStackTrace();
				 }
				 return "";
			
		   }
		
			public static Map<Long,String> getFVRReportTypeMap(){
				Map<Long,String> fvrReportIdMap = new HashMap<Long,String>();
				
				fvrReportIdMap.put(ReferenceTable.ASSIGNFVR, SHAConstants.FVR_ASSIGNED);
				fvrReportIdMap.put(ReferenceTable.SKIPFVR,SHAConstants.FVR_NOT_REQUIRED );
				fvrReportIdMap.put(ReferenceTable.INITITATE_FVR,SHAConstants.FVR_ASSIGNMENT_PENDING);
					
				return fvrReportIdMap;
			}
			

			/*public static long monthsBetween(Date startDate, Date endDate) {
			    Calendar cal = Calendar.getInstance();
			    if (startDate.before(endDate)) {
			        cal.setTime(startDate);
			    } else {
			        cal.setTime(endDate);
			        endDate = startDate;
			    }
			    int c = 0;
			    while (cal.getTime().before(endDate)) {
			        cal.add(Calendar.MONTH, 1);
			        c++;
			    }
			    return c - 1;
			}
			*/
			/*public static long monthsBetween(Date startDate, Date endDate) {
			Date fromDate = startDate;
			Date toDate = endDate;

			Period p = Period.between(toDate, fromDate);
			long p2 = ChronoUnit.DAYS.between(toDate, fromDate);
			}*/

			public static void setClearPreauthDTO(PreauthDTO bean){
				
				if(bean != null){
				
					bean.setPreauthDataExtractionDetails(null);
					bean.setPreauthMedicalDecisionDetails(null);
					bean.setPreauthPreviousClaimsDetails(null);
					bean.setPreauthMedicalProcessingDetails(null);
					bean.setPopupMap(null);
					bean.setSuspiciousPopupMap(null);
					bean.setReconsiderationList(null);
					bean.setReconsiderRodRequestList(null);
					bean.setInsuredPedDetails(null);
					bean.setDocumentDetailsDTOList(null);
					bean.setHospitalizationDetails(null);
					bean.setPostHospitalizationDTO(null);
					bean.setPrehospitalizationDTO(null);
					bean.setHospitalizationTabSummaryList(null);
					bean.setPostHospitalizationTabSummaryList(null);
					bean.setPreHospitalizationTabSummaryList(null);
					bean.setPrePostHospitalizationList(null);
					bean.setDiagnosisProcedureDtoList(null);
					bean.setRoomRentNursingChargeList(null);
					bean.setPolicyLimitDList(null);
					bean.setPolicyLimitEList(null);
					bean.setPolicyLimitDandEList(null);
					bean.setAmbulanceChargeList(null);
					bean.setTreatmentRemarksList(null);
					bean.setPreviousAccountDetailsList(null);
					bean.setDeletedClaimedAmountIds(null);
					bean.setFilePathAndTypeMap(null);
					bean.setRoomRentMappingDTOList(null);
					bean.setIcuRoomRentMappingDTOList(null);
					bean.setOtherInsHospSettlementCalcDTO(null);
					bean.setOtherInsPreHospSettlementCalcDTO(null);
					bean.setOtherInsPostHospSettlementCalcDTO(null);
					bean.setConsolidatedAmtDTO(null);
					bean.setSpecificProductDeductibleDTO(null);
					bean.setPreviousClaimsList(null);
					bean.setUploadDocDTO(null);
					bean.setUploadDocumentDTO(null);
					bean.setCoordinatorDetails(null);
					bean.setHospitalizationDetails(null);
					bean.setDownSizePreauthDataExtrationDetails(null);
					bean.setDeletedDiagnosis(null);
					bean.setDeletedProcedure(null);
					bean.setRrcDTO(null);
					bean.setSpecificProductDeductibleDTO(null);
					bean.setNonPreferredPopupMap(null);
					bean = null;
				}

			}
			
			public static void setClearReimbursementDTO(ReceiptOfDocumentsDTO bean){
				
				if(bean != null){
					
					bean.setSelectRODtoAddAdditionalDocumentsDTO(null);
					bean.setDocumentDetails(null);
					bean.setUploadDocumentsDTO(null);
					bean.setUploadedDocumentsDTO(null);
					bean.setRodQueryDetailsList(null);
					bean.setReconsiderRodRequestList(null);
					bean.setChangeHospitalDto(null);
					bean.setSectionDetailsDTO(null);
					bean.setClaimDTO(null);
					bean.setNewIntimationDTO(null);
					bean.setReconsiderRODdto(null);
					bean.setRodqueryDTO(null);
					bean.setUploadDocsList(null);
					bean.setBillEntryList(null);
					bean.setBillEntryDetailsDTO(null);
					bean.setProductBenefitMap(null);
					bean.setAddOnBenefitsDTO(null);
					bean.setProductCoPay(null);
					bean.setAlreadyUploadDocsList(null);
					bean.setSearchUploadpreauthUploadTblList(null);
					bean.setSearchUploadrodUploadTblList(null);
					bean.setCheckListTableContainerForROD(null);
					bean.setSectionList(null);
					bean.setPreviousAccountDetailsList(null);
					if(bean.getPreauthDTO() != null){
						setClearPreauthDTO(bean.getPreauthDTO());
					}	
				}				
			}
			public static void popinReportLog(EntityManager entityManager, String userId, String reportName,Date exeDate,Date txnDate,String statusFlag){
				
				try{
				
					ReportLog reportLogObj = new ReportLog();
					
					reportLogObj.setReportName(reportName);
					reportLogObj.setCurrentDate(exeDate);
					reportLogObj.setReportTime(txnDate);
					reportLogObj.setStatusFlag(statusFlag);
					reportLogObj.setUserId(userId);
					reportLogObj.setAciveStatusFlag("Y");
					
					entityManager.persist(reportLogObj);
					// Below Line added for memory issue
					entityManager.flush(); 
					entityManager.clear();

				}
				catch(Exception e){
					e.printStackTrace();	
				}
				
				
			}
			
			public static void setClearReferenceData(Map<String, Object> referenceData){
				if(referenceData != null){
		    	
			    	Iterator<Entry<String, Object>> iterator = referenceData.entrySet().iterator();
			    	//referenceData.clear();
			    	try{
				        while (iterator.hasNext()) {
				            Map.Entry pair = (Map.Entry)iterator.next();
				            Object object = pair.getValue();
				            object = null;
				            pair = null;	
				        }
			    	}catch(Exception e){
			    		e.printStackTrace();
			    	}
			        referenceData.clear();
			        referenceData = null;
				}
		    	
		    }
			
			public static void setClearMapValue(Map<Long, Object> referenceData){
				
				if(referenceData != null){
		    	
			    	Iterator<Entry<Long, Object>> iterator = referenceData.entrySet().iterator();
			    	//referenceData.clear();
			    	try{
				        while (iterator.hasNext()) {
				            Map.Entry pair = (Map.Entry)iterator.next();
				            Object object = pair.getValue();
				            object = null;
				            pair = null;	
				        }
			    	}catch(Exception e){
			    		e.printStackTrace();
			    	}
			       referenceData.clear();
			       referenceData = null;
				}
		    	
		    }
			
			public static void setClearMapDoubleValue(Map<Long, Double> referenceData){
				
				if(referenceData != null){
					Iterator<Entry<Long, Double>> iterator = referenceData.entrySet().iterator();
			    	//referenceData.clear();
			    	try{
				        while (iterator.hasNext()) {
				            Map.Entry pair = (Map.Entry)iterator.next();
				            Object object = pair.getValue();
				            object = null;
				            pair = null;	
				        }
					}catch(Exception e){
			    		e.printStackTrace();
			    	}
			       referenceData.clear();
			        referenceData = null;
				}
		    	
		    	
		    	
		    }
			
			public static void setClearMapBooleanValues(Map<String, Boolean> referenceData){
		    	
		    	Iterator<Entry<String, Boolean>> iterator = referenceData.entrySet().iterator();
//		    	referenceData.clear();
		    	try{
			        while (iterator.hasNext()) {
			            Map.Entry pair = (Map.Entry)iterator.next();
			            Object object = pair.getValue();
			            object = null;
			            pair = null;	
			        }
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}
		       referenceData.clear();
		       referenceData = null;
		    	
		    }
			
			public static Map<String, String> getNonPreferredMap(String remarks){
				
				Map<String, String> nonPreferredPopupMessage = new WeakHashMap<String, String>();
				nonPreferredPopupMessage.put(remarks, "<b style = 'color: blue'>Non-Preferred Hospital </b>");
				return nonPreferredPopupMessage;
				
			}
			
			public static void setClearMapLongValue(Map<Long, Long> referenceData){
		    	
		    	Iterator<Entry<Long, Long>> iterator = referenceData.entrySet().iterator();
//		    	referenceData.clear();
		    	try{
			        while (iterator.hasNext()) {
			            Map.Entry pair = (Map.Entry)iterator.next();
			            Object object = pair.getValue();
			            object = null;	
			            pair = null;	
			        }
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}
		       referenceData.clear();
		       referenceData = null;
		    	
		    }

	public static void setClearMapIntegerValue(Map<Integer, Object> referenceData){
		
		Iterator<Entry<Integer, Object>> iterator = referenceData.entrySet().iterator();
	//	referenceData.clear();
		try{
	        while (iterator.hasNext()) {
	            Map.Entry pair = (Map.Entry)iterator.next();
	            Object object = pair.getValue();
	            object = null;
	            pair = null;	
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
	   referenceData.clear();
	   referenceData = null;
		
	}
	
	public static void setClearTableItem(Map<DiagnosisDetailsTableDTO, HashMap<String, AbstractField<?>>> referenceData){
		
		if(referenceData != null){
    	
	    	Iterator<Entry<DiagnosisDetailsTableDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
	    	//referenceData.clear();
	    	try{
		        while (iterator.hasNext()) {
		            Map.Entry pair = (Map.Entry)iterator.next();
		            Object object = pair.getValue();
		            object = null;
		            pair = null;	
		        }
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	       referenceData.clear();
	       referenceData = null;
		}
    	
    }
	
	public static void setClearTableItemOP(Map<DiagnosisDetailsOPTableDTO, HashMap<String, AbstractField<?>>> referenceData){

		if(referenceData != null){

			Iterator<Entry<DiagnosisDetailsOPTableDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
			//referenceData.clear();
			try{
				while (iterator.hasNext()) {
					Map.Entry pair = (Map.Entry)iterator.next();
					Object object = pair.getValue();
					object = null;
					pair = null;	
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			referenceData.clear();
			referenceData = null;
		}

	}

	public static void clearSessionObject(VaadinRequest currentRequest){
		if(currentRequest != null){
		    currentRequest.setAttribute(SHAConstants.OBJECT_MAPPER, null);
		    currentRequest.setAttribute(SHAConstants.PREAUTH_DTO, null);
		    currentRequest.setAttribute(SHAConstants.BILL_ASSESSMENT_VIEW_PAGE, null);
		    currentRequest.setAttribute(SHAConstants.CLAIM_DETAILS, null);
		    currentRequest.setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS, null);
		    currentRequest.setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE, null);
		}
	}
	
	public static Boolean validateApprovedAmount(PreauthDTO reimbursementDTO,Integer amount){
		Boolean inValid = false;
		if(reimbursementDTO.getHospitalizaionFlag() != null && reimbursementDTO.getHospitalizaionFlag()){
			inValid = amount == 0 ? true : false;
		}
		else if(reimbursementDTO.getPartialHospitalizaionFlag() != null && reimbursementDTO.getPartialHospitalizaionFlag()){
			inValid = amount == 0 ? true : false;
		}
		else if(reimbursementDTO.getPreHospitalizaionFlag()!= null && reimbursementDTO.getPreHospitalizaionFlag()){
			inValid = amount == 0 ? true : false;
		}
		else if(reimbursementDTO.getPostHospitalizaionFlag()!= null && reimbursementDTO.getPostHospitalizaionFlag()){
			inValid = amount == 0 ? true : false;
		}
		else if(reimbursementDTO.getIsHospitalizationRepeat() != null && reimbursementDTO.getIsHospitalizationRepeat()){
			inValid = amount == 0 ? true : false;
		}else if(reimbursementDTO.getOtherBenefitsFlag() != null && reimbursementDTO.getOtherBenefitsFlag()){
			inValid = amount == 0 ? true : false;
		}
		return inValid;
	}
	
	public static StreamResource.StreamSource getStreamResource(final String finalFilePath){
		 
		StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {
					
					File f = new File(finalFilePath);
					FileInputStream  fis = new FileInputStream(f);
					return fis;
					
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				} 
			}
		};
		return s;
	}
	
	public static void closeStreamResource(StreamResource.StreamSource s) {

		try {
			s.getStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Boolean getProcedureStatusforConsiderForPayment(DiagnosisProcedureTableDTO diagnosisDto) {

		Boolean isPaymentAvailable = true;
		if (diagnosisDto.getProcedureDTO().getConsiderForPaymentFlag() != null) {
			isPaymentAvailable = diagnosisDto.getProcedureDTO()
					.getConsiderForPaymentFlag().toLowerCase()
					.equalsIgnoreCase("y") ? true : false;
		} else {
			isPaymentAvailable = false;
			if(diagnosisDto.getProcedureDTO().getNewProcedureFlag() != null && diagnosisDto.getProcedureDTO().getNewProcedureFlag().equals(1)) {
				isPaymentAvailable = true;				
			}
		}
			
		if (isPaymentAvailable) {
			if (diagnosisDto.getProcedureDTO().getExclusionDetails() != null
					&& diagnosisDto.getProcedureDTO().getExclusionDetails()
							.getValue() != null
					&& !diagnosisDto.getProcedureDTO().getExclusionDetails()
							.getValue().toString().toLowerCase()
							.equalsIgnoreCase("not applicable")) {
				isPaymentAvailable = false;
				diagnosisDto.setIsPedExclusionFlag(false);
			}
		}
			
		return isPaymentAvailable;
	
	}
	
	public static Boolean getDiagnosisStatusforConsiderForPayment(DiagnosisProcedureTableDTO diagnosisDto) {

		Boolean isPaymentAvailable = (diagnosisDto.getDiagnosisDetailsDTO()
				.getConsiderForPaymentFlag() != null && diagnosisDto.getDiagnosisDetailsDTO()
						.getConsiderForPaymentFlag().toLowerCase()
				.equalsIgnoreCase("y")) ? true : false;
		if (isPaymentAvailable) {
			List<PedDetailsTableDTO> pedList = diagnosisDto.getDiagnosisDetailsDTO()
					.getPedList();
			if (!pedList.isEmpty()) {
				for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
                    
					List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO
							.getExclusionAllDetails();
					String paymentFlag = "y";
					if(exclusionAllDetails != null){
						for (ExclusionDetails exclusionDetails : exclusionAllDetails) {
							if (null != pedDetailsTableDTO
									.getExclusionDetails()
									&& exclusionDetails
											.getKey()
											.equals(pedDetailsTableDTO
													.getExclusionDetails()
													.getId())) {
								paymentFlag = exclusionDetails
										.getPaymentFlag();
							}
						}
					}

					if (paymentFlag.toLowerCase().equalsIgnoreCase(
							"n")) {
						isPaymentAvailable = false;
						diagnosisDto.setIsPedExclusionFlag(false);
						break;
					}
				}
			}
		}
		return isPaymentAvailable;
	}
	
	public static void setDiagnosisTableList(BeanItemContainer<SelectValue> dianosisList){
		dianosisListValue = dianosisList;
	}
	
	public static Boolean hasDuplicates(List<Long> limitValues) {
	    final List<Long> existingValues = new ArrayList<Long>();
	    for (Long limitId : limitValues) {

	        if (existingValues.contains(limitId)) {
	            return true;
	        }

	        existingValues.add(limitId);
	    }

	    return false;
	}
	
	
	public static List<ZUAViewQueryHistoryTableDTO> setViewZUAQueryHistoryTableValues(String policyNumber,PolicyService policyService) {
		
		Policy policyObj = null;
		Builder builder = null;
		List<ZUAViewQueryHistoryTableDTO> listOfQueryHistory = new ArrayList<ZUAViewQueryHistoryTableDTO>();

		if (policyNumber != null) {
			policyObj = policyService.getByPolicyNumber(policyNumber);
			if (policyObj != null) {
				if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
					listOfQueryHistory = BancsConsumerDetailService.getInstance().getQCZonalAuditQueryDetails(policyNumber);
				}else {	
					List<Object[]> QuerytableValues = policyService.zuaQueryHistoryDetails(policyNumber);			
					for (Object[] viewQueryDTO : QuerytableValues) {
						ZUAViewQueryHistoryTableDTO queryHistoryDTO = new ZUAViewQueryHistoryTableDTO();
						queryHistoryDTO.setFlowFrom(String.valueOf(viewQueryDTO[2]));
						queryHistoryDTO.setFlowTo(String.valueOf(viewQueryDTO[3]));
						queryHistoryDTO.setType(String.valueOf(viewQueryDTO[4]));
						queryHistoryDTO.setRemarks(String.valueOf(viewQueryDTO[7]));
						queryHistoryDTO.setSerialNo(String.valueOf(viewQueryDTO[8]));
						Timestamp apprDate = (Timestamp)viewQueryDTO[10];
						String formatDate = SHAUtils.formateDateForHistory(new Date(apprDate.getTime()));
						queryHistoryDTO.setApprDateAndTime(formatDate);
						queryHistoryDTO.setUserId(String.valueOf(viewQueryDTO[11]));
					 	listOfQueryHistory.add(queryHistoryDTO);
					 	//zuaViewQueryHistoryTable.addBeanToList(queryHistoryDTO);
					}
				} 
			}
		}
	
		return listOfQueryHistory;
	}
	
	@SuppressWarnings("static-access")
	public static String callAlfrescoUploadAPI(String fileName, File file) {
		String tokenId = "";
		Random randomGenerator = new Random();
		int randomId = randomGenerator.nextInt(10000);
		StringBuffer tokenBfr = new StringBuffer();
		Date date  = new Date();
		
		tokenBfr.append(randomId).append(date.getTime());
		tokenId = String.valueOf(tokenBfr);
		String httpURL = SHAConstants.ALFRESCO_ENDPOINT_URL+SHAConstants.ALFRESCO_SERVICE_FILE_UPLOAD+"/"+tokenId+"/"
				+SHAConstants.ALFRESCO_APP_ID+"/"+SHAConstants.ALFRESCO_TEMP_ID;
		String response = PremiaService.getInstance().getUploadFile(httpURL,file);
		/*Client client = null;
		WebTarget webTarget = null;
		
		MultipartFormDataOutput multipartFormDataOutput = null;
		GenericEntity<MultipartFormDataOutput> genericEntity = null;
		Response response = null;
		int responseCode;
		//String responseMessageFromServer = null;
		String responseString = null;
		try {
			client = ClientBuilder.newClient();
			webTarget = client.target(httpURL);
			client.property("Content-Type", MediaType.MULTIPART_FORM_DATA);
			builder = webTarget.request();
			Builder uploadFile = PremiaService.getInstance().getUploadFile(httpURL);
			multipartFormDataOutput = new MultipartFormDataOutput();
			multipartFormDataOutput.addFormData("uploadFile", fileAsBytes, MediaType.APPLICATION_OCTET_STREAM_TYPE);
			genericEntity = new GenericEntity<MultipartFormDataOutput>(multipartFormDataOutput) {};
			//
		  response = builder.post(Entity.entity(genericEntity, MediaType.MULTIPART_FORM_DATA));
			responseCode = response.getStatus();

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + responseCode);
			}*/
			//System.out.println("ResponseMessageFromServer: " + responseMessageFromServer);
		return response;

			//responseString = response.readEntity(String.class);
		} 
			
				 
			
	
	public static List<ZUAViewQueryHistoryTableDTO> setViewTopZUAQueryHistoryTableValues(String policyNumber,PolicyService policyService) {
		
		Policy policyObj = null;
		Builder builder = null;
		List<ZUAViewQueryHistoryTableDTO> listOfQueryHistory = new ArrayList<ZUAViewQueryHistoryTableDTO>();

		if (policyNumber != null) {
			policyObj = policyService.getByPolicyNumber(policyNumber);
			if (policyObj != null) {
				if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
					listOfQueryHistory = BancsConsumerDetailService.getInstance().getQCZonalAuditQueryDetails(policyNumber);
				}else {
					List<ZUASendQueryTable> zuaQuerytableValues = policyService.getZUAQueryDetails(policyNumber);
					
					if(null != zuaQuerytableValues && !zuaQuerytableValues.isEmpty()){
					
					for (ZUASendQueryTable zuaSendQueryTable : zuaQuerytableValues) {
							ZUAViewQueryHistoryTableDTO queryHistoryDTO = new ZUAViewQueryHistoryTableDTO();
							queryHistoryDTO.setQueryCode(zuaSendQueryTable.getQueryCode());
							queryHistoryDTO.setQueryDescription(zuaSendQueryTable.getQueryRaiseRemarks());
							listOfQueryHistory.add(queryHistoryDTO);
						}
					}
				}
			}
		}
		/*List<ZUASendQueryTable> zuaQuerytableValues = policyService.getZUAQueryDetails(policyNumber);
		List<ZUAViewQueryHistoryTableDTO> listOfQueryHistory = new ArrayList<ZUAViewQueryHistoryTableDTO>();
		
		if(null != zuaQuerytableValues && !zuaQuerytableValues.isEmpty()){
		
		for (ZUASendQueryTable zuaSendQueryTable : zuaQuerytableValues) {
			ZUAViewQueryHistoryTableDTO queryHistoryDTO = new ZUAViewQueryHistoryTableDTO();
			queryHistoryDTO.setQueryCode(zuaSendQueryTable.getQueryCode());
			queryHistoryDTO.setQueryDescription(zuaSendQueryTable.getQueryRaiseRemarks());
			listOfQueryHistory.add(queryHistoryDTO);
		}
		}*/
		return listOfQueryHistory;
	}
	
	public static Map<Long, Long> getInitiateFVRStatusMap() {
		Map<Long, Long> initateStatusMap = new HashMap<Long, Long>();
		initateStatusMap.put(87l, 87l);
		initateStatusMap.put(123l, 123l);
		initateStatusMap.put(85l, 85l);
		initateStatusMap.put(86l, 86l);
		initateStatusMap.put(148l, 148l);
		return initateStatusMap;
	}

	//Lumen submit task procedure object creation...
	public static Object[] getArrayListForLumenSubmit(LumenRequest lumenObj){

		//		   WK_KEY NUMBER (15),
		//		   INTIMATION_NO VARCHAR2 (100),
		//		   INT_SOURCE VARCHAR2 (100) ,              --Initiate From 
		//		   CPU_CODE NUMBER (15),
		//		   CLAIM_TYPE VARCHAR2 (100),
		//		   POLICY_NUMBER VARCHAR2 (100),
		//		   PRODUCT_NAME VARCHAR2 (100),
		//		   DOCUMENT_RECEVIED_FROM VARCHAR2 (100),   --Insured Patient Name
		//		   STAGE_SOURCE   VARCHAR2 (100),           -- Lumen Source, ,
		//		   OUTCOME VARCHAR2 (100),                  --Lumen OUTCOME
		//		   PROCESSED_DATE TIMESTAMP (6),
		//		   USER_ID VARCHAR2 (100),
		//		   FVR_KEY NUMBER,                          --LUMEN_KEY
		//		   FVR_NUMBER VARCHAR2 (30),                -- LUMEN_NUMBER
		//		   ROD_CREATED_DATE     TIMESTAMP,          --LUMEN_INITIATE_DATE
		//		   REIMB_REQ_BY VARCHAR2 (100),             --LUMEN_INITIATED_BY 
		//		   ROD_REFER_BY VARCHAR2 (100),             --LUMEN_EMP_NAME 
		//		   PROCESS      VARCHAR2 (100),             --Action
		//		   PROCESS_TYPE VARCHAR2 (100),             --Branch
		//		   PREAUTH_STATUS VARCHAR2 (100),           --LUMEN_STATUS 
		//		   RRC_REQUEST_TYPE VARCHAR2 (20),          --LUMEN_QUERY_STAGE
		//		   INV_REQUESTED_BY VARCHAR2 (50)           --LUMEN_QRY_INITIATED_BY 

		Object[] attributes = new Object[22];   	   
		attributes[SHAConstants.LMN_INDEX_WORK_FLOW_KEY] = 0;
		attributes[SHAConstants.LMN_INDEX_INTIMATION_NO] = (lumenObj.getClaim() ==  null)?null:lumenObj.getClaim().getIntimation().getIntimationId();
		attributes[SHAConstants.LMN_INDEX_INTIMATION_SOURCE] = lumenObj.getRequestedStageId().getStageName();
		attributes[SHAConstants.LMN_INDEX_CPU_CODE] = (lumenObj.getClaim() ==  null)?null:lumenObj.getClaim().getIntimation().getCpuCode().getCpuCode();
		attributes[SHAConstants.LMN_INDEX_CLAIM_TYPE] = (lumenObj.getClaim() ==  null)?"":lumenObj.getClaim().getClaimType().getValue();
		attributes[SHAConstants.LMN_INDEX_POLICY_NUMBER] = lumenObj.getPolicyNumber();//getClaim().getIntimation().getPolicy().getPolicyNumber();
		attributes[SHAConstants.LMN_INDEX_PRODUCT_NAME] = (lumenObj.getClaim() ==  null)?null:lumenObj.getClaim().getIntimation().getPolicy().getProduct().getValue();
		attributes[SHAConstants.LMN_INDEX_DOCUMENT_RECEVIED_FROM] = (lumenObj.getClaim() ==  null)?null:lumenObj.getClaim().getIntimation().getPolicy().getProposerFirstName();
		attributes[SHAConstants.LMN_INDEX_STAGE_SOURCE] = lumenObj.getStage().getStageName();
		attributes[SHAConstants.LMN_INDEX_OUT_COME] = "";
		attributes[SHAConstants.LMN_INDEX_PROECSSED_DATE] = null;
		attributes[SHAConstants.LMN_INDEX_USER_ID] = (lumenObj.getModifiedBy() == null)?lumenObj.getCreatedBy():lumenObj.getModifiedBy();
		attributes[SHAConstants.LMN_INDEX_FVR_KEY] = lumenObj.getKey();
		attributes[SHAConstants.LMN_INDEX_FVR_NUMBER] = lumenObj.getLumenRefNumber();
		attributes[SHAConstants.LMN_INDEX_ROD_CREATED_DATE] = new java.sql.Timestamp(lumenObj.getLumenInitiatedDate().getTime());//   new java.sql.Date(Calendar.getInstance().getTime().getTime()); 
		attributes[SHAConstants.LMN_INDEX_REIMB_REQ_BY] = lumenObj.getCreatedBy();
		attributes[SHAConstants.LMN_INDEX_ROD_REFER_BY] = lumenObj.getProcessedBy();		
		attributes[SHAConstants.LMN_INDEX_PROCESS] = "";
		attributes[SHAConstants.LMN_INDEX_PROCESS_TYPE] = "";
		attributes[SHAConstants.LMN_INDEX_PREAUTH_STATUS] = lumenObj.getStatus().getUserStatus();
		attributes[SHAConstants.LMN_INDEX_RRC_REQUEST_TYPE] = "";
		attributes[SHAConstants.LMN_INDEX_INV_REQUESTED_BY] = "";
		return attributes;
	}

	//Lumen submit task procedure object creation...
	public static Object[] getArrayListForLumenGetTask(CommonSearchFormData dataObj){		 
		Object[] attributes = new Object[12];   	   
		attributes[SHAConstants.LMN_SRCH_INDEX_INTIMATION_NO] = dataObj.getIntimationNumber();
		attributes[SHAConstants.LMN_SRCH_INDEX_POLICY_NUMBER] = dataObj.getPolicyNumber();
		attributes[SHAConstants.LMN_SRCH_INDEX_CPU_CODE] = dataObj.getCpuCodeMulti();
		attributes[SHAConstants.LMN_SRCH_INDEX_DOCUMENT_RECEVIED_FROM] = "";
		attributes[SHAConstants.LMN_SRCH_INDEX_STAGE_SOURCE] = dataObj.getSource();
		attributes[SHAConstants.LMN_SRCH_INDEX_PREAUTH_STATUS] = dataObj.getStatus();
		attributes[SHAConstants.LMN_SRCH_INDEX_CURRENT_Q] = dataObj.getCurrentQ();
		attributes[SHAConstants.LMN_SRCH_INDEX_USER_ID] = dataObj.getEmpName();
		attributes[SHAConstants.LMN_SRCH_INDEX_RRC_REQUEST_TYPE] = "";
		attributes[SHAConstants.LMN_SRCH_INDEX_INV_REQUESTED_BY] = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		if(dataObj.getFromDate() != null){
			String formatFromDate = formatter.format(dataObj.getFromDate());
			attributes[SHAConstants.LMN_SRCH_INDEX_INIT_FROM_DATE] = formatFromDate;
		}else{
			attributes[SHAConstants.LMN_SRCH_INDEX_INIT_FROM_DATE] = "";
		}
		
		if(dataObj.getToDate() != null){
			String formatTODate = formatter.format(dataObj.getToDate());
			attributes[SHAConstants.LMN_SRCH_INDEX_INIT_TO_DATE] = formatTODate;
		}else{
			attributes[SHAConstants.LMN_SRCH_INDEX_INIT_TO_DATE] = "";
		}

		return attributes;
	}
	
public static Object[] setMappingValueForBatch(Map<String, Object> map) {
		
		Object[] resultArray = new Object[1];
		
		Object[] inputArray = new Object[10];
		inputArray[0] = map.get(SHAConstants.INTIMATION_NO) != null ?  map.get(SHAConstants.INTIMATION_NO) : null;
		inputArray[1] = map.get(SHAConstants.HOLD_TYPE) != null ?  map.get(SHAConstants.HOLD_TYPE) : null;
		inputArray[2] = map.get(SHAConstants.CPU_CODE) != null ?  map.get(SHAConstants.CPU_CODE) : null;
		inputArray[3] = map.get(SHAConstants.CLAIM_TYPE) != null ?  map.get(SHAConstants.CLAIM_TYPE) : null;
		inputArray[4] = map.get(SHAConstants.ZONE) != null ?  map.get(SHAConstants.ZONE) : null;
		inputArray[5] = map.get(SHAConstants.PAYMENT_TYPE) != null ?  map.get(SHAConstants.PAYMENT_TYPE) : null;
		inputArray[6] = map.get(SHAConstants.LOT_NUMBER_VALUE) != null ?  map.get(SHAConstants.LOT_NUMBER_VALUE) : null;
		inputArray[7] = map.get(SHAConstants.BATCH_NUMBER) != null ?  map.get(SHAConstants.BATCH_NUMBER) : null;
		inputArray[8] = map.get(SHAConstants.PRODUCT_NAME) != null ?  map.get(SHAConstants.PRODUCT_NAME) : null;
		inputArray[9] = map.get(SHAConstants.PAYMENT_CPU_CODE) != null ?  map.get(SHAConstants.PAYMENT_CPU_CODE) : null;
		
		resultArray[0]= inputArray;
		
		return resultArray; 
		
	}

	public static Object[] setMappingValueForLot(Map<String, Object> map) {
		
		Object[] resultArray = new Object[1];
		
		Object[] inputArray = new Object[6];
		
		inputArray[0] = map.get(SHAConstants.DOCUMENT_RECEVIED_FROM) != null ?  map.get(SHAConstants.DOCUMENT_RECEVIED_FROM) : null;
		inputArray[1] = map.get(SHAConstants.CPU_CODE) != null ?  map.get(SHAConstants.CPU_CODE) : null;
		inputArray[2] = map.get(SHAConstants.CLAIM_TYPE) != null ?  map.get(SHAConstants.CLAIM_TYPE) : null;
		inputArray[3] = map.get(SHAConstants.PAYMENT_STATUS) != null ?  map.get(SHAConstants.PAYMENT_STATUS) : null;
		inputArray[4] = map.get(SHAConstants.START_DATE) != null ?  map.get(SHAConstants.START_DATE) : null;
		inputArray[5] = map.get(SHAConstants.END_DATE) != null ?  map.get(SHAConstants.END_DATE) : null;
		
		resultArray[0]= inputArray;
		
		return resultArray; 
		
	}
	
	public static HorizontalLayout newImageCRM(PreauthDTO bean) {
		Label crmImage = new Label();
		crmImage.setDescription("CRM Flagged");
		//crmImage.setStyleName("myfield");	--IMSSUPPOR-28749
		if(bean.getCrmFlagged()!=null && bean.getCrmFlagged().equalsIgnoreCase("Y")){
			crmImage.setIcon(new ThemeResource("images/CRM_image.png"));
		}else{
			crmImage.setIcon(new ThemeResource("images/CRM_image_Grey.png"));
		}
		
		Label hospital = new Label();
		hospital.setDescription("Preferred Network Hospital");
		//hospital.setStyleName("myfield");
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto()!=null && bean.getNewIntimationDTO().getHospitalDto().getIsPreferredHospital() != null 
				&& bean.getNewIntimationDTO().getHospitalDto().getIsPreferredHospital().equalsIgnoreCase("Y")){
			hospital.setIcon(new ThemeResource("images/hospital.png"));
		}else{
			hospital.setIcon(new ThemeResource("images/hospital_Grey.png"));
		}
			
		Label senior = new Label();
		senior.setDescription("Senior Citizen");
		//senior.setStyleName("myfield");
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getInsuredPatient()!=null && bean.getNewIntimationDTO().getInsuredPatient().getInsuredAge()!=null
				&&bean.getNewIntimationDTO().getInsuredPatient().getInsuredAge() >= 60){
			senior.setIcon(new ThemeResource("images/senior.png"));
		}else{
			senior.setIcon(new ThemeResource("images/senior_Grey.png"));
		}
		Label vip = new Label();
		vip.setDescription("VIP");
		if(bean.getVipCustomer() != null && bean.getVipCustomer().toString().equalsIgnoreCase("1")){
			vip.setIcon(new ThemeResource("images/VIP_Image.png"));
		}else{
			vip.setIcon(new ThemeResource("images/VIP_Image_Grey.png"));
		}
		
		Label priorityLabel = new Label();
		if(bean.getClaimPriorityLabel() != null && bean.getClaimPriorityLabel().equals("Y")){
			priorityLabel.setDescription("Priority - Corporate Claim");
			priorityLabel.setIcon(new ThemeResource("images/Priority.png"));
		}else{
			priorityLabel.setIcon(new ThemeResource("images/Non_Priority.png"));
		}
		
		DBCalculationService dbService = new DBCalculationService();
		Boolean fraudFlag = dbService.getFraudFlag(bean.getNewIntimationDTO().getIntimationId(),
				bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),bean.getNewIntimationDTO().getHospitalDto().getHospitalCode(),
				bean.getNewIntimationDTO().getHospitalDto().getHospitalIrdaCode(),bean.getNewIntimationDTO().getPolicy().getAgentCode());
		Label fraudLabel = new Label();
		if(fraudFlag != null && fraudFlag)
		{
			fraudLabel.setDescription("Fraud Claim");
			fraudLabel.setIcon(new ThemeResource("images/Fraud_red.png"));
		}else {
			fraudLabel.setDescription("Fraud Claim");
			fraudLabel.setIcon(new ThemeResource("images/Fraud_grey.png"));
		}
			
		HorizontalLayout formLayout = new HorizontalLayout(crmImage,hospital,senior,vip,priorityLabel,fraudLabel);
		return formLayout;
	}
	
	public static HorizontalLayout zohoGrievanceFlag(PreauthDTO bean) {
		Label grievanceIcon = new Label();
		grievanceIcon.setDescription("Grievance Flagged");
		if(bean.getZohoGrievanceFlag()!=null && bean.getZohoGrievanceFlag().equalsIgnoreCase("Y")){
			grievanceIcon.setIcon(new ThemeResource("images/zohoGrievanceIcon.jfif"));
		}
		HorizontalLayout formLayout = new HorizontalLayout(grievanceIcon);
		return formLayout;
	}
	
	
	public static HorizontalLayout coInsuranceIcon(PreauthDTO bean,CoInsuranceDetailView coInsuranceDetailView){
		Label coInsurance  = new Label();
		coInsurance.setDescription("COINSURANCE");
		if(bean.getIsCoInsurance()){
			coInsurance.setIcon(new ThemeResource("images/co_insurance_color.png"));
		}else{
			coInsurance.setIcon(new ThemeResource("images/co_insurance_grey.png"));
		}
		HorizontalLayout formLayout = new HorizontalLayout(coInsurance);
		
		if(bean.getIsCoInsurance()){
			 formLayout.addLayoutClickListener(new LayoutClickListener() {

					@Override
					public void layoutClick(LayoutClickEvent event) {
						List<CoInsuranceTableDTO>coInsuranceCheckList = bean.getCoInsuranceList();
						coInsuranceDetailView.init(coInsuranceCheckList);
						Window popup = new com.vaadin.ui.Window();
						popup.setCaption("CoInsurance Details");
						popup.setWidth("30%");
						popup.setHeight("30%");
						popup.setResizable(true);
						popup.setContent(coInsuranceDetailView);
						popup.setClosable(true);
						popup.center();
//						popup.setResizable(false);
						popup.addCloseListener(new Window.CloseListener() {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							@Override
							public void windowClose(CloseEvent e) {
								System.out.println("Close listener called");
							}
						});

						popup.setModal(true);
					
						UI.getCurrent().addWindow(popup);
					}

				
			});
			}
		return formLayout;
		
	}
	

	// CR2019199
	public static HorizontalLayout coInsuranceFlag(String policyNumber , CoInsuranceDetailView coInsuranceDetailView){
		Label coInsurance  = new Label();
		DBCalculationService dbService = new DBCalculationService();
		
		String activePopup = dbService.getCoInsuranceCheckValue(policyNumber);
		coInsurance.setDescription("COINSURANCE");
		if(activePopup.equalsIgnoreCase("Y")){
			coInsurance.setIcon(new ThemeResource("images/co_insurance_color.png"));
		}else{
			coInsurance.setIcon(new ThemeResource("images/co_insurance_grey.png"));
		}
		HorizontalLayout formLayout = new HorizontalLayout(coInsurance);
		
		if(activePopup.equalsIgnoreCase("Y")){
		 formLayout.addLayoutClickListener(new LayoutClickListener() {

				@Override
				public void layoutClick(LayoutClickEvent event) {
					List<CoInsuranceTableDTO>coInsuranceCheckList = dbService.getCoInsuranceDetails(policyNumber);
					coInsuranceDetailView.init(coInsuranceCheckList);
					Window popup = new com.vaadin.ui.Window();
					popup.setCaption("CoInsurance Details");
					popup.setWidth("30%");
					popup.setHeight("30%");
					popup.setResizable(true);
					popup.setContent(coInsuranceDetailView);
					popup.setClosable(true);
					popup.center();
//					popup.setResizable(false);
					popup.addCloseListener(new Window.CloseListener() {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});

					popup.setModal(true);
				
					UI.getCurrent().addWindow(popup);
				}

			
		});
		}
		return formLayout;
		
	}
	
	
	//CR20181282
	public static HorizontalLayout hospitalFlag(PreauthDTO bean){
		Label hospitalFlag1 = new Label();
		
		Label hospitalFlag2 = new Label("HT : ");
		hospitalFlag2.setDescription("Hospital Type");
		hospitalFlag2.setStyleName("hospitalFlag");
		hospitalFlag2.setStyleName("myfield");
		Label hospitalFlag3 = new Label();
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto()!=null && bean.getNewIntimationDTO().getHospitalDto().getHospitalFlag()!=null){
			hospitalFlag3.setValue(bean.getNewIntimationDTO().getHospitalDto().getHospitalFlag());
		}else{
			hospitalFlag3.setIcon(new ThemeResource("images/hypn.PNG"));
			hospitalFlag3.setStyleName("myfield");
		}
		
		
		HorizontalLayout hFlag=new HorizontalLayout(hospitalFlag2,hospitalFlag3);
		VerticalLayout hospitalFlag=new VerticalLayout(hospitalFlag1,hFlag);
	    HorizontalLayout formLayout = new HorizontalLayout(hospitalFlag);
		return formLayout;
	}
	
	public static HorizontalLayout hospitalScore(PreauthDTO bean,HospitalService hospitalService){
		Label hospitalFlag1 = new Label();
		
		ProviderWiseScoring hospitaldetails =  hospitalService.getScoringDetails(bean.getNewIntimationDTO().getHospitalDto().getHospitalCode());
		if(hospitaldetails != null) {
			bean.getNewIntimationDTO().getHospitalDto().setClaimsReported(hospitaldetails.getCliamCount());
			bean.getNewIntimationDTO().getHospitalDto().setClaimsScored(hospitaldetails.getScoredClaimCount());
			bean.getNewIntimationDTO().getHospitalDto().setDeficiencyNotIn(hospitaldetails.getScorePercentage());
			//bean.getNewIntimationDTO().getHospitalDto().setFinalScore(hospitaldetails.getFinalScore());
			bean.getNewIntimationDTO().getHospitalDto().setFinalScoreDeficiency(hospitaldetails.getFinalScoreDeficiency());
		}
		Label hospitalFlag2 = new Label("Hospital Score");
		hospitalFlag2.addStyleName("v-labelarea-Boldstyle");
		DBCalculationService dbService = new DBCalculationService();
		Label hospitalFlag3 = new Label();
		Link hSPoints = new Link();
		hSPoints.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		hSPoints.setWidth("25%");
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto()!=null && bean.getNewIntimationDTO().getHospitalDto().getHospitalCode()!=null){
			
			Map<String,Object> preauthDTO = dbService.getHospitalScorePoints(bean.getNewIntimationDTO().getHospitalDto().getHospitalCode());
			String color = preauthDTO.get(SHAConstants.HOSPITAL_SCORE_COLOR).toString();
			hSPoints.setCaption("<b style = 'color: "+color+";text-decoration: underline;'>"+preauthDTO.get(SHAConstants.HOSPITAL_SCORE_POINTS).toString()+"");
			hSPoints.setCaptionAsHtml(true);
			bean.setFinalScore(preauthDTO.get(SHAConstants.HOSPITAL_SCORE_POINTS).toString());
		}
		Label dummy = new Label();
		Label dummy1 = new Label();
		Label dummy2 = new Label();
		HorizontalLayout dummyLayout = new HorizontalLayout(dummy,dummy1,dummy2,hSPoints);
		VerticalLayout hospitalSCore = new VerticalLayout(hospitalFlag2,dummyLayout);
		
		HorizontalLayout hspointFlag = new HorizontalLayout(hospitalSCore);
		HorizontalLayout hFlag=new HorizontalLayout(hspointFlag,hospitalFlag3);
		VerticalLayout hospitalFlag=new VerticalLayout(hospitalFlag1,hFlag);
	    HorizontalLayout formLayout = new HorizontalLayout(hospitalFlag);
	    formLayout.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) { 
				Window popup = new Window("Hospital Scoring Details");
				popup.setWidth("38%");
				popup.setResizable(false);
				popup.setClosable(false);
				popup.center();
				Label noOfClaimsReported = new Label("Total No. claims Reported");
				Label noOfClaimsScored = new Label("No. of Claims Scored with Deficiency");
				Label deficiencyNoteIn = new Label("Score Percentage (No of Claims Scored/Reported)");
				Label finalScore = new Label("Final Score based on No of claims scored with deficiency");
				noOfClaimsReported.setWidth("100%");
				noOfClaimsScored.setWidth("100%");
				deficiencyNoteIn.setWidth("100%");
				finalScore.setWidth("100%");
				Label claimsReportedValue = new Label();
				claimsReportedValue.setWidth("100%");
				if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getHospitalDto() && null != bean.getNewIntimationDTO().getHospitalDto().getClaimsReported()) {
					claimsReportedValue.setValue(bean.getNewIntimationDTO().getHospitalDto().getClaimsReported().toString()); 
					}else{
						claimsReportedValue.setValue("-");
					}
				Label claimsScored = new Label();
				claimsScored.setWidth("100%");
				if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getHospitalDto() && null != bean.getNewIntimationDTO().getHospitalDto().getClaimsScored()) {
					claimsScored.setValue(bean.getNewIntimationDTO().getHospitalDto().getClaimsScored().toString()); 
					}else{
						claimsScored.setValue("-");
					}
				Label defNoted = new Label();
				defNoted.setWidth("100%");
				if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getHospitalDto() && null != bean.getNewIntimationDTO().getHospitalDto().getDeficiencyNotIn()) {
					//defNoted.setValue((df.format(bean.getNewIntimationDTO().getHospitalDto().getDeficiencyNotIn())).toString()+"%");
					defNoted.setValue(bean.getNewIntimationDTO().getHospitalDto().getDeficiencyNotIn());
				}else{
						defNoted.setValue("-");
					}
				Label finalSCr = new Label();
				finalSCr.setWidth("100%");
				if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getHospitalDto() && null != bean.getNewIntimationDTO().getHospitalDto().getFinalScoreDeficiency()) {
//					finalSCr.setValue((df.format(bean.getNewIntimationDTO().getHospitalDto().getFinalScoreDeficiency())).toString());
					finalSCr.setValue(bean.getNewIntimationDTO().getHospitalDto().getFinalScoreDeficiency());
					//finalSCr.setValue(bean.getFinalScore());
					}else{
						finalSCr.setValue("-");
					}
				VerticalLayout layout1 = new VerticalLayout(noOfClaimsReported,noOfClaimsScored,deficiencyNoteIn,finalScore);
				VerticalLayout layout2 = new VerticalLayout(claimsReportedValue,claimsScored,defNoted,finalSCr);
				
				layout2.setStyleName("layoutDesign");
				FormLayout leftForm = new FormLayout(layout1);
				FormLayout rightForm = new FormLayout(layout2);
				HorizontalLayout resultLayout = new HorizontalLayout(leftForm,rightForm);
				Button close = new Button("Close");
				VerticalLayout resultLayout1 = new VerticalLayout(resultLayout,close);
				resultLayout1.setComponentAlignment(close, Alignment.BOTTOM_CENTER);
				close.setStyleName(ValoTheme.BUTTON_DANGER);
				Panel panel = new Panel(resultLayout1);
				FormLayout resultForm = new FormLayout(resultLayout1);
				resultForm.setStyleName("layoutDesign");
				popup.setContent(resultForm);
				close.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						popup.close();
					}
				});
				 popup.setModal(true);
					UI.getCurrent().addWindow(popup);
			}
		});
	   
	   
		return formLayout;
	}
	
	public static HorizontalLayout hospitalScoreForView(PreauthDTO bean,HospitalService hospitalService){
		Label hospitalFlag1 = new Label();
		
		ProviderWiseScoring hospitaldetails =  hospitalService.getScoringDetails(bean.getNewIntimationDTO().getHospitalDto().getHospitalCode());
		if(hospitaldetails != null) {
			bean.getNewIntimationDTO().getHospitalDto().setClaimsReported(hospitaldetails.getCliamCount());
			bean.getNewIntimationDTO().getHospitalDto().setClaimsScored(hospitaldetails.getScoredClaimCount());
			bean.getNewIntimationDTO().getHospitalDto().setDeficiencyNotIn(hospitaldetails.getScorePercentage());
			//bean.getNewIntimationDTO().getHospitalDto().setFinalScore(hospitaldetails.getFinalScoreDeficiency());
			bean.getNewIntimationDTO().getHospitalDto().setFinalScoreDeficiency(hospitaldetails.getFinalScoreDeficiency());
		}
		Label hospitalFlag2 = new Label("Hospital Score");
		hospitalFlag2.addStyleName("v-labelarea-Boldstyle");
		DBCalculationService dbService = new DBCalculationService();
		Label hospitalFlag3 = new Label();
		Link hSPoints = new Link();
		hSPoints.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		hSPoints.setWidth("25%");
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto()!=null && bean.getNewIntimationDTO().getHospitalDto().getHospitalCode()!=null){
			
			Map<String,Object> preauthDTO = dbService.getHospitalScorePoints(bean.getNewIntimationDTO().getHospitalDto().getHospitalCode());
			String color = preauthDTO.get(SHAConstants.HOSPITAL_SCORE_COLOR).toString();
			hSPoints.setCaption("<b style = 'color: "+color+";text-decoration: underline;'>"+preauthDTO.get(SHAConstants.HOSPITAL_SCORE_POINTS).toString()+"");
			hSPoints.setCaptionAsHtml(true);
			bean.setFinalScore(preauthDTO.get(SHAConstants.HOSPITAL_SCORE_POINTS).toString());
		}
		Label dummy = new Label();
		Label dummy1 = new Label();
		Label dummy2 = new Label();
		HorizontalLayout pointsLayout = new HorizontalLayout(dummy,dummy1,dummy2,hSPoints);
		HorizontalLayout hospitalSCore = new HorizontalLayout(hospitalFlag2,pointsLayout);
		hospitalSCore.setSpacing(true);
		HorizontalLayout hspointFlag = new HorizontalLayout(hospitalSCore);
		HorizontalLayout hFlag=new HorizontalLayout(hspointFlag,hospitalFlag3);
		VerticalLayout hospitalFlag=new VerticalLayout(hospitalFlag1,hFlag);
	    HorizontalLayout formLayout = new HorizontalLayout(hospitalFlag);
	    formLayout.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) { 
				Window popup = new Window("Hospital Scoring Details");
				popup.setWidth("30%");
				popup.setResizable(false);
				popup.setClosable(false);
				popup.center();
				Label noOfClaimsReported = new Label("Total No. claims Reported");
				Label noOfClaimsScored = new Label("No. of Claims Scored with Deficiency");
				Label deficiencyNoteIn = new Label("Score Percentage (No of Claims Scored/Reported)");
				Label finalScore = new Label("Final Score based on No of claims scored with deficiency");
				noOfClaimsReported.setWidth("100%");
				noOfClaimsScored.setWidth("100%");
				deficiencyNoteIn.setWidth("100%");
				finalScore.setWidth("100%");
				Label claimsReportedValue = new Label();
				claimsReportedValue.setWidth("100%");
				if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getHospitalDto() && null != bean.getNewIntimationDTO().getHospitalDto().getClaimsReported()) {
					claimsReportedValue.setValue(bean.getNewIntimationDTO().getHospitalDto().getClaimsReported().toString()); 
					}else{
						claimsReportedValue.setValue("-");
					}
				Label claimsScored = new Label();
				claimsScored.setWidth("100%");
				if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getHospitalDto() && null != bean.getNewIntimationDTO().getHospitalDto().getClaimsScored()) {
					claimsScored.setValue(bean.getNewIntimationDTO().getHospitalDto().getClaimsScored().toString()); 
					}else{
						claimsScored.setValue("-");
					}
				Label defNoted = new Label();
				defNoted.setWidth("100%");
				if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getHospitalDto() && null != bean.getNewIntimationDTO().getHospitalDto().getDeficiencyNotIn()) {
					//defNoted.setValue((df.format(bean.getNewIntimationDTO().getHospitalDto().getDeficiencyNotIn())).toString()+"%");
					defNoted.setValue(bean.getNewIntimationDTO().getHospitalDto().getDeficiencyNotIn());
				}else{
						defNoted.setValue("-");
					}
				Label finalSCr = new Label();
				finalSCr.setWidth("100%");
				if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getHospitalDto() && null != bean.getNewIntimationDTO().getHospitalDto().getFinalScoreDeficiency()) {
					//finalSCr.setValue((df.format(bean.getNewIntimationDTO().getHospitalDto().getFinalScoreDeficiency())).toString());
					finalSCr.setValue(bean.getNewIntimationDTO().getHospitalDto().getFinalScoreDeficiency());
					//finalSCr.setValue(bean.getFinalScore());
					}else{
						finalSCr.setValue("-");
					}
				VerticalLayout layout1 = new VerticalLayout(noOfClaimsReported,noOfClaimsScored,deficiencyNoteIn,finalScore);
				VerticalLayout layout2 = new VerticalLayout(claimsReportedValue,claimsScored,defNoted,finalSCr);
				
				layout2.setStyleName("layoutDesign");
				FormLayout leftForm = new FormLayout(layout1);
				FormLayout rightForm = new FormLayout(layout2);
				HorizontalLayout resultLayout = new HorizontalLayout(leftForm,rightForm);
				Button close = new Button("Close");
				VerticalLayout resultLayout1 = new VerticalLayout(resultLayout,close);
				resultLayout1.setComponentAlignment(close, Alignment.BOTTOM_CENTER);
				close.setStyleName(ValoTheme.BUTTON_DANGER);
				Panel panel = new Panel(resultLayout1);
				FormLayout resultForm = new FormLayout(resultLayout1);
				resultForm.setStyleName("layoutDesign");
				popup.setContent(resultForm);
				close.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						popup.close();
					}
				});
				 popup.setModal(true);
					UI.getCurrent().addWindow(popup);
			}
		});
	   
	   
		return formLayout;
	}
	
	//For ICR agent and brnach score display
	public static HorizontalLayout icrAgentBranch(PreauthDTO bean) {
		Label icr = new Label();
		icr.setCaption("<b style='color:red; font-weight: bold; font-size:19px'> ICR % :&nbsp </b>");
		icr.setCaptionAsHtml(true);
		Link icrAgent = new Link();
		icrAgent.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		icrAgent.setCaption("<b style='color:green; font-weight: bold; font-size:19px'>AG- </b>");
		icrAgent.setCaptionAsHtml(true);
		icrAgent.setDescription("Agent");
		Label icrAgentScore = new Label();
		Label icrBranch = new Label();
		icrBranch.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		icrBranch.setCaption("<b style='color:green; font-weight: bold; font-size:19px'>BR- </b>");
		icrBranch.setCaptionAsHtml(true);
		icrBranch.setDescription("Branch");
		Label icrBranchScore = new Label();
		
		Label icrSMScore = new Label();
		Link icrSM = new Link();
		icrSM.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		icrSM.setCaption("<b style='color:green; font-weight: bold; font-size:19px'>SM- </b>");
		icrSM.setCaptionAsHtml(true);
		icrSM.setDescription("SM");
		
		DBCalculationService dbService = new DBCalculationService();
		
		String agentPrvPoint = "-";
		String smPrvPoint = "-";
		Label agentPrvPoints = new Label();
		Label smPrvPoints = new Label();
		
		if(bean.getNewIntimationDTO().getIntimationId() != null) {
			Map<String,Object> icrValues = dbService.getAgentAndBranchName(bean.getNewIntimationDTO().getIntimationId());
			if(icrValues != null && !icrValues.isEmpty()) {
			String agentScorePoint = icrValues.get(SHAConstants.ICR_AGENT_POINT).toString();
			String agentColor = icrValues.get(SHAConstants.ICR_AGENT_COLOR).toString();
			String branchScorePoint = icrValues.get(SHAConstants.ICR_BRANCH_POINT).toString();
			String branchColor = icrValues.get(SHAConstants.ICR_BRANCH_COLOR).toString();
			
			String smScorePoint = icrValues.get(SHAConstants.SM_AGENT_POINT).toString();
			String smColour = icrValues.get(SHAConstants.SM_COLOUR_CODE).toString();
			
			agentPrvPoint = icrValues.get(SHAConstants.AGENT_PRV_POINT).toString();
			smPrvPoint = icrValues.get(SHAConstants.SM_AGENT_PRV_POINT).toString();
			/*String agentColor = "#FF0000";
			String branchColor ="#FF0000";
			String agentScorePoint = "100.0";
			String branchScorePoint = "100.0";*/
			icrAgentScore.setCaption("<b style='color:"+agentColor+"; font-weight: bold; font-size:19px'>"+agentScorePoint+"&nbsp");
			icrAgentScore.setCaptionAsHtml(true);
			icrBranchScore.setCaption("<b style = 'color:"+branchColor+"; font-weight: bold; font-size:19px'>"+branchScorePoint+"&nbsp");
			icrBranchScore.setCaptionAsHtml(true);
			
			icrSMScore.setCaption("<b style='color:"+smColour+"; font-weight: bold; font-size:19px'>"+smScorePoint+"</b>");
			icrSMScore.setCaptionAsHtml(true);
			
			agentPrvPoints.setCaption("Previous Year ICR : "+ agentPrvPoint);
			smPrvPoints.setCaption("Previous Year ICR : "+ smPrvPoint);
			}
		}
		Label dummy = new Label();
		Label dummy1 = new Label();
		Label dummy2 = new Label();
		HorizontalLayout agentLayout = new HorizontalLayout(icrAgent,icrAgentScore);
		
		agentLayout.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) { 
				Window popup = new Window("Previous Year ICR:");
				popup.setWidth("15%");
				popup.setResizable(false);
				popup.setClosable(false);
				popup.center();
				HorizontalLayout resultLayout = new HorizontalLayout(agentPrvPoints);
				Button close = new Button("Close");
				VerticalLayout resultLayout1 = new VerticalLayout(resultLayout,close);
				resultLayout1.setComponentAlignment(close, Alignment.BOTTOM_CENTER);
				close.setStyleName(ValoTheme.BUTTON_DANGER);
				popup.setContent(resultLayout1);
				close.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						popup.close();
					}
				});
				 popup.setModal(true);
					UI.getCurrent().addWindow(popup);
			}
		});
		
		HorizontalLayout smLayout = new HorizontalLayout(icrSM,icrSMScore);
		
		smLayout.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) { 
				Window popup = new Window("Previous Year ICR:");
				popup.setWidth("15%");
				popup.setResizable(false);
				popup.setClosable(false);
				popup.center();
				HorizontalLayout resultLayout = new HorizontalLayout(smPrvPoints);
				Button close = new Button("Close");
				VerticalLayout resultLayout1 = new VerticalLayout(resultLayout,close);
				resultLayout1.setComponentAlignment(close, Alignment.BOTTOM_CENTER);
				close.setStyleName(ValoTheme.BUTTON_DANGER);
				popup.setContent(resultLayout1);
				close.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						popup.close();
					}
				});
				 popup.setModal(true);
					UI.getCurrent().addWindow(popup);
			}
		});
		
		HorizontalLayout icrLayout = new HorizontalLayout(icr,dummy,agentLayout/*icrAgent,icrAgentScore*/,dummy1,icrBranch,icrBranchScore,dummy2,smLayout/*icrSM,icrSMScore*/);
		 
		return icrLayout;
	}
	
	public static HorizontalLayout buyBackPed(PreauthDTO bean) {

		Label buyBackPedLabel = new Label();
		buyBackPedLabel.setCaption("&nbsp Buy Back PED Opted - ");
		buyBackPedLabel.setCaptionAsHtml(true);
		Label buyBackPedValue = new Label();
		String buyBackPedFlag = "No";

		if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null &&
				bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(SHAConstants.PRODUCT_CODE_78)){
			String buyBackPed = bean.getNewIntimationDTO().getInsuredPatient().getBuyBackPed();

			if(buyBackPed != null && buyBackPed.equals(SHAConstants.YES_FLAG)){
				buyBackPedFlag = "Yes";
			}
			else{
				buyBackPedFlag = "No";
			}
		}
		else if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null &&
				bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equals(SHAConstants.PRODUCT_CODE_88)){
			String buyBackPed = bean.getNewIntimationDTO().getInsuredPatient().getBuyBackPed();

			if(buyBackPed != null && buyBackPed.equals(SHAConstants.YES_FLAG)){
				buyBackPedFlag = "Yes";
			}
			else{
				buyBackPedFlag = "No";
			}
		}
		
		Label dummy = new Label();
		String branchColor ="#FF0000";
		buyBackPedValue.setCaption("<b style = 'color:"+branchColor+"'>"+buyBackPedFlag+"&nbsp");
		buyBackPedValue.setCaptionAsHtml(true);
		HorizontalLayout buyBackPedLayout = new HorizontalLayout(buyBackPedLabel,dummy,buyBackPedValue);
		return buyBackPedLayout;

	}
	
	//Due to f8 popup create a new class name CrmFlaggedComponents
/*	public static HorizontalLayout crmFlaggedLayout(String reason, String remark) {
		
		TextField crmFlaggedReason = new TextField("CRM Flagged Reason");
		//approvalStatus.setHeight("50px");
		crmFlaggedReason.setValue(reason);
		crmFlaggedReason.setNullRepresentation("");
		crmFlaggedReason.setEnabled(false);
		crmFlaggedReason.setReadOnly(Boolean.TRUE);
		
		TextArea crmFlaggedRemarks = new TextArea("CRM Flagged Remarks");
		//premedicalRemarks.setHeight("50px");
		crmFlaggedRemarks.setValue(remark);
		crmFlaggedRemarks.setNullRepresentation("");
		crmFlaggedRemarks.setId("flag");
		crmFlaggedRemarks.setEnabled(false);
		crmFlaggedRemarks.setReadOnly(Boolean.TRUE);
		crmFlaggedRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		crmFlaggedRemarks.setData(remark);
		
		FormLayout sugessionform =new FormLayout( crmFlaggedReason ,crmFlaggedRemarks);
		HorizontalLayout horizontalLayout = new HorizontalLayout(sugessionform);
		horizontalLayout.setSpacing(true);
		//horizontalLayout.setHeight("40%");
		return horizontalLayout;
	}*/

public static Boolean isLotCreatedAndBatchNotCreated(ClaimPayment claimPayment ) {
	if(null != claimPayment && (claimPayment.getStatusId().getKey().equals(ReferenceTable.LOT_CREATED_STATUS) && 
			(null != claimPayment.getLotNumber() && claimPayment.getBatchNumber() == null))) {
		return true;
	}else if(null != claimPayment && claimPayment.getStatusId().getKey().equals(ReferenceTable.PAYMENT_LVL1_TYPE_SEARCH_BATCH)){
		return true;
	}
	return false;
}

public static Boolean isBatchCreated(Reimbursement reimbursement,ClaimPayment claimPayment ) {
	if(claimPayment.getStatusId().getKey().equals(ReferenceTable.BATCH_CREATED_STATUS) && 
			(claimPayment ==null || (claimPayment != null && claimPayment.getLotNumber() != null && claimPayment.getBatchNumber()!= null))) {
		return true;
	}
	return false;
}

public static String getBillClassificationValue(DocAcknowledgement docAck) {
	StringBuilder strBuilder = new StringBuilder();
	// StringBuilder amtBuilder = new StringBuilder();
	// Double total = 0d;
	try {
		if (("Y").equals(docAck.getHospitalisationFlag())) {
			strBuilder.append("Hospitalization");
			strBuilder.append(",");
		}
		if (("Y").equals(docAck.getPreHospitalisationFlag())) {
			strBuilder.append("Pre-Hospitalization");
			strBuilder.append(",");
		}
		if (("Y").equals(docAck.getPostHospitalisationFlag())) {
			strBuilder.append("Post-Hospitalization");
			strBuilder.append(",");
		}

		if (("Y").equals(docAck.getPartialHospitalisationFlag())) {
			strBuilder.append("Partial-Hospitalization");
			strBuilder.append(",");
		}

		if (("Y").equals(docAck.getLumpsumAmountFlag())) {
			strBuilder.append("Lumpsum Amount");
			strBuilder.append(",");

		}
		if (("Y").equals(docAck.getHospitalCashFlag())) {
			strBuilder.append("Add on Benefits (Hospital cash)");
			strBuilder.append(",");

		}
		if (("Y").equals(docAck.getPatientCareFlag())) {
			strBuilder.append("Add on Benefits (Patient Care)");
			strBuilder.append(",");
		}
		if (("Y").equals(docAck.getHospitalizationRepeatFlag())) {
			strBuilder.append("Hospitalization Repeat");
			strBuilder.append(",");
		}
		
		if (("Y").equals(docAck.getCompassionateTravel())) {
			strBuilder.append("Compassionate Travel");
			strBuilder.append(",");
		}
		
		if (("Y").equals(docAck.getRepatriationOfMortalRemain())) {
			strBuilder.append("Repatriation Of Mortal Remains");
			strBuilder.append(",");
		}
		
		if(null != docAck.getClaim()&& docAck.getClaim().getIntimation() != null && docAck.getClaim().getIntimation().getPolicy() != null &&
				(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey())
						|| ReferenceTable.getValuableServiceProviderForFHO().containsKey(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey()))){
			if (("Y").equals(docAck.getPreferredNetworkHospita())) {
				strBuilder.append("Valuable Service Provider (Hospital)");
				strBuilder.append(",");
			}
		}
		else{ 
			if (("Y").equals(docAck.getPreferredNetworkHospita())) {
				strBuilder.append("Preferred Network Hospital");
				strBuilder.append(",");
			}
		}
		
		if (("Y").equals(docAck.getSharedAccomodation())) {
			strBuilder.append("Shared Accomodation");
			strBuilder.append(",");
		}
		
		if (("Y").equals(docAck.getEmergencyMedicalEvaluation())) {
			strBuilder.append("Emergency Medical Evacuation");
			strBuilder.append(",");
		}
		if (("Y").equals(docAck.getStarWomenCare())) {
			strBuilder.append("Star Mother Cover");
			strBuilder.append(",");
		}
		// rodQueryDTO.setClaimedAmount(total);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return strBuilder.toString();
}


public static BeanItemContainer<SelectValue> getInvAssignStatusContainer(){
	BeanItemContainer<SelectValue> invAssignStatusContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	invAssignStatusContainer.addBean(new SelectValue(ReferenceTable.ASSIGN_INVESTIGATION,SHAConstants.ASSIGN_INVESTIGATION_REPORT_TYPE));
	invAssignStatusContainer.addBean(new SelectValue(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED, SHAConstants.UPLOAD_INVESTIGATION_COMPLETED_REPORT_TYPE));
	invAssignStatusContainer.addBean(new SelectValue(ReferenceTable.PARALLEL_INVES_CANCELLED, SHAConstants.INVESTIGATION_CANCEL));
	return invAssignStatusContainer;
}

	public static WeakHashMap<Long, Long> getOmpClassificationMap(){
		
		WeakHashMap<Long, Long> ompClassificationMap = new WeakHashMap<Long, Long>();
		ompClassificationMap.put(ReferenceTable.OMP_CLAIM_RELATED_CLASSIFICATION_KEY, ReferenceTable.OMP_CLAIM_RELATED_SUB_CLASSIFICATION_KEY);
		ompClassificationMap.put(ReferenceTable.OMP_NEGOTIATOR_CLASSIFICATION_KEY,ReferenceTable.OMP_NEGOTIATOR_SUB_CLASSIFICATION_KEY);
	  
	  return ompClassificationMap;
}
	public static String formatDoctorActivityDateTime(Date date) {
		String strDate = "";
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm aaa");
			strDate = dateFormat.format(date);
		}
		return strDate;
	}
	
	public static String formatDateForHold(String date){
		Date strDate = null;
		if (date != null) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSSSSS");
		SimpleDateFormat correctFormat = new SimpleDateFormat(
				"dd/MM/yyyy hh:mm");
		try {
			strDate = dateFormat.parse(date);
			String format = correctFormat.format(strDate);
			return format;
		} catch (ParseException e) {		
 			e.printStackTrace();
			return null;
		}
			
		}
		return null;
	}
	public static  void handleTextAreaPopup(TextArea searchField, final  Listener listener, final UI ui) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForOpinionRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForOpinion(searchField, getShortCutListenerForOpinionRemarks(searchField,ui));

	}
	
	public static  void handleShortcutForOpinion(final TextArea textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);

			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}
	
	private static ShortcutListener getShortCutListenerForOpinionRemarks(final TextArea txtFld,final UI ui)
	{
		ShortcutListener listener =  new ShortcutListener("Opinion Remarks",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(2000);
				txtArea.setReadOnly(false);
				txtArea.setRows(25);

				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
						PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
						if(null != mainDto){
							mainDto.getPreauthMedicalDecisionDetails().setRemarksFromDeptHead(txtFld.getValue());
						}
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();
				
				String strCaption = "Opinion Given";
				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(ui.getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				ui.getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};

		return listener;
	}
	
	@SuppressWarnings("static-access")
	public static Boolean alertMessageForStopProcess(String policyNumber,UI ui) {
		
		Boolean result = Boolean.FALSE;
		String remarks = null;
		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<String, String> callStopProcess = dbCalculationService.callStopProcess(policyNumber);
		if(callStopProcess != null && ! callStopProcess.isEmpty()){
			if(callStopProcess.get(SHAConstants.PROCESS_FLAG) != null && callStopProcess.get(SHAConstants.PROCESS_FLAG).equalsIgnoreCase("Y")){
				result = Boolean.TRUE;
				remarks = callStopProcess.get(SHAConstants.PROCESS_REMARKS);
			}else{
				return result;
			}
		}
		if(! result){
			return result;
		}
		//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
		Label successLabel = new Label("<b style = 'color: red;'>"+remarks, ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		/*HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);*/
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(ui.getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
		
		return result;

	}
	
	public static  void handleTextAreaPopupDetails(TextArea searchField, final  Listener listener, final UI ui,final String remarksType) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForCancelRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForCancel(searchField, getShortCutListenerForCancelRemarks(searchField,ui,remarksType));

	}
	
	public static  void handleShortcutForCancel(final TextArea textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);

			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}
	
	private static ShortcutListener getShortCutListenerForCancelRemarks(final TextArea txtFld,final UI ui,final String remarksType)
	{
		ShortcutListener listener =  new ShortcutListener("Cancel Remarks",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				if(null != remarksType && SHAConstants.NEGOTIATION_DOWNSIZE_REMARKS.equalsIgnoreCase(remarksType)){
					txtArea.setMaxLength(1000);
				}else if(null != remarksType && SHAConstants.NEGOTIATION_DOWNSIZE_INSURED_REMARKS.equalsIgnoreCase(remarksType)){
					txtArea.setMaxLength(1000);
				} else if(null != remarksType && SHAConstants.NEGOWITH.equalsIgnoreCase(remarksType)){
					txtArea.setMaxLength(500);
				}else if(null != remarksType && SHAConstants.CLAIMS_ALERT.equalsIgnoreCase(remarksType)){
					txtArea.setMaxLength(500);
				}else if(null != remarksType && SHAConstants.IMPLANT_TYPE.equalsIgnoreCase(remarksType)){
					txtArea.setMaxLength(100);
				}else if(null != remarksType && SHAConstants.IMPLANT_NAME.equalsIgnoreCase(remarksType)){
					txtArea.setMaxLength(100);
				}else if(null != remarksType && SHAConstants.ICAC_REMARKS.equalsIgnoreCase(remarksType)){
					txtArea.setMaxLength(3000);
				} else if(null != remarksType && SHAConstants.TIMELY_EXCLUDED_ICD_CODE_REMARKS.equalsIgnoreCase(remarksType)){
					txtArea.setMaxLength(500);
				}else if(null != remarksType && SHAConstants.ESCALATE_PCC_REMARKS.equalsIgnoreCase(remarksType)){
					txtArea.setMaxLength(200);
				} else if(null != remarksType && SHAConstants.STP_REMARKS.equalsIgnoreCase(remarksType)){
					txtArea.setMaxLength(1000);
				}else if(null != remarksType && SHAConstants.OP_ALLOW_CLAIM_PROCESSING_REMARKS.equalsIgnoreCase(remarksType)){
					txtArea.setMaxLength(1000);
				}else if(null != remarksType && SHAConstants.FILE_UPLOAD_REMARKS.equalsIgnoreCase(remarksType)){
						txtArea.setMaxLength(1000);
				} else if(null != remarksType && SHAConstants.PA_REJECTION_REMARKS.equalsIgnoreCase(remarksType)){
					txtArea.setMaxLength(8000);	
				} else if(null != remarksType && SHAConstants.REMARKS_BILL_ENTRY.equalsIgnoreCase(remarksType)){
					txtArea.setMaxLength(4000);	
				} else if(null != remarksType && SHAConstants.TALK_TALK_TALK_REMARK.equalsIgnoreCase(remarksType)){
					txtArea.setMaxLength(4000);	
				}
				else {
					txtArea.setMaxLength(4000);
				}
				if(null != remarksType && SHAConstants.DATA_CORECTION_VIEW.equalsIgnoreCase(remarksType)){
					txtArea.setReadOnly(true);
				}else{
					txtArea.setReadOnly(false);
				}
				txtArea.setRows(25);

				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
						PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
						if(null != mainDto){
							mainDto.setAutoAllocCancelRemarks(txtFld.getValue());
						}
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();
				String strCaption = "";
				if(null != remarksType && SHAConstants.STP_REMARKS.equalsIgnoreCase(remarksType)){
					strCaption = " Remarks";
				} else if(null != remarksType && SHAConstants.STP_NEGOTIATION_POINTS_REMARKS.equalsIgnoreCase(remarksType)){
					strCaption = " Points to negotiate";
				} else if(null != remarksType && SHAConstants.NEG_AGREED_REMARKS.equalsIgnoreCase(remarksType)){
					strCaption = " Agreed with";
				} else if(null != remarksType && SHAConstants.EMAIL_ID.equalsIgnoreCase(remarksType)){
					strCaption = "Email Id";
				} else if(null != remarksType && SHAConstants.NEGOTIATION_DOWNSIZE_REMARKS.equalsIgnoreCase(remarksType)){
					strCaption = "Downsize Remarks";
				}else if(null != remarksType && SHAConstants.NEGOTIATION_DOWNSIZE_INSURED_REMARKS.equalsIgnoreCase(remarksType)){
					strCaption = "Downsize Insured Remarks";
				}else if(null != remarksType && SHAConstants.ESCALATE_PCC_REMARKS.equalsIgnoreCase(remarksType)){
					strCaption = "Escalate PCC Remarks";
				}else if(null != remarksType && SHAConstants.DISALLOWANCE_REMARKS.equalsIgnoreCase(remarksType)){
					strCaption = "Disallowance Remarks";
				}else if(null != remarksType && SHAConstants.RRC_REMARK.equalsIgnoreCase(remarksType)){
					strCaption = "RRC Remarks";
				}else if(null != remarksType && SHAConstants.NEGOWITH.equalsIgnoreCase(remarksType)){
					strCaption = "Negotiated With";
				}else if(null != remarksType && SHAConstants.CLAIMS_ALERT.equalsIgnoreCase(remarksType)){
					strCaption = "Claim Alert";
				}else if(null != remarksType && SHAConstants.FLP_HOLD_REMARK.equalsIgnoreCase(remarksType)){
					strCaption = "FLP Hold Remark";
				}else if(null != remarksType && SHAConstants.DATA_CORECTION.equalsIgnoreCase(remarksType)){
					strCaption = "Data Correction Remark";
				}else if(null != remarksType && SHAConstants.DATA_CORECTION_VIEW.equalsIgnoreCase(remarksType)){
					strCaption = "Data Correction Remark";
				}else if(null != remarksType && SHAConstants.IMPLANT_TYPE.equalsIgnoreCase(remarksType)){
					strCaption = "IMPLANT TYPE";
				}else if(null != remarksType && SHAConstants.IMPLANT_NAME.equalsIgnoreCase(remarksType)){
					strCaption = "IMPLANT NAME";
				}else if(null != remarksType && SHAConstants.ICAC_REMARKS.equalsIgnoreCase(remarksType)){
					strCaption = "Refer to ICAC Remarks";
				}else if(null != remarksType && SHAConstants.TIMELY_EXCLUDED_ICD_CODE_REMARKS.equalsIgnoreCase(remarksType)){
					strCaption = "TIMELY EXCLUDED ICD CODE REMARKS";
				}
				else if(null != remarksType && SHAConstants.HRM_PROCESSING_REMARKS.equalsIgnoreCase(remarksType)){
					strCaption = "Long Stay Claims Remarks";
				}else if(null != remarksType && SHAConstants.OP_ALLOW_CLAIM_PROCESSING_REMARKS.equalsIgnoreCase(remarksType)){
					strCaption = "Remarks for allowing the expired policy for claim processing";
				}else if(null != remarksType && SHAConstants.FILE_UPLOAD_REMARKS.equalsIgnoreCase(remarksType)){
					strCaption = "PCC FILE UPLOAD REMARKS";
				}else if(null != remarksType && SHAConstants.PA_REJECTION_REMARKS.equalsIgnoreCase(remarksType)){
					strCaption = "REJECTION REMARKS";
				}else if(null != remarksType && SHAConstants.REMARKS_BILL_ENTRY.equalsIgnoreCase(remarksType)){
					strCaption = "REMARKS BILL ENTRY";
				}else if(null != remarksType && SHAConstants.TALK_TALK_TALK_REMARK.equalsIgnoreCase(remarksType)){
					strCaption = "REMARKS";
				}
				else{
					strCaption = "Cancel Remarks";
				}
				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(ui.getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				ui.getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};

		return listener;
	}

public static void buildEsclateToRawView(Instance<EsclateClaimToRawPage> esclateClaimToRawPageInstance,EsclateClaimToRawPage esclateClaimToRawPageViewObj,PreauthDTO bean,String screenName){
		
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("85%");
		popup.setHeight("90%");
		esclateClaimToRawPageViewObj = esclateClaimToRawPageInstance.get();
		esclateClaimToRawPageViewObj.initPresenter(screenName);
		esclateClaimToRawPageViewObj.init(bean, popup);

		popup.setCaption("Escalate To RAW");
		popup.setContent(esclateClaimToRawPageViewObj);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}   

public static  void handlePopUpFeedbackRemarks(TextArea searchField, final  Listener listener, final UI ui,final String feedbackFor) {

	ShortcutListener enterShortCut = new ShortcutListener(
			"ShortcutForFeedbackRemarks", ShortcutAction.KeyCode.F8, null) {


		private static final long serialVersionUID = 1L;
		@Override
		public void handleAction(Object sender, Object target) {
			((ShortcutListener) listener).handleAction(sender, target);
		}
	};
	handleShortcutForFeedbackReply(searchField, getShortCutListenerForReply(searchField,ui,feedbackFor));

}

public static  void handleShortcutForFeedbackReply(final TextArea textField, final ShortcutListener shortcutListener) {
	textField.addFocusListener(new FocusListener() {

		@Override
		public void focus(FocusEvent event) {
			textField.addShortcutListener(shortcutListener);

		}
	});
	textField.addBlurListener(new BlurListener() {

		@Override
		public void blur(BlurEvent event) {

			textField.removeShortcutListener(shortcutListener);

		}
	});
}

private static ShortcutListener getShortCutListenerForReply(final TextArea txtFld,final UI ui,final String feedbackFor)
{
	ShortcutListener listener =  new ShortcutListener("Description",KeyCodes.KEY_F8,null) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void handleAction(Object sender, Object target) {
			BranchManagerFeedbackTableDTO  searchPedDto = (BranchManagerFeedbackTableDTO) txtFld.getData();
			VerticalLayout vLayout =  new VerticalLayout();
			
			vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
			vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
			vLayout.setMargin(true);
			vLayout.setSpacing(true);
			TextArea txtArea = new TextArea();
			txtArea.setMaxLength(4000);
			txtArea.setNullRepresentation("");
			txtArea.setValue(txtFld.getValue());
			
			txtArea.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					TextArea txt = (TextArea)event.getProperty();
					txtFld.setValue(txt.getValue());
				}
			});
			
			
			txtArea.setSizeFull();
			txtArea.setWidth("100%");
			txtArea.setRows(25);
			if(feedbackFor.equals(SHAConstants.FEEDBACK_POLICY)) {
			searchPedDto.setTechnicalTeamReply(txtArea.getValue());
			}else if(feedbackFor.equals(SHAConstants.FEEDBACK_PROPOSAL)) {
				searchPedDto.setCorpTeamReply(txtArea.getValue());
			}else if(feedbackFor.equals(SHAConstants.FEEDBACK_CLAIM)) {
				searchPedDto.setClaimsDepartmentReply(txtArea.getValue());
			}
			
			Button okBtn = new Button("OK");
			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			vLayout.addComponent(txtArea);
			vLayout.addComponent(okBtn);
			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
			
			final Window dialog = new Window();
			dialog.setCaption("Remarks");
			dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
			dialog.setWidth("45%");
			dialog.setClosable(false);
			
			dialog.setContent(vLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.setDraggable(true);
			
			if(ui.getCurrent().getPage().getWebBrowser().isIE()) {
				dialog.setPositionX(250);
				dialog.setPositionY(100);
			}
			ui.getCurrent().addWindow(dialog);
			
			okBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});	
		}
	};
	
	return listener;
}

public static WeakHashMap getBMFBRatingImgMap(){
	
	WeakHashMap<Double,String> feedbackRatingMap = new WeakHashMap<Double, String>();
	
	feedbackRatingMap.put(0.0, "EMTStarRating_Blue.jpg");
	feedbackRatingMap.put(0.5, "HalfStarRating_Blue.jpg");
	feedbackRatingMap.put(1.0, "OneStarRating_Blue.jpg");
	feedbackRatingMap.put(1.5, "OneNHalfStarRating_Blue.jpg");
	feedbackRatingMap.put(2.0, "TwoStarRating_Blue.jpg");
	feedbackRatingMap.put(2.5, "TwoANHalfStarRating_Blue.jpg");
	feedbackRatingMap.put(3.0, "ThreeStarRating_Blue.jpg");
	feedbackRatingMap.put(3.5, "ThreeANHalfStarRating_Blue.jpg");
	feedbackRatingMap.put(4.0, "FourStarRating_Blue.jpg");
	feedbackRatingMap.put(4.5, "FourANHalfStarRating_Blue.jpg");
	feedbackRatingMap.put(5.0, "FiveStarRating_Blue.jpg");
			
	return feedbackRatingMap;
}

public static WeakHashMap<Integer, String> getBMFBRptPatternImgMap(){
	WeakHashMap<Integer, String> feedbackRptPatternMap = new WeakHashMap<Integer, String>();
	feedbackRptPatternMap.put(1, "ReportedTick.jpg");
	feedbackRptPatternMap.put(0, "NotReportedCross.jpg");
	feedbackRptPatternMap.put(-2, "Future.jpg");
	
	return feedbackRptPatternMap;
}

private static ShortcutListener getShortCutListenerForReply(final TextArea txtFld,final UI ui)
{
	ShortcutListener listener =  new ShortcutListener("Description",KeyCodes.KEY_F8,null) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void handleAction(Object sender, Object target) {
			TopUpPolicyMasterTableDTO  searchPedDto = (TopUpPolicyMasterTableDTO) txtFld.getData();
			VerticalLayout vLayout =  new VerticalLayout();
			
			vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
			vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
			vLayout.setMargin(true);
			vLayout.setSpacing(true);
			TextArea txtArea = new TextArea();
			txtArea.setMaxLength(4000);
			txtArea.setNullRepresentation("");
			txtArea.setValue(txtFld.getValue());
			
			txtArea.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					TextArea txt = (TextArea)event.getProperty();
					txtFld.setValue(txt.getValue());
				}
			});
			
			
			txtArea.setSizeFull();
			txtArea.setWidth("100%");
			txtArea.setRows(25);
			searchPedDto.setRemarks(txtArea.getValue());
			
			Button okBtn = new Button("OK");
			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			vLayout.addComponent(txtArea);
			vLayout.addComponent(okBtn);
			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
			
			final Window dialog = new Window();
			dialog.setCaption("Remarks");
			dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
			dialog.setWidth("45%");
			dialog.setClosable(false);
			
			dialog.setContent(vLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.setDraggable(true);
			
			if(ui.getCurrent().getPage().getWebBrowser().isIE()) {
				dialog.setPositionX(250);
				dialog.setPositionY(100);
			}
			ui.getCurrent().addWindow(dialog);
			
			okBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});	
		}
	};
	
	return listener;
}

public static  void handleShortcutForTopUpPolicy(TextArea searchField, final  Listener listener, final UI ui) {

	ShortcutListener enterShortCut = new ShortcutListener(
			"ShortcutForFeedbackRemarks", ShortcutAction.KeyCode.F8, null) {


		private static final long serialVersionUID = 1L;
		@Override
		public void handleAction(Object sender, Object target) {
			((ShortcutListener) listener).handleAction(sender, target);
		}
	};
	handleShortcutForTopUpPolicyRemarks(searchField, getShortCutListenerForTopUp(searchField,ui));

}

public static  void handleShortcutForTopUpPolicyRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
	textField.addFocusListener(new FocusListener() {

		@Override
		public void focus(FocusEvent event) {
			textField.addShortcutListener(shortcutListener);

		}
	});
	textField.addBlurListener(new BlurListener() {

		@Override
		public void blur(BlurEvent event) {

			textField.removeShortcutListener(shortcutListener);

		}
	});
}

private static ShortcutListener getShortCutListenerForTopUp(final TextArea txtFld,final UI ui)
{
	ShortcutListener listener =  new ShortcutListener("Description",KeyCodes.KEY_F8,null) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void handleAction(Object sender, Object target) {
			TopUpPolicyMasterTableDTO  searchPedDto = (TopUpPolicyMasterTableDTO) txtFld.getData();
			VerticalLayout vLayout =  new VerticalLayout();
			
			vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
			vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
			vLayout.setMargin(true);
			vLayout.setSpacing(true);
			TextArea txtArea = new TextArea();
			txtArea.setMaxLength(4000);
			txtArea.setNullRepresentation("");
			txtArea.setValue(txtFld.getValue());
			
			txtArea.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					TextArea txt = (TextArea)event.getProperty();
					txtFld.setValue(txt.getValue());
				}
			});
			
			
			txtArea.setSizeFull();
			txtArea.setWidth("100%");
			txtArea.setRows(25);
			searchPedDto.setRemarks(txtArea.getValue());
			
			Button okBtn = new Button("OK");
			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			vLayout.addComponent(txtArea);
			vLayout.addComponent(okBtn);
			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
			
			final Window dialog = new Window();
			dialog.setCaption("Remarks");
			dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
			dialog.setWidth("45%");
			dialog.setClosable(true);
			
			dialog.setContent(vLayout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.setDraggable(true);
			
			if(ui.getCurrent().getPage().getWebBrowser().isIE()) {
				dialog.setPositionX(250);
				dialog.setPositionY(100);
			}
			ui.getCurrent().addWindow(dialog);
			
			okBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});	
		}
	};
	
	return listener;
}

public static  void handleShortcutForFraudIdentification(TextArea searchField, final  Listener listener, final UI ui,final String remarksType) {

	ShortcutListener enterShortCut = new ShortcutListener(
			"ShortcutForUserRemarks", ShortcutAction.KeyCode.F8, null) {


		private static final long serialVersionUID = 1L;
		@Override
		public void handleAction(Object sender, Object target) {
			((ShortcutListener) listener).handleAction(sender, target);
		}
	};
	handleShortcutForFraudIdentificationRemarks(searchField, getShortCutListenerForFraudIdentification(searchField,ui,remarksType));

}
public static  void handleShortcutForFraudIdentificationRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
	textField.addFocusListener(new FocusListener() {

		@Override
		public void focus(FocusEvent event) {
			textField.addShortcutListener(shortcutListener);

		}
	});
	textField.addBlurListener(new BlurListener() {

		@Override
		public void blur(BlurEvent event) {

			textField.removeShortcutListener(shortcutListener);

		}
	});
}

private static ShortcutListener getShortCutListenerForFraudIdentification(final TextArea txtFld,final UI ui,final String remarksType)
{
	ShortcutListener listener =  new ShortcutListener("Description",KeyCodes.KEY_F8,null) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void handleAction(Object sender, Object target) {
			FraudIdentificationTableDTO  FraudIdDto = (FraudIdentificationTableDTO) txtFld.getData();
			VerticalLayout vLayout =  new VerticalLayout();
			
			vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
			vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
			vLayout.setMargin(true);
			vLayout.setSpacing(true);
			TextArea txtArea = new TextArea();
			txtArea.setMaxLength(4000);
			txtArea.setNullRepresentation("");
			txtArea.setValue(txtFld.getValue());
			
			txtArea.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					TextArea txt = (TextArea)event.getProperty();
					txtFld.setValue(txt.getValue());
				}
			});
			
			
			txtArea.setSizeFull();
			txtArea.setWidth("100%");
			txtArea.setRows(25);
			FraudIdDto.setUserRemarks(txtArea.getValue());
			
			Button okBtn = new Button("OK");
			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			vLayout.addComponent(txtArea);
			vLayout.addComponent(okBtn);
			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
			
			final Window dialog = new Window();
			String strCaption = "";
			if(null != remarksType && SHAConstants.USER_REMARKS.equalsIgnoreCase(remarksType)){
				dialog.setCaption("User Remarks");
			}
			else if(null != remarksType && SHAConstants.RECIPIENT_DETAILS_TO.equalsIgnoreCase(remarksType)){
				dialog.setCaption("RECIPIENT DETAILS(TO)");
			}
			else if(null != remarksType && SHAConstants.RECIPIENT_DETAILS_CC.equalsIgnoreCase(remarksType)){
				dialog.setCaption("RECIPIENT DETAILS(CC)");
			}else{
				dialog.setCaption("Remarks");
			}

			dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
			dialog.setWidth("45%");
			dialog.setClosable(true);
			
			dialog.setContent(vLayout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.setDraggable(true);
			
			if(ui.getCurrent().getPage().getWebBrowser().isIE()) {
				dialog.setPositionX(250);
				dialog.setPositionY(100);
			}
			ui.getCurrent().addWindow(dialog);
			
			okBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});	
		}
	};
	
	return listener;
}
public static  void shortCutForFeedbackRemarks(TextArea searchField, final  Listener listener, final UI ui) {

	ShortcutListener enterShortCut = new ShortcutListener(
			"ShortcutForFeedbackRemarks", ShortcutAction.KeyCode.F8, null) {


		private static final long serialVersionUID = 1L;
		@Override
		public void handleAction(Object sender, Object target) {
			((ShortcutListener) listener).handleAction(sender, target);
		}
	};
	handleShortcutForFeedback(searchField, getShortCutListenerForFeedBackRemarks(searchField,ui));

}

public static  void handleShortcutForFeedback(final TextArea textField, final ShortcutListener shortcutListener) {
	textField.addFocusListener(new FocusListener() {

		@Override
		public void focus(FocusEvent event) {
			textField.addShortcutListener(shortcutListener);

		}
	});
	textField.addBlurListener(new BlurListener() {

		@Override
		public void blur(BlurEvent event) {

			textField.removeShortcutListener(shortcutListener);

		}
	});
}


private static ShortcutListener getShortCutListenerForFeedBackRemarks(final TextArea txtFld,final UI ui)
{
	ShortcutListener listener =  new ShortcutListener("Description",KeyCodes.KEY_F8,null) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void handleAction(Object sender, Object target) {
			ManagerFeedBackPolicyTableDto  searchPedDto = (ManagerFeedBackPolicyTableDto) txtFld.getData();
			VerticalLayout vLayout =  new VerticalLayout();
			
			vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
			vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
			vLayout.setMargin(true);
			vLayout.setSpacing(true);
			TextArea txtArea = new TextArea();
			txtArea.setMaxLength(4000);
			txtArea.setNullRepresentation("");
			txtArea.setValue(txtFld.getValue());
			
			txtArea.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					TextArea txt = (TextArea)event.getProperty();
					txtFld.setValue(txt.getValue());
				}
			});
			
			
			txtArea.setSizeFull();
			txtArea.setWidth("100%");
			txtArea.setRows(25);
			searchPedDto.setRemarks(txtArea.getValue());
			
			Button okBtn = new Button("OK");
			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			vLayout.addComponent(txtArea);
			vLayout.addComponent(okBtn);
			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
			
			final Window dialog = new Window();
			dialog.setCaption("Remarks");
			dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
			dialog.setWidth("45%");
			dialog.setClosable(false);
			
			dialog.setContent(vLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.setDraggable(true);
			
			if(ui.getCurrent().getPage().getWebBrowser().isIE()) {
				dialog.setPositionX(250);
				dialog.setPositionY(100);
			}
			ui.getCurrent().addWindow(dialog);
			
			okBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});	
		}
	};
	
	return listener;
}

@SuppressWarnings("static-access")
public static Boolean alertMessageForProvisionValidation(Long stage, Long status, Long provisionAmt,Long policyKey,
		Long insuredKey, Long transactionKey, String transactionFlag,UI ui) {
	
	
	Boolean result = Boolean.FALSE;
	String remarks = null;
	DBCalculationService dbCalculationService = new DBCalculationService();
	Map<String, String> provisionUpdateValidation = dbCalculationService.getProvisionUpdateValidation(stage, status, provisionAmt, 
			policyKey, insuredKey, transactionKey, transactionFlag);
	if(provisionUpdateValidation != null && ! provisionUpdateValidation.isEmpty()){
		if(provisionUpdateValidation.get(SHAConstants.PROCESS_FLAG) != null && provisionUpdateValidation.get(SHAConstants.PROCESS_FLAG).equalsIgnoreCase("ERROR")){
			result = Boolean.TRUE;
			remarks = provisionUpdateValidation.get(SHAConstants.PROCESS_REMARKS);
		}else{
			return result;
		}
	}
	if(! result){
		return result;
	}
	//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
	Label successLabel = new Label("<b style = 'color: red;'>"+remarks, ContentMode.HTML);
	
//	Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
	
	Button homeButton = new Button("Ok");
	homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

	HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
	horizontalLayout.setMargin(true);
	horizontalLayout.setSpacing(true);
	
	VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
	layout.setSpacing(true);
	layout.setMargin(true);
	/*HorizontalLayout hLayout = new HorizontalLayout(layout);
	hLayout.setMargin(true);*/
	
	final ConfirmDialog dialog = new ConfirmDialog();
	dialog.setCaption("");
	dialog.setClosable(false);
	dialog.setContent(layout);
	dialog.setResizable(false);
	dialog.setModal(true);
	dialog.show(ui.getCurrent(), null, true);
	
	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			dialog.close();
		}
	});
	
	return result;

}

public static  void handleTextAreaPopUp(TextArea searchField, final  Listener listener, final UI ui,final String remarksType,final PreauthDTO bean) {

	ShortcutListener enterShortCut = new ShortcutListener(
			"ShortcutForRemarks", ShortcutAction.KeyCode.F8, null) {

		private static final long serialVersionUID = 1L;
		@Override
		public void handleAction(Object sender, Object target) {
			((ShortcutListener) listener).handleAction(sender, target);
		}
	};
	handleShortcutForRemarks(searchField, getShortCutListenerForRemarks(searchField,ui,remarksType,bean));

}

public static  void handleShortcutForRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
	textField.addFocusListener(new FocusListener() {

		@Override
		public void focus(FocusEvent event) {
			textField.addShortcutListener(shortcutListener);

		}
	});
	textField.addBlurListener(new BlurListener() {

		@Override
		public void blur(BlurEvent event) {

			textField.removeShortcutListener(shortcutListener);

		}
	});
}

private static ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld,final UI ui,final String remarksType,final PreauthDTO bean)
{
	ShortcutListener listener =  new ShortcutListener("Cancel Remarks",KeyCodes.KEY_F8,null) {

		private static final long serialVersionUID = 1L;

		@SuppressWarnings({ "static-access", "deprecation" })
		@Override
		public void handleAction(Object sender, Object target) {
			VerticalLayout vLayout =  new VerticalLayout();

			vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
			vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
			vLayout.setMargin(true);
			vLayout.setSpacing(true);
			final TextArea txtArea = new TextArea();
			txtArea.setValue(txtFld.getValue());
			txtArea.setNullRepresentation("");
			txtArea.setSizeFull();
			txtArea.setWidth("100%");
			txtArea.setMaxLength(4000);
			txtArea.setReadOnly(false);
			txtArea.setRows(25);

			txtArea.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					txtFld.setValue(((TextArea)event.getProperty()).getValue());
					PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
					if(null != mainDto){
						
					}
				}
			});
			
			if(null != bean.getIsNegotiationApplicable() && bean.getIsNegotiationApplicable()){
				if(bean.getIsNegotiationPending()){
					txtArea.setReadOnly(true);
				}
				
			}
			Button okBtn = new Button("OK");
			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			vLayout.addComponent(txtArea);
			vLayout.addComponent(okBtn);
			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

			final Window dialog = new Window();
			String strCaption = "";
			strCaption = " Remarks";
	
			dialog.setCaption(strCaption);

			dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
			dialog.setWidth("45%");
			dialog.setClosable(true);

			dialog.setContent(vLayout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.setDraggable(true);
			dialog.setData(txtFld);

			dialog.addCloseListener(new Window.CloseListener() {
				@Override
				public void windowClose(CloseEvent e) {
					dialog.close();
				}
			});

			if(ui.getCurrent().getPage().getWebBrowser().isIE()) {
				dialog.setPositionX(250);
				dialog.setPositionY(100);
			}
			ui.getCurrent().addWindow(dialog);
			okBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});	
		}
	};

	return listener;
}

public static void buildNegotiationSuccessLayout(final UI ui,Boolean isCancel) {
	Label successLabel = null;
	
	if(isCancel){
		successLabel = new Label("<b style = 'color: green;'> Negotiation Cancelled Successfully.</b>",ContentMode.HTML);
	}
	else
	{
		successLabel = new Label("<b style = 'color: green;'> Negotiation Updated Successfully.</b>",ContentMode.HTML);
	}
	Button homeButton = new Button("Ok");
	homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
	layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
	layout.setSpacing(true);
	layout.setMargin(true);
	layout.setStyleName("borderLayout");
	final ConfirmDialog dialog = new ConfirmDialog();
	dialog.setCaption("");
	dialog.setClosable(false);
	dialog.setContent(layout);
	dialog.setResizable(false);
	dialog.setModal(true);
	dialog.show(ui.getCurrent(), null, true);

	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			dialog.close();
		}
	});
}

public static String getBillAssessmentVersion(EntityManager em, String rodNumber, String docType, String scrcDocType){
	
	Query query = em.createNamedQuery("DocumentDetails.findBillAssessmentVersions"); 
	query = query.setParameter("reimbursementNumber", rodNumber);
	
	if(scrcDocType != null){
		scrcDocType = scrcDocType.toLowerCase();
	}
	
	if(docType != null){
		docType = docType.toLowerCase();
	}
	
	query = query.setParameter("billsummary", docType);
	query = query.setParameter("billassessment", scrcDocType);
	query = query.setParameter("financialapproval", SHAConstants.FINANCIAL_APPROVER.toLowerCase());
	List<DMSDocumentDetailsDTO> documentDetailsDTOList =  null;
	List<DocumentDetails> documentDetailsList  = query.getResultList();
	if(documentDetailsList != null && !documentDetailsList.isEmpty()) {
		return "V"+String.valueOf(documentDetailsList.size());
	}
	
	return "V0";
}

public static void showAlertMessageBox(String message){
    final MessageBox msgBox = MessageBox.createWarning()
			    .withCaptionCust("Information") .withHtmlMessage(message)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
		
}

public static void showMessageBox(String message){
    final MessageBox msgBox = MessageBox.createWarning()
			    .withCaptionCust("Warning") .withHtmlMessage(message)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
		
}

public static Object[] getRevisedArrayListForManualHospitalScoringDBCall(Intimation intimation,Hospitals hospital,Claim claim, String stageSource){
	Object[] resultArray = new Object[1];
	
	Object[] attributes = new Object[85];
	attributes[SHAConstants.INDEX_WORK_FLOW_KEY] = 0;
	attributes[SHAConstants.INDEX_INTIMATION_NO] = intimation.getIntimationId();
	attributes[SHAConstants.INDEX_INTIMATION_KEY] = intimation.getKey();
	attributes[SHAConstants.INDEX_INTIMATION_SOURCE] = intimation.getIntimationSource().getValue();
	attributes[SHAConstants.INDEX_CPU_CODE] =intimation.getCpuCode().getCpuCode();
	attributes[SHAConstants.INDEX_CLAIM_NUMBER] ="";
	attributes[SHAConstants.INDEX_CLAIM_KEY] =0;
	attributes[SHAConstants.INDEX_CLAIM_TYPE] = claim.getClaimType();
	attributes[SHAConstants.INDEX_BALANCE_SI_FLAG] = "Y";
	
//		DATE admissionDate = new DATE(claim.getIntimation().getAdmissionDate().getTime());
//		attributes[SHAConstants.INDEX_ADMISSION_DATE] = claim.getIntimation().getAdmissionDate();
	attributes[SHAConstants.INDEX_ADMISSION_DATE] = intimation.getAdmissionDate();
//	attributes[SHAConstants.INDEX_ADMISSION_DATE] = null;
		
//		DATE createdDate = new DATE(claim.getIntimation().getCreatedDate().getTime());
		attributes[SHAConstants.INDEX_INTIMATION_DATE] = intimation.getCreatedDate();
//		attributes[SHAConstants.INDEX_INTIMATION_DATE] = null;
		
	

	attributes[SHAConstants.INDEX_POLICY_KEY] =intimation.getPolicy().getKey();
	attributes[SHAConstants.INDEX_POLICY_NUMBER] = intimation.getPolicy().getPolicyNumber();
	attributes[SHAConstants.INDEX_PRODUCT_KEY] = intimation.getPolicy().getProduct().getKey();
	attributes[SHAConstants.INDEX_PRODUCT_NAME] = intimation.getPolicy().getProduct().getValue();
	attributes[SHAConstants.INDEX_LOB] = "HEALTH";
	attributes[SHAConstants.INDEX_LOB_TYPE] = "H";
	attributes[SHAConstants.INDEX_HOSPITAL_TYPE] =intimation.getHospitalType().getValue();
	attributes[SHAConstants.INDEX_NETWORK_TYPE] = hospital.getNetworkHospitalType();
	attributes[SHAConstants.INDEX_HOSPITAL_KEY] = hospital.getKey();
	attributes[SHAConstants.INDEX_TREATMENT_TYPE] = "";
	attributes[SHAConstants.INDEX_SPECIALITY_NAME] = "";
	attributes[SHAConstants.INDEX_PRIORITY] = "";
	attributes[SHAConstants.INDEX_RECORD_TYPE] = "FRESH";
	attributes[SHAConstants.INDEX_STAGE_SOURCE] =  (stageSource != null && stageSource != "" ) ? stageSource : intimation.getStage().getStageName();
	attributes[SHAConstants.INDEX_CASHLESS_NO] = "";
	attributes[SHAConstants.INDEX_CASHLESS_KEY] = 0;
	attributes[SHAConstants.INDEX_CLAIMED_AMT] = null;
	attributes[SHAConstants.INDEX_ROLE_ID] = "";
	attributes[SHAConstants.INDEX_ESCALATE_ROLE_ID] = 0l;
	attributes[SHAConstants.INDEX_ESCALATE_USER_ID] = "";
	attributes[SHAConstants.INDEX_PROCESS_TYPE] = "";
	attributes[SHAConstants.INDEX_RECONSIDERATION_FLAG] = "N";
//	attributes[SHAConstants.INDEX_REFERENCE_USER_ID] = "ADMINB";
	attributes[SHAConstants.INDEX_OUT_COME] = "REGNWFPA";
	DATE currentDate = new DATE();
	attributes[SHAConstants.INDEX_PROECSSED_DATE] = currentDate;
	attributes[SHAConstants.INDEX_USER_ID] = "ADMINB";
	
	attributes[SHAConstants.INDEX_PROCESS] = "";					
	attributes[SHAConstants.INDEX_REGISTRATION_DATE] = currentDate;			    	
	attributes[SHAConstants.INDEX_FVR_KEY] = 0;
	attributes[SHAConstants.INDEX_FVR_CPU_CODE] = "";
	attributes[SHAConstants.INDEX_FVR_NUMBER] = "";
	attributes[SHAConstants.INDEX_ROD_KEY] = 0;
	attributes[SHAConstants.INDEX_ROD_NUMBER] = "";
//	Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
	attributes[SHAConstants.INDEX_ROD_CREATED_DATE] = null;
	attributes[SHAConstants.INDEX_REIMB_REQ_BY] = "";
	attributes[SHAConstants.INDEX_ROD_REFER_BY] = "";
	attributes[SHAConstants.INDEX_ACK_KEY] = 0;
	attributes[SHAConstants.INDEX_ACK_NUMBER] = "";
	attributes[SHAConstants.INDEX_PREAUTH_STATUS] = "";
	attributes[SHAConstants.INDEX_BILL_AVAILABLE] = "";
	attributes[SHAConstants.INDEX_HOSPITALIZATION] = "";
	attributes[SHAConstants.INDEX_POST_HOSPITALIZATION] = "";
	attributes[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = "";
	attributes[SHAConstants.INDEX_PRE_HOSPITALIZATION] = "";
	attributes[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = "";
	attributes[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = "";
	attributes[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = "";
	attributes[SHAConstants.INDEX_RRC_REQUEST_KEY] = 0;
	attributes[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = "";
	attributes[SHAConstants.INDEX_RRC_REQUEST_TYPE] = "";
	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY_TYPE] = 0;
	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY] = 0;
	attributes[SHAConstants.INDEX_PED_KEY] = 0;
	attributes[SHAConstants.INDEX_PED_REQUESTOR_ROLE] = "";
	attributes[SHAConstants.INDEX_PED_REQUESTOR_ID] = 0;
	attributes[SHAConstants.INDEX_PED_REFERRED_BY] = "";
	attributes[SHAConstants.INDEX_QUERY_KEY] = 0;
	attributes[SHAConstants.INDEX_QUERY_DISAPPROVED] = "";
	attributes[SHAConstants.INDEX_QUERY_REQUESTED_BY] = "";
	attributes[SHAConstants.INDEX_INVESTIGATION_KEY] = 0;
	attributes[SHAConstants.INDEX_INV_DISAPPROVED] = "";
	attributes[SHAConstants.INDEX_INV_REQUEST] = "";
	attributes[SHAConstants.INDEX_INV_REQUESTED_BY] = "";
	attributes[SHAConstants.INDEX_REMINDER_CATEGORY] = "";
	attributes[SHAConstants.INDEX_HEALTH_CARD_NUMBER] = "";
	attributes[SHAConstants.INDEX_REMINDER_TYPE] = "";	 
	attributes[SHAConstants.INDEX_PED_TYPE] = "";
	attributes[SHAConstants.INDEX_PAYMENT_CANCELLATION] = "";
	attributes[SHAConstants.INDEX_DOCUMENT_RECEVIED_FROM] = "";
	attributes[SHAConstants.INDEX_ZONAL_BYPASS] = "";
	attributes[SHAConstants.INDEX_REMINDER_LATTER_FLAG] = "";
	attributes[SHAConstants.INDEX_ALLOCATED_USER] = "";
	attributes[SHAConstants.INDEX_ALLOCATED_DATE] = null;
	attributes[SHAConstants.INDEX_PED_INITIATED_DATE] = null;
	
	resultArray[0] = attributes;

	return resultArray;
}

//New additional method for submiting Hospital scoring
public static Object[] getRevisedObjArrayForHospitalScoringSubmit(Map<String,Object> map,String stageSource){			   
	   
	   Object[] resultArray = new Object[1];
	
	Object[] attributes = new Object[85];
	attributes[SHAConstants.INDEX_WORK_FLOW_KEY] = map.get(SHAConstants.WK_KEY);
	attributes[SHAConstants.INDEX_INTIMATION_NO] = map.get(SHAConstants.INTIMATION_NO);
	attributes[SHAConstants.INDEX_INTIMATION_KEY] = map.get(SHAConstants.INTIMATION_KEY);
	attributes[SHAConstants.INDEX_INTIMATION_SOURCE] = map.get(SHAConstants.INT_SOURCE);
	attributes[SHAConstants.INDEX_CPU_CODE] =map.get(SHAConstants.CPU_CODE);
	attributes[SHAConstants.INDEX_CLAIM_NUMBER] = map.get(SHAConstants.DB_CLAIM_NUMBER);
	attributes[SHAConstants.INDEX_CLAIM_KEY] = map.get(SHAConstants.DB_CLAIM_KEY);
	attributes[SHAConstants.INDEX_CLAIM_TYPE] =map.get(SHAConstants.CLAIM_TYPE);
	attributes[SHAConstants.INDEX_BALANCE_SI_FLAG] =map.get(SHAConstants.BAL_SI_FLAG);
	attributes[SHAConstants.INDEX_ADMISSION_DATE] = map.get(SHAConstants.ADMISSION_DATE);
	attributes[SHAConstants.INDEX_INTIMATION_DATE] =map.get(SHAConstants.INTIMATION_DATE);
	attributes[SHAConstants.INDEX_POLICY_KEY] =map.get(SHAConstants.POLICY_KEY);
	attributes[SHAConstants.INDEX_POLICY_NUMBER] = map.get(SHAConstants.POLICY_NUMBER);
	attributes[SHAConstants.INDEX_PRODUCT_KEY] = map.get(SHAConstants.PRODUCT_KEY);
	attributes[SHAConstants.INDEX_PRODUCT_NAME] = map.get(SHAConstants.PRODUCT_NAME);
	attributes[SHAConstants.INDEX_LOB] =map.get(SHAConstants.LOB);
	attributes[SHAConstants.INDEX_LOB_TYPE] =map.get(SHAConstants.LOB_TYPE);
	attributes[SHAConstants.INDEX_HOSPITAL_TYPE] = map.get(SHAConstants.HOSPITAL_TYPE);
	attributes[SHAConstants.INDEX_NETWORK_TYPE] = map.get(SHAConstants.NETWORK_TYPE);
	attributes[SHAConstants.INDEX_HOSPITAL_KEY] = map.get(SHAConstants.HOSPITAL_KEY);
	attributes[SHAConstants.INDEX_TREATMENT_TYPE] = map.get(SHAConstants.TREATEMENT_TYPE);
	attributes[SHAConstants.INDEX_SPECIALITY_NAME] =map.get(SHAConstants.SPECIALITY_NAME);
	attributes[SHAConstants.INDEX_PRIORITY] = map.get(SHAConstants.PRIORITY);
	attributes[SHAConstants.INDEX_RECORD_TYPE] = map.get(SHAConstants.RECORD_TYPE);
//	attributes[SHAConstants.INDEX_STAGE_SOURCE] = map.get(SHAConstants.STAGE_SOURCE);
	if(stageSource != null && stageSource != "") {
	 	attributes[SHAConstants.INDEX_STAGE_SOURCE] = stageSource; 		
	} else {
		attributes[SHAConstants.INDEX_STAGE_SOURCE] = map.get(SHAConstants.STAGE_SOURCE);
	}

	attributes[SHAConstants.INDEX_CASHLESS_NO] = map.get(SHAConstants.CASHLESS_NO);
	attributes[SHAConstants.INDEX_CASHLESS_KEY] =map.get(SHAConstants.CASHLESS_KEY);
	attributes[SHAConstants.INDEX_CLAIMED_AMT] =map.get(SHAConstants.CLAIMED_AMOUNT);
	attributes[SHAConstants.INDEX_ROLE_ID] = map.get(SHAConstants.ROLE_ID);
	attributes[SHAConstants.INDEX_ESCALATE_ROLE_ID] =map.get(SHAConstants.ESCALATE_ROLE_ID);
	attributes[SHAConstants.INDEX_ESCALATE_USER_ID] =map.get(SHAConstants.ESCALATE_USER_ID);
	attributes[SHAConstants.INDEX_PROCESS_TYPE] = map.get(SHAConstants.PROCESS_TYPE);
	attributes[SHAConstants.INDEX_RECONSIDERATION_FLAG] =map.get(SHAConstants.RECONSIDER_FLAG);
	attributes[SHAConstants.INDEX_REFERENCE_USER_ID] = map.get(SHAConstants.REFERENCE_USER_ID);
	attributes[SHAConstants.INDEX_OUT_COME] = map.get(SHAConstants.OUTCOME);
	attributes[SHAConstants.INDEX_PROECSSED_DATE] = map.get(SHAConstants.PROCESSED_DATE);
	attributes[SHAConstants.INDEX_USER_ID] = map.get(SHAConstants.USER_ID);
/*	    	attributes[SHAConstants.INDEX_PROCESS] = map.get(SHAConstants.PAYLOAD_PROCESS) != null ? map.get(SHAConstants.PAYLOAD_PROCESS) : "";					
		attributes[SHAConstants.INDEX_REGISTRATION_DATE] = map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) != null ? map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) : "";			    	
	attributes[SHAConstants.INDEX_FVR_KEY] = map.get(SHAConstants.FVR_KEY) != null ? map.get(SHAConstants.FVR_KEY) : 0;
	attributes[SHAConstants.INDEX_FVR_CPU_CODE] = map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE) != null ? map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE) : "";
	attributes[SHAConstants.INDEX_FVR_NUMBER] = map.get(SHAConstants.PAYLOAD_FVR_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_FVR_NUMBER) : "";
	attributes[SHAConstants.INDEX_ROD_KEY] = map.get(SHAConstants.PAYLOAD_ROD_KEY) != null ? map.get(SHAConstants.PAYLOAD_ROD_KEY) : "";
	attributes[SHAConstants.INDEX_ROD_NUMBER] = map.get(SHAConstants.PAYLOAD_ROD_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_ROD_NUMBER) : "";
	attributes[SHAConstants.INDEX_ROD_CREATED_DATE] = map.get(SHAConstants.PAYLOAD_ROD_CREATED_DATE) != null ? map.get(SHAConstants.PAYLOAD_ROD_CREATED_DATE) : null;
	attributes[SHAConstants.INDEX_REIMB_REQ_BY] = map.get(SHAConstants.PAYLOAD_REIMB_REQ_BY) != null ? map.get(SHAConstants.PAYLOAD_REIMB_REQ_BY) : "";
	attributes[SHAConstants.INDEX_ROD_REFER_BY] = map.get(SHAConstants.PAYLOAD_ROD_REFER_BY) != null ? map.get(SHAConstants.PAYLOAD_ROD_REFER_BY) : "";
	attributes[SHAConstants.INDEX_ACK_KEY] = map.get(SHAConstants.PAYLOAD_ACK_KEY) != null ? map.get(SHAConstants.PAYLOAD_ACK_KEY) :  0;
	attributes[SHAConstants.INDEX_ACK_NUMBER] = map.get(SHAConstants.PAYLOAD_ACK_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_ACK_NUMBER) : "";
	attributes[SHAConstants.INDEX_PREAUTH_STATUS] = map.get(SHAConstants.PAYLOAD_PREAUTH_STATUS) != null ? map.get(SHAConstants.PAYLOAD_PREAUTH_STATUS) : "";
	attributes[SHAConstants.INDEX_BILL_AVAILABLE] = map.get(SHAConstants.PAYLOAD_BILL_AVAILABLE) != null ? map.get(SHAConstants.PAYLOAD_BILL_AVAILABLE) : "";
	attributes[SHAConstants.INDEX_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_HOSPITALIZATION) != null ? map.get(SHAConstants.PAYLOAD_HOSPITALIZATION) : "";
	attributes[SHAConstants.INDEX_POST_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_POST_HOSPITALIZATION) != null ? map.get(SHAConstants.PAYLOAD_POST_HOSPITALIZATION) : "";
	attributes[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION) != null ? map.get(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION) : "";
	attributes[SHAConstants.INDEX_PRE_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION) != null ? map.get(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION) : "";
	attributes[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = map.get(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT) != null ? map.get(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT) : "";
	attributes[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE) != null ? map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE) : "";
	attributes[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH) != null ? map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH) : "";
	attributes[SHAConstants.INDEX_RRC_REQUEST_KEY] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY) != null ? map.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY) : 0;
	attributes[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER) : "";
	attributes[SHAConstants.INDEX_RRC_REQUEST_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) != null ? map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) : "";
	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE) != null ? map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE) : "";
	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY) != null ? map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY) : "";
	attributes[SHAConstants.INDEX_PED_KEY] = map.get(SHAConstants.PAYLOAD_PED_KEY) != null ? map.get(SHAConstants.PAYLOAD_PED_KEY) : "";
	attributes[SHAConstants.INDEX_PED_REQUESTOR_ROLE] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE) != null ? map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE) : "";
	attributes[SHAConstants.INDEX_PED_REQUESTOR_ID] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ID) != null ? map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ID) : "";
	attributes[SHAConstants.INDEX_PED_REFERRED_BY] = map.get(SHAConstants.PAYLOAD_PED_REFERRED_BY) != null ? map.get(SHAConstants.PAYLOAD_PED_REFERRED_BY) : "";
	attributes[SHAConstants.INDEX_QUERY_KEY] = map.get(SHAConstants.PAYLOAD_QUERY_KEY) != null ? map.get(SHAConstants.PAYLOAD_QUERY_KEY) : "";
	attributes[SHAConstants.INDEX_QUERY_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_QUERY_DISAPPROVED) != null ? map.get(SHAConstants.PAYLOAD_QUERY_DISAPPROVED) : "";
	attributes[SHAConstants.INDEX_QUERY_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY) != null ? map.get(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY) : "";
	attributes[SHAConstants.INDEX_INVESTIGATION_KEY]  = map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY) != null ? map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY) : "";
	attributes[SHAConstants.INDEX_INV_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_INV_DISAPPROVED) != null ? map.get(SHAConstants.PAYLOAD_INV_DISAPPROVED) : "";
	attributes[SHAConstants.INDEX_INV_REQUEST] = map.get(SHAConstants.PAYLOAD_INV_REQUEST) != null ? map.get(SHAConstants.PAYLOAD_INV_REQUEST) : "";
	attributes[SHAConstants.INDEX_INV_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_INV_REQUESTED_BY) != null ? map.get(SHAConstants.PAYLOAD_INV_REQUESTED_BY) : "";
	attributes[SHAConstants.INDEX_REMINDER_CATEGORY] = map.get(SHAConstants.PAYLOAD_REMINDER_CATEGORY) != null ? map.get(SHAConstants.PAYLOAD_REMINDER_CATEGORY) : "";
	attributes[SHAConstants.INDEX_HEALTH_CARD_NUMBER] = map.get(SHAConstants.PAYLOAD_HEALTH_CARD_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_HEALTH_CARD_NUMBER) : "";
	attributes[SHAConstants.INDEX_REMINDER_TYPE] = map.get(SHAConstants.PAYLOAD_REMINDER_TYPE) != null ? map.get(SHAConstants.PAYLOAD_REMINDER_TYPE) : "";*/
	
		attributes[SHAConstants.INDEX_PROCESS] = map.get(SHAConstants.PAYLOAD_PROCESS);					
			attributes[SHAConstants.INDEX_REGISTRATION_DATE] = map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) ;			    	
	    	attributes[SHAConstants.INDEX_FVR_KEY] = map.get(SHAConstants.FVR_KEY);
	    	attributes[SHAConstants.INDEX_FVR_CPU_CODE] = map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE);
	    	attributes[SHAConstants.INDEX_FVR_NUMBER] = map.get(SHAConstants.PAYLOAD_FVR_NUMBER) ;
	    	attributes[SHAConstants.INDEX_ROD_KEY] = map.get(SHAConstants.PAYLOAD_ROD_KEY) ;
	    	attributes[SHAConstants.INDEX_ROD_NUMBER] = map.get(SHAConstants.PAYLOAD_ROD_NUMBER) ;
	    	attributes[SHAConstants.INDEX_ROD_CREATED_DATE] = map.get(SHAConstants.PAYLOAD_ROD_CREATED_DATE);
	    	attributes[SHAConstants.INDEX_REIMB_REQ_BY] = map.get(SHAConstants.PAYLOAD_REIMB_REQ_BY) ;
	    	attributes[SHAConstants.INDEX_ROD_REFER_BY] = map.get(SHAConstants.PAYLOAD_ROD_REFER_BY);
	    	attributes[SHAConstants.INDEX_ACK_KEY] = map.get(SHAConstants.PAYLOAD_ACK_KEY);
	    	attributes[SHAConstants.INDEX_ACK_NUMBER] = map.get(SHAConstants.PAYLOAD_ACK_NUMBER);
	    	attributes[SHAConstants.INDEX_PREAUTH_STATUS] = map.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
	    	attributes[SHAConstants.INDEX_BILL_AVAILABLE] = map.get(SHAConstants.PAYLOAD_BILL_AVAILABLE) ;
	    	attributes[SHAConstants.INDEX_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_HOSPITALIZATION);
	    	attributes[SHAConstants.INDEX_POST_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_POST_HOSPITALIZATION);
	    	attributes[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION);
	    	attributes[SHAConstants.INDEX_PRE_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION);
	    	attributes[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = map.get(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT) ;
	    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE);
	    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH);
	    	attributes[SHAConstants.INDEX_RRC_REQUEST_KEY] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY);
	    	attributes[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER);
	    	attributes[SHAConstants.INDEX_RRC_REQUEST_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) ;
	    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE);
	    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY);
	    	attributes[SHAConstants.INDEX_PED_KEY] = map.get(SHAConstants.PAYLOAD_PED_KEY) ;
	    	attributes[SHAConstants.INDEX_PED_TYPE] = map.get(SHAConstants.PAYLOAD_PED_TYPE);
	    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ROLE] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE);
	    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ID] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ID) ;
	    	attributes[SHAConstants.INDEX_PED_REFERRED_BY] = map.get(SHAConstants.PAYLOAD_PED_REFERRED_BY);
	    	attributes[SHAConstants.INDEX_PREAUTH_STATUS] =  map.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
	    	attributes[SHAConstants.INDEX_QUERY_KEY] = map.get(SHAConstants.PAYLOAD_QUERY_KEY) ;
	    	attributes[SHAConstants.INDEX_QUERY_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_QUERY_DISAPPROVED) ;
	    	attributes[SHAConstants.INDEX_QUERY_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY);
	    	attributes[SHAConstants.INDEX_INVESTIGATION_KEY]  = map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY);
	    	attributes[SHAConstants.INDEX_INV_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_INV_DISAPPROVED);
	    	attributes[SHAConstants.INDEX_INV_REQUEST] = map.get(SHAConstants.PAYLOAD_INV_REQUEST) ;
	    	attributes[SHAConstants.INDEX_INV_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_INV_REQUESTED_BY) ;
	    	attributes[SHAConstants.INDEX_REMINDER_CATEGORY] = map.get(SHAConstants.PAYLOAD_REMINDER_CATEGORY);
	    	attributes[SHAConstants.INDEX_HEALTH_CARD_NUMBER] = map.get(SHAConstants.PAYLOAD_HEALTH_CARD_NUMBER);
	    	attributes[SHAConstants.INDEX_REMINDER_TYPE] = map.get(SHAConstants.PAYLOAD_REMINDER_TYPE);
	    	attributes[SHAConstants.INDEX_PAYMENT_CANCELLATION] = map.get(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION);
	    	attributes[SHAConstants.INDEX_DOCUMENT_RECEVIED_FROM] = map.get(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM);
	    	attributes[SHAConstants.INDEX_ZONAL_BYPASS] = map.get(SHAConstants.PAYLOAD_ZONAL_BY_PASS);
	    	attributes[SHAConstants.INDEX_REMINDER_LATTER_FLAG] = map.get(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG);
	    	attributes[SHAConstants.INDEX_ALLOCATED_USER] = map.get(SHAConstants.PAYLOAD_ALLOCATED_USER);
	    	attributes[SHAConstants.INDEX_ALLOCATED_DATE] = map.get(SHAConstants.PAYLOAD_ALLOCATED_DATE);
	    	attributes[SHAConstants.INDEX_PED_INITIATED_DATE] = map.get(SHAConstants.PAYLOAD_PED_INITIATED_DATE);
	    	
	resultArray[0] = attributes;
		
		return resultArray;
}

//CR2019058 New Get task for Assign Investigation - To genearate revised object
public static Object[] setRevisedObjArrayForAssignInvestigationGetTask(Map<String, Object> map) {
	
	Object[] resultArray = new Object[1];
	
	Object[] inputArray = new Object[40];
	inputArray[0] = map.get(SHAConstants.INTIMATION_NO) != null ?  map.get(SHAConstants.INTIMATION_NO) : "";
	inputArray[1] = map.get(SHAConstants.INT_SOURCE) != null ?  map.get(SHAConstants.INT_SOURCE) : "";
	inputArray[2] = map.get(SHAConstants.CPU_CODE) != null ?  map.get(SHAConstants.CPU_CODE) : null;
	inputArray[3] = map.get(SHAConstants.CLAIM_NUMBER) != null ?  map.get(SHAConstants.CLAIM_NUMBER) : "";
	inputArray[4] = map.get(SHAConstants.CLAIM_TYPE) != null ?  map.get(SHAConstants.CLAIM_TYPE) : "";
	inputArray[5] = map.get(SHAConstants.ADMISSION_DATE) != null ?  map.get(SHAConstants.ADMISSION_DATE) : "";
	inputArray[6] = map.get(SHAConstants.INTIMATION_DATE) != null ?  map.get(SHAConstants.INTIMATION_DATE) : "";
	inputArray[7] = map.get(SHAConstants.POLICY_NUMBER) != null ?  map.get(SHAConstants.POLICY_NUMBER) : "";
	inputArray[8] = map.get(SHAConstants.PRODUCT_NAME) != null ?  map.get(SHAConstants.PRODUCT_NAME) : "";
	inputArray[9] = map.get(SHAConstants.LOB) != null ?  map.get(SHAConstants.LOB) : "";
	inputArray[10] = map.get(SHAConstants.LOB_TYPE) != null ?  map.get(SHAConstants.LOB_TYPE) : "";
	inputArray[11] = map.get(SHAConstants.HOSPITAL_TYPE) != null ?  map.get(SHAConstants.HOSPITAL_TYPE) : "";
	inputArray[12] = map.get(SHAConstants.NETWORK_TYPE) != null ?  map.get(SHAConstants.NETWORK_TYPE) : "";
	inputArray[13] = map.get(SHAConstants.TREATEMENT_TYPE) != null ?  map.get(SHAConstants.TREATEMENT_TYPE) : "";
	inputArray[14] = map.get(SHAConstants.SPECIALITY_NAME) != null ?  map.get(SHAConstants.SPECIALITY_NAME) : "";
	inputArray[15] = map.get(SHAConstants.PRIORITY) != null ?  map.get(SHAConstants.PRIORITY) : "";
	inputArray[16] = map.get(SHAConstants.RECORD_TYPE) != null ?  map.get(SHAConstants.RECORD_TYPE) : "";
	inputArray[17] = map.get(SHAConstants.STAGE_SOURCE) != null ?  map.get(SHAConstants.STAGE_SOURCE) : "";
	inputArray[18] = map.get(SHAConstants.CLM_FM_AMT) != null ?  map.get(SHAConstants.CLM_FM_AMT) : null;
	inputArray[19] = map.get(SHAConstants.CLM_TO_AMT) != null ?  map.get(SHAConstants.CLM_TO_AMT) :null;
	inputArray[20] = map.get(SHAConstants.PROCESS_TYPE) != null ?  map.get(SHAConstants.PROCESS_TYPE) : "";
	inputArray[21] = map.get(SHAConstants.CURRENT_Q) != null ?  map.get(SHAConstants.CURRENT_Q) : "WFPA";
	inputArray[22] = map.get(SHAConstants.USER_ID) != null ?  map.get(SHAConstants.USER_ID) : "ADMINB";
	inputArray[23] = map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) != null ? map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) : null;
	inputArray[24] = map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE) != null ? map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE) : null;
	inputArray[25] = map.get(SHAConstants.PAYLOAD_ROD_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_ROD_NUMBER) : "";
	inputArray[26] = map.get(SHAConstants.PAYLOAD_ACK_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_ACK_NUMBER) : "";
	inputArray[27] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER) != null ? map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER) : "";
	inputArray[28] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) != null ? map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) : "";
	inputArray[29] = map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY) != null ? map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY) : null;
	inputArray[30] = map.get(SHAConstants.PAYLOAD_QUERY_KEY) != null ? map.get(SHAConstants.PAYLOAD_QUERY_KEY) : null;
	inputArray[31] = map.get(SHAConstants.PAYLOAD_PED_KEY) != null ? map.get(SHAConstants.PAYLOAD_PED_KEY) :null;
	inputArray[32] = map.get(SHAConstants.PAYLOAD_PED_TYPE) != null ? map.get(SHAConstants.PAYLOAD_PED_TYPE) :"";
	inputArray[33] = map.get(SHAConstants.PAYLOAD_REFERENCE_USER_ID) != null ? map.get(SHAConstants.PAYLOAD_REFERENCE_USER_ID) : null;
	inputArray[34] = map.get(SHAConstants.PAYLOAD_BILLCLASS_HOSP) != null ? map.get(SHAConstants.PAYLOAD_BILLCLASS_HOSP) : null;
	inputArray[35] = map.get(SHAConstants.PAYLOAD_BILLCLASS_POSTHOSP) != null ? map.get(SHAConstants.PAYLOAD_BILLCLASS_POSTHOSP) : null;
	inputArray[36] = map.get(SHAConstants.PAYLOAD_BILLCLASS_PREHOSP) != null ? map.get(SHAConstants.PAYLOAD_BILLCLASS_PREHOSP) : null;
	//CR2019058 added 3 new fields
	inputArray[37] = map.get(SHAConstants.INV_TYPE) != null ? map.get(SHAConstants.INV_TYPE) : null;
	inputArray[38] = map.get(SHAConstants.INV_FROM_DATE) != null ? map.get(SHAConstants.INV_FROM_DATE) : null;
	inputArray[39] = map.get(SHAConstants.INV_TO_DATE) != null ? map.get(SHAConstants.INV_TO_DATE) : null;

	//inputArray[37] = map.get(SHAConstants.CRM_PRIORITY) != null ? map.get(SHAConstants.CRM_PRIORITY) : null;
	resultArray[0]= inputArray;
	
	return resultArray;
	
	}

public static Object[] getRevisedObjArrayForAssignInvestigationSubmit(Map<String,Object> map){			   
	   
	   Object[] resultArray = new Object[1];
 	
 	Object[] attributes = new Object[88];
 	attributes[SHAConstants.INDEX_WORK_FLOW_KEY] = map.get(SHAConstants.WK_KEY);
 	attributes[SHAConstants.INDEX_INTIMATION_NO] = map.get(SHAConstants.INTIMATION_NO);
 	attributes[SHAConstants.INDEX_INTIMATION_KEY] = map.get(SHAConstants.INTIMATION_KEY);
 	attributes[SHAConstants.INDEX_INTIMATION_SOURCE] = map.get(SHAConstants.INT_SOURCE);
 	attributes[SHAConstants.INDEX_CPU_CODE] =map.get(SHAConstants.CPU_CODE);
 	attributes[SHAConstants.INDEX_CLAIM_NUMBER] = map.get(SHAConstants.DB_CLAIM_NUMBER);
 	attributes[SHAConstants.INDEX_CLAIM_KEY] = map.get(SHAConstants.DB_CLAIM_KEY);
 	attributes[SHAConstants.INDEX_CLAIM_TYPE] =map.get(SHAConstants.CLAIM_TYPE);
 	attributes[SHAConstants.INDEX_BALANCE_SI_FLAG] =map.get(SHAConstants.BAL_SI_FLAG);
 	attributes[SHAConstants.INDEX_ADMISSION_DATE] = map.get(SHAConstants.ADMISSION_DATE);
 	attributes[SHAConstants.INDEX_INTIMATION_DATE] =map.get(SHAConstants.INTIMATION_DATE);
 	attributes[SHAConstants.INDEX_POLICY_KEY] =map.get(SHAConstants.POLICY_KEY);
 	attributes[SHAConstants.INDEX_POLICY_NUMBER] = map.get(SHAConstants.POLICY_NUMBER);
 	attributes[SHAConstants.INDEX_PRODUCT_KEY] = map.get(SHAConstants.PRODUCT_KEY);
 	attributes[SHAConstants.INDEX_PRODUCT_NAME] = map.get(SHAConstants.PRODUCT_NAME);
 	attributes[SHAConstants.INDEX_LOB] =map.get(SHAConstants.LOB);
 	attributes[SHAConstants.INDEX_LOB_TYPE] =map.get(SHAConstants.LOB_TYPE);
 	attributes[SHAConstants.INDEX_HOSPITAL_TYPE] = map.get(SHAConstants.HOSPITAL_TYPE);
 	attributes[SHAConstants.INDEX_NETWORK_TYPE] = map.get(SHAConstants.NETWORK_TYPE);
 	attributes[SHAConstants.INDEX_HOSPITAL_KEY] = map.get(SHAConstants.HOSPITAL_KEY);
 	attributes[SHAConstants.INDEX_TREATMENT_TYPE] = map.get(SHAConstants.TREATEMENT_TYPE);
 	attributes[SHAConstants.INDEX_SPECIALITY_NAME] =map.get(SHAConstants.SPECIALITY_NAME);
 	attributes[SHAConstants.INDEX_PRIORITY] = map.get(SHAConstants.PRIORITY);
 	attributes[SHAConstants.INDEX_RECORD_TYPE] = map.get(SHAConstants.RECORD_TYPE);
 	attributes[SHAConstants.INDEX_STAGE_SOURCE] = map.get(SHAConstants.STAGE_SOURCE);
 	attributes[SHAConstants.INDEX_CASHLESS_NO] = map.get(SHAConstants.CASHLESS_NO);
 	attributes[SHAConstants.INDEX_CASHLESS_KEY] =map.get(SHAConstants.CASHLESS_KEY);
 	attributes[SHAConstants.INDEX_CLAIMED_AMT] =map.get(SHAConstants.CLAIMED_AMOUNT);
 	attributes[SHAConstants.INDEX_ROLE_ID] = map.get(SHAConstants.ROLE_ID);
 	attributes[SHAConstants.INDEX_ESCALATE_ROLE_ID] =map.get(SHAConstants.ESCALATE_ROLE_ID);
 	attributes[SHAConstants.INDEX_ESCALATE_USER_ID] =map.get(SHAConstants.ESCALATE_USER_ID);
 	attributes[SHAConstants.INDEX_PROCESS_TYPE] = map.get(SHAConstants.PROCESS_TYPE);
 	attributes[SHAConstants.INDEX_RECONSIDERATION_FLAG] =map.get(SHAConstants.RECONSIDER_FLAG);
 	attributes[SHAConstants.INDEX_REFERENCE_USER_ID] = map.get(SHAConstants.REFERENCE_USER_ID);
 	attributes[SHAConstants.INDEX_OUT_COME] = map.get(SHAConstants.OUTCOME);
 	attributes[SHAConstants.INDEX_PROECSSED_DATE] = map.get(SHAConstants.PROCESSED_DATE);
 	attributes[SHAConstants.INDEX_USER_ID] = map.get(SHAConstants.USER_ID);
 	
 			attributes[SHAConstants.INDEX_PROCESS] = map.get(SHAConstants.PAYLOAD_PROCESS);					
			attributes[SHAConstants.INDEX_REGISTRATION_DATE] = map.get(SHAConstants.PAYLOAD_REGISTRATION_DATE) ;			    	
	    	attributes[SHAConstants.INDEX_FVR_KEY] = map.get(SHAConstants.FVR_KEY);
	    	attributes[SHAConstants.INDEX_FVR_CPU_CODE] = map.get(SHAConstants.PAYLOAD_FVR_CPU_CODE);
	    	attributes[SHAConstants.INDEX_FVR_NUMBER] = map.get(SHAConstants.PAYLOAD_FVR_NUMBER) ;
	    	attributes[SHAConstants.INDEX_ROD_KEY] = map.get(SHAConstants.PAYLOAD_ROD_KEY) ;
	    	attributes[SHAConstants.INDEX_ROD_NUMBER] = map.get(SHAConstants.PAYLOAD_ROD_NUMBER) ;
	    	attributes[SHAConstants.INDEX_ROD_CREATED_DATE] = map.get(SHAConstants.PAYLOAD_ROD_CREATED_DATE);
	    	attributes[SHAConstants.INDEX_REIMB_REQ_BY] = map.get(SHAConstants.PAYLOAD_REIMB_REQ_BY) ;
	    	attributes[SHAConstants.INDEX_ROD_REFER_BY] = map.get(SHAConstants.PAYLOAD_ROD_REFER_BY);
	    	attributes[SHAConstants.INDEX_ACK_KEY] = map.get(SHAConstants.PAYLOAD_ACK_KEY);
	    	attributes[SHAConstants.INDEX_ACK_NUMBER] = map.get(SHAConstants.PAYLOAD_ACK_NUMBER);
	    	attributes[SHAConstants.INDEX_PREAUTH_STATUS] = map.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
	    	attributes[SHAConstants.INDEX_BILL_AVAILABLE] = map.get(SHAConstants.PAYLOAD_BILL_AVAILABLE) ;
	    	attributes[SHAConstants.INDEX_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_HOSPITALIZATION);
	    	attributes[SHAConstants.INDEX_POST_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_POST_HOSPITALIZATION);
	    	attributes[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION);
	    	attributes[SHAConstants.INDEX_PRE_HOSPITALIZATION] = map.get(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION);
	    	attributes[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = map.get(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT) ;
	    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE);
	    	attributes[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = map.get(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH);
	    	attributes[SHAConstants.INDEX_RRC_REQUEST_KEY] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY);
	    	attributes[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER);
	    	attributes[SHAConstants.INDEX_RRC_REQUEST_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE) ;
	    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY_TYPE] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE);
	    	attributes[SHAConstants.INDEX_RRC_ELIGIBILITY] = map.get(SHAConstants.PAYLOAD_RRC_ELIGIBILITY);
	    	attributes[SHAConstants.INDEX_PED_KEY] = map.get(SHAConstants.PAYLOAD_PED_KEY) ;
	    	attributes[SHAConstants.INDEX_PED_TYPE] = map.get(SHAConstants.PAYLOAD_PED_TYPE);
	    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ROLE] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE);
	    	attributes[SHAConstants.INDEX_PED_REQUESTOR_ID] = map.get(SHAConstants.PAYLOAD_PED_REQUESTOR_ID) ;
	    	attributes[SHAConstants.INDEX_PED_REFERRED_BY] = map.get(SHAConstants.PAYLOAD_PED_REFERRED_BY);
	    	attributes[SHAConstants.INDEX_PREAUTH_STATUS] =  map.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
	    	attributes[SHAConstants.INDEX_QUERY_KEY] = map.get(SHAConstants.PAYLOAD_QUERY_KEY) ;
	    	attributes[SHAConstants.INDEX_QUERY_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_QUERY_DISAPPROVED) ;
	    	attributes[SHAConstants.INDEX_QUERY_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY);
	    	attributes[SHAConstants.INDEX_INVESTIGATION_KEY]  = map.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY);
	    	attributes[SHAConstants.INDEX_INV_DISAPPROVED] = map.get(SHAConstants.PAYLOAD_INV_DISAPPROVED);
	    	attributes[SHAConstants.INDEX_INV_REQUEST] = map.get(SHAConstants.PAYLOAD_INV_REQUEST) ;
	    	attributes[SHAConstants.INDEX_INV_REQUESTED_BY] = map.get(SHAConstants.PAYLOAD_INV_REQUESTED_BY) ;
	    	attributes[SHAConstants.INDEX_REMINDER_CATEGORY] = map.get(SHAConstants.PAYLOAD_REMINDER_CATEGORY);
	    	attributes[SHAConstants.INDEX_HEALTH_CARD_NUMBER] = map.get(SHAConstants.PAYLOAD_HEALTH_CARD_NUMBER);
	    	attributes[SHAConstants.INDEX_REMINDER_TYPE] = map.get(SHAConstants.PAYLOAD_REMINDER_TYPE);
	    	attributes[SHAConstants.INDEX_PAYMENT_CANCELLATION] = map.get(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION);
	    	attributes[SHAConstants.INDEX_DOCUMENT_RECEVIED_FROM] = map.get(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM);
	    	attributes[SHAConstants.INDEX_ZONAL_BYPASS] = map.get(SHAConstants.PAYLOAD_ZONAL_BY_PASS);
	    	attributes[SHAConstants.INDEX_REMINDER_LATTER_FLAG] = map.get(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG);
	    	attributes[SHAConstants.INDEX_ALLOCATED_USER] = map.get(SHAConstants.PAYLOAD_ALLOCATED_USER);
	    	attributes[SHAConstants.INDEX_ALLOCATED_DATE] = map.get(SHAConstants.PAYLOAD_ALLOCATED_DATE);
	    	attributes[SHAConstants.INDEX_PED_INITIATED_DATE] = map.get(SHAConstants.PAYLOAD_PED_INITIATED_DATE);
	    	//CR2019058 New get task parameters
	    	
	    	attributes[SHAConstants.INDEX_INV_INITIATED_DATE] = map.get(SHAConstants.INV_INITIATED_DATE);
	    	attributes[SHAConstants.INDEX_INV_APPROVED_DATE] = map.get(SHAConstants.INV_APPROVED_DATE);
	    	attributes[SHAConstants.INDEX_INV_REQ_DRAFTED_DATE] = map.get(SHAConstants.INV_REQ_DRAFTED_DATE);

 	resultArray[0] = attributes;
		
		return resultArray;
}

//CR2019058 New mwthod to form cursor object
public static Map <String,Object> getRevisedAssignInvestigationObjectFromCursorObj(ResultSet rs) {
	
		Map <String,Object> objectMap = new WeakHashMap<String,Object>();
		
		try {
			
			
			objectMap.put(SHAConstants.WK_KEY, (Long)rs.getLong(SHAConstants.WK_KEY));
			objectMap.put(SHAConstants.INTIMATION_NO, (String) rs.getString(SHAConstants.INTIMATION_NO));
			objectMap.put(SHAConstants.INTIMATION_KEY, (Long) rs.getLong(SHAConstants.INTIMATION_KEY));
			objectMap.put(SHAConstants.INT_SOURCE, (String) rs.getString(SHAConstants.INT_SOURCE));
			objectMap.put(SHAConstants.CPU_CODE, (String) rs.getString(SHAConstants.CPU_CODE));
			objectMap.put(SHAConstants.CLAIM_NUMBER, (String) rs.getString(SHAConstants.DB_CLAIM_NUMBER));
			objectMap.put(SHAConstants.DB_CLAIM_KEY, (Long) rs.getLong(SHAConstants.DB_CLAIM_KEY));
			objectMap.put(SHAConstants.CLAIM_TYPE, (String) rs.getString(SHAConstants.CLAIM_TYPE));
			objectMap.put(SHAConstants.BAL_SI_FLAG, (String) rs.getString(SHAConstants.BAL_SI_FLAG));
			objectMap.put(SHAConstants.ADMISSION_DATE, (String) rs.getString(SHAConstants.ADMISSION_DATE));
			objectMap.put(SHAConstants.INTIMATION_DATE, (String) rs.getString(SHAConstants.INTIMATION_DATE));
			objectMap.put(SHAConstants.POLICY_KEY, (Long) rs.getLong(SHAConstants.DB_POLICY_KEY));
			objectMap.put(SHAConstants.POLICY_NUMBER, (String) rs.getString(SHAConstants.POLICY_NUMBER));
			objectMap.put(SHAConstants.PRODUCT_KEY, (Long) rs.getLong(SHAConstants.PRODUCT_KEY));
			objectMap.put(SHAConstants.PRODUCT_NAME, (String) rs.getString(SHAConstants.PRODUCT_NAME));
			objectMap.put(SHAConstants.LOB, (String) rs.getString(SHAConstants.LOB));
			objectMap.put(SHAConstants.LOB_TYPE, (String) rs.getString(SHAConstants.LOB_TYPE));
			objectMap.put(SHAConstants.HOSPITAL_TYPE, (String) rs.getString(SHAConstants.HOSPITAL_TYPE));
			objectMap.put(SHAConstants.NETWORK_TYPE, (String) rs.getString(SHAConstants.NETWORK_TYPE));
			objectMap.put(SHAConstants.HOSPITAL_KEY, (Long) rs.getLong(SHAConstants.HOSPITAL_KEY));
			objectMap.put(SHAConstants.TREATEMENT_TYPE, (String) rs.getString(SHAConstants.TREATEMENT_TYPE));
			objectMap.put(SHAConstants.SPECIALITY_NAME, (String) rs.getString(SHAConstants.SPECIALITY_NAME));
			objectMap.put(SHAConstants.PRIORITY, (String) rs.getString(SHAConstants.PRIORITY));
			objectMap.put(SHAConstants.RECORD_TYPE, (String) rs.getString(SHAConstants.RECORD_TYPE));
			objectMap.put(SHAConstants.STAGE_SOURCE, (String) rs.getString(SHAConstants.STAGE_SOURCE));
			objectMap.put(SHAConstants.CASHLESS_NO, (String) rs.getString(SHAConstants.CASHLESS_NO));
			objectMap.put(SHAConstants.CASHLESS_KEY,(Long) rs.getLong(SHAConstants.CASHLESS_KEY));
			objectMap.put(SHAConstants.CLAIMED_AMOUNT, (String) rs.getString(SHAConstants.CLAIMED_AMOUNT));
			objectMap.put(SHAConstants.ROLE_ID,(String) rs.getString(SHAConstants.ROLE_ID));
			objectMap.put(SHAConstants.ESCALATE_ROLE_ID,(Long)rs.getLong(SHAConstants.ESCALATE_ROLE_ID));
			objectMap.put(SHAConstants.ESCALATE_USER_ID,(String) rs.getString(SHAConstants.ESCALATE_USER_ID));
			objectMap.put(SHAConstants.PROCESS_TYPE,(String)rs.getString(SHAConstants.PROCESS_TYPE));
			objectMap.put(SHAConstants.RECONSIDER_FLAG, (String) rs.getString(SHAConstants.RECONSIDER_FLAG));
			objectMap.put(SHAConstants.REFERENCE_USER_ID, (String) rs.getString(SHAConstants.REFERENCE_USER_ID));
			objectMap.put(SHAConstants.CURRENT_Q, (String) rs.getString(SHAConstants.CURRENT_Q));
			objectMap.put(SHAConstants.OUTCOME, (String) rs.getString(SHAConstants.OUTCOME));
			objectMap.put(SHAConstants.PAYLOAD_PROCESS,(String) rs.getString(SHAConstants.PAYLOAD_PROCESS));
			objectMap.put(SHAConstants.PAYLOAD_REGISTRATION_DATE, (String) rs.getString(SHAConstants.PAYLOAD_REGISTRATION_DATE));
			objectMap.put(SHAConstants.PAYLOAD_FVR_CPU_CODE, (String) rs.getString(SHAConstants.PAYLOAD_FVR_CPU_CODE));
			objectMap.put(SHAConstants.FVR_KEY, (String) rs.getString(SHAConstants.FVR_KEY));
			objectMap.put(SHAConstants.PAYLOAD_FVR_NUMBER, (String) rs.getString(SHAConstants.PAYLOAD_FVR_NUMBER));
			objectMap.put(SHAConstants.PAYLOAD_ROD_KEY, (Long) rs.getLong(SHAConstants.PAYLOAD_ROD_KEY));
			objectMap.put(SHAConstants.PAYLOAD_ROD_NUMBER, (String) rs.getString(SHAConstants.PAYLOAD_ROD_NUMBER));
			objectMap.put(SHAConstants.PAYLOAD_ROD_CREATED_DATE, (String) rs.getString(SHAConstants.PAYLOAD_ROD_CREATED_DATE));
			objectMap.put(SHAConstants.PAYLOAD_REIMB_REQ_BY, (String) rs.getString(SHAConstants.PAYLOAD_REIMB_REQ_BY));
			objectMap.put(SHAConstants.PAYLOAD_ROD_REFER_BY, (String) rs.getString(SHAConstants.PAYLOAD_ROD_REFER_BY));
			objectMap.put(SHAConstants.PAYLOAD_ACK_KEY, (Long) rs.getLong(SHAConstants.PAYLOAD_ACK_KEY));
			objectMap.put(SHAConstants.PAYLOAD_ACK_NUMBER, (String) rs.getString(SHAConstants.PAYLOAD_ACK_NUMBER));
			objectMap.put(SHAConstants.PAYLOAD_PREAUTH_STATUS, (String) rs.getString(SHAConstants.PAYLOAD_PREAUTH_STATUS));
			objectMap.put(SHAConstants.PAYLOAD_BILL_AVAILABLE, (String) rs.getString(SHAConstants.PAYLOAD_BILL_AVAILABLE));
			objectMap.put(SHAConstants.PAYLOAD_HOSPITALIZATION, (String) rs.getString(SHAConstants.PAYLOAD_HOSPITALIZATION));
			objectMap.put(SHAConstants.PAYLOAD_POST_HOSPITALIZATION, (String) rs.getString(SHAConstants.PAYLOAD_POST_HOSPITALIZATION));
			objectMap.put(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION, (String) rs.getString(SHAConstants.PAYLOAD_PARTIAL_HOSPITALIZATION));
			objectMap.put(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION, (String) rs.getString(SHAConstants.PAYLOAD_PRE_HOSPITALIZATION));
			objectMap.put(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT, (String) rs.getString(SHAConstants.PAYLOAD_LUMP_SUM_AMOUNT));
			objectMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE, (String) rs.getString(SHAConstants.PAYLOAD_ADDON_BENEFITS_PATIENT_CARE));
			objectMap.put(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH, (String) rs.getString(SHAConstants.PAYLOAD_ADDON_BENEFITS_HOSP_CASH));
			objectMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_KEY, (String) rs.getString(SHAConstants.PAYLOAD_RRC_REQUEST_KEY));
			objectMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER, (String) rs.getString(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER));
			objectMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, (String) rs.getString(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE));
			objectMap.put(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE, (String) rs.getString(SHAConstants.PAYLOAD_RRC_ELIGIBILITY_TYPE));
			objectMap.put(SHAConstants.PAYLOAD_RRC_ELIGIBILITY, (String) rs.getString(SHAConstants.PAYLOAD_RRC_ELIGIBILITY));
			objectMap.put(SHAConstants.PAYLOAD_PED_KEY, (Long) rs.getLong((SHAConstants.PAYLOAD_PED_KEY)));
			objectMap.put(SHAConstants.PAYLOAD_PED_TYPE, (String) rs.getString((SHAConstants.PAYLOAD_PED_TYPE)));
			objectMap.put(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE, (String) rs.getString(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE));
			objectMap.put(SHAConstants.PAYLOAD_PED_REQUESTOR_ID, (String) rs.getString(SHAConstants.PAYLOAD_PED_REQUESTOR_ID));
			objectMap.put(SHAConstants.PAYLOAD_PED_REFERRED_BY, (String) rs.getString(SHAConstants.PAYLOAD_PED_REFERRED_BY));
			objectMap.put(SHAConstants.PAYLOAD_QUERY_KEY, (Long) rs.getLong(SHAConstants.PAYLOAD_QUERY_KEY));
			objectMap.put(SHAConstants.PAYLOAD_QUERY_DISAPPROVED, (String) rs.getString(SHAConstants.PAYLOAD_QUERY_DISAPPROVED));
			objectMap.put(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY, (String) rs.getString(SHAConstants.PAYLOAD_QUERY_REQUESTED_BY));
			objectMap.put(SHAConstants.PAYLOAD_INVESTIGATION_KEY, (Long) rs.getLong(SHAConstants.PAYLOAD_INVESTIGATION_KEY));
			objectMap.put(SHAConstants.PAYLOAD_INV_DISAPPROVED, (String) rs.getString(SHAConstants.PAYLOAD_INV_DISAPPROVED));
			objectMap.put(SHAConstants.PAYLOAD_INV_REQUEST, (String) rs.getString(SHAConstants.PAYLOAD_INV_REQUEST));
			objectMap.put(SHAConstants.PAYLOAD_INV_REQUESTED_BY, (String) rs.getString(SHAConstants.PAYLOAD_INV_REQUESTED_BY));
			objectMap.put(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION, (String) rs.getString(SHAConstants.PAYLOAD_PAYMENT_CANCELLATION));
			objectMap.put(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM, (String) rs.getString(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM));
			objectMap.put(SHAConstants.PAYLOAD_ZONAL_BY_PASS, (String) rs.getString(SHAConstants.PAYLOAD_ZONAL_BY_PASS));
			objectMap.put(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG, (String) rs.getString(SHAConstants.PAYLOAD_REMINDER_LATTER_FLAG));
			objectMap.put(SHAConstants.PAYLOAD_REFERENCE_USER_ID, (String) rs.getString(SHAConstants.PAYLOAD_REFERENCE_USER_ID));
			objectMap.put(SHAConstants.PAYLOAD_BILLCLASS_HOSP, (String) rs.getString(SHAConstants.PAYLOAD_BILLCLASS_HOSP));
			objectMap.put(SHAConstants.PAYLOAD_BILLCLASS_POSTHOSP, (String) rs.getString(SHAConstants.PAYLOAD_BILLCLASS_POSTHOSP));
			objectMap.put(SHAConstants.PAYLOAD_BILLCLASS_PREHOSP, (String) rs.getString(SHAConstants.PAYLOAD_BILLCLASS_PREHOSP));
			objectMap.put(SHAConstants.PAYLOAD_PED_INITIATED_DATE, (String) rs.getString(SHAConstants.PAYLOAD_PED_INITIATED_DATE));
			
			objectMap.put(SHAConstants.INV_APPROVED_DATE, (String) rs.getString(SHAConstants.INV_APPROVED_DATE));
			objectMap.put(SHAConstants.INV_REQ_DRAFTED_DATE, (String) rs.getString(SHAConstants.INV_REQ_DRAFTED_DATE));
			objectMap.put(SHAConstants.INV_INITIATED_DATE, (String) rs.getString(SHAConstants.INV_INITIATED_DATE));

			/*if(rs.getString(SHAConstants.CPU_ADVISE_STATUS) != null){
				objectMap.put(SHAConstants.CPU_ADVISE_STATUS, (String) rs.getString(SHAConstants.CPU_ADVISE_STATUS));
			}*/
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objectMap;
		
	}

	public static Map<String,Object> getTopUpPolicyObject(ResultSet rs){
	
	Map <String,Object> objectMap = new WeakHashMap<String,Object>();
	
	try{
		
		objectMap.put(SHAConstants.INSURED_NAME, (String)rs.getString(SHAConstants.INSURED_NAME));
		objectMap.put(SHAConstants.INSURED_NUMBER, (String) rs.getString(SHAConstants.INSURED_NUMBER));
		
	}catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return objectMap;
	}

	public static WeakHashMap<Long, Long> getPolCondionalExclutionRejKeys() {
		
		WeakHashMap<Long, Long> rejectIdMap = new WeakHashMap<Long, Long>();
		rejectIdMap.put(2681l, 2681l);
		rejectIdMap.put(2153l, 2153l);
		rejectIdMap.put(7578l, 7578l);
		rejectIdMap.put(7591l, 7591l);
		rejectIdMap.put(7628l, 7628l);
		rejectIdMap.put(7629l, 7629l);
		rejectIdMap.put(7633l, 7633l);
		rejectIdMap.put(7951l, 7951l);
		rejectIdMap.put(7952l, 7952l);
		rejectIdMap.put(7640l, 7640l);
		
		return rejectIdMap;
		
	}

	public static Date parseDate1(Date datechange) {
		Date formattedDate = null;
		String formatDat = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			formattedDate = sdf.parse(new SimpleDateFormat("dd-MM-yyyy").format(datechange));
						
		} catch (Exception e) {
			e.printStackTrace();
		}

		return formattedDate;
	}


	public static Date getDate(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date resultdate = null;
		try {
			resultdate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		System.out.println(date); //Tue Aug 31 10:20:56 SGT 1982
		return resultdate;
	}
	public static Comparator<Object> getComparator(){
		
		class SpecialSelectValueVal implements Comparator<Object> {

			@Override
			public int compare(Object o1,
					Object o2) {
				
				String[] o1ageSplit = ((SpecialSelectValue) o1).getValue().split(" ");
				int selVal1 = Integer.valueOf(o1ageSplit[0]);
				
				String[] o2ageSplit = ((SpecialSelectValue)o2).getValue().split(" ");
				
				int selVal2 = Integer.valueOf(o2ageSplit[0]);
				
				return selVal1< selVal2 ? -1 : 1;
				
			}
			
		}
		return new SpecialSelectValueVal();
		
	}
	
	public static Date combineDateTime(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date resultdate = null;
		try {
			resultdate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resultdate;
	}
	
	public static Date baNCSDateTime(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date resultdate = null;
		try {
			resultdate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resultdate;
	}

	//CR2019202
	public static void showFraudAlertMessageBox(String message){
	    final MessageBox msgBox = MessageBox.createCritical()
				    .withCaptionCust("Potential Fraud Related Warnings") 
				    .withHtmlMessage(message)
				    .withWidth("30%")
				    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				    .open();
	}
	
	//CR2019217
	public static void showICRAgentAlert(String icrMessage, String icrPercentage, String smMessage, String smPercentage){
		
		String alertMsg = "";
		
		if(Integer.parseInt(icrMessage) >= Integer.parseInt(icrPercentage)){
			alertMsg = " Agent ICR exceeds "+ icrPercentage +" % " +"( Currently  " +icrMessage +" % )";
		}
		
		if(Integer.parseInt(smMessage) >= Integer.parseInt(smPercentage)){
			//alertMsg = "SM ICR exceeds "+ smPercentage +" % " +"( Currently  " +smMessage +" % )";
			alertMsg = "SM ICR - "+smMessage+"%";
		}
		
		if((Integer.parseInt(icrMessage) >= Integer.parseInt(icrPercentage)) 
			&& (Integer.parseInt(smMessage) >= Integer.parseInt(smPercentage))){
			alertMsg = " Agent ICR exceeds "+ icrPercentage +" % " +"( Currently  " +icrMessage +" % ) <br> "
					+ "SM ICR exceeds "+ smPercentage +" % " +"(Currently  " +smMessage +" % )";
		}
		
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withHtmlMessage(alertMsg)
			    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			    .open();
			
		
	}
	
	public static BeanItemContainer<SelectValue> getSelectValueForANH(){
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue selectValue = new SelectValue();
		selectValue.setId(ReferenceTable.COMMONMASTER_YES);
		selectValue.setValue(SHAConstants.YES);
		container.addBean(selectValue);
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(ReferenceTable.COMMONMASTER_NO);
		selectValue2.setValue(SHAConstants.No);
		container.addBean(selectValue2);
		return container;
	}

	public static void setEmployeeListValue(
			BeanItemContainer<SelectValue> employeeListValue) {
		SHAUtils.employeeListValue = employeeListValue;
	}

	public static MessageBox showMessageBoxWithCaption(String message,String Caption){
		
		final MessageBox msgBox = MessageBox
				.createInfo()
				.withCaptionCust(Caption)
				.withHtmlMessage(message)
				.withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				.open();
		
		return msgBox;
	}
	
	/**
	 * Commented on 25/10/2019 and the batch scheduled process comment as per Raja A. instruction
	 **/
	
	/*public static Date combineDateTime(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date resultdate = null;
		try {
			resultdate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		System.out.println(date); //Tue Aug 31 10:20:56 SGT 1982
		return resultdate;
	}
	
	public static String formatDateForAdmissionDischarge(Date dateStr)
	{
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	  	  try {
	  		  	String parseDate = formatter.format(dateStr);
	  		  	return parseDate;
//				return new SimpleDateFormat("dd/MM/yyyy").format(parseDate);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	  	  return null;
	}*/
	
	public static MessageBox showMessageBox(String message,String Caption){
		final MessageBox msgBox = MessageBox
				.createInfo()
				.withCaptionCust(Caption)
				.withHtmlMessage(message)
				.withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				.open();

		return msgBox;
	} 
	
public static void setClearAuditQryTableItem(Map<SearchCVCAuditClsQryTableDTO, HashMap<String, AbstractField<?>>> referenceData){
		
		if(referenceData != null){
    	
	    	Iterator<Entry<SearchCVCAuditClsQryTableDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
	    	//referenceData.clear();
	    	try{
		        while (iterator.hasNext()) {
		            Map.Entry pair = (Map.Entry)iterator.next();
		            Object object = pair.getValue();
		            object = null;
		            pair = null;	
		        }
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	       referenceData.clear();
	       referenceData = null;
		}
    	
    }

	public static void showAuditQryAlert(){
		
		String alertMsg = "Claims audit has happened in this intimation.";
	
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Alert")
				    .withHtmlMessage(alertMsg)
				    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				    .open();
			
		
	}
	
	public static void setCleaEEETableItem(Map<ExtraEmployeeEffortDTO, HashMap<String, AbstractField<?>>> referenceData){
		
		if(referenceData != null){
    	
	    	Iterator<Entry<ExtraEmployeeEffortDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
	    	//referenceData.clear();
	    	try{
		        while (iterator.hasNext()) {
		            Map.Entry pair = (Map.Entry)iterator.next();
		            Object object = pair.getValue();
		            object = null;
		            pair = null;	
		        }
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	       referenceData.clear();
	       referenceData = null;
		}
    	
    }


	private static ShortcutListener getShortCutListenerForNegotiatedWith(final TextArea txtFld,final UI ui)
	{
		ShortcutListener listener =  new ShortcutListener("Negotiated With",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				//txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(200);
				txtArea.setReadOnly(false);
				txtArea.setRows(15);

				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
						PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
						if(null != mainDto){
							mainDto.getPreauthMedicalDecisionDetails().setRemarksFromDeptHead(txtFld.getValue());
						}
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();
				
				String strCaption = "Negotiated With";
				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("20%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(ui.getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				ui.getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};

		return listener;
	}
	
	
	public static  void handleTextAreaPopupForNegotiation(TextArea searchField, final  Listener listener, final UI ui) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForNegotiatedWith", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForOpinion(searchField, getShortCutListenerForNegotiatedWith(searchField,ui));

	}
	
	public static void setClearTableItemForLot(Map<CreateAndSearchLotTableDTO, HashMap<String, AbstractField<?>>> referenceData){
        
        if(referenceData != null){
        
            Iterator<Entry<CreateAndSearchLotTableDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
            //referenceData.clear();
            try{
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry)iterator.next();
                    Object object = pair.getValue();
                    object = null;
                    pair = null;        
                }
            }catch(Exception e){
                    e.printStackTrace();
            }
       referenceData.clear();
       referenceData = null;
        }
	}



	public static void setClearReferenceDataLot(HashMap<Long, Component> compMap){
        if(compMap != null){
    
            Iterator<Entry<Long, Component>> iterator = compMap.entrySet().iterator();
            //referenceData.clear();
            try{
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry)iterator.next();
                    Object object = pair.getValue();
                    object = null;
                    pair = null;        
                }
            }catch(Exception e){
                    e.printStackTrace();
            }
        compMap.clear();
        compMap = null;
        }
    
	}
	
	public static MessageBox showErrorMessageBoxWithCaption(String message,String Caption){

		final MessageBox msgBox = MessageBox
				.createError()
				.withCaptionCust(Caption)
				.withHtmlMessage(message)
				.withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				.open();
		return msgBox;
	}


	public static void setClearTableItemForPed(
			Map<ViewPEDTableDTO, HashMap<String, AbstractField<?>>> referenceData) {
		
		if(referenceData != null){
		
	    	Iterator<Entry<ViewPEDTableDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
	    	//referenceData.clear();
	    	try{
		        while (iterator.hasNext()) {
		            Map.Entry pair = (Map.Entry)iterator.next();
		            Object object = pair.getValue();
		            object = null;
		            pair = null;	
		        }
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	       referenceData.clear();
	       referenceData = null;
		}
	}
	
	public static Map<Long, String> getCPUCodeMapForJSP() {
		Map<Long,String> CPUCodeMap = new LinkedHashMap<Long, String>();
		CPUCodeMap.put(950001l, "CHENNAI");
		CPUCodeMap.put(950002l, "BANGALORE");
		CPUCodeMap.put(950003l, "MUMBAI");
		CPUCodeMap.put(950004l, "KERALA");
		CPUCodeMap.put(950006l, "DELHI");
		CPUCodeMap.put(950007l, "AHMEDABAD");
		CPUCodeMap.put(950008l, "PUNE");
		CPUCodeMap.put(950009l, "KOLKATA");
		CPUCodeMap.put(950010l, "HYDERABAD");
		CPUCodeMap.put(950011l, "RAJASTHAN");
		CPUCodeMap.put(950012l, "CHANDIGARH");
		CPUCodeMap.put(950013l, "LUDHIANA");
		CPUCodeMap.put(950014l, "BHOPAL");
		CPUCodeMap.put(950015l, "LUCKNOW");
		CPUCodeMap.put(950016l, "COIMBATORE");
		CPUCodeMap.put(950017l, "MADURAI");
		CPUCodeMap.put(950018l, "BHUBANESWAR");
		CPUCodeMap.put(950019l, "CHENNAI");
		CPUCodeMap.put(950025l, "TATA TRUST");
		CPUCodeMap.put(950026l, "CLAIMS-JIO");
		CPUCodeMap.put(950027l, "CLAIMS-GMC");
		CPUCodeMap.put(950030l, "CLAIMS-PAAYAS");
		CPUCodeMap.put(950031l, "CLAIMS-KOTAK");
		CPUCodeMap.put(950032l, "DEHRADUN");
		CPUCodeMap.put(950033l, "GUWAHATI");
		CPUCodeMap.put(950034l, "TRICHY");
		CPUCodeMap.put(950035l, "DELHI II");
		return CPUCodeMap;
	}
	
	public static Map<Long, String> getDivisionCodeMapForJSP() {
		Map<Long,String> divisionCodeMap = new LinkedHashMap<Long, String>();
		
		divisionCodeMap.put(960001l, "DIV-1(HYD,BHUB,BHO)");
		divisionCodeMap.put(960002l, "DIV-2(DEL-AO1,GUW,CHEN)");
		divisionCodeMap.put(960003l, "DIV-3(PUN,AHM,CHAN,LUDH)");
		divisionCodeMap.put(960004l, "DIV-4(DEL-AO2,DEHR,BGL,JAI)");
		divisionCodeMap.put(960005l, "DIV-5(MUM,MDU,COIM,TRY)");
		divisionCodeMap.put(960006l, "DIV-6(KER,LUCK,KOL)");
		return divisionCodeMap;
	}
	
	public static Map<Long, String> getLegalBaseCellMap() {
		
		 Map<Long,String> legalbaseCellMap = new HashMap<Long, String>();	
		 legalbaseCellMap.put(8l, "Award Amount");
		 legalbaseCellMap.put(9l, "Cost");
		 legalbaseCellMap.put(10l, "Compensation");
		 legalbaseCellMap.put(11l, "Interest Rate");
		 legalbaseCellMap.put(12l, "Interest Current Claim");
		 legalbaseCellMap.put(13l, "Interest Other Claim");
		 legalbaseCellMap.put(23l, "Interest Payable");
		 legalbaseCellMap.put(14l, "TDS");
		 legalbaseCellMap.put(15l, "Interest payable - TDS");
		 legalbaseCellMap.put(16l, "Total Amount Payable");
		
		 return legalbaseCellMap;
	}
		 
	public static Map<Long, Integer> getLegalBaseCellSno() {
		
		 Map<Long,Integer> legalbaseCellSno = new HashMap<Long, Integer>();
		 legalbaseCellSno.put(8l,  1);
		 legalbaseCellSno.put(9l,  2);
		 legalbaseCellSno.put(10l, 3);
		 legalbaseCellSno.put(11l, 4);
		 legalbaseCellSno.put(12l, 5);
		 legalbaseCellSno.put(13l, 6);
		 legalbaseCellSno.put(14l, 7);
		 legalbaseCellSno.put(15l, 8);
		 legalbaseCellSno.put(16l, 9);	 
		 return baseCellSno;
		 
	}
	
	// ADD FOR GALAXYMAIN-13460-decimal value getting Exception and return zero product 84
	public static final Integer getIntegerFromStringRevised(String strNum) {
		if (isValidIntegerOrDecimal(strNum)) {
			return new Float(strNum).intValue();
		} else {
			return new Float("0.0").intValue();
		}
	}
	
	public static final boolean isValidIntegerOrDecimal(String strNum) {
		try {
			if (strNum != null && !strNum.equals("") && !strNum.equals("NaN") && !strNum.equals("NA")) {
				String replaceAll = strNum.replaceAll(",", "");
				if(strNum.contains("."))
				{
					String replacedStr = replaceAll.replaceAll("\\.", "");
					Integer.valueOf(replacedStr);
				}
				else
				{
				Integer.valueOf(replaceAll);
				}
				//Integer.valueOf(replacedStr);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
//			e.printStackTrace();
			return false;
		}

	}
		
public static void showPolicyInstalmentAlert(){
		
		String alertMsg = "This Policy has Instalment Selected.";
	
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Alert")
				    .withHtmlMessage(alertMsg)
				    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				    .open();
		
	}	

	public static Boolean getAlertMessageForPolicyInstalmentamount() {
	String alertMsg = SHAConstants.POLICY_INSTALMENT_PREMIUM_EXCEEDING_MESSAGE;
	final MessageBox msgBox = MessageBox.createWarning()
		    .withCaptionCust("Information") .withHtmlMessage(alertMsg)
		    .withOkButton(ButtonOption.caption(ButtonType.OK.name()))
		    .open();
	return true;
}

public static Date dateformatFromString(String date){
	Date date1 = null;
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
	try {
			date1 = formatter.parse(date);
			System.out.println(date);
			
	 
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	
	return date1;
	
	
}

public static MessageBox showIcacProcessCritcalAlertMessageBox(String message){
	final MessageBox msgBox = MessageBox
			.createCritical()
			.withCaptionCust("Critical Alert")
			.withHtmlMessage(message)
			.withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			.open();

	return msgBox;
}

public static void  popupMessageForWaitingPeriodForNovelCorona(Long diffDays) {

	if((diffDays != null && 15 > diffDays)){

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(SHAConstants.FIFTEEN_DAYS_WAITING_ALERT_CORONA + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {


			}
		});
	}
}

public static void getTravelHisAlrtMsgForStarCoronaGrpPolicy() {
	String alertMsg = SHAConstants.STAR_CORONA_TRAVEL_HIS_MSG;
	final MessageBox msgBox = MessageBox.createWarning()
			.withCaptionCust("Information") .withHtmlMessage(alertMsg)
			.withOkButton(ButtonOption.caption(ButtonType.OK.name()))
			.open();
}

public static Boolean ValidateEmailAddresses(String[] listEmailIDs)
{
	System.out.println("Validating Email addresses....");
	
	for(String oneEmailID : listEmailIDs)
	{
		oneEmailID = oneEmailID.trim();
		if(!isValidEmail(oneEmailID))
		{
			System.out.println(String.format("Email [%s] is invalid", oneEmailID));
			return false;
		}
		
	}
	
	return true;
}

	public static Object[] getRevisedArrayListForDBCallForOPCashless(OPClaim claim){
		Object[] resultArray = new Object[1];
		
		Object[] attributes = new Object[85];
		attributes[SHAConstants.INDEX_WORK_FLOW_KEY] = 0;
		attributes[SHAConstants.INDEX_INTIMATION_NO] = claim.getIntimation().getIntimationId();
		attributes[SHAConstants.INDEX_INTIMATION_KEY] = claim.getIntimation().getKey();
		attributes[SHAConstants.INDEX_INTIMATION_SOURCE] = "";
		attributes[SHAConstants.INDEX_CPU_CODE] = null;
		attributes[SHAConstants.INDEX_CLAIM_NUMBER] = claim.getClaimId();
		attributes[SHAConstants.INDEX_CLAIM_KEY] = claim.getKey();
		attributes[SHAConstants.INDEX_CLAIM_TYPE] = claim.getClaimType().getValue();
		attributes[SHAConstants.INDEX_BALANCE_SI_FLAG] = "Y";
		
		attributes[SHAConstants.INDEX_ADMISSION_DATE] = claim.getIntimation().getAdmissionDate();
		attributes[SHAConstants.INDEX_INTIMATION_DATE] = claim.getIntimation().getCreatedDate();

		attributes[SHAConstants.INDEX_POLICY_KEY] = claim.getIntimation().getPolicy().getKey();
		attributes[SHAConstants.INDEX_POLICY_NUMBER] = claim.getIntimation().getPolicy().getPolicyNumber();
		attributes[SHAConstants.INDEX_PRODUCT_KEY] = claim.getIntimation().getPolicy().getProduct().getKey();
		attributes[SHAConstants.INDEX_PRODUCT_NAME] = claim.getIntimation().getPolicy().getProduct().getValue();
		attributes[SHAConstants.INDEX_LOB] = SHAConstants.HEALTH_LOB;	
		attributes[SHAConstants.INDEX_LOB_TYPE] = "H";	    	
		
		
		if(null != claim.getIntimation().getInsured().getLopFlag() &&
				(SHAConstants.PA_LOB_TYPE).equals(claim.getIntimation().getInsured().getLopFlag())){
			
			attributes[SHAConstants.INDEX_LOB] = SHAConstants.PA_LOB;	
			attributes[SHAConstants.INDEX_LOB_TYPE] = SHAConstants.PA_LOB_TYPE;
		}
		attributes[SHAConstants.INDEX_TREATMENT_TYPE] = claim.getServiceType() != null ? claim.getServiceType() : "";
		attributes[SHAConstants.INDEX_SPECIALITY_NAME] = "";
		attributes[SHAConstants.INDEX_PRIORITY] = claim.getIsVipCustomer() != null ? (claim.getIsVipCustomer().intValue() == 1 ? SHAConstants.VIP_CUSTOMER  : SHAConstants.NORMAL) : "" ;
		if(claim.getIntimation().getInsured() != null && claim.getIntimation().getInsured().getInsuredAge() != null && claim.getIntimation().getInsured().getInsuredAge().intValue()>60){
			attributes[SHAConstants.INDEX_PRIORITY] = SHAConstants.SENIOR_CITIZEN;
		}
		attributes[SHAConstants.INDEX_RECORD_TYPE] = claim.getStatus().getProcessValue();
		attributes[SHAConstants.INDEX_STAGE_SOURCE] = claim.getStage().getStageName();
		attributes[SHAConstants.INDEX_CASHLESS_NO] = "";
		attributes[SHAConstants.INDEX_CASHLESS_KEY] = 0;
		attributes[SHAConstants.INDEX_CLAIMED_AMT] = claim.getIntimation().getRequestedAmount();
		attributes[SHAConstants.INDEX_ROLE_ID] = "";
		attributes[SHAConstants.INDEX_ESCALATE_ROLE_ID] = 0;
		attributes[SHAConstants.INDEX_ESCALATE_USER_ID] = "";
		attributes[SHAConstants.INDEX_PROCESS_TYPE] = claim.getClaimType().getValue();
		attributes[SHAConstants.INDEX_RECONSIDERATION_FLAG] = "N";
		attributes[SHAConstants.INDEX_OUT_COME] = "OPREGOPVER";
		DATE currentDate = new DATE();
		attributes[SHAConstants.INDEX_PROECSSED_DATE] = currentDate;
		attributes[SHAConstants.INDEX_USER_ID] = "ADMINB";
		attributes[SHAConstants.INDEX_PROCESS] = "";
		try {
			if(claim.getDataOfAdmission() != null){
				currentDate = new DATE(claim.getDataOfAdmission());
				attributes[SHAConstants.INDEX_REGISTRATION_DATE] = currentDate;
			}
			else{
				attributes[SHAConstants.INDEX_REGISTRATION_DATE] = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					    	
		attributes[SHAConstants.INDEX_FVR_KEY] = 0;
		attributes[SHAConstants.INDEX_FVR_CPU_CODE] = "";
		attributes[SHAConstants.INDEX_FVR_NUMBER] = "";
		attributes[SHAConstants.INDEX_ROD_KEY] = 0;
		attributes[SHAConstants.INDEX_ROD_NUMBER] = "";
	//	Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		attributes[SHAConstants.INDEX_ROD_CREATED_DATE] = null;
		attributes[SHAConstants.INDEX_REIMB_REQ_BY] = "";
		attributes[SHAConstants.INDEX_ROD_REFER_BY] = "";
		attributes[SHAConstants.INDEX_ACK_KEY] = 0;
		attributes[SHAConstants.INDEX_ACK_NUMBER] = "";
		attributes[SHAConstants.INDEX_PREAUTH_STATUS] = "";
		attributes[SHAConstants.INDEX_BILL_AVAILABLE] = "";
		attributes[SHAConstants.INDEX_HOSPITALIZATION] = "";
		attributes[SHAConstants.INDEX_POST_HOSPITALIZATION] = "";
		attributes[SHAConstants.INDEX_PARTIAL_HOSPITALIZATION] = "";
		attributes[SHAConstants.INDEX_PRE_HOSPITALIZATION] = "";
		attributes[SHAConstants.INDEX_LUMP_SUM_AMOUNT] = "";
		attributes[SHAConstants.INDEX_ADDON_BENEFITS_PATIENT_CARE] = "";
		attributes[SHAConstants.INDEX_ADDON_BENEFITS_HOSP_CASH] = "";
		attributes[SHAConstants.INDEX_RRC_REQUEST_KEY] = 0;
		attributes[SHAConstants.INDEX_RRC_REQUEST_NUMBER] = "";
		attributes[SHAConstants.INDEX_RRC_REQUEST_TYPE] = null;
		attributes[SHAConstants.INDEX_RRC_ELIGIBILITY_TYPE] = 0;
		attributes[SHAConstants.INDEX_RRC_ELIGIBILITY] = 0;
		attributes[SHAConstants.INDEX_PED_KEY] = 0;
		attributes[SHAConstants.INDEX_PED_REQUESTOR_ROLE] = "";
		attributes[SHAConstants.INDEX_PED_REQUESTOR_ID] = 0;
		attributes[SHAConstants.INDEX_PED_REFERRED_BY] = "";
		attributes[SHAConstants.INDEX_QUERY_KEY] = 0;
		attributes[SHAConstants.INDEX_QUERY_DISAPPROVED] = "";
		attributes[SHAConstants.INDEX_QUERY_REQUESTED_BY] = "";
		attributes[SHAConstants.INDEX_INVESTIGATION_KEY] = 0;
		attributes[SHAConstants.INDEX_INV_DISAPPROVED] = "";
		attributes[SHAConstants.INDEX_INV_REQUEST] = "";
		attributes[SHAConstants.INDEX_INV_REQUESTED_BY] = "";
		attributes[SHAConstants.INDEX_REMINDER_CATEGORY] = "";
		attributes[SHAConstants.INDEX_HEALTH_CARD_NUMBER] = "";
		attributes[SHAConstants.INDEX_REMINDER_TYPE] = "";	 
		attributes[SHAConstants.INDEX_PED_TYPE] = "";
		attributes[SHAConstants.INDEX_PAYMENT_CANCELLATION] = "";
		attributes[SHAConstants.INDEX_DOCUMENT_RECEVIED_FROM] = "";
		attributes[SHAConstants.INDEX_ZONAL_BYPASS] = "";
		attributes[SHAConstants.INDEX_REMINDER_LATTER_FLAG] = "";
		attributes[SHAConstants.INDEX_ALLOCATED_USER] = "";
		attributes[SHAConstants.INDEX_ALLOCATED_DATE] = null;
		attributes[SHAConstants.INDEX_PED_INITIATED_DATE] = null;
		
		
		resultArray[0] = attributes;
		
		return resultArray;
	}


public static String getParsedAmountWithRupeesOnly(double amount)
{
	StringBuffer amountInWords = new StringBuffer();
	String amountToWords = "";
	if(amount > 0)
	{
    int a_rupees;
    int a_paise;
    int a_length;
      
    String s="";
    a_rupees = (int)(amount);
    a_paise = Math.round((float)(amount - a_rupees)*100);  
    a_length=Integer.toString(a_rupees).length();
    if(a_rupees !=0)
    {    
      if(a_paise==100)
      {
        a_paise=0;
        a_rupees++;
      }
      else
      {
      }
     amountInWords=getNumberInWords(a_rupees,a_length,amountInWords);
    }
    else
    {  
    }
     if(a_paise !=0)
      {    
       amountInWords.append(" and");
       amountInWords.append(getTens(a_paise));
       amountInWords.append(" Paise only");  
      }
      else
      {
    	  amountInWords.append(" Rupees Only");
      } 
  
     amountToWords = amountInWords.toString();

}
	else if(amount == 0){
		amountToWords = "Zero Rupees Only";
	}

return amountToWords;	
	
}

public static BeanItemContainer<SelectValue> getSelectValueForBufferLimitApplicableTo(){
	BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
	SelectValue selectValue = new SelectValue();
	selectValue.setId(1l);
	selectValue.setValue("E");
	SelectValue selectValue2 = new SelectValue();
	selectValue2.setId(2l);
	selectValue2.setValue("E + S");
	SelectValue selectValue3 = new SelectValue();
	selectValue3.setId(3l);
	selectValue3.setValue("E + S + C");
	SelectValue selectValue4 = new SelectValue();
	selectValue4.setId(4l);
	selectValue4.setValue("E + S + C +P");
	SelectValue selectValue5 = new SelectValue();
	selectValue5.setId(5l);
	selectValue5.setValue("E+S+C+Inlaws");
	SelectValue selectValue6 = new SelectValue();
	selectValue6.setId(6l);
	selectValue6.setValue("All");
	container.addBean(selectValue6);
	container.addBean(selectValue5);
	container.addBean(selectValue3);
	container.addBean(selectValue4);	
	container.addBean(selectValue2);
	container.addBean(selectValue);
	
	container.sort(new Object[] {"value"}, new boolean[] {true});
	
	return container;
}

public static boolean validateRangeDate(Date policyFromDate)
{	 
    Date intiateDate= formatDateWithoutTime("09-12-2020");
	Date finalDate = formatDateWithoutTime("31-03-2021");

	if((policyFromDate.equals(intiateDate) || policyFromDate.after(intiateDate))
			&& (policyFromDate.equals(finalDate) || policyFromDate.before(finalDate)))
	{
		return true;
	}	
	else
	{
		return false;
	}
}
public static void setClearMapIntegerStringValue(Map<Integer, String> referenceData){
	
	Iterator<Entry<Integer, String>> iterator = referenceData.entrySet().iterator();
//	referenceData.clear();
	try{
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            Object object = pair.getValue();
            object = null;
            pair = null;	
        }
	}catch(Exception e){
		e.printStackTrace();
	}
   referenceData.clear();
   referenceData = null;
	
}

public static void setClearMaplongIntegerValue(Map<Long, Integer> referenceData){
	
	Iterator<Entry<Long, Integer>> iterator = referenceData.entrySet().iterator();
//	referenceData.clear();
	try{
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            Object object = pair.getValue();
            object = null;
            pair = null;	
        }
	}catch(Exception e){
		e.printStackTrace();
	}
   referenceData.clear();
   referenceData = null;
	
}

public static void setClearRRCDTO(RRCDTO bean){
	
	if(bean != null){
	bean.setEmployeeEffortDTO(null);
	bean.setEmployeeEffortList(null);
	bean.setEmployeeNameList(null);
	bean.setRrcCategoryList(null);
	bean.setQuantumReductionDetailsDTO(null);
	bean.setQuantumReductionDetailsDTOList(null);
	bean.setQuantumReductionListForClaimWiseRRCHistory(null);
	bean.setDataSourcesMap(null);
	bean.setNewIntimationDTO(null);
	bean.setClaimDto(null);
	bean.setClaimDTO(null);
	bean.setPolicyDto(null);
	if(bean.getPreauthDTO() != null){
		setClearPreauthDTO(bean.getPreauthDTO());
	}
	bean.setDbOutArray(null);
	bean = null;
	}

}

	public static void setClearMapStringValue(Map<String, String> referenceData){
		
		Iterator<Entry<String, String>> iterator = referenceData.entrySet().iterator();
	//	referenceData.clear();
		try{
	        while (iterator.hasNext()) {
	            Map.Entry pair = (Map.Entry)iterator.next();
	            Object object = pair.getValue();
	            object = null;
	            pair = null;	
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
	   referenceData.clear();
	   referenceData = null;
		
		}

	
//For ICR agent and brnach score display For PA
	public static HorizontalLayout icrAgentBranchForPA(PreauthDTO bean) {
		Label icr = new Label();
		icr.setCaption("<b style='color:red; font-weight: bold; font-size:19px'> ICR % :&nbsp </b>");
		icr.setCaptionAsHtml(true);
		/*Label icrAgent = new Label("AG-");
		icrAgent.setDescription("Agent");
		Label icrAgentScore = new Label();
		Label icrBranch = new Label("BR-");
		icrBranch.setDescription("Branch");
		Label icrBranchScore = new Label();*/
		
		Label icrSMScore = new Label();
		Link icrSM = new Link();
		icrSM.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		icrSM.setCaption("<b style='color:green; font-weight: bold; font-size:19px'>SM- </b>");
		icrSM.setCaptionAsHtml(true);
		icrSM.setDescription("SM");
		
		DBCalculationService dbService = new DBCalculationService();
		

		String smPrvPoint = "-";
		Label smPrvPoints = new Label();
		
		if(bean.getNewIntimationDTO().getIntimationId() != null) {
			Map<String,Object> icrValues = dbService.getAgentAndBranchName(bean.getNewIntimationDTO().getIntimationId());
			if(icrValues != null && !icrValues.isEmpty()) {
			/*String agentScorePoint = icrValues.get(SHAConstants.ICR_AGENT_POINT).toString();
			String agentColor = icrValues.get(SHAConstants.ICR_AGENT_COLOR).toString();
			String branchScorePoint = icrValues.get(SHAConstants.ICR_BRANCH_POINT).toString();
			String branchColor = icrValues.get(SHAConstants.ICR_BRANCH_COLOR).toString();*/
			
			String smScorePoint = icrValues.get(SHAConstants.SM_AGENT_POINT).toString();
			String smColour = icrValues.get(SHAConstants.SM_COLOUR_CODE).toString();
			
			smPrvPoint = icrValues.get(SHAConstants.SM_AGENT_PRV_POINT).toString();
			/*String agentColor = "#FF0000";
			String branchColor ="#FF0000";
			String agentScorePoint = "100.0";
			String branchScorePoint = "100.0";*/
			/*icrAgentScore.setCaption("<b style='color:"+agentColor+"'>"+agentScorePoint+"&nbsp");
			icrAgentScore.setCaptionAsHtml(true);
			icrBranchScore.setCaption("<b style = 'color:"+branchColor+"'>"+branchScorePoint+"&nbsp");
			icrBranchScore.setCaptionAsHtml(true);*/
			
			icrSMScore.setCaption("<b style='color:"+smColour+"; font-weight: bold; font-size:19px'>"+smScorePoint+"</b>");
			icrSMScore.setCaptionAsHtml(true);
			
			smPrvPoints.setCaption("Previous Year ICR : "+ smPrvPoint);
			}
		}
		Label dummy = new Label();
		/*Label dummy1 = new Label();
		Label dummy2 = new Label();*/
		

		HorizontalLayout smLayout = new HorizontalLayout(icrSM,icrSMScore);
		
		smLayout.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) { 
				Window popup = new Window("Previous Year ICR:");
				popup.setWidth("15%");
				popup.setResizable(false);
				popup.setClosable(false);
				popup.center();
				HorizontalLayout resultLayout = new HorizontalLayout(smPrvPoints);
				Button close = new Button("Close");
				VerticalLayout resultLayout1 = new VerticalLayout(resultLayout,close);
				resultLayout1.setComponentAlignment(close, Alignment.BOTTOM_CENTER);
				close.setStyleName(ValoTheme.BUTTON_DANGER);
				popup.setContent(resultLayout1);
				close.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						popup.close();
					}
				});
				 popup.setModal(true);
					UI.getCurrent().addWindow(popup);
			}
		});
		
		HorizontalLayout icrLayout = new HorizontalLayout(icr,dummy,/*icrAgent,icrAgentScore,dummy1,icrBranch,icrBranchScore,dummy2,*/smLayout/*icrSM,icrSMScore*/);
		return icrLayout;
	}
	
	public static HorizontalLayout getInsuredChannedName(PreauthDTO bean){

		 Window popup = new com.vaadin.ui.Window();
		 popup.setCaption("Insured/Channel Name");
		 popup.setWidth("40%");
		 popup.setHeight("30%");
		 //popup.setContent(insuredChannelName);
		 popup.setClosable(true);
		 popup.center();
		 popup.setResizable(true);
		 popup.addCloseListener(new Window.CloseListener() {
			 /**
			  * 
			  */
			 private static final long serialVersionUID = 1L;

			 @Override
			 public void windowClose(CloseEvent e) {
				 System.out.println("Close listener called");
			 }
		 });

		 /*popup.setModal(true);
			UI.getCurrent().addWindow(popup);*/

		 Label insuredNamelbl = new Label("Insured Name : ");
		 Label agentIntermediaryNamelbl = new Label("Agent / Intermediary Name : ");
		 Label salesManagerNamelbl= new Label("Sales Manager Name :  ");

		 insuredNamelbl.setWidth("100%");
		 agentIntermediaryNamelbl.setWidth("100%");
		 salesManagerNamelbl.setWidth("100%");

		 Label insuredName = new Label();
		 insuredName.setWidth("100%");

		 if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getInsuredPatient() && null != bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()) {
			 insuredName.setValue(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()); 
		 }else{
			 insuredName.setValue("-");
		 }

		 Label agentIntermediaryName = new Label();
		 agentIntermediaryName.setWidth("100%");


		 if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() && null != bean.getNewIntimationDTO().getPolicy().getAgentName()) {
			 agentIntermediaryName.setValue(bean.getNewIntimationDTO().getPolicy().getAgentName()); 
		 }else{
			 agentIntermediaryName.setValue("-");
		 }

		 Label smName = new Label();
		 smName.setWidth("100%");

		 if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() && null != bean.getNewIntimationDTO().getPolicy().getSmName()) {
			 smName.setValue(bean.getNewIntimationDTO().getPolicy().getSmName()); 
		 }else{
			 smName.setValue("-");
		 }

		 VerticalLayout layout1 = new VerticalLayout(insuredNamelbl,agentIntermediaryNamelbl,salesManagerNamelbl);
		 VerticalLayout layout2 = new VerticalLayout(insuredName,agentIntermediaryName,smName);

		 //layout2.setStyleName("layoutDesign");
		 FormLayout leftForm = new FormLayout(layout1);
		 FormLayout rightForm = new FormLayout(layout2);
		 HorizontalLayout resultLayout = new HorizontalLayout(leftForm,rightForm);

		 Button ok = new Button("OK");
		 VerticalLayout resultLayout1 = new VerticalLayout(resultLayout,ok);
		 resultLayout1.setComponentAlignment(ok, Alignment.BOTTOM_CENTER);
		 //close.setStyleName(ValoTheme.BUTTON_DANGER);
		 Panel panel = new Panel(resultLayout1);
		 FormLayout resultForm = new FormLayout(resultLayout1);
		 //resultForm.setStyleName("layoutDesign");
		 popup.setContent(resultForm);
		 ok.addClickListener(new ClickListener() {

			 @Override
			 public void buttonClick(ClickEvent event) {
				 popup.close();
			 }
		 });
		 popup.setModal(true);
		 UI.getCurrent().addWindow(popup);

		 return resultLayout;
	}
	public static HorizontalLayout HsTraficImages(PreauthDTO bean){

		 DBCalculationService dbService = new DBCalculationService();
		 HorizontalLayout HSHLayout = new HorizontalLayout();

		 if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto()!=null && bean.getNewIntimationDTO().getHospitalDto().getHospitalCode()!=null){

			 List<Map<String, Object>> hsLisValues = dbService.getHopScrColrIcnByHoscode(bean.getNewIntimationDTO().getHospitalDto().getHospitalCode());

			 if(hsLisValues != null && !hsLisValues.isEmpty() && hsLisValues.size() > 0){
				 int i = 0 ;
				 Label [] sp = new Label[hsLisValues.size()];
				 for (Map<String, Object> hsDetVals :hsLisValues){
					 if(hsDetVals != null && hsDetVals.get(SHAConstants.HS_DESC).toString() != null && ! hsDetVals.get(SHAConstants.HS_DESC).toString().isEmpty()){
						 sp[i] = new Label();
						 sp[i].setCaptionAsHtml(true);
						 sp[i].setContentMode(ContentMode.HTML);
						 String span1 =hsDetVals.get(SHAConstants.HS_DESC).toString();
						 String span2 ="Average Score " + hsDetVals.get(SHAConstants.HS_PERC).toString();
						 String color = hsDetVals.get(SHAConstants.HS_COLR).toString();
						 sp[i].setDescription(span1 + System.lineSeparator() + span2);
						 if(color !=null && color.equalsIgnoreCase("Green")){
							 sp[i].setIcon(new ThemeResource("images/HSGreenCircle.png"));
						 }else if (color !=null && color.equalsIgnoreCase("Yellow")){
							 sp[i].setIcon(new ThemeResource("images/HSYellowCircle.png"));
						 }
						 else if (color !=null && color.equalsIgnoreCase("Red")){
							 sp[i].setIcon(new ThemeResource("images/HSRedCircle.png"));
						 }

						 HSHLayout.addComponent(sp[i]);
					 }
					 i++;
				 }

			 }
		 }

		 HSHLayout.setSpacing(true);
		 return HSHLayout;
	 }
	
	public static Date removeTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static int getDiffMonths(Date first, Date last) {
	    Calendar a = getCalendar(first);
	    Calendar b = getCalendar(last);
	    int diff = b.get(Calendar.MONTH) - a.get(Calendar.MONTH);
	    if (a.get(Calendar.DAY_OF_YEAR) > b.get(Calendar.DAY_OF_YEAR)) {
	        diff--;
	    }
	    return diff;
	}
	public static boolean isDateOfAdmissionWithPolicyRange(Date policyFromDate , Date policyToDate, Date enteredDate){
		
		if((enteredDate.after(policyFromDate) || DateUtils.isSameDay(enteredDate, policyFromDate)) &&
				(enteredDate.before(policyToDate) || DateUtils.isSameDay(enteredDate, policyToDate)))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static String formateDateForDispatch(Date createdDate){

		if(createdDate != null){
			return new SimpleDateFormat("dd-MM-yyyy HH:mm a").format(createdDate);
		}
		return null;
	}
	
	public static long getNoOfDaysBetweenDate(Date star, Date end) {
		 long daysBetween = 0L;
		try {
		    LocalDateTime date1 = LocalDateTime.ofInstant(star.toInstant(), ZoneId.systemDefault());
		    LocalDateTime date2 = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault());
		    daysBetween = Duration.between(date1, date2).toDays();
		    System.out.println ("Days: " + daysBetween);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return daysBetween;
	}
	
	public static String formateDateForDialerHistory(Date createdDate){
		
		if(createdDate != null){
			return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a").format(createdDate);
			}
		   	  return null;
	}
}