package com.shaic.paclaim.manageclaim.reopenclaim.pageClaimLevel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
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
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PAReopenReimbursementEditTable extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<ViewDocumentDetailsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ViewDocumentDetailsDTO, HashMap<String, AbstractField<?>>>();

	/***
	 * Bean object fetch from db
	 */
	BeanItemContainer<ViewDocumentDetailsDTO> data = new BeanItemContainer<ViewDocumentDetailsDTO>(ViewDocumentDetailsDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private BeanItemContainer<SelectValue> icdBlock;
	
	private BeanItemContainer<SelectValue> icdCode;
	
	private BeanItemContainer<SelectValue> description;
	
	private String pedCode;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	public List<ViewDocumentDetailsDTO> deletedDTO;
	
	private String presenterString;
	
	public TextField dummyField;
	
	
	public void init(String presenterString) {
		
		this.presenterString = presenterString;
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		deletedDTO = new ArrayList<ViewDocumentDetailsDTO>();
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
//		layout.addComponent(btnLayout);
		
		initTable();
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		addListener();
		
		layout.addComponent(table);
		
		dummyField = new TextField();
		table.setColumnFooter("billClassification", "Total");
		

		setCompositionRoot(layout);
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(data.size()==0){
				BeanItem<ViewDocumentDetailsDTO> addItem = data.addItem(new ViewDocumentDetailsDTO());
				//btnAdd.setVisible(false);
				//}
				manageListeners();
			}
		});
	}
	
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());

		table.setVisibleColumns(new Object[] { "acknowledgeNumber","rodNumber", "receivedFromValue", "strDocumentReceivedDate", "modeOfReceiptValue", "benefits","approvedAmount", "status" });

		table.setColumnHeader("acknowledgeNumber", "Acknowledgement No");
		table.setColumnHeader("rodNumber", "ROD No");
		table.setColumnHeader("receivedFromValue", "Document Recieved From");
		table.setColumnHeader("strDocumentReceivedDate", "Document Recieved Date");
		table.setColumnHeader("modeOfReceiptValue", "Mode of Receipt");
		table.setColumnHeader("benefits", "Benefit/Cover");
		table.setColumnHeader("approvedAmount", "Approved Amount");
		table.setColumnHeader("status", "Status");
		table.setEditable(true);
		
		table.setColumnWidth("acknowledgeNumber", 200);
		table.setColumnWidth("rodNumber", 200);
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
		table.setFooterVisible(false);

	}
	
	public void manageListeners() {

		
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			ViewDocumentDetailsDTO initiateDTO = (ViewDocumentDetailsDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(initiateDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(initiateDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(initiateDTO);
			}
			
			if("approvedAmount".equals(propertyId)) {
				
				TextField field = new TextField();
				field.setNullRepresentation("");
				CSValidator validator = new CSValidator();
				field.setMaxLength(10);
				validator.extend(field);
				field.setConverter(plainIntegerConverter);
				field.setData(initiateDTO);
				if(initiateDTO.getIsReadOnly()){
					field.setEnabled(false);
				}
				validator.setRegExp("^[0-9 ]*$");	
				validator.setPreventInvalidTyping(true);
				tableRow.put("approvedAmount", field);
//				consideredAmountListener(field,null);
				field.setMaxLength(10);
				field.addBlurListener(getAmountConsiderListener());

				return field;
			} 

			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField){
					field.setWidth("100%");
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setReadOnly(true);
					((TextField) field).setNullRepresentation("");
					
				}
				return field;
			}
		}
	}
	
public void calculateTotal() {
		
		List<ViewDocumentDetailsDTO> itemIconPropertyId = (List<ViewDocumentDetailsDTO>) table.getItemIds();
		
		Integer provisionAmount = 0;
		
		for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : itemIconPropertyId) {
			if(viewDocumentDetailsDTO.getApprovedAmount() != null){
				Double approvedAmount = viewDocumentDetailsDTO.getApprovedAmount();
				Integer amount = approvedAmount.intValue();
				provisionAmount += amount;
			}

		}

		dummyField.setValue(String.valueOf(provisionAmount));
	}
	
	
	StringToIntegerConverter plainIntegerConverter = new StringToIntegerConverter() {
		private static final long serialVersionUID = -2154393632039317675L;

		protected java.text.NumberFormat getFormat(Locale locale) {
	        NumberFormat format = super.getFormat(locale);
	        format.setGroupingUsed(false);
	        return format;
	    };
	};
	
	public BlurListener getAmountConsiderListener() {
		
		BlurListener listener = new BlurListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField component = (TextField) event.getComponent();

				ViewDocumentDetailsDTO documentDetailsDto = (ViewDocumentDetailsDTO)component.getData();
				
				if(component.getValue() != null){
					Integer amount = SHAUtils.getIntegerFromStringWithComma(component.getValue());
					documentDetailsDto.setApprovedAmount(Double.valueOf(amount));
				}

				calculateTotal();
				
			}
		};
		return listener;
		
	}
	
	 public void addBeanToList(ViewDocumentDetailsDTO viewDocumentDetailsDTO) {
	    	data.addBean(viewDocumentDetailsDTO);
	 }
	 
	 public void addList(List<ViewDocumentDetailsDTO> viewDocumentDetailsDTO) {
		 for (ViewDocumentDetailsDTO diagnosisProcedureTableDTO2 : viewDocumentDetailsDTO) {
			 data.addBean(diagnosisProcedureTableDTO2);
		 }
	 }
	 
	 @SuppressWarnings("unchecked")
	public List<ViewDocumentDetailsDTO> getValues() {
		List<ViewDocumentDetailsDTO> itemIds = (List<ViewDocumentDetailsDTO>) this.table.getItemIds() ;

		List<ViewDocumentDetailsDTO> filterDTO = new ArrayList<ViewDocumentDetailsDTO>();
		
		for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : itemIds) {
			if(! viewDocumentDetailsDTO.getIsReadOnly()){
				filterDTO.add(viewDocumentDetailsDTO);
			}
		}

    	return filterDTO;
	}
	
	
	
	

}

