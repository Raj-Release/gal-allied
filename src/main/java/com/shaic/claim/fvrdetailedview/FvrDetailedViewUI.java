package com.shaic.claim.fvrdetailedview;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jfree.util.Log;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRDetailsTable;
import com.shaic.claim.fvrdetails.view.ViewFVRGradingUI;
import com.shaic.claim.fvrdetails.view.ViewFVRMapper;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.FvrNotRequired;
import com.shaic.domain.FvrTriggerPoint;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author GokulPrasath.A
 *
 */
public class FvrDetailedViewUI extends ViewComponent {

	private static final long serialVersionUID = 1L;

	private ViewFVRDTO viewFVRDTO;
	
	private FvrDetailedViewDTO fvrDetailedViewDTO;
	
	private Window popup;

	@Inject
	private ViewFVRGradingUI viewFVRGradingUI;
	
	private FvrDetailedViewUI instance;
	
	@EJB
	private FieldVisitRequestService fieldVisitRequestService;
	
	@EJB
	private CreateRODService billDetailsService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	@EJB
	private ClaimService claimService;
    
    @Inject
    private FvrNotRequiredDetailsTable fvrNotRequiredDetailsTable;

	@Inject
	private ViewFVRMapper viewFVRMapper;
	
	VerticalLayout fvrDetailedViewLayout ;
	
	private BeanFieldGroup<FvrDetailedViewDTO> binder;
	
	private TextField hospitalName;
	
	private OptionGroup initiateFVR;
	
	private TextField allocationTo;
	
	private TextField fvrInitiatedBy;
	
	private TextField fvrPriority;
    
    private TextField fvrInitiatedDateTime;
	
	private TextField fvrCancelledBy;
	
	private TextField fvrStatus;
    
    private TextField fvrCancelledDateTime;
    
    private TextArea fvrClosedRemarks;
	
	private TextArea fvrExecutiveClosedReason;
	
	private TextField fvrAssignedBy;
    
    private TextField fvrAssignedDateTime;
	
	private TextField repCode;
    
    private TextArea fvrRemarks;
	
	private TextField repName;
	
	private TextField repContactNo;
	
	private TextField fvrReplyReceivedFrom;
    
    private TextField hospitalVisitedDate;
    
    private TextField fvrReplyReceivedDateTime;
	
	private TextField fvrGradedBy;
    
    private TextArea fvrGradedRemarks;
    
    private TextField fvrGradedDateTime;
    
    FieldVisitRequest fieldVisitByKey = null;
    
