package com.shaic.claim.userproduct.document.search;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class SearchDoctorNameForm extends SearchComponent<SearchDoctorNameDTO>{
	
	
	@Inject
	private ViewDoctorSearchCriteriaViewImpl viewSearchCriteria;
	
	/*@Inject
	private ViewDoctorSearchCriteriaViewImpl viewDoctorDetails;*/

	
	private TextField doctorName;
	
	private Button searchDoctor;
	
	public static Window popup;
	
	private Panel mainPanel;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("User - Product & Document Type Mapping");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		doctorName = binder.buildAndBind("Doctor Name", "doctorName", TextField.class);
		
		
		searchDoctor = new Button();
		searchDoctor.setStyleName(ValoTheme.BUTTON_LINK);
		searchDoctor.setIcon(new ThemeResource("images/search.png"));
		
		addDoctorListner();
		
		FormLayout formLayoutRight = new FormLayout(searchDoctor);
		FormLayout formLayoutLeft = new FormLayout(doctorName);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:80.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:80.0px;left:329.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("650px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("150px");
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchDoctorNameDTO>(SearchDoctorNameDTO.class);
		this.binder.setItemDataSource(new SearchDoctorNameDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}	
	
	public void setPanelCaption(){
		if(mainPanel != null){
			mainPanel.setCaption("User - Product & Document Type Mapping");
		}
	}
	
	public void addDoctorListner() {
		
		searchDoctor.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				popup = new com.vaadin.ui.Window();
				
				viewSearchCriteria.initView(popup);
//				viewSearchCriteria.setWindowObject(popup);
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

	public void setDoctorNameValue(SearchDoctorDetailsTableDTO viewSearchCriteriaDTO) {
		// TODO Auto-generated method stub
		doctorName.setValue(viewSearchCriteriaDTO.getDoctorName());
		//doctorName.setHeight("150px");
	}
}
