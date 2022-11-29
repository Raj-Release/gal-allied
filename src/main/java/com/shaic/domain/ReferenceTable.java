package com.shaic.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import com.shaic.arch.SHAConstants;
import com.shaic.main.navigator.domain.MenuItemBean;

public class ReferenceTable {
	public static String ADMISSION_TYPE = "ADMTYPE";

	public static String CURRENCY = "CURRENCY";
	public static String HOSPITAL_TYPE = "HOSPTYPE";
	public static String INTIMATED_BY = "INTITYPE";

	public static String MODE_OF_INTIMATION = "INTIMODE";
	public static String REASON_FOR_CONVERSION = "CONVRSN";
	public static String CASHLESS_REASON_FOR_CONVERSION = "RODCONVRSN";
	public static String RELATIONSHIP = "RELNSHIP";
	public static String ROOM_CATEGORY = "ROOMCTGY";
	public static String TREATMENT_MANAGEMENT = "TREATMGMT";
	public static String PRODUCT_TYPE = "PRODTYPE";
	public static String INTIMATION_SEARCH_BY = "INTISRCHBY";
	public static String OMP_INTIMATION_REJCTION = "OMP_REJCTGY";
	public static String OMP_UPLOAD_DOCLIST = "OMP";
	public static String OMP_RECIVED_STATUS = "OMP_RECSTS";
	public static String OMP_REOPEN_REASON = "OMP_REOPNRSN";
	public static String OMP_UPLOAD_FILETYPE = "OMP_FILETYPE";
	public static String OMP_MODE_OF_INTIMATION = "OMP_INTIMODE";
	public static String OMP_INTIMATED_BY = "OMP_INTITYPE";
	

	public static String GENDER = "GENDER";
	public static String RECORD_STATUS = "RCDSTS";
	public static String INTIMATION_YEAR = "INTIYEAR";
	public static String HOSPITAL_CATEGORY = "HOSPCTGY";
	public static String LINE_OF_BUSINESS = "LINOFBUS";
	public static String CLAIM_TYPE = "CLMTYPE";
	public static String PIO_CODE = "PIOCODE";
	public static String VERIFICATION_TYPE = "PYMNTVRFY";
	public static String VERIFICATION_REQUIRED = "Verification Required";
	public static String VERIFICATION_NOT_REQUIRED  = "Verification Not Required";


	public static String EMP_TYPE = "EMPTYPE";
	
	public static String NATURE_OF_TREATMENT = "NATURTRTMNT";
	public static String POLICY_TYPE = "POLTYPE";
	public static String PATIENT_STATUS = "PREPATSTS";
	public static String REIMBURSEMENT_PATIENT_STATUS = "RODPATSTS";
	public static String ALLOCATION_TO = "FVRASIGN";
	public static String ASSIGN_TO = "FVRALLOC";
	public static String FVR_PRIORITY = "FVRPROTY";

	public static Long ALLOCATION_TO_OUTSOURCE = Long.valueOf(1122L);
	public static Long ALLOCATION_TO_RVO = Long.valueOf(1124L);
	
	public static String CONFIRMED_BY = "CONFRMBY";
	public static String ALLOCATION_TO_INVESTIGATION = "INVALLOC";
	public static String REASON_FOR_INITIATE_INVESTIGATION = "RSNFRINV";
	public static Long INITIATE_INVESTIGATION_OTHERS = Long.valueOf(11187L);
	
	public static String NETWORK_HOSPITAL_TYPE = "NWHOSPTYPE";
	public static String REJECTION_CATEGORY = "REJCTGY";
	public static String REJECTION_RETAIL = "REJCTGY_RTL";
	public static String REJECTION_CARDIAC = "REJCTGY_CARDIAC";
	public static String REJECTION_CATEGORY_CNCR = "REJCTGY_CANCR";
	public static String REJECTION_CATEGORY_COMPHN = "REJCTGY_COMPREHN";
	public static String ENH_REJECTION_CATEGORY = "ENH_REJCTGY";
	
	public static String SETTLED_REJECTION_CATEGORY = "SETLREJCAT";
	public static String REIMB_REJECTION_CATEGORY = "REMREJCTGY";
	public static String DENIAL_REASON = "DENRSN";
	public static String DENIAL_REASON_RETAIL = "DENRSN_RTL";
	public static String ESCALATE_TO = "ESCALATE";
	public static String ESCALATE_TO_ROD = "RODESCALATE";
	public static String COORDINATOR_REQUEST_TYPE = "COREQTYPE";
	public static String WITHDRAWAL_REASON = "WDRLRSN";
	public static String WITHDRAWAL_REASON_RETAIL = "WDRLRSNRTL";
	public static String REJECT_WITHDRAWAL_REASON_RETAIL = "REJWDRL_RSN_RTL";
	public static String REJECT_WITHDRAWAL_REASON_RETAIL_CNCR = "REJWDRL_RSN_RTL_CANCR";
	public static String INTIMATION_SOURCE = "INTISRCE";
	public static String SPECIALIST_TYPE = "SPLTYPE";
	public static String EXCLUSION_DETAILS = "EXCLNDTLS";
	public static String IMPACT_ON_DIAGNOSI = "DIAGIMPT";
	public static String PED_IMPACT_ON_DIAGNOSI = "PEDIMPDIGNIS";
	public static String PED_NON_PAYABLE_REASON = "PEDPAYNOTCONS";
	public static String PROCEDURE_STATUS = "PRCDRSTS";
	public static String COMMON_VALUES = "CMNVAL";
	public static String PED_SUGGESTION = "PEDSUGTN";
	public static String PED_SOURCE = "PEDSRC";
	public static String PNC_RELATIONSHIP = "PNCREL";
	public static String ILLNESS = "ILLNESS";
	public static String SUM_INSURED = "SIRES";
	public static String INVESTIGATION_GRADING_CATEGORY= "INVGRADE";
	public static String FEEDBACKRATING = "BMFBRATING";

	public static String SPECIALIST_CONSULTED = "";

	public static String FVR_NOT_REQUIRED_REMARKS = "FVRNTREQREM";
	public static String MEDICAL_CATEGORY = "MDLCTGY";
	public static String TERMINATE_COVER = "TMTCVR";
	public static String DOWNSIZE_REASON = "DWNRSN";
	public static String DOWNSIZE_REASON_RETAIL = "DWNRSN_RTL";
	public static String HOSPITALIZATION_DUE_TO = "HOSPDUETO";
	public static String PRE_AUTH_TYPE = "STPPRCSLVL";   //INCLUSION OF NEW FIELD BASED ON CR2019075
	public static String CAUSE_OF_INJURY = "INJCAUSE";
	public static String MEDICAL_VERIFICATION = "MDLVRCN";
	public static String TREATMENT_QUALITY_VERIFICATION = "TRTQLTYVRCN";
	public static String CANCELLATION_REASON = "RODCNCLRSN";

	public static String SIGNIFICANT_CLINICAL_INFORMATION = "SIGCLNLINFO";
	public static String RRC_CATEGORY = "RRCCTGY";
	public static String RRC_ELIGIBLITY = "RRCELGTY";
	public static String RRC_CREDIT_TYPE = "RRCCRDTYPE";
	public static String RRC_BILL_DETAILS = "RRCBILDTLS";
	public static String RRC_REQUEST_TYPE = "RRCREQTYPE";
	
	public static String TALKCOMM = "TALKCOMM";
	
	public static Long REPORT_RESTRICTION_TIME_FROM = Long.valueOf(7056L);
	public static Long REPORT_RESTRICTION_TIME_TO = Long.valueOf(7057L);
	
	public static Long RESTRICTION_FLAG = Long.valueOf(7058L);
	

	public static String NORMAL = "normal";

	public static String DIRECT_TO_BILLING = "directToBilling";

	public static String DIRECT_TO_FINANCIAL = "directToFinancial";

	public static String ACK_DOC_RECEIVED_FROM = "DOCRECFRM";
	
  
  public static String PA_DOCUMENT_TYPE = "PADOCTYPE";
  
  public static String PA_ROD_UPLOAD_DOC_TABLE_FILE_TYPE = "PAFILETYPE";
  
  public static String INVESTIGATION_CLAIM_REPOPT_TABLE_FILE_TYPE = "INVCLMRPT";

	public static String ACK_DOC_MODE_OF_RECEIPT = "RECMODE";

	public static String ACK_DOC_RECEIVED_STATUS = "DOCRECSTS";

	public static String ROD_UPLOAD_DOC_TABLE_FILE_TYPE = "DOCTYPE";

	public static String STAR_UNIQUE_PRODUCT_CODE = "MED-PRD-023";
	
	public static String SENIOR_CITIZEN_REDCARPET_REVISED = "MED-PRD-070";

	public static String OP_BILL_DETAILS = "OPBILLTYPE";

	public static String REASON_FOR_RECONSIDERATION = "RECONSRSN";

	public static String REASON_FOR_CLOSING = "CLOSERSN";

	public static String REASON_FOR_REOPEN = "REOPNRSN";

	public static String REASON_FOR_CANCEL_ACKNOWLEDGEMENT = "CANACKRSN";

	public static String PAYMENT_STATUS = "PYNTSTS";
	public static String PAYMENT_MODE = "PAYMTMODE";

	public static String SECTION = "POLSEC";
	public static String DELIVERY_TYPE = "DLYTYPE";

	public static String DOCUMENT_TOKEN = "documentToken";
	public static String FILE_NAME = "fileName";

	public static String STAR_CARE_ICU_ROOM_RENT_LIMIT = "10000";

	public static String HEALTH_CARE_ROOM_RENT_LIMIT = "10000";

	public static String PREAUTH_STATUS = "Approve";

	public static String WITHDRAW = "W";

	public static String PROCEDURE = "procedure";

	public static String DIAGNOSIS = "diagnosis";
	public static String AGREED_NETWORK_HOSPITAL_TYPE = "Agreed Network Hospital";

	public static String PENAL_INTREST = "PENALINTRAT";

	public static String NETWORK_HOSPITAL_NAME = "Network Hospital";

	public static String NON_NETWORK_HOSPITAL_NAME = "Non-Network Hospital";
	
	public static String UPDATE_PAYMENT_TYPE = "UPD_PAYMENT"; 

	public static Long INVESTIGATION_STAGE = Long.valueOf(19L);

	public static Long INITIATE_INVESTIGATION = Long.valueOf(54L);

	public static Long INITIATE_INVESTIGATION_APPROVED = Long.valueOf(67L);

	public static Long INITIATE_INVESTIGATION_DIS_APPROVED = Long.valueOf(68L);

	public static Long INVESTIGATION_REPLY_RECEIVED = Long.valueOf(55L);
  
	public static Long ASSIGN_INVESTIGATION = Long.valueOf(56L);
	
	public static Long INVESTIGATION_GRADING = Long.valueOf(204l);

	public static Long FVR_REPLY_RECEIVED = Long.valueOf(50L);

	public static Long FHO_PRODUCT_REVISED = Long.valueOf(61L);
	
	public static Long POS_FAMILY_HEALTH_OPTIMA = Long.valueOf(81L);
	
	public static Long FHO_PRODUCT_REVISED_1 = Long.valueOf(72L);
	
	public static Long FHO_PRODUCT_REVISED_PACKAGED = Long.valueOf(213L);
	
	public static Long FHO_NEW_PRODUCT = Long.valueOf(40L);
	
	public static Long GPA_PRODUCT_KEY= Long.valueOf(111L);

	public static Long MCI_PRODUCT = Long.valueOf(18L);

	public static Long COMPREHENSIVE_PRODUCT = Long.valueOf(24L);

	public static Long CARDIAC_CARE_PRODUCT = Long.valueOf(23L);

	public static Long COMPREHENSIVE_37_PRODUCT = Long.valueOf(41L);
	
	public static Long COMPREHENSIVE_78_PRODUCT_FLOATER = Long.valueOf(99L);
	
	public static Long COMPREHENSIVE_78_PRODUCT_INDIVIDUAL = Long.valueOf(100L);

	public static Long COMPREHENSIVE_27_PRODUCT = Long.valueOf(24L);
	
	public static Long STAR_FIRST_COMPREHENSIVE_PRODUCT = Long.valueOf(63L);  
	
	public static Long STAR_HEALTH_GAIN_INDIVIDUAL_PRODUCT = Long.valueOf(78L);  
	
	public static Long STAR_HEALTH_GAIN_FLOATER_PRODUCT = Long.valueOf(79L);  
	
	public static Long JET_PRIVILEGE_PRODUCT =Long.valueOf(71L);
	
	public static Long FHO_PRODUCT_REVISED_TEMP = Long.valueOf(201L);
	
	public static Long SKIPFVR = Long.valueOf(52L);
	public static Long ASSIGNFVR = Long.valueOf(51L);
	public static Long INITITATE_FVR = Long.valueOf(49L);
	public static Long INVESTIGATION_DOCUMENT_TYPE_KEY = Long.valueOf(4023L);
	public static Long CLAIM_VERIFICATION_REPORT_DOCUMENT_TYPE_KEY = Long.valueOf(7521L);

	public static Long FVR_REPLAY_RECIEVED_STATUS = Long.valueOf(50L);

	public static Long PROCESS_PRE_MEDICAL = Long.valueOf(11L);
	public static Long PRE_MEDICAL_PROCESSING_ENHANCEMENT = Long.valueOf(12L);
	public static Long PROCESS_ENHANCEMENT_TYPE = Long.valueOf(14L);
	public static Long PROCESS_PREAUTH = Long.valueOf(13L);

	public static Long DENIAL_OF_CASHLESS_REASON_PREMIUM = Long.valueOf(4326L);

	public static Long INTIMATION_STAGE_KEY = Long.valueOf(8L);
	public static Long INTIMATION_SUBMIT_STATUS_KEY = Long.valueOf(8L);
	public static Long INTIMATION_SAVE_STATUS_KEY = Long.valueOf(9L);
	public static Long INTIMATION_PENDING_STATUS_KEY = Long.valueOf(10L);

	public static Long FVRCANCELLED = Long.valueOf(165L);
	public static Long FVR_NOT_REQUIRED = Long.valueOf(217L);
	public static Long CORP_LEVEL_CLOSE = Long.valueOf(218L);

	public static Long PRE_MEDICAL_PRE_AUTH_STAGE = Long.valueOf(11L);
	public static Long PRE_MEDICAL_PRE_AUTH_QUERY_STATUS = Long.valueOf(16L);
	public static Long PRE_MEDICAL_PRE_AUTH_QUERY_RECEIVED_STATUS = Long
			.valueOf(65L);
	public static Long PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS = Long
			.valueOf(17L);
	public static Long PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS = Long
			.valueOf(18L);
	public static Long PRE_MEDICAL_PRE_AUTH_REFER_TO_COORDINATOR_STATUS = Long
			.valueOf(60L);

	public static Long PRE_MEDICAL_PRE_AUTH_MEDICAL_CATEGORY = Long
			.valueOf(131L);

	public static Long PRE_MEDICAL_PRE_AUTH_NON_MEDICAL_CATEGORY = Long
			.valueOf(132L);

	public static final Long OMP_LOB_KEY = Long.valueOf(443L);
	
	public static Long CLAIM_REGISTRATION_STAGE = Long.valueOf(9L);
	public static Long CLAIM_REGISTERED_STATUS = Long.valueOf(11L);
	public static Long CLAIM_SUGGEST_REJECTION_STATUS = Long.valueOf(12L);

	public static Long PREAUTH_STAGE = Long.valueOf(13L);

	public static Long PREAUTH_APPROVE_STATUS = Long.valueOf(22L);
	public static Long PREAUTH_REJECT_STATUS = Long.valueOf(23L);
	public static Long PREAUTH_QUERY_STATUS = Long.valueOf(24L);
	public static Long PREAUTH_QUERY_RECEIVED_STATUS = Long.valueOf(25L);
	public static Long PREAUTH_DENIAL_OF_CASHLESS_STATUS = Long.valueOf(26L);
	public static Long PREAUTH_ESCALATION_STATUS = Long.valueOf(27L);
	public static Long PREAUTH_REFER_TO_COORDINATOR_STATUS = Long.valueOf(28L);
	public static Long PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS = Long
			.valueOf(29L);
	public static Long PREAUTH_REFER_TO_FLP_STATUS = Long.valueOf(180l);
	
	public static Long TOPUP_FLP_STATUS = Long.valueOf(273L);

	public static Long PREAUTH_CLOSED_STATUS = Long.valueOf(174L);
	public static Long PREAUTH_REOPENED_STATUS = Long.valueOf(175L);

	public static Long ENHANCEMENT_STAGE = Long.valueOf(14L);

	public static Long ENHANCEMENT_APPROVE_STATUS = Long.valueOf(30L);
	public static Long ENHANCEMENT_REJECT_STATUS = Long.valueOf(31L);
	public static Long ENHANCEMENT_QUERY_STATUS = Long.valueOf(32L);
	public static Long ENHANCEMENT_QUERY_RECEIVED_STATUS = Long.valueOf(33L);
	public static Long ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS = Long
			.valueOf(34L);
	public static Long ENHANCEMENT_ESCALATION_STATUS = Long.valueOf(35L);
	public static Long ENHANCEMENT_REFER_TO_COORDINATOR_STATUS = Long
			.valueOf(36L);
	public static Long ENHANCEMENT_COORDINATOR_REPLY_RECEIVED_STATUS = Long
			.valueOf(37L);
	public static Long ENHANCEMENT_PREAUTH_WITHDRAW_STATUS = Long.valueOf(64L);
	public static Long ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS = Long.valueOf(63L);
	public static Long ENHANCEMENT_REFER_TO_FLP_STATUS = Long.valueOf(181l);
	public static Long ENHANCEMENT_WITHDRAW_AND_REJECT = Long.valueOf(182l);

	public static Long STANDALONE_WITHDRAW_STAGE = Long.valueOf(16L);
	public static Long STANDALONE_WITHDRAW_STATUS = Long.valueOf(40L);
	public static Long STANDALONE_WITHDRAW_AND_REJECT_STATUS = Long
			.valueOf(183l);
	public static Long STANDALONE_WITHDRAW_POST_STATUS = Long.valueOf(270L);
	public static Long ENHANCEMENT_DOWNSIZE_STAGE = Long.valueOf(15L);
	public static Long ENHANCEMENT_DOWNSIZE_STATUS = Long.valueOf(38L);

	public static Long PRE_MEDICAL_ENHANCEMENT_STAGE = Long.valueOf(12L);
	public static Long PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS = Long.valueOf(19L);
	public static Long PRE_MEDICAL_ENHANCEMENT_QUERY_RECEIVED_STATUS = Long
			.valueOf(66L);
	public static Long PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS = Long
			.valueOf(20L);
	public static Long PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS = Long
			.valueOf(21L);
	public static Long PRE_MEDICAL_ENHANCEMENT_REFER_TO_COORDINATOR_STATUS = Long
			.valueOf(61L);

	public static Long CLAIM_REQUEST_STAGE = Long.valueOf(25L);
	public static Long CLAIM_REQUEST_APPROVE_STATUS = Long.valueOf(78L);
	public static Long CLAIM_REQUEST_REJECT_STATUS = Long.valueOf(93L);
	public static Long CLAIM_REQUEST_CANCEL_ROD_STATUS = Long.valueOf(148L);
	public static Long CLAIM_REQUEST_QUERY_STATUS = Long.valueOf(88L);
	public static Long CLAIM_REQUEST_QUERY_RECEIVED_STATUS = Long.valueOf(33L);
	public static Long CLAIM_REQUEST_ESCALATION_STATUS = Long.valueOf(86L);
	public static Long CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS = Long
			.valueOf(81L);
	public static Long CLAIM_REQUEST_COORDINATOR_REPLY_RECEIVED_STATUS = Long
			.valueOf(82L);
	public static Long CLAIM_REQUEST_ESCALATION_REPLY_STATUS = Long
			.valueOf(85L);
	public static Long CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS = Long
			.valueOf(79L);
	public static Long CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS = Long
			.valueOf(80L);
	public static Long CLAIM_REQUEST_SEND_REPLY_STATUS = Long.valueOf(87L);
	public static Long CLAIM_REQUEST_SEND_REPLY_CLAIM_APPROVAL_STATUS = Long.valueOf(185L);
	public static Long CLAIM_REQUEST_SEND_REPLY_FA_STATUS = Long.valueOf(123L);
	public static Long CLAIM_REQUEST_SPECIALIST_STATUS = Long.valueOf(83L);
	public static Long CLAIM_REQUEST_SPECIALIST_REPLY_RECEIVED_STATUS = Long
			.valueOf(84L);

	public static Long ZONAL_REVIEW_STAGE = Long.valueOf(24L);
	public static Long ZONAL_REVIEW_INITATE_INV_STATUS = Long.valueOf(203L); 
	public static Long ZONAL_REVIEW_QUERY_STATUS = Long.valueOf(73L);
	public static Long ZONAL_REVIEW_APPROVE_STATUS = Long.valueOf(75L);
	public static Long ZONAL_REVIEW_REJECTION_STATUS = Long.valueOf(74L);
	public static Long ZONAL_REVIEW_CANCEL_ROD = Long.valueOf(147L);
	public static Long ZONAL_REVIEW_REFER_TO_COORDINATOR_STATUS = Long
			.valueOf(76L);
	public static Long ZONAL_REVIEW_COORDINATOR_REPLY_STATUS = Long
			.valueOf(77L);

	public static Long BILLING_REFER_TO_COORDINATOR = Long.valueOf(98L);
	public static Long BILLING_COORDINATOR_REPLY_RECEIVED = Long.valueOf(99L);
	public static Long BILLING_REFER_TO_MEDICALA_APPROVER = Long.valueOf(100L);
	public static Long BILLING_SEND_TO_FINANCIAL_APPROVER = Long.valueOf(101L);
	public static Long BILLING_CANCEL_ROD = Long.valueOf(149L);
	public static Long BILLING_BENEFITS_APPROVED = Long.valueOf(102L);
	public static Long BILLING_STAGE = Long.valueOf(26L);
	public static Long CLAIM_APPROVAL_STAGE = Long.valueOf(33l);
	// Added for refer to bill entry.

	/*
	 * Since no status is available for refer to bill entry, as of now adding a
	 * dummy status valuel
	 */
	public static Long BILLING_REFER_TO_BILL_ENTRY = Long.valueOf(176l);
	public static Long FINANCIAL_REFER_TO_BILL_ENTRY = Long.valueOf(177l);
	
	public static Long ZMR_REFER_TO_BILL_ENTRY = Long.valueOf(208L);
	public static Long CLAIM_REQUEST_REFER_TO_BILL_ENTRY = Long.valueOf(209L);
	
	public static Long INTIMATION_SUBMITTED_VALUE = 1173l;

	public static Long FINANCIAL_STAGE = Long.valueOf(27L);
	public static Long FINANCIAL_APPROVE_STATUS = Long.valueOf(103L);
	public static Long FINANCIAL_REJECT_STATUS = Long.valueOf(117L);
	public static Long FINANCIAL_CANCEL_ROD = Long.valueOf(150L);
	public static Long FINANCIAL_QUERY_STATUS = Long.valueOf(112L);
	public static Long FINANCIAL_REFER_TO_COORDINATOR_STATUS = Long
			.valueOf(108L);
	public static Long FINANCIAL_COORDINATOR_REPLY_RECEIVED_STATUS = Long
			.valueOf(109L);
	public static Long FINANCIAL_INITIATE_FIELD_REQUEST_STATUS = Long
			.valueOf(106L);
	public static Long FINANCIAL_INITIATE_INVESTIGATION_STATUS = Long
			.valueOf(107L);
	public static Long FINANCIAL_REFER_TO_BILLING = Long.valueOf(105L);
	public static Long FINANCIAL_REFER_TO_MEDICAL_APPROVER = Long.valueOf(104L);
	public static Long FINANCIAL_REFER_TO_SPECIALIST = Long.valueOf(110L);
	public static Long FINANCIAL_SPECIALIST_REPLY_RECEIVED_STATUS = Long
			.valueOf(111L);
	public static Long FINANCIAL_SETTLED = Long.valueOf(161L);

	public static Long PED_APPROVED = Long.valueOf(59L);
	public static Long PED_INITIATE = Long.valueOf(41L);
	public static Long PED_ESCALATED = Long.valueOf(213L);                  // PED_ESCALATED
	public static Long PED_QUERY = Long.valueOf(42L);
	public static Long PED_QUERY_RECEIVED = Long.valueOf(43L);
	public static Long PED_REJECT = Long.valueOf(44L);
	public static Long REFER_TO_SPECIALIST = Long.valueOf(45L);
	public static Long SEND_TO_PROCESSOR = Long.valueOf(46L);
	public static Long SEND_TO_APPROVER = Long.valueOf(47L);
	public static Long ENDORSEMENT_PROCESSING = Long.valueOf(48L);
	public static Long SPECIALIST_REPLY_RECEIVED = Long.valueOf(62L);

