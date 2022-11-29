package com.shaic.reimbursement.reassigninvestigation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.MasterService;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.MasPrivateInvestigator;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorPresenter;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ReAssignMutiInvestigatorTable extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private MasterService masterService;
	
	@EJB
	private PreMedicalService premedicalService;

	
	private Map<AssignInvestigatorDto, HashMap<String, AbstractField<?>>> tableItem = new HashMap<AssignInvestigatorDto, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<AssignInvestigatorDto> data;
	
	private Table table;
	
	AssignInvestigatorDto bean;
	
	private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
	public TextField dummyField;
	
	private BeanItemContainer<SelectValue> stateContainer;
	
	private BeanItemContainer<SelectValue> cityContainer;
	
	private BeanItemContainer<SelectValue> NameContainer;
	
	private BeanItemContainer<SelectValue> allocationToContainer;
	
	private BeanItemContainer<SelectValue> zoneContainer;
	
	private BeanItemContainer<SelectValue> invescoordinatorContainer;
	
	private BeanItemContainer<SelectValue> privateInvestigatorContainer;
	
	private BeanItemContainer<SelectValue> investigatorNameContainer;
	
	private WeakHashMap<Long, Object> privateInvestContactMap;
	
	private WeakHashMap<Long, Object> contactMap;

	private WeakHashMap<Long, Integer> countMap;
	
