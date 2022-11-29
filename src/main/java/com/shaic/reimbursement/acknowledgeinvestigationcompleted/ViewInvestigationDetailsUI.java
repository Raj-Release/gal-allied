package com.shaic.reimbursement.acknowledgeinvestigationcompleted;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.userproduct.document.UserProductMappingService;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationDetails;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasRoleLimit;
import com.shaic.domain.MasUserLimitMapping;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.assigninvesigation.AssignMutiInvestigatorTableView;
import com.shaic.reimbursement.draftinvesigation.DraftTriggerPointsToFocusDetailsTableDto;
import com.shaic.reimbursement.draftinvesigation.InvestigatorTriggerPointsTable;
import com.shaic.reimbursement.reassigninvestigation.InvReassignHistoryTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ViewInvestigationDetailsUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClaimService claimService;
	
	@EJB
	private CreateRODService billDetailsService;

	@EJB
	private FieldVisitRequestService fieldVisitRequestService; 
	
	private InvestigationService investigationService;

	private  MasterService masterService;
	
	@EJB
	private  ReimbursementService reimbursementService;
	
	private HospitalService hospitalService;
	
	private DMSDocumentViewDetailsPage dmsDocDetailsViewPage;
	
	private TextField requestedRole;
	private TextField requestorIdName;
	private TextField requestedDateTime;
	private ComboBox invAllocationTocmb;
	private ComboBox reasonforIniInvesCmb;
	private TextArea reasonForReferingTxta;
	private TextArea triggerPointsToFocusTxta;
	private TextField invApproverIdName;
	private TextField processedDateTime;
	private TextField outCome;
	private TextArea invApprovedRemarksTxta;
	private TextArea invdisapprovedRemarksTxta;
	private OptionGroup initateFVROpt;
	private ComboBox fvrAllocationCmb;
	private ComboBox fvrPriorityCmb;
	private TextArea fvrTrigPointsTxta;
	private TextField draftPersonIdName;
	private TextField draftprocessedDateTime;
	private ComboBox draftInvAllocationToCmb;
	private TextArea claimBackgroundTxta;
	private TextArea factsOfCaseTxta;
	private TextField invAssignedByIdName;
	private TextField invAssignedDateTime;
	private TextField uploadedByIdName;
	private TextField uploadedDateTime;
	private TextField fileName;
	private TextField fileType;
	private TextField dateOfCompletion;
	private TextArea invCompletionRemarksTxta;
	private TextField invGradedByIdName;
	private TextField invGradedDateTime;
	private ComboBox invGradedCategCmb;
	private TextArea invGradingRemarksTxta;
	private TmpEmployee empObj;
	private StageInformation stageObj;
	
	@Inject
	private AssignMutiInvestigatorTableView viewassignedTable;
	
	@Inject
	private InvReassignHistoryTable invReassignHistoryTable;
	
	@Inject
	private InvestigatorTriggerPointsTable investigationTriggerPointTable;
	
	private VerticalLayout mainVLayout;

	private HorizontalLayout wholeLayout;
	
	private Investigation investigation;
	
	private AssignedInvestigatiorDetails assignedInvestigationDetailsObj;
	
	@EJB
	private UserProductMappingService userMappingService;
	
	@EJB
	private DBCalculationService dbCalService;
	
	public void initView(Long investigationAssignedKey,Long investigationKey){
		InvestigationDetailsReimbursementTableDTO invDetailsTableDto = new InvestigationDetailsReimbursementTableDTO();
		invDetailsTableDto.setInvestigationAssignedKey(investigationAssignedKey);
		invDetailsTableDto.setKey(investigationKey);
		
		if (invDetailsTableDto.getKey() != null) {
			investigation = investigationService
					.getByInvestigationKey(invDetailsTableDto.getKey());
		}
		
		if (invDetailsTableDto.getInvestigationAssignedKey() != null) {
			assignedInvestigationDetailsObj = invDetailsTableDto.getInvestigationAssignedKey() != null ? investigationService.getAssignedInvestigByKey(invDetailsTableDto.getInvestigationAssignedKey()) : null;
		
			if(assignedInvestigationDetailsObj != null && assignedInvestigationDetailsObj.getInvestigation() != null)
				investigation = investigationService.getByInvestigationKey(assignedInvestigationDetailsObj.getInvestigation().getKey());
		}
		else{
			assignedInvestigationDetailsObj = null;
		}
			
		
		init(invDetailsTableDto);
	}
	
	public void setServiceObjects(HospitalService hospitalService,ClaimService claimService,InvestigationService investigationService,MasterService masterService){
		
		this.hospitalService = hospitalService;
		this.claimService = claimService;
		this.investigationService = investigationService;
		this.masterService = masterService;
	}
	
	public void setViewInvestigationDoc(DMSDocumentViewDetailsPage  dmsDocDetailsViewPage){
		this.dmsDocDetailsViewPage = dmsDocDetailsViewPage;
	}

	public void init(InvestigationDetailsReimbursementTableDTO invDetailsTableDto) {
		
		mainVLayout = new VerticalLayout();

		mainVLayout.setSpacing(true);
		
//		setCompositionRoot(mainVLayout);
		
			BeanItemContainer<SelectValue> invAllocationContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
			if (investigation != null) {
				
				stageObj = null;
				empObj = null;	
				
				MastersValue invAllocationTo =  investigation.getAllocationTo();
				invAllocationContainer.addBean(new SelectValue(invAllocationTo.getKey(), invAllocationTo.getValue()));

				Panel initiateInvPanel = new Panel();
				initiateInvPanel.addStyleName("girdBorder");
//				initiateInvPanel.setWidth("100%");
				
				requestedRole = new TextField("Requested Role");
				/*requestedRole.setValue(investigation.getCreatedBy() != null ? investigation.getCreatedBy() : "");
				requestedRole.setReadOnly(true);
				
				requestorIdName = new TextField("Requestor ID / Name");
				requestorIdName.setValue(investigation.getCreatedBy() != null ? investigation.getCreatedBy() : "");
				requestorIdName.setReadOnly(true);*/
				MasUserLimitMapping userRole = userMappingService.getRoleDetails(investigation.getCreatedBy());
				if(userRole!=null){
					MasRoleLimit emprole = userMappingService.getAmountDetails(userRole.getRoleId());
					if(emprole!=null){
						requestedRole.setValue(emprole.getRoleType() != null ? emprole.getRoleType() : "");
						requestedRole.setReadOnly(true);
					}
				}
				TmpEmployee employeeObj = masterService.getEmployeeNameWithInactiveUser(investigation.getCreatedBy());
				
				requestorIdName = new TextField("Requestor ID / Name");
				requestorIdName.setValue(employeeObj != null ? (employeeObj.getLoginId()+"-"+employeeObj.getEmpFirstName()) : "");
				requestorIdName.setReadOnly(true);
				
				requestedDateTime = new TextField("Requested Date & Time");
				requestedDateTime.setValue(investigation.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm aa").format(new Timestamp(investigation.getCreatedDate().getTime())) : "");
				requestedDateTime.setReadOnly(true);
				
				String invTrigPoints = "";
				
				invTrigPoints = investigation.getTriggerPoints() != null ?  investigation.getTriggerPoints() : "";
				String invBypassAllowed = dbCalService.bypassInvestigationAllowed(investigation.getClaim().getIntimation().getPolicy().getPolicyNumber());
				boolean isDirectToAssignInv = (SHAConstants.YES_FLAG).equalsIgnoreCase(invBypassAllowed) ? true : false;
				
				if(isDirectToAssignInv || (null != investigation.getTransactionFlag() && SHAConstants.CASHLESS_CHAR.equalsIgnoreCase(investigation.getTransactionFlag()))){
					List<InvestigationDetails> cashlessInvsTriggerPoints = investigationService.getCashlessInvsTriggerPoints(investigation.getKey());
					StringBuffer triggerPoints = new StringBuffer();
					if(null != cashlessInvsTriggerPoints && !cashlessInvsTriggerPoints.isEmpty()){
						for (InvestigationDetails investigationDetails : cashlessInvsTriggerPoints) {
							if(null != investigationDetails.getDraftOrReDraftRemarks()){
								invTrigPoints = triggerPoints.append(investigationDetails.getDraftOrReDraftRemarks()).append(",").toString();
							}
						}
					}
				}
				triggerPointsToFocusTxta = new TextArea("Trigger Points To Focus");			
				triggerPointsToFocusTxta.setValue(invTrigPoints);
				triggerPointsToFocusTxta.setReadOnly(true);
				triggerPointsToFocusTxta.setWidth("500px");
				triggerPointsToFocusTxta.setData(invTrigPoints);
				triggerPointsToFocusTxta.setId("TrigPointsFocus");
				handleRemarksPopup(triggerPointsToFocusTxta, null);
				triggerPointsToFocusTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
				

				FormLayout initiateInvFrm1 = new FormLayout(requestedRole,requestorIdName,requestedDateTime,triggerPointsToFocusTxta);
				
				invAllocationTocmb = new ComboBox("Allocation To");
				invAllocationTocmb.setContainerDataSource(invAllocationContainer);
				invAllocationTocmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				invAllocationTocmb.setItemCaptionPropertyId("value");
				invAllocationTocmb.setValue(invAllocationContainer.size() > 0 ? invAllocationContainer.getIdByIndex(0): null);
				invAllocationTocmb.setEnabled(false);
				
				BeanItemContainer<SelectValue> reasonforIniInvContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
				MastersValue reasonforIniInv =  investigation.getReasonForInitiatingInv();
				reasonforIniInvContainer.addBean(new SelectValue(reasonforIniInv.getKey(), reasonforIniInv.getValue()));
				
				reasonforIniInvesCmb = new ComboBox("Reason for Initiating Investigation");
				reasonforIniInvesCmb.setContainerDataSource(reasonforIniInvContainer);
				reasonforIniInvesCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				reasonforIniInvesCmb.setItemCaptionPropertyId("value");
				reasonforIniInvesCmb.setValue(reasonforIniInvContainer.size() > 0 ? reasonforIniInvContainer.getIdByIndex(0): null);
				reasonforIniInvesCmb.setEnabled(false);
				
				
				reasonForReferingTxta = new TextArea("Reason For Refering");
				if(investigation.getReasonForReferring()!=null){
				reasonForReferingTxta.setValue(investigation.getReasonForReferring());
				}else{
					reasonForReferingTxta.setValue("");
				}
				reasonForReferingTxta.setReadOnly(true);
				reasonForReferingTxta.setWidth("448px");
				if(investigation.getReasonForReferring()!=null){
				reasonForReferingTxta.setData(investigation.getReasonForReferring());
				}else{
					reasonForReferingTxta.setData("");
				}
				reasonForReferingTxta.setId("invRefReason");
				handleRemarksPopup(reasonForReferingTxta, null);
				reasonForReferingTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
				
				FormLayout initiateInvFrm2 = new FormLayout(invAllocationTocmb,reasonforIniInvesCmb,reasonForReferingTxta);
				
				HorizontalLayout initiateInvLayout = new HorizontalLayout(initiateInvFrm1,initiateInvFrm2);
				initiateInvLayout.setSpacing(true);
				initiateInvLayout.setWidth("100%");
				initiateInvPanel.setCaption("Initiate Investigation");
				initiateInvPanel.setContent(initiateInvLayout);
				
				Panel processInvInitiatePanel = new Panel("Process Investigation Initiated");
				processInvInitiatePanel.addStyleName("girdBorder");
//				processInvInitiatePanel.setWidth("100%");
				
				Long statusKey = investigation.getStatus() != null && ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigation.getStatus().getKey()) ? ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED : ReferenceTable.INITIATE_INVESTIGATION_APPROVED ;
				if(investigation != null && investigation.getTransactionKey() != null){
					stageObj = investigationService.getStageInfoByStatusNStage(ReferenceTable.INVESTIGATION_STAGE,statusKey,investigation.getTransactionKey());
					empObj = stageObj != null ? masterService.getEmployeeName(stageObj.getCreatedBy()) : null;
				}				
				else if(investigation.getClaim() != null){
					stageObj = investigationService.getStageInfoByClaimStatusNStage(ReferenceTable.INVESTIGATION_STAGE,statusKey,investigation.getClaim().getKey());
					empObj = stageObj != null ? masterService.getEmployeeName(stageObj.getCreatedBy()) : null;
				}
				else{
					empObj = null;
				}
				
				invApproverIdName = new TextField("Investigation Approver ID / Name");
				invApproverIdName.setValue((stageObj != null && stageObj.getCreatedBy() != null ? stageObj.getCreatedBy() : "")
						+ (empObj != null && empObj.getEmpFirstName() != null && !empObj.getEmpFirstName().isEmpty() ? (" - " + empObj.getEmpFirstName()) : ""));
				invApproverIdName.setReadOnly(true);
				
				processedDateTime = new TextField("Processed Date & Time");
				processedDateTime.setValue(stageObj != null ? (stageObj.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm aa").format(new Timestamp(stageObj.getCreatedDate().getTime())) : "") : "");
				processedDateTime.setReadOnly(true);
				
				outCome = new TextField("Outcome");
				
				String outcomeValue = "";
				if(stageObj != null && stageObj.getCreatedBy() != null){
					outcomeValue = investigation.getStatus() != null && ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigation.getStatus().getKey()) ? "Investigation Disapproved" : "Investigation Approved";
				}
				outCome.setValue(outcomeValue);
				outCome.setReadOnly(true);
				
				invApprovedRemarksTxta = new TextArea("Investigation Approved Remarks");
				String invApproveRemarks = !ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigation.getStatus().getKey()) && investigation.getRemarks() != null ? investigation.getRemarks() : "";
				invApprovedRemarksTxta.setValue(invApproveRemarks);
				invApprovedRemarksTxta.setReadOnly(true);
				invApprovedRemarksTxta.setWidth("385px");
				invApprovedRemarksTxta.setData(invApproveRemarks);
				invApprovedRemarksTxta.setId("invApproveRemarks");
				handleRemarksPopup(invApprovedRemarksTxta, null);
				invApprovedRemarksTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
				
				FormLayout processInvInitiateFrm1 = new FormLayout(invApproverIdName,processedDateTime,outCome);
				processInvInitiateFrm1.setWidth("100%");
				
				
				FormLayout processInvInitiateFrm2 = new FormLayout();
				processInvInitiateFrm2.setWidth("100%");
				
				String invdisapprovedRemarks =  ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigation.getStatus().getKey()) ? investigation.getRemarks() : null;

				if(invdisapprovedRemarks != null){
					invdisapprovedRemarksTxta = new TextArea("Investigation Disapproved Remarks");
					invdisapprovedRemarksTxta.setValue(investigation.getRemarks());
					invdisapprovedRemarksTxta.setReadOnly(true);
					invdisapprovedRemarksTxta.setWidth("385px");
					invdisapprovedRemarksTxta.setData(investigation.getRemarks());
					invdisapprovedRemarksTxta.setId("invdisappruRemarks");
					handleRemarksPopup(invdisapprovedRemarksTxta, null);
					invdisapprovedRemarksTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
					
					processInvInitiateFrm2.addComponent(invdisapprovedRemarksTxta);
					FieldVisitRequest fvrObj = null;
					if(investigation.getTransactionKey() == null && investigation.getClaim() != null){
						List<FieldVisitRequest> fvrListObj =	fieldVisitRequestService.getFieldVisitByClaimKey(investigation.getClaim().getKey());
						fvrObj = fvrListObj != null && !fvrListObj.isEmpty() ? fvrListObj.get(fvrListObj.size()-1) : null;  
					}
					if(investigation.getTransactionKey() != null){
						
						Reimbursement reimbObj = reimbursementService.getReimbursementByKey(investigation.getTransactionKey());
						if(reimbObj != null){
		
							fvrObj =	fieldVisitRequestService.getFieldVisitByReimbursementKey(reimbObj.getKey());
						}
					}
						if(fvrObj != null){
							
							initateFVROpt = new OptionGroup("Initate FVR");
							initateFVROpt.addItem("Yes");
							initateFVROpt.addItem("No");
							initateFVROpt.select("Yes");
							initateFVROpt.setStyleName("inlineStyle");
							initateFVROpt.setEnabled(false);
							
							fvrAllocationCmb = new ComboBox("Allocation To");
							BeanItemContainer<SelectValue> fvrAllocationContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
							SelectValue fvrAllocationSelectValue = new SelectValue(1l, fvrObj.getAllocationTo() != null ? fvrObj.getAllocationTo().getValue() : "");
							fvrAllocationContainer.addBean(fvrAllocationSelectValue);
							fvrAllocationCmb.setContainerDataSource(fvrAllocationContainer);
							fvrAllocationCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
							fvrAllocationCmb.setItemCaptionPropertyId("value");
							fvrAllocationCmb.setValue(fvrAllocationContainer.size() > 0 ? fvrAllocationContainer.getIdByIndex(0): null);
							fvrAllocationCmb.setEnabled(false);
							
						  /*fvrAssignToCmb =  new ComboBox("Assign To");
							BeanItemContainer<SelectValue> fvrAssignContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
							SelectValue fvrAssignSelectValue = new SelectValue(1l, fvrObj.getAssignTo() != null ? fvrObj.getAssignTo().getValue() : "");
							fvrAssignContainer.addBean(fvrAssignSelectValue);
							fvrAssignToCmb.setContainerDataSource(fvrAssignContainer);
							fvrAssignToCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
							fvrAssignToCmb.setItemCaptionPropertyId("value");
							fvrAssignToCmb.setValue(fvrAssignContainer.size() > 0 ? fvrAssignContainer.getIdByIndex(0): null);
							fvrAssignToCmb.setEnabled(false);*/
							
							fvrPriorityCmb = new ComboBox("Priority");
							BeanItemContainer<SelectValue> fvrPriorityContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
							SelectValue fvrPrioritySelectValue = new SelectValue(1l, fvrObj.getPriority() != null ? fvrObj.getPriority().getValue() : "");
							fvrPriorityContainer.addBean(fvrPrioritySelectValue);
							fvrPriorityCmb.setContainerDataSource(fvrPriorityContainer);
							fvrPriorityCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
							fvrPriorityCmb.setItemCaptionPropertyId("value");
							fvrPriorityCmb.setValue(fvrPriorityContainer.size() > 0 ? fvrPriorityContainer.getIdByIndex(0): null);
							fvrPriorityCmb.setEnabled(false);
							
							fvrTrigPointsTxta = new TextArea("FVR Trigger Points");
							fvrTrigPointsTxta.setValue(fvrObj.getFvrTriggerPoints() != null ? fvrObj.getFvrTriggerPoints() : "");
							fvrTrigPointsTxta.setReadOnly(true);
							fvrTrigPointsTxta.setWidth("385px");
							fvrTrigPointsTxta.setData(fvrObj.getFvrTriggerPoints() != null ? fvrObj.getFvrTriggerPoints() : "");
							fvrTrigPointsTxta.setId("fvrTrigPts");
							handleRemarksPopup(fvrTrigPointsTxta, null);
							fvrTrigPointsTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
							
							processInvInitiateFrm2.addComponents(initateFVROpt,fvrAllocationCmb,/*fvrAssignToCmb,*/fvrPriorityCmb,fvrTrigPointsTxta);
						}	
						
					
				}
				else{
					processInvInitiateFrm2.addComponent(invApprovedRemarksTxta);	
				}
				HorizontalLayout processInvInitiateLayout = new HorizontalLayout();
				processInvInitiateLayout.setWidth("100%");
				processInvInitiateLayout.addComponents(processInvInitiateFrm1,processInvInitiateFrm2);
				
				processInvInitiatePanel.setContent(processInvInitiateLayout);
				
				
				Panel draftInvLetterPanel = new Panel("Draft Investigation Letter");  //1frm
				draftInvLetterPanel.addStyleName("girdBorder");
				draftInvLetterPanel.setWidth("100%");
				
				Panel assignInvPanel = new Panel("Assign Investigation");
				assignInvPanel.addStyleName("girdBorder");
//				assignInvPanel.setWidth("100%");
				
				Panel uploadInvReportPanel = new Panel("Upload Investigation Report");
				uploadInvReportPanel.addStyleName("girdBorder");
//				uploadInvReportPanel.setWidth("100%");
				

				Panel invGradingPanel = new Panel("Investigation Grading");
				invGradingPanel.addStyleName("girdBorder");
//				invGradingPanel.setWidth("100%");
			
											
					stageObj = null;
					empObj = null;
						
					if(!ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigation.getStatus().getKey())){
								if(investigation.getTransactionKey() != null){
									stageObj = investigationService.getStageInfoByStatusNStage(ReferenceTable.INVESTIGATION_STAGE,ReferenceTable.DRAFT_INVESTIGATION,investigation.getTransactionKey());
									empObj = stageObj != null ? masterService.getEmployeeName(stageObj.getCreatedBy()) : null;
								}				
								else{
									empObj = null;
								}
						if(stageObj != null){
							draftPersonIdName = new TextField("DraftPerson Id / Name");
							draftPersonIdName.setValue((stageObj != null && stageObj.getCreatedBy() != null ? stageObj.getCreatedBy() : "")
									+ (empObj != null && empObj.getEmpFirstName() != null && !empObj.getEmpFirstName().isEmpty() ? (" - " + empObj.getEmpFirstName()) : ""));
							
							draftprocessedDateTime = new TextField("Processed Date & Time");
							draftprocessedDateTime.setValue(stageObj != null ? (stageObj.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm aa").format(new Timestamp(stageObj.getCreatedDate().getTime())) : "") : "");
							draftprocessedDateTime.setReadOnly(true);
							
							draftInvAllocationToCmb = new ComboBox("Allocation To");
							draftInvAllocationToCmb.setContainerDataSource(invAllocationContainer);
							draftInvAllocationToCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
							draftInvAllocationToCmb.setItemCaptionPropertyId("value");
							draftInvAllocationToCmb.setValue(stageObj != null && stageObj.getCreatedBy() != null  ? invAllocationContainer.getIdByIndex(0) : null);
							draftInvAllocationToCmb.setEnabled(false);
							
							claimBackgroundTxta = new TextArea("Claim Background");
							claimBackgroundTxta.setValue(investigation.getClaimBackgroundDetails() != null ? investigation.getClaimBackgroundDetails() : "");
							claimBackgroundTxta.setReadOnly(true);
							claimBackgroundTxta.setWidth("500px");
							claimBackgroundTxta.setData(investigation.getClaimBackgroundDetails() != null ? investigation.getClaimBackgroundDetails() : "");
							claimBackgroundTxta.setId("clmBackgroundDetails");
							handleRemarksPopup(claimBackgroundTxta, null);
							claimBackgroundTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
							
							factsOfCaseTxta = new TextArea("Facts Of the Case");
							factsOfCaseTxta.setValue(investigation.getFactsOfCase() != null ? investigation.getFactsOfCase() : "");
							factsOfCaseTxta.setReadOnly(true);
							factsOfCaseTxta.setWidth("407px");
							factsOfCaseTxta.setData(investigation.getFactsOfCase() != null ? investigation.getFactsOfCase() : "");
							factsOfCaseTxta.setId("invFactsCase");
							handleRemarksPopup(factsOfCaseTxta, null);
							factsOfCaseTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
							
							investigationTriggerPointTable.init("Investigator Trigger Points", false,false);
							
							FormLayout draftInvLetterFrm1 = new FormLayout(draftPersonIdName,draftprocessedDateTime,draftInvAllocationToCmb,claimBackgroundTxta);
							
							
							FormLayout draftInvLetterFrm2 = new FormLayout(factsOfCaseTxta);
							
							
							List<DraftTriggerPointsToFocusDetailsTableDto> trigPtsListDto = investigationService.getInvestigationDetailsBasedOnInvestigationKey(investigation.getKey());
							if(trigPtsListDto != null && !trigPtsListDto.isEmpty()){
								investigationTriggerPointTable.setTableList(trigPtsListDto);
							}
							
							HorizontalLayout draftHlayout = new HorizontalLayout(draftInvLetterFrm1,draftInvLetterFrm2);
							
							VerticalLayout draftInvLetterLayout = new VerticalLayout(draftHlayout, investigationTriggerPointTable); 
							draftInvLetterLayout.setWidth("100%");
							
							draftInvLetterPanel.setContent(draftInvLetterLayout);	
						}	
					}
					if(assignedInvestigationDetailsObj != null){
						invAssignedByIdName = new TextField("Assigned By Id / Name");
						empObj = masterService.getEmployeeName(assignedInvestigationDetailsObj.getCreatedBy());
						invAssignedByIdName.setValue((assignedInvestigationDetailsObj.getCreatedBy() != null ? assignedInvestigationDetailsObj.getCreatedBy() : "")
														+ (empObj != null && empObj.getEmpFirstName() != null && !empObj.getEmpFirstName().isEmpty() ? (" - " + empObj.getEmpFirstName()) : ""));
						invAssignedByIdName.setReadOnly(true);
						
						invAssignedDateTime = new TextField("Assigned Date & Time");
						invAssignedDateTime.setValue(assignedInvestigationDetailsObj.getCreatedDate() != null ? (assignedInvestigationDetailsObj.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm aa").format(new Timestamp(assignedInvestigationDetailsObj.getCreatedDate().getTime())) : ""): "");
						invAssignedDateTime.setReadOnly(true);
						
						FormLayout assignInvFrm1 = new FormLayout(invAssignedByIdName,invAssignedDateTime);
						// Assigned Table needs to be added
						
						viewassignedTable.init("", false, false);
						viewassignedTable.initView(assignedInvestigationDetailsObj.getKey());
						
						Button invAssignHistoryBtn = new Button("Investigation Assigned History");
						
						invAssignHistoryBtn.addClickListener(new Button.ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								getInvHistoryTable(assignedInvestigationDetailsObj.getKey());
																
							}
						});
						
						VerticalLayout assignInvLayout = new VerticalLayout(invAssignHistoryBtn,assignInvFrm1,viewassignedTable);
						
						assignInvLayout.setWidth("100%");
						
						assignInvLayout.setComponentAlignment(invAssignHistoryBtn, Alignment.TOP_RIGHT);
						
						assignInvPanel.setContent(assignInvLayout);
						
						List<MultipleUploadDocumentDTO> uploadFileList = reimbursementService.getUpdateDocumentDetails(assignedInvestigationDetailsObj.getKey());
						String fileNameValue = "";
						String fileUploadedUser = "";
						Date uploadedDate = null;
						
						if(uploadFileList != null && !uploadFileList.isEmpty()){
							for (MultipleUploadDocumentDTO multipleUploadDocumentDTO : uploadFileList) {
								if(multipleUploadDocumentDTO.getFileName() != null && !multipleUploadDocumentDTO.getFileName().isEmpty()){
									fileNameValue = (fileNameValue != null && !fileNameValue.isEmpty() ? (fileNameValue + ", ") : "") + multipleUploadDocumentDTO.getFileName();
								}
								fileUploadedUser = multipleUploadDocumentDTO.getUsername();
								uploadedDate = multipleUploadDocumentDTO.getUploadedDate();
							}
						}
		
						if(!ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigation.getStatus().getKey())){
							if(fileUploadedUser != null && !fileUploadedUser.isEmpty())
							{
								empObj = fileUploadedUser != null ? masterService.getEmployeeName(fileUploadedUser) : null;
							}				
							else{
								empObj = null;
							}
						}			
						uploadedByIdName = new TextField("Uploaded By Id / Name");
						uploadedByIdName.setValue((fileUploadedUser != null ? fileUploadedUser : "") + 
								(empObj != null && empObj.getEmpFirstName() != null && !empObj.getEmpFirstName().isEmpty() ? (" - " + empObj.getEmpFirstName()) : ""));
						uploadedByIdName.setReadOnly(true);
						
						uploadedDateTime = new TextField("Uploaded Date & Time");
						uploadedDateTime.setValue(fileUploadedUser != null && !fileUploadedUser.isEmpty() && uploadedDate != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm aa").format(new Timestamp(uploadedDate.getTime())) : "");
						uploadedDateTime.setReadOnly(true);
										
						fileName = new TextField("File Name"); 
						fileName.setValue(fileNameValue);
						fileName.setReadOnly(true);
						
						fileType = new TextField("File Type");
						fileType.setValue(empObj != null && empObj.getEmpFirstName() != null && !empObj.getEmpFirstName().isEmpty() ? SHAConstants.CLAIM_VERIFICATION_REPORT : "");
						fileType.setReadOnly(true);
						
						FormLayout uploadInvReportFrm1 = new FormLayout(uploadedByIdName,uploadedDateTime,fileName,fileType);
						uploadInvReportFrm1.setWidth("100%");
						dateOfCompletion = new TextField("Date Of Completion");
						dateOfCompletion.setValue(assignedInvestigationDetailsObj.getCompletionDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(assignedInvestigationDetailsObj.getCompletionDate()) : "");
						dateOfCompletion.setEnabled(false);
						
						invCompletionRemarksTxta = new TextArea("Investigation Completion Remarks");
						invCompletionRemarksTxta.setValue(assignedInvestigationDetailsObj.getCompletionRemarks() != null ? assignedInvestigationDetailsObj.getCompletionRemarks() : "");
						invCompletionRemarksTxta.setReadOnly(true);
						invCompletionRemarksTxta.setWidth("375px");
						invCompletionRemarksTxta.setData(assignedInvestigationDetailsObj.getCompletionRemarks() != null ? assignedInvestigationDetailsObj.getCompletionRemarks() : "");
						invCompletionRemarksTxta.setId("invComplnRemarks");
						handleRemarksPopup(invCompletionRemarksTxta, null);
						invCompletionRemarksTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
						
						FormLayout uploadInvReportFrm2 = new FormLayout(dateOfCompletion,invCompletionRemarksTxta);
						uploadInvReportFrm2.setWidth("100%");
						HorizontalLayout uploadInvReportHLayout = new HorizontalLayout(uploadInvReportFrm1,uploadInvReportFrm2);
						uploadInvReportHLayout.setWidth("100%");
						Button viewUploadDocsBtn = new Button("View Documents");
						VerticalLayout uploadInvReportVLayout = new VerticalLayout(viewUploadDocsBtn,uploadInvReportHLayout);
						uploadInvReportVLayout.setWidth("100%");
						uploadInvReportVLayout.setComponentAlignment(viewUploadDocsBtn, Alignment.TOP_RIGHT);
						viewUploadDocsBtn.addClickListener(new Button.ClickListener() {
		
							@Override
							public void buttonClick(ClickEvent event) {
		
								if(assignedInvestigationDetailsObj.getKey() != null){
									List<MultipleUploadDocumentDTO>  uploadedDocList = investigationService.getUpdateDocumentDetails(assignedInvestigationDetailsObj.getKey());
									if(uploadedDocList != null && !uploadedDocList.isEmpty()){
										List<Long> docTokenList = new ArrayList<Long>();
										for (MultipleUploadDocumentDTO docDto : uploadedDocList) {
											docTokenList.add(Long.valueOf(docDto.getFileToken()));
										}	
										viewUploadedInvDocDetails(investigation.getIntimation().getIntimationId(), investigationService.getUploadedDocDetailsByToken(docTokenList));
									}
								}
							}
						});
						
						uploadInvReportPanel.setContent(uploadInvReportVLayout);
						
						invGradedByIdName = new TextField("Investigation Graded By Id / Name");
						empObj = masterService.getEmployeeName(assignedInvestigationDetailsObj.getGradedBy());
						invGradedByIdName.setValue((assignedInvestigationDetailsObj.getGradedBy() != null ? assignedInvestigationDetailsObj.getGradedBy() : "") + 
						(empObj != null && empObj.getEmpFirstName() != null && !empObj.getEmpFirstName().isEmpty() ? (" - " + empObj.getEmpFirstName()) : ""));
						invGradedByIdName.setReadOnly(true);
						invGradedDateTime = new TextField("investigation Graded Date & Time");
						invGradedDateTime.setValue(assignedInvestigationDetailsObj.getGradingDate() != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm aa").format(new Timestamp(assignedInvestigationDetailsObj.getGradingDate().getTime())) : "");
						invGradedDateTime.setReadOnly(true);
						FormLayout invGradingFrm1 = new FormLayout(invGradedByIdName,invGradedDateTime);
						
						invGradedCategCmb = new ComboBox("Graded Category");
						SelectValue gradingCategorySelect =  new SelectValue(1l, (assignedInvestigationDetailsObj.getGradingCategory() != null ? assignedInvestigationDetailsObj.getGradingCategory() : ""));
						BeanItemContainer<SelectValue> gardContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
						gardContainer.addBean(gradingCategorySelect);
						invGradedCategCmb.setContainerDataSource(gardContainer);
						invGradedCategCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						invGradedCategCmb.setItemCaptionPropertyId("value");
						invGradedCategCmb.setValue(gardContainer.getIdByIndex(0));
						invGradedCategCmb.setEnabled(false);
						
						invGradingRemarksTxta = new TextArea("investigation Grading Remarks");
						invGradingRemarksTxta.setValue(assignedInvestigationDetailsObj.getGradingRemarks() != null ? assignedInvestigationDetailsObj.getGradingRemarks() : "");
						invGradingRemarksTxta.setReadOnly(true);
						invGradingRemarksTxta.setWidth("395px");
						invGradingRemarksTxta.setData(assignedInvestigationDetailsObj.getGradingRemarks() != null ? assignedInvestigationDetailsObj.getGradingRemarks() : "");
						invGradingRemarksTxta.setId("invGradingRemarks");
						handleRemarksPopup(invGradingRemarksTxta, null);
						invGradingRemarksTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
						
						FormLayout invGradingFrm2 = new FormLayout(invGradedCategCmb,invGradingRemarksTxta);
						
						HorizontalLayout invGradingHLayout = new HorizontalLayout(invGradingFrm1,invGradingFrm2);
						invGradingHLayout.setWidth("100%");
						
						invGradingPanel.setContent(invGradingHLayout);
						
				}
				
				Button viewAllInvUploadedDocs = new Button("View Investigation Upload Report");
				
				viewAllInvUploadedDocs.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						
						List<AssignedInvestigatiorDetails> assignedList = investigationService.getAssignedListByInvestigationKey(investigation.getKey());
						if(assignedList != null && !assignedList.isEmpty()){
							List<MultipleUploadDocumentDTO>  uploadedDocList = new ArrayList<MultipleUploadDocumentDTO>();
							List<MultipleUploadDocumentDTO>  resultDocList = null;
							for (AssignedInvestigatiorDetails assignedObj : assignedList) {
								resultDocList = investigationService.getUpdateDocumentDetails(assignedObj.getKey());
								if(resultDocList != null && !resultDocList.isEmpty()){
									uploadedDocList.addAll(resultDocList);
								}
							}
							if(uploadedDocList != null && !uploadedDocList.isEmpty()){
								List<Long> docTokenList = new ArrayList<Long>();
								for (MultipleUploadDocumentDTO docDto : uploadedDocList) {
									if(docDto.getFileToken() != null){
										docTokenList.add(Long.valueOf(docDto.getFileToken()));
									}
								}	
								viewUploadedInvDocDetails(investigation.getIntimation().getIntimationId(), investigationService.getUploadedDocDetailsByToken(docTokenList));
							}
						}
					}
				});				

				wholeLayout = new HorizontalLayout();
				wholeLayout.setWidth("100%");
				mainVLayout.setWidth("100%");
				mainVLayout.addComponents(viewAllInvUploadedDocs,initiateInvPanel,processInvInitiatePanel,draftInvLetterPanel,assignInvPanel,uploadInvReportPanel,invGradingPanel);
				mainVLayout.setComponentAlignment(viewAllInvUploadedDocs, Alignment.TOP_RIGHT);