	public static Long OP_STAGE = Long.valueOf(29L);
	public static Long OP_REGISTER_CLAIM_STATUS = Long.valueOf(128L);
	public static Long OP_APPROVE = Long.valueOf(129L);
	public static Long OP_REJECT = Long.valueOf(130L);
	public static Long OP_SETTLED = Long.valueOf(301L);

	public static Long VIEW_PREVIOUS_INTIMATION_STATUS_FOR_DRAFT = Long
			.valueOf(9L);

	public static Long PED_ENDORSEMENT_STAGE = Long.valueOf(17L);

	public static Long PED_WATCHLIST_PROCESSOR = Long.valueOf(168L);

	public static Long PED_WATCHLIST_APPROVER = Long.valueOf(169L);

	public static Long PED_WATCHLIST_RELEASE_PROCESSOR = Long.valueOf(170L);

	public static Long PED_WATCHLIST_RELEASE_APPROVER = Long.valueOf(171L);

	public static Long PED_OTHER_CATEGORY = Long.valueOf(124L);

	public static Long STAR_UNIQUE_PRODUCT_KEY = Long.valueOf(35L);

	public static Long SUPER_SURPLUS_INDIVIDUAL = Long.valueOf(33L);
	public static Long SUPER_SURPLUS_FLOATER = Long.valueOf(32L);
	
	public static Long SUPER_SURPLUS_REVISED_INDIVIDUAL = Long.valueOf(59L);
	public static Long SUPER_SURPLUS_REVISED_FLOATER = Long.valueOf(60L);
	public static Long SUPER_SURPLUS_INDIVIDUAL_2018 = Long.valueOf(87L);
	public static Long SUPER_SURPLUS_FLOATER_2018 = Long.valueOf(88L);
	
	

	public static Long PLANNED_ADMISSION_KEY = Long.valueOf(1003L);

	public static Long CLAIM_REGISTRATION_WAIVED = Long.valueOf(14L);

	public static Long CASHLESS_SETTLEMENT_BILL_KEY = Long.valueOf(4056L);

	public static Long SPECIALIST_DOCTOR = Long.valueOf(1581L);
	public static Long CMA1 = Long.valueOf(1582L);
	public static Long CMA2 = Long.valueOf(1583L);
	public static Long CMA3 = Long.valueOf(1584L);
	public static Long CMA4 = Long.valueOf(1585L);
	public static Long CMA5 = Long.valueOf(1586L);
	public static Long CMA6 = Long.valueOf(1587L);

	public static Long RMA1 = Long.valueOf(1802L);
	public static Long RMA2 = Long.valueOf(1803L);
	public static Long REIMBURSEMENT_ESCALATE_SPECIALIST = Long.valueOf(1801L);
	public static Long RMA3 = Long.valueOf(1804L);
	public static Long RMA4 = Long.valueOf(1805L);
	public static Long RMA5 = Long.valueOf(1806L);
	public static Long RMA6 = Long.valueOf(1807L);

	public static Long PRE_HOSPITALIZATION = Long.valueOf(9L);
	public static Long POST_HOSPITALIZATION = Long.valueOf(10L);
	public static Long HOSPITALIZATION = Long.valueOf(8L);
	public static Long LUMPSUM = Long.valueOf(11L);
	public static Long OTHER_BENEFIT = Long.valueOf(12L);
	public static Long HOSPITAL_CASH_BILL_ID = Long.valueOf(13L);

	public static Long INJURY_MASTER_ID = Long.valueOf(421L);
	public static Long ILLNESS_MASTER_ID = Long.valueOf(422L);
	public static Long MATERNITY_MASTER_ID = Long.valueOf(423L);
	public static Long ASSISTED_REPRODUCTION_TREATMENT = Long.valueOf(463L);

	public static Long NETWORK_HOSPITAL_TYPE_ID = Long.valueOf(121L);
	public static Long NON_NETWORK_HOSPITAL_TYPE_ID = Long.valueOf(122L);
	public static Long NOT_REGISTERED_HOSPITAL_TYPE_ID = Long.valueOf(123L);
	public static Long ANH_HOSPITAL_TYPE_ID = Long.valueOf(1701L);
	public static Long GREEN_CHANNEL_HOSPITAL_TYPE_ID = Long.valueOf(1702L);

	public static Long DOMICILIARY_ID = Long.valueOf(72L);

	public static Integer PATIET_VERIFIED = Integer.valueOf(8);
	public static Integer DIAGNOSIS_VERIFIED = Integer.valueOf(9);
	public static Integer ROOM_CATEGORY_VERIFIED = Integer.valueOf(10);
	public static Integer TRIGGER_POINTS_FOCUSED = Integer.valueOf(11);
	public static Integer PRE_EXISITNG_DISEASE_VERIFIED = Integer.valueOf(2151);
	public static Integer PATIENT_DISCHARGED = Integer.valueOf(13);
	public static Integer PATIENT_NOT_ADMITTED = Integer.valueOf(14);
	public static Integer OUTSTANDING_FVR = Integer.valueOf(15);

	public static Long POL_SECTION_1 = Long.valueOf(1761L);

	public static Long POL_SECTION_2 = Long.valueOf(1762L);

	public static Long OUT_PATIENT = Long.valueOf(403L);
	public static Long HEALTH_CHECK_UP = Long.valueOf(404L);
	public static Long CASHLESS_CLAIM_TYPE_KEY = Long.valueOf(401L);
	public static Long REIMBURSEMENT_CLAIM_TYPE_KEY = Long.valueOf(402L);

	public static Long DELUXE = Long.valueOf(2951L);
	public static Long SEMI_PRIVATE_ROOM_AC = Long.valueOf(2952L);
	public static Long SPECIAL_ROOM_NON_AC = Long.valueOf(2953L);
	public static Long SINGLE_ROOM_NON_AC = Long.valueOf(2954L);
	public static Long GENERAL_WARD = Long.valueOf(2955L);
	public static Long SINGLE_ROOM_AC = Long.valueOf(2956L);
	public static Long SHARING_OCCUPANCY_SINGLE_ROOM_NON_AC = Long
			.valueOf(2957L);
	public static Long SHARING_SEMI_PRIVATE_ROOM_NON_AC = Long.valueOf(2958L);

	public static Long COMMONMASTER_YES = Long.valueOf(1021L);
	public static Long COMMONMASTER_NO = Long.valueOf(1022L);
	public static Long OTHERSPECIFY = Long.valueOf(1728L);

	public static Long RENEWAL_POLICY = Long.valueOf(2802L);

	public static Long PATIENT_NOT_ADMITTED_FOR_WITHDRAW = Long.valueOf(1863L);
	
	public static Long PATIENT_NOT_ADMITTED_FOR_WITHDRAW_RETAIL = Long.valueOf(1552L);

	public static Long UPLOAD_INVESIGATION_COMPLETED = Long.valueOf(58L);

	public static Long PAYMENT_MODE_CHEQUE_DD = Long.valueOf(461L);

	public static Long PAYMENT_MODE_BANK_TRANSFER = Long.valueOf(462L);

	public static String CLAIM_TYPE_CASHLESS = "Cashless";
	public static Long CLAIM_TYPE_CASHLESS_ID = Long.valueOf(401L);
	public static String CLAIM_TYPE_REIMBURSEMENT = "Reimbursement";
	
  public static final Long CONSUMABLES = Long.valueOf(17L);
  public static final Long CONSUMABLES_OUTSIDE_HOSPITAL = Long.valueOf(17L);
  public static final Long CONSUMABLES_INSIDE_HOSPITAL = Long.valueOf(44L);
  public static final Long TAXES_AND_OTHER_CESS = Long.valueOf(45L);

  public static final Long IMPLANT_STUNT_VALVE = Long.valueOf(18L);

	public static String CHEQUE_DD = "Cheque";
	public static String PAYMENT_TYPE_CHEQUE = "CHEQUE";

	public static String BANK_TRANSFER = "NEFT";
	public static final String HOSPITAL_NETWORK = "Network";
	public static final String HOSPITAL_NON_NETWORK = "Non-Network";
	public static final String CASHLESS_CLAIM = "CASHLESS";
	public static final String REIMBURSEMENT_CLAIM = "REIMBURSEMENT";
	public static final String ACKNOWLEDGE_DOC_RECEIVED = "AcknowledgeDocReceived";
	public static final String CREATE_ROD = "CreateROD";
	public static final String MEDICAL = "Medical";
	public static final Long MEDICAL_CODE = Long.valueOf(181L);

	public static final Long ACKNOWLEDGE_STAGE_KEY = Long.valueOf(21L);

	public static final Long ACKNOWLEDGE_STATUS_KEY = Long.valueOf(70L);

	public static final Long CREATE_ROD_STAGE_KEY = Long.valueOf(22L);

	public static final Long CREATE_ROD_STATUS_KEY = Long.valueOf(71L);

	public static final Long BILL_ENTRY_STAGE_KEY = Long.valueOf(23L);

	public static final Long BILL_ENTRY_STATUS_KEY = Long.valueOf(72L);
	public static final String UPLOAD_INVESTIGATION_SCREEN = "Upload investigation Report Screen";
	public static final String SURGICAL = "Surgical";
	public static final Long SURGICAL_CODE = Long.valueOf(182L);
	public static final String ACK_TASK_BPM_FORM_NAME = "ACK_ROD";
	public static final String CLAIM_BILLING_BENEFITS = "CLAIM_BILLING_BENEFITS";
	public static final String BILL_ENTRY = "BILL_ENTRY";
	public static final String ROD_CREATION = "CREATE_ROD";
	public static final String BENEFITS_HOSPITAL_CASH = "HC";
	public static final String BENEFITS_PATIENT_CARE = "PC";
	public static final String PROCESS_CLAIM_REQUEST_BENEFITS = "PROCESS_CLAIM_REQUEST_BENEFITS";
	public static final String HOSPITAL_CASH = "Hospital Cash";
	public static final String PATIENT_CARE = "Patient Care";
	public static String PROVISION_AMOUNT_PERCENTAGE = "PROVAMT";
	
	
	public static final String PCC_ZONAL_MEDICAL_HEAD_SCREEN = "PCC Zonal medical head Screen";
	public static final String PCC_ZONAL_COORDINATOR_SCREEN = "PCC Zonal Coordinator Screen";
	public static final String PCC_HRM_COORDINATOR_SCREEN = "PCC HRM Coordinator Screen";

	public static String RRC_CONTRIBUTOR_TYPE = "RRCCNTRTYPE";

	public static final Long FIELD_VISIT_REPRESENTATION = Long.valueOf(25L);

	public static final Long RECEIVED_FROM_INSURED = Long.valueOf(1541L);

	public static final Long RECEIVED_FROM_HOSPITAL = Long.valueOf(1542L);
	public static final String SUPER_SURPLUS_INDIVIDUAL_CODE = "MED-PRD-016";
	public static final String SUPER_SURPLUS_FLOATER_CODE = "MED-PRD-018";
	
	public static final Long ROOM_CATEGORY_ID = Long.valueOf(8L);
	public static final Long ICU_ROOM_CATEGORY_ID = Long.valueOf(10L);
	public static final Long ICCU_ROOM_CATEGORY_ID = Long.valueOf(86L);

	public static final Long ROOM_BILL_TYPE = Long.valueOf(8L);
	public static final Long NURSING_BILL_TYPE = Long.valueOf(9L);

	public static final Long ICU_ROOM_BILL_TYPE = Long.valueOf(10L);
	public static final Long ICU_NURSING_BILL_TYPE = Long.valueOf(11L);
	
	//For CR1284 Addition of ICCU room rent anf nursing charges
	public static final Long ICCU_ROOM_BILL_TYPE = Long.valueOf(86L);
	public static final Long ICCU_NURSING_BILL_TYPE = Long.valueOf(85L);

	public static final Long ROOM_RENT_NURSING_CHARGES = Long.valueOf(8L);
	public static final Long ICU_CHARGES = Long.valueOf(10L);
	public static final Long ICCU_CHARGES = Long.valueOf(47L);

	public static final Long OT_CHARGES = Long.valueOf(12L);

	public static final Long PROFESSIONAL_CHARGES = Long.valueOf(13L);

	public static final Long INVESTIGATION_DIAG_WITHIN_HOSPITAL = Long
			.valueOf(14L);
	public static final Long INVESTIGATION_DIAG_OUTSIDE_HOSPITAL = Long
			.valueOf(15L);

	public static final Long MEDICINES = Long.valueOf(16L);
	public static final Long MEDICINES_OUTSIDE_HOSPITAL = Long.valueOf(16L);
	public static final Long MEDICINES_INSIDE_HOSPITAL = Long.valueOf(43L);


  public static Long PROCESS_CLAIM_FINANCIAL_DRAFT_QUERY_STATUS = Long.valueOf(113L);
  public static Long PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS = Long.valueOf(114L);
  public static Long PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS = Long.valueOf(115L);
  public static Long PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS = Long.valueOf(116L);  
  
  public static Long CLAIM_APPROVAL_QUERY_DRAFT_STATUS = Long.valueOf(192L);
  public static Long CLAIM_APPROVAL_QUERY_REDRAFT_STATUS = Long.valueOf(193L);
  public static Long CLAIM_APPROVAL_QUERY_REJECT_STATUS = Long.valueOf(194L);
  public static Long CLAIM_APPROVAL_QUERY_APPROVE_STATUS = Long.valueOf(195L);  
  public static Long CLAIM_APPROVAL_FIELD_VISIT_STATUS = Long.valueOf(205L);

  public static Long CLAIM_APPROVAL_DRAFT_REJECT_STATUS = Long.valueOf(197L);
  public static Long CLAIM_APPROVAL_REDRAFT_REJECT_STATUS = Long.valueOf(198L);
  public static Long CLAIM_APPROVAL_DISAPPROVE_REJECT_STATUS = Long.valueOf(199L);
  public static Long CLAIM_APPROVAL_APPROVE_REJECT_STATUS = Long.valueOf(200L);  
  
  public static Long PROCESS_CLAIM_FINANCIAL_DRAFT_REJECT_STATUS = Long.valueOf(118L);
  public static Long PROCESS_CLAIM_FINANCIAL_REDRAFT_REJECT_STATUS = Long.valueOf(119L);
  public static Long PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS = Long.valueOf(120L);
  public static Long PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS = Long.valueOf(121L);

	public static final Long AMBULANCE_FEES = Long.valueOf(19L);
	public static final Long ANH_PACKAGES = Long.valueOf(20L);
	public static final Long COMPOSITE_PACKAGES = Long.valueOf(21L);
	public static final Long OTHER_PACKAGES = Long.valueOf(22L);

	public static final Long PROCEDURES = Long.valueOf(23L);
	public static final Long MISC_WITHIN_HOSPITAL = Long.valueOf(24L);

	public static final Long MISC_OUTSIDE_HOSPITAL = Long.valueOf(25L);
	public static final Long OTHERS = Long.valueOf(26L);

	public static final Long PRE_NATAL_EXPENSES = Long.valueOf(41L);
	public static final Long POST_NATAL_EXPENSES = Long.valueOf(42L);

	public static final Long HOSPITAL_DISCOUNT = Long.valueOf(27L);

	public static final Long DEDUCTIONS = Long.valueOf(28L);

	public static final Long SENIOR_CITIZEN_RED_CARPET = Long.valueOf(30L);

	public static final Long SENIOR_CITIZEN_RED_CARPET_PRODUCT = Long
			.valueOf(54L);

	public static final Long MICRO_INS_IND_OT_TILL_DEDUCTIONS = Long
			.valueOf(29L);

	public static final Long FVR_STAGE_KEY = Long.valueOf(18L);

	public static final Long BILLING_PROCESS_STAGE_KEY = Long.valueOf(23L);

	public static final Long PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY = Long
			.valueOf(24L);

	public static final Long PROCESS_CLAIM_REQUEST_STAGE_KEY = Long
			.valueOf(25L);

	public static final Long PROCESS_CLAIM_FINANCIAL_STAGE_KEY = Long
			.valueOf(27L);

	public static final Long PATIENT_STATUS_ADMITTED = Long.valueOf(141L);
	public static final Long PATIENT_STATUS_DECEASED = Long.valueOf(142L);

	public static final Long PATIENT_STATUS_ADMITTED_REIMB = Long.valueOf(151L);
	public static final Long PATIENT_STATUS_DECEASED_REIMB = Long.valueOf(152L);

	public static final Long PATIENT_STATUS_DISCHARGED = Long.valueOf(151L);

	public static Long PROCESS_REJECTED = Long.valueOf(13L);

	public static Long PROCESS_CLAIM_REQUEST_DRAFT_QUERY_STATUS = Long
			.valueOf(89L);
	public static Long PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS = Long
			.valueOf(90L);
	public static Long PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS = Long
			.valueOf(91L);
	public static Long PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS = Long
			.valueOf(92L);
	public static Long PROCESS_CLAIM_APPROVAL_QUERY_APPROVE_STATUS = Long
			.valueOf(33L);

	public static Long PROCESS_CLAIM_REQUEST_DRAFT_REJECT_STATUS = Long
			.valueOf(94L);
	public static Long PROCESS_CLAIM_REQUEST_REDRAFT_REJECT_STATUS = Long
			.valueOf(95L);
	public static Long PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS = Long
			.valueOf(96L);
	public static Long PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS = Long
			.valueOf(97L);
	
	public static Long PAYMENT_REJECTED = Long.valueOf(225L);
	public static  Long OMPPAYTO_INSURED =Long.valueOf(7001L);


	public static Long PREMEDICAL_WAIVED_REJECTION = Long.valueOf(15L);

	public static Long WITHDRAW_STAGE = Long.valueOf(16L);
	public static Long DOWNSIZE_STAGE = Long.valueOf(15L);
	public static Long WITHDRAW_APPROVED_STATUS = Long.valueOf(64L);

	public static Long DOWNSIZE_APPROVED_STATUS = Long.valueOf(38L);
	public static Long DOWNSIZE_ESCALATION_STATUS = Long.valueOf(39L);

	public static Long RRC_REQUEST_STATUS_FRESH = Long.valueOf(1181L);
	public static Long RRC_REQUEST_STATUS_SECOND_ROD = Long.valueOf(1182L);
	public static Long RRC_REQUEST_STATUS_ON_HOLD = Long.valueOf(1183L);

	public static Map<Long, Long> PACKAGE_BILL_TYPE_NUMBER = getPackageNumbers();

	//public static Map<Long, String> DEFAULT_CO_PAY_VALUES = getDefaultCoPayValues();

	public static Map<Long, Long> PREMIUM_DEDUCTION_PRODUCT_KEYS = getPremiumDeductionProductKeys();

	public static Long AMBULANCE_BILL_TYPE_NUMBER = Long.valueOf(19L);

	public static Long INTIATE_RRC_REQUEST_STATUS = Long.valueOf(124L);
	public static Long RRC_REQUEST_PROCESS_STATUS = Long.valueOf(125L);
	public static Long RRC_REQUEST_HOLD_STATUS = Long.valueOf(126L);
	public static Long RRC_REQUEST_REVIEWED_STATUS = Long.valueOf(127L);                     
	public static Long PROCESS_REJECTION_STAGE = Long.valueOf(10L);
	public static Long ACKNOWLEDGE_HOSPITAL_STAGE = Long.valueOf(20L);
	public static Long ACKNOWLEDGE_HOSPTIAL_STATUS = Long.valueOf(69L);

	public static Long FRESH_POLICY = Long.valueOf(2801L);

	public static Long RRC_STAGE = Long.valueOf(28L);

	//public static Long RELATED_TO_EARLIER_CLAIMS = Long.valueOf(155L);
	
	public static Long RELATED_TO_EARLIER_CLAIMS = Long.valueOf(1091L);

	public static Long INDIVIDUAL_POLICY = Long.valueOf(2901L);

	public static Long FLOATER_POLICY = Long.valueOf(2903L);

	public static Long LATE_INTIMATION_KEY = Long.valueOf(1002L);

	public static Long CREATE_ROD_CLOSED_STATUS = Long.valueOf(132L);

	public static Long BILL_ENTRY_PROCESS_CLOSED_STATUS = Long.valueOf(133L);

	public static Long PROCESS_CLAIM_REQUEST_CLOSED_STATUS = Long.valueOf(134L);

	public static Long ZONAL_REVIEW_PROCESS_CLAIM_REQUEST_CLOSED_STATUS = Long
			.valueOf(135L);

	public static Long BILLING_CLOSED_STATUS = Long.valueOf(136L);

	public static Long FINANCIAL_CLOSED_STATUS = Long.valueOf(137L);

	public static Integer PAGE_SIZE = Integer.valueOf(10);

	public static Long STAR_CARE_INVIDUAL = Long.valueOf(45L);

	public static Long STAR_CARE_FLOATER = Long.valueOf(46L);

	public static Long HEALTH_ALL_CARE = Long.valueOf(49L);

	public static Long DIALYSIS_SUB_LIMIT = Long.valueOf(107L);

	public static Long SUBLIMIT_INFECTIOUS_1 = Long.valueOf(445L);

	public static Long NON_ALLOPATHIC_ID = Long.valueOf(1682L);

	public static Long SUBLIMIT_INFECTIOUS_2 = Long.valueOf(433L);

	public static Long STAR_CARE_DELITE = Long.valueOf(27L);

	public static Long UPLOAD_INVESTIGATION_STAGE = Long.valueOf(19L);

	public static Long COORDINATOR_PROCESS_REPLY_STAGE = Long.valueOf(30L);

	public static Long SPECIALIST_PROCESS_STAGE = Long.valueOf(31L);

	public static Long PREMEDICAL_QUERY_STAGE = Long.valueOf(11L);

	public static Long PREAUTH_QUERY_STAGE = Long.valueOf(13L);

	public static Long PREMEDICAL_ENHANCEMENT_QUERY_STAGE = Long.valueOf(12L);

	public static Long PREAUTH_ENHANCEMENT_QUERY_STAGE = Long.valueOf(14L);

	public static Long MA_CORPORATE_QUERY_STAGE = Long.valueOf(25L);

	public static Long FINANCIAL_QUERY_STAGE = Long.valueOf(27L);

	public static Long FVR_STAGE = Long.valueOf(18L);

	public static Long MA_CORPORATE_FVR_STAGE = Long.valueOf(25L);

	public static Long FINANCIAL_FVR_STAGE = Long.valueOf(27L);

	public static Long PREMEDICAL_PREAUTH_COORDINATOR_REPLY = Long
			.valueOf(138L);

	public static Long PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY = Long
			.valueOf(139L);

	public static Long REFER_TO_FLE = Long.valueOf(181L);

	public static Long REFER_TO_FLP = Long.valueOf(180L);

	public static Long NETWORK_HOSPITAL = Long.valueOf(1703L);

	public static Long AGREED_NETWORK_HOSPITAL = Long.valueOf(1701L);

	public static Long GREEN_NETWORK_HOSPITAL = Long.valueOf(1702L);

	public static Long FVR_ALLOCATION_TO = Long.valueOf(1901L);

	public static Long CALL_CENTRE_SOURCE = Long.valueOf(1641L);

	public static Long CREATE_ROD_CLOSED = Long.valueOf(132L);
	public static Long BILL_ENTRY_CLOSED = Long.valueOf(133L);
	public static Long ZONAL_REVIEW_CLOSED = Long.valueOf(134L);
	public static Long CLAIM_REQUEST_CLOSED = Long.valueOf(135L);
	public static Long BILLING_CLOSED = Long.valueOf(136L);
	public static Long FINANCIAL_CLOSED = Long.valueOf(137L);

	public static Long CREATE_ROD_REOPENED = Long.valueOf(141L);
	public static Long BILL_ENTRY_REOPENED = Long.valueOf(142L);
	public static Long ZONAL_REVIEW_REOPENED = Long.valueOf(143L);
	public static Long CLAIM_REQUEST_REOPENED = Long.valueOf(144L);
	public static Long BILLING_REOPENED = Long.valueOf(145L);
	public static Long FINANCIAL_REOPENED = Long.valueOf(146L);
	public static Long CLAIM_APPROVAL_STAGE_REOPENED = Long.valueOf(216L);

	public static Long DOWNSIZE_SUBMIT_SPECIALIST_REPLY = Long.valueOf(131L);

	public static Long CANCEL_ACKNOWLEDGEMENT_STATUS = Long.valueOf(151L);

