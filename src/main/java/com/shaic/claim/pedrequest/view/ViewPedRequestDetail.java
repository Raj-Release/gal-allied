package com.shaic.claim.pedrequest.view;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.v7.ui.VerticalLayout;

public class ViewPedRequestDetail extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PEDRequestDetailsViewTable pedRequestDetailsTable;
	
	@Inject
	private ViewpedRequestService viewpedRequestService;
	
	private VerticalLayout mainLayout;
	
	public void init(Long intimationKey){
		pedRequestDetailsTable.init("", false, false);
		pedRequestDetailsTable.setTableList(viewpedRequestService.search(intimationKey));
		mainLayout = new VerticalLayout(pedRequestDetailsTable);
		setCompositionRoot(mainLayout);
	}
	
	public void clearPEDReqDetailsPopup(){
    	if(mainLayout != null){
    		mainLayout.removeAllComponents();
    	}
    	if(this.pedRequestDetailsTable != null){
    		this.pedRequestDetailsTable.getTable().clear();
    	}
    }

}