//	private Button btnAdd;

	private Validator validator;

	public TextField listenerField = new TextField();

	@SuppressWarnings("deprecation")
	public VerticalLayout init(AssignInvestigatorDto bean) {
		this.bean = bean;
		
		stateContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(this.bean.getStateList() != null && ! this.bean.getStateList().isEmpty()){
			stateContainer.addAll(this.bean.getStateList());
		}	
		
		cityContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(this.bean.getCityList() != null && ! this.bean.getCityList().isEmpty()){
			cityContainer.addAll(this.bean.getCityList());
		}	 
		
		
		allocationToContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(this.bean.getAllocationToIdList() != null && ! this.bean.getAllocationToIdList().isEmpty()){
			allocationToContainer.addAll(this.bean.getAllocationToIdList());
		}
		
		NameContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		investigatorNameContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		List<TmpInvestigation> tmpInvestigationList = bean.getInvestigatorNameList();
		
		if(tmpInvestigationList != null && !tmpInvestigationList.isEmpty()){
			contactMap = new WeakHashMap<Long, Object>();
			countMap = new WeakHashMap<Long, Integer>();
			for (TmpInvestigation tmpInvestigation : tmpInvestigationList) {
				
				NameContainer.addBean(new SelectValue(tmpInvestigation.getKey(), tmpInvestigation.getInvestigatorName(),tmpInvestigation.getInvestigatorCode()));
				investigatorNameContainer.addBean(new SelectValue(tmpInvestigation.getKey(), tmpInvestigation.getInvestigatorName(),tmpInvestigation.getInvestigatorCode()));
				List<Long> contactList = new ArrayList<Long>();
				contactList.add(tmpInvestigation.getMobileNumber());
				contactList.add(tmpInvestigation.getPhoneNumber());
				contactMap.put(tmpInvestigation.getKey(),contactList);
				countMap.put(tmpInvestigation.getKey(), Integer.valueOf(tmpInvestigation.getMaxCount()));
			}
		}
		
		List<MasPrivateInvestigator> privateInvesList = bean.getPrivateInvestigatorsList();
		
		if(privateInvesList != null && !privateInvesList.isEmpty()){
			privateInvestContactMap = new WeakHashMap<Long, Object>();
			for (MasPrivateInvestigator masPrivateInvestigator : privateInvesList) {
				NameContainer.addBean(new SelectValue(masPrivateInvestigator.getPrivateInvestigationKey(),masPrivateInvestigator.getInvestigatorName()));
				List<Long> privateInvstContactList = new ArrayList<Long>();
				privateInvstContactList.add(masPrivateInvestigator.getMobileNumberOne());
				privateInvstContactList.add(masPrivateInvestigator.getMobileNumberTwo());
				privateInvestContactMap.put(masPrivateInvestigator.getPrivateInvestigationKey(), privateInvstContactList);
			}
		}
		
		zoneContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(this.bean.getZonalList() != null){
			zoneContainer.addAll(this.bean.getZonalList());
		}
		
		privateInvestigatorContainer = new BeanItemContainer<SelectValue>(SelectValue.class);

			//Commented below condition for Jira - IMSSUPPOR-35888
			/*if(this.bean.getAllocationToSelectValue() != null && this.bean.getAllocationToSelectValue().getValue().equalsIgnoreCase("Private")){
				
				if(privateInvesList != null && !privateInvesList.isEmpty()){
					for (MasPrivateInvestigator masPrivateInvestigator : privateInvesList) {
						if(this.bean.getCoordinatorSelectValue().getValue() != null &&
								this.bean.getCoordinatorSelectValue().getValue().equalsIgnoreCase(masPrivateInvestigator.getCordinatorName())){
							privateInvestigatorContainer.addBean(new SelectValue(masPrivateInvestigator.getPrivateInvestigationKey(),masPrivateInvestigator.getInvestigatorName()));
						}
					}
				}
			} else {
				if(tmpInvestigationList != null && !tmpInvestigationList.isEmpty()){
					for (TmpInvestigation tmpInvestigation : tmpInvestigationList) {
						privateInvestigatorContainer.addBean(new SelectValue(tmpInvestigation.getKey(), tmpInvestigation.getInvestigatorName(),tmpInvestigation.getInvestigatorCode()));
					}
				}
			}*/
		// Add below condition for Jira - IMSSUPPOR-35888
		if(privateInvesList != null && !privateInvesList.isEmpty()){
			for (MasPrivateInvestigator masPrivateInvestigator : privateInvesList) {
				privateInvestigatorContainer.addBean(new SelectValue(masPrivateInvestigator.getPrivateInvestigationKey(),masPrivateInvestigator.getInvestigatorName()));
			}
		}
		
				
		//this.procedureList = procedureList;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
//		btnAdd = new Button();
//		btnAdd.setStyleName("link");
//		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
//		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
//		btnLayout.setWidth("100%");
//		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
       	initTable();
		
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());

		layout.addComponent(table);

		return layout;
	}

	void initTable() {
		// Create a data source and bind it to a table
		data = new BeanItemContainer<AssignInvestigatorDto>(AssignInvestigatorDto.class);
		data.addBean(bean);
		table = new Table("", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());

		table.setHeight("160px");
		table.setWidth("75%");
		
		table.setVisibleColumns(new Object[] {
				"stateSelectValue", "citySelectValue", "allocationToSelectValue",  "zoneSelectValue", "coordinatorSelectValue", "investigatorNameListSelectValue", "investigatorTelNo", "investigatorMobileNo","reassignComment"});

		table.setColumnHeader("stateSelectValue", "State");
		table.setColumnHeader("citySelectValue", "City");
		table.setColumnHeader("allocationToSelectValue", "Allocation");
		table.setColumnHeader("zoneSelectValue", "Zone");
		table.setColumnHeader("coordinatorSelectValue", "Investigation Coordinator");
		table.setColumnHeader("investigatorNameListSelectValue", "Investigator Name");
		table.setColumnHeader("investigatorTelNo", "investigator Telephone No");
		table.setColumnHeader("investigatorMobileNo", "investigator Mobile No");
		table.setColumnHeader("reassignComment", "Re-assign Comment");
		
		table.setColumnWidth("stateSelectValue", 180);
		table.setColumnWidth("citySelectValue", 180);
		table.setColumnWidth("allocationToSelectValue", 120);
		table.setColumnWidth("zoneSelectValue", 120);
		table.setColumnWidth("coordinatorSelectValue", 320);
		table.setColumnWidth("investigatorNameListSelectValue", 320);
		table.setColumnWidth("investigatorTelNo", 158);
		table.setColumnWidth("investigatorMobileNo", 148);
		
		table.setEditable(true);

		table.setTableFieldFactory(new ImmediateFieldFactory());

	}
	
	
	protected void manageListeners() {

		for (AssignInvestigatorDto multiInvestTableDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(multiInvestTableDTO);
			
			final ComboBox stateCombo = (ComboBox) combos.get("stateSelectValue");
			final ComboBox cityCombo = (ComboBox) combos.get("citySelectValue");
			final ComboBox allocationCombo = (ComboBox) combos.get("allocationToSelectValue");
			final ComboBox investNameCombo = (ComboBox) combos.get("investigatorNameListSelectValue");
			
//			Long stateKey = multiInvestTableDTO.getStateSelectValue().getId();
//			Long cityKey = multiInvestTableDTO.getCitySelectValue().getId();
//			Long allocationKey = multiInvestTableDTO.getAllocationToSelectValue().getId();
//			Long investKey = multiInvestTableDTO.getInvestigatorNameListSelectValue().getId();
			
			
			addState(stateCombo,multiInvestTableDTO.getStateSelectValue());
			
			if(multiInvestTableDTO.getStateSelectValue() != null){
				addCity(multiInvestTableDTO.getStateSelectValue().getId(), cityCombo,
						multiInvestTableDTO.getCitySelectValue());
			}
			
			if (multiInvestTableDTO.getStateSelectValue() != null && multiInvestTableDTO.getCitySelectValue() != null) {
				if (allocationCombo != null) {
					addAllocationTo(allocationCombo,
							multiInvestTableDTO.getAllocationToSelectValue());
				}
			}
			
			
//			if (stateKey != null) {
//				addCity(stateKey,
//						cityCombo, multiInvestTableDTO.getCitySelectValue());
//			}
//			
//			if (cityKey != null) {
//				addAllocationTo(stateKey, cityKey,
//						allocationCombo, multiInvestTableDTO.getAllocationToSelectValue());
//			}
//			
//			if (allocationKey != null) {
//				addInvestigator(stateKey, cityKey,allocationKey, 
//						investNameCombo, multiInvestTableDTO.getInvestigatorNameListSelectValue());
//			}

		}
	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = 1L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			final AssignInvestigatorDto investigDto = (AssignInvestigatorDto) itemId;

			Map<String, AbstractField<?>> tableRow = null;

			if(tableItem.get(investigDto) == null)
			{
				tableItem.put(investigDto,
						new HashMap<String, AbstractField<?>>());
				
			}
			tableRow = tableItem.get(investigDto);

			if ("stateSelectValue".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("100%");
				tableRow.put("stateSelectValue", box);
				box.setData(investigDto);
				addState(box,investigDto.getStateSelectValue());
				addStateListener(box,investigDto);
				return box;
			} else if ("citySelectValue".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("100%");
				box.setData(investigDto);
				tableRow.put("citySelectValue", box);
				setDefaultCity(investigDto,box,investigDto.getCitySelectValue());
				addCityListener(box, investigDto);
				return box;
			} else if ("allocationToSelectValue".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("100%");
				box.setData(investigDto);
				tableRow.put("allocationToSelectValue", box);
				addAllocationTo(box,investigDto.getAllocationToSelectValue());
				addAllocationToListener(box,investigDto);
				return box;
			}  else if("zoneSelectValue".equals(propertyId)){
				GComboBox box = new GComboBox();
				box.setWidth("100%");
				box.setData(investigDto);
				tableRow.put("zoneSelectValue",box);
				addZonalTo(investigDto.getAllocationToSelectValue().getValue(),box,investigDto.getZoneSelectValue());
				addZonalToListener(box,investigDto);
				return box;	
			} else if("coordinatorSelectValue".equals(propertyId)){
				GComboBox box = new GComboBox();
				box.setWidth("100%");
				if(investigDto.getAllocationToSelectValue() != null
						&& !investigDto.getAllocationToSelectValue().getValue().equalsIgnoreCase("Private")){
					box.setEnabled(false);
					investigDto.setCoordinatorSelectValue(null);
				}
				box.setData(investigDto);
				tableRow.put("coordinatorSelectValue", box);
				if(investigDto.getAllocationToSelectValue() != null
						&& investigDto.getAllocationToSelectValue().getValue().equalsIgnoreCase("Private")){
					addCoordinatorTo(investigDto.getZoneSelectValue().getValue(),box,investigDto.getCoordinatorSelectValue());
				}
				addCoordinatorToListener(box,investigDto);
				return box;	
			} else if ("investigatorNameListSelectValue".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("100%");
				box.setData(investigDto);
				tableRow.put("investigatorNameListSelectValue", box);
				if(investigDto.getAllocationToSelectValue().getValue() != null && investigDto.getAllocationToSelectValue().getValue().equalsIgnoreCase("Private")) {
					setNameContainer(privateInvestigatorContainer,box);
				} else {
					setNameContainer(NameContainer,box);
				}
				addInvestigatorListener(box,investigDto);
				return box;
			} else if ("investigatorTelNo".equals(propertyId)) {
				final TextField field = new TextField();
				field.setWidth("100%");
				field.setNullRepresentation("");
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setEnabled(false);
				field.setData(investigDto);
				tableRow.put("investigatorTelNo", field);
				return field;
			} else if ("investigatorMobileNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setEnabled(false);
				field.setWidth("100%");
				field.setNullRepresentation("");
				field.setData(investigDto);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("investigatorMobileNo", field);
				return field;
			}  else if ("reassignComment".equals(propertyId)) {
				final TextArea field = new TextArea();
				field.setWidth("100%");
				field.setMaxLength(2000);
				field.setNullRepresentation("");
				field.setData(investigDto);
				handleRemarksPopup(field,null);
				tableRow.put("reassignComment", field);
				return field;
			}else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				field.setEnabled(false);
				return field;
			}
		}
	}
	
