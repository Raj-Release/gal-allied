/**
 * 
 */
package com.shaic.claim.reimbursement.billing.benefits.wizard.pages;

import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.claim.reimbursement.billing.benefits.wizard.forms.ProcessClaimRequestBenefitsDataExtractionPage;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.vaadin.ui.Component;

/**
 * @author ntv.vijayar
 *
 */
public class ProcessClaimBenefitsDataExtractionViewImpl extends AbstractMVPView 
implements ProcessClaimRequestBenefitsDataExtractionView
{

	@Inject
	private ProcessClaimRequestBenefitsDataExtractionPage documentDetailsPage;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	private ReceiptOfDocumentsDTO bean;

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(ReceiptOfDocumentsDTO bean) {
		//documentDetailsPage.init(bean);

	}

	@Override
	public Component getContent() {
		Component comp =  documentDetailsPage.getContent();
		//setCompositionRoot(comp);
		fireViewEvent(ProcessClaimRequestBenefitsDataExtractionPresenter.CLAIM_REQUEST_BENEFITS_SETUP_DROPDOWN_VALUES, bean);
		return comp;

	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		//documentDetailsPage.setValuesFromDTO();
	}

	@Override
	public boolean onAdvance() {		
		documentDetailsPage.setTableValuesToDTO();
		return documentDetailsPage.validatePage();
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		
		return true;
	}

	@Override
	public boolean onSave() {
		documentDetailsPage.setTableValuesToDTO();
		return documentDetailsPage.validatePage();
	}

	@Override
	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard) {
		// TODO Auto-generated method stub
		localize(null);
		this.bean = bean;
		documentDetailsPage.init(bean,wizard);
		//resetPage();
		//documentDetailsPage.resetPage();
		
	}
	
	@Override
	public String getCaption() {
		//return "Document Details";
		return strCaptionString;
	}
	
	
	
	@Override
	public void setUpDropDownValues(Map<String, Object> referenceDataMap) 
	{
		documentDetailsPage.loadContainerDataSources(referenceDataMap);
	}

	
	protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
             strCaptionString = tb.getText(textBundlePrefixString() + "dataextration");//String.valueOf(propertyId).toLowerCase());
        }
    
	

	private String textBundlePrefixString()
	{
		return "process-claim-benefits-";
	}


	
	@Override
	public void resetPage() {
		
		documentDetailsPage.resetPage();
		
	}

	@Override
	public void generateFieldsBasedOnHospitalCashBenefits(Boolean selectedValue) {
		documentDetailsPage.generateFieldsBasedOnHospitalCashBenefits(selectedValue);
		
	}

	@Override
	public void generateFieldsBasedOnPatientCareBenefits(Boolean selectedValue) {
		documentDetailsPage.generateFieldsBasedOnPatientCareBenefits(selectedValue);
	}

	@Override
	public void setUpHospitalCashValues(List<Object> benefitList) {
		documentDetailsPage.setHospitalCashValues(benefitList);
	}

	@Override
	public void setUpPatientCareValues(List<Object> benefitList) {
		documentDetailsPage.setPatientCareValues(benefitList);
	}

	@Override
	public void setBenefitsData(List<AddOnBenefitsDTO> benefitsDTO) {
		// TODO Auto-generated method stub
		documentDetailsPage.setBenefitsData(benefitsDTO);
	}

}
