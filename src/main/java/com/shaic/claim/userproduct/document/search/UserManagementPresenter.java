package com.shaic.claim.userproduct.document.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.userproduct.document.ProductAndDocumentTypeDTO;
@ViewInterface(UserManagementView.class)
public class UserManagementPresenter  extends AbstractMVPPresenter<UserManagementView>{
	
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doDoctorSearchTableUserManagement";
	
	public static final String CREATE_USER_CLICK = "createUser";
	
	public static final String USER_MANAGEMENT_CPU_LIMIT = "userManagementCpuLimit";
	
	@EJB
	private UserMagmtService userMgmtService;

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchDoctorNameDTO searchFormDTO = (SearchDoctorNameDTO) parameters.getPrimaryParameter();

		
		view.getResultList(userMgmtService.search(searchFormDTO));
	}
	
	@SuppressWarnings({ "deprecation" })
	public void createUser(@Observes @CDIEvent(CREATE_USER_CLICK) final ParameterDTO parameters) {
		
		String searchFormDTO = (String) parameters.getPrimaryParameter();
		String userName = (String) parameters.getSecondaryParameter(0,
				String.class);
		userMgmtService.userCreation(searchFormDTO,userName);
	}

}
