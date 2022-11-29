package com.shaic.paclaim.cashless.preauth.wizard.pages;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthPreviousClaimsDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.premedical.wizard.PreauthPreviousClaimsTable;
import com.shaic.claim.premedical.wizard.pages.PreMedicalPreauthPreviousClaimsPageUI;
import com.shaic.domain.ReferenceTable;
import com.shaic.paclaim.cashless.preauth.wizard.wizardfiles.PAPreauthWizardPresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class PAPreauthPreviousClaimDetailsPage extends PreMedicalPreauthPreviousClaimsPageUI implements WizardStep<PreauthDTO> {

	private static final long serialVersionUID = 4952887833164317208L;

		@Inject
		private PreauthDTO bean;
		
		@Inject
		private Instance<PreauthPreviousClaimsTable> preauthPreviousClaimsTable;
		
		private PreauthPreviousClaimsTable preauthPreviousClaimsTableObj;
		
		private BeanFieldGroup<PreauthPreviousClaimsDTO> binder;

//		private Button viewButton;

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
//					Date tempDate = SHAUtils.formatTimestamp(previousClaimsTableDTO.getAdmissionDate());
//					previousClaimsTableDTO.setAdmissionDate((SHAUtils.formatDate(tempDate)));
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
			cmbRelapseOfIllness.setEnabled(false);
			
			leftFLayout = new FormLayout(cmbRelapseOfIllness);
					
			rightFLayout = new FormLayout();
			
//			viewButton = getViewButton();
			viewType = getOptionGroup(this.bean.getNewIntimationDTO().getPolicy().getProductType().getValue());
			//viewType = getOptionGroup(this.bean.getPolicyDto().getPolicyType().getValue());
			viewType.select("Policy Wise");
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
							preauthPreviousClaimsTableObj.setTableList(bean.getPreviousClaimsList());
						} else if (viewType.getValue().equals(INSURED_WISE)) {
							preauthPreviousClaimsTableObj.setCaption("Insured Wise");
							fireViewEvent(PAPreauthWizardPresenter.PREAUTH_PREVIOUS_CLAIM_DETAILS, bean);
						} else if (viewType.getValue().equals(RISK_WISE)) {
							preauthPreviousClaimsTableObj.setCaption("Risk Wise");
							preauthPreviousClaimsTableObj.setTableList(bean.getPreviousClaimsList());
						}
					}
					
				}

				
			});
//			viewButton.addClickListener(new Button.ClickListener() {
	//
//				private static final long serialVersionUID = -676865664871344469L;
	//
//				@SuppressWarnings("unchecked")
//				@Override
//				public void buttonClick(ClickEvent event) {
//					if (viewType.getValue() != null) {
//						if (viewType.getValue().equals(POLICY_WISE)) {						
//							preauthPreviousClaimsTableObj.setCaption("Policy Wise");
//							preauthPreviousClaimsTableObj.setTableList(bean.getPreviousClaimsList());
//						} else if (viewType.getValue().equals(INSURED_WISE)) {
//							preauthPreviousClaimsTableObj.setCaption("Insured Wise");
//							fireViewEvent(PreauthWizardPresenter.PREAUTH_PREVIOUS_CLAIM_DETAILS, bean);
//						} else if (viewType.getValue().equals(RISK_WISE)) {
//							preauthPreviousClaimsTableObj.setCaption("Risk Wise");
//							preauthPreviousClaimsTableObj.setTableList(bean.getPreviousClaimsList());
//						}
//					}
	//
//				}
//				});
			
			cmbRelapseOfIllness.addValueChangeListener(new ValueChangeListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 296691832384011391L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					if(event.getProperty() != null && (SelectValue)event.getProperty().getValue() != null){// && ((SelectValue)event.getProperty().getValue()).getId() == ReferenceTable.COMMONMASTER_YES) {
						fireViewEvent(PAPreauthWizardPresenter.PREAUTH_RELAPSE_OF_ILLNESS_CHANGED, bean, (SelectValue)event.getProperty().getValue());
					}
					
				}
			});
		}

		@Override
		public void setupReferences(Map<String, Object> referenceData) {
		 	BeanItemContainer<SelectValue> commonValues = (BeanItemContainer<SelectValue>) referenceData.get("commonValues");
			
		 	cmbRelapseOfIllness.setContainerDataSource(commonValues);
		 	cmbRelapseOfIllness.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 	cmbRelapseOfIllness.setItemCaptionPropertyId("value");
		 	if(this.bean.getPreauthPreviousClaimsDetails().getRelapseOfIllness() != null) {
		 		cmbRelapseOfIllness.setValue(this.bean.getPreauthPreviousClaimsDetails().getRelapseOfIllness());;
		 	} else {
		 		cmbRelapseOfIllness.setValue(cmbRelapseOfIllness.getContainerDataSource().getItemIds().toArray()[0]);
		 	}
			
			if(!this.bean.getPreviousClaimsList().isEmpty()) {
				List<PreviousClaimsTableDTO> previousClaimsList = this.bean.getPreviousClaimsList();
				for (PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimsList) {
					this.preauthPreviousClaimsTableObj.addBeanToList(previousClaimsTableDTO);
				}
			}
			
		}

		@Override
		public boolean onAdvance() {
			return validatePage();
		}

		@Override
		public boolean onBack() {
			this.bean.setIsBack(true);
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
			String eMsg = "";
			
			if (!this.binder.isValid()) {
			    for (Field<?> field : this.binder.getFields()) {
			    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
			    	if (errMsg != null) {
			    		eMsg += errMsg.getFormattedHtmlMessage();
			    	}
			    	hasError = true;
			    }
			 } 
			   if(hasError) {
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
					txtRemarks.setMaxLength(100);
					mandatoryFields.add(txtRemarks);
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
		
		
		public void setPreviousClaims(
				List<PreviousClaimsTableDTO> previousClaimDTOList) {
			this.preauthPreviousClaimsTableObj.setTableList(previousClaimDTOList);
			
		}
		
}
