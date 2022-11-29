package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.coordinator.view.ViewCoOrdinatorReplyService;
import com.shaic.claim.coordinator.view.ViewCoOrdinatorReplyTable;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.CoordinatorDTO;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.claim.reimbursement.dto.ReimbursementWizardDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;

import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ReimbursementCoordinatorView extends ViewComponent {
	
	private static final long serialVersionUID = 2932723033685619756L;

	private BeanFieldGroup<CoordinatorDTO> fieldGroup;
	
	private OptionGroup refertoCoordinatorRadio;
	
	private ComboBox cmbTypeofCoordinatorRequest;
	
	private TextArea reasonForReferingTxt;
	
	private VerticalLayout coordinatorVLayout;
	
//	private FormLayout dynamicFieldsFLayout;
	private HorizontalLayout dynamicFieldsFLayout;
	
	@Inject
	private ReimbursementWizardDTO bean;
	
	private Map<String, Object> referenceData;
	
	@Inject
	//private Instance<CoordinatorReplyTable> coordinatorTable;
	private Instance<ViewCoOrdinatorReplyTable> viewCoOrdinatorReplyTable;
	
	@Inject 
	
	private ViewCoOrdinatorReplyService viewCoOrdinatorReplyService;
	
	private GWizard wizard;
	
	private Button submitBtn;
	
	private String strMenuName;
	
	@PostConstruct
	public void initView() {
		
	}
	
	public void setUpReference(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		if(this.bean.getCoordinatorDetails() != null)
		{
			refertoCoordinatorRadio.select((this.bean.getCoordinatorDetails().getRefertoCoordinator() != null && this.bean.getCoordinatorDetails().getRefertoCoordinator() ) ? true : false );
			refertoCoordinatorRadio.setValue((this.bean.getCoordinatorDetails().getRefertoCoordinator() != null && this.bean.getCoordinatorDetails().getRefertoCoordinator() ) ? true : false );
			coordinatorVLayout.addComponent(generateFieldsBasedOnCoordinator((Boolean)refertoCoordinatorRadio.getValue()));
		}
	}
	
	public void setWizard(GWizard wizard, String strMenuName) {
		this.wizard = wizard;
		this.strMenuName = strMenuName;
	}
	
	public void init(ReimbursementWizardDTO bean) {
		this.bean = bean;
		initBinder();
		fieldGroup.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		coordinatorVLayout = new VerticalLayout();
		//dynamicFieldsFLayout = new FormLayout();
		dynamicFieldsFLayout = new HorizontalLayout();
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		refertoCoordinatorRadio = (OptionGroup) fieldGroup.buildAndBind( "Refer to Coordinator", "refertoCoordinator", OptionGroup.class);
//		refertoCoordinatorRadio = new OptionGroup("Refer to Coordinator", coordinatorValues);
		refertoCoordinatorRadio.setCaption("Refer to Coordinator");
		refertoCoordinatorRadio.addItems(coordinatorValues);
		refertoCoordinatorRadio.setItemCaption(true, "Yes");
		refertoCoordinatorRadio.setItemCaption(false, "No");
		refertoCoordinatorRadio.setStyleName("horizontal");
		//Vaadin8-setImmediate() refertoCoordinatorRadio.setImmediate(true);
		
		submitBtn = new Button();
		submitBtn.setCaption("Submit");
		//Vaadin8-setImmediate() submitBtn.setImmediate(true);
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		//Vaadin8-setImmediate() submitBtn.setImmediate(true);
		
		addListener();
		
		/*CoordinatorReplyTable coordinatorReplyTable = coordinatorTable.get();
		coordinatorReplyTable.init("", false);*/
		/**
		 * Added for interfacing the co ordinator table in
		 * pre medical data extraction page.
		 * */
		ViewCoOrdinatorReplyTable coordinatorReplyTable = viewCoOrdinatorReplyTable.get();
		coordinatorReplyTable.init("", false, false);

		coordinatorReplyTable.setTableList(viewCoOrdinatorReplyService.search(bean.getIntimationKey()));
		//coordinatorReplyTable.setHeight("75px");

		coordinatorVLayout.addComponents(coordinatorReplyTable, new FormLayout(refertoCoordinatorRadio));
		
		
		//coordinatorVLayout.setHeight("75px");
		coordinatorVLayout.setMargin(true);
		coordinatorVLayout.setSpacing(true);
		
		
		setCompositionRoot(coordinatorVLayout);
	}
	
	protected void initBinder() {
		fieldGroup = new BeanFieldGroup<CoordinatorDTO>(CoordinatorDTO.class);
		this.fieldGroup.setItemDataSource(bean.getCoordinatorDetails());
		
		
	}
	
	private void unbindField(Field<?> field) {
		if (field != null && field.getValue() == null)
		{
			Object fieldGroupField = this.fieldGroup.getPropertyId(field);
			if(fieldGroupField != null) {
				this.fieldGroup.unbind(field);	
			}
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		if(reasonForReferingTxt != null && cmbTypeofCoordinatorRequest != null) {
			reasonForReferingTxt.setRequired(!isVisible);
			reasonForReferingTxt.setValidationVisible(isVisible);
			cmbTypeofCoordinatorRequest.setRequired(!isVisible);
			cmbTypeofCoordinatorRequest.setValidationVisible(isVisible);
		}
	}
	
	public StringBuffer getErrors(StringBuffer errorMessage) throws CommitException {
		showOrHideValidation(true);
		Boolean isValid = true;
		if (!this.fieldGroup.isValid()) {
		    isValid = false;
		    for (Field<?> field : this.fieldGroup.getFields()) {
		    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
		    	if (errMsg != null) {
		    		errorMessage.append(errMsg.getFormattedHtmlMessage());
		    	}
		    }
		 }  
		if(isValid) {
			this.fieldGroup.commit();
		}
		showOrHideValidation(false);
		return errorMessage;
		
	}
	
	protected void addListener() {
		refertoCoordinatorRadio.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 4501157362073741222L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = (Boolean)event.getProperty().getValue();
				
				if(wizard != null) {
					wizard.getButtonPanel().setVisible(true);
					if(isChecked) {
						wizard.getButtonPanel().setVisible(false);
						if((SHAConstants.PRE_MEDICAL_PRE_AUTH).equalsIgnoreCase(strMenuName))
						{
							bean.setStageKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
							bean.setStatusKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_REFER_TO_COORDINATOR_STATUS);
						}
						else if((SHAConstants.PRE_AUTH).equalsIgnoreCase(strMenuName))
						{
							bean.setStageKey(ReferenceTable.PREAUTH_STAGE);
							bean.setStatusKey(ReferenceTable.PREAUTH_REFER_TO_COORDINATOR_STATUS);
						}
						else if((SHAConstants.PRE_MEDICAL_ENHANCEMENT).equalsIgnoreCase(strMenuName))
						{
							bean.setStageKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
							bean.setStatusKey(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_REFER_TO_COORDINATOR_STATUS);
						}
						else if((SHAConstants.PRE_AUTH_ENHANCEMENT).equalsIgnoreCase(strMenuName))
						{
							bean.setStageKey(ReferenceTable.ENHANCEMENT_STAGE);
							bean.setStatusKey(ReferenceTable.ENHANCEMENT_REFER_TO_COORDINATOR_STATUS);
						}
						
						/*bean.setStageKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
						bean.setStatusKey(ReferenceTable.PRE_MEDICAL_PRE_AUTH_REFER_TO_COORDINATOR_STATUS);*/
					}
				}
				
				generateFieldsBasedOnCoordinator(isChecked);
			}
		});
		submitBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
