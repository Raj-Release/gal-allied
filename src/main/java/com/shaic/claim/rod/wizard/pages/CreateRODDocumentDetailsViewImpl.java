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

import com.google.gwt.thirdparty.common.css.compiler.gssfunctions.GssFunctions.AddToNumericValue;
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
import com.shaic.claim.rod.wizard.forms.AddBankDetailsTable;
import com.shaic.claim.rod.wizard.forms.AddBanksDetailsTableDTO;
import com.shaic.claim.rod.wizard.forms.CreateRODDocumentDetailsPage;
import com.vaadin.ui.Component;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.TextField;

/**
 * @author ntv.vijayar
 *
 */
public class CreateRODDocumentDetailsViewImpl extends AbstractMVPView 
		implements CreateRODDocumentDetailsView
{
	
	@Inject
	private CreateRODDocumentDetailsPage documentDetailsPage;
	
	@Inject
	private AddBankDetailsTable addBankDetailsTable;
	
	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	private String presenterString;

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
		fireViewEvent(CreateRODDocumentDetailsPresenter.CREATE_ROD_SETUP_DROPDOWN_VALUES, bean);
		
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
				if(!bean.getIsComparisonDone() && ("Y".equalsIgnoreCase(bean.getDocumentDetails().getHospitalizationFlag()) || "Y".equalsIgnoreCase(bean.getDocumentDetails().getPartialHospitalizationFlag()) || "Y".equalsIgnoreCase(bean.getDocumentDetails().getHospitalizationRepeatFlag()))) {
					documentDetailsPage.showPopupForComparison(bean.getComparisonResult());
					return false;
				}
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
		return documentDetailsPage.validatePage();
	}

	@Override
	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard,String presenterString) {
		// TODO Auto-generated method stub
		localize(null);
		this.bean = bean;
		this.presenterString =presenterString;
		resetFlds();
		documentDetailsPage.init(bean,wizard,presenterString);
		
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
		documentDetailsPage.resetLumpsumValidationFlag();
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
	public void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		documentDetailsPage.setUpAddBankIFSCDetails(dto);
		
	}
	
	@Override
	public void setUpBankDetails(ReceiptOfDocumentsDTO dto) {
		documentDetailsPage.setUpBankDetails(dto);
		
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

	@Override
	public void validateLumpSumAmount(Boolean isValid,
			String classificationType, CheckBox chkBox) {
		documentDetailsPage.validateLumpSumAmount(isValid,classificationType,chkBox);
		
	}

	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
		documentDetailsPage.setCoverList(coverContainer);
		
	}

	@Override
	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {
		documentDetailsPage.setSubCoverList(subCoverContainer);		
	}

	
	/*@Override
	public void setUpPaymentDetailsByQueryRecStatus(Reimbursement reimObj) {
		// TODO Auto-generated method stub
		documentDetailsPage.getBean().getDocumentDetails().setPanNo(reimObj.getPanNumber());
		documentDetailsPage.getBean().getDocumentDetails().setAccountNo(reimObj.getAccountNumber());
	}
*/
	
	@Override
	public void validateHospitalizationRepeat(Boolean isValid) {
		documentDetailsPage.validateHospitalizationRepeat(isValid);
		
	}
	
	@Override
	public void setClearMapObject(){
		documentDetailsPage.setClearReferenceData();
	}
	
	/*@Override
	public void validateClaimedAmount(){

		
		ConfirmDialog dialog = ConfirmDialog
				.show(getUI(),
						"Confirmation",
						SHAConstants.ACK_CLAIMED_AMOUNT_ALERT,
						"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									
									dialog.close();
									
								} else {
									// User did not confirm
									txtHospitalizationClaimedAmt.setValue(null);
									txtPreHospitalizationClaimedAmt.setValue(null);
									txtPostHospitalizationClaimedAmt.setValue(null);
									txtOtherBenefitsClaimedAmnt.setValue(null);
									documentDetailsPage.setOptDocVerifiedValue();
									dialog.close();
									dialog.setClosable(false);
									//dialog.setStyleName(Reindeer.WINDOW_BLACK);

								}
							}
						});
			dialog.setClosable(false);
	
	}*/
	
	@Override
	public void setOptDocVerifiedValue(){
		documentDetailsPage.setOptDocVerifiedValue();
	}
	
	@Override
	public void resetClaimedAmntValue(TextField txtField){
		documentDetailsPage.resetClaimedAmntValue(txtField);
	}

	@Override
	public void setUpPayableDetails(String payableAt) {
		documentDetailsPage.setUpPayableDetails(payableAt);		
	}
}
