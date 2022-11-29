package com.shaic.claim.lumen.initiatorquerycase;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
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

import com.shaic.arch.table.Page;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.lumen.CommonLumenCarousel;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.LumenDetailsDTO;
import com.shaic.claim.lumen.LumenQueryDTO;
import com.shaic.claim.lumen.LumenQueryDetailsDTO;
import com.shaic.claim.lumen.components.LumenRequestProcessLayout;
import com.shaic.claim.lumen.components.LumenTypeEditablelayoutComponents;
import com.shaic.claim.lumen.components.MISQueryReplyDetails;
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
public class ProcessInitiatorWizardImpl extends AbstractMVPView implements ProcessInitiatorWizard{

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

	@Inject
	private LumenUploadDocumentsViewImpl uploadDocumentView;

	@Inject
	private RequestInitiationDetails initiationDetailsPanel;

	@Inject
	private QueryDetails queryTable;

	@Inject
	private ReplyDetails replyTable;

	@Inject
	private MISQueryReplyDetails misReplyDetails;

	@Inject
	private LumenRequestProcessLayout functionalbuttonLayout;


	@Inject
	private LumenTypeEditablelayoutComponents lumenTypelayout;

	private List<LumenDetailsDTO> listOfDetailsObj;
	private List<LumenQueryDTO> listOfQryObj;
	private List<LumenQueryDetailsDTO> listOfQryDetailsObj;
	
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
		lumenCarousel.init(dtoBean, "Lumen Initiator");

		Map<String, Object> coreObj = lumenService.getAllLumenDetails(dtoBean);
		Policy policy = lumenService.getPolicyByPolicyNumber(resultDTO.getPolicyNumber());
		Long policykey =  null;
		if(policy != null){
			policykey = policy.getKey();
		}else{
			policykey = 0L;
		}
		
		viewDetails.initView(resultDTO.getIntimationNumber(), policykey, ViewLevels.LUMEN_TRAILS, false,"Lumen Trails Page loaded.........",false);
		viewDetails.setLumenKey(resultDTO.getLumenRequestKey());
		HorizontalLayout horLayout_1 = new HorizontalLayout();
		horLayout_1.setWidth("100%");

		initiationDetailsPanel.init(resultDTO, "Request Initiation Details");
		queryTable.init("Query Details", false, false);

		//-------------loading query table----------------------
		listOfQryObj = (List<LumenQueryDTO>) coreObj.get("QueryObj");
		System.out.println("listOfQryObj "+listOfQryObj);
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
		listOfQryDetailsObj = (List<LumenQueryDetailsDTO>) coreObj.get("QueryDetailsObjList");
		System.out.println("listOfQryDetailsObj "+listOfQryDetailsObj);
		
		onlylistOfReplyObj = new ArrayList<LumenQueryDetailsDTO>();
		for(Long queryKey : dtoBean.getLumenQueryKey()){
			for(LumenQueryDetailsDTO rec : listOfQryDetailsObj){
				if(rec.getLumenQuery().getKey() == queryKey){
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
		//-----------------End of loading reply table--------------
		uploadButton = new Button();
		uploadButton.setCaption("Upload Document (Lumen)");
		uploadButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				showDocumentUploadView();
			}
		});

