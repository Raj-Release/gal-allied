package com.shaic.claim.fileUpload;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTableDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(FileUploadView.class)
public class fileUploadPresenter extends AbstractMVPPresenter<FileUploadView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SET_TABLE_DATA="set table data for Upload Translated Document";
	
	public static final String SUBMIT_BUTTON="Submit button is clicked for Upload Translated Document";
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private CoordinatorService coordinatorService;
	
	@EJB
	private MasterService masterService;

	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocService;
	
	public static final String VALIDATE_COORDINATOR_REPLY_QUERY_USER_RRC_REQUEST = "coordinator_reply_user_rrc_request";
	
	public static final String COORDINATOR_REPLY_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "coordinator_reply_load_rrc_request_drop_down_values";
	
	public static final String COORDINATOR_REPLY_SAVE_RRC_REQUEST_VALUES = "coordinator_reply_save_rrc_request_values";

	public static final String COORDINATOR_REPLY_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "coordinator_reply_load_rrc_request_sub_category_values";

	public static final String COORDINATOR_REPLY_LOAD_RRC_REQUEST_SOURCE_VALUES = "coordinator_reply_load_rrc_request_source_values";
	
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_COORDINATOR_REPLY_QUERY_USER_RRC_REQUEST) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(COORDINATOR_REPLY_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(COORDINATOR_REPLY_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */
	
	
	public void setEditableTable( @Observes@CDIEvent(SET_TABLE_DATA) final ParameterDTO parameters){
		
		SearchProcessTranslationTableDTO fileUploadDTO=(SearchProcessTranslationTableDTO)parameters.getPrimaryParameter();
		
		Intimation intimation=intimationService.getIntimationByNo(fileUploadDTO.getIntimationNo());
		
		Claim claim = null;
		Preauth preauthDetails = null;
		Reimbursement reimbursement = null;
		NewIntimationDto intimationDto=intimationService.getIntimationDto(intimation);
		
		if(null != fileUploadDTO.getKey())
		{
			 preauthDetails=preauthService.getPreauthById(fileUploadDTO.getKey());
			claim = preauthDetails.getClaim();
		}
		else
		{
			reimbursement = ackDocService.getReimbursement(fileUploadDTO.getRodKey());
			claim = reimbursement.getClaim();
		}
		
		   //Claim claim=preauthDetails.getClaim();
 			
		    ClaimDto claimDto=new ClaimDto();
		    claimDto =ClaimMapper.getInstance().getClaimDto(claim);
	        claimDto.setClaimId(claim.getClaimId());
	        MastersValue currency=claim.getCurrencyId();
	        SelectValue currencyId=new SelectValue();
	        currencyId.setId(currency.getKey());
	        currencyId.setValue(currency.getValue());
	        
	        claimDto.setCurrencyId(currencyId);
		Long stageKey = 0l;
		if(null != preauthDetails)
		{
			stageKey=preauthDetails.getStage().getKey();
		}
		else if (null != reimbursement)
		{
			stageKey=reimbursement.getStage().getKey();
		}
		Long claimKey=0l;
		
		fileUploadDTO.setDecisionKey(fileUploadDTO.getKey());

		if(stageKey.equals(ReferenceTable.PROCESS_PRE_MEDICAL) ||stageKey.equals(ReferenceTable.PRE_MEDICAL_PROCESSING_ENHANCEMENT))
		{
			claimKey=preauthDetails.getClaim().getKey();
			fileUploadDTO.setDecisionKey(claimKey);
		}
		
		
		 
		
		
		//fileUploadDTO.setDecisionKey(fileUploadDTO.getKey());

		
		//IMSSUPPOR-26461
		if(fileUploadDTO!=null && fileUploadDTO.getRodKey()!=null){
            Coordinator coordinator=coordinatorService.getCoordinatorByRodKey(fileUploadDTO.getRodKey());
            
            FileUploadDTO fileUploadDto=new FileUploadDTO();
            
            fileUploadDto.setKey(coordinator.getKey());
            fileUploadDto.setRequestType(coordinator.getCoordinatorRequestType().getValue());
            fileUploadDto.setRequestorMarks(coordinator.getRequestorRemarks());
            
            referenceData.put("requestType",coordinator.getCoordinatorRequestType().getValue());
            referenceData.put("requestorMarks",coordinator.getRequestorRemarks());
            view.setReferenceData(referenceData,intimationDto,fileUploadDto,fileUploadDTO,claimDto);
		}else{
            
            Coordinator coordinator=coordinatorService.getCoordinatorByPreauthKey(claim.getKey());
            
            FileUploadDTO fileUploadDto=new FileUploadDTO();
            
            fileUploadDto.setKey(coordinator.getKey());
            fileUploadDto.setRequestType(coordinator.getCoordinatorRequestType().getValue());
            fileUploadDto.setRequestorMarks(coordinator.getRequestorRemarks());
            
            referenceData.put("requestType",coordinator.getCoordinatorRequestType().getValue());
            referenceData.put("requestorMarks",coordinator.getRequestorRemarks());
            view.setReferenceData(referenceData,intimationDto,fileUploadDto,fileUploadDTO,claimDto);
		}
	}
	
	public void submitButtonIsClicked(@Observes@CDIEvent(SUBMIT_BUTTON)final ParameterDTO parameters){
		
		FileUploadDTO bean=(FileUploadDTO)parameters.getPrimaryParameter();
		SearchProcessTranslationTableDTO fileUploadDTO=(SearchProcessTranslationTableDTO)parameters.getSecondaryParameter(0, SearchProcessTranslationTableDTO.class);
		
		Boolean result=coordinatorService.updateCoordinatorRemarks(bean, fileUploadDTO);
		
		if(result){
			view.result();
		}
	}

	@Override
	public void viewEntered() {
		
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(COORDINATOR_REPLY_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(COORDINATOR_REPLY_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}
	
	

}
