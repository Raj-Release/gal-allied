package com.shaic.reimbursement.draftinvesigation;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
public class DraftInvestigatorUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TextField txtRequestingRole;

	private TextField txtRequestorIdOrName;

	private ComboBox cmbAllocationTo;
	
	private ComboBox cmbReasonforIniInv;

	private TextArea txtInvestigatingApprovedRemarks;

	private TextArea txtReasonForReferring;

	private TextArea txtTriggerPointsForFocus;
	
	private String autoTrgpts;

	private FormLayout requestingRoleFormLayout;

	private FormLayout reasonFormLayout;

	private HorizontalLayout requestingHorizontalLayout;

	// private ComboBox cmbAllocationTo;
	private TextField ipNo;
	
	private Boolean isInvEnable = false;

	private TextArea txtClaimBackgroundDetails;

	private TextArea txtInvestigationCaseFacts;

	private FormLayout investigationFormLayout;

	private VerticalLayout mainLayout;

	private GWizard wizard;

	private BeanFieldGroup<DraftInvestigatorDto> binder;

	private DraftInvestigatorDto bean;
	
	@EJB
	private MasterService masterService;

//	@Inject
//	private Instance<DraftTriggerPointsToFocusDetailsTable> draftTriggerPointsTableInstance;
//	
//	private DraftTriggerPointsToFocusDetailsTable draftTriggerPointsTableInstanceObj;
	
	@Inject
	private Instance<RevisedDraftInvTriggerPointsTable> draftTriggerPointsTableInstance;
	
	private RevisedDraftInvTriggerPointsTable draftTriggerPointsTableInstanceObj;
	
	private VerticalLayout vLayout ;
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<DraftInvestigatorDto>(
				DraftInvestigatorDto.class);
		/*bean.setReasonForReferring( investigation
		.getReasonForReferring() != null ? investigation
		.getReasonForReferring() : "");
		
		//Reason for Initiate Inv
		SelectValue selectValueforInInv = new SelectValue();
		if(investigation.getReasonForInitiatingInv() !=null){
		selectValueforInInv.setId(investigation.getReasonForInitiatingInv().getKey());
		selectValueforInInv.setValue(investigation.getReasonForInitiatingInv().getValue());
		bean.setReasonForIniInvestValue(investigation.getReasonForInitiatingInv().getValue());
		bean.setReasonForInitiatingInvestSelectValue(selectValueforInInv);
		bean.setReasonForIniInvestId(investigation.getReasonForInitiatingInv().getKey());
		}*/
		
		BeanItemContainer<SelectValue> reasonforIniInvContainer = masterService.getSelectValueContainer(ReferenceTable.REASON_FOR_INITIATE_INVESTIGATION);
		List<SelectValue> reasonforIniInvList = reasonforIniInvContainer.getItemIds();
		
		
		bean.setReasonForInitiatingInvestSelectValueList(reasonforIniInvContainer.getItemIds());
		bean.setReasonForInitiatingInvestIdList(reasonforIniInvList);
		
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		this.binder.setItemDataSource(bean);
	}

	public void init(DraftInvestigatorDto bean, GWizard wizard) {
		this.bean = bean;
		autoTrgpts = bean.getTriggerPointsForFocus() != null ? bean.getTriggerPointsForFocus() : "";
		this.wizard = wizard;
		vLayout = new VerticalLayout();
		
		draftTriggerPointsTableInstanceObj = draftTriggerPointsTableInstance.get();

		draftTriggerPointsTableInstanceObj.init("");
		//draftTriggerPointsTableInstanceObj.setTableList();
	}

	public Component getContent() {
		//this.wizard = wizard;
		
		// R1152
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
		else if(!bean.getNonPreferredPopupMap().isEmpty()){
			nonPreferredPopupMessage();
		}
		else if(bean.getIs64VBChequeStatusAlert()){
			get64VbChequeStatusAlert();
		}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
			poupMessageForProduct();
		}else if(bean.getPreauthDTO().getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
	    } 
		
		if(SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getClaimDto().getNewIntimationDto().getInsuredDeceasedFlag())) {
			
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
		String agentpercentage = masterService.getAgentPercentage(ReferenceTable.ICR_AGENT);
		String smpercentage = masterService.getAgentPercentage(ReferenceTable.SM_AGENT);
		if((bean.getPreauthDTO().getIcrAgentValue() != null && !bean.getPreauthDTO().getIcrAgentValue().isEmpty() 
				&& (Integer.parseInt(bean.getPreauthDTO().getIcrAgentValue()) >= Integer.parseInt(agentpercentage))) 
				|| bean.getPreauthDTO().getSmAgentValue() != null && !bean.getPreauthDTO().getSmAgentValue().isEmpty() 
						&& (Integer.parseInt(bean.getPreauthDTO().getSmAgentValue()) >= Integer.parseInt(smpercentage))){
			SHAUtils.showICRAgentAlert(bean.getPreauthDTO().getIcrAgentValue(), agentpercentage, bean.getPreauthDTO().getSmAgentValue(), smpercentage);
		}
		
		DBCalculationService dbService = new DBCalculationService();
		Boolean fraudFlag = dbService.getFraudFlag(bean.getPreauthDTO().getNewIntimationDTO().getIntimationId(),
				bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getPolicyNumber(),bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalCode(),
				bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalIrdaCode(),bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getAgentCode());
		if(fraudFlag)
		{
			if(bean.getPreauthDTO().getNewIntimationDTO().getIntimationId() != null)
			{
				Boolean result = dbService.getFraudAlertValue(bean.getPreauthDTO().getNewIntimationDTO().getIntimationId());
				if(result)
				{
					SHAUtils.showMessageBoxWithCaption("Suspicious found in Intimation "+bean.getPreauthDTO().getNewIntimationDTO().getIntimationId(),"Information");
				}
			}
			if(bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getPolicyNumber() != null)
			{
				Boolean result = dbService.getFraudAlertValue(bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getPolicyNumber());
				if(result)
				{
					SHAUtils.showMessageBoxWithCaption("Suspicious found in Policy "+bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getPolicyNumber(),"Information");
				}
			}
			if(bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalCode() != null)
			{
				Boolean result = dbService.getFraudAlertValue(bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalCode());
				if(result)
				{
					SHAUtils.showMessageBoxWithCaption("Suspicious found in Hospital "+bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalCode(),"Information");
				}
			}
			if(bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalIrdaCode() != null)
			{
				Boolean result = dbService.getFraudAlertValue(bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalIrdaCode());
				if(result)
				{
					SHAUtils.showMessageBoxWithCaption("Suspicious found in Hospital IRDA "+bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalIrdaCode(),"Information");
				}
			}
			if(bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getAgentCode() != null)
			{
				Boolean result = dbService.getFraudAlertValue(bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getAgentCode());
				if(result)
				{
					SHAUtils.showMessageBoxWithCaption("Suspicious found in Intermediary "+bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getAgentCode(),"Information");
				}
			}
		}
		
		initBinder();
		txtRequestingRole = new TextField("Requesting Role");
		txtRequestingRole.setValue(bean.getRequestingRole());
		txtRequestingRole.setNullRepresentation("");
		txtRequestingRole.setEnabled(false);
		
		txtRequestorIdOrName = new TextField("Requestor ID/Name");
		txtRequestorIdOrName.setValue(bean.getRequestroIdOrName());
		txtRequestorIdOrName.setNullRepresentation("");
		txtRequestorIdOrName.setEnabled(false);
		
		cmbAllocationTo = new ComboBox("Allocation to");
		cmbAllocationTo.setRequired(false);
		cmbAllocationTo.setEnabled(false);
		
		cmbReasonforIniInv = new ComboBox("Reason For Initiating Investigation");
		//cmbReasonforIniInv.setRequired(false);
		
		if(bean.getClaimDto().getClaimType()!=null && bean.getClaimDto().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
			cmbReasonforIniInv.setEnabled(true);
			isInvEnable = true;
		}
		else{
			cmbReasonforIniInv.setEnabled(false);
			isInvEnable =false;
		}
		
		
		txtInvestigatingApprovedRemarks = new TextArea(
		"Investigation Approved\nRemarks");
		txtInvestigatingApprovedRemarks.setId("invApproveRemarks");
		txtInvestigatingApprovedRemarks.setData(bean);
		handleRemarksPopup(txtInvestigatingApprovedRemarks,null);
		txtInvestigatingApprovedRemarks.setRequired(false);
		txtInvestigatingApprovedRemarks.setReadOnly(true);
		txtInvestigatingApprovedRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);

		txtReasonForReferring = new TextArea("Reason for Refering");
