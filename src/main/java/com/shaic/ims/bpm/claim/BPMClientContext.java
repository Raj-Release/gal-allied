package com.shaic.ims.bpm.claim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import oracle.xml.parser.v2.XMLElement;

import org.apache.regexp.REProgram;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.shaic.arch.SHAConstants;

public class BPMClientContext {

	private static Driver myDriver = new oracle.jdbc.OracleDriver();

	public static String dataDir = System.getProperty("jboss.server.data.dir");

	public static String USERID = "UserName";

	public static String PASSWORD = "password";

	public static final String USER_PASSWORD = "Star@123";
	
	public static final String USER_OBJECT = "user_object";
	
	public static final String EMAIL_ADDRESS = "email_address";
	
	public static final String KEYCLOAK_PRINCIPAL = "keycloakPrincipal";
	
	public static String BPM_URL = null;
	
	public static String WSO2_URL =null;
	
	public static String WSO2_SERVER =null;
	
	public static String ITEMS_PER_PAGE = null;
	
	public String PA_ACC_PROV_AMT = null;
	
	//public static String DB_CONNECTION_URL = null;
	
	public static String PREMIA_FLAG = null;
	
	public static String PREMIA_URL = null;
	
	public static String BANCS_URL = null;

	
	public static String HEALTH_CARD_NUMBER_URL = null;
	
	public static String DOCS_URL = null;
	
	public static String HOSP_DISCOUNT_URL = null;
	
	public static String DMS_VIEW_URL = null;
	
	public static String PORTABLITY_VIEW_URL = null;
	
	public static String PREMIA_CLAIM_VIEW_URL = null;
	
	public static String CLAIMS_DMS_URL = null;
	
	public static String MER_REPORT_URL = null;
	
	public static String INTIMATED_BY = null;
	
	public static String CLAIM_STATUS_PREMIA = null;
	
	public static String BPMN_TASK_USER =  null;
	
	public static String DB_USER_NAME =  null;
	
	//public static String DB_PASSWORD =  null;
	
	public static String BPMN_PASSWORD =  null;
	
	public static String BEDS_COUNT =  null;
	
	public static String INSERT_NON_NETWORK_HOSP = null;
	
	public static String BATCH_SIZE =  null;
	
	
	
	public static String PAGE_SIZE =  null;
	
	public static String PAGE_NUMBER =  null;
	
	public static String REFER_TO_FLP_APPLICABLE_DATE =  null;
	
	public static String CASHLESS_SETTLEMENT_BILL_PATH = null;
	public static Properties menuProperty = null;
	public static String SEPARATE_ITEMS_PER_PAGE = null;
	public  String GALAXY_DMS_URL = null;
	
	public String PREMIA_SPECIALIST_VIEW_URL = null;
	
	public String PREMIA_DOCTOR_REMARKS_URL = null;
	
	public String GATEWAY_ALERT = null;
	
	public String PREMIA_ANH_PACKAGE_URL = null;
	
	public String PREMIA_PED_DETAILS = null;
	
	public String CASHLESS_DENIAL_REASON = null;
	
	public String PMS_DOCUMENT_URL = null;

	
	public static String IRDA_TAT_DAYS = null;
	
	public static String IRDA_FA_PLUS_DAYS = null;
	
	public  String DMS_REST_API_URL = null;
	
	public static String HOSP_TARIFF_URL = null;
	
	public String CASHLESS_DMS_URL = null;
	
	public static String LOT_BATCH_SIZE = null;
	
	public String JET_PRIVILEGE_FLAG = null;
	public String  IRDA_TAT_FVR_INVS = null;
	public String DMS_FILE_UPLOAD_WEBSERVICE_URL = null;
	public static String RSA_PUBLIC = null;
	
	public static String KEYCLOAK_REALM = null;
	public static String KEYCLOAK_CLIENT_ID = null;
	public static String KEYCLOAK_SERVER_URL = null;
	public static String KEYCLOAK_REDIRECT_URL = null;
	
	public String RS_DOCUMENT_URL = null;
	public static String CUMULATIVE_BONUS_URL = null;
	public static String SALT_KEY = null;
	public static String HP_AUTH_KEY = null;
	public static String FVR_AUTH_KEY = null;
	public static String SRMS_AUTH_KEY = null;

	public  String ALFRESCO_CONN_TIMEOUT = null;
	public static String CALL_RECORDING_WS_URL = null;

	public static String BANCS_WEB_SERVICE_URL = null;
	public static String BANCS_WEB_SERVICE_UW_URL=null;
	public static String BANCS_WEB_SERVICE_PARTY_URL =null;
	public static String BANCS_GENERIC_URL =null;
	
	//Bancs URL
	public static String BANCS_HEALTH_CARD_DOCUMENT_VIEW_URL = null;
	public static String BANCS_CUMULATIVE_BONUS_URL = null;
	public static String BANCS_SPECIALIST_VIEW_URL= null;
	public static String BANCS_PED_DETAILS_VIEW_URL= null;
	public static String BANCS_DOCTOR_REMARKS_URL = null;
	public static String BANCS_CLAIM_VIEW_URL= null;
	public static String BANCS_MER_REPORT_URL = null;
	public static String BANCS_POLICY_DETAIL_VIEW_URL = null;
	public static String BANCS_POLICY_SCHEDULE_VIEW_URL = null;
	public static String BANCS_POLICY_DOCUMENT_URL = null;


	
	//Bancs Flag
	
	public static String BANCS_PROVISON_FLAG = null;
	public static String BANCS_LOCK_FLAG = null;
	public static String BANCS_UNLOCK_FLAG = null;

	public static String BANCS_EXP_WS_AUTH_KEY = null;
	
	public static String CRM_WS_LIST_INTIMATION_KEY = null;
	public static String CRM_WS_ADD_INTIMATION_KEY = null;
	public static String CRM_WS_VIEW_INTIMATION_KEY = null;
	public  String GALAXY_METABASE_URL = null;
	public  String CSH_DASHBOARD_URL = null;
	public  String CSH_DB_URL_ONE = null;
	public  String CSH_DB_URL_TWO = null;
	
	public String PREMIA_MB_PACKAGE_URL = null;
	
	public static String POLICY_SCHEDULE_WITHOUT_RISK = null;
	
	public static String SHOW_CASHLESS_MENU = null;
	
	public static String CASHLESS_URL = null;

	public static String VALIDATE_SERVICE_URL = null;
	
	public static String SHOW_MB_MONITORING_RPT = null;
	
	public static String SHOW_MB_FLR_MONITORING_RPT = null;
	
	public static String EXCUDED_PROVIDER_DATE =  null;
	
	public static String BANCS_BONUS_FLAG = null;
	public String BANCS_RENEWAL_FLAG = null;
	public String BANCS_INSTALMENT_FLAG = null;
	
	public static String SHOW_ACTIONS = null;
	public static String SHOW_REPORTS = null;
	public static String REPORTS_URL = null;
	
	public static String DMS_SECRET_KEY = null;
	
	public static String BANCS_OP_POLICY_PULL_API = null;
	public static String API_URL_AUT_NAME = null;
	public static String API_URL_AUT_PWD = null;
	
	public static String BOT_URL = null;
	public static String BOT_ENABLE = null;
	public static String BOT_KEY_LOCATION = null;
	public static String GALAXY_BOT_URL = null;
	
	public static String DIALER_URL = null;
	public static String DIALER_ACCESS_TOKEN = null;
	public static String DIALER_PASSWORD = null;
	static
	{
		Properties prop = readConnectionPropertyFile();
		
		menuProperty = readUserRoleFile();
		// get the properties value
		if (prop == null)
		{
			throw new IllegalStateException("Error bpm_url  and db_connection propery is missing");
		}
		BPM_URL = prop.getProperty("bpm_url");
		WSO2_URL = prop.getProperty("wso2_url");
		WSO2_SERVER = prop.getProperty("wso2_server");
		CASHLESS_SETTLEMENT_BILL_PATH = dataDir + File.separator + "CASHELESSSETTLEMENTBILL.pdf";
		
		
		//DB_CONNECTION_URL = prop.getProperty("db_connection");
		PREMIA_FLAG = prop.getProperty("premia_flag");
		PREMIA_URL = prop.getProperty("premia_url");
		BANCS_URL = prop.getProperty("bancs_url");
		HEALTH_CARD_NUMBER_URL = prop.getProperty("health_card_document_view");
		DOCS_URL = prop.getProperty(SHAConstants.DOCS_VIEW_URL);
		HOSP_DISCOUNT_URL = prop.getProperty(SHAConstants.HOSP_DISCOUNT_URL);
		DMS_VIEW_URL = prop.getProperty(SHAConstants.DMS_VIEW_URL);
		PORTABLITY_VIEW_URL=prop.getProperty(SHAConstants.PORTABLITY_VIEW_URL);
		PREMIA_CLAIM_VIEW_URL = prop.getProperty(SHAConstants.PREMIA_CLAIM_VIEW_URL);
		CLAIMS_DMS_URL = prop.getProperty(SHAConstants.CLAIMS_DMS_URL);
		ITEMS_PER_PAGE = prop.getProperty("items_per_page");
		BPMN_TASK_USER = prop.getProperty(SHAConstants.BPMN_TASK_USER);
		DB_USER_NAME = prop.getProperty(SHAConstants.DB_USER_NAME);
		//DB_PASSWORD = prop.getProperty(SHAConstants.DB_PASSWORD);
		BPMN_PASSWORD = prop.getProperty(SHAConstants.BPMN_PASSWORD);
		MER_REPORT_URL = prop.getProperty(SHAConstants.MER_REPORT_URL);
		INTIMATED_BY = prop.getProperty(SHAConstants.INTIMATED_BY);
		CLAIM_STATUS_PREMIA = prop.getProperty(SHAConstants.CLAIM_STATUS_PREMIA);
		BEDS_COUNT = prop.getProperty(SHAConstants.BEDS_PREMIA_SERVICE);
		INSERT_NON_NETWORK_HOSP = prop.getProperty(SHAConstants.NON_NETWORK_HOSP_SERVICE);
		BATCH_SIZE = prop.getProperty(SHAConstants.BATCH_SIZE);
		PAGE_SIZE = prop.getProperty(SHAConstants.PAGE_SIZE);
		PAGE_NUMBER = prop.getProperty(SHAConstants.PAGE_NUMBER);
		SEPARATE_ITEMS_PER_PAGE = prop.getProperty(SHAConstants.SEPARATE_ITEMS_PER_PAGE);
		REFER_TO_FLP_APPLICABLE_DATE = prop.getProperty(SHAConstants.REFER_TO_FLP_APPLICABLE_DATE);
		HOSP_TARIFF_URL = prop.getProperty(SHAConstants.HOSP_TARIFF_URL);	
		RSA_PUBLIC = prop.getProperty(SHAConstants.RSA_PUBLIC_KEY);
		//GLX2020132
		EXCUDED_PROVIDER_DATE = prop.getProperty(SHAConstants.EXCUDED_PROVIDER_DATE);
		
		
		KEYCLOAK_REALM = prop.getProperty(SHAConstants.KEYCLOAK_REALM);
		KEYCLOAK_CLIENT_ID = prop.getProperty(SHAConstants.KEYCLOAK_CLIENT_ID);
		KEYCLOAK_SERVER_URL = prop.getProperty(SHAConstants.KEYCLOAK_SERVER_URL);
		KEYCLOAK_REDIRECT_URL = prop.getProperty(SHAConstants.KEYCLOAK_REDIRECT_URL);
	
		CUMULATIVE_BONUS_URL = prop.getProperty(SHAConstants.CUMULATIVE_BONUS_URL);
		
		SALT_KEY = prop.getProperty(SHAConstants.SALT_KEY);
		HP_AUTH_KEY = prop.getProperty(SHAConstants.HP_KEY);
		FVR_AUTH_KEY = prop.getProperty(SHAConstants.FVR_AUTH_KEY);
		SRMS_AUTH_KEY = prop.getProperty(SHAConstants.SRMS_KEY);
		
		CALL_RECORDING_WS_URL = prop.getProperty(SHAConstants.TELE_RECORDING_URL);

		//Bancs 
		BANCS_HEALTH_CARD_DOCUMENT_VIEW_URL = prop.getProperty(SHAConstants.BANCS_HEALTH_CARD_DOCUMENT_VIEW_URL);
		BANCS_CUMULATIVE_BONUS_URL = prop.getProperty(SHAConstants.BANCS_CUMULATIVE_BONUS_URL);
		BANCS_CLAIM_VIEW_URL = prop.getProperty(SHAConstants.BANCS_CLAIM_VIEW_URL);
		BANCS_MER_REPORT_URL = prop.getProperty(SHAConstants.BANCS_MER_REPORT_URL);
		BANCS_POLICY_DOCUMENT_URL = prop.getProperty(SHAConstants.BANCS_POLICY_DOCUMENT_URL);


		BANCS_WEB_SERVICE_URL = prop.getProperty(SHAConstants.BNCS_WS_URL);
		BANCS_WEB_SERVICE_UW_URL= prop.getProperty(SHAConstants.BNCS_WS_UW_URL);
		BANCS_WEB_SERVICE_PARTY_URL= prop.getProperty(SHAConstants.BNCS_WS_PARTY_URL);
		BANCS_EXP_WS_AUTH_KEY = prop.getProperty(SHAConstants.BANCS_WS_AUTH_KEY);		
		BANCS_POLICY_DETAIL_VIEW_URL=prop.getProperty(SHAConstants.BANCS_POLICY_DETAIL_VIEW_URL);
		BANCS_POLICY_SCHEDULE_VIEW_URL=prop.getProperty(SHAConstants.BANCS_POLICY_SCHEDULE_VIEW_URL);
		BANCS_GENERIC_URL= prop.getProperty(SHAConstants.BANCS_WS_GENERIC_URL);
		CRM_WS_LIST_INTIMATION_KEY  = prop.getProperty(SHAConstants.CRM_WS_LIST_INTIMATION_AUTH);
		CRM_WS_ADD_INTIMATION_KEY = prop.getProperty(SHAConstants.CRM_WS_ADD_INTIMATION_AUTH);
		CRM_WS_VIEW_INTIMATION_KEY = prop.getProperty(SHAConstants.CRM_WS_VIEW_INTIMATION_AUTH);
		CASHLESS_URL = prop.getProperty(SHAConstants.CASHLESS_URL);
		REPORTS_URL = prop.getProperty(SHAConstants.REPORTS_URL);
		
		VALIDATE_SERVICE_URL = prop.getProperty(SHAConstants.VALIDATE_SERVICE_URL);
		DMS_SECRET_KEY = prop.getProperty(SHAConstants.DMS_SECRET_KEY);
		BANCS_OP_POLICY_PULL_API = prop.getProperty(SHAConstants.BANCS_OP_POLICY_PULL);
		API_URL_AUT_NAME = prop.getProperty(SHAConstants.CLAIMS_API_AUTH_NAME);
		API_URL_AUT_PWD = prop.getProperty(SHAConstants.CLAIMS_API_AUTH_PWD);
		
		BOT_URL =  prop.getProperty(SHAConstants.BOT_URL);
		BOT_ENABLE =  prop.getProperty(SHAConstants.BOT_ENABLE);
		BOT_KEY_LOCATION =  prop.getProperty(SHAConstants.BOT_KEY_LOCATION);
		GALAXY_BOT_URL =  prop.getProperty(SHAConstants.GALAXY_BOT_URL);
		DIALER_URL = prop.getProperty(SHAConstants.DIALER_URL);
		DIALER_ACCESS_TOKEN = prop.getProperty(SHAConstants.DIALER_ACCESS_TOKEN);
		DIALER_PASSWORD = prop.getProperty(SHAConstants.DIALER_PASSWORD);
		
		//if (DB_CONNECTION_URL == null || WSO2_URL == null || PREMIA_FLAG == null || PREMIA_URL == null)
		if (WSO2_URL == null || PREMIA_FLAG == null || PREMIA_URL == null)
		{
			throw new IllegalStateException("Error bpm_url/wso2_url/wso2_server/Premia URL propery is missing");
		}
	}

	
	public String getDMSRestApiUrl()
	{
		Properties prop = readConnectionPropertyFile();
		DMS_REST_API_URL = prop.getProperty(SHAConstants.DMS_REST_API_URL);
		return DMS_REST_API_URL;
	}
	
