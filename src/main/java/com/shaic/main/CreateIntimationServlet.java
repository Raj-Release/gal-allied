package com.shaic.main;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.keycloak.KeycloakPrincipal;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.utils.Props;
import com.shaic.claim.policy.search.ui.SearchPolicyViewImpl;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.cdi.server.VaadinCDIServlet;
import com.vaadin.server.Constants;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinSession;

@WebServlet(urlPatterns = { "/createIntimation/*", "/VAADIN/*" }, asyncSupported = true, initParams = {
		@WebInitParam(name = VaadinSession.UI_PARAMETER, value = Props.CREATE_INTIMATION_UI_NAME),
		@WebInitParam(name = "widgetset", value = "com.shaic.WidgetSet.imsWidgetset"),
		@WebInitParam(name = Constants.SERVLET_PARAMETER_UI_PROVIDER, value = "com.vaadin.cdi.CDIUIProvider"),
		@WebInitParam(name = Constants.SERVLET_PARAMETER_PRODUCTION_MODE, value = "true"),
		@WebInitParam(name = "resteasy.scan", value = "false"),
		@WebInitParam(name = "resteasy.scan.resources", value = "false"),
		@WebInitParam(name = "resteasy.scan.providers", value = "false"),
		@WebInitParam(name = Constants.SERVLET_PARAMETER_HEARTBEAT_INTERVAL, value = "-1"),
		@WebInitParam(name = Constants.SERVLET_PARAMETER_CLOSE_IDLE_SESSIONS, value = "true")
		})
public class CreateIntimationServlet extends VaadinCDIServlet {
	private static final long serialVersionUID = -8587507925495817888L;

	@SuppressWarnings("rawtypes")
	public void samlLogin(final HttpServletRequest request) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if(session != null) {
			session.setMaxInactiveInterval(3600);
		}
		
		String username = (String) session.getAttribute(BPMClientContext.USERID);
		KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) session.getAttribute(BPMClientContext.KEYCLOAK_PRINCIPAL);
//		SSOAgentSessionBean ssoAgentBean = (SSOAgentSessionBean) session.getAttribute("ssoAgentBean");
		
		if (keycloakPrincipal != null) {
//			Map<String, String> samlSSOAttributes = ssoAgentBean.getSAMLSSOSessionBean().getSAMLSSOAttributes();
//			username = samlSSOAttributes.get("http://wso2.org/claims/givenname");
			username = keycloakPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername();
		}
	}

	@Override
	protected VaadinServletRequest createVaadinRequest(HttpServletRequest request) {
		
		HttpSession sessionRequest = request.getSession();
		String username = (String) sessionRequest.getAttribute(BPMClientContext.USERID);
		
		VaadinServletRequest vaadinServletRequest = new VaadinServletRequest(request, getService());
//		VaadinSession current = VaadinSession.getCurrent();
//		WrappedSession session2 = VaadinSession.getCurrent().getSession();
//		Integer existingTaskNumber = (Integer) current.getAttribute(SHAConstants.TOKEN_ID);
//    	String userName = (String) vaadinServletRequest.getSession().getAttribute(BPMClientContext.USERID);
//		String passWord = (String) vaadinServletRequest.getSession().getAttribute(BPMClientContext.PASSWORD);
//		System.out.println("userName = " + userName);
		
//		if(existingTaskNumber != null) {
//			vaadinServletRequest.getSession().setAttribute(SHAConstants.TOKEN_ID, null);
//		}
		vaadinServletRequest.getSession().setAttribute(BPMClientContext.USERID, username);
		
		String queryString = request.getQueryString();
		if(queryString != null && queryString.contains("action")){
			String policyNumber = request.getParameter("action");
			vaadinServletRequest.getSession().setAttribute(SHAConstants.ACTION_FOR_CREATE_INTIMATION, policyNumber);
		}
		if(queryString != null && queryString.contains("PolicyNo")){
			String policyNumber = request.getParameter("PolicyNo");
			vaadinServletRequest.getSession().setAttribute(SHAConstants.SEARCH_POLICY_NUMBER, policyNumber);
		}
		if(queryString != null && queryString.contains("IDCardNo")){
			String policyNumber = request.getParameter("IDCardNo");
			vaadinServletRequest.getSession().setAttribute(SHAConstants.SEARCH_HEALTH_CARD_NUMBER, policyNumber);
		}
		if(queryString != null && queryString.contains("AgentId")){
			String agentId = request.getParameter("AgentId");
			vaadinServletRequest.getSession().setAttribute(SHAConstants.CALLER_ATTENDEE_CODE, agentId);
		}
		if(queryString != null && queryString.contains("AgentName")){
			String agentName = request.getParameter("AgentName");
			vaadinServletRequest.getSession().setAttribute(SHAConstants.CALLER_ATTENDEE_NAME, agentName);
		}
		 
		
//		searchPolicyViewImpl.showSearchPolicy();
		
/*		KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) sessionRequest.getAttribute(BPMClientContext.KEYCLOAK_PRINCIPAL);
		vaadinServletRequest.getSession().setAttribute(BPMClientContext.KEYCLOAK_PRINCIPAL, keycloakPrincipal);*/
		
//		SSOAgentSessionBean ssoAgentBean = (SSOAgentSessionBean) sessionRequest.getAttribute("ssoAgentBean");
//		vaadinServletRequest.getSession().setAttribute("ssoAgentBean", ssoAgentBean);
//		ssoAgentBean = null;
		return vaadinServletRequest;
	}
}