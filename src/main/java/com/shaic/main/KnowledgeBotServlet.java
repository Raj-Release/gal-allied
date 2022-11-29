package com.shaic.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KnowledgeBotServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  
  private final Logger logger = LoggerFactory.getLogger(KnowledgeBotServlet.class);

  
  private final String USER_AGENT = "Mozilla/5.0";
  
  protected void doGet(HttpServletRequest request, 
      HttpServletResponse response) throws ServletException, IOException{
	  
	  String queryString = request.getQueryString();
	  
	  if(queryString != null && queryString.contains("token")){
		  request.setAttribute("jwt", request.getParameter("token"));
		  System.out.println(request.getParameter("token"));
	  }
	  
	  request.getRequestDispatcher("/WEB-INF/KnowledgeBot.jsp").include(request, response); 
  }
  
}