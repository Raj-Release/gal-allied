/**
 * 
 */
package com.shaic.claim.rod.wizard.pages;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
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

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

/**
 * @author ntv.vijayar
 *
 */
public class CreateOnlineRODWizardViewImpl extends AbstractMVPView implements CreateOnlineRODWizardView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7042237764557529471L;

	private static final String DOCUMENT_DETAILS = "Document Details for ROD";
	
	@Inject
	private Instance<CreateRODDocumentDetailsView> documentDetailsViewImpl;
	
	private CreateRODDocumentDetailsView documentDetailsView;
	
	//@Inject
	//private Instance<IWizard> iWizard;
	
	//The second page will be replaced with upload document details screen.
	@Inject
	private Instance<CreateRODUploadDocumentView> uploadDocumentsViemImpl;
		
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
    ////private static Window popup;
	
	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
	
	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	@Inject
	private ViewDetails viewDetails;
	
	/*@Inject
	private TextBundle tb;*/
	
	
	private VerticalSplitPanel mainPanel;
	
	private IWizardPartialComplete wizard;
	
	private FieldGroup binder;
	
	private ReceiptOfDocumentsDTO bean;
	
	private CreateRODUploadDocumentView uploadDocumentView;
	
	private PreauthDTO preauthDTO;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	private RRCDTO rrcDTO;	
	
	@Inject
	private Toolbar toolBar;
