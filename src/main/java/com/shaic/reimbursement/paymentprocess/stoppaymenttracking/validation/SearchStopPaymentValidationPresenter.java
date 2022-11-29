package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.table.Page;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestDto;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestFormDTO;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestService;

@ViewInterface(StopPaymentValidationView.class)
public class SearchStopPaymentValidationPresenter extends AbstractMVPPresenter<StopPaymentValidationView> {

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public static final String GET_SEARCH_RESULT = "Get Search Result for stop Payment Validation";
	public static final String RESET_SEARCH_VIEW = "Reset Search Fields for view details";
	public static final String SUBMIT_VALIDATION = "Submit Search Validation view details";
	
	@EJB
	private StopPaymentRequestService requestService;
	
	protected void showSearchResultView(@Observes @CDIEvent(GET_SEARCH_RESULT) final ParameterDTO parameters) {
		StopPaymentRequestFormDTO searchDto = (StopPaymentRequestFormDTO) parameters.getPrimaryParameter();
		Page<StopPaymentRequestDto> newIntimationDtoPagedContainer = requestService.showStopPaymentValidation(searchDto);
		view.showSearchResultViewDetail(newIntimationDtoPagedContainer);		
	}
	
	protected void showSearchViewDetailIntimationTable(@Observes @CDIEvent(SUBMIT_VALIDATION) final ParameterDTO parameters) {
		view.showSearchViewDetailIntimationTable((SearchIntimationFormDto) parameters.getPrimaryParameter());
	}
	
	protected void showSearchIntimation(@Observes @CDIEvent(RESET_SEARCH_VIEW) final ParameterDTO parameters) {
		view.resetView();
		System.out.println("view Entered called");
	}

}
