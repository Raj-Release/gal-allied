package com.shaic.arch;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.shaic.ims.bpm.claim.BPMClientContext;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class PremiaConstants {

	public static final String UNIQUE_INSTALLMENT_AMOUNT = "GetUniqueInstAmount";
	
	public static final String ADJUST_UNIQUE_INSTALLMENT_AMOUNT = "AdjustUniqueInstallment";
	
	public static final String ADJUST_POLICY_INSTALLMENT = "AdjustPolicyInstallment";
	
	public static final String POLICY_NUMBER = "PolicyNo";
	public static final String INTIMATION_NUMBER = "IntimationNo";
	public static final String CPU_CODE = "CpuCode";
	public static final String ADJUSTMENT_AMOUNT = "AdjustmentAmount";
	public static final String POLICY_STATUS = "CheckEndStatus";
	public static final String POLICY_DETAILS = "GetPolicyDetail";
	public static final String PORTABILITY_POLICY_DETAILS = "GetPortabilityPolicyDetail";
	public static final String POLICY_INSTALLMENT_AMOUNT = "GetPolicyInstlAmount";
	
	public static final String Insert_Bonus_Details = "InsertBonusDetails";
	
	public static Builder getBuilder(String urlString) {
		Builder builder = null;
		try {
			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getClasses().add(JacksonJsonProvider.class);
			
			Client client = Client.create(clientConfig);
			String strPremiaFlag = BPMClientContext.PREMIA_URL;
			WebResource webResource = client.resource(strPremiaFlag + urlString);
			webResource.accept("application/json");
			
			builder =  webResource.type("application/json");
			builder.accept("application/json");

		} catch (Exception e) {
			e.printStackTrace();

		}
		return builder;
	}
}