//	@SuppressWarnings("unchecked")
//	private void addCommonValues(ComboBox stateCombo, String tableColumnName) {
//		
////		stateContainer = (BeanItemContainer<SelectValue>) referenceData
////				.get(tableColumnName);
//		
//		stateCombo.setContainerDataSource(stateContainer);
//		stateCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		stateCombo.setItemCaptionPropertyId("value");
//	}
		
	private void showErrorPopup(String errMsg) {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		Label errMsgLbl = new Label(errMsg, ContentMode.HTML);
		layout.addComponent(errMsgLbl);
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

	public void addState(ComboBox stateCombo,
			SelectValue value) {

//		fireViewEvent(AssignInvestigatorPresenter.GET_STATE, null);

		stateCombo.setContainerDataSource(stateContainer);
		stateCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		stateCombo.setItemCaptionPropertyId("value");

//		for(int i=0;i<stateContainer.size();i++){
//			if (value != null) {
//				if(stateContainer.getIdByIndex(i).getValue().equalsIgnoreCase(value.getValue())){
//					stateCombo.setValue(value);
//					break;
//				}	
//			}
//		}
	}
	
	private void addStateListener(final ComboBox stateCombo,
			AssignInvestigatorDto investigDto) {
		if (stateCombo != null) {
			stateCombo.addListener(new Listener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					AssignInvestigatorDto investigTableDTO = (AssignInvestigatorDto) component
							.getData();
					
					SelectValue selectedState = (SelectValue)component.getValue();
					
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(investigTableDTO);
					System.out.println("---the hashmap---"+hashMap);
					ComboBox cityBox = (ComboBox) hashMap.get("citySelectValue");
					if (selectedState != null) {
							investigTableDTO.setStateSelectValue(selectedState);
							if (selectedState != null && cityBox != null) {
								addCity(selectedState.getId(), cityBox,
										investigTableDTO.getCitySelectValue());
							}
					}
				}
			});			
		}
	}
	
	public void addCity(Long stateKey, ComboBox cityCombo,
			SelectValue value) {

		fireViewEvent(ReAssignInvestigatorPresenter.REASSIGN_GET_CITY, stateKey,cityCombo,value);
		
	}
	
	public void setDefaultCity(AssignInvestigatorDto investigDto,ComboBox cityCombo,
			SelectValue value){
		
		HashMap<String, AbstractField<?>> hashMap = tableItem
				.get(investigDto);
		ComboBox stateBox = (ComboBox) hashMap.get("stateSelectValue");
		SelectValue selectedState = (SelectValue)stateBox.getValue();
		
		if (selectedState != null) {
			investigDto.setStateSelectValue(selectedState);
			if (selectedState != null && cityCombo != null) {
				addCity(selectedState.getId(), cityCombo,value);
			}
		}
	}

	private void addCityListener(final ComboBox cityCombo,
			AssignInvestigatorDto investigDto) {
		if (cityCombo != null) {
			cityCombo.addListener(new Listener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					AssignInvestigatorDto investigTableDTO = (AssignInvestigatorDto) component
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(investigTableDTO);
					System.out.println("---the hashmap---"+hashMap);
					
					SelectValue selectedCity = (SelectValue)component.getValue();
					
					ComboBox stateBox = (ComboBox) hashMap.get("stateSelectValue");
					SelectValue stateSelected = (SelectValue)stateBox.getValue();
					ComboBox allocationBox = (ComboBox) hashMap.get("allocationToSelectValue");
					if (investigTableDTO != null) {
						if (stateSelected != null && selectedCity != null) {
							
							investigTableDTO.setStateSelectValue(stateSelected);
							investigTableDTO.setCitySelectValue(selectedCity);
							
							if (allocationBox != null) {
								addAllocationTo(allocationBox,
										investigTableDTO.getAllocationToSelectValue());
							}
						}
					}
				}
			});			
		}
	}
	public void addAllocationTo(ComboBox allocationToCombo,
			SelectValue value) {
		
//		fireViewEvent(AssignInvestigatorPresenter.GET_ALLOCATION_TO, stateKey,cityKey);
 	    
	  allocationToCombo.setContainerDataSource(allocationToContainer);
 	  allocationToCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
 	  allocationToCombo.setItemCaptionPropertyId("value");

 	
	for(int i=0;i<allocationToContainer.size();i++){
		if (value != null) {
			if(allocationToContainer.getIdByIndex(i).getValue().equalsIgnoreCase(value.getValue())){
				allocationToCombo.setValue(allocationToContainer.getIdByIndex(i));
				break;
			}	
		}
	}
	}

	private void addAllocationToListener(final ComboBox allocationCombo,
			AssignInvestigatorDto investigDto) {
		if (allocationCombo != null) {
			allocationCombo.addListener(new Listener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					AssignInvestigatorDto investigTableDTO = (AssignInvestigatorDto) component
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(investigTableDTO);
					System.out.println("---the hashmap---"+hashMap);
					
					ComboBox stateCmbo = (ComboBox) hashMap.get("stateSelectValue");
					SelectValue selectState = (SelectValue) stateCmbo.getValue();
							
					ComboBox cityCmbo = (ComboBox) hashMap.get("citySelectValue");
					
					SelectValue selectCity = (SelectValue) cityCmbo.getValue();
					
					SelectValue selectedallocationTo = (SelectValue)component.getValue();
					
					investigTableDTO.setStateSelectValue(selectState);
					investigTableDTO.setCitySelectValue(selectCity);
					investigTableDTO.setAllocationToSelectValue(selectedallocationTo);
					
					if(selectedallocationTo != null && !selectedallocationTo.getValue().equalsIgnoreCase("Private")){
						ComboBox zonalCmbo = (ComboBox) hashMap.get("zoneSelectValue");
						/*zonalCmbo.setEnabled(false);
						zonalCmbo.setValue(null);*/
						ComboBox coordinatorComb = (ComboBox) hashMap.get("coordinatorSelectValue");
						coordinatorComb.setEnabled(false);
						coordinatorComb.setValue(null);
						ComboBox investigatorNameComb = (ComboBox) hashMap.get("investigatorNameListSelectValue");
						investigatorNameComb.setValue(null);
						investigTableDTO.setInvestigatorNameList(null);
						addZonalTo(selectedallocationTo.getValue(),zonalCmbo,investigTableDTO.getAllocationToSelectValue());
						setNameContainer(investigatorNameContainer,investigatorNameComb);
						
						} else {
							if(selectedallocationTo != null) {
							ComboBox zonalCmbo = (ComboBox) hashMap.get("zoneSelectValue");
							zonalCmbo.setEnabled(true);
							ComboBox coordinatorComb = (ComboBox) hashMap.get("coordinatorSelectValue");
							coordinatorComb.setEnabled(true);
							ComboBox investigatorNameComb = (ComboBox) hashMap.get("investigatorNameListSelectValue");
							investigatorNameComb.setValue(null);
							investigTableDTO.setInvestigatorNameList(null);
								if(investigTableDTO.getAllocationToSelectValue() != null){
									addZonalTo(selectedallocationTo.getValue(),zonalCmbo,investigTableDTO.getAllocationToSelectValue());
									setNameContainer(privateInvestigatorContainer,investigatorNameComb);
								}
							}
						}
					
//					ComboBox investigCmbo = (ComboBox) hashMap.get("investigatorNameListSelectValue");
//					if (investigTableDTO != null) {
//						if (investigTableDTO.getStateSelectValue() != null) {
//							if (investigCmbo != null) {
//								addInvestigator(selectState.getId(), selectCity.getId(), selectedallocationTo.getId(),  investigCmbo,
//										investigTableDTO.getAllocationToSelectValue());
//							}
//						}
//					}
				}
			});			
		}
	}
	
