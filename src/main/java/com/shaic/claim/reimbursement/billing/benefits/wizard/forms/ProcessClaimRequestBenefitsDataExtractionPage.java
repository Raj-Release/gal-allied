/**
 * 
 */
package com.shaic.claim.reimbursement.billing.benefits.wizard.forms;

/**
 * @author ntv.vijayar
 *
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.intimation.create.ViewBasePolicyDetails;
import com.shaic.claim.reimbursement.billing.benefits.wizard.pages.ProcessClaimRequestBenefitsDataExtractionPresenter;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.pages.HopsitalCashBenefitDTO;
import com.shaic.claim.rod.wizard.pages.HospitalCashProductDetailsTable;
import com.shaic.claim.rod.wizard.tables.DocumentCheckListTable;
import com.shaic.claim.rod.wizard.tables.RODQueryDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.paclaim.health.reimbursement.listenertable.PAHealthUploadedDocumentsListenerTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author ntv.vijayar
 *
 */
public class ProcessClaimRequestBenefitsDataExtractionPage  extends ViewComponent {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BeanFieldGroup<DocumentDetailsDTO> binder;

	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	private ComboBox cmbDocumentsReceivedFrom;

	private DateField documentsReceivedDate;
		
	private CheckBox chkhospitalization;
	
	private CheckBox chkPreHospitalization;
	
	private CheckBox chkPostHospitalization;
	
	private CheckBox chkPartialHospitalization;
	
	private CheckBox chkLumpSumAmount;
	
	private CheckBox chkAddOnBenefitsHospitalCash;
	
	private CheckBox chkAddOnBenefitsPatientCare;
	
	private VerticalLayout documentDetailsPageLayout;

	//private GWizard wizard;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	 
	private BeanItemContainer<SelectValue> docReceivedFromRequest ;
	  
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private  DocumentDetailsDTO docDTO ;
	
	@Inject
	private AddOnBenefitsDataExtractionPage addOnBenefitsPage;
	
	@EJB
	private MasterService masterService;
	
	//MED-PRD-076	
	private CheckBox chkHospitalCash;
	 
	private ComboBox cmbDiagnosisHospitalCash;
	
	private BeanItemContainer<SelectValue> diagnosisHospitalCashContainer;
	
	private ComboBox cmbHospitalCashDueTo;
	
	private BeanItemContainer<SelectValue> hospitalCashDueTo;
	
	@Inject
	private Instance<HospitalCashProductDetailsTable> hospitalCashProductDetailsInstance;
	
	private HospitalCashProductDetailsTable hospitalCashProductDetailsTable;
	
	private DateField dateOfAdmission;
	private DateField dateOfDischarge;
	
	private OptionGroup optPatientDayCare;
	
	private BeanItemContainer<SelectValue> patientDayCareValueContainer;
	
	private ComboBox cmbPatientDayCareValue;
	
