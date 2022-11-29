package com.shaic.paclaim.rod.enterbilldetails.search;

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.ui.CheckBox;

public interface PABillEntryDocumentDetailsView extends GMVPView, WizardStep<ReceiptOfDocumentsDTO>{

	
	void init(ReceiptOfDocumentsDTO bean, GWizard wizard);
	/* void setTableValuesToDTO();
	 void setTableValues();*/
	 void setUpDropDownValues(Map<String,Object> referenceDataMap);
	 void setDocumentCheckListTableValues(List<DocumentCheckListDTO> documentCheckList);
	 void saveReconsiderRequestTableItems(ReconsiderRODRequestTableDTO dto,List<UploadDocumentDTO> uploadDocsDTO);
	 void setDocumentDetailsDTOForValidation(List<DocumentDetailsDTO> documentDetailsDTO);
	 void returnPreviousPage();
	 void setDocStatusIfReplyReceivedForQuery(RODQueryDetailsDTO rodQueryDetails);
	void validateBillClassificationAgainstBillEntry(List<Long> categoryList, Long categoryKey,String chkBox);
	 void validateBenefitRepeat(Boolean isValid, Boolean chkBoxValue);
	 void validateCoversRepeat(Boolean isValid,String Name);
	 void validateBenefitRepeat(Boolean isValid, Boolean chkBoxValue,CheckBox chkBox,String benefitValue);
	 void resetParticularsBasedOnBenefit(SelectValue value);
}
