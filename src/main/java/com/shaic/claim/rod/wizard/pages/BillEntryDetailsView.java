/**
 * 
 */
package com.shaic.claim.rod.wizard.pages;

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.vijayar
 *
 */
public interface BillEntryDetailsView extends GMVPView, WizardStep<ReceiptOfDocumentsDTO> {
	
	
	void setUpDropDownValues (Map<String , Object> referenceMap);
	void setUpCategoryValues (BeanItemContainer<SelectValue> categoryValues);
	void setBillEntryFinalStatus(UploadDocumentDTO dto);
	
	void generateFieldsBasedOnHospitalCashBenefits(Boolean selectedValue);
	void generateFieldsBasedOnPatientCareBenefits(Boolean selectedValue);
	
	void setUpHospitalCashValues(List<Object> benefitList);
	
	void setUpPatientCareValues(List<Object> benefitList);
	//void loadUploadedDocsTableValues(List<UploadDocumentDTO> uploadDocList);
	//void deleteUploadDocumentDetails(UploadDocumentDTO dto);
	void enableOrDisableBtn(UploadDocumentDTO uploadDTO);
	void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO);
	void setClearReferenceData();
	void setUpdateOtherClaimsDetails(
			List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails);
	void generateFieldsBasedOnHospitalCashProduct(Boolean selectedValue);
}

