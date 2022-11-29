package com.shaic.claim.omp.ratechange;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryService;


@SuppressWarnings("serial")
@ViewInterface(OMPClaimRateChangeAndOsUpdationView.class)
public class OMPClaimRateChangeAndOsUpdationPresenter extends AbstractMVPPresenter<OMPClaimRateChangeAndOsUpdationView>{
	
	public static final String SUBMIT_SEARCH_VIEW = "Submit Search Fields"; 
	public static final String SUBMIT_SEARCH = "Claim Rate Search Submit";
	public static final String CLEAR_SEARCH_FORM = "Clear Search Form";
	public static final String RESET_SEARCH_FROM = "Reset Search Form";

	@EJB
	private OMPClaimRateChangeAndOsUpdationService searchService;
	
	@EJB
	private OMPProcessRODBillEntryService rodBillentryService;
	private Double[] Object;
	private OMPClaimRateChangeAndOsUpdationDetailTable claimRateTable;
	
	@Inject
	private Instance<OMPClaimRateChangeAndOsUpdationDetailTable> ompClaimCalcViewTableInstance;
	@Override
	public void viewEntered() {
		System.out.println("view Entered called");
//		System.out.println(view.getCurrentParameters());
		}
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SUBMIT_SEARCH) final ParameterDTO parameters) {
		OMPClaimRateChangeAndOsUpdationFormDto searchFormDTO = (OMPClaimRateChangeAndOsUpdationFormDto) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
		view.list(searchService.search(searchFormDTO,userName,passWord));
	}
	
		
	protected void submitSearchViewForm(
			@Observes @CDIEvent(SUBMIT_SEARCH_VIEW) final ParameterDTO parameters) {
		@SuppressWarnings("unchecked")
		List<OMPClaimRateChangeAndOsUpdationTableDTO> searchTableDTO = (List<OMPClaimRateChangeAndOsUpdationTableDTO>) parameters.getPrimaryParameter();
		
		Object[] secondaryParameters = parameters.getSecondaryParameters();
//		Double modify = secondaryParameters;
		Long rodKey = null;
		for(OMPClaimRateChangeAndOsUpdationTableDTO searchDto :searchTableDTO){
			if(searchDto != null){
				searchDto.setConversionRate((Double) secondaryParameters[0]);
			}
//		}
//		for(int index = 0 ; index < searchTableDTO.size() ; index++){
//			
//			rodKey = searchTableDTO.get(index).getKey();
//			if(rodKey != null){
//			OMPReimbursement ompReimbursement = rodBillentryService.getReimbursementByKey(rodKey);
//			if(ompReimbursement != null){
//			ompReimbursement.setInrConversionRate(secondaryParameters[0]);
			searchService.saveReimbursementConversionRate(searchTableDTO);
//			}
//		}
//		Long rodKey = searchTableDTO.getReimbursementKey();
//			claimRateTable = ompClaimCalcViewTableInstance.get();
//			claimRateTable.setTableList(searchTableDTO);
		}
		
		view.buildSuccessLayout();
	}
	
	protected void resetSearchReminderLetterForm(
			@Observes @CDIEvent(RESET_SEARCH_FROM) final ParameterDTO parameters) {
		
		//view.resetReminderLetterSearch();
	}
	
}
