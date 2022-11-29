package com.shaic.claim.pedquery;

import java.util.Iterator;

import javax.ejb.EJB;

import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ViewPEDQueryDetails extends Window {
	
	@EJB
	public MasterService masterService;

	private Object[] columns = new Object[] { "PEDCode", "Description",
			"ICDChapter", "ICDBlock", "ICDCode", "Source", "OthersSpecify",
			"DoctorRemarks" };

	private IndexedContainer iContainer;
	private VerticalLayout mainLayout;
	private Panel specialistAdviseDetailsPanel;
	private GridLayout specialistAdviseDetailsGridLayout;
	private TextArea specialistRemarksTxt;
	private Label specialistRemarkslbl;
	private TextField pedStatusSpecialistAdvisetxt;
	private Label pedStatusSpecialistAdvisetlab;
	private Panel requestProcessedQueryRepliedDetailsPanel;
	private GridLayout queryRepliedDetailsGridLayout;
	private TextArea queryReplyRemarksTxt;
	private Label queryReplyRemarksProcessLbl;
	private TextField pedQuerStatusTxt;
	private Label queryReplyRemarksLbl;
	private Panel pedRequestApprovalDetailsPanel;
	private GridLayout RequestApprovalDetailsGridLayout;
	private TextField approvalRemarksTxt;
	private Label approvalRemarksLbl;
	private TextField rejectionRemarksTxt;
	private Label rejectionRemarksLbl;
	private TextField reasonforReferringTxt;
	private Label reasonforReferringLbl;
	private TextField specialisttypeSelect;
	private Label specialistTypeLbl;
	private TextField queryRemarksrtxt;
	private Label queryRemarksrLbl;
	private TextField pEDStatusApprovalDetailstxt;
	private Label pEDStatusApprovalDetailsLbl;
	private Panel pedRequestProcessedDetailsPanel;
	private GridLayout pedRequestProcessedDetailsgridLayout;
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
	private GridLayout requestorDetailsgridLayout;
	private TextField requestedDateTxt;
	private Label requestedDateLbl;
	private TextField requestorIDorNameTxt;
	private Label requestorIDorNameLbl;
	private TextField requestorRolText;
	private Label requestorRolLbl;
	private VerticalLayout pedTableverticalLayout;
	private GridLayout requestInitiationgridLayout;
	private TextField repudiationLetterDateTxt;
	private Label repudiationLetterDateLbl;
	private TextField pedRequsetInitialtionRemarksTxt;
	private Label pedRequsetInitialtionRemarksLbl;
	private TextField nameofPEDTxt;
	private Label nameofPEDLbl;
	private TextField pedSuggestionTxt;
	private Label pedSuggestionLbl;

	public ViewPEDQueryDetails() {
		setCaption("View PED Endorsement Details");
		buildMainLayout();
		setReadOnly(specialistAdviseDetailsGridLayout, true);
		setReadOnly(queryRepliedDetailsGridLayout, true);
		setReadOnly(RequestApprovalDetailsGridLayout, true);
		setReadOnly(pedRequestProcessedDetailsgridLayout, true);
		setReadOnly(requestorDetailsgridLayout, true);
		setReadOnly(requestInitiationgridLayout, true);

		setContent(mainLayout);

	}
	
	public void init(OldPedEndorsementDTO bean){
		
//		System.out.println("ped name-------->"+bean.getPedName());
		
		VerticalLayout verticalMain=buildMainLayout();
		setModal(true);
		setContent(verticalMain);
	}

	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setCaption("Query Remarks(Approver)");
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(false);

		setWidth("800");
		setHeight("700");

		pedRequestInitiationDetailspanel = buildPedRequestInitiationDetailsPanel();
		mainLayout.addComponent(pedRequestInitiationDetailspanel);

		pedRequestProcessedDetailsPanel = buildPedRequestProcessedDetailsPanel();
		mainLayout.addComponent(pedRequestProcessedDetailsPanel);

		pedRequestApprovalDetailsPanel = buildPedRequestApprovalDetailsPanel();
		mainLayout.addComponent(pedRequestApprovalDetailsPanel);

		requestProcessedQueryRepliedDetailsPanel = buildRequestProcessedQueryRepliedDetailsPanel();
		mainLayout.addComponent(requestProcessedQueryRepliedDetailsPanel);

		specialistAdviseDetailsPanel = buildSpecialistAdviseDetailsPanel();
		mainLayout.addComponent(specialistAdviseDetailsPanel);

		return mainLayout;
	}

	private Panel buildPedRequestInitiationDetailsPanel() {
		pedRequestInitiationDetailspanel = new Panel();
		pedRequestInitiationDetailspanel
				.setCaption("PED Request Initiation Details");
		//Vaadin8-setImmediate() pedRequestInitiationDetailspanel.setImmediate(false);
		pedRequestInitiationDetailspanel.setWidth("100.0%");
		pedRequestInitiationDetailspanel.setHeight("-1px");

		pedRequestInitiationDetailsverticalLayout = buildPedRequestInitiationDetailsverticalLayout();
		pedRequestInitiationDetailspanel
				.setContent(pedRequestInitiationDetailsverticalLayout);

		return pedRequestInitiationDetailspanel;
	}

	private VerticalLayout buildPedRequestInitiationDetailsverticalLayout() {
		pedRequestInitiationDetailsverticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() pedRequestInitiationDetailsverticalLayout.setImmediate(false);
		pedRequestInitiationDetailsverticalLayout.setWidth("100.0%");
		pedRequestInitiationDetailsverticalLayout.setHeight("100.0%");
		// pedRequestInitiationDetailsverticalLayout.setMargin(true);
		// pedRequestInitiationDetailsverticalLayout.setSpacing(true);
		requestInitiationgridLayout = buildRequestInitiationgridLayout();
		pedRequestInitiationDetailsverticalLayout
				.addComponent(requestInitiationgridLayout);
		pedRequestInitiationDetailsverticalLayout.setExpandRatio(
				requestInitiationgridLayout, 25.0f);

		pedTableverticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() pedTableverticalLayout.setImmediate(false);
		pedTableverticalLayout.setWidth("-1px");
		pedTableverticalLayout.setHeight("-1px");
		pedTableverticalLayout.setMargin(true);
		pedTableverticalLayout.setSpacing(true);
		this.iContainer = createContainer();
		Table createViewTable = createTable(iContainer, "");
		pedTableverticalLayout.addComponent(createViewTable);

		pedRequestInitiationDetailsverticalLayout
				.addComponent(pedTableverticalLayout);

		requestorDetailsgridLayout = buildRequestorDetailsgridLayout();
		pedRequestInitiationDetailsverticalLayout
				.addComponent(requestorDetailsgridLayout);

		return pedRequestInitiationDetailsverticalLayout;
	}

	private GridLayout buildRequestInitiationgridLayout() {
		requestInitiationgridLayout = new GridLayout();
		requestInitiationgridLayout.setColumnExpandRatio(0, 0.10f);
		//Vaadin8-setImmediate() requestInitiationgridLayout.setImmediate(false);
		requestInitiationgridLayout.setWidth("100.0%");
		requestInitiationgridLayout.setHeight("100.0%");
		requestInitiationgridLayout.setMargin(true);
		requestInitiationgridLayout.setSpacing(true);
		requestInitiationgridLayout.setColumnExpandRatio(0, 0.10f);
		requestInitiationgridLayout.setColumns(3);
		requestInitiationgridLayout.setRows(4);

		pedSuggestionLbl = new Label();
		//Vaadin8-setImmediate() pedSuggestionLbl.setImmediate(false);
		pedSuggestionLbl.setWidth("-1px");
		pedSuggestionLbl.setHeight("-1px");
		pedSuggestionLbl.setValue("PED Suggestion");
		requestInitiationgridLayout.addComponent(pedSuggestionLbl, 0, 0);

		pedSuggestionTxt = new TextField();
		//Vaadin8-setImmediate() pedSuggestionTxt.setImmediate(false);
		pedSuggestionTxt.setWidth("-1px");
		pedSuggestionTxt.setHeight("-1px");
		requestInitiationgridLayout.addComponent(pedSuggestionTxt, 1, 0);

		nameofPEDLbl = new Label();
		//Vaadin8-setImmediate() nameofPEDLbl.setImmediate(false);
		nameofPEDLbl.setWidth("-1px");
		nameofPEDLbl.setHeight("-1px");
		nameofPEDLbl.setValue("Name of PED");
		requestInitiationgridLayout.addComponent(nameofPEDLbl, 0, 1);

		nameofPEDTxt = new TextField();
		//Vaadin8-setImmediate() nameofPEDTxt.setImmediate(false);
		nameofPEDTxt.setWidth("-1px");
		nameofPEDTxt.setHeight("-1px");
		requestInitiationgridLayout.addComponent(nameofPEDTxt, 1, 1);

		pedRequsetInitialtionRemarksLbl = new Label();
		//Vaadin8-setImmediate() pedRequsetInitialtionRemarksLbl.setImmediate(false);
		pedRequsetInitialtionRemarksLbl.setWidth("-1px");
		pedRequsetInitialtionRemarksLbl.setHeight("-1px");
		pedRequsetInitialtionRemarksLbl.setValue("Remarks");
		requestInitiationgridLayout.addComponent(
				pedRequsetInitialtionRemarksLbl, 0, 2);

		pedRequsetInitialtionRemarksTxt = new TextField();
		//Vaadin8-setImmediate() pedRequsetInitialtionRemarksTxt.setImmediate(false);
		pedRequsetInitialtionRemarksTxt.setWidth("-1px");
		pedRequsetInitialtionRemarksTxt.setHeight("-1px");
		requestInitiationgridLayout.addComponent(
				pedRequsetInitialtionRemarksTxt, 1, 2);

		repudiationLetterDateLbl = new Label();
		//Vaadin8-setImmediate() repudiationLetterDateLbl.setImmediate(false);
		repudiationLetterDateLbl.setWidth("-1px");
		repudiationLetterDateLbl.setHeight("-1px");
		repudiationLetterDateLbl.setValue("Repudiation Letter Date");
		requestInitiationgridLayout
				.addComponent(repudiationLetterDateLbl, 0, 3);

		repudiationLetterDateTxt = new TextField();
		//Vaadin8-setImmediate() repudiationLetterDateTxt.setImmediate(false);
		repudiationLetterDateTxt.setWidth("-1px");
		repudiationLetterDateTxt.setHeight("-1px");
		requestInitiationgridLayout
				.addComponent(repudiationLetterDateTxt, 1, 3);

		return requestInitiationgridLayout;
	}

	private GridLayout buildRequestorDetailsgridLayout() {
		requestorDetailsgridLayout = new GridLayout();
		requestorDetailsgridLayout.setCaption("Requestor Details");
		requestorDetailsgridLayout.setColumnExpandRatio(0, 0.10f);
		//Vaadin8-setImmediate() requestorDetailsgridLayout.setImmediate(false);
		requestorDetailsgridLayout.setWidth("100.0%");
		requestorDetailsgridLayout.setHeight("100.0%");
		requestorDetailsgridLayout.setMargin(true);
		requestorDetailsgridLayout.setSpacing(true);
		requestorDetailsgridLayout.setColumns(3);
		requestorDetailsgridLayout.setRows(3);

		requestorRolLbl = new Label();
		//Vaadin8-setImmediate() requestorRolLbl.setImmediate(false);
		requestorRolLbl.setWidth("-1px");
		requestorRolLbl.setHeight("-1px");
		requestorRolLbl.setValue("Requestor Role");
		requestorDetailsgridLayout.addComponent(requestorRolLbl, 0, 0);

		requestorRolText = new TextField();
		//Vaadin8-setImmediate() requestorRolText.setImmediate(false);
		requestorRolText.setWidth("-1px");
		requestorRolText.setHeight("-1px");
		requestorDetailsgridLayout.addComponent(requestorRolText, 1, 0);

		requestorIDorNameLbl = new Label();
		//Vaadin8-setImmediate() requestorIDorNameLbl.setImmediate(false);
		requestorIDorNameLbl.setWidth("-1px");
		requestorIDorNameLbl.setHeight("-1px");
		requestorIDorNameLbl.setValue("Requestor ID / Name");
		requestorDetailsgridLayout.addComponent(requestorIDorNameLbl, 0, 1);

		requestorIDorNameTxt = new TextField();
		//Vaadin8-setImmediate() requestorIDorNameTxt.setImmediate(false);
		requestorIDorNameTxt.setWidth("-1px");
		requestorIDorNameTxt.setHeight("-1px");
		requestorDetailsgridLayout.addComponent(requestorIDorNameTxt, 1, 1);

		requestedDateLbl = new Label();
		//Vaadin8-setImmediate() requestedDateLbl.setImmediate(false);
		requestedDateLbl.setWidth("-1px");
		requestedDateLbl.setHeight("-1px");
		requestedDateLbl.setValue("Requested Date");
		requestorDetailsgridLayout.addComponent(requestedDateLbl, 0, 2);

		requestedDateTxt = new TextField();
		//Vaadin8-setImmediate() requestedDateTxt.setImmediate(false);
		requestedDateTxt.setWidth("-1px");
		requestedDateTxt.setHeight("-1px");
		requestorDetailsgridLayout.addComponent(requestedDateTxt, 1, 2);

		return requestorDetailsgridLayout;
	}

	private Panel buildPedRequestProcessedDetailsPanel() {
		pedRequestProcessedDetailsPanel = new Panel();
		pedRequestProcessedDetailsPanel
				.setCaption("PED Request Processed Details");
		//Vaadin8-setImmediate() pedRequestProcessedDetailsPanel.setImmediate(false);
		pedRequestProcessedDetailsPanel.setWidth("100.0%");
		pedRequestProcessedDetailsPanel.setHeight("-1px");

		pedRequestProcessedDetailsgridLayout = buildPedRequestProcessedDetailsgridLayout();
		pedRequestProcessedDetailsPanel
				.setContent(pedRequestProcessedDetailsgridLayout);

		return pedRequestProcessedDetailsPanel;
	}

	private GridLayout buildPedRequestProcessedDetailsgridLayout() {
		pedRequestProcessedDetailsgridLayout = new GridLayout();
		pedRequestProcessedDetailsgridLayout.setColumnExpandRatio(0, 0.10f);
		//Vaadin8-setImmediate() pedRequestProcessedDetailsgridLayout.setImmediate(false);
		pedRequestProcessedDetailsgridLayout.setWidth("100.0%");
		pedRequestProcessedDetailsgridLayout.setHeight("100.0%");
		pedRequestProcessedDetailsgridLayout.setMargin(true);
		pedRequestProcessedDetailsgridLayout.setSpacing(true);
		pedRequestProcessedDetailsgridLayout.setColumns(3);
		pedRequestProcessedDetailsgridLayout.setRows(4);

		pedStatusProcessedLbl = new Label();
		//Vaadin8-setImmediate() pedStatusProcessedLbl.setImmediate(false);
		pedStatusProcessedLbl.setWidth("-1px");
		pedStatusProcessedLbl.setHeight("-1px");
		pedStatusProcessedLbl.setValue("PED Status");
		pedRequestProcessedDetailsgridLayout.addComponent(
				pedStatusProcessedLbl, 0, 0);

		pedStatusProcessedTxt = new TextField();
		//Vaadin8-setImmediate() pedStatusProcessedTxt.setImmediate(false);
		pedStatusProcessedTxt.setWidth("-1px");
		pedStatusProcessedTxt.setHeight("-1px");
		pedRequestProcessedDetailsgridLayout.addComponent(
				pedStatusProcessedTxt, 1, 0);

		specialistTypelbl = new Label();
		//Vaadin8-setImmediate() specialistTypelbl.setImmediate(false);
		specialistTypelbl.setWidth("-1px");
		specialistTypelbl.setHeight("-1px");
		specialistTypelbl.setValue("Specialist Type");
		pedRequestProcessedDetailsgridLayout.addComponent(specialistTypelbl, 0,
				1);

		specialistTypenativeSelect = new TextField();
		//Vaadin8-setImmediate() specialistTypenativeSelect.setImmediate(false);
		specialistTypenativeSelect.setWidth("-1px");
		specialistTypenativeSelect.setHeight("-1px");
		pedRequestProcessedDetailsgridLayout.addComponent(
				specialistTypenativeSelect, 1, 1);

		ReasoforReferringProcessorLbl = new Label();
		//Vaadin8-setImmediate() ReasoforReferringProcessorLbl.setImmediate(false);
		ReasoforReferringProcessorLbl.setWidth("-1px");
		ReasoforReferringProcessorLbl.setHeight("-1px");
		ReasoforReferringProcessorLbl
				.setValue("Reason for Referring (Processor)");
		pedRequestProcessedDetailsgridLayout.addComponent(
				ReasoforReferringProcessorLbl, 0, 2);

		ReasoforReferringProcessorTxt = new TextField();
		//Vaadin8-setImmediate() ReasoforReferringProcessorTxt.setImmediate(false);
		ReasoforReferringProcessorTxt.setWidth("-1px");
		ReasoforReferringProcessorTxt.setHeight("-1px");
		pedRequestProcessedDetailsgridLayout.addComponent(
				ReasoforReferringProcessorTxt, 1, 2);

		reviewRemarksLbl = new Label();
		//Vaadin8-setImmediate() reviewRemarksLbl.setImmediate(false);
		reviewRemarksLbl.setWidth("-1px");
		reviewRemarksLbl.setHeight("-1px");
		reviewRemarksLbl.setValue("Review Remarks");
		pedRequestProcessedDetailsgridLayout.addComponent(reviewRemarksLbl, 0,
				3);

		// reviewRemarksTxt
		reviewRemarksTxt = new TextField();
		//Vaadin8-setImmediate() reviewRemarksTxt.setImmediate(false);
		reviewRemarksTxt.setWidth("-1px");
		reviewRemarksTxt.setHeight("-1px");
		pedRequestProcessedDetailsgridLayout.addComponent(reviewRemarksTxt, 1,
				3);

		return pedRequestProcessedDetailsgridLayout;
	}

	private Panel buildPedRequestApprovalDetailsPanel() {
		pedRequestApprovalDetailsPanel = new Panel();
		pedRequestApprovalDetailsPanel
				.setCaption("PED Request Approval Details");
		//Vaadin8-setImmediate() pedRequestApprovalDetailsPanel.setImmediate(false);
		pedRequestApprovalDetailsPanel.setWidth("100.0%");
		pedRequestApprovalDetailsPanel.setHeight("-1px");

		RequestApprovalDetailsGridLayout = buildRequestApprovalDetailsGridLayout();
		pedRequestApprovalDetailsPanel
				.setContent(RequestApprovalDetailsGridLayout);

		return pedRequestApprovalDetailsPanel;
	}

	private GridLayout buildRequestApprovalDetailsGridLayout() {
		RequestApprovalDetailsGridLayout = new GridLayout();
		RequestApprovalDetailsGridLayout.setColumnExpandRatio(0, 0.10f);
		RequestApprovalDetailsGridLayout.setCaption("Query Remarks(Approver) ");
		//Vaadin8-setImmediate() RequestApprovalDetailsGridLayout.setImmediate(false);
		RequestApprovalDetailsGridLayout.setWidth("100.0%");
		RequestApprovalDetailsGridLayout.setHeight("100.0%");
		RequestApprovalDetailsGridLayout.setMargin(true);
		RequestApprovalDetailsGridLayout.setSpacing(true);
		RequestApprovalDetailsGridLayout.setColumns(3);
		RequestApprovalDetailsGridLayout.setRows(6);

		pEDStatusApprovalDetailsLbl = new Label();
		//Vaadin8-setImmediate() pEDStatusApprovalDetailsLbl.setImmediate(false);
		pEDStatusApprovalDetailsLbl.setWidth("-1px");
		pEDStatusApprovalDetailsLbl.setHeight("-1px");
		pEDStatusApprovalDetailsLbl.setValue("PED Status");
		RequestApprovalDetailsGridLayout.addComponent(
				pEDStatusApprovalDetailsLbl, 0, 0);

		pEDStatusApprovalDetailstxt = new TextField();
		//Vaadin8-setImmediate() pEDStatusApprovalDetailstxt.setImmediate(false);
		pEDStatusApprovalDetailstxt.setWidth("-1px");
		pEDStatusApprovalDetailstxt.setHeight("-1px");
		RequestApprovalDetailsGridLayout.addComponent(
				pEDStatusApprovalDetailstxt, 1, 0);

		queryRemarksrLbl = new Label();
		//Vaadin8-setImmediate() queryRemarksrLbl.setImmediate(false);
		queryRemarksrLbl.setWidth("-1px");
		queryRemarksrLbl.setHeight("-1px");
		queryRemarksrLbl.setValue("Query Remarks(Approver)");
		RequestApprovalDetailsGridLayout.addComponent(queryRemarksrLbl, 0, 1);

		queryRemarksrtxt = new TextField();
		//Vaadin8-setImmediate() queryRemarksrtxt.setImmediate(false);
		queryRemarksrtxt.setWidth("-1px");
		queryRemarksrtxt.setHeight("-1px");
		RequestApprovalDetailsGridLayout.addComponent(queryRemarksrtxt, 1, 1);

		specialistTypeLbl = new Label();
		//Vaadin8-setImmediate() specialistTypeLbl.setImmediate(false);
		specialistTypeLbl.setWidth("-1px");
		specialistTypeLbl.setHeight("-1px");
		specialistTypeLbl.setValue("Specialist Type");
		RequestApprovalDetailsGridLayout.addComponent(specialistTypeLbl, 0, 2);

		specialisttypeSelect = new TextField();
		//Vaadin8-setImmediate() specialisttypeSelect.setImmediate(false);
		specialisttypeSelect.setWidth("-1px");
		specialisttypeSelect.setHeight("-1px");
		RequestApprovalDetailsGridLayout.addComponent(specialisttypeSelect, 1,
				2);

		reasonforReferringLbl = new Label();
		//Vaadin8-setImmediate() reasonforReferringLbl.setImmediate(false);
		reasonforReferringLbl.setWidth("-1px");
		reasonforReferringLbl.setHeight("-1px");
		reasonforReferringLbl.setValue("Reason for Referring (Approver)");
		RequestApprovalDetailsGridLayout.addComponent(reasonforReferringLbl, 0,
				3);

		reasonforReferringTxt = new TextField();
		//Vaadin8-setImmediate() reasonforReferringTxt.setImmediate(false);
		reasonforReferringTxt.setWidth("-1px");
		reasonforReferringTxt.setHeight("-1px");
		RequestApprovalDetailsGridLayout.addComponent(reasonforReferringTxt, 1,
				3);

		// label_5
		rejectionRemarksLbl = new Label();
		//Vaadin8-setImmediate() rejectionRemarksLbl.setImmediate(false);
		rejectionRemarksLbl.setWidth("-1px");
		rejectionRemarksLbl.setHeight("-1px");
		rejectionRemarksLbl.setValue("Rejection Remarks");
		RequestApprovalDetailsGridLayout
				.addComponent(rejectionRemarksLbl, 0, 4);

		rejectionRemarksTxt = new TextField();
		//Vaadin8-setImmediate() rejectionRemarksTxt.setImmediate(false);
		rejectionRemarksTxt.setWidth("-1px");
		rejectionRemarksTxt.setHeight("-1px");
		RequestApprovalDetailsGridLayout
				.addComponent(rejectionRemarksTxt, 1, 4);

		approvalRemarksLbl = new Label();
		//Vaadin8-setImmediate() approvalRemarksLbl.setImmediate(false);
		approvalRemarksLbl.setWidth("-1px");
		approvalRemarksLbl.setHeight("-1px");
		approvalRemarksLbl.setValue("Approval Remarks");
		RequestApprovalDetailsGridLayout.addComponent(approvalRemarksLbl, 0, 5);

		approvalRemarksTxt = new TextField();
		//Vaadin8-setImmediate() approvalRemarksTxt.setImmediate(false);
		approvalRemarksTxt.setWidth("-1px");
		approvalRemarksTxt.setHeight("-1px");
		RequestApprovalDetailsGridLayout.addComponent(approvalRemarksTxt, 1, 5);

		return RequestApprovalDetailsGridLayout;
	}

	private Panel buildRequestProcessedQueryRepliedDetailsPanel() {
		requestProcessedQueryRepliedDetailsPanel = new Panel();
		requestProcessedQueryRepliedDetailsPanel
				.setCaption("PED Request Processed - Query Replied Details");
		//Vaadin8-setImmediate() requestProcessedQueryRepliedDetailsPanel.setImmediate(false);
		requestProcessedQueryRepliedDetailsPanel.setWidth("100.0%");
		requestProcessedQueryRepliedDetailsPanel.setHeight("-1px");

		queryRepliedDetailsGridLayout = buildQueryRepliedDetailsGridLayout();
		requestProcessedQueryRepliedDetailsPanel
				.setContent(queryRepliedDetailsGridLayout);

		return requestProcessedQueryRepliedDetailsPanel;
	}

	private GridLayout buildQueryRepliedDetailsGridLayout() {
		queryRepliedDetailsGridLayout = new GridLayout();
		queryRepliedDetailsGridLayout.setColumnExpandRatio(0, 0.10f);
		//Vaadin8-setImmediate() queryRepliedDetailsGridLayout.setImmediate(false);
		queryRepliedDetailsGridLayout.setWidth("100.0%");
		queryRepliedDetailsGridLayout.setHeight("100.0%");
		queryRepliedDetailsGridLayout.setMargin(true);
		queryRepliedDetailsGridLayout.setSpacing(true);
		queryRepliedDetailsGridLayout.setColumns(3);
		queryRepliedDetailsGridLayout.setRows(2);

		queryReplyRemarksLbl = new Label();
		//Vaadin8-setImmediate() queryReplyRemarksLbl.setImmediate(false);
		queryReplyRemarksLbl.setWidth("-1px");
		queryReplyRemarksLbl.setHeight("-1px");
		queryReplyRemarksLbl.setValue("PED Status");
		queryRepliedDetailsGridLayout.addComponent(queryReplyRemarksLbl, 0, 0);

		pedQuerStatusTxt = new TextField();
		//Vaadin8-setImmediate() pedQuerStatusTxt.setImmediate(false);
		pedQuerStatusTxt.setWidth("-1px");
		pedQuerStatusTxt.setHeight("-1px");
		queryRepliedDetailsGridLayout.addComponent(pedQuerStatusTxt, 1, 0);

		queryReplyRemarksProcessLbl = new Label();
		//Vaadin8-setImmediate() queryReplyRemarksProcessLbl.setImmediate(false);
		queryReplyRemarksProcessLbl.setWidth("-1px");
		queryReplyRemarksProcessLbl.setHeight("-1px");
		queryReplyRemarksProcessLbl.setValue("Query Reply Remarks (Process)");
		queryRepliedDetailsGridLayout.addComponent(queryReplyRemarksProcessLbl,
				0, 1);

		queryReplyRemarksTxt = new TextArea();
		//Vaadin8-setImmediate() queryReplyRemarksTxt.setImmediate(false);
		queryReplyRemarksTxt.setWidth("-1px");
		queryReplyRemarksTxt.setHeight("-1px");
		queryRepliedDetailsGridLayout.addComponent(queryReplyRemarksTxt, 1, 1);

		return queryRepliedDetailsGridLayout;
	}

	private Panel buildSpecialistAdviseDetailsPanel() {
		specialistAdviseDetailsPanel = new Panel();
		specialistAdviseDetailsPanel
				.setCaption("PED Request Processed - Specialist Advise Details");
		//Vaadin8-setImmediate() specialistAdviseDetailsPanel.setImmediate(false);
		specialistAdviseDetailsPanel.setWidth("100.0%");
		specialistAdviseDetailsPanel.setHeight("-1px");

		specialistAdviseDetailsGridLayout = buildSpecialistAdviseDetailsGridLayout();
		specialistAdviseDetailsPanel
				.setContent(specialistAdviseDetailsGridLayout);

		return specialistAdviseDetailsPanel;
	}

	private GridLayout buildSpecialistAdviseDetailsGridLayout() {
		specialistAdviseDetailsGridLayout = new GridLayout();
		specialistAdviseDetailsGridLayout.setColumnExpandRatio(0, 0.10f);
		//Vaadin8-setImmediate() specialistAdviseDetailsGridLayout.setImmediate(false);
		specialistAdviseDetailsGridLayout.setWidth("100.0%");
		specialistAdviseDetailsGridLayout.setHeight("100.0%");
		specialistAdviseDetailsGridLayout.setMargin(true);
		specialistAdviseDetailsGridLayout.setSpacing(true);
		specialistAdviseDetailsGridLayout.setColumns(3);
		specialistAdviseDetailsGridLayout.setRows(2);

		pedStatusSpecialistAdvisetlab = new Label();
		//Vaadin8-setImmediate() pedStatusSpecialistAdvisetlab.setImmediate(false);
		pedStatusSpecialistAdvisetlab.setWidth("-1px");
		pedStatusSpecialistAdvisetlab.setHeight("-1px");
		pedStatusSpecialistAdvisetlab.setValue("PED Status");
		specialistAdviseDetailsGridLayout.addComponent(
				pedStatusSpecialistAdvisetlab, 0, 0);

		pedStatusSpecialistAdvisetxt = new TextField();
		//Vaadin8-setImmediate() pedStatusSpecialistAdvisetxt.setImmediate(false);
		pedStatusSpecialistAdvisetxt.setWidth("-1px");
		pedStatusSpecialistAdvisetxt.setHeight("-1px");
		specialistAdviseDetailsGridLayout.addComponent(
				pedStatusSpecialistAdvisetxt, 1, 0);

		specialistRemarkslbl = new Label();
		//Vaadin8-setImmediate() specialistRemarkslbl.setImmediate(false);
		specialistRemarkslbl.setWidth("-1px");
		specialistRemarkslbl.setHeight("-1px");
		specialistRemarkslbl.setValue("Specialist Remarks");
		specialistAdviseDetailsGridLayout.addComponent(specialistRemarkslbl, 0,
				1);

		specialistRemarksTxt = new TextArea();
		//Vaadin8-setImmediate() specialistRemarksTxt.setImmediate(false);
		specialistRemarksTxt.setWidth("-1px");
		specialistRemarksTxt.setHeight("-1px");
		specialistAdviseDetailsGridLayout.addComponent(specialistRemarksTxt, 1,
				1);

		return specialistAdviseDetailsGridLayout;
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

	@SuppressWarnings({ "rawtypes", "deprecation" })
	private void setReadOnly(GridLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setWidth("350px");
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}else if(c instanceof TextArea){
				TextArea field = (TextArea) c;
				field.setWidth("350px");
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}
}