	public static Long HOSPITAL_PORTAL = Long.valueOf(1642L);

	public static Long PAYMENT_SETTLED = Long.valueOf(158L);

	public static Long PED_PENDING_ENDORSEMENT_STATUS = Long.valueOf(152L);
	public static Long PED_COMPLETED_ENDORSEMENT_STATUS = Long.valueOf(153L);

	public static Map<Long, Long> CANCEL_ROD_KEYS = getCancelRODKeys();

	public static Map<Long, Long> CLOSE_CLAIM_STATUS_KEYS = getCloseClaimKeys();

	public static Map<Long, Long> RE_OPEN_CLAIM_STATUS_KEYS = getReopenClaimKeys();

	public static Map<Long, Long> REFER_TO_FLP_KEYS = getReferToFLPKeys();

	public static Long MICRO_INSURANCE_GROUP = Long.valueOf(50L);

	public static Long SENIOR_CITIZEN_POLICY = Long.valueOf(54L);

	public static Long PORTABILITY_POLICY_TYPE = Long.valueOf(2804L);

	public static Long MICRO_INSURANCE_INDIVIDUAL = Long.valueOf(42L);

	public static Long PAYMENT_PROCESS_STAGE = Long.valueOf(32L);

	public static Long LOT_CREATED_STATUS = Long.valueOf(154L);

	public static Long BATCH_CREATED_STATUS = Long.valueOf(155L);

	public static Long PAYMENT_NEW_STATUS = Long.valueOf(157L);

	public static Long FEMALE_GENDER = Long.valueOf(112L);
	
	public static Long MALE_GENDER = Long.valueOf(111L);
	
	public static Long TRANS_GENDER = Long.valueOf(113L);

	public static Long WEDDING_GIFT_PRODUCT_KEY = Long.valueOf(37L);

	public static Long PAYMENT_STATUS_FRESH = Long.valueOf(1161L);

	public static Long CORRECTION_PAYMENT_STATUS_ID = Long.valueOf(1162L);

	public static Long PAYMENT_STATUS_HOLD_PENDING = Long.valueOf(1163L);

	public static Long SEND_FOR_PAYMENT_APPROVAL_STATUS = Long.valueOf(156L);

	public static Long PAYMENT_RECONSIDERATION = Long.valueOf(159L);

	public static Long PAYMENT_CANCELLED = Long.valueOf(160L);

	public static Long INTIMATION_REGISTERED_STATUS = Long.valueOf(7L);
	
	 public static Long PA_HEALTH_CASHLESS_SETTLEMENT_BILL_KEY = Long.valueOf(4453L);

//	public static Map<Long, Long> REJECT_ROD_KEYS = getRejectedRODKeys();
//
//	public static Map<Long, Long> SECTION_PRODUCT_KEYS = getSectionKeys();
//
//	public static Long DIABETES_FLOATER_POLICY = Long.valueOf(53L);
//	public static Long DIABETES_INDIVIDUAL_POLICY = Long.valueOf(52L);
//	public static Long DIABETES_INDIVIDUAL_ANOTHER_POLICY = Long.valueOf(13L);
//	public static Long STAR_CARDIAC_CARE_POLICY = Long.valueOf(23L);
//	public static Long MA_CORPORATE_QUERY_REPLY_STATUS = Long.valueOf(162L);
//	public static Long FA_QUERY_REPLY_STATUS = Long.valueOf(163L);
//
//	public static Long PATIENT_NOT_ADMITTED_KEY = Long.valueOf(1863L);
//
//	public static Long STAR_WEDDING_GIFT_INSURANCE = Long.valueOf(37L);
//
//	public static Long CLAIM_REJECTION_STAGE = Long.valueOf(10L);
//
//	public static Long REIMBURSEMENT_SETTLED_STATUS = Long.valueOf(161L);
//
//	public static Long REIMBURSEMENT_PAYMENT_RECONSIDERATION_STATUS = Long
//			.valueOf(159L);
//
//	public static Long PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL = Long
//			.valueOf(121L);
//
//	public static Long PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL = Long
//			.valueOf(122L);
//
//	public static Long INR_CURRENCY_CODE = Long.valueOf(1053L);
//
//	public static Long ACK_DOC_NOT_APPLICABLE = Long.valueOf(1564L);
//	public static Long ACK_DOC_NOT_RECEIVED = Long.valueOf(1563L);
//	public static Long ACK_DOC_RECEIVED_PHOTOCOPY = Long.valueOf(1562L);
//	public static Long ACK_DOC_RECEIVED_ORIGINAL = Long.valueOf(1561L);
//	public static Long CLAIM_TYPE_REIMBURSEMENT_ID = Long.valueOf(402L);
//
//	public static Long PREAUTH_NOT_RECEIVED = Long.valueOf(2001L);
//	public static Long POST_WITHDRAW = Long.valueOf(2002L);
//	public static Long POST_DENIAL = Long.valueOf(2003L);
//	public static Long POST_CASHLESS_QUERY = Long.valueOf(2004L);
//
//	// Added for defaulting NOT APPLICABLE IN Document checklist table in Ack
//	// screen
//	public static Long DOCUMENT_CHECKLIST_NOT_APPLICABLE = Long.valueOf(1564L);
//
//	public static Long DOC_RECEIVED_TYPE_HOSPITAL = Long.valueOf(1542l);
//
//	public static Long CLAIM_CLOSED_STATUS = Long.valueOf(164l);
//
//	public static Long CLAIM_REOPENED_STATUS = Long.valueOf(173L);
//
//	public static Long KERALA_ZONE_CPU_CODE = Long.valueOf(950004l);
//
//	public static Long CLEAR_CASHLESS_STATUS_FOR_PREAUTH = Long.valueOf(166l);
//
//	public static Long CLEAR_CASHLESS_STATUS_FOR_ENHANCEMENT = Long
//			.valueOf(167l);
//
//	public static final Long ADD_ADDITIONAL_DOCUMENTS_STATUS = Long
//			.valueOf(178l);
//
//	public static Map<Long, Long> WITHDRAW_KEYS = getWithdrawnKeys();
//
//	public static final Long RECEIVED_PHOTOCOPY = Long.valueOf(1562);
//
//	public static final Long RECEIVED_ORIGINAL = Long.valueOf(1561);

	public static final Long PREAUTH_REFER_64_VB_COMPLIANCE = Long.valueOf(3173);
	
	public static final Long ENHANCEMENT_REFER_64_VB_COMPLIANCE = Long.valueOf(3174);

	public static final Long VB_COMPLIANCE_TO_PREAUTH = Long.valueOf(3175);
	
	public static final Long VB_COMPLIANCE_TO_ENHANCEMENT = Long.valueOf(3176);
	
	public static final Long GMC_CPU_CODE = Long.valueOf(27L);
	
	public static final Long PAAYAS_CPU_CODE = Long.valueOf(31L);
	
	public static final Long TATA_TRUST_CPU_CODE = Long.valueOf(33L);
	
    public static final Long JIO_CPU_CODE = Long.valueOf(32L);
	
	public static final Long JET_PRIVILEGE_CPU_CODE = Long.valueOf(27L);
	
	public static final Long GPA_SUNGARD_POLICY_CPU_CODE = Long.valueOf(19L);
	// SECTION AND COVER AND SUB COVER Constants

	// SEC-01 - HOSPITALIZATON
	// SEC-02 - DELIVERY EXPENSES
	// SEC-03 - DENTAL OPTAHLMIC TREATMENT
	// SEC-04 - HOSPITAL CASH
	// SEC-05 - HEALTH CHECKUP
	// SEC-06 - BARIATRIC
	// SEC-07 - ACCIDENTAL
	// SEC-08 - LUMPSUM

	// SEC-COV-01 - HOSPITALIZATION
	// SEC-COV-02 - MATERNITY
	// SEC-COV-03 - NEW BORN
	// SEC-COV-04 - NEW BORN CHILD VACCINATION
	// SEC-COV-05 - DENTAL OPTHALMIC
	// SEC-COV-06 - HOSPITAL CASH
	// SEC-COV-07 - HEALTH CHECKUP
	// SEC-COV-08 - BARIATRIC
	// SEC-COV-09 - ACCIDENTAL
	// SEC-COV-10 - LUMPSUM


  public static Map<Long, Long> REJECT_ROD_KEYS = getRejectedRODKeys();

  public static Map<Long, Long> SECTION_PRODUCT_KEYS = getSectionKeys();

  public static Long DIABETES_FLOATER_POLICY = Long.valueOf(53L);
  public static Long DIABETES_INDIVIDUAL_POLICY = Long.valueOf(52L);
  public static Long DIABETES_INDIVIDUAL_ANOTHER_POLICY = Long.valueOf(13L);
  public static Long STAR_CARDIAC_CARE_POLICY = Long.valueOf(23L);
  public static Long MA_CORPORATE_QUERY_REPLY_STATUS = Long.valueOf(162L);
  public static Long FA_QUERY_REPLY_STATUS = Long.valueOf(163L);

  public static Long PATIENT_NOT_ADMITTED_KEY = Long.valueOf(1863L);

  public static Long STAR_WEDDING_GIFT_INSURANCE = Long.valueOf(37L);

  public static Long CLAIM_REJECTION_STAGE = Long.valueOf(10L);

  public static Long REIMBURSEMENT_SETTLED_STATUS = Long.valueOf(161L);

  public static Long REIMBURSEMENT_PAYMENT_RECONSIDERATION_STATUS = Long.valueOf(159L);

  public static Long PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL = Long.valueOf(121L);

  public static Long PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL = Long.valueOf(122L);

  public static Long INR_CURRENCY_CODE = Long.valueOf(1053L);
  
  public static Long ACK_DOC_NOT_APPLICABLE = Long.valueOf(1564L);
  public static Long ACK_DOC_NOT_RECEIVED = Long.valueOf(1563L);
  public static Long ACK_DOC_RECEIVED_PHOTOCOPY = Long.valueOf(1562L);
  public static Long ACK_DOC_RECEIVED_ORIGINAL = Long.valueOf(1561L);
  public static Long CLAIM_TYPE_REIMBURSEMENT_ID = Long.valueOf(402L);
  
  public static Long PREAUTH_NOT_RECEIVED = Long.valueOf(2001L);
  public static Long POST_WITHDRAW = Long.valueOf(2002L);
  public static Long POST_DENIAL = Long.valueOf(2003L);
  public static Long POST_CASHLESS_QUERY = Long.valueOf(2004L);
  
  //Added for defaulting NOT APPLICABLE IN Document checklist table in Ack screen
  public static Long DOCUMENT_CHECKLIST_NOT_APPLICABLE = Long.valueOf(1564L);
  
  public static Long DOC_RECEIVED_TYPE_HOSPITAL = Long.valueOf(1542l);
  
  
  
  public static Long CLAIM_CLOSED_STATUS = Long.valueOf(164l);
  
  public static Long CLAIM_REOPENED_STATUS = Long.valueOf(173L);
  
  
  
  public static Long KERALA_ZONE_CPU_CODE = Long.valueOf(950004l);
  
  public static Long CLEAR_CASHLESS_STATUS_FOR_PREAUTH = Long.valueOf(166l); 
  
  public static Long CLEAR_CASHLESS_STATUS_FOR_ENHANCEMENT = Long.valueOf(167l);
  
  
  public static final Long ADD_ADDITIONAL_DOCUMENTS_STATUS = Long.valueOf(178l);
  
  public static Map<Long, Long> WITHDRAW_KEYS = getWithdrawnKeys();
  
  public static final Long RECEIVED_PHOTOCOPY = Long.valueOf(1562);
  
  public static final Long RECEIVED_ORIGINAL = Long.valueOf(1561);
  
  public static final Long POLICY_DOCUMENT = Long.valueOf(28l);
  public static final Long PHOTO_ID_CARD = Long.valueOf(29l);
  public static final Long CHEQUE = Long.valueOf(30l);

  public static final String UPDATE_PANCARD_SCREEN = "update pancard screen";
  public static final String UPDATE_AADHAR_SCREEN = "update aadhar screen";  
  
  public static final String UPLOAD_BED_PHOTO = "BED PHOTO";
  
  public static final Long COMBI_LOB_KEY = Long.valueOf(441l);
  public static final Long HEALTH_LOB_KEY = Long.valueOf(442l);
  public static final Long PACKAGE_MASTER_VALUE= Long.valueOf(444l);
  public static final Long PA_LOB_KEY = Long.valueOf(445l);

  public static final String MASTER_TYPE_CODE_BENEFITS = "PABENFITS";
  public static final String MASTER_TYPE_ACCIDENT_OF_CAUSE = "ACCOFCAUSE";
  
  public static final Long HOSP_EXPENSE_NON_WORK_PLACE = Long.valueOf(1237l);
  public static final Long HOSP_EXPENSE_WORK_PLACE = Long.valueOf(1238l);
  public static final Long HOSPITALIZATION_BENEFITS = Long.valueOf(1235l);
  public static final Long PARTIAL_HOSP_BENEFITS = Long.valueOf(1236l);

//  public static final String MASTER_TYPE_CODE_BENEFITS = "PABENFITS"; 
  public static String PA_FRESH_DOCUMENT = "Fresh Document";
  
  public static Long BENEFITS_TTD_MASTER_VALUE = Long.valueOf(1234L);
  public static Long PA_RECONSIDERATION_DOCUMENT = Long.valueOf(1222l);
  public static Long PA_QUERY_REPLY_DOCUMENT = Long.valueOf(1223l);
  public static Long PA_FRESH_DOCUMENT_ID = Long.valueOf(1221l);
  public static Long PA_PAYMENT_QUERY_REPLY = Long.valueOf(1224l);
  //The below status value is subjected to change. For testing adding a dummy value as of now.
  public static Long PA_BILLING_SEND_TO_CLAIM_APPROVER = Long.valueOf(101L);
  public static Long PA_BILLING_SEND_TO_FA = Long.valueOf(3177L);
  
  public static Long DEATH_BENEFIT_MASTER_VALUE = Long.valueOf(1231l);
  public static Long PPD_BENEFIT_MASTER_VALUE = Long.valueOf(1232l);
  public static Long PTD_BENEFIT_MASTER_VALUE = Long.valueOf(1233l);
  public static Long TTD_BENEFIT_MASTER_VALUE = Long.valueOf(1234l);
  public static Long HOSP_BENEFIT_MASTER_VALUE = Long.valueOf(1235l);
  public static Long PART_BENEFIT_MASTER_VALUE = Long.valueOf(1236l);
  
  public static Long PACKAGE_PAC_PRD_003 = Long.valueOf(203l); 
  public static Long FINANCIAL_REFER_TO_CLAIM_APPROVAL = Long.valueOf(184L);
  

  
  // SECTION AND COVER AND SUB COVER Constants
  
//  SEC-01 - HOSPITALIZATON
//  SEC-02 - DELIVERY EXPENSES
//  SEC-03 - DENTAL OPTAHLMIC TREATMENT
//  SEC-04 - HOSPITAL CASH
//  SEC-05 - HEALTH CHECKUP
//  SEC-06 - BARIATRIC 
//  SEC-07 - ACCIDENTAL
//  SEC-08 - LUMPSUM
  
  public static String HOSPITALIZATION_SECTION_CODE = "SEC-01";
  
  public static String ASSISTED_REPRODUCTION_TREATEMENT_SECTION_CODE = "SEC-02";
  
  public static String DELVIERY_AND_NEW_BORN_SECTION_CODE = "SEC-02";
  
  public static String DENTAL_OPTHALMIC_TREATMENT_SECTION_CODE = "SEC-03";
  
  public static String HOSPITAL_CASH_SECTION_CODE = "SEC-04";
  
  public static String HEALTH_CHECKUP_SECTION_CODE = "SEC-05";
  
  public static String BARIATRIC_SECTION_CODE = "SEC-06";
  
  public static String ACCIDENTAL_SECTION_CODE = "SEC-07";
  
  public static String LUMPSUM_SECTION_CODE = "SEC-08";
  
  public static Long CAUSE_OF_INJURY_ACCIDENT_KEY = 1102L;
  
  public static String HOSPITALISATION_SURGICAL_SECTION_CODE = "SEC-01";
  
  public static String HOSP_NON_SURGICAL_SECTION_CODE = "SEC-02";
  
//  SEC-COV-01 - HOSPITALIZATION
//  SEC-COV-02 - MATERNITY
//  SEC-COV-03 - NEW BORN
//  SEC-COV-04 - NEW BORN CHILD VACCINATION
//  SEC-COV-05 - DENTAL OPTHALMIC
//  SEC-COV-06 - HOSPITAL CASH
//  SEC-COV-07 - HEALTH CHECKUP
//  SEC-COV-08 - BARIATRIC
//  SEC-COV-09 - ACCIDENTAL
//  SEC-COV-10 - LUMPSUM
  
  public static String HOSP_COVER_CODE = "SEC-COV-01";
  
  public static String MATERNITY_COVER_CODE = "SEC-COV-02";
  
  public static String NEW_BORN_COVER_CODE = "SEC-COV-03";
  
  public static String NEW_BORN_CHILD_VACCINATION_COVER_CODE = "SEC-COV-04";
  
  public static String DENTAL_OPTHALMIC_COVER_CODE = "SEC-COV-05";
  
  public static String HOSPITAL_CASH_COVER_CODE = "SEC-COV-06";
  
  public static String HEALTH_CHECKUP_COVER_CODE = "SEC-COV-07";
  
  public static String BARIATRIC_COVER_CODE = "SEC-COV-08";
  
  public static String ACCIDENTAL_COVER_CODE = "SEC-COV-09";
  
  public static String LUMPSUM_COVER_CODE = "SEC-COV-10";
  
  public static String ASSISTED_REPRODUCTION_COVER_CODE = "SEC-COV-11";
  
  public static Long ASSISTED_REPRODUCTION_TREATMENT_HOSPITALISATION_KEY = 463L;
  
  public static Long ASSISTED_REPRODUCTION_TREATMENT_COVER_KEY = 1080L;
  
//  SEC-SUB-COV-01 - HOSPITALIZATION
//  SEC-SUB-COV-02 - MATERNITY - NORMAL
//  SEC-SUB-COV-03 - MATERNITY - CEASEAREAN
//  SEC-SUB-COV-04 - NEW BORN
//  SEC-SUB-COV-05 - NEW BORN CHILD VACCINATION
//  SEC-SUB-COV-06 - DENTAL OPTHALMIC 
//  SEC-SUB-COV-07 - HOSPITAL CASH
//  SEC-SUB-COV-08 - HEALTH CHECKUP
//  SEC-SUB-COV-09 - BARIATRIC
//  SEC-SUB-COV-10 - ACCIDENTAL
//  SEC-SUB-COV-11 - LUMPSUM
  
  public static String HOSP_SUB_COVER_CODE = "SEC-SUB-COV-01";
  
  public static String ASSISTED_REPRODUCTION_SUB_COVER_CODE = "SEC-SUB-COV-14";
  
  public static String MATERNITY_NORMAL_SUB_COVER_CODE = "SEC-SUB-COV-02";
  
  public static String MATERNITY_CEASEAREAN_SUB_COVER_CODE = "SEC-SUB-COV-03";
  
  public static String NEW_BORN_SUB_COVER_CODE = "SEC-SUB-COV-04";
  
  public static String NEW_BORN_CHILD_VACCINATION_SUB_COVER_CODE = "SEC-SUB-COV-05";
  
  public static String DENTAL_OPTHALMIC_SUB_COVER_CODE = "SEC-SUB-COV-06";
  
  public static String HOSPITAL_CASH_SUB_COVER_CODE = "SEC-SUB-COV-07";
  
  public static String HEALTH_CHECKUP_SUB_COVER_CODE = "SEC-SUB-COV-08";
  
  public static String BARIATRIC_SUB_COVER_CODE = "SEC-SUB-COV-09";
  
  public static String ACCIDENTAL_SUB_COVER_CODE = "SEC-SUB-COV-10";
  
  public static String LUMPSUM_SUB_COVER_CODE = "SEC-SUB-COV-11";
  
  public static String NEW_BORN_HOSPITALISATION = "SEC-SUB-COV-12";
  
  public static String NEW_BORN_LUMPSUM_SUB_COVER_CODE = "SEC-SUB-COV-13";
  
  public static String MEDIPREMIER_PRODUCT_CODE = "MED-PRD-007";
  public static String STAR_CRITICARE_PRODUCT = "MED-PRD-021";
  
  public static String GMC_PRODUCT_CODE = "MED-PRD-047";
  public static String STAR_GMC_PRODUCT_CODE = "MED-PRD-068";
  public static String STAR_GMC_NBFC_PRODUCT_CODE = "MED-PRD-106";
  
  //star criticare for other banks
  public static String STAR_CRITICARE_OTHER_BANKS = "MED-PRD-073";
  
  public static Long POST_NATAL_CATEGORY = Long.valueOf(71);
  public static Long PRE_NATAL_CATEGORY = Long.valueOf(70);
  
  public static Long MEDIPREMIER_PRODUCT_KEY = Long.valueOf(44l);
  public static Long STAR_CRITICARE_PRODUCT_KEY = Long.valueOf(26l);
  
  public static Long PROCEDURE_STATUS_EXCLUDED_MASTER_ID = Long.valueOf(1142l);
  public static Long EXCLUSION_NOT_APPLICABLE_MASTER_ID = Long.valueOf(2601l);

  //public static Long CLAIM_APPROVAL_STAGE = Long.valueOf(27L);
  public static Long CLAIM_APPROVAL_APPROVE_STATUS = Long.valueOf(103L);
  public static Long CLAIM_APPROVAL_REJECT_STATUS = Long.valueOf(117L);
  public static Long CLAIM_APPROVAL_CANCEL_ROD = Long.valueOf(188L);
  public static Long CLAIM_APPROVAL_NON_HOSP_REJECT_STATUS = Long.valueOf(196L);
  public static Long CLAIM_APPROVAL_QUERY_STATUS = Long.valueOf(191L);
  public static Long CLAIM_APPROVAL_INVESTIGATION_STATUS = Long.valueOf(201L);
  
  public static Long EDUCATIONALGRANT_COVER_KEY = Long.valueOf(101l);
  public static String EDUCATIONALGRANT_COVER_CODE = "ACC-CVR-021";
  
  public static Long TRANSPORTATION_EXPENSE_OF_MORTAL_REMAINS_COVER_KEY = Long.valueOf(102l);
  public static Long TRANSPORTATION_EXPENSE_OF_MORTAL_REMAINS_COVER_KEY_ACC_PRD_015 = Long.valueOf(138l);
  public static String TRANSPORTATION_EXPENSE_OF_MORTAL_REMAINS_COVER_CODE = "ACC-CVR-022";
  //public static Long PA_MASTER_VALUE = Long.valueOf(l)
  
  public static Long CLAIM_APPROVAL_REFER_TO_BILLING = Long.valueOf(186L);
  public static Long CLAIM_APPROVAL_REFER_TO_MEDICAL_APPROVER = Long.valueOf(187L);
  
  public static Long CLAIM_APPROVAL_TO_FINANCIAL_APPROVER = Long.valueOf(190L);
  public static Long CLAIM_APPROVAL_SEND_REPLY_STATUS = Long.valueOf(189L);
  public static Long CLAIM_APPROVAL_SEND_REPLY_FA_STATUS = Long.valueOf(123L);
  
  public static Long ACCIDENT_CARE_SCHOOL_STUDENT_PRODUCT_KEY = Long.valueOf(102l);
  public static Long ACCIDENT_CARE_COLLEGE_STUDENT_PRODUCT_KEY = Long.valueOf(103l);
  
  public static Long ACCIDENT_TRAUMA_CARE_INDIVIDUAL = Long.valueOf(106l);
  public static Long ACCIDENT_TRAUMA_CARE_GROUP = Long.valueOf(107l);
  public static Long ANY_OTHER_PERMANENT_PARTIAL_DISABLEMENT = Long.valueOf(3043l);
  
  public static Long  IN_HOUSE_INVESTIGATOR = Long.valueOf(1121l);
  public static Long PROCESS_CLAIM_APPROVAL_REJECTION_STATUS = Long.valueOf(200l);
  
  
  public static Long OUTPATIENT_EXPENSES = Long.valueOf(191L);
  public static Long EARNING_PARENT_SI = Long.valueOf(215l);
  //public static Long PA_MASTER_VALUE = Long.valueOf(l)
  
  public static Long ENDORSEMENT_APPROVED_BY_PREMIA = Long.valueOf(254L);
  public static Long PED_SUGGESTION_SUG006 = Long.valueOf(2096L);
  

