package com.shaic.claim.sublimit;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewSublimitDetails extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private VerticalLayout mainLayout;
	
	@Inject
	private ViewSublimitTable viewSublimitTable;
	
	public void init(){
		buildMainLayout();
		mainLayout = new VerticalLayout(viewSublimitTable);
		mainLayout.setComponentAlignment(viewSublimitTable, Alignment.MIDDLE_CENTER);
		setCompositionRoot(mainLayout);
	}
	
	public void buildMainLayout(){
		viewSublimitTable.init("", false, false);
		viewSublimitTable.setHeight("100.0%");
		viewSublimitTable.setWidth("100.0%");
	}

}
