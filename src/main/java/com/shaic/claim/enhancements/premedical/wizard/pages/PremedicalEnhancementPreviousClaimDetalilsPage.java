package com.shaic.claim.enhancements.premedical.wizard.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.teemu.wizards.WizardStep;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthPreviousClaimsDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.premedical.wizard.PreauthPreviousClaimsTable;
import com.shaic.claim.premedical.wizard.pages.PreMedicalPreauthPreviousClaimsPageUI;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class PremedicalEnhancementPreviousClaimDetalilsPage extends PreMedicalPreauthPreviousClaimsPageUI implements WizardStep<PreauthDTO> {
	private static final long serialVersionUID = -7231510197286214737L;

	@Inject
	private PreauthDTO bean;
	
	@Inject
	private Instance<PreauthPreviousClaimsTable> preauthPreviousClaimsTable;
	
	private PreauthPreviousClaimsTable preauthPreviousClaimsTableObj;
	
	private BeanFieldGroup<PreauthPreviousClaimsDTO> binder;

//	private Button viewButton;

	private OptionGroup viewType;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	@Override
	public String getCaption() {
		return "Previous Claim Details";
	}

	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
		for(PreviousClaimsTableDTO previousClaimsTableDTO : this.bean.getPreviousClaimsList()){
			if(previousClaimsTableDTO.getAdmissionDate()!=null && !previousClaimsTableDTO.getAdmissionDate().equals("")){
//				Date tempDate = SHAUtils.formatTimestamp(previousClaimsTableDTO.getAdmissionDate());
//				previousClaimsTableDTO.setAdmissionDate((SHAUtils.formatDate(tempDate)));
			}												
		}
	}

	@Override
	public Component getContent() {
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		cmbRelapseOfIllness = (ComboBox) binder.buildAndBind("Relapse Of Illness", "relapseOfIllness", ComboBox.class);
		cmbRelapseOfIllness.setNullSelectionAllowed(false);
		cmbRelapseOfIllness.setRequired(true);
		
		leftFLayout = new FormLayout(cmbRelapseOfIllness);
		
		if(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			cmbRelapseOfIllness.setVisible(false);
		}
				
		rightFLayout = new FormLayout();
		
//		viewButton = getViewButton();
		if(this.bean.getNewIntimationDTO().getPolicy().getProductType() != null){
			viewType = getOptionGroup(this.bean.getNewIntimationDTO().getPolicy().getProductType().getValue());
		}else{
			viewType = getOptionGroup("group");
		}
		if (this.bean.getNewIntimationDTO().getPolicy().getProductType().getValue().equalsIgnoreCase("group")){
			viewType.select("Risk Wise");
		}else{
			viewType.select("Policy Wise");
		}
		
		addListener();
		
		PreauthPreviousClaimsTable preauthPreviousClaimsTableInstance = preauthPreviousClaimsTable.get();
		preauthPreviousClaimsTableInstance.init("", false, false);
		this.preauthPreviousClaimsTableObj = preauthPreviousClaimsTableInstance;
		this.preauthPreviousClaimsTableObj.setCaption("Policy Wise");
		HorizontalLayout radioButonHLayout = new HorizontalLayout(viewType/*, viewButton*/);
		HorizontalLayout alignmentHLayout = new HorizontalLayout(radioButonHLayout);
		alignmentHLayout.setWidth("100%");
		alignmentHLayout.setComponentAlignment(radioButonHLayout, Alignment.MIDDLE_CENTER);
		
		VerticalLayout layout = new VerticalLayout(alignmentHLayout,preauthPreviousClaimsTableObj, new HorizontalLayout(leftFLayout, rightFLayout));
		layout.setMargin(true);
		layout.setSpacing(true);
		Panel wholePanel = new Panel("Previous Claim Details");
		wholePanel.setContent(layout);
		
		if(!(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getIsJioPolicy() && bean.getNewIntimationDTO().getIsJioPolicy()) && !(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			if(this.bean.getPreviousClaimsList() != null && this.bean.getPreviousClaimsList().isEmpty()){
				
				fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_ENHANCEMENT_PREVIOUS_CLAIM_FOR_POLICY,bean);
			}
		}
		
		Collection<OptionGroup> itemIds = (Collection<OptionGroup>) viewType.getContainerDataSource().getItemIds();
		if(itemIds != null && !itemIds.isEmpty()) {
			
			if((null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getIsJioPolicy() && bean.getNewIntimationDTO().getIsJioPolicy()) || (ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){	
					
					viewType.removeItem(itemIds.toArray()[0]);
			}
				
			    	viewType.setValue(itemIds.toArray()[0]);
		}
		
		return wholePanel;
	}
	
	public void initBinder() {
			this.binder = new BeanFieldGroup<PreauthPreviousClaimsDTO>(PreauthPreviousClaimsDTO.class);
			this.binder.setItemDataSource(this.bean.getPreauthPreviousClaimsDetails());
	}

	private void addListener() {
		
		viewType.addValueChangeListener(new Property.ValueChangeListener() {
			
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
				
					if (viewType.getValue() != null) {
						if (viewType.getValue().equals(POLICY_WISE)) {
							preauthPreviousClaimsTableObj.setCaption("Policy Wise");
							//preauthPreviousClaimsTableObj.setTableList(bean.getPreviousClaimsList());
							fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_PREVIOUS_CLAIM_DETAILS, bean,SHAConstants.POLICY_WISE);
						} else if (viewType.getValue().equals(INSURED_WISE)) {
							preauthPreviousClaimsTableObj.setCaption("Insured Wise");
						//	fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_PREVIOUS_CLAIM_DETAILS, bean.getPolicyDto().getInsuredId());
							fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_PREVIOUS_CLAIM_DETAILS, bean,SHAConstants.INSURED_WISE);
						} else if (viewType.getValue().equals(RISK_WISE)) {
							preauthPreviousClaimsTableObj.setCaption("Risk Wise");
							//preauthPreviousClaimsTableObj.setTableList(bean.getPreviousClaimsList());
							fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_PREVIOUS_CLAIM_DETAILS, bean,SHAConstants.RISK_WISE);
						}
					}
				
			}

			
		});
		
		
		
