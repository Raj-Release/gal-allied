package com.shaic.claim.userproduct.document;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.userproduct.document.search.UserACtivationView;
import com.shaic.claim.userproduct.document.search.UserMagmtService;
import com.shaic.claim.userproduct.document.search.UserManagementDTO;
@ViewInterface(UserACtivationView.class)
public class UserActivationPresenter extends
		AbstractMVPPresenter<UserACtivationView> {
	@EJB
	private UserMagmtService userMgmtService;
	public static final String USER_MANAGEMENT_CPU_LIMIT = "userManagementCpuLimit";
	
	public static final String ADD_USER_LIMIT = "adduserlimit";
	
	public static final String USER_LIMIT_DELETION = "deleteuserlimit";

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

	protected void submituserCpuManagement(
			@Observes @CDIEvent(USER_MANAGEMENT_CPU_LIMIT) final ParameterDTO parameters) {

		UserManagementDTO bean = (UserManagementDTO) parameters
				.getPrimaryParameter();

		String userName = (String) parameters.getSecondaryParameter(0,
				String.class);

		userMgmtService.updateUserManagementCpu(bean, userName);
		view.submitValues();
	}
	protected void addUserLimit(
			@Observes @CDIEvent(ADD_USER_LIMIT) final ParameterDTO parameters) {
		String roleCategory = (String) parameters.getPrimaryParameter();
		String limit = (String) parameters.getSecondaryParameter(0,
				String.class);
		
		
		
	}
}
