/**
 * 
 */
package com.shaic.claim.reimbursement.billing.benefits.wizard.forms;

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

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.view.ParallelInvestigationDetails;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.wizard.AddOnBenefitsListenerTable;
import com.shaic.claim.reimbursement.billing.wizard.AddOnBenefitsPatientCareListenerTable;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.tables.DocumentCheckListTable;
import com.shaic.claim.rod.wizard.tables.RODQueryDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
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
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class ProcessClaimRequestBenefitsDecisionPage  extends ViewComponent {
	

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

	private GWizard wizard;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	 
	private BeanItemContainer<SelectValue> docReceivedFromRequest ;
	  
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private  DocumentDetailsDTO docDTO ;
	
	
	@Inject
	private Instance<AddOnBenefitsListenerTable> addOnBenefitsListenerTable;
	
	private AddOnBenefitsListenerTable addOnBenefitsListenerTableObj;
	
	@Inject
	private Instance<AddOnBenefitsPatientCareListenerTable> addOnBenefitsPatientCareListenerTable;
	
	private AddOnBenefitsPatientCareListenerTable addOnBenefitsPatientCareLiseterObj;
	
	//private Button btnCancelROD;
	
	private ComboBox cmbCancel;
	
	private TextArea txtCancelRemarks;
	
	////private static Window popup;
	
	private VerticalLayout cancelRodLayout = new VerticalLayout();
	
	@Inject
	private Instance<BillingBenefitsButtonsUI> billingProcessButtonInstance;
	
	private BillingBenefitsButtonsUI billingProcessButtonObj;
	
	@Inject
	private Instance<FinancialBenefitsButtonsUI> financialButtonInstance;
	
	private FinancialBenefitsButtonsUI financialButtonObj;
	
	public Double totalApprovedAmt = 0d;
	
	//added for new product
	private CheckBox chkHospitalCash;
	 
	private ComboBox cmbDiagnosisHospitalCash;
	
	private BeanItemContainer<SelectValue> diagnosisHospitalCashContainer;
	
	private ComboBox cmbHospitalCashDueTo;
	
	private BeanItemContainer<SelectValue> hospitalCashDueTo;
	
	@Inject
	private Instance<MedicalRequestBenefitsButtonsUI> medicalRequestBenefitsButtonInstance;
	
	private MedicalRequestBenefitsButtonsUI medicalRequestBenefitsButtonObj;
	
/*	@Inject
	private AddOnBenefitsListenerTable addOnBenefitsListenerTable;
	
	@Inject
	private AddOnBenefitsPatientCareListenerTable addOnBenefitsPatientCareListnerTable;*/
	//Add on benefits hospital cash table needs to be inserted here.
	
	@EJB
	private PreauthService preauthService;

	 
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
	
	public Component getContent() {
		
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		documentDetailsPageLayout = new VerticalLayout();
		documentDetailsPageLayout.setSpacing(false);
		documentDetailsPageLayout.setMargin(true);
			
		HorizontalLayout documentDetailsLayout = buildDocumentDetailsLayout(); 
		documentDetailsLayout.setCaption("Document Details");
		documentDetailsLayout.setWidth("100%");
		
		HorizontalLayout billClassificationLayout = buildBillClassificationLayout();
		billClassificationLayout.setWidth("100%");
		
		//documentDetailsPageLayout = new VerticalLayout(documentDetailsLayout,billClassificationLayout);
		
		VerticalLayout docAckAndClassificationLayout = new VerticalLayout(documentDetailsLayout,billClassificationLayout);
		
		Panel layoutPanel = new Panel();
		layoutPanel.setHeight("100%");
		layoutPanel.setWidth("100%");
		layoutPanel.setContent(docAckAndClassificationLayout);
		
		
		addOnBenefitsListenerTableObj = addOnBenefitsListenerTable.get();
		
		addOnBenefitsListenerTableObj.init();
		
		addOnBenefitsPatientCareLiseterObj = addOnBenefitsPatientCareListenerTable.get();
		
		addOnBenefitsPatientCareLiseterObj.init();
		
		Panel hospitalCashTablePanel = new Panel();
		hospitalCashTablePanel.setContent(addOnBenefitsListenerTableObj);
		
		Panel patientCareTablePanel = new Panel();
		patientCareTablePanel.setContent(addOnBenefitsPatientCareLiseterObj);
		
		//hospitalCashTablePanel.setHeight("100%");
		//hospitalCashTablePanel.setWidth("100%");
		
		
		
		
		
		documentDetailsPageLayout.addComponent(layoutPanel);
		documentDetailsPageLayout.addComponent(hospitalCashTablePanel);
		documentDetailsPageLayout.addComponent(patientCareTablePanel);
		if(null != bean && bean.getIsBillingScreen() && ! bean.getIsMedicalScreen())
		{
			billingProcessButtonObj =  billingProcessButtonInstance.get();
			billingProcessButtonObj.initView(this.bean, this.wizard);
			documentDetailsPageLayout.addComponent(billingProcessButtonObj);
		}
		else if(null != bean && bean.getIsMedicalScreen() && ! bean.getIsBillingScreen())
		{
			medicalRequestBenefitsButtonObj =  medicalRequestBenefitsButtonInstance.get();
			medicalRequestBenefitsButtonObj.initView(this.bean, this.wizard);
			documentDetailsPageLayout.addComponent(medicalRequestBenefitsButtonObj);
		}
		else if(null != bean && ! bean.getIsBillingScreen() && ! bean.getIsMedicalScreen())
		{
			financialButtonObj =  financialButtonInstance.get();
			financialButtonObj.initView(this.bean, this.wizard);
			documentDetailsPageLayout.addComponent(financialButtonObj);
		}
			 
		
		
		/*btnCancelROD = new Button("Cancel ROD");
		HorizontalLayout btnHorizontal = new HorizontalLayout(btnCancelROD);
		btnHorizontal.setWidth("100%");
		btnHorizontal.setComponentAlignment(btnCancelROD, Alignment.BOTTOM_RIGHT);
		documentDetailsPageLayout.addComponent(btnHorizontal);*/
		
		addListener();
		
		//documentDetailsPageLayout.addComponent(addOnBenefitsPanel);
		
		//addListener();
		setTableValues();
		showOrHideValidation(false);
		return documentDetailsPageLayout;
		
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
				
		HorizontalLayout docDetailsLayout = new HorizontalLayout(detailsLayout1);
		docDetailsLayout.setMargin(true);
		docDetailsLayout.setSpacing(true);
		
		return docDetailsLayout;
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
		
		//added for new product076
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
		FormLayout classificationLayout4 = new FormLayout(chkPartialHospitalization,chkHospitalCash);
		
		HorizontalLayout billClassificationLayout = new HorizontalLayout(classificationLayout1,classificationLayout2,classificationLayout3,classificationLayout4);
		//billClassificationLayout.setCaption("Document Details");
		billClassificationLayout.setCaption("Bill Classification");
		billClassificationLayout.setSpacing(true);
		billClassificationLayout.setMargin(true);
		billClassificationLayout.setWidth("100%");
		billClassificationLayout.setEnabled(false);
		return billClassificationLayout;
	}
	
	public void addListener(){
		
		
		/*btnCancelROD.addClickListener(new Button.ClickListener() {

			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
			     generateCancelRODLayout();
			}
		});*/
	}
	
	
	public void generateCancelRODLayout(){
		
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("45%");
		popup.setHeight("45%");
		popup.setContent(buildLayoutForCancelROD(popup));
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
	
	public VerticalLayout buildLayoutForCancelROD(final Window popup){
		
		cancelRodLayout.removeAllComponents();
		
		unbindField(cmbCancel);
		unbindField(txtCancelRemarks);
		cmbCancel=(ComboBox) binder.buildAndBind("Reason for Cancelling", "cancelReason", ComboBox.class);
		txtCancelRemarks =(TextArea) binder.buildAndBind("Remarks (Cancellation)", "cancelRemarks", TextArea.class);
		txtCancelRemarks.setMaxLength(100);
		
		FormLayout cancelRODform = new FormLayout(cmbCancel,txtCancelRemarks);
		
		
		Button btnSubmit = new Button("Submit");
		btnSubmit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				bean.getDocumentDetails().setStatusId(ReferenceTable.BILLING_CANCEL_ROD);
				wizard.finish();
			}
		});

		Button btnCancel = new Button("Cancel");
		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		
		btnCancel.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				popup.close();
			}
		});

		HorizontalLayout btnHorizontal = new HorizontalLayout(btnSubmit,btnCancel);
		btnHorizontal.setWidth("100%");
		
		cancelRodLayout.addComponent(cancelRODform);
		cancelRodLayout.addComponent(btnHorizontal);
		cancelRodLayout.setComponentAlignment(cancelRODform, Alignment.TOP_CENTER);
		cancelRodLayout.setComponentAlignment(btnHorizontal, Alignment.BOTTOM_CENTER);
		cancelRodLayout.setMargin(true);
		
		return cancelRodLayout;
		
	}
	
	
	
	//private ReconsiderRODRequestTable buildReconsiderRequestLayout()
	
	
	private void unbindField(Field<?> field) {
		if (field != null) {
			Object propertyId = this.binder.getPropertyId(field);
			if (propertyId != null) {
				this.binder.unbind(field);
			}

		}
	}
	
	
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
		
		if(this.financialButtonObj != null && !this.financialButtonObj.isValid()) {
			hasError = true;
			List<String> errors = this.financialButtonObj.getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			   }
		}

		if(!bean.getPreauthDTO().getIsBusinessProfileClicked()){
			hasError = true;
			eMsg.append("Please Verify View Mini Business Profile.</br>");
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
			eMsg.append("Please select any one bill classification value");
		}
		
		if(null != bean && null != bean.getIsBillingScreen() && !bean.getIsBillingScreen())
		{
			if(null != this.bean.getDocumentDetails().getStatusId() && (ReferenceTable.FINANCIAL_APPROVE_STATUS).equals(this.bean.getDocumentDetails().getStatusId()))
			{
				if(totalApprovedAmt <= 0) {
					hasError = true;
					eMsg.append("Approved Amount is Zero. Hence this ROD can not be Apporved. ");
					if(this.financialButtonObj != null) {
						this.financialButtonObj.disableApprove(true);
					}
				} else {
					if(this.financialButtonObj != null) {
						this.financialButtonObj.disableApprove(false);
					}
				}
			}
		}
		if(null != bean && null != bean.getIsMedicalScreen() && bean.getIsMedicalScreen()){

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
//			this.bean.getPreauthMedicalDecisionDetails().setReferToBillEntryBillingRemarks("");
//			
//			bean.setIsFVRAlertOpened(preauthService.getFVRStatusByClaimKey(bean.getClaimKey(),bean.getRodNumber()));
//			
//			
//				if(bean != null && bean.getIsFVRAlertOpened() != null && !bean.getIsFVRAlertOpened() && !(SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getFvrAlertFlag()))){
//					if(bean.getStatusKey() != null && !(SHAUtils.getInitiateFVRStatusMap().containsKey(bean.getStatusKey()))){
//						if(!bean.getIsFvrClicked() && !SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getFvrAlertFlag())){
//							if(!SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getReconsiderationFlag()) && !(bean.getIsDishonoured()
//									|| (bean.getPreauthDataExtractionDetails().getIllness() != null && bean
//									.getPreauthDataExtractionDetails().getIllness().getId()
//									.equals(ReferenceTable.RELATED_TO_EARLIER_CLAIMS))
//							|| (null != bean.getPreauthDataExtractionDetails()
//									.getHospitalisationDueTo()
//									&& !bean.getMaternityFlag() && bean
//									.getPreauthDataExtractionDetails()
//									.getHospitalisationDueTo().getId()
//									.equals(ReferenceTable.MATERNITY_MASTER_ID)))){
//								hasError = true;
//								eMsg.append("FVR has not been initiated. Please Initiate FVR</br>");
//							}
//							else if((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
//									||!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
//									&& bean.getClaimDTO().getClaimType() != null
//									&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(bean.getClaimDTO().getClaimType().getId())
//									&& (SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getMulticlaimAvailFlag()) && bean.getIsFvrInitiate() != null && !bean.getIsFvrInitiate())) {
//								hasError = true;
//								eMsg.append("Please Initiate FVR</br>");
//							}
//						}
//						else if((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
//								||!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
//								&& bean.getClaimDTO().getClaimType() != null
//								&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(bean.getClaimDTO().getClaimType().getId())
//								&& (SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getMulticlaimAvailFlag()) && bean.getIsFvrInitiate() != null && !bean.getIsFvrInitiate())) {
//							hasError = true;
//							eMsg.append("Please Initiate FVR</br>");
//						}
//					}
//					else if((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
//							||!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
//							&& bean.getClaimDTO().getClaimType() != null
//							&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(bean.getClaimDTO().getClaimType().getId())
//							&& (SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getMulticlaimAvailFlag()) && bean.getIsFvrInitiate() != null && !bean.getIsFvrInitiate())) {
//						hasError = true;
//						eMsg.append("Please Initiate FVR</br>");
//					}							
//				}
//				else if((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
//						||!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
//						&& bean.getClaimDTO().getClaimType() != null
//						&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(bean.getClaimDTO().getClaimType().getId())
//						&& (SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getMulticlaimAvailFlag()) && bean.getIsFvrInitiate() != null && !bean.getIsFvrInitiate())) {
//					hasError = true;
//					eMsg.append("Please Initiate FVR</br>");
//				}
//				
				bean.getPreauthDTO().setIsFVRAlertOpened(preauthService.getFVRStatusByClaimKey(bean.getPreauthDTO().getClaimKey(),bean.getPreauthDTO().getRodNumber()));

				if(null != bean.getPreauthDTO().getScreenName() && !(SHAConstants.WAIT_FOR_INPUT_SCREEN.equals(bean.getPreauthDTO().getScreenName()))){
				if(bean.getPreauthDTO() != null && bean.getPreauthDTO().getIsFVRAlertOpened() != null && !bean.getPreauthDTO().getIsFVRAlertOpened() && !(SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDTO().getFvrAlertFlag()))){
					if(bean.getPreauthDTO().getStatusKey() != null && !(SHAUtils.getInitiateFVRStatusMap().containsKey(bean.getPreauthDTO().getStatusKey()))){
						if(!bean.getPreauthDTO().getIsFvrClicked() && !SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDTO().getFvrAlertFlag())){
								hasError = true;
								eMsg.append("FVR has not been initiated. Please Initiate FVR</br>");
					}
				}
			}
	    }
				
			/*if(bean.getPreauthMedicalDecisionDetails()!= null && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiatedMA() != null && bean.getPreauthMedicalDecisionDetails().getIsFvrIntiatedMA().equals(Boolean.FALSE)){
				if(bean.getStatusKey() != null && !(SHAUtils.getInitiateFVRStatusMap().containsKey(bean.getStatusKey()))){
					if(!bean.getIsFvrClicked() && !SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getFvrAlertFlag())){
						hasError = true;
						eMsg.append("FVR has not been initiated. Please Initiate FVR</br>");
					}
				}
				else{
					if(!bean.getIsFvrInitiate() && !bean.getIsFvrNotRequiredAndNotSelected()){
						hasError = true;
						eMsg.append("Please select Approve or Query or Reject or Escalate Claim or Refer to coordinator. </br>");
					}
				}
			}*/
			
