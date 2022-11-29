package com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorPagePresenter;
import com.shaic.claim.OMPViewDetails.view.OMPViewCurrentPolicyDetailsUI;
import com.shaic.claim.OMPprocessrejection.detailPage.OMPPreviousClaimWindowUI;
import com.shaic.claim.OMPprocessrejection.detailPage.ViewOMPPreviousClaimWindowOpen;
import com.shaic.claim.OMPprocessrejection.detailPage.ViewOMPPreviousClaimsTable;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersEvents;
import com.shaic.domain.OMPViewClaimantDetailsPageUI;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class OMPAcknowledgementDocumentsPageUI extends ViewComponent {
	@Inject
	private Instance<IntimationDetailsCarousel> commonCarouselInstance;
	
	private VerticalLayout mainPanel;
	
	@Inject
	private OMPClaimProcessorDTO bean;

	private Button riskButton;

	private Button balanceSi;

	private Button policyDetails;

	private Button vb64Button;

	private Button viewMedButton;

	private Button previousClaimButton;

	private Button btnClose;

	private Button btnReOpen;

	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private OMPViewCurrentPolicyDetailsUI ompViewCurrentPolicyUI;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private MasterService masterService;
	
	@Inject
	private Instance<ViewOMPPreviousClaimWindowOpen> viewOMPPreviousClaimWindowOpen;
	
	@Inject
	private ViewOMPPreviousClaimsTable ompPreviousClaimsTable;
	
	@Inject
	private OMPViewClaimantDetailsPageUI ompClaimantUI;
	
	@EJB
	private IntimationService intimService;
	
	private BeanFieldGroup<OMPClaimProcessorDTO> binder;

	private OptionGroup claimTypeOption;

	private OptionGroup hospitalOption;
	
	private ComboBox cmbEventCode;

	private VerticalLayout  mainLayout;
	
	protected Button btnCancel;
	
	@Inject
	private Instance<OMPNewAcknowledgementDocumentsViewTable> ompClaimCalcViewTableInstance;
	
	private OMPNewAcknowledgementDocumentsViewTable ompClaimCalcViewTableObj;
	
	Map<String, Object> referenceDataMap = new HashMap<String, Object>();
	
	private Button earlierAcknowledgementDetailsButton;
	private Button acknowledgementDetailsButton;
	
	private boolean isTPAUserLogin;

	public boolean isTPAUserLogin() {
		return isTPAUserLogin;
	}

	public void setTPAUserLogin(boolean isTPAUserLogin) {
		this.isTPAUserLogin = isTPAUserLogin;
	}
	
	@Override
	public String getCaption() {
		return "Bill Hospitalization";
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}
	
	public void init(OMPClaimProcessorDTO bean, BeanItemContainer<SelectValue> classification, BeanItemContainer<SelectValue> subClassification, 
			BeanItemContainer<SelectValue> paymentTo, BeanItemContainer<SelectValue> paymentMode, BeanItemContainer<SelectValue> eventCode, 
			BeanItemContainer<SelectValue> currencyValue, BeanItemContainer<SelectValue> negotiatorName, BeanItemContainer<SelectValue> modeOfReciept, 
			BeanItemContainer<SelectValue> documentRecievedFrom, BeanItemContainer<SelectValue> documentType, BeanItemContainer<SelectValue> country) {
		this.bean = bean;
		
		ImsUser user = (ImsUser)UI.getCurrent().getSession().getAttribute("imsUser");
		isTPAUserLogin = user.getFilteredRoles().contains("CLM_OMP_TPA_INTIMATION");
		
		IntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
		intimationDetailsCarousel.initOMPCarousal(this.bean.getNewIntimationDto(), "Acknowledge Receipt of Document");
		mainPanel = new VerticalLayout();
		mainPanel.addComponent(intimationDetailsCarousel);
		mainPanel.addComponent(viewDetailsButtonLayout());
		HorizontalLayout dummyhLayout = new HorizontalLayout();
		dummyhLayout.setSpacing(true);
		dummyhLayout.setMargin(true);
		mainPanel.addComponent(commonButtonsLayout(eventCode, country));
		mainPanel.setSizeFull();
		mainLayout.addComponent(getContent());
		if(bean.getIsMutipleRod()){
			cmbEventCode.setReadOnly(Boolean.TRUE);	
			claimTypeOption.setEnabled(Boolean.FALSE);
		}
		//addListenerForEventCode();
		
		ConfirmDialog confirmDialog = new ConfirmDialog();
		HorizontalLayout wizardLayout5 = new HorizontalLayout (getCancelButton(confirmDialog));
		//mainPanel.setComponentAlignment(wizardLayout5, Alignment.BOTTOM_RIGHT);
		wizardLayout5.setCaption("");
		wizardLayout5.setSpacing(true);
		wizardLayout5.setSizeFull();
		wizardLayout5.setWidth("50%");
		mainPanel.addComponent(commonButtonsLayout());
		
		setCompositionRoot(mainPanel);
	}
	
	private VerticalLayout viewDetailsButtonLayout() {
		
		 riskButton = new Button("View Risk Details");
		riskButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getNewIntimationDto()!= null && bean.getNewIntimationDto().getPolicy()!= null ||bean.getIntimationId()!=null){
					viewDetails.getViewOmpRiskDetails(bean.getNewIntimationDto().getPolicy(),bean.getIntimationId());
				}
			}
		});
		
		earlierAcknowledgementDetailsButton = new Button("Earlier Acknowledgement Details");
		earlierAcknowledgementDetailsButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getNewIntimationDto()!= null && bean.getNewIntimationDto().getPolicy()!= null ||bean.getIntimationId()!=null){
					viewDetails.getEarlierAcknowledgementDetailsButton(bean.getIntimationId());
				}
			}
		});
		
		acknowledgementDetailsButton = new Button("View Acknowledgement Details");
		acknowledgementDetailsButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getNewIntimationDto()!= null && bean.getNewIntimationDto().getPolicy()!= null ||bean.getIntimationId()!=null){
					viewDetails.getAcknowledgementDetailsButton(bean.getIntimationId());
				}
			}
		});
		
		 balanceSi = new Button("View Balance SI");
		 if(isTPAUserLogin){
			 balanceSi.setEnabled(Boolean.FALSE); 
		 }
		 balanceSi.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
					
					viewDetails.getViewBalanceSumInsured(bean.getIntimationId());
				}
				else{
//					showErrorMessage("Intimation not available");
				}
			}
			
		});
		
		 policyDetails = new Button("View Policy Details");
		 policyDetails.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getNewIntimationDto()!= null && bean.getNewIntimationDto().getPolicy()!= null){
					Policy policy = bean.getNewIntimationDto().getPolicy();
					ompViewCurrentPolicyUI.setPolicyServiceAndPolicy(policyService, policy, masterService);
					ompViewCurrentPolicyUI.initView();
					UI.getCurrent().addWindow(ompViewCurrentPolicyUI);
				}
			}
			
		});
		
		 
		 vb64Button = new Button("View 64VB");
		 if(isTPAUserLogin){
			 vb64Button.setEnabled(Boolean.FALSE); 
		 }
		 vb64Button.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
					viewDetails.getIrda64VbDetails(bean.getIntimationId());
				}
				else{
//					showErrorMessage("Intimation not available");
				}
			}
			
		});
		 
		 viewMedButton = new Button("View MER Details");
		 if(isTPAUserLogin){
			 viewMedButton.setEnabled(Boolean.FALSE); 
		 }
		 viewMedButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
					
					String merUrl = viewDetails.getOMPMERDetails(bean.getIntimationId());
					
					if(merUrl != null && !merUrl.isEmpty()){
						getUI().getPage().open(merUrl, "_blank",1500,800,BorderStyle.NONE);
					}
