package com.shaic.claim.cpuautoallocation;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

public class CpuAutoAllocationSearchCriteriaViewImpl extends AbstractMVPView implements CpuAutoAllocationSearchCriteriaView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SearchCpuAutoAllocationCPUTable cpuTable;
	
	@Inject 
	private SearchCpuAutoAllocationService cpuSearchCriteriaServiceObj;
	
	private Panel panelLayout;
	
	public void initView(Window popup) {
		
//		addStyleName("view");
		
		cpuTable.init("",false,false);
		cpuTable.setWindowObject(popup);
		
		cpuTable.setTableList(cpuSearchCriteriaServiceObj.search());
		
		panelLayout = new Panel(cpuTable);
		panelLayout.setWidth("100%");
		
		setCompositionRoot(panelLayout);
		
	}

	
//	public void setWindowObject(Window popup){
//		this.popup = popup;
//		doctorTable.setWindowObject(popup);
//	}
//	
//	public void setPresenterString(String presenterString)
//	{
//		this.presenterString = presenterString;
//	}
	
	
	
}
