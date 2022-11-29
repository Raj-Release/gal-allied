package com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search;

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface PAAcknowledgementDocumentDetailsView extends GMVPView,WizardStep<ReceiptOfDocumentsDTO> {

	 void init(ReceiptOfDocumentsDTO bean, GWizard wizard);
	/* void setTableValuesToDTO();
	 void setTableValues();*/
	 void setUpDropDownValues(Map<String,Object> referenceDataMap);
	 void setDocumentCheckListTableValues(List<DocumentCheckListDTO> documentCheckList);
	 void returnPreviousPage();
	 void disableReconsiderRequestTableItems(ReconsiderRODRequestTableDTO dto);
	
	 void setDocStatusIfReplyReceivedForQuery(RODQueryDetailsDTO rodQueryDetails);
	 void resetDocStatusIfReplyReceivedForQuery();
	 void validateHospitalizationRepeat(Boolean isValid);
	 void validateBenefitRepeat(Boolean isValid, Boolean chkBoxValue, String value);
	 void validateCoversRepeat(Boolean isValid,String Name);
	 void resetPage();
	 void resetReconsiderRequestTableItems(ReconsiderRODRequestTableDTO dto);
	void resetParticularsBasedOnBenefit(
			BeanItemContainer<SelectValue> particulars, String value);
	void resetParticularsBasedOnAddOnCover(SelectValue value,Boolean deleteValue);
	void setDefaultParticularValues(
			BeanItemContainer<SelectValue> documentCheckListValuesContainer);
	void setNextValue();
}
