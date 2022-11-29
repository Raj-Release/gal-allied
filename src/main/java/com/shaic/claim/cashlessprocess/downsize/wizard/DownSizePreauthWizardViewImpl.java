package com.shaic.claim.cashlessprocess.downsize.wizard;

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
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.cashlessprocess.downsizeRequest.page.DownsizePreauthRequestPdfPage;
import com.shaic.claim.preauth.dto.MedicalDecisionTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.MasterRemarks;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
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
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class DownSizePreauthWizardViewImpl extends AbstractMVPView implements DownsizePreauthWizard, GWizardListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Instance<DownsizePreauthDataExtractionPage> downSizePreauthDataExtractionPage;
	
	@Inject
	private Instance<DownsizePreauthRequestPdfPage> downSizePreauthPdfPage;
	
	private DownsizePreauthRequestPdfPage downSizePreauthPdfObj;
	
	@Inject
	private Instance<ClaimedAmountDetailsPage> claimedAmountDetailsPageInstance;
	
	private ClaimedAmountDetailsPage claimedAmountDetailsPageObj;

	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;
	
	private DownsizePreauthDataExtractionPage downsizePreauthPageObj;
	
	@Inject
	private ViewDetails viewDetails;
	
	private PreauthDTO bean;
	
	private VerticalLayout mainPanel;
	
	
//	private GWizard wizard;
	
	private IWizardPartialComplete wizard;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	@Inject
	private Toolbar toolBar;
	
	private final Logger log = LoggerFactory.getLogger(DownSizePreauthWizardViewImpl.class);

	
	@PostConstruct
	public void initView() {
		addStyleName("view");
		setSizeFull();		
	}
	
	public void initView(PreauthDTO preauthDto, BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> escalateContainer)
	{
		
//		this.wizard=new IWizard();
		this.wizard = new IWizardPartialComplete();
		wizard.getFinishButton().setCaption("Submit");
		this.bean = preauthDto;
		this.bean.getDownSizePreauthDataExtractionDetails().setDownsizeReason(selectValueContainer);
		this.bean.getDownSizePreauthDataExtractionDetails().setEscalateTo(escalateContainer);
		
		
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(), ViewLevels.INTIMATION, false,/*"Downsize Pre-auth"*/ "Revision of Authorization");
		
		mainPanel = new VerticalLayout();
		setHeight("100%");
		RevisedCashlessCarousel preauthIntimation = commonCarouselInstance.get();
//		preauthIntimation.init(bean.getNewIntimationDTO(), bean.getClaimDTO(),"Downsize Pre-auth");
		preauthIntimation.init(bean,/*"Downsize Pre-auth"*/"Revision of Authorization");
		
		DownsizePreauthDataExtractionPage downsizePreauthPageObj=downSizePreauthDataExtractionPage.get();
		downsizePreauthPageObj.init(this.bean,this.wizard);
		this.downsizePreauthPageObj=downsizePreauthPageObj;
		
		DownsizePreauthRequestPdfPage downSizePreauthPdfObj=downSizePreauthPdfPage.get();
		downSizePreauthPdfObj.init(this.bean,this.wizard);
		this.downSizePreauthPdfObj=downSizePreauthPdfObj;
		//this.withdrawPreauthPageObj=withdrawPreauthPageObj;
		
//		WithdrawPreauthPdfPage withdrawPreauthPdfObj=withdrawPreauthPdfInstance.get();
//		withdrawPreauthPdfObj.init(this.bean);
		
		this.claimedAmountDetailsPageObj = this.claimedAmountDetailsPageInstance.get();
		this.claimedAmountDetailsPageObj.init(preauthDto);
