/**
 * 
 */
package com.shaic.claim.rod.billing.pages;

/**
 * @author ntv.vijayar
 *
 */


import java.util.HashMap;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;





import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class BillingWorksheetUploadDocumentsViewImpl extends AbstractMVPView 
implements BillingWorksheetUploadDocumentsView {
	
	@Inject
	private BillingWorksheetUploadDocumentsPage uploadDocumentsPage;


	@Inject
	private TextBundle tb;
	
	private String strCaptionString;
	
	private Window popup;
	
	private PreauthDTO bean;
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(PreauthDTO bean,Window popup) {
		localize(null);
		this.bean = bean;
		uploadDocumentsPage.init(bean,popup);
		setCompositionRoot(uploadDocumentsPage.getContent());
		// TODO Auto-generated method stub
		
	}


	
	
	protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
             strCaptionString = tb.getText(textBundlePrefixString() + "uploaddocuments");//String.valueOf(propertyId).toLowerCase());
        }
    
	@Override
	public String getCaption() {
		//return "Document Details";
		return "Upload Documents Page";
	}
	

	private String textBundlePrefixString()
	{
		return "create-rod-";
	}
	
	public void initPresenter(String presenterString)
	{
		uploadDocumentsPage.initPresenter(presenterString);
	}

	/*@Override
	public void loadUploadedDocsTableValues(
			List<UploadDocumentDTO> uploadDocList) {
		
		this.uploadDocumentsPage.loadUploadedDocsTableValues(uploadDocList);
		
	}*/
	
	@Override
	public void loadUploadedDocsTableValues(
			UploadDocumentDTO uploadDocList) {
		
		this.uploadDocumentsPage.loadUploadedDocsTableValues(uploadDocList);
		
	}
	
	@Override
	public void deleteUploadDocumentDetails(UploadDocumentDTO dto) {
		uploadDocumentsPage.deleteUploadDocumentDetails(dto);
	}

	/*@Override
	public void editUploadDocumentDetails(UploadDocumentDTO dto) {
		uploadDocumentsPage.editUploadedDocumentDetails(dto);
		
	}*/

	@Override
	public void resetPage() {
		uploadDocumentsPage.reset();
		
	}

	@Override
	//public void buildSuccessLayout(Boolean value,final Window popup) {
	public void buildSuccessLayout(String value,final Window popup) {
		String successMessage = "";
		/*if(value)
		{
			 successMessage = "Uploaded Records saved successfully!!!";
		}
		else {
			 successMessage = "Failure occured while uploading records.Please contact administrator.!!!";
		}*/
		if(("true").equalsIgnoreCase(value))
		{
			 successMessage = "Uploaded Records saved successfully!!!";
		}
		else if(("exception").equalsIgnoreCase(value)) {
			 successMessage = "Failure occured while uploading records.Please contact administrator.!!!";
		}
		else if(("No documents to upload").equalsIgnoreCase(value))
		{
			successMessage = "No documents was available to upload. Click OK to go to previous screen or click CANCEL to stay back for upload";
		}
		else
		{
			successMessage = "Please upload a document before submitting";
		}
		/*Label successLabel = new Label(
				"<b style = 'color: green;'>" + successMessage + "</b>",
				ContentMode.HTML);*/

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		/*Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		Button cancelBtn = new Button("Cancel");
		cancelBtn.setStyleName(ValoTheme.BUTTON_DANGER);
		
		FormLayout fLayout1 = new FormLayout(homeButton);
		FormLayout fLayout2 = new FormLayout(cancelBtn);

		HorizontalLayout hLayout1 = new HorizontalLayout(fLayout1, fLayout2);*/
		
		
		//VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		/*VerticalLayout layout = new VerticalLayout(successLabel, hLayout1);
	//	layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(hLayout1, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);*/

		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.CANCEL.toString(), "Cancel");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("<b style = 'color: green;'>" + successMessage + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		Button cancelBtn = messageBoxButtons.get(GalaxyButtonTypesEnum.CANCEL
				.toString());
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				//fireViewEvent(MenuItemBean.PROCESS_RRC_REQUEST, null);
				popup.close();
			
			}
		});
		cancelBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(BillingWorksheetUploadDocumentsPresenter.RETREIVE_VALUES_FOR_UPLOADED_TABLE, bean.getKey());
				//dialog.close();
				//fireViewEvent(MenuItemBean.PROCESS_RRC_REQUEST, null);
				//popup.close();
			
			}
		});
		
		
	}

	@Override
	public void setUploadedDocsTableValues(List<UploadDocumentDTO> uploadList) {
				this.uploadDocumentsPage.intializeUploadedTableList(uploadList);
	}


}
