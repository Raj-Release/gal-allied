package com.shaic.claim.cvc.postprocess;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.cvc.SearchCVCService;
import com.shaic.claim.cvc.SearchCVCTableDTO;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PostProcessClaimCVCAuditView.class)
public class PostProcessClaimCVCPresenter extends AbstractMVPPresenter<PostProcessClaimCVCAuditView>{
	
	public static final String LOAD_POST_PROCESS_CVC_STATUS_VALUES = "load_post_process_cvc_values_cvc";
	
	public static final String POST_PROCESS_CVC_CANCEL_EVENT = "Post Process Cancel event for CVC";
	
	public static final String POST_PROCESS_CVC_SUBMIT_EVENT = "Post Process Claim Submit event for CVC";
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private SearchCVCService cvcSearchService;

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void loadCVCStatusDropDownValues(
			@Observes @CDIEvent(LOAD_POST_PROCESS_CVC_STATUS_VALUES) final ParameterDTO parameters) 
		{
		
		SearchCVCTableDTO tableDto = (SearchCVCTableDTO) parameters.getPrimaryParameter();
		
		BeanItemContainer<SelectValue> statusValueContainer = masterService.getMasterValueByReference(SHAConstants.CVC_STATUS_CODE);
		BeanItemContainer<SelectValue> ErrorValueContainer = masterService.getPostClaimCVCErrorCategoryByMaster();
		BeanItemContainer<SelectValue> TeamValueContainer = masterService.getMasterValueByReference(SHAConstants.CVC_TEAM);
		BeanItemContainer<SelectValue> monetaryValueContainer = masterService.getMasterValueByReference(SHAConstants.CVC_MONETARY_RESULT);
		BeanItemContainer<SelectValue> remediationStatusValueContainer = masterService.getMasterValueByReference(SHAConstants.CVC_REMEDIATION_STATUS);
		BeanItemContainer<SelectValue> processorValueContainer = masterService.getCVCProcessorValueContainer(tableDto.getIntimationKey());
		view.loadCVCStatusDropDownValues(statusValueContainer,ErrorValueContainer,TeamValueContainer,
				monetaryValueContainer,remediationStatusValueContainer,processorValueContainer);
		}
	
	public void submitEvent(
			@Observes @CDIEvent(POST_PROCESS_CVC_SUBMIT_EVENT) final ParameterDTO parameters) {

		SearchCVCTableDTO tableDto = (SearchCVCTableDTO) parameters.getPrimaryParameter();
		
		cvcSearchService.submitPostProcessClaimCVCAuditDetails(tableDto);	
		
		view.result();
	}

}
