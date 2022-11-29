/**
 * 
 */
package com.shaic.claim.rod.wizard.pages;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.bpc.ViewBusinessProfileChart;
import com.shaic.claim.preauth.view.ViewPreviousClaimsTable;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.BasePolicyPreviousClaimWindowUI;
import com.shaic.claim.registration.ViewBasePolicyClaimsWindowOpen;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.event.ShortcutListener;
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
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.vijayar
 *
 */
public class BillEntryWizardViewImpl extends AbstractMVPView implements
BillEntryWizardView {
	
	private static final String DOCUMENT_DETAILS = "Document Details For Bill Entry";
	
	@Inject
	private Instance<BillEntryDocumentDetailsView> documentDetailsViewImpl;
	
	private BillEntryDocumentDetailsView documentDetailsView;
	
	//@Inject
	//private Instance<IWizard> iWizard;
	
	//The second page will be replaced with upload document details screen.
	@Inject
	private Instance<BillEntryUploadDocumentsView> uploadDocumentsViemImpl;
	
	@Inject
	private Instance<BillEntryDetailsView> billEntryViewImpl;
	
	private BillEntryDetailsView billEntryViewImplObj;
	
    ////private static Window popup;
	
	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
	
	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
	@Inject
	private ViewDetails viewDetails;
	
	/*@Inject
	private TextBundle tb;*/
	
	
	private VerticalSplitPanel mainPanel;
	
	//private GWizard wizard;
	private IWizardPartialComplete wizard;
	
	private FieldGroup binder;
	
	private ReceiptOfDocumentsDTO bean;
//	private String[] arrScreenName ; 
	//private Map<String,String> captionMap ;

	private BillEntryUploadDocumentsView uploadDocumentView;
	
	private PreauthDTO preauthDTO;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	private RRCDTO rrcDTO;	
	
	@Inject
	private Toolbar toolBar;
	
	private Button viewLinkPolicyDtls;
	
	private NativeSelect viewBasePolicyDetailsSelect;
	
	private Button btnGo;
	
	private EnhancedBrowserWindowOpener sopener;
	
	private static final String VIEW_BASE_POLICY_CLAIMS = "Policy Claims Details";

	@Inject
	private ViewPreviousClaimsTable preauthPreviousClaimsTable;
	@Inject
	private Instance<ViewBasePolicyClaimsWindowOpen> ViewBasePolicyClaimsWindowOpen;
	
	@EJB
	private PolicyService policyService;

	private Button btnBusinessProfileChart;
	
	@Inject		
	private ViewBusinessProfileChart viewBusinessProfileChart;	
	
	
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

	//	item.addNestedProperty("uploadDocumentsDTO.fileUpload");
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
		preauthDTO.setRrcDTO(rrcDTO);
		//preauthDTO.setRodHumanTask(rodDTO.getHumanTask());
		

		documentDetailsView = documentDetailsViewImpl.get();
		documentDetailsView.init(this.bean,wizard);
		//wizard.addStep(documentDetailsView,"Document Details");
		wizard.addStep(documentDetailsView,DOCUMENT_DETAILS);
		
		
		//BillEntryUploadDocumentsView uploadDocumentView = uploadDocumentsViemImpl.get();
		uploadDocumentView = uploadDocumentsViemImpl.get();
		uploadDocumentView.init(this.bean);
		wizard.addStep(uploadDocumentView,"Upload Documents For Bill Entry");
		
		billEntryViewImplObj = billEntryViewImpl.get();
		billEntryViewImplObj.init(bean);
		wizard.addStep(billEntryViewImplObj,"Bill Entry");
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		
	
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//		intimationDetailsCarousel.init(this.bean.getClaimDTO().getNewIntimationDto(), this.bean.getClaimDTO(),  "Enter Bill Details (Bill Entry)");
		intimationDetailsCarousel.init(this.bean.getClaimDTO().getNewIntimationDto(), this.bean.getClaimDTO(),  "Enter Bill Details (Bill Entry)",rodDTO.getDiagnosis());
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		viewDetails.initView(bean.getClaimDTO().getNewIntimationDto().getIntimationId(),bean.getDocumentDetails().getRodKey(),ViewLevels.PREAUTH_MEDICAL,"Enter Bill Details (Bill Entry)");
		HorizontalLayout hLayout = new HorizontalLayout(commonButtonsLayout(),viewDetails);
		hLayout.setWidth("100%");
		hLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		
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
		
		VerticalLayout wizardLayout1 = new VerticalLayout(hLayout,horizontalLayout);
		
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
//		panel1.setHeight("50px");
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
	
//	private HorizontalLayout commonButtonsLayout()
//	{
//		
//		/*TextField acknowledgementNumber = new TextField("Acknowledgement Number");
//		acknowledgementNumber.setValue(String.valueOf(this.bean.getAcknowledgementNumber()));
//		//Vaadin8-setImmediate() acknowledgementNumber.setImmediate(true);
//		acknowledgementNumber.setWidth("250px");
//		acknowledgementNumber.setHeight("20px");
//		acknowledgementNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		acknowledgementNumber.setReadOnly(true);
//		acknowledgementNumber.setEnabled(false);
//		acknowledgementNumber.setNullRepresentation("");
//		FormLayout hLayout = new FormLayout (acknowledgementNumber);
//		hLayout.setComponentAlignment(acknowledgementNumber, Alignment.MIDDLE_LEFT);*/
//		
//		Button viewEarlierRODDetails = new Button("View Earlier ROD Details");
//		viewEarlierRODDetails.addClickListener(new ClickListener() {
//			
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
////					viewDetails.getTranslationMiscRequest(bean.getNewIntimationDTO().getIntimationId());
//				popup = new com.vaadin.ui.Window();
//				popup.setCaption("");
//				popup.setWidth("75%");
//				popup.setHeight("85%");
//				earlierRodDetailsViewObj = earlierRodDetailsViemImplInstance.get();
//				ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
//				documentDetails.setClaimDto(bean.getClaimDTO());
//				earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getDocumentDetails().getRodKey());
//				popup.setContent(earlierRodDetailsViewObj);
//				popup.setClosable(true);
//				popup.center();
//				popup.setResizable(false);
//				popup.addCloseListener(new Window.CloseListener() {
//					/**
//					 * 
//					 */
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public void windowClose(CloseEvent e) {
//						System.out.println("Close listener called");
//					}
//				});
//
//				popup.setModal(true);
//				UI.getCurrent().addWindow(popup);
//			}
//		});
//		
//		FormLayout viewEarlierRODLayout = new FormLayout(viewEarlierRODDetails);
//		
//		FormLayout viewDetailsForm = new FormLayout();
//		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
//		viewDetailsForm.setWidth("-1px");
//		viewDetailsForm.setHeight("-1px");
//		viewDetailsForm.setMargin(false);
//		viewDetailsForm.setSpacing(true);
//		//ComboBox viewDetailsSelect = new ComboBox()
//		viewDetails.initView(bean.getClaimDTO().getNewIntimationDto().getIntimationId(),bean.getDocumentDetails().getRodKey(),ViewLevels.PREAUTH_MEDICAL);
//		viewDetailsForm.addComponent(viewDetails);
//		
//		
//	//	viewDetailsForm.addComponent(viewDetailsSelect);
//	//	Button goButton = new Button("GO");
//		/*HorizontalLayout horizontalLayout1 = new HorizontalLayout(
//				viewDetailsForm, goButton);*/
//	/*	HorizontalLayout horizontalLayout1 = new HorizontalLayout(
//				viewDetailsForm);
//		horizontalLayout1.setSizeUndefined();
//		//Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
//		horizontalLayout1.setSpacing(true);*/
//
//		
//	//	HorizontalLayout componentsHLayout = new HorizontalLayout(hLayout, viewEarlierRODLayout, viewDetailsForm);
//		/*HorizontalLayout componentsHLayout = new HorizontalLayout( viewEarlierRODLayout, viewDetailsForm);
//		//HorizontalLayout alignmentHLayout = new HorizontalLayout(componentsHLayout);
//		componentsHLayout.setWidth("100%");
//		componentsHLayout.setComponentAlignment(viewEarlierRODLayout, Alignment.BOTTOM_RIGHT);
//	Button btnRRC = new Button("RRC");
//		btnRRC.addClickListener(new ClickListener() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				validateUserForRRCRequestIntiation();
//				
//			}
//			
//		});
//		
//	//	viewDetailsForm.addComponent(viewDetailsSelect);
//	//	Button goButton = new Button("GO");
//		/*HorizontalLayout horizontalLayout1 = new HorizontalLayout(
//				viewDetailsForm, goButton);*/
//	/*	HorizontalLayout horizontalLayout1 = new HorizontalLayout(
//				viewDetailsForm);
//		horizontalLayout1.setSizeUndefined();
//		//Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
//
//		horizontalLayout1.setSpacing(true);*/
//		Button btnRRC = new Button("RRC");
//		btnRRC.addClickListener(new ClickListener() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				validateUserForRRCRequestIntiation();
//				
//			}
//			
//		});
//
//		/*HorizontalLayout componentsHLayout = new HorizontalLayout(viewEarlierRODLayout, viewDetailsForm);
//		componentsHLayout.setComponentAlignment(viewEarlierRODLayout,  Alignment.BOTTOM_RIGHT);
//		HorizontalLayout rrcBtnLayout = new HorizontalLayout(btnRRC);
//		
//		VerticalLayout vLayout = new VerticalLayout(componentsHLayout , rrcBtnLayout);
//		vLayout.setComponentAlignment(rrcBtnLayout, Alignment.TOP_RIGHT);
//
//		
//		
//		HorizontalLayout alignmentHLayout = new HorizontalLayout(vLayout);
//		
//		HorizontalLayout componentsHLayout = new HorizontalLayout(hLayout, viewEarlierRODLayout, viewDetailsForm);
//		//HorizontalLayout alignmentHLayout = new HorizontalLayout(componentsHLayout);
//		componentsHLayout.setWidth("100%");
//		componentsHLayout.setComponentAlignment(viewEarlierRODLayout, Alignment.BOTTOM_RIGHT);
//		return alignmentHLayout;*/
//		
//		HorizontalLayout componentsHLayout = new HorizontalLayout(viewEarlierRODLayout, viewDetailsForm);
//		//HorizontalLayout componentsHLayout = new HorizontalLayout(hLayout, viewEarlierRODLayout);
//		componentsHLayout.setComponentAlignment(viewEarlierRODLayout,  Alignment.BOTTOM_RIGHT);
//		//componentsHLayout.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
//		componentsHLayout.setSpacing(true);
//		componentsHLayout.setWidth("100%");
//		HorizontalLayout rrcBtnLayout = new HorizontalLayout(btnRRC);
//		//rrcBtnLayout.setComponentAlignment(btnRRC, Alignment.TOP_LEFT);
//		
//		VerticalLayout vLayout = new VerticalLayout(componentsHLayout ,  rrcBtnLayout);
//		//vLayout.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
//		vLayout.setComponentAlignment(rrcBtnLayout, Alignment.TOP_RIGHT);
//		vLayout.setWidth("100%");
//		
//		
//		HorizontalLayout alignmentHLayout = new HorizontalLayout(vLayout);
//		alignmentHLayout.setWidth("100%");
//	//	alignmentHLayout.setComponentAlignment(vLayout, Alignment.BOTTOM_RIGHT);
//		//componentsHLayout.setWidth("100%");
//		//componentsHLayout.setComponentAlignment(viewEarlierRODLayout, Alignment.BOTTOM_RIGHT);
//		//return componentsHLayout;
//		return alignmentHLayout;
//	}
	
	private HorizontalLayout commonButtonsLayout()
	{
		
		/*TextField acknowledgementNumber = new TextField("Acknowledgement Number");
		acknowledgementNumber.setValue(String.valueOf(this.bean.getAcknowledgementNumber()));
		//Vaadin8-setImmediate() acknowledgementNumber.setImmediate(true);
		acknowledgementNumber.setWidth("250px");
		acknowledgementNumber.setHeight("20px");
		acknowledgementNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		acknowledgementNumber.setReadOnly(true);
		acknowledgementNumber.setEnabled(false);
		acknowledgementNumber.setNullRepresentation("");
		FormLayout hLayout = new FormLayout (acknowledgementNumber);
		hLayout.setComponentAlignment(acknowledgementNumber, Alignment.MIDDLE_LEFT);*/
		
		Button viewEarlierRODDetails = new Button("View Earlier ROD Details");
		viewEarlierRODDetails.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
//					viewDetails.getTranslationMiscRequest(bean.getNewIntimationDTO().getIntimationId());
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
				earlierRodDetailsViewObj = earlierRodDetailsViemImplInstance.get();
				ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
				documentDetails.setClaimDto(bean.getClaimDTO());
				earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getDocumentDetails().getRodKey());
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
		
		/*FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);	
		viewDetailsForm.setSpacing(true);
		
		//ComboBox viewDetailsSelect = new ComboBox()
	
		viewDetailsForm.addComponent(viewDetails);*/
		
		
	//	viewDetailsForm.addComponent(viewDetailsSelect);
	//	Button goButton = new Button("GO");
		/*HorizontalLayout horizontalLayout1 = new HorizontalLayout(
				viewDetailsForm, goButton);*/
	/*	HorizontalLayout horizontalLayout1 = new HorizontalLayout(
				viewDetailsForm);
		horizontalLayout1.setSizeUndefined();
		//Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
		horizontalLayout1.setSpacing(true);*/

		
	//	HorizontalLayout componentsHLayout = new HorizontalLayout(hLayout, viewEarlierRODLayout, viewDetailsForm);
		/*HorizontalLayout componentsHLayout = new HorizontalLayout( viewEarlierRODLayout, viewDetailsForm);
		//HorizontalLayout alignmentHLayout = new HorizontalLayout(componentsHLayout);
		componentsHLayout.setWidth("100%");
		componentsHLayout.setComponentAlignment(viewEarlierRODLayout, Alignment.BOTTOM_RIGHT);
	Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		
	//	viewDetailsForm.addComponent(viewDetailsSelect);
	//	Button goButton = new Button("GO");
		/*HorizontalLayout horizontalLayout1 = new HorizontalLayout(
				viewDetailsForm, goButton);*/
	/*	HorizontalLayout horizontalLayout1 = new HorizontalLayout(
				viewDetailsForm);
		horizontalLayout1.setSizeUndefined();
		//Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);

		horizontalLayout1.setSpacing(true);*/
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		
		TextField icrEarnedPremium = new TextField("ICR(Earned Premium %)");
		if(null != bean.getNewIntimationDTO().getIcrEarnedPremium()){
			icrEarnedPremium.setValue(bean.getNewIntimationDTO().getIcrEarnedPremium());
		}
		icrEarnedPremium.setEnabled(Boolean.FALSE);
		FormLayout icrLayout = new FormLayout(icrEarnedPremium);
		icrLayout.setMargin(false);
		icrLayout.setSpacing(true);
		
		HorizontalLayout componentsHLayout = new HorizontalLayout();
	
		/*if( ReferenceTable.GMC_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			
			componentsHLayout = new HorizontalLayout(btnRRC,viewEarlierRODDetails,icrLayout);
		}
		else
		{
			
		}*/
		
		//added for businss chart view 
				btnBusinessProfileChart = new Button("View Mini Business Profile");
				btnBusinessProfileChart.setStyleName(ValoTheme.BUTTON_DANGER);
				btnBusinessProfileChart.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						// TODO Auto-generated method stub
						viewBusinessProfileChart.init(bean.getPreauthDTO());
						
						bean.getPreauthDTO().setIsBusinessProfileClicked(true);
						Button button = (Button)event.getSource();
						button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						
						Window popup = new com.vaadin.ui.Window();
						popup.setCaption("VIEW MINI BUSINESS PROFILE");
						popup.setWidth("75%");
						popup.setHeight("75%");
						popup.setContent(viewBusinessProfileChart);
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
							}
						});

						popup.setModal(true);
						UI.getCurrent().addWindow(popup);
					}
				});	
		
		HorizontalLayout formLayout = SHAUtils.newImageCRM(bean.getPreauthDTO());
		HorizontalLayout crmLayout = new HorizontalLayout(formLayout);
		crmLayout.setSpacing(false);
		crmLayout.setWidth("100%");
		HorizontalLayout icrAgentBranch = SHAUtils.icrAgentBranch(bean.getPreauthDTO());
		HorizontalLayout buyBackPedHLayout = new HorizontalLayout();
		
		//GLX2020162 topup policy creation for HC policy
		viewLinkPolicyDtls = new Button("View Linked Policy");
		Policy linkPolicyKey = null;
		if((bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo() != null && 
				!bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo().isEmpty()) ||(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo() != null && 
				!bean.getNewIntimationDTO().getPolicy().getBasePolicyNo().isEmpty())){
			if(bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo() != null && 
					!bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo().isEmpty()){
				linkPolicyKey = policyService.getKeyByPolicyNumber(bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo());
			}else if(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo() != null && 
					!bean.getNewIntimationDTO().getPolicy().getBasePolicyNo().isEmpty()){
				linkPolicyKey = policyService.getKeyByPolicyNumber(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo());
			}
			viewLinkPolicyDtls.setVisible(true);
		}else {
			viewLinkPolicyDtls.setVisible(false);
		}
		if(linkPolicyKey != null && linkPolicyKey.getKey() != null){
			final ShortcutListener sListener = callSListener();
			final EnhancedBrowserWindowOpener opener = new EnhancedBrowserWindowOpener(BasePolicyPreviousClaimWindowUI.class);
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,linkPolicyKey.getKey());					
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,ViewBasePolicyClaimsWindowOpen);
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,preauthPreviousClaimsTable);
			opener.popupBlockerWorkaround(true);

			opener.withShortcut(sListener);
			opener.setFeatures("height=700,width=1300,resizable");
			opener.doExtend(viewLinkPolicyDtls);
			//					setSopener(opener);
			viewLinkPolicyDtls.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					opener.open();
				}
			});
		}	
		
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
		HorizontalLayout buttonsLayout = new HorizontalLayout(btnRRC,viewEarlierRODDetails,viewLinkPolicyDtls,btnBusinessProfileChart);
		VerticalLayout icrAGBR = new VerticalLayout(crmLayout,icrAgentBranch,buttonsLayout);
		
		
		componentsHLayout = new HorizontalLayout(icrAGBR);
		componentsHLayout.setSpacing(false);
		
		return componentsHLayout;
}
	 
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(BillEntryWizardPresenter.VALIDATE_BILL_ENTRY_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.BILL_ENTRY);
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
		fireViewEvent(BillEntryWizardPresenter.SUBMIT_BILL_ENTRY_DETAILS, this.bean);
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		/*ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
		        "No", "Yes", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                	
		                	releaseHumanTask();
		                	SHAUtils.setClearReimbursementDTO(bean);
		    				fireViewEvent(MenuItemBean.ENTER_BILL_DETAILS, null);
		    				documentDetailsView.setClearReferenceData();
		    				uploadDocumentView.setClearReferenceData();
		    				billEntryViewImplObj.setClearReferenceData();
		    				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
		    				SHAUtils.clearSessionObject(currentRequest);
		    				
		                	//fireViewEvent(MenuPresenter.SHOW_CREATE_ROD_WIZARD, null);
		                    // Confirmed to continue
		                	//fireViewEvent(MenuItemBean.PROCESS_PRE_MEDICAL, null);
		                	//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
		                } else {
		                    // User did not confirm
		                }
		            }
		        });*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
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
				fireViewEvent(MenuItemBean.ENTER_BILL_DETAILS, null);
				documentDetailsView.setClearReferenceData();
				uploadDocumentView.setClearReferenceData();
				billEntryViewImplObj.setClearReferenceData();
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
			
			}
			});
		noButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {}
			});
		
		/*dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);*/
		
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardSave(GWizardEvent event) {
		fireViewEvent(BillEntryWizardPresenter.SUBMIT_BILL_ENTRY_DETAILS, this.bean);

		
	}

	@Override
	public void buildSuccessLayout() {
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Bill Entry Home");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("Bill Entry Completed successfully !!!</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				toolBar.countTool();
				SHAUtils.setClearReimbursementDTO(bean);
				fireViewEvent(MenuItemBean.ENTER_BILL_DETAILS, null);
				documentDetailsView.setClearReferenceData();
				uploadDocumentView.setClearReferenceData();
				billEntryViewImplObj.setClearReferenceData();
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		
		
		
	
		
	}

	@Override
	public void buildFailureLayout(String strMessage) {
	
		//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
		/*Label successLabel = new Label("<b style = 'color: green;'>Hospitalization ROD is pending for financial approval.</br> Please try submitting this ROD , after hospitalization ROD is approved.</b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Bill Entry Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Bill Entry Home");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("Hospitalization ROD is pending for financial approval.</br> Please try submitting this ROD , after hospitalization ROD is approved.</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				fireViewEvent(MenuItemBean.ENTER_BILL_DETAILS, null);
				documentDetailsView.setClearReferenceData();
				uploadDocumentView.setClearReferenceData();
				billEntryViewImplObj.setClearReferenceData();
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
	}

	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
		documentDetailsView.setCoverList(coverContainer);
		
	}

	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {
		documentDetailsView.setSubCoverContainer(subCoverContainer);
		
	}
	
	  private void releaseHumanTask(){
			
			Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
	     	/*String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
	 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);*/
	 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

	 		if(existingTaskNumber != null){
	 		//	BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
	 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
	 		}
	 		
	 		if(wrkFlowKey != null){
	 			DBCalculationService dbService = new DBCalculationService();
	 			dbService.callUnlockProcedure(wrkFlowKey);
	 			getSession().setAttribute(SHAConstants.WK_KEY, null);
	 		}
		}
	  
	  @Override
		public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
			 rewardRecognitionRequestViewObj.setsubCategoryValues(selectValueContainer, subCategory, value);
		 }
		 
		 @Override
		 public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
			 rewardRecognitionRequestViewObj.setsourceValues(selectValueContainer, source, value);
		 }

		 
		 @SuppressWarnings("serial")
		 public ShortcutListener callSListener(){
			 ShortcutListener shortcutListener = new ShortcutListener("PreviousClaimDetails", KeyCode.NUM3, new int[]{ModifierKey.CTRL, ModifierKey.ALT}) {
				 @Override
				 public void handleAction(Object sender, Object target) {
					 viewLinkPolicyDtls.click();
				 }
			 };
			 getActionManager().addAction(shortcutListener);
			 return shortcutListener;
		 }
}

