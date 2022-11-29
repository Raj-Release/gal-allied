package com.shaic.claim.withdrawWizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.WizardStep;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.PedRaisedDetailsTable;
import com.shaic.claim.cashlessprocess.downsize.wizard.PreviousPreauthSummaryTable;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.MasterRemarks;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
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
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class WithdrawPreauthPage extends ViewComponent implements WizardStep<WithdrawPreauthPageDTO>{
	
	
	private ComboBox reasonForWithdraw;
	
	private TextArea txtWithdrawRemarks;
	
	private TextArea withdrawRemarksInsuredTxta;
	
	private TextArea rejectRemarksInsuredTxta;
	
	private TextArea medicalRemarks;
	
	private TextArea doctorNote;
	
	/*@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;*/
	
	@Inject
	private WithdrawPreauthPageDTO bean;
	
	private BeanFieldGroup<WithdrawPreauthPageDTO> binder;
	
	@Inject
	private Instance<PreviousPreauthSummaryTable> preauthPreviousDetailsPage;
	
	private PreviousPreauthSummaryTable preauthPreviousDetailsPageObj;
	
//	private WithdrawPreauthTable withdrawPreauthTableObj;
	
	//private BeanItemContainer<SelectValue> selectValueContainer;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private ArrayList<Component> mandatoryFieldsForMedicalRemarks = new ArrayList<Component>();
	
	/*@SuppressWarnings("unused")
	private NewIntimationDto newIntimationDto;*/
	
	//private ClaimDto claimDto;
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;
	
	////private static Window popup;
	
	private VerticalLayout wholeVertical;
	
	private Button initiatePEDButton;
	
	private Button withdrawBtn;
	
	private Button withdrawAndRejectBtn;
	
	private FormLayout formLayout;

	private ComboBox rejectionCategoryCmb;
	
	private TextField rejCondtNoTxt;

	private TextArea rejectionRemarksTxta;

	//private Button submitButton;

	private VerticalLayout fieldsLayout;
	
	private HorizontalLayout remarksLayout;
	
	private FormLayout withdrawForm;
	
	private FormLayout withdrawAndRejectForm;
	
	FormLayout userLayout = new FormLayout();
	private ComboBoxMultiselect cmbUserRoleMulti;
	private ComboBoxMultiselect cmbDoctorNameMulti;
	private TextArea remarksFromDeptHead;
	private Map<String, String> roleValidationContainer = new HashMap<String, String>();
	private Map<String, String> userValidationContainer = new HashMap<String, String>();
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@Inject 
	private Instance<PedRaisedDetailsTable> pedRaiseDetailsTable;
	
	private PedRaisedDetailsTable pedRaiseDetailsTableObj;
	
	@Override
	public String getCaption() {
		return "Withdraw Pre-auth";
	}
	
	@PostConstruct
	public void init() {

	}

	@Override
	public void init(WithdrawPreauthPageDTO bean) {
		this.bean=bean;
		/*this.newIntimationDto=this.bean.getNewIntimationDto();
		this.claimDto=this.bean.getClaimDto();
		this.selectValueContainer=this.bean.getSelectValueContainer();*/
		
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<WithdrawPreauthPageDTO>(
				WithdrawPreauthPageDTO.class);
		this.binder.setItemDataSource(this.bean);
	}
	
	@Override
	public Component getContent() {
		if(bean.getPreauthDto().getPolicyDto().getGmcPolicyType() != null && !bean.getPreauthDto().getPolicyDto().getGmcPolicyType().isEmpty() && bean.getPreauthDto().getPolicyDto().getLinkPolicyNumber() != null
				&& ((bean.getPreauthDto().getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT)) || (bean.getPreauthDto().getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT)))){
			showAlertForGMCParentLink(bean.getPreauthDto().getPolicyDto().getLinkPolicyNumber());
		}else if(bean.getPreauthDto().getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), bean.getPreauthDto().getIsSuspicious(), bean.getPreauthDto().getClmPrcsInstruction());
	    }
		
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		if(bean.getPreauthDto().isInsuredDeleted()){
			alertMessageForPEDInitiate(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getPreauthDto().getNewIntimationDTO().getInsuredPatient().getInsuredName()));
		}else{
			alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
		}
		
		initiatePEDButton = new Button("Initiate PED Endorsement");

		if(null != bean.getPreauthDto().getNewIntimationDTO() && null != bean.getPreauthDto().getNewIntimationDTO().getPolicy() 
				&& null != bean.getPreauthDto().getNewIntimationDTO().getPolicy().getProductType()
				&& null != bean.getPreauthDto().getNewIntimationDTO().getPolicy().getProductType().getKey()
				&& 2904 == bean.getPreauthDto().getNewIntimationDTO().getPolicy().getProductType().getKey().intValue()){
			initiatePEDButton.setEnabled(false);
		}
		else{
			initiatePEDButton.setEnabled(true);
		}
		
		HorizontalLayout buttonHLayout = new HorizontalLayout(initiatePEDButton);
		buttonHLayout.setMargin(true);
		buttonHLayout.setComponentAlignment(initiatePEDButton, Alignment.MIDDLE_RIGHT);
		
