package com.shaic.claim.translationmiscrequest.view;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.Intimation;
import com.vaadin.ui.Panel;

public class ViewTranslationMiscRequestDetails extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ViewTranslationMiscRequestTable viewCoOrdinatorReplyTable;
	
	@Inject ViewTranslationMiscRequestService viewCoOrdinatorReplyService;
	
	public Panel mainLayout;
	
	public void init(Intimation intimation){
		viewCoOrdinatorReplyTable.init("", false, false);
		viewCoOrdinatorReplyTable.setTableList(viewCoOrdinatorReplyService.search(intimation.getKey()));
		mainLayout = new Panel(viewCoOrdinatorReplyTable);
		mainLayout.setWidth("100%");
		
		//TODO:
		
		setCompositionRoot(mainLayout);
		
	}
	
	

}
