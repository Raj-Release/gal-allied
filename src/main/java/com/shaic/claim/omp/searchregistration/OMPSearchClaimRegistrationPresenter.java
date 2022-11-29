package com.shaic.claim.omp.searchregistration;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.table.Page;
import com.shaic.domain.MasterService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.omp.OMPIntimationService;
@SuppressWarnings("serial")
@ViewInterface(OMPSearchClaimRegisterView.class)
public class OMPSearchClaimRegistrationPresenter extends AbstractMVPPresenter<OMPSearchClaimRegisterView>{

	@EJB
	private OMPIntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private MasterService masterService;
		
    private Page<OMPSearchClaimRegistrationTableDTO> registrationListBean;

    private Map<String, Object> referenceData;

    // CDI MVP includes a built-in CDI event qualifier @CDIEvent which
    // uses a String (method identifier) as it's member
    public static final String SEARCH_CLAIMREGISTER_TABLE_SUBMIT = "omp create_claimRegistarction_presenter_search_submit";
    public static final String OMP_CREATE_CLAIM_REGISTRATION = "omp create_claim_registration";
    public static final String GET_INTIMATION_LIST_TO_REGISTER = "omp intimation_list_to_register";
    public static final String GET_INTIMATION_DETAILS = "omp get_intimation_details";
    public static final String GET_REFERENCE_DATA = "omp get_reference_data";
	
	@Override
	public void viewEntered() {
		
	}
	
	 protected void showPolicyDetailsTable(
	            @Observes @CDIEvent(SEARCH_CLAIMREGISTER_TABLE_SUBMIT) final ParameterDTO parameters) {
		 OMPSearchClaimRegistrationFormDto claimRegisterQf = (OMPSearchClaimRegistrationFormDto)parameters.getPrimaryParameter();
		
		 registrationListBean = intimationService.getOmpIntimationToRegister(claimRegisterQf);
			
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
	  
	  @SuppressWarnings("unchecked")
	protected void getIntimationList(
	            @Observes @CDIEvent(GET_INTIMATION_LIST_TO_REGISTER) final ParameterDTO parameters) {
		  
		List<OMPSearchClaimRegistrationTableDTO> searchParameterList = (List<OMPSearchClaimRegistrationTableDTO>) parameters.getPrimaryParameter();
		  List<OMPSearchClaimRegistrationTableDTO> searchResultList = intimationService.searchOmpIntimationToRegister(searchParameterList);  
		  
		  view.setTableDataSource(searchResultList);
	    }
	  
//	  @SuppressWarnings("unchecked")
//		protected void getReferenceData(
//		            @Observes @CDIEvent(GET_REFERENCE_DATA) final ParameterDTO parameters) {
//	  BeanItemContainer<SelectValue> hospitalTypeContainer = masterService.getSelectValueContainer(ReferenceTable.HOSPITAL_TYPE);
//	  referenceData.put("hospitalType",hospitalTypeContainer);
//	  view.setReferenceData(referenceData);
//	  }
}
