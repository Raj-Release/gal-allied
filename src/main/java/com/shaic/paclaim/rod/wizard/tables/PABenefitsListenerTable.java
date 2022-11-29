/**
 * 
 */
package com.shaic.paclaim.rod.wizard.tables;

/**
 * @author ntv.vijayar
 *
 */



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.PAClaimRequestDataExtractionPagePresenter;
import com.shaic.paclaim.rod.wizard.dto.PABenefitsDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class PABenefitsListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<PABenefitsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<PABenefitsDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<PABenefitsDTO> container = new BeanItemContainer<PABenefitsDTO>(PABenefitsDTO.class);
	
	private Table table;

	
	private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private Button btnAdd;
	
	private Button btnDelete;
	
	private List<PABenefitsDTO> deletedList;
	
	private Long benefitsId;
	
	private PABenefitsDTO paBenefitsDTO ;
	List<PABenefitsDTO> paBenefitsList ;
	
//	private ReceiptOfDocumentsDTO bean;
	
	public void initReferenceId (Long benefitsId)
	{
		this.benefitsId = benefitsId;
	}
	public void init() {
		//container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		
		this.errorMessages = new ArrayList<String>();
		deletedList = new ArrayList<PABenefitsDTO>();
		
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));

		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.BOTTOM_RIGHT);
	//	btnLayout.setComponentAlignment(btnDelete, Alignment.MIDDLE_RIGHT);

		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(btnLayout);
		
		initTable();
		table.removeAllItems();
	//	table.setWidth("100%");
		table.setHeight("100%");
		table.setPageLength(table.getItemIds().size());
		
		addListener();
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}

	
	public void setTableList(final List<PABenefitsDTO> list) {
		table.removeAllItems();
		for (final PABenefitsDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		//container.removeAllItems();
		table.addStyleName("generateColumnTable");
	//	table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		if(null != benefitsId && benefitsId.equals(ReferenceTable.BENEFITS_TTD_MASTER_VALUE))
			table.setVisibleColumns(new Object[] { "benefitCoverValue","noOfWeeks","sumInsured","eligibleAmountPerWeek","totalEligibleAmount"});
		else
			table.setVisibleColumns(new Object[] { "benefitCoverValue","percentage","sumInsured","eligibleAmount"});
		table.setColumnHeader("benefitCoverValue","Benefit / Cover");
		table.setColumnHeader("percentage", "Percentage (%)");
		table.setColumnHeader("sumInsured", "Sum Insured");
		table.setColumnHeader("eligibleAmount", "Eligible Amount");
		table.setColumnHeader("eligibleAmountPerWeek", "Eligible Amount Per Week");
		table.setColumnHeader("noOfWeeks", "No. of Weeks");
		table.setColumnHeader("totalEligibleAmount", "Total Eligible Amt");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				//final Button deleteButton = new Button("Delete");
				final Button deleteButton = new Button();
				//deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				deleteButton.setIcon(FontAwesome.TRASH_O);
				deleteButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				deleteButton.setWidth("-1px");
				deleteButton.setHeight("-10px");
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						final Object currentItemId = event.getButton().getData();
						//PABenefitsDTO billEntryDetailsDTO = (PABenefitsDTO) currentItemId;
						//deletedList.add(billEntryDetailsDTO);
						/*if (table.getItemIds().size() > 1)*/ {
							
							ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {

												public void onClose(ConfirmDialog dialog) {
													if (!dialog.isConfirmed()) {
														// Confirmed to continue
														PABenefitsDTO billEntryDetailsDTO = (PABenefitsDTO) currentItemId;
														//if(billEntryDetailsDTO.getKey() != null && dto.getDiagnosis() != null && dto.getDiagnosis().length() > 0) {
															deletedList.add(billEntryDetailsDTO);
															//deletedDTO.add((DiagnosisDetailsTableDTO)currentItemId);
														//}
														table.removeItem(currentItemId);
													} else {
														// User did not confirm
													}
												}
											});
							dialog.setClosable(false);
							
						} /*else {
							HorizontalLayout layout = new HorizontalLayout(
									new Label("One Benefits is Mandatory."));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
						}*/
					}
				});

				return deleteButton;
			}
		});


		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());
	}
	
	public void removeAllItems()
	{
		table.removeAllItems();
		//table.removeItem(\);
	}
	
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	public void setTableData(List<PABenefitsDTO> paBenefitsList)
	{
		this.paBenefitsList = paBenefitsList;
	}
	
	
	private void addBenefitCoverContainerValue(GComboBox cmbBox)
	{
		BeanItemContainer<SelectValue> benefitContainer = (BeanItemContainer<SelectValue>) referenceData.get("coverContainerValue");
		cmbBox.setContainerDataSource(benefitContainer);
		cmbBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbBox.setItemCaptionPropertyId("value");
	}
	
	private void addListener() {
		

		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
		    public void buttonClick(ClickEvent event) {
				PABenefitsDTO paBenefitDTO = new PABenefitsDTO();	
				BeanItem<PABenefitsDTO> addItem = container.addItem(paBenefitDTO);
				table.setPageLength(2);
				
			}
		});
		
	/*	btnDelete.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				List<PABenefitsDTO> paCoversDTO = getValues();
				if(null != paCoversDTO && !paCoversDTO.isEmpty())
				{
					int iSize = paCoversDTO.size();
					table.removeItem(paCoversDTO.get(iSize - 1));
				}
			}
		});*/
	
	}
	
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
/**
		 * 
		 */
		private static final long serialVersionUID = -8967055486309269929L;

		/*		private static final long serialVersionUID = -2192723245525925990L;
*/
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			PABenefitsDTO entryDTO = (PABenefitsDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			
			/*if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(entryDTO);
			}*/
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} //else {
				tableRow = tableItem.get(entryDTO);
			//}
			
			if("benefitCoverValue".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("550px");
				tableRow.put("benefitCoverValue", box);
				box.setData(entryDTO);
				addBenefitCoverContainerValue(box);
				addBenefitsCoverListener(box);
				/*if(null != entryDTO.getBenefitCoverValue())
				{
					loadBenefitsTableValues(box);
				}*/
				return box;
			} 
			else if("percentage".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(entryDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("percentage", field);
				addPercentageListener(field);
				return field;
			} 	
			else if("sumInsured".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(entryDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("sumInsured", field);	
				//addBenefitsCoversValue(field);
				return field;
			} 
			else if("eligibleAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(entryDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("eligibleAmount", field);		
				//populateDataBasedOnCover(field);
				return field;
			} 
			else if("eligibleAmountPerWeek".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("150px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(entryDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.addValueChangeListener(getNoOfDaysListener(field));
				tableRow.put("eligibleAmountPerWeek", field);		
				return field;
			}
			else if("totalEligibleAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("150px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(entryDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("totalEligibleAmount", field);		
				return field;
			}
			else if("noOfWeeks".equals(propertyId)) {

				TextField field = new TextField();
				field.setWidth("150px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
			//	field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.addValueChangeListener(getNoOfDaysListener(field));
				tableRow.put("noOfWeeks", field);		
				return field;
			} 
			else
			{
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}
	
	
	 public ValueChangeListener getNoOfDaysListener(final TextField component)
	 {
		 ValueChangeListener listener =  new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				//CombBox comp = (ComboBox) event.
				//GComboBox component = (GComboBox) event.
				//caluculateEligibleAmtForTTD(component);
				calculateTotalEligibleAmt(component/*type*/);
				//populateProductPerDayAmt(component);
				
			}
		};
		return listener;
	 }
	
	 public ValueChangeListener getEligibleAmtPerWeek(final TextField component)
	 {
		 ValueChangeListener listener =  new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				//CombBox comp = (ComboBox) event.
				//GComboBox component = (GComboBox) event.
				calculateTotalEligibleAmt(component/*type*/);
				//populateProductPerDayAmt(component);
				
			}
		};
		return listener;
	 }
	 
	 
	 private void caluculateEligibleAmtForTTD(TextField txtFld)
	 {
		 PABenefitsDTO dto = (PABenefitsDTO) txtFld.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
		 if(null != hashMap && !hashMap.isEmpty())
		 {
			 TextField sumInsured =  (TextField)hashMap.get("sumInsured");
			 TextField eligibleAmtFld = (TextField)hashMap.get("eligibleAmountPerWeek");
			 Double sumInsuredVal = 0d;
			 Double eligibleAmt = 0d;
			 Double calcualtedSIValue = 0d;
			 if(null != sumInsured && null != sumInsured.getValue())
			 {
				 sumInsuredVal = Double.valueOf(SHAUtils.getDoubleFromStringWithComma(sumInsured.getValue()));
				 calcualtedSIValue = sumInsuredVal * (1f/100f);
			 }
			 if(null != eligibleAmtFld && null != eligibleAmtFld.getValue())
			 {
				 eligibleAmt = Double.valueOf(SHAUtils.getDoubleFromStringWithComma(eligibleAmtFld.getValue()));
				 Double calculatedEligibleAmt = Math.min(calcualtedSIValue, eligibleAmt);
				 if(null != calculatedEligibleAmt )
					 eligibleAmtFld.setValue(String.valueOf(calculatedEligibleAmt));
			 }
			
			  
		 }
	 }
	 
	 private void calculateTotalEligibleAmt(TextField txtFld)
	 {
		 PABenefitsDTO dto = (PABenefitsDTO) txtFld.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
		 
		 if(null != hashMap && !hashMap.isEmpty())
		 {
			 TextField noOfWeeksFld =  (TextField)hashMap.get("noOfWeeks");
			 TextField eligibleAmtFld = (TextField)hashMap.get("eligibleAmountPerWeek");
			 TextField totalEligibleAmtFld = (TextField)hashMap.get("totalEligibleAmount");
			 Double noOfWeeks = 0d;
			 Double eligibleAmt = 0d;
			 if(null != noOfWeeksFld && null != noOfWeeksFld.getValue())
			 {
				 noOfWeeks = Double.valueOf(SHAUtils.getDoubleFromStringWithComma(noOfWeeksFld.getValue()));
				
			 }
			 if(noOfWeeks > 100)
			 {
				 showDeleteRowsPopup("No Of weeks is greater than 100. Please enter a value less than 100 to proceed.");
				 noOfWeeksFld.setValue(null);
			 }
			 else
			 {
				/* if(null != noOfWeeksFld && null != noOfWeeksFld.getValue())
				 {
					 
				 }*/
				 if(null != eligibleAmtFld && null != eligibleAmtFld.getValue())
				 {
					 eligibleAmt = Double.valueOf(SHAUtils.getDoubleFromStringWithComma(eligibleAmtFld.getValue()));
				 }
				 Double totalAmt = noOfWeeks * eligibleAmt;
				 if(null != totalEligibleAmtFld)
				 {
					 totalEligibleAmtFld.setValue(String.valueOf(totalAmt));
				 }
			 }
			 
		 }
	 }
	
	private void populateDataBasedOnCover(TextField field)
	{
		PABenefitsDTO benefitsDTO = (PABenefitsDTO) field.getData();
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(benefitsDTO);
		ComboBox cmbBox = (ComboBox) hashMap.get("benefitCoverValue");
		SelectValue selectValue = null;
		if(null != cmbBox)
		{
			selectValue = (SelectValue)cmbBox.getValue();
		}
		TextField percentage = (TextField) hashMap.get("percentage");
		 TextField sumInsured = (TextField) hashMap.get("sumInsured");
		 TextField eligibleAmount = (TextField) hashMap.get("eligibleAmount");
		 if(null != paBenefitsList && !paBenefitsList.isEmpty())
		 {
			 for (PABenefitsDTO paBenefitsDTO : paBenefitsList) {
				if(null != selectValue && selectValue.getId().equals(paBenefitsDTO.getBenefitsId()))
				{
					if(null != percentage)
						 percentage.setValue(paBenefitsDTO.getPercentage());
					 if(null != sumInsured)
						 sumInsured.setValue(String.valueOf(null != paBenefitsDTO.getSumInsured() ? paBenefitsDTO.getSumInsured() : 0d));
					 if(null != eligibleAmount)
						 eligibleAmount.setValue(String.valueOf(null != paBenefitsDTO.getEligibleAmount() ? paBenefitsDTO.getEligibleAmount() : 0d));
				}
			}
		 }
		 
	}
	
	private void addBenefitsCoversValue(TextField txtFld)
	{
		if(null != txtFld)
		{
			PABenefitsDTO coversDTO = (PABenefitsDTO) txtFld.getData();
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(coversDTO);
			 if(null != hashMap)
			 {
				 GComboBox cmbBox = (GComboBox) hashMap.get("benefitCoverValue");
				 if(null != cmbBox)
				 {
					if(null != paBenefitsDTO)
					{
						BeanItemContainer<SelectValue> benefitContainer = (BeanItemContainer<SelectValue>) referenceData.get("coverContainerValue");
						 for(int i = 0 ; i<benefitContainer.size() ; i++)
						 {
							if((benefitContainer.getIdByIndex(i).getId()).equals(paBenefitsDTO.getBenefitsId()))
							{
								
								cmbBox.setValue(benefitContainer.getIdByIndex(i));
							}
						}
					}
				 }
			 }
			 

		}
	}
	
	private void addBenefitsCoverListener(final GComboBox cmbBox)
	{
		cmbBox.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(null != value)
				{
					loadBenefitsTableValues(cmbBox);
				/*	PreauthDTO preauthDTO = (PreauthDTO)referenceData.get("preauthDTO");
					if(null != preauthDTO)
						fireViewEvent(PAClaimRequestDataExtractionPagePresenter.PA_BENEFITS_TABLE_DATA, benefitsId , preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),cmbBox);*/
					/*PABenefitsDTO benefitsDTO =  (PABenefitsDTO)cmbBox.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(benefitsDTO);
					TextField percentage = (TextField) hashMap.get("percentage");
					 TextField sumInsured = (TextField) hashMap.get("sumInsured");
					 TextField eligibleAmount = (TextField) hashMap.get("eligibleAmount");
					 if(null != paBenefitsList && !paBenefitsList.isEmpty())
					 {
						 for (PABenefitsDTO paBenefitsDTO : paBenefitsList) {
							if(null != value && value.getId().equals(paBenefitsDTO.getBenefitsId()))
							{
								if(null != percentage)
									 percentage.setValue(paBenefitsDTO.getPercentage());
								 if(null != sumInsured)
									 sumInsured.setValue(String.valueOf(null != paBenefitsDTO.getSumInsured() ? paBenefitsDTO.getSumInsured() : 0d));
								 if(null != eligibleAmount)
									 eligibleAmount.setValue(String.valueOf(null != paBenefitsDTO.getEligibleAmount() ? paBenefitsDTO.getEligibleAmount() : 0d));
							}
						}
					 }*/
					/*Long coverId = value.getId();
					List<PABenefitsDTO> paBenefitsList = getValues();
					showErrorMsg("Same cover Id cannot be selected again. Please choose different cover.");
					if(null != paBenefitsList && !paBenefitsList.isEmpty())
					{
						
					}*/
				}
			}
		});
	}
	
	private void loadBenefitsTableValues(GComboBox cmbBox)
	{
		PreauthDTO preauthDTO = (PreauthDTO)referenceData.get("preauthDTO");
		if(null != preauthDTO)
			fireViewEvent(PAClaimRequestDataExtractionPagePresenter.PA_BENEFITS_TABLE_DATA, benefitsId , preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),cmbBox);

	}
	
	
	public void loadBenefitsTableBasedOnBenefit(List<PABenefitsDTO> benefitsDTOList, GComboBox cmb)
	{
		PABenefitsDTO benefitsDTO =  (PABenefitsDTO)cmb.getData();
		SelectValue value = (SelectValue) cmb.getValue();
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(benefitsDTO);
		if(null != hashMap)
		{
			TextField percentage = (TextField) hashMap.get("percentage");
			TextField sumInsured = (TextField) hashMap.get("sumInsured");
			TextField eligibleAmount = (TextField) hashMap.get("eligibleAmount");
			TextField eligibleAmountPerWeek = (TextField) hashMap.get("eligibleAmountPerWeek");
			if(null != paBenefitsList && !paBenefitsList.isEmpty())
			{
			 for (PABenefitsDTO paBenefitsDTO : benefitsDTOList) {
				if(null != value && value.getId().equals(paBenefitsDTO.getBenefitsId()))
				{
					if(null != percentage)
					{
						 percentage.setValue(paBenefitsDTO.getPercentage());
						 if(ReferenceTable.ANY_OTHER_PERMANENT_PARTIAL_DISABLEMENT.equals(value.getId()))
						 {
							 percentage.setEnabled(true);
						 }
						 else
						 {
							 percentage.setEnabled(false);
						 }
					}
					 if(null != sumInsured)
						 sumInsured.setValue(String.valueOf(null != paBenefitsDTO.getSumInsured() ? paBenefitsDTO.getSumInsured() : 0d));
					 if(null != eligibleAmount)
						 eligibleAmount.setValue(String.valueOf(null != paBenefitsDTO.getEligibleAmount() ? paBenefitsDTO.getEligibleAmount() : 0d));
					 if(null != eligibleAmountPerWeek)
						 eligibleAmountPerWeek.setValue(String.valueOf(null != paBenefitsDTO.getEligibleAmountPerWeek() ? paBenefitsDTO.getEligibleAmountPerWeek() : 0d));
				}
			 }
			}
		}
	}
	
	private void showErrorMsg(String message)
	{
		Label label = new Label(message, ContentMode.HTML);
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
	/* HorizontalLayout layout = new HorizontalLayout(
				new Label(""));
		layout.setMargin(true);
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setWidth("35%");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
	}
	
	public void setPABenefitTableValue(PABenefitsDTO paBenefitDTO)
	{
		this.paBenefitsDTO = paBenefitDTO;
	}
	
	
	public void valueChangeLisenerForDate(final DateField  total,final String fldName){
		
		if(null != total)
		{
			total
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
				}
			});
		}
	}
	

	


	
	
	
	
	
	
	 public void addBeanToList(PABenefitsDTO pabenefitsDTO) {
	    	//container.addBean(uploadDocumentsDTO);
		 container.addItem(pabenefitsDTO);

//	    	data.addItem(pedValidationDTO);
	    	//manageListeners();
	    }
	
	 
	
	
	 public List<PABenefitsDTO> getValues() {
	    	@SuppressWarnings("unchecked")
	    	List<PABenefitsDTO> itemIds = new ArrayList<PABenefitsDTO>();
	    	if(this.table != null) {
	    		itemIds = (List<PABenefitsDTO>) this.table.getItemIds() ;
	    	} 
			
	    	return itemIds;
	    }
	 
	 public List<String> getErrors()
		{
			return this.errorMessages;
		}
	 
	  public void getErrorMessage(String eMsg){
			
			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Error");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
		}
	  
	  public boolean isValid()
		{
			boolean hasError = false;
			errorMessages.removeAll(getErrors());
			@SuppressWarnings("unchecked")
			List<PABenefitsDTO> itemIds = (List<PABenefitsDTO>) table.getItemIds();
			Map<Long, String> duplicateItemMap = new HashMap<Long, String>();
			Map<Long, String> validationMap = new HashMap<Long, String>();
			if(null != itemIds && !itemIds.isEmpty())
			{
				for (PABenefitsDTO bean : itemIds) {
					if(null != bean.getBenefitCoverValue())
					{
						if(null != duplicateItemMap && !duplicateItemMap.isEmpty())
						{
							if(duplicateItemMap.containsKey(bean.getBenefitCoverValue().getId()))
							{
								hasError = true;
								errorMessages.add("Same classification value cannot be selected twice in Benefits Table. Please change the covers in row to proceed further");
								break;
							}
							
						}
							duplicateItemMap.put(bean.getBenefitCoverValue().getId(), bean.getBenefitCoverValue().getValue());
					}
					else
					{
						hasError = true;
						errorMessages.add("Classification in benefits table cannot be empty. Please select a value to proceed further");
						break;
					}
					if(null != benefitsId && benefitsId.equals(ReferenceTable.BENEFITS_TTD_MASTER_VALUE))
					{
						if(null == bean.getNoOfWeeks())
						{
							hasError = true;
							errorMessages.add("No Of weeks mandatory for TTD benefits.");
							break;
						}
					}
			}
		}
			return !hasError;

		}
	  
	  private void showDeleteRowsPopup(String message)
		{

			
			Label successLabel = new Label("<b style = 'color: red;'> "+ message, ContentMode.HTML);
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			Button cancelButton = new Button("Cancel");
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			 HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
			horizontalLayout.setMargin(true);
			horizontalLayout.setSpacing(true);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
			
			//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
			//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
			
			VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final Window dialog = new Window();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
		
			getUI().getCurrent().addWindow(dialog);
//					dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {					
						dialog.close();
				}
			});
		}
	  
	  public List<PABenefitsDTO> getDeletedBillList()
		{
			return deletedList;
		}
	  
	  
	  public void addPercentageListener(final TextField field){
			
			if(null != field)
			{
				field
				.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						calculateEligibleAmount(field);
						
					}
				});
			}
		}
	  
	  public void calculateEligibleAmount(TextField field)
	  {
		  PABenefitsDTO dto = (PABenefitsDTO) field.getData();
		  if(null != dto)
		  {
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
			if(null != hashMap)
			{
				TextField sumInsured = (TextField)hashMap.get("sumInsured");
				TextField percentage = (TextField)hashMap.get("percentage");
				TextField eligibleAmount = (TextField)hashMap.get("eligibleAmount");
				Double sumInsuredVal = 0d;
				Double percentageVal = 0d;
				Double eligibleAmountVal = 0d;
				if(null != sumInsured)
				{
					//sumInsuredVal = Double.valueOf(String.valueOf(sumInsured.getValue()));
					sumInsuredVal = SHAUtils.getDoubleFromStringWithComma(String.valueOf(sumInsured.getValue()));
				}
				if(null != percentageVal)
				{
					//percentageVal = Double.valueOf(String.valueOf(percentage.getValue()));
					percentageVal= SHAUtils.getDoubleFromStringWithComma(String.valueOf(percentage.getValue()));
				}		
				if(percentageVal > 100)
				{ 
				 	Label label = new Label("Percentage cannot be greater than 100. Please enter value less than or equal to 100 to proceed.", ContentMode.HTML);
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
				/* HorizontalLayout layout = new HorizontalLayout(
							new Label(""));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Errors");
					dialog.setWidth("35%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);*/
					if(null != percentage)
						percentage.setValue(null);
					/*if(null != txtAmount)
						txtAmount.setEnabled(false);*/
			 
				
				}
				else
				{
					eligibleAmountVal = sumInsuredVal * (percentageVal/100f);
					if(null != eligibleAmount && null != eligibleAmountVal)
					{
						eligibleAmount.setValue(String.valueOf(eligibleAmountVal));
					}
				}
			}
		  }
	  }
	  
	  public void setEnableBasedOnNoOfWeeks(){
		  List<PABenefitsDTO> itemIds = new ArrayList<PABenefitsDTO>();
		  
	    	if(this.table != null) {
	    		itemIds = (List<PABenefitsDTO>) this.table.getItemIds() ;
	    		
	    		for (PABenefitsDTO paBenefitsDTO : itemIds) {
	    			
	    			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(paBenefitsDTO);
	    			 
	    			 if(null != hashMap && !hashMap.isEmpty())
	    			 {
	    				 TextField noOfWeeksFld =  (TextField)hashMap.get("noOfWeeks");
	    				 GComboBox benefitCoverFld =  (GComboBox)hashMap.get("benefitCoverValue");
	    				 if(noOfWeeksFld != null){
	    					 noOfWeeksFld.setEnabled(true);
	    				 }
	    				 if(benefitCoverFld != null){
	    					 benefitCoverFld.setEnabled(false);
	    				 }
	    			 }
	    		}
	    	} 
		 
	  }
		 

}

