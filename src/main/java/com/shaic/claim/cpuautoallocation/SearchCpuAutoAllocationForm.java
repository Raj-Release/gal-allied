package com.shaic.claim.cpuautoallocation;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class SearchCpuAutoAllocationForm extends SearchComponent<SearchCpuAutoAllocationDTO>{
	
	
	@Inject
	private CpuAutoAllocationSearchCriteriaViewImpl viewSearchCriteria;
	
	private TextField cpuName;
	
	private Button searchCPU;
	
	public static Window popup;
	
	private Panel mainPanel;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("CPU Master");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		cpuName = binder.buildAndBind("CPU Name", "cpuName", TextField.class);
		
		searchCPU = new Button();
		searchCPU.setStyleName(ValoTheme.BUTTON_LINK);
		searchCPU.setIcon(new ThemeResource("images/search.png"));
		
		addCpuListner();
		FormLayout formLayoutLeft = new FormLayout(cpuName);
		FormLayout formLayoutRight = new FormLayout(searchCPU);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft, formLayoutRight);

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:120.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:120.0px;left:329.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("650px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("200px");
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchCpuAutoAllocationDTO>(SearchCpuAutoAllocationDTO.class);
		this.binder.setItemDataSource(new SearchCpuAutoAllocationDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}	
	
	public void addCpuListner() {
		
		searchCPU.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				popup = new com.vaadin.ui.Window();
				
				viewSearchCriteria.initView(popup);
//				viewSearchCriteria.setWindowObject(popup);
				popup = new com.vaadin.ui.Window();
				popup.setWidth("60%");
				popup.setHeight("90%");
				popup.setContent(viewSearchCriteria);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(true);
				popup.addCloseListener(new Window.CloseListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
						popup.close();
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			}
		});
		
	}

	public void setCpuNameValue(SearchCpuAutoAllocationTableDTO viewSearchCriteriaDTO) {
		// TODO Auto-generated method stub
		cpuName.setValue(viewSearchCriteriaDTO.getCpu());
	}
	
	private void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
}
