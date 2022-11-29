package com.shaic.main.navigator.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.Stateless;

import com.shaic.arch.SHAConstants;
import com.shaic.cmn.login.ImsUser;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.ui.MenuPresenter;

@Stateless
public class MenuItemBean {
	public static final String HOME_PAGE = "HOME_PAGE";
	public static final String TEST_COMPONENT = "Test Component";
	public static final String HEALTH_PAGE = "HEALTH_PAGE";
	public static final String CASHLESS_PAGE = "CASHLESS_PAGE";

	public static final String REIMBURSEMENT_PAGE = "REIMBURSEMENT_PAGE";
	public static final String NEW_INTIMATION = "REVISED_NEW_INTIMATION";
	public static final String NEW_OMP_INTIMATION = "NEW_OMP_INTIMATION";
	public static final String NEW_PREAUTH = "NEW_PREAUTH";
	public static final String INTIMATION_HEADER = "INTIMATION_HEADER";
	public static final String EDIT_INTIMATION = "EDIT_INTIMATION";
	public static final String REVISED_EDIT_INTIMATION = "REVISED_EDIT_INTIMATION";
	public static final String SEARCH_INTIMATION = "SEARCH_INTIMATION";
	public static final String INTIMATION_VIEW_DETAILS = "INTIMATION_VIEW_DETAILS";
	public static final String UNLOCK_INTIMATION = "unlock_intimation";

	public static final String UNLOCK_INTIMATION_BPMN_TO_DB = "unlock_intimation_BPMN_to_DB";
	// public static final String UNLOCK_OMP_INTIMATION_DB
	// ="unlock_omp_intimation_DB";
	public static final String UPDATE_HOSPITAL_INFORMATION = "UPDATE_HOSPITAL_INFORMATION";
	public static final String EDIT_HOSPITAL_INFORMATION = "EDIT_HOSPITAL_INFORMATION";
	public static final String SEARCH_POLICY = "CREATE_INTIMATION";
	public static final String SEARCH_POLICY_PARAMETER = "CREATE_INTIMATION_WITH_PARAMETER";
	
//	REPORTS MENUS 
	public static final String SEARCH_CLAIM_POLICY_REPORT = "SEARCH_CLAIM_POLICYWISE";
	public static final String EXECUTIVE_STATUS_REPORT = "EXECUTIVE_STATUS_REPORT";
	public static final String EXECUTIVE_STATUS_SUMMARY_REPORT = "EXECUTIVE_STATUS_SUMMARY_REPORT";
	public static final String SEARCH_PREAUTH_CPUWISE_REPORT = "PREAUTH_CPUWISE_REPORT";
	public static final String CLAIM_STATUS_CPUWISE_REPORT = "CLAIM_STATUS_CPUWISE_REPORT";
	public static final String HELP_DESK_STATUS_REPORT = "HELP_DESK_STATUS_REPORT";
	public static final String CLAIMS_DAILY_REPORT = "CLAIMS_DAILY_REPORT";
	public static final String GMC_DAILY_REPORT = "GMC_DAILY_REPORT";
	public static final String PLANNED_ADMISSION_REPORT = "PLANNED_ADMISSION_REPORT";
	public static final String INTIMATED_RISK_DETAILS_REPORT = "INTIMATED_RISK_DETAILS_REPORT";
	public static final String MEDICAL_AUDIT_CLAIM_STATUS_REPORT = "MEDICAL_AUDIT_CLAIM_STATUS_REPORT";
	public static final String MEDICAL_AUDIT_CASHLESS_ISSUANCE_REPORT = "MEDICAL_AUDIT_CASHLESS_ISSUANCE_REPORT";
	public static final String INTIMATION_ALTERNATE_CPU_REPORT = "INTIMATION_ALTERNATE_CPU_REPORT";
	public static final String HOSPITAL_WISE_REPORT = "HOSPITAL_WISE_REPORT";
	public static final String CALL_CENTER_DASH_BOARD = "CALL_CENTER_DASH_BOARD";
	public static final String AUTOMATION_DASHBOARD = "AUTOMATION_DASHBOARD";
	public static final String PAYMENT_PENDING_DASHBOARD = "PAYMENT_PENDING_DASHBOARD";
	public static final String BILL_RECIECVED_STATUS_REPORT = "BILL_RECIECVED_STATUS_REPORT";
	public static final String ADMINISTRATIVE_DASH_BOARD = "ADMINISTRATIVE_DASH_BOARD";
	public static final String PAYMENT_PROCESS_REPORT = "PAYMENT_PROCESS_REPORT";
	public static final String FVR_ASSIGNMENT_REPORT = "FVR_ASSIGNMENT_REPORT";
	public static final String HOSPITAL_INTIMATION_STATUS = "HOSPITAL_INTIMATION_STATUS";
	public static final String MEDICAL_MAIL_STATUS = "MEDICAL_MAIL_STATUS";
	public static final String DAILY_REPORT = "DAILY_REPORT";
	public static final String CPU_WISE_PERFORMANCE_REPORT = "CPU_WISE_PERFORMANCE_REPORT";
	public static final String AGENT_BROKER_REPORT = "AGENT_BROKER_REPORT";
	public static final String OP_CLAIM_REPORT = "OP_CLAIM_REPORT";
	public static final String TAT_REPORT = "TAT_REPORT";
	public static final String ACK_WITHOUT_ROD_REPORT = "Ack_Without_Rod_Report"; // R1097
																					// ACK
																					// WITHOUT
																					// ROD
																					// 06-03-2018
	public static final String REVISED_PROVISION = "REVISED_PROVISION";
	public static final String AUTO_CLOSURE_BATCH = "AUTO CLOSURE BATCH";
	// public static final String CREATE_LOT_MAKER = " CREATELOT_MAKER";
	public static final String PAYMENT_BATCH_REPORT = "PAYMENT_BATCH_REPORT";

	public static final String PAYMENT_PROCESS = "PAYMENT_PROCESS";

	// PAYMENT PROCESS CPU MENU
	public static final String OP_SCREEN = "OP_SCREEN";
	public static final String PROCESSING_PAYMENT = "PROCESSING_PAYMENT";
	public static final String PAYMENT_PROCESS_CPU = "PAYMENT_PROCESS_CPU";
	public static final String PAYMENT_PROCESS_CPU_PAGE = "PAYMENT_PROCESS_CPU_PAGE";
	public static final String SEARCH_OR_UPLOAD_DOCUMENTS = "SEARCH_OR_UPLOAD_DOCUMENTS";
	public static final String SEARCH_OR_UPLOAD_DOCUMENTS_FOR_ACK_NOT_RECEIVED = "SEARCH_OR_UPLOAD_DOCUMENTS_FOR_ACK_NOT_RECEIVED";
	public static final String CANCEL_ACKNOWLEDGEMENT = "CANCEL_ACKNOWLEDGEMENT";

	public static final String PROCESS_64_VB_COMPLIANCE = "PROCESS_64_VB_COMPLIANCE";

	// Adding for creating new menu which was suggested by siva sir.
	public static final String CREATE_NEW_INTIMATION = "CREATE_NEW_INTIMATION";
	public static final String REGISTER_CLAIM = "REGISTER_CLAIMS";
	public static final String CONVERT_CLAIM_BULK = "BULK CONVERT CLAIM REIMBURSEMENT";
	public static final String CONVERT_CLAIM = "CONVERT CLAIM";
	public static final String PA_CONVERT_CLAIM = "PA_CONVERT CLAIM";
	public static final String SEARCH_CONVERT_PA_CLAIM = "SEARCH CONVERT PA CLAIM";
	public static final String CONVERT_PA_CLAIM_OUTSIDE_PROCESS = "Convert_pa_claim_outside_process";
	public static final String CONVERT_CLAIM_CASHLESS = "CONVERT_CLAIM_CASHLESS";
	public static final String CONVERT_CLAIM_OUTSIDE_PROCESS = "Convert_claim_outside_process";
	public static final String REGISTER_CLAIM_HEADER = "REGISTER_CLAIM_HEADER";
	public static final String REMINDER_LETTERS = "REMINDER_LETTERS";
	public static final String CONVERT_CLAIM_PAGE = "CONVERT_CLAIM_PAGE";
	public static final String GENERATE_REMINDER_LETTER_CLAIM_WISE = "GENERATE_REMINDER_LETTER_CLAIM_WISE";
	public static final String GENERATE_REMINDER_LETTER_BULK = "GENERATE_REMINDER_LETTER_BULK_removed";
	public static final String PRINT_REMINDER_LETTER_BULK = "PRINT_REMINDER_LETTER_BULK";
	public static final String PRINT_PA_REMINDER_LETTER_BULK = "PRINT_PA_REMINDER_LETTER_BULK";
	
	//CR2019034
	public static final String PRINT_OMP_PAYEMENT_LETTER_BULK = "PRINT_OMP_PAYEMENT_LETTER_BULK";

	// public static final String SEARCH_REMINDER_LETTERS =
	// "SEARCH_REMINDER_LETTERS";
	public static final String REPORTS_HEADER = "HEALTH_REPORTS_HEADER";
	public static final String HEALTH_REPORT = "REPORTS_HEADER";
	public static final String GENERATE_PA_REMINDER_LETTER_CLAIM_WISE = "GENERATE_PA_REMINDER_LETTER_CLAIM_WISE";
	public static final String GENERATE_PA_REMINDER_LETTER_BULK = "GENERATE_PA_REMINDER_LETTER_BULK_removed";
	// public static final String SEARCH_REMINDER_LETTERS =
	// "SEARCH_REMINDER_LETTERS";;
	public static final String PA_CLAIMS = "PA_CLAIMS";
	public static final String PA_REIMBURSEMENT_PAGE = "PA_REIMBURSEMENT_PAGE";

	public static final String PA_FIELD_VISIT = "PA_Field Visit";
	public static final String PA_SHOW_PROCESS_FIELD_VISIT = "PA_process field visit";

	public static final String PA_CLAIM_REGISTER = "PA_CLAIM_REGISTER";
	public static final String SEARCH_PA_CLAIM_REGISTER = "PA_CLAIM_REGISTRATION";
	public static final String PA_MEDICAL_APPROVAL = "PA_Medical Approval";
	public static final String PA_PROCESS_CLAIM_REQUEST = "PA_Process Claim Request";

	public static final String PA_HEALTH_PROCESS_CLAIM_REQUEST = "PA_health_Process Claim Request";
	
	public static final String PA_PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT = "PA_PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT";
	
	public static final String PA_HEALTH_PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT = "PA_HEALTH_PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT";

	public static final String PA_HEALTH_PROCESS_CLAIM_FINANCIALS = "PA_health_Process_claim_financial";

	public static final String PA_HEALTH_PROCESS_CLAIM_BILLING = "PA_health_Process_claim_billing";

	public static final String PA_BILLING = "PA_BILLING";
	public static final String PA_PROCESS_CLAIM_BILLING = "PA_PROCESS_CLAIM_BILLING";

	public static final String PA_FINANCIAL = "PA_FINANCIAL";
	public static final String PA_NON_HOSP_PROCESS_CLAIM_FINANCIAL = "PA_NON_HOSP_PROCESS_CLAIM_FINANCIAL";

	public static final String PA_CLAIM_APPROVAL = "PA_CLAIM_APPROVAL";
	public static final String PA_CLAIM_APPROVAL_NON_HOS = "PA_CLAIM_APPROVAL_NON_HOS";
	public static final String PA_CLAIM_APPROVAL_HOS = "PA_CLAIM_APPROVAL_HOS";

	public static final String PA_CASHLESS_PROCESS_HEADER = "PA Cashless Process";
	public static final String PA_PROCESS_PREAUTH_REJECTION = "PA Process Rejection";
	public static final String PA_FIRST_LEVEL_PROCESSING_PREMEDICAL = "PA first level processing premedical";
	public static final String PA_FIRST_LEVEL_PROCESSING_PREMEDICAL_ENHANCEMENT = "PA first level processing premedical enhancement";
	public static final String PA_PROCESS_PREAUTH = "PA Process preauth";
	public static final String PA_PROCESS_ENHANCEMENT = "PA Process Enhancement";
	public static final String PA_STANDALONE_WITHDRAW = "PA standalone withdraw";
	public static final String PA_STANDALONE_DOWNSIZE = "PA standalone downsize";
	public static final String PA_PROCESS_DOWNSIZE = "PA process downsize";
	public static final String PA_CLEAR_CASHLESS = "PA Clear cashless";

	public static final String SEARCH_REGISTER_CLAIM = "SEARCH_REGISTER_CLAIM";
	public static final String SEARCH_GHI_ALLOW_REGISTER_CLAIM = "SEARCH_GHI_ALLOW_REGISTER_CLAIM";
	public static final String INTIMATION_FINISH = "INTIMATION_FINISH";
	public static final String INDEX = "";
	public static final String GENERATE_COVERINGLETTER = "Generate CoveringLetter";
	public static final String SEARCH_GENERATE_PA_COVERING_LETTER = "Search Generate PA Covering Letter";
	public static final String PA_LETTER_PAGE = "PA_LETTER_PAGE";
	public static final String PA_COVERING_LETTER_DETAIL = "PA Covering Letter Detail";
	public static final String GENERATE_REJECTIONLETTER = "Generate RejectionLetter";
	public static final String UPDATEHOSPITAL = "update Hospital details";
	public static final String HOSPITAL_COMMUNICATION = "Acknowledgement Hospital Communication";
	public static final String CASHLESS_PROCESS_HEADER = "Cahsless Process";
	public static final String PROCESS_PREAUTH_REJECTION = "Process Rejection";
	public static final String PROCESS_PRE_MEDICAL = "Process Pre Medical (Pre-Auth)";
	public static final String PROCESS_PREAUTH = "Process Pre-Auth";
	public static final String PROCESS_PREAUTH_AUTO_ALLOCATION = "Preauth Auto Allocation";
	public static final String PROCESS_ENHANCEMENT = "Process Enhancements";
	public static final String ACKNOWLEDGE_HOSPITAL_COMMUNICATION = "Acknowledge Hospital Communication_removed";
	public static final String PRE_MEDICAL_PROCESSING_ENHANCEMENT = "Process Pre-Medical (Enhancement)";
	public static final String PROCESS_PED_QUERY = "Process PED Query";
	public static final String WITHDRAW_PRE_AUTH = "Withdraw Pre-Auth";
	public static final String DOWNSIZE_PRE_AUTH = "DownSize Pre-Auth";  
	public static final String DOWNSIZE_PRE_AUTH_REQUEST = "Process Downsize Request";
	public static final String VISIT_FIELD = "visit field";
	public static final String SELECT_HOSPITAL = "select hospital";
	public static final String FIELD_VISIT_REPESENTATION = "Field Visit Representation_removed";
	public static final String REASSIGN_FIELD_VISIT_REPESENTATION = "Re Assign Field Visit Representation_removed";
	public static final String CPU_SKIP_ZMR = "CPU Skip ZMR";
	public static final String WITHDRAW_PREAUTH_WIZARD = "Withdraw Pre-Auth Wizard";
	public static final String WITHDRAW_PRE_AUTH_POST_PROCESS = "Withdraw Pre-Auth(Post Process)";

	public static final String PROCESS_PED_REQUEST_PROCESS = "Process PED Request (Processor) _Disabled";
	public static final String UPLOAD_FILE_TO_DMS = "upload_file_to_dms";
	public static final String PROCESS_PED_REQUEST_APPROVE = "Process PED Request (Approver) Team Lead";
	 public static final String PROCESS_PED_QUERY_REQUEST = "Process PED Query Request";
	public static final String PROCESS_PED_REQUEST_TL_APPROVE = "Process PED Request (Approver) Team Lead_old_removed";
	public static final String PED_INITIATE = "PED Initiate";
	public static final String UPLOAD_TRANSLATED_DOCUMENTS = "Upload Translated Documents";
	public static final String ADVISE_ON_PED = "Advise on PED";
	public static final String SUBMIT_SPECIALLIST_ADVISE = "Submit Specialist Advise";
	public static final String SEARCH_CLAIM = "Search Claim";
	public static final String OP_HEALTH_CHECK_PROCESSING = "OP & Health Check Processing";
	public static final String EXPIRED_POLICIES_CLAIM_OP = "Expired Policies_Claim_op";
	public static final String REGISTER_CLAIM_OP = "Register Claim_op";
	public static final String PORCESS_CLAIM_OP = "Process Claim_op";
	public static final String PAYMENT_PROCESS_OP = "Payment Process_op";
	public static final String CREATE_BATCH_OP = "Create Batch_op";
	public static final String CREATE_SETTLEMENT_BATCH_OP = "Create Settlement Batch_op";
	
	

	public static final String ENDORSEMENTS_HEADER = "ENDORSEMENTS_HEADER";
	public static final String PROCESS_CLAIM_HEADER = "PROCESS_CLAIM_HEADER";
	public static final String SPECIALIST_PROCESSING_HEADER = "SPECIALIST_PROCESSING_HEADER";
	public static final String SHOW_HOSPITAL_ACKNOWLEDGE_FORM = "show_hospital_acknowledge_form";
	// public static final String TEST_VIEW = "show_ped_request_view";
	// For Reimbursement
	public static final String INVESTIGATION = "INVESTIGATION";
	
	public static final String INVESTIGATION_MASTER = "INVESTIGATION_MASTER";
	public static final String QUERYREJECTION = "QUERYREJECTION";
	public static final String ProcessDraftQueryLetter = "Process Draft Query Letter";
	public static final String GenerateReminderLetter = "Generate Reminder Letter";
	public static final String ProcessDraftRejectionLetter = "Process Draft Rejection Letter";
	public static final String PROCESS_FIELD_VISIT = "PROCESS FIELD VISIT";
	public static final String SEARCH_PROCESS_INVESTIGATION_INITIATED = "SEARCH_PROCESS_INVESTIGATION_INITIATED";
	public static final String ACKNOWLEGE_INVESTIGATION_COMPLETED = "Acknowlege Investigation Completed";
	public static final String SEARCH_INVESTIGATION_GRADING = "SEARCH_INVESTIGATION_GRADING";

	// Added for star fax simulation .
	public static final String INVESTIGATION_DIRECT_ASSIGNEMENT = "INVESTIGATION_DIRECT_ASSIGNEMENT";
	public static final String STARFAX_SIMULATION = "STARFAX_SIMULATION_removed";
	public static final String SHOW_POLICY_DETAILS = "SHOW_POLICY_DETAILS";
	public static final String SHOW_EARLIER_ROD_DETAILS = "View earlier ROD Details";
	public static final String ACKNOWLEDGE_DOCUMENT_RECEIVED = "ACKNOWLEDGE_DOCUMENT_RECEIVED";
	// R3
	public static final String FIELD_VISIT_CASHLESS = "Field Visit Cashless_removed";
	public static final String FIELD_VISIT_REIMBURSEMENT = "Field Visit Reimbusrsement_removed";
	public static final String SHOW_PROCESS_FIELD_VISIT = "process field visit_removed";
	public static final String SHOW_INVESTIGATION = "Investigation";
	public static final String ACKNOWLEDGE_INVESTIGATION_COMPLETED = "Acknowledge Investigation Completed_removed";
	public static final String SHOW_ASSIGN_INVESTIGATION = "Assign Investigation for Reibursement Search";
	public static final String SHOW_RE_ASSIGN_INVESTIGATION = "ReAssign Investigation Search";
	public static final String SHOW_DRAFT_INVESTIGATION = "Draft Investigation for Reibursement Search";
	public static final String SEARCH_ACKNOWLEDGE_INVESTIGATION_COMPLETED = "Acknowledge Investigation Completed_removed";

	public static final String ROD = "rdo";
	public static final String SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED = "show acknowledgement document received";
	public static final String ADD_ADDITIONAL_DOCUMENT_PAYMENT_INFORMATION = "Add Additional Document Payment Information";//R1069
	public static final String CREATE_ROD = "create dod";
	public static final String UPLOAD_NEFT_DETAILS = "Upload NEFT Details";
	public static final String ENTER_BILL_DETAILS = "Enter Bill Details (Bill Entry)";
	public static final String UPLOAD_INVESTIGATION_REPORT = "Upload Investigation Report";
	public static final String QUERY_REJECTION = "Query/Rejection";
	public static final String DRAFT_QUERY_LETTER_SEARCH = "Draft Query Letter Search";
	public static final String DRAFT_QUERY_LETTER_DETAIL = "Draft Query Letter Detail";
	public static final String PROCESS_DRAFT_QUERY_LETTER_DETAIL = "Process Draft Query Letter Detail";
	public static final String PROCESS_DRAFT_QUERY_LETTER = "Process Draft Query Letter";
	public static final String PROCESS_DRAFT_QUERY_LETTER_WIZARD = "Process Draft Query Letter Wizard";
	public static final String PA_QUERY_REJECTION = "PA Query/Rejection";
	public static final String PA_DRAFT_QUERY_LETTER_SEARCH = "PA Draft Query Letter Search";
	public static final String PA_DRAFT_QUERY_LETTER_DETAIL = "PA Draft Query Letter Detail";
	public static final String PA_DRAFT_QUERY_LETTER = "PA Draft Query Letter";
	public static final String PA_PROCESS_DRAFT_QUERY_LETTER = "PA Process Draft Query Letter";
	public static final String PA_PROCESS_DRAFT_QUERY_LETTER_DETAIL = "PA Process Draft Query Letter Detail";
	// public static final String PA_PROCESS_DRAFT_QUERY_LETTER_WIZARD =
	// "PA Process Draft Query Letter Wizard";
	// public static final String GENERATE_REMINDER_LETTER_CLAIMWISE =
	// "Generate Reminder Letter Claimwise";
	public static final String GENERATE_REMINDER_LETTER_DETAIL = "Generate Reminder Letter Detail";
	public static final String DRAFT_REJECTION_LETTER = "Draft Rejection Letter";
	public static final String DRAFT_REJECTION_LETTER_DETAIL = "Draft Rejection Letter Detail";
	public static final String PROCESS_DRAFT_REJECTION_LETTER = "Process Draft Rejection Letter";
	public static final String DRAFT_REJECTION_LETTER_WIZARD = "Process Draft Rection Letter Wizard";
	public static final String PA_DRAFT_REJECTION_LETTER = "PA Draft Rejection Letter";
	public static final String PA_DRAFT_REJECTION_LETTER_DETAIL = "PA Draft Rejection Letter Detail";
	public static final String PA_PROCESS_DRAFT_REJECTION_LETTER = "PA Process Draft Rejection Letter";
	public static final String PA_DRAFT_REJECTION_LETTER_WIZARD = "PA Process Draft Rection Letter Wizard";
	public static final String SPECIALIST_PROCESSING = "Specialist Processing";
	public static final String SUBMIT_SPECIALIST_ADVISE = "Submit Specialist Advise1";
	public static final String MEDICAL_APPROVAL = "Medical Approval";
	public static final String PROCESS_CLAIM_REQUEST_ZONAL_REVIEW = "Process Claim Request(Zonal Review)";
	public static final String PROCESS_CLAIM_REQUEST = "Process Claim Request";
	public static final String PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT = "Wait For Input";
	public static final String PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION = "Process Claim Request(Auto Allocation)";
	public static final String PROCESS_CLAIM_REQUEST_BENEFITSBILLING = "Process Claim Request (Benefits) bill";
	public static final String BILLING = "Billing";
	public static final String PROCESS_CLAIM_BILLING = "Process Claim Billing";
	public static final String FINANCIAL_APPROVAL = "Financial Approval";
	public static final String PROCESS_CLAIM_REQUEST_BENEFITSFINANCIAL = "Process Claim Request (Benefits) finance";
	public static final String PROCESS_CLAIM_FINANCIALS = "Process Claim Financials";
	public static final String PROCESS_CLAIM_COMMON_BILLING_FINANCIALS = "ProcessCommonBillingAndFA";
	public static final String PROCESS_CLAIM_COMMON_BILLING_FINANCIALS_AUTO_ALLOCATION = "CommonBillingAndFAAutoAllocation";
	public static final String FINANCIALS_APPROVAL_AUTO_ALLOCATION = "FinancialApprovalAutoAllocation";
	public static final String MANAGE_CLAIMS = "Manage Claims";
	public static final String SEARCH_INTERNAL_NOTES = "Update Internal Notes";
	public static final String SEARCH_INTERNAL_NOTES_PA = "Update Internal Notes PA";
	// public static final String RE_OPEN_CLAIM_ROD_LEVEL =
	// "Re-Open Claim(Search Based)";
	public static final String RE_OPEN_CLAIM_ROD_LEVEL = "Re-Open Claim - ROD Level (Search Based)";
	public static final String RE_OPEN_CLAIM_CLAIM_LEVEL = "Re-Open Claim - Claim Level (Search Based)";
	// public static final String CLOSE_CLAIM_ROD_LEVEL =
	// " Close Claim(Search Based)";
	public static final String CLOSE_CLAIM_ROD_LEVEL = "Close Claim - ROD Level (Search Based)";
	public static final String CLOSE_CLAIM_CLAIM_LEVEL = "Close Claim - Claim Level (Search Based)";
	public static final String ALLOW_RECONSIDERATION = "Allow Reconsideration";
	// public static final String CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL =
	// "Close Claim(In Process)";
	public static final String CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL = "Close Claim - ROD Level (In Process)_removed";
	public static final String QUERYREJECTIONMENU = "query rejection menu";
	public static final String PA_QUERYREJECTIONMENU = "PA Query rejection menu";
	public static final String ADD_ADDITIONAL_DOC = "Add Additional Documents";
	public static final String REWARD_RECOGNITION_CELL = "Reward & Recognition Cell";
	public static final String PROCESS_RRC_REQUEST = "Process RRC Request";
	public static final String REVIEW_RRC_REQUEST = "Review RRC Request";
	public static final String MODIFY_RRC_REQUEST = "Modify_RRC_Request";
	public static final String SEARCH_RRC_REQUEST = "Search_RRC_request";
	
	public static final String  TALK_TALK_TALK= "TALK TALK TALK";
	public static final String INITIATE_TALK_TALK_TALK = "Initiate TALK TALK TALK";
	public static final String INITIATE_TALK_TALK_TALK_WIZARD = "show_initiate_talk_talk_talk_wizard";
	
	//Dinesh
	public static final String RRC_STATUS_SCREEN = "RRC_Status_Screen";
	
	public static final String SPECIAL_APPROVALS = "special approver";
	public static final String APPROVE_CLAIM = "approver_claim";
	public static final String PROCESS_CLAIM = "reffered for special approve";
	public static final String CLEAR_CASHLESS = "clear_cashless";
	public static final String SETTLEMENT_PULL_BACK = "settlement_pull_back_search";
	public static final String LOT_PULL_BACK = "lot_pull_back_search";
	public static final String CLAIM_WISE_ALLOW_APPROVAL = "Claim Wise Allow Approval";
	public static final String HOLD_MONITOR_SCREEN = "Hold_Monitoring";
	public static final String HOLD_MONITOR_SCREEN_FOR_MA = "Hold_Monitoring For MA";
	public static final String HOLD_MONITOR_FLP_SCREEN = "Hold_Monitoring_FLP";
	public static final String UPDATE_ROD_DETAILS = "Update Rod Details";
	public static final String HOLD_MONITOR_SCREEN_FOR_FA_AUTO = "Hold_Monitoring For FA";
	public static final String INITIATE_RRC_REQUEST = "Initiate RRC Request";
	public static final String INITIATE_RRC_REQUEST_WIZARD = "show_initiate_rrc_wizard";
	public static final String LEGAL_SCREENS = "legal_screens";
	public static final String PHYSICAL_DOCUMENT = "Received Physical Documents (Maker)";
	public static final String PHYSICAL_DOCUMENT_CHECKER = "Received Physical Documents (Checker)";
	
	public static final String RESEARCH_ANALYSIS_WING_PROCESSING = "Research Analysis Wing Processing";
	public static final String PROCESS_RAW_REQUEST = "Process Raw Request";
	
	//PCC Processing
	public static final String POST_CASHLESS_CELL = "POST_CASHLESS_CELL";
	public static final String PCC_CO_ORDINATOR = "PCC_CO_ORDINATOR";
	public static final String PCC_PROCESSOR = "PCC_PROCESSOR";
	public static final String PCC_REVIEWER = "PCC_REVIEWER";
	public static final String DIVISION_HEAD = "DIVISION_HEAD";
	public static final String ZONAL_MEDICAL_HEAD_PCC_PROCESS = "ZONAL_MEDICAL_HEAD_PCC_PROCESS";
	public static final String ZONAL_CO_ORDINATOR = "ZONAL_CO_ORDINATOR";
	public static final String HRM_CO_ORDINATOR = "HRM_CO_ORDINATOR";
	public static final String REASSIGN_HRM = "Re-Assign HRM";
	
	
	public static final String PROCESS_NEGOTIATION = "PROCESS_NEGOTIATION";
	public static final String CLAIMS_ALERT_MASTER_SCREEN = "Claims Alert";
	public static final String PROCESS_FLP_AUTO_ALLOCATION = "FLP Auto Allocation";
	public static final String MONITORING_REPORT = "monitoring_report";	
	public static final String DATA_CODING_DATA_CORRECTION = "DATA_CODING_DATA_CORRECTION";
	public static final String PROCESS_DATA_CORRECTION_VIEW = "process_data_correction_view";
	public static final String DATA_CODING_DATA_CORRECTION_HISTORICAL = "DATA_CODING_DATA_CORRECTION_HISTORICAL";
	public static final String PROCESS_DATA_CORRECTION_HISTORICAL_VIEW = "process_data_correction_historical_view";
	public static final String HRMP = "HRMP";
	public static final String CREATE_ONLINE_ROD = "create_online_rod";
	public static final String DATA_CODING_DATA_CORRECTION_PRIORITY = "DATA_CODING_DATA_CORRECTION_PRIORITY";
	public static final String PROCESS_DATA_CORRECTION_PRIORITY_VIEW = "process_data_correction_priority_view";
	public static final String PROCESS_CLAIM_BILLING_AUTO_ALLOCATION = "process_claim_billing_auto_allocation";
	public static final String HOLD_MONITOR_SCREEN_FOR_BILLING = "hold_monitoring_for_billing";
	public static final String SEARCH_DISPATCH_DETAILS_REPORT = "DISPATCH_DETAILS_REPORT";
	public static final String FRAUD_ANALYSIS_REPORT = "VIEW_FRAUD_ANALYSIS_REPORT";
	
//___________________________________PA CLAIMS MENU_________________________________________
	
	public static final String ROD_PA_CLAIMS = "pa_rod";
	public static final String PA_SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED = "pa_acknowledgement document received";
	public static final String PA_ADD_ADDITIONAL_DOCUMENTS = "pa_add_additional_documents";
	public static final String PA_UPLOAD_INVESTIGATION_REPORT = "PA Upload Investigation Report";
	public static final String PA_SEARCH_OR_UPLOAD_DOCUMENTS = "PA Search Upoad Documents";
	public static final String PA_SEARCH_OR_UPLOAD_DOCUMENTS_FOR_ACK_NOT_RECEIVED = "PA_SEARCH_OR_UPLOAD_DOCUMENTS_FOR_ACK_NOT_RECEIVED";
	public static final String PA_ADD_ADDITIONAL_DOCUMENT_PAYMENT_INFORMATION = "PA Add Additional Document Payment Information";//R1069
	public static final String PA_CREATE_ROD = "pa_create_rod";
	public static final String PA_ENTER_BILL_DETAILS = "pa_Enter_Bill_Details";

	public static final String PA_MANAGE_CLAIMS = "pa_Manage_claims";
	public static final String PA_RE_OPEN_CLAIM_ROD_LEVEL = "PA Re-Open Claim - ROD Level (Search Based)";
	public static final String PA_CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL = "PA Close Claim - ROD Level (In Process)";
	public static final String PA_CLOSE_CLAIM_ROD_LEVEL = "PA Close Claim - ROD Level (Search Based)";
	public static final String PA_CLOSE_CLAIM_CLAIM_LEVEL = "PA Close Claim - Claim Level (Search Based)";
	public static final String PA_RE_OPEN_CLAIM_CLAIM_LEVEL = "PA Re-Open Claim - Claim Level (Search Based)";
	public static final String PA_CANCEL_ACKNOWLEDGEMENT = "PA_CANCEL_ACKNOWLEDGEMENT";
	public static final String PA_SETTLEMENT_PULL_BACK = "PA settlement_pull_back_search";
	public static final String PA_CPU_SKIP_ZMR = "PA CPU Skip ZMR";
	public static final String PA_UNLOCK_INTIMATION = "PA unlock_intimation";
	public static final String GPA_UNNAMED_RISK_DETAILS = "GPA Unnamed Risk Details";
	public static final String GPA_UNNAMED_RISK_DETAILS_PAGE = "GPA Unnamed Risk Details Page";

	public static final String PA_HOSP_RE_OPEN_CLAIM_ROD_LEVEL = "PA Hosp Re-Open Claim - ROD Level (Search Based)";
	public static final String PA_HOSP_CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL = "PA Hosp Close Claim - ROD Level (In Process)";
	public static final String PA_HOSP_CLOSE_CLAIM_ROD_LEVEL = "PA Hosp Close Claim - ROD Level (Search Based)";
	public static final String PA_HOSP_CLOSE_CLAIM_CLAIM_LEVEL = "PA Hosp Close Claim - Claim Level (Search Based)";
	public static final String PA_HOSP_RE_OPEN_CLAIM_CLAIM_LEVEL = "PA Hosp Re-Open Claim - Claim Level (Search Based)";
	public static final String PA_HOSP_SETTLEMENT_PULL_BACK = "PA Hosp settlement_pull_back_search";

	// OMP
	public static final String OMP_SEARCHINTIMATION_CREATE = "Search & Create Intimation";
	public static final String OMP_INTIMATION_REG = "Registration";
	public static final String OMP_ACKNOWLEDGEMENT_DOCUMENTS_RECEIVED = "Acknowledgement Documents Received";
	public static final String OMP_MAIN_MENUBAR = "omp menubar";
	public static final String OMP_PROCESS_OMP_CLAIM_PROCESSOR = "Process OMP Claim -Processor";
	public static final String OMP_PROCESS_OMP_CLAIM_APPROVER = "Process OMP Claim - Approver";
	public static final String OMP_CLAIMS_RATE_CHANGE_AND_OUTSTANDING_UPDATION = "Omp Claims Rate Change And Outstanding Updation";
	public static final String SEARCH_OMP_OUTSTANDING_REPORT = "OMP_Outstanding_Report";
	public static final String SEARCH_OMP_STATUS_WISE_REPORT = "OMP_Status_Report";
	//CR20181332 new menu for bulk upload rejection 
	public static final String OMP_BULK_UPLOAD_REJECTION = "Bulk upload rejection";
	
	// Medical Opinion Validation
	public static final String MEDICAL_ACTIVITIES = "MEDICAL_ACTIVITIES";
	public static final String OPINION_VALIDATION = "OPINION_VALIDATION";
	public static final String OPINION_VALIDATION_REPORT = "OPINION_VALIDATION_REPORT";
	
	//GLX2020135
	public static final String MARKETING_ESCALATION_REPORT = "MARKETING_ESCALATION_REPORT";
	
	//Managers FeedBack Screen
	public static final String CLM_BR_MGR_FEEDBACK = "CLM_BR_MGR_FEEDBACK";
	public static final String CLM_BR_MGR_FEEDBACK_RPT = "CLM_BR_MGR_FEEDBACK_RPT";
	public static final String CLM_BR_MGR_REPLY_FEEDBACK = "CLM_BR_MGR_REPLY_FEEDBACK";
	
	//CVC
	public static final String CVC_PROCESSING = "CVC_PROCESSING";
	public static final String CVC_AUDIT = "CVC_AUDIT";
	public static final String POST_PROCESS_CVC = "POST_PROCESS_CVC";
	public static final String CVC_AUDIT_ACTION_PROCESSING = "CVC_AUDIT_ACTION_PROCESSING";
	public static final String CVC_AUDIT_QRY_APPRVOAL = "CVC_AUDIT_QUERY_APPRVOAL";
	public static final String CVC_AUDIT_CLS_QRY_RLY = "CVC_AUDIT_CLS_QUERY_RLY";
	public static final String CVC_AUDIT_RMB_QRY_RLY = "CVC_AUDIT_RMB_QUERY_RLY";
	public static final String CVC_AUDIT_FA_QRY_RLY = "CVC_AUDIT_FA_QUERY_RLY";

	public static final String CVC_AUDIT_REPORT = "CVC_AUDIT_REPORT";
	
	public static final String HRM_ACCESS = "HRM_ACCESS";

	public static BPMClientContext bpmClientContext = new BPMClientContext();
	// Allocate Corp Buffer
	public static final String ALLOCATE_CORP_BUFFER = "ALLOCATE_CORP_BUFFER";

	private static final Map<String, MenuItem> MENU_ITEMS_PERMISSION_MAPPING = new HashMap<String, MenuItem>();

	private static final Map<String, List<String>> MENU_ASSOCIATED_WITH_ROLE = new HashMap<String, List<String>>();

	private static MenuItem rootMenu = new MenuItem(0l, "My Task", HOME_PAGE);

	private static MenuItem intimationMenu = new MenuItem(1l, "Intimations",
			INTIMATION_HEADER);

	private static MenuItem zroMenu = new MenuItem(0l, "New Intimation",
			NEW_INTIMATION);

	public static final String PAYMENT_PROCESSOR = "payment processor";
	public static final String CREATE_OR_SEARCH_LOT = "Create / Search Lot (Maker)";

	public static final String CREATE_BATCH = "Create Batch (Checker)";
	//bancs-payment verification
	public static final String PAYMENT_VERIFICATION_LVL1 = "Payment Verification Level I";
	public static final String PAYMENT_VERIFICATION_LVL2 = "Payment Verification Level II";
	
	public static final String PROCESS_PAYMENT_RETURNS = "Process Payment Returns";
	public static final String PAYMENT_REPROCESS = "Payment Re-Process";
	// public static final String UPDATE_PAYMENT_DETAILS =
	// "UPDATE_PAYMENT_DETAILS";
	public static final String PAYMENT_INITIATE_RECOVERY = "Payment Initiate Recovery";

	//GLX2020084
	//public static final String STOP_PAYMENT_TRACKING = "Stop Payment Tracking";
	public static final String STOP_PAYMENT_REQUREST = "Stop Payment Request";
	public static final String STOP_PAYMENT_VALIDATION = "Stop Payment Validation";
	
	public static final String INTIMATION_VIEW_UPR_DETAILS = "INTIMATION_VIEW_UPR_DETAILS";

