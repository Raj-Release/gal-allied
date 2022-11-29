package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.ejb.EJB;

import org.apache.commons.io.FileUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
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
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class OMPUploadDocumentGridForm extends ViewComponent implements Receiver,
		SucceededListener {
	private VerticalLayout mainaLayout;
	private Panel panel;
	private Button btnUpload;
	private Upload upload;
	private ComboBox cbxFileType;
	private ComboBox cmbDocMnts;
	private DateField docRecivDate;
	private ComboBox recivedStatus;
	private TextField txtNoofDocs;
	private TextField txtrmarks;
	private BeanFieldGroup<UploadDocumentDTO> binder;
	private File file;
	private String presenterString;
	private UploadDocumentDTO bean;
	
	private TextField txtBillingWorksheetRemarks;
	private String filename;
	
	@EJB
	private MasterService masterService;
	
	//private String presenterString;

	public static String dataDir = System.getProperty("jboss.server.data.dir");
	public static String BYPASS_UPLOAD;

	public void init(UploadDocumentDTO bean, String presenterString) {
		BeanItemContainer<SelectValue> fileTypeContainer = bean.getFileTypeContainer();
		//this.bean = new UploadDocumentDTO();
		this.bean = bean;
		this.presenterString = presenterString;
		filename = new String();
		file = null;
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
		mainaLayout.setWidth("100%");
		btnUpload = new Button("Upload");
		btnUpload.setWidth("-1px");
		//Vaadin8-setImmediate() btnUpload.setImmediate(true);
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		upload = new Upload("", this);
		upload.setWidth("10%");
		upload.addSucceededListener(this);
		upload.setButtonCaption(null);		
		
//		mainaLayout.setWidth("100%");
		
//		if((SHAConstants.BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString))
//		{
//			panel = buildUploadDocLayoutFromZonal();
//			//panel.setSpacing(false);
//			mainaLayout.addComponent(panel);
//			mainaLayout.addComponent(btnUpload);
//			mainaLayout.setComponentAlignment(btnUpload, Alignment.MIDDLE_CENTER);
//			//mainaLayout.addComponent(gridLayout);
//			//mainaLayout.addComponent(btnUpload);
//			//mainaLayout.setComponentAlignment(btnUpload, Alignment.MIDDLE_RIGHT);
//		}
//		else {
			panel = buildUploadDocLayoutTillBillEntry();
		//	panel.setSpacing(false);
			mainaLayout.addComponent(panel);
			mainaLayout.addComponent(btnUpload);
			mainaLayout.setComponentAlignment(btnUpload, Alignment.MIDDLE_RIGHT);
			
//			mainaLayout.setComponentAlignment(gridLayout, Alignment.MIDDLE_CENTER);
//		}
			
		// mainaLayout.setCaption("Upload Documents");

		addListener();

		return mainaLayout;
	}
	
	private Panel buildUploadDocLayoutTillBillEntry()
	{
/*		txtBillNo = binder.buildAndBind("", "billNo", TextField.class);

		CSValidator billNoValidator = new CSValidator();
		billNoValidator.extend(txtBillNo);
		billNoValidator.setRegExp("^[a-zA-Z0-9]*$");
		billNoValidator.setPreventInvalidTyping(true);
		txtBillNo.setMaxLength(20);
		txtBillNo.setNullRepresentation("");

		billDate = binder.buildAndBind("", "billDate", DateField.class);
		billDate.setValidationVisible(false);

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
		txtBillValue.setNullRepresentation("");*/
		
		cmbDocMnts = binder.buildAndBind("", "documentType", ComboBox.class);
		
		

		cbxFileType = binder.buildAndBind("", "fileType", ComboBox.class);
//		if(bean.getFileTypeContainer()!=null){
//		BeanItemContainer<SelectValue> ompcbxFileType = masterService.getConversionReasonByValue(ReferenceTable.OMP_UPLOAD_FILETYPE);
		BeanItemContainer<SelectValue> ompcbxFileType = masterService.getOMPFileTypes();
		cbxFileType.setContainerDataSource(ompcbxFileType);
		cbxFileType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxFileType.setItemCaptionPropertyId("value");
//		}
		cbxFileType.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue fileTyp = (SelectValue) event.getProperty().getValue();
				if(fileTyp != null){
//					BeanItemContainer<SelectValue> ompcmbDocMnts = masterService.getDocumentCheckListValuesContainer(ReferenceTable.OMP_UPLOAD_DOCLIST);
					BeanItemContainer<SelectValue> ompcmbDocMnts = masterService.getOMPDocumentTypes(fileTyp.getId());
					cmbDocMnts.setContainerDataSource(ompcmbDocMnts);
					cmbDocMnts.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbDocMnts.setItemCaptionPropertyId("value");
				}else{
					cmbDocMnts.setContainerDataSource(null);
					cmbDocMnts.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbDocMnts.setItemCaptionPropertyId("value");
				}
			}
		});
		
		docRecivDate = binder.buildAndBind("", "docReceivedDate", DateField.class);
		
		recivedStatus = binder.buildAndBind("", "receivStatus", ComboBox.class);
		BeanItemContainer<SelectValue> omprecivedStatus = masterService.getConversionReasonByValue(ReferenceTable.OMP_RECIVED_STATUS);
		recivedStatus.setContainerDataSource(omprecivedStatus);
		recivedStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		recivedStatus.setItemCaptionPropertyId("value");
		txtNoofDocs = binder.buildAndBind("", "noOfItems", TextField.class);
		
		txtrmarks = binder.buildAndBind("", "remarks", TextField.class);
		
		GridLayout gridLayout = new GridLayout(7, 2);
		gridLayout.setColumnExpandRatio(0, 50.0f);
		gridLayout.addComponent(new Label("File Upload"), 0, 0);
		gridLayout.addComponent(upload, 0, 1);

		gridLayout.addComponent(new Label("File Type"), 1, 0);
		gridLayout.addComponent(cbxFileType, 1, 1);

		gridLayout.addComponent(new Label("Documents"), 2, 0);
		gridLayout.addComponent(cmbDocMnts, 2, 1);

		gridLayout.addComponent(new Label("Document Received Date"), 3, 0);
		gridLayout.addComponent(docRecivDate, 3, 1);

		gridLayout.addComponent(new Label("Recieved Status"), 4, 0);
		gridLayout.addComponent(recivedStatus, 4, 1);

		gridLayout.addComponent(new Label("No of Documents"), 5, 0);
		gridLayout.addComponent(txtNoofDocs, 5, 1);
		
		gridLayout.addComponent(new Label("Remarks"), 6, 0);
		gridLayout.addComponent(txtrmarks, 6, 1);
		
		gridLayout.setCaption("Upload Documents");
		
		Panel panel = new Panel();
		panel.setContent(gridLayout);
		
		return panel;
		
	}
	
