package com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.teemu.wizards.GWizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.pages.DocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.tables.DocumentCheckListTable;
import com.shaic.claim.rod.wizard.tables.DocumentCheckListValidationListenerTable;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDetails;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PAAcknowledgementDocumentDetailsPage  extends ViewComponent {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BeanFieldGroup<DocumentDetailsDTO> binder;

	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	
	private Instance<PARODQueryDetailsTable> rodQueryDetailsObj;	

	
	@Inject
	public PARODQueryDetailsTable rodQueryDetails;
	
	@Inject
	public PAPamentQueryDetailsTable paymentQueryDetails;

	@Inject
	private PAReconsiderRODRequestListenerTable reconsiderRequestDetails;	

	@Inject
	private Instance<DocumentCheckListValidationListenerTable> documentCheckListValidationObj;
	
	private DocumentCheckListValidationListenerTable documentCheckListValidation;
	
	@Inject
	private AddOnCoversTable addOnCoversTable;
	
	@Inject
	private OptionalCoversTable optionalCoversTable;	
	
	private VerticalLayout addOnBenifitLayout;
	
	private VerticalLayout optionalCoverLayout;
	
	private OptionGroup accidentOrDeath;
	
	private DateField accidentOrDeathDate;
	
	private ComboBox cmbDocumentType;
	
	
	private ComboBox cmbDocumentsReceivedFrom;
	
	private TextField txtAcknowledgementContactNo;
	
	private OptionGroup workOrNonWorkSpace;
	
	private DateField documentsReceivedDate;
	
	private TextField txtEmailId;
	
	private ComboBox cmbModeOfReceipt;
	
	private ComboBox cmbReconsiderationRequest;
	
	
	private TextArea txtAdditionalRemarks;	
	
	private CheckBox chkDeath;
	
	private CheckBox chkPermanentPartialDisability;
	
	private CheckBox chkPermanentTotalDisability;
	
	private CheckBox chkTemporaryTotalDisability;
	
	private CheckBox chkHospitalExpensesCover;

	private CheckBox chkhospitalization;
	
	private CheckBox chkPreHospitalization;
	
	private CheckBox chkPostHospitalization;
	
	private CheckBox chkPartialHospitalization;
	
	private CheckBox chkHospitalizationRepeat;
	
	private CheckBox chkLumpSumAmount;
	
	private CheckBox chkAddOnBenefitsHospitalCash;
	
	private CheckBox chkAddOnBenefitsPatientCare;
		
	private VerticalLayout documentDetailsPageLayout;
	
	private VerticalLayout legalHeirLayout;

	private GWizard wizard;
	
	private VerticalLayout reconsiderationLayout;
	
	private VerticalLayout basedOnDocumentTypeLayout;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();

	private BeanItemContainer<SelectValue> reconsiderationRequest;
	
	private BeanItemContainer<SelectValue> reasonForReconsiderationRequest;
	 
	private BeanItemContainer<SelectValue> modeOfReceipt;
	 
	private BeanItemContainer<SelectValue> docReceivedFromRequest ;
	
	private BeanItemContainer<SelectValue> documentType;
	 
	private TextField txtHospitalizationClaimedAmt;
	 
	private TextField txtPreHospitalizationClaimedAmt;
	
	private TextField txtPostHospitalizationClaimedAmt;	 
	
	private TextField txtBenifitClaimedAmnt;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	//private  DocumentDetailsDTO docDTO ;
	private List<DocumentDetailsDTO> docDTO;
	
	private static boolean isQueryReplyReceived = false;
	//public static boolean isQueryReplyReceived = false;
	
	private ComboBox cmbReasonForReconsideration;
	
	@Inject
	private ViewDetails viewDetails;
	
	private List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList;
	
	public Map<String,Boolean> reconsiderationMap = new HashMap<String, Boolean>();
	
	
	public String hospitalizationClaimedAmt = "";
	
	public String preHospitalizationAmt = "";
	
	public String postHospitalizationAmt= "";
	
	public String benefitClaimedAmnt = "";
	
	public ReconsiderRODRequestTableDTO reconsiderDTO = null;
	
	public RODQueryDetailsDTO queryDTO = null;
	

 	private String strDocRecFrom = "";
 	
 	private String strModeOfReceipt = null;
 	
 	/*
 	 * Added for removing hospitalization validation.
 	 * */
 	
 	private Boolean isFinalEnhancement = false;
 	
 	private OptionGroup optPaymentCancellation;
 	
 	public HorizontalLayout hLayout = null;
 	
	private final Logger log = LoggerFactory.getLogger(PAAcknowledgementDocumentDetailsPage.class);
	
	public Boolean isNext = false;
	
	public HorizontalLayout documentDetailsLayout;
	
	public VerticalLayout docCheckListLayout;
	
	public HorizontalLayout remarksLayout;
	
	public Map<String, Object> referenceMap = new HashMap<String, Object>();
	
	public Map<String, Object> existingReferenceMap = new HashMap<String, Object>();
	
	public BeanItemContainer<SelectValue> defaultParticularValues = null;
	
	private Boolean isValid = false;
	
	private String coverName = "";

	public Boolean isHospitalization = false;
	
	private DateField dateOfAccident;
	
	private DateField dateOfDeath;
	
	private DateField dateOfDisablement;
	
	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	
	@Inject
	private Instance<LegalHeirDetails> legalHeirObj;
	
	private LegalHeirDetails legalHeirDetails;
		
	private BeanItemContainer<SelectValue> relationshipContainer;
	
	private CheckBox chkNomineeDeceased;
	
//	private FormLayout legaHeirLayout;
	//private static boolean isReconsiderRequestFlag = false;
	 
//	List<DocumentCheckListDTO> mainDocList = new ArrayList<DocumentCheckListDTO>();
	 
	
	@PostConstruct
	public void init() {
		
		
		//reconsiderRequestDetails.init();
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
		hLayout.setStyleName("borderLayout");*/

		/*final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("<b style = 'color: red;'>" + SHAConstants.PED_RAISE_MESSAGE + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				bean.setIsPEDInitiated(false);
			}
		});
		return true;
	}
	
	public Component getContent() {

			reconsiderDTO = null;
			if(bean.getIsPEDInitiated()) {
			alertMessageForPED();
		  }
		initBinder();
		
		if(this.bean.getClaimDTO().getNewIntimationDto().getInsuredDeceasedFlag() != null 
				&& SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getClaimDTO().getNewIntimationDto().getInsuredDeceasedFlag())) {

			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
		
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		//reconsiderationMap = new HashMap<String, Boolean>();

		//IMSSUPPOR-23207
		isQueryReplyReceived = Boolean.FALSE;
		
		documentDetailsPageLayout = new VerticalLayout();
		documentDetailsPageLayout.setSpacing(false);
		documentDetailsPageLayout.setMargin(false);		 
		reconsiderationLayout = new VerticalLayout();
		reconsiderationLayout.setSpacing(false);
		
		
		basedOnDocumentTypeLayout = new VerticalLayout();
		basedOnDocumentTypeLayout.setSpacing(false);	
		
		
		documentDetailsLayout = new HorizontalLayout(); 
		documentDetailsLayout.addComponent(buildDocumentDetailsLayout());
		documentDetailsLayout.setCaption("Document Details");

		if(rodQueryDetails != null){
			rodQueryDetails.removeRow();
		}
		
		if(paymentQueryDetails != null){
			paymentQueryDetails.removeRow();
		}
		
		VerticalLayout addOnCoversLayout = new VerticalLayout();	
		addOnCoversLayout.addComponent(buildAddOncoversDetailsLayout());
		addOnCoversLayout.setSpacing(false);
		addOnCoversLayout.setMargin(false);
		
		VerticalLayout benifitLayout  = new VerticalLayout();
		benifitLayout.addComponent(buildBenefitsLayout());
		benifitLayout.setSpacing(false);
		benifitLayout.setMargin(false);
		
		VerticalLayout optionalCoversLayout = new VerticalLayout();	
		optionalCoversLayout.addComponent(buildOptionalCoversDetailsLayout());
		optionalCoversLayout.setSpacing(false);
		optionalCoversLayout.setMargin(false);
		
		
		VerticalLayout reconsiderLayout  = new VerticalLayout(buildReconsiderationLayout());		
		reconsiderLayout.setSpacing(true);
		
		remarksLayout = new HorizontalLayout();
		FormLayout fLayout = new FormLayout(txtAdditionalRemarks);
		remarksLayout.addComponent(fLayout);
		//remarksLayout.setMargin(true);
		remarksLayout.setSpacing(true);
		
		List<DocumentCheckListDTO> dtoList = new ArrayList<DocumentCheckListDTO>();
		dtoList.addAll(this.bean.getDocumentDetails().getDocumentCheckList());
		//documentCheckList = documentCheckListObj.get();
		//documentCheckList.init("", false);
		
		documentCheckListValidation = documentCheckListValidationObj.get();
		documentCheckListValidation.setPaAckDocumentDetailsPage(this);
		/*Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
		String lob = (String) wrkFlowMap.get(SHAConstants.LOB);*/		
		documentCheckListValidation.initPresenter(SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED,SHAConstants.PA_LOB);
		documentCheckListValidation.init();
		
		
		//documentCheckList.setReference(this.referenceData);
		
		this.bean.getDocumentDetails().setDocumentCheckList(dtoList);
		//getDocumentTableDataList();
		
		docCheckListLayout = new VerticalLayout();
		docCheckListLayout.addComponent(documentCheckListValidation);
		docCheckListLayout.setCaption("Document Checklist");
		docCheckListLayout.setHeight("100%");
		
		 /*hLayout = buildQueryDetailsLayout();*/
		
		documentDetailsPageLayout = new VerticalLayout(documentDetailsLayout,benifitLayout,addOnCoversLayout,optionalCoversLayout,reconsiderLayout,reconsiderationLayout,docCheckListLayout,remarksLayout);
		
		//fireViewEvent(DocumentDetailsPresenter.SETUP_DROPDOWN_VALUES, null);
		
		/*if((bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				   && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey()))		
				   && (SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
				   && bean.getDocumentDetails().getDocumentsReceivedFrom() != null
				   && ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getDocumentDetails().getDocumentsReceivedFrom().getId())) {
			buildNomineeLayout();
		}*/
		
		
		buildNomineeLayout();
		nomineeDetailsTable.setVisible(false);
		if(chkNomineeDeceased != null){
			chkNomineeDeceased.setVisible(false);
		}
		legalHeirLayout.setVisible(false);
		
		if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
				(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
		{
			addOnCoversTable.setEnabled(false);
			optionalCoversTable.setEnabled(false);
		}
		else
		{
			addOnCoversTable.setEnabled(true);
			optionalCoversTable.setEnabled(true);
		}
		
		if(null != bean.getClaimDTO().getClaimTypeValue() && SHAConstants.CASHLESS_CLAIM_TYPE.equalsIgnoreCase(bean.getClaimDTO().getClaimType().getValue())){
			
			accidentOrDeath.setEnabled(false);			
		}
		
		addListener();
		setTableValues();
		showOrHideValidation(false);
		//addBillClassificationLister();		
		return documentDetailsPageLayout;
	}
	
	
	
	private VerticalLayout buildAddOncoversDetailsLayout()
	{
				
	//	kk.initpresenterString(SHAConstants.CREATE_ROD);
		addOnCoversTable.init("", true);
		addOnCoversTable.setScreenName(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
		//loadQueryDetailsTableValues();
		
		referenceMap.put("covers", bean.getDocumentDetails().getAdditionalCovers());
		addOnCoversTable.setReference(referenceMap);
		
		addOnBenifitLayout = new VerticalLayout();
		addOnBenifitLayout.setWidth("100%");
		addOnBenifitLayout.setCaption("Covers (Add On Covers)");
		addOnBenifitLayout.setSpacing(true);
		addOnBenifitLayout.setMargin(false);
		addOnBenifitLayout.addComponent(addOnCoversTable);
		
		if(this.bean.getDocumentDetails().getAddOnCoversList() != null && ! this.bean.getDocumentDetails().getAddOnCoversList().isEmpty()){
			for (AddOnCoversTableDTO covers : this.bean.getDocumentDetails().getAddOnCoversList()) {
				addOnCoversTable.addBeanToList(covers);
				
			}
		}
	//	addOnBenifitLayout.addComponent();
		
		//return rodQueryDetails;
		return addOnBenifitLayout;
	}
	
	private VerticalLayout buildOptionalCoversDetailsLayout()
	{
				
	//	addOnCoversTable.initpresenterString(SHAConstants.CREATE_ROD);
		optionalCoversTable.init("", true);
		optionalCoversTable.setScreenName(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
		//loadQueryDetailsTableValues();
		
		referenceMap.put("optionalCover", bean.getDocumentDetails().getOptionalCovers());
		optionalCoversTable.setReference(referenceMap);
		
		optionalCoverLayout = new VerticalLayout();
		optionalCoverLayout.setWidth("100%");
		optionalCoverLayout.setCaption("Optional Covers");
		optionalCoverLayout.setSpacing(true);
		optionalCoverLayout.setMargin(false);
		optionalCoverLayout.addComponent(optionalCoversTable);	
	//	addOnBenifitLayout.addComponent();
		
		if(this.bean.getDocumentDetails().getOptionalCoversList() != null && ! this.bean.getDocumentDetails().getOptionalCoversList().isEmpty()){
			for (AddOnCoversTableDTO covers : this.bean.getDocumentDetails().getOptionalCoversList()) {
				optionalCoversTable.addBeanToList(covers);
				
			}
		}
		//return rodQueryDetails;
		return optionalCoverLayout;
	}
	
	
	private VerticalLayout buildReconsiderationLayout()
	{
		reconsiderationLayout = new VerticalLayout();
		reconsiderationLayout.addComponent(cmbReconsiderationRequest);
		
		return 	reconsiderationLayout;
	}
	
	private void alertMessageForClaimCount(Long claimCount){
		
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
		
		
		final Window popup = new com.vaadin.ui.Window();
		popup.setWidth("30%");
		popup.setHeight("20%");
//		popup.setContent( viewDocumentDetailsPage);
		popup.setContent(panel);
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
	}
	
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
							/*	else if(individualComp instanceof RODQueryDetailsTable)
								{
									RODQueryDetailsTable field = (RODQueryDetailsTable) individualComp;
									field.removeRow();
								}*/
								else if(individualComp instanceof PARODQueryDetailsTable)
								{
									PARODQueryDetailsTable field = (PARODQueryDetailsTable) individualComp;
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
										/*else if(individualComp instanceof RODQueryDetailsTable)
										{
											RODQueryDetailsTable field = (RODQueryDetailsTable) individualComp;
											field.removeRow();
										}*/
										else if(individualComp instanceof PARODQueryDetailsTable)
										{
											PARODQueryDetailsTable field = (PARODQueryDetailsTable) individualComp;
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
	
	private void getDocumentTableDataList()
	{
		fireViewEvent(DocumentDetailsPresenter.SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES, null);
	}

	
	private HorizontalLayout buildDocumentDetailsLayout()
	{
		
		
		
		accidentOrDeath = (OptionGroup) binder.buildAndBind("Accident / Death" , "accidentOrDeath" , OptionGroup.class);
		//	optPaymentMode = new OptionGroup("Payment Mode");
		accidentOrDeath.setRequired(true);
		accidentOrDeath.addItems(getReadioButtonOptions());
		accidentOrDeath.setItemCaption(true, "Accident");
		accidentOrDeath.setItemCaption(false, "Death");
		accidentOrDeath.setStyleName("horizontal");
		//Vaadin8-setImmediate() accidentOrDeath.setImmediate(true);
		accidentOrDeath.setReadOnly(false);
		
		accidentOrDeathDate = binder.buildAndBind("Date of Accident / Death" , "accidentOrDeathDate" , DateField.class);
		accidentOrDeathDate.setEnabled(true);
		//Vaadin8-setImmediate() accidentOrDeathDate.setImmediate(true);
			
		cmbDocumentType = binder.buildAndBind("Document Type" , "documentType" , ComboBox.class);
		cmbDocumentType.setEnabled(true);
		//Vaadin8-setImmediate() cmbDocumentType.setImmediate(true);		
		
		
		/*SelectValue selectFreshDocument = new SelectValue();
		selectFreshDocument.setId(null);
		selectFreshDocument.setValue("Fresh Document");

		SelectValue selectReconsiderationDocument = new SelectValue();
		selectReconsiderationDocument.setId(null);
		selectReconsiderationDocument.setValue("Reconsideration Document");
		
		SelectValue selectQueryReplydocument = new SelectValue();
		selectQueryReplydocument.setId(null);
		selectQueryReplydocument.setValue("Query Reply Document");
		
		SelectValue selectPaymentQueryReply = new SelectValue();
		selectPaymentQueryReply.setId(null);
		selectPaymentQueryReply.setValue("Payment Query Reply");
		

		List<SelectValue> selectVallueList = new ArrayList<SelectValue>();
		selectVallueList.add(selectFreshDocument);
		selectVallueList.add(selectReconsiderationDocument);
		selectVallueList.add(selectQueryReplydocument);
		selectVallueList.add(selectPaymentQueryReply);
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer.addAll(selectVallueList);
		cmbDocumentType.setContainerDataSource(selectValueContainer);
		cmbDocumentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDocumentType.setItemCaptionPropertyId("value");*/

		
		
		
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
		
		
		workOrNonWorkSpace = (OptionGroup) binder.buildAndBind(
				"Accident", "workOrNonWorkPlace", OptionGroup.class);
		workOrNonWorkSpace.addItems(getReadioButtonOptions());
		workOrNonWorkSpace.setItemCaption(true, "WorkPlace");
		workOrNonWorkSpace.setItemCaption(false, "NonWorkPlace");
		workOrNonWorkSpace.setStyleName("horizontal");
		
		documentsReceivedDate = binder.buildAndBind("Documents Recieved Date", "documentsReceivedDate", DateField.class);
		documentsReceivedDate.setValue(new Date());
		
		
		cmbModeOfReceipt = binder.buildAndBind("Mode Of Receipt", "modeOfReceipt", ComboBox.class);
		txtEmailId = binder.buildAndBind("Email Id", "emailId", TextField.class);
		txtEmailId.setNullRepresentation("");
		txtEmailId.setEnabled(false);
		txtEmailId.setReadOnly(true);
		txtEmailId.setMaxLength(50);
		
		txtHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed\n(Hospitalisation)" , "hospitalizationClaimedAmount",TextField.class);
		txtPreHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed(Pre-Hosp)", "preHospitalizationClaimedAmount", TextField.class);
		txtPostHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed (Post-Hosp)", "postHospitalizationClaimedAmount",TextField.class);
		txtBenifitClaimedAmnt = binder.buildAndBind("Amount Claimed\n (Benefit)", "benifitClaimedAmount",TextField.class);
		
		txtHospitalizationClaimedAmt.setEnabled(false);
		txtHospitalizationClaimedAmt.setNullRepresentation("");
		txtHospitalizationClaimedAmt.setMaxLength(15);
		
		txtPreHospitalizationClaimedAmt.setEnabled(false);
		txtPreHospitalizationClaimedAmt.setNullRepresentation("");
		txtPreHospitalizationClaimedAmt.setMaxLength(15);
		
		txtPostHospitalizationClaimedAmt.setEnabled(false);
		txtPostHospitalizationClaimedAmt.setNullRepresentation("");
		txtPostHospitalizationClaimedAmt.setMaxLength(15);
		
		txtBenifitClaimedAmnt.setEnabled(true);
		txtBenifitClaimedAmnt.setNullRepresentation("");
		txtBenifitClaimedAmnt.setMaxLength(15);
		
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
		
		CSValidator postbenefitAmtValidator = new CSValidator();
		postbenefitAmtValidator.extend(txtBenifitClaimedAmnt);
		postbenefitAmtValidator.setRegExp("^[0-9.]*$");
		postbenefitAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator emailValidator = new CSValidator();
		emailValidator.extend(txtEmailId);
		emailValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		emailValidator.setPreventInvalidTyping(true);
		
		cmbReconsiderationRequest = binder.buildAndBind("Reconsideration Request", "reconsiderationRequest", ComboBox.class);
		//Vaadin8-setImmediate() cmbReconsiderationRequest.setImmediate(true);
		
		/**
		 * The below combo box will be displayed only if the reconsideration request is set to Yes. Therefore , the below
		 * box will be part of 
		 * */
		cmbReasonForReconsideration = binder.buildAndBind("Reason for Reconsideration" , "reasonForReconsideration" , ComboBox.class);
	/*	loadReasonForReconsiderationDropDown();*/
		txtAdditionalRemarks = binder.buildAndBind("Additional Remarks","additionalRemarks",TextArea.class);
		txtAdditionalRemarks.setMaxLength(4000);
		txtAdditionalRemarks.setWidth("800px");
		
		dateOfAccident = (DateField) binder.buildAndBind("Date of Accident",
				"dateOfAccident", DateField.class);
		
		dateOfDeath = (DateField) binder.buildAndBind("Date of Death",
				"dateOfDeath", DateField.class);
		
		dateOfDisablement = (DateField) binder.buildAndBind("Date of Disablement",
				"dateOfDisablement", DateField.class);		
		
		FormLayout detailsLayout1 = new FormLayout(accidentOrDeath,dateOfAccident,dateOfDeath,cmbDocumentsReceivedFrom,documentsReceivedDate,
				cmbModeOfReceipt,cmbDocumentType);
			
		FormLayout detailsLayout2 = new FormLayout(workOrNonWorkSpace,txtAcknowledgementContactNo,txtEmailId,txtBenifitClaimedAmnt,dateOfDisablement);
				detailsLayout2.setMargin(true);
		//HorizontalLayout docDetailsLayout = new HorizontalLayout(detailsLayout1,new FormLayout(),new FormLayout(),detailsLayout2);
		HorizontalLayout docDetailsLayout = new HorizontalLayout(detailsLayout1,detailsLayout2);
		//docDetailsLayout.setComponentAlignment(detailsLayout2, Alignment.MIDDLE_LEFT);
		docDetailsLayout.setMargin(false);
		docDetailsLayout.setSpacing(true);
		
		setRequiredAndValidation(cmbDocumentsReceivedFrom);
		setRequiredAndValidation(documentsReceivedDate);
		setRequiredAndValidation(cmbModeOfReceipt);
		setRequiredAndValidation(cmbDocumentType);
		
		mandatoryFields.add(cmbDocumentsReceivedFrom);
		mandatoryFields.add(documentsReceivedDate);
		mandatoryFields.add(cmbModeOfReceipt);
		 
		if(null != cmbDocumentsReceivedFrom)
		{
			SelectValue selValue = (SelectValue)cmbDocumentsReceivedFrom.getValue();
			if(null != selValue && null != selValue.getId() && (ReferenceTable.RECEIVED_FROM_INSURED.equals(selValue.getId()))){
				
				workOrNonWorkSpace.setEnabled(true);				
			}
			else
			{
				workOrNonWorkSpace.setEnabled(false);	
			}
		}
		
		//docDetailsLayout.setCaption("Document Details");
		
		return docDetailsLayout;
	}
	
	private HorizontalLayout buildBenefitsLayout() {
			
		
		
		chkDeath = binder.buildAndBind("Death", "death", CheckBox.class);
		//Vaadin8-setImmediate() chkDeath.setImmediate(true);
		
		//chkhospitalization.setEnabled(false);
		chkPermanentPartialDisability = binder.buildAndBind("Permanent Partial Disability", "permanentPartialDisability", CheckBox.class);
		//Vaadin8-setImmediate() chkPermanentPartialDisability.setImmediate(true);
		//chkPartialHospitalization.setEnabled(false);
		chkPermanentTotalDisability = binder.buildAndBind("Permanent Total Disability", "permanentTotalDisability", CheckBox.class);
		//Vaadin8-setImmediate() chkPermanentTotalDisability.setImmediate(true);
		
		//chkPreHospitalization = binder.buildAndBind("Pre-Hospitalisation", "preHospitalization", CheckBox.class);
		
		//chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalization", CheckBox.class);
		
		chkTemporaryTotalDisability = binder.buildAndBind("Temporary Total Disability", "temporaryTotalDisability", CheckBox.class);
		//Vaadin8-setImmediate() chkTemporaryTotalDisability.setImmediate(true);
		
		
		chkHospitalExpensesCover = binder.buildAndBind("Hospital Expenses Cover", "hospitalExpensesCover", CheckBox.class);
		//Vaadin8-setImmediate() chkHospitalExpensesCover.setImmediate(true);
		
		chkhospitalization = binder.buildAndBind("Hospitalisation", "hospitalization", CheckBox.class);

		chkPartialHospitalization = binder.buildAndBind("Partial-Hospitalisation", "partialHospitalization", CheckBox.class);
		
		if(("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			chkPartialHospitalization.setEnabled(false);
		}
		
		
		/**
		 * The below code is throwing a null 
		 * pointer exception. This code is now moved into document
		 * received from listener fld. This is because, when the screen
		 * is painted for first time, the document receivedfrom id would
		 * be null. Hence the below code will throw exception. 
		 * 
		 * Need to check with preethi, why she has added this condition 
		 * here.
		 * **/
		
		
		if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
			{
				SelectValue selValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
				if(null != selValue && ((ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL).equals(selValue.getId())))
			{
				chkhospitalization.setEnabled(true);
				chkDeath.setEnabled(false);
				chkPartialHospitalization.setEnabled(false);
				chkPermanentPartialDisability.setEnabled(false);
				chkPermanentTotalDisability.setEnabled(false);
				chkTemporaryTotalDisability.setEnabled(false);
				addOnCoversTable.setEnabled(false);
				optionalCoversTable.setEnabled(false);
			}	
			
			else
			{
				chkhospitalization.setEnabled(false);
				chkPartialHospitalization.setEnabled(true);
				chkDeath.setEnabled(true);
				chkPermanentPartialDisability.setEnabled(true);
				chkPermanentTotalDisability.setEnabled(true);
				chkTemporaryTotalDisability.setEnabled(true);
				addOnCoversTable.setEnabled(true);
				optionalCoversTable.setEnabled(true);

			}
			}
		}
		
		/*Below code only for Product - MED-PRD-074*/
		if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null
				&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT_KEY)){
			chkhospitalization.setEnabled(false);
			chkTemporaryTotalDisability.setEnabled(false);
		}
		
		if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null
				&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().equals(ReferenceTable.ACCIDENT_FAMILY_CARE_FLT_KEY)){
			chkhospitalization.setEnabled(false);
			chkDeath.setEnabled(true);
			chkPartialHospitalization.setEnabled(false);
			chkPermanentPartialDisability.setEnabled(false);
			chkPermanentTotalDisability.setEnabled(true);
			chkTemporaryTotalDisability.setEnabled(false);
			addOnCoversTable.setEnabled(false);
			optionalCoversTable.setEnabled(false);
		}
		if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null
				&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().equals(ReferenceTable.SARAL_SURAKSHA_CARE_INDIVIDUAL)){
//			chkhospitalization.setEnabled(false);
			chkTemporaryTotalDisability.setEnabled(false);
		}
		
		HorizontalLayout benifitClassificationLayout = new HorizontalLayout(chkDeath,chkPermanentPartialDisability,chkPermanentTotalDisability,chkTemporaryTotalDisability,chkhospitalization,chkPartialHospitalization);
		
		//billClassificationLayout.setCaption("Document Details");
		benifitClassificationLayout.setCaption("Benefits");
		benifitClassificationLayout.setSpacing(true);
		benifitClassificationLayout.setMargin(true);
//		billClassificationLayout.setWidth("100%");
		return benifitClassificationLayout;
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
		
		HorizontalLayout queryDetailsLayout = new HorizontalLayout(rodQueryDetails);
		queryDetailsLayout.setWidth("100%");
		queryDetailsLayout.setCaption("View Query Details");
		queryDetailsLayout.setSpacing(true);
		queryDetailsLayout.setMargin(true);
		
		
		
		
		//return rodQueryDetails;
		return queryDetailsLayout;
		
	}
	
	private HorizontalLayout buildPaymentQueryDetailsLayout()
	{
		//rodQueryDetails = rodQueryDetailsObj.get();
		paymentQueryDetails.initpresenterString(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
		paymentQueryDetails.init("", false, false,reconsiderationRequest);
//		rodQueryDetails.setReference(this.referenceData);
		loadPaymentQueryDetailsTableValues();
		//rodQueryDetails.setEditable(true);
			//rodQueryDetails.setTableList(rodQueryDetailsList);
			/*for (RODQueryDetailsDTO rodQueryDetailsDTO : rodQueryDetailsList) {
				rodQueryDetails.addBeanToList(rodQueryDetailsDTO);
			}*/
			
		
		/**/
		
		HorizontalLayout queryDetailsLayout = new HorizontalLayout(paymentQueryDetails);
		queryDetailsLayout.setWidth("100%");
		queryDetailsLayout.setCaption("View Query Details");
		queryDetailsLayout.setSpacing(true);
		queryDetailsLayout.setMargin(true);
		
		//return rodQueryDetails;
		return queryDetailsLayout;
		
	}
	
	private void loadQueryDetailsTableValues()
	{
		if(null != rodQueryDetails)
		{
			List<RODQueryDetailsDTO> rodQueryDetailsList = this.bean.getRodQueryDetailsList();
			if(null != rodQueryDetailsList && !rodQueryDetailsList.isEmpty())
			{
			//	rodQueryDetails.removeRow();
				if(null != this.cmbDocumentsReceivedFrom && null != this.cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docRecFrom = (SelectValue)this.cmbDocumentsReceivedFrom.getValue();
					String docRecFromVal = docRecFrom.getValue();
					for (RODQueryDetailsDTO rodQueryDetailsDTO : rodQueryDetailsList) {
						
						if(rodQueryDetailsDTO.getDocReceivedFrom().equalsIgnoreCase(docRecFromVal))
						{
							rodQueryDetails.addBeanToList(rodQueryDetailsDTO);
						}
						else
						{
							if(null != rodQueryDetails)
							{
								rodQueryDetails.removeRow();
							}
						}
					}
				}
			}
		}
	}
	
	
	private void loadPaymentQueryDetailsTableValues()
	{
		if(null != paymentQueryDetails )
		{
			List<RODQueryDetailsDTO> rodPaymentQueryDetailsList = this.bean.getPaymentQueryDetailsList();
			if(null != rodPaymentQueryDetailsList && !rodPaymentQueryDetailsList.isEmpty())
			{
			//	rodQueryDetails.removeRow();
				if(null != this.cmbDocumentsReceivedFrom && null != this.cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docRecFrom = (SelectValue)this.cmbDocumentsReceivedFrom.getValue();
					String docRecFromVal = docRecFrom.getValue();
					for (RODQueryDetailsDTO rodPaymentQueryDetailsDTO : rodPaymentQueryDetailsList) {
						
						if(rodPaymentQueryDetailsDTO.getDocReceivedFrom().equalsIgnoreCase(docRecFromVal))
						{
							paymentQueryDetails.addBeanToList(rodPaymentQueryDetailsDTO);
						}
						else
						{
							if(null != paymentQueryDetails)
							{
								paymentQueryDetails.removeRow();
							}
						}
					}
				}
			}
		}
	}
	//private ReconsiderRODRequestTable buildReconsiderRequestLayout()
	private VerticalLayout buildReconsiderRequestLayout()
	//private HorizontalLayout buildReconsiderRequestLayout()
	{
		//reconsiderRequestDetails = objReconsiderRequestDetails.get();
		
		//List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList = this.bean.getReconsiderRodRequestList();
		this.reconsiderRODRequestList = this.bean.getReconsiderRodRequestList();
		//reconsiderRequestDetails.init("", false, false);
		reconsiderRequestDetails.initPresenter(SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED);
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
				//reconsiderList.setSelect(null);
				reconsiderRequestDetails.addBeanToList(reconsiderList);
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
		
		//HorizontalLayout reconsiderRequestLayout = new HorizontalLayout(cmbReasonForReconsideration, reconsiderRequestDetails);
		VerticalLayout reconsiderRequestLayout = new VerticalLayout(cmbReasonForReconsideration, reconsiderRequestDetails,new FormLayout(optPaymentCancellation));
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
				/*Label successLabel = new Label("<b style = 'color: green;'>"+message+"</b>", ContentMode.HTML);
				
//				Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
				
				Button homeButton = new Button("Ok");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				Button cancelButton = new Button("Cancel");
				cancelButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
				horizontalLayout.setSpacing(true);
				horizontalLayout.setMargin(true);
				
				VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
				layout.setSpacing(true);
				layout.setMargin(true);
				HorizontalLayout hLayout = new HorizontalLayout(layout);
				hLayout.setMargin(true);
				hLayout.setStyleName("borderLayout");*/
				
				/*final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(hLayout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				buttonsNamewithType.put(GalaxyButtonTypesEnum.CANCEL.toString(), "Cancel");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox("<b style = 'color: green;'>"+message+"</b>", buttonsNamewithType);
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
	
	protected Collection<Boolean> getReadioButtonOptions() {
		
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	private void addListener()
	{
		accidentOrDeath.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				/*SelectValue docRecFrom = (SelectValue)cmbDocumentsReceivedFrom.getValue();
				Boolean accedentDeath = event.getProperty().getValue() != null ? (boolean)event.getProperty().getValue() : null; 
				if((bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				   && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey()))		
				   && ((accedentDeath != null && !accedentDeath))
				   && docRecFrom != null
				   && ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFrom.getId())) {
					
					buildNomineeLayout();
				}
				else {
					if(nomineeDetailsTable != null) { 
						documentDetailsPageLayout.removeComponent(nomineeDetailsTable);
					}
					if(legalHeirLayout != null) {
						documentDetailsPageLayout.removeComponent(legalHeirLayout);
					}
				}*/
				
			}
		});
				
		cmbDocumentType
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = event.getProperty().getValue() != null ? (SelectValue) event.getProperty().getValue() : null;
				if(null != value)
				{	
					if (documentDetailsPageLayout != null
							&& documentDetailsPageLayout.getComponentCount() > 0) {
						documentDetailsPageLayout.removeAllComponents();
					}
					if(("Fresh Document").equalsIgnoreCase(value.getValue()))
					{	
						//isReconsiderRequestFlag = false;
						unbindField(getListOfChkBox());			
						/*documentDetailsPageLayout.addComponent(documentDetailsLayout);
						documentDetailsPageLayout.addComponent(buildBillClassificationLayout());
						documentDetailsPageLayout.addComponent(buildAddOncoversDetailsLayout());*/
						documentDetailsPageLayout.addComponents(documentDetailsLayout,buildBenefitsLayout(),buildAddOncoversDetailsLayout(),buildOptionalCoversDetailsLayout(),docCheckListLayout,remarksLayout);
						resetReconsiderationValue();
					}
					
					else if(("Reconsideration Document").equalsIgnoreCase(value.getValue())){
						
						unbindField(getListOfChkBox());		
						documentDetailsPageLayout.addComponents(documentDetailsLayout,buildReconsiderRequestLayout(),docCheckListLayout,remarksLayout);
						
					}
					else if(("Query Reply Document").equalsIgnoreCase(value.getValue())){
						unbindField(getListOfChkBox());
						documentDetailsPageLayout.addComponents(documentDetailsLayout,buildQueryDetailsLayout(),docCheckListLayout,remarksLayout);
						//documentDetailsPageLayout.addComponent(buildQueryDetailsLayout());
						
					}
					else
					{
						unbindField(getListOfChkBox());
						documentDetailsPageLayout.addComponents(documentDetailsLayout,buildQueryDetailsLayout(),docCheckListLayout,remarksLayout);		
					}
					
					SelectValue docReceivedSelect = cmbDocumentsReceivedFrom != null && cmbDocumentsReceivedFrom.getValue() != null ? (SelectValue)cmbDocumentsReceivedFrom.getValue() : null;
					
					if(bean != null 
							&& SHAConstants.DEATH_FLAG.equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())										
							&& docReceivedSelect != null
							&& docReceivedSelect.getValue().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_INSURED)
							&& bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
							&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
							
								if(nomineeDetailsTable != null
										&& ((value.getValue().contains("Query Reply"))//IMSSUPPOR-31121
												|| ("Reconsideration Document").equalsIgnoreCase(value.getValue()))) {
									
									nomineeDetailsTable.setVisible(true);
									
									if(chkNomineeDeceased != null){
										chkNomineeDeceased.setVisible(true);
									}
									
									if(bean.getClaimDTO().getNewIntimationDto().getNomineeList() != null && !bean.getClaimDTO().getNewIntimationDto().getNomineeList().isEmpty()) {
										nomineeDetailsTable.setTableList(bean.getClaimDTO().getNewIntimationDto().getNomineeList());
										nomineeDetailsTable.setViewColumnDetails();
										nomineeDetailsTable.generateSelectColumn();
									}
									
									documentDetailsPageLayout.addComponent(nomineeDetailsTable);
									
									if(chkNomineeDeceased != null){
										documentDetailsPageLayout.addComponent(chkNomineeDeceased);
										addNomineeDeceasedListener();
									}
								}
								
								if(legalHeirLayout != null
										&& legalHeirDetails != null
										&& ((value.getValue().contains("Query Reply"))//IMSSUPPOR-31121
												|| ("Reconsideration Document").equalsIgnoreCase(value.getValue()))) {
									
									legalHeirLayout.setVisible(true);
									legalHeirDetails.addBeanToList(bean.getPreauthDTO().getLegalHeirDTOList());
									documentDetailsPageLayout.addComponent(legalHeirLayout);
								}
							
					}
					
					addBillClassificationLister();
					
					
					/*SelectValue docRecFrom = (SelectValue)cmbDocumentsReceivedFrom.getValue();
					Boolean accedentDeath = accidentOrDeath.getValue() != null ? (boolean)accidentOrDeath.getValue() : null; 
					if((bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
					   && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey()))		
					   && ((accedentDeath != null && !accedentDeath))
					   && docRecFrom != null
					   && ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFrom.getId())) {
						
						buildNomineeLayout();
					}*/
				}
				
			}
		});
		
		
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
						//reconsiderationLayout.addComponent(buildBillClassificationLayout());
					//	reconsiderationLayout.addComponent(buildAddOncoversDetailsLayout());
						if(null == hLayout)
						{
							hLayout = buildQueryDetailsLayout();
						}
					//	reconsiderationLayout.addComponent(hLayout);
						if(null != cmbDocumentsReceivedFrom)
						{
							SelectValue selValue = (SelectValue)cmbDocumentsReceivedFrom.getValue();
							buildBillClassificationBasedOnDocumentReceivedFrom(selValue);
							
						}
						
						addBillClassificationLister();
						bean.setReconsiderRODdto(null);
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
						
						unbindField(getListOfChkBox());
						reconsiderationLayout.addComponent(buildReconsiderRequestLayout());
						reconsiderationLayout.addComponent(hLayout);
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
						 /*Label label = new Label("Document Received Date cannot be greater than current system date.", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
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
						GalaxyAlertBox.createErrorBox("Document Received Date can 7 days prior to current system date.", buttonsNamewithType);
						documentsReceivedDate.setValue(null);
					}
				}
			}
		});
		
		txtBenifitClaimedAmnt.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				String value = (String)event.getProperty().getValue();				//SelectValue value = (SelectValue) event.getProperty();
				if(null != value)
				{
					//ReconsiderRODRequestTableDTO reconsiderDTO = new ReconsiderRODRequestTableDTO();
					if(null != reconsiderDTO)
						reconsiderDTO.setBenefitClaimedAmnt(Double.parseDouble(value));
					if(null != queryDTO)
						queryDTO.setBenefitClaimedAmount(Double.parseDouble(value));
						
					
				}
			}
		});
		
		

		dateOfAccident.addValueChangeListener(new Property.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Policy policy = (Policy) ((DateField) event
						.getProperty()).getData();

				Date enteredDate = (Date) ((DateField) event
						.getProperty()).getValue();
				if (enteredDate != null) {

					try {
						dateOfAccident.validate();
						enteredDate = (Date) event.getProperty()
								.getValue();
					} catch (Exception e) {
						dateOfAccident.setValue(null);
						showErrorMessage("Please Enter a valid Accident Date");
						// Notification.show("Please Enter a valid Date");
						return;
					}
				}

				Date currentDate = new Date();
				Date policyFrmDate = null;
				Date policyToDate = null;
				if (policy != null) {
					policyFrmDate = policy.getPolicyFromDate();
					policyToDate = policy.getPolicyToDate();
				}
				if (enteredDate != null && policyFrmDate != null
						&& policyToDate != null) {
					if (!enteredDate.after(policyFrmDate)
							|| enteredDate.compareTo(policyToDate) > 0) {
						event.getProperty().setValue(null);
					
						showErrorMessage("Accident Date is not in range between Policy From Date and Policy To Date.");
					}
				}
			}
		});		
		
		dateOfDeath.addValueChangeListener(new Property.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Policy policy = (Policy) ((DateField) event
						.getProperty()).getData();

				Date enteredDate = (Date) ((DateField) event
						.getProperty()).getValue();
				if (enteredDate != null) {

					try {
						dateOfDeath.validate();
						enteredDate = (Date) event.getProperty()
								.getValue();
					} catch (Exception e) {
						dateOfDeath.setValue(null);
						showErrorMessage("Please Enter a valid Death Date");
						// Notification.show("Please Enter a valid Date");
						return;
					}
				}

				Date currentDate = new Date();
				Date policyFrmDate = null;
				Date policyToDate = null;
				if (policy != null) {
					policyFrmDate = policy.getPolicyFromDate();
					policyToDate = policy.getPolicyToDate();
				}
				/*if (enteredDate != null && policyFrmDate != null
						&& policyToDate != null) {
					if (!enteredDate.after(policyFrmDate)
							|| enteredDate.compareTo(policyToDate) > 0) {
						event.getProperty().setValue(null);
					
						showErrorMessage("Death Date is not in range between Policy From Date and Policy To Date.");
					}
				}*/
				if(null != enteredDate)
				{
					Date accidentDate = new Date();
					if(null != dateOfAccident.getValue()){
						accidentDate = dateOfAccident.getValue();
					}
					if (accidentDate != null && null != enteredDate) {
						
						Long diffDays = SHAUtils.getDaysBetweenDate(accidentDate,enteredDate);
						
						if(null != diffDays && diffDays>365)
						{
							showErrorMessage("The date of death captured is beyond 12 months from the date of accident");
						}
					}
				}
				
				
				
			}
		});		
		
		dateOfDisablement.addValueChangeListener(new Property.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Policy policy = (Policy) ((DateField) event
						.getProperty()).getData();

				Date enteredDate = (Date) ((DateField) event
						.getProperty()).getValue();
				if (enteredDate != null) {

					try {
						dateOfDisablement.validate();
						enteredDate = (Date) event.getProperty()
								.getValue();
					} catch (Exception e) {
						dateOfDisablement.setValue(null);
						showErrorMessage("Please Enter a valid Disablement Date");
						// Notification.show("Please Enter a valid Date");
						return;
					}
				}

				Date accidentDate = new Date();
				if(null != dateOfAccident.getValue()){
					accidentDate = dateOfAccident.getValue();
				}
				if (accidentDate != null && null != enteredDate) {
					
					Long diffMonths = SHAUtils.getDaysBetweenDate(accidentDate,enteredDate);
					
					if(null != diffMonths && diffMonths>365)
					{
						showErrorMessage("The date of disablement captured is beyond 12 months from the date of accident");
					}
				}
				
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

			
			/**
			 * The below code was added, to enable
			 * the benefits classification based on document type.
			 * If document is received from insured, then as per
			 * below code, the benefits checkbox will be enabled.
			 * */
			
			if(null != workOrNonWorkSpace)
			{
				workOrNonWorkSpace.setEnabled(true);
			}
			
			setRequiredAndValidation(txtEmailId);
			
			mandatoryFields.add(txtEmailId);
			
			/*if((bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
					   && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey()))		
					   && (SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())) {
				
				buildNomineeLayout();
			}*/
			
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
			
			/*
			 * If claim type is cashless and doc received from
			 * is hospital, then hospitalization alone needs to be
			 * enabled. Rest and all will be disabled.
			 * **/	
		
			if(null != workOrNonWorkSpace)
			{
				workOrNonWorkSpace.setEnabled(true);
			}
			
			mandatoryFields.remove(txtAcknowledgementContactNo);
			mandatoryFields.remove(txtEmailId);
		}
		
		if(("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			chkPartialHospitalization.setEnabled(false);
		}	
		
		
		if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			if(null != strDocRecFrom && !("").equals(strDocRecFrom) && ("Hospital").equalsIgnoreCase(strDocRecFrom ))
			{				
				resetChkBoxValues();
				chkhospitalization.setEnabled(true);
				chkDeath.setEnabled(false);
				chkPartialHospitalization.setEnabled(false);
				chkPermanentPartialDisability.setEnabled(false);
				chkPermanentTotalDisability.setEnabled(false);
				chkTemporaryTotalDisability.setEnabled(false);
				if(null != addOnCoversTable)
				{
					addOnCoversTable.setEnabled(false);
					addOnCoversTable.removeRow();
				}
				if(null != optionalCoversTable)
				{
					optionalCoversTable.setEnabled(false);
					optionalCoversTable.removeRow();
				}
			}			
			else
			{
				resetChkBoxValues();
				chkhospitalization.setEnabled(false);
				chkPartialHospitalization.setEnabled(true);
				chkDeath.setEnabled(true);
				chkPermanentPartialDisability.setEnabled(true);
				chkPermanentTotalDisability.setEnabled(true);
				chkTemporaryTotalDisability.setEnabled(true);
				addOnCoversTable.setEnabled(true);
				optionalCoversTable.setEnabled(true);
			}
			
		}			
		loadQueryDetailsTableValues();
	}
	}
	
	private void addBillClassificationLister()

	{	
		chkDeath
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				
					// chkTemporaryTotalDisability.setValue(value);
				
				 addOnCoversTable.setEnabled(true);
				 optionalCoversTable.setEnabled(true);
				 isNonHospitalization();
				 
				 //   bean.getDocumentDetails().setDeath(true);
				
					 
					if(validateBenifits())
					 {
						/* Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
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
							GalaxyAlertBox.createErrorBox("Can not select more than one Benefit", buttonsNamewithType);
							
							chkDeath.setValue(false);
					 }
					
					fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT, bean.getClaimDTO().getKey(),SHAConstants.DEATH_FLAGS,value);
					
					if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				}					
				
				}
				
		});
		
		
		chkPermanentPartialDisability
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {			
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				 					 
					// chkTemporaryTotalDisability.setValue(value);
				isNonHospitalization();
				addOnCoversTable.setEnabled(true);
				 optionalCoversTable.setEnabled(true);
				 
				//   bean.getDocumentDetails().setPermanentPartialDisability(true);
					if(validateBenifits())
					 {
						/* Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
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
							GalaxyAlertBox.createErrorBox("Can not select more than one Benefit", buttonsNamewithType);
							chkPermanentPartialDisability.setValue(false);
					 }
					
					fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT, bean.getClaimDTO().getKey(),SHAConstants.PPD,value);
					if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				}
					
				}
				
		});
		
		
		chkHospitalExpensesCover
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {				
			
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				
					// chkTemporaryTotalDisability.setValue(value);
				isNonHospitalization();
				addOnCoversTable.setEnabled(true);
				 optionalCoversTable.setEnabled(true);
				
					if(validateBenifits())
					 {
						 /*Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
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
							GalaxyAlertBox.createErrorBox("Can not select more than one Benefit", buttonsNamewithType);
							chkHospitalExpensesCover.setValue(false);
					 }
					
				
					fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT, bean.getClaimDTO().getKey(),SHAConstants.DEATH_FLAGS,value);
					if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				}
						
				}
				
		});
		
		
		chkPermanentTotalDisability
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				
				//   bean.getDocumentDetails().setPermanentTotalDisability(true);
					// chkTemporaryTotalDisability.setValue(value);
				isNonHospitalization();
				addOnCoversTable.setEnabled(true);
				 optionalCoversTable.setEnabled(true);
					 
					if(validateBenifits())
					 {
						/* Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
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
							GalaxyAlertBox.createErrorBox("Can not select more than one Benefit", buttonsNamewithType);
							chkPermanentTotalDisability.setValue(false);
					 }
					
					fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT, bean.getClaimDTO().getKey(),SHAConstants.PTD,value);
					if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				 
				}
						
				}
				
		});
		
		
		chkTemporaryTotalDisability
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				
				isNonHospitalization();
				//   bean.getDocumentDetails().setTemporaryTotalDisability(true);
					// chkTemporaryTotalDisability.setValue(value);
				addOnCoversTable.setEnabled(true);
				 optionalCoversTable.setEnabled(true);
					 
					if(validateBenifits())
					 {
						/* Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
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
							GalaxyAlertBox.createErrorBox("Can not select more than one Benefit", buttonsNamewithType);
							chkTemporaryTotalDisability.setValue(false);
					 }
					
					fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT, bean.getClaimDTO().getKey(),SHAConstants.TTD,value);
					
					if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				 
				}
						
				}
				
		});
		
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
				//	 bean.getDocumentDetails().setHospitalization(true);
					// chkPostHospitalization.setEnabled(true);
					// chkPreHospitalization.setEnabled(true);
					if(value)
					{
					 
					 addOnCoversTable.setEnabled(false);
					 optionalCoversTable.setEnabled(false); 
					 isHospitalization();
					}
					else
					{

						if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())						
						{
						SelectValue selValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
						if(null != selValue && ((ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL).equals(selValue.getId())))
						{
							addOnCoversTable.setEnabled(false);
							optionalCoversTable.setEnabled(false);
						}
						else
						{

						addOnCoversTable.setEnabled(true);
						 optionalCoversTable.setEnabled(true);
						}
						}
					}
					 
					 if(validateBenifits())
					 {
						/* Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
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
							GalaxyAlertBox.createErrorBox("Can not select more than one Benefit", buttonsNamewithType);
							chkhospitalization.setValue(false);
					 }
					
					//	fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT, bean.getClaimDTO().getKey(),SHAConstants.HOSP,value);
					 if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
								(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
						{
							addOnCoversTable.setEnabled(false);
							optionalCoversTable.setEnabled(false);
						}
						else
						{
							addOnCoversTable.setEnabled(true);
							optionalCoversTable.setEnabled(true);
						}
					 
					 if(validateBillClassification())
					 {
						 /*Label label = new Label("Already hospitalization is existing for this claim.", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
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
							GalaxyAlertBox.createErrorBox("Already hospitalization is existing for this claim.", buttonsNamewithType);
							chkhospitalization.setValue(false);
					 }
					 else
					 {
						 txtHospitalizationClaimedAmt.setEnabled(true);
						 if(value){
							 fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.RESET_PARTICULARS_VALUES,SHAConstants.HOSPITALIZATION);
						 }
					 }
					 
					 if((null != addOnCoversTable.getValues() && !addOnCoversTable.getValues().isEmpty())|| null != optionalCoversTable.getValues() && !optionalCoversTable.getValues().isEmpty())
					 {
						 /*Label label = new Label("Please Delete the selected Add On and Optional covers before selecting hospitalization", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
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
							GalaxyAlertBox.createErrorBox("Please Delete the selected Add On and Optional covers before selecting hospitalization", buttonsNamewithType);
							chkhospitalization.setValue(false);
					 }
				 }
				 else
				 {
					 //chkPostHospitalization.setEnabled(false);
					 //chkPreHospitalization.setEnabled(false);
					 if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())						
						{
						SelectValue selValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
						if(null != selValue && ((ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL).equals(selValue.getId())))
						{
							addOnCoversTable.setEnabled(false);
							optionalCoversTable.setEnabled(false);
						}
						else
						{

						addOnCoversTable.setEnabled(true);
						 optionalCoversTable.setEnabled(true);
						}
						}
					/* addOnCoversTable.setEnabled(true);
					 optionalCoversTable.setEnabled(true);*/
					 
					 if(validateBillClassification())
					 {
						// Label label = new Label("Pre or Post hospitalization cannot exist without hospitalization", ContentMode.HTML);
						 /*Label label = new Label("None of the bill classification can exist without hospitalization", ContentMode.HTML);
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
							dialog.show(getUI().getCurrent(), null, true);*/
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox("None of the bill classification can exist without hospitalization", buttonsNamewithType);
					 }
					// else
					 //{
						
					 //}

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
					// bean.getDocumentDetails().setPartialHospitalization(true);
					 addOnCoversTable.setEnabled(false);
					 optionalCoversTable.setEnabled(false);
					 isHospitalization();
					 
					 if(validateBenifits())
					 {
						/* Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
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
							GalaxyAlertBox.createErrorBox("Can not select more than one Benefit", buttonsNamewithType);
							chkPartialHospitalization.setValue(false);
					 }
					
					//	fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT, bean.getClaimDTO().getKey(),SHAConstants.PART,value);
					 if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
								(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
						{
						 if(null != addOnCoversTable.getValues() && !addOnCoversTable.getValues().isEmpty())
						 {
							addOnCoversTable.setEnabled(true);
						 }
						 else
						 {
							 addOnCoversTable.setEnabled(false);
						 }
						  if(null != optionalCoversTable.getValues()  && !optionalCoversTable.getValues().isEmpty() )
						 {
							optionalCoversTable.setEnabled(true);
						 }
						  else
						  {
							  optionalCoversTable.setEnabled(false);
						 }
						}
						else
						{
							addOnCoversTable.setEnabled(true);
							optionalCoversTable.setEnabled(true);
						}
					 
					 if(validateBillClassification())
					 {
						/* Label label = new Label("Already partial hospitalization is existing for this claim.", ContentMode.HTML);
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
						 if(value){
							 fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.RESET_PARTICULARS_VALUES,SHAConstants.HOSPITALIZATION);
						 }
					 }
					 
					 if((null != addOnCoversTable.getValues() && !addOnCoversTable.getValues().isEmpty())|| null != optionalCoversTable.getValues() && !optionalCoversTable.getValues().isEmpty())
					 {
						 /*Label label = new Label("Please Delete the selected Add On and Optional covers before selecting hospitalization", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
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
							GalaxyAlertBox.createErrorBox("Please Delete the selected Add On and Optional covers before selecting hospitalization", buttonsNamewithType);
							chkPartialHospitalization.setValue(false);
					 }
					 
				 }
				 else
				 {
					 
							addOnCoversTable.setEnabled(true);
							 optionalCoversTable.setEnabled(true);
						
					 
					 
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
						 
						/* Label label = new Label("Pre or Post hospitalization cannot exist without Partial hospitalization", ContentMode.HTML);
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
							dialog.show(getUI().getCurrent(), null, true);*/
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox("Pre or Post hospitalization cannot exist without Partial hospitalization", buttonsNamewithType);
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
	
	
	
	public void loadContainerDataSources(Map<String, Object> referenceDataMap)
	{
		
		this.referenceData =referenceDataMap; 
		
		if(! isNext){
			existingReferenceMap = referenceDataMap;
		}
		
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
		
		
		 
		
		 modeOfReceipt = (BeanItemContainer<SelectValue>) referenceDataMap.get("modeOfReceipt");
		 cmbModeOfReceipt.setContainerDataSource(modeOfReceipt);
		 cmbModeOfReceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbModeOfReceipt.setItemCaptionPropertyId("value");
		 
	
		 documentType = (BeanItemContainer<SelectValue>) referenceDataMap.get("documentType");
		 cmbDocumentType.setContainerDataSource(documentType);
		 cmbDocumentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbDocumentType.setItemCaptionPropertyId("value");
		 
		 
		 
		 
		 /*
		  * On load , reason for reconsideration drop down container is loaded. But only if 
		  * reconsideration is set to YES, the reason for reconsideration drop down will be enabled.
		  * Hence loading of this is done in loadReasonForReconsiderationDropDown() method.
		  * */ 
		 reasonForReconsiderationRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("reasonForReconsiderationRequest");
			loadReasonForReconsiderationDropDown();
	
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
			/*if ((null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) || (null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue()) 
					|| (null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()))
			{
				txtHospitalizationClaimedAmt.setEnabled(true);
				txtHospitalizationClaimedAmt.setValue(hospitalizationClaimedAmt);
			}*/
			
		}
	}

	
	
	private List<Field<?>> getListOfChkBox()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(chkDeath);
		fieldList.add(chkPermanentPartialDisability);
		fieldList.add(chkPermanentTotalDisability);
		fieldList.add(chkTemporaryTotalDisability);
		fieldList.add(chkHospitalExpensesCover);
		fieldList.add(chkhospitalization);
		fieldList.add(chkPartialHospitalization);
		//Added for issue no 2558
		if(null != cmbReasonForReconsideration)
		fieldList.add(cmbReasonForReconsideration);
		if(null != optPaymentCancellation)
		fieldList.add(optPaymentCancellation);
		if(null != cmbDocumentType)
		fieldList.add(cmbDocumentType);
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
		
		Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";
		Boolean isReconsiderationRequest = false;
		
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
		
		if(null != this.txtEmailId && null != this.txtEmailId.getValue() && !("").equalsIgnoreCase(this.txtEmailId.getValue()))
		{
			if(!isValidEmail(this.txtEmailId.getValue()))
			{
				hasError = true;
				eMsg += "Please enter a valid email </br>";
			}
		}
		if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null
				&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().equals(ReferenceTable.SARAL_SURAKSHA_CARE_INDIVIDUAL)){
			if(optionalCoversTable.getValues() != null && !optionalCoversTable.getValues().isEmpty() && 
					((null != this.chkDeath && (null == this.chkDeath.getValue()) || this.chkDeath.getValue().equals(false)) &&
					(null != this.chkPermanentTotalDisability && (null == this.chkPermanentTotalDisability.getValue()) || this.chkPermanentTotalDisability.getValue().equals(false))) ){
               List<AddOnCoversTableDTO> values = this.optionalCoversTable.getValues();	
				if(null != values){
					for(AddOnCoversTableDTO optionalList : values){
						if(optionalList.getOptionalCover().getId() != null && optionalList.getOptionalCover().getId().equals(339l)){
							hasError = true;
							eMsg += "Please select Educational Grant Cover along with Death or PTD Benefit </br>";	
						}
					}
				}
			}
		}

		if(null != this.cmbReconsiderationRequest )
		{
			SelectValue reconsiderReqValue = (SelectValue)cmbReconsiderationRequest.getValue();
			//if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("NO").equalsIgnoreCase(reconsiderReqValue.getValue()))
			if(null != cmbDocumentType && null != cmbDocumentType.getValue())
			{
				SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
				if(null != selValue && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(selValue.getId())))
				{/*if(null!= cmbDocumentType && null != cmbDocumentType.getValue() && cmbDocumentType.getValue().toString().equals(ReferenceTable.PA_FRESH_DOCUMENT))
				{*/
				if((null != this.chkDeath && (null == this.chkDeath.getValue()) || this.chkDeath.getValue().equals(false)) &&
						(null != this.chkPermanentPartialDisability &&  (null == this.chkPermanentPartialDisability.getValue()) || this.chkPermanentPartialDisability.getValue().equals(false)) &&
						
						(null != this.chkPermanentTotalDisability && (null == this.chkPermanentTotalDisability.getValue()) || this.chkPermanentTotalDisability.getValue().equals(false)) &&
						
						(null != this.chkTemporaryTotalDisability && (null == this.chkTemporaryTotalDisability.getValue()) || this.chkTemporaryTotalDisability.getValue().equals(false)) &&
						
					//	(null != this.chkHospitalExpensesCover && null != this.chkHospitalExpensesCover.getValue() && this.chkHospitalExpensesCover.getValue().equals(false)) &&
						
						(null != this.chkhospitalization &&( null == this.chkhospitalization.getValue()) || this.chkhospitalization.getValue().equals(false)) &&
						
						(null != this.chkPartialHospitalization && ( null == this.chkPartialHospitalization.getValue()) || this.chkPartialHospitalization.getValue().equals(false)) &&
						
						(null == addOnCoversTable.getValues() || addOnCoversTable.getValues().isEmpty()) &&
						(null == optionalCoversTable.getValues() || optionalCoversTable.getValues().isEmpty()))
					
				/*if(( && null == this.chkPermanentPartialDisability && null == this.chkTemporaryTotalDisability  && null == this.chkhospitalization || this.chkhospitalization.equals(false)
							&& null == this.chkPartialHospitalization || this.chkPartialHospitalization.equals(false))&& ((null == addOnCoversTable.getValues() || addOnCoversTable.getValues().isEmpty()) &&
									(null == optionalCoversTable.getValues() || optionalCoversTable.getValues().isEmpty())))*/
				{
					hasError = true;
					eMsg += "Please select Benefit Or Add on Covers Or Optional Covers</br>";
				}
				/*
				if(null != this.addOnCoversTable){
					List<AddOnCoversTableDTO> values = this.addOnCoversTable.getValues();	
					if(null != values){	
						
						fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.VALIDATE_ADD_ON_COVERS_REPEAT, bean.getClaimDTO().getKey(),values);
						
					for (int cover = 0; cover < values.size()-1; cover++) 
						   for (int cover1 = cover+1; cover1 < values.size(); cover1++) 
						      if(values.get(cover).getCovers().getId() == values.get(cover1).getCovers().getId())
						      {
						    	  hasError = true;
						    	  eMsg += "you can not select same Additional Covers</br>";
						      }
					}
				}
				if(null != this.optionalCoversTable){
					
					List<AddOnCoversTableDTO> values = this.optionalCoversTable.getValues();
					if(null != values){
						
						fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.VALIDATE_OPTIONAL_COVERS_REPEAT, bean.getClaimDTO().getKey(),values);
						
					for (int cover = 0; cover < values.size()-1; cover++) 
						   for (int cover1 = cover+1; cover1 < values.size(); cover1++) 
						      if(values.get(cover).getCovers().getId() == values.get(cover1).getOptionalCover().getId())
						      {
						    	  hasError = true;
						    	  eMsg += "you can not select same Optional Covers</br>";
						      }
					}
				}*/
				//}
			}
			
			/*if(null != this.addOnCoversTable){
				List<AddOnCoversTableDTO> coverValues = this.addOnCoversTable.getValues();	
				if(null != coverValues){
			
				}
			}*/
			
		
			
			if(null != cmbDocumentType && null== cmbDocumentType.getValue()){
				hasError = true;
				eMsg += "Please select Document Type</br>";
			}
			
						
			if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("Yes").equalsIgnoreCase(reconsiderReqValue.getValue().trim()))
			{
				isReconsiderationRequest = true;
				/*if(null != optPaymentCancellation && null == optPaymentCancellation.getValue())
				{
					hasError = true;
					eMsg += "Please choose payment cancellation </br>";
				}
				 */
			}	
			if(("Reconsideration Document").equalsIgnoreCase(selValue.getValue())){
                isReconsiderationRequest = true;
			}
			
			if(cmbDocumentsReceivedFrom != null && cmbDocumentsReceivedFrom.getValue() != null && this.bean.getReconsiderRODdto() != null){
				
			    SelectValue selectValue =(SelectValue)cmbDocumentsReceivedFrom.getValue();
				
				
				if(this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId() != null 
						&& this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					
					if(this.bean.getReconsiderRODdto().getDocumentReceivedFrom() != null && selectValue != null && selectValue.getValue().equalsIgnoreCase("Hospital")&&
							(this.bean.getReconsiderRODdto().getHospitalizationFlag() != null && ! this.bean.getReconsiderRODdto().getHospitalizationFlag().equalsIgnoreCase("Y")
									)){
						
						hasError = true;
						eMsg +="Document Received from can not be Hospital for classification type other than Hospital" ;
						
					}
				}
			}
			
			
		//	else if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("YES").equalsIgnoreCase(reconsiderReqValue.getValue().trim()))
			if(null != cmbDocumentType && null != cmbDocumentType.getValue() )//&& (ReferenceTable.PA_RECONSIDERATION_DOCUMENT).equals(cmbDocumentType.getId()))
			{
				SelectValue selValue1 = (SelectValue) cmbDocumentType.getValue();
				if(null != selValue1 && ((ReferenceTable.PA_RECONSIDERATION_DOCUMENT).equals(selValue1.getId())))
				{
					if(null ==  reconsiderRODRequestList || reconsiderRODRequestList.isEmpty())
					{
						hasError = true;
						eMsg += "No Claim is available for reconsideration. Please reset Reconsideration request to NO to proceed further </br>";
					}
					else if(null == this.bean.getReconsiderRODdto())
					{
						//ReconsiderRODRequestTableDTO reconsiderDTO = this.bean.getReconsiderRODdto();
						//if(!(null != this.reconsiderDTO.getSelect() && this.reconsiderDTO.getSelect()))
						{
							hasError = true;
							eMsg += "Please select any one earlier rod for reconsideration. If not, then reset reconsideration request to NO to proceed further </br>";
						}
					}
					
					if(!(null != optPaymentCancellation && null != optPaymentCancellation.getValue()))
					{
						hasError = true;
						eMsg += "Please select payment cancellation needed since reconsideration request is yes </br>";
					}
				}
			}
		}
			else
			{
				if(null != cmbDocumentType && null== cmbDocumentType.getValue()){
					hasError = true;
					eMsg += "Please select Document Type</br>";
				}
			}
		}
		
		
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
					eMsg += "Please enter Acknowledgement Contact No</br>";
				}
				if((!(null != this.txtEmailId && null != this.txtEmailId.getValue() && !("").equalsIgnoreCase(this.txtEmailId.getValue()))))
				{
					hasError = true;
					eMsg += "Please enter email Id </br>";
				}
			}
			
			/**
			 * Since the binding of acknowledgement contact number didn't go through well,
			 * we had added the below manual validation.
			 * */

		}
		
		
		if(null != this.documentCheckListValidation)
		{
			Boolean isValid = documentCheckListValidation.validatePageForAckScreen();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.documentCheckListValidation.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
					break;
				}
			}
		}
		
		if(null != cmbDocumentType && null != cmbDocumentType.getValue())
		{
			SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
			if(null != selValue && ((ReferenceTable.PA_QUERY_REPLY_DOCUMENT).equals(selValue.getId())) || ((ReferenceTable.PA_PAYMENT_QUERY_REPLY).equals(selValue.getId())))
			{
		if(!isReconsiderationRequest)
		{
			if(null != this.rodQueryDetails)
			{
				Boolean isValid = rodQueryDetails.isValid();
				if (!isValid) {
					hasError = true;
					List<String> errors = this.rodQueryDetails.getErrors();
					for (String error : errors) {
						eMsg += error + "</br>";
					}				
			
				}
				// IMSSUPPOR-32184
				else if (rodQueryDetails.getValues() == null
					|| rodQueryDetails.getValues().isEmpty()) {
					hasError = true;
					eMsg += "Query Details not available please change document type";
				}

					
			}
		}
			}
		}
		
		if(null != cmbDocumentType && null != cmbDocumentType.getValue())
		{
			SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
			if(null != selValue && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(selValue.getId())))
			{
		if (null != this.addOnCoversTable){
			boolean isValid = this.addOnCoversTable.isValid();
			if(!isValid){
				hasError = true;
				List<String> errors = this.addOnCoversTable.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
			else
			{
				List<AddOnCoversTableDTO> values = this.addOnCoversTable.getValues();	
				if(null != values){	
				//	Long ackKey = null != bean.getDocumentDetails().getDocAcknowledgementKey() ?bean.getDocumentDetails().getDocAcknowledgementKey():0l;
				fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.VALIDATE_ADD_ON_COVERS_REPEAT, bean.getClaimDTO().getKey(),values);
				if(this.isValid){
					hasError = true;
					eMsg += "The Cover  "+coverName+"  has been selected in another ROD for this claim. Please select another Additional Cover" + "</br>";
				}
				}
			}
		}
		
		if (null != optionalCoversTable){
			boolean isValid = this.optionalCoversTable.isValid();
			if(!isValid){
				hasError = true;
				List<String> errors = this.optionalCoversTable.getErrors();
				for (String error : errors) {
					eMsg += error + "</br>";
				}
			}
			else
			{
				List<AddOnCoversTableDTO> values = this.optionalCoversTable.getValues();	
				if(null != values){	
					//Long ackKey = null != bean.getDocumentDetails().getDocAcknowledgementKey() ?bean.getDocumentDetails().getDocAcknowledgementKey():0l;
					fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.VALIDATE_OPTIONAL_COVERS_REPEAT, bean.getClaimDTO().getKey(),values);
					if(this.isValid){
						hasError = true;
						eMsg += "The Cover  "+coverName+"  has been selected in another ROD for this claim. Please select another Optional Cover" + "</br>";
					}
				}
			}
		}
			}
	}
		if(null != cmbDocumentType && null != cmbDocumentType.getValue())
		{
			SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
			if(null != selValue && ((ReferenceTable.PA_QUERY_REPLY_DOCUMENT).equals(selValue.getId()) ||
					(ReferenceTable.PA_PAYMENT_QUERY_REPLY).equals(selValue.getId())) )
			{
		if(!isReconsiderationRequest)
		{
			if(null != this.rodQueryDetails)
			{
				Boolean isQueryReplyNo = rodQueryDetails.isQueryReplyNo();
				if(isQueryReplyNo){							
							
					//documentType = (BeanItemContainer<SelectValue>) referenceDataMap.get("documentType"); 
					if(null != documentType)
					{
					 for(int i = 0 ; i<documentType.size() ; i++)
					 {					
							this.cmbDocumentType.setValue(documentType.getIdByIndex(i));
							hasError = true;
							eMsg += "Query Reply Status selected as NO so document type has been selected as Fresh Document"+ "</br>";
							break;
							//this.cmbDocumentType.setEnabled(false);
						}
					}
				}
						
				}
				
				
					
			}
		}
			}
		/*if(null != cmbDocumentType && null != cmbDocumentType.getValue())
		{
			SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
			if(null != selValue && ((ReferenceTable.PA_QUERY_REPLY_DOCUMENT).equals(selValue.getId())) )
			{
		if(!isReconsiderationRequest)
		{
			if(null != this.rodQueryDetails)
			{
				Boolean isQueryReplyNo = rodQueryDetails.isQueryReplyNo();
				if(isQueryReplyNo){							
							
					//documentType = (BeanItemContainer<SelectValue>) referenceDataMap.get("documentType"); 
					if(null != documentType)
					{
					 for(int i = 0 ; i<documentType.size() ; i++)
					 {					
							this.cmbDocumentType.setValue(documentType.getIdByIndex(i));
							//this.cmbDocumentType.setEnabled(false);
						}
					}
				}
						
				}
				
				
					
			}
		}
			}
		*/
		
		
		if(ReferenceTable.getGPAProducts().containsKey(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
			Boolean validationforOutPatientExpenses = validationforOutPatientExpenses();
			if(!validationforOutPatientExpenses){
				hasError = true;
				eMsg += "Can not select any benefit along with Outpatient Expences</br>";
			}
			
			
			
			Boolean validationforEarningParent = validationforOutEarningParentSI();
			if(!validationforEarningParent){
				hasError = true;
				eMsg += "Can not select any benefit along with Earning Parent SI </br>";
			}
			
			
		}
		Boolean accedentDeath = accidentOrDeath.getValue() != null ? (Boolean) accidentOrDeath.getValue() : null;
		SelectValue docRecFromValue = (SelectValue)this.cmbDocumentsReceivedFrom.getValue();
		if((isReconsiderationRequest
				|| isQueryReplyReceived) 
				&& accedentDeath != null 
				&& !accedentDeath
				&& docRecFromValue != null
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFromValue.getId())
				&& bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {  

			if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() && !isNomineeDeceased()){
				List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
			
				if(tableList != null && !tableList.isEmpty()){
					bean.getClaimDTO().getNewIntimationDto().setNomineeList(tableList);
					StringBuffer nomineeNames = new StringBuffer("");
					int selectCnt = 0;
					for (NomineeDetailsDto nomineeDetailsDto : tableList) {
						nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
						if(nomineeDetailsDto.isSelectedNominee()) {
//							nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
							nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
						    selectCnt++;	
						}
					}
					bean.getClaimDTO().getNewIntimationDto().setNomineeSelectCount(selectCnt);
					if(selectCnt>0){
						bean.getClaimDTO().getNewIntimationDto().setNomineeName(nomineeNames.toString());
					}
					else{
						bean.getClaimDTO().getNewIntimationDto().setNomineeName(null);
						eMsg += "Please Select Nominee<br>";		
						hasError = true;
					}							
				}
			}
			else{
				/*bean.getClaimDTO().getNewIntimationDto().setNomineeList(null);
				bean.getClaimDTO().getNewIntimationDto().setNomineeName(null);
				Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
				if((legalHeirMap.get("FNAME") != null && !legalHeirMap.get("FNAME").toString().isEmpty())
						&& (legalHeirMap.get("ADDR") != null && !legalHeirMap.get("ADDR").toString().isEmpty()))
				{
					bean.getClaimDTO().getNewIntimationDto().setNomineeName(legalHeirMap.get("FNAME").toString());
					bean.getClaimDTO().getNewIntimationDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());
					
				}
				else{
					bean.getClaimDTO().getNewIntimationDto().setNomineeName(null);
					bean.getClaimDTO().getNewIntimationDto().setNomineeAddr(null);
				}
				
				
				if( (bean.getClaimDTO().getNewIntimationDto().getNomineeName() == null && bean.getClaimDTO().getNewIntimationDto().getNomineeAddr() == null))
				{
					eMsg += "Please Enter Claimant / Legal Heir Details<br>";
					hasError = true;
				}
				else{
					bean.getClaimDTO().getNewIntimationDto().setNomineeName(legalHeirMap.get("FNAME").toString());
					bean.getClaimDTO().getNewIntimationDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());							
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
					eMsg += "Please Enter Claimant / Legal Heir Details Mandatory (Name, Address, Pincode, Share %)<br>";
				}
					
			
			}
		}
			
		
		
		if (hasError) {
			setRequired(true);
			/*Label label = new Label(eMsg, ContentMode.HTML);
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
			GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
			hasError = true;
			return !hasError;
		} 
		else 
		{
			try {
				this.binder.commit();
				
				if(null != this.addOnCoversTable){
					List<AddOnCoversTableDTO> values = this.addOnCoversTable.getValues();
					this.bean.getDocumentDetails().setAddOnCoversList(values);
				}

				if(null != this.optionalCoversTable){
					List<AddOnCoversTableDTO> values = this.optionalCoversTable.getValues();
					this.bean.getDocumentDetails().setOptionalCoversList(values);
				}
				
				if(null != cmbReasonForReconsideration && null != cmbReasonForReconsideration.getValue())
				{	
					SelectValue selValue = (SelectValue) cmbReasonForReconsideration.getValue();
					bean.getDocumentDetails().setReasonForReconsideration(selValue);
				}
				if(null != cmbDocumentType && null != cmbDocumentType.getValue())
				{
					SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
					bean.getDocumentDetails().setDocumentType(selValue);
				}
				if(null != txtHospitalizationClaimedAmt)
					hospitalizationClaimedAmt = txtHospitalizationClaimedAmt.getValue();
				
				if(null != txtPreHospitalizationClaimedAmt)
					preHospitalizationAmt = txtPreHospitalizationClaimedAmt.getValue();
				
				if(null != txtPostHospitalizationClaimedAmt)
					postHospitalizationAmt = txtPostHospitalizationClaimedAmt.getValue();
				
				if(isReconsiderationRequest
						|| isQueryReplyReceived){
				if(((accedentDeath != null && !accedentDeath) 
							|| (SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue()))
						&& bean.getDocumentDetails().getDocumentsReceivedFrom() != null
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getDocumentDetails().getDocumentsReceivedFrom().getId())
						&& bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {

					if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty()){
						List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
					
						if(tableList != null && !tableList.isEmpty()){
							bean.getClaimDTO().getNewIntimationDto().setNomineeList(tableList);
							StringBuffer nomineeNames = new StringBuffer("");
							int selectCnt = 0;
							for (NomineeDetailsDto nomineeDetailsDto : tableList) {
								nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
								if(nomineeDetailsDto.isSelectedNominee()) {
//									nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
									nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
								    selectCnt++;	
								}
							}
							bean.getClaimDTO().getNewIntimationDto().setNomineeSelectCount(selectCnt);
							if(selectCnt>0){
								bean.getClaimDTO().getNewIntimationDto().setNomineeName(nomineeNames.toString());
							}
							else{
								bean.getClaimDTO().getNewIntimationDto().setNomineeName(null);
//								errMsg += "Please Select Nominee<br>";
								//return false;
							}							
						}
					}
					/*else{
						bean.getClaimDTO().getNewIntimationDto().setNomineeList(null);
						bean.getClaimDTO().getNewIntimationDto().setNomineeName(null);
						Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
						if((legalHeirMap.get("FNAME") != null && !legalHeirMap.get("FNAME").toString().isEmpty())
								&& (legalHeirMap.get("ADDR") != null && !legalHeirMap.get("ADDR").toString().isEmpty()))
						{
							bean.getClaimDTO().getNewIntimationDto().setNomineeName(legalHeirMap.get("FNAME").toString());
							bean.getClaimDTO().getNewIntimationDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());
							
						}
						else{
							bean.getClaimDTO().getNewIntimationDto().setNomineeName(null);
							bean.getClaimDTO().getNewIntimationDto().setNomineeAddr(null);
							return false;
						}
					}*/
					
					if(chkNomineeDeceased != null){
						bean.getPreauthDTO().getPreauthDataExtractionDetails().setNomineeDeceasedFlag(chkNomineeDeceased.getValue() ? SHAConstants.YES_FLAG : SHAConstants.N_FLAG);
					}
				}
				}
				
					
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}		
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
		this.bean.getDocumentDetails().setDocumentCheckList(documentDetailsList);
	}
	
	
	private void setTableValues()
	{
		loadDocumentCheckListTable();
	}
	
	
	private void loadDocumentCheckListTable()
	{
		if(null != this.documentCheckListValidation)
		{
			List<DocumentCheckListDTO> docCheckList = this.bean.getDocumentDetails().getDocumentCheckList();
			
			for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
				//The below select value code needs to be removed before checkin
			//	if(!isNext)
			//	{
					
					SelectValue selParticulars = new SelectValue();
					selParticulars.setId(documentCheckListDTO.getKey());
					selParticulars.setValue(documentCheckListDTO.getValue());
					documentCheckListDTO.setParticulars(selParticulars);
					SelectValue setReceivedStatus = new SelectValue();					
					if(!isNext)
							{
					setReceivedStatus.setId(ReferenceTable.DOCUMENT_CHECKLIST_NOT_APPLICABLE);
					setReceivedStatus.setValue(SHAConstants.DOC_CHECKLIST_NOT_APPLICABLE);
				
					documentCheckListDTO.setReceivedStatus(setReceivedStatus);
					documentCheckListDTO.setRemarks(null);
				}
				//documentCheckListDTO.setRemarks("Test");
				this.documentCheckListValidation.addBeanToList(documentCheckListDTO);
			}
		}
	}
	
	private void resetDocumentCheckListTableForReconsiderationRequest()
	{
		List<DocumentCheckListDTO> docCheckList =  this.bean.getDocumentDetails().getDocumentCheckList();
		if(null != docCheckList && !docCheckList.isEmpty())
		{
			
			this.documentCheckListValidation.removeRow();
			for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
				
				if(!(("Hospital Final Bill").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Hospital Break-up Bill").equalsIgnoreCase(documentCheckListDTO.getValue())
						|| ("Investigation Reports - Lab Reports").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Investigation Reports - Films - X Rays / USG / ECHO / MRI / CT Scan / HPE").equalsIgnoreCase(documentCheckListDTO.getValue())
						|| ("Investigation Reports - Reports -X Rays / USG / ECHO / MRI / CT Scan / HPE").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Medicine Bills and Dr Prescriptions").equalsIgnoreCase(documentCheckListDTO.getValue())
						|| ("Pre-hospitalisation Bills").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("Post hospitalisation Bills").equalsIgnoreCase(documentCheckListDTO.getValue()) 
						|| ("Letter from Insured (late Intimation or Delay Submission of bills or Reconsideration)").equalsIgnoreCase(documentCheckListDTO.getValue()) || ("others").equalsIgnoreCase(documentCheckListDTO.getValue()))
				 )
				{
					SelectValue setReceivedStatus = new SelectValue();
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
		this.queryDTO = rodQueryDetails;
		if(null != this.documentCheckListValidation)
		{
			this.chkhospitalization.setValue(false);
			//this.chkPreHospitalization.setValue(false);
		//	this.chkPostHospitalization.setValue(false);
			//this.chkLumpSumAmount.setValue(false);
			//this.chkHospitalizationRepeat.setValue(false);
			this.chkPartialHospitalization.setValue(false);
		//	this.chkAddOnBenefitsHospitalCash.setValue(false);
		//	this.chkAddOnBenefitsPatientCare.setValue(false);
			//List<DocumentCheckListDTO> mainDocList = this.documentCheckList.getValues();
			
			//mainDocList .addAll(this.bean.getDocumentDetails().getDocumentCheckList());
		
			//List<DocumentCheckListDTO> docCheckList = documentCheckList.getValues();
			this.cmbDocumentType.setEnabled(true);
					
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
					
					if(null != txtBenifitClaimedAmnt)
					{
						if(null != rodQueryDetails.getClaimedAmount() && 0 != rodQueryDetails.getBenefitClaimedAmount() && !("").equals(rodQueryDetails.getBenefitClaimedAmount()))
						{
							
							txtBenifitClaimedAmnt.setValue(String.valueOf(rodQueryDetails.getBenefitClaimedAmount()));
							
						}
					}				/*	if(null != rodQueryDetails.getPreHospitalizationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getPreHospitalizationFlag()))
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
					}*/
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
				/*	if(null != rodQueryDetails.getHospitalizationRepeatFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getHospitalizationRepeatFlag()))
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
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}
						this.chkLumpSumAmount.setEnabled(false);
					}
					if(null != rodQueryDetails.getAddOnBeneftisHospitalCashFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getAddOnBeneftisHospitalCashFlag()))
					{
						this.chkAddOnBenefitsHospitalCash.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}
						this.chkAddOnBenefitsHospitalCash.setEnabled(false);
					}
					if(null != rodQueryDetails.getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getAddOnBenefitsPatientCareFlag()))
					{
						this.chkAddOnBenefitsPatientCare.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}
						this.chkAddOnBenefitsPatientCare.setEnabled(false);
					}
					*/
					this.cmbDocumentType.setEnabled(true);
					disableBillClassification();
					/*txtHospitalizationClaimedAmt.setEnabled(false);
					txtPreHospitalizationClaimedAmt.setEnabled(false);
					txtPostHospitalizationClaimedAmt.setEnabled(false);*/
				
					if(rodQueryDetails.getAcknowledgementContactNumber() != null){
					this.txtAcknowledgementContactNo.setValue(rodQueryDetails.getAcknowledgementContactNumber());
					}
					
					SelectValue docRecFrom = cmbDocumentsReceivedFrom != null ? (SelectValue)cmbDocumentsReceivedFrom.getValue() : null;
					
					if((SHAConstants.DEATH_FLAG.equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
									|| (rodQueryDetails.getReimbursement().getPatientStatus() != null
											&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(rodQueryDetails.getReimbursement().getPatientStatus().getKey())
													|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(rodQueryDetails.getReimbursement().getPatientStatus().getKey()))))
							&& ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFrom.getId())
							&& bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
							&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
					
						if(rodQueryDetails != null) {
							bean.getClaimDTO().getNewIntimationDto().setNomineeName(rodQueryDetails.getReimbursement().getNomineeName());
							bean.getClaimDTO().getNewIntimationDto().setNomineeAddr(rodQueryDetails.getReimbursement().getNomineeAddr());
							bean.getPreauthDTO().getPreauthDataExtractionDetails().setPatientStatus(new SelectValue());
							bean.getPreauthDTO().getPreauthDataExtractionDetails().getPatientStatus().setId(rodQueryDetails.getReimbursement().getPatientStatus() != null ? rodQueryDetails.getReimbursement().getPatientStatus().getKey() : null);
							bean.getPreauthDTO().getPreauthDataExtractionDetails().getPatientStatus().setValue(rodQueryDetails.getReimbursement().getPatientStatus() != null ? rodQueryDetails.getReimbursement().getPatientStatus().getValue() : "");
						}
						
						if(nomineeDetailsTable != null) { 
							nomineeDetailsTable.setVisible(true);
							if(chkNomineeDeceased != null){
								chkNomineeDeceased.setVisible(true);
							}
							if(bean.getClaimDTO().getNewIntimationDto().getNomineeList() != null && !bean.getClaimDTO().getNewIntimationDto().getNomineeList().isEmpty()) {
								nomineeDetailsTable.setTableList(bean.getClaimDTO().getNewIntimationDto().getNomineeList());
								nomineeDetailsTable.setViewColumnDetails();
								nomineeDetailsTable.generateSelectColumn();
							}
						}
						if(legalHeirLayout != null) {
							legalHeirLayout.setVisible(true);
						}
//							setLegalHeirDetails(bean.getNewIntimationDTO().getNomineeName() != null ? bean.getNewIntimationDTO().getNomineeName() : "", bean.getNewIntimationDTO().getNomineeAddr() != null ? bean.getNewIntimationDTO().getNomineeAddr() : "");
						
					}
					else{
							if(nomineeDetailsTable != null) { 
								nomineeDetailsTable.setVisible(false);
							}
							if(chkNomineeDeceased != null){
								chkNomineeDeceased.setVisible(false);
							}
							if(legalHeirLayout != null) {
								legalHeirLayout.setVisible(false);
							}
						}
					
				}
			
			else if(("No").equalsIgnoreCase(rodQueryDetails.getReplyStatus()))
			{
				isQueryReplyReceived = false;
				
				this.cmbDocumentType.setEnabled(true);
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
				if(null != txtBenifitClaimedAmnt)
				{
					txtBenifitClaimedAmnt.setValue(null);
				}
				
				
			/*	if((null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("preHospitalizationFlag"))))
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
				}*/
				
				
				
				//chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalization", CheckBox.class);
			/*	
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
				}*/
				
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
				
			/*	if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("LumpSumFlag")))
				{	if(null != chkLumpSumAmount)
					{
						chkLumpSumAmount.setEnabled(true);
						if(null != chkLumpSumAmount.getValue() && chkLumpSumAmount.getValue())
						{
							chkLumpSumAmount.setValue(false);
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
				}*/
				
				
				/*
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
				*/
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
				
				/*if(null != chkHospitalizationRepeat)
				{
					this.chkHospitalizationRepeat.setEnabled(true);
				}*/
				
				resetDocCheckListTable();
				
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
						//if(null != documentCheckListDTO.getKey())
						//{
							documentCheckListDTO.setKey(documentCheckListDTO.getParticulars().getId());
						//}
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
			
		/*	
			List<RODQueryDetailsDTO> rodPaymentQueryDetailsDTO = this.paymentQueryDetails.getValues();
			if(null != rodPaymentQueryDetailsDTO && !rodPaymentQueryDetailsDTO.isEmpty())
			{
				for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodPaymentQueryDetailsDTO) {
					
					this.bean.setRodqueryDTO(null);
					
					if(null != rodQueryDetailsDTO2.getReplyStatus() && ("Yes").equals (rodQueryDetailsDTO2.getReplyStatus().trim()))
					{
						this.bean.setRodqueryDTO(rodQueryDetailsDTO2);
						break;
					}
				}
			}*/
			
			
			if(null != addOnCoversTable && null != addOnCoversTable.getTableList() && !addOnCoversTable.getTableList().isEmpty())
			{
				this.bean.getDocumentDetails().setAddOnCoversList(addOnCoversTable.getTableList());
			}
			
			if(null != optionalCoversTable && null != optionalCoversTable.getTableList() && !optionalCoversTable.getTableList().isEmpty())
			{
				this.bean.getDocumentDetails().setOptionalCoversList(optionalCoversTable.getTableList());
			}
			
			if(null != this.addOnCoversTable)
			{
				List<AddOnCoversTableDTO> addOnCoverList = this.bean.getDocumentDetails().getAddOnCoversList();				
					
				if(null != addOnCoverList && !addOnCoverList.isEmpty())
				{
				for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoverList) {
					
					//if(!isNext)
					{
						if(null != addOnCoversTableDTO.getCovers())
						{
						SelectValue addOnCovers = new SelectValue();
						addOnCovers.setId(addOnCoversTableDTO.getCovers().getId());
						addOnCovers.setValue(addOnCoversTableDTO.getCovers().getValue());
						addOnCoversTableDTO.setCovers(addOnCovers);
						}
						
					}
					
					this.addOnCoversTable.addBeanToList(addOnCoversTableDTO);
				}
				}
								
			}
			
			if(null != this.optionalCoversTable)
			{
				List<AddOnCoversTableDTO> optionalCoverList = this.bean.getDocumentDetails().getOptionalCoversList();
				
				if(null != optionalCoverList && !optionalCoverList.isEmpty())
				{

				for (AddOnCoversTableDTO optionalCoversTableDTO : optionalCoverList) {
					
					//if(!isNext)
					{
						if(null != optionalCoversTableDTO.getOptionalCover())
						{
						SelectValue addOnCovers = new SelectValue();
						addOnCovers.setId(optionalCoversTableDTO.getOptionalCover().getId());
						addOnCovers.setValue(optionalCoversTableDTO.getOptionalCover().getValue());
						optionalCoversTableDTO.setCovers(addOnCovers);
						}
						
					}
					
					this.optionalCoversTable.addBeanToList(optionalCoversTableDTO);
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
			for(int i = 0 ; i<reasonForReconsiderationRequest.size() ; i++)
		 	{
				if ( documentDetails.getReconsiderationRequest().getValue().equalsIgnoreCase(reasonForReconsiderationRequest.getIdByIndex(i).getValue()))
				{
					this.cmbReasonForReconsideration.setValue(reasonForReconsiderationRequest.getIdByIndex(i));
				}
			}
		}
		
		if(null != documentDetails.getDocumentType())
		{
			for(int i = 0 ; i<documentType.size() ; i++)
		 	{
				if ( documentDetails.getDocumentType().getValue().equalsIgnoreCase(documentType.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentType.setValue(documentType.getIdByIndex(i));
				}
			}
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
			}
			
			if(reconsiderDTO != null 
					&& (SHAConstants.DEATH_FLAG.equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
							|| (reconsiderDTO.getPatientStatus() != null
									&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(reconsiderDTO.getPatientStatus().getId())
											|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reconsiderDTO.getPatientStatus().getId()))))
					&& dto.getDocumentReceivedFrom() != null
					&& dto.getDocumentReceivedFrom().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_INSURED)
					&& bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
					
						if(nomineeDetailsTable != null) { 
							nomineeDetailsTable.setVisible(true);
							if(chkNomineeDeceased != null){
								chkNomineeDeceased.setVisible(true);
							}
							if(bean.getClaimDTO().getNewIntimationDto().getNomineeList() != null && !bean.getClaimDTO().getNewIntimationDto().getNomineeList().isEmpty()) {
								nomineeDetailsTable.setTableList(bean.getClaimDTO().getNewIntimationDto().getNomineeList());
								nomineeDetailsTable.setViewColumnDetails();
								nomineeDetailsTable.generateSelectColumn();
							}
						}
					
						if(legalHeirLayout != null && legalHeirDetails != null) {
							legalHeirLayout.setVisible(true);
							legalHeirDetails.addBeanToList(bean.getPreauthDTO().getLegalHeirDTOList());
						}
					
			}
				
		}
		//rebuildReconsiderLayout(dto);
		
		//reconsiderRequestDetails.disableTableItems(dto);
		
	}
	
	public void getErrorMessage(String eMsg){
		
		/*Label label = new Label(eMsg, ContentMode.HTML);
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
			
			if(null != txtBenifitClaimedAmnt)
			{
				if(null != dto.getBenefitClaimedAmnt() && 0 != dto.getBenefitClaimedAmnt() && !("").equals(dto.getBenefitClaimedAmnt()))
				{
					txtBenifitClaimedAmnt.setValue(String.valueOf(dto.getBenefitClaimedAmnt()));
				}
			}
			if(null != dto.getBenefitFlag())
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
			if(null != txtBenifitClaimedAmnt)
			{
				txtBenifitClaimedAmnt.setValue(null);
			}
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
			hospitalizationClaimedAmt = "";
			preHospitalizationAmt = "";
			postHospitalizationAmt = "";
			benefitClaimedAmnt = "";
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
	public Boolean validateBenifits() 
	{
		Boolean isError = false;
		
		if(((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true))) && 
			((null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue() && chkPermanentPartialDisability.getValue().equals(true)) ||
			(null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue() && chkPermanentTotalDisability.getValue().equals(true)) || 
			(null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue()  && chkTemporaryTotalDisability.getValue().equals(true)) ||		
			(null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) ||
			(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
		{
			isError = true;
		}
		if(((null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue() && chkPermanentPartialDisability.getValue().equals(true))) && 
				((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true)) ||
				(null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue()&& chkPermanentTotalDisability.getValue().equals(true)) || 
				(null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue() && chkTemporaryTotalDisability.getValue().equals(true)) ||				
				(null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) ||
				(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
			{
				isError = true;
			}
		if(((null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue() && chkPermanentTotalDisability.getValue().equals(true))) && 
				((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true)) ||
				(null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue()  && chkPermanentPartialDisability.getValue().equals(true)) || 
				(null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue()  && chkTemporaryTotalDisability.getValue().equals(true)) ||				
				(null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) ||
				(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
			{
				isError = true;
			}
		if(((null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue() && chkTemporaryTotalDisability.getValue().equals(true))) && 
				((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true)) ||
				(null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue() && chkPermanentPartialDisability.getValue().equals(true)) || 
				(null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue() && chkPermanentTotalDisability.getValue().equals(true)) ||				
				(null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) ||
				(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
			{
				isError = true;
			}
		
		
		if(((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true))) && 
				((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true)) ||
				(null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue() && chkPermanentTotalDisability.getValue().equals(true)) || 
				(null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue() && chkTemporaryTotalDisability.getValue().equals(true)) ||
				(null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue() && chkPermanentPartialDisability.getValue().equals(true)) ||					
				(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
			{
				isError = true;
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
			label.setStyleName("errMessage");
		 HorizontalLayout layout = new HorizontalLayout(
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
	
	
	public void validateBenefitRepeat(Boolean isValid,Boolean ChkBoxValue,String value) {
		
		if(isValid && ChkBoxValue)
		{
		 /*Label label = new Label("Benefit has been selected in another ROD for this claim. Please select another Benefit", ContentMode.HTML);
			label.setStyleName("errMessage");
		 HorizontalLayout layout = new HorizontalLayout(
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
			GalaxyAlertBox.createErrorBox("Benefit has been selected in another ROD for this claim. Please select another Benefit", buttonsNamewithType);
			//chkHospitalizationRepeat.setValue(null);
			chkDeath.setValue(false);
			chkPermanentPartialDisability.setValue(false);
			chkPermanentTotalDisability.setValue(false);
			chkTemporaryTotalDisability.setValue(false);
			chkHospitalExpensesCover.setValue(false);
		}
		else
		{
			 txtHospitalizationClaimedAmt.setEnabled(true);
			 
			 if(ChkBoxValue){
				 fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.RESET_PARTICULARS_VALUES,value);
			 }
			
		}
		
	}	
	
	

	public void validateCoversRepeat(Boolean isValid,String coverName) {
		this.isValid = isValid;
		this.coverName = coverName;
		if(isValid)
		{
			
		 /*Label label = new Label("The Cover  "+coverName+"  has been selected in another ROD for this claim. Please select another Additional Cover", ContentMode.HTML);
			label.setStyleName("errMessage");
		 HorizontalLayout layout = new HorizontalLayout(
				 label);
			layout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			//dialog.setWidth("35%");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			//chkHospitalizationRepeat.setValue(null);
*/			
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
	
private HorizontalLayout buildBillClassificationLayout() {
		
		
		//chkhospitalization = binder.buildAndBind("Hospitalisation", "hospitalization", CheckBox.class);
		
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
		
		
		//chkPartialHospitalization = binder.buildAndBind("Partial-Hospitalisation", "partialHospitalization", CheckBox.class);
		
		chkHospitalizationRepeat = binder.buildAndBind("Hospitalisation (Repeat)", "hospitalizationRepeat", CheckBox.class);
		
		chkLumpSumAmount = binder.buildAndBind("Lumpsum Amount", "lumpSumAmount", CheckBox.class);
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("LumpSumFlag")))
		{
			chkLumpSumAmount.setEnabled(false);
		}
		
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
		
		
		
		if(("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			chkPartialHospitalization.setEnabled(false);
		}
		
		FormLayout classificationLayout1 = new FormLayout(chkhospitalization,chkHospitalizationRepeat);
		FormLayout classificationLayout2 = new FormLayout(chkPreHospitalization,chkLumpSumAmount);
		FormLayout classificationLayout3 = new FormLayout(chkPostHospitalization,chkAddOnBenefitsHospitalCash);
		FormLayout classificationLayout4 = new FormLayout(chkPartialHospitalization,chkAddOnBenefitsPatientCare);
		
		HorizontalLayout billClassificationLayout = new HorizontalLayout(classificationLayout1,classificationLayout2,classificationLayout3,classificationLayout4);
		//billClassificationLayout.setCaption("Document Details");
		billClassificationLayout.setCaption("Bill Classification");
		billClassificationLayout.setSpacing(false);
		billClassificationLayout.setMargin(false);
//		billClassificationLayout.setWidth("100%");
		return billClassificationLayout;
	}

