package com.shaic.claim.reports.powerbi.services;

//import com.embedsample.appownsdata.config.Config;
//import com.embedsample.appownsdata.models.EmbedConfig;
//import com.embedsample.appownsdata.models.ReportConfig;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class PowerBIService {

	static final Logger logger = LoggerFactory.getLogger(PowerBIService.class);
	
	// Prevent instantiation 
	private PowerBIService () {
		throw new IllegalStateException("Power BI service class");
	}
	
	
	public static String getReportDetails(String reportsUrl, String accessToken, String groupId, String reportId){
		
		reportsUrl = reportsUrl.replace("(groupId)", groupId);
		logger.info("POWER BI REPORT API REQUEST "+reportsUrl);
		StringBuilder responseBuilder = new StringBuilder();
		try{
			Client client = com.sun.jersey.api.client.Client.create();
			WebResource webResource = client.resource(reportsUrl);
			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+accessToken)
					.get(ClientResponse.class);
			if(response.getStatus() == 200){
				String output = response.getEntity(String.class);
				logger.info("POWER BI REPORT API RESPONSE "+output);
				if(!StringUtils.isBlank(output)){
					Any biResponse = JsonIterator.deserialize(output);
					if(biResponse != null){
						List<Any> listOfReports = biResponse.get("value").asList();
						if(listOfReports != null && listOfReports.size() > 0){
							responseBuilder.append("[");
							for(Any list : listOfReports){
								if(list.get("id") != null && list.get("id").toString().equalsIgnoreCase(reportId)){
									responseBuilder.append("{");
									responseBuilder.append("\"reportId\":"+"\""+list.get("id").toString()+"\"");
									responseBuilder.append(",");
									responseBuilder.append("\"embedUrl\":"+"\""+list.get("embedUrl").toString()+"\"");
									responseBuilder.append(",");
									responseBuilder.append("\"reportName\":"+"\""+list.get("name").toString()+"\"");
									responseBuilder.append("}");
								}
							}
							responseBuilder.append("]");
						}
					}
				}
			}else if(response.getStatus() != 200){
				logger.info("Power BI Reports API URL Error Response Code : "+response.getStatus());
				String errOutput = response.getEntity(String.class);
				logger.info("POWER BI REPORT API ERROR RESPONSE "+errOutput);
			}
		}catch(Exception ex){
			logger.error("Exception occurred in while getting report details from power bi : "+ex.getMessage());
			ex.printStackTrace();
		}
		return responseBuilder.toString();
	}


	public static String getEmbedToken(String reportUrl, String accessToken, String groupId, String reportId) {
		reportUrl = reportUrl.replace("(groupId)", groupId);
		reportUrl = reportUrl+"/"+reportId+"/GenerateToken";
		String embedToken = "";
		try{
			Client client = com.sun.jersey.api.client.Client.create();
			WebResource webResource = client.resource(reportUrl);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("allowLevel", "View");
			jsonObj.put("allowSaveAs", "true");
			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+accessToken)
					.header("Content-Type", "application/json")
					.post(ClientResponse.class, jsonObj.toString());
			if(response.getStatus() == 200){
				String output = response.getEntity(String.class);
				logger.info("EMBED API RESPONSE "+output);
				if(!StringUtils.isBlank(output)){
					Any embedResponse = JsonIterator.deserialize(output);
					if(embedResponse != null){
						embedToken = embedResponse.get("token").toString();
					}
				}
			}else if(response.getStatus() != 200){
				logger.info("Power BI Embed Token API URL Error Response Code : "+response.getStatus());
				String errOutput = response.getEntity(String.class);
				logger.info("EMBED API ERROR RESPONSE "+errOutput);
			}
		}catch(Exception ex){
			logger.error("Exception occurred in while getting embed token : "+ex.getMessage());
			ex.printStackTrace();
		}
		return embedToken;
	}

}
