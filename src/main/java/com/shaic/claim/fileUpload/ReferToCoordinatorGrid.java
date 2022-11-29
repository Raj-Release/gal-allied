package com.shaic.claim.fileUpload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTableDTO;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class ReferToCoordinatorGrid extends ViewComponent implements Receiver,SucceededListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private MultipleUploadDocumentPageUI multipleDocumentPage;

	private VerticalLayout mainLayout;
	private GridLayout gridLayout;
//	private Button upload ;
	private TextField txtRequestType;
	private TextArea txtRequestorRemarks;
	private TextArea txtCoordinatorRemarks ;
	private TextField txtSNo;
	private BeanFieldGroup<FileUploadDTO> binder;
	private File file;
	private String presenterString;

	
	private Button submitBtn;

	private Button cancelBtn;
	
	private HorizontalLayout buttonHorLayout;
	
	private SearchProcessTranslationTableDTO fileUploadDTO;
	
	private Button tmpViewBtn;
	
	private FileUploadDTO bean;
	
	@Inject
	private UploadedFileViewUI fileViewUI;
	
	public void init(FileUploadDTO bean, SearchProcessTranslationTableDTO fileUploadDTO, String presenterString){
		this.bean = bean;
		this.presenterString = presenterString;
		this.fileUploadDTO = fileUploadDTO;
		setSizeFull();
		setCompositionRoot(mainLayout());
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<FileUploadDTO>(
				FileUploadDTO.class);
		this.binder.setItemDataSource(this.bean
				);
		
	}
	
	private VerticalLayout mainLayout(){
		initBinder();
		
		
		
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		mainLayout = new VerticalLayout();
		
		
//		upload  = new Button("Upload");	
//	    upload.addSucceededListener(this);
//	    upload.setButtonCaption(null);
	    
	    tmpViewBtn = new Button("View File");
	    tmpViewBtn.setEnabled(false);
	    tmpViewBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
	    tmpViewBtn.addStyleName(ValoTheme.BUTTON_LINK);
	    tmpViewBtn.setWidth("50%");
	    
	    Button uploadBtn = new Button("Upload");
	    HorizontalLayout hor = new HorizontalLayout(/*upload,*/uploadBtn/*,tmpViewBtn*/);
	    hor.setComponentAlignment(uploadBtn, Alignment.BOTTOM_CENTER);
//	    hor.setComponentAlignment(tmpViewBtn, Alignment.BOTTOM_RIGHT);
	    
	    uploadBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
//				upload.submitUpload();
				uploadDocument();
			}
		});
		
	
	    txtRequestType = binder.buildAndBind("", "requestType", TextField.class);
		
		txtRequestorRemarks = binder.buildAndBind("", "requestorMarks", TextArea.class);
		
		txtCoordinatorRemarks =binder.buildAndBind("", "remarks", TextArea.class);
		
		Label lable = new Label("<b>Specialist Details</b>",ContentMode.HTML);
		
		txtSNo = new TextField(" ");
		txtSNo.setValue("1");
		txtSNo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		gridLayout = new GridLayout(7, 2);
		
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
		
//		gridLayout.addComponent(new Label("S.No"),0,0);
//		gridLayout.addComponent(txtSNo , 0, 1);
		
		VerticalLayout reMark = new VerticalLayout(new Label("Request Type"));
		reMark.setWidth("100%");
		reMark.addStyleName("gridth");
		gridLayout.addComponent(reMark,1,0);
		gridLayout.addComponent(txtRequestType, 1, 1);
		
		VerticalLayout requestDate = new VerticalLayout(new Label("Requestor Remarks"));
		requestDate.setWidth("100%");
		requestDate.addStyleName("gridth");
		gridLayout.addComponent(requestDate,2,0);
		gridLayout.addComponent(txtRequestorRemarks, 2, 1);
		
		VerticalLayout uploadLayout = new VerticalLayout(new Label("File Upload"));
		uploadLayout.setWidth("100%");
		uploadLayout.addStyleName("gridth");
		gridLayout.addComponent(uploadLayout,3,0);
		gridLayout.addComponent(hor, 3, 1);
		
		
		VerticalLayout remarksLayout = new VerticalLayout(new Label("Coordinator Remarks"));
		remarksLayout.setWidth("100%");
		remarksLayout.addStyleName("gridth");
		gridLayout.addComponent(remarksLayout,4,0);
		gridLayout.addComponent(txtCoordinatorRemarks, 4, 1);
		
		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		buttonHorLayout = new HorizontalLayout(submitBtn, cancelBtn);
		buttonHorLayout.setSpacing(true);
		
		FormLayout dummyForm = new FormLayout();
		dummyForm.setHeight("150px");
		
		FormLayout dummyForm1 = new FormLayout();
		dummyForm1.setHeight("100px");
		
		gridLayout.setSpacing(true);
		gridLayout.addStyleName("gridbg");
		Panel gridPanel =new Panel(gridLayout);
		gridPanel.setSizeFull();
		
//		mainLayout.addComponent(dummyForm1);
		mainLayout.addComponent(gridPanel);