//				bean.setIsRepremedical(true);
				if((SHAConstants.PRE_MEDICAL_PRE_AUTH).equalsIgnoreCase(strMenuName))
				{
					fireViewEvent(PreMedicalPreauthWizardPresenter.PREMEDICAL_COORDINATOR_REQUEST_EVENT, event);
				}
				else if((SHAConstants.PRE_AUTH).equalsIgnoreCase(strMenuName))
				{
					fireViewEvent(PreauthWizardPresenter.PREAUTH_COORDINATOR_REQUEST_EVENT, event);
				}
				else if((SHAConstants.PRE_MEDICAL_ENHANCEMENT).equalsIgnoreCase(strMenuName))
				{
					fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_ENHANCEMENT_COORDINATOR_REQUEST_EVENT, event);
				}
				else if((SHAConstants.PRE_AUTH_ENHANCEMENT).equalsIgnoreCase(strMenuName))
				{
					fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_COORDINATOR_REQUEST_EVENT, event);
				}
			}
		});
		
	}
	
	private HorizontalLayout generateFieldsBasedOnCoordinator(Boolean isChecked) {
		
		
		if(isChecked) {
			
			if(cmbTypeofCoordinatorRequest == null)
			{
				cmbTypeofCoordinatorRequest = (ComboBox) fieldGroup.buildAndBind( "Type of Coordinator Request", "typeofCoordinatorRequest", ComboBox.class);
			}
			if(reasonForReferingTxt == null)
			{
				reasonForReferingTxt = (TextArea) fieldGroup.buildAndBind( "Reason For Referring", "reasonForRefering", TextArea.class);
			}
			
			reasonForReferingTxt.setNullRepresentation("");
			reasonForReferingTxt.setMaxLength(100);
			CSValidator validator = new CSValidator();
			validator.extend(reasonForReferingTxt);
			validator.setRegExp("^[a-zA-Z 0-9.]*$");
			validator.setPreventInvalidTyping(true);
			reasonForReferingTxt.setRequired(true);
			cmbTypeofCoordinatorRequest.setRequired(true);
			reasonForReferingTxt.setValidationVisible(false);
			cmbTypeofCoordinatorRequest.setValidationVisible(false);
			
			BeanItemContainer<SelectValue> typeOfCoordinatorRequest = (BeanItemContainer<SelectValue>) referenceData.get("coordinatorTypeRequest");
			cmbTypeofCoordinatorRequest.setContainerDataSource(typeOfCoordinatorRequest);
			cmbTypeofCoordinatorRequest.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbTypeofCoordinatorRequest.setItemCaptionPropertyId("value");
		
			if(this.bean.getCoordinatorDetails().getTypeofCoordinatorRequest() != null) {
				cmbTypeofCoordinatorRequest.setValue(this.bean.getCoordinatorDetails().getTypeofCoordinatorRequest());
			}
			FormLayout formLayout = new FormLayout(cmbTypeofCoordinatorRequest, reasonForReferingTxt);
			dynamicFieldsFLayout.setWidth("100%");
			dynamicFieldsFLayout.addComponents(formLayout, submitBtn);
			dynamicFieldsFLayout.setComponentAlignment(submitBtn, Alignment.BOTTOM_RIGHT);
		} else {
			if(cmbTypeofCoordinatorRequest != null && reasonForReferingTxt != null) {
				unbindField(cmbTypeofCoordinatorRequest);
				unbindField(reasonForReferingTxt);
				removeComponentsFromLayout(dynamicFieldsFLayout);
				/*dynamicFieldsFLayout.removeComponent(cmbTypeofCoordinatorRequest);
				dynamicFieldsFLayout.removeComponent(reasonForReferingTxt);*/
				
				cmbTypeofCoordinatorRequest.setValue("");
				reasonForReferingTxt.setValue("");
//				coordinatorVLayout.removeComponent(dynamicFieldsFLayout);
			}
			
		}
	return dynamicFieldsFLayout;
	}
	
	private void removeComponentsFromLayout(HorizontalLayout hLayout)
	{
		if( null != hLayout && 0!=hLayout.getComponentCount())
		{
			hLayout.removeAllComponents();
		}
	}
}
