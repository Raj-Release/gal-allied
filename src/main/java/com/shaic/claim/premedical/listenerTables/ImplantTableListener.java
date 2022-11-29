package com.shaic.claim.premedical.listenerTables;

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
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.wizard.dto.ImplantDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.PreauthService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ImplantTableListener extends ViewComponent{

	private Map<ImplantDetailsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ImplantDetailsDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<ImplantDetailsDTO> data = new BeanItemContainer<ImplantDetailsDTO>(ImplantDetailsDTO.class);
	
	private Table table;

	private Button btnAdd;

	private PreauthDTO bean;

	private List<String> errorMessages;

	private Validator validator;
	
	@EJB
	private PreauthService preauthService;

	public void init(PreauthDTO bean,String presenterString){
		this.bean = bean;
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
				ImplantDetailsDTO implantDetailsDTO = new ImplantDetailsDTO();
				BeanItem<ImplantDetailsDTO> addItem = data.addItem(implantDetailsDTO);
			}
		});
	}

	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout){

		table = new Table("Implant Details", data);
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
						ImplantDetailsDTO dto =  (ImplantDetailsDTO)currentItemId;
						if(dto.getKey() != null ) {
							String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
							preauthService.deleteImplantDetails(dto.getKey(), userName);
						}
						table.removeItem(currentItemId);
					} 
				});
				deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				return deleteButton;
			}
		});

		table.setVisibleColumns(new Object[] { "implantName","implantType","implantCost","Delete" });
		table.setColumnHeader("implantName", "Implant Name");
		table.setColumnHeader("implantType", "Implant Type");
		table.setColumnHeader("implantCost", "Implant Cost");

		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {

		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {

			ImplantDetailsDTO  implantDetailsDTO= (ImplantDetailsDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(implantDetailsDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(implantDetailsDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(implantDetailsDTO);
			}

			if("implantName".equals(propertyId)){
				TextArea implantName = new TextArea();
				implantName.setWidth("70%");
				implantName.setHeight("21px");
				implantName.setNullRepresentation("");
				implantName.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
				SHAUtils.handleTextAreaPopupDetails(implantName,null,getUI(),SHAConstants.IMPLANT_NAME);
				implantName.setMaxLength(100);
				if(implantDetailsDTO.getImplantName() != null){
					implantName.setValue(implantDetailsDTO.getImplantName());
				}
				tableRow.put("implantName", implantName);
				implantName.setData(implantDetailsDTO);
				return implantName;
			} else if("implantType".equals(propertyId)){
				TextArea implantType = new TextArea();
				implantType.setWidth("70%");
				implantType.setHeight("21px");
				implantType.setNullRepresentation("");
				implantType.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
				SHAUtils.handleTextAreaPopupDetails(implantType,null,getUI(),SHAConstants.IMPLANT_TYPE);
				implantType.setMaxLength(100);
				if(implantDetailsDTO.getImplantType() != null && !implantDetailsDTO.getImplantType().isEmpty()){
					implantType.setValue(implantDetailsDTO.getImplantType());
				}
				tableRow.put("implantType", implantType);
				implantType.setData(implantDetailsDTO);
				return implantType;
			} else if("implantCost".equals(propertyId)){
				TextField implantCost = new TextField();
				implantCost.setWidth("70%");
				implantCost.setNullRepresentation("");
				CSValidator designationValid = new CSValidator();
				designationValid.extend(implantCost);
				designationValid.setRegExp("^[0-9.]*$");
				designationValid.setPreventInvalidTyping(true);
				if(implantDetailsDTO.getImplantCost() != null){
					implantCost.setValue(implantDetailsDTO.getImplantCost().toString());
				}

				return implantCost;
			} else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	public List<ImplantDetailsDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<ImplantDetailsDTO> itemIds = (List<ImplantDetailsDTO>) this.table.getItemIds();
		if(itemIds.isEmpty()) {
			itemIds = new ArrayList<ImplantDetailsDTO>();
		}
		return itemIds;
	}

	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(ImplantDetailsDTO implantDetailsDTO) {
		data.addItem(implantDetailsDTO);
	}

	public boolean isValid() {

		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<ImplantDetailsDTO> itemIds = (Collection<ImplantDetailsDTO>) table.getItemIds();
		for (ImplantDetailsDTO implantDetailsDTO : itemIds) {
			if(implantDetailsDTO.getImplantName() == null || 
					(implantDetailsDTO.getImplantName() != null &&  implantDetailsDTO.getImplantName().trim().isEmpty())) {
				hasError = true;
				errorMessages.add("Please Enter Implant Name");
			}else if(implantDetailsDTO.getImplantType() == null ||
						(implantDetailsDTO.getImplantType() != null &&  implantDetailsDTO.getImplantType().trim().isEmpty())){
				hasError = true;
				errorMessages.add("Please Enter Implant Type");
			}else if(implantDetailsDTO.getImplantCost() == null){
				hasError = true;
				errorMessages.add("Please Enter Implant Cost");
			}

			Set<ConstraintViolation<ImplantDetailsDTO>> validate = validator.validate(implantDetailsDTO);
			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<ImplantDetailsDTO> constraintViolation : validate) {
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
