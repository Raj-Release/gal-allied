package com.shaic.claim.reports.powerbi;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.claim.reports.powerbi.services.AzureADService;
import com.shaic.claim.reports.powerbi.services.PowerBIConfig;
import com.shaic.claim.reports.powerbi.services.PowerBIService;

public class PowerBIController extends HttpServlet {
	
	static final Logger logger = LoggerFactory.getLogger(PowerBIController.class);

	private static final long serialVersionUID = -817675889167569083L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		doPost(request, response);
	}
	
	@SuppressWarnings({"unused" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			PowerBIConfig config = new PowerBIConfig();
			String accessToken = AzureADService.getAccessToken();
			logger.info("reportURL : " +PowerBIConfig.reportUrl);
			logger.info(" accessToken : "+accessToken+" workspaceId/groupId : "+PowerBIConfig.workspaceId+" reportId : "+PowerBIConfig.reportId);
			String reportResponse = PowerBIService.getReportDetails(PowerBIConfig.reportUrl, accessToken, PowerBIConfig.workspaceId, PowerBIConfig.reportId);
			logger.info("Filtered Response : "+reportResponse);
			String reportEmbedToken = PowerBIService.getEmbedToken(PowerBIConfig.reportUrl, accessToken, PowerBIConfig.workspaceId, PowerBIConfig.reportId);
			logger.info("EmbedToken Response : "+reportEmbedToken);
			JSONArray array = new JSONArray(reportResponse);
			
			JSONObject responseObj = new JSONObject();
			responseObj.put("embedToken", reportEmbedToken);
			responseObj.put("embedReports", array);
			responseObj.put("tokenExpiry", 3599);
			
			response.setContentType("application/json");
			String res = responseObj.toString();
			logger.info("response given to jsp : "+res);
			PrintWriter out = response.getWriter();
			out.print(res);
			out.flush();
		}catch(Exception exp){
			logger.error("Exception Occuring While getting the inputs for Power BI Report "+exp.getMessage());
			exp.printStackTrace();
		}
	}
}