//		withdrawPreauthTableObj=withdrawPreauthTableInstance.get();
//		withdrawPreauthTableObj.init("Pre-auth Summary", false);
		preauthPreviousDetailsPageObj = preauthPreviousDetailsPage.get();
		preauthPreviousDetailsPageObj.init("Pre-auth Summary", false, false);
		preauthPreviousDetailsPageObj.setTableList(this.bean.getPreviousPreAuthTableDTO());
		
		withdrawBtn = new Button("Withdrawal of cashless approval");
		withdrawAndRejectBtn = new Button("Rejection and withdrawal");
		
		HorizontalLayout btnLayout = new HorizontalLayout(withdrawBtn, withdrawAndRejectBtn);
		fieldsLayout = new VerticalLayout();
		remarksLayout = new HorizontalLayout();
		remarksLayout.setSpacing(true);
		remarksLayout.setWidth("100%");
		remarksLayout.setMargin(true);
		
		wholeVertical=new VerticalLayout(buttonHLayout,preauthPreviousDetailsPageObj,buildVertical(),btnLayout,fieldsLayout,remarksLayout);
		wholeVertical.setSpacing(true);
		wholeVertical.setComponentAlignment(buttonHLayout, Alignment.TOP_RIGHT);
		//wholeVertical.setHeight("450px");
		
		addListener();
		
		return wholeVertical;
	}
	
	public FormLayout buildVertical(){
		medicalRemarks=(TextArea)binder.buildAndBind("Medical Remarks","medicalRemarks",TextArea.class);
		medicalRemarks.setMaxLength(100);
		medicalRemarks.setWidth("400px");
		
		mandatoryFieldsForMedicalRemarks.add(medicalRemarks);
		showOrHideValidationforRemarks(false);
		
		doctorNote=(TextArea)binder.buildAndBind("Doctor Note(Internal Purpose)", "doctorNote", TextArea.class);
		doctorNote.setMaxLength(100);
		doctorNote.setWidth("400px");
		
		formLayout=new FormLayout(medicalRemarks,doctorNote);
		
//		formLayout.setHeight("250px");
		
		
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
					createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
				}				
			}
		});
		
		
		withdrawBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(WithdrawPreauthWizardPresenter.CLICK_WITHDRAW_ACTION, null);
			}
		});
		
		
		
		withdrawAndRejectBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(WithdrawPreauthWizardPresenter.CLICK_WITHDRAW_AND_REJECT_ACTION, bean);
			}
		});
	}
	
	
	private void createPEDScreen(Long preauthKey, Long intimationKey,
			Long policyKey, Long claimKey) {
		ViewPEDRequestWindow viewPEDRequest = viewPedRequest.get();
		viewPEDRequest.initView(bean.getPreauthDto(),preauthKey,intimationKey,policyKey,claimKey,ReferenceTable.DOWNSIZE_STAGE,false);	
		viewPEDRequest.setPresenterString(SHAConstants.CASHLESS_STRING);
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
//		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		
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
//		dialog.setCaption("Alert");
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
//				Long preauthKey=bean.getPreauthDto().getKey();               // as per CR R1086 Auto deletion of Risk - Non Disclosed PED
//				Long intimationKey=bean.getPreauthDto().getIntimationKey();
//				Long policyKey=bean.getPreauthDto().getPolicyKey();
//				Long claimKey=bean.getPreauthDto().getClaimKey();
//				createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
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
		
		if(("").equals(medicalRemarks.getValue())){
			medicalRemarks.setValue(null);
		}
		
		if(validatePage()){
		SelectValue selected=new SelectValue();
		selected=(SelectValue)reasonForWithdraw.getValue();
		this.bean.setReasonForWithdraw(selected);
		this.bean.setMedicalRemarks(medicalRemarks.getValue());
		this.bean.setDoctorNote(doctorNote.getValue());
		this.bean.setTotalApprovedAmt(preauthPreviousDetailsPageObj.getTotalApprovedAmt());
		this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(preauthPreviousDetailsPageObj.getTotalApprovedAmt());
		this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setWithdrawReason(selected);
		this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setWithdrawRemarks(txtWithdrawRemarks.getValue());
		this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setWithDrawRemarksForInsured(withdrawRemarksInsuredTxta != null ? withdrawRemarksInsuredTxta.getValue() : null);
		this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setRejectionCategory(rejectionCategoryCmb != null ? (SelectValue)rejectionCategoryCmb.getValue() : null);
		this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setRejectionRemarks(rejectionRemarksTxta != null ? rejectionRemarksTxta.getValue() : null);
		this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setRemarksForInsured(rejectRemarksInsuredTxta != null ? rejectRemarksInsuredTxta.getValue() : null);
		this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setMedicalRemarks(medicalRemarks.getValue());
		this.bean.getPreauthDto().setMedicalRemarks(medicalRemarks.getValue());
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
		for (Component component : mandatoryFieldsForMedicalRemarks) {
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
		
		SelectValue rejCategorySelect = rejectionCategoryCmb != null && rejectionCategoryCmb.getValue() != null ? (SelectValue)rejectionCategoryCmb.getValue() : null;
		
		if(!ReferenceTable.getGMCProductList().containsKey(bean.getPreauthDto().getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			
			rejCategorySelect = reasonForWithdraw != null && reasonForWithdraw.getValue() != null ? (SelectValue)reasonForWithdraw.getValue() : null; 
		
		
		if(rejCategorySelect != null
			&& rejCategorySelect.getId() != null 
			&& SHAUtils.getPolCondionalExclutionRejKeys().containsKey(rejCategorySelect.getId()))
			{

			if(rejCondtNoTxt != null
					&& (rejCondtNoTxt.getValue() == null || rejCondtNoTxt.getValue().isEmpty())) {
				
				hasError = true;
				eMsg.append("Please Enter Condition Number.");
			}
			/*else{
				if (rejCondtNoTxt != null ) {
					Object propertyId = this.binder.getPropertyId(rejCondtNoTxt);
					if (rejCondtNoTxt != null && rejCondtNoTxt.isAttached() && propertyId != null) {
						rejCondtNoTxt.setValue(null);
						this.binder.unbind(rejCondtNoTxt);
					}
				}
			}*/

		}else{
			if (rejCondtNoTxt != null ) {
				Object propertyId = this.binder.getPropertyId(rejCondtNoTxt);
				if (rejCondtNoTxt != null && rejCondtNoTxt.isAttached() && propertyId != null) {
					rejCondtNoTxt.setValue(null);
					this.binder.unbind(rejCondtNoTxt);
				}
			}
		}
		}else{
			if (rejCondtNoTxt != null ) {
				Object propertyId = this.binder.getPropertyId(rejCondtNoTxt);
				if (rejCondtNoTxt != null && rejCondtNoTxt.isAttached() && propertyId != null) {
					rejCondtNoTxt.setValue(null);
					this.binder.unbind(rejCondtNoTxt);
				}
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
		
		if(bean.getPreauthDto().getPreauthMedicalDecisionDetails().getIsMandatory()){
			String nonMandateerrorMessage = "Please provide Consulted With, Opinion Given by, and Opinion Given or Make Consulted With, Opinion Given by, and Opinion Given as Empty"+"</br>";
			if(cmbUserRoleMulti != null && cmbDoctorNameMulti != null && remarksFromDeptHead != null){
				
				if (cmbUserRoleMulti.isEmpty() || cmbUserRoleMulti.getValue() == null) {
					hasError = true;
					eMsg.append("Please select Consulted With"+"</br>");
				}
				
				if (cmbDoctorNameMulti.isEmpty() || cmbDoctorNameMulti.getValue() == null) {
					hasError = true;
					eMsg.append("Please select Opinion Given by"+"</br>");
				}
			}
		}
		
		if(bean.getPreauthDto().getPreauthMedicalDecisionDetails().getIsMandatory()){
			if((cmbUserRoleMulti != null && cmbDoctorNameMulti != null && remarksFromDeptHead != null) 
					&& (remarksFromDeptHead.isEmpty() || remarksFromDeptHead.getValue() == null)){
				hasError = true;
				eMsg.append("Please Enter Opinion Given Remarks.");
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
				bean.getPreauthDto().getPreauthMedicalDecisionDetails().setPolicyConditionNoReject(bean.getPolicyConditionNoReject());
				bean.getPreauthDto().getPreauthMedicalDecisionDetails().setRemarksForInsured(bean.getInsuredWithdrawRemarks());
				 //added for preauth approved date in withdraw letter issue(IMSSUPPOR-28726).
				fireViewEvent(WithdrawPreauthWizardPresenter.PREAUTH_APPROVED_DATE_WITHDRAW_PAGE,bean);
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

		if (!mandatoryFieldsForMedicalRemarks.isEmpty()) {
			for (int i = 0; i < mandatoryFieldsForMedicalRemarks.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFieldsForMedicalRemarks
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
		
		this.bean.setStatusKey(ReferenceTable.STANDALONE_WITHDRAW_STATUS);
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
		mandatoryFields.add(medicalRemarks);
		mandatoryFields.add(txtWithdrawRemarks);
		showOrHideValidation(false);
		
		if(withdrawForm == null) {
			withdrawForm = new FormLayout();
		}
		withdrawForm.addComponent(reasonForWithdraw);
		
		if(remarksLayout == null){
			remarksLayout = new HorizontalLayout();
		}
		
		remarksLayout.addComponents(new FormLayout(txtWithdrawRemarks),withdrawRemarksInsuredTxta);
		remarksLayout.setSpacing(true);
		
		userLayout = buildUserRoleLayout();
		userLayout.setMargin(Boolean.TRUE);
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(withdrawForm,userLayout);
		alignFormComponents();
		
		fieldsLayout.addComponent(hLayout);
		wholeVertical.addComponent(remarksLayout);
	}
	
	@SuppressWarnings("deprecation")
	private void alignFormComponents()
	{
		if(withdrawForm != null)
		{
			for(int i=0; i<withdrawForm.getComponentCount();i++)
			{
				withdrawForm.setExpandRatio(withdrawForm.getComponent(i), 0.5f);
			}
		}
	}
	
	private void unBindFields() {
		if (reasonForWithdraw != null ) {
			Object propertyId = this.binder.getPropertyId(reasonForWithdraw);
			if (reasonForWithdraw!= null && reasonForWithdraw.isAttached() && propertyId != null) {
				reasonForWithdraw.setValue(null);
				this.binder.unbind(reasonForWithdraw);
			}
		}
		
		if (rejCondtNoTxt != null ) {
			Object propertyId = this.binder.getPropertyId(rejCondtNoTxt);
			if (rejCondtNoTxt != null && rejCondtNoTxt.isAttached() && propertyId != null) {
				rejCondtNoTxt.setValue(null);
				this.binder.unbind(rejCondtNoTxt);
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
		
		if (remarksFromDeptHead != null ) {
			Object propertyId = this.binder.getPropertyId(remarksFromDeptHead);
			if (remarksFromDeptHead!= null && remarksFromDeptHead.isAttached() && propertyId != null) {
				remarksFromDeptHead.setValue(null);
				this.binder.unbind(remarksFromDeptHead);
			}
		}
		
		if (userLayout != null) {
			userLayout.removeAllComponents();
		}
		if (withdrawForm != null) {
			withdrawForm.removeAllComponents();
		}
		if (userLayout != null) {
			wholeVertical.removeComponent(userLayout);
		}
		if (withdrawForm != null) {
			wholeVertical.removeComponent(withdrawForm);
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
		mandatoryFields.add(medicalRemarks);
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
		
		userLayout = buildUserRoleLayout();
		userLayout.setMargin(Boolean.TRUE);
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(withdrawForm,userLayout);
		alignFormComponents();
		
		HorizontalLayout remarksHLayout = new HorizontalLayout(hLayout,withdrawAndRejectForm);
		remarksHLayout.setWidth("100%");
		remarksHLayout.setSpacing(true);
		remarksHLayout.setMargin(true);
		
		fieldsLayout.addComponent(remarksHLayout);
		
//		fieldsLayout.addComponent(withdrawAndRejectForm);
		
	}
	
	private void addWithDrawListenerListener() {
			
		reasonForWithdraw.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue value = (SelectValue) event.getProperty().getValue();
					if (value != null) {
						fireViewEvent(WithdrawPreauthWizardPresenter.STANDALONE_WITHDRAW_GENERATE_REMARKS,value.getId(),SHAConstants.ENHANCEMENT_WITHDARW);
					}
					
					
				}
			});
	}
	
	private void addRejectWithDrawListenerListener() {
		
		reasonForWithdraw.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue value = (SelectValue) event.getProperty().getValue();
					if (value != null) {
						
						if(SHAUtils.getPolCondionalExclutionRejKeys().containsKey(value.getId())) {
							mandatoryFields.add(rejCondtNoTxt);
							rejCondtNoTxt.setVisible(true);
							showOrHideValidation(false);
						}
						else {
							rejCondtNoTxt.setVisible(false);
							mandatoryFields.remove(rejCondtNoTxt);
						}
						
						fireViewEvent(WithdrawPreauthWizardPresenter.STANDALONE_WITHDRAW_GENERATE_REMARKS,value.getId(),SHAConstants.ENHANCEMENT_WITHDRAW_REJECT);
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
					
					fireViewEvent(WithdrawPreauthWizardPresenter.STANDALONE_WITHDRAW_GENERATE_REMARKS,value.getId(),SHAConstants.ENHANCEMENT_REJECTION);
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
			
			SelectValue rejCatgId = reasonForWithdraw != null ? (SelectValue)reasonForWithdraw.getValue() : null;
			if(rejCatgId != null 
					&& SHAUtils.getPolCondionalExclutionRejKeys().containsKey(rejCatgId.getId())) {
				String [] userRemarks = remarks.getInsuredRemarks().split(";");
				
				if(userRemarks.length == 2){
					insuredRemarksValue = userRemarks[0];
					bean.getPreauthDto().getPreauthMedicalDecisionDetails().setPolicyRejectConditClause(userRemarks[userRemarks.length-1]);
				}
			}
			
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
		
		if (decision.equals(SHAConstants.ENHANCEMENT_WITHDRAW_REJECT) && txtWithdrawRemarks != null) {
			txtWithdrawRemarks.setValue(remarksValue);
		}
		
		if (decision.equals(SHAConstants.ENHANCEMENT_WITHDRAW_REJECT) && withdrawRemarksInsuredTxta != null) {
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
				
				
//				if(("hospRmrks").equalsIgnoreCase(txtFld.getId()) || ("insuredRmrks").equalsIgnoreCase(txtFld.getId())){
					txtArea.setRows(25);
					txtArea.setHeight("30%");
					txtArea.setWidth("100%");
					dialog.setHeight("75%");
			    	dialog.setWidth("65%");
					txtArea.setReadOnly(false);
//				}
				
				
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
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
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
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
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
	
	public void buildWithdrawAndRejectLayout(
			BeanItemContainer<SelectValue> withdrawRejectionContainer) {
		
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
		reasonForWithdraw = (ComboBox) binder.buildAndBind("Reason for Reject & Withdraw",
				"reasonForWithdraw", ComboBox.class);
		
		reasonForWithdraw.setWidth("300px");
				
		rejCondtNoTxt = (TextField) binder.buildAndBind("Condition Number","policyConditionNoReject",TextField.class);
		rejCondtNoTxt.setWidth("55px");
		rejCondtNoTxt.setMaxLength(10);
		rejCondtNoTxt.setVisible(false);
		
		reasonForWithdraw.setContainerDataSource(withdrawRejectionContainer);
		reasonForWithdraw.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForWithdraw.setItemCaptionPropertyId("value");
		addRejectWithDrawListenerListener();

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
		
		mandatoryFields.add(reasonForWithdraw);
		mandatoryFields.add(txtWithdrawRemarks);
		mandatoryFields.add(medicalRemarks);
		
		showOrHideValidation(false);
		
		if(withdrawForm == null) {
			withdrawForm = new FormLayout();
		}
		
		withdrawForm.addComponent(reasonForWithdraw);
		withdrawForm.addComponent(rejCondtNoTxt);
		withdrawForm.addComponent(txtWithdrawRemarks);
		withdrawForm.addComponent(withdrawRemarksInsuredTxta);
		
		userLayout = buildUserRoleLayout();
		userLayout.setMargin(Boolean.TRUE);
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(withdrawForm,userLayout);
		alignFormComponents();
		
		HorizontalLayout remarksHLayout = new HorizontalLayout(hLayout);
		remarksHLayout.setWidth("100%");
		remarksHLayout.setSpacing(true);
		remarksHLayout.setMargin(true);
		
		fieldsLayout.addComponent(remarksHLayout);
		
//		fieldsLayout.addComponent(withdrawAndRejectForm);
		
	}

	public FormLayout buildUserRoleLayout(){
		String enhancementType = (bean.getPreauthDto().getPreauthDataExtractionDetails().getInterimOrFinalEnhancement() != null && bean.getPreauthDto().getPreauthDataExtractionDetails().getInterimOrFinalEnhancement() == true)?"I":"F";
		String hospitilizationType = "Y";
		
		Integer qryTyp;
		if (bean.getPreauthDto().getQueryType() == null){
			qryTyp = 0;
		}else{
			qryTyp = bean.getPreauthDto().getQueryType().getId().intValue();
		}
		Integer qryCnt;
		if (bean.getPreauthDto().getQueryCount() == null){
			qryCnt = 0;
		}else{
			qryCnt = bean.getPreauthDto().getQueryCount() + 1;
		}
		
		String finalClaimAmount = "";
		finalClaimAmount = bean.getPreauthDto().getAmountConsidered();
		finalClaimAmount = (finalClaimAmount == null)?"0":finalClaimAmount;
		bean.getPreauthDto().getPreauthMedicalDecisionDetails().setFinalClaimAmout(Long.valueOf(finalClaimAmount));
		
		System.out.println("Withdrawal : User Role For Intimation No "+bean.getPreauthDto().getNewIntimationDTO().getIntimationId());
		Map<String,Object> opinionValues = dbCalculationService.getOpinionValidationDetails(Long.valueOf(finalClaimAmount),bean.getPreauthDto().getStageKey(),bean.getStatusKey(),
				Long.valueOf(bean.getPreauthDto().getNewIntimationDTO().getCpuCode()),"N",bean.getPreauthDto().getNewIntimationDTO().getPolicy().getKey(),
				bean.getPreauthDto().getClaimDTO().getKey(),enhancementType, hospitilizationType,qryTyp,qryCnt,
				bean.getPreauthDto().getClaimDTO().getClaimType().getId(),
				ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL,SHAConstants.ENHANCEMENT,bean.getPreauthDto().getKey(), bean.getStatusKey(),SHAConstants.N_FLAG); 
		
		BeanItemContainer<SpecialSelectValue> userRole = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
		userRole.addAll((List<SpecialSelectValue>)opinionValues.get("role"));
		BeanItemContainer<SpecialSelectValue> userRoleWithoutSGm = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
		for (SpecialSelectValue component : userRole.getItemIds()) {
			if(!component.getSpecialValue().equalsIgnoreCase("R0007")) {
				userRoleWithoutSGm.addItem(component);
			}
		}
		
		BeanItemContainer<SpecialSelectValue> empNames = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
		empNames.addAll((List<SpecialSelectValue>) opinionValues.get("emp"));
		
		if(null != opinionValues){			
			String mandatoryFlag =  (String) opinionValues.get("mandatoryFlag");
			if(null != mandatoryFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(mandatoryFlag)){
				bean.getPreauthDto().getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.TRUE);
			}
			else{
				bean.getPreauthDto().getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
			}
			
			String portedFlag =  (String) opinionValues.get("portedFlag");
			if(null != portedFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(portedFlag)){
				bean.getPreauthDto().getPreauthMedicalDecisionDetails().setIsPortedPolicy(Boolean.TRUE);
			}else{
				bean.getPreauthDto().getPreauthMedicalDecisionDetails().setIsPortedPolicy(Boolean.FALSE);
			}
	
			String seriousDefiencyFlagForOpinion = (String) opinionValues.get("seriousDeficiencyFlag");
			System.out.println("seriousDefiencyFlagForOpinion : "+seriousDefiencyFlagForOpinion);
			if(null != portedFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(seriousDefiencyFlagForOpinion)){
				bean.getPreauthDto().getPreauthMedicalDecisionDetails().setIsSDMarkedForOpinion(Boolean.TRUE);
			}else if(null != portedFlag && SHAConstants.N_FLAG.equalsIgnoreCase(seriousDefiencyFlagForOpinion)){
				bean.getPreauthDto().getPreauthMedicalDecisionDetails().setIsSDMarkedForOpinion(Boolean.FALSE);
			}else{
				bean.getPreauthDto().getPreauthMedicalDecisionDetails().setIsSDMarkedForOpinion(null);
			}
		}
	
		cmbUserRoleMulti = new ComboBoxMultiselect("Consulted With");
		cmbUserRoleMulti.setShowSelectedOnTop(true);
		cmbUserRoleMulti.setComparator(SHAUtils.getComparator());
		cmbUserRoleMulti.setContainerDataSource(userRoleWithoutSGm);
		cmbUserRoleMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbUserRoleMulti.setItemCaptionPropertyId("value");	
		cmbUserRoleMulti.setData(userRoleWithoutSGm);
		
				
		cmbDoctorNameMulti = new ComboBoxMultiselect("Opinion Given by");
		cmbDoctorNameMulti.setShowSelectedOnTop(true);
		cmbDoctorNameMulti.setContainerDataSource(empNames);
		cmbDoctorNameMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDoctorNameMulti.setItemCaptionPropertyId("commonValue");
		cmbDoctorNameMulti.setData(empNames);
		
		bean.getPreauthDto().getPreauthMedicalDecisionDetails().setRemarksFromDeptHead(null);
		remarksFromDeptHead = (TextArea) binder.buildAndBind("Opinion Given", "remarksFromDeptHead",TextArea.class);		
		remarksFromDeptHead.setMaxLength(2000);
		remarksFromDeptHead.setWidth("400px");
		remarksFromDeptHead.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopup(remarksFromDeptHead,null,getUI());
		remarksFromDeptHead.setData(bean);
		addUserRoleListener();
		userLayout = new FormLayout();
		userLayout.addComponents(cmbUserRoleMulti,cmbDoctorNameMulti,remarksFromDeptHead);
		userLayout.setMargin(Boolean.TRUE);
		userLayout.setSpacing(Boolean.TRUE);
		if(bean.getPreauthDto().getPreauthMedicalDecisionDetails().getIsMandatory()){
			mandatoryFields.add(cmbUserRoleMulti);
			mandatoryFields.add(cmbDoctorNameMulti);
			mandatoryFields.add(remarksFromDeptHead);
			showOrHideValidation(false);
		}
		else{
			mandatoryFields.remove(cmbUserRoleMulti);
			mandatoryFields.remove(cmbDoctorNameMulti);
			mandatoryFields.remove(remarksFromDeptHead);
		}
		return userLayout;	
		
	}
	
	public void addUserRoleListener(){
		getRoleValidationContainer().clear();
		getUserValidationContainer().clear();
		cmbUserRoleMulti.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				
				BeanItem<WithdrawPreauthPageDTO> dtoBeanObject = binder.getItemDataSource();
				WithdrawPreauthPageDTO dtoObject = dtoBeanObject.getBean();
//				dtoObject.setUserRoleMulti(null);
				dtoObject.setUserRoleMulti(event.getProperty().getValue());
				
				if(cmbDoctorNameMulti != null && cmbDoctorNameMulti.getData() != null){
					BeanItemContainer<SpecialSelectValue> listOfDoctors = (BeanItemContainer<SpecialSelectValue>) cmbDoctorNameMulti.getData();
					List<String> docList = preauthService.getListFromMultiSelectComponent(event.getProperty().getValue());
					BeanItemContainer<SpecialSelectValue> listOfRoleContainer = (BeanItemContainer<SpecialSelectValue>)cmbUserRoleMulti.getData();
					getRoleValidationContainer().clear();
					List<String> roles = new ArrayList<String>();
					List<SpecialSelectValue> listOfRoles = listOfRoleContainer.getItemIds();
					for (SpecialSelectValue specialSelectValue : listOfRoles) {
						if(null != docList && !docList.isEmpty() &&docList.contains(specialSelectValue.getValue())){
							roles.add(specialSelectValue.getSpecialValue());
							if(!getRoleValidationContainer().containsKey(specialSelectValue.getSpecialValue())){
								getRoleValidationContainer().put(specialSelectValue.getSpecialValue(), specialSelectValue.getValue());
							}
						}
						
					}
					List<SpecialSelectValue> filtersValue = new ArrayList<SpecialSelectValue>();
					List<SpecialSelectValue> itemIds = listOfDoctors.getItemIds();
					for (SpecialSelectValue specialSelectValue : itemIds) {
						if( specialSelectValue.getSpecialValue() != null && 
								roles.contains(specialSelectValue.getSpecialValue())){
							filtersValue.add(specialSelectValue);
						}
					}
					
					BeanItemContainer<SpecialSelectValue> filterContainer = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
					filterContainer.addAll(filtersValue);
					cmbDoctorNameMulti.setContainerDataSource(filterContainer);
					cmbDoctorNameMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbDoctorNameMulti.setItemCaptionPropertyId("commonValue");
				}
			}
		});
		
		cmbDoctorNameMulti.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				BeanItem<WithdrawPreauthPageDTO> dtoBeanObject = binder.getItemDataSource();
				WithdrawPreauthPageDTO dtoObject = dtoBeanObject.getBean();
//				dtoObject.setDoctorName(null);
				dtoObject.setDoctorName(event.getProperty().getValue());
				Set selectedObject = new HashSet<>((Collection) event.getProperty().getValue());
				getUserValidationContainer().clear();
				List<SpecialSelectValue> listOfUserSelected = new ArrayList<SpecialSelectValue>(selectedObject);
				if(listOfUserSelected.size() > 0){
					for(SpecialSelectValue specialSelectValue : listOfUserSelected){
						if(!getUserValidationContainer().containsKey(specialSelectValue.getSpecialValue())){
							getUserValidationContainer().put(specialSelectValue.getSpecialValue(), specialSelectValue.getValue());
						}else{
							String temp = getUserValidationContainer().get(specialSelectValue.getSpecialValue());
							if(temp.contains(specialSelectValue.getValue())){
								getUserValidationContainer().put(specialSelectValue.getSpecialValue(),specialSelectValue.getValue());
							}else{
								getUserValidationContainer().put(specialSelectValue.getSpecialValue(), temp+","+specialSelectValue.getValue());
							}
						}
					}
				}else{
					getUserValidationContainer().clear();
				}
				List<String> docList = preauthService.getListFromMultiSelectComponent(event.getProperty().getValue());
				BeanItemContainer<SpecialSelectValue> listOfDoctors = (BeanItemContainer<SpecialSelectValue>) cmbDoctorNameMulti.getData();
				bean.getPreauthDto().getPreauthMedicalDecisionDetails().setDoctorContainer(listOfDoctors);
			}
		});
	}
	
	public Map<String, String> getRoleValidationContainer() {
		return roleValidationContainer;
	}
	
	public void setRoleValidationContainer(
			Map<String, String> roleValidationContainer) {
		this.roleValidationContainer = roleValidationContainer;
	}
	
	public Map<String, String> getUserValidationContainer() {
		return userValidationContainer;
	}

}
