/**
 * 
 */
package com.shaic.claim.outpatient.processOPpages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.test.DiagnosisComboBox;
import com.shaic.arch.test.SuggestingContainer;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.premedical.listenerTables.SpecialityTableListener.ImmediateFieldFactory;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

/**
 * @author ntv.narasimhaj
 *
 */
public class OPSpecialityListenerTable extends ViewComponent{
	
	private Map<OPSpecialityDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<OPSpecialityDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<OPSpecialityDTO> data = new BeanItemContainer<OPSpecialityDTO>(
			OPSpecialityDTO.class);
	
	private Table table;
	
	private Button btnAdd;
	
	private Map<String, Object> referenceData;

	private BeanItemContainer<SelectValue> procedure;
	
	private String presenterString;
	
	public List<OPSpecialityDTO> deletedDTO;
	
	private OutPatientDTO bean;
	
	private List<String> errorMessages;
	
	private Validator validator;
	
	private BeanItemContainer<SelectValue> procedCode;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private PreauthService preauthService;
	
	public void init(OutPatientDTO bean){
		this.bean = bean;
		this.presenterString = presenterString;
		deletedDTO = new ArrayList<OPSpecialityDTO>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(btnLayout);
		layout.setMargin(true);
		initTable(layout);
		
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());

		addListener();

		layout.addComponent(table);

		setCompositionRoot(layout);
		
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				OPSpecialityDTO specialityDto = new OPSpecialityDTO();
				BeanItem<OPSpecialityDTO> addItem = data
						.addItem(specialityDto);
			}
		});
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout){
		
		table = new Table("Speciality", data);
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
						table.removeItem(currentItemId);
			        } 
			    });
		    	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		        return deleteButton;
		      }
		    });
		
		/*table.removeGeneratedColumn("addProcedure");
		table.addGeneratedColumn("addProcedure", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button addProcedure = new Button("");
				final OPSpecialityDTO dto = (OPSpecialityDTO) itemId;
				if (dto.getEnableOrDisable() != null) {
					addProcedure.setEnabled(true);
				}
				addProcedure.setEnabled(true);
				addProcedure.setIcon(FontAwesome.FILE);
				addProcedure.setData(itemId);
				addProcedure.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						
						Window popup = new com.vaadin.ui.Window();
						popup.setCaption("Add New Procedure");
						popup.setWidth("30%");
						popup.setHeight("30%");
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
						
						final ComboBox cmbBox = (ComboBox) hashMap.get("ped");
						
						addProcedurePopUp.init(dto,cmbBox,popup);
						popup.setContent(addProcedurePopUp);
						popup.setClosable(true);
						popup.center();
						popup.setResizable(false);
						popup.addCloseListener(new Window.CloseListener() {
							*//**
							 * 
							 *//*
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
				return addProcedure;
			}
		});*/
		
		table.setVisibleColumns(new Object[] { "specialityType","pedfromPolicy","ped","remarks","Delete" });
		table.setColumnHeader("specialityType", "Speciality");
		table.setColumnHeader("pedfromPolicy", "PED (identified) from Policy");
		table.setColumnHeader("ped", "PED");
		table.setColumnHeader("remarks", "Remarks");
		table.setEditable(true);
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
	}
	
