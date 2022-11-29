package com.shaic.paclaim.rod.enterbilldetails.search;

import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Component;

public class PABillEntryDetailsViewImpl extends AbstractMVPView implements PABillEntryDetailsView{

	@Inject
	private PABillEntryDetailsPage billEntryDetailsPage;


	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	private  ReceiptOfDocumentsDTO bean;
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(ReceiptOfDocumentsDTO bean) {
		localize(null);
		this.bean = bean;
		billEntryDetailsPage.init(bean);
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getContent() {
		Component comp = billEntryDetailsPage.getContent();
		fireViewEvent(PABillEntryDetailsPresenter.BILL_ENTRY_DETAILS_PAGE_UPLOADED_DOC_SETUP_DROPDOWN_VALUES, bean);
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
		//return true;
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		//billEntryDetailsPage.setTableValuesToDTO();
		//return billEntryDetailsPage.commitBinderOnNavigation();
		//billEntryDetailsPage.initBinder();
		return true;
		//return billEntryDetailsPage.isValid();
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		billEntryDetailsPage.setTableValuesToDTO();
		return false;
	}

	@Override
	public void setUpDropDownValues(Map<String, Object> referenceDataMap) {
			billEntryDetailsPage.setFileTypeValues(referenceDataMap);
	}
	
	protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
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
	public void enableOrDisableBtn(UploadDocumentDTO uploadDTO) {
		billEntryDetailsPage.enableOrDisableDtn(uploadDTO);	
	}

	@Override
	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		billEntryDetailsPage.setUploadDTOForBillEntry(uploadDTO);
		
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
