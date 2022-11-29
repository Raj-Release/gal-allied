package com.shaic.main;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.domain.PreauthService;

public class SamplePortalServlet extends HttpServlet {
	
private static final long serialVersionUID = 1L;
	
	private final Logger logger = LoggerFactory.getLogger(SamplePortalServlet.class);
	
	  @EJB
	  private PreauthService preauthService;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		  String queryString = request.getQueryString();
		  
		  String error = "";
		  if(queryString != null && queryString.contains("token")){
		  JSONObject jSONObject = null;
		  jSONObject =getToken(request.getParameter("token"));
		  if(jSONObject != null ){
		  String source = (String) jSONObject.get(SHAConstants.JSON_APPLICATION_NAME);
		  
			 if(source.equals("CRC")){
				 logger.info("Enter SamplePortalServlet.doGet() Method");
				 request.getRequestDispatcher("/WEB-INF/SamplePage.jsp").include(request, response);
			 }else{
				 request.setAttribute("error", error);
				  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response); 
			 }
		  }
		}else{
			request.setAttribute("error", error);
			  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response); 
		}
	
	}
		  
		  
		  private JSONObject getToken(String token){
				
				JSONObject jSONObject = null;
				jSONObject = preauthService.validateToken(token);
				if(jSONObject != null) {
					String requestJsonInString = jSONObject.toJSONString();
					System.out.println("requestJsonInString = " + requestJsonInString);
					
				}
				return jSONObject;
		}
}