public void resetReconsiderationValue()
{
	if(null != cmbDocumentType && null != cmbDocumentType.getValue() )//&& (ReferenceTable.PA_RECONSIDERATION_DOCUMENT).equals(cmbDocumentType.getId()))
	{
		SelectValue selValue1 = (SelectValue) cmbDocumentType.getValue();
		if(null != selValue1 && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(selValue1.getId())))	
		{
			
			txtBenifitClaimedAmnt.setValue(null);
			/*List<ReconsiderRODRequestTableDTO> reconsiderList = new ArrayList<ReconsiderRODRequestTableDTO>();
			if(null != reconsiderRequestDetails)
			{
				reconsiderList = reconsiderRequestDetails.getValues();
			}
			
			if(null != reconsiderList && !reconsiderList.isEmpty())
			{
				reconsiderRODRequestList = new ArrayList<ReconsiderRODRequestTableDTO>();
				for (ReconsiderRODRequestTableDTO reconsiderDto : reconsiderList) {
					
					if(null != reconsiderDto.getSelect() && reconsiderDto.getSelect())
					{
						reconsiderDto.setSelect(false);
						txtBenifitClaimedAmnt.setValue(null);
					}
					reconsiderRODRequestList.add(reconsiderDto);
					
				}
				this.bean.setReconsiderRodRequestList(reconsiderRODRequestList);
			}
	*/	}
	}
	
}

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

	public void resetChkBoxValues()
	{
		chkhospitalization.setValue(false);
		chkDeath.setValue(false);
		chkPartialHospitalization.setValue(false);
		chkPermanentPartialDisability.setValue(false);
		chkPermanentTotalDisability.setValue(false);
		chkTemporaryTotalDisability.setValue(false);
	
		
		
	}
	
	public void isHospitalization()
	{
		if(null != documentCheckListValidation)
		{
			documentCheckListValidation.setIsHospitalization(true);
			documentCheckListValidation.removeRow();
			loadDocumentCheckListTable();
		}
		
	}
	public void isNonHospitalization()
	{
		//documentCheckListValidation.setIsHospitalization(false);
		if(null != documentCheckListValidation)
		{
			documentCheckListValidation.setIsHospitalization(false);
			/*documentCheckListValidation.removeRow();
			loadDocumentCheckListTable();*/
			setParticularsByDeselect();
		}
	}

	public void setParticularsByBenefitValue(String benefitValue) {
		
		if(documentCheckListValidation != null){
			
			List<String> benefitList = new ArrayList<String>();
			benefitList.add(SHAConstants.DEATH_FLAGS);
			benefitList.add(SHAConstants.PPD);
			benefitList.add(SHAConstants.PTD);
			benefitList.add(SHAConstants.TTD);
			benefitList.add(SHAConstants.HOSPITALIZATION);			
			
			List<DocumentCheckListDTO> documentCheckListValues = new ArrayList<DocumentCheckListDTO>();
			List<DocumentCheckListDTO> tableValue = documentCheckListValidation.getValues();
			documentCheckListValues.addAll(tableValue);
			documentCheckListValidation.setPaAckDocumentDetailsPage(this);
			/*Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
			String lob = (String) wrkFlowMap.get(SHAConstants.LOB);*/		
			documentCheckListValidation.initPresenter(SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED,SHAConstants.PA_LOB);
			
			List<SpecialSelectValue> particularsBasedOnBenefit = new ArrayList<SpecialSelectValue>();
			if(referenceData.get(benefitValue) != null){
				BeanItemContainer<SpecialSelectValue> items = (BeanItemContainer<SpecialSelectValue>)referenceData.get(benefitValue);
				if(items != null){
					particularsBasedOnBenefit.addAll(items.getItemIds());
				}
				
			}
			if(addOnCoversTable != null){
				List<AddOnCoversTableDTO> addOnCoversValue = addOnCoversTable.getValues();
				for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversValue) {
					if(addOnCoversTableDTO.getCovers() != null && addOnCoversTableDTO.getCovers().getValue() != null){
						if(referenceData.get(addOnCoversTableDTO.getCovers().getValue()) != null){
							BeanItemContainer<SpecialSelectValue> items = (BeanItemContainer<SpecialSelectValue>)referenceData.get(addOnCoversTableDTO.getCovers().getValue());
							if(items != null){
								particularsBasedOnBenefit.addAll(items.getItemIds());
							}
						}
					}
				}
			}
			
			if(optionalCoversTable != null){
				List<AddOnCoversTableDTO> optionalCover = optionalCoversTable.getValues();
				for (AddOnCoversTableDTO addOnCoversTableDTO : optionalCover) {
					if(addOnCoversTableDTO.getOptionalCover() != null && addOnCoversTableDTO.getOptionalCover().getValue() != null){
						if(referenceData.get(addOnCoversTableDTO.getOptionalCover().getValue()) != null){
							BeanItemContainer<SpecialSelectValue> items = (BeanItemContainer<SpecialSelectValue>)referenceData.get(addOnCoversTableDTO.getOptionalCover().getValue());
							if(items != null){
								particularsBasedOnBenefit.addAll(items.getItemIds());
							}						
							}
					}
				}
			}
		
			BeanItemContainer<SelectValue> paritculars = new BeanItemContainer<SelectValue>(SelectValue.class);
			paritculars.addAll(particularsBasedOnBenefit);
			
			this.referenceData.put("particulars",paritculars);
			
			documentCheckListValidation.setReferenceData(referenceData);
			documentCheckListValidation.init();
			
			

			/*for (DocumentCheckListDTO documentCheckListDTO : tableValue) {
					
					if(particularsBasedOnBenefit != null && documentCheckListDTO.getBenefitId() == null){
						for (SpecialSelectValue selectValue : particularsBasedOnBenefit) {
							DocumentCheckListDTO documentDTO = new DocumentCheckListDTO();
							documentDTO.setParticulars(selectValue);	
							documentDTO.setBenefitId(selectValue.getCommonValue());
							SelectValue setReceivedStatus = new SelectValue();
							setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
							setReceivedStatus.setValue("Not Applicable");
							documentDTO.setReceivedStatus(setReceivedStatus);
							documentCheckListValidation.addBeanToList(documentDTO);
						}
					}
			}*/
			
			int benefitValueCount = 1;
			
			for (SpecialSelectValue specialSelectValue : particularsBasedOnBenefit) {
				Boolean isAlreadyAvailable = Boolean.FALSE;
				for (DocumentCheckListDTO documentCheckListDTO : documentCheckListValues) {
					if(documentCheckListDTO.getParticulars() != null && specialSelectValue.getValue().equalsIgnoreCase(documentCheckListDTO.getParticulars().getValue())
							&& specialSelectValue.getCommonValue().equalsIgnoreCase("Y")){
						if(specialSelectValue.getSpecialValue() != null && benefitList.contains(specialSelectValue.getSpecialValue()) && benefitValueCount <= 5)
						{
							documentCheckListDTO.setParticulars(specialSelectValue);	
							documentCheckListValidation.addBeanToList(documentCheckListDTO);
							benefitValueCount++;
						}else{
								documentCheckListDTO.setParticulars(specialSelectValue);
								documentCheckListValidation.addBeanToList(documentCheckListDTO);
						}
						isAlreadyAvailable = Boolean.TRUE;
						break;
					}
				}
				if(!isAlreadyAvailable){
					DocumentCheckListDTO documentDTO = new DocumentCheckListDTO();
					documentDTO.setParticulars(specialSelectValue);	
					//documentDTO.setBenefitId(specialSelectValue.getCommonValue());
					SelectValue setReceivedStatus = new SelectValue();
					setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
					setReceivedStatus.setValue("Not Applicable");
					documentDTO.setReceivedStatus(setReceivedStatus);
					if(specialSelectValue.getSpecialValue() != null && benefitList.contains(specialSelectValue.getSpecialValue())
							&& specialSelectValue.getCommonValue().equalsIgnoreCase("Y"))
					{
						if(benefitValueCount <= 5){
							documentCheckListValidation.addBeanToList(documentDTO);
							benefitValueCount++;
						}
						
					}else{
						if(specialSelectValue.getCommonValue().equalsIgnoreCase("Y")){
							documentCheckListValidation.addBeanToList(documentDTO);
						}
						
					}
					
				}
			}
			
			
			
			//int i = 0;
			
			/*for (SelectValue selectValue : itemIds) {
				if(documentCheckListValues.size() > i){
					DocumentCheckListDTO documentCheckListDTO = documentCheckListValues.get(i);
					documentCheckListDTO.setParticulars(selectValue);
					documentCheckListValidation.addBeanToList(documentCheckListDTO);
				}else{
					DocumentCheckListDTO documentDTO = new DocumentCheckListDTO();
					documentDTO.setParticulars(selectValue);					
					SelectValue setReceivedStatus = new SelectValue();
					
					setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
					setReceivedStatus.setValue("Not Applicable");
					documentDTO.setReceivedStatus(setReceivedStatus);
					documentCheckListValidation.addBeanToList(documentDTO);
				}
				
				DocumentCheckListDTO documentDTO = new DocumentCheckListDTO();
				documentDTO.setParticulars(selectValue);					
				SelectValue setReceivedStatus = new SelectValue();
				
				setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
				setReceivedStatus.setValue("Not Applicable");
				documentDTO.setReceivedStatus(setReceivedStatus);
				documentCheckListValidation.addBeanToList(documentDTO);
				
				i++;
				
			}*/
			
			
			
		
		}
	
		
	}
	
