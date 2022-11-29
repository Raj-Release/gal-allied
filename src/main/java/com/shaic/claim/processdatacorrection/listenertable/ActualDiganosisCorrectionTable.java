package com.shaic.claim.processdatacorrection.listenertable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.test.DiagnosisComboBox;
import com.shaic.arch.test.InsuranceDiagnosisComboBox;
import com.shaic.arch.test.InsuranceDiagnosisContainer;
import com.shaic.arch.test.SuggestingContainer;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.premedical.listenerTables.AddDiagnosisPopup;
import com.shaic.claim.processdatacorrection.dto.DiganosisCorrectionDTO;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.claim.processdatacorrectionhistorical.search.DataCorrectionHistoricalPresenter;
import com.shaic.claim.processdatacorrectionpriority.search.DataCorrectionPriorityPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ActualDiganosisCorrectionTable extends ViewComponent{



	/**
	 * 
	 */
	private static final long serialVersionUID = 197854198269811200L;

	@EJB
	private MasterService masterService;

	private Map<DiganosisCorrectionDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<DiganosisCorrectionDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<DiganosisCorrectionDTO> data = new BeanItemContainer<DiganosisCorrectionDTO>(DiganosisCorrectionDTO.class);

	private Table table;

	private Map<String, Object> referenceData;

	private List<String> errorMessages;

	public Boolean isValueChanges=false;

	public String presenterString;

	@Inject
	private AddDiagnosisPopup diagnosisPopup;

	public void init(String presenterString){

		this.presenterString = presenterString;
		this.errorMessages = new ArrayList<String>();

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		initTable(layout);

		table.setWidth("80%");
		table.setPageLength(table.getItemIds().size());

		layout.addComponent(table);
		setCompositionRoot(layout);

	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout){

		table = new Table("Actual Diagnosis Details", data);
		table.addStyleName("generateColumnTable");
		table.setPageLength(table.getItemIds().size());

		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final Button deleteButton = new Button("Delete");
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						DiganosisCorrectionDTO dto = (DiganosisCorrectionDTO) currentItemId;
						if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
							fireViewEvent(DataCorrectionPresenter.DELETE_DIGANOSIS_CORRECTION_VALUES,dto.getKey());	
						}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_HISTORICAL)){
							fireViewEvent(DataCorrectionHistoricalPresenter.DELETE_DIGANOSIS_CORRECTION_VALUES_HIST,dto.getKey());	
						}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
							fireViewEvent(DataCorrectionPriorityPresenter.DELETE_DIGANOSIS_CORRECTION_VALUES_PRIORITY,dto.getKey());	
						}
						table.removeItem(currentItemId);
						removeTOAbyvalues();
					} 
				});
				deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				return deleteButton;
			}
		});
		
		table.removeGeneratedColumn("addDiagnosis");
		table.addGeneratedColumn("addDiagnosis", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button addDiagnasis = new Button("");
				final DiganosisCorrectionDTO dto = (DiganosisCorrectionDTO) itemId;
				addDiagnasis.setEnabled(true);
				addDiagnasis.setIcon(FontAwesome.FILE);
				addDiagnasis.setData(itemId);
				addDiagnasis.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {

						Window popup = new com.vaadin.ui.Window();
						popup.setCaption("Add New Diagnosis");
						popup.setWidth("30%");
						popup.setHeight("30%");
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);

						final ComboBox cmbBox = (ComboBox) hashMap.get("proposeddiagnosisName");

						diagnosisPopup.init(null, cmbBox, popup);
						popup.setContent(diagnosisPopup);
						popup.setClosable(true);
						popup.center();
						popup.setResizable(false);
						popup.addCloseListener(new Window.CloseListener() {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							@Override
							public void windowClose(CloseEvent e) {
								// TODO Auto-generated method stub
							}
						});

						popup.setModal(true);
						UI.getCurrent().addWindow(popup);

					}
				});
				return addDiagnasis;
			}
		});

		table.setVisibleColumns(new Object[] {"primaryDiagnosis","proposeddiagnosisName","addDiagnosis","proposedicdCode","Delete"});
		table.setColumnHeader("proposeddiagnosisName", "Actual Hospital Diagnosis");
		table.setColumnHeader("proposedicdCode", "Actual Insurance Diagnosis");
		table.setColumnHeader("primaryDiagnosis", "Primary");
		table.setColumnHeader("addDiagnosis", "");

		table.setColumnAlignment("proposeddiagnosisName",Align.CENTER);
		table.setColumnAlignment("proposedicdCode",Align.CENTER);
		table.setColumnAlignment("primaryDiagnosis",Align.CENTER);
		table.setEditable(true);

		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {

		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {

			DiganosisCorrectionDTO diganosisCorrectionDTO = (DiganosisCorrectionDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(diganosisCorrectionDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(diganosisCorrectionDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(diganosisCorrectionDTO);
			}
			if("proposeddiagnosisName".equals(propertyId)){
				DiagnosisComboBox box = new DiagnosisComboBox();
				SuggestingContainer diagnosisContainer = new SuggestingContainer(masterService);
				box.setContainerDataSource(diagnosisContainer);
				box.setEnabled(true);
				box.setFilteringMode(FilteringMode.STARTSWITH);
				box.setTextInputAllowed(true);
				box.setNullSelectionAllowed(true);
				box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				box.setItemCaptionPropertyId("value");
				box.setNewItemsAllowed(true);
				box.setData(diganosisCorrectionDTO);
				tableRow.put("proposeddiagnosisName", box);
				box.setWidth("70%");
				addDiagnosisNameListener(box);
				return box;
			}else if("proposedicdCode".equals(propertyId)){
				InsuranceDiagnosisComboBox box = new InsuranceDiagnosisComboBox();
				InsuranceDiagnosisContainer diagnosisContainer = new InsuranceDiagnosisContainer(masterService);
				box.setContainerDataSource(diagnosisContainer);
				box.setEnabled(true);
				box.setFilteringMode(FilteringMode.STARTSWITH);
				box.setTextInputAllowed(true);
				box.setNullSelectionAllowed(true);
				box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				box.setItemCaptionPropertyId("value");
				box.setNewItemsAllowed(true);
				box.setData(diganosisCorrectionDTO);
				box.setWidth("70%");	
				tableRow.put("proposedicdCode", box);
				insuranceDiagnosisListener(box);
				return box;

			}else if("primaryDiagnosis".equals(propertyId))
			 {
				 final OptionGroup optionType = new OptionGroup();
				 optionType.addItems(getReadioButtonOptions());
				 optionType.setItemCaption(Boolean.valueOf("true"), "");
				 optionType.setEnabled(true);
				 optionType.setData(diganosisCorrectionDTO);
				 optionType.setStyleName("inlineStyle");
				 optionType.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
				 optionType.setValue(diganosisCorrectionDTO.getPrimaryDiagnosis());
				 if(diganosisCorrectionDTO.getPrimaryDiagnosis() != null){
					 if(diganosisCorrectionDTO.getPrimaryDiagnosis().booleanValue()){
						 optionType.select(Boolean.valueOf("true"));
					 }
				 }else{
					 optionType.select(null);
				 }
				 addPrimaryListener(optionType);
				 return optionType;
			 } else {
				Field<?> field = super.createField(container, itemId,propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	public List<DiganosisCorrectionDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<DiganosisCorrectionDTO> itemIds = (List<DiganosisCorrectionDTO>) this.table.getItemIds();
		return itemIds;
	}

	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(DiganosisCorrectionDTO diganosisCorrectionDTO) {
		data.addItem(diganosisCorrectionDTO);
	}

	public void addBeansToList(List<DiganosisCorrectionDTO> diganosisCorrectionDTOs){

		for(DiganosisCorrectionDTO diganosisCorrectionDTO: diganosisCorrectionDTOs){
			addBeanToList(diganosisCorrectionDTO);
		}
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}

	@SuppressWarnings("deprecation")
	private void addDiagnosisNameListener(ComboBox diagnosisName) {
		diagnosisName.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2332276795125344767L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				GComboBox diagnosis = (GComboBox) event.getProperty();
				if(diagnosis.getValue() != null) {					
					SuggestingContainer containerDataSource = (SuggestingContainer)diagnosis.getContainerDataSource();

					if(diagnosis != null && diagnosis.getValue() != null){
						SelectValue selected = (SelectValue)diagnosis.getValue();
						containerDataSource.setComboBoxValue(selected.getValue());
					}else{
						containerDataSource.setComboBoxValue("");
					}
				}else{
					SuggestingContainer containerDataSource = (SuggestingContainer)diagnosis.getContainerDataSource();
					containerDataSource.setComboBoxValue("");
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void insuranceDiagnosisListener(ComboBox diagnosisName) {
		diagnosisName.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2332276795125344767L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				ComboBox diagnosis = (ComboBox) event.getProperty();
				if (diagnosis.getValue() != null) {
					InsuranceDiagnosisContainer containerDataSource = (InsuranceDiagnosisContainer) diagnosis.getContainerDataSource();

					if (diagnosis != null && diagnosis.getValue() != null) {
						SelectValue selected = (SelectValue) diagnosis.getValue();
						containerDataSource.setComboBoxValue(selected.getCommonValue());
						
						//Covid 19Variant
						String insuranceDiagnosisCode=selected.getValue();
						if(insuranceDiagnosisCode !=null){
							
								if(ReferenceTable.getCovid19VariantInsuranceDiagnosisCode().contains(insuranceDiagnosisCode)){
									//bean.setIsCovid19VariantEnable(true);
									if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
										fireViewEvent(DataCorrectionPresenter.DATA_VALIDATION_GENERATE_COVID19VARIANT_FIELDS,true);
									}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_HISTORICAL)){
										fireViewEvent(DataCorrectionHistoricalPresenter.DATA_VALIDATION_HISTORICAL_GENERATE_COVID19VARIANT_FIELDS,true);
									}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
										fireViewEvent(DataCorrectionPriorityPresenter.DATA_VALIDATION_PRIORITY_GENERATE_COVID19VARIANT_FIELDS,true);	
									}
								}
								else{
									//bean.setIsCovid19VariantEnable(false);
									if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
										fireViewEvent(DataCorrectionPresenter.DATA_VALIDATION_GENERATE_COVID19VARIANT_FIELDS,false);
									}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_HISTORICAL)){
										fireViewEvent(DataCorrectionHistoricalPresenter.DATA_VALIDATION_HISTORICAL_GENERATE_COVID19VARIANT_FIELDS,false);
									}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
										fireViewEvent(DataCorrectionPriorityPresenter.DATA_VALIDATION_PRIORITY_GENERATE_COVID19VARIANT_FIELDS,false);	
									}
								}
						}
						
						if (selected.getId() != null 
								&& selected.getId().equals(SHAConstants.COVID_19_ICD_IDENT_KEY)) {
							if(presenterString.equalsIgnoreCase("Data_Validation")){
								fireViewEvent(DataCorrectionPresenter.ACTUAL_TOA_CHANGED,true);
							}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
								fireViewEvent(DataCorrectionPriorityPresenter.ACTUAL_TOA_CHANGED_PRIORITY,true);
							}
						}else{
							removeTOAbyvalues();
						}
					} else {
						containerDataSource.setComboBoxValue("");
					}
				} else {
					InsuranceDiagnosisContainer containerDataSource = (InsuranceDiagnosisContainer) diagnosis.getContainerDataSource();
					containerDataSource.setComboBoxValue("");
				}
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	private void addPrimaryListener(final OptionGroup optionType){

		optionType.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				OptionGroup component = (OptionGroup) event.getProperty();
				DiganosisCorrectionDTO pedValidationTableDTO = (DiganosisCorrectionDTO) component.getData();
				List<DiganosisCorrectionDTO> itemIds = (List<DiganosisCorrectionDTO>) table.getItemIds();
				if(pedValidationTableDTO.getDiagnosisName() !=null){
					if(itemIds !=null && !itemIds.isEmpty()){
						for(DiganosisCorrectionDTO detailsTableDTO:itemIds){
							if(pedValidationTableDTO.getDiagnosisName().toString().equals(detailsTableDTO.getDiagnosisName().toString())){
								detailsTableDTO.setPrimaryDiagnosis(true);
							}		
							else{
								detailsTableDTO.setPrimaryDiagnosis(null);
							}
						}
					}
					refreshTable();
				}else{
					component.select(null);
				}
			}
		});
	}
	
	public void refreshTable(){
		table.refreshRowCache(); 
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(1);
		coordinatorValues.add(true);

		return coordinatorValues;
	}

	private void removeTOAbyvalues(){
		List<DiganosisCorrectionDTO> diagnosisList =  getValues();
		boolean isExist = false;
		for (DiganosisCorrectionDTO diagnosisDetailsTableDTO : diagnosisList) {
			if(diagnosisDetailsTableDTO.getProposedicdCode().getId().equals(SHAConstants.COVID_19_ICD_IDENT_KEY)){
				isExist = true;
				break;
			}
		}
		if(!isExist){
			if(presenterString.equalsIgnoreCase("Data_Validation")){
				fireViewEvent(DataCorrectionPresenter.ACTUAL_TOA_CHANGED,false);								
			}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
				fireViewEvent(DataCorrectionPriorityPresenter.ACTUAL_TOA_CHANGED_PRIORITY,false);
			}
		}
	}
	
	private void removeCovid19Variantvalues(){
		List<DiganosisCorrectionDTO> diagnosisList =  getValues();
		boolean isExist = false;
		for (DiganosisCorrectionDTO diagnosisDetailsTableDTO : diagnosisList) {
			if(diagnosisDetailsTableDTO != null && diagnosisDetailsTableDTO.getIcdCode() != null && diagnosisDetailsTableDTO.getIcdCode().getValue() != null){

				String insuranceDiagnosisCode= diagnosisDetailsTableDTO.getIcdCode().getValue();
				if(ReferenceTable.getCovid19VariantInsuranceDiagnosisCode().contains(insuranceDiagnosisCode)){
				isExist = true;
				break;
			}
			}
		}
		if(!isExist){
			if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
				fireViewEvent(DataCorrectionPresenter.DATA_VALIDATION_GENERATE_COVID19VARIANT_FIELDS,false);
			}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_HISTORICAL)){
				fireViewEvent(DataCorrectionHistoricalPresenter.DATA_VALIDATION_HISTORICAL_GENERATE_COVID19VARIANT_FIELDS,false);
			}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
				fireViewEvent(DataCorrectionPriorityPresenter.DATA_VALIDATION_PRIORITY_GENERATE_COVID19VARIANT_FIELDS,false);	
			}
		}
	}
	
	public void clearObject() {
		clearDataCorrectionTableItem(tableItem);
		SHAUtils.setClearReferenceData(referenceData);
		masterService = null;
		data = null;
		errorMessages = null;
		presenterString = null;
	}
	
	private void clearDataCorrectionTableItem(Map<DiganosisCorrectionDTO, HashMap<String, AbstractField<?>>> referenceData){
		
		if(referenceData != null){
		
	    	Iterator<Entry<DiganosisCorrectionDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
	    	//referenceData.clear();
	    	try{
		        while (iterator.hasNext()) {
		            Map.Entry pair = (Map.Entry)iterator.next();
		            Object object = pair.getValue();
		            object = null;
		            pair = null;	
		        }
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	       referenceData.clear();
	       referenceData = null;
		}
		
		}
}