	public String getGalaxyDMSUrl()
	{
		Properties prop = readConnectionPropertyFile();
		GALAXY_DMS_URL = prop.getProperty(SHAConstants.GALAXY_DMS_URL);
		return GALAXY_DMS_URL;
	}
	
	public String getSpecialistPremiaUrl()
	{
		Properties prop = readConnectionPropertyFile();
		PREMIA_SPECIALIST_VIEW_URL = prop.getProperty(SHAConstants.PREMIA_SPECIALIST_URL);
		return PREMIA_SPECIALIST_VIEW_URL;
	}
	
	public String getDoctorRemarksPremiaUrl()
	{
		Properties prop = readConnectionPropertyFile();
		PREMIA_DOCTOR_REMARKS_URL = prop.getProperty(SHAConstants.PREMIA_DOCTOR_REMARKS_URL);
		return PREMIA_DOCTOR_REMARKS_URL;
	}
	
	public String getDocumentGateWayUrl()
	{
		Properties prop = readConnectionPropertyFile();
		GATEWAY_ALERT = prop.getProperty(SHAConstants.GATEWAY_ALERT);
		return GATEWAY_ALERT;
	}
	
	public String getPedDetailsForPremiaUrl()
	{
		Properties prop = readConnectionPropertyFile();
		PREMIA_PED_DETAILS = prop.getProperty(SHAConstants.PREMIA_PED_DETAILS_URL);
		return PREMIA_PED_DETAILS;
	}
	
	public String getDenialReasonRemaks()
	{
		Properties prop = readConnectionPropertyFile();
		CASHLESS_DENIAL_REASON = prop.getProperty(SHAConstants.CASHLESS_DENIAL_REASON);
		return CASHLESS_DENIAL_REASON;
	}
	
	public String getDefaultPAProvisionAmt()
	{
		Properties prop = readConnectionPropertyFile();
		PA_ACC_PROV_AMT = prop.getProperty(SHAConstants.PA_ACC_PROV_AMT);
		return PA_ACC_PROV_AMT;
	}
	public String getPmsDocumentURL()
	{
		Properties prop = readConnectionPropertyFile();
		PMS_DOCUMENT_URL = prop.getProperty(SHAConstants.PMS_DOCUMENT_URL);
		return PMS_DOCUMENT_URL;
	}
	
	public String getHospitalPackageDetails()
	{
		Properties prop = readConnectionPropertyFile();
		PREMIA_ANH_PACKAGE_URL = prop.getProperty(SHAConstants.PREMIA_ANH_PACKAGE_URL);
		return PREMIA_ANH_PACKAGE_URL;
	}
	
	public static String getIRDATATDays()
	{
		Properties prop = readConnectionPropertyFile();
		IRDA_TAT_DAYS = prop.getProperty(SHAConstants.IRDA_TAT_DAYS);
		return IRDA_TAT_DAYS;
	}
	
	public static String getFAPlusDays()
	{
		Properties prop = readConnectionPropertyFile();
		IRDA_FA_PLUS_DAYS = prop.getProperty(SHAConstants.IRDA_FA_PLUS_DAYS);
		return IRDA_FA_PLUS_DAYS;
	}
	
