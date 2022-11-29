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
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;

/**
 * @author ntv.vijayar
 *
 */
public interface DocumentDetailsView extends GMVPView,WizardStep<ReceiptOfDocumentsDTO> {
	
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
	 void resetPage();
	void validateLumpSumAmount(Boolean isValid, String classificationType,CheckBox chkBox);
	void setCoverList(BeanItemContainer<SelectValue> coverContainer);
	void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer);
	void setClearMapObject();
		
	 
}

