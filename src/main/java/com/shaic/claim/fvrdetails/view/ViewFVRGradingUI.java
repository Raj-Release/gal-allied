package com.shaic.claim.fvrdetails.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.fvrdetailedview.FvrDetailedViewUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.NewMedicalDecisionFVRGradingAListenerTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.NewMedicalDecisionFVRGradingBListenerTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.NewMedicalDecisionFVRGradingCListenerTable;
import com.shaic.domain.FVRGradingDetail;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.Intimation;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.themes.Reindeer;

public class ViewFVRGradingUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Label lblFvrGradingDetails;

	private Label lblFvrSequence;

	private TextField txtRepresentativeCode;

	private TextField txtRepresentativeName;

	private TextField txtAssignedDate;

	private TextField txtReceivedDate;

	private TextField txtFvrTat;

	private Button btnClose;

	@Inject
	private ViewFVRGraddingTable viewFVRGraddingTable;
	@Inject
	private NewMedicalDecisionFVRGradingAListenerTable segmentA;
	
	@Inject
	private NewMedicalDecisionFVRGradingBListenerTable segmentB;
	
	@Inject
	private NewMedicalDecisionFVRGradingCListenerTable segmentC;
	
	private List<ViewFVRDTO> viewFVRDTOList = null;

	private List<ViewFVRDTO> viewFVRDTO;
	
	private ViewFVRDTO viewFVRDTO2;

	private List<FieldVisitRequest> fieldVisitRequestList;

	@Inject
	private ViewFVRMapper viewFVRMapper;

	@Inject
	private FieldVisitRequestService fieldVisitRequestService;

	private BeanFieldGroup<ViewFVRDTO> binder;

	private VerticalLayout mainLayout;

	private Window popUp;
	
	private ViewFVRDetailsTable viewFVRDetailsTable;
	
	private List<FVRGradingDetail> fvrGradingDetailsList;
	
	private List<NewFVRGradingDTO> fvrGradingDtoList;
	
	private Boolean isPA;
	
	private TextArea gradingRmrks;
	
	private FvrDetailedViewUI fvrDetailedViewUI;

	private void initBinder() {
		this.binder = new BeanFieldGroup<ViewFVRDTO>(ViewFVRDTO.class);
		this.binder.setItemDataSource(new ViewFVRDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	@SuppressWarnings("static-access")
	public void init(Long fvrKey, ViewFVRDetailsTable viewFVRDetailsTable, ViewFVRDTO viewFVRDTO3) {
		this.viewFVRDTO2 = viewFVRDTO3;
		initBinder();
		this.viewFVRDetailsTable = viewFVRDetailsTable;
		lblFvrGradingDetails = new Label("FVR Grading Details");
		lblFvrGradingDetails.setStyleName(Reindeer.LABEL_H1);
		lblFvrSequence = new Label("FVR Sequence");
		txtRepresentativeCode = binder.buildAndBind("Representative Code",
				"representativeCode", TextField.class);
		txtRepresentativeCode.setEnabled(false);
		txtRepresentativeName = binder.buildAndBind("Representative Name",
				"representativeName", TextField.class);
		txtRepresentativeName.setEnabled(false);
		txtAssignedDate = binder.buildAndBind("FVR Assigned Date",
				"fvrassignedDate", TextField.class);
		txtAssignedDate.setEnabled(false);
		txtReceivedDate = binder.buildAndBind("FVR Received Date",
				"fVRreceivedDate", TextField.class);
		txtReceivedDate.setEnabled(false);
		txtFvrTat = binder.buildAndBind("FVR TAT", "fvrTat", TextField.class);
		txtFvrTat.setEnabled(false);
		
		gradingRmrks = binder.buildAndBind("Doctor's Grading suggestions (FVR)",
				"gradingRemarks", TextArea.class);
		gradingRmrks.setEnabled(false);
		gradingRmrks.setWidth("400px");
		
		btnClose = new Button("Close");

		viewFVRGraddingTable.init("", false, false);
		segmentA.init(null);
		segmentA.removeRow();
		segmentB.init(null);
		segmentB.removeRow();
		segmentC.init(null);
		segmentC.removeRow();
		
		fieldVisitRequestList = fieldVisitRequestService
				.getFieldVisitRequestByKey(fvrKey);
		
		fvrGradingDetailsList = fieldVisitRequestService.getFvrGradingDetailsByFvrKey(fvrKey);

		if (!fieldVisitRequestList.isEmpty()) {
			List<FieldVisitRequest> fvrIntimationList = new ArrayList<FieldVisitRequest>();
			fvrIntimationList = fieldVisitRequestService
					.getFieldVisitRequestByIntimationKey(fieldVisitRequestList
							.get(0).getIntimation().getKey());
			viewFVRDTO = new ArrayList<ViewFVRDTO>();
			viewFVRMapper.getAllMapValues();
			viewFVRDTO = viewFVRMapper.getViewFvrDto(fieldVisitRequestList);
			if (!viewFVRDTO.isEmpty()) {
				setViewData(viewFVRDTO2, fvrIntimationList);
			}
			
			Intimation intimation = fieldVisitRequestList
					.get(0).getIntimation();
			
			if(intimation != null && intimation.getProcessClaimType() != null && intimation.getProcessClaimType().equalsIgnoreCase("P")){
				isPA = Boolean.TRUE;
			}else{
				isPA = Boolean.FALSE;
			}
			if(isPA != null && isPA){
				viewFVRDTOList = viewFVRMapper
						.getFvrGraddingDetails(fieldVisitRequestList.get(0));
				viewFVRGraddingTable.setTableList(viewFVRDTOList);
			}
			
			
			fvrGradingDtoList = viewFVRMapper.getFvrGraddingDetailsNew(fvrGradingDetailsList);
			
			if(fvrGradingDtoList != null && !fvrGradingDtoList.isEmpty()){
				segmentA.setTableList(fvrGradingDtoList);
				segmentB.setTableList(fvrGradingDtoList);
				segmentC.setTableList(fvrGradingDtoList);	
			}
		}

		FormLayout leftLayout = new FormLayout(txtRepresentativeCode,
				txtRepresentativeName);
		leftLayout.setSpacing(true);
		leftLayout.setMargin(true);
		FormLayout rightLayout = new FormLayout(txtAssignedDate,
				txtReceivedDate, txtFvrTat);
		rightLayout.setSpacing(true);
		rightLayout.setMargin(true);
		HorizontalLayout horizontalLayout = new HorizontalLayout(leftLayout,
				rightLayout);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setMargin(true);
		
		if(isPA != null && isPA){
			mainLayout = new VerticalLayout(lblFvrGradingDetails, lblFvrSequence,
					horizontalLayout,viewFVRGraddingTable, btnClose);	
		}else{
			mainLayout = new VerticalLayout(lblFvrGradingDetails, lblFvrSequence,
					horizontalLayout, segmentA,segmentB,segmentC,gradingRmrks, btnClose);
			
			mainLayout.setComponentAlignment(gradingRmrks, Alignment.MIDDLE_CENTER);
		}
		
		
		mainLayout.setComponentAlignment(btnClose, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		addListener();
		setCompositionRoot(mainLayout);
	}
	
	@SuppressWarnings("static-access")
	public void init(Long fvrKey, FvrDetailedViewUI instance, ViewFVRDTO viewFVRDTO3) {
		this.viewFVRDTO2 = viewFVRDTO3;
		initBinder();
		this.fvrDetailedViewUI = instance;
		lblFvrGradingDetails = new Label("FVR Grading Details");
		lblFvrGradingDetails.setStyleName(Reindeer.LABEL_H1);
		lblFvrSequence = new Label("FVR Sequence");
		txtRepresentativeCode = binder.buildAndBind("Representative Code",
				"representativeCode", TextField.class);
		txtRepresentativeCode.setEnabled(false);
		txtRepresentativeName = binder.buildAndBind("Representative Name",
				"representativeName", TextField.class);
		txtRepresentativeName.setEnabled(false);
		txtAssignedDate = binder.buildAndBind("FVR Assigned Date",
				"fvrassignedDate", TextField.class);
		txtAssignedDate.setEnabled(false);
		txtReceivedDate = binder.buildAndBind("FVR Received Date",
				"fVRreceivedDate", TextField.class);
		txtReceivedDate.setEnabled(false);
		txtFvrTat = binder.buildAndBind("FVR TAT", "fvrTat", TextField.class);
		txtFvrTat.setEnabled(false);
		gradingRmrks = binder.buildAndBind("Doctor's Grading suggestions (FVR)",
				"gradingRemarks", TextArea.class);
		gradingRmrks.setEnabled(false);
		gradingRmrks.setWidth("400px");
		
		btnClose = new Button("Close");

		viewFVRGraddingTable.init("", false, false);
		segmentA.init(null);
		segmentA.removeRow();
		segmentB.init(null);
		segmentB.removeRow();
		segmentC.init(null);
		segmentC.removeRow();
		
		fieldVisitRequestList = fieldVisitRequestService
				.getFieldVisitRequestByKey(fvrKey);
		
		fvrGradingDetailsList = fieldVisitRequestService.getFvrGradingDetailsByFvrKey(fvrKey);

		if (!fieldVisitRequestList.isEmpty()) {
			List<FieldVisitRequest> fvrIntimationList = new ArrayList<FieldVisitRequest>();
			fvrIntimationList = fieldVisitRequestService
					.getFieldVisitRequestByIntimationKey(fieldVisitRequestList
							.get(0).getIntimation().getKey());
			viewFVRDTO = new ArrayList<ViewFVRDTO>();
			viewFVRMapper.getAllMapValues();
			viewFVRDTO = viewFVRMapper.getViewFvrDto(fieldVisitRequestList);
			if (!viewFVRDTO.isEmpty()) {
				setViewData(viewFVRDTO2, fvrIntimationList);
			}
			
			Intimation intimation = fieldVisitRequestList
					.get(0).getIntimation();
			
			if(intimation != null && intimation.getProcessClaimType() != null && intimation.getProcessClaimType().equalsIgnoreCase("P")){
				isPA = Boolean.TRUE;
			}else{
				isPA = Boolean.FALSE;
			}
			if(isPA != null && isPA){
				viewFVRDTOList = viewFVRMapper
						.getFvrGraddingDetails(fieldVisitRequestList.get(0));
				viewFVRGraddingTable.setTableList(viewFVRDTOList);
			}
			
			
			fvrGradingDtoList = viewFVRMapper.getFvrGraddingDetailsNew(fvrGradingDetailsList);
			
			if(fvrGradingDtoList != null && !fvrGradingDtoList.isEmpty()){
				segmentA.setTableList(fvrGradingDtoList);
				segmentB.setTableList(fvrGradingDtoList);
				segmentC.setTableList(fvrGradingDtoList);	
			}
		}

		FormLayout leftLayout = new FormLayout(txtRepresentativeCode,
				txtRepresentativeName);
		leftLayout.setSpacing(true);
		leftLayout.setMargin(true);
		FormLayout rightLayout = new FormLayout(txtAssignedDate,
				txtReceivedDate, txtFvrTat);
		rightLayout.setSpacing(true);
		rightLayout.setMargin(true);
		HorizontalLayout horizontalLayout = new HorizontalLayout(leftLayout,
				rightLayout);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setMargin(true);
		
		
		if(isPA != null && isPA){
			mainLayout = new VerticalLayout(lblFvrGradingDetails, lblFvrSequence,
					horizontalLayout,viewFVRGraddingTable, btnClose);	
		}else{
			mainLayout = new VerticalLayout(lblFvrGradingDetails, lblFvrSequence,
					horizontalLayout, segmentA,segmentB,segmentC,gradingRmrks, btnClose);
		}
		
		
		mainLayout.setComponentAlignment(btnClose, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		addListener();
		setCompositionRoot(mainLayout);
	}

	private void setViewData(ViewFVRDTO viewFVRDTO,
			List<FieldVisitRequest> fvrIntimationList) {
		if (viewFVRDTO != null && !fvrIntimationList.isEmpty()) {
			for (int index = 0; index < fvrIntimationList.size(); index++) {
				if (viewFVRDTO.getKey().equals(
						fvrIntimationList.get(index).getKey())) {
					int value = index + 1;
					String fvrValue = "";
					if (value == 1) {
						fvrValue = value + "st";
					}
					if (value == 2) {
						fvrValue = value + "nd";
					}
					if (value == 3) {
						fvrValue = value + "rd";
					}
					if (value >= 4) {
						fvrValue = value + "th";
					}
					lblFvrSequence
							.setValue("FVR Sequence " + fvrValue + " FVR");
				}
			}
			Date tempDate = new Date();
			txtRepresentativeCode.setValue(viewFVRDTO.getRepresentativeCode());
			txtRepresentativeName.setValue(viewFVRDTO.getRepresentativeName());
			if (viewFVRDTO.getfVRAssignedDate() != null) {
				tempDate = SHAUtils.formatTimestamp(viewFVRDTO
						.getfVRAssignedDate().toString());
				txtAssignedDate.setValue(SHAUtils.formatDate(tempDate));
			}
			if (viewFVRDTO.getfVRReceivedDate() != null) {
				tempDate = SHAUtils.formatTimestamp(viewFVRDTO
						.getfVRReceivedDate().toString());
				txtReceivedDate.setValue(SHAUtils.formatDate(tempDate));
			}
			if (viewFVRDTO.getfVRAssignedDate() != null
					&& viewFVRDTO.getfVRReceivedDate() != null) {
				Long days = SHAUtils.getDaysBetweenDate(
						viewFVRDTO.getfVRAssignedDate(),
						viewFVRDTO.getfVRReceivedDate());
				txtFvrTat.setValue(days != null ? days.toString() : "");
			}
			gradingRmrks.setValue(viewFVRDTO.getGradingRemarks());
		}
	}

	private void addListener() {
		btnClose.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(fvrDetailedViewUI != null){
					fvrDetailedViewUI.closePopup();
				}else if(viewFVRDetailsTable != null){
					viewFVRDetailsTable.closePopup();
				}
				
			}
		});
	}

}
