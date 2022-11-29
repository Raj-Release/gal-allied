package com.shaic.paclaim.rod.createrod.search;

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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.balancesuminsured.view.ViewUnnamedRiskDetailsUI;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.PARevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizard;
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
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class PACreateRODWizardViewImpl extends AbstractMVPView implements PACreateRODWizardView{

	
private static final String DOCUMENT_DETAILS = "Document Details for ROD_PA";
	
	@Inject
	private Instance<PACreateRODDocumentDetailsViewImpl> documentDetailsViewImpl;
	
	@Inject
	private Instance<IWizard> iWizard;
	
	//The second page will be replaced with upload document details screen.
	@Inject
	private Instance<PACreateRODUploadDocumentsView> uploadDocumentsViemImpl;
		
	@Inject
	private Instance<PARevisedCarousel> commonCarouselInstance;
	
	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
	
	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	@Inject
	private Instance<ViewUnnamedRiskDetailsUI> unNamedRiskDetailsInstance;
	
	private ViewUnnamedRiskDetailsUI unNamedRiskDetailsObj;
	
	@Inject
	private ViewDetails viewDetails;
	
	/*@Inject
	private TextBundle tb;*/
	
	
	private VerticalSplitPanel mainPanel;
	
	private IWizardPartialComplete wizard;
	
	private FieldGroup binder;
	
	private ReceiptOfDocumentsDTO bean;
	
	private PACreateRODUploadDocumentsView uploadDocumentView;
	
	private PreauthDTO preauthDTO;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	private RRCDTO rrcDTO;	
	
	@Inject
	private Toolbar toolBar;
//	private String[] arrScreenName ; 
	//private Map<String,String> captionMap ;

	
	
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
		item.addNestedProperty("documentDetails.accidentOrDeath");
		item.addNestedProperty("documentDetails.accidentOrDeathDate");
		item.addNestedProperty("documentDetails.death");
		item.addNestedProperty("documentDetails.permanentPartialDisability");
		item.addNestedProperty("documentDetails.permanentTotalDisability");
		item.addNestedProperty("documentDetails.temporaryTotalDisability");
		item.addNestedProperty("documentDetails.hospitalExpensesCover");
		item.addNestedProperty("documentDetails.documentType");
		item.addNestedProperty("documentDetails.benifitClaimedAmount");

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
		//preauthDTO.setRodHumanTask(rodDTO.getHumanTask());
		
		PACreateRODDocumentDetailsView documentDetailsView = documentDetailsViewImpl.get();
		documentDetailsView.init(this.bean,wizard);
		//wizard.addStep(documentDetailsView,"Document Details");
		wizard.addStep(documentDetailsView,DOCUMENT_DETAILS);
		
		uploadDocumentView = uploadDocumentsViemImpl.get();
		uploadDocumentView.init(this.bean);
		wizard.addStep(uploadDocumentView,"Upload Documents");
		
		//this.wizard.restView(DOCUMENT_DETAILS);

		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		
		viewDetails.initView(bean.getClaimDTO().getNewIntimationDto().getIntimationId(), 0l,ViewLevels.PA_PROCESS,"Receipt of Discharge Documents");
		viewDetails.setAcknowledgementKey(bean.getDocumentDetails().getDocAcknowledgementKey());
		
	
		PARevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//		intimationDetailsCarousel.init(this.bean.getClaimDTO().getNewIntimationDto(), this.bean.getClaimDTO(),  "Receipt of Discharge Documents");
		intimationDetailsCarousel.init(this.bean.getClaimDTO().getNewIntimationDto(), this.bean.getClaimDTO(),  "Receipt of Discharge Documents",rodDTO.getDiagnosis());
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		
		HorizontalLayout wizardLayout1 = new HorizontalLayout(commonButtonsLayout(),viewDetails);
		wizardLayout1.setWidth("100%");
		wizardLayout1.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
		panel1.setHeight("85px");
		//panel1.setHeight("100px");
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
		
		
		
		
		
		
		Button viewUnnamedDetails = new Button("Unnamed Risk Details");
		viewUnnamedDetails.addClickListener(new ClickListener() {
			
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
				unNamedRiskDetailsObj = unNamedRiskDetailsInstance.get();				
				unNamedRiskDetailsObj.init(bean.getClaimDTO().getNewIntimationDto().getKey());
				popup.setContent(unNamedRiskDetailsObj);
				popup.setCaption("Unnamed Risk Details");
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
		
		HorizontalLayout unNamedRiskDetailsLayout = new HorizontalLayout(viewUnnamedDetails);
		//FormLayout viewEarlierRODLayout = new FormLayout(viewEarlierRODDetails);
		
		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		//ComboBox viewDetailsSelect = new ComboBox()
	
		viewDetailsForm.addComponent(viewDetails);
		
		
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
		HorizontalLayout componentsHLayout = new HorizontalLayout(rrcBtnLayout, viewEarlierRODLayout);
		
		if(null != bean.getClaimDTO() && null != bean.getClaimDTO().getNewIntimationDto() && null != bean.getClaimDTO().getNewIntimationDto().getPolicy()&&
				null !=  bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() && null !=  bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() &&
				(ReferenceTable.getGPAProducts().containsKey( bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())) &&
				(SHAConstants.GPA_UN_NAMED_POLICY_TYPE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getGpaPolicyType()))){					
			
			componentsHLayout = new HorizontalLayout(rrcBtnLayout, viewEarlierRODLayout,unNamedRiskDetailsLayout);
		}
		componentsHLayout.setSpacing(true);
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getKey());
		
		TextField cmdClubMembership = new TextField("Club Membership");
		cmdClubMembership.setValue(memberType);
		cmdClubMembership.setReadOnly(true);
		cmdClubMembership.setStyleName("style = background-color: yellow");
		
		FormLayout clubMembershipLayout = new FormLayout(cmdClubMembership);
		clubMembershipLayout.setMargin(false);
		
		if (null != memberType && !memberType.isEmpty()) {
			componentsHLayout.addComponent(clubMembershipLayout);
		}
		
		VerticalLayout vLayout = new VerticalLayout(componentsHLayout,hLayout);
		
		
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(vLayout);
		alignmentHLayout.setWidth("90%");
		alignmentHLayout.setSizeUndefined();
		alignmentHLayout.setSizeFull();
		return alignmentHLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(PACreateRODWizardPresenter.VALIDATE_CREATE_ROD_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PA_CREATE_ROD);
			//rewardRecognitionRequestViewObj.init(bean, popup);
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
		// TODO Auto-generated method stub
		if(this.bean.getScreenName() !=null && this.bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
			fireViewEvent(PACreateRODWizardPresenter.UPDATE_ROD, this.bean);
		} else {
			fireViewEvent(PACreateRODWizardPresenter.SUBMIT_ROD, this.bean);
		}
		
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
		        "No", "Yes", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
                            wizard.releaseHumanTask();
                            if(bean.getScreenName() != null && bean.getScreenName().equalsIgnoreCase(SHAConstants.UPDATE_ROD_DETAILS_SCREEN)){
                            	fireViewEvent(MenuItemBean.UPDATE_ROD_DETAILS, null);
                            } else {
                            	fireViewEvent(MenuItemBean.PA_CREATE_ROD, null);
                            }
		        
		                } else {
		                	//fireViewEvent(MenuPresenter.SHOW_CREATE_ROD_WIZARD, null);
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

	@Override
	public void wizardSave(GWizardEvent event) {
		fireViewEvent(PACreateRODWizardPresenter.SUBMIT_ROD, this.bean);

	}

	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label("<b style = 'color: green;'>ROD Completed successfully !!!</b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		
		Button homeButton = new Button("ROD Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");
		
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
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
		getSession().setAttribute(SHAConstants.TOKEN_ID, null);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				toolBar.countTool();
				fireViewEvent(MenuItemBean.PA_CREATE_ROD, null);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		
		
		
	
		
	}
	
	@Override
	public void buildSuccessLayout(final String docToken) {
		Label successLabel = new Label("<b style = 'color: green;'>ROD Completed successfully !!!</b>", ContentMode.HTML);
		
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
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");
		
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
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
		getSession().setAttribute(SHAConstants.TOKEN_ID, null);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				toolBar.countTool();
				fireViewEvent(MenuItemBean.PA_CREATE_ROD, null);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		docViewButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				fireViewEvent(PACreateRODWizardPresenter.SHOW_PAYMENT_LETTER_POPUP,docToken);
				//fireViewEvent(MenuItemBean.CREATE_ROD, null);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		
		
		
	
		
	}
	
	@Override
	public void buildFailureLayout(String message) {
		//Label successLabel = new Label("<b style = 'color: green;'> Amount Claimed is less than preauth approved amount. Please downsize the preauth amount to proceed further.</b>", ContentMode.HTML);
		Label successLabel = new Label("<b style = 'color: green;'>"+message+"</b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		
		Button homeButton = new Button("ROD Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");
		
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
				wizard.releaseHumanTask();
				dialog.close();
				fireViewEvent(MenuItemBean.PA_CREATE_ROD, null);
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
				fireViewEvent(MenuItemBean.PA_CREATE_ROD, null);
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
				fireViewEvent(MenuItemBean.PA_CREATE_ROD, null);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
	
		
	}
	
	@Override
	public void buildSuccessLayoutForUpdateRod() {
		Label successLabel = new Label("<b style = 'color: green;'>ROD Details Updated Successfully !!!</b>", ContentMode.HTML);
		
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
		dialog.show(getUI().getCurrent(), null, true);
		
		getSession().setAttribute(SHAConstants.TOKEN_ID, null);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				SHAUtils.setClearReimbursementDTO(bean);
				fireViewEvent(MenuItemBean.UPDATE_ROD_DETAILS, null);
				/*documentDetailsView.setClearMapObject();
        		uploadDocumentView.setClearReferenceData();*/
        		VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				
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
