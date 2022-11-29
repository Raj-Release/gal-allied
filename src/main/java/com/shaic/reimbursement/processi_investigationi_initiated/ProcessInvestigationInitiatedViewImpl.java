package com.shaic.reimbursement.processi_investigationi_initiated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.fvrdetails.view.FVRTriggerPtsTable;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.claim.lumen.createinpopup.PopupInitiateLumenRequestWizardImpl;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.domain.Investigation;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.reimbursement.processi_investigationi_initiated.search.SearchProcessInvestigationInitiatedTableDTO;
import com.vaadin.cdi.UIScoped;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

@UIScoped
public class ProcessInvestigationInitiatedViewImpl extends AbstractMVPView
		implements ProcessInvestigationInitiatedView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private static final String WIDTH = "1250px";

	private TextField txtInvestigationNo;

	private Button btnViewInvestigationDetails;

	@Inject
	private ViewDetails viewDetails;

	@Inject
	private RevisedCarousel preauthIntimationDetailsCarousel;
	
	@Inject
	private Toolbar toolbar;

	private HorizontalLayout viewDetailsHorizontalLayout;

	private VerticalLayout pnlLayout;

	private Label lblInvestigationRequestDetails;

	private TextField txtRequestingRole;

	private TextField txtRequestorIdOrName;

	private TextField txtAllocationTo;

	private FormLayout requestingRoleFormLayout;

	private TextArea txtReasonForReferring;

	private TextArea txtTriggerPointsToFocus;

	private FormLayout reasonForRefferingFormLayout;

	private HorizontalLayout requestingRoleHorizontalLayout;

	private Button btnApproveInvestigationRequest;

	private Button btnDisapproveInvestigationRequest;

	private HorizontalLayout btnHorizontalLayout;
	
	private OptionGroup initiateFieldVisitRequestRadio;
	
	private FormLayout fvrFormLayout;
	
	private VerticalLayout fvrVertLayout;

	private TextArea txtInvestigationApprovedRemarks;

	private TextArea txtInvestigationNotRequiredRemarks;

	private VerticalLayout mainLayout;

	private Panel mainPanel;

	private Button btnSubmit;

	private Button btnCancel;

	private HorizontalLayout submitHorizontalLayout;

	private BeanFieldGroup<ProcessInvestigationInitiatedDto> binder;

	private ClaimDto claimDto;

	private Investigation investigation;

	private Integer investigaitonSize;
	
	private SearchProcessInvestigationInitiatedTableDTO bean;
	
	private Boolean isDisapprove = false;
	
	private Boolean isInvEnable = false;
	
	private String investigationRole;
	
	private ComboBox cmbAllocationTo;
	
	private ComboBox cmbReasonforIniInv;
	
	private ComboBox cmbInvAllocationTo;
	
	//private ComboBox cmbFvrAssignTo;
	
	private ComboBox cmbFvrPriority;

//	private TextArea fvrTriggerPoints;
	
	@Inject
	private Instance<FVRTriggerPtsTable> triggerPtsTable;
	
	private FVRTriggerPtsTable triggerPtsTableObj;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private Boolean isButtonClicked = false;
	

