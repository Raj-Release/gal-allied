package com.shaic.claim.reports.autoClosureBatch;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.table.Searchable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.VerticalLayout;

public class AutoClosureBatchViewImpl extends AbstractMVPView implements AutoClosureBatchView,Searchable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private AutoClosureForm searchShadowProvisionForm;

	@PostConstruct
	protected void initView() {
		
		searchShadowProvisionForm.init();
		
		VerticalLayout mainVertical = new VerticalLayout(searchShadowProvisionForm);
		
//		addListner();
		
		setCompositionRoot(mainVertical);
		
	}
	
	@Override
	public void init() {
		
//		searchShadowProvisionForm.init();
//		Panel mainPanel = new Panel(searchShadowProvisionForm);
//		
//		btnExportToExcel = searchShadowProvisionForm.getExportButtonToExcel();
//		
//		addListner();
//		
//		setCompositionRoot(mainPanel);


	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSearch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}

	

}
