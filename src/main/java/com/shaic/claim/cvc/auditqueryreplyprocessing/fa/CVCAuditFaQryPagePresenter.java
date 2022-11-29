package com.shaic.claim.cvc.auditqueryreplyprocessing.fa;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AuditCategory;
import com.shaic.arch.table.AuditDetails;
import com.shaic.arch.table.AuditMasCategory;
import com.shaic.arch.table.AuditProcessor;
import com.shaic.arch.table.AuditTeam;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryFormDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.reimbursement.uploadrodreports.UploadedDocumentsDTO;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.data.util.BeanItemContainer;


@ViewInterface(CVCAuditFaQryPageView.class)
public class CVCAuditFaQryPagePresenter extends AbstractMVPPresenter<CVCAuditFaQryPageView>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SUBMIT_EVENT_AUDIT_QUERY_FA = "Submit event for CVC Audit Fa Qry Rly ";
	
	public static final String LOAD_CVC_AUDIT_ACTION_STATUS_VALUES = "load_cvc_audit_FA_Qry_Rly_status_values";
	
	public static final String VALIDATE_UPLOAD_INVESTIGATION_REPORT_USER_RRC_REQUEST = "validate_upload_investigation_user_rrc_request_Audit_Cls_Qry_Rly";
	
	public static final String PROCESS_UPLOAD_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "upload_investigation_load_rrc_request_drop_down_values_Audit_Cls_Qry_Rly";
	
	public static final String PROCESS_UPLOAD_INVESTIGATION_SAVE_RRC_REQUEST_VALUES = "upload_investigation_save_rrc_request_values_Audit_Cls_Qry_Rly";
	
	public static final String CANCEL_EVENT_AUDIT_ACTION = "Cancel event for CVC Audit Cls Qry Rly";

	@EJB
	private MasterService masterService;

	@EJB
	private PreauthService preauthService;

	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private SearchCVCAuditFaQryService cvcSearchService;
	
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
			@Observes @CDIEvent(SUBMIT_EVENT_AUDIT_QUERY_FA) final ParameterDTO parameters) {

		SearchCVCAuditActionTableDTO tableDto = (SearchCVCAuditActionTableDTO) parameters.getPrimaryParameter();
		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		SearchCVCAuditClsQryFormDTO searchFrmDto = masterService.getAuditReplyUserRoleByUserIdForRplScreen(userId, SHAConstants.AUDIT_BILLING_FA_QRY_REPLY_SCREEN);
		tableDto.setQryReplyBy(userId);
		tableDto.setQryReplyRole(searchFrmDto.getUserRoleName().trim());
		cvcSearchService.submitCVCAuditDetails(tableDto);	
		view.result();
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	public void loadCVCStatusDropDownValues(
			@Observes @CDIEvent(LOAD_CVC_AUDIT_ACTION_STATUS_VALUES) final ParameterDTO parameters) 
		{
		
		SearchCVCAuditActionTableDTO tableDto = (SearchCVCAuditActionTableDTO) parameters.getPrimaryParameter();
		
//		AuditDetails auditDetails = masterService.getCVCAuditActionIntimation(tableDto.getIntimationKey());
		//IMSSUPPOR-30955
		//AuditDetails auditDetails = masterService.getCVCAuditActionByintimationKey(tableDto.getIntimationKey());
		AuditDetails auditDetails = masterService.getCVCAuditActionByAuditKey(tableDto.getAuditKey());
		BeanItemContainer<SelectValue> remediationStatusValueContainer = masterService.getMasterValueByReference(SHAConstants.CVC_REMEDIATION_STATUS);
		BeanItemContainer<SelectValue> statusValueContainer = masterService.getMasterValueByReference(SHAConstants.CVC_STATUS_CODE);
		List<AuditTeam> auditTeam =null;
		List<AuditCategory> auditCategory=null;
		List<AuditProcessor> auditProcessor=null;
		if(auditDetails!=null && auditDetails.getKey()!=null){
	     auditProcessor = masterService.getCVCAuditActionProcessor(auditDetails.getKey());
		 auditTeam = masterService.getCVCAuditActionTeam(auditDetails.getKey());
		 auditCategory = masterService.getCVCAuditActionCategory(auditDetails.getKey());
		}
		BeanItemContainer<SelectValue> cmbCategoryContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(auditCategory!=null && !auditCategory.isEmpty()){
			for (AuditCategory AuditCategory : auditCategory) {
				SelectValue cmbCategoryOfErrortemp = new SelectValue();
				AuditMasCategory auditMasCategory = masterService.getCVCMasCategoryValue(AuditCategory.getAuditCategory());
				cmbCategoryOfErrortemp.setValue(auditMasCategory.getCategory());
				cmbCategoryContainer.addBean(cmbCategoryOfErrortemp);
			}
		}
		BeanItemContainer<SelectValue> TeamValueContainer = masterService.getMasterValueByReference(SHAConstants.CVC_TEAM);
		BeanItemContainer<SelectValue> ErrorValueContainer = masterService.getCVCErrorCategoryByMaster();
		BeanItemContainer<SelectValue> processorValueContainer = masterService.getCVCProcessorValueContainer(tableDto.getIntimationKey());
		BeanItemContainer<SelectValue> monetaryValueContainer = masterService.getMasterValueByReference(SHAConstants.CVC_MONETARY_RESULT);
	
		view.loadCVCStatusDropDownValues(auditDetails,remediationStatusValueContainer,statusValueContainer,
				auditTeam,auditCategory,auditProcessor,cmbCategoryContainer,TeamValueContainer,ErrorValueContainer,
				processorValueContainer,monetaryValueContainer);
		}

	public void cancelEvent(
			@Observes @CDIEvent(CANCEL_EVENT_AUDIT_ACTION) final ParameterDTO parameters){
		SearchCVCAuditActionTableDTO tableDto = (SearchCVCAuditActionTableDTO) parameters.getPrimaryParameter();
		cvcSearchService.updateCVCLockKeyForCancel(tableDto);
	}
}
