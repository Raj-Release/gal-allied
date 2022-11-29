package com.shaic.claim.rod.wizard.tables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Properties;
import java.util.WeakHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.outpatient.processOP.pages.assessmentsheet.OPClaimAssessmentPagePresenter;
import com.shaic.claim.outpatient.registerclaim.pages.rodanduploadandbillentry.OPRODAndBillEntryPagePresenter;
import com.shaic.claim.rod.billing.pages.BillingWorksheetUploadDocumentsPresenter;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryUploadDocumentsPresenter;
import com.shaic.claim.rod.wizard.pages.CreateRODUploadDocumentsPresenter;
import com.shaic.claims.reibursement.addaditionaldocuments.UploadDocumentsPresenter;
import com.shaic.paclaim.addAdditinalDocument.search.PAAddAdditionalDocUploadDocumentsPresenter;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.paclaim.rod.createrod.search.PACreateRODUploadDocumentsPresenter;
import com.shaic.paclaim.rod.enterbilldetails.search.PABillentryUploadDocumentsPresenter;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class UploadDocumentGridForm extends ViewComponent implements Receiver,
		SucceededListener {
	private VerticalLayout mainaLayout;
	private Panel panel;
	private Button btnUpload;
	private Upload upload;
	private ComboBox cbxFileType;
	private TextField txtBillNo;
	private PopupDateField billDate;
	private TextField txtNoOfItem;
	private TextField txtBillValue;
	private BeanFieldGroup<UploadDocumentDTO> binder;
	private File file;
	private String presenterString;
	private UploadDocumentDTO bean;
	
	private TextField txtBillingWorksheetRemarks;
	
	//private String presenterString;

	public static String dataDir = System.getProperty("jboss.server.data.dir");
	public String BYPASS_UPLOAD;

	public void init(UploadDocumentDTO bean, String presenterString) {
		this.bean = bean;
		this.presenterString = presenterString;
//		setSizeFull();
		setCompositionRoot(mainLayout());
	}
	
	

	public void initBinder() {
		this.binder = new BeanFieldGroup<UploadDocumentDTO>(
				UploadDocumentDTO.class);
		this.binder.setItemDataSource(this.bean);

	}

	private VerticalLayout mainLayout() {
		initBinder();
		readConnectionPropertyFile();

		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		mainaLayout = new VerticalLayout();
		btnUpload = new Button("Upload");
		btnUpload.setWidth("-1px");
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		upload = new Upload("", this);
		upload.setWidth("10%");
		upload.addSucceededListener(this);
		upload.setButtonCaption(null);
		
		
//		mainaLayout.setWidth("100%");
		
		if((SHAConstants.BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString))
		{
			panel = buildUploadDocLayoutFromZonal();
			//panel.setSpacing(false);
			mainaLayout.addComponent(panel);
			mainaLayout.addComponent(btnUpload);
			mainaLayout.setComponentAlignment(btnUpload, Alignment.MIDDLE_CENTER);
			//mainaLayout.addComponent(gridLayout);
			//mainaLayout.addComponent(btnUpload);
			//mainaLayout.setComponentAlignment(btnUpload, Alignment.MIDDLE_RIGHT);
		}
		else {
			panel = buildUploadDocLayoutTillBillEntry();
		//	panel.setSpacing(false);
			mainaLayout.addComponent(panel);
			mainaLayout.addComponent(btnUpload);
			mainaLayout.setComponentAlignment(btnUpload, Alignment.MIDDLE_RIGHT);
			//mainaLayout.setComponentAlignment(gridLayout, Alignment.MIDDLE_CENTER);
		}
		
		

		
		

		
		// mainaLayout.setCaption("Upload Documents");

		addListener();

		return mainaLayout;
	}
	
	private Panel buildUploadDocLayoutTillBillEntry()
	{
		txtBillNo = binder.buildAndBind("", "billNo", TextField.class);

		CSValidator billNoValidator = new CSValidator();
		billNoValidator.extend(txtBillNo);
		billNoValidator.setRegExp("^[a-zA-Z0-9]*$");
		billNoValidator.setPreventInvalidTyping(true);
		txtBillNo.setMaxLength(20);
		txtBillNo.setNullRepresentation("");

		billDate = binder.buildAndBind("", "billDate", PopupDateField.class);
		billDate.setValidationVisible(false);
		billDate.setTextFieldEnabled(false);
		

		txtNoOfItem = binder.buildAndBind("", "noOfItems", TextField.class);
		txtNoOfItem.setMaxLength(5);
		txtNoOfItem.setNullRepresentation("");
		CSValidator noOfItemValidator = new CSValidator();
		noOfItemValidator.extend(txtNoOfItem);
		noOfItemValidator.setRegExp("^[0-9]*$");
		noOfItemValidator.setPreventInvalidTyping(true);

		txtBillValue = binder.buildAndBind("", "billValue", TextField.class);

		CSValidator billValueValidator = new CSValidator();
		billValueValidator.extend(txtBillValue);
		billValueValidator.setRegExp("^[0-9]*$");
		billValueValidator.setPreventInvalidTyping(true);
		txtBillValue.setMaxLength(7);
		txtBillValue.setNullRepresentation("");

		cbxFileType = binder.buildAndBind("", "fileType", ComboBox.class);
		
		GridLayout gridLayout = new GridLayout(6, 2);
		gridLayout.setColumnExpandRatio(0, 50.0f);
		gridLayout.addComponent(new Label("File Upload"), 0, 0);
		gridLayout.addComponent(upload, 0, 1);

		gridLayout.addComponent(new Label("File Type"), 1, 0);
		gridLayout.addComponent(cbxFileType, 1, 1);

		gridLayout.addComponent(new Label("Bill No"), 2, 0);
		gridLayout.addComponent(txtBillNo, 2, 1);

		gridLayout.addComponent(new Label("Bill Date"), 3, 0);
		gridLayout.addComponent(billDate, 3, 1);

		gridLayout.addComponent(new Label("No of Items"), 4, 0);
		gridLayout.addComponent(txtNoOfItem, 4, 1);

		gridLayout.addComponent(new Label("Bill Value"), 5, 0);
		gridLayout.addComponent(txtBillValue, 5, 1);
		gridLayout.setCaption("Upload Documents");
		
		Panel panel = new Panel();
		panel.setContent(gridLayout);
		
		return panel;
		
	}
	
	private Panel buildUploadDocLayoutFromZonal()
	{
		txtBillingWorksheetRemarks = binder.buildAndBind("", "billingWorkSheetRemarks", TextField.class);
		txtBillingWorksheetRemarks.setMaxLength(200);
		
		/*CSValidator validator = new CSValidator();
		validator.extend(txtBillingWorksheetRemarks);
		validator.setRegExp("^[a-z A-Z 0-9 .]*$");
		validator.setPreventInvalidTyping(true);*/
		
		GridLayout gridLayout = new GridLayout(3, 3);
		gridLayout.setWidth("100%");
		gridLayout.addComponent(new Label("File Upload"), 0, 0);
		gridLayout.addComponent(upload, 0, 1);

		gridLayout.addComponent(new Label("Remarks"), 1, 0);
		gridLayout.addComponent(txtBillingWorksheetRemarks, 1, 1);
		
		/*gridLayout.addComponent(new Label("Upload"), 2,0);
		gridLayout.addComponent(new HorizontalLayout(btnUpload), 2,1);*/
		Panel panel = new Panel();
		panel.setContent(gridLayout);
		
		return panel;
	}

	public void setFileTypeValues(BeanItemContainer<SelectValue> parameter) {
		// this.parameter = parameter;
		cbxFileType.setContainerDataSource(parameter);
		cbxFileType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxFileType.setItemCaptionPropertyId("value");

	}

	public void setValueFromTable(BeanItemContainer<SelectValue> parameter,
			String value) {
		for (int i = 0; i < parameter.size(); i++) {
			if ((value).equalsIgnoreCase(parameter.getIdByIndex(i).getValue())) {
				this.cbxFileType.setValue(parameter.getIdByIndex(i));
			}
		}
	}

	protected void addListener() {

		btnUpload.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				if (isValid()) {
					System.out.println("UPlaod Action Called"+bean.getRodNo());
					if (null != BYPASS_UPLOAD
							&& !("").equalsIgnoreCase(BYPASS_UPLOAD)
							&& ("false").equalsIgnoreCase(BYPASS_UPLOAD)) {
						upload.submitUpload();
					}

					else {
						try {
							System.out.println("Before submit upload"+bean.getRodNo());
							upload.submitUpload();
							System.out.println("Before commit"+bean.getRodNo());
							binder.commit();
							System.out.println("presenterString"+bean.getRodNo()+presenterString);
							if(null != bean.getIsEdit() && !bean.getIsEdit())
								bean.setIsEdit(false);
							if (("Create ROD").equalsIgnoreCase(presenterString)) {
								fireViewEvent(
										CreateRODUploadDocumentsPresenter.SUBMIT_UPLOADED_DOCUMENTS,
										bean);
							} else if (("Bill Entry")
									.equalsIgnoreCase(presenterString)) {
								System.out.println("UPlaod Action Called before presenter"+bean.getRodNo());
								fireViewEvent(
										BillEntryUploadDocumentsPresenter.BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS,
										bean);
							} else if (("Add Additional Documents")
									.equalsIgnoreCase(presenterString)) {
								fireViewEvent(
										UploadDocumentsPresenter.BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS,
										bean);
							}  else if ((SHAConstants.OUTPATIENT_FLAG) .equalsIgnoreCase(presenterString)) {
//								fireViewEvent(OPRODAndBillEntryPagePresenter.BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS, bean);
								fireViewEvent(OPClaimAssessmentPagePresenter.OP_BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS, bean);
							} 
							else if((SHAConstants.BILLING_WORKSHEET).equalsIgnoreCase(presenterString))
							{
								fireViewEvent(
										BillingWorksheetUploadDocumentsPresenter.SUBMIT_BILLING_UPLOAD_DOCUMENTS,
										bean); 
							}
							else if ((SHAConstants.CREATE_ROD_PA).equalsIgnoreCase(presenterString)) {
								fireViewEvent(
										PACreateRODUploadDocumentsPresenter.SUBMIT_UPLOADED_DOCUMENTS,
										bean);
							}
							else if (("PA Add Additional Documents").equalsIgnoreCase(presenterString)) {
								fireViewEvent(PAAddAdditionalDocUploadDocumentsPresenter.ADD_ADDITIONAL_SUBMIT_UPLOADED_DOCUMENTS, bean);
							}else if (("PA Bill Entry").equalsIgnoreCase(presenterString)) {
								fireViewEvent(PABillentryUploadDocumentsPresenter.BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS, bean);
							}
							
							/*fireViewEvent(
									CreateRODUploadDocumentsPresenter.SUBMIT_UPLOADED_DOCUMENTS,
									bean);*/
						} catch (CommitException ce) {
							ce.printStackTrace();
						}
					}

				}
			}
		});

		if(null != cbxFileType)
		{
			cbxFileType
					.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
						private static final long serialVersionUID = 1L;
	
						@Override
						public void valueChange(ValueChangeEvent event) {
	
							SelectValue selectValue = (SelectValue) event
									.getProperty().getValue();
							if (null != selectValue) {
								if (null != selectValue.getValue()
										&& !selectValue.getValue().contains("Bill")) {
									editFeilds(false);
								} else {
									editFeilds(true);
								}
							}
	
						}
					});
		}
	}

	private Boolean isValid() {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();

		/*if(!((SHAConstants.ZONAL_REVIEW_BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString) || (SHAConstants.CLAIM_REQUEST_BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString) 
				|| (SHAConstants.CLAIM_BILLING_BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString) || (SHAConstants.CLAIM_FINANCIAL_BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString)))*/
		if(!(SHAConstants.BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString))
		{
				
			if ((null != cbxFileType)) {
				SelectValue selValue = (SelectValue) cbxFileType.getValue();
				if ((null == selValue || selValue != null
						&& ("").equalsIgnoreCase(selValue.getValue()))) {
					hasError = true;
					eMsg.append("Please Select File Type to be uploaded </br>");
				}
			}
	
			if (txtBillNo.isEnabled()) {
				if (!(null != txtBillNo && !("").equalsIgnoreCase(txtBillNo
						.getValue()))) {
					hasError = true;
					eMsg.append("Please Enter  Bill No </br>");
				}
			}
	
			if (billDate.isEnabled()) {
				if (!(null != billDate && null != billDate.getValue())) {
					hasError = true;
					eMsg.append("Please Enter Bill Date </br>");
				}
			}
			if (txtNoOfItem.isEnabled()) {
				if (null != txtNoOfItem
						&& (null == txtNoOfItem.getValue()
								|| ("").equalsIgnoreCase(txtNoOfItem.getValue())
								|| ("0").equalsIgnoreCase(txtNoOfItem.getValue()) || ("null")
									.equalsIgnoreCase(txtNoOfItem.getValue()))) {
					hasError = true;
					eMsg.append("Please Enter Bill Items </br>");
				}
				/*
				 * if(!(null != txtNoOfItem &&
				 * (!("").equalsIgnoreCase(txtNoOfItem.getValue())) &&
				 * (!("0").equalsIgnoreCase(txtNoOfItem.getValue())) &&
				 * (!("null").equalsIgnoreCase(txtNoOfItem.getValue())))) {
				 * 
				 * }
				 */
			}
			if (txtBillValue.isEnabled()) {
				if (null != txtBillValue
						&& (null == txtBillValue.getValue()
								|| ("").equalsIgnoreCase(txtBillValue.getValue())
								|| ("0").equalsIgnoreCase(txtBillValue.getValue()) || ("null")
									.equalsIgnoreCase(txtBillValue.getValue())))
				// if(!(null != txtBillValue &&
				// (!("").equalsIgnoreCase(txtBillValue.getValue())) &&
				// (!("0").equalsIgnoreCase(txtBillValue.getValue()))))
				{
					hasError = true;
					eMsg.append("Please Enter  Bill Value </br>");
				}
			}
		}
		else 
		{
			
			
			
			if(!(null != txtBillingWorksheetRemarks && null != txtBillingWorksheetRemarks.getValue() && !txtBillingWorksheetRemarks.getValue().isEmpty() && !("").equalsIgnoreCase(txtBillingWorksheetRemarks.getValue().trim()) ))
			{
				hasError = true;
				eMsg.append("Please Enter Remarks </br>");
			}
		}

		if (hasError) {
			/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
			hasError = true;
			return !hasError;
		} else {
			return true;
		}
	}

	private void editFeilds(Boolean value) {
		txtBillNo.setEnabled(value);
		billDate.setEnabled(value);
		txtNoOfItem.setEnabled(value);
		txtBillValue.setEnabled(value);
		if (!value) {
			txtBillNo.setValue(null);
			billDate.setValue(null);
			txtNoOfItem.setValue(null);
			txtBillValue.setValue(null);
		}
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		try {
			if(file.exists() && !file.isDirectory()) { 
			byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
			String fileName = event.getFilename();
			if (null != fileAsbyteArray) {
				//file gets uploaded in data directory when code comes here.
				if(null != event && null != event.getFilename() 
						&& (event.getFilename().toLowerCase().endsWith("jpg") || event.getFilename().toLowerCase().endsWith("jpeg"))){
					File convertedFile  = SHAFileUtils.convertImageToPDF(event.getFilename());
					fileName = convertedFile.getName();
					fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);
				}

				//Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
				//boolean hasSpecialChar = p.matcher(event.getFilename()).find();
				// if(hasSpecialChar)
				// {
				
				WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(fileName, fileAsbyteArray);
				Boolean flagUploadSuccess = new Boolean(StringUtils.EMPTY + uploadStatus.get("status"));
				
				// TO read file after load
				if (flagUploadSuccess.booleanValue()) {
					String token = StringUtils.EMPTY + uploadStatus.get("fileKey");
					System.out.println("the token value & file -----" + token + " : " +  event.getFilename());
					// String token = "290";
					// System.out.println("----the uploadStatusMap---"+uploadStatus);
					this.bean.setDmsDocToken(token);
					this.bean.setFileName(fileName);
					this.bean.setCreatedBy((String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID));
					this.bean.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					binder.commit();

					if(null != bean.getIsEdit() && !bean.getIsEdit())
						bean.setIsEdit(false);
					
					if (null != bean.getFileType() && null != bean.getFileType().getValue()) {
						if (("Create ROD").equalsIgnoreCase(presenterString)) {
							fireViewEvent(CreateRODUploadDocumentsPresenter.SUBMIT_UPLOADED_DOCUMENTS, bean);
						} else if (("Bill Entry").equalsIgnoreCase(presenterString)) {
							fireViewEvent(BillEntryUploadDocumentsPresenter.BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS, bean);
						} else if (("Add Additional Documents").equalsIgnoreCase(presenterString)) {
							fireViewEvent(UploadDocumentsPresenter.BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS, bean);
						} else if ((SHAConstants.OUTPATIENT_FLAG).equalsIgnoreCase(presenterString)) {
//							fireViewEvent(OPRODAndBillEntryPagePresenter.BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS, bean);
							fireViewEvent(OPClaimAssessmentPagePresenter.OP_BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS, bean);
						} 

						 else if (("PA Create ROD").equalsIgnoreCase(presenterString)) {
								fireViewEvent(
										PACreateRODUploadDocumentsPresenter.SUBMIT_UPLOADED_DOCUMENTS,
										bean);
							} 
						 else if (("PA Add Additional Documents").equalsIgnoreCase(presenterString)) {
								fireViewEvent(PAAddAdditionalDocUploadDocumentsPresenter.ADD_ADDITIONAL_SUBMIT_UPLOADED_DOCUMENTS, bean);
							}
						
						 else if (("PA Bill Entry").equalsIgnoreCase(presenterString)) {
								fireViewEvent(PABillentryUploadDocumentsPresenter.BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS, bean);
							}
						
							
					}
					else if((SHAConstants.BILLING_WORKSHEET).equalsIgnoreCase(presenterString))
					{
							fireViewEvent(
									BillingWorksheetUploadDocumentsPresenter.SUBMIT_BILLING_UPLOAD_DOCUMENTS,
									bean); 
					} 
					else {
							/*Label label = new Label(
									"Please upload one document before pressing upload button",
									ContentMode.HTML);
							label.setStyleName("errMessage");
							VerticalLayout layout = new VerticalLayout();
							layout.setMargin(true);
							layout.addComponent(label);
							ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(true);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);*/
							
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox("Please upload one document before pressing upload button", buttonsNamewithType);
						}
				}
				// else
				//{
					// Notification.show("Error", "" +
					// uploadStatus.get("message"), Type.ERROR_MESSAGE);
				//}
				// }
				/*
				 * else { Label label = new Label(
				 * "File name with special characters can't be uploaded. Please remove the special characters from file name to proceed."
				 * , ContentMode.HTML); label.setStyleName("errMessage");
				 * VerticalLayout layout = new VerticalLayout();
				 * layout.setMargin(true); layout.addComponent(label);
				 * ConfirmDialog dialog = new ConfirmDialog();
				 * dialog.setCaption("Errors"); dialog.setClosable(true);
				 * dialog.setContent(layout); dialog.setResizable(true);
				 * dialog.setModal(true); dialog.show(getUI().getCurrent(),
				 * null, true); }
				 */
				//for clearing heap
				fileAsbyteArray = null;
				fileName = null;
				SHAUtils.setClearReferenceData(uploadStatus);
			} }else {
				Notification.show("Error", StringUtils.EMPTY + "Please select the file to be uploaded", Type.ERROR_MESSAGE);
			}
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (CommitException ce) {
			ce.printStackTrace();
		}
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {

		FileOutputStream fos = null;
		try {
			this.file = new File(System.getProperty("jboss.server.data.dir")
					+ File.separator + filename);
			if (null != file /*&& !StringUtils.isBlank(filename)*/) {
				fos = new FileOutputStream(file);
			} else {
				Notification.show("Error", ""
						+ "Please select the file to be uploaded",
						Type.ERROR_MESSAGE);
			}
		} catch (FileNotFoundException e) {/* comented for error handled
			Notification.show("Error", ""
			+ "Please select the file to be uploaded",
			Type.ERROR_MESSAGE);*/
			e.printStackTrace();}
		/*finally{
			if(null != fos){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}*/
		if(fos == null){
			try {
				fos = new FileOutputStream("DUMMY");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fos;
	}

	//public static Properties readConnectionPropertyFile() {
	public  Properties readConnectionPropertyFile() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(dataDir + "/" + "connection.properties");
			prop.load(input);

			BYPASS_UPLOAD = prop.getProperty("bypass_upload");
			return prop;

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public void disableFileUploadComponent()
	{
		if(null != upload)
		{
			upload.setEnabled(false);
			BYPASS_UPLOAD = "true";
		}
	}

}
