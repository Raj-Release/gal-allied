package com.shaic.claim.registration.manual;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@UIScoped
@SuppressWarnings("serial")
public class ManualRegisterUI  extends ViewComponent{

	private VerticalLayout mainLayout;

	private Panel panel_4;

	private VerticalLayout verticalLayout_8;
	
	private Button nativeButton_1;
	
	private HorizontalLayout horizontalLayout_4;
	
	private FormLayout formLayout_7;
	
	private TextField textField_9;
	
	private TextField textField_8;
	
	private FormLayout formLayout_6;
	
	private PopupDateField popupDateField_5;
	
	private NativeSelect nativeSelect_5;

	
	private PagedTable claimRegistractionTable;
	
	public void init()
	{
		buildMainLayout();
		
		setCompositionRoot(mainLayout);
	}

	
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.addStyleName("view");
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// panel_4
		panel_4 = buildPanel_4();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.addComponent(panel_4);
		
		mainLayout.setComponentAlignment(panel_4, Alignment.MIDDLE_CENTER);
		return mainLayout;
	}

	
	private Panel buildPanel_4() {
		// common part: create layout
		panel_4 = new Panel();
		
		//Vaadin8-setImmediate() panel_4.setImmediate(false);
		panel_4.setWidth("100.0%");
		panel_4.setHeight("100.0%");
		panel_4.addStyleName("view");
		
		// verticalLayout_8
		verticalLayout_8 = buildVerticalLayout_8();
		panel_4.setContent(verticalLayout_8);
		
		return panel_4;
	}

	@SuppressWarnings("deprecation")
	
	private VerticalLayout buildVerticalLayout_8() {
		// common part: create layout
		Label headerLabel = new Label("<b style='font-size: 20px;'>Claim Registration</b>",Label.CONTENT_TEXT.HTML);
		verticalLayout_8 = new VerticalLayout();
		//Vaadin8-setImmediate() verticalLayout_8.setImmediate(false);
		verticalLayout_8.setWidth("100.0%");
		verticalLayout_8.setHeight("100.0%");
		verticalLayout_8.setMargin(true);
		verticalLayout_8.setSpacing(true);
		verticalLayout_8.addStyleName("view");
		verticalLayout_8.addComponent(headerLabel);
		horizontalLayout_4 = buildHorizontalLayout_4();
		verticalLayout_8.addComponent(horizontalLayout_4);
			
		nativeButton_1 = new NativeButton();
		nativeButton_1.setCaption("Submit");
		//Vaadin8-setImmediate() nativeButton_1.setImmediate(true);
		nativeButton_1.setWidth("-1px");
		nativeButton_1.setHeight("-1px");
		verticalLayout_8.addComponent(nativeButton_1);
		verticalLayout_8.setComponentAlignment(nativeButton_1,
				new Alignment(20));
		nativeButton_1.addClickListener(new Button.ClickListener(){
					@Override
					public void buttonClick(ClickEvent event) {
						fireViewEvent(
								ManualRegistrationPresenter.SEARCH_CLAIMREGISTER_TABLE_SUBMIT,null);
					}
				});

		return verticalLayout_8;
	}

	
	private HorizontalLayout buildHorizontalLayout_4() {
		// common part: create layout
		horizontalLayout_4 = new HorizontalLayout();
		//Vaadin8-setImmediate() horizontalLayout_4.setImmediate(false);
		horizontalLayout_4.setWidth("100.0%");
		horizontalLayout_4.setHeight("64px");
		horizontalLayout_4.setMargin(false);
		horizontalLayout_4.addStyleName("view");
		// formLayout_6
		formLayout_6 = buildFormLayout_6();
		horizontalLayout_4.addComponent(formLayout_6);
		
		// formLayout_7
		formLayout_7 = buildFormLayout_7();
		horizontalLayout_4.addComponent(formLayout_7);
		
		return horizontalLayout_4;
	}

	
	private FormLayout buildFormLayout_6() {
		// common part: create layout
		formLayout_6 = new FormLayout();
		//Vaadin8-setImmediate() formLayout_6.setImmediate(false);
		formLayout_6.setWidth("100.0%");
		formLayout_6.setHeight("-1px");
		formLayout_6.setMargin(false);
		formLayout_6.setSpacing(true);
		
		// nativeSelect_5
		nativeSelect_5 = new NativeSelect();
		nativeSelect_5.setCaption("Hospital Type");
		//Vaadin8-setImmediate() nativeSelect_5.setImmediate(true);
		nativeSelect_5.setWidth("-1px");
		nativeSelect_5.setHeight("-1px");
		nativeSelect_5.addItem("Network");
		nativeSelect_5.addItem("Non-Network");
		nativeSelect_5.setValue("Network");
		//Vaadin8-setImmediate() nativeSelect_5.setImmediate(true);
		
		
		formLayout_6.addComponent(nativeSelect_5);
		
		// popupDateField_5
		popupDateField_5 = new PopupDateField();
		popupDateField_5.setCaption("Intimation Date");
		//Vaadin8-setImmediate() popupDateField_5.setImmediate(false);
		popupDateField_5.setWidth("-1px");
		popupDateField_5.setHeight("-1px");
		formLayout_6.addComponent(popupDateField_5);
		
		return formLayout_6;
	}
	
	private FormLayout buildFormLayout_7() {
		// common part: create layout
		formLayout_7 = new FormLayout();
		//Vaadin8-setImmediate() formLayout_7.setImmediate(false);
		formLayout_7.setWidth("-1px");
		formLayout_7.setHeight("-1px");
		formLayout_7.setMargin(false);
		formLayout_7.setSpacing(true);
		
		// textField_8
		textField_8 = new TextField();
		textField_8.setCaption("Policy No");
		//Vaadin8-setImmediate() textField_8.setImmediate(false);
		textField_8.setWidth("-1px");
		textField_8.setHeight("-1px");
		formLayout_7.addComponent(textField_8);
		
		// textField_9
		textField_9 = new TextField();
		textField_9.setCaption("Id");
		//Vaadin8-setImmediate() textField_9.setImmediate(false);
		textField_9.setWidth("-1px");
		textField_9.setHeight("-1px");
		formLayout_7.addComponent(textField_9);
		
		return formLayout_7;
	}
	
	
	/*public void showTable(List<HumanTask> tasks){	
		removeTableFromLayout();
		mainLayout.addComponent(retrieveCorrespondingValuesFromDB(tasks).createControls());
		
	}*/
	
	private void removeTableFromLayout(){
		try {
			// Remove the Table also if it exists.
			ArrayList<Component> componentArray = new ArrayList<Component>();
			int componentCount = mainLayout.getComponentCount();
			for (int i = 0; i < componentCount; i++) {
				Component eachComponent = mainLayout.getComponent(i);
				String componentClass = eachComponent.getClass().toString();
				if (StringUtils.equalsIgnoreCase(componentClass,
						"class com.vaadin.ui.Panel")) {
					continue;
				}
				componentArray.add(eachComponent);
			}
			for (int i = 0; i < componentArray.size(); i++) {
				mainLayout.removeComponent(componentArray.get(i));
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
/*	@SuppressWarnings("deprecation")
	private PagedTable retrieveCorrespondingValuesFromDB(List<HumanTask> tasks){
		// TODO: retrieve the values based on the search value.
		
		BeanItemContainer<HumanTask> beans = new BeanItemContainer<HumanTask>(HumanTask.class);
		for (HumanTask humanTask : tasks) {

		}
		
		claimRegistractionTable = new PagedTable();
		claimRegistractionTable.setContainerDataSource(beans);
		claimRegistractionTable.addStyleName("wordwrap-headers");
		claimRegistractionTable.setPageLength(5);
		claimRegistractionTable.setWidth("1000px");
		//Vaadin8-setImmediate() claimRegistractionTable.setImmediate(true);
		claimRegistractionTable.setSelectable(true);
		claimRegistractionTable.setWidth(100, Unit.PERCENTAGE);
		
		Object[] columns = new Object[]{"id", "title", "state", "outcome"};
		claimRegistractionTable.setVisibleColumns(columns);
		
		
		claimRegistractionTable.setColumnHeader("id", "Intimation No");
		claimRegistractionTable.setColumnHeader("title", "Title");
		claimRegistractionTable.setColumnHeader("state", "State");
		claimRegistractionTable.setColumnHeader("outcome", "OutCome");
		
		claimRegistractionTable.setPageLength(10);
		return claimRegistractionTable;
		
	}*/
	
}