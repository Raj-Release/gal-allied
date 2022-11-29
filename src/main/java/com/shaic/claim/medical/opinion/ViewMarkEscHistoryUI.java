package com.shaic.claim.medical.opinion;

import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.v7.ui.VerticalLayout;

public class ViewMarkEscHistoryUI extends ViewComponent{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Instance<ViewMarkEscHistoryTable> ViewMarkEscHistoryTableInst;
	
	private ViewMarkEscHistoryTable viewMarkEscHistoryTable;
	
	private VerticalLayout mainLayout;
	
	@SuppressWarnings("deprecation")
	public void init(List<ViewMarkEscHistoryDTO> viewMarkEscHistoryDTOs){
			
		viewMarkEscHistoryTable = ViewMarkEscHistoryTableInst.get();
		viewMarkEscHistoryTable.init("", false, false);
		viewMarkEscHistoryTable.setCaption("View Marketing Escalations History");
		if(viewMarkEscHistoryDTOs !=null && !viewMarkEscHistoryDTOs.isEmpty()){
			viewMarkEscHistoryTable.setTableList(viewMarkEscHistoryDTOs);
		}
		mainLayout = new VerticalLayout(viewMarkEscHistoryTable);
		mainLayout.setMargin(true);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		 
		setCompositionRoot(mainLayout);
	}
	
	public VerticalLayout getLayout(){
		return mainLayout;
	}
	
}
