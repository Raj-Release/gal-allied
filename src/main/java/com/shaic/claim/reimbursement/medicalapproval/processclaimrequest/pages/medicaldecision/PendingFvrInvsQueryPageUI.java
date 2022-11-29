package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
/*import org.vaadin.dialogs.ConfirmDialog;*/







import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.fileUpload.MultipleUploadDocumentPageUI;
import com.shaic.claim.fileUpload.WaivedQueryUploadDocumentPageUI;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.benefits.wizard.pages.ProcessClaimRequestBenefitsDecisionPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.ClaimRequestDataExtractionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.premedicalprocessing.ClaimRequestPremedicalProcessingPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.previousclaims.ClaimRequestPreviousClaimsPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.InvesAndQueryAndFvrParallelFlowTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.InvesAndQueryAndFvrParallelFlowTableDTO;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.domain.preauth.CashlessWorkFlow;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.medicaldecision.PAHealthClaimRequestMedicalDecisionPagePresenter;
import com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.PAClaimRequestDataExtractionPagePresenter;
import com.vaadin.server.Page;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class PendingFvrInvsQueryPageUI extends ViewComponent{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TextField txtInsuredName;
	
	private Button btnSubmit;
	
	private Button btnClose;
	
	@Inject
	public Instance<InvesAndQueryAndFvrParallelFlowTable> invesFvrQueryPendingTable;
	
	public InvesAndQueryAndFvrParallelFlowTable invesFvrQueryPendingTableObj;
	
	@EJB
	private PreauthService preauthService;
	
	@Inject
	private ViewDetails viewDetails;
	
	@EJB
	private ReimbursementService reimbursementService;

	@EJB
	private ReimbursementQueryService reimbursementQueryService;
	
	@EJB
	private InvestigationService investigationService;
	
	private Window popup = null;
	
	private PreauthDTO bean;
	
	@EJB
	private FieldVisitRequestService fieldVisitService;
	
	@Inject
	private WaivedQueryUploadDocumentPageUI uploadWaivedQueryDocumentPageUI;
	
	private Boolean isAllInvsCanceled = Boolean.FALSE;
	
	@EJB
	private DBCalculationService dbCalService;	
	
	public void init(List<InvesAndQueryAndFvrParallelFlowTableDTO>  pendingFvrInvsQueryList,PreauthDTO preauthDto, Window popup,String screenName,String event){
		
		this.popup = popup;
		this.bean = preauthDto;
		
		
		InvesAndQueryAndFvrParallelFlowTable invesFvrQueryPendingTableInstance = invesFvrQueryPendingTable.get();
		//procedureTableInstance.init(bean.getHospitalKey(), "preauthEnhancement",diagList);
		invesFvrQueryPendingTableInstance.init("",false,false);
		invesFvrQueryPendingTableInstance.initTable(bean);
		this.invesFvrQueryPendingTableObj = invesFvrQueryPendingTableInstance;
		//this.invesFvrQueryPendingTableObj.setTableList(pendingFvrInvsQueryList);
		this.invesFvrQueryPendingTableObj.setPendingFvrInvsQueryPageObj(this);
		for (InvesAndQueryAndFvrParallelFlowTableDTO invesFvrQueryPendingTableDTO : pendingFvrInvsQueryList) {
			Boolean isCancelRod = event != null ? 
					event.equals(SHAConstants.CANCEL_ROD) ? true : false: false;
			invesFvrQueryPendingTableDTO.setIsCanCelRodStatus(isCancelRod);
			this.invesFvrQueryPendingTableObj.addBeanToList(invesFvrQueryPendingTableDTO);
		}
		btnSubmit = new Button("Submit");
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		
		btnClose = new Button("Cancel");
		btnClose.addStyleName(ValoTheme.BUTTON_DANGER);
		btnClose.setWidth("-1px");
		btnClose.setHeight("-10px");
		
		HorizontalLayout buttonHor = new HorizontalLayout(btnSubmit,btnClose);
		buttonHor.setSpacing(true);
		
		uploadWaivedQueryDocumentPageUI.init(SHAConstants.UPLOAD_QUERY_LETTER, bean.getKey(), false);
		Button viewQueryDetails = new Button("View Query Details");
		viewQueryDetails.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
			//To implement claims dms functionality.
				viewDetails.getRevisedQueryDetailsTable(bean.getNewIntimationDTO().getIntimationId());
				
			}
		});
		viewQueryDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		viewQueryDetails.setWidth("150px");
		viewQueryDetails.addStyleName(ValoTheme.BUTTON_LINK);
		
		Button viewFVRDetails = new Button("View FVR Details");
		viewFVRDetails.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
			//To implement claims dms functionality.
				viewDetails.getFVRDetails(bean.getNewIntimationDTO().getIntimationId(),false);
				
			}
		});
		viewFVRDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		viewFVRDetails.setWidth("150px");
		viewFVRDetails.addStyleName(ValoTheme.BUTTON_LINK);
		
		Button viewInvesDetails = new Button("View Investigation Details");
		viewInvesDetails.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
			//To implement claims dms functionality.
				viewDetails.getInvestigationDetails(bean.getNewIntimationDTO().getIntimationId(), false);
				
			}
		});
		viewInvesDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		viewInvesDetails.setWidth("150px");
		viewInvesDetails.addStyleName(ValoTheme.BUTTON_LINK);
		
		Button uploadLetter = new Button("Upload Letter");
		uploadLetter.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				final Window popup = new com.vaadin.ui.Window();
				popup.setWidth("85%");
				popup.setHeight("80%");
				
				uploadWaivedQueryDocumentPageUI.init(SHAConstants.UPLOAD_QUERY_LETTER, bean.getKey(), false);
				Page page = getUI().getPage();
				uploadWaivedQueryDocumentPageUI.setCurrentPage(page);
				
				Button btnClose = new Button("Close");
				btnClose.addStyleName(ValoTheme.BUTTON_DANGER);
				btnClose.setWidth("-1px");
				btnClose.setHeight("-10px");
				
				VerticalLayout vLayout = new VerticalLayout();
				vLayout.addComponents(uploadWaivedQueryDocumentPageUI,btnClose);
				vLayout.setComponentAlignment(btnClose, Alignment.MIDDLE_CENTER);
				
				popup.setContent(vLayout);
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

				btnClose.addClickListener(new Button.ClickListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						popup.close();
					}
				});
				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
				
