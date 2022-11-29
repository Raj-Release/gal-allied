package com.shaic.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shaic.ims.bpm.claim.BPMClientContext;

public class ForwardingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected  void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getRequestURI().endsWith("/samlsso")) {
            request.getRequestDispatcher("samlsso-home.jsp").forward(request,response);
        } else if (request.getRequestURI().endsWith("/openid")) {
            request.getRequestDispatcher("openid-home.jsp").forward(request,response);
        } else if (request.getRequestURI().endsWith("/logout")) {
        	request.logout();
        	/*HttpSession session = request.getSession();
        	session.invalidate();*/
        	String redirectUrl = BPMClientContext.KEYCLOAK_SERVER_URL + "/auth/realms/" 
	        	+ BPMClientContext.KEYCLOAK_REALM + "/protocol/openid-connect/logout?client_id="
	        	+ BPMClientContext.KEYCLOAK_CLIENT_ID + "&redirect_uri=" + BPMClientContext.KEYCLOAK_REDIRECT_URL;
        	response.sendRedirect(redirectUrl);
//        	request.getRequestDispatcher("index.html").forward(request,response);
        }
    }
}
