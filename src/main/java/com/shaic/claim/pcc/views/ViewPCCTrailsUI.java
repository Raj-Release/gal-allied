package com.shaic.claim.pcc.views;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.pcc.dto.ViewPCCTrailsDTO;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewPCCTrailsUI extends ViewComponent{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Instance<ViewPCCTrailsTable> viewPCCTrailsTableInst;
	
	private ViewPCCTrailsTable viewPCCTrailsTable;
	
	private VerticalLayout mainLayout;
	
	@SuppressWarnings("deprecation")
	public void init(List<ViewPCCTrailsDTO> pccTrailsDTOs){
			
		viewPCCTrailsTable = viewPCCTrailsTableInst.get();
		viewPCCTrailsTable.init("", false, false);
		viewPCCTrailsTable.setCaption("PCC Trails");
		if(pccTrailsDTOs !=null && !pccTrailsDTOs.isEmpty()){
			viewPCCTrailsTable.setTableList(pccTrailsDTOs);
		}
		mainLayout = new VerticalLayout(viewPCCTrailsTable);
		mainLayout.setMargin(true);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		 
		setCompositionRoot(mainLayout);
	}
	
	public VerticalLayout getLayout(){
		return mainLayout;
	}


}