//				}else{
//					getErrorMessage("File not Available");
//				}
				
	
			
				
			}
		});
		uploadLetter.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		uploadLetter.setWidth("150px");
		uploadLetter.addStyleName(ValoTheme.BUTTON_LINK);
		
		TextField dummyFiled = new TextField();
		dummyFiled.setHeight("80px");
		dummyFiled.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		VerticalLayout fvrInvsQueryLayout = new VerticalLayout(dummyFiled,uploadLetter,viewQueryDetails,viewFVRDetails,viewInvesDetails);
		fvrInvsQueryLayout.setSpacing(true);
		//fvrInvsQueryLayout.setMargin(true);
		
		FormLayout tableFormLayout = new FormLayout();
		tableFormLayout.addComponent(invesFvrQueryPendingTableObj);
		tableFormLayout.setSpacing(true);
		
		VerticalLayout mainVertical = new VerticalLayout(tableFormLayout,buttonHor);
		mainVertical.setSpacing(true);
		mainVertical.setComponentAlignment(buttonHor, Alignment.BOTTOM_CENTER);
		
		HorizontalLayout mainHorizontalLayout = new HorizontalLayout(mainVertical,fvrInvsQueryLayout);
		mainHorizontalLayout.setSpacing(true);
		addListener(screenName,event);
		
		setCompositionRoot(mainHorizontalLayout);
	}
	
	public void addListener(final String screenName,final String action){
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				Boolean isClose = Boolean.FALSE;
				
				List<InvesAndQueryAndFvrParallelFlowTableDTO> invesFvrQueryPendingDto = invesFvrQueryPendingTableObj.getValues();
				List<InvesAndQueryAndFvrParallelFlowTableDTO> finalList = new ArrayList<InvesAndQueryAndFvrParallelFlowTableDTO>();
				
				if(null != invesFvrQueryPendingDto && !invesFvrQueryPendingDto.isEmpty()){	
					
					for (InvesAndQueryAndFvrParallelFlowTableDTO pendingProcessDto : invesFvrQueryPendingDto) {
						
						if(null != pendingProcessDto.getType() && ! (SHAConstants.PARALLEL_INVESTIGATION_TYPE).equalsIgnoreCase(pendingProcessDto.getType())){
							
							//IMSSUPPOR-28268 - As discussed with BA if rod need to be cancelled, parallel query also to be cancelled. - 29-Apr-19
							if(null != pendingProcessDto.getType() && (SHAConstants.PARALLEL_QUERY_TYPE).equalsIgnoreCase(pendingProcessDto.getType())){
								if(null != pendingProcessDto.getIsCanCelRodStatus() && pendingProcessDto.getIsCanCelRodStatus())
								{
									if(null != pendingProcessDto.getFvrInvsQueryCancelStatus() && !pendingProcessDto.getFvrInvsQueryCancelStatus()){
										showErrorPopUp("Please select Cancel Request for Query Initiation");
										isClose = Boolean.TRUE;
										break;
									}
								}
							}
							
							
							
							if((null != pendingProcessDto.getFvrInvsQueryCancelStatus() && (pendingProcessDto.getFvrInvsQueryCancelStatus())) ||
									(null != pendingProcessDto.getProceedWithOutCheckStatus() && (pendingProcessDto.getProceedWithOutCheckStatus()))	){
								
								finalList.add(pendingProcessDto);
								
							}
						}
					}
					
					for (InvesAndQueryAndFvrParallelFlowTableDTO pendingProcessDto : invesFvrQueryPendingDto) {
						
						
							if(null != pendingProcessDto.getType() && (SHAConstants.PARALLEL_INVESTIGATION_TYPE).equalsIgnoreCase(pendingProcessDto.getType())){
								if((null != bean.getPreauthDataExtractionDetails().getInvestigatorsCount() 
										&& bean.getPreauthDataExtractionDetails().getInvestigatorsCount()>1)
										&& (null != bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount() 
										&& bean.getPreauthDataExtractionDetails().getInvsReplyReceivedCount()>=1))
								{
									if(null != pendingProcessDto.getFvrInvsQueryCancelStatus() && !pendingProcessDto.getFvrInvsQueryCancelStatus()){
										showErrorPopUp("Please select cancel Request for all the assigned investigations");
										isClose = Boolean.TRUE;
										break;
									}
								}
							}
							
							if(null != pendingProcessDto.getType() && (SHAConstants.PARALLEL_INVESTIGATION_TYPE).equalsIgnoreCase(pendingProcessDto.getType())){
							
								if((null != pendingProcessDto.getFvrInvsQueryCancelStatus() && (pendingProcessDto.getFvrInvsQueryCancelStatus())) ||
										(null != pendingProcessDto.getProceedWithOutCheckStatus() && (pendingProcessDto.getProceedWithOutCheckStatus()))	){
									
									finalList.add(pendingProcessDto);
									
								}
							}
					}
				}
				DBCalculationService dbServcie = new DBCalculationService();
				if(null != finalList && !finalList.isEmpty()){
					for (InvesAndQueryAndFvrParallelFlowTableDTO cancelledFvrInvsOrquery : finalList) {						
						 if((null != cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus() && cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus()) ||
							(null != cancelledFvrInvsOrquery.getProceedWithOutCheckStatus() && cancelledFvrInvsOrquery.getProceedWithOutCheckStatus())){
							
							Map<String, Object> workFlowPayload = new WeakHashMap<String, Object>();
							
							Map<String, Object> workFlowObj = (Map<String, Object>) cancelledFvrInvsOrquery.getDbOutArray();
							
							workFlowPayload.putAll(workFlowObj);
							
													
							if(null != cancelledFvrInvsOrquery.getType() && (SHAConstants.PARALLEL_INVESTIGATION_TYPE).equalsIgnoreCase(cancelledFvrInvsOrquery.getType())){
								
								if(null != cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus() && cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus()){
									
									workFlowPayload.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_CASHLESS_INV_DISAPPROVE);
								}
								
								Long investigationKey = (Long) workFlowPayload.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY);
								String assignInvestigatinKey = (String) workFlowPayload.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY);
								Long assignInvestigationKey = 0l;
								if(assignInvestigatinKey != null){
									assignInvestigationKey = Long.parseLong(assignInvestigatinKey);
								}
										
								investigationService.updateInvestigationStatus(investigationKey,cancelledFvrInvsOrquery,bean,assignInvestigationKey);
								
								if(null != cancelledFvrInvsOrquery.getProceedWithOutCheckStatus() && cancelledFvrInvsOrquery.getProceedWithOutCheckStatus()){
									
									workFlowPayload.put(SHAConstants.PAYLOAD_PED_REQUESTOR_ROLE, SHAConstants.YES_FLAG);
								}
								
							}
							else if(null != cancelledFvrInvsOrquery.getType() && (SHAConstants.PARALLEL_QUERY_TYPE).equalsIgnoreCase(cancelledFvrInvsOrquery.getType())){
								
								if(null != cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus() && cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus()){
									
									workFlowPayload.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_APPROVE_PROCESS_DRAFT_QUERY_LETTER);
								}
								
								Long queryKey = (Long) workFlowPayload.get(SHAConstants.PAYLOAD_QUERY_KEY);
								
								reimbursementQueryService.updateQueryStatus(queryKey,cancelledFvrInvsOrquery,bean);
								dbServcie.stopReminderProcessProcedure(bean.getNewIntimationDTO().getIntimationId(),SHAConstants.OTHERS);
							}							
							else if(null != cancelledFvrInvsOrquery.getType() && (SHAConstants.PARALLEL_FVR_TYPE).equalsIgnoreCase(cancelledFvrInvsOrquery.getType())){
								
								Long fvrKey = Long.parseLong(String.valueOf( workFlowPayload.get(SHAConstants.FVR_KEY)));
								fieldVisitService.updateFvrDetails(fvrKey,cancelledFvrInvsOrquery);
							}
							
							workFlowPayload.put(SHAConstants.USER_ID,bean.getStrUserName());
							
							if (workFlowPayload != null) {
								
								Object[] inputWorkFlowArray = SHAUtils
										.getRevisedObjArrayForSubmit(workFlowPayload);
								dbServcie
										.revisedInitiateTaskProcedure(inputWorkFlowArray);
								
								Long wkFlowKey = (Long) workFlowPayload.get(SHAConstants.WK_KEY);
								String intimationNo = (String) workFlowPayload.get(SHAConstants.INTIMATION_NO);
								
								dbCalService.workFlowEndCallProcedure(wkFlowKey, intimationNo);
							}
						   
						}
						
					}
				}								
				if(null != invesFvrQueryPendingDto && !invesFvrQueryPendingDto.isEmpty() && null != finalList && !finalList.isEmpty() && 
						(invesFvrQueryPendingDto.size() == finalList.size())){
					
				if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
				Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
				bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
				}else{
					bean.setIsPedWatchList(false);
				}
				
				if(! bean.getIsPedWatchList()){
				
					if(SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION.equalsIgnoreCase(screenName)) {
							
						if(SHAConstants.CANCEL_ROD.equals(action)){
							fireViewEvent(ClaimRequestDataExtractionPagePresenter.CLAIM_CANCEL_ROD_EVENT,null);
						}
						else if(SHAConstants.SEND_REPLY.equals(action)){
							fireViewEvent(ClaimRequestDataExtractionPagePresenter.CLAIM_REQUEST_SENT_TO_REPLY_EVENT,null);
						}
							
					}
					else if(SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_PREVIOUS_CLAIM_PAGE.equalsIgnoreCase(screenName)){
						if (SHAConstants.CANCEL_ROD.equals(action)){
							fireViewEvent(ClaimRequestPreviousClaimsPagePresenter.CLAIM_CANCEL_ROD_EVENT,null);
						}
						else if(SHAConstants.SEND_REPLY.equals(action)){
							fireViewEvent(ClaimRequestPreviousClaimsPagePresenter.CLAIM_REQUEST_SENT_TO_REPLY_EVENT,null);
						}
					}
					else if(SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_MEDICAL_PROCESSING.equalsIgnoreCase(screenName)){
						if(SHAConstants.CANCEL_ROD.equals(action)){
							fireViewEvent(ClaimRequestPremedicalProcessingPagePresenter.CLAIM_CANCEL_ROD_EVENT,null);
						}
						else if(SHAConstants.SEND_REPLY.equals(action)){
							fireViewEvent(ClaimRequestPremedicalProcessingPagePresenter.CLAIM_REQUEST_SENT_TO_REPLY_EVENT,null);
						}
					}
					else if(SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_MEDICAL_DECISION.equalsIgnoreCase(screenName)){
						if( SHAConstants.CANCEL_ROD.equals(action)){
							fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.CLAIM_CANCEL_ROD_EVENT,null);
						}
						else if(SHAConstants.APPROVAL.equals(action)){
							fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_APPROVE_EVENT,null);
						}
						else if(SHAConstants.REJECT.equals(action)){
							fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_REJECTION_EVENT,bean);
						}
						else if(SHAConstants.SEND_REPLY.equals(action)){
							fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_SENT_TO_REPLY_EVENT,null);
						}
					}
					else if(SHAConstants.PA_MEDICAL_APPROVAL_CLAIM_REQUEST_MEDICAL_DECISION.equalsIgnoreCase(screenName)){
						if(SHAConstants.APPROVAL.equals(action)){
							fireViewEvent(PAClaimRequestDataExtractionPagePresenter.CLAIM_REQUEST_APPROVE_EVENT,null);
						}
						else if(SHAConstants.REJECT.equals(action)){
							fireViewEvent(PAClaimRequestDataExtractionPagePresenter.CLAIM_REQUEST_REJECTION_EVENT,bean);
						}else if( SHAConstants.CANCEL_ROD.equals(action)){
							fireViewEvent(PAClaimRequestDataExtractionPagePresenter.CLAIM_CANCEL_ROD_EVENT,null);
						}
					}
					else if(SHAConstants.PA_Health_MEDICAL_APPROVAL_CLAIM_REQUEST_MEDICAL_DECISION.equalsIgnoreCase(screenName)){
						if( SHAConstants.CANCEL_ROD.equals(action)){
							fireViewEvent(PAHealthClaimRequestMedicalDecisionPagePresenter.CLAIM_CANCEL_ROD_EVENT,null);
						}
						else if(SHAConstants.APPROVAL.equals(action)){
							fireViewEvent(PAHealthClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_APPROVE_EVENT,null);
						}
						else if(SHAConstants.REJECT.equals(action)){
							fireViewEvent(PAHealthClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_REJECTION_EVENT,bean);
						}
						else if(SHAConstants.SEND_REPLY.equals(action)){
							fireViewEvent(PAHealthClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_SENT_TO_REPLY_EVENT,null);
						}
					}
					else if(SHAConstants.MEDICAL_APPROVAL_BENEFITS_CLAIM_REQUEST.equalsIgnoreCase(screenName)){
						if( SHAConstants.CANCEL_ROD.equals(action)){
							fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_MEDICAL_CANCEL_ROD,null);
						}
						else if(SHAConstants.APPROVAL.equals(action)){
							fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_MEDICAL_APPROVE_EVENT,null);
						}
						else if(SHAConstants.REJECT.equals(action)){
							fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_MEDICAL_REQUEST_REJECT_EVENT,bean);
						}
						else if(SHAConstants.SEND_REPLY.equals(action)){
							fireViewEvent(ProcessClaimRequestBenefitsDecisionPresenter.BENEFITS_MEDICAL_REQUEST_SENT_TO_REPLY_EVENT,null);
						}
					}
				}else{
				    alertMessageForPEDWatchList();
				}
			}
			if(!isClose){
				popup.close();
			}
		}
	});
		
		btnClose.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				popup.close();
				bean.setStatusKey(bean.getParallelStatusKey());
				bean.setStatusKey(ReferenceTable.WAIT_FOR_INPUT_KEY);
			}
		});
		
		
	}
	
	public Boolean alertMessageForPEDWatchList() {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PED_WATCHLIST + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("<b style = 'color: red;'>" + SHAConstants.PED_WATCHLIST + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				bean.setIsPEDInitiated(false);
			}
		});
		return true;
	}
	
	
	public void buildLayoutBasedOnButtons(){
		
		if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
				&& bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
//			Boolean taskAvailableInWatchListForIntimation = pedQueryService.isTaskAvailableInWatchListForIntimation(bean.getNewIntimationDTO().getIntimationId());
			Boolean taskAvailableInWatchListForIntimation = preauthService.getDBTaskForPreauth(bean.getNewIntimationDTO().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
			bean.setIsPedWatchList(taskAvailableInWatchListForIntimation);
		}else{
			bean.setIsPedWatchList(false);
		}
		
		if(! bean.getIsPedWatchList()){
			
			if(null != bean.getStatusKey() && (ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS.equals(bean.getStatusKey()))){
		fireViewEvent(
				ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_ESCALATE_EVENT,
				null);
			}
			else if(null != bean.getStatusKey() && (ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS.equals(bean.getStatusKey()))){
				fireViewEvent(
						ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_ESCALATE_REPLY_EVENT,
						null);	
			}
			else if(null != bean.getStatusKey() && (ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS.equals(bean.getStatusKey()))){
				fireViewEvent(
						ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_REFERCOORDINATOR_EVENT,
						null);	
			}
			else if(null != bean.getStatusKey() && (ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS.equals(bean.getStatusKey()))){
				fireViewEvent(
						ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_SPECIALIST_EVENT,
						null);	
			}
			else if(null != bean.getStatusKey() && (ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS.equals(bean.getStatusKey()))){
				fireViewEvent(
						ClaimRequestMedicalDecisionPagePresenter.CLAIM_REQUEST_SENT_TO_REPLY_EVENT,
						null);	
			}
			
		}else{
	
			alertMessageForPEDWatchList();
		}
			
		
	}
	
	public Boolean isDocumentUploaded(){
		return uploadWaivedQueryDocumentPageUI.IsDocumentUploaded();
	}
	
	public Boolean showErrorPopUp(String msg) {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + msg + "</b>",
				ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("<b style = 'color: red;'>" + msg + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
			}
		});
		return true;
	}
}
