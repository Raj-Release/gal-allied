package com.shaic.claim.premedical.listenerTables;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.ClaimRequestDataExtractionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.MedicalApprovalDataExtractionPagePresenter;
import com.shaic.domain.IntimationService;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.converter.StringToIntegerConverter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class UpdateClaimDetailListenerTable extends ViewComponent {
	private static final long serialVersionUID = 4809460534159116589L;
	
	private Map<UpdateOtherClaimDetailDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<UpdateOtherClaimDetailDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<UpdateOtherClaimDetailDTO> data = new BeanItemContainer<UpdateOtherClaimDetailDTO>(UpdateOtherClaimDetailDTO.class);

	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private String hospitalCode;
	
	private String packageRateValue;
	
	private String dayCareFlagValue;
	
	private String procedureCodeValue;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private String presenterString;
	
	public TextField dummyField;
	
	private PreauthDTO bean;
	
	public List<UpdateOtherClaimDetailDTO> deletedDTO;
	
	@Inject
	IntimationService intimationService;
	
	//private List<DiagnosisDetailsTableDTO> diagnosisList;
	//public List<String> diagnosisList = new ArrayList<String>();
	public void init( PreauthDTO bean) {
		deletedDTO = new ArrayList<UpdateOtherClaimDetailDTO>();
		this.bean = bean;
		//this.diagnosisList = diagnosisList;
	//	diagnosisList = new ArrayList<String>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
//		this.hospitalKey = hospitalKey;
		//this.hospitalCode = hosptialCode;
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
//		TextField label = new TextField("Insured Name");
//		label.addStyleName("select-option");
//		label.setValue(bean.getPolicyDto().getInsuredFirstName());
//		label.setEnabled(false);
//		label.setNullRepresentation("");
		//TextField label1 = new TextField();
		
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(btnLayout);
		layout.setMargin(true);
		
		initTable(layout);
		table.setWidth("100%");
		//table.setHeight("30%");
		/**
		 * Height is set for table visiblity.
		 * */
		table.setHeight("160px");
		table.setPageLength(table.getItemIds().size());
		
		addListener();
		
		layout.addComponent(table);
		
		if(dummyField == null){
			dummyField = new TextField();
		}

		setCompositionRoot(layout);
	}


	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				UpdateOtherClaimDetailDTO updateClaimDetailDTO = new UpdateOtherClaimDetailDTO();
				updateClaimDetailDTO.setEnableOrDisable(true);
				BeanItem<UpdateOtherClaimDetailDTO> addItem = data.addItem(updateClaimDetailDTO);
			}
		});
	}
	
	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("Update Previous/Other Claim Details(Defined Limit)", data);
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button deleteButton = new Button("Delete");
		    	deleteButton.setData(itemId);
		    	UpdateOtherClaimDetailDTO dto = (UpdateOtherClaimDetailDTO) itemId;
		    	if(dto.getEnableOrDisable() != null) {
		    		deleteButton.setEnabled(dto.getEnableOrDisable());
				}
		    	
		    	
		    	
		    	deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						
						final UpdateOtherClaimDetailDTO currentItemId = (UpdateOtherClaimDetailDTO) event.getButton().getData();
//						if (table.getItemIds().size() >= 1) {
							
							/*ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {

												public void onClose(ConfirmDialog dialog) {
													if (!dialog.isConfirmed()) {
														// Confirmed to continue
														UpdateOtherClaimDetailDTO dto =  (UpdateOtherClaimDetailDTO)currentItemId;
														if(dto.getKey() != null && dto.getDiagnosis() != null && dto.getDiagnosis().length() > 0) {
															deletedDTO.add((UpdateOtherClaimDetailDTO)currentItemId);
														}
														table.removeItem(currentItemId);
													} else {
														// User did not confirm
													}
												}
											});
							dialog.setClosable(false);*/
							
							
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
							buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
							HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
									.createConfirmationbox("Do you want to Delete ?", buttonsNamewithType);
							Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
									.toString());
							Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
									.toString());
							yesButton.addClickListener(new ClickListener() {
								private static final long serialVersionUID = 7396240433865727954L;

								@Override
								public void buttonClick(ClickEvent event) {
									UpdateOtherClaimDetailDTO dto =  (UpdateOtherClaimDetailDTO)currentItemId;
									if(dto.getKey() != null && dto.getDiagnosis() != null && dto.getDiagnosis().length() > 0) {
										deletedDTO.add((UpdateOtherClaimDetailDTO)currentItemId);
									}
									table.removeItem(currentItemId);
								}
								});
							noButton.addClickListener(new ClickListener() {
								private static final long serialVersionUID = 7396240433865727954L;

								@Override
								public void buttonClick(ClickEvent event) {
									
								}
								});
