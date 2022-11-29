/**
 * 
 */
package com.shaic.claim.legal.home;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;


public class SearchLegalHomePage  extends ViewComponent{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6589043418245772350L;
	
	@Inject
	private ViewDetails viewDetails;
	
	private VerticalLayout finalLayout ;
	
	
	@PostConstruct
	public void init() {
		

		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Legal Home");
		viewDetails.initView("",
				ViewLevels.LEGAL_CLAIMS,"");
		finalLayout = new VerticalLayout(viewDetails);
		finalLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		mainPanel.setContent(finalLayout);
		mainPanel.setHeight("100%");
		setCompositionRoot(mainPanel);
	
		
	}
	
	
	
	
		
}

