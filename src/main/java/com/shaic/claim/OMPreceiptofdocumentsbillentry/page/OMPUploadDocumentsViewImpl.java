/**
 * 
 */
package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;

import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;

import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.ui.Component;

/**
 * @author ntv.srikanthp
 *
 */
public class OMPUploadDocumentsViewImpl extends AbstractMVPView 
implements OMPUploadDocumentsView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Inject
	private OMPUploadDocumentsPage oMPUploadDocumentsPage;

	@Inject
	private OMPUploadDocumentGridForm uploadDocsTable;

	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	public OMPClaimCalculationViewTableDTO bean;
	
	@Override
	public void resetView() {
		
	}

	@Override
	public void init(OMPClaimCalculationViewTableDTO bean) {
		localize(null);
		//this.bean = new ReceiptOfDocumentsDTO();
		this.bean = bean;
		oMPUploadDocumentsPage.init(bean);
		
	}

	@Override
	public Component getContent() {
		Component comp = oMPUploadDocumentsPage.getContent();
		fireViewEvent(OMPUploadDocumentsPresenter.OMP_UPLOAD_DOC_SETUP_DROPDOWN_VALUES, bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		
	}

	@Override
	public boolean onAdvance() {
		oMPUploadDocumentsPage.setTableValuesToDTO();
		bean  = oMPUploadDocumentsPage.bean;
		return true;
	}

	@Override
	public boolean onBack() {
		return oMPUploadDocumentsPage.isValid();
	}

	@Override
	public boolean onSave() {
		return false;
	}

	@Override
	public void setUpDropDownValues(Map<String, Object> referenceDataMap) {
			oMPUploadDocumentsPage.setFileTypeValues(referenceDataMap);
	}
	
	protected void localize(
			@Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
		strCaptionString = tb.getText(textBundlePrefixString()
				+ "uploaddocuments");// String.valueOf(propertyId).toLowerCase());
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
		
		this.oMPUploadDocumentsPage.loadUploadedDocsTableValues(uploadDocList);
		this.oMPUploadDocumentsPage.setTableValuesToDTO();
		
	}
	@Override
	public void deleteUploadDocumentDetails(UploadDocumentDTO dto) {
		oMPUploadDocumentsPage.deleteUploadDocumentDetails(dto);
	}
	
	@Override
	public void editUploadDocumentDetails(UploadDocumentDTO dto) {
		oMPUploadDocumentsPage.editUploadedDocumentDetails(dto);
		
	}
	
	public void resetPage()
	{
		oMPUploadDocumentsPage.reset();
		
	}

	
}
