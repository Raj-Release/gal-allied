package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import javax.ejb.EJB;

import org.apache.commons.io.FileUtils;
/*import org.vaadin.dialogs.ConfirmDialog;*/




import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ClaimRequestFileUploadUI extends Window {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	
	private PreauthDTO bean;
	
	private File file;
	
	private ComboBox specialistType;
	
	private ComboBox escalateToCmb;
	
	private TextArea escalationRemarksTxta;
	
	private TextArea reasonForReferringIV;
	
	private ArrayList<Component> mandatoryFields;
	
	private List<String> errorMessages;
	
	private Upload upload;
	
	//private Button submitButton = new Button("Submit");
	private Button submitButton = null;
	
	public Button cancelButton;
	
	private ClaimsSubmitHandler submitHandler; 
	
	private Button tmpViewBtn;
	

	//private UploadedFileViewUI fileViewUI;
	
	
	
	//private ClaimRequestFileUploadUI specialistWindow;
	
	private ClaimRequestFileUploadUI thisObj;
	
	private TextField referredByRole;
	private TextField referredByName;
	private TextField escalateDesignation;
	private TextArea escalteReplyTxt;
	//private TextField reasonForReferring;
	private TextField remarks;
	//private TextArea medicalApproversReply;
	
	private ComboBox cmbSpecialistType;
	
	public void addSubmitHandler(ClaimsSubmitHandler handler)
	{
		this.submitHandler = handler;
	}
//	
	private void initBinder() {
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(
				PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(bean.getPreauthMedicalDecisionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		thisObj = this;
	}
	
	public void init(PreauthDTO bean){
		
		this.bean = bean;
		initBinder();
		errorMessages = new ArrayList<String>();
		
	}
	
	public void buildSpecialityLayout(Object specialistValues,final UploadedFileViewUI fileViewUI){
		//this.fileViewUI = fileViewUI;
		specialistType = (ComboBox) binder.buildAndBind("Specialist Type",
				"specialistType", ComboBox.class);
		specialistType
				.setContainerDataSource((BeanItemContainer<SelectValue>) specialistValues);
		specialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		specialistType.setItemCaptionPropertyId("value");
		
		if(this.bean.getPreauthMedicalDecisionDetails() != null && this.bean.getPreauthMedicalDecisionDetails().getSpecialistType() != null){

			specialistType.setValue(this.bean.getPreauthMedicalDecisionDetails().getSpecialistType());
		}
		
		// uploadFile = (Upload)
		// binder.buildAndBind("File UpLoad","uploadFile",Upload.class);
	    upload  = new Upload("", new Receiver() {
			
			private static final long serialVersionUID = 4775959511314943621L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				// Create upload stream
		        FileOutputStream fos = null; // Stream to write to
		        try {
		            // Open the file for writing.
		        	
		            file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
		            fos = new FileOutputStream(file);
//		        	if(this.screenName.equals(ReferenceTable.UPLOAD_INVESTIGATION_SCREEN)){
//		        		fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT, this.key,filename);
//		        	}
		        } catch (final java.io.FileNotFoundException e) {
		            new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
		                .show(Page.getCurrent());
		            return null;
		        }
		        return fos; // Return the output stream to write to
			}
		});	
		upload.addSucceededListener(new SucceededListener() {
			
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				System.out.println("File uploaded" + event.getFilename());
				
				try{
					
					byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);

					String fileName = event.getFilename();

					if (null != fileAsbyteArray) {
						//file gets uploaded in data directory when code comes here.
						if(null != event && null != event.getFilename() && (event.getFilename().endsWith("jpg") || event.getFilename().endsWith("jpeg") ||
								event.getFilename().endsWith("JPG") || event.getFilename().endsWith("JPEG")))
						{
							/*File convertedFile  = SHAFileUtils.convertImageToPDF(event.getFilename());

							fileName = convertedFile.getName();
							fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);*/
						}
						

						//Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
						//boolean hasSpecialChar = p.matcher(event.getFilename()).find();
					//	if(hasSpecialChar)
						//{
						WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
							Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
							//TO read file after load
							if (flagUploadSuccess.booleanValue())
							{
								String token = "" + uploadStatus.get("fileKey");
								//String fileName = event.getFilename();
							    bean.setTokenName(token);
							    bean.setFileName(fileName);
							    tmpViewBtn.setEnabled(true);
							    buildSuccessLayout();
							    uploadStatus = null;
//							    submitHandler.submit(bean);
//							    thisObj.close();
							}
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		upload.setButtonCaption(null);
		upload.setCaption("File Upload");
		
		TextField dummyField = new TextField();
		dummyField.setWidth("60%");
		dummyField.setEnabled(false);
		dummyField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		TextField dummyField2 = new TextField();
		dummyField2.setWidth("60%");
		dummyField2.setEnabled(false);
		dummyField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		tmpViewBtn = new Button("View File");
	    tmpViewBtn.setEnabled(false);
	    tmpViewBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
	    tmpViewBtn.addStyleName(ValoTheme.BUTTON_LINK);
	    tmpViewBtn.setWidth("50%");
	
        tmpViewBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(bean.getFileName() != null && bean.getTokenName() != null){
					Window popup = new com.vaadin.ui.Window();
					popup.setWidth("75%");
					popup.setHeight("90%");
					fileViewUI.init(popup,bean.getFileName(), bean.getTokenName());
					popup.setContent(fileViewUI);
					popup.setClosable(true);
					popup.center();
					popup.setResizable(false);
					popup.addCloseListener(new Window.CloseListener() {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;
	
						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});
	
					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
				}
			}
		});
		
		Button uploadBtn = new Button("Upload");
		
		VerticalLayout firstVertical = new VerticalLayout(dummyField,uploadBtn);
		
		VerticalLayout secondVertical = new VerticalLayout(dummyField2,tmpViewBtn);
		
		HorizontalLayout uploadHor = new HorizontalLayout(firstVertical,secondVertical);
		uploadHor.setSpacing(true);
		
		uploadBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				upload.submitUpload();
			}
		});
		
		reasonForReferringIV = (TextArea) binder.buildAndBind(
				"Reason for Referring", "reasonForRefering", TextArea.class);
		reasonForReferringIV.setMaxLength(4000);
