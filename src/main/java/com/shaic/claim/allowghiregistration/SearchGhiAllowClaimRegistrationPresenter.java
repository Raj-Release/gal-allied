package com.shaic.claim.allowghiregistration;

import java.util.List;

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
@ViewInterface(SearchGhiAllowClaimRegisterView.class)
public class SearchGhiAllowClaimRegistrationPresenter extends AbstractMVPPresenter<SearchGhiAllowClaimRegisterView>{

	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private MasterService masterService;
		
    private Page<SearchClaimRegistrationTableDto> registrationListBean;

    //private Map<String, Object> referenceData;
    
    // CDI MVP includes a built-in CDI event qualifier @CDIEvent which
    // uses a String (method identifier) as it's member
    public static final String SEARCH_GHI_ALLOW_CLAIMREGISTER_TABLE_SUBMIT = "ghi_allow_claimRegistarction_presenter_search_submit";
//    public static final String GET_INTIMATION_LIST_OF_GHI_ALLOW_REGISTER = "intimation_list_of_ghi_allow_register";
    public static final String GET_INTIMATION_DETAILS_GHI_ALLOW = "get_intimation_details_ghi_allow";
    public static final String GET_REFERENCE_DATA_GHI_ALLOW = "get_reference_data_ghi_allow";
	
	@Override
	public void viewEntered() {
		
	}
	
	 protected void showPolicyDetailsTable(
	            @Observes @CDIEvent(SEARCH_GHI_ALLOW_CLAIMREGISTER_TABLE_SUBMIT) final ParameterDTO parameters) {
		 SearchClaimRegisterationFormDto claimRegisterQf = (SearchClaimRegisterationFormDto)parameters.getPrimaryParameter();
		 boolean isAllowed = false;
		if(claimRegisterQf.getIntimationNumber() != null && !claimRegisterQf.getIntimationNumber().isEmpty()){
				
			isAllowed = intimationService.isGhiIntimation(claimRegisterQf.getIntimationNumber());
		}	
		 if(isAllowed)
		 {
			 registrationListBean = intimationService.getIntimationToRegister(claimRegisterQf);
		 }
		 else{
			 registrationListBean = null;
		 }
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
//	      view.setViewG(ClaimRegistrationView.class, true, searchClaimRegistrationTableDto);
//	    }
	  
//	  @SuppressWarnings("unchecked")
//	protected void getIntimationList(
//	            @Observes @CDIEvent(GET_INTIMATION_LIST_OF_GHI_ALLOW_REGISTER) final ParameterDTO parameters) {
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