	@Inject
	private ViewBasePolicyDetails viewBasePolicyDetail;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	 
	@PostConstruct
	public void init() {

	}
	
	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard) {
		this.bean = bean;
		//this.wizard = wizard;
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<DocumentDetailsDTO>(
				DocumentDetailsDTO.class);
		this.binder.setItemDataSource(this.bean
				.getDocumentDetails());
		//this.binder.setItemDataSource(new DocumentDetailsDTO());
	}
	
	public Component getContent() {
		initBinder();
		showBasePolicyAlert();
		if(masterService.doGMCPolicyCheckForICR(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber())){
			showICRMessage();
		}
		
		
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		documentDetailsPageLayout = new VerticalLayout();
		documentDetailsPageLayout.setSpacing(false);
		documentDetailsPageLayout.setMargin(true);
			
		HorizontalLayout documentDetailsLayout = buildDocumentDetailsLayout(); 
		documentDetailsLayout.setCaption("Document Details");
//		documentDetailsLayout.setWidth("100%");
		
		HorizontalLayout billClassificationLayout = buildBillClassificationLayout();
//		billClassificationLayout.setWidth("100%");
		
		//documentDetailsPageLayout = new VerticalLayout(documentDetailsLayout,billClassificationLayout);
		
		VerticalLayout docAckAndClassificationLayout = new VerticalLayout(documentDetailsLayout,billClassificationLayout);
		docAckAndClassificationLayout.setSpacing(false);
		Panel layoutPanel = new Panel();
		layoutPanel.setHeight("100%");
		layoutPanel.setWidth("100%");
		layoutPanel.setContent(docAckAndClassificationLayout);
		
		
		documentDetailsPageLayout.addComponent(layoutPanel);
		if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
			|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
					this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			hospitalCashProductDetailsTable = hospitalCashProductDetailsInstance.get();
			hospitalCashProductDetailsTable.clearTableItems();
			hospitalCashProductDetailsTable.init(bean);
			documentDetailsPageLayout.addComponent(hospitalCashProductDetailsTable);
		}else{
			addOnBenefitsPage.init(this.bean.getUploadDocumentsDTO(),ReferenceTable.PROCESS_CLAIM_REQUEST_BENEFITS, false);
			addOnBenefitsPage.getContent();
			documentDetailsPageLayout.addComponent(addOnBenefitsPage);
		}
		
		addListener();
		setTableValues();
		showOrHideValidation(false);
		return documentDetailsPageLayout;
		
	}
	public void getErrorMessage(String eMsg){
	 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
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
	
	
	private HorizontalLayout buildDocumentDetailsLayout()
	{
		cmbDocumentsReceivedFrom = binder.buildAndBind("Documents Recieved From" , "documentsReceivedFrom" , ComboBox.class);
		//Vaadin8-setImmediate() cmbDocumentsReceivedFrom.setImmediate(true);
		cmbDocumentsReceivedFrom.setEnabled(false);
			
		documentsReceivedDate = binder.buildAndBind("Documents Recieved Date", "documentsReceivedDate", DateField.class);
		documentsReceivedDate.setEnabled(false);
		//documentsReceivedDate.setValue(new Date());

		FormLayout detailsLayout1 = new FormLayout(cmbDocumentsReceivedFrom,documentsReceivedDate);
		
		cmbDiagnosisHospitalCash = (ComboBox) binder.buildAndBind("Diagnosis Hospital Cash",
				"diagnosisHospitalCash", ComboBox.class);
		
		cmbHospitalCashDueTo = (ComboBox) binder.buildAndBind("Hospital Cash Due To",
				"hospitalCashDueTo", ComboBox.class);
		
		FormLayout detailsLayout2 = new FormLayout(cmbDiagnosisHospitalCash, cmbHospitalCashDueTo);
		
		dateOfAdmission = binder.buildAndBind("Date of\nAdmission","dateOfAdmission",DateField.class);
		
		dateOfDischarge = binder.buildAndBind("Date Of\nDischarge", "dateOfDischarge", DateField.class);
		
		optPatientDayCare = (OptionGroup) binder.buildAndBind("Day Care" , "patientDayCare" , OptionGroup.class);
		
		cmbPatientDayCareValue = (ComboBox) binder.buildAndBind("Day Care Procedure",
				"patientDayCareDueTo", ComboBox.class);
		
		optPatientDayCare.addItems(getReadioButtonOptions());
		optPatientDayCare.setItemCaption(true, "Yes");
		optPatientDayCare.setItemCaption(false, "No");
		optPatientDayCare.setStyleName("horizontal");
		

		
		FormLayout detailsLayout3 = new FormLayout(dateOfAdmission, dateOfDischarge);
		
		FormLayout detailsLayout4 = new FormLayout(optPatientDayCare,cmbPatientDayCareValue);
		detailsLayout4.setMargin(false);
		
		cmbDiagnosisHospitalCash.setVisible(false);
		cmbHospitalCashDueTo.setVisible(false);
		dateOfAdmission.setVisible(false);
		dateOfDischarge.setVisible(false);
		optPatientDayCare.setVisible(false);
		cmbPatientDayCareValue.setVisible(false);
		
		
		//added for new product
		if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
          || (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
        		  this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			if(optPatientDayCare.getValue().toString() == "true"){
				optPatientDayCare.select(true);
				cmbPatientDayCareValue.setEnabled(true);
				cmbPatientDayCareValue.setVisible(true);
				if(bean.getDocumentDetails().getPatientDayCareDueTo() != null){
				cmbPatientDayCareValue.setValue(bean.getDocumentDetails().getPatientDayCareDueTo());
				}
			}
			else{
			optPatientDayCare.select(false);
			}
			mandatoryFields.add(cmbDiagnosisHospitalCash);
			mandatoryFields.add(cmbHospitalCashDueTo);
			setRequiredAndValidation(cmbDiagnosisHospitalCash);
			setRequiredAndValidation(cmbHospitalCashDueTo);
			cmbDiagnosisHospitalCash.setVisible(true);
			cmbHospitalCashDueTo.setVisible(true);
			cmbHospitalCashDueTo.setRequired(true);
			cmbDiagnosisHospitalCash.setRequired(true);
			dateOfAdmission.setVisible(true);
			dateOfDischarge.setVisible(true);
			optPatientDayCare.setVisible(true);
			
		}
		
		addDateOfAdmissionListener();
				
		HorizontalLayout docDetailsLayout = new HorizontalLayout(detailsLayout1, detailsLayout2, detailsLayout3);
//		HorizontalLayout docDetailsLayout = new HorizontalLayout(detailsLayout1, detailsLayout3);
		HorizontalLayout docDetailsLayout1 = new HorizontalLayout(detailsLayout4);
		docDetailsLayout.setMargin(true);
		docDetailsLayout.setSpacing(true);
		docDetailsLayout1.setMargin(true);
		docDetailsLayout1.setSpacing(true);
		
		VerticalLayout wholeVLayout = new VerticalLayout(docDetailsLayout,docDetailsLayout1);
		HorizontalLayout docDetailsLayout3 = new HorizontalLayout(wholeVLayout) ;
		return docDetailsLayout3;
	}
	
	private HorizontalLayout buildBillClassificationLayout() {
		
		chkhospitalization = binder.buildAndBind("Hospitalisation", "hospitalization", CheckBox.class);
		
		chkPreHospitalization = binder.buildAndBind("Pre-Hospitalisation", "preHospitalization", CheckBox.class);
		
		chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalization", CheckBox.class);
		chkPartialHospitalization = binder.buildAndBind("Partial-Hospitalisation", "partialHospitalization", CheckBox.class);
		
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
		
		chkhospitalization.setEnabled(false);
		//chkPreHospitalization.setEnabled(false);
		//chkPostHospitalization.setEnabled(false);
		chkPartialHospitalization.setEnabled(false);
		//System.out.println("---the claimType value---"+this.bean.getClaimDTO().getClaimTypeValue());
		if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			chkPartialHospitalization.setEnabled(true);
		}
		else
		{
			chkhospitalization.setEnabled(true);
		}
		
		chkHospitalCash = binder.buildAndBind("Hospital Cash", "hospitalCash", CheckBox.class);
		chkHospitalCash.setEnabled(false);
		chkHospitalCash.setValue(false);
		
		if(null != this.bean.getDocumentDetails().getHospitalCashFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalCashFlag()))
		{
			if(null != chkHospitalCash){
				chkHospitalCash.setValue(true);
			}
		}
		
		FormLayout classificationLayout1 = new FormLayout(chkhospitalization,chkLumpSumAmount);
		FormLayout classificationLayout2 = new FormLayout(chkPreHospitalization,chkAddOnBenefitsHospitalCash);
		FormLayout classificationLayout3 = new FormLayout(chkPostHospitalization,chkAddOnBenefitsPatientCare);
		FormLayout classificationLayout4 = new FormLayout(chkPartialHospitalization, chkHospitalCash);
		
		HorizontalLayout billClassificationLayout = new HorizontalLayout(classificationLayout1,classificationLayout2,classificationLayout3,classificationLayout4);
		//billClassificationLayout.setCaption("Document Details");
		billClassificationLayout.setCaption("Bill Classification");
		billClassificationLayout.setSpacing(false);
		billClassificationLayout.setMargin(false);
