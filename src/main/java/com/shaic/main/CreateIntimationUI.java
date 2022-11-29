package com.shaic.main;

import java.util.Date;
import java.util.Locale;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import ma.glasnost.orika.OrikaSystemProperties;

import org.keycloak.KeycloakPrincipal;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.utils.Lang;
import com.shaic.arch.utils.Props;
import com.shaic.claim.policy.search.ui.SearchPolicyViewImpl;
import com.shaic.domain.ClaimService;
import com.shaic.domain.TmpEmployee;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.ui.MenuViewImpl;
import com.shaic.main.navigator.ui.NavigationTree;
import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme(Props.INTIMATION_THEME_NAME)
@CDIUI(value="/createIntimation")
@UIScoped
public class CreateIntimationUI extends UI {
	
	@Inject
	private Instance<MenuViewImpl> mainView;
	
	@Inject
	private NavigationTree tree;

	@Inject
	private Lang lang;
	
	@EJB
	private ClaimService claimService;

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

//		SSOAgentSessionBean ssoAgentBean = (SSOAgentSessionBean) hsRequest.getSession().getAttribute("ssoAgentBean");
//		getSession().setAttribute("ssoAgentBean", ssoAgentBean);
		
		/*SearchPolicyViewImpl searchPolicyViewObj = searchPolicyViewImpl.get();
		searchPolicyViewObj.showSearchPolicy();
		setContent(searchPolicyViewObj);*/

		String action = null;
		if(request.getWrappedSession().getAttribute(SHAConstants.ACTION_FOR_CREATE_INTIMATION) != null){
			
			if(mainView.get().isAttached()){
				mainView.get().detach();
			}
			
			setContent(mainView.get());
			mainView.get().enterIntimationView();
			action = (String) request.getWrappedSession().getAttribute(SHAConstants.ACTION_FOR_CREATE_INTIMATION);
			getSession().setAttribute(SHAConstants.ACTION_FOR_CREATE_INTIMATION,action);
			if(request.getWrappedSession().getAttribute(SHAConstants.CALLER_ATTENDEE_CODE) != null
					&& request.getWrappedSession().getAttribute(SHAConstants.CALLER_ATTENDEE_NAME) != null){
				String userId = (String) request.getWrappedSession().getAttribute(SHAConstants.CALLER_ATTENDEE_CODE);
				String userName = (String) request.getWrappedSession().getAttribute(SHAConstants.CALLER_ATTENDEE_NAME);
				insertOrUpdateEmployeeDetails(userId, userName);
				getSession().setAttribute(BPMClientContext.USERID,userId);
			}
			if(request.getWrappedSession().getAttribute(SHAConstants.CALLER_ATTENDEE_NAME) != null){
				
			}
			if(action.equals("search")){
				tree.callCreateIntimation();
			}else if(action.equals("create")){
				String policyNumber = null;
				String healthCardNumber = null;
				
				if(request.getWrappedSession().getAttribute(SHAConstants.SEARCH_POLICY_NUMBER) != null){
					policyNumber = (String) request.getWrappedSession().getAttribute(SHAConstants.SEARCH_POLICY_NUMBER);
					getSession().setAttribute(SHAConstants.SEARCH_POLICY_NUMBER, policyNumber);
				}
				if(request.getWrappedSession().getAttribute(SHAConstants.SEARCH_HEALTH_CARD_NUMBER) != null){
					healthCardNumber = (String) request.getWrappedSession().getAttribute(SHAConstants.SEARCH_HEALTH_CARD_NUMBER);
					getSession().setAttribute(SHAConstants.SEARCH_HEALTH_CARD_NUMBER, healthCardNumber);
				}
				if(policyNumber != null || healthCardNumber != null){
					tree.callCreateIntimation();
					tree.callCreateIntimationWithSearchParameter(policyNumber, healthCardNumber);
				}
			}
		}

	}
	
	public void insertOrUpdateEmployeeDetails(String userId, String userName){
		if(userId != null && userName != null){
			TmpEmployee tmpEmployee = claimService.getEmployeeName(userId);
			if(tmpEmployee != null) {
				//tmpEmployee.setEmpFirstName(userName);
				//tmpEmployee.setModifiedDate(new Date());
				//claimService.updateEmployeeDetails(tmpEmployee);
			} else {
				tmpEmployee = new TmpEmployee();
				tmpEmployee.setEmpId(userId);
				tmpEmployee.setLoginId(userId);
				tmpEmployee.setEmpFirstName(userName);
				tmpEmployee.setActiveStatus(1L);
				tmpEmployee.setCreatedDate(new Date());
				claimService.insertEmployeeDetails(tmpEmployee);
			}
		}
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
