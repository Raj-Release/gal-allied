/**
 * 
 */
package com.shaic.claim.rod.wizard.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.teemu.wizards.GWizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.galaxyalert.utils.GalaxyTypeofMessage;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.PedRaisedDetailsTable;
import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.pages.DocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.rod.wizard.tables.DocumentCheckListTable;
import com.shaic.claim.rod.wizard.tables.DocumentCheckListValidationListenerTable;
import com.shaic.claim.rod.wizard.tables.RODQueryDetailsTable;
import com.shaic.claim.rod.wizard.tables.ReconsiderRODRequestListenerTable;
import com.shaic.claim.rod.wizard.tables.SectionDetailsListenerTable;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PAPamentQueryDetailsTable;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.vijayar
 *
 */
public class DocumentDetailsPage  extends ViewComponent {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BeanFieldGroup<DocumentDetailsDTO> binder;
	
	private GWizard wizard;

	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	@Inject
	private Instance<SectionDetailsListenerTable> sectionDetailsListenerTable;
	
	private SectionDetailsListenerTable sectionDetailsListenerTableObj;
	
	
	//private Instance<RODQueryDetailsTable> rodQueryDetailsObj;
	
	@Inject
	public RODQueryDetailsTable rodQueryDetails;
	
	@Inject
	public PAPamentQueryDetailsTable paymentQueryDetails;
	
	//private ReconsiderRODRequestTable reconsiderRequestDetails;
	@Inject
	private ReconsiderRODRequestListenerTable reconsiderRequestDetails;
	
/*	@Inject
	private Instance<ReconsiderRODRequestTable> objReconsiderRequestDetails;*/
	
	/*@Inject
	private Instance<DocumentCheckListTable> documentCheckListObj;*/
	
	//@Inject
	//private DocumentCheckListTable documentCheckList;
	
	//private DocumentCheckListValidationListenerTable documentCheckList;
	
	@Inject
	private Instance<DocumentCheckListValidationListenerTable> documentCheckListValidationObj;
	
	private DocumentCheckListValidationListenerTable documentCheckListValidation;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	private ComboBox cmbDocumentsReceivedFrom;
	
	private TextField txtAcknowledgementContactNo;
	
	////private static Window popup;
	
	private PopupDateField documentsReceivedDate;
	
	private TextField txtEmailId;
	
	private ComboBox cmbModeOfReceipt;
	
	private ComboBox cmbReconsiderationRequest;
	
	//private TextField txtAdditionalRemarks;
	private TextArea txtAdditionalRemarks;
	
	private CheckBox chkhospitalization;
	
	private CheckBox chkPreHospitalization;
	
	private CheckBox chkPostHospitalization;
	
	private CheckBox chkPartialHospitalization;
	
	private CheckBox chkHospitalizationRepeat;
	
	private CheckBox chkLumpSumAmount;
	
	private CheckBox chkAddOnBenefitsHospitalCash;
	
	private CheckBox chkAddOnBenefitsPatientCare;
	
	private CheckBox chkOtherBenefits;
	
	private CheckBox chkEmergencyMedicalEvaluation;
	
	private CheckBox chkCompassionateTravel;
	
	private CheckBox chkRepatriationOfMortalRemains;
	
	private CheckBox chkPreferredNetworkHospital;
	
	private CheckBox chkSharedAccomodation;
	
	
	private VerticalLayout documentDetailsPageLayout;

	//private GWizard wizard;
	
	private FormLayout detailsLayout1;
	
	private VerticalLayout reconsiderationLayout;
	
	private VerticalLayout otherBenefitsLayout;
	
	private BeanItemContainer<SelectValue> reconsiderationRequest;
	
	private BeanItemContainer<SelectValue> reasonForReconsiderationRequest;
	 
	private BeanItemContainer<SelectValue> modeOfReceipt;
	 
	private BeanItemContainer<SelectValue> docReceivedFromRequest ;
	 
	private TextField txtHospitalizationClaimedAmt;
	 
	private TextField txtPreHospitalizationClaimedAmt;
	
	private TextField txtPostHospitalizationClaimedAmt;
	
	private TextField txtOtherBenefitClaimedAmnt;
	
	private CheckBox chkHospitalCash;
	
	private TextField txtHospitalCashClaimedAmnt;
	 
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	//private  DocumentDetailsDTO docDTO ;
	private List<DocumentDetailsDTO> docDTO;
	
	private static boolean isQueryReplyReceived = false;
	//public static boolean isQueryReplyReceived = false;
	
	private ComboBox cmbReasonForReconsideration;
	
	@Inject
	private ViewDetails viewDetails;
	
	private List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList;
	
	public Map<String,Boolean> reconsiderationMap = new WeakHashMap<String, Boolean>();
	
	
	public String hospitalizationClaimedAmt = "";
	
	public String preHospitalizationAmt = "";
	
	public String postHospitalizationAmt= "";
	
	public String otherBenefitsAmnt = "";
	
	public ReconsiderRODRequestTableDTO reconsiderDTO = null;
	

 	private String strDocRecFrom = "";
 	
 	private String strModeOfReceipt = null;
 	
 	/*
 	 * Added for removing hospitalization validation.
 	 * */
 	
 	private Boolean isFinalEnhancement = false;
 	
 	private OptionGroup optPaymentCancellation;
 	
 	public HorizontalLayout hLayout = null;
 	
	private final Logger log = LoggerFactory.getLogger(DocumentDetailsPage.class);
	
	public Boolean isNext = false;
	
	private Boolean lumpSumValidationFlag = false;

	private OptionGroup optDocumentVerified;
	
	@Inject 
	private Instance<PedRaisedDetailsTable> pedRaiseDetailsTable;
	
	private PedRaisedDetailsTable pedRaiseDetailsTableObj;
	
	//private static boolean isReconsiderRequestFlag = false;
	 
//	List<DocumentCheckListDTO> mainDocList = new ArrayList<DocumentCheckListDTO>();
	@EJB
	private MasterService masterService;
	 
	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	
//	private FormLayout legaHeirLayout;
	
	private TextField legalHeirNameTxt;

	private TextArea legalHeirAddressTxt;
	
	private VerticalLayout legalHeirLayout;
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;
	
	private LegalHeirDetails legalHeirDetails;
	
	private BeanItemContainer<SelectValue> relationshipContainer;
	
	@EJB
	private CreateRODService createRodService;
	
	private BeanItemContainer<SelectValue> hospmodeOfReceipt;
	private BeanItemContainer<SelectValue> insmodeOfReceipt;
	
	@PostConstruct
	public void init() {
		
		

	}
	
	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<DocumentDetailsDTO>(
				DocumentDetailsDTO.class);
		this.binder.setItemDataSource(this.bean
				.getDocumentDetails());
		//this.binder.setItemDataSource(new DocumentDetailsDTO());
	}
	
	public Boolean alertMessageForPED() {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PED_RAISE_MESSAGE + "</b>",
				ContentMode.HTML);*/
   		//final Boolean isClicked = false;
		/*Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/
		Label successLabel = new Label(SHAConstants.PED_RAISE_MESSAGE);
		pedRaiseDetailsTableObj = pedRaiseDetailsTable.get();
		pedRaiseDetailsTableObj.init("", false, false);
		pedRaiseDetailsTableObj.initViewAck(bean.getNewIntimationDTO().getPolicy().getKey());
		
		VerticalLayout layout = new VerticalLayout(successLabel, pedRaiseDetailsTableObj.getTable());
		//layout.setComponentAlignment( Alignment.MIDDLE_CENTER);
		/*layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");*/
		/*HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");*/

		/*final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				bean.setIsPEDInitiated(false);
				/*if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}*/
			}
		});
		return true;
	}
	
	public Component getContent() {
//		if(bean.getClaimCount() > 1){
//			alertMessageForClaimCount(this.bean.getClaimCount());
//		}else 
		
		
		reconsiderDTO = null;
		
		//IMSSUPPOR-23207
		isQueryReplyReceived = Boolean.FALSE;
		
		//GLX2020168
		/*Date policyFromDate = bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
		Date admissionDate = this.bean.getPreauthDTO().getPreauthDataExtractionDetails().getAdmissionDate();
		Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
		Date currnetDate = new Date();
		boolean dateRange= SHAUtils.validateRangeDate(policyFromDate);
		boolean currentDateRange= SHAUtils.validateRangeDate(currnetDate);
		boolean validation= createRodService.getDiagnosisICDValidtion(bean.getNewIntimationDTO().getKey());
		
		if(validation && dateRange && currentDateRange && ReferenceTable.getCovidProducts().contains(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) && (diffDays != null && diffDays < 30)){
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox(SHAConstants.COVID_WAITING_PERIOD_15DAYS_ALERT_MSG, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
		}*/
	    
		if(SHAConstants.YES_FLAG.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredDeceasedFlag())) {
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
		if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
			getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
		}
		else if(bean.getPreauthDTO().getIsPolicyValidate()){		
			policyValidationPopupMessage();
		}
		else if(bean.getIsPEDInitiated()) {
			alertMessageForPED();
		  }/*else if(bean.getIsSuspicious()!=null){
			  StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
			}*/
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		//reconsiderationMap = new HashMap<String, Boolean>();

		documentDetailsPageLayout = new VerticalLayout();
		documentDetailsPageLayout.setSpacing(false);
		documentDetailsPageLayout.setMargin(false);
		
		reconsiderationLayout = new VerticalLayout();
		reconsiderationLayout.setSpacing(false);
		
		otherBenefitsLayout = new VerticalLayout();	
		
		/*//Vaadin8-setImmediate() documentDetailsPageLayout.setImmediate(false);
		documentDetailsPageLayout.setWidth("100.0%");
		documentDetailsPageLayout.setMargin(false);*/
		
		
		
		HorizontalLayout documentDetailsLayout = buildDocumentDetailsLayout(); 
		documentDetailsLayout.setCaption("Document Details");