//		txtReasonForReferring.setMaxLength(100);
		txtReasonForReferring.setId("referReason");
		txtReasonForReferring.setData(bean);
		//handleRemarksPopup(txtReasonForReferring,null);
		//txtReasonForReferring.setReadOnly(true);
		txtReasonForReferring.setEnabled(false);
		//txtReasonForReferring.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		
		txtTriggerPointsForFocus = new TextArea("Trigger points to focus");
//		txtTriggerPointsForFocus.setMaxLength(100);
		txtTriggerPointsForFocus.setId("invTriggerFocus");
		txtTriggerPointsForFocus.setData(bean);
		handleRemarksPopup(txtTriggerPointsForFocus,null);
		txtTriggerPointsForFocus.setRequired(false);
		txtTriggerPointsForFocus.setReadOnly(true);
		txtTriggerPointsForFocus.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		
		
		cmbReasonforIniInv.addValueChangeListener( new ValueChangeListener() {
			
			@Override

			public void valueChange(ValueChangeEvent event) {

				SelectValue value = event.getProperty().getValue() != null ? (SelectValue) event.getProperty().getValue() : null;
				cmbReasonforIniInv.setValue(value);
				if(value != null && ((SelectValue)cmbReasonforIniInv.getValue()).getValue().equalsIgnoreCase("others")) {
						//unbindField(reasonforRefering);
					txtReasonForReferring.setEnabled(true);
					txtReasonForReferring.setRequired(true);
					//txtReasonForReferring.setReadOnly(false);
				
				}else{
					//txtReasonForReferring.setValue(null);
					txtReasonForReferring.setEnabled(false);
					txtReasonForReferring.setRequired(false);
					//txtReasonForReferring.setReadOnly(true);
				}
			}
	 });
		
		
		
		requestingRoleFormLayout = new FormLayout(txtRequestingRole,
				txtRequestorIdOrName, cmbAllocationTo,
				txtInvestigatingApprovedRemarks);
		requestingRoleFormLayout.setMargin(true);
		reasonFormLayout = new FormLayout(cmbReasonforIniInv,txtReasonForReferring,
				txtTriggerPointsForFocus);
		//reasonFormLayout.setMargin(true);
		requestingHorizontalLayout = new HorizontalLayout(
				requestingRoleFormLayout, reasonFormLayout);
		requestingHorizontalLayout.setSpacing(true);
		//requestingHorizontalLayout.setHeight("150px");
		//requestingHorizontalLayout.setMargin(true);
		
		//ipNo = new TextField("UHID/IP No");
		ipNo = binder.buildAndBind("UHID/IP No", "uhidIpNo", TextField.class);
		ipNo.setMaxLength(100);
		
		txtClaimBackgroundDetails = binder.buildAndBind(
				"Claim Background Details", "claimBackgroundDetails",
				TextArea.class);
		txtClaimBackgroundDetails.setMaxLength(4000);
		txtClaimBackgroundDetails.setWidth("750px");
		txtClaimBackgroundDetails.setRequired(true);
		txtClaimBackgroundDetails.setId("clmBackgroundDetails");
		txtClaimBackgroundDetails.setData(bean);
		handleRemarksPopup(txtClaimBackgroundDetails,null);
		txtClaimBackgroundDetails.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		
		txtInvestigationCaseFacts = binder.buildAndBind(
				"Facts of the Case", "investigationTriggerPoints",
				TextArea.class);
		txtInvestigationCaseFacts.setMaxLength(4000);	
		txtInvestigationCaseFacts.setWidth("500px");
		txtInvestigationCaseFacts.setId("invFactsofCase");
