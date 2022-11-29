package com.shaic.claim.withdrawPostProcessWizard;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addon.cdiproperties.annotation.TableProperties;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.WizardStep;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.PedRaisedDetailsTable;
import com.shaic.claim.aadhar.search.SearchUpdateAadharTableDTO;
import com.shaic.claim.cashlessprocess.downsize.wizard.PreviousPreauthSummaryTable;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.MasterRemarks;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component.Listener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class WithdrawPreauthPostProcessPage extends ViewComponent implements WizardStep<WithdrawPreauthPostProcessPageDTO>{


	private ComboBox reasonForWithdraw;

	private TextArea txtWithdrawRemarks;

	private TextArea withdrawRemarksInsuredTxta;

	private TextArea rejectRemarksInsuredTxta;

	private TextArea medicalRemarks;

	private TextArea doctorNote;

	/*@Inject
		private Instance<RevisedCashlessCarousel> commonCarouselInstance;*/

	@Inject
	private WithdrawPreauthPostProcessPageDTO bean;

	private BeanFieldGroup<WithdrawPreauthPostProcessPageDTO> binder;

	@Inject
	private Instance<PreviousPreauthSummaryTable> preauthPreviousDetailsPage;

	private PreviousPreauthSummaryTable preauthPreviousDetailsPageObj;

	//		private WithdrawPreauthTable withdrawPreauthTableObj;

	//private BeanItemContainer<SelectValue> selectValueContainer;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private ArrayList<Component> mandatoryFieldsForwithdrawInternalRemarks = new ArrayList<Component>();

	/*@SuppressWarnings("unused")
		private NewIntimationDto newIntimationDto;*/

	//private ClaimDto claimDto;

	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;

	////private static Window popup;

	private VerticalLayout wholeVertical;

	private Button initiatePEDButton;

	private FormLayout formLayout;

	private ComboBox rejectionCategoryCmb;

	private TextArea rejectionRemarksTxta;

	//private Button submitButton;

	private VerticalLayout fieldsLayout;

	private HorizontalLayout remarksLayout;

	private FormLayout withdrawForm;

	private FormLayout withdrawAndRejectForm;
	
	private TextArea withdrawInternalRemarks;

	@Inject 
	private Instance<PedRaisedDetailsTable> pedRaiseDetailsTable;

	private PedRaisedDetailsTable pedRaiseDetailsTableObj;
	
	@EJB
	private MasterService masterService;
	
	@Inject
	@TableProperties(nullSelectionAllowed = false, sizeFull = true, immediate = true, columnCollapsingAllowed = true, columnReorderingAllowed = true, selectable = true)
	protected Table table;

	@Override
	public String getCaption() {
		return "Withdraw Pre-Auth(Post Process)";
	}

	@PostConstruct
	public void init() {

	}

	@Override
	public void init(WithdrawPreauthPostProcessPageDTO bean) {
		this.bean=bean;
		/*this.newIntimationDto=this.bean.getNewIntimationDto();
			this.claimDto=this.bean.getClaimDto();
			this.selectValueContainer=this.bean.getSelectValueContainer();*/

	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<WithdrawPreauthPostProcessPageDTO>(
				WithdrawPreauthPostProcessPageDTO.class);
		this.binder.setItemDataSource(this.bean);
	}

	@Override
	public Component getContent() {
		if(bean.getPreauthDto().getPolicyDto().getGmcPolicyType() != null && !bean.getPreauthDto().getPolicyDto().getGmcPolicyType().isEmpty() && bean.getPreauthDto().getPolicyDto().getLinkPolicyNumber() != null
				&& ((bean.getPreauthDto().getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT)) || (bean.getPreauthDto().getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT)))){
			showAlertForGMCParentLink(bean.getPreauthDto().getPolicyDto().getLinkPolicyNumber());
		}

		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		if(bean.getPreauthDto().isInsuredDeleted()){
			alertMessageForPEDInitiate(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getPreauthDto().getNewIntimationDTO().getInsuredPatient().getInsuredName()));
		}else{
			alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
		}
		
		if(bean.getPreauthDto().getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), bean.getPreauthDto().getIsSuspicious(), bean.getPreauthDto().getClmPrcsInstruction());
	    }

		initiatePEDButton = new Button("Initiate PED Endorsement");
		HorizontalLayout buttonHLayout = new HorizontalLayout(initiatePEDButton);
		buttonHLayout.setMargin(true);
		buttonHLayout.setComponentAlignment(initiatePEDButton, Alignment.MIDDLE_RIGHT);

		//			withdrawPreauthTableObj=withdrawPreauthTableInstance.get();
		//			withdrawPreauthTableObj.init("Pre-auth Summary", false);
		preauthPreviousDetailsPageObj = preauthPreviousDetailsPage.get();
		preauthPreviousDetailsPageObj.init("Pre-auth Summary", false, false);
		preauthPreviousDetailsPageObj.setTableList(this.bean.getPreviousPreAuthTableDTO());
		
