package com.shaic.claim.lumen.searchleveltwo;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.lumen.CommonLumenCarousel;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.LumenDetailsDTO;
import com.shaic.claim.lumen.LumenQueryDTO;
import com.shaic.claim.lumen.LumenQueryDetailsDTO;
import com.shaic.claim.lumen.components.CoordinatorApprovalDetails;
import com.shaic.claim.lumen.components.LetterDetailsDTO;
import com.shaic.claim.lumen.components.LevelOneApprovalDetails;
import com.shaic.claim.lumen.components.LumenRequestProcessLayout;
import com.shaic.claim.lumen.components.LumenTypeEditablelayoutComponents;
import com.shaic.claim.lumen.components.MISDTO;
import com.shaic.claim.lumen.components.MISDocumentDTO;
import com.shaic.claim.lumen.components.MISQueryReplyDTO;
import com.shaic.claim.lumen.components.MISQueryReplyDetails;
import com.shaic.claim.lumen.components.MISSubDTO;
import com.shaic.claim.lumen.components.MISSubTableViews;
import com.shaic.claim.lumen.components.QueryDetails;
import com.shaic.claim.lumen.components.ReplyDetails;
import com.shaic.claim.lumen.components.RequestInitiationDetails;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.shaic.claim.lumen.upload.LumenUploadDocumentsViewImpl;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ProcessLevelTwoWizardImpl extends AbstractMVPView implements ProcessLevelTwoWizard{

	@Inject
	private Instance<CommonLumenCarousel> carouselInstance;

	@Inject
	private ViewDetails viewDetails;

	private VerticalLayout mainPanel;
	private LumenRequestDTO dtoBean;
	private StringBuilder errMsg = new StringBuilder();

	@EJB
	private MasterService masterService;
	
	@EJB
	private IntimationService intimationService;

	@Inject
	private LumenDbService lumenService;

	private Button uploadButton;

	private Button viewDocumentsButton;

	private Button replyFromMISButton;

	@Inject
	private LumenUploadDocumentsViewImpl uploadDocumentView;

	@Inject
	private RequestInitiationDetails initiationDetailsPanel;
	
	@Inject
	private LevelOneApprovalDetails level1ApprovalRemarksPanel;
	
	@Inject
	private CoordinatorApprovalDetails coordinatorApprovalRemarksPanel;

	@Inject
	private QueryDetails queryTable;

	@Inject
	private ReplyDetails replyTable;

	@Inject
	private MISQueryReplyDetails misReplyDetails;
	
	@Inject
	private MISSubTableViews misSubViews;

	@Inject
	private LumenRequestProcessLayout functionalbuttonLayout;

	@Inject
	private LumenTypeEditablelayoutComponents lumenTypelayout;

	private List<LumenDetailsDTO> listOfDetailsObj;
	private List<LumenQueryDTO> listOfQryObj;
	private List<LumenQueryDetailsDTO> listOfQryDetailsObj;
	private List<MISQueryReplyDTO> listOfMISQryDetailsObj;
	
	private List<LumenQueryDetailsDTO> onlylistOfReplyObj;

	@PostConstruct
	public void initView() {
		addStyleName("view");
		setSizeFull();			
	}

	@SuppressWarnings("unchecked")
	public void initView(LumenRequestDTO resultDTO) {
		this.dtoBean =null;
		this.dtoBean = resultDTO;
		mainPanel = new VerticalLayout();
		lumenService.getCarouselDetailsFromLumen(resultDTO);
		CommonLumenCarousel lumenCarousel = carouselInstance.get();
		lumenCarousel.init(dtoBean, "Process Lumen Request - Level II");

		Map<String, Object> coreObj = lumenService.getAllLumenDetails(dtoBean);
		Policy policy = lumenService.getPolicyByPolicyNumber(resultDTO.getPolicyNumber());
		Long policykey =  null;
		if(policy != null){
			policykey = policy.getKey();
		}else{
			policykey = 0L;
		}
		
		viewDetails.initView(resultDTO.getIntimationNumber(), policykey, ViewLevels.LUMEN_TRAILS, false,"Lumen Trails Page loaded.........", true);
		viewDetails.setLumenKey(resultDTO.getLumenRequestKey());
		HorizontalLayout horLayout_1 = new HorizontalLayout();
		horLayout_1.setWidth("100%");


		initiationDetailsPanel.init(resultDTO, "Request Initiation Details");
		level1ApprovalRemarksPanel.init(resultDTO, "Level I Approval Remarks");
		coordinatorApprovalRemarksPanel.init(resultDTO, "Coordinator Approval Remarks");
		queryTable.init("Query Details", false, false);

		//-------------loading query table----------------------
		listOfQryObj = (List<LumenQueryDTO>) coreObj.get("QueryObj");
		//System.out.println("listOfQryObj "+listOfQryObj);
		//Taking query object for persisting and merging purpose.
		if(listOfQryObj != null && !listOfQryObj.isEmpty()){
			for(LumenQueryDTO rec :listOfQryObj){
				dtoBean.getLumenQueryKey().add(rec.getQueryKey());
			}
		}

		Page<LumenQueryDTO> page = new Page<LumenQueryDTO>();
		page.setPageItems(listOfQryObj);
		page.setTotalRecords(listOfQryObj.size());
		page.setTotalList(listOfQryObj);
		queryTable.setTableList(page.getTotalList());
		queryTable.setSubmitTableHeader();
		queryTable.setSizeFull();
		//-----------------End of loading query table--------------

		//---------------loading reply table------------------------
		
		replyTable.init("Reply Details", false, false);
		// nulling the list for every load.
		onlylistOfReplyObj = null;
		if(listOfQryObj.size() > 0){
			listOfQryDetailsObj = (List<LumenQueryDetailsDTO>) coreObj.get("QueryDetailsObjList");
			//System.out.println("listOfQryDetailsObj "+listOfQryDetailsObj);
			
			onlylistOfReplyObj = new ArrayList<LumenQueryDetailsDTO>();
			for(Long queryKey : dtoBean.getLumenQueryKey()){
				for(LumenQueryDetailsDTO rec : listOfQryDetailsObj){
					if(rec.getLumenQuery().getKey() == queryKey && rec.getLumenQuery().getRepliedDate() != null){
						onlylistOfReplyObj.add(rec);
					}
				}
			}	

			Page<LumenQueryDetailsDTO> replyPage = new Page<LumenQueryDetailsDTO>();
			replyPage.setPageItems(onlylistOfReplyObj);
			replyPage.setTotalRecords(onlylistOfReplyObj.size());	
			replyPage.setTotalList(onlylistOfReplyObj);
			replyTable.setTableList(replyPage.getTotalList());
			replyTable.setSubmitTableHeader();
			replyTable.setSizeFull();
		}
		//-----------------End of loading reply table--------------
		
		//---------------------loading Reply From MIS table--------------------------
		
		listOfMISQryDetailsObj = (List<MISQueryReplyDTO>) coreObj.get("MISQueryObj");
		//System.out.println("listOfMISQryDetailsObj "+listOfMISQryDetailsObj);
		
		//------------------End of loading Reply From MIS table------------------------
		uploadButton = new Button();
		uploadButton.setCaption("Upload Document (Lumen)");
		uploadButton.setWidth("-1");
		uploadButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				showDocumentUploadView();
			}
		});

		viewDocumentsButton = new Button();
		viewDocumentsButton.setCaption("View Uploaded Documents (Lumen)");
		viewDocumentsButton.setWidth("-1");
		viewDocumentsButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				viewUploadedDocumentDetails(dtoBean.getIntimationNumber(), String.valueOf(dtoBean.getLumenRequestKey()));
			}
		});

		replyFromMISButton = new Button();
		replyFromMISButton.setCaption("Reply From MIS");
		replyFromMISButton.setWidth("-1");
		replyFromMISButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				showReplyFromMISView();
			}
		});

		HorizontalLayout buttonHContainer = new HorizontalLayout(); 

		buttonHContainer.addComponent(uploadButton);
		buttonHContainer.addComponent(viewDocumentsButton);
		buttonHContainer.addComponent(replyFromMISButton);
		buttonHContainer.setSpacing(true);

		VerticalLayout tableContainer = new VerticalLayout();
		tableContainer.addComponent(queryTable);
		tableContainer.addComponent(replyTable);

		HorizontalLayout horLayout_2 = new HorizontalLayout();
		horLayout_2.addComponent(initiationDetailsPanel);
		horLayout_2.addComponent(level1ApprovalRemarksPanel);
		horLayout_2.addComponent(coordinatorApprovalRemarksPanel);
		horLayout_2.setSpacing(true);
		horLayout_2.setWidth("100%");
		
		HorizontalLayout horLayout_3 = new HorizontalLayout();
		horLayout_3.addComponent(tableContainer);
		horLayout_3.setSpacing(true);
		horLayout_3.setWidth("100%");

		horLayout_1.addComponent(buttonHContainer);
		horLayout_1.addComponent(viewDetails);
		horLayout_1.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		horLayout_1.setComponentAlignment(buttonHContainer, Alignment.TOP_LEFT);

		if(listOfQryObj != null && onlylistOfReplyObj != null){
			if(listOfQryObj.size() != onlylistOfReplyObj.size()){
				//enabling only reply button when query is raised.
				functionalbuttonLayout.init(false, false, true, false, false, false,"Process Level II");
			}else{
				functionalbuttonLayout.init(true, true, false, true, true, true,"Process Level II");
			}
		}else{
			functionalbuttonLayout.init(true, true, false, true, true, true,"Process Level II");
		}
		functionalbuttonLayout.setActionName("");
		if(listOfQryObj != null && listOfQryObj.size() > 0){
			for(LumenQueryDTO rec : listOfQryObj){
				if(rec.getRepliedDate() == null){
					functionalbuttonLayout.setQueryRaisedRole(rec.getQueryRaisedRole());
				}else{
					functionalbuttonLayout.setQueryRaisedRole("");
				}
			}
		}else{
			functionalbuttonLayout.setQueryRaisedRole("");
		}
		
		HorizontalLayout horLayout_4 = new HorizontalLayout();
		horLayout_4.addComponent(functionalbuttonLayout);
		horLayout_4.setComponentAlignment(functionalbuttonLayout, Alignment.MIDDLE_CENTER);
		horLayout_4.setSizeFull();

		listOfDetailsObj = (List<LumenDetailsDTO>) coreObj.get("DetailsObj");

		Map<String, Object> argMapObj = new HashMap<String, Object>();
		argMapObj.put("lumenObj", resultDTO);
		argMapObj.put("participantObj", listOfDetailsObj);

		lumenTypelayout.init(argMapObj, true);
		lumenTypelayout.makeFieldsReadOnly(false);

		mainPanel.addComponent(lumenCarousel);
		mainPanel.addComponent(horLayout_1);
		mainPanel.addComponent(horLayout_2);
		mainPanel.addComponent(horLayout_3);
		mainPanel.addComponent(lumenTypelayout);
		mainPanel.addComponent(horLayout_4);
		mainPanel.addComponent(addFooterButtons());
		mainPanel.setSizeFull();		

		setCompositionRoot(mainPanel);			
	}

	public VerticalLayout addFooterButtons(){
		HorizontalLayout buttonsLayout = new HorizontalLayout();

		Button	cancelButton = new Button("Cancel");
		cancelButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(ProcessLevelTwoWizardPresenter.CANCEL_LEVEL_II_REQUEST,null);
			}
		});

		Button	submitButton = new Button("Submit");
		submitButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(!validatePage()){
					fireViewEvent(ProcessLevelTwoWizardPresenter.SUBMIT_LEVEL_II_REQUEST,dtoBean);
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
					fireViewEvent(MenuItemBean.LUMEN_REQUEST_LEVEL_II, null);
				}
			}
		});

		dialog.setStyleName(Reindeer.WINDOW_BLACK);
	}

	@SuppressWarnings("rawtypes")
	public void submitLumenRequest(){

		String actionName = functionalbuttonLayout.getActionName();

		if(actionName.equals("Close")){
			dtoBean.setLevel1CloseRemarks(functionalbuttonLayout.getCloseComments().getValue());
		}

		if(actionName.equals("Reject")){
			dtoBean.setLevel1RejectRemarks(functionalbuttonLayout.getRejectComments().getValue());
		}

		if(actionName.equals("Query")){
			dtoBean.setLevel1QueryRemarks(functionalbuttonLayout.getQueryComments().getValue());
			dtoBean.setLevel1QueryTo(String.valueOf(functionalbuttonLayout.getCmbQueryTo().getValue()));
		}
		if(actionName.equals("Reply")){
			dtoBean.setLevel1ReplyRemarks(functionalbuttonLayout.getReplyComments().getValue());
			dtoBean.setLevel1ReplyTo(String.valueOf(functionalbuttonLayout.getCmbReplyTo().getValue()));
		}
		
		if(actionName.equals("Approve")){
			dtoBean.setGenerateLetterFlag((Boolean)functionalbuttonLayout.getGenerateLetterOption().getValue());
			if(dtoBean.getGenerateLetterFlag()){
				List<LetterDetailsDTO> listOfLetters = functionalbuttonLayout.getLetterTableObj().getValues();
				dtoBean.setListOfApprovalLetters(listOfLetters);
			}
			dtoBean.setLevel1ApprovalRemarks(functionalbuttonLayout.getApproveComments().getValue());
		}

		if(actionName.equals("Refer to MIS")){
			//getting data from Refer to MIS table.....
			List<String> misQry = new ArrayList<String>();
			List<MISDTO> mislist = functionalbuttonLayout.getReferToMISTableObj().getValues();
			if(mislist != null && !mislist.isEmpty()){
				for(MISDTO  rec : mislist){
					String qryContent = rec.getQueryContent();
					if(!StringUtils.isBlank(qryContent)){
						misQry.add(qryContent);
					}
				}
			}
			dtoBean.setMisQryList(misQry);
		}

		dtoBean.setUserActionName(actionName);
		SelectValue tempSelect = (SelectValue) lumenTypelayout.getCmbLumenType().getValue();
		dtoBean.setLumenTypeId(tempSelect.getId());
//		if(tempSelect.getValue().equals("Hospital Errors")){
//			tempSelect = (SelectValue) lumenTypelayout.getCmbErrorType().getValue();
//			dtoBean.setErrorTypeId(tempSelect.getId());
//		}else{
//			dtoBean.setErrorTypeId(null);
//		}

		if(lumenTypelayout.getTxtComments() == null){
			dtoBean.setRemarks(null);
		}else{
			dtoBean.setRemarks(lumenTypelayout.getTxtComments().getValue());
		}


		//getting data from participants table.....
		List<String> participants = new ArrayList<String>();
		Collection<?> parlist = lumenTypelayout.getParticipantsOtherDetails().getTable().getItemIds();
		if(parlist != null && !parlist.isEmpty()){
			Property property = null;
			for(Object  rec : parlist){
				property= lumenTypelayout.getParticipantsOtherDetails().getTable().getContainerProperty(rec, "empName");
				if(property.getValue() != null){
					participants.add(String.valueOf(property.getValue()));
				}
			}
		}
		dtoBean.setListOfParticipants(participants);

		//getting data from lapse table.....
		List<String> lapse = new ArrayList<String>();
		Collection<?> laplist = lumenTypelayout.getLapseDetails().getTable().getItemIds();
		if(laplist != null && !laplist.isEmpty()){
			Property property = null;
			for(Object  rec : laplist){
				property = lumenTypelayout.getLapseDetails().getTable().getContainerProperty(rec, "empName");
				if(property.getValue() != null){
					lapse.add(String.valueOf(property.getValue()));
				}
			}
		}
		dtoBean.setListOfLapse(lapse);

		if(actionName.equals("Approve") && (participants.size() == 0 && lapse.size() == 0)){
			showErrorMessage("Please add atleast One Participant(Others) or One Lapse Participants to proceed");
			return;
		}else{

			try {
				lumenService.saveLumenLevelTwoRequest(dtoBean);
				String msg = "Lumen Level 2 Request Changes Submitted Successfully </br>";
				showSubmitMessagePanel(msg);
			} catch (Exception e) {
				String msg = "Exception Occurred While merging Lumen level 2 Request </br>";
				showSubmitMessagePanel(msg);
				e.printStackTrace();
			}
		}
	}

	private boolean validatePage() {
		Boolean hasError = false;
		errMsg.setLength(0);

		if(lumenTypelayout.getCmbLumenType().getValue() == null){
			hasError = true;
			errMsg.append("Please select lumen type </br>");
		}
//		else{
//			if(lumenTypelayout.getCmbLumenType().getValue().toString().equals("Hospital Errors")){
//				if(lumenTypelayout.getCmbErrorType().getValue() == null){
//					hasError = true;
//					errMsg.append("Please select lumen hospital error type </br>");
//				}
//			}
//		}

		String actionName = functionalbuttonLayout.getActionName();

		if(StringUtils.isBlank(actionName)){
			hasError = true;
			errMsg.append("Please perform functional operations like Approve/Reject/Query/Close/Refer to MIS before submit </br>");
		}else{
			if(actionName.equals("Close")){
				if(functionalbuttonLayout.getCloseComments().getValue() == null || StringUtils.isBlank(functionalbuttonLayout.getCloseComments().getValue())){
					hasError = true;
					errMsg.append("Please provide Closure Comments. </br>");
				}
			}

			if(actionName.equals("Reject")){
				if(functionalbuttonLayout.getRejectComments().getValue() == null || StringUtils.isBlank(functionalbuttonLayout.getRejectComments().getValue())){
					hasError = true;
					errMsg.append("Please provide Rejection Comments. </br>");
				}
			}

			if(actionName.equals("Query")){
				if(functionalbuttonLayout.getCmbQueryTo().getValue() == null){
					hasError = true;
					errMsg.append("Please Select Query To value. </br>");
				}

				if(functionalbuttonLayout.getQueryComments().getValue() == null || StringUtils.isBlank(functionalbuttonLayout.getQueryComments().getValue())){
					hasError = true;
					errMsg.append("Please provide Query Remarks. </br>");
				}
			}
			if(actionName.equals("Approve")){
				if(functionalbuttonLayout.getGenerateLetterOption() == null){
					hasError = true;
					errMsg.append("Please select any generate Letter option before submit. </br>");
				}

				if(((Boolean)functionalbuttonLayout.getGenerateLetterOption().getValue()) == true && (functionalbuttonLayout.getLetterTableObj().getValues().size() == 0)){
					hasError = true;
					errMsg.append("Please create atleast one letter before submit </br>");
				}
				
				if(functionalbuttonLayout.getApproveComments().getValue() == null || StringUtils.isBlank(functionalbuttonLayout.getApproveComments().getValue())){
					hasError = true;
					errMsg.append("Please provide Approval Comments. </br>");
				}
				
				if(((Boolean)functionalbuttonLayout.getGenerateLetterOption().getValue()) == true && (functionalbuttonLayout.getLetterTableObj().getValues().size() > 0)){
					List<LetterDetailsDTO> listOfLetters = functionalbuttonLayout.getLetterTableObj().getValues();
					for(LetterDetailsDTO rec : listOfLetters){
						if(StringUtils.isBlank(rec.getToPerson())){
							hasError = true;
							errMsg.append("Please fill all letter To fields. </br>");
							break;
						}
						
						if(StringUtils.isBlank(rec.getSubject())){
							hasError = true;
							errMsg.append("Please fill all letter Subject fields. </br>");
							break;
						}
						
						if(StringUtils.isBlank(rec.getLetterContent())){
							hasError = true;
							errMsg.append("Please fill all Content of Letter fields. </br>");
							break;
						}
						
						if(StringUtils.isBlank(rec.getAddress())){
							hasError = true;
							errMsg.append("Please fill all letter address fields. </br>");
							break;
						}
					}
				}
			}

			if(actionName.equals("Refer to MIS")){
				List<MISDTO> misList = (List<MISDTO>) functionalbuttonLayout.getReferToMISTableObj().getValues();
				if(misList.size()  == 0){
					hasError = true;
					errMsg.append("Please provide MIS Query. </br>");
				}
				
				if(misList.size() >= 1){
					for(MISDTO rec : misList){
						if(StringUtils.isBlank(rec.getQueryContent())){
							hasError = true;
							errMsg.append("Query Description should not be empty. Please fill all row(s) Query Description </br>");
							break;
						}
					}
				}
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
				fireViewEvent(MenuItemBean.LUMEN_REQUEST_LEVEL_II, null);

			}
		});
	}

	private Button btnClose;

	public void showDocumentUploadView(){
		final Window popup = new com.vaadin.ui.Window();
		// include the content here like viewlumenTrialsTable
		uploadDocumentView.init(this.dtoBean,"Lumen Documents Upload");

		// close button in window
		btnClose = new Button();
		btnClose.setCaption("Close");
		btnClose.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnClose.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().removeWindow(popup);
			}
		});

		HorizontalLayout closebutLayout = new HorizontalLayout();
		closebutLayout.addComponent(btnClose);
		closebutLayout.setSizeFull();
		closebutLayout.setComponentAlignment(btnClose, Alignment.MIDDLE_CENTER);

		VerticalLayout uploadLayout = new VerticalLayout();
		uploadLayout.addComponent(uploadDocumentView);
		uploadLayout.addComponent(closebutLayout);
		uploadLayout.setSpacing(true);
		uploadLayout.setMargin(true);

		popup.setCaption("Upload Documents");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(uploadLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

	}

	@SuppressWarnings("unchecked")
	public void showReplyFromMISView(){
		misReplyDetails.init("Query and Reply Details From MIS", false, false);
		misReplyDetails.setScreenName("ProcessLevel2");

		Page<MISQueryReplyDTO> misPage = new Page<MISQueryReplyDTO>();
		misPage.setPageItems(listOfMISQryDetailsObj);
		misPage.setTotalRecords(listOfMISQryDetailsObj.size());
		misPage.setTotalList(listOfMISQryDetailsObj);
		misReplyDetails.setTableList(misPage.getTotalList());
		misReplyDetails.setSubmitTableHeader();
		misReplyDetails.setSizeFull();
		//------------------End of loading Reply From MIS table------------------------
		
		VerticalLayout misLayout = new VerticalLayout(misReplyDetails);
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(misLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

	}
	//MIS Sub/Nestd Popup
	@SuppressWarnings("unchecked")
	public void showReplyFromMISSubView(Map<String, List<?>> argDataHolder, String argReplyRemarks){
		List<MISSubDTO> listOfReplys = (List<MISSubDTO>) argDataHolder.get("replyData");
		List<MISDocumentDTO> listOfDocuments = (List<MISDocumentDTO>) argDataHolder.get("documentData");
		misSubViews.init(listOfReplys, listOfDocuments, argReplyRemarks);		
		VerticalLayout misLayout = new VerticalLayout(misSubViews);
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(misLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}

	public void viewUploadedDocumentDetails(String intimationNo, String lumenRequestKey) {
		BPMClientContext bpmClientContext = new BPMClientContext();
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 tokenInputs.put("lumenKey", lumenRequestKey);
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
		  } catch (NoSuchAlgorithmException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  } catch (ParseException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  tokenInputs = null;
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
		/*Below Code commented for security reason
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo + "&lumen-" + lumenRequestKey;*/
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
	}

//	@Inject
//	private Instance<GenerateLetterTable> letterTableInstance;
//	
//	private GenerateLetterTable letterTableObj;
	
/*	@Override
	public void showGenerateLetterTable(Boolean flag) {
		if(flag){
			FormLayout letterlayout = functionalbuttonLayout.getTempLayout();
			letterTableObj = letterTableInstance.get();
			letterTableObj.init(new LumenQueryDetailsDTO());
			letterTableObj.setVisibleColumns();
			letterlayout.addComponent(letterTableObj);
		}
	}*/
}