//		
		wizard.addStep(claimedAmountDetailsPageObj,"Claimed Amount");
		wizard.addStep(downsizePreauthPageObj,"Revision of Authorization Page"/*"Downsize Preauth Page"*/);
		wizard.addStep(downSizePreauthPdfObj,"Decision Communication");
		
		
		//wizard.addStep(withdrawPreauthPdfObj,"Decision Communication");
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		
		VerticalLayout wizardLayout2 = new VerticalLayout(wizard);
		mainPanel.addComponent(preauthIntimation);
		
		HorizontalLayout commonButtonsLayout = new HorizontalLayout(commonButtonsLayout(),viewDetails);
		commonButtonsLayout.setWidth("100%");
		commonButtonsLayout.setComponentAlignment(viewDetails,Alignment.TOP_RIGHT );
		mainPanel.addComponent(commonButtonsLayout);
		mainPanel.addComponent(wizardLayout2);
		mainPanel.setComponentAlignment(commonButtonsLayout, Alignment.TOP_LEFT);
		//mainPanel.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		
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
		

		TextField icrEarnedPremium = new TextField("ICR(Earned Premium %)");
		icrEarnedPremium.setReadOnly(Boolean.FALSE);
		if(null != bean.getNewIntimationDTO().getIcrEarnedPremium()){
			icrEarnedPremium.setValue(bean.getNewIntimationDTO().getIcrEarnedPremium());
		}
		icrEarnedPremium.setReadOnly(Boolean.TRUE);
		FormLayout icrLayout = new FormLayout(icrEarnedPremium);
		icrLayout.setMargin(false);
		icrLayout.setSpacing(true);
		
		HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(bean);
		
		Button viewLinkedPolicy = viewDetails.getLinkedPolicyDetails(bean);
		HorizontalLayout alignmentHLayout = new HorizontalLayout();
		alignmentHLayout.setSpacing(true);
		
		if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			
			if(bean.getNewIntimationDTO().getPolicy().getGmcPolicyType() != null && 
					((bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT)) || (bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT)))){
						alignmentHLayout = new HorizontalLayout(btnRRC,viewLinkedPolicy,icrLayout);
					} else {
					alignmentHLayout = new HorizontalLayout(btnRRC,icrLayout);
					}
		}
		else
		{
			alignmentHLayout = new HorizontalLayout(btnRRC);
			alignmentHLayout.setSpacing(true);
		}
		
		HorizontalLayout hlaoutforFlag=new HorizontalLayout(alignmentHLayout,hopitalFlag);
		hlaoutforFlag.setSpacing(true);
		return hlaoutforFlag;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		if(claimedAmountDetailsPageObj !=null){
			bean.getRrcDTO().getQuantumReductionDetailsDTO().setPreAuthAmount(claimedAmountDetailsPageObj.getTotalRequestedAmt());
		}
		if(downsizePreauthPageObj !=null){
			bean.getRrcDTO().getQuantumReductionDetailsDTO().setSettlementAmount(downsizePreauthPageObj.getApprovalAmtForRRC());
		}
		fireViewEvent(DownSizePreauthWizardPresenter.VALIDATE_DOWNSIZE_PREAUTH_USER_RRC_REQUEST, bean);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PROCESS_DOWNSIZE_PREAUTH);
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
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void activeStepChanged(WizardStepActivationEvent event) {
		fireViewEvent(DownSizePreauthWizardPresenter.DOWNSIZE_STEP_CHANGE_EVENT, event);

	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {
		// TODO Auto-generated method stub
		
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
	public void wizardCompleted(WizardCompletedEvent event) {
		try
		{
			fireViewEvent(DownSizePreauthWizardPresenter.DOWNSIZE_SUBMIT_EVENT, this.bean);
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
		                	SHAUtils.setClearPreauthDTO(bean);
		                	fireViewEvent(MenuItemBean.DOWNSIZE_PRE_AUTH, null);
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
	public void setDiagnosisSumInsuredValuesFromDB(List<MedicalDecisionTableDTO> medicalDecisionTableList) {
		
		downsizePreauthPageObj.setReferenceData(medicalDecisionTableList);
		
	}

	@Override
	public void setDownsizeAmount(Double amount) {
		
		downsizePreauthPageObj.downSizedAmount(amount);
		
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void buildSuccessLayout(String message) {
		Label label = new Label(message, ContentMode.HTML);
		label.setStyleName("errMessage");
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Downsize Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(label, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");
		
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
				toolBar.countTool();
				SHAUtils.setClearPreauthDTO(bean);
				fireViewEvent(MenuItemBean.DOWNSIZE_PRE_AUTH, null);
				
			}
		});
	}

	@Override
	public void setHospitalizationDetails(
			Map<Integer, Object> hospitalizationDetails) {
		
		this.claimedAmountDetailsPageObj.setHospitalizationDetails(hospitalizationDetails);
	}

	@Override
	public void setBalanceSumInsured(Double balanceSI, List<Double> copayValue) {
		downsizePreauthPageObj.setBalanceSI(balanceSI, copayValue);
		
	}

	@Override
	public void setDiagnosisSumInsuredValuesFromDB(
			Map<String, Object> diagnosisSumInsuredLimit, String diagnosisName) {
		downsizePreauthPageObj.setSumInsuredCaculationsForSublimit(diagnosisSumInsuredLimit, diagnosisName);
		
	}

	@Override
	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> icdCodeContainer) {
		downsizePreauthPageObj.setExclusionDetails(icdCodeContainer);
		
	}

	@Override
	public void showErrorMessage() {
		downsizePreauthPageObj.showErrorMessage();
	}

	@Override
	public void viewClaimAmountDetails() {
		downsizePreauthPageObj.showClaimAmountDetails();
	}

	@Override
	public void viewBalanceSumInsured(String intimationId) {
		
		downsizePreauthPageObj.showBalanceSumInsured(intimationId);
	}
	
	@Override
	public void setRemarks(MasterRemarks remarks) {
		downsizePreauthPageObj.setRemarks(remarks);
		
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
