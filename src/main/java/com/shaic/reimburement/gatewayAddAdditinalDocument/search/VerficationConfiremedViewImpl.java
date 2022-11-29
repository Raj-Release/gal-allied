package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;

import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.ui.Component;

public class VerficationConfiremedViewImpl extends AbstractMVPView implements VerficationConfiremedView{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Inject
	private VerficationConfiremedPage verificationPage;


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
		verificationPage.init(bean);
		// TODO Auto-generated method stub
	}

	@Override
	public Component getContent() {
		Component comp = verificationPage.getContent();
		fireViewEvent(VerficationConfiremedPresenter.BILL_ENTRY_DETAILS_PAGE_UPLOADED_DOC_SETUP_DROPDOWN_VALUES, rodDto);
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
		return verificationPage.validatePage();
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		//billEntryDetailsPage.setTableValuesToDTO();
//		return verificationPage.validatePage();
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
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
	public void setUploadDTOForBillEntry(UploadDocumentDTO uploadDTO) {
		// TODO Auto-generated method stub
		
	}

	protected void localize(
			@Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
		strCaptionString = tb.getText(textBundlePrefixString()
				+ "verificationconfirmed");// String.valueOf(propertyId).toLowerCase());
	}


}