//	private String[] arrScreenName ; 
	//private Map<String,String> captionMap ;
	@Inject
	private CreateRODService createRODService;
	
	private void initBinder() {
		
		wizard.getFinishButton().setCaption("Submit");
		this.binder = new FieldGroup();
		BeanItem<ReceiptOfDocumentsDTO> item = new BeanItem<ReceiptOfDocumentsDTO>(bean);
		item.addNestedProperty("documentDetails");
		item.addNestedProperty("documentDetails.documentsReceivedFrom");
		item.addNestedProperty("documentDetails.acknowledgmentContactNumber");
		item.addNestedProperty("documentDetails.documentsReceivedDate");
		item.addNestedProperty("documentDetails.emailId");
		item.addNestedProperty("documentDetails.modeOfReceipt");
		item.addNestedProperty("documentDetails.reconsiderationRequest");
		item.addNestedProperty("documentDetails.hospitalization");
		item.addNestedProperty("documentDetails.preHospitalization");
		item.addNestedProperty("documentDetails.postHospitalization");
		item.addNestedProperty("documentDetails.partialHospitalization");
		item.addNestedProperty("documentDetails.lumpSumAmount");
		item.addNestedProperty("documentDetails.addOnBenefitsHospitalCash");
		item.addNestedProperty("documentDetails.addOnBenefitsPatientCare");
		item.addNestedProperty("documentDetails.hospitalizationClaimedAmount");
		item.addNestedProperty("documentDetails.preHospitalizationClaimedAmount");
		item.addNestedProperty("documentDetails.postHospitalizationClaimedAmount");
		item.addNestedProperty("documentDetails.HospitalCashClaimedAmnt");

		//item.addNestedProperty("uploadDocumentsDTO.fileUpload");
		item.addNestedProperty("uploadDocumentsDTO.fileType");
		item.addNestedProperty("uploadDocumentsDTO.billNo");
		item.addNestedProperty("uploadDocumentsDTO.billDate");
		item.addNestedProperty("uploadDocumentsDTO.noOfItems");
		item.addNestedProperty("uploadDocumentsDTO.billValue");
		
		item.addNestedProperty("uploadedDocumentsDTO.fileType");
		item.addNestedProperty("uploadedDocumentsDTO.fileName");
		item.addNestedProperty("uploadedDocumentsDTO.rodNo");
		item.addNestedProperty("uploadedDocumentsDTO.billNo");
		item.addNestedProperty("uploadedDocumentsDTO.billDate");
		item.addNestedProperty("uploadedDocumentsDTO.noOfItems");
		item.addNestedProperty("uploadedDocumentsDTO.billValue");
		
		
		this.binder.setItemDataSource(item);	
	}
	
	@PostConstruct
	public void initView() {
		/*arrScreenName = new String[] {
					"documentDetails","acknowledgementReceipt"};*/
		mainPanel = new VerticalSplitPanel();
		//captionMap = new HashMap<String, String>();
		
		addStyleName("view");
		setSizeFull();			
	}
	
	public void initView(ReceiptOfDocumentsDTO rodDTO)
	{
		this.bean = rodDTO;
		//this.wizard = new IWizard();
		//this.wizard = iWizard.get();
		this.wizard = new IWizardPartialComplete();
		
		this.rrcDTO = rodDTO.getRrcDTO();
		initBinder();
		
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(this.rrcDTO);
		bean.getPreauthDTO().setStrUserName(rrcDTO.getStrUserName());
		//preauthDTO.setRodHumanTask(rodDTO.getHumanTask());
		
		documentDetailsView = documentDetailsViewImpl.get();
		documentDetailsView.init(this.bean,wizard,SHAConstants.CREATE_ONLINE_ROD);
		//wizard.addStep(documentDetailsView,"Document Details");
		wizard.addStep(documentDetailsView,DOCUMENT_DETAILS);
		
		uploadDocumentView = uploadDocumentsViemImpl.get();
		uploadDocumentView.init(this.bean);
		wizard.addStep(uploadDocumentView,"Upload Documents");
		
		//this.wizard.restView(DOCUMENT_DETAILS);

		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		
		viewDetails.initView(bean.getClaimDTO().getNewIntimationDto().getIntimationId(), 0l,ViewLevels.ACK_ROD_PROCESS,"Receipt of Discharge Documents");
		viewDetails.setAcknowledgementKey(bean.getDocumentDetails().getDocAcknowledgementKey());
		
	
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//		intimationDetailsCarousel.init(this.bean.getClaimDTO().getNewIntimationDto(), this.bean.getClaimDTO(),  "Receipt of Discharge Documents");
		intimationDetailsCarousel.init(this.bean.getClaimDTO().getNewIntimationDto(), this.bean.getClaimDTO(),  "Receipt of Discharge Documents",rodDTO.getDiagnosis());
		intimationDetailsCarousel.setEnableOrDisableFields();
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		
		HorizontalLayout hTempLayout = new HorizontalLayout(commonButtonsLayout(),viewDetails);
		hTempLayout.setWidth("100%");
		hTempLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		
		crmFlaggedComponents.init(bean.getPreauthDTO().getCrcFlaggedReason(),bean.getPreauthDTO().getCrcFlaggedRemark());
		crmFlaggedComponents.setWidth("100%");
		
		FormLayout crmLayout = new FormLayout(crmFlaggedComponents);
		crmLayout.setMargin(false);
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getNewIntimationDTO().getPolicy().getKey());
		
		TextField cmdClubMembership = new TextField("Club Membership");
		cmdClubMembership.setValue(memberType);
		cmdClubMembership.setReadOnly(true);
		cmdClubMembership.setStyleName("style = background-color: yellow");
		
		FormLayout clubMemberLayout = new FormLayout(cmdClubMembership);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(crmLayout);
		horizontalLayout.setSpacing(true);
		
		if (null != memberType && !memberType.isEmpty()) {
			horizontalLayout.addComponent(clubMemberLayout);
		}
		
		VerticalLayout wizardLayout1 = new VerticalLayout(hTempLayout,horizontalLayout);
		wizardLayout1.setSpacing(true);
		
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
		//panel1.setHeight("85px");