//		withdrawBtn = new Button("Withdraw");
//		withdrawAndRejectBtn = new Button("Withdraw and Reject");
//
//		HorizontalLayout btnLayout = new HorizontalLayout(withdrawBtn, withdrawAndRejectBtn);
//		fieldsLayout = new VerticalLayout();
//		remarksLayout = new HorizontalLayout();
//		remarksLayout.setSpacing(true);
//		remarksLayout.setWidth("100%");
//		remarksLayout.setMargin(true);

		wholeVertical=new VerticalLayout(buttonHLayout,preauthPreviousDetailsPageObj,buildVertical());
		wholeVertical.setSpacing(true);
		wholeVertical.setComponentAlignment(buttonHLayout, Alignment.TOP_RIGHT);
		//wholeVertical.setHeight("450px");

		addListener();

		return wholeVertical;
	}

	public FormLayout buildVertical(){
	
		table.setContainerDataSource(new BeanItemContainer<WithdrawPreauthPostProcessPageDTO>(WithdrawPreauthPostProcessPageDTO.class));
		withdrawInternalRemarks=(TextArea)binder.buildAndBind("Withdrawal Internal Remarks","withdrawInternalRemarks",TextArea.class);
		withdrawInternalRemarks.setMaxLength(100);
		withdrawInternalRemarks.setWidth("400px");

		mandatoryFieldsForwithdrawInternalRemarks.add(withdrawInternalRemarks);
		showOrHideValidationforRemarks(false);

		doctorNote=(TextArea)binder.buildAndBind("Doctor Note(Internal Purpose)", "doctorNote", TextArea.class);
		doctorNote.setMaxLength(100);
		doctorNote.setWidth("400px");
		//added for CR rejection approved
		reasonForWithdraw = (ComboBox) binder.buildAndBind("Reason for Withdrawal",
				"reasonForWithdraw", ComboBox.class);
		reasonForWithdraw.setWidth("300px");
	
		BeanItemContainer<SelectValue> selectValueContainer2 = masterService.getSelectValueContainer(ReferenceTable.WITHDRAWAL_REASON);
		//added for CR R1180
		if(!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDto().getPolicy().getProduct().getKey())){
			selectValueContainer2.removeAllItems();
			SelectValue withdrawReasonForNoNGMC = new SelectValue();
			MastersValue withdrawReason = masterService.getMaster(ReferenceTable.INSURED_PAID_AMOUNT_NOT_UTILIZED);
			withdrawReasonForNoNGMC.setId(withdrawReason.getKey());
			withdrawReasonForNoNGMC.setValue(withdrawReason.getValue());
			selectValueContainer2.addBean(withdrawReasonForNoNGMC);
			}
		
		reasonForWithdraw.setContainerDataSource(selectValueContainer2);
		reasonForWithdraw.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForWithdraw.setItemCaptionPropertyId("value");


		txtWithdrawRemarks = (TextArea)binder.buildAndBind("Withdrawal Remarks Hospital","withdrawRemarks",TextArea.class);
		txtWithdrawRemarks.setMaxLength(4000);

		txtWithdrawRemarks.setWidth("50%");
		txtWithdrawRemarks.setHeight("200px");
		txtWithdrawRemarks.setId("hospWithdrawRmrks");
		txtWithdrawRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(txtWithdrawRemarks,null);

		reasonForWithdraw.setNullSelectionAllowed(false);

		if(this.bean.getReasonForWithdraw() != null){
			reasonForWithdraw.setValue(this.bean.getReasonForWithdraw());
		}

		if(this.bean.getWithdrawRemarks() != null){
			txtWithdrawRemarks.setValue(this.bean.getWithdrawRemarks());
		}

		withdrawRemarksInsuredTxta = (TextArea)binder.buildAndBind("Withdrawal Remarks Insured","insuredWithdrawRemarks",TextArea.class);
		withdrawRemarksInsuredTxta.setMaxLength(4000);

		withdrawRemarksInsuredTxta.setWidth("50%");
		withdrawRemarksInsuredTxta.setHeight("200px");
		withdrawRemarksInsuredTxta.setId("insuredWithdrawRmrks");
		withdrawRemarksInsuredTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(withdrawRemarksInsuredTxta,null);

		if(this.bean.getInsuredWithdrawRemarks() != null){
			withdrawRemarksInsuredTxta.setValue(this.bean.getInsuredWithdrawRemarks());
		}

		addWithDrawListenerListener();
		mandatoryFields.add(reasonForWithdraw);
		mandatoryFields.add(withdrawInternalRemarks);
		mandatoryFields.add(txtWithdrawRemarks);
		showOrHideValidation(false);
		
		Label chkLabel = new Label("Do you want to send the letter");
		//Vaadin8-setImmediate() chkLabel.setImmediate(true);
		
		final CheckBox chkAgree = new CheckBox();
		//Vaadin8-setImmediate() chkAgree.setImmediate(true);
		chkAgree.setStyleName(ValoTheme.CHECKBOX_LARGE);
		chkAgree.setEnabled(false);
		chkAgree.setValue(true);
		
		FormLayout chkFromLayout = new FormLayout(chkLabel);
		chkFromLayout.setSpacing(true);
		chkFromLayout.setMargin(false);
		
		FormLayout chkFromLayout1 = new FormLayout(chkAgree);
		chkFromLayout1.setSpacing(true);
		chkFromLayout1.setMargin(false);
		
		HorizontalLayout horLayout = new HorizontalLayout(chkFromLayout,chkFromLayout1);
		horLayout.setSpacing(true);
		horLayout.setMargin(false);
		

		formLayout=new FormLayout(withdrawInternalRemarks,doctorNote,reasonForWithdraw,txtWithdrawRemarks,withdrawRemarksInsuredTxta,horLayout);

		//			formLayout.setHeight("250px");


		return formLayout;
	}

	public void addListener() {
		initiatePEDButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8159939563947706329L;

			@Override
			public void buttonClick(ClickEvent event) {
				Long preauthKey=bean.getTableDto().getKey();          //preauth key from table dto
				Long intimationKey=bean.getNewIntimationDto().getKey();
				Long policyKey=bean.getTableDto().getPolicyKey();
				Long claimKey=bean.getTableDto().getClaimKey();
				if(bean.getPreauthDto().getIsPEDInitiatedForBtn()) {
					if(bean.getPreauthDto().isInsuredDeleted()){
						alertMessageForPEDInitiate(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getPreauthDto().getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}
					else{
						alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
					}	
				}				
				createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
			}
		});
	}

