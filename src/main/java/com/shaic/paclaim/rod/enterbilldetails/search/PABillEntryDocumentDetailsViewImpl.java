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
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;

public class PABillEntryDocumentDetailsViewImpl extends AbstractMVPView implements PABillEntryDocumentDetailsView{

	@Inject
	private PABillEntryDocumentDetailsPage documentDetailsPage;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	private boolean isCoverRepeat;

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
		fireViewEvent(PABillEntryDocumentDetailsPresenter.BILL_ENTRY_SETUP_DROPDOWN_VALUES, bean);
		
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
		
		documentDetailsPage.initBinder();
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
	public void validateCoversRepeat(Boolean isValid,String coverName)
	{
		isCoverRepeat = isValid;
		documentDetailsPage.validateCoversRepeat(isValid,coverName);
	}

	@Override
	public void validateBenefitRepeat(Boolean isValid,Boolean chkBoxValue)
	{
		documentDetailsPage.validateBenefitRepeat(isValid,chkBoxValue);
	}
	
	@Override
	public void validateBenefitRepeat(Boolean isValid,Boolean chkBoxValue,CheckBox chkBox,String benefitValue)
	{
		documentDetailsPage.validateBenefitRepeat(isValid,chkBoxValue,chkBox,benefitValue);
	}
	

	@Override
	public void resetParticularsBasedOnBenefit(SelectValue particulars) {
		documentDetailsPage.setParticularsByBenefitValue(particulars);
		
	}

}
