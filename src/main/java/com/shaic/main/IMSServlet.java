package com.shaic.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jsoup.select.Elements;
import org.keycloak.KeycloakPrincipal;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.shaic.arch.utils.Props;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.cdi.server.VaadinCDIServlet;
import com.vaadin.cdi.server.VaadinCDIServletService;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.Constants;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.DeploymentConfiguration;

@WebServlet(urlPatterns = { "/claims/*", "/VAADIN/*" }, asyncSupported = true, initParams = {
		@WebInitParam(name = VaadinSession.UI_PARAMETER, value = Props.UI_NAME),
		@WebInitParam(name = "widgetset", value = "com.shaic.WidgetSet.imsWidgetset"),
		@WebInitParam(name = Constants.SERVLET_PARAMETER_UI_PROVIDER, value = "com.vaadin.cdi.CDIUIProvider"),
		@WebInitParam(name = Constants.SERVLET_PARAMETER_PRODUCTION_MODE, value = "true"),
		@WebInitParam(name = "resteasy.scan", value = "false"),
		@WebInitParam(name = "resteasy.scan.resources", value = "false"),
		@WebInitParam(name = "resteasy.scan.providers", value = "false"),
		@WebInitParam(name = Constants.SERVLET_PARAMETER_HEARTBEAT_INTERVAL, value = "-1"),
		@WebInitParam(name = Constants.SERVLET_PARAMETER_CLOSE_IDLE_SESSIONS, value = "true")
		})
public class IMSServlet extends VaadinCDIServlet {
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
		try {
			samlLogin(request);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
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
		
		KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) sessionRequest.getAttribute(BPMClientContext.KEYCLOAK_PRINCIPAL);
		vaadinServletRequest.getSession().setAttribute(BPMClientContext.KEYCLOAK_PRINCIPAL, keycloakPrincipal);
		
//		SSOAgentSessionBean ssoAgentBean = (SSOAgentSessionBean) sessionRequest.getAttribute("ssoAgentBean");
//		vaadinServletRequest.getSession().setAttribute("ssoAgentBean", ssoAgentBean);
//		ssoAgentBean = null;
		/*try{
			vaadinServletRequest.getService().addSessionInitListener(new SessionInitListener(){
	
				@Override
				public void sessionInit(SessionInitEvent event)
						throws ServiceException {
					// TODO Auto-generated method stub
					
					//System.out.println(username);
					
					event.getSession().addBootstrapListener(new BootstrapListener() {
						
						@Override
						public void modifyBootstrapFragment(
								BootstrapFragmentResponse response) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void modifyBootstrapPage(
								BootstrapPageResponse response) {							
							// TODO Auto-generated method stub
							
							if(BPMClientContext.BOT_ENABLE.equalsIgnoreCase("YES")){
								String bot_url = BPMClientContext.BOT_URL;
//								System.out.println(username+" - "+bot_url);
								if(username!=null){
									response.getDocument().head().append("<script type=\"text/javascript\" src=\""+bot_url+"\" jwt=\""+generateBotToken(username)+"\"/>");
								}
							}
							
						} }); 
					} 
				});
		}catch(Exception e){
			e.printStackTrace();
		}*/
		return vaadinServletRequest;
	}
	
}