//		reasonForReferringIV.setWidth("400px");
		
		reasonForReferringIV.setId("splRmrks");
		escalationRemarksListener(reasonForReferringIV,null);
		reasonForReferringIV.setData(bean);
		reasonForReferringIV.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		

		HorizontalLayout layout = new HorizontalLayout(new FormLayout(
				specialistType, upload, reasonForReferringIV),uploadHor);
		layout.setSpacing(true);

		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(reasonForReferringIV);
		mandatoryFields.add(specialistType);
		showOrHideValidation(false);

//		Button submitButtonWithListener = getSubmitButtonWithDMS(dialog);
		
		Button submitButton = new Button("Submit");
		
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new Button.ClickListener() {
			

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {			
				StringBuffer eMsg = new StringBuffer();
				
				if (isValid()) {
//					 upload.submitUpload();
					 submitHandler.submit(bean);
				} else {
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
					showErrorPopup(eMsg.toString());
				}
				
			}
		});

		HorizontalLayout btnLayout = new HorizontalLayout(
				submitButton,getCancelButton(thisObj));
		btnLayout.setWidth("400px");
		btnLayout.setMargin(true);
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButton,
				Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);

		VerticalLayout VLayout = new VerticalLayout(layout, btnLayout);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
		setContent(VLayout);