//			if(this.bean.getStatusKey() != null && ! this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS)){
//				
//				if(this.medicalVerificationTableObj != null && !this.medicalVerificationTableObj.isValid()) {
//					hasError = true;
//					List<String> errors = this.medicalVerificationTableObj.getErrors();
//					for (String error : errors) {
//						eMsg.append(error).append("</br>");
//					}
//				}
//			}
//			
//			
//			
//			if(this.treatmentVerificationTableObj != null && !this.treatmentVerificationTableObj.isValid()) {
//				hasError = true;
//				List<String> errors = this.treatmentVerificationTableObj.getErrors();
//				for (String error : errors) {
//					eMsg.append(error).append("</br>");
//				}
//			}
//			
//			if(this.treatmentVerificationTableObj != null && !this.treatmentVerificationTableObj.isValidTable()) {
//				hasError = true;
//				List<String> errors = this.treatmentVerificationTableObj.getErrorsForRemarks();
//				for (String error : errors) {
//					eMsg.append(error).append("</br>");
//				}
//			}
//			
//			if(this.medicalVerificationTableObj != null && !this.medicalVerificationTableObj.isValidTable()) {
//				hasError = true;
//				List<String> errors = this.medicalVerificationTableObj.getErrorsforRemarks();
//				for (String error : errors) {
//					eMsg.append(error).append("</br>");
//				}
//			}
//			
			
			/*List<FvrGradingDetailsDTO> fvrGradingDTO = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
			if(!fvrGradingDTO.isEmpty()) {
				for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrGradingDTO) {
					List<FVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO.getFvrGradingDTO();
					for (FVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
						if(fvrGradingDTO3.getStatus() == null) {
							hasError = true;
							eMsg.append("Please Select All the FVR Grading and set the Status. </br>");
							break;
						}
					}
				}
			}*/
			Boolean isShown = Boolean.FALSE;
			if (!this.medicalRequestBenefitsButtonObj.isValid()) {
				hasError = true;
				List<String> errors = this.medicalRequestBenefitsButtonObj.getErrors();
				for (String error : errors) {
					if(error.equalsIgnoreCase("Please select Approve or Reject or Escalate Claim or Refer to coordinator. </br>")){
						isShown = Boolean.TRUE;
					}
					eMsg.append(error);
				}
				
				if(!isShown){
					if(!bean.getPreauthDTO().getIsFvrInitiate() && !bean.getPreauthDTO().getIsFvrNotRequiredAndSelected()){
						hasError = true;
						eMsg.append("Please select Approve or Query or Reject or Escalate Claim or Refer to coordinator. </br>");
					}	
				}
			}
	     
			/*if(bean.getIsInvestigation() && (investigationReviewRemarks.getValue() == null || investigatorName.getValue() == null || (investigationReportReviewedChk.getValue() == null || !investigationReportReviewedChk.getValue()) )) {
				hasError = true;
				eMsg.append("Please Enter Investigation Remarks and Name");
			}*/
