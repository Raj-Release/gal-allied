package com.shaic.claim.scoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.processdatacorrection.search.HospitalScoringCorrectionView;
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

public class HospitalScoringTable extends ViewComponent {

	private static final long serialVersionUID = -2451354773032502514L;

	private Map<HospitalScoringDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<HospitalScoringDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<HospitalScoringDTO> data = new BeanItemContainer<HospitalScoringDTO>(HospitalScoringDTO.class);
	
	private Table table;

	HospitalScoringDTO bean;
	
	private HospitalScoringView viewPageObj;
	
	private HospitalScoringCorrectionView correctionView;
	
	public HospitalScoringView getViewPageObj() {
		return viewPageObj;
	}

	public void setViewPageObj(HospitalScoringView viewPageObj) {
		this.viewPageObj = viewPageObj;
	}
	
	public void setViewPageObj(HospitalScoringCorrectionView correctionView) {
		this.correctionView = correctionView;
	}
	
	@Inject
	private HospitalScoringService hsService;
	
	private Intimation currentIntim;

	boolean isOptionEnabled;
	boolean isConfirmBoxClicked;

	public Object[] VISIBLE_COLUMNS = new Object[] {"scoringName","scoringBooleanValue"};
	
	public void init(boolean isEnableFlag, String intimationNumber) {
		isOptionEnabled = isEnableFlag;
		currentIntim = hsService.getIntimationByNo(intimationNumber);
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
		/*if(currentIntim.getClaimType().getKey().intValue() == ReferenceTable.CLAIM_TYPE_CASHLESS_ID){
			table.setPageLength(19);
		}else{
			table.setPageLength(15);
		}*/
		table.setPageLength(14);
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
		isConfirmBoxClicked = false;
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
				queryfield.setWidth("100%");
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
						final ValueChangeEvent event_Rej = event;
						HospitalScoringDTO dtoObj = (HospitalScoringDTO)((OptionGroup) event.getProperty()).getData();
						List<Integer> R3SCatList =  new ArrayList<Integer>();
//						R3SCatList.add("Professional charges");
//						R3SCatList.add("OT consumables");
//						R3SCatList.add("OT Medicines");
//						R3SCatList.add("Implant");
						R3SCatList.add(1007);
						R3SCatList.add(1023);
						R3SCatList.add(1010);
						
						final List<Integer> SDList =  new ArrayList<Integer>();
						SDList.add(1025);
						SDList.add(1026);
						SDList.add(1027);
						SDList.add(1034);
						
						final List<Integer> MDList =  new ArrayList<Integer>();
						MDList.add(1028);
						MDList.add(1029);
						MDList.add(1030);
						MDList.add(1031);
						MDList.add(1032);
						MDList.add(1033);
						
						//Long intimationStatus = getViewPageObj().getDtoBean().getStatusKey();
						List<Long> rejectionStatus = new ArrayList<Long>();
//						rejectionStatus.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS);
						rejectionStatus.add(ReferenceTable.PREAUTH_REJECT_STATUS);
						rejectionStatus.add(ReferenceTable.ENHANCEMENT_REJECT_STATUS);
						rejectionStatus.add(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);		
						 //rejectionStatus.contains(intimationStatus)
						//To be checked CR2019023
//						if(viewPageObj.getDtoBean().getIsValidationReq() && dtoObj.getComponentId().equals("7_0") && !dtoObj.getScoringBooleanValue() && !isConfirmBoxClicked){
						if(dtoObj.getComponentId().equals("7_0") && !dtoObj.getScoringBooleanValue() && !isConfirmBoxClicked){
							ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Serious Deficiency is selected as \"No\". Do you want to proceed ?",
											"No", "Yes", new ConfirmDialog.Listener() {
												private static final long serialVersionUID = 1L;

												public void onClose(ConfirmDialog dialog) {
													HospitalScoringDTO dtoObj_R = (HospitalScoringDTO)((OptionGroup) event_Rej.getProperty()).getData();
													if (!dialog.isConfirmed()) {
														isConfirmBoxClicked = true;
//														System.out.println("Yes");
														List<HospitalScoringDTO> listOfAllRows = (List<HospitalScoringDTO>) table.getItemIds();
														for(HospitalScoringDTO rec : listOfAllRows){
															if(rec.getSubCategoryKey() != null && rec.getSubCategoryKey() == 8){
																rec.setScoringValue("N");
																rec.setScoringBooleanValue(false);
															}else{
																rec.setScoringBooleanValue(null);
																rec.setScoringValue(null);
															}
														}
														refreshTable();
													} else {
														System.out.println("No");
														setHSValueWithValidation(optionType, event_Rej, dtoObj_R, SDList, MDList);
													}
												}
											});
							dialog.setClosable(false);
						}else{
							setHSValueWithValidation(optionType, event, dtoObj, SDList, MDList);
						}
					}
				});			
				if(viewPageObj !=null && !viewPageObj.getScreenName().equals("ViewPage")){
					optionType.setEnabled(queryDetailsTableDto.getOptionEnabled());
				}else if(correctionView !=null && correctionView.isProposedScoring()){
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
	
	public void setConfirmationValue(boolean argVal){
		
	}

	private void setHSValueWithValidation(
			final OptionGroup optionType,
			ValueChangeEvent event, HospitalScoringDTO dtoObj,
			List<Integer> SDList, List<Integer> MDList) {
		if(dtoObj.getComponentId().endsWith("0")){
			String idStartWith = dtoObj.getComponentId().substring(0, 2);
			List<HospitalScoringDTO> listOfAllRows = (List<HospitalScoringDTO>) table.getItemIds();
			boolean isSDFlag = false;
			for(HospitalScoringDTO rec : listOfAllRows){
				if(rec.getComponentId().startsWith(idStartWith) && !rec.getComponentId().endsWith("0")){
					/*if(dtoObj.getScoringBooleanValue()){
						dtoObj.setScoringValue("Y");
						rec.setScoringBooleanValue(true);
						rec.setScoringValue("Y");
						rec.setOptionEnabled(false);
					}else{
						dtoObj.setScoringValue("N");
						rec.setScoringBooleanValue(null);
						rec.setScoringValue(null);
						rec.setOptionEnabled(true);
					}*/
					dtoObj.setScoringValue("Y");
					dtoObj.setOptionEnabled(true);
					if(dtoObj.getScoringBooleanValue()){
						isSDFlag = true;
//						System.out.println("Serious Deficiency Yes");
						if(SDList.contains(rec.getActualSubCategoryId().intValue())){
//							dtoObj.setScoringValue(null);
							rec.setScoringBooleanValue(null);
							rec.setScoringValue(null);
							rec.setOptionEnabled(true);
						}else{
//							System.out.println("Going in the Else condition SD Yes");
						}
					}else{
						isSDFlag = false;
						dtoObj.setScoringValue("N");
						dtoObj.setOptionEnabled(true);
//						System.out.println("Serious Deficiency No");
						if(SDList.contains(rec.getActualSubCategoryId().intValue())){
//							dtoObj.setScoringValue(null);
							rec.setScoringBooleanValue(null);
							rec.setScoringValue(null);
							rec.setOptionEnabled(false);
						}else{
//							System.out.println("Going in the Else condition MD Yes");
						}
					}
					

				}
			}
			
			if(isSDFlag){
				for(HospitalScoringDTO rec : listOfAllRows){
					if(rec.getActualSubCategoryId() != null){
						if(MDList.contains(rec.getActualSubCategoryId().intValue())){
//							dtoObj.setScoringValue(null);
							rec.setScoringBooleanValue(null);
							rec.setScoringValue(null);
							rec.setOptionEnabled(false); // disabling the Moderate Deficiency Child elements
						}
					}
				}
			}else{
				for(HospitalScoringDTO rec : listOfAllRows){
					if(rec.getActualSubCategoryId() != null){
						if(MDList.contains(rec.getActualSubCategoryId().intValue())){
//							dtoObj.setScoringValue(null);
							rec.setScoringBooleanValue(null);
							rec.setScoringValue(null);
							rec.setOptionEnabled(true); // enabling the Moderate Deficiency Child elements
						}
					}
				}
			}
			refreshTable();						
		}else{
			Boolean selectedValue = (Boolean)event.getProperty().getValue();
			List<HospitalScoringDTO> listOfAllRows = (List<HospitalScoringDTO>) table.getItemIds();
			// For SD Patient not found/Fake Admission
			if(dtoObj.getComponentId().equals("7_1")){
				if(selectedValue.booleanValue()){
					List<Integer> idz = new ArrayList<Integer>();
					idz.add(1026);
					idz.add(1027);
					idz.add(1034);
					//1025,1026,1027
					optionType.select(Boolean.valueOf("true"));
					dtoObj.setScoringBooleanValue(true);
					dtoObj.setScoringValue("Y");
					for(HospitalScoringDTO rec : listOfAllRows){
						if(idz.contains(rec.getActualSubCategoryId())){
							rec.setScoringBooleanValue(false);
							rec.setScoringValue("N");
						}
					}
				}else{
					optionType.select(Boolean.valueOf("false"));
					dtoObj.setScoringBooleanValue(false);
					dtoObj.setScoringValue("N");
				}
			}else if(dtoObj.getComponentId().equals("7_2")){
				if(selectedValue.booleanValue()){
					List<Integer> idz = new ArrayList<Integer>();
					idz.add(1025);
					idz.add(1027);
					idz.add(1034);
					//1025,1026,1027
					optionType.select(Boolean.valueOf("true"));
					dtoObj.setScoringBooleanValue(true);
					dtoObj.setScoringValue("Y");
					for(HospitalScoringDTO rec : listOfAllRows){
						if(idz.contains(rec.getActualSubCategoryId())){
							rec.setScoringBooleanValue(false);
							rec.setScoringValue("N");
						}
					}
				}else{
					optionType.select(Boolean.valueOf("false"));
					dtoObj.setScoringBooleanValue(false);
					dtoObj.setScoringValue("N");
				}
			}else if(dtoObj.getComponentId().equals("7_3")){
				if(selectedValue.booleanValue()){
					List<Integer> idz = new ArrayList<Integer>();
					idz.add(1025);
					idz.add(1026);
					idz.add(1034);
					//1025,1026,1027
					optionType.select(Boolean.valueOf("true"));
					dtoObj.setScoringBooleanValue(true);
					dtoObj.setScoringValue("Y");
					for(HospitalScoringDTO rec : listOfAllRows){
						if(idz.contains(rec.getActualSubCategoryId())){
							rec.setScoringBooleanValue(false);
							rec.setScoringValue("N");
						}
					}
				}else{
					optionType.select(Boolean.valueOf("false"));
					dtoObj.setScoringBooleanValue(false);
					dtoObj.setScoringValue("N");
				}
			}else if(dtoObj.getComponentId().equals("7_4")){
				if(selectedValue.booleanValue()){
					List<Integer> idz = new ArrayList<Integer>();
					idz.add(1025);
					idz.add(1026);
					idz.add(1027);
					//1025,1026,1027
					optionType.select(Boolean.valueOf("true"));
					dtoObj.setScoringBooleanValue(true);
					dtoObj.setScoringValue("Y");
					for(HospitalScoringDTO rec : listOfAllRows){
						if(idz.contains(rec.getActualSubCategoryId())){
							rec.setScoringBooleanValue(false);
							rec.setScoringValue("N");
						}
					}
				}else{
					optionType.select(Boolean.valueOf("false"));
					dtoObj.setScoringBooleanValue(false);
					dtoObj.setScoringValue("N");
				}
			}else{
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
			
			refreshTable();
			
			/*
//			if(dtoObj.getScoringName().equals("ANH Package – For ANH / Index Price/SOC/Market Rate  - For NANH")){
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
		*/
			}
	}

}