//		showInPopup(VLayout, dialog);
	}
	
	public void buildSpecialityLayout(Object specialistValues,final UploadedFileViewUI fileViewUI, final BeanItemContainer<SelectValue> masterValueByReference){
		//this.fileViewUI = fileViewUI;
		specialistType = (ComboBox) binder.buildAndBind("Specialist Type",
				"specialistType", ComboBox.class);
		specialistType.setContainerDataSource((BeanItemContainer<SelectValue>) masterValueByReference);
		specialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		specialistType.setItemCaptionPropertyId("value");
		
		if(this.bean.getPreauthMedicalDecisionDetails() != null && this.bean.getPreauthMedicalDecisionDetails().getSpecialistType() != null){

			specialistType.setValue(this.bean.getPreauthMedicalDecisionDetails().getSpecialistType());
		}
		
		// uploadFile = (Upload)
		// binder.buildAndBind("File UpLoad","uploadFile",Upload.class);
	    upload  = new Upload("", new Receiver() {
			
			private static final long serialVersionUID = 4775959511314943621L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				// Create upload stream
		        FileOutputStream fos = null; // Stream to write to
		        try {
		            // Open the file for writing.
		        	
		            file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
		            fos = new FileOutputStream(file);
//		        	if(this.screenName.equals(ReferenceTable.UPLOAD_INVESTIGATION_SCREEN)){
//		        		fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT, this.key,filename);
//		        	}
		        } catch (final java.io.FileNotFoundException e) {
		            new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
		                .show(Page.getCurrent());
		            return null;
		        }
		        return fos; // Return the output stream to write to
			}
		});	
		upload.addSucceededListener(new SucceededListener() {
			
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				System.out.println("File uploaded" + event.getFilename());
				
				try{
					
					byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);

					String fileName = event.getFilename();

					if (null != fileAsbyteArray) {
						//file gets uploaded in data directory when code comes here.
						if(null != event && null != event.getFilename() && (event.getFilename().endsWith("jpg") || event.getFilename().endsWith("jpeg") ||
								event.getFilename().endsWith("JPG") || event.getFilename().endsWith("JPEG")))
						{
							/*File convertedFile  = SHAFileUtils.convertImageToPDF(event.getFilename());

							fileName = convertedFile.getName();
							fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);*/
						}
						

						//Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
						//boolean hasSpecialChar = p.matcher(event.getFilename()).find();
					//	if(hasSpecialChar)
						//{
						WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
							Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
							//TO read file after load
							if (flagUploadSuccess.booleanValue())
							{
								String token = "" + uploadStatus.get("fileKey");
								//String fileName = event.getFilename();
							    bean.setTokenName(token);
							    bean.setFileName(fileName);
							    tmpViewBtn.setEnabled(true);
							    buildSuccessLayout();
							    uploadStatus = null;
//							    submitHandler.submit(bean);
//							    thisObj.close();
							}
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		upload.setButtonCaption(null);
		upload.setCaption("File Upload");
		
		TextField dummyField = new TextField();
		dummyField.setWidth("60%");
		dummyField.setEnabled(false);
		dummyField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		TextField dummyField2 = new TextField();
		dummyField2.setWidth("60%");
		dummyField2.setEnabled(false);
		dummyField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		tmpViewBtn = new Button("View File");
	    tmpViewBtn.setEnabled(false);
	    tmpViewBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
	    tmpViewBtn.addStyleName(ValoTheme.BUTTON_LINK);
	    tmpViewBtn.setWidth("50%");
	
        tmpViewBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(bean.getFileName() != null && bean.getTokenName() != null){
					Window popup = new com.vaadin.ui.Window();
					popup.setWidth("75%");
					popup.setHeight("90%");
					fileViewUI.init(popup,bean.getFileName(), bean.getTokenName());
					popup.setContent(fileViewUI);
					popup.setClosable(true);
					popup.center();
					popup.setResizable(false);
					popup.addCloseListener(new Window.CloseListener() {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;
	
						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});
	
					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
				}
			}
		});
		
		Button uploadBtn = new Button("Upload");
		
		VerticalLayout firstVertical = new VerticalLayout(dummyField,uploadBtn);
		
		VerticalLayout secondVertical = new VerticalLayout(dummyField2,tmpViewBtn);
		
		HorizontalLayout uploadHor = new HorizontalLayout(firstVertical,secondVertical);
		uploadHor.setSpacing(true);
		
		uploadBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				upload.submitUpload();
			}
		});
		
		reasonForReferringIV = (TextArea) binder.buildAndBind(
				"Reason for Referring", "reasonForRefering", TextArea.class);
		reasonForReferringIV.setMaxLength(4000);
//		reasonForReferringIV.setWidth("400px");
		
		reasonForReferringIV.setId("splRmrks");
		escalationRemarksListener(reasonForReferringIV,null);
		reasonForReferringIV.setData(bean);
		reasonForReferringIV.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		

		HorizontalLayout layout = new HorizontalLayout(new FormLayout(
				specialistType, upload, reasonForReferringIV),uploadHor);
		layout.setSpacing(true);

		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(reasonForReferringIV);
		mandatoryFields.add(specialistType);
		showOrHideValidation(false);

