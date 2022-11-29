package com.shaic.claim.coordinator.view;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.Intimation;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewCoOrdinatorReplyDetails extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ViewCoOrdinatorReplyTable viewCoOrdinatorReplyTable;
	
	@Inject ViewCoOrdinatorReplyService viewCoOrdinatorReplyService;
	
	public VerticalLayout mainLayout;
	
	public void init(Intimation intimation){
		viewCoOrdinatorReplyTable.init("", false, false);
		viewCoOrdinatorReplyTable.setTableList(viewCoOrdinatorReplyService.search(intimation.getKey()));
		mainLayout = new VerticalLayout(viewCoOrdinatorReplyTable);
		setCompositionRoot(mainLayout);
		
	}
	
	

}
