package com.shaic.claim.premedical.listenerTables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.domain.MasterService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("deprecation")
public class TreatingTableListener extends ViewComponent{

	private Map<TreatingDoctorDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<TreatingDoctorDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<TreatingDoctorDTO> data = new BeanItemContainer<TreatingDoctorDTO>(TreatingDoctorDTO.class);
	private Table table;

	private Button btnAdd;

	private Map<String, Object> referenceData;

	private BeanItemContainer<SelectValue> DoctorQualification;

	private String presenterString;

	public List<TreatingDoctorDTO> deletedDTO;

	private PreauthDTO bean;

	private List<String> errorMessages;

	private Validator validator;

	private BeanItemContainer<SelectValue> treatmentCode;

	@Inject
	private AddQualificationPopup addQualificationPopup;

	@EJB
	private MasterService masterService;

	public void init(PreauthDTO bean,String presenterString){
		this.bean = bean;
		this.presenterString = presenterString;
		deletedDTO = new ArrayList<TreatingDoctorDTO>();
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

				TreatingDoctorDTO treatingDoctorDTO = new TreatingDoctorDTO();
				BeanItem<TreatingDoctorDTO> addItem = data.addItem(treatingDoctorDTO);
				//				manageListeners();
			}
		});
	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}


	void initTable(VerticalLayout layout){

		table = new Table("Treating Doctor Details", data);
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
						TreatingDoctorDTO dto =  (TreatingDoctorDTO)currentItemId;
						if(dto.getKey() != null && dto.getDiagnosis() != null && dto.getDiagnosis().length() > 0) {
							deletedDTO.add((TreatingDoctorDTO)currentItemId);
						}
						table.removeItem(currentItemId);
					} 
				});
				deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				return deleteButton;
			}
		});

		table.setVisibleColumns(new Object[] { "treatingDoctorName","qualification","Delete" });
		table.setColumnHeader("treatingDoctorName", "Treating Doctor Name");
		table.setColumnHeader("qualification", "Treating Doctor Qualification");
		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {

		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {

			TreatingDoctorDTO  treatingDoctorDTO= (TreatingDoctorDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(treatingDoctorDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(treatingDoctorDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(treatingDoctorDTO);
			}

			if("treatingDoctorName".equals(propertyId)){
				TextField doctorName = new TextField();
				doctorName.setWidth("70%");
				doctorName.setNullRepresentation("");
				CSValidator designationValid = new CSValidator();
				designationValid.extend(doctorName);
				designationValid.setRegExp("^[a-zA-Z .]*$");
				designationValid.setPreventInvalidTyping(true);
				if(treatingDoctorDTO.getTreatingDoctorName() != null){
					doctorName.setValue(treatingDoctorDTO.getTreatingDoctorName());
				}
				return doctorName;
			} else if("qualification".equals(propertyId)){
				TextField qualification = new TextField();
				qualification.setWidth("70%");
				qualification.setNullRepresentation("");
				CSValidator designationValid = new CSValidator();
				designationValid.extend(qualification);
				designationValid.setRegExp("^[a-zA-Z .,-]*$");
				designationValid.setPreventInvalidTyping(true);
				if(treatingDoctorDTO.getQualification() != null && !treatingDoctorDTO.getQualification().isEmpty()){
					qualification.setValue(treatingDoctorDTO.getQualification());
				}

				return qualification;
			} else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	public void setTreatingQualification(BeanItemContainer<SelectValue> qualification) {
		treatmentCode = qualification;
	}

	public List<TreatingDoctorDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<TreatingDoctorDTO> itemIds = (List<TreatingDoctorDTO>) this.table.getItemIds();
		if(itemIds.isEmpty()) {
			itemIds = new ArrayList<TreatingDoctorDTO>();
		}
		return itemIds;
	}

	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(TreatingDoctorDTO treatingDoctorDTO) {
		data.addItem(treatingDoctorDTO);
	}

	public boolean isValid() {

		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<TreatingDoctorDTO> itemIds = (Collection<TreatingDoctorDTO>) table
		.getItemIds();
		for (TreatingDoctorDTO treatingDoctorDTO : itemIds) {
			if(treatingDoctorDTO.getTreatingDoctorName() == null || 
					(treatingDoctorDTO.getTreatingDoctorName() != null &&  treatingDoctorDTO.getTreatingDoctorName().trim().isEmpty()) ||
					treatingDoctorDTO.getQualification() == null ||
					(treatingDoctorDTO.getQualification() != null &&  treatingDoctorDTO.getQualification().trim().isEmpty())) {

				hasError = true;
				errorMessages.add("Please Select Doctor Name & Qualification");
			}

			Set<ConstraintViolation<TreatingDoctorDTO>> validate = validator.validate(treatingDoctorDTO);
			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<TreatingDoctorDTO> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}

		return !hasError;
	}


	public List<String> getErrors() {
		return this.errorMessages;
	}

}
