package com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(UploadDocumentsForAckNotReceivedView.class)
public class UploadDocumentsForAckNotReceivedPresenter extends AbstractMVPPresenter<UploadDocumentsForAckNotReceivedView>{

public static final String SEARCH_BUTTON_CLICK = "doSearchUploadDocumentsForAckNotReceived";
	
	public static final String SEARCH_ERROR = "doSearchAddAditionaDocErrorForAckNotReceived";
	
	
	public static final String SEARCH_INVALID_INTIMATION = "doSearchInvalidIntimation";
	
	public static final String SEARCH_INVALID_ACK_NO = "doSearchInvalidAcknowledgeNo";
	
	@EJB
	private UploadDocumentsForAckNotReceivedService searchService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		UploadDocumentsForAckNotReceivedFormDTO searchFormDTO = (UploadDocumentsForAckNotReceivedFormDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
	/*@SuppressWarnings({ "deprecation" })
	public void handleInvalidIntimation(@Observes @CDIEvent(SEARCH_INVALID_INTIMATION) final ParameterDTO parameters) {
		
		String intimationNo = (String) parameters.getPrimaryParameter();
		view.buildSuccessMessageLayout(searchService.getDocAckByIntimationNo(intimationNo));
	}
	
	@SuppressWarnings({ "deprecation" })
	public void handleInvalidAckNo(@Observes @CDIEvent(SEARCH_INVALID_ACK_NO) final ParameterDTO parameters) {
		
		String ackNo = (String) parameters.getPrimaryParameter();
		view.buildSuccessMessageLayout(searchService.getDocAckByAckNo(ackNo));
	}
	*/
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
}
