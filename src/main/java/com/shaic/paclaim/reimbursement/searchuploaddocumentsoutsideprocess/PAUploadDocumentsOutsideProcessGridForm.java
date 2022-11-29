package com.shaic.paclaim.reimbursement.searchuploaddocumentsoutsideprocess;

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

import org.apache.commons.io.FileUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.data.Property.ValueChangeEvent;
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

public class PAUploadDocumentsOutsideProcessGridForm extends ViewComponent implements Receiver,SucceededListener{



	private VerticalLayout mainaLayout;
	private Panel panel;
	private Button btnUpload;
	private Upload upload;
	private ComboBox cbxFileType;
	private TextField txtBillNo;
	private DateField billDate;
	private TextField txtNoOfItem;
	private TextField txtBillValue;
	private BeanFieldGroup<UploadDocumentDTO> binder;
	private File file;
	private String presenterString;
	private UploadDocumentDTO bean;
	
	private TextField txtBillingWorksheetRemarks;
	
	private ComboBox cbxReferenceNo;
	
	//private String presenterString;

	public static String dataDir = System.getProperty("jboss.server.data.dir");
	public static String BYPASS_UPLOAD;

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
		
		panel = buildUploadDocLayout();
		mainaLayout.addComponent(panel);
		mainaLayout.addComponent(btnUpload);
		mainaLayout.setComponentAlignment(btnUpload, Alignment.MIDDLE_RIGHT);


		addListener();

		return mainaLayout;
	}
	
	private Panel buildUploadDocLayout()
	{
		
		
		cbxReferenceNo = binder.buildAndBind("", "referenceNo", ComboBox.class);
		cbxFileType = binder.buildAndBind("", "fileType", ComboBox.class);
		
		GridLayout gridLayout = new GridLayout(6, 2);
		gridLayout.setColumnExpandRatio(0, 50.0f);
		
		gridLayout.addComponent(new Label("ROD NO/Reference No"), 0, 0);
		gridLayout.addComponent(cbxReferenceNo, 0, 1);
		
		gridLayout.addComponent(new Label("File Upload"), 1, 0);
		gridLayout.addComponent(upload, 1, 1);

		gridLayout.addComponent(new Label("File Type"), 2, 0);
		gridLayout.addComponent(cbxFileType, 2, 1);

/*		gridLayout.addComponent(new Label("Bill No"), 2, 0);
		gridLayout.addComponent(txtBillNo, 2, 1);

		gridLayout.addComponent(new Label("Bill Date"), 3, 0);
		gridLayout.addComponent(billDate, 3, 1);

		gridLayout.addComponent(new Label("No of Items"), 4, 0);
		gridLayout.addComponent(txtNoOfItem, 4, 1);

		gridLayout.addComponent(new Label("Bill Value"), 5, 0);
		gridLayout.addComponent(txtBillValue, 5, 1);*/
		gridLayout.setCaption("Upload Documents");
		
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
	
	public void setReferencNoValues(BeanItemContainer<SelectValue> parameter) {
		// this.parameter = parameter;
		cbxReferenceNo.setContainerDataSource(parameter);
		cbxReferenceNo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxReferenceNo.setItemCaptionPropertyId("value");

	}

	public void setValueFromTable(BeanItemContainer<SelectValue> parameter,
			String value) {
		for (int i = 0; i < parameter.size(); i++) {
			if ((value).equalsIgnoreCase(parameter.getIdByIndex(i).getValue())) {
				this.cbxFileType.setValue(parameter.getIdByIndex(i));
			}
		}
	}
	
	public void setValueFromTableForReferenceNo(BeanItemContainer<SelectValue> parameter,
			String value) {
		for (int i = 0; i < parameter.size(); i++) {
			if ((value).equalsIgnoreCase(parameter.getIdByIndex(i).getValue())) {
				this.cbxReferenceNo.setValue(parameter.getIdByIndex(i));
			}
		}
	}

	protected void addListener() {

		btnUpload.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				if (isValid()) {

					if (null != BYPASS_UPLOAD
							&& !("").equalsIgnoreCase(BYPASS_UPLOAD)
							&& ("false").equalsIgnoreCase(BYPASS_UPLOAD)) {
						upload.submitUpload();
					}

					else {
						try {
							upload.submitUpload();
							 
							binder.commit();
							if (("Upload document (Outside Process)").equalsIgnoreCase(presenterString)) {
								if(null != bean.getIsEdit() && !bean.getIsEdit())
										bean.setIsEdit(false);
								fireViewEvent(PAUploadDocumentsOutsideProcessPagePresenter.SUBMIT_SEARCH_OR_UPLOADED_DOCUMENTS,bean);
							}
							
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
								//	editFeilds(false);
								} else {
									//editFeilds(true);
									//editFeilds(false);
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

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		try {
			//Added for error
			if(file.exists() && !file.isDirectory()) { 
			byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
			if (null != fileAsbyteArray) {

				Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
				boolean hasSpecialChar = p.matcher(event.getFilename()).find();
				// if(hasSpecialChar)
				// {
				WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(
						event.getFilename(), fileAsbyteArray);
				Boolean flagUploadSuccess = new Boolean(""
						+ uploadStatus.get("status"));
				// TO read file after load
				if (flagUploadSuccess.booleanValue()) {
					String token = "" + uploadStatus.get("fileKey");
					// String token = "290";
					this.bean.setDmsDocToken(token);
					this.bean.setFileName(event.getFilename());
					System.out.println("the token value-----" + token);
					System.out.println("the file name-----"
							+ event.getFilename());
					// System.out.println("----the uploadStatusMap---"+uploadStatus);
					binder.commit();
					if (null != bean.getFileType()
							&& null != bean.getFileType().getValue())
					{
						if (("Upload Document (Outside Process)").equalsIgnoreCase(presenterString)) {
							if(null != bean.getIsEdit() && !bean.getIsEdit())
								bean.setIsEdit(false);
							fireViewEvent(
									PAUploadDocumentsOutsideProcessPagePresenter.SUBMIT_SEARCH_OR_UPLOADED_DOCUMENTS,bean);
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
				// else
				{
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
			}
				
				//for clearing heap
				fileAsbyteArray = null;
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


	

}