//		Button submitButtonWithListener = getSubmitButtonWithDMS(dialog);
		
		Button submitButton = new Button("Submit");
		
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new Button.ClickListener() {
			

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {			
				StringBuffer eMsg = new StringBuffer();
				
				if (isValid()) {
//					 upload.submitUpload();
					 submitHandler.submit(bean);
				} else {
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
					showErrorPopup(eMsg.toString());
				}
				
			}
		});

		HorizontalLayout btnLayout = new HorizontalLayout(
				submitButton,getCancelButton(thisObj));
		btnLayout.setWidth("400px");
		btnLayout.setMargin(true);
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButton,
				Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);

		VerticalLayout VLayout = new VerticalLayout(layout, btnLayout);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
		setContent(VLayout);
//		showInPopup(VLayout, dialog);
	}
	
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> File Uploaded Successfully.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		/*Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("<b style = 'color: green;'> File Uploaded Successfully.</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
			}
		});
	}
	
	
   public void buildEscalateLayout(Object escalateToValues,final UploadedFileViewUI fileViewUI,final BeanItemContainer<SelectValue> masterValueByReference) {
	   
	   //this.fileViewUI = fileViewUI;
	   
	   initBinder();
	   
	   
	BeanItemContainer<SelectValue> escalateValues = (BeanItemContainer<SelectValue>) escalateToValues;
		
		BeanItemContainer<SelectValue> updatedValues = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		List<SelectValue> itemIds = escalateValues.getItemIds();
		
		/*for (SelectValue selectValue : itemIds) {
			if(selectValue.getId().equals(ReferenceTable.REIMBURSEMENT_ESCALATE_SPECIALIST)){
//				updatedValues.addBean(selectValue);
			}
		}*/
		
		if(bean.getPreauthMedicalDecisionDetails().getRMA5()){
			for(SelectValue selectValue : itemIds){
				if(selectValue.getId() > (ReferenceTable.RMA5)){
					updatedValues.addBean(selectValue);
				}
			}
		}else if(bean.getPreauthMedicalDecisionDetails().getRMA4()){
			
			for(SelectValue selectValue : itemIds){
				if(selectValue.getId() > (ReferenceTable.RMA4)){
					updatedValues.addBean(selectValue);
				}
			}
		}else if(bean.getPreauthMedicalDecisionDetails().getRMA3()){
			for(SelectValue selectValue : itemIds){
				if(selectValue.getId() > (ReferenceTable.RMA3)){
					updatedValues.addBean(selectValue);
				}
			}
		}else if(bean.getPreauthMedicalDecisionDetails().getRMA2()){
			for(SelectValue selectValue : itemIds){
				if(selectValue.getId() > (ReferenceTable.RMA2)){
					updatedValues.addBean(selectValue);
				}
			}
			
		}else if(bean.getPreauthMedicalDecisionDetails().getRMA1()){ 
			for(SelectValue selectValue : itemIds){
				if(selectValue.getId() > (ReferenceTable.RMA1)){
					updatedValues.addBean(selectValue);
				}
			}
		}
		
