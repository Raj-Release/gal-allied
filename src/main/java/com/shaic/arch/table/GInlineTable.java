package com.shaic.arch.table;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.vaadin.cdi.CDIUI;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})

public abstract class GInlineTable<T> extends GBaseTable<T> {

	private VerticalLayout vLayout = new VerticalLayout();

	 private BeanItemContainer<T> container;
	 
	private Button btnAdd;
	
	private Button btnValidate;

	private SpecialistPageTableFactory tableFieldFactory;
	
	private Class beanType;
	
	private List<String> errorMessages;

	private HorizontalLayout btnLayout;  

	public GInlineTable(Class beanType) {
		this.beanType = beanType;
		this.errorMessages = new ArrayList<String>();
	}
	
	public void setTableList(final Collection<T> list) {
		table.removeAllItems();
		T lastElement = null;
		for (T t : list) {
			lastElement = t;
		}
		table.setData(lastElement);
		for (final T bean : list) {
			table.addItem(bean);
		}
		Collection<?> itemIds = table.getItemIds();
		table.sort();
	}
	
	
	public void addBeanToList(final T bean) {
		table.setData(bean);
		table.addItem(bean);
		table.sort();
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
		
		this.tableFieldFactory = new SpecialistPageTableFactory(container);
		table.setTableFieldFactory(this.tableFieldFactory);
		btnAdd.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				try {
					
					T instance = createInstance();
					table.addItem(instance);
//					Button deleteButton = new Button("Delete");
//					deleteButton.setData(item);
//					deleteButton.addClickListener(new Button.ClickListener() {
//				        public void buttonClick(ClickEvent event) {
//				        	 Item itemId = (Item) event.getButton().getData();
//				        	 deleteRow(itemId);
//				        } 
//				    });
//			    	item.addItemProperty("delete", (Property) deleteButton);
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
					// TODO Auto-generated catch block
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
		table.setHeight("30%");
		table.setPageLength(table.getItemIds().size());
		
		
		vLayout.addComponent(table);

		setCompositionRoot(vLayout);
		initTable();
		localize(null);
	}
	

	public void setReference(Map<String, Object> referenceData) {
		Map<String, TableFieldDTO> filedMapping = getFiledMapping();
		tableFieldFactory.setTableFieldFactory(filedMapping, referenceData);
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
}
