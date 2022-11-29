package com.shaic.claim.rod.wizard.tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;




import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.claim.reimbursement.billing.pages.billreview.BillingReviewPagePresenter;
import com.shaic.claim.reimbursement.financialapproval.pages.billreview.FinancialReviewPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.ClaimRequestDataExtractionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.MedicalApprovalDataExtractionPagePresenter;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.pages.AcknowledgeDocReceivedWizardPresenter;
import com.shaic.claim.rod.wizard.pages.BillEntryWizardPresenter;
import com.shaic.claim.rod.wizard.pages.CreateOnlineRODWizardPresenter;
import com.shaic.claim.rod.wizard.pages.CreateRODWizardPresenter;
import com.shaic.domain.HospitalService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SectionDetailsListenerTable extends ViewComponent {

	private static final long serialVersionUID = 3983656295247163640L;

	private Table table;
	
	PreauthDTO bean;

	public Object[] VISIBLE_COLUMNS = new Object[] { "section", "cover",
			"subCover" };

	private Map<String, Object> referenceData;
	
	public Map<String, Object> getReferenceData() {
		return referenceData;
	}

	private String presenterString ;
	
	private List<String> errorMessages;

	BeanItemContainer<SectionDetailsTableDTO> beanItemContainer = new BeanItemContainer<SectionDetailsTableDTO>(
			SectionDetailsTableDTO.class);

	private Map<SectionDetailsTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<SectionDetailsTableDTO, HashMap<String, AbstractField<?>>>();
	
	private BeanItemContainer<SelectValue> secCover;
	
	private BeanItemContainer<SelectValue> subCover;
	
	private static Validator validator;
	
	@EJB
	private PolicyService policyService;
	
//	public TextField dummyField;
	
	public TextField dummySubCoverField;

	public void init(PreauthDTO bean, String presenterString) {
		this.bean = bean;
		this.presenterString = presenterString;
//		dummyField = new TextField();
		dummySubCoverField = new TextField();
		this.errorMessages = new ArrayList<String>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		VerticalLayout layout = new VerticalLayout();
		initTable(layout);
		table.setWidth("100%");
		table.setHeight("90px");
		table.setPageLength(table.getItemIds().size());
		layout.addComponent(table);		
		table.setEnabled(true);
		setCompositionRoot(layout);
	}
	
	public void enableDisableTable(boolean option){
		table.setEnabled(option);
	}

	public void initTable(VerticalLayout layout) {

		table = new Table("Section Details", beanItemContainer);
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");

		table.setColumnHeader("section", "Section");
		table.setColumnHeader("cover", "Cover");
		table.setColumnHeader("subCover", "Sub Cover");
		table.setEditable(true);

		layout.addComponent(table);

		table.setTableFieldFactory(new ImmediateFieldFactory());
		manageListeners();
	}

	protected void manageListeners() {

		for (SectionDetailsTableDTO sectionDetailsDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem
					.get(sectionDetailsDTO);

			final ComboBox section = (ComboBox) combos.get("section");
			final ComboBox cover = (ComboBox) combos.get("cover");
			final ComboBox subCover = (ComboBox) combos.get("subCover");

		}
	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	public void addBeanToList(SectionDetailsTableDTO sectionDetailDTO) {
		beanItemContainer.addItem(sectionDetailDTO);
		manageListeners();
	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@SuppressWarnings("unchecked")
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			final SectionDetailsTableDTO sectionDetails = (SectionDetailsTableDTO) itemId;

			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(sectionDetails) == null) {
				tableItem.put(sectionDetails,
						new HashMap<String, AbstractField<?>>());

			}
			tableRow = tableItem.get(sectionDetails);

			if ("section".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("150px");
				tableRow.put("section", box);

				addSectionValues(box, sectionDetails);
				box.setData(sectionDetails);
				addSectionListener(box);
				if(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() == null && bean.getClaimDTO().getClaimSectionCode() != null) {
					BeanItemContainer<SelectValue> section = (BeanItemContainer<SelectValue>) referenceData
							.get("sectionDetails");					
					
					SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(section.getItemIds(), bean.getClaimDTO().getClaimSectionCode());
					
					box.setValue(correctSelectValue);
					sectionDetails.setSection(correctSelectValue);
					doSectionManipulation(box, false);
				} 
				box.setEnabled(false);
				enableOrDisable(box);
			
				return box;
			} else if ("cover".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("150px");
				box.setData(sectionDetails);
				tableRow.put("cover", box);
				addCoverListener(box);
				GComboBox sectionCombo = (GComboBox) tableRow.get("section");
				if(sectionDetails.getSection() != null && sectionDetails.getCover() == null) {
					doSectionManipulation(sectionCombo, false);
				} else {
					if(sectionDetails.getSection() != null) {
						addSecCover(sectionDetails.getSection().getId(), box, sectionDetails.getCover(), sectionDetails.getSection().getCommonValue(), sectionDetails);
					}
					
				}
				box.setEnabled(false);
				enableOrDisable(box);
				return box;
			} else if ("subCover".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("150px");
				box.setData(sectionDetails);
				tableRow.put("subCover", box);
				GComboBox coverCombo = (GComboBox) tableRow.get("cover");
				GComboBox sectionCombo = (GComboBox) tableRow.get("section");
				addSubCoverListener(box);
				if(sectionDetails.getCover() != null && sectionDetails.getSubCover() == null) {
					doCoverManipulations(coverCombo, false);
				}  else {
					if(sectionDetails.getCover() != null) {
						addSubCover(sectionDetails.getCover().getId(), box, sectionDetails.getSubCover(), sectionDetails.getCover().getCommonValue(), sectionDetails);
					}
				}
				if(SHAConstants.PRE_MEDICAL_PRE_AUTH.equalsIgnoreCase(presenterString) || SHAConstants.PRE_AUTH.equalsIgnoreCase(presenterString) || SHAConstants.PRE_MEDICAL_ENHANCEMENT.equalsIgnoreCase(presenterString) || SHAConstants.PRE_AUTH_ENHANCEMENT.equalsIgnoreCase(presenterString)) {
					if(bean.getClaimDTO().getClaimSectionCode() == null && referenceData != null) {
						BeanItemContainer<SelectValue> section = (BeanItemContainer<SelectValue>) referenceData
								.get("sectionDetails");
						SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(section.getItemIds(), ReferenceTable.HOSPITALIZATION_SECTION_CODE);
						sectionCombo.setValue(correctSelectValue);
						sectionDetails.setSection(correctSelectValue);
						doSectionManipulation(sectionCombo, false);
					}
					
				}
				// addSubCoverValues(box);
				
				
				box.setEnabled(false);
				enableOrDisable(box);
				return box;
			} else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}

		}
	}

	@SuppressWarnings("unchecked")
	public void addSectionValues(ComboBox comboBox, SectionDetailsTableDTO sectionDetails) {
		if(referenceData != null) {
			BeanItemContainer<SelectValue> section = (BeanItemContainer<SelectValue>) referenceData
					.get("sectionDetails");
			
			if(null != bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsPaayasPolicy()){
				
					List<SelectValue> value = section.getItemIds();
					List<SelectValue> paayasSectionList = new ArrayList<SelectValue>();
					if(null != value){
						for (SelectValue selectValue : value) {
							if(null != selectValue.getId() && ReferenceTable.getTataTrustSectionKeys().containsKey((selectValue.getId()))){
								
								paayasSectionList.add(selectValue);
								
							}

						}
						section.removeAllItems();
						section.addAll(paayasSectionList);
					}
				
			}
			comboBox.setContainerDataSource(section);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");
		}
		

	}

	@SuppressWarnings("unchecked")
	public void addCoverValues(ComboBox comboBox) {
		if(referenceData != null) {
			BeanItemContainer<SelectValue> cover = (BeanItemContainer<SelectValue>) referenceData
					.get("cover");
			comboBox.setContainerDataSource(cover);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");
		}
		

	}

	@SuppressWarnings("unchecked")
	public void addSubCoverValues(ComboBox comboBox) {
		if(referenceData != null) {
			BeanItemContainer<SelectValue> coverValues = (BeanItemContainer<SelectValue>) referenceData
					.get("subCover");
			comboBox.setContainerDataSource(coverValues);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");
		}
		

	}

	public List<SectionDetailsTableDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<SectionDetailsTableDTO> itemIds = (List<SectionDetailsTableDTO>) this.table
				.getItemIds();
		if (itemIds.isEmpty()) {
			itemIds = new ArrayList<SectionDetailsTableDTO>();
		}
		return itemIds;
	}
	
	public SectionDetailsTableDTO getValue() {
		@SuppressWarnings("unchecked")
		List<SectionDetailsTableDTO> itemIds = (List<SectionDetailsTableDTO>) this.table
				.getItemIds();
		if (itemIds.isEmpty()) {
			itemIds = new ArrayList<SectionDetailsTableDTO>();
		}
		return itemIds.isEmpty() ? bean.getPreauthDataExtractionDetails().getSectionDetailsDTO() : itemIds.get(0) ;
	}

	@SuppressWarnings("unused")
	private void addSectionListener(final ComboBox sectionCombo) {
		if (sectionCombo != null) {
			sectionCombo.addListener(new Listener() {
				private static final long serialVersionUID = 8513155091255795975L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					doSectionManipulation(component, true);

				}

			
			});
		}

	}

	public void addSubCover(Long coverComboKey, ComboBox subCoverCombo,
			SelectValue value, String coverCode, SectionDetailsTableDTO sectionDetailsDTO) {
		if (SHAConstants.PRE_MEDICAL_PRE_AUTH.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PreMedicalPreauthWizardPresenter.GET_SUB_COVER,
					coverComboKey);
		} else if (SHAConstants.PRE_AUTH.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PreauthWizardPresenter.GET_SUB_COVER,
					coverComboKey);
		} else if (SHAConstants.PRE_MEDICAL_ENHANCEMENT.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PremedicalEnhancementWizardPresenter.GET_SUB_COVER,
					coverComboKey);
		}else if (SHAConstants.PRE_AUTH_ENHANCEMENT.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PreauthEnhancemetWizardPresenter.GET_SUB_COVER,
					coverComboKey);
		}else if (SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(AcknowledgeDocReceivedWizardPresenter.GET_SUB_COVER,
					coverComboKey);
		}else if (SHAConstants.ZONAL_REVIEW.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(MedicalApprovalDataExtractionPagePresenter.GET_SUB_COVER,
					coverComboKey);
		}else if (SHAConstants.CLAIM_REQUEST.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(ClaimRequestDataExtractionPagePresenter.GET_SUB_COVER,
					coverComboKey);
		}else if (SHAConstants.BILLING.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(BillingReviewPagePresenter.GET_SUB_COVER,
					coverComboKey);
		} else if (SHAConstants.FINANCIAL.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(FinancialReviewPagePresenter.GET_SUB_COVER,
					coverComboKey);
		}  else if (SHAConstants.CREATE_ROD.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(CreateRODWizardPresenter.GET_SUB_COVER,
					coverComboKey);
		} else if (SHAConstants.BILL_ENTRY.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(BillEntryWizardPresenter.GET_SUB_COVER,
					coverComboKey);
		} else if (SHAConstants.CREATE_ONLINE_ROD.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(CreateOnlineRODWizardPresenter.GET_ONLINE_SUB_COVER,
					coverComboKey);
		} 
		
		subCoverCombo.setContainerDataSource(subCover);
		subCoverCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		subCoverCombo.setItemCaptionPropertyId("value");

		
		if(coverCode.equalsIgnoreCase(ReferenceTable.BARIATRIC_COVER_CODE)) {
			SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(subCover.getItemIds(), ReferenceTable.BARIATRIC_SUB_COVER_CODE);
			subCoverCombo.setValue(correctSelectValue);
			sectionDetailsDTO.setSubCover(correctSelectValue);
		} else if(coverCode.equalsIgnoreCase(ReferenceTable.HOSP_COVER_CODE)) {
			SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(subCover.getItemIds(), bean.getClaimDTO().getClaimSubCoverCode() != null ? bean.getClaimDTO().getClaimSubCoverCode() : ReferenceTable.HOSP_SUB_COVER_CODE);
			subCoverCombo.setValue(correctSelectValue);
			sectionDetailsDTO.setSubCover(correctSelectValue);
		} else if(coverCode.equalsIgnoreCase(ReferenceTable.LUMPSUM_COVER_CODE)) {
			SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(subCover.getItemIds(), ReferenceTable.LUMPSUM_SUB_COVER_CODE);
			subCoverCombo.setValue(correctSelectValue);
			sectionDetailsDTO.setSubCover(correctSelectValue);
		} else if(coverCode.equalsIgnoreCase(ReferenceTable.HOSPITAL_CASH_COVER_CODE)) {
			SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(subCover.getItemIds(), ReferenceTable.HOSPITAL_CASH_SUB_COVER_CODE);
			subCoverCombo.setValue(correctSelectValue);
			sectionDetailsDTO.setSubCover(correctSelectValue);
		}
		 else if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && coverCode.equalsIgnoreCase(ReferenceTable.ASSISTED_REPRODUCTION_COVER_CODE)) {
				SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(subCover.getItemIds(), ReferenceTable.ASSISTED_REPRODUCTION_SUB_COVER_CODE);
				subCoverCombo.setValue(correctSelectValue);
				sectionDetailsDTO.setSubCover(correctSelectValue);
			}
		else if(bean.getClaimDTO().getClaimSubCoverCode() != null) {
			SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(subCover.getItemIds(), bean.getClaimDTO().getClaimSubCoverCode());
			subCoverCombo.setValue(correctSelectValue);
			sectionDetailsDTO.setSubCover(correctSelectValue);
		}
		
		else if (value != null) {
			subCoverCombo.setValue(value);
		} 
		
	}
	
	public void addSecCover(Long sessionComboKey, ComboBox coverCombo,
			SelectValue value, String sectionCode, SectionDetailsTableDTO sectionDetailsDTO) {
		
		if(coverCombo != null ) {
			if (SHAConstants.PRE_MEDICAL_PRE_AUTH.equalsIgnoreCase(this.presenterString)) {
				fireViewEvent(PreMedicalPreauthWizardPresenter.GET_SEC_COVER,
						sessionComboKey,bean.getNewIntimationDTO().getPolicy().getKey());
			} else if (SHAConstants.PRE_AUTH.equalsIgnoreCase(this.presenterString)) {
				fireViewEvent(PreauthWizardPresenter.GET_SEC_COVER,
						sessionComboKey);
			} else if (SHAConstants.PRE_MEDICAL_ENHANCEMENT.equalsIgnoreCase(this.presenterString)) {
				fireViewEvent(PremedicalEnhancementWizardPresenter.GET_SEC_COVER,
						sessionComboKey);
			} else if (SHAConstants.PRE_AUTH_ENHANCEMENT.equalsIgnoreCase(this.presenterString)) {
				fireViewEvent(PreauthEnhancemetWizardPresenter.GET_SEC_COVER,
						sessionComboKey);
			} else if (SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(this.presenterString)) {
				fireViewEvent(AcknowledgeDocReceivedWizardPresenter.GET_SEC_COVER,
						sessionComboKey);
			} else if (SHAConstants.ZONAL_REVIEW.equalsIgnoreCase(this.presenterString)) {
				fireViewEvent(MedicalApprovalDataExtractionPagePresenter.GET_SEC_COVER,
						sessionComboKey);
			} else if (SHAConstants.CLAIM_REQUEST.equalsIgnoreCase(this.presenterString)) {
				fireViewEvent(ClaimRequestDataExtractionPagePresenter.GET_SEC_COVER,
						sessionComboKey);	
			} else if (SHAConstants.BILLING.equalsIgnoreCase(this.presenterString)) {
				fireViewEvent(BillingReviewPagePresenter.GET_SEC_COVER,
						sessionComboKey);
			} else if (SHAConstants.FINANCIAL.equalsIgnoreCase(this.presenterString)) {
				fireViewEvent(FinancialReviewPagePresenter.GET_SEC_COVER,
						sessionComboKey);
			} else if (SHAConstants.CREATE_ROD.equalsIgnoreCase(this.presenterString)) {
				fireViewEvent(CreateRODWizardPresenter.GET_SEC_COVER,
						sessionComboKey);
			} else if (SHAConstants.BILL_ENTRY.equalsIgnoreCase(this.presenterString)) {
				fireViewEvent(BillEntryWizardPresenter.GET_SEC_COVER,
						sessionComboKey);
			}else if (SHAConstants.CREATE_ONLINE_ROD.equalsIgnoreCase(this.presenterString)) {
				fireViewEvent(CreateOnlineRODWizardPresenter.GET_ONLINE_SEC_COVER,
						sessionComboKey);
			}
			
			
			coverCombo.setContainerDataSource(secCover);
			coverCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			coverCombo.setItemCaptionPropertyId("value");
			
			coverCombo.setValue(null);
			
			if(sectionCode.equalsIgnoreCase(ReferenceTable.BARIATRIC_SECTION_CODE)) {
				SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(secCover.getItemIds(), ReferenceTable.BARIATRIC_COVER_CODE);
				coverCombo.setValue(correctSelectValue);
				sectionDetailsDTO.setCover(correctSelectValue);
			} else if(sectionCode.equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE)) {
				SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(secCover.getItemIds(),bean.getClaimDTO().getClaimCoverCode() != null ? bean.getClaimDTO().getClaimCoverCode() : ReferenceTable.HOSP_COVER_CODE);
				coverCombo.setValue(correctSelectValue);
				sectionDetailsDTO.setCover(correctSelectValue);
			} else if(sectionCode.equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)) {
				SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(secCover.getItemIds(), ReferenceTable.LUMPSUM_COVER_CODE);
				coverCombo.setValue(correctSelectValue);
				sectionDetailsDTO.setCover(correctSelectValue);
			} else if(sectionCode.equalsIgnoreCase(ReferenceTable.HOSPITAL_CASH_SECTION_CODE)) {
				SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(secCover.getItemIds(), ReferenceTable.HOSPITAL_CASH_COVER_CODE);
				coverCombo.setValue(correctSelectValue);
				sectionDetailsDTO.setCover(correctSelectValue);
			} else if(bean.getClaimDTO().getClaimCoverCode() != null) {
				SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(secCover.getItemIds(), bean.getClaimDTO().getClaimCoverCode());
				coverCombo.setValue(correctSelectValue);
				sectionDetailsDTO.setCover(correctSelectValue);
			}
			
			if((sectionCode.equalsIgnoreCase(ReferenceTable.HOSPITALIZATION_SECTION_CODE) || (sectionCode.equalsIgnoreCase(ReferenceTable.ASSISTED_REPRODUCTION_TREATEMENT_SECTION_CODE))) && ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				List<SelectValue> selectValueList =  secCover.getItemIds();
				SelectValue  correctSelectValue = value;
				if(selectValueList != null && !selectValueList.isEmpty() && selectValueList.size() > 1){
					coverCombo.setValue(selectValueList.get(1));
					value= selectValueList.get(1);
				}
				else if(selectValueList != null && !selectValueList.isEmpty() && selectValueList.size() > 0 && selectValueList.size() == 1){
					coverCombo.setValue(selectValueList.get(0));
					value= selectValueList.get(0);
				}
				else{
					sectionDetailsDTO.setCover(correctSelectValue);
				}
				
				if(bean.getClaimDTO().getClaimCoverCode() != null) {
					correctSelectValue = StarCommonUtils.getCorrectSelectValue(secCover.getItemIds(), bean.getClaimDTO().getClaimCoverCode());
					coverCombo.setValue(correctSelectValue);
					sectionDetailsDTO.setCover(correctSelectValue);
					value = correctSelectValue;
					
				}
			}
			
			if (value != null) {
				coverCombo.setValue(value);
			}
		}
		
		
	}
	
	@SuppressWarnings("unused")
	private void addCoverListener(final ComboBox coverCombo) {
		if (coverCombo != null) {
			coverCombo.addListener(new Listener() {
				private static final long serialVersionUID = 8513155091255795975L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					doCoverManipulations(component, true);

				}

			
			});
		}

	}
	
	
	/**
	 * Added for section based validation for comprehensive product/
	 * */
	@SuppressWarnings("unused")
	private void addSubCoverListener(final ComboBox subCoverCombo) {
		if (subCoverCombo != null) {
			subCoverCombo.addListener(new Listener() {
				private static final long serialVersionUID = 8513155091255795975L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					//doCoverManipulations(component);
					SelectValue value = (SelectValue) component.getValue();
					if (value != null && value.getId() != null && value.getCommonValue() != null) {
						bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().setSubCover(value);
						if(null != dummySubCoverField)
							dummySubCoverField.setValue(value.getCommonValue());
					}

				}

			
			});
		}

	}
	
	private void doCoverManipulations(ComboBox component, Boolean shouldinvoke) {
		
		SelectValue value = (SelectValue) component.getValue();
		SectionDetailsTableDTO sectionDetailsDTO = (SectionDetailsTableDTO) component
				.getData();
		HashMap<String, AbstractField<?>> hashMap = tableItem
				.get(sectionDetailsDTO);
		//ComboBox coverCombo = (ComboBox) hashMap.get("section");
		ComboBox subCoverCombo = (hashMap != null && hashMap.containsKey("subCover")) ? (ComboBox) hashMap.get("subCover") : null;
		if (value != null && value.getId() != null && value.getCommonValue() != null) {
			
			
			if(subCoverCombo!=null) { 
				
				addSubCover(value.getId(), subCoverCombo, sectionDetailsDTO.getSubCover(), value.getCommonValue(), sectionDetailsDTO);
				
			}
			
			
			// If Maternity is selected then we have to check
			// difference between insured entry age and doa should
			// be above 36 months. If all the conditions are not
			// satisfied, should not allow the user to proceed
			// further. This validation will be on only FLP AND PREAUTH screens
			if (value.getCommonValue()
					.equals(ReferenceTable.MATERNITY_COVER_CODE)) {
//				Integer differenceInMonths = SHAUtils.differenceInMonths(bean.getNewIntimationDTO().getInsuredPatient().getInsuredDateOfBirth(), bean.getPreauthDataExtractionDetails().getAdmissionDate());
//				if(differenceInMonths <= 36 && (presenterString.equalsIgnoreCase(SHAConstants.PRE_MEDICAL_PRE_AUTH) || presenterString.equalsIgnoreCase(SHAConstants.PRE_AUTH))) {
//					StarCommonUtils.alertMessage(getUI(), SHAConstants.MATERNITY_COVER_ALERT_MESSAGE);
//				}
				if (shouldinvoke && ReferenceTable.getSuperSurplusKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					StarCommonUtils.alertMessage(getUI(), SHAConstants.MATERNITY_COVER_ALERT_MESSAGE_FOR_SUPER_SURPLUS); 
				}else{
					if(shouldinvoke && !(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER) || 
							bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL)
						   || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_88_PRODUCT_FLOATER) || 
									bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_88_PRODUCT_INDIVIDUAL))) {
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MATERNITY_COVER_ALERT_MESSAGE); 
					}
					else{
						StarCommonUtils.alertMessage(getUI(), SHAConstants.MATERNITY_COVER_ALERT_MESSAGE_PRODUCT_78); 
					}
				}
				
				
			} else if (value.getCommonValue()
					.equals(ReferenceTable.NEW_BORN_COVER_CODE)) {
//				long daysBetweenDate = SHAUtils.getDaysBetweenDate(bean.getNewIntimationDTO().getInsuredPatient().getInsuredDateOfBirth(), bean.getPreauthDataExtractionDetails().getAdmissionDate());
//				if(daysBetweenDate >=1 && daysBetweenDate<=90 && (presenterString.equalsIgnoreCase(SHAConstants.PRE_MEDICAL_PRE_AUTH) || presenterString.equalsIgnoreCase(SHAConstants.PRE_AUTH))) {
//					StarCommonUtils.alertMessage(getUI(), SHAConstants.NEW_BORN_BABAY_MESSAGE);
//				}
				if(shouldinvoke) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.NEW_BORN_BABAY_MESSAGE);
				}
				
			} else if(value.getCommonValue()
					.equals(ReferenceTable.NEW_BORN_CHILD_VACCINATION_COVER_CODE)) {
				if(shouldinvoke) {
					StarCommonUtils.alertMessage(getUI(), SHAConstants.NEW_BORN_CHILD_VACCINATION_MESSAGE);
				}
			}
		} else {
			if(subCoverCombo != null) {
				subCoverCombo.setValue(null);
			}
		}
	}

	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
		secCover = coverContainer;
	}

	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {
		subCover = subCoverContainer;
	}
	
	public List<String> getErrors() {
		return this.errorMessages;
	}
	
	public boolean isValid(Boolean shouldCheckMandatoryValues) {
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<SectionDetailsTableDTO> itemIds = (Collection<SectionDetailsTableDTO>) table
				.getItemIds();
		if(shouldCheckMandatoryValues) {
			for (SectionDetailsTableDTO bean : itemIds) {
				Set<ConstraintViolation<SectionDetailsTableDTO>> validate = validator.validate(bean);
				if (validate.size() > 0) {
					hasError = true;
					for (ConstraintViolation<SectionDetailsTableDTO> constraintViolation : validate) {
						errorMessages.add(constraintViolation.getMessage());
					}
				}
			}
		}
		for (SectionDetailsTableDTO bean : itemIds) {
			if(bean.getSection() != null && bean.getSection().getCommonValue() != null && ReferenceTable
								.getComprehensiveProductSectionAlertMessage()
								.containsKey(bean.getSection().getCommonValue())) {
				if(!(bean.getSection().getCommonValue().equalsIgnoreCase(ReferenceTable.HOSPITAL_CASH_SECTION_CODE) && (this.bean.getAddOnBenefitsHospitalCash() != null && this.bean.getAddOnBenefitsHospitalCash()))) {
					hasError = true;
					errorMessages.add(ReferenceTable.getComprehensiveProductSectionAlertMessage().get(bean.getSection().getCommonValue()) + "</br> . Hence This record cannot be processed.");
				}
				
			} else if(bean.getCover() != null && bean.getCover().getCommonValue() != null && bean.getCover().getCommonValue().equalsIgnoreCase(ReferenceTable.NEW_BORN_CHILD_VACCINATION_COVER_CODE)) {
				hasError = true;
				errorMessages.add(SHAConstants.NEW_BORN_CHILD_VACCINATION_MESSAGE + "</br> . Hence This record cannot be processed.");
			}
		}
		if(!hasError) {
			for (SectionDetailsTableDTO bean : itemIds) {
				this.bean.getPreauthDataExtractionDetails().setSectionDetailsDTO(bean);
				ClaimDto claimDTO = this.bean.getClaimDTO();
				if(claimDTO != null) {
					claimDTO.setClaimCoverCode(bean.getCover() != null ? bean.getCover().getCommonValue() : null);
					claimDTO.setClaimSectionCode(bean.getSection() != null ? bean.getSection().getCommonValue() : null);
					claimDTO.setClaimSubCoverCode(bean.getSubCover() != null ? bean.getSubCover().getCommonValue() : null);
				}
				
				break;
			}
		}
		return !hasError;
	}
	
	public boolean isValid() {
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<SectionDetailsTableDTO> itemIds = (Collection<SectionDetailsTableDTO>) table
				.getItemIds();
			for (SectionDetailsTableDTO bean : itemIds) {
				Set<ConstraintViolation<SectionDetailsTableDTO>> validate = validator.validate(bean);
				if (validate.size() > 0) {
					hasError = true;
					for (ConstraintViolation<SectionDetailsTableDTO> constraintViolation : validate) {
						errorMessages.add(constraintViolation.getMessage());
					}
				}
			}
		if(!hasError) {
			for (SectionDetailsTableDTO bean : itemIds) {
				this.bean.getPreauthDataExtractionDetails().setSectionDetailsDTO(bean);
				ClaimDto claimDTO = this.bean.getClaimDTO();
				if(claimDTO != null) {
					claimDTO.setClaimCoverCode(bean.getCover() != null ? bean.getCover().getCommonValue() : null);
					claimDTO.setClaimSectionCode(bean.getSection() != null ? bean.getSection().getCommonValue() : null);
					claimDTO.setClaimSubCoverCode(bean.getSubCover() != null ? bean.getSubCover().getCommonValue() : null);
				}
				
				break;
			}
		}
		return !hasError;
	}
	
	@SuppressWarnings("unchecked")
	public String showAlertMessage() {
		Collection<SectionDetailsTableDTO> itemIds = (Collection<SectionDetailsTableDTO>) table
				.getItemIds();
		String message = null;
		for (SectionDetailsTableDTO bean : itemIds) {
			if(bean.getSection() != null && bean.getSection().getCommonValue() != null && bean.getSection().getCommonValue().equalsIgnoreCase(ReferenceTable.BARIATRIC_SECTION_CODE)) {
				String age = SHAUtils.getAge(this.bean.getNewIntimationDTO().getInsuredPatient().getInsuredDateOfBirth(),this.bean.getNewIntimationDTO().getAdmissionDate() );
				if(age != null && Integer.valueOf(age) < 18) {
					message = "Insured patient age is below 18years and not eligible to process the claim for Bariatric surgery";
					break;
				}
			}
		}
		
		return message;
	}
	
	private void doSectionManipulation(ComboBox component, Boolean shouldInvoke) {
//		ComboBox component = (ComboBox) event.getComponent();
		SelectValue value = (SelectValue) component.getValue();
		SectionDetailsTableDTO sectionDetailsDTO = (SectionDetailsTableDTO) component
				.getData();
		HashMap<String, AbstractField<?>> hashMap = tableItem
				.get(sectionDetailsDTO);
		ComboBox coverCombo = (hashMap != null && hashMap.containsKey("cover")) ? (ComboBox) hashMap.get("cover") : null;
		ComboBox subCoverCombo = (hashMap != null && hashMap.containsKey("subCover")) ? (ComboBox) hashMap.get("subCover") : null;
		
		if (value != null && value.getId() != null && value.getCommonValue() != null) {
			addSecCover(value.getId(), coverCombo, sectionDetailsDTO.getCover(), value.getCommonValue(), sectionDetailsDTO);
			
			if(value.getCommonValue().equalsIgnoreCase(ReferenceTable.BARIATRIC_SECTION_CODE)) {
				if(shouldInvoke) {
					StarCommonUtils.alertMessage(getUI(), "Waiting period of 36 months for this cover.");
				}
				
				String age = SHAUtils.getAge(bean.getNewIntimationDTO().getInsuredPatient().getInsuredDateOfBirth(),bean.getNewIntimationDTO().getAdmissionDate() );
				if(age != null && Integer.valueOf(age) < 18) {
					if(shouldInvoke) {
						StarCommonUtils.alertMessage(getUI(), "Insured patient age is below 18years and not eligible to process the claim for Bariatric surgery");
					}
					
				}
				
				bean.getPreauthDataExtractionDetails().setSectionDetailsDTO(getValue());
			} else if (ReferenceTable
					.getComprehensiveProductSectionAlertMessage()
					.containsKey(value.getCommonValue())) {
				StarCommonUtils
						.alertMessage(
								getUI(),
								ReferenceTable
										.getComprehensiveProductSectionAlertMessage()
										.get(value.getCommonValue()));
			}
			
			if(SHAConstants.PRE_MEDICAL_PRE_AUTH.equalsIgnoreCase(presenterString) || SHAConstants.PRE_AUTH.equalsIgnoreCase(presenterString) || 
					SHAConstants.PRE_MEDICAL_ENHANCEMENT.equalsIgnoreCase(presenterString) || SHAConstants.PRE_AUTH_ENHANCEMENT.equalsIgnoreCase(presenterString)) {
					
					if(value.getCommonValue().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)){
						if(shouldInvoke) {
						StarCommonUtils.alertMessage(getUI(), "Cashless not applicable for this section.");
						}
					}
					
			}
			else
			{
				if((null != bean.getClaimDTO().getNewIntimationDto().getPolicy() && 
						!(ReferenceTable.getRevisedCriticareProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
						&& !(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM)
								|| bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM))
						 && !bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.RAKSHAK_CORONA_PRODUCT_KEY)) &&
						value.getCommonValue().equalsIgnoreCase(ReferenceTable.LUMPSUM_SECTION_CODE)){
					if(shouldInvoke) {
						alertMessageForStarCancerGoldSection();
					}
				}
			}
			
			if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				
				if(SHAConstants.PRE_MEDICAL_PRE_AUTH.equalsIgnoreCase(presenterString) || SHAConstants.PRE_AUTH.equalsIgnoreCase(presenterString) || 
						SHAConstants.PRE_MEDICAL_ENHANCEMENT.equalsIgnoreCase(presenterString) || SHAConstants.PRE_AUTH_ENHANCEMENT.equalsIgnoreCase(presenterString) ||
						SHAConstants.ZONAL_REVIEW.equalsIgnoreCase(presenterString) || SHAConstants.CLAIM_REQUEST.equalsIgnoreCase(presenterString)){
					
				if(value.getCommonValue().equalsIgnoreCase(ReferenceTable.HOSPITALISATION_SURGICAL_SECTION_CODE)){
					if(shouldInvoke) {
						StarCommonUtils.alertMessage(getUI(), "One procedure to be selected.");
					}
				}
			}
		}
			 
		} else {
			if(coverCombo != null) {
				coverCombo.setValue(null);
			}
			
			if(subCoverCombo != null) {
				subCoverCombo.setValue(null);
			}
			
		}
	}

	@SuppressWarnings("unchecked")
	protected void enableLumpsumOrHospitalCash(String sectionCode) {
		for (SectionDetailsTableDTO sectionDetailsDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem
					.get(sectionDetailsDTO);

			ComboBox section = (ComboBox) combos.get("section");
			ComboBox cover = (ComboBox) combos.get("cover");
			ComboBox subCover = (ComboBox) combos.get("subCover");
			if(section != null && sectionCode != null) {
				BeanItemContainer<SelectValue> sectionDetails = (BeanItemContainer<SelectValue>) referenceData
						.get("sectionDetails");
				SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(sectionDetails.getItemIds(), sectionCode);
				section.setValue(correctSelectValue);
				sectionDetailsDTO.setSection(correctSelectValue);
				doSectionManipulation(section, false);
			}
		}
	}
	
	protected void enableOrDisable(GComboBox box) {
		
		Boolean isFirstROD = Boolean.FALSE;
		if(bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			if(ReferenceTable.getEnabledScreens().containsKey(presenterString)) {
				box.setEnabled(true);
			} else {
				box.setEnabled(false);
			}
			if(ReferenceTable.getCashlessNonEditableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())) {
				box.setEnabled(false);
			}
			
			if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				
				if(ReferenceTable.getEnabledSection().containsKey(presenterString)) {
					box.setEnabled(true);
				}
			}
			/*IMSSUPPOR-31512*/
			if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && ReferenceTable.HOSPITAL_CASH_POLICY.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())) {
				box.setEnabled(true);
			}
			
			if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && ReferenceTable.RAKSHAK_CORONA_PRODUCT_CODE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())) {
				box.setEnabled(true);
			}

			/*IMSSUPPOR-31614*/
			if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && ReferenceTable.getComprehensiveProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					&& presenterString.equalsIgnoreCase(SHAConstants.PRE_AUTH)) {
				box.setEnabled(true);
			}
		} else {
			
			if(ReferenceTable.getReimbursmentEditableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				if((presenterString.equalsIgnoreCase(SHAConstants.BILLING) || presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)) && 
						!ReferenceTable.isEditable().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
					box.setEnabled(false);
				}
				else
				{
					if((bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) || (bean.getIsQueryReceived() != null && bean.getIsQueryReceived())) {
						box.setEnabled(false);
					}
					else
					{
						box.setEnabled(true);
					}
				}
		   }
			// As discussed with Vijay,  the below 3 screens only Seciton Details will be non editable...
			if(presenterString.equalsIgnoreCase(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED)  || presenterString.equalsIgnoreCase(SHAConstants.CREATE_ROD)  || presenterString.equalsIgnoreCase(SHAConstants.BILL_ENTRY) ) {
				box.setEnabled(true);
				if(bean.getShouldDisableSection() || bean.getIsReconsiderationRequest()) {
					box.setEnabled(false);
				}
			} else {
				if(bean.getRodNumber() != null ) {
					String[] split = bean.getRodNumber().split("/");
					String string = split[split.length - 1];
					if(string != null && Integer.valueOf(string) == 1) {
						
						isFirstROD = Boolean.TRUE;
						box.setEnabled(true);
						if(!ReferenceTable.isEditable().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
							box.setEnabled(false);
							if(ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && presenterString.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)) {
								box.setEnabled(true);
							}
							
						}
					} 
				}
				if(!isFirstROD && (presenterString.equalsIgnoreCase(SHAConstants.BILLING) || presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)) && !ReferenceTable.isEditable().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
					box.setEnabled(false);
				}
				
				if(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag() && !(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
						|| ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
					box.setEnabled(false);
				}
				if((ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) || ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))&& 
						(presenterString.equalsIgnoreCase(SHAConstants.BILLING) || presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL))){
					box.setEnabled(false);
				}
				
				if(bean.getRodNumber() != null ) {
					String[] split = bean.getRodNumber().split("/");
					String string = split[split.length - 1];
					if(string != null && !Integer.valueOf(string).equals(1)) {
						box.setEnabled(false);
					} 
				}
				
				
				if((bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) || (bean.getIsQueryReceived() != null && bean.getIsQueryReceived())) {
					box.setEnabled(false);
				}
				
				if(ReferenceTable.getReimbursmentEditableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					if((presenterString.equalsIgnoreCase(SHAConstants.BILLING) || presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)) && 
							!ReferenceTable.isEditable().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
						box.setEnabled(false);
					}
					else
					{
						if((bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) || (bean.getIsQueryReceived() != null && bean.getIsQueryReceived())) {
							box.setEnabled(false);
						}
						else
						{
							box.setEnabled(true);
							
							if(bean.getRodNumber() != null ) {
								String[] split = bean.getRodNumber().split("/");
								String string = split[split.length - 1];
								if(string != null && !Integer.valueOf(string).equals(1)) {
									box.setEnabled(false);
								} 
							}
						}
					}
			   }
				
			}
		}	
						
			
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void enableDisable(Boolean shouldEnable) {
		Collection<SectionDetailsTableDTO> itemIds = (Collection<SectionDetailsTableDTO>) table
				.getItemIds();
		for (SectionDetailsTableDTO sectionDetailsDTO : itemIds) {
			HashMap<String, AbstractField<?>> combos = tableItem
					.get(sectionDetailsDTO);
			if(combos != null && !combos.isEmpty()) {
				final ComboBox sectionCombo = (ComboBox) combos.get("section");
				final ComboBox coverCombo = (ComboBox) combos.get("cover");
				final ComboBox subCoverCombo = (ComboBox) combos.get("subCover");
				if(sectionCombo != null && coverCombo != null && subCoverCombo != null) {
					// As discussed with Vijay,  the below 3 screens only Seciton Details will be non editable...
					if(presenterString.equalsIgnoreCase(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED)  || presenterString.equalsIgnoreCase(SHAConstants.CREATE_ROD)  || presenterString.equalsIgnoreCase(SHAConstants.BILL_ENTRY) ) {
						sectionCombo.setEnabled(true);
						coverCombo.setEnabled(true);
						subCoverCombo.setEnabled(true);
						if(bean.getShouldDisableSection() || bean.getIsReconsiderationRequest()) {
							sectionCombo.setEnabled(false);
							coverCombo.setEnabled(false);
							subCoverCombo.setEnabled(false);
						}
					} else {
						if(bean.getRodNumber() != null ) {
							String[] split = bean.getRodNumber().split("/");
							String string = split[split.length - 1];
							if(string != null && Integer.valueOf(string) == 1) {
								sectionCombo.setEnabled(false);
								coverCombo.setEnabled(false);
								subCoverCombo.setEnabled(false);
								if(!ReferenceTable.isEditable().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
									sectionCombo.setEnabled(false);
									coverCombo.setEnabled(false);
									subCoverCombo.setEnabled(false);
								}
								if(ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && presenterString.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)) {
									sectionCombo.setEnabled(true);
									coverCombo.setEnabled(true);
									subCoverCombo.setEnabled(true);
								}
							} 
						}
						if((presenterString.equalsIgnoreCase(SHAConstants.BILLING) || presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)) && !ReferenceTable.isEditable().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ) {
							sectionCombo.setEnabled(false);
							coverCombo.setEnabled(false);
							subCoverCombo.setEnabled(false);
						}
						if(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) {
							sectionCombo.setEnabled(false);
							coverCombo.setEnabled(false);
							subCoverCombo.setEnabled(false);
						}
						
						if(bean.getRodNumber() != null ) {
							String[] split = bean.getRodNumber().split("/");
							String string = split[split.length - 1];
							if(string != null && !Integer.valueOf(string).equals(1)) {
								sectionCombo.setEnabled(false);
								coverCombo.setEnabled(false);
								subCoverCombo.setEnabled(false);
							} 
						}
						if(bean.getIsReconsiderationRequest() != null && bean.getIsReconsiderationRequest()) {
							sectionCombo.setEnabled(false);
							coverCombo.setEnabled(false);
							subCoverCombo.setEnabled(false);;
						}
					}
					
					if(sectionCombo.isEnabled()) {
						sectionCombo.setEnabled(shouldEnable);
						coverCombo.setEnabled(shouldEnable);
						subCoverCombo.setEnabled(shouldEnable);;
					}
				}
			}
			
			}
			
		
	}
	
	public String getSubCoverFieldValue()
	{
		if(null != dummySubCoverField)
		{
			return dummySubCoverField.getValue();
		}
		return null;
	}
 public Boolean alertMessageForStarCancerGoldSection() {
		 
		 String msg = "";
		 
		 final Integer diffDays;
		 
		 if(presenterString.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST) || presenterString.equalsIgnoreCase(SHAConstants.ZONAL_REVIEW)){
	
			 //diffDays = SHAUtils.differenceInMonths(bean.getNewIntimationDTO().getPolicy().getPolicyFromDate(),bean.getPreauthDataExtractionDetails().getAdmissionDate());
			 //IMSSUPPOR-32538
			 diffDays = SHAUtils.differenceInMonths(bean.getNewIntimationDTO().getPolicyInceptionDate() != null ? 
					 bean.getNewIntimationDTO().getPolicyInceptionDate() : bean.getNewIntimationDTO().getPolicy().getPolicyFromDate(),bean.getPreauthDataExtractionDetails().getAdmissionDate());
			
			if(diffDays <= 30){
				
				msg = "Waiting period  30 months  or below"; 
			}
			else
			{
				msg = "Whether the signs and symptoms of cancer detected after  30 months  of inception date of policy.<br>"
						+ "<Center> Do you want to Proceed ?</center>"; 
		  
			}
		 
	   		/*Label successLabel = new Label(
					"<b style = 'color: red;'>" + msg  + "</b>",
					ContentMode.HTML);
	   		//final Boolean isClicked = false;
			Button okButton = new Button("Ok");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			Button cancelButton = new Button("Cancel");
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			
			HorizontalLayout hLayout = new HorizontalLayout();
			hLayout.addComponents(okButton,cancelButton);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setSpacing(true);
			
			VerticalLayout layout = new VerticalLayout();
			if(diffDays <= 30){
				layout.addComponents(successLabel, okButton);
				layout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			}
			else
			{
				layout.addComponents(successLabel, hLayout);
				layout.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
			}
			
			layout.setStyleName("borderLayout");
			layout.setSpacing(true); 
			layout.setMargin(true);				

			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
/*			buttonsNamewithType.put(GalaxyButtonTypesEnum.CANCEL.toString(), "Cancel")*/;
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox(msg  + "</b>", buttonsNamewithType);
			Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
		/*	Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.CANCEL
					.toString());*/
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;  

				@Override
				public void buttonClick(ClickEvent event) {
					
					if(diffDays <= 30){
						bean.setIsWaitingDaysLessThan30(Boolean.TRUE);
					}else{
						bean.setIsWaitingDaysLessThan30(Boolean.FALSE);
					}
					
					//dialog.close();						
				}
			});
			// As Raja sir confirm Cancel Removed From Alert
			/*cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					bean.setIsWaitingDaysLessThan30(Boolean.TRUE);
					//dialog.close();						
				}
			});*/
		 }
			return true;
		 }
	
}