		viewDocumentsButton = new Button();
		viewDocumentsButton.setCaption("View Uploaded Documents (Lumen)");
		viewDocumentsButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				viewUploadedDocumentDetails(dtoBean.getIntimationNumber(), String.valueOf(dtoBean.getLumenRequestKey()));
			}
		});
		// Hiding the View Uploaded Documents (Lumen) button as per the issue raised in CR-R1085.
		viewDocumentsButton.setVisible(false);

		HorizontalLayout buttonHContainer = new HorizontalLayout(); 

		buttonHContainer.addComponent(uploadButton);
		buttonHContainer.addComponent(viewDocumentsButton);
		buttonHContainer.setSpacing(true);

		VerticalLayout tableContainer = new VerticalLayout();
		tableContainer.addComponent(queryTable);
		tableContainer.addComponent(replyTable);
		
		HorizontalLayout horLayout_2 = new HorizontalLayout();
		horLayout_2.addComponent(initiationDetailsPanel);
		horLayout_2.addComponent(tableContainer);
		horLayout_2.setSpacing(true);
		horLayout_2.setWidth("100%");
		
		horLayout_1.addComponent(buttonHContainer);
		horLayout_1.addComponent(viewDetails);
		horLayout_1.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		horLayout_1.setComponentAlignment(buttonHContainer, Alignment.TOP_LEFT);

		functionalbuttonLayout.init(false, false, true, false, false, false,"Initiator Query Case");
		functionalbuttonLayout.setActionName("");
		if(listOfQryObj != null && listOfQryObj.size() > 0){
			for(LumenQueryDTO r : listOfQryObj){
				if(r.getRepliedDate() == null ){
					functionalbuttonLayout.setQueryRaisedRole(r.getQueryRaisedRole());
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

		lumenTypelayout.init(argMapObj, false);
		lumenTypelayout.makeFieldsReadOnly(true);

		mainPanel.addComponent(lumenCarousel);
		mainPanel.addComponent(horLayout_1);
		mainPanel.addComponent(horLayout_2);
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
				fireViewEvent(ProcessInitiatorWizardPresenter.CANCEL_INITIATOR_REQUEST,null);
			}
		});

		Button	submitButton = new Button("Submit");
		submitButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(!validatePage()){
					fireViewEvent(ProcessInitiatorWizardPresenter.SUBMIT_INITIATOR_REQUEST,dtoBean);
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
					fireViewEvent(MenuItemBean.LUMEN_REQUEST_INITIATOR, null);
				}
			}
		});

		dialog.setStyleName(Reindeer.WINDOW_BLACK);
	}

	public void submitLumenRequest(){
		String actionName = functionalbuttonLayout.getActionName();
		if(actionName.equals("Reply")){
			dtoBean.setLevel1ReplyRemarks(functionalbuttonLayout.getReplyComments().getValue());
			dtoBean.setLevel1ReplyTo(String.valueOf(functionalbuttonLayout.getCmbReplyTo().getValue()));
		}
		dtoBean.setUserActionName(actionName);
		try {
			lumenService.saveLumenInitiatorRequest(dtoBean);
			String msg = "Lumen Initiator Changes Submitted Successfully </br>";
			showSubmitMessagePanel(msg);
		} catch (Exception e) {
			String msg = "Exception Occurred While merging Initiator Qry Case Request";
			showSubmitMessagePanel(msg);
			e.printStackTrace();
		}
	}

	private boolean validatePage() {
		Boolean hasError = false;
		errMsg.setLength(0);
		String actionName = functionalbuttonLayout.getActionName();
		
		if(StringUtils.isBlank(actionName)){
			hasError = true;
			errMsg.append("Please perform functional Reply operation before submit </br>");
		}else{
			if(actionName.equals("Reply")){
				if(functionalbuttonLayout.getCmbReplyTo().getValue() == null){
					hasError = true;
					errMsg.append("Please Select Reply To value. </br>");
				}
				
				if(functionalbuttonLayout.getReplyComments().getValue() == null || StringUtils.isBlank(functionalbuttonLayout.getReplyComments().getValue())){
					hasError = true;
					errMsg.append("Please provide Reply Remarks. </br>");
				}
			}
		}
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
				fireViewEvent(MenuItemBean.LUMEN_REQUEST_INITIATOR, null);

			}
		});
	}

	public void showDocumentUploadView(){
		// include the content here like viewlumenTrialsTable
		uploadDocumentView.init(this.dtoBean,"Lumen Documents Upload");
		VerticalLayout uploadLayout = new VerticalLayout(uploadDocumentView);
		Window popup = new com.vaadin.ui.Window();
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

	public void showReplyFromMISView(){
		misReplyDetails.init("Query and Reply Details From MIS", false, false);
		VerticalLayout misLayout = new VerticalLayout(misReplyDetails);
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("40%");
		popup.setHeight("50%");
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
		/*Below Code Commented for Security Reason
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo + "&lumen-" + lumenRequestKey;*/
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
	}


}