//			int invCount = 0;
//			if(this.bean.getStatusKey() != null && (ReferenceTable.invsAlertRequiredStatus()).containsKey(this.bean.getStatusKey())){
//				if(null != bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList() && !bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().isEmpty())
//				{
//					for (AssignedInvestigatiorDetails invsObj : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) {			
//					
//						if(null == invsObj.getReportReviewed() || !(invsObj.getReportReviewed().equalsIgnoreCase(SHAConstants.YES_FLAG))){
//							hasError = true;
//							eMsg.append("Investigation Review required").append("</br>");
//							break;
//						}
//						invCount++;
//					}
//					
//					if(bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().size() == invCount){
//						for (AssignedInvestigatiorDetails invsObj : bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList()) 
//						{		
//							if((null != invsObj.getReportReviewed() && invsObj.getReportReviewed().equalsIgnoreCase(SHAConstants.YES_FLAG)) &&
//									(null == invsObj.getReviewRemarks() || ("").equalsIgnoreCase(invsObj.getReviewRemarks()))){
//								hasError = true;
//								if(bean.getPreauthMedicalDecisionDetails().getInvsReviewRemarksTableList().size()== 1){
//									eMsg.append("Investigation Review Remarks required for Selected Item").append("</br>");
//									break;
//								}
//								else
//								{
//									eMsg.append("Investigation Review Remarks required for Selected Items").append("</br>");
//									break;
//								}
//							}
//						}
//					}
//				}
//			}
//			if(bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS) && bean.getModeOfReceipt() != null && bean.getModeOfReceipt().equals(ReferenceTable.MODE_OF_RECEIPT_ONLINE)){
//				BPMClientContext clientContext = new BPMClientContext();
//				String documentGateWayUrl = clientContext.getDocumentGateWayUrl();
//				if(null != documentGateWayUrl &&  documentGateWayUrl.equalsIgnoreCase("M")){
//					if(bean.getCheckerVerified() == null){
//						hasError = true;
//						eMsg.append("Physical document is not vierified</br>");
//					}
//				}
//			}