  public static String BEHAVIOUR_OF_HOSP = "BEHAV_HOSP";

  @SuppressWarnings("unchecked")
public static Map<Long, Long> getPackageNumbers() {
    Map values = new HashMap<Long, Long>();
    values.put(Long.valueOf(22L), Long.valueOf(22L));
    values.put(Long.valueOf(20L), Long.valueOf(20L));
    values.put(Long.valueOf(21L), Long.valueOf(21L));
    return values;
  }

  @SuppressWarnings("unchecked")
public static Map<Long, Long> getCancelRODKeys()
  {
    Map values = new HashMap<Long, Long>();
    values.put(Long.valueOf(147L), Long.valueOf(147L));
    values.put(Long.valueOf(148L), Long.valueOf(148L));
    values.put(Long.valueOf(149L), Long.valueOf(149L));
    values.put(Long.valueOf(150L), Long.valueOf(150L));
    values.put(Long.valueOf(188L), Long.valueOf(188L));
    //Claim approval cancel rod keys to be added.
    return values;
  }

  @SuppressWarnings("unchecked")
public static Map<Long, Long> getCloseClaimKeys() {
    Map values = new HashMap<Long, Long>();
    values.put(CREATE_ROD_CLOSED, CREATE_ROD_CLOSED);
    values.put(BILL_ENTRY_CLOSED, BILL_ENTRY_CLOSED);
    values.put(ZONAL_REVIEW_CLOSED, ZONAL_REVIEW_CLOSED);
    values.put(CLAIM_REQUEST_CLOSED, CLAIM_REQUEST_CLOSED);
    values.put(BILLING_CLOSED, BILLING_CLOSED);
    values.put(FINANCIAL_CLOSED, FINANCIAL_CLOSED);
    return values;
  }

  @SuppressWarnings("unchecked")
public static Map<Long, Long> getReopenClaimKeys() {
    Map values = new HashMap<Long, Long>();
    values.put(CREATE_ROD_REOPENED, CREATE_ROD_REOPENED);
    values.put(BILL_ENTRY_REOPENED, BILL_ENTRY_REOPENED);
    values.put(ZONAL_REVIEW_REOPENED, ZONAL_REVIEW_REOPENED);
    values.put(CLAIM_REQUEST_REOPENED, CLAIM_REQUEST_REOPENED);
    values.put(BILLING_REOPENED, BILLING_REOPENED);
    values.put(FINANCIAL_REOPENED, FINANCIAL_REOPENED);
    return values;
  }

  @SuppressWarnings("unchecked")
public static Map<Long, Long> getRejectedPreauthKeys() {
    Map values = new WeakHashMap();
    values.put(Long.valueOf(23L), Long.valueOf(23L));
    values.put(Long.valueOf(26L), Long.valueOf(26L));
    values.put(Long.valueOf(31L), Long.valueOf(31L));
    values.put(Long.valueOf(34L), Long.valueOf(34L));
//    values.put(Long.valueOf(64L), Long.valueOf(64L));
//    values.put(Long.valueOf(40L), Long.valueOf(40L));
    return values;
  }

  public static List<Long> getRejectedPreauthKeysList() {
	  List<Long> preauthKey = new ArrayList<Long>();
	  preauthKey.add(Long.valueOf(23L));
	  preauthKey.add(Long.valueOf(26L));
	  preauthKey.add(Long.valueOf(31L));
	  preauthKey.add(Long.valueOf(34L));
	  return preauthKey;
	  
  }
  
  
  @SuppressWarnings("unchecked")
public static Map<Long, Long> getRejectedRODKeys() {
    Map values = new HashMap<Long, Long>();
    values.put(PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS, PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS);
    values.put(PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS, PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);
    return values;
  }

  @SuppressWarnings("unchecked")
public static Map<Long, Long> getSectionKeys()
  {
    Map values = new HashMap<Long, Long>();
    values.put(DIABETES_FLOATER_POLICY, DIABETES_FLOATER_POLICY);
    values.put(DIABETES_INDIVIDUAL_POLICY, DIABETES_INDIVIDUAL_POLICY);
    values.put(STAR_CARDIAC_CARE_POLICY, STAR_CARDIAC_CARE_POLICY);
    values.put(FHO_PRODUCT_REVISED, FHO_PRODUCT_REVISED);
    values.put(STAR_DIABETIC_SAFE_FLOATER, STAR_DIABETIC_SAFE_FLOATER);
    values.put(STAR_DIABETIC_SAFE_INDIVIDUAL, STAR_DIABETIC_SAFE_INDIVIDUAL);
    values.put(STAR_CARDIAC_CARE, STAR_CARDIAC_CARE);
    values.put(POS_FAMILY_HEALTH_OPTIMA, POS_FAMILY_HEALTH_OPTIMA);
    values.put(STAR_DIABETIC_SAFE_INDIVIDUAL_REVISED, STAR_DIABETIC_SAFE_INDIVIDUAL_REVISED);
    values.put(STAR_DIABETIC_SAFE_FLOATER_REVISED, STAR_DIABETIC_SAFE_FLOATER_REVISED); 
    values.put(STAR_CARDIAC_CARE_NEW, STAR_CARDIAC_CARE_NEW);
  //  values.put(FHO_PRODUCT_REVISED_1, FHO_PRODUCT_REVISED_1);
	// values.put(FHO_PRODUCT_REVISED_PACKAGED, FHO_PRODUCT_REVISED_PACKAGED);

    values.put(FHO_REVISED_PRODUCT_2021_KEY, FHO_REVISED_PRODUCT_2021_KEY);
    values.put(STAR_CARDIAC_CARE_PLATIANUM, STAR_CARDIAC_CARE_PLATIANUM);

    values.put(NEW_FHO_PRODUCT_REVISED_PACKAGED, NEW_FHO_PRODUCT_REVISED_PACKAGED);
    return values;
  }

  @SuppressWarnings("unchecked")
public static Map<Long, Long> getValidationMapForAck()
  {
    Map values = new WeakHashMap();

    values.put(PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS, PRE_MEDICAL_PRE_AUTH_STAGE);

    values.put(PREMEDICAL_PREAUTH_COORDINATOR_REPLY, PRE_MEDICAL_PRE_AUTH_STAGE);
    values.put(PRE_MEDICAL_PRE_AUTH_QUERY_RECEIVED_STATUS, PRE_MEDICAL_PRE_AUTH_STAGE);
    values.put(PRE_MEDICAL_PRE_AUTH_REFER_TO_COORDINATOR_STATUS, PRE_MEDICAL_PRE_AUTH_STAGE);
    values.put(PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS, PRE_MEDICAL_PRE_AUTH_STAGE);

    values.put(PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS, PRE_MEDICAL_ENHANCEMENT_STAGE);

    values.put(PRE_MEDICAL_ENHANCEMENT_QUERY_RECEIVED_STATUS, PRE_MEDICAL_ENHANCEMENT_STAGE);
    values.put(PRE_MEDICAL_ENHANCEMENT_REFER_TO_COORDINATOR_STATUS, PRE_MEDICAL_ENHANCEMENT_STAGE);
    values.put(PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS, PRE_MEDICAL_ENHANCEMENT_STAGE);
    values.put(PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY, PRE_MEDICAL_ENHANCEMENT_STAGE);

    values.put(PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS, PREAUTH_STAGE);
    values.put(PREAUTH_QUERY_RECEIVED_STATUS, PREAUTH_STAGE);
    values.put(PREAUTH_REFER_TO_COORDINATOR_STATUS, PREAUTH_STAGE);
    values.put(PREAUTH_ESCALATION_STATUS, PREAUTH_STAGE);

    values.put(ENHANCEMENT_REFER_TO_COORDINATOR_STATUS, ENHANCEMENT_STAGE);
    values.put(ENHANCEMENT_COORDINATOR_REPLY_RECEIVED_STATUS, ENHANCEMENT_STAGE);
    values.put(ENHANCEMENT_ESCALATION_STATUS, ENHANCEMENT_STAGE);

    values.put(ENHANCEMENT_QUERY_RECEIVED_STATUS, ENHANCEMENT_STAGE);

    values.put(DOWNSIZE_ESCALATION_STATUS, DOWNSIZE_STAGE);
    values.put(DOWNSIZE_SUBMIT_SPECIALIST_REPLY, DOWNSIZE_STAGE);

    values.put(STANDALONE_WITHDRAW_STATUS, STANDALONE_WITHDRAW_STAGE);
    values.put(ENHANCEMENT_PREAUTH_WITHDRAW_STATUS, ENHANCEMENT_STAGE);
    
    values.put(STANDALONE_WITHDRAW_AND_REJECT_STATUS, STANDALONE_WITHDRAW_STAGE);
	 values.put(ENHANCEMENT_WITHDRAW_AND_REJECT, ENHANCEMENT_STAGE);
	// values.put(PREAUTH_REJECT_STATUS,PREAUTH_STAGE);

    return values;
  }
  
  @SuppressWarnings("unchecked")
  public static Map<Long, Long> getValidationMapForPAAck()
    {
      Map values = new WeakHashMap();

      values.put(PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS, PRE_MEDICAL_PRE_AUTH_STAGE);

      values.put(PREMEDICAL_PREAUTH_COORDINATOR_REPLY, PRE_MEDICAL_PRE_AUTH_STAGE);
      values.put(PRE_MEDICAL_PRE_AUTH_QUERY_RECEIVED_STATUS, PRE_MEDICAL_PRE_AUTH_STAGE);
      values.put(PRE_MEDICAL_PRE_AUTH_QUERY_STATUS, PRE_MEDICAL_PRE_AUTH_STAGE);
      values.put(REFER_TO_FLP, PREAUTH_STAGE);
      values.put(PREAUTH_QUERY_STATUS, PREAUTH_STAGE);
      values.put(PREMEDICAL_WAIVED_REJECTION, PRE_MEDICAL_PRE_AUTH_STAGE);
      values.put(SPECIALIST_REPLY_RECEIVED, PREAUTH_STAGE);
      values.put(ENHANCEMENT_QUERY_STATUS, ENHANCEMENT_STAGE);
      values.put(PRE_MEDICAL_PRE_AUTH_REFER_TO_COORDINATOR_STATUS, PRE_MEDICAL_PRE_AUTH_STAGE);
      values.put(PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS, PRE_MEDICAL_PRE_AUTH_STAGE);

      values.put(PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS, PRE_MEDICAL_ENHANCEMENT_STAGE);

      values.put(PRE_MEDICAL_ENHANCEMENT_QUERY_RECEIVED_STATUS, PRE_MEDICAL_ENHANCEMENT_STAGE);
      values.put(PRE_MEDICAL_ENHANCEMENT_REFER_TO_COORDINATOR_STATUS, PRE_MEDICAL_ENHANCEMENT_STAGE);
      values.put(PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS, PRE_MEDICAL_ENHANCEMENT_STAGE);
      values.put(PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY, PRE_MEDICAL_ENHANCEMENT_STAGE);

      values.put(PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS, PREAUTH_STAGE);
      values.put(PREAUTH_QUERY_RECEIVED_STATUS, PREAUTH_STAGE);
      values.put(PREAUTH_REFER_TO_COORDINATOR_STATUS, PREAUTH_STAGE);
      values.put(PREAUTH_ESCALATION_STATUS, PREAUTH_STAGE);

      values.put(ENHANCEMENT_REFER_TO_COORDINATOR_STATUS, ENHANCEMENT_STAGE);
      values.put(ENHANCEMENT_COORDINATOR_REPLY_RECEIVED_STATUS, ENHANCEMENT_STAGE);
      values.put(ENHANCEMENT_ESCALATION_STATUS, ENHANCEMENT_STAGE);

      values.put(ENHANCEMENT_QUERY_RECEIVED_STATUS, ENHANCEMENT_STAGE);

      values.put(DOWNSIZE_ESCALATION_STATUS, DOWNSIZE_STAGE);
      values.put(DOWNSIZE_SUBMIT_SPECIALIST_REPLY, DOWNSIZE_STAGE);

      values.put(STANDALONE_WITHDRAW_STATUS, STANDALONE_WITHDRAW_STAGE);
      values.put(ENHANCEMENT_PREAUTH_WITHDRAW_STATUS, ENHANCEMENT_STAGE);
      
      values.put(STANDALONE_WITHDRAW_AND_REJECT_STATUS, STANDALONE_WITHDRAW_STAGE);
  	 values.put(ENHANCEMENT_WITHDRAW_AND_REJECT, ENHANCEMENT_STAGE);
  	// values.put(PREAUTH_REJECT_STATUS,PREAUTH_STAGE);

      return values;
    }

  @SuppressWarnings("unchecked")
public static Map<Long, Long> getConversionStatusMap()
  {
    Map values1 = new WeakHashMap();
    values1.put(PREAUTH_DENIAL_OF_CASHLESS_STATUS, PREAUTH_STAGE);
    values1.put(PREAUTH_REJECT_STATUS, PREAUTH_STAGE);
    values1.put(ENHANCEMENT_WITHDRAW_AND_REJECT, ENHANCEMENT_STAGE);
    values1.put(STANDALONE_WITHDRAW_AND_REJECT_STATUS, STANDALONE_WITHDRAW_STAGE);

//    values1.put(PRE_MEDICAL_PRE_AUTH_QUERY_STATUS, PRE_MEDICAL_PRE_AUTH_STAGE);
//    values1.put(PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS, PRE_MEDICAL_ENHANCEMENT_STAGE);
//    values1.put(PREAUTH_QUERY_STATUS, PREAUTH_STAGE);
//    values1.put(ENHANCEMENT_QUERY_STATUS, ENHANCEMENT_STAGE);
    values1.put(PROCESS_REJECTED, PROCESS_REJECTION_STAGE);
    values1.put(PREMEDICAL_WAIVED_REJECTION, PROCESS_REJECTION_STAGE);

    return values1;
  }

  @SuppressWarnings("unchecked")
public static Map<Long, Long> getPreauthForWithdrawAndDownsize()
  {
    Map values = new WeakHashMap();

    values.put(PREAUTH_APPROVE_STATUS, PREAUTH_APPROVE_STATUS);
    values.put(ENHANCEMENT_APPROVE_STATUS, ENHANCEMENT_APPROVE_STATUS);
    values.put(ENHANCEMENT_DOWNSIZE_STATUS, ENHANCEMENT_DOWNSIZE_STATUS);
    values.put(ENHANCEMENT_REJECT_STATUS, ENHANCEMENT_REJECT_STATUS);
    values.put(ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS, ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS);

    return values;
  }
  
  public static Boolean isNonAllopathicApplicableProduct(Long productKey){
	  
	  List<Long> productList = new ArrayList<Long>();
	  productList.add(MICRO_INSURANCE_GROUP);
	  productList.add(SENIOR_CITIZEN_POLICY);
	  
	  if(productList.contains(productKey)){
		  return true;
	  }
	  return false;
	  
  }
  
  public static Map<Long, Long> getPaymentStatus() {
    Map<Long, Long> values = new WeakHashMap();
//    Settled
//    STAGE_ID=27,
//    STATUS_ID=161
//    Cancelled
//    STAGE_ID=27,
//     WHEN 'AMOUNT CHANGE' THEN 159
//     WHEN 'NAME CHANGE'   THEN 103
//    WHEN 'DD EXPIRED'    THEN 103
    
    values.put(161l, 161l);
    values.put(159l, 159l);
    values.put(103l, 103l);

    return values;
  }
  
  public static Map<Long, Long> getPaymentStatusExceptCancel() {
	  Map<Long, Long> values = new WeakHashMap();
	  values.put(154l, 154l);
	  values.put(155l, 155l);
	  values.put(156l, 156l);
	  values.put(157l, 157l);
	  values.put(158l, 158l);

	  return values;
	  
  }
  
  public static Map<String, String> getEnabledScreens() {
	  Map<String, String> values = new WeakHashMap();
	  values.put(SHAConstants.PRE_MEDICAL_PRE_AUTH, SHAConstants.PRE_MEDICAL_PRE_AUTH);
//	  values.put(SHAConstants.PRE_AUTH, SHAConstants.PRE_AUTH);
	  values.put(SHAConstants.PRE_MEDICAL_ENHANCEMENT, SHAConstants.PRE_MEDICAL_ENHANCEMENT);
//	  values.put(SHAConstants.PRE_AUTH_ENHANCEMENT, SHAConstants.PRE_AUTH_ENHANCEMENT );

	  return values;
	  
  }
  
  
  public static Map<Long, Long> isEditable() {
	  Map<Long, Long> values = new WeakHashMap();
	  values.put(ReferenceTable.COMPREHENSIVE_27_PRODUCT,ReferenceTable.COMPREHENSIVE_27_PRODUCT );
	  values.put(ReferenceTable.COMPREHENSIVE_37_PRODUCT, ReferenceTable.COMPREHENSIVE_37_PRODUCT);
	  values.put(ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER, ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER);
	  values.put(ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL, ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL);
	  values.put(ReferenceTable.GMC_PRODUCT_KEY, GMC_PRODUCT_KEY);
	  values.put(STAR_GMC_PRODUCT_KEY, STAR_GMC_PRODUCT_KEY);
	  values.put(STAR_GMC_NBFC_PRODUCT_KEY, STAR_GMC_NBFC_PRODUCT_KEY);
	  values.put(STAR_CRITICARE_OTHER_BANKS_KEY,STAR_CRITICARE_OTHER_BANKS_KEY);
	  values.put(ReferenceTable.STAR_FIRST_COMPREHENSVE, ReferenceTable.STAR_FIRST_COMPREHENSVE);
	  values.put(ReferenceTable.JET_PRIVILEGE_PRODUCT, ReferenceTable.JET_PRIVILEGE_PRODUCT);
	  values.put(ReferenceTable.STAR_DIABETIC_SAFE_INDIVIDUAL, ReferenceTable.STAR_DIABETIC_SAFE_INDIVIDUAL);
	  values.put(ReferenceTable.STAR_DIABETIC_SAFE_FLOATER, ReferenceTable.STAR_DIABETIC_SAFE_FLOATER);
	  values.put(ReferenceTable.STAR_CARDIAC_CARE,ReferenceTable.STAR_CARDIAC_CARE);
	  values.put(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY, ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY);
	  values.put(ReferenceTable.STAR_DIABETIC_SAFE_INDIVIDUAL_REVISED, ReferenceTable.STAR_DIABETIC_SAFE_INDIVIDUAL_REVISED);
	  values.put(ReferenceTable.STAR_DIABETIC_SAFE_FLOATER_REVISED, ReferenceTable.STAR_DIABETIC_SAFE_FLOATER_REVISED);
	  values.put(ReferenceTable.STAR_CARDIAC_CARE_NEW, ReferenceTable.STAR_CARDIAC_CARE_NEW);
	  values.put(ReferenceTable.STAR_COMPREHENSIVE_IND_PRODUCT_KEY, ReferenceTable.STAR_COMPREHENSIVE_IND_PRODUCT_KEY);
	  values.put(ReferenceTable.COMPREHENSIVE_88_PRODUCT_FLOATER, ReferenceTable.COMPREHENSIVE_88_PRODUCT_FLOATER);
	  values.put(ReferenceTable.COMPREHENSIVE_88_PRODUCT_INDIVIDUAL, ReferenceTable.COMPREHENSIVE_88_PRODUCT_INDIVIDUAL);
	  values.put(ReferenceTable.STAR_CARDIAC_CARE_PLATIANUM, ReferenceTable.STAR_CARDIAC_CARE_PLATIANUM);

	  values.put(ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND, ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND);
	  return values;
	  
  }
  
  public static Map<Long, Long> getWithdrawnKeys() {
	  Map<Long, Long> values = new WeakHashMap();
	  values.put(ENHANCEMENT_PREAUTH_WITHDRAW_STATUS, ENHANCEMENT_PREAUTH_WITHDRAW_STATUS);
	  values.put(STANDALONE_WITHDRAW_STATUS, STANDALONE_WITHDRAW_STATUS);
	  values.put(STANDALONE_WITHDRAW_AND_REJECT_STATUS, STANDALONE_WITHDRAW_AND_REJECT_STATUS);
	  values.put(ENHANCEMENT_WITHDRAW_AND_REJECT, ENHANCEMENT_WITHDRAW_AND_REJECT);

	  return values;
	  
  }
  
  public static Map<Long, Long> getWithdrawnKeysExceptWithdrawReject() {
	  @SuppressWarnings("rawtypes")
	Map<Long, Long> values = new WeakHashMap();
	  values.put(ENHANCEMENT_PREAUTH_WITHDRAW_STATUS, ENHANCEMENT_PREAUTH_WITHDRAW_STATUS);
	  values.put(STANDALONE_WITHDRAW_STATUS, STANDALONE_WITHDRAW_STATUS);
	  return values;
	  
  }
  
  public static Map<Long, Long> getFinancialApprovalStatus() {
	  @SuppressWarnings("rawtypes")
	Map<Long, Long> values = new WeakHashMap();
	  values.put(154l, 154l);
	  values.put(155l, 155l);
	  values.put(156l, 156l);
	  values.put(157l, 157l);
	  values.put(158l, 158l);
	  values.put(FINANCIAL_APPROVE_STATUS, FINANCIAL_APPROVE_STATUS);
	  values.put(PAYMENT_CANCELLED, PAYMENT_CANCELLED);
	  values.put(REIMBURSEMENT_SETTLED_STATUS, REIMBURSEMENT_SETTLED_STATUS);
	  return values;
	  
  }
  
  public static Map<Long, Long> getFinalBillKeys() {
	  @SuppressWarnings("rawtypes")
	Map<Long, Long> values = new WeakHashMap();
	  values.put(4049l, 4049l);
	  values.put(4002l, 4002l);

	  return values;
	  
  }
  
  @SuppressWarnings("rawtypes")
public static Map<Long, Long> getReferToKeys() {
	  Map<Long, Long> values = new WeakHashMap();
	  values.put(BILLING_REFER_TO_MEDICALA_APPROVER, BILLING_REFER_TO_MEDICALA_APPROVER);
	  values.put(FINANCIAL_REFER_TO_MEDICAL_APPROVER, FINANCIAL_REFER_TO_MEDICAL_APPROVER);
	  values.put(FINANCIAL_REFER_TO_BILLING, FINANCIAL_REFER_TO_BILLING);
	  values.put(FINANCIAL_REFER_TO_COORDINATOR_STATUS, FINANCIAL_REFER_TO_COORDINATOR_STATUS);
	  values.put(BILLING_REFER_TO_COORDINATOR, BILLING_REFER_TO_COORDINATOR);
	  values.put(BILLING_REFER_TO_BILL_ENTRY, BILLING_REFER_TO_BILL_ENTRY);
	  values.put(FINANCIAL_REFER_TO_BILL_ENTRY, FINANCIAL_REFER_TO_BILL_ENTRY);
	  return values;
	  
  }
  
  @SuppressWarnings("rawtypes")
  public static Map<Long, Long> getReplyReceivedStatus() {
	  Map<Long, Long> values = new WeakHashMap();
	  values.put(CLAIM_REQUEST_SPECIALIST_REPLY_RECEIVED_STATUS, CLAIM_REQUEST_SPECIALIST_REPLY_RECEIVED_STATUS);
	  values.put(CLAIM_REQUEST_COORDINATOR_REPLY_RECEIVED_STATUS, CLAIM_REQUEST_COORDINATOR_REPLY_RECEIVED_STATUS);
	  values.put(CLAIM_REQUEST_QUERY_RECEIVED_STATUS, CLAIM_REQUEST_QUERY_RECEIVED_STATUS);
	  
	  return values;
	  
  }
  
  
  public static Map<Long, Long> getSeniorCitizenKeys() {
	Map<Long, Long> values = new WeakHashMap();
	  values.put(SENIOR_CITIZEN_RED_CARPET_PRODUCT, SENIOR_CITIZEN_RED_CARPET_PRODUCT);
	  values.put(SENIOR_CITIZEN_RED_CARPET, SENIOR_CITIZEN_RED_CARPET);
	  values.put(SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL,SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL);
	  values.put(SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER, SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER);
	  
	  return values;
	  
  }
  