//		billClassificationLayout.setWidth("100%");
		billClassificationLayout.setEnabled(false);
		return billClassificationLayout;
	}
	
	
	
	//private ReconsiderRODRequestTable buildReconsiderRequestLayout()
	
	
	
	
	/*private void addBillClassificationLister()

	{
		chkhospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					// chkPostHospitalization.setEnabled(true);
					// chkPreHospitalization.setEnabled(true);
					 
					 if(validateBillClassification())
					 {
						 HorizontalLayout layout = new HorizontalLayout(
									new Label("Already hospitalization is existing for this claim."));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkhospitalization.setValue(false);
					 }
					
					 
				 }
				 else
				 {
					 //chkPostHospitalization.setEnabled(false);
					 //chkPreHospitalization.setEnabled(false);
					 
					 if(validateBillClassification())
					 {
						 HorizontalLayout layout = new HorizontalLayout(
									new Label("Pre or Post hospitalization cannot exist without hospitalization"));
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
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
					

				 }
			}
		});
		
		chkPreHospitalization .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					
					 if(validateBillClassification())
					 {
						 HorizontalLayout layout = new HorizontalLayout(
									new Label("Pre hosptilization cannot be selected without selecting hospitalization or partial hosptilization"));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkPreHospitalization.setValue(false);
					 }	
				 }
			}
		});
		
		chkPostHospitalization .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 if(validateBillClassification())
					 {
						 HorizontalLayout layout = new HorizontalLayout(
									new Label("Post hosptilization cannot be selected without selecting hospitalization or partial hosptilization"));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkPostHospitalization.setValue(false);
					 }
			}
			}
		});
		
		chkPartialHospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 
					 if(validateBillClassification())
					 {
						 HorizontalLayout layout = new HorizontalLayout(
									new Label("Already hospitalization is existing for this claim."));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkPartialHospitalization.setValue(false);
					 }
					
				 }
				 else
				 {
					 
					 if(validateBillClassification())
					 {
						 HorizontalLayout layout = new HorizontalLayout(
									new Label("Pre or Post hospitalization cannot exist without Partial hospitalization"));
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							if(null != chkPreHospitalization)
							{
								chkPreHospitalization.setValue(false);
							}
							if(null != chkPostHospitalization)
							{
								chkPostHospitalization.setValue(false);
							}
					 }
				 }
			}
		});
	}*/
	
	
	/*private int getDifferenceBetweenDates(Date value)
	{
		
		long currentDay = new Date().getTime();
		long enteredDay = value.getTime();
		int diff = (int)((currentDay-enteredDay))/(1000 * 60 * 60 * 24);
		return diff;
	}*/
	
	
	
	public void loadContainerDataSources(Map<String, Object> referenceDataMap)
	{	 
		 docReceivedFromRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("docReceivedFrom");
		 cmbDocumentsReceivedFrom.setContainerDataSource(docReceivedFromRequest);
		 cmbDocumentsReceivedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbDocumentsReceivedFrom.setItemCaptionPropertyId("value");
		 
		 for(int i = 0 ; i<docReceivedFromRequest.size() ; i++)
		 	{
				if (("Insured").equalsIgnoreCase(docReceivedFromRequest.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
				}
			}
		 
		 
		//added for new product
		diagnosisHospitalCashContainer = (BeanItemContainer<SelectValue>) referenceDataMap.get("diagnosisHospitalCashContainer");
		 
         cmbDiagnosisHospitalCash.setContainerDataSource(diagnosisHospitalCashContainer);
         cmbDiagnosisHospitalCash.setItemCaptionMode(ItemCaptionMode.PROPERTY);
         cmbDiagnosisHospitalCash.setItemCaptionPropertyId("value");
         
         hospitalCashDueTo = (BeanItemContainer<SelectValue>) referenceDataMap.get("hospitalCashDueTo");
         cmbHospitalCashDueTo.setContainerDataSource(hospitalCashDueTo);
         cmbHospitalCashDueTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
         cmbHospitalCashDueTo.setItemCaptionPropertyId("value");
         
         patientDayCareValueContainer = (BeanItemContainer<SelectValue>) referenceDataMap.get("patientDayCareValueContainer");
         cmbPatientDayCareValue.setContainerDataSource(patientDayCareValueContainer);
         cmbPatientDayCareValue.setItemCaptionMode(ItemCaptionMode.PROPERTY);
         cmbPatientDayCareValue.setItemCaptionPropertyId("value");
		 
		// documentCheckList.setReceivedStatus((BeanItemContainer<SelectValue>)referenceDataMap.get("docReceivedStatus"));
		 this.docDTO = (DocumentDetailsDTO) referenceDataMap.get("billClaissificationDetails");
		 setValuesFromDTO();

	}
	
	public boolean validatePage() {
		
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		
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
		
		
		if(!((null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) || 
				(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) ||
				
				(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue()) ||
				
				(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue()) ||
				
				(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue()) ||
				
				(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue()) ||
				
				(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue()) ||
				
				(null != this.chkHospitalCash && null != this.chkHospitalCash.getValue() && this.chkHospitalCash.getValue())))
		{
			hasError = true;
			eMsg.append("Please select any one bill classification value</br>");
		}
		
		
		if(/*this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				||*/ (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			
			if(null == dateOfAdmission.getValue())
			{
				hasError = true;
				eMsg.append("Please enter admission date. </br>");
			}
			
			if(null == dateOfDischarge.getValue())
			{
				hasError = true;
				eMsg.append("Please enter discharge date. </br>");
			}
			
			
			if(cmbDiagnosisHospitalCash == null || cmbDiagnosisHospitalCash.isEmpty()){
				hasError = true;
				eMsg.append("Please select Hospital Diagnosis.</br>");
			}
			
			if(cmbHospitalCashDueTo == null || cmbHospitalCashDueTo.isEmpty()){
				hasError = true;
				eMsg.append("Please select Hospital Cash Due To.</br>");
			}
			
			if(optPatientDayCare.getValue().toString() == "true"){
				if(cmbPatientDayCareValue == null || cmbPatientDayCareValue.isEmpty()){
					hasError = true;
					eMsg.append("Please select Patient Day Care Value");
				}
			}
			
			if(hospitalCashProductDetailsTable != null){
				List<HopsitalCashBenefitDTO> hospitalCashBenefit = hospitalCashProductDetailsTable.getValues();
				Long totalNoOfDays =0l;
				Long perDayAmount =0l;
				Long noOfDaysAllowed =0l;
				for (HopsitalCashBenefitDTO hospitalCashBenefit1 : hospitalCashBenefit){
					if(hospitalCashBenefit1.getHospitalCashDays() != null && !hospitalCashBenefit1.getHospitalCashDays().equals("")){
						totalNoOfDays = totalNoOfDays + Long.valueOf(hospitalCashBenefit1.getHospitalCashDays());
					}
					if(hospitalCashBenefit1.getHospitalCashPerDayAmt() != null && !hospitalCashBenefit1.getHospitalCashPerDayAmt().equals("")){
						perDayAmount = perDayAmount + Long.valueOf(hospitalCashBenefit1.getHospitalCashPerDayAmt());
					}
					if(hospitalCashBenefit1.getNoOfDaysAllowed() != null && !hospitalCashBenefit1.getNoOfDaysAllowed().equals("")){
						noOfDaysAllowed = noOfDaysAllowed + Long.valueOf(hospitalCashBenefit1.getNoOfDaysAllowed());
					}
				}
				if(totalNoOfDays == 0){
					hasError = true;
					eMsg.append("Please enter any one No Of Days Claimed table Values.</br> ");
				}
				if(perDayAmount==0){
					hasError = true;
					eMsg.append("Please enter any one Per Day Claimed Amount table Values.</br> ");
				}
				if(noOfDaysAllowed == 0){
					hasError = true;
					eMsg.append("Please enter any one No Of Days Allowed table Values .</br>");
				}
			}
		}
				
		
		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
			hasError = true;
			return !hasError;
		} 
		else 
		{
			try {
				if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
					this.binder.commit();
					if ((null != bean.getDocumentDetails()
							.getHospitalCash() && (bean
							.getDocumentDetails().getHospitalCash()))) {
					fireViewEvent(ProcessClaimRequestBenefitsDataExtractionPresenter.SAVE_HOSPITAL_CASH_PHC_TABLE_VALUES, this.bean);
					}
				}
				else{
				this.binder.commit();
				if(null != addOnBenefitsPage)
				{
					addOnBenefitsPage.isValid();
				}
				if(null != chkAddOnBenefitsPatientCare && null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.getValue())
				{
					fireViewEvent(ProcessClaimRequestBenefitsDataExtractionPresenter.SAVE_PATIENT_CARE_TABLE_VALUES, this.bean);
				}
				if (null != chkAddOnBenefitsHospitalCash && null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.getValue())
				{
					fireViewEvent(ProcessClaimRequestBenefitsDataExtractionPresenter.SAVE_HOSPITAL_CASH_TABLE_VALUES, this.bean);
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

	
	private void setTableValues()
	{
		//MED-PRD-076
		if(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
         || (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
        		 this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			if(null != hospitalCashProductDetailsTable)
			{
				List<HopsitalCashBenefitDTO> hospiitalCashList = bean.getUploadDocumentsDTO().getHopsitalCashBenefitDTO();
				if(null != hospiitalCashList && !hospiitalCashList.isEmpty())
				{
					//hospitalCashProductDetailsTable.removeAllItems();
					for (HopsitalCashBenefitDTO hospiitalCashList2 : hospiitalCashList) {
						hospitalCashProductDetailsTable.addBeanToList(hospiitalCashList2);
					}
					
				}
			}
		}
	}
	
	public  void setTableValuesToDTO()
	{
		//invoke patient care table list here.
		//if(n)
		/*if(null != this.patientCareAddOnBenefits && null != (this.patientCareAddOnBenefits.getValue()) && ("Yes").equalsIgnoreCase(this.patientCareAddOnBenefits.getValue().toString()) && null != patientCareTableObj )
		{
			List<PatientCareDTO> patientList = patientCareTableObj.getValues();
			this.bean.getUploadDocumentsDTO().setPatientCareDTO(patientList);
		}*/
		if(/*this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
          || */(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
        		  this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			if(null != this.bean.getDocumentDetails().getHospitalCashFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalCashFlag()))
			{
				List<HopsitalCashBenefitDTO> hospitalCashBenefit = hospitalCashProductDetailsTable.getValues();
				this.bean.getUploadDocumentsDTO().setHopsitalCashBenefitDTO(hospitalCashBenefit);
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
		
		if(null != documentDetails.getDiagnosisHospitalCash())
		{
			 for(int i = 0 ; i<diagnosisHospitalCashContainer.size() ; i++)
			 	{
					if ( documentDetails.getDiagnosisHospitalCash().getValue().equalsIgnoreCase(diagnosisHospitalCashContainer.getIdByIndex(i).getValue()))
					{
						this.cmbDiagnosisHospitalCash.setValue(diagnosisHospitalCashContainer.getIdByIndex(i));
					}
				}
		}
		
		if(null != documentDetails.getHospitalCashDueTo())
		{
			 for(int i = 0 ; i<hospitalCashDueTo.size() ; i++)
			 	{
					if ( documentDetails.getHospitalCashDueTo().getValue().equalsIgnoreCase(hospitalCashDueTo.getIdByIndex(i).getValue()))
					{
						this.cmbHospitalCashDueTo.setValue(hospitalCashDueTo.getIdByIndex(i));
					}
				}
		}
		
		if(bean.getDocumentDetails().getPatientDayCareDueTo() != null){
			this.cmbPatientDayCareValue.setValue(bean.getDocumentDetails().getPatientDayCareDueTo());
		}
		
	}
	
	public Boolean validateBillClassification() 
	{
		Boolean isError = false;
		if(null != docDTO)
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
		}
		else
		{
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
				if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
				{
					isError = true;
				}
				if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
				{
					isError = true;
				}
			}
			else if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && !this.chkPartialHospitalization.getValue())
			{
				if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
				{
					isError = true;
				}
				if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
				{
					isError = true;
				}
			}
			else
			{
				isError = true;
			}
		}
		return isError;
		
	}
	
	/**
	 * The below method will set the per day amount
	 * for hospital cash and patient care. This amount
	 * is returned by procedure.
	 * */

	public void setHospitalCashValues(List<Object> benefitList) {
		/*if(null != benefitList && !benefitList.isEmpty() && null != benefitList.get(3))
		this.bean.getUploadDocumentsDTO().setHospitalCashPerDayAmt(String.valueOf(benefitList.get(3)));*/
		
		addOnBenefitsPage.setHospitalCashValues(benefitList);
		
	}

	public void setPatientCareValues(List<Object> benefitList) {
		/*if(null != benefitList && !benefitList.isEmpty() && null != benefitList.get(3))
			this.bean.getUploadDocumentsDTO().setPatientCarePerDayAmt(String.valueOf(benefitList.get(3)));*/
		addOnBenefitsPage.setPatientCareValues(benefitList);
		
	}

	public void generateFieldsBasedOnHospitalCashBenefits(Boolean selectedValue) {
		
		addOnBenefitsPage.generateFieldsBasedOnHospitalCashBenefits(selectedValue);
	}

	public void generateFieldsBasedOnPatientCareBenefits(Boolean selectedValue) {
		addOnBenefitsPage.generateFieldsBasedOnPatientCareBenefits(selectedValue);
	}

	public void setBenefitsData(List<AddOnBenefitsDTO> benefitsDTO) {
		// TODO Auto-generated method stub
		this.bean.setAddOnBenefitsDTO(benefitsDTO);
	}
	
	private void showICRMessage(){
		String msg = SHAConstants.ICR_MESSAGE;
		Label successLabel = new Label("<div style = 'text-align:center;'><b style = 'color: red;'>"+msg+"</b></div>", ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout firstForm = new VerticalLayout(successLabel,homeButton);
		firstForm.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		firstForm.setSpacing(true);
		firstForm.setMargin(true);
		firstForm.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(firstForm);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.setWidth("20%");
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
	}
	
	private void showBasePolicyAlert(){
	// CR2019257 Base Policy
	if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getBasePolicyNo() != null){
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "View Base Policy");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(SHAConstants.BASEPOLICY + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		Button BaseViewButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;
			
	
		@Override
		public void buttonClick(ClickEvent event) {
			}
			});
			BaseViewButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					
					if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getBasePolicyNo() != null)
					{
					VerticalLayout verticalLayout = null;
						VerticalLayout vLayout = new VerticalLayout();
						Policy policy = policyService.getByPolicyNumber(bean.getClaimDTO().getNewIntimationDto().getPolicy().getBasePolicyNo());
						Intimation intimation=intimationService.getIntimationByKey(bean.getClaimDTO().getNewIntimationDto().getKey());
						//Policy policy = policyService.getPolicy(intimation.getPolicy().getBasePolicyNo());
						if(policy !=null){
						viewBasePolicyDetail.setPolicyServiceAndPolicy(policyService, policy,
								masterService, intimationService);	
						viewBasePolicyDetail.initView();
						UI.getCurrent().addWindow(viewBasePolicyDetail);
						}
						else{
							getErrorMessage("Intimation not available for this Base policy");
						}
						
				}
					else
					{
						getErrorMessage("Base Policy is not available");
					}
					
				}
			});	
	}
}
	
	private void addDateOfAdmissionListener()
	{
		dateOfAdmission.addValueChangeListener(new Property.ValueChangeListener() {			
			private static final long serialVersionUID = -1774887765294036092L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date dateOfAdmissionValue = (Date)event.getProperty().getValue();
				Date policyFrmDate = bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyFromDate();
				Date policyToDate = bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyToDate();
				if (null != dateOfAdmissionValue && null != policyFrmDate 
						&& null != policyToDate) {
					//if ( !(((DateUtils.isSameDay(dateOfAdmissionValue, policyFrmDate)  || dateOfAdmissionValue.after(policyFrmDate))) && dateOfAdmissionValue.before(policyToDate))) {
					if (!SHAUtils.isDateOfAdmissionWithPolicyRange(policyFrmDate,policyToDate,dateOfAdmissionValue)) {
						dateOfAdmission.setValue(null);
						 	HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
									.createErrorBox("Admission Date is not in range between Policy From Date and Policy To Date.", buttonsNamewithType);
					}
					String admissionDate= SHAUtils.changeDatetoString((dateOfAdmission.getValue()));
					bean.setDateOfAdmission(admissionDate);
					hospitalCashProductDetailsTable.init(bean);
				}
				
				if(dateOfDischarge != null && dateOfDischarge.getValue() != null){
					Date disChargeDate = dateOfDischarge.getValue();
					Long diffDays = SHAUtils.getDaysBetweenDate( dateOfAdmissionValue,disChargeDate);
					if(diffDays<0)
					{
						dateOfAdmission.setValue(null);
						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
						GalaxyAlertBox.createAlertBox("Admission date is after the Discharge date. Please enter valid Admission date", buttonsNamewithType);
						
					}
				
				}//IMSSUPPOR-30923
				if(dateOfAdmission != null && dateOfAdmission.getValue() != null){
					Date admissionDate = dateOfAdmission.getValue();
					bean.getDocumentDetails().setDateOfAdmission(admissionDate);
				}
			}
		});
		
		dateOfDischarge.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Date dischargeDate = (Date)event.getProperty().getValue();
				if(null != dateOfAdmission)
				{
					Date admissionDate = dateOfAdmission.getValue();
					if(null != admissionDate)
					{
						Long diffDays = SHAUtils.getDaysBetweenDate( admissionDate,dischargeDate);
						if(diffDays<0)
						{
							dateOfDischarge.setValue(null);
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createAlertBox("Discharge date is before than admission date. Please enter valid discharge date", buttonsNamewithType);
							
						}
					}
					String dischargeDate1= SHAUtils.changeDatetoString((dateOfDischarge.getValue()));
					bean.setDateOfDischarge(dischargeDate1);
					//IMSSUPPOR-30923
					bean.getDocumentDetails().setDateOfDischarge(SHAUtils.getDateFormatDate(dischargeDate1));

					hospitalCashProductDetailsTable.init(bean);
				}
			}
		});
	}
	
	protected void addListener() {
		optPatientDayCare
	.addValueChangeListener(new Property.ValueChangeListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			Boolean isChecked = false;
			if (event.getProperty() != null
					&& event.getProperty().getValue().toString() == "true") {
				cmbPatientDayCareValue.setVisible(true);
				cmbPatientDayCareValue.setRequired(true);
				isChecked = true;
//				fireViewEvent(
//						ProcessClaimRequestBenefitsDataExtractionPresenter.CLAIM_REQUEST_BENEFITS_PATIENT_DAYCARE_DROPDOWN_VALUES,bean);
			}else if(event.getProperty() != null
					&& event.getProperty().getValue().toString() == "false"){
				cmbPatientDayCareValue.setVisible(false);
				cmbPatientDayCareValue.setRequired(false);
				cmbPatientDayCareValue.setValue(null);
				
			}
		}
	});
	}
}