//	public void addInvestigator(Long stateKey, Long cityKey, Long allocationKey, ComboBox investigatorCombo,
//			SelectValue value) {
//
//		fireViewEvent(AssignInvestigatorPresenter.GET_INVESTIGATOR,
//				stateKey, cityKey, allocationKey,investigatorCombo);
//
//	}
	
	private void addInvestigatorListener(final ComboBox investigCombo,
			final AssignInvestigatorDto investigDto) {
		if (investigCombo != null) {
			investigCombo.addListener(new Listener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					AssignInvestigatorDto investigTableDTO = (AssignInvestigatorDto) component
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(investigTableDTO);
					System.out.println("---the hashmap---"+hashMap);
					
//					SelectValue selectedInvestigator = investigDto.getInvestigatorNameListSelectValue();
					
					SelectValue selectedInvestigator =  (SelectValue) component.getValue();
					
					TextField investigMob = (TextField) hashMap.get("investigatorMobileNo");
					TextField investigTel = (TextField) hashMap.get("investigatorTelNo");
					if (selectedInvestigator != null && selectedInvestigator.getValue() != null && !selectedInvestigator.getValue().equalsIgnoreCase(investigTableDTO.getReassignedFrom())) {
						
						ComboBox stateCmbo = (ComboBox) hashMap.get("stateSelectValue");
						SelectValue selectState = (SelectValue) stateCmbo.getValue();
								
						ComboBox cityCmbo = (ComboBox) hashMap.get("citySelectValue");
						
						SelectValue selectCity = (SelectValue) cityCmbo.getValue();
						
						ComboBox allocationCmbo = (ComboBox) hashMap.get("allocationToSelectValue");
						
						SelectValue selectedAllocation = (SelectValue) allocationCmbo.getValue();
						
						investigTableDTO.setStateSelectValue(selectState);
						investigTableDTO.setCitySelectValue(selectCity);
						investigTableDTO.setAllocationToSelectValue(selectedAllocation);
						
						investigTableDTO.setInvestigatorNameListSelectValue(selectedInvestigator);
						investigTableDTO.setInvestigatorName(selectedInvestigator.getValue());
						
						Integer maxcount = (Integer)countMap.get(selectedInvestigator.getId());
						
						investigTableDTO.setMaxCount(maxcount);
						
						List<Long> contactList = (List<Long>)contactMap.get(selectedInvestigator.getId());
						
						List<Long> privateInvestigatorContactList = (List<Long>)privateInvestContactMap.get(selectedInvestigator.getId());
						
						if(contactList != null &&  !contactList.isEmpty()){
							if (investigMob != null && contactList.get(0) != null) {
									investigMob.setValue(String.valueOf(contactList.get(0)));
									investigTableDTO.setInvestigatorMobileNo(String.valueOf(contactList.get(0)));
							}
							if (investigTel != null && contactList.get(1) != null) {
								investigTel.setValue(String.valueOf(contactList.get(1)));
								investigTableDTO.setInvestigatorTelNo(String.valueOf(contactList.get(1)));
							}
						} else if(privateInvestigatorContactList != null && !privateInvestigatorContactList.isEmpty()){
							if(investigMob != null && privateInvestigatorContactList.get(0) != null){
								investigMob.setValue(String.valueOf(privateInvestigatorContactList.get(0)));
								investigTableDTO.setInvestigatorMobileNo(String.valueOf(privateInvestigatorContactList.get(0)));
							}
							if (investigTel != null && privateInvestigatorContactList.get(1) != null) {
								investigTel.setValue(String.valueOf(privateInvestigatorContactList.get(1)));
								investigTableDTO.setInvestigatorTelNo(String.valueOf(privateInvestigatorContactList.get(1)));
							}
						}
					}
					else{
						if(selectedInvestigator != null) {
							showErrorPopup("Please Re-Assign To Different Investigator");
						}
					}
				}
			});			
		}
	}
	
