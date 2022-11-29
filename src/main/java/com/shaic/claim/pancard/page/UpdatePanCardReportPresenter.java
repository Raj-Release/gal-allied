package com.shaic.claim.pancard.page;

import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.claim.pancard.search.pages.SearchUploadPanCardTableDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(UpdatePanCardReportView.class)
public class UpdatePanCardReportPresenter extends
		AbstractMVPPresenter<UpdatePanCardReportView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SET_REFERENCE_FOR_INVESTGATION_DETAILS_TABLE_UPLOAD_REPORTS = "PAN_SET_REFERENCE_FOR_INVESTIGATION_DETAILS_TABLE_UPLOAD_REPORTS ";

	public static final String SET_REFERENCE = "PAN_set Referece for Upload documents investigation page";

	public static final String CLAIM_OBJECT = "PAN_Claim Object for upload reprots";

	public static final String UPLOAD_EVENT = "PAN_Upload document button";

	public static final String SUBMIT_EVENT = "PAN_Submit event for update Pan";
	
	public static final String VALIDATE_UPLOAD_INVESTIGATION_REPORT_USER_RRC_REQUEST = "PAN_validate_upload_investigation_user_rrc_request";
	
	public static final String PROCESS_UPLOAD_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "PAN_upload_investigation_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_UPLOAD_INVESTIGATION_SAVE_RRC_REQUEST_VALUES = "PAN_upload_investigation_save_rrc_request_values";

	//@Inject
	//private InvestigationDetailsMapper investigationDetailsMapper;

	//@EJB
	//private HospitalService hospitalService;

	//@EJB
	//private MasterService masterService;

	//@EJB
	//private PreauthService preauthService;

	//@EJB
	//private AcknowledgementDocumentsReceivedService acknowledgementService;

	//@EJB
	//private InvestigationService investigationService;

	//@EJB
	//private ClaimService claimService;
	
	
	@EJB
	private ReimbursementService reimbursementService;

	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_UPLOAD_INVESTIGATION_REPORT_USER_RRC_REQUEST) final ParameterDTO parameters){/*
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	*/}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_UPLOAD_INVESTIGATION_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){/*
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_REIMBURSEMENT);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	*/}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_UPLOAD_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{/*
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		*/}
	/**
	 * Added for RRC Ends
	 * */


	public void setReferenceForInvestigationTableOfUploadReports(
			@Observes @CDIEvent(SET_REFERENCE_FOR_INVESTGATION_DETAILS_TABLE_UPLOAD_REPORTS) final ParameterDTO parameters) {
		
	}

	public void setUploadDocument(
			@Observes @CDIEvent(UPLOAD_EVENT) final ParameterDTO parameters) {

		Long intimationKey = (Long) parameters.getPrimaryParameter();

		String fileName = (String) parameters.getSecondaryParameter(0,
				String.class);
		
		String token = (String)parameters.getSecondaryParameter(1, String.class);
		
		String screenName = (String)parameters.getSecondaryParameter(2, String.class);
		String userName = (String)parameters.getSecondaryParameter(3, String.class);
		
		MultipleUploadDocumentDTO dto = new MultipleUploadDocumentDTO();
		dto.setFileName(fileName);
		dto.setTransactionKey(intimationKey);
		dto.setFileToken(token);
		dto.setTransactionName(screenName);
		dto.setUploadedDate(new Date());
		dto.setUsername(userName);
		reimbursementService.updateDocumentDetails(dto);

		/*Investigation investigation = investigationService
				.getByInvestigationKey(investigationKey);*/
		Intimation intimationByKey = intimationService.getIntimationByKey(intimationKey);
		intimationService.updateDocumentDetails(dto,intimationByKey.getIntimationId());
		view.updateTableValues(intimationByKey.getKey());
		
	}

	public void submitEvent(
			@Observes @CDIEvent(SUBMIT_EVENT) final ParameterDTO parameters) {


		SearchUploadPanCardTableDTO tableDto = (SearchUploadPanCardTableDTO) parameters.getPrimaryParameter();
		
		String screenName = (String) parameters.getSecondaryParameter(0,String.class);
		String policyNo = tableDto.getPolicyNo();
		
		Policy byPolicyNumber = policyService.getByPolicyNumber(policyNo);
		byPolicyNumber.setProposedPanNumber(tableDto.getPanCardNo());
		byPolicyNumber.setProPanCardRemarks(tableDto.getPanRemarks());
		policyService.updatePanCardDetails(byPolicyNumber);
		DBCalculationService dbCalculationService = new DBCalculationService();
		dbCalculationService.stopReminderProcessProcedure(tableDto.getNewIntimationDto().getIntimationId(),SHAConstants.PAN_CARD);
		if(screenName!=null && screenName.equalsIgnoreCase(ReferenceTable.UPDATE_PANCARD_SCREEN)){
			view.result();
		}
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

}
