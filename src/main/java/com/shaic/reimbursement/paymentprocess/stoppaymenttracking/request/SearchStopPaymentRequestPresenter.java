package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.table.Page;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;


@SuppressWarnings("serial")
@ViewInterface(StopPaymentRequestView.class)
public class SearchStopPaymentRequestPresenter extends AbstractMVPPresenter<StopPaymentRequestView> {

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	public static final String SUBMIT_UTR_SEARCH = "UTR Search Submit for view details";
	public static final String GET_SEARCH_RESULT = "Get Search Result for stop Payment Request";
	public static final String RESET_UTR_SEARCH_VIEW = "Reset UTR Search Fields for view details";
	
	@EJB
	private StopPaymentRequestService requestService;
	
	protected void showSearchResultView(@Observes @CDIEvent(GET_SEARCH_RESULT) final ParameterDTO parameters) {
		StopPaymentRequestFormDTO searchDto = (StopPaymentRequestFormDTO) parameters.getPrimaryParameter();
		Page<StopPaymentRequestDto> newIntimationDtoPagedContainer = requestService.showStopPaymentReuest(searchDto);
		view.showSearchResultViewDetail(newIntimationDtoPagedContainer);		
	}
	
	protected void showSearchViewDetailIntimationTable(@Observes @CDIEvent(SUBMIT_UTR_SEARCH) final ParameterDTO parameters) {
		view.showSearchViewDetailIntimationTable((SearchIntimationFormDto) parameters.getPrimaryParameter());
	}
	
	protected void showSearchIntimation(@Observes @CDIEvent(RESET_UTR_SEARCH_VIEW) final ParameterDTO parameters) {
		view.resetView();
		System.out.println("view Entered called");
	}

}
