/**
 * 
 */
package com.shaic.claims.reibursement.addaditionaldocuments;

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

/**
 * @author ntv.srikanthp
 *
 */
public class UploadDocumentsViewImpl extends AbstractMVPView 
implements UploadDocumentsView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Inject
	private UploadDocumentsPage uploadDocumentsPage;


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
		fireViewEvent(UploadDocumentsPresenter.BILL_ENTRY_UPLOAD_DOC_SETUP_DROPDOWN_VALUES, bean);
		
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		
	}

	@Override
	public boolean onAdvance() {
		uploadDocumentsPage.setTableValuesToDTO();
		return true;
	}

	@Override
	public boolean onBack() {
		return uploadDocumentsPage.isValid();
	}

	@Override
	public boolean onSave() {
		return false;
	}

	@Override
	public void setUpDropDownValues(Map<String, Object> referenceDataMap) {
			uploadDocumentsPage.setFileTypeValues(referenceDataMap);
	}
	
	protected void localize(
			@Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
		strCaptionString = tb.getText(textBundlePrefixString()
				+ "uploaddocumentsverification");// String.valueOf(propertyId).toLowerCase());
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

	
}
