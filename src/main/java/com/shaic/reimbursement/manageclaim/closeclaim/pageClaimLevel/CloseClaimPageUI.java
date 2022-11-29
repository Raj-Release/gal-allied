package com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhacement.table.PreviousPreAuthDetailsTable;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class CloseClaimPageUI extends ViewComponent implements Receiver,SucceededListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CloseClaimPageDTO bean;
	
	@Inject
	private CloseClaimTable closeClaimTable;
	
	@Inject
	private Instance<PreviousPreAuthDetailsTable> previousPreauthDetailsTableInstance;
	
	private PreviousPreAuthDetailsTable previousPreAuthDetailsTableObj;
	
	@Inject
	private Instance<CloseClaimReimbursementTable> closeClaimReimbursementTableInstance;
	
	private CloseClaimReimbursementTable closeClaimReimbursementTableObj;
	
	@Inject
	private Instance<DocumentUploadTableForCloseClaim> uploadDocumentTableInstance;
	
	private DocumentUploadTableForCloseClaim uploadDocumentTableObj;
	
	private GridLayout gridLayout;
	
	private Upload 	upload;
	
	private VerticalLayout mainLayout;
	
	private ComboBox cmbFileType;
	
	private ComboBox cmbReferenceNo;
	
	private ComboBox cmbCloseClaimReason;
	
	
	
	private TextArea txtReason;
	
	private File file;
	
	private Button btnSubmit;
	
	private Button btnCancel;
	
	@PostConstruct
	public void initView() {
	}
	
	
	public void init(CloseClaimPageDTO bean){
		
		this.bean = bean;
		
		closeClaimTable.init("Claim Details", false, false);
		
		List<CloseClaimTableDTO> closeClaimList = bean.getCloseClaimList();
		closeClaimTable.setTableList(closeClaimList);
		
		this.previousPreAuthDetailsTableObj = previousPreauthDetailsTableInstance
				.get();
		this.previousPreAuthDetailsTableObj.init("Pre-auth Details", false, false);
		
		List<PreviousPreAuthTableDTO> previousPreauthDetailsList = bean.getPreviousPreauthDetailsList();
		if(previousPreauthDetailsList != null && ! previousPreauthDetailsList.isEmpty()){
			
			this.previousPreAuthDetailsTableObj.setTableList(previousPreauthDetailsList);
		}else{
			this.previousPreAuthDetailsTableObj.setVisible(false);
		}
		
		this.closeClaimReimbursementTableObj = closeClaimReimbursementTableInstance
				.get();
		this.closeClaimReimbursementTableObj.init("ROD Details", false, false);
		
		List<ViewDocumentDetailsDTO> rodDocumentDetailsList = bean.getRodDocumentDetailsList();
		if(rodDocumentDetailsList != null && ! rodDocumentDetailsList.isEmpty()){
			this.closeClaimReimbursementTableObj.setTableList(rodDocumentDetailsList);
		}else{
			this.closeClaimReimbursementTableObj.setVisible(false);
		}
		
		Panel fileUploadPanel = getFileUploadPanel();
		
		this.uploadDocumentTableObj = uploadDocumentTableInstance
				.get();
		this.uploadDocumentTableObj.init("Document Details", false, false);
		
		cmbCloseClaimReason = new ComboBox("Reason For Close Claim");
		cmbCloseClaimReason.setRequired(true);
		
		
		cmbCloseClaimReason.setContainerDataSource(bean.getReasonForCloseClaimContainer());
		cmbCloseClaimReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCloseClaimReason.setItemCaptionPropertyId("value");
		
		txtReason = new TextArea("Close Claim Remarks");
		txtReason.setRequired(true);
		
		FormLayout firstForm = new FormLayout(cmbCloseClaimReason,txtReason);
		
		btnSubmit=new Button("Submit");
		btnCancel=new Button("Cancel");
        
        btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnSubmit.setWidth("-1px");
        btnSubmit.setHeight("-10px");
		
        btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
        btnCancel.setWidth("-1px");
        btnCancel.setHeight("-10px");
		
        HorizontalLayout buttonHorLayout=new HorizontalLayout(btnSubmit,btnCancel);
        buttonHorLayout.setSpacing(true);
        buttonHorLayout.setMargin(false);
		
		mainLayout = new VerticalLayout(closeClaimTable,this.previousPreAuthDetailsTableObj,this.closeClaimReimbursementTableObj,fileUploadPanel,uploadDocumentTableObj,firstForm,buttonHorLayout);
		mainLayout.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		mainLayout.setComponentAlignment(firstForm, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		
		setCompositionRoot(mainLayout);
		
		addListener();

	}
	
	private Panel getFileUploadPanel(){
		
		gridLayout = new GridLayout(7, 2);
		
		cmbFileType = new ComboBox();
		cmbReferenceNo = new ComboBox();
		
		Label sLabel = new Label("1");
		VerticalLayout snoLabel = new VerticalLayout(sLabel);
		snoLabel.setComponentAlignment(sLabel, Alignment.MIDDLE_CENTER);
		snoLabel.setWidth("20%");
		snoLabel.setHeight("50px");
		Label snoHLabel = new Label("S.No");
		VerticalLayout sNo = new VerticalLayout(snoHLabel);
		sNo.setComponentAlignment(snoHLabel, Alignment.BOTTOM_CENTER);
		sNo.addStyleName("gridth");
		sNo.setWidth("100%");
		
		gridLayout.addComponent(sNo,0,0);
		gridLayout.addComponent(snoLabel,0,1);
		gridLayout.setComponentAlignment(sNo, Alignment.TOP_CENTER);
		gridLayout.setComponentAlignment(snoLabel, Alignment.BOTTOM_CENTER);
		
		upload  = new Upload("", this);	
	    upload.addSucceededListener(this);
	    upload.setButtonCaption(null);
	    
	    Button uploadBtn = new Button("Upload");
	    uploadBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	    HorizontalLayout hor = new HorizontalLayout(upload);
//	    hor.setComponentAlignment(uploadBtn, Alignment.BOTTOM_CENTER);
	    
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
		
		VerticalLayout uploadLayout = new VerticalLayout(new Label("File Upload"));
		uploadLayout.setWidth("100%");
		uploadLayout.addStyleName("gridth");
		gridLayout.addComponent(uploadLayout,1,0);
		gridLayout.addComponent(hor, 1, 1);
		
		VerticalLayout requestDate = new VerticalLayout(new Label("File Type"));
		requestDate.setWidth("100%");
		requestDate.addStyleName("gridth");
		gridLayout.addComponent(requestDate,2,0);
		gridLayout.addComponent(cmbFileType, 2, 1);
		gridLayout.setComponentAlignment(cmbFileType, Alignment.BOTTOM_CENTER);
		
		cmbFileType.setContainerDataSource(bean.getFileTypeContainer());
		cmbFileType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbFileType.setItemCaptionPropertyId("value");
		
		VerticalLayout referenceNo = new VerticalLayout(new Label("ROD NO/Reference No"));
		referenceNo.setWidth("100%");
		referenceNo.addStyleName("gridth");
		gridLayout.addComponent(referenceNo,3,0);
		gridLayout.addComponent(cmbReferenceNo, 3, 1);
		gridLayout.setComponentAlignment(cmbReferenceNo, Alignment.BOTTOM_CENTER);
		
		if(bean.getReferenceNoContainer() != null){
			cmbReferenceNo.setContainerDataSource(bean.getReferenceNoContainer());
			cmbReferenceNo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbReferenceNo.setItemCaptionPropertyId("value");
		}
		
		VerticalLayout remarksLayout = new VerticalLayout(new Label(""));
		remarksLayout.setWidth("100%");
		remarksLayout.addStyleName("gridth");
		gridLayout.addComponent(remarksLayout,4,0);
		gridLayout.addComponent(uploadBtn, 4, 1);
		gridLayout.setComponentAlignment(uploadBtn, Alignment.BOTTOM_CENTER);
		
		gridLayout.setSpacing(true);
		
		Panel gridPanel =new Panel(gridLayout);
		gridPanel.setSizeFull();
		gridPanel.setCaption("Upload Details");
		
		return gridPanel;
	}
	
	
	public void addListener(){
		
		btnCancel.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
				        "No", "Yes", new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
				                	fireViewEvent(MenuItemBean.CLOSE_CLAIM_CLAIM_LEVEL, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
				dialog.setClosable(false);
			}
		});
		btnSubmit.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(validatePage()){
					
			    String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			    bean.setUserName(userName);
				fireViewEvent(CloseClaimPresenter.SUBMIT_CLOSE_CLAIM, bean);
				}
				
				
			}
		});
	}
	
	public boolean validatePage() {
		Boolean hasError = false;
		
		StringBuffer eMsg = new StringBuffer();		
		
		if(cmbCloseClaimReason != null && cmbCloseClaimReason.getValue() == null){
			eMsg.append("Please Choose Reason for Close Claim </br>");
			hasError = true;
		}
		
		if(txtReason != null && ((txtReason.getValue() == null) || (txtReason.getValue() != null && txtReason.getValue().equalsIgnoreCase("")))){
			eMsg.append("Please Enter Close Claim Remarks </br>");
			hasError = true;
		}

		if (hasError) {
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			hasError = true;
			return !hasError;
		} else {
			
			if(cmbCloseClaimReason != null && cmbCloseClaimReason.getValue() != null){
				SelectValue reasonId = (SelectValue) cmbCloseClaimReason.getValue();
				
				bean.setReasonId(reasonId);
			}
			bean.setCloseRemarks(txtReason.getValue());
			
			List<UploadDocumentCloseClaimDTO> values = this.uploadDocumentTableObj.getValues();
			
			bean.setUploadDocumentDetails(values);
			

			return true;
		}
	}
	
	


	@Override
	public void uploadSucceeded(SucceededEvent event) {

		try {
			
			if(cmbFileType != null && cmbFileType.getValue() != null && cmbReferenceNo != null && cmbReferenceNo.getValue() != null){

				byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
				if(null != fileAsbyteArray )
				{
					
					
					Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
					boolean hasSpecialChar = p.matcher(event.getFilename()).find();
				//	if(hasSpecialChar)
					//{
					WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
						Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
						//TO read file after load
						if (flagUploadSuccess.booleanValue())
						{
							String token = "" + uploadStatus.get("fileKey");
							
							CloseClaimPageDTO mainDTO = new CloseClaimPageDTO();
							mainDTO.setFileName(event.getFilename());
							mainDTO.setFileToken(token);
							mainDTO.setDocumentSource("CLOSE_CLAIM");
							mainDTO.setIntimationNumber(this.bean.getIntimationNumber());
							mainDTO.setClaimNumber(this.bean.getClaimNumber());
							mainDTO.setUserName(this.bean.getUserName());
							
							if(cmbFileType != null && cmbFileType.getValue() != null){
								SelectValue fileType = (SelectValue) cmbFileType.getValue();
								mainDTO.setFileType(fileType.getValue());	
							}
							if(cmbReferenceNo != null && cmbReferenceNo.getValue() != null){
								SelectValue referenceNo = (SelectValue)cmbReferenceNo.getValue();
								String reference = referenceNo.getValue();
								String[] split = reference.split("-");
								if(split[0].contains("ROD")){
									mainDTO.setReimbursmentNumber(split[0]);
								}else{
									mainDTO.setCashlessNumber(split[0]);
								}
								mainDTO.setReferenceNo(referenceNo.getValue());
							}
							
							String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
							
							mainDTO.setUserName(userName);
						
							System.out.println("the token value-----"+token);
							System.out.println("the file name-----"+event.getFilename());
							System.out.println("----the uploadStatusMap---"+uploadStatus);
							
							fireViewEvent(CloseClaimPresenter.UPLOAD_DOCUMENT_TABLE, mainDTO);

						}
						else
						{
	//						if(SHAConstants.CASHLESS_REFER_TO_COORDINATOR_TABLE.equalsIgnoreCase(presenterString))
	//						{
	//							fireViewEvent(fileUploadPresenter.SUBMIT_BUTTON,bean,fileUploadDTO);
	//						} else if(SHAConstants.REIMBURSEMENT_REFER_TO_COORDINATOR.equalsIgnoreCase(presenterString)){
	//							fireViewEvent(UploadTranslatedDocumentPresenter.SUBMIT_BUTTON, bean, fileUploadDTO);
	//						}
							Notification.show("Error", "" + uploadStatus.get("message"), Type.ERROR_MESSAGE);
						}
				}
			}else{
				getErrorMessage("Please Choose File Type and Reference No");
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	
	}


	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		
		FileOutputStream fos = null;
		try {
			this.file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
			if(null != file)
			{
				fos = new FileOutputStream(file);
			}
			else
			{
				Notification.show("Error", "" + "Please select the file to be uploaded", Type.ERROR_MESSAGE);
			}
		} catch (FileNotFoundException e) {
			Notification.show("Error", "" + "Please select the file to be uploaded", Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
			return fos;
	}
	
	public void setUploadDocumentTable(UploadDocumentCloseClaimDTO documentDTO){
//		List<UploadDocumentCloseClaimDTO> values = uploadDocumentTableObj.getValues();
//		
//		uploadDocumentTableObj.removeRow();
//		
//		for (UploadDocumentCloseClaimDTO uploadDocumentCloseClaimDTO : values) {
//			uploadDocumentTableObj.addBeanToList(uploadDocumentCloseClaimDTO);
//		}
		
		uploadDocumentTableObj.addBeanToList(documentDTO);
		
		
	}
	
	public void getErrorMessage(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	

}
