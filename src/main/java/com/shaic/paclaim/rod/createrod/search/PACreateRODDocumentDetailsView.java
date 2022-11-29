package com.shaic.paclaim.rod.createrod.search;

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
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;

public interface PACreateRODDocumentDetailsView extends GMVPView, WizardStep<ReceiptOfDocumentsDTO>{

	
	 void init(ReceiptOfDocumentsDTO bean, GWizard wizard);
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
	void setDocStatusIfReplyReceivedForQuery(RODQueryDetailsDTO rodQueryDetails);
	void setComparisonResult(String comparisonResult);
	void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO);
	//void setUpPaymentDetailsByQueryRecStatus(Reimbursement reimObj);	
	 void validateBenefitRepeat(Boolean isValid, Boolean chkBoxValue,CheckBox chkBox,String benefitValue);
	 void validateCoversRepeat(Boolean isValid,String Name);
	 void resetParticularsBasedOnBenefit(BeanItemContainer<SelectValue> particulars,String values);
	void resetParticularsBasedOnAddOnCover(SelectValue value);
	
	void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto);
}
 