//		factsOfCasesListener(txtInvestigationCaseFacts,null);
		txtInvestigationCaseFacts.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		handleRemarksPopup(txtInvestigationCaseFacts,null);
		
		txtInvestigationCaseFacts.addBlurListener(getfactsOfCaseListener(txtInvestigationCaseFacts));
		
		investigationFormLayout = new FormLayout(ipNo,txtClaimBackgroundDetails, txtInvestigationCaseFacts);
		investigationFormLayout.setSpacing(true);
		investigationFormLayout.setMargin(true);
		//investigationFormLayout.setHeight("240px");

		VerticalLayout investigationVerticalLayout = new VerticalLayout(
				investigationFormLayout);
		investigationVerticalLayout.setSpacing(true);
		investigationVerticalLayout.setMargin(false);
		
		VerticalLayout triggerPointsDetailLayout = new VerticalLayout();
		draftTriggerPointsTableInstanceObj.setWidth("80%");
		triggerPointsDetailLayout.addComponent(draftTriggerPointsTableInstanceObj);
		triggerPointsDetailLayout.setSizeFull();
		
		investigationFormLayout.addComponent(triggerPointsDetailLayout);

		mainLayout = new VerticalLayout(requestingHorizontalLayout, investigationVerticalLayout);
		addListener();
		setFormData(bean);
		// setCompositionRoot(mainLayout);
		return mainLayout;
	}

	private void setFormData(DraftInvestigatorDto draftInvestigatorDto) {
		if (draftInvestigatorDto != null) {
			this.bean = draftInvestigatorDto;
			BeanItemContainer<SelectValue> allocationContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			allocationContainer.addAll(this.bean.getAllocationToSelectValueList());
			cmbAllocationTo.setContainerDataSource(allocationContainer);
			cmbAllocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbAllocationTo.setItemCaptionPropertyId("value");
			
			if(this.bean.getAllocationToSelectValue() != null){
				cmbAllocationTo.setValue(this.bean.getAllocationToSelectValue());
			}
			
			BeanItemContainer<SelectValue> reasonForInitiateInvestContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			reasonForInitiateInvestContainer.addAll(this.bean.getReasonForInitiatingInvestSelectValueList());
			cmbReasonforIniInv.setContainerDataSource(reasonForInitiateInvestContainer);
			cmbReasonforIniInv.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbReasonforIniInv.setItemCaptionPropertyId("value");
			
			if(this.bean.getReasonForInitiatingInvestSelectValue() != null){
				cmbReasonforIniInv.setValue(this.bean.getReasonForInitiatingInvestSelectValue());
			}


			txtInvestigatingApprovedRemarks.setReadOnly(false);
			txtInvestigatingApprovedRemarks
					.setValue(draftInvestigatorDto
							.getInvestiationApprovedRemarks() != null ? draftInvestigatorDto
							.getInvestiationApprovedRemarks() : "");
			txtInvestigatingApprovedRemarks.setReadOnly(true);
			//txtReasonForReferring.setReadOnly(false);
			txtReasonForReferring.setValue(draftInvestigatorDto
					.getReasonForRefering() != null ? draftInvestigatorDto
					.getReasonForRefering() : "");
			//txtReasonForReferring.setReadOnly(true);
			txtTriggerPointsForFocus.setReadOnly(false);
			txtTriggerPointsForFocus.setValue(autoTrgpts);
			txtTriggerPointsForFocus.setReadOnly(true);
			
		}
	}

	private void addListener() {
		
	}

	public boolean validatePage() {
		try {
			this.binder.commit();
			
			if(null != cmbReasonforIniInv.getValue() && cmbReasonforIniInv.getValue().toString().equalsIgnoreCase("Others")) 
			{
				if(txtReasonForReferring.getValue() == null || ("").equalsIgnoreCase(txtReasonForReferring.getValue()) || 
						txtReasonForReferring.getValue().isEmpty())	
				{
					showErrorMsg("Please Enter Reason For Refering.</br>");
					return false;
				}
			}
			
			if(isInvEnable){
				if(cmbReasonforIniInv !=null && cmbReasonforIniInv.getValue() == null) 
				{
					showErrorMsg("Please Select the Reason for Initiating Investigation.</br>");
					return false;
				}
			}
			
			List<DraftTriggerPointsToFocusDetailsTableDto> list = draftTriggerPointsTableInstanceObj.getValues();
			
			draftTriggerPointsTableInstanceObj.deleteEmptyRows();

				if(list != null && !list.isEmpty()){
					for(DraftTriggerPointsToFocusDetailsTableDto dto: list){
						dto.setSno(list.indexOf(dto)+1);
					}
				}
				
				this.bean.setTriggerPointsList(list);
				this.bean.setDeletedTriggerPointsList(draftTriggerPointsTableInstanceObj.getDeletedDraftInvgDescList());
				

				if(isInvEnable && cmbReasonforIniInv.getValue() !=null){
					SelectValue selectedInv = (SelectValue)cmbReasonforIniInv.getValue();
					this.bean.setReasonForInitiatingInvestSelectValue(selectedInv);
				}

				if(isInvEnable && null != cmbReasonforIniInv.getValue() && cmbReasonforIniInv.getValue().toString().equalsIgnoreCase("Others")) {
					if(txtReasonForReferring.getValue()!=null && !txtReasonForReferring.getValue().isEmpty() /*&& bean.getReasonForRefering() == null*/){
						this.bean.setReasonForRefering(txtReasonForReferring.getValue());
					}
				}
				
				return true;
		
		} catch (CommitException e) {
			showErrorMsg("Please enter the Mandatory Fields.");
			return false;
			
		}		
	}
	
	private void showErrorMsg(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
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
	
	public  void factsOfCasesListener(TextArea searchField, final  Listener listener) {
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "EnterShortcut", ShortcutAction.KeyCode.F8, null) {
	      private static final long serialVersionUID = -2267576464623389044L;
	      @Override
	      public void handleAction( Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };

	    handleShortcut(searchField, getShortCutListener(searchField));
	  }
	
	public  void handleShortcut(final TextArea textField, final ShortcutListener shortcutListener) {
	
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
	
	
	private ShortcutListener getShortCutListener(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("EnterShortcut",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				//DraftInvestigatorDto draftInvestigationDTO = (DraftInvestigatorDto) txtFld.getData();		
				 
				if (null != vLayout
						&& vLayout.getComponentCount() > 0) {
					vLayout.removeAllComponents();
				}
				
				final TextArea txtArea = new TextArea();			
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(4000);
				txtArea.setRows(25);
				
				txtArea.setValue(null != txtInvestigationCaseFacts.getValue() ? txtInvestigationCaseFacts.getValue() : "");				
				txtArea.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						TextArea txt = (TextArea)event.getProperty();
						txtFld.setValue(txt.getValue());
					}
				});
				
					
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				final Window dialog = new Window();
				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setCaption("Facts of the Case");
				dialog.setClosable(true);
				
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);
				
				//dialog.show(getUI().getCurrent(), null, true);
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);
				
				okBtn.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
						bean.setInvestigationTriggerPoints(txtFld.getValue());	
					}
				});	
			}
		};
		
		return listener;
	}
	
	
	
	public BlurListener getfactsOfCaseListener(final TextArea reasonField) {
		
		BlurListener listener = new BlurListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextArea component = (TextArea) event.getComponent();
				if(null != component && null != component.getValue()) {
					bean.setInvestigationTriggerPoints(component.getValue());
				} 
				
			}
		};
		return listener;
		
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
				DraftInvestigatorDto  draftInvDto = (DraftInvestigatorDto) txtFld.getData();
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

				
				if(("referReason").equalsIgnoreCase((txtFld.getId())) || ("invTriggerfocus").equalsIgnoreCase((txtFld.getId())) || ("invApproveRemarks").equalsIgnoreCase((txtFld.getId()))){
					if(("referReason").equalsIgnoreCase((txtFld.getId()))){
						remarksValue = draftInvDto.getReasonForRefering() != null ? draftInvDto.getReasonForRefering() : "";
//						txtArea.setRows(draftInvDto.getReasonForRefering() != null ? (draftInvDto.getReasonForRefering().length()/80 >= 25 ? 25 : ((draftInvDto.getReasonForRefering().length()/80)%25)+1) : 25);
					}
					if(("invTriggerfocus").equalsIgnoreCase((txtFld.getId()))){
						remarksValue = draftInvDto.getTriggerPointsForFocus() != null ? draftInvDto.getTriggerPointsForFocus() : "";
//						txtArea.setRows(draftInvDto.getTriggerPointsForFocus() != null ? (draftInvDto.getTriggerPointsForFocus().length()/80 >= 25 ? 25 : ((bean.getTriggerPointsForFocus().length()/80)%25)+1) : 25);
					}
					if(("invApproveRemarks").equalsIgnoreCase((txtFld.getId()))){
						remarksValue = draftInvDto.getInvestiationApprovedRemarks() != null ? draftInvDto.getInvestiationApprovedRemarks() : "";
//						txtArea.setRows(draftInvDto.getInvestiationApprovedRemarks() != null ? (draftInvDto.getInvestiationApprovedRemarks().length()/80 >= 25 ? 25 : ((draftInvDto.getInvestiationApprovedRemarks().length()/80)%25)+1) : 25);
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
						
						if(("invFactsofCase").equalsIgnoreCase(txtFld.getId()) || ("clmBackgroundDetails").equalsIgnoreCase(txtFld.getId())){
							
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
				if(("clmBackgroundDetails").equalsIgnoreCase((txtFld.getId()))){
					strCaption = "Claim Background Details";
				}
				if(("invFactsofCase").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Facts of the Case";
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
				else if(!bean.getNonPreferredPopupMap().isEmpty()){
					nonPreferredPopupMessage();
				}
				else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
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
//	dialog.setCaption("Alert");
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
			else if(!bean.getNonPreferredPopupMap().isEmpty()){
				nonPreferredPopupMessage();
			}
			else if(bean.getIs64VBChequeStatusAlert()){
				get64VbChequeStatusAlert();
			}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
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
				else if(bean.getIs64VBChequeStatusAlert()){
					get64VbChequeStatusAlert();
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
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
				}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
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
//	dialog.setCaption("Alert");
	dialog.setClosable(false);
	dialog.setContent(layout);
	dialog.setResizable(false);
	dialog.setModal(true);
	dialog.show(getUI().getCurrent(), null, true);

	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
//			 bean.setIsPopupMessageOpened(true);

//				bean.setIsPopupMessageOpened(true);
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
					else if(!bean.getNonPreferredPopupMap().isEmpty()){
						nonPreferredPopupMessage();
					}
					else if(bean.getIs64VBChequeStatusAlert()){
						get64VbChequeStatusAlert();
					}else if(!bean.getPopupMap().isEmpty() && !bean.getIsPopupMessageOpened()) {
						poupMessageForProduct();
					}
					 
				}
			});
		}
}