//					viewDetails.getOMPMERDetails(bean.getIntimationId());
				}
//				showErrorMessage("Intimation not available");
			}
			
		});
		 
		 previousClaimButton = new Button("View Deductibles");
		 if(isTPAUserLogin){
			 previousClaimButton.setEnabled(Boolean.FALSE); 
		 }
		 previousClaimButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
					
					viewDetails.getDeductibles(bean.getIntimationId());
				}
				else{
//					showErrorMessage("Intimation not available");
				}
			}
			
		});
		 
		 Button viewHistoryButton = new Button("View Claim History");
		 viewHistoryButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
					viewDetails.getOmpViewClaimHistory(bean.getIntimationId());
				}
			}
			
		});
		 
		 final Button viewPreviousPolicyClaimButton = new Button("Prev Claim Details");
		 if(isTPAUserLogin){
			 viewPreviousPolicyClaimButton.setEnabled(Boolean.FALSE); 
		 }
		 if(bean.getIntimationId()!= null){
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,bean.getIntimationId());
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,viewOMPPreviousClaimWindowOpen);
				VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,ompPreviousClaimsTable);
				
		}
		 BrowserWindowOpener opener = new BrowserWindowOpener(OMPPreviousClaimWindowUI.class);
		 	opener.setFeatures("height=700,width=1300,resizable");
		 	opener.setWindowName("_blank");
			opener.extend(viewPreviousPolicyClaimButton);
		 viewPreviousPolicyClaimButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,bean.getIntimationId());
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,viewOMPPreviousClaimWindowOpen);
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,ompPreviousClaimsTable);
			
				}
				else{
//					showErrorMessage("Intimation not available");
				}
			}
			
		});
		 
		 Button viewClaimantButton = new Button("View Claimant Details");
		 viewClaimantButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				Window popup =null;
				popup = new com.vaadin.ui.Window();
				popup.setWidth("800px");
				popup.setHeight("280px");
				popup.setCaption("View Claimant Details");
				Policy policy = bean.getNewIntimationDto().getPolicy();
				ompClaimantUI.setPolicyServiceAndPolicy(policyService, policy, masterService);
				Policy apolicy = policyService.getPolicy(policy.getPolicyNumber());
				ompClaimantUI.init(apolicy);
				popup.setContent(ompClaimantUI);
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
		 
		 Button viewDocButton = new Button("View Documents");
		 viewDocButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
					BPMClientContext bpmClientContext = new BPMClientContext();
					Long dummyno = 1l;
					String dummystrin = "";
					Map<String,String> tokenInputs = new HashMap<String, String>();
					 tokenInputs.put("intimationNo", bean.getIntimationId());
					 tokenInputs.put("ompdoc", dummyno.toString());
					 String intimationNoToken = null;
					  try {
						  intimationNoToken = intimService.createJWTTokenForClaimStatusPages(tokenInputs);
					  } catch (NoSuchAlgorithmException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
					  } catch (ParseException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
					  }
					tokenInputs = null;  
					String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
					/*Below code commented for security reason
					String url = bpmClientContext.getGalaxyDMSUrl() + bean.getIntimationId() + "&&ompdoc?" + dummyno;*/