public void setParticularsByAddonCovers(SelectValue value,Boolean deleteValue) {
		
		if(documentCheckListValidation != null){
			
			List<DocumentCheckListDTO> documentCheckListValues = new ArrayList<DocumentCheckListDTO>();
			List<DocumentCheckListDTO> tableValue = documentCheckListValidation.getValues();
			documentCheckListValues.addAll(tableValue);
			documentCheckListValidation.setPaAckDocumentDetailsPage(this);
			/*Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
			String lob = (String) wrkFlowMap.get(SHAConstants.LOB);*/		
			documentCheckListValidation.initPresenter(SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED,SHAConstants.PA_LOB);
			
			List<SpecialSelectValue> particularsBasedOnBenefit = new ArrayList<SpecialSelectValue>();
			
			if(value != null && value.getId() != null && value.getId().equals(ReferenceTable.OUTPATIENT_EXPENSES) && null !=deleteValue && !deleteValue){
				Boolean uncheckBenefitsValue = uncheckBenefitsValue();
				resetBenefitsValue();
				if(! uncheckBenefitsValue){
					showErrorMessage("Can not select any benefit along with Outpatient Expences");
				}
				
			}
			else if(value != null && value.getId() != null && value.getId().equals(ReferenceTable.EARNING_PARENT_SI) && null !=deleteValue && !deleteValue){
				Boolean uncheckBenefitsValue = uncheckBenefitsValue();
				resetBenefitsValue();
				if(! uncheckBenefitsValue){
					showErrorMessage("Can not select any benefit along with Earning Parent SI");
				}
			}
						
			String benefitValue = "";
			if(chkDeath != null && chkDeath.getValue() != null && chkDeath.getValue()){
				benefitValue = SHAConstants.DEATH_FLAGS;
			}else if(chkTemporaryTotalDisability != null && chkTemporaryTotalDisability.getValue() != null && chkTemporaryTotalDisability.getValue()){
				benefitValue = SHAConstants.TTD;
			}else if(chkPermanentTotalDisability != null && chkPermanentTotalDisability.getValue() != null && chkPermanentTotalDisability.getValue()){
				benefitValue = SHAConstants.PTD;
			}else if(chkPermanentPartialDisability != null && chkPermanentPartialDisability.getValue() != null && chkPermanentPartialDisability.getValue()){
				benefitValue = SHAConstants.PPD;
			}else if(chkhospitalization != null && chkhospitalization.getValue() != null && chkhospitalization.getValue()){
				benefitValue = SHAConstants.HOSPITALIZATION;
			}else if(chkPartialHospitalization != null && chkPartialHospitalization.getValue() != null && chkPartialHospitalization.getValue()){
				benefitValue = SHAConstants.HOSPITALIZATION;
			}
			if(referenceData.get(benefitValue) != null){
				BeanItemContainer<SpecialSelectValue> items = (BeanItemContainer<SpecialSelectValue>)referenceData.get(benefitValue);
				if(items != null){
					particularsBasedOnBenefit.addAll(items.getItemIds());
				}
			}
			if(addOnCoversTable != null){
				List<AddOnCoversTableDTO> addOnCoversValue = addOnCoversTable.getValues();
				for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversValue) {
					if(addOnCoversTableDTO.getCovers() != null && addOnCoversTableDTO.getCovers().getValue() != null){
						if(referenceData.get(addOnCoversTableDTO.getCovers().getValue()) != null){
							BeanItemContainer<SpecialSelectValue> items = (BeanItemContainer<SpecialSelectValue>)referenceData.get(addOnCoversTableDTO.getCovers().getValue());
							if(items != null){
								particularsBasedOnBenefit.addAll(items.getItemIds());
							}						
							}
					}
				}
			}
			
			if(optionalCoversTable != null){
				List<AddOnCoversTableDTO> optionalCover = optionalCoversTable.getValues();
				for (AddOnCoversTableDTO addOnCoversTableDTO : optionalCover) {
					if(addOnCoversTableDTO.getOptionalCover() != null && addOnCoversTableDTO.getOptionalCover().getValue() != null){
						if(referenceData.get(addOnCoversTableDTO.getOptionalCover().getValue()) != null){
							BeanItemContainer<SpecialSelectValue> items = (BeanItemContainer<SpecialSelectValue>)referenceData.get(addOnCoversTableDTO.getOptionalCover().getValue());
							if(items != null){
								particularsBasedOnBenefit.addAll(items.getItemIds());
							}						
							}
					}
				}
			}
		
			BeanItemContainer<SelectValue> paritculars = new BeanItemContainer<SelectValue>(SelectValue.class);
			paritculars.addAll(particularsBasedOnBenefit);
			
			this.referenceData.put("particulars",paritculars);
			
			if(! particularsBasedOnBenefit.isEmpty()){
				documentCheckListValidation.setReferenceData(referenceData);
				documentCheckListValidation.init();
			}else{
				existingReferenceMap.put("particulars",defaultParticularValues);
				documentCheckListValidation.setReferenceData(existingReferenceMap);
				documentCheckListValidation.init();
				List<DocumentCheckListDTO> docCheckList = this.bean.getDocumentDetails().getDefaultDocumentCheckList();
				for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
					documentCheckListValidation.addBeanToList(documentCheckListDTO);
				}
			}
			
			int benefitValueCount = 1;
			
			for (SpecialSelectValue specialSelectValue : particularsBasedOnBenefit) {
				Boolean isAlreadyAvailable = Boolean.FALSE;
				for (DocumentCheckListDTO documentCheckListDTO : documentCheckListValues) {
					if(documentCheckListDTO.getParticulars() != null && specialSelectValue.getValue().equalsIgnoreCase(documentCheckListDTO.getParticulars().getValue())
							&& specialSelectValue.getCommonValue().equalsIgnoreCase("Y")){
						if(specialSelectValue.getSpecialValue() != null && benefitValue.equalsIgnoreCase(specialSelectValue.getSpecialValue()) && benefitValueCount <= 5)
						{
							documentCheckListDTO.setParticulars(specialSelectValue);
							documentCheckListValidation.addBeanToList(documentCheckListDTO);
							benefitValueCount++;
						}else{
							documentCheckListDTO.setParticulars(specialSelectValue);
							documentCheckListValidation.addBeanToList(documentCheckListDTO);
						}
						isAlreadyAvailable = Boolean.TRUE;
						break;
					}
				}
				if(!isAlreadyAvailable){
					DocumentCheckListDTO documentDTO = new DocumentCheckListDTO();
					documentDTO.setParticulars(specialSelectValue);	
					//documentDTO.setBenefitId(specialSelectValue.getCommonValue());
					SelectValue setReceivedStatus = new SelectValue();
					setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
					setReceivedStatus.setValue("Not Applicable");
					documentDTO.setReceivedStatus(setReceivedStatus);
					if(specialSelectValue.getSpecialValue() != null && benefitValue.equalsIgnoreCase(specialSelectValue.getSpecialValue())
							&& specialSelectValue.getCommonValue().equalsIgnoreCase("Y"))
					{
						if(benefitValueCount <= 5){
							documentCheckListValidation.addBeanToList(documentDTO);
							benefitValueCount++;
						}
						
					}else{
						if(specialSelectValue.getCommonValue().equalsIgnoreCase("Y")){
							documentCheckListValidation.addBeanToList(documentDTO);
						}
						
					}
					
				}
			}

		
		}
	
		
	}
	
	private void showErrorMessage(String eMsg) {
		/*Label label = new Label(eMsg, ContentMode.HTML);
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}
	
public void setParticularsByDeselect() {
		
		if(documentCheckListValidation != null){
			
			List<DocumentCheckListDTO> documentCheckListValues = new ArrayList<DocumentCheckListDTO>();
			List<DocumentCheckListDTO> tableValue = documentCheckListValidation.getValues();
			documentCheckListValues.addAll(tableValue);
			documentCheckListValidation.setPaAckDocumentDetailsPage(this);
			/*Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
			String lob = (String) wrkFlowMap.get(SHAConstants.LOB);*/		
			documentCheckListValidation.initPresenter(SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED,SHAConstants.PA_LOB);
			
			List<SpecialSelectValue> particularsBasedOnBenefit = new ArrayList<SpecialSelectValue>();

			if(addOnCoversTable != null){
				List<AddOnCoversTableDTO> addOnCoversValue = addOnCoversTable.getValues();
				for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversValue) {
					if(addOnCoversTableDTO.getCovers() != null && addOnCoversTableDTO.getCovers().getValue() != null){
						if(referenceData.get(addOnCoversTableDTO.getCovers().getValue()) != null){
							BeanItemContainer<SpecialSelectValue> items = (BeanItemContainer<SpecialSelectValue>)referenceData.get(addOnCoversTableDTO.getCovers().getValue());
							if(items != null){
								particularsBasedOnBenefit.addAll(items.getItemIds());
							}						
							}
					}
				}
			}
			
			if(optionalCoversTable != null){
				List<AddOnCoversTableDTO> optionalCover = optionalCoversTable.getValues();
				for (AddOnCoversTableDTO addOnCoversTableDTO : optionalCover) {
					if(addOnCoversTableDTO.getOptionalCover() != null && addOnCoversTableDTO.getOptionalCover().getValue() != null){
						if(referenceData.get(addOnCoversTableDTO.getOptionalCover().getValue()) != null){
							BeanItemContainer<SpecialSelectValue> items = (BeanItemContainer<SpecialSelectValue>)referenceData.get(addOnCoversTableDTO.getOptionalCover().getValue());
							if(items != null){
								particularsBasedOnBenefit.addAll(items.getItemIds());
							}						
							}
					}
				}
			}
		
			BeanItemContainer<SelectValue> paritculars = new BeanItemContainer<SelectValue>(SelectValue.class);
			paritculars.addAll(particularsBasedOnBenefit);
			
			//this.referenceData.put("particulars",paritculars);
			
			if(! particularsBasedOnBenefit.isEmpty()){
				documentCheckListValidation.setReferenceData(referenceData);
				documentCheckListValidation.init();
			}else{
				existingReferenceMap.put("particulars",defaultParticularValues);
				documentCheckListValidation.setReferenceData(existingReferenceMap);
				documentCheckListValidation.init();
				List<DocumentCheckListDTO> docCheckList = this.bean.getDocumentDetails().getDefaultDocumentCheckList();
				for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
					documentCheckListValidation.addBeanToList(documentCheckListDTO);
				}
			}
			
			
			for (SpecialSelectValue specialSelectValue : particularsBasedOnBenefit) {
				Boolean isAlreadyAvailable = Boolean.FALSE;
				for (DocumentCheckListDTO documentCheckListDTO : documentCheckListValues) {
					if(documentCheckListDTO.getParticulars() != null && specialSelectValue.getValue().equalsIgnoreCase(documentCheckListDTO.getParticulars().getValue())
							&& specialSelectValue.getCommonValue().equalsIgnoreCase("Y")){
							documentCheckListDTO.setParticulars(specialSelectValue);
							documentCheckListValidation.addBeanToList(documentCheckListDTO);
						isAlreadyAvailable = Boolean.TRUE;
						break;
					}
				}
				if(!isAlreadyAvailable){
					DocumentCheckListDTO documentDTO = new DocumentCheckListDTO();
					documentDTO.setParticulars(specialSelectValue);	
					//documentDTO.setBenefitId(specialSelectValue.getCommonValue());
					SelectValue setReceivedStatus = new SelectValue();
					setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
					setReceivedStatus.setValue("Not Applicable");
					documentDTO.setReceivedStatus(setReceivedStatus);
					documentCheckListValidation.addBeanToList(documentDTO);
					
				}
			}
			
			

		
		}
	
		
	}