	public static final String MISC = "misc";
	public static final String REGISTER_CLAIM_REFUND = "Register Claim Refund";
	public static final String PROCESS_CLAIM_REFUND = "process Claim Refund";
	public static final String USER_ACCESS_ALLOCATION = "User Access (Auto Allocation)";
	//user Management Nov 13 2018
	public static final String USER_MANAGEMENT = "user_management";
	public static final String USER_RE_ALLOCATION = "User Re-Allocation";
	public static final String PINCODE_CLASS_MAPPING = "PINCODE_CLASS_MAPPING";
	public static final String CPU_AUTO_ALLOCATION = "Cpu Auto-Allocation";
	public static final String UPDATE_SUBLIMIT = "Update Sublimit";
	public static final String TOP_UP_POLICY_MASTER_SCREEN = "Top Up Policy Master";
	public static final String FRAUD_IDENTIFICATION = "Fraud Identification";
	public static final String GMC_AUTOMAILER = "GMC Automailer";
	
	public static final String PROCESS_CLAIM_HEADERR3="PROCESS_CLAIM_HEADERR3_removed";
	public static final String UPLOAD_TRANSLATED_DOCUMENTSR3 = "Upload Translated DocumentsR3_removed";	

	public static final String MASTER = "master";
	public static final String ICD_SUBLIMIT_MAPPING = "Icd_Sublimit_Mapping";

	public static final String VIEW_DETAILS = "view Detail";

	public static final String LEGAL_HOME = "legal_home";
	public static final String CONSUMER_FORUM = "consumer_forum";
	public static final String ADVOCATE_NOTICE = "advocate_notice";
	public static final String ADVOCATE_FEE = "advocate_fee";
	public static final String OMBUDSMAN = "ombudsman";

	public static final String WAR_HOUSE = "war_house";
	public static final String FILE_DETAILS_REPORT = "FILE_DETAILS_REPORT";
	public static final String PRODUCTIVITY_REPORT = "PRODUCTIVITY_REPORT";
	public static final String PREAUTH_FORM_DOCUMENT_REPORT = "PREAUTH_FORM_DOCUMENT_REPORT";
	public static final String NOT_ADHERING_TO_ANH_REPORT = "NOT_ADHERING_TO_ANH_REPORT"; //CR R1257
	public static final String INV_ASSIGN_STATUS_REPORT = "InvAssignStatusReport";
	public static final String FA_APPROVED_SETTLEMENT_PENDING_REPORT = "FAapprovedSettlementPendingReport";  //CR R1201  FA approved not settled Report
	
	public static final String BRANCH_MANAGER_FEEDBACK_REPORT = "BRANCH_MANAGER_FEEDBACK_REPORT";  // CR R1238 Branch Manager Feedback Report
	
	public static final String BRANCH_MANAGER_FEEDBACK_REPORTING_PATTERN = "BranchManagerFeedbackReportingPattern";  // CR R1238 Branch Manager Feedback Reporting Pattern
	
	public static final String NEGOTIATION_REPORT = "VIEW_NEGOTIATION_REPORT";
	public static final String UPDATE_NEGOTIATION = "UPDATE_NEGOTIATION";
	
	//CANCEL REPORT 
	public static final String AUTO_ALLOCATION_CANCEL_REPORT = "CancelStatusReport";

	public static final String UPDATE_PAN_CARD = "UPDATE_PAN_CARD";
	public static final String UPDATE_AADHAR_DETAILS = "UPDATE_AADHAR_DETAILS";
	public static final String MANAGER_FEEDBACK_FORM = "MANAGER_FEEDBACK_FORM";
	public static final String MANAGER_PREVIOUS_FEEDBACK = "MANAGER_PREVIOUS_FEED_BACK_FORM";
	//public static final String MANAGER_FEEDBACK_FORM = "Branch Manager Feedback Home Page";
	
	public static final String UPLOAD_BED_PHOTO = "UPLOAD_BED_PHOTO";
	
	//lumen
	public static final String LUMEN_HEADER = "Lumen";
	public static final String SEARCH_LUMEN = "SearchLumen";
	public static final String CREATE_LUMEN = "CreateLumen";
	public static final String LUMEN_REQUEST_LEVEL_I = "SearchLumenRequestLevel-1";
	public static final String LUMEN_REQUEST_CO_ORDINATOR = "SearchLumenCoOrdinatorRequest";
	public static final String LUMEN_REQUEST_LEVEL_II = "SearchLumenRequestLevel-2";
	public static final String LUMEN_REQUEST_MIS = "LumenRequestMIS";
	public static final String LUMEN_REQUEST_INITIATOR = "LumenRequestInitiator";
	public static final String LUMEN_STATUS_WISE_REPORT = "LumenStatusWiseReport";

	public static final String INITIATE_REQUEST = "InitiateRequest";
	public static final String INITIATE_POLICY_REQUEST = "InitiatePolicyRequest";
	public static final String LEVEL_I_WIZARD = "Level_1_Wizard";
	public static final String INITIATOR_WIZARD = "Initiator_Wizard";
	public static final String COORDINATOR_WIZARD = "Coordinator_Wizard";
	public static final String LEVEL_II_WIZARD = "Level_2_Wizard";
	public static final String QUERY_MIS_WIZARD = "Query_MIS_Wizard";

	public static final String FVR_PROCESSING = "FVR_PROCESSING";
	public static final String FVR_GRADING = "FVR_GRADING";
	
	public static final String UNCHECK_NEGOTIATION = "UNCHECK_NEGOTIATION";

	public static final String MANAGER_FEEDBACK = "MANAGER_FEEDBACK";	

	//New additon for hospital scoring details
		public static final String HOSPITAL_SCORING_DETAILS = "HOSPITAL_SCORING_DETAILS";
	
	public static final String FLAG_RECONSIDERATION_REQUEST = "Flag_Reconsideration_Request";
	
	public static final String AUDIT_INTIMATION_UNLOCK = "unlock_intimation_audit";
	
	public static final String RECORD_MARKETING_ESCALATION = "RECORD_MARKETING_ESCALATION";

	public static final String PROVISION_HISTORY="Provision_History";
	private static MenuItem healthMenu = new MenuItem(0l, "Health Claim Processing", HEALTH_PAGE);
	
	private static MenuItem cashLessMenu = new MenuItem(0l, "CashLess", CASHLESS_PAGE);
	
	private static MenuItem rembursementMenu = new MenuItem(0l, "Reimbursement Processing", REIMBURSEMENT_PAGE);
	private static MenuItem QueryRejectionMenu = new MenuItem(0l, "Intimations", QUERYREJECTIONMENU);
	private MenuItem masterMenu = new MenuItem(0l, "Master", MASTER);
	private static MenuItem PAQueryRejectionMenu = new MenuItem(0l, "Intimations", PA_QUERYREJECTIONMENU);
	
	private static MenuItem searchClaim = new MenuItem(0l, "Search Claim", SEARCH_CLAIM);
	private static MenuItem legalmenu = new MenuItem(0l, "Legal Processing", LEGAL_SCREENS);
	private static MenuItem reportHeader = new MenuItem(0l, "Reports", REPORTS_HEADER);
	private static MenuItem reportMenu = new MenuItem(0l, "Health - Reports", HEALTH_REPORT);
	private static MenuItem paClaimMenu = new MenuItem(0l, "PA Claims Processing", PA_CLAIMS);
	private static MenuItem paClaimRegisterMenu = new MenuItem(1l,"Registration Processing",PA_CLAIM_REGISTER);
	private static MenuItem paymentProcessCpu = new MenuItem(1l,"Payment Process CPU", PAYMENT_PROCESS_CPU);
	private static MenuItem processVBcomplaince = new MenuItem(1l,"Process 64VB Compliance", PROCESS_64_VB_COMPLIANCE);
	private static MenuItem skipZMR = new MenuItem(2l,"Master Screen-Skip ZMR", CPU_SKIP_ZMR);
	
//	private static MenuItem searchOrUploadDocuments = new MenuItem(11l,"Search / Upload Documents",SEARCH_OR_UPLOAD_DOCUMENTS);
	private static MenuItem opScreen = new MenuItem(0l,"OP Screen",OP_SCREEN);
	private static MenuItem processingPayment = new MenuItem(0l,"Processing Payment",PROCESSING_PAYMENT);
	
	
	private static MenuItem warHouseMenu = new MenuItem(0l, "File Storage", WAR_HOUSE);
	private static MenuItem fileDetailsReport = new MenuItem(0l,"File Details Report", FILE_DETAILS_REPORT);
	private static MenuItem updatePanCard = new MenuItem(1l,"Update Pan Details", UPDATE_PAN_CARD);
	private static MenuItem updateAadharDetails = new MenuItem(1l,"Update Aadhar Details", UPDATE_AADHAR_DETAILS);	
	
	private static MenuItem uploadBedPhoto = new MenuItem(1l,"Upload Bed Photo", UPLOAD_BED_PHOTO);
	public static final String SEARCH_OPINION_VALIDATION = "SEARCH_OPINION_VALIDATION";
	private static MenuItem monitoringMenu = new MenuItem(1l, "Cashless Operational Monitoring", MONITORING_REPORT);
	

	public static final String ICAC_MENU = "ICAC_MENU";
	public static final String PROCESS_ICAC_REQUEST = "PROCESS_ICAC_REQUEST";

