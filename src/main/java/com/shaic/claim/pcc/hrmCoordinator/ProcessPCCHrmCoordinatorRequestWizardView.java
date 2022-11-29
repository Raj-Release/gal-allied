package com.shaic.claim.pcc.hrmCoordinator;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pcc.dto.PCCUploadDocumentsDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ProcessPCCHrmCoordinatorRequestWizardView extends GMVPView {
	
	void generateFieldsBasedOnNegotiation(Boolean isChecked);
	
	public void buildSuccessLayout();
	
	void editPCCUploadDocumentDetails(PCCUploadDocumentsDTO dto);
	
	void deletePCCUploadedDocumentDetails(PCCUploadDocumentsDTO dto);
	
	public List<PCCUploadDocumentsDTO> getUploadedDocDtoList();	

	public void updateTableValues(List<PCCUploadDocumentsDTO> uploadedDocList);
	
	void addUserDetails(BeanItemContainer<SelectValue> userDetailsContainer);
}