//	@Inject
	private ProcessInvestigationInitiatedDto processInvestigationInitiatedDto;
	
	private RRCDTO rrcDTO;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	////private static Window popup;
	PreauthDTO preauthDTO = null;
	
	@Inject
	private LumenDbService lumenService;
	
	@Inject
	private Instance<PopupInitiateLumenRequestWizardImpl> initiateLumenRequestWizardInstance;
	
	private PopupInitiateLumenRequestWizardImpl initiateLumenRequestWizardObj;
	@EJB
	private PreauthService preauthService;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	@EJB
	private MasterService masterService;
	

	@Override
	public void setReference(Map<String, Object> referenceObj) {
		this.claimDto = (ClaimDto) referenceObj
				.get(ProcessInvestigationInitiatedPresenter.CLAIM_DTO);
		this.investigation = (Investigation) referenceObj
				.get(ProcessInvestigationInitiatedPresenter.INVESTIGATION_DTO);
		this.investigaitonSize = (Integer) referenceObj
				.get(ProcessInvestigationInitiatedPresenter.INVESTIGATION_SIZE);
	}

	private void initBinder() {
		this.binder = new BeanFieldGroup<ProcessInvestigationInitiatedDto>(
				ProcessInvestigationInitiatedDto.class);
		
		this.processInvestigationInitiatedDto = new ProcessInvestigationInitiatedDto();
		
		
		processInvestigationInitiatedDto.setReasonForReferring( investigation
		.getReasonForReferring() != null ? investigation
		.getReasonForReferring() : "");
		
		processInvestigationInitiatedDto.setTriggerPointsToFocus(investigation.getTriggerPoints() != null ? investigation
				.getTriggerPoints() : "");		
		
		//Reason for Initiate Inv
		SelectValue selectValueforInInv = new SelectValue();
		if(investigation.getReasonForInitiatingInv() !=null){
		selectValueforInInv.setId(investigation.getReasonForInitiatingInv().getKey());
		selectValueforInInv.setValue(investigation.getReasonForInitiatingInv().getValue());
		processInvestigationInitiatedDto.setReasonForIniInvestValue(investigation.getReasonForInitiatingInv().getValue());
		processInvestigationInitiatedDto.setReasonForInitiatingInvestSelectValue(selectValueforInInv);
		processInvestigationInitiatedDto.setReasonForIniInvestId(investigation.getReasonForInitiatingInv().getKey());
		}
		
		BeanItemContainer<SelectValue> reasonforIniInvContainer = masterService.getSelectValueContainer(ReferenceTable.REASON_FOR_INITIATE_INVESTIGATION);
		List<SelectValue> reasonforIniInvList = reasonforIniInvContainer.getItemIds();
		
		
		processInvestigationInitiatedDto.setReasonForInitiatingInvestSelectValueList(reasonforIniInvContainer.getItemIds());
		processInvestigationInitiatedDto.setReasonForInitiatingInvestIdList(reasonforIniInvList);
		
		this.binder.setItemDataSource(processInvestigationInitiatedDto);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setInvestigationRole(){
		if(bean.getInvestigationRequestedRole() != null){
			if(bean.getInvestigationRequestedRole().equalsIgnoreCase(SHAConstants.SENIOR_MEDICAL_APPROVER)){
				investigationRole ="Senior Medical Approver";
			}
			else if(bean.getInvestigationRequestedRole().equalsIgnoreCase(SHAConstants.MEDICAL_APPROVER_ROLE)){
				investigationRole = "Medical Approver";
			}
			else if(bean.getInvestigationRequestedRole().equalsIgnoreCase(SHAConstants.RCMO)){
				investigationRole = "Reimbursement Chief Medical officer";
			}
			}			
		}

	public void init(SearchProcessInvestigationInitiatedTableDTO investigation) {
		bean = investigation;
		rrcDTO = investigation.getRrcDTO();
		isDisapprove = false;
		isButtonClicked = false;
		
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(rrcDTO);
		//preauthDTO.setRodHumanTask(investigation.getHumanTaskDTO());
		
		if(SHAConstants.YES_FLAG.equalsIgnoreCase(investigation.getPreauthDTO().getNewIntimationDTO().getInsuredDeceasedFlag())) {
			
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		//CR2019217
	 	//changes done for SM agent percentage by noufel on 13-01-2020
		String agentpercentage = masterService.getAgentPercentage(ReferenceTable.ICR_AGENT);
		String smpercentage = masterService.getAgentPercentage(ReferenceTable.SM_AGENT);
		if((investigation.getPreauthDTO().getIcrAgentValue() != null && !investigation.getPreauthDTO().getIcrAgentValue().isEmpty() 
				&& (Integer.parseInt(bean.getPreauthDTO().getIcrAgentValue()) >= Integer.parseInt(agentpercentage))) 
				|| investigation.getPreauthDTO().getSmAgentValue() != null && !investigation.getPreauthDTO().getSmAgentValue().isEmpty() 
						&& (Integer.parseInt(investigation.getPreauthDTO().getSmAgentValue()) >= Integer.parseInt(smpercentage))){
			SHAUtils.showICRAgentAlert(investigation.getPreauthDTO().getIcrAgentValue(), agentpercentage, investigation.getPreauthDTO().getSmAgentValue(), smpercentage);
		}
	 
		
		setInvestigationRole();
		Long investigationKey = investigation.getInvestigationKey();
		fireViewEvent(ProcessInvestigationInitiatedPresenter.SET_REFERNCE,
				investigationKey);
	}
	
	public HorizontalLayout commonButtonsLayout()
	{
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		
		Button btnLumen = new Button("Initiate Lumen");
		btnLumen.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				invokePreMedicalLumenRequest();
			}
		});		
	
		FormLayout formLayout = new FormLayout(txtInvestigationNo);
		
		HorizontalLayout crmIconLayout = SHAUtils.newImageCRM(bean.getPreauthDTO());
		crmIconLayout.setSpacing(false);
		crmIconLayout.setWidth("100%");	
		
        HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(bean.getPreauthDTO());
		HorizontalLayout icrAgentBranch = SHAUtils.icrAgentBranch(bean.getPreauthDTO());
		HorizontalLayout buyBackPedHLayout = new HorizontalLayout();
		if(bean.getPreauthDTO().getNewIntimationDTO() !=null && bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getProduct().getCode() != null
				&& bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_78)){
			buyBackPedHLayout = SHAUtils.buyBackPed(bean.getPreauthDTO());
			icrAgentBranch.addComponent(buyBackPedHLayout);
		}
		if(bean.getPreauthDTO().getNewIntimationDTO() !=null && bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getProduct().getCode() != null
				&& bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_88)){
			buyBackPedHLayout = SHAUtils.buyBackPed(bean.getPreauthDTO());
			icrAgentBranch.addComponent(buyBackPedHLayout);
		}
		HorizontalLayout dummy = new HorizontalLayout();
		VerticalLayout icrHS = new VerticalLayout(dummy,icrAgentBranch);
		HorizontalLayout icrAGBR = new HorizontalLayout(icrHS);
        
        VerticalLayout hflayout=new VerticalLayout(crmIconLayout);
		HorizontalLayout vLayout = new HorizontalLayout(hflayout,hopitalFlag, btnLumen, btnRRC, btnViewInvestigationDetails);
		vLayout.setSpacing(true);
		VerticalLayout icrHospitalLayout =new VerticalLayout(vLayout,icrAGBR);
		VerticalLayout vLayout1 = new VerticalLayout(icrHospitalLayout,formLayout);
		HorizontalLayout alignmentHLayout = new HorizontalLayout(icrHospitalLayout);
		return alignmentHLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(ProcessInvestigationInitiatedPresenter.VALIDATE_PROCESS_INVESTIGATION_INTIATED_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
	}

	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {
		
			if (!isValid) {
				Label label = new Label("Same user cannot raise request more than once from same stage", ContentMode.HTML);
				label.setStyleName("errMessage");
				VerticalLayout layout = new VerticalLayout();
				layout.setMargin(true);
				layout.addComponent(label);
				ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Errors");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);
			} 
		else
		{
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("");
			popup.setWidth("85%");
			popup.setHeight("100%");
			rewardRecognitionRequestViewObj = rewardRecognitionRequestViewInstance.get();
			//ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
			//documentDetails.setClaimDto(bean.getClaimDTO());
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PROCESS_INVESTIGATION_INTIATED);
			
			
			
			rewardRecognitionRequestViewObj.init(preauthDTO, popup);
			
			//earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getKey());
			popup.setCaption("Reward Recognition Request");
			popup.setContent(rewardRecognitionRequestViewObj);
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
	
	private void invokePreMedicalLumenRequest(){
		//fireViewEvent(PreMedicalPreauthWizardPresenter.PREMEDICAL_PREAUTH_LUMEN_REQUEST, bean);
		List<Long> listOfSettledStatus = new ArrayList<Long>();
		/*listOfSettledStatus.add(ReferenceTable.FINANCIAL_SETTLED);
		listOfSettledStatus.add(ReferenceTable.PAYMENT_SETTLED);*/
		if(!listOfSettledStatus.contains(investigation.getClaim().getStatus().getKey())){
			fireViewEvent(ProcessInvestigationInitiatedPresenter.VALIDATE_PROCESS_INVESTIGATION_INTIATED_USER_LUMEN_REQUEST, investigation);
		}else{
			showErrorMessage("Claim is settled, lumen cannot be initiated");
			return;
		}
	}
	
	@Override
	public void buildInitiateLumenRequest(String intimationNumber){
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Initiate Lumen Request");
		popup.setWidth("75%");
		popup.setHeight("90%");
		LumenSearchResultTableDTO resultObj = lumenService.buildInitiatePage(intimationNumber);
		initiateLumenRequestWizardObj = initiateLumenRequestWizardInstance.get();
		initiateLumenRequestWizardObj.initView(resultObj, popup, "Process Investigation");
		
		VerticalLayout containerLayout = new VerticalLayout();
		containerLayout.addComponent(initiateLumenRequestWizardObj);
		popup.setContent(containerLayout);
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
	
	@Override
	public void loadRRCRequestDropDownValues(
			BeanItemContainer<SelectValue> mastersValueContainer) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.loadRRCRequestDropDownValues(mastersValueContainer)	;
		
	}
	
 
	@Override
	public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}

	@Override
	public void setLayout() {
		
		//R1152
		
		if (!this.bean.getIsGeoSame()) {
			getGeoBasedOnCPU();
		}
		
		else if(this.bean.getClaimCount() >3){
			alertMessageForClaimCount(this.bean.getClaimCount());
		}
		else if(bean.getIsPEDInitiated()) {
			alertMessageForPED();
		}
		else if(!bean.getSuspiciousPopupMap().isEmpty()){
			suspiousPopupMessage();
		}
		else  if(!bean.getNonPreferredPopupMap().isEmpty()){
			nonPreferredPopupMessage();
		}
		else  if(bean.getIs64VBChequeStatusAlert()){
			get64VbChequeStatusAlert();
		}
		else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
			poupMessageForProduct();
		}
		//CR2019112
		else if(bean.getPreauthDTO().getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
		}
		
		
		if (claimDto != null && claimDto.getNewIntimationDto() != null
				&& investigation != null) {
			initBinder();			
//			preauthIntimationDetailsCarousel.init(
//					claimDto.getNewIntimationDto(), this.claimDto,
//					"Process Investigation Initiated");
			preauthIntimationDetailsCarousel.init(
					claimDto.getNewIntimationDto(), this.claimDto,
					"Process Investigation Initiated",bean.getDiagnosis());
			txtInvestigationNo = binder.buildAndBind("Investigation No",
					"investigationNo", TextField.class);
			txtInvestigationNo.setNullRepresentation("");
			txtInvestigationNo
					.setValue(investigaitonSize != null ? investigaitonSize
							.toString() : "0");

			btnViewInvestigationDetails = new Button(
					"View Investigation  details");

			viewDetails.initView(this.investigation.getIntimation().getIntimationId(),bean.getRodKey(), ViewLevels.INTIMATION, false,"Process Investigation Initiated");

			/*viewDetailsHorizontalLayout = new HorizontalLayout(new FormLayout(
					txtInvestigationNo), btnViewInvestigationDetails,
					viewDetails);*/
			
			crmFlaggedComponents.init(bean.getCrcFlaggedReason(), bean.getCrcFlaggedRemark());
			
			viewDetailsHorizontalLayout = new HorizontalLayout(commonButtonsLayout(),viewDetails);
			viewDetailsHorizontalLayout.setWidth("100%");
			viewDetailsHorizontalLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
			//viewDetailsHorizontalLayout.setMargin(true);
			//viewDetailsHorizontalLayout.setComponentAlignment(viewDetails, Alignment.MIDDLE_RIGHT);
			
			VerticalLayout crmFlagButtonVerticalLayout = new VerticalLayout(viewDetailsHorizontalLayout, crmFlaggedComponents);

			lblInvestigationRequestDetails = new Label(
					"Investigation Request Details");
			lblInvestigationRequestDetails.setStyleName(Reindeer.LABEL_H2);

			txtRequestingRole = binder.buildAndBind("Requesting Role",
					"requestingRole", TextField.class);
			txtRequestingRole.setNullRepresentation("");
            txtRequestingRole.setValue(this.bean.getInvestigationRequestedRole());
			
			txtRequestorIdOrName = binder.buildAndBind("Requestor ID/Name",
					"requestorIdOrName", TextField.class);
			txtRequestorIdOrName.setNullRepresentation("");
			txtRequestorIdOrName.setValue(this.bean.getRequestedBy());

			txtAllocationTo = binder.buildAndBind("Allocation to",
					"allocationTo", TextField.class);
			txtAllocationTo.setRequired(true);
			txtAllocationTo
					.setValue(investigation.getAllocationTo() != null ? investigation
							.getAllocationTo().getValue() : "");
			

			requestingRoleFormLayout = new FormLayout(txtRequestingRole,
					txtRequestorIdOrName, txtAllocationTo);
			
			cmbReasonforIniInv = binder.buildAndBind("Reason for Initiating Investingation","reasonForInitiatingInvestSelectValue", ComboBox.class);
			if(bean.getClaimTypeId()!=null && bean.getClaimTypeId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
				cmbReasonforIniInv.setEnabled(true);
				isInvEnable =true;
			}
			else{
				cmbReasonforIniInv.setEnabled(false);
				isInvEnable =false;
			}
		
			//cmbReasonforIniInv.setValue(investigation.getReasonForInitiatingInv() != null ? investigation.getReasonForInitiatingInv() : "");
			
			BeanItemContainer<SelectValue> reasonForInitiateInvestContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			reasonForInitiateInvestContainer.addAll(this.processInvestigationInitiatedDto.getReasonForInitiatingInvestSelectValueList());
			cmbReasonforIniInv.setContainerDataSource(reasonForInitiateInvestContainer);
			cmbReasonforIniInv.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbReasonforIniInv.setItemCaptionPropertyId("value");
			
			if(this.processInvestigationInitiatedDto.getReasonForInitiatingInvestSelectValue() != null){
				cmbReasonforIniInv.setValue(this.processInvestigationInitiatedDto.getReasonForInitiatingInvestSelectValue());
			}
			/*BeanItemContainer<SelectValue> reasonForIniInvestContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			if(investigation.getReasonForInitiatingInv() !=null){
			MastersValue reasonForInitateInvest =  investigation.getReasonForInitiatingInv();
			reasonForIniInvestContainer.addBean(new SelectValue(reasonForInitateInvest.getKey(), reasonForInitateInvest.getValue()));
			}*/
			
			
			txtReasonForReferring = binder.buildAndBind("Reason for Referring",
					"reasonForReferring", TextArea.class);
			txtReasonForReferring.setMaxLength(4000);
			txtReasonForReferring.setNullRepresentation("");
			//txtReasonForReferring.setRequired(true);
			txtReasonForReferring.setValue(investigation
					.getReasonForReferring() != null ? investigation
					.getReasonForReferring() : "");
			txtReasonForReferring.setData(processInvestigationInitiatedDto);
			txtReasonForReferring.setId("referReason");
			/*handleRemarksPopup(txtReasonForReferring,null);
			txtReasonForReferring.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);	*/		
			txtReasonForReferring.setEnabled(false);
			
			
			cmbReasonforIniInv.addValueChangeListener( new ValueChangeListener() {
				
				@Override

				public void valueChange(ValueChangeEvent event) {

					SelectValue value = event.getProperty().getValue() != null ? (SelectValue) event.getProperty().getValue() : null;
					cmbReasonforIniInv.setValue(value);
					if(value != null && ((SelectValue)cmbReasonforIniInv.getValue()).getValue().equalsIgnoreCase("others")) {
							//unbindField(reasonforRefering);
						txtReasonForReferring.setEnabled(true);
						txtReasonForReferring.setRequired(true);
						//mandatoryFields.add(txtReasonForReferring);
					
					}else{
						txtReasonForReferring.setValue(investigation.getReasonForReferring() != null ? investigation.getReasonForReferring() : "");
						txtReasonForReferring.setEnabled(false);
						txtReasonForReferring.setRequired(false);
						//mandatoryFields.remove(txtReasonForReferring);
					}
				}
		 });
			
			txtTriggerPointsToFocus = binder.buildAndBind(
					"Trigger points to focus", "triggerPointsToFocus",
					TextArea.class);
			txtTriggerPointsToFocus.setMaxLength(4000);
			txtTriggerPointsToFocus.setNullRepresentation("");
			txtTriggerPointsToFocus.setRequired(true);
			txtTriggerPointsToFocus
					.setValue(investigation.getTriggerPoints() != null ? investigation
							.getTriggerPoints() : "");
			txtTriggerPointsToFocus.setData(processInvestigationInitiatedDto);
			txtTriggerPointsToFocus.setId("invTriggerfocus");
			handleRemarksPopup(txtTriggerPointsToFocus,null);
			txtTriggerPointsToFocus.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
			
			reasonForRefferingFormLayout = new FormLayout(cmbReasonforIniInv,txtReasonForReferring, txtTriggerPointsToFocus);

			requestingRoleHorizontalLayout = new HorizontalLayout(
					requestingRoleFormLayout, reasonForRefferingFormLayout);
			requestingRoleHorizontalLayout.setSpacing(false);
			requestingRoleHorizontalLayout.setMargin(true);
			requestingRoleHorizontalLayout.setWidth("100%");
			requestingRoleHorizontalLayout.setHeight("235px");
			btnApproveInvestigationRequest = new Button(
					"Approve Investigation Request");

			btnDisapproveInvestigationRequest = new Button(
					"Disapprove Investigation Request");

			btnHorizontalLayout = new HorizontalLayout(
					btnApproveInvestigationRequest,
					btnDisapproveInvestigationRequest);
			btnHorizontalLayout.setMargin(true);
			btnHorizontalLayout.setSpacing(true);

			txtInvestigationApprovedRemarks = binder.buildAndBind(
					"Investigation Approved Remarks",
					"investigationApprovedRemarks", TextArea.class);
			txtInvestigationApprovedRemarks.setMaxLength(4000);
			txtInvestigationApprovedRemarks.setData(processInvestigationInitiatedDto);
			txtInvestigationApprovedRemarks.setId("invApproveRemarks");
			handleRemarksPopup(txtInvestigationApprovedRemarks,null);
			txtInvestigationApprovedRemarks.setNullRepresentation("");
			txtInvestigationApprovedRemarks.setRequired(true);
			txtInvestigationApprovedRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
			
			cmbInvAllocationTo = binder.buildAndBind("Allocation to","invAllocationTo", ComboBox.class);
			
//			txtInvestigationNotRequiredRemarks = binder.buildAndBind(
//					"Investigation Not Required Remarks",
//					"investigationNotRequiredRemarks", TextArea.class);
			txtInvestigationNotRequiredRemarks = binder.buildAndBind(
					"Disapprove Investigation Remarks",
					"investigationNotRequiredRemarks", TextArea.class);
			txtInvestigationNotRequiredRemarks.setMaxLength(4000);
			txtInvestigationNotRequiredRemarks.setNullRepresentation("");
			txtInvestigationNotRequiredRemarks.setRequired(true);
			txtInvestigationNotRequiredRemarks.setId("invDisapproveRemarks");
			txtInvestigationNotRequiredRemarks.setData(processInvestigationInitiatedDto);
			handleRemarksPopup(txtInvestigationNotRequiredRemarks,null);
			txtInvestigationNotRequiredRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
			
			initiateFieldVisitRequestRadio = (OptionGroup) binder.buildAndBind(
					"Initiate Field Visit Request",
					"initiateFieldVisitRequestFlag", OptionGroup.class);
			
			initiateFieldVisitRequestRadio.addItems(getReadioButtonOptions());
			initiateFieldVisitRequestRadio.setItemCaption(true, "Yes");
			initiateFieldVisitRequestRadio.setItemCaption(false, "No");
			initiateFieldVisitRequestRadio.setStyleName("horizontal");
			initiateFieldVisitRequestRadio.setRequired(true);
			initiateFieldVisitRequestRadio.setData(claimDto.getKey());
			fvrFormLayout = new FormLayout(initiateFieldVisitRequestRadio);
			fvrVertLayout = new VerticalLayout(fvrFormLayout);
			
			triggerPtsTableObj = triggerPtsTable.get();
			triggerPtsTableObj.init();	    

			btnSubmit = new Button("Submit");
			btnCancel = new Button("Cancel");
			
			btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			btnSubmit.setWidth("-1px");
			btnSubmit.setHeight("-10px");
			
			btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
			btnCancel.setWidth("-1px");
			btnCancel.setHeight("-10px");

			submitHorizontalLayout = new HorizontalLayout(btnSubmit, btnCancel);
			submitHorizontalLayout.setMargin(true);
			submitHorizontalLayout.setSpacing(true);

			pnlLayout = new VerticalLayout(preauthIntimationDetailsCarousel,
					crmFlagButtonVerticalLayout,
					lblInvestigationRequestDetails,
					requestingRoleHorizontalLayout, btnHorizontalLayout,
					submitHorizontalLayout);
			pnlLayout.setComponentAlignment(submitHorizontalLayout,
					Alignment.MIDDLE_CENTER);
		//	pnlLayout.setMargin(false);
			pnlLayout.setSpacing(true);
			//pnlLayout.setHeight("5px");

			mainPanel = new Panel();
			mainPanel.setContent(pnlLayout);
			mainLayout = new VerticalLayout(mainPanel);
			setCompositionRoot(mainLayout);
			addListener();
			setReadonly(true);
			mandatoryFields.add(initiateFieldVisitRequestRadio);
		}
	}

	private void setReadonly(Boolean flag) {
		txtInvestigationNo.setReadOnly(flag);
		txtRequestingRole.setReadOnly(flag);
		txtRequestorIdOrName.setReadOnly(flag);
		//txtReasonForReferring.setReadOnly(flag);
		txtTriggerPointsToFocus.setReadOnly(flag);
		txtAllocationTo.setReadOnly(flag);
	}
	
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
	}

	@SuppressWarnings("serial")
	private void addListener() {

		btnViewInvestigationDetails
				.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						if (claimDto != null) {
							viewDetails.getInvestigationDetails(claimDto.getNewIntimationDto().getIntimationId(), false);
//							viewDeTAILS.GETVIEWINVESTIGATIONDETAILS(CLAIMDTO
//									.GEtKey(), false);
						}
					}
				});

		btnApproveInvestigationRequest
				.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						isDisapprove = false;
						isButtonClicked = true;
						try {
							pnlLayout
									.removeComponent(txtInvestigationNotRequiredRemarks);
							pnlLayout.removeComponent(fvrVertLayout);
							
						} catch (Exception e) {

						}
						try {
							pnlLayout
									.removeComponent(txtInvestigationApprovedRemarks);
							pnlLayout
							.removeComponent(cmbInvAllocationTo);
							BeanItemContainer<SelectValue> beanItemContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
							beanItemContainer.addAll(bean.getInvAllocationToList());
							cmbInvAllocationTo.setContainerDataSource(beanItemContainer);
							cmbInvAllocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
							cmbInvAllocationTo.setItemCaptionPropertyId("value");
						} catch (Exception e) {

						}
						pnlLayout.addComponent(cmbInvAllocationTo,5);
						pnlLayout.addComponent(txtInvestigationApprovedRemarks,6);
						
						unbindField(txtInvestigationNotRequiredRemarks);