public void setDefaultParticularValues(
		BeanItemContainer<SelectValue> documentCheckListValuesContainer) {
	defaultParticularValues = documentCheckListValuesContainer;
	
}

public void setNextValue() {
	isNext = false;
}


public Boolean validationforOutPatientExpenses(){
	
	Boolean isOutPatient = true;
	
	List<AddOnCoversTableDTO> values = addOnCoversTable.getValues();
	for (AddOnCoversTableDTO addOnCoversTableDTO : values) {
		
		if(addOnCoversTableDTO.getCovers() != null && addOnCoversTableDTO.getCovers().getId() != null && 
				addOnCoversTableDTO.getCovers().getId().equals(ReferenceTable.OUTPATIENT_EXPENSES)){
			isOutPatient = uncheckBenefitsValue();
			
			break;
		}
		
	}
	
	return isOutPatient;
}

public Boolean validationforOutEarningParentSI(){
	
	Boolean isEarningParentSI = true;
	
	List<AddOnCoversTableDTO> values = addOnCoversTable.getValues();
	for (AddOnCoversTableDTO addOnCoversTableDTO : values) {
		
		if(addOnCoversTableDTO.getCovers() != null && addOnCoversTableDTO.getCovers().getId() != null && 
				addOnCoversTableDTO.getCovers().getId().equals(ReferenceTable.EARNING_PARENT_SI)){
			isEarningParentSI = uncheckBenefitsValue();
			break;
		}
		
	}
	
	return isEarningParentSI;
}

	public Boolean uncheckBenefitsValue(){
	
				
		if(chkDeath != null && chkDeath.getValue() != null && chkDeath.getValue()){
			return false;
		}
		if(chkPermanentPartialDisability != null && chkPermanentPartialDisability.getValue() != null && chkPermanentPartialDisability.getValue()){
			return false;
		}
		
		if(chkPermanentTotalDisability != null && chkPermanentTotalDisability.getValue() != null && chkPermanentTotalDisability.getValue()){
			return false;
		}
		
		if(chkTemporaryTotalDisability != null && chkTemporaryTotalDisability.getValue() != null && chkTemporaryTotalDisability.getValue()){
			return false;
		}
		
		if( chkhospitalization != null && chkhospitalization.getValue() != null && chkhospitalization.getValue()){
			return false;
		}
		
		if(chkPartialHospitalization != null && chkPartialHospitalization.getValue() != null && chkPartialHospitalization.getValue()){
			return false;
		}
		
		return true;
	
	
}
	
	public void resetBenefitsValue(){
		if(chkDeath != null){
			chkDeath.setValue(false);
		}
		if(chkTemporaryTotalDisability != null){
			chkTemporaryTotalDisability.setValue(false);
		}
		
		if(chkPermanentTotalDisability != null){
			chkPermanentTotalDisability.setValue(false);
		}
		
		if(chkPermanentPartialDisability != null){
			chkPermanentPartialDisability.setValue(false);
		}
		
		if(chkhospitalization != null){
			chkhospitalization.setValue(false);
		}
		
		if(chkPartialHospitalization != null){
			chkPartialHospitalization.setValue(false);
		}
	}

	public void buildNomineeLayout() {
		
		if(nomineeDetailsTable != null) { 
			documentDetailsPageLayout.removeComponent(nomineeDetailsTable);
		}
		if(legalHeirLayout != null) {
			documentDetailsPageLayout.removeComponent(legalHeirLayout);
		}
		
		if(chkNomineeDeceased != null){
			documentDetailsPageLayout.removeComponent(chkNomineeDeceased);
		}
		
		nomineeDetailsTable = nomineeDetailsTableInstance.get();
		
		nomineeDetailsTable.init("", false, false);
		chkNomineeDeceased = null;
		if(bean.getClaimDTO().getNewIntimationDto().getNomineeList() != null && !bean.getClaimDTO().getNewIntimationDto().getNomineeList().isEmpty()) { 
			nomineeDetailsTable.setTableList(bean.getClaimDTO().getNewIntimationDto().getNomineeList());
			nomineeDetailsTable.generateSelectColumn();
			chkNomineeDeceased = new CheckBox("Nominee Deceased");
			if(bean.getPreauthDTO().getPreauthDataExtractionDetails().getNomineeDeceasedFlag() != null && bean.getPreauthDTO().getPreauthDataExtractionDetails().getNomineeDeceasedFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				chkNomineeDeceased.setValue(Boolean.TRUE);
			}
		}	
		
		documentDetailsPageLayout.addComponent(nomineeDetailsTable);
		
		if(chkNomineeDeceased != null){
			documentDetailsPageLayout.addComponent(chkNomineeDeceased);
			addNomineeDeceasedListener();
		}
		
		boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
		
		legalHeirLayout = new VerticalLayout();
		
		legalHeirDetails = legalHeirObj.get();
		
		relationshipContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		Map<String,Object> refData = new HashMap<String, Object>();
		relationshipContainer.addAll(bean.getPreauthDTO().getLegalHeirDto().getRelationshipContainer());
		refData.put("relationship", relationshipContainer);
		legalHeirDetails.setReferenceData(refData);
		legalHeirDetails.init(bean.getPreauthDTO());
		legalHeirDetails.setViewColumnDetails();
		legalHeirLayout.addComponent(legalHeirDetails);
		documentDetailsPageLayout.addComponent(legalHeirLayout);

		if(isNomineeDeceased()){
			enableLegalHeir = Boolean.TRUE;
			nomineeDetailsTable.setEnabled(false);
		}
		
		if(enableLegalHeir) {
			
			legalHeirDetails.addBeanToList(bean.getPreauthDTO().getLegalHeirDTOList());
			legalHeirDetails.getBtnAdd().setEnabled(true);
		}
		else {
			legalHeirDetails.deleteRows();
			legalHeirDetails.getBtnAdd().setEnabled(false);
		}	
		
	}
	
	private void addNomineeDeceasedListener(){
		 chkNomineeDeceased
			.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					
					if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
					{
					boolean value = (Boolean) event.getProperty().getValue();
					
					if(value){
						if(nomineeDetailsTable != null){
							nomineeDetailsTable.setEnabled(false);
						}
						if(legalHeirDetails != null){
							if(bean.getPreauthDTO().getLegalHeirDTOList() != null && !bean.getPreauthDTO().getLegalHeirDTOList().isEmpty()){
								legalHeirDetails.addBeanToList(bean.getPreauthDTO().getLegalHeirDTOList());
							}
							legalHeirDetails.getBtnAdd().setEnabled(true);
						
						}
					}else{
						if(nomineeDetailsTable != null){
							nomineeDetailsTable.setEnabled(true);
						}
						if(legalHeirDetails != null){
							legalHeirDetails.deleteRows();
							legalHeirDetails.getBtnAdd().setEnabled(false);
						}
					}
					 
					}
							
					}
					
			});
	 }
	 
	 private Boolean isNomineeDeceased(){
		 if(chkNomineeDeceased != null && chkNomineeDeceased.getValue() != null && chkNomineeDeceased.getValue()){
			 return true;
		 }
		 return false;
	 }
}
