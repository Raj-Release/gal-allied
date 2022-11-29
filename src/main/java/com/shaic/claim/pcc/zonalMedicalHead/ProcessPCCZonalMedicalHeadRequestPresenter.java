package com.shaic.claim.pcc.zonalMedicalHead;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.persistence.Query;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.pcc.dto.PCCUploadDocumentsDTO;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.wizard.ProcessPCCCoOrdinatorRequestWizard;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.reimbursement.uploadrodreports.UploadedDocumentsDTO;

@ViewInterface(ProcessPCCZonalMedicalHeadRequestWizardView.class)
public class ProcessPCCZonalMedicalHeadRequestPresenter extends AbstractMVPPresenter<ProcessPCCZonalMedicalHeadRequestWizardView> {
	
	@EJB
	private ZonalMedicalHeadRequestService requestService;
	
	@EJB
	private SearchProcessPCCRequestService processPCCRequestService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private IntimationService intimationService;
	
	
	public static final String SUBMIT_ZONAL_MEDICAL_HEAD_DETAILS = "submit_zonal_medical_head_details";
	
    public static final String ZONAL_MEDICAL_HEAD_GENERATE_RESPONSE_LAYOUT = "zonal_medical_head_generate_response_layout";
	
	public static final String ZONAL_MEDICAL_HEAD_GENERATE_ASSIGN_LAYOUT = "zonal_medical_head_generate_assign_layout";
		
	public static final String ZONAL_MEDICAL_HEAD_GENERATE_NEGOTIATION_APPLICABLE = "zonal_medical_head_generate_negotiation_applicable";
	
	public static final String PCC_UPLOAD_EVENT = "PCC Upload document button";
	
	public static final String PCC_EDIT_UPLOAD_EVENT = "PCC edit upload button";
	
	public static final String PCC_DELETE_UPLOAD_EVENT = "PCC delete upload button";
	
	public static final String PCCZMH_GENERATE_USER_DETAILS = "pccZMH_generate_user_details";	
	