//		viewButton.addClickListener(new Button.ClickListener() {
//
//			private static final long serialVersionUID = -676865664871344469L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				if (viewType.getValue() != null) {
//					if (viewType.getValue().equals(POLICY_WISE)) {
//						preauthPreviousClaimsTableObj.setCaption("Policy Wise");
//						preauthPreviousClaimsTableObj.setTableList(bean.getPreviousClaimsList());
//					} else if (viewType.getValue().equals(INSURED_WISE)) {
//						preauthPreviousClaimsTableObj.setCaption("Insured Wise");
//					//	fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_PREVIOUS_CLAIM_DETAILS, bean.getPolicyDto().getInsuredId());
//						fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_PREVIOUS_CLAIM_DETAILS, bean);
//					} else if (viewType.getValue().equals(RISK_WISE)) {
//						preauthPreviousClaimsTableObj.setCaption("Risk Wise");
//						preauthPreviousClaimsTableObj.setTableList(bean.getPreviousClaimsList());
//					}
//				}
//
//			}
//			});
		
		cmbRelapseOfIllness.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 296691832384011391L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_RELAPSE_OF_ILLNESS_CHANGED, bean, (SelectValue)event.getProperty().getValue());
			}
		});
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
	 	BeanItemContainer<SelectValue> commonValues = (BeanItemContainer<SelectValue>) referenceData.get("commonValues");
		
	 	cmbRelapseOfIllness.setContainerDataSource(commonValues);
	 	cmbRelapseOfIllness.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	 	cmbRelapseOfIllness.setItemCaptionPropertyId("value");
	 	if(this.bean.getPreauthPreviousClaimsDetails().getRelapseFlag() != null && this.bean.getPreauthPreviousClaimsDetails().getRelapseFlag().toLowerCase().equalsIgnoreCase("y")) {
	 		cmbRelapseOfIllness.setValue(this.bean.getPreauthPreviousClaimsDetails().getRelapseOfIllness());
	 	} else {
	 		cmbRelapseOfIllness.setValue(cmbRelapseOfIllness.getContainerDataSource().getItemIds().toArray()[0]);
	 	}
	 	
	 	if(this.bean.getNewIntimationDTO() != null && !this.bean.getNewIntimationDTO().getIsJioPolicy()){
	 		
	 		List<PreviousClaimsTableDTO> previousClaimsList = this.bean.getPreviousClaimsList();
	 		if(!previousClaimsList.isEmpty()) {
	 			for (PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimsList) {
	 				preauthPreviousClaimsTableObj.addBeanToList(previousClaimsTableDTO);
	 			}
	 		}
	 	}
	 	
		
	}

	@Override
	public boolean onAdvance() {
		return validatePage();
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onSave() {
		return validatePage();
	}
	
	private void unbindField(Field<?> field) {
		if (field != null)
		{
			Object propertyId = this.binder.getPropertyId(field);
			if(field.isAttached() && propertyId != null) {
				this.binder.unbind(field);	
			}
		}
	}
	
	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		
		if (!this.binder.isValid()) {
		    for (Field<?> field : this.binder.getFields()) {
		    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
		    	if (errMsg != null) {
		    		eMsg.append(errMsg.getFormattedHtmlMessage());
		    	}
		    	hasError = true;
		    }
		 } 
		   if(hasError) {/*
			   Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
			   MessageBox.createError()
		    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
		        .withOkButton(ButtonOption.caption("OK")).open();
			    
			    hasError = true;
			    return !hasError;
		   } else {
				try {
					if(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
						this.binder.commit();
					}
				} catch (CommitException e) {
					e.printStackTrace();
				}
			   showOrHideValidation(false);
			   return true;
		   }
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void generateFieldsBasedOnRelapseOfIllness(Map<String, Object> attachPreviousClaimsData) {
		SelectValue relapseOfIlllness = (SelectValue) cmbRelapseOfIllness.getValue();
		if(relapseOfIlllness != null) {
			if(relapseOfIlllness.getId() == ReferenceTable.COMMONMASTER_YES) {
				txtRemarks = (TextField) binder.buildAndBind("Remarks", "relapseRemarks", TextField.class);
				txtRemarks.setMaxLength(100);
				cmbAttachtoPreviouClaim = (ComboBox) binder.buildAndBind("Attach to Previous Claim", "attachToPreviousClaim", ComboBox.class);
				cmbAttachtoPreviouClaim.setContainerDataSource((BeanItemContainer<SelectValue>)attachPreviousClaimsData.get("relapsedClaims"));
				cmbAttachtoPreviouClaim.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbAttachtoPreviouClaim.setItemCaptionPropertyId("value");
				leftFLayout.addComponent(cmbAttachtoPreviouClaim);
				rightFLayout.addComponent(txtRemarks);
				txtRemarks.setRequired(true);
				txtRemarks.setValidationVisible(false);
				mandatoryFields.add(txtRemarks);
				
				if(this.bean.getPreauthPreviousClaimsDetails().getAttachToPreviousClaim() != null) {
					cmbAttachtoPreviouClaim.setValue(this.bean.getPreauthPreviousClaimsDetails().getAttachToPreviousClaim());
				}
				
			} else {
				if(txtRemarks != null && cmbAttachtoPreviouClaim != null) {
					unbindField(txtRemarks);
					unbindField(cmbAttachtoPreviouClaim);
					leftFLayout.removeComponent(cmbAttachtoPreviouClaim);
					rightFLayout.removeComponent(txtRemarks);
					mandatoryFields.remove(txtRemarks);
					
				}
			}
		} 
	}

	@SuppressWarnings("unchecked")
	public void setRelapsedClaims(Map<String, Object> referenceData){
		cmbRelapseOfIllness.setContainerDataSource((BeanItemContainer<SelectValue>)referenceData.get("relapsedClaims"));
		cmbRelapseOfIllness.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRelapseOfIllness.setItemCaptionPropertyId("value");
	}

	public void setPreviousClaims(List<PreviousClaimsTableDTO> previousClaimsDTO) {
		this.preauthPreviousClaimsTableObj.setTableList(previousClaimsDTO);
	}

	
	public void setPreviousClaimDetailsForPolicy(
			List<PreviousClaimsTableDTO> previousClaimDTOList) {
		
		this.bean.setPreviousClaimsList(previousClaimDTOList);
		this.preauthPreviousClaimsTableObj.setTableList(previousClaimDTOList);
		
	}
	
	public void setClearTableObj(){
		SHAUtils.setClearPreauthDTO(bean);
		//preauthPreviousClaimsTable.destroy(preauthPreviousClaimsTableObj);
	}
}