//	public void setstateContainer(
//			BeanItemContainer<SelectValue> stateSelectValueContainer) {
//		stateContainer = stateSelectValueContainer;
//	}

	public void setCityContainer(
			BeanItemContainer<SelectValue> citySelectValueContainer,ComboBox cityCmbo,SelectValue selectedCity) {
		
		if(cityContainer == null){
			cityContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		}
		
		cityContainer = citySelectValueContainer;
		
		cityCmbo.setContainerDataSource(cityContainer);
		cityCmbo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cityCmbo.setItemCaptionPropertyId("value");

		for(int i=0;i<cityContainer.size();i++){
			if (selectedCity != null) {
				if(cityContainer.getIdByIndex(i).getValue().equalsIgnoreCase(selectedCity.getValue())){
					cityCmbo.setValue(cityContainer.getIdByIndex(i));
					break;
				}	
			}
		}
	}

//	public void setAllocationContainer(
//			BeanItemContainer<SelectValue> allocationSelectValueContainer) {
//		allocationToContainer = allocationSelectValueContainer;
//	}
	
//	public void setInvestigatorDetails(List<TmpInvestigation> tmpInvestigationList, ComboBox investigatorCombo){
//		
//		if(tmpInvestigationList != null && tmpInvestigationList.isEmpty()){
//			contactMap = new WeakHashMap<Long, Object>();
//			for (TmpInvestigation tmpInvestigation : tmpInvestigationList) {
//				
//				NameContainer.addBean(new SelectValue(tmpInvestigation.getKey(), tmpInvestigation.getInvestigatorName()));
//				List<Long> contactList = new ArrayList<Long>();
//				contactList.add(tmpInvestigation.getMobileNumber());
//				contactList.add(tmpInvestigation.getPhoneNumber());
//				contactMap.put(tmpInvestigation.getKey(),contactList); 
//			}
//			setNameContainer(NameContainer,investigatorCombo);
//		}
//	}
	
	public void setNameContainer(
			BeanItemContainer<SelectValue> nameSelectValueContainer,ComboBox investigatorCombo) {
		
		AssignInvestigatorDto investigDto = (AssignInvestigatorDto) investigatorCombo.getData();
		NameContainer = nameSelectValueContainer;
		
		investigatorCombo.setContainerDataSource(NameContainer);
		investigatorCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		investigatorCombo.setItemCaptionPropertyId("value");
		
		if(investigDto.getInvestigatorNameListSelectValue() != null){
		
			for (int i = 0; i < NameContainer.size(); i++) {
				if (NameContainer.getIdByIndex(i).getValue()
						.equalsIgnoreCase(investigDto.getInvestigatorNameListSelectValue().getValue())) {
					investigatorCombo.setValue(NameContainer.getIdByIndex(i));
					break;
				}
			}
		}
	}
	
	public void setCoordinatorContainer(BeanItemContainer<SelectValue> coordinatorValues,ComboBox combCoordinator,SelectValue selectedCoordinator){
		
		if(invescoordinatorContainer != null){
			invescoordinatorContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		}
		invescoordinatorContainer = coordinatorValues;
		combCoordinator.setContainerDataSource(invescoordinatorContainer);
		combCoordinator.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		combCoordinator.setItemCaptionPropertyId("value");
		if(invescoordinatorContainer.size()<=1){
			
		}
		
		for(int i=0;i <invescoordinatorContainer.size();i++){
			if(selectedCoordinator != null && selectedCoordinator.getValue() != null && !selectedCoordinator.getValue().isEmpty()){
				if(invescoordinatorContainer.getIdByIndex(i).getValue().equalsIgnoreCase(selectedCoordinator.getValue())){
					combCoordinator.setValue(invescoordinatorContainer.getIdByIndex(i));
					break;
				}
			}
		}
	}
	
	public void setPrivateInvestigatorNameContainer(BeanItemContainer<SelectValue> investigatorNameValues,ComboBox combInvestigator,SelectValue selectedInvestigator){
		if(NameContainer != null){
			NameContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		}
		NameContainer = investigatorNameValues;
		if(NameContainer != null && !NameContainer.getItemIds().isEmpty()){
		combInvestigator.setContainerDataSource(NameContainer);
		combInvestigator.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		combInvestigator.setItemCaptionPropertyId("value");
		for(int i=0;i<NameContainer.size();i++){
			if(selectedInvestigator != null && selectedInvestigator.getValue() != null && !selectedInvestigator.getValue().isEmpty()) {
				if(NameContainer.getIdByIndex(i).getValue().equalsIgnoreCase(selectedInvestigator.getValue())){
					combInvestigator.setValue(NameContainer.getIdByIndex(i));
					break;
				}
			}
			}
		}
		
	}
	
	public void setZonesContainer(BeanItemContainer<SelectValue> zoneNameValues,ComboBox cmbZone,SelectValue zoneSelected){
		if(zoneContainer != null){
			zoneContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		}
		zoneContainer = zoneNameValues;
		cmbZone.setContainerDataSource(zoneContainer);
		cmbZone.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbZone.setItemCaptionPropertyId("value");
		for(int i=0;i<zoneContainer.size();i++){
			if(zoneSelected != null && zoneSelected.getValue() != null && !zoneSelected.getValue().isEmpty()){
				if(zoneContainer.getIdByIndex(i).getValue().equalsIgnoreCase(zoneSelected.getValue())){
					cmbZone.setValue(zoneContainer.getIdByIndex(i));
				}
			}
		}
	}
	
	public void addZonalTo(String selectesAllocationTo,ComboBox zonal,SelectValue value){
		
	 	fireViewEvent(ReAssignInvestigatorPresenter.GET_REASSIGN_ZONE, selectesAllocationTo,zonal,value);
	}
	
	public void addZonalToListener(final ComboBox zonalValue,AssignInvestigatorDto investigationDto){
		
		if(zonalValue != null){

			zonalValue.addListener(new Listener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					AssignInvestigatorDto investigTableDTO = (AssignInvestigatorDto) component
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(investigTableDTO);
					System.out.println("---the hashmap---"+hashMap);
					
					ComboBox stateCmbo = (ComboBox) hashMap.get("stateSelectValue");
					SelectValue selectState = (SelectValue) stateCmbo.getValue();
							
					ComboBox cityCmbo = (ComboBox) hashMap.get("citySelectValue");
					
					SelectValue selectCity = (SelectValue) cityCmbo.getValue();
					
					SelectValue selectedallocationTo = (SelectValue)component.getValue();
					
					investigTableDTO.setStateSelectValue(selectState);
					investigTableDTO.setCitySelectValue(selectCity);
					investigTableDTO.setZoneSelectValue(selectedallocationTo);
					
					ComboBox zonalCmbo = (ComboBox) hashMap.get("zoneSelectValue");
					SelectValue selectedZone = (SelectValue) zonalCmbo.getValue();
					System.out.println("Coordinator Select Value*********************"+ hashMap.get("coordinatorSelectValue"));
					ComboBox coordinatorComb = (ComboBox) hashMap.get("coordinatorSelectValue");
					ComboBox investigatorNameComb = (ComboBox) hashMap.get("investigatorNameListSelectValue");
					ComboBox allocationCmbo = (ComboBox) hashMap.get("allocationToSelectValue");
					SelectValue selectedAllocation = (SelectValue) allocationCmbo.getValue();
//					investigatorNameComb.setValue(null);
					investigTableDTO.setInvestigatorNameList(null);
					if(selectedAllocation != null && selectedAllocation.getValue().equalsIgnoreCase("Private")){
						if(selectedZone != null){
							ComboBox cordComb=new ComboBox();
							addCoordinatorTo(selectedZone.getValue(),cordComb,investigTableDTO.getCoordinatorSelectValue());
						}
					}
				}
			});			
		
		}
		
	}
	
	public void addCoordinatorTo(String selectedZone,ComboBox coordinatorValue,SelectValue value){

		fireViewEvent(ReAssignInvestigatorPresenter.GET_REASSIGN_COORDINATOR, selectedZone,coordinatorValue,value);
	
	}
	
	public void addCoordinatorToListener(final ComboBox cooridnator,AssignInvestigatorDto investigationDto){
		if(cooridnator != null){
			cooridnator.addListener(new Listener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					AssignInvestigatorDto investigTableDTO = (AssignInvestigatorDto) component
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(investigTableDTO);
					System.out.println("---the hashmap---"+hashMap);
					
					ComboBox stateCmbo = (ComboBox) hashMap.get("stateSelectValue");
					SelectValue selectState = (SelectValue) stateCmbo.getValue();
							
					ComboBox cityCmbo = (ComboBox) hashMap.get("citySelectValue");
					
					SelectValue selectCity = (SelectValue) cityCmbo.getValue();
					
					SelectValue selectedallocationTo = (SelectValue)component.getValue();
					
					investigTableDTO.setStateSelectValue(selectState);
					investigTableDTO.setCitySelectValue(selectCity);
					investigTableDTO.setCoordinatorSelectValue(selectedallocationTo);
					
					ComboBox allocationCmbo = (ComboBox) hashMap.get("allocationToSelectValue");
					
					SelectValue selectedAllocation = (SelectValue) allocationCmbo.getValue();
					
					ComboBox selectedCord = (ComboBox) hashMap.get("coordinatorSelectValue");
					SelectValue selectCordValue = (SelectValue) selectedCord.getValue();
					ComboBox privateInvestigatorComb = (ComboBox) hashMap.get("investigatorNameListSelectValue");
//					SelectValue investigatorValue = (SelectValue)privateInvestigatorComb.getValue();
					if(selectCordValue != null && selectedAllocation != null && selectedAllocation.getValue().equalsIgnoreCase("Private")){
						addPrivateInvestigator(selectCordValue.getCommonValue(),privateInvestigatorComb,investigTableDTO.getInvestigatorNameListSelectValue());
					} else if(selectCordValue == null && selectedAllocation != null && selectedAllocation.getValue().equalsIgnoreCase("Private")){
						List<MasPrivateInvestigator> privateInvesList = bean.getPrivateInvestigatorsList();
						if(privateInvesList != null && !privateInvesList.isEmpty()){
							for (MasPrivateInvestigator masPrivateInvestigator : privateInvesList) {
								privateInvestigatorContainer.addBean(new SelectValue(masPrivateInvestigator.getPrivateInvestigationKey(),masPrivateInvestigator.getInvestigatorName()));
							}
						}
						setNameContainer(privateInvestigatorContainer,privateInvestigatorComb);
					} else {
						setNameContainer(investigatorNameContainer,privateInvestigatorComb);
					}
				}
			});			
		
		
		}
	}
	
	public void addPrivateInvestigator(String coordinatorselectValue,ComboBox combInvestigator,SelectValue investigatorSelectValue){
		
		fireViewEvent(ReAssignInvestigatorPresenter.GET_REASSIGN_PRIVATE_INVESTIGATOR, coordinatorselectValue,combInvestigator,investigatorSelectValue);
	}
	
	public List<AssignInvestigatorDto> getValues() {
		@SuppressWarnings("unchecked")
		List<AssignInvestigatorDto> itemIds = (List<AssignInvestigatorDto>) this.table
				.getItemIds();
		if(itemIds.isEmpty()) {
			itemIds = new ArrayList<AssignInvestigatorDto>();
		}
		return itemIds;
	}
	
	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(AssignInvestigatorDto investigDTO) {
		data.addItem(investigDTO);
		manageListeners();
	}

	public boolean isValid() {
		boolean hasError = false;
		errorMessages = new ArrayList<String>();
		if(table != null && table.getItemIds() != null){
			@SuppressWarnings("unchecked")
			Collection<AssignInvestigatorDto> itemIds = (Collection<AssignInvestigatorDto>) table
				.getItemIds();

			for (AssignInvestigatorDto bean : itemIds) {
	
				if(bean.getAllocationToSelectValue() == null
						|| (bean.getAllocationToSelectValue() != null && bean.getAllocationToSelectValue().getId() == null)){
				 	hasError = true;
				 	errorMessages.add("Please Select Allocation Details");
					break;
				} else if(bean.getZoneSelectValue() == null
						|| (bean.getZoneSelectValue() != null && bean.getZoneSelectValue().getId() == null)) {
					hasError = true;
					errorMessages.add("Please Select Zone Details");
					break;
				}  else if (bean.getInvestigatorNameListSelectValue() == null
						|| (bean.getInvestigatorNameListSelectValue()  != null
						&& bean.getInvestigatorNameListSelectValue().getId() == null)) {
					hasError = true;
					errorMessages.add("Please Select Investigator Name Details");
					break;
				}
				if (bean.getInvestigatorNameListSelectValue()  != null && bean.getInvestigatorNameListSelectValue().getValue() != null
						&& bean.getInvestigatorNameListSelectValue().getValue().equalsIgnoreCase(bean.getReassignedFrom())) {
					hasError = true;
					errorMessages.add("Please Select a Different Investigator Name");
					break;
				}
			}
		}	
		else{
			errorMessages.add("Please Re-assign to a Different Investigator");
			hasError = true;
		}
		
		return !hasError;
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}
	
	private Converter<Object, Object> getConverter(final Object object) {
		return new Converter<Object, Object>() {

			@Override
			public Object convertToModel(Object itemId,
					Class<? extends Object> targetType, Locale locale)
					throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
				if (itemId != null) {
					IndexedContainer c = (IndexedContainer) object;
					Object propertyId = c.getContainerPropertyIds().iterator()
							.next();
					
					Object name = c.getItem(itemId).getItemProperty(propertyId)
							.getValue();
					return (Object) name;
					
				}
				
				return null;
			}

			@Override
			public Object convertToPresentation(Object value,
					Class<? extends Object> targetType, Locale locale)
					throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
				if (value != null) {
					IndexedContainer c = (IndexedContainer) object;
					Object propertyId = c.getContainerPropertyIds().iterator()
							.next();
					for (Object itemId : c.getItemIds()) {
						Object name = c
								.getContainerProperty(itemId, propertyId)
								.getValue();
						if (value.equals(name)) {
							return itemId;
						}
					}
				}
				return null;
			}

			@Override
			public Class<Object> getModelType() {
				// TODO Auto-generated method stub
				return Object.class;
				
			}

			@Override
			public Class<Object> getPresentationType() {
				// TODO Auto-generated method stub
				return Object.class;
			}
		};
	}
	
	
	private Converter<Object, String> getCustomConverter(final Object object) {
		return new Converter<Object, String>() {

			@Override
			public String convertToModel(Object itemId,
					Class<? extends String> targetType, Locale locale)
					throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
				if (itemId != null) {
					IndexedContainer c = (IndexedContainer) object;
					Object propertyId = c.getContainerPropertyIds().iterator()
							.next();
					
					Object name = c.getItem(itemId).getItemProperty(propertyId)
							.getValue();
					SelectValue selValue = (SelectValue)name;
					return (String) selValue.getValue();
					
				}
				return null;
			}
			
			@Override
			public Object convertToPresentation(String value,
					Class<? extends Object> targetType, Locale locale)
					throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
				if (value != null) {
					IndexedContainer c = (IndexedContainer) object;
					Object propertyId = c.getContainerPropertyIds().iterator()
							.next();
					for (Object itemId : c.getItemIds()) {
						Object name = c
								.getContainerProperty(itemId, propertyId)
								.getValue();
						if (value.equals(name)) {
							return itemId;
						}
					}
				}
				return null;
			}

			@Override
			public Class<String> getModelType() {
				// TODO Auto-generated method stub
				return String.class;
				
			}

			@Override
			public Class<Object> getPresentationType() {
				// TODO Auto-generated method stub
				return Object.class;
			}

		};
	}
	
	public void clearObject(){
		setClearTableItem(tableItem);
		SHAUtils.setClearReferenceData(referenceData);
		masterService = null;
		premedicalService = null;
		data = null;
		errorMessages = null;
		cityContainer = null;
		allocationToContainer = null;
		NameContainer = null;
	}	

	public void setClearTableItem(Map<AssignInvestigatorDto, HashMap<String, AbstractField<?>>> referenceData){
		
		if(referenceData != null){
    	
	    	Iterator<Entry<AssignInvestigatorDto, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
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
	
public  void handleRemarksPopup(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForRemarks(searchField, getShortCutListenerForRemarks(searchField));
	    
	  }
	
	public  void handleShortcutForRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);
				
			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}	
	
	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Re-assign Remarks",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				AssignInvestigatorDto searchTableDto = (AssignInvestigatorDto) txtFld.getData();
				VerticalLayout vLayout =  new VerticalLayout();
				
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(2000);
				txtArea.setData(bean);
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				
				txtArea.setRows(25);
				txtArea.setReadOnly(false);
				
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
							AssignInvestigatorDto mainDto = (AssignInvestigatorDto)txtFld.getData();
							mainDto.setReassignComment(txtFld.getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				final Window dialog = new Window();
				String strCaption = "Re-Assign Remarks";
				dialog.setCaption(strCaption);
				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);
				
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);
				
				dialog.addCloseListener(new Window.CloseListener() {
					
					@Override
					public void windowClose(CloseEvent e) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}
}

