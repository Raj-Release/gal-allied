package com.shaic.paclaim.cashless.withdraw.wizard;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.cashlessprocess.downsize.wizard.PreviousPreauthSummaryTable;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PAWithdrawPreauthPage extends ViewComponent implements WizardStep<PAWithdrawPreauthPageDTO>{
	
	
	private ComboBox reasonForWithdraw;
	
	private TextArea txtWithdrawRemarks;
	
	private TextArea medicalRemarks;
	
	private TextArea doctorNote;
	
	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;
	
	@Inject
	private PAWithdrawPreauthPageDTO bean;
	
	private BeanFieldGroup<PAWithdrawPreauthPageDTO> binder;
	
	@Inject
	private Instance<PreviousPreauthSummaryTable> preauthPreviousDetailsPage;
	
	private PreviousPreauthSummaryTable preauthPreviousDetailsPageObj;
	
//	private WithdrawPreauthTable withdrawPreauthTableObj;
	
	private BeanItemContainer<SelectValue> selectValueContainer;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private ArrayList<Component> mandatoryFieldsForMedicalRemarks = new ArrayList<Component>();
	
	@SuppressWarnings("unused")
	private NewIntimationDto newIntimationDto;
	
	private ClaimDto claimDto;
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;
	
	private VerticalLayout wholeVertical;
	
	private Button initiatePEDButton;
	
	private Button withdrawBtn;
	
	private Button withdrawAndRejectBtn;
	
	private FormLayout formLayout;

	private ComboBox rejectionCategoryCmb;

	private TextArea rejectionRemarksTxta;

	private Button submitButton;

	private HorizontalLayout fieldsLayout;
	
	private FormLayout withdrawForm;
	
	private FormLayout withdrawAndRejectForm;
	
	@Override
	public String getCaption() {
		return "Withdraw Pre-auth";
	}
	
	@PostConstruct
	public void init() {

	}

	@Override
	public void init(PAWithdrawPreauthPageDTO bean) {
		this.bean=bean;
		this.newIntimationDto=this.bean.getNewIntimationDto();
		this.claimDto=this.bean.getClaimDto();
		this.selectValueContainer=this.bean.getSelectValueContainer();
		
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<PAWithdrawPreauthPageDTO>(
				PAWithdrawPreauthPageDTO.class);
		this.binder.setItemDataSource(this.bean);
	}
	
	@Override
	public Component getContent() {
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		initiatePEDButton = new Button("Initiate PED Endorsement");
		HorizontalLayout buttonHLayout = new HorizontalLayout(initiatePEDButton);
		buttonHLayout.setMargin(true);
		buttonHLayout.setComponentAlignment(initiatePEDButton, Alignment.MIDDLE_RIGHT);
		initiatePEDButton.setEnabled(false);
		
//		withdrawPreauthTableObj=withdrawPreauthTableInstance.get();
//		withdrawPreauthTableObj.init("Pre-auth Summary", false);
		preauthPreviousDetailsPageObj = preauthPreviousDetailsPage.get();
		preauthPreviousDetailsPageObj.init("Pre-auth Summary", false, false);
		preauthPreviousDetailsPageObj.setTableList(this.bean.getPreviousPreAuthTableDTO());
		
		withdrawBtn = new Button("Withdraw");
		withdrawAndRejectBtn = new Button("Withdraw and Reject");
		
		HorizontalLayout btnLayout = new HorizontalLayout(withdrawBtn, withdrawAndRejectBtn);
		fieldsLayout = new HorizontalLayout();
		wholeVertical=new VerticalLayout(buttonHLayout,preauthPreviousDetailsPageObj,buildVertical(),btnLayout,fieldsLayout);
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
					alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
				} else {
					createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
				}				
			}
		});
		
		
		withdrawBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PAWithdrawPreauthWizardPresenter.CLICK_WITHDRAW_ACTION, null);
			}
		});
		
		
		
		withdrawAndRejectBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PAWithdrawPreauthWizardPresenter.CLICK_WITHDRAW_AND_REJECT_ACTION, null);
			}
		});
	}
	
	
	private void createPEDScreen(Long preauthKey, Long intimationKey,
			Long policyKey, Long claimKey) {
		ViewPEDRequestWindow viewPEDRequest = viewPedRequest.get();
		viewPEDRequest.initView(bean.getPreauthDto(),preauthKey,intimationKey,policyKey,claimKey,ReferenceTable.DOWNSIZE_STAGE,false);						
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
	
	

	
	
	public Boolean alertMessageForPEDInitiate(String message) {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
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
				Long preauthKey=bean.getPreauthDto().getKey();
				Long intimationKey=bean.getPreauthDto().getIntimationKey();
				Long policyKey=bean.getPreauthDto().getPolicyKey();
				Long claimKey=bean.getPreauthDto().getClaimKey();
				createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
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
		this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setMedicalRemarks(medicalRemarks.getValue());
		this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setWithdrawRemarks(txtWithdrawRemarks.getValue());
		this.bean.setWithdrawRemarks(txtWithdrawRemarks.getValue());
		this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setRejectionCategory(rejectionCategoryCmb != null ? (SelectValue)rejectionCategoryCmb.getValue() : null);
		this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().setRejectionRemarks(rejectionRemarksTxta != null ? rejectionRemarksTxta.getValue() : null);
		this.bean.getPreauthDto().setMedicalRemarks(medicalRemarks.getValue());
		return true;
		}
		else{
			return false;
		}
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
		String eMsg = "";
		if(fieldsLayout != null && fieldsLayout.getComponentCount() <= 0) {
			hasError = true;
			eMsg += "Please choose any action to proceed further.";
		}
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg += errMsg.getFormattedHtmlMessage();
				}
				hasError = true;
			}
		}
		if (hasError) {
			setRequired(true);
			setRequiredforRemarks(true);
			Label label = new Label(eMsg, ContentMode.HTML);
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
		reasonForWithdraw = (ComboBox) binder.buildAndBind("Reason for Withdrawal",
				"reasonForWithdraw", ComboBox.class);
		
		reasonForWithdraw.setContainerDataSource(selectValueContainer2);
		reasonForWithdraw.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForWithdraw.setItemCaptionPropertyId("value");
		
		txtWithdrawRemarks = (TextArea)binder.buildAndBind("Withdrawal Remarks","withdrawRemarks",TextArea.class);
		txtWithdrawRemarks.setMaxLength(4000);
		txtWithdrawRemarks.setWidth("400px");
		
		reasonForWithdraw.setNullSelectionAllowed(false);
		
		if(this.bean.getReasonForWithdraw() != null){
			reasonForWithdraw.setValue(this.bean.getReasonForWithdraw());
		}
		
		if(this.bean.getPreauthDto() != null && this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().getWithdrawRemarks() != null){
			txtWithdrawRemarks.setValue(this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().getWithdrawRemarks());
		}
		
		mandatoryFields.add(reasonForWithdraw);
		mandatoryFields.add(txtWithdrawRemarks);
		showOrHideValidation(false);
		
		if(withdrawForm == null) {
			withdrawForm = new FormLayout();
		}
		withdrawForm.addComponent(reasonForWithdraw);
		withdrawForm.addComponent(txtWithdrawRemarks);
		
		fieldsLayout.addComponent(withdrawForm);
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
	}

	public void buildWithdrawAndRejectLayout(
			BeanItemContainer<SelectValue> withdrawContainer,
			BeanItemContainer<SelectValue> rejectionContainer) {
		this.bean.setStatusKey(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS);
		mandatoryFields.removeAll(mandatoryFields);
		unBindFields();
		if(withdrawForm != null) {
			withdrawForm.removeAllComponents();
		}
		if(withdrawAndRejectForm != null) {
			withdrawAndRejectForm.removeAllComponents();
		}
		fieldsLayout.removeAllComponents();
		reasonForWithdraw = (ComboBox) binder.buildAndBind("Reason for Withdrawal",
				"reasonForWithdraw", ComboBox.class);
		
		reasonForWithdraw.setContainerDataSource(withdrawContainer);
		reasonForWithdraw.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForWithdraw.setItemCaptionPropertyId("value");
		
		txtWithdrawRemarks = (TextArea)binder.buildAndBind("Withdrawal Remarks","withdrawRemarks",TextArea.class);
		txtWithdrawRemarks.setMaxLength(4000);
		txtWithdrawRemarks.setWidth("400px");
		
		reasonForWithdraw.setNullSelectionAllowed(false);
		
		if(this.bean.getReasonForWithdraw() != null){
			reasonForWithdraw.setValue(this.bean.getReasonForWithdraw());
		}
		
		if(this.bean.getPreauthDto() != null && this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().getWithdrawRemarks() != null){
			txtWithdrawRemarks.setValue(this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().getWithdrawRemarks());
		}
		
		
		rejectionCategoryCmb = (ComboBox) binder.buildAndBind("Rejection Category","rejectionCategory",ComboBox.class);
		rejectionCategoryCmb.setContainerDataSource(rejectionContainer);
		rejectionCategoryCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		rejectionCategoryCmb.setItemCaptionPropertyId("value");
		
		if(this.bean.getRejectionCategory() != null){
			rejectionCategoryCmb.setValue(this.bean.getRejectionCategory());
		}
		
		rejectionRemarksTxta = (TextArea) binder.buildAndBind("Rejection Remarks","rejectionRemarks",TextArea.class);
		rejectionRemarksTxta.setMaxLength(4000);

		rejectionRemarksTxta.setWidth("400px");
		
		if(this.bean.getPreauthDto() != null && this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().getRejectionRemarks() != null){
			rejectionRemarksTxta.setValue(this.bean.getPreauthDto().getPreauthMedicalDecisionDetails().getRejectionRemarks());
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
		
		if(withdrawAndRejectForm == null) {
			withdrawAndRejectForm = new FormLayout();
		}
		
		withdrawAndRejectForm.addComponent(rejectionCategoryCmb);
		withdrawAndRejectForm.addComponent(rejectionRemarksTxta);
		
		fieldsLayout.addComponent(withdrawForm);
		fieldsLayout.addComponent(withdrawAndRejectForm);
		
	}
	
	
	
}
