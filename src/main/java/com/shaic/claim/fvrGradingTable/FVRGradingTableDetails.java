package com.shaic.claim.fvrGradingTable;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.v7.ui.VerticalLayout;

public class FVRGradingTableDetails extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private FvrGradingTable objFVRGradingTable;	
	
	@Inject
	private FvrGradingMapper fvrGradingMapper;
	
	private VerticalLayout mainLayout;
	
	@SuppressWarnings("static-access")
	public void init(){
		objFVRGradingTable.init("FVR Grading", false, false);
		objFVRGradingTable.setTableList(fvrGradingMapper.getViewClaimHistoryDTO());
		mainLayout = new VerticalLayout(objFVRGradingTable);
		setCompositionRoot(mainLayout);
	}

}
