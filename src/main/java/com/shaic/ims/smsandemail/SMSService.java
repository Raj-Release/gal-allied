//package com.shaic.ims.smsandemail;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.shaic.ims.bpm.claim.BPMClientContext;
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.WebResource;
//import com.sun.jersey.api.client.WebResource.Builder;
//import com.sun.jersey.api.client.config.ClientConfig;
//import com.sun.jersey.api.client.config.DefaultClientConfig;
//
//public class SMSService {
////	private static final SMSService instance = new SMSService();
////	
////	public static SMSService getInstance() {
////		return instance;
////	}
//	
//	public SMSService() {
//		super();
//	}
//	
//	
////	public Builder getBuilderForSmsService() {
//////		ClientConfig clientConfig = new DefaultClientConfig();
//////		clientConfig.getClasses().add(JacksonJsonProvider.class);
//////		Client client = Client.create(clientConfig);
////////		String strPremiaFlag = BPMClientContext.SMS_SERVICE_URL;
//////		WebResource webResource = client.resource(strPremiaFlag);
//////		webResource.accept("application/json");
//////		Builder builder = webResource.type("application/json");
//////		builder.accept("application/json");
//////		return builder;
////	}
//	
//	public void sendSMS() {
////		Builder builderForSmsService = getBuilderForSmsService();
//		try {
//			String url = "http://alert.starhealth.in/message_requests/create_and_enqueue.json";
//			
//			ClientConfig clientConfig = new DefaultClientConfig();
//			clientConfig.getClasses().add(JacksonJsonProvider.class);
//			Client client = Client.create(clientConfig);
//			WebResource webResource = client.resource(url);
//			webResource.accept("application/json");
//			Builder builder = webResource.type("application/json");
//			builder.accept("application/json");
//			
//			
////			Preauth allDatasForSMS = getAllDatasForSMS();
//			JSONObject json = new JSONObject();
//			json.put("application", "DNDOverrideDisclaimer");
//			json.put("reference", "9500898858");
//			json.put("connector", "CallCentre");
//			json.put("message_type", "TextMessageRequest");
//			JSONArray array = new JSONArray();
//	        
////			for(int i=0; i< 2; i++) {
////				Map<String, String> map = new HashMap<String, String>();
////		        map.put("insured", "9500898858");
////		        map.put("Hospital","9500898858");
////		        map.put("sales","9500898858");
////		        map.put("branchManager","9500898858");
////		        map.put("Agent","9500898858");
////		        map.put("Broker","9500898858");
////		        array.put(map);
////			}
//			
//			Map<String, String> payload = new HashMap<String, String>();
//	        payload.put("CUSTOMER_MOBILE_NUMBER", "9500898858");
//	        json.accumulate("payload", payload);
//	        JSONObject jsonOP = new JSONObject();
//	        jsonOP.put("message_request", json);
//			System.out.println(jsonOP.toString());
//			String post = builder.post(String.class, jsonOP.toString());
//		} catch(Exception e) {
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
//		
//	}
//}
