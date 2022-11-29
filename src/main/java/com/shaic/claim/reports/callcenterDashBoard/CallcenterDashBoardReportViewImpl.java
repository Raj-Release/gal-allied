package com.shaic.claim.reports.callcenterDashBoard;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

public class CallcenterDashBoardReportViewImpl extends AbstractMVPView implements CallcenterDashBoardReportView{
	
	
	@Inject
    private Instance<CallcenterDashBoard> callCenterDashBoardUI; 

	@Override
	public void resetView() {
		callCenterDashBoardUI.get().refresh();

	}
	
	@PostConstruct
	public void initView() {
		showSearchCallCenterDashBoard();
    	resetView();
    }
    
	public void searchCallCenterDashBoard(List<CallcenterDashBoardReportDto> resultList) {
		
			callCenterDashBoardUI.get().buildSearchIntimationTable(resultList);
		
	}
	
	@Override
	public void showSearchCallCenterDashBoard() {
		addStyleName("view");
        setSizeFull();
//        callCenterDashBoardUI.get();
        setCompositionRoot(callCenterDashBoardUI.get());
        setVisible(true);
	}

	@Override
	public void hideSearchFields(String valChanged) {
		callCenterDashBoardUI.get().hideSearchFields(valChanged);
		
	}
	
	@Override
	public void resetSearchIntimationView() {
		resetView();
	}

	@Override
	public void setAuditView(boolean auditView) {
		callCenterDashBoardUI.get().setAuditColVisible(auditView);
	}

}
