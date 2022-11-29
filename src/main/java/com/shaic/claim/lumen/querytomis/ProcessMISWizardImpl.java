package com.shaic.claim.lumen.querytomis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.lumen.CommonLumenCarousel;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.LumenQueryDetailsDTO;
import com.shaic.claim.lumen.components.MISQueryInitiationDetails;
import com.shaic.claim.lumen.components.MISQueryReplyDTO;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.shaic.claim.lumen.upload.FileData;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
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
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ProcessMISWizardImpl extends AbstractMVPView implements ProcessMISWizard, Receiver,SucceededListener{

	@Inject
	private Instance<CommonLumenCarousel> carouselInstance;

	@Inject
	private ViewDetails viewDetails;

	private VerticalLayout mainPanel;
	private LumenRequestDTO dtoBean;
	private StringBuilder errMsg = new StringBuilder();

	@EJB
	private MasterService masterService;

	@Inject
	private LumenDbService lumenService;

	@Inject
	private MISQueryInitiationDetails misQueryDetails;

	private List<MISQueryReplyDTO> listOfMISQryDetailsObj;

	@Inject
	private Instance<MISQueryReplyTable> qrMISTableInstance;

	private MISQueryReplyTable qrMISTableObj;

	private List<LumenQueryDetailsDTO> listOfQryDtlObj;

	private GridLayout gridLayout;
	private Button btnUpload;	
	private ComboBox cmb_Category;
	private Upload 	fileBrowser;
	private File file;
	private ComboBox cmb_QueryType;
	@Inject
	private LumenDbService lumenDbService; 
	@Inject
	private MISUploadedDocumentTable documentTable;
	private FileData fileData;	
	
	private TextArea queryRemarks;
	
	private int queryToBeRepliedIndex = 0;
	private long queryToBeRepliedKey = 0;

	@PostConstruct
	public void initView() {
		addStyleName("view");
		setSizeFull();			
	}

	@SuppressWarnings("unchecked")
	public void initView(LumenRequestDTO resultDTO) {
		this.dtoBean = null;
		this.dtoBean = resultDTO;
		mainPanel = new VerticalLayout();
		lumenService.getCarouselDetailsFromLumen(resultDTO);
		CommonLumenCarousel lumenCarousel = carouselInstance.get();
		lumenCarousel.init(dtoBean, "Lumen Query Initiated to MIS");

		Map<String, Object> coreObj = lumenService.getAllLumenDetails(dtoBean);
		Policy policy = lumenService.getPolicyByPolicyNumber(resultDTO.getPolicyNumber());
		Long policykey =  null;
		if(policy != null){
			policykey = policy.getKey();
		}else{
			policykey = 0L;
		}
		
		viewDetails.initView(resultDTO.getIntimationNumber(), policykey, ViewLevels.LUMEN_TRAILS, false,"Lumen Trails Page loaded.........", false);
		viewDetails.setLumenKey(resultDTO.getLumenRequestKey());
		HorizontalLayout horLayout_1 = new HorizontalLayout();
		horLayout_1.setWidth("100%");
		horLayout_1.addComponent(viewDetails);
		horLayout_1.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);



		//---------------------loading Reply From MIS table--------------------------

		listOfMISQryDetailsObj = (List<MISQueryReplyDTO>) coreObj.get("MISQueryObj");
		
		for(MISQueryReplyDTO rec : listOfMISQryDetailsObj){
			if(rec.getRepliedDate() == null){
				queryToBeRepliedIndex = listOfMISQryDetailsObj.indexOf(rec);
				queryToBeRepliedKey = rec.getQueryKey();
				System.out.println("listOfMISQryDetailsObj "+listOfMISQryDetailsObj.get(queryToBeRepliedIndex).getQueryKey());
			}
		}

		//------------------End of loading Reply From MIS table------------------------
		listOfQryDtlObj = (List<LumenQueryDetailsDTO>) coreObj.get("QueryDetailsObjList");
		System.out.println("listOfQryDtlObj "+listOfQryDtlObj);
		ListIterator<LumenQueryDetailsDTO> iter = listOfQryDtlObj.listIterator();
		while(iter.hasNext()){
		    if(iter.next().getLumenQuery().getKey() != queryToBeRepliedKey){
		        iter.remove();
		    }
		}

		misQueryDetails.init(listOfMISQryDetailsObj.get(queryToBeRepliedIndex), "MIS Query Initiation Details");
		if(!listOfQryDtlObj.isEmpty()){
			for (LumenQueryDetailsDTO r: listOfQryDtlObj) {
				r.setSno(listOfQryDtlObj.indexOf(r)+1);
			}
		}

		//MIS Query Details Table Construction
		qrMISTableObj = qrMISTableInstance.get();
		qrMISTableObj.init(new LumenQueryDetailsDTO());
		qrMISTableObj.setVisibleColumns();
		qrMISTableObj.addList(listOfQryDtlObj);

		/*Page<LumenQueryDetailsDTO> page = new Page<LumenQueryDetailsDTO>();
		page.setPageItems(listOfQryDtlObj);
		page.setTotalRecords(listOfQryDtlObj.size());
		page.setTotalList(listOfQryDtlObj);
		qrMISTableObj.setTableList(page.getTotalList());
		qrMISTableObj.setSizeFull();*/

		HorizontalLayout horLayout_2 = new HorizontalLayout();
		horLayout_2.addComponent(misQueryDetails);
		horLayout_2.setSpacing(true);
		horLayout_2.setWidth("100%");

		//Upload Grid Layout
		btnUpload = new Button();
		btnUpload.setCaption("Upload");
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		btnUpload.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(!validateUpload()){
					fileBrowser.submitUpload();
				}else{
					showErrorMessage(errMsg.toString());
				}
			}
		});

		VerticalLayout gLayout = new VerticalLayout(buildGridlayoutComponents());
		VerticalLayout buttonLayout = new VerticalLayout(btnUpload);
		buttonLayout.setComponentAlignment(btnUpload, Alignment.BOTTOM_LEFT);
		buttonLayout.setWidth("100%");
		HorizontalLayout fieldLayout = new HorizontalLayout(gLayout,buttonLayout);

		//Uploaded Document Table
		documentTable.init("", false, false);
		
		
		queryRemarks = new TextArea("Reply Remarks");
		queryRemarks.setRows(6);
		queryRemarks.setColumns(22);
		queryRemarks.setMaxLength(4000);
		handleTextAreaPopup(queryRemarks,null);
		
		FormLayout tempLayout = new FormLayout();
		tempLayout.addComponent(queryRemarks);
		
		/*HorizontalLayout containerHorlayout = new HorizontalLayout();
		containerHorlayout.addComponent(queryRemarks);
		containerHorlayout.setMargin(true);
		containerHorlayout.setSizeFull();*/

		AbsoluteLayout actionLayout =  new AbsoluteLayout();
		actionLayout.addComponent(tempLayout, "left: 30%; top: 0%;");
		actionLayout.setWidth("100%");
		actionLayout.setHeight("170px");
		
		mainPanel.addComponent(lumenCarousel);
		mainPanel.addComponent(horLayout_1);
		mainPanel.addComponent(horLayout_2);
		mainPanel.addComponent(qrMISTableObj);
		mainPanel.addComponent(fieldLayout);
		mainPanel.addComponent(documentTable);
		mainPanel.addComponent(actionLayout);
		mainPanel.addComponent(addFooterButtons());
		mainPanel.setSpacing(true);
		mainPanel.setSizeFull();

		setCompositionRoot(mainPanel);			
	}

	public VerticalLayout addFooterButtons(){
		HorizontalLayout buttonsLayout = new HorizontalLayout();

		Button	cancelButton = new Button("Cancel");
		cancelButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(ProcessMISWizardPresenter.CANCEL_QUERY_MIS_REQUEST,null);
			}
		});

		Button	submitButton = new Button("Submit");
		submitButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(!validatePage()){
					fireViewEvent(ProcessMISWizardPresenter.SUBMIT_QUERY_MIS_REQUEST,dtoBean);
				}else{
					showErrorMessage(errMsg.toString());
				}				
			}
		});

		buttonsLayout.addComponents(cancelButton,submitButton);
		buttonsLayout.setSpacing(true);
		VerticalLayout	btnLayout = new VerticalLayout(buttonsLayout);
		btnLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);

		return btnLayout;
	}



	@Override
	public void resetView() {

	}

	public void cancelLumenRequest(){
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
				"Are you sure you want to cancel ?",
				"No", "Yes", new ConfirmDialog.Listener() {

			private static final long serialVersionUID = 1L;
			public void onClose(ConfirmDialog dialog) {
				if (dialog.isCanceled() && !dialog.isConfirmed()) {
					// YES
					fireViewEvent(MenuItemBean.LUMEN_REQUEST_MIS, null);
				}
			}
		});

		dialog.setStyleName(Reindeer.WINDOW_BLACK);
	}

	public void submitLumenRequest(){
		try {
			MISQueryReplyDTO misQueryObj = listOfMISQryDetailsObj.get(queryToBeRepliedIndex);
			dtoBean.setMisQueryObj(misQueryObj);
			dtoBean.setUpdatedMISReplyRemarksList(qrMISTableObj.getValues());
			String replyRemarks = (queryRemarks.getValue() == null)?"":queryRemarks.getValue();
			dtoBean.setQueryTableReplyRemarks(replyRemarks);
			
			dtoBean.setUserActionName("MIS Reply");
			
			lumenService.saveLumenMISQueryRequest(dtoBean);
			String msg = "Lumen MIS Query Changes Submitted Successfully </br>";
			showSubmitMessagePanel(msg);
		} catch (Exception e) {
			String msg = "Exception Occurred While merging Lumen MIS Query Changes </br>";
			showSubmitMessagePanel(msg);
			e.printStackTrace();
		}
	}

	private boolean validatePage() {
		Boolean hasError = false;
		errMsg.setLength(0);
		
		List<LumenQueryDetailsDTO> listOfValues = qrMISTableObj.getValues();
		for(LumenQueryDetailsDTO r:listOfValues){
			//System.out.println("Value of :"+r.getReplyRemarks());
			if(StringUtils.isBlank(r.getReplyRemarks())){
				hasError = true;
				errMsg.append("Please reply for all the raised queries before submit. </br>");
				break;
			}
		}
		System.out.println("Error Occurred : "+errMsg.toString());
		return hasError;
	}


	@SuppressWarnings("static-access")
	private void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
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


	@SuppressWarnings("static-access")
	public void showSubmitMessagePanel(String messageInfo){
		Label label = new Label(messageInfo, ContentMode.HTML);

		Button homeButton = new Button("Home Page");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(label, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);

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
				fireViewEvent(MenuItemBean.LUMEN_REQUEST_MIS, null);

			}
		});
	}



	private GridLayout buildGridlayoutComponents(){
		gridLayout = new GridLayout(3,2);

		Label typeLabel = new Label("Query Type");
		gridLayout.addComponent(typeLabel, 0,0);
		gridLayout.setComponentAlignment(typeLabel, Alignment.MIDDLE_CENTER);

		Label uploadLabel = new Label("File Upload"); 
		gridLayout.addComponent(uploadLabel, 1,0);
		gridLayout.setComponentAlignment(uploadLabel, Alignment.MIDDLE_CENTER);

		Label categoryLabel = new Label("File Category");
		gridLayout.addComponent(categoryLabel, 2,0);
		gridLayout.setComponentAlignment(categoryLabel, Alignment.MIDDLE_CENTER);

		cmb_QueryType = new ComboBox();
		gridLayout.addComponent(cmb_QueryType, 0,1);
		gridLayout.setComponentAlignment(cmb_QueryType, Alignment.MIDDLE_CENTER);

		fileBrowser = new Upload("", this);	
		fileBrowser.addSucceededListener(this);
		fileBrowser.setButtonCaption(null);

		gridLayout.addComponent(fileBrowser, 1,1);
		gridLayout.setComponentAlignment(fileBrowser, Alignment.MIDDLE_CENTER);

		cmb_Category = new ComboBox();
		gridLayout.addComponent(cmb_Category, 2,1);
		gridLayout.setComponentAlignment(cmb_Category, Alignment.MIDDLE_CENTER);

		gridLayout.setSpacing(true);

		setDropDownValues();
		return gridLayout;
	}

	@SuppressWarnings("unchecked")
	public void setDropDownValues(){
		BeanItemContainer<SelectValue> cmb_Category_values = lumenDbService.getFileCategory();
		cmb_Category.setContainerDataSource(cmb_Category_values);
		cmb_Category.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmb_Category.setItemCaptionPropertyId("value");

		List<SelectValue> defaultType = (List<SelectValue>)cmb_Category.getContainerDataSource().getItemIds();
		cmb_Category.setValue(defaultType.get(0));
		cmb_Category.setReadOnly(true);

		BeanItemContainer<SelectValue> cmb_fileType_values = new BeanItemContainer<SelectValue>(SelectValue.class);//lumenDbService.getFileTypes();
		SelectValue select = null;
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		for(int i = 0; i < listOfQryDtlObj.size(); i++){
			select = new SelectValue();
			select.setValue("Query "+(i+1));
			selectValuesList.add(select);
		}
		cmb_fileType_values.addAll(selectValuesList);
		cmb_QueryType.setContainerDataSource(cmb_fileType_values);
		cmb_QueryType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmb_QueryType.setItemCaptionPropertyId("value");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void uploadSucceeded(SucceededEvent event) {

		try {
			byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
			String fileName = event.getFilename();
			if (null != fileAsbyteArray) {
				//file gets uploaded in data directory when code comes here.
				if(null != event && null != event.getFilename() && (event.getFilename().endsWith("jpg") || event.getFilename().endsWith("jpeg") ||
						event.getFilename().endsWith("JPG") || event.getFilename().endsWith("JPEG")))
				{
					File convertedFile  = SHAFileUtils.convertImageToPDF(event.getFilename());
					fileName = convertedFile.getName();
					fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);
				}

				WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
				Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
				//TO read file after load
				if (flagUploadSuccess.booleanValue())
				{
					String token = String.valueOf(uploadStatus.get("fileKey"));
					fileData = new FileData();
					fileData.setFileName(fileName);
					fileData.setToken(token);
					DocumentDetails temp = lumenDbService.updateMISDocumentTable(dtoBean,fileData);
					if(temp != null){
						dtoBean.setUserUploadedDocument(temp);
						dtoBean.getListOfUserUploadedDocuments().add(dtoBean.getUserUploadedDocument());
					}
					documentTable.setListOfUserUploadedDocuments(dtoBean.getListOfUserUploadedDocuments());
					
					List<DocumentAckTableDTO> uploadedDocumentsList = new ArrayList<DocumentAckTableDTO>();
					String selectedQueryName  = ((SelectValue)cmb_QueryType.getValue()).getValue();
					LumenQueryDetailsDTO queryRec = listOfQryDtlObj.get(Integer.parseInt(selectedQueryName.substring(selectedQueryName.length() - 1)) -1);
					DocumentAckTableDTO docAckObj = lumenDbService.prepareDocumentAckTableData(queryRec, temp);
					dtoBean.getListOfAckTableDTO().add(docAckObj);
					uploadedDocumentsList = dtoBean.getListOfAckTableDTO();
					//setting the data for updating/marking the user deleted records...
					documentTable.setListOfAckTableDocuments(uploadedDocumentsList);
				
					documentTable.removeRow();
					
					List<DocumentAckTableDTO> retainedList = new ArrayList<DocumentAckTableDTO>();				
					for(DocumentAckTableDTO rec : uploadedDocumentsList){
						if(rec.getDeletedFlag().equals("N")){
							retainedList.add(rec);
						}
					}

					Page<DocumentAckTableDTO> page = new Page<DocumentAckTableDTO>();
					page.setPageItems(retainedList);
					page.setTotalRecords(retainedList.size());
					page.setTotalList(retainedList);

					documentTable.setTableList(retainedList);
					documentTable.setSubmitTableHeader();
					documentTable.tablesize();
					documentTable.setHasNextPage(page.isHasNext());
				} else {
					Notification.show("Error", "" + uploadStatus.get("message"), Type.ERROR_MESSAGE);
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
/*	public void loadUploadedDocuments(String intimationNumber){
		List<DocumentTableDTO> uploadedDocumentsList = null;
		documentTable.setListOfUserUploadedDocuments(dtoBean.getListOfUserUploadedDocuments());
		uploadedDocumentsList = lumenDbService.getDocumentDetailsFromCache(dtoBean.getListOfUserUploadedDocuments());

		for(DocumentTableDTO rec : uploadedDocumentsList){
			if(rec.getDeletedFlag().equals("N")){
				documentTable.addBeanToList(rec);
			}
		}
	}*/
	
	public void reloadDocumentPopup(DocumentAckTableDTO docTblDTO){
		List<DocumentAckTableDTO> uploadedDocumentsList = null;
		uploadedDocumentsList = documentTable.getListOfAckTableDocuments();
		//Merging dtoBean list of Ack document with deleted changes, which can be used for persisting/saving purpose.
		dtoBean.setListOfAckTableDTO(uploadedDocumentsList);
		documentTable.removeRow();
		for(DocumentAckTableDTO rec : uploadedDocumentsList){
			if(rec.getDeletedFlag().equals("N")){
				documentTable.addBeanToList(rec);
			}
		}
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		FileOutputStream fos = null;
		try {
			this.file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
			if(null != file){
				fos = new FileOutputStream(file);
			}else{
				Notification.show("Error", "" + "Please select the file to be uploaded", Type.ERROR_MESSAGE);
			}
		} catch (FileNotFoundException e) {
			Notification.show("Error", "" + "Please select the file to be uploaded", Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return fos;
	}

	private boolean validateUpload() {
		Boolean hasError = false;
		errMsg.setLength(0);

		if(cmb_QueryType.getValue() == null){
			hasError = true;
			errMsg.append("Please select query type drop-down value and click the upload button. </br>");
		}

		return hasError;
	}
	
	@SuppressWarnings("unused")
	public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRedraft(searchField, getShortCutListenerForRemarks(searchField));

	}

	public  void handleShortcutForRedraft(final TextArea textField, final ShortcutListener shortcutListener) {
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
	
	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(4000);
				txtArea.setReadOnly(false);
				txtArea.setRows(25);

				txtArea.addValueChangeListener(new ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();

				String strCaption = "Reply Remarks";

				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};

		return listener;
	}
}
