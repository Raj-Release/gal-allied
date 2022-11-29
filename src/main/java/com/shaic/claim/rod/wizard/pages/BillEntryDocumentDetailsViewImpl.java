/**
 * 
 */
package com.shaic.claim.rod.wizard.pages;

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
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.forms.BillEntryDocumentDetailsPage;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;

/**
 * @author ntv.vijayar
 *
 */
public class BillEntryDocumentDetailsViewImpl extends AbstractMVPView 
		implements BillEntryDocumentDetailsView
{
	
	@Inject
	private BillEntryDocumentDetailsPage documentDetailsPage;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	@Inject
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
		fireViewEvent(BillEntryDocumentDetailsPresenter.BILL_ENTRY_SETUP_DROPDOWN_VALUES, bean);
		
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
		resetFlds();
		documentDetailsPage.init(bean,wizard);
		
	}
	
	private void resetFlds()
	{
		documentDetailsPage.resetQueryReplyReceived();
		documentDetailsPage.reconsiderationMap.clear();
		documentDetailsPage.hospitalizationClaimedAmt = "";
		documentDetailsPage.preHospitalizationAmt= "";
		documentDetailsPage.postHospitalizationAmt = "";
		documentDetailsPage.resetLumpsumValidationFlag();
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
             strCaptionString = tb.getText(textBundlePrefixString() + "documentdetails");
        }
    
	

	private String textBundlePrefixString()
	{
		return "bill-entry-";
	}

	@Override
	public void returnPreviousPage() {
		onBack();
		
	}

	@Override
	public void setDocumentCheckListTableValues(List<DocumentCheckListDTO> documentCheckList) {
		
	//	documentDetailsPage.setDocumentCheckList(documentCheckList);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveReconsiderRequestTableItems(ReconsiderRODRequestTableDTO dto,List<UploadDocumentDTO> uploadDocsDTO) {
		// TODO Auto-generated method stub
		documentDetailsPage.saveReconsideRequestTableValue(dto,uploadDocsDTO);
		
	}

	@Override
	public void setDocumentDetailsDTOForValidation(
			List<DocumentDetailsDTO> documentDetailsDTO) {
		documentDetailsPage.setDocumentDetailsListForValidation(documentDetailsDTO);
		
	}

	@Override
	public void setDocStatusIfReplyReceivedForQuery(
			RODQueryDetailsDTO rodQueryDetails) {
		documentDetailsPage.setDocStatusIfReplyReceivedForQuery(rodQueryDetails);
	}

	@Override
	public void validateBillClassificationAgainstBillEntry(
			List<Long> categoryList,Long classificationKey,String chkBox) {
		documentDetailsPage.validateBillClassificationAgainstBillEntry(categoryList,classificationKey,chkBox);
		
	}

	@Override
	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
		documentDetailsPage.setCoverList(coverContainer);
		
	}

	@Override
	public void setSubCoverContainer(
			BeanItemContainer<SelectValue> subCoverContainer) {
		documentDetailsPage.setSubCoverList(subCoverContainer);
		
	}
	
	@Override
	public void validateLumpSumAmount(Boolean isValid,String classificationType,CheckBox checkBox) {
		// TODO Auto-generated method stub
		documentDetailsPage.validateLumpSumAmount(isValid,classificationType,checkBox);
		
	}

	@Override
	public void validateHospitalizationRepeat(Boolean isValid) {
		documentDetailsPage.validateHospitalizationRepeat(isValid);
		
	}
	
	@Override
	public void setClearReferenceData(){
		documentDetailsPage.setClearReferenceData();
	}

	


	
}
