package com.shaic.claim.pcc.zonalMedicalHead;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pcc.dto.PCCUploadDocumentsDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ProcessPCCZonalMedicalHeadRequestWizardView extends GMVPView {
	    
	void buildSuccessLayout();
	
	void generateResponseLayout();
	
	void generateAssignLayout();
	
	void generateFieldsBasedOnNegotiation(Boolean isChecked);
	
	void editPCCUploadDocumentDetails(PCCUploadDocumentsDTO dto);
	
	void deletePCCUploadedDocumentDetails(PCCUploadDocumentsDTO dto);
	
	public List<PCCUploadDocumentsDTO> getUploadedDocDtoList();	

	public void updateTableValues(List<PCCUploadDocumentsDTO> uploadedDocList);
	
	void addUserDetails(BeanItemContainer<SelectValue> userDetailsContainer);
	
}