//					String url = bpmClientContext.getGalaxyDMSUrl() + bean.getIntimationId();
				//	getUI().getPage().open(url, "_blank");
					getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
//					viewDetails.getViewDocumentByPolicyNo(dtoBean.getIntimationno());
				}
				else{
//					showErrorMessage("Intimation not available");
				}
			}
			
		});
		 
		 Button viewOtherCurrency = new Button("View Other Currency Rate");
		 if(isTPAUserLogin){
			 viewOtherCurrency.setEnabled(Boolean.FALSE); 
		 }
		 viewOtherCurrency.addClickListener(new ClickListener() {
			 private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
				if(bean.getIntimationId()!= null){
					viewDetails.getviewOtherCurrencyRateDetails(bean.getIntimationId(),bean.getRodKey());
				}
			}
	});
		 
		 HorizontalLayout horizontalLayout = new HorizontalLayout(acknowledgementDetailsButton,earlierAcknowledgementDetailsButton,riskButton,balanceSi,policyDetails,vb64Button,viewMedButton);
		 HorizontalLayout horizontalLayout2 = new HorizontalLayout(previousClaimButton,viewHistoryButton, viewPreviousPolicyClaimButton,viewClaimantButton,viewOtherCurrency,viewDocButton);
			VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout,horizontalLayout2);
			verticalLayout.setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
			verticalLayout.setComponentAlignment(horizontalLayout2, Alignment.TOP_RIGHT);
			verticalLayout.setSpacing(true);
			verticalLayout.setWidth("100%");
			verticalLayout.setHeight("95px");
			return verticalLayout;
}
	public void initBinder() {
		this.binder = new BeanFieldGroup<OMPClaimProcessorDTO>(
				OMPClaimProcessorDTO.class);
		this.binder.setItemDataSource(bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	@SuppressWarnings("serial")
	private VerticalLayout commonButtonsLayout(BeanItemContainer<SelectValue> eventCode,BeanItemContainer<SelectValue> country){
		initBinder();
		FormLayout emptyFormLayoutOne = new FormLayout(new Label());
		emptyFormLayoutOne.setSizeFull();
		FormLayout emptyFormLayoutTwo = new FormLayout(new Label());	
		emptyFormLayoutTwo.setSizeFull();
//		claimTypeOption = new OptionGroup();
		claimTypeOption = binder.buildAndBind("","claimType",OptionGroup.class);
		claimTypeOption.setNullSelectionAllowed(true);
		claimTypeOption.addItems(getReadioButtonOptions());
		claimTypeOption.setItemCaption(true, "Cashless");
		claimTypeOption.setItemCaption(false, "Reimbursement");
		claimTypeOption.setStyleName("horizontal");
		hospitalOption = new OptionGroup();
//		hospitalOption = binder.buildAndBind("","hospTypeBooleanval",OptionGroup.class);
		hospitalOption.addItem("Non Medical");
		if(bean.getNonHospitalisationFlag()!= null && bean.getNonHospitalisationFlag().equalsIgnoreCase("Y")){
			hospitalOption.setValue("Non Medical");
		}
		if(String.valueOf(hospitalOption.getValue()).equals("Non Medical")){
			this.bean.setNonHospitalisationFlag("Y");
		}else{
			this.bean.setNonHospitalisationFlag("N");
		}
		if(claimTypeOption!= null && claimTypeOption.getValue()!= null&& claimTypeOption.getValue().equals(Boolean.TRUE)){
			this.bean.setIsCashless(Boolean.TRUE);
		}else{
			this.bean.setIsCashless(Boolean.FALSE);
		}
		claimTypeOption.setEnabled(Boolean.FALSE);
		hospitalOption.setEnabled(Boolean.FALSE);
		cmbEventCode = binder.buildAndBind("Event Code" , "eventCode", ComboBox.class);
		cmbEventCode.setContainerDataSource(eventCode);
		cmbEventCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbEventCode.setItemCaptionPropertyId("value");
		cmbEventCode.setValue(bean.getEventCode());
		cmbEventCode.setEnabled(Boolean.FALSE);
		
		TextField dummyfield = new TextField();
		dummyfield.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyfield.setReadOnly(true);
		
		FormLayout lossDetailsFrom = new FormLayout(cmbEventCode,claimTypeOption/*,admissionDate,dateofDischarge,txtAilmentLoss,txtPlaceOfAccident,txtplaceofvisit,lossDateField,lossTime,txtPlaceOfLossOrDelay,patientStatus,deathDate*/);
		
		FormLayout lossDetailsForm1 = new FormLayout(dummyfield,hospitalOption/*,addHospital,cmbHospitalCodeOrName,txtCity,cmbCountry,txtAvaiSi,txtlossDetails,txtplaceofvisit*/);
		addListener();
		cmbEventCode.setValue(null);
		cmbEventCode.setValue(bean.getEventCode());
		HorizontalLayout lossDetailsLayuot = new HorizontalLayout(lossDetailsFrom,emptyFormLayoutTwo,lossDetailsForm1);
		lossDetailsLayuot.setSpacing(true);
		lossDetailsLayuot.setMargin(true);
		lossDetailsLayuot.setCaption("Loss Details");
//		addListener();
		VerticalLayout vLayout = new VerticalLayout(lossDetailsLayuot/*,provisnLayout,cgLayout*/);
		
		HorizontalLayout billDetailsLayout = new HorizontalLayout();
		//billDetailsLayout.setCaption("Document Details");
		//billDetailsLayout.addComponent(getContent(/*eventCode*/));
		billDetailsLayout.setMargin(true);
		billDetailsLayout.setSpacing(true);
		billDetailsLayout.setSizeFull();
		
		mainLayout = new VerticalLayout();
		mainLayout.addComponent(vLayout);
		mainLayout.addComponent(billDetailsLayout);
		//mainLayout.addComponent(wizardLayout5);
		/*mainLayout.addComponent(getApproverContent());
		mainLayout.addComponent(fotterLayout);
		mainLayout.setComponentAlignment(fotterLayout, Alignment.MIDDLE_CENTER);*/
		mainLayout.setSpacing(Boolean.TRUE);
		mainLayout.setMargin(Boolean.TRUE);
		
		return mainLayout;
	}
	@SuppressWarnings("serial")
	private VerticalLayout commonButtonsLayout(){
		
		ConfirmDialog confirmDialog = new ConfirmDialog();
		HorizontalLayout wizardLayout5 = new HorizontalLayout (getCancelButton(confirmDialog));
		wizardLayout5.setCaption("");
		wizardLayout5.setSpacing(true);
		wizardLayout5.setSizeFull();
		wizardLayout5.setWidth("50%");
		HorizontalLayout dummyLayout5 = new HorizontalLayout ();
		HorizontalLayout dummyLayout6 = new HorizontalLayout ();
		dummyLayout6.setCaption("");
		dummyLayout6.setSpacing(true);
		dummyLayout6.setSizeFull();
		dummyLayout6.setWidth("50%");
		dummyLayout6.addComponents(dummyLayout5,wizardLayout5);
		//mainLayout.setComponentAlignment(wizardLayout5, Alignment.MIDDLE_CENTER);
		mainLayout.addComponent(dummyLayout6);
		mainLayout.setComponentAlignment(dummyLayout6, Alignment.BOTTOM_RIGHT);
		
		return mainLayout;
	}
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	@SuppressWarnings("deprecation")
	public Component getContent(/*BeanItemContainer<SelectValue> negotiatorName*/) {
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getNewIntimationDto().getPolicy().getKey());
		if(null != memberType && !memberType.isEmpty() && memberType.equalsIgnoreCase(SHAConstants.CMD)){
			showCMDAlert();
		}
		//CR2019041
		List<String> negoRod = new ArrayList<String>();
		List<OMPClaimCalculationViewTableDTO> claimCalculationViewTableObi = bean.getClaimCalculationViewTable();
		if(claimCalculationViewTableObi!=null && !claimCalculationViewTableObi.isEmpty()){
			for(OMPClaimCalculationViewTableDTO claimCalculationViewTableObi1 : claimCalculationViewTableObi){
				if(claimCalculationViewTableObi1!=null && claimCalculationViewTableObi1.getNegotiationDone()!=null && claimCalculationViewTableObi1.getNegotiationDone().getValue()!=null
					&&  claimCalculationViewTableObi1.getNegotiationDone().getValue().equalsIgnoreCase("Yes")){
						negoRod.add(claimCalculationViewTableObi1.getAcknumber());
				}
			}
		}
		
		List<String> negoRod1 = new ArrayList<String>();
		List<OMPClaimCalculationViewTableDTO> claimCalculationViewTableObi1 = bean.getClaimCalculationViewTable();
		if(claimCalculationViewTableObi!=null && !claimCalculationViewTableObi.isEmpty()){
			for(OMPClaimCalculationViewTableDTO claimCalculationViewTableObi11 : claimCalculationViewTableObi){
				if(claimCalculationViewTableObi1!=null && claimCalculationViewTableObi11.getSelect()!=null && claimCalculationViewTableObi11.getSelect().getValue()!=null
					&& claimCalculationViewTableObi11.getClassification()!=null &&  claimCalculationViewTableObi11.getClassification().getValue()!=null 
					&& claimCalculationViewTableObi11.getClassification().getValue().equalsIgnoreCase("Negotiator Fee")){
						negoRod1.add(claimCalculationViewTableObi11.getAcknumber());
				}
			}
		}
		/*if(negoRod.size()!=negoRod1.size()){
			showErrorMessage("Please create provision for negotiation fee");
		}*/
		//CR2019041 PANNEERSELVAM 
		fireViewEvent(OMPAcknowledgementDocumentsPagePresenter.OMP_PROCESSOR_PARTICULARS, referenceDataMap);
		ompClaimCalcViewTableObj =  ompClaimCalcViewTableInstance.get();
		List<OMPClaimCalculationViewTableDTO> claimCalculationViewTable = bean.getClaimCalculationViewTable();
		ompClaimCalcViewTableObj.init(bean);
		ompClaimCalcViewTableObj.setReferenceData(referenceDataMap);
		if(bean.getClaimCalculationViewTable() != null){
			ompClaimCalcViewTableObj.setTableList(claimCalculationViewTable);
			
			}
		/*if(claimCalculationViewTable!= null && !claimCalculationViewTable.isEmpty() && claimCalculationViewTable.size() >1){
			cmbEventCode.setEnabled(false);
		}
		if(ompClaimCalcViewTableObj.dummyField.getValue()!=null){
			cmbEventCode.setEnabled(false);
		}*/
		/*txtRemark = binder.buildAndBind("Remarks (Processor)", "remarks",TextArea.class);
		txtRemark.setWidth("40%");
		txtRemark.setMaxLength(4000);*/
		VerticalLayout wholeLayouts = new VerticalLayout(ompClaimCalcViewTableObj);
		wholeLayouts.setComponentAlignment(ompClaimCalcViewTableObj, Alignment.TOP_CENTER);
		wholeLayouts.setSpacing(true);
		wholeLayouts.setSizeFull();
		 return wholeLayouts;
	}
	
	public void showCMDAlert() {	 
		 
		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.CMD_ALERT + "</b>",
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
	
	private void showErrorMessage(String eMsg) {
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
	}
	
	private Button getCancelButton(final ConfirmDialog dialog) {
		btnCancel = new Button("Cancel");
//		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				releaseWorkFlowTask();
				fireViewEvent(MenuItemBean.OMP_ACKNOWLEDGEMENT_DOCUMENTS_RECEIVED, null);
			}
		});
		return btnCancel;
	}
	
	private void releaseWorkFlowTask(){
		VaadinSession session = getSession();
		Long wrkFlowKey=(Long)session.getAttribute(SHAConstants.WK_KEY);
		DBCalculationService dbService = new DBCalculationService();
 		if(wrkFlowKey != null) {
 			dbService.callOMPUnlockProcedure(wrkFlowKey);
 			getSession().setAttribute(SHAConstants.WK_KEY, null);
 		}
	}
	private void addListener() {

		if(cmbEventCode!= null){

			cmbEventCode.addValueChangeListener( new ValueChangeListener( ) {

		          
		          /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
		          public void valueChange( ValueChangeEvent event ) {
					
					SelectValue eventSelectValue = (SelectValue) event.getProperty().getValue();
					if(eventSelectValue != null && eventSelectValue.getValue()!=null && eventSelectValue.getId()!= null) {
						/*	String eventCodeType = eventSelectValue.getValue();
							String[] split = eventCodeType.split("-");
							String description1 = split[0];
							String description2 = split[1];
							String description3 = split[2];
							String description = split[3];
							String eventCode = description1+"-"+description2+"-"+description3;
							eventCode = eventCode.trim();*/
//							MastersEvents events = masterService.getEventType(eventCode);
//							fireViewEvent(OMPClaimProcessorPagePresenter.OMP_Processor_BSI, bean,eventSelectValue);
							/*if(bean.getSumInsured()!=null && txtAvaiSi!=null){
								txtAvaiSi.setReadOnly(Boolean.FALSE);
								txtAvaiSi.setValue(bean.getSumInsured().toString());
								txtAvaiSi.setReadOnly(Boolean.TRUE);
							}*/
							if(!bean.getIsOnLoad()){
							/*if(txtInitialProvisionAmt.getValue()!=null && !txtInitialProvisionAmt.getValue().equalsIgnoreCase("")&& bean.getBalanceSI()!= null){
								double provisionAmt = SHAUtils.getDoubleFromStringWithComma(txtInitialProvisionAmt.getValue());
								if(provisionAmt > bean.getBalanceSI()){
									
									showErrorMessage( "Initial provision is greater than the Balance Sum Insured = " + bean.getBalanceSI());
									txtInitialProvisionAmt.setReadOnly(Boolean.FALSE);
									txtINRConversionRate.setReadOnly(Boolean.FALSE);
									txtInitialProvisionAmt.setValue("");
//									txtInitialProvisionAmt.setReadOnly(Boolean.TRUE);
									}
								}*/
							}
							MastersEvents events = masterService.getEventTypeByKey(eventSelectValue.getId());
							if(events!= null && events.getProcessType()!= null){
								if(events.getProcessType().equalsIgnoreCase("A") || events.getProcessType().equalsIgnoreCase("C") || events.getProcessType().equalsIgnoreCase("R")){
									if(hospitalOption!= null){
										hospitalOption.setValue(null);
										hospitalOption.setEnabled(false);
										bean.setNonHospitalisationFlag("N");
										/*if(events.getProcessType().equalsIgnoreCase("A") || events.getProcessType().equalsIgnoreCase("C")){
					  						claimTypeOption.setEnabled(Boolean.TRUE);
					  						claimTypeOption.setValue(true);
										}
					  					if(events.getProcessType().equalsIgnoreCase("R")){
					  						claimTypeOption.setEnabled(Boolean.TRUE);
					  						claimTypeOption.setValue(false);
					  					}*/
					  					/*txtadmissionDate.setVisible(Boolean.TRUE);
										dateofDischarge.setVisible(Boolean.TRUE);
										txtAilmentLoss.setVisible(Boolean.TRUE);
										txtPlaceOfAccident.setVisible(Boolean.TRUE);
										txtplaceofvisit.setVisible(Boolean.TRUE);
										cmbHospitalCodeOrName.setVisible(Boolean.TRUE);
										txtCity.setVisible(Boolean.TRUE);
										cmbCountry.setVisible(Boolean.TRUE);
										addHospital.setVisible(Boolean.TRUE);
										lossDateField.setVisible(Boolean.FALSE);
					  					lossTime.setVisible(Boolean.FALSE);
					  					txtPlaceOfLossOrDelay.setVisible(Boolean.FALSE);
					  					txtlossDetails.setVisible(Boolean.FALSE);*/
//					  					dummyTextField.setVisible(Boolean.FALSE);
					  					if(hospitalOption!=null){/*
					  						claimTypeOption.setEnabled(Boolean.TRUE);
					  						claimTypeOption.setValue("Cashless");
										*/}
					  					if(!bean.getIsOnLoad()){
					  						if(events.getProcessType().equalsIgnoreCase("A") || events.getProcessType().equalsIgnoreCase("C")){
						  						claimTypeOption.setEnabled(Boolean.TRUE);
						  						claimTypeOption.setValue(true);
											}
						  					if(events.getProcessType().equalsIgnoreCase("R")){
						  						claimTypeOption.setEnabled(Boolean.TRUE);
						  						claimTypeOption.setValue(false);
						  					}
					  					}
					  				
									}
								}
								
								if(events!= null && events.getProcessType()!= null){
									if(events.getProcessType().equalsIgnoreCase("N")){
										if(hospitalOption!= null){
											hospitalOption.setEnabled(Boolean.TRUE);
											hospitalOption.setValue("Non Medical");
											bean.setNonHospitalisationFlag("Y");
											/*lossDateField.setVisible(Boolean.TRUE);
											lossTime.setVisible(Boolean.FALSE);
											txtPlaceOfLossOrDelay.setVisible(Boolean.TRUE);
						  					txtlossDetails.setVisible(Boolean.TRUE);
						  					txtplaceofvisit.setVisible(Boolean.TRUE);
						  					txtadmissionDate.setVisible(Boolean.FALSE);
						  					dateofDischarge.setVisible(Boolean.FALSE);
						  					txtAilmentLoss.setVisible(Boolean.FALSE);
											txtPlaceOfAccident.setVisible(Boolean.FALSE);
//											placeofvisit.setVisible(false);
											cmbHospitalCodeOrName.setVisible(Boolean.FALSE);
											txtCity.setVisible(Boolean.FALSE);
											cmbCountry.setVisible(Boolean.FALSE);
											addHospital.setVisible(Boolean.FALSE);*/
//											dummyTextField.setVisible(Boolean.FALSE);
											if(hospitalOption!=null){
//												optionClaimtype.setValue("Reimbursement");
												claimTypeOption.setEnabled(Boolean.FALSE);
												claimTypeOption.setValue(null);
						  				}else{
						  					claimTypeOption.setEnabled(true);
						  					}
						  				}
									}
								}
								if(events!= null && events.getEventCode().equalsIgnoreCase("OMP-CVR-011") ||  events.getEventCode().equalsIgnoreCase("CFT-CVR-009") ||  events.getEventCode().equalsIgnoreCase("OMP-CVR-012") ||
										 events.getEventCode().equalsIgnoreCase("CFT-CVR-010") ||  events.getEventCode().equalsIgnoreCase("OMP-CVR-015") ||  events.getEventCode().equalsIgnoreCase("CFT-CVR-012")){
									
									/*lossTime.setVisible(Boolean.TRUE);
									if(StringUtils.isBlank(String.valueOf(lossTime.getValue()))){
//				  						hasError = true;
//				  						eMsg = eMsg + "Please Provide lossDate. </br>";
				  					}*/
								}
								Boolean copayEnable =Boolean.TRUE;
								if(bean.getProductCode()!=null){/*
									String productCode = bean.getProductCode();
									if(!productCode.equalsIgnoreCase("OMP-PRD-001")){
										copayEnable = Boolean.FALSE;
									}else if(!events.getEventCode().equalsIgnoreCase("OMP-CVR-004")){
										copayEnable = Boolean.FALSE;
										}else if(bean.getAge()<75){
											copayEnable = Boolean.FALSE;
											}else if(bean.getAge()>75 && bean.getAge()<80){
												List<String> planList = new ArrayList<String>();
												planList.add("PLAN C1");
												planList.add("PLAN C2");
												planList.add("PLAN D1");
												planList.add("PLAN D2");
												if(!planList.contains(bean.getPlan())){
													copayEnable = Boolean.FALSE;
											}
										}
								*/}else{/*
									copayEnable = Boolean.FALSE;
								*/}
								/*if(ompClaimCalcViewTableObj!=null){
									ompClaimCalcViewTableObj.enableCopayDisableField(copayEnable);
									
								}
								bean.setCopayEnable(copayEnable);*/
								
								/*if(!bean.getIsOnLoad()){
									ompClaimCalcViewTableObj.setClassificationField();
								}*/
//								
							}
							

						}
					}
			});
}}}
