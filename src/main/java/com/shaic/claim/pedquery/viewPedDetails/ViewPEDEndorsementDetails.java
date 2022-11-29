package com.shaic.claim.pedquery.viewPedDetails;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.userproduct.document.UserProductMappingService;
import com.shaic.domain.MasRoleLimit;
import com.shaic.domain.MasUserLimitMapping;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.domain.preauth.PedEndorementHistory;
import com.shaic.domain.preauth.PedEndorsementDetailsHistory;
import com.shaic.domain.preauth.PedQuery;
import com.shaic.domain.preauth.PedSpecialist;
import com.shaic.domain.preauth.PreExistingDisease;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("unused")
public class ViewPEDEndorsementDetails extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object[] columns = new Object[] { "PEDCode", "Description",
			"ICDChapter", "ICDBlock", "ICDCode", "Source", "OthersSpecify",
			"DoctorRemarks" };

	private IndexedContainer iContainer;
	private VerticalLayout mainLayout;
	private Panel specialistAdviseDetailsPanel;
	private Panel escalatedDetailsPanel;
	private FormLayout specialistAdviseDetailsFormLayout;
	private FormLayout escalatedDetailsFormLayout;
	private TextArea specialistRemarksTxt;
	private Label specialistRemarkslbl;
	private TextField pedStatusSpecialistAdvisetxt;
	private Label pedStatusSpecialistAdvisetlab;
	private Panel requestProcessedQueryRepliedDetailsPanel;
	private FormLayout queryRepliedDetailsFormLayout;
	private TextArea queryReplyRemarksTxt;
	private Label queryReplyRemarksProcessLbl;
	private TextField pedQuerStatusTxt;
	private Label queryReplyRemarksLbl;
	private Panel pedRequestApprovalDetailsPanel;
	private FormLayout RequestApprovalDetailsFormLayout;
	private TextField approvalRemarksTxt;
	private Label approvalRemarksLbl;
	private TextField rejectionRemarksTxt;
	private Label rejectionRemarksLbl;
	private TextField reasonforReferringTxt;
	private Label reasonforReferringLbl;
	private TextField specialisttypeSelect;
	private Label specialistTypeLbl;
	private TextField queryRemarksrtxt;
	private TextField specialisttype;
	private TextField queryRemarktxt;
	private Label queryRemarksrLbl;
	private TextField pEDStatusApprovalDetailstxt;
	private Label pEDStatusApprovalDetailsLbl;
	private Panel pedRequestProcessedDetailsPanel;
	private Panel pedRequestdetailsPanel;
	private FormLayout pedRequestProcessedDetailsFormLayout;
	private FormLayout pedRequestorDetailsLayout;
	private TextField reviewRemarksTxt;
	private Label reviewRemarksLbl;
	private TextField ReasoforReferringProcessorTxt;
	private Label ReasoforReferringProcessorLbl;
	private TextField specialistTypenativeSelect;
	private Label specialistTypelbl;
	private TextField pedStatusProcessedTxt;
	private Label pedStatusProcessedLbl;
	private Panel pedRequestInitiationDetailspanel;
	private VerticalLayout pedRequestInitiationDetailsverticalLayout;
	private FormLayout requestorDetailsFormLayout;
	private TextField requestedDateTxt;
	private Label requestedDateLbl;
	private TextField requestorIDorNameTxt;
	private Label requestorIDorNameLbl;
	private TextField requestorRolText;
	private Label requestorRolLbl;
	private VerticalLayout pedTableverticalLayout;
	private FormLayout requestInitiationFormLayout;
	private TextField repudiationLetterDateTxt;
	private Label repudiationLetterDateLbl;
	private TextArea pedRequsetInitialtionRemarksTxt;
	private Label pedRequsetInitialtionRemarksLbl;
	private TextArea nameofPEDTxt;
	private Label nameofPEDLbl;
	private TextArea pedSuggestionTxt;
	private Label pedSuggestionLbl;
	private TextField processedQueryRemarks;

	private PEDQueryService pedService;

	private MasterService masterService;

	private PreauthService preauthService;


	private ViewPEDEndoresmentDetailsTable viewPEDEndoresmentDetailsTable;


	private ViewPEDEndoresementDetailsService viewPEDEndoresementDetailsService;

	private Long key;
	
	private FormLayout uwFormLayout;
	
	//private CheckBox pedDiscussed;
	private TextField pedDiscussedWithTxt;
	private TextArea pedDiscussedTxt;
	
	private TextField pedEffectiveFromDate;
	
	private UserProductMappingService userMappingService;

	public ViewPEDEndorsementDetails(
			PEDQueryService pedService,
			MasterService masterService,
			PreauthService preauthService,
			ViewPEDEndoresmentDetailsTable viewPEDEndoresmentDetailsTable,
			ViewPEDEndoresementDetailsService viewPEDEndoresementDetailsService,
			Long key) {
		this.masterService = masterService;
		this.pedService = pedService;
		this.preauthService = preauthService;
		this.viewPEDEndoresmentDetailsTable = viewPEDEndoresmentDetailsTable;
		this.viewPEDEndoresementDetailsService = viewPEDEndoresementDetailsService;
		this.key = key;
	}

	@SuppressWarnings("null")
	public void initView(Long key,String strStatus) {
		setCaption("View PED Endorsement Details");
		Panel mainPanel = new Panel(buildMainLayout(key,strStatus));
		setWidth("1000px");
		setHeight("100%");
		setModal(true);
		setClosable(true);
		setResizable(true);
		try {
			OldInitiatePedEndorsement initiate = pedService.findByKey(this.key);
			pedSuggestionTxt.setValue(initiate.getPedSuggestion().getValue());
			nameofPEDTxt.setValue(initiate.getPedName());
			pedRequsetInitialtionRemarksTxt.setValue(initiate.getRemarks());
			if (initiate.getRepudiationLetterDate() != null) {
				repudiationLetterDateTxt.setValue(SHAUtils.formatDate(initiate
						.getRepudiationLetterDate()));
			}
			if(initiate.getPedEffectiveFromDate() != null){
			pedEffectiveFromDate.setValue(SHAUtils.formatDate(initiate.getPedEffectiveFromDate()));
			}
			/*requestorRolText.setValue(null);// TODO need to implement in Data
											// base;
			if(initiate.getModifiedBy() != null){
				requestorIDorNameTxt.setValue(initiate.getModifiedBy()!=null ? initiate.getModifiedBy() : "");
			}else{
				requestorIDorNameTxt.setValue(initiate.getCreatedBy()!=null ? initiate.getCreatedBy() : "");
			}*/
			
			MasUserLimitMapping userRole = userMappingService.getRoleDetails(initiate.getCreatedBy());
			if(userRole!=null){
				MasRoleLimit emprole = userMappingService.getAmountDetails(userRole.getRoleId());
				if(emprole!=null){
					requestorRolText.setValue(emprole.getRoleType() != null ? emprole.getRoleType() : "");
					requestorRolText.setReadOnly(true);
				}
			}
			if(initiate.getModifiedBy() != null){
			
				TmpEmployee employeeObj = masterService.getEmployeeName(initiate.getModifiedBy());
				
				requestorIDorNameTxt.setValue(employeeObj.getLoginId()+"-"+employeeObj.getEmpFirstName());
			}else{
				TmpEmployee employeeObj = masterService.getEmployeeName(initiate.getCreatedBy());
				requestorIDorNameTxt.setValue(employeeObj.getLoginId()+"-"+employeeObj.getEmpFirstName());
			}

			
			if (initiate.getCreatedDate() != null) {
				requestedDateTxt.setValue(SHAUtils.formatDate(initiate.getCreatedDate()));
			}
//			reviewRemarksTxt.setValue(initiate
//					.getProcessorRemarks()!=null ? initiate.getProcessorRemarks() : "");
//			rejectionRemarksTxt.setValue(initiate
//					.getRejectionRemarks()!=null ? initiate
//							.getRejectionRemarks() : "");
//			approvalRemarksTxt.setValue(initiate
//					.getApprovalRemarks()!=null ? initiate
//							.getApprovalRemarks() : "");
			List<PedQuery> pedQueryDetailsList = pedService
					.getPedQueryDetailsList(this.key);

			if (pedQueryDetailsList != null || pedQueryDetailsList.size() != 0) {
				for (PedQuery pedQuery : pedQueryDetailsList) {
					
					if (pedQuery.getReplyRemarks() != null) {

						if (pedQuery.getStatus() != null) {
							queryRepliedDetailsFormLayout
									.addComponent(new TextField("PED Status",
											pedQuery.getStatus()
													.getProcessValue()));
						} else {
							queryRepliedDetailsFormLayout
									.addComponent(new TextField("PED Status"));
						}

						if (pedQuery.getReplyRemarks() != null) {
							queryRepliedDetailsFormLayout
									.addComponent(new TextArea(
											"Query Reply Remarks (Process)",
											pedQuery.getReplyRemarks()));
						} else {
							queryRepliedDetailsFormLayout
									.addComponent(new TextArea(
											"Query Reply Remarks (Process)"));
						}
					}
					else {
//						queryRepliedDetailsFormLayout.addComponent(new TextField(
//								"PED Status"));
//						queryRepliedDetailsFormLayout.addComponent(new TextArea(
//								"Query Reply Remarks (Process)"));
					}
				}

			} else {
				queryRepliedDetailsFormLayout.addComponent(new TextField(
						"PED Status"));
				queryRepliedDetailsFormLayout.addComponent(new TextArea(
						"Query Reply Remarks (Process)"));
			}

//			PedSpecialist specialistType = pedService
//					.getSpecialistDetailsByKey(initiate.getKey());
//
//			if (specialistType != null) {
//				if (null != specialistType.getInitiateType()) {
//					if (specialistType.getInitiatorType().equals("A")) {
//						specialisttypeSelect.setValue(specialistType
//								.getSpecialistType().getValue());
//						reasonforReferringTxt.setValue(specialistType
//								.getReferringReason());
//					} else {
//						specialistTypenativeSelect.setValue(specialistType
//								.getSpecialistType().getValue());
//						ReasoforReferringProcessorTxt.setValue(specialistType
//								.getReferringReason());
//					}
//				}
//				pedStatusSpecialistAdvisetxt.setValue(specialistType
//						.getStatus() != null ? specialistType.getStatus()
//						.getProcessValue() : "");
//
//				specialistRemarksTxt.setValue(specialistType
//						.getSpecialistRemarks() != null ? specialistType
//						.getSpecialistRemarks() : "");
//			}
//
//		
			
			if(initiate.getEscalateRemarks() != null && ! initiate.getEscalateRemarks().isEmpty()){
				
				
				TextArea escalateTxtA = new TextArea("Escalate Remarks",initiate.getEscalateRemarks());
				escalateTxtA.setWidth("100%");
				escalateTxtA.setRows(initiate.getEscalateRemarks().length()/100 >= 25 ? 25 : ((initiate.getEscalateRemarks().length()/100)%25)+1);
				escalateTxtA.setHeight("150%");
				escalatedDetailsFormLayout.setWidth("100%");
				escalatedDetailsFormLayout.addComponent(escalateTxtA);
			}
			
			List<PedSpecialist> specialistTypeList = pedService
					.getSpecialistDetailsListByKey(initiate.getKey());
			if (specialistTypeList != null || specialistTypeList.size() != 0) {
				for (PedSpecialist pedSpecialist : specialistTypeList) {
					
					if (pedSpecialist.getSpecialistRemarks() != null) {
						if (pedSpecialist.getStatus() != null) {
							specialistAdviseDetailsFormLayout
									.addComponent(new TextField("PED Status",
											pedSpecialist.getStatus()
													.getProcessValue()));
						} else {
							specialistAdviseDetailsFormLayout
									.addComponent(new TextField("PED Status"));
						}

						if (pedSpecialist.getSpecialistRemarks() != null) {
							specialistAdviseDetailsFormLayout
									.addComponent(new TextArea(
											"Specialist Remarks",
											pedSpecialist
													.getSpecialistRemarks()));
						} else {
							specialistAdviseDetailsFormLayout
									.addComponent(new TextArea(
											"Specialist Remarks"));
						}

					}
				}
			} else {
				specialistAdviseDetailsFormLayout.addComponent(new TextField(
						"PED Status"));
				specialistAdviseDetailsFormLayout.addComponent(new TextArea(
						"Specialist Remarks"));
			}
			
			//pedDiscussed.setValue(initiate.getUwTlFlag() != null ? (initiate.getUwTlFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) ? Boolean.TRUE : Boolean.FALSE) : Boolean.FALSE);
			pedDiscussedWithTxt.setValue(initiate.getUwDiscussWith() != null ? initiate.getUwDiscussWith().getValue() : "");
			pedDiscussedTxt.setValue(initiate.getUwSuggestion() != null ? initiate.getUwSuggestion() : "");
			
			
			if(initiate.getUwTlFlag() != null && initiate.getUwTlFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				uwFormLayout.setVisible(true);
			}else{
				uwFormLayout.setVisible(false);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		setReadOnly(specialistAdviseDetailsFormLayout, true);
		setReadOnly(escalatedDetailsFormLayout, true);
		setReadOnly(queryRepliedDetailsFormLayout, true);
//		setReadOnly(RequestApprovalDetailsFormLayout, true);
//		setReadOnly(pedRequestProcessedDetailsFormLayout, true);
		setReadOnly(requestorDetailsFormLayout, true);
		setReadOnly(requestInitiationFormLayout, true);
		setReadOnly(uwFormLayout, true);
		//pedDiscussed.setReadOnly(true);
		setContent(mainPanel);
	}
	
	
	

	private VerticalLayout buildMainLayout(Long key,String strStatus) {
		mainLayout = new VerticalLayout();
		mainLayout.setCaption("Query Remarks(Approver)");
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(false);
		setWidth("800");
		setHeight("700");
		pedRequestInitiationDetailspanel = buildPedRequestInitiationDetailsPanel(key,strStatus);
		mainLayout.addComponent(pedRequestInitiationDetailspanel);
		
		
		List<PedEndorementHistory> pedEndorsementHistory = pedService.getPedEndorsementHistory(key);
		
		if(pedEndorsementHistory != null){
			for (PedEndorementHistory pedEndorementHistory : pedEndorsementHistory) {
				if(pedEndorementHistory.getProcessType() != null && pedEndorementHistory.getProcessType().equalsIgnoreCase("I")
						&& pedEndorementHistory.getStatus() != null && ! pedEndorementHistory.getStatus().getKey().equals(ReferenceTable.PED_WATCHLIST_RELEASE_PROCESSOR)
						&& ! pedEndorementHistory.getStatus().getKey().equals(ReferenceTable.PED_WATCHLIST_RELEASE_APPROVER)){
					Panel buildPedRequestProcessHistoryPanelDetails = buildPedRequestInitiaterHistoryPanelDetails(pedEndorementHistory,strStatus);
					mainLayout.addComponent(buildPedRequestProcessHistoryPanelDetails);
				}
			}
		}
		
		if(pedEndorsementHistory != null){
			for (PedEndorementHistory pedEndorementHistory : pedEndorsementHistory) {
				if(pedEndorementHistory.getProcessType() != null && pedEndorementHistory.getProcessType().equalsIgnoreCase("I")
						&& pedEndorementHistory.getStatus() != null && pedEndorementHistory.getStatus().getKey().equals(ReferenceTable.PED_WATCHLIST_RELEASE_PROCESSOR)
						&& pedEndorementHistory.getStatus().getKey().equals(ReferenceTable.PED_WATCHLIST_RELEASE_APPROVER)){
					Panel buildPedRequestProcessHistoryPanelDetails = buildPedRequestInitiatorReviewerHistoryPanelDetails(pedEndorementHistory,strStatus);
					mainLayout.addComponent(buildPedRequestProcessHistoryPanelDetails);
				}
			}
		}
		
		if(pedEndorsementHistory  != null){
			for (PedEndorementHistory pedEndorementHistory : pedEndorsementHistory) {
				if(pedEndorementHistory.getProcessType() != null && pedEndorementHistory.getProcessType().equalsIgnoreCase("P")){
					Panel buildPedRequestProcessHistoryPanelDetails = buildPedRequestProcessHistoryPanelDetails(pedEndorementHistory,strStatus);
					mainLayout.addComponent(buildPedRequestProcessHistoryPanelDetails);
				}
				else if(pedEndorementHistory.getProcessType() != null && pedEndorementHistory.getProcessType().equalsIgnoreCase("A")){
					Panel buildPedRequestProcessHistoryPanelDetails = buildPedRequestApproveHistoryPanelDetails(pedEndorementHistory,strStatus);
					mainLayout.addComponent(buildPedRequestProcessHistoryPanelDetails);
				}
			}
		}
		
//		pedRequestProcessedDetailsPanel = buildPedRequestProcessedDetailsPanel(key);
//		mainLayout.addComponent(pedRequestProcessedDetailsPanel);
//		pedRequestApprovalDetailsPanel = buildPedRequestApprovalDetailsPanel(key);
//		mainLayout.addComponent(pedRequestApprovalDetailsPanel);
		
		requestProcessedQueryRepliedDetailsPanel = buildRequestProcessedQueryRepliedDetailsPanel();
		mainLayout.addComponent(requestProcessedQueryRepliedDetailsPanel);
		specialistAdviseDetailsPanel = buildSpecialistAdviseDetailsPanel();
		mainLayout.addComponent(specialistAdviseDetailsPanel);
		
		escalatedDetailsPanel = buildEscalatedDetailsPanel();
		mainLayout.addComponent(escalatedDetailsPanel);
		
		return mainLayout;
	}

	private Panel buildPedRequestInitiationDetailsPanel(Long key,String status) {
		pedRequestInitiationDetailspanel = new Panel();
		pedRequestInitiationDetailspanel
				.setCaption("PED Request Initiation Details");
		//Vaadin8-setImmediate() pedRequestInitiationDetailspanel.setImmediate(false);
		pedRequestInitiationDetailspanel.setWidth("100.0%");
		pedRequestInitiationDetailspanel.setHeight("-1px");
		pedRequestInitiationDetailspanel.addStyleName("panelStyle");
		
		pedRequestInitiationDetailsverticalLayout = buildPedRequestInitiationDetailsverticalLayout(key,status);
		pedRequestInitiationDetailspanel
				.setContent(pedRequestInitiationDetailsverticalLayout);
		return pedRequestInitiationDetailspanel;
	}
	
	private Panel buildPedRequestProcess(Long key,String strStatus) {
		
		Panel pedRequestInitiationDetailspanel = new Panel();
		pedRequestInitiationDetailspanel
				.setCaption("PED Request Processed Details");
		//Vaadin8-setImmediate() pedRequestInitiationDetailspanel.setImmediate(false);
		pedRequestInitiationDetailspanel.setWidth("100.0%");
		pedRequestInitiationDetailspanel.setHeight("-1px");
		pedRequestInitiationDetailspanel.addStyleName("panelStyle");
		
		VerticalLayout pedRequestInitiationDetailsverticalLayout = buildPedRequestInitiationDetailsverticalLayout(key,strStatus);
		pedRequestInitiationDetailspanel
				.setContent(pedRequestInitiationDetailsverticalLayout);
		return pedRequestInitiationDetailspanel;
	}

	private VerticalLayout buildPedRequestInitiationDetailsverticalLayout(
			Long key,String strStatus) {
		pedRequestInitiationDetailsverticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() pedRequestInitiationDetailsverticalLayout.setImmediate(false);
		pedRequestInitiationDetailsverticalLayout.setWidth("100.0%");
		pedRequestInitiationDetailsverticalLayout.setHeight("100.0%");
		pedRequestInitiationDetailsverticalLayout.setStyleName("background");
		requestInitiationFormLayout = buildRequestInitiationFormLayout();
		uwFormLayout = buildUnderWritingFormLayout();
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setWidth("100.0%");
		hlayout.setHeight("100.0%");
		hlayout.addComponents(requestInitiationFormLayout, uwFormLayout);
		pedRequestInitiationDetailsverticalLayout
				.addComponent(hlayout);
		/*pedRequestInitiationDetailsverticalLayout.setExpandRatio(
				hlayout, 25.0f);*/
		pedTableverticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() pedTableverticalLayout.setImmediate(false);
		pedTableverticalLayout.setWidth("-1px");
		pedTableverticalLayout.setHeight("-1px");
		pedTableverticalLayout.setMargin(true);
		pedTableverticalLayout.setSpacing(true);

		try {
			viewPEDEndoresmentDetailsTable.init("", false ,false);
			viewPEDEndoresmentDetailsTable.setWidth("50%");
			viewPEDEndoresmentDetailsTable.setHeight("25%");
		} catch (Exception e) {
			e.printStackTrace();
		}
		viewPEDEndoresmentDetailsTable
				.setTableList(viewPEDEndoresementDetailsService.search(key));

		/*
		 * this.iContainer = createContainer(); Table createViewTable =
		 * createTable(iContainer, "");
		 */
		pedTableverticalLayout.addComponent(viewPEDEndoresmentDetailsTable);
		pedRequestInitiationDetailsverticalLayout
				.addComponent(pedTableverticalLayout);
		pedRequestdetailsPanel = buildPedRequestDetailsPanel();
		pedRequestInitiationDetailsverticalLayout
				.addComponent(pedRequestdetailsPanel);
		return pedRequestInitiationDetailsverticalLayout;
	}
	
	public Panel buildPedRequestProcessHistoryPanelDetails(PedEndorementHistory pedEndorsementHistory,String strStatus){
		
		Panel pedRequestInitiationDetailspanel = new Panel();
		pedRequestInitiationDetailspanel
				.setCaption("PED Request Processed Details");
		//Vaadin8-setImmediate() pedRequestInitiationDetailspanel.setImmediate(false);
		pedRequestInitiationDetailspanel.setWidth("100.0%");
		pedRequestInitiationDetailspanel.setHeight("-1px");
		pedRequestInitiationDetailspanel.addStyleName("panelStyle");
		
		VerticalLayout pedRequestInitiationDetailsverticalLayout = buildPEDRequestProcessHistoryDetails(pedEndorsementHistory,strStatus);
		pedRequestInitiationDetailspanel
				.setContent(pedRequestInitiationDetailsverticalLayout);
		return pedRequestInitiationDetailspanel;
	}
	
public Panel buildPedRequestApproveHistoryPanelDetails(PedEndorementHistory pedEndorsementHistory,String status){
		
		Panel pedRequestInitiationDetailspanel = new Panel();
		pedRequestInitiationDetailspanel
				.setCaption("PED Request Approver Details");
		//Vaadin8-setImmediate() pedRequestInitiationDetailspanel.setImmediate(false);
		pedRequestInitiationDetailspanel.setWidth("100.0%");
		pedRequestInitiationDetailspanel.setHeight("-1px");
		pedRequestInitiationDetailspanel.addStyleName("panelStyle");
		
		VerticalLayout pedRequestInitiationDetailsverticalLayout = buildPEDRequestApproveHistoryDetails(pedEndorsementHistory,status);
		pedRequestInitiationDetailspanel
				.setContent(pedRequestInitiationDetailsverticalLayout);
		return pedRequestInitiationDetailspanel;
	}

public Panel buildPedRequestInitiatorReviewerHistoryPanelDetails(PedEndorementHistory pedEndorsementHistory,String strStatus){
	
	Panel pedRequestInitiationDetailspanel = new Panel();
	pedRequestInitiationDetailspanel
			.setCaption("PED Request Reviewed Details");
	//Vaadin8-setImmediate() pedRequestInitiationDetailspanel.setImmediate(false);
	pedRequestInitiationDetailspanel.setWidth("100.0%");
	pedRequestInitiationDetailspanel.setHeight("-1px");
	pedRequestInitiationDetailspanel.addStyleName("panelStyle");
	
	VerticalLayout pedRequestInitiationDetailsverticalLayout = buildPEDRequestReviewerHistoryDetails(pedEndorsementHistory,strStatus);
	pedRequestInitiationDetailspanel
			.setContent(pedRequestInitiationDetailsverticalLayout);
	return pedRequestInitiationDetailspanel;
}

public Panel buildPedRequestInitiaterHistoryPanelDetails(PedEndorementHistory pedEndorsementHistory,String strStatus){
	
	Panel pedRequestInitiationDetailspanel = new Panel();
	pedRequestInitiationDetailspanel
			.setCaption("PED Request Ammended Details");
	//Vaadin8-setImmediate() pedRequestInitiationDetailspanel.setImmediate(false);
	pedRequestInitiationDetailspanel.setWidth("100.0%");
	pedRequestInitiationDetailspanel.setHeight("-1px");
	pedRequestInitiationDetailspanel.addStyleName("panelStyle");
	
	VerticalLayout pedRequestInitiationDetailsverticalLayout = buildPEDRequestInitiatorHistoryDetails(pedEndorsementHistory,strStatus);
	pedRequestInitiationDetailspanel
			.setContent(pedRequestInitiationDetailsverticalLayout);
	return pedRequestInitiationDetailspanel;
}
	
	
	public VerticalLayout buildPEDRequestProcessHistoryDetails(PedEndorementHistory pedEndorsementHistory,String strStatus){

		TextField pedSuggestion = new TextField("PED Suggestion");
		TextField nameOfPEd = new TextField("Name Of PED");
		TextField txtRemarks = new TextField("Remarks");
		TextField txtRepudiationLetter = new TextField("Repudiation Letter Date");
		
		pedSuggestion.setEnabled(false);
		nameOfPEd.setEnabled(false);
		txtRemarks.setEnabled(false);
		txtRepudiationLetter.setEnabled(false);
		
		FormLayout firstForm = new FormLayout(pedSuggestion,nameOfPEd,txtRemarks,txtRepudiationLetter);

		if(pedEndorsementHistory != null && pedEndorsementHistory.getPedSuggestion() != null){
			
			pedSuggestion.setValue(pedEndorsementHistory.getPedSuggestion().getValue());
		}
		
		nameOfPEd.setValue(pedEndorsementHistory.getPedName());
		txtRemarks.setValue(pedEndorsementHistory.getRemarks());
		if(pedEndorsementHistory.getRepudiationLetterDate() != null){
			String formatDate = SHAUtils.formatDate(pedEndorsementHistory.getRepudiationLetterDate());
			txtRepudiationLetter.setValue(formatDate);
		}
				
		List<PedEndorsementDetailsHistory> pedEndorsementDetailsHistory = viewPEDEndoresementDetailsService.getPedEndorsementDetailsHistory(pedEndorsementHistory.getKey());
		
		Panel pedDiagnosisTable = getPedDiagnosisTable(pedEndorsementDetailsHistory);
		
		TextField pedStatus = new TextField("PED Status");
//		pedStatus.setValue(pedEndorsementHistory.getStatus().getProcessValue());
		pedStatus.setValue(strStatus);
		
		 PedQuery pedQuery=pedService.getPedQueryforViewDetails(key);
		    
			TextField queryRemarks = new TextField("Query Remarks(Processor)");
			
			if(pedQuery != null){
				queryRemarks.setValue(pedQuery.getQueryRemarks());
			}
			
		PedSpecialist pedSpecialist=pedService.getSpecialistforViewDetails(key);
		
		TextField specialistType = new TextField("Specialist Type");
		
		if(pedSpecialist != null && pedSpecialist.getSpecialistType() != null){
			specialistType.setValue(pedSpecialist.getSpecialistType().getValue());
		}
		
		TextField txtReasonReferring = new TextField("Reason For Referring(Processor)");
		if(pedSpecialist != null){
			txtReasonReferring.setValue(pedSpecialist.getReferringReason());
		}
		
		txtReasonReferring.setNullRepresentation("");
		TextField txtReviewRemarks = new TextField("Review Remarks");
		
		txtReviewRemarks.setValue(pedEndorsementHistory.getApprovalRemarks());
		txtReviewRemarks.setNullRepresentation("");
		
		pedStatus.setEnabled(false);
		queryRemarks.setEnabled(false);
		specialistType.setEnabled(false);
		txtReasonReferring.setEnabled(false);
		txtReviewRemarks.setEnabled(false);
		
		FormLayout secondForm = new FormLayout(pedStatus,queryRemarks,specialistType,txtReasonReferring,txtReviewRemarks);
		
		
		VerticalLayout mainVertical = new VerticalLayout(firstForm,pedDiagnosisTable,secondForm);

		return mainVertical;
	}
	
	public VerticalLayout buildPEDRequestApproveHistoryDetails(PedEndorementHistory pedEndorsementHistory,String strStatus){

		TextField pedSuggestion = new TextField("PED Suggestion");
		TextField nameOfPEd = new TextField("Name Of PED");
		TextField txtRemarks = new TextField("Remarks");
		TextField txtRepudiationLetter = new TextField("Repudiation Letter Date");
		
		FormLayout firstForm = new FormLayout(pedSuggestion,nameOfPEd,txtRemarks,txtRepudiationLetter);
		
		pedSuggestion.setEnabled(false);
		nameOfPEd.setEnabled(false);
		txtRemarks.setEnabled(false);
		txtRepudiationLetter.setEnabled(false);
		
		if(pedEndorsementHistory != null && pedEndorsementHistory.getPedSuggestion() != null){
			
			pedSuggestion.setValue(pedEndorsementHistory.getPedSuggestion().getValue());
		}
		
		nameOfPEd.setValue(pedEndorsementHistory.getPedName());
		txtRemarks.setValue(pedEndorsementHistory.getRemarks());
		if(pedEndorsementHistory.getRepudiationLetterDate() != null){
			String formatDate = SHAUtils.formatDate(pedEndorsementHistory.getRepudiationLetterDate());
			txtRepudiationLetter.setValue(formatDate);
		}
		
		List<PedEndorsementDetailsHistory> pedEndorsementDetailsHistory = viewPEDEndoresementDetailsService.getPedEndorsementDetailsHistory(pedEndorsementHistory.getKey());
		
		Panel pedDiagnosisTable = getPedDiagnosisTable(pedEndorsementDetailsHistory);
		
		TextField pedStatus = new TextField("PED Status");
//	    pedStatus.setValue(pedEndorsementHistory.getStatus().getProcessValue());
		pedStatus.setValue(strStatus);
	    pedStatus.setEnabled(false);
	    
	    PedQuery pedQuery=pedService.getPedQueryforApprovePed(key);
	  
	    
		TextField queryRemarks = new TextField("Query Remarks(Approver)");
		queryRemarks.setEnabled(false);
		
		if(pedQuery != null){
			queryRemarks.setValue(pedQuery.getQueryRemarks());
		}

		PedSpecialist pedSpecialist=pedService.getSpecialistforApprover(key);
		
		TextField specialistType = new TextField("Specialist Type");
		specialistType.setEnabled(false);
		
		if(pedSpecialist != null && pedSpecialist.getSpecialistType() != null){
			specialistType.setValue(pedSpecialist.getSpecialistType().getValue());
		}
		
		TextField txtReasonReferring = new TextField("Reason For Referring(Approver)");
		txtReasonReferring.setEnabled(false);
		
		if(pedSpecialist != null){
			txtReasonReferring.setValue(pedSpecialist.getReferringReason());
		}
		
		TextField txtRejectionRemarks = new TextField("Rejection Remarks");
		txtRejectionRemarks.setEnabled(false);
		txtRejectionRemarks.setValue(pedEndorsementHistory.getRejectionRemarks());
		txtRejectionRemarks.setNullRepresentation("");
		TextField txtApproverRemarks = new TextField("Approval Remarks");
		txtApproverRemarks.setValue(pedEndorsementHistory.getApprovalRemarks());
		txtApproverRemarks.setEnabled(false);
		txtApproverRemarks.setNullRepresentation("");
		
		FormLayout secondForm = new FormLayout(pedStatus,queryRemarks,specialistType,txtReasonReferring,txtRejectionRemarks,txtApproverRemarks);
		
		VerticalLayout mainVertical = new VerticalLayout(firstForm,pedDiagnosisTable,secondForm);

		return mainVertical;
	}
	
	public VerticalLayout buildPEDRequestReviewerHistoryDetails(PedEndorementHistory pedEndorsementHistory,String strStatus){

		TextField pedSuggestion = new TextField("PED Suggestion");
		TextField nameOfPEd = new TextField("Name Of PED");
		TextField txtRemarks = new TextField("Remarks");
		TextField txtRepudiationLetter = new TextField("Repudiation Letter Date");
		
		FormLayout firstForm = new FormLayout(pedSuggestion,nameOfPEd,txtRemarks,txtRepudiationLetter);
		
		pedSuggestion.setEnabled(false);
		nameOfPEd.setEnabled(false);
		txtRemarks.setEnabled(false);
		txtRepudiationLetter.setEnabled(false);
		
		if(pedEndorsementHistory != null && pedEndorsementHistory.getPedSuggestion() != null){
			
			pedSuggestion.setValue(pedEndorsementHistory.getPedSuggestion().getValue());
		}
		
		nameOfPEd.setValue(pedEndorsementHistory.getPedName());
		txtRemarks.setValue(pedEndorsementHistory.getRemarks());
		if(pedEndorsementHistory.getRepudiationLetterDate() != null){
			String formatDate = SHAUtils.formatDate(pedEndorsementHistory.getRepudiationLetterDate());
			txtRepudiationLetter.setValue(formatDate);
		}
		
		List<PedEndorsementDetailsHistory> pedEndorsementDetailsHistory = viewPEDEndoresementDetailsService.getPedEndorsementDetailsHistory(pedEndorsementHistory.getKey());
		
		Panel pedDiagnosisTable = getPedDiagnosisTable(pedEndorsementDetailsHistory);
		
        TextArea txtReviewRemarks = new TextArea();
        txtReviewRemarks.setValue(pedEndorsementHistory.getReviewRemarks());
        txtReviewRemarks.setEnabled(false);

		FormLayout secondForm = new FormLayout(txtReviewRemarks);
		
		VerticalLayout mainVertical = new VerticalLayout(firstForm,pedDiagnosisTable,secondForm);

		return mainVertical;
	}
	
	public VerticalLayout buildPEDRequestInitiatorHistoryDetails(PedEndorementHistory pedEndorsementHistory,String strStatus){

		TextField pedSuggestion = new TextField("PED Suggestion");
		TextField nameOfPEd = new TextField("Name Of PED");
		TextField txtRemarks = new TextField("Remarks");
		TextField txtRepudiationLetter = new TextField("Repudiation Letter Date");
		
		FormLayout firstForm = new FormLayout(pedSuggestion,nameOfPEd,txtRemarks,txtRepudiationLetter);
		
		pedSuggestion.setEnabled(false);
		nameOfPEd.setEnabled(false);
		txtRemarks.setEnabled(false);
		txtRepudiationLetter.setEnabled(false);
		
		if(pedEndorsementHistory != null && pedEndorsementHistory.getPedSuggestion() != null){
			
			pedSuggestion.setValue(pedEndorsementHistory.getPedSuggestion().getValue());
		}
		
		nameOfPEd.setValue(pedEndorsementHistory.getPedName());
		txtRemarks.setValue(pedEndorsementHistory.getRemarks());
		if(pedEndorsementHistory.getRepudiationLetterDate() != null){
			String formatDate = SHAUtils.formatDate(pedEndorsementHistory.getRepudiationLetterDate());
			txtRepudiationLetter.setValue(formatDate);
		}
		
//		ViewPEDEndoresmentDetailsTable viewPEDEndoresmentDetailsTableObj = new ViewPEDEndoresmentDetailsTable();
//		viewPEDEndoresmentDetailsTableObj.init("", false,false);
		List<PedEndorsementDetailsHistory> pedEndorsementDetailsHistory = viewPEDEndoresementDetailsService.getPedEndorsementDetailsHistory(pedEndorsementHistory.getKey());
		
		Panel pedDiagnosisTable = getPedDiagnosisTable(pedEndorsementDetailsHistory);
		
		TextField txtRequestorRole = new TextField("Requestor Role");
		TextField txtRequestorName = new TextField("Requestor ID / Name");
		TextField txtRequestDate = new TextField("Requested Date");
		
		if(pedEndorsementHistory.getModifiedBy() != null){
			txtRequestorName.setValue(pedEndorsementHistory.getModifiedBy());
		}else{
			txtRequestorName.setValue(pedEndorsementHistory.getCreatedBy());
		}
		
		
		String formatDate = SHAUtils.formatDate(pedEndorsementHistory.getCreatedDate());
		txtRequestDate.setValue(formatDate);
		
		txtRequestorRole.setEnabled(false);
		txtRequestorName.setEnabled(false);
		txtRequestDate.setEnabled(false);
		
		FormLayout secondForm = new FormLayout(txtRequestorRole,txtRequestorName,txtRequestDate);
		
		
		VerticalLayout mainVertical = new VerticalLayout(firstForm,pedDiagnosisTable,secondForm);
	
		return mainVertical;
	}
	
	private Panel getPedDiagnosisTable(List<PedEndorsementDetailsHistory> diagnosisDetails){
		
		List<ViewPEDEndoresementDetailsDTO> resultList = new ArrayList<ViewPEDEndoresementDetailsDTO>();
		
		for (PedEndorsementDetailsHistory pedEndorsementDetailsHistory : diagnosisDetails) {
			ViewPEDEndoresementDetailsDTO dto = new ViewPEDEndoresementDetailsDTO();
			dto.setOthersSpecify(pedEndorsementDetailsHistory.getOthesSpecify());
			dto.setDoctorRemarks(pedEndorsementDetailsHistory.getDoctorRemarks());
			dto.setPedCode(pedEndorsementDetailsHistory.getDescription());
			
			PreExistingDisease pedCode = masterService.getValueByKey(pedEndorsementDetailsHistory.getPedCode());
			if(pedCode != null){
				dto.setDescription(pedCode.getValue());
			}
			
			SelectValue icdBlock = masterService.getIcdBlock(pedEndorsementDetailsHistory.getIcdBlockId());
			SelectValue icdChapterbyId = masterService.getIcdChapterbyId(pedEndorsementDetailsHistory.getIcdChapterId());
			SelectValue icdCodeByKey = masterService.getIcdCodeByKey(pedEndorsementDetailsHistory.getIcdCodeId());
			if(icdBlock != null){
				dto.setIcdBlock(icdBlock.getValue());
			}
			if(icdChapterbyId != null){
				dto.setIcdChapter(icdChapterbyId.getValue());
			}
			if(icdCodeByKey != null){
				dto.setIcdCode(icdCodeByKey.getValue());
			}
			
			if(pedEndorsementDetailsHistory.getSource() != null){
				dto.setSource(pedEndorsementDetailsHistory.getSource().getValue());
			}
			
			resultList.add(dto);

		}

		Table table = new Table();
		table.setWidth("100%");
		table.addContainerProperty("description", String.class, null);
		table.addContainerProperty("pedCode",  String.class, null);
		table.addContainerProperty("icdBlock",  String.class, null);
		table.addContainerProperty("icdChapter",  String.class, null);
		table.addContainerProperty("icdCode",  String.class, null);
		table.addContainerProperty("source",  String.class, null);
		table.addContainerProperty("othersSpecify",  String.class, null);
		table.addContainerProperty("doctorRemarks",  String.class, null);
//		table.setCaption("PED Details");
		
		int i=0;
//		table.setStyleName(ValoTheme.TABLE_NO_HEADER);
		if(resultList != null && !resultList.isEmpty()) {
			for (ViewPEDEndoresementDetailsDTO pedDetails : resultList) {
				
				table.addItem(new Object[]{pedDetails.getDescription(), pedDetails.getPedCode(),pedDetails.getIcdChapter(),
						pedDetails.getIcdBlock(),pedDetails.getIcdCode(),pedDetails.getSource(),pedDetails.getOthersSpecify(),pedDetails.getDoctorRemarks()}, i+1);
				i++;

			}
		}
		
		
		table.setPageLength(3);
		table.setColumnHeader("description", "Description");
		table.setColumnHeader("pedCode",  "Ped Code");
		table.setColumnHeader("icdBlock", "Icd Block");
		table.setColumnHeader("icdChapter",  "Icd Chapter");
		table.setColumnHeader("icdCode",  "Icd Code");
		table.setColumnHeader("source",  "Source");
		table.setColumnHeader("othersSpecify",  "Others Specify");
		table.setColumnHeader("doctorRemarks",  "Doctors Remarks");
		
		
		Panel tablePanel = new Panel(table);
		return tablePanel;
	}

	private FormLayout buildRequestInitiationFormLayout() {
		requestInitiationFormLayout = new FormLayout();
		//Vaadin8-setImmediate() requestInitiationFormLayout.setImmediate(false);
		//requestInitiationFormLayout.setWidth("100.0%");
		//requestInitiationFormLayout.setHeight("100.0%");
		requestInitiationFormLayout.setMargin(true);
		requestInitiationFormLayout.setSpacing(true);
//		requestInitiationFormLayout.addStyleName("layoutDesign");

		pedSuggestionTxt = new TextArea("PED Suggestion");
		//Vaadin8-setImmediate() pedSuggestionTxt.setImmediate(false);
		pedSuggestionTxt.setWidth("200px");
		pedSuggestionTxt.setHeight("-1px");
		requestInitiationFormLayout.addComponent(pedSuggestionTxt);

		nameofPEDTxt = new TextArea("Name of PED");
		//Vaadin8-setImmediate() nameofPEDTxt.setImmediate(false);
		nameofPEDTxt.setWidth("100px");
		nameofPEDTxt.setHeight("-1px");
		nameofPEDTxt.setNullRepresentation("");
		requestInitiationFormLayout.addComponent(nameofPEDTxt);

		pedRequsetInitialtionRemarksTxt = new TextArea("Remarks");
		//Vaadin8-setImmediate() pedRequsetInitialtionRemarksTxt.setImmediate(false);
		pedRequsetInitialtionRemarksTxt.setWidth("500px");
		pedRequsetInitialtionRemarksTxt.setHeight("-1px");
		requestInitiationFormLayout
				.addComponent(pedRequsetInitialtionRemarksTxt);

		repudiationLetterDateTxt = new TextField("Repudiation Letter Date");
		//Vaadin8-setImmediate() repudiationLetterDateTxt.setImmediate(false);
		repudiationLetterDateTxt.setWidth("-1px");
		repudiationLetterDateTxt.setHeight("-1px");
		requestInitiationFormLayout.addComponent(repudiationLetterDateTxt);
		//requestInitiationFormLayout.setWidth("100%");
		
		pedEffectiveFromDate = new TextField("PED Effective from date");
		pedEffectiveFromDate.setWidth("-1px");
		pedEffectiveFromDate.setHeight("-1px");
		requestInitiationFormLayout.addComponent(pedEffectiveFromDate);
		
		return requestInitiationFormLayout;
	}
	
	private Panel buildPedRequestDetailsPanel() {
		pedRequestdetailsPanel = new Panel();
		pedRequestdetailsPanel
				.setCaption("Requestor Details");
		//Vaadin8-setImmediate() pedRequestdetailsPanel.setImmediate(false);
		pedRequestdetailsPanel.setWidth("100.0%");
		pedRequestdetailsPanel.setHeight("-1px");
		pedRequestdetailsPanel.addStyleName("panelStyle");
		requestorDetailsFormLayout = buildRequestorDetailsFormLayout();
		pedRequestdetailsPanel
				.setContent(requestorDetailsFormLayout);

		return pedRequestdetailsPanel;
	}

	private FormLayout buildRequestorDetailsFormLayout() {
		requestorDetailsFormLayout = new FormLayout();
		requestorDetailsFormLayout.setCaption("Requestor Details");
		//Vaadin8-setImmediate() requestorDetailsFormLayout.setImmediate(false);
		requestorDetailsFormLayout.setWidth("100.0%");
		requestorDetailsFormLayout.setHeight("100.0%");
		requestorDetailsFormLayout.setMargin(true);
		requestorDetailsFormLayout.setSpacing(true);
//		requestorDetailsFormLayout.addStyleName("layoutDesign");
		requestorDetailsFormLayout.setStyleName("background");
		requestorRolText = new TextField("Requestor Role");
		//Vaadin8-setImmediate() requestorRolText.setImmediate(false);
		requestorRolText.setWidth("-1px");
		requestorRolText.setHeight("-1px");
		requestorRolText.setNullRepresentation("");
		requestorDetailsFormLayout.addComponent(requestorRolText);

		requestorIDorNameTxt = new TextField("Requestor ID / Name");
		//Vaadin8-setImmediate() requestorIDorNameTxt.setImmediate(false);
		requestorIDorNameTxt.setWidth("-1px");
		requestorIDorNameTxt.setHeight("-1px");
		requestorIDorNameTxt.setNullRepresentation("");
		requestorDetailsFormLayout.addComponent(requestorIDorNameTxt);

		requestedDateTxt = new TextField("Requested Date");
		//Vaadin8-setImmediate() requestedDateTxt.setImmediate(false);
		requestedDateTxt.setWidth("-1px");
		requestedDateTxt.setHeight("-1px");
		requestorDetailsFormLayout.addComponent(requestedDateTxt);

		return requestorDetailsFormLayout;
	}

	private Panel buildPedRequestProcessedDetailsPanel(Long key) {
		pedRequestProcessedDetailsPanel = new Panel();
		pedRequestProcessedDetailsPanel
				.setCaption("PED Request Processed Details");
		//Vaadin8-setImmediate() pedRequestProcessedDetailsPanel.setImmediate(false);
		pedRequestProcessedDetailsPanel.setWidth("100.0%");
		pedRequestProcessedDetailsPanel.setHeight("-1px");
		pedRequestProcessedDetailsPanel.addStyleName("panelStyle");
		pedRequestProcessedDetailsFormLayout = buildPedRequestProcessedDetailsFormLayout(key);
		pedRequestProcessedDetailsPanel
				.setContent(pedRequestProcessedDetailsFormLayout);

		return pedRequestProcessedDetailsPanel;
	}

	private FormLayout buildPedRequestProcessedDetailsFormLayout(Long key) {
		
		OldInitiatePedEndorsement initiate = pedService.findByKey(key);
		
		pedRequestProcessedDetailsFormLayout = new FormLayout();
		//Vaadin8-setImmediate() pedRequestProcessedDetailsFormLayout.setImmediate(false);
		pedRequestProcessedDetailsFormLayout.setWidth("100.0%");
		pedRequestProcessedDetailsFormLayout.setHeight("100.0%");
		pedRequestProcessedDetailsFormLayout.setMargin(true);
		pedRequestProcessedDetailsFormLayout.setSpacing(true);
//		pedRequestProcessedDetailsFormLayout.addStyleName("layoutDesign");
		pedRequestProcessedDetailsFormLayout.setStyleName("background");
		pedStatusProcessedTxt = new TextField("PED Status");
		//Vaadin8-setImmediate() pedStatusProcessedTxt.setImmediate(false);
		pedStatusProcessedTxt.setWidth("-1px");
		pedStatusProcessedTxt.setHeight("-1px");
		pedStatusProcessedTxt.setValue(initiate.getStatus().getProcessValue());
		pedRequestProcessedDetailsFormLayout
				.addComponent(pedStatusProcessedTxt);
		PedQuery processedPedQuery= pedService.getPedQueryforViewDetails(key);
		
		processedQueryRemarks=new TextField("Query Remarks(Processor)");
		//Vaadin8-setImmediate() processedQueryRemarks.setImmediate(false);
		processedQueryRemarks.setWidth("-1px");
		processedQueryRemarks.setHeight("-1px");
		if(processedPedQuery!=null){
			if(processedPedQuery.getQueryRemarks()!=null && null != processedPedQuery.getInitiatorType()
					&& ("P").equalsIgnoreCase(processedPedQuery.getInitiatorType())){
				processedQueryRemarks.setValue(processedPedQuery.getQueryRemarks());
			}
		}
		//pedStatusProcessedTxt.setValue(initiate.getStatus().getProcessValue());
		pedRequestProcessedDetailsFormLayout
				.addComponent(processedQueryRemarks);
		
        PedSpecialist pedSpecialist=pedService.getSpecialistforViewDetails(key);
        
		specialistTypenativeSelect = new TextField("Specialist Type");
		//Vaadin8-setImmediate() specialistTypenativeSelect.setImmediate(false);
		specialistTypenativeSelect.setWidth("-1px");
		specialistTypenativeSelect.setHeight("-1px");
		
		ReasoforReferringProcessorTxt = new TextField(
				"Reason for Referring (Processor)");
		//Vaadin8-setImmediate() ReasoforReferringProcessorTxt.setImmediate(false);
		ReasoforReferringProcessorTxt.setWidth("-1px");
		ReasoforReferringProcessorTxt.setHeight("-1px");
		pedRequestProcessedDetailsFormLayout
				.addComponent(ReasoforReferringProcessorTxt);
		
		if(pedSpecialist != null){
			if(pedSpecialist.getSpecialistType()!=null && pedSpecialist.getReferringReason() != null){
				specialistTypenativeSelect.setValue(pedSpecialist.getSpecialistType().getValue());
				ReasoforReferringProcessorTxt.setValue(pedSpecialist.getReferringReason());
			}
		}
		pedRequestProcessedDetailsFormLayout
				.addComponent(specialistTypenativeSelect);


		reviewRemarksTxt = new TextField("Review Remarks");
		//Vaadin8-setImmediate() reviewRemarksTxt.setImmediate(false);
		reviewRemarksTxt.setWidth("-1px");
		reviewRemarksTxt.setHeight("-1px");
		
		if(initiate != null){
			reviewRemarksTxt.setValue(initiate.getProcessorRemarks());
		}
		
		pedRequestProcessedDetailsFormLayout.addComponent(reviewRemarksTxt);

		return pedRequestProcessedDetailsFormLayout;
	}

	private Panel buildPedRequestApprovalDetailsPanel(Long key) {
		pedRequestApprovalDetailsPanel = new Panel();
		pedRequestApprovalDetailsPanel
				.setCaption("PED Request Approval Details");
		//Vaadin8-setImmediate() pedRequestApprovalDetailsPanel.setImmediate(false);
		pedRequestApprovalDetailsPanel.setWidth("100.0%");
		pedRequestApprovalDetailsPanel.addStyleName("panelStyle");
		RequestApprovalDetailsFormLayout = buildRequestApprovalDetailsFormLayout(key);
		pedRequestApprovalDetailsPanel
				.setContent(RequestApprovalDetailsFormLayout);

		return pedRequestApprovalDetailsPanel;
	}

	private FormLayout buildRequestApprovalDetailsFormLayout(Long key) {
		
		
		OldInitiatePedEndorsement initiate = pedService.findByKey(key);
		
		RequestApprovalDetailsFormLayout = new FormLayout();
		RequestApprovalDetailsFormLayout.setCaption("Query Remarks(Approver) ");
		//Vaadin8-setImmediate() RequestApprovalDetailsFormLayout.setImmediate(false);
		RequestApprovalDetailsFormLayout.setWidth("100.0%");
		RequestApprovalDetailsFormLayout.setHeight("100.0%");
		RequestApprovalDetailsFormLayout.setMargin(true);
		RequestApprovalDetailsFormLayout.setSpacing(true);
//		RequestApprovalDetailsFormLayout.addStyleName("layoutDesign");
		RequestApprovalDetailsFormLayout.setStyleName("background");
		
		
		pEDStatusApprovalDetailstxt = new TextField("PED Status");
		//Vaadin8-setImmediate() pEDStatusApprovalDetailstxt.setImmediate(false);
		pEDStatusApprovalDetailstxt.setWidth("-1px");
		pEDStatusApprovalDetailstxt.setHeight("-1px");
		if (initiate != null) {
			if (initiate.getStatus() != null && null != initiate.getProcessorRemarks()) {
				pEDStatusApprovalDetailstxt.setValue(initiate.getStatus()
						.getProcessValue());
			}
		}
		
		RequestApprovalDetailsFormLayout
				.addComponent(pEDStatusApprovalDetailstxt);
		
		
        PedQuery pedQuery=pedService.getPedQueryforApprovePed(key);
		
		queryRemarktxt = new TextField("Query Remarks(Approver)");
		//Vaadin8-setImmediate() queryRemarktxt.setImmediate(false);
		queryRemarktxt.setWidth("-1px");
		queryRemarktxt.setHeight("-1px");
		
		if(pedQuery != null){
			if(pedQuery.getQueryRemarks()!=null && null != pedQuery.getInitiatorType()
					&& ("A").equalsIgnoreCase(pedQuery.getInitiatorType())){
				
			queryRemarktxt.setValue(pedQuery.getQueryRemarks());
			
			}
		}
		RequestApprovalDetailsFormLayout.addComponent(queryRemarktxt);

		PedSpecialist pedSpecialist=pedService.getSpecialistforApprover(key);
		
		specialisttype = new TextField("Specialist Type");
		//Vaadin8-setImmediate() specialisttype.setImmediate(false);
		specialisttype.setWidth("-1px");
		specialisttype.setHeight("-1px");
		if(pedSpecialist != null){
			if(pedSpecialist.getSpecialistType()!=null){
				specialisttype.setValue(pedSpecialist.getSpecialistType().getValue());
			}
		}
		RequestApprovalDetailsFormLayout.addComponent(specialisttype);

		reasonforReferringTxt = new TextField("Reason for Referring (Approver)");
		//Vaadin8-setImmediate() reasonforReferringTxt.setImmediate(false);
		reasonforReferringTxt.setWidth("-1px");
		reasonforReferringTxt.setHeight("-1px");
		if(pedSpecialist != null){
			if(pedSpecialist.getReferringReason()!=null){
				reasonforReferringTxt.setValue(pedSpecialist.getReferringReason());
			}
		}
		RequestApprovalDetailsFormLayout.addComponent(reasonforReferringTxt);

	
		rejectionRemarksTxt = new TextField("Rejection Remarks");
		//Vaadin8-setImmediate() rejectionRemarksTxt.setImmediate(false);
		rejectionRemarksTxt.setWidth("-1px");
		rejectionRemarksTxt.setHeight("-1px");
		
		if(initiate!=null){
			if(initiate.getRejectionRemarks()!=null){
				rejectionRemarksTxt.setValue(initiate.getRejectionRemarks());
			}
		}
		
		RequestApprovalDetailsFormLayout.addComponent(rejectionRemarksTxt);

		approvalRemarksTxt = new TextField("Approval Remarks");
		//Vaadin8-setImmediate() approvalRemarksTxt.setImmediate(false);
		approvalRemarksTxt.setWidth("-1px");
		approvalRemarksTxt.setHeight("-1px");
		if(initiate!=null){
			if(initiate.getApprovalRemarks()!=null){
				approvalRemarksTxt.setValue(initiate.getApprovalRemarks());
			}
		}
		RequestApprovalDetailsFormLayout.addComponent(approvalRemarksTxt);

		return RequestApprovalDetailsFormLayout;
	}

	private Panel buildRequestProcessedQueryRepliedDetailsPanel() {
		requestProcessedQueryRepliedDetailsPanel = new Panel();
		requestProcessedQueryRepliedDetailsPanel
				.setCaption("PED Request Processed - Query Replied Details");
		//Vaadin8-setImmediate() requestProcessedQueryRepliedDetailsPanel.setImmediate(false);
		requestProcessedQueryRepliedDetailsPanel.setWidth("100.0%");
		requestProcessedQueryRepliedDetailsPanel.setHeight("-1px");
		requestProcessedQueryRepliedDetailsPanel.addStyleName("panelStyle");
		queryRepliedDetailsFormLayout = buildQueryRepliedDetailsFormLayout();
		requestProcessedQueryRepliedDetailsPanel
				.setContent(queryRepliedDetailsFormLayout);

		return requestProcessedQueryRepliedDetailsPanel;
	}

	private FormLayout buildQueryRepliedDetailsFormLayout() {
		queryRepliedDetailsFormLayout = new FormLayout();
		//Vaadin8-setImmediate() queryRepliedDetailsFormLayout.setImmediate(false);
		queryRepliedDetailsFormLayout.setWidth("100.0%");
		queryRepliedDetailsFormLayout.setHeight("100.0%");
		queryRepliedDetailsFormLayout.setMargin(true);
		queryRepliedDetailsFormLayout.setSpacing(true);
//		queryRepliedDetailsFormLayout.addStyleName("layoutDesign");
		queryRepliedDetailsFormLayout.setStyleName("background");
		return queryRepliedDetailsFormLayout;
	}

	private Panel buildSpecialistAdviseDetailsPanel() {
		specialistAdviseDetailsPanel = new Panel();
		specialistAdviseDetailsPanel
				.setCaption("PED Request Processed - Specialist Advise Details");
		//Vaadin8-setImmediate() specialistAdviseDetailsPanel.setImmediate(false);
		specialistAdviseDetailsPanel.setWidth("100.0%");
		specialistAdviseDetailsPanel.setHeight("-1px");
		specialistAdviseDetailsPanel.addStyleName("panelStyle");
		specialistAdviseDetailsFormLayout = buildSpecialistAdviseDetailsFormLayout();
		specialistAdviseDetailsPanel
				.setContent(specialistAdviseDetailsFormLayout);

		return specialistAdviseDetailsPanel;
	}

	private Panel buildEscalatedDetailsPanel() {
		escalatedDetailsPanel = new Panel();
		escalatedDetailsPanel
				.setCaption("PED Request Processed - Escalation Details");
		//Vaadin8-setImmediate() escalatedDetailsPanel.setImmediate(false);
		escalatedDetailsPanel.setWidth("100.0%");
		escalatedDetailsPanel.setHeight("-1px");
		escalatedDetailsPanel.addStyleName("panelStyle");
		escalatedDetailsFormLayout = buildEscalatedDetailsFormLayout();
		escalatedDetailsPanel
				.setContent(escalatedDetailsFormLayout);

		return escalatedDetailsPanel;
	}
	
	private FormLayout buildEscalatedDetailsFormLayout(){

		escalatedDetailsFormLayout = new FormLayout();
		//Vaadin8-setImmediate() escalatedDetailsFormLayout.setImmediate(false);
		escalatedDetailsFormLayout.setWidth("100.0%");
		escalatedDetailsFormLayout.setHeight("100.0%");
		escalatedDetailsFormLayout.setMargin(true);
		escalatedDetailsFormLayout.setSpacing(true);
		escalatedDetailsFormLayout.setStyleName("background");

		return escalatedDetailsFormLayout;
	
		
	}
	
	private FormLayout buildSpecialistAdviseDetailsFormLayout() {
		specialistAdviseDetailsFormLayout = new FormLayout();
		//Vaadin8-setImmediate() specialistAdviseDetailsFormLayout.setImmediate(false);
		specialistAdviseDetailsFormLayout.setWidth("100.0%");
		specialistAdviseDetailsFormLayout.setHeight("100.0%");
		specialistAdviseDetailsFormLayout.setMargin(true);
		specialistAdviseDetailsFormLayout.setSpacing(true);
//		specialistAdviseDetailsFormLayout.addStyleName("layoutDesign");
		specialistAdviseDetailsFormLayout.setStyleName("background");
		pedStatusSpecialistAdvisetxt = new TextField("PED Status");
		//Vaadin8-setImmediate() pedStatusSpecialistAdvisetxt.setImmediate(false);
		pedStatusSpecialistAdvisetxt.setWidth("-1px");
		pedStatusSpecialistAdvisetxt.setHeight("-1px");
//		specialistAdviseDetailsFormLayout
//				.addComponent(pedStatusSpecialistAdvisetxt);

		specialistRemarksTxt = new TextArea("Specialist Remarks");
		//Vaadin8-setImmediate() specialistRemarksTxt.setImmediate(false);
		specialistRemarksTxt.setWidth("-1px");
		specialistRemarksTxt.setHeight("-1px");
//		specialistAdviseDetailsFormLayout.addComponent(specialistRemarksTxt);

		return specialistAdviseDetailsFormLayout;
	}

	private void columnMapper(Table table, IndexedContainer container) {

		table.setContainerDataSource(container);
		table.setVisibleColumns(columns);
		table.setPageLength(table.size() + 1);
	}

	public IndexedContainer createContainer() {
		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty("key", Long.class, null);
		container.addContainerProperty("PEDCode", String.class, null);
		container.addContainerProperty("Description", String.class, null);
		container.addContainerProperty("ICDChapter", String.class, null);

		container.addContainerProperty("ICDBlock", String.class, null);

		container.addContainerProperty("ICDCode", String.class, null);
		container.addContainerProperty("Source", String.class, null);
		container.addContainerProperty("OthersSpecify", String.class, null);
		container.addContainerProperty("DoctorRemarks", String.class, null);

		return container;
	}

	public Table createTable(IndexedContainer container, String tableHeader) {

		final Table table = new Table(tableHeader);
		columnMapper(table, container);
		table.setWidth("100%");
		table.setHeight("100%");
		table.setEditable(false);
		return table;
	}

	public void addItem(final Table table, final IndexedContainer container) {
		Object itemId = container.addItem();
		container.getItem(itemId).getItemProperty("key");
		container.getItem(itemId).getItemProperty("PEDCode");
		container.getItem(itemId).getItemProperty("Description");
		container.getItem(itemId).getItemProperty("ICDChapter");
		container.getItem(itemId).getItemProperty("ICDBlock");
		container.getItem(itemId).getItemProperty("ICDCode");
		container.getItem(itemId).getItemProperty("Source");
		container.getItem(itemId).getItemProperty("OthersSpecify");
		container.getItem(itemId).getItemProperty("DoctorRemarks");
	}

	@SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setWidth("600px");
				// field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				//field.setWidth("100%");
				// field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}
	
	private FormLayout buildUnderWritingFormLayout() {
		uwFormLayout = new FormLayout();
		//Vaadin8-setImmediate() uwFormLayout.setImmediate(false);
		//uwFormLayout.setWidth("100.0%");
		//uwFormLayout.setHeight("100.0%");
		uwFormLayout.setMargin(true);
		uwFormLayout.setSpacing(true);
//		uwFormLayout.addStyleName("layoutDesign");

		/*pedDiscussed = new CheckBox("Discussed with PED Team Leader");
		//Vaadin8-setImmediate() pedDiscussed.setImmediate(false);
		pedDiscussed.setWidth("100px");
		pedDiscussed.setHeight("-1px");
		uwFormLayout.addComponent(pedDiscussed);*/

		pedDiscussedWithTxt = new TextField("Discussed with");
		//Vaadin8-setImmediate() pedDiscussedWithTxt.setImmediate(false);
		pedDiscussedWithTxt.setWidth("-1px");
		pedDiscussedWithTxt.setHeight("-1px");
		uwFormLayout.addComponent(pedDiscussedWithTxt);

		pedDiscussedTxt = new TextArea("Suggestion");
		//Vaadin8-setImmediate() pedDiscussedTxt.setImmediate(false);
		pedDiscussedTxt.setWidth("500px");
		pedDiscussedTxt.setHeight("-1px");
		uwFormLayout
				.addComponent(pedDiscussedTxt);

		//uwFormLayout.setWidth("100%");
		return uwFormLayout;
	}
	
	public void viewroleService(UserProductMappingService userMappingService2) {
		this.userMappingService = userMappingService2;
		
	}
}
