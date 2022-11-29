package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.previousclaims;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;





import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthPreviousClaimsDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.premedical.wizard.PreauthPreviousClaimsTable;
import com.shaic.claim.premedical.wizard.pages.PreMedicalPreauthPreviousClaimsPageUI;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.previousclaims.ClaimRequestPreviousClaimsPagePresenter;
import com.shaic.domain.ReferenceTable;
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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
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

public class MedicalApprovalPreviousClaimsPageUI extends PreMedicalPreauthPreviousClaimsPageUI {
	//public class PreauthPreviousClaimDetailsPage extends PreauthPreviousClaimsPageUI implements WizardStep<PreauthDTO> {


		private static final long serialVersionUID = 7353950175329383688L;
		
		@Inject
		private PreauthDTO bean;
		
		//private GWizard wizard;
		
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

		public void init(PreauthDTO bean) {
			this.bean = bean;
		}

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
			
//			viewButton = getViewButton();
			viewType = getOptionGroup(this.bean.getNewIntimationDTO().getPolicy().getProductType().getValue());
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
					
					fireViewEvent(MedicalApprovalPreviousClaimsPagePresenter.ZONAL_REVIEW_PREVIOUS_CLAIM_FOR_POLICY,bean);
				}
			}
			
			Collection<OptionGroup> itemIds = (Collection<OptionGroup>) viewType.getContainerDataSource().getItemIds();
			if(itemIds != null && !itemIds.isEmpty()) {
				
				if((null != bean.getNewIntimationDTO() && (null != bean.getNewIntimationDTO().getIsJioPolicy() && bean.getNewIntimationDTO().getIsJioPolicy() ||
						ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
						(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
						 || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.RAKSHAK_CORONA_PRODUCT_KEY)))){	
						
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
							fireViewEvent(MedicalApprovalPreviousClaimsPagePresenter.ZONAL_REVIEW_PREVIOUS_CLAIM_DETAILS, bean,SHAConstants.POLICY_WISE);
						} else if (viewType.getValue().equals(INSURED_WISE)) {
							preauthPreviousClaimsTableObj.setCaption("Insured Wise");
							//fireViewEvent(PreauthWizardPresenter.PREAUTH_PREVIOUS_CLAIM_DETAILS, bean.getPolicyDto().getInsuredId());
							fireViewEvent(MedicalApprovalPreviousClaimsPagePresenter.ZONAL_REVIEW_PREVIOUS_CLAIM_DETAILS, bean,SHAConstants.INSURED_WISE);
						} else if (viewType.getValue().equals(RISK_WISE)) {
							preauthPreviousClaimsTableObj.setCaption("Risk Wise");
							//preauthPreviousClaimsTableObj.setTableList(bean.getPreviousClaimsList());
							fireViewEvent(MedicalApprovalPreviousClaimsPagePresenter.ZONAL_REVIEW_PREVIOUS_CLAIM_DETAILS, bean,SHAConstants.RISK_WISE);
						}
					}
				
			}

			
		    });
			
			
//			viewButton.addClickListener(new Button.ClickListener() {
//
//				private static final long serialVersionUID = -676865664871344469L;
//
//				@Override
//				public void buttonClick(ClickEvent event) {
//
//					if (viewType.getValue() != null) {
//						if (viewType.getValue().equals(POLICY_WISE)) {
//							preauthPreviousClaimsTableObj.setCaption("Policy Wise");
//							preauthPreviousClaimsTableObj.setTableList(bean.getPreviousClaimsList());
//						} else if (viewType.getValue().equals(INSURED_WISE)) {
//							preauthPreviousClaimsTableObj.setCaption("Insured Wise");
//							//fireViewEvent(PreauthWizardPresenter.PREAUTH_PREVIOUS_CLAIM_DETAILS, bean.getPolicyDto().getInsuredId());
//							fireViewEvent(MedicalApprovalPreviousClaimsPagePresenter.MEDICAL_APPROVAL_PREVIOUS_CLAIM_DETAILS, bean);
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
						fireViewEvent(MedicalApprovalPreviousClaimsPagePresenter.MEDICAL_APPROVAL_RELAPSE_OF_ILLNESS_CHANGED, bean, (SelectValue)event.getProperty().getValue());
					}
					
				}
			});
		}

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
			
		 	if(this.bean.getNewIntimationDTO() != null && !this.bean.getNewIntimationDTO().getIsJioPolicy()){
		 		
		 		if(!this.bean.getPreviousClaimsList().isEmpty()) {
		 			List<PreviousClaimsTableDTO> previousClaimsList = this.bean.getPreviousClaimsList();
		 			for (PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimsList) {
		 				this.preauthPreviousClaimsTableObj.addBeanToList(previousClaimsTableDTO);
		 			}
		 		}
		 	}
			
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
		
		public boolean validatePage() {
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
			   if(hasError) {
				  /* Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
					GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
					
				    hasError = true;
				    return !hasError;
			   } else {
					try {
						if(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
							this.binder.commit();
						}
						
						//R1152
						if(!this.bean.getIsGeoSame()){
							getGeoBasedOnCPU();
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

		public void setPreviousClaimDetailsForPolicy(List<PreviousClaimsTableDTO> previousClaimDTOList) {
			this.preauthPreviousClaimsTableObj.setTableList(previousClaimDTOList);
			
		}
		
		public void getGeoBasedOnCPU() {	 
			 
			 Label successLabel = new Label(
						"<b style = 'color: red;'>" + SHAConstants.GEO_PANT_HOSP_ALERT_MESSAGE + "</b>",
						ContentMode.HTML);
			/*	Button homeButton = new Button("OK");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
				layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
				layout.setSpacing(true);
				layout.setMargin(true);
				layout.setStyleName("borderLayout");

				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setClosable(false);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox(SHAConstants.GEO_PANT_HOSP_ALERT_MESSAGE, buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						 
					}
				});
			}
		
}
