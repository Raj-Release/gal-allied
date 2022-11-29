package com.shaic.claim.OMPprocessrejection.detailPage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
public class OMPProcessRejectionPage extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1678011305032044637L;
	
	@Inject
	private ViewDetails viewDetails;

	private ProcessRejectionDTO bean;

	private NewIntimationDto intimationDto;
	
	private BeanFieldGroup<ProcessRejectionDTO> binder;
	
	private TextField provisionAmt;
	
	private TextField inrConversionRate;
	
	private TextField inrValue;
	
	private ComboBox  eventCode;

	private TextField hospitalName;

	private TextField hospitalCity;
	
	private TextField hospitalCounty;

	private TextField ailmentLoss;
	
	private TextArea remarks;
	
	private OptionGroup  clmTypeOptionGroup;
	
	private OptionGroup  hospitalisationOptionGroup;
	
	private VerticalLayout mainLayout;
	
	private FormLayout firstForm;
	
	private FormLayout secondForm;
	
	public static Boolean DESICION;
	
	private HorizontalLayout rejectionButtonLayout;
	
	private FormLayout dynamicLayout;
	
	private FormLayout waiveLayout;
	
	private ArrayList<Component> mandatoryFields;

	private Button confirmRejectionBtn;

	private Button waiveRejectionBtn;
	
	private TextArea waiveRemarks;

	private TextArea confirmRemarks;

	private TextArea medicalRemarks;

	private Button submitBtn;

	private Button cancelBtn;
	
	private HorizontalLayout buttonHorLayout;
	
	private static BeanItemContainer<SelectValue> selectedValues;
	
	private Boolean isButtonClicked;
	
	private FormLayout confirmLayout;
	
	@PostConstruct
	public void initView(){
		DESICION = false;
	}
	
	public void setReferenceData(ProcessRejectionDTO bean,NewIntimationDto intimationDto){
		
//		this.bean=bean;
//		this.intimationDto = intimationDto;
//		this.binder= new BeanFieldGroup<ProcessRejectionDTO>(ProcessRejectionDTO.class);
//		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
//		this.binder.setItemDataSource(this.bean);
		isButtonClicked = false;
		mandatoryFields = new ArrayList<Component>();		
	}
	
	public void initView(SearchProcessRejectionTableDTO searchDTO){
		
		this.bean=searchDTO.getProcessRejectionDTO();
		this.intimationDto = searchDTO.getIntimationDTO();
		this.binder= new BeanFieldGroup<ProcessRejectionDTO>(ProcessRejectionDTO.class);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		this.binder.setItemDataSource(this.bean);
		
		mainLayout = new VerticalLayout();
		
		dynamicLayout = new FormLayout();
		waiveLayout = new FormLayout();
		confirmLayout = new FormLayout();
		setReferenceData(bean, intimationDto);
		Panel panel=new Panel();
		panel.addStyleName("girdBorder");
		panel.setContent(buildDetailLayout());
		panel.setWidth("100%");		
		viewDetails.initView(bean.getIntimationNumber(), null,  ViewLevels.OMP ,null);
		mainLayout.addComponents(viewDetails,panel,buildDetailsLayout());
		mainLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		
				
//		setCompositionRoot(panel);
		setWidth("100%");
		setCompositionRoot(mainLayout);
	}
	
	private VerticalLayout buildDetailsLayout(){
		
		VerticalLayout verticalMain = new VerticalLayout();		

		confirmRejectionBtn = new Button("Confirm Rejection");
		confirmRejectionBtn.addStyleName("querybtn");
		waiveRejectionBtn = new Button("Waive Rejection");
		waiveRejectionBtn.addStyleName("querybtn");

		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");

		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");

		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");

		buttonHorLayout = new HorizontalLayout(submitBtn, cancelBtn);
		buttonHorLayout.setSpacing(true);

		rejectionButtonLayout = new HorizontalLayout(confirmRejectionBtn,
				waiveRejectionBtn);
		rejectionButtonLayout.setSpacing(true);
		verticalMain.setWidth("90%");
		verticalMain.addComponents(rejectionButtonLayout,dynamicLayout,buttonHorLayout);
		verticalMain.setComponentAlignment(rejectionButtonLayout,
					Alignment.TOP_RIGHT);		
		verticalMain.setComponentAlignment(buttonHorLayout,Alignment.BOTTOM_CENTER);

		addListener();

		if (this.bean.getStatusKey() != null
				&& this.bean.getStatusKey().equals(
						ReferenceTable.PROCESS_REJECTED)) {

			generateFieldBasedOnConfirmClick(selectedValues);

		}
//		else if (bean.getStatusKey() != null
//				&& this.searchDTO.getStatusKey().equals(
//						ReferenceTable.PREMEDICAL_WAIVED_REJECTION)) {
//
//		}

		return verticalMain;
	
	}
	
	public void addListener() {

		confirmRejectionBtn.addClickListener(new Button.ClickListener() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				OMPProcessRejectionPageViewImpl.DESICION = false;
				DESICION = false;
				isButtonClicked = true;
				fireViewEvent(OMPProcessRejectionPresenter.OMP_CONFIRM_BUTTON,
						null);
			}
		});
		waiveRejectionBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				DESICION = true;
				OMPProcessRejectionPageViewImpl.DESICION = true;
				isButtonClicked = true;
				fireViewEvent(OMPProcessRejectionPresenter.OMP_WAIVE_BUTTON,
						true);
			}
		});

		cancelBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("serial")
			@Override
			public void buttonClick(ClickEvent event) {

				ConfirmDialog dialog = ConfirmDialog.show(getUI(),
						"Confirmation", "Are you sure you want to cancel ?",
						"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									// Confirmed to continue
									releaseHumanTask();
									/*fireViewEvent(
											MenuItemBean.SEARCH_OMP_PROCESS_REJECTION,
											true);*/
								} else {
									// User did not confirm
								}
							}
						});

				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});

		submitBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				if(validatePage()){
					if (!OMPProcessRejectionPageViewImpl.DESICION) {
	
						fireViewEvent(OMPProcessRejectionPresenter.OMP_SUBMIT_DATA,
								bean, false,
								SHAConstants.OUTCOME_FLP_NON_MED_CONFIRM_REJECTION);
	
					} else {
						fireViewEvent(OMPProcessRejectionPresenter.OMP_SUBMIT_DATA,
								bean, true,
								SHAConstants.OUTCOME_OMP_REG_WAIVE_REJECTION);
					}
				}	
			}
		});
		
		  
		  if(clmTypeOptionGroup!=null){
			  clmTypeOptionGroup.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 7455756225751111662L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						OptionGroup totalValue = (OptionGroup) event.getProperty();
						if(totalValue.getValue()!=null && totalValue.getValue().equals(true)){
								if(hospitalisationOptionGroup!=null){
									hospitalisationOptionGroup.setValue(true);
								}
						}
						if(totalValue.getValue()!=null && totalValue.getValue().equals(false)){
							if(hospitalisationOptionGroup!=null){
								hospitalisationOptionGroup.setValue(false);
							}
					}
						
					}
				});
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
			else if (c instanceof ComboBox) {
				ComboBox field = (ComboBox) c;
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
			else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}
	
	public HorizontalLayout buildDetailLayout()
	{
		setReferenceData(bean, intimationDto);
		clmTypeOptionGroup = (OptionGroup)binder.buildAndBind("", "claimType", OptionGroup.class);
		
//		clmTypeOptionGroup = new OptionGroup();
//		clmTypeOptionGroup.setNullSelectionAllowed(true);
		Collection<Boolean> clmTypeCollectionValues = new ArrayList<Boolean>(2);
		clmTypeCollectionValues.add(true);
		clmTypeCollectionValues.add(false);
		
		clmTypeOptionGroup.addItems(clmTypeCollectionValues);
		clmTypeOptionGroup.setItemCaption(true, SHAConstants.CASHLESS_CLAIM_TYPE);
		clmTypeOptionGroup.setItemCaption(false, SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
		clmTypeOptionGroup.setStyleName("horizontal");
		
		hospitalisationOptionGroup = (OptionGroup)binder.buildAndBind("", "hospitalisation", OptionGroup.class);
//		hospitalisationOptionGroup.setNullSelectionAllowed(true);
		Collection<Boolean> hospitalisationCollectionValues = new ArrayList<Boolean>(2);
		hospitalisationCollectionValues.add(true);
		hospitalisationCollectionValues.add(false);
		
		hospitalisationOptionGroup.addItems(hospitalisationCollectionValues);
		hospitalisationOptionGroup.setItemCaption(true, "Hospitalisation");
		hospitalisationOptionGroup.setItemCaption(false, "Non Hospitalisation");
		hospitalisationOptionGroup.addStyleName("horizontal");
		
		remarks = (TextArea) binder.buildAndBind("Suggested Rejection Remarks", "rejectionRemarks", TextArea.class);
		
		provisionAmt = (TextField) binder.buildAndBind("Initial Provision Amt($)","provisionAmt", TextField.class);

		inrConversionRate = (TextField) binder.buildAndBind("INR Conversion Rate*","inrConversionRate", TextField.class);
		
		inrValue = (TextField) binder.buildAndBind("INR Value","inrValue", TextField.class);
		
		eventCode = (ComboBox) binder.buildAndBind("Event Code","eventCode", ComboBox.class);
		
		hospitalName = (TextField) binder.buildAndBind("Hospital Name","hospitalName", TextField.class);
		
		hospitalCity = (TextField) binder.buildAndBind("Hospital City","hospitalCity", TextField.class);
		
		hospitalCounty = (TextField) binder.buildAndBind("Hospital County","hospitalCounty", TextField.class);
		
		ailmentLoss = (TextField) binder.buildAndBind("Ailment/Loss","ailmentLossDetails", TextField.class);
		
		firstForm = new FormLayout(clmTypeOptionGroup,provisionAmt,inrConversionRate,inrValue,eventCode);
		secondForm = new FormLayout(remarks,hospitalisationOptionGroup,hospitalName,hospitalCity,hospitalCounty,ailmentLoss);
		
		HorizontalLayout hLayout1 = new HorizontalLayout(firstForm,secondForm);
		
		setValueReadonly(true);
		
		hLayout1.setHeight("22%");
				
		return hLayout1;			
	}
	
	public void setValueReadonly(boolean enable){
//		firstForm.setReadOnly(enable);
//		secondForm.setReadOnly(enable);
		
		hospitalisationOptionGroup.setReadOnly(enable);
		clmTypeOptionGroup.setReadOnly(enable);
		remarks.setReadOnly(enable);
		provisionAmt.setReadOnly(enable);
		inrConversionRate.setReadOnly(enable);
		inrValue.setReadOnly(true);
		eventCode.setReadOnly(enable);
		hospitalName.setReadOnly(enable);
		hospitalCity.setReadOnly(enable);
		hospitalCounty.setReadOnly(enable);
		ailmentLoss.setReadOnly(enable);
	}
	public void generateFieldBasedOnConfirmClick(
			BeanItemContainer<SelectValue> selectedValues) {

		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		this.bean.setStatusKey(ReferenceTable.PROCESS_REJECTED);

		this.selectedValues = selectedValues;
		unbindField(confirmRemarks);
		unbindField(waiveRemarks);

		confirmRemarks = (TextArea) binder.buildAndBind("Confirm Rejection Remarks",
				"confirmRemarks", TextArea.class);
		confirmRemarks.setWidth("400px");
		
		medicalRemarks = new TextArea("Suggested Rejection Remarks");
		medicalRemarks.setValue(bean.getRejectionRemarks() != null ? bean.getRejectionRemarks() : "");
		medicalRemarks.setEnabled(false);
		medicalRemarks.setWidth("400px");

		mandatoryFields.add(confirmRemarks);
		mandatoryFields.add(medicalRemarks);

		showOrHideValidation(false);

		confirmLayout.removeAllComponents();
		confirmLayout.addComponent(medicalRemarks);
		confirmLayout.addComponent(confirmRemarks);

		waiveLayout.removeAllComponents();
		dynamicLayout.removeComponent(waiveLayout);

		dynamicLayout.addComponent(confirmLayout);
		dynamicLayout.setComponentAlignment(confirmLayout,
				Alignment.BOTTOM_LEFT);

	}

	private void unbindField(Field<?> field) {
		if (field != null) {
			if (binder.getPropertyId(field) != null) {
				this.binder.unbind(field);
			}

		}
	}

	public void generateFieldBasedOnWaiveClick(Boolean isChecked) {

		this.bean.setStatusKey(ReferenceTable.CLAIM_REGISTRATION_WAIVED);

		unbindField(confirmRemarks);
		unbindField(waiveRemarks);

		waiveRemarks = (TextArea) binder.buildAndBind("Waive Rejection Remarks",
				"waiveRemarks", TextArea.class);
		waiveRemarks.setNullRepresentation("");
		waiveRemarks.setWidth("400px");

		mandatoryFields.add(waiveRemarks);

		showOrHideValidation(false);

		confirmLayout.removeAllComponents();
		dynamicLayout.removeComponent(confirmLayout);
		waiveLayout.removeAllComponents();
		waiveLayout.addComponent(waiveRemarks);
		dynamicLayout.addComponent(waiveLayout);
		dynamicLayout.setComponentAlignment(waiveLayout, Alignment.BOTTOM_LEFT);

	}
	
	private void releaseHumanTask() {
		VaadinSession session = getSession();
		Long wrkFlowKey = (Long) session.getAttribute(SHAConstants.WK_KEY);
		DBCalculationService dbService = new DBCalculationService();
		if (wrkFlowKey != null) {
			dbService.callOMPUnlockProcedure(wrkFlowKey);
			getSession().setAttribute(SHAConstants.WK_KEY, null);
		}
	}
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";

		if (isButtonClicked == null) {
			hasError = true;
			eMsg += "Please Click Confirm or Waive Button";
		}

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

		if (hasError) {
			setRequired(true);
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

			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		showOrHideValidation(false);
		return true;
	}

	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}
}
