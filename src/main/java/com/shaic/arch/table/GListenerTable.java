package com.shaic.arch.table;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.vaadin.cdi.CDIUI;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})

public abstract class GListenerTable<T> extends GBaseTable<T> {

	private VerticalLayout vLayout = new VerticalLayout();

	 private BeanItemContainer<T> container;
	 
	private Button btnAdd;
	
	private Button btnValidate;

	private ListenerTableFactory tableFieldFactory;
	
	private Class beanType;
	
	private List<String> errorMessages;

	private HorizontalLayout btnLayout;  

	public GListenerTable(Class beanType) {
		this.beanType = beanType;
		this.errorMessages = new ArrayList<String>();
	}
	
	
	private static Validator validator;
	
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	
	@PostConstruct
	public void initView()
	{
		container = new BeanItemContainer<T>(beanType);
		table.setContainerDataSource(container);
		table.setSelectable(false);
		
	}
	
	@Override
	protected void preInit() {
		
		super.preInit();
		
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
		btnValidate = new Button("Validate");
		
		this.tableFieldFactory = new ListenerTableFactory();
		table.setTableFieldFactory(this.tableFieldFactory);
		btnAdd.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				try {
					
					T instance = createInstance();
					table.addItem(instance);
					table.removeGeneratedColumn("Delete");
					table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
					      @Override
					      public Object generateCell(final Table source, final Object itemId, Object columnId) {
					    	Button deleteButton = new Button("Delete");
					    	deleteButton.addClickListener(new Button.ClickListener() {
						        public void buttonClick(ClickEvent event) {
						        	 deleteRow(itemId);
						        } 
						    });
					    	return deleteButton;
					      };
					});
					table.setEditable(true);
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		btnValidate.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if (!isValid())
				{
					List<String> errors = getErrors();
					StringBuffer eMsg = new StringBuffer();
					
					for (String string : errors) {
						eMsg.append(string);
					}
					Notification.show("Error", eMsg.toString(), Type.ERROR_MESSAGE);
				}
			}
		});
	}

	private final T createInstance() {
		try {
			return ((Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setEditable(boolean flag) {
		table.setEditable(flag);
	}

	public void init(String tableCaption, Boolean isNeedAddButton) {
		preInit();
		setSizeFull();
		table.setEditable(true);
		table.setCaption(tableCaption);
		btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		btnLayout.setVisible(isNeedAddButton);
		if(vLayout != null) {
			vLayout.removeAllComponents();
		}
		
		vLayout.addComponent(btnLayout);
		table.setWidth("60%");
		table.setPageLength(table.getItemIds().size());
		
		
		vLayout.addComponent(table);
		vLayout.setMargin(true);

		setCompositionRoot(vLayout);
		initTable();
		localize(null);
	}
	

	public void setReference(Map<String, Object> referenceData, TableCellSelectionHandler handler) {
		Map<String, TableFieldDTO> filedMapping = getFiledMapping();
		tableFieldFactory.setTableFieldFactory(filedMapping, referenceData, handler);
	}

	protected abstract void newRowAdded();

	protected abstract Map<String, TableFieldDTO> getFiledMapping();
	
	public boolean isValid()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		Collection<T> itemIds = (Collection<T>) table.getItemIds();
		for (T bean : itemIds) {
			Set<ConstraintViolation<T>> validate = validator.validate(bean);

			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<T> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}
		return !hasError;
	}
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	
	public List<T> getValues()
	{
		Collection<T> coll = (Collection<T>) table.getItemIds();
		List list;
		if (coll instanceof List){
			list = (List)coll;
		}
		else{
			list = new ArrayList(coll);
		}
		return list;
	}
	
	public abstract void deleteRow(Object itemId);
	
	public Collection<?> getTableItemIds() {
		int totalItemValue = (table.getItemIds().size()) - 1;
		Object object2 = table.getItemIds().toArray()[totalItemValue];
		final Item totalItem = table.getItem(object2);
		for (Object object : table.getItemIds()) {
			Item item = table.getItem(object);
			Iterator<?> iterator = item.getItemPropertyIds().iterator();
			while(iterator.hasNext()) {
				String propertyId = (String) iterator.next();
				Object propertyField = item.getItemProperty(propertyId).getValue();
				if(propertyId.startsWith("approvedAmount")) {
					TextField approveTxt = (TextField) propertyField;
					approveTxt.addBlurListener(new BlurListener() {
						@Override
						public void blur(BlurEvent event) {
							Component component = event.getComponent();
							AbstractField<?> field = (AbstractField<?>) component;
							TextField value = (TextField) totalItem.getItemProperty("approvedAmount").getValue();
							Integer amount = Integer.valueOf(value.getValue()) + Integer.valueOf(field.getValue().toString());
							value.setValue(amount.toString());
						}
					});
				}
			}
		}
		return table.getItemIds();
	}
}
