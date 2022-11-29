package com.shaic.paclaim.registration;

import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.table.Page;
import com.shaic.claim.registration.SearchClaimRegisterationFormDto;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PolicyService;

@SuppressWarnings("serial")
@ViewInterface(SearchPAClaimRegisterView.class)
public class SearchPAClaimRegistrationPresenter extends AbstractMVPPresenter<SearchPAClaimRegisterView>{

	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private MasterService masterService;
		
    private Page<SearchClaimRegistrationTableDto> registrationListBean;

    private Map<String, Object> referenceData;
    
    // CDI MVP includes a built-in CDI event qualifier @CDIEvent which
    // uses a String (method identifier) as it's member
    public static final String SEARCH_PA_CLAIMREGISTER_TABLE_SUBMIT = "search_paclaim_Registarction_submit";
    public static final String CREATE_PA_CLAIM_REGISTRATION = "create_pa_claim_registration";
//    public static final String GET_PA_INTIMATION_LIST_TO_REGISTER = "pa_intimation_list_to_register";
    public static final String GET_PA_INTIMATION_DETAILS = "get_pa_intimation_details";
    public static final String GET_PA_REFERENCE_DATA = "get_pa_reference_data";
	
	@Override
	public void viewEntered() {
		
	}
	
	 protected void showPolicyDetailsTable(
	            @Observes @CDIEvent(SEARCH_PA_CLAIMREGISTER_TABLE_SUBMIT) final ParameterDTO parameters) {
		 SearchClaimRegisterationFormDto claimRegisterQf = (SearchClaimRegisterationFormDto)parameters.getPrimaryParameter();
		 
		 registrationListBean = intimationService.getIntimationToRegister(claimRegisterQf);
	
		 view.showSearchClaimRegistrationTable(registrationListBean);

	 }
	 
//	 protected void getIntimationDetails(
//	            @Observes @CDIEvent(GET_INTIMATION_DETAILS) final ParameterDTO parameters) {
//		 
//		 String intimationNumber = parameters.getPrimaryParameter().toString();
//		 Intimation intimation = intimationService.searchbyIntimationNo(intimationNumber);
////		 Policy policy = policyService.getPolicyByKey(intimation.getPolicy().getKey());
//		 NewIntimationDto newIntimationDto  = new NewIntimationMapper().getNewIntimationDto(intimation);
//		 newIntimationDto.setPolicy(intimation.getPolicy());
//		 view.setIntimationDetails(newIntimationDto);  
//	    }
	 
//	  protected void showCreateRegistration(
//	            @Observes @CDIEvent(CREATE_CLAIM_REGISTRATION) final ParameterDTO parameters) {
//
//		  SearchClaimRegistrationTableDto searchClaimRegistrationTableDto = (SearchClaimRegistrationTableDto) parameters.getPrimaryParameter();
//	      view.setViewG(PAClaimRegistrationView.class, true, searchClaimRegistrationTableDto);
//	    }
	  
//	  @SuppressWarnings("unchecked")
//	protected void getIntimationList(
//	            @Observes @CDIEvent(GET_PA_INTIMATION_LIST_TO_REGISTER) final ParameterDTO parameters) {
//		  
//		List<SearchClaimRegistrationTableDto> searchParameterList = (List<SearchClaimRegistrationTableDto>) parameters.getPrimaryParameter();
//		  List<SearchClaimRegistrationTableDto> searchResultList = intimationService.searchIntimationToRegister(searchParameterList);  
//		  
//		  view.setTableDataSource(searchResultList);
//	    }
	  
//	  @SuppressWarnings("unchecked")
//		protected void getReferenceData(
//		            @Observes @CDIEvent(GET_REFERENCE_DATA) final ParameterDTO parameters) {
//	  BeanItemContainer<SelectValue> hospitalTypeContainer = masterService.getSelectValueContainer(ReferenceTable.HOSPITAL_TYPE);
//	  referenceData.put("hospitalType",hospitalTypeContainer);
//	  view.setReferenceData(referenceData);
//	  }
}