  public static Map<Long, Long> getSuperSurplusKeys() {
		  Map<Long, Long> values = new WeakHashMap();
		  values.put(SUPER_SURPLUS_INDIVIDUAL, SUPER_SURPLUS_INDIVIDUAL);
		  values.put(SUPER_SURPLUS_FLOATER, SUPER_SURPLUS_FLOATER);
		  values.put(SUPER_SURPLUS_REVISED_FLOATER, SUPER_SURPLUS_REVISED_FLOATER);
		  values.put(SUPER_SURPLUS_REVISED_INDIVIDUAL, SUPER_SURPLUS_REVISED_INDIVIDUAL);
		  values.put(SUPER_SURPLUS_INDIVIDUAL_2018, SUPER_SURPLUS_INDIVIDUAL_2018);
		  values.put(SUPER_SURPLUS_FLOATER_2018, SUPER_SURPLUS_FLOATER_2018);
		  values.put(SUPER_SURPLUS_REVISED_INDIVIDUAL_2020, SUPER_SURPLUS_REVISED_INDIVIDUAL_2020);
		  values.put(SUPER_SURPLUS_REVISED_FLOATER_2020, SUPER_SURPLUS_REVISED_FLOATER_2020);
		  //added for group top-up product
		  values.put(SUPER_SURPLUS_INDIVIDUAL_2018, SUPER_SURPLUS_INDIVIDUAL_2018);
		  values.put(SUPER_SURPLUS_FLOATER_2018, SUPER_SURPLUS_FLOATER_2018);
		  
		  return values;
		  
	}
  
 public static Map<Long, Long> getRevisedSuperSurplusKeys() {
	  Map<Long, Long> values = new WeakHashMap();
	  values.put(SUPER_SURPLUS_REVISED_FLOATER, SUPER_SURPLUS_REVISED_FLOATER);
	  values.put(SUPER_SURPLUS_REVISED_INDIVIDUAL, SUPER_SURPLUS_REVISED_INDIVIDUAL);
	  values.put(SUPER_SURPLUS_INDIVIDUAL_2018, SUPER_SURPLUS_INDIVIDUAL_2018);
	  values.put(SUPER_SURPLUS_FLOATER_2018, SUPER_SURPLUS_FLOATER_2018);
	  values.put(SUPER_SURPLUS_REVISED_INDIVIDUAL_2020, SUPER_SURPLUS_REVISED_INDIVIDUAL_2020);
	  values.put(SUPER_SURPLUS_REVISED_FLOATER_2020, SUPER_SURPLUS_REVISED_FLOATER_2020);
	  return values;
	  
 }
  
  
 
  
  /*public static Map<Long, String> getDefaultCoPayValues() {

	  Map<Long, String> values = new HashMap<Long, String>();
	  values.put(14L, "20");
	  values.put(15L, "20");
	  values.put(40L, "20");
	  values.put(16L, "10");
	  values.put(17L, "10");
	  values.put(18L, "10");
	  values.put(38L, "10");
	  values.put(39L, "10");
	  values.put(51L, "10");
	  values.put(23L, "10");
	  values.put(24L, "10");
	  values.put(70L, "10");
	  values.put(74L, "10");
	  values.put(82L, "10");
	  values.put(83L, "10");
	  
	  return values;
	  
  }*/
  
  public static Map<Long, Long> getReferToFLPKeys() {
	  Map<Long, Long> values = new HashMap<Long, Long>();
	  values.put(PREAUTH_REFER_TO_FLP_STATUS, PREAUTH_REFER_TO_FLP_STATUS);
	  values.put(ENHANCEMENT_REFER_TO_FLP_STATUS, ENHANCEMENT_REFER_TO_FLP_STATUS);
	  
	  return values;
	  
  }
  
  public static Map<Long, Long> getPremiumDeductionProductKeys() {
	  HashMap<Long, Long> values = new HashMap<Long, Long>();
	  values.put(STAR_UNIQUE_PRODUCT_KEY, STAR_UNIQUE_PRODUCT_KEY);
	  return values;
  }
  
  public static Map<Long, Long> getComprehensiveProducts() {
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(COMPREHENSIVE_37_PRODUCT, COMPREHENSIVE_37_PRODUCT);
	  values.put(COMPREHENSIVE_78_PRODUCT_FLOATER, COMPREHENSIVE_78_PRODUCT_FLOATER);
	  values.put(COMPREHENSIVE_78_PRODUCT_INDIVIDUAL, COMPREHENSIVE_78_PRODUCT_INDIVIDUAL);
	  values.put(COMPREHENSIVE_27_PRODUCT, COMPREHENSIVE_27_PRODUCT);
	  values.put(STAR_FIRST_COMPREHENSVE,STAR_FIRST_COMPREHENSVE);
	  values.put(ReferenceTable.JET_PRIVILEGE_PRODUCT, ReferenceTable.JET_PRIVILEGE_PRODUCT);
	  values.put(ReferenceTable.STAR_COMPREHENSIVE_IND_PRODUCT_KEY, ReferenceTable.STAR_COMPREHENSIVE_IND_PRODUCT_KEY);
	  values.put(COMPREHENSIVE_88_PRODUCT_FLOATER, COMPREHENSIVE_88_PRODUCT_FLOATER);
	  values.put(COMPREHENSIVE_88_PRODUCT_INDIVIDUAL, COMPREHENSIVE_88_PRODUCT_INDIVIDUAL);
	  return values;
	  
  }
  
  public static Map<String, String> getCashlessNonEditableProducts() {
	  Map<String, String> values = new WeakHashMap<String, String>();
	  values.put(MEDIPREMIER_PRODUCT_CODE, MEDIPREMIER_PRODUCT_CODE);
	  values.put(STAR_CRITICARE_PRODUCT, STAR_CRITICARE_PRODUCT);
	  
	  return values;
	  
  }
  
  public static Map<Long, Long> getReimbursmentEditableProducts() {
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(FHO_PRODUCT_REVISED, FHO_PRODUCT_REVISED);
	  values.put(FHO_PRODUCT_REVISED_1, FHO_PRODUCT_REVISED_1);
	  values.put(FHO_PRODUCT_REVISED_PACKAGED, FHO_PRODUCT_REVISED_PACKAGED);
	  values.put(POS_FAMILY_HEALTH_OPTIMA, POS_FAMILY_HEALTH_OPTIMA);	  
	 // values.put(FHO_PRODUCT_REVISED_TEMP,FHO_PRODUCT_REVISED_TEMP);
	  values.put(FHO_REVISED_PRODUCT_2021_KEY, FHO_REVISED_PRODUCT_2021_KEY);

	  values.put(NEW_FHO_PRODUCT_REVISED_PACKAGED, NEW_FHO_PRODUCT_REVISED_PACKAGED);
	  return values;
	  
  }
  
  public static Map<String, String> getComprehensiveProductSectionAlertMessage() {
	  Map<String, String> values = new WeakHashMap<String, String>();
	  values.put(DENTAL_OPTHALMIC_TREATMENT_SECTION_CODE, "Dental and Ophthalmic Treatment should be processed as Out Patient");
	  values.put(HOSPITAL_CASH_SECTION_CODE, "Hospital Cash should be processed under Bill classification- Add on Benefit (Hospital Cash)");
	  values.put(HEALTH_CHECKUP_SECTION_CODE, "Health Check Up should be processed as Out Patient");
	  values.put(ACCIDENTAL_SECTION_CODE, "Accidental Death and Permanent Total Disablement should be processed in PA Screens");
	  return values;
	  
  }
  
  public static Map<Long, Long> getNoLimitForMaxPayableProducts() {
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(FHO_NEW_PRODUCT, FHO_NEW_PRODUCT);
	  values.put(FHO_PRODUCT_REVISED, FHO_PRODUCT_REVISED);
	  values.put(COMPREHENSIVE_37_PRODUCT, COMPREHENSIVE_37_PRODUCT);
	  values.put(COMPREHENSIVE_78_PRODUCT_FLOATER, COMPREHENSIVE_78_PRODUCT_FLOATER);
	  values.put(COMPREHENSIVE_78_PRODUCT_INDIVIDUAL, COMPREHENSIVE_78_PRODUCT_INDIVIDUAL);
	  values.put(COMPREHENSIVE_27_PRODUCT, COMPREHENSIVE_27_PRODUCT);	
	  values.put(STAR_FIRST_OPTIMA, STAR_FIRST_OPTIMA);	
	  values.put(STAR_FIRST_COMPREHENSVE, STAR_FIRST_COMPREHENSVE);
	  values.put(GMC_PRODUCT_KEY, GMC_PRODUCT_KEY);
	  values.put(STAR_GMC_PRODUCT_KEY, STAR_GMC_PRODUCT_KEY);
	  values.put(STAR_GMC_NBFC_PRODUCT_KEY, STAR_GMC_NBFC_PRODUCT_KEY);
	  values.put(STAR_CRITICARE_OTHER_BANKS_KEY,STAR_CRITICARE_OTHER_BANKS_KEY);
	  values.put(ReferenceTable.JET_PRIVILEGE_PRODUCT, ReferenceTable.JET_PRIVILEGE_PRODUCT);
	  values.put(FHO_PRODUCT_REVISED, FHO_PRODUCT_REVISED);
	  values.put(FHO_PRODUCT_REVISED_1, FHO_PRODUCT_REVISED_1);
	  values.put(FHO_PRODUCT_REVISED_PACKAGED, FHO_PRODUCT_REVISED_PACKAGED);
	  values.put(SUPER_SURPLUS_REVISED_INDIVIDUAL,SUPER_SURPLUS_REVISED_INDIVIDUAL);
	  values.put(SUPER_SURPLUS_REVISED_FLOATER,SUPER_SURPLUS_REVISED_INDIVIDUAL);
	  values.put(STAR_CANCER_GOLD_PRODUCT_KEY, STAR_CANCER_GOLD_PRODUCT_KEY);
	  values.put(POS_FAMILY_HEALTH_OPTIMA, POS_FAMILY_HEALTH_OPTIMA);
	  values.put(STAR_HEALTH_GAIN_FLOATER_PRODUCT, STAR_HEALTH_GAIN_FLOATER_PRODUCT);
	  values.put(STAR_HEALTH_GAIN_INDIVIDUAL_PRODUCT, STAR_HEALTH_GAIN_INDIVIDUAL_PRODUCT);
	  values.put(SUPER_SURPLUS_INDIVIDUAL_2018, SUPER_SURPLUS_INDIVIDUAL_2018);
	  values.put(SUPER_SURPLUS_FLOATER_2018, SUPER_SURPLUS_FLOATER_2018);
	  values.put(STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY, STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY);
	  values.put(STAR_COMPREHENSIVE_IND_PRODUCT_KEY, STAR_COMPREHENSIVE_IND_PRODUCT_KEY);
	  values.put(STAR_YOUNG_IND_PRODUCT_KEY, STAR_YOUNG_IND_PRODUCT_KEY);
	  values.put(STAR_YOUNG_FLT_PRODUCT_KEY, STAR_YOUNG_FLT_PRODUCT_KEY);
	  values.put(STAR_AROGYA_SANJEEVANI_PRODUCT_INDIVIDUAL_KEY, STAR_AROGYA_SANJEEVANI_PRODUCT_INDIVIDUAL_KEY);
	  values.put(STAR_AROGYA_SANJEEVANI_PRODUCT_FLOATER_KEY, STAR_AROGYA_SANJEEVANI_PRODUCT_FLOATER_KEY);
	  values.put(STAR_CORONA_GRP_PRODUCT_KEY, STAR_CORONA_GRP_PRODUCT_KEY);
	  values.put(STAR_GRP_COVID_PROD_KEY_INDI, STAR_GRP_COVID_PROD_KEY_INDI);
	  values.put(STAR_CORONA_KAVACH_PRODUCT_KEY_IND, STAR_CORONA_KAVACH_PRODUCT_KEY_IND);
	  values.put(STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY, STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY);
	  values.put(STAR_YOUNG_IND_PRODUCT_KEY_091, STAR_YOUNG_IND_PRODUCT_KEY_091);
	  values.put(STAR_YOUNG_FLT_PRODUCT_KEY_091, STAR_YOUNG_FLT_PRODUCT_KEY_091);
	  values.put(STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY, STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY); 
	  values.put(COMPREHENSIVE_88_PRODUCT_FLOATER, COMPREHENSIVE_88_PRODUCT_FLOATER);
	  values.put(COMPREHENSIVE_88_PRODUCT_INDIVIDUAL, COMPREHENSIVE_88_PRODUCT_INDIVIDUAL);
	  values.put(SUPER_SURPLUS_REVISED_INDIVIDUAL_2020, SUPER_SURPLUS_REVISED_INDIVIDUAL_2020);
	  values.put(SUPER_SURPLUS_REVISED_FLOATER_2020, SUPER_SURPLUS_REVISED_FLOATER_2020);
	  values.put(GROUP_TOPUP_PROD_KEY, GROUP_TOPUP_PROD_KEY);
	  values.put(FHO_REVISED_PRODUCT_2021_KEY, FHO_REVISED_PRODUCT_2021_KEY);

	  values.put(STAR_CARDIAC_CARE_PLATIANUM, STAR_CARDIAC_CARE_PLATIANUM);

	  values.put(NEW_FHO_PRODUCT_REVISED_PACKAGED, NEW_FHO_PRODUCT_REVISED_PACKAGED);
	  return values;
	  
  }
  
  public static Map<Long, Long> getFHOProductList() {
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(FHO_NEW_PRODUCT, FHO_NEW_PRODUCT);
	  values.put(FHO_PRODUCT_REVISED, FHO_PRODUCT_REVISED);
	  values.put(FHO_PRODUCT_REVISED_1, FHO_PRODUCT_REVISED_1);
	  values.put(FHO_PRODUCT_REVISED_PACKAGED, FHO_PRODUCT_REVISED_PACKAGED);
	  values.put(POS_FAMILY_HEALTH_OPTIMA, POS_FAMILY_HEALTH_OPTIMA);
	  values.put(FHO_REVISED_PRODUCT_2021_KEY, FHO_REVISED_PRODUCT_2021_KEY);

	  values.put(NEW_FHO_PRODUCT_REVISED_PACKAGED, NEW_FHO_PRODUCT_REVISED_PACKAGED);
	  return values;
  }

  public static Map<Long,Long> getPrePostNatalMap()
  {
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(PRE_NATAL_CATEGORY, PRE_NATAL_CATEGORY);
	  values.put(POST_NATAL_CATEGORY, POST_NATAL_CATEGORY);
	  
	  return values;
  }
  
  public static Map<String,String> getMaternityMap()
  {
	  Map<String, String> values = new WeakHashMap<String, String>();
	  values.put(MATERNITY_CEASEAREAN_SUB_COVER_CODE, MATERNITY_CEASEAREAN_SUB_COVER_CODE);
	  values.put(MATERNITY_NORMAL_SUB_COVER_CODE, MATERNITY_NORMAL_SUB_COVER_CODE);
	  return values;
  }
  
  
  public static Long CATEGORY_ANH_PACKAGES = Long.valueOf(34);
  public static Long CATEGORY_PACKAGES = Long.valueOf(34L);
  public static Long CATEGORY_PACKAGES_25PERCENT = Long.valueOf(35L);
  public static Long CATEGORY_PACKAGES_50PERCENT = Long.valueOf(36L);
  public static Long CATEGORY_PACKAGES_80PERCENT = Long.valueOf(37L);
  public static Long CATEGORY_PACKAGES_OTHER_PACKAGES = Long.valueOf(69L);
  public static Long CATEGORY_PROCEDURE_CHARGES = Long.valueOf(38L);
  public static Long CATEGORY_PROCEDURE_CHARGES_25PERCENT = Long.valueOf(39L);
  public static Long CATEGORY_PROCEDURE_CHARGES_50PERCENT = Long.valueOf(40L);
  public static Long CATEGORY_INVESTIGATION_WITHIN_HOSPITAL = Long.valueOf(26L);
  public static Long CATEGORY_MISCELLANEOUS_WITHIN_HOSPITAL = Long.valueOf(28L);
  public static Long ACCIDENT_CARE_INDIVIDUAL_PRODUCT_KEY = Long.valueOf(101l);
  public static Long SCHOOL_STUDENT_CARE_PRODUCT_KEY = Long.valueOf(102l);
  public static Long COLLEGE_STUDENT_CARE_PRODUCT_KEY = Long.valueOf(103l);
  public static Long ACCIDENT_CARE_GROUP_PRODUCT_KEY = Long.valueOf(104l);
  public static Long ACCIDENT_TRAUMA_CARE_INDIVIDUAL_PRODUCT_KEY = Long.valueOf(106l);
  public static Long ACCIDENT_TRAUMA_CARE_GROUP_PRODUCT_KEY = Long.valueOf(107l);
  public static Long ACCIDENT_CARE_INDIVIDUAL_REVISED_PRODUCT_KEY = Long.valueOf(114l);
  public static Long FAMILY_HEALTH_OPTIMA_PRODUCT_KEY = Long.valueOf(201l);
  
  public static Long CATEGORY_HOSPITAL_DISCOUNT = Long.valueOf(23L);
  

  public static Map<Long,Long> getDomicillaryMap()
  {
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(ROOM_RENT_NURSING_CHARGES, ROOM_RENT_NURSING_CHARGES);
	  values.put(ICU_CHARGES, ICU_CHARGES);
	  values.put(CATEGORY_PACKAGES, CATEGORY_PACKAGES);
	  values.put(CATEGORY_PACKAGES_25PERCENT, CATEGORY_PACKAGES_25PERCENT);
	  values.put(CATEGORY_PACKAGES_50PERCENT, CATEGORY_PACKAGES_50PERCENT);
	  values.put(CATEGORY_PACKAGES_80PERCENT, CATEGORY_PACKAGES_80PERCENT);
	  values.put(CATEGORY_PACKAGES_OTHER_PACKAGES, CATEGORY_PACKAGES_OTHER_PACKAGES);
	  values.put(CATEGORY_PROCEDURE_CHARGES, CATEGORY_PROCEDURE_CHARGES);
	  values.put(CATEGORY_PROCEDURE_CHARGES_25PERCENT, CATEGORY_PROCEDURE_CHARGES_25PERCENT);
	  values.put(CATEGORY_PROCEDURE_CHARGES_50PERCENT, CATEGORY_PROCEDURE_CHARGES_50PERCENT);
	  values.put(CATEGORY_INVESTIGATION_WITHIN_HOSPITAL, CATEGORY_INVESTIGATION_WITHIN_HOSPITAL);
	  values.put(CATEGORY_MISCELLANEOUS_WITHIN_HOSPITAL, CATEGORY_MISCELLANEOUS_WITHIN_HOSPITAL);
	  values.put(CATEGORY_HOSPITAL_DISCOUNT, CATEGORY_HOSPITAL_DISCOUNT);

	  return values;
  }
  
  public static Map<Long,String> getSiLimitMap()
  {
	  Map<Long, String> values = new WeakHashMap<Long, String>();
	  values.put(EDUCATIONALGRANT_COVER_KEY, EDUCATIONALGRANT_COVER_CODE);
	  values.put(TRANSPORTATION_EXPENSE_OF_MORTAL_REMAINS_COVER_KEY, TRANSPORTATION_EXPENSE_OF_MORTAL_REMAINS_COVER_CODE);
	  return values;
  }
  
  public static Map<String, String> getProductsForProporotionateDedForOthers() {
	  Map<String, String> values = new WeakHashMap<String, String>();
	  values.put(MEDIPREMIER_PRODUCT_CODE, MEDIPREMIER_PRODUCT_CODE);
	  values.put(STAR_CRITICARE_PRODUCT, STAR_CRITICARE_PRODUCT);
	  values.put(GMC_PRODUCT_CODE, GMC_PRODUCT_CODE);
	  
	  return values;
  }
  
  public static Long REALLOCATION_ACTIVE_STATUS = Long.valueOf(1351L);
  public static Long REALLOCATION_HOLD_STATUS = Long.valueOf(1352L);
  public static Long CPU_ALLOCATION_OTHERS = Long.valueOf(7068L);
  
  public static Long CPU_AUTO_CAPA = Long.valueOf(7060L);
  public static Long CPU_AUTO_CP = Long.valueOf(7061L);
  public static String CPU_AUTO_CASES = "AUTOPRCESS";
  public static Long AUTO_CPU_USER = Long.valueOf(7063L);
  public static Long AUTO_CORP_USER = Long.valueOf(7062L);
  public static String AUTO_USERS = "USER";
  public static String AUTO_PROCESS_SUGGESTION = "AUTOALLOCPROC";
  
  public static Long DRAFT_INVESTIGATION = Long.valueOf(202L);
  
  public static Map<Long, Long> getEducationGrantValues() {
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(ACCIDENT_CARE_INDIVIDUAL_PRODUCT_KEY, Long.valueOf(101l));
	  values.put(SCHOOL_STUDENT_CARE_PRODUCT_KEY, Long.valueOf(110l));
	  values.put(COLLEGE_STUDENT_CARE_PRODUCT_KEY, Long.valueOf(119l));
	  values.put(ACCIDENT_CARE_GROUP_PRODUCT_KEY, Long.valueOf(128l));
	  values.put(ACCIDENT_TRAUMA_CARE_INDIVIDUAL_PRODUCT_KEY, Long.valueOf(146l));
	  values.put(ACCIDENT_TRAUMA_CARE_GROUP_PRODUCT_KEY, Long.valueOf(155l));
	  values.put(ACCIDENT_CARE_INDIVIDUAL_REVISED_PRODUCT_KEY, Long.valueOf(137l));
	  values.put(FAMILY_HEALTH_OPTIMA_PRODUCT_KEY, Long.valueOf(164l));
	  values.put(PACKAGE_PAC_PRD_003, Long.valueOf(173l));
	  values.put(FHO_PRODUCT_REVISED_PACKAGED,  Long.valueOf(206l));
	  values.put(ACCIDENT_CARE_INDIVIDUAL_REVISED_2015, Long.valueOf(137l));
	  values.put(MEDI_CLASSIC_ACCIDENT_CARE_PACKAGE, Long.valueOf(230l));
	  values.put(ACCIDENT_CARE_INDIVIDUAL_REVISED, Long.valueOf(101l));
	  values.put(POS_ACCIDENT_CARE_INDIVIDUAL_PRODUCT_KEY, Long.valueOf(239l));

	  values.put(PACK_ACCIDENT_CARE_012, Long.valueOf(321l));
	  values.put(PACK_ACCIDENT_CARE_014, Long.valueOf(253l));
	  values.put(SARAL_SURAKSHA_CARE_INDIVIDUAL, Long.valueOf(339l));

	  values.put(NEW_FHO_PRODUCT_REVISED_PACKAGED,  Long.valueOf(330l));
	  return values;
  }
  
  public static Map<Long, Long> getAccidentCoverValue() {
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(Long.valueOf(102l), Long.valueOf(102l));
	  values.put(Long.valueOf(111l), Long.valueOf(111l));
	  values.put(Long.valueOf(120l), Long.valueOf(120l));
	  values.put(Long.valueOf(129l), Long.valueOf(129l));
	  values.put(Long.valueOf(165l), Long.valueOf(165l));
	  values.put(Long.valueOf(174l), Long.valueOf(174l));
	  values.put(Long.valueOf(147l), Long.valueOf(147l));
	  values.put(Long.valueOf(156l), Long.valueOf(156l));
	  values.put(Long.valueOf(322l), Long.valueOf(322l));
	  return values;
  }
  
  public static Map<Long, Long> getTraumaCareValues() {
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(ACCIDENT_TRAUMA_CARE_INDIVIDUAL_PRODUCT_KEY, Long.valueOf(106l));
	  values.put(ACCIDENT_TRAUMA_CARE_GROUP_PRODUCT_KEY, Long.valueOf(107l));
	  
	  return values;
  }
	
	public static final Long ZONE_BRANCH_TYPE_ID = Long.valueOf(2l);
	
	public static final Long AREA_BRANCH_TYPE_ID = Long.valueOf(3L);
	
	public static final Long BRANCH_TYPE_ID = Long.valueOf(4L);
	
	public static List<Long> getSpecialityTypeSurgical() {
		List<Long> values = new ArrayList<Long>();
		values.add(SPECIALITY_TYPE_CARDIO);
		values.add(SPECIALITY_TYPE_INTERVENTIONAL);
		return values;
	}

	public static final Long SPECIALITY_TYPE_CARDIO = Long.valueOf(19L);
	public static final Long SPECIALITY_TYPE_INTERVENTIONAL = Long.valueOf(20L);
	
	public static String STENT = "STENT";
	
	public static Long GMC_PRODUCT_KEY = Long.valueOf(401L);
	public static Long STAR_GMC_PRODUCT_KEY = Long.valueOf(402L);
	public static Long STAR_GMC_NBFC_PRODUCT_KEY = Long.valueOf(603L);
	//FOR STAR CRITICARE OTHER BANKS
	public static Long STAR_CRITICARE_OTHER_BANKS_KEY = Long.valueOf(403L);

	public static final Long ROAD_TRAFFIC_ACCIDENT = Long.valueOf(1102L);
	
