/**
 * 
 */
package com.shaic.claim.reimbursement.billing.benefits.wizard.pages;

import java.util.List;
import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;

/**
 * @author ntv.vijayar
 *
 */
public interface ProcessClaimRequestBenefitsDataExtractionView extends GMVPView, WizardStep<ReceiptOfDocumentsDTO>  {
	
	
	void generateFieldsBasedOnHospitalCashBenefits(Boolean selectedValue);
	void generateFieldsBasedOnPatientCareBenefits(Boolean selectedValue);
	
	void setUpHospitalCashValues(List<Object> benefitList);
	
	void setUpPatientCareValues(List<Object> benefitList);
	
	void setUpDropDownValues(Map<String, Object> referenceDataMap);
	
	 void init(ReceiptOfDocumentsDTO bean, GWizard wizard);
	 
	 void setBenefitsData(List<AddOnBenefitsDTO> benefitsDTO);
	 
	 void resetPage();

}
