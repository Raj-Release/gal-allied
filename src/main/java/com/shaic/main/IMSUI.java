package com.shaic.main;

import java.util.Locale;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import ma.glasnost.orika.OrikaSystemProperties;

import org.keycloak.KeycloakPrincipal;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.utils.Lang;
import com.shaic.arch.utils.Props;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.ui.MenuViewImpl;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
@Theme(Props.THEME_NAME)
@CDIUI(value="/claims")
@UIScoped
public class IMSUI extends UI {

	@Inject
	private Instance<MenuViewImpl> mainView;
	
	@Inject
	private Lang lang;

	@Inject
	@TextBundleUpdated
	private javax.enterprise.event.Event<ParameterDTO> localizeEvent;
	
	WrappedSession wrappedSession;

	@Override
	public void setLocale(final Locale locale) {
		lang.setLocale(locale);
		super.setLocale(locale);
		localizeEvent.fire(new ParameterDTO(locale));
	}

	@Override
	protected void init(final VaadinRequest request) {
		System.setProperty(OrikaSystemProperties.WRITE_SOURCE_FILES,"false");
		System.setProperty(OrikaSystemProperties.WRITE_SOURCE_FILES_TO_PATH,"false");
		System.setProperty(OrikaSystemProperties.WRITE_CLASS_FILES,"false");
		System.setProperty(OrikaSystemProperties.WRITE_CLASS_FILES_TO_PATH,"false");
		setLocale(Lang.EN_US);
		
		VaadinServletRequest vsRequest = (VaadinServletRequest)request;
		HttpServletRequest hsRequest = vsRequest.getHttpServletRequest();
		String username = (String) hsRequest.getSession().getAttribute(BPMClientContext.USERID); 
		
		getSession().setAttribute(BPMClientContext.USERID, username);
		
		
		KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) hsRequest.getSession().getAttribute(BPMClientContext.KEYCLOAK_PRINCIPAL);
		getSession().setAttribute(BPMClientContext.KEYCLOAK_PRINCIPAL, keycloakPrincipal);
		
//		SSOAgentSessionBean ssoAgentBean = (SSOAgentSessionBean) hsRequest.getSession().getAttribute("ssoAgentBean");
//		getSession().setAttribute("ssoAgentBean", ssoAgentBean);
		
		wrappedSession = VaadinService.getCurrentRequest().getWrappedSession();
		final DetachListener detachListener = new DetachListener() {
            @Override
            public void detach(final DetachEvent detachEvent) {
                Integer taskNumber = (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
                releaseHumanTask();
                try {
//                	System.out.println("*************************************SESSION BEING INVALIDATED*****************************************");
//                    wrappedSession.invalidate();
//                    System.out.println("*************************************SESSION BEING INVALIDATED*****************************************");
                } catch (IllegalStateException ise) {
                	// Silently fail for now.
                }
            }
        };
        getUI().getCurrent().addDetachListener(detachListener);
		setContent(mainView.get());
		mainView.get().enter();
	}
	
    public void releaseHumanTask(){
		/*Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
 		Long wrkFlowKey=(Long)getSession().getAttribute(SHAConstants.WK_KEY);

 		if(existingTaskNumber != null){
 			//BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
 		}
 		
 		if(wrkFlowKey != null){
 			DBCalculationService dbService = new  DBCalculationService();
 			dbService.callUnlockProcedure(wrkFlowKey);
 			getSession().setAttribute(SHAConstants.WK_KEY, null);
 		}*/
	}
}
