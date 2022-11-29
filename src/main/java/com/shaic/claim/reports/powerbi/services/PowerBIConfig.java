package com.shaic.claim.reports.powerbi.services;

import com.shaic.ims.bpm.claim.BPMClientContext;

public class PowerBIConfig {

	// Set this to true, to show debug statements in console
	public static boolean DEBUG = false;

	//	Two possible Authentication methods: 
	//	- For authentication with master user credential choose MasterUser as AuthenticationType.
	//	- For authentication with app secret choose ServicePrincipal as AuthenticationType.
	//	More details here: https://aka.ms/EmbedServicePrincipal
	public static String authenticationType = "";

	//	Common configuration properties for both authentication types
	// Enter workspaceId / groupId
	public static String workspaceId = "";

	// The id of the report to embed.
	public static String reportId = "";

	// Enter Application Id / Client Id
	public static String clientId = "";

	// Enter MasterUser credentials
	public static String pbiUsername = "";
	public static String pbiPassword = "";

	// Enter ServicePrincipal credentials
	public static String tenantId = "";
	public static String appSecret = "";

	//	DO NOT CHANGE
	public static String authorityUrl = null;
	public static String scopeUrl = null;
	public static String reportUrl = null;
	
	public PowerBIConfig(){
		BPMClientContext config = new BPMClientContext();
		config.readPowerBIProperties();

		authorityUrl = config.POWER_BI_AUTHORITY_URL;
		scopeUrl = config.POWER_BI_SCOPE_URL;
		reportUrl = config.POWER_BI_REPORT_URL;
		
		tenantId = config.POWER_BI_TENANT_ID;
		clientId = config.POWER_BI_APPLICATION_ID;
		appSecret = config.POWER_BI_VALUE;
		
		workspaceId = config.POWER_BI_GROUP_ID;
		reportId = config.POWER_BI_REPORT_ID;
		
		authenticationType = config.POWER_BI_AUTH_TYPE;
	}

}
