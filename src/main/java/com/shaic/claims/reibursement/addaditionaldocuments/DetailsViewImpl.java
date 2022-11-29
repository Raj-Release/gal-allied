/**
 * 
 */
package com.shaic.claims.reibursement.addaditionaldocuments;

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

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Component;

/**
 * @author ntv.srikanthp
 *
 */
public class DetailsViewImpl extends AbstractMVPView 
implements DetailsView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Inject
	private BillEntryDetailsPageForAddAdditionalDetails billEntryDetailsPage;


	@Inject
	private TextBundle tb;
	
	@Inject
	private ReceiptOfDocumentsDTO rodDto;
	
	private String strCaptionString;
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(ReceiptOfDocumentsDTO bean) {
		localize(null);
		this.rodDto = bean;
		billEntryDetailsPage.init(bean);
		// TODO Auto-generated method stub
	}

	@Override
	public Component getContent() {
		Component comp = billEntryDetailsPage.getContent();
		
		billEntryDetailsPage.wizardButtonEnableOrDisable();
		// TODO Auto-generated method stub
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
	//	billEntryDetailsPage.setTableValuesToDTO();
		billEntryDetailsPage.setTableValuesToDTO();
		return billEntryDetailsPage.validatePage();
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		//billEntryDetailsPage.setTableValuesToDTO();
		return billEntryDetailsPage.isValid();
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setUpDropDownValues(Map<String, Object> referenceDataMap) {
			billEntryDetailsPage.setFileTypeValues(referenceDataMap);
	}
	
	protected void localize(
            @SuppressWarnings("cdi-observer") @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
             strCaptionString = tb.getText(textBundlePrefixString() + "billentry");//String.valueOf(propertyId).toLowerCase());
        }
    
	@Override
	public String getCaption() {
		//return "Document Details";
		return strCaptionString;
	}
	

	private String textBundlePrefixString()
	{
		return "bill-entry-";
	}

	@Override
	public void setUpCategoryValues(
			BeanItemContainer<SelectValue> categoryValues) {
		billEntryDetailsPage.setUpCategoryValues(categoryValues);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBillEntryFinalStatus(UploadDocumentDTO status) {
		billEntryDetailsPage.setBillEntryFinalStatus(status);
	}

	@Override
	public void generateFieldsBasedOnHospitalCashBenefits(Boolean selectedValue) {
		billEntryDetailsPage.generateFieldsBasedOnHospitalCashBenefits(selectedValue);
		
	}

	@Override
	public void generateFieldsBasedOnPatientCareBenefits(Boolean selectedValue) {
		billEntryDetailsPage.generateFieldsBasedOnPatientCareBenefits(selectedValue);
		
	}

	@Override
	public void setUpHospitalCashValues(List<Object> benefitList) {
		
		billEntryDetailsPage.setHospitalCashValues(benefitList);
	}

	@Override
	public void setUpPatientCareValues(List<Object> benefitList) {
		billEntryDetailsPage.setPatientCareValues(benefitList);
		
	}

	@Override
	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		// TODO Auto-generated method stub
		billEntryDetailsPage.setUploadDTOForBillEntry(uploadDTO);
		
	}


	@Override
	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard) {
		// TODO Auto-generated method stub
		localize(null);
		//this.bean = bean;
		billEntryDetailsPage.init(bean,wizard);
	}
	/*@Override
	public void loadUploadedDocsTableValues(
			List<UploadDocumentDTO> uploadDocList) {
		
		this.billEntryDetailsPage.loadUploadedDocsTableValues(uploadDocList);
		
	}
	@Override
	public void deleteUploadDocumentDetails(UploadDocumentDTO dto) {
		billEntryDetailsPage.deleteUploadDocumentDetails(dto);
	}*/

}
