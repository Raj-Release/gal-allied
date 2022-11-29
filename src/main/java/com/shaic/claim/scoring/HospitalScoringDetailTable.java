package com.shaic.claim.scoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class HospitalScoringDetailTable extends ViewComponent {

	private static final long serialVersionUID = -2451354773032502514L;

	private Map<HospitalScoringDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<HospitalScoringDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<HospitalScoringDTO> data = new BeanItemContainer<HospitalScoringDTO>(HospitalScoringDTO.class);
	
	private Table table;

	HospitalScoringDTO bean;
	
	private HospitalScoringDetailView viewPageObj;
	
	public HospitalScoringDetailView getViewPageObj() {
		return viewPageObj;
	}

	public void setViewPageObj(HospitalScoringDetailView viewPageObj) {
		this.viewPageObj = viewPageObj;
	}
	
	@Inject
	private HospitalScoringService hsService;
	
	private Intimation currentIntim;

	boolean isOptionEnabled;

	public Object[] VISIBLE_COLUMNS = new Object[] {"scoringName","scoringBooleanValue"};
	
	private int tableRSize = 0;
	
	public void init(boolean isEnableFlag, String intimationNumber, int noOfRows) {
		isOptionEnabled = isEnableFlag;
		currentIntim = hsService.getIntimationByNo(intimationNumber);
		tableRSize = noOfRows;
		VerticalLayout layout = new VerticalLayout();
		initTable(layout);
		layout.addComponent(table);
		setCompositionRoot(layout);
	}

	void initTable(VerticalLayout layout) {
		table = new Table();
		table.setContainerDataSource(data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(tableRSize);
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setSortEnabled(false);

		table.setColumnHeader("scoringName", "Hospital Score Points");
		table.setColumnHeader("scoringBooleanValue", "Select");
		table.setColumnWidth("scoringName", 630);
		table.setColumnWidth("scoringBooleanValue", 200);
		table.setEditable(true);
		table.setSelectable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());
		layout.addComponent(table);
	}

	public void setVisibleColumns() {
		table.setVisibleColumns(VISIBLE_COLUMNS);
	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = 7116790204338353464L;

		@Override
		public Field<?> createField(Container container, Object itemId,	Object propertyId, Component uiContext) {
			HospitalScoringDTO queryDetailsTableDto = (HospitalScoringDTO) itemId;
			TextField queryfield = null;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(queryDetailsTableDto) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(queryDetailsTableDto, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(queryDetailsTableDto);
			}

			if("scoringName".equals(propertyId)) {
				queryfield = new TextField();
				queryfield.setNullRepresentation("");
				queryfield.setStyleName("tfwb");
				queryfield.setData(queryDetailsTableDto);
				queryfield.setStyleName(queryDetailsTableDto.getTextFieldStyleName());
				queryfield.setWidth("400px");
				queryfield.setReadOnly(true);
				tableRow.put("scoringName", queryfield);
				return queryfield;
			} else {
				final OptionGroup optionType = new OptionGroup();
				optionType.addItems(getRadioButtonOptions());
				optionType.setItemCaption(Boolean.valueOf("true"), "Yes");
				optionType.setItemCaption(Boolean.valueOf("false"), "No");
				optionType.setData(queryDetailsTableDto);
				optionType.setVisible(queryDetailsTableDto.isOptionVisible());
				optionType.setStyleName("inlineStyle");
				optionType.setId(queryDetailsTableDto.getComponentId());
				if(queryDetailsTableDto.getScoringBooleanValue() != null){
					if(queryDetailsTableDto.getScoringBooleanValue().booleanValue()){
						optionType.select(Boolean.valueOf("true"));
					}
					if(!queryDetailsTableDto.getScoringBooleanValue().booleanValue()){
						optionType.select(Boolean.valueOf("false"));
					}
				}else{
					optionType.select(null);
				}

				optionType.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
				optionType.setValue(queryDetailsTableDto.getScoringBooleanValue());
				optionType.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;
					@SuppressWarnings("unchecked")
					@Override
					public void valueChange(ValueChangeEvent event) {
						HospitalScoringDTO dtoObj = (HospitalScoringDTO)((OptionGroup) event.getProperty()).getData();
						List<Integer> R3SCatList =  new ArrayList<Integer>();
//						R3SCatList.add("Professional charges");
//						R3SCatList.add("OT consumables");
//						R3SCatList.add("OT Medicines");
//						R3SCatList.add("Implant");
						R3SCatList.add(1007);
						R3SCatList.add(1023);
						R3SCatList.add(1010);
						
						if(dtoObj.getComponentId().endsWith("0")){
							String idStartWith = dtoObj.getComponentId().substring(0, 2);
							List<HospitalScoringDTO> listOfAllRows = (List<HospitalScoringDTO>) table.getItemIds();
							for(HospitalScoringDTO rec : listOfAllRows){
								if(rec.getComponentId().startsWith(idStartWith) && !rec.getComponentId().endsWith("0")){
									if(dtoObj.getScoringBooleanValue()){
										dtoObj.setScoringValue("Y");
										rec.setScoringBooleanValue(true);
										rec.setScoringValue("Y");
										rec.setOptionEnabled(false);
									}else{
										dtoObj.setScoringValue("N");
										rec.setScoringBooleanValue(null);
										rec.setScoringValue(null);
										rec.setOptionEnabled(true);
									}

								}
							}
							refreshTable();						
						}else{
//							if(dtoObj.getScoringName().equals("ANH Package – For ANH / Index Price/SOC/Market Rate  - For NANH")){
							if(dtoObj.getActualSubCategoryId().intValue() ==  1004){
								if(dtoObj.getScoringBooleanValue()){
									dtoObj.setScoringValue("Y");
								}else{
									dtoObj.setScoringValue("N");
								}
								
								List<Integer> listofSC =  new ArrayList<Integer>();
								listofSC.add(1007);
								listofSC.add(1023);
								listofSC.add(1010);
								List<HospitalScoringDTO> listOfDtos = (List<HospitalScoringDTO>) table.getItemIds();
								boolean isViolationRowEnabled =  false;
								for(HospitalScoringDTO rec : listOfDtos){
									if(rec.getScoringName().equals("If Violation of ANH/Excess of Index Price ,Justifiable")){
										if(dtoObj.getScoringBooleanValue()){
											rec.setScoringBooleanValue(null);
											rec.setScoringValue(null);
											rec.setOptionEnabled(false);
										}else{
											isViolationRowEnabled = true;
											rec.setOptionEnabled(true);
										}
									}else if(listofSC.contains(rec.getActualSubCategoryId())){
										if(dtoObj.getScoringBooleanValue()){
											rec.setScoringBooleanValue(null);
											rec.setScoringValue(null);
											rec.setOptionEnabled(false);
										}else{
											if(isViolationRowEnabled){
												rec.setOptionEnabled(false);
											}else{
												rec.setOptionEnabled(true);
											}
										}
									}
								}
								refreshTable();
							}//else if(dtoObj.getScoringName().equals("If Violation of ANH/Excess of Index Price ,Justifiable") && dtoObj.getScoringBooleanValue()){
							   else if(dtoObj.getActualSubCategoryId().intValue() ==  1006 && dtoObj.getScoringBooleanValue()){
								if(dtoObj.getScoringBooleanValue()){
									dtoObj.setScoringValue("Y");
								}else{
									dtoObj.setScoringValue("N");
								}
								
								List<HospitalScoringDTO> listOfDtos = (List<HospitalScoringDTO>) table.getItemIds();
								for(HospitalScoringDTO rec : listOfDtos){
									if(R3SCatList.contains(rec.getActualSubCategoryId())){
										rec.setScoringBooleanValue(null);
										rec.setScoringValue(null);
										rec.setOptionEnabled(false);
									}
								}
								refreshTable();
							}//else if(dtoObj.getScoringName().equals("If Violation of ANH/Excess of Index Price ,Justifiable") && !dtoObj.getScoringBooleanValue()){
							   else if(dtoObj.getActualSubCategoryId().intValue() ==  1006 && !dtoObj.getScoringBooleanValue()){
								if(dtoObj.getScoringBooleanValue()){
									dtoObj.setScoringValue("Y");
								}else{
									dtoObj.setScoringValue("N");
								}
								
								List<HospitalScoringDTO> listOfDtos = (List<HospitalScoringDTO>) table.getItemIds();
								for(HospitalScoringDTO rec : listOfDtos){
									if(R3SCatList.contains(rec.getActualSubCategoryId())){
										rec.setScoringBooleanValue(null);
										rec.setScoringValue(null);
										rec.setOptionEnabled(true);
									}
								}
								refreshTable();
							}else{
								Boolean selectedValue = (Boolean)event.getProperty().getValue();
								if(selectedValue.booleanValue()){
									optionType.select(Boolean.valueOf("true"));
									dtoObj.setScoringBooleanValue(true);
									dtoObj.setScoringValue("Y");
								}else{
									optionType.select(Boolean.valueOf("false"));
									dtoObj.setScoringBooleanValue(false);
									dtoObj.setScoringValue("N");
								}
							}
						}
					}
				});			
				if(!viewPageObj.getScreenName().equals("ViewPage")){
					optionType.setEnabled(queryDetailsTableDto.getOptionEnabled());
				}else{
					optionType.setEnabled(false);
				}
//				optionType.setEnabled(isOptionEnabled);
//				if(!queryDetailsTableDto.isOptionVisible()){
//					optionType.setVisible(queryDetailsTableDto.isOptionVisible());
//				}
				tableRow.put("scoringValue", optionType);
				return optionType;
			}
		}
	}


	public void addBeanToList(HospitalScoringDTO diagnosisProcedureTableDTO) {
		data.addBean(diagnosisProcedureTableDTO);
	}
	public void addList(List<HospitalScoringDTO> diagnosisProcedureTableDTO) {
		for (HospitalScoringDTO diagnosisProcedureTableDTO2 : diagnosisProcedureTableDTO) {
			data.addBean(diagnosisProcedureTableDTO2);
		}
	}

	@SuppressWarnings("unchecked")
	public List<HospitalScoringDTO> getValues() {
		List<HospitalScoringDTO> itemIds = (List<HospitalScoringDTO>) this.table.getItemIds() ;
		return itemIds;
	}
	
	public void removeRow() {
		table.removeAllItems();
	}
	
	public void refreshTable(){
		table.refreshRowCache(); 
	}

	protected Collection<Boolean> getRadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}

}