//			if (!this.medicalDecisionTableObj.getTotalAmountConsidered().equals(
//					SHAUtils.getIntegerFromString(this.bean.getAmountConsidered()))) {
//				hasError = true;
//				eMsg += "Total Amount Considered Should be equal to Data Extraction Page Payable Amount. </br>";
//			}

//			if(!this.bean.getIsScheduleClicked() && (null != bean.getNewIntimationDTO().getIsTataPolicy() && !bean.getNewIntimationDTO().getIsTataPolicy())){
//				hasError = true;
//				eMsg.append("Please Verify View Policy Schedule Button.</br>");
//			}
			
//			//New FVR GRADING SEG A,B&C
//					List<FvrGradingDetailsDTO> fvrBGradingDTO = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
//					if(fvrBGradingDTO !=null && !fvrBGradingDTO.isEmpty()) {
//						int i=0;
//						for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrBGradingDTO) {
//							i++;
//								if(fvrGradingDetailsDTO.getIsSegmentBNotEdit() != null && !fvrGradingDetailsDTO.getIsSegmentBNotEdit() && fvrGradingDetailsDTO.getIsSegmentANotEdit() != null && !fvrGradingDetailsDTO.getIsSegmentANotEdit()){
//									List<NewFVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO.getNewFvrGradingDTO();
//									for (NewFVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
//											if(fvrGradingDTO3.getSegment() != null && fvrGradingDTO3.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_B) && fvrGradingDTO3.getSelectFlag() == null) {
//												hasError = true;
//												eMsg.append("Please Select All SEGMENT A and B in FVR Grading "+i+". </br>");
//												break;
//											}else if(fvrGradingDTO3.getSegment() != null && fvrGradingDTO3.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_A) && fvrGradingDTO3.getCheckFlagA() == null){
//												hasError = true;
//												eMsg.append("Please Select All SEGMENT A and B in FVR Grading "+i+". </br>");
//												break;
//											}
//									}
//								}else if(fvrGradingDetailsDTO.getIsSegmentCNotEdit() != null && !fvrGradingDetailsDTO.getIsSegmentCNotEdit()){
//
//									List<NewFVRGradingDTO> fvrGradingDTO2 = fvrGradingDetailsDTO.getNewFvrGradingDTO();
//									Boolean isAnySegmentCSelected = false;
//									for (NewFVRGradingDTO fvrGradingDTO3 : fvrGradingDTO2) {
//											if(fvrGradingDTO3.getSegment() != null && fvrGradingDTO3.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_C) && (fvrGradingDTO3.getCheckFlag() != null && fvrGradingDTO3.getCheckFlag().equals(Boolean.TRUE))) {
//												isAnySegmentCSelected = true;
//												break;
//											}
//									}
//									
//									if(!isAnySegmentCSelected){
//										hasError = true;
//										eMsg.append("Please Select Any SEGMENT C in FVR Grading "+i+". </br>");
//									}
//									//IMSSUPPOR-23522
//									else if(fvrGradingTableObj.getSegmentCListenerTableSelectCount() > 1 ){
//										hasError = true;
//										eMsg.append("Please Select only one Check box value in SEGMENT C. </br>");
//										break;
//									}
//								}
//							else{
//								hasError = true;
//								eMsg.append("Please Select Any SEGMENT in FVR Grading "+i+". </br>");
//							}
//								
//								
//								if(fvrGradingTableObj != null){
//									Map<Long, AbstractField<?>> tableItem = fvrGradingTableObj.getGradingRemarks();
//									if(tableItem != null && !tableItem.isEmpty()){
//										if(fvrGradingDetailsDTO.getIsFVRReplied() != null && fvrGradingDetailsDTO.getIsFVRReplied() && fvrGradingDetailsDTO.getKey() != null){
//											if(tableItem.get(fvrGradingDetailsDTO.getKey()) == null && fvrGradingDetailsDTO.getGradingRemarks() == null){
//												hasError = true;
//												eMsg.append("Please Enter Grading Suggestion in FVR Grading "+i+". </br>");
//											}else{
//												TextArea gradeRmrks = (TextArea)tableItem.get(fvrGradingDetailsDTO.getKey());
//												if(gradeRmrks != null && (gradeRmrks.getValue() == null || gradeRmrks.getValue().isEmpty())){
//													hasError = true;
//													eMsg.append("Please Enter Grading Suggestion in FVR Grading "+i+". </br>");
//												}
//											}
//										}
//									}
//								}
//							
//						}
//					}
					
					/*Below code removed as per Ver Doc R20181286
					if(!hasError) {
						if(bean.getPreauthMedicalDecisionDetails().getNegotiationMade() != null &&
								bean.getPreauthMedicalDecisionDetails().getNegotiationMade() && bean.getPreauthMedicalDecisionDetails().getNegotiationAmount() == null) {
							hasError = true;
							eMsg.append("Please Enter Negotiation Amount");
						}
					}*/
			