//						} 
						
			        } 
			    });
		    	//deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		        return deleteButton;
		      }
		    });
		
		table.setVisibleColumns(new Object[] { "serialNumber", "intimationNo", "insurerName", "primaryProcedure", "icdChaper", "icdBlock", "icdCode", "claimAmount", "deductibles","admissibleAmount", "remarks" , "Delete"});

		table.setColumnHeader("intimationNo", "Intimation No");
		table.setColumnHeader("insurerName", "Insurer Name");
		table.setColumnHeader("primaryProcedure", "Primary Diagnosis/Procedure");
		table.setColumnHeader("icdChaper", "ICD Chapter");
		table.setColumnHeader("icdBlock", "ICD Block");
		table.setColumnHeader("icdCode", "ICD Code");
		table.setColumnHeader("claimAmount", "Claim Amount");
		table.setColumnHeader("deductibles", "Deductibles");
		table.setColumnHeader("admissibleAmount", "Paid / Admissible Amount");
		table.setColumnHeader("remarks", "Remarks");
		table.setColumnHeader("serialNumber", "S.No");
		table.setEditable(true);
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
		table.setFooterVisible(true);
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			UpdateOtherClaimDetailDTO updateClaimDetailDTO = (UpdateOtherClaimDetailDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			Boolean isEnabled =  updateClaimDetailDTO.getEnableOrDisable() ? true : false;
			
			/*Boolean isEnabled = (null != procedureDTO && null != procedureDTO.getRecTypeFlag() && procedureDTO.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c") ) ? false: true;
			if(!"premedicalEnhancement".equalsIgnoreCase(presenterString) && !isEnabled) {
				isEnabled = true;
			}*/
			if (tableItem.get(updateClaimDetailDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(updateClaimDetailDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(updateClaimDetailDTO);
			}
			
			 if("intimationNo".equals(propertyId)) {
				 if(updateClaimDetailDTO.getEnableOrDisable()){
					TextField field = new TextField();
					field.setNullRepresentation("");
					field.setWidth("200px");
					field.setNullRepresentation("");
					tableRow.put("intimationNo", field);
					
					
					final TextField txt = (TextField) tableRow.get("serialNumber");
					generateSlNo(txt);
					
					return field;
				 }else{
					 return returnDefaultField(container, itemId, propertyId,
							uiContext);
				 }
			}
			 
			 else  if("insurerName".equals(propertyId)) {
				 if(updateClaimDetailDTO.getEnableOrDisable()){
					TextField field = new TextField();
					field.setNullRepresentation("");
					field.setWidth("200px");
					field.setNullRepresentation("");
					tableRow.put("insurerName", field);
					updateClaimDetailDTO.setEditFlag(true);
					return field;
				 }else{
					 return returnDefaultField(container, itemId, propertyId,
								uiContext);
				 }
				}
			 else  if("primaryProcedure".equals(propertyId)) {
				 if(updateClaimDetailDTO.getEnableOrDisable()){
					TextField field = new TextField();
					field.setNullRepresentation("");
					field.setWidth("200px");
					field.setNullRepresentation("");
					tableRow.put("primaryProcedure", field);
					updateClaimDetailDTO.setEditFlag(true);
					return field;
				 }else{
					 return returnDefaultField(container, itemId, propertyId,
								uiContext);
				 }
			 }
			 
			 else  if("icdChaper".equals(propertyId)) {
				 if(updateClaimDetailDTO.getEnableOrDisable()){
					TextField field = new TextField();
					field.setNullRepresentation("");
					field.setWidth("200px");
					field.setNullRepresentation("");
					tableRow.put("icdChaper", field);
					updateClaimDetailDTO.setEditFlag(true);
					return field;
				 }else{
					 return returnDefaultField(container, itemId, propertyId,
								uiContext);
				 }
				}
			 
			 else  if("icdBlock".equals(propertyId)) {
				 if(updateClaimDetailDTO.getEnableOrDisable()){
					TextField field = new TextField();
					field.setNullRepresentation("");
					field.setWidth("200px");
					field.setNullRepresentation("");
					tableRow.put("icdBlock", field);
					updateClaimDetailDTO.setEditFlag(true);
					return field;
				 }else{
					 return returnDefaultField(container, itemId, propertyId,
								uiContext);
				 }
				}
			 
			 else  if("icdCode".equals(propertyId)) {
				 if(updateClaimDetailDTO.getEnableOrDisable()){
					TextField field = new TextField();
					field.setNullRepresentation("");
					field.setWidth("200px");
					field.setNullRepresentation("");
					tableRow.put("icdCode", field);
					updateClaimDetailDTO.setEditFlag(true);
					return field;
				 }else{
					 return returnDefaultField(container, itemId, propertyId,
								uiContext);
				 }
				}
			else if("claimAmount".equals(propertyId)) {
				if(updateClaimDetailDTO.getEnableOrDisable()){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setData(updateClaimDetailDTO);
				
				CSValidator validator = new CSValidator();
				validator.extend(field);
				field.setConverter(plainIntegerConverter);
				validator.setRegExp("^[0-9 ]*$");	
				validator.setPreventInvalidTyping(true);
				
				
				tableRow.put("claimAmount", field);
				updateClaimDetailDTO.setEditFlag(true);
				field.addBlurListener(getAmountConsiderListener());
				
				return field;
				}else{
					 return returnDefaultField(container, itemId, propertyId,
								uiContext);
				 }
			} else if("deductibles".equals(propertyId)) {
				if(updateClaimDetailDTO.getEnableOrDisable()){
				TextField field = new TextField();
				field.setEnabled(isEnabled);
				field.setNullRepresentation("");
				field.setData(updateClaimDetailDTO);
				field.setWidth("200px");
				field.setMaxLength(100);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				field.setConverter(plainIntegerConverter);
				validator.setRegExp("^[0-9 ]*$");	
				validator.setPreventInvalidTyping(true);
				tableRow.put("deductibles", field);
				updateClaimDetailDTO.setEditFlag(true);
				field.addBlurListener(getAmountConsiderListener());
				return field;
				}else{
					 return returnDefaultField(container, itemId, propertyId,
								uiContext);
				 }
			}  else if("admissibleAmount".equals(propertyId)) {
				if(updateClaimDetailDTO.getEnableOrDisable()){
				TextField field = new TextField();
				field.setEnabled(isEnabled);
				field.setNullRepresentation("");
				field.setWidth("200px");
				field.setMaxLength(100);
				field.setEnabled(false);
				field.setData(updateClaimDetailDTO);
				
				CSValidator validator = new CSValidator();
				validator.extend(field);
				field.setConverter(plainIntegerConverter);
				validator.setRegExp("^[0-9 ]*$");	
				validator.setPreventInvalidTyping(true);
				
				tableRow.put("admissibleAmount", field);
				
				updateClaimDetailDTO.setEditFlag(true);
				field.addBlurListener(getAmountConsiderListener());
				return field;
				}else{
					 return returnDefaultField(container, itemId, propertyId,
								uiContext);
				 }
			}else if("remarks".equals(propertyId)) {
				subTotalForFooter();
				if(updateClaimDetailDTO.getEnableOrDisable()){
				TextField field = new TextField();
				field.setEnabled(isEnabled);
				field.setNullRepresentation("");
				field.setWidth("200px");
				field.setMaxLength(100);
//				CSValidator validator = new CSValidator();				
//				validator.extend(field);
//				validator.setRegExp("^[a-zA-Z 0-9/]*$");
//				validator.setPreventInvalidTyping(true);
				tableRow.put("remarks", field);
				updateClaimDetailDTO.setEditFlag(true);
				return field;
				}else if ("serialNumber".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("50px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setMaxLength(6);
					field.setData(updateClaimDetailDTO);
				//	field.setConverter(plainIntegerConverter);
					//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
					//field.setValue(String.valueOf(entryDTO.getItemNo()));
					CSValidator validator = new CSValidator();
					validator.extend(field);
					validator.setRegExp("^[0-9 ,]*$");
					validator.setPreventInvalidTyping(true);
					tableRow.put("serialNumber", field);

					return field;
				}else{
					 return returnDefaultField(container, itemId, propertyId,
								uiContext);
				 }
			}
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setEnabled(isEnabled);
					field.setWidth("100%");
					
				subTotalForFooter();
				return field;
			}
		}

		private Field<?> returnDefaultField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField){
					((TextField) field).setNullRepresentation("");
					TextField txtField = (TextField)field;
					txtField.setDescription(txtField.getValue());
					}
					field.setReadOnly(true);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setWidth("100%");
					
					
				return field;
		}

	}
	
	public BlurListener getAmountConsiderListener() {
		
		BlurListener listener = new BlurListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField component = (TextField) event.getComponent();
				
				UpdateOtherClaimDetailDTO otherclaimdetailsDto = (UpdateOtherClaimDetailDTO)component.getData();
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(otherclaimdetailsDto);
				TextField txtClaimedAmt = (TextField)hashMap.get("claimAmount");
				TextField txtNonPayableAmt = (TextField)hashMap.get("deductibles");
				TextField txtApprovedAmout = (TextField)hashMap.get("admissibleAmount");
				
				Integer claimedAmount = 0;
				Integer nonPayableAmt = 0;
				Integer payableAmount = 0;
				if(txtClaimedAmt != null && txtClaimedAmt.getValue() != null){
					claimedAmount = SHAUtils.getIntegerFromStringWithComma(txtClaimedAmt.getValue());
				}
				
				if(txtNonPayableAmt != null && txtNonPayableAmt.getValue() != null){
					nonPayableAmt = SHAUtils.getIntegerFromStringWithComma(txtNonPayableAmt.getValue());
				}
				
				if(txtApprovedAmout != null){
					payableAmount = claimedAmount - nonPayableAmt > 0 ? claimedAmount - nonPayableAmt : 0 ;
					txtApprovedAmout.setValue(payableAmount.toString());
				}
				subTotalForFooter();

			}
		};
		return listener;
		
	}
	
	StringToIntegerConverter plainIntegerConverter = new StringToIntegerConverter() {
		private static final long serialVersionUID = -2154393632039317675L;

		protected java.text.NumberFormat getFormat(Locale locale) {
	        NumberFormat format = super.getFormat(locale);
	        format.setGroupingUsed(false);
	        return format;
	    };
	};
	
	public void subTotalForFooter(){
		
		Long claimedAmount = 0l;
		Long deductibleAmount = 0l;
		Long admissibleAmt = 0l;
		
		List<UpdateOtherClaimDetailDTO> totalList = (List<UpdateOtherClaimDetailDTO>) table.getItemIds();
        for (UpdateOtherClaimDetailDTO updateOtherClaimDetailDTO : totalList) {
			Long claimed = updateOtherClaimDetailDTO.getClaimAmount() != null ? updateOtherClaimDetailDTO.getClaimAmount() :0l;
			claimedAmount+= claimed;
			Long deducted = updateOtherClaimDetailDTO.getDeductibles() != null ? updateOtherClaimDetailDTO.getDeductibles() : 0l;
			deductibleAmount += deducted;
			Long approved = updateOtherClaimDetailDTO.getAdmissibleAmount() != null ? updateOtherClaimDetailDTO.getAdmissibleAmount() :0l;
			admissibleAmt += approved;

		}
        
        table.setColumnFooter("claimAmount", String.valueOf(claimedAmount));
        table.setColumnFooter("deductibles", String.valueOf(deductibleAmount));
        table.setColumnFooter("admissibleAmount", String.valueOf(admissibleAmt));
        
        this.bean.setAlreadySettlementAmt(String.valueOf(admissibleAmt));
        dummyField.setValue(String.valueOf(admissibleAmt));
        

	}
	
	public Integer getTotalClaimedAmount(){
		return SHAUtils.getIntegerFromString(this.table.getColumnFooter("claimAmount")) ;
	}
	
	public Integer getTotalDeductibleAmount(){
		return SHAUtils.getIntegerFromString(this.table.getColumnFooter("deductibles")) ;
	}
	
	public Integer getTotalApprovedAmount(){
		return SHAUtils.getIntegerFromString(this.table.getColumnFooter("admissibleAmount")) ;
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	 public List<UpdateOtherClaimDetailDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<UpdateOtherClaimDetailDTO> itemIds = (List<UpdateOtherClaimDetailDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	
	public void addBeanToList(UpdateOtherClaimDetailDTO updateClaimDetailDTO) {
    	data.addItem(updateClaimDetailDTO);
    }
	

	private void setPackageRateAndDayCareProcedure(Long procedureKey,String procedureCode,TextField packageRate, TextField dayCareProcedure, ComboBox procedureCodeCombo) {
		if("premedicalPreauth".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PreMedicalPreauthWizardPresenter.PREMEDICAL_GET_PACKAGE_RATE,procedureKey,hospitalCode,bean);
		} else if("premedicalEnhancement".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_GET_PACKAGE_RATE,procedureKey,hospitalCode,bean);
		} else if("preauthEnhancement".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_GET_PACKAGE_RATE,procedureKey,hospitalCode,bean);
		}else if (SHAConstants.MEDICAL_APPROVAL_DATA_EXTRACTION.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(MedicalApprovalDataExtractionPagePresenter.GET_PACKAGE_RATE,procedureKey,hospitalCode,bean);
		} else if (SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(ClaimRequestDataExtractionPagePresenter.GET_PACKAGE_RATE,procedureKey,hospitalCode,bean);
		} else if(SHAConstants.PRE_AUTH.equalsIgnoreCase(this.presenterString)) {
			if(procedureKey != null) {
				fireViewEvent(PreauthWizardPresenter.PREAUTH_GET_PACKAGE_RATE, procedureKey,hospitalCode,bean);
			}
		}
		if(packageRate != null) {
			packageRate.setReadOnly(false);
			packageRate.setValue(packageRateValue);
			packageRate.setReadOnly(true);
		}
		if(dayCareProcedure != null) {
			dayCareProcedure.setReadOnly(false);
			dayCareProcedure.setValue(dayCareFlagValue);
			dayCareProcedure.setReadOnly(true);
		}
		
		
		if(procedureCodeCombo != null) {
			SelectValue value = new SelectValue();
			value.setId(procedureKey);
			value.setValue(procedureCodeValue);
			procedureCodeCombo.setValue(value);
		}
		
	}
	
	public boolean isValid()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<UpdateOtherClaimDetailDTO> itemIds = (Collection<UpdateOtherClaimDetailDTO>) table.getItemIds();
		Map<Long, String> valuesMap = new HashMap<Long, String>();
		Map<Long, String> validationMap = new HashMap<Long, String>();
