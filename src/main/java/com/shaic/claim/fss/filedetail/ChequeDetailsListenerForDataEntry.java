package com.shaic.claim.fss.filedetail;

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
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.fss.searchfile.SearchDataEntryTableDTO;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ChequeDetailsListenerForDataEntry extends ViewComponent {
	
	@EJB
	private MasterService masterService;
	
	private Map<ChequeDetailsTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ChequeDetailsTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<ChequeDetailsTableDTO> data = new BeanItemContainer<ChequeDetailsTableDTO>(
			ChequeDetailsTableDTO.class);
	
	private Table table;
	
	private Button btnAdd;

	private Map<String, Object> referenceData;

	private List<String> errorMessages;

	private static Validator validator;

	private String presenterString;
	
	public TextField dummyField = new TextField();
	
	private SearchDataEntryTableDTO bean;
	
	public List<ChequeDetailsTableDTO> deletedDTO;
	
	public TextField listenerField = new TextField();
	
	public void init(SearchDataEntryTableDTO bean, String presenterString) {
		this.presenterString = presenterString;
		this.bean = bean;
		deletedDTO = new ArrayList<ChequeDetailsTableDTO>();
		//this.procedureList = procedureList;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
		

		VerticalLayout layout = new VerticalLayout();
		

		initTable();
		table.setWidth("1000px");
		table.setHeight("100px");
		table.setPageLength(table.getItemIds().size());
		HorizontalLayout btnLayout = new HorizontalLayout(table, btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.TOP_RIGHT);
		addListener();

		layout.setMargin(true);

		setCompositionRoot(btnLayout);
	}

	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				ChequeDetailsTableDTO tableDTO = new ChequeDetailsTableDTO();
				BeanItem<ChequeDetailsTableDTO> addItem = data
						.addItem(tableDTO);
			}
		});
	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("Cheque Details", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());

		// Added for table height..
		table.setHeight("160px");

		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button deleteButton = new Button("Delete");
				ChequeDetailsTableDTO dto = (ChequeDetailsTableDTO) itemId;
				deleteButton.setEnabled(true);
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					
					public void buttonClick(ClickEvent event) {
						final ChequeDetailsTableDTO currentItemId = (ChequeDetailsTableDTO) event.getButton().getData();
							ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {

												public void onClose(ConfirmDialog dialog) {
													if (!dialog.isConfirmed()) {
														// Confirmed to continue
														ChequeDetailsTableDTO dto =  (ChequeDetailsTableDTO)currentItemId;
														if(dto.getChequeNo() != null && dto.getChequeNo().length() > 0) {
															deletedDTO.add((ChequeDetailsTableDTO)currentItemId);
														}
														table.removeItem(currentItemId);
													} else {
														// User did not confirm
													}
												}
											});
							dialog.setClosable(false);
					}
				});
				// deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				return deleteButton;
			}
		});
		
		table.setVisibleColumns(new Object[] { "chequeNo","chequeDate", "bankName",
				"bankBranch", "Delete" });

		table.setColumnHeader("chequeNo", "Cheque No");
		table.setColumnHeader("chequeDate", "Cheque Date");
		table.setColumnHeader("bankName", "Bank");
		table.setColumnHeader("bankBranch", "Branch");
		table.setEditable(true);

		// manageListeners();

		// Use a custom field factory to set the edit fields as immediate
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			final ChequeDetailsTableDTO chequeDetail = (ChequeDetailsTableDTO) itemId;
			
			Map<String, AbstractField<?>> tableRow = null;

			if(tableItem.get(chequeDetail) == null)
			{
				tableItem.put(chequeDetail,
						new HashMap<String, AbstractField<?>>());
				
			}
			tableRow = tableItem.get(chequeDetail);
			
			if ("chequeNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setEnabled(true);
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setReadOnly(false);
				tableRow.put("chequeNo", field);
				return field;
			}else if("chequeDate".equals(propertyId)){
				PopupDateField cheque = new PopupDateField();
				cheque.setDateFormat("dd-MM-yyyy");
				cheque.setData(chequeDetail);
				cheque.setEnabled(true);
				cheque.setWidth("200px");
				cheque.setReadOnly(false);
				tableRow.put("chequeDate", cheque);
				return cheque;
			}else if ("bankName".equals(propertyId)) {
				TextField field = new TextField();
				field.setEnabled(true);
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setReadOnly(false);
				tableRow.put("bankName", field);
				return field;
			}else if ("bankBranch".equals(propertyId)) {
				TextField field = new TextField();
				field.setEnabled(true);
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setReadOnly(false);
				tableRow.put("bankBranch", field);
				return field;
			}
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				field.setEnabled(true);
				return field;
			}
		}
	}
		
	private void showErrorPopup(ComboBox field, VerticalLayout layout) {
		layout.setMargin(true);
		layout.setSpacing(true);
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
		field.setValue(null);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
		
	public List<ChequeDetailsTableDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<ChequeDetailsTableDTO> itemIds = (List<ChequeDetailsTableDTO>) this.table
				.getItemIds();
		if(itemIds.isEmpty()) {
			itemIds = new ArrayList<ChequeDetailsTableDTO>();
		}
		return itemIds;
	}
	
	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(ChequeDetailsTableDTO tableDTO) {
		data.addItem(tableDTO);
	}

	public boolean isValid() {
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<ChequeDetailsTableDTO> itemIds = (Collection<ChequeDetailsTableDTO>) table
				.getItemIds();
		for (ChequeDetailsTableDTO bean : itemIds) {

			Set<ConstraintViolation<ChequeDetailsTableDTO>> validate = validator
					.validate(bean);

			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<ChequeDetailsTableDTO> constraintViolation : validate) {
					if(constraintViolation.getRootBean() != null && presenterString.equalsIgnoreCase(SHAConstants.ADD_DATA_ENTRY)){
						ChequeDetailsTableDTO rootBean = constraintViolation.getRootBean();
					    errorMessages.add(constraintViolation.getMessage());
					}else{
						errorMessages.add(constraintViolation.getMessage());
					}
				}
			}
		}
		return !hasError;
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}
	
}
