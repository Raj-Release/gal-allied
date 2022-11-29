package com.shaic.claim.negotiation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.GWizardListener;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.cashlessprocess.downsizeRequest.page.DownsizePreauthRequestWizardViewImpl;
import com.shaic.claim.preauth.dto.MedicalDecisionTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class ProcessNegotiationWizardViewImpl extends AbstractMVPView implements ProcessNegotiationWizard,GWizardListener{
	
	@Inject
	private Instance<ProcessNegotiationDetailsPage> processNegotiationViewInstace;
	
	private ProcessNegotiationDetailsPage processNegotiationObj;
	
	@Inject
	private Instance<NegotiationPreauthDataExtractionPage> negotiationPreauthDataExtractionPage;
	
	private NegotiationPreauthDataExtractionPage negotiationPreauthPageObj;
	
	@Inject
	private Instance<NegotiationDownsizePreauthPdfPage> downSizePreauthPdfPage;
	
	private NegotiationDownsizePreauthPdfPage downSizePreauthPdfObj;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	private PreauthDTO bean;
	
	private Map<String, Object> referenceData;
	
	private VerticalLayout mainPanel;
	
	
	private IWizardPartialComplete wizard;
	
	private final Logger log = LoggerFactory.getLogger(DownsizePreauthRequestWizardViewImpl.class);
	
	
	@PostConstruct
	public void initView() {
		addStyleName("view");
		setSizeFull();		
	}
	
	public void initView(PreauthDTO preauthDto, BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> escalateContainer)
	{

		this.wizard = new IWizardPartialComplete();
		wizard.getFinishButton().setCaption("Submit");
		this.bean = preauthDto;
		this.bean.getDownSizePreauthDataExtractionDetails().setDownsizeReason(selectValueContainer);
		this.bean.getDownSizePreauthDataExtractionDetails().setEscalateTo(escalateContainer);
		
		referenceData = new HashMap<String, Object>();
		
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(), ViewLevels.INTIMATION, false,"Downsize Pre-auth");
		
		mainPanel = new VerticalLayout();
		setHeight("100%");
		RevisedCashlessCarousel preauthIntimation = commonCarouselInstance.get();
//		preauthIntimation.init(bean.getNewIntimationDTO(), bean.getClaimDTO(),"Downsize Pre-auth");

		preauthIntimation.init(bean,"Process Negotiation");
		
		NegotiationPreauthDataExtractionPage negotiationPreauthPageObj=negotiationPreauthDataExtractionPage.get();
		negotiationPreauthPageObj.init(this.bean,this.wizard);
		this.negotiationPreauthPageObj=negotiationPreauthPageObj;
		this.negotiationPreauthPageObj.setupReferences(this.referenceData);
		
		NegotiationDownsizePreauthPdfPage downSizePreauthPdfObj=downSizePreauthPdfPage.get();
		downSizePreauthPdfObj.init(this.bean);
		this.downSizePreauthPdfObj=downSizePreauthPdfObj;

		
		this.processNegotiationObj = this.processNegotiationViewInstace.get();
		this.processNegotiationObj.init(preauthDto,this.wizard);
//		
		wizard.addStep(processNegotiationObj,"Negotiation Details");
		wizard.addStep(negotiationPreauthPageObj,"Downsize Preauth Page");
		wizard.addStep(downSizePreauthPdfObj,"Decision Communication");
		
		
		//wizard.addStep(withdrawPreauthPdfObj,"Decision Communication");
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		
		HorizontalLayout escalationDetailsLayout = null;
		
		if(bean.getDownSizePreauthDataExtrationDetails().getIsEscalateFromSpecialist()){
			
			escalationDetailsLayout = getSpecialistDetailsLayout();
			escalationDetailsLayout.setCaption("Specialist Details");
			escalationDetailsLayout.setMargin(false);
		}else{
			escalationDetailsLayout = getEscalationDetailsLayout();
			escalationDetailsLayout.setCaption("Escalation Details");
			escalationDetailsLayout.setMargin(false);
		}
		
		
		VerticalLayout wizardLayout2 = new VerticalLayout(wizard);
		mainPanel.addComponent(preauthIntimation);
		//mainPanel.addComponent(viewDetails);
		HorizontalLayout commonButton = new HorizontalLayout(commonButtonsLayout(),viewDetails);
		commonButton.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		commonButton.setWidth("100%");
		mainPanel.addComponent(commonButton);
		mainPanel.addComponent(wizardLayout2);
		mainPanel.setMargin(false);

		
		setCompositionRoot(mainPanel);
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
		HorizontalLayout alignmentHLayout = new HorizontalLayout(btnRRC);
		return alignmentHLayout;
	}
	
	public HorizontalLayout getSpecialistDetailsLayout(){
			
			TextField txtSpecialistType = new TextField("Specialist Type");
			txtSpecialistType.setEnabled(false);
			txtSpecialistType.setNullRepresentation("");
			
			TextArea txtSpecialistRemarks = new TextArea("Specialist Remarks");
			txtSpecialistRemarks.setEnabled(false);
			txtSpecialistRemarks.setNullRepresentation("");
			
			
			FormLayout firstForm = new FormLayout(txtSpecialistType,txtSpecialistRemarks);
			firstForm.setMargin(false);
			
			txtSpecialistType.setValue(this.bean.getDownSizePreauthDataExtrationDetails().getSpecialistType());
			String specialistRemarks = this.bean.getDownSizePreauthDataExtrationDetails().getSpecialistRemarks();
			txtSpecialistRemarks.setValue(specialistRemarks);
			txtSpecialistRemarks.setDescription(specialistRemarks);
			
			HorizontalLayout mainHor = new HorizontalLayout(firstForm);
			
			return mainHor;
		}
	public HorizontalLayout getEscalationDetailsLayout(){
			
			TextField txtEscalateRole = new TextField("Escalated Role");
			txtEscalateRole.setEnabled(false);
			txtEscalateRole.setNullRepresentation("");
			TextArea txtEscalateRemarks = new TextArea("Escalation Remarks");
			txtEscalateRemarks.setEnabled(false);
			txtEscalateRemarks.setNullRepresentation("");
			TextField txtUserName = new TextField("Escalated Role - ID/User Name");
			txtUserName.setEnabled(false);
			txtUserName.setNullRepresentation("");
			TextField txtDesignation = new TextField("Escalated Role - Designation");
			txtDesignation.setEnabled(false);
			txtDesignation.setNullRepresentation("");
			
			txtEscalateRole.setValue(this.bean.getDownSizePreauthDataExtrationDetails().getEscalatedRole());
			String escalationRemarks = this.bean.getDownSizePreauthDataExtrationDetails().getEscalationRemarks();
			txtEscalateRemarks.setValue(escalationRemarks);
			txtUserName.setValue(this.bean.getDownSizePreauthDataExtrationDetails().getEscalatedRole());
			txtEscalateRemarks.setDescription(escalationRemarks);
			
			FormLayout firstForm = new FormLayout(txtEscalateRole,txtEscalateRemarks);
			
			FormLayout secondForm = new FormLayout(txtUserName,txtDesignation);
			
			HorizontalLayout mainHor = new HorizontalLayout(firstForm,secondForm);
			
			return mainHor;
		}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(NegotiationPreauthRequestPresenter.VALIDATE_NEGOTIATION_REQUEST_USER_RRC_REQUEST, bean);//, secondaryParameters);
	}


	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activeStepChanged(WizardStepActivationEvent event) {
		fireViewEvent(NegotiationPreauthRequestPresenter.NEGOTIATION_STEP_CHANGE_EVENT, event);
	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardCompleted(WizardCompletedEvent event) {

		try{
			if(this.bean.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
				this.bean.setAgreedWith(processNegotiationObj.getAgreedRemarks());
				this.bean.setNegotiationagreedFlagValue(SHAConstants.YES_FLAG);
			} else {
				this.bean.setRegotiateRemarks(processNegotiationObj.getNegRemarks());
				this.bean.setNegotiationagreedFlagValue(SHAConstants.N_FLAG);
			}
			fireViewEvent(NegotiationPreauthRequestPresenter.NEGOTIATION_SUBMIT_EVENT, this.bean);
		}catch(Exception e)
		{
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
	
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {

		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
		        "Cancel", "Ok", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                    // Confirmed to continue
		                	
		                	
		                	wizard.releaseHumanTask();
							
		                	fireViewEvent(MenuItemBean.PROCESS_NEGOTIATION, null);
		                } else {
		                    // User did not confirm
		                }
		            }
		        });
		
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
	
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardSave(GWizardEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildSuccessLayout(String message) {

		Label label = new Label(message, ContentMode.HTML);
		label.setStyleName("errMessage");
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Process Negotiation Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(label, homeButton);
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
				fireViewEvent(MenuItemBean.PROCESS_NEGOTIATION, null);
				
			}
		});
	
	}

	@Override
	public void setDiagnosisSumInsuredValuesFromDB(
			List<MedicalDecisionTableDTO> medicalDecisionTableList) {
		negotiationPreauthPageObj.setReferenceData(medicalDecisionTableList);
	}

	@Override
	public void setDownsizeAmount(Double amount) {
		negotiationPreauthPageObj.downSizedAmount(amount);
	}

	@Override
	public void setNegotiationHospitalizationDetails(
			Map<Integer, Object> hospitalizationDetails) {
		this.processNegotiationObj.setHospitalizationDetails(hospitalizationDetails);
		
	}

	@Override
	public void setBalanceSumInsured(Double balanceSI, List<Double> copayValue) {
		negotiationPreauthPageObj.setBalanceSI(balanceSI, copayValue);
	}

	@Override
	public void setWizardPageReferenceData(Map<String, Object> referenceData) {
		WizardStep currentStep = this.wizard.getCurrentStep();
		if (currentStep != null)
		{
			currentStep.setupReferences(referenceData);	
		}
	}

	@Override
	public void setDiagnosisSumInsuredValuesFromDB(
			Map<String, Object> medicalDecisionTableValue, String diagnosis) {
		negotiationPreauthPageObj.setSumInsuredCaculationsForSublimit(medicalDecisionTableValue, diagnosis);
	}

	@Override
	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> icdCodeContainer) {
		negotiationPreauthPageObj.setExclusionDetails(icdCodeContainer);
	}

	@Override
	public void showErrorMessage() {
		negotiationPreauthPageObj.showErrorMessage();
	}

	@Override
	public void buildNegotiationRRCRequestSuccessLayout(String rrcRequestNo) {
		rewardRecognitionRequestViewObj.buildRRCRequestSuccessLayout(rrcRequestNo);
	}
	
	@Override
	public void viewClaimAmountDetails() {
		negotiationPreauthPageObj.showClaimAmountDetails();
	}
	
	@Override
	public void viewBalanceSumInsured(String intimationId) {
		
		negotiationPreauthPageObj.showBalanceSumInsured(intimationId);
	}

	@Override
	public void buildNegotiationValidationUserRRCRequestLayout(Boolean isValid) {

		
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
		rewardRecognitionRequestViewObj.initPresenter(SHAConstants.NEGOTIATION_PREAUTH_REQUEST_SCREEN);
		rewardRecognitionRequestViewObj.init(bean, popup);
		
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

	@Override
	public void loadNegotiationRRCRequestDropDownValues(
			BeanItemContainer<SelectValue> mastersValueContainer) {
		rewardRecognitionRequestViewObj.loadRRCRequestDropDownValues(mastersValueContainer)	;
	}
	
	@Override
	public void setReferenceDetailsForMedicalDecision(Map<String, Object> referenceData){
		this.referenceData = referenceData;
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
