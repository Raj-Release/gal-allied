package com.shaic.claim.reports.shadowProvision;

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

public class ShadowProvisionViewImpl extends AbstractMVPView implements SearchShowdowView,Searchable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private ShadowProvisionForm searchShadowProvisionForm;
	
	@Inject
	private SearchShadowProvisionTable searchShadowProvisonTableObj;
	
	private ExcelExport excelExport;
	
	private Button btnExportToExcel;
	
	
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
	
	public void addListner(){
		btnExportToExcel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
//				fireViewEvent(StarFaxSimulatePresenter.SUBMIT_CONVERT_CLAIM, txtCLoseCLaim.getValue().toString());
				DBCalculationService dbcalculationService = new DBCalculationService();
				List<SearchShadowProvisionDTO> downloadForProvisonExcel = dbcalculationService.getDownloadForProvisonExcel(new Date());
				
				searchShadowProvisonTableObj.init("", false, false);
				for (SearchShadowProvisionDTO searchShadowProvisionDTO : downloadForProvisonExcel) {
					searchShadowProvisonTableObj.addBeanToList(searchShadowProvisionDTO);
				}			
				excelExport = new  ExcelExport(searchShadowProvisonTableObj.getTable());
				excelExport.excludeCollapsedColumns();
				excelExport.setDisplayTotals(false);
				excelExport.setReportTitle("Revision of Provision");
				excelExport.export();
			
//				fireViewEvent(StarFaxSimulatePresenter.AUTO_CLOSE_CLAIM_PROCESS, txtCLoseCLaim.getValue().toString());

			}
		});
	}

	@Override
	public void doSearch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportToExcelList(
			List<SearchShadowProvisionDTO> errorLogDetailsForShadow) {
		searchShadowProvisionForm.exportToErrorLog(errorLogDetailsForShadow);
		
	}

	

}