/*		for (UpdateClaimDetailDTO bean : itemIds) {
			
			if(bean.getSublimitApplicable() != null && bean.getSublimitApplicable().getId().equals(ReferenceTable.COMMONMASTER_YES) && bean.getSublimitName() == null) {
				hasError = true;
				errorMessages.add("Please Select Sublimit Name.");
			}
			
			if(bean.getSublimitApplicable() == null) {
				hasError = true;
				errorMessages.add("Please Select Sublimit Applicable.");
			}
			
			if(bean.getConsiderForPayment() == null) {
				hasError = true;
				errorMessages.add("Please Select Consider For Payment.");
			}
			
			if(bean.getProcedureName() == null || bean.getProcedureCode() == null) {
				hasError = true;
				errorMessages.add("Please choose Procedure Name or Procedure Code.");
			}
			
			

			if(bean.getSublimitName() != null) {
				if(valuesMap.containsKey(bean.getSublimitName().getLimitId()) && (bean.getConsiderForPayment() == null || (null != bean.getConsiderForPayment() && bean.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)))) {
					validationMap.put(bean.getSublimitName().getLimitId(), bean.getSublimitName().getLimitId().toString());
				} else {
					valuesMap.put(bean.getSublimitName().getLimitId(), bean.getSublimitName().getLimitId().toString());
				}
			}
			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<ProcedureDTO> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}*/
		
		for (UpdateOtherClaimDetailDTO bean : itemIds) {
		
			Set<ConstraintViolation<UpdateOtherClaimDetailDTO>> validate = validator.validate(bean);
			
			
			if(bean.getClaimAmount() == null){
				hasError = true;
				errorMessages.add("Please Enter Claimed Amount");
			}
			
			if(bean.getAdmissibleAmount() == null){
				hasError = true;
				errorMessages.add("Please Enter Admissible Amount");
			}
			
			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<UpdateOtherClaimDetailDTO> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}
		
		return !hasError;
	}
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	
	public void enableOrDisableDeleteButton(final Boolean isEnable) {
		
	}
	
	public void removeRow() {
		table.removeAllItems();

	}
	
	private void generateSlNo(TextField txtField)
	{
		
		Collection<UpdateOtherClaimDetailDTO> itemIds = (Collection<UpdateOtherClaimDetailDTO>) table.getItemIds();
		
		int i = 0;
		 for (UpdateOtherClaimDetailDTO updateOtherClaimDetailDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(updateOtherClaimDetailDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("serialNumber");
				 if(null != itemNoFld)
				 {
					 itemNoFld.setValue(String.valueOf(i)); 
					 itemNoFld.setEnabled(false);
				 }
			 }
		 }
		
	}

	
	
}
