package com.shaic.claim.pincodemapping;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;

public class PinCodeZoneClassHistory extends ViewComponent {

	@Inject
	private Instance<ZoneClassHistoryTable> historyTable;
	
	private ZoneClassHistoryTable historyTableInstance;
	
	private VerticalLayout mainLayout;
	
	private Panel mainPanel;
	
	@EJB
	private SearchPinCodeService pinCodeService;
	
	public void initView() {

		Panel buildMainLayout = buildMainLayout();
		setCompositionRoot(buildMainLayout);
		
		

	}
	
	public VerticalLayout bindFieldGroup(Long code) {
		initView();
		setHistoryTableValues(code);
		mainLayout.setSpacing(true);
		return mainLayout;
	}
	
	private Panel buildMainLayout() {
	
		historyTableInstance = historyTable.get();
		historyTableInstance.init("", false, false);
		
		mainLayout = new VerticalLayout(historyTableInstance);
		
		mainLayout.setSpacing(true);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		
		mainPanel = new Panel();
		mainPanel.setSizeFull();
		mainPanel.setContent(mainLayout);

		return mainPanel;
		
	}
	
	private void setHistoryTableValues(Long code){
		List<ZoneClassHistoryDto> tableList1 = pinCodeService.getTableListValues(code);
		historyTableInstance.setTableList(tableList1);
		
	}
}
