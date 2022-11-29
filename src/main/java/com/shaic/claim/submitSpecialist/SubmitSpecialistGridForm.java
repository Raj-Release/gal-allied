package com.shaic.claim.submitSpecialist;

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

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.fileUpload.MultipleUploadDocumentPageUI;
import com.shaic.claim.search.specialist.search.SubmitSpecialistTableDTO;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.reimbursement.uploadTranslatedDocument.UploadTranslatedDocumentPresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
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

public class SubmitSpecialistGridForm extends ViewComponent implements Receiver,SucceededListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VerticalLayout mainLayout;
	private GridLayout gridLayout;
	private Button upload ;
	private TextField txtRequestedDate;
	private TextArea txtRequestorRemarks;
	private TextArea txtSpecialistRemarks ;
	private BeanFieldGroup<SubmitSpecialistDTO> binder;
	private File file;
	private String presenterString;
	
	@Inject
	private MultipleUploadDocumentPageUI multipleDocumentPage;
	
	private Button btnreferToSpecialist;
	
	private Button submitBtn;
	
	private Button viewBtn;

	private Button cancelBtn;
	
	private HorizontalLayout buttonHorLayout;
	
	private SubmitSpecialistTableDTO fileUploadDTO;
	
	private SubmitSpecialistDTO bean;
	
	private String fileName = "";
	
	private String fileToken = "";
	
	private ComboBox cmbSpecialistType;
	
	@Inject
	private UploadedFileViewUI fileViewUI;
	
	private Button tmpViewBtn;
	
	private VerticalLayout vLayout ;
	
	////private static Window popup;
	
	public void init(SubmitSpecialistDTO bean, SubmitSpecialistTableDTO fileUploadDTO, String presenterString){

		this.bean = bean;
		this.presenterString = presenterString;
		this.fileUploadDTO = fileUploadDTO;
		vLayout = new VerticalLayout();
		setSizeFull();
		setCompositionRoot(mainLayout());
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<SubmitSpecialistDTO>(
				SubmitSpecialistDTO.class);
		this.binder.setItemDataSource(this.bean);
		
	}
	
	private VerticalLayout mainLayout(){
		initBinder();
		
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		mainLayout = new VerticalLayout();
		
		
		upload  = new Button("Upload");	
//	    upload.addSucceededListener(this);
//	    upload.setButtonCaption(null);
//	    upload.setWidth("50%");
	    
	    tmpViewBtn = new Button("View File");
	    tmpViewBtn.setEnabled(false);
	    tmpViewBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
	    tmpViewBtn.addStyleName(ValoTheme.BUTTON_LINK);
	    tmpViewBtn.setWidth("50%");
	    
	    Button uploadBtn = new Button("Upload");
	    HorizontalLayout hor = new HorizontalLayout(upload/*,uploadBtn*//*,tmpViewBtn*/);
//	    hor.setComponentAlignment(uploadBtn, Alignment.BOTTOM_CENTER);
//	    hor.setComponentAlignment(tmpViewBtn, Alignment.BOTTOM_RIGHT);
	    
//	    uploadBtn.addClickListener(new Button.ClickListener() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				upload.submitUpload();
//			}
//		});
	    
	    viewBtn = new Button();
	    viewBtn.addStyleName(ValoTheme.BUTTON_BORDERLESS);
	    viewBtn.setWidth("150px");
	    viewBtn.addStyleName(ValoTheme.BUTTON_LINK);
	    
	    fileName = bean.getFileName();
	    fileToken = bean.getFileToken();
//	    
	    if(bean.getFileName() != null){
	    	viewBtn.setCaption("View File");
	    }else{
	    	viewBtn.setEnabled(false);
	    }
	    
	    Label lable = new Label("<b>Specialist Details</b>",ContentMode.HTML);
	
	    txtRequestorRemarks = binder.buildAndBind("", "requestorRemarks", TextArea.class);
	    
	    String requestorRemarks = this.bean.getRequestorRemarks();
	    
	    txtRequestorRemarks.setDescription(requestorRemarks);
	    txtRequestorRemarks.setData(bean);
	    txtRequestorRemarks.setId("requestorRmrks");	    
	    specialistRemarksListener(txtRequestorRemarks,null);
	    txtRequestorRemarks.setDescription("Click the Text Box and Press F8 For Detail Popup");
	    
		txtRequestedDate = binder.buildAndBind("", "requestedDate", TextField.class);
		
//		  if(tempDate != null){
//		    	txtRequestedDate.setValue(tempDate.toString());
//		    }
		
		txtSpecialistRemarks =binder.buildAndBind("", "specialistRemarks", TextArea.class);
		txtSpecialistRemarks.setMaxLength(4000);
		txtSpecialistRemarks.setId("specialistRmrks");
		specialistRemarksListener(txtSpecialistRemarks,null);
		txtSpecialistRemarks.setData(bean);
		txtSpecialistRemarks.setDescription("Click the Text Box and Press F8 For Detail Popup");
		Label sLabel = new Label("1");
		VerticalLayout snoLabel = new VerticalLayout(sLabel);
		snoLabel.setComponentAlignment(sLabel, Alignment.MIDDLE_CENTER);
		snoLabel.setWidth("20%");
		snoLabel.setHeight("50px");
		gridLayout = new GridLayout(6, 2);
		Label snoHLabel = new Label("S.No");
		VerticalLayout sNo = new VerticalLayout(snoHLabel);
		sNo.setComponentAlignment(snoHLabel, Alignment.BOTTOM_CENTER);
		sNo.addStyleName("gridth");
		sNo.setWidth("100%");
		
		gridLayout.addComponent(sNo,0,0);
		gridLayout.addComponent(snoLabel,0,1);
		gridLayout.setComponentAlignment(sNo, Alignment.TOP_CENTER);
		gridLayout.setComponentAlignment(snoLabel, Alignment.BOTTOM_CENTER);
		
		VerticalLayout reMark = new VerticalLayout(new Label("Requestor Remarks"));
		reMark.setWidth("100%");
		reMark.addStyleName("gridth");
		gridLayout.addComponent(reMark,1,0);
		gridLayout.addComponent(txtRequestorRemarks, 1, 1);
		
		VerticalLayout requestDate = new VerticalLayout(new Label("Requested Date"));
		requestDate.setWidth("100%");
		requestDate.addStyleName("gridth");
		gridLayout.addComponent(requestDate,2,0);
		gridLayout.addComponent(txtRequestedDate, 2, 1);
		
		VerticalLayout uploadLabel = new VerticalLayout(new Label("File Uploaded"));
		uploadLabel.setWidth("100%");
		
		uploadLabel.addStyleName("gridth");
		gridLayout.addComponent(uploadLabel,3,0);
		gridLayout.setComponentAlignment(uploadLabel, Alignment.TOP_LEFT);
		
		
		gridLayout.addComponent(viewBtn, 3, 1);
		gridLayout.setComponentAlignment(viewBtn, Alignment.BOTTOM_LEFT);
		VerticalLayout fileupload = new VerticalLayout(new Label("Upload File"));
		fileupload.setWidth("100%");
		fileupload.addStyleName("gridth");
		gridLayout.addComponent(fileupload,4,0);
		gridLayout.addComponent(hor, 4, 1);
		
		VerticalLayout label2 = new VerticalLayout(new Label("Specialist Remarks"));
		label2.setWidth("100%");
	
		label2.addStyleName("gridth");
		gridLayout.addComponent(label2,5,0);
		gridLayout.addComponent(txtSpecialistRemarks, 5, 1);
		
		
		gridLayout.setSpacing(true);
		gridLayout.addStyleName("gridbg");
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
		Panel gridpanel =new Panel();
		gridpanel.setContent(gridLayout);
		gridpanel.setSizeFull();
		FormLayout dummyForm = new FormLayout();
		dummyForm.setHeight("150px");
		
		FormLayout dummyForm1 = new FormLayout(lable);
		dummyForm1.setHeight("50px");
		
		btnreferToSpecialist = new Button("Refer to Inter Specialist");
		
		cmbSpecialistType = new ComboBox("Specialist Type");
		
		FormLayout cmbForm = new FormLayout(cmbSpecialistType);
		
		mainLayout.addComponent(dummyForm1);
		mainLayout.addComponent(gridpanel);
		mainLayout.addComponent(btnreferToSpecialist);
		mainLayout.addComponent(cmbForm);
//		mainLayout.addComponent(dummyForm);
//		mainLayout.addComponent(btnUpload);
//		mainaLayout.setComponentAlignment(btnUpload, Alignment.MIDDLE_RIGHT);
		mainLayout.addComponent(buttonHorLayout);
		mainLayout.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		mainLayout.setComponentAlignment(cmbForm, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		
		addListener();
		
		txtRequestedDate.setReadOnly(true);
		txtRequestorRemarks.setReadOnly(true);
		
		return mainLayout;
	}
	
//public  void handlePopupForRequesterRemarks(TextArea searchField, final  Listener listener) {
//		
//	    ShortcutListener enterShortCut = new ShortcutListener(
//	        "ShortcutForRequesterRemarks", ShortcutAction.KeyCode.F7, null) {
//		
//	      private static final long serialVersionUID = 1L;
//	      @Override
//	      public void handleAction(Object sender, Object target) {
//	        ((ShortcutListener) listener).handleAction(sender, target);
//	      }
//	    };
//	    handleShortcutForRequester(searchField, getShortCutListenerForRequesterRemarks(searchField));
//	    
//	  }
//
//private ShortcutListener getShortCutListenerForRequesterRemarks(final TextArea txtFld)
//{ 
//	ShortcutListener listener =  new ShortcutListener("Requester Remarks",KeyCodes.KEY_F7,null) {
//		
//		private static final long serialVersionUID = 1L;
//
//		@Override
//		public void handleAction(Object sender, Object target) {
//			SubmitSpecialistDTO   specialistDTO = (SubmitSpecialistDTO) txtFld.getData();
//			VerticalLayout vLayout =  new VerticalLayout();
//			
//			vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
//			vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
//			vLayout.setMargin(true);
//			vLayout.setSpacing(true);
//			TextArea txtArea = new TextArea();
//			txtArea.setNullRepresentation("");
//			txtArea.setValue(specialistDTO.getRequestorRemarks());
//			txtArea.setSizeFull();
//			txtArea.setWidth("100%");
//			if(specialistDTO.getRequestorRemarks() != null){
//				txtArea.setRows(specialistDTO.getRequestorRemarks().length()/80 >= 25 ? 25 : ((specialistDTO.getRequestorRemarks().length()/80)%25)+1);
//			}
//			else{
//				txtArea.setRows(25);
//			}
//			txtArea.setReadOnly(true);
//			
//			Button okBtn = new Button("OK");
//			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//			vLayout.addComponent(txtArea);
//			vLayout.addComponent(okBtn);
//			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
//			
//			final Window dialog = new Window();
//			dialog.setCaption("Requestor Remarks");
//			dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
//			dialog.setWidth("45%");
//			dialog.setClosable(true);
//			
//			dialog.setContent(vLayout);
//			dialog.setResizable(false);
//			dialog.setModal(true);
//			dialog.setDraggable(true);
//			
//			if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
//				dialog.setPositionX(450);
//				dialog.setPositionY(500);
//			}
//			getUI().getCurrent().addWindow(dialog);
//			
//			okBtn.addClickListener(new ClickListener() {
//				private static final long serialVersionUID = 7396240433865727954L;
//
//				@Override
//				public void buttonClick(ClickEvent event) {
//					dialog.close();
//				}
//			});	
//		}
//	};
//	
//	return listener;
//}
//	
//	public  void handleShortcutForRequester(final TextArea textField, final ShortcutListener shortcutListener) {
//		textField.addFocusListener(new FocusListener() {
//			
//			@Override
//			public void focus(FocusEvent event) {
//				textField.addShortcutListener(shortcutListener);
//				
//			}
//		});
//	}
	
	
	
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
		
		viewBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				popup.setWidth("75%");
				popup.setHeight("90%");
				fileViewUI.init(popup,fileName, fileToken);
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
		
		upload.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				uploadDocument();
			}
		});
		
submitBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
    	 private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure you want to submit ?",
				        "NO", "Yes", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		            	if (!dialog.isConfirmed()) {

		            		if(fileUploadDTO.getPreauthDTO().getIsBusinessProfileClicked()){
	            				
		            			if(fileUploadDTO.getPreauthDTO().getIsInsuredChannedNameClicked()){


		            			if(txtSpecialistRemarks != null  && txtSpecialistRemarks.getValue() != null &&  ! txtSpecialistRemarks.getValue().equals(""))
		            			{
		            				//								upload.submitUpload();

		            				try {
		            					binder.commit();
		            					if(SHAConstants.REIMBURSEMENT_SPECIALIST_PAGE.equalsIgnoreCase(presenterString))
		            					{
		            						fireViewEvent(SubmitSpecialistPagePresenter.SUBMIT_BUTTON_CLICK,bean,fileUploadDTO);
		            					}else if(SHAConstants.CASHLESS_SPECIALIST.equalsIgnoreCase(presenterString)) {
		            						fireViewEvent(SubmitSpecialistPagePresenter.SUBMIT,bean,fileUploadDTO);
		            					}
		            					else if(SHAConstants.REIMBURSEMENT_REFER_TO_COORDINATOR.equalsIgnoreCase(presenterString)){
		            						fireViewEvent(UploadTranslatedDocumentPresenter.SUBMIT_BUTTON, bean, fileUploadDTO);
		            					}
		            				} catch (CommitException e) {
		            					// TODO Auto-generated catch block
		            					e.printStackTrace();
		            				}

								}
								else{
									invalidPage();
								}
		            			
		            			}else{
		            				errorMsgForInsuredChannelButton();
		            		}
									
		                	}
		                	else{
		                		errorMsgForBusinessProfile();
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
		
		cancelBtn.addClickListener(new Button.ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure you want to cancel ?",
					        "NO", "Yes", new ConfirmDialog.Listener() {

					            public void onClose(ConfirmDialog dialog) {
					                if (!dialog.isConfirmed()) {
					                	
					                	VaadinSession session = getSession();
									    SHAUtils.releaseHumanTask(fileUploadDTO.getUsername(), fileUploadDTO.getPassword(), fileUploadDTO.getTaskNumber(),session);
									    Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

								 		
								 		if(wrkFlowKey != null){
								 			DBCalculationService dbService = new DBCalculationService();
								 			dbService.callUnlockProcedure(wrkFlowKey);
								 			getSession().setAttribute(SHAConstants.WK_KEY, null);
								 		}
					                	
									    if(SHAConstants.REIMBURSEMENT_SPECIALIST_PAGE.equalsIgnoreCase(presenterString)){
					                		//fireViewEvent(MenuItemBean.SUBMIT_SPECIALIST_ADVISE, true);
									    	fireViewEvent(MenuPresenter.SUBMIT_SPECIALIST_ADVISE_GETTASK, fileUploadDTO.getSearchFormDTO());
					                	}else{
					                		//fireViewEvent(MenuItemBean.SUBMIT_SPECIALLIST_ADVISE, true);
					                		fireViewEvent(MenuPresenter.SUBMIT_SPECIALIST_ADVISE_GETTASK, fileUploadDTO.getSearchFormDTO());
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
	
	private void invalidPage(){
		
		Label label = new Label("Please Enter specialist Remarks", ContentMode.HTML);
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
	
	private void errorMsgForInsuredChannelButton(){
		
		Label label = new Label("Please Verify Insured/Channel Name Button.", ContentMode.HTML);
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
	
	private void errorMsgForBusinessProfile(){
		
		Label label = new Label("Please Verify View Mini Business Profile.", ContentMode.HTML);
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
			//Added for error
			if(file.exists() && !file.isDirectory()) {
			byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
			String fileName = event.getFilename();
			if (null != fileAsbyteArray) {

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
//						bean.setFileToken(token);
//						this.bean.setFileName(event.getFilename());
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
						Notification.show("Error", "" + uploadStatus.get("message"), Type.ERROR_MESSAGE);
					}
			}
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
		
		
		public void uploadDocument() {

				multipleDocumentPage.init(SHAConstants.SPECIALIST_SCREEN, bean.getKey(),false);
				multipleDocumentPage.setCurrentPage(getUI().getPage());
			
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
		
		
/*		public  void specialistRemarksListener(TextArea searchField, final  Listener listener) {
		    ShortcutListener enterShortCut = new ShortcutListener(
		        "EnterShortcut", ShortcutAction.KeyCode.F8, null) {
		      private static final long serialVersionUID = -2267576464623389044L;
		      @Override
		      public void handleAction( Object sender, Object target) {
		        ((ShortcutListener) listener).handleAction(sender, target);
		      }
		    };

		    handleShortcut(searchField, getShortCutListener(searchField));
		  }
		
		public  void handleShortcut(final TextArea textField, final ShortcutListener shortcutListener) {
		
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
		
		
		private ShortcutListener getShortCutListener(final TextArea txtFld)
		{
			ShortcutListener listener =  new ShortcutListener("EnterShortcut",KeyCodes.KEY_F8,null) {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void handleAction(Object sender, Object target) {
					//DraftInvestigatorDto draftInvestigationDTO = (DraftInvestigatorDto) txtFld.getData();		
					 
					if (null != vLayout
							&& vLayout.getComponentCount() > 0) {
						vLayout.removeAllComponents();
					}
					
					final TextArea txtArea = new TextArea();			
					txtArea.setValue(txtFld.getValue());
					txtArea.setNullRepresentation("");
					txtArea.setSizeFull();
					txtArea.setWidth("100%");
					txtArea.setMaxLength(4000);
					txtArea.setRows(25);
					
					txtArea.setValue(null != txtSpecialistRemarks.getValue() ? txtSpecialistRemarks.getValue() : "");				
					txtArea.addValueChangeListener(new ValueChangeListener() {
						
						@Override
						public void valueChange(ValueChangeEvent event) {
							TextArea txt = (TextArea)event.getProperty();
							txtFld.setValue(txt.getValue());
							txtFld.setDescription(txt.getValue());
							// TODO Auto-generated method stub					
						}
					});
					
						
					vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
					vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
					vLayout.setMargin(true);
					vLayout.setSpacing(true);
					
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
					
					final Window dialog = new Window();
					dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
					dialog.setWidth("45%");
					dialog.setClosable(true);
					
					dialog.setContent(vLayout);
					dialog.setResizable(true);
					dialog.setModal(true);
					dialog.setDraggable(true);
					dialog.setData(txtFld);
					
					//dialog.show(getUI().getCurrent(), null, true);
					
					if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
						dialog.setPositionX(450);
						dialog.setPositionY(500);
					}
					getUI().getCurrent().addWindow(dialog);
					
					okBtn.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							dialog.close();
							bean.setSpecialistRemarks(txtFld.getValue());	
						}
					});	
				}
			};
			
			return listener;
		}*/
		
		
		
		public  void specialistRemarksListener(TextArea searchField, final  Listener listener) {
			
		    ShortcutListener enterShortCut = new ShortcutListener(
		        "ShortcutForSpecialistLetterRemarks", ShortcutAction.KeyCode.F8, null) {
			
		      private static final long serialVersionUID = 1L;
		      @Override
		      public void handleAction(Object sender, Object target) {
		        ((ShortcutListener) listener).handleAction(sender, target);
		      }
		    };
		    handleShortcutForSpecialistRemarks(searchField, getShortCutListenerForSpecialistRemarks(searchField));
		    
		  }
		
		public  void handleShortcutForSpecialistRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
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
		private ShortcutListener getShortCutListenerForSpecialistRemarks(final TextArea txtFld)
		{
			ShortcutListener listener =  new ShortcutListener("Specialist Remarks",KeyCodes.KEY_F8,null) {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void handleAction(Object sender, Object target) {
					SubmitSpecialistDTO  searchTableDto = (SubmitSpecialistDTO) txtFld.getData();
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
					
					
					if(("specialistRmrks").equalsIgnoreCase(txtFld.getId())){
						
						
						txtArea.setRows(25);
						txtArea.setHeight("30%");
						txtArea.setWidth("100%");
						dialog.setHeight("75%");
				    	dialog.setWidth("65%");
						txtArea.setReadOnly(false);
					}
					else{
						txtArea.setSizeFull();
						txtArea.setWidth("100%");
						if(("requestorRmrks").equalsIgnoreCase(txtFld.getId()) && searchTableDto.getRequestorRemarks()!= null && !searchTableDto.getRequestorRemarks().isEmpty()){
							//txtArea.setRows(searchTableDto.getRequestorRemarks().length()/80 >= 25 ? 25 : ((searchTableDto.getRequestorRemarks().length()/80)%25)+1);
							
							/*String splitArray[] = searchTableDto.getRequestorRemarks().split("[\n*|.*]");
							
							/*if(splitArray != null && splitArray.length > 0 && splitArray.length > 25){
								txtArea.setRows(25);
							}
							else{
								txtArea.setRows(splitArray.length);
							}*/
							//IMSSUPPOR-24557
							txtArea.setRows(25);
							//IMSSUPPOR-23361
							txtArea.setHeight("30%");
							txtArea.setWidth("100%");
					    	dialog.setWidth("65%");
							
							dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
							
						}						
						txtArea.setReadOnly(true);
					}
					
					txtArea.addValueChangeListener(new Property.ValueChangeListener() {
						
						@Override
						public void valueChange(ValueChangeEvent event) {
							if(("specialistRmrks").equalsIgnoreCase(txtFld.getId())){
								txtFld.setValue(((TextArea)event.getProperty()).getValue());						
								SubmitSpecialistDTO mainDto = (SubmitSpecialistDTO)txtFld.getData();
								mainDto.setSpecialistRemarks(txtFld.getValue());
							}
						}
					});
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
					
					
					String strCaption = "";
					
					if(("specialistRmrks").equalsIgnoreCase(txtFld.getId())){
						strCaption = "Specialist Remarks";
					}
				    else if(("requestorRmrks").equalsIgnoreCase(txtFld.getId())){
				    	strCaption = "Requestor Remarks";
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
//							TextArea txtArea = (TextArea)dialog.getData();
//							txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
							dialog.close();
						}
					});
					
					if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
						dialog.setPositionX(450);
						dialog.setPositionY(500);
						dialog.setModal(false);
					}
					getUI().getCurrent().addWindow(dialog);
					okBtn.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
//							TextArea txtArea = (TextArea)dialog.getData();
//							txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
							dialog.close();
						}
					});	
				}
			};
			
			return listener;
		}
}