//	private Panel buildUploadDocLayoutFromZonal()
//	{
//		txtBillingWorksheetRemarks = binder.buildAndBind("", "billingWorkSheetRemarks", TextField.class);
//		txtBillingWorksheetRemarks.setMaxLength(200);
//		
//		/*CSValidator validator = new CSValidator();
//		validator.extend(txtBillingWorksheetRemarks);
//		validator.setRegExp("^[a-z A-Z 0-9 .]*$");
//		validator.setPreventInvalidTyping(true);*/
//		
//		GridLayout gridLayout = new GridLayout(3, 3);
//		gridLayout.setWidth("100%");
//		gridLayout.addComponent(new Label("File Upload"), 0, 0);
//		gridLayout.addComponent(upload, 0, 1);
//
//		gridLayout.addComponent(new Label("Remarks"), 1, 0);
//		gridLayout.addComponent(txtBillingWorksheetRemarks, 1, 1);
//		
//		/*gridLayout.addComponent(new Label("Upload"), 2,0);
//		gridLayout.addComponent(new HorizontalLayout(btnUpload), 2,1);*/
//		Panel panel = new Panel();
//		panel.setContent(gridLayout);
//		
//		return panel;
//	}

	public void setFileTypeValues(BeanItemContainer<SelectValue> parameter) {
		// this.parameter = parameter;
//		BeanItemContainer<SelectValue> parameter1 = masterService.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE);
	/*	cbxFileType.setContainerDataSource(parameter);
		cbxFileType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxFileType.setItemCaptionPropertyId("value");*/
		//cbxFileType.setValue(null);

	}

	public void setValueFromTable(BeanItemContainer<SelectValue> parameter,
			String value) {
		if(parameter!=null){
		for (int i = 0; i < parameter.size(); i++) {
			if ((value).equalsIgnoreCase(parameter.getIdByIndex(i).getValue())) {
				this.cbxFileType.setValue(parameter.getIdByIndex(i));
			}
		}}
	}

	protected void addListener() {

		btnUpload.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				if (isValid()) {

					if (null != BYPASS_UPLOAD
							&& !("").equalsIgnoreCase(BYPASS_UPLOAD)
							&& ("true").equalsIgnoreCase(BYPASS_UPLOAD)) {
						upload.submitUpload();
					}

					else {
						try {
							upload.submitUpload();

							binder.commit();
							if(null != bean.getIsEdit() && !bean.getIsEdit())
								bean.setIsEdit(false);
							 if((SHAConstants.OMP_ROD).equalsIgnoreCase(presenterString))
							{
								bean.setFileName(filename);
								if(file!=null && filename !=null && filename.length()>0){
								/*fireViewEvent(
										OMPUploadDocumentsPresenter.OMP_SUBMIT_UPLOADED_DOCUMENTS,
										bean); */
								}
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
		String eMsg = "";

		/*if(!((SHAConstants.ZONAL_REVIEW_BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString) || (SHAConstants.CLAIM_REQUEST_BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString) 
				|| (SHAConstants.CLAIM_BILLING_BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString) || (SHAConstants.CLAIM_FINANCIAL_BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString)))*/
		if(!(SHAConstants.BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString))
		{
				
			if ((null != cbxFileType)) {
				SelectValue selValue = (SelectValue) cbxFileType.getValue();
				if ((null == selValue || selValue != null
						&& ("").equalsIgnoreCase(selValue.getValue()))) {
					hasError = true;
					eMsg += "Please Select File Type to be uploaded </br>";
				}
			}
	
			/*if (txtBillNo.isEnabled()) {
				if (!(null != txtBillNo && !("").equalsIgnoreCase(txtBillNo
						.getValue()))) {
					hasError = true;
					eMsg += "Please Enter  Bill No </br>";
				}
			}
	
			if (billDate.isEnabled()) {
				if (!(null != billDate && null != billDate.getValue())) {
					hasError = true;
					eMsg += "Please Enter Bill Date </br>";
				}
			}
			if (txtNoOfItem.isEnabled()) {
				if (null != txtNoOfItem
						&& (null == txtNoOfItem.getValue()
								|| ("").equalsIgnoreCase(txtNoOfItem.getValue())
								|| ("0").equalsIgnoreCase(txtNoOfItem.getValue()) || ("null")
									.equalsIgnoreCase(txtNoOfItem.getValue()))) {
					hasError = true;
					eMsg += "Please Enter Bill Items </br>";
				}
				
				 * if(!(null != txtNoOfItem &&
				 * (!("").equalsIgnoreCase(txtNoOfItem.getValue())) &&
				 * (!("0").equalsIgnoreCase(txtNoOfItem.getValue())) &&
				 * (!("null").equalsIgnoreCase(txtNoOfItem.getValue())))) {
				 * 
				 * }
				 
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
					eMsg += "Please Enter  Bill Value </br>";
				}
			}*/
		}
		else 
		{
			
			
			
			if(!(null != txtBillingWorksheetRemarks && null != txtBillingWorksheetRemarks.getValue() && !txtBillingWorksheetRemarks.getValue().isEmpty() && !("").equalsIgnoreCase(txtBillingWorksheetRemarks.getValue().trim()) ))
			{
				hasError = true;
				eMsg += "Please Enter Remarks </br>";
			}
		}

		if (hasError) {
			Label label = new Label(eMsg, ContentMode.HTML);
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
			dialog.show(getUI().getCurrent(), null, true);
			hasError = true;
			return !hasError;
		} else {
			return true;
		}
	}

	private void editFeilds(Boolean value) {/*
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
	*/}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		try {
			//Added for error
			if(file.exists() && !file.isDirectory()) { 
			byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
			String fileName = event.getFilename();
			this.filename = fileName;
			if (null != fileAsbyteArray) {
				//file gets uploaded in data directory when code comes here.
				if(null != event && null != event.getFilename() && (event.getFilename().endsWith("jpg") || event.getFilename().endsWith("jpeg") ||
						event.getFilename().endsWith("JPG") || event.getFilename().endsWith("JPEG")))
				{
					File convertedFile  = SHAFileUtils.convertImageToPDF(event.getFilename());
					fileName = convertedFile.getName();
					fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);
				}

				Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
				boolean hasSpecialChar = p.matcher(event.getFilename()).find();
				// if(hasSpecialChar)
				// {
				WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(
						fileName, fileAsbyteArray);
				
				if(uploadStatus != null){
				Boolean flagUploadSuccess = new Boolean(""
						+ uploadStatus.get("status"));
				// TO read file after load
				if (flagUploadSuccess.booleanValue()) {
						String token = "" + uploadStatus.get("fileKey");
						
						SelectValue fileType = this.bean.getFileType();
						String fileTypeValue = this.bean.getFileTypeValue();
						// String token = "290";
						Long rodKey = this.bean.getRodKey();
						String intimationNo = this.bean.getIntimationNo();
						this.bean = new UploadDocumentDTO();
						bean.setRodKey(rodKey);
						bean.setIntimationNo(intimationNo);
						if(cbxFileType.getValue()!=null){
							SelectValue value = (SelectValue) cbxFileType.getValue();
							bean.setFileType(value);
						}
						if(txtrmarks!= null && txtrmarks.getValue()!= null){
							bean.setRemarks(txtrmarks.getValue());
						}
					/*	if(cmbDocMnts!=null && cmbDocMnts.getValue()!= null){
							SelectValue docValue = (SelectValue) cmbDocMnts.getValue();
							if(docValue!= null){
								bean.setDocumentType(docValue);
							}
						}*/
						/*if(recivedStatus!=null && recivedStatus.getValue()!= null){
							SelectValue statusValue =(SelectValue)recivedStatus.getValue();
							if(statusValue!= null){
								bean.setReceivStatus(statusValue);
							}
						}
						if(docRecivDate!= null && docRecivDate.getValue()!= null){
							bean.setDocReceivedDate(docRecivDate.getValue());
						}*/
						if(txtNoofDocs!= null&& txtNoofDocs.getValue()!= null){
							long noOfDoc =Long.parseLong(txtNoofDocs.getValue());
							bean.setNoOfItems(noOfDoc);
						}
						this.bean.setFileTypeValue(fileTypeValue);
						this.bean.setDmsDocToken(token);
						this.bean.setFileName(fileName);
						if(recivedStatus.getValue()!=null){
							SelectValue value = (SelectValue) recivedStatus.getValue();
							this.bean.setReceivStatus(value);
							this.bean.setReceivStatusValue(value.getValue());
						}
						if(cmbDocMnts.getValue()!=null){
							SelectValue value = (SelectValue) cmbDocMnts.getValue();
							this.bean.setDocumentType(value);
							this.bean.setDocumentTypeValue(value.getValue());
						}
						if(docRecivDate.getValue()!=null){
							this.bean.setDocReceivedDate(docRecivDate.getValue());
						}
						System.out.println("the token value-----" + token);
						System.out.println("the file name-----"
								+ event.getFilename());
						// System.out.println("----the uploadStatusMap---"+uploadStatus);
						binder.commit();
						if(null != bean.getIsEdit() && !bean.getIsEdit())
							bean.setIsEdit(false);
						if (null != bean.getFileType()
								&& null != bean.getFileType().getValue())
						{
								
							if((SHAConstants.OMP_ROD).equalsIgnoreCase(presenterString))
							{
									fireViewEvent(
											OMPUploadDocumentsPresenter.OMP_SUBMIT_UPLOADED_DOCUMENTS,
											bean); 
									clear();
							}
							
							else {
									Label label = new Label(
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
									dialog.show(getUI().getCurrent(), null, true);
								}
						}
				   }
				}
			    else
				{
			    	Label label = new Label(
							"Error has Occured while uploading the document Please Contact Support Team.",
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
					dialog.show(getUI().getCurrent(), null, true);
					// Notification.show("Error", "" +
					// uploadStatus.get("message"), Type.ERROR_MESSAGE);
				}
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
				uploadStatus = null;
			}
			}
			else
			{
				Notification.show("Error", ""
						+ "Please select the file to be uploaded",
						Type.ERROR_MESSAGE);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (CommitException ce) {

		}

	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {

		FileOutputStream fos = null;
		try {
			this.file = new File(System.getProperty("jboss.server.data.dir")
					+ "/" + filename);
			if (null != file) {
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
			e.printStackTrace();
			}
		//Added for error
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



	public void clear() {
		 if(cbxFileType!=null){
			 cbxFileType.setValue(null);
		 }
		 if(cmbDocMnts!=null){
			 cmbDocMnts.setValue(null);
		 }
		 if(docRecivDate!=null){
			 docRecivDate.setValue(null);
		 }
		 if(recivedStatus!=null){
			 recivedStatus.setValue(null);
		 }
		 if(txtNoofDocs!=null){
			 txtNoofDocs.setValue(null);
		 }
		 if(txtrmarks!=null){
			 txtrmarks.setValue(null);
		 }
		 if(file!=null){
			 file =null;
		 }
		 if(filename!=null){
			 filename =null;
		 }
	}

}