	public void submitZonalMedicalHead(@Observes @CDIEvent(SUBMIT_ZONAL_MEDICAL_HEAD_DETAILS) final ParameterDTO parameters) {	

		PccDTO pccDTO = (PccDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		//upload Document
		List<PCCUploadDocumentsDTO> uploadedFileTableDto = pccDTO.getUploadedFileTableDto();
		
		uploadedFileTableDto = updateUploadPCCDocToDB(uploadedFileTableDto,pccDTO.getPccKey(),userName);

		List<PCCUploadDocumentsDTO> deletedFileListDto = pccDTO.getUploadedNDeletedFileListDto();
		updateDeletedDocDetails(deletedFileListDto,userName);
		
		requestService.submitZonalMedicalHead(pccDTO,userName);
		view.buildSuccessLayout();		
	}
	
   public void generateapproveLayout(@Observes @CDIEvent(ZONAL_MEDICAL_HEAD_GENERATE_RESPONSE_LAYOUT) final ParameterDTO parameters) {	
		
		view.generateResponseLayout();		
	}

	public void generateQuerryLayout(@Observes @CDIEvent(ZONAL_MEDICAL_HEAD_GENERATE_ASSIGN_LAYOUT) final ParameterDTO parameters) {	

		view.generateAssignLayout();		
	}
	
	public void generateFieldsBasedOnNegotiationApplicable(@Observes @CDIEvent(ZONAL_MEDICAL_HEAD_GENERATE_NEGOTIATION_APPLICABLE) final ParameterDTO parameters)
	{
		Boolean isCked = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnNegotiation(isCked);
	}
	
	public void addUserDetails(@Observes @CDIEvent(PCCZMH_GENERATE_USER_DETAILS) final ParameterDTO parameters) {	

		String roleCode = (String) parameters.getPrimaryParameter();
		Long cpuCode = (Long)parameters.getSecondaryParameter(0, Long.class);
		view.addUserDetails(processPCCRequestService.getPCCUserNamesBasedOnCPUCode(roleCode,cpuCode));	
		//view.addUserDetails(processPCCRequestService.getPCCUserNames(roleCode));
	}
	
	public void editPCCUploadedDocumentDetails(@Observes @CDIEvent(PCC_EDIT_UPLOAD_EVENT) final ParameterDTO parameters) {
		PCCUploadDocumentsDTO uploadDTO = (PCCUploadDocumentsDTO)parameters.getPrimaryParameter();
		view.editPCCUploadDocumentDetails(uploadDTO);
	}
	
	public void deletePCCUploadedDocumentDetails(@Observes @CDIEvent(PCC_DELETE_UPLOAD_EVENT) final ParameterDTO parameters) {
		PCCUploadDocumentsDTO uploadDTO = (PCCUploadDocumentsDTO)parameters.getPrimaryParameter();
		view.deletePCCUploadedDocumentDetails(uploadDTO);
	}
	public void setUploadDocument(
			@Observes @CDIEvent(PCC_UPLOAD_EVENT) final ParameterDTO parameters) {

		//This is actual pcc key
		Long transacKey = (Long) parameters.getPrimaryParameter();
		
		String fileName = (String) parameters.getSecondaryParameter(0,String.class);
		
		String token = (String)parameters.getSecondaryParameter(1, String.class);
		
		String screenName = (String)parameters.getSecondaryParameter(2, String.class);
		
		PccDTO pccDto = (PccDTO) parameters.getSecondaryParameter(3, PccDTO.class);
		
		String fileUploadRemarks=(String)parameters.getSecondaryParameter(4, String.class);
		
		String userName=(String)parameters.getSecondaryParameter(5, String.class);
		
		
		
		PCCUploadDocumentsDTO dto = new PCCUploadDocumentsDTO(); 
		dto.setFileName(fileName);
		dto.setFileUploadRemarks(fileUploadRemarks);
		dto.setToken(token);
		dto.setTransactionKey(transacKey);
		dto.setFileType(SHAConstants.POST_CASHLESS_CELL);
		dto.setTransacName(screenName);
		dto.setDeletedFlag("N");
		dto.setUsername(userName);
		
		List<PCCUploadDocumentsDTO> uploadListDto = new ArrayList<PCCUploadDocumentsDTO>();
		
		uploadListDto.addAll(view.getUploadedDocDtoList());     
		uploadListDto.add(dto);
		pccDto.setUploadedFileTableDto(uploadListDto);
		System.out.println(String.format(" uploadListDto ***********[%S]", uploadListDto.size()));
		System.out.println(String.format("PCC KEY in upload event presenter ***********[%S]", pccDto.getPccKey()));
		view.updateTableValues(getUploadDocumentList(uploadListDto,pccDto.getPccKey()));
		
	}
	
public List<PCCUploadDocumentsDTO> getUploadDocumentList(List<PCCUploadDocumentsDTO> updateDocumentDetails, Long pccKey){
		
		PCCRequest pccRequestObj = requestService.getPCCRequestByKey(pccKey);
		for (PCCUploadDocumentsDTO documentDto : updateDocumentDetails) {
			if(documentDto.getFileName() != null){
				if(null != pccRequestObj){
					documentDto.setIntimationNumber(pccRequestObj.getIntimationNo() != null ? pccRequestObj.getIntimationNo() : "");
				}
			}
		}
		return updateDocumentDetails;
	}
private List<PCCUploadDocumentsDTO> updateUploadPCCDocToDB(List<PCCUploadDocumentsDTO> uploadedFileTableDto,Long pccKey,String userName){
	
	if(uploadedFileTableDto != null && !uploadedFileTableDto.isEmpty()){
		MultipleUploadDocumentDTO docDto;
		for (PCCUploadDocumentsDTO uploadedDocumentsDTO : uploadedFileTableDto) {
			if(uploadedDocumentsDTO.getKey() == null){
			docDto = new MultipleUploadDocumentDTO();
			docDto.setFileName(uploadedDocumentsDTO.getFileName());
			docDto.setTransactionKey(pccKey);
			docDto.setFileToken(uploadedDocumentsDTO.getToken());
			docDto.setTransactionName(uploadedDocumentsDTO.getTransacName());
			docDto.setPccFileType(SHAConstants.POST_CASHLESS_CELL);
			docDto.setDeletedFlag(uploadedDocumentsDTO.getDeletedFlag());
			docDto.setUsername(userName);
			docDto.setFileUploadRemarks(uploadedDocumentsDTO.getFileUploadRemarks());
			docDto = reimbursementService.updatePCCDocDetails(docDto);
			uploadedDocumentsDTO.setKey(docDto.getKey());
			}
		}
	}

	return uploadedFileTableDto;
}


private void updateDeletedDocDetails(List<PCCUploadDocumentsDTO> deletedFileListDto,String userName){
	
	if(deletedFileListDto != null && !deletedFileListDto.isEmpty()){
		MultipleUploadDocumentDTO docDto;
		for (PCCUploadDocumentsDTO uploadedDocumentsDTO : deletedFileListDto) {
			if(uploadedDocumentsDTO.getKey() != null){
				docDto = new MultipleUploadDocumentDTO();
				docDto.setKey(uploadedDocumentsDTO.getKey());
				docDto.setFileToken(uploadedDocumentsDTO.getToken());
				docDto.setDeletedFlag("Y");
				docDto.setUsername(userName);
				reimbursementService.updatePCCDocDetails(docDto);
			}
		}
	}
}

	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