//		for (SelectValue selectValue : itemIds) {
//			if(! selectValue.getId().equals(ReferenceTable.RMA3) && ! selectValue.getId().equals(ReferenceTable.RMA4)){
//				newItemIds.add(selectValue);
//			}
//		}
//		
//		if(! newItemIds.isEmpty()){
//			updatedValues.addAll(newItemIds);
//		}
		
	escalateToCmb = (ComboBox) binder.buildAndBind("Escalate To",
			"escalateTo", ComboBox.class);
	escalateToCmb
			.setContainerDataSource((BeanItemContainer<SelectValue>) updatedValues);
	escalateToCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	escalateToCmb.setItemCaptionPropertyId("value");
	
	if(this.bean.getPreauthMedicalDecisionDetails().getEscalateTo() != null){
		escalateToCmb.setValue(this.bean.getPreauthMedicalDecisionDetails().getEscalateTo());
	}
	
	// uploadFile = (Upload)
	// binder.buildAndBind("File UpLoad","uploadFile",Upload.class);
    upload  = new Upload("", new Receiver() {
		
		private static final long serialVersionUID = 4775959511314943621L;

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			// Create upload stream
	        FileOutputStream fos = null; // Stream to write to
	        try {
	            // Open the file for writing.
	            file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
	            fos = new FileOutputStream(file);
//	        	if(this.screenName.equals(ReferenceTable.UPLOAD_INVESTIGATION_SCREEN)){
//	        		fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT, this.key,filename);
//	        	}
	        } catch (final java.io.FileNotFoundException e) {
	            new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
	                .show(Page.getCurrent());
	            return null;
	        }
	        return fos; // Return the output stream to write to
		}
	});	
	upload.addSucceededListener(new SucceededListener() {
		
		@Override
		public void uploadSucceeded(SucceededEvent event) {
			System.out.println("File uploaded" + event.getFilename());
			
			try{
				
				byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);

				
				String fileName = event.getFilename();

				if (null != fileAsbyteArray) {
					//file gets uploaded in data directory when code comes here.
					if(null != event && null != event.getFilename() && (event.getFilename().endsWith("jpg") || event.getFilename().endsWith("jpeg") ||
							event.getFilename().endsWith("JPG") || event.getFilename().endsWith("JPEG")))
					{
						/*File convertedFile  = SHAFileUtils.convertImageToPDF(event.getFilename());
						fileName = convertedFile.getName();
						fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);*/
					}
					/*Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
					boolean hasSpecialChar = p.matcher(event.getFilename()).find();*/
				//	if(hasSpecialChar)
					//{
					WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
						Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
						//TO read file after load
						if (flagUploadSuccess.booleanValue())
						{
							String token = "" + uploadStatus.get("fileKey");
							//String fileName = event.getFilename();
						    bean.setTokenName(token);
						    bean.setFileName(fileName);
						    tmpViewBtn.setEnabled(true);
						    buildSuccessLayout();
						    uploadStatus = null;
//						    submitHandler.submit(bean);
//						    thisObj.close();
						}
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
	upload.setButtonCaption(null);
	
	TextField dummyField = new TextField();
	dummyField.setWidth("60%");
	dummyField.setEnabled(false);
	dummyField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	TextField dummyField2 = new TextField();
	dummyField2.setWidth("60%");
	dummyField2.setEnabled(false);
	dummyField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	tmpViewBtn = new Button("View File");
    tmpViewBtn.setEnabled(false);
    tmpViewBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
    tmpViewBtn.addStyleName(ValoTheme.BUTTON_LINK);
    tmpViewBtn.setWidth("50%");

    tmpViewBtn.addClickListener(new Button.ClickListener() {
		
		@Override
		public void buttonClick(ClickEvent event) {
			
			if(bean.getFileName() != null && bean.getTokenName() != null){
				Window popup = new com.vaadin.ui.Window();
				popup.setWidth("75%");
				popup.setHeight("90%");
				fileViewUI.init(popup,bean.getFileName(), bean.getTokenName());
				popup.setContent(fileViewUI);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.addCloseListener(new Window.CloseListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			}
		}
	});
	
	Button uploadBtn = new Button("Upload");
	
//	VerticalLayout firstVertical = new VerticalLayout(dummyField,uploadBtn);
//	
//	VerticalLayout secondVertical = new VerticalLayout(dummyField2,tmpViewBtn);
//	
//	HorizontalLayout uploadHor = new HorizontalLayout(firstVertical,secondVertical);
//	uploadHor.setSpacing(true);
	
	HorizontalLayout uploadHor = new HorizontalLayout(upload,uploadBtn,tmpViewBtn);
	uploadHor.setComponentAlignment(uploadBtn, Alignment.BOTTOM_CENTER);
	uploadHor.setComponentAlignment(tmpViewBtn, Alignment.BOTTOM_RIGHT);
	uploadHor.setSpacing(true);
	
	uploadBtn.addClickListener(new Button.ClickListener() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			
			bean.getPreauthMedicalDecisionDetails().setReasonForRefering(escalationRemarksTxta.getValue());
			upload.submitUpload();
		}
	});
	
	escalationRemarksTxta = (TextArea) binder.buildAndBind("Escalate Remarks", "escalationRemarks", TextArea.class);
    escalationRemarksTxta.setMaxLength(4000);
//    escalationRemarksTxta.setWidth("00px");
    
    escalationRemarksTxta.setId("escalateRmrks");
	escalationRemarksListener(escalationRemarksTxta,null);
	escalationRemarksTxta.setData(bean);
	escalationRemarksTxta.setDescription("Click the Text Box and Press F8 For Detailed Popup");
	
    final FormLayout dynamicFormLayout = new FormLayout(
			escalateToCmb, uploadHor,escalationRemarksTxta);

	HorizontalLayout layout = new HorizontalLayout(dynamicFormLayout);
	layout.setSpacing(true);

	mandatoryFields = new ArrayList<Component>();
	mandatoryFields.add(escalationRemarksTxta);
	mandatoryFields.add(escalateToCmb);
	showOrHideValidation(false);
	escalateToCmb.addValueChangeListener(new ValueChangeListener() {
		
		@Override
		public void valueChange(ValueChangeEvent event) {
			SelectValue value = (SelectValue) event.getProperty().getValue();
			
			if(value != null && value.getId().equals(ReferenceTable.REIMBURSEMENT_ESCALATE_SPECIALIST)){
				
				unbindField(cmbSpecialistType);
				cmbSpecialistType = (ComboBox) binder.buildAndBind("Specialist Type","specialistType",ComboBox.class);
				cmbSpecialistType.setContainerDataSource(masterValueByReference);
				cmbSpecialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbSpecialistType.setItemCaptionPropertyId("value");
				cmbSpecialistType.setVisible(true);
				
				dynamicFormLayout.addComponent(cmbSpecialistType,1);
				mandatoryFields.add(cmbSpecialistType);
				showOrHideValidation(false);
				
			}else{
				unbindField(cmbSpecialistType);
				if(cmbSpecialistType != null){
					
					dynamicFormLayout.removeComponent(cmbSpecialistType);
					mandatoryFields.remove(cmbSpecialistType);
					cmbSpecialistType.setVisible(false);
				}
					
			}
		}
	});

//	Button submitButtonWithListener = getSubmitButtonWithDMS(dialog);
	submitButton = new Button("Submit");
	submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	submitButton.addClickListener(new Button.ClickListener() {
		

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {			
			StringBuffer eMsg = new StringBuffer();
			
			if (isValid()) {
//				 upload.submitUpload();
				 submitHandler.submit(bean);
			} else {
				List<String> errors = getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
				showErrorPopup(eMsg.toString());
			}
			
		}
	});

	HorizontalLayout btnLayout = new HorizontalLayout(
			submitButton,getCancelButton(thisObj));
	btnLayout.setWidth("400px");
	btnLayout.setMargin(true);
	btnLayout.setSpacing(true);
	btnLayout.setComponentAlignment(submitButton,
			Alignment.MIDDLE_CENTER);
	showOrHideValidation(false);

	VerticalLayout VLayout = new VerticalLayout(layout, btnLayout);
	VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
	VLayout.setWidth("800px");
	VLayout.setMargin(true);
	VLayout.setSpacing(true);
	setContent(VLayout);
	

//
//	HorizontalLayout layout = new HorizontalLayout(new FormLayout(
//			escalateToCmb, upload, escalationRemarksTxta));
//	layout.setSpacing(true);
//
//	mandatoryFields = new ArrayList<Component>();
//	mandatoryFields.add(escalationRemarksTxta);
//	mandatoryFields.add(escalateToCmb);
//	showOrHideValidation(false);
//
////	Button submitButtonWithListener = getSubmitButtonWithDMS(dialog);
//	
//	submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//	submitButton.addClickListener(new Button.ClickListener() {
//		
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//
//		@Override
//		public void buttonClick(ClickEvent event) {			
//			String eMsg = "";
//			
//			if (isValid()) {
//				 upload.submitUpload();
//				 
////				 submitHandler.submit(bean);
//			} else {
//				List<String> errors = getErrors();
//				for (String error : errors) {
//					eMsg += error + "</br>";
//				}
//				showErrorPopup(eMsg);
//			}
//			
//		}
//	});
//
//	HorizontalLayout btnLayout = new HorizontalLayout(
//			submitButton,getCancelButton(thisObj));
//	btnLayout.setWidth("400px");
//	btnLayout.setMargin(true);
//	btnLayout.setSpacing(true);
//	btnLayout.setComponentAlignment(submitButton,
//			Alignment.MIDDLE_CENTER);
//	showOrHideValidation(false);
//
//	VerticalLayout VLayout = new VerticalLayout(layout, btnLayout);
//	VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
//	VLayout.setWidth("800px");
//	VLayout.setMargin(true);
//	VLayout.setSpacing(true);
//	setContent(VLayout);
	}
   
	public void buildEscalateReplyLayout() {
		

		referredByRole = new TextField("Escalted By-Role");
		referredByRole.setEnabled(false);

		referredByName = new TextField("Escalted By-ID / Name");
		referredByName.setEnabled(false);

		escalateDesignation = new TextField("Escalted By - Designation");
		escalateDesignation.setEnabled(false);

		remarks = new TextField("Escaltion Remarks");
		remarks.setEnabled(false);
		remarks.setMaxLength(4000);
		
		if(this.bean.getPreauthMedicalDecisionDetails().getEscalateRemarks() != null){
			remarks.setValue(this.bean.getPreauthMedicalDecisionDetails().getEscalateRemarks());
		}

		 upload  = new Upload("", new Receiver() {
				
				private static final long serialVersionUID = 4775959511314943621L;

				@Override
				public OutputStream receiveUpload(String filename, String mimeType) {
					// Create upload stream
			        FileOutputStream fos = null; // Stream to write to
			        try {
			            // Open the file for writing.
			            file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
			            fos = new FileOutputStream(file);
//			        	if(this.screenName.equals(ReferenceTable.UPLOAD_INVESTIGATION_SCREEN)){
//			        		fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT, this.key,filename);
//			        	}
			        } catch (final java.io.FileNotFoundException e) {
			            new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
			                .show(Page.getCurrent());
			            return null;
			        }
			        return fos; // Return the output stream to write to
				}
			});	
			upload.addSucceededListener(new SucceededListener() {
				
				@Override
				public void uploadSucceeded(SucceededEvent event) {
					System.out.println("File uploaded" + event.getFilename());
					
					try{
						
						byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
						String fileName = event.getFilename();

						if (null != fileAsbyteArray) {
							//file gets uploaded in data directory when code comes here.
							if(null != event && null != event.getFilename() && (event.getFilename().endsWith("jpg") || event.getFilename().endsWith("jpeg") ||
									event.getFilename().endsWith("JPG") || event.getFilename().endsWith("JPEG")))
							{
								/*File convertedFile  = SHAFileUtils.convertImageToPDF(event.getFilename());
								fileName = convertedFile.getName();
								fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);*/
							}

							/*Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
							boolean hasSpecialChar = p.matcher(event.getFilename()).find();*/
						//	if(hasSpecialChar)
							//{
							WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
								Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
								//TO read file after load
								if (flagUploadSuccess.booleanValue())
								{
									String token = "" + uploadStatus.get("fileKey");
									//String fileName = event.getFilename();
								    bean.setTokenName(token);
								    bean.setFileName(fileName);
								    submitHandler.submit(bean);
								    uploadStatus = null;
//								    thisObj.close();
								}
						}
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
			upload.setButtonCaption(null);
			
			upload.setCaption("File Upload");
			
		escalteReplyTxt = (TextArea) binder.buildAndBind("Escalate Reply",
				"escalateReply", TextArea.class);
		escalteReplyTxt.setMaxLength(4000);

		HorizontalLayout layout = new HorizontalLayout(new FormLayout(
				referredByRole, remarks, escalteReplyTxt, upload),
				new FormLayout(referredByName, escalateDesignation));
		layout.setSpacing(true);


		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(escalteReplyTxt);
		showOrHideValidation(false);

		//final ConfirmDialog dialog = new ConfirmDialog();
		submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new Button.ClickListener() {
			

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {			
				StringBuffer eMsg = new StringBuffer();
				
				if (isValid()) {
					 upload.submitUpload();
					 
					 submitHandler.submit(bean);
				} else {
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
					showErrorPopup(eMsg.toString());
				}
				
			}
		});
		HorizontalLayout btnLayout = new HorizontalLayout(
				submitButton, getCancelButton(thisObj));
		btnLayout.setWidth("400px");
		btnLayout.setMargin(true);
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButton,
				Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);

		VerticalLayout VLayout = new VerticalLayout(layout, btnLayout);
		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		VLayout.setWidth("1000px");
		VLayout.setMargin(true);
		VLayout.setSpacing(true);
		setContent(VLayout);

	}
	
	public boolean isValid() {
		boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		
		 if (!this.binder.isValid()) {
			hasError = true;
			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					errorMessages.add(errMsg.getFormattedHtmlMessage());
				}
			}
		} else {
			try {
				this.binder.commit();

			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		showOrHideValidation(false);
		return !hasError;
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}
	
	/*private void showInPopup(Layout layout, ConfirmDialog dialog) {
		dialog.setCaption("");
		dialog.setClosable(true);

		Panel panel = new Panel();
		panel.setHeight("400px");
		panel.setContent(layout);
		dialog.setContent(panel);

		dialog.setResizable(false);
		dialog.setModal(true);

		dialog.show(getUI().getCurrent(), null, true);

	}*/

	
	protected void showOrHideValidation(Boolean isVisible) {
		if(null != mandatoryFields){
			for (Component component : mandatoryFields) {
				AbstractField<?> field = (AbstractField<?>) component;
				if (field != null) {
					field.setRequired(!isVisible);
					field.setValidationVisible(isVisible);
				}
			}
		}
	}


	private void showErrorPopup(String eMsg) {
		/*Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Alert");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createAlertBox(eMsg, buttonsNamewithType);
	}

	private Button getCancelButton(final ClaimRequestFileUploadUI dialog) {
		cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(null != bean.getIsParallelInvFvrQuery() && bean.getIsParallelInvFvrQuery()){
					bean.setStatusKey(bean.getParallelStatusKey());
				}
				binder = null;
			}
		});
		return cancelButton;
	}
	
	private void unbindField(Field<?> field) {
		if (field != null)
		{
			Object propertyId = this.binder.getPropertyId(field);
			if(propertyId != null) {
				this.binder.unbind(field);	
			}
			
		}
	}
	
	
public  void escalationRemarksListener(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForMAEscalateRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForMedicalRemarks(searchField, getShortCutListenerForMedicalRemarks(searchField));
	    
	  }
	
	public  void handleShortcutForMedicalRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);
				
			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}
	
	private ShortcutListener getShortCutListenerForMedicalRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();
				final Window dialog = new Window();
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				txtArea.setData(bean);
				//txtArea.setStyleName("Boldstyle");
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				//txtArea.setSizeFull();
				
				
				if(("escalateRmrks").equalsIgnoreCase(txtFld.getId()) || ("splRmrks").equalsIgnoreCase(txtFld.getId())){
					txtArea.setRows(25);
					txtArea.setHeight("30%");
					txtArea.setWidth("100%");
					dialog.setHeight("75%");
			    	dialog.setWidth("65%");
					txtArea.setReadOnly(false);
				}
				
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						if(("escalateRmrks").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
							PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
							mainDto.getPreauthMedicalDecisionDetails().setEscalateRemarks(txtFld.getValue());
						}else if(("splRmrks").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
							PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
							mainDto.getPreauthMedicalDecisionDetails().setReasonForRefering(txtFld.getValue());
						}
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				
				String strCaption = "";
				
				if(("escalateRmrks").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Escalate Remarks";
				}
			    else if(("splRmrks").equalsIgnoreCase(txtFld.getId())){
			    	strCaption = "Reason for Referring";
			    }
			   				    	
				dialog.setCaption(strCaption);
						
				
				dialog.setClosable(true);
				
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);
				
				dialog.addCloseListener(new Window.CloseListener() {
					
					@Override
					public void windowClose(CloseEvent e) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}
	
	public boolean isValidSpecialist(ReimbursementService reimbService) {
		boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		
		Boolean isParallelProcessHappen = reimbService.isFvrOrInvesOrQueryInitiated(bean);
		
		if(null != bean.getScreenName() && ((!(SHAConstants.WAIT_FOR_INPUT_SCREEN.equals(bean.getScreenName())) &&
				!isParallelProcessHappen) || (SHAConstants.WAIT_FOR_INPUT_SCREEN.equals(bean.getScreenName()))))
		{
			if (this.binder == null) {
				hasError = true;
				errorMessages.add("Please select Approve or Reject or Escalate Claim or Refer to coordinator. </br"
						+ ">");
				return !hasError;
			}
		}
		else {
			try {
				if(null != binder){
					this.binder.commit();
				}

			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		showOrHideValidation(false);
		return !hasError;
	}
}
