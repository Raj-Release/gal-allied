package com.shaic.paclaim.reimbursement.searchuploaddocumentsoutsideprocess;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived.UploadDocumentsForAckNotReceivedFormDTO;

@ViewInterface(PAUploadDocumentsOutsideProcessView.class)
public class PAUploadDocumentsOutsideProcessPresenter extends AbstractMVPPresenter<PAUploadDocumentsOutsideProcessView>{



public static final String SEARCH_BUTTON_CLICK = "pa_doSearchUploadDocumentsForAckNotReceived";
	
	public static final String SEARCH_ERROR = "pa_doSearchAddAditionaDocErrorForAckNotReceived";
	
	
	public static final String SEARCH_INVALID_INTIMATION = "pa_doSearchInvalidIntimation";
	
	public static final String SEARCH_INVALID_ACK_NO = "pa_doSearchInvalidAcknowledgeNo";
	
	@EJB
	private PAUploadDocumentsOutsideProcessService searchService;
	
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
