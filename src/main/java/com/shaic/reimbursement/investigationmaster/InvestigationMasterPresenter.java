package com.shaic.reimbursement.investigationmaster;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

@SuppressWarnings("serial")
@ViewInterface(InvestigationMasterView.class)
public class InvestigationMasterPresenter extends AbstractMVPPresenter<InvestigationMasterView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK = "doSearch_Investigation_Master_Table";

	@EJB
	private InvestigationMasterService searchService;


	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {

		InvestigationMasterFormDTO searchFormDTO = (InvestigationMasterFormDTO) parameters.getPrimaryParameter();

		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}

	@Override
	public void viewEntered() {


	}


}
