package com.shaic.claim.reimbursement.opscreen;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.ViewDetails;

@ViewInterface(OpView.class)

public class OpPresenter extends AbstractMVPPresenter<OpView >{
	
	private static final long serialVersionUID = 1L;
	public static final String OP_SCREEN_VIEW= "OP_Screen";
	public static final String PROCESS_OP_QUERY_LAYOUT = "Op Query Layout";
	public static final String PROCESS_OP_APPROVE_LAYOUT = "Op Approve Layout";
	public static final String PROCESS_OP_REJECT_LAYOUT = "Op Reject Layout";
	
	
	@EJB
	private OpService searchService;
		
	@Inject
	private ViewDetails viewDetails;
		
			
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(OP_SCREEN_VIEW) final ParameterDTO parameters) {
		
		OpFormDTO searchFormDTO = (OpFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	public void buildQueryLayout(
			@Observes @CDIEvent(PROCESS_OP_QUERY_LAYOUT) final ParameterDTO parameters) {
		view.buildQueryLayout();
	}
	
	public void buildApproveLayout(
			@Observes @CDIEvent(PROCESS_OP_APPROVE_LAYOUT) final ParameterDTO parameters) {
		view.buildApproveLayout();
	}
	
	public void buildRejectLayout(
			@Observes @CDIEvent(PROCESS_OP_REJECT_LAYOUT) final ParameterDTO parameters) {
		view.buildRejectLayout();
	}
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

}
