package com.shaic.claim.selecthospital.ui;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.v7.ui.VerticalLayout;

public class SearchIntimationHospitalUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Instance<HospitalSearch> selectHospital;
	
	@Inject
	private Instance<HospitalSearchTable> hospitalSearchTableInstance;
	
	private VerticalLayout mainPanel = new VerticalLayout();	
	
	private HospitalSearchTable hospitalTable;
	
	@PostConstruct
	public void initview(){
		
	}
	
	public void init() {
		addStyleName("view");
		setSizeFull();
		hospitalTable = hospitalSearchTableInstance.get();
		hospitalTable.init("", false,true);
		hospitalTable.setHeight("100.0%");
		hospitalTable.setWidth("100.0%");
		HospitalSearch searchHospital = selectHospital.get();
		searchHospital.init();
		searchHospital.setHeight(200, Unit.PIXELS);		
		
		mainPanel.addComponent(searchHospital);
		mainPanel.addComponent(hospitalTable);		
				
		mainPanel.setSpacing(true);		
		
		mainPanel.setHeight("100.0%");
		mainPanel.setWidth("100.0%");
		setHeight("100.0%");
		setHeight("600px");
		setCompositionRoot(mainPanel);
	}
	
}