	static {

		MenuItem policywiseClaimSearch = new MenuItem(1l,
				"Search Claim Policy wise", SEARCH_CLAIM_POLICY_REPORT);
		MenuItem executiveStatusReport = new MenuItem(2l,
				"Executive Status Report (Detail)", EXECUTIVE_STATUS_REPORT);
		MenuItem executiveStatusSummaryReport = new MenuItem(3l,
				"Executive Status Report (Summary)",
				EXECUTIVE_STATUS_SUMMARY_REPORT);
		MenuItem preauthCpuWiseReport = new MenuItem(4l,
				"Pre Auth CPU wise Report", SEARCH_PREAUTH_CPUWISE_REPORT);
		MenuItem claimStatusCpuwiseReport = new MenuItem(5l,
				"Claims Status Report New", CLAIM_STATUS_CPUWISE_REPORT);
		MenuItem helpDeskStatusReport = new MenuItem(6l,
				"Help Desk Status Report", HELP_DESK_STATUS_REPORT);
		MenuItem claimsDailyReport = new MenuItem(7l, "Claims Daily Report",
				CLAIMS_DAILY_REPORT);
		MenuItem hospitalwiseReport = new MenuItem(8l, "Hospitalwise Report",
				HOSPITAL_WISE_REPORT);
		MenuItem callcenterDashBoard = new MenuItem(9l, "Callcenter DashBoard",
				CALL_CENTER_DASH_BOARD);
		MenuItem automationDashBoard = new MenuItem(9l, "Automation DashBoard",
				AUTOMATION_DASHBOARD);
		MenuItem paymentPendingDashBoard = new MenuItem(9l, "Payment Pending DashBoard",
				PAYMENT_PENDING_DASHBOARD);
		MenuItem billReceivedStatusReport = new MenuItem(10l,
				"Bill Received Status Report", BILL_RECIECVED_STATUS_REPORT);
		MenuItem administrativeDashBoard = new MenuItem(11l,
				"Administrative DashBoard", ADMINISTRATIVE_DASH_BOARD);
		MenuItem paymentProcessReport = new MenuItem(12l,
				"Payment Process Report", PAYMENT_PROCESS_REPORT);
		MenuItem fvrAssignmentReport = new MenuItem(13l,
				"FVR Assignment Report", FVR_ASSIGNMENT_REPORT);

		MenuItem hospitalIntimationStatusReport = new MenuItem(14l,
				"Hospital Intimation Status", HOSPITAL_INTIMATION_STATUS);
		MenuItem medicalMailStatus = new MenuItem(15l, "Medical Mail Status",
				MEDICAL_MAIL_STATUS);
		MenuItem dailyReport = new MenuItem(16l, "Daily Report", DAILY_REPORT);
		MenuItem cpuWiseReport = new MenuItem(17l,
				"Cpu Wise Performance Report", CPU_WISE_PERFORMANCE_REPORT);
		MenuItem agentBrokerReport = new MenuItem(18l, "Agent Broker Report",
				AGENT_BROKER_REPORT);
		MenuItem opclaimReport = new MenuItem(19l, "OP claim Report",
				OP_CLAIM_REPORT);
		MenuItem tatReport = new MenuItem(19l, "TAT Report", TAT_REPORT);
		MenuItem ackWithoutRodReport = new MenuItem(30l,
				"Ack Created ROD Not Created", ACK_WITHOUT_ROD_REPORT); // R1097
																		// ACK
																		// WITHOUT
																		// ROD
																		// 06-03-2018
		MenuItem provisionReport = new MenuItem(19l, "Revision of Provision",
				REVISED_PROVISION);
		MenuItem autoClosureBatch = new MenuItem(20l, "Auto Closure",
				AUTO_CLOSURE_BATCH);
		// MenuItem createSearchLot = new
		// MenuItem(19l,"Create Or search Lot",CREATE_LOT_MAKER);
		MenuItem paymentBatchReport = new MenuItem(19l, "Payment Batch Report",
				PAYMENT_BATCH_REPORT);
		MenuItem plannedAdmissionReport = new MenuItem(20l,
				"Planned Admission Report", PLANNED_ADMISSION_REPORT);
		MenuItem intimatedRiskDetailsReport = new MenuItem(21l,
				"Intimated Risk Details Report", INTIMATED_RISK_DETAILS_REPORT);
		MenuItem medicalAuditClaimStatusReport = new MenuItem(22l,
				"Medical Audit Claim Status Report",
				MEDICAL_AUDIT_CLAIM_STATUS_REPORT);
		MenuItem intimationAlternateCPUwiseReport = new MenuItem(23l,
				"Intimation Alternate CPU wise Report",
				INTIMATION_ALTERNATE_CPU_REPORT);
		MenuItem medicalAuditCashlessIssuanceReport = new MenuItem(24l,
				"Medical Audit Cashless Issuance Report",
				MEDICAL_AUDIT_CASHLESS_ISSUANCE_REPORT);
		MenuItem productivityReport = new MenuItem(25l, "Productivity Report",
				PRODUCTIVITY_REPORT);
		MenuItem preauthFormDocReport = new MenuItem(26l,
				"Preauth Form Without Supporting Documents",
				PREAUTH_FORM_DOCUMENT_REPORT);
		
		MenuItem notAdheringToANHReport = new MenuItem(26l,"Not Adhering to ANH-Report", NOT_ADHERING_TO_ANH_REPORT);
		MenuItem invAssignReport = new MenuItem(28l,
				"Investigation Assign Report", INV_ASSIGN_STATUS_REPORT);
		MenuItem cancelReport = new MenuItem(29l,
				"Auto Allocation Cancel Report", AUTO_ALLOCATION_CANCEL_REPORT);
		
		MenuItem gmc_daily_report = new MenuItem(27l, "Gmc daily report",
				GMC_DAILY_REPORT);
		MenuItem faApprovedSettlementPendingReport = new MenuItem(130l,"FA Approved and not settled Report", FA_APPROVED_SETTLEMENT_PENDING_REPORT); //CR R1201  FA approved not settled Report
		MenuItem provisionhistory = new MenuItem(1302L, "Provision History",
				PROVISION_HISTORY);
		MenuItem negotiationReport = new MenuItem(136l,"Negotaion Report",NEGOTIATION_REPORT);
		MenuItem dispatchReport = new MenuItem(28l, "Dispatch Details Report",SEARCH_DISPATCH_DETAILS_REPORT);		
//		MenuItem bmfbReport = new MenuItem(134l,"Branch Manager's Feedback - Report", BRANCH_MANAGER_FEEDBACK_REPORT);
		
//		MenuItem bmfbReportingPattern = new MenuItem(135l,"Branch Manager's Feedback Reporting Pattern", BRANCH_MANAGER_FEEDBACK_REPORTING_PATTERN);

//		MENU_ITEMS_PERMISSION_MAPPING.put(BRANCH_MANAGER_FEEDBACK_REPORTING_PATTERN, bmfbReportingPattern);
		
		MenuItem fraudAnalysisReport = new MenuItem(29l, "Fraud Analysis Reports",FRAUD_ANALYSIS_REPORT);
		
		reportMenu.addChild(policywiseClaimSearch);
		reportMenu.addChild(executiveStatusReport);
		reportMenu.addChild(executiveStatusSummaryReport);
		reportMenu.addChild(preauthCpuWiseReport);
		reportMenu.addChild(claimStatusCpuwiseReport);
		reportMenu.addChild(helpDeskStatusReport);
		reportMenu.addChild(claimsDailyReport);
		reportMenu.addChild(hospitalwiseReport);
		reportMenu.addChild(callcenterDashBoard);
		reportMenu.addChild(automationDashBoard);
		reportMenu.addChild(paymentPendingDashBoard);
		reportMenu.addChild(billReceivedStatusReport);
		reportMenu.addChild(administrativeDashBoard);
		reportMenu.addChild(paymentProcessReport);
		reportMenu.addChild(fvrAssignmentReport);
		reportMenu.addChild(hospitalIntimationStatusReport);
		reportMenu.addChild(medicalMailStatus);
		reportMenu.addChild(dailyReport);
		reportMenu.addChild(cpuWiseReport);
		reportMenu.addChild(agentBrokerReport);
		reportMenu.addChild(opclaimReport);
		// reportMenu.addChild(createSearchLot);
		reportMenu.addChild(plannedAdmissionReport);
		reportMenu.addChild(intimatedRiskDetailsReport);
		reportMenu.addChild(medicalAuditClaimStatusReport);
		reportMenu.addChild(intimationAlternateCPUwiseReport);
		reportMenu.addChild(medicalAuditCashlessIssuanceReport);

		reportMenu.addChild(paymentBatchReport);
		reportMenu.addChild(tatReport);
		reportMenu.addChild(ackWithoutRodReport); // R1097 ACK WITHOUT ROD
													// 06-03-2018
		reportMenu.addChild(provisionReport);
		reportMenu.addChild(autoClosureBatch);
		reportMenu.addChild(productivityReport);
		reportMenu.addChild(preauthFormDocReport);
		reportMenu.addChild(notAdheringToANHReport);
		reportMenu.addChild(invAssignReport);
		reportMenu.addChild(dispatchReport);
		reportMenu.addChild(cancelReport);
		reportMenu.addChild(gmc_daily_report);
		reportMenu.addChild(faApprovedSettlementPendingReport);  //CR R1201  FA approved not settled Report
		reportMenu.addChild(provisionhistory);
		reportMenu.addChild(negotiationReport); // CR20181286
		
//		reportMenu.addChild(bmfbReport);  // CR R1238 Branch manager Feedback Report
//		reportMenu.addChild(bmfbReportingPattern);  // CR R1238 Branch manager Feedback Reporting Pattern
		
		reportMenu.addChild(fraudAnalysisReport);
		
		reportHeader.addChild(reportMenu);
		reportHeader.addChild(monitoringMenu);

		MENU_ITEMS_PERMISSION_MAPPING.put(PAYMENT_PROCESS_CPU,
				paymentProcessCpu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_64_VB_COMPLIANCE,
				processVBcomplaince);
		MENU_ITEMS_PERMISSION_MAPPING.put(UPDATE_PAN_CARD, updatePanCard);

		MENU_ITEMS_PERMISSION_MAPPING.put(UPDATE_AADHAR_DETAILS,
				updateAadharDetails);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(UPLOAD_BED_PHOTO,uploadBedPhoto);

		MENU_ITEMS_PERMISSION_MAPPING.put(CPU_SKIP_ZMR, skipZMR);
		// MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_OR_UPLOAD_DOCUMENTS,searchOrUploadDocuments);
		MENU_ITEMS_PERMISSION_MAPPING.put(OP_SCREEN, opScreen);
		MENU_ITEMS_PERMISSION_MAPPING
				.put(PROCESSING_PAYMENT, processingPayment);
		// MENU_ITEMS_PERMISSION_MAPPING.put(HEALTH_REPORT,reportMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(HEALTH_REPORT, reportMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(REPORTS_HEADER, reportHeader);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CLAIMS, paClaimMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CLAIM_REGISTER,
				paClaimRegisterMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_CLAIM_POLICY_REPORT,
				policywiseClaimSearch);
		MENU_ITEMS_PERMISSION_MAPPING.put(EXECUTIVE_STATUS_REPORT,
				executiveStatusReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(EXECUTIVE_STATUS_SUMMARY_REPORT,
				executiveStatusSummaryReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_PREAUTH_CPUWISE_REPORT,
				preauthCpuWiseReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(CLAIM_STATUS_CPUWISE_REPORT,
				claimStatusCpuwiseReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(HELP_DESK_STATUS_REPORT,
				helpDeskStatusReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(CLAIMS_DAILY_REPORT,
				claimsDailyReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(HOSPITAL_WISE_REPORT,
				hospitalwiseReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(CALL_CENTER_DASH_BOARD,
				callcenterDashBoard);
		MENU_ITEMS_PERMISSION_MAPPING.put(AUTOMATION_DASHBOARD,
				automationDashBoard);
		MENU_ITEMS_PERMISSION_MAPPING.put(PAYMENT_PENDING_DASHBOARD,
				paymentPendingDashBoard);
		MENU_ITEMS_PERMISSION_MAPPING.put(BILL_RECIECVED_STATUS_REPORT,
				billReceivedStatusReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(ADMINISTRATIVE_DASH_BOARD,
				administrativeDashBoard);
		MENU_ITEMS_PERMISSION_MAPPING.put(PAYMENT_PROCESS_REPORT,
				paymentProcessReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(FVR_ASSIGNMENT_REPORT,
				fvrAssignmentReport);

		MENU_ITEMS_PERMISSION_MAPPING.put(HOSPITAL_INTIMATION_STATUS,
				hospitalIntimationStatusReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(MEDICAL_MAIL_STATUS,
				medicalMailStatus);
		MENU_ITEMS_PERMISSION_MAPPING.put(DAILY_REPORT, dailyReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(CPU_WISE_PERFORMANCE_REPORT,
				cpuWiseReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(AGENT_BROKER_REPORT,
				agentBrokerReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(OP_CLAIM_REPORT, opclaimReport);
		// MENU_ITEMS_PERMISSION_MAPPING.put(CREATE_LOT_MAKER,createSearchLot);

		MENU_ITEMS_PERMISSION_MAPPING.put(PLANNED_ADMISSION_REPORT,
				plannedAdmissionReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(INTIMATED_RISK_DETAILS_REPORT,
				intimatedRiskDetailsReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(MEDICAL_AUDIT_CLAIM_STATUS_REPORT,
				medicalAuditClaimStatusReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(INTIMATION_ALTERNATE_CPU_REPORT,
				intimationAlternateCPUwiseReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(
				MEDICAL_AUDIT_CASHLESS_ISSUANCE_REPORT,
				medicalAuditCashlessIssuanceReport);

		MENU_ITEMS_PERMISSION_MAPPING.put(PAYMENT_BATCH_REPORT,
				paymentBatchReport);

		MENU_ITEMS_PERMISSION_MAPPING.put(TAT_REPORT, tatReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(ACK_WITHOUT_ROD_REPORT,
				ackWithoutRodReport); // R1097 ACK WITHOUT ROD 06-03-2018
		
		MENU_ITEMS_PERMISSION_MAPPING.put(PAYMENT_PROCESS_CPU,paymentProcessCpu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_64_VB_COMPLIANCE,processVBcomplaince);
		MENU_ITEMS_PERMISSION_MAPPING.put(UPDATE_PAN_CARD,updatePanCard);
		MENU_ITEMS_PERMISSION_MAPPING.put(UPDATE_AADHAR_DETAILS,updateAadharDetails);
	//	MENU_ITEMS_PERMISSION_MAPPING.put(MANAGER_FEEDBACK_FORM,managerFeedbackForm);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(UPLOAD_BED_PHOTO,uploadBedPhoto);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(CPU_SKIP_ZMR,skipZMR);
//		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_OR_UPLOAD_DOCUMENTS,searchOrUploadDocuments);
		MENU_ITEMS_PERMISSION_MAPPING.put(OP_SCREEN,opScreen);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESSING_PAYMENT,processingPayment);
//		MENU_ITEMS_PERMISSION_MAPPING.put(HEALTH_REPORT,reportMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(HEALTH_REPORT,reportMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(REPORTS_HEADER,reportHeader);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CLAIMS,paClaimMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CLAIM_REGISTER,paClaimRegisterMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_CLAIM_POLICY_REPORT,policywiseClaimSearch);
		MENU_ITEMS_PERMISSION_MAPPING.put(EXECUTIVE_STATUS_REPORT,executiveStatusReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(EXECUTIVE_STATUS_SUMMARY_REPORT,executiveStatusSummaryReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_PREAUTH_CPUWISE_REPORT,preauthCpuWiseReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(CLAIM_STATUS_CPUWISE_REPORT,claimStatusCpuwiseReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(HELP_DESK_STATUS_REPORT,helpDeskStatusReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(CLAIMS_DAILY_REPORT,claimsDailyReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(HOSPITAL_WISE_REPORT,hospitalwiseReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(CALL_CENTER_DASH_BOARD,callcenterDashBoard);
		MENU_ITEMS_PERMISSION_MAPPING.put(AUTOMATION_DASHBOARD,automationDashBoard);
		MENU_ITEMS_PERMISSION_MAPPING.put(PAYMENT_PENDING_DASHBOARD,paymentPendingDashBoard);
		MENU_ITEMS_PERMISSION_MAPPING.put(BILL_RECIECVED_STATUS_REPORT,billReceivedStatusReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(ADMINISTRATIVE_DASH_BOARD,administrativeDashBoard);
		MENU_ITEMS_PERMISSION_MAPPING.put(PAYMENT_PROCESS_REPORT,paymentProcessReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(FVR_ASSIGNMENT_REPORT,fvrAssignmentReport);

		MENU_ITEMS_PERMISSION_MAPPING.put(HOSPITAL_INTIMATION_STATUS,hospitalIntimationStatusReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(MEDICAL_MAIL_STATUS ,medicalMailStatus);
		MENU_ITEMS_PERMISSION_MAPPING.put(DAILY_REPORT ,dailyReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(CPU_WISE_PERFORMANCE_REPORT,cpuWiseReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(AGENT_BROKER_REPORT, agentBrokerReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(OP_CLAIM_REPORT,opclaimReport);
	//	MENU_ITEMS_PERMISSION_MAPPING.put(CREATE_LOT_MAKER,createSearchLot);

		MENU_ITEMS_PERMISSION_MAPPING.put(PLANNED_ADMISSION_REPORT,plannedAdmissionReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(INTIMATED_RISK_DETAILS_REPORT,intimatedRiskDetailsReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(MEDICAL_AUDIT_CLAIM_STATUS_REPORT, medicalAuditClaimStatusReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(INTIMATION_ALTERNATE_CPU_REPORT, intimationAlternateCPUwiseReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(MEDICAL_AUDIT_CASHLESS_ISSUANCE_REPORT, medicalAuditCashlessIssuanceReport);

		MENU_ITEMS_PERMISSION_MAPPING.put(PAYMENT_BATCH_REPORT,paymentBatchReport);

		MENU_ITEMS_PERMISSION_MAPPING.put(TAT_REPORT,tatReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(ACK_WITHOUT_ROD_REPORT,ackWithoutRodReport);    //  R1097  ACK WITHOUT ROD  06-03-2018
		MENU_ITEMS_PERMISSION_MAPPING.put(REVISED_PROVISION, provisionReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(AUTO_CLOSURE_BATCH, autoClosureBatch);
		MENU_ITEMS_PERMISSION_MAPPING.put(LEGAL_SCREENS, legalmenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(WAR_HOUSE, warHouseMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(FILE_DETAILS_REPORT,
				fileDetailsReport);

		MENU_ITEMS_PERMISSION_MAPPING.put(PRODUCTIVITY_REPORT,
				productivityReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(PREAUTH_FORM_DOCUMENT_REPORT,
				preauthFormDocReport);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(NOT_ADHERING_TO_ANH_REPORT,
				notAdheringToANHReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(INV_ASSIGN_STATUS_REPORT,
				invAssignReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(AUTO_ALLOCATION_CANCEL_REPORT,
				cancelReport);

		MENU_ITEMS_PERMISSION_MAPPING.put(GMC_DAILY_REPORT, gmc_daily_report);
		MENU_ITEMS_PERMISSION_MAPPING.put(FA_APPROVED_SETTLEMENT_PENDING_REPORT,faApprovedSettlementPendingReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(NEGOTIATION_REPORT,negotiationReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(FRAUD_ANALYSIS_REPORT,fraudAnalysisReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(MONITORING_REPORT,monitoringMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_DISPATCH_DETAILS_REPORT,dispatchReport);
//		MENU_ITEMS_PERMISSION_MAPPING.put(BRANCH_MANAGER_FEEDBACK_REPORT, bmfbReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROVISION_HISTORY,provisionhistory);
		// Payment Process CPU

		// Cashless Menu Items TODO

		// --------------------------------------New
		// Intimation-------------------------------------
		MenuItem policySearchMenu = new MenuItem(4l, "Create Intimation",
				SEARCH_POLICY);
		MenuItem searchIntimationMenu = new MenuItem(5l, "Search Intimation",
				SEARCH_INTIMATION);
		MenuItem searchViewDetialsMenu = new MenuItem(5l,
				"Search Intimation View-Details", INTIMATION_VIEW_DETAILS);
		
		MenuItem searchViewUprDetialsMenu = new MenuItem(5l,
				"Search UTR Details", INTIMATION_VIEW_UPR_DETAILS);
		
		MenuItem searchIntimationUnlock = new MenuItem(5l,
				"Intimation Unlock(Reimbursement)", UNLOCK_INTIMATION);
		MenuItem searchIntimationUnlockBPMNtoDB = new MenuItem(5l,
				"Intimation Unlock", UNLOCK_INTIMATION_BPMN_TO_DB);
		
		MenuItem searchIntimationUnlockAudit = new MenuItem(6l,
				"Audit Intimation Unlock", AUDIT_INTIMATION_UNLOCK);
		
				
		// omp
		MenuItem ompmenubarMenu_omp = new MenuItem(5l, "Omp Task",
				OMP_MAIN_MENUBAR);
		// MenuItem searchOMPIntimationUnlockDB = new MenuItem(57l,
		// "Intimation Unlock (OMP)",UNLOCK_OMP_INTIMATION_DB);
		MenuItem searchIntimationMenu_omp = new MenuItem(5l, "Intimation",
				OMP_SEARCHINTIMATION_CREATE);
		MenuItem registrationMenu_omp = new MenuItem(5l, "Registration", OMP_INTIMATION_REG);
		MenuItem acknowledgement_Documents_ReceivedMenu_omp = new MenuItem(5l, "Acknowledgement Documents Received", OMP_ACKNOWLEDGEMENT_DOCUMENTS_RECEIVED);
		MenuItem processompclaimprcessorMenu_omp = new MenuItem(5l,
				"Process OMP Claim -Processor", OMP_PROCESS_OMP_CLAIM_PROCESSOR);
		MenuItem processompclaimapproverMenu_omp = new MenuItem(6l,
				"Process OMP claim -Approver", OMP_PROCESS_OMP_CLAIM_APPROVER);
		
		//CR2019034
				MenuItem printPaymentLetterBulkMenu = new MenuItem(7l,
						"Print OMP Payment Letter (BULK)", PRINT_OMP_PAYEMENT_LETTER_BULK);
				
		MenuItem claimsratechangeandoutstandingupdation_omp = new MenuItem(5l,
				"OMP Claim Rate Change & OS Updation",
				OMP_CLAIMS_RATE_CHANGE_AND_OUTSTANDING_UPDATION);
		//CR20181332
		MenuItem ompBulkUploadRejection = new MenuItem(5l, "BULK Dispatch Upload & Download", OMP_BULK_UPLOAD_REJECTION);
		
		MenuItem testComponentMenu = new MenuItem(66l, "Test Component",
				TEST_COMPONENT);
		MenuItem updateHospitalInformationMenu = new MenuItem(56l,
				"Update Hospital Details", UPDATE_HOSPITAL_INFORMATION);
		MenuItem editHospitalDetailsMenu = new MenuItem(5l,
				"Edit Hospital Details", EDIT_HOSPITAL_INFORMATION);

		/***
		 * create intimation screen is disabled
		 */
		intimationMenu.addChild(policySearchMenu);

		intimationMenu.addChild(searchIntimationMenu);
		intimationMenu.addChild(searchViewDetialsMenu);
		intimationMenu.addChild(searchViewUprDetialsMenu);
		intimationMenu.addChild(searchIntimationUnlock);

		// omp
		// ompmenubarMenu_omp.addChild(searchOMPIntimationUnlockDB);
		ompmenubarMenu_omp.addChild(searchIntimationMenu_omp);
		ompmenubarMenu_omp.addChild(registrationMenu_omp);
		ompmenubarMenu_omp.addChild(acknowledgement_Documents_ReceivedMenu_omp);
		ompmenubarMenu_omp.addChild(processompclaimprcessorMenu_omp);
		ompmenubarMenu_omp.addChild(processompclaimapproverMenu_omp);
		ompmenubarMenu_omp.addChild(claimsratechangeandoutstandingupdation_omp);
		ompmenubarMenu_omp.addChild(ompBulkUploadRejection);
		ompmenubarMenu_omp.addChild(printPaymentLetterBulkMenu);


		MenuItem ompOutstandingReport = new MenuItem(100l,
				"OMP Outstanding Report", SEARCH_OMP_OUTSTANDING_REPORT);
		reportMenu.addChild(ompOutstandingReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_OMP_OUTSTANDING_REPORT,
				ompOutstandingReport);

		MenuItem ompStatusReport = new MenuItem(101l, "OMP - Status Report",
				SEARCH_OMP_STATUS_WISE_REPORT);
		reportMenu.addChild(ompStatusReport);			
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_OMP_STATUS_WISE_REPORT,ompStatusReport);		
				
		intimationMenu.addChild(searchIntimationUnlockBPMNtoDB);
		intimationMenu.addChild(searchIntimationUnlockAudit);

		intimationMenu.addChild(testComponentMenu);
		intimationMenu.addChild(updateHospitalInformationMenu);
		intimationMenu.addChild(editHospitalDetailsMenu);
		testComponentMenu.setAccessFlag(true);
		MENU_ITEMS_PERMISSION_MAPPING.put(TEST_COMPONENT, testComponentMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(INTIMATION_HEADER, intimationMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_POLICY, policySearchMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_INTIMATION,
				searchIntimationMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(INTIMATION_VIEW_DETAILS,
				searchViewDetialsMenu);
		
		// MENU_ITEMS_PERMISSION_MAPPING.put(UNLOCK_INTIMATION,
		// searchIntimationUnlockBPMNtoDB);
		MENU_ITEMS_PERMISSION_MAPPING.put(UNLOCK_INTIMATION_BPMN_TO_DB,
				searchIntimationUnlockBPMNtoDB);
		MENU_ITEMS_PERMISSION_MAPPING.put(AUDIT_INTIMATION_UNLOCK,
				searchIntimationUnlockAudit);
		MENU_ITEMS_PERMISSION_MAPPING.put(UPDATE_HOSPITAL_INFORMATION,
				updateHospitalInformationMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(EDIT_HOSPITAL_INFORMATION,
				editHospitalDetailsMenu);
		// omp
		MENU_ITEMS_PERMISSION_MAPPING.put(OMP_MAIN_MENUBAR, ompmenubarMenu_omp);
		// MENU_ITEMS_PERMISSION_MAPPING.put(UNLOCK_OMP_INTIMATION_DB,
		// searchOMPIntimationUnlockDB);
		MENU_ITEMS_PERMISSION_MAPPING.put(OMP_SEARCHINTIMATION_CREATE,
				searchIntimationMenu_omp);
		MENU_ITEMS_PERMISSION_MAPPING.put(OMP_INTIMATION_REG,
				registrationMenu_omp);
		MENU_ITEMS_PERMISSION_MAPPING.put(OMP_ACKNOWLEDGEMENT_DOCUMENTS_RECEIVED,
				acknowledgement_Documents_ReceivedMenu_omp);
		MENU_ITEMS_PERMISSION_MAPPING.put(OMP_PROCESS_OMP_CLAIM_PROCESSOR,
				processompclaimprcessorMenu_omp);
		MENU_ITEMS_PERMISSION_MAPPING.put(OMP_PROCESS_OMP_CLAIM_APPROVER,
				processompclaimapproverMenu_omp);
		MENU_ITEMS_PERMISSION_MAPPING.put(
				OMP_CLAIMS_RATE_CHANGE_AND_OUTSTANDING_UPDATION,
				claimsratechangeandoutstandingupdation_omp);
		//CR20181332
				MENU_ITEMS_PERMISSION_MAPPING.put(
						OMP_BULK_UPLOAD_REJECTION,
						ompBulkUploadRejection);

		
		// ---------------------------------------Register 
		// claim-----------------------------------------
		MenuItem registrationMenu = new MenuItem(2l, "Registration Processing",
				REGISTER_CLAIM_HEADER);
		MenuItem registerClaimMenu = new MenuItem(6L, "Register Claim",
				SEARCH_REGISTER_CLAIM);
		MenuItem ghiAllowRegisterClaimMenu = new MenuItem(14L,
				"GHI - Allow Registration", SEARCH_GHI_ALLOW_REGISTER_CLAIM);
		registrationMenu.addChild(registerClaimMenu);
		registrationMenu.addChild(ghiAllowRegisterClaimMenu);

		// MenuItem remiderLettersMenu = new MenuItem(7L,"Letter Processing" ,
		// REMINDER_LETTERS);
		// MenuItem generateReminderLetterClaimwise = new MenuItem(1L,
		// "Generate Reminder Letter(Claim wise Search)",GENERATE_REMINDER_LETTER_CLAIM_WISE);
		// MenuItem generateReminderLetterBulkMenu = new
		// MenuItem(2l,"Generate Reminder Letter Bulk",GENERATE_REMINDER_LETTER_BULK);
		// MenuItem generateCoveringLetter = new MenuItem(8L,
		// "Generate Covering Letter", GENERATE_COVERINGLETTER);
		// remiderLettersMenu.addChild(generateReminderLetterClaimwise);
		// remiderLettersMenu.addChild(generateReminderLetterBulkMenu);
		// remiderLettersMenu.addChild(generateCoveringLetter);

		MenuItem convertClaimPage = new MenuItem(7L,
				"Convert Claim Processing", CONVERT_CLAIM_PAGE);
		MenuItem convertClaimMenu = new MenuItem(9L,
				"Convert Claim To Reimbursement(In process)", CONVERT_CLAIM);
		MenuItem convertClaimOutSideMenu = new MenuItem(10L,
				"Convert Claim To Reimbursement(Search Based)",
				CONVERT_CLAIM_OUTSIDE_PROCESS);
		MenuItem ackHospitalCommunicationMenu = new MenuItem(11L,
				"Acknowledge Hospital Communication",
				ACKNOWLEDGE_HOSPITAL_COMMUNICATION);
		MenuItem convertClaimBulkMenu = new MenuItem(12L,
				"Convert Claim to Reimbursement Bulk", CONVERT_CLAIM_BULK);
		MenuItem convertClaimCashless = new MenuItem(13L,
				"Convert Claim To Cashless", CONVERT_CLAIM_CASHLESS);

		MenuItem remiderLettersMenu = new MenuItem(7L, "Letter Processing",
				REMINDER_LETTERS);
		MenuItem generateReminderLetterClaimwise = new MenuItem(1L,
				"Generate Reminder Letter(Claim wise Search)",
				GENERATE_REMINDER_LETTER_CLAIM_WISE);
		MenuItem generateReminderLetterBulkMenu = new MenuItem(2l,
				"Generate Reminder Letter (Bulk)",
				GENERATE_REMINDER_LETTER_BULK);
		MenuItem printReminderLetterBulkMenu = new MenuItem(3l,
				"Print Reminder Letter (BULK)", PRINT_REMINDER_LETTER_BULK);
		
		MenuItem generateCoveringLetter = new MenuItem(8L,
				"Generate Covering Letter", GENERATE_COVERINGLETTER);
		// MenuItem searchReminderLetters = new MenuItem(2L,
		// "Search Reminder Letter (Batch)", SEARCH_REMINDER_LETTERS);
		remiderLettersMenu.addChild(generateReminderLetterClaimwise);
		remiderLettersMenu.addChild(generateReminderLetterBulkMenu);
		remiderLettersMenu.addChild(generateCoveringLetter);
		remiderLettersMenu.addChild(printReminderLetterBulkMenu);
		// remiderLettersMenu.addChild(searchReminderLetters);

		/*
		 * registrationMenu.addChild(remiderLettersMenu);
		 * registrationMenu.addChild(generateCoveringLetter);
		 * registrationMenu.addChild(convertClaimMenu);
		 * registrationMenu.addChild(convertClaimOutSideMenu);
		 * registrationMenu.addChild(ackHospitalCommunicationMenu);
		 * registrationMenu.addChild(convertClaimCashless);
		 * registrationMenu.addChild(convertClaimBulkMenu);
		 */
		// Added logic for cashless screens hide
//		if(bpmClientContext.showCashlessMenu()){
//			convertClaimPage.addChild(convertClaimMenu);
//			convertClaimPage.addChild(convertClaimOutSideMenu);
//			convertClaimPage.addChild(convertClaimBulkMenu);
//		}
		convertClaimPage.addChild(convertClaimCashless);
		// MenuItem convertClaimCashless = new MenuItem(12L,
		// "Convert Claim To Cashless", CONVERT_CLAIM_CASHLESS);
		// MenuItem convertClaimBulkMenu = new
		// MenuItem(13L,"Convert Claim to Reimbursement Bulk",
		// CONVERT_CLAIM_BULK);

		// MenuItem searchReminderLetters = new MenuItem(2L,
		// "Search Reminder Letter (Batch)", SEARCH_REMINDER_LETTERS);

		// remiderLettersMenu.addChild(searchReminderLetters);

//		convertClaimPage.addChild(convertClaimMenu);
//		convertClaimPage.addChild(convertClaimOutSideMenu);
//		// convertClaimPage.addChild(ackHospitalCommunicationMenu);
//		convertClaimPage.addChild(convertClaimCashless);
//		convertClaimPage.addChild(convertClaimBulkMenu);

		MENU_ITEMS_PERMISSION_MAPPING.put(REGISTER_CLAIM_HEADER,
				registrationMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_REGISTER_CLAIM,
				registerClaimMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_GHI_ALLOW_REGISTER_CLAIM,
				ghiAllowRegisterClaimMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(REMINDER_LETTERS, remiderLettersMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(CONVERT_CLAIM_PAGE, convertClaimPage);
		MENU_ITEMS_PERMISSION_MAPPING.put(GENERATE_REMINDER_LETTER_CLAIM_WISE,
				generateReminderLetterClaimwise);
		MENU_ITEMS_PERMISSION_MAPPING.put(GENERATE_REMINDER_LETTER_BULK,
				generateReminderLetterBulkMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PRINT_REMINDER_LETTER_BULK,
				printReminderLetterBulkMenu);
		// MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_REMINDER_LETTERS,searchReminderLetters);
		MENU_ITEMS_PERMISSION_MAPPING.put(GENERATE_COVERINGLETTER,
				generateCoveringLetter);
		MENU_ITEMS_PERMISSION_MAPPING.put(CONVERT_CLAIM, convertClaimMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(CONVERT_CLAIM_BULK,
				convertClaimBulkMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(CONVERT_CLAIM_OUTSIDE_PROCESS,
				convertClaimOutSideMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(ACKNOWLEDGE_HOSPITAL_COMMUNICATION,
				ackHospitalCommunicationMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(CONVERT_CLAIM_CASHLESS,
				convertClaimCashless);

		//CR2019034
		
		MENU_ITEMS_PERMISSION_MAPPING.put(PRINT_OMP_PAYEMENT_LETTER_BULK,printPaymentLetterBulkMenu); 
		
		// ---------------------------------------cashless
		// process--------------------------------------------

		MenuItem preauthMenu = new MenuItem(3l, "Cashless Processing",
				CASHLESS_PROCESS_HEADER);

		MenuItem processPreauthRejectMenu = new MenuItem(9L,
				"Process Rejection", PROCESS_PREAUTH_REJECTION);
		// MenuItem preMedicalMenu = new MenuItem(11L,
		// "Process Pre Medical (Pre-Auth)", PROCESS_PRE_MEDICAL);
		MenuItem preMedicalMenu = new MenuItem(11L,
				"First Level Processing(Pre-auth)", PROCESS_PRE_MEDICAL);
		// MenuItem preMedicalProceeingEnhancementMenu = new MenuItem(9L,
		// "Process Pre-Medical (Enhancement)",
		// PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		MenuItem preMedicalProceeingEnhancementMenu = new MenuItem(9L,
				"First Level Processing (Enhancement)",
				PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		MenuItem preAuthProcessMenu = new MenuItem(10L, "Process Pre-Auth",
				PROCESS_PREAUTH);
		MenuItem preauthAutoAllocationMenu = new MenuItem(12L,
				"Process Cashless Request (Auto Allocation)",
				PROCESS_PREAUTH_AUTO_ALLOCATION);
		MenuItem processEnhanceMentMenu = new MenuItem(11L,
				"Process Enhancements", PROCESS_ENHANCEMENT);
		MenuItem processPEDQueryMenu = new MenuItem(11L, "Process PED Query",
				PROCESS_PED_QUERY);
		
//		MenuItem withdrawPreAuthMenu = new MenuItem(14L, "Withdraw Pre-Auth",       // R1313
//				WITHDRAW_PRE_AUTH);
		
		MenuItem withdrawPreAuthMenu = new MenuItem(14L, "Withdrawal of cashless approval",   // R1313
				WITHDRAW_PRE_AUTH);
		
//		MenuItem downsizePreAuthMenu = new MenuItem(15L, "DownSize Pre-Auth",     // R1313
//				DOWNSIZE_PRE_AUTH);
		
		MenuItem downsizePreAuthMenu = new MenuItem(15L, "Revision of authorization",   // R1313
				DOWNSIZE_PRE_AUTH);
		
//		MenuItem downsizePreAuthRequestMenu = new MenuItem(16L,
//				"Process Downsize Request", DOWNSIZE_PRE_AUTH_REQUEST);                // R1313
		
		MenuItem downsizePreAuthRequestMenu = new MenuItem(16L,
				"Revision of authorization Request", DOWNSIZE_PRE_AUTH_REQUEST);      // R1313
		
		MenuItem clearCashless = new MenuItem(9l, "Clear Cashless",
				CLEAR_CASHLESS);
		/*Menu For R1232 Negotiation Screen*/
		MenuItem processNegotiation = new MenuItem(17l, "Process Negotiation Cases", PROCESS_NEGOTIATION);
		MenuItem unchekNegotiation = new MenuItem(9l, "Uncheck Negotiation",UNCHECK_NEGOTIATION);
		MENU_ITEMS_PERMISSION_MAPPING.put(UNCHECK_NEGOTIATION, unchekNegotiation);		
		MenuItem withdrawPreAuthPostProcessMenu = new MenuItem(18L, "Withdraw Pre-Auth(Post Process)",
				WITHDRAW_PRE_AUTH_POST_PROCESS);
		
		MenuItem cvcAuditClsQryRly = new MenuItem(224l, "Claim Audit Query (Cashless)", CVC_AUDIT_CLS_QRY_RLY);
		
		MenuItem firstLevelProcessingMenu = new MenuItem(10L, "First Level Processing(Auto Allocation)",
				PROCESS_FLP_AUTO_ALLOCATION);
		
		preauthMenu.addChild(processPreauthRejectMenu);
		preauthMenu.addChild(preMedicalMenu);
		preauthMenu.addChild(preAuthProcessMenu);
		preauthMenu.addChild(firstLevelProcessingMenu);
		preauthMenu.addChild(preauthAutoAllocationMenu);
		preauthMenu.addChild(preMedicalProceeingEnhancementMenu);
		preauthMenu.addChild(processEnhanceMentMenu);
		preauthMenu.addChild(processPEDQueryMenu);
		preauthMenu.addChild(withdrawPreAuthMenu);
		preauthMenu.addChild(downsizePreAuthMenu);
		preauthMenu.addChild(downsizePreAuthRequestMenu);
		preauthMenu.addChild(clearCashless);
		preauthMenu.addChild(processNegotiation);
		preauthMenu.addChild(unchekNegotiation);
		preauthMenu.addChild(withdrawPreAuthPostProcessMenu);
		preauthMenu.addChild(cvcAuditClsQryRly);
			
		
		MENU_ITEMS_PERMISSION_MAPPING.put(CASHLESS_PROCESS_HEADER, preauthMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_PREAUTH_REJECTION,
				processPreauthRejectMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_PRE_MEDICAL, preMedicalMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PRE_MEDICAL_PROCESSING_ENHANCEMENT,
				preMedicalProceeingEnhancementMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_PREAUTH, preAuthProcessMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_PREAUTH_AUTO_ALLOCATION,
				preauthAutoAllocationMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_ENHANCEMENT,
				processEnhanceMentMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_PED_QUERY,
				processPEDQueryMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(WITHDRAW_PRE_AUTH,
				withdrawPreAuthMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(DOWNSIZE_PRE_AUTH,
				downsizePreAuthMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(DOWNSIZE_PRE_AUTH_REQUEST,
				downsizePreAuthRequestMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(CLEAR_CASHLESS, clearCashless);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_NEGOTIATION, processNegotiation);
		MENU_ITEMS_PERMISSION_MAPPING.put(WITHDRAW_PRE_AUTH_POST_PROCESS,
				withdrawPreAuthPostProcessMenu);

		MENU_ITEMS_PERMISSION_MAPPING.put(CVC_AUDIT_CLS_QRY_RLY, cvcAuditClsQryRly);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_FLP_AUTO_ALLOCATION,firstLevelProcessingMenu);
		
		
		
		// ------------------------------------Field
		// visit---------------------------------

		MenuItem fieldVisitMenu = new MenuItem(4l, "Field Visit Processing",
				FIELD_VISIT_CASHLESS);

		MenuItem assignFieldVisitMenu = new MenuItem(16L,
				"Assign Field Visit Representative(Cashless)",
				FIELD_VISIT_REPESENTATION);
		MenuItem reassignFieldVisitMenu = new MenuItem(17L,
				"Re-Assign Field Visit Representative",
				REASSIGN_FIELD_VISIT_REPESENTATION);
		MenuItem showPRocessFieldVisit = new MenuItem(1l,
				"Assign Field Visit Representative(Reimbursement)",
				SHOW_PROCESS_FIELD_VISIT);

		fieldVisitMenu.addChild(assignFieldVisitMenu);
		fieldVisitMenu.addChild(reassignFieldVisitMenu);
		fieldVisitMenu.addChild(showPRocessFieldVisit);

		MENU_ITEMS_PERMISSION_MAPPING.put(FIELD_VISIT_CASHLESS, fieldVisitMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(FIELD_VISIT_REPESENTATION,
				assignFieldVisitMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(REASSIGN_FIELD_VISIT_REPESENTATION,
				reassignFieldVisitMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SHOW_PROCESS_FIELD_VISIT,
				showPRocessFieldVisit);

		// ------------------------------------------Endrosement------------------------------------------------------

		MenuItem endorsementsMenu = new MenuItem(4l, "Endorsement Processing",
				ENDORSEMENTS_HEADER);

		MenuItem processPEDRequestMenu = new MenuItem(18L,
				"Process PED Request (Processor)", PROCESS_PED_REQUEST_PROCESS);
		// MenuItem processPEDRequestApprove = new MenuItem(19L,
		// "Process PED Request (Approver)", PROCESS_PED_REQUEST_APPROVE);
		// MenuItem processPEDRequestTL = new MenuItem(20L,
		// "Process PED Request (Approver)",PROCESS_PED_REQUEST_TL_APPROVE);

		MenuItem processPEDRequestApprove = new MenuItem(19L,
				"Process PED Request", PROCESS_PED_REQUEST_APPROVE);
		MenuItem processPEDRequestTL = new MenuItem(20L, "Process PED Request",
				PROCESS_PED_REQUEST_TL_APPROVE);
		MenuItem pedInitiate = new MenuItem(21L, "Initiate PED Endorsement",
				PED_INITIATE);
		 MenuItem processPEDRequest = new MenuItem(22L, "Process PED Query Request",
				 PROCESS_PED_QUERY_REQUEST);
		endorsementsMenu.addChild(processPEDRequestMenu);
		endorsementsMenu.addChild(processPEDRequestApprove);
		endorsementsMenu.addChild(processPEDRequestTL);
		endorsementsMenu.addChild(pedInitiate);
		endorsementsMenu.addChild(processPEDRequest);

		MENU_ITEMS_PERMISSION_MAPPING
				.put(ENDORSEMENTS_HEADER, endorsementsMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_PED_REQUEST_PROCESS,
				processPEDRequestMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_PED_REQUEST_APPROVE,
				processPEDRequestApprove);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_PED_REQUEST_TL_APPROVE,
				processPEDRequestTL);
		MENU_ITEMS_PERMISSION_MAPPING.put(PED_INITIATE, pedInitiate);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_PED_QUERY_REQUEST,
				processPEDRequest);

		// --------------------------------------------Process
		// Claim-----------------------------------------------

		MenuItem processClaimMenu = new MenuItem(4l, "Co-Ordinator Processing",
				PROCESS_CLAIM_HEADER);

		MenuItem uploadTranslatedDocument = new MenuItem(18L,
				"Submit Coordinator reply", UPLOAD_TRANSLATED_DOCUMENTS);
		// MenuItem uploadTranslatedDocumentR3 = new MenuItem(18L,
		// "Coordinator reply (Reimbursement)", UPLOAD_TRANSLATED_DOCUMENTSR3);
		
		MenuItem flagReconsiderationRequest = new MenuItem(18L,
				"Flag Reconsideration Request", FLAG_RECONSIDERATION_REQUEST);

		

		processClaimMenu.addChild(uploadTranslatedDocument);
		// processClaimMenu.addChild(uploadTranslatedDocumentR3);
		processClaimMenu.addChild(flagReconsiderationRequest);

		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_HEADER,
				processClaimMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(UPLOAD_TRANSLATED_DOCUMENTS,
				uploadTranslatedDocument);
		MENU_ITEMS_PERMISSION_MAPPING.put(FLAG_RECONSIDERATION_REQUEST,
				flagReconsiderationRequest);
		// MENU_ITEMS_PERMISSION_MAPPING.put(UPLOAD_TRANSLATED_DOCUMENTSR3,uploadTranslatedDocumentR3);

		// ------------------------------Specialist
		// Processing-----------------------------------------
		MenuItem specialistProcessingMenu = new MenuItem(4l,
				"Specialist Processing", SPECIALIST_PROCESSING_HEADER);

		MenuItem adviseONPED = new MenuItem(19L, "Advise on PED Request",
				ADVISE_ON_PED);
		MenuItem submitSpecialistAdviseRequestMenu = new MenuItem(19L,
				"Submit Specialist Advise (Cashless)",
				SUBMIT_SPECIALLIST_ADVISE);

		MenuItem submitSpecialistAdviseMenu = new MenuItem(5l,
				"Submit Specialist Advise (Reimbursement)",
				SUBMIT_SPECIALIST_ADVISE);

		specialistProcessingMenu.addChild(adviseONPED);
		if(bpmClientContext.showCashlessMenu()){
			specialistProcessingMenu.addChild(submitSpecialistAdviseRequestMenu);
		}
		specialistProcessingMenu.addChild(submitSpecialistAdviseMenu);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(SPECIALIST_PROCESSING_HEADER,specialistProcessingMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(ADVISE_ON_PED,adviseONPED);
		MENU_ITEMS_PERMISSION_MAPPING.put(SUBMIT_SPECIALLIST_ADVISE,submitSpecialistAdviseRequestMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SUBMIT_SPECIALIST_ADVISE, submitSpecialistAdviseMenu);
			
	
//		MENU_ITEMS_PERMISSION_MAPPING.put(CLEAR_CASHLESS,clearCashlesss);
		
		//--------------------------------------------Search Claim------------------------------------------
	//	MenuItem searchClaim = new MenuItem(5l,"Search Claim" , SEARCH_CLAIM);
		
	//	MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_CLAIM,searchClaim);
		
		//--------------------------Starfax Simulation--------------------------
		
		MenuItem starfaxSimulation = new MenuItem(6l,"Starfax Simulation" , STARFAX_SIMULATION);
		MENU_ITEMS_PERMISSION_MAPPING.put(STARFAX_SIMULATION, starfaxSimulation);
		
		
		
		
		//Reimbursement Menu Items
		//R3
		MenuItem fieldVisitReimbursementMenu = new MenuItem(1l,"Field Visit(Reimbursment)",FIELD_VISIT_REIMBURSEMENT);
//		MenuItem showPRocessFieldVisit = new MenuItem(1l,"Process Field Visit",SHOW_PROCESS_FIELD_VISIT);
//		fieldVisitReimbursementMenu.addChild(showPRocessFieldVisit);
		MENU_ITEMS_PERMISSION_MAPPING.put(FIELD_VISIT_REIMBURSEMENT, fieldVisitReimbursementMenu);
//		MENU_ITEMS_PERMISSION_MAPPING.put(SHOW_PROCESS_FIELD_VISIT, showPRocessFieldVisit);
		
		MenuItem investigation = new MenuItem(2l,"Investigation Processing",SHOW_INVESTIGATION);
		MenuItem investigationMaster = new MenuItem(2l,"Investigator Master",INVESTIGATION_MASTER);
		MenuItem searchProcessInvestigationMenu = new MenuItem(2l, "Process Investigation Initiated", SEARCH_PROCESS_INVESTIGATION_INITIATED);
		MenuItem showDraftInvestination = new MenuItem(2l, "Draft Investigation Letter", SHOW_DRAFT_INVESTIGATION);

		MenuItem showAssignInvestination = new MenuItem(2l, "Assign Investigation", SHOW_ASSIGN_INVESTIGATION);
		MenuItem showReAssignInvestination = new MenuItem(3l,"Re-Assign Investigation", SHOW_RE_ASSIGN_INVESTIGATION);
		MenuItem ackInvestinationCompletedMenu = new MenuItem(2l,"Acknowledge Investigation Completed",SEARCH_ACKNOWLEDGE_INVESTIGATION_COMPLETED);
		MenuItem investigationGrading = new MenuItem(4l,"Investigation Grading",SEARCH_INVESTIGATION_GRADING);
		MenuItem investigationDirectAssigmentMenu = new MenuItem(2l, "Investigation Direct Assignment", INVESTIGATION_DIRECT_ASSIGNEMENT);
		
		
		
		MENU_ITEMS_PERMISSION_MAPPING.put(SHOW_INVESTIGATION,investigation);
		MENU_ITEMS_PERMISSION_MAPPING.put(INVESTIGATION_MASTER,investigationMaster);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_PROCESS_INVESTIGATION_INITIATED,searchProcessInvestigationMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SHOW_DRAFT_INVESTIGATION,showDraftInvestination);
		MENU_ITEMS_PERMISSION_MAPPING.put(SHOW_ASSIGN_INVESTIGATION,showAssignInvestination);
		MENU_ITEMS_PERMISSION_MAPPING.put(SHOW_RE_ASSIGN_INVESTIGATION,showReAssignInvestination);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_ACKNOWLEDGE_INVESTIGATION_COMPLETED,ackInvestinationCompletedMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_INVESTIGATION_GRADING, investigationGrading);
		MENU_ITEMS_PERMISSION_MAPPING.put(INVESTIGATION_DIRECT_ASSIGNEMENT,investigationDirectAssigmentMenu);
		MenuItem uploadInvestigationReportMenu = new MenuItem(3l, "Upload Investigation Report", UPLOAD_INVESTIGATION_REPORT);
		
		investigation.addChild(investigationMaster);
		investigation.addChild(investigationDirectAssigmentMenu);
		investigation.addChild(searchProcessInvestigationMenu);
		investigation.addChild(showDraftInvestination);
		investigation.addChild(showAssignInvestination);
		investigation.addChild(showReAssignInvestination);
		investigation.addChild(ackInvestinationCompletedMenu);
		investigation.addChild(uploadInvestigationReportMenu);
		investigation.addChild(investigationGrading);
		
		
		MenuItem fvrProcess = new MenuItem(2l,"FVR Processing",FVR_PROCESSING);
		MenuItem fvrReportGrading = new MenuItem(2l, "Field Visit Report Grading", FVR_GRADING);
		fvrProcess.addChild(fvrReportGrading);

		MenuItem rod = new MenuItem(3l, "ROD", ROD);
		MenuItem showAckDocumentREceivedMenu = new MenuItem(3l,"Acknowledgement Documents Received",SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED);
		MenuItem serachOrUploadDocuments = new MenuItem(3l,"Search / Upload Documents", SEARCH_OR_UPLOAD_DOCUMENTS);
		MenuItem serachOrUploadDocumentsForAckNotReceived = new MenuItem(3l,"Upload Documents (Post Process)",SEARCH_OR_UPLOAD_DOCUMENTS_FOR_ACK_NOT_RECEIVED);
		MenuItem addAdditional = new MenuItem(22l, "Add Additional Documents",ADD_ADDITIONAL_DOC);
		MenuItem physicalDocument = new MenuItem(22l,"Physical Copy Received (Maker)", PHYSICAL_DOCUMENT);
		MenuItem physicalDocumentChecker = new MenuItem(22l,"Physical Copy Received (Checker)", PHYSICAL_DOCUMENT_CHECKER);
		MenuItem uploadNEFTDetails = new MenuItem(22l,"Upload NEFT Details", UPLOAD_NEFT_DETAILS);
		MenuItem createRODMenu = new MenuItem(3l, "Create ROD (Offline)", CREATE_ROD);
		MenuItem createOnlineRODMenu = new MenuItem(3l, "Create ROD (Online)", CREATE_ONLINE_ROD);
		MenuItem enterBillDetailsMenu = new MenuItem(3l,"Enter Bill Details (Bill Entry)", ENTER_BILL_DETAILS);
		
		MenuItem addAdditionalDocumentPaymentInfo  = new MenuItem(22l, "Add Additional Document-Payment Information", ADD_ADDITIONAL_DOCUMENT_PAYMENT_INFORMATION);

		// MenuItem uploadInvestigationReportMenu = new MenuItem(3l,
		// "Upload Investigation Report", UPLOAD_INVESTIGATION_REPORT);
		// MenuItem addAdditional = new MenuItem(22l,
		// "Add Additional Documents", ADD_ADDITIONAL_DOC);
		MENU_ITEMS_PERMISSION_MAPPING.put(FVR_PROCESSING,fvrProcess);
		MENU_ITEMS_PERMISSION_MAPPING.put(FVR_GRADING,fvrReportGrading);

		MENU_ITEMS_PERMISSION_MAPPING.put(ROD, rod);
		MENU_ITEMS_PERMISSION_MAPPING.put(
				SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED,
				showAckDocumentREceivedMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_OR_UPLOAD_DOCUMENTS,
				serachOrUploadDocuments);
		MENU_ITEMS_PERMISSION_MAPPING.put(
				SEARCH_OR_UPLOAD_DOCUMENTS_FOR_ACK_NOT_RECEIVED,
				serachOrUploadDocumentsForAckNotReceived);
		MENU_ITEMS_PERMISSION_MAPPING.put(CREATE_ROD, createRODMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(CREATE_ONLINE_ROD, createOnlineRODMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(ENTER_BILL_DETAILS,
				enterBillDetailsMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(UPLOAD_INVESTIGATION_REPORT,
				uploadInvestigationReportMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(ADD_ADDITIONAL_DOC, addAdditional);
		MENU_ITEMS_PERMISSION_MAPPING.put(PHYSICAL_DOCUMENT, physicalDocument);
		MENU_ITEMS_PERMISSION_MAPPING.put(PHYSICAL_DOCUMENT_CHECKER, physicalDocumentChecker);
		MENU_ITEMS_PERMISSION_MAPPING.put(UPLOAD_NEFT_DETAILS, uploadNEFTDetails);
		MENU_ITEMS_PERMISSION_MAPPING.put(ADD_ADDITIONAL_DOCUMENT_PAYMENT_INFORMATION, addAdditionalDocumentPaymentInfo);
				
		rod.addChild(showAckDocumentREceivedMenu);
		rod.addChild(serachOrUploadDocuments);
		rod.addChild(serachOrUploadDocumentsForAckNotReceived);
		rod.addChild(addAdditional);
		rod.addChild(addAdditionalDocumentPaymentInfo);
		rod.addChild(physicalDocument);
		rod.addChild(physicalDocumentChecker);
		rod.addChild(uploadNEFTDetails);
		rod.addChild(createRODMenu);
		rod.addChild(createOnlineRODMenu);
		rod.addChild(enterBillDetailsMenu);
	
		// rod.addChild(uploadInvestigationReportMenu);

		// rod.addChild(createRODMenu);
		// rod.addChild(enterBillDetailsMenu);
		// rod.addChild(addAdditional);
		// rod.addChild(serachOrUploadDocuments);
		// rod.addChild(serachOrUploadDocumentsForAckNotReceived);
		// // rod.addChild(uploadInvestigationReportMenu);

		// ________________________________PA_CLAIMS_MENU__________________________________________

		MenuItem paReimbursementProcess = new MenuItem(1l,
				"Reimbursement Processing", PA_REIMBURSEMENT_PAGE);
		MenuItem rodPAClaims = new MenuItem(1l, "ROD", ROD_PA_CLAIMS);
		MenuItem paAckDocReceived = new MenuItem(1l,
				"Acknowledgement Documents Received",
				PA_SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED);
		MenuItem paAddadditionalDoc = new MenuItem(2l,
				"Add Additional Documents", PA_ADD_ADDITIONAL_DOCUMENTS);
		MenuItem pacreateRODMenu = new MenuItem(3l, "Create ROD", PA_CREATE_ROD);
		MenuItem paEnterBillDetails = new MenuItem(4l,
				"Enter Bill Details (Bill entry)", PA_ENTER_BILL_DETAILS);
		MenuItem paUploadInvestigation = new MenuItem(4l,
				"Upload Investigation Report", PA_UPLOAD_INVESTIGATION_REPORT);
		MenuItem paSerachOrUploadDocuments = new MenuItem(3l,
				"Search / Upload Documents", PA_SEARCH_OR_UPLOAD_DOCUMENTS);
		MenuItem paSerachOrUploadDocumentsForAckNotReceived = new MenuItem(3l,
				"Upload Documents (Post Process)",
				PA_SEARCH_OR_UPLOAD_DOCUMENTS_FOR_ACK_NOT_RECEIVED);
		MenuItem paAddadditionalDocPaymentInfo = new MenuItem(2l,"Add Additional Document - Payment Information", PA_ADD_ADDITIONAL_DOCUMENT_PAYMENT_INFORMATION);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_REIMBURSEMENT_PAGE, paReimbursementProcess);
		MENU_ITEMS_PERMISSION_MAPPING.put(ROD_PA_CLAIMS, rodPAClaims);
		MENU_ITEMS_PERMISSION_MAPPING.put(
				PA_SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED, paAckDocReceived);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_ADD_ADDITIONAL_DOCUMENTS,
				paAddadditionalDoc);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CREATE_ROD, pacreateRODMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_ENTER_BILL_DETAILS, paEnterBillDetails);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_UPLOAD_INVESTIGATION_REPORT, paUploadInvestigation);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_SEARCH_OR_UPLOAD_DOCUMENTS, paSerachOrUploadDocuments);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_SEARCH_OR_UPLOAD_DOCUMENTS_FOR_ACK_NOT_RECEIVED, paSerachOrUploadDocumentsForAckNotReceived);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_ADD_ADDITIONAL_DOCUMENT_PAYMENT_INFORMATION, paAddadditionalDocPaymentInfo);
		rodPAClaims.addChild(paAckDocReceived);
		rodPAClaims.addChild(pacreateRODMenu);
		rodPAClaims.addChild(paEnterBillDetails);
		rodPAClaims.addChild(paAddadditionalDoc);
		rodPAClaims.addChild(paAddadditionalDocPaymentInfo);
		rodPAClaims.addChild(paUploadInvestigation);
		rodPAClaims.addChild(paSerachOrUploadDocuments);
		rodPAClaims.addChild(paSerachOrUploadDocumentsForAckNotReceived);
		paReimbursementProcess.addChild(rodPAClaims);

		// __________________________________________________________________________________________

		MenuItem queryRejection = new MenuItem(4l,
				"Query/Rejection Processing", QUERY_REJECTION);
		MenuItem draftQueryLetterMenu = new MenuItem(1l, "Draft Query Letter",
				DRAFT_QUERY_LETTER_SEARCH);
		MenuItem processDraftQueryMenu = new MenuItem(2l,
				"Process Draft Query Letter", PROCESS_DRAFT_QUERY_LETTER);
		// MenuItem generateReminderLetterClaimwiseMenu = new
		// MenuItem(3l,"Generate Reminder Letter Claim wise",GENERATE_REMINDER_LETTER_CLAIMWISE);
		MenuItem draftRejectionLetterMenu = new MenuItem(5l,
				"Draft Rejection Letter", DRAFT_REJECTION_LETTER);
		MenuItem processDraftRejectionMenu = new MenuItem(6l,
				"Process Draft Rejection Letter",
				PROCESS_DRAFT_REJECTION_LETTER);

		MENU_ITEMS_PERMISSION_MAPPING.put(QUERY_REJECTION, queryRejection);
		MENU_ITEMS_PERMISSION_MAPPING.put(DRAFT_QUERY_LETTER_SEARCH,
				draftQueryLetterMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_DRAFT_QUERY_LETTER,
				processDraftQueryMenu);

		// MenuItem processDraftQuery = new
		// MenuItem(7l,"PROCESS DRAFT QUERY",PROCESS_DRAFT_QUERY_LETTER_WIZARD);
		// MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_DRAFT_QUERY_LETTER_WIZARD,processDraftQuery);

		// MENU_ITEMS_PERMISSION_MAPPING.put(GENERATE_REMINDER_LETTER_CLAIMWISE,
		// generateReminderLetterClaimwiseMenu);
		// MENU_ITEMS_PERMISSION_MAPPING.put(GENERATE_REMINDER_LETTER_BULK,
		// generateReminderLetterBulkMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(DRAFT_REJECTION_LETTER,
				draftRejectionLetterMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_DRAFT_REJECTION_LETTER,
				processDraftRejectionMenu);

		queryRejection.addChild(draftQueryLetterMenu);
		queryRejection.addChild(processDraftQueryMenu);
		// queryRejection.addChild(generateReminderLetterClaimwiseMenu);
		queryRejection.addChild(draftRejectionLetterMenu);
		queryRejection.addChild(processDraftRejectionMenu);

		// MenuItem specialistProcessing = new
		// MenuItem(5l,"Specialist Processing",SPECIALIST_PROCESSING);

		// specialistProcessing.addChild(submitSpecialistAdviseMenu);

		// MENU_ITEMS_PERMISSION_MAPPING.put(SPECIALIST_PROCESSING,
		// specialistProcessing);
		MENU_ITEMS_PERMISSION_MAPPING.put(SUBMIT_SPECIALIST_ADVISE,
				submitSpecialistAdviseMenu);

		MenuItem medicalApproval = new MenuItem(6l, "Medical Processing",
				MEDICAL_APPROVAL);
		MenuItem processClaimRequestMenu = new MenuItem(6l,
				"Process Claim Request(Zonal Review)",
				PROCESS_CLAIM_REQUEST_ZONAL_REVIEW);
		MenuItem processClaimRequest = new MenuItem(6l,
				"Process Claim Request", PROCESS_CLAIM_REQUEST);
		MenuItem processClaimRequestWaitForInput = new MenuItem(6l,
				"Wait For Input", PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT);
		MenuItem processClaimRequestAutoAllocation = new MenuItem(6l,
				"Process Claim Request(Auto Allocation)", PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION);

		MenuItem cvcAuditRmbQryRly = new MenuItem(324l, "Claim Audit Query (Reimbursement)", CVC_AUDIT_RMB_QRY_RLY);

		MENU_ITEMS_PERMISSION_MAPPING.put(MEDICAL_APPROVAL, medicalApproval);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_REQUEST_ZONAL_REVIEW,
				processClaimRequestMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_REQUEST,
				processClaimRequest);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT,
				processClaimRequestWaitForInput);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION,
				processClaimRequestAutoAllocation);

		MENU_ITEMS_PERMISSION_MAPPING.put(CVC_AUDIT_RMB_QRY_RLY, cvcAuditRmbQryRly);
		medicalApproval.addChild(processClaimRequestMenu);
		medicalApproval.addChild(processClaimRequest);
		medicalApproval.addChild(processClaimRequestAutoAllocation);
		medicalApproval.addChild(processClaimRequestWaitForInput);
		medicalApproval.addChild(cvcAuditRmbQryRly);
		

		MenuItem billing = new MenuItem(7l, "Billing Processing", BILLING);
		MenuItem processClaimBilling = new MenuItem(7l,
				"Process Claim Billing", PROCESS_CLAIM_BILLING);
		MenuItem processBillingAutoAllocation = new MenuItem(7l,"Process Claim Billing (Auto Allocation)", PROCESS_CLAIM_BILLING_AUTO_ALLOCATION);
		// MenuItem processClaimRequestBenefitsBilling = new MenuItem(6l,
		// "Process Claim Request (Benefits)",
		// PROCESS_CLAIM_REQUEST_BENEFITSBILLING);
		billing.addChild(processClaimBilling);
		billing.addChild(processBillingAutoAllocation);
		// billing.addChild(processClaimRequestBenefitsBilling);
		MENU_ITEMS_PERMISSION_MAPPING.put(BILLING, billing);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_BILLING,
				processClaimBilling);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_BILLING_AUTO_ALLOCATION,processBillingAutoAllocation);
		// MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_REQUEST_BENEFITSBILLING,processClaimRequestBenefitsBilling);

		MenuItem financialApproval = new MenuItem(8l, "Financial Approval",
				FINANCIAL_APPROVAL);
		MenuItem processClaimFinancial = new MenuItem(8l,
				"Process Claim Financials", PROCESS_CLAIM_FINANCIALS);
		
		// MenuItem processClaimRequestBenefitsFinancial = new MenuItem(6l,
		// "Process Claim Request (Benefits)",
		// PROCESS_CLAIM_REQUEST_BENEFITSFINANCIAL);
		financialApproval.addChild(processClaimFinancial);
		// financialApproval.addChild(processClaimRequestBenefitsFinancial);

		

		
		
		MENU_ITEMS_PERMISSION_MAPPING
				.put(FINANCIAL_APPROVAL, financialApproval);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_FINANCIALS,
				processClaimFinancial);
		
		MenuItem processClaimCommonBillingAndFinancial = new MenuItem(208l,
				"Common for Billing & FA", PROCESS_CLAIM_COMMON_BILLING_FINANCIALS);
		
		MenuItem processClaimCommonBillingAndFinancialAutoAllocation = new MenuItem(209l,
				"Common for Billing & FA(Auto Allocation)", PROCESS_CLAIM_COMMON_BILLING_FINANCIALS_AUTO_ALLOCATION);
		
		MenuItem FinancialApprovalAutoAllocation = new MenuItem(209l,
				"Process Claim Financials (Auto Allocation)", FINANCIALS_APPROVAL_AUTO_ALLOCATION);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_COMMON_BILLING_FINANCIALS,
				processClaimCommonBillingAndFinancial);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_COMMON_BILLING_FINANCIALS_AUTO_ALLOCATION,
				processClaimCommonBillingAndFinancialAutoAllocation);
		MENU_ITEMS_PERMISSION_MAPPING.put(FINANCIALS_APPROVAL_AUTO_ALLOCATION,
				FinancialApprovalAutoAllocation);

		financialApproval.addChild(processClaimCommonBillingAndFinancial);
		financialApproval.addChild(processClaimCommonBillingAndFinancialAutoAllocation);
		financialApproval.addChild(FinancialApprovalAutoAllocation);
		MenuItem cvcAuditFaQryRly = new MenuItem(24l, "Claim Audit Query (FA)", CVC_AUDIT_FA_QRY_RLY);
		MENU_ITEMS_PERMISSION_MAPPING.put(CVC_AUDIT_FA_QRY_RLY, cvcAuditFaQryRly);
		financialApproval.addChild(cvcAuditFaQryRly);
		
		
		// MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_REQUEST_BENEFITSFINANCIAL,processClaimRequestBenefitsFinancial);

		MenuItem manageClaims = new MenuItem(9l, "Manage Claims", MANAGE_CLAIMS);
		MenuItem searchInternalNotes = new MenuItem(55l, "Add Doctor Note",
				SEARCH_INTERNAL_NOTES);
		MenuItem reOpenClaim = new MenuItem(1l,
				"Re-Open Claim - ROD Level(Search Based)",
				RE_OPEN_CLAIM_ROD_LEVEL);
		MenuItem closeClaimInProcess = new MenuItem(3l,
				"Close Claim - ROD Level(In Process)",
				CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL);
		MenuItem closeClaim = new MenuItem(4l,
				"Close Claim - ROD Level(Search Based)", CLOSE_CLAIM_ROD_LEVEL);
		MenuItem closeClaimClaimLevel = new MenuItem(5l,
				"Close Claim - Claim Level (Search Based)",
				CLOSE_CLAIM_CLAIM_LEVEL);
		MenuItem reOpenClaimClaimLevel = new MenuItem(6l,
				"Re-Open Claim - Claim Level (Search Based)",
				RE_OPEN_CLAIM_CLAIM_LEVEL);
		MenuItem allowReconsider = new MenuItem(7l, "Allow Reconsideration",
				ALLOW_RECONSIDERATION);
		MenuItem claimWiseAllowApproval = new MenuItem(28l,
				"Claim Wise Allow Approval", CLAIM_WISE_ALLOW_APPROVAL);
		MenuItem updateRodDetails = new MenuItem(29l, "Update Rod Details",
				UPDATE_ROD_DETAILS);

		MenuItem cancelAckMenu = new MenuItem(25l, "Cancel Acknowledgment",
				CANCEL_ACKNOWLEDGEMENT);
		MenuItem settlementPullBackMenu = new MenuItem(26l, "UNDO FA",
				SETTLEMENT_PULL_BACK);
		MenuItem lotPullBackMenu = new MenuItem(26l, "UNDO LOT", LOT_PULL_BACK);

		MenuItem allocateCorpBuffer = new MenuItem(21L,
				"Allocate Corporate Buffer", ALLOCATE_CORP_BUFFER);
		
		MenuItem updateNegotiation = new MenuItem(28L, "Update Negotiation Details", UPDATE_NEGOTIATION);

		// New menu added for Hospital scoring CR 023
		MenuItem hospitalScoringDetails = new MenuItem(1l,"Edit Hospital Scoring", HOSPITAL_SCORING_DETAILS);
				
		MenuItem holdMonitorFlpScreen = new MenuItem(29l, "Hold Monitoring - First Level Processing",
				HOLD_MONITOR_FLP_SCREEN);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(MANAGE_CLAIMS, manageClaims);
		MENU_ITEMS_PERMISSION_MAPPING.put(RE_OPEN_CLAIM_ROD_LEVEL, reOpenClaim);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_INTERNAL_NOTES,
				searchInternalNotes);
		MENU_ITEMS_PERMISSION_MAPPING.put(CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL,
				closeClaimInProcess);
		MENU_ITEMS_PERMISSION_MAPPING.put(CLOSE_CLAIM_ROD_LEVEL, closeClaim);
		MENU_ITEMS_PERMISSION_MAPPING.put(CLOSE_CLAIM_CLAIM_LEVEL,
				closeClaimClaimLevel);
		MENU_ITEMS_PERMISSION_MAPPING.put(RE_OPEN_CLAIM_CLAIM_LEVEL,
				reOpenClaimClaimLevel);
		MENU_ITEMS_PERMISSION_MAPPING.put(ALLOW_RECONSIDERATION,
				allowReconsider);
		MENU_ITEMS_PERMISSION_MAPPING.put(CLAIM_WISE_ALLOW_APPROVAL,
				claimWiseAllowApproval);
		MENU_ITEMS_PERMISSION_MAPPING.put(ALLOCATE_CORP_BUFFER,
				allocateCorpBuffer);
		MENU_ITEMS_PERMISSION_MAPPING.put(UPDATE_ROD_DETAILS, updateRodDetails);
		MENU_ITEMS_PERMISSION_MAPPING.put(UPDATE_NEGOTIATION, updateNegotiation);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(HOSPITAL_SCORING_DETAILS, hospitalScoringDetails);
		MENU_ITEMS_PERMISSION_MAPPING.put(HOLD_MONITOR_FLP_SCREEN,holdMonitorFlpScreen);

		// MenuItem cancelAckMenu = new MenuItem(25l, "Cancel Acknowledgment",
		// CANCEL_ACKNOWLEDGEMENT);
		// MenuItem settlementPullBackMenu = new MenuItem(26l,
		// "Claim Final Approval Cancel", SETTLEMENT_PULL_BACK);

		manageClaims.addChild(cancelAckMenu);
		manageClaims.addChild(searchInternalNotes);
		manageClaims.addChild(settlementPullBackMenu);
		manageClaims.addChild(reOpenClaim);
		manageClaims.addChild(reOpenClaimClaimLevel);
		manageClaims.addChild(closeClaimInProcess);
		manageClaims.addChild(closeClaim);
		manageClaims.addChild(closeClaimClaimLevel);
		manageClaims.addChild(reOpenClaimClaimLevel);
		manageClaims.addChild(skipZMR);
		manageClaims.addChild(allowReconsider);
		manageClaims.addChild(claimWiseAllowApproval);
		manageClaims.addChild(updateRodDetails);

		manageClaims.addChild(cancelAckMenu);
		manageClaims.addChild(settlementPullBackMenu);
		manageClaims.addChild(lotPullBackMenu);
		manageClaims.addChild(skipZMR);
		manageClaims.addChild(allocateCorpBuffer);
		manageClaims.addChild(updateNegotiation);
		// manageClaims.addChild(searchIntimationUnlock);
		//Add new Hospital Scoring Menu CR2019023
		manageClaims.addChild(hospitalScoringDetails);
		manageClaims.addChild(holdMonitorFlpScreen);
		
		MenuItem rewardAndRecognitionCell = new MenuItem(24l,
				"Reward & Recognition Cell Processing", REWARD_RECOGNITION_CELL);
		MenuItem initiateRRCRequest = new MenuItem(24l, "Initiate RRC Request",
				INITIATE_RRC_REQUEST);
		MenuItem processRRCRequest = new MenuItem(24l, "Process RRC Request",
				PROCESS_RRC_REQUEST);
		MenuItem reviewRRCRequest = new MenuItem(24l, "Review RRC Request",
				REVIEW_RRC_REQUEST);
		MenuItem modifyRRCRequest = new MenuItem(24l, "Modify RRC Request",
				MODIFY_RRC_REQUEST);
		MenuItem searchRRCRequest = new MenuItem(24l, "Search RRC Request",
				SEARCH_RRC_REQUEST);
		
		//Dinesh
		MenuItem rrcStatusScreen = new MenuItem(24l, "RRC Status Screen",
				RRC_STATUS_SCREEN);

		rewardAndRecognitionCell.addChild(initiateRRCRequest);
		rewardAndRecognitionCell.addChild(processRRCRequest);
		rewardAndRecognitionCell.addChild(reviewRRCRequest);
		rewardAndRecognitionCell.addChild(modifyRRCRequest);
		rewardAndRecognitionCell.addChild(searchRRCRequest);
		
		//Dinesh
		rewardAndRecognitionCell.addChild(rrcStatusScreen);

		MENU_ITEMS_PERMISSION_MAPPING.put(REWARD_RECOGNITION_CELL,
				rewardAndRecognitionCell);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_RRC_REQUEST,
				processRRCRequest);
		MENU_ITEMS_PERMISSION_MAPPING.put(REVIEW_RRC_REQUEST, reviewRRCRequest);
		MENU_ITEMS_PERMISSION_MAPPING.put(MODIFY_RRC_REQUEST, modifyRRCRequest);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_RRC_REQUEST, searchRRCRequest);
		
		//Dinesh
		MENU_ITEMS_PERMISSION_MAPPING.put(RRC_STATUS_SCREEN, rrcStatusScreen);
				
		MENU_ITEMS_PERMISSION_MAPPING.put(INITIATE_RRC_REQUEST,
				initiateRRCRequest);
		
		
		MenuItem talkTalkTalk = new MenuItem(275l,
				"TALK TALK TALK", TALK_TALK_TALK);
		MenuItem initiateTALKTALKTALK = new MenuItem(275l, "Initiate TALK TALK TALK",
				INITIATE_TALK_TALK_TALK);
		
		talkTalkTalk.addChild(initiateTALKTALKTALK);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(TALK_TALK_TALK,talkTalkTalk);
		MENU_ITEMS_PERMISSION_MAPPING.put(INITIATE_TALK_TALK_TALK,initiateTALKTALKTALK);

		MENU_ITEMS_PERMISSION_MAPPING
				.put(CANCEL_ACKNOWLEDGEMENT, cancelAckMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SETTLEMENT_PULL_BACK,
				settlementPullBackMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(LOT_PULL_BACK, lotPullBackMenu);
		
		/* Menu for R1191 - RAW*/
		MenuItem researchAnalysisWing = new MenuItem(32l, "Research Analysis Wing Processing", RESEARCH_ANALYSIS_WING_PROCESSING);
		MenuItem processRawRequest = new MenuItem(34l, "Process RAW Request", PROCESS_RAW_REQUEST);
		
		researchAnalysisWing.addChild(processRawRequest);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(RESEARCH_ANALYSIS_WING_PROCESSING,researchAnalysisWing);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_RAW_REQUEST, processRawRequest);
		
		/**
		 * Menu added for GLX2020044
		 */
		MenuItem postCashlessCell = new MenuItem(32l, "Post Cashless Cell(PCC Processing)", POST_CASHLESS_CELL);
		MenuItem pccCoOrdinator = new MenuItem(34l, "PCC Co-Ordinator", PCC_CO_ORDINATOR);
		MenuItem pccProcessor = new MenuItem(34l, "PCC Processor", PCC_PROCESSOR);
		MenuItem pccReviewer = new MenuItem(34l, "PCC Reviewer", PCC_REVIEWER);
		MenuItem divisionHead = new MenuItem(34l, "Division Head(PCC Process)", DIVISION_HEAD);
		MenuItem zonalMedicalHead = new MenuItem(34l, "Zonal Medical Head(PCC Process)", ZONAL_MEDICAL_HEAD_PCC_PROCESS);
		MenuItem zonalCoOrdinator = new MenuItem(34l, "Zonal Co Ordinator(PCC Process)", ZONAL_CO_ORDINATOR);
		
		
		MenuItem reAssignHRM = new MenuItem(34l, "Re-Assign HRM", REASSIGN_HRM);
		
		postCashlessCell.addChild(pccCoOrdinator);
		postCashlessCell.addChild(pccProcessor);
		postCashlessCell.addChild(pccReviewer);
		postCashlessCell.addChild(divisionHead);
		postCashlessCell.addChild(zonalMedicalHead);
		postCashlessCell.addChild(zonalCoOrdinator);
		postCashlessCell.addChild(reAssignHRM);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(POST_CASHLESS_CELL,postCashlessCell);
		MENU_ITEMS_PERMISSION_MAPPING.put(PCC_CO_ORDINATOR,pccCoOrdinator);
		MENU_ITEMS_PERMISSION_MAPPING.put(PCC_PROCESSOR,pccProcessor);
		MENU_ITEMS_PERMISSION_MAPPING.put(PCC_REVIEWER,pccReviewer);
		MENU_ITEMS_PERMISSION_MAPPING.put(DIVISION_HEAD,divisionHead);
		MENU_ITEMS_PERMISSION_MAPPING.put(ZONAL_MEDICAL_HEAD_PCC_PROCESS,zonalMedicalHead);
		MENU_ITEMS_PERMISSION_MAPPING.put(ZONAL_CO_ORDINATOR,zonalCoOrdinator);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(REASSIGN_HRM,reAssignHRM);

		MenuItem hrmAccess = new MenuItem(110l, "HRM Access", HRM_ACCESS);
		MenuItem hrmp = new MenuItem(34l, "Long Stay Claims", HRMP);
		MenuItem hrmCoOrdinator = new MenuItem(34l, "PCC Negotiations", HRM_CO_ORDINATOR);
		hrmAccess.addChild(hrmp);
		hrmAccess.addChild(hrmCoOrdinator);
		
		
		MENU_ITEMS_PERMISSION_MAPPING.put(HRM_ACCESS,hrmAccess);
		MENU_ITEMS_PERMISSION_MAPPING.put(HRMP,hrmp);
		MENU_ITEMS_PERMISSION_MAPPING.put(HRM_CO_ORDINATOR,hrmCoOrdinator);
		
		MenuItem speicalApprovals = new MenuItem(23l, "Special Approvals", SPECIAL_APPROVALS);
		MenuItem approveClaim = new MenuItem(23l, "Approve Claim(Special Approval)", APPROVE_CLAIM);
		MenuItem refferdForSpecialApproval = new MenuItem(23l, "Process Claim (Reffered for Special Approval)", PROCESS_CLAIM);
		
		speicalApprovals.addChild(approveClaim);
		speicalApprovals.addChild(refferdForSpecialApproval);

		MENU_ITEMS_PERMISSION_MAPPING.put(SPECIAL_APPROVALS, speicalApprovals);
		MENU_ITEMS_PERMISSION_MAPPING.put(APPROVE_CLAIM, approveClaim);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM,
				refferdForSpecialApproval);

		// Added for testing the R3 Menu
		MenuItem acknowledgeDocReceived = new MenuItem(6l,
				"Acknowledge Document Received", ACKNOWLEDGE_DOCUMENT_RECEIVED);

		MenuItem paymentProcessor = new MenuItem(11l, "Payment Processing",
				PAYMENT_PROCESSOR);
		MenuItem createSearchLotMenu = new MenuItem(11l,
				"Create / Search Lot (Maker)", CREATE_OR_SEARCH_LOT);
		MenuItem createBatchMenu = new MenuItem(11l, "Create Batch (Checker)",
				CREATE_BATCH);
		//bancs payment verification
		MenuItem createPaymentVerificationLevel1 = new MenuItem(11l,"Payment Verifictaion Level I",PAYMENT_VERIFICATION_LVL1);
		MenuItem createPaymentVerificationLevel2 = new MenuItem(11l,"Payment Verifictaion Level II",PAYMENT_VERIFICATION_LVL2);
		
		MenuItem processPaymentReturnsMenu = new MenuItem(11l,
				"Process Payment Returns", PROCESS_PAYMENT_RETURNS);
		
		MenuItem paymentReprocess = new MenuItem(11l,"Payment Re-Process", PAYMENT_REPROCESS);
		MenuItem paymentInitiateRecovery = new MenuItem(11l,"Initiate Recovery",PAYMENT_INITIATE_RECOVERY);
		
		//MenuItem stopPaymentTracking = new MenuItem(11l,"Stop Payment Tracking",STOP_PAYMENT_TRACKING);
		MenuItem stopPaymentRequest = new MenuItem(111l,"Stop Payment Request",STOP_PAYMENT_REQUREST);
		MenuItem stopPaymentValidation = new MenuItem(112l,"Stop Payment Validation",STOP_PAYMENT_VALIDATION);
		
		// MenuItem updatePaymentBatchMenu = new MenuItem(11l,
		// "Update Payment Detail", UPDATE_PAYMENT_DETAILS);
		
		
		MENU_ITEMS_PERMISSION_MAPPING.put(PAYMENT_PROCESSOR, paymentProcessor);
		MENU_ITEMS_PERMISSION_MAPPING.put(CREATE_OR_SEARCH_LOT,
				createSearchLotMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(CREATE_BATCH, createBatchMenu);
		//bancs payment verifictaion
		MENU_ITEMS_PERMISSION_MAPPING.put(PAYMENT_VERIFICATION_LVL1, createPaymentVerificationLevel1);
		MENU_ITEMS_PERMISSION_MAPPING.put(PAYMENT_VERIFICATION_LVL2, createPaymentVerificationLevel2);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_PAYMENT_RETURNS,
				processPaymentReturnsMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_PAYMENT_RETURNS, processPaymentReturnsMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PAYMENT_REPROCESS, paymentReprocess);
		MENU_ITEMS_PERMISSION_MAPPING.put(PAYMENT_INITIATE_RECOVERY, paymentInitiateRecovery);
		//MENU_ITEMS_PERMISSION_MAPPING.put(STOP_PAYMENT_TRACKING, stopPaymentTracking);
		MENU_ITEMS_PERMISSION_MAPPING.put(STOP_PAYMENT_REQUREST, stopPaymentRequest);
		MENU_ITEMS_PERMISSION_MAPPING.put(STOP_PAYMENT_VALIDATION, stopPaymentValidation);
		
		/*stopPaymentTracking.addChild(stopPaymentRequest);
		stopPaymentTracking.addChild(stopPaymentValidation);*/
		// MENU_ITEMS_PERMISSION_MAPPING.put(UPDATE_PAYMENT_DETAILS,updatePaymentBatchMenu);

		MENU_ITEMS_PERMISSION_MAPPING.put(INTIMATION_VIEW_UPR_DETAILS,
				searchViewUprDetialsMenu);
		
		paymentProcessor.addChild(createSearchLotMenu);
		paymentProcessor.addChild(createBatchMenu);
		paymentProcessor.addChild(createPaymentVerificationLevel1);
		paymentProcessor.addChild(createPaymentVerificationLevel2);
		paymentProcessor.addChild(paymentReprocess);
		paymentProcessor.addChild(paymentInitiateRecovery);

		// paymentProcessor.addChild(updatePaymentBatchMenu);

		paymentProcessor.addChild(paymentProcessCpu);
		
		//paymentProcessor.addChild(stopPaymentTracking);
		paymentProcessor.addChild(stopPaymentRequest);
		paymentProcessor.addChild(stopPaymentValidation);
		
		MenuItem misc = new MenuItem(15l, "Misc", MISC);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(MISC, misc);

		MenuItem registerClaimRefundMenu = new MenuItem(12l, " 	Register Claim Refund", REGISTER_CLAIM_REFUND);
		MenuItem processClaimRefundMenu = new MenuItem(12l, " 	Process Claim Refund", PROCESS_CLAIM_REFUND);
		MenuItem userAccessAllocationMenu = new MenuItem(12l, " User Access(Auto-Allocation)", USER_ACCESS_ALLOCATION);
		MenuItem userManagementMenu = new MenuItem(12l, " User Management",USER_MANAGEMENT);
		MenuItem userReAllocationMenu = new MenuItem(12l, "Reallocation", USER_RE_ALLOCATION);
		MenuItem pinCodeClassMenu = new MenuItem(12l, "Pincode to Class Mapping", PINCODE_CLASS_MAPPING);
		MenuItem cpuAutoAllocationMenu = new MenuItem(12l, "Cpu Auto-Allocation", CPU_AUTO_ALLOCATION);
		MenuItem updateSublimit = new MenuItem(12l,"Update Sublimit", UPDATE_SUBLIMIT);
		MenuItem gmcAutomailer = new MenuItem(12l,"GMC Automailer", GMC_AUTOMAILER);
		//MenuItem topUpPolicyMasterMenu = new MenuItem(12l, "Top Up Policy- Master", TOP_UP_POLICY_MASTER_SCREEN);
		
				
		MENU_ITEMS_PERMISSION_MAPPING.put(MISC,misc);
		MENU_ITEMS_PERMISSION_MAPPING.put(REGISTER_CLAIM_REFUND,registerClaimRefundMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_REFUND,processClaimRefundMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(USER_ACCESS_ALLOCATION,userAccessAllocationMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(USER_MANAGEMENT, userManagementMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(USER_RE_ALLOCATION,userReAllocationMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PINCODE_CLASS_MAPPING,pinCodeClassMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(CPU_AUTO_ALLOCATION,cpuAutoAllocationMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(UPDATE_SUBLIMIT, updateSublimit);
		MENU_ITEMS_PERMISSION_MAPPING.put(GMC_AUTOMAILER, gmcAutomailer);
	
		//	MENU_ITEMS_PERMISSION_MAPPING.put(TOP_UP_POLICY_MASTER_SCREEN,topUpPolicyMasterMenu);
		
		misc.addChild(registerClaimRefundMenu);
		misc.addChild(processClaimRefundMenu);
		misc.addChild(userAccessAllocationMenu);
		misc.addChild(userManagementMenu);
		misc.addChild(userReAllocationMenu);
		misc.addChild(pinCodeClassMenu);

		misc.addChild(cpuAutoAllocationMenu);
		misc.addChild(updateSublimit);
		misc.addChild(gmcAutomailer);
	//	misc.addChild(topUpPolicyMasterMenu);
		
		//Added for testing the R3 Menu
		//MenuItem acknowledgeDocReceived = new MenuItem(6l,"Acknowledge Document Received" , ACKNOWLEDGE_DOCUMENT_RECEIVED);
		
		
		
		/*MenuItem processClaimMenuR3 = new MenuItem(4l, "Process Claim", PROCESS_CLAIM_HEADERR3);
		
		//MenuItem uploadTranslatedDocumentR3 = new MenuItem(18L, "Coordinator reply", UPLOAD_TRANSLATED_DOCUMENTSR3);
		processClaimMenuR3.addChild(uploadTranslatedDocumentR3);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_HEADERR3, processClaimMenuR3);*/
		//MENU_ITEMS_PERMISSION_MAPPING.put(UPLOAD_TRANSLATED_DOCUMENTSR3,uploadTranslatedDocumentR3);

		// Added for testing the R3 Menu
		// MenuItem acknowledgeDocReceived = new
		// MenuItem(6l,"Acknowledge Document Received" ,
		// ACKNOWLEDGE_DOCUMENT_RECEIVED);

		/*
		 * MenuItem processClaimMenuR3 = new MenuItem(4l, "Process Claim",
		 * PROCESS_CLAIM_HEADERR3);
		 * 
		 * //MenuItem uploadTranslatedDocumentR3 = new MenuItem(18L,
		 * "Coordinator reply", UPLOAD_TRANSLATED_DOCUMENTSR3);
		 * processClaimMenuR3.addChild(uploadTranslatedDocumentR3);
		 * 
		 * MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_CLAIM_HEADERR3,
		 * processClaimMenuR3);
		 */
		// MENU_ITEMS_PERMISSION_MAPPING.put(UPLOAD_TRANSLATED_DOCUMENTSR3,uploadTranslatedDocumentR3);

		MenuItem op_healthCheckProcessing = new MenuItem(11l,
				"OP Processing", OP_HEALTH_CHECK_PROCESSING);
		MenuItem claimForExpiredPolicies = new MenuItem(11L, "Allow Claim for Expired policies",EXPIRED_POLICIES_CLAIM_OP);
		MenuItem registerClaim = new MenuItem(11l, "Create & Register OP Request",REGISTER_CLAIM_OP);
		MenuItem processClaim = new MenuItem(11l,
				"Process OP Request", PORCESS_CLAIM_OP);
		MenuItem paymentProcess = new MenuItem(11l, "Payment Process", PAYMENT_PROCESS_OP);
		MenuItem createBatch = new MenuItem(11l, "Create Batch", CREATE_BATCH_OP);
		MenuItem createSettlementLetterBatch = new MenuItem(11l, "Create Settlement Batch", CREATE_SETTLEMENT_BATCH_OP);

		MENU_ITEMS_PERMISSION_MAPPING.put(OP_HEALTH_CHECK_PROCESSING,op_healthCheckProcessing);
		MENU_ITEMS_PERMISSION_MAPPING.put(EXPIRED_POLICIES_CLAIM_OP,claimForExpiredPolicies);
		MENU_ITEMS_PERMISSION_MAPPING.put(REGISTER_CLAIM_OP, registerClaim);
		MENU_ITEMS_PERMISSION_MAPPING.put(PORCESS_CLAIM_OP, processClaim);
		MENU_ITEMS_PERMISSION_MAPPING.put(PAYMENT_PROCESS_OP, paymentProcess);
		MENU_ITEMS_PERMISSION_MAPPING.put(CREATE_BATCH_OP, createBatch);
		MENU_ITEMS_PERMISSION_MAPPING.put(CREATE_SETTLEMENT_BATCH_OP, createSettlementLetterBatch);

		
		op_healthCheckProcessing.addChild(claimForExpiredPolicies);
		op_healthCheckProcessing.addChild(registerClaim);
		op_healthCheckProcessing.addChild(processClaim);
		op_healthCheckProcessing.addChild(paymentProcess);
		paymentProcess.addChild(createBatch);
		paymentProcess.addChild(createSettlementLetterBatch);


		// fieldVisitMenu.addChild(new MenuItem(17L, "Select Hospital",
		// SELECT_HOSPITAL));

		// acknowledgeDocReceived.addChild(new
		// MenuItem(20L,"Acknowledge Document Received",ACKNOWLEDGE_DOCUMENT_RECEIVED));
		// specialistProcessingMenu.addChild(new MenuItem(19L, "Test",
		// TEST_VIEW));

		// for Reimbursement

		MenuItem masterMenu = new MenuItem(16l, "Master", MASTER);
		MenuItem icdSublimitMappingMenu = new MenuItem(1111l,
				"ICD Sublimit Mapping", ICD_SUBLIMIT_MAPPING);
		MenuItem topUpPolicyMasterMenu = new MenuItem(12l, "Policy Alert", TOP_UP_POLICY_MASTER_SCREEN);
		MenuItem fraudIdentification = new MenuItem(13l, "Fraud Identification", FRAUD_IDENTIFICATION);
		MenuItem claimsAlertMasterMenu = new MenuItem(14l, "Claims Alert", CLAIMS_ALERT_MASTER_SCREEN);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(MASTER, masterMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(ICD_SUBLIMIT_MAPPING,
				icdSublimitMappingMenu);
		masterMenu.addChild(icdSublimitMappingMenu);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(TOP_UP_POLICY_MASTER_SCREEN,topUpPolicyMasterMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(FRAUD_IDENTIFICATION,fraudIdentification);
		MENU_ITEMS_PERMISSION_MAPPING.put(CLAIMS_ALERT_MASTER_SCREEN,claimsAlertMasterMenu);

		masterMenu.addChild(topUpPolicyMasterMenu);
		masterMenu.addChild(fraudIdentification);
		masterMenu.addChild(claimsAlertMasterMenu);

		MENU_ITEMS_PERMISSION_MAPPING.put(HEALTH_PAGE, healthMenu);

//		MENU_ITEMS_PERMISSION_MAPPING.put(CASHLESS_PAGE, cashLessMenu);

		MENU_ITEMS_PERMISSION_MAPPING.put(REIMBURSEMENT_PAGE, rembursementMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_CLAIM, searchClaim);

		// Medical Opinion Validation
		MenuItem medicalActivities = new MenuItem(11L, "Medical Activities",
				MEDICAL_ACTIVITIES);
		MenuItem dataCodingdataCorrection = new MenuItem(0l,"Data Validation", DATA_CODING_DATA_CORRECTION);
		MenuItem dataCodingdataCorrectionHistorical = new MenuItem(0l,"Data Validation Historical", DATA_CODING_DATA_CORRECTION_HISTORICAL);
		MenuItem dataCodingdataCorrectionPriority = new MenuItem(0l,"Data Validation Priority", DATA_CODING_DATA_CORRECTION_PRIORITY);
		MenuItem opinionValidation = new MenuItem(0l, "Opinion Validation",
				OPINION_VALIDATION);
		MenuItem opinionValidationReport = new MenuItem(001l,
				"Opinion Validation Report", OPINION_VALIDATION_REPORT);
		MenuItem holdMonitorScreen = new MenuItem(30l, "Hold Monitoring - Common for Billing & FA",
				HOLD_MONITOR_SCREEN);
		
		MenuItem recordMarketingEscalation = new MenuItem(0l,"Record Marketing Escalations", RECORD_MARKETING_ESCALATION);
		
		MenuItem marketingEscalationReport = new MenuItem(001l,
				"Marketing Escalation Report", MARKETING_ESCALATION_REPORT);
		
		MenuItem holdMonitorScreenForMA = new MenuItem(31l, "Hold Monitoring - Process Claim Request",
				HOLD_MONITOR_SCREEN_FOR_MA);
		MenuItem holdMonitorScreenForFA = new MenuItem(30l, "Hold Monitoring - Process Claim Financials",
				HOLD_MONITOR_SCREEN_FOR_FA_AUTO);
				
		MenuItem holdMonitorScreenForBilling = new MenuItem(31l, "Hold Monitoring - Process Claim Billing",HOLD_MONITOR_SCREEN_FOR_BILLING);
		
		
		MENU_ITEMS_PERMISSION_MAPPING
						.put(DATA_CODING_DATA_CORRECTION, dataCodingdataCorrection);
		MENU_ITEMS_PERMISSION_MAPPING
				.put(DATA_CODING_DATA_CORRECTION_HISTORICAL, dataCodingdataCorrectionHistorical);
		MENU_ITEMS_PERMISSION_MAPPING.put(DATA_CODING_DATA_CORRECTION_PRIORITY, dataCodingdataCorrectionPriority);
		MENU_ITEMS_PERMISSION_MAPPING
		.put(MEDICAL_ACTIVITIES, medicalActivities);
		MENU_ITEMS_PERMISSION_MAPPING
				.put(OPINION_VALIDATION, opinionValidation);
		MENU_ITEMS_PERMISSION_MAPPING.put(OPINION_VALIDATION_REPORT,
				opinionValidationReport);
		MENU_ITEMS_PERMISSION_MAPPING.put(HOLD_MONITOR_SCREEN,
				holdMonitorScreen);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(RECORD_MARKETING_ESCALATION,
				recordMarketingEscalation);
		
		MENU_ITEMS_PERMISSION_MAPPING.put(HOLD_MONITOR_SCREEN_FOR_MA,
				holdMonitorScreenForMA);
		MENU_ITEMS_PERMISSION_MAPPING.put(HOLD_MONITOR_SCREEN_FOR_FA_AUTO,
				holdMonitorScreenForFA);
		MENU_ITEMS_PERMISSION_MAPPING.put(HOLD_MONITOR_SCREEN_FOR_BILLING,holdMonitorScreenForBilling);
		MENU_ITEMS_PERMISSION_MAPPING.put(MARKETING_ESCALATION_REPORT,marketingEscalationReport);
		
		medicalActivities.addChild(dataCodingdataCorrection);
		medicalActivities.addChild(dataCodingdataCorrectionHistorical);
		medicalActivities.addChild(dataCodingdataCorrectionPriority);
		medicalActivities.addChild(opinionValidation);
		medicalActivities.addChild(opinionValidationReport);
		medicalActivities.addChild(holdMonitorScreen);
		medicalActivities.addChild(recordMarketingEscalation);
		medicalActivities.addChild(marketingEscalationReport);
		medicalActivities.addChild(holdMonitorScreenForMA);
		medicalActivities.addChild(holdMonitorScreenForFA);
		medicalActivities.addChild(holdMonitorScreenForBilling);
		
		
		// Added logic for cashless screens hide
//		if(bpmClientContext.showCashlessMenu()){
//			medicalActivities.addChild(holdMonitorScreen);
//		}
		
		// ICAC REQUEST MENU
		MenuItem icacMenu = new MenuItem(11L, "ICAC",
				ICAC_MENU);
		MenuItem processIcacRequest = new MenuItem(0l,"Process ICAC Request", PROCESS_ICAC_REQUEST);
		MENU_ITEMS_PERMISSION_MAPPING.put(ICAC_MENU, icacMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PROCESS_ICAC_REQUEST, processIcacRequest);
		icacMenu.addChild(processIcacRequest);

		//Manager Feedback Screen
		MenuItem managerFeedbackRoot = new MenuItem(11L, "Manager Feedback",CLM_BR_MGR_FEEDBACK);
		//MenuItem managerFeedbackScreen = new MenuItem(0l, "Manager's Feedback Form",MANAGER_FEEDBACK_FORM);
		MenuItem managerFeedBackHomeScreen = new MenuItem(0l, "Manager's Feedback Form",MenuPresenter.SHOW_BRANCH_MANAGER_FEEDBACK_HOME_PAGE);
		MenuItem managerPreviousFeedback = new MenuItem(801l, "Previous Feedback raised by Manager",MANAGER_PREVIOUS_FEEDBACK);
		MenuItem branchMangerFeedbackReply = new MenuItem(802l, "Reply To Branch Manager's Feedback", MANAGER_FEEDBACK);
		MenuItem bmfbReport1 = new MenuItem(134l,"Branch Manager's Feedback - Report", BRANCH_MANAGER_FEEDBACK_REPORT);
		MenuItem bmfbReportingPattern1 = new MenuItem(135l,"Branch Manager's Feedback Reporting Pattern", BRANCH_MANAGER_FEEDBACK_REPORTING_PATTERN);
		
		//CVC
		MenuItem cvcProcess = new MenuItem(24l , "Claims Audit Group", CVC_PROCESSING);
		MenuItem cvcAudit = new MenuItem(24l, "Claims Audit", CVC_AUDIT);
		MenuItem cvcAuditReport = new MenuItem(24l, "Claims Audit Report", CVC_AUDIT_REPORT);
		MenuItem postProcessCVC = new MenuItem(24l, "Post Claim Process Audit", POST_PROCESS_CVC);
		MenuItem cvcAuditActionProcessing = new MenuItem(24l, "Audit Action Processing", CVC_AUDIT_ACTION_PROCESSING);
		MenuItem cvcAuditQryApproval = new MenuItem(24l, "Claims Audit Query Approval", CVC_AUDIT_QRY_APPRVOAL);
		
		
		cvcProcess.addChild(cvcAudit);
		cvcProcess.addChild(postProcessCVC);
		cvcProcess.addChild(cvcAuditActionProcessing);
		cvcProcess.addChild(cvcAuditQryApproval);
		cvcProcess.addChild(cvcAuditReport);
		
		
		MENU_ITEMS_PERMISSION_MAPPING.put(CVC_PROCESSING, cvcProcess);
		MENU_ITEMS_PERMISSION_MAPPING.put(CVC_AUDIT, cvcAudit);
		MENU_ITEMS_PERMISSION_MAPPING.put(POST_PROCESS_CVC,postProcessCVC);
		MENU_ITEMS_PERMISSION_MAPPING.put(CVC_AUDIT_ACTION_PROCESSING, cvcAuditActionProcessing);
		MENU_ITEMS_PERMISSION_MAPPING.put(CVC_AUDIT_QRY_APPRVOAL, cvcAuditQryApproval);
		MENU_ITEMS_PERMISSION_MAPPING.put(CVC_AUDIT_REPORT, cvcAuditReport);

		
		MENU_ITEMS_PERMISSION_MAPPING.put(CLM_BR_MGR_FEEDBACK, managerFeedbackRoot);
		MENU_ITEMS_PERMISSION_MAPPING.put(MenuPresenter.SHOW_BRANCH_MANAGER_FEEDBACK_HOME_PAGE, managerFeedBackHomeScreen);
		MENU_ITEMS_PERMISSION_MAPPING.put(MANAGER_FEEDBACK, branchMangerFeedbackReply);
		MENU_ITEMS_PERMISSION_MAPPING.put(BRANCH_MANAGER_FEEDBACK_REPORT, bmfbReport1);
		MENU_ITEMS_PERMISSION_MAPPING.put(BRANCH_MANAGER_FEEDBACK_REPORTING_PATTERN, bmfbReportingPattern1);
		MENU_ITEMS_PERMISSION_MAPPING.put(MANAGER_PREVIOUS_FEEDBACK, managerPreviousFeedback);
		managerFeedbackRoot.addChild(managerFeedBackHomeScreen);
		managerFeedbackRoot.addChild(managerPreviousFeedback);
		managerFeedbackRoot.addChild(branchMangerFeedbackReply);
		managerFeedbackRoot.addChild(bmfbReport1);                    // CR R1238 Branch manager Feedback Report
		managerFeedbackRoot.addChild(bmfbReportingPattern1);          // CR R1238 Branch manager Feedback Reporting Pattern

		/*
		 * <<<<<<< HEAD rootMenu.addChild(intimationMenu);
		 * rootMenu.addChild(cashLessMenu); rootMenu.addChild(rembursementMenu);
		 * rootMenu.addChild(searchClaim);
		 * 
		 * =======
		 */

		healthMenu.addChild(registrationMenu);
		healthMenu.addChild(remiderLettersMenu);
		healthMenu.addChild(convertClaimPage);
		// Added logic for cashless screens hide
//		if(bpmClientContext.showCashlessMenu()){
//			healthMenu.addChild(preauthMenu);
//		}
		healthMenu.addChild(processClaimMenu);
		healthMenu.addChild(rembursementMenu);
		
		//Added logic for Action screens visible
		if(bpmClientContext.showActions()){
			rootMenu.addChild(medicalActivities);
			rootMenu.addChild(icacMenu);
			rootMenu.addChild(managerFeedbackRoot);
			rootMenu.addChild(intimationMenu);
			// Added logic for cashless screens hide
			if(bpmClientContext.showCashlessMenu()){
				rootMenu.addChild(healthMenu);
			}
			rootMenu.addChild(fieldVisitMenu);
			rootMenu.addChild(endorsementsMenu);
			// Added logic for cashless screens hide
	//		if(bpmClientContext.showCashlessMenu()){
	//			rootMenu.addChild(specialistProcessingMenu);
	//		}
			//rootMenu.addChild(hrmp);
			rootMenu.addChild(investigation);
			rootMenu.addChild(fvrProcess);
			rootMenu.addChild(paymentProcessor);
			rootMenu.addChild(rewardAndRecognitionCell);
			rootMenu.addChild(talkTalkTalk);
			rootMenu.addChild(researchAnalysisWing);
			rootMenu.addChild(postCashlessCell);
			rootMenu.addChild(hrmAccess);
			rootMenu.addChild(ompmenubarMenu_omp);
	//		rootMenu.addChild(cashLessMenu);
			rootMenu.addChild(rembursementMenu);
			rootMenu.addChild(searchClaim);
	
			rootMenu.addChild(legalmenu);
	
			// rootMenu.addChild(cashLessMenu);
			// rootMenu.addChild(rembursementMenu);
			// rootMenu.addChild(searchClaim);
	
			// cashLessMenu.addChild(registrationMenu);
			// cashLessMenu.addChild(preauthMenu);
			// cashLessMenu.addChild(fieldVisitMenu);
			// cashLessMenu.addChild(endorsementsMenu);
			// cashLessMenu.addChild(processClaimMenu);
			// cashLessMenu.addChild(specialistProcessingMenu);
			// cashLessMenu.addChild(clearCashlesss);
	
			healthMenu.addChild(registrationMenu);
			healthMenu.addChild(remiderLettersMenu);
			healthMenu.addChild(convertClaimPage);
			//Commented the below line for cashlesss menu
	//		healthMenu.addChild(preauthMenu);
			healthMenu.addChild(rembursementMenu);
			healthMenu.addChild(masterMenu);
	
			// cashLessMenu.addChild(searchClaim);
			// Added for star fax simulation
	
			rootMenu.addChild(intimationMenu);
			rootMenu.addChild(healthMenu);
			rootMenu.addChild(specialistProcessingMenu);
			rootMenu.addChild(paClaimMenu);
			rootMenu.addChild(processClaimMenu);
			rootMenu.addChild(fieldVisitMenu);
			rootMenu.addChild(endorsementsMenu);
			rootMenu.addChild(investigation);
			rootMenu.addChild(paymentProcessor);
			rootMenu.addChild(rewardAndRecognitionCell);
			// rootMenu.addChild(searchClaim);
			rootMenu.addChild(starfaxSimulation);
			rootMenu.addChild(cvcProcess);
			// Commenting the below menu
			// rootMenu.addChild(acknowledgeDocReceived);
	
			// rootMenu.addChild(acknowledgeDocReceived);
	
			// rootMenu.addChild(viewEarlierRod);
	
	//		rootMenu.addChild(reportHeader);
	
			// rootMenu.addChild(searchOrUploadDocuments);
			rootMenu.addChild(legalmenu);
			rootMenu.addChild(opScreen);
			rootMenu.addChild(processingPayment);
			// Added logic for cashless screens hide
	//		if(bpmClientContext.showCashlessMenu()){
	//			rootMenu.addChild(processVBcomplaince);
	//		}
			rootMenu.addChild(warHouseMenu);
			rootMenu.addChild(fileDetailsReport);
			rootMenu.addChild(updatePanCard);
			//New Addition for CR 23
			//rootMenu.addChild(hospitalScoringDetails);
			rootMenu.addChild(updateAadharDetails);
			rootMenu.addChild(uploadBedPhoto);	
		}
//		Added logic for Reports only visible
		if(bpmClientContext.showReports()){
			rootMenu.addChild(reportHeader);
		}
	//	rootMenu.addChild(managerFeedbackForm);
		
		//For Reimbursement	
		//R3

		QueryRejectionMenu.addChild(processDraftQueryMenu);
		QueryRejectionMenu.addChild(processDraftRejectionMenu);
		rembursementMenu.addChild(QueryRejectionMenu);

		// rembursementMenu.addChild(investigation);
		rembursementMenu.addChild(rod);

		rembursementMenu.addChild(medicalApproval);
		rembursementMenu.addChild(billing);
		rembursementMenu.addChild(financialApproval);
		rembursementMenu.addChild(queryRejection);
		rembursementMenu.addChild(manageClaims);
		// rembursementMenu.addChild(op_healthCheckProcessing);
		// rembursementMenu.addChild(specialistProcessing);
		// rembursementMenu.addChild(paymentProcessor);
		rembursementMenu.addChild(misc);
		// rembursementMenu.addChild(processClaimMenuR3);
		// rembursementMenu.addChild(addAdditional);
		rembursementMenu.addChild(speicalApprovals);
		// rembursementMenu.addChild(rewardAndRecognitionCell);
		// rembursementMenu.addChild(cancelAckMenu);
		// rembursementMenu.addChild(settlementPullBackMenu);

		// Adding Menu item to the map for permission mapping with the wso2
		MENU_ITEMS_PERMISSION_MAPPING.put(HOME_PAGE, rootMenu);

		intimationMenu.addChild(searchIntimationMenu);
		intimationMenu.addChild(searchViewDetialsMenu);
		intimationMenu.addChild(searchIntimationUnlock);
		intimationMenu.addChild(searchIntimationUnlockBPMNtoDB);
		// intimationMenu.addChild(searchIntimationUnlockBPMNtoDB);
		// TODO: comment this once testing completed
		intimationMenu.addChild(testComponentMenu);
		intimationMenu.addChild(updateHospitalInformationMenu);
		intimationMenu.addChild(editHospitalDetailsMenu);

		MenuItem legalHome = new MenuItem(7l, "Legal Home ", LEGAL_HOME);
		MenuItem cosumerForum = new MenuItem(7l, "Consumer Forum",
				CONSUMER_FORUM);
		MenuItem advocateNotice = new MenuItem(7l, "Lawyer/Advocate Notice",
				ADVOCATE_NOTICE);
		MenuItem advocateFee = new MenuItem(7l, "Advocate Fee", ADVOCATE_FEE);
		MenuItem ombudsman = new MenuItem(7l, "Ombudsman", OMBUDSMAN);
		MENU_ITEMS_PERMISSION_MAPPING.put(LEGAL_HOME, legalHome);
		MENU_ITEMS_PERMISSION_MAPPING.put(CONSUMER_FORUM, cosumerForum);
		MENU_ITEMS_PERMISSION_MAPPING.put(ADVOCATE_NOTICE, advocateNotice);
		MENU_ITEMS_PERMISSION_MAPPING.put(ADVOCATE_FEE, advocateFee);
		MENU_ITEMS_PERMISSION_MAPPING.put(OMBUDSMAN, ombudsman);
		legalmenu.addChild(legalHome);
		legalmenu.addChild(cosumerForum);
		legalmenu.addChild(advocateNotice);
		legalmenu.addChild(advocateFee);
		legalmenu.addChild(ombudsman);

		MenuItem paClaimRegistration = new MenuItem(2L, "Register Claim",
				SEARCH_PA_CLAIM_REGISTER);
		paClaimRegisterMenu.addChild(paClaimRegistration);

		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_PA_CLAIM_REGISTER,
				paClaimRegistration);

		MenuItem paLetterPage = new MenuItem(3L, "Letter Processing",
				PA_LETTER_PAGE);
		MenuItem generatepaCoveringLetter = new MenuItem(3L,
				"Generate Covering Letter", SEARCH_GENERATE_PA_COVERING_LETTER);
		MenuItem generatepaReminderLetterClaimwise = new MenuItem(4L,
				"Generate Reminder Letter(Claim wise Search)",
				GENERATE_PA_REMINDER_LETTER_CLAIM_WISE);
		MenuItem generatePAReminderLetterBulkMenu = new MenuItem(5l,
				"Generate Reminder Letter Bulk",
				GENERATE_PA_REMINDER_LETTER_BULK);
		MenuItem printPAReminderLetterBulkMenu = new MenuItem(6l,
				"Print Reminder Letter (BULK)", PRINT_PA_REMINDER_LETTER_BULK);

		paLetterPage.addChild(generatepaCoveringLetter);
		paLetterPage.addChild(generatepaReminderLetterClaimwise);
		paLetterPage.addChild(generatePAReminderLetterBulkMenu);
		paLetterPage.addChild(printPAReminderLetterBulkMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_LETTER_PAGE, paLetterPage);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_GENERATE_PA_COVERING_LETTER,
				generatepaCoveringLetter);
		MENU_ITEMS_PERMISSION_MAPPING.put(
				GENERATE_PA_REMINDER_LETTER_CLAIM_WISE,
				generatepaReminderLetterClaimwise);
		MENU_ITEMS_PERMISSION_MAPPING.put(GENERATE_PA_REMINDER_LETTER_BULK,
				generatePAReminderLetterBulkMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PRINT_PA_REMINDER_LETTER_BULK,
				printPAReminderLetterBulkMenu);

		MenuItem paConvertClaimPage = new MenuItem(4L,
				"Convert Claim Processing ", PA_CONVERT_CLAIM);
		MenuItem convertPAClaimMenu = new MenuItem(4L,
				"Convert Claim(In process)", SEARCH_CONVERT_PA_CLAIM);
		MenuItem convertPAClaimOutSideMenu = new MenuItem(4L,
				"Convert Claim To Reimbursement(Search Based)",
				CONVERT_PA_CLAIM_OUTSIDE_PROCESS);

		paConvertClaimPage.addChild(convertPAClaimMenu);
		paConvertClaimPage.addChild(convertPAClaimOutSideMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CONVERT_CLAIM, paConvertClaimPage);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_CONVERT_PA_CLAIM,
				convertPAClaimMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(CONVERT_PA_CLAIM_OUTSIDE_PROCESS,
				convertPAClaimOutSideMenu);

		// MenuItem paClaimGenerateReminderClaimwise =

		MenuItem paFieldVisitPaclaimsMenu = new MenuItem(2l, "Field Visit",
				PA_FIELD_VISIT);
		MenuItem paShowPRocessFieldVisit = new MenuItem(2l,
				"Process Field Visit", PA_SHOW_PROCESS_FIELD_VISIT);
		paFieldVisitPaclaimsMenu.addChild(paShowPRocessFieldVisit);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_FIELD_VISIT,
				paFieldVisitPaclaimsMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_SHOW_PROCESS_FIELD_VISIT,
				paShowPRocessFieldVisit);

		MenuItem paMedicalApproval = new MenuItem(6l, "Medical Processing",
				PA_MEDICAL_APPROVAL);
		// MenuItem paProcessClaimRequestMenu = new MenuItem(6l,
		// "Process Claim Request(Zonal Review)",
		// PA_PROCESS_CLAIM_REQUEST_ZONAL_REVIEW);
		MenuItem paProcessClaimRequest = new MenuItem(6l,
				"Process Claim Request - Non Hospitalisation",
				PA_PROCESS_CLAIM_REQUEST);
		MenuItem paHealthProcessClaimRequest = new MenuItem(6l,
				"Process Claim Request - Hospitalisation",
				PA_HEALTH_PROCESS_CLAIM_REQUEST);
		MenuItem paProcessClaimRequestWaitForInput = new MenuItem(6l,
				"Wait for Input  - Non Hospitalisation",
				PA_PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT);
		MenuItem paHealthProcessClaimRequestWaitForInput = new MenuItem(6l,
				"Wait for Input  - Hospitalisation",
				PA_HEALTH_PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT);

		paMedicalApproval.addChild(paProcessClaimRequest);
		paMedicalApproval.addChild(paHealthProcessClaimRequest);
		paMedicalApproval.addChild(paProcessClaimRequestWaitForInput);
		paMedicalApproval.addChild(paHealthProcessClaimRequestWaitForInput);
		paReimbursementProcess.addChild(paMedicalApproval);

		MENU_ITEMS_PERMISSION_MAPPING.put(PA_MEDICAL_APPROVAL,
				paMedicalApproval);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_PROCESS_CLAIM_REQUEST,
				paProcessClaimRequest);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT,
				paProcessClaimRequestWaitForInput);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_HEALTH_PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT,
				paHealthProcessClaimRequestWaitForInput);

		MenuItem PAqueryRejection = new MenuItem(4l, "Query/Rejection",
				PA_QUERY_REJECTION);
		MenuItem PAdraftQueryLetterMenu = new MenuItem(1l,
				"Draft Query Letter", PA_DRAFT_QUERY_LETTER_SEARCH);
		MenuItem PAprocessDraftQueryMenu = new MenuItem(2l,
				"Process Draft Query Letter", PA_PROCESS_DRAFT_QUERY_LETTER);
		MenuItem PAdraftRejectionLetterMenu = new MenuItem(3l,
				"Draft Rejection Letter", PA_DRAFT_REJECTION_LETTER);
		MenuItem PAprocessDraftRejectionMenu = new MenuItem(4l,
				"Process Draft Rejection Letter",
				PA_PROCESS_DRAFT_REJECTION_LETTER);

		MENU_ITEMS_PERMISSION_MAPPING.put(PA_QUERY_REJECTION, PAqueryRejection);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_DRAFT_QUERY_LETTER_SEARCH,
				PAdraftQueryLetterMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_PROCESS_DRAFT_QUERY_LETTER,
				PAprocessDraftQueryMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_DRAFT_REJECTION_LETTER,
				PAdraftRejectionLetterMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_PROCESS_DRAFT_REJECTION_LETTER,
				PAprocessDraftRejectionMenu);

		PAqueryRejection.addChild(PAdraftQueryLetterMenu);
		PAqueryRejection.addChild(PAprocessDraftQueryMenu);
		PAqueryRejection.addChild(PAdraftRejectionLetterMenu);
		PAqueryRejection.addChild(PAprocessDraftRejectionMenu);

		PAQueryRejectionMenu.addChild(PAprocessDraftQueryMenu);
		PAQueryRejectionMenu.addChild(PAprocessDraftRejectionMenu);
		paClaimMenu.addChild(PAQueryRejectionMenu);

		// paReimbursementProcess.addChild(PAqueryRejection);

		MENU_ITEMS_PERMISSION_MAPPING.put(PA_HEALTH_PROCESS_CLAIM_REQUEST,
				paHealthProcessClaimRequest);

		MenuItem paBilling = new MenuItem(7l, "Billing Processing", PA_BILLING);
		MenuItem paProcessClaimBilling = new MenuItem(7l,
				"Process Claim Billing - Non Hospitalisation",
				PA_PROCESS_CLAIM_BILLING);
		MenuItem paHealthProcessClaimBilling = new MenuItem(6l,
				"Process Claim Billing - Hospitalisation",
				PA_HEALTH_PROCESS_CLAIM_BILLING);

		MENU_ITEMS_PERMISSION_MAPPING.put(PA_BILLING, paBilling);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_PROCESS_CLAIM_BILLING,
				paProcessClaimBilling);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_HEALTH_PROCESS_CLAIM_BILLING,
				paHealthProcessClaimBilling);

		paBilling.addChild(paProcessClaimBilling);
		paBilling.addChild(paHealthProcessClaimBilling);

		paReimbursementProcess.addChild(paBilling);

		MenuItem paFinancial = new MenuItem(7l, "Financial Approval",
				PA_FINANCIAL);
		MenuItem paProcessClaimFinancial = new MenuItem(7l,
				"Process Claim Financials - Non Hospitalisation",
				PA_NON_HOSP_PROCESS_CLAIM_FINANCIAL);
		MenuItem paHealthProcessClaimFinancial = new MenuItem(6l,
				"Process Claim Financial - Hospitalisation",
				PA_HEALTH_PROCESS_CLAIM_FINANCIALS);

		MenuItem paClaimApproval = new MenuItem(7l, "Claim Approval ",
				PA_CLAIM_APPROVAL);
		MenuItem paProcessClaimAprNonHosp = new MenuItem(9l,
				"Process claim Approval", PA_CLAIM_APPROVAL_NON_HOS);

		paClaimApproval.addChild(paProcessClaimAprNonHosp);

		paFinancial.addChild(paProcessClaimFinancial);
		paFinancial.addChild(paHealthProcessClaimFinancial);

		paReimbursementProcess.addChild(paClaimApproval);
		paReimbursementProcess.addChild(paFinancial);

		paReimbursementProcess.addChild(PAqueryRejection);

		MENU_ITEMS_PERMISSION_MAPPING.put(PA_FINANCIAL, paFinancial);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_NON_HOSP_PROCESS_CLAIM_FINANCIAL,
				paProcessClaimFinancial);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CLAIM_APPROVAL, paClaimApproval);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CLAIM_APPROVAL_NON_HOS,
				paProcessClaimAprNonHosp);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_HEALTH_PROCESS_CLAIM_FINANCIALS,
				paHealthProcessClaimFinancial);

		MenuItem paManageClaims = new MenuItem(9l, "Manage Claims",
				PA_MANAGE_CLAIMS);
		MenuItem paReOpenClaim = new MenuItem(
				1l,
				"Re-Open Claim - ROD Level(Search Based) - Non Hospitalisation",
				PA_RE_OPEN_CLAIM_ROD_LEVEL);
		MenuItem pasearchInternalNotes = new MenuItem(12l, "Add Doctor Note",
				SEARCH_INTERNAL_NOTES_PA);
		MenuItem paHospReOpenClaim = new MenuItem(2l,
				"Re-Open Claim - ROD Level(Search Based) - Hospitalisation",
				PA_HOSP_RE_OPEN_CLAIM_ROD_LEVEL);

		MenuItem paCloseClaimInProcess = new MenuItem(3l,
				"Close Claim - ROD Level(In Process) - Non Hospitalisation",
				PA_CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL);
		MenuItem paHospCloseClaimInProcess = new MenuItem(4l,
				"Close Claim - ROD Level(In Process) - Hospitalisation",
				PA_HOSP_CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL);

		MenuItem paCloseClaim = new MenuItem(5l,
				"Close Claim - ROD Level(Search Based) - Non Hospitalisation",
				PA_CLOSE_CLAIM_ROD_LEVEL);
		MenuItem paHospCloseClaim = new MenuItem(6l,
				"Close Claim - ROD Level(Search Based) - Hospitalisation",
				PA_HOSP_CLOSE_CLAIM_ROD_LEVEL);

		MenuItem paCloseClaimClaimLevel = new MenuItem(
				7l,
				"Close Claim - Claim Level (Search Based) - Non Hospitalisation",
				PA_CLOSE_CLAIM_CLAIM_LEVEL);
		MenuItem paHospCloseClaimClaimLevel = new MenuItem(8l,
				"Close Claim - Claim Level (Search Based) - Hospitalisation",
				PA_HOSP_CLOSE_CLAIM_CLAIM_LEVEL);

		MenuItem paReOpenClaimClaimLevel = new MenuItem(
				9l,
				"Re-Open Claim - Claim Level (Search Based) - Non Hospitalisation",
				PA_RE_OPEN_CLAIM_CLAIM_LEVEL);
		MenuItem paHospReOpenClaimClaimLevel = new MenuItem(10l,
				"Re-Open Claim - Claim Level (Search Based) - Hospitalisation",
				PA_HOSP_RE_OPEN_CLAIM_CLAIM_LEVEL);

		MenuItem paCancelAckMenu = new MenuItem(25l, "Cancel Acknowledgment",
				PA_CANCEL_ACKNOWLEDGEMENT);
		MenuItem paSettlementPullBackMenu = new MenuItem(26l,
				"UNDO FA - Non Hospitalisation", PA_SETTLEMENT_PULL_BACK);
		MenuItem paHospSettlementPullBackMenu = new MenuItem(27l,
				"UNDO FA - Hospitalisation", PA_HOSP_SETTLEMENT_PULL_BACK);

		MenuItem paSearchIntimationUnlock = new MenuItem(5l,
				"Intimation Unlock", PA_UNLOCK_INTIMATION);
		MenuItem gpaUnnamedRiskDetails = new MenuItem(6l, "Category Updation",
				GPA_UNNAMED_RISK_DETAILS);
		MenuItem paSkipZMR = new MenuItem(2l, "Master Screen-Skip ZMR",
				PA_CPU_SKIP_ZMR);

		MENU_ITEMS_PERMISSION_MAPPING.put(PA_MANAGE_CLAIMS, paManageClaims);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_RE_OPEN_CLAIM_ROD_LEVEL,
				paReOpenClaim);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_INTERNAL_NOTES_PA,
				pasearchInternalNotes);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_HOSP_RE_OPEN_CLAIM_ROD_LEVEL,
				paHospReOpenClaim);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL,
				paCloseClaimInProcess);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CLOSE_CLAIM_ROD_LEVEL,
				paCloseClaim);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CLOSE_CLAIM_CLAIM_LEVEL,
				paCloseClaimClaimLevel);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_RE_OPEN_CLAIM_CLAIM_LEVEL,
				paReOpenClaimClaimLevel);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_HOSP_RE_OPEN_CLAIM_CLAIM_LEVEL,
				paHospReOpenClaimClaimLevel);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CANCEL_ACKNOWLEDGEMENT,
				paCancelAckMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_SETTLEMENT_PULL_BACK,
				paSettlementPullBackMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_HOSP_SETTLEMENT_PULL_BACK,
				paHospSettlementPullBackMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_UNLOCK_INTIMATION,
				paSearchIntimationUnlock);
		MENU_ITEMS_PERMISSION_MAPPING.put(GPA_UNNAMED_RISK_DETAILS,
				gpaUnnamedRiskDetails);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CPU_SKIP_ZMR, paSkipZMR);

		paManageClaims.addChild(paReOpenClaim);
		paManageClaims.addChild(pasearchInternalNotes);
		paManageClaims.addChild(paHospReOpenClaim);
		paManageClaims.addChild(paReOpenClaimClaimLevel);
		paManageClaims.addChild(paHospReOpenClaimClaimLevel);
		paManageClaims.addChild(paCloseClaimInProcess);
		paManageClaims.addChild(paHospCloseClaimInProcess);
		paManageClaims.addChild(paCloseClaim);
		paManageClaims.addChild(paHospCloseClaim);
		paManageClaims.addChild(paCloseClaimClaimLevel);
		paManageClaims.addChild(paHospCloseClaimClaimLevel);
		paManageClaims.addChild(paCancelAckMenu);
		paManageClaims.addChild(paSettlementPullBackMenu);
		paManageClaims.addChild(paHospSettlementPullBackMenu);
		paManageClaims.addChild(paSkipZMR);
		paManageClaims.addChild(paSearchIntimationUnlock);
		paManageClaims.addChild(gpaUnnamedRiskDetails);

		paReimbursementProcess.addChild(paManageClaims);

		// paMedicalApproval.addChild(paProcessClaimRequestMenu);
		MenuItem papreauthMenu = new MenuItem(10l, "Cashless Processing",
				PA_CASHLESS_PROCESS_HEADER);
		MenuItem paprocessPreauthRejectMenu = new MenuItem(10L,
				"Process Rejection", PA_PROCESS_PREAUTH_REJECTION);
		MenuItem firstLevelPremedicalMenu = new MenuItem(11L,
				"First Level Processing (Pre-auth)",
				PA_FIRST_LEVEL_PROCESSING_PREMEDICAL);
		MenuItem firstLevelEnhMenu = new MenuItem(12L,
				"First Level Processing (Enhancement)",
				PA_FIRST_LEVEL_PROCESSING_PREMEDICAL_ENHANCEMENT);
		MenuItem processPreauthMenu = new MenuItem(13L, "Process Pre-Auth",
				PA_PROCESS_PREAUTH);
		MenuItem processEnhMenu = new MenuItem(14L, "Process Enhancement",
				PA_PROCESS_ENHANCEMENT);
		MenuItem standaloneWithdrawMenu = new MenuItem(15L,
				"Withdraw Pre-Auth", PA_STANDALONE_WITHDRAW);
		MenuItem standaloneDownsizeMenu = new MenuItem(16L,
				"Downsize Pre-Auth", PA_STANDALONE_DOWNSIZE);
		MenuItem processDownsizeMenu = new MenuItem(16L,
				"Process Downsize Request", PA_PROCESS_DOWNSIZE);
		MenuItem paClearCashlesss = new MenuItem(9l, "PA Clear Cashless",
				PA_CLEAR_CASHLESS);// new

		papreauthMenu.addChild(paprocessPreauthRejectMenu);
		papreauthMenu.addChild(firstLevelPremedicalMenu);
		papreauthMenu.addChild(processPreauthMenu);
		papreauthMenu.addChild(firstLevelEnhMenu);
		papreauthMenu.addChild(processEnhMenu);
		papreauthMenu.addChild(standaloneWithdrawMenu);
		papreauthMenu.addChild(standaloneDownsizeMenu);
		papreauthMenu.addChild(processDownsizeMenu);
		papreauthMenu.addChild(paClearCashlesss);

		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CASHLESS_PROCESS_HEADER,
				papreauthMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_PROCESS_PREAUTH_REJECTION,
				paprocessPreauthRejectMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_FIRST_LEVEL_PROCESSING_PREMEDICAL,
				firstLevelPremedicalMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(
				PA_FIRST_LEVEL_PROCESSING_PREMEDICAL_ENHANCEMENT,
				firstLevelEnhMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_PROCESS_PREAUTH,
				processPreauthMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_PROCESS_ENHANCEMENT,
				processEnhMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_STANDALONE_WITHDRAW,
				standaloneWithdrawMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_STANDALONE_DOWNSIZE,
				standaloneDownsizeMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_PROCESS_DOWNSIZE,
				processDownsizeMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(PA_CLEAR_CASHLESS, paClearCashlesss);

		paClaimMenu.addChild(paClaimRegisterMenu);
		paClaimMenu.addChild(paLetterPage);
		// Added logic for cashless screens hide
//		if(bpmClientContext.showCashlessMenu()){
//			paClaimMenu.addChild(paConvertClaimPage);
//			paClaimMenu.addChild(papreauthMenu);
//		}
		paClaimMenu.addChild(paReimbursementProcess);
		// paClaimMenu.addChild(paFieldVisitPaclaimsMenu);

		// lumenMenu
		MenuItem lumenMenu = new MenuItem(80l, "Lumen", LUMEN_HEADER);

		MenuItem searchLumenRequest = new MenuItem(81l, "Search Lumen Request", SEARCH_LUMEN);
		MenuItem initiateLumenRequest = new MenuItem(82l, "Initiate Lumen Request", CREATE_LUMEN);
		
		MenuItem lumenRequestLevel_I = new MenuItem(83l, "Process Lumen Request - Level I", LUMEN_REQUEST_LEVEL_I);
		MenuItem lumenRequestCoordinator = new MenuItem(84l, "Process Co-Ordinator(Lumen)", LUMEN_REQUEST_CO_ORDINATOR);
		MenuItem lumenRequestLevel_II = new MenuItem(85l, "Process Lumen Request - Level II", LUMEN_REQUEST_LEVEL_II);
		MenuItem queryToMIS = new MenuItem(86l, "Lumen Query Initiated to MIS", LUMEN_REQUEST_MIS);
		MenuItem initiatorQueryCase = new MenuItem(87l, "Initiator (Query Cases)", LUMEN_REQUEST_INITIATOR);
		MenuItem lumenStatusWiseReport = new MenuItem(889l, "Lumen - Status Wise Report", LUMEN_STATUS_WISE_REPORT);
		//Branch manager's feedback				
		//MenuItem managerFeedback = new MenuItem(8892, "Branch Manager's Feedback", MANAGER_FEEDBACK);	
		
		lumenMenu.addChild(searchLumenRequest);
		lumenMenu.addChild(initiateLumenRequest);
		lumenMenu.addChild(lumenRequestLevel_I);
		lumenMenu.addChild(lumenRequestCoordinator);
		lumenMenu.addChild(lumenRequestLevel_II);
		lumenMenu.addChild(queryToMIS);
		lumenMenu.addChild(initiatorQueryCase);
		lumenMenu.addChild(lumenStatusWiseReport);

		MENU_ITEMS_PERMISSION_MAPPING.put(LUMEN_HEADER, lumenMenu);
		MENU_ITEMS_PERMISSION_MAPPING.put(SEARCH_LUMEN, searchLumenRequest);
		MENU_ITEMS_PERMISSION_MAPPING.put(CREATE_LUMEN, initiateLumenRequest);
		MENU_ITEMS_PERMISSION_MAPPING.put(LUMEN_REQUEST_LEVEL_I,
				lumenRequestLevel_I);
		MENU_ITEMS_PERMISSION_MAPPING.put(LUMEN_REQUEST_CO_ORDINATOR,
				lumenRequestCoordinator);
		MENU_ITEMS_PERMISSION_MAPPING.put(LUMEN_REQUEST_LEVEL_II,
				lumenRequestLevel_II);
		MENU_ITEMS_PERMISSION_MAPPING.put(LUMEN_REQUEST_MIS, queryToMIS);

		MENU_ITEMS_PERMISSION_MAPPING.put(LUMEN_REQUEST_INITIATOR,
				initiatorQueryCase);
		MENU_ITEMS_PERMISSION_MAPPING.put(LUMEN_STATUS_WISE_REPORT,
				lumenStatusWiseReport);

		// lumen
		if(bpmClientContext.showActions()){
			rootMenu.addChild(lumenMenu);
			rootMenu.addChild(op_healthCheckProcessing);
		}
	}
		
	
	private static final String CLAIMS_HEAD = "CLAIMS_HEAD";
	private static final String CASHLESS_PROCESSING_HEAD = "CASHLESS_PROCESSING_HEAD";
	private static final String CASHLESS_CHIEF_MEDICAL_OFFICER = "CASHLESS_CHIEF_MEDICAL_OFFICER";
	private static final String SENIOR_MEDICAL_APPROVER = "SENIOR_MEDICAL_APPROVER";
	private static final String MEDICAL_APPROVER = "MEDICAL_APPROVER";
	private static final String CLS_SETTLEMENT_MEDICAL_APPRO = "CLS_SETTLEMENT_MEDICAL_APPRO";
	private static final String SPECIALIST_DOCTOR = "SPECIALIST_DOCTOR";
	private static final String FIELD_VISIT_HEAD = "FIELD_VISIT_HEAD";
	private static final String REGISTRATION_TEAM_LEAD = "REGISTRATION_TEAM_LEAD";
	private static final String REGISTRATION_OFFICER = "REGISTRATION_OFFICER";
	private static final String ROD_TEAM_LEAD = "ROD_TEAM_LEAD";
	private static final String ROD_OFFICER = "ROD_OFFICER";
	private static final String CASHLESS_CLAIMS_CO_ORDINATOR = "CASHLESS_CLAIMS_CO_ORDINATOR";
	private static final String CUSTOMER_CARE_REPRESENTATIVE = "CUSTOMER_CARE_REPRESENTATIVE";
	private static final String HMS_PMS = "HMS_PMS";
	private static final String PRE_MEDICAL = "PRE_MEDICAL";
	private static final String NON_MEDICAL_REJECTION = "NON_MEDICAL_REJECTION";
	private static final String SEND_TO_PROCESSING_NON_MEDICAL = "SEND_TO_PROCESSING_NON_MEDICAL";
	private static final String CLAIMS_ACCOUNTS_OFFICER = "CLAIMS_ACCOUNTS_OFFICER";
	private static final String SENIOR_CLAIMS_ACCOUNTS_OFFICER = "SENIOR_CLAIMS_ACCOUNTS_OFFICER";
	private static final String PED_PROCESSOR = "PED_PROCESSOR";
	private static final String PED_APPROVER = "PED_APPROVER";
	private static final String ZONAL_CLAIMS_HEAD = "ZONAL_CLAIMS_HEAD";
	private static final String ZONAL_MEDICAL_HEAD = "ZONAL_MEDICAL_HEAD";
	private static final String ZONAL_SENIOR_MEDICAL_APPROVER = "ZONAL_SENIOR_MEDICAL_APPROVER";
	private static final String ZONAL_MEDICAL_APPROVER = "ZONAL_MEDICAL_APPROVER";
	private static final String FIELD_VISIT_DOCTOR_PERSONNEL = "FIELD_VISIT_DOCTOR_PERSONNEL";
	private static final String ZONAL_FIELD_VISIT_CO_ORDINATOR = "ZONAL_FIELD_VISIT_CO_ORDINATOR";
	private static final String ZONAL_REGISTRATION_TEAM_LEAD = "ZONAL_REGISTRATION_TEAM_LEAD";
	private static final String ZONAL_REGISTRATION_OFFICER = "ZONAL_REGISTRATION_OFFICER";
	private static final String ZONAL_HOSPITAL_CO_ORDINATOR = "ZONAL_HOSPITAL_CO_ORDINATOR";
	private static final String ZONAL_CLS_CLAIMS_COORDINATOR = "ZONAL_CLS_CLAIMS_COORDINATOR";
	private static final String ZONAL_QUALITY_CLAIMS_AUDITOR = "ZONAL_QUALITY_CLAIMS_AUDITOR";
	private static final String REIMBURSEMENT_PROCESSING_HEAD = "REIMBURSEMENT_PROCESSING_HEAD";
	private static final String REIMBURSEMENT_CHIEF_MEDICAL_OFFICER = "RBM_CHIEF_MEDICAL_OFFICER";
	private static final String QUERY_APPROVER = "QUERY_APPROVER";
	private static final String QUERY_DRAFTSPERSON = "QUERY_DRAFTSPERSON";
	private static final String REIMBURSEMENT_CLAIMS_CO_ORDINATOR = "RBM_CLAIMS_COORDINATOR";
	private static final String CLAIMS_BILLING_OFFICER = "CLAIMS_BILLING_OFFICER";
	private static final String CLAIMS_FINANCIAL_APPROVER = "CLAIMS_FINANCIAL_APPROVER";
	private static final String QUALITY_CLAIMS_AUDITOR = "";
	private static final String INVESTIGATION_APPROVER = "INVESTIGATION_APPROVER";
	private static final String INVESTIGATION_DRAFTSPERSON = "INVESTIGATION_DRAFTSPERSON";
	private static final String ZONAL_REIMBURSEMENT_CLAIMS_CO_ORDINATOR = "ZONAL_REIMBURSEMENT_CLAIMS_CO_ORDINATOR";
	private static final String OPR_OFFICE_CLAIMS_CO_ORDINATOR = "OPR_OFFICE_CLAIMS_COORDINATOR";
	private static final String REJECTION_APPROVER = "REJECTION_APPROVER";
	private static final String REJECTION_DRAFTSPERSON = "REJECTION_DRAFTSPERSON";
	private static final String CLAIMS_ADMIN = "CLAIMS_ADMIN";
	private static final String REPRESENTATIVE = "REPRESENTATIVE";
	private static final String BRANCH_OFFICE = "BRANCH_OFFICE";
	private static final String AREA_OFFICE = "AREA_OFFICE";
	private static final String RRC_PROCESSOR = "RRC_PROCESSOR";
	private static final String RRC_REVIEWER = "RRC_REVIEWER";

	// Newly added role for Reports
	private static final String REPORT_GROUP = "REPORT_GROUP";

	private static final String REMINDER_OFFICER = "REMINDER_OFFICER";

	private static final String ZONAL_ROD_OFFICER = "ZONAL_ROD_OFFICER";

	private static final String CMA_MEDICAL_APPROVER = "CMA_MEDICAL_APPROVER";

	private static final String RMA_MEDICAL_APPROVER = "RMA_MEDICAL_APPROVER";

	private static final String AMA_MEDICAL_APPROVER = "AMA_MEDICAL_APPROVER";

	static {
		List<String> registrationOfficerMenu = new ArrayList<String>();

		registrationOfficerMenu.add(HEALTH_PAGE);

		registrationOfficerMenu.add(CASHLESS_PAGE);

		registrationOfficerMenu.add(INTIMATION_HEADER);
		// registrationOfficerMenu.add(REIMBURSEMENT_PAGE);
		registrationOfficerMenu.add(REGISTER_CLAIM_HEADER);
		registrationOfficerMenu.add(SEARCH_REGISTER_CLAIM);
		registrationOfficerMenu.add(GENERATE_COVERINGLETTER);
		// registrationOfficerMenu.add(CONVERT_CLAIM);
		// registrationOfficerMenu.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
		// registrationOfficerMenu.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);
		// registrationOfficerMenu.add(SEARCH_POLICY);
		registrationOfficerMenu.add(SEARCH_CLAIM);
		// registrationOfficerMenu.add(SEARCH_INTIMATION);
		// registrationOfficerMenu.add(UPDATE_HOSPITAL_INFORMATION);
		// registrationOfficerMenu.add(EDIT_HOSPITAL_INFORMATION);
		// ============================================================================

		List<String> claimHeadMenu = new ArrayList<String>();
		//----------CLS----------------

				claimHeadMenu.add(HEALTH_PAGE);

				claimHeadMenu.add(CASHLESS_PAGE);
				
				claimHeadMenu.add(HEALTH_REPORT);
				claimHeadMenu.add(PA_CLAIMS);
				claimHeadMenu.add(SEARCH_CLAIM_POLICY_REPORT);
				claimHeadMenu.add(EXECUTIVE_STATUS_REPORT);
				claimHeadMenu.add(EXECUTIVE_STATUS_SUMMARY_REPORT);
				claimHeadMenu.add(SEARCH_PREAUTH_CPUWISE_REPORT);
				claimHeadMenu.add(CLAIM_STATUS_CPUWISE_REPORT);
				claimHeadMenu.add(HELP_DESK_STATUS_REPORT);
				claimHeadMenu.add(CLAIMS_DAILY_REPORT);
				claimHeadMenu.add(HOSPITAL_WISE_REPORT);
				claimHeadMenu.add(CALL_CENTER_DASH_BOARD);
				claimHeadMenu.add(AUTOMATION_DASHBOARD);
				claimHeadMenu.add(BILL_RECIECVED_STATUS_REPORT);
				claimHeadMenu.add(ADMINISTRATIVE_DASH_BOARD);
				claimHeadMenu.add(PLANNED_ADMISSION_REPORT);
				claimHeadMenu.add(INTIMATED_RISK_DETAILS_REPORT);
				claimHeadMenu.add(MEDICAL_AUDIT_CLAIM_STATUS_REPORT);
				claimHeadMenu.add(INTIMATION_ALTERNATE_CPU_REPORT);
				claimHeadMenu.add(MEDICAL_AUDIT_CASHLESS_ISSUANCE_REPORT);
				claimHeadMenu.add(PAYMENT_PROCESS_REPORT);
				claimHeadMenu.add(FVR_ASSIGNMENT_REPORT);
				claimHeadMenu.add(HOSPITAL_INTIMATION_STATUS);
				claimHeadMenu.add(MEDICAL_MAIL_STATUS);
				claimHeadMenu.add(DAILY_REPORT);
				claimHeadMenu.add(CPU_WISE_PERFORMANCE_REPORT);
				claimHeadMenu.add(AGENT_BROKER_REPORT);
				claimHeadMenu.add(OP_CLAIM_REPORT);
			//	claimHeadMenu.add(CREATE_LOT_MAKER);
				
				
		//---------Payment Process Cpu----------
				
				claimHeadMenu.add(PAYMENT_PROCESS_CPU);
//				claimHeadMenu.add(SEARCH_OR_UPLOAD_DOCUMENTS);
				claimHeadMenu.add(OP_SCREEN);
				claimHeadMenu.add(PROCESSING_PAYMENT);
				
				claimHeadMenu.add(CANCEL_ACKNOWLEDGEMENT);
				
				
				claimHeadMenu.add(INTIMATION_HEADER);
				//Remove this is disable test component
				//TODO: remove for protection
//				claimHeadMenu.add(TEST_COMPONENT);
				claimHeadMenu.add(SEARCH_POLICY);
				claimHeadMenu.add(SEARCH_INTIMATION);
				claimHeadMenu.add(INTIMATION_VIEW_DETAILS);
				claimHeadMenu.add(UPDATE_HOSPITAL_INFORMATION);
				claimHeadMenu.add(EDIT_HOSPITAL_INFORMATION);
				claimHeadMenu.add(OMP_MAIN_MENUBAR);
				claimHeadMenu.add(REGISTER_CLAIM_HEADER);
				claimHeadMenu.add(SEARCH_REGISTER_CLAIM);
				claimHeadMenu.add(REMINDER_LETTERS);       //sdfasdfasdfasdf
				claimHeadMenu.add(GENERATE_REMINDER_LETTER_CLAIM_WISE);
				claimHeadMenu.add(GENERATE_REMINDER_LETTER_BULK);
				claimHeadMenu.add(GENERATE_COVERINGLETTER);
				claimHeadMenu.add(CONVERT_CLAIM);
				claimHeadMenu.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
				claimHeadMenu.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);
				
				
				claimHeadMenu.add(CASHLESS_PROCESS_HEADER);
				claimHeadMenu.add(PROCESS_PREAUTH_REJECTION);
				claimHeadMenu.add(PROCESS_PRE_MEDICAL);
				claimHeadMenu.add(PRE_MEDICAL_PROCESSING_ENHANCEMENT);
				claimHeadMenu.add(PROCESS_PREAUTH);
				claimHeadMenu.add(PROCESS_PREAUTH_AUTO_ALLOCATION);
				claimHeadMenu.add(PROCESS_ENHANCEMENT);
				claimHeadMenu.add(PROCESS_PED_QUERY);
				claimHeadMenu.add(DOWNSIZE_PRE_AUTH);
				claimHeadMenu.add(WITHDRAW_PRE_AUTH);
				claimHeadMenu.add(DOWNSIZE_PRE_AUTH_REQUEST);
					
				claimHeadMenu.add(FIELD_VISIT_CASHLESS);
				claimHeadMenu.add(FIELD_VISIT_REPESENTATION);
				
				claimHeadMenu.add(ENDORSEMENTS_HEADER);
				claimHeadMenu.add(PROCESS_PED_REQUEST_PROCESS);
				claimHeadMenu.add(PROCESS_PED_REQUEST_APPROVE);
				
				claimHeadMenu.add(PROCESS_CLAIM_HEADER);
				claimHeadMenu.add(UPLOAD_TRANSLATED_DOCUMENTS);
				claimHeadMenu.add(DATA_CODING_DATA_CORRECTION);	
				claimHeadMenu.add(HOLD_MONITOR_SCREEN);	
				claimHeadMenu.add(HOLD_MONITOR_SCREEN_FOR_MA);	
				claimHeadMenu.add(HOLD_MONITOR_SCREEN_FOR_BILLING);	
				claimHeadMenu.add(FLAG_RECONSIDERATION_REQUEST);
				claimHeadMenu.add(DATA_CODING_DATA_CORRECTION_HISTORICAL);	
				claimHeadMenu.add(DATA_CODING_DATA_CORRECTION_PRIORITY);
				claimHeadMenu.add(SPECIALIST_PROCESSING_HEADER);
				claimHeadMenu.add(ADVISE_ON_PED);
				claimHeadMenu.add(SUBMIT_SPECIALLIST_ADVISE);
				
				claimHeadMenu.add(SEARCH_CLAIM);
				claimHeadMenu.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
				claimHeadMenu.add(PROCESS_FLP_AUTO_ALLOCATION);
				
				
				//-----------------R3----------------------
				
				claimHeadMenu.add(REIMBURSEMENT_PAGE);
				
				claimHeadMenu.add(SHOW_INVESTIGATION);
				claimHeadMenu.add(SEARCH_PROCESS_INVESTIGATION_INITIATED);
				claimHeadMenu.add(SHOW_DRAFT_INVESTIGATION);
				claimHeadMenu.add(SHOW_ASSIGN_INVESTIGATION);
				claimHeadMenu.add(SEARCH_ACKNOWLEDGE_INVESTIGATION_COMPLETED);
				claimHeadMenu.add(SEARCH_INVESTIGATION_GRADING);
				
				claimHeadMenu.add(ROD);
				claimHeadMenu.add(SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED);
				claimHeadMenu.add(CREATE_ROD);
				claimHeadMenu.add(CREATE_ONLINE_ROD);
				claimHeadMenu.add(ENTER_BILL_DETAILS);
				claimHeadMenu.add(UPLOAD_INVESTIGATION_REPORT);
				claimHeadMenu.add(ADD_ADDITIONAL_DOCUMENT_PAYMENT_INFORMATION);
				
				claimHeadMenu.add(QUERY_REJECTION);
				claimHeadMenu.add(DRAFT_QUERY_LETTER_SEARCH);
				claimHeadMenu.add(PROCESS_DRAFT_QUERY_LETTER);
//				claimHeadMenu.add(GENERATE_REMINDER_LETTER_CLAIMWISE);
				claimHeadMenu.add(DRAFT_REJECTION_LETTER);
				claimHeadMenu.add(PROCESS_DRAFT_REJECTION_LETTER);
				
				claimHeadMenu.add(SPECIALIST_PROCESSING);
				claimHeadMenu.add(SUBMIT_SPECIALIST_ADVISE);
				
				claimHeadMenu.add(MEDICAL_APPROVAL);
				claimHeadMenu.add(PROCESS_CLAIM_REQUEST_ZONAL_REVIEW);
				claimHeadMenu.add(PROCESS_CLAIM_REQUEST);
				claimHeadMenu.add(PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION);
				
				claimHeadMenu.add(BILLING);
				claimHeadMenu.add(PROCESS_CLAIM_BILLING);
				claimHeadMenu.add(PROCESS_CLAIM_BILLING_AUTO_ALLOCATION);
				claimHeadMenu.add(PROCESS_CLAIM_REQUEST_BENEFITSBILLING);
				
				claimHeadMenu.add(FINANCIAL_APPROVAL);
				claimHeadMenu.add(PROCESS_CLAIM_FINANCIALS);
				claimHeadMenu.add(PROCESS_CLAIM_REQUEST_BENEFITSFINANCIAL);
				
				claimHeadMenu.add(MANAGE_CLAIMS);
				claimHeadMenu.add(RE_OPEN_CLAIM_ROD_LEVEL);
				claimHeadMenu.add(CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL);
				claimHeadMenu.add(CLOSE_CLAIM_ROD_LEVEL);
				claimHeadMenu.add(CLOSE_CLAIM_CLAIM_LEVEL);
				claimHeadMenu.add(RE_OPEN_CLAIM_CLAIM_LEVEL);
				claimHeadMenu.add(ALLOW_RECONSIDERATION);
				claimHeadMenu.add(CLAIM_WISE_ALLOW_APPROVAL);
				claimHeadMenu.add(UPDATE_ROD_DETAILS);
				
				claimHeadMenu.add(PAYMENT_PROCESSOR);
				claimHeadMenu.add(MISC);
				claimHeadMenu.add(REGISTER_CLAIM_REFUND);
				claimHeadMenu.add(PROCESS_CLAIM_REFUND);
				claimHeadMenu.add(USER_ACCESS_ALLOCATION);
				claimHeadMenu.add(USER_MANAGEMENT);
				claimHeadMenu.add(USER_RE_ALLOCATION);
				claimHeadMenu.add(CPU_AUTO_ALLOCATION);
				
				claimHeadMenu.add(ADD_ADDITIONAL_DOC);
				claimHeadMenu.add(PHYSICAL_DOCUMENT);
				claimHeadMenu.add(PHYSICAL_DOCUMENT_CHECKER);
				
				claimHeadMenu.add(REWARD_RECOGNITION_CELL);
				claimHeadMenu.add(TALK_TALK_TALK);
		
				claimHeadMenu.add(SPECIAL_APPROVALS);
				claimHeadMenu.add(APPROVE_CLAIM);
				claimHeadMenu.add(PROCESS_CLAIM);
				
				//lumen
				claimHeadMenu.add(LUMEN_HEADER);
				claimHeadMenu.add(OP_HEALTH_CHECK_PROCESSING);
				//claimHeadMenu.add(EXPIRED_POLICIES_CLAIM_OP);
				claimHeadMenu.add(RESEARCH_ANALYSIS_WING_PROCESSING);
			//	claimHeadMenu.add(MANAGER_FEEDBACK);
				
		
/*		List<String> cashless_processing_head = new ArrayList<String>();
		//----------CLS----------------
	
		cashless_processing_head.add(HEALTH_PAGE);
>>>>>>> 39366ce448e44155c696cd39af09ce82ffb2a6f8*/
		
		// ----------CLS----------------

		claimHeadMenu.add(HEALTH_PAGE);

		claimHeadMenu.add(CASHLESS_PAGE);

		claimHeadMenu.add(HEALTH_REPORT);
		claimHeadMenu.add(PA_CLAIMS);
		claimHeadMenu.add(SEARCH_CLAIM_POLICY_REPORT);
		claimHeadMenu.add(EXECUTIVE_STATUS_REPORT);
		claimHeadMenu.add(EXECUTIVE_STATUS_SUMMARY_REPORT);
		claimHeadMenu.add(SEARCH_PREAUTH_CPUWISE_REPORT);
		claimHeadMenu.add(CLAIM_STATUS_CPUWISE_REPORT);
		claimHeadMenu.add(HELP_DESK_STATUS_REPORT);
		claimHeadMenu.add(CLAIMS_DAILY_REPORT);
		claimHeadMenu.add(HOSPITAL_WISE_REPORT);
		claimHeadMenu.add(CALL_CENTER_DASH_BOARD);
		claimHeadMenu.add(AUTOMATION_DASHBOARD);
		claimHeadMenu.add(BILL_RECIECVED_STATUS_REPORT);
		claimHeadMenu.add(ADMINISTRATIVE_DASH_BOARD);
		claimHeadMenu.add(PLANNED_ADMISSION_REPORT);
		claimHeadMenu.add(INTIMATED_RISK_DETAILS_REPORT);
		claimHeadMenu.add(MEDICAL_AUDIT_CLAIM_STATUS_REPORT);
		claimHeadMenu.add(INTIMATION_ALTERNATE_CPU_REPORT);
		claimHeadMenu.add(MEDICAL_AUDIT_CASHLESS_ISSUANCE_REPORT);
		claimHeadMenu.add(PAYMENT_PROCESS_REPORT);
		claimHeadMenu.add(FVR_ASSIGNMENT_REPORT);
		claimHeadMenu.add(HOSPITAL_INTIMATION_STATUS);
		claimHeadMenu.add(MEDICAL_MAIL_STATUS);
		claimHeadMenu.add(DAILY_REPORT);
		claimHeadMenu.add(CPU_WISE_PERFORMANCE_REPORT);
		claimHeadMenu.add(AGENT_BROKER_REPORT);
		claimHeadMenu.add(OP_CLAIM_REPORT);
		// claimHeadMenu.add(CREATE_LOT_MAKER);

		// ---------Payment Process Cpu----------

		claimHeadMenu.add(PAYMENT_PROCESS_CPU);
		// claimHeadMenu.add(SEARCH_OR_UPLOAD_DOCUMENTS);
		claimHeadMenu.add(OP_SCREEN);
		claimHeadMenu.add(PROCESSING_PAYMENT);

		claimHeadMenu.add(CANCEL_ACKNOWLEDGEMENT);

		claimHeadMenu.add(INTIMATION_HEADER);
		// Remove this is disable test component
		// TODO: remove for protection
		// claimHeadMenu.add(TEST_COMPONENT);
		claimHeadMenu.add(SEARCH_POLICY);
		claimHeadMenu.add(SEARCH_INTIMATION);
		claimHeadMenu.add(INTIMATION_VIEW_DETAILS);
		claimHeadMenu.add(UPDATE_HOSPITAL_INFORMATION);
		claimHeadMenu.add(EDIT_HOSPITAL_INFORMATION);
		claimHeadMenu.add(OMP_MAIN_MENUBAR);
		claimHeadMenu.add(REGISTER_CLAIM_HEADER);
		claimHeadMenu.add(SEARCH_REGISTER_CLAIM);
		claimHeadMenu.add(REMINDER_LETTERS); // sdfasdfasdfasdf
		claimHeadMenu.add(GENERATE_REMINDER_LETTER_CLAIM_WISE);
		claimHeadMenu.add(GENERATE_REMINDER_LETTER_BULK);
		claimHeadMenu.add(GENERATE_COVERINGLETTER);
		claimHeadMenu.add(CONVERT_CLAIM);
		claimHeadMenu.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
		claimHeadMenu.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);

		claimHeadMenu.add(CASHLESS_PROCESS_HEADER);
		claimHeadMenu.add(PROCESS_PREAUTH_REJECTION);
		claimHeadMenu.add(PROCESS_PRE_MEDICAL);
		claimHeadMenu.add(PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		claimHeadMenu.add(PROCESS_PREAUTH);
		claimHeadMenu.add(PROCESS_PREAUTH_AUTO_ALLOCATION);
		claimHeadMenu.add(PROCESS_ENHANCEMENT);
		claimHeadMenu.add(PROCESS_PED_QUERY);
		claimHeadMenu.add(DOWNSIZE_PRE_AUTH);
		claimHeadMenu.add(WITHDRAW_PRE_AUTH);
		claimHeadMenu.add(DOWNSIZE_PRE_AUTH_REQUEST);
		
		claimHeadMenu.add(FIELD_VISIT_CASHLESS);
		claimHeadMenu.add(FIELD_VISIT_REPESENTATION);

		claimHeadMenu.add(ENDORSEMENTS_HEADER);
		claimHeadMenu.add(PROCESS_PED_REQUEST_PROCESS);
		claimHeadMenu.add(PROCESS_PED_REQUEST_APPROVE);

		claimHeadMenu.add(PROCESS_CLAIM_HEADER);
		claimHeadMenu.add(UPLOAD_TRANSLATED_DOCUMENTS);
		claimHeadMenu.add(DATA_CODING_DATA_CORRECTION);
		claimHeadMenu.add(FLAG_RECONSIDERATION_REQUEST);
		claimHeadMenu.add(DATA_CODING_DATA_CORRECTION_HISTORICAL);
		claimHeadMenu.add(DATA_CODING_DATA_CORRECTION_PRIORITY);

		claimHeadMenu.add(SPECIALIST_PROCESSING_HEADER);
		claimHeadMenu.add(ADVISE_ON_PED);
		claimHeadMenu.add(SUBMIT_SPECIALLIST_ADVISE);

		claimHeadMenu.add(SEARCH_CLAIM);
		claimHeadMenu.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		claimHeadMenu.add(PROCESS_FLP_AUTO_ALLOCATION);
		
		// -----------------R3----------------------

		claimHeadMenu.add(REIMBURSEMENT_PAGE);

		claimHeadMenu.add(SHOW_INVESTIGATION);
		claimHeadMenu.add(SEARCH_PROCESS_INVESTIGATION_INITIATED);
		claimHeadMenu.add(SHOW_DRAFT_INVESTIGATION);
		claimHeadMenu.add(SHOW_ASSIGN_INVESTIGATION);
		claimHeadMenu.add(SEARCH_ACKNOWLEDGE_INVESTIGATION_COMPLETED);
		claimHeadMenu.add(SEARCH_INVESTIGATION_GRADING);

		claimHeadMenu.add(ROD);
		claimHeadMenu.add(SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED);
		claimHeadMenu.add(CREATE_ROD);
		claimHeadMenu.add(CREATE_ONLINE_ROD);
		claimHeadMenu.add(ENTER_BILL_DETAILS);
		claimHeadMenu.add(UPLOAD_INVESTIGATION_REPORT);

		claimHeadMenu.add(QUERY_REJECTION);
		claimHeadMenu.add(DRAFT_QUERY_LETTER_SEARCH);
		claimHeadMenu.add(PROCESS_DRAFT_QUERY_LETTER);
		// claimHeadMenu.add(GENERATE_REMINDER_LETTER_CLAIMWISE);
		claimHeadMenu.add(DRAFT_REJECTION_LETTER);
		claimHeadMenu.add(PROCESS_DRAFT_REJECTION_LETTER);

		claimHeadMenu.add(SPECIALIST_PROCESSING);
		claimHeadMenu.add(SUBMIT_SPECIALIST_ADVISE);

		claimHeadMenu.add(MEDICAL_APPROVAL);
		claimHeadMenu.add(PROCESS_CLAIM_REQUEST_ZONAL_REVIEW);
		claimHeadMenu.add(PROCESS_CLAIM_REQUEST);
		claimHeadMenu.add(PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION);


		claimHeadMenu.add(BILLING);
		claimHeadMenu.add(PROCESS_CLAIM_BILLING);
		claimHeadMenu.add(PROCESS_CLAIM_BILLING_AUTO_ALLOCATION);
		claimHeadMenu.add(PROCESS_CLAIM_REQUEST_BENEFITSBILLING);

		claimHeadMenu.add(FINANCIAL_APPROVAL);
		claimHeadMenu.add(PROCESS_CLAIM_FINANCIALS);
		claimHeadMenu.add(PROCESS_CLAIM_REQUEST_BENEFITSFINANCIAL);

		claimHeadMenu.add(MANAGE_CLAIMS);
		claimHeadMenu.add(RE_OPEN_CLAIM_ROD_LEVEL);
		claimHeadMenu.add(CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL);
		claimHeadMenu.add(CLOSE_CLAIM_ROD_LEVEL);
		claimHeadMenu.add(CLOSE_CLAIM_CLAIM_LEVEL);
		claimHeadMenu.add(RE_OPEN_CLAIM_CLAIM_LEVEL);
		claimHeadMenu.add(ALLOW_RECONSIDERATION);
		claimHeadMenu.add(CLAIM_WISE_ALLOW_APPROVAL);
		claimHeadMenu.add(UPDATE_ROD_DETAILS);

		claimHeadMenu.add(PAYMENT_PROCESSOR);
		claimHeadMenu.add(MISC);
		claimHeadMenu.add(REGISTER_CLAIM_REFUND);
		claimHeadMenu.add(PROCESS_CLAIM_REFUND);
		claimHeadMenu.add(USER_ACCESS_ALLOCATION);
		claimHeadMenu.add(USER_MANAGEMENT);
		claimHeadMenu.add(USER_RE_ALLOCATION);
		claimHeadMenu.add(CPU_AUTO_ALLOCATION);

		claimHeadMenu.add(ADD_ADDITIONAL_DOC);
		claimHeadMenu.add(PHYSICAL_DOCUMENT);
		claimHeadMenu.add(PHYSICAL_DOCUMENT_CHECKER);

		claimHeadMenu.add(REWARD_RECOGNITION_CELL);
		claimHeadMenu.add(TALK_TALK_TALK);

		claimHeadMenu.add(SPECIAL_APPROVALS);
		claimHeadMenu.add(APPROVE_CLAIM);
		claimHeadMenu.add(PROCESS_CLAIM);

		// lumen
		claimHeadMenu.add(LUMEN_HEADER);

		List<String> cashless_processing_head = new ArrayList<String>();
		// ----------CLS----------------

		cashless_processing_head.add(HEALTH_PAGE);

		cashless_processing_head.add(CASHLESS_PAGE);

		cashless_processing_head.add(INTIMATION_HEADER);
		cashless_processing_head.add(SEARCH_POLICY);
		cashless_processing_head.add(SEARCH_INTIMATION);
		cashless_processing_head.add(INTIMATION_VIEW_DETAILS);
		cashless_processing_head.add(UPDATE_HOSPITAL_INFORMATION);
		cashless_processing_head.add(EDIT_HOSPITAL_INFORMATION);

		cashless_processing_head.add(REGISTER_CLAIM_HEADER);
		cashless_processing_head.add(SEARCH_REGISTER_CLAIM);
		cashless_processing_head.add(GENERATE_COVERINGLETTER);
		cashless_processing_head.add(CONVERT_CLAIM);
		cashless_processing_head.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
		cashless_processing_head.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);

		cashless_processing_head.add(CASHLESS_PROCESS_HEADER);
		cashless_processing_head.add(PROCESS_PREAUTH_REJECTION);
		cashless_processing_head.add(PROCESS_PRE_MEDICAL);
		cashless_processing_head.add(PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		cashless_processing_head.add(PROCESS_PREAUTH);
		cashless_processing_head.add(PROCESS_PREAUTH_AUTO_ALLOCATION);
		cashless_processing_head.add(PROCESS_ENHANCEMENT);
		cashless_processing_head.add(PROCESS_PED_QUERY);
		cashless_processing_head.add(DOWNSIZE_PRE_AUTH);
		cashless_processing_head.add(WITHDRAW_PRE_AUTH);
		cashless_processing_head.add(DOWNSIZE_PRE_AUTH_REQUEST);
		
		cashless_processing_head.add(FIELD_VISIT_CASHLESS);
		cashless_processing_head.add(FIELD_VISIT_REPESENTATION);

		cashless_processing_head.add(ENDORSEMENTS_HEADER);
		cashless_processing_head.add(PROCESS_PED_REQUEST_PROCESS);
		cashless_processing_head.add(PROCESS_PED_REQUEST_APPROVE);

		cashless_processing_head.add(PROCESS_CLAIM_HEADER);
		cashless_processing_head.add(UPLOAD_TRANSLATED_DOCUMENTS);
		cashless_processing_head.add(DATA_CODING_DATA_CORRECTION);
		cashless_processing_head.add(FLAG_RECONSIDERATION_REQUEST);
		cashless_processing_head.add(DATA_CODING_DATA_CORRECTION_HISTORICAL);
		cashless_processing_head.add(DATA_CODING_DATA_CORRECTION_PRIORITY);
		cashless_processing_head.add(SPECIALIST_PROCESSING_HEADER);
		cashless_processing_head.add(ADVISE_ON_PED);
		cashless_processing_head.add(SUBMIT_SPECIALLIST_ADVISE);

		cashless_processing_head.add(SEARCH_CLAIM);
		
		cashless_processing_head.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		cashless_processing_head.add(PROCESS_FLP_AUTO_ALLOCATION);	

		List<String> cashless_chief_medical_officer = new ArrayList<String>();
		// ----------CLS----------------

		cashless_chief_medical_officer.add(HEALTH_PAGE);

		cashless_chief_medical_officer.add(CASHLESS_PAGE);

		cashless_chief_medical_officer.add(INTIMATION_HEADER);
		cashless_chief_medical_officer.add(SEARCH_POLICY);
		cashless_chief_medical_officer.add(SEARCH_INTIMATION);
		cashless_chief_medical_officer.add(INTIMATION_VIEW_DETAILS);
		cashless_chief_medical_officer.add(UPDATE_HOSPITAL_INFORMATION);
		cashless_chief_medical_officer.add(EDIT_HOSPITAL_INFORMATION);

		cashless_chief_medical_officer.add(REGISTER_CLAIM_HEADER);
		cashless_chief_medical_officer.add(SEARCH_REGISTER_CLAIM);
		cashless_chief_medical_officer.add(GENERATE_COVERINGLETTER);
		cashless_chief_medical_officer.add(CONVERT_CLAIM);
		cashless_chief_medical_officer.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
		cashless_chief_medical_officer.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);

		cashless_chief_medical_officer.add(CASHLESS_PROCESS_HEADER);
		cashless_chief_medical_officer.add(PROCESS_PREAUTH_REJECTION);
		cashless_chief_medical_officer.add(PROCESS_PRE_MEDICAL);
		cashless_chief_medical_officer.add(PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		cashless_chief_medical_officer.add(PROCESS_PREAUTH);
		cashless_chief_medical_officer.add(PROCESS_PREAUTH_AUTO_ALLOCATION);
		cashless_chief_medical_officer.add(PROCESS_ENHANCEMENT);
		cashless_chief_medical_officer.add(PROCESS_PED_QUERY);
		cashless_chief_medical_officer.add(DOWNSIZE_PRE_AUTH);
		cashless_chief_medical_officer.add(WITHDRAW_PRE_AUTH);
		cashless_chief_medical_officer.add(DOWNSIZE_PRE_AUTH_REQUEST);
		

		cashless_chief_medical_officer.add(FIELD_VISIT_CASHLESS);
		cashless_chief_medical_officer.add(FIELD_VISIT_REPESENTATION);

		cashless_chief_medical_officer.add(ENDORSEMENTS_HEADER);
		cashless_chief_medical_officer.add(PROCESS_PED_REQUEST_PROCESS);
		cashless_chief_medical_officer.add(PROCESS_PED_REQUEST_APPROVE);

		cashless_chief_medical_officer.add(PROCESS_CLAIM_HEADER);
		cashless_chief_medical_officer.add(UPLOAD_TRANSLATED_DOCUMENTS);
		cashless_chief_medical_officer.add(DATA_CODING_DATA_CORRECTION);
		cashless_chief_medical_officer.add(FLAG_RECONSIDERATION_REQUEST);
		cashless_chief_medical_officer.add(DATA_CODING_DATA_CORRECTION_HISTORICAL);
		cashless_chief_medical_officer.add(DATA_CODING_DATA_CORRECTION_PRIORITY);
		cashless_chief_medical_officer.add(SPECIALIST_PROCESSING_HEADER);
		cashless_chief_medical_officer.add(ADVISE_ON_PED);
		cashless_chief_medical_officer.add(SUBMIT_SPECIALLIST_ADVISE);

		cashless_chief_medical_officer.add(SEARCH_CLAIM);
		
		cashless_chief_medical_officer.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		cashless_chief_medical_officer.add(PROCESS_FLP_AUTO_ALLOCATION);	

		List<String> senior_medical_approver = new ArrayList<String>();
		// --------------------CLS-----------------------------------

		senior_medical_approver.add(HEALTH_PAGE);
		senior_medical_approver.add(CASHLESS_PAGE);

		senior_medical_approver.add(CASHLESS_PROCESS_HEADER);
		senior_medical_approver.add(PROCESS_PREAUTH);
		senior_medical_approver.add(PROCESS_PREAUTH_AUTO_ALLOCATION);
		senior_medical_approver.add(PROCESS_ENHANCEMENT);
		senior_medical_approver.add(PROCESS_PED_QUERY);
		senior_medical_approver.add(DOWNSIZE_PRE_AUTH);
		senior_medical_approver.add(WITHDRAW_PRE_AUTH);
		senior_medical_approver.add(DOWNSIZE_PRE_AUTH_REQUEST);
		senior_medical_approver.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		senior_medical_approver.add(SEARCH_CLAIM);
		senior_medical_approver.add(PROCESS_FLP_AUTO_ALLOCATION);	

		// -----------------R3----------------------

		senior_medical_approver.add(REIMBURSEMENT_PAGE);

		senior_medical_approver.add(MEDICAL_APPROVAL);
		senior_medical_approver.add(PROCESS_CLAIM_REQUEST);
		senior_medical_approver.add(PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION);

		List<String> medical_approver = new ArrayList<String>();

		medical_approver.add(HEALTH_PAGE);
		medical_approver.add(CASHLESS_PAGE);

		medical_approver.add(CASHLESS_PROCESS_HEADER);
		// medical_approver.add(PROCESS_PREAUTH);
		// medical_approver.add(PROCESS_ENHANCEMENT);
		// medical_approver.add(PROCESS_PED_QUERY);
		// medical_approver.add(DOWNSIZE_PRE_AUTH);
		medical_approver.add(WITHDRAW_PRE_AUTH);
		// medical_approver.add(DOWNSIZE_PRE_AUTH_REQUEST);

		medical_approver.add(SEARCH_CLAIM);
		medical_approver.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		medical_approver.add(PROCESS_FLP_AUTO_ALLOCATION);	

		// -----------------R3----------------------

		medical_approver.add(REIMBURSEMENT_PAGE);

		medical_approver.add(MEDICAL_APPROVAL);

		medical_approver.add(PROCESS_CLAIM_REQUEST);

		List<String> cls_settlement_medical_approver = new ArrayList<String>();
		// ---------------------CLS--------------------
		// cls_settlement_medical_approver.add(CASHLESS_PAGE);
		// -----------------------------R3-----------------------------------
		cls_settlement_medical_approver.add(REIMBURSEMENT_PAGE);
		cls_settlement_medical_approver.add(MEDICAL_APPROVAL);
		cls_settlement_medical_approver.add(PROCESS_CLAIM_REQUEST);
		cls_settlement_medical_approver.add(SEARCH_CLAIM);

		List<String> specialist_doctor = new ArrayList<String>();
		// ---------------------CLS--------------------
		specialist_doctor.add(HEALTH_PAGE);
		specialist_doctor.add(CASHLESS_PAGE);

		specialist_doctor.add(SPECIALIST_PROCESSING_HEADER);
		specialist_doctor.add(ADVISE_ON_PED);
		specialist_doctor.add(SUBMIT_SPECIALLIST_ADVISE);
		specialist_doctor.add(SEARCH_CLAIM);
		// -----------------------------R3-----------------------------------

		specialist_doctor.add(REIMBURSEMENT_PAGE);
		specialist_doctor.add(SPECIALIST_PROCESSING);
		specialist_doctor.add(SUBMIT_SPECIALIST_ADVISE);

		List<String> field_visit_head = new ArrayList<String>();
		// -------------------CLS-----------------
		field_visit_head.add(HEALTH_PAGE);
		field_visit_head.add(CASHLESS_PAGE);

		field_visit_head.add(FIELD_VISIT_CASHLESS);
		field_visit_head.add(FIELD_VISIT_REPESENTATION);
		// -----------------R3--------------------
		field_visit_head.add(REIMBURSEMENT_PAGE);
		field_visit_head.add(FIELD_VISIT_REIMBURSEMENT);
		field_visit_head.add(SHOW_PROCESS_FIELD_VISIT);
		field_visit_head.add(SEARCH_CLAIM);

		List<String> registration_team_lead = new ArrayList<String>();
		// ------------------------------CLS------------------------------
		registration_team_lead.add(HEALTH_PAGE);
		registration_team_lead.add(CASHLESS_PAGE);
		registration_team_lead.add(INTIMATION_HEADER);
		registration_team_lead.add(SEARCH_POLICY);
		registration_team_lead.add(REGISTER_CLAIM_HEADER);
		registration_team_lead.add(SEARCH_REGISTER_CLAIM);
		registration_team_lead.add(GENERATE_COVERINGLETTER);
		// registration_team_lead.add(CONVERT_CLAIM);
		registration_team_lead.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
		registration_team_lead.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);

		registration_team_lead.add(REIMBURSEMENT_PAGE);
		registration_team_lead.add(SEARCH_CLAIM);

		List<String> rod_team_lead = new ArrayList<String>();
		// --------------------------CLS-------------------------
		rod_team_lead.add(HEALTH_PAGE);
		rod_team_lead.add(CASHLESS_PAGE);

		rod_team_lead.add(INTIMATION_HEADER);
		rod_team_lead.add(SEARCH_POLICY);
		rod_team_lead.add(SEARCH_INTIMATION);
		rod_team_lead.add(INTIMATION_VIEW_DETAILS);
		rod_team_lead.add(SEARCH_CLAIM);
		// -----------------R3-----------------
		rod_team_lead.add(REIMBURSEMENT_PAGE);
		rod_team_lead.add(ROD);
		rod_team_lead.add(SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED);
		rod_team_lead.add(CREATE_ROD);
		rod_team_lead.add(CREATE_ONLINE_ROD);
		rod_team_lead.add(ENTER_BILL_DETAILS);
		rod_team_lead.add(UPLOAD_INVESTIGATION_REPORT);
		// rod_team_lead.add(CANCEL_ACKNOWLEDGEMENT);

		List<String> rod_officer = new ArrayList<String>();
		// ---------------------CLS------------------------
		rod_officer.add(HEALTH_PAGE);
		rod_officer.add(CASHLESS_PAGE);

		rod_officer.add(INTIMATION_HEADER);
		rod_officer.add(SEARCH_POLICY);
		rod_officer.add(SEARCH_INTIMATION);
		rod_officer.add(INTIMATION_VIEW_DETAILS);
		// -----------------R3---------------
		rod_officer.add(REIMBURSEMENT_PAGE);
		rod_officer.add(ROD);
		// rod_officer.add(SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED);
		rod_officer.add(CREATE_ROD);
		rod_officer.add(CREATE_ONLINE_ROD);
		rod_officer.add(ENTER_BILL_DETAILS);
		// rod_officer.add(UPLOAD_INVESTIGATION_REPORT);
		rod_officer.add(SEARCH_CLAIM);
		// rod_officer.add(OP_HEALTH_CHECK_PROCESSING);
		// rod_officer.add(REGISTER_CLAIM_OP);
		// rod_officer.add(PORCESS_CLAIM_OP);
		// rod_officer.add(CREATE_LOT);
		// rod_officer.add(CANCEL_ACKNOWLEDGEMENT);

		List<String> cashless_claims_co_ordinator = new ArrayList<String>();
		// --------------------CLS------------------------
		cashless_claims_co_ordinator.add(HEALTH_PAGE);
		cashless_claims_co_ordinator.add(CASHLESS_PAGE);
		cashless_claims_co_ordinator.add(INTIMATION_HEADER);
		cashless_claims_co_ordinator.add(SEARCH_POLICY);
		cashless_claims_co_ordinator.add(SEARCH_INTIMATION);
		cashless_claims_co_ordinator.add(INTIMATION_VIEW_DETAILS);

		cashless_claims_co_ordinator.add(PROCESS_CLAIM_HEADER);
		cashless_claims_co_ordinator.add(UPLOAD_TRANSLATED_DOCUMENTS);
		cashless_claims_co_ordinator.add(DATA_CODING_DATA_CORRECTION);
		cashless_claims_co_ordinator.add(FLAG_RECONSIDERATION_REQUEST);
		cashless_claims_co_ordinator.add(DATA_CODING_DATA_CORRECTION_HISTORICAL);
		cashless_claims_co_ordinator.add(DATA_CODING_DATA_CORRECTION_PRIORITY);
		cashless_claims_co_ordinator.add(SEARCH_CLAIM);

		List<String> customer_care_representative = new ArrayList<String>();
		// ------------------------------CLS---------------------------------
		customer_care_representative.add(HEALTH_PAGE);
		customer_care_representative.add(CASHLESS_PAGE);

		customer_care_representative.add(INTIMATION_HEADER);
		customer_care_representative.add(SEARCH_POLICY);
		customer_care_representative.add(SEARCH_INTIMATION);
		customer_care_representative.add(INTIMATION_VIEW_DETAILS);
		// customer_care_representative.add(UPDATE_HOSPITAL_INFORMATION);
		customer_care_representative.add(EDIT_HOSPITAL_INFORMATION);
		customer_care_representative.add(SEARCH_CLAIM);
		customer_care_representative.add(STARFAX_SIMULATION);

		List<String> hms_pms = new ArrayList<String>();
		// ----------------------CLS------------------------
		hms_pms.add(HEALTH_PAGE);
		hms_pms.add(CASHLESS_PAGE);

		hms_pms.add(INTIMATION_HEADER);
		hms_pms.add(EDIT_HOSPITAL_INFORMATION);

		hms_pms.add(SEARCH_CLAIM);

		List<String> pre_medical = new ArrayList<String>();
		// -----------------------CLS-------------------------------
		pre_medical.add(HEALTH_PAGE);
		pre_medical.add(CASHLESS_PAGE);

		pre_medical.add(CASHLESS_PROCESS_HEADER);
		pre_medical.add(PROCESS_PRE_MEDICAL);
		pre_medical.add(PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		pre_medical.add(REGISTER_CLAIM_HEADER);
		pre_medical.add(SEARCH_REGISTER_CLAIM);
		pre_medical.add(SEARCH_CLAIM);

		List<String> non_medical_rejection = new ArrayList<String>();
		// --------------------------CLS------------------------------
		non_medical_rejection.add(HEALTH_PAGE);
		non_medical_rejection.add(CASHLESS_PAGE);

		non_medical_rejection.add(CASHLESS_PROCESS_HEADER);
		non_medical_rejection.add(PROCESS_PREAUTH_REJECTION);
		non_medical_rejection.add(SEARCH_CLAIM);

		List<String> send_to_processing_non_medical = new ArrayList<String>();
		// --------------------------CLS------------------------------
		send_to_processing_non_medical.add(HEALTH_PAGE);
		send_to_processing_non_medical.add(CASHLESS_PAGE);

		send_to_processing_non_medical.add(CASHLESS_PROCESS_HEADER);
		send_to_processing_non_medical.add(PROCESS_PRE_MEDICAL);
		send_to_processing_non_medical.add(PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		send_to_processing_non_medical.add(SEARCH_CLAIM);

		List<String> claims_accounts_officer = new ArrayList<String>();

		claims_accounts_officer.add(SEARCH_CLAIM);
		// --------------------------R3------------------------------
		claims_accounts_officer.add(REIMBURSEMENT_PAGE);
		claims_accounts_officer.add(MISC);
		claims_accounts_officer.add(REGISTER_CLAIM_REFUND);

		List<String> senior_claims_accounts_officer = new ArrayList<String>();

		senior_claims_accounts_officer.add(SEARCH_CLAIM);
		// --------------------------R3------------------------------
		senior_claims_accounts_officer.add(REIMBURSEMENT_PAGE);
		senior_claims_accounts_officer.add(MISC);
		senior_claims_accounts_officer.add(PROCESS_CLAIM_REFUND);

		List<String> ped_processor = new ArrayList<String>();
		// --------------------------CLS------------------------------
		ped_processor.add(HEALTH_PAGE);
		ped_processor.add(CASHLESS_PAGE);
		ped_processor.add(ENDORSEMENTS_HEADER);
		ped_processor.add(PROCESS_PED_REQUEST_PROCESS);

		ped_processor.add(SEARCH_CLAIM);

		List<String> ped_approver = new ArrayList<String>();
		// --------------------------CLS------------------------------
		ped_approver.add(HEALTH_PAGE);
		ped_approver.add(CASHLESS_PAGE);
		ped_approver.add(ENDORSEMENTS_HEADER);
		ped_approver.add(PROCESS_PED_REQUEST_APPROVE);

		ped_approver.add(SEARCH_CLAIM);

		List<String> zonal_claims_head = new ArrayList<String>();

		zonal_claims_head.add(HEALTH_PAGE);
		zonal_claims_head.add(CASHLESS_PAGE);

		zonal_claims_head.add(INTIMATION_HEADER);
		zonal_claims_head.add(SEARCH_POLICY);
		zonal_claims_head.add(SEARCH_INTIMATION);
		zonal_claims_head.add(INTIMATION_VIEW_DETAILS);
		zonal_claims_head.add(UPDATE_HOSPITAL_INFORMATION);
		zonal_claims_head.add(EDIT_HOSPITAL_INFORMATION);

		zonal_claims_head.add(REGISTER_CLAIM_HEADER);
		zonal_claims_head.add(SEARCH_REGISTER_CLAIM);
		zonal_claims_head.add(GENERATE_COVERINGLETTER);
		zonal_claims_head.add(CONVERT_CLAIM);
		zonal_claims_head.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
		zonal_claims_head.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);

		zonal_claims_head.add(CASHLESS_PROCESS_HEADER);
		zonal_claims_head.add(PROCESS_PREAUTH_REJECTION);
		zonal_claims_head.add(PROCESS_PRE_MEDICAL);
		zonal_claims_head.add(PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		zonal_claims_head.add(PROCESS_PREAUTH);
		zonal_claims_head.add(PROCESS_PREAUTH_AUTO_ALLOCATION);
		zonal_claims_head.add(PROCESS_ENHANCEMENT);
		zonal_claims_head.add(PROCESS_PED_QUERY);
		zonal_claims_head.add(DOWNSIZE_PRE_AUTH);
		zonal_claims_head.add(WITHDRAW_PRE_AUTH);
		zonal_claims_head.add(DOWNSIZE_PRE_AUTH_REQUEST);
		
		zonal_claims_head.add(FIELD_VISIT_CASHLESS);
		zonal_claims_head.add(FIELD_VISIT_REPESENTATION);

		zonal_claims_head.add(ENDORSEMENTS_HEADER);
		zonal_claims_head.add(PROCESS_PED_REQUEST_PROCESS);
		zonal_claims_head.add(PROCESS_PED_REQUEST_APPROVE);

		zonal_claims_head.add(PROCESS_CLAIM_HEADER);
		zonal_claims_head.add(UPLOAD_TRANSLATED_DOCUMENTS);
		zonal_claims_head.add(DATA_CODING_DATA_CORRECTION);
		zonal_claims_head.add(FLAG_RECONSIDERATION_REQUEST);
		zonal_claims_head.add(DATA_CODING_DATA_CORRECTION_HISTORICAL);
		zonal_claims_head.add(DATA_CODING_DATA_CORRECTION_PRIORITY);
		zonal_claims_head.add(SPECIALIST_PROCESSING_HEADER);
		zonal_claims_head.add(ADVISE_ON_PED);
		zonal_claims_head.add(SUBMIT_SPECIALLIST_ADVISE);

		zonal_claims_head.add(SEARCH_CLAIM);
		zonal_claims_head.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		zonal_claims_head.add(PROCESS_FLP_AUTO_ALLOCATION);

		// -----------------R3----------------------

		zonal_claims_head.add(REIMBURSEMENT_PAGE);

		zonal_claims_head.add(SHOW_INVESTIGATION);
		zonal_claims_head.add(SEARCH_PROCESS_INVESTIGATION_INITIATED);
		zonal_claims_head.add(SHOW_DRAFT_INVESTIGATION);
		zonal_claims_head.add(SHOW_ASSIGN_INVESTIGATION);
		zonal_claims_head.add(SEARCH_ACKNOWLEDGE_INVESTIGATION_COMPLETED);
		zonal_claims_head.add(SEARCH_INVESTIGATION_GRADING);

		zonal_claims_head.add(ROD);
		zonal_claims_head.add(SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED);
		zonal_claims_head.add(CREATE_ROD);
		zonal_claims_head.add(CREATE_ONLINE_ROD);
		zonal_claims_head.add(ENTER_BILL_DETAILS);
		zonal_claims_head.add(UPLOAD_INVESTIGATION_REPORT);

		zonal_claims_head.add(QUERY_REJECTION);
		zonal_claims_head.add(DRAFT_QUERY_LETTER_SEARCH);
		zonal_claims_head.add(PROCESS_DRAFT_QUERY_LETTER);
		// zonal_claims_head.add(GENERATE_REMINDER_LETTER_CLAIMWISE);
		zonal_claims_head.add(DRAFT_REJECTION_LETTER);
		zonal_claims_head.add(PROCESS_DRAFT_REJECTION_LETTER);

		zonal_claims_head.add(SPECIALIST_PROCESSING);
		zonal_claims_head.add(SUBMIT_SPECIALIST_ADVISE);

		zonal_claims_head.add(MEDICAL_APPROVAL);
		zonal_claims_head.add(PROCESS_CLAIM_REQUEST_ZONAL_REVIEW);
		zonal_claims_head.add(PROCESS_CLAIM_REQUEST);

		zonal_claims_head.add(BILLING);
		zonal_claims_head.add(PROCESS_CLAIM_BILLING);
		zonal_claims_head.add(PROCESS_CLAIM_BILLING_AUTO_ALLOCATION);
		zonal_claims_head.add(PROCESS_CLAIM_REQUEST_BENEFITSBILLING);

		zonal_claims_head.add(FINANCIAL_APPROVAL);
		zonal_claims_head.add(PROCESS_CLAIM_FINANCIALS);
		zonal_claims_head.add(PROCESS_CLAIM_REQUEST_BENEFITSFINANCIAL);

		zonal_claims_head.add(MANAGE_CLAIMS);
		zonal_claims_head.add(RE_OPEN_CLAIM_ROD_LEVEL);
		zonal_claims_head.add(CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL);
		zonal_claims_head.add(CLOSE_CLAIM_ROD_LEVEL);
		zonal_claims_head.add(CLOSE_CLAIM_CLAIM_LEVEL);
		zonal_claims_head.add(RE_OPEN_CLAIM_CLAIM_LEVEL);
		zonal_claims_head.add(ALLOW_RECONSIDERATION);
		zonal_claims_head.add(CLAIM_WISE_ALLOW_APPROVAL);
		zonal_claims_head.add(HOLD_MONITOR_SCREEN);
		zonal_claims_head.add(UPDATE_ROD_DETAILS);

		zonal_claims_head.add(MISC);
		zonal_claims_head.add(REGISTER_CLAIM_REFUND);
		zonal_claims_head.add(PROCESS_CLAIM_REFUND);
		zonal_claims_head.add(CANCEL_ACKNOWLEDGEMENT);
		// zonal_claims_head.add(ADD_ADDITIONAL_DOC);

		List<String> zonal_medical_head = new ArrayList<String>();

		zonal_medical_head.add(HEALTH_PAGE);
		zonal_medical_head.add(CASHLESS_PAGE);

		zonal_medical_head.add(INTIMATION_HEADER);
		zonal_medical_head.add(SEARCH_POLICY);
		zonal_medical_head.add(SEARCH_INTIMATION);
		zonal_claims_head.add(INTIMATION_VIEW_DETAILS);
		zonal_medical_head.add(UPDATE_HOSPITAL_INFORMATION);
		zonal_medical_head.add(EDIT_HOSPITAL_INFORMATION);

		zonal_medical_head.add(REGISTER_CLAIM_HEADER);
		zonal_medical_head.add(SEARCH_REGISTER_CLAIM);
		zonal_medical_head.add(GENERATE_COVERINGLETTER);
		zonal_medical_head.add(CONVERT_CLAIM);
		zonal_medical_head.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
		zonal_medical_head.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);

		zonal_medical_head.add(CASHLESS_PROCESS_HEADER);
		zonal_medical_head.add(PROCESS_PREAUTH_REJECTION);
		zonal_medical_head.add(PROCESS_PRE_MEDICAL);
		zonal_medical_head.add(PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		zonal_medical_head.add(PROCESS_PREAUTH);
		zonal_medical_head.add(PROCESS_PREAUTH_AUTO_ALLOCATION);
		zonal_medical_head.add(PROCESS_ENHANCEMENT);
		zonal_medical_head.add(PROCESS_PED_QUERY);
		zonal_medical_head.add(DOWNSIZE_PRE_AUTH);
		zonal_medical_head.add(WITHDRAW_PRE_AUTH);
		zonal_medical_head.add(DOWNSIZE_PRE_AUTH_REQUEST);
		
		zonal_medical_head.add(FIELD_VISIT_CASHLESS);
		zonal_medical_head.add(FIELD_VISIT_REPESENTATION);

		zonal_medical_head.add(ENDORSEMENTS_HEADER);
		zonal_medical_head.add(PROCESS_PED_REQUEST_PROCESS);
		zonal_medical_head.add(PROCESS_PED_REQUEST_APPROVE);

		zonal_medical_head.add(PROCESS_CLAIM_HEADER);
		zonal_medical_head.add(UPLOAD_TRANSLATED_DOCUMENTS);
		zonal_medical_head.add(DATA_CODING_DATA_CORRECTION);
		zonal_medical_head.add(FLAG_RECONSIDERATION_REQUEST);
		zonal_medical_head.add(DATA_CODING_DATA_CORRECTION_HISTORICAL);
		zonal_medical_head.add(DATA_CODING_DATA_CORRECTION_PRIORITY);
		zonal_medical_head.add(SPECIALIST_PROCESSING_HEADER);
		zonal_medical_head.add(ADVISE_ON_PED);
		zonal_medical_head.add(SUBMIT_SPECIALLIST_ADVISE);

		zonal_medical_head.add(SEARCH_CLAIM);
		zonal_medical_head.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		zonal_medical_head.add(PROCESS_FLP_AUTO_ALLOCATION);

		// -----------------R3----------------------

		zonal_medical_head.add(REIMBURSEMENT_PAGE);

		zonal_medical_head.add(SHOW_INVESTIGATION);
		zonal_medical_head.add(SEARCH_PROCESS_INVESTIGATION_INITIATED);
		zonal_medical_head.add(SHOW_DRAFT_INVESTIGATION);
		zonal_medical_head.add(SHOW_ASSIGN_INVESTIGATION);
		zonal_medical_head.add(SEARCH_ACKNOWLEDGE_INVESTIGATION_COMPLETED);
		zonal_medical_head.add(SEARCH_INVESTIGATION_GRADING);

		zonal_medical_head.add(ROD);
		zonal_medical_head.add(SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED);
		zonal_medical_head.add(CREATE_ROD);
		zonal_medical_head.add(CREATE_ONLINE_ROD);
		zonal_medical_head.add(ENTER_BILL_DETAILS);
		zonal_medical_head.add(UPLOAD_INVESTIGATION_REPORT);

		zonal_medical_head.add(QUERY_REJECTION);
		zonal_medical_head.add(DRAFT_QUERY_LETTER_SEARCH);
		zonal_medical_head.add(PROCESS_DRAFT_QUERY_LETTER);
		// zonal_medical_head.add(GENERATE_REMINDER_LETTER_CLAIMWISE);
		zonal_medical_head.add(DRAFT_REJECTION_LETTER);
		zonal_medical_head.add(PROCESS_DRAFT_REJECTION_LETTER);

		zonal_medical_head.add(SPECIALIST_PROCESSING);
		zonal_medical_head.add(SUBMIT_SPECIALIST_ADVISE);

		zonal_medical_head.add(MEDICAL_APPROVAL);
		zonal_medical_head.add(PROCESS_CLAIM_REQUEST_ZONAL_REVIEW);
		zonal_medical_head.add(PROCESS_CLAIM_REQUEST);

		zonal_medical_head.add(BILLING);
		zonal_medical_head.add(PROCESS_CLAIM_BILLING);
		zonal_medical_head.add(PROCESS_CLAIM_REQUEST_BENEFITSBILLING);

		zonal_medical_head.add(FINANCIAL_APPROVAL);
		zonal_medical_head.add(PROCESS_CLAIM_FINANCIALS);
		zonal_medical_head.add(PROCESS_CLAIM_REQUEST_BENEFITSFINANCIAL);

		zonal_medical_head.add(MANAGE_CLAIMS);
		zonal_medical_head.add(RE_OPEN_CLAIM_ROD_LEVEL);
		zonal_claims_head.add(CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL);
		zonal_medical_head.add(CLOSE_CLAIM_ROD_LEVEL);
		zonal_medical_head.add(CLOSE_CLAIM_CLAIM_LEVEL);
		zonal_medical_head.add(RE_OPEN_CLAIM_CLAIM_LEVEL);
		zonal_medical_head.add(ALLOW_RECONSIDERATION);
		zonal_medical_head.add(CLAIM_WISE_ALLOW_APPROVAL);
		zonal_medical_head.add(HOLD_MONITOR_SCREEN);
		zonal_medical_head.add(UPDATE_ROD_DETAILS);

		zonal_medical_head.add(MISC);
		zonal_medical_head.add(REGISTER_CLAIM_REFUND);
		zonal_medical_head.add(PROCESS_CLAIM_REFUND);
		zonal_medical_head.add(CANCEL_ACKNOWLEDGEMENT);

		List<String> zonal_senior_medical_approver = new ArrayList<String>();

		zonal_senior_medical_approver.add(HEALTH_PAGE);
		zonal_senior_medical_approver.add(CASHLESS_PAGE);

		zonal_senior_medical_approver.add(CASHLESS_PROCESS_HEADER);
		zonal_senior_medical_approver.add(PROCESS_PREAUTH);
		zonal_senior_medical_approver.add(PROCESS_PREAUTH_AUTO_ALLOCATION);
		zonal_senior_medical_approver.add(PROCESS_ENHANCEMENT);
		zonal_senior_medical_approver.add(PROCESS_PED_QUERY);
		zonal_senior_medical_approver.add(DOWNSIZE_PRE_AUTH);
		zonal_senior_medical_approver.add(WITHDRAW_PRE_AUTH);
		zonal_senior_medical_approver.add(DOWNSIZE_PRE_AUTH_REQUEST);
		
		zonal_senior_medical_approver.add(SEARCH_CLAIM);

		zonal_senior_medical_approver.add(ADD_ADDITIONAL_DOC);
		zonal_senior_medical_approver.add(PHYSICAL_DOCUMENT);
		zonal_senior_medical_approver.add(PHYSICAL_DOCUMENT_CHECKER);
		/*
		 * zonal_senior_medical_approver.add(REWARD_RECOGNITION_CELL);
		 * zonal_senior_medical_approver.add(PROCESS_RRC_REQUEST);
		 * zonal_senior_medical_approver.add(REVIEW_RRC_REQUEST);
		 */
		zonal_senior_medical_approver.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		zonal_senior_medical_approver.add(PROCESS_FLP_AUTO_ALLOCATION);
		List<String> zonal_medical_approver = new ArrayList<String>();
		// ----------------------------CLS----------------------------
		zonal_medical_approver.add(HEALTH_PAGE);
		zonal_medical_approver.add(CASHLESS_PAGE);
		zonal_medical_approver.add(CASHLESS_PROCESS_HEADER);
		zonal_medical_approver.add(PROCESS_PREAUTH);
		zonal_medical_approver.add(PROCESS_PREAUTH_AUTO_ALLOCATION);
		zonal_medical_approver.add(PROCESS_ENHANCEMENT);
		zonal_medical_approver.add(PROCESS_PED_QUERY);
		zonal_medical_approver.add(DOWNSIZE_PRE_AUTH);
		zonal_medical_approver.add(WITHDRAW_PRE_AUTH);
		zonal_medical_approver.add(DOWNSIZE_PRE_AUTH_REQUEST);
		zonal_medical_approver.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		zonal_medical_approver.add(PROCESS_FLP_AUTO_ALLOCATION);
		// -------------------------R3-------------------------------
		zonal_medical_approver.add(REIMBURSEMENT_PAGE);
		zonal_medical_approver.add(MEDICAL_APPROVAL);
		zonal_medical_approver.add(PROCESS_CLAIM_REQUEST_ZONAL_REVIEW);
		zonal_medical_approver.add(PROCESS_CLAIM_REQUEST);
		zonal_medical_approver.add(REWARD_RECOGNITION_CELL);
		zonal_medical_approver.add(RESEARCH_ANALYSIS_WING_PROCESSING);
		/*zonal_medical_approver.add(PROCESS_RRC_REQUEST);
		zonal_medical_approver.add(REVIEW_RRC_REQUEST);*/
		zonal_medical_approver.add(SEARCH_CLAIM);

		List<String> field_visit_doctor_personnel = new ArrayList<String>();

		field_visit_doctor_personnel.add(HEALTH_PAGE);
		field_visit_doctor_personnel.add(CASHLESS_PAGE);
		field_visit_doctor_personnel.add(FIELD_VISIT_CASHLESS);
		field_visit_doctor_personnel.add(FIELD_VISIT_REPESENTATION);

		field_visit_doctor_personnel.add(REIMBURSEMENT_PAGE);
		field_visit_doctor_personnel.add(FIELD_VISIT_REIMBURSEMENT);
		field_visit_doctor_personnel.add(SHOW_PROCESS_FIELD_VISIT);
		field_visit_doctor_personnel.add(SEARCH_CLAIM);

		List<String> zonal_field_visit_co_ordinator = new ArrayList<String>();
		// ----------------------CLS------------------------------
		zonal_field_visit_co_ordinator.add(HEALTH_PAGE);
		zonal_field_visit_co_ordinator.add(CASHLESS_PAGE);

		zonal_field_visit_co_ordinator.add(INTIMATION_HEADER);
		zonal_field_visit_co_ordinator.add(SEARCH_POLICY);
		zonal_field_visit_co_ordinator.add(SEARCH_INTIMATION);
		zonal_field_visit_co_ordinator.add(INTIMATION_VIEW_DETAILS);

		// zonal_field_visit_co_ordinator.add(FIELD_VISIT_CASHLESS);
		// zonal_field_visit_co_ordinator.add(FIELD_VISIT_REPESENTATION);
		//
		zonal_field_visit_co_ordinator.add(REIMBURSEMENT_PAGE);

		zonal_field_visit_co_ordinator.add(FIELD_VISIT_REIMBURSEMENT);
		zonal_field_visit_co_ordinator.add(SHOW_PROCESS_FIELD_VISIT);

		zonal_field_visit_co_ordinator.add(SEARCH_CLAIM);

		List<String> zonal_registration_team_lead = new ArrayList<String>();

		zonal_registration_team_lead.add(HEALTH_PAGE);
		zonal_registration_team_lead.add(CASHLESS_PAGE);

		zonal_registration_team_lead.add(INTIMATION_HEADER);
		zonal_registration_team_lead.add(SEARCH_POLICY);
		zonal_registration_team_lead.add(REGISTER_CLAIM_HEADER);
		zonal_registration_team_lead.add(SEARCH_REGISTER_CLAIM);
		zonal_registration_team_lead.add(GENERATE_COVERINGLETTER);
		// zonal_registration_team_lead.add(CONVERT_CLAIM);
		zonal_registration_team_lead.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
		zonal_registration_team_lead.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);

		zonal_registration_team_lead.add(SEARCH_CLAIM);

		List<String> zonal_registration_officer = new ArrayList<String>();

		zonal_registration_officer.add(HEALTH_PAGE);
		zonal_registration_officer.add(CASHLESS_PAGE);

		zonal_registration_officer.add(INTIMATION_HEADER);
		zonal_registration_officer.add(SEARCH_POLICY);

		zonal_registration_officer.add(REGISTER_CLAIM_HEADER);
		zonal_registration_officer.add(SEARCH_REGISTER_CLAIM);
		zonal_registration_officer.add(GENERATE_COVERINGLETTER);
		// zonal_registration_officer.add(CONVERT_CLAIM);
		zonal_registration_officer.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
		zonal_registration_officer.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);

		zonal_registration_officer.add(SEARCH_CLAIM);

		List<String> zonal_hospital_co_ordinator = new ArrayList<String>();

		zonal_hospital_co_ordinator.add(HEALTH_PAGE);
		zonal_hospital_co_ordinator.add(CASHLESS_PAGE);

		zonal_hospital_co_ordinator.add(INTIMATION_HEADER);
		zonal_hospital_co_ordinator.add(SEARCH_POLICY);

		zonal_hospital_co_ordinator.add(REGISTER_CLAIM_HEADER);
		zonal_hospital_co_ordinator.add(SEARCH_REGISTER_CLAIM);
		zonal_hospital_co_ordinator.add(GENERATE_COVERINGLETTER);
		// zonal_hospital_co_ordinator.add(CONVERT_CLAIM);
		zonal_hospital_co_ordinator.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
		zonal_hospital_co_ordinator.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);

		zonal_hospital_co_ordinator.add(SEARCH_CLAIM);

		List<String> zonal_cashless_claims_co_ordinator = new ArrayList<String>();

		zonal_cashless_claims_co_ordinator.add(HEALTH_PAGE);
		zonal_cashless_claims_co_ordinator.add(CASHLESS_PAGE);
		//
		zonal_cashless_claims_co_ordinator.add(INTIMATION_HEADER);
		zonal_cashless_claims_co_ordinator.add(SEARCH_POLICY);
		zonal_cashless_claims_co_ordinator.add(SEARCH_INTIMATION);
		zonal_cashless_claims_co_ordinator.add(INTIMATION_VIEW_DETAILS);

		zonal_cashless_claims_co_ordinator.add(PROCESS_CLAIM_HEADER);
		zonal_cashless_claims_co_ordinator.add(UPLOAD_TRANSLATED_DOCUMENTS);
		zonal_cashless_claims_co_ordinator.add(DATA_CODING_DATA_CORRECTION);
		zonal_cashless_claims_co_ordinator.add(FLAG_RECONSIDERATION_REQUEST);
		zonal_cashless_claims_co_ordinator.add(DATA_CODING_DATA_CORRECTION_HISTORICAL);
		zonal_cashless_claims_co_ordinator.add(DATA_CODING_DATA_CORRECTION_PRIORITY);
		zonal_cashless_claims_co_ordinator.add(SEARCH_CLAIM);

		List<String> zonal_quality_claims_auditor = new ArrayList<String>();

		zonal_quality_claims_auditor.add(SEARCH_CLAIM);

		List<String> reimbursement_processing_head = new ArrayList<String>();

		reimbursement_processing_head.add(HEALTH_PAGE);
		reimbursement_processing_head.add(CASHLESS_PAGE);

		reimbursement_processing_head.add(INTIMATION_HEADER);
		reimbursement_processing_head.add(SEARCH_POLICY);
		reimbursement_processing_head.add(SEARCH_INTIMATION);
		reimbursement_processing_head.add(INTIMATION_VIEW_DETAILS);
		reimbursement_processing_head.add(UPDATE_HOSPITAL_INFORMATION);
		reimbursement_processing_head.add(EDIT_HOSPITAL_INFORMATION);

		reimbursement_processing_head.add(REGISTER_CLAIM_HEADER);
		reimbursement_processing_head.add(SEARCH_REGISTER_CLAIM);
		reimbursement_processing_head.add(GENERATE_COVERINGLETTER);
		reimbursement_processing_head.add(CONVERT_CLAIM);
		reimbursement_processing_head.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
		reimbursement_processing_head.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);

		reimbursement_processing_head.add(CASHLESS_PROCESS_HEADER);
		reimbursement_processing_head.add(PROCESS_PREAUTH_REJECTION);
		reimbursement_processing_head.add(PROCESS_PRE_MEDICAL);
		reimbursement_processing_head.add(PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		reimbursement_processing_head.add(PROCESS_PREAUTH);
		reimbursement_processing_head.add(PROCESS_PREAUTH_AUTO_ALLOCATION);
		reimbursement_processing_head.add(PROCESS_ENHANCEMENT);
		reimbursement_processing_head.add(PROCESS_PED_QUERY);
		reimbursement_processing_head.add(DOWNSIZE_PRE_AUTH);
		reimbursement_processing_head.add(WITHDRAW_PRE_AUTH);
		reimbursement_processing_head.add(DOWNSIZE_PRE_AUTH_REQUEST);
		
		reimbursement_processing_head.add(FIELD_VISIT_CASHLESS);
		reimbursement_processing_head.add(FIELD_VISIT_REPESENTATION);

		reimbursement_processing_head.add(ENDORSEMENTS_HEADER);
		reimbursement_processing_head.add(PROCESS_PED_REQUEST_PROCESS);
		reimbursement_processing_head.add(PROCESS_PED_REQUEST_APPROVE);

		reimbursement_processing_head.add(PROCESS_CLAIM_HEADER);
		reimbursement_processing_head.add(UPLOAD_TRANSLATED_DOCUMENTS);
		reimbursement_processing_head.add(DATA_CODING_DATA_CORRECTION);
		reimbursement_processing_head.add(FLAG_RECONSIDERATION_REQUEST);
		reimbursement_processing_head.add(DATA_CODING_DATA_CORRECTION_HISTORICAL);
		reimbursement_processing_head.add(DATA_CODING_DATA_CORRECTION_PRIORITY);
		reimbursement_processing_head.add(SPECIALIST_PROCESSING_HEADER);
		reimbursement_processing_head.add(ADVISE_ON_PED);
		reimbursement_processing_head.add(SUBMIT_SPECIALLIST_ADVISE);

		reimbursement_processing_head.add(SEARCH_CLAIM);
		reimbursement_processing_head.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		reimbursement_processing_head.add(PROCESS_FLP_AUTO_ALLOCATION);
		// -----------------R3----------------------

		reimbursement_processing_head.add(REIMBURSEMENT_PAGE);

		reimbursement_processing_head.add(SHOW_INVESTIGATION);
		reimbursement_processing_head
				.add(SEARCH_PROCESS_INVESTIGATION_INITIATED);
		reimbursement_processing_head.add(SHOW_DRAFT_INVESTIGATION);
		reimbursement_processing_head.add(SHOW_ASSIGN_INVESTIGATION);
		reimbursement_processing_head
				.add(SEARCH_ACKNOWLEDGE_INVESTIGATION_COMPLETED);
		reimbursement_processing_head.add(SEARCH_INVESTIGATION_GRADING);

		reimbursement_processing_head.add(ROD);
		reimbursement_processing_head
				.add(SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED);
		reimbursement_processing_head.add(CREATE_ROD);
		reimbursement_processing_head.add(CREATE_ONLINE_ROD);
		reimbursement_processing_head.add(ENTER_BILL_DETAILS);
		reimbursement_processing_head.add(UPLOAD_INVESTIGATION_REPORT);
		reimbursement_processing_head.add(ADD_ADDITIONAL_DOCUMENT_PAYMENT_INFORMATION);
		reimbursement_processing_head.add(QUERY_REJECTION);
		reimbursement_processing_head.add(DRAFT_QUERY_LETTER_SEARCH);
		reimbursement_processing_head.add(PROCESS_DRAFT_QUERY_LETTER);
		// reimbursement_processing_head.add(GENERATE_REMINDER_LETTER_CLAIMWISE);
		reimbursement_processing_head.add(DRAFT_REJECTION_LETTER);
		reimbursement_processing_head.add(PROCESS_DRAFT_REJECTION_LETTER);

		reimbursement_processing_head.add(SPECIALIST_PROCESSING);
		reimbursement_processing_head.add(SUBMIT_SPECIALIST_ADVISE);

		reimbursement_processing_head.add(MEDICAL_APPROVAL);
		reimbursement_processing_head.add(PROCESS_CLAIM_REQUEST_ZONAL_REVIEW);
		reimbursement_processing_head.add(PROCESS_CLAIM_REQUEST);

		reimbursement_processing_head.add(BILLING);
		reimbursement_processing_head.add(PROCESS_CLAIM_BILLING);
		reimbursement_processing_head
				.add(PROCESS_CLAIM_REQUEST_BENEFITSBILLING);

		reimbursement_processing_head.add(FINANCIAL_APPROVAL);
		reimbursement_processing_head.add(PROCESS_CLAIM_FINANCIALS);
		reimbursement_processing_head
				.add(PROCESS_CLAIM_REQUEST_BENEFITSFINANCIAL);

		reimbursement_processing_head.add(MANAGE_CLAIMS);
		reimbursement_processing_head.add(RE_OPEN_CLAIM_ROD_LEVEL);
		reimbursement_processing_head.add(CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL);
		reimbursement_processing_head.add(CLOSE_CLAIM_ROD_LEVEL);
		reimbursement_processing_head.add(CLOSE_CLAIM_CLAIM_LEVEL);
		reimbursement_processing_head.add(RE_OPEN_CLAIM_CLAIM_LEVEL);
		reimbursement_processing_head.add(ALLOW_RECONSIDERATION);
		reimbursement_processing_head.add(CLAIM_WISE_ALLOW_APPROVAL);
		reimbursement_processing_head.add(HOLD_MONITOR_SCREEN);
		reimbursement_processing_head.add(UPDATE_ROD_DETAILS);

		reimbursement_processing_head.add(MISC);
		reimbursement_processing_head.add(REGISTER_CLAIM_REFUND);
		reimbursement_processing_head.add(PROCESS_CLAIM_REFUND);

		reimbursement_processing_head.add(CANCEL_ACKNOWLEDGEMENT);

		List<String> reimbursement_chief_medical_officer = new ArrayList<String>();

		reimbursement_chief_medical_officer.add(HEALTH_PAGE);
		reimbursement_chief_medical_officer.add(CASHLESS_PAGE);

		reimbursement_chief_medical_officer.add(INTIMATION_HEADER);
		reimbursement_chief_medical_officer.add(SEARCH_POLICY);
		reimbursement_chief_medical_officer.add(SEARCH_INTIMATION);
		reimbursement_chief_medical_officer.add(INTIMATION_VIEW_DETAILS);
		reimbursement_chief_medical_officer.add(UPDATE_HOSPITAL_INFORMATION);
		reimbursement_chief_medical_officer.add(EDIT_HOSPITAL_INFORMATION);

		reimbursement_chief_medical_officer.add(REGISTER_CLAIM_HEADER);
		reimbursement_chief_medical_officer.add(SEARCH_REGISTER_CLAIM);
		reimbursement_chief_medical_officer.add(GENERATE_COVERINGLETTER);
		reimbursement_chief_medical_officer.add(CONVERT_CLAIM);
		reimbursement_chief_medical_officer.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
		reimbursement_chief_medical_officer
				.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);

		reimbursement_chief_medical_officer.add(CASHLESS_PROCESS_HEADER);
		reimbursement_chief_medical_officer.add(PROCESS_PREAUTH_REJECTION);
		reimbursement_chief_medical_officer.add(PROCESS_PRE_MEDICAL);
		reimbursement_chief_medical_officer
				.add(PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		reimbursement_chief_medical_officer.add(PROCESS_PREAUTH);
		reimbursement_chief_medical_officer
				.add(PROCESS_PREAUTH_AUTO_ALLOCATION);
		reimbursement_chief_medical_officer.add(PROCESS_ENHANCEMENT);
		reimbursement_chief_medical_officer.add(PROCESS_PED_QUERY);
		reimbursement_chief_medical_officer.add(DOWNSIZE_PRE_AUTH);
		reimbursement_chief_medical_officer.add(WITHDRAW_PRE_AUTH);
		reimbursement_chief_medical_officer.add(DOWNSIZE_PRE_AUTH_REQUEST);
		

		reimbursement_chief_medical_officer.add(FIELD_VISIT_CASHLESS);
		reimbursement_chief_medical_officer.add(FIELD_VISIT_REPESENTATION);

		reimbursement_chief_medical_officer.add(ENDORSEMENTS_HEADER);
		reimbursement_chief_medical_officer.add(PROCESS_PED_REQUEST_PROCESS);
		reimbursement_chief_medical_officer.add(PROCESS_PED_REQUEST_APPROVE);

		reimbursement_chief_medical_officer.add(PROCESS_CLAIM_HEADER);
		reimbursement_chief_medical_officer.add(UPLOAD_TRANSLATED_DOCUMENTS);
		reimbursement_chief_medical_officer.add(DATA_CODING_DATA_CORRECTION);
		reimbursement_chief_medical_officer.add(FLAG_RECONSIDERATION_REQUEST);
		reimbursement_chief_medical_officer.add(DATA_CODING_DATA_CORRECTION_HISTORICAL);
		reimbursement_chief_medical_officer.add(DATA_CODING_DATA_CORRECTION_PRIORITY);
		reimbursement_chief_medical_officer.add(SPECIALIST_PROCESSING_HEADER);
		reimbursement_chief_medical_officer.add(ADVISE_ON_PED);
		reimbursement_chief_medical_officer.add(SUBMIT_SPECIALLIST_ADVISE);

		reimbursement_chief_medical_officer.add(SEARCH_CLAIM);
		reimbursement_chief_medical_officer.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		reimbursement_chief_medical_officer.add(PROCESS_FLP_AUTO_ALLOCATION);
		// -----------------R3----------------------

		reimbursement_chief_medical_officer.add(REIMBURSEMENT_PAGE);

		reimbursement_chief_medical_officer.add(SHOW_INVESTIGATION);
		reimbursement_chief_medical_officer
				.add(SEARCH_PROCESS_INVESTIGATION_INITIATED);
		reimbursement_chief_medical_officer.add(SHOW_DRAFT_INVESTIGATION);
		reimbursement_chief_medical_officer.add(SHOW_ASSIGN_INVESTIGATION);
		reimbursement_chief_medical_officer
				.add(SEARCH_ACKNOWLEDGE_INVESTIGATION_COMPLETED);
		reimbursement_chief_medical_officer.add(SEARCH_INVESTIGATION_GRADING);

		reimbursement_chief_medical_officer.add(ROD);
		reimbursement_chief_medical_officer
				.add(SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED);
		reimbursement_chief_medical_officer.add(CREATE_ROD);
		reimbursement_chief_medical_officer.add(CREATE_ONLINE_ROD);
		reimbursement_chief_medical_officer.add(ENTER_BILL_DETAILS);
		reimbursement_chief_medical_officer.add(UPLOAD_INVESTIGATION_REPORT);

		reimbursement_chief_medical_officer.add(QUERY_REJECTION);
		reimbursement_chief_medical_officer.add(DRAFT_QUERY_LETTER_SEARCH);
		reimbursement_chief_medical_officer.add(PROCESS_DRAFT_QUERY_LETTER);
		// reimbursement_chief_medical_officer.add(GENERATE_REMINDER_LETTER_CLAIMWISE);
		reimbursement_chief_medical_officer.add(DRAFT_REJECTION_LETTER);
		reimbursement_chief_medical_officer.add(PROCESS_DRAFT_REJECTION_LETTER);

		reimbursement_chief_medical_officer.add(SPECIALIST_PROCESSING);
		reimbursement_chief_medical_officer.add(SUBMIT_SPECIALIST_ADVISE);

		reimbursement_chief_medical_officer.add(MEDICAL_APPROVAL);
		reimbursement_chief_medical_officer
				.add(PROCESS_CLAIM_REQUEST_ZONAL_REVIEW);
		reimbursement_chief_medical_officer.add(PROCESS_CLAIM_REQUEST);

		reimbursement_chief_medical_officer.add(BILLING);
		reimbursement_chief_medical_officer.add(PROCESS_CLAIM_BILLING);
		reimbursement_chief_medical_officer
				.add(PROCESS_CLAIM_REQUEST_BENEFITSBILLING);

		reimbursement_chief_medical_officer.add(FINANCIAL_APPROVAL);
		reimbursement_chief_medical_officer.add(PROCESS_CLAIM_FINANCIALS);
		reimbursement_chief_medical_officer
				.add(PROCESS_CLAIM_REQUEST_BENEFITSFINANCIAL);

		reimbursement_chief_medical_officer.add(MANAGE_CLAIMS);
		reimbursement_chief_medical_officer.add(RE_OPEN_CLAIM_ROD_LEVEL);
		reimbursement_chief_medical_officer
				.add(CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL);
		reimbursement_chief_medical_officer.add(CLOSE_CLAIM_ROD_LEVEL);
		reimbursement_chief_medical_officer.add(CLOSE_CLAIM_CLAIM_LEVEL);
		reimbursement_chief_medical_officer.add(RE_OPEN_CLAIM_CLAIM_LEVEL);
		reimbursement_chief_medical_officer.add(ALLOW_RECONSIDERATION);
		reimbursement_chief_medical_officer.add(CLAIM_WISE_ALLOW_APPROVAL);
		reimbursement_chief_medical_officer.add(HOLD_MONITOR_SCREEN);
		reimbursement_chief_medical_officer.add(UPDATE_ROD_DETAILS);

		reimbursement_chief_medical_officer.add(MISC);
		reimbursement_chief_medical_officer.add(REGISTER_CLAIM_REFUND);
		reimbursement_chief_medical_officer.add(PROCESS_CLAIM_REFUND);

		reimbursement_chief_medical_officer.add(CANCEL_ACKNOWLEDGEMENT);

		List<String> query_approver = new ArrayList<String>();

		query_approver.add(REIMBURSEMENT_PAGE);
		query_approver.add(QUERY_REJECTION);
		query_approver.add(PROCESS_DRAFT_QUERY_LETTER);
		query_approver.add(SEARCH_CLAIM);

		List<String> query_draftsperson = new ArrayList<String>();

		query_draftsperson.add(REIMBURSEMENT_PAGE);
		query_draftsperson.add(QUERY_REJECTION);
		query_draftsperson.add(DRAFT_QUERY_LETTER_SEARCH);
		// query_draftsperson.add(GENERATE_REMINDER_LETTER);
		query_draftsperson.add(SEARCH_CLAIM);

		List<String> reimbursement_claims_co_ordinator = new ArrayList<String>();

		reimbursement_claims_co_ordinator.add(HEALTH_PAGE);
		reimbursement_claims_co_ordinator.add(CASHLESS_PAGE);
		reimbursement_claims_co_ordinator.add(INTIMATION_HEADER);
		reimbursement_claims_co_ordinator.add(SEARCH_POLICY);
		reimbursement_claims_co_ordinator.add(SEARCH_INTIMATION);
		reimbursement_claims_co_ordinator.add(INTIMATION_VIEW_DETAILS);
		reimbursement_claims_co_ordinator.add(PROCESS_CLAIM_HEADER);
		reimbursement_claims_co_ordinator.add(UPLOAD_TRANSLATED_DOCUMENTS);
		reimbursement_claims_co_ordinator.add(DATA_CODING_DATA_CORRECTION);
		reimbursement_claims_co_ordinator.add(FLAG_RECONSIDERATION_REQUEST);
		reimbursement_claims_co_ordinator.add(DATA_CODING_DATA_CORRECTION_HISTORICAL);
		reimbursement_claims_co_ordinator.add(DATA_CODING_DATA_CORRECTION_PRIORITY);
		// reimbursement_claims_co_ordinator.add(REIMBURSEMENT_PAGE);
		// reimbursement_claims_co_ordinator.add(PROCESS_CLAIM_HEADERR3);
		// reimbursement_claims_co_ordinator.add(UPLOAD_TRANSLATED_DOCUMENTSR3);
		reimbursement_claims_co_ordinator.add(SEARCH_CLAIM);

		List<String> claims_billing_officer = new ArrayList<String>();

		claims_billing_officer.add(REIMBURSEMENT_PAGE);
		claims_billing_officer.add(BILLING);
		claims_billing_officer.add(PROCESS_CLAIM_BILLING);
		claims_billing_officer.add(PROCESS_CLAIM_REQUEST_BENEFITSBILLING);
		claims_billing_officer.add(SEARCH_CLAIM);

		List<String> claims_financial_approver = new ArrayList<String>();

		claims_financial_approver.add(REIMBURSEMENT_PAGE);
		claims_financial_approver.add(FINANCIAL_APPROVAL);
		claims_financial_approver.add(PROCESS_CLAIM_FINANCIALS);
		claims_financial_approver.add(PROCESS_CLAIM_REQUEST_BENEFITSFINANCIAL);
		claims_financial_approver.add(SEARCH_CLAIM);

		List<String> investigation_approver = new ArrayList<String>();

		investigation_approver.add(REIMBURSEMENT_PAGE);
		investigation_approver.add(SHOW_INVESTIGATION);
		investigation_approver.add(SEARCH_PROCESS_INVESTIGATION_INITIATED);
		investigation_approver.add(SEARCH_CLAIM);

		List<String> investigation_draftsperson = new ArrayList<String>();

		investigation_draftsperson.add(REIMBURSEMENT_PAGE);
		investigation_draftsperson.add(SHOW_INVESTIGATION);
		investigation_draftsperson.add(SEARCH_PROCESS_INVESTIGATION_INITIATED);
		investigation_draftsperson.add(SHOW_DRAFT_INVESTIGATION);
		investigation_draftsperson.add(SHOW_ASSIGN_INVESTIGATION);
		investigation_draftsperson
				.add(SEARCH_ACKNOWLEDGE_INVESTIGATION_COMPLETED);
		investigation_draftsperson.add(SEARCH_CLAIM);
		investigation_draftsperson.add(SEARCH_INVESTIGATION_GRADING);

		List<String> operating_office_claims_co_ordinator = new ArrayList<String>();

		operating_office_claims_co_ordinator.add(HEALTH_PAGE);
		operating_office_claims_co_ordinator.add(CASHLESS_PAGE);

		operating_office_claims_co_ordinator.add(INTIMATION_HEADER);
		operating_office_claims_co_ordinator.add(SEARCH_POLICY);
		operating_office_claims_co_ordinator.add(SEARCH_INTIMATION);
		operating_office_claims_co_ordinator.add(INTIMATION_VIEW_DETAILS);
		operating_office_claims_co_ordinator.add(REGISTER_CLAIM_HEADER);
		operating_office_claims_co_ordinator.add(CONVERT_CLAIM_OUTSIDE_PROCESS);

		operating_office_claims_co_ordinator.add(REIMBURSEMENT_PAGE);
		operating_office_claims_co_ordinator.add(ROD);
		operating_office_claims_co_ordinator
				.add(SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED);
		operating_office_claims_co_ordinator.add(SEARCH_CLAIM);

		List<String> rejection_approver = new ArrayList<String>();

		rejection_approver.add(HEALTH_PAGE);
		rejection_approver.add(CASHLESS_PAGE);
		rejection_approver.add(CASHLESS_PROCESS_HEADER);
		rejection_approver.add(PROCESS_PREAUTH_REJECTION);
		rejection_approver.add(REIMBURSEMENT_PAGE);
		rejection_approver.add(QUERY_REJECTION);
		rejection_approver.add(PROCESS_DRAFT_REJECTION_LETTER);
		rejection_approver.add(SEARCH_CLAIM);

		List<String> rejection_draftsperson = new ArrayList<String>();

		rejection_draftsperson.add(REIMBURSEMENT_PAGE);
		rejection_draftsperson.add(QUERY_REJECTION);
		rejection_draftsperson.add(DRAFT_REJECTION_LETTER);
		rejection_draftsperson.add(SEARCH_CLAIM);

		List<String> claims_admin = new ArrayList<String>();

		// ----------CLS----------------
		claims_admin.add(HEALTH_PAGE);
		claims_admin.add(CASHLESS_PAGE);

		claims_admin.add(INTIMATION_HEADER);
		claims_admin.add(SEARCH_POLICY);
		claims_admin.add(SEARCH_INTIMATION);
		claims_admin.add(INTIMATION_VIEW_DETAILS);
		claims_admin.add(UPDATE_HOSPITAL_INFORMATION);
		claims_admin.add(EDIT_HOSPITAL_INFORMATION);

		claims_admin.add(REGISTER_CLAIM_HEADER);
		claims_admin.add(SEARCH_REGISTER_CLAIM);
		claims_admin.add(GENERATE_COVERINGLETTER);
		claims_admin.add(CONVERT_CLAIM);
		claims_admin.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
		claims_admin.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);

		claims_admin.add(CASHLESS_PROCESS_HEADER);
		claims_admin.add(PROCESS_PREAUTH_REJECTION);
		claims_admin.add(PROCESS_PRE_MEDICAL);
		claims_admin.add(PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		claims_admin.add(PROCESS_PREAUTH);
		claims_admin.add(PROCESS_PREAUTH_AUTO_ALLOCATION);
		claims_admin.add(PROCESS_ENHANCEMENT);
		claims_admin.add(PROCESS_PED_QUERY);
		claims_admin.add(DOWNSIZE_PRE_AUTH);
		claims_admin.add(WITHDRAW_PRE_AUTH);
		claims_admin.add(DOWNSIZE_PRE_AUTH_REQUEST);
		
		claims_admin.add(FIELD_VISIT_CASHLESS);
		claims_admin.add(FIELD_VISIT_REPESENTATION);

		claims_admin.add(ENDORSEMENTS_HEADER);
		claims_admin.add(PROCESS_PED_REQUEST_PROCESS);
		claims_admin.add(PROCESS_PED_REQUEST_APPROVE);

		claims_admin.add(PROCESS_CLAIM_HEADER);
		claims_admin.add(UPLOAD_TRANSLATED_DOCUMENTS);
		claims_admin.add(DATA_CODING_DATA_CORRECTION);
		claims_admin.add(UPLOAD_TRANSLATED_DOCUMENTS);		
		claims_admin.add(FLAG_RECONSIDERATION_REQUEST);
		claims_admin.add(DATA_CODING_DATA_CORRECTION_HISTORICAL);
		claims_admin.add(DATA_CODING_DATA_CORRECTION_PRIORITY);
		claims_admin.add(SPECIALIST_PROCESSING_HEADER);
		claims_admin.add(ADVISE_ON_PED);
		claims_admin.add(SUBMIT_SPECIALLIST_ADVISE);

		claims_admin.add(SEARCH_CLAIM);
		claims_admin.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		claims_admin.add(PROCESS_FLP_AUTO_ALLOCATION);

		// -----------------R3----------------------

		claims_admin.add(REIMBURSEMENT_PAGE);

		claims_admin.add(SHOW_INVESTIGATION);
		claims_admin.add(SEARCH_PROCESS_INVESTIGATION_INITIATED);
		claims_admin.add(SHOW_DRAFT_INVESTIGATION);
		claims_admin.add(SHOW_ASSIGN_INVESTIGATION);
		claims_admin.add(SEARCH_ACKNOWLEDGE_INVESTIGATION_COMPLETED);
		claims_admin.add(SEARCH_INVESTIGATION_GRADING);

		claims_admin.add(ROD);
		claims_admin.add(SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED);
		claims_admin.add(CREATE_ROD);
		claims_admin.add(CREATE_ONLINE_ROD);
		claims_admin.add(ENTER_BILL_DETAILS);
		claims_admin.add(UPLOAD_INVESTIGATION_REPORT);

		claims_admin.add(QUERY_REJECTION);
		claims_admin.add(DRAFT_QUERY_LETTER_SEARCH);
		claims_admin.add(PROCESS_DRAFT_QUERY_LETTER);
		// claims_admin.add(GENERATE_REMINDER_LETTER_CLAIMWISE);
		claims_admin.add(DRAFT_REJECTION_LETTER);
		claims_admin.add(PROCESS_DRAFT_REJECTION_LETTER);

		claims_admin.add(SPECIALIST_PROCESSING);
		claims_admin.add(SUBMIT_SPECIALIST_ADVISE);

		claims_admin.add(MEDICAL_APPROVAL);
		claims_admin.add(PROCESS_CLAIM_REQUEST_ZONAL_REVIEW);
		claims_admin.add(PROCESS_CLAIM_REQUEST);

		claims_admin.add(BILLING);
		claims_admin.add(PROCESS_CLAIM_BILLING);
		claims_admin.add(PROCESS_CLAIM_REQUEST_BENEFITSBILLING);

		claims_admin.add(FINANCIAL_APPROVAL);
		claims_admin.add(PROCESS_CLAIM_FINANCIALS);
		claims_admin.add(PROCESS_CLAIM_REQUEST_BENEFITSFINANCIAL);

		claims_admin.add(MANAGE_CLAIMS);
		claims_admin.add(RE_OPEN_CLAIM_ROD_LEVEL);
		claims_admin.add(CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL);
		claims_admin.add(CLOSE_CLAIM_ROD_LEVEL);
		claims_admin.add(CLOSE_CLAIM_CLAIM_LEVEL);
		claims_admin.add(RE_OPEN_CLAIM_CLAIM_LEVEL);
		claims_admin.add(ALLOW_RECONSIDERATION);
		claims_admin.add(CLAIM_WISE_ALLOW_APPROVAL);
		claims_admin.add(HOLD_MONITOR_SCREEN);
		claims_admin.add(UPDATE_ROD_DETAILS);

		claims_admin.add(MISC);
		claims_admin.add(REGISTER_CLAIM_REFUND);
		claims_admin.add(PROCESS_CLAIM_REFUND);

		claims_admin.add(CANCEL_ACKNOWLEDGEMENT);

		claims_admin.add(ADD_ADDITIONAL_DOC);
		claims_admin.add(PHYSICAL_DOCUMENT);
		claims_admin.add(PHYSICAL_DOCUMENT_CHECKER);
		
		claims_admin.add(PAYMENT_PROCESS_CPU);
		claims_admin.add(SEARCH_OR_UPLOAD_DOCUMENTS);
		claims_admin.add(PROCESSING_PAYMENT);

		List<String> representative = new ArrayList<String>();
		representative.add(INTIMATION_HEADER);
		representative.add(SEARCH_POLICY);
		representative.add(SEARCH_INTIMATION);
		representative.add(INTIMATION_VIEW_DETAILS);
		representative.add(UPDATE_HOSPITAL_INFORMATION);
		representative.add(EDIT_HOSPITAL_INFORMATION);
		representative.add(SEARCH_CLAIM);
		representative.add(STARFAX_SIMULATION);

		List<String> branch_office = new ArrayList<String>();
		branch_office.add(REIMBURSEMENT_PAGE);

		branch_office.add(SEARCH_CLAIM);
		branch_office.add(OP_HEALTH_CHECK_PROCESSING);
		branch_office.add(EXPIRED_POLICIES_CLAIM_OP);
		branch_office.add(REGISTER_CLAIM_OP);
		branch_office.add(PAYMENT_PROCESS_OP);
		branch_office.add(CREATE_BATCH_OP);
		branch_office.add(CREATE_SETTLEMENT_BATCH_OP);


		List<String> area_office = new ArrayList<String>();
		area_office.add(REIMBURSEMENT_PAGE);

		area_office.add(SEARCH_CLAIM);
		area_office.add(OP_HEALTH_CHECK_PROCESSING);
		//area_office.add(EXPIRED_POLICIES_CLAIM_OP);
		area_office.add(PORCESS_CLAIM_OP);
		area_office.add(PAYMENT_PROCESS_OP);
		area_office.add(CREATE_BATCH_OP);
		area_office.add(CREATE_SETTLEMENT_BATCH_OP);


		// TODO:
		// TODO:
		// TODO:
		// TODO:

		List<String> rrcProcessorList = new ArrayList<String>();
		rrcProcessorList.add(REIMBURSEMENT_PAGE);

		rrcProcessorList.add(REWARD_RECOGNITION_CELL);
		rrcProcessorList.add(PROCESS_RRC_REQUEST);
		rrcProcessorList.add(MODIFY_RRC_REQUEST);
		rrcProcessorList.add(SEARCH_RRC_REQUEST);
		
		//Dinesh
		rrcProcessorList.add(RRC_STATUS_SCREEN);

		// rrcProcessorList.add(REVIEW_RRC_REQUEST);

		List<String> rrcReviewerList = new ArrayList<String>();
		rrcReviewerList.add(REIMBURSEMENT_PAGE);
		rrcReviewerList.add(REWARD_RECOGNITION_CELL);
		rrcReviewerList.add(REVIEW_RRC_REQUEST);
		rrcReviewerList.add(MODIFY_RRC_REQUEST);
		rrcReviewerList.add(SEARCH_RRC_REQUEST);
		
		//Dinesh
		rrcReviewerList.add(RRC_STATUS_SCREEN);

		List<String> reportList = new ArrayList<String>();
		reportList.add(HEALTH_REPORT);
		reportList.add(SEARCH_CLAIM_POLICY_REPORT);
		reportList.add(EXECUTIVE_STATUS_REPORT);
		reportList.add(EXECUTIVE_STATUS_SUMMARY_REPORT);
		reportList.add(SEARCH_PREAUTH_CPUWISE_REPORT);
		reportList.add(CLAIM_STATUS_CPUWISE_REPORT);
		reportList.add(CLAIM_STATUS_CPUWISE_REPORT);
		reportList.add(HELP_DESK_STATUS_REPORT);
		reportList.add(CLAIMS_DAILY_REPORT);
		reportList.add(PLANNED_ADMISSION_REPORT);
		reportList.add(INTIMATED_RISK_DETAILS_REPORT);
		reportList.add(MEDICAL_AUDIT_CLAIM_STATUS_REPORT);
		reportList.add(MEDICAL_AUDIT_CASHLESS_ISSUANCE_REPORT);
		reportList.add(INTIMATION_ALTERNATE_CPU_REPORT);
		reportList.add(HOSPITAL_WISE_REPORT);
		reportList.add(CALL_CENTER_DASH_BOARD);
		reportList.add(AUTOMATION_DASHBOARD);
		reportList.add(ADMINISTRATIVE_DASH_BOARD);
		reportList.add(PAYMENT_PROCESS_REPORT);
		reportList.add(FVR_ASSIGNMENT_REPORT);
		reportList.add(HOSPITAL_INTIMATION_STATUS);
		reportList.add(MEDICAL_MAIL_STATUS);
		reportList.add(DAILY_REPORT);
		reportList.add(CPU_WISE_PERFORMANCE_REPORT);
		reportList.add(AGENT_BROKER_REPORT);
		reportList.add(OP_CLAIM_REPORT);

		/*
		 * List<String> paClaimList = new ArrayList<String>();
		 * paClaimList.add(PA_CLAIMS)
		 */;

		List<String> zonalReimClsCoordinator = new ArrayList<String>();
		zonalReimClsCoordinator.add(HEALTH_PAGE);
		zonalReimClsCoordinator.add(CASHLESS_PAGE);
		zonalReimClsCoordinator.add(PROCESS_CLAIM_HEADER);
		zonalReimClsCoordinator.add(UPLOAD_TRANSLATED_DOCUMENTS);
		zonalReimClsCoordinator.add(DATA_CODING_DATA_CORRECTION);
		zonalReimClsCoordinator.add(FLAG_RECONSIDERATION_REQUEST);
		zonalReimClsCoordinator.add(DATA_CODING_DATA_CORRECTION_HISTORICAL);
		zonalReimClsCoordinator.add(DATA_CODING_DATA_CORRECTION_PRIORITY);
		zonalReimClsCoordinator.add(SEARCH_CLAIM);

		List<String> reminderOfficer = new ArrayList<String>();
		reminderOfficer.add(HEALTH_PAGE);
		reminderOfficer.add(CASHLESS_PAGE);
		reminderOfficer.add(REGISTER_CLAIM_HEADER);
		reminderOfficer.add(REMINDER_LETTERS);
		reminderOfficer.add(GENERATE_REMINDER_LETTER_CLAIM_WISE);
		reminderOfficer.add(GENERATE_REMINDER_LETTER_BULK);
		reminderOfficer.add(SEARCH_CLAIM);

		List<String> zonalRodOfficer = new ArrayList<String>();

		zonalRodOfficer.add(REIMBURSEMENT_PAGE);
		zonalRodOfficer.add(ROD);
		zonalRodOfficer.add(UPLOAD_INVESTIGATION_REPORT);
		zonalRodOfficer.add(SEARCH_CLAIM);

		List<String> cmaMedicalApprover = new ArrayList<String>();
		cmaMedicalApprover.add(HEALTH_PAGE);
		cmaMedicalApprover.add(CASHLESS_PAGE);
		cmaMedicalApprover.add(CASHLESS_PROCESS_HEADER);
		cmaMedicalApprover.add(PROCESS_PREAUTH_REJECTION);
		cmaMedicalApprover.add(PROCESS_PREAUTH);
		cmaMedicalApprover.add(PROCESS_PREAUTH_AUTO_ALLOCATION);
		cmaMedicalApprover.add(PROCESS_ENHANCEMENT);
		cmaMedicalApprover.add(PROCESS_PED_QUERY);
		cmaMedicalApprover.add(DOWNSIZE_PRE_AUTH);
		cmaMedicalApprover.add(WITHDRAW_PRE_AUTH);
		cmaMedicalApprover.add(DOWNSIZE_PRE_AUTH_REQUEST);
		
		cmaMedicalApprover.add(ENDORSEMENTS_HEADER);
		cmaMedicalApprover.add(PROCESS_PED_REQUEST_APPROVE);

		cmaMedicalApprover.add(SPECIALIST_PROCESSING_HEADER);
		cmaMedicalApprover.add(ADVISE_ON_PED);

		cmaMedicalApprover.add(SEARCH_CLAIM);
		cmaMedicalApprover.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		cmaMedicalApprover.add(PROCESS_FLP_AUTO_ALLOCATION);
		
		List<String> rmaMedicalApprover = new ArrayList<String>();
		rmaMedicalApprover.add(HEALTH_PAGE);
		rmaMedicalApprover.add(CASHLESS_PAGE);
		rmaMedicalApprover.add(CASHLESS_PROCESS_HEADER);
		rmaMedicalApprover.add(PROCESS_PREAUTH_REJECTION);
		rmaMedicalApprover.add(PROCESS_PED_QUERY);

		rmaMedicalApprover.add(ENDORSEMENTS_HEADER);
		rmaMedicalApprover.add(PROCESS_PED_REQUEST_APPROVE);

		rmaMedicalApprover.add(SPECIALIST_PROCESSING_HEADER);
		rmaMedicalApprover.add(ADVISE_ON_PED);

		rmaMedicalApprover.add(REIMBURSEMENT_PAGE);

		rmaMedicalApprover.add(MEDICAL_APPROVAL);
		rmaMedicalApprover.add(PROCESS_CLAIM_REQUEST);
		rmaMedicalApprover.add(SEARCH_CLAIM);

		List<String> amaMedicalApprover = new ArrayList<String>();

		amaMedicalApprover.add(HEALTH_PAGE);
		amaMedicalApprover.add(CASHLESS_PAGE);
		amaMedicalApprover.add(CASHLESS_PROCESS_HEADER);
		amaMedicalApprover.add(PROCESS_PREAUTH_REJECTION);
		amaMedicalApprover.add(PROCESS_PREAUTH);
		amaMedicalApprover.add(PROCESS_PREAUTH_AUTO_ALLOCATION);
		amaMedicalApprover.add(PROCESS_ENHANCEMENT);
		amaMedicalApprover.add(PROCESS_PED_QUERY);
		amaMedicalApprover.add(DOWNSIZE_PRE_AUTH);
		amaMedicalApprover.add(WITHDRAW_PRE_AUTH);
		amaMedicalApprover.add(DOWNSIZE_PRE_AUTH_REQUEST);
		
		amaMedicalApprover.add(ENDORSEMENTS_HEADER);
		amaMedicalApprover.add(PROCESS_PED_REQUEST_APPROVE);

		amaMedicalApprover.add(SPECIALIST_PROCESSING_HEADER);
		amaMedicalApprover.add(ADVISE_ON_PED);

		amaMedicalApprover.add(REIMBURSEMENT_PAGE);

		amaMedicalApprover.add(MEDICAL_APPROVAL);
		amaMedicalApprover.add(PROCESS_CLAIM_REQUEST);

		amaMedicalApprover.add(SEARCH_CLAIM);
		amaMedicalApprover.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		amaMedicalApprover.add(PROCESS_FLP_AUTO_ALLOCATION);
		// TODO:

		MENU_ASSOCIATED_WITH_ROLE.put(REPRESENTATIVE, representative);
		MENU_ASSOCIATED_WITH_ROLE.put(REGISTRATION_OFFICER,
				registrationOfficerMenu);
		MENU_ASSOCIATED_WITH_ROLE.put(CLAIMS_HEAD, claimHeadMenu);

		MENU_ASSOCIATED_WITH_ROLE.put(CASHLESS_PROCESSING_HEAD,
				cashless_processing_head);
		MENU_ASSOCIATED_WITH_ROLE.put(CASHLESS_CHIEF_MEDICAL_OFFICER,
				cashless_chief_medical_officer);
		MENU_ASSOCIATED_WITH_ROLE.put(SENIOR_MEDICAL_APPROVER,
				senior_medical_approver);
		MENU_ASSOCIATED_WITH_ROLE.put(MEDICAL_APPROVER, medical_approver);
		MENU_ASSOCIATED_WITH_ROLE.put(CLS_SETTLEMENT_MEDICAL_APPRO,
				cls_settlement_medical_approver);
		MENU_ASSOCIATED_WITH_ROLE.put(SPECIALIST_DOCTOR, specialist_doctor);
		MENU_ASSOCIATED_WITH_ROLE.put(FIELD_VISIT_HEAD, field_visit_head);
		MENU_ASSOCIATED_WITH_ROLE.put(REGISTRATION_TEAM_LEAD,
				registration_team_lead);
		MENU_ASSOCIATED_WITH_ROLE.put(ROD_TEAM_LEAD, rod_team_lead);
		MENU_ASSOCIATED_WITH_ROLE.put(ROD_OFFICER, rod_officer);
		MENU_ASSOCIATED_WITH_ROLE.put(CASHLESS_CLAIMS_CO_ORDINATOR,
				cashless_claims_co_ordinator);
		MENU_ASSOCIATED_WITH_ROLE.put(CUSTOMER_CARE_REPRESENTATIVE,
				customer_care_representative);
		MENU_ASSOCIATED_WITH_ROLE.put(HMS_PMS, hms_pms);
		MENU_ASSOCIATED_WITH_ROLE.put(PRE_MEDICAL, pre_medical);
		MENU_ASSOCIATED_WITH_ROLE.put(NON_MEDICAL_REJECTION,
				non_medical_rejection);
		MENU_ASSOCIATED_WITH_ROLE.put(SEND_TO_PROCESSING_NON_MEDICAL,
				send_to_processing_non_medical);
		MENU_ASSOCIATED_WITH_ROLE.put(CLAIMS_ACCOUNTS_OFFICER,
				claims_accounts_officer);
		MENU_ASSOCIATED_WITH_ROLE.put(SENIOR_CLAIMS_ACCOUNTS_OFFICER,
				senior_claims_accounts_officer);
		MENU_ASSOCIATED_WITH_ROLE.put(PED_PROCESSOR, ped_processor);
		MENU_ASSOCIATED_WITH_ROLE.put(PED_APPROVER, ped_approver);
		MENU_ASSOCIATED_WITH_ROLE.put(ZONAL_CLAIMS_HEAD, zonal_claims_head);
		MENU_ASSOCIATED_WITH_ROLE.put(ZONAL_MEDICAL_HEAD, zonal_medical_head);
		MENU_ASSOCIATED_WITH_ROLE.put(ZONAL_SENIOR_MEDICAL_APPROVER,
				zonal_senior_medical_approver);
		MENU_ASSOCIATED_WITH_ROLE.put(ZONAL_MEDICAL_APPROVER,
				zonal_medical_approver);
		MENU_ASSOCIATED_WITH_ROLE.put(FIELD_VISIT_DOCTOR_PERSONNEL,
				field_visit_doctor_personnel);
		MENU_ASSOCIATED_WITH_ROLE.put(ZONAL_FIELD_VISIT_CO_ORDINATOR,
				zonal_field_visit_co_ordinator);
		MENU_ASSOCIATED_WITH_ROLE.put(ZONAL_REGISTRATION_TEAM_LEAD,
				zonal_registration_team_lead);
		MENU_ASSOCIATED_WITH_ROLE.put(ZONAL_REGISTRATION_OFFICER,
				zonal_registration_officer);
		MENU_ASSOCIATED_WITH_ROLE.put(ZONAL_HOSPITAL_CO_ORDINATOR,
				zonal_hospital_co_ordinator);
		MENU_ASSOCIATED_WITH_ROLE.put(ZONAL_CLS_CLAIMS_COORDINATOR,
				zonal_cashless_claims_co_ordinator);
		MENU_ASSOCIATED_WITH_ROLE.put(ZONAL_QUALITY_CLAIMS_AUDITOR,
				zonal_quality_claims_auditor);
		MENU_ASSOCIATED_WITH_ROLE.put(REIMBURSEMENT_PROCESSING_HEAD,
				reimbursement_processing_head);
		MENU_ASSOCIATED_WITH_ROLE.put(REIMBURSEMENT_CHIEF_MEDICAL_OFFICER,
				reimbursement_chief_medical_officer);
		MENU_ASSOCIATED_WITH_ROLE.put(QUERY_APPROVER, query_approver);
		MENU_ASSOCIATED_WITH_ROLE.put(QUERY_DRAFTSPERSON, query_draftsperson);
		MENU_ASSOCIATED_WITH_ROLE.put(REIMBURSEMENT_CLAIMS_CO_ORDINATOR,
				reimbursement_claims_co_ordinator);
		MENU_ASSOCIATED_WITH_ROLE.put(CLAIMS_BILLING_OFFICER,
				claims_billing_officer);
		MENU_ASSOCIATED_WITH_ROLE.put(CLAIMS_FINANCIAL_APPROVER,
				claims_financial_approver);
		MENU_ASSOCIATED_WITH_ROLE.put(INVESTIGATION_APPROVER,
				investigation_approver);
		MENU_ASSOCIATED_WITH_ROLE.put(INVESTIGATION_DRAFTSPERSON,
				investigation_draftsperson);
		MENU_ASSOCIATED_WITH_ROLE.put(OPR_OFFICE_CLAIMS_CO_ORDINATOR,
				operating_office_claims_co_ordinator);
		MENU_ASSOCIATED_WITH_ROLE.put(REJECTION_APPROVER, rejection_approver);
		MENU_ASSOCIATED_WITH_ROLE.put(REJECTION_DRAFTSPERSON,
				rejection_draftsperson);
		MENU_ASSOCIATED_WITH_ROLE.put(CLAIMS_ADMIN, claims_admin);
		MENU_ASSOCIATED_WITH_ROLE.put(BRANCH_OFFICE, branch_office);
		MENU_ASSOCIATED_WITH_ROLE.put(AREA_OFFICE, area_office);
		MENU_ASSOCIATED_WITH_ROLE.put(RRC_PROCESSOR, rrcProcessorList);
		MENU_ASSOCIATED_WITH_ROLE.put(RRC_REVIEWER, rrcReviewerList);
		MENU_ASSOCIATED_WITH_ROLE.put(REPORT_GROUP, reportList);
		MENU_ASSOCIATED_WITH_ROLE.put(ZONAL_REIMBURSEMENT_CLAIMS_CO_ORDINATOR,
				zonalReimClsCoordinator);
		MENU_ASSOCIATED_WITH_ROLE.put(REMINDER_OFFICER, reminderOfficer);
		MENU_ASSOCIATED_WITH_ROLE.put(ZONAL_ROD_OFFICER, zonalRodOfficer);
		MENU_ASSOCIATED_WITH_ROLE.put(CMA_MEDICAL_APPROVER, cmaMedicalApprover);
		MENU_ASSOCIATED_WITH_ROLE.put(RMA_MEDICAL_APPROVER, rmaMedicalApprover);
		MENU_ASSOCIATED_WITH_ROLE.put(AMA_MEDICAL_APPROVER, amaMedicalApprover);

	}

	public MenuItem getMenus() {
		List<String> menusforRep = getClaimsHeadPermission();
		for (String menu_id : menusforRep) {
			MenuItem menuItem = MENU_ITEMS_PERMISSION_MAPPING.get(menu_id);
			if (null != menuItem) {
				menuItem.setAccessFlag(true);
			}
		}
		return rootMenu;
	}

	private static void resetAllMenus() {
		for (String key : MENU_ITEMS_PERMISSION_MAPPING.keySet()) {
			MENU_ITEMS_PERMISSION_MAPPING.get(key).setAccessFlag(false);
		}
	}

	// public static MenuItem getMenusBasedOnUserLogin(ImsUser imsUser)
	// {
	// List<String> userRoleList = imsUser.getFilteredRoles();
	// List<String> permissionList = new ArrayList<String>();
	// resetAllMenus();
	// if (userRoleList != null)
	// {
	// for (String string : userRoleList) {
	//
	// List<String> permissionTmpList = MENU_ASSOCIATED_WITH_ROLE.get(string);
	// if (permissionTmpList != null)
	// {
	// if (permissionTmpList.isEmpty())
	// {
	// permissionTmpList.addAll(getClaimsHeadPermission());
	// }
	// else
	// {
	// for (String string2 : permissionTmpList) {
	// permissionList.add(string2);
	// }
	// }
	// }
	// // else
	// // {
	// // permissionList.addAll(getClaimsHeadPermission());
	// // }
	// }
	// }
	//
	// for (String menu_id : permissionList) {
	// MenuItem menuItem = MENU_ITEMS_PERMISSION_MAPPING.get(menu_id);
	// if (null != menuItem)
	// {
	// menuItem.setAccessFlag(true);
	// }
	// }
	// rootMenu.setAccessFlag(true);
	// return rootMenu;
	// }

	public static MenuItem getMenusBasedOnUserLogin(ImsUser imsUser, String bancsPaymentFlag) {
		List<String> userRoleList = imsUser.getFilteredRoles();
		List<String> permissionList = new ArrayList<String>();
		resetAllMenus();
		
		if (userRoleList != null) {
			for (String string : userRoleList) {
				Properties userRoleProperty = BPMClientContext.menuProperty;
				List<String> permissionTmpList = new ArrayList<String>();
				if (userRoleProperty != null) {
					String menus = userRoleProperty.getProperty(string);
					if (menus != null) {
						String[] split = menus.split(",");
						for (String eachString : split) {
							
							if(eachString != null && !eachString.trim().isEmpty())
								permissionList.add(eachString.trim());
						}
					}
				}
//				System.out.println(permissionList);
				// List<String> permissionTmpList =
				// MENU_ASSOCIATED_WITH_ROLE.get(string);
				if (permissionTmpList != null) {
					if (permissionTmpList.isEmpty()) {
						permissionTmpList.addAll(getClaimsHeadPermission());
					} else {
						for (String string2 : permissionTmpList) {
							if(string2 != null && !string2.trim().isEmpty())
								permissionList.add(string2.trim());
						}
					}
				}
				// else
				// {
				// permissionList.addAll(getClaimsHeadPermission());
				// }
			}
		}

		for (String menu_id : permissionList) {
			MenuItem menuItem = MENU_ITEMS_PERMISSION_MAPPING.get(menu_id);
			if(menuItem != null && menuItem.getCode().equalsIgnoreCase(SHAConstants.PAYMENT_VERIFICATION_LVL1)) {
				if(bancsPaymentFlag != null && bancsPaymentFlag.equalsIgnoreCase(SHAConstants.YES_FLAG)) {
				menuItem.setAccessFlag(false);
				}
				else{
					menuItem.setAccessFlag(true);
				}
			}else if (null != menuItem) {
				menuItem.setAccessFlag(true);
			}
		}
		rootMenu.setAccessFlag(true);
		return rootMenu;
	}

	// public static MenuItem getMenusBasedOnUserLogin(ImsUser imsUser)
	// {
	// List<String> userRoleList = imsUser.getFilteredRoles();
	// List<String> permissionList = new ArrayList<String>();
	// resetAllMenus();
	// if (userRoleList != null)
	// {
	// for (String string : userRoleList) {
	//
	// List<String> permissionTmpList = MENU_ASSOCIATED_WITH_ROLE.get(string);
	// if (permissionTmpList != null)
	// {
	// if (permissionTmpList.isEmpty())
	// {
	// permissionTmpList.addAll(getClaimsHeadPermission());
	// }
	// else
	// {
	// for (String string2 : permissionTmpList) {
	// permissionList.add(string2);
	// }
	// }
	// }
	// // else
	// // {
	// // permissionList.addAll(getClaimsHeadPermission());
	// // }
	// }
	// }
	//
	// for (String menu_id : permissionList) {
	// MenuItem menuItem = MENU_ITEMS_PERMISSION_MAPPING.get(menu_id);
	// if (null != menuItem)
	// {
	// menuItem.setAccessFlag(true);
	// }
	// }
	// rootMenu.setAccessFlag(true);
	// return rootMenu;
	// }

	private List<String> getMenusforRep() {
		List<String> rep_menus = new ArrayList<String>();
		rep_menus.add(HOME_PAGE);
		rep_menus.add(INTIMATION_HEADER);
		rep_menus.add(SEARCH_POLICY);
		rep_menus.add(SEARCH_INTIMATION);
		rep_menus.add(INTIMATION_VIEW_DETAILS);
		rep_menus.add(STARFAX_SIMULATION);
		rep_menus.add(UPDATE_HOSPITAL_INFORMATION);
		rep_menus.add(EDIT_HOSPITAL_INFORMATION);
		return rep_menus;
	}

	private static List<String> getClaimsHeadPermission() {
		List<String> rep_menus = new ArrayList<String>();
		rep_menus.add(HOME_PAGE);
		rep_menus.add(INTIMATION_HEADER);
		// rep_menus.add(TEST_COMPONENT);
		rep_menus.add(HEALTH_PAGE);
		rep_menus.add(CASHLESS_PAGE);

		rep_menus.add(REIMBURSEMENT_PAGE);
		rep_menus.add(SEARCH_POLICY);
		rep_menus.add(SEARCH_INTIMATION);
		rep_menus.add(INTIMATION_VIEW_DETAILS);

		rep_menus.add(UPDATE_HOSPITAL_INFORMATION);
		rep_menus.add(EDIT_HOSPITAL_INFORMATION);
		rep_menus.add(REGISTER_CLAIM);
		rep_menus.add(CASHLESS_PROCESS_HEADER);
		rep_menus.add(ENDORSEMENTS_HEADER);
		rep_menus.add(VISIT_FIELD);
		rep_menus.add(PROCESS_CLAIM_BILLING);
		rep_menus.add(SPECIALIST_PROCESSING_HEADER);
		rep_menus.add(SEARCH_CLAIM);
		rep_menus.add(FIELD_VISIT_REIMBURSEMENT);
		rep_menus.add(INVESTIGATION);
		rep_menus.add(STARFAX_SIMULATION);
		rep_menus.add(FIELD_VISIT_CASHLESS);
		rep_menus.add(SHOW_PROCESS_FIELD_VISIT);
		rep_menus.add(SHOW_INVESTIGATION);
		rep_menus.add(SEARCH_PROCESS_INVESTIGATION_INITIATED);
		rep_menus.add(SHOW_DRAFT_INVESTIGATION);
		rep_menus.add(SHOW_ASSIGN_INVESTIGATION);
		rep_menus.add(SEARCH_ACKNOWLEDGE_INVESTIGATION_COMPLETED);
		rep_menus.add(SEARCH_INVESTIGATION_GRADING);
		rep_menus.add(ROD);
		rep_menus.add(SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED);
		rep_menus.add(CREATE_ROD);
		rep_menus.add(CREATE_ONLINE_ROD);
		rep_menus.add(ENTER_BILL_DETAILS);
		rep_menus.add(UPDATE_HOSPITAL_INFORMATION);
		rep_menus.add(QUERY_REJECTION);
		rep_menus.add(DRAFT_QUERY_LETTER_SEARCH);
		// rep_menus.add(GENERATE_REMINDER_LETTER_CLAIMWISE);
		rep_menus.add(DRAFT_REJECTION_LETTER);
		rep_menus.add(PROCESS_DRAFT_REJECTION_LETTER);
		rep_menus.add(SPECIALIST_PROCESSING);
		rep_menus.add(SUBMIT_SPECIALIST_ADVISE);
		rep_menus.add(MEDICAL_APPROVAL);
		rep_menus.add(PROCESS_CLAIM_REQUEST_ZONAL_REVIEW);
		rep_menus.add(PROCESS_CLAIM_REQUEST);
		rep_menus.add(BILLING);
		rep_menus.add(PROCESS_CLAIM_BILLING);
		rep_menus.add(PROCESS_CLAIM_REQUEST_BENEFITSBILLING);
		rep_menus.add(FINANCIAL_APPROVAL);
		rep_menus.add(PROCESS_CLAIM_FINANCIALS);
		rep_menus.add(PROCESS_CLAIM_REQUEST_BENEFITSFINANCIAL);
		rep_menus.add(MANAGE_CLAIMS);
		rep_menus.add(RE_OPEN_CLAIM_ROD_LEVEL);
		rep_menus.add(CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL);
		rep_menus.add(CLOSE_CLAIM_ROD_LEVEL);
		rep_menus.add(CLOSE_CLAIM_CLAIM_LEVEL);
		rep_menus.add(RE_OPEN_CLAIM_CLAIM_LEVEL);
		rep_menus.add(ALLOW_RECONSIDERATION);
		rep_menus.add(CLAIM_WISE_ALLOW_APPROVAL);
		rep_menus.add(HOLD_MONITOR_SCREEN);
		rep_menus.add(UPDATE_ROD_DETAILS);
		rep_menus.add(PAYMENT_PROCESSOR);
		rep_menus.add(CREATE_OR_SEARCH_LOT);
		rep_menus.add(CREATE_BATCH);
		rep_menus.add(PAYMENT_VERIFICATION_LVL1);
		rep_menus.add(PAYMENT_VERIFICATION_LVL2);
		rep_menus.add(PROCESS_PAYMENT_RETURNS);
		rep_menus.add(MISC);
		rep_menus.add(REGISTER_CLAIM_REFUND);
		rep_menus.add(PROCESS_CLAIM_REFUND);
		rep_menus.add(VIEW_DETAILS);
		// rep_menus.add(PROCESS_DRAFT_QUERY_LETTER_WIZARD);
		rep_menus.add(SEARCH_REGISTER_CLAIM);
		// rep_menus.add(SEARCH_REMINDER_LETTERS);
		rep_menus.add(GENERATE_REMINDER_LETTER_CLAIM_WISE);
		rep_menus.add(GENERATE_REMINDER_LETTER_BULK);
		rep_menus.add(GENERATE_COVERINGLETTER);
		rep_menus.add(CONVERT_CLAIM);
		rep_menus.add(CONVERT_CLAIM_BULK);
		rep_menus.add(CONVERT_CLAIM_OUTSIDE_PROCESS);
		rep_menus.add(ACKNOWLEDGE_HOSPITAL_COMMUNICATION);
		rep_menus.add(PROCESS_PREAUTH_REJECTION);
		rep_menus.add(PROCESS_PRE_MEDICAL);
		rep_menus.add(PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		rep_menus.add(PROCESS_PREAUTH);
		rep_menus.add(PROCESS_ENHANCEMENT);
		rep_menus.add(PROCESS_PED_QUERY);
		rep_menus.add(WITHDRAW_PRE_AUTH);
		rep_menus.add(DOWNSIZE_PRE_AUTH);
		rep_menus.add(DOWNSIZE_PRE_AUTH_REQUEST);
		rep_menus.add(FIELD_VISIT_REPESENTATION);
		rep_menus.add(PROCESS_PED_REQUEST_PROCESS);
		rep_menus.add(PROCESS_PED_REQUEST_APPROVE);
		rep_menus.add(UPLOAD_TRANSLATED_DOCUMENTS);
		rep_menus.add(DATA_CODING_DATA_CORRECTION);		
		rep_menus.add(DATA_CODING_DATA_CORRECTION_HISTORICAL);
		rep_menus.add(DATA_CODING_DATA_CORRECTION_PRIORITY);
		rep_menus.add(ADVISE_ON_PED);
		rep_menus.add(SUBMIT_SPECIALLIST_ADVISE);
		rep_menus.add(FIELD_VISIT_REPESENTATION);
		rep_menus.add(STARFAX_SIMULATION);
		rep_menus.add(CANCEL_ACKNOWLEDGEMENT);
		rep_menus.add(WITHDRAW_PRE_AUTH_POST_PROCESS);
		rep_menus.add(PROCESS_FLP_AUTO_ALLOCATION);
		return rep_menus;

	}
}
