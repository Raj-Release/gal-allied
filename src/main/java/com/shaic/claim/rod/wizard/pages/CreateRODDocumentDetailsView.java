/**
 * 
 */
package com.shaic.claim.rod.wizard.pages;

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.forms.AddBanksDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.TextField;

/**
 * @author ntv.vijayar
 *
 */
public interface CreateRODDocumentDetailsView  extends GMVPView, WizardStep<ReceiptOfDocumentsDTO> {
	
	 void init(ReceiptOfDocumentsDTO bean, GWizard wizard,String presenterString);
	/* void setTableValuesToDTO();
	 void setTableValues();*/
	 void setUpDropDownValues(Map<String,Object> referenceDataMap);
	 void setDocumentCheckListTableValues(List<DocumentCheckListDTO> documentCheckList);
	 void saveReconsiderRequestTableItems(ReconsiderRODRequestTableDTO dto,List<UploadDocumentDTO> uploadDocsDTO);
	 void setDocumentDetailsDTOForValidation(List<DocumentDetailsDTO> documentDetailsDTO);
	 void returnPreviousPage();
	void setHospitalDetails(UpdateHospitalDetailsDTO changeHospital);
	
	void setUpPaymentDetails(ReceiptOfDocumentsDTO rodDTO);
	
	
	void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto);
	void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto);
	void setUpBankDetails(ReceiptOfDocumentsDTO dto);
	void setDocStatusIfReplyReceivedForQuery(RODQueryDetailsDTO rodQueryDetails);
	void setComparisonResult(String comparisonResult);
	void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO);
	void validateLumpSumAmount(Boolean isValid, String classificationType,CheckBox chkBox);

	//void setUpPaymentDetailsByQueryRecStatus(Reimbursement reimObj);	
	void setCoverList(BeanItemContainer<SelectValue> coverContainer);
	void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer);
	void validateHospitalizationRepeat(Boolean isValid);
	void setClearMapObject();
	//void validateClaimedAmount();
	void setOptDocVerifiedValue();
	void resetClaimedAmntValue(TextField textField);
	void setUpPayableDetails(String payableAt);
	 
}