//		mainLayout.addComponent(dummyForm);
//		mainLayout.addComponent(btnUpload);
//		mainaLayout.setComponentAlignment(btnUpload, Alignment.MIDDLE_RIGHT);
		mainLayout.addComponent(buttonHorLayout);
		mainLayout.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		
		addListener();
		
		txtRequestType.setReadOnly(true);
		txtRequestorRemarks.setReadOnly(true);
		
		return mainLayout;
	}
	
	
	public void setFileTypeValues(BeanItemContainer<SelectValue> parameter){	
		//this.parameter = parameter;
		
	}
	
	public void setValueFromTable(BeanItemContainer<SelectValue> parameter, String value)
	{
//		 for(int i = 0 ; i<parameter.size() ; i++)
//		 	{
//				if ((value).equalsIgnoreCase(parameter.getIdByIndex(i).getValue()))
//				{
//					this.cbxFileType.setValue(parameter.getIdByIndex(i));
//				}
//			}
	}
	
	

	
	protected void addListener() {
		
     submitBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(txtCoordinatorRemarks != null  && txtCoordinatorRemarks.getValue() != null &&  ! txtCoordinatorRemarks.getValue().equals(""))
				{
//					upload.submitUpload();
					try {
						
						binder.commit();
						/**
						 * Since during bpmn to db migration, coordinator reply has been merged . Hence forth
						 * no separate screens for cashless and reimbursement. Only common submit. Hence this 
						 * common submit code has been implemented in File upload presenter and not in upload
						 * translated document presenter. Hence below if else block is replaced with the filupload
						 * presenter call.
						 * */
						fireViewEvent(fileUploadPresenter.SUBMIT_BUTTON,bean,fileUploadDTO);
						/*if(SHAConstants.CASHLESS_REFER_TO_COORDINATOR_TABLE.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(fileUploadPresenter.SUBMIT_BUTTON,bean,fileUploadDTO);
						} else if(SHAConstants.REIMBURSEMENT_REFER_TO_COORDINATOR.equalsIgnoreCase(presenterString)){
							fireViewEvent(UploadTranslatedDocumentPresenter.SUBMIT_BUTTON, bean, fileUploadDTO);
						}*/
						
						
					} catch (CommitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					invalidPage();
				}
				
			}
		});
     
		tmpViewBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(bean.getTmpFileName() != null && bean.getTmpFileToken() != null){
					Window popup = new com.vaadin.ui.Window();
					popup.setWidth("75%");
					popup.setHeight("90%");
					fileViewUI.init(popup,bean.getTmpFileName(), bean.getTmpFileToken());
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
		
		cancelBtn.addClickListener(new Button.ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
					        "NO", "Yes", new ConfirmDialog.Listener() {

					            public void onClose(ConfirmDialog dialog) {
					                if (!dialog.isConfirmed()) {
					                	
					                	VaadinSession session = getSession();
										SHAUtils.releaseHumanTask(fileUploadDTO.getUsername(), fileUploadDTO.getPassword(), fileUploadDTO.getTaskNumber(),session);
					                	
										fileUploadDTO = null;
										bean = null;
					                	
					                	if(SHAConstants.REIMBURSEMENT_REFER_TO_COORDINATOR.equalsIgnoreCase(presenterString)){
					                		releaseHumanTask();
					                		fireViewEvent(MenuItemBean.UPLOAD_TRANSLATED_DOCUMENTSR3, true);
					                	}else{
					                		releaseHumanTask();
					                		fireViewEvent(MenuItemBean.UPLOAD_TRANSLATED_DOCUMENTS, true);
					                	}
					                } else {
					                    dialog.close();
					                }
					            }
					        });
					dialog.setClosable(false);
					dialog.setStyleName(Reindeer.WINDOW_BLACK);
				}
			});
	}
	
	 private void releaseHumanTask(){
			
			Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
	     	/*String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
	 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);*/
	 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

	 		if(existingTaskNumber != null){
	 			//BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
	 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
	 		}
	 		
	 		if(wrkFlowKey != null){
	 			DBCalculationService dbService = new DBCalculationService();
	 			dbService.callUnlockProcedure(wrkFlowKey);
	 			getSession().setAttribute(SHAConstants.WK_KEY, null);
	 		}
		}
	
	private void invalidPage(){
		
		Label label = new Label("Please Enter Coordinator Remarks", ContentMode.HTML);
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
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		try {
			byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
			String fileName = event.getFilename();


			if(null != fileAsbyteArray )
			{

				if(null != event && null != event.getFilename() && (event.getFilename().endsWith("jpg") || event.getFilename().endsWith("jpeg") ||
						event.getFilename().endsWith("JPG") || event.getFilename().endsWith("JPEG")))
				{
					/*File convertedFile  = SHAFileUtils.convertImageToPDF(event.getFilename());
					fileName = convertedFile.getName();
					fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);*/

				}


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
						bean.setFileToken(token);
						this.bean.setFileName(fileName);
						bean.setTmpFileName(fileName);
						bean.setTmpFileToken(token);
						tmpViewBtn.setEnabled(true);
						System.out.println("the token value-----"+token);
						System.out.println("the file name-----"+event.getFilename());
						System.out.println("----the uploadStatusMap---"+uploadStatus);
						buildSuccessLayout();
					

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
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> File Uploaded Successfully.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
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
		
		public void uploadDocument() {

			multipleDocumentPage.init(SHAConstants.REFER_TO_COORDINATOR, bean.getKey(),false);
			multipleDocumentPage.setCurrentPage(getUI().getPage());
			multipleDocumentPage.setFileUploadDTO(fileUploadDTO);
		
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("File Upload");
			popup.setWidth("75%");
			popup.setHeight("75%");
			popup.setContent(multipleDocumentPage);
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
