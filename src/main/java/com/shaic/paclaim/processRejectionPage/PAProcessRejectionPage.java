package com.shaic.paclaim.processRejectionPage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.domain.Policy;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


public class PAProcessRejectionPage extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1678011305032044637L;

	@Inject
	private ProcessRejectionDTO bean;
	
	private TextBundle textBundle;
	
	private BeanFieldGroup<ProcessRejectionDTO> binder;
	
	private TextField currencyName;
	
	private TextField claimedAmt;
	
	private TextField provisionAmt;
	
	private OptionGroup accDeath;
	
	private PopupDateField accDeathDate;
	
	private PopupDateField dateOfAdmission;
	
	private DateField dateOfAccident;
	
	private DateField dateOfDeath;
	
	private DateField dateOfDisablement;
	
	private TextArea injuryLossDetails;
	
//	private TextField claimNumber;
	
	private TextField registerRemarks;
	
	private TextArea remarks;
	
	private HorizontalLayout mainLayout;
	
	private FormLayout firstForm;
	
	private FormLayout secondForm;
	
	public static Boolean DESICION=false;
	
	@PostConstruct
	public void initView(){
		
	}
	
	public void setReferenceData(ProcessRejectionDTO bean,NewIntimationDto intimationDto){
		
		this.bean=bean;
		this.binder= new BeanFieldGroup<ProcessRejectionDTO>(ProcessRejectionDTO.class);
		this.binder.setItemDataSource(this.bean);
		
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
//		currencyName =(TextField) binder.buildAndBind("Currency Name","currencyName", TextField.class);
		
		claimedAmt =(TextField) binder.buildAndBind("Amount Claimed","claimedAmount", TextField.class);		
		
		provisionAmt =(TextField) binder.buildAndBind("Provision Amount","provisionAmt", TextField.class);
		
		accDeath = (OptionGroup) binder.buildAndBind("Accident / Death","accDeathFlag", OptionGroup.class);
		
		Collection<Boolean> paClaimTypeValues = new ArrayList<Boolean>(2);
		paClaimTypeValues.add(true);
		paClaimTypeValues.add(false);
		
		accDeath.addItems(paClaimTypeValues);
		accDeath.setItemCaption(true, "Accident");
		accDeath.setItemCaption(false, "Death");
		accDeath.setStyleName("horizontal");
		//Vaadin8-setImmediate() accDeath.setImmediate(true);
		accDeath.setEnabled(false);
		
		accDeathDate = (PopupDateField) binder.buildAndBind("Date of Accident / Death","accDeathDate", PopupDateField.class);
		
		injuryLossDetails = (TextArea) binder.buildAndBind("Injury / Loss Details","injuryLossDetails", TextArea.class);
//		claimNumber =(TextField) binder.buildAndBind("Claim Number","claimNumber", TextField.class);	
		
		registerRemarks =(TextField) binder.buildAndBind("Department Remarks\n(Registration)","registerRemarks", TextField.class);
		
//		firstForm=new FormLayout(currencyName,claimedAmt,provisionAmt,claimNumber,registerRemarks);
		
		dateOfAccident = (DateField) binder.buildAndBind("Date of Accident",
				"dateOfAccident", DateField.class);
		dateOfAccident.setEnabled(false);
		
		dateOfDeath = (DateField) binder.buildAndBind("Date of Death",
				"dateOfDeath", DateField.class);
		dateOfDeath.setEnabled(false);
		
		dateOfDisablement = (DateField) binder.buildAndBind("Date of Disablement",
				"dateOfDisablement", DateField.class);
		dateOfDisablement.setEnabled(false);
		
		firstForm=new FormLayout(claimedAmt,provisionAmt,accDeath,dateOfAccident,dateOfDeath,dateOfDisablement);
		
		remarks=(TextArea) binder.buildAndBind("Suggested Rejection Remarks", "rejectionRemarks", TextArea.class);
		remarks.setReadOnly(true);

		secondForm=new FormLayout(remarks,registerRemarks,injuryLossDetails);
		
		setReadOnly(firstForm,true);
		setReadOnly(secondForm,true);
		
		mainLayout=new HorizontalLayout(firstForm,secondForm);
		mainLayout.setSizeFull();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		
		Panel panel=new Panel();
		panel.setCaption("Registration Remarks");
		panel.addStyleName("girdBorder");
		panel.setContent(mainLayout);
		panel.setWidth("100%");
		
		addListener();
		setCompositionRoot(panel);
		setSizeFull();
	}
	
	public void initView(SearchProcessRejectionTableDTO searchDTO){
		
		if(searchDTO.getStatusKey() == null){
		fireViewEvent(PAProcessRejectionPresenter.SET_PA_DATA,searchDTO);
		}else{
			this.binder= new BeanFieldGroup<ProcessRejectionDTO>(ProcessRejectionDTO.class);
			this.binder.setItemDataSource(searchDTO.getProcessRejectionDTO());
			setReferenceData(searchDTO.getProcessRejectionDTO(),searchDTO.getIntimationDTO());
		}		
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
			else if (c instanceof PopupDateField) {
				PopupDateField field = (PopupDateField) c;
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.DATEFIELD_BORDERLESS);
			}
			else if (c instanceof ComboBox) {
				ComboBox field = (ComboBox) c;
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.COMBOBOX_BORDERLESS);
			}
			else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
			}
		}
	}
	
	public void addListener()
	{
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
						dateOfDisablement.setValue(null);
						showErrorMessage("The date of disablement captured is beyond 12 months from the date of accident");
					}
				}
				
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

}