//		documentDetailsLayout.setWidth("90%");
		
		

		
		HorizontalLayout remarksLayout = new HorizontalLayout(new FormLayout(txtAdditionalRemarks));
		//remarksLayout.setMargin(true);
		remarksLayout.setSpacing(true);
		
		List<DocumentCheckListDTO> dtoList = new ArrayList<DocumentCheckListDTO>();
		dtoList.addAll(this.bean.getDocumentDetails().getDocumentCheckList());
		//documentCheckList = documentCheckListObj.get();
		//documentCheckList.init("", false);
		
		 
		
		documentCheckListValidation = documentCheckListValidationObj.get();
		documentCheckListValidation.initPresenter(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
		documentCheckListValidation.init();
		
		
		//documentCheckList.setReference(this.referenceData);
		
		this.bean.getDocumentDetails().setDocumentCheckList(dtoList);
		//getDocumentTableDataList();
		
		
		Map<String, Object> referenceData = new WeakHashMap<String, Object>();
		referenceData.put("sectionDetails", this.bean.getSectionList());
		
		PreauthDTO preauthDto = new PreauthDTO();
		preauthDto.setNewIntimationDTO(this.bean.getNewIntimationDTO());
		preauthDto.setClaimDTO(this.bean.getClaimDTO());
		preauthDto.setShouldDisableSection(this.bean.getShouldDisableSection());
		this.sectionDetailsListenerTableObj = sectionDetailsListenerTable.get();
		this.sectionDetailsListenerTableObj.init(preauthDto, SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
		sectionDetailsListenerTableObj.setReferenceData(referenceData);
		SectionDetailsTableDTO sectionDTO = new SectionDetailsTableDTO();
		SelectValue correctSelectValue = new SelectValue();
		if(null != bean.getClaimDTO().getNewIntimationDto().getPolicy() && null != bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() 
				&& !(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
				&& !((ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()) 
						|| ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())) 
						&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))
						&& !(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().equals(ReferenceTable.RAKSHAK_CORONA_PRODUCT_KEY))){
			correctSelectValue = StarCommonUtils.getCorrectSelectValue(this.bean.getSectionList().getItemIds(), this.bean.getClaimDTO().getClaimSectionCode() != null ? this.bean.getClaimDTO().getClaimSectionCode() : ReferenceTable.HOSPITALIZATION_SECTION_CODE);
		}
		else{
			correctSelectValue = StarCommonUtils.getCorrectSelectValue(this.bean.getSectionList().getItemIds(), this.bean.getClaimDTO().getClaimSectionCode() != null ? this.bean.getClaimDTO().getClaimSectionCode() : ReferenceTable.LUMPSUM_SECTION_CODE);
		}
		sectionDTO.setSection(correctSelectValue);
		bean.getDocumentDetails().setSectionDetailsDTO(sectionDTO);
		this.sectionDetailsListenerTableObj.addBeanToList(bean.getDocumentDetails().getSectionDetailsDTO());
		
		
		
		VerticalLayout docCheckListLayout = new VerticalLayout(documentCheckListValidation);
		docCheckListLayout.setCaption("Document Checklist");
		docCheckListLayout.setHeight("100%");
		
		 /*hLayout = buildQueryDetailsLayout();*/
		
		documentDetailsPageLayout = new VerticalLayout(documentDetailsLayout,sectionDetailsListenerTableObj,reconsiderationLayout,docCheckListLayout,remarksLayout);
		
		buildNomineeLayout();
		nomineeDetailsTable.setVisible(false);
		legalHeirLayout.setVisible(false);
		/*if(legaHeirLayout.getComponentCount() == 2) {
			Iterator<Component> componentIterator = legaHeirLayout.getComponentIterator();
			while(componentIterator.hasNext()) {
				(componentIterator.next()).setVisible(false);
			}
			legaHeirLayout.setVisible(false);
		}*/
		
		//fireViewEvent(DocumentDetailsPresenter.SETUP_DROPDOWN_VALUES, null);
		
		addListener();
		setTableValues();
		showOrHideValidation(false);
		
		if(("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && null != chkLumpSumAmount)
		{
			chkLumpSumAmount.setEnabled(false);
		}
	
		return documentDetailsPageLayout;
	}
	

			/*private void alertMessageForClaimCount(Long claimCount){
		
		String msg = SHAConstants.CLAIM_COUNT_MESSAGE+claimCount;
		
		
   		Label successLabel = new Label(
				"<b style = 'color: black;'>"+msg+"</b>",
				ContentMode.HTML);
//   		successLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//   		successLabel.addStyleName(ValoTheme.LABEL_BOLD);
   		successLabel.addStyleName(ValoTheme.LABEL_H3);
   		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
   		FormLayout firstForm = new FormLayout(successLabel,homeButton);
		Panel panel = new Panel(firstForm);
		
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
			panel.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			panel.addStyleName("girdBorder2");
		}
		
		panel.setHeight("103px");
//		panel.setSizeFull();
		
		
		popup = new com.vaadin.ui.Window();
		popup.setWidth("30%");
		popup.setHeight("20%");
//		popup.setContent( viewDocumentDetailsPage);
		popup.setContent(panel);
		popup.setClosable(true);
		
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				popup.close();
				
				if(bean.getIsPEDInitiated()) {
					alertMessageForPED();
				}

			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}*/
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	public void resetPage()
	{	
		if(null != documentDetailsPageLayout )
		{
		Iterator<Component> componentIterator = documentDetailsPageLayout.iterator();
			while(componentIterator.hasNext()) 
			{
				Component component = componentIterator.next() ;
				if(component instanceof  HorizontalLayout)
				{	
					HorizontalLayout hLayout = (HorizontalLayout)component;
					Iterator<Component> subCompents = hLayout.iterator();
					while (subCompents.hasNext())
					{
						Component indivdualComp = subCompents.next();
						if(indivdualComp instanceof FormLayout)
						{
							FormLayout fLayout = (FormLayout)indivdualComp;
							Iterator<Component> subComp = fLayout.iterator();
							while(subComp.hasNext())
							{
								Component individualComp = subComp.next();
								if(individualComp instanceof TextField) 
								{
									TextField field = (TextField) individualComp;
									if(field.isReadOnly())
									{
										field.setReadOnly(false);
										field.setValue("");
										field.setReadOnly(true);
									}
									else
									{
										field.setValue("");
									}
								} 
								else if(individualComp instanceof ComboBox)
								{
									ComboBox field = (ComboBox) individualComp;
									field.setValue(null);
								}
								else if(individualComp instanceof CheckBox)
								{
									CheckBox field = (CheckBox) individualComp;
									field.setValue(false);
								}
								else if(individualComp instanceof DateField)
								{
									DateField field = (DateField) individualComp;
									field.setValue(new Date());
									//field.setValue(null);
								}
								else if(individualComp instanceof RODQueryDetailsTable)
								{
									RODQueryDetailsTable field = (RODQueryDetailsTable) individualComp;
									field.removeRow();
								}
								else if(individualComp instanceof ViewQueryDetailsTable)
								{
									ViewQueryDetailsTable field = (ViewQueryDetailsTable) individualComp;
									field.removeRow();
								}
								else if(indivdualComp instanceof DocumentCheckListTable)
								{
									DocumentCheckListTable field = (DocumentCheckListTable) individualComp;
									field.removeRow();
								}
							}
						}
					}
				}
				else if(component instanceof  VerticalLayout)
				{	
					VerticalLayout vLayout = (VerticalLayout)component;
					Iterator<Component> subCompents = vLayout.iterator();
					while (subCompents.hasNext())
					{
						Component indivdualComp = subCompents.next();
						if(indivdualComp instanceof HorizontalLayout)
						{
							HorizontalLayout hLayout = (HorizontalLayout) indivdualComp;
							Iterator<Component> verticalSubCompents = hLayout.iterator();
							while (verticalSubCompents.hasNext())
							{
								Component indivdualVComp = verticalSubCompents.next();
								if(indivdualVComp instanceof FormLayout)
								{
									FormLayout fLayout = (FormLayout)indivdualVComp;
									Iterator<Component> subComp = fLayout.iterator();
									while(subComp.hasNext())
									{
										Component individualComp = subComp.next();
										if(individualComp instanceof TextField) 
										{
											TextField field = (TextField) individualComp;
											if(field.isReadOnly())
											{
												field.setReadOnly(false);
												field.setValue("");
												field.setReadOnly(true);
											}
											else
											{
												field.setValue("");
											}
										} 
										else if(individualComp instanceof ComboBox)
										{
											ComboBox field = (ComboBox) individualComp;
											field.setValue(null);
										}
										else if(individualComp instanceof CheckBox)
										{
											CheckBox field = (CheckBox) individualComp;
											field.setValue(false);
										}
										else if(individualComp instanceof DateField)
										{
											DateField field = (DateField) individualComp;
											field.setValue(new Date());
											//field.setValue(null);
										}
										else if(individualComp instanceof RODQueryDetailsTable)
										{
											RODQueryDetailsTable field = (RODQueryDetailsTable) individualComp;
											field.removeRow();
										}
										else if(individualComp instanceof ViewQueryDetailsTable)
										{
											ViewQueryDetailsTable field = (ViewQueryDetailsTable) individualComp;
											field.removeRow();
										}
										/*else if(indivdualVComp instanceof DocumentCheckListTable)
										{
											DocumentCheckListTable field = (DocumentCheckListTable) individualComp;
											field.removeRow();
										}*/
									}
								}
							}
							
						}
						else if(indivdualComp instanceof DocumentCheckListTable)
						{
							DocumentCheckListTable field = (DocumentCheckListTable) indivdualComp;
							field.removeRow();
						}
						/*if(indivdualComp instanceof CheckBox) 
						{
							CheckBox field = (CheckBox) indivdualComp;
							field.setValue(false);
						} 
						else if(indivdualComp instanceof ReconsiderRODRequestTable)
						{
							ReconsiderRODRequestTable field = (ReconsiderRODRequestTable) indivdualComp;
							field.removeRow();
						}*/
					}
				}
			}
		}
	}
	
	/*private void getDocumentTableDataList()
	{
		fireViewEvent(DocumentDetailsPresenter.SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES, null);
	}*/

	
	private HorizontalLayout buildDocumentDetailsLayout()
	{
		cmbDocumentsReceivedFrom = binder.buildAndBind("Documents Recieved From" , "documentsReceivedFrom" , ComboBox.class);
		//Vaadin8-setImmediate() cmbDocumentsReceivedFrom.setImmediate(true);
		
		
		txtAcknowledgementContactNo = binder.buildAndBind("Acknowledgement\nContact Number\n"+ "(Docs Submitted Person)","acknowledgmentContactNumber", TextField.class);
		txtAcknowledgementContactNo.setNullRepresentation("");
		txtAcknowledgementContactNo.setEnabled(false);
		txtAcknowledgementContactNo.setReadOnly(true);
		txtAcknowledgementContactNo.setMaxLength(15);
//		txtAcknowledgementContactNo.addStyleName("wrap");
		CSValidator validator = new CSValidator();
		validator.extend(txtAcknowledgementContactNo);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
		
		documentsReceivedDate = binder.buildAndBind("Documents Recieved Date", "documentsReceivedDate", PopupDateField.class);
		documentsReceivedDate.setValue(new Date());
		documentsReceivedDate.setTextFieldEnabled(false);
		
		
		cmbModeOfReceipt = binder.buildAndBind("Mode Of Receipt", "modeOfReceipt", ComboBox.class);
		txtEmailId = binder.buildAndBind("Email Id", "emailId", TextField.class);
		txtEmailId.setNullRepresentation("");
		txtEmailId.setEnabled(false);
		txtEmailId.setReadOnly(true);
		txtEmailId.setMaxLength(50);
		
		
		optDocumentVerified = (OptionGroup) binder.buildAndBind("Verified and entered the\namount claimed based on the\noriginal bills received" , "documentVerification" , OptionGroup.class);
		optDocumentVerifiedListener();
	//	optDocumentVerified.setRequired(true);
		optDocumentVerified.addItems(getReadioButtonOptions());
		optDocumentVerified.setItemCaption(true, "Yes");
		optDocumentVerified.setItemCaption(false, "No");
		optDocumentVerified.setStyleName("horizontal");
		//Vaadin8-setImmediate() optDocumentVerified.setImmediate(true);
		optDocumentVerified.setEnabled(false);
		
		
		
		txtHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed\n(Hospitalisation)" , "hospitalizationClaimedAmount",TextField.class);
	//	txtHospitalizationClaimedAmt.addBlurListener(getHospitalLisenter());
		txtPreHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed(Pre-Hosp)", "preHospitalizationClaimedAmount", TextField.class);
	//	txtPreHospitalizationClaimedAmt.addBlurListener(getPreHospLisenter());
		txtPostHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed (Post-Hosp)", "postHospitalizationClaimedAmount",TextField.class);
	//	txtPostHospitalizationClaimedAmt.addBlurListener(getPostHospLisenter());
		txtOtherBenefitClaimedAmnt = binder.buildAndBind("Amount Claimed (Other Benefits)", "otherBenefitclaimedAmount",TextField.class);
	//	txtOtherBenefitClaimedAmnt.addBlurListener(getOtherBenefitLisener());
		txtHospitalCashClaimedAmnt = binder.buildAndBind("Amount Claimed (Hospital Cash)", "txtHospitalCashClaimedAmnt",TextField.class);
		
		txtHospitalizationClaimedAmt.setEnabled(false);
		txtHospitalizationClaimedAmt.setNullRepresentation("");
		txtHospitalizationClaimedAmt.setMaxLength(15);
		
		txtPreHospitalizationClaimedAmt.setEnabled(false);
		txtPreHospitalizationClaimedAmt.setNullRepresentation("");
		txtPreHospitalizationClaimedAmt.setMaxLength(15);
		
		txtPostHospitalizationClaimedAmt.setEnabled(false);
		txtPostHospitalizationClaimedAmt.setNullRepresentation("");
		txtPostHospitalizationClaimedAmt.setMaxLength(15);
		
		txtOtherBenefitClaimedAmnt.setEnabled(false);
		txtOtherBenefitClaimedAmnt.setNullRepresentation("");
		txtOtherBenefitClaimedAmnt.setMaxLength(15);
		
		txtHospitalCashClaimedAmnt.setEnabled(false);
		txtHospitalCashClaimedAmnt.setNullRepresentation("");
		txtHospitalCashClaimedAmnt.setMaxLength(15);
		
		CSValidator hospClaimedAmtValidator = new CSValidator();
		hospClaimedAmtValidator.extend(txtHospitalizationClaimedAmt);
		hospClaimedAmtValidator.setRegExp("^[0-9.]*$");
		hospClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator preHospClaimedAmtValidator = new CSValidator();
		preHospClaimedAmtValidator.extend(txtPreHospitalizationClaimedAmt);
		preHospClaimedAmtValidator.setRegExp("^[0-9.]*$");
		preHospClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator postHospClaimedAmtValidator = new CSValidator();
		postHospClaimedAmtValidator.extend(txtPostHospitalizationClaimedAmt);
		postHospClaimedAmtValidator.setRegExp("^[0-9.]*$");
		postHospClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator otherBenefitClaimedAmtValidator = new CSValidator();
		otherBenefitClaimedAmtValidator.extend(txtOtherBenefitClaimedAmnt);
		otherBenefitClaimedAmtValidator.setRegExp("^[0-9.]*$");
		otherBenefitClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator hospitalCashClaimedAmtValidator = new CSValidator();
		hospitalCashClaimedAmtValidator.extend(txtHospitalCashClaimedAmnt);
		hospitalCashClaimedAmtValidator.setRegExp("^[0-9.]*$");
		hospitalCashClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator emailValidator = new CSValidator();
		emailValidator.extend(txtEmailId);
		emailValidator.setRegExp("^[a-zA-Z 0-9 @ . _]*$");
		emailValidator.setPreventInvalidTyping(true);
		
		cmbReconsiderationRequest = binder.buildAndBind("Reconsideration Request", "reconsiderationRequest", ComboBox.class);
		//Vaadin8-setImmediate() cmbReconsiderationRequest.setImmediate(true);
		
		/**
		 * The below combo box will be displayed only if the reconsideration request is set to Yes. Therefore , the below
		 * box will be part of 
		 * */
		cmbReasonForReconsideration = binder.buildAndBind("Reason for Reconsideration" , "reasonForReconsideration" , ComboBox.class);
		cmbReasonForReconsideration.setWidth("300px");
		loadReasonForReconsiderationDropDown();
			
		txtAdditionalRemarks = binder.buildAndBind("Additional Remarks","additionalRemarks",TextArea.class);
		txtAdditionalRemarks.setMaxLength(100);
		txtAdditionalRemarks.setWidth("400px");
		
		/*CSValidator remarksValidator = new CSValidator();
		remarksValidator.extend(txtAdditionalRemarks);
		remarksValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		//remarksValidator.setRegExp("^[a-zA-Z ]*$");
		remarksValidator.setPreventInvalidTyping(true);*/
		
		detailsLayout1 = new FormLayout(cmbDocumentsReceivedFrom,documentsReceivedDate,
				cmbModeOfReceipt,cmbReconsiderationRequest/*,optDocumentVerified*/);
		FormLayout detailsLayout2 = new FormLayout(txtAcknowledgementContactNo,txtEmailId/*,txtHospitalizationClaimedAmt,txtPreHospitalizationClaimedAmt,txtPostHospitalizationClaimedAmt,txtOtherBenefitClaimedAmnt*/);
		//HorizontalLayout docDetailsLayout = new HorizontalLayout(detailsLayout1,new FormLayout(),new FormLayout(),detailsLayout2);
		HorizontalLayout docDetailsLayout = new HorizontalLayout(detailsLayout1,detailsLayout2);
		//docDetailsLayout.setComponentAlignment(detailsLayout2, Alignment.MIDDLE_LEFT);
		docDetailsLayout.setMargin(false);
		docDetailsLayout.setSpacing(true);
		
		setRequiredAndValidation(cmbDocumentsReceivedFrom);
		setRequiredAndValidation(documentsReceivedDate);
		setRequiredAndValidation(cmbModeOfReceipt);
		
		mandatoryFields.add(cmbDocumentsReceivedFrom);
		mandatoryFields.add(documentsReceivedDate);
		mandatoryFields.add(cmbModeOfReceipt);
		//docDetailsLayout.setCaption("Document Details");
		
		return docDetailsLayout;
	}
	
	private HorizontalLayout buildBillClassificationLayout() {
		
		
		chkhospitalization = binder.buildAndBind("Hospitalisation", "hospitalization", CheckBox.class);
		
		chkPreHospitalization = binder.buildAndBind("Pre-Hospitalisation", "preHospitalization", CheckBox.class);
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("preHospitalizationFlag")))
		{
			chkPreHospitalization.setEnabled(false);
		}
		
		
		
		chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalization", CheckBox.class);
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("postHospitalizationFlag")))
		{
			chkPostHospitalization.setEnabled(false);
		}
		
		
		chkPartialHospitalization = binder.buildAndBind("Partial-Hospitalisation", "partialHospitalization", CheckBox.class);
		
		chkHospitalizationRepeat = binder.buildAndBind("Hospitalisation (Repeat)", "hospitalizationRepeat", CheckBox.class);
		
		chkLumpSumAmount = binder.buildAndBind("Lumpsum Amount", "lumpSumAmount", CheckBox.class);
		
		chkOtherBenefits = binder.buildAndBind("Other Benefits", "otherBenefits", CheckBox.class);
		
		chkHospitalCash = binder.buildAndBind("Hospital Cash", "hospitalCash", CheckBox.class);
		
		chkOtherBenefits.setValue(false);
		chkHospitalCash.setEnabled(false);
		//Vaadin8-setImmediate() chkOtherBenefits.setImmediate(true);
		
		
		
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("LumpSumFlag")))
		{
			chkLumpSumAmount.setEnabled(false);
		}
		
		//Need to comment the below line. Aded for testing.
		//chkLumpSumAmount.setEnabled(true);
		
		chkAddOnBenefitsHospitalCash = binder.buildAndBind("Add on Benefits (Hospital cash)", "addOnBenefitsHospitalCash", CheckBox.class);
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("hospitalCashFlag")))
		{
			chkAddOnBenefitsHospitalCash.setEnabled(false);
		}
		
		
		chkAddOnBenefitsPatientCare = binder.buildAndBind("Add on Benefits (Patient Care)", "addOnBenefitsPatientCare", CheckBox.class);
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("PatientCareFlag")))
		{
			chkAddOnBenefitsPatientCare.setEnabled(false);
		}
		
		
		chkhospitalization.setEnabled(true);
		//chkPreHospitalization.setEnabled(false);
		//chkPostHospitalization.setEnabled(false);
		//chkPartialHospitalization.setEnabled(false);
		//System.out.println("---the claimType value---"+this.bean.getClaimDTO().getClaimTypeValue());
		/*if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			chkPartialHospitalization.setEnabled(true);
		}
		else
		{
			chkhospitalization.setEnabled(true);
		}*/
		
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get(SHAConstants.OTHER_BENEFITS_FLAG)))
		{
			if(null != chkOtherBenefits)
			{
			chkOtherBenefits.setEnabled(false);
			}
		}
		
		
		if(("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			chkPartialHospitalization.setEnabled(false);
		}
		
		/*Below commented bcoz below code only for doc received from Insured only
		if(chkPreHospitalization != null) {
			if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL)
						|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER)){
					chkPreHospitalization.setEnabled(true);
			}
		}*/
		
		FormLayout classificationLayout1 = new FormLayout(chkhospitalization,chkHospitalizationRepeat);
		FormLayout classificationLayout2 = new FormLayout(chkPreHospitalization,chkLumpSumAmount);
		FormLayout classificationLayout3 = new FormLayout(chkPostHospitalization,chkAddOnBenefitsHospitalCash);
		FormLayout classificationLayout4 = new FormLayout(chkPartialHospitalization,chkAddOnBenefitsPatientCare);
		FormLayout classificationLayout5 = new FormLayout(chkOtherBenefits,chkHospitalCash);
		
		HorizontalLayout billClassificationLayout = new HorizontalLayout(classificationLayout1,classificationLayout2,classificationLayout3,classificationLayout4,classificationLayout5);
		//billClassificationLayout.setCaption("Document Details");
		billClassificationLayout.setCaption("Bill Classification");
		billClassificationLayout.setSpacing(false);
		billClassificationLayout.setMargin(false);
//		billClassificationLayout.setWidth("100%");
		
		addBillClassificationLister();
		if(null != this.bean.getDocumentDetails().getOtherBenefitsFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(this.bean.getDocumentDetails().getOtherBenefitsFlag()))
		{
			if(null != chkOtherBenefits){
			chkOtherBenefits.setValue(true);
			}
		}
		//GLX2020017
		if(null != this.bean.getClaimDTO() && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_INDIVIDUAL_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			||	null != this.bean.getClaimDTO() && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_FLOATER_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			||	null != this.bean.getClaimDTO() && ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			||	null != this.bean.getClaimDTO() && ReferenceTable.GROUP_TOPUP_PROD_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
		{
			if(chkOtherBenefits != null){
				chkOtherBenefits.setEnabled(false);
			}
			if(chkAddOnBenefitsHospitalCash !=null){
				chkAddOnBenefitsHospitalCash.setEnabled(false);
			}
		}
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.STAR_MICRO_RORAL_AND_FARMERS_CARE)) {
			chkAddOnBenefitsHospitalCash.setEnabled(false);
			chkAddOnBenefitsPatientCare.setEnabled(false);
			chkOtherBenefits.setEnabled(false);
			
		}
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)) {
			chkAddOnBenefitsPatientCare.setEnabled(false);
			
		}
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
			if(null != chkhospitalization) {
				chkhospitalization.setEnabled(false); }
				if(null != chkHospitalizationRepeat) {
				chkHospitalizationRepeat.setEnabled(false); }
				if(null != chkPreHospitalization) {
				chkPreHospitalization.setEnabled(false); }
				if(null != chkLumpSumAmount) {
				chkLumpSumAmount.setEnabled(false); }
				if(null != chkPostHospitalization) {
				chkPostHospitalization.setEnabled(false); }
				if(null != chkPartialHospitalization) {
				chkPartialHospitalization.setEnabled(false); }
				if(null != chkAddOnBenefitsHospitalCash) {
				chkAddOnBenefitsHospitalCash.setEnabled(false); }
				if(null != chkAddOnBenefitsPatientCare) {
				chkAddOnBenefitsPatientCare.setEnabled(false); }
				if(null != chkOtherBenefits) {
				chkOtherBenefits.setEnabled(false); }
				if(null != chkHospitalCash) {
				chkHospitalCash.setEnabled(true); 
				cmbReconsiderationRequest.setEnabled(false);
				}
			
		}
				
		return billClassificationLayout;
	}
	
	
	//private RODQueryDetailsTable buildQueryDetailsLayout()
	private HorizontalLayout buildQueryDetailsLayout()
	{
		//rodQueryDetails = rodQueryDetailsObj.get();
		rodQueryDetails.initpresenterString(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
		rodQueryDetails.init("", false, false,reconsiderationRequest);
//		rodQueryDetails.setReference(this.referenceData);
		loadQueryDetailsTableValues();
		//rodQueryDetails.setEditable(true);
			//rodQueryDetails.setTableList(rodQueryDetailsList);
			/*for (RODQueryDetailsDTO rodQueryDetailsDTO : rodQueryDetailsList) {
				rodQueryDetails.addBeanToList(rodQueryDetailsDTO);
			}*/
			
		
		/**/
		
		rodQueryDetails.setEnabled(true);
		
		HorizontalLayout queryDetailsLayout = new HorizontalLayout(rodQueryDetails);
		queryDetailsLayout.setWidth("100%");
		queryDetailsLayout.setCaption("View Query Details");
		queryDetailsLayout.setSpacing(true);
		queryDetailsLayout.setMargin(true);
		
		//return rodQueryDetails;
		return queryDetailsLayout;
		
	}
	
	private void buildOtherBenefitsLayout(Boolean value)
	{
		if(value)
		{
			otherBenefitsLayout.removeAllComponents();
			List<Field<?>> listOfOtherBenefitsChkBox = getListOfOtherBenefitsChkBox();
			unbindField(listOfOtherBenefitsChkBox);
			
			chkEmergencyMedicalEvaluation = binder.buildAndBind("Emergency Medical Evacuation", "emergencyMedicalEvaluation", CheckBox.class);
			
			chkCompassionateTravel = binder.buildAndBind("Compassionate Travel", "compassionateTravel", CheckBox.class);
			
			chkRepatriationOfMortalRemains = binder.buildAndBind("Repatriation Of Mortal Remains", "repatriationOfMortalRemains", CheckBox.class);
			
			chkPreferredNetworkHospital = binder.buildAndBind("Preferred Network Hospital", "preferredNetworkHospital", CheckBox.class);
			
			if(null != this.bean.getClaimDTO() && ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				chkPreferredNetworkHospital.setCaption("Valuable Service Provider (Hospital)");
			}
			
			chkSharedAccomodation = binder.buildAndBind("Shared Accomodation", "sharedAccomodation", CheckBox.class);
			
			FormLayout otherBenefitsLayout1 = new FormLayout(chkEmergencyMedicalEvaluation,chkPreferredNetworkHospital);
			FormLayout otherBenefitsLayout2 = new FormLayout(chkCompassionateTravel,chkSharedAccomodation);
			FormLayout otherBenefitsLayout3 = new FormLayout(chkRepatriationOfMortalRemains);	
			
			if(null != this.bean.getClaimDTO() && ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				chkEmergencyMedicalEvaluation.setVisible(false);
				chkSharedAccomodation.setVisible(false);
				chkRepatriationOfMortalRemains.setVisible(false);
			}
			
			HorizontalLayout otherBenefitsLayput = new HorizontalLayout();
			
			otherBenefitsLayput.addComponents(otherBenefitsLayout1,otherBenefitsLayout2,otherBenefitsLayout3);
			
			if(null != this.bean.getClaimDTO().getClaimType() && (SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
			{
				if( null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docReceivedFrom = (SelectValue) cmbDocumentsReceivedFrom.getValue();
					
					if(null != docReceivedFrom && null != docReceivedFrom.getId() && (ReferenceTable.RECEIVED_FROM_HOSPITAL).equals(docReceivedFrom.getId()))
					{
						otherBenefitsLayput.removeAllComponents();
						otherBenefitsLayput.addComponents(chkEmergencyMedicalEvaluation,chkRepatriationOfMortalRemains);
					}	
				}
			}
			otherBenefitsLayput.setSpacing(true);
			otherBenefitsLayput.setMargin(true);

			if(ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())*/
						(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
							&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
									SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
									|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
									|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
					|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
				
				chkEmergencyMedicalEvaluation.setVisible(false);
				chkCompassionateTravel.setVisible(false);
				chkRepatriationOfMortalRemains.setVisible(false);
				chkPreferredNetworkHospital.setVisible(false);
			}
			else if(ReferenceTable.POS_FAMILY_HEALTH_OPTIMA.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				chkCompassionateTravel.setVisible(false);
			}
			
			otherBenefitsLayout.addComponent(otherBenefitsLayput);
			
		}else {
			List<Field<?>> listOfOtherBenefitsChkBox = getListOfOtherBenefitsChkBox();
			unbindField(listOfOtherBenefitsChkBox);
			otherBenefitsLayout.removeAllComponents();
		}
		
		//return otherBenefitsLayput;
		
	}
	
	private void loadQueryDetailsTableValues()
	{
		if(null != rodQueryDetails)
		{
			List<RODQueryDetailsDTO> rodQueryDetailsList = this.bean.getRodQueryDetailsList();
			if(null != rodQueryDetailsList && !rodQueryDetailsList.isEmpty())
			{
				rodQueryDetails.removeRow();
				
				if(null != this.cmbDocumentsReceivedFrom && null != this.cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docRecFrom = (SelectValue)this.cmbDocumentsReceivedFrom.getValue();
					String docRecFromVal = docRecFrom.getValue();
					int serialNumber = 1;
					for (RODQueryDetailsDTO rodQueryDetailsDTO : rodQueryDetailsList) {
						
						if(rodQueryDetailsDTO.getDocReceivedFrom().equalsIgnoreCase(docRecFromVal))
						{
							rodQueryDetails.removeRow();
							rodQueryDetailsDTO.setSno(serialNumber);
							rodQueryDetails.addBeanToList(rodQueryDetailsDTO);
							serialNumber++;
							
						}
						else
						{
							if(null != rodQueryDetails)
							{
								//rodQueryDetails.removeRow();
							}
						}
					}					
				}
			}
		}
	}
	
	//private ReconsiderRODRequestTable buildReconsiderRequestLayout()
	private synchronized VerticalLayout buildReconsiderRequestLayout()
	//private HorizontalLayout buildReconsiderRequestLayout()
	{
		//reconsiderRequestDetails = objReconsiderRequestDetails.get();
		
		//List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList = this.bean.getReconsiderRodRequestList();
		this.reconsiderRODRequestList = this.bean.getReconsiderRodRequestList();
		//reconsiderRequestDetails.init("", false, false);
		reconsiderRequestDetails.initPresenter(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
		reconsiderRequestDetails.init();
		reconsiderRequestDetails.setCaption("Select Earlier Request to be Reconsidered");
		
		reconsiderRequestDetails.setViewDetailsObj(viewDetails);
		if(null != reconsiderRODRequestList && !reconsiderRODRequestList.isEmpty())
		{
			for (ReconsiderRODRequestTableDTO reconsiderList : reconsiderRODRequestList) {
				if(null != reconsiderationMap && !reconsiderationMap.isEmpty())
				{
					Boolean isSelect = reconsiderationMap.get(reconsiderList.getAcknowledgementNo());
					reconsiderList.setSelect(isSelect);
				}
				else
				{
					reconsiderList.setSelect(null);
				}
				
//				if(reconsiderList.getDocumentReceivedFrom() == null){
//					reconsiderRequestDetails.addBeanToList(reconsiderList);
//				}
				
				if(cmbDocumentsReceivedFrom != null && cmbDocumentsReceivedFrom.getValue() != null){
					SelectValue selected = (SelectValue)cmbDocumentsReceivedFrom.getValue();
					if(reconsiderList.getDocumentReceivedFrom() != null && selected.getValue().equalsIgnoreCase(reconsiderList.getDocumentReceivedFrom()))
					reconsiderRequestDetails.addBeanToList(reconsiderList);
				}
				//reconsiderList.setSelect(null);
				
			}
			//reconsiderRequestDetails.setTableList(reconsiderRODRequestList);
		}
		if(null != reconsiderationMap && !reconsiderationMap.isEmpty())
		{
			reconsiderationMap.clear();
		}
		
		optPaymentCancellation = (OptionGroup) binder.buildAndBind("Payment Cancellation Needed" , "paymentCancellationNeeded" , OptionGroup.class);
		//unbindField(optPaymentMode);
		optPaymentCancellationListener();
	//	//Vaadin8-setImmediate() optPaymentMode.setImmediate(true);
		optPaymentCancellation.setRequired(true);
		optPaymentCancellation.addItems(getReadioButtonOptions());
		optPaymentCancellation.setItemCaption(true, "Yes");
		optPaymentCancellation.setItemCaption(false, "No");
		optPaymentCancellation.setStyleName("horizontal");
		//optPaymentMode.select(true);
		//Vaadin8-setImmediate() optPaymentCancellation.setImmediate(true);
		
		//cmbReasonForReconsideration = binder.buildAndBind("Reason for Reconsideration" , "reasonForReconsideration" , ComboBox.class);
	//	loadReasonForReconsiderationDropDown();
		
//		detailsLayout1.addComponent(cmbReasonForReconsideration);
		VerticalLayout reconsiderRequestLayout = new VerticalLayout(cmbReasonForReconsideration,reconsiderRequestDetails, new FormLayout(optPaymentCancellation));
	//	VerticalLayout vLayout = new VerticalLayout(cmbReasonForReconsideration, reconsiderRequestDetails);
		//HorizontalLayout reconsiderRequestLayout = new HorizontalLayout(reconsiderRequestDetails);
		/*vLayout.setCaption("Select Earlier Request to be Reconsidered");
		vLayout.setSpacing(true);
		vLayout.setMargin(true);*/
		
		//reconsiderRequestLayout.setCaption("Select Earlier Request to be Reconsidered");
		reconsiderRequestLayout.setSpacing(true);
		reconsiderRequestLayout.setMargin(true);
		
		return reconsiderRequestLayout;
		//return vLayout;
		
	}
	
	private void optDocumentVerifiedListener()
	{
		optDocumentVerified.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Boolean value = (Boolean) event.getProperty().getValue();
				String message = null;
				if(null != value)
				{
				if(!value)
				{
					message = "Please verify the original bills before proceeding";
					showErrorMessage(message);
					bean.getDocumentDetails().setDocumentVerificationFlag(SHAConstants.N_FLAG);
					optDocumentVerified.setValue(null);
				}
				else{
					
					bean.getDocumentDetails().setDocumentVerificationFlag(SHAConstants.YES_FLAG);
				}
			}
			}

		});
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	private void addListener()
	{
		cmbReconsiderationRequest
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(null != value)
				{	
					if (reconsiderationLayout != null
							&& reconsiderationLayout.getComponentCount() > 0) {
						reconsiderationLayout.removeAllComponents();
					}
					if(("NO").equalsIgnoreCase(value.getValue()))
					{	
						//isReconsiderRequestFlag = false;
						unbindField(getListOfChkBox());
						reconsiderationLayout.addComponent(buildBillClassificationLayout());
						reconsiderationLayout.addComponent(otherBenefitsLayout);
						//reconsiderationLayout.addComponent(buildQueryDetailsLayout());
						if(null == hLayout)
						{
							 hLayout = buildQueryDetailsLayout();
						}
						reconsiderationLayout.addComponent(hLayout);
						if(null != cmbDocumentsReceivedFrom)
						{
							SelectValue selValue = (SelectValue)cmbDocumentsReceivedFrom.getValue();
							buildBillClassificationBasedOnDocumentReceivedFrom(selValue);
							
						}
						if(null != txtHospitalizationClaimedAmt)
						{
							txtHospitalizationClaimedAmt.setValue(null);
							txtHospitalizationClaimedAmt.setEnabled(false);
						}
						if(null != txtPreHospitalizationClaimedAmt)
						{
							txtPreHospitalizationClaimedAmt.setValue(null);
							txtPreHospitalizationClaimedAmt.setEnabled(false);
						}
						if(null != txtPostHospitalizationClaimedAmt)
						{
							txtPostHospitalizationClaimedAmt.setValue(null);
							txtPostHospitalizationClaimedAmt.setEnabled(false);
						}
						if(null != txtOtherBenefitClaimedAmnt)
						{
							txtOtherBenefitClaimedAmnt.setValue(null);
							txtOtherBenefitClaimedAmnt.setEnabled(false);
						}
						addBillClassificationLister();
						bean.setReconsiderRODdto(null);
						sectionDetailsListenerTableObj.enableDisable(true);
						if((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue())){
							sectionDetailsListenerTableObj.enableDisable(false);
						}
						wizard.getNextButton().setEnabled(true);
						
						
					}
					else
					{
						/*
						 * If reconsider request is Yes, then bill classification
						 * validation needs to be skipped. Hence , based on below
						 * validation flag, decision would be made to skip or
						 * to consider the validation.
						 * */
					//	isReconsiderRequestFlag = true;
						if(validateMisApproval()){
							alertMessageForRejectionReconsider();
						} else {

							sectionDetailsListenerTableObj.enableDisable(false);
							unbindField(getListOfChkBox());
							reconsiderationLayout.addComponent(buildReconsiderRequestLayout());
							
						}
					}
				}
				
			}
		});
		
		cmbModeOfReceipt.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(null != value)
				{
					validateFieldsBasedOnModeOfReceipt(value.getValue());
				}
			}
		});

		
		cmbDocumentsReceivedFrom.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(null != value)
				{
					buildBillClassificationBasedOnDocumentReceivedFrom(value);
					if(value != null && value.getValue().equalsIgnoreCase("Insured")){
						if(insmodeOfReceipt != null){
						cmbModeOfReceipt.setContainerDataSource(insmodeOfReceipt);
						 cmbModeOfReceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						 cmbModeOfReceipt.setItemCaptionPropertyId("value");
						}
					}
					
					if(value != null && value.getValue().equalsIgnoreCase("Hospital")){
						if(hospmodeOfReceipt != null){
						cmbModeOfReceipt.setContainerDataSource(hospmodeOfReceipt);
						 cmbModeOfReceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						 cmbModeOfReceipt.setItemCaptionPropertyId("value");
						}
					}

				}
			}
		});
		
		documentsReceivedDate.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date value = (Date)event.getProperty().getValue();
				if(null != value)
				{
					if(value.after(new Date()))
					{
						documentsReceivedDate.setValue(null);
						/* Label label = new Label("Document Received Date cannot be greater than current system date.", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);*/
							
							/*final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);*/
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox("Document Received Date cannot be greater than current system date.", buttonsNamewithType);
							//chkhospitalization.setValue(false);
/*						
						
						HorizontalLayout layout = new HorizontalLayout(
								new Label("Document Received Date cannot be greater than current system date."));
						layout.setMargin(true);
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("");
						dialog.setWidth("35%");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);*/
					}
					else if(getDifferenceBetweenDates(value) > 7)
					{
						
						/*Label label = new Label("Document Received Date can 7 days prior to current system date.", ContentMode.HTML);
						label.setStyleName("errMessage");
					 HorizontalLayout layout = new HorizontalLayout(
							 label);
						layout.setMargin(true);
						*/
						/*final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("Errors");
						//dialog.setWidth("35%");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);*/
						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
						GalaxyAlertBox.createErrorBox("Document Received Date can 7 days prior to current system date.", buttonsNamewithType);
						documentsReceivedDate.setValue(null);
					}
				}
			}
		});
		
		
		this.sectionDetailsListenerTableObj.dummySubCoverField.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -7831804284490287934L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField property = (TextField) event.getProperty();
				String value = property.getValue();
				subCoverBasedBillClassificationManipulation(value,bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey());
			}
		});
		
	}
	
	private void validateFieldsBasedOnModeOfReceipt(String value)
	{

		strModeOfReceipt = value;
		if(null != strDocRecFrom && ("Hospital").equalsIgnoreCase(value))
		//if((null != strDocRecFrom && ("Insured").equalsIgnoreCase(strDocRecFrom)) && (("Courier").equalsIgnoreCase(value) || ("Post").equalsIgnoreCase(value)))
		{
			txtAcknowledgementContactNo.setRequired(false);
			txtAcknowledgementContactNo.setReadOnly(true);
			txtAcknowledgementContactNo.setEnabled(false);
		}
		else if((null != strDocRecFrom && ("Insured").equalsIgnoreCase(strDocRecFrom)) && (("Courier").equalsIgnoreCase(value) || ("Post").equalsIgnoreCase(value)))
		{
			txtAcknowledgementContactNo.setRequired(false);
			txtAcknowledgementContactNo.setReadOnly(false);
			txtAcknowledgementContactNo.setEnabled(true);
		}
		else if((null != strDocRecFrom && ("Insured").equalsIgnoreCase(strDocRecFrom)) && ("In Person").equalsIgnoreCase(value))
		{
			txtAcknowledgementContactNo.setRequired(true);
			txtAcknowledgementContactNo.setReadOnly(false);
			txtAcknowledgementContactNo.setEnabled(true);
		}
	}

	
	private void buildBillClassificationBasedOnDocumentReceivedFrom(SelectValue value)
	{
		if(null != value)
		{
			strDocRecFrom = value.getValue();
		if(("Insured").equalsIgnoreCase(value.getValue()))
		{	
			
			if(null != strModeOfReceipt && (("Courier").equalsIgnoreCase(strModeOfReceipt) || ("Post").equalsIgnoreCase(strModeOfReceipt)))
			{
				txtAcknowledgementContactNo.setRequired(false);
				txtAcknowledgementContactNo.setReadOnly(false);
				txtAcknowledgementContactNo.setEnabled(true);
			}
			else 
			{
				txtAcknowledgementContactNo.setRequired(true);
				txtAcknowledgementContactNo.setReadOnly(false);
				txtAcknowledgementContactNo.setEnabled(true);
				setRequiredAndValidation(txtAcknowledgementContactNo);
				mandatoryFields.add(txtAcknowledgementContactNo);
			}
			/*else
			{
				txtAcknowledgementContactNo.setRequired(true);
				txtAcknowledgementContactNo.setReadOnly(false);
				txtAcknowledgementContactNo.setEnabled(true);
			}*/
			
			if(null == txtAcknowledgementContactNo.getValue())
			{
				 if(null != bean.getDocumentDetails().getAcknowledgmentContactNumber() && !("").equalsIgnoreCase(bean.getDocumentDetails().getAcknowledgmentContactNumber()))
				 {
					 txtAcknowledgementContactNo.setValue(bean.getDocumentDetails().getAcknowledgmentContactNumber());
				 }
			}
			
			
			txtEmailId.setReadOnly(false);
			txtEmailId.setEnabled(true);
			txtEmailId.setRequired(true);
			
			if(null == txtEmailId.getValue())
			{
				 if(null != bean.getDocumentDetails().getEmailId() && !("").equalsIgnoreCase(bean.getDocumentDetails().getEmailId()))
				 {
					 txtEmailId.setValue(bean.getDocumentDetails().getEmailId());
				 }
			}
			
			
			if(null != chkhospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))// && null != chkPartialHospitalization)
			{
				chkhospitalization.setEnabled(false);
				if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
				chkhospitalization.setValue(false);
				
				if(null != chkHospitalizationRepeat)
				{
					chkHospitalizationRepeat.setEnabled(true);
					if(null != this.bean.getDocumentDetails().getHospitalizationRepeat())
						chkHospitalizationRepeat.setValue(this.bean.getDocumentDetails().getHospitalizationRepeat());
				}
				
			/*	if(null != chkHospitalizationRepeat.getValue() && chkHospitalizationRepeat.getValue())
					chkHospitalizationRepeat.setValue(false);*/
				
				if(null != chkPartialHospitalization)
				chkPartialHospitalization.setEnabled(true);
				if(null != this.bean.getDocumentDetails().getPartialHospitalization())
					chkPartialHospitalization.setValue(this.bean.getDocumentDetails().getPartialHospitalization());
				
				if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
				{
					/**
					 * Production Enhancement.
					 * If doc rec from is insured, then by default
					 * pre and post hospitalization will be checked 
					 * and will be disabled.
					 * */
					//chkPreHospitalization.setEnabled(false);
					//reverting above comments fix, as in FA or billing will have provision to change pre or post hosp.
					//chkPreHospitalization.setValue(true);
					chkPreHospitalization.setEnabled(true);
					if(null != this.bean.getDocumentDetails().getPreHospitalization())
						chkPreHospitalization.setValue(this.bean.getDocumentDetails().getPreHospitalization());
					
				}
				//chkPreHospitalization.setEnabled(true);
				if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
				{
					/**
					 * Production Enhancement.
					 * If doc rec from is insured, then by default
					 * pre and post hospitalization will be checked 
					 * and will be disabled.
					 * */
					//reverting above comments fix, as in FA or billing will have provision to change pre or post hosp.
					//chkPostHospitalization.setEnabled(false);
					chkPostHospitalization.setEnabled(true);
					//chkPostHospitalization.setValue(true);
					if(null != this.bean.getDocumentDetails().getPostHospitalization())
						chkPostHospitalization.setValue(this.bean.getDocumentDetails().getPostHospitalization());
				}
			//	chkPostHospitalization.setEnabled(true);
				if(null != chkLumpSumAmount && null != chkLumpSumAmount.getValue() && chkLumpSumAmount.isEnabled())
				{
					chkLumpSumAmount.setEnabled(true);
					if(null != this.bean.getDocumentDetails().getLumpSumAmount())
						chkLumpSumAmount.setValue(this.bean.getDocumentDetails().getLumpSumAmount());
				}
				if(null != chkAddOnBenefitsHospitalCash && null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.isEnabled())
				{
					chkAddOnBenefitsHospitalCash.setEnabled(true);
					if(null != this.bean.getDocumentDetails().getAddOnBenefitsHospitalCash())
						chkAddOnBenefitsHospitalCash.setValue(this.bean.getDocumentDetails().getAddOnBenefitsHospitalCash());
					
				}
				if(null != chkAddOnBenefitsPatientCare && null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.isEnabled())
				{
					chkAddOnBenefitsPatientCare.setEnabled(true);
					if(null != this.bean.getDocumentDetails().getAddOnBenefitsPatientCare())
						chkAddOnBenefitsPatientCare.setValue(this.bean.getDocumentDetails().getAddOnBenefitsPatientCare());
				}
				//chkPartialHospitalization.setValue(false);
				if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL)
						|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER)){
					chkPreHospitalization.setEnabled(true);
				}
				
			}
			if(null != chkhospitalization && ("Reimbursement").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && null != chkhospitalization)
			{
				chkhospitalization.setEnabled(true);
				if(null != chkPartialHospitalization)
				{
					chkPartialHospitalization.setEnabled(false);
					if(null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
						chkPartialHospitalization.setValue(false);
				}
				
				if(null != chkHospitalizationRepeat)
				{
					if(chkHospitalizationRepeat != null){
						chkHospitalizationRepeat.setEnabled(true);
					}
				}
				
				//chkPartialHospitalization.setEnabled(true);
				
				/*chkPreHospitalization.setEnabled(true);
				chkPostHospitalization.setEnabled(true);*/
				
				if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
				{
					/**
					 * Production Enhancement.
					 * If doc rec from is insured, then by default
					 * pre and post hospitalization will be checked 
					 * and will be disabled.
					 * */
					//reverting the above comments fix . Setting it to true.
					chkPreHospitalization.setEnabled(true);
				//	chkPreHospitalization.setValue(true);
					//chkPostHospitalization.setEnabled(false);
				/*	if(chkPreHospitalization != null){
						chkPreHospitalization.setEnabled(true);
					}*/
				}
				//chkPreHospitalization.setEnabled(true);
				if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
				{
					//reverting the above comments fix . Setting it to true.
					chkPostHospitalization.setEnabled(true);
					//chkPostHospitalization.setValue(true);
					/*if(chkPostHospitalization != null){
						chkPostHospitalization.setEnabled(true);
					}*/
				}
				
				//chkPartialHospitalization.setValue(false);
				
			}
			/*else
			{
				chkhospitalization.setEnabled(true);
			}*/
			
			
			if(null != bean.getProductBenefitMap() && bean.getProductBenefitMap().get("LumpSumFlag") != null && !(0 == bean.getProductBenefitMap().get("LumpSumFlag")))
			{
				if(chkLumpSumAmount != null){
					chkLumpSumAmount.setEnabled(true);
				}
			}
			
			if(null != bean.getProductBenefitMap() && bean.getProductBenefitMap().get("hospitalCashFlag") != null && !(0 == bean.getProductBenefitMap().get("hospitalCashFlag")))
			{
				if(chkAddOnBenefitsHospitalCash != null){
					chkAddOnBenefitsHospitalCash.setEnabled(true); 
				}
			}
			
			//chkAddOnBenefitsPatientCare = binder.buildAndBind("Add on Benefits (Patient Care)", "addOnBenefitsPatientCare", CheckBox.class);
			
			if(null != bean.getProductBenefitMap() && bean.getProductBenefitMap().get("PatientCareFlag") != null && !(0 == bean.getProductBenefitMap().get("PatientCareFlag")))
			{
				if(chkAddOnBenefitsPatientCare != null){
					chkAddOnBenefitsPatientCare.setEnabled(true);
				}
			}
			
			
			if(null != bean.getProductBenefitMap() && bean.getProductBenefitMap().get(SHAConstants.OTHER_BENEFITS_FLAG) != null && !(0 == bean.getProductBenefitMap().get(SHAConstants.OTHER_BENEFITS_FLAG)))
			{
				if(chkOtherBenefits != null){
					chkOtherBenefits.setEnabled(true);
				}
				
			}
			//GLX2020017
			if(null != this.bean.getClaimDTO() && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_INDIVIDUAL_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
				||	null != this.bean.getClaimDTO() && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_FLOATER_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
				||	null != this.bean.getClaimDTO() && ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
				||	null != this.bean.getClaimDTO() && ReferenceTable.GROUP_TOPUP_PROD_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
			{
				if(chkOtherBenefits != null){
					chkOtherBenefits.setEnabled(false);
				}
				if(chkAddOnBenefitsHospitalCash !=null){
					chkAddOnBenefitsHospitalCash.setEnabled(false);
				}
			}
			/*
			if(null != chkLumpSumAmount && null != chkLumpSumAmount.getValue() && chkLumpSumAmount.isEnabled())
			{
				chkLumpSumAmount.setEnabled(true);
			}
			if(null != chkAddOnBenefitsHospitalCash && null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.isEnabled())
			{
				chkAddOnBenefitsHospitalCash.setEnabled(true);
			}
			if(null != chkAddOnBenefitsPatientCare && null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.isEnabled())
			{
				chkAddOnBenefitsPatientCare.setEnabled(true);
			}*/
			if(null == chkhospitalization)
			{
			txtHospitalizationClaimedAmt.setValue(null);
			//optDocumentVerified.setEnabled(false);
			}
			if(null == chkPreHospitalization)
			{
			txtPreHospitalizationClaimedAmt.setValue(null);
		//	optDocumentVerified.setEnabled(false);
			}
			
			if(null == chkPostHospitalization){
			txtPostHospitalizationClaimedAmt.setValue(null);
			//optDocumentVerified.setEnabled(false);
			}
			
			
			
			setRequiredAndValidation(txtEmailId);
			
			mandatoryFields.add(txtEmailId);
			
			/*if(null != chkOtherBenefits){
				if(null != chkEmergencyMedicalEvaluation)
				chkEmergencyMedicalEvaluation.setEnabled(true);
				if(null != chkCompassionateTravel)
				chkCompassionateTravel.setValue(true);
				if(null != chkRepatriationOfMortalRemains)
				chkRepatriationOfMortalRemains.setValue(true);
				if(null != chkPreferredNetworkHospital)
				chkPreferredNetworkHospital.setValue(true);
				if(null != chkSharedAccomodation)
				chkSharedAccomodation.setValue(true);
			}*/
			
			
			if(("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && null != chkLumpSumAmount)
			{
				chkLumpSumAmount.setEnabled(false);
			}
			if(null != sectionDetailsListenerTableObj)
			{
				subCoverBasedBillClassificationManipulation(sectionDetailsListenerTableObj.getSubCoverFieldValue(),this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey());
			}
			//added for new product only hospital cash need to enable
//			if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
//					this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_37)) {
//				if(chkhospitalization != null) {
//					chkhospitalization.setEnabled(false);
//				} else if (chkHospitalizationRepeat != null) {
//				chkHospitalizationRepeat.setEnabled(false);
//				}else if (chkPreHospitalization != null) {
//				chkPreHospitalization.setEnabled(false);
//				} else if (chkLumpSumAmount != null) {
//				chkLumpSumAmount.setEnabled(false);
//				} else if (chkPostHospitalization != null) {
//				chkPostHospitalization.setEnabled(false);
//				} else if (chkPartialHospitalization != null) {
//				chkPartialHospitalization.setEnabled(false);
//				} else if (chkAddOnBenefitsHospitalCash != null) {
//				chkAddOnBenefitsHospitalCash.setEnabled(false);
//				} else if (chkAddOnBenefitsPatientCare != null) {
//				chkAddOnBenefitsPatientCare.setEnabled(false);
//				} else if (chkOtherBenefits != null) {
//				chkOtherBenefits.setEnabled(false);
//				} else if (chkHospitalCash != null) {
//				chkHospitalCash.setEnabled(true);
//				}
//				
//			}
						
		}
		else
		{
			txtAcknowledgementContactNo.setReadOnly(false);
			//txtAcknowledgementContactNo.setEnabled(true);
			//if(!(null != txtAcknowledgementContactNo && null != txtAcknowledgementContactNo.getValue() && !("").equalsIgnoreCase(txtAcknowledgementContactNo.getValue())))
			//{
				txtAcknowledgementContactNo.setValue(null);
			//}
			txtAcknowledgementContactNo.setReadOnly(true);
			txtAcknowledgementContactNo.setEnabled(false);
			txtAcknowledgementContactNo.setRequired(false);
			
			
			txtEmailId.setReadOnly(false);
			//txtEmailId.setEnabled();
			//if(!(null != txtEmailId && null != txtEmailId.getValue() && !("").equalsIgnoreCase(txtEmailId.getValue())))
			{
				txtEmailId.setValue(null);
			}
	//	txtEmailId.setValue(null);
			txtEmailId.setReadOnly(true);
			txtEmailId.setEnabled(false);
			txtEmailId.setRequired(false);
			/*if(null != chkPartialHospitalization && null != chkhospitalization && null != chkPreHospitalization && null != chkPostHospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))
			{
				chkPartialHospitalization.setEnabled(false);
				if(null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
				chkPartialHospitalization.setValue(false);
				
				chkhospitalization.setEnabled(true);
				
				chkPreHospitalization.setEnabled(false);
				chkPostHospitalization.setEnabled(false);
			}*/
			if(null != chkhospitalization)
				chkhospitalization.setEnabled(true);
			if(null != chkHospitalizationRepeat)
			chkHospitalizationRepeat.setEnabled(false);
			if(null != chkPartialHospitalization)
			{

				chkPartialHospitalization.setEnabled(false);
				chkPartialHospitalization.setValue(null);
				if(null == chkhospitalization)
				txtHospitalizationClaimedAmt.setValue(null);
				txtHospitalizationClaimedAmt.setEnabled(false);
				//optDocumentVerified.setEnabled(false);
				
			}
			if(null != chkPreHospitalization)
			{

				chkPreHospitalization.setEnabled(false);
				chkPreHospitalization.setValue(null);
				txtPreHospitalizationClaimedAmt.setValue(null);
				txtPreHospitalizationClaimedAmt.setEnabled(false);
				//optDocumentVerified.setEnabled(false);
			}
			if(null != chkPostHospitalization)
			{

				chkPostHospitalization.setEnabled(false);
				chkPostHospitalization.setValue(null);
				txtPostHospitalizationClaimedAmt.setValue(null);
				txtPostHospitalizationClaimedAmt.setEnabled(false);
				//optDocumentVerified.setEnabled(false);
			}
			//	chkPostHospitalization.setEnabled(false);
			if(null != chkLumpSumAmount)
			{
				chkLumpSumAmount.setEnabled(false);
				chkLumpSumAmount.setValue(null);
				//optDocumentVerified.setEnabled(false);
			}
			if(null != chkAddOnBenefitsHospitalCash)
			{
				chkAddOnBenefitsHospitalCash.setEnabled(false);
				chkAddOnBenefitsHospitalCash.setValue(null);
				//optDocumentVerified.setEnabled(false);
				//chkAddOnBenefitsHospitalCash.setEnabled(false);
			}
			if(null != chkAddOnBenefitsPatientCare)
			{
				chkAddOnBenefitsPatientCare.setEnabled(false);
				chkAddOnBenefitsPatientCare.setValue(null);
				//optDocumentVerified.setEnabled(false);
			}
			/*Below commented bcoz below code only for doc received from Insured only
			if(chkPreHospitalization != null &&
					bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL)
					|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER)){
				chkPreHospitalization.setEnabled(true);
			}*/
			
			if(null != this.bean.getClaimDTO() && ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				if(null != chkOtherBenefits)
				{
					chkOtherBenefits.setValue(null);
					chkOtherBenefits.setEnabled(false);
					List<Field<?>> listOfOtherBenefitsChkBox = getListOfOtherBenefitsChkBox();
					unbindField(listOfOtherBenefitsChkBox);
					if(otherBenefitsLayout != null){
						otherBenefitsLayout.removeAllComponents();
					}
					
				}
			}
				//chkAddOnBenefitsPatientCare.setEnabled(false);
		
			/*if(null != chkOtherBenefits){
				if(null != chkEmergencyMedicalEvaluation)
				chkEmergencyMedicalEvaluation.setValue(true);
				if(null != chkCompassionateTravel)
				chkCompassionateTravel.setValue(false);
				if(null != chkRepatriationOfMortalRemains)
				chkRepatriationOfMortalRemains.setValue(true);
				if(null != chkPreferredNetworkHospital)
				chkPreferredNetworkHospital.setValue(false);
				if(null != chkSharedAccomodation)
				chkSharedAccomodation.setValue(false);
			}*/
			
			
			mandatoryFields.remove(txtAcknowledgementContactNo);
			mandatoryFields.remove(txtEmailId);
		}
		
		SelectValue selValue = new SelectValue(ReferenceTable.COMMONMASTER_NO, "No");
		if(cmbReconsiderationRequest != null && cmbReconsiderationRequest.getValue() != null){
			SelectValue selectValue = (SelectValue)cmbReconsiderationRequest.getValue();
			if(selectValue.getId().equals(ReferenceTable.COMMONMASTER_YES)){
				cmbReconsiderationRequest.setValue(selValue);
			}
		}
		
		
		loadQueryDetailsTableValues();
	}
		
	}
	
	private void addBillClassificationLister()

	{
		chkhospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 { 
					// chkPostHospitalization.setEnabled(true);
					// chkPreHospitalization.setEnabled(true);
					 if(!isQueryReplyReceived)
					 {
						 validateLumpSumClassification(SHAConstants.HOSPITALIZATION, chkhospitalization);
					 }
					 if(!lumpSumValidationFlag)
					 {
						 if(validateBillClassification())
						 {
							 /*Label label = new Label("Already hospitalization is existing for this claim.", ContentMode.HTML);
								label.setStyleName("errMessage");*/
							 /*HorizontalLayout layout = new HorizontalLayout(
									 label);
								layout.setMargin(true);
								
								//final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
								//dialog.setWidth("35%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								SelectValue modOfReceiptSpp= null;
								if(null != cmbModeOfReceipt)
								{
									modOfReceiptSpp = (SelectValue)cmbModeOfReceipt.getValue();
								}
								
								if(((null != modOfReceiptSpp) && ("SPP").equalsIgnoreCase(modOfReceiptSpp.getValue())))
								{
								if(bean.getPreauthDTO().getIsPhysicalVerificationPending()){
									GalaxyAlertBox.createErrorBox("Hospitalisation Acknowledgement not allowed. Scanned Copy already submitted by the hospital. Physical copy receipt is pending. Please use the menu Physical Copy Received (Maker) for further action.", buttonsNamewithType);
								}
								else if(bean.getPreauthDTO().getIsPhysicalVerificationcompleted()){
									GalaxyAlertBox.createErrorBox("Hospitlisation Acknowledgement not allowed. Scanned Copy already submitted by the hospital and Verification Completed.", buttonsNamewithType);
								}
								else{
								GalaxyAlertBox.createErrorBox("Already hospitalization is existing for this claim.", buttonsNamewithType);
						        }
								}
								else{
								GalaxyAlertBox.createErrorBox("Already hospitalization is existing for this claim.", buttonsNamewithType);
						        }
								chkhospitalization.setValue(false);
						 }
						 else
						 {
							 txtHospitalizationClaimedAmt.setEnabled(true);
						 }
					 }
					 //CR2019161 - Consent Letter from Insured for Investigation of Reimbursement Claims
					 if(cmbDocumentsReceivedFrom !=null && cmbDocumentsReceivedFrom.getValue() !=null && cmbReconsiderationRequest != null)
					 {
						 SelectValue docReceivedFrom = (SelectValue) cmbDocumentsReceivedFrom.getValue();
						 if (chkhospitalization.getValue() !=null && bean.getClaimDTO().getClaimType() !=null 
								 &&  bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)
								 && docReceivedFrom !=null					
								 && docReceivedFrom.getValue().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_INSURED)
								 && cmbReconsiderationRequest.getValue() !=null && ! cmbReconsiderationRequest.getValue().equals("Yes"))
						 {
								/* if(null != docDTO && !docDTO.isEmpty())
									{
										for (DocumentDetailsDTO documentDetailsDTO : docDTO) {
										
											if(null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue()) 
											{
												//if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()) && !(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS).equals(documentDetailsDTO.getStatusId()))
												{
												log.info("------hospitalization flag-----"+documentDetailsDTO.getHospitalizationFlag());
													if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()))
													{
														HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
														buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
														GalaxyAlertBox.createErrorBox("Already hospitalization is existing for this claim.", buttonsNamewithType);
														chkhospitalization.setValue(false);
													}
												}
											}
										}
									}	
								 else{*/
									 	DocumentCheckListDTO docList = masterService.getDocumentCheckListVal(40l); 
									 	SelectValue selParticulars = new SelectValue();
										selParticulars.setId(docList.getKey());
										selParticulars.setValue(docList.getValue());
										docList.setParticulars(selParticulars);
										SelectValue receivedStatus = new SelectValue();
										receivedStatus.setId(bean.getDocumentDetails().getDocumentCheckList().get(0).getReceivedStatus().getId());
										receivedStatus.setValue(bean.getDocumentDetails().getDocumentCheckList().get(0).getReceivedStatus().getValue());
										docList.setReceivedStatus(receivedStatus);
										List<DocumentCheckListDTO> objDocCheckList = documentCheckListValidation.getValues();
										List<Long> keys = new ArrayList<Long>();
										for (DocumentCheckListDTO docChekList : objDocCheckList) {
											keys.add(docChekList.getParticulars().getId());
										}
										if(docList != null && keys != null && !keys.contains(docList.getKey())) {
											bean.getDocumentDetails().getDocumentCheckList().add(docList);
											documentCheckListValidation.addBeanToList(docList);
										}
								 }
						 //} 
						}
				 }
				 else
				 {
					 //CR2019161 - Consent Letter from Insured for Investigation of Reimbursement Claims
						if(bean.getDocumentDetails().getDocumentCheckList() !=null && !bean.getDocumentDetails().getDocumentCheckList().isEmpty())
					 	{
							List<DocumentCheckListDTO> list = new ArrayList<DocumentCheckListDTO>();
					 		for (DocumentCheckListDTO documentChecckListDTO : bean.getDocumentDetails().getDocumentCheckList())
					 		{ 
					 			if(documentChecckListDTO!=null && documentChecckListDTO.getKey()!=null && !documentChecckListDTO.getKey().equals(40l)){
					 				list.add(documentChecckListDTO);
					 			}
					 		}
					 		documentCheckListValidation.setTableList(list);
					 	}
					 //chkPostHospitalization.setEnabled(false);
					 //chkPreHospitalization.setEnabled(false);
					 
					 if(validateBillClassification())
					 {
						// Label label = new Label("Pre or Post hospitalization cannot exist without hospitalization", ContentMode.HTML);
						 /*Label label = new Label("None of the bill classification can exist without hospitalization", ContentMode.HTML);
							label.setStyleName("errMessage");*/
						/* HorizontalLayout layout = new HorizontalLayout(
							label);
						 	layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("55%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);*/
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox("None of the bill classification can exist without hospitalization", buttonsNamewithType);
							/*if(null != cmbDocumentsReceivedFrom)
							{
								SelectValue docRecFrom = (SelectValue)cmbDocumentsReceivedFrom.getValue();
								if(null != docRecFrom)
								{
									String val = docRecFrom.getValue();
									subCoverBasedBillClassificationManipulation(val);
								}
								
							}*/
							
							/*if(null != sectionDetailsListenerTableObj)
							{
								String secVal = sectionDetailsListenerTableObj.getSubCoverFieldValue();
							}*/
							if(null != chkPreHospitalization)
							{
								chkPreHospitalization.setValue(null);
							}
							if(null != chkPostHospitalization)
							{
								chkPostHospitalization.setValue(null);
							}
							if(null != chkLumpSumAmount && null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("LumpSumFlag")))
							{
								if(null != sectionDetailsListenerTableObj)
								{
									String secVal = sectionDetailsListenerTableObj.getSubCoverFieldValue();
									if(ReferenceTable.LUMPSUM_SUB_COVER_CODE.equalsIgnoreCase(secVal))
									{
										chkLumpSumAmount.setEnabled(true);
										chkLumpSumAmount.setValue(null);
									}
									else
									{
										chkLumpSumAmount.setEnabled(false);
									}
								}
								else
								{
									chkLumpSumAmount.setEnabled(false);
								}
								
							}
							if(null != chkAddOnBenefitsHospitalCash && null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("hospitalCashFlag")))
							{
								chkAddOnBenefitsHospitalCash.setValue(null);
							}
							else if(null != chkAddOnBenefitsHospitalCash)
							{
								chkAddOnBenefitsHospitalCash.setEnabled(false);
							}
							if(null != chkAddOnBenefitsPatientCare && null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("PatientCareFlag")))
							{
								chkAddOnBenefitsPatientCare.setValue(null);
							}
							else if(null != chkAddOnBenefitsHospitalCash)
							{
								chkAddOnBenefitsPatientCare.setEnabled(false);
							}
							if(null != chkHospitalizationRepeat)
							{
								chkHospitalizationRepeat.setValue(null);
							}
							//chkhospitalization.setValue(false);
					 }
					// else
					 //{
						 txtHospitalizationClaimedAmt.setEnabled(false);
						 txtHospitalizationClaimedAmt.setValue(null);
					 //}

				 }
				 if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.STAR_MICRO_RORAL_AND_FARMERS_CARE)) {
						chkAddOnBenefitsHospitalCash.setEnabled(false);
						chkAddOnBenefitsPatientCare.setEnabled(false);
						chkOtherBenefits.setEnabled(false);
						
					}
			}
			}
		});
		
		
		/*chkHospitalizationRepeat
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					// chkPostHospitalization.setEnabled(true);
					// chkPreHospitalization.setEnabled(true);
					 
					 if(!((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue()) ||
									 (null != chkPreHospitalization && null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue()) ||
									 (null != chkPostHospitalization && null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue()) ||
									 (null != chkLumpSumAmount && null != chkLumpSumAmount.getValue() && chkLumpSumAmount.getValue()) ||
									 (null != chkAddOnBenefitsHospitalCash && null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.getValue()) ||
									 (null != chkAddOnBenefitsPatientCare && null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.getValue()))
					   )
					 {
						 if(validateBillClassification())
						 {
							 Label label = new Label("Hospitalization Repeat cannot exist without hospitalization", ContentMode.HTML);
								label.setStyleName("errMessage");
							 HorizontalLayout layout = new HorizontalLayout(
									 label);
								layout.setMargin(true);
								final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
							//	dialog.setWidth("55%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);
								chkHospitalizationRepeat.setValue(false);
						 }
						 else
						 {
							
							 
							 chkhospitalization.setEnabled(false);
							 //if(null != chkPreHospitalization && chkPreHospitalization.isEnabled())
							 chkPreHospitalization.setEnabled(false);
							 //if(null != chkPostHospitalization && chkPostHospitalization.isEnabled())
							 chkPostHospitalization.setEnabled(false);
							 //if(null != chkPartialHospitalization && chkPartialHospitalization.isEnabled())
							 chkPartialHospitalization.setEnabled(false);
							 //if(null != chkLumpSumAmount && chkLumpSumAmount.isEnabled())
							 chkLumpSumAmount.setEnabled(false);
							 //if(null != chkAddOnBenefitsHospitalCash && chkAddOnBenefitsHospitalCash.isEnabled())
							 chkAddOnBenefitsHospitalCash.setEnabled(false);
							 //if(null != chkAddOnBenefitsPatientCare && chkAddOnBenefitsPatientCare.isEnabled())
							 chkAddOnBenefitsPatientCare.setEnabled(false);
							 txtHospitalizationClaimedAmt.setEnabled(true);
						 }
					 }
					 else
					 {
						 Label label = new Label("None of the classification details can be selected along with hospitalization repeat", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
									label);
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("55%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkHospitalizationRepeat.setValue(false);
							 if (null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue())
							 {
								 txtHospitalizationClaimedAmt.setEnabled(true);
							 }
							 if (null != chkPreHospitalization && null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue()) 
							 {
								 txtPreHospitalizationClaimedAmt.setEnabled(true);
							 }
							 if (null != chkPostHospitalization && null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue()) 
							 {
								 txtPostHospitalizationClaimedAmt.setEnabled(true);
							 }
							chkhospitalization.setValue(false);
							chkPreHospitalization.setValue(false);
							chkPostHospitalization.setValue(false);
							chkPartialHospitalization.setValue(false);
							chkLumpSumAmount.setValue(false);
							chkAddOnBenefitsHospitalCash.setValue(false);
							chkAddOnBenefitsPatientCare.setValue(false);
					 }	
				 }
				 else
				 {
					 txtHospitalizationClaimedAmt.setEnabled(false);
					 if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
					 {
						 SelectValue docRecFromVal = (SelectValue)cmbDocumentsReceivedFrom.getValue();
						 if(("Hospital").equalsIgnoreCase(docRecFromVal.getValue()))
						 {
							 if(null != chkhospitalization && !chkhospitalization.isEnabled())
								 chkhospitalization.setEnabled(true);
							 if(null != chkPreHospitalization )//&& !chkPreHospitalization.isEnabled())
								 chkPreHospitalization.setEnabled(false);
								 if(null != chkPostHospitalization)// && !chkPostHospitalization.isEnabled())
								 chkPostHospitalization.setEnabled(false);
							if(null != chkLumpSumAmount)
								chkLumpSumAmount.setEnabled(false);
							if(null != chkAddOnBenefitsHospitalCash)
								chkAddOnBenefitsHospitalCash.setEnabled(false);
							if(null != chkAddOnBenefitsPatientCare)
								chkAddOnBenefitsPatientCare.setEnabled(false);
						 }
						 if(("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && ("Insured").equalsIgnoreCase(docRecFromVal.getValue()))
						 {
							 if(null != chkhospitalization)
								 chkhospitalization.setEnabled(false);
							 if(null != chkPartialHospitalization && !chkPartialHospitalization.isEnabled())
								 chkPartialHospitalization.setEnabled(true);
							 if(null != chkPreHospitalization )//&& !chkPreHospitalization.isEnabled())
								 chkPreHospitalization.setEnabled(true);
								 if(null != chkPostHospitalization)// && !chkPostHospitalization.isEnabled())
								 chkPostHospitalization.setEnabled(true);
							if(null != chkLumpSumAmount && chkLumpSumAmount.isEnabled())
								chkLumpSumAmount.setEnabled(true);
							if(null != chkAddOnBenefitsHospitalCash && chkAddOnBenefitsHospitalCash.isEnabled())
								chkAddOnBenefitsHospitalCash.setEnabled(true);
							if(null != chkAddOnBenefitsPatientCare && chkAddOnBenefitsPatientCare.isEnabled())
								chkAddOnBenefitsPatientCare.setEnabled(true);
						 }
						 if(("Reimbursement").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && ("Insured").equalsIgnoreCase(docRecFromVal.getValue()))
						 {
							 if(null != chkhospitalization)
								 chkhospitalization.setEnabled(true);
							 if(null != chkPartialHospitalization && !chkPartialHospitalization.isEnabled())
								 chkPartialHospitalization.setEnabled(false);
							 if(null != chkPreHospitalization )//&& !chkPreHospitalization.isEnabled())
								 chkPreHospitalization.setEnabled(true);
								 if(null != chkPostHospitalization)// && !chkPostHospitalization.isEnabled())
								 chkPostHospitalization.setEnabled(true);
							if(null != chkLumpSumAmount && chkLumpSumAmount.isEnabled())
								chkLumpSumAmount.setEnabled(true);
							if(null != chkAddOnBenefitsHospitalCash && chkAddOnBenefitsHospitalCash.isEnabled())
								chkAddOnBenefitsHospitalCash.setEnabled(true);
							if(null != chkAddOnBenefitsPatientCare && chkAddOnBenefitsPatientCare.isEnabled())
								chkAddOnBenefitsPatientCare.setEnabled(true);
						 }
						 
						 if(null != chkhospitalization && ( 
								 (("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(docRecFromVal.getValue())) ||("Reimbursement").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue())
								 
								 ))  
						 if(null != chkhospitalization && !chkhospitalization.isEnabled())
						 chkhospitalization.setEnabled(true);
						 
						 if(null != chkPartialHospitalization && ( 
								 (("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && ("Insured").equalsIgnoreCase(docRecFromVal.getValue())) //||("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue())
								 )) 
						 //if(null != chkPartialHospitalization && chkPartialHospitalization.isEnabled())
						 chkPartialHospitalization.setEnabled(true);
						if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("LumpSumFlag"))) 
						 chkLumpSumAmount.setEnabled(true);
						 if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("hospitalCashFlag")))
						 chkAddOnBenefitsHospitalCash.setEnabled(true);
						 if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("PatientCareFlag")))
						 chkAddOnBenefitsPatientCare.setEnabled(true);
					 }
					 else
					 {
						 HorizontalLayout layout = new HorizontalLayout(
									new Label("Please select documents received from"));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setWidth("55%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkHospitalizationRepeat.setValue(false);
					 }
				 }
			}
			}
		});*/
		
		chkHospitalizationRepeat
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					// chkPostHospitalization.setEnabled(true);
					// chkPreHospitalization.setEnabled(true);
					 /**
					  * Added for ticket 5628. When query reply is received,
					  * then lumpsum based validation should be skipped.
					  * */
					 if(!isQueryReplyReceived)
					 {
						 validateLumpSumClassification(SHAConstants.HOSPITALIZATION_REPEAT, chkHospitalizationRepeat);
					 }
					 
					 if(!lumpSumValidationFlag)
					 {
						 if(!((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue()) ||
										 (null != chkPreHospitalization && null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue()) ||
										 (null != chkPostHospitalization && null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue()) ||
										 (null != chkLumpSumAmount && null != chkLumpSumAmount.getValue() && chkLumpSumAmount.getValue()) ||
										 (null != chkAddOnBenefitsHospitalCash && null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.getValue()) ||
										 (null != chkAddOnBenefitsPatientCare && null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.getValue()) ||
										 (null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
								 )
						   )
						 {
							 if(validateBillClassification())
							 {
								 /*Label label = new Label("Hospitalization Repeat cannot exist without hospitalization", ContentMode.HTML);
									label.setStyleName("errMessage");
								 HorizontalLayout layout = new HorizontalLayout(
										 label);
									layout.setMargin(true);*/
									/*final ConfirmDialog dialog = new ConfirmDialog();
									dialog.setCaption("Errors");
									//dialog.setWidth("40%");
									dialog.setClosable(true);
									dialog.setContent(layout);
									dialog.setResizable(false);
									dialog.setModal(true);
									dialog.show(getUI().getCurrent(), null, true);*/
									HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
									buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
									GalaxyAlertBox.createErrorBox("Hospitalization Repeat cannot exist without hospitalization", buttonsNamewithType);
									chkHospitalizationRepeat.setValue(null);
							 }
							 /*else if(true)
							 {
								 fireViewEvent(DocumentDetailsPresenter.VALIDATE_HOSPITALIZATION_REPEAT, bean.getClaimDTO().getKey());
							 }*/
							 
							 else
							 {
								 fireViewEvent(DocumentDetailsPresenter.VALIDATE_HOSPITALIZATION_REPEAT, bean.getClaimDTO().getKey());
								 
								/* txtHospitalizationClaimedAmt.setEnabled(true);
								 chkhospitalization.setEnabled(false);
								 //if(null != chkPreHospitalization && chkPreHospitalization.isEnabled())
								 chkPreHospitalization.setEnabled(false);
								 //if(null != chkPostHospitalization && chkPostHospitalization.isEnabled())
								 chkPostHospitalization.setEnabled(false);
								 //if(null != chkPartialHospitalization && chkPartialHospitalization.isEnabled())
								 chkPartialHospitalization.setEnabled(false);
								 //chkPartialHospitalization.setValue(null);
								 //if(null != chkLumpSumAmount && chkLumpSumAmount.isEnabled())
								 chkLumpSumAmount.setEnabled(false);
								 //if(null != chkAddOnBenefitsHospitalCash && chkAddOnBenefitsHospitalCash.isEnabled())
								 chkAddOnBenefitsHospitalCash.setEnabled(false);
								 //if(null != chkAddOnBenefitsPatientCare && chkAddOnBenefitsPatientCare.isEnabled())
								 chkAddOnBenefitsPatientCare.setEnabled(false);*/
							 }
							 
						 }
					// }
					 else
					 {
						 /*Label label = new Label("None of the classification details can be selected along with hospitalization repeat", ContentMode.HTML);
							label.setStyleName("errMessage");*/
						 /*HorizontalLayout layout = new HorizontalLayout(
									label);
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
						//	dialog.setWidth("55%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);*/
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox("None of the classification details can be selected along with hospitalization repeat", buttonsNamewithType);
							chkHospitalizationRepeat.setValue(false);
							if (null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue())
							 {
								 txtHospitalizationClaimedAmt.setEnabled(true);
							 }
							 if (null != chkPreHospitalization && null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue()) 
							 {
								 txtPreHospitalizationClaimedAmt.setEnabled(true);
							 }
							 if (null != chkPostHospitalization && null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue()) 
							 {
								 txtPostHospitalizationClaimedAmt.setEnabled(true);
							 }
							 if(null != chkLumpSumAmount && null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("LumpSumFlag")))
								{
									if(null != sectionDetailsListenerTableObj)
									{
										String secVal = sectionDetailsListenerTableObj.getSubCoverFieldValue();
										if(ReferenceTable.LUMPSUM_SUB_COVER_CODE.equalsIgnoreCase(secVal))
										{
											chkLumpSumAmount.setEnabled(true);
											chkLumpSumAmount.setValue(null);
										}
										else
										{
											chkLumpSumAmount.setEnabled(false);
										}
									}
									else
									{
										chkLumpSumAmount.setEnabled(false);
									}
									
								}
								if(null != chkAddOnBenefitsHospitalCash && null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("hospitalCashFlag")))
								{
									chkAddOnBenefitsHospitalCash.setValue(null);
								}
								else if(null != chkAddOnBenefitsHospitalCash)
								{
									chkAddOnBenefitsHospitalCash.setEnabled(false);
								}
								if(null != chkAddOnBenefitsPatientCare && null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("PatientCareFlag")))
								{
									chkAddOnBenefitsPatientCare.setValue(null);
								}
								else if(null != chkAddOnBenefitsPatientCare)
								{
									chkAddOnBenefitsPatientCare.setEnabled(false);
								}
							/*chkhospitalization.setValue(false);
							chkPreHospitalization.setValue(false);
							chkPostHospitalization.setValue(false);
							chkPartialHospitalization.setValue(false);
							chkLumpSumAmount.setValue(false);
							chkAddOnBenefitsHospitalCash.setValue(false);
							chkAddOnBenefitsPatientCare.setValue(false);*/
					 }	
					}
				 }
				 else
				 {
					 txtHospitalizationClaimedAmt.setEnabled(false);
					 if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
					 {
						 SelectValue docRecFromVal = (SelectValue)cmbDocumentsReceivedFrom.getValue();
						 
						 if(("Hospital").equalsIgnoreCase(docRecFromVal.getValue()))
						 {
							 if(null != chkPreHospitalization)
							 {
								 chkPreHospitalization.setEnabled(false);
							 }
							 if(null != chkPostHospitalization)
							 {
								 chkPostHospitalization.setEnabled(false);
							 }
							 if(null != chkPartialHospitalization)
							 {
								 chkPartialHospitalization.setEnabled(false);
							 }
							 if(null != chkLumpSumAmount)
							 {
								 chkLumpSumAmount.setEnabled(false);
							 }
							 if(null != chkAddOnBenefitsHospitalCash)
							 {
								 chkAddOnBenefitsHospitalCash.setEnabled(false);
							 }
							 if(null != chkAddOnBenefitsPatientCare)
							 {
								 chkAddOnBenefitsPatientCare.setEnabled(false);
							 }
						 }
						 else
						 {
							 if(null != chkhospitalization && ( 
									 (("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(docRecFromVal.getValue())) ||("Reimbursement").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue())
									 )) 
							 if(null != chkhospitalization && !chkhospitalization.isEnabled())
							 chkhospitalization.setEnabled(true);
							// if(null != chkPreHospitalization && !chkPreHospitalization.isEnabled())
							 if(null != chkPreHospitalization && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
							 chkPreHospitalization.setEnabled(true);
							 //if(null != chkPostHospitalization && !chkPostHospitalization.isEnabled())
							 if(null != chkPostHospitalization && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
							 chkPostHospitalization.setEnabled(true);
							 if(null != chkPartialHospitalization && ( 
									 (("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && ("Insured").equalsIgnoreCase(docRecFromVal.getValue())) //||("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue())
									 )) 
							 //if(null != chkPartialHospitalization && chkPartialHospitalization.isEnabled())
							 chkPartialHospitalization.setEnabled(true);
							if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("LumpSumFlag"))) 
							 chkLumpSumAmount.setEnabled(true);
							 if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("hospitalCashFlag")))
							 chkAddOnBenefitsHospitalCash.setEnabled(true);
							 if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("PatientCareFlag")))
							 chkAddOnBenefitsPatientCare.setEnabled(true);
						 }
					 }
					/* else
					 {
						 HorizontalLayout layout = new HorizontalLayout(
									new Label("Please select documents received from"));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setWidth("55%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkHospitalizationRepeat.setValue(false);
					 }*/
				 }
			}
			}
		});
	
		
		
		chkPartialHospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 if(!isQueryReplyReceived)
					 {
						 validateLumpSumClassification(SHAConstants.PARTIALHOSPITALIZATION, chkPartialHospitalization);
					 }
					 if(!lumpSumValidationFlag)
					 {
						 if(validateBillClassification())
						 {
							 /*Label label = new Label("Already partial hospitalization is existing for this claim.", ContentMode.HTML);
								label.setStyleName("errMessage");*/
							 /*HorizontalLayout layout = new HorizontalLayout(
										label);
	
								layout.setMargin(true);
								final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
								//dialog.setWidth("55%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Already partial hospitalization is existing for this claim.", buttonsNamewithType);
								//chkPartialHospitalization.setValue(false);
								chkPartialHospitalization.setValue(null);
						 }
						 else 
						 {
							 //chkPostHospitalization.setEnabled(true);
							 //chkPreHospitalization.setEnabled(true);
							 txtHospitalizationClaimedAmt.setEnabled(true);						 
						 }
					 }
				 }
				 else
				 {
					 
					 
					 
					 if(validateBillClassification())
					 {
						 /**
						  * Pre and post will be selected and disabled by default for docs which 
						  * has been received from insured. Hence commenting the below code.
						  * Also, if its hospital which has sent documents, then hospitalization
						  * alone will be enabled in screen. Hence commenting this will not
						  * have any impact on existing func.
						  * */
						 /*The above comments is reverted and validations are enabled, since in FA or billing they can edit pre or post.
						  * 
						  */
						 
						 /*Label label = new Label("Pre or Post hospitalization cannot exist without Partial hospitalization", ContentMode.HTML);
							label.setStyleName("errMessage");*/
						 /*HorizontalLayout layout = new HorizontalLayout(
									label);
						 layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("55%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);*/
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox("Pre or Post hospitalization cannot exist without Partial hospitalization", buttonsNamewithType);
							if(null != chkPreHospitalization)
							{
								chkPreHospitalization.setValue(false);
							}
							if(null != chkPostHospitalization)
							{
								chkPostHospitalization.setValue(false);
							}
							//chkhospitalization.setValue(false);
					 }
					 
					 //chkPostHospitalization.setEnabled(false);
					 //chkPreHospitalization.setEnabled(false);
					 txtHospitalizationClaimedAmt.setEnabled(false);
					 txtHospitalizationClaimedAmt.setValue(null);

				 }
			}
			}
		});
		
		chkPreHospitalization .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 if(!isQueryReplyReceived)
					 {
						 validateLumpSumClassification(SHAConstants.PREHOSPITALIZATION, chkPreHospitalization);
					 }
					 
					
					 /**
					  * Pre and post will be selected and disabled by default for docs which 
					  * has been received from insured. Hence commenting the below code.
					  * Also, if its hospital which has sent documents, then hospitalization
					  * alone will be enabled in screen. Hence commenting this will not
					  * have any impact on existing func.
					  * */
					 /*The above comments is reverted and validations are enabled, since in FA or billing they can edit pre or post.
					  * 
					  */
					if(!lumpSumValidationFlag)
					{
						 if(validateBillClassification())
						 {
							 /*Label label = new Label("Pre hosptilization cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
								label.setStyleName("errMessage");*/
							 /*HorizontalLayout layout = new HorizontalLayout(
										label);
							
								layout.setMargin(true);
								final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
								//dialog.setWidth("55%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Pre hosptilization cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
								chkPreHospitalization.setValue(false);
						 }
						 else
						 {
							 txtPreHospitalizationClaimedAmt.setEnabled(true);
						 }
					// txtPreHospitalizationClaimedAmt.setEnabled(true);
					}
				 }
				 else
				 {
					 txtPreHospitalizationClaimedAmt.setEnabled(false);
					 txtPreHospitalizationClaimedAmt.setValue(null);

				 }
			}
			}
		});
		
		chkPostHospitalization .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(!isQueryReplyReceived){
					 validateLumpSumClassification(SHAConstants.POSTHOSPITALIZATION, chkPostHospitalization);	 
				 }
				 
				 

				 /**
				  * Pre and post will be selected and disabled by default for docs which 
				  * has been received from insured. Hence commenting the below code.
				  * Also, if its hospital which has sent documents, then hospitalization
				  * alone will be enabled in screen. Hence commenting this will not
				  * have any impact on existing func.
				  * 
				  * 
				  * */
				 
				 /*The above comments is reverted and validations are enabled, since in FA or billing they can edit pre or post.
				  * 
				  */
				 
				/* if(value)
				 {
					 txtPostHospitalizationClaimedAmt.setEnabled(true);
				 }else{
					 txtPostHospitalizationClaimedAmt.setEnabled(false); 
					 txtPostHospitalizationClaimedAmt.setValue(null);
				 }*/
				/* if(value)
				 {
					 
					 */
				 if(!lumpSumValidationFlag)
				 {
						 if(value)
						 {
						 
							 if(validateBillClassification())
							 {
								 /*Label label = new Label("Post hosptilization cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
									label.setStyleName("errMessage");*/
								 /*HorizontalLayout layout = new HorizontalLayout(
											label);
									layout.setMargin(true);
									final ConfirmDialog dialog = new ConfirmDialog();
									dialog.setCaption("Errors");
									//dialog.setWidth("55%");
									dialog.setClosable(true);
									dialog.setContent(layout);
									dialog.setResizable(false);
									dialog.setModal(true);
									dialog.show(getUI().getCurrent(), null, true);*/
									HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
									buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
									GalaxyAlertBox.createErrorBox("Post hosptilization cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
									chkPostHospitalization.setValue(false);
							 }
							 else 
							 {
								 txtPostHospitalizationClaimedAmt.setEnabled(true); 
							 }
						 }
						 else
						 {
							 txtPostHospitalizationClaimedAmt.setEnabled(false);
							 txtPostHospitalizationClaimedAmt.setValue(null);
						 }
				 }
					
				 }
				/* else
				 {
					 txtPostHospitalizationClaimedAmt.setEnabled(false);
					 txtPostHospitalizationClaimedAmt.setValue(null);

				 }*/
				
			}
			
		});
		
		chkLumpSumAmount.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 Boolean value = (Boolean) event.getProperty().getValue();
				 if(null != value && value)
				 {
					 if(!isQueryReplyReceived){
						 	validateLumpSumClassification(SHAConstants.LUMPSUMAMOUNT,chkLumpSumAmount);
					 }
					if(!lumpSumValidationFlag)
					{
						/*
						 * Lumpsum can be first rod for medi premier product. Hence if product is
						 * medipremier, then below validation will not happen.
						 * **/
						if(!(ReferenceTable.getLumsumProductKeys().containsKey(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
								&& !((ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
										|| ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
										&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))
										 && !(ReferenceTable.RAKSHAK_CORONA_PRODUCT_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())))
							{
								 if(validateBillClassification())
								 {
									/* Label label = new Label("Lumpsum Amount cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
										label.setStyleName("errMessage");
									 HorizontalLayout layout = new HorizontalLayout(
												label);
										layout.setMargin(true);*/
										/*final ConfirmDialog dialog = new ConfirmDialog();
										dialog.setCaption("Errors");
										//dialog.setWidth("55%");
										dialog.setClosable(true);
										dialog.setContent(layout);
										dialog.setResizable(false);
										dialog.setModal(true);
<<<<<<< HEAD
										dialog.show(getUI().getCurrent(), null, true); 
=======
										dialog.show(getUI().getCurrent(), null, true);*/
										HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
										buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
										GalaxyAlertBox.createErrorBox("Lumpsum Amount cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
										chkLumpSumAmount.setValue(false);
										enableOrDisableBillClassification(true);
								 } 
								 else
								 {
									// warnMessageForLumpSum(); 
									 enableOrDisableBillClassification(false);
								 }
						}
						else
						{
							if(!((ReferenceTable.STAR_CANCER_PRODUCT_KEY).equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()) ||
									(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY).equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()) ||
									(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
									|| ((ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()) 
											|| ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
									&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))
									 || bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().equals(ReferenceTable.RAKSHAK_CORONA_PRODUCT_KEY) ||
									 (ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND).equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())))
							{
							if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() && null != bean.getNewIntimationDTO().getPolicy().getPolicyType() 
									&& ReferenceTable.FRESH_POLICY.equals(bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey()))
							{
								warnMessageForLumpSum();
							}
							 enableOrDisableBillClassification(false);
							}
						}
					}
				 }
				 else
				 {
					 if(null != sectionDetailsListenerTableObj)
					 {
						 String subCovervalue = sectionDetailsListenerTableObj.getSubCoverFieldValue();
						  	if(!(ReferenceTable.LUMPSUM_SUB_COVER_CODE).equalsIgnoreCase(subCovervalue))
							{
						  		 if(!isQueryReplyReceived){
						  		enableOrDisableBillClassification(true);
						  		 }
								//enableOrDisableClassificationBasedOnsubCover(5,docRecFromVal);
							}
					 }
					 
				 }
			}
			} 
		});
		
		chkAddOnBenefitsHospitalCash.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 if(!isQueryReplyReceived){
						 validateLumpSumClassification(SHAConstants.HOSPITALCASH,chkAddOnBenefitsHospitalCash);
					 }
					if(!lumpSumValidationFlag)
					{
							 if(validateBillClassification())
							 {
								 
								 /*Label label = new Label("Hospital cash cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
									label.setStyleName("errMessage");*/
								 /*HorizontalLayout layout = new HorizontalLayout(
											label);
									layout.setMargin(true);
									final ConfirmDialog dialog = new ConfirmDialog();
									dialog.setCaption("Errors");
									//dialog.setWidth("55%");
									dialog.setClosable(true);
									dialog.setContent(layout);
									dialog.setResizable(false);
									dialog.setModal(true);
									dialog.show(getUI().getCurrent(), null, true);*/
									HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
									buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
									GalaxyAlertBox.createErrorBox("Hospital cash cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
									chkAddOnBenefitsHospitalCash.setValue(false);
							 }
						
					}
				 }
				}
			}
		});
		
		chkAddOnBenefitsPatientCare.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 if(!isQueryReplyReceived){
						 validateLumpSumClassification(SHAConstants.PATIENTCARE,chkAddOnBenefitsPatientCare);
					 }
					 if(!lumpSumValidationFlag)
					 {
						 if(validateBillClassification())
						 {
							 /*Label label = new Label("Patient care cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
								label.setStyleName("errMessage");
							 HorizontalLayout layout = new HorizontalLayout(
										label);
								layout.setMargin(true);*/
								/*final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
								//dialog.setWidth("55%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Patient care cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
								chkAddOnBenefitsPatientCare.setValue(false);
						 }
					 }
				 }
			}
			}
		});
		
		chkOtherBenefits .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
										 
				 buildOtherBenefitsLayout(value);
				 
				 //method
				 if(value)
				 {
					 txtOtherBenefitClaimedAmnt.setEnabled(true);
				 }
				 else{
					 txtOtherBenefitClaimedAmnt.setEnabled(false);
					 txtOtherBenefitClaimedAmnt.setValue(null);
				 }	 						 
						
				 }							
			}
			
		});
		
	}
	private int getDifferenceBetweenDates(Date value)
	{
		
		long currentDay = new Date().getTime();
		long enteredDay = value.getTime();
		int diff = (int)((currentDay-enteredDay))/(1000 * 60 * 60 * 24);
		if(diff<0){
			diff = diff *(-1);
		}
		return diff;
	}
	
	
	@SuppressWarnings("unchecked")
	public void loadContainerDataSources(Map<String, Object> referenceDataMap)
	{		
		docReceivedFromRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("docReceivedFrom");
		 cmbDocumentsReceivedFrom.setContainerDataSource(docReceivedFromRequest);
		 cmbDocumentsReceivedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbDocumentsReceivedFrom.setItemCaptionPropertyId("value");
		// cmbDocumentsReceivedFrom.setEnabled(false);
		 if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		 {
			 for(int i = 0 ; i<docReceivedFromRequest.size() ; i++)
			 {
				if (("Hospital").equalsIgnoreCase(docReceivedFromRequest.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
					if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getIsTataPolicy() && bean.getNewIntimationDTO().getIsTataPolicy()){
						this.cmbDocumentsReceivedFrom.setEnabled(Boolean.FALSE);
					}
				}
			}
		 }
		 else 
		 {
			 for(int i = 0 ; i<docReceivedFromRequest.size() ; i++)
			 {
				if (("Insured").equalsIgnoreCase(docReceivedFromRequest.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
					this.cmbDocumentsReceivedFrom.setEnabled(false);
				}
			}
		 }
		 
		 reconsiderationRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("commonValues");
		 cmbReconsiderationRequest.setContainerDataSource(reconsiderationRequest);
		 cmbReconsiderationRequest.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbReconsiderationRequest.setItemCaptionPropertyId("value");
		 for(int i = 0 ; i<reconsiderationRequest.size() ; i++)
		 	{
				if (("NO").equalsIgnoreCase(reconsiderationRequest.getIdByIndex(i).getValue()))
				{
					this.cmbReconsiderationRequest.setValue(reconsiderationRequest.getIdByIndex(i));
				}				
			}
		
		 SelectValue selectValue =(SelectValue)cmbDocumentsReceivedFrom.getValue();
		 if(selectValue != null && selectValue.getValue() != null && !selectValue.getValue().isEmpty()
				 && selectValue.getValue().equalsIgnoreCase("Insured")){
			 modeOfReceipt = (BeanItemContainer<SelectValue>) referenceDataMap.get("modeOfReceipt");
		 } else {
			 modeOfReceipt = (BeanItemContainer<SelectValue>) referenceDataMap.get("hospmodeOfReceipt");
		 }
		 cmbModeOfReceipt.setContainerDataSource(modeOfReceipt);
		 cmbModeOfReceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbModeOfReceipt.setItemCaptionPropertyId("value");
		 
		 
		 hospmodeOfReceipt = (BeanItemContainer<SelectValue>) referenceDataMap.get("hospmodeOfReceipt");
		 insmodeOfReceipt = (BeanItemContainer<SelectValue>) referenceDataMap.get("modeOfReceipt");
	
		 /*
		  * On load , reason for reconsideration drop down container is loaded. But only if 
		  * reconsideration is set to YES, the reason for reconsideration drop down will be enabled.
		  * Hence loading of this is done in loadReasonForReconsiderationDropDown() method.
		  * */ 
		reasonForReconsiderationRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("reasonForReconsiderationRequest");
		
		loadReasonForReconsiderationDropDown();
//		cmbReasonForReconsideration.setReadOnly(false);
//		for(int i = 0; i<reasonForReconsiderationRequest.size();i++){
//			if(reasonForReconsiderationRequest.getIdByIndex(i).getValue().equalsIgnoreCase("Reconsideration of settled claims")){	
//				
//				cmbReasonForReconsideration.setValue(reasonForReconsiderationRequest.getIdByIndex(i));
//				break;
//			}
//			else if(reasonForReconsiderationRequest.getIdByIndex(i).getValue().equalsIgnoreCase("Reconsideration of rejected claims")){
//				cmbReasonForReconsideration.setValue(reasonForReconsiderationRequest.getIdByIndex(i));
//				break;
//			}
//	     }
		cmbReasonForReconsideration.setReadOnly(true);
		
		 
		 /*docReceivedFromRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("docReceivedFrom");
		 cmbDocumentsReceivedFrom.setContainerDataSource(docReceivedFromRequest);
		 cmbDocumentsReceivedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbDocumentsReceivedFrom.setItemCaptionPropertyId("value");
		// cmbDocumentsReceivedFrom.setEnabled(false);
		 if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		 {
			 for(int i = 0 ; i<docReceivedFromRequest.size() ; i++)
			 {
				if (("Hospital").equalsIgnoreCase(docReceivedFromRequest.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
				}
			}
		 }
		 else 
		 {
			 for(int i = 0 ; i<docReceivedFromRequest.size() ; i++)
			 {
				if (("Insured").equalsIgnoreCase(docReceivedFromRequest.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
					this.cmbDocumentsReceivedFrom.setEnabled(false);
				}
			}
		 }*/
		 
		// documentCheckList.setReceivedStatus((BeanItemContainer<SelectValue>)referenceDataMap.get("docReceivedStatus"));
			
			
		 //documentCheckList.setReference(referenceDataMap);
	//	 documentCheckList.setDefaultValuesToCmb("Not Received");
	//	 rodQueryDetails.generateDropDown(reconsiderationRequest);
		 //this.docDTO = (DocumentDetailsDTO) referenceDataMap.get("billClaissificationDetails");
			if(null != documentCheckListValidation)
			{
				documentCheckListValidation.setReferenceData(referenceDataMap);
			}
		 this.docDTO = (List<DocumentDetailsDTO>) referenceDataMap.get("billClaissificationDetails");
		 this.isFinalEnhancement = (Boolean)referenceDataMap.get("isFinalEnhancement");
		 setValuesFromDTO();
		 /*
		  * When user navigates front and back, amount claimed fields are disabled,
		  * thought the bill classification checkbox is enabled. Hence , to display those
		  * values, the below methods are added.
		  * */
		 displayAmountClaimedDetails();
		

	}
	
	private void loadReasonForReconsiderationDropDown()
	{
		 if(null != cmbReasonForReconsideration)
		 { 
			 cmbReasonForReconsideration.setContainerDataSource(reasonForReconsiderationRequest);
			 cmbReasonForReconsideration.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			 cmbReasonForReconsideration.setItemCaptionPropertyId("value");
		 }
	}
	
	private void displayAmountClaimedDetails()
	{
		if(null != this.bean.getDocumentDetails())
		{
			if ((null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) || (null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue()) 
					|| (null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()))
			{
				txtHospitalizationClaimedAmt.setEnabled(true);
				txtHospitalizationClaimedAmt.setValue(hospitalizationClaimedAmt);
			}
			 if (null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
			{
				txtPreHospitalizationClaimedAmt.setEnabled(true);
				txtPreHospitalizationClaimedAmt.setValue(preHospitalizationAmt);
			}
			 if (null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
			{
				txtPostHospitalizationClaimedAmt.setEnabled(true);
				txtPostHospitalizationClaimedAmt.setValue(postHospitalizationAmt);
				//txtPostHospitalizationClaimedAmt.setEnabled(false);
			}
			 if(null != this.chkOtherBenefits && null != this.chkOtherBenefits.getValue() && this.chkOtherBenefits.getValue())
			 {
				 chkOtherBenefits.setEnabled(true);
				 txtOtherBenefitClaimedAmnt.setValue(otherBenefitsAmnt);
			 }
		}
	}

	
	
	private List<Field<?>> getListOfChkBox()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(chkhospitalization);
		fieldList.add(chkPreHospitalization);
		fieldList.add(chkPostHospitalization);
		fieldList.add(chkPartialHospitalization);
		fieldList.add(chkLumpSumAmount);
		fieldList.add(chkAddOnBenefitsHospitalCash);
		fieldList.add(chkAddOnBenefitsPatientCare);
		fieldList.add(chkHospitalizationRepeat);
		fieldList.add(chkOtherBenefits);
		//added for new product
		fieldList.add(chkHospitalCash);
		//Added for issue no 2558
		if(null != cmbReasonForReconsideration)
		fieldList.add(cmbReasonForReconsideration);
		if(null != optPaymentCancellation)
		fieldList.add(optPaymentCancellation);
		return fieldList;
	}
	
	private List<Field<?>> getListOfOtherBenefitsChkBox()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(chkEmergencyMedicalEvaluation);
		fieldList.add(chkCompassionateTravel);
		fieldList.add(chkRepatriationOfMortalRemains);
		fieldList.add(chkPreferredNetworkHospital);
		fieldList.add(chkSharedAccomodation);
		return fieldList;
	}
	
	private void unbindField(List<Field<?>> field) {
		if(null != field && !field.isEmpty())
		{
			for (Field<?> field2 : field) {
				if (field2 != null ) {
					Object propertyId = this.binder.getPropertyId(field2);
					//if (field2!= null && field2.isAttached() && propertyId != null) {
					if (field2!= null  && propertyId != null) {
						this.binder.unbind(field2);
					}
				}
			}
		}
	}
	
	public boolean validatePage() {
		
		bean.getClaimDTO().setNewIntimationDto(bean.getNewIntimationDTO());
		
		Boolean hasError = false;
		
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		Boolean isReconsiderationRequest = false;
		
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
		
		if(null != this.txtEmailId && null != this.txtEmailId.getValue() && !("").equalsIgnoreCase(this.txtEmailId.getValue()))
		{
			if(!isValidEmail(this.txtEmailId.getValue()))
			{
				hasError = true;
				eMsg.append("Please enter a valid email </br>");
			}
		}
		
		/*if((null != this.txtHospitalizationClaimedAmt && 
				null != this.txtHospitalizationClaimedAmt.getValue() && !("").equalsIgnoreCase(this.txtHospitalizationClaimedAmt.getValue())) ||
				((null != this.txtPreHospitalizationClaimedAmt && null != this.txtPreHospitalizationClaimedAmt.getValue() &&
				!("").equalsIgnoreCase(this.txtPreHospitalizationClaimedAmt.getValue()))) ||
				((null != this.txtPostHospitalizationClaimedAmt && null != this.txtPostHospitalizationClaimedAmt.getValue() &&
				!("").equalsIgnoreCase(this.txtPostHospitalizationClaimedAmt.getValue()))) ||
				((null != this.txtOtherBenefitClaimedAmnt && null != this.txtOtherBenefitClaimedAmnt.getValue() && 
				!("").equalsIgnoreCase(this.txtOtherBenefitClaimedAmnt.getValue()))))
		{
		
			if(null != this.optDocumentVerified && null == this.optDocumentVerified.getValue()){
			
				hasError = true;
				eMsg.append("Please select document verified option");
			}
		}*/
		/*if((null == this.bean.getDocumentDetails().getHospitalization() || null == this.bean.getDocumentDetails().getPreHospitalization() || null == this.bean.getDocumentDetails().getPostHospitalization()
				|| null == this.bean.getDocumentDetails().getPartialHospitalization() || null == this.bean.getDocumentDetails().getLumpSumAmount() || null == this.bean.getDocumentDetails().getAddOnBenefitsHospitalCash()
				|| null == this.bean.getDocumentDetails().getAddOnBenefitsPatientCare()))
		{
			hasError = true;
			eMsg += "Please select any one bill classification value";
		}*/
		
		//if(!isReconsiderRequestFlag)
		//{
		if(null != this.cmbReconsiderationRequest )
		{
			SelectValue reconsiderReqValue = (SelectValue)cmbReconsiderationRequest.getValue();
			if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("NO").equalsIgnoreCase(reconsiderReqValue.getValue()))
			{
				if(!((null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) || 
						(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) ||
						
						(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue()) ||
						
						(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue()) ||
						
						(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue()) ||
						(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue()) ||
						
						(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue()) ||
						
						(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue()) || 
						
						(null != this.chkOtherBenefits && null != this.chkOtherBenefits.getValue() && this.chkOtherBenefits.getValue()) ||
						
						(null != this.chkHospitalCash && null != this.chkHospitalCash.getValue() && this.chkHospitalCash.getValue()))
						)
					
				{
					hasError = true;
					eMsg.append("Please select any one bill classification value </br>");
				}
				
				if(null != this.chkOtherBenefits && null != this.chkOtherBenefits.getValue() && this.chkOtherBenefits.getValue())
				{
					if(!((null != this.chkEmergencyMedicalEvaluation && null != this.chkEmergencyMedicalEvaluation.getValue() && this.chkEmergencyMedicalEvaluation.getValue()) ||
						(null != this.chkCompassionateTravel && null != this.chkCompassionateTravel.getValue() && this.chkCompassionateTravel.getValue()) ||
						(null != this.chkRepatriationOfMortalRemains && null != this.chkRepatriationOfMortalRemains.getValue() && this.chkRepatriationOfMortalRemains.getValue()) ||
						(null != this.chkPreferredNetworkHospital && null != this.chkPreferredNetworkHospital.getValue() && this.chkPreferredNetworkHospital.getValue()) ||
						(null != this.chkSharedAccomodation && null != this.chkSharedAccomodation.getValue() && this.chkSharedAccomodation.getValue())))
					{
						
						hasError = true;
						eMsg.append("Please select any one of the benefits </br>");
						
					}
				}
			}
			
			
			if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("Yes").equalsIgnoreCase(reconsiderReqValue.getValue().trim()))
			{
				isReconsiderationRequest = true;
				
				if(this.bean.getReconsiderRODdto() == null){
					this.bean.setReconsiderRODdto(new ReconsiderRODRequestTableDTO());
				}
				
				this.bean.getReconsiderRODdto().setPatientStatus(reconsiderDTO != null ? reconsiderDTO.getPatientStatus() : null);
				
				if(null != optPaymentCancellation && null == optPaymentCancellation.getValue())
				{
					hasError = true;
					eMsg.append("Please choose payment cancellation </br>");
				}
			}
			
//			IMSSUPPOR-29002
			if(cmbDocumentsReceivedFrom != null && cmbDocumentsReceivedFrom.getValue() != null && (this.bean.getReconsiderRODdto() != null && this.bean.getReconsiderRODdto().getSelect() != null && this.bean.getReconsiderRODdto().getSelect())){
				
			    SelectValue selectValue =(SelectValue)cmbDocumentsReceivedFrom.getValue();
				
				
				if(this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId() != null 
						&& this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					
					if(this.bean.getReconsiderRODdto().getDocumentReceivedFrom() != null && selectValue != null && selectValue.getValue().equalsIgnoreCase("Hospital")&&
							(this.bean.getReconsiderRODdto().getHospitalizationFlag() != null && ! this.bean.getReconsiderRODdto().getHospitalizationFlag().equalsIgnoreCase("Y")
									)){
						hasError = true;
						eMsg.append("Document Received from can not be Hospital for classification type other than Hospital");
						
					}
				}
				
				if(isReconsiderationRequest
						&& reconsiderDTO != null 
						&& (SHAConstants.DEATH_FLAG.equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
										|| (reconsiderDTO.getPatientStatus() != null
												&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(reconsiderDTO.getPatientStatus().getId())
														|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reconsiderDTO.getPatientStatus().getId()))))
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(selectValue.getId())
						&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
					
					hasError = validateNomineeDetails(hasError, eMsg);							
					
				}
				
			}
			
			else if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			
			if(cmbDocumentsReceivedFrom != null && cmbDocumentsReceivedFrom.getValue() != null){
				
			    SelectValue selectValue =(SelectValue)cmbDocumentsReceivedFrom.getValue();				
				
				if(this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId() != null 
						&& this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					
					if(selectValue != null && selectValue.getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)&&
							(null != chkhospitalization &&  ((null == chkhospitalization.getValue()) 
									|| (chkhospitalization.getValue() != null && !chkhospitalization.getValue())))){
						hasError = true;
						eMsg.append("Document Received from can not be Hospital for classification type other than Hospital </br>");
						
					} 
				}
			  }
			
			//IMSSUPPOR-28062
			if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("YES").equalsIgnoreCase(reconsiderReqValue.getValue().trim()))
			{
				if(null ==  reconsiderRODRequestList || reconsiderRODRequestList.isEmpty())
				{
					hasError = true;
					eMsg.append("No Claim is available for reconsideration. Please reset Reconsideration request to NO to proceed further </br>");
				}
//				IMSSUPPOR-29002
				else if(null == this.bean.getReconsiderRODdto() || (this.bean.getReconsiderRODdto().getSelect() == null || !this.bean.getReconsiderRODdto().getSelect()))
				{
					{
						hasError = true;
						eMsg.append("Please select any one earlier rod for reconsideration. If not, then reset reconsideration request to NO to proceed further </br>");
					}
				}
				
				if(!(null != optPaymentCancellation && null != optPaymentCancellation.getValue()))
				{
					hasError = true;
					eMsg.append("Please select payment cancellation needed since reconsideration request is yes </br>");
				}
			}
			
			}
			
			/*if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("Yes").equalsIgnoreCase(reconsiderReqValue.getValue().trim())){
			
				if(cmbDocumentsReceivedFrom != null && cmbDocumentsReceivedFrom.getValue() != null && chkhospitalization != null && chkhospitalization.getValue()){
					
				    SelectValue selectValue =(SelectValue)cmbDocumentsReceivedFrom.getValue();
					if(this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId() != null 
							&& this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
						if(selectValue.getValue().equalsIgnoreCase("Hospital")){
							hasError = true;
							eMsg.append("Reconsideration is not possible for Hospital");
						}
						
					}
				}
			}*/
			
			
			else if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("YES").equalsIgnoreCase(reconsiderReqValue.getValue().trim()))
			{
				if(null ==  reconsiderRODRequestList || reconsiderRODRequestList.isEmpty())
				{
					hasError = true;
					eMsg.append("No Claim is available for reconsideration. Please reset Reconsideration request to NO to proceed further </br>");
				}
//				IMSSUPPOR-29002
				else if(null == this.bean.getReconsiderRODdto() || (this.bean.getReconsiderRODdto().getSelect() == null || !this.bean.getReconsiderRODdto().getSelect()))
				{
					//ReconsiderRODRequestTableDTO reconsiderDTO = this.bean.getReconsiderRODdto();
					//if(!(null != this.reconsiderDTO.getSelect() && this.reconsiderDTO.getSelect()))
					{
						hasError = true;
						eMsg.append("Please select any one earlier rod for reconsideration. If not, then reset reconsideration request to NO to proceed further </br>");
					}
				}
				
				if(!(null != optPaymentCancellation && null != optPaymentCancellation.getValue()))
				{
					hasError = true;
					eMsg.append("Please select payment cancellation needed since reconsideration request is yes </br>");
				}
			}
		}
		//}
		
		/*if(null != this.cmbReconsiderationRequest )
		{
			SelectValue reconsiderReqValue = (SelectValue)cmbReconsiderationRequest.getValue();
			if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("NO").equalsIgnoreCase(reconsiderReqValue.getValue()))
			{
				if((null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) || 
						(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue())
						)
				{
					if(!(null != this.txtHospitalizationClaimedAmt && null != this.txtHospitalizationClaimedAmt.getValue() && !("").equalsIgnoreCase(this.txtHospitalizationClaimedAmt.getValue())))
					{
						hasError = true;
						eMsg += "Please enter hospitalization claimed amount </br>";
					}
				}
				if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
				{
					if(!(null != this.txtPreHospitalizationClaimedAmt && null != this.txtPreHospitalizationClaimedAmt.getValue() && !("").equalsIgnoreCase(this.txtPreHospitalizationClaimedAmt.getValue())))
					{
						hasError = true;
						eMsg += "Please enter Pre hospitalization claimed amount </br>";
					}
				}
				if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
				{
					if(!(null != this.txtPostHospitalizationClaimedAmt && null != this.txtPostHospitalizationClaimedAmt.getValue() && !("").equalsIgnoreCase(this.txtPostHospitalizationClaimedAmt.getValue())))
					{
						hasError = true;
						eMsg += "Please enter Post hospitalization claimed amount </br>";
					}
				}
				
			}
		}*/
			
		
		/*if((null == this.chkhospitalization || null == this.chkPartialHospitalization || null == this.chkPreHospitalization
				|| null == this.chkPostHospitalization|| null == this.chkLumpSumAmount || null == this.chkAddOnBenefitsHospitalCash
				|| null == this.chkAddOnBenefitsPatientCare))
		{
			hasError = true;
			eMsg += "Please select any one bill classification value";
		}*/
			
		
		/*if(!(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) || (null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue()) 
				|| ( null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) || (null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue())
				|| (null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue()) || (null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue()))
		{
			hasError = true;
			eMsg += "Please select any one bill classification value";
		}*/
		
		if(null != this.cmbDocumentsReceivedFrom) {
			SelectValue selValue = (SelectValue)this.cmbDocumentsReceivedFrom.getValue();
			SelectValue selValue1 = null;
			if(null != cmbModeOfReceipt)
			{
				 selValue1 = (SelectValue)this.cmbModeOfReceipt.getValue();
			}
			
			if(((null!= selValue) &&("Insured").equalsIgnoreCase(selValue.getValue())) && ((null != selValue1) && ("In Person").equalsIgnoreCase(selValue1.getValue())))
			{
				if(!(null != this.txtAcknowledgementContactNo && null != this.txtAcknowledgementContactNo.getValue() && !("").equalsIgnoreCase(this.txtAcknowledgementContactNo.getValue())))
				{
					hasError = true;
					eMsg.append("Please enter Acknowledgement Contact No</br>");
				}
				if((!(null != this.txtEmailId && null != this.txtEmailId.getValue() && !("").equalsIgnoreCase(this.txtEmailId.getValue()))))
				{
					hasError = true;
					eMsg.append("Please enter email Id </br>");
				}
			}
			
			/**
			 * Since the binding of acknowledgement contact number didn't go through well,
			 * we had added the below manual validation.
			 * */

			/*if(((null!= selValue) &&("Hospital").equalsIgnoreCase(selValue.getValue())) && ((null != selValue1) && ("In Person").equalsIgnoreCase(selValue1.getValue()))
			 )
>>>>>>> 9bb04c950f3aa31aa1960f5df2a960fc5b653b7f
			{
				if(!(null != this.txtAcknowledgementContactNo && null != this.txtAcknowledgementContactNo.getValue() && !("").equalsIgnoreCase(this.txtAcknowledgementContactNo.getValue())))
				{
					hasError = true;
					eMsg += "Please enter Acknowledgement Contact No</br>";
				}
			}*/
			 
		}
		/*if(null != this.documentCheckList)
		{
			Boolean isValid = documentCheckList.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.documentCheckList.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
		}*/
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				
				if(cmbDocumentsReceivedFrom != null && cmbDocumentsReceivedFrom.getValue() != null){
					
				    SelectValue selectValue =(SelectValue)cmbDocumentsReceivedFrom.getValue();				
					
					if(this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId() != null 
							&& this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
						
						if(selectValue != null && selectValue.getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)&&
								(chkOtherBenefits != null && chkOtherBenefits.getValue() != null && chkOtherBenefits.getValue())){
							if(! bean.getDocumentDetails().getIsOtherBenefitApplicableInPreauth()){
							hasError = true;
							eMsg.append("Other Benefits is not applicable. Since It is not applied in Cashless</br>");
							}else{
								if(chkEmergencyMedicalEvaluation != null && chkEmergencyMedicalEvaluation.getValue() != null && chkEmergencyMedicalEvaluation.getValue() && ! this.bean.getDocumentDetails().getIsEmergencyMedicalEvacuation()){
									hasError = true;
									eMsg.append("Emergency Medical Evaluation is not applicable. Since It is not applied in Cashless</br>");
								}
								if(chkRepatriationOfMortalRemains != null && chkRepatriationOfMortalRemains.getValue() != null && chkRepatriationOfMortalRemains.getValue() && ! this.bean.getDocumentDetails().getIsRepatriationOfMortal()){
									hasError = true;
									eMsg.append("Repatriation Of Mortal Remains is not applicable. Since It is not applied in Cashless</br>");
								}
								
								if(chkEmergencyMedicalEvaluation != null && chkEmergencyMedicalEvaluation.getValue() != null && ! chkEmergencyMedicalEvaluation.getValue() && this.bean.getDocumentDetails().getIsEmergencyMedicalEvacuation()){
									hasError = true;
									eMsg.append("Please select Emergency Medical Evaluation. Since It is applied in Cashless</br>");
								}
								if(chkRepatriationOfMortalRemains != null && chkRepatriationOfMortalRemains.getValue() != null && ! chkRepatriationOfMortalRemains.getValue() && this.bean.getDocumentDetails().getIsRepatriationOfMortal()){
									hasError = true;
									eMsg.append("Please select Repatriation Of Mortal Remains. Since It is applied in Cashless");
								}
								if(chkRepatriationOfMortalRemains != null && chkRepatriationOfMortalRemains.getValue() == null && this.bean.getDocumentDetails().getIsRepatriationOfMortal()){
									hasError = true;
									eMsg.append("Please select Repatriation Of Mortal Remains. Since It is applied in Cashless");
								}
								
							}
						}else if(selectValue != null && selectValue.getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)&&
								(chkOtherBenefits != null && chkOtherBenefits.getValue() != null && ! chkOtherBenefits.getValue())){
							if(bean.getDocumentDetails().getIsOtherBenefitApplicableInPreauth()){
								hasError = true;
								eMsg.append("Please select Other benefit,  Since It is applied in Cashless");
							}
						}
						
						
					}
				  }
				}
		
		if(null != this.documentCheckListValidation)
		{
			Boolean isValid = documentCheckListValidation.validatePageForAckScreen();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.documentCheckListValidation.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		if(!isReconsiderationRequest)
		{
			if(null != this.rodQueryDetails)
			{
				Boolean isValid = rodQueryDetails.isValid();
				if (!isValid) {
					hasError = true;
					List<String> errors = this.rodQueryDetails.getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
				}
			}
		}
		
		// Section details included for Comprehensive product. Remaining products, the Hospitalization will be the default value.........
		if(this.sectionDetailsListenerTableObj != null && !sectionDetailsListenerTableObj.isValid()) {
			hasError = true;
			List<String> errors = this.sectionDetailsListenerTableObj.getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		}
		
		//IMSSUPPOR-23596
		Boolean isQueryRod = Boolean.FALSE;
		List<RODQueryDetailsDTO> rodQueryDetailsDTO = this.rodQueryDetails.getValues();
		if(null != rodQueryDetailsDTO && !rodQueryDetailsDTO.isEmpty())
		{
			for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO) {
				
				if(null != rodQueryDetailsDTO2.getReplyStatus() && ("Yes").equals (rodQueryDetailsDTO2.getReplyStatus().trim()))
				{
					isQueryRod = Boolean.TRUE;
					break;
				}
			}
		}
		
		
		if(!isQueryRod && this.bean.getReconsiderRODdto() == null && (null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) && this.bean.getIsAlreadyHospitalizationExist()){
			hasError = true;
			eMsg.append("Already hospitalization is existing for this claim.<br>");
		}
		
		SelectValue selectValue =(SelectValue)cmbDocumentsReceivedFrom.getValue();
		
		if(isQueryReplyReceived
				&& (SHAConstants.DEATH_FLAG.equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
						|| (bean.getPreauthDTO().getPreauthDataExtractionDetails().getPatientStatus() != null
				&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getPreauthDTO().getPreauthDataExtractionDetails().getPatientStatus().getId())
						|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getPreauthDTO().getPreauthDataExtractionDetails().getPatientStatus().getId()))))
				&& selectValue.getValue().equalsIgnoreCase("insured")
				&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
			
			bean.setQueryReplyStatus(isQueryReplyReceived);
			hasError = validateNomineeDetails(hasError, eMsg);
			
		}
		
		if(null != this.cmbReconsiderationRequest && null != this.cmbReconsiderationRequest.getValue() && ("YES").equalsIgnoreCase(this.cmbReconsiderationRequest.getValue().toString())
				&& null != reconsiderRODRequestList && !reconsiderRODRequestList.isEmpty())
		{
			List<Long> ackKeygetTaskList = new ArrayList<Long>();
			List<Long> ackKeyList = new ArrayList<Long>();
			List<Long> rodstatusList = new ArrayList<Long>();
			rodstatusList.add(ReferenceTable.FINANCIAL_SETTLED);
			rodstatusList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS);
			rodstatusList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);
			//IMSSUPPOR-29815
			rodstatusList.add(ReferenceTable.PAYMENT_REJECTED);
			
			for (ReconsiderRODRequestTableDTO reconsiderTable : reconsiderRODRequestList){
				if(reconsiderTable!=null && reconsiderTable.getSelect()!=null && reconsiderTable.getSelect().equals(Boolean.TRUE)){
					if(reconsiderTable.getRodNo()!=null){
						Reimbursement rodObj=createRodService.getLatestReimbursementByRodNumber(reconsiderTable.getRodNo());
						List<DocAcknowledgement> docAckList =createRodService.getListOfAcknowledgReconsider(rodObj.getClaim().getKey());
						if(docAckList!=null && !docAckList.isEmpty()){
							for(DocAcknowledgement docAcknowledgObj :docAckList){
								ackKeyList.add(docAcknowledgObj.getKey());
							}
						}
						List<Map<String, Object>> taskProcedure= createRodService.getDBTaskForPreauth(rodObj.getClaim().getIntimation(),SHAConstants.ROD_CURRENT_KEY,rodObj.getClaim());
						for (Map<String, Object> map : taskProcedure) {
							Long ackKey = (Long)map.get(SHAConstants.PAYLOAD_ACK_KEY);
							ackKeygetTaskList.add(ackKey);
						}
						if(rodObj!=null && rodObj.getStatus()!=null && rodObj.getStatus().getKey()!=null){
							//IMSSUPPOR-29214 added for cancel rod status
							if(!(rodstatusList.contains(rodObj.getStatus().getKey()) || ReferenceTable.getCancelRODKeys().containsKey(rodObj.getStatus().getKey()))){
								hasError = true;
								eMsg.append(" Reconsideration in progress for this rod </br>");
							}else{
								if(ackKeygetTaskList!=null && !ackKeygetTaskList.isEmpty()){
									for(Long newackKer :ackKeygetTaskList){
										if(ackKeyList.contains(newackKer)){
											hasError = true;
											eMsg.append(" Reconsideration in progress for this rod </br>");
										}

									}
								}
							}
						}
					}
				}

			}
		}
		
		if (hasError) {
			setRequired(true);
			/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);*/
			/*ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
			hasError = false;
			//return !hasError;
		} 
		else 
		{
			try {
				this.binder.commit();
				if(null != cmbReasonForReconsideration && null != cmbReasonForReconsideration.getValue())
				{	
					SelectValue selValue = (SelectValue) cmbReasonForReconsideration.getValue();
					bean.getDocumentDetails().setReasonForReconsideration(selValue);
				}
				if(null != txtHospitalizationClaimedAmt)
					hospitalizationClaimedAmt = txtHospitalizationClaimedAmt.getValue();
				
				if(null != txtPreHospitalizationClaimedAmt)
					preHospitalizationAmt = txtPreHospitalizationClaimedAmt.getValue();
				
				if(null != txtPostHospitalizationClaimedAmt)
					postHospitalizationAmt = txtPostHospitalizationClaimedAmt.getValue();
				
				if(null != txtOtherBenefitClaimedAmnt){
					otherBenefitsAmnt = txtOtherBenefitClaimedAmnt.getValue();
				}
				
				if(!this.sectionDetailsListenerTableObj.getValues().isEmpty()) {
					bean.setSectionDetailsDTO(this.sectionDetailsListenerTableObj.getValues().get(0));
				}
				
				
				
					
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			//return true;
			hasError = true;
		}
		//CR2019161 - Consent Letter from Insured for Investigation of Reimbursement Claims
		 SelectValue docReceivedFrom = (SelectValue) cmbDocumentsReceivedFrom.getValue();
		 if (chkhospitalization.getValue() !=null && bean.getClaimDTO().getClaimType() !=null 
				 &&  bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)
				 && docReceivedFrom !=null					
				 && docReceivedFrom.getValue().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_INSURED))
		 {
			 List<DocumentCheckListDTO> docCheckList = documentCheckListValidation.getValues();
				if(null != docCheckList && !docCheckList.isEmpty())
				{
					for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
						if(documentCheckListDTO.getKey() !=null && documentCheckListDTO.getParticulars().getId().equals(ReferenceTable.DOC_TYPE_CONTENT_LETTER)
								&& !(documentCheckListDTO.getReceivedStatus().getId().equals(ReferenceTable.ACK_DOC_RECEIVED_ORIGINAL) || 
										(documentCheckListDTO.getReceivedStatus().getId().equals(ReferenceTable.ACK_DOC_RECEIVED_PHOTOCOPY))))
						{
							String contentLetter = new String("Note: Consent letter for Seeking Information / Documents from Hospital is required to be submitted for smooth processing of your claim. Kindly submit the same at the earliest.");
							bean.setConsentAlert(contentLetter);
						}
						else if (documentCheckListDTO.getKey() !=null && !documentCheckListDTO.getParticulars().getId().equals(ReferenceTable.DOC_TYPE_CONTENT_LETTER)
								&& !(documentCheckListDTO.getReceivedStatus().getId().equals(ReferenceTable.ACK_DOC_RECEIVED_ORIGINAL) || 
										(documentCheckListDTO.getReceivedStatus().getId().equals(ReferenceTable.ACK_DOC_RECEIVED_PHOTOCOPY))))
						{
							String contentLetter = new String("Note: Consent letter for Seeking Information / Documents from Hospital is required to be submitted for smooth processing of your claim. Kindly submit the same at the earliest.");
							bean.setConsentAlert(contentLetter);
						}
						else
						{
							bean.setConsentAlert("");
						}
					}
				 }
			}
		
		return hasError;
	}

	private Boolean validateNomineeDetails(Boolean hasError, StringBuffer eMsg) {
		
		if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty()){
			List<NomineeDetailsDto> tableList = new ArrayList<NomineeDetailsDto>(); 
			
			if(nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty()){
				StringBuffer nomineeNames = new StringBuffer("");
				int selectCnt = 0;
				for (NomineeDetailsDto nomineeDetailsDto : nomineeDetailsTable.getTableList()) {
					nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
					if(nomineeDetailsDto.isSelectedNominee()) {
//						nomineeNames.append(nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeDetailsDto.getAppointeeName() : nomineeDetailsDto.getNomineeName()) : (nomineeDetailsDto.getAppointeeName() != null ? (", "+nomineeDetailsDto.getAppointeeName()) : (", "+nomineeDetailsDto.getNomineeName())));
						nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
					    selectCnt++;	
					}
					tableList.add(nomineeDetailsDto);
				}
				bean.getNewIntimationDTO().setNomineeList(tableList);
				bean.getNewIntimationDTO().setNomineeSelectCount(selectCnt);
				if(selectCnt>0){
					bean.getNewIntimationDTO().setNomineeName(nomineeNames.toString());
					bean.getNewIntimationDTO().setNomineeAddr(null);
				}
				else{
					bean.getNewIntimationDTO().setNomineeName(null);
					
					eMsg.append("Please Select Nominee<br>");
					hasError = true;						
				}							
			}
		}
		else{
			/*bean.getNewIntimationDTO().setNomineeList(null);
			bean.getNewIntimationDTO().setNomineeName(null);
			
			if((legalHeirNameTxt != null && (legalHeirNameTxt.getValue() == null || legalHeirNameTxt.getValue().isEmpty()))
					|| (legalHeirAddressTxt != null && (legalHeirAddressTxt.getValue() == null || legalHeirAddressTxt.getValue().isEmpty()))) {
				
				bean.getNewIntimationDTO().setNomineeName(null);
				bean.getNewIntimationDTO().setNomineeAddr(null);
				hasError = true;
				eMsg.append("Please Enter Claimant / Legal Heir Details");
				
			}
			
			if(legalHeirNameTxt != null 
					&& legalHeirNameTxt.getValue() != null
					&& legalHeirAddressTxt != null 
					&& legalHeirAddressTxt.getValue() != null) {
				bean.getNewIntimationDTO().setNomineeName(legalHeirNameTxt.getValue());
				bean.getNewIntimationDTO().setNomineeAddr(legalHeirAddressTxt.getValue());
			}*/
			

			bean.getNewIntimationDTO().setNomineeList(null);
			bean.getNewIntimationDTO().setNomineeName(null);
			
			if(this.legalHeirDetails.isValid()) {
				
				//added for support fix IMSSUPPOR-31323
				List<LegalHeirDTO> legalHeirList = new ArrayList<LegalHeirDTO>(); 
				legalHeirList.addAll(this.legalHeirDetails.getValues());
				if(legalHeirList != null && !legalHeirList.isEmpty()) {
					
					List<LegalHeirDTO> legalHeirDelList = legalHeirDetails.getDeletedList();
					
					for (LegalHeirDTO legalHeirDTO : legalHeirDelList) {
						legalHeirList.add(legalHeirDTO);
					}
					
					bean.getPreauthDTO().setLegalHeirDTOList(legalHeirList);
				}
				
			}
			else{
				bean.getPreauthDTO().setLegalHeirDTOList(null);
				hasError = true;
				eMsg.append("Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %)");
			}
				
		
		}
		return hasError;
	}
	
	public Boolean validateMisApproval(){
		Boolean hasError = true;
		if(this.bean.getRejectionDetails() != null && !this.bean.getRejectionDetails().isEmpty()){
			List<ReimbursementRejectionDto> rejectionDetails = this.bean.getRejectionDetails();
			List<ReconsiderRODRequestTableDTO> reconsiderRod = this.bean.getReconsiderRodRequestList();
			SelectValue docRecvd = (SelectValue) cmbDocumentsReceivedFrom.getValue();
			for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : reconsiderRod) {
					for (ReimbursementRejectionDto rejectionDetailsDto : rejectionDetails) {
						if(!(reconsiderRODRequestTableDTO.getIsSettledReconsideration()) &&
								docRecvd.getValue().equals(reconsiderRODRequestTableDTO.getDocumentReceivedFrom())) {
						if(reconsiderRODRequestTableDTO.getRodKey().equals(rejectionDetailsDto.getReimbursementDto().getKey())
								&&  rejectionDetailsDto.getAllowReconsider() != null && rejectionDetailsDto.getAllowReconsider().equals("Y")
								&& docRecvd.getValue().equals(reconsiderRODRequestTableDTO.getDocumentReceivedFrom())){
							hasError = false;
							break;
						} else {
							hasError = true;
						}
						} else {
							hasError = false;
							break;
						}
					} if(hasError) {
						break;
					}
			}

		} else {
			hasError = false;
		}
		return hasError;
	}
	
	public Boolean alertMessageForRejectionReconsider() {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.ALLOW_REJECTION_RECONSIDER + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");*/

		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(SHAConstants.ALLOW_REJECTION_RECONSIDER + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				wizard.getNextButton().setEnabled(false);
					
			}
		});
		return true;
	}
	
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}
	
	@SuppressWarnings("unused")
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}

	
	public void setDocumentCheckList(List<DocumentCheckListDTO> documentDetailsList)
	{
		documentCheckListValidation.setTableList(documentDetailsList);
	}
	
	
	private void setTableValues()
	{
		
		{
			if(null != this.documentCheckListValidation)
			{
				List<DocumentCheckListDTO> docCheckList = this.bean.getDocumentDetails().getDocumentCheckList();
				SelectValue selParticulars = null;
				SelectValue setReceivedStatus = null;
				for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
					//The below select value code needs to be removed before checkin
					if(!isNext)
					{
						
						selParticulars = new SelectValue();
						selParticulars.setId(documentCheckListDTO.getKey());
						selParticulars.setValue(documentCheckListDTO.getValue());
						documentCheckListDTO.setParticulars(selParticulars);
						setReceivedStatus = new SelectValue();
					/*setReceivedStatus.setId(264l);
					setReceivedStatus.setValue("Not Received");*/
						setReceivedStatus.setId(ReferenceTable.DOCUMENT_CHECKLIST_NOT_APPLICABLE);
						setReceivedStatus.setValue(SHAConstants.DOC_CHECKLIST_NOT_APPLICABLE);
					
						documentCheckListDTO.setReceivedStatus(setReceivedStatus);
					}
					//documentCheckListDTO.setRemarks("Test");
					this.documentCheckListValidation.addBeanToList(documentCheckListDTO);
				}
				
				/*List<DocumentCheckListDTO> docCheckList = this.documentCheckList.getValues();
				if(!docCheckList.isEmpty())
				{
					for (DocumentCheckListDTO docCheckListDTO : docCheckList) {
						this.documentCheckList.addBeanToList(docCheckListDTO);
					}
				}*/
				
			}
		}
	}
	
	private void resetDocumentCheckListTableForReconsiderationRequest()
	{
		List<DocumentCheckListDTO> docCheckList =  this.bean.getDocumentDetails().getDocumentCheckList();
		if(null != docCheckList && !docCheckList.isEmpty())
		{
			
			this.documentCheckListValidation.removeRow();
			SelectValue setReceivedStatus = null;
			for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
				
				if(!(("Hospital Final Bill").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Hospital Break-up Bill").equalsIgnoreCase(documentCheckListDTO.getValue())
						|| ("Investigation Reports - Lab Reports").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Investigation Reports - Films - X Rays / USG / ECHO / MRI / CT Scan / HPE").equalsIgnoreCase(documentCheckListDTO.getValue())
						|| ("Investigation Reports - Reports -X Rays / USG / ECHO / MRI / CT Scan / HPE").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Medicine Bills and Dr Prescriptions").equalsIgnoreCase(documentCheckListDTO.getValue())
						|| ("Pre-hospitalisation Bills").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Post hospitalisation Bills").equalsIgnoreCase(documentCheckListDTO.getValue()) 
						|| ("Letter from Insured (late Intimation or Delay Submission of bills or Reconsideration)").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("others").equalsIgnoreCase(documentCheckListDTO.getValue()))
				 )
				{
					setReceivedStatus = new SelectValue();
					setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
					setReceivedStatus.setValue("Not Applicable");
					documentCheckListDTO.setReceivedStatus(setReceivedStatus); 
					
				}	
				this.documentCheckListValidation.addBeanToList(documentCheckListDTO);
			}
		}
	}
	
	
	
	public void setDocStatusIfReplyReceivedForQuery(RODQueryDetailsDTO rodQueryDetails)
	{
		if(null != this.documentCheckListValidation)
		{
			/*this.chkhospitalization.setValue(false);
			this.chkPreHospitalization.setValue(false);
			this.chkPostHospitalization.setValue(false);
			this.chkLumpSumAmount.setValue(false);
			this.chkHospitalizationRepeat.setValue(false);
			this.chkPartialHospitalization.setValue(false);
			this.chkAddOnBenefitsHospitalCash.setValue(false);
			this.chkAddOnBenefitsPatientCare.setValue(false);*/
			//List<DocumentCheckListDTO> mainDocList = this.documentCheckList.getValues();
			
			//mainDocList .addAll(this.bean.getDocumentDetails().getDocumentCheckList());
		
			//List<DocumentCheckListDTO> docCheckList = documentCheckList.getValues();
		
			/**
			 * The below code is commented as an impact of R0364 - Document checklist table enhancement.
			 * Since mandatory flag column is removed, the below code doesn't have any effect. Hence
			 * commenting the same.
			 * **/
			
			/*List<DocumentCheckListDTO> docCheckList =  this.bean.getDocumentDetails().getDocumentCheckList();
			if(null != docCheckList && !docCheckList.isEmpty())
			{
				
				this.documentCheckListValidation.removeRow();
				for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
					
					if(!(("Hospital Final Bill").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Hospital Break-up Bill").equalsIgnoreCase(documentCheckListDTO.getValue())
							|| ("Investigation Reports - Lab Reports").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Investigation Reports - Films - X Rays / USG / ECHO / MRI / CT Scan / HPE").equalsIgnoreCase(documentCheckListDTO.getValue())
							|| ("Investigation Reports - Reports -X Rays / USG / ECHO / MRI / CT Scan / HPE").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Medicine Bills and Dr Prescriptions").equalsIgnoreCase(documentCheckListDTO.getValue())
							|| ("Pre-hospitalisation Bills").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Post hospitalisation Bills ").equalsIgnoreCase(documentCheckListDTO.getValue()) 
							|| ("Others").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Query Reply Documents").equalsIgnoreCase(documentCheckListDTO.getValue()))
					 )
					{
						SelectValue setReceivedStatus = new SelectValue();
						setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
						setReceivedStatus.setValue("Not Applicable");
						documentCheckListDTO.setReceivedStatus(setReceivedStatus); 
						
						
						
						
					}else{
						
						SelectValue setReceivedStatus = new SelectValue();
						setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
						setReceivedStatus.setValue("Not Applicable");
						documentCheckListDTO.setReceivedStatus(setReceivedStatus);
						
						SelectValue setReceivedStatus = new SelectValue();
						//setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
						setReceivedStatus.setValue("");					
						documentCheckListDTO.setReceivedStatus(setReceivedStatus);
						
						
					}
					this.documentCheckListValidation.addBeanToList(documentCheckListDTO);
				}
			}*/
		
		}
		if(null != rodQueryDetails)
		{
				if(("Yes").equalsIgnoreCase(rodQueryDetails.getReplyStatus()))
				{
					isQueryReplyReceived = true;
					
					this.chkhospitalization.setValue(false);
					this.chkPreHospitalization.setValue(false);
					this.chkPostHospitalization.setValue(false);
					this.chkPartialHospitalization.setValue(false);
					this.chkHospitalizationRepeat.setValue(false);
					this.chkLumpSumAmount.setValue(false);
					this.chkAddOnBenefitsHospitalCash.setValue(false);
					this.chkAddOnBenefitsPatientCare.setValue(false);
					this.chkOtherBenefits.setValue(false);
					this.optDocumentVerified.setValue(true);
					
					if(null != rodQueryDetails.getHospitalizationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getHospitalizationFlag()))
					{
						this.chkhospitalization.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getHospitalizationClaimedAmt()));
							txtHospitalizationClaimedAmt.setEnabled(false);
						}
						this.chkhospitalization.setEnabled(false);
					}
					if(null != rodQueryDetails.getPreHospitalizationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getPreHospitalizationFlag()))
					{
						this.chkPreHospitalization.setValue(true);
						if(null != rodQueryDetails.getPreHospitalizationClaimedAmt())
						{
							this.txtPreHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
							txtPreHospitalizationClaimedAmt.setEnabled(false);
						}
						this.chkPreHospitalization.setEnabled(false);
					}
					if(null != rodQueryDetails.getPostHospitalizationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getPostHospitalizationFlag()))
					{
						this.chkPostHospitalization.setValue(true);
						if(null != rodQueryDetails.getPostHospitalizationClaimedAmt())
						{
							this.txtPostHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPostHospitalizationClaimedAmt()));
							txtPostHospitalizationClaimedAmt.setEnabled(false);
						}
						this.chkPostHospitalization.setEnabled(false);
					}
					if(null != rodQueryDetails.getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getPartialHospitalizationFlag()))
					{
						this.chkPartialHospitalization.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getHospitalizationClaimedAmt()));
							txtHospitalizationClaimedAmt.setEnabled(false);
						}
						this.chkPartialHospitalization.setEnabled(false);
					}
					if(null != rodQueryDetails.getHospitalizationRepeatFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getHospitalizationRepeatFlag()))
					{
						this.chkHospitalizationRepeat.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getHospitalizationClaimedAmt()));
							txtHospitalizationClaimedAmt.setEnabled(false);
						}
						this.chkHospitalizationRepeat.setEnabled(false);
					}
					if(null != rodQueryDetails.getAddOnBenefitsLumpsumFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getAddOnBenefitsLumpsumFlag()))
					{
						this.chkLumpSumAmount.setValue(true);
						/*if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}*/
						this.chkLumpSumAmount.setEnabled(false);
					}
					if(null != rodQueryDetails.getAddOnBeneftisHospitalCashFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getAddOnBeneftisHospitalCashFlag()))
					{
						this.chkAddOnBenefitsHospitalCash.setValue(true);
						/*if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}*/
						this.chkAddOnBenefitsHospitalCash.setEnabled(false);
					}
					if(null != rodQueryDetails.getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getAddOnBenefitsPatientCareFlag()))
					{
						this.chkAddOnBenefitsPatientCare.setValue(true);
						/*if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}*/
						this.chkAddOnBenefitsPatientCare.setEnabled(false);
					}
					
					if(null != rodQueryDetails.getOtherBenefitsFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getOtherBenefitsFlag()))
					{
						
						this.chkOtherBenefits.setValue(true);
						setBenefitsValueForQuery(rodQueryDetails);
						
						if(null != rodQueryDetails.getOtherBenefitClaimedAmnt())
						{
							this.txtOtherBenefitClaimedAmnt.setValue(String.valueOf(rodQueryDetails.getOtherBenefitClaimedAmnt()));
							txtOtherBenefitClaimedAmnt.setEnabled(false);
						}
						this.chkOtherBenefits.setEnabled(false);
					}
					
					disableBillClassification();
					/*txtHospitalizationClaimedAmt.setEnabled(false);
					txtPreHospitalizationClaimedAmt.setEnabled(false);
					txtPostHospitalizationClaimedAmt.setEnabled(false);*/
				
					if(rodQueryDetails.getAcknowledgementContactNumber() != null){
					this.txtAcknowledgementContactNo.setValue(rodQueryDetails.getAcknowledgementContactNumber());
					}
					if(null != this.sectionDetailsListenerTableObj)
					{
						
						/**
						 * The below code is commented for ticket-5268.
						 * 
						 * */
						//subCoverBasedBillClassificationManipulation(sectionDetailsListenerTableObj.getSubCoverFieldValue(),this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey());

						this.sectionDetailsListenerTableObj.setEnabled(false);
					}
					
					
					SelectValue docRecFrom = cmbDocumentsReceivedFrom != null ? (SelectValue)cmbDocumentsReceivedFrom.getValue() : null;
						
					if((SHAConstants.DEATH_FLAG.equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
									|| (rodQueryDetails.getReimbursement().getPatientStatus() != null
											&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodQueryDetails.getReimbursement().getPatientStatus().getKey())
													|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodQueryDetails.getReimbursement().getPatientStatus().getKey()))))
							&& ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFrom.getId())
							&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
							&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
					
						if(rodQueryDetails != null) {
							bean.getNewIntimationDTO().setNomineeName(rodQueryDetails.getReimbursement().getNomineeName());
							bean.getNewIntimationDTO().setNomineeAddr(rodQueryDetails.getReimbursement().getNomineeAddr());
							bean.getPreauthDTO().getPreauthDataExtractionDetails().setPatientStatus(new SelectValue());
							bean.getPreauthDTO().getPreauthDataExtractionDetails().getPatientStatus().setId(rodQueryDetails.getReimbursement().getPatientStatus() != null ? rodQueryDetails.getReimbursement().getPatientStatus().getKey() : null);
							bean.getPreauthDTO().getPreauthDataExtractionDetails().getPatientStatus().setValue(rodQueryDetails.getReimbursement().getPatientStatus() != null ? rodQueryDetails.getReimbursement().getPatientStatus().getValue() : "");
						}
						
						if(nomineeDetailsTable != null) { 
							nomineeDetailsTable.setVisible(true);
							if(bean.getNewIntimationDTO().getNomineeList() != null && !bean.getNewIntimationDTO().getNomineeList().isEmpty()) {
								nomineeDetailsTable.setTableList(bean.getNewIntimationDTO().getNomineeList());
								nomineeDetailsTable.setViewColumnDetails();
								nomineeDetailsTable.generateSelectColumn();
							}
						}
						if(legalHeirLayout != null) {
							legalHeirLayout.setVisible(true);
						}
						if(bean.getPreauthDTO().getLegalHeirDTOList() != null && !bean.getPreauthDTO().getLegalHeirDTOList().isEmpty()){
							legalHeirDetails.addBeanToList(bean.getPreauthDTO().getLegalHeirDTOList());
						}
