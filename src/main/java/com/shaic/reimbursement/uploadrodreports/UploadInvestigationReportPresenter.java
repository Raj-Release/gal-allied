package com.shaic.reimbursement.uploadrodreports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.MasPrivateInvestigator;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationAssignDetailsMapper;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsMapper;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsReimbursementTableDTO;
import com.shaic.reimbursement.rod.uploadinvestication.search.SearchUploadInvesticationTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(UploadInvestigationReportView.class)
public class UploadInvestigationReportPresenter extends
		AbstractMVPPresenter<UploadInvestigationReportView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SET_REFERENCE_FOR_INVESTGATION_DETAILS_TABLE_UPLOAD_REPORTS = "SET_REFERENCE_FOR_INVESTIGATION_DETAILS_TABLE_UPLOAD_REPORTS ";

	public static final String SET_REFERENCE = "set Referece for Upload documents investigation page";

	public static final String CLAIM_OBJECT = "Claim Object for upload reprots";

	public static final String UPLOAD_EVENT = "Upload document button";

	public static final String SUBMIT_EVENT = "Submit event for upload investigation";
	
	public static final String SAVE_EVENT = "Save event for upload investigation";
	
	public static final String VALIDATE_UPLOAD_INVESTIGATION_REPORT_USER_RRC_REQUEST = "validate_upload_investigation_user_rrc_request";
	
	public static final String PROCESS_UPLOAD_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "upload_investigation_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_UPLOAD_INVESTIGATION_SAVE_RRC_REQUEST_VALUES = "upload_investigation_save_rrc_request_values";

	public static final String PROCESS_UPLOAD_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES = "process_upload_investigation_load_rrc_request_sub_category_values";
	
	public static final String PROCESS_UPLOAD_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES = "process_upload_investigation_load_rrc_request_source_values";
	 
	@Inject
	private InvestigationDetailsMapper investigationDetailsMapper;
	
	@Inject
	private InvestigationAssignDetailsMapper invAssignDtailsMapper; 

	@EJB
	private HospitalService hospitalService;

	@EJB
	private MasterService masterService;

	@EJB
	private PreauthService preauthService;

	@EJB
	private AcknowledgementDocumentsReceivedService acknowledgementService;

	@EJB
	private InvestigationService investigationService;

	@EJB
	private ClaimService claimService;
	
	
	@EJB
	private ReimbursementService reimbursementService;
	
	
	
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
	/**
	 * Added for RRC Ends
	 * */


	public void setReferenceForInvestigationTableOfUploadReports(
			@Observes @CDIEvent(SET_REFERENCE_FOR_INVESTGATION_DETAILS_TABLE_UPLOAD_REPORTS) final ParameterDTO parameters) {
		Long investigationKey = (Long) parameters.getPrimaryParameter();
		Long investigAssignedKey = (Long) parameters.getSecondaryParameter(0, Long.class);
		Long rodKey = (Long) parameters.getSecondaryParameter(1, Long.class);

		Long countOfAckByClaimKey = null;

		ClaimDto claimDto = null;
		Investigation investigation = investigationService
				.getByInvestigationKey(investigationKey);
		if (investigation != null) {
			claimDto = claimService.claimToClaimDTO(investigation.getClaim());
			countOfAckByClaimKey = acknowledgementService
					.getCountOfAckByClaimKey(investigation.getClaim().getKey());
		}
		List<Investigation> investigationList = investigationService
				.getByInvestigation(investigation.getIntimation().getKey());
		
		AssignedInvestigatiorDetails currentassignedObj = investigationService.getAssignedInvestigByKey(investigAssignedKey);

		List<AssignedInvestigatiorDetails> assignedObjListByCode = null;
		
		if(investigation != null && investigation.getIntimation() != null && investigation.getIntimation().getKey() != null && currentassignedObj != null && currentassignedObj.getInvestigatorCode() != null)
			assignedObjListByCode = investigationService.getAssignedInvestionByInvestigatorCodeAndIntimation(currentassignedObj.getInvestigatorCode(),investigation.getIntimation().getKey());
		
		List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList = new ArrayList<InvestigationDetailsReimbursementTableDTO>();
		if(assignedObjListByCode != null && !assignedObjListByCode.isEmpty()){
			for (AssignedInvestigatiorDetails assignedObj : assignedObjListByCode) {
				InvestigationDetailsReimbursementTableDTO investigationDetailsReimbursementTableDTO = new InvestigationDetailsReimbursementTableDTO();
				
				investigationDetailsReimbursementTableDTO.setInvestigatorName(assignedObj.getInvestigatorName());
//				if(assignedObj.getInvestigatorCode().contains("INV")) {
					investigationDetailsReimbursementTableDTO.setInvestigatorCode(assignedObj.getInvestigatorCode());
//				}
				investigationDetailsReimbursementTableDTO.setInvestigationAssignedKey(assignedObj.getKey());
				
				investigationDetailsReimbursementTableDTO.setInvestigationAssignedDate(assignedObj.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(assignedObj.getCreatedDate()) :"");
				Hospitals hospitals = investigation != null && investigation.getIntimation() != null ?  hospitalService.getHospitalById(investigation.getIntimation().getHospital()) : null;
				investigationDetailsReimbursementTableDTO.setHospitalName(hospitals != null ? hospitals.getName() : "");
				if (investigationDetailsReimbursementTableDTO.getInvestigatorCode() != null) {
					if(assignedObj.getZoneCode() != null &&  !(assignedObj.getInvestigatorCode().contains("INV"))){
						Long privateinvestigationKey = Long.valueOf(assignedObj.getInvestigatorCode());
						MasPrivateInvestigator privateInvestigation = investigationService.getPrivateInvestigatorByKey(privateinvestigationKey);
						investigationDetailsReimbursementTableDTO.setInvestigatorContactNo((privateInvestigation.getMobileNumberOne() != null && !("0").equalsIgnoreCase(String.valueOf(privateInvestigation.getMobileNumberOne())) ? privateInvestigation.getMobileNumberOne() : "") + (privateInvestigation.getMobileNumberTwo() != null && ("0").equalsIgnoreCase(String.valueOf(privateInvestigation.getMobileNumberTwo())) ? (", " + privateInvestigation.getMobileNumberTwo()) : ""));
					} else {
						TmpInvestigation tmpInvestigation = investigationService.getTmpInvestigationByInactiveInvestigatorCode(assignedObj.getInvestigatorCode());
						investigationDetailsReimbursementTableDTO.setInvestigatorContactNo((tmpInvestigation.getMobileNumber() != null && !("0").equalsIgnoreCase(String.valueOf(tmpInvestigation.getMobileNumber())) ? tmpInvestigation.getMobileNumber() : "") + (tmpInvestigation.getPhoneNumber() != null && ("0").equalsIgnoreCase(String.valueOf(tmpInvestigation.getPhoneNumber())) ? (", " + tmpInvestigation.getPhoneNumber()) : ""));
					}
				}
			
				if (investigationDetailsReimbursementTableDTO.getInvestigationCompletedDate() != null && !investigationDetailsReimbursementTableDTO
								.getInvestigationCompletedDate().equals("")) {
					Date tempDate = SHAUtils
							.formatTime(investigationDetailsReimbursementTableDTO
									.getInvestigationCompletedDate());
					investigationDetailsReimbursementTableDTO
							.setInvestigationCompletedDate(SHAUtils
									.formatDate(tempDate));
				}		
				
				investigationDetailsReimbursementTableDTO.setSno(assignedObjListByCode.indexOf(assignedObj)+1);
				investigationDetailsReimbursementTableDTO.setInvestigatorStatus(assignedObj.getStatus() != null ? assignedObj.getStatus().getProcessValue() : "");
				investigationDetailsReimbursementTableDTO.setInvestigationCompletedDate(assignedObj.getCompletionDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(assignedObj.getCompletionDate()): "");
				investigationDetailsReimbursementTableDTO.setRemarks(assignedObj.getCompletionRemarks() != null ? assignedObj.getCompletionRemarks() : "" );
				investigationDetailsReimbursementTableDTO.setKey(investigation != null && investigation.getKey() != null ? investigation.getKey() : null); 
				investigationDetailsReimbursementTableDTOList.add(investigationDetailsReimbursementTableDTO);	
			}
		}
		
		/*
		investigationDetailsReimbursementTableDTOList = invAssignDtailsMapper.getAssignedInvestigationDto(assignedObjListByCode);
		
		for (InvestigationDetailsReimbursementTableDTO investigationDetailsReimbursementTableDTO : investigationDetailsReimbursementTableDTOList) {
			investigationDetailsReimbursementTableDTO.setInvestigatorName(currentassignedObj.getInvestigatorName());
			investigationDetailsReimbursementTableDTO.setInvestigatorCode(currentassignedObj.getInvestigatorCode());
			investigationDetailsReimbursementTableDTO.setInvestigationAssignedKey(investigationDetailsReimbursementTableDTO.getKey());
			
			investigationDetailsReimbursementTableDTO.setInvestigationAssignedDate(currentassignedObj.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(currentassignedObj.getCreatedDate()) :"");
			Hospitals hospitals = hospitalService.getHospitalById(Long
					.parseLong(investigationDetailsReimbursementTableDTO
							.getHospitalName()));
			investigationDetailsReimbursementTableDTO.setHospitalName(hospitals
					.getName());
			if (investigationDetailsReimbursementTableDTO.getInvestigatorCode() != null) {
				TmpInvestigation tmpInvestigation = investigationService.getTmpInvestigationByInvestigatorCode(currentassignedObj.getInvestigatorCode());
				investigationDetailsReimbursementTableDTO.setInvestigatorContactNo((tmpInvestigation.getMobileNumber() != null && !("0").equalsIgnoreCase(String.valueOf(tmpInvestigation.getMobileNumber())) ? tmpInvestigation.getMobileNumber() : "") + (tmpInvestigation.getPhoneNumber() != null && ("0").equalsIgnoreCase(String.valueOf(tmpInvestigation.getPhoneNumber())) ? (", " + tmpInvestigation.getPhoneNumber()) : ""));
			}
//			if (investigationDetailsReimbursementTableDTO
//					.getInvestigationAssignedDate() != null
//					&& !investigationDetailsReimbursementTableDTO
//							.getInvestigationAssignedDate().equals("")) {
//				Date tempDate = SHAUtils
//						.formatTimestamp(investigationDetailsReimbursementTableDTO
//								.getInvestigationAssignedDate());
//				investigationDetailsReimbursementTableDTO
//						.setInvestigationAssignedDate(SHAUtils
//								.formatDate(tempDate));
//			}			
			if (investigationDetailsReimbursementTableDTO.getInvestigationCompletedDate() != null && !investigationDetailsReimbursementTableDTO
							.getInvestigationCompletedDate().equals("")) {
				Date tempDate = SHAUtils
						.formatTime(investigationDetailsReimbursementTableDTO
								.getInvestigationCompletedDate());
				investigationDetailsReimbursementTableDTO
						.setInvestigationCompletedDate(SHAUtils
								.formatDate(tempDate));
			}		

			if (investigationDetailsReimbursementTableDTO
					.getInvestigationAssignedDate() != null) {
				// Date tempDate = SHAUtils
				// .formatTimestamp(investigationDetailsReimbursementTableDTO
				// .getInvestigationAssignedDate().toString());
				// investigationDetailsReimbursementTableDTO
				// .setInvestigationAssignedDate(SHAUtils
				// .formatDate(tempDate));
			}
		}*/
		view.setReferenceForCorosal(claimDto);
		view.setReference(investigationDetailsReimbursementTableDTOList,
				countOfAckByClaimKey);
		view.setUploadInvestigationReportUI(investigationDetailsReimbursementTableDTOList);
	}

	public void setUploadDocument(
			@Observes @CDIEvent(UPLOAD_EVENT) final ParameterDTO parameters) {

		Long transacKey = (Long) parameters.getPrimaryParameter();

		String fileName = (String) parameters.getSecondaryParameter(0,
				String.class);
		
		String token = (String)parameters.getSecondaryParameter(1, String.class);
		
		String screenName = (String)parameters.getSecondaryParameter(2, String.class);
		
//		Long assignedInvestigationKey = (Long) parameters.getSecondaryParameter(3, Long.class);
		
		SearchUploadInvesticationTableDTO searchUploadInvTableDto = (SearchUploadInvesticationTableDTO) parameters.getSecondaryParameter(3, SearchUploadInvesticationTableDTO.class);
		
//		MultipleUploadDocumentDTO dto = new MultipleUploadDocumentDTO();
//		dto.setFileName(fileName);
//		dto.setTransactionKey(searchUploadInvTableDto.getInvestigationAssignedKey());
//		dto.setFileToken(token);
//		dto.setTransactionName(screenName);
//		dto.setFileType(new SelectValue(ReferenceTable.INVESTIGATION_DOCUMENT_TYPE_KEY,"Investigation Report"));
//		dto.setDeletedFlag("N");
		
		UploadedDocumentsDTO dto = new UploadedDocumentsDTO(); 
		dto.setFileName(fileName);
		dto.setToken(token);
		dto.setFileType(SHAConstants.CLAIM_VERIFICATION_REPORT);
		dto.setTransacName(screenName);
		dto.setDeletedFlag("N");
		dto.setUsername(searchUploadInvTableDto.getUsername());
		
		List<UploadedDocumentsDTO> uploadListDto = new ArrayList<UploadedDocumentsDTO>();
		
		uploadListDto.addAll(view.getUploadedDocDtoList());     
		uploadListDto.add(dto);
		searchUploadInvTableDto.setUploadedFileTableDto(uploadListDto);
		
		view.updateTableValues(getUploadDocumentList(uploadListDto,searchUploadInvTableDto.getRodKey()));
		
/*		reimbursementService.updateDocumentDetails(dto);

//		Investigation investigation = investigationService
//				.getByInvestigationKey(investigationKey);
		
		AssignedInvestigatiorDetails assignedInvestigator = investigationService.getAssignedInvestigByKey(assignedInvestigationKey);
		
		Investigation investigationObj = assignedInvestigator.getInvestigation();
		
		reimbursementService.updateClaimDocumentDetails(dto, investigationObj);
		
		if (investigationObj != null) {
			investigationObj.setFileName(fileName);
			investigationObj.setToken(token);
			MastersValue fileTypeId = new MastersValue();
			fileTypeId.setKey(ReferenceTable.INVESTIGATION_DOCUMENT_TYPE_KEY);
			investigationObj.setFileTypeId(fileTypeId);
			//TODO Becoz this status being updated only in child table multiple childs avaible after assigned investigation
//			investigationService.updateIvestigation(investigationObj, SHAConstants.UPLOAD_INVESTIGATION,null);
			
			investigationService.updateAssignedIvestigation(assignedInvestigator,SHAConstants.UPLOAD_INVESTIGATION,null);
			view.updateTableValues(assignedInvestigationKey);

		}*/
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
	
	
//	private void updateUploadInvDocToDB(MultipleUploadDocumentDTO dto){
		
		
//		reimbursementService.updateDocumentDetails(dto);
//		
//		AssignedInvestigatiorDetails assignedInvestigator = investigationService.getAssignedInvestigByKey(assignedInvestigationKey);
//		
//		Investigation investigationObj = assignedInvestigator.getInvestigation();
//		
//		reimbursementService.updateClaimDocumentDetails(dto, investigationObj);  //  this was internally Called in the below service.

	private List<UploadedDocumentsDTO> updateUploadInvDocToDB(List<UploadedDocumentsDTO> uploadedFileTableDto,Long assignedKey,String userName){
		
		if(uploadedFileTableDto != null && !uploadedFileTableDto.isEmpty()){
			MultipleUploadDocumentDTO docDto;
			for (UploadedDocumentsDTO uploadedDocumentsDTO : uploadedFileTableDto) {
				if(uploadedDocumentsDTO.getKey() == null){
				docDto = new MultipleUploadDocumentDTO();
				docDto.setFileName(uploadedDocumentsDTO.getFileName());
				docDto.setTransactionKey(assignedKey);
				docDto.setFileToken(uploadedDocumentsDTO.getToken());
				docDto.setTransactionName(uploadedDocumentsDTO.getTransacName());
				docDto.setFileType(new SelectValue(ReferenceTable.CLAIM_VERIFICATION_REPORT_DOCUMENT_TYPE_KEY,SHAConstants.CLAIM_VERIFICATION_REPORT));
				docDto.setDeletedFlag(uploadedDocumentsDTO.getDeletedFlag());
				docDto.setUsername(userName);
//				docDto.setKey(uploadedDocumentsDTO.getKey());
				docDto = reimbursementService.updateInvDocDetails(docDto);
				uploadedDocumentsDTO.setKey(docDto.getKey());
				}
			}
		}
	
		return uploadedFileTableDto;
	}
	
	
	public void saveEvent(
			@Observes @CDIEvent(SAVE_EVENT) final ParameterDTO parameters) {

		Long investigationKey = (Long) parameters.getPrimaryParameter();

		SearchUploadInvesticationTableDTO tableDto = (SearchUploadInvesticationTableDTO) parameters
				.getSecondaryParameter(0,
						SearchUploadInvesticationTableDTO.class);
				
		List<UploadedDocumentsDTO> uploadedFileTableDto = tableDto.getUploadedFileTableDto();
		
		uploadedFileTableDto = updateUploadInvDocToDB(uploadedFileTableDto,tableDto.getInvestigationAssignedKey(),tableDto.getUsername());
		
		List<UploadedDocumentsDTO> deletedFileListDto = tableDto.getUploadedNDeletedFileListDto();
		
		updateDeletedDocDetails(deletedFileListDto,tableDto.getUsername());
		
		investigationService.saveAssignedIvestigation(tableDto);
		view.showSaveResult();
		view.updateTableValues(uploadedFileTableDto);
	}
	
	public void submitEvent(
			@Observes @CDIEvent(SUBMIT_EVENT) final ParameterDTO parameters) {

		Long investigationAssignedKey = (Long) parameters.getPrimaryParameter();
		
		Long investigationKey = (Long) parameters.getSecondaryParameters()[0];

		SearchUploadInvesticationTableDTO tableDto = (SearchUploadInvesticationTableDTO) parameters
											.getSecondaryParameter(1, SearchUploadInvesticationTableDTO.class);		

		AssignedInvestigatiorDetails assingnedInvestigObj = investigationService.getAssignedInvestigByKey(investigationAssignedKey);
		
		List<UploadedDocumentsDTO> uploadedFileTableDto = tableDto.getUploadedFileTableDto();
		
		uploadedFileTableDto = updateUploadInvDocToDB(uploadedFileTableDto,tableDto.getInvestigationAssignedKey(),tableDto.getUsername());

		List<UploadedDocumentsDTO> deletedFileListDto = tableDto.getUploadedNDeletedFileListDto();
		
		updateDeletedDocDetails(deletedFileListDto,tableDto.getUsername());
		
		Stage stageObj = preauthService.getStageByKey(ReferenceTable.INVESTIGATION_STAGE);
		Status statusObj = preauthService.getStatusByKey(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED);
		assingnedInvestigObj.setCompletionRemarks(tableDto.getInvestigationCompletionRemarks());
		assingnedInvestigObj.setStage(stageObj);
		assingnedInvestigObj.setStatus(statusObj);
		
		Investigation investigation = investigationService.getByInvestigationKey(investigationKey);
		
			
		investigationService.updateAssignedIvestigation(assingnedInvestigObj,SHAConstants.UPLOAD_INVESTIGATION,tableDto.getUsername(),investigation);
		
			Map<String, Object> wrkFlowMap = (Map<String, Object>) tableDto.getDbOutArray();
			wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_KEY, investigationAssignedKey);
//			investigationService.updateReimbursementStatus(tableDto.getRodKey(), status, stage, tableDto.getUsername());
			investigationService.submitUploadInvestigationTaskToDB(investigation,tableDto);
			view.result();
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	private void updateDeletedDocDetails(List<UploadedDocumentsDTO> deletedFileListDto,String userName){
		
		if(deletedFileListDto != null && !deletedFileListDto.isEmpty()){
			MultipleUploadDocumentDTO docDto;
			for (UploadedDocumentsDTO uploadedDocumentsDTO : deletedFileListDto) {
				if(uploadedDocumentsDTO.getKey() != null){
					docDto = new MultipleUploadDocumentDTO();
					docDto.setKey(uploadedDocumentsDTO.getKey());
					docDto.setFileToken(uploadedDocumentsDTO.getToken());
					docDto.setDeletedFlag("Y");
					docDto.setUsername(userName);
					reimbursementService.updateInvDocDetails(docDto);
				}
			}
		}
	}
	
	public void setUpSubCategoryValues(
			@Observes @CDIEvent(PROCESS_UPLOAD_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES) final ParameterDTO parameters) {
		Long categoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox subCategory = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSubCatValues(categoryKey);
		view.setsubCategoryValues(selectValueContainer,subCategory,sel);
	}
	
	public void setUpsourceValues(
			@Observes @CDIEvent(PROCESS_UPLOAD_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES) final ParameterDTO parameters) {
		Long subCategoryKey = (Long) parameters.getPrimaryParameter();
		GComboBox source = (GComboBox) parameters.getSecondaryParameter(0, GComboBox.class);
		SelectValue sel = (SelectValue) parameters.getSecondaryParameter(1, SelectValue.class);
		BeanItemContainer<SelectValue> selectValueContainer = masterService.getRRCSourceValues(subCategoryKey);
		view.setsourceValues(selectValueContainer,source,sel);
	}

}