//						unbindField(fvrTriggerPoints);
						unbindField(cmbAllocationTo);
						//unbindField(cmbFvrAssignTo);
						unbindField(cmbFvrPriority);
						unbindField(initiateFieldVisitRequestRadio);
					}
				});

		btnDisapproveInvestigationRequest
				.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						isDisapprove = true;
						isButtonClicked = true;
						try {
							pnlLayout.removeComponent(txtInvestigationApprovedRemarks);
							pnlLayout.removeComponent(cmbInvAllocationTo);
						} catch (Exception e) {

						}
						try {
							pnlLayout.removeComponent(txtInvestigationNotRequiredRemarks);
							pnlLayout.removeComponent(fvrVertLayout);
						} catch (Exception e) {

						}
						
						unbindField(txtInvestigationApprovedRemarks);
						unbindField(cmbInvAllocationTo);
						
						pnlLayout.addComponent(
								txtInvestigationNotRequiredRemarks, 5);
						pnlLayout.addComponent(fvrVertLayout,6);
					}
				});

		btnCancel.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				DBCalculationService dbCalculationService = new DBCalculationService();
				VaadinSession session =VaadinSession.getCurrent();
				Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
			}
		});
		
		initiateFieldVisitRequestRadio
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				Long claimKey = null;
				if (event.getProperty() != null
						&& event.getProperty().getValue().toString() == "true") {
					isChecked = true;
					claimKey = (Long)((OptionGroup)event.getProperty()).getData();
				}
				else{
					isChecked = false;
				}
				bean.setIsAllowInitiateFVR(preauthService.getFVRStatusByRodKey(bean.getKey()));
				
				if(bean.getIsAllowInitiateFVR() != null && !bean.getIsAllowInitiateFVR()){

					fireViewEvent(
							ProcessInvestigationInitiatedPresenter.FIELD_VISIT_RADIO_CHANGED,
							isChecked,claimKey,claimDto.getNewIntimationDto().getIntimationId(),processInvestigationInitiatedDto.getUserName(),initiateFieldVisitRequestRadio);				
				}
				else
				{
					showFVRErrorMessage("FVR request is in process cannot initiate another request");
				}
				
				}
				
		});

		btnSubmit.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
					if ((txtInvestigationApprovedRemarks.getValue() != null && !txtInvestigationApprovedRemarks
						.getValue().equals(""))
						|| (txtInvestigationNotRequiredRemarks.getValue() != null && !txtInvestigationNotRequiredRemarks
								.getValue().equals(""))){
					processInvestigationInitiatedDto.setInvestigationApprovedRemarks(txtInvestigationApprovedRemarks.getValue());
					processInvestigationInitiatedDto.setInvestigationNotRequiredRemarks(txtInvestigationNotRequiredRemarks.getValue());
					
					if(validatePage()){
						
						if(!isDisapprove){
							processInvestigationInitiatedDto.setInitiateFieldVisitRequestFlag(false);
						}
					
					fireViewEvent(
							ProcessInvestigationInitiatedPresenter.SUBMIT_CLICK,
							processInvestigationInitiatedDto, investigation,bean,isDisapprove);
					}
				}else{
					
					String msg = null;
					if(!isButtonClicked)
					{
						msg = "Please Select any one of the action to proceed";
					}
					else
					{
						msg = "Please enter the Investigation Remarks";
					}
					
					ConfirmDialog dialog = ConfirmDialog.show(getUI(),
							msg,
							new ConfirmDialog.Listener() {

								public void onClose(ConfirmDialog dialog) {
									if (dialog.isConfirmed()) {
										
									} else {
										dialog.close();
									}
								}
							});
					dialog.setStyleName(Reindeer.WINDOW_BLACK);
					dialog.getCancelButton().setVisible(false);
				}
					
			}
		});
		
		btnCancel.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),
						"Are you sure want to Cancel",
						new ConfirmDialog.Listener() {

							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									
									VaadinSession session = getSession();
									SHAUtils.releaseHumanTask(bean.getUsername(), bean.getPassword(), bean.getTaskNumber(),session);
									
									fireViewEvent(MenuItemBean.SEARCH_PROCESS_INVESTIGATION_INITIATED, null);
								} else {
									dialog.close();
								}
							}
						});
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
		
		

	}
	
	public void generateFieldsBasedOnFieldVisit(Boolean isChecked,
			Object dropdowValues, Object assignToValues, Object priorityValues) {
		if (isChecked) {
			
			if(processInvestigationInitiatedDto.getInitiateFieldVisitRequestFlag() != null && processInvestigationInitiatedDto.getInitiateFieldVisitRequestFlag() && processInvestigationInitiatedDto.getFvrTriggerPtsList() != null && !processInvestigationInitiatedDto.getFvrTriggerPtsList().isEmpty()){
				triggerPtsTableObj.setTableList(processInvestigationInitiatedDto.getFvrTriggerPtsList());
			}
			else{
				ViewFVRDTO trgptsDto = null;
		    	List<ViewFVRDTO> trgptsList = new ArrayList<ViewFVRDTO>();
		    	for(int i = 1; i<=5;i++){
		    		trgptsDto = new ViewFVRDTO();
		    		trgptsDto.setRemarks("");
		    		trgptsList.add(trgptsDto);
		    	}
		    	triggerPtsTableObj.setTableList(trgptsList);
			}
			
//			unbindField(fvrTriggerPoints);
			unbindField(cmbAllocationTo);
			//unbindField(cmbFvrAssignTo);
			unbindField(cmbFvrPriority);
			
//			fvrTriggerPoints = (TextArea) binder.buildAndBind(
//					"FVR Trigger Points", "fvrTriggerPoints", TextArea.class);
//			fvrTriggerPoints.setMaxLength(4000);
//			fvrTriggerPoints.setWidth("100%");
			
			cmbAllocationTo = (ComboBox) binder.buildAndBind("Allocation To",
					"fvrAllocationTo", ComboBox.class);
			cmbAllocationTo
					.setContainerDataSource((BeanItemContainer<SelectValue>) dropdowValues);
			cmbAllocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbAllocationTo.setItemCaptionPropertyId("value");
			
			if (this.processInvestigationInitiatedDto.getFvrAllocationTo() != null) {
				this.cmbAllocationTo.setValue(this.processInvestigationInitiatedDto
						.getFvrAllocationTo());
			}
			
			/*cmbFvrAssignTo = (ComboBox) binder.buildAndBind("Assign To",
					"assignTo", ComboBox.class);
			cmbFvrAssignTo
					.setContainerDataSource((BeanItemContainer<SelectValue>) assignToValues);
			cmbFvrAssignTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbFvrAssignTo.setItemCaptionPropertyId("value");*/
			
			/*if(this.processInvestigationInitiatedDto.getAssignTo() != null){
				this.cmbFvrAssignTo.setValue(this.processInvestigationInitiatedDto.getAssignTo());
			}*/
			
			cmbFvrPriority = (ComboBox) binder.buildAndBind("Priority",
					"priority", ComboBox.class);
			cmbFvrPriority
					.setContainerDataSource((BeanItemContainer<SelectValue>) priorityValues);
			cmbFvrPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbFvrPriority.setItemCaptionPropertyId("value");
			
			if(this.processInvestigationInitiatedDto.getPriority() != null){
				this.cmbFvrPriority.setValue(this.processInvestigationInitiatedDto.getPriority() != null);
			}
			
			//setRequiredAndValidation(cmbFvrAssignTo);
			setRequiredAndValidation(cmbFvrPriority);
//			setRequiredAndValidation(fvrTriggerPoints);
			setRequiredAndValidation(cmbAllocationTo);

//			mandatoryFields.add(fvrTriggerPoints);
			mandatoryFields.add(cmbAllocationTo);
			//mandatoryFields.add(cmbFvrAssignTo);
			mandatoryFields.add(cmbFvrPriority);

			
			fvrFormLayout.addComponent(cmbAllocationTo);
			//fvrFormLayout.addComponent(cmbFvrAssignTo);
			fvrFormLayout.addComponent(cmbFvrPriority);
			
			if(null != triggerPtsTable ){
				fvrVertLayout.removeComponent(triggerPtsTableObj);
			}
			fvrVertLayout.addComponent(triggerPtsTableObj);
			
//			fvrFormLayout.addComponent(fvrTriggerPoints);

		} else {

//			unbindField(fvrTriggerPoints);
			unbindField(cmbAllocationTo);
			//unbindField(cmbFvrAssignTo);
			unbindField(cmbFvrPriority);
			
			if(null != triggerPtsTable ){
				fvrVertLayout.removeComponent(triggerPtsTableObj);
			}
			
			if(null != cmbAllocationTo){
				fvrFormLayout.removeComponent(cmbAllocationTo);
			}
			/*if(null != cmbFvrAssignTo) {
				fvrFormLayout.removeComponent(cmbFvrAssignTo);
			}*/
			
			if(null != cmbFvrPriority) {
				fvrFormLayout.removeComponent(cmbFvrPriority);
			}
//			mandatoryFields.remove(fvrTriggerPoints);
			mandatoryFields.remove(cmbAllocationTo);
			//mandatoryFields.remove(cmbFvrAssignTo);
			mandatoryFields.remove(cmbFvrPriority);
			
		}
	}
	
	private void setRequiredAndValidation(Component component) {
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}
	
	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer("");
		
		if(null != initiateFieldVisitRequestRadio.getValue() && initiateFieldVisitRequestRadio.getValue().toString() == "true"){
			if(triggerPtsTable != null){
				if(!triggerPtsTableObj.isValid()){
					eMsg.append("Please Provide atleast one FVR Trigger Point.</br>");
					eMsg.append("FVR Trigger Points size will be Max. of 600.</br>");
					hasError = true;
				}
				else{
					processInvestigationInitiatedDto.setFvrTriggerPtsList(triggerPtsTableObj.getValues());					
					processInvestigationInitiatedDto.setFvrTriggerPoints(triggerPtsTableObj.getTriggerRemarks());
				}
			}
		}
		
		 if(null != cmbReasonforIniInv.getValue() && cmbReasonforIniInv.getValue().toString().equalsIgnoreCase("Others")) 
			{
				if(txtReasonForReferring.getValue() == null || ("").equalsIgnoreCase(txtReasonForReferring.getValue()) || 
						txtReasonForReferring.getValue().isEmpty())	
				{
					hasError = true;
					eMsg.append("Please Enter Reason For Refering.</br>");
				}
			}
		 
		 if(isInvEnable){
			 if(cmbReasonforIniInv !=null && cmbReasonforIniInv.getValue() == null) 
			 {
				 hasError = true;
				 eMsg.append("Please Select the Reason for Initiating Investigation.</br>");
			 }
		 }
		 
		if (!this.binder.isValid()) {
			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}
		}
		
		
		
		if (hasError && !eMsg.toString().isEmpty()) {
			setRequired(true);
			showErrorMessage(eMsg.toString());

			hasError = true;
			return !hasError;
		}else{
			try {
				this.binder.commit();
				if(initiateFieldVisitRequestRadio != null && initiateFieldVisitRequestRadio.getValue() != null && initiateFieldVisitRequestRadio.getValue().toString().equalsIgnoreCase("true")){
					processInvestigationInitiatedDto.setInitiateFieldVisitRequestFlag(true);	
				}
				else{
					processInvestigationInitiatedDto.setInitiateFieldVisitRequestFlag(false);
				}
				
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return true;
	}

	@Override
	public void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg.toString(), ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(label);
		dialog.setResizable(false);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	@SuppressWarnings("unused")
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void finalResult(Boolean flag) {
       Label successLabel = new Label("<b style = 'color: black;'>Process Investigation Initiated Successfully!!! </b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Home Page");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
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
				toolbar.countTool();
				fireViewEvent(MenuItemBean.SEARCH_PROCESS_INVESTIGATION_INITIATED, null);
				
			}
		});
	}
	
	private void unbindField(Field<?> field) {
		if (field != null) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field != null && propertyId != null) {
				this.binder.unbind(field);
			}

		}
	}

	@Override
	public void genertateFieldsBasedOnFieldVisit(Boolean isChecked,
			BeanItemContainer<SelectValue> allocationTo,
			BeanItemContainer<SelectValue> assignTo,
			BeanItemContainer<SelectValue> priority,
			OptionGroup initiateFvrOptionField) {
		if (isChecked) {
			
//			unbindField(fvrTriggerPoints);
			unbindField(cmbAllocationTo);
			//unbindField(cmbFvrAssignTo);
			unbindField(cmbFvrPriority);
			
//			fvrTriggerPoints = (TextArea) binder.buildAndBind(
//					"FVR Trigger Points", "fvrTriggerPoints", TextArea.class);
//			fvrTriggerPoints.setMaxLength(4000);
//			fvrTriggerPoints.setWidth("100%");
			
			if(processInvestigationInitiatedDto.getInitiateFieldVisitRequestFlag() != null && processInvestigationInitiatedDto.getInitiateFieldVisitRequestFlag() && processInvestigationInitiatedDto.getFvrTriggerPtsList() != null && !processInvestigationInitiatedDto.getFvrTriggerPtsList().isEmpty()){
				triggerPtsTableObj.setTableList(processInvestigationInitiatedDto.getFvrTriggerPtsList());
			}
			else{
				ViewFVRDTO trgptsDto = null;
		    	List<ViewFVRDTO> trgptsList = new ArrayList<ViewFVRDTO>();
		    	for(int i = 1; i<=5;i++){
		    		trgptsDto = new ViewFVRDTO();
		    		trgptsDto.setRemarks("");
		    		trgptsList.add(trgptsDto);
		    	}
		    	triggerPtsTableObj.setTableList(trgptsList);
			}
			
			cmbAllocationTo = (ComboBox) binder.buildAndBind("Allocation To",
					"fvrAllocationTo", ComboBox.class);
			cmbAllocationTo
					.setContainerDataSource(allocationTo);
			cmbAllocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbAllocationTo.setItemCaptionPropertyId("value");
			
			if (this.processInvestigationInitiatedDto.getFvrAllocationTo() != null) {
				this.cmbAllocationTo.setValue(this.processInvestigationInitiatedDto
						.getFvrAllocationTo());
			}
			
			/*cmbFvrAssignTo = (ComboBox) binder.buildAndBind("Assign To",
					"assignTo", ComboBox.class);
			cmbFvrAssignTo
					.setContainerDataSource(assignTo);
			cmbFvrAssignTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbFvrAssignTo.setItemCaptionPropertyId("value");*/
			
			/*if(this.processInvestigationInitiatedDto.getAssignTo() != null){
				this.cmbFvrAssignTo.setValue(this.processInvestigationInitiatedDto.getAssignTo());
			}*/
			
			cmbFvrPriority = (ComboBox) binder.buildAndBind("Priority",
					"priority", ComboBox.class);
			cmbFvrPriority
					.setContainerDataSource(priority);
			cmbFvrPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbFvrPriority.setItemCaptionPropertyId("value");
			
			if(this.processInvestigationInitiatedDto.getPriority() != null){
				this.cmbFvrPriority.setValue(this.processInvestigationInitiatedDto.getPriority() != null);
			}
			
			//setRequiredAndValidation(cmbFvrAssignTo);
			setRequiredAndValidation(cmbFvrPriority);
//			setRequiredAndValidation(fvrTriggerPoints);
			setRequiredAndValidation(cmbAllocationTo);

//			mandatoryFields.add(fvrTriggerPoints);
			mandatoryFields.add(cmbAllocationTo);
			//mandatoryFields.add(cmbFvrAssignTo);
			mandatoryFields.add(cmbFvrPriority);
			
			fvrFormLayout.removeAllComponents();
			
			fvrFormLayout.addComponent(initiateFieldVisitRequestRadio);
			fvrFormLayout.addComponent(cmbAllocationTo);
			//fvrFormLayout.addComponent(cmbFvrAssignTo);
			fvrFormLayout.addComponent(cmbFvrPriority);
			
			if(null != triggerPtsTable ){
				fvrVertLayout.removeComponent(triggerPtsTableObj);
			}
			fvrVertLayout.addComponent(triggerPtsTableObj);
			
			//showFVRErrorMessage("<BR>FVR request is in process, cannot initiate another request.",initiateFvrOptionField);
//			fvrFormLayout.addComponent(fvrTriggerPoints);

		} else {

//			unbindField(fvrTriggerPoints);
			unbindField(cmbAllocationTo);
			//unbindField(cmbFvrAssignTo);
			unbindField(cmbFvrPriority);
			if(null != triggerPtsTable ){
				fvrVertLayout.removeComponent(triggerPtsTableObj);
			}
			if(null != cmbAllocationTo){
				fvrFormLayout.removeComponent(cmbAllocationTo);
			}
			/*if(null != cmbFvrAssignTo) {
				fvrFormLayout.removeComponent(cmbFvrAssignTo);
			}*/
			
			if(null != cmbFvrPriority) {
				fvrFormLayout.removeComponent(cmbFvrPriority);
			}
//			mandatoryFields.remove(fvrTriggerPoints);
			mandatoryFields.remove(cmbAllocationTo);
			//mandatoryFields.remove(cmbFvrAssignTo);
			mandatoryFields.remove(cmbFvrPriority);
//			showErrorMessage("<BR>FVR request is in process, cannot initiate another request.");
			
		}
	}
	