	public static final Long MODE_OF_RECEIPT_ONLINE = Long.valueOf(7123L);
	
	public static Long INTIMATION_ONLINE_MODE = Long.valueOf(1626L);

	public static List<Long> getStatusForSuppDoc() {
		List<Long> values = new ArrayList<Long>();
		values.add(PREAUTH_QUERY_RECEIVED_STATUS);
		values.add(PREAUTH_DENIAL_OF_CASHLESS_STATUS);
		values.add(PREAUTH_REJECT_STATUS);
		return values;
	}

	public static final Long PREAUTH_REFER_TO_CPU = Long.valueOf(3178L);
	public static final Long ENHN_REFER_TO_CPU = Long.valueOf(3179L);

	public static final String DOCVERIFY = "DOCVERIFY";
	
	public static Long CPU_ALLOCATION_APPROVE = Long.valueOf(7064L);
  
  public static Map<String, String> getGpaType() {
	  Map<String, String> values = new WeakHashMap<String, String>();
	  values.put("ACC-SEC-011A", "Named");
	  values.put("ACC-SEC-011B", "Named");
	  values.put("ACC-SEC-011C", "Named");
	  values.put("ACC-SEC-011D", "Named");
	  values.put("ACC-SEC-011E", "Un Named");
	  values.put("ACC-SEC-011F", "Un Named");
	  values.put("ACC-SEC-011G", "Un Named");
	  values.put("ACC-SEC-011H", "Un Named");
	  return values;
  }

  public static Long STAR_FIRST_COMPREHENSVE = Long.valueOf(63L);
  public static Long STAR_FIRST_OPTIMA = Long.valueOf(65L);
  public static Long STAR_DIABETIC_SAFE_FLOATER = Long.valueOf(69L);
  public static Long STAR_DIABETIC_SAFE_INDIVIDUAL = Long.valueOf(68L);
  public static Long STAR_CARDIAC_CARE = Long.valueOf(70L);
  public static Long STAR_DIABETIC_SAFE_INDIVIDUAL_REVISED = Long.valueOf(84L);
  public static Long STAR_DIABETIC_SAFE_FLOATER_REVISED = Long.valueOf(85L);
  public static Long STAR_CARDIAC_CARE_NEW = Long.valueOf(86L);
  
  @SuppressWarnings("unchecked")
  public static Map<Long, Long> getSectionKeysOfCombiProducts()
    {
      Map values = new HashMap<Long, Long>();
  
      values.put(STAR_DIABETIC_SAFE_FLOATER, STAR_DIABETIC_SAFE_FLOATER);
      values.put(STAR_DIABETIC_SAFE_INDIVIDUAL, STAR_DIABETIC_SAFE_INDIVIDUAL);
      values.put(STAR_DIABETIC_SAFE_INDIVIDUAL_REVISED, STAR_DIABETIC_SAFE_INDIVIDUAL_REVISED);
      values.put(STAR_CARDIAC_CARE, STAR_CARDIAC_CARE);
      values.put(STAR_DIABETIC_SAFE_FLOATER_REVISED, STAR_DIABETIC_SAFE_FLOATER_REVISED);      
      values.put(STAR_CARDIAC_CARE_NEW, STAR_CARDIAC_CARE_NEW);      
      values.put(STAR_CARDIAC_CARE_PLATIANUM, STAR_CARDIAC_CARE_PLATIANUM);
      return values;
    }
  
  public static Long SUPER_SURPLUS_SIVER_REJECT = Long.valueOf(7350l);
  public static Long SUPER_SURPLUS_GOLD_REJECT = Long.valueOf(7351l);
  
  public static Long SUPER_SURPLUS_GOLD_REJECT_RTL = Long.valueOf(7604l);
  
  public static Long SUPER_SURPLUS_SIVER_DINAL = Long.valueOf(7373l);
  public static Long SUPER_SURPLUS_GOLD_DINAL = Long.valueOf(7374l);
  public static Long CLAIM_APPROVAL_CLOSED = Long.valueOf(214L);
  public static Long FVR_GRADING_STATUS = Long.valueOf(206L);
  
  public static Long DEFINE_LIMIT_REJECTION_CATEGORY_ID = Long.valueOf(7351L);
  public static Long DEFINE_LIMIT_REJECTION_CATEGORY = Long.valueOf(7351L);
  
  
  public static Long REJECTED_RECONSIDERATION_ID = 7376L;
  public static Long SETTLED_RECONSIDERATION_ID = 7377L;
  
  public static Map<Long, Long> getFHORevisedKeys() {
	  
		Map<Long, Long> values = new WeakHashMap();
		  values.put(FHO_PRODUCT_REVISED, FHO_PRODUCT_REVISED);
		  values.put(FHO_PRODUCT_REVISED_1, FHO_PRODUCT_REVISED_1);
		  values.put(FHO_PRODUCT_REVISED_PACKAGED, FHO_PRODUCT_REVISED_PACKAGED);
		  values.put(POS_FAMILY_HEALTH_OPTIMA, POS_FAMILY_HEALTH_OPTIMA);
		  values.put(FHO_REVISED_PRODUCT_2021_KEY, FHO_REVISED_PRODUCT_2021_KEY);
		  values.put(NEW_FHO_PRODUCT_REVISED_PACKAGED, NEW_FHO_PRODUCT_REVISED_PACKAGED);
		  
		  return values;
		  
	  }
  public static Long STAR_CANCER_PRODUCT_KEY = Long.valueOf(73l);
  public static String STAR_CANCER_PRODUCT_CODE = "MED-PRD-054";
  public static Long STAR_CANCER_GOLD_PRODUCT_KEY = Long.valueOf(74l);
  public static String STAR_CANCER_GOLD_PRODUCT_CODE = "MED-PRD-055";
  
  public static Long STAR_CANCER_GOLD_REJ_CATG_KEY = Long.valueOf(7575L);
  
  public static Map<String, String> getEnabledSection() {
	  Map<String, String> values = new WeakHashMap();
	  values.put(SHAConstants.PRE_MEDICAL_PRE_AUTH, SHAConstants.PRE_MEDICAL_PRE_AUTH);
	  values.put(SHAConstants.PRE_AUTH, SHAConstants.PRE_AUTH);
	  values.put(SHAConstants.PRE_MEDICAL_ENHANCEMENT, SHAConstants.PRE_MEDICAL_ENHANCEMENT);
	  values.put(SHAConstants.PRE_AUTH_ENHANCEMENT, SHAConstants.PRE_AUTH_ENHANCEMENT );

	  return values;
	  
  }
  
  //lumen
  public static String LUMEN_SEARCH_TYPE = "LMNSRCHBY";
  public static String LUMEN_TYPE = "LMNTYPE";
  public static String LUMEN_ERR_TYPE = "LMN_HOSERRTYPE";

  //lumen stage
  public static Long LUMEN_PROCESS = Long.valueOf(34L);
  public static Long LUMEN_PROCESS_LEVEL_I = Long.valueOf(35L);
  public static Long LUMEN_PROCESS_LEVEL_CO_ORDINATOR = Long.valueOf(36L);
  public static Long LUMEN_PROCESS_LEVEL_II = Long.valueOf(37L);
  public static Long LUMEN_PROCESS_LEVEL_MIS = Long.valueOf(38L);
  public static Long LUMEN_PROCESS_INITIATOR_QUERY = Long.valueOf(39L);
  //lumen status
  public static Long LUMEN_QUERY = Long.valueOf(900L);
  
  //lumen process level 1 status (Based on button action)
  public static Long LUMEN_LEVEL_I_APPROVE = Long.valueOf(911L);
  public static Long LUMEN_LEVEL_I_REJECT = Long.valueOf(912L);
  public static Long LUMEN_LEVEL_I_REPLY = Long.valueOf(913L);
  public static Long LUMEN_LEVEL_I_QUERY = Long.valueOf(914L);
  public static Long LUMEN_LEVEL_I_MIS = Long.valueOf(915L);
  public static Long LUMEN_LEVEL_I_CLOSE = Long.valueOf(916L);
  
//Initiator Query Cases status (Based on button action)
  public static Long LUMEN_INITIATOR_REPLY = Long.valueOf(941L);
  
  //lumen process coordinator status (Based on button action)
  public static Long LUMEN_COORDINATOR_APPROVE = Long.valueOf(920L);
  public static Long LUMEN_COORDINATOR_REPLY = Long.valueOf(921L);
  public static Long LUMEN_COORDINATOR_QUERY = Long.valueOf(922L);
  public static Long LUMEN_COORDINATOR_MIS = Long.valueOf(923L);
  
  //lumen MIS Query status (Based on button action)
  public static Long LUMEN_MIS_REPLY = Long.valueOf(940L);
  
  //lumen process level 2 status (Based on button action)
  public static Long LUMEN_LEVEL_II_APPROVE = Long.valueOf(930L);
  public static Long LUMEN_LEVEL_II_REJECT = Long.valueOf(931L);
  public static Long LUMEN_LEVEL_II_QUERY = Long.valueOf(932L);
  public static Long LUMEN_LEVEL_II_MIS = Long.valueOf(933L);
  public static Long LUMEN_LEVEL_II_CLOSE = Long.valueOf(934L);

  public static Long UPDATE_PAYMENT_STAGE = Long.valueOf(41L);
  public static Long UPDATE_PAYMENT_STATUS = Long.valueOf(215L);

  public static Long RE_ASSIGN_INVESTIGATION_STATUS = Long.valueOf(3204L);
  
  
  public static Long ZMR_INITIATE_FIELD_REQUEST_STATUS = Long
			.valueOf(207L);
  
  public static Long ACCIDENT_CARE_INDIVIDUAL_REVISED_2015 = Long.valueOf(161L);
  public static Long ACCIDENT_CARE_INDIVIDUAL_REVISED = Long.valueOf(151L);
  public static Long MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY = Long.valueOf(75L);
  public static Long MEDI_CLASSIC_ACCIDENT_CARE_PACKAGE = Long.valueOf(214l);
  public static Long POS_ACCIDENT_CARE_INDIVIDUAL_PRODUCT_KEY = Long.valueOf(162L);
//  public static Long STAR_SENIOR_CITIZEN_RED_CARPET_REVISED_2018 = Long.valueOf(90L);
  
  public static Map<Long, Long> getHealthGainProducts() {
	  
		Map<Long, Long> values = new WeakHashMap();
		  values.put(STAR_HEALTH_GAIN_FLOATER_PRODUCT, STAR_HEALTH_GAIN_FLOATER_PRODUCT);
		  values.put(STAR_HEALTH_GAIN_INDIVIDUAL_PRODUCT, STAR_HEALTH_GAIN_INDIVIDUAL_PRODUCT);
		  return values;
		  
	 }
  public static Long INDORE_CPU_CODE = Long.valueOf(30L);
  public static Long CHENNAI_CPU_CODE = Long.valueOf(28L);
  public static Long LUCKNOW_CPU_CODE = Long.valueOf(29L);
  
  
  public static Map<Long, Long> getRemovableCpuCodeList() {
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(INDORE_CPU_CODE, INDORE_CPU_CODE);
	  values.put(CHENNAI_CPU_CODE, CHENNAI_CPU_CODE);
	  values.put(LUCKNOW_CPU_CODE, LUCKNOW_CPU_CODE);
	  return values;
  }

  public static String PED_TL = "PEDTL";

  public static Long PARALLEL_QUERY_CANCELLED = Long.valueOf(3203l);
  public static Long PARALLEL_INVES_CANCELLED = Long.valueOf(3202l);
  
  public static Long OMP_PROCESSOR_STAGE = Long.valueOf(3104L);
  public static Long OMP_APPROVER_STAGE = Long.valueOf(3105L);
  public static Long OMP_PROCESSOR_APPROVE_STATUS = Long.valueOf(3180L);
  public static Long OMP_PROCESSOR_REJECT_STATUS = Long.valueOf(3181L);
  public static Long OMP_APPROVER_APPROVE_STATUS = Long.valueOf(3182L);
  public static Long OMP_APPROVER_DISCHARGE_VOUCHER_DISPATCHED_STATUS = Long.valueOf(3221L);
  public static Long OMP_APPROVER_DISCHARGE_VOUCHER_RECEIVED_STATUS = Long.valueOf(3222L);
  public static Long OMP_APPROVER_REJECT_STATUS = Long.valueOf(3183L);
  public static Long OMP_REGISTRATION_REJECTED = Long.valueOf(272L);
  
  public static Long GRADING_FVR_NOT_RECEIVED = Long.valueOf(22L);
  public static Long RELATED_TO_PED = Long.valueOf(1062L);
  
  public static Long COMPASSIONATE_TRAVEL = Long.valueOf(81L);
  public static Long PREFERRED_NETWORK_HOSPITAL = Long.valueOf(83L);
  
  public static Long STAR_SPECIAL_CARE_PRODUCT_KEY = Long.valueOf(80L);
  public static Long SHARED_ACCOMODATION = Long.valueOf(84L);

