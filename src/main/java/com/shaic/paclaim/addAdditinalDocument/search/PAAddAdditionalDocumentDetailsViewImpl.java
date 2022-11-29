package com.shaic.paclaim.addAdditinalDocument.search;

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

import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claims.reibursement.addaditionaldocuments.AcknowledgementReceiptViewImpl;
import com.vaadin.ui.Component;

public class PAAddAdditionalDocumentDetailsViewImpl extends AbstractMVPView implements PAAddadditionalDocumentDetailsView{

	
	private static final long serialVersionUID = 1L;

	@Inject
	private PAAddadditionalDocumentDetailsPage documentDetailsPage;
	
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
		documentDetailsPage.isNext = false;
		//documentDetailsPage.init(bean);
	}

	@Override
	public Component getContent() {
	//	documentDetailsPage.isNext = false;
		Component comp =  documentDetailsPage.getContent();
		fireViewEvent(PAAddAdditionalDocumentDetailsPresenter.BILL_ENTRY_SETUP_DROPDOWN_VALUES, bean);
		
		return comp;

	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		//documentDetailsPage.setValuesFromDTO();
	}

	@Override
	public boolean onAdvance() {
		
		if(documentDetailsPage.validatePage()) {
			documentDetailsPage.setTableValuesToDTO();
			documentDetailsPage.isNextClicked();
			return true;
		}
		
		return false;
		
		/*documentDetailsPage.setTableValuesToDTO();
		documentDetailsPage.isNextClicked();
		return documentDetailsPage.validatePage();*/
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
		documentDetailsPage.isNext = false;
		
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
	public void updateBean(ReceiptOfDocumentsDTO bean) {
			documentDetailsPage.updateBean(bean);	
	}

	@Override
	public void setThirdPageInstance(
			AcknowledgementReceiptViewImpl detailsViewImpl) {
		documentDetailsPage.setThirdPageInstance(detailsViewImpl);
	}
}
