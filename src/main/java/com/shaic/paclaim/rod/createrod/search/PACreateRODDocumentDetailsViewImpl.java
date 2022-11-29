package com.shaic.paclaim.rod.createrod.search;

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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;

public class PACreateRODDocumentDetailsViewImpl extends AbstractMVPView implements PACreateRODDocumentDetailsView{

	
	@Inject
	private PACreateRODDocumentDetailsPage documentDetailsPage;
	
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
		documentDetailsPage.resetList();
		Component comp =  documentDetailsPage.getContent();
		//setCompositionRoot(comp);
		fireViewEvent(PACreateRODDocumentDetailsPresenter.CREATE_ROD_SETUP_DROPDOWN_VALUES, bean);
		
		return comp;

	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		//documentDetailsPage.setValuesFromDTO();
	}

	@Override
	public boolean onAdvance() {		
		
		if(!(bean.getScreenName() != null && bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN))){
			documentDetailsPage.setTableValuesToDTO();
			if(documentDetailsPage.validatePage()) {
				/**
				 * Since there is no pre or post hospitalization
				 * in PA, the below code is not required. Hence
				 * commenting the same.
				 * 
				 * */
				/*if(!bean.getIsComparisonDone() && ("Y".equalsIgnoreCase(bean.getDocumentDetails().getHospitalizationFlag()) || "Y".equalsIgnoreCase(bean.getDocumentDetails().getPartialHospitalizationFlag()) || "Y".equalsIgnoreCase(bean.getDocumentDetails().getHospitalizationRepeatFlag()))) {
					documentDetailsPage.showPopupForComparison(bean.getComparisonResult());
					return false;
				}*/
				return true;
			}
			
			return false;
		} else {
			if(documentDetailsPage.validatePaymentCancellation()){
				documentDetailsPage.alertForvalidatePaymentCancellationFlag();
				return false;
			}
			return true;
		}
	}

	@Override
	public boolean onBack() {
		documentDetailsPage.initBinder();
		return true;
	}

	@Override
	public boolean onSave() {
		documentDetailsPage.setTableValuesToDTO();
		//return documentDetailsPage.validatePage();
		return false;
	}

	@Override
	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard) {
		// TODO Auto-generated method stub
		localize(null);
		this.bean = bean;
		resetFlds();
		documentDetailsPage.init(bean,wizard);
		
	}
	
	@Override
	public String getCaption() {
		//return "Document Details";
		return strCaptionString;
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
		return "create-rod-";
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
	public void setHospitalDetails(UpdateHospitalDetailsDTO changeHospital) {
	
		documentDetailsPage.changeHospitalDetails(changeHospital);
	}
	
	public void setDocumentDetailsDTOForValidation(
			List<DocumentDetailsDTO> documentDetailsDTO) {
		documentDetailsPage.setDocumentDetailsListForValidation(documentDetailsDTO);
		
	}

	@Override
	public void setUpPaymentDetails(ReceiptOfDocumentsDTO rodDTO) {
		documentDetailsPage.setUpPaymentDetails(rodDTO);
		
	}

	@Override
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		documentDetailsPage.setUpIFSCDetails(dto);
		
	}

	@Override
	public void setDocStatusIfReplyReceivedForQuery(
			RODQueryDetailsDTO rodQueryDetails) {
		documentDetailsPage.setDocStatusIfReplyReceivedForQuery(rodQueryDetails);
		
	}
	

	@Override
	public void setComparisonResult(String comparisonResult) {
		this.bean.setComparisonResult(comparisonResult);
		if(comparisonResult == null || comparisonResult.isEmpty()) {
			this.bean.setIsComparisonDone(true);
		}
		
	}

	@Override
	public void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO) {
		documentDetailsPage.populatePreviousPaymentDetails(tableDTO);
		
	}


	
	/*@Override
	public void setUpPaymentDetailsByQueryRecStatus(Reimbursement reimObj) {
		// TODO Auto-generated method stub
		documentDetailsPage.getBean().getDocumentDetails().setPanNo(reimObj.getPanNumber());
		documentDetailsPage.getBean().getDocumentDetails().setAccountNo(reimObj.getAccountNumber());
	}
*/
	@Override
	public void validateBenefitRepeat(Boolean isValid,Boolean chkBoxValue,CheckBox chkBox,String benefitValue)
	{
		documentDetailsPage.validateBenefitRepeat(isValid,chkBoxValue,chkBox,benefitValue);
	}
	
	@Override
	public void validateCoversRepeat(Boolean isValid,String coverName)
	{
		isCoverRepeat = isValid;
		documentDetailsPage.validateCoversRepeat(isValid,coverName);
	}
	
	@Override
	public void resetParticularsBasedOnBenefit(
			BeanItemContainer<SelectValue> particulars,String values) {
		documentDetailsPage.setParticularsByBenefitValue(values);
		
	}

	@Override
	public void resetParticularsBasedOnAddOnCover(SelectValue value) {
		documentDetailsPage.setParticularsByAddonCovers(value);		
	}
	
	@Override
	public void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto){
		documentDetailsPage.setUpAddBankIFSCDetails(dto);
	}
}
