package com.shaic.claim.reports.powerbi;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class PowerBIReport extends HttpServlet {

	static final Logger logger = LoggerFactory.getLogger(PowerBIReport.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		try{
			request.getRequestDispatcher("/EmbedReport.jsp").forward(request, response);
		}catch(Exception ex){
			logger.error("Exception Occuring Redirecting to Power Bi Jsp File "+ex.getMessage());
			ex.printStackTrace();
		}
	}
}