//				wholeLayout.addComponents(mainVLayout);
//				wholeLayout.setSpacing(true);
			}
		
		setCompositionRoot(mainVLayout);
	
	}
	
	public void getInvHistoryTable(Long invAssignKey){
		invReassignHistoryTable.init("", false, false);
		invReassignHistoryTable.initView(invAssignKey);
		showContentPopup(invReassignHistoryTable,null);
	}
	public void viewUploadedInvDocDetails(String intimationNo,List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO) {

		DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());	
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
		}

		Window popup = new com.vaadin.ui.Window();

		dmsDocDetailsViewPage.init(dmsDTO, popup);
		dmsDocDetailsViewPage.getContent();
		showContentPopup(dmsDocDetailsViewPage,popup);
		
	}
	
	public void showContentPopup(Component comp,Window popup){
		
		if(popup == null){
			popup = new com.vaadin.ui.Window();
			popup.setCaption("View Re-Assignement History");
			popup.setResizable(true);
		}
		else{
			popup.setCaption("");
			popup.setResizable(false);
		}
		
		popup.setWidth("75%");
		popup.setHeight("85%");
		//popup.setSizeFull();
		popup.setContent(comp);
		popup.setClosable(true); 
		popup.center();
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
//				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(4000);
//				txtArea.setRows(remarksValue != null ? (remarksValue.length()/80 >= 25 ? 25 : ((remarksValue.length()/80)%25)+1) : 25);
				txtArea.setReadOnly(true);
				
				
				String splitArray[] = remarksValue.split("[\n*|.*]");
				
				if(splitArray != null && splitArray.length > 0 && splitArray.length > 25){
					txtArea.setRows(25);
				}
				else{
					txtArea.setRows(splitArray.length);
				}
				txtArea.setHeight("30%");
				
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				final Window dialog = new Window();
				
				String strCaption = "";

				if(("TrigPointsFocus").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Trigger points to focus";
				}
				if(("invRefReason").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Reason for Refering";
				}
				if(("invApproveRemarks").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Investigation Approved Remarks";
				}
				if(("invdisappruRemarks").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Investigation Disapproved Remarks";
				}
				if(("fvrTrigPts").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "FVR Trigger Points";
				}
				if(("clmBackgroundDetails").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Claim Background Details";
				}
				if(("invFactsCase").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Facts of the Case";
				}
				if(("invComplnRemarks").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Completion Remarks";
				}
				if(("invGradingRemarks").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Investigation Grading Remarks";
				}
				if(("invTrigPoints").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Investigation Trigger Points";
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