public class ImmediateFieldFactory extends DefaultFieldFactory {
		
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			
			OPSpecialityDTO specialityDTO = (OPSpecialityDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(specialityDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(specialityDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(specialityDTO);
			}
			
			if("specialityType".equals(propertyId)){
				GComboBox box = new GComboBox();
				addSpecialityNames(box);
				tableRow.put("specialityType", box);
				final ComboBox procedureValue = (ComboBox) tableRow.get("procedure");
				box.setData(specialityDTO);
//				addProcedureNameListener(box, procedureValue);
				return box;
			} else if("pedfromPolicy".equals(propertyId)){
				TextField pedFromPolicy = new TextField();
				pedFromPolicy.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//				pedFromPolicy.setReadOnly(true);
				pedFromPolicy.setNullRepresentation("");
//				pedFromPolicy.setValue("NIL");
				if(bean.getPolicyPed() != null){
					specialityDTO.setPedfromPolicy(bean.getPolicyPed());
				}else {
					specialityDTO.setPedfromPolicy("");
				}
				tableRow.put("pedfromPolicy", pedFromPolicy);
				pedFromPolicy.setData(specialityDTO);
				return pedFromPolicy;
				
			} else if("ped".equals(propertyId)){
				GComboBox box = new GComboBox();
				addPedValues(box);
				/*DiagnosisComboBox addbox = new DiagnosisComboBox();
				SuggestingContainer diagnosisContainer = new SuggestingContainer(masterService);
				box.setContainerDataSource(diagnosisContainer);
//				box.setEnabled(isEnabled);
				box.setFilteringMode(FilteringMode.CONTAINS);
				box.setTextInputAllowed(true);
				box.setNullSelectionAllowed(true);
				box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				box.setItemCaptionPropertyId("value");
				box.setNewItemsAllowed(true);*/
				tableRow.put("procedure", box);
				box.setData(specialityDTO);
				return box;
			} else if("remarks".equals(propertyId)){
				TextField remarks = new TextField();
				remarks.setNullRepresentation("");
				if(specialityDTO.getRemarks() != null){
					remarks.setValue(specialityDTO.getRemarks());
				}
				return remarks;
				
			} 	else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	protected void manageListeners() {
		
		for (OPSpecialityDTO specialityDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(specialityDTO);
			final ComboBox procedure = (ComboBox)combos.get("ped");
			if(specialityDTO.getSpecialityType() != null){
				
			}
		}
	}

	private void addSpecialityNames(ComboBox box) {
		BeanItemContainer<SelectValue> speciality = new BeanItemContainer<SelectValue>(SelectValue.class);
//		speciality = masterService.getMastersValuebyTypeCodeOnStaatus("OP_SPLTYPE");
		speciality =  preauthService.getSpecialityType("M");
		box.setContainerDataSource(speciality);
		box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		box.setItemCaptionPropertyId("value");
	}
	
	private void addPedValues(ComboBox box) {
		BeanItemContainer<SelectValue> ped = new BeanItemContainer<SelectValue>(SelectValue.class);
		ped = masterService.getOPPedValues();
		box.setContainerDataSource(ped);
		box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		box.setItemCaptionPropertyId("value");
	}
	
	private void addProcedureNameListener(ComboBox speclbox,ComboBox procBox){
		if(speclbox != null){
			speclbox.addListener(new Listener(){

				@Override
				public void componentEvent(Event event) {

					ComboBox component = (ComboBox) event.getComponent();
					OPSpecialityDTO specialityDTO = (OPSpecialityDTO) component
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(specialityDTO);
					ComboBox comboBox = (ComboBox) hashMap.get("ped");
					if (specialityDTO != null) {
						if (specialityDTO.getSpecialityType() != null && specialityDTO.getSpecialityType().getValue() != null) {
							if (comboBox != null) {
								addProcedureValues(specialityDTO.getSpecialityType().getId(), comboBox,specialityDTO.getPed());
							}
						}
					}
				
				}
				
			});
		}
		
	}
	
	public void addProcedureValues(Long specId,ComboBox prodcomboBox,SelectValue procValue){
		/*if(this.presenterString.equalsIgnoreCase("PreAuth")){
			fireViewEvent(PreauthWizardPresenter.GET_PREAUTH_PROCEDURE_VALUES,specId);
			fireViewEvent(PreauthWizardPresenter.SHOW_PTCA_CABG,true);
		} */
		
		prodcomboBox.setContainerDataSource(procedCode);
		prodcomboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		prodcomboBox.setItemCaptionPropertyId("value");
		
		if (procValue != null) {
			prodcomboBox.setValue(procValue);
		}
		
	}
	
	public void setProcedure(
			BeanItemContainer<SelectValue> procdSelectValueContainer) {
		procedCode = procdSelectValueContainer;
	}
	
	public List<OPSpecialityDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<OPSpecialityDTO> itemIds = (List<OPSpecialityDTO>) this.table
				.getItemIds();
		if(itemIds.isEmpty()) {
			itemIds = new ArrayList<OPSpecialityDTO>();
		}
		List<OPSpecialityDTO> procedureList = new ArrayList<OPSpecialityDTO>();
		for (OPSpecialityDTO specialityDTO : itemIds) {
				if(specialityDTO.getPed() != null && specialityDTO.getPed().getValue() != null && specialityDTO.getPed().getValue().equalsIgnoreCase("others")){
					if(specialityDTO.getAddprocedureId() != null && specialityDTO.getProcedureValue() !=null && !specialityDTO.getProcedureValue().isEmpty()) {
						SelectValue selectValue = new SelectValue();
						selectValue.setId(specialityDTO.getAddprocedureId());
						selectValue.setValue(specialityDTO.getProcedureValue());
						specialityDTO.setPed(selectValue);
					}
				}
				procedureList.add(specialityDTO);
			
		}
		return procedureList;
	}
	
	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(OPSpecialityDTO specialityDTO) {
		data.addItem(specialityDTO);
		manageListeners();
	}

}