	/*public static Connection getConnectionFromURL() throws SQLException {
		Connection connection = null;
		DriverManager.registerDriver(myDriver);
		if (true)
		{
			connection = DriverManager
					.getConnection("jdbc:oracle:thin:IMS_GALTXN/star123@192.168.1.176:1522:GALDEV");
		}
		else
		//{
			connection = DriverManager.getConnection(DB_CONNECTION_URL, DB_USER_NAME, DB_PASSWORD);
		//}
		
		return connection;
	}*/
	
	
	public static Connection getConnection() {
	    Connection connection = null;
	    try {
	      InitialContext context = new InitialContext();
	      DataSource dataSource = (DataSource) context.lookup(SHAConstants.CONNECTION_STRING);
	      connection = dataSource.getConnection();
	    } catch (Exception e) {
	    	/*try {
	    		return getConnectionFromURL();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
	    	e.printStackTrace();
	    } 
	     
	    return connection;
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
	
	public static Properties readUserRoleFile()
	{
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			
			input = new FileInputStream(dataDir + "/" + "UserRole.properties");
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

//	public static Context getInitialContext(String username, String password) {
//		Context instance = null;
//		try {
//			Environment env = new Environment();
//			env.setInitialContextFactory("weblogic.jndi.WLInitialContextFactory");
//			
//			/**
//			 * The below code was commented, since for UAT
//			 * training BPMN user credential were modified.
//			 * Hence commented the below one and added UAT setup
//			 * credentials.
//			 * */
//			env.setSecurityPrincipal(BPMClientContext.BPMN_TASK_USER);
//			env.setSecurityCredentials(BPMN_PASSWORD);
//			/**
//			 * Credentials for UAT setup.
//			 * */
//			/*env.setSecurityPrincipal("weblogic");
//			env.setSecurityCredentials("weblogic123");*/
//
//			// env.setProviderURL("t3://192.168.2.165:7101");
//
////			 env.setProviderURL("t3://192.168.1.154:7101"); // this is for QA server
//
////			 env.setProviderURL("t3://192.168.2.171:7101"); // this is for Temporary Testing server
//			env.setProviderURL(BPM_URL); // this is for development server
//
//			instance = env.getInitialContext();
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//		return instance;
//	}
	
	 /*public static void callSystemActionsHumanTaskServiceBPM(String username,  int humanTaskId)
   	      throws NamingException
   	    {
   	      Context context = getInitialContext(username, "Star@123");
   	      SystemActionsHumanTaskService task = null;
   	        task = (SystemActionsHumanTaskService) context.lookup("SystemActionsHumanTaskService#com.shaic.ims.bpm.claim.servicev2.SystemActionsHumanTaskService");
//			task.execute(username, 200565, "SYS_ACQUIRE");
   	        task.execute(username, humanTaskId, "SYS_ACQUIRE");
   	   }*/

	/*public IntMsg getTaskList(String username, String password) {
		System.out.println("BPM Init lookup called");
		Context context = getInitialContext(username, password);
		IntMsg tasks = null;
		if (!TEST_MODE) {
			try {
				tasks = (IntMsg) context
						.lookup("IntMsg#com.shaic.ims.bpm.claim.servicev2.intimation.IntMsg");
				System.out
						.println("Lookup called and executed at the server side ");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		} else {
			// tasks = new GetTasksImpl();
		}
		return tasks;
	}*/

	/**
	 * Method to invoke BPM service for Ped Request Process (Process) Search
	 * menu
	 * 
	 * */
/*
	public static com.shaic.ims.bpm.claim.servicev2.ped.search.ProcessorPedReqTask getProcessPedTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.ped.search.ProcessorPedReqTask tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.ped.search.ProcessorPedReqTask) context
					.lookup("ProcessorPedReqTask#com.shaic.ims.bpm.claim.servicev2.ped.search.ProcessorPedReqTask");
			System.out
					.println("ProcessorPedReqTask Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
*/
	/**
	 * Method to invoke BPM service for Ped Request Process (Approve) Search
	 * menu
	 * 
	 * */
	/*public static com.shaic.ims.bpm.claim.servicev2.ped.search.ApproverPedReqTask getApproverPedTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.ped.search.ApproverPedReqTask tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.ped.search.ApproverPedReqTask ) context
					.lookup("ApproverPedReqTask#com.shaic.ims.bpm.claim.servicev2.ped.search.ApproverPedReqTask");
			System.out
					.println("ApproverPedReqTask Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitApproverPedReqTask getSubmitPedTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		SubmitApproverPedReqTask tasks = null;
		try {
			tasks = (SubmitApproverPedReqTask) context
					.lookup("SubmitApproverPedReqTask#com.shaic.ims.bpm.claim.servicev2.ped.SubmitApproverPedReqTask");
			System.out
					.println("SubmitApproverPedReqTask Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	
	public static SubmitProcessPedQueryTask getSubmitPedQueryTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		SubmitProcessPedQueryTask tasks = null;
		try {
			tasks = (SubmitProcessPedQueryTask) context
					.lookup("SubmitProcessPedQueryTask#com.shaic.ims.bpm.claim.servicev2.ped.SubmitProcessPedQueryTask");
			System.out
					.println("SubmitProcessPedQueryTask Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitDownsizeSpecialistAdviseTask getSubmitSpecialistDownsizeTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		SubmitDownsizeSpecialistAdviseTask tasks = null;
		try {
			tasks = (SubmitDownsizeSpecialistAdviseTask) context
					.lookup("SubmitDownsizeSpecialistAdviseTask#com.shaic.ims.bpm.claim.servicev2.preauth.SubmitDownsizeSpecialistAdviseTask");
			System.out
					.println("SubmitProcessPedQueryTask Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitAdviseOnPedTask getSubmitPedAdviseTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		SubmitAdviseOnPedTask tasks = null;
		try {
			tasks = (SubmitAdviseOnPedTask) context
					.lookup("SubmitAdviseOnPedTask#com.shaic.ims.bpm.claim.servicev2.ped.SubmitAdviseOnPedTask");
			System.out
					.println("SubmitAdviseOnPedTask Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	public static SubmitProcessorPedReqTask getSubmitPedProcessorTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		SubmitProcessorPedReqTask tasks = null;
		try {
			tasks = (SubmitProcessorPedReqTask) context
					.lookup("SubmitProcessorPedReqTask#com.shaic.ims.bpm.claim.servicev2.ped.SubmitProcessorPedReqTask");
			System.out
					.println("SubmitProcessorPedReqTask Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	@SuppressWarnings("static-access")
	public static void execute(String username, String password, PayloadBOType payloadBo) {

		BPMClientContext instance = new BPMClientContext();
		System.out.println("BPM Init lookup called");
		Context context = instance.getInitialContext(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		PedProcess pedProcess = null;

		try {
			pedProcess = (PedProcess) context
					.lookup("PedProcess#com.shaic.ims.bpm.claim.servicev2.ped.PedProcess");

			try{
			pedProcess.initiate(BPMClientContext.BPMN_TASK_USER, payloadBo);
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("Look up called at server side");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("static-access")
	public static void intiateInvestigationProcess(String userName, String password, PayloadBOType cashlessPayload){
		BPMClientContext instance = new BPMClientContext();
		System.out.println("BPM Init lookup called");
		Context context = instance.getInitialContext(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		
		InvestigationProcess investigationProcess = null;
		try{
			
			investigationProcess = (InvestigationProcess) context.lookup("InvestigationProcess#com.shaic.ims.bpm.claim.servicev2.investigation.InvestigationProcess");
			
			try{
			investigationProcess.initiate(BPMClientContext.BPMN_TASK_USER, cashlessPayload);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		} catch(NamingException e){
			e.printStackTrace();
		}
	}
	
	
//	@SuppressWarnings("static-access")
//	public static void executeforWithdraw(IntimationMessage intMsg, PreAuthReq preAuthReq,String outCome) {
//		
//		
//		BPMClientContext instance=new BPMClientContext();
//		System.out.println("BPM Init LookUp called");
//		Context context=instance.getInitialContext("weblogic", "Star@123");
//		WithdrawPreAuth withdrawPreauth=null;
//		try{
//			withdrawPreauth=(WithdrawPreAuth)context.lookup("WithdrawPreAuth#com.shaic.ims.bpm.claim.service.preauth.WithdrawPreAuth");
//			
//			withdrawPreauth.initiate("weblogic","Star@123", intMsg, preAuthReq, outCome);
//		
//			System.out.println("Look up called at server side");
//		}catch(NamingException e){
//			e.printStackTrace();
//		}
//	}
	
	@SuppressWarnings("static-access")
	public static void submitWithdrawTask(PayloadBOType payloadBOType,String outCome) {
		
		
		BPMClientContext instance=new BPMClientContext();
		System.out.println("BPM Init LookUp called");
		Context context=instance.getInitialContext(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		WithdrawPreAuth withdrawPreauth=null;
		try {
			withdrawPreauth = (WithdrawPreAuth)context.lookup("WithdrawPreAuth#com.shaic.ims.bpm.claim.servicev2.preauth.WithdrawPreAuth");
			
			try{
			withdrawPreauth.initiate(BPMClientContext.BPMN_TASK_USER, payloadBOType );
			}catch(Exception e){
				e.printStackTrace();
			}
		
		} catch(NamingException e) {
			e.printStackTrace();
		}
	}*/
	
	
	
//	@SuppressWarnings("static-access")
//	public static void executeforDownSize(IntimationMessage intMsg, PreAuthReq preAuthReq,String outCome) {
//		
//		
//		BPMClientContext instance=new BPMClientContext();
//		System.out.println("BPM Init LookUp called");
//		Context context=instance.getInitialContext("weblogic", "Star@123");
//		DownsizePreAuth downsizePreauth=null;
//		try{
//			downsizePreauth=(DownsizePreAuth)context.lookup("DownsizePreAuth#com.shaic.ims.bpm.claim.service.preauth.DownsizePreAuth");
//			
//			downsizePreauth.initiate("weblogic","Star@123", intMsg, preAuthReq, outCome);
//		
//			System.out.println("Look up called at server side");
//		}catch(NamingException e){
//			e.printStackTrace();
//		}
//	}
	
	/*@SuppressWarnings("static-access")
	public static void executeforDownSize(PayloadBOType payloadBO, String outCome,String userName) {
		
		
		BPMClientContext instance=new BPMClientContext();
		System.out.println("BPM Init LookUp called");
		Context context=instance.getInitialContext(BPMClientContext.BPMN_TASK_USER, "Star@123");
		DownsizePreAuth downsizePreauth=null;
		try{
			downsizePreauth=(DownsizePreAuth)context.lookup("DownsizePreAuth#com.shaic.ims.bpm.claim.servicev2.preauth.DownsizePreAuth");
			
			try{
			downsizePreauth.initiate(BPMClientContext.BPMN_TASK_USER, payloadBO);
			}catch(Exception e){
				e.printStackTrace();
			}
		
			System.out.println("Look up called at server side");
		}catch(NamingException e){
			e.printStackTrace();
		}
	}
	

	*//**
	 * Method to invoke BPM service for Advise on PED Task Process Search menu
	 * *//*

	public static com.shaic.ims.bpm.claim.servicev2.ped.search.AdviseOnPedTask getAdviseOnPedTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.ped.search.AdviseOnPedTask tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.ped.search.AdviseOnPedTask) context
					.lookup("AdviseOnPedTask#com.shaic.ims.bpm.claim.servicev2.ped.search.AdviseOnPedTask");
			System.out
					.println("AdviseOnPedTask Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke BPM service for Process PED Query Search menu
	 * 
	 * *//*

	public static com.shaic.ims.bpm.claim.servicev2.ped.search.ProcessPedQueryTask getProcessPedQueryTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.ped.search.ProcessPedQueryTask  tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.ped.search.ProcessPedQueryTask ) context
					.lookup("ProcessPedQueryTask#com.shaic.ims.bpm.claim.servicev2.ped.search.ProcessPedQueryTask");
			System.out
					.println(" Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke BPM service for Process Rejection Service Search menu
	 * 
	 * *//*
	public static com.shaic.ims.bpm.claim.servicev2.registration.search.ProcessRejectionTask getProcessRejectionTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.registration.search.ProcessRejectionTask tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.registration.search.ProcessRejectionTask) context
					.lookup("ProcessRejectionTask#com.shaic.ims.bpm.claim.servicev2.registration.search.ProcessRejectionTask");
			System.out
					.println("ProcessRejectionTask Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static ProcessRejectionNonMedicalTask getProcessRejectionNonMedicalTask(String userName, String passWord){
		
		Context context = getInitialContext(userName, passWord);
		
		ProcessRejectionNonMedicalTask tasks = null;
		
		try{
			
			tasks = (ProcessRejectionNonMedicalTask) context.lookup("ProcessRejectionNonMedicalTask#com.shaic.ims.bpm.claim.servicev2.registration.search.ProcessRejectionNonMedicalTask");
			System.out
			.println("ProcessRejectionNonMedicalTask Lookup called and executed at the server side ");
			
		}catch(NamingException e){
			e.printStackTrace();
		}
		
		return tasks;
	}

	*//**
	 * Method to invoke BPM service for Process Rejection Service Search menu
	 * 
	 * *//*
	public static com.shaic.ims.bpm.claim.servicev2.conversion.search.ClaimConvTask getConvertClaimSearchTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.conversion.search.ClaimConvTask tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.conversion.search.ClaimConvTask) context
					.lookup("ClaimConvTask#com.shaic.ims.bpm.claim.servicev2.conversion.search.ClaimConvTask");
			System.out
					.println("ClaimConvTask Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke BPM service for Process Pre Auth Service Search menu
	 * 
	 * *//*
	public static com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthTask getProcessPreAuthTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthTask tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthTask) context
					.lookup("PreAuthTask#com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthTask");
			System.out
					.println("PreAuthTask Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke BPM service for Process Enhancement Service Search menu
	 * 
	 * *//*
	public static com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthEnhTask getProcessEnhancementTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthEnhTask tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthEnhTask) context
					.lookup("PreAuthEnhTask#com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthEnhTask");
			System.out
					.println("PreAuthTask Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke BPM service for Process Enhancement Service Search menu
	 * 
	 * *//*
	public static PreAuthEnhTask getProcessPreAuthEnhTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		PreAuthEnhTask tasks = null;
		try {
			tasks = (PreAuthEnhTask) context
					.lookup("PreAuthEnhTask#com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthEnhTask");
			System.out
					.println("PreAuthEnhTask Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	
	public static SubmitDownsizePreAuthTask getDownsizePreauthTask(String username,
			String password) {
		Context context = getInitialContext(username, password);
		SubmitDownsizePreAuthTask tasks = null;
		try {
			tasks = (SubmitDownsizePreAuthTask) context
					.lookup("SubmitDownsizePreAuthTask#com.shaic.ims.bpm.claim.servicev2.preauth.SubmitDownsizePreAuthTask");
			System.out
					.println("SubmitDownsizePreAuthTask Lookup called and executed at the server side ");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke BPM service for Downsize Pre Auth Service Search menu
	 * 
	 * *//*
	public static com.shaic.ims.bpm.claim.servicev2.preauth.search.DownsizePreAuthTask getProcessDownSizePreAuthTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.preauth.search.DownsizePreAuthTask tasks = null;

		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.preauth.search.DownsizePreAuthTask) context
					.lookup("DownsizePreAuthTask#com.shaic.ims.bpm.claim.servicev2.preauth.search.DownsizePreAuthTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static CloseClaimTask getCloseClaimInProcessTask(String userName, String password){
		
		Context context = getInitialContext(userName, password);
		
		CloseClaimTask tasks = null;
		
		try {
			tasks = (CloseClaimTask) context
					.lookup("CloseClaimTask#com.shaic.ims.bpm.claim.servicev2.common.search.CloseClaimTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
		
	}
	
	
	public static CloseAllClaimTask getCloseClaimTask(String userName, String password){
		
		Context context = getInitialContext(userName, password);
		
		CloseAllClaimTask tasks = null;
		
		try {
			tasks = (CloseAllClaimTask) context
					.lookup("CloseAllClaimTask#com.shaic.ims.bpm.claim.servicev2.common.search.CloseAllClaimTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static ReopenAllClaimTask getReOpAllClaimTask(String userName, String passWord){
		
		Context context = getInitialContext(userName, passWord);
		
		ReopenAllClaimTask tasks = null;
		
		try {
			tasks = (ReopenAllClaimTask) context
					.lookup("ReopenAllClaimTask#com.shaic.ims.bpm.claim.servicev2.common.search.ReopenAllClaimTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke BPM service for Upload Translated Document Service
	 * Search menu
	 * 
	 * *//*
	public static com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthTranslDocTask getProcessUploadDocumentService(
			String username, String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthTranslDocTask  tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthTranslDocTask) context
					.lookup("PreAuthTranslDocTask#com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthTranslDocTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke BPM service for Process Pre Medical Document Service
	 * Search menu
	 * 
	 * *//*
	public static com.shaic.ims.bpm.claim.servicev2.preauth.search.PreMedicalPreAuthTask getProcessPreMedicalTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.preauth.search.PreMedicalPreAuthTask tasks = null;
		try {
			tasks = (PreMedicalPreAuthTask) context
					.lookup("PreMedicalPreAuthTask#com.shaic.ims.bpm.claim.service.preauth.search.PreMedicalPreAuthTask");
			tasks = (com.shaic.ims.bpm.claim.servicev2.preauth.search.PreMedicalPreAuthTask)context.lookup("PreMedicalPreAuthTask#com.shaic.ims.bpm.claim.servicev2.preauth.search.PreMedicalPreAuthTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke BPM service for Process Pre Medical Enhancement Service
	 * Search menu
	 * 
	 * *//*
	public static com.shaic.ims.bpm.claim.servicev2.preauth.search.PreMedicalPreAuthEnhTask getProcessPreMedicalEnhancementTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.preauth.search.PreMedicalPreAuthEnhTask tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.preauth.search.PreMedicalPreAuthEnhTask) context
					.lookup("PreMedicalPreAuthEnhTask#com.shaic.ims.bpm.claim.servicev2.preauth.search.PreMedicalPreAuthEnhTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke BPM service for Submit Specialist Enhancement Service
	 * Search menu
	 * 
	 * *//*
//	public static DownsizeSpecialistAdviseTask getSubmitSpecialistTaks(
//			String username, String password) {
//		Context context = getInitialContext(username, password);
//		DownsizeSpecialistAdviseTask tasks = null;
//		try {
//			tasks = (DownsizeSpecialistAdviseTask) context
//					.lookup("DownsizeSpecialistAdviseTask#com.shaic.ims.bpm.claim.service.preauth.search.DownsizeSpecialistAdviseTask");
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//		return tasks;
//	}
	
	public static com.shaic.ims.bpm.claim.servicev2.preauth.search.SubmitSpecialistAdviseTask getSubmitSpecialistAdvise(
			String username, String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.preauth.search.SubmitSpecialistAdviseTask tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.preauth.search.SubmitSpecialistAdviseTask) context
					.lookup("SubmitSpecialistAdviseTask#com.shaic.ims.bpm.claim.servicev2.preauth.search.SubmitSpecialistAdviseTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke BPM service for Edit Hospital Info Task.
	 * *//*
	public static com.shaic.ims.bpm.claim.servicev2.hms.search.EditHospitalDetailsTask getEditHospitalDetailsTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.hms.search.EditHospitalDetailsTask tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.hms.search.EditHospitalDetailsTask) context
					.lookup("EditHospitalDetailsTask#com.shaic.ims.bpm.claim.servicev2.hms.search.EditHospitalDetailsTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	
	*//**
	 * Method to invoke BPM service for Submit Edit Hospital Info Task.
	 * *//*
	public static SubmitEditHospitalDetailsTask getSubmitEditHospitalDetailsTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		SubmitEditHospitalDetailsTask tasks = null;
		try {
			tasks = (SubmitEditHospitalDetailsTask) context
					.lookup("SubmitEditHospitalDetailsTask#com.shaic.ims.bpm.claim.servicev2.hms.SubmitEditHospitalDetailsTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * Method to invoke BPM service for AcknowledgeHospitalCommunication Task.
	 * *//*
	
	
	public static com.shaic.ims.bpm.claim.servicev2.hms.search.AckHospitalCommunicationTask  getAckHospitalCommunicationTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.hms.search.AckHospitalCommunicationTask  tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.hms.search.AckHospitalCommunicationTask) context
					.lookup("AckHospitalCommunicationTask#com.shaic.ims.bpm.claim.servicev2.hms.search.AckHospitalCommunicationTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
    public static AckHospitalCommunicationCLTask getAckHositpitalCommunicationForPreauthQuery(String userName, String passWord){
    	
    	Context context = getInitialContext(userName, passWord);
		AckHospitalCommunicationCLTask  tasks = null;
		try {
			tasks = (AckHospitalCommunicationCLTask) context
					.lookup("AckHospitalCommunicationCLTask#com.shaic.ims.bpm.claim.servicev2.hms.search.AckHospitalCommunicationCLTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
    }
	
	public static StartAckHospitalCommunication getInitiateAckHospitalCommunication(String userName, String passWord){
		
		Context context = getInitialContext(userName, passWord);
		StartAckHospitalCommunication  tasks = null;
		try {
			tasks = (StartAckHospitalCommunication) context
					.lookup("StartAckHospitalCommunication#com.shaic.ims.bpm.claim.servicev2.hms.StartAckHospitalCommunication");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke Star Fax simulator BPM service Task.
	 * *//*

	public static com.shaic.ims.bpm.claim.servicev2.dms.SimulateStarfax getSimulateStarfax(String username,
			String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.dms.SimulateStarfax tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.dms.SimulateStarfax) context
					.lookup("SimulateStarfax#com.shaic.ims.bpm.claim.servicev2.dms.SimulateStarfax");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke Assign Feild Visit Representative Search Screen.
	 * *//*

	public static com.shaic.ims.bpm.claim.servicev2.preauth.search.AssignFieldVisitTask getAssignFeildVistRepTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		com.shaic.ims.bpm.claim.servicev2.preauth.search.AssignFieldVisitTask tasks = null;
		try {
			tasks = (com.shaic.ims.bpm.claim.servicev2.preauth.search.AssignFieldVisitTask) context
					.lookup("AssignFieldVisitTask#com.shaic.ims.bpm.claim.servicev2.preauth.search.AssignFieldVisitTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * Method to invoke acknowledgement doc receipt service.
	 * *//*
	
	public static Acknowledgement getAcknowledgementTask(String struserName , String password)
	{
		Context context = getInitialContext(struserName, password);
		ProcessInstanceService tasks = null;
		try
		{
			tasks = (ProcessInstanceService) context
					.lookup("ProcessInstanceServiceV2#com.shaic.ims.bpm.claim.servicev2.ProcessInstanceService");
		}
		 catch (NamingException e) {
				e.printStackTrace();
			}
		
		Acknowledgement tasks = null;
		try {
			tasks = (Acknowledgement) context
					.lookup("Acknowledgement#com.shaic.ims.bpm.claim.servicev2.ack.Acknowledgement");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static RegisterClaim getRegisterClaimTask(String struserName , String password)
	{
		Context context = getInitialContext(struserName, password);
		RegisterClaim tasks = null;
		try {
			tasks = (RegisterClaim) context
					.lookup("RegisterClaim#com.shaic.ims.bpm.claim.servicev2.ophealth.RegisterClaim");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	public static ClaimRegTask getManualRegisterClaimTask(String userName , String password)
	{
		Context context = getInitialContext(userName, password);
		ClaimRegTask tasks = null;
		try {
			tasks = (ClaimRegTask) context
					.lookup("ClaimRegTask#com.shaic.ims.bpm.claim.servicev2.registration.search.ClaimRegTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static ClaimRegROTask getManualRegisterClaimTaskForRO(String userName, String password){
		
		Context context = getInitialContext(userName, password);
		ClaimRegROTask tasks = null;
		try {
			tasks = (ClaimRegROTask) context
					.lookup("ClaimRegROTask#com.shaic.ims.bpm.claim.servicev2.registration.search.ClaimRegROTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
		
	}*/
	
	public static void printPayloadElement(Element element)
			throws TransformerConfigurationException, TransformerException {
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		StringWriter buffer = new StringWriter();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.transform(new DOMSource(element), new StreamResult(buffer));
		String str = buffer.toString();
		System.out.println("Payload=> " + str);
	}

	public static Long getKeyFromPayload1(XMLElement xmlPayload_, String tagName) {
		return Long.valueOf(getMapFromPayload(xmlPayload_, tagName).get("key"));
	}

	public static Map<String, String> getMapFromPayload(XMLElement xmlPayload_,
			String tagName) {
		Map<String, String> payloadItems = new HashMap<String, String>();

		NodeList payloadNodeList_ = xmlPayload_.getElementsByTagName(tagName)
				.item(0).getChildNodes();
		;

		if (payloadNodeList_ != null && payloadNodeList_.getLength() > 0) {
			for (int i = 0; i < payloadNodeList_.getLength(); i++) {
				if (payloadNodeList_.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element element_ = (Element) payloadNodeList_.item(i);
					String nodeName = element_.getNodeName();
					/**
					 * The below if block is introduced, since in BPM payload,
					 * at times we would get xml data as follows. <tns:key> . We
					 * would require only "key" string and based on that further
					 * processing is done. Hence we split with :.
					 * 
					 * But in some cases we would also get , <key> alone. In
					 * those scenarios , the below if block is skipped and else
					 * block is executed.
					 * */
					if (nodeName.contains(":")) {
						String nodeNameArr[] = nodeName.split(":");
						if (null != nodeNameArr && 0 != nodeNameArr.length) {
							{
								payloadItems.put(nodeNameArr[1],
										element_.getTextContent());
							}
						} else {
							payloadItems.put(nodeName,
									element_.getTextContent());
						}
					} else {
						payloadItems.put(nodeName, element_.getTextContent());

					}
				}
			}
		}
		return payloadItems;
	}

	public static XMLElement setNodeValue(XMLElement xmlPayload_,
			String parent, String tagName, String value)
			throws TransformerConfigurationException, TransformerException {
		NodeList payloadNodeList_ = xmlPayload_.getElementsByTagName(parent)
				.item(0).getChildNodes();
		;

		if (payloadNodeList_ != null && payloadNodeList_.getLength() > 0) {
			for (int i = 0; i < payloadNodeList_.getLength(); i++) {
				if (payloadNodeList_.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element element_ = (Element) payloadNodeList_.item(i);
					String nodeName = element_.getNodeName();
					System.out.println("nodeName := " + nodeName);
					if (nodeName.equals(tagName)) {
						element_.setTextContent(value);
					}
				}
			}
		}
		return xmlPayload_;
	}

	public static XMLElement setNodeValue(XMLElement xmlPayload_,
			String parent, Map<String, String> valueMap)
			throws TransformerConfigurationException, TransformerException {
		Set<String> keySet = valueMap.keySet();
		NodeList payloadNodeList_ = xmlPayload_.getElementsByTagName(parent)
				.item(0).getChildNodes();
		;

		if (payloadNodeList_ != null && payloadNodeList_.getLength() > 0) {
			for (int i = 0; i < payloadNodeList_.getLength(); i++) {
				if (payloadNodeList_.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element element_ = (Element) payloadNodeList_.item(i);
					String nodeName = element_.getNodeName();
					System.out.println("nodeName := " + nodeName);
					if (keySet.contains(nodeName)) {
						element_.setTextContent(valueMap.get(nodeName));
					}
				}
			}
		}
		return xmlPayload_;
	}
	
	//----------------------------------------------------------------------------R3-------search-------------------------------------------------------------------
	
	/**
	 * Method to invoke BPM service for CreateROD Service
	 * Search menu
	 * 
	 * */
	/*public static CreateRODTask getCreateRODTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		CreateRODTask tasks = null;
		try {
			tasks = (CreateRODTask) context
					.lookup("CreateRODTask#com.shaic.ims.bpm.claim.servicev2.ack.search.CreateRODTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	public static SubmitRODTask getCreateRODSubmitService(
			String username, String password) {
		Context context = getInitialContext(username, password);
		SubmitRODTask tasks = null;
		try {
			tasks = (SubmitRODTask) context
					.lookup("SubmitRODTask#com.shaic.ims.bpm.claim.servicev2.ack.SubmitRODTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitProcessClaimRequestZMRTask getClaimRequestZonalReview(String userName, String password){
		Context context = getInitialContext(userName, password);
		SubmitProcessClaimRequestZMRTask tasks = null;
		try{
			tasks = (SubmitProcessClaimRequestZMRTask) context.lookup("SubmitProcessClaimRequestZMRTask#com.shaic.ims.bpm.claim.servicev2.medical.SubmitProcessClaimRequestZMRTask");                               //need to implements
			
		} catch(NamingException e){
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitProcessInvestigationTask getInvestigationTask(String userName,String password){
		Context context = getInitialContext(userName, password);
		
		SubmitProcessInvestigationTask tasks =null;
		
		try{
			tasks = (SubmitProcessInvestigationTask) context.lookup("SubmitProcessInvestigationTask#com.shaic.ims.bpm.claim.servicev2.investigation.SubmitProcessInvestigationTask");                               //need to implements
			
		} catch(NamingException e){
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitAssignInvestigationTask getSubmitAssignInvestigationTask(String userName,String passWord){
		Context context = getInitialContext(userName, passWord);
		
		SubmitAssignInvestigationTask tasks = null;
		
		try{
			tasks = (SubmitAssignInvestigationTask) context.lookup("SubmitAssignInvestigationTask#com.shaic.ims.bpm.claim.servicev2.investigation.SubmitAssignInvestigationTask");
			}
		catch(NamingException e){
			e.printStackTrace();
		}
		
		return tasks;
	} 
	
	public static SubmitAssignInvestigationCLTask getSubmitAssignInvestigationCashlessTask(String userName, String passWord){
		
		Context context = getInitialContext(userName, passWord);
		
		SubmitAssignInvestigationCLTask tasks = null;
		
		try{
			tasks = (SubmitAssignInvestigationCLTask) context.lookup("SubmitAssignInvestigationCLTask#com.shaic.ims.bpm.claim.servicev2.investigation.SubmitAssignInvestigationCLTask");
			
		} catch(NamingException e){
			e.printStackTrace();
		}
		
		return tasks;
	}
	
	public static SubmitCoordinatorReplyTask getSubmitCoordinatorReplyTask(String userName,String passWord){
		Context context = getInitialContext(userName, passWord);
		
		SubmitCoordinatorReplyTask tasks = null;
		
		try{
			tasks = (SubmitCoordinatorReplyTask) context.lookup("SubmitCoordinatorReplyTask#com.shaic.ims.bpm.claim.servicev2.medical.SubmitCoordinatorReplyTask");
			}
		catch(NamingException e){
			e.printStackTrace();
		}
		
		return tasks;
	}
	
	public static SystemActionsHumanTaskService getActiveAndDeactiveHumanTask(String userName,String passWord){
		Context context = getInitialContext(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		
		SystemActionsHumanTaskService tasks = null;
		
		try{
			tasks = (SystemActionsHumanTaskService) context.lookup("SystemActionsHumanTaskService#com.shaic.ims.bpm.claim.servicev2.SystemActionsHumanTaskService");
			}
		catch(NamingException e){
			e.printStackTrace();
		}
		
		return tasks;
	}
	
	public static void setActiveOrDeactive(SystemActionsHumanTaskService humanTask, String userName,int taskNumber, String outCome){
		
		humanTask.execute(userName, taskNumber,outCome);
		
	}
	
	public static void setActiveOrDeactiveClaim(String userName,String password, int taskNumber, String outCome){
		
			SystemActionsHumanTaskService task = getActiveAndDeactiveHumanTask(userName, password);
			task.execute(userName, taskNumber, outCome);
		
		
	}
	
	public static HTAcquireByUser getAcquireHumanTask(String userName, String password){
		
		Context context = getInitialContext(userName, password);
		
		HTAcquireByUser tasks = null;
		
		try{
			tasks = (HTAcquireByUser) context.lookup("HTAcquireByUser#com.shaic.ims.bpm.claim.servicev2.HTAcquireByUser");
			}
		catch(NamingException e){
			e.printStackTrace();
		}
		
		return tasks;
	}
	
	
	

	public static SubmitAckInvestigationTask getSubmitAckInvestigationTask(String userName,String passWord){
		
		
       Context context = getInitialContext(userName, passWord);
		
       SubmitAckInvestigationTask tasks = null;
		
		try{
			tasks = (SubmitAckInvestigationTask) context.lookup("SubmitAckInvestigationTask#com.shaic.ims.bpm.claim.servicev2.investigation.SubmitAckInvestigationTask");
			}
		catch(NamingException e){
			e.printStackTrace();
		}
		
		return tasks;
	}
	
	public static SubmitUploadInvestigationTask getSubmitUploadInvestigationTask(String userName, String passWord){
		
       Context context = getInitialContext(userName, passWord);
		
		SubmitUploadInvestigationTask tasks = null;
		
		try{
			tasks = (SubmitUploadInvestigationTask) context.lookup("SubmitUploadInvestigationTask#com.shaic.ims.bpm.claim.servicev2.investigation.SubmitUploadInvestigationTask");
			}
		catch(NamingException e){
			e.printStackTrace();
		}
		
		return tasks;
	}
	
	public static SubmitProcessClaimRequestTask getClaimRequest(String userName, String passWord){
		Context context = getInitialContext(userName, passWord);
		SubmitProcessClaimRequestTask tasks = null;
		
		try{
			tasks = (SubmitProcessClaimRequestTask) context.lookup("SubmitProcessClaimRequestTask#com.shaic.ims.bpm.claim.servicev2.medical.SubmitProcessClaimRequestTask");
		} catch(NamingException e){
			e.printStackTrace();
		}
		return tasks;
	}
	

	
	public static SubmitPreMedicalPreAuthTask getPremedicalPreauth(String userName, String passWord){
		Context context = getInitialContext(userName, passWord);
		SubmitPreMedicalPreAuthTask tasks = null;
		
		try{
			tasks = (SubmitPreMedicalPreAuthTask) context.lookup("SubmitPreMedicalPreAuthTask#com.shaic.ims.bpm.claim.servicev2.preauth.SubmitPreMedicalPreAuthTask");
		} catch(NamingException e){
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitPreAuthTask getPreauthTask(String userName, String passWord){
		Context context = getInitialContext(userName, passWord);
		SubmitPreAuthTask tasks = null;
		
		try{
			tasks = (SubmitPreAuthTask) context.lookup("SubmitPreAuthTask#com.shaic.ims.bpm.claim.servicev2.preauth.SubmitPreAuthTask");
		} catch(NamingException e){
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitPreAuthEnhTask getPreauthEnhancementTask(String userName, String passWord){
		Context context = getInitialContext(userName, passWord);
		SubmitPreAuthEnhTask tasks = null;
		
		try{
			tasks = (SubmitPreAuthEnhTask) context.lookup("SubmitPreAuthEnhTask#com.shaic.ims.bpm.claim.servicev2.preauth.SubmitPreAuthEnhTask");
		} catch(NamingException e){
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitPreMedicalPreAuthEnhTask getPremedicalEnhancementTask(String userName, String passWord){
		Context context = getInitialContext(userName, passWord);
		SubmitPreMedicalPreAuthEnhTask tasks = null;
		
		try{
			tasks = (SubmitPreMedicalPreAuthEnhTask) context.lookup("SubmitPreMedicalPreAuthEnhTask#com.shaic.ims.bpm.claim.servicev2.preauth.SubmitPreMedicalPreAuthEnhTask");
		} catch(NamingException e){
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	
	*//**
	 * Method to invoke BPM service for BillEntry Service
	 * Search menu
	 * 
	 * *//*
	public static BillEntryTask getBillEntryTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		BillEntryTask tasks = null;
		try {
			tasks = (BillEntryTask) context
					.lookup("BillEntryTask#com.shaic.ims.bpm.claim.servicev2.ack.search.BillEntryTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	
	*//**
	 * Method to invoke BPM service for uploadinvestigation Service
	 * Search menu
	 * 
	 * *//*
	public static UploadInvestigationTask getUploadInvestigationTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		UploadInvestigationTask tasks = null;
		try {
			tasks = (UploadInvestigationTask) context
					.lookup("UploadInvestigationTask#com.shaic.ims.bpm.claim.servicev2.investigation.search.UploadInvestigationTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	*//**
	 * Method to invoke BPM service for bill entry submit Service.
	 * *//*
	
	public static SubmitBillEntryTask getSubmitBillEntryTask (String username, String password) 
	{
		Context context = getInitialContext(username, password);
		SubmitBillEntryTask tasks = null;
		try {
			tasks = (SubmitBillEntryTask) context
					.lookup("SubmitBillEntryTask#com.shaic.ims.bpm.claim.servicev2.ack.SubmitBillEntryTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	public static InvokeHumanTaskServiceV2 getCreateRODSubmitService(
			String username, String password) {
		Context context = getInitialContext(username, password);
		InvokeHumanTaskServiceV2 tasks = null;
		try {
			tasks = (InvokeHumanTaskServiceV2) context
					.lookup("InvokeHumanTaskServiceV2#com.shaic.ims.bpm.claim.servicev2.InvokeHumanTaskServiceV2");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	*//**
	 * Method to invoke BPM service for BillEntry Service
	 * Search menu
	 * 
	 * *//*
	public static ProcessClaimRequestZMRTask getprocessClaimRequestZonalTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		ProcessClaimRequestZMRTask tasks = null;
		try {
			tasks = (ProcessClaimRequestZMRTask) context
					.lookup("ProcessClaimRequestZMRTask#com.shaic.ims.bpm.claim.servicev2.medical.search.ProcessClaimRequestZMRTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	*//**
	 * Method to invoke BPM service for BillEntry Service
	 * Search menu
	 * 
	 * *//*
	public static ProcessClaimRequestTask getprocessClaimRequestTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		ProcessClaimRequestTask tasks = null;
		try {
			tasks = (ProcessClaimRequestTask) context
					.lookup("ProcessClaimRequestTask#com.shaic.ims.bpm.claim.servicev2.medical.search.ProcessClaimRequestTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke BPM service for submit specaialist search Service
	 * Search menu
	 * 
	 * *//*
	public static SpecialistTask getSpecialistTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		SpecialistTask tasks = null;
		try {
			tasks = (SpecialistTask) context
					.lookup("SpecialistTask#com.shaic.ims.bpm.claim.servicev2.medical.search.SpecialistTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	public static DownsizeSpecialistAdviseTask getDownsizeSpecialistAdviseTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		DownsizeSpecialistAdviseTask tasks = null;
		try {
			tasks = (DownsizeSpecialistAdviseTask) context
					.lookup("DownsizeSpecialistAdviseTask#com.shaic.ims.bpm.claim.servicev2.preauth.search.DownsizeSpecialistAdviseTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	*//**
	 * Method to invoke BPM service for submit FVR search Service
	 * Search menu
	 * 
	 * *//*
	public static ProcessFieldVisitTask getProcessFieldVisitTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		ProcessFieldVisitTask tasks = null;
		try {
			tasks = (ProcessFieldVisitTask) context
					.lookup("ProcessFieldVisitTask#com.shaic.ims.bpm.claim.servicev2.fvr.search.ProcessFieldVisitTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	

	*//**
	 * Method to invoke BPM service for submit investigation search Service
	 * Search menu
	 * 
	 * *//*
	
	public static ProcessInvestigationTask getProcessInvestigationInitTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		ProcessInvestigationTask tasks = null;
		try {
			tasks = (ProcessInvestigationTask) context
					.lookup("ProcessInvestigationTask#com.shaic.ims.bpm.claim.servicev2.investigation.search.ProcessInvestigationTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * BPM Service Lookup for Submiting Draft Query Letter
	 * 
	 * @param username
	 * @param password
	 * @return
	 *//*
	
	public static SubmitDraftQueryLetterTask getSubmitDraftQueryService(
			String username, String password) {
		Context context = getInitialContext(username, password);
		SubmitDraftQueryLetterTask task = null;
		try {
			task = (SubmitDraftQueryLetterTask) context
					.lookup("SubmitDraftQueryLetterTask#com.shaic.ims.bpm.claim.servicev2.query.SubmitDraftQueryLetterTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		return task;
	}
	
	*//**
	 * BPM Service Lookup for Submiting Process Draft Query Letter
	 * 
	 * @param username
	 * @param password
	 * @return
	 *//*
	
	public static SubmitProcessDraftQueryLetterTask getSubmitProcessDraftQueryService(
			String username, String password) {
		Context context = getInitialContext(username, password);
		SubmitProcessDraftQueryLetterTask tasks = null;
		try {
			tasks = (SubmitProcessDraftQueryLetterTask) context
					.lookup("SubmitProcessDraftQueryLetterTask#com.shaic.ims.bpm.claim.servicev2.query.SubmitProcessDraftQueryLetterTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * BPM Service Lookup for Submiting Draft Rejection Letter
	 * 
	 * @param username
	 * @param password
	 * @return
	 *//*
	
	public static SubmitDraftRejectionLetterTask getSubmitDraftRejectionService(
			String username, String password) {
		Context context = getInitialContext(username, password);
		SubmitDraftRejectionLetterTask task = null;
		try {
			task = (SubmitDraftRejectionLetterTask) context
					.lookup("SubmitDraftRejectionLetterTask#com.shaic.ims.bpm.claim.servicev2.rejection.SubmitDraftRejectionLetterTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		return task;
	}
	
	*//**
	 * BPM Service Lookup for Submiting Process Draft Rejection Letter
	 * 
	 * @param username
	 * @param password
	 * @return
	 *//*
	
	public static SubmitProcessDraftRejectionLetterTask getSubmitProcessDraftRejectionService(
			String username, String password) {
		Context context = getInitialContext(username, password);
		SubmitProcessDraftRejectionLetterTask tasks = null;
		try {
			tasks = (SubmitProcessDraftRejectionLetterTask) context
					.lookup("SubmitProcessDraftRejectionLetterTask#com.shaic.ims.bpm.claim.servicev2.rejection.SubmitProcessDraftRejectionLetterTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}*/
	
	
	
	
	/**
	 * Method to invoke BPM service for Assign investigation search Service
	 * Search menu
	 * 
	 * */
	
/*	public static AssignInvestigationTask getAssignInvestigationTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		AssignInvestigationTask tasks = null;
		try {
			tasks = (AssignInvestigationTask) context
					.lookup("AssignInvestigationTask#com.shaic.ims.bpm.claim.servicev2.investigation.search.AssignInvestigationTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static AssignInvestigationCLTask getAssignInvestigationCashlessTask(String userName, String password){
		Context context = getInitialContext(userName,password);
		AssignInvestigationCLTask tasks = null;
		try{
			tasks = (AssignInvestigationCLTask) context.lookup("AssignInvestigationCLTask#com.shaic.ims.bpm.claim.servicev2.investigation.search.AssignInvestigationCLTask");
			
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	*//**
	 * Method to invoke BPM service for Ack investigation search Service
	 * Search menu
	 * 
	 * *//*
	
	public static AckInvestigationTask getAckInvestigationCplTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		AckInvestigationTask tasks = null;
		try {
			tasks = (AckInvestigationTask) context
					.lookup("AckInvestigationTask#com.shaic.ims.bpm.claim.servicev2.investigation.search.AckInvestigationTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * Method to invoke BPM service for Draft Query letter search Service
	 * Search menu
	 * 
	 * *//*
	
	public static DraftQueryLetterTask getDraftQueryLetterTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		DraftQueryLetterTask tasks = null;
		try {
			tasks = (DraftQueryLetterTask) context
					.lookup("DraftQueryLetterTask#com.shaic.ims.bpm.claim.servicev2.query.search.DraftQueryLetterTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * Method to invoke BPM service for Draft Rejection letter search Service
	 * Search menu
	 * 
	 * *//*
	
	public static DraftRejectionLetterTask getDraftRejectionLetterTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		DraftRejectionLetterTask tasks = null;
		try {
			tasks = (DraftRejectionLetterTask) context
					.lookup("DraftRejectionLetterTask#com.shaic.ims.bpm.claim.servicev2.rejection.search.DraftRejectionLetterTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitFVRTask getSumbitFieldFisit(
			String username, String password) {
		Context context = getInitialContext(username, password);
		SubmitFVRTask tasks = null;
		try {
			tasks = (SubmitFVRTask) context
					.lookup("SubmitFVRTask#com.shaic.ims.bpm.claim.servicev2.fvr.SubmitFVRTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	public static FVR getInitiateFVR(
			String username, String password) {
		Context context = getInitialContext(username, password);
		FVR tasks = null;
		try {
			tasks = (FVR) context
					.lookup("FVR#com.shaic.ims.bpm.claim.servicev2.fvr.FVR");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitAssignFieldVisitTask getSumbitFieldFisitForCashless(
			String username, String password) {
		Context context = getInitialContext(username, password);
		SubmitAssignFieldVisitTask tasks = null;
		try {
			tasks = (SubmitAssignFieldVisitTask) context
					.lookup("SubmitAssignFieldVisitTask#com.shaic.ims.bpm.claim.servicev2.preauth.SubmitAssignFieldVisitTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	

	*//**
	 * Method to invoke BPM service for Generate Remainder letter search Service
	 * Search menu
	 * 
	 * *//*

	public static GenerateReminderLetterTask getGenerateRemainderLetterTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		GenerateReminderLetterTask tasks = null;
		try {
			tasks = (GenerateReminderLetterTask) context
					.lookup("GenerateReminderLetterTask#com.shaic.ims.bpm.claim.servicev2.query.search.GenerateReminderLetterTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static GenerateLetterTask getGenerateReimbRemainderLetterTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		GenerateLetterTask tasks = null;
		try {
			tasks = (GenerateLetterTask) context
					.lookup("GenerateLetterTask#com.shaic.ims.bpm.claim.servicev2.reminder.search.GenerateLetterTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	*//**
	 * Method to invoke BPM service for Generate Cashless Remainder letter search Service
	 * Search menu
	 * 
	 * *//*
	
	public static CLGenerateLetterTask getCashlessRemainderLetterTask(String username, String password) {
		Context context = getInitialContext(username, password);
		CLGenerateLetterTask tasks = null;
		try {
			tasks = (CLGenerateLetterTask) context
					.lookup("CLGenerateLetterTask#com.shaic.ims.bpm.claim.servicev2.reminder.search.CLGenerateLetterTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
		
	}
	
	public static  ReminderTask getReimbRemainderTask(String username, String password) {
		Context context = getInitialContext(username, password);
		ReminderTask tasks = null;
		try {
			tasks = (ReminderTask) context
					.lookup("ReminderTask#com.shaic.ims.bpm.claim.servicev2.reminder.search.ReminderTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
		
	}
	public static CLReminderTask  getGenerateCashlessRemainderLetterTask(String username, String password) {
		Context context = getInitialContext(username, password);
		CLReminderTask tasks = null;
		try {
			tasks = (CLReminderTask) context
					.lookup("CLReminderTask#com.shaic.ims.bpm.claim.servicev2.reminder.search.CLReminderTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
		
	}
	
	public static CLReminder  getCashlessReimnderLetterInitiateTask(String username, String password) {
		Context context = getInitialContext(username, password);
		CLReminder tasks = null;
		try {
			tasks = (CLReminder) context
					.lookup("CLReminder#com.shaic.ims.bpm.claim.servicev2.reminder.CLReminder");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
		
	}
	
	public static ReimbReminder  getInitiateRemainderTaskForReimb(String username, String password) {
		Context context = getInitialContext(username, password);
		ReimbReminder tasks = null;
		try {
			tasks = (ReimbReminder) context
					.lookup("ReimbReminder#com.shaic.ims.bpm.claim.servicev2.reminder.ReimbReminder");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
		
	}
	
	*//**
	 * Used to Submit Cashless Reminder Letter Process to BPM
	 * @param username
	 * @param password
	 * @return
	 *//*

	public static SubmitCLGenerateLetterTask getSubmitCLReimnderLetterTask(String username, String password){
		
		Context context = getInitialContext(username, password);
		SubmitCLGenerateLetterTask tasks = null;
		try {
			tasks = (SubmitCLGenerateLetterTask) context
					.lookup("SubmitCLGenerateLetterTask#com.shaic.ims.bpm.claim.servicev2.reminder.SubmitCLGenerateLetterTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
		
	}
	public static SubmitReminderTask getSubmitReimbReimnderTask(String username, String password){
		
		Context context = getInitialContext(username, password);
		SubmitReminderTask tasks = null;
		try {
			tasks = (SubmitReminderTask) context
					.lookup("SubmitReminderTask#com.shaic.ims.bpm.claim.servicev2.reminder.SubmitReminderTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	
	
	public static SubmitCLReminderTask getSubmitCLReimnderTask(String username, String password){
		
		Context context = getInitialContext(username, password);
		SubmitCLReminderTask tasks = null;
		try {
			tasks = (SubmitCLReminderTask) context
					.lookup("SubmitCLReminderTask#com.shaic.ims.bpm.claim.servicev2.reminder.SubmitCLReminderTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
		
	}
	
	
	public static SubmitGenerateLetterTask getSubmitReimbReimnderLetterTask(String username, String password){
		
		Context context = getInitialContext(username, password);
		SubmitGenerateLetterTask tasks = null;
		try {
			tasks = (SubmitGenerateLetterTask) context
					.lookup("SubmitGenerateLetterTask#com.shaic.ims.bpm.claim.servicev2.reminder.SubmitGenerateLetterTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
		
	}
	
	*//**
	 * Used to Submit Reimbursement QueryReminder Process to BPM
	 * @param username
	 * @param password
	 * @return
	 *//*
	
	public static SubmitGenerateReminderLetterTask getSubmitQueryReimnderLetterTask(String username, String password){
		
		Context context = getInitialContext(username, password);
		SubmitGenerateReminderLetterTask tasks = null;
		try {
			tasks = (SubmitGenerateReminderLetterTask) context
					.lookup("SubmitGenerateReminderLetterTask#com.shaic.ims.bpm.claim.servicev2.query.SubmitGenerateReminderLetterTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
		
	}	
	
	*//**
	 * Used to Initiate Reimbursement Reminder Process
	 * @param username
	 * @param password
	 * @return
	 *//*	
	public static ReimbReminder getReimburseReimnderLetterInitiateTask(String username, String password){
		
		Context context = getInitialContext(username, password);
		ReimbReminder tasks = null;
		try {
			tasks = (ReimbReminder) context
					.lookup("ReimbReminder#com.shaic.ims.bpm.claim.servicev2.reminder.ReimbReminder");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;		
	}
	
	*/
	
//	public static SubmitGenerateLetterTask getSubmitReimnderLetterTask(String username, String password){
//		
//		Context context = getInitialContext(username, password);
//		SubmitGenerateLetterTask tasks = null;
//		try {
//			tasks = (SubmitGenerateLetterTask) context
//					.lookup("SubmitGenerateLetterTask#com.shaic.ims.bpm.claim.servicev2.reminder.SubmitGenerateLetterTask");
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//		return tasks;
//		
//	}
	
	
	
	/**
	 * Method to invoke BPM service for Process Draft query letter search Service
	 * Search menu
	 * 
	 * */
	
	/*public static ProcessDraftQueryLetterTask getProcessDraftQueryTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		ProcessDraftQueryLetterTask tasks = null;
		try {
			tasks = (ProcessDraftQueryLetterTask) context
					.lookup("ProcessDraftQueryLetterTask#com.shaic.ims.bpm.claim.servicev2.query.search.ProcessDraftQueryLetterTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * Method to invoke BPM service for Process Draft Rejection letter search Service
	 * Search menu
	 * 
	 * *//*
	
	public static ProcessDraftRejectionLetterTask getProcessDraftRejectionTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		ProcessDraftRejectionLetterTask tasks = null;
		try {
			tasks = (ProcessDraftRejectionLetterTask) context
					.lookup("ProcessDraftRejectionLetterTask#com.shaic.ims.bpm.claim.servicev2.rejection.search.ProcessDraftRejectionLetterTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * Method to invoke BPM service for Process Claim Billing letter search Service
	 * Search menu
	 * 
	 * *//*
	
	public static ProcessClaimBillingTask getProcessClaimBillingTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		ProcessClaimBillingTask tasks = null;
		try {
			tasks = (ProcessClaimBillingTask) context
					.lookup("ProcessClaimBillingTask#com.shaic.ims.bpm.claim.servicev2.billing.search.ProcessClaimBillingTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static ClaimApprovalTask getClaimApprovalTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		ClaimApprovalTask tasks = null;
		try {
			tasks = (ClaimApprovalTask) context
					.lookup("ClaimApprovalTask#com.shaic.ims.bpm.claim.servicev2.financial.search.ClaimApprovalTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static ProcessClaimTask getProcessClaimTaskForOP(
			String username, String password) {
		Context context = getInitialContext(username, password);
		ProcessClaimTask tasks = null;
		try {
			tasks = (ProcessClaimTask) context
					.lookup("ProcessClaimTask#com.shaic.ims.bpm.claim.servicev2.ophealth.search.ProcessClaimTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static AuditImageService getAuditImageService(
			String username, String password) {
		Context context = getInitialContext(username, password);
		AuditImageService tasks = null;
		try {
			tasks = (AuditImageService) context
					.lookup("AuditImageService#com.shaic.ims.bpm.claim.servicev2.AuditImageService");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * Method to invoke BPM service for Process Claim Financial letter search Service
	 * Search menu
	 * 
	 * *//*
	
	public static ProcessClaimFinancialsTask getProcessClaimfinancialTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		ProcessClaimFinancialsTask tasks = null;
		try {
			tasks = (ProcessClaimFinancialsTask) context
					.lookup("ProcessClaimFinancialsTask#com.shaic.ims.bpm.claim.servicev2.financial.search.ProcessClaimFinancialsTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	

	public static SubmitSpecialistTask getSubmitSpecialistTask(String userName, String passWord){
		Context context = getInitialContext(userName, passWord);
		
		SubmitSpecialistTask tasks = null;
		
		try{
			tasks = (SubmitSpecialistTask) context.lookup("SubmitSpecialistTask#com.shaic.ims.bpm.claim.servicev2.medical.SubmitSpecialistTask");
		} catch(NamingException e){
			e.printStackTrace();
		}
		
		return tasks;
	}
	*//**
	 * Method to invoke BPM service for Process Claim co ordinater search Service
	 * Search menu
	 * 
	 * *//*
	
	public static CoordinatorReplyTask getCoordinatorReplyTask(
			String username, String password) {
		Context context = getInitialContext(username, password);
		CoordinatorReplyTask tasks = null;
		try {
			tasks = (CoordinatorReplyTask) context
					.lookup("CoordinatorReplyTask#com.shaic.ims.bpm.claim.servicev2.medical.search.CoordinatorReplyTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitProcessClaimBillingTask getClaimBillingTask(String userName, String passWord){
		
		Context context = getInitialContext(userName, passWord);
		SubmitProcessClaimBillingTask tasks = null;
		try {
			tasks = (SubmitProcessClaimBillingTask) context
					.lookup("SubmitProcessClaimBillingTask#com.shaic.ims.bpm.claim.servicev2.billing.SubmitProcessClaimBillingTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitProcessClaimTask getProcessClaimOPTask(String userName, String passWord){
		
		Context context = getInitialContext(userName, passWord);
		SubmitProcessClaimTask tasks = null;
		try {
			tasks = (SubmitProcessClaimTask) context
					.lookup("SubmitProcessClaimTask#com.shaic.ims.bpm.claim.servicev2.ophealth.SubmitProcessClaimTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitProcessClaimFinancialsTask getClaimFinancialTask(String userName, String passWord){
		
		Context context = getInitialContext(userName, passWord);
		SubmitProcessClaimFinancialsTask tasks = null;
		try {
			tasks = (SubmitProcessClaimFinancialsTask) context
					.lookup("SubmitProcessClaimFinancialsTask#com.shaic.ims.bpm.claim.servicev2.financial.SubmitProcessClaimFinancialsTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
<<<<<<< HEAD
	*//**
=======
	public static SubmitClaimApprovalTask getSubmitClaimApprovalTask(String userName, String passWord){
		
		Context context = getInitialContext(userName, passWord);
		SubmitClaimApprovalTask tasks = null;
		try {
			tasks = (SubmitClaimApprovalTask) context
					.lookup("SubmitClaimApprovalTask#com.shaic.ims.bpm.claim.servicev2.financial.SubmitClaimApprovalTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	/**
>>>>>>> 33089fd2ea65506eb901441220d70fd5f3dc2bf6
	 * Method to invoke BPM service for Intiate RRC Request.
	 * *//*
	
	public static RRC getIntiateRRCRequestTask (String username, String password) 
	{
		Context context = getInitialContext(username, password);
		RRC tasks = null;
		try {
			tasks = (RRC) context
					.lookup("RRC#com.shaic.ims.bpm.claim.servicev2.rrc.RRC");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * Added for process rrc request task.-- details page
	 * *//*
	public static ProcessRRCRequestTask getProcessRRCRequestTask(String username, String password) 
	{
		Context context = getInitialContext(username, password);
		ProcessRRCRequestTask tasks = null;
		try {
			tasks = (ProcessRRCRequestTask) context
					.lookup("ProcessRRCRequestTask#com.shaic.ims.bpm.claim.servicev2.rrc.search.ProcessRRCRequestTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	public static ReviewRRCRequestTask getReviewRRCRequestTask(String username , String password)
	{
		Context context = getInitialContext(username, password);
		ReviewRRCRequestTask tasks = null;
		try
		{
			tasks = (ReviewRRCRequestTask) context
					.lookup("ReviewRRCRequestTask#com.shaic.ims.bpm.claim.servicev2.rrc.search.ReviewRRCRequestTask");
		}catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * Added for Process rrc request task.-- Details
	 * *//*
	public static SubmitProcessRRCRequestTask getProcessRRCRequestSubmitTask(String username, String password) 
	{
		Context context = getInitialContext(username, password);
		SubmitProcessRRCRequestTask tasks = null;
		try {
			tasks = (SubmitProcessRRCRequestTask) context
					.lookup("SubmitProcessRRCRequestTask#com.shaic.ims.bpm.claim.servicev2.rrc.SubmitProcessRRCRequestTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * Added for Review rrc request task.-- Details
	 * *//*
	public static SubmitReviewRRCRequestTask getReviewRRCRequestSubmitTask(String username, String password) 
	{
		Context context = getInitialContext(username, password);
		SubmitReviewRRCRequestTask tasks = null;
		try {
			tasks = (SubmitReviewRRCRequestTask) context
					.lookup("SubmitReviewRRCRequestTask#com.shaic.ims.bpm.claim.servicev2.rrc.SubmitReviewRRCRequestTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**Added for intimation*//*
	public static IntMsg getIntimationIntiationTask(String userName, String password)
	{
		Context context = getInitialContext(userName, password);
		IntMsg tasks = null;
		try {
			tasks = (IntMsg) context
					.lookup("IntMsg#com.shaic.ims.bpm.claim.servicev2.intimation.IntMsg");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	*//**
	 * Added for convert claim submit
	 * *//*
	
	public static SubmitClaimConvTask getSubmitClaimConvTask(String userName, String password)
	{
		Context context = getInitialContext(userName, password);
		SubmitClaimConvTask tasks = null;
		try
		{
			tasks = (SubmitClaimConvTask) context.lookup("SubmitClaimConvTask#com.shaic.ims.bpm.claim.servicev2.conversion.SubmitClaimConvTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitSSpecialistAdviseTask getRefractoredSubmitSpecialistTask(String userName, String password)
	{
		Context context = getInitialContext(userName, password);
		SubmitSSpecialistAdviseTask tasks = null;
		try
		{
			tasks = (SubmitSSpecialistAdviseTask) context.lookup("SubmitSSpecialistAdviseTask#com.shaic.ims.bpm.claim.servicev2.preauth.SubmitSSpecialistAdviseTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	 
	public static SubmitClaimRegTask getClaimRegistrationSubmitTask(String userName, String passWord){
		
		Context context = getInitialContext(userName, passWord);
		SubmitClaimRegTask tasks = null;
		try {
			tasks = (SubmitClaimRegTask) context
					.lookup("SubmitClaimRegTask#com.shaic.ims.bpm.claim.servicev2.registration.SubmitClaimRegTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	public static GenCovLetterRegTask getCoveringLetterTask(String userName, String passWord){
		
		Context context = getInitialContext(userName, passWord);
		GenCovLetterRegTask tasks = null;
		try {
			tasks = (GenCovLetterRegTask) context.lookup("GenCovLetterRegTask#com.shaic.ims.bpm.claim.servicev2.registration.search.GenCovLetterRegTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static GenCovLetterConvTask getGenerateCoveringLetterTask(String userName, String password){
		
		Context context = getInitialContext(userName, password);
		GenCovLetterConvTask  tasks = null;
		try {
			tasks = (GenCovLetterConvTask) context.lookup("GenCovLetterConvTask#com.shaic.ims.bpm.claim.servicev2.conversion.search.GenCovLetterConvTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
		
	}

	public static SubmitGenCovLetterRegTask getSubmitGenerateCoveringLetterTask(String userName, String passWord){
		
		Context context = getInitialContext(userName, passWord);
		SubmitGenCovLetterRegTask tasks = null;
		try {
			tasks = (SubmitGenCovLetterRegTask) context
					.lookup("SubmitGenCovLetterRegTask#com.shaic.ims.bpm.claim.servicev2.registration.SubmitGenCovLetterRegTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	*//**
	 * Added for process rejection task.
	 * *//*
	
	public static SubmitProcessRejectionTask getRefractoredProcessRejectionTask(String userName, String passWord)
	{
		Context context = getInitialContext(userName, passWord);
		SubmitProcessRejectionTask tasks = null;
		try {
			tasks = (SubmitProcessRejectionTask) context
					.lookup("SubmitProcessRejectionTask#com.shaic.ims.bpm.claim.servicev2.registration.SubmitProcessRejectionTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;

	}
	
	public static SubmitAckHospitalCommunicationTask getRefractoredAckHospitalCommunicationTask(String userName, String passWord)
	{
		Context context = getInitialContext(userName, passWord);
		SubmitAckHospitalCommunicationTask tasks = null;
		try {
			tasks = (SubmitAckHospitalCommunicationTask) context
					.lookup("SubmitAckHospitalCommunicationTask#com.shaic.ims.bpm.claim.servicev2.hms.SubmitAckHospitalCommunicationTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * Added for coordinator reply submit - cashless
	 * *//*
	
	public static SubmitPreAuthTranslDocTask getRefractoredTranslDocTask(String userName, String password)
	{
		Context context = getInitialContext(userName, password);
		SubmitPreAuthTranslDocTask tasks = null;
		try {
			tasks = (SubmitPreAuthTranslDocTask) context
					.lookup("SubmitPreAuthTranslDocTask#com.shaic.ims.bpm.claim.servicev2.preauth.SubmitPreAuthTranslDocTask");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * Added for starfax query search task
	 * *//*
	public static QueryReplyDocTask getQueryReplyDocTask(String userName , String password)
	{
		Context context = getInitialContext(userName, password);
		QueryReplyDocTask tasks = null;
		try
		{
			tasks = (QueryReplyDocTask) context
					.lookup("QueryReplyDocTask#com.shaic.ims.bpm.claim.servicev2.dms.search.QueryReplyDocTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static QueryReplyEnhDocTask getQueryReplyEnhancementDocTask(String userName , String password)
	{
		Context context = getInitialContext(userName, password);
		QueryReplyEnhDocTask tasks = null;
		try
		{
			tasks = (QueryReplyEnhDocTask) context
					.lookup("QueryReplyEnhDocTask#com.shaic.ims.bpm.claim.servicev2.dms.search.QueryReplyEnhDocTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	
	
	*//**
	 * Added for starfax query submit task
	 * *//*
	public static SubmitQueryReplyDocTask getSubmitQueryReplyDocTask(String userName , String password)
	{
		Context context = getInitialContext(userName, password);
		SubmitQueryReplyDocTask tasks = null;
		try
		{
			tasks = (SubmitQueryReplyDocTask) context
					.lookup("SubmitQueryReplyDocTask#com.shaic.ims.bpm.claim.servicev2.dms.SubmitQueryReplyDocTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitQueryReplyEnhDocTask getSubmitQueryReplyEnhancementDocTask(String userName , String password)
	{
		Context context = getInitialContext(userName, password);
		SubmitQueryReplyEnhDocTask tasks = null;
		try
		{
			tasks = (SubmitQueryReplyEnhDocTask) context
					.lookup("SubmitQueryReplyEnhDocTask#com.shaic.ims.bpm.claim.servicev2.dms.SubmitQueryReplyEnhDocTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	*//**
	 * Added for starfax query search task -- Reimbursement search query
	 * *//*
	public static QueryReplyDocReimbTask getReimbQueryReplyDocTask(String userName , String password)
	{
		Context context = getInitialContext(userName, password);
		QueryReplyDocReimbTask tasks = null;
		try
		{
			tasks = (QueryReplyDocReimbTask) context
					.lookup("QueryReplyDocReimbTask#com.shaic.ims.bpm.claim.servicev2.dms.search.QueryReplyDocReimbTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	*//**
	 * Added for starfax query submit task
	 * *//*
	public static SubmitQueryReplyDocReimbTask getReimbSubmitQueryReplyDocTask(String userName , String password)
	{
		Context context = getInitialContext(userName, password);
		SubmitQueryReplyDocReimbTask tasks = null;
		try
		{
			tasks = (SubmitQueryReplyDocReimbTask) context
					.lookup("SubmitQueryReplyDocReimbTask#com.shaic.ims.bpm.claim.servicev2.dms.SubmitQueryReplyDocReimbTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	 * Added for starfax fvr search task
	 * 
	
	public static FVRReplyTask getFVRReplyTask(String userName , String password)
	{
		Context context = getInitialContext(userName, password);
		FVRReplyTask tasks = null;
		try
		{
			tasks = (FVRReplyTask) context
					.lookup("FVRReplyTask#com.shaic.ims.bpm.claim.servicev2.fvr.search.FVRReplyTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * Added for starfax reimbursement submit task
	 * *//*
	public static SubmitFVRReplyTask getReimbSubmitFVRReplyDocTask(String userName , String password)
	{
		Context context = getInitialContext(userName, password);
		SubmitFVRReplyTask tasks = null;
		try
		{
			tasks = (SubmitFVRReplyTask) context
					.lookup("SubmitFVRReplyTask#com.shaic.ims.bpm.claim.servicev2.fvr.SubmitFVRReplyTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	*//**
	 * Added for starfax reimbursement submit task
	 * *//*
	public static ReceivePreAuthTask getPreAuthReceived(String userName , String password)
	{
		Context context = getInitialContext(userName, password);
		ReceivePreAuthTask tasks = null;
		try
		{
			tasks = (ReceivePreAuthTask) context
					.lookup("ReceivePreAuthTask#com.shaic.ims.bpm.claim.servicev2.preauth.search.ReceivePreAuthTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitReceivePreAuthTask getSubmitPreAuthReceivedTask(String userName , String password)
	{
		Context context = getInitialContext(userName, password);
		SubmitReceivePreAuthTask tasks = null;
		try
		{
			tasks = (SubmitReceivePreAuthTask) context
					.lookup("SubmitReceivePreAuthTask#com.shaic.ims.bpm.claim.servicev2.preauth.SubmitReceivePreAuthTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static PaymentCancellationTask getPaymentCancellationTask(String userName, String strPassword)
	{
		Context context = getInitialContext(userName, strPassword);
		PaymentCancellationTask tasks = null;
		try
		{
			tasks = (PaymentCancellationTask) context
					.lookup("PaymentCancellationTask#com.shaic.ims.bpm.claim.servicev2.ack.search.PaymentCancellationTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	
	
	public static SubmitPaymentCancellationTask getSubmitPaymentCancellationTask(String userName, String password)
	{
		Context context = getInitialContext(userName, password);
		SubmitPaymentCancellationTask tasks = null;
		try
		{
			tasks = (SubmitPaymentCancellationTask) context
					.lookup("SubmitPaymentCancellationTask#com.shaic.ims.bpm.claim.servicev2.ack.SubmitPaymentCancellationTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static CancelAcknowledgementTask getCancelAcknowledgementTask(String userName, String password){
		
		Context context = getInitialContext(userName, password);

		CancelAcknowledgementTask tasks = null;
		try
		{
			tasks = (CancelAcknowledgementTask) context
					.lookup("CancelAcknowledgementTask#com.shaic.ims.bpm.claim.servicev2.ack.search.CancelAcknowledgementTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
 
	public static HTAcquireBy getAcquireByUser(String userName, String password){
		
		Context context = getInitialContext(userName, password);

		HTAcquireBy tasks = null;
		try
		{
			tasks = (HTAcquireBy) context
					.lookup("HTAcquireBy#com.shaic.ims.bpm.claim.servicev2.HTAcquireBy");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static CancelAcknowledgement intiateAcknowledgmentTask(String userName, String passWord){
		
		Context context = getInitialContext(userName, passWord);

		CancelAcknowledgement tasks = null;
		try
		{
			tasks = (CancelAcknowledgement) context
					.lookup("CancelAcknowledgement#com.shaic.ims.bpm.claim.servicev2.ack.CancelAcknowledgement");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
		
	}
	
	public static SubmitCancelAcknowledgementTask submitCancelAcknowledgementTask(String userName,String passWord){
		
		Context context = getInitialContext(userName, passWord);

		SubmitCancelAcknowledgementTask tasks = null;
		try
		{
			tasks = (SubmitCancelAcknowledgementTask) context
					.lookup("SubmitCancelAcknowledgementTask#com.shaic.ims.bpm.claim.servicev2.ack.SubmitCancelAcknowledgementTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}

  public static AckProcessConvertClaimToReimbTask getConvertClaimTaskFromAck(String userName, String password){
		
		Context context = getInitialContext(userName, password);
		AckProcessConvertClaimToReimbTask tasks = null;
		try
		{
			tasks = (AckProcessConvertClaimToReimbTask) context
					.lookup("AckProcessConvertClaimToReimbTask#com.shaic.ims.bpm.claim.servicev2.ack.search.AckProcessConvertClaimToReimbTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitAckProcessConvertClaimToReimbTask getSubmitConvertReimbursement(String userName,String password){
		
         Context context = getInitialContext(userName, password);
		
         SubmitAckProcessConvertClaimToReimbTask tasks = null;
		
		try
		{
			tasks = (SubmitAckProcessConvertClaimToReimbTask) context
					.lookup("SubmitAckProcessConvertClaimToReimbTask#com.shaic.ims.bpm.claim.servicev2.ack.SubmitAckProcessConvertClaimToReimbTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
		
	}
	
	  public static SimulateStarfax callSimulateStarfaxBPM()
			  {
			    System.out.println("BPM Init lookup called");
			    Context context =  getInitialContext(BPMN_TASK_USER, BPMN_PASSWORD);
			    SimulateStarfax simulateStarfax = null;
			   
			     try {
					simulateStarfax = (SimulateStarfax) context.lookup("SimulateStarfax#com.shaic.ims.bpm.claim.servicev2.dms.SimulateStarfax");
				} catch (NamingException e) {
					
					e.printStackTrace();
				}

			    return simulateStarfax;
			  }
	
	
    public static FVR getReimbursementFVR(String userName, String password){
    	
    	 Context context = getInitialContext(userName, password);
 		
         FVR tasks = null;
		
		try
		{
			tasks = (FVR) context
					.lookup("FVR#com.shaic.ims.bpm.claim.servicev2.fvr.FVR");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
    	
    }
    
    public static UpdateCashlessPayload getUpdateCashlessPayload(){
    	
    	Context context = getInitialContext(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
    	
    	UpdateCashlessPayload tasks = null;
   		
   		try
   		{
   			tasks = (UpdateCashlessPayload) context
   					.lookup("UpdateCashlessPayload#com.shaic.ims.bpm.claim.servicev2.common.UpdateCashlessPayload");
   		}
   		catch(NamingException e)
   		{
   			e.printStackTrace();
   		}
   		return tasks;
    	
    	
    	
    }
    

    public static UpdateCPUCodeCashless getUpdateCashlessPayloadTask() {

		Context context = getInitialContext(BPMClientContext.BPMN_TASK_USER,
				BPMClientContext.BPMN_PASSWORD);
		UpdateCPUCodeCashless tasks = null;

		try {
			tasks = (UpdateCPUCodeCashless) context
					.lookup("UpdateCPUCodeCashless#com.shaic.ims.bpm.claim.servicev2.common.UpdateCPUCodeCashless");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
    
    
	public static UpdateCPUCode callUpdateCPUCode() {
		Context context = getInitialContext(BPMClientContext.BPMN_TASK_USER,
				BPMClientContext.BPMN_PASSWORD);
		UpdateCPUCode tasks = null;
		try {
			tasks = (UpdateCPUCode) context
					.lookup("UpdateCPUCode#com.shaic.ims.bpm.claim.servicev2.common.UpdateCPUCode");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return tasks;
	}
    

    public static SubmitProcessorPedReqWLTask getSubmitProcessorPedReqWatchList(){
    	
    	Context context = getInitialContext(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
    	
    	SubmitProcessorPedReqWLTask tasks = null;
   		
   		try
   		{
   			tasks = (SubmitProcessorPedReqWLTask) context
   					.lookup("SubmitProcessorPedReqWLTask#com.shaic.ims.bpm.claim.servicev2.ped.SubmitProcessorPedReqWLTask");
   		}
   		catch(NamingException e)
   		{
   			e.printStackTrace();
   		}
   		return tasks;
    	
    }
    
 public static SubmitApproverPedReqWLTask getSubmitApproverPedReqWatchList(){
    	
    	Context context = getInitialContext(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
    	
    	SubmitApproverPedReqWLTask tasks = null;
   		
   		try
   		{
   			tasks = (SubmitApproverPedReqWLTask) context
   					.lookup("SubmitApproverPedReqWLTask#com.shaic.ims.bpm.claim.servicev2.ped.SubmitApproverPedReqWLTask");
   		}
   		catch(NamingException e)
   		{
   			e.printStackTrace();
   		}
   		return tasks;
    	
    }
 
 
 public static ProcessorPedReqWLTask getTaskProcessorPedReqWatchList(){
 	
 	Context context = getInitialContext(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
 	
 	ProcessorPedReqWLTask tasks = null;
		
		try
		{
			tasks = (ProcessorPedReqWLTask) context
					.lookup("ProcessorPedReqWLTask#com.shaic.ims.bpm.claim.servicev2.ped.search.ProcessorPedReqWLTask");
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}
		return tasks;
 	
 }
 
 public static ApproverPedReqWLTask getTaskApproverPedReqWatchList(){
	 	
	 	Context context = getInitialContext(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
	 	
	 	ApproverPedReqWLTask tasks = null;
			
			try
			{
				tasks = (ApproverPedReqWLTask) context
						.lookup("ApproverPedReqWLTask#com.shaic.ims.bpm.claim.servicev2.ped.search.ApproverPedReqWLTask");
			}
			catch(NamingException e)
			{
				e.printStackTrace();
			}
			return tasks;
	 }
 
 
 	public static GetAllHTInfoClient getAllCompletedTask() throws NamingException {
        Context context = getInitialContext(BPMN_TASK_USER,BPMN_PASSWORD);
        GetAllHTInfoClient tasks = null;
        try {
        	tasks = (GetAllHTInfoClient) context.lookup("GetAllHTInfoClient#com.shaic.ims.bpm.claim.servicev2.common.search.GetAllHTInfoClient");
		}
		catch(NamingException e) {
			e.printStackTrace();
		}
        
        return tasks;
    }
 	
 	public static AcquireHTList getAllUnlockedTask() throws NamingException {
        Context context = getInitialContext(BPMN_TASK_USER,BPMN_PASSWORD);
        AcquireHTList tasks = null;
        try {
        	tasks = (AcquireHTList) context.lookup("AcquireHTList#com.shaic.ims.bpm.claim.servicev2.common.search.AcquireHTList");
		}
		catch(NamingException e) {
			e.printStackTrace();
		}
        
        return tasks;
    }
 	
 	public static AutoCloseClaimCLTask getAutoCloseClaimCashlessTask() throws NamingException {
        Context context = getInitialContext(BPMN_TASK_USER,BPMN_PASSWORD);
        AutoCloseClaimCLTask tasks = null;
        try {
        	tasks = (AutoCloseClaimCLTask) context.lookup("AutoCloseClaimCLTask#com.shaic.ims.bpm.claim.servicev2.common.search.AutoCloseClaimCLTask");
		}
		catch(NamingException e) {
			e.printStackTrace();
		}
        
        return tasks;
    }
 	
 	public static AutoCloseClaimReimbTask getAutoCloseClaimReimbursementTask() throws NamingException {
        Context context = getInitialContext(BPMN_TASK_USER,BPMN_PASSWORD);
        AutoCloseClaimReimbTask tasks = null;
        try {
        	tasks = (AutoCloseClaimReimbTask) context.lookup("AutoCloseClaimReimbTask#com.shaic.ims.bpm.claim.servicev2.common.search.AutoCloseClaimReimbTask");
		}
		catch(NamingException e) {
			e.printStackTrace();
		}
        
        return tasks;
    }
 	
 	public static AutoCloseClaimTask getAutoCloseClaimTask() throws NamingException {
        Context context = getInitialContext(BPMN_TASK_USER,BPMN_PASSWORD);
        AutoCloseClaimTask tasks = null;
        try {
        	tasks = (AutoCloseClaimTask) context.lookup("AutoCloseClaimTask#com.shaic.ims.bpm.claim.servicev2.common.search.AutoCloseClaimTask");
		}
		catch(NamingException e) {
			e.printStackTrace();
		}
        
        return tasks;
    }
 	
	public static SubmitAutoCloseClaimCLTask getSubmitAutoCloseClaimCashlessTask(String userName, String passWord){
		Context context = getInitialContext(userName, passWord);
		SubmitAutoCloseClaimCLTask tasks = null;
		
		try{
			tasks = (SubmitAutoCloseClaimCLTask) context.lookup("SubmitAutoCloseClaimCLTask#com.shaic.ims.bpm.claim.servicev2.common.SubmitAutoCloseClaimCLTask");
		} catch(NamingException e){
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static SubmitAutoCloseClaimTask getSubmitAutoCloseClaimTask(String userName, String passWord){
		Context context = getInitialContext(userName, passWord);
		SubmitAutoCloseClaimTask tasks = null;
		
		try{
			tasks = (SubmitAutoCloseClaimTask) context.lookup("SubmitAutoCloseClaimTask#com.shaic.ims.bpm.claim.servicev2.common.SubmitAutoCloseClaimTask");
		} catch(NamingException e){
			e.printStackTrace();
		}
		return tasks;
	}*/
	
	public String getGalaxyviewCashless()
	{
		/**
		 * Release Number : R3
		 * Requirement Number:R0725
		 * Modified By : Durga Rao
		 * Modified On : 15th May 2017
		 * Referred By  : Narasimha
		 */
		Properties prop = readConnectionPropertyFile();
		CASHLESS_DMS_URL = prop.getProperty(SHAConstants.CASHLESS_DMS_URL);
		return CASHLESS_DMS_URL;
	}
	
	public String getJetPrivilegeFlag()
	{
		Properties prop = readConnectionPropertyFile();
		JET_PRIVILEGE_FLAG = prop.getProperty(SHAConstants.JET_PRIVILEGE_FLAG);
		return JET_PRIVILEGE_FLAG;
	}

	public String getIRDAForFvrAndInvs()
	{
		Properties prop = readConnectionPropertyFile();
		IRDA_TAT_FVR_INVS = prop.getProperty(SHAConstants.IRDA_TAT_FVR_INVS);
		return IRDA_TAT_FVR_INVS;
	}
	
	public String getDmsWebserviceUrl()
	{
		Properties prop = readConnectionPropertyFile();
		DMS_FILE_UPLOAD_WEBSERVICE_URL = prop.getProperty(SHAConstants.DMS_UPLOAD_URL_WEBSERVICE);
		return DMS_FILE_UPLOAD_WEBSERVICE_URL;
	}
	
	public String getRSWebserviceUrl() {
		Properties prop = readConnectionPropertyFile();
		RS_DOCUMENT_URL = prop.getProperty(SHAConstants.RS_DOCUMENT_URL);
		return RS_DOCUMENT_URL;
	}
	
	public static Long getLotBatchFectchSize()
	{
		Properties prop = readConnectionPropertyFile();
		LOT_BATCH_SIZE = prop.getProperty(SHAConstants.LOT_BATCH_SIZE);
		return Long.valueOf(LOT_BATCH_SIZE);
	}
	
	public String getDMSViewUrl()
	{
		Properties prop = readConnectionPropertyFile();
		DMS_VIEW_URL = prop.getProperty(SHAConstants.DMS_VIEW_URL);
		return DMS_VIEW_URL;
	}
	
	
	public Integer getAlfrescoTimeOut()
	{
		Properties prop = readConnectionPropertyFile();
		ALFRESCO_CONN_TIMEOUT = prop.getProperty(SHAConstants.ALFRESCO_CONN_TIMEOUT);
		return Integer.valueOf(ALFRESCO_CONN_TIMEOUT);
	}
	
	public String getSpecialistBancsUrl()
	{
		Properties prop = readConnectionPropertyFile();
		BANCS_SPECIALIST_VIEW_URL = prop.getProperty(SHAConstants.BANCS_SPECIALIST_VIEW_URL);
		return BANCS_SPECIALIST_VIEW_URL;
	}
	
	public String getPedDetailsForBancsUrl()
	{
		Properties prop = readConnectionPropertyFile();
		BANCS_PED_DETAILS_VIEW_URL = prop.getProperty(SHAConstants.BANCS_PED_DETAILS_VIEW_URL);
		return BANCS_PED_DETAILS_VIEW_URL;
	}
	
	public String getDoctorRemarksBancsUrl()
	{
		Properties prop = readConnectionPropertyFile();
		BANCS_DOCTOR_REMARKS_URL = prop.getProperty(SHAConstants.BANCS_DOCTOR_REMARKS_URL);
		return BANCS_DOCTOR_REMARKS_URL;
	}
	
	public String getProvisonFlag()
	{
		Properties prop = readConnectionPropertyFile();
		BANCS_PROVISON_FLAG = prop.getProperty(SHAConstants.BANCS_PROVISON_FLAG);
		return BANCS_PROVISON_FLAG;
	}
	
	public String getLockFlag()
	{
		Properties prop = readConnectionPropertyFile();
		BANCS_LOCK_FLAG = prop.getProperty(SHAConstants.BANCS_LOCK_FLAG);
		return BANCS_LOCK_FLAG;
	}
	
	public String getUnLockFlag()
	{
		Properties prop = readConnectionPropertyFile();
		BANCS_UNLOCK_FLAG = prop.getProperty(SHAConstants.BANCS_UN_LOCK_FLAG);
		return BANCS_UNLOCK_FLAG;
	}
	
	public String getPolicyDocumentViewUrl()
	{
		Properties prop = readConnectionPropertyFile();
		BANCS_POLICY_DOCUMENT_URL = prop.getProperty(SHAConstants.BANCS_POLICY_DOCUMENT_URL);
		return BANCS_POLICY_DOCUMENT_URL;
	}
	
	public String getGalaxyMetabaseUrl()
	{
		Properties prop = readConnectionPropertyFile();
		GALAXY_METABASE_URL = prop.getProperty(SHAConstants.GALAXY_METABASE_URL);
		return GALAXY_METABASE_URL;
	}
	
	public String getMetabaseFLPViewUrl()
	{
		Properties prop = readConnectionPropertyFile();
		String METABASE_FLPVIEW_URL = prop.getProperty(SHAConstants.GALAXY_METABASE_FLPVIEW_URL);
		return METABASE_FLPVIEW_URL;
	}
	
	public String getMetabaseDoctorViewUrl()
	{
		Properties prop = readConnectionPropertyFile();
		String METABASE_DOCTOR_URL = prop.getProperty(SHAConstants.GALAXY_METABASE_DOCTOR_URL);
		return METABASE_DOCTOR_URL;
	}
	
	//GLX2020047
	public String getMediBuddyPackageDetails()
	{
		Properties prop = readConnectionPropertyFile();
		PREMIA_MB_PACKAGE_URL = prop.getProperty(SHAConstants.PREMIA_MB_PACKAGE_URL);
		return PREMIA_MB_PACKAGE_URL;
	}
	
	public static boolean showCashlessMenu(){
		Properties prop = readConnectionPropertyFile();
		SHOW_CASHLESS_MENU = prop.getProperty(SHAConstants.SHOW_CASHLESS_MENU);
		if(SHOW_CASHLESS_MENU.equalsIgnoreCase("Yes")){
			return true;
		}else{
			return false;
		}
	}
	
	public String getDashboardUrl(){
		Properties prop = readConnectionPropertyFile();
		CSH_DASHBOARD_URL = prop.getProperty(SHAConstants.CSH_DASHBOARD_URL);
		return CSH_DASHBOARD_URL;
	}
	
	public String getSummaryUrl(){
		Properties prop = readConnectionPropertyFile();
		CSH_DB_URL_ONE = prop.getProperty(SHAConstants.CSH_DB_URL_SUMMARY);
		return CSH_DB_URL_ONE;
	}
	
	public String getTransactionUrl(){
		Properties prop = readConnectionPropertyFile();
		CSH_DB_URL_TWO = prop.getProperty(SHAConstants.CSH_DB_URL_TRANSACTION);
		return CSH_DB_URL_TWO;
	}
	
	public static boolean showMetaBaseMonitoringRpt(){
		Properties prop = readConnectionPropertyFile();
		SHOW_MB_MONITORING_RPT = prop.getProperty(SHAConstants.SHOW_MONITORING_RPT);
		if(SHOW_MB_MONITORING_RPT.equalsIgnoreCase("Yes")){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean showMetaBaseFlrMonitoringRpt(){
		Properties prop = readConnectionPropertyFile();
		SHOW_MB_FLR_MONITORING_RPT = prop.getProperty(SHAConstants.SHOW_FLR_MONITORING_RPT);
		if(SHOW_MB_FLR_MONITORING_RPT.equalsIgnoreCase("Yes")){
			return true;
		}else{
			return false;
		}
	}
	
	public String getBancsBonusFlag()
	{
		Properties prop = readConnectionPropertyFile();
		BANCS_BONUS_FLAG = prop.getProperty(SHAConstants.BANCS_BONUS_FLAG);
		return BANCS_BONUS_FLAG;
	}
	public String getBancsRenewalFlag()
	{
		Properties prop = readConnectionPropertyFile();
		BANCS_RENEWAL_FLAG = prop.getProperty(SHAConstants.BANCS_RENEWAL_FLAG);
		return BANCS_RENEWAL_FLAG;
	}
	
	public String getBancsInstalmentFlag()
	{
		Properties prop = readConnectionPropertyFile();
		BANCS_INSTALMENT_FLAG = prop.getProperty(SHAConstants.BANCS_INSTALMENT_FLAG);
		return BANCS_INSTALMENT_FLAG;
	}
	
	public static boolean showActions(){
		Properties prop = readConnectionPropertyFile();
		SHOW_ACTIONS = prop.getProperty(SHAConstants.SHOW_ACTIONS);
		if(SHOW_ACTIONS.equalsIgnoreCase("Yes")){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean showReports(){
		Properties prop = readConnectionPropertyFile();
		SHOW_REPORTS = prop.getProperty(SHAConstants.SHOW_REPORTS);
		if(SHOW_REPORTS.equalsIgnoreCase("Yes")){
			return true;
		}else{
			return false;
		}
	}
	
	public String getDMSSecretKey()
	{
		Properties prop = readConnectionPropertyFile();
		DMS_SECRET_KEY = prop.getProperty(SHAConstants.DMS_SECRET_KEY);
		return DMS_SECRET_KEY;
	}
	
	
	public String ZOHO_GRIEV_WS_URL = null;
	public String ZOHO_GRIEV_DOC_WS_URL = null;
	
	public String getZohoGrievWSUrl(){
		Properties prop = readConnectionPropertyFile();
		ZOHO_GRIEV_WS_URL = prop.getProperty(SHAConstants.ZOHO_GRIEVANCE_WS_URL);
		return ZOHO_GRIEV_WS_URL;
	}
	public String getZohoGrievDocWSUrl(){
		Properties prop = readConnectionPropertyFile();
		ZOHO_GRIEV_DOC_WS_URL = prop.getProperty(SHAConstants.ZOHO_GRIEVANCE_DOC_WS_URL);
		return ZOHO_GRIEV_DOC_WS_URL;
	}
	
	public String POWER_BI_APPLICATION_ID = null;
	public String POWER_BI_SECRET_ID = null;
	public String POWER_BI_TENANT_ID = null;
	public String POWER_BI_VALUE = null;
	public String POWER_BI_AUTHORITY_URL = null;
	public String POWER_BI_SCOPE_URL = null;
	public String POWER_BI_GROUP_ID = null;
	public String POWER_BI_REPORT_ID = null;
	public String POWER_BI_AUTH_TYPE = null;
	public String POWER_BI_REPORT_URL = null;
	
	public void readPowerBIProperties(){
		Properties prop = readConnectionPropertyFile();
		POWER_BI_APPLICATION_ID = prop.getProperty(SHAConstants.PWR_BI_APPLICATION_ID);
		POWER_BI_SECRET_ID = prop.getProperty(SHAConstants.PWR_BI_SECRET_ID);
		POWER_BI_TENANT_ID = prop.getProperty(SHAConstants.PWR_BI_TENANT_ID);
		POWER_BI_VALUE = prop.getProperty(SHAConstants.PWR_BI_VALUE);
		POWER_BI_AUTHORITY_URL = prop.getProperty(SHAConstants.PWR_BI_AUTHORITY_URL);
		POWER_BI_SCOPE_URL = prop.getProperty(SHAConstants.PWR_BI_SCOPE_URL);
		POWER_BI_GROUP_ID = prop.getProperty(SHAConstants.PWR_BI_GROUP_ID);
		POWER_BI_REPORT_ID = prop.getProperty(SHAConstants.PWR_BI_REPORT_ID);
		POWER_BI_AUTH_TYPE = prop.getProperty(SHAConstants.PWR_BI_AUTH_TYPE);
		POWER_BI_REPORT_URL= prop.getProperty(SHAConstants.PWR_BI_REPORT_URL);
	}
	/*public String getPwrBIApplicationId(){
		Properties prop = readConnectionPropertyFile();
		POWER_BI_SECRET_ID = prop.getProperty(SHAConstants.PWR_BI_SECRET_ID);
		return POWER_BI_SECRET_ID;
	}
	public String getPwrBIApplicationId(){
		Properties prop = readConnectionPropertyFile();
		POWER_BI_TENANT_ID = prop.getProperty(SHAConstants.PWR_BI_TENANT_ID);
		return POWER_BI_TENANT_ID;
	}
	public String getPwrBIApplicationId(){
		Properties prop = readConnectionPropertyFile();
		POWER_BI_VALUE = prop.getProperty(SHAConstants.PWR_BI_VALUE);
		return POWER_BI_VALUE;
	}
	public String getPwrBIApplicationId(){
		Properties prop = readConnectionPropertyFile();
		POWER_BI_AUTHORITY_URL = prop.getProperty(SHAConstants.PWR_BI_AUTHORITY_URL);
		return POWER_BI_AUTHORITY_URL;
	}
	public String getPwrBIApplicationId(){
		Properties prop = readConnectionPropertyFile();
		POWER_BI_SCOPE_URL = prop.getProperty(SHAConstants.PWR_BI_SCOPE_URL);
		return POWER_BI_SCOPE_URL;
	}
	public String getPwrBIApplicationId(){
		Properties prop = readConnectionPropertyFile();
		POWER_BI_GROUP_ID = prop.getProperty(SHAConstants.PWR_BI_GROUP_ID);
		return POWER_BI_GROUP_ID;
	}
	public String getPwrBIApplicationId(){
		Properties prop = readConnectionPropertyFile();
		POWER_BI_REPORT_ID = prop.getProperty(SHAConstants.PWR_BI_REPORT_ID);
		return POWER_BI_REPORT_ID;
	}*/
}