  public static Map<Long, Long> getMedicalDecisionButtonStatus() {
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS, Long.valueOf(81L));
	  values.put(CLAIM_REQUEST_ESCALATION_STATUS, Long.valueOf(86L));
	  values.put(CLAIM_REQUEST_SPECIALIST_STATUS, Long.valueOf(83L));
	  values.put(CLAIM_REQUEST_REJECT_STATUS, Long.valueOf(93L));
	  values.put(CLAIM_REQUEST_APPROVE_STATUS, Long.valueOf(78L));
	  values.put(CLAIM_REQUEST_CANCEL_ROD_STATUS, Long.valueOf(148L));
	  values.put(CLAIM_REQUEST_SEND_REPLY_STATUS, Long.valueOf(87L));
	  values.put(CLAIM_REQUEST_SEND_REPLY_FA_STATUS, Long.valueOf(123L));
	  values.put(CLAIM_REQUEST_REFER_TO_BILL_ENTRY, Long.valueOf(209L));
	  values.put(CLAIM_REQUEST_ESCALATION_REPLY_STATUS, Long.valueOf(85L));
	  values.put(CLAIM_REQUEST_SEND_REPLY_CLAIM_APPROVAL_STATUS, Long.valueOf(185L));
	  return values;
  }
  
  public static Long PED_SUGGESTION_SUG002 = Long.valueOf(2092L);
  public static Long PED_SUGGESTION_SUG003 = Long.valueOf(2093L);
  public static Long PED_SUGGESTION_SUG004 = Long.valueOf(2094L);
  public static Long PED_SUGGESTION_SUG005 = Long.valueOf(2095L);
  public static Long PED_SUGGESTION_SUG010 = Long.valueOf(7375L);
  
  
  public static Map<Long, Long> getDefaultCopayNotApplicableProducts() {
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(STAR_UNIQUE_PRODUCT_KEY, Long.valueOf(35L));
	  values.put(SENIOR_CITIZEN_RED_CARPET_PRODUCT, Long.valueOf(54L));
	  values.put(SENIOR_CITIZEN_RED_CARPET, Long.valueOf(30L));
	  values.put(SUPER_SURPLUS_INDIVIDUAL, Long.valueOf(33L));
	  values.put(SUPER_SURPLUS_FLOATER, Long.valueOf(32L));
	  values.put(SUPER_SURPLUS_REVISED_FLOATER, Long.valueOf(60L));
	  values.put(SUPER_SURPLUS_REVISED_INDIVIDUAL, Long.valueOf(59L));
	  values.put(JET_PRIVILEGE_PRODUCT, Long.valueOf(71L));
	  values.put(SUPER_SURPLUS_INDIVIDUAL_2018, Long.valueOf(87L));
	  values.put(SUPER_SURPLUS_FLOATER_2018, Long.valueOf(88L));
	  values.put(SUPER_SURPLUS_REVISED_INDIVIDUAL_2020, Long.valueOf(529L));
	  values.put(SUPER_SURPLUS_REVISED_FLOATER_2020, Long.valueOf(530L));
	  return values;
  }
  public static Long HOSPITALISATION_SECTION_ID = Long.valueOf(179L);
  
  public static Long POS_MEDICLASSIC_PRODUCT_KEY = Long.valueOf(83L);
  
  public static Long EXECUTIVE_CLOSED = Long.valueOf(217L);
  public static Long CORPORATE_CLOSED = Long.valueOf(218L);
  
  public static Map<Long, Long> getFVRNotRequiredStatus() {
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(SKIPFVR, Long.valueOf(52L));
	  values.put(FVRCANCELLED, Long.valueOf(165L));
	  values.put(EXECUTIVE_CLOSED, Long.valueOf(217L));
	  values.put(CORPORATE_CLOSED, Long.valueOf(218L));
	  
	  return values;
  }
  
  public static Map<Long, Long> getCombinationOfHealthAndPA() {
	  Map<Long, Long> values = new WeakHashMap();
	  values.put(COMPREHENSIVE_37_PRODUCT, COMPREHENSIVE_37_PRODUCT);
	  values.put(COMPREHENSIVE_78_PRODUCT_FLOATER, COMPREHENSIVE_78_PRODUCT_FLOATER);
	  values.put(COMPREHENSIVE_78_PRODUCT_INDIVIDUAL, COMPREHENSIVE_78_PRODUCT_INDIVIDUAL);
	  values.put(JET_PRIVILEGE_PRODUCT, JET_PRIVILEGE_PRODUCT);
	  values.put(STAR_DIABETIC_SAFE_FLOATER, STAR_DIABETIC_SAFE_FLOATER);
	  values.put(STAR_DIABETIC_SAFE_INDIVIDUAL, STAR_DIABETIC_SAFE_INDIVIDUAL);
	  values.put(STAR_CARDIAC_CARE, STAR_CARDIAC_CARE);
	  values.put(STAR_DIABETIC_SAFE_INDIVIDUAL_REVISED, STAR_DIABETIC_SAFE_INDIVIDUAL_REVISED);
	  values.put(STAR_DIABETIC_SAFE_FLOATER_REVISED, STAR_DIABETIC_SAFE_FLOATER_REVISED);
	  values.put(GMC_PRODUCT_KEY, GMC_PRODUCT_KEY);
	  values.put(STAR_CARDIAC_CARE_NEW, STAR_CARDIAC_CARE_NEW);
	  values.put(JET_PRIVILEGE_GOLD_PRODUCT_KEY, JET_PRIVILEGE_GOLD_PRODUCT_KEY);
	  values.put(STAR_COMPREHENSIVE_IND_PRODUCT_KEY, STAR_COMPREHENSIVE_IND_PRODUCT_KEY);
	  values.put(STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY, STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY);
	  values.put(COMPREHENSIVE_88_PRODUCT_FLOATER, COMPREHENSIVE_88_PRODUCT_FLOATER);
	  values.put(COMPREHENSIVE_88_PRODUCT_INDIVIDUAL, COMPREHENSIVE_88_PRODUCT_INDIVIDUAL);
	  return values;
}
  public static String JIO_COPAY_TYPE_VALUE = "JIO_COPAY_TY";
  public static Long JIO_PED_COPAY_ID = Long.valueOf(5000L);
  public static Long JIO_NON_PED_COPAY_ID = Long.valueOf(5001L);
  public static Long JIO_SUBLIMIT_COPAY_ID = Long.valueOf(5002L);
  public static Long JIO_OTHERS_COPAY_ID = Long.valueOf(5003L);
  
  public static Long OMP_CLAIM_RELATED_CLASSIFICATION_KEY = Long.valueOf(1361L);
  public static Long OMP_CLAIM_RELATED_SUB_CLASSIFICATION_KEY = Long.valueOf(1376L);
  public static Long OMP_NEGOTIATOR_CLASSIFICATION_KEY = Long.valueOf(1363L);
  public static Long OMP_NEGOTIATOR_SUB_CLASSIFICATION_KEY = Long.valueOf(1375L);
  public static Long OMP_OTHER_EXP_CLASSIFICATION_KEY = Long.valueOf(1362L);
  
  public static Long WAIT_FOR_INPUT_KEY = Long.valueOf(220L);
  public static String WAIT_FOR_INPUT_STATUS = "Wait For Input";
  
  public static Map<Long, Long> setMaxCopayProducts() {
	  Map<Long, Long> values = new WeakHashMap();
	  values.put(ReferenceTable.STAR_CARDIAC_CARE, ReferenceTable.STAR_CARDIAC_CARE);
	  values.put(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY,ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY);
	  values.put(ReferenceTable.STAR_CARDIAC_CARE_NEW, ReferenceTable.STAR_CARDIAC_CARE_NEW);
	  values.put(ReferenceTable.STAR_CARDIAC_CARE_PLATIANUM, ReferenceTable.STAR_CARDIAC_CARE_PLATIANUM);

	  values.put(ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND, ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND);
	  return values;
	  
  }
  
  public static Long INVS_REPORT_UPLOADED_KEY = Long.valueOf(219L);
  public static String INVS_REPORT_UPLOADED_VALUE = "Investigation Report uploaded";
  
  public static Long CLAIM_WISE_ALLOW_APPROVAL = Long.valueOf(221l);
  
  public static Long GPA_GROUP_ACCIDENT= Long.valueOf(163L);
  
  public static Map<Long, Long> getGPAProducts(){
	  Map<Long, Long> values = new WeakHashMap();
	  values.put(ReferenceTable.GPA_PRODUCT_KEY, ReferenceTable.GPA_PRODUCT_KEY);
	  values.put(ReferenceTable.GPA_GROUP_ACCIDENT,ReferenceTable.GPA_GROUP_ACCIDENT); 
	  return values;
  }
 
  
  public static Map<String, String> getGpaUnnamedSection() {
	  Map<String, String> values = new WeakHashMap<String, String>();
	  values.put("ACC-SEC-011E", "Un Named");
	  values.put("ACC-SEC-011F", "Un Nnamed");
	  values.put("ACC-SEC-011G", "Un Named");
	  values.put("ACC-SEC-011H", "Un Named");
	  return values;
  }
  
  public static Map<Long, Long> getGMCProductListWithoutOtherBanks(){
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(GMC_PRODUCT_KEY, GMC_PRODUCT_KEY);
	  values.put(STAR_GMC_PRODUCT_KEY, STAR_GMC_PRODUCT_KEY);
	  values.put(STAR_GMC_NBFC_PRODUCT_KEY, STAR_GMC_NBFC_PRODUCT_KEY);
	  values.put(STAR_CORONA_GRP_PRODUCT_KEY, STAR_CORONA_GRP_PRODUCT_KEY);
	  values.put(STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM, STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM);
	  values.put(STAR_GRP_COVID_PROD_KEY_INDI, STAR_GRP_COVID_PROD_KEY_INDI);
	  values.put(STAR_GRP_COVID_PROD_KEY_LUMSUM, STAR_GRP_COVID_PROD_KEY_LUMSUM);
	  values.put(STAR_GROUP_CRITICAL_MULTIPAY_PRD_KEY, STAR_GROUP_CRITICAL_MULTIPAY_PRD_KEY);
	  return values;
  }
  
  public static Map<Long, Long> getGMCProductList(){
	  Map<Long, Long> values = new WeakHashMap<Long, Long>();
	  values.put(GMC_PRODUCT_KEY, GMC_PRODUCT_KEY);
	  values.put(STAR_GMC_PRODUCT_KEY, STAR_GMC_PRODUCT_KEY);
	  values.put(STAR_GMC_NBFC_PRODUCT_KEY, STAR_GMC_NBFC_PRODUCT_KEY);
	  values.put(STAR_CRITICARE_OTHER_BANKS_KEY, STAR_CRITICARE_OTHER_BANKS_KEY);
	  values.put(JET_PRIVILEGE_GOLD_PRODUCT_KEY, JET_PRIVILEGE_GOLD_PRODUCT_KEY);
	  values.put(STAR_CORONA_GRP_PRODUCT_KEY, STAR_CORONA_GRP_PRODUCT_KEY);
	  values.put(STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM, STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM);
	  values.put(STAR_GRP_COVID_PROD_KEY_INDI, STAR_GRP_COVID_PROD_KEY_INDI);
	  values.put(STAR_GRP_COVID_PROD_KEY_LUMSUM, STAR_GRP_COVID_PROD_KEY_LUMSUM);
	  values.put(STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY, STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY);
	  values.put(GROUP_TOPUP_PROD_KEY, GROUP_TOPUP_PROD_KEY);
	  values.put(STAR_GROUP_CRITICAL_MULTIPAY_PRD_KEY, STAR_GROUP_CRITICAL_MULTIPAY_PRD_KEY);
	  return values;
  }
  
  public static Map<String, String> getGMCProductCodeListWithoutOtherBanks(){
	  Map<String, String> values = new WeakHashMap<String, String>();
	  values.put(GMC_PRODUCT_CODE, GMC_PRODUCT_CODE);
	  values.put(STAR_GMC_PRODUCT_CODE, STAR_GMC_PRODUCT_CODE);
	  values.put(STAR_GMC_NBFC_PRODUCT_CODE, STAR_GMC_NBFC_PRODUCT_CODE);
	  values.put(GROUP_HOSPITAL_CASH_POLICY, GROUP_HOSPITAL_CASH_POLICY);
	  values.put(STAR_CORONA_GRP_PRODUCT_CODE, STAR_CORONA_GRP_PRODUCT_CODE);
	  values.put(STAR_GRP_COVID_PROD_CODE, STAR_GRP_COVID_PROD_CODE);
	  values.put(STAR_GROUP_CRITICAL_MULTIPAY_PRD_CODE, STAR_GROUP_CRITICAL_MULTIPAY_PRD_CODE);
	  return values;
  }
  
  
  public static Map<String, String> getGMCProductCodeList(){
	  Map<String, String> values = new WeakHashMap<String, String>();
	  values.put(GMC_PRODUCT_CODE, GMC_PRODUCT_CODE);
	  values.put(STAR_GMC_PRODUCT_CODE, STAR_GMC_PRODUCT_CODE);
	  values.put(STAR_GMC_NBFC_PRODUCT_CODE, STAR_GMC_NBFC_PRODUCT_CODE);
	  values.put(STAR_CRITICARE_OTHER_BANKS,STAR_CRITICARE_OTHER_BANKS);
	  values.put(STAR_CORONA_GRP_PRODUCT_CODE, STAR_CORONA_GRP_PRODUCT_CODE);
	  values.put(STAR_GRP_COVID_PROD_CODE, STAR_GRP_COVID_PROD_CODE);
	  values.put(STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE, STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE);
	  values.put(GROUP_TOPUP_PRODUCT_CODE_96, GROUP_TOPUP_PRODUCT_CODE_96);
	  values.put(STAR_GROUP_CRITICAL_MULTIPAY_PRD_CODE, STAR_GROUP_CRITICAL_MULTIPAY_PRD_CODE);
	  return values;
  }
  public static Long OPINION_VALIDATION_PENDING_KEY = Long.valueOf(3207);
  //Allocate Corporate Buffer
  public static Long ALLOCATE_CORP_BUFFER_STATUS_KEY = Long.valueOf(222L);
  public static String ALLOCATE_CORP_BUFFER_STATUS_VALUE = "Corporate Buffer Allocated";
  public static Long ESCLATE_TO_RAW = Long.valueOf(251L);
 
  public static Long PREAUTH_HOLD_STATUS_KEY = Long.valueOf(223L);
  public static String PREAUTH_HOLD_STATUS = "Hold";
  public static Long MODE_OF_INTIMATION_PHONE = Long.valueOf(1621L);
  public static Long OPINION_VALIDATION_ELAPSED_KEY = Long.valueOf(3208);
  
  public static Long SUBLIMIT_UPDATE = Long.valueOf(253L);
  public static String SUBLIMIT_UPDATE_VALUE = "Sublimit Updation";

  public static Long PAYMENT_CANCEL_UPDATED = Long.valueOf(224l);

    
  
  
  public static Long RAW_TEAM_REPLIED = Long.valueOf(252l);

  public static Map<Long, Long> invsAlertRequiredStatus() {
	  Map<Long, Long> values = new WeakHashMap();
	  values.put(ReferenceTable.PREAUTH_APPROVE_STATUS, ReferenceTable.PREAUTH_APPROVE_STATUS);
	  values.put(ReferenceTable.PREAUTH_REJECT_STATUS,ReferenceTable.PREAUTH_REJECT_STATUS);
	  values.put(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS,ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS);
	  values.put(ReferenceTable.ENHANCEMENT_APPROVE_STATUS, ReferenceTable.ENHANCEMENT_APPROVE_STATUS);
	  values.put(ReferenceTable.ENHANCEMENT_REJECT_STATUS, ReferenceTable.ENHANCEMENT_REJECT_STATUS);
	  values.put(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS, ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS);
	  values.put(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT, ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT);
	  values.put(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS, ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
	  values.put(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS, ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
	  values.put(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS, ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS);
	  values.put(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS, ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS);
	  values.put(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS, ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS);
	  return values;
  }
  public static List<String> getCumulativeProductBonusList() {
		List<String> values = new ArrayList<String>();
		values.add("MED-PRD-051");
		values.add("MED-PRD-034");
		values.add("MED-PRD-028");
		values.add("MED-PRD-027");
		values.add("MED-PRD-037");
		values.add("PAC-PRD-001");
		values.add("PAC-PRD-009");
		values.add("MED-PRD-011");
		values.add("ACC-PRD-001");
		values.add("ACC-PRD-015");
		values.add("MED-PRD-072");
		values.add("MED-PRD-083");
		values.add("MED-PRD-081");
		values.add("PAC-PRD-013");
		values.add("PAC-PRD-014");
		values.add("MED-PRD-087");
		values.add("MED-PRD-100");
		values.add("ACC-PRD-021");
		//values.add("MED-PRD-023");
		//values.add("MED-PRD-047");
		return values;
	}
  public static final Long DOCTYPE_NEFT = Long.valueOf(4026L);
  public static Map<Long, Long> getMaternityValidateProducts() {
	  Map<Long, Long> values = new WeakHashMap();
	  values.put(ReferenceTable.WEDDING_GIFT_PRODUCT_KEY, ReferenceTable.WEDDING_GIFT_PRODUCT_KEY);
	  values.put(ReferenceTable.COMPREHENSIVE_27_PRODUCT,ReferenceTable.COMPREHENSIVE_27_PRODUCT);
	  values.put(ReferenceTable.COMPREHENSIVE_37_PRODUCT, ReferenceTable.COMPREHENSIVE_37_PRODUCT);
	  values.put(ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER, ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER);
	  values.put(ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL, ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL);
	  values.put(ReferenceTable.STAR_COMPREHENSIVE_IND_PRODUCT_KEY, ReferenceTable.STAR_COMPREHENSIVE_IND_PRODUCT_KEY);
	  values.put(ReferenceTable.COMPREHENSIVE_88_PRODUCT_FLOATER, ReferenceTable.COMPREHENSIVE_88_PRODUCT_FLOATER);
	  values.put(ReferenceTable.COMPREHENSIVE_88_PRODUCT_INDIVIDUAL, ReferenceTable.COMPREHENSIVE_88_PRODUCT_INDIVIDUAL);
	  return values;

  }
  public static Long NEGOTIATION_INITIATED = Long.valueOf(261l);
  public static Long NEGOTIATION_AGREED = Long.valueOf(262l);
  public static Long NEGOTIATION_NOT_AGREED = Long.valueOf(263l);   
  public static Long NEGOTIATION_CANCELLED = Long.valueOf(264l);
  public static Long NEGOTIATION = Long.valueOf(265l);
  
  public static Long FEEDBACK_TYPE_MER = Long.valueOf(3302l);
  public static Long FEEDBACK_TYPE_CLAIM_RETAIL = Long.valueOf(3301l);
  public static Long FEEDBACK_TYPE_CLAIM_GMC = Long.valueOf(3303l);
  
  public static Long RATING_FOR_VERYGOOD = Long.valueOf(3311L);
  public static Long RATING_FOR_GOOD = Long.valueOf(3312L);
  public static Long RATING_FOR_SASTIFACTORY = Long.valueOf(3313L);
  public static Long RATING_FOR_AVERAGE = Long.valueOf(3314L);
  public static Long RATING_FOR_BELOWAVERAGE = Long.valueOf(3315L);
  public static Long FEEDBACK_REPORTED_STATUS= Long.valueOf(3211L);
  public static Long FEEDBACK_COMPLETED_STATUS= Long.valueOf(3213L); 
  
  public static String FEEDBACK_TYPE = "BMFBRATING";
  public static Long FEEDBACK_REPORTED_KEY =Long.valueOf(3211L);
  public static Long FEEDBACK_RESPONDED_KEY = Long.valueOf(3212L);
  
  public static String FEEDBACK = "BMFBTYPE";
  public static Long POLICY_FEEDBACK_TYPE_KEY = Long.valueOf(3301L);  
  public static Long PROPOSAL_FEEDBACK_TYPE_KEY = Long.valueOf(3302L);  
  public static Long CLAIM_FEEDBACK_TYPE_KEY = Long.valueOf(3303L);  
  
  public static String BM_FEEDBACK_PERIOD = "BMFBPRD";
  public static Long FEEDBACK_TYPE_VERY_GOOD = Long.valueOf(3311l);
  public static Long FEEDBACK_TYPE_GOOD = Long.valueOf(3312l);
  public static Long FEEDBACK_TYPE_SATISFACTORY = Long.valueOf(3313l);
  public static Long FEEDBACK_TYPE_AVERAGE = Long.valueOf(3314l);
  public static Long FEEDBACK_TYPE_BELOW_AVERAGE = Long.valueOf(3315l);
  
  public static String VERY_GOOD_FEEDBACK = "Very Good";
  public static String GOOD_FEEDBACK = "Good";
  public static String SATISFACTORY_FEEDBACK = "Satisfactory";
  public static String AVERAGE_FEEDBACK = "Average";
  public static String BELOW_AVERAGE_FEEDBACK = "Below Average";
  
  public static String POLICY_FEEDBACK = "Policy";
  public static String PROPOSAL_FEEDBACK = "Proposal";
  public static String CLAIM_FEEDBACK = "Claim";
  
  public static String NEW_POLICY = "New Policy";
  
  public static String WAITING_PERIOD = "Y";
  public static Long FEEDBACK_STATUS_TYPE_ALL = Long.valueOf(3316l);
  
  public static String ALLOPATHIC = "Allopathic";
  public static String NON_ALLOPATHIC = "Non-Allopathic";
  public static Long AYURVEDA_KEY = Long.valueOf(4329l);

  public static Long BANGALORE_CPU_ID = Long.valueOf(15L);
  public static String BANGALORE_CPU_CODE = "950002";
  public static String YES_FLAG = "Y";
  public static String NO_FLAG = "N";
  public static String TRUE = "true";
  public static String FALSE = "false";

  public static Long PENAL_EXCEEDS_IRDA_TAT = Long.valueOf(1391l);
  public static Long PENAL_DUE_TODAY = Long.valueOf(1392l);
  public static Long PENAL_DUE_IN_1_DAY = Long.valueOf(1393l);
  public static Long PENAL_DUE_IN_2_DAY = Long.valueOf(1394l);
  public static Long PENAL_DUE_IN_3_DAY = Long.valueOf(1395l);
  public static Long PENAL_DUE_IN_4_DAY = Long.valueOf(1396l);
  public static Long PENAL_DUE_IN_5_DAY = Long.valueOf(1397l);
  public static String NEGOTIATION_INITIATED_VALUE = "Negotiation Initiated";
  public static String NEGOTIATION_NOT_AGREED_VALUE = "Negotiation Not Agreed";
  public static String NEGOTIATION_AGREED_VALUE = "Negotiation Agreed";
  public static String NEGOTIATION_CANCELED_VALUE = "Negotiation Cancelled";
  public static Long HOSP_SECTION_ID_TATA_TRUST= Long.valueOf(179L);
  public static Long HOSP_SECTION_ID_TATA_TRUST_REVISED = Long.valueOf(227L);
  
  @SuppressWarnings("unchecked")
public static Map<Long, Long> getTataTrustSectionKeys() {
    Map values = new HashMap<Long, Long>();
    values.put(HOSP_SECTION_ID_TATA_TRUST, HOSP_SECTION_ID_TATA_TRUST);
    values.put(HOSP_SECTION_ID_TATA_TRUST_REVISED, HOSP_SECTION_ID_TATA_TRUST_REVISED);
    return values;
  }
  
  public static Long HOSPITALISATION_SECTION_ID_TATA = Long.valueOf(227L);
  public static final Long FILE_TYPE_OTHERS = Long.valueOf(4029L);
  public static Long BED_PHOTO = Long.valueOf(7579l);
  public static Long STAR_CRITICARE_PLATINUM_PRODUCT_KEY = Long.valueOf(89l);
  public static Long STAR_CRITICARE_GOLD_PRODUCT_KEY = Long.valueOf(92l);
  
  public static Long STANDALONE_NEGOTIATION_UPDATE_STAGE_KEY = Long.valueOf(52l);
  public static Long STANDALONE_NEGOTIATION_UPDATE_STATUS_KEY = Long.valueOf(266l);
  
  @SuppressWarnings("unchecked")
  public static Map<Long, Long> getLumsumProductKeys() {
     Map values = new HashMap<Long, Long>();
     values.put(MEDIPREMIER_PRODUCT_KEY, MEDIPREMIER_PRODUCT_KEY);
     values.put(STAR_CRITICARE_PRODUCT_KEY, STAR_CRITICARE_PRODUCT_KEY);
     values.put(STAR_CANCER_PRODUCT_KEY, STAR_CANCER_PRODUCT_KEY);
     values.put(STAR_CANCER_GOLD_PRODUCT_KEY, STAR_CANCER_GOLD_PRODUCT_KEY);
     values.put(STAR_CRITICARE_PLATINUM_PRODUCT_KEY, STAR_CRITICARE_PLATINUM_PRODUCT_KEY);
     values.put(STAR_CRITICARE_GOLD_PRODUCT_KEY, STAR_CRITICARE_GOLD_PRODUCT_KEY);
     values.put(STAR_CORONA_VIRUS_PRODUCT_KEY, STAR_CORONA_VIRUS_PRODUCT_KEY);
     values.put(NOVEL_CORONA_VIRUS_PRODUCT_KEY, NOVEL_CORONA_VIRUS_PRODUCT_KEY);
     values.put(RAKSHAK_CORONA_PRODUCT_KEY, RAKSHAK_CORONA_PRODUCT_KEY);

	  values.put(STAR_CANCER_PLATINUM_PRODUCT_KEY_IND, STAR_CANCER_PLATINUM_PRODUCT_KEY_IND);
     return values;
   }
  
  public static String STAR_CRITICARE_PLATINUM_PRODUCT_CODE = "MED-PRD-069";
  public static String STAR_CRITICARE_GOLD_PRODUCT_CODE = "MED-PRD-071";
  public static final String JET_PRIVILEGE_PRODUCT_CODE = "MED-PRD-052";
  
  public static String JET_PRIVILEGE_GROUP_PRODUCT = "MED-PRD-073";
  
  public static String JET_PRIVILEGE_GOLD_PRODUCT = "MED-PRD-074";
  public static Long JET_PRIVILEGE_GOLD_PRODUCT_KEY = Long.valueOf(404l);
  
  
  @SuppressWarnings("unchecked")
  public static Map<Long, Long> getLumsumAlertApplicableProducts() {
     Map values = new HashMap<Long, Long>();
     values.put(MEDIPREMIER_PRODUCT_KEY, MEDIPREMIER_PRODUCT_KEY);    
     values.put(STAR_CRITICARE_PLATINUM_PRODUCT_KEY, STAR_CRITICARE_PLATINUM_PRODUCT_KEY);
     values.put(STAR_CRITICARE_GOLD_PRODUCT_KEY, STAR_CRITICARE_GOLD_PRODUCT_KEY);
     values.put(STAR_CORONA_VIRUS_PRODUCT_KEY, STAR_CORONA_VIRUS_PRODUCT_KEY);
     values.put(NOVEL_CORONA_VIRUS_PRODUCT_KEY, NOVEL_CORONA_VIRUS_PRODUCT_KEY);
     return values;
   }
  
  @SuppressWarnings("unchecked")
  public static Map<String, String> getDirectReimbursementProducts() {
     Map values = new HashMap<String, String>();
     values.put(STAR_CRITICARE_PLATINUM_PRODUCT_CODE, STAR_CRITICARE_PLATINUM_PRODUCT_CODE);    
     values.put(STAR_CRITICARE_GOLD_PRODUCT_CODE, STAR_CRITICARE_GOLD_PRODUCT_CODE);
     values.put(STAR_CORONA_VIRUS_PRODUCT_CODE, STAR_CORONA_VIRUS_PRODUCT_CODE);
     values.put(NOVEL_CORONA_VIRUS_PRODUCT_CODE, NOVEL_CORONA_VIRUS_PRODUCT_CODE);
     //IMSSUPPOR-30911 - Commented only for product code MED-PRD-076
     //values.put(HOSPITAL_CASH_POLICY, HOSPITAL_CASH_POLICY); 
     return values;
   }

  public static Long CONVERSION_REASON_FOR_PATIENT_ADMITTED_PAID = Long.valueOf(2002l);
  public static Long INSURED_PAID_AMOUNT_NOT_UTILIZED = Long.valueOf(1551l);
  
  @SuppressWarnings("unchecked")
  public static Map<Long, Long> getRevisedCriticareProducts() {
     Map values = new HashMap<Long, Long>();
     values.put(STAR_CRITICARE_PLATINUM_PRODUCT_KEY, STAR_CRITICARE_PLATINUM_PRODUCT_KEY);     
     values.put(STAR_CRITICARE_GOLD_PRODUCT_KEY, STAR_CRITICARE_GOLD_PRODUCT_KEY);
     values.put(STAR_CORONA_VIRUS_PRODUCT_KEY, STAR_CORONA_VIRUS_PRODUCT_KEY); 
     values.put(NOVEL_CORONA_VIRUS_PRODUCT_KEY, NOVEL_CORONA_VIRUS_PRODUCT_KEY);
     return values;
   }
  
  public static Long PAYMENT_PROCESS_CLOSED = Long.valueOf(227L);
  public static Long PAYMENT_PROCESS_REOPENED = Long.valueOf(228L);
  public static final Long SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL = Long
			.valueOf(90L);
  public static final Long SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER = Long
			.valueOf(91L);
  
  
  @SuppressWarnings("unchecked")
  public static Map<String, String> getMasterPolicyAvailableProducts() {
     Map values = new HashMap<String, String>();
     values.put(STAR_CRITICARE_PLATINUM_PRODUCT_CODE, STAR_CRITICARE_PLATINUM_PRODUCT_CODE);    
     values.put(STAR_CRITICARE_GOLD_PRODUCT_CODE, STAR_CRITICARE_GOLD_PRODUCT_CODE);
     values.put(JET_PRIVILEGE_PRODUCT_CODE, JET_PRIVILEGE_PRODUCT_CODE);
     values.put(JET_PRIVILEGE_GROUP_PRODUCT,JET_PRIVILEGE_GROUP_PRODUCT);
     values.put(JET_PRIVILEGE_GOLD_PRODUCT, JET_PRIVILEGE_GOLD_PRODUCT);
     values.put(STAR_CORONA_VIRUS_PRODUCT_CODE, STAR_CORONA_VIRUS_PRODUCT_CODE);  
     values.put(NOVEL_CORONA_VIRUS_PRODUCT_CODE, NOVEL_CORONA_VIRUS_PRODUCT_CODE);
     values.put(RAKSHAK_CORONA_PRODUCT_CODE, RAKSHAK_CORONA_PRODUCT_CODE);
     values.put(STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE,STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE);
     values.put(GROUP_TOPUP_PRODUCT_CODE_96,GROUP_TOPUP_PRODUCT_CODE_96);
     return values;
   }
  
  public static final Long STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY = Long.valueOf(404L);

  public static final String STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS = "MED-PRD-074"; 

  public static final Long KOTAK_CPU_CODE = Long.valueOf(34L);

  
  public static final Long CLS_QUERY_TYPE_NEW_QUERY_KEY = Long.valueOf(1771L);
  public static final Long CLS_QUERY_TYPE_REMINDER_KEY = Long.valueOf(1772L);
  
  public static final Long RELATION_SHIP_SELF_KEY = Long.valueOf(4201L);
  
  public static final Long DRAFT = Long.valueOf(1171L);
  public static final Long SUBMITTED = Long.valueOf(1173L);
  public static final Long PENDING = Long.valueOf(1172L);
 
  
  public static Map<Long, Long> getPreauthQueryTypeMap() {
	  
	  Map values = new WeakHashMap<Long, Long>();
	  
	  values.put(CLS_QUERY_TYPE_NEW_QUERY_KEY, 58l);   
	  values.put(CLS_QUERY_TYPE_REMINDER_KEY, 59l);    
	  
	  return values;
	  
  }
  
  public static Map<Long, Long> getEnhQueryTypeMap() {
	  
	  Map values = new WeakHashMap<Long, Long>();
	  
	  values.put(CLS_QUERY_TYPE_NEW_QUERY_KEY, 60l);   
	  values.put(CLS_QUERY_TYPE_REMINDER_KEY, 61l);    
	  
	  return values;
  }
  
  public static Map<Long, Long> getCallCenterStatusValues() {
	     Map values = new HashMap<Long, Long>();
	     values.put(DRAFT, Long.valueOf(9l));    
	     values.put(SUBMITTED, Long.valueOf(8l));
	     values.put(PENDING, Long.valueOf(10l));
	     return values;
  }

  public static final String ONLY_PRE_AUTH = "Only Pre-auth"; 
  public static final String PRE_AUTH_WITH_FINAL_BILL = "Pre-auth with final bill"; 
  public static Long ONLY_PRE_AUTH_KEY = Long.valueOf(1791L);
  public static Long PRE_AUTH_WITH_FINAL_BILL_KEY = Long.valueOf(1792L);
  
  public static Long PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION = Long.valueOf(7594L);
  public static Long PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_ENH = Long.valueOf(7637L);
  
  public static Long REJ_SUB_CATEG_PED_CANCEL_POLICY = Long.valueOf(10001L);
  public static Long REJ_SUB_CATEG_PED_CANCEL_POLICY_ENH = Long.valueOf(10005L);
  
  public static Long PED_SYMPTOMS_PRIOR_TO_POLICY_INCEPTION_REIMB = Long.valueOf(7921L);
  public static Long DOC_TYPE_CONTENT_LETTER = Long.valueOf(40L);
 
  public static Long CARDIAC_RELATED_90_DAYS_WAITING_PERIOD = Long.valueOf(8601L);
  
  
  public static Long PAYMENT_LVL1_TYPE_SEARCH_BATCH = 229L;
  public static Long PAYMENT_LVL2_TYPE_SEARCH_BATCH = 230L;
  
  public static Long PAYMENT_LVL1_TYPE_CREATE_BATCH = 157L;
  public static Long PAYMENT_LVL2_TYPE_CREATE_MASTERCODE_Y = 229L;
  
  public static Long PAYMENT_LVL2_TYPE_CREATE_MASTERCODE_N = 157L;
  //CR2019169
  public static final String NATURE_OF_LOSS = "NATURE_LOSS";
  public static final String CAUSE_OF_LOSS = "CAUSE_LOSS";
  
  public static List<String> getHealthValues() {
	  List<String> values = new ArrayList<String>();
	  values.add("HEALTH_PA");
	  values.add("HEALTH");
	  return values;
  }
  public static List<String> getPAValues() {
	  List<String> values = new ArrayList<String>();
	  values.add("HEALTH_PA");
	  values.add("PA");
	  return values;
  }
  
  public static Long HEALTH_LEGAL_HEIR_DOC_KEY = Long.valueOf(4025L);
  public static Long PA_LEGAL_HEIR_DOC_KEY = Long.valueOf(4423L);
  
  public static Long MEDI_CLASSIC_BASIC_PRODUCT_KEY = Long.valueOf(97L);
  public static Long MEDI_CLASSIC_GOLD_PRODUCT_KEY = Long.valueOf(98L);

  public static String HOSPITAL_CASH_DUE_TO = "PHC_DUETO";

  public static String HOSPITAL_CASH_POLICY = "MED-PRD-076";
  
  public static Long HOSPITAL_CASH_POLICY_IND_B = Long.valueOf(502L);
  //public static Long HOSPITAL_CASH_POLICY_IND_E = Long.valueOf(503L);
  public static Long HOSPITAL_CASH_POLICY_FLT_B = Long.valueOf(504L);
  public static Long HOSPITAL_CASH_POLICY_FLT_E = Long.valueOf(505L);
  
  public static final String ICR_AGENT = "ICR_AGENT";
  public static String AUTO_ALLOCATION_DOCUMENT_TYPE = "GNCLMACSSTG";
  public static String PCC_REMARK_FLAG = "PCCLOV";
  
  public static String GROUP_HOSPITAL_CASH_POLICY = "MED-PRD-080";

  public static String PCC_MASTER_CODE = "PCC";
  public static String NON_PCC_MASTER_CODE = "Non-PCC";
  
  public static String HOSPITAL_CASH_PATIENT_DAY_CARE = "PHC_DAYCARE";
  public static String OP_RECMODE = "OP_RECMODE";
  public static String OP_TRTMNT_TYPE = "OP_NATURTRTMNT";

  public static Long PED_QUERY_STAGE_KEY = Long.valueOf(17L);
  public static Long PED_QUERY_STATUS_KEY = Long.valueOf(281L);
  
  public static Long BANCS_PED_QUERY_RECEIVED = Long.valueOf(282L);

  public static Long PREAUTH_RELEASE_STATUS_KEY = Long.valueOf(240L);
  public static String PREAUTH_RELEASE_STATUS = "Release";
  
  public static final String SM_AGENT = "SM_AGENT";
  
  public static String CLAIM_FLAG_TYPE = "CLMAPPTYP";
  public static String ACCIDENT_TRAUMA_GROUP_PRODUCT = "ACC-PRD-008";
  public static Long OP_PAYMENT_SETTLED = Long.valueOf(301L);

  public static Long REVISED_POS_MEDICLASSIC_PRODUCT_KEY = Long.valueOf(508L);

  public static String CLAIMS_ALERT_CATEGORY = "CLM_REMARK";
  
  public static final Long STAR_COMPREHENSIVE_IND_PRODUCT_KEY = Long.valueOf(507L);
  
  public static final Long MODE_OF_RECEIPT_EMAIL = Long.valueOf(7208L);
  public static Long NO_HC_MODE_OF_RECEIPT = Long.valueOf(7130L);

  public static Long WITHDRAWS_REJECT_STATUS = Long.valueOf(183);

  public static Long STAR_YOUNG_IND_PRODUCT_KEY = Long.valueOf(510L);
  
  public static Long STAR_YOUNG_FLT_PRODUCT_KEY = Long.valueOf(511L);
  
  public static Long STAR_CORONA_VIRUS_PRODUCT_KEY = Long.valueOf(512L);
  public static String STAR_CORONA_VIRUS_PRODUCT_CODE = "MED-PRD-085";
  
  public static Long STAR_AROGYA_SANJEEVANI_PRODUCT_INDIVIDUAL_KEY = Long.valueOf(514L);
  public static Long STAR_AROGYA_SANJEEVANI_PRODUCT_FLOATER_KEY = Long.valueOf(515L);
  public static String STAR_AROGYA_SANJEEVANI_PRODUCT_CODE = "MED-PRD-083";
  public static String STAR_YOUNG_PRODUCT_CODE = "MED-PRD-084";

  public static final Long PATIENT_STATUS_RECOVERED_CASHLESS = Long.valueOf(143L);
  public static final Long PATIENT_STATUS_SHIFTED_CASHLESS = Long.valueOf(144L);
  public static final Long PATIENT_STATUS_MED_ADVICE_CASHLESS = Long.valueOf(145L);
  
  public static final Long PATIENT_STATUS_RECOVERED_REIMB = Long.valueOf(153L);
  public static final Long PATIENT_STATUS_SHIFTED_REIMB = Long.valueOf(154L);
  public static final Long PATIENT_STATUS_MED_ADVICE_REIMB = Long.valueOf(155L);
  
  public static Long PAYMENT_HOLD_STATUS = Long.valueOf(231L);
  
  public static Long NOVEL_CORONA_VIRUS_PRODUCT_KEY = Long.valueOf(518l);
  public static String NOVEL_CORONA_VIRUS_PRODUCT_CODE = "MED-PRD-089";

  public static String STAR_CORONA_GRP_PRODUCT_CODE = "MED-PRD-086";
  public static Long STAR_CORONA_GRP_PRODUCT_KEY = Long.valueOf(405L);
  public static Long STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM = Long.valueOf(406L);
  public static String REJECTION_CATEGORY_LUMPSUM = "GMC_LUMPSUM";
  public static String REJECTION_CATEGORY_INDI = "GMC_INDEMNITY";
  public static final String PCC_SOURCE_TYPE = "PCCSOURCE";
  
  public static final Long ACCIDENT_FAMILY_CARE_FLT_KEY = Long.valueOf(164L);
  public static final String ACCIDENT_FAMILY_CARE_FLT_CODE = "ACC-PRD-020";
  
  @SuppressWarnings("unchecked")
  public static Map<Long, Long> getNewPatientStatusKeys()
    {
      Map values = new HashMap<Long, Long>();
      values.put(PATIENT_STATUS_RECOVERED_REIMB, PATIENT_STATUS_RECOVERED_REIMB);
      values.put(PATIENT_STATUS_SHIFTED_REIMB, PATIENT_STATUS_SHIFTED_REIMB);
      values.put(PATIENT_STATUS_MED_ADVICE_REIMB, PATIENT_STATUS_MED_ADVICE_REIMB);
      return values;
    }
  public static WeakHashMap<Long, Long> getMedicalExtentionKeys() {
	  
	  WeakHashMap<Long, Long> resultList = new WeakHashMap<Long, Long>();
	  
	  resultList.put(107l, 107l);
	  resultList.put(116l, 116l);
	  resultList.put(125l, 125l);
	  resultList.put(134l, 134l);
	  resultList.put(143l, 143l);
	  resultList.put(152l, 152l);
	  resultList.put(161l, 161l);
	  resultList.put(170l, 170l);
	  resultList.put(179l, 179l);
	  resultList.put(189l, 189l);
	  resultList.put(212l, 212l);
	  resultList.put(245l, 245l);
	  resultList.put(258l, 258l);
	  
	  return resultList;
  }
  
  public static Long POS_MED_CLASSIC_PRODUCT = Long.valueOf(508L);
  
  public static String KERALA_CPU_CODE = "950004";
  
  public static Long KOTAK_PROCESSING_CPU_CODE = Long.valueOf(950031L);
  
 public static WeakHashMap<Long, Long> getGMCRelationShipKey() {
	  
	  WeakHashMap<Long, Long> resultList = new WeakHashMap<Long, Long>();
	  
	  resultList.put(4203l, 4203l);
	  resultList.put(4204l, 4204l);
	  resultList.put(4215l, 4215l);
	  resultList.put(4218l, 4218l);
	  resultList.put(4235l, 4235l);
	
	  
	  return resultList;
  }
 
  public static Long PREAUTH_ICAC_REQUEST_INITIATED = Long.valueOf(3111);
  public static Long PREAUTH_ICAC_RESPONSE_RECEIVED = Long.valueOf(3112);
  public static Long PREAUTH_ICAC_RESPONSE_FINAL_RECEIVED = Long.valueOf(3113);
  public static Long ENHANCEMENT_ICAC_REQUEST_INITIATED = Long.valueOf(3114);
  public static Long ENHANCEMENT_ICAC_RESPONSE_RECEIVED = Long.valueOf(3115);
  public static Long ENHANCEMENT_ICAC_RESPONSE_FINAL_RECEIVED = Long.valueOf(3116);
  public static Long CLAIM_REQUEST_ICAC_REQUEST_INITIATED = Long.valueOf(3117);
  public static Long CLAIM_REQUEST_ICAC_RESPONSE_RECEIVED = Long.valueOf(3118);
  public static Long CLAIM_REQUEST_ICAC_RESPONSE_FINAL_RECEIVED = Long.valueOf(3119);
  public static String PREAUTH_ICAC_RESPONSE_RECEIVED_STATUS = "ICAC Response Received";
  public static String PREAUTH_ICAC_REQUEST_INITIATED_STATUS = "ICAC Request Initiated";
  public static String PREAUTH_ICAC_RESPONSE_FINAL_RECEIVED_STATUS = "ICAC Final Response Received";

  public static List<String> getQueryAndAssignedRoles(String action){

	  ArrayList<String> roles;
	  if(action.equals("PCC_COORDINATOR_QUERY")){
		  roles = new ArrayList<String>();
		  roles.add(SHAConstants.DIVISION_HEAD_ROLE);
		  roles.add(SHAConstants.PCC_REVIEWER_ROLE);
		  roles.add(SHAConstants.PCC_PROCESSOR_ROLE);
		  return roles;
	  }else if(action.equals("PCC_PROCESSOR_QUERY")){
		  roles = new ArrayList<String>();
		  roles.add(SHAConstants.DIVISION_HEAD_ROLE);
		  roles.add(SHAConstants.PCC_REVIEWER_ROLE);
		  return roles;
	  }else if(action.equals("PCC_PROCESSOR_APPROVE")){
		  roles = new ArrayList<String>();
		  roles.add(SHAConstants.ZONAL_MEDICAL_HEAD_ROLE);
		  return roles;
	  }else if(action.equals("PCC_PROCESSOR_DISAPPROVE")){
		  roles = new ArrayList<String>();
		  roles.add(SHAConstants.PCC_COORDINATOR_ROLE);
		  return roles;
	  }

	  return null;
  }
  
  public static List<Long> getPCCRemarkNonMandList(){

	  ArrayList<Long> nonMand = new ArrayList<Long>();
	  nonMand.add(1003L);
	  nonMand.add(1004L);
	  nonMand.add(1005L);
	  nonMand.add(1007L);
	  return nonMand;
  }

  public static List<Long> getPCCQueryStatusKeysByRole(String role){

	  ArrayList<Long> statusKeys;
	  if(role.equals(SHAConstants.PCC_PROCESSOR_ROLE)){
		  statusKeys = new ArrayList<Long>();
		  statusKeys.add(SHAConstants.PCC_PROCESSOR_QUERY_INITIATED_STATUS);
		  statusKeys.add(SHAConstants.PCC_REVIEWER_QUERY_REPLIED_STATUS);
		  statusKeys.add(SHAConstants.PCC_DIVISIONHEAD_QUERY_REPLIED_STATUS);
		  return statusKeys;
	  }else if(role.equals(SHAConstants.PCC_COORDINATOR_ROLE)){
		  statusKeys = new ArrayList<Long>();
		  statusKeys.add(SHAConstants.PCC_PROCESSOR_QUERY_REPLIED_STATUS);
		  statusKeys.add(SHAConstants.PCC_REVIEWER_QUERY_REPLIED_STATUS);
		  statusKeys.add(SHAConstants.PCC_DIVISIONHEAD_QUERY_REPLIED_STATUS);
		  return statusKeys;
	  }else if(role.equals(SHAConstants.PCC_REVIEWER_ROLE)){
		  statusKeys = new ArrayList<Long>();
		  statusKeys.add(SHAConstants.PCC_REVIEWER_QUERY_INITIATED_STATUS);
		  statusKeys.add(SHAConstants.PCC_DIVISIONHEAD_QUERY_REPLIED_STATUS);
		  return statusKeys;
	  }else if(role.equals(SHAConstants.DIVISION_HEAD_ROLE)){
		  statusKeys = new ArrayList<Long>();
		  statusKeys.add(SHAConstants.PCC_DIVISIONHEAD_QUERY_INITIATED_STATUS);
		  return statusKeys;
	  }else if(role.equals(SHAConstants.ZONAL_MEDICAL_HEAD_ROLE)){
		  statusKeys = new ArrayList<Long>();
		  statusKeys.add(SHAConstants.PCC_ZC_NEGOTIATION_DISAPPROVED_STATUS);
		  statusKeys.add(SHAConstants.PCC_HRMC_NEGOTIATION_DISAPPROVED_STATUS);
		  return statusKeys;
	  }else if(role.equals(SHAConstants.ZONAL_COORDINATOR_ROLE)){
		  statusKeys = new ArrayList<Long>();
		  statusKeys.add(SHAConstants.PCC_ZC_ASSIGNED_STATUS);
		  statusKeys.add(SHAConstants.PCC_ZC_NEGOTIATION_DISAPPROVED_STATUS);
		  return statusKeys;
	  }else if(role.equals(SHAConstants.HRM_COORDINATOR_ROLE)){
		  statusKeys = new ArrayList<Long>();
		  statusKeys.add(SHAConstants.PCC_HRMC_ASSIGNED_STATUS);
		  statusKeys.add(SHAConstants.PCC_HRMC_NEGOTIATION_DISAPPROVED_STATUS);
		  return statusKeys;
	  }

	  return null;
  }
  
  public static String STAR_CORONA_KAVACH_PRODUCT_CODE = "MED-PRD-090";
  public static Long STAR_CORONA_KAVACH_PRODUCT_KEY_IND = Long.valueOf(519L);
  public static Long STAR_CORONA_KAVACH_PRODUCT_KEY_FLT= Long.valueOf(520L);
  public static String HOME_CARE_TREATMENT_KAVACH= "COVID_TREATMENT";

  public static Long RAKSHAK_CORONA_PRODUCT_KEY = Long.valueOf(521l);
  public static String RAKSHAK_CORONA_PRODUCT_CODE = "MED-PRD-092";

  public static List<String> getCovidSpecifiedProducts() {
	  List<String> values = new ArrayList<String>();
	  values.add("MED-PRD-072");
	  values.add("MED-PRD-051");
	  values.add("MED-PRD-078");
	  values.add("MED-PRD-011");
	  values.add("MED-PRD-084");
	  values.add("MED-PRD-083");
	  values.add("PAC-PRD-011");
	  values.add("PAC-PRD-010");
	  values.add("MED-PRD-060");
	  values.add("MED-PRD-081");
	  values.add("MED-PRD-079");
	  values.add("MED-PRD-082");
	  values.add("MED-PRD-091");
	  values.add("MED-PRD-088");
	  values.add("MED-PRD-087");
	  values.add("MED-PRD-100");
	  return values;
  }

  public static List<String> getCovidInsuranceDiagnosisCode() {
	  List<String> values = new ArrayList<String>();
	  values.add("COVID-19, virus identified - U07.1");
	  values.add("COVID-19, virus not identified - U07.2");
	  return values;
  }

  public static List<String> getCovid19VariantInsuranceDiagnosisCode() {
		List<String> values = new ArrayList<String>();
		values.add("COVID-19, virus identified - U07.1");
		values.add("COVID-19, virus not identified - U07.2");
		values.add("Post COVID-19 condition,unspecified - U09.9");
		
		return values;
}


  public static String COVID19VARIANT = "COVID19VARIANT";
  public static String COCKTAILDRUG = "COCKTAILDRUG";

  public static String ADM_HOME_CARE_TREATMENT_KAVACH= "ADM_COVID_TREATMENT";

public static Long DENTAL_AND_OPTHALMIC_TREATMENT = Long.valueOf(102);
public static Long OUT_PATIENT_BENEFIT_COVER = Long.valueOf(101);

  public static Long PP_VERSION = Long.valueOf(1L);

  public static Long PACK_ACCIDENT_CARE_012= Long.valueOf(252L);
  public static Long PACK_ACCIDENT_CARE_014 = Long.valueOf(253L);
  
  public static Long PAC_TRANSPORTATION_EXPENSE_OF_MORTAL_REMAINS_COVER_KEY= Long.valueOf(322l);
  public static String PAC_TRANSPORTATION_EXPENSE_OF_MORTAL_REMAINS_COVER_CODE = "ACC-CVR-022";

  public static Long FSP = Long.valueOf(1704l);

  

  public static Long GROUP_HOSPITAL_CASH_PRODUCT_KEY = Long.valueOf(506L);
  
  public static Long PED_SUGGESTION_SUG001 = Long.valueOf(2091L);
  
  public static String PED_IMPACT_DIAG = "Current Diagnosis related to PED";
  public static Long HRM_INITINATE_KEY = Long.valueOf(3226L);
  public static Long HRM_COMPLETED_KEY = Long.valueOf(3227L);
  public static Long ZONAL_INITINATE_KEY = Long.valueOf(3228L);
  public static Long ZONAL_COMPLETED_KEY = Long.valueOf(3229L);
  public static Long OP_VERIFICATION_INITIATED_STATUS = Long.valueOf(411L);
  
  public static String REC_MARK_ESC_ROLE = "MKTESCROL";
  public static String REC_MARK_ESC_REASON = "MKTESCRES";
  
  public static Long GROUP_POLICY = Long.valueOf(2904L);
  
  public static String STAR_GRP_COVID_PROD_CODE = "MED-PRD-093";
  public static Long STAR_GRP_COVID_PROD_KEY_INDI = Long.valueOf(407L);
  public static Long STAR_GRP_COVID_PROD_KEY_LUMSUM = Long.valueOf(408L);
  public static String REJ_CATG_GRP_LUMPSUM_93 = "REJ_RESON_LUMPSUM";
  public static String REJ_CATG_GRP_INDIMEN_93 = "REJ_RESON_INDEMNITY";
  public static Long STOP_PAYMENT_NAME_CORRECTION = Long.valueOf(10857L);
  public static Long STOP_PAYMENT_STALE_DD = Long.valueOf(10858L);
  public static Long STOP_PAYMENT_OTHERS = Long.valueOf(10859L);
  
  public static String STOP_PAYMENT_REQUEST = "Stop payment requested";
  
  public static String MASTER_TYPE_CODE_RVO_FINDINGS = "RVO_FINDING";
  public static String MASTER_TYPE_CODE_RVO_REASON = "RVO_REASON";
  public static String MASTER_TYPE_CODE_RVO_GRADE = "INV_RVO_GRADE";


  public static Long  RVO_FINDINGS_NOT_ACCEPTED_CLAIM_REJECTED_KEY = 10924l;
  public static Long  RVO_FINDINGS_NOT_ACCEPTED_CLAIM_APPROVED_KEY = 10923l;
  public static Long  RVO_FINDINGS_ACCEPTED_CLAIM_REJECTED_KEY = 10922l;
  public static Long  RVO_FINDINGS_ACCEPTED_CLAIM_APPROVED_KEY = 10921l;
  public static String STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE = "MED-PRD-094";
  public static Long STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY = Long.valueOf(409l);

  public static final Long OTHERS_WITH_PRORORTIONATE_DEDUCTION = Long.valueOf(151L);
  public static final Long OTHERS_WITH_PRORORTIONATE_DEDUCTION_BILLTYPE = Long.valueOf(46L);
  public static final Long OTHERS_WITHOUT_PRORORTIONATE_DEDUCTION = Long.valueOf(152L);
  
  public static List<Long> getPropDedCalcValue() {
		List<Long> values = new ArrayList<Long>();
		values.add(OTHERS_WITH_PRORORTIONATE_DEDUCTION_BILLTYPE);
		values.add(OT_CHARGES);
		values.add(PROFESSIONAL_CHARGES);
		values.add(ANH_PACKAGES);
		values.add(COMPOSITE_PACKAGES);
		values.add(OTHER_PACKAGES);
		values.add(PROCEDURES);
		values.add(MISC_WITHIN_HOSPITAL);
		values.add(MISC_OUTSIDE_HOSPITAL);
		return values;
	}
  
  public static final Long DENIAL_RECONSIDERATION_KEY = 10903l;	
  public static final Long REJECTION_RECONSIDERATION_KEY = 10904l;
  public static final Long DEDUCTION_RECONSIDERATION_KEY = 10905l;

  
  public static String SELECTED_ALL_PRIORITY = "VIP|CRM|ATOS|CMD|ED|BM|ZM|CMD ELITE|MD|COVID";
  public static Long OUT_PATIENT_VACCINATION_BENEFIT = Long.valueOf(103);
  public static String OUT_PATIENT_VACCINATION_BENEFIT_VALUE ="Vaccination Benefit";
  public static final Long OUT_PATIENT_AGE_LIMIT = Long.valueOf(3L);
  
  public static List<String> getCovidProducts() {
		List<String> values = new ArrayList<String>();
		values.add("MED-PRD-072");
		values.add("MED-PRD-051");
		values.add("MED-PRD-078");
		values.add("MED-PRD-084");
		values.add("MED-PRD-079");
		values.add("MED-PRD-100");
		return values;
	}
  
  public static final Long OMP_ACKNOWLEDGE_STAGE_KEY = Long.valueOf(3109L);
  public static Long STAR_YOUNG_IND_PRODUCT_KEY_091 = Long.valueOf(524L);
  public static Long STAR_YOUNG_FLT_PRODUCT_KEY_091 = Long.valueOf(525L);

	public static final Long OMP_ACKNOWLEDGE_STATUS_KEY = Long.valueOf(3240L);
	public static Long COMPREHENSIVE_88_PRODUCT_INDIVIDUAL = Long.valueOf(522L);
	public static Long COMPREHENSIVE_88_PRODUCT_FLOATER = Long.valueOf(523L);
  public static Long MEDI_CLASSIC_BASIC_PRODUCT_KEY_98 = Long.valueOf(98L);
  
  public static final Long PAYMENT_VERIFICATION_PENDING_STATUS = Long.valueOf(3241L);
  public static final Long PAYMENT_MAKER_VERIFIED_STATUS = Long.valueOf(3242L);
  public static final Long PAYMENT_CHECKER_VERIFIED_STATUS = Long.valueOf(3243L);
  
  public static final Long PHYSICAL_VERIFICATION_STAGE = Long.valueOf(59L);
  
  
  public static Long  NEFT_STATUS_PENDING_KEY = 3244l;
  public static Long  NEFT_STATUS_RECEIVED_KEY = 3245l;
  public static Long  NEFT_STAGE_KEY = 32l;
  

    public static final Long SUPER_SURPLUS_REVISED_INDIVIDUAL_2020 = Long.valueOf(529L);
	public static final Long SUPER_SURPLUS_REVISED_FLOATER_2020 = Long.valueOf(530L);
	
	public static final String SUPER_SURPLUS_REVISED_INDIVIDUAL_CODE = "MED-PRD-097";
	public static final String SUPER_SURPLUS_REVISED_FLOATER_CODE = "MED-PRD-098";
    public static final Long GROUP_TOPUP_PROD_KEY = Long.valueOf(410L);
//	public static final Long GROUP_TOPUP_FLOATER = Long.valueOf(530L);
	
	public static final String GROUP_TOPUP_PRODUCT_CODE_96 = "MED-PRD-096";

  public static Long FHO_REVISED_PRODUCT_2021_KEY = Long.valueOf(531L);

  public static Long VALUABLE_SERVICE_PROVIDER = Long.valueOf(154L);
  
  public static Long STAR_GROUP_HEALTH_INSURANCE_PROD_KEY = Long.valueOf(402l);

  public static final Long SARAL_SURAKSHA_CARE_INDIVIDUAL = Long.valueOf(165L);
  public static final String SARAL_SURAKSHA_CARE_PRODUCT_CODE ="ACC-PRD-021";
  
  public static Long HbA1C_TREATMENT = Long.valueOf(104);
  public static final Long NETWORK_HOSPITAL_DISCOUNT = Long.valueOf(153L);

  public static Long STAR_CARDIAC_CARE_PLATIANUM = Long.valueOf(533L);

  public static final String STAR_CARDIAC_CARE_PLATIANUM_CODE = "MED-PRD-104";

  public static String REJECTION_CARDIAC_PLAT = "REJCTGY_CARDIAC_PLAT";
  
  public static WeakHashMap<Long, Long> getMasClaimLimitSublimitKeysProd() {

		WeakHashMap<Long, Long> resultList = new WeakHashMap<Long, Long>();

		resultList.put(17847l, 17847l);
		resultList.put(17848l, 17848l);
		resultList.put(17849l, 17849l);
		resultList.put(17850l, 17850l);

		return resultList;
	}
  
  public static Long STAR_CANCER_PLATINUM_PRODUCT_KEY_IND = Long.valueOf(532L);

  public static Long OUT_PATIENT_BENEFIT_VACINATION_COVER = Long.valueOf(105);
  
  public static final String DISPATCH_BULK_UPLOAD_TYPE = "BLKUPTYP";
  
  public static Long AWSNUMBER_UPDATE_STATUS = Long.valueOf(3251L);
  
  public static Long DISPATCH_DETAILS_UPDATE_STATUS = Long.valueOf(3252L);
  
  public static final Long D_BULK_UPLOAD_TYPE_AWB = Long.valueOf(11028L);
  
  public static final Long D_BULK_UPLOAD_TYPE_DD = Long.valueOf(11029L);
  
  public static Long NEW_FHO_PRODUCT_REVISED_PACKAGED = Long.valueOf(254L);
  
  @SuppressWarnings("unchecked")
  public static Map<Long, Long> getValuableServiceProviderForFHO()
  {
    Map values = new HashMap<Long, Long>();
    values.put(NEW_FHO_PRODUCT_REVISED_PACKAGED, NEW_FHO_PRODUCT_REVISED_PACKAGED);
    return values;
  }

  public static final String STAR_GROUP_CRITICAL_MULTIPAY_PRD_CODE ="MED-PRD-105";
  public static Long STAR_GROUP_CRITICAL_MULTIPAY_PRD_KEY = Long.valueOf(411L);
  public static final Long PAYMENT_SETTLED_STATUS = Long.valueOf(158L);
}

