package com.shaic.claim.outpatient.processOP.wizard;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.outpatient.OutpatientProcessClaimCarousel;
import com.shaic.claim.outpatient.processOP.pages.assessmentsheet.OPClaimAssessmentPageViewImpl;
import com.shaic.claim.outpatient.processOP.pages.claimDecision.OPClaimDecisionPageViewImpl;
import com.shaic.claim.outpatient.processOP.pages.settlement.OPClaimSettlementPageViewImpl;
import com.shaic.claim.outpatient.processOPpages.ProcessOPClaimDetailsPage;
import com.shaic.claim.outpatient.processOPpages.assessmentsheet.OPBillAssessmentPageViewImpl;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizard;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class ProcessOPClaimWizardViewImpl extends AbstractMVPView implements ProcessOPClaimWizard {
	private static final long serialVersionUID = -8044167858989544230L;

//	@Inject
//	private Instance<IWizard> iWizard;
	
	private IWizardPartialComplete wizard;
	
	private OutPatientDTO bean;
	
	private FieldGroup binder;

	private VerticalLayout mainLayout;
	
	@Inject
	private Instance<OPClaimDecisionPageViewImpl> opClaimDecisionPageViewImpl;
	
	@Inject
	private Instance<OPClaimAssessmentPageViewImpl> opClaimAssessmentPageImpl;
	
	/*@Inject
	private Instance<OPClaimSettlementPageViewImpl> opClaimSettlementPageImpl;*/
	
	@Inject
	private Instance<OPBillAssessmentPageViewImpl> opBillAssessmentPageImpl;
	
	
	
	private OPClaimDecisionPageViewImpl claimDetailsPageViewImplObj;
	
	private OPClaimAssessmentPageViewImpl opClaimAssessmentPageViewObj;
	
	private OPClaimSettlementPageViewImpl opClaimSettlementPageViewObj;

	private OPBillAssessmentPageViewImpl opBillAssessmentPageViewObj;
	
	
	@Inject
	private Instance<OutpatientProcessClaimCarousel> commonCarouselInstance;
	

	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private Toolbar toolBar;
	
	
	@Override
	public void resetView() {
		if(null != this.wizard && !this.wizard.getSteps().isEmpty()) {
			this.wizard.clearWizardMap("OP Claim Details");
			this.wizard.clearWizardMap("OP Billing & Settlement");
//			this.wizard.clearWizardMap("OP Settlement");
			this.wizard.clearWizardMap("OP Assessment");
			this.wizard.clearCurrentStep();
		}
	}
	
	public void initView(OutPatientDTO bean) {
		this.bean = bean;
		mainLayout = new VerticalLayout();
		this.wizard = new IWizardPartialComplete();
		initBinder();
		
		claimDetailsPageViewImplObj = opClaimDecisionPageViewImpl.get();
		claimDetailsPageViewImplObj.init(this.bean , wizard);
		wizard.addStep(claimDetailsPageViewImplObj,"OP Claim Details");
		
		opClaimAssessmentPageViewObj = opClaimAssessmentPageImpl.get();
		opClaimAssessmentPageViewObj.init(this.bean, wizard);
		wizard.addStep(opClaimAssessmentPageViewObj,"OP Billing & Settlement");
		
//		opClaimSettlementPageViewObj = opClaimSettlementPageImpl.get();
//		opClaimSettlementPageViewObj.init(this.bean, wizard);
//		wizard.addStep(opClaimSettlementPageViewObj,"OP Settlement");		
		
		opBillAssessmentPageViewObj = opBillAssessmentPageImpl.get();
		opBillAssessmentPageViewObj.init(this.bean, wizard);
		wizard.addStep(opBillAssessmentPageViewObj, "OP Assessment");	
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		
		OutpatientProcessClaimCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.bean.getPolicy(), this.bean.getPioName());
//		mainPanel.setFirstComponent(intimationDetailsCarousel);
		mainLayout.addComponent(intimationDetailsCarousel);
		
		/*HorizontalLayout commonButtonsLayout = commonButtonsLayout();
		commonButtonsLayout.setWidth("100%");*/
		
		/*VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout);
		wizardLayout1.setComponentAlignment(commonButtonsLayout, Alignment.TOP_RIGHT);*/
		
		mainLayout.addComponent(commonButtonsLayout());
		
//		Panel panel1 = new Panel();
//		panel1.setContent(wizardLayout1);
//	//	panel1.setHeight("60px");
//		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
//		wizardLayout2.setSpacing(true);
		//mainPanel.setSecondComponent(wizardLayout2);
//		mainPanel.setSizeFull();
//		mainPanel.setSplitPosition(13, Unit.PERCENTAGE);
//		mainPanel.setHeight("700px");
		mainLayout.addComponent(wizard);
		mainLayout.setSpacing(true);
		setCompositionRoot(mainLayout);
	}
	
	private HorizontalLayout commonButtonsLayout() {
		Label claimNo = new Label("Claim Number :   "+ bean.getClaimDTO().getClaimId());
		claimNo.setWidth("770px");
		
		FormLayout viewDetailsForm = new FormLayout();
		viewDetailsForm.addComponent(claimNo);
		viewDetailsForm.setHeight("25px");
		
		Boolean oPflag = true;
		if(bean.getHealthCheckupFlag()){
			oPflag=false;
		}
		//viewDetails.initViewForProcessClaimRegistraionOP(bean.getNewIntimationDTO().getIntimationId(),ViewLevels.OUPATIENT,true);
		viewDetails.initViewForProcessClaimRegistraionOP(bean.getPolicy().getPolicyNumber(),bean.getNewIntimationDTO().getIntimationId(), bean.getPolicy().getKey(), ViewLevels.OUPATIENT,true,bean.getNewIntimationDTO().getInsuredPatient(),oPflag);
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		
//		viewDetailsForm.addComponent(claimNo);
		HorizontalLayout horizontalLayout1 = new HorizontalLayout(claimNo, viewDetails);
		horizontalLayout1.addComponent(viewDetailsForm);
		/*horizontalLayout1.setComponentAlignment(claimNo, Alignment.MIDDLE_LEFT);
		horizontalLayout1.setComponentAlignment(viewDetails, Alignment.MIDDLE_RIGHT);*/
		//Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
		horizontalLayout1.setSpacing(true);
		horizontalLayout1.setComponentAlignment(claimNo, Alignment.TOP_LEFT);
		horizontalLayout1.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
		horizontalLayout1.setWidth("145%");
		return horizontalLayout1;
	}

	private void initBinder() {
		wizard.getFinishButton().setCaption("Submit");
		wizard.getNextButton().setEnabled(false);
		this.binder = new FieldGroup();
		BeanItem<OutPatientDTO> item = new BeanItem<OutPatientDTO>(bean);
//		item.addNestedProperty("documentDetails");
//		item.addNestedProperty("opBillEntryDetails");
//		
//
//		/*item.addNestedProperty("documentDetails.claimType");
//		item.addNestedProperty("documentDetails.insuredPatientName");
//		item.addNestedProperty("documentDetails.amountClaimed");
//		item.addNestedProperty("documentDetails.provisionAmt");
//		item.addNestedProperty("documentDetails.documentReceivedFrom");
//		item.addNestedProperty("documentDetails.documentReceivedDate");
//		item.addNestedProperty("documentDetails.modeOfReceipt");
//		item.addNestedProperty("documentDetails.acknowledgementContactNumber");
//		item.addNestedProperty("documentDetails.emailID");
//		item.addNestedProperty("documentDetails.additionalRemarks");
//		item.addNestedProperty("documentDetails.paymentModeFlag");
//		item.addNestedProperty("documentDetails.payeeName");
//		item.addNestedProperty("documentDetails.payeeEmailId");
//		item.addNestedProperty("documentDetails.panNo");
//		item.addNestedProperty("documentDetails.approvalRemarks");
//		item.addNestedProperty("documentDetails.rejectionRemarks");
//		item.addNestedProperty("documentDetails.payableAt");
//		item.addNestedProperty("documentDetails.accountNo");
//		item.addNestedProperty("documentDetails.ifscCode");
//		item.addNestedProperty("documentDetails.branch");
//		item.addNestedProperty("documentDetails.bankName");
//		item.addNestedProperty("documentDetails.city");*/
		this.binder.setItemDataSource(item);	
	}

	@Override
	public void wizardSave(GWizardEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activeStepChanged(WizardStepActivationEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardCompleted(WizardCompletedEvent event) {
		fireViewEvent(ProcessOPClaimWizardPresenter.PROECSS_OP_SUBMITTED_EVENT, bean);
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		ConfirmDialog dialog = ConfirmDialog
				.show(getUI(),
						"Confirmation",
						"Are you sure you want to cancel ?",
						"No", "Yes", new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 1L;

							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									// Confirmed to continue
									claimDetailsPageViewImplObj.clearTable();
									fireViewEvent(MenuItemBean.PORCESS_CLAIM_OP,
											null);
								} else {
									// User did not confirm
								}
							}
						});

		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("static-access")
	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label("<b style = 'color: green;'> Claim Information Saved Successfully !!!</b>", ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Home Page");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
//		Button acknowledgeLetterBtn = new Button("View Assessment Sheet");
//		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setSpacing(true);
		
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
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
				toolBar.opcountTool();
				fireViewEvent(MenuItemBean.PORCESS_CLAIM_OP, null);

			}
		});
		
//		acknowledgeLetterBtn.addClickListener(new ClickListener() {
//			private static final long serialVersionUID = 7396240433865727954L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				dialog.close();
//				fireViewEvent(MenuItemBean.PORCESS_CLAIM_OP, null);
//
//			}
//		});
		
	}

	@Override
	public void setHospitalDetails(HospitalDto dto) {
		claimDetailsPageViewImplObj.setHospitalDetails(dto);
	}
	
	@Override
	public void setDiagnosticHospitalDetails(HospitalDto dto) {
		claimDetailsPageViewImplObj.setDiagnosticHospitalDetails(dto);
	}
	
	@Override
	public void setUpPayableDetails(String payableAt) {
		opClaimAssessmentPageViewObj.setPayableDtls(payableAt);		
	}

}
