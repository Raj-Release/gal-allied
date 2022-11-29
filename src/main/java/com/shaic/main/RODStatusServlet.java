package com.shaic.main;

import java.io.IOException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.PreauthService;

import net.minidev.json.JSONObject;

public class RODStatusServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final Logger logger = LoggerFactory.getLogger(ClaimStatusServlet.class);

	@Inject
	private ClaimService claimService;

	@EJB
	private PreauthService preauthService;


	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		String queryString = request.getQueryString();
		String rodNUmber="";

		if(queryString != null && queryString.contains("token")){
			JSONObject jSONObject = null;
			jSONObject =getToken(request.getParameter("token"));
			if(jSONObject != null ){
				String source = (String) jSONObject.get(SHAConstants.JSON_APPLICATION_NAME);
				rodNUmber = (String) jSONObject.get(SHAConstants.ROD_NUMBER);
				System.out.println(rodNUmber+"rodNUmber");
				if(rodNUmber != null && !rodNUmber.isEmpty()){
					DocumentDetails docDetails = claimService.getRODBillStatus(rodNUmber);
					if(null != docDetails)
					{
						if(null != docDetails.getDocumentToken())
						{
							final String imageUrl = SHAFileUtils.viewFileByToken(String.valueOf(docDetails.getDocumentToken()));
							request.setAttribute("url",imageUrl);
							if(null != docDetails.getSfFileName())
								request.setAttribute("fileName", docDetails.getSfFileName());
							else if(null != docDetails.getFileName())
								request.setAttribute("fileName", docDetails.getFileName());
							request.getRequestDispatcher("/WEB-INF/DocumentViewJsp.jsp").include(request, response);
						}

					}
					else
					{
						request.getRequestDispatcher("/WEB-INF/RODBillStatusErrorPage.jsp").include(request, response); 
					}
				}
			}
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