    private TextArea fvrClaimAlertRemarks;
    
	
	//@PostConstruct
	@SuppressWarnings("static-access")
	public void init(ViewFVRDTO viewFVRDTO, FvrDetailedViewUI instance) {
		this.viewFVRDTO = viewFVRDTO;
		this.instance = instance;
		fieldVisitByKey = null;
		
		fieldVisitByKey = fieldVisitRequestService.getFieldVisitByKey(viewFVRDTO.getKey());
		viewFVRMapper.getAllMapValues();
		
			if(fieldVisitByKey != null && fieldVisitByKey.getIntimation() != null && fieldVisitByKey.getIntimation().getKey() != null){
			
				fvrDetailedViewDTO = viewFVRMapper.getFVRDetailView(fieldVisitByKey);
				fvrDetailedViewDTO.setIntimationKey(fieldVisitByKey.getIntimation().getKey());
				initBinder();
				if(fieldVisitByKey.getStatus() != null && fieldVisitByKey.getStatus().getKey().equals(ReferenceTable.FVRCANCELLED)
						|| fieldVisitByKey.getStatus().getKey().equals(ReferenceTable.CORP_LEVEL_CLOSE)){
					fvrDetailedViewDTO.setFvrCancelledBy(fvrDetailedViewDTO.getModifiedBy());
					fvrDetailedViewDTO.setFvrCancelledDateTime(fvrDetailedViewDTO.getModifiedDate());
					fvrDetailedViewDTO.setFvrExecutiveClosedReason(fieldVisitByKey.getExecutiveComments());
				    fvrDetailedViewDTO.setFvrStatus(fieldVisitByKey.getStatus().getProcessValue());
				}else if(fieldVisitByKey.getStatus() != null && fieldVisitByKey.getStatus().getKey().equals(ReferenceTable.FVR_GRADING_STATUS)){
					fvrDetailedViewDTO.setFvrGradedBy(fieldVisitByKey.getModifiedBy());
					fvrDetailedViewDTO.setFvrReplyReceivedFrom(fieldVisitByKey.getModifiedBy());
				}else if(fieldVisitByKey.getStatus() != null && fieldVisitByKey.getStatus().getKey().equals(ReferenceTable.ASSIGNFVR)
						|| fieldVisitByKey.getStatus() != null && fieldVisitByKey.getStatus().getKey().equals(ReferenceTable.FVR_REPLAY_RECIEVED_STATUS)){
					fvrDetailedViewDTO.setFvrReplyReceivedFrom(fieldVisitByKey.getModifiedBy());
				}
				fvrDetailedViewDTO = fieldVisitRequestService.setFVRClaimAlertRemark(fieldVisitByKey.getIntimation().getIntimationId(),fvrDetailedViewDTO);
			}else{
				fvrDetailedViewDTO = new FvrDetailedViewDTO();
				initBinder();
			}
			fvrDetailedViewLayout  = new VerticalLayout();
			Panel processEnhancementPanel	= new Panel();
			//Vaadin8-setImmediate() processEnhancementPanel.setImmediate(false);
			processEnhancementPanel.setWidth("100%");
			processEnhancementPanel.setHeight("50%");
			processEnhancementPanel.setCaption("Field Visit Report - Detailed View");
			processEnhancementPanel.addStyleName("panelHeader");
			processEnhancementPanel.addStyleName("g-search-panel");
			processEnhancementPanel.setContent(buildFVRDetailedViewLayout());
			fvrDetailedViewLayout.addComponent(processEnhancementPanel);
			fvrDetailedViewLayout.setComponentAlignment(processEnhancementPanel, Alignment.MIDDLE_LEFT);
			setCompositionRoot(fvrDetailedViewLayout);
			
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<FvrDetailedViewDTO>(FvrDetailedViewDTO.class);
		this.binder.setItemDataSource(fvrDetailedViewDTO);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	private VerticalLayout buildFVRDetailedViewLayout() 
	{
		
		Panel fvrInitialPanel = fvrInitialPanel();
		fvrInitialPanel.setHeight("80%");
		
		Panel fvrNotRequiredPanel = fvrNotRequiredPanel();
		
		Panel fvrCancelPanel = fvrCancelPanel();
		
		Panel fvrAssignPanel = fvrAssignPanel();
		
		Panel fvrReplyReceivedPanel = fvrReplyReceivedPanel();
		
		Panel fvrGradingPanel = fvrGradingPanel();
		
		
		VerticalLayout verticalLayout = new VerticalLayout(fvrInitialPanel,fvrNotRequiredPanel,fvrCancelPanel,fvrAssignPanel,fvrReplyReceivedPanel,fvrGradingPanel);
		//Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		verticalLayout.setWidth("100.0%");
		verticalLayout.setMargin(false);
		
		return verticalLayout;
	}

	private Panel fvrInitialPanel() {
		
		hospitalName = (TextField) binder.buildAndBind("Hospital Name","hospitalName",TextField.class);
		hospitalName.setValue(viewFVRDTO.getHospitalName());
		hospitalName.setWidth("100%");
		hospitalName.setReadOnly(true);
		hospitalName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		hospitalName.setNullRepresentation("-");
		
		initiateFVR = new OptionGroup("Initiate FVR");
	    initiateFVR.addItem(SHAConstants.YES);
	    initiateFVR.addItem(SHAConstants.No);
	    initiateFVR.addStyleName("horizontal");
	    initiateFVR.select(SHAConstants.YES);
	    initiateFVR.setReadOnly(true);
	    
		allocationTo = (TextField) binder.buildAndBind("Allocation To","allocationTo",TextField.class);
		allocationTo.setReadOnly(true);
		allocationTo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		allocationTo.setNullRepresentation("-");
		
		fvrInitiatedBy = (TextField) binder.buildAndBind("FVR Initiated by","fvrInitiatedBy",TextField.class);
		fvrInitiatedBy.setReadOnly(true);
		fvrInitiatedBy.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fvrInitiatedBy.setNullRepresentation("-");
		
		fvrPriority = (TextField) binder.buildAndBind("Priority","fvrPriority",TextField.class);
		fvrPriority.setReadOnly(true);
		fvrPriority.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fvrPriority.setNullRepresentation("-");
		
		fvrInitiatedDateTime = (TextField) binder.buildAndBind("FVR Initiated Date & Time","fvrInitiatedDateTime",TextField.class);
		fvrInitiatedDateTime.setReadOnly(true);
		fvrInitiatedDateTime.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fvrInitiatedDateTime.setNullRepresentation("-");
		
		fvrClaimAlertRemarks = (TextArea) binder.buildAndBind("FVR Claim Alert Remarks","fvrClaimAlertRemarks",TextArea.class);
		fvrClaimAlertRemarks.setReadOnly(true);
		fvrClaimAlertRemarks.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		fvrClaimAlertRemarks.setNullRepresentation("-");
		fvrClaimAlertRemarks.setData(fvrDetailedViewDTO.getFvrClaimAlertRemarks());
		fvrClaimAlertRemarks.setId("FvrGradedRemarks");
	    handleRemarksPopup(fvrClaimAlertRemarks, null);
	    fvrClaimAlertRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		
		final List<FvrTriggerPoint> fvrTriggerPoints = reimbursementService.getFVRTriggerPoints(viewFVRDTO.getKey());
		
		Table table = new Table();
		table.addContainerProperty("sno", String.class, null);
		table.addContainerProperty("description",  String.class, null);
		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("description", "Description");
		table.setColumnWidth("sno", 70);
		table.setColumnWidth("description", 500);
		table.setWidth("575px");
		table.setCaption("FVR Trigger Point");
		
		if(fvrTriggerPoints != null && !fvrTriggerPoints.isEmpty()) {
			for(int i = 0; i < fvrTriggerPoints.size(); i++) {
				if(i != fvrTriggerPoints.size()){
				table.addItem(new Object[]{"" + (i+1), fvrTriggerPoints.get(i).getRemarks()}, i+1);
				table.setItemDescriptionGenerator(new ItemDescriptionGenerator() {                             
					@Override
					public String generateDescription(Component source,
							Object itemId, Object propertyId) {
						 if(propertyId != null && propertyId.equals("description")) {
						        return fvrTriggerPoints.get((int)itemId-1).getRemarks();
						  }
						 return null;
					  }
					});
				
			}
		}
		}
		table.setPageLength(4);
		
		FormLayout firstForm = new FormLayout(initiateFVR,fvrInitiatedBy,fvrInitiatedDateTime,fvrClaimAlertRemarks);
		firstForm.setSpacing(true);
		firstForm.setMargin(true);
		
		FormLayout secondForm = new FormLayout(allocationTo,fvrPriority,table);
		secondForm.setSpacing(true);
		secondForm.setMargin(true);
		
		HorizontalLayout mainHor = new HorizontalLayout(firstForm,secondForm);
		mainHor.setSpacing(true);
		mainHor.setMargin(true);
		
		FormLayout thirdForm = new FormLayout(hospitalName);
		thirdForm.setSpacing(true);
		thirdForm.setMargin(true);
		
		VerticalLayout verticalLayout = new VerticalLayout(thirdForm,mainHor);
		
		Panel mainPanel = new Panel(verticalLayout);
	    mainPanel.addStyleName("girdBorder");
	    return mainPanel;
	}

	private Panel fvrNotRequiredPanel() {
		
		initiateFVR = new OptionGroup("Initiate FVR");
	    initiateFVR.addItem(SHAConstants.YES);
	    initiateFVR.addItem(SHAConstants.No);
	    initiateFVR.addStyleName("horizontal");
	    initiateFVR.select(SHAConstants.No);
	    initiateFVR.setReadOnly(true);
	    
	    fvrNotRequiredDetailsTable.init("", false, false);
	    fvrNotRequiredDetailsTable.setWidth("100%");
	    
		if(viewFVRDTO.getFvrNotRequiredKey() != null){
			  List<FvrNotRequired> fieldRequestNotRemarksList = fieldVisitRequestService.getFieldRequestNotRemarksList(viewFVRDTO.getFvrNotRequiredKey());
			    List<FvrDetailedViewDTO> fvrNotRemarksList = new ArrayList<FvrDetailedViewDTO>();
			    if (fieldRequestNotRemarksList != null && !fieldRequestNotRemarksList.isEmpty()){
			    	for (FvrNotRequired fvrNotRequired : fieldRequestNotRemarksList) {
						FvrDetailedViewDTO bean = new FvrDetailedViewDTO();
						bean.setFvrRemarksNotRequiredBy(fvrNotRequired.getCreatedBy());
						bean.setFvrNotRequiredUpdatedDateAndTime(fvrNotRequired.getCreatedDate());
						bean.setFvrNotRequiredRemarks(fvrNotRequired.getFvrLovRemarks());
						bean.setClaimStage(fvrNotRequired.getFvrNotRequiredStage());
						fvrNotRemarksList.add(bean);
					}
				    fvrNotRequiredDetailsTable.setTableList(fvrNotRemarksList);
			    }
		}else if(fieldVisitByKey != null && fieldVisitByKey.getStatus().getKey().equals(ReferenceTable.FVR_NOT_REQUIRED)){
			FvrDetailedViewDTO bean = new FvrDetailedViewDTO();
			bean.setFvrRemarksNotRequiredBy(fieldVisitByKey.getCreatedBy());
			bean.setFvrNotRequiredUpdatedDateAndTime(fieldVisitByKey.getCreatedDate());
			bean.setFvrNotRequiredRemarks(fieldVisitByKey.getExecutiveComments());
			bean.setClaimStage(fieldVisitByKey.getStage().getStageName());
			fvrNotRequiredDetailsTable.addBeanToList(bean);
		}
	  
	    
		VerticalLayout verticalLayout = new VerticalLayout(initiateFVR,fvrNotRequiredDetailsTable);
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		
		HorizontalLayout mainHor = new HorizontalLayout(verticalLayout);
		mainHor.setSpacing(true);
		mainHor.setMargin(true);
		mainHor.setWidth("1200px");
		
		Panel mainPanel = new Panel(mainHor);
	    mainPanel.addStyleName("girdBorder");
	    mainPanel.setCaption("FVR Not Required Details");
	    return mainPanel;
	}

	private Panel fvrCancelPanel() {
		
		fvrCancelledBy = (TextField) binder.buildAndBind("FVR Cancelled by","fvrCancelledBy",TextField.class);
		fvrCancelledBy.setReadOnly(true);
		fvrCancelledBy.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fvrCancelledBy.setNullRepresentation("-");
		
		fvrStatus = (TextField) binder.buildAndBind("FVR Status","fvrStatus",TextField.class);
		fvrStatus.setReadOnly(true);
		fvrStatus.setWidth("500px");
		fvrStatus.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fvrStatus.setNullRepresentation("-");
		fvrStatus.setDescription(fvrDetailedViewDTO.getFvrStatus());
		
	    fvrCancelledDateTime = (TextField) binder.buildAndBind("FVR Cancelled Date & Time","fvrCancelledDateTime",TextField.class);
	    fvrCancelledDateTime.setReadOnly(true);
	    fvrCancelledDateTime.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	    fvrCancelledDateTime.setNullRepresentation("-");
		
	    fvrClosedRemarks = (TextArea) binder.buildAndBind("FVR Closed Remarks","fvrClosedRemarks",TextArea.class);
	    fvrClosedRemarks.setReadOnly(true);
	    fvrClosedRemarks.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
	    fvrClosedRemarks.setNullRepresentation("-");
	    fvrClosedRemarks.setData(fvrDetailedViewDTO.getFvrClosedRemarks());
	    fvrClosedRemarks.setId("FvrClosedRemarks");
	    handleRemarksPopup(fvrClosedRemarks, null);
	    fvrClosedRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		
		fvrExecutiveClosedReason = (TextArea) binder.buildAndBind("FVR Executive Closed Reason","fvrExecutiveClosedReason",TextArea.class);
		fvrExecutiveClosedReason.setReadOnly(true);
		fvrExecutiveClosedReason.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fvrExecutiveClosedReason.setNullRepresentation("-");
		fvrExecutiveClosedReason.setData(fvrDetailedViewDTO.getFvrExecutiveClosedReason());
		fvrExecutiveClosedReason.setId("FvrExecutiveClosedReason");
	    handleRemarksPopup(fvrExecutiveClosedReason, null);
		fvrExecutiveClosedReason.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		
		
		FormLayout firstForm = new FormLayout(fvrCancelledBy,fvrCancelledDateTime,fvrExecutiveClosedReason);
		firstForm.setSpacing(true);
		firstForm.setMargin(true);
		
		FormLayout secondForm = new FormLayout(fvrStatus,fvrClosedRemarks);
		secondForm.setSpacing(true);
		secondForm.setMargin(true);
		
		HorizontalLayout mainHor = new HorizontalLayout(firstForm,secondForm);
		mainHor.setSpacing(true);
		mainHor.setMargin(true);
		
		Panel mainPanel = new Panel(mainHor);
	    mainPanel.addStyleName("girdBorder");
	    mainPanel.setCaption("FVR Cancelled/Closed");
	    return mainPanel;
	}

	private Panel fvrAssignPanel() {
		
		fvrAssignedBy = (TextField) binder.buildAndBind("FVR Assigned by","fvrAssignedBy",TextField.class);
		fvrAssignedBy.setReadOnly(true);
		fvrAssignedBy.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fvrAssignedBy.setNullRepresentation("-");
		
		fvrAssignedDateTime = (TextField) binder.buildAndBind("FVR Assigned Date & Time","fvrAssignedDateTime",TextField.class);
		fvrAssignedDateTime.setReadOnly(true);
		fvrAssignedDateTime.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fvrAssignedDateTime.setNullRepresentation("-");
		
		repCode = (TextField) binder.buildAndBind("Representative Code","repCode",TextField.class);
		repCode.setReadOnly(true);
		repCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		repCode.setNullRepresentation("-");
		
		fvrRemarks = (TextArea) binder.buildAndBind("FVR Remarks","fvrRemarks",TextArea.class);
		fvrRemarks.setReadOnly(false);
		fvrRemarks.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		fvrRemarks.setNullRepresentation("-");
		fvrRemarks.setData(fvrDetailedViewDTO.getFvrRemarks());
		fvrRemarks.setId("FvrRemarks");
		fvrRemarks.setValue(fvrDetailedViewDTO.getFvrAssignedDateTime() != null ? fvrDetailedViewDTO.getFvrRemarks() : "-");
		fvrRemarks.setReadOnly(true);
	    handleRemarksPopup(fvrRemarks, null);
		fvrRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		
		/*repName = (TextField) binder.buildAndBind("Representative Name","repName",TextField.class);
		repName.setReadOnly(true);
		repName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		repName.setNullRepresentation("-");
		
		repContactNo = (TextField) binder.buildAndBind("Representative Contact No","repContactNo",TextField.class);
		repContactNo.setReadOnly(true);
		repContactNo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		repContactNo.setNullRepresentation("-");*/
		
		FormLayout firstForm = new FormLayout(fvrAssignedBy,repCode/*,repName,repContactNo*/);
		firstForm.setSpacing(true);
		firstForm.setMargin(true);
		
		FormLayout secondForm = new FormLayout(fvrAssignedDateTime,fvrRemarks);
		secondForm.setSpacing(true);
		secondForm.setMargin(true);
		
		HorizontalLayout mainHor = new HorizontalLayout(firstForm,secondForm);
		mainHor.setSpacing(true);
		mainHor.setMargin(true);
		
		Panel mainPanel = new Panel(mainHor);
	    mainPanel.addStyleName("girdBorder");
	    mainPanel.setCaption("Assigned/Re-assign FVR");
	    return mainPanel;
	}

	private Panel fvrReplyReceivedPanel() {
		
		fvrReplyReceivedFrom = (TextField) binder.buildAndBind("FVR Reply Received From","fvrReplyReceivedFrom",TextField.class);
		fvrReplyReceivedFrom.setReadOnly(true);
		fvrReplyReceivedFrom.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fvrReplyReceivedFrom.setNullRepresentation("-");
		
		hospitalVisitedDate = (TextField) binder.buildAndBind("Hospital Visited Date","hospitalVisitedDate",TextField.class);
		hospitalVisitedDate.setReadOnly(true);
		hospitalVisitedDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		hospitalVisitedDate.setNullRepresentation("-");
		
		fvrReplyReceivedDateTime = (TextField) binder.buildAndBind("FVR Reply Received Date & Time","fvrReplyReceivedDateTime",TextField.class);
		fvrReplyReceivedDateTime.setReadOnly(true);
		fvrReplyReceivedDateTime.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fvrReplyReceivedDateTime.setNullRepresentation("-");
		
		FormLayout firstForm = new FormLayout(fvrReplyReceivedFrom,fvrReplyReceivedDateTime);
		firstForm.setSpacing(true);
		firstForm.setMargin(true);
		
		FormLayout secondForm = new FormLayout(hospitalVisitedDate);
		secondForm.setSpacing(true);
		secondForm.setMargin(true);
		
		HorizontalLayout mainHor = new HorizontalLayout(firstForm,secondForm);
		mainHor.setSpacing(true);
		mainHor.setMargin(true);
		
		Button viewFVRDocuments = new Button("View FVR Documents");
		VerticalLayout btnVerticalLayout = new VerticalLayout();
		btnVerticalLayout.addComponent(viewFVRDocuments);
		btnVerticalLayout.setComponentAlignment(viewFVRDocuments, Alignment.TOP_RIGHT);
		btnVerticalLayout.addComponent(mainHor);
		
		viewFVRDocuments.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try{
				List<FieldVisitRequest> fieldVisitRequestByKey = fieldVisitRequestService.getFieldVisitRequestByKey(viewFVRDTO.getKey());
				String intimationNo = null;
				if(fieldVisitRequestByKey != null && ! fieldVisitRequestByKey.isEmpty()){
					FieldVisitRequest fieldVisitRequest = fieldVisitRequestByKey.get(0);
					if(fieldVisitRequest != null && fieldVisitRequest.getIntimation() != null
							&& fieldVisitRequest.getIntimation().getIntimationId() != null){
						intimationNo = fieldVisitRequest.getIntimation().getIntimationId();
					}
				}
				
				    if(intimationNo != null){
					  viewUploadedDocumentDetails(intimationNo);
				    }
				}catch(Exception e){
					Log.info("view document exception for FVR");
				}
			}
		});
		
		Panel mainPanel = new Panel(btnVerticalLayout);
	    mainPanel.addStyleName("girdBorder");
	    mainPanel.setCaption("FVR Reply Received");
	    return mainPanel;
	}

	private Panel fvrGradingPanel() {
		
		fvrGradedBy = (TextField) binder.buildAndBind("FVR Graded by","fvrGradedBy",TextField.class);
		fvrGradedBy.setReadOnly(true);
		fvrGradedBy.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fvrGradedBy.setNullRepresentation("-");
		
		fvrGradedRemarks = (TextArea) binder.buildAndBind("FVR Graded Remarks","fvrGradedRemarks",TextArea.class);
		fvrGradedRemarks.setReadOnly(true);
		fvrGradedRemarks.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		fvrGradedRemarks.setNullRepresentation("-");
		fvrGradedRemarks.setData(fvrDetailedViewDTO.getFvrGradedRemarks());
		fvrGradedRemarks.setId("FvrGradedRemarks");
	    handleRemarksPopup(fvrGradedRemarks, null);
		fvrGradedRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		
		fvrGradedDateTime = (TextField) binder.buildAndBind("FVR Graded Date & Time","fvrGradedDateTime",TextField.class);
		fvrGradedDateTime.setReadOnly(true);
		fvrGradedDateTime.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fvrGradedDateTime.setNullRepresentation("-");
		
		FormLayout firstForm = new FormLayout(fvrGradedBy,fvrGradedDateTime);
		firstForm.setSpacing(true);
		firstForm.setMargin(true);
		
		FormLayout secondForm = new FormLayout(fvrGradedRemarks);
		secondForm.setSpacing(true);
		secondForm.setMargin(true);
		
		HorizontalLayout mainHor = new HorizontalLayout(firstForm,secondForm);
		mainHor.setSpacing(true);
		mainHor.setMargin(true);
		
		Button viewGradingDetails = new Button("View Grading Details");
		VerticalLayout btnVerticalLayout = new VerticalLayout();
		btnVerticalLayout.addComponent(viewGradingDetails);
		btnVerticalLayout.setComponentAlignment(viewGradingDetails, Alignment.TOP_RIGHT);
		btnVerticalLayout.addComponent(mainHor);
		
		viewGradingDetails.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				popup = new com.vaadin.ui.Window();
				viewFVRGradingUI.init(viewFVRDTO.getKey(), instance, viewFVRDTO);											
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("75%");
				popup.setContent(viewFVRGradingUI);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.addCloseListener(new Window.CloseListener() { 
					
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(
							CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);

			}
		});
		
		Panel mainPanel = new Panel(btnVerticalLayout);
	    mainPanel.addStyleName("girdBorder");
	    mainPanel.setCaption("FVR Grading Details");
	    return mainPanel;
	}
	
	public void viewUploadedDocumentDetails(String intimationNo) {

		DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService
				.getDocumentDetailsData(intimationNo, 0);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
		}

		popup = new com.vaadin.ui.Window();

		dmsDocumentDetailsViewPage.init(dmsDTO, popup);
		dmsDocumentDetailsViewPage.getContent();

		popup.setCaption("View Uploaded Documents");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(dmsDocumentDetailsViewPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
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
	
	public void closePopup(){
		if (popup != null) {
			popup.close();
		}
	}
	
	public  void handleRemarksPopup(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForRedraft(searchField, getShortCutListenerForRedraftRemarks(searchField));
	    
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
	private ShortcutListener getShortCutListenerForRedraftRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				String remarksValue = (String) txtFld.getData();
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
				txtArea.setReadOnly(true);
				txtArea.setHeight("30%");
				
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				final Window dialog = new Window();
				
				String strCaption = "";

				if(("FvrClosedRemarks").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "FVR Closed Remarks";
				}
				if(("FvrExecutiveClosedReason").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "FVR Executive Closed Reason";
				}
				if(("FvrRemarks").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "FVR Remarks";
				}
				if(("FvrGradedRemarks").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "FVR Graded Remarks";
				}
				
				dialog.setCaption(strCaption);
				
				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
//				dialog.setHeight("75%");
//		    	dialog.setWidth("65%");
				dialog.setClosable(true);
				
				dialog.setContent(vLayout);
//				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					
					@Override
					public void windowClose(CloseEvent e) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getRedraftRemarks());
						dialog.close();
					}
				});
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getRedraftRemarks());
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}

}