//			if (hasError) {
//				setRequired(true);
//				/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
//				label.setStyleName("errMessage");
//				VerticalLayout layout = new VerticalLayout();
//				layout.setMargin(true);
//				layout.addComponent(label);
//
//				ConfirmDialog dialog = new ConfirmDialog();
//				dialog.setCaption("Alert");
//				dialog.setClosable(true);
//				dialog.setContent(label);
//				dialog.setResizable(false);
//				dialog.show(getUI().getCurrent(), null, true);*/
//				
//				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
//				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
//				GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
//
//				hasError = true;
//				return !hasError;
//			} else {
//				try {
//					this.binder.commit();
//					this.bean.getPreauthDTO().getPreauthMedicalDecisionDetails().setMedicalVerificationTableDTO(this.medicalVerificationTableObj.getValues());
//					this.bean.getPreauthMedicalDecisionDetails().setTreatmentVerificationDTO(this.treatmentVerificationTableObj.getValues());
//					if(this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName() != null){
//						this.bean.getPreauthMedicalDecisionDetails().setInvestigatorCode(this.bean.getPreauthMedicalDecisionDetails().getInvestigatorName().getInvestigatorCode());
//						
//					}
////					// New requirement for saving Copay values to Transaction Table......... 
////					SHAUtils.setCopayAmounts(bean, this.claimRequestButtonObj.amountConsideredTable);
//					
//					if(fvrGradingTableObj != null){
//						Map<Long, AbstractField<?>> tableItem = fvrGradingTableObj.getGradingRemarks();
//						List<FvrGradingDetailsDTO> fvrBGradingDTO1 = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
//						if(tableItem != null && !tableItem.isEmpty()){
//							for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrBGradingDTO1) {
//								TextArea gradingRmrk = (TextArea) tableItem.get(fvrGradingDetailsDTO.getKey());
//								if(gradingRmrk != null && gradingRmrk.getValue() != null){
//									fvrGradingDetailsDTO.setGradingRemarks(gradingRmrk.getValue());
//								}
//							}
//						}
//					}
//					
//					
//					
//					return true;
//				} catch (CommitException e) {
//					e.printStackTrace();
//				}
//				showOrHideValidation(false);
//				return false;
//			}
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
				this.binder.commit();
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}		
	}
	
	
	/*@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}*/
	
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
		if(null != this.addOnBenefitsListenerTableObj && !(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) 
				|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))))
		{
			if(null != this.bean.getAddOnBenefitsDTO() && !this.bean.getAddOnBenefitsDTO().isEmpty())
			{
				for (AddOnBenefitsDTO addOnBenefitsDTO : this.bean.getAddOnBenefitsDTO()) {
					if((ReferenceTable.HOSPITAL_CASH).equalsIgnoreCase(addOnBenefitsDTO.getParticulars()))
							{
								addOnBenefitsListenerTableObj.addBeanToList(addOnBenefitsDTO);
							}
						
				}
			}
		}
		if(null != this.addOnBenefitsPatientCareLiseterObj && !(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) 
				|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))))
		{
			if(null != this.bean.getAddOnBenefitsDTO() && !this.bean.getAddOnBenefitsDTO().isEmpty())
			{
				for (AddOnBenefitsDTO addOnBenefitsDTO : this.bean.getAddOnBenefitsDTO()) {
					if((ReferenceTable.PATIENT_CARE).equalsIgnoreCase(addOnBenefitsDTO.getParticulars()))
							{
								
								addOnBenefitsPatientCareLiseterObj.addBeanToList(addOnBenefitsDTO);
							}
				}
			}
		}
		
		if(null != this.addOnBenefitsListenerTableObj && (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
				this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| (this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))))
		{
			if(null != this.bean.getAddOnBenefitsDTO() && !this.bean.getAddOnBenefitsDTO().isEmpty())
			{
				for (AddOnBenefitsDTO addOnBenefitsDTO : this.bean.getAddOnBenefitsDTO()) {
					
								addOnBenefitsListenerTableObj.addBeanToList(addOnBenefitsDTO);
				}
			}
		}
	}
	
	public  void setTableValuesToDTO()
	{
		
		List<AddOnBenefitsDTO> addOnBenefitsFinalList = new ArrayList<AddOnBenefitsDTO>();
		List<AddOnBenefitsDTO> addOnBenefitsList = null;
		List<AddOnBenefitsDTO> addOnBenefitsPatientList = null;
		
		if(null != addOnBenefitsListenerTableObj)
		{
			addOnBenefitsList = addOnBenefitsListenerTableObj.getValues();
			if(null != addOnBenefitsList && !addOnBenefitsList.isEmpty())
			{
				for (AddOnBenefitsDTO addOnBenefitsDTO : addOnBenefitsList) {
					addOnBenefitsFinalList.add(addOnBenefitsDTO);
					totalApprovedAmt += addOnBenefitsDTO.getPayableAmount();
				}
			}
			/*if(null != addOnBenefitsList && !addOnBenefitsList.isEmpty())
			{
				this.bean.setAddOnBenefitsDTO(addOnBenefitsList);
			}*/
		}
		if(null != addOnBenefitsPatientCareLiseterObj)
		{
			addOnBenefitsPatientList = addOnBenefitsPatientCareLiseterObj.getValues();
			
			if(null != addOnBenefitsPatientList && !addOnBenefitsPatientList.isEmpty())
			{
				for (AddOnBenefitsDTO addOnBenefitsDTO : addOnBenefitsPatientList) {
					addOnBenefitsFinalList.add(addOnBenefitsDTO);
					totalApprovedAmt += addOnBenefitsDTO.getPayableAmount();
				}
			}
		}
		
		if(null != addOnBenefitsFinalList && !addOnBenefitsFinalList.isEmpty())
		{
			this.bean.setAddOnBenefitsDTO(addOnBenefitsFinalList);
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

	
	public void generateButtonFields(String eventName, BeanItemContainer<SelectValue> selectValueContainer) {
		this.bean.setStageKey(ReferenceTable.BILLING_STAGE);
		if(SHAConstants.REFER_TO_COORDINATOR.equalsIgnoreCase(eventName)) {
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR)) {
				this.bean.getDocumentDetails().setTypeOfCoordinatorRequest(null);
				this.bean.getDocumentDetails().setReasonForRefering("");
			}
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.BILLING_REFER_TO_COORDINATOR);
			//this.bean.setStatusKey(ReferenceTable.BILLING_REFER_TO_COORDINATOR);
			billingProcessButtonObj.buildReferCoordinatorLayout(selectValueContainer);
		} /*lse if(SHAConstants.MEDICAL_APPROVER.equalsIgnoreCase(eventName)) {
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)) {
				this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
			}
			this.bean.setStatusKey(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER);
			billingProcessButtonObj.buildReferToMedicalApproverLayout();
		} */ else if(SHAConstants.FINANCIAL.equalsIgnoreCase(eventName)) {
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)) {
				this.bean.getDocumentDetails().setReasonForRefering("");
			}
			//this.bean.setStatusKey(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
			billingProcessButtonObj.buildSendToFinancialLayout();
		} else if(SHAConstants.BILLING_CANCEL_ROD.equalsIgnoreCase(eventName)){
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.BILLING_CANCEL_ROD)) {
				this.bean.getDocumentDetails().setReasonForRefering("");
			}
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.BILLING_CANCEL_ROD);	
//			this.bean.setStatusKey();
			billingProcessButtonObj.buildCancelRODLayout(selectValueContainer);
		}
	}
	
	
	public void generateButton(Integer clickedButton, Object dropDownValues) {
		this.bean.setStageKey(ReferenceTable.FINANCIAL_STAGE);
		switch (clickedButton) {
		case 1: 
//			
//		 this.financialButtonObj.buildSpecialistLayout(dropDownValues);
//		 this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST);
//		 break;
//			if(validatePage()){
		/*		specialistWindow.init(bean);
				specialistWindow.buildSpecialityLayout(dropDownValues,fileViewUI);
				specialistWindow.center();
				specialistWindow.setHeight("400px");
				specialistWindow.setResizable(false);
				specialistWindow.setModal(true);
				specialistWindow.addSubmitHandler(this);
				UI.getCurrent().addWindow(specialistWindow);
				
				specialistWindow.addCloseListener(new CloseListener() {
			            private static final long serialVersionUID = -4381415904461841881L;

			            public void windowClose(CloseEvent e) {
//			                System.out.println("close called");
			            }
			        });
				 
//				this.claimRequestFileUploadUI.init(bean, wizard);
//				this.claimRequestFileUploadUI.buildSpecialityLayout(dropDownValues);
				
				// this.medicalDecisionTableObj.setVisibleApproveFields(false);
				 if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST)) {
					 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
					 this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
				 }
				this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST);*/
//				}
				break;
		 
		case 2: 
			/* this.financialButtonObj.buildInitiateFieldVisit(dropDownValues);
			 this.bean.setStatusKey(ReferenceTable.FINANCIAL_INITIATE_FIELD_REQUEST_STATUS);*/
			 break;
			 
		case 3: 
			 /*this.financialButtonObj.buildInitiateInvestigation(dropDownValues);
			 this.bean.setStatusKey(ReferenceTable.FINANCIAL_INITIATE_INVESTIGATION_STATUS);*/
			 break;
		case 4:
			this.financialButtonObj
					.buildReferCoordinatorLayout(dropDownValues);
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS);
			break;
		case 5:
			this.financialButtonObj.buildReferToMedicalApproverLayout();;
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER);
			break;
		case 6:
			this.financialButtonObj.buildReferToBilling();
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.FINANCIAL_REFER_TO_BILLING);
			break;
			
		case 7:
			this.financialButtonObj.buildQueryLayout();
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.FINANCIAL_QUERY_STATUS);
			break;
		case 8:
			this.financialButtonObj.buildRejectLayout(dropDownValues);
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.FINANCIAL_REJECT_STATUS);
			break;
		case 9:
			this.financialButtonObj.generateFieldsForApproval();
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.FINANCIAL_APPROVE_STATUS);
			break;
		case 10:
			this.financialButtonObj.generateCancelRODLayout(dropDownValues);
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.FINANCIAL_CANCEL_ROD);
		default:
			break;
		}
	}
	
	//added for new product076 in MA screen
	
	public void generateButtonForMA(Integer clickedButton, BeanItemContainer<SelectValue> selectValueContainer,BeanItemContainer<SelectValue> fvrAssignTo,BeanItemContainer<SelectValue> fvrPriority,
			boolean isFVRAssigned, String repName, String repContactNo) {
		this.bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
		switch (clickedButton) {
		case 1: 
//			
//		 this.financialButtonObj.buildSpecialistLayout(dropDownValues);
//		 this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST);
//		 break;
//			if(validatePage()){
		/*		specialistWindow.init(bean);
				specialistWindow.buildSpecialityLayout(dropDownValues,fileViewUI);
				specialistWindow.center();
				specialistWindow.setHeight("400px");
				specialistWindow.setResizable(false);
				specialistWindow.setModal(true);
				specialistWindow.addSubmitHandler(this);
				UI.getCurrent().addWindow(specialistWindow);
				
				specialistWindow.addCloseListener(new CloseListener() {
			            private static final long serialVersionUID = -4381415904461841881L;

			            public void windowClose(CloseEvent e) {
//			                System.out.println("close called");
			            }
			        });
				 
//				this.claimRequestFileUploadUI.init(bean, wizard);
//				this.claimRequestFileUploadUI.buildSpecialityLayout(dropDownValues);
				
				// this.medicalDecisionTableObj.setVisibleApproveFields(false);
				 if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST)) {
					 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
					 this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
				 }
				this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST);*/
//				}
				break;
		 
//		case 3: 
//			 this.medicalRequestBenefitsButtonObj.buildInitiateInvestigation(selectValueContainer);
//			 this.bean.setStatusKey(ReferenceTable.FINANCIAL_INITIATE_INVESTIGATION_STATUS);
//			 break;
		case 4:
			this.medicalRequestBenefitsButtonObj
					.buildReferCoordinatorLayout(selectValueContainer);
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);
			break;
		case 5:
			/*this.financialButtonObj.buildReferToMedicalApproverLayout();;
			this.bean.setStatusKey(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER);*/
			break;
		case 6:
			this.medicalRequestBenefitsButtonObj.buildReferToBilling();
//			this.bean.getDocumentDetails().setStatusId(ReferenceTable.FINANCIAL_REFER_TO_BILLING);
			break;
			
		case 8:
			this.medicalRequestBenefitsButtonObj.buildRejectLayout(selectValueContainer);
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
			break;
		case 9:
			this.medicalRequestBenefitsButtonObj.generateFieldsForApproval();
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
			break;
		case 10:
			this.medicalRequestBenefitsButtonObj.generateCancelRODLayout(selectValueContainer);
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS);
		default:
			break;
		}
	}
	
	//added for new product076
	public void genertateFieldsBasedOnFieldVisitForMA(
			BeanItemContainer<SelectValue> selectValueContainer,BeanItemContainer<SelectValue> fvrAssignTo,BeanItemContainer<SelectValue> fvrPriority,
			boolean isFVRAssigned, String repName, String repContactNo) {
		this.medicalRequestBenefitsButtonObj.genertateFieldsBasedOnFieldVisit(selectValueContainer, fvrAssignTo,fvrPriority,
				isFVRAssigned,repName,repContactNo);
	}
	
	public void generateQueryLayoutForMA() {
		if(!bean.getPreauthDTO().getIsFvrInitiate()){
			 bean.getPreauthDTO().setIsFvrNotRequiredAndSelected(Boolean.TRUE);
		 }
		buildQueryLayout();
	}
	
	
	public void buildQueryLayout() {
		
		final Integer setQueryValues = this.medicalRequestBenefitsButtonObj.setQueryValues(this.bean.getKey(), this.bean.getClaimDTO().getKey());
		if(setQueryValues > 0) {
			this.medicalRequestBenefitsButtonObj.alertMessage(SHAConstants.QUERY_RAISE_MESSAGE, setQueryValues);
		} else {
			this.medicalRequestBenefitsButtonObj.generateQueryDetails(setQueryValues);
			
		}
	}
	
	public void generateInvestigationLayoutForMA(boolean isDirectToAssignInv) {
		Window popup = new com.vaadin.ui.Window();
		popup.setClosable(true);
		popup.setWidth("75%");
		popup.setHeight("90%");
		popup.center();
		popup.setResizable(false);
		this.bean.getPreauthDTO().setDirectToAssignInv(isDirectToAssignInv);
		this.bean.getPreauthDTO().setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
		this.bean.getPreauthDTO().setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
		this.bean.getPreauthDTO().setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
		if(!bean.getPreauthDTO().getIsFvrInitiate()){
			 bean.getPreauthDTO().setIsFvrNotRequiredAndSelected(Boolean.TRUE);
		 }
		ParallelInvestigationDetails viewInvestigationDetails = this.medicalRequestBenefitsButtonObj.getRevisedInvestigationDetails(bean.getPreauthDTO(), true,ReferenceTable.ZONAL_REVIEW_STAGE,popup);
		this.bean.getDocumentDetails().setStatusId(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
		popup.setContent(viewInvestigationDetails);
		popup.setModal(true);
		popup.setClosable(true);
		UI.getCurrent().addWindow(popup);
	
	}
	public void generateCancelRODLayoutForMA() {
		this.bean.getPreauthDTO().setStatusKey(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS);
		this.bean.getDocumentDetails().setStatusId(ReferenceTable.CLAIM_REQUEST_CANCEL_ROD_STATUS);
		this.medicalRequestBenefitsButtonObj.builtCancelRODLayout();
		if(!bean.getPreauthDTO().getIsFvrInitiate()){
			 bean.getPreauthDTO().setIsFvrNotRequiredAndSelected(Boolean.TRUE);
		 }
	}
	
	public void generateRejectLayoutForMA(BeanItemContainer<SelectValue> selectValueContainer) {
		this.bean.getPreauthDTO().setStatusKey(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
		this.bean.getDocumentDetails().setStatusId(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
		
		this.medicalRequestBenefitsButtonObj.buildRejectLayout(selectValueContainer,false);
		if(!bean.getPreauthDTO().getIsFvrInitiate()){
			 bean.getPreauthDTO().setIsFvrNotRequiredAndSelected(Boolean.TRUE);
		 }
		
	}
	
	public void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer) {
		this.medicalRequestBenefitsButtonObj.setSubCategContainer(rejSubcategContainer);
		
	}
	public void generateApproveRODLayoutForMA() {
		this.bean.getPreauthDTO().setStatusKey(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
		this.bean.getDocumentDetails().setStatusId(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);
		this.medicalRequestBenefitsButtonObj.generateFieldsForApproval();
		
		if(!bean.getPreauthDTO().getIsFvrInitiate()){
			 bean.getPreauthDTO().setIsFvrNotRequiredAndSelected(Boolean.TRUE);
		 }
	}
	
	public void generateReferToCoordinatorLayoutForMA(BeanItemContainer<SelectValue> selectValueContainer) {
		this.bean.getPreauthDTO().setStatusKey(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);
		if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS)) {
			this.bean.getDocumentDetails().setTypeOfCoordinatorRequest(null);
			this.bean.getDocumentDetails().setReasonForRefering("");
		}
		this.bean.getDocumentDetails().setStatusId(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);
		this.medicalRequestBenefitsButtonObj.buildReferCoordinatorLayout(selectValueContainer);
		if(!bean.getPreauthDTO().getIsFvrInitiate()){
			 bean.getPreauthDTO().setIsFvrNotRequiredAndSelected(Boolean.TRUE);
		 }
	}
	
	public void generateSendReplyToFinancial() {
		this.medicalRequestBenefitsButtonObj.builtSendReplyToFinancialLayout();
         this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS);
		 if(this.bean.getPreauthDTO().getIsReplyToFA() != null && this.bean.getPreauthDTO().getIsReplyToFA()){
			 this.bean.getPreauthDTO().setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS);
		 }
		 
		Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getPreauthDTO().getDbOutArray();
		String sendReplyFrom = (String) wrkFlowMap.get(SHAConstants.PAYLOAD_PREAUTH_STATUS);
		if(null != sendReplyFrom && SHAConstants.SEND_REPLY_BILLING.equalsIgnoreCase(sendReplyFrom)){
			this.bean.getPreauthDTO().setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS);
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS);
		}
		else if(null != sendReplyFrom && SHAConstants.SEND_REPLY_FA.equalsIgnoreCase(sendReplyFrom))
		{
			this.bean.getPreauthDTO().setStatusKey(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS);
			this.bean.getDocumentDetails().setStatusId(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS);
		}
		if(!bean.getPreauthDTO().getIsFvrInitiate()){
			 bean.getPreauthDTO().setIsFvrNotRequiredAndSelected(Boolean.TRUE);
		 }
	}
	
}