//							setLegalHeirDetails(bean.getNewIntimationDTO().getNomineeName() != null ? bean.getNewIntimationDTO().getNomineeName() : "", bean.getNewIntimationDTO().getNomineeAddr() != null ? bean.getNewIntimationDTO().getNomineeAddr() : "");
						
					}
					else{
							if(nomineeDetailsTable != null) { 
								nomineeDetailsTable.setVisible(false);
							}
							if(legalHeirLayout != null) {
								legalHeirLayout.setVisible(false);
							}
						}
					}
			else if(("No").equalsIgnoreCase(rodQueryDetails.getReplyStatus()))
			{
				isQueryReplyReceived = false;
				
				String docRecFromVal = null;
				if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docRecFrom = (SelectValue)cmbDocumentsReceivedFrom.getValue();
					docRecFromVal = docRecFrom.getValue();
				}
				if(null != chkhospitalization )//&& chkhospitalization.isEnabled())
				{
					if(null != docRecFromVal && (("Hospital").equalsIgnoreCase(docRecFromVal) || (("Insured").equalsIgnoreCase(docRecFromVal) && ("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))))
					{
						chkhospitalization.setEnabled(true);
						if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
						{
							chkhospitalization.setValue(false);
							txtHospitalizationClaimedAmt.setValue(null);
						}
					}
					
				}
				
				
				if((null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("preHospitalizationFlag"))))
				{
					if(null != chkPreHospitalization)
					{
						chkPreHospitalization.setEnabled(true);
						if(null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue())
						{
							chkPreHospitalization.setValue(false);
							txtPreHospitalizationClaimedAmt.setValue(null);
						}
					}
				}
				
				
				
				//chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalization", CheckBox.class);
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("postHospitalizationFlag")))
				{
					if(null != chkPostHospitalization)
					{
						chkPostHospitalization.setEnabled(true);
						if(null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue())
						{
							chkPostHospitalization.setValue(false);
							txtPostHospitalizationClaimedAmt.setValue(null);
						}
					}
				}
				
				/*if(null != chkPreHospitalization && chkPreHospitalization.isEnabled())
				{
					chkPreHospitalization.setEnabled(true);
					txtPreHospitalizationClaimedAmt.setValue(null);
				}
				if(null != chkPostHospitalization && chkPostHospitalization.isEnabled())
				{
					chkPostHospitalization.setEnabled(true);
					txtPostHospitalizationClaimedAmt.setValue(null);
				}*/
				if(null != chkPartialHospitalization)//&& chkPartialHospitalization.isEnabled())
				{
					if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && ("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
					{
						chkPartialHospitalization.setEnabled(true);	
						if(null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
						{
							chkPartialHospitalization.setValue(false);
							txtHospitalizationClaimedAmt.setValue(null);
						}
					}
					//txtHospitalizationClaimedAmt.setValue(null);
				}
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("LumpSumFlag")))
				{	if(null != chkLumpSumAmount)
					{
						chkLumpSumAmount.setEnabled(true);
						if(null != chkLumpSumAmount.getValue() && chkLumpSumAmount.getValue())
						{
							//chkLumpSumAmount.setValue(false);
							/**
							 * If false is set, then in the listener level, for false, bill
							 * classifications are enabled or disabled. Hence resetting to null
							 * which will not have any effect in listener level.
							 * */
							this.chkLumpSumAmount.setValue(null);
						}
					}
				}
				
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("hospitalCashFlag")))
				{
					if(null != chkAddOnBenefitsHospitalCash)
					{
						chkAddOnBenefitsHospitalCash.setEnabled(true);
						if(null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.getValue())
						{
							chkAddOnBenefitsHospitalCash.setValue(false);
						}
					}
				}
				
				
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("PatientCareFlag")))
				{
					//chkAddOnBenefitsPatientCare.setEnabled(true);
					if(null != chkAddOnBenefitsPatientCare)
					{
						chkAddOnBenefitsPatientCare.setEnabled(true);
						if(null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.getValue())
						{
							chkAddOnBenefitsPatientCare.setValue(false);
						}
					}
				}
				
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get(SHAConstants.OTHER_BENEFITS_FLAG)))
				{
					//chkAddOnBenefitsPatientCare.setEnabled(true);
					if(null != chkOtherBenefits)
					{
						chkOtherBenefits.setEnabled(true);
						if(null != chkOtherBenefits.getValue() && chkOtherBenefits.getValue())
						{
							chkOtherBenefits.setValue(false);
						}
					}
				}
				/*if(null != chkLumpSumAmount && chkLumpSumAmount.isEnabled())
				{
					chkLumpSumAmount.setEnabled(true);
					//txtHospitalizationClaimedAmt.setValue(null);
				}
				if(null != chkAddOnBenefitsHospitalCash && chkAddOnBenefitsHospitalCash.isEnabled())
				{
					chkAddOnBenefitsHospitalCash.setEnabled(true);
					//txtHospitalizationClaimedAmt.setValue(null);
				}
				if(null != chkAddOnBenefitsPatientCare && chkAddOnBenefitsPatientCare.isEnabled())
				{
					chkAddOnBenefitsPatientCare.setEnabled(true);
					//txtHospitalizationClaimedAmt.setValue(null);
				}*/
				
				if((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && (SHAConstants.DOC_RECEIVED_FROM_HOSPITAL).equalsIgnoreCase(docRecFromVal) && null != chkHospitalizationRepeat)
				{
					this.chkHospitalizationRepeat.setEnabled(false);
					chkHospitalizationRepeat.setValue(null);
				}

				else if(null != chkHospitalizationRepeat)
				{
					this.chkHospitalizationRepeat.setEnabled(true);
					chkHospitalizationRepeat.setValue(null);
				}
				resetDocCheckListTable();
				
				if(null != sectionDetailsListenerTableObj)
				{
					subCoverBasedBillClassificationManipulation(sectionDetailsListenerTableObj.getSubCoverFieldValue(),this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey());
					
					this.sectionDetailsListenerTableObj.setEnabled(true);
					
				}
				
				if(nomineeDetailsTable != null) { 
					nomineeDetailsTable.setVisible(false);
				}
				if(legalHeirLayout != null) {
					legalHeirLayout.setVisible(false);
				}
				
			}
		}
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.STAR_MICRO_RORAL_AND_FARMERS_CARE)) {
			chkAddOnBenefitsHospitalCash.setEnabled(false);
			chkAddOnBenefitsPatientCare.setEnabled(false);
			chkOtherBenefits.setEnabled(false);
			
		}
		
		if(chkAddOnBenefitsPatientCare != null && this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)) {
			chkAddOnBenefitsPatientCare.setEnabled(false);
			
		}
		
		//DR
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
//			chkhospitalization.setEnabled(false);
//			chkHospitalizationRepeat.setEnabled(false);
//			chkPreHospitalization.setEnabled(false);
//			chkLumpSumAmount.setEnabled(false);
//			chkPostHospitalization.setEnabled(false);
//			chkPartialHospitalization.setEnabled(false);
//			chkAddOnBenefitsHospitalCash.setEnabled(false);
//			chkAddOnBenefitsPatientCare.setEnabled(false);
//			chkOtherBenefits.setEnabled(false);
//			chkHospitalCash.setEnabled(true);
			if(chkhospitalization != null) chkhospitalization.setEnabled(false);
            if(chkHospitalizationRepeat!= null)chkHospitalizationRepeat.setEnabled(false);
            if(chkPreHospitalization!= null)chkPreHospitalization.setEnabled(false);
            if(chkLumpSumAmount!= null)chkLumpSumAmount.setEnabled(false);
            if(chkPostHospitalization!= null)chkPostHospitalization.setEnabled(false);
            if(chkPartialHospitalization!= null)chkPartialHospitalization.setEnabled(false);
            if(chkAddOnBenefitsHospitalCash!= null)chkAddOnBenefitsHospitalCash.setEnabled(false);
            if(chkAddOnBenefitsPatientCare!= null)chkAddOnBenefitsPatientCare.setEnabled(false);
            if(chkOtherBenefits!= null)chkOtherBenefits.setEnabled(false);
            if(chkHospitalCash!= null)chkHospitalCash.setEnabled(true);
            //IMSSUPPOR-36652
            if((rodQueryDetails.getReplyStatus() !=null && ("Yes").equalsIgnoreCase(rodQueryDetails.getReplyStatus())) 
            		&& (null != rodQueryDetails.getHospitalCashFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getHospitalCashFlag())))
			{
				this.chkHospitalCash.setValue(true);
				this.chkHospitalCash.setEnabled(false);
			}else if(rodQueryDetails.getReplyStatus() !=null && ("No").equalsIgnoreCase(rodQueryDetails.getReplyStatus())){
            	this.chkHospitalCash.setValue(false);
				this.chkHospitalCash.setEnabled(true);
            	
            }
			
		}
		
		
		
	}
	
	private void disableBillClassification()
	{
		List<Field<?>> chkBoxList = getListOfChkBox();
		if(null != chkBoxList && !chkBoxList.isEmpty())
		{
			for (Field<?> field : chkBoxList) {
				if(null != field)
				{
					field.setEnabled(false);
				}
			}
		}
		
	}
	
	public  void setTableValuesToDTO()
	{
		/**
		 * Get the list of DTO's first.
		 * Loop it and get individual object. And then assign them to dto and set this
		 * dto to list. This final list will be set in  bean again.
		 * 
		 * */
		
			List<DocumentCheckListDTO> objDocCheckList = this.documentCheckListValidation.getValues();
			if(null != objDocCheckList && !objDocCheckList.isEmpty())
			{
				for (DocumentCheckListDTO documentCheckListDTO : objDocCheckList) {
					if(null != documentCheckListDTO && null != documentCheckListDTO.getParticulars() && null != documentCheckListDTO.getParticulars().getValue())
					{
						documentCheckListDTO.setValue(documentCheckListDTO.getParticulars().getValue());
//						if(null != documentCheckListDTO.getKey())
//						{
							documentCheckListDTO.setParticulars(documentCheckListDTO.getParticulars());
//						}
					}
					
				}
			}
			this.bean.getDocumentDetails().setDocumentCheckList(objDocCheckList);
			
			List<RODQueryDetailsDTO> rodQueryDetailsDTO = this.rodQueryDetails.getValues();
			if(null != rodQueryDetailsDTO && !rodQueryDetailsDTO.isEmpty())
			{
				for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO) {
					
					this.bean.setRodqueryDTO(null);
					
					if(null != rodQueryDetailsDTO2.getReplyStatus() && ("Yes").equals (rodQueryDetailsDTO2.getReplyStatus().trim()))
					{
						this.bean.setRodqueryDTO(rodQueryDetailsDTO2);
						break;
					}
				}
			}		
			
	}
	
	public void setValuesFromDTO()
	{
		DocumentDetailsDTO documentDetails = this.bean.getDocumentDetails();
		if(null != documentDetails.getDocumentsReceivedFrom())
		{
			 for(int i = 0 ; i<docReceivedFromRequest.size() ; i++)
			 	{
					if ( documentDetails.getDocumentsReceivedFrom().getValue().equalsIgnoreCase(docReceivedFromRequest.getIdByIndex(i).getValue()))
					{
						this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
					}
				}
		}
		
		if(null != documentDetails.getDocumentsReceivedDate())
		{
			documentsReceivedDate.setValue(documentDetails.getDocumentsReceivedDate());
		}
		
		if(null != documentDetails.getModeOfReceipt())
		{
			 for(int i = 0 ; i<modeOfReceipt.size() ; i++)
			 	{
					if ( documentDetails.getModeOfReceipt().getValue().equalsIgnoreCase(modeOfReceipt.getIdByIndex(i).getValue()))
					{
						this.cmbModeOfReceipt.setValue(modeOfReceipt.getIdByIndex(i));
					}
				}
		}
		
		if(null != documentDetails.getReconsiderationRequest())
		{
			for(int i = 0 ; i<reconsiderationRequest.size() ; i++)
		 	{
				if ( documentDetails.getReconsiderationRequest().getValue().equalsIgnoreCase(reconsiderationRequest.getIdByIndex(i).getValue()))
				{
					this.cmbReconsiderationRequest.setValue(reconsiderationRequest.getIdByIndex(i));
				}
			}
		}
		if(null != documentDetails.getReasonForReconsideration())
		{
			this.cmbReasonForReconsideration.setReadOnly(false);
			for(int i = 0 ; i<reasonForReconsiderationRequest.size() ; i++)
		 	{
				if ( (reasonForReconsiderationRequest.getIdByIndex(i).getValue()).equalsIgnoreCase(documentDetails.getReasonForReconsideration().getValue()))
				{	
					this.cmbReasonForReconsideration.setValue(reasonForReconsiderationRequest.getIdByIndex(i));
					break;
				}
			}
			this.cmbReasonForReconsideration.setReadOnly(true);
		}
		
	}
	
	
	
	public void linkTableDTO(
			ReconsiderRODRequestTableDTO dto) {
		//if(reconsiderRequestDetails.validateSelectItems())
		//if(null != dto && dto.getSelect())
		{
			this.bean.setReconsiderRODdto(dto);
			this.reconsiderDTO = dto;
			setClaimedAmountField(dto);
			
			cmbReasonForReconsideration.setReadOnly(false);
			for(int i = 0; i<reasonForReconsiderationRequest.size();i++){
				if(dto.getIsSettledReconsideration()){
					if(reasonForReconsiderationRequest.getIdByIndex(i).getValue().equalsIgnoreCase("Reconsideration of settled claims")){	
						cmbReasonForReconsideration.setValue(reasonForReconsiderationRequest.getIdByIndex(i));
						break;
					}
				}
				else if(dto.getIsRejectReconsidered()){
					if(reasonForReconsiderationRequest.getIdByIndex(i).getValue().equalsIgnoreCase("Reconsideration of rejected claims")){
						cmbReasonForReconsideration.setValue(reasonForReconsiderationRequest.getIdByIndex(i));
						break;
					}
				}
			}
			cmbReasonForReconsideration.setReadOnly(true);
						
			resetDocumentCheckListTableForReconsiderationRequest();
			
			if(cmbDocumentsReceivedFrom != null && cmbDocumentsReceivedFrom.getValue() != null){
			
			    SelectValue selectValue =(SelectValue)cmbDocumentsReceivedFrom.getValue();
				
				
				if(this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId() != null 
						&& this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					
					if(dto.getDocumentReceivedFrom() != null && selectValue != null && selectValue.getValue().equalsIgnoreCase("Hospital")&&
							(dto.getHospitalizationFlag() != null && ! dto.getHospitalizationFlag().equalsIgnoreCase("Y"))){
						
						getErrorMessage("Document Received from can not be Hospital for classification type other than Hospital");
						
					}
				}
				
				if(reconsiderDTO != null 
						&& (SHAConstants.DEATH_FLAG.equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
								|| (reconsiderDTO.getPatientStatus() != null
										&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(reconsiderDTO.getPatientStatus().getId())
												|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reconsiderDTO.getPatientStatus().getId()))))
						&& selectValue.getValue().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_INSURED)
						&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
					
					if(nomineeDetailsTable != null) { 
						nomineeDetailsTable.setVisible(true);
						if(bean.getNewIntimationDTO().getNomineeList() != null && !bean.getNewIntimationDTO().getNomineeList().isEmpty()) {
							nomineeDetailsTable.setTableList(bean.getNewIntimationDTO().getNomineeList());
							nomineeDetailsTable.setViewColumnDetails();
							nomineeDetailsTable.generateSelectColumn();
						}
					}
					/*if(legalHeirLayout != null) {
							legalHeirLayout.setVisible(true);
							bean.getNewIntimationDTO().setNomineeName(reconsiderDTO.getLegalHeirName());
							bean.getNewIntimationDTO().setNomineeAddr(reconsiderDTO.getLegalHeirAddr());
//							setLegalHeirDetails(bean.getNewIntimationDTO().getNomineeName() != null ? bean.getNewIntimationDTO().getNomineeName() : "", bean.getNewIntimationDTO().getNomineeAddr() != null ? bean.getNewIntimationDTO().getNomineeAddr() : "");
					}*/
					
					if(legalHeirLayout != null && legalHeirDetails != null) {
						legalHeirLayout.setVisible(true);
						legalHeirDetails.addBeanToList(bean.getPreauthDTO().getLegalHeirDTOList());
					}
					
					}
			}		
		}
		
		//rebuildReconsiderLayout(dto);
		
		//reconsiderRequestDetails.disableTableItems(dto);
		
	}
	
	public void getErrorMessage(String eMsg){
		
	/*	Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}

	
	private void setClaimedAmountField(ReconsiderRODRequestTableDTO dto)
	{
		if(!reconsiderationMap.isEmpty())
		{
			reconsiderationMap.clear();
		}
		if(null != dto.getSelect() && dto.getSelect())
		{
			reconsiderationMap.put(dto.getAcknowledgementNo(), dto.getSelect());
			if((null != dto.getHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getHospitalizationFlag())) || 
					(null != dto.getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getPartialHospitalizationFlag())) ||
					(null != dto.getHospitalizationRepeatFlag() && ("Y").equalsIgnoreCase(dto.getHospitalizationRepeatFlag()))
					)
			{
				if(null!= txtHospitalizationClaimedAmt)
				{
					txtHospitalizationClaimedAmt.setEnabled(true);
					if(null != hospitalizationClaimedAmt && !("").equalsIgnoreCase(hospitalizationClaimedAmt))
					{
						txtHospitalizationClaimedAmt.setValue(hospitalizationClaimedAmt);
						
					}
					else
					{
						txtHospitalizationClaimedAmt.setValue(null != dto.getHospitalizationClaimedAmt() ? String.valueOf(dto.getHospitalizationClaimedAmt()) : "");
					}
				}
			}
			if(null != dto.getPreHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getPreHospitalizationFlag()))
					{
						if(null!= txtPreHospitalizationClaimedAmt)
						{
							txtPreHospitalizationClaimedAmt.setEnabled(true);
							if(null != preHospitalizationAmt && !("").equalsIgnoreCase(preHospitalizationAmt))
							{
								txtPreHospitalizationClaimedAmt.setValue(preHospitalizationAmt);
								
							}
							else
							{
								txtPreHospitalizationClaimedAmt.setValue(null != dto.getPreHospClaimedAmt() ? String.valueOf(dto.getPreHospClaimedAmt()) : "");
							}
							
						}
					}
			if(null != dto.getPostHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getPostHospitalizationFlag()))
			{
				if(null!= txtPostHospitalizationClaimedAmt)
				{
					txtPostHospitalizationClaimedAmt.setEnabled(true);
					if(null != postHospitalizationAmt && !("").equalsIgnoreCase(postHospitalizationAmt))
					{
						txtPostHospitalizationClaimedAmt.setValue(postHospitalizationAmt);
						
					}
					else
					{
						txtPostHospitalizationClaimedAmt.setValue(null != dto.getPostHospClaimedAmt() ? String.valueOf(dto.getPostHospClaimedAmt()) : "");
					}
				}
			}
			
			if(null != dto.getOtherBenefitFlag() && ("Y").equalsIgnoreCase(dto.getOtherBenefitFlag()))
			{
				if(null!= txtOtherBenefitClaimedAmnt)
				{
					txtOtherBenefitClaimedAmnt.setEnabled(true);
					if(null != otherBenefitsAmnt && !("").equalsIgnoreCase(otherBenefitsAmnt))
					{
						txtOtherBenefitClaimedAmnt.setValue(otherBenefitsAmnt);
						
					}
					else
					{
						txtOtherBenefitClaimedAmnt.setValue(null != dto.getOtherBenefitClaimedAmnt() ? String.valueOf(dto.getOtherBenefitClaimedAmnt()) : "");
					}
				}
			}
			
			/*if(null != dto.getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getPartialHospitalizationFlag()))
			{
				if(null!= txtHospitalizationClaimedAmt)
				{
					txtPostHospitalizationClaimedAmt.setEnabled(true);
					txtPostHospitalizationClaimedAmt.setValue(null != dto.getPostHospClaimedAmt() ? String.valueOf(dto.getPostHospClaimedAmt()) : "");
				}
			}*/
			
		}
		else
		{
			if(null!= txtHospitalizationClaimedAmt)
			{
				txtHospitalizationClaimedAmt.setEnabled(false);
				txtHospitalizationClaimedAmt.setValue(null);
			}
			if(null!= txtPreHospitalizationClaimedAmt)
			{
				txtPreHospitalizationClaimedAmt.setEnabled(false);
				txtPreHospitalizationClaimedAmt.setValue(null);
			}
			if(null!= txtPostHospitalizationClaimedAmt)
			{
				txtPostHospitalizationClaimedAmt.setEnabled(false);
				txtPostHospitalizationClaimedAmt.setValue(null);
			}
			if(null != txtOtherBenefitClaimedAmnt)
			{
				txtOtherBenefitClaimedAmnt.setEnabled(false);
				txtOtherBenefitClaimedAmnt.setValue(null);

			}
			hospitalizationClaimedAmt = "";
			preHospitalizationAmt = "";
			postHospitalizationAmt = "";
			otherBenefitsAmnt = "";
			this.bean.setReconsiderRODdto(null);
			//resetDocumentCheckListTable();
		//	this.reconsiderDTO = null;
		}
	}

	/*private void resetDocumentCheckListTable()
	{
		List<DocumentCheckListDTO> docCheckList =  this.bean.getDocumentDetails().getDocumentCheckList();
		if(null != docCheckList && !docCheckList.isEmpty())
		{
			
			this.documentCheckList.removeRow();
			for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
				
				if(!(("Hospital Final Bill").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Hospital Break-up Bill").equalsIgnoreCase(documentCheckListDTO.getValue())
						|| ("Investigation Reports - Lab Reports").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Investigation Reports - Films - X Rays / USG / ECHO / MRI / CT Scan / HPE").equalsIgnoreCase(documentCheckListDTO.getValue())
						|| ("Investigation Reports - Reports -X Rays / USG / ECHO / MRI / CT Scan / HPE").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Medicine Bills and Dr Prescriptions").equalsIgnoreCase(documentCheckListDTO.getValue())
						|| ("Pre-hospitalisation Bills").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Post hospitalisation Bills ").equalsIgnoreCase(documentCheckListDTO.getValue()) 
						|| ("Letter from Insured (late Intimation or Delay Submission of bills or Reconsideration)").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("others").equalsIgnoreCase(documentCheckListDTO.getValue()))
				 )
				{
					SelectValue setReceivedStatus = new SelectValue();
					setReceivedStatus.setId(266l);
					setReceivedStatus.setValue("Not Applicable");
					documentCheckListDTO.setReceivedStatus(setReceivedStatus); 
					
				}	
				this.documentCheckList.addBeanToList(documentCheckListDTO);
			}
		}
	}*/
	public Boolean validateBillClassification() 
	{
		Boolean isError = false;
		/**
		 * If query reply received, then isQueryReplyReceived
		 * will be true and as per below condition validation will be bypassed. 
		 * 
		 * */
		log.info("------Query reply received----------"+isQueryReplyReceived);

		if(!isQueryReplyReceived)
		{
			if(null != docDTO && !docDTO.isEmpty())
			{
				for (DocumentDetailsDTO documentDetailsDTO : docDTO) {
				
					if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) 
					{
						//if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()) && !(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS).equals(documentDetailsDTO.getStatusId()))
						{
						log.info("------hospitalization flag-----"+documentDetailsDTO.getHospitalizationFlag());
							if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()))
							{
								isError = true;
							}
						}
					}
					if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) 
					{
						//if(("Y").equalsIgnoreCase(documentDetailsDTO.getPartialHospitalizationFlag()) && !(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS).equals(documentDetailsDTO.getStatusId()))
						{
							if(documentDetailsDTO.getPartialHospitalizationFlag().equalsIgnoreCase("Y"))
							{
								isError = true;
							}
						}
					}
					/**
					 * Below validation is added for cancel rod scenario. If an hospitalization rod is cancelled and user tries to deselect hospitalization
					 * and select hospitalization repeat, then below validation will not allow user to create an hospitalization repeat rod, since hospitalization
					 * rod is not yet created. -- Added for #3768 
					 */
						/*if(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && null != this.chkHospitalizationRepeat.getValue())
						{
							//if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()) && (ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS).equals(documentDetailsDTO.getStatusId()))
								if(documentDetailsDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
								{
									isError = true;
								}
						}*/
				}
			}
			
			/*if(null != docDTO)
			{
				if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) 
				{
					if(docDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
					{
						isError = true;
					}
				}
				if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) 
				{
					if(docDTO.getPartialHospitalizationFlag().equalsIgnoreCase("Y"))
					{
						isError = true;
					}
				}
			}*/
			else
			{
				log.info("------In else block of validation method------------");

				if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) 
				{
					isError = false;
				}
				else if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() &&this.chkPartialHospitalization.getValue())
				{
					
					isError = false;
				}
				else if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && !this.chkhospitalization.getValue())
				{
					if((SHAConstants.CLAIMREQUEST_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && isFinalEnhancement)
					{
						isError = false;
					}
					else
					{
						if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
						{
							isError = true;
						}
						if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
						{
							isError = true;
						}
						if(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue())
						{
							isError = true;
						}
						if(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue())
						{
							isError = true;
						}
						if(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue())
						{
							isError = true;
						}
						if(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue())
						{
							isError = true;
						}
					}
					
				}
				else if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && !this.chkPartialHospitalization.getValue())
				{
					if((SHAConstants.CLAIMREQUEST_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && isFinalEnhancement)
					{
						isError = false;
					}
					else
					{
						if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
						{
							isError = true;
						}
						if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
						{
							isError = true;
						}
						if(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue())
						{
							isError = true;
						}
						if(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue())
						{
							isError = true;
						}
						if(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue())
						{
							isError = true;
						}
					}
				}
				else if((SHAConstants.CLAIMREQUEST_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && isFinalEnhancement)
				{
					isError = false;
				}
				else
				{
					isError = true;
				}
			}
		}
		return isError;
		
	}
	
	private Boolean isValidEmail(String strEmail)
	{
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern validEmailPattern = Pattern.compile(emailPattern);
		Matcher validMatcher = validEmailPattern.matcher(strEmail);
		return validMatcher.matches();
	}

	public void resetDocCheckListTable() {
		
	
		if(null != documentCheckListValidation)
		{
			List<DocumentCheckListDTO> docCheckList = this.bean.getDocumentDetails().getDocumentCheckList();
			if(null != docCheckList && !docCheckList.isEmpty())
			{
				this.documentCheckListValidation.removeRow();
				for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
					SelectValue setReceivedStatus = new SelectValue();
					setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
					setReceivedStatus.setValue("Not Applicable");
					documentCheckListDTO.setReceivedStatus(setReceivedStatus);
					this.documentCheckListValidation.addBeanToList(documentCheckListDTO);
				}
			}
		}
		
	}

	public void validateHospitalizationRepeat(Boolean isValid) {
		
		if(!isValid)
		{
		 /*Label label = new Label("Hospitalization Repeat can be allowed only if Hospitalization is approved for this claim", ContentMode.HTML);
			label.setStyleName("errMessage");*/
		 /*HorizontalLayout layout = new HorizontalLayout(
				 label);
			layout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			//dialog.setWidth("35%");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox("Hospitalization Repeat can be allowed only if Hospitalization is approved for this claim", buttonsNamewithType);
			chkHospitalizationRepeat.setValue(null);
		}
		else
		{
			 txtHospitalizationClaimedAmt.setEnabled(true);
			 if(null != chkhospitalization && chkhospitalization.getValue() != null && !chkhospitalization.getValue())
			 {
				 txtHospitalizationClaimedAmt.setValue(null);
			 }
			 
			 chkhospitalization.setEnabled(false);
			 //if(null != chkPreHospitalization && chkPreHospitalization.isEnabled())
			 chkPreHospitalization.setEnabled(false);
			 //if(null != chkPostHospitalization && chkPostHospitalization.isEnabled())
			 chkPostHospitalization.setEnabled(false);
			 //if(null != chkPartialHospitalization && chkPartialHospitalization.isEnabled())
			 chkPartialHospitalization.setEnabled(false);
			 //chkPartialHospitalization.setValue(null);
			 //if(null != chkLumpSumAmount && chkLumpSumAmount.isEnabled())
			 chkLumpSumAmount.setEnabled(false);
			 //if(null != chkAddOnBenefitsHospitalCash && chkAddOnBenefitsHospitalCash.isEnabled())
			 chkAddOnBenefitsHospitalCash.setEnabled(false);
			 //if(null != chkAddOnBenefitsPatientCare && chkAddOnBenefitsPatientCare.isEnabled())
			 chkAddOnBenefitsPatientCare.setEnabled(false);
		}
		
	}
	
	
	public void resetQueryReplyReceived()
	{
		isQueryReplyReceived = false;	
	}
	
	public void isNextClicked()
	{
		isNext = true;
	}
	
	
	private void validateLumpSumClassification(String classificationType,CheckBox chkBox)
	{
		Long claimKey = bean.getClaimDTO().getKey();
		Long intimationKey = bean.getClaimDTO().getNewIntimationDto().getKey();
		fireViewEvent(DocumentDetailsPresenter.VALIDATE_LUMPSUM_AMOUNT_CLASSIFICATION,claimKey, classificationType, chkBox,intimationKey);

	}

	public void validateLumpSumAmount(Boolean isValid,String classificationType,CheckBox checkBox) {
		// TODO Auto-generated method stub
		if(isValid && (SHAConstants.LUMPSUMAMOUNT).equalsIgnoreCase(classificationType))
		{
			 /*Label label = new Label("Lumpsum cannot be processed under this Intimation", ContentMode.HTML);
				label.setStyleName("errMessage");
			 HorizontalLayout layout = new HorizontalLayout(
						label);
				layout.setMargin(true);*/
				/*final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Errors");
				//dialog.setWidth("55%");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox("Lumpsum cannot be processed under this Intimation", buttonsNamewithType);
				chkLumpSumAmount.setValue(null);
				lumpSumValidationFlag = true;
		}
		else if(isValid)
		{
			/*Label label = new Label("Section I cannot be processed under this Intimation", ContentMode.HTML);
			label.setStyleName("errMessage");*/
		/* HorizontalLayout layout = new HorizontalLayout(
					label);
			layout.setMargin(true);
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			//dialog.setWidth("55%");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox("Section I cannot be processed under this Intimation", buttonsNamewithType);
			//chkLumpSumAmount.setValue(false);
			if(null != checkBox)
				checkBox.setValue(null);
			lumpSumValidationFlag = true;
		}
		else
		{
			lumpSumValidationFlag = false;
		}
		
	}
	
	private void enableOrDisableBillClassification(Boolean value)
	{
		if(null != chkhospitalization)
		{
			chkhospitalization.setEnabled(value);
			chkhospitalization.setValue(null);
			txtHospitalizationClaimedAmt.setEnabled(value);
			txtHospitalizationClaimedAmt.setValue(null);
		}
		if(null != chkPreHospitalization)
		{
			chkPreHospitalization.setEnabled(value);
			chkPreHospitalization.setValue(null);
			txtPreHospitalizationClaimedAmt.setEnabled(value);
			txtPreHospitalizationClaimedAmt.setValue(null);
		}
		if(null != chkPostHospitalization)
		{
			chkPostHospitalization.setEnabled(value);
			chkPostHospitalization.setValue(null);
			txtPostHospitalizationClaimedAmt.setEnabled(value);
			txtPostHospitalizationClaimedAmt.setValue(null);
		}
		if(null != chkPartialHospitalization)
		{
			chkPartialHospitalization.setEnabled(value);
			chkPartialHospitalization.setValue(null);
			txtHospitalizationClaimedAmt.setEnabled(value);
			txtHospitalizationClaimedAmt.setValue(null);
		}
		if(null != chkAddOnBenefitsHospitalCash)
		{
			chkAddOnBenefitsHospitalCash.setEnabled(value);
			chkAddOnBenefitsHospitalCash.setValue(null);
		}
		if(null != chkAddOnBenefitsPatientCare)
		{
			chkAddOnBenefitsPatientCare.setEnabled(value);
			chkAddOnBenefitsPatientCare.setValue(null);
		}
		if(null != chkHospitalizationRepeat)
		{
			chkHospitalizationRepeat.setEnabled(value);
			chkHospitalizationRepeat.setValue(null);
			txtHospitalizationClaimedAmt.setEnabled(value);
			txtHospitalizationClaimedAmt.setValue(null);
		}
	}
	
	public void resetLumpsumValidationFlag()
	{
		this.lumpSumValidationFlag = false;
	}
	
	
	public void setBenefitsValueForQuery(RODQueryDetailsDTO rodQueryDetails){
		
		if(null != chkEmergencyMedicalEvaluation && null != rodQueryDetails && null != rodQueryDetails.getEmergencyMedicalEvaluationFlag() &&
				(SHAConstants.YES_FLAG.equalsIgnoreCase(rodQueryDetails.getEmergencyMedicalEvaluationFlag()))){
			
			chkEmergencyMedicalEvaluation.setValue(true);			
		}

		if(null != chkCompassionateTravel && null != rodQueryDetails && null != rodQueryDetails.getCompassionateTravelFlag() &&
				(SHAConstants.YES_FLAG.equalsIgnoreCase(rodQueryDetails.getCompassionateTravelFlag()))){
			
			chkCompassionateTravel.setValue(true);			
		}
		

		if(null != chkRepatriationOfMortalRemains && null != rodQueryDetails && null != rodQueryDetails.getRepatriationOfMortalRemainsFlag() &&
				(SHAConstants.YES_FLAG.equalsIgnoreCase(rodQueryDetails.getRepatriationOfMortalRemainsFlag()))){
			
			chkRepatriationOfMortalRemains.setValue(true);			
		}
		

		if(null != chkPreferredNetworkHospital && null != rodQueryDetails && null != rodQueryDetails.getPreferredNetworkHospitalFlag() &&
				(SHAConstants.YES_FLAG.equalsIgnoreCase(rodQueryDetails.getPreferredNetworkHospitalFlag()))){
			
			chkPreferredNetworkHospital.setValue(true);			
		}
		

		if(null != chkSharedAccomodation && null != rodQueryDetails && null != rodQueryDetails.getSharedAccomodationFlag() &&
				(SHAConstants.YES_FLAG.equalsIgnoreCase(rodQueryDetails.getSharedAccomodationFlag()))){
			
			chkSharedAccomodation.setValue(true);			
		}
		
		chkEmergencyMedicalEvaluation.setEnabled(false);
		chkCompassionateTravel.setEnabled(false);
		chkRepatriationOfMortalRemains.setEnabled(false);
		chkPreferredNetworkHospital.setEnabled(false);
		chkSharedAccomodation.setEnabled(false);
		
	}
	
	public Boolean warnMessageForLumpSum() {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.LUMPSUM_WARNING_MESSAGE + "</b>",
				ContentMode.HTML);*/
   		//final Boolean isClicked = false;
		/*Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(SHAConstants.LUMPSUM_WARNING_MESSAGE + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				alertMessageForLumpSum();
			}
		});
		return true;
	}
	
	public Boolean alertMessageForLumpSum() {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.LUMPSUM_ALERT_MESSAGE + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");*/
	/*	HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");*/
		/*final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(SHAConstants.LUMPSUM_ALERT_MESSAGE + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
			}
		});
		return true;
	}
	
	 public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
	    	// TODO Auto-generated method stub
	    	sectionDetailsListenerTableObj.setCoverList(coverContainer);
	    	
	    }

	    public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {
	    	
	    	sectionDetailsListenerTableObj.setSubCoverList(subCoverContainer);
	    	
	    }
	    
		  private void enableOrDisableClassificationBasedOnsubCover(int i,String docRecValue)
		    {
		    	
		    	switch(i)
		    	{
		    		case 1:
		    			enableDisableHospAndPartialHosp(docRecValue);
		    			if(null != chkPreHospitalization)
		    				chkPreHospitalization.setEnabled(true);
		    			if(null != chkPostHospitalization)
		    				chkPostHospitalization.setEnabled(true);
		    			if(null != chkHospitalizationRepeat)
		    				chkHospitalizationRepeat.setEnabled(true);
		    			if(null != chkLumpSumAmount)
		    			{
		    				chkLumpSumAmount.setEnabled(false);
		    				chkLumpSumAmount.setValue(null);
		    			}
		    			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("hospitalCashFlag") && null != chkAddOnBenefitsHospitalCash))
		    			{
		    				chkAddOnBenefitsHospitalCash.setEnabled(false);
		    			}
		    			else if(null != chkAddOnBenefitsHospitalCash)
		    			{
		    				chkAddOnBenefitsHospitalCash.setEnabled(true);
		    			}
		    			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("PatientCareFlag") && null != chkAddOnBenefitsPatientCare))
		    			{
			    			chkAddOnBenefitsPatientCare.setEnabled(false);
//			    			chkAddOnBenefitsPatientCare.setValue(false);
		    			}else if(null != chkAddOnBenefitsPatientCare){
		    				chkAddOnBenefitsPatientCare.setEnabled(true);
		    			}
		    			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get(SHAConstants.OTHER_BENEFITS_FLAG)))
		    			{
		    				chkOtherBenefits.setEnabled(false);
		    			}
		    			
		    		break;
		    		
		    		case 2:
		    			enableDisableHospAndPartialHosp(docRecValue);
		    			if(null != chkPostHospitalization)
		    			{
		    				chkPostHospitalization.setEnabled(false);
		    				chkPostHospitalization.setValue(null);
		    			}
		    			
		    			if(null != chkPreHospitalization)
		    			{
		    				chkPreHospitalization.setEnabled(false);
		    				chkPreHospitalization.setValue(null);
		    			}
		    			
		    			if(null != chkHospitalizationRepeat)
		    			{
		    				chkHospitalizationRepeat.setEnabled(false);
		    				chkHospitalizationRepeat.setValue(null);
		    			}
		    			if(null != chkLumpSumAmount)
		    			{
		    				chkLumpSumAmount.setEnabled(false);
		    				chkLumpSumAmount.setValue(null);
		    			}
		    			if(null != chkAddOnBenefitsHospitalCash)
		    			{
		    				chkAddOnBenefitsHospitalCash.setEnabled(false);
		    				chkAddOnBenefitsHospitalCash.setValue(null);
		    			}
		    			if(null != chkAddOnBenefitsPatientCare)
		    			{
		    				chkAddOnBenefitsPatientCare.setEnabled(false);
		    				chkAddOnBenefitsPatientCare.setValue(null);
		    			}
		    		break;
		    		
		    		case 3:
		    			
		    			if(null != chkhospitalization)
		    			{
		    				chkhospitalization.setEnabled(false);
		    				chkhospitalization.setValue(null);
		    			}
		    			if(null != chkPartialHospitalization)
		    			{
		    				chkPartialHospitalization.setEnabled(false);
		    				chkPartialHospitalization.setValue(null);
		    			}
		    			if(null != chkPreHospitalization)
		    			{
		    				chkPreHospitalization.setEnabled(false);
		    				chkPreHospitalization.setValue(null);
		    			}
		    			if(null != chkPostHospitalization)
		    			{
		    				chkPostHospitalization.setEnabled(false);
		    				chkPostHospitalization.setValue(null);
		    			}
		    			if(null != chkHospitalizationRepeat)
		    			{
		    				chkHospitalizationRepeat.setEnabled(false);
		    				chkHospitalizationRepeat.setValue(null);
		    			}
		    			if(null != chkLumpSumAmount)
		    			{
		    				chkLumpSumAmount.setEnabled(false);
		    				chkLumpSumAmount.setValue(null);
		    			}
		    			if(null != chkAddOnBenefitsHospitalCash)
		    			{
		    				chkAddOnBenefitsHospitalCash.setEnabled(false);
		    				chkAddOnBenefitsHospitalCash.setValue(null);
		    			}
		    			if(null != chkAddOnBenefitsPatientCare)
		    			{
		    				chkAddOnBenefitsPatientCare.setEnabled(false);
		    				chkAddOnBenefitsPatientCare.setValue(null);
		    			}
		    		break;
		    		
		    		case 4:
		    			enableDisableHospAndPartialHosp(docRecValue);
		    			if(null != chkPreHospitalization)
		    			{
		    				chkPreHospitalization.setEnabled(true);
		    				chkPostHospitalization.setEnabled(true);
		    			}
		    			
		    			if(null != chkHospitalizationRepeat)
		    			{
		    				chkHospitalizationRepeat.setEnabled(false);
		    				chkHospitalizationRepeat.setValue(null);
		    			}
		    			if(null != chkLumpSumAmount)
		    			{
		    				chkLumpSumAmount.setEnabled(false);
		    				chkLumpSumAmount.setValue(null);
		    			}
		    			
		    			if(null != chkAddOnBenefitsHospitalCash)
		    			{
		    				chkAddOnBenefitsHospitalCash.setEnabled(false);
		    				chkAddOnBenefitsHospitalCash.setValue(null);
		    			}
		    			if(null != chkAddOnBenefitsPatientCare)
		    			{
		    				chkAddOnBenefitsPatientCare.setEnabled(false);
		    				chkAddOnBenefitsPatientCare.setValue(null);
		    			}
		    			
		    		break;
		    		
		    		case 5:
		    			if(null != chkhospitalization)
		    			{
		    				chkhospitalization.setEnabled(false);
		    				chkPartialHospitalization.setEnabled(false);
		    			}
		    			if(null != chkPreHospitalization)
		    			{
		    				chkPreHospitalization.setEnabled(false);
		    			}
		    			if(null != chkPostHospitalization)
		    			{
		    				chkPostHospitalization.setEnabled(false);
		    			}
		    			if(null != chkHospitalizationRepeat)
		    			{
		    				chkHospitalizationRepeat.setEnabled(false);
		    			}
		    			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("LumpSumFlag") && null != chkLumpSumAmount))
		    			{
		    				chkLumpSumAmount.setEnabled(false);
		    			}
		    			else if(null != chkLumpSumAmount)
		    			{
		    				chkLumpSumAmount.setEnabled(true);
		    			}
		    			if(null != chkAddOnBenefitsHospitalCash)
		    			{
		    				chkAddOnBenefitsHospitalCash.setEnabled(false);
		    			}
		    			if(null != chkAddOnBenefitsPatientCare)
		    			{
		    				chkAddOnBenefitsPatientCare.setEnabled(false);
		    			}
		    			if(null != chkhospitalization)
		    			{
		    				chkhospitalization.setValue(null);
		    			}
		    			if(null != chkPreHospitalization)
		    			{
		    				chkPreHospitalization.setValue(null);
		    			}
		    			if(null != chkPostHospitalization)
		    			{
		    				chkPostHospitalization.setValue(null);
		    			}
		    			if(null != chkPartialHospitalization)
		    			{
		    				chkPartialHospitalization.setValue(null);
		    			}
		    			if(null != chkHospitalizationRepeat)
		    			{
		    				chkHospitalizationRepeat.setValue(null);
		    			}
		    			if(null != chkAddOnBenefitsHospitalCash)
		    			{
		    				chkAddOnBenefitsHospitalCash.setValue(null);
		    				chkAddOnBenefitsPatientCare.setValue(null);
		    			}
		    			
		    			if(null != txtHospitalizationClaimedAmt)
		    			{
		    				txtHospitalizationClaimedAmt.setValue(null);
		    				txtHospitalizationClaimedAmt.setEnabled(false);
		    			}
		    			
		    			if(null != txtPreHospitalizationClaimedAmt)
		    			{
		    				txtPreHospitalizationClaimedAmt.setValue(null);
		    				txtPreHospitalizationClaimedAmt.setEnabled(false);
		    			}
		    			
		    			if(null != txtPostHospitalizationClaimedAmt)
		    			{
		    				txtPostHospitalizationClaimedAmt.setValue(null);
		    				txtPostHospitalizationClaimedAmt.setEnabled(false);
		    			}
		    			
		    		break;	
		    		
		    		case 6:
		    			enableDisableHospAndPartialHosp(docRecValue);
		    			if(null != chkPreHospitalization)
		    				chkPreHospitalization.setEnabled(true);
		    			if(null != chkPostHospitalization)
		    				chkPostHospitalization.setEnabled(true);
		    			if(null != chkHospitalizationRepeat)
		    				chkHospitalizationRepeat.setEnabled(true);
		    			if(null != chkLumpSumAmount)
		    			{
		    				chkLumpSumAmount.setEnabled(false);
		    				chkLumpSumAmount.setValue(null);
		    			}
		    			 if(null != chkAddOnBenefitsHospitalCash)
		    			{
		    				chkAddOnBenefitsHospitalCash.setEnabled(false);
		    				chkAddOnBenefitsHospitalCash.setValue(null);
		    			}
		    			if(null != chkAddOnBenefitsPatientCare)
		    			{
			    			chkAddOnBenefitsPatientCare.setEnabled(false);
			    			chkAddOnBenefitsPatientCare.setValue(null);
		    			}
		    		break;
		    		
		    		case 7:
		    			enableDisableHospAndPartialHosp(docRecValue);
		    			if(null != chkPreHospitalization)
		    				chkPreHospitalization.setEnabled(true);
		    			if(null != chkPostHospitalization)
		    				chkPostHospitalization.setEnabled(true);
		    			if(null != chkHospitalizationRepeat)
		    				chkHospitalizationRepeat.setEnabled(false);
		    			if(null != chkLumpSumAmount)
		    			{
		    				chkLumpSumAmount.setEnabled(false);
		    				chkLumpSumAmount.setValue(null);
		    			}
		    			 if(null != chkAddOnBenefitsHospitalCash)
		    			{
		    				chkAddOnBenefitsHospitalCash.setEnabled(false);
		    				chkAddOnBenefitsHospitalCash.setValue(null);
		    			}
		    			if(null != chkAddOnBenefitsPatientCare)
		    			{
			    			chkAddOnBenefitsPatientCare.setEnabled(false);
			    			chkAddOnBenefitsPatientCare.setValue(null);
		    			}
		    		break;
		    		
		    		case 8:
		    			enableDisableHospAndPartialHosp(docRecValue);
		    			if(null != chkPreHospitalization)
		    				chkPreHospitalization.setEnabled(false);
		    			if(null != chkPostHospitalization)
		    				chkPostHospitalization.setEnabled(false);
		    			if(null != chkHospitalizationRepeat)
		    				chkHospitalizationRepeat.setEnabled(false);
		    			if(null != chkLumpSumAmount)
		    			{
		    				chkLumpSumAmount.setEnabled(false);
		    				chkLumpSumAmount.setValue(null);
		    			}
		    			 if(null != chkAddOnBenefitsHospitalCash)
		    			{
		    				chkAddOnBenefitsHospitalCash.setEnabled(false);
		    				chkAddOnBenefitsHospitalCash.setValue(null);
		    			}
		    			if(null != chkAddOnBenefitsPatientCare)
		    			{
			    			chkAddOnBenefitsPatientCare.setEnabled(false);
			    			chkAddOnBenefitsPatientCare.setValue(null);
		    			}
		    		break;
		    		
		    	}
		    	if(chkAddOnBenefitsHospitalCash != null && chkAddOnBenefitsPatientCare != null && chkOtherBenefits != null &&
		    			this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.STAR_MICRO_RORAL_AND_FARMERS_CARE)) {
					chkAddOnBenefitsHospitalCash.setEnabled(false);
					chkAddOnBenefitsPatientCare.setEnabled(false);
					chkOtherBenefits.setEnabled(false);
					
				}
		    	
		    	if(chkAddOnBenefitsPatientCare!= null && chkAddOnBenefitsPatientCare != null && this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)) {
					chkAddOnBenefitsPatientCare.setEnabled(false);
					
				}
		    	
		    	//DR
				if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						|| this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
					if(null != chkhospitalization) {
						chkhospitalization.setEnabled(false); }
						if(null != chkHospitalizationRepeat) {
						chkHospitalizationRepeat.setEnabled(false); }
						if(null != chkPreHospitalization) {
						chkPreHospitalization.setEnabled(false); }
						if(null != chkLumpSumAmount) {
						chkLumpSumAmount.setEnabled(false); }
						if(null != chkPostHospitalization) {
						chkPostHospitalization.setEnabled(false); }
						if(null != chkPartialHospitalization) {
						chkPartialHospitalization.setEnabled(false); }
						if(null != chkAddOnBenefitsHospitalCash) {
						chkAddOnBenefitsHospitalCash.setEnabled(false); }
						if(null != chkAddOnBenefitsPatientCare) {
						chkAddOnBenefitsPatientCare.setEnabled(false); }
						if(null != chkOtherBenefits) {
						chkOtherBenefits.setEnabled(false); }
						if(null != chkHospitalCash) {
						chkHospitalCash.setEnabled(true); }
					
				}
				
		    	
		    }
	    
	 /*   private void enableOrDisableClassificationBasedOnsubCover(int i,String docRecValue)
	    {
	    	
	    	switch(i)
	    	{
	    		case 1:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			chkPreHospitalization.setEnabled(true);
	    			chkPostHospitalization.setEnabled(true);	    			
	    			chkHospitalizationRepeat.setEnabled(true);
	    			chkLumpSumAmount.setEnabled(false);
	    			chkLumpSumAmount.setValue(null);
	    			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("hospitalCashFlag")))
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    			}
	    			else
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(true);
	    			}
	    			chkAddOnBenefitsPatientCare.setEnabled(false);
	    			chkAddOnBenefitsPatientCare.setValue(false);
	    		break;
	    		
	    		case 2:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			
	    			chkPostHospitalization.setEnabled(false);
	    			chkPostHospitalization.setValue(null);
	    			
	    			chkPreHospitalization.setEnabled(false);
	    			chkPreHospitalization.setValue(null);
	    			
	    			chkHospitalizationRepeat.setEnabled(false);
	    			chkHospitalizationRepeat.setValue(null);
	    			chkLumpSumAmount.setEnabled(false);
	    			chkLumpSumAmount.setValue(null);
	    			chkAddOnBenefitsHospitalCash.setEnabled(false);
	    			chkAddOnBenefitsHospitalCash.setValue(null);
	    			chkAddOnBenefitsPatientCare.setEnabled(false);
	    			chkAddOnBenefitsPatientCare.setValue(null);
	    		break;
	    		
	    		case 3:
	    			chkhospitalization.setEnabled(false);
	    			chkhospitalization.setValue(null);
	    			chkPartialHospitalization.setEnabled(false);
	    			chkPartialHospitalization.setValue(null);
	    			chkPreHospitalization.setEnabled(false);
	    			chkPreHospitalization.setValue(null);
	    			chkPostHospitalization.setEnabled(false);
	    			chkPostHospitalization.setValue(null);
	    			chkHospitalizationRepeat.setEnabled(false);
	    			chkHospitalizationRepeat.setValue(null);
	    			chkLumpSumAmount.setEnabled(false);
	    			chkLumpSumAmount.setValue(null);
	    			chkAddOnBenefitsHospitalCash.setEnabled(false);
	    			chkAddOnBenefitsHospitalCash.setValue(null);
	    			chkAddOnBenefitsPatientCare.setEnabled(false);
	    			chkAddOnBenefitsPatientCare.setValue(null);
	    		break;
	    		
	    		case 4:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			
	    			chkPreHospitalization.setEnabled(true);
	    			chkPostHospitalization.setEnabled(true);
	    			
	    			chkHospitalizationRepeat.setEnabled(false);
	    			chkHospitalizationRepeat.setValue(null);
	    			
	    			chkLumpSumAmount.setEnabled(false);
	    			chkLumpSumAmount.setValue(null);
	    			
	    			chkAddOnBenefitsHospitalCash.setEnabled(false);
	    			chkAddOnBenefitsHospitalCash.setValue(null);
	    			
	    			chkAddOnBenefitsPatientCare.setEnabled(false);
	    			chkAddOnBenefitsPatientCare.setValue(null);
	    			
	    		break;
	    		
	    		case 5:
	    			chkhospitalization.setEnabled(false);
	    			chkPartialHospitalization.setEnabled(false);
	    			chkPreHospitalization.setEnabled(false);
	    			chkPostHospitalization.setEnabled(false);
	    			chkHospitalizationRepeat.setEnabled(false);
	    			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("LumpSumFlag")))
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    			}
	    			else
	    			{
	    				chkLumpSumAmount.setEnabled(true);
	    			}
	    			chkAddOnBenefitsHospitalCash.setEnabled(false);
	    			chkAddOnBenefitsPatientCare.setEnabled(false);
	    			chkhospitalization.setValue(null);
	    			chkPreHospitalization.setValue(null);
	    			chkPostHospitalization.setValue(null);
	    			chkPartialHospitalization.setValue(null);
	    			chkHospitalizationRepeat.setValue(null);
	    			chkAddOnBenefitsHospitalCash.setValue(null);
	    			chkAddOnBenefitsPatientCare.setValue(null);
	    			
	    			txtHospitalizationClaimedAmt.setValue(null);
	    			txtHospitalizationClaimedAmt.setEnabled(false);
	    			

	    			txtPreHospitalizationClaimedAmt.setValue(null);
	    			txtPreHospitalizationClaimedAmt.setEnabled(false);
	    			

	    			txtPostHospitalizationClaimedAmt.setValue(null);
	    			txtPostHospitalizationClaimedAmt.setEnabled(false);
	    			
	    		break;	
	    		
	    	}
	    }*/
	    
	    private void enableDisableHospAndPartialHosp(String docRecValue)
	    {
	    	if((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && (SHAConstants.DOC_RECEIVED_FROM_INSURED).equalsIgnoreCase(docRecValue))
			{
	    		if(null != chkPartialHospitalization)
	    			chkPartialHospitalization.setEnabled(true);
	    		if(null != chkhospitalization)
	    		{
	    			chkhospitalization.setEnabled(false);
					chkhospitalization.setValue(null);
					optDocumentVerified.setValue(null);
					optDocumentVerified.setEnabled(false);
	    		}
			}
			else if ((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && (SHAConstants.DOC_RECEIVED_FROM_INSURED).equalsIgnoreCase(docRecValue))
			{
				if(null != chkPartialHospitalization)
				{
					chkPartialHospitalization.setEnabled(false);
					chkPartialHospitalization.setValue(null);
				}
				if(null != chkhospitalization)
					chkhospitalization.setEnabled(true);
				
			}
	    }
	    
	    private void subCoverBasedBillClassificationManipulation(String value,Long productKey)
	    {

			String docRecFromVal = null;
			if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
			{
				SelectValue selValue = (SelectValue)cmbDocumentsReceivedFrom.getValue();
				docRecFromVal = selValue.getValue();
			}
			
			if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					|| this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
				if(null != chkhospitalization) {
				chkhospitalization.setEnabled(false); }
				if(null != chkHospitalizationRepeat) {
				chkHospitalizationRepeat.setEnabled(false); }
				if(null != chkPreHospitalization) {
				chkPreHospitalization.setEnabled(false); }
				if(null != chkLumpSumAmount) {
				chkLumpSumAmount.setEnabled(false); }
				if(null != chkPostHospitalization) {
				chkPostHospitalization.setEnabled(false); }
				if(null != chkPartialHospitalization) {
				chkPartialHospitalization.setEnabled(false); }
				if(null != chkAddOnBenefitsHospitalCash) {
				chkAddOnBenefitsHospitalCash.setEnabled(false); }
				if(null != chkAddOnBenefitsPatientCare) {
				chkAddOnBenefitsPatientCare.setEnabled(false); }
				if(null != chkOtherBenefits) {
				chkOtherBenefits.setEnabled(false); }
				if(null != chkHospitalCash) {
				chkHospitalCash.setEnabled(true); 
				cmbReconsiderationRequest.setEnabled(false);
				}
				
			}
				
			if(!((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && (SHAConstants.DOC_RECEIVED_FROM_HOSPITAL).equalsIgnoreCase(docRecFromVal))) 
			{
				if((ReferenceTable.HOSP_SUB_COVER_CODE).equalsIgnoreCase(value) && ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(productKey))					
				{
					enableOrDisableClassificationBasedOnsubCover(6,docRecFromVal);
				}
				else if((ReferenceTable.HOSP_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(1,docRecFromVal);
				}
				else if(ReferenceTable.MATERNITY_NORMAL_SUB_COVER_CODE.equalsIgnoreCase(value) || ReferenceTable.MATERNITY_CEASEAREAN_SUB_COVER_CODE.equalsIgnoreCase(value) ||
						ReferenceTable.NEW_BORN_SUB_COVER_CODE.equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(2,docRecFromVal);
				}
				else if((ReferenceTable.NEW_BORN_CHILD_VACCINATION_SUB_COVER_CODE).equalsIgnoreCase(value) || (ReferenceTable.DENTAL_OPTHALMIC_SUB_COVER_CODE).equalsIgnoreCase(value) ||
						(ReferenceTable.HOSPITAL_CASH_SUB_COVER_CODE).equalsIgnoreCase(value) || (ReferenceTable.HEALTH_CHECKUP_SUB_COVER_CODE).equalsIgnoreCase(value) || (ReferenceTable.ACCIDENTAL_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(3,docRecFromVal);
				}
				else if((ReferenceTable.BARIATRIC_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(4,docRecFromVal);
				}
				else if((ReferenceTable.LUMPSUM_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(5,docRecFromVal);
				}
				else if((ReferenceTable.NEW_BORN_HOSPITALISATION).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(7,docRecFromVal);
				}
				else if((ReferenceTable.NEW_BORN_LUMPSUM_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(8,docRecFromVal);
				}
			}
		
	    }
	    protected void showPopup(final String message) {

			 String message1 = message.replaceAll("(.{200})", "$1<br />");
			 message1 = message1.replaceAll("(\r\n|\n)", "<br />");

			/*Label successLabel = new Label(
					"<b style = 'color: red;'>" +   message1 + "</br>",
					ContentMode.HTML);
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			//layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
			layout.setHeightUndefined();*/
			/*if(bean.getClmPrcsInstruction()!=null && bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
				if(message.length()>4000){
				layout.setHeight("100%");
				layout.setWidth("100%");
				}
				
			}		*/	
			/*final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
		
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(message1 + "</br>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			/*if(bean.getClmPrcsInstruction()!=null && bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
				if(message.length()>4000){
					dialog.setWidth("55%");
				}
			}*/
			//

			homeButton.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						 	//dialog.close();
						 	if(bean.getClmPrcsInstruction()!=null && !bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
						 		showPopup(bean.getClmPrcsInstruction());
						 	}
					}
				});
				
		
	    }
	    
	    public void policyValidationPopupMessage() {	 
			 
			 /*Label successLabel = new Label(
						"<b style = 'color: red;'>" + SHAConstants.POLICY_VALIDATION_ALERT + "</b>",
						ContentMode.HTML);
		   		//final Boolean isClicked = false;
				Button homeButton = new Button("OK");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
				layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
				layout.setSpacing(true);
				layout.setMargin(true);
				layout.setStyleName("borderLayout");*/
				/*HorizontalLayout hLayout = new HorizontalLayout(layout);
				hLayout.setMargin(true);
				hLayout.setStyleName("borderLayout");*/

			/*	final ConfirmDialog dialog = new ConfirmDialog();
//				dialog.setCaption("Alert");
				dialog.setClosable(false);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createAlertBox( SHAConstants.POLICY_VALIDATION_ALERT + "</b>", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						 if(bean.getIsPEDInitiated()) {
							alertMessageForPED();
						  }/*else if(bean.getIsSuspicious()!=null){
							  StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
							}*/
					}
				});
			}
	    
	    
	    
	    
	    
	    private void optPaymentCancellationListener()
		{
			optPaymentCancellation.addValueChangeListener(new Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					Boolean value = (Boolean) event.getProperty().getValue();
					String message = null;
					if(null != value && value)
					{
						message = "Please confirm that payment cancellation needs to be done and you have recieved the cheque/DD for cancellation";
					}
					else
					{
						message = "Are you sure that payment cancellation is not required and the payment has been accepted";
					}
					//Label successLabel = new Label("<b style = 'color: green;'>"+message+"</b>", ContentMode.HTML);
					
//					Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
					
					/*Button homeButton = new Button("Ok");
					homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					Button cancelButton = new Button("Cancel");
					cancelButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
					horizontalLayout.setSpacing(true);
					horizontalLayout.setMargin(true);
					
					VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
					layout.setSpacing(true);
					layout.setMargin(true);
					layout.setStyleName("borderLayout");*/
					/*HorizontalLayout hLayout = new HorizontalLayout(layout);
					hLayout.setMargin(true);
					hLayout.setStyleName("borderLayout");*/
					
					/*final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setClosable(false);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);*/
					
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					buttonsNamewithType.put(GalaxyButtonTypesEnum.CANCEL.toString(), "Cancel");
					HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createInformationBox(message+"</b>", buttonsNamewithType);
					Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
							.toString());
					Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.CANCEL
							.toString());
					homeButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							//dialog.close();

							//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED, null);
							
						}
					});
					cancelButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							//dialog.close();

							//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED, null);
							
						}
					});
				}

			});
		}
		
	    private void showErrorMessage(String eMsg) {
			/*Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);*/

			/*ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
			
			//optDocumentVerified.setValue(null);
		}
	    
	    public BlurListener getHospitalLisenter() {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField property = (TextField) event.getComponent();
					if(null != property)
					{
						if(!("").equalsIgnoreCase(property.getValue())){
							
							optDocumentVerified.setEnabled(true);
							optDocumentVerified.setValue(null);
						}
						else
						{
							optDocumentVerified.setEnabled(false);
						}
					}
				}
			};
			return listener;
			
		}
	  
	  public BlurListener getPreHospLisenter() {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField property = (TextField) event.getComponent();
					if(null != property)
					{if(!("").equalsIgnoreCase(property.getValue())){
						
						optDocumentVerified.setEnabled(true);
						optDocumentVerified.setValue(null);
					}
					else
					{
						optDocumentVerified.setEnabled(false);
					}
					}
				}
			};
			return listener;
			
		}
	  
	  public BlurListener getPostHospLisenter() {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField property = (TextField) event.getComponent();
					if(null != property)
					{if(!("").equalsIgnoreCase(property.getValue())){
						
						optDocumentVerified.setEnabled(true);
						optDocumentVerified.setValue(null);
					}
					else
					{
						optDocumentVerified.setEnabled(false);
					}
				  }
				}
			};
			return listener;
			
		}
	  
	  public BlurListener getOtherBenefitLisener() {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField property = (TextField) event.getComponent();
					if(null != property)
					{
						if(!("").equalsIgnoreCase(property.getValue())){
							
							optDocumentVerified.setEnabled(true);
							optDocumentVerified.setValue(null);
						}
						else
						{
							optDocumentVerified.setEnabled(false);
						}
					}
				}
			};
			return listener;
			
		}
	  
	  public void getHospitalCategory(String hospitalCategory) {	 
			 
			 /*Label successLabel = new Label(
						"<b style = 'color: red;'>" + hospitalCategory + " Category Hospital"+ "</b>",
						ContentMode.HTML);
				Button homeButton = new Button("OK");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
				layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
				layout.setSpacing(true);
				layout.setMargin(true);
				layout.setStyleName("borderLayout");*/
		
				/*final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setClosable(false);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox(hospitalCategory + " Category Hospital"+ "</b>", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;
		
					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						if(bean.getPreauthDTO().getIsPolicyValidate()){		
							policyValidationPopupMessage();
						}
						else if(bean.getIsPEDInitiated()) {
							alertMessageForPED();
						  }/*else if(bean.getIsSuspicious()!=null){
							  StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
							}*/
					}
				});
	  }
	  public synchronized void buildNomineeLayout(){
		  
	  		nomineeDetailsTable = nomineeDetailsTableInstance.get();
			
			nomineeDetailsTable.init("", false, false);
			
			if(bean.getNewIntimationDTO().getNomineeList() != null && !bean.getNewIntimationDTO().getNomineeList().isEmpty()) {
				nomineeDetailsTable.setTableList(bean.getNewIntimationDTO().getNomineeList());
				nomineeDetailsTable.setViewColumnDetails();
				nomineeDetailsTable.generateSelectColumn();
			}
			
			documentDetailsPageLayout.addComponent(nomineeDetailsTable);
		
			/*boolean enableLegalHeir = bean.getNewIntimationDTO().getNomineeList() != null && !bean.getNewIntimationDTO().getNomineeList().isEmpty() ? false : true; 
				
			legaHeirLayout = getLegalHeirLayout(enableLegalHeir);
		
			if(enableLegalHeir) {
				
				setLegalHeirDetails(bean.getNewIntimationDTO().getNomineeName() != null ? bean.getNewIntimationDTO().getNomineeName() : "", bean.getNewIntimationDTO().getNomineeAddr() != null ? bean.getNewIntimationDTO().getNomineeAddr() : "");
			}	
			
			documentDetailsPageLayout.addComponent(legaHeirLayout);*/

			boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
			
			legalHeirLayout = new VerticalLayout();
			
			legalHeirDetails = legalHeirObj.get();
			
			relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			Map<String,Object> refData = new WeakHashMap<String, Object>();
			relationshipContainer.addAll(bean.getPreauthDTO().getLegalHeirDto().getRelationshipContainer());
			refData.put("relationship", relationshipContainer);
			legalHeirDetails.setReferenceData(refData);
			legalHeirDetails.init(bean.getPreauthDTO());
			legalHeirDetails.setViewColumnDetails();
			legalHeirLayout.addComponent(legalHeirDetails);
			documentDetailsPageLayout.addComponent(legalHeirLayout);

			if(enableLegalHeir) {
				
				legalHeirDetails.addBeanToList(bean.getPreauthDTO().getLegalHeirDTOList());
				legalHeirDetails.getBtnAdd().setEnabled(true);
			}
			else {
				legalHeirDetails.deleteRows();
				legalHeirDetails.getBtnAdd().setEnabled(false);
			}
			
	  }
	  
	  private void showAlert(String alertMsg)
	  {
		  Label alertLabel = new Label(alertMsg);
		  VerticalLayout layout = new VerticalLayout(alertLabel);
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createCustomBox("Information", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
	  }

	public void setClearReferenceData() {
			SHAUtils.setClearMapBooleanValues(reconsiderationMap);
			if(documentDetailsPageLayout!=null){
				documentDetailsPageLayout.removeAllComponents();
			}
	}
	  
	  /*public synchronized FormLayout getLegalHeirLayout(boolean enable){
		  	
		  	legaHeirLayout = new FormLayout();
		  	legalHeirNameTxt = new TextField("Claimant / Legal Heir Name");
			legalHeirNameTxt.setWidth("45%");
			legalHeirNameTxt.setEnabled(enable);
			legalHeirNameTxt.setRequired(enable);

			legalHeirAddressTxt = new TextArea("Claimant / Legal Heir Address");
			legalHeirAddressTxt.setWidth("45%");
			legalHeirAddressTxt.setEnabled(enable);
			legalHeirAddressTxt.setRequired(enable);
			legalHeirAddressTxt.setMaxLength(4000);		
			
			
			legaHeirLayout.addComponents(legalHeirNameTxt, legalHeirAddressTxt);
			
			return legaHeirLayout;
		}
		
		public void enableLegalHeir(boolean enable) {
			
			if(legaHeirLayout.getComponentCount() == 2) {
				legaHeirLayout.getComponent(0).setEnabled(enable);
				legaHeirLayout.getComponent(0).setVisible(enable);
				legaHeirLayout.getComponent(1).setEnabled(enable);
				legaHeirLayout.getComponent(1).setVisible(enable);
			}		
			
			if(legalHeirNameTxt != null) {
			
				legalHeirNameTxt.setEnabled(enable);
				legalHeirAddressTxt.setEnabled(enable);

				legalHeirNameTxt.setRequired(enable);
				legalHeirAddressTxt.setRequired(enable);
			}	
			
		}
		
		public void setLegalHeirDetails(String nomineeName, String nomineeAddr){
			
			if(legalHeirNameTxt != null) {
				legalHeirNameTxt.setValue(nomineeName != null ? nomineeName : "");
				legalHeirAddressTxt.setValue(nomineeAddr != null ? nomineeAddr : "");
			}
		}*/
		
		
}