public  void handleRemarksPopup(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {
		
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
		ShortcutListener listener =  new ShortcutListener("Redraft Remarks",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				ProcessInvestigationInitiatedDto  processInvInititatedDto = (ProcessInvestigationInitiatedDto) txtFld.getData();
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
				String remarksValue = "";	
				
				if(("referReason").equalsIgnoreCase((txtFld.getId())) || ("invTriggerfocus").equalsIgnoreCase((txtFld.getId()))){
					if(("referReason").equalsIgnoreCase((txtFld.getId()))){
						
						remarksValue = processInvInititatedDto.getReasonForReferring() != null ? processInvInititatedDto.getReasonForReferring() : "";
//						txtArea.setRows(processInvInititatedDto.getReasonForReferring() != null ? (processInvInititatedDto.getReasonForReferring().length()/80 >= 25 ? 25 : ((processInvInititatedDto.getReasonForReferring().length()/80)%25)+1) : 25);
					}
					if(("invTriggerfocus").equalsIgnoreCase((txtFld.getId()))){
						remarksValue = processInvInititatedDto.getTriggerPointsToFocus() != null ? processInvInititatedDto.getTriggerPointsToFocus() : "";
//						txtArea.setRows(processInvInititatedDto.getTriggerPointsToFocus() != null ? (processInvInititatedDto.getTriggerPointsToFocus().length()/80 >= 25 ? 25 : ((processInvInititatedDto.getTriggerPointsToFocus().length()/80)%25)+1) : 25);
					}
					txtArea.setReadOnly(true);
				
//					String splitArray[] = remarksValue.split("\n");
					String splitArray[] = remarksValue.split("[\n*|.*]");
					
					if(splitArray != null && splitArray.length > 0 && splitArray.length > 25){
						txtArea.setRows(25);
					}
					else{
						txtArea.setRows(splitArray.length);
					}
				}
				else{
					txtArea.setReadOnly(false);
					txtArea.setRows(25);
				}					
				
				txtArea.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						
						if(("invApproveRemarks").equalsIgnoreCase((txtFld.getId())) || ("invDisapproveRemarks").equalsIgnoreCase((txtFld.getId()))){
							
							txtFld.setValue(((TextArea)event.getProperty()).getValue());		
							
						}	
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				final Window dialog = new Window();
				
				String strCaption = "";
				
				if(("referReason").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Reason for Referring";
				}
				if(("invTriggerfocus").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Trigger points to focus";
				}
				if(("invApproveRemarks").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Investigation Approved Remarks";
				}
				if(("invDisapproveRemarks").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Disapprove Investigation Remarks";
				}
				
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
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getRedraftRemarks());
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
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getRedraftRemarks());
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}
	
	private void showFVRErrorMessage(String eMsg) {
		Label label = new Label(eMsg.toString(), ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(label);
		dialog.setResizable(false);
		dialog.show(getUI().getCurrent(), null, true);
		initiateFieldVisitRequestRadio.setValue(Boolean.FALSE);
		
	}
	private void alertMessageForClaimCount(Long claimCount){
		
		String msg = SHAConstants.CLAIM_COUNT_MESSAGE+claimCount;

		String additionalMessage = SHAConstants.ADDITIONAL_MESSAGE;
		
//		Button alertIcon = new Button("");
//		alertIcon.setIcon(new ThemeResource("images/alert.png"));
//		alertIcon.setEnabled(false);
//        alertIcon.setStyleName("ling");
		
//		alertIcon.addStyleName(ValoTheme.BUTTON_BORDERLESS);

   		Label successLabel = new Label(
				"<b style = 'color: black;'>"+msg+"</b>",
				ContentMode.HTML);
		
   		if(this.bean.getClaimCount() >2){
	   		successLabel = new Label(
					"<b style = 'color: black;'>"+msg+"<br>"
							+ additionalMessage+"</b>",
					ContentMode.HTML);
   		}
//   		successLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//   		successLabel.addStyleName(ValoTheme.LABEL_BOLD);
   		successLabel.addStyleName(ValoTheme.LABEL_H3);
   		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		HorizontalLayout mainHor = new HorizontalLayout(successLabel);
		TextField dummyField = new TextField();
		dummyField.setEnabled(false);
		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
   		VerticalLayout firstForm = new VerticalLayout(dummyField,mainHor,homeButton);
//   		firstForm.setComponentAlignment(mainHor, Alignment.MIDDLE_CENTER);
   		firstForm.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
//   		firstForm.setHeight("1003px");
		Panel panel = new Panel();
		panel.setContent(firstForm);
		
		if(this.bean.getClaimCount() > 3){
			panel.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			panel.addStyleName("girdBorder2");
		}
		
//		panel.setHeight("143px");
		panel.setSizeFull();
		
		
		final Window popup = new com.vaadin.ui.Window();
		popup.setWidth("45%");
		popup.setHeight("20%");
//		popup.setCaption("Alert");
//		popup.setContent( viewDocumentDetailsPage);
		popup.setContent(panel);
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
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				popup.close();
				if(bean.getIsPEDInitiated()) {
					alertMessageForPED();
				}
				else if(!bean.getSuspiciousPopupMap().isEmpty()){
					suspiousPopupMessage();
				}
				else  if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else  if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}
				else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
				}
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public Boolean alertMessageForPED() {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PED_RAISE_MESSAGE + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		/*HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");*/

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				bean.setIsPEDInitiated(false);
				if(!bean.getSuspiciousPopupMap().isEmpty()){
					suspiousPopupMessage();
				}
				else  if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else  if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}
				else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
					poupMessageForProduct();
				}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
				}
			
			}
		});
		
		return true;
	}
	
	 public void suspiousPopupMessage() {
		  final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(true);
			dialog.setResizable(false);
			dialog.setModal(true);
			
			Button okButton = new Button("OK");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					bean.setIsPopupMessageOpened(true);
					dialog.close();
					if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else  if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}
					else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
					}
				}
			});
			
			/*HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setSpacing(true);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);*/
			VerticalLayout layout = new VerticalLayout();
			Map<String, String> popupMap = bean.getSuspiciousPopupMap();
			for (Map.Entry<String, String> entry : popupMap.entrySet()) {
				Label label = new Label(entry.getValue(), ContentMode.HTML);
				label.setWidth(null);
			   layout.addComponent(label);
			   layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
			   layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
			}
			layout.addComponent(okButton);
			layout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			layout.setMargin(true);
			layout.setSpacing(true);
			dialog.setContent(layout);
			dialog.setWidth("30%");
			this.bean.setIsPopupMessageOpened(true);
			dialog.show(getUI().getCurrent(), null, true);
		}
	  
	 public void nonPreferredPopupMessage() {
		  final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(true);
			dialog.setResizable(false);
			dialog.setModal(true);
			
			Button okButton = new Button("OK");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			okButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					bean.setIsPopupMessageOpened(true);
					dialog.close();
					if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}
					else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
					}
				
				}
			});
			
			/*HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setSpacing(true);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);*/
			VerticalLayout layout = new VerticalLayout();
			Map<String, String> popupMap = bean.getNonPreferredPopupMap();
			for (Map.Entry<String, String> entry : popupMap.entrySet()) {
				Label label = new Label(entry.getValue(), ContentMode.HTML);
				label.setWidth(null);
			   layout.addComponent(label);
			   layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
			   layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
			}
			layout.addComponent(okButton);
			layout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			layout.setMargin(true);
			layout.setSpacing(true);
			dialog.setContent(layout);
			dialog.setWidth("30%");
			this.bean.setIsPopupMessageOpened(true);
			dialog.show(getUI().getCurrent(), null, true);
		}
	
	 public void get64VbChequeStatusAlert() {
	   		Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.VB64STATUSALERT + "</b>",
					ContentMode.HTML);
	   		//final Boolean isClicked = false;
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
			/*HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");*/

			final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
						dialog.close();
						if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
							poupMessageForProduct();
						}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
							StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
						}
				}
			});
			
		}
	 
	 public void poupMessageForProduct() {
		  final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(true);
			dialog.setResizable(false);
			dialog.setModal(true);
			
			Button okButton = new Button("OK");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					bean.setIsPopupMessageOpened(true);
					dialog.close();
					 if(bean.getPreauthDTO().getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
					}
				}
			});
			
			/*HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setSpacing(true);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);*/
			VerticalLayout layout = new VerticalLayout();
			Map<String, String> popupMap = bean.getPopupMap();
			for (Map.Entry<String, String> entry : popupMap.entrySet()) {
				if(entry.getKey().equals(SHAConstants.BREAK_INSURANCE_MESSAGE)){
					layout.addComponent(new Label(entry.getValue(), ContentMode.HTML));
				   layout.addComponent(new Label(entry.getKey(), ContentMode.HTML));
				}
			}
			layout.addComponent(okButton);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.setWidth("30%");
			bean.setIsPopupMessageOpened(true);
			dialog.show(getUI().getCurrent(), null, true);
		}
	 
		public void getGeoBasedOnCPU() {
			 
			 Label successLabel = new Label(
						"<b style = 'color: red;'>" + SHAConstants.GEO_PANT_HOSP_ALERT_MESSAGE + "</b>",
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
				dialog.show(getUI().getCurrent(), null, true);

				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
							dialog.close();
							
							if(bean.getClaimCount() >3){
								alertMessageForClaimCount(bean.getClaimCount());
							}
							else if(bean.getIsPEDInitiated()) {
								alertMessageForPED();
							}
							else if(!bean.getSuspiciousPopupMap().isEmpty()){
								suspiousPopupMessage();
							}
							else  if(!bean.getNonPreferredPopupMap().isEmpty()){
								nonPreferredPopupMessage();
							}
							else  if(bean.getIs64VBChequeStatusAlert()){
								get64VbChequeStatusAlert();
							}
							else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
								poupMessageForProduct();
							}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
								StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
							}
					}
				});
				
			}
		
		@Override
		public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
			 rewardRecognitionRequestViewObj.setsubCategoryValues(selectValueContainer, subCategory, value);
		 }
		 
		 @Override
		 public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
			 rewardRecognitionRequestViewObj.setsourceValues(selectValueContainer, source, value);
		 }
}