//		withdrawBtn.addClickListener(new ClickListener() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				fireViewEvent(WithdrawPreauthPostProcessWizardPresenter.CLICK_WITHDRAW_ACTION, null);
//			}
//		});
//
//
//
//		withdrawAndRejectBtn.addClickListener(new ClickListener() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				fireViewEvent(WithdrawPreauthPostProcessWizardPresenter.CLICK_WITHDRAW_AND_REJECT_ACTION, null);
//			}
//		});
//	}


	private void createPEDScreen(Long preauthKey, Long intimationKey,
			Long policyKey, Long claimKey) {
		ViewPEDRequestWindow viewPEDRequest = viewPedRequest.get();
		viewPEDRequest.initView(bean.getPreauthDto(),preauthKey,intimationKey,policyKey,claimKey,ReferenceTable.DOWNSIZE_STAGE,false);	
		viewPEDRequest.setPresenterString(SHAConstants.REIMBURSEMENT_SCREEN);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View PED Request Details");
		popup.setWidth("85%");
		popup.setHeight("100%");
		popup.setContent(viewPEDRequest);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {

			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}





	public Boolean alertMessageForPEDInitiate(final String message) {
		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
		//final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		//			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);

		VerticalLayout layout = new VerticalLayout(successLabel);

		if(SHAConstants.PED_RAISE_MESSAGE.equalsIgnoreCase(message)){

			pedRaiseDetailsTableObj = pedRaiseDetailsTable.get();
			pedRaiseDetailsTableObj.init("", false, false);
			pedRaiseDetailsTableObj.initView(bean.getPreauthDto().getNewIntimationDTO().getPolicy().getKey(), bean.getPreauthDto().getNewIntimationDTO().getInsuredPatient().getKey());

			layout.addComponent(pedRaiseDetailsTableObj.getTable());
		}

		layout.addComponent(homeButton);

		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		//			dialog.setCaption("Alert");
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

				if(bean.getPreauthDto().isMultiplePEDAvailableNotDeleted() && !SHAConstants.PED_RAISE_MESSAGE.equalsIgnoreCase(message)){
					alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
					bean.getPreauthDto().setMultiplePEDAvailableNotDeleted(false);
				}
				//					Long preauthKey=bean.getPreauthDto().getKey();               // as per CR R1086 Auto deletion of Risk - Non Disclosed PED
				//					Long intimationKey=bean.getPreauthDto().getIntimationKey();
				//					Long policyKey=bean.getPreauthDto().getPolicyKey();
				//					Long claimKey=bean.getPreauthDto().getClaimKey();
				//					createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
			}
		});
		return true;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onAdvance() {

		Boolean errMsg = Boolean.FALSE;

		if(("").equals(withdrawInternalRemarks.getValue())){
			withdrawInternalRemarks.setValue(null);
		}

		if(validatePage()){
			SelectValue selected=new SelectValue();
			selected=(SelectValue)reasonForWithdraw.getValue();
			this.bean.setReasonForWithdraw(selected);
			this.bean.setWithdrawInternalRemarks(withdrawInternalRemarks.getValue());
			this.bean.setDoctorNote(doctorNote.getValue());
			this.bean.setTotalApprovedAmt(preauthPreviousDetailsPageObj.getTotalApprovedAmt());
			this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(preauthPreviousDetailsPageObj.getTotalApprovedAmt());
			this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setWithdrawReason(selected);
			this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setWithdrawRemarks(txtWithdrawRemarks.getValue());
			this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setWithDrawRemarksForInsured(withdrawRemarksInsuredTxta != null ? withdrawRemarksInsuredTxta.getValue() : null);
			this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setRejectionCategory(rejectionCategoryCmb != null ? (SelectValue)rejectionCategoryCmb.getValue() : null);
			this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setRejectionRemarks(rejectionRemarksTxta != null ? rejectionRemarksTxta.getValue() : null);
			this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setRemarksForInsured(rejectRemarksInsuredTxta != null ? rejectRemarksInsuredTxta.getValue() : null);
			this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setWithdrawInternalRemarks(withdrawInternalRemarks.getValue());
			this.bean.getPreauthDto().setWithdrawInternalRemarks(withdrawInternalRemarks.getValue());
			this.bean.setStatusKey(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS);
			errMsg = Boolean.TRUE;
			//return true;
		}
		else{
			//return false;
			errMsg = Boolean.FALSE;
		}

		return errMsg;
	}

	@Override
	public boolean onBack() {

		return false;
	}
	@Override
	public boolean onSave() {
		return false;
	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	protected void showOrHideValidationforRemarks(Boolean isVisible) {
		for (Component component : mandatoryFieldsForwithdrawInternalRemarks) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		if(fieldsLayout != null && fieldsLayout.getComponentCount() <= 0) {
			hasError = true;
			eMsg.append("Please choose any action to proceed further.");
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
		if (hasError) {
			setRequired(true);
			setRequiredforRemarks(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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

			hasError = true;
			return !hasError;
		} 
		showOrHideValidation(false);
		showOrHideValidationforRemarks(false);
		try {
			binder.commit();
			 //added for preauth approved date in withdraw letter issue(IMSSUPPOR-28726).
			fireViewEvent(WithdrawPreauthPostProcessWizardPresenter.PREAUTH_APPROVED_DATE_POST_PROCESS,bean);
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}

	private void setRequiredforRemarks(Boolean isRequired) {

		if (!mandatoryFieldsForwithdrawInternalRemarks.isEmpty()) {
			for (int i = 0; i < mandatoryFieldsForwithdrawInternalRemarks.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFieldsForwithdrawInternalRemarks
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}

	public void buildWithdrawLayout(
			BeanItemContainer<SelectValue> selectValueContainer2) {

		if(bean.getPreauthDto().getIsPEDInitiatedForBtn()) {

			if(bean.getPreauthDto().isInsuredDeleted()){
				alertMessageForPEDInitiate(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getPreauthDto().getNewIntimationDTO().getInsuredPatient().getInsuredName()));
			}
		}	

		this.bean.setStatusKey(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS);
		unBindFields();

		mandatoryFields.removeAll(mandatoryFields);
		if(withdrawForm != null) {
			withdrawForm.removeAllComponents();
		}
		if(withdrawAndRejectForm != null) {
			withdrawAndRejectForm.removeAllComponents();
		}
		fieldsLayout.removeAllComponents();
		remarksLayout.removeAllComponents();
		wholeVertical.removeComponent(remarksLayout);

		reasonForWithdraw = (ComboBox) binder.buildAndBind("Reason for Withdrawal",
				"reasonForWithdraw", ComboBox.class);
		reasonForWithdraw.setWidth("300px");

		reasonForWithdraw.setContainerDataSource(selectValueContainer2);
		reasonForWithdraw.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForWithdraw.setItemCaptionPropertyId("value");


		txtWithdrawRemarks = (TextArea)binder.buildAndBind("Withdrawal Remarks Hospital","withdrawRemarks",TextArea.class);
		txtWithdrawRemarks.setMaxLength(4000);

		txtWithdrawRemarks.setWidth("50%");
		txtWithdrawRemarks.setHeight("200px");
		txtWithdrawRemarks.setId("hospWithdrawRmrks");
		txtWithdrawRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(txtWithdrawRemarks,null);

		reasonForWithdraw.setNullSelectionAllowed(false);

		if(this.bean.getReasonForWithdraw() != null){
			reasonForWithdraw.setValue(this.bean.getReasonForWithdraw());
		}

		if(this.bean.getWithdrawRemarks() != null){
			txtWithdrawRemarks.setValue(this.bean.getWithdrawRemarks());
		}

		withdrawRemarksInsuredTxta = (TextArea)binder.buildAndBind("Withdrawal Remarks Insured","insuredWithdrawRemarks",TextArea.class);
		withdrawRemarksInsuredTxta.setMaxLength(4000);

		withdrawRemarksInsuredTxta.setWidth("50%");
		withdrawRemarksInsuredTxta.setHeight("200px");
		withdrawRemarksInsuredTxta.setId("insuredWithdrawRmrks");
		withdrawRemarksInsuredTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(withdrawRemarksInsuredTxta,null);

		if(this.bean.getInsuredWithdrawRemarks() != null){
			withdrawRemarksInsuredTxta.setValue(this.bean.getInsuredWithdrawRemarks());
		}

		addWithDrawListenerListener();
		mandatoryFields.add(reasonForWithdraw);
		mandatoryFields.add(withdrawInternalRemarks);
		mandatoryFields.add(txtWithdrawRemarks);
		showOrHideValidation(false);

		if(withdrawForm == null) {
			withdrawForm = new FormLayout();
		}
		withdrawForm.addComponent(reasonForWithdraw);

		//			withdrawForm.addComponent(txtWithdrawRemarks);

		if(remarksLayout == null){
			remarksLayout = new HorizontalLayout();
		}

		remarksLayout.addComponents(new FormLayout(txtWithdrawRemarks),withdrawRemarksInsuredTxta);
		remarksLayout.setSpacing(true);

		fieldsLayout.addComponent(withdrawForm);
		wholeVertical.addComponent(remarksLayout);
	}

	private void unBindFields() {
		if (reasonForWithdraw != null ) {
			Object propertyId = this.binder.getPropertyId(reasonForWithdraw);
			if (reasonForWithdraw!= null && reasonForWithdraw.isAttached() && propertyId != null) {
				reasonForWithdraw.setValue(null);
				this.binder.unbind(reasonForWithdraw);
			}
		}

		if (txtWithdrawRemarks != null ) {
			Object propertyId = this.binder.getPropertyId(txtWithdrawRemarks);
			if (txtWithdrawRemarks!= null && txtWithdrawRemarks.isAttached() && propertyId != null) {
				txtWithdrawRemarks.setValue(null);
				this.binder.unbind(txtWithdrawRemarks);
			}
		}

		if (withdrawRemarksInsuredTxta != null ) {
			Object propertyId = this.binder.getPropertyId(withdrawRemarksInsuredTxta);
			if (withdrawRemarksInsuredTxta!= null && withdrawRemarksInsuredTxta.isAttached() && propertyId != null) {
				withdrawRemarksInsuredTxta.setValue(null);
				this.binder.unbind(withdrawRemarksInsuredTxta);
			}
		}


		if (rejectionCategoryCmb != null ) {
			Object propertyId = this.binder.getPropertyId(rejectionCategoryCmb);
			if (rejectionCategoryCmb!= null && rejectionCategoryCmb.isAttached() && propertyId != null) {
				rejectionCategoryCmb.setValue(null);
				this.binder.unbind(rejectionCategoryCmb);
			}
		}

		if (rejectionRemarksTxta != null ) {
			Object propertyId = this.binder.getPropertyId(rejectionRemarksTxta);
			if (rejectionRemarksTxta!= null && rejectionRemarksTxta.isAttached() && propertyId != null) {
				rejectionRemarksTxta.setValue(null);
				this.binder.unbind(rejectionRemarksTxta);
			}
		}

		if (rejectRemarksInsuredTxta != null ) {
			Object propertyId = this.binder.getPropertyId(rejectRemarksInsuredTxta);
			if (rejectRemarksInsuredTxta != null && rejectRemarksInsuredTxta.isAttached() && propertyId != null) {
				rejectRemarksInsuredTxta.setValue(null);
				this.binder.unbind(rejectRemarksInsuredTxta);
			}
		}
	}

	public void buildWithdrawAndRejectLayout(
			BeanItemContainer<SelectValue> withdrawContainer,
			BeanItemContainer<SelectValue> rejectionContainer) {

		if(bean.getPreauthDto().getIsPEDInitiatedForBtn()) {

			if(bean.getPreauthDto().isInsuredDeleted()){
				alertMessageForPEDInitiate(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getPreauthDto().getNewIntimationDTO().getInsuredPatient().getInsuredName()));
			}
		}

		this.bean.setStatusKey(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS);
		mandatoryFields.removeAll(mandatoryFields);
		unBindFields();

		wholeVertical.removeComponent(remarksLayout);
		if(withdrawForm != null) {
			withdrawForm.removeAllComponents();
		}
		if(withdrawAndRejectForm != null) {
			withdrawAndRejectForm.removeAllComponents();
		}
		fieldsLayout.removeAllComponents();
		reasonForWithdraw = (ComboBox) binder.buildAndBind("Reason for Withdrawal",
				"reasonForWithdraw", ComboBox.class);

		reasonForWithdraw.setWidth("300px");

		reasonForWithdraw.setContainerDataSource(withdrawContainer);
		reasonForWithdraw.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForWithdraw.setItemCaptionPropertyId("value");
		addWithDrawListenerListener();

		reasonForWithdraw.setNullSelectionAllowed(false);
		if(this.bean.getReasonForWithdraw() != null){
			reasonForWithdraw.setValue(this.bean.getReasonForWithdraw());
		}


		txtWithdrawRemarks = (TextArea)binder.buildAndBind("Withdrawal Remarks Hospital","withdrawRemarks",TextArea.class);
		txtWithdrawRemarks.setMaxLength(4000);
		txtWithdrawRemarks.setWidth("50%");
		txtWithdrawRemarks.setHeight("250px");
		txtWithdrawRemarks.setId("hospWithdrawRmrks");
		txtWithdrawRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(txtWithdrawRemarks,null);

		if(this.bean.getWithdrawRemarks() != null){
			txtWithdrawRemarks.setValue(this.bean.getWithdrawRemarks());
		}

		withdrawRemarksInsuredTxta = (TextArea)binder.buildAndBind("Withdrawal Remarks Insured","insuredWithdrawRemarks",TextArea.class);
		withdrawRemarksInsuredTxta.setMaxLength(4000);

		withdrawRemarksInsuredTxta.setWidth("50%");
		withdrawRemarksInsuredTxta.setHeight("250px");
		withdrawRemarksInsuredTxta.setId("insuredWithdrawRmrks");
		withdrawRemarksInsuredTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(withdrawRemarksInsuredTxta,null);

		if(this.bean.getInsuredWithdrawRemarks() != null){
			withdrawRemarksInsuredTxta.setValue(this.bean.getInsuredWithdrawRemarks());
		}


		rejectionCategoryCmb = (ComboBox) binder.buildAndBind("Rejection Category","rejectionCategory",ComboBox.class);


		BeanItemContainer<SelectValue> filterValues = new BeanItemContainer<SelectValue>(SelectValue.class);

		Product product = bean.getNewIntimationDto().getPolicy().getProduct();

		if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				bean.getNewIntimationDto().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
			List<SelectValue> itemIds = rejectionContainer.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_SIVER_REJECT)){
					filterValues.addBean(selectValue);
				}
			}
		}else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				bean.getNewIntimationDto().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase("S")){
			List<SelectValue> itemIds = rejectionContainer.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_GOLD_REJECT)){
					filterValues.addBean(selectValue);
				}
			}
		}else{
			List<SelectValue> itemIds = rejectionContainer.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if((! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_GOLD_REJECT)) && (! selectValue.getId().equals(ReferenceTable.SUPER_SURPLUS_SIVER_REJECT))){
					filterValues.addBean(selectValue);
				}
			}
		}

		rejectionCategoryCmb.setContainerDataSource(filterValues);
		rejectionCategoryCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		rejectionCategoryCmb.setItemCaptionPropertyId("value");
		rejectionCategoryCmb.setWidth("300px");

		if(this.bean.getRejectionCategory() != null){
			rejectionCategoryCmb.setValue(this.bean.getRejectionCategory());
		}

		addRejectionListener();

		rejectionRemarksTxta = (TextArea) binder.buildAndBind("Rejection Remarks Hospital","rejectionRemarks",TextArea.class);
		rejectionRemarksTxta.setMaxLength(4000);

		rejectionRemarksTxta.setWidth("325px");
		rejectionRemarksTxta.setHeight("250px");
		rejectionRemarksTxta.setId("hospRejRmrks");
		rejectionRemarksTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(rejectionRemarksTxta,null);

		if(this.bean.getRejectionRemarks() != null){
			rejectionRemarksTxta.setValue(this.bean.getRejectionRemarks());
		}
		mandatoryFields.add(reasonForWithdraw);
		mandatoryFields.add(txtWithdrawRemarks);
		mandatoryFields.add(withdrawInternalRemarks);
		mandatoryFields.add(rejectionCategoryCmb);
		mandatoryFields.add(rejectionRemarksTxta);

		showOrHideValidation(false);

		if(withdrawForm == null) {
			withdrawForm = new FormLayout();
		}

		withdrawForm.addComponent(reasonForWithdraw);

		withdrawForm.addComponent(txtWithdrawRemarks);
		withdrawForm.addComponent(withdrawRemarksInsuredTxta);


		if(withdrawAndRejectForm == null) {
			withdrawAndRejectForm = new FormLayout();
		}

		withdrawAndRejectForm.removeAllComponents();
		withdrawAndRejectForm.addComponent(rejectionCategoryCmb);
		withdrawAndRejectForm.addComponent(rejectionRemarksTxta);

		rejectRemarksInsuredTxta = (TextArea) binder.buildAndBind("Rejection Remarks Insured","insuredRejectionRemarks",TextArea.class);
		rejectRemarksInsuredTxta.setMaxLength(4000);

		rejectRemarksInsuredTxta.setWidth("50%");
		rejectRemarksInsuredTxta.setHeight("250px");
		rejectRemarksInsuredTxta.setId("insuredRejRmrks");
		rejectRemarksInsuredTxta.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(rejectRemarksInsuredTxta,null);

		if(this.bean.getInsuredRejectionRemarks() != null){
			rejectRemarksInsuredTxta.setValue(this.bean.getInsuredRejectionRemarks());
		}

		withdrawAndRejectForm.addComponent(rejectRemarksInsuredTxta);

		HorizontalLayout remarksHLayout = new HorizontalLayout(withdrawForm,withdrawAndRejectForm);
		remarksHLayout.setWidth("100%");
		remarksHLayout.setSpacing(true);
		remarksHLayout.setMargin(true);

		fieldsLayout.addComponent(remarksHLayout);

		//			fieldsLayout.addComponent(withdrawAndRejectForm);

	}

	private void addWithDrawListenerListener() {

		reasonForWithdraw.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if (value != null) {
					fireViewEvent(WithdrawPreauthPostProcessWizardPresenter.STANDALONE_WITHDRAW_POST_GENERATE_REMARKS,value.getId(),SHAConstants.ENHANCEMENT_WITHDARW);
				}


			}
		});
	}

	private void addRejectionListener() {

		rejectionCategoryCmb.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if (value != null) {
					fireViewEvent(WithdrawPreauthPostProcessWizardPresenter.STANDALONE_WITHDRAW_POST_GENERATE_REMARKS,value.getId(),SHAConstants.ENHANCEMENT_REJECTION);
				}


			}
		});
	}

	public void setRemarks(MasterRemarks remarks, String decision) {
		String remarksValue = null;
		String insuredRemarksValue = null;

		if (remarks != null) {
			remarksValue = remarks.getRemarks();
			insuredRemarksValue = remarks.getInsuredRemarks();
		}
		if (decision.equals(SHAConstants.ENHANCEMENT_REJECTION)) {
			rejectionRemarksTxta.setValue(remarksValue);
		} else if (decision.equals(SHAConstants.ENHANCEMENT_WITHDARW)) {
			txtWithdrawRemarks.setValue(remarksValue);
		}

		if (decision.equals(SHAConstants.ENHANCEMENT_REJECTION)) {
			rejectRemarksInsuredTxta.setValue(insuredRemarksValue);
		} 
		if (decision.equals(SHAConstants.ENHANCEMENT_WITHDARW)) {
			withdrawRemarksInsuredTxta.setValue(insuredRemarksValue);
		}


	}

	public  void remarksPopupListener(TextArea searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForPreauthMedRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForMedicalRemarks(searchField, getShortCutListenerForRemarks(searchField));

	}

	public  void handleShortcutForMedicalRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
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


	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();
				final Window dialog = new Window();
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				txtArea.setData(bean);
				//txtArea.setStyleName("Boldstyle");
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				//txtArea.setSizeFull();


				//					if(("hospRmrks").equalsIgnoreCase(txtFld.getId()) || ("insuredRmrks").equalsIgnoreCase(txtFld.getId())){
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				dialog.setHeight("75%");
				dialog.setWidth("65%");
				txtArea.setReadOnly(false);
				//					}


				txtArea.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());						
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);


				String strCaption = "";

				if(("hospWithdrawRmrks").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Withdraw Remarks For Hospital";
				}
				else if(("hospRejRmrks").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Rejection Remarks For Hospital";
				}
				else if(("insuredWithdrawRmrks").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Withdraw Remarks For Insured)";
				}
				else if(("insuredRejRmrks").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Rejection Remarks For Insured)";
				}			    	
				dialog.setCaption(strCaption);


				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {

					@Override
					public void windowClose(CloseEvent e) {
						//							TextArea txtArea = (TextArea)dialog.getData();
						//							txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
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
						//							TextArea txtArea = (TextArea)dialog.getData();
						//							txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});	
			}
		};

		return listener;
	}

	public void showAlertForGMCParentLink(String policyNumber){	 

		Label successLabel = new Label(
				"<b style = 'color: red;'>Policy is  Linked to Policy No " + policyNumber + "</b>",
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
			}
		});
	}

}



