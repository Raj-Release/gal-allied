package com.shaic.claim.preauth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import com.shaic.claim.preauth.wizard.dto.CoordinatorDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.ClaimRequestDataExtractionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.MedicalApprovalDataExtractionPagePresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.dataextraction.PAHealthClaimRequestDataExtractionPagePresenter;
import com.vaadin.cdi.UIScoped;
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

@UIScoped
public class PreauthCoordinatorView extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8729502214715478987L;
	
	private BeanFieldGroup<CoordinatorDTO> fieldGroup;
	
	private OptionGroup refertoCoordinatorRadio;
	
	private ComboBox cmbTypeofCoordinatorRequest;
	
	private TextArea reasonForReferingTxt;
	
	private List<String> errorMessages = new ArrayList<String>();
	
	private VerticalLayout coordinatorVLayout;
	
//	private FormLayout dynamicFieldsFLayout;
	private HorizontalLayout dynamicFieldsFLayout;
	
	@Inject
	private PreauthDTO bean;
	
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
			refertoCoordinatorRadio.select((this.bean.getCoordinatorDetails().getRefertoCoordinator() != null && this.bean.getCoordinatorDetails().getRefertoCoordinator() ) ? false : false );
			refertoCoordinatorRadio.setValue((this.bean.getCoordinatorDetails().getRefertoCoordinator() != null && this.bean.getCoordinatorDetails().getRefertoCoordinator() ) ? false : false );
			this.bean.getCoordinatorDetails().setReasonForRefering(null);
			this.bean.getCoordinatorDetails().setTypeofCoordinatorRequest(null);
			coordinatorVLayout.addComponent(generateFieldsBasedOnCoordinator(false));
		}
	}
	
	public void setWizard(GWizard wizard, String strMenuName) {
		this.wizard = wizard;
		this.strMenuName = strMenuName;
	}
	
	public void init(PreauthDTO bean) {
		this.bean = bean;
		initBinder();
		
		
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
		
		/***
		 * As per ticket number 4367 , disabled coordinator radio button
		 */
		if(strMenuName != null && strMenuName.equalsIgnoreCase(SHAConstants.PRE_AUTH)
				|| strMenuName.equalsIgnoreCase(SHAConstants.PRE_MEDICAL_PRE_AUTH)
				|| strMenuName.equalsIgnoreCase(SHAConstants.PRE_MEDICAL_ENHANCEMENT)
				|| strMenuName.equalsIgnoreCase(SHAConstants.PRE_AUTH_ENHANCEMENT)){
			refertoCoordinatorRadio.setValue(false);
			refertoCoordinatorRadio.setEnabled(false);
		}
		
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
//		coordinatorReplyTable.setHeight("250px");
		FormLayout referform =new FormLayout(refertoCoordinatorRadio);
		referform.setMargin(false);
		coordinatorVLayout.addComponents(coordinatorReplyTable,referform);
		
		
		//coordinatorVLayout.setHeight("75px");
		coordinatorVLayout.setMargin(true);
		coordinatorVLayout.setSpacing(true);
		
		showOrHideValidation(false);
		
		
		setCompositionRoot(coordinatorVLayout);
	}
	
	protected void initBinder() {
		fieldGroup = new BeanFieldGroup<CoordinatorDTO>(CoordinatorDTO.class);
		this.fieldGroup.setItemDataSource(bean.getCoordinatorDetails());
		fieldGroup.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	private void unbindField(Field<?> field) {
		if (field != null)
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
	
	public Boolean isValid() throws CommitException {
		showOrHideValidation(true);
		Boolean isValid = true;
		boolean hasError = false;
		errorMessages.removeAll(errorMessages);
		if (!this.fieldGroup.isValid()) {
		    isValid = false;
		    for (Field<?> field : this.fieldGroup.getFields()) {
		    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
		    	if (errMsg != null) {
//		    		errorMessage += errMsg.getFormattedHtmlMessage();
		    		errorMessages.add(errMsg.getFormattedHtmlMessage());
		    		hasError=true;
		    	}
		    }
		 }  
		
		if(isValid) {
			this.fieldGroup.commit();
			this.bean.getCoordinatorDetails().setTypeofCoordinatorRequest( (cmbTypeofCoordinatorRequest != null && cmbTypeofCoordinatorRequest.getValue() != null) ?
					(SelectValue)cmbTypeofCoordinatorRequest.getValue() : null );
		}
		showOrHideValidation(false);
		return !hasError;
		
	}
	
	public List<String> getErrors() {
		return this.errorMessages;
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
						else if((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(strMenuName))
						{
							bean.setStageKey(ReferenceTable.ZONAL_REVIEW_STAGE);
							bean.setStatusKey(ReferenceTable.ZONAL_REVIEW_REFER_TO_COORDINATOR_STATUS);
						}
						else if((SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(strMenuName))
						{
							bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
							bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);
						} else if((SHAConstants.PA_CLAIM_REQUEST).equalsIgnoreCase(strMenuName))
						{
							bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
							bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_REFER_TO_COORDINATOR_STATUS);
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
				bean.setIsRepremedical(true);
				if((SHAConstants.PRE_MEDICAL_PRE_AUTH).equalsIgnoreCase(strMenuName))
				{
					fireViewEvent(PreMedicalPreauthWizardPresenter.PREMEDICAL_COORDINATOR_REQUEST_EVENT, event);
				}
				else if((SHAConstants.PRE_AUTH).equalsIgnoreCase(strMenuName))
				{
				    bean.setStageKey(ReferenceTable.PREAUTH_STAGE);
					fireViewEvent(PreauthWizardPresenter.PREAUTH_COORDINATOR_REQUEST_EVENT, event);
				}
				else if((SHAConstants.PRE_MEDICAL_ENHANCEMENT).equalsIgnoreCase(strMenuName))
				{
					if(bean.getStatusKey() != null && ! bean.getStatusKey().equals(ReferenceTable.PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY)
							&& ! bean.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_RECEIVED_STATUS)){
						
//						bean.setIsRepremedical(false);
						if(bean.getIsPreMedicalForCoordinator()){
							bean.setIsRepremedical(false);
						}
						
					}
					fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_ENHANCEMENT_COORDINATOR_REQUEST_EVENT, event);
				}
				else if((SHAConstants.PRE_AUTH_ENHANCEMENT).equalsIgnoreCase(strMenuName))
				{
					bean.setStageKey(ReferenceTable.ENHANCEMENT_STAGE);
					fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_COORDINATOR_REQUEST_EVENT, event);
				}
				else if((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(strMenuName))
				{
					fireViewEvent(MedicalApprovalDataExtractionPagePresenter.MEDICAL_APPROVAL_COORDINATIOR, event);
		        }
				else if((SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(strMenuName))
				{
					fireViewEvent(ClaimRequestDataExtractionPagePresenter.CLAIM_REQUEST_COORDINATOR, event);
		        } else if((SHAConstants.PA_CLAIM_REQUEST).equalsIgnoreCase(strMenuName))
				{
					fireViewEvent(PAHealthClaimRequestDataExtractionPagePresenter.CLAIM_REQUEST_COORDINATOR, event);
		        }
			
			}
		});
		
	}
	
	private HorizontalLayout generateFieldsBasedOnCoordinator(Boolean isChecked) {
		this.bean.getCoordinatorDetails().setRefertoCoordinator(isChecked);
		unbindField(cmbTypeofCoordinatorRequest);
		unbindField(reasonForReferingTxt);
		if(isChecked) {
			initBinder();
			cmbTypeofCoordinatorRequest = (ComboBox) fieldGroup.buildAndBind( "Type of Coordinator Request", "typeofCoordinatorRequest", ComboBox.class);
			reasonForReferingTxt = (TextArea) fieldGroup.buildAndBind( "Reason For Referring", "reasonForRefering", TextArea.class);
		
			reasonForReferingTxt.setNullRepresentation("");
			reasonForReferingTxt.setMaxLength(100);
			CSValidator validator = new CSValidator();
//			validator.extend(reasonForReferingTxt);
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
			cmbTypeofCoordinatorRequest.setNullSelectionAllowed(false);
		
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
		showOrHideValidation(false);
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
