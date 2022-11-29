package com.shaic.claim.cvc;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.reimbursement.rod.uploadinvestication.search.SearchUploadInvesticationTableDTO;
import com.shaic.reimbursement.uploadrodreports.UploadedDocumentsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(CVCPageView.class)
public class CVCPagePresenter extends AbstractMVPPresenter<CVCPageView>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SUBMIT_EVENT = "Submit event for CVC";
	
	public static final String LOAD_CVC_STATUS_VALUES = "load_cvc_values_cvc";
	
	public static final String VALIDATE_UPLOAD_INVESTIGATION_REPORT_USER_RRC_REQUEST = "validate_upload_investigation_user_rrc_request";
	
	public static final String PROCESS_UPLOAD_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "upload_investigation_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_UPLOAD_INVESTIGATION_SAVE_RRC_REQUEST_VALUES = "upload_investigation_save_rrc_request_values";
	
	public static final String CANCEL_EVENT = "Cancel event for CVC";

	@EJB
	private MasterService masterService;

	@EJB
	private PreauthService preauthService;

	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private SearchCVCService cvcSearchService;
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_UPLOAD_INVESTIGATION_REPORT_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_UPLOAD_INVESTIGATION_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_UPLOAD_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	
	
	public List<UploadedDocumentsDTO> getUploadDocumentList(List<UploadedDocumentsDTO> updateDocumentDetails, Long rodKey){
		
		Reimbursement reimbObj = reimbursementService.getReimbursementByKey(rodKey);
		Integer sno = 1;
		for (UploadedDocumentsDTO documentDto : updateDocumentDetails) {
			if(documentDto.getFileName() != null){
				documentDto.setSno(sno);
				if(null != reimbObj){
					documentDto.setRODNo(reimbObj.getRodNumber() != null ? reimbObj.getRodNumber() : "");
				}
				sno++;
			}
		}
		return updateDocumentDetails;
	}
	
	public void submitEvent(
			@Observes @CDIEvent(SUBMIT_EVENT) final ParameterDTO parameters) {

		SearchCVCTableDTO tableDto = (SearchCVCTableDTO) parameters.getPrimaryParameter();
		
		cvcSearchService.submitCVCAuditDetails(tableDto);	
		
		view.result();
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	public void loadCVCStatusDropDownValues(
			@Observes @CDIEvent(LOAD_CVC_STATUS_VALUES) final ParameterDTO parameters) 
		{
		
		SearchCVCTableDTO tableDto = (SearchCVCTableDTO) parameters.getPrimaryParameter();
		
		BeanItemContainer<SelectValue> statusValueContainer = masterService.getMasterValueByReference(SHAConstants.CVC_STATUS_CODE);
		BeanItemContainer<SelectValue> ErrorValueContainer = masterService.getCVCErrorCategoryByMaster();
		BeanItemContainer<SelectValue> TeamValueContainer = masterService.getMasterValueByReference(SHAConstants.CVC_TEAM);
		BeanItemContainer<SelectValue> monetaryValueContainer = masterService.getMasterValueByReference(SHAConstants.CVC_MONETARY_RESULT);
		BeanItemContainer<SelectValue> remediationStatusValueContainer = masterService.getMasterValueByReference(SHAConstants.CVC_REMEDIATION_STATUS);
		BeanItemContainer<SelectValue> processorValueContainer = masterService.getCVCProcessorValueContainer(tableDto.getIntimationKey());
		view.loadCVCStatusDropDownValues(statusValueContainer,ErrorValueContainer,TeamValueContainer,
				monetaryValueContainer,remediationStatusValueContainer,processorValueContainer);
		}

	public void cancelEvent(
			@Observes @CDIEvent(CANCEL_EVENT) final ParameterDTO parameters){
		SearchCVCTableDTO tableDto = (SearchCVCTableDTO) parameters.getPrimaryParameter();
		cvcSearchService.updateUnLockFlag(tableDto);	
	}
}
