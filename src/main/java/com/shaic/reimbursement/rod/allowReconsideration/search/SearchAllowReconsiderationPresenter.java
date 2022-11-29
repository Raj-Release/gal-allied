package com.shaic.reimbursement.rod.allowReconsideration.search;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.claim.fieldVisitPage.TmpFVRDTO;
import com.shaic.domain.ReimbursementRejectionService;
import com.shaic.reimbursement.manageclaim.reopenclaim.sarchClaimLevel.SearchReOpenClaimFormDTO;

@ViewInterface(SearchAllowReconsideration.class)
public class SearchAllowReconsiderationPresenter extends AbstractMVPPresenter<SearchAllowReconsideration> {

	public static final String SEARCH_BUTTON_CLICK = "search allow reconsideration";
	
	public static final String ALLOW_RECONSIDERATION= "allow reconsideration";
	
	public static final String SUBMIT_RECONSIDERATION = "submit allow reconsideration";
	
	@EJB
	public SearchAllowReconsiderService allowReconsiderService;
	
	@EJB
	public ReimbursementRejectionService reimbursementRejectionService;
	
	
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		SearchAllowReconsiderationDTO searchFormDTO = (SearchAllowReconsiderationDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(allowReconsiderService.doSearch(searchFormDTO,userName,passWord));
	}
	
	public void allowRejectionReconsideraion(
			@Observes @CDIEvent(ALLOW_RECONSIDERATION) final ParameterDTO parameters) {
		SearchAllowReconsiderationTableDTO submitAllowDto = (SearchAllowReconsiderationTableDTO) parameters.getPrimaryParameter();
//		Boolean isallowedFlag = (Boolean) parameters.getPrimaryParameter();
		view.setallowedRejectionReconsider(submitAllowDto);
	}
	
	public void submitAllowReconsideration(
			@Observes @CDIEvent(SUBMIT_RECONSIDERATION) final ParameterDTO parameters){
		SearchAllowReconsiderationTableDTO submitAllowDto = (SearchAllowReconsiderationTableDTO) parameters.getPrimaryParameter();
//		String uncheckRemarks = (String) parameters.getSecondaryParameter(0,String.class);
//		Boolean allowUncheck = (Boolean) parameters.getSecondaryParameter(1, Boolean.class);
//		submitAllowDto.setAllowReconsiderFlag(allowUncheck != null && allowUncheck ? "Y" : "N");
//		submitAllowDto.setUnCheckRemarks(uncheckRemarks!=null ? uncheckRemarks: "");
		reimbursementRejectionService.submitAllowRejectionReconsider(submitAllowDto);
		view.buildSuccessLayout();
		
	}
	
	@Override
	public void viewEntered() {
		
		
	}

}
