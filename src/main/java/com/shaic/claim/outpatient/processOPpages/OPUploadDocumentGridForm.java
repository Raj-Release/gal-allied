/**
 * 
 */
package com.shaic.claim.outpatient.processOPpages;

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
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.billing.pages.BillingWorksheetUploadDocumentsPresenter;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryUploadDocumentsPresenter;
import com.shaic.claim.rod.wizard.pages.CreateRODUploadDocumentsPresenter;
import com.shaic.claims.reibursement.addaditionaldocuments.UploadDocumentsPresenter;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.paclaim.addAdditinalDocument.search.PAAddAdditionalDocUploadDocumentsPresenter;
import com.shaic.paclaim.rod.createrod.search.PACreateRODUploadDocumentsPresenter;
import com.shaic.paclaim.rod.enterbilldetails.search.PABillentryUploadDocumentsPresenter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;

/**
 * @author ntv.narasimhaj
 *
 */
public class OPUploadDocumentGridForm extends ViewComponent implements Receiver,
SucceededListener{
	private VerticalLayout mainaLayout;
	private Panel panel;
	private Button btnUpload;
	private Upload upload;
	private ComboBox cbxFileType;
	private TextField txtBillNo;
	private PopupDateField billDate;
	private TextField txtBillAmt;
	private TextField txtDeductibleAmt;
	private TextField txtNonPaybleAmt;
	private TextField txtBillValue;
	private TextField remarks;
	private BeanFieldGroup<UploadDocumentDTO> binder;
	private File file;
	private String presenterString;
	private UploadDocumentDTO bean;
	private OutPatientDTO outptDto;
	private ProcessOPBillDetailsPage parentObj;
	
	private TextField txtBillingWorksheetRemarks;
	
	
	//private String presenterString;

	public static String dataDir = System.getProperty("jboss.server.data.dir");
	public static String BYPASS_UPLOAD;

	public void init(UploadDocumentDTO bean,OutPatientDTO outptDto, String presenterString) {
		this.bean = bean;
		this.outptDto = outptDto;
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
			/*panel = buildUploadDocLayoutFromZonal();
			//panel.setSpacing(false);
			mainaLayout.addComponent(panel);*/
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
		

		txtBillAmt = binder.buildAndBind("", "billAmt", TextField.class);
		txtBillAmt.setMaxLength(7);
		txtBillAmt.setNullRepresentation("");
		CSValidator noOfItemValidator = new CSValidator();
		noOfItemValidator.extend(txtBillAmt);
		noOfItemValidator.setRegExp("^[0-9]*$");
		noOfItemValidator.setPreventInvalidTyping(true);
		txtBillAmt.addBlurListener(getBillAmtListener());
		
		txtDeductibleAmt = binder.buildAndBind("", "deductibleAmt", TextField.class);
		txtDeductibleAmt.setMaxLength(7);
		txtDeductibleAmt.setNullRepresentation("");
		CSValidator deductValidator = new CSValidator();
		deductValidator.extend(txtDeductibleAmt);
		deductValidator.setRegExp("^[0-9]*$");
		deductValidator.setPreventInvalidTyping(true);
		txtDeductibleAmt.addBlurListener(getDeductableAmtListener());
		
		txtNonPaybleAmt = binder.buildAndBind("", "nonPaybleAmt", TextField.class);
		txtNonPaybleAmt.setMaxLength(7);
		txtNonPaybleAmt.setNullRepresentation("");
		CSValidator nonPayableValidator = new CSValidator();
		nonPayableValidator.extend(txtNonPaybleAmt);
		nonPayableValidator.setRegExp("^[0-9]*$");
		nonPayableValidator.setPreventInvalidTyping(true);
		txtNonPaybleAmt.addBlurListener(getNonPayableAmtListener());

		txtBillValue = binder.buildAndBind("", "billValue", TextField.class);

		CSValidator billValueValidator = new CSValidator();
		billValueValidator.extend(txtBillValue);
		billValueValidator.setRegExp("^[0-9]*$");
		billValueValidator.setPreventInvalidTyping(true);
		txtBillValue.setMaxLength(7);
		txtBillValue.setNullRepresentation("");
		txtBillValue.setEnabled(false);
		
		remarks = binder.buildAndBind("", "remarks", TextField.class);
//		remarks.setMaxLength(400);
		remarks.setNullRepresentation("");

		cbxFileType = binder.buildAndBind("", "fileType", ComboBox.class);
		
		GridLayout gridLayout = new GridLayout(9, 2);
		gridLayout.setColumnExpandRatio(0, 50.0f);
		gridLayout.addComponent(new Label("File Upload"), 0, 0);
		gridLayout.addComponent(upload, 0, 1);

		gridLayout.addComponent(new Label("Category"), 1, 0);
		gridLayout.addComponent(cbxFileType, 1, 1);
		
		gridLayout.addComponent(new Label("Bill Date"), 2, 0);
		gridLayout.addComponent(billDate, 2, 1);

		gridLayout.addComponent(new Label("Bill Number"), 3, 0);
		gridLayout.addComponent(txtBillNo, 3, 1);

		gridLayout.addComponent(new Label("Bill Amount"), 4, 0);
		gridLayout.addComponent(txtBillAmt, 4, 1);
		
		gridLayout.addComponent(new Label("Deductible Amount"), 5, 0);
		gridLayout.addComponent(txtDeductibleAmt, 5, 1);
		
		gridLayout.addComponent(new Label("Non Payables"), 6, 0);
		gridLayout.addComponent(txtNonPaybleAmt, 6, 1);

		gridLayout.addComponent(new Label("Payable Amount"), 7, 0);
		gridLayout.addComponent(txtBillValue, 7, 1);
		
		gridLayout.addComponent(new Label("Remarks"), 8, 0);
		gridLayout.addComponent(remarks, 8, 1);
		gridLayout.setCaption("Upload Documents");
		
		Panel panel = new Panel();
		panel.setContent(gridLayout);
		
		return panel;
		
	}
	
	private BlurListener getDeductableAmtListener() {
		BlurListener listener = new BlurListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField value = (TextField) event.getComponent();
				if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
					if(txtBillAmt != null && txtBillAmt.getValue() != null && !txtBillAmt.getValue().isEmpty()
							&& txtNonPaybleAmt != null && txtNonPaybleAmt.getValue() != null && !txtNonPaybleAmt.getValue().isEmpty()){
						Integer deductableAmt = SHAUtils.getIntegerFromStringWithComma(value.getValue());
						Integer discount = SHAUtils.getIntegerFromStringWithComma(txtBillAmt.getValue());
						Integer nonPayableAmt = SHAUtils.getIntegerFromStringWithComma(txtNonPaybleAmt.getValue());
						Integer netAmt = discount - nonPayableAmt - deductableAmt;
						
						if(txtBillValue != null){
							txtBillValue.setValue(netAmt.toString());
						}
						
					}
					
				}
					
			}
		};
		return listener;
	}
	
	private BlurListener getBillAmtListener() {
		BlurListener listener = new BlurListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField value = (TextField) event.getComponent();
				if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
					if(txtNonPaybleAmt != null && txtNonPaybleAmt.getValue() != null && !txtNonPaybleAmt.getValue().isEmpty()
							&& txtDeductibleAmt != null && txtDeductibleAmt.getValue() != null && !txtDeductibleAmt.getValue().isEmpty()){
						Integer billAmt = SHAUtils.getIntegerFromStringWithComma(value.getValue());
						Integer nonPayable = SHAUtils.getIntegerFromStringWithComma(txtNonPaybleAmt.getValue());
						Integer deductableAmt = SHAUtils.getIntegerFromStringWithComma(txtDeductibleAmt.getValue());
						Integer netAmt = billAmt - deductableAmt - nonPayable;
						
						if(outptDto.getSpecialityDTOList() != null && !outptDto.getSpecialityDTOList().isEmpty()){
							for (OPSpecialityDTO specilaity : outptDto.getSpecialityDTOList()) {
								if(specilaity.getPedfromPolicy() != null && !specilaity.getPedfromPolicy().isEmpty() && !specilaity.getPedfromPolicy().equalsIgnoreCase("NIL")
										&& !specilaity.getPedfromPolicy().equalsIgnoreCase("NO")
										&& specilaity.getPed().getValue().equalsIgnoreCase("Related PED")){
									if(txtDeductibleAmt != null){
										txtDeductibleAmt.setValue(value.getValue());
									}
									txtDeductibleAmt.setEnabled(false);
									txtNonPaybleAmt.setEnabled(false);
									Integer deductable = SHAUtils.getIntegerFromStringWithComma(txtDeductibleAmt.getValue());
									Integer netPayable = billAmt - deductable - nonPayable;
									if(txtBillValue != null){
										txtBillValue.setValue(netPayable.toString());
									}
								}
							}
						} else if(txtBillValue != null){
							txtBillValue.setValue(netAmt.toString());
						}
						
						
					}
					
				}
					
			}
		};
		return listener;
	}
	
	private BlurListener getNonPayableAmtListener() {
		BlurListener listener = new BlurListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField value = (TextField) event.getComponent();
				if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
					if(txtBillAmt != null && txtBillAmt.getValue() != null && !txtBillAmt.getValue().isEmpty()
							&& txtDeductibleAmt != null && txtDeductibleAmt.getValue() != null && !txtDeductibleAmt.getValue().isEmpty()){
						Integer nonPayableclaimedAmt = SHAUtils.getIntegerFromStringWithComma(value.getValue());
						Integer discount = SHAUtils.getIntegerFromStringWithComma(txtBillAmt.getValue());
						Integer deductableAmt = SHAUtils.getIntegerFromStringWithComma(txtDeductibleAmt.getValue());
						Integer netAmt = discount - deductableAmt - nonPayableclaimedAmt;
						
						if(txtBillValue != null){
							txtBillValue.setValue(netAmt.toString());
						}
						
					}
					
				}
					
			}
		};
		return listener;
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

					if (null != BYPASS_UPLOAD
							&& !("").equalsIgnoreCase(BYPASS_UPLOAD)
							&& ("false").equalsIgnoreCase(BYPASS_UPLOAD)) {
						upload.submitUpload();
					}

					else {
						try {
							upload.submitUpload();

							binder.commit();
							if(null != bean.getIsEdit() && !bean.getIsEdit())
								bean.setIsEdit(false);
						 if ((SHAConstants.OUTPATIENT_FLAG) .equalsIgnoreCase(presenterString)) {
//								fireViewEvent(OPRODAndBillEntryPagePresenter.BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS, bean);
								fireViewEvent(OPClaimAssessmentPagePresenter.OP_BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS, bean);
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
			if (txtBillAmt.isEnabled()) {
				if (null != txtBillAmt
						&& (null == txtBillAmt.getValue()
								|| ("").equalsIgnoreCase(txtBillAmt.getValue())
								|| ("0").equalsIgnoreCase(txtBillAmt.getValue()) || ("null")
									.equalsIgnoreCase(txtBillAmt.getValue()))) {
					hasError = true;
					eMsg.append("Please Enter Bill Amount </br>");
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
			if (txtDeductibleAmt.isEnabled()) {
				if (null != txtDeductibleAmt
						&& (null == txtDeductibleAmt.getValue()
								|| ("").equalsIgnoreCase(txtDeductibleAmt.getValue())
								|| ("null")
									.equalsIgnoreCase(txtDeductibleAmt.getValue())))
				{
					hasError = true;
					eMsg.append("Please Enter Deductable Amount </br>");
				}
			}
			if (txtNonPaybleAmt.isEnabled()) {
				if (null != txtNonPaybleAmt
						&& (null == txtNonPaybleAmt.getValue()
								|| ("").equalsIgnoreCase(txtNonPaybleAmt.getValue())
								|| ("null")
									.equalsIgnoreCase(txtNonPaybleAmt.getValue())))
				{
					hasError = true;
					eMsg.append("Please Enter Non Payable Amount </br>");
				}
			}
			if (txtBillValue.isEnabled()) {
				if (null != txtBillValue
						&& (null == txtBillValue.getValue()
								|| ("").equalsIgnoreCase(txtBillValue.getValue())
								|| ("null")
									.equalsIgnoreCase(txtBillValue.getValue())))
				// if(!(null != txtBillValue &&
				// (!("").equalsIgnoreCase(txtBillValue.getValue())) &&
				// (!("0").equalsIgnoreCase(txtBillValue.getValue()))))
				{
					hasError = true;
					eMsg.append("Please Enter Payable Amount </br>");
				}
			}
			if (remarks.isEnabled()) {
				if (null != remarks
						&& (null == remarks.getValue()
								|| ("").equalsIgnoreCase(remarks.getValue())
								|| ("null")
									.equalsIgnoreCase(remarks.getValue())))
				{
					hasError = true;
					eMsg.append("Please Enter Remarks </br>");
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

	/*private void editFeilds(Boolean value) {
		txtBillNo.setEnabled(value);
		billDate.setEnabled(value);
		txtBillAmt.setEnabled(value);
		txtBillValue.setEnabled(value);
		if (!value) {
			txtBillNo.setValue(null);
			billDate.setValue(null);
			txtBillAmt.setValue(null);
			txtBillValue.setValue(null);
		}
	}*/

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		try {
			if(bean.getIsPhysicalDoc()){
				bean.setBillDate(billDate.getValue());
				bean.setBillNo(txtBillNo.getValue() != null ? txtBillNo.getValue() : "");
				bean.setBillAmt(txtBillAmt.getValue() != null ? SHAUtils.getIntegerFromStringWithComma(txtBillAmt.getValue()) : 0l);
				bean.setDeductibleAmt(txtDeductibleAmt.getValue() != null ? SHAUtils.getIntegerFromStringWithComma(txtDeductibleAmt.getValue()) : 0l);
				bean.setNonPaybleAmt(txtNonPaybleAmt.getValue() != null ? SHAUtils.getIntegerFromStringWithComma(txtNonPaybleAmt.getValue()) : 0l);
				bean.setBillValue(txtBillValue.getValue() != null ? SHAUtils.getIntegerFromStringWithComma(txtBillValue.getValue()) : 0d);
				bean.setRemarks(remarks.getValue() != null ? remarks.getValue():"");
				fireViewEvent(OPClaimAssessmentPagePresenter.OP_BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS, bean);
			}
			else if(file.exists() && !file.isDirectory()) { 
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
						fireViewEvent(OPClaimAssessmentPagePresenter.OP_BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS, bean);
					}
					else {
							
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox("Please upload one document before pressing upload button", buttonsNamewithType);
						}
				}

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
		if(!bean.getIsPhysicalDoc()){
		try {
			this.file = new File(System.getProperty("jboss.server.data.dir")
					+ File.separator + filename);
			if (null != file /*&& file.exists() && !file.isDirectory()*/) {
				fos = new FileOutputStream(file);
			} else {
				Notification.show("Error", ""
						+ "Please select the file to be uploaded",
						Type.ERROR_MESSAGE);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();}
		}

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
	
	private void editFeilds(Boolean value) {
		txtBillNo.setEnabled(value);
		billDate.setEnabled(value);
		txtBillAmt.setEnabled(value);
		txtDeductibleAmt.setEnabled(value);
		txtNonPaybleAmt.setEnabled(value);
//		txtBillValue.setEnabled(value);
		if (!value) {
			txtBillNo.setValue(null); 
			billDate.setValue(null); 
			txtBillAmt.setValue(null); 
			txtDeductibleAmt.setValue(null); 
			txtNonPaybleAmt.setValue(null); 
			txtBillValue.setValue(null);
			txtBillNo.setValue(null);
		}
	}
	
	public void setParentObj(ProcessOPBillDetailsPage parentObj) {
		this.parentObj = parentObj;
	}

}
