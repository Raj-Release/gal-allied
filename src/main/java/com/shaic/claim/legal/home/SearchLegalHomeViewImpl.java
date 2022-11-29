package com.shaic.claim.legal.home;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.vaadin.v7.ui.VerticalLayout;

public class SearchLegalHomeViewImpl extends AbstractMVPView implements SearchLegalHomeView{

	@Inject
	private SearchLegalHomePage searchForm;
	
	private VerticalLayout mainPanel;
	
	@PostConstruct
	public void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		mainPanel = new VerticalLayout();
		mainPanel.addComponent(searchForm);
		//mainPanel.setSplitPosition(47);
		//setHeight("590px");
	//	mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		//searchForm.addSearchListener(this);
		resetView();
	}
	
	
	@Override
	public void doSearch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}


	
	
	

}
