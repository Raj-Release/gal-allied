package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class UploadReceivedPhysicalDocumentsViewImpl extends AbstractMVPView 
implements UploadReceivedPhysicalDocumentsView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Inject
	private UploadReceivedPhysicalDocumentsPage uploadDocumentsPage;


	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	private ReceiptOfDocumentsDTO bean;
	
	@Override
	public void resetView() {
		
	}

	@Override
	public void init(ReceiptOfDocumentsDTO bean) {	
		localize(null);
		this.bean = bean;
		uploadDocumentsPage.init(bean);
		
	}

	@Override
	public Component getContent() {
		Component comp = uploadDocumentsPage.getContent();
		fireViewEvent(UploadReceivedPhysicalDocumentsPresenter.UPLOAD_RECEIVED_PHYSICAL_DOC_SETUP_DROPDOWN_VALUES, bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		
	}

	@Override
	public boolean onAdvance() {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
		uploadDocumentsPage.setTableValuesToDTO();
		uploadDocumentsPage.validatePage();
		boolean isValid = this.uploadDocumentsPage.isValid();
		if(!isValid){
			hasError = true;
			List<String> errors = this.uploadDocumentsPage.getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
//			setRequired(true);
			MessageBox.createError()
	    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
	        .withOkButton(ButtonOption.caption("OK")).open();

			hasError = true;
			
		}
		return !hasError;
//		if(bean.getSelectedPhysicalDocumentsDTO() != null){
//			return true;
//		} 
//		else {
//			alertMessageForReceived();
//			return false;
//		}
	}

	@Override
	public boolean onBack() {
//		uploadDocumentsPage.isValid();
		return true;
	}

	@Override
	public boolean onSave() {
		return false;
	}

	@Override
	public void setUpDropDownValues(Map<String, Object> referenceDataMap) {
			uploadDocumentsPage.setFileTypeValues(referenceDataMap);
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
	public void loadUploadedDocsTableValues(
			UploadDocumentDTO uploadDocList) {
		
		this.uploadDocumentsPage.loadUploadedDocsTableValues(uploadDocList);
		
	}
	@Override
	public void deleteUploadDocumentDetails(UploadDocumentDTO dto) {
		uploadDocumentsPage.deleteUploadDocumentDetails(dto);
	}
	
	@Override
	public void editUploadDocumentDetails(UploadDocumentDTO dto) {
		uploadDocumentsPage.editUploadedDocumentDetails(dto);
		
	}
	
	public void resetPage()
	{
		uploadDocumentsPage.reset();
		
	}

	@Override
	public void buildSuccessLayout() {
		// TODO Auto-generated method stub
		uploadDocumentsPage.buildSuccessLayout();
	}

	protected void localize(
			@Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
		strCaptionString = tb.getText(textBundlePrefixString()
				+ "uploaddocuments");// String.valueOf(propertyId).toLowerCase());
	}
	
	public Boolean alertMessageForReceived() {
   		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createErrorBox("Please Select Received / Ignore in Uploaded Documents" + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
					
			}
		});
		return true;
	}


}