//		panel1.setHeight("95px");
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		wizardLayout2.setSpacing(true);
		mainPanel.setSecondComponent(wizardLayout2);
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(26, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);
		
			
	}
	
	/*protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
        for (final String propertyId : arrScreenName) {
            final String header = tb.getText(textBundlePrefixString() + propertyId.toLowerCase());//String.valueOf(propertyId).toLowerCase());
            captionMap.put(propertyId, header);
        }
    }
	

	private String textBundlePrefixString()
	{
		return "acknowledge-document-received-";
	}*/
	
	private HorizontalLayout commonButtonsLayout()
	{
		
//		TextField txtClaimCount = new TextField("Claim Count");
//		txtClaimCount.setValue(this.bean.getClaimCount().toString());
//		txtClaimCount.setReadOnly(true);
//		TextField dummyField = new TextField();
//		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		FormLayout firstForm = new FormLayout(txtClaimCount,dummyField);
//		dummyField.setReadOnly(true);
////		firstForm.setWidth(txtClaimCount.getWidth(), txtClaimCount.getWidthUnits());
//		Panel claimCount = new Panel(firstForm);
//		txtClaimCount.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		firstForm.setHeight("50px");
//		firstForm.setComponentAlignment(txtClaimCount, Alignment.TOP_LEFT);
////		txtClaimCount.addStyleName("fail");
////		claimCount.setWidth(txtClaimCount.getWidth(),txtClaimCount.getWidthUnits());
////		
//		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
//			claimCount.addStyleName("girdBorder1");
//		}else if(this.bean.getClaimCount() >2){
//			claimCount.addStyleName("girdBorder2");
//		}
		
		TextField acknowledgementNumber = new TextField("Acknowledgement Number");
		acknowledgementNumber.setValue(String.valueOf(this.bean.getAcknowledgementNumber()));
		//Vaadin8-setImmediate() acknowledgementNumber.setImmediate(true);
		acknowledgementNumber.setWidth("250px");
		acknowledgementNumber.setHeight("20px");
		acknowledgementNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		acknowledgementNumber.setReadOnly(true);
		acknowledgementNumber.setEnabled(false);
		acknowledgementNumber.setNullRepresentation("");
		FormLayout hLayout = new FormLayout (acknowledgementNumber);
		hLayout.setComponentAlignment(acknowledgementNumber, Alignment.MIDDLE_LEFT);
		
		Button viewEarlierRODDetails = new Button("View Earlier ROD Details");
		viewEarlierRODDetails.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
				earlierRodDetailsViewObj = earlierRodDetailsViemImplInstance.get();
				ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
				documentDetails.setClaimDto(bean.getClaimDTO());
				earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),null);
				popup.setContent(earlierRodDetailsViewObj);
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
		});
		
		HorizontalLayout viewEarlierRODLayout = new HorizontalLayout(viewEarlierRODDetails);
		//FormLayout viewEarlierRODLayout = new FormLayout(viewEarlierRODDetails);
		/*
		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		//ComboBox viewDetailsSelect = new ComboBox()
	
		viewDetailsForm.addComponent(viewDetails);*/
		
		
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		

		
		
		HorizontalLayout rrcBtnLayout = new HorizontalLayout(btnRRC);
		Label dummy = new Label();
		dummy.setWidth("160px");
		
		TextField icrEarnedPremium = new TextField("ICR(Earned Premium %)");
		if(null != bean.getNewIntimationDTO().getIcrEarnedPremium()){
			icrEarnedPremium.setValue(bean.getNewIntimationDTO().getIcrEarnedPremium());
		}
		icrEarnedPremium.setEnabled(Boolean.FALSE);
		FormLayout icrLayout = new FormLayout(icrEarnedPremium);
		icrLayout.setMargin(false);
		
		Button viewLinkedPolicy = viewDetails.getLinkedPolicyDetails(bean.getPreauthDTO());
		
		HorizontalLayout componentsHLayout = new HorizontalLayout();
		
		/*if(ReferenceTable.GMC_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				componentsHLayout = new HorizontalLayout(rrcBtnLayout, viewEarlierRODLayout,icrLayout);
			}
			else
			{
				componentsHLayout = new HorizontalLayout(rrcBtnLayout, viewEarlierRODLayout);
			}*/
		
		HorizontalLayout formLayout = SHAUtils.newImageCRM(bean.getPreauthDTO());
		HorizontalLayout crmLayout = new HorizontalLayout(formLayout);
		crmLayout.setSpacing(true);
		crmLayout.setWidth("100%");
		HorizontalLayout icrAgentBranch = SHAUtils.icrAgentBranch(bean.getPreauthDTO());
		HorizontalLayout buyBackPedHLayout = new HorizontalLayout();
		if(bean.getNewIntimationDTO() !=null && bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_78)){
			buyBackPedHLayout = SHAUtils.buyBackPed(bean.getPreauthDTO());
			icrAgentBranch.addComponent(buyBackPedHLayout);
		}
		if(bean.getNewIntimationDTO() !=null && bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_88)){
			buyBackPedHLayout = SHAUtils.buyBackPed(bean.getPreauthDTO());
			icrAgentBranch.addComponent(buyBackPedHLayout);
		}
		VerticalLayout icrAGBR = new VerticalLayout(crmLayout,icrAgentBranch);
		icrAGBR.setHeight("225%");
		
		if(bean.getPreauthDTO().getPolicyDto().getGmcPolicyType() != null &&
				(bean.getPreauthDTO().getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
						bean.getPreauthDTO().getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))) {
			componentsHLayout = new HorizontalLayout(icrAGBR,rrcBtnLayout, viewEarlierRODLayout,viewLinkedPolicy);
		} else {
			componentsHLayout = new HorizontalLayout(icrAGBR,rrcBtnLayout, viewEarlierRODLayout);
		}
		componentsHLayout.setSpacing(true);
		
		VerticalLayout vLayout = new VerticalLayout();
		
		vLayout = new VerticalLayout(componentsHLayout/*,hLayout*/);
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(vLayout);
		alignmentHLayout.setWidth("90%");
		alignmentHLayout.setSizeUndefined();
		alignmentHLayout.setSizeFull();
		return alignmentHLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		
		fireViewEvent(CreateOnlineRODWizardPresenter.VALIDATE_CREATE_ONLINE_ROD_USER_RRC_REQUEST,bean.getPreauthDTO());//, secondaryParameters);
	}

	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {
		
			if (!isValid) {
				/*Label label = new Label("Same user cannot raise request more than once from same stage", ContentMode.HTML);
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
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox("Same user cannot raise request more than once from same stage", buttonsNamewithType);
				
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.CREATE_ROD);
			//rewardRecognitionRequestViewObj.init(bean, popup);
			rewardRecognitionRequestViewObj.init(bean.getPreauthDTO(), popup);
			
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
		
		if(null != uploadDocumentView)
		{
			uploadDocumentView.resetPage();
		}
		
		/*if(null != this.wizard && !this.wizard.getSteps().isEmpty())
		{
			this.wizard.clearWizardMap(DOCUMENT_DETAILS);
			this.wizard.clearWizardMap("Upload Documents");
		//	this.wizard.clearWizardMap(DOCUMENT_DETAILS);
			this.wizard.clearCurrentStep();
		}*/
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
		Boolean isHospitalizationAvailable = createRODService.getHospitalizationRODAvailableOrNot(bean.getClaimDTO().getKey());
		if(isHospitalizationAvailable && bean.getClaimDTO() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
			ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Hospitalisation ROD is not available. Do you want to proceed further?",
			        "No", "Yes", new ConfirmDialog.Listener() {

				public void onClose(ConfirmDialog dialog) {
					if (!dialog.isConfirmed()) {
                    	// TODO Auto-generated method stub
            	        Double totalClaimedAmt = getTotalClaimedAmt(bean);
            			if(bean.getScreenName() !=null && bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
            				fireViewEvent(CreateOnlineRODWizardPresenter.UPDATE_ONLINE_ROD, bean);
            			} else {
            				Boolean alertMessageForProvisionValidation = SHAUtils.alertMessageForProvisionValidation(ReferenceTable.CREATE_ROD_STAGE_KEY, ReferenceTable.CREATE_ROD_STATUS_KEY, totalClaimedAmt != null ? totalClaimedAmt.longValue():0l, 
            						bean.getClaimDTO().getNewIntimationDto().getPolicy().getKey(), bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey(), 0l, "R",getUI());
            				if(!alertMessageForProvisionValidation){
            					/*if(isAlertMessageNeededForNEFTDetails()){
            						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
            						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
            						GalaxyAlertBox.createErrorBox("Select mode of payment as Bank Transfer only", buttonsNamewithType);

            					}else{*/
            					
            						List<UploadDocumentDTO> uploadDocsList = bean.getUploadDocsList();

            						 if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
            							 for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
            								 if (null != uploadDocumentDTO.getFileType()
            										 && null != uploadDocumentDTO.getFileType().getValue()
            										 && uploadDocumentDTO.getFileType().getValue()
            										 .contains(SHAConstants.NEFT_DETAILS)) {
            									 bean.setIsNEFTDetailsAvailable(false);
            									 bean.setIsNEFTDetailsAvailableinDMS(true);
            									 break;

            								 }
            							 }
            						 }
            						 
            						 
									if(bean.getIsNEFTDetailsAvailable()){
										MessageBox neftLetter = MessageBox
												.createInfo()
												.withCaptionCust("Information")
												.withMessage("NEFT Query letter will be sent to proposer/insured to submit NEFT Details for payment processing")
												.withOkButton(ButtonOption.caption("OK"))
												.open();
										Button okBtn =neftLetter.getButton(ButtonType.OK);
										okBtn.addClickListener(new Button.ClickListener() {
											@Override
											public void buttonClick(ClickEvent event) {
												wizard.getFinishButton().setEnabled(true);
												fireViewEvent(CreateOnlineRODWizardPresenter.SUBMIT_ONLINE_ROD, bean);
											}
										});	

									}
									//fireViewEvent(CreateOnlineRODWizardPresenter.SUBMIT_ROD, bean);
									else{

            						fireViewEvent(CreateOnlineRODWizardPresenter.SUBMIT_ONLINE_ROD, bean);
            					}
            				//}
            				
            			}
            			}
                        } else {
                        	wizard.getFinishButton().setCaption("Submit");
                        	wizard.getFinishButton().setEnabled(true);
//                        	fireViewEvent(CreateOnlineRODWizardPresenter.SUBMIT_ROD, bean);
                        }
					}
			        });
			dialog.setClosable(false);
			dialog.setStyleName(Reindeer.WINDOW_BLACK);
		}
		else{
			// TODO Auto-generated method stub
	        Double totalClaimedAmt = getTotalClaimedAmt(bean);
			if(this.bean.getScreenName() !=null && this.bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
				fireViewEvent(CreateOnlineRODWizardPresenter.UPDATE_ONLINE_ROD, this.bean);
			} else {
				Boolean alertMessageForProvisionValidation = SHAUtils.alertMessageForProvisionValidation(ReferenceTable.CREATE_ROD_STAGE_KEY, ReferenceTable.CREATE_ROD_STATUS_KEY, totalClaimedAmt != null ? totalClaimedAmt.longValue():0l, 
						bean.getClaimDTO().getNewIntimationDto().getPolicy().getKey(), bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey(), 0l, "R",getUI());
				if(!alertMessageForProvisionValidation){
					/*if(isAlertMessageNeededForNEFTDetails()){
						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
						GalaxyAlertBox.createErrorBox("Select mode of payment as Bank Transfer only", buttonsNamewithType);

					
					}else{*/
						
						List<UploadDocumentDTO> uploadDocsList = bean.getUploadDocsList();
						 bean.setIsNEFTDetailsAvailableinDMS(false);
						 if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
							 for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
								 if (null != uploadDocumentDTO.getFileType()
										 && null != uploadDocumentDTO.getFileType().getValue()
										 && uploadDocumentDTO.getFileType().getValue()
										 .contains(SHAConstants.NEFT_DETAILS)) {
									 bean.setIsNEFTDetailsAvailable(false);
									 bean.setIsNEFTDetailsAvailableinDMS(true);
									 break;

								 }
							 }
						 }
						 
//						 if(bean.getDocumentDetails().getPaymentModeFlag().equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)){
//    						 NEFTQueryDetails neftQueryDetails = createRODService.getNEFTDetailsByReimbursementKey(bean.getDocumentDetails().getRodKey());
//    						 Reimbursement reimbursement = createRODService.getReimbursementByKey(bean.getDocumentDetails().getRodKey());
//    							createRODService.saveNEFTDetailsForCheque(bean,reimbursement);
//
//    					}


						if(bean.getIsNEFTDetailsAvailable()){
							MessageBox neftLetter = MessageBox
									.createInfo()
									.withCaptionCust("Information")
									.withMessage("NEFT Query letter will be sent to proposer/insured to submit NEFT Details for payment processing")
									.withOkButton(ButtonOption.caption("OK"))
									.open();
							Button okBtn =neftLetter.getButton(ButtonType.OK);
							okBtn.addClickListener(new Button.ClickListener() {
								@Override
								public void buttonClick(ClickEvent event) {
									wizard.getFinishButton().setEnabled(true);
									fireViewEvent(CreateOnlineRODWizardPresenter.SUBMIT_ONLINE_ROD, bean);
								}
							});	

						}
						//fireViewEvent(CreateOnlineRODWizardPresenter.SUBMIT_ROD, bean);
					else{

						fireViewEvent(CreateOnlineRODWizardPresenter.SUBMIT_ONLINE_ROD, bean);
					}
				//}
					
				}
				
				
			}
		}
		
		
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		/*ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
		        "No", "Yes", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
                            releaseHumanTask();
                            SHAUtils.setClearReimbursementDTO(bean);
                            if(bean.getScreenName() != null && bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
                            	fireViewEvent(MenuItemBean.UPDATE_ROD_DETAILS, null);
                            } else {
                            	fireViewEvent(MenuItemBean.CREATE_ONLINE_ROD, null);
                            }
		            		documentDetailsView.setClearMapObject();
		            		uploadDocumentView.setClearReferenceData();
		            		VaadinRequest currentRequest = VaadinService.getCurrentRequest();
		    				SHAUtils.clearSessionObject(currentRequest);
		        
		                } else {
		                	//fireViewEvent(MenuPresenter.SHOW_CREATE_ROD_WIZARD, null);
		                }
		            }
		        });
		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createConfirmationbox("Are you sure you want to cancel ?", buttonsNamewithType);
		Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
				.toString());
		yesButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
                releaseHumanTask();
                SHAUtils.setClearReimbursementDTO(bean);
                if(bean.getScreenName() != null && bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
                	fireViewEvent(MenuItemBean.UPDATE_ROD_DETAILS, null);
                } else {
                	fireViewEvent(MenuItemBean.CREATE_ONLINE_ROD, null);
                }
        		documentDetailsView.setClearMapObject();
        		uploadDocumentView.setClearReferenceData();
        		VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
    
            }
			});
		noButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
			}
			});
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardSave(GWizardEvent event) {
		fireViewEvent(CreateOnlineRODWizardPresenter.SUBMIT_ONLINE_ROD, this.bean);

	}

	@Override
	public void buildSuccessLayout() {
		/*Label successLabel = new Label("<b style = 'color: green;'>ROD Completed successfully !!!</b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		
		Button homeButton = new Button("ROD Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		getSession().setAttribute(SHAConstants.TOKEN_ID, null);
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "ROD Home");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("ROD Completed successfully !!!</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				toolBar.countTool();
				SHAUtils.setClearReimbursementDTO(bean);
				fireViewEvent(MenuItemBean.CREATE_ONLINE_ROD, null);
				documentDetailsView.setClearMapObject();
        		uploadDocumentView.setClearReferenceData();
        		VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		
		
		
	
		
	}
	
	@Override
	public void buildSuccessLayout(final String docToken) {
		/*Label successLabel = new Label("<b style = 'color: green;'>ROD Completed successfully !!!</b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		
		Button homeButton = new Button("ROD Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		Button docViewButton = new Button("View Payment Letter");
		docViewButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		//FormLayout fLayout = new FormLayout(homeButton, docViewButton);
		HorizontalLayout btnLayout = new HorizontalLayout(homeButton , docViewButton );
		
		VerticalLayout layout = new VerticalLayout(successLabel, btnLayout);
		//layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");
		
		
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		getSession().setAttribute(SHAConstants.TOKEN_ID, null);
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "ROD Home");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "View Payment Letter");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("ROD Completed successfully !!!</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		Button docViewButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				toolBar.countTool();

				fireViewEvent(MenuItemBean.CREATE_ONLINE_ROD, null);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		docViewButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				fireViewEvent(CreateOnlineRODWizardPresenter.SHOW_ONLINE_PAYMENT_LETTER_POPUP,docToken);
				//fireViewEvent(MenuItemBean.CREATE_ONLINE_ROD, null);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		
		
		
	
		
	}
	
	@Override
	public void buildFailureLayout(String message) {
		//Label successLabel = new Label("<b style = 'color: green;'> Amount Claimed is less than preauth approved amount. Please downsize the preauth amount to proceed further.</b>", ContentMode.HTML);
		/*Label successLabel = new Label("<b style = 'color: green;'>"+message+"</b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		
		Button homeButton = new Button("ROD Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");*/
		/*HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");*/
		
		/*Button homeButton = new Button("ROD Home");
		//homeButton.set
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);*/
		
		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "ROD Home");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(message+"</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				wizard.releaseHumanTask();
				//dialog.close();
				fireViewEvent(MenuItemBean.CREATE_ONLINE_ROD, null);
				documentDetailsView.setClearMapObject();
        		uploadDocumentView.setClearReferenceData();
        		VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
	}

	@Override
	public void showDocViewPopup(Panel e) {
		Button closeButton = new Button("Close");
		closeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout docViewLayout = new VerticalLayout();
		e.setHeight("900px");
		docViewLayout.addComponent(e);
		docViewLayout.addComponent(closeButton);
		docViewLayout.setComponentAlignment(closeButton,Alignment.BOTTOM_CENTER);
		
		// TODO Auto-generated method stub
		final Window popup = new com.vaadin.ui.Window();
		
		
		
		/*viewClaimWiseRRCHistoryPage.init(bean,popup);
		viewClaimWiseRRCHistoryPage.initPresenter(SHAConstants.VIEW_RRC_REQUEST);
		viewClaimWiseRRCHistoryPage.getContent();*/
		
		popup.setCaption("View Uploaded File");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(docViewLayout);
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
				System.out.println("Close listener called");
				fireViewEvent(MenuItemBean.CREATE_ONLINE_ROD, null);
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		
		closeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				wizard.releaseHumanTask();
				//dialog.close();
				popup.close();
				fireViewEvent(MenuItemBean.CREATE_ONLINE_ROD, null);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
	
		
	}

	@Override
	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {

		documentDetailsView.setCoverList(coverContainer);

	}

	@Override
	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {

		documentDetailsView.setSubCoverList(subCoverContainer);

	}
	 private void releaseHumanTask(){
			
			Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
	     	/*String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
	 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);*/
	 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

	 		if(existingTaskNumber != null){
	 			//BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
	 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
	 		}
	 		
	 		if(wrkFlowKey != null){
	 			DBCalculationService dbService = new DBCalculationService();
	 			dbService.callUnlockProcedure(wrkFlowKey);
	 			getSession().setAttribute(SHAConstants.WK_KEY, null);
	 		}
		}

	@Override
	public void validateClaimedAmount(final TextField txtField) {

		
		/*ConfirmDialog dialog = ConfirmDialog
				.show(getUI(),
						"Confirmation",
						SHAConstants.ACK_CLAIMED_AMOUNT_ALERT,
						"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									
									dialog.close();
									
								} else {
									// User did not confirm
									txtHospitalizationClaimedAmt.setValue(null);
									txtPreHospitalizationClaimedAmt.setValue(null);
									txtPostHospitalizationClaimedAmt.setValue(null);
									txtOtherBenefitsClaimedAmnt.setValue(null);
									documentDetailsView.resetClaimedAmntValue(txtField);
									documentDetailsView.setOptDocVerifiedValue();
									dialog.close();
									dialog.setClosable(false);
									//dialog.setStyleName(Reindeer.WINDOW_BLACK);

								}
							}
						});
			dialog.setClosable(false);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createConfirmationbox(SHAConstants.ACK_CLAIMED_AMOUNT_ALERT, buttonsNamewithType);
			Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
					.toString());
			Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
					.toString());
			yesButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//optDocumentVerified.setValue(null);
				}
				});
			noButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {

					// User did not confirm
					/*txtHospitalizationClaimedAmt.setValue(null);
					txtPreHospitalizationClaimedAmt.setValue(null);
					txtPostHospitalizationClaimedAmt.setValue(null);
					txtOtherBenefitsClaimedAmnt.setValue(null);*/
					documentDetailsView.resetClaimedAmntValue(txtField);
					documentDetailsView.setOptDocVerifiedValue();
					//dialog.setStyleName(Reindeer.WINDOW_BLACK);

				
				}
				});
	
	}
	
	@Override
	public void buildSuccessLayoutForUpdateRod() {
		/*Label successLabel = new Label("<b style = 'color: green;'>ROD Details Updated Successfully !!!</b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		
		Button homeButton = new Button("Update ROD Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		getSession().setAttribute(SHAConstants.TOKEN_ID, null);
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Update ROD Home");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("ROD Details Updated Successfully !!!</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				SHAUtils.setClearReimbursementDTO(bean);
				fireViewEvent(MenuItemBean.UPDATE_ROD_DETAILS, null);
				documentDetailsView.setClearMapObject();
        		uploadDocumentView.setClearReferenceData();
        		VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
	
	}
	
	public Double getTotalClaimedAmt(ReceiptOfDocumentsDTO rodDTO){
		Double totalClaimedAmt = 0d;
		if ((null != rodDTO.getDocumentDetails().getHospitalizationClaimedAmount()) && !("").equals(rodDTO.getDocumentDetails().getHospitalizationClaimedAmount())
				&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails().getHospitalizationClaimedAmount())))
		{
		totalClaimedAmt += (Double
				.parseDouble(rodDTO.getDocumentDetails()
						.getHospitalizationClaimedAmount()));
		
	
		}
		if ((null != rodDTO.getDocumentDetails()
				.getPreHospitalizationClaimedAmount())
				&& !("").equals(rodDTO.getDocumentDetails()
						.getPreHospitalizationClaimedAmount())
				&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
						.getPreHospitalizationClaimedAmount())))
		{
				totalClaimedAmt += (Double.parseDouble(rodDTO.getDocumentDetails().getPreHospitalizationClaimedAmount()));
			
		}
		if ((null != rodDTO.getDocumentDetails()
				.getPostHospitalizationClaimedAmount())
				&& !("").equals(rodDTO.getDocumentDetails()
						.getPostHospitalizationClaimedAmount())
				&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
						.getPostHospitalizationClaimedAmount())))
		{
			
			totalClaimedAmt += (Double.parseDouble(rodDTO.getDocumentDetails().getPostHospitalizationClaimedAmount()));
		}
		
		if ((null != rodDTO.getDocumentDetails()
				.getOtherBenefitclaimedAmount())
				&& !("").equals(rodDTO.getDocumentDetails()
						.getOtherBenefitclaimedAmount())
				&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
						.getOtherBenefitclaimedAmount())))
		{
			
			totalClaimedAmt += (Double.parseDouble(rodDTO.getDocumentDetails().getOtherBenefitclaimedAmount()));
			}
		
		if ((null != rodDTO.getDocumentDetails()
				.getHospitalCashClaimedAmnt())
				&& !("").equals(rodDTO.getDocumentDetails()
						.getHospitalCashClaimedAmnt())
				&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
						.getHospitalCashClaimedAmnt())))
		{
			
			totalClaimedAmt += (Double.parseDouble(rodDTO.getDocumentDetails().getHospitalCashClaimedAmnt()));
			}
		
		return totalClaimedAmt;
	}
	
	public void WizardForHospitalizationAvailableOrNot(WizardCancelledEvent event) {
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
		        "No", "Yes", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
                            releaseHumanTask();
                            SHAUtils.setClearReimbursementDTO(bean);
                            if(bean.getScreenName() != null && bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
                            	fireViewEvent(MenuItemBean.UPDATE_ROD_DETAILS, null);
                            } else {
                            	fireViewEvent(MenuItemBean.CREATE_ONLINE_ROD, null);
                            }
		            		documentDetailsView.setClearMapObject();
		            		uploadDocumentView.setClearReferenceData();
		            		VaadinRequest currentRequest = VaadinService.getCurrentRequest();
		    				SHAUtils.clearSessionObject(currentRequest);
		        
		                } else {
		                	//fireViewEvent(MenuPresenter.SHOW_CREATE_ROD_WIZARD, null);
		                }
		            }
		        });
		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
	}
	
	@Override
	public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
		 rewardRecognitionRequestViewObj.setsubCategoryValues(selectValueContainer, subCategory, value);
	 }
	 
	 @Override
	 public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
		 rewardRecognitionRequestViewObj.setsourceValues(selectValueContainer, source, value);
	 }
	 
	 public Boolean isAlertMessageNeededForNEFTDetails(){
		 List<UploadDocumentDTO> uploadDocsList = bean.getUploadDocsList();

		 if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
			 for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
				 if (null != uploadDocumentDTO.getFileType()
						 && null != uploadDocumentDTO.getFileType().getValue()
						 && uploadDocumentDTO.getFileType().getValue()
						 .contains(SHAConstants.NEFT_DETAILS)) {
					 bean.setIsNEFTDetailsAvailableinDMS(true);

				 }
			 }
		 }
		 
		 if((bean.getDocumentDetails().getDocumentsReceivedFrom() != null
					&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getDocumentDetails().getDocumentsReceivedFrom().getId()))
					&& (bean.getDocumentDetails().getPatientStatus() != null && bean.getDocumentDetails().getPatientStatus().getId() != null
							&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getDocumentDetails().getPatientStatus().getId()) 
									|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getDocumentDetails().getPatientStatus().getId()))
						&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
									&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey()))) {
			return false; 
		 }
		 
		 if(bean.getDocumentDetails().getPaymentModeFlag().equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD) && bean.getIsNEFTDetailsAvailableinDMS() && !bean.getIsNEFTDetailsAvailable()){
			 /*	HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox("Select mode of payment as Bank Transfer only", buttonsNamewithType);*/
			 return true;

		 }
		 else{

			 return false;
		 }
	 }
	 
}

