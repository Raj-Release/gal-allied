package com.shaic.claim.pedrequest.approve.bancspedQuery;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(BancsSearchPEDRequestApproveView.class)
public class BancsSearchPEDRequestApprovePresenter extends
		AbstractMVPPresenter<BancsSearchPEDRequestApproveView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String BANCS_SEARCH_BUTTON_CLICK = "bancspedappSearchClick";
	
	@EJB
	private BancsSearchPEDRequestApproveService searchPEDRequestApproveService;	
	
	public void searchClick(
			@Observes @CDIEvent(BANCS_SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		BancsSearchPEDRequestApproveFormDTO searchFormDTO = (BancsSearchPEDRequestApproveFormDTO) parameters
				.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchPEDRequestApproveService.search(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {

	}